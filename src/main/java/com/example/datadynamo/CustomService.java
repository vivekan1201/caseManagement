package com.example.datadynamo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "services")  // MongoDB collection name
public class CustomService {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;


    private String serviceName;
    private List<Stask> taskDetails;  // Changed from Task to Stask

    // Getters and Setters
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<Stask> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<Stask> taskDetails) {
        this.taskDetails = taskDetails;
    }
}
