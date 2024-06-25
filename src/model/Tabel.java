package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class Tabel extends AbstractTableModel {

    private List<Mahasiswa> listMahasiswa;
    private final String[] columnNames = {
        "Email", "Nama", "NIM", "Kelas", "No Telepon", "Jenis Kelamin", "Tanggal Penggunaan Ruang Gym"
    };

    public Tabel(List<Mahasiswa> listMahasiswa) {
        this.listMahasiswa = listMahasiswa;
    }
    
    public Tabel() {
        this.listMahasiswa = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        if (this.listMahasiswa == null) {
            return 0;
        }
        return this.listMahasiswa.size();
    }

    public void setListMahasiswa(List<Mahasiswa> listMahasiswa) {
        if (listMahasiswa == null) {
            this.listMahasiswa = new ArrayList<>();
        } else {
            this.listMahasiswa = listMahasiswa;
        }
    }


    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Mahasiswa mahasiswa = listMahasiswa.get(row);
        switch (column) {
            case 0:
                return mahasiswa.getEmail();
            case 1:
                return mahasiswa.getNama();
            case 2:
                return mahasiswa.getNim();
            case 3:
                return mahasiswa.getKelas();
            case 4:
                return mahasiswa.getTelp();
            case 5:
                return mahasiswa.getJk();
            case 6:
                return mahasiswa.getTgl();
            default:
                return null;
        }
    }
}
