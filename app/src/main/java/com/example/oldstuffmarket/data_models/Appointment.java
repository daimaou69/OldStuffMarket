package com.example.oldstuffmarket.data_models;

public class Appointment {
    private String appointmentID;
    private String tieuDe;
    private String ngayHen;
    private String moTaCuocHen;
    private String nguoiHenID;
    private String nguoiDuocHenID;
    private boolean active;

    public Appointment() {
    }

    public Appointment(String appointmentID, String tieuDe, String ngayHen, String moTaCuocHen, String nguoiHenID, String nguoiDuocHenID, boolean active) {
        this.appointmentID = appointmentID;
        this.tieuDe = tieuDe;
        this.ngayHen = ngayHen;
        this.moTaCuocHen = moTaCuocHen;
        this.nguoiHenID = nguoiHenID;
        this.nguoiDuocHenID = nguoiDuocHenID;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getNguoiHenID() {
        return nguoiHenID;
    }

    public void setNguoiHenID(String nguoiHenID) {
        this.nguoiHenID = nguoiHenID;
    }

    public String getNguoiDuocHenID() {
        return nguoiDuocHenID;
    }

    public void setNguoiDuocHenID(String nguoiDuocHenID) {
        this.nguoiDuocHenID = nguoiDuocHenID;
    }

    public String getNgayHen() {
        return ngayHen;
    }

    public void setNgayHen(String ngayHen) {
        this.ngayHen = ngayHen;
    }

    public String getMoTaCuocHen() {
        return moTaCuocHen;
    }

    public void setMoTaCuocHen(String moTaCuocHen) {
        this.moTaCuocHen = moTaCuocHen;
    }
}
