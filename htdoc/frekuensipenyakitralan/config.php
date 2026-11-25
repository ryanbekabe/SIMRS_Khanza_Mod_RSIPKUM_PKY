<?php
declare(strict_types=1);

/**
 * Konfigurasi koneksi database SIMRS Khanza.
 * Sesuaikan dengan setting/database.xml pada aplikasi Java.
 */
return [
    'db' => [
        'host'    => '10.8.0.2',
        'port'    => 3306,
        'name'    => 'sik',
        'user'    => 'root',
        'pass'    => '',
        'charset' => 'utf8mb4',
    ],
    // Kunci AES yang dipakai SIMRS Khanza (fungsi/akses.java)
    'aes_user_key' => 'nur',
    'aes_pass_key' => 'windi',

    'nama_instansi' => 'RUMAH SAKIT',
    'app_title'     => 'Frekuensi Penyakit Rawat Jalan',
];
