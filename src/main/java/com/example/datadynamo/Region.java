package com.example.datadynamo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "regions") // Maps this class to the "regions" collection in MongoDB
public class Region {

    @Id
    private String id;  // Primary key for the Region document
    
    private String name;  // Name of the region

    // Default constructor
    public Region() {
    }

    // Parameterized constructor
    public Region(String name) {
        this.name = name;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // toString method for easy debugging
    @Override
    public String toString() {
        return "Region{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
