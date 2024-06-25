package model;

import dao.ImplementMahasiswa;
import database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDAO implements ImplementMahasiswa {
    private Connection connection;

    public MahasiswaDAO() throws SQLException {
        this.connection = Database.getConnection();
    }

    @Override
    public void insert(Mahasiswa mhs) {
        String sql = "INSERT INTO mahasiswa (email, nama, nim, kelas, telp, jk, tgl) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, mhs.getEmail());
            ps.setString(2, mhs.getNama());
            ps.setString(3, mhs.getNim());
            ps.setString(4, mhs.getKelas());
            ps.setString(5, mhs.getTelp());
            ps.setString(6, mhs.getJk());
            ps.setDate(7, Date.valueOf(mhs.getTgl()));

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data berhasil disimpan.");
            } else {
                System.out.println("Gagal menyimpan data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Mahasiswa mhs) {
        String sql = "UPDATE mahasiswa SET email=?, nama=?, kelas=?, telp=?, jk=?, tgl=? WHERE nim=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, mhs.getEmail());
            ps.setString(2, mhs.getNama());
            ps.setString(3, mhs.getKelas());
            ps.setString(4, mhs.getTelp());
            ps.setString(5, mhs.getJk());
            ps.setDate(6, Date.valueOf(mhs.getTgl()));
            ps.setString(7, mhs.getNim());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data berhasil diupdate.");
            } else {
                System.out.println("Gagal mengupdate data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   @Override
    public void delete(Mahasiswa mhs) {
        String sql = "DELETE FROM mahasiswa WHERE nim=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, mhs.getNim());

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Data berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Mahasiswa> getALL() {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Mahasiswa mhs = new Mahasiswa();
                mhs.setEmail(rs.getString("email"));
                mhs.setNama(rs.getString("nama"));
                mhs.setNim(rs.getString("nim"));
                mhs.setKelas(rs.getString("kelas"));
                mhs.setTelp(rs.getString("telp"));
                mhs.setJk(rs.getString("jk"));

                java.sql.Date sqlDate = rs.getDate("tgl");
                if (sqlDate != null) {
                    mhs.setTgl(sqlDate.toLocalDate());
                } else {
                    mhs.setTgl(null);
                }
                mahasiswaList.add(mhs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return mahasiswaList;
    }

    @Override
    public List<Mahasiswa> getCariNama(String nama) {
        List<Mahasiswa> list = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa WHERE nama LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + nama + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mahasiswa mhs = new Mahasiswa();
                    mhs.setEmail(rs.getString("email"));
                    mhs.setNama(rs.getString("nama"));
                    mhs.setNim(rs.getString("nim"));
                    mhs.setKelas(rs.getString("kelas"));
                    mhs.setTelp(rs.getString("telp"));
                    mhs.setJk(rs.getString("jk"));

                    // Ensure you correctly convert java.sql.Date to LocalDate
                    java.sql.Date sqlDate = rs.getDate("tgl");
                    if (sqlDate != null) {
                        mhs.setTgl(sqlDate.toLocalDate());
                    } else {
                        mhs.setTgl(null);
                    }
                    list.add(mhs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
