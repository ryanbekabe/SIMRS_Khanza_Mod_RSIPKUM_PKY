package rekammedis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import fungsi.koneksiDB;

public class RMPenilaianAwalFisikospritualCare extends JDialog {
    private static final boolean DEBUG = true;
    private JTextField txtNoRm;
    private JTextField txtNama;
    private JTextField txtNoRawat;
    private JTextField txtTanggal;
    private JTextField txtTanggalAwal;
    private JTextField txtTanggalAkhir;
    private JTable tblData;
    private DefaultTableModel tblModel;
    private JComboBox<String> cmbKecemasan;
    private JComboBox<String> cmbMotivasi;
    private JComboBox<String> cmbIbadahMandiri;
    private JComboBox<String> cmbTataCaraShalat;
    private JComboBox<String> cmbKunjunganOperasi;
    private JComboBox<String> cmbKritisTerminal;

    public RMPenilaianAwalFisikospritualCare(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        setTitle("Penilaian Awal Fisikospritual Care");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel headerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        headerPanel.add(new JLabel("No. RM"), gbc);

        gbc.gridx = 1;
        txtNoRm = new JTextField();
        txtNoRm.setPreferredSize(new Dimension(140, 25));
        txtNoRm.setEditable(false);
        headerPanel.add(txtNoRm, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        headerPanel.add(new JLabel("Nama"), gbc);

        gbc.gridx = 1;
        txtNama = new JTextField();
        txtNama.setPreferredSize(new Dimension(240, 25));
        txtNama.setEditable(false);
        headerPanel.add(txtNama, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        headerPanel.add(new JLabel("No. Rawat"), gbc);

        gbc.gridx = 1;
        txtNoRawat = new JTextField();
        txtNoRawat.setPreferredSize(new Dimension(140, 25));
        txtNoRawat.setEditable(false);
        headerPanel.add(txtNoRawat, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        headerPanel.add(new JLabel("Tanggal"), gbc);

        gbc.gridx = 1;
        txtTanggal = new JTextField();
        txtTanggal.setPreferredSize(new Dimension(140, 25));
        txtTanggal.setEditable(false);
        headerPanel.add(txtTanggal, gbc);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(4, 4, 4, 4);
        formGbc.anchor = GridBagConstraints.WEST;

        String[] choices = {"Ya", "Tidak"};
        int row = 0;

        addQuestion(formPanel, formGbc, row++, "Mengalami kecemasan atau ketakutan", cmbKecemasan = new JComboBox<>(choices));
        addQuestion(formPanel, formGbc, row++, "Memerlukan motivasi & edukasi dalam menghadapi kecemasan", cmbMotivasi = new JComboBox<>(choices));
        addQuestion(formPanel, formGbc, row++, "Mampu melaksanakan ibadah secara mandiri", cmbIbadahMandiri = new JComboBox<>(choices));
        addQuestion(formPanel, formGbc, row++, "Memahami tata cara shalat saat sakit dan bersuci (Tayamum)", cmbTataCaraShalat = new JComboBox<>(choices));
        addQuestion(formPanel, formGbc, row++, "Memerlukan kunjungan Menjelang operasi", cmbKunjunganOperasi = new JComboBox<>(choices));
        addQuestion(formPanel, formGbc, row++, "Pasien kritis/terminal", cmbKritisTerminal = new JComboBox<>(choices));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Tanggal Awal"));
        txtTanggalAwal = new JTextField(getTodayString(), 10);
        filterPanel.add(txtTanggalAwal);
        filterPanel.add(new JLabel("Tanggal Akhir"));
        txtTanggalAkhir = new JTextField(getTodayString(), 10);
        filterPanel.add(txtTanggalAkhir);
        JButton btnTampilkan = new JButton("Tampilkan");
        btnTampilkan.addActionListener(e -> tampilkanData());
        filterPanel.add(btnTampilkan);

        String[] columnNames = {"No. RM", "Nama", "No. Rawat", "Tanggal", "Kecemasan", "Motivasi", "Ibadah Mandiri", "Shalat", "Kunjungan Operasi", "Kritis/Terminal"};
        tblModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblData = new JTable(tblModel);
        tblData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblData.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblData.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblData.getColumnModel().getColumn(2).setPreferredWidth(180);
        tblData.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblData.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblData.getColumnModel().getColumn(5).setPreferredWidth(90);
        tblData.getColumnModel().getColumn(6).setPreferredWidth(110);
        tblData.getColumnModel().getColumn(7).setPreferredWidth(110);
        tblData.getColumnModel().getColumn(8).setPreferredWidth(120);
        tblData.getColumnModel().getColumn(9).setPreferredWidth(110);

        JPanel listPanel = new JPanel(new BorderLayout(6, 6));
        listPanel.setBorder(new EmptyBorder(8, 0, 0, 0));
        listPanel.add(filterPanel, BorderLayout.NORTH);
        listPanel.add(new JScrollPane(tblData), BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout(8, 8));
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(listPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);

        btnSimpan.addActionListener(e -> {
            try {
                if (simpanData()) {
                    JOptionPane.showMessageDialog(this, "Data Penilaian Awal Fisikospritual Care berhasil disimpan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    emptTeks();
                    tampilkanData();
                }
            } catch (Exception ex) {
                logException("Gagal menyimpan data", ex);
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBatal.addActionListener(e -> dispose());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(parent);
        tampilkanData();
    }

    private Connection openConnection() throws SQLException {
        log("Mencoba membuka koneksi database MySQL dengan kredensial hybrid web...");

        String host = koneksiDB.HOSTHYBRIDWEB();
        String user = koneksiDB.USERHYBRIDWEB();
        String pass = koneksiDB.PASHYBRIDWEB();
        String database = koneksiDB.DATABASE();
        String port = koneksiDB.PORTWEB();

        if (host == null || host.trim().isEmpty()) {
            host = "localhost";
        }
        if (port == null || port.trim().isEmpty()) {
            port = "3307";
        }
        if (database == null || database.trim().isEmpty()) {
            database = "sik";
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?zeroDateTimeBehavior=convertToNull&autoReconnect=true&useCompression=true";

        log("Mencoba koneksi ke URL: " + url + " dengan user=" + user);
        ensureMySqlDriverLoaded();
        Connection con = DriverManager.getConnection(url, user, pass);

        if (con == null || con.isClosed()) {
            log("Koneksi database tidak tersedia.");
            throw new SQLException("Koneksi database tidak tersedia.");
        }

        log("Koneksi database berhasil dibuat.");
        try {
            log("Detail koneksi: URL=" + con.getMetaData().getURL() + ", catalog=" + con.getCatalog());
        } catch (SQLException ex) {
            log("Tidak bisa membaca detail koneksi: " + ex.getMessage());
        }
        return con;
    }

    private void ensureMySqlDriverLoaded() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            log("Driver MySQL com.mysql.cj.jdbc.Driver berhasil dimuat.");
            return;
        } catch (ClassNotFoundException ex) {
            log("Driver com.mysql.cj.jdbc.Driver tidak ditemukan, mencoba driver lama...");
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            log("Driver MySQL com.mysql.jdbc.Driver berhasil dimuat.");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Driver MySQL tidak ditemukan di classpath.", ex);
        }
    }

    private void tampilkanData() {
        try {
            log("Memuat data dengan filter tanggal awal/akhir...");
            Date awal = parseDateInput(txtTanggalAwal.getText());
            Date akhir = parseDateInput(txtTanggalAkhir.getText());
            log("Filter tanggal: " + awal + " s/d " + akhir);
            if (awal != null && akhir != null && awal.after(akhir)) {
                JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh lebih besar dari tanggal akhir.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            loadData(awal, akhir);
        } catch (Exception ex) {
            log("Gagal memuat data: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData(Date tanggalAwal, Date tanggalAkhir) throws SQLException {
        String sql = "SELECT no_rm, nama, no_rawat, tanggal, mengalami_kecemasan, memerlukan_motivasi, "
                + "mampu_beribadah_mandiri, memahami_tata_cara_shalat, memerlukan_kunjungan_operasi, pasien_kritis_terminal "
                + "FROM penilaian_awal_fisikospritual_care "
                + "WHERE tanggal >= ? AND tanggal <= ? "
                + "ORDER BY tanggal DESC, no_rawat DESC LIMIT 200";

        tblModel.setRowCount(0);
        log("Menjalankan query load data: " + sql);
        try (Connection con = openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            java.sql.Date awal = tanggalAwal == null ? getTodaySqlDate() : new java.sql.Date(tanggalAwal.getTime());
            java.sql.Date akhir = tanggalAkhir == null ? getTodaySqlDate() : new java.sql.Date(tanggalAkhir.getTime());
            ps.setDate(1, awal);
            ps.setDate(2, akhir);
            log("Parameter query: tanggalAwal=" + awal + ", tanggalAkhir=" + akhir);
            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    tblModel.addRow(new Object[]{
                        rs.getString("no_rm"),
                        rs.getString("nama"),
                        rs.getString("no_rawat"),
                        rs.getDate("tanggal") != null ? new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("tanggal")) : "",
                        rs.getString("mengalami_kecemasan"),
                        rs.getString("memerlukan_motivasi"),
                        rs.getString("mampu_beribadah_mandiri"),
                        rs.getString("memahami_tata_cara_shalat"),
                        rs.getString("memerlukan_kunjungan_operasi"),
                        rs.getString("pasien_kritis_terminal")
                    });
                    rowCount++;
                }
                log("Data berhasil dimuat: " + rowCount + " record");
            }
        }
    }

    private Date parseDateInput(String value) throws ParseException {
        if (value == null || value.trim().isEmpty()) {
            return getTodayDate();
        }
        return new SimpleDateFormat("dd-MM-yyyy").parse(value.trim());
    }

    private String getTodayString() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    private Date getTodayDate() {
        return new Date();
    }

    private java.sql.Date getTodaySqlDate() {
        return new java.sql.Date(new Date().getTime());
    }

    private boolean simpanData() throws SQLException {
        String noRm = txtNoRm.getText().trim();
        String nama = txtNama.getText().trim();
        String noRawat = txtNoRawat.getText().trim();

        if (noRawat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No. Rawat belum terisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        java.sql.Date tanggal = null;
        try {
            tanggal = parseTanggal();
        } catch (ParseException ex) {
            throw new SQLException("Format tanggal tidak valid: " + ex.getMessage(), ex);
        }

        String sql = "INSERT INTO penilaian_awal_fisikospritual_care "
                + "(no_rm, nama, no_rawat, tanggal, mengalami_kecemasan, memerlukan_motivasi, "
                + "mampu_beribadah_mandiri, memahami_tata_cara_shalat, memerlukan_kunjungan_operasi, pasien_kritis_terminal) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        log("Menyimpan data penilaian awal fisikospritual care...");
        log("Payload yang akan disimpan: noRm=" + noRm + ", nama=" + nama + ", noRawat=" + noRawat + ", tanggal=" + tanggal
                + ", kecemasan=" + getComboValue(cmbKecemasan)
                + ", motivasi=" + getComboValue(cmbMotivasi)
                + ", ibadahMandiri=" + getComboValue(cmbIbadahMandiri)
                + ", shalat=" + getComboValue(cmbTataCaraShalat)
                + ", kunjunganOperasi=" + getComboValue(cmbKunjunganOperasi)
                + ", kritisTerminal=" + getComboValue(cmbKritisTerminal));
        try (Connection con = openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, noRm.isEmpty() ? null : noRm);
            ps.setString(2, nama.isEmpty() ? null : nama);
            ps.setString(3, noRawat);
            if (tanggal != null) {
                ps.setDate(4, tanggal);
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setString(5, getComboValue(cmbKecemasan));
            ps.setString(6, getComboValue(cmbMotivasi));
            ps.setString(7, getComboValue(cmbIbadahMandiri));
            ps.setString(8, getComboValue(cmbTataCaraShalat));
            ps.setString(9, getComboValue(cmbKunjunganOperasi));
            ps.setString(10, getComboValue(cmbKritisTerminal));
            ps.executeUpdate();
            log("Data berhasil disimpan ke database.");
            return true;
        }
    }

    private java.sql.Date parseTanggal() throws ParseException {
        String value = txtTanggal.getText() == null ? "" : txtTanggal.getText().trim();
        if (value.isEmpty()) {
            return null;
        }
        Date parsed = new SimpleDateFormat("dd-MM-yyyy").parse(value);
        return new java.sql.Date(parsed.getTime());
    }

    private void log(String message) {
        if (DEBUG) {
            System.out.println("[RMPenilaianAwalFisikospritualCare] " + message);
        }
    }

    private void logException(String context, Exception ex) {
        log(context + ": " + ex.getMessage());
        ex.printStackTrace();
    }

    private String getComboValue(JComboBox<String> comboBox) {
        if (comboBox == null || comboBox.getSelectedItem() == null) {
            return null;
        }
        return comboBox.getSelectedItem().toString();
    }

    private void addQuestion(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComboBox<String> comboBox) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(comboBox, gbc);
    }

    public void emptTeks() {
        txtNoRm.setText("");
        txtNama.setText("");
        txtNoRawat.setText("");
        txtTanggal.setText("");
        cmbKecemasan.setSelectedIndex(0);
        cmbMotivasi.setSelectedIndex(0);
        cmbIbadahMandiri.setSelectedIndex(0);
        cmbTataCaraShalat.setSelectedIndex(0);
        cmbKunjunganOperasi.setSelectedIndex(0);
        cmbKritisTerminal.setSelectedIndex(0);
    }

    public void setNoRawat(String noRawat, Date tanggal) {
        txtNoRawat.setText(noRawat);
        if (tanggal != null) {
            txtTanggal.setText(new SimpleDateFormat("dd-MM-yyyy").format(tanggal));
        } else {
            txtTanggal.setText("");
        }
    }

    public void setPasien(String noRm, String nama, String noRawat, Date tanggal) {
        txtNoRm.setText(noRm);
        txtNama.setText(nama);
        txtNoRawat.setText(noRawat);
        if (tanggal != null) {
            txtTanggal.setText(new SimpleDateFormat("dd-MM-yyyy").format(tanggal));
        } else {
            txtTanggal.setText("");
        }
    }
}
