<?php
// Atur KOP surat: upload gambar KOP sendiri, kembali ke default, opsi basmalah teks.
declare(strict_types=1);
require_once __DIR__ . '/config.php';

const MAX_BYTES = 5 * 1024 * 1024;
$pesan = $_SESSION['pesan_kop'] ?? '';
unset($_SESSION['pesan_kop']);

function selesai(string $m): never
{
    $_SESSION['pesan_kop'] = $m;
    header('Location: kop.php');
    exit;
}

function hapus_kop(): void
{
    foreach (['jpg', 'png', 'webp'] as $x) {
        @unlink(KOP_DIR . "/KOP_Surat.$x");
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    csrf_check();
    if (!is_dir(KOP_DIR) && !mkdir(KOP_DIR, 0775, true)) {
        selesai('Folder kop tidak bisa dibuat. Periksa izin tulis folder aplikasi.');
    }
    // cegah eksekusi skrip di folder upload (Apache)
    if (!is_file(KOP_DIR . '/.htaccess')) {
        file_put_contents(KOP_DIR . '/.htaccess', "php_flag engine off\nRemoveHandler .php .phtml\nOptions -ExecCGI\n");
    }

    $aksi = $_POST['aksi'] ?? '';
    if ($aksi === 'upload') {
        $f = $_FILES['kop'] ?? null;
        if (!$f || $f['error'] === UPLOAD_ERR_NO_FILE) {
            selesai('Pilih file gambar terlebih dahulu.');
        }
        if ($f['error'] !== UPLOAD_ERR_OK || !is_uploaded_file($f['tmp_name'])) {
            selesai('Upload gagal (kode ' . (int)$f['error'] . '). Ukuran maksimal ' . ini_get('upload_max_filesize') . '.');
        }
        if ($f['size'] > MAX_BYTES) {
            selesai('Ukuran file maksimal 5 MB.');
        }
        $info = @getimagesize($f['tmp_name']);
        $map = [IMAGETYPE_JPEG => 'jpg', IMAGETYPE_PNG => 'png', IMAGETYPE_WEBP => 'webp'];
        if (!$info || !isset($map[$info[2]])) {
            selesai('File harus gambar JPG, PNG, atau WEBP.');
        }
        hapus_kop();
        if (!move_uploaded_file($f['tmp_name'], KOP_DIR . '/KOP_Surat.' . $map[$info[2]])) {
            selesai('Gagal menyimpan file KOP.');
        }
        selesai("KOP baru tersimpan ({$info[0]}x{$info[1]} px).");
    } elseif ($aksi === 'default') {
        hapus_kop();
        selesai('KOP dikembalikan ke default (logo dari database).');
    } elseif ($aksi === 'opsi') {
        file_put_contents(KOP_DIR . '/opsi.json', json_encode(['basmalah' => isset($_POST['basmalah'])]));
        selesai('Pengaturan disimpan.');
    }
    selesai('');
}

$kop = kop_aktif();
$opsi = kop_opsi();
?>
<!doctype html>
<html lang="id">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Atur KOP Surat</title>
<style>
  :root { --bg:#f3f6f1; --card:#fff; --ink:#2d3a2d; --muted:#6b7a6b; --line:#d9e2d6; --pri:#2f7d4f; --pri-d:#256640; --danger:#b3382c; }
  @media (prefers-color-scheme: dark) { :root { --bg:#141a15; --card:#1d251e; --ink:#e4ece4; --muted:#93a393; --line:#2f3b30; --pri:#4cae75; --pri-d:#3d9262; --danger:#e0695c; } }
  * { box-sizing:border-box; }
  body { margin:0; font:14px/1.45 system-ui,Segoe UI,Tahoma,sans-serif; background:var(--bg); color:var(--ink); }
  .wrap { max-width:800px; margin:0 auto; padding:16px; }
  h1 { font-size:18px; margin:0 0 12px; } h2 { font-size:14px; margin:0 0 8px; }
  .card { background:var(--card); border:1px solid var(--line); border-radius:8px; padding:14px; margin-bottom:14px; }
  .msg { padding:9px 12px; border-radius:6px; background:var(--card); border:1px solid var(--pri); margin-bottom:12px; }
  .prev { background:#fff; border:1px dashed var(--line); padding:8px; text-align:center; }
  .prev img { max-width:100%; height:auto; }
  .muted { color:var(--muted); font-size:12px; }
  input[type=file] { width:100%; padding:7px; border:1px solid var(--line); border-radius:5px; background:var(--bg); color:var(--ink); }
  button,.btn { padding:7px 14px; border:1px solid var(--pri); background:var(--pri); color:#fff; border-radius:5px; cursor:pointer; font:inherit; text-decoration:none; display:inline-block; }
  button:hover,.btn:hover { background:var(--pri-d); }
  .sec { background:transparent; color:var(--pri); } .del { background:var(--danger); border-color:var(--danger); }
  .row { display:flex; gap:8px; flex-wrap:wrap; margin-top:10px; align-items:center; }
</style>
</head>
<body>
<div class="wrap">
  <h1>Atur KOP Surat</h1>
  <?php if ($pesan): ?><div class="msg"><?= e($pesan) ?></div><?php endif; ?>

  <div class="card">
    <h2>KOP saat ini <?= $kop ? '(hasil upload)' : '(default: logo dari database)' ?></h2>
    <div class="prev"><img src="<?= e($kop['url'] ?? 'logo.php') ?>" alt="KOP surat" onerror="this.replaceWith('Tidak ada gambar KOP.')"></div>
  </div>

  <form class="card" method="post" enctype="multipart/form-data">
    <input type="hidden" name="csrf" value="<?= e(csrf_token()) ?>">
    <input type="hidden" name="aksi" value="upload">
    <h2>Upload KOP baru</h2>
    <input type="file" name="kop" accept="image/jpeg,image/png,image/webp" required>
    <p class="muted">JPG, PNG, atau WEBP, maks. 5 MB. Gambar mengisi lebar kertas, sehingga lebar sekitar 1500–2000 px dengan rasio melebar (kop saja, tanpa isi surat) memberi hasil paling tajam.</p>
    <div class="row">
      <button type="submit">Upload</button>
      <?php if ($kop): ?>
        <button type="submit" class="del" form="fDefault" onclick="return confirm('Kembalikan ke KOP default?')">Kembalikan ke default</button>
      <?php endif; ?>
    </div>
  </form>
  <form id="fDefault" method="post"><input type="hidden" name="csrf" value="<?= e(csrf_token()) ?>"><input type="hidden" name="aksi" value="default"></form>

  <form class="card" method="post">
    <input type="hidden" name="csrf" value="<?= e(csrf_token()) ?>">
    <input type="hidden" name="aksi" value="opsi">
    <h2>Opsi</h2>
    <label><input type="checkbox" name="basmalah" <?= $opsi['basmalah'] ? 'checked' : '' ?>> Tampilkan basmalah (teks Arab) di bawah KOP</label>
    <p class="muted">Matikan jika gambar KOP sudah memuat kaligrafi basmalah.</p>
    <div class="row"><button type="submit">Simpan</button></div>
  </form>

  <a class="btn sec" href="index.php">&larr; Kembali ke Data Surat</a>
</div>
</body>
</html>
