<?php
declare(strict_types=1);

/*
 * Port PHP 8.2 dari laporan/DlgFrekuensiPenyakitRanap.java
 *
 * Dua mode, sama dengan dua tab di versi Java:
 *  - masuk  : "Berdasar Tanggal Masuk"  -> tampil()  (reg_periksa.tgl_registrasi)
 *  - keluar : "Berdasar Tanggal Keluar" -> tampil2() (kamar_inap.tgl_keluar,
 *             stts_pulang <> 'Pindah Kamar')
 *
 * Versi Java menjalankan 1 query utama + 5 query per penyakit (N+1) dan
 * menulis ke tabel temporary_surveilens_penyakit. Di sini semua dihitung
 * dengan 2 query agregat tanpa menulis ke database.
 */

session_name('FPRANAPSESS'); // terpisah dari sesi aplikasi Ralan
session_start([
    'cookie_httponly' => true,
    'cookie_samesite' => 'Lax',
    'use_strict_mode' => true,
]);

$CONFIG = require __DIR__ . '/config.php';

function config(string $key): mixed
{
    global $CONFIG;
    return $CONFIG[$key] ?? null;
}

function db(): PDO
{
    static $pdo = null;
    if ($pdo instanceof PDO) {
        return $pdo;
    }
    $c = config('db');
    $dsn = sprintf('mysql:host=%s;port=%d;dbname=%s;charset=%s', $c['host'], $c['port'], $c['name'], $c['charset']);
    $pdo = new PDO($dsn, $c['user'], $c['pass'], [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ]);
    $pdo->exec('SET SESSION group_concat_max_len = 1000000');
    return $pdo;
}

