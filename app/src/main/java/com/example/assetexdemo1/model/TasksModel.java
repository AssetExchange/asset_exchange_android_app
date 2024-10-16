package com.example.assetexdemo1.model;

import java.time.LocalDateTime;

public class TasksModel {
    private int taskId;
    private String taskTitle = "";
    private String taskDescription = "";
    private int taskOwnerId;

    private boolean priority = false;
    private LocalDateTime dateCreated = LocalDateTime.now();
    private LocalDateTime dueDate = null;
    private LocalDateTime dateModified = LocalDateTime.now();
    private LocalDateTime dateCompleted = null;

    public TasksModel(int taskId, String taskTitle, String taskDescription, int taskOwnerId, boolean priority, LocalDateTime dateCreated, LocalDateTime dueDate) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskOwnerId = taskOwnerId;
        this.priority = priority;
        this.dateCreated = dateCreated;
        this.dueDate = dueDate;
    }

    public TasksModel(int taskId, String taskTitle, String taskDescription, int taskOwnerId, boolean priority, LocalDateTime dateCreated, LocalDateTime dueDate, LocalDateTime dateModified, LocalDateTime dateCompleted) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskOwnerId = taskOwnerId;
        this.priority = priority;
        this.dateCreated = dateCreated;
        this.dueDate = dueDate;
        this.dateModified = dateModified;
        this.dateCompleted = dateCompleted;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskOwnerId() {
        return taskOwnerId;
    }

    public void setTaskOwnerId(int taskOwnerId) {
        this.taskOwnerId = taskOwnerId;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public LocalDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public boolean isCompleted() {
        return this.dateCompleted != null;
    }

    public void setCompleted() {
        this.dateCompleted = LocalDateTime.now();
    }
}
