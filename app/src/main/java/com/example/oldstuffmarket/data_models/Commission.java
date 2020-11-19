package com.example.oldstuffmarket.data_models;

public class Commission {
    private String id;
    private int userCommission;
    private int shopCommission;

    public Commission() {
    }

    public Commission(String id, int userCommission, int shopCommission) {
        this.id = id;
        this.userCommission = userCommission;
        this.shopCommission = shopCommission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserCommission() {
        return userCommission;
    }

    public void setUserCommission(int userCommission) {
        this.userCommission = userCommission;
    }

    public int getShopCommission() {
        return shopCommission;
    }

    public void setShopCommission(int shopCommission) {
        this.shopCommission = shopCommission;
    }
}
