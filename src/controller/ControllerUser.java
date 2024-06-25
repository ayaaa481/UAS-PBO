/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import database.Database;
import static database.Database.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

/**
 *
 * @author Ayaa
 */
public class ControllerUser {
    public static void insertUser(User user) throws SQLException {
    if (user == null || user.getNim() == null || user.getNama() == null || user.getPassword() == null) {
        throw new IllegalArgumentException("User object or its properties cannot be null.");
    }
    String sql = "INSERT INTO users (nim, nama, password, role) VALUES (?, ?, ?,?)";
    Connection conn = null;
    try {
        conn = Database.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getNim());
            pstmt.setString(2, user.getNama());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, "user"); // Default role untuk pengguna baru            
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            System.out.println("Error inserting user: " + e.getMessage());
            throw e;
        }
    } catch (SQLException e) {
        System.out.println("Database connection error: " + e.getMessage());
        throw e;
    } finally {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); // Set kembali ke auto-commit mode
                conn.close(); // Menutup koneksi ke database
            } catch (SQLException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }
    }
}
    
     public static User getUserInfo(String nim) throws SQLException {
        User user = null;
        String query = "SELECT * FROM users WHERE nim = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nim);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nama = rs.getString("nama");
                String password = rs.getString("password");
                // Misalkan Anda juga memiliki kolom role untuk menentukan admin atau bukan
                boolean isAdmin = rs.getBoolean("isAdmin");

                user = new User(nim, nama, password);
                user.setAdmin(isAdmin);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        return user;
    }


   public static String validateUser(String nim, String password) throws SQLException {
    String role = ""; // Inisialisasi peran (role) user

    // Misalnya, Anda ingin menentukan bahwa user adalah admin jika nim=123 dan password=admin
    if (nim.equals("123") && password.equals("admin")) {
        role = "admin";
    } else {
        // Jika bukan admin, lakukan validasi di database
        String query = "SELECT role FROM users WHERE nim = ? AND password = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, nim);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                role = rs.getString("role"); // Ambil peran (role) dari database
            }
        } catch (SQLException e) {
            System.out.println("Error validating user: " + e.getMessage());
            throw e;
        }
    }
    
    return role;
    }

}
