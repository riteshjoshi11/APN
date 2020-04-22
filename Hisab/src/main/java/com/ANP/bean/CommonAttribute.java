package com.ANP.bean;

public class CommonAttribute {
    public String statusMessage;

    public String userID="E1"; //TODo values will be set after log in from Token hardcoded till implimentation

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
