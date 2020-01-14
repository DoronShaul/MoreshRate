package com.example.rate;

public class Photos {
    private String userID;
    private String imageData;

    Photos() {
        this.imageData = "";
        this.userID = "";
    }

    Photos(String uID, String iData) {
        this.userID = uID;
        this.imageData = iData;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
