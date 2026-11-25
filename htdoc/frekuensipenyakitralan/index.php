<?php
declare(strict_types=1);
require __DIR__ . '/lib.php';
$auth = require_login();

$f = Filter::fromRequest($_GET);
$sudahCari = isset($_GET['tgl1']);
$data = [];
$error = '';
if ($sudahCari) {
    try {
        $data = ambil_data($f);
    } catch (PDOException $ex) {
        error_log('Frekuensi penyakit ralan: ' . $ex->getMessage());
        $error = 'Gagal mengambil data dari database.';
    }
}

// Form "Input" (filter lanjutan) dibuka otomatis jika ada filter yang terisi
$filterTerbuka = $f->poli . $f->dokter . $f->penjab . $f->kabupaten . $f->kecamatan . $f->kelurahan !== '';
$periode = tgl_indo($f->tgl1) . ' s.d. ' . tgl_indo($f->tgl2);

$lookupFields = [
    'poli'      => 'Poliklinik',
    'dokter'    => 'Dokter',
    'penjab'    => 'Cara Bayar',
    'kabupaten' => 'Kabupaten',
    'kecamatan' => 'Kecamatan',
    'kelurahan' => 'Kelurahan',
];
?>
<!doctype html>
<html lang="id">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><?= e(config('app_title')) ?></title>
<link rel="stylesheet" href="style.css">
</head>
<body>
<header class="topbar">
    <h1>::[ Frekuensi Penyakit Di Rawat Jalan ]::</h1>
    <div><?= e($auth['nama']) ?> · <a href="logout.php">Keluar</a></div>
</header>

<form method="get" id="frm" class="panel">
    <div class="row">
        <label>Periode
            <input type="date" name="tgl1" value="<?= e($f->tgl1) ?>" required>
        </label>
        <label>s.d.
            <input type="date" name="tgl2" value="<?= e($f->tgl2) ?>" required>
        </label>
        <label class="grow">Key Word
            <input type="search" name="cari" maxlength="90" value="<?= e($f->cari) ?>" placeholder="Kode / nama penyakit">
        </label>
        <button type="submit" class="btn primary">Cari</button>
        <button type="button" class="btn" id="btnAll">Semua</button>
    </div>

    <details <?= $filterTerbuka ? 'open' : '' ?>>
        <summary>Filter lanjutan</summary>
        <div class="grid">
            <?php foreach ($lookupFields as $name => $label): ?>
                <label><?= e($label) ?>
                    <span class="lookup">
                        <input name="<?= $name ?>" list="dl-<?= $name ?>" value="<?= e($f->$name) ?>"
                               data-lookup="<?= $name ?>" autocomplete="off" placeholder="Semua">
                        <button type="button" class="clear" title="Kosongkan" data-clear="<?= $name ?>">×</button>
                    </span>
                    <datalist id="dl-<?= $name ?>"></datalist>
                </label>
            <?php endforeach; ?>
        </div>
    </details>
</form>

<?php if ($error): ?><div class="alert"><?= e($error) ?></div><?php endif; ?>

<div class="toolbar">
    <span id="recordInfo">Record : <?= count($data) ?></span>
    <span class="spacer"></span>
    <?php if ($data): ?>
        <button type="button" class="btn" data-chart="top-bar">10 Terbanyak (Batang)</button>
        <button type="button" class="btn" data-chart="top-pie">10 Terbanyak (Pie)</button>
        <button type="button" class="btn" data-chart="low-bar">10 Terkecil (Batang)</button>
        <button type="button" class="btn" data-chart="low-pie">10 Terkecil (Pie)</button>
        <a class="btn" href="export.php?<?= e($f->toQuery()) ?>">Excel/CSV</a>
        <button type="button" class="btn primary" id="btnCetak">Cetak</button>
    <?php endif; ?>
</div>

<div class="table-wrap">
    <table id="tbl">
        <thead>
        <tr>
            <?php foreach (KOLOM as $key => $label): ?>
                <th data-key="<?= $key ?>" class="<?= in_array($key, ['kd_penyakit', 'penyakit', 'diagnosa_lain'], true) ? '' : 'n' ?>"><?= e($label) ?></th>
            <?php endforeach; ?>
        </tr>
        </thead>
        <tbody>
        <?php if (!$data): ?>
            <tr><td colspan="8" class="empty"><?= $sudahCari ? 'Tidak ada data pada periode/filter ini.' : 'Pilih periode lalu klik Cari.' ?></td></tr>
        <?php endif; ?>
        </tbody>
    </table>
</div>

<dialog id="chartDlg">
    <div class="dlg-head">
        <strong id="chartTitle"></strong>
        <button type="button" class="btn" onclick="this.closest('dialog').close()">Tutup</button>
    </div>
    <div class="chart-box"><canvas id="chartCanvas"></canvas></div>
</dialog>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"></script>
<script>
const DATA = <?= json_encode($data, JSON_UNESCAPED_UNICODE | JSON_HEX_TAG | JSON_HEX_AMP) ?>;
const PERIODE = <?= json_encode($periode, JSON_HEX_TAG) ?>;
const QUERY = <?= json_encode($f->toQuery(), JSON_HEX_TAG) ?>;
const NUM_COLS = ['lk_hidup', 'pr_hidup', 'lk_mati', 'pr_mati', 'jumlah'];

