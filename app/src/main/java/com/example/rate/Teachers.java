package com.example.rate;

public class Teachers {
    private String teacherName;
    private String userID;

    Teachers(){
        teacherName="";
        userID="";
    }

    Teachers(String teacherName, String userID){
        this.userID = userID;
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
