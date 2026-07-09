package rekammedis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import fungsi.koneksiDB;

public class RMPenilaianAwalFisikospritualCare extends JDialog {
    private JTextField txtNoRm;
    private JTextField txtNama;
    private JTextField txtNoRawat;
    private JTextField txtTanggal;
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);

        btnSimpan.addActionListener(e -> {
            try {
                if (simpanData()) {
                    JOptionPane.showMessageDialog(this, "Data Penilaian Awal Fisikospritual Care berhasil disimpan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBatal.addActionListener(e -> dispose());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(parent);
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

        try (Connection con = koneksiDB.condb(); PreparedStatement ps = con.prepareStatement(sql)) {
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