function e(mixed $v): string
{
    return htmlspecialchars((string) $v, ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8');
}

function csrf_token(): string
{
    if (empty($_SESSION['csrf'])) {
        $_SESSION['csrf'] = bin2hex(random_bytes(32));
    }
    return $_SESSION['csrf'];
}

function csrf_check(?string $token): bool
{
    return is_string($token) && hash_equals($_SESSION['csrf'] ?? '', $token);
}

/* ------------------------------------------------------------------ */
/* Otentikasi (sama dengan fungsi/akses.java)                          */
/* ------------------------------------------------------------------ */

/**
 * @return array{nama:string, akses:bool}|null
 */
function login_attempt(string $user, string $pass): ?array
{
    $ku = config('aes_user_key');
    $kp = config('aes_pass_key');

    $st = db()->prepare('SELECT 1 FROM admin WHERE usere = AES_ENCRYPT(?, ?) AND passworde = AES_ENCRYPT(?, ?)');
    $st->execute([$user, $ku, $pass, $kp]);
    if ($st->fetchColumn()) {
        return ['nama' => 'Admin Utama', 'akses' => true];
    }

    $st = db()->prepare('SELECT penyakit_ranap FROM user WHERE id_user = AES_ENCRYPT(?, ?) AND password = AES_ENCRYPT(?, ?)');
    $st->execute([$user, $ku, $pass, $kp]);
    $row = $st->fetch();
    if ($row) {
        return ['nama' => $user, 'akses' => $row['penyakit_ranap'] === 'true'];
    }
    return null;
}

function require_login(): array
{
    if (empty($_SESSION['auth'])) {
        header('Location: login.php');
        exit;
    }
    if (empty($_SESSION['auth']['akses'])) {
        http_response_code(403);
        exit('Maaf, Anda tidak memiliki hak akses ke menu Frekuensi Penyakit Ranap.');
    }
    return $_SESSION['auth'];
}

/* ------------------------------------------------------------------ */
/* Filter                                                              */
/* ------------------------------------------------------------------ */

enum Mode: string
{
    case Masuk  = 'masuk';
    case Keluar = 'keluar';

    public function label(): string
    {
        return match ($this) {
            self::Masuk  => 'Berdasar Tanggal Masuk',
            self::Keluar => 'Berdasar Tanggal Keluar',
        };
    }
}

final readonly class Filter
{
    public function __construct(
        public Mode $mode,
        public string $tgl1,
        public string $tgl2,
        public string $kamar = '',
        public string $poli = '',
        public string $dokter = '',
        public string $penjab = '',
        public string $kabupaten = '',
        public string $kecamatan = '',
        public string $kelurahan = '',
        public string $cari = '',
    ) {}

    public static function fromRequest(array $q): self
    {
        $today = date('Y-m-d');
        $str = static fn(string $k, int $max = 100): string => mb_substr(trim((string) ($q[$k] ?? '')), 0, $max);
        $tgl = static function (string $k) use ($q, $today): string {
            $v = (string) ($q[$k] ?? '');
            $d = DateTimeImmutable::createFromFormat('!Y-m-d', $v);
            return ($d && $d->format('Y-m-d') === $v) ? $v : $today;
        };

        return new self(
            mode: Mode::tryFrom((string) ($q['mode'] ?? '')) ?? Mode::Masuk,
            tgl1: $tgl('tgl1'),
            tgl2: $tgl('tgl2'),
            kamar: $str('kamar'),
            poli: $str('poli'),
            dokter: $str('dokter'),
            penjab: $str('penjab'),
            kabupaten: $str('kabupaten'),
            kecamatan: $str('kecamatan'),
            kelurahan: $str('kelurahan'),
            cari: $str('cari', 90), // batasInput 90 karakter di versi Java
        );
    }

    /** @param array<string,string> $override */
    public function toQuery(array $override = []): string
    {
        $v = get_object_vars($this);
        $v['mode'] = $this->mode->value;
        return http_build_query(array_merge($v, $override));
    }
}

/** Escape karakter wildcard LIKE agar input dicari apa adanya. */
function like(string $s): string
{
    return '%' . addcslashes($s, '\\%_') . '%';
}

/**
 * FROM + WHERE yang setara dengan query di tampil()/tampil2() versi Java.
 *
 * @return array{0:string, 1:list<string>}
 */
function base_from_where(Filter $f, string $extraJoin = ''): array
{
    // Filter Poliklinik (tambahan, tidak ada di versi Java): join hanya bila
    // dipakai, agar hasil tanpa filter tetap identik dengan versi Java.
    if ($f->poli !== '') {
        $extraJoin = "INNER JOIN poliklinik pl ON rp.kd_poli = pl.kd_poli\n            " . $extraJoin;
    }

    $sql = "FROM diagnosa_pasien dp
            INNER JOIN penyakit    p  ON p.kd_penyakit   = dp.kd_penyakit
            INNER JOIN reg_periksa rp ON rp.no_rawat     = dp.no_rawat
            INNER JOIN pasien      ps ON rp.no_rkm_medis = ps.no_rkm_medis
            INNER JOIN kamar_inap  ki ON ki.no_rawat     = rp.no_rawat
            INNER JOIN kamar       km ON ki.kd_kamar     = km.kd_kamar
            INNER JOIN bangsal     bs ON km.kd_bangsal   = bs.kd_bangsal
            INNER JOIN dokter      d  ON rp.kd_dokter    = d.kd_dokter
            INNER JOIN penjab      pj ON rp.kd_pj        = pj.kd_pj
            INNER JOIN kabupaten   kb ON ps.kd_kab       = kb.kd_kab
            INNER JOIN kecamatan   kc ON ps.kd_kec       = kc.kd_kec
            INNER JOIN kelurahan   kl ON ps.kd_kel       = kl.kd_kel
            $extraJoin
            WHERE rp.status_lanjut = 'Ranap'
              AND dp.status = 'Ranap'
              AND dp.prioritas = '1' ";

    $sql .= match ($f->mode) {
        Mode::Masuk  => ' AND rp.tgl_registrasi BETWEEN ? AND ? ',
        Mode::Keluar => " AND ki.stts_pulang <> 'Pindah Kamar' AND ki.tgl_keluar BETWEEN ? AND ? ",
    };
    $params = [$f->tgl1, $f->tgl2];

    // Versi Java memakai LIKE '%...%' untuk semua filter
    foreach ([
        'bs.nm_bangsal' => $f->kamar,
        'pl.nm_poli'    => $f->poli,
        'pj.png_jawab'  => $f->penjab,
        'kb.nm_kab'     => $f->kabupaten,
        'kc.nm_kec'     => $f->kecamatan,
        'kl.nm_kel'     => $f->kelurahan,
    ] as $col => $val) {
        if ($val !== '') {
            $sql .= " AND $col LIKE ? ";
            $params[] = like($val);
        }
    }

    // Dokter: di Ranap, reg_periksa.kd_dokter hanya dokter saat pendaftaran
    // (dokter IGD/poli asal). Dokter yang merawat (DPJP) ada di dpjp_ranap,
    // jadi keduanya dicocokkan.
    if ($f->dokter !== '') {
        $sql .= ' AND (d.nm_dokter LIKE ?
                       OR EXISTS (SELECT 1 FROM dpjp_ranap dr
                                  INNER JOIN dokter d2 ON d2.kd_dokter = dr.kd_dokter
                                  WHERE dr.no_rawat = rp.no_rawat AND d2.nm_dokter LIKE ?)) ';
        $params[] = like($f->dokter);
        $params[] = like($f->dokter);
    }

    if ($f->cari !== '') {
        $sql .= ' AND (p.kd_penyakit LIKE ? OR p.nm_penyakit LIKE ?) ';
        $params[] = like($f->cari);
        $params[] = like($f->cari);
    }
    return [$sql, $params];
}

