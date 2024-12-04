package com.example.datadynamo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Document(collection = "tasks")
public class Task {

    @Id
    private String taskId;          // Auto-generated task ID by MongoDB
    private String clientName;      // Name of the client associated with the task
    private String taskName;        // Name of the task (e.g., "Initial Contact")
    private String taskStatus;
    private String service;
    private String assignedEmployee;

    // Constructor
    public Task(String clientName, String taskName, String taskStatus, String service, String assignedEmployee) {
        this.clientName = clientName;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.service = service;
        this.assignedEmployee = assignedEmployee;
    }

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", service='" + service + '\'' +
                ", assignedEmployee='" + assignedEmployee + '\'' +
                '}';
    }
}
