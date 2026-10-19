<?php
// Menampilkan logo/kop dari kolom setting.logo
declare(strict_types=1);
require_once __DIR__ . '/config.php';

$blob = db()->query('select logo from setting limit 1')->fetchColumn();
if (!$blob) {
    http_response_code(404);
    exit;
}
header('Content-Type: ' . (new finfo(FILEINFO_MIME_TYPE))->buffer($blob));
header('Cache-Control: private, max-age=3600');
echo $blob;
