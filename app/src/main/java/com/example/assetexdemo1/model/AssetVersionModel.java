package com.example.assetexdemo1.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class AssetVersionModel implements Parcelable {
    private String revisionId;
    private String revisionIdFilePath;
    private int assetRevisionId;
    private String previousRevisionId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateProcessed;
    private int revisionStatus;
    private String comment;

    public AssetVersionModel(String revisionId, String revisionIdFilePath, int assetRevisionId, String previousRevisionId, LocalDateTime dateCreated, LocalDateTime dateProcessed, int revisionStatus, String comment) {
        this.revisionId = revisionId;
        this.revisionIdFilePath = revisionIdFilePath;
        this.assetRevisionId = assetRevisionId;
        this.previousRevisionId = previousRevisionId;
        this.dateCreated = dateCreated;
        this.dateProcessed = dateProcessed;
        this.revisionStatus = revisionStatus;
        this.comment = comment;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getRevisionIdFilePath() {
        return revisionIdFilePath;
    }

    public void setRevisionIdFilePath(String revisionIdFilePath) {
        this.revisionIdFilePath = revisionIdFilePath;
    }

    public int getAssetRevisionId() {
        return assetRevisionId;
    }

    public void setAssetRevisionId(int assetRevisionId) {
        this.assetRevisionId = assetRevisionId;
    }

    public String getPreviousRevisionId() {
        return previousRevisionId;
    }

    public void setPreviousRevisionId(String previousRevisionId) {
        this.previousRevisionId = previousRevisionId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(LocalDateTime dateProcessed) {
        this.dateProcessed = dateProcessed;
    }

    public int getRevisionStatus() {
        return revisionStatus;
    }

    public void setRevisionStatus(int revisionStatus) {
        this.revisionStatus = revisionStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(revisionId);
        dest.writeString(revisionIdFilePath);
        dest.writeInt(assetRevisionId);
        dest.writeString(previousRevisionId);
        dest.writeString(dateCreated == null ? null : dateCreated.toString());
        dest.writeString(dateProcessed == null ? null : dateProcessed.toString());
        dest.writeInt(revisionStatus);
        dest.writeString(comment);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<AssetVersionModel> CREATOR = new Parcelable.Creator<AssetVersionModel>() {
        public AssetVersionModel createFromParcel(Parcel in) {
            return new AssetVersionModel(in);
        }

        public AssetVersionModel[] newArray(int size) {
            return new AssetVersionModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    public AssetVersionModel(Parcel in) {
        this.revisionId = in.readString();
        this.revisionIdFilePath = in.readString();
        this.assetRevisionId = in.readInt();
        this.previousRevisionId = in.readString();

        String created = in.readString();
        this.dateCreated = (created == null ? null : LocalDateTime.parse(created));

        String processed = in.readString();
        this.dateProcessed = (processed == null ? null : LocalDateTime.parse(processed));

        this.revisionStatus = in.readInt();
        this.comment = in.readString();
    }
}
