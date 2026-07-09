package rekammedis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
            JOptionPane.showMessageDialog(this, "Data Penilaian Awal Fisikospritual Care berhasil disimpan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        btnBatal.addActionListener(e -> dispose());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(parent);
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
