package com.example.oldstuffmarket.data_models;

public class SanPham {
    private String sID, sUserID, sShopID, sTenSP, sSPImage, sMoTa, sDanhMuc, sNgayDang, sDiaChiDang;
    private long lGiaTien;
    private int iSoLuong, iTinhTrang;

    public SanPham() {
    }

    public SanPham(String sID, String sUserID, String sShopID, String sTenSP, String sSPImage, String sMoTa, String sDanhMuc, String sNgayDang, String sDiaChiDang, long lGiaTien, int iSoLuong, int iTinhTrang) {
        this.sID = sID;
        this.sUserID = sUserID;
        this.sShopID = sShopID;
        this.sTenSP = sTenSP;
        this.sSPImage = sSPImage;
        this.sMoTa = sMoTa;
        this.sDanhMuc = sDanhMuc;
        this.sNgayDang = sNgayDang;
        this.sDiaChiDang = sDiaChiDang;
        this.lGiaTien = lGiaTien;
        this.iSoLuong = iSoLuong;
        this.iTinhTrang = iTinhTrang;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsUserID() {
        return sUserID;
    }

    public void setsUserID(String sUserID) {
        this.sUserID = sUserID;
    }

    public String getsShopID() {
        return sShopID;
    }

    public void setsShopID(String sShopID) {
        this.sShopID = sShopID;
    }

    public String getsTenSP() {
        return sTenSP;
    }

    public void setsTenSP(String sTenSP) {
        this.sTenSP = sTenSP;
    }

    public String getsSPImage() {
        return sSPImage;
    }

    public void setsSPImage(String sSPImage) {
        this.sSPImage = sSPImage;
    }

    public String getsMoTa() {
        return sMoTa;
    }

    public void setsMoTa(String sMoTa) {
        this.sMoTa = sMoTa;
    }

    public String getsDanhMuc() {
        return sDanhMuc;
    }

    public void setsDanhMuc(String sDanhMuc) {
        this.sDanhMuc = sDanhMuc;
    }

    public String getsNgayDang() {
        return sNgayDang;
    }

    public void setsNgayDang(String sNgayDang) {
        this.sNgayDang = sNgayDang;
    }

    public String getsDiaChiDang() {
        return sDiaChiDang;
    }

    public void setsDiaChiDang(String sDiaChiDang) {
        this.sDiaChiDang = sDiaChiDang;
    }

    public long getlGiaTien() {
        return lGiaTien;
    }

    public void setlGiaTien(long lGiaTien) {
        this.lGiaTien = lGiaTien;
    }

    public int getiSoLuong() {
        return iSoLuong;
    }

    public void setiSoLuong(int iSoLuong) {
        this.iSoLuong = iSoLuong;
    }

    public int getiTinhTrang() {
        return iTinhTrang;
    }

    public void setiTinhTrang(int iTinhTrang) {
        this.iTinhTrang = iTinhTrang;
    }
}
