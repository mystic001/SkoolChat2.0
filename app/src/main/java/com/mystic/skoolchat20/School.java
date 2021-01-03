package com.mystic.skoolchat20;

public class School {

    private String schoolName;
    private String adminEmail;
    private String schoolId;
    private String phnoneNumber;
    private String teacherpassword;
    private String studentPassword;

    public School(String schoolId) {
        this.schoolId = schoolId;

    }

    public School(){

    }


    public String getTeacherpassword() {
        return teacherpassword;
    }

    public void setTeacherpassword(String teacherpassword) {
        this.teacherpassword = teacherpassword;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
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
