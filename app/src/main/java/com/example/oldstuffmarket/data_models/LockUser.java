package com.example.oldstuffmarket.data_models;

public class LockUser {
    String lockID, userID;

    public LockUser() {
    }

    public LockUser(String lockID, String userID) {
        this.lockID = lockID;
        this.userID = userID;
    }

    public String getLockID() {
        return lockID;
    }

    public String getUserID() {
        return userID;
    }

    public void setLockID(String lockID) {
        this.lockID = lockID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
