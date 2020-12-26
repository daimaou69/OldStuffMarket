package com.example.oldstuffmarket.data_models;

public class RemoveProductData {
    private String sID;
    private SanPham sanPham;

    public RemoveProductData() {
    }

    public RemoveProductData(String sID, SanPham sanPham) {
        this.sID = sID;
        this.sanPham = sanPham;
    }

    public String getsID() {
        return sID;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}
