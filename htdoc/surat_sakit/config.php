<?php
// Konfigurasi koneksi & helper — Surat Keterangan Sakit (versi PHP)
declare(strict_types=1);
session_start();

const DB_HOST = '10.8.0.2';
const DB_NAME = 'sik';
const DB_PORT = 3306;
const DB_USER = 'pku1912';
const DB_PASS = 'pku1912';

function db(): PDO
{
    static $pdo = null;
    if ($pdo === null) {
        $pdo = new PDO(
            'mysql:host=' . DB_HOST . ';port=' . DB_PORT . ';dbname=' . DB_NAME . ';charset=utf8mb4',
            DB_USER,
            DB_PASS,
            [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES => false,
            ]
        );
    }
    return $pdo;
}

function e(?string $s): string
{
    return htmlspecialchars((string)$s, ENT_QUOTES, 'UTF-8');
}

function csrf_token(): string
{
    if (empty($_SESSION['csrf'])) {
        $_SESSION['csrf'] = bin2hex(random_bytes(16));
    }
    return $_SESSION['csrf'];
}

function csrf_check(): void
{
    if (!hash_equals($_SESSION['csrf'] ?? '', (string)($_POST['csrf'] ?? ''))) {
        http_response_code(419);
        exit('Token tidak valid, muat ulang halaman.');
    }
}

function tgl_id(?string $ymd): string
{
    return $ymd ? date('d-m-Y', strtotime($ymd)) : '';
}

function valid_date(string $d): bool
{
    $x = DateTime::createFromFormat('Y-m-d', $d);
    return $x && $x->format('Y-m-d') === $d;
}

/** Data identitas rumah sakit (tabel setting), tanpa logo. */
function instansi(): array
{
    static $r = null;
    if ($r === null) {
        $r = db()->query(
            'select nama_instansi,alamat_instansi,kabupaten,propinsi,kontak,email from setting limit 1'
        )->fetch() ?: [];
    }
    return $r;
}

/** Nomor surat otomatis: SKS + yyyymmdd + urut 3 digit (per tanggal awal), sama seperti aplikasi Java. */
function nomor_baru(string $tglAwal): string
{
    $prefix = 'SKS' . str_replace('-', '', $tglAwal);
    $st = db()->prepare(
        'select ifnull(max(convert(right(no_surat,3),signed)),0) from suratsakit where tanggalawal=?'
    );
    $st->execute([$tglAwal]);
    return $prefix . str_pad((string)((int)$st->fetchColumn() + 1), 3, '0', STR_PAD_LEFT);
}

const KOP_DIR = __DIR__ . '/kop';

/** Berkas KOP yang aktif (hasil upload), atau null. Dikembalikan sebagai ['file'=>path, 'url'=>relatif]. */
function kop_aktif(): ?array
{
    foreach (['jpg', 'png', 'webp'] as $ext) {
        $f = KOP_DIR . "/KOP_Surat.$ext";
        if (is_file($f)) {
            return ['file' => $f, 'url' => "kop/KOP_Surat.$ext?v=" . filemtime($f)];
        }
    }
    // kompatibel dengan KOP_Surat.* yang diletakkan manual di folder aplikasi
    foreach (['jpg', 'jpeg', 'png', 'webp'] as $ext) {
        $f = __DIR__ . "/KOP_Surat.$ext";
        if (is_file($f)) {
            return ['file' => $f, 'url' => "KOP_Surat.$ext?v=" . filemtime($f)];
        }
    }
    return null;
}

function kop_opsi(): array
{
    $o = ['basmalah' => true];
    $f = KOP_DIR . '/opsi.json';
    if (is_file($f)) {
        $j = json_decode((string)file_get_contents($f), true);
        if (is_array($j)) {
            $o = array_merge($o, $j);
        }
    }
    return $o;
}
