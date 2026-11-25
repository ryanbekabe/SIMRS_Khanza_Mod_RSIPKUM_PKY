<?php
declare(strict_types=1);
/** Ekspor hasil ke CSV (bisa dibuka di Excel). */
require __DIR__ . '/lib.php';
require_login();

$f = Filter::fromRequest($_GET);
$data = ambil_data($f);

$nama = sprintf('FrekuensiPenyakitRalan_%s_sd_%s.csv', $f->tgl1, $f->tgl2);
header('Content-Type: text/csv; charset=utf-8');
header('Content-Disposition: attachment; filename="' . $nama . '"');

$out = fopen('php://output', 'w');
fwrite($out, "\xEF\xBB\xBF"); // BOM agar Excel membaca UTF-8
fputcsv($out, array_values(KOLOM), ';');
foreach ($data as $row) {
    fputcsv($out, array_values($row), ';');
}
fclose($out);
