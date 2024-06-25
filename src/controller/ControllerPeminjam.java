/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ImplementMahasiswa;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import model.Mahasiswa;
import model.MahasiswaDAO;
import model.Tabel;
import view.ViewPeminjam;

/**
 *
 * @author Ayaa
 */
public class ControllerPeminjam {
    ViewPeminjam framePeminjam;
    ImplementMahasiswa implementMahasiswa;
    List <Mahasiswa> listMahasiswa;

    public ControllerPeminjam(ViewPeminjam framePeminjam) {
        this.framePeminjam= framePeminjam;
        try {
            implementMahasiswa = new MahasiswaDAO(); // Correct instantiation
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPeminjam.class.getName()).log(Level.SEVERE, null, ex);
        }
        listMahasiswa = implementMahasiswa.getALL();
        isiTabel(); // Mengisi tabel saat objek ControllerMahasiswa dibuat
    }
   
    public void insert() {
        if (!framePeminjam.getEmailTextField().getText().trim().isEmpty() &&
                !framePeminjam.getNamaTextField().getText().trim().isEmpty() &&
                !framePeminjam.getNimTextField().getText().trim().isEmpty() &&
                !framePeminjam.getKelasTextField().getText().trim().isEmpty() &&
                !framePeminjam.getTelpTextField().getText().trim().isEmpty() &&
                (framePeminjam.getLakiRadioButton().isSelected() || framePeminjam.getPerempuanRadioButton().isSelected()) &&
                framePeminjam.getTglDateChooser().getDate() != null) {
           
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(framePeminjam.getTglDateChooser().getDate());
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Get the day of the week

           String gender = "";
            if (framePeminjam.getLakiRadioButton().isSelected()) {
                gender = "Laki-laki";
            } else if (framePeminjam.getPerempuanRadioButton().isSelected()) {
                gender = "Perempuan";
            }
            Mahasiswa b = new Mahasiswa();
            b.setEmail(framePeminjam.getEmailTextField().getText());
            b.setNama(framePeminjam.getNamaTextField().getText());
            b.setKelas(framePeminjam.getKelasTextField().getText());
            b.setTelp(framePeminjam.getTelpTextField().getText());
            b.setJk(gender); // Set gender

            Date selectedDate = framePeminjam.getTglDateChooser().getDate();
            LocalDate localDate = DateUtil.convertToLocalDate(selectedDate);
            b.setTgl(localDate);

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    implementMahasiswa.insert(b); // Insert the data into the database
                    return null;
                }
                @Override
                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
                    } catch (InterruptedException | ExecutionException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
            worker.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("state") && SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                        isiTabel();
                    }
                }
            });

        } else {
            JOptionPane.showMessageDialog(framePeminjam, "Semua data harus diisi dan tanggal harus dipilih!");
        }
    }

    public void update() {
        if (!framePeminjam.getNimTextField().getText().trim().isEmpty()) {
            Mahasiswa b = new Mahasiswa();            
            b.setEmail(framePeminjam.getEmailTextField().getText());
            b.setNama(framePeminjam.getNamaTextField().getText());
            b.setKelas(framePeminjam.getKelasTextField().getText());
            b.setTelp(framePeminjam.getTelpTextField().getText());  
        if (framePeminjam.getLakiRadioButton().isSelected()) {
                b.setJk("Laki-laki");
            } else if (framePeminjam.getPerempuanRadioButton().isSelected()) {
                b.setJk("Perempuan");
            }

            Date tglGym = framePeminjam.getTglDateChooser().getDate();
            LocalDate localDate = DateUtil.convertToLocalDate(tglGym);
            b.setTgl(localDate);
            b.setNim(framePeminjam.getNimTextField().getText());
            implementMahasiswa.update(b);
            JOptionPane.showMessageDialog(null, "Data Berhasil di Update");
        } else {
            JOptionPane.showMessageDialog(framePeminjam, "Silakan Pilih Data yang Akan di Update");
        }
    }
    
    public void delete() {
        if (!framePeminjam.getNimTextField().getText().trim().isEmpty()) {
            String nim = framePeminjam.getNimTextField().getText();
            Mahasiswa mahasiswaToDelete = new Mahasiswa();
            mahasiswaToDelete.setNim(nim);
            implementMahasiswa.delete(mahasiswaToDelete);

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } else {
            JOptionPane.showMessageDialog(framePeminjam, "Silakan Pilih Data yang Akan Di Hapus");
        }
    }

    public void isiTableCariNama() {
        listMahasiswa = implementMahasiswa.getCariNama(framePeminjam.getCariTextField().getText());
        Tabel tbl = new Tabel(listMahasiswa);
        framePeminjam.getMahasiswaTable().setModel(tbl);
    }

    public void cariNama() {
        if (!framePeminjam.getCariTextField().getText().trim().isEmpty()) {
            implementMahasiswa.getCariNama(framePeminjam.getCariTextField().getText());
            isiTableCariNama();
        } else {
            JOptionPane.showMessageDialog(framePeminjam, "Silahkan Pilih Data!");
        }
    }
    
    public class DateUtil {
    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
    }
}
    
     public void reset(){
        framePeminjam.getEmailTextField().setText("");
        framePeminjam.getNamaTextField().setText("");
        framePeminjam.getNimTextField().setText("");
        framePeminjam.getKelasTextField().setText("");
        framePeminjam.getTelpTextField().setText("");
        
        // Clear the selection of gender radio buttons
        framePeminjam.getLakiRadioButton().setSelected(false);
        framePeminjam.getPerempuanRadioButton().setSelected(false);
        
        // Assuming TglDateChooser is a JDateChooser
        framePeminjam.getTglDateChooser().setDate(null);
    }
    
    public void isiTabel(){  
        listMahasiswa = implementMahasiswa.getALL();
        Tabel tbl = new Tabel(listMahasiswa);
        framePeminjam.getMahasiswaTable().setModel(tbl);
    }
    
    public void isiFields(int row) {
        Mahasiswa selectedMahasiswa = listMahasiswa.get(row);
        framePeminjam.getEmailTextField().setText(selectedMahasiswa.getEmail());
        framePeminjam.getNamaTextField().setText(selectedMahasiswa.getNama());
        framePeminjam.getNimTextField().setText(selectedMahasiswa.getNim());
        framePeminjam.getKelasTextField().setText(selectedMahasiswa.getKelas());
        framePeminjam.getTelpTextField().setText(selectedMahasiswa.getTelp());
        
        String gender = selectedMahasiswa.getJk(); // Retrieve gender attribute
        if (gender.equals("Laki-laki")) {
            framePeminjam.getLakiRadioButton().setSelected(true);
        } else if (gender.equals("Perempuan")) {
            framePeminjam.getPerempuanRadioButton().setSelected(true);
        }
        
        LocalDate localDate = selectedMahasiswa.getTgl();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        framePeminjam.getTglDateChooser().setDate(date);
    }
}
