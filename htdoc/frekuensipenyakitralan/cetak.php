<?php
declare(strict_types=1);
/**
 * Pengganti BtnPrint + billing/LaporanPenyakitRalan.php.
 * Tidak lagi menulis ke tabel `temporary`; laporan langsung dirender.
 */
require __DIR__ . '/lib.php';
require_login();

$f = Filter::fromRequest($_GET);
$data = ambil_data($f);

$sortable = array_keys(KOLOM);
$sort = in_array($_GET['sort'] ?? '', $sortable, true) ? $_GET['sort'] : 'kd_penyakit';
$dir  = ($_GET['dir'] ?? 'asc') === 'desc' ? -1 : 1;
usort($data, fn(array $a, array $b): int => $dir * (is_int($a[$sort]) ? $a[$sort] <=> $b[$sort] : strnatcasecmp($a[$sort], $b[$sort])));

$total = array_fill_keys(['lk_hidup', 'pr_hidup', 'lk_mati', 'pr_mati', 'jumlah'], 0);
foreach ($data as $r) {
    foreach ($total as $k => $_) {
        $total[$k] += $r[$k];
    }
}

$filterInfo = array_filter([
    'Poli'      => $f->poli,
    'Dokter'    => $f->dokter,
    'Cara Bayar' => $f->penjab,
    'Kabupaten' => $f->kabupaten,
    'Kecamatan' => $f->kecamatan,
    'Kelurahan' => $f->kelurahan,
    'Kata Kunci' => $f->cari,
], fn($v) => $v !== '');
?>
<!doctype html>
<html lang="id">
<head>
<meta charset="utf-8">
<title>Laporan Frekuensi Penyakit Rawat Jalan</title>
<style>
    body { font: 11px Tahoma, Arial, sans-serif; color: #000; margin: 16px; }
    h2, h3 { text-align: center; margin: 2px 0; }
    .info { text-align: center; margin-bottom: 10px; }
    table { width: 100%; border-collapse: collapse; }
    th, td { border: 1px solid #000; padding: 3px 4px; vertical-align: top; }
    th { background: #eee; }
    td.n { text-align: right; }
    tfoot td { font-weight: bold; }
    .noprint { margin-bottom: 12px; }
    @media print { .noprint { display: none; } body { margin: 0; } }
</style>
</head>
<body>
<div class="noprint">
    <button onclick="window.print()">Cetak</button>
    <button onclick="window.close()">Tutup</button>
</div>
<h2><?= e(config('nama_instansi')) ?></h2>
<h3>LAPORAN FREKUENSI PENYAKIT RAWAT JALAN</h3>
<div class="info">
    Periode <?= e(tgl_indo($f->tgl1)) ?> s.d. <?= e(tgl_indo($f->tgl2)) ?>
    <?php foreach ($filterInfo as $k => $v): ?><br><?= e($k) ?> : <?= e($v) ?><?php endforeach; ?>
</div>
<table>
    <thead>
    <tr>
        <th>No</th>
        <?php foreach (KOLOM as $label): ?><th><?= e($label) ?></th><?php endforeach; ?>
    </tr>
    </thead>
    <tbody>
    <?php foreach ($data as $i => $r): ?>
        <tr>
            <td class="n"><?= $i + 1 ?></td>
            <td><?= e($r['kd_penyakit']) ?></td>
            <td><?= e($r['penyakit']) ?></td>
            <td><?= e($r['diagnosa_lain']) ?></td>
            <td class="n"><?= $r['lk_hidup'] ?></td>
            <td class="n"><?= $r['pr_hidup'] ?></td>
            <td class="n"><?= $r['lk_mati'] ?></td>
            <td class="n"><?= $r['pr_mati'] ?></td>
            <td class="n"><?= $r['jumlah'] ?></td>
        </tr>
    <?php endforeach; ?>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="4">Total</td>
        <?php foreach ($total as $v): ?><td class="n"><?= $v ?></td><?php endforeach; ?>
    </tr>
    </tfoot>
</table>
<p>Dicetak oleh <?= e($_SESSION['auth']['nama']) ?> pada <?= date('d-m-Y H:i:s') ?></p>
<script>window.addEventListener('load', () => window.print());</script>
</body>
</html>