/* ---------- Tabel + sorting (klik header kolom, seperti JTable) ---------- */
let sortKey = 'kd_penyakit', sortDir = 1;
const tbody = document.querySelector('#tbl tbody');

function esc(s) {
    return String(s).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
}

function render() {
    if (!DATA.length) return;
    const rows = [...DATA].sort((a, b) => {
        const x = a[sortKey], y = b[sortKey];
        return sortDir * (typeof x === 'number' ? x - y : String(x).localeCompare(String(y), 'id', {numeric: true}));
    });
    tbody.innerHTML = rows.map(r => `<tr>
        <td>${esc(r.kd_penyakit)}</td><td>${esc(r.penyakit)}</td><td class="lain">${esc(r.diagnosa_lain)}</td>
        ${NUM_COLS.map(k => `<td class="n">${r[k]}</td>`).join('')}
    </tr>`).join('');
    document.querySelectorAll('#tbl th').forEach(th => {
        th.dataset.sort = th.dataset.key === sortKey ? (sortDir > 0 ? 'asc' : 'desc') : '';
    });
}

document.querySelectorAll('#tbl th').forEach(th => th.addEventListener('click', () => {
    const k = th.dataset.key;
    if (sortKey === k) sortDir = -sortDir;
    else { sortKey = k; sortDir = NUM_COLS.includes(k) ? -1 : 1; }
    render();
}));
render();

/* ---------- Tombol ---------- */
document.getElementById('btnAll').addEventListener('click', () => {
    const frm = document.getElementById('frm');
    frm.querySelectorAll('input:not([type=date])').forEach(i => i.value = '');
    frm.submit();
});

document.getElementById('btnCetak')?.addEventListener('click', () => {
    window.open(`cetak.php?${QUERY}&sort=${sortKey}&dir=${sortDir > 0 ? 'asc' : 'desc'}`, '_blank');
});

document.querySelectorAll('[data-clear]').forEach(b => b.addEventListener('click', () => {
    document.querySelector(`input[name="${b.dataset.clear}"]`).value = '';
}));

/* ---------- Lookup (pengganti dialog cari poli/dokter/dll) ---------- */
document.querySelectorAll('input[data-lookup]').forEach(inp => {
    let timer;
    const load = () => {
        clearTimeout(timer);
        timer = setTimeout(async () => {
            try {
                const res = await fetch(`lookup.php?jenis=${inp.dataset.lookup}&q=${encodeURIComponent(inp.value)}`);
                const list = await res.json();
                if (!Array.isArray(list)) return;
                document.getElementById('dl-' + inp.dataset.lookup).innerHTML =
                    list.map(o => `<option value="${esc(o.nama)}">${esc(o.kode)}</option>`).join('');
            } catch (e) { console.error(e); }
        }, 250);
    };
    inp.addEventListener('focus', load, {once: true});
    inp.addEventListener('input', load);
});

/* ---------- Grafik 10 besar / 10 terkecil ---------- */
let chart;
const PALETTE = ['#2a78d6','#e0773a','#2fa36b','#c94a6b','#8a63c8','#c9a227','#2aa3b5','#7a8b99','#d65a9e','#5b8c2a'];

document.querySelectorAll('[data-chart]').forEach(b => b.addEventListener('click', () => {
    const [urut, jenis] = b.dataset.chart.split('-');
    const rows = [...DATA].sort((a, x) => urut === 'top' ? x.jumlah - a.jumlah : a.jumlah - x.jumlah).slice(0, 10);
    const title = (urut === 'top' ? '10 Besar' : '10 Terkecil') + ' Penyakit Di Rawat Jalan';
    const labels = rows.map(r => `${r.kd_penyakit}, ${r.penyakit}, ${r.jumlah}`);

    document.getElementById('chartTitle').textContent = `Grafik ${title} (${PERIODE})`;
    chart?.destroy();
    chart = new Chart(document.getElementById('chartCanvas'), jenis === 'bar' ? {
        type: 'bar',
        data: {
            labels: [PERIODE],
            datasets: rows.map((r, i) => ({ label: labels[i], data: [r.jumlah], backgroundColor: PALETTE[i] }))
        },
        options: {
            maintainAspectRatio: false,
            plugins: { title: { display: true, text: title }, legend: { position: 'bottom' } },
            scales: {
                x: { title: { display: true, text: 'Periode' } },
                y: { beginAtZero: true, ticks: { precision: 0 }, title: { display: true, text: 'Jumlah Penyakit' } }
            }
        }
    } : {
        type: 'pie',
        data: { labels, datasets: [{ data: rows.map(r => r.jumlah), backgroundColor: PALETTE }] },
        options: {
            maintainAspectRatio: false,
            plugins: { title: { display: true, text: title }, legend: { position: 'right' } }
        }
    });
    document.getElementById('chartDlg').showModal();
}));
</script>
</body>
</html>
