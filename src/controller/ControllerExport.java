package controller;

import database.Database;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import view.ViewExport;

public class ControllerExport {
    private DefaultTableModel tableModel;
    private final ViewExport view;
    
    public ControllerExport(ViewExport view) {
        this.view = view;
        this.tableModel = new DefaultTableModel();
        this.view.getMhsTable().setModel(tableModel); // Mengatur model tabel
        
        initController();
    }
    
    private void initController() {
        // Mengatur event handler untuk label CSV
        view.getCsvLabel().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                csvLabelMouseClicked(evt);
            }
        });
        
        //Mengatur event handler untuk label PDF (jika diperlukan)
        view.getPdfLabel().addMouseListener(new java.awt.event.MouseAdapter() {
             @Override
             public void mouseClicked(java.awt.event.MouseEvent evt) {
                 pdfLabelMouseClicked(evt);
             }

            private void pdfLabelMouseClicked(MouseEvent evt) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });
    }
    
    private void csvLabelMouseClicked(java.awt.event.MouseEvent evt) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Export to CSV");
            
            int userSelection = fileChooser.showSaveDialog(view);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".csv";
                Database.exportMahasiswaToCSV(filePath);
                view.showMessage("Data berhasil diekspor ke " + filePath, "Export Success");
            }
        } catch (SQLException | IOException ex) {
            view.showMessage("Error: Gagal melakukan ekspor data ke CSV.", "Export Error");
            Logger.getLogger(ControllerExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
}
