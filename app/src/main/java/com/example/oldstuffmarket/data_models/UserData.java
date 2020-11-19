package com.example.oldstuffmarket.data_models;

public class UserData {
    private String sUserName, sShopID, sFullName, sSdt, sGioiTinh, sDiaChi, sPassword, sImage, sUserID, sNgayThamGia;
    private int iPermission, iCommission, iTinhTrang, iSoSPDaBan, iAccPoint, iReport;
    private long lMoney;

    public UserData() {}

    public UserData(String sUserName, String sShopID, String sFullName, String sSdt, String sGioiTinh, String sDiaChi, String sPassword, String sImage, String sUserID, String sNgayThamGia, int iPermission, int iCommission, int iTinhTrang, int iSoSPDaBan, int iAccPoint, int iReport, long lMoney) {
        this.sUserName = sUserName;
        this.sShopID = sShopID;
        this.sFullName = sFullName;
        this.sSdt = sSdt;
        this.sGioiTinh = sGioiTinh;
        this.sDiaChi = sDiaChi;
        this.sPassword = sPassword;
        this.sImage = sImage;
        this.sUserID = sUserID;
        this.sNgayThamGia = sNgayThamGia;
        this.iPermission = iPermission;
        this.iCommission = iCommission;
        this.iTinhTrang = iTinhTrang;
        this.iSoSPDaBan = iSoSPDaBan;
        this.iAccPoint = iAccPoint;
        this.iReport = iReport;
        this.lMoney = lMoney;
    }

    public String getsUserName() {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public String getsShopID() {
        return sShopID;
    }

    public void setsShopID(String sShopID) {
        this.sShopID = sShopID;
    }

    public String getsFullName() {
        return sFullName;
    }

    public void setsFullName(String sFullName) {
        this.sFullName = sFullName;
    }

    public String getsSdt() {
        return sSdt;
    }

    public void setsSdt(String sSdt) {
        this.sSdt = sSdt;
    }

    public String getsGioiTinh() {
        return sGioiTinh;
    }

    public void setsGioiTinh(String sGioiTinh) {
        this.sGioiTinh = sGioiTinh;
    }

    public String getsDiaChi() {
        return sDiaChi;
    }

    public void setsDiaChi(String sDiaChi) {
        this.sDiaChi = sDiaChi;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public String getsUserID() {
        return sUserID;
    }

    public void setsUserID(String sUserID) {
        this.sUserID = sUserID;
    }

    public String getsNgayThamGia() {
        return sNgayThamGia;
    }

    public void setsNgayThamGia(String sNgayThamGia) {
        this.sNgayThamGia = sNgayThamGia;
    }

    public int getiPermission() {
        return iPermission;
    }

    public void setiPermission(int iPermission) {
        this.iPermission = iPermission;
    }

    public int getiCommission() {
        return iCommission;
    }

    public void setiCommission(int iCommission) {
        this.iCommission = iCommission;
    }

    public int getiTinhTrang() {
        return iTinhTrang;
    }

    public void setiTinhTrang(int iTinhTrang) {
        this.iTinhTrang = iTinhTrang;
    }

    public int getiSoSPDaBan() {
        return iSoSPDaBan;
    }

    public void setiSoSPDaBan(int iSoSPDaBan) {
        this.iSoSPDaBan = iSoSPDaBan;
    }

    public int getiAccPoint() {
        return iAccPoint;
    }

    public void setiAccPoint(int iAccPoint) {
        this.iAccPoint = iAccPoint;
    }

    public int getiReport() {
        return iReport;
    }

    public void setiReport(int iReport) {
        this.iReport = iReport;
    }

    public long getlMoney() {
        return lMoney;
    }

    public void setlMoney(long lMoney) {
        this.lMoney = lMoney;
    }
}
