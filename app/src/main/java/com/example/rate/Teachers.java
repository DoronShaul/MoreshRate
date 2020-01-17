package com.example.rate;

/**
 * this class represents a teacher.
 */
public class Teachers {
    private String teacherName;
    private String userID;

    /**
     * default constructor.
     */
    Teachers(){
        teacherName="";
        userID="";
    }

    /**
     * a constructor.
     */
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
