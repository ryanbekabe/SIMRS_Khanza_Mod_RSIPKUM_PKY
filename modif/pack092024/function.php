<?php
include 'config.php';

class EnkripsiAES {
    private static $key = "Bar12345Bar12345"; 
    private static $initVector = "sayangsamakhanza"; 
    public static function encrypt($value) {
        try {
            $iv = self::$initVector;
            $encrypted = openssl_encrypt($value, 'AES-128-CBC', self::$key, OPENSSL_RAW_DATA, $iv);
            return base64_encode($encrypted);  
        } catch (Exception $e) {
            echo "Error during encryption: " . $e->getMessage();
        }

        return null;
    }

    public static function decrypt($encrypted) {
        try {
            $iv = self::$initVector;
            $encrypted = base64_decode($encrypted);  
            $decrypted = openssl_decrypt($encrypted, 'AES-128-CBC', self::$key, OPENSSL_RAW_DATA, $iv);
            return $decrypted;
        } catch (Exception $e) {
            echo "Error during decryption: " . $e->getMessage();
        }

        return null;
    }
}

 
$id = isset($_GET['id']) ? $mysqli->real_escape_string($_GET['id']) : '';
$id = str_replace(' ', '+', $id);
$data_id = EnkripsiAES::decrypt($id);
$data_id= str_replace("'", '"', $data_id);
$data = json_decode($data_id, true);
// echo "data_id: ".$data_id;
// echo "<br>";
// echo "data: ".$data;
// echo "<br>";
if (!$data || !isset($data['x']) || !isset($data['t'])) { 
    // header("Location: https://www.google.com");
    exit;  
}


$x = $data['x'];
$t = $data['t'];
// echo "$x: ".$x;
// echo "<br>";
// echo "------";
// echo "<br>";
// echo "$t: ".$t;
if(isset($data['x']) && $data['x'] == "rj") {
    $sql = "SELECT
      reg_periksa.no_rawat as nomor,
      reg_periksa.tgl_registrasi as tanggal,
      resume_pasien.kd_dokter as id_dokter,
      dokter.nm_dokter as dokter,
      pasien.nm_pasien as pasien, 
      dokter.no_ijn_praktek as sip
    FROM
      resume_pasien
      INNER JOIN reg_periksa ON resume_pasien.no_rawat = reg_periksa.no_rawat
      INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis
      INNER JOIN dokter ON resume_pasien.kd_dokter = dokter.kd_dokter 
    WHERE
      resume_pasien.no_rawat = '$t'";
    $type = "Resume Rawat Jalan";
  
  } elseif(isset($data['x']) && $data['x'] == "ri") {
    $sql = "SELECT
      reg_periksa.no_rawat as nomor,
      reg_periksa.tgl_registrasi as tanggal,
      resume_pasien_ranap.kd_dokter as id_dokter,
      dokter.nm_dokter as dokter,
      pasien.nm_pasien as pasien, 
      dokter.no_ijn_praktek as sip
    FROM
      resume_pasien_ranap
      INNER JOIN reg_periksa ON resume_pasien_ranap.no_rawat = reg_periksa.no_rawat
      INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis
      INNER JOIN dokter ON resume_pasien_ranap.kd_dokter = dokter.kd_dokter 
    WHERE
      resume_pasien_ranap.no_rawat = '$t'";
    $type = "Resume Rawat Inap";
  
  } elseif(isset($data['x']) && $data['x'] == "sks") {
    $sql = "SELECT
      reg_periksa.tgl_registrasi as tanggal,
      dokter.nm_dokter as dokter,
      dokter.no_ijn_praktek as sip,
      suratsakit.no_surat as nomor,
      pasien.nm_pasien as pasien,
      pasien.alamat as alamat
    FROM
      reg_periksa
      INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis
      INNER JOIN suratsakit ON reg_periksa.no_rawat = suratsakit.no_rawat
      INNER JOIN dokter ON reg_periksa.kd_dokter = dokter.kd_dokter
    WHERE
      suratsakit.no_surat = '$t'";
    $type = "Surat Keterangan Sakit";
  } elseif(isset($data['x']) && $data['x'] == "ske") {
    $sql = "SELECT
      reg_periksa.tgl_registrasi as tanggal,
      dokter.nm_dokter as dokter,
      dokter.no_ijn_praktek as sip,
      surat_keterangan_sehat.no_surat as nomor,
      pasien.nm_pasien as pasien,
      pasien.alamat as alamat
    FROM
      reg_periksa
      INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis
      INNER JOIN surat_keterangan_sehat ON reg_periksa.no_rawat = surat_keterangan_sehat.no_rawat
      INNER JOIN dokter ON reg_periksa.kd_dokter = dokter.kd_dokter
    WHERE
    surat_keterangan_sehat.no_surat = '$t'";
    $type = "Surat Keterangan Sehat";
  } elseif(isset($data['x']) && $data['x'] == "skw") {
    $sql = "SELECT
      reg_periksa.tgl_registrasi as tanggal,
      dokter.nm_dokter as dokter,
      dokter.no_ijn_praktek as sip,
      surat_buta_warna.no_surat as nomor,
      pasien.nm_pasien as pasien,
      pasien.alamat as alamat
    FROM
      reg_periksa
      INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis
      INNER JOIN surat_buta_warna ON reg_periksa.no_rawat = surat_buta_warna.no_rawat
      INNER JOIN dokter ON reg_periksa.kd_dokter = dokter.kd_dokter
    WHERE
    surat_buta_warna.no_surat = '$t'";
    $type = "Surat Keterangan Buta Warna";
  } elseif(isset($data['x']) && $data['x'] == "skm") {
    $sql = "SELECT
      pasien_mati.tanggal as tanggal,pasien_mati.kd_dokter,pasien_mati.keterangan as nomor,pasien_mati.no_rkm_medis,
      dokter.kd_dokter,dokter.nm_dokter as dokter,dokter.no_ijn_praktek as sip,
      pasien.no_rkm_medis,pasien.nm_pasien as pasien,pasien.alamat
    FROM
    pasien_mati
      INNER JOIN pasien ON pasien.no_rkm_medis = pasien_mati.no_rkm_medis
      INNER JOIN dokter ON dokter.kd_dokter = pasien_mati.kd_dokter
    WHERE
    pasien_mati.keterangan = '$t'";
    $type = "Surat Keterangan Meninggal";
  } elseif(isset($data['x']) && $data['x'] == "skl") {
    $sql = "SELECT
      pasien_bayi.no_rkm_medis,pasien_bayi.jam_lahir,pasien_bayi.penolong,pasien_bayi.no_skl as nomor,
      dokter.kd_dokter,dokter.nm_dokter as dokter,dokter.no_ijn_praktek as sip,
      pasien.no_rkm_medis,pasien.nm_pasien as pasien,pasien.alamat,pasien.tgl_daftar as tanggal
    FROM
    pasien_bayi
      INNER JOIN pasien ON pasien.no_rkm_medis = pasien_bayi.no_rkm_medis
      INNER JOIN dokter ON dokter.kd_dokter = pasien_bayi.penolong
    WHERE
    pasien_bayi.no_skl = '$t'";
    $type = "Surat Keterangan Lahir";
  } else { 
    header("Location: https://www.google.com");
    exit;
  }
  
$result = $mysqli->query($sql);
$data = $result->fetch_assoc();

$mysqli->close();

function formatDate($date) {
  $d = new DateTime($date);
  return $d->format('d/m/Y');
}

?>
