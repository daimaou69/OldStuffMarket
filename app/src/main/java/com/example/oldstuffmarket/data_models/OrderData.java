package com.example.oldstuffmarket.data_models;

import java.util.Date;

public class OrderData {
    private String donHangID, nguoiMuaID, nguoiBanID, ngayTaoDonHang, soDienThoai, diaChi;
    private SanPham sanPham;
    private int loaiDonHang, tinhTrang;
    private long giaTien;

    public OrderData() {
    }

    public OrderData(String donHangID, String nguoiMuaID, String nguoiBanID, String ngayTaoDonHang, String soDienThoai, String diaChi, SanPham sanPham, int loaiDonHang, int tinhTrang, long giaTien) {
        this.donHangID = donHangID;
        this.nguoiMuaID = nguoiMuaID;
        this.nguoiBanID = nguoiBanID;
        this.ngayTaoDonHang = ngayTaoDonHang;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.sanPham = sanPham;
        this.loaiDonHang = loaiDonHang;
        this.tinhTrang = tinhTrang;
        this.giaTien = giaTien;
    }

    public String getDonHangID() {
        return donHangID;
    }

    public void setDonHangID(String donHangID) {
        this.donHangID = donHangID;
    }

    public String getNguoiMuaID() {
        return nguoiMuaID;
    }

    public void setNguoiMuaID(String nguoiMuaID) {
        this.nguoiMuaID = nguoiMuaID;
    }

    public String getNguoiBanID() {
        return nguoiBanID;
    }

    public void setNguoiBanID(String nguoiBanID) {
        this.nguoiBanID = nguoiBanID;
    }

    public String getNgayTaoDonHang() {
        return ngayTaoDonHang;
    }

    public void setNgayTaoDonHang(String ngayTaoDonHang) {
        this.ngayTaoDonHang = ngayTaoDonHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public int getLoaiDonHang() {
        return loaiDonHang;
    }

    public void setLoaiDonHang(int loaiDonHang) {
        this.loaiDonHang = loaiDonHang;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public long getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(long giaTien) {
        this.giaTien = giaTien;
    }
}
