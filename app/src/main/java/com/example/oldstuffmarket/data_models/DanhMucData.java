package com.example.oldstuffmarket.data_models;

import java.util.ArrayList;

public class DanhMucData {
    private String sDanhMucID;
    private String sTenDanhMuc;
    private String sDanhMucIMG;

    public DanhMucData() {
    }

    public DanhMucData(String sDanhMucID, String sTenDanhMuc, String sDanhMucIMG) {
        this.sDanhMucID = sDanhMucID;
        this.sTenDanhMuc = sTenDanhMuc;
        this.sDanhMucIMG = sDanhMucIMG;
    }

    public String getsDanhMucID() {
        return sDanhMucID;
    }

    public void setsDanhMucID(String sDanhMucID) {
        this.sDanhMucID = sDanhMucID;
    }

    public String getsTenDanhMuc() {
        return sTenDanhMuc;
    }

    public void setsTenDanhMuc(String sTenDanhMuc) {
        this.sTenDanhMuc = sTenDanhMuc;
    }

    public String getsDanhMucIMG() {
        return sDanhMucIMG;
    }

    public void setsDanhMucIMG(String sDanhMucIMG) {
        this.sDanhMucIMG = sDanhMucIMG;
    }
}
