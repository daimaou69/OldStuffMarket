package com.example.oldstuffmarket.data_models;

public class RemoveProductData {
    private String sID, sMoTa;
    private SanPham sanPham;

    public RemoveProductData() {
    }

    public RemoveProductData(String sID, String sMoTa, SanPham sanPham) {
        this.sID = sID;
        this.sMoTa = sMoTa;
        this.sanPham = sanPham;
    }

    public String getsID() {
        return sID;
    }

    public String getsMoTa() {
        return sMoTa;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public void setsMoTa(String sMoTa) {
        this.sMoTa = sMoTa;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}
