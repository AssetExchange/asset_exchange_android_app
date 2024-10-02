package com.example.assetexdemo1;

import android.os.Parcel;
import android.os.Parcelable;
import java.time.LocalDateTime;

public class AssetModel implements Parcelable {
    private int assetId;
    private int projectId;
    private String assetTitle = "";
    private String assetDescription = "";
    private LocalDateTime dateCreated = LocalDateTime.now();
    private LocalDateTime dateModified = LocalDateTime.now();
    private String latestRevision;
    private String latestRevisionFilePath;

    public AssetModel(int assetId, int projectId, String assetTitle, String assetDescription, LocalDateTime dateCreated, LocalDateTime dateModified, String latestRevision, String latestRevisionFilePath) {
        this.assetId = assetId;
        this.projectId = projectId;
        this.assetTitle = assetTitle;
        this.assetDescription = assetDescription;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.latestRevision = latestRevision;
        this.latestRevisionFilePath = latestRevisionFilePath;
    }

    protected AssetModel(Parcel in) {
        assetId = in.readInt();
        projectId = in.readInt();
        assetTitle = in.readString();
        assetDescription = in.readString();
        dateCreated = LocalDateTime.parse(in.readString());
        dateModified = LocalDateTime.parse(in.readString());
        latestRevision = in.readString();
        latestRevisionFilePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(assetId);
        dest.writeInt(projectId);
        dest.writeString(assetTitle);
        dest.writeString(assetDescription);
        dest.writeString(dateCreated.toString());
        dest.writeString(dateModified.toString());
        dest.writeString(latestRevision);
        dest.writeString(latestRevisionFilePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AssetModel> CREATOR = new Creator<AssetModel>() {
        @Override
        public AssetModel createFromParcel(Parcel in) {
            return new AssetModel(in);
        }

        @Override
        public AssetModel[] newArray(int size) {
            return new AssetModel[size];
        }
    };

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getAssetTitle() {
        return assetTitle;
    }

    public void setAssetTitle(String assetTitle) {
        this.assetTitle = assetTitle;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public String getLatestRevision() {
        return latestRevision;
    }

    public void setLatestRevision(String latestRevision) {
        this.latestRevision = latestRevision;
    }

    public String getLatestRevisionFilePath() {
        return latestRevisionFilePath;
    }

    public void setLatestRevisionFilePath(String latestRevisionFilePath) {
        this.latestRevisionFilePath = latestRevisionFilePath;
    }
}
