<?php
$conn = new mysqli('localhost', 'root', '', 'hanyajasa_sikdemo');
if ($conn->connect_error) {
    die('Koneksi gagal: ' . htmlspecialchars($conn->connect_error));
}
$conn->set_charset('utf8mb4');

// Kunci sesuai DlgUser.java: id_user => 'nur', password => 'windi'
$result = $conn->query(
    "SELECT id_user AS id_user_asli,
            HEX(id_user) AS id_user,
            AES_DECRYPT(id_user, 'nur') AS id_user_dekripsi,
            `password` AS password_asli,
            HEX(`password`) AS `password`,
            AES_DECRYPT(`password`, 'windi') AS password_dekripsi
     FROM `user`
     ORDER BY id_user_dekripsi"
);
if (!$result) {
    die('Query gagal: ' . htmlspecialchars($conn->error));
}
?>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Data User</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; }
        th, td { border: 1px solid #999; padding: 6px 12px; }
        th { background: #eee; }
        .hex { font-family: monospace; font-size: 12px; color: #666; }
    </style>
</head>
<body>
    <h2>Daftar User</h2>
    <table>
        <tr>
            <th>No</th>
            <th>id_user (data asli biner)</th>
            <th>id_user (hex)</th>
            <th>id_user dekripsi</th>
            <th>password (data asli biner)</th>
            <th>password (hex)</th>
            <th>password dekripsi</th>
        </tr>
        <?php $no = 1; while ($row = $result->fetch_assoc()): ?>
            <tr>
                <td><?= $no++ ?></td>
                <td><?= htmlspecialchars((string)$row['id_user_asli'], ENT_SUBSTITUTE) ?></td>
                <td class="hex"><?= htmlspecialchars((string)$row['id_user']) ?></td>
                <td><?= htmlspecialchars((string)$row['id_user_dekripsi']) ?></td>
                <td><?= htmlspecialchars((string)$row['password_asli'], ENT_SUBSTITUTE) ?></td>
                <td class="hex"><?= htmlspecialchars((string)$row['password']) ?></td>
                <td><?= htmlspecialchars((string)$row['password_dekripsi']) ?></td>
            </tr>
        <?php endwhile; ?>
    </table>
</body>
</html>
<?php $conn->close(); ?>
