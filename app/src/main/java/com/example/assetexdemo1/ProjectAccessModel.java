package com.example.assetexdemo1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProjectAccessModel implements Parcelable {
    private ProjectModel projectModel;
    private UserModel projectOwner;
    private UserModel shareUser;
    private int privileges = 2;


    public ProjectAccessModel(ProjectModel projectModel, UserModel projectOwner, UserModel shareUser, int privileges) {
        this.projectModel = projectModel;
        this.projectOwner = projectOwner;
        this.shareUser = shareUser;
        this.privileges = privileges;
    }


    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public UserModel getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwnerId(UserModel projectOwner) {
        this.projectOwner = projectOwner;
    }

    public UserModel getShareUser() {
        return shareUser;
    }

    public void setShareUserId(UserModel shareUser) {
        this.shareUser = shareUser;
    }

    public int getPrivileges() {
        return privileges;
    }

    public void setPrivileges(int privileges) {
        this.privileges = privileges;
    }

    public int getProjectId() {
        return projectModel.getProjectId();
    }

    public void setProjectId(int projectId) {
        projectModel.setProjectId(projectId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(projectModel, 0);
        dest.writeParcelable(projectOwner, 0);
        dest.writeParcelable(shareUser, 0);
        dest.writeInt(privileges);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ProjectAccessModel> CREATOR = new Parcelable.Creator<ProjectAccessModel>() {
        public ProjectAccessModel createFromParcel(Parcel in) {
            return new ProjectAccessModel(in);
        }

        public ProjectAccessModel[] newArray(int size) {
            return new ProjectAccessModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private ProjectAccessModel(Parcel in) {
        this.projectModel = in.readParcelable(getClass().getClassLoader());
        this.projectOwner = in.readParcelable(projectOwner.getClass().getClassLoader());
        this.shareUser = in.readParcelable(shareUser.getClass().getClassLoader());
        this.privileges = in.readInt();
    }
}
