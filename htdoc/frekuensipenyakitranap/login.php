<?php
declare(strict_types=1);
require __DIR__ . '/lib.php';

$error = '';
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!csrf_check($_POST['csrf'] ?? null)) {
        $error = 'Sesi kedaluwarsa, silakan coba lagi.';
    } else {
        try {
            $auth = login_attempt(trim((string) ($_POST['user'] ?? '')), (string) ($_POST['pass'] ?? ''));
            if ($auth) {
                session_regenerate_id(true);
                $_SESSION['auth'] = $auth;
                header('Location: index.php');
                exit;
            }
            $error = 'User ID atau password salah.';
        } catch (PDOException $ex) {
            error_log('Login error: ' . $ex->getMessage());
            $error = 'Koneksi database gagal.';
        }
    }
}
?>
<!doctype html>
<html lang="id">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login - <?= e(config('app_title')) ?></title>
<link rel="stylesheet" href="style.css">
</head>
<body class="login-page">
<form method="post" class="login-box">
    <h1><?= e(config('app_title')) ?></h1>
    <?php if ($error): ?><div class="alert"><?= e($error) ?></div><?php endif; ?>
    <input type="hidden" name="csrf" value="<?= e(csrf_token()) ?>">
    <label>User ID<input name="user" required autofocus autocomplete="username"></label>
    <label>Password<input type="password" name="pass" required autocomplete="current-password"></label>
    <button type="submit" class="btn primary">Masuk</button>
</form>
</body>
</html>
