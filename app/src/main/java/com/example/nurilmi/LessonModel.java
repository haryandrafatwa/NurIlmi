package com.example.nurilmi;

public class LessonModel {

    private String id_lesson, nama_lesson, tanggal;

    public LessonModel(String id_lesson, String nama_lesson, String tanggal) {
        this.id_lesson = id_lesson;
        this.nama_lesson = nama_lesson;
        this.tanggal = tanggal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getId_lesson() {
        return id_lesson;
    }

    public void setId_lesson(String id_lesson) {
        this.id_lesson = id_lesson;
    }

    public String getNama_lesson() {
        return nama_lesson;
    }

    public void setNama_lesson(String nama_lesson) {
        this.nama_lesson = nama_lesson;
    }
}
