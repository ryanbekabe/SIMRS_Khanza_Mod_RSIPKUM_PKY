/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import AESsecurity.EnkripsiAES;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author khanzasoft
 */
public class koneksiDB {
    private static Connection connection=null;
    private static final Properties prop = new Properties();  
    private static final MysqlDataSource dataSource=new MysqlDataSource();
    private static String var="";
    
    public koneksiDB(){} 
    public static Connection condb(){ 
        if(connection == null){
            try{
                prop.loadFromXML(new FileInputStream("setting/database.xml"));
                dataSource.setURL("jdbc:mysql://"+EnkripsiAES.decrypt(prop.getProperty("HOST"))+":"+EnkripsiAES.decrypt(prop.getProperty("PORT"))+"/"+EnkripsiAES.decrypt(prop.getProperty("DATABASE"))+"?zeroDateTimeBehavior=convertToNull&autoReconnect=true&useCompression=true");
                dataSource.setUser(EnkripsiAES.decrypt(prop.getProperty("USER")));
                dataSource.setPassword(EnkripsiAES.decrypt(prop.getProperty("PAS")));
                connection=dataSource.getConnection();       
                System.out.println("  Koneksi Berhasil. Sorry bro loading, silahkan baca dulu.... \n\n"+
                        "	Software ini adalah Software Menejemen Rumah Sakit/Klinik/\n" +
                        "  Puskesmas yang  gratis dan boleh digunakan siapa saja tanpa dikenai \n" +
                        "  biaya apapun. Dilarang keras memperjualbelikan/mengambil \n" +
                        "  keuntungan dari Software ini dalam bentuk apapun tanpa seijin pembuat \n" +
                        "  software (Khanza.Soft Media).\n"+
                        "                                                                           \n"+
                        "  #    ____  ___  __  __  ____   ____    _  __ _                              \n" +
                        "  #   / ___||_ _||  \\/  ||  _ \\ / ___|  | |/ /| |__    __ _  _ __   ____ __ _ \n" +
                        "  #   \\___ \\ | | | |\\/| || |_) |\\___ \\  | ' / | '_ \\  / _` || '_ \\ |_  // _` |\n" +
                        "  #    ___) || | | |  | ||  _ <  ___) | | . \\ | | | || (_| || | | | / /| (_| |\n" +
                        "  #   |____/|___||_|  |_||_| \\_\\|____/  |_|\\_\\|_| |_| \\__,_||_| |_|/___|\\__,_|\n" +
                        "  #                                                                           \n"+
                        "                                                                           \n"+
                        "  Licensi yang dianut di software ini https://en.wikipedia.org/wiki/Aladdin_Free_Public_License \n"+
                        "  Informasi dan panduan bisa dicek di halaman https://github.com/mas-elkhanza/SIMRS-Khanza/wiki \n"+
                        "  Bagi yang ingin berdonasi untuk pengembangan aplikasi ini bisa ke BSI 1015369872 atas nama Windiarto\n"+
                        "                            \n"+
                        "  *Pengembangan/modifikasi dari GitHub 20250222_18032025:                          \n"+
                        "  1) Form/Java:    \n"+
                        "        fungsi/koneksiDB.java (1) (Changelog 13062025);\n"+
                        "        fungsi/sekuel.java (1) (AKTIFKANTRACKSQL);\n"+
                        
                        "        inventory/DlgCopyResep.java (1) (Silakan konfirmasi ke bagian Farmasi);\n"+
                        "        inventory/DlgInputStok.java (0 lewati dulu) (Tambah kolom Ekspired);\n"+
                        "        inventory/DlgResepObat.java (1) (Tambah No.SEP di rptresep jika pasien BPJS, E-Tiket Obat);\n"+
                        "        inventory/DlgStokOpname.java (0 lewati dulu) (Tambah kolom Ekspired);\n"+
                        "        inventory/InventoryObatBHPTidakBergerak.java (0 lewati dulu) (Tambah kolom Ekspired);\n"+
                        
                        "        laporan/DlgRl38.java (1) (Tambah JK L P 13062025);\n"+
                        "        laporan/DlgRL4A.java (0) ();\n"+
                        
                        "        permintaan/DlgPermintaanKonsultasiMedik.java (1) (KOP, SIP dr.);\n"+
                        
                        "        rekammedis/RMCatatanPersalinan.java (1) (JahitanLuar2, JahitanDalam2 5 -> 50 11072025);\n"+
                        "        rekammedis/RMDataCatatanObservasiRanapPostPartum.java (1) (TFU: 15 -> 50 11072025);\n"+
                        "        rekammedis/RMDataResumePasien.java (1) (Kondisi Pasien Pulang = Kondisi.setModel);\n"+
                        "        rekammedis/RMDataResumePasienRanap.java (1) (KOP Surat);\n"+
                        "        rekammedis/RMPenilaianAwalMedisIGD.java (1) (V. DIAGNOSIS/ASESMEN DAN RETRIASE, BtnEdit disable jika Ranap);\n"+
                        "        rekammedis/RMPenilaianAwalMedisRanapDewasa.java (1) (KOP Surat);\n"+
                        "        rekammedis/RMPenilaianAwalKeperawatanIGD.java (1) (KOP Surat);\n"+
                        "        rekammedis/RMPenilaianAwalKeperawatanRanap.java (1) (KOP Surat);\n"+
                        "        rekammedis/RMRiwayatPerawatan.java (1) (Fix gambar Radiologi kebesaran);\n"+
                        "        rekammedis/RMSkriningTBC.java (1) (KOP Surat; Skrining bisa oleh hanya perawat);\n"+
                        
                        "        simrskhanza/DlgAbout.java (1) (Versi Git dan build app 13062025. 11072025);\n"+
                        "        simrskhanza/DlgCariPeriksaLab.java (0) (Judul pemeriksaan Lab. tetap di atas);\n"+
                        "        simrskhanza/DlgCariPeriksaLabPA.java (1) (MDT 1-3, batasInput 1024; SIP dokter sesuai kd_dokter; Cetak Sputum Gram 11072025);\n"+
                        "        simrskhanza/DlgCariPeriksaLabPA2.java (1) (Tambahan menu khusus dr. Frau. 13072025);\n"+
                        "        simrskhanza/DlgCariPeriksaRadiologi.java (2) (KOP; perbaiki bug simpan gambar pada Windows);\n"+
                        "        simrskhanza/DlgCariReg.java (0) ();\n"+
                        "        simrskhanza/DlgIGD.java (1) (Notif Potensi Re-Admisi, lebih dari 1 hari 24 jam dan kurang dari sama dengan 30 hari kemarin, nonaktifkan menu Penilaian Awal jika sudah Ranap 26052025);\n"+
                        "        simrskhanza/DlgIKBBayi.java (1) (Tgl Lhr Ibu; Tgl Lhr Ayah, Limit karakter 8 ke 10 UmurIbu; UmurAyah, Surat Keterangan Lahir 1 - Tunggal; Surat Keterangan Lahir 2 - Kembar; TCariRMIbu jika gagal mendapatkan NIK Ibu, PenilaianAwalMedisIGD MessageDialog Akses ke menu ini kami nonaktifkan);\n"+
                        "        simrskhanza/DlgKamarInap.java (1) (Label Radiologi);\n"+
                        "        simrskhanza/DlgKasirRalan.java (1) (Surat Keterangan Sehat 3 - Oke, Label Radiologi, Tambah tulisan pasien - MJKN, PenilaianAwalMedisIGD MessageDialog Akses ke menu ini kami nonaktifkan);\n"+
                        "        simrskhanza/DlgPasienMati.java (1) (Ket./NoSrt, Surat Kematian 1 - Oke);\n"+
                        "        simrskhanza/DlgRawatJalan.java (1) (Suhu harap dilengkapi, dll...);\n"+
                        "        simrskhanza/DlgReg.java (1) (Surat Keterangan Sehat 3 - Oke; - MJKN);\n"+
                        
                        "        simrskhanza/frmUtama.java (1) (Tambah tombol Update dan updater.jar; btnLaboratoriumPA2 13072025);\n"+
                        
                        "        surat/SuratBebasNarkoba.java (1) (3 Jenis Parameter, Perpanjang Field Nomor Surat, nama dokter dari tabel surat_skbn database);\n"+
                        "        surat/SuratButaWarna.java (1) (KOP Surat);\n"+
                        "        surat/SuratKeteranganRawatInap.java (1) (NoSurat 30; Mode Print A Dirawat dan B Sakit);\n"+
                        "        surat/SuratKeteranganSehat.java (1) (KOP Surat);\n"+
                        "        surat/SuratSakit.java (1) (Cetak Surat Sakit 1 - Oke);\n"+
                        
                        "  2) Report:    \n"+
                        "        rptBebasNarkoba.jasper (1);\n"+
                        "        rptBebasNarkoba1.jasper (1);\n"+
                        "        rptBebasNarkoba2.jasper (1);\n"+
                        
                        "        rptCetakPenilaianAwalKeperawatanRalan.jasper (0);\n"+
                        "        rptCetakPenilaianAwalKeperawatanIGD.jasper (1) (KOP Surat);\n"+
                        "        rptCetakPenilaianAwalMedisIGD.jasper (1) (KOP Surat);\n"+
                        "        rptCetakPenilaianAwalMedisRanap.jasper (1) (KOP Surat, SIP dokter);\n"+
                        "        rptCetakPenilaianAwalKeperawatanRanap.jasper (1) (KOP Surat);\n"+
                        "        rptCetakPenilaianAwalKeperawatanRanap2.jasper (0) (Nama perawat belum muncul);\n"+

                        "        rptItemResep.jasper (1) (E-Tiket penambahan Bismillah dan ED 10072025);\n"+
                        
                        "        rptFormulirSkriningTBC.jasper (1) (KOP Surat);\n"+
                        "        rptFormulirSkriningTBCD.jasper (0);\n"+
                        
                        "        rptLabelDiet.jasper (1) (Menu layak konsumsi jam ...);\n"+
                        "        rptLaporanResume.jasper (1) (KOP Surat, SIP);\n"+
                        "        rptLaporanResumeRanap.jasper (1) (KOP Surat);\n"+
                        
                        "        rptPeriksaLab.jasper (1)(Short);\n"+
                        "        rptPeriksaLabPermintaan.jasper (1 13062025) (SIP dr.);\n"+
                        "        rptPeriksaLab2.jasper (1)(Long);\n"+
                        "        rptPeriksaLab2Permintaan.jasper (1 13062025) (SIP dr.);\n"+
                        "        rptPeriksaLab3.jasper (1)(Swab);\n"+
                        "        rptPeriksaLab3Permintaan.jasper;\n"+
                        "        rptPeriksaLab4.jasper (1);\n"+
                        "        rptPeriksaLab4Permintaan.jasper (1 13062025) (SIP dr.);\n"+
                        "        rptPeriksaLabPermintaanPASGHJ.jasper (1 11072025) (Mikroskopis Sputum - Sputum Gram);\n"+
                        
                        "        rptPeriksaLabPADTHJ.jasper (1) (SIP dokter 19062025);\n"+
                        "        rptPeriksaLabPADTHJ2.jasper (1) (SIP dokter 19062025);\n"+
                        "        rptPeriksaLabPADTHJ3.jasper (1) (SIP dokter 19062025);\n"+
                        "        rptPeriksaRadiologi.jasper (1);\n"+
                        
                        "        rptResep.jasper (1) (Tampil No.SEP jika BPJS);\n"+
                        
                        "        rptSKL1.jasper (1) (KOP Surat, NIK Ibu);\n"+
                        "        rptSKL2.jasper (1) (KOP Surat, NIK Ibu);\n"+
                        "        rptSuratKematian.jasper (1) (KOP Surat);\n"+
                        "        rptSuratKeteranganRawatInap.jasper (1) (KOP Surat);\n"+
                        "        rptSuratKeteranganRawatInapB.jasper (1);\n"+
                        "        rptSuratKeteranganSehat.jasper (1);\n"+
                        "        rptSuratSakit5.jasper (1);\n"+
                        "        rptSuratTidakButaWarna.jasper (1);\n"+
                        
                        "  Re-Compile: LM Cinnamon 5.4.12 i7 IDE 15 JDK bellsoft-java15-full-amd64 / MSI IDE 21 Liberica Java Platform JDK 15 Full 64-bit Source/Binary Format JDK 8; Git Clone 22 Feb 2025. \n"+
                        "  Re-Compile_by: Bang Riyan Hidayat Samosir - www.HanyaJasa.Com - hanyajasa@gmail.com, 13/07/2025 - RS Islam PKU Muhammadiyah Palangka Raya. \n"+
                        "                                                                           ");

            }catch(Exception e){
                System.out.println("Notif : "+e);
                try {
                    if(connection.isClosed()){
                        prop.loadFromXML(new FileInputStream("setting/database.xml"));
                        dataSource.setURL("jdbc:mysql://"+EnkripsiAES.decrypt(prop.getProperty("HOST"))+":"+EnkripsiAES.decrypt(prop.getProperty("PORT"))+"/"+EnkripsiAES.decrypt(prop.getProperty("DATABASE"))+"?zeroDateTimeBehavior=convertToNull&amp;autoReconnect=true&amp;cachePrepStmts=true");
                        dataSource.setUser(EnkripsiAES.decrypt(prop.getProperty("USER")));
                        dataSource.setPassword(EnkripsiAES.decrypt(prop.getProperty("PAS")));
                        connection=dataSource.getConnection();  
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Koneksi Putus : "+e);
                }
            }
        }
        return connection;        
    }
    
    public static String HOST(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("HOST"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String DATABASE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("DATABASE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PORT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PORT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USER(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USER"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CARICEPAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("CARICEPAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String HOSTHYBRIDWEB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("HOSTHYBRIDWEB"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERHYBRIDWEB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERHYBRIDWEB"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASHYBRIDWEB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASHYBRIDWEB"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String HYBRIDWEB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("HYBRIDWEB");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PORTWEB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("PORTWEB");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ANTRIAN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ANTRIAN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ALARMAPOTEK(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ALARMAPOTEK");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String FORMALARMAPOTEK(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("FORMALARMAPOTEK");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ALARMLAB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ALARMLAB");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String FORMALARMLAB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("FORMALARMLAB");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ALARMRADIOLOGI(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ALARMRADIOLOGI");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String FORMALARMRADIOLOGI(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("FORMALARMRADIOLOGI");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ALARMRSISRUTE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ALARMRSISRUTE");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ALARMBOOKINGPERIKSA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ALARMBOOKINGPERIKSA");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ALARMPERMINTAANRANAP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ALARMPERMINTAANRANAP");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ALARMPENGADUANPASIEN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ALARMPENGADUANPASIEN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String MENUTRANSPARAN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("MENUTRANSPARAN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPIBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIBPJS");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYAPIBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIBPJS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDAPIBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIBPJS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERKEYAPIBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERKEYAPIBPJS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPIAPLICARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIAPLICARE");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYAPIAPLICARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIAPLICARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDAPIAPLICARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIAPLICARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERKEYAPIAPLICARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERKEYAPIAPLICARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPIMOBILEJKN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIMOBILEJKN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYAPIMOBILEJKN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIMOBILEJKN"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDAPIMOBILEJKN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIMOBILEJKN"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERKEYAPIMOBILEJKN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERKEYAPIMOBILEJKN"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPIAPOTEKBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIAPOTEKBPJS");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYAPIAPOTEKBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIAPOTEKBPJS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDAPIAPOTEKBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIAPOTEKBPJS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERKEYAPIAPOTEKBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERKEYAPIAPOTEKBPJS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String JADIKANPIUTANGAPOTEKBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("JADIKANPIUTANGAPOTEKBPJS"));
        }catch(Exception e){
            var="no"; 
        }
        return var;
    }
    
    public static String URLAPIPCARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIPCARE");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYAPIPCARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIPCARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDAPIPCARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIPCARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERKEYAPIPCARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERKEYAPIPCARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSPCARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSPCARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERPCARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERPCARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String DIVREGPCARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("DIVREGPCARE");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KACABPCARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("KACABPCARE");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPISISRUTE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPISISRUTE");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String IDSISRUTE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("IDSISRUTE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSSISRUTE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSSISRUTE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPISIRS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPISIRS");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String IDSIRS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("IDSIRS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSSIRS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSSIRS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPICORONA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPICORONA");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String IDCORONA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("IDCORONA"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSCORONA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSCORONA"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPISITT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPISITT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String IDSITT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("IDSITT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSSITT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSSITT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KABUPATENSITT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("KABUPATENSITT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KAMARAKTIFRANAP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("KAMARAKTIFRANAP").replaceAll("'","");;
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String DOKTERAKTIFKASIRRALAN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("DOKTERAKTIFKASIRRALAN").replaceAll("'","");;
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String POLIAKTIFKASIRRALAN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("POLIAKTIFKASIRRALAN").replaceAll("'","");;
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String RUANGANAKTIFINVENTARIS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("RUANGANAKTIFINVENTARIS").replaceAll("'","");;
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String BASENOREG(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("BASENOREG");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String VALIDASIULANGBERIOBAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("VALIDASIULANGBERIOBAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URUTNOREG(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URUTNOREG");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String JADWALDOKTERDIREGISTRASI(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("JADWALDOKTERDIREGISTRASI");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String IPPRINTERTRACER(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("IPPRINTERTRACER");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPIINHEALTH(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIINHEALTH");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String TOKENINHEALTH(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("TOKENINHEALTH"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PEMBULATANHARGAOBAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("PEMBULATANHARGAOBAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String AKTIFKANBATCHOBAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("AKTIFKANBATCHOBAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CETAKRINCIANOBAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("CETAKRINCIANOBAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String AKTIFKANBILLINGPARSIAL(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("AKTIFKANBILLINGPARSIAL");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLDUKCAPILJAKARTA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLDUKCAPILJAKARTA");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERDUKCAPILJAKARTA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERDUKCAPILJAKARTA"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSDUKCAPILJAKARTA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSDUKCAPILJAKARTA"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String VAR1DUKCAPILJAKARTA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("VAR1DUKCAPILJAKARTA");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String VAR2DUKCAPILJAKARTA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("VAR2DUKCAPILJAKARTA");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLDUKCAPIL(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLDUKCAPIL");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERDUKCAPIL(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERDUKCAPIL"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSDUKCAPIL(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSDUKCAPIL"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String IPUSERDUKCAPIL(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("IPUSERDUKCAPIL");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String AKTIFKANTRACKSQL(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("AKTIFKANTRACKSQL"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String HOSTWSLICA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("HOSTWSLICA");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KEYWSLICA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("KEYWSLICA"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String DEPOAKTIFOBAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("DEPOAKTIFOBAT").replaceAll("'","");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String STOKKOSONGRESEP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("STOKKOSONGRESEP");
        }catch(Exception e){
            var="no"; 
        }
        return var;
    }
    
    public static String TAMPILKANCOPYRESEPDOKTERLAIN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("TAMPILKANCOPYRESEPDOKTERLAIN");
        }catch(Exception e){
            var="no"; 
        }
        return var;
    }
    
    public static String HPPFARMASI(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            if(prop.getProperty("HPPFARMASI").equals("h_beli")){
                var="h_beli";
            }else{
                var="dasar";
            }
        }catch(Exception e){
            var="dasar"; 
        }
        return var;
    }
    
    public static String HPPTOKO(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            if(prop.getProperty("HPPTOKO").equals("h_beli")){
                var="h_beli";
            }else{
                var="dasar";
            }
        }catch(Exception e){
            var="dasar"; 
        }
        return var;
    }
    
    public static String URLAPIMEDQLAB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIMEDQLAB");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYAPIMEDQLAB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIMEDQLAB"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDAPIMEDQLAB(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIMEDQLAB"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLCARESTREAM(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLCARESTREAM");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPISOFTMEDIX(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPISOFTMEDIX");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PRODUCTSOFTMEDIX(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PRODUCTSOFTMEDIX"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String VERSIONSOFTMEDIX(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("VERSIONSOFTMEDIX"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERIDSOFTMEDIX(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERIDSOFTMEDIX"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KEYSOFTMEDIX(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("KEYSOFTMEDIX"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String RESEPRAJALKEPLAN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("RESEPRAJALKEPLAN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String DIAGNOSARUJUKANMASUKAPIBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("DIAGNOSARUJUKANMASUKAPIBPJS");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String AKTIFKANWARNARALAN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("AKTIFKANWARNARALAN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CLIENTIDSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CLIENTIDSATUSEHAT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYSATUSEHAT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String IDSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("IDSATUSEHAT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAUTHSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAUTHSATUSEHAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLFHIRSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLFHIRSATUSEHAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KELURAHANSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("KELURAHANSATUSEHAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KECAMATANSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("KECAMATANSATUSEHAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KABUPATENSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("KABUPATENSATUSEHAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PROPINSISATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("PROPINSISATUSEHAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KODEPOSSATUSEHAT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("KODEPOSSATUSEHAT");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERORTHANC(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERORTHANC"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSORTHANC(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSORTHANC"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PORTORTHANC(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PORTORTHANC"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLORTHANC(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLORTHANC");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ADDANTRIANAPIMOBILEJKN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ADDANTRIANAPIMOBILEJKN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String JADIKANBOOKINGSURATKONTROL(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("JADIKANBOOKINGSURATKONTROL");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String JADIKANBOOKINGSURATKONTROLAPIBPJS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("JADIKANBOOKINGSURATKONTROLAPIBPJS");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPIICARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIICARE");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYAPIICARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIICARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDAPIICARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIICARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERKEYAPIICARE(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERKEYAPIICARE"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPISMARTCLAIM(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPISMARTCLAIM");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYAPISMARTCLAIM(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPISMARTCLAIM"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDAPISMARTCLAIM(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDAPISMARTCLAIM"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERKEYAPISMARTCLAIM(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERKEYAPISMARTCLAIM"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String TANGGALMUNDUR(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("TANGGALMUNDUR");
        }catch(Exception e){
            var="yes"; 
        }
        return var;
    }
    
    public static String URLMOBILEJKNFKTP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLMOBILEJKNFKTP");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SECRETKEYMOBILEJKNFKTP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SECRETKEYMOBILEJKNFKTP"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String CONSIDMOBILEJKNFKTP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("CONSIDMOBILEJKNFKTP"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERKEYMOBILEJKNFKTP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERKEYMOBILEJKNFKTP"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSMOBILEJKNFKTP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSMOBILEJKNFKTP"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERMOBILEJKNFKTP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERMOBILEJKNFKTP"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIPATHPEMBAYARANPIHAKKETIGA(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIPATHPEMBAYARANPIHAKKETIGA"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIPATHPEMBAYARANPAJAK(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIPATHPEMBAYARANPAJAK"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIPATHPEMBAYARANVIRTUALACCOUNT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIPATHPEMBAYARANVIRTUALACCOUNT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIPATHACK(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIPATHACK"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIPATHMT940(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIPATHMT940"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIHOST(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIHOST"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIPORT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIPORT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIUSER(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIUSER"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPMANDIRIPAS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPMANDIRIPAS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String KUNCIDOKTERRANAP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("KUNCIDOKTERRANAP"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String ADDANTRIANAPIMOBILEJKNFKTP(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("ADDANTRIANAPIMOBILEJKNFKTP");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPIESIGN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPIESIGN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String USERNAMEAPIESIGN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("USERNAMEAPIESIGN"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String PASSAPIESIGN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("PASSAPIESIGN"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPFILEESIGNHOST(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPFILEESIGNHOST"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPFILEESIGNPORT(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPFILEESIGNPORT"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPFILEESIGNUSER(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPFILEESIGNUSER"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPFILEESIGNPAS(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPFILEESIGNPAS"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String SFTPFILEESIGNFOLDER(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("SFTPFILEESIGNFOLDER"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAKSESFILEESIGN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAKSESFILEESIGN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLAPISERTISIGN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLAPISERTISIGN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String APIKEYSERTISIGN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=EnkripsiAES.decrypt(prop.getProperty("APIKEYSERTISIGN"));
        }catch(Exception e){
            var=""; 
        }
        return var;
    }
    
    public static String URLDOKUMENSERTISIGN(){
        try{
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            var=prop.getProperty("URLDOKUMENSERTISIGN");
        }catch(Exception e){
            var=""; 
        }
        return var;
    }

//        long startTime = System.currentTimeMillis();
//            // Catat waktu setelah form utama ditampilkan
//            long endTime = System.currentTimeMillis();
//            long duration = endTime - startTime;
//            System.out.println("Notif Waktu startup: " + duration + " ms");
//               System.out.println("Waktu sekarang KDB: "+System.currentTimeMillis());


}