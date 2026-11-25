<?php
declare(strict_types=1);
/**
 * Pengganti dialog cari Kamar/Bangsal, Poliklinik, Dokter, Cara Bayar,
 * Kabupaten, Kecamatan, Kelurahan. Mengembalikan JSON daftar nama.
 */
require __DIR__ . '/lib.php';
require_login();

header('Content-Type: application/json; charset=utf-8');

// jenis => [tabel, kolom nama, kolom kode, punya kolom status]
$map = [
    'kamar'     => ['bangsal',   'nm_bangsal', 'kd_bangsal', true],
    'poli'      => ['poliklinik', 'nm_poli',   'kd_poli',    true],
    'dokter'    => ['dokter',    'nm_dokter',  'kd_dokter',  true],
    'penjab'    => ['penjab',    'png_jawab',  'kd_pj',      true],
    'kabupaten' => ['kabupaten', 'nm_kab',     'kd_kab',     false],
    'kecamatan' => ['kecamatan', 'nm_kec',     'kd_kec',     false],
    'kelurahan' => ['kelurahan', 'nm_kel',     'kd_kel',     false],
];

$jenis = (string) ($_GET['jenis'] ?? '');
if (!isset($map[$jenis])) {
    http_response_code(400);
    echo json_encode(['error' => 'jenis tidak valid']);
    exit;
}
[$table, $nama, $kode, $hasStatus] = $map[$jenis];
$q = like(mb_substr(trim((string) ($_GET['q'] ?? '')), 0, 60));

$where = "($nama LIKE ? OR $kode LIKE ?)" . ($hasStatus ? " AND status = '1'" : '');

try {
    if ($jenis === 'dokter') {
        // Dokter dicari berdasarkan nama, kode, atau nama spesialis
        // (mis. ketik "bedah" untuk menampilkan semua dokter bedah).
        $st = db()->prepare("SELECT d.kd_dokter AS kode, d.nm_dokter AS nama, COALESCE(s.nm_sps, '') AS spesialis
                             FROM dokter d LEFT JOIN spesialis s ON s.kd_sps = d.kd_sps
                             WHERE d.status = '1' AND (d.nm_dokter LIKE ? OR d.kd_dokter LIKE ? OR s.nm_sps LIKE ?)
                             ORDER BY s.nm_sps, d.nm_dokter LIMIT 100");
        $st->execute([$q, $q, $q]);
        echo json_encode($st->fetchAll(), JSON_UNESCAPED_UNICODE);
        exit;
    }

    $st = db()->prepare("SELECT $kode AS kode, $nama AS nama FROM $table WHERE $where ORDER BY $nama LIMIT 50");
    $st->execute([$q, $q]);
    echo json_encode($st->fetchAll(), JSON_UNESCAPED_UNICODE);
} catch (PDOException $ex) {
    error_log('Lookup error: ' . $ex->getMessage());
    http_response_code(500);
    echo json_encode(['error' => 'Gagal mengambil data']);
}
