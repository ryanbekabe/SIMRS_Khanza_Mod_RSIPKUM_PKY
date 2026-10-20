<?php
// Cetak Surat Keterangan Sakit (padanan rptSuratSakit5.jrxml). Cetak/simpan PDF lewat Ctrl+P browser.
declare(strict_types=1);
require_once __DIR__ . '/config.php';

$no = trim($_GET['no_surat'] ?? '');
$st = db()->prepare(
    "select s.no_surat,s.tanggalawal,s.tanggalakhir,s.lamasakit,r.no_rawat,r.kd_dokter,
            date_format(r.tgl_registrasi,'%d-%m-%Y') as tgl_registrasi,
            d.nm_dokter,d.no_ijn_praktek,p.nm_pasien,p.jk,p.pekerjaan,
            concat(r.umurdaftar,' ',r.sttsumur) as umur,
            concat_ws(', ',p.alamat,kel.nm_kel,kec.nm_kec,kab.nm_kab) as alamat
     from suratsakit s
     inner join reg_periksa r on s.no_rawat=r.no_rawat
     inner join pasien p on r.no_rkm_medis=p.no_rkm_medis
     inner join dokter d on r.kd_dokter=d.kd_dokter
     left join kelurahan kel on p.kd_kel=kel.kd_kel
     left join kecamatan kec on p.kd_kec=kec.kd_kec
     left join kabupaten kab on p.kd_kab=kab.kd_kab
     where s.no_surat=?"
);
$st->execute([$no]);
$d = $st->fetch();
if (!$d) {
    http_response_code(404);
    exit('Surat sakit tidak ditemukan.');
}

// Diagnosa utama (prioritas 1), sama seperti aplikasi Java; hanya nama penyakit yang ditampilkan.
$dg = db()->prepare(
    'select penyakit.nm_penyakit from diagnosa_pasien
     inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit
     where diagnosa_pasien.no_rawat=? and diagnosa_pasien.prioritas=1 limit 1'
);
$dg->execute([$d['no_rawat']]);
$diagnosa = (string)$dg->fetchColumn();

$rs = instansi();
$fp = db()->prepare(
    'select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?'
);
$fp->execute([$d['kd_dokter']]);
$finger = (string)$fp->fetchColumn();

$qr = 'Dikeluarkan di ' . $rs['nama_instansi'] . ', Kabupaten/Kota ' . $rs['kabupaten']
    . "\nDitandatangani secara elektronik oleh " . $d['nm_dokter']
    . "\nID " . ($finger !== '' ? $finger : $d['kd_dokter'])
    . "\n" . $d['tgl_registrasi'];

// KOP: hasil upload (kop.php); jika belum ada, pakai logo dari database.
$kop = kop_aktif()['url'] ?? 'logo.php';
$tampilBasmalah = kop_opsi()['basmalah'];

function tgl_panjang(string $ymd): string
{
    static $bln = [1 => 'Januari', 'Februari', 'Maret', 'April', 'Mei', 'Juni', 'Juli', 'Agustus', 'September', 'Oktober', 'November', 'Desember'];
    $t = strtotime($ymd);
    return date('j', $t) . ' ' . $bln[(int)date('n', $t)] . ' ' . date('Y', $t);
}

