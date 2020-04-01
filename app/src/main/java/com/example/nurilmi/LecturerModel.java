package com.example.nurilmi;

public class LecturerModel {

    public LecturerModel(String id_lecturer, String id_lesson, String nama_lecturer, String tanggal, String pendidikan, String phoneNumber, String alamat) {
        this.id_lecturer = id_lecturer;
        this.id_lesson = id_lesson;
        this.nama_lecturer = nama_lecturer;
        this.tanggal = tanggal;
        this.pendidikan = pendidikan;
        this.phoneNumber = phoneNumber;
        this.alamat = alamat;
    }

    private String id_lecturer, nama_lecturer, tanggal, pendidikan, phoneNumber, alamat, id_lesson;

    public String getId_lesson() {
        return id_lesson;
    }

    public void setId_lesson(String id_lesson) {
        this.id_lesson = id_lesson;
    }

    public String getId_lecturer() {
        return id_lecturer;
    }

    public void setId_lecturer(String id_lecturer) {
        this.id_lecturer = id_lecturer;
    }

    public String getNama_lecturer() {
        return nama_lecturer;
    }

    public void setNama_lecturer(String nama_lecturer) {
        this.nama_lecturer = nama_lecturer;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
