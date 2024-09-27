package com.example.assetexdemo1;

public class UserModel {
    private int userId;
    private String email = "";
    private String fullName = "";
    private int roleId = 4;
    private String profilePicPath = "";


    public UserModel(int userId, String email, String fullName, int roleId, String profilePicPath) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.roleId = roleId;
        this.profilePicPath = profilePicPath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }
}
