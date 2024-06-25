package model;

import java.sql.Date;
import java.time.LocalDate;

public class Mahasiswa {
    private String email;
    private String nama;
    private String nim;
    private String kelas;
    private String telp;
    private String jk;
    private LocalDate tgl;
    

    // Constructor
    public Mahasiswa(String email, String nama, String nim, String kelas, String telp, String jk, LocalDate tgl) {
        this.email = email;
        this.nama = nama;
        this.nim = nim;
        this.kelas = kelas;
        this.telp = telp;
        this.jk = jk;
        this.tgl = tgl;
    }

    public Mahasiswa(String email, String nama, String nim, String kelas, String telp, String jk, Date sqlDate) {
    }

    public Mahasiswa() {
        // Default constructor
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public LocalDate getTgl() {
        return tgl;
    }

    /**
     *
     * @param tgl
     */
    public void setTgl(LocalDate tgl) {
        this.tgl = tgl;
    }
}