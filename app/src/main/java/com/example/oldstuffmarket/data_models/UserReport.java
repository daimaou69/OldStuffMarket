package com.example.oldstuffmarket.data_models;

public class UserReport {
    private String reportID, objectReportID, reportDescription;
    private int loaiReport;
    private boolean tinhTrangXem;

    public UserReport() {
    }

    public UserReport(String reportID, String objectReportID, String reportDescription, int loaiReport, boolean tinhTrangXem) {
        this.reportID = reportID;
        this.objectReportID = objectReportID;
        this.reportDescription = reportDescription;
        this.loaiReport = loaiReport;
        this.tinhTrangXem = tinhTrangXem;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getObjectReportID() {
        return objectReportID;
    }

    public void setObjectReportID(String objectReportID) {
        this.objectReportID = objectReportID;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public int getLoaiReport() {
        return loaiReport;
    }

    public void setLoaiReport(int loaiReport) {
        this.loaiReport = loaiReport;
    }

    public boolean isTinhTrangXem() {
        return tinhTrangXem;
    }

    public void setTinhTrangXem(boolean tinhTrangXem) {
        this.tinhTrangXem = tinhTrangXem;
    }
}
