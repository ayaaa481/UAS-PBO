package controller;

import model.Mahasiswa;
import view.ViewFormulir;
import database.Database;
import java.time.LocalDate;
import java.time.ZoneId;
import view.ViewDashboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ControllerFormulir {
    private final ViewFormulir viewFormulir;
    private ViewDashboard viewDashboard;

   public ControllerFormulir(ViewFormulir viewFormulir, ViewDashboard viewDashboard) {
        this.viewFormulir = viewFormulir;
        this.viewDashboard = viewDashboard;
}

    public ControllerFormulir(ViewFormulir viewFormulir) {
        this.viewFormulir = viewFormulir;
    }

    private void initController() {
        JButton simpanButton = viewFormulir.getSimpanButton();
        if (simpanButton != null) {
            simpanButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        insert(); // Call insert() method when simpanButton is clicked
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(viewFormulir, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } else {
            System.out.println("Simpan Button is null!");
        }
    } 
    public void insert() throws SQLException {
    String email = viewFormulir.getEmailTextField().getText();
    String nama = viewFormulir.getNamaTextField().getText();
    String nim = viewFormulir.getNimTextField().getText();
    String kelas = viewFormulir.getKelasTextField().getText();
    String telp = viewFormulir.getTelpTextField().getText();
    String jk = viewFormulir.getLakiRadioButton().isSelected() ? "Laki-laki" : "Perempuan";

    java.util.Date date = viewFormulir.getTglDateChooser().getDate();
    if (date == null) {
        JOptionPane.showMessageDialog(viewFormulir, "Tanggal harus dipilih", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    LocalDate tglLocal = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    Mahasiswa mahasiswa = new Mahasiswa(email, nama, nim, kelas, telp, jk, tglLocal);

    try {
        Database.insertMahasiswa(mahasiswa);
        JOptionPane.showMessageDialog(viewFormulir, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        if (viewDashboard != null) {
            viewDashboard.showDashboard();
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(viewFormulir, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}