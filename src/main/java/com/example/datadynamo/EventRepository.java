package com.example.datadynamo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    // Custom method to find events by assignedEmployee
    List<Event> findByAssignedEmployee(String assignedEmployee);
}
