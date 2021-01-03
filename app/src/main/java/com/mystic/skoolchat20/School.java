package com.mystic.skoolchat20;

public class School {

    private String schoolName;
    private String adminEmail;
    private String schoolId;
    private String phnoneNumber;

    public School(String schoolId) {
        this.schoolId = schoolId;

    }

    public School(){

    }

    public String getSchoolName() {
        return schoolName;
    }


    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getPhnoneNumber() {
        return phnoneNumber;
    }

    public void setPhnoneNumber(String phnoneNumber) {
        this.phnoneNumber = phnoneNumber;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
