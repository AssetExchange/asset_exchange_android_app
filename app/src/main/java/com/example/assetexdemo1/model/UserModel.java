package com.example.assetexdemo1.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserModel implements Parcelable {
    private int userId;
    private String email = "";
    private String fullName = "";
    private int roleId = 4;
    private String profilePicPath;


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(email);
        dest.writeString(fullName);
        dest.writeInt(roleId);
        dest.writeString(profilePicPath);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private UserModel(Parcel in) {
        this.userId = in.readInt();
        this.email = in.readString();
        this.fullName = in.readString();
        this.roleId = in.readInt();
        this.profilePicPath = in.readString();
    }
}
