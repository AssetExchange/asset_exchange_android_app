package com.example.assetexdemo1.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class ProjectModel implements Parcelable {
    private int projectId;
    private LocalDateTime dateCreated = LocalDateTime.now();
    private int projectOwnerId;
    private String projectTitle = "";
    private String projectDescription = "";
    private boolean priority = false;
    private LocalDateTime dueDate;
    private String projectImagePath = null;

//    public ProjectModel(int projectId, LocalDateTime dateCreated, int projectOwnerId, String projectTitle, boolean priority, String projectImagePath, String projectDescription) {
//        this.projectId = projectId;
//        this.dateCreated = dateCreated;
//        this.projectOwnerId = projectOwnerId;
//        this.projectTitle = projectTitle;
//        this.projectDescription = projectDescription;
//        this.priority = priority;
//        this.projectImagePath = projectImagePath;
//    }

//    public ProjectModel(int projectId, LocalDateTime dateCreated, int projectOwnerId, String projectTitle, String projectDescription, boolean priority, String projectImagePath) {
//        this.projectId = projectId;
//        this.dateCreated = dateCreated;
//        this.projectOwnerId = projectOwnerId;
//        this.projectTitle = projectTitle;
//        this.projectDescription = projectDescription;
//        this.priority = priority;
//        this.projectImagePath = projectImagePath;
//    }

    public ProjectModel(int projectId, LocalDateTime dateCreated, int projectOwnerId, String projectTitle, String projectDescription, LocalDateTime dueDate, boolean priority, String projectImagePath) {
        this.projectId = projectId;
        this.dateCreated = dateCreated;
        this.projectOwnerId = projectOwnerId;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.dueDate = dueDate;
        this.priority = priority;
        this.projectImagePath = projectImagePath;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getProjectOwnerId() {
        return projectOwnerId;
    }

    public void setProjectOwnerId(int projectOwnerId) {
        this.projectOwnerId = projectOwnerId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public String getProjectImagePath() {
        return projectImagePath;
    }

    public void setProjectImagePath(String projectImagePath) {
        this.projectImagePath = projectImagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(projectId);
        dest.writeString(dateCreated.toString());
        dest.writeInt(projectOwnerId);
        dest.writeString(projectTitle);
        dest.writeString(projectDescription);
        dest.writeString(dueDate == null ? null : dueDate.toString());
        dest.writeInt(priority ? 1 : 0);
        dest.writeString(projectImagePath);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ProjectModel> CREATOR = new Parcelable.Creator<ProjectModel>() {
        public ProjectModel createFromParcel(Parcel in) {
            return new ProjectModel(in);
        }

        public ProjectModel[] newArray(int size) {
            return new ProjectModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private ProjectModel(Parcel in) {
        this.projectId = in.readInt();
        this.dateCreated = LocalDateTime.parse(in.readString());
        this.projectOwnerId = in.readInt();
        this.projectTitle = in.readString();
        this.projectDescription = in.readString();
        String due = in.readString();
        this.dueDate = (due == null ? null : LocalDateTime.parse(due));
        this.priority = (in.readInt() == 1);
        this.projectImagePath = in.readString();
    }
}
