<?php
declare(strict_types=1);
/**
 * Pengganti dialog DlgCariPoli, DlgCariDokter, DlgCariCaraBayar,
 * DlgKabupaten, DlgKecamatan, DlgKelurahan. Mengembalikan JSON daftar nama.
 */
require __DIR__ . '/lib.php';
require_login();

header('Content-Type: application/json; charset=utf-8');

$map = [
    'poli'      => ['poliklinik', 'nm_poli',   'kd_poli'],
    'dokter'    => ['dokter',     'nm_dokter', 'kd_dokter'],
    'penjab'    => ['penjab',     'png_jawab', 'kd_pj'],
    'kabupaten' => ['kabupaten',  'nm_kab',    'kd_kab'],
    'kecamatan' => ['kecamatan',  'nm_kec',    'kd_kec'],
    'kelurahan' => ['kelurahan',  'nm_kel',    'kd_kel'],
];

$jenis = (string) ($_GET['jenis'] ?? '');
if (!isset($map[$jenis])) {
    http_response_code(400);
    echo json_encode(['error' => 'jenis tidak valid']);
    exit;
}
[$table, $nama, $kode] = $map[$jenis];
$q = '%' . mb_substr(trim((string) ($_GET['q'] ?? '')), 0, 60) . '%';

$where = "($nama LIKE ? OR $kode LIKE ?)";
// Hanya tampilkan data aktif bila kolom status tersedia
if (in_array($jenis, ['poli', 'dokter', 'penjab'], true)) {
    $where .= " AND status = '1'";
}

try {
    $st = db()->prepare("SELECT $kode AS kode, $nama AS nama FROM $table WHERE $where ORDER BY $nama LIMIT 50");
    $st->execute([$q, $q]);
    echo json_encode($st->fetchAll(), JSON_UNESCAPED_UNICODE);
} catch (PDOException $ex) {
    error_log('Lookup error: ' . $ex->getMessage());
    http_response_code(500);
    echo json_encode(['error' => 'Gagal mengambil data']);
}
