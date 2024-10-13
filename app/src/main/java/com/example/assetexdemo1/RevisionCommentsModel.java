package com.example.assetexdemo1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class RevisionCommentsModel implements Parcelable {
    private int revisionCommentId;
    private AssetModel asset;
    private UserModel revisionCommentUser;
    private String revisionCommentText;
    private LocalDateTime dateCreated;
    private LocalDateTime dateProcessed;
    private int commentStatus = 1;



    public RevisionCommentsModel(Parcel in) {
        revisionCommentId = in.readInt();
        asset = in.readParcelable(asset.getClass().getClassLoader());
        revisionCommentUser = in.readParcelable(revisionCommentUser.getClass().getClassLoader());
        revisionCommentText = in.readString();
        dateCreated = LocalDateTime.parse(in.readString());
        String processed = in.readString();
        dateProcessed = (processed == null ? null : LocalDateTime.parse(processed));
        commentStatus = in.readInt();
    }

    public RevisionCommentsModel(int revisionCommentId, AssetModel asset, UserModel revisionCommentUser, String revisionCommentText, LocalDateTime dateCreated, LocalDateTime dateProcessed, int commentStatus) {
        this.revisionCommentId = revisionCommentId;
        this.asset = asset;
        this.revisionCommentUser = revisionCommentUser;
        this.revisionCommentText = revisionCommentText;
        this.dateCreated = dateCreated;
        this.dateProcessed = dateProcessed;
        this.commentStatus = commentStatus;
    }

    public int getRevisionCommentId() {
        return revisionCommentId;
    }

    public void setRevisionCommentId(int revisionCommentId) {
        this.revisionCommentId = revisionCommentId;
    }

    public AssetModel getAsset() {
        return asset;
    }

    public void setAsset(AssetModel asset) {
        this.asset = asset;
    }

    public UserModel getRevisionCommentUser() {
        return revisionCommentUser;
    }

    public void setRevisionCommentUser(UserModel revisionCommentUser) {
        this.revisionCommentUser = revisionCommentUser;
    }

    public String getRevisionCommentText() {
        return revisionCommentText;
    }

    public void setRevisionCommentText(String revisionCommentText) {
        this.revisionCommentText = revisionCommentText;
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

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    public static final Creator<RevisionCommentsModel> CREATOR = new Creator<RevisionCommentsModel>() {
        @Override
        public RevisionCommentsModel createFromParcel(Parcel in) {
            return new RevisionCommentsModel(in);
        }

        @Override
        public RevisionCommentsModel[] newArray(int size) {
            return new RevisionCommentsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(revisionCommentId);
        dest.writeParcelable(asset, 0);
        dest.writeParcelable(revisionCommentUser, 0);
        dest.writeString(revisionCommentText);
        dest.writeString(dateCreated.toString());
        dest.writeString(dateProcessed == null ? null : dateProcessed.toString());
        dest.writeInt(commentStatus);
    }
}
