package com.example.assetexdemo1;

import java.time.LocalDateTime;

public class ProjectModel {
    private int projectId;
    private LocalDateTime dateCreated = LocalDateTime.now();
    private int projectOwnerId;
    private String projectTitle = "";
    private String projectDescription = "";
    private boolean priority = false;
    private String projectImagePath = null;


    public ProjectModel(int projectId, LocalDateTime dateCreated, int projectOwnerId, String projectTitle, boolean priority, String projectImagePath, String projectDescription) {
        this.projectId = projectId;
        this.dateCreated = dateCreated;
        this.projectOwnerId = projectOwnerId;
        this.projectTitle = projectTitle;
        this.priority = priority;
        this.projectImagePath = projectImagePath;
        this.projectDescription = projectDescription;
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
}
