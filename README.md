# SIMRS_Khanza_Mod_RSIPKUM_PKY

07:53:07 28/12/2022 - Palangka Raya, Riyan Hidayat Samosir, S.Kom

Source Code ini hasil modifikasi seperlunya yang disesuaikan untuk keperluan Rumah Sakit Islam PKU Muhammadiyah Palangka Raya.

Clone dari https://github.com/mas-elkhanza/SIMRS-Khanza 12/12/2022

Java:
```git
/Users/windiartonugroho/src/fungsi/koneksiDB.java
/Users/windiartonugroho/src/permintaan/DlgPermintaanLaboratorium.java
/Users/windiartonugroho/src/rekammedis/RMPenilaianAwalKeperawatanRalan.java
/Users/windiartonugroho/src/simrskhanza/DlgIKBBayi.java
/Users/windiartonugroho/src/simrskhanza/DlgKasirRalan.java
/Users/windiartonugroho/src/simrskhanza/frmUtama.java
/Users/windiartonugroho/src/surat/SuratBebasNarkoba.java
/Users/windiartonugroho/src/surat/SuratButaWarna.java
/Users/windiartonugroho/src/surat/SuratKeteranganSehat.java
/Users/windiartonugroho/src/surat/SuratSakit.java
```

Report:
```git
/Users/windiartonugroho/report/rptBebasNarkoba.jasper
/Users/windiartonugroho/report/rptBebasNarkoba.jrxml
/Users/windiartonugroho/report/rptCetakPenilaianAwalKeperawatanRalan.jasper
/Users/windiartonugroho/report/rptCetakPenilaianAwalKeperawatanRalan.jrxml
/Users/windiartonugroho/report/rptSKL2.jasper
/Users/windiartonugroho/report/rptSKL2.jrxml
/Users/windiartonugroho/report/rptSuratKeteranganSehat.jasper
/Users/windiartonugroho/report/rptSuratKeteranganSehat.jrxml
/Users/windiartonugroho/report/rptSuratSakit5.jasper
/Users/windiartonugroho/report/rptSuratSakit5.jrxml
/Users/windiartonugroho/report/rptSuratTidakButaWarna.jasper
/Users/windiartonugroho/report/rptSuratTidakButaWarna.jrxml
```

Poin:
Form Utama warna hijau
Surat Keterangan format nomor surat: (No.Srt)/671024/(UNIT)/(BLN)/(THN)

Debug:
```git
WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
jdbc:mysql://localhost:3306/Peoples?verifyServerCertificate=false&useSSL=true
```
```git
find $(pwd) -maxdepth 1 -type f -not -path '*/\.*' | sort
mv /Users/windiartonugroho/dist/SIMRSKhanza.jar ./
chmod 777 *.jar
java -jar SIMRSKhanza.jar
```

```git
…or create a new repository on the command line
echo "# SIMRS_Khanza_Mod_RSIPKUM_PKY" >> README.md
git init
git add .
git commit -m "Update ke-4b 14:06:07 29/12/2022"
git branch -M main
git push -u origin main
```