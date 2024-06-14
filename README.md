# SIMRS_Khanza_Mod_RSIPKUM_PKY

13.27 14/06/2024 - Palangka Raya, Riyan Hidayat Samosir, S.Kom | hanyajasa.com

Permintaan Lab dari dokter ke Lab.
1. rptPeriksaLabPermintaan.jrxml
2. rptPeriksaLabPermintaan.jasper
3. rptPeriksaLab2Permintaan.jrxml
4. rptPeriksaLab2Permintaan.jasper
5. D:\windiartonugroho\src\simrskhanza\DlgCariPeriksaLab.java
6. DlgCariPeriksaLab.java
7. D:\windiartonugroho\src\surat\SuratBebasNarkoba.java
8. SuratBebasNarkoba.java |
9. DlgCariPeriksaLabPA.java
10. DlgCariPeriksaLabPA.form
11. DlgRawatJalan.java


Sourcecode:
param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
param.put("noktp",Sequel.cariIsi("select pasien.no_ktp from pasien where pasien.no_rkm_medis=?",rs.getString("no_rkm_medis")));
param.put("tgl_lahir",Sequel.cariIsi("select date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir from pasien where pasien.no_rkm_medis=?",rs.getString("no_rkm_medis")));


----
Modif Jasper Report untuk Lab. Parameter Narkoba 3 dan 6

Table: audit_cuci_tangan_medis; 
Tambahkan: ruang; 
Type: varchar(30)

Ketinggalan file report report/rptResep.jasper

Source Code ini hasil modifikasi seperlunya yang disesuaikan untuk keperluan Rumah Sakit Islam PKU Muhammadiyah Palangka Raya.

Clone dari https://github.com/mas-elkhanza/SIMRS-Khanza 12/12/2022; 17/06/2023; 28/02/2024; 04/04/2024

https://github.com/ryanbekabe/SIMRS_Khanza_Mod_RSIPKUM_PKY

Java:
```git
/Users/windiartonugroho/src/fungsi/koneksiDB.java
/Users/windiartonugroho/src/fungsi/sekuel.java
/Users/windiartonugroho/src/inventory/DlgResepObat.form
/Users/windiartonugroho/src/inventory/DlgResepObat.java
/Users/windiartonugroho/src/inventory/InventoryTelaahResep.form
/Users/windiartonugroho/src/inventory/InventoryTelaahResep.java
/Users/windiartonugroho/src/permintaan/DlgPermintaanLaboratorium.java
/Users/windiartonugroho/src/kepegawaian/DlgAuditCuciTanganMedis.form
/Users/windiartonugroho/src/kepegawaian/DlgAuditCuciTanganMedis.java
/Users/windiartonugroho/src/rekammedis/RMPenilaianAwalKeperawatanRalan.java
/Users/windiartonugroho/src/simrskhanza/DlgCariPeriksaLab.form
/Users/windiartonugroho/src/simrskhanza/DlgCariPeriksaLab.java
/Users/windiartonugroho/src/simrskhanza/DlgCariPeriksaLabPA.form
/Users/windiartonugroho/src/simrskhanza/DlgCariPeriksaLabPA.java
/Users/windiartonugroho/src/simrskhanza/DlgIKBBayi.java
/Users/windiartonugroho/src/simrskhanza/DlgKasirRalan.java
/Users/windiartonugroho/src/simrskhanza/DlgRawatJalan.java
/Users/windiartonugroho/src/simrskhanza/frmUtama.java
/Users/windiartonugroho/src/surat/SuratBebasNarkoba.java
/Users/windiartonugroho/src/surat/SuratBebasNarkoba_18112023.java
/Users/windiartonugroho/src/surat/SuratButaWarna.java
/Users/windiartonugroho/src/surat/SuratKeteranganSehat.java
/Users/windiartonugroho/src/surat/SuratSakit.java
```

Report:
```git
/Users/windiartonugroho/report/rptBebasNarkoba.jasper
/Users/windiartonugroho/report/rptBebasNarkoba.jrxml
/Users/windiartonugroho/report/rptBebasNarkoba1.jasper
/Users/windiartonugroho/report/rptBebasNarkoba1.jrxml
/Users/windiartonugroho/report/rptBebasNarkoba2.jasper
/Users/windiartonugroho/report/rptBebasNarkoba2.jrxml
/Users/windiartonugroho/report/rptCetakPenilaianAwalKeperawatanRalan.jasper
/Users/windiartonugroho/report/rptCetakPenilaianAwalKeperawatanRalan.jrxml
/Users/windiartonugroho/report/rptLabelDiet.jasper
/Users/windiartonugroho/report/rptLabelDiet.jrxml
/Users/windiartonugroho/report/rptPeriksaLab.jasper
/Users/windiartonugroho/report/rptPeriksaLab.jrxml
/Users/windiartonugroho/report/rptPeriksaLab2.jasper
/Users/windiartonugroho/report/rptPeriksaLab2.jrxml
/Users/windiartonugroho/report/rptPeriksaLab3.jasper
/Users/windiartonugroho/report/rptPeriksaLab3.jrxml
/Users/windiartonugroho/report/rptPeriksaLabPAHJ.jasper
/Users/windiartonugroho/report/rptPeriksaLabPAHJ.jrxml
/Users/windiartonugroho/report/rptPeriksaLabPermintaanPADT.jasper
/Users/windiartonugroho/report/rptPeriksaLabPermintaanPADT.jrxml
/Users/windiartonugroho/report/rptPeriksaLabPermintaan.jrxml
/Users/windiartonugroho/report/rptPeriksaLabPermintaan.jasper
/Users/windiartonugroho/report/rptPeriksaLab2Permintaan.jrxml
/Users/windiartonugroho/report/rptPeriksaLab2Permintaan.jasper
/Users/windiartonugroho/report/rptResep.jasper
/Users/windiartonugroho/report/rptResep.jrxml
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
git commit -m "Update ke-29 Permintaan Lab. dan input TTV beri warning jika kurang atau tidak diisi."
git branch -M main
git push -u origin main
```
