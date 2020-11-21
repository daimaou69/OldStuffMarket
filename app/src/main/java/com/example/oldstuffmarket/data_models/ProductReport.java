package com.example.oldstuffmarket.data_models;

public class ProductReport {
    private String reportID, userID, lyDoBaoCao;
    private SanPham sanPham;

    public ProductReport() {
    }

    public ProductReport(String reportID, String userID, String lyDoBaoCao, SanPham sanPham) {
        this.reportID = reportID;
        this.userID = userID;
        this.lyDoBaoCao = lyDoBaoCao;
        this.sanPham = sanPham;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReportID() {
        return reportID;
    }

    public String getLyDoBaoCao() {
        return lyDoBaoCao;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public void setLyDoBaoCao(String lyDoBaoCao) {
        this.lyDoBaoCao = lyDoBaoCao;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}
