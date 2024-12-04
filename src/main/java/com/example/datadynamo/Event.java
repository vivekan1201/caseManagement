package com.example.datadynamo;

import java.time.LocalDateTime;

public class Event {
    private LocalDateTime eventDate; // Use LocalDateTime
    private String eventName;
    private String assignedEmployee;
    private String clientName;

    // Constructor
    public Event(LocalDateTime eventDate, String eventName, String assignedEmployee, String clientName) {
        this.eventDate = eventDate;
        this.eventName = eventName;
        this.assignedEmployee = assignedEmployee;
        this.clientName = clientName;
    }

    // Getters and Setters
    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
