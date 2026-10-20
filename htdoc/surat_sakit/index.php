<?php
// Data Surat Keterangan Sakit — daftar, simpan, ganti, hapus, cetak
declare(strict_types=1);
require_once __DIR__ . '/config.php';

$pesan = $_SESSION['pesan'] ?? '';
unset($_SESSION['pesan']);

function kembali(string $msg): never
{
    $_SESSION['pesan'] = $msg;
    header('Location: index.php?' . http_build_query([
        'tgl1' => $_POST['tgl1'] ?? '', 'tgl2' => $_POST['tgl2'] ?? '', 'q' => $_POST['q'] ?? '',
    ]));
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    csrf_check();
    $aksi = $_POST['aksi'] ?? '';
    $no = trim($_POST['no_surat'] ?? '');
    $lama = trim($_POST['lamasakit'] ?? '');
    $rawat = trim($_POST['no_rawat'] ?? '');
    $awal = $_POST['tanggalawal'] ?? '';
    $akhir = $_POST['tanggalakhir'] ?? '';
    $lama_asli = trim($_POST['no_surat_lama'] ?? '');

    try {
        if ($aksi === 'hapus') {
            db()->prepare('delete from suratsakit where no_surat=?')->execute([$lama_asli]);
            kembali("Surat $lama_asli dihapus.");
        }
        if ($no === '' || strlen($no) > 30) {
            kembali('No.Surat Sakit wajib diisi (maks. 30 karakter).');
        }
        if ($lama === '' || strlen($lama) > 20) {
            kembali('Lama sakit wajib diisi (maks. 20 karakter).');
        }
        if (!valid_date($awal) || !valid_date($akhir) || $akhir < $awal) {
            kembali('Tanggal tidak valid (tanggal akhir tidak boleh sebelum tanggal awal).');
        }
        $cek = db()->prepare('select count(*) from reg_periksa where no_rawat=?');
        $cek->execute([$rawat]);
        if (!$cek->fetchColumn()) {
            kembali('No.Rawat tidak ditemukan, pilih pasien yang valid.');
        }
        if ($aksi === 'simpan') {
            db()->prepare('insert into suratsakit values (?,?,?,?,?)')
                ->execute([$no, $rawat, $awal, $akhir, $lama]);
            kembali("Surat $no disimpan.");
        } elseif ($aksi === 'ganti') {
            db()->prepare(
                'update suratsakit set no_surat=?,no_rawat=?,tanggalawal=?,tanggalakhir=?,lamasakit=? where no_surat=?'
            )->execute([$no, $rawat, $awal, $akhir, $lama, $lama_asli]);
            kembali("Surat $no diperbarui.");
        }
    } catch (PDOException $ex) {
        kembali($ex->getCode() === '23000' ? 'No.Surat Sakit sudah ada.' : 'Gagal menyimpan data.');
    }
    kembali('');
}

$tgl1 = valid_date($_GET['tgl1'] ?? '') ? $_GET['tgl1'] : date('Y-m-d');
$tgl2 = valid_date($_GET['tgl2'] ?? '') ? $_GET['tgl2'] : date('Y-m-d');
$q = trim($_GET['q'] ?? '');

$sql = 'select s.no_surat,s.no_rawat,r.no_rkm_medis,p.nm_pasien,s.tanggalawal,s.tanggalakhir,s.lamasakit
        from suratsakit s
        inner join reg_periksa r on s.no_rawat=r.no_rawat
        inner join pasien p on r.no_rkm_medis=p.no_rkm_medis
        where s.tanggalawal between ? and ?';
$par = [$tgl1, $tgl2];
if ($q !== '') {
    $sql .= ' and (s.no_surat like ? or s.no_rawat like ? or r.no_rkm_medis like ? or p.nm_pasien like ?
               or s.tanggalawal like ? or s.tanggalakhir like ?)';
    array_push($par, ...array_fill(0, 6, "%$q%"));
}
$sql .= ' order by s.no_surat';
$st = db()->prepare($sql);
$st->execute($par);
$rows = $st->fetchAll();