$lama = trim(preg_replace('/\s*hari\s*$/i', '', $d['lamasakit']));
$ttdTgl = $rs['kabupaten'] . ', ' . tgl_panjang(date('Y-m-d', strtotime($d['tgl_registrasi'])));
?>
<!doctype html>
<html lang="id">
<head>
<meta charset="utf-8">
<title>Surat Sakit <?= e($d['no_surat']) ?></title>
<style>
  @page { size: A4; margin: 0; } /* margin 0 = header/footer browser (tanggal, judul, URL) tidak tercetak */
  body { margin:0; background:#ddd; font:12pt/1.5 "Times New Roman",Times,serif; color:#000; }
  .page { width:178mm; min-height:130mm; margin:10px auto; padding:6mm 0; background:#fff; }
  .kop { display:block; width:100%; height:auto; margin-bottom:6mm; }
  .bism { text-align:center; font-size:20pt; line-height:1.2; margin:0 0 8mm; }
  h1 { text-align:center; font-size:14pt; text-decoration:underline; margin:0; letter-spacing:.3px; }
  .no { text-align:center; margin:0 0 9mm; }
  table.id { margin:5mm 0 4mm; border-collapse:collapse; }
  table.id td { padding:1.2mm 0; vertical-align:top; }
  table.id td:nth-child(1) { width:38mm; } table.id td:nth-child(2) { width:4mm; }
  p { margin:0 0 4mm; text-align:justify; }
  .ttd { width:max-content; min-width:75mm; max-width:100%; margin:10mm 0 0 auto; text-align:center; }
  .sip { white-space:nowrap; font-size:11pt; }
  #qr { margin:2mm auto; width:fit-content; min-height:2mm; }
  #qr img { width:26mm; height:26mm; }
  .dr { font-weight:bold; text-decoration:underline; }
  .tools { text-align:center; margin:10px; } .tools button { padding:6px 16px; }
  @media print { body { background:#fff; } .page { margin:0; padding:12mm 16mm; width:auto; min-height:0; box-sizing:border-box; } .tools { display:none; } }
</style>
</head>
<body>
<div class="tools"><button onclick="print()">Cetak / Simpan PDF</button></div>
<div class="page">
  <img class="kop" src="<?= e($kop) ?>" alt="" onerror="this.style.display='none'">
  <?php if ($tampilBasmalah): ?><div class="bism" lang="ar" dir="rtl">بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ</div><?php endif; ?>
  <h1>SURAT KETERANGAN SAKIT</h1>
  <div class="no">Nomor : <?= e($d['no_surat']) ?></div>

  <p>Yang bertanda tangan di bawah ini menerangkan bahwa:</p>
  <table class="id">
    <tr><td>Nama Pasien</td><td>:</td><td><b><?= e($d['nm_pasien']) ?></b></td></tr>
    <tr><td>Usia</td><td>:</td><td><?= e($d['umur']) ?></td></tr>
    <tr><td>Jenis Kelamin</td><td>:</td><td><?= $d['jk'] === 'L' ? 'Laki-Laki' : 'Perempuan' ?></td></tr>
    <tr><td>Pekerjaan</td><td>:</td><td><?= e($d['pekerjaan']) ?></td></tr>
    <tr><td>Alamat</td><td>:</td><td><?= e($d['alamat']) ?></td></tr>
    <?php if ($diagnosa !== ''): ?><tr><td>Diagnosa</td><td>:</td><td><?= e($diagnosa) ?></td></tr><?php endif; ?>
  </table>

  <p style="margin-bottom:1mm">Mengingat kondisinya saat ini maka memerlukan :</p>
  <p>Istirahat selama : <b><?= e($lama) ?></b> Hari sejak tanggal <b><?= e(tgl_panjang($d['tanggalawal'])) ?></b>
     s/d <b><?= e(tgl_panjang($d['tanggalakhir'])) ?></b>.</p>
  <p style="margin-top:7mm">Demikian surat keterangan ini dibuat dengan sesungguhnya dan harap dapat digunakan sebagaimana perlunya.</p>

  <div class="ttd">
    <?= e($ttdTgl) ?><br>
    Dokter Pemeriksa
    <div id="qr"></div>
    <span class="dr"><?= e($d['nm_dokter']) ?></span><br>
  </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/qrcode-generator/1.4.4/qrcode.min.js"></script>
<script>
  if (window.qrcode) {
    const q = qrcode(0, 'H');
    q.addData(<?= json_encode($qr, JSON_UNESCAPED_UNICODE | JSON_HEX_TAG) ?>, 'Byte');
    q.make();
    document.getElementById('qr').innerHTML = q.createImgTag(4, 0);
  }
</script>
</body>
</html>
