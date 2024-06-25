package database;

import java.io.FileWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Mahasiswa;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class Database {
    private static final String URL = "jdbc:sqlite:C:/Users/Ayaa/OneDrive/Documents/BISMILLA/Gym/gymApp.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                System.out.println("Connected to SQLite database successfully!");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unable to connect to SQLite database.");
            }
        }
        return connection;
    }
    
    public static void insertMahasiswa(Mahasiswa mahasiswa) throws SQLException {
        String query = "INSERT INTO mahasiswa (email, nama, nim, kelas, telp, jk, tgl) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, mahasiswa.getEmail());
            ps.setString(2, mahasiswa.getNama());
            ps.setString(3, mahasiswa.getNim());
            ps.setString(4, mahasiswa.getKelas());
            ps.setString(5, mahasiswa.getTelp());
            ps.setString(6, mahasiswa.getJk());
            ps.setDate(7, Date.valueOf(mahasiswa.getTgl()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data mahasiswa berhasil disimpan.");
            } else {
                System.out.println("Gagal menyimpan data mahasiswa.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
            throw e;
        }
    }

    public static List<Mahasiswa> searchMahasiswa(String keyword) throws SQLException {
    List<Mahasiswa> mahasiswaList = new ArrayList<>();
    String sql = "SELECT * FROM mahasiswa WHERE nama LIKE ? OR nim LIKE ?";
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    
    try {
        connection = getConnection(); // Memastikan koneksi tersedia
        statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + keyword + "%");
        statement.setString(2, "%" + keyword + "%");
        
        resultSet = statement.executeQuery();
        
        while (resultSet.next()) {
            Mahasiswa mahasiswa = new Mahasiswa(
                resultSet.getString("email"),
                resultSet.getString("nama"),
                resultSet.getString("nim"),
                resultSet.getString("kelas"),
                resultSet.getString("telp"),
                resultSet.getString("jk"),
                resultSet.getDate("tgl").toLocalDate()
            );
            mahasiswaList.add(mahasiswa);
        }
        
    } catch (SQLException e) {
        System.err.println("Error searching data: " + e.getMessage());
        throw e;
    } finally {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
    }
    
    return mahasiswaList;
}

    public static void deleteMahasiswa(String email) throws SQLException {
        String query = "DELETE FROM mahasiswa WHERE email=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, email);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data mahasiswa berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus data mahasiswa.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting data: " + e.getMessage());
            throw e;
        }
    }

    public static void updateMahasiswa(Mahasiswa mahasiswa) throws SQLException {
        String query = "UPDATE mahasiswa SET nama=?, nim=?, kelas=?, telp=?, jk=?, tgl=? WHERE email=?";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, mahasiswa.getNama());
            ps.setString(2, mahasiswa.getNim());
            ps.setString(3, mahasiswa.getKelas());
            ps.setString(4, mahasiswa.getTelp());
            ps.setString(5, mahasiswa.getJk());
            ps.setDate(6, Date.valueOf(mahasiswa.getTgl()));
            ps.setString(7, mahasiswa.getEmail());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data mahasiswa berhasil diupdate.");
            } else {
                System.out.println("Gagal mengupdate data mahasiswa.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            throw e;
        }
    } 


    public static void exportMahasiswaToCSV(String filePath) throws SQLException, IOException {
    if (!filePath.toLowerCase().endsWith(".csv")) {
        filePath += ".csv";
    }
    
    String query = "SELECT * FROM mahasiswa";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery();
         FileWriter csvWriter = new FileWriter(filePath)) {

        csvWriter.append("Email,Nama,NIM,Kelas,No Telepon,Jenis Kelamin,Tanggal Kunjungan\n");

        while (rs.next()) {
            String email = rs.getString("email");
            String nama = rs.getString("nama");
            String nim = rs.getString("nim");
            String kelas = rs.getString("kelas");
            String telp = rs.getString("telp");
            String jk = rs.getString("jk");
            LocalDate tgl = rs.getDate("tgl").toLocalDate();
            String formattedDate = tgl.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            csvWriter.append(String.join(",", email, nama, nim, kelas, telp, jk, formattedDate));
            csvWriter.append("\n");
        }
    } catch (SQLException | IOException e) {
        System.out.println("Error: " + e.getMessage());
        throw e;
    }
}   
    
    public static void exportMahasiswaToPDF(String filePath) throws SQLException, IOException {
    if (!filePath.toLowerCase().endsWith(".pdf")) {
        filePath += ".pdf";
    }
    String query = "SELECT * FROM mahasiswa";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery();
         PDDocument document = new PDDocument()) {

        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDType0Font robotoBold = PDType0Font.load(document, new File("C:/Users/Ayaa/OneDrive/Documents/BISMILLA/PeminjamanGym/PeminjamanGym/src/fonts/Roboto-Bold.ttf"));
        PDType0Font robotoRegular = PDType0Font.load(document, new File("C:/Users/Ayaa/OneDrive/Documents/BISMILLA/PeminjamanGym/PeminjamanGym/src/fonts/Roboto-Regular.ttf"));

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Use Roboto-Bold for the title
            contentStream.setFont(robotoBold, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Data Mahasiswa");
            contentStream.endText();

            float margin = 50;
            float yStart = 700;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float lineHeight = 15;

            // Header kolom using Roboto-Bold
            String[] headers = { "Nama", "NIM", "Kelas", "No Telepon", "Jenis Kelamin", "Tanggal Kunjungan"};
            for (int i = 0; i < headers.length; i++) {
                contentStream.beginText();
                contentStream.setFont(robotoBold, 10);
                contentStream.newLineAtOffset(margin + i * (tableWidth / headers.length), yPosition);
                contentStream.showText(headers[i]);
                contentStream.endText();
            }
            yPosition -= lineHeight;

            // Data rows using Roboto-Regular
            while (rs.next()) {
               
                String nama = rs.getString("nama");
                String nim = rs.getString("nim");
                String kelas = rs.getString("kelas");
                String telp = rs.getString("telp");
                String jk = rs.getString("jk");
                LocalDate tgl = rs.getDate("tgl").toLocalDate();
                String formattedDate = tgl.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                String[] rowData = {nama, nim, kelas, telp, jk, formattedDate};
                
                
                for (int i = 0; i < rowData.length; i++) {
                    contentStream.beginText();
                    contentStream.setFont(robotoRegular, 10);
                    contentStream.newLineAtOffset(margin + i * (tableWidth / headers.length), yPosition);
                    contentStream.showText(rowData[i]);
                    contentStream.endText();
                }

                yPosition -= lineHeight;
            }
        }

        document.save(filePath);
        document.close();
    } catch (SQLException | IOException e) {
        System.out.println("Error: " + e.getMessage());
        throw e;
    }
}
}