$prefill = trim($_GET['no_rawat'] ?? '');
?>
<!doctype html>
<html lang="id">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Data Surat Keterangan Sakit</title>
<style>
  :root { --bg:#f3f6f1; --card:#fff; --ink:#2d3a2d; --muted:#6b7a6b; --line:#d9e2d6; --pri:#2f7d4f; --pri-d:#256640; --danger:#b3382c; }
  @media (prefers-color-scheme: dark) { :root { --bg:#141a15; --card:#1d251e; --ink:#e4ece4; --muted:#93a393; --line:#2f3b30; --pri:#4cae75; --pri-d:#3d9262; --danger:#e0695c; } }
  * { box-sizing:border-box; }
  body { margin:0; font:14px/1.45 system-ui,Segoe UI,Tahoma,sans-serif; background:var(--bg); color:var(--ink); }
  .wrap { max-width:1100px; margin:0 auto; padding:16px; }
  h1 { font-size:18px; margin:0 0 12px; }
  h1 .home { color:inherit; text-decoration:none; } h1 .home:hover { text-decoration:underline; }
  .card { background:var(--card); border:1px solid var(--line); border-radius:8px; padding:14px; margin-bottom:14px; }
  .grid { display:grid; grid-template-columns:repeat(auto-fit,minmax(170px,1fr)); gap:10px; }
  label { display:block; font-size:12px; color:var(--muted); margin-bottom:3px; }
  input { width:100%; padding:7px 8px; border:1px solid var(--line); border-radius:5px; background:var(--bg); color:var(--ink); font:inherit; }
  input[readonly] { opacity:.75; }
  .bar { display:flex; flex-wrap:wrap; gap:8px; margin-top:12px; }
  button,.btn { padding:7px 14px; border:1px solid var(--pri); background:var(--pri); color:#fff; border-radius:5px; cursor:pointer; font:inherit; text-decoration:none; display:inline-block; }
  button:hover,.btn:hover { background:var(--pri-d); }
  .sec { background:transparent; color:var(--pri); } .sec:hover { background:var(--line); }
  .del { background:var(--danger); border-color:var(--danger); } .del:hover { background:var(--danger); opacity:.85; }
  .msg { padding:9px 12px; border-radius:6px; background:var(--card); border:1px solid var(--pri); margin-bottom:12px; }
  .filter { display:flex; flex-wrap:wrap; gap:10px; align-items:end; }
  .filter > div { min-width:140px; }
  .tw { overflow-x:auto; }
  table { width:100%; border-collapse:collapse; }
  th,td { padding:7px 9px; border-bottom:1px solid var(--line); text-align:left; white-space:nowrap; }
  th { font-size:12px; color:var(--muted); }
  tbody tr { cursor:pointer; } tbody tr:hover,tr.sel { background:var(--line); }
  .count { color:var(--muted); margin-left:auto; align-self:center; }
  [hidden] { display:none !important; }
</style>
</head>
<body>
<div class="wrap">
  <h1><a href="./" class="home">::[ Data Surat Keterangan Sakit ]::</a></h1>
  <?php if ($pesan): ?><div class="msg"><?= e($pesan) ?></div><?php endif; ?>

  <form class="card" id="frm" method="post" autocomplete="off">
    <input type="hidden" name="csrf" value="<?= e(csrf_token()) ?>">
    <input type="hidden" name="aksi" id="aksi" value="simpan">
    <input type="hidden" name="no_surat_lama" id="no_surat_lama">
    <input type="hidden" name="tgl1" value="<?= e($tgl1) ?>">
    <input type="hidden" name="tgl2" value="<?= e($tgl2) ?>">
    <input type="hidden" name="q" value="<?= e($q) ?>">
    <div class="grid">
      <div><label for="no_rawat">No.Rawat</label><input id="no_rawat" name="no_rawat" maxlength="17" required value="<?= e($prefill) ?>"></div>
      <div><label>No.R.M.</label><input id="no_rm" readonly></div>
      <div style="grid-column:span 2"><label>Nama Pasien</label><input id="pasien" readonly></div>
      <div><label for="tanggalawal">Dari Tanggal</label><input type="date" id="tanggalawal" name="tanggalawal" required value="<?= date('Y-m-d') ?>"></div>
      <div><label for="tanggalakhir">Sampai Tanggal</label><input type="date" id="tanggalakhir" name="tanggalakhir" required value="<?= date('Y-m-d') ?>"></div>
      <div><label for="lamasakit">Lama Sakit</label><input id="lamasakit" name="lamasakit" maxlength="20" required value="1 (Satu)"></div>
      <div><label for="no_surat">No. Surat Sakit</label><input id="no_surat" name="no_surat" maxlength="30" required></div>
    </div>
    <div class="bar">
      <button type="submit" id="bSimpan">Simpan</button>
      <button type="submit" id="bGanti" class="sec" hidden>Ganti</button>
      <button type="button" id="bBaru" class="sec">Baru</button>
      <button type="submit" id="bHapus" class="del" hidden>Hapus</button>
      <a id="bCetak" class="btn sec" target="_blank" hidden>Cetak Surat</a>
      <a class="btn sec" href="kop.php" style="margin-left:auto">Atur KOP Surat</a>
    </div>
  </form>

  <form class="card filter" method="get">
    <div><label>Tgl. Surat</label><input type="date" name="tgl1" value="<?= e($tgl1) ?>"></div>
    <div><label>s.d.</label><input type="date" name="tgl2" value="<?= e($tgl2) ?>"></div>
    <div style="flex:1"><label>Key Word</label><input name="q" value="<?= e($q) ?>" placeholder="No.surat / rawat / RM / nama"></div>
    <button type="submit">Cari</button>
    <a class="btn sec" href="index.php?tgl1=2000-01-01&tgl2=2099-12-31">Semua</a>
    <span class="count">Record : <?= count($rows) ?></span>
  </form>

  <div class="card tw">
    <table>
      <thead><tr><th>No.Surat Sakit</th><th>No.Rawat</th><th>No.R.M.</th><th>Nama Pasien</th><th>Dari Tanggal</th><th>Sampai Tanggal</th><th>Lama Sakit</th></tr></thead>
      <tbody>
      <?php foreach ($rows as $r): ?>
        <tr data-no="<?= e($r['no_surat']) ?>" data-rawat="<?= e($r['no_rawat']) ?>" data-rm="<?= e($r['no_rkm_medis']) ?>"
            data-pasien="<?= e($r['nm_pasien']) ?>" data-awal="<?= e($r['tanggalawal']) ?>" data-akhir="<?= e($r['tanggalakhir']) ?>" data-lama="<?= e($r['lamasakit']) ?>">
          <td><?= e($r['no_surat']) ?></td><td><?= e($r['no_rawat']) ?></td><td><?= e($r['no_rkm_medis']) ?></td>
          <td><?= e($r['nm_pasien']) ?></td><td><?= e(tgl_id($r['tanggalawal'])) ?></td><td><?= e(tgl_id($r['tanggalakhir'])) ?></td><td><?= e($r['lamasakit']) ?></td>
        </tr>
      <?php endforeach; ?>
      <?php if (!$rows): ?><tr><td colspan="7" style="cursor:default">Tidak ada data.</td></tr><?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<script>
const $ = id => document.getElementById(id);
const f = $('frm');
let editing = false;

async function cariRawat() {
  const v = $('no_rawat').value.trim();
  $('no_rm').value = ''; $('pasien').value = '';
  if (!v) return;
  const d = await (await fetch('api.php?action=rawat&no_rawat=' + encodeURIComponent(v))).json();
  if (d && d.nm_pasien) { $('no_rm').value = d.no_rkm_medis; $('pasien').value = d.nm_pasien; }
}
async function nomorBaru() {
  if (editing) return;
  const d = await (await fetch('api.php?action=nomor&tgl=' + $('tanggalawal').value)).json();
  $('no_surat').value = d.no_surat || '';
}
function mode(edit) {
  editing = edit;
  $('bSimpan').hidden = edit;
  $('bGanti').hidden = $('bHapus').hidden = $('bCetak').hidden = !edit;
}
function baru() {
  document.querySelectorAll('tr.sel').forEach(t => t.classList.remove('sel'));
  mode(false);
  $('aksi').value = 'simpan';
  $('no_rawat').value = ''; $('no_rm').value = ''; $('pasien').value = '';
  $('lamasakit').value = '1 (Satu)';
  $('tanggalawal').value = $('tanggalakhir').value = new Date().toLocaleDateString('sv-SE');
  nomorBaru(); $('no_rawat').focus();
}

$('no_rawat').addEventListener('change', cariRawat);
$('tanggalawal').addEventListener('change', () => { if ($('tanggalakhir').value < $('tanggalawal').value) $('tanggalakhir').value = $('tanggalawal').value; nomorBaru(); });
$('bBaru').addEventListener('click', baru);
$('bGanti').addEventListener('click', () => $('aksi').value = 'ganti');
$('bSimpan').addEventListener('click', () => $('aksi').value = 'simpan');
$('bHapus').addEventListener('click', ev => {
  if (!confirm('Hapus surat ' + $('no_surat_lama').value + ' ?')) return ev.preventDefault();
  $('aksi').value = 'hapus';
  f.querySelectorAll('[required]').forEach(i => i.required = false);
});

document.querySelector('tbody').addEventListener('click', ev => {
  const tr = ev.target.closest('tr[data-no]'); if (!tr) return;
  document.querySelectorAll('tr.sel').forEach(t => t.classList.remove('sel'));
  tr.classList.add('sel');
  const d = tr.dataset;
  $('no_surat').value = $('no_surat_lama').value = d.no;
  $('no_rawat').value = d.rawat; $('no_rm').value = d.rm; $('pasien').value = d.pasien;
  $('tanggalawal').value = d.awal; $('tanggalakhir').value = d.akhir; $('lamasakit').value = d.lama;
  $('bCetak').href = 'cetak.php?no_surat=' + encodeURIComponent(d.no);
  mode(true);
  window.scrollTo({ top: 0, behavior: 'smooth' });
});

if ($('no_rawat').value) cariRawat();
nomorBaru();
</script>
</body>
</html>
