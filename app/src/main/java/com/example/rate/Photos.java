package com.example.rate;

/**
 * this class represents a profile picture of a user.
 */
public class Photos {
    private String userID;
    private String imageData;

    /**
     * default constructor.
     */
    Photos() {
        this.imageData = "";
        this.userID = "";
    }

    /**
     * a constructor.
     */
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
