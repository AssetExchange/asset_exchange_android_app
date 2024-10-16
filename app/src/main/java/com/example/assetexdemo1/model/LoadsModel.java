package com.example.assetexdemo1.model;

import java.time.LocalDateTime;

public class LoadsModel {
    private String loadName = "";
    private String loadClient = "";
    private String loadStatus = "";
    private LocalDateTime loadDate = LocalDateTime.now();
    private LocalDateTime loadDue = LocalDateTime.now();
    private int loadImage = 0;

    public LoadsModel(String loadName, String loadClient, String loadStatus) {
        this.loadName = loadName;
        this.loadClient = loadClient;
        this.loadStatus = loadStatus;
    }

    public LoadsModel(String loadName, String loadClient, String loadStatus, LocalDateTime loadDate, LocalDateTime loadDue) {
        this.loadName = loadName;
        this.loadClient = loadClient;
        this.loadStatus = loadStatus;
        this.loadDate = loadDate;
        this.loadDue = loadDue;
    }

    public LoadsModel(String loadName, String loadClient, String loadStatus, LocalDateTime loadDate, LocalDateTime loadDue, int loadImage) {
        this.loadName = loadName;
        this.loadClient = loadClient;
        this.loadStatus = loadStatus;
        this.loadDate = loadDate;
        this.loadDue = loadDue;
        this.loadImage = loadImage;
    }

    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName;
    }

    public String getLoadClient() {
        return loadClient;
    }

    public void setLoadClient(String loadClient) {
        this.loadClient = loadClient;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public LocalDateTime getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(LocalDateTime loadDate) {
        this.loadDate = loadDate;
    }

    public LocalDateTime getLoadDue() {
        return loadDue;
    }

    public void setLoadDue(LocalDateTime loadDue) {
        this.loadDue = loadDue;
    }

    public int getLoadImage() {
        return loadImage;
    }

    public void setLoadImage(int loadImage) {
        this.loadImage = loadImage;
    }
}
