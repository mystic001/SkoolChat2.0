package com.mystic.skoolchat20;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String schoolName;
    private String role;
    private String uid;
    private String password;
    private boolean userVerified;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;

    public User(String name, String email, String schoolName,String role,String uid) {
        this.name = name;
        this.email = email;
        this.schoolName = schoolName;
        this.uid = uid;
        this.role = role;
    }


    public User(String uid){
        this.uid = uid;
    }


    public User(String name, String email,String uid){
        this.name = name;
        this.email = email;;
        this.uid = uid;
        this.role = "admin";
    }


    public User(){

    }
    public User(String name, String email){
        this.name = name;
        this.email = email;;
        this.role = "owner";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isUserVerified() {
        return userVerified;
    }

    public void setUserVerified(boolean userVerified) {
        this.userVerified = userVerified;
    }
}
