<?php
// Endpoint JSON kecil untuk form: lookup pasien dari No.Rawat & nomor surat otomatis (hanya baca).
declare(strict_types=1);
require_once __DIR__ . '/config.php';
header('Content-Type: application/json; charset=utf-8');

try {
    $action = $_GET['action'] ?? '';
    if ($action === 'rawat') {
        $st = db()->prepare(
            'select reg_periksa.no_rkm_medis,pasien.nm_pasien from reg_periksa
             inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis
             where reg_periksa.no_rawat=?'
        );
        $st->execute([trim($_GET['no_rawat'] ?? '')]);
        echo json_encode($st->fetch() ?: null);
    } elseif ($action === 'nomor') {
        $tgl = $_GET['tgl'] ?? '';
        echo json_encode(['no_surat' => valid_date($tgl) ? nomor_baru($tgl) : '']);
    } else {
        http_response_code(400);
        echo json_encode(['error' => 'aksi tidak dikenal']);
    }
} catch (Throwable $ex) {
    http_response_code(500);
    echo json_encode(['error' => 'Kesalahan server']);
}