/**
 * @return list<array{kd_penyakit:string, penyakit:string, diagnosa_lain:string,
 *                    lk_hidup:int, pr_hidup:int, lk_mati:int, pr_mati:int, jumlah:int}>
 */
function ambil_data(Filter $f): array
{
    [$fromWhere, $params] = base_from_where($f);
    [$fromWhereMati] = base_from_where($f, 'LEFT JOIN pasien_mati pm ON pm.no_rkm_medis = ps.no_rkm_medis');

    // jumlah = i; lk_mati/pr_mati = a/b; hidup = total - mati (c/d), sama dengan Java.
    $sql = "SELECT p.kd_penyakit,
                   SUBSTRING(p.nm_penyakit, 1, 80) AS penyakit,
                   COUNT(DISTINCT dp.no_rawat) AS jumlah,
                   COUNT(DISTINCT CASE WHEN ps.jk = 'L' THEN dp.no_rawat END) AS total_lk,
                   COUNT(DISTINCT CASE WHEN ps.jk = 'P' THEN dp.no_rawat END) AS total_pr,
                   COUNT(DISTINCT CASE WHEN ps.jk = 'L' AND pm.no_rkm_medis IS NOT NULL THEN dp.no_rawat END) AS lk_mati,
                   COUNT(DISTINCT CASE WHEN ps.jk = 'P' AND pm.no_rkm_medis IS NOT NULL THEN dp.no_rawat END) AS pr_mati
            $fromWhereMati
            GROUP BY p.kd_penyakit
            ORDER BY p.kd_penyakit";
    $st = db()->prepare($sql);
    $st->execute($params);
    $rows = $st->fetchAll();
    if (!$rows) {
        return [];
    }

    // Diagnosa sekunder (prioritas <> 1) pada no_rawat yang sama
    $sql2 = "SELECT x.kd_penyakit,
                    GROUP_CONCAT(DISTINCT d2.kd_penyakit ORDER BY d2.kd_penyakit SEPARATOR ', ') AS lain
             FROM (SELECT DISTINCT dp.kd_penyakit, dp.no_rawat $fromWhere) x
             INNER JOIN diagnosa_pasien d2
                     ON d2.no_rawat = x.no_rawat AND d2.prioritas <> '1' AND d2.status = 'Ranap'
             GROUP BY x.kd_penyakit";
    $st2 = db()->prepare($sql2);
    $st2->execute($params);
    $lain = $st2->fetchAll(PDO::FETCH_KEY_PAIR);

    $hasil = [];
    foreach ($rows as $r) {
        $hasil[] = [
            'kd_penyakit'   => (string) $r['kd_penyakit'],
            'penyakit'      => (string) $r['penyakit'],
            'diagnosa_lain' => (string) ($lain[$r['kd_penyakit']] ?? ''),
            'lk_hidup'      => (int) $r['total_lk'] - (int) $r['lk_mati'],
            'pr_hidup'      => (int) $r['total_pr'] - (int) $r['pr_mati'],
            'lk_mati'       => (int) $r['lk_mati'],
            'pr_mati'       => (int) $r['pr_mati'],
            'jumlah'        => (int) $r['jumlah'],
        ];
    }
    return $hasil;
}

const KOLOM = [
    'kd_penyakit'   => 'Kode',
    'penyakit'      => 'Nama Penyakit',
    'diagnosa_lain' => 'Diagnosa Lain',
    'lk_hidup'      => 'Lk2(Hidup)',
    'pr_hidup'      => 'Pr(Hidup)',
    'lk_mati'       => 'Lk2(Mati)',
    'pr_mati'       => 'Pr(Mati)',
    'jumlah'        => 'Jumlah',
];

function tgl_indo(string $ymd): string
{
    $d = DateTimeImmutable::createFromFormat('!Y-m-d', $ymd);
    return $d ? $d->format('d-m-Y') : $ymd;
}
