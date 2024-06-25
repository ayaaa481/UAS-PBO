// ImplementMahasiswa.java
package dao;

import java.util.List;
import model.Mahasiswa;

public interface ImplementMahasiswa {
    public void insert(Mahasiswa b);
    public void update(Mahasiswa b);
    public void delete(Mahasiswa b);
    
    public List<Mahasiswa> getALL();
    public List<Mahasiswa> getCariNama(String nama);
    
}
