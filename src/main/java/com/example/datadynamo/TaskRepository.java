package com.example.datadynamo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface TaskRepository extends MongoRepository<Task, String> {

    // Fetch all tasks for a specific employee
    List<Task> findByAssignedEmployee(String assignedEmployee);

    // Fetch pending tasks for a specific employee
    List<Task> findByAssignedEmployeeAndTaskStatus(String assignedEmployee, String taskStatus);

    // Fetch tasks by status (e.g., "Pending", "Completed")
    List<Task> findByTaskStatus(String taskStatus);

    // Fetch all tasks for a client
    List<Task> findByClientName(String clientName);


    @Query("{'assignedEmployee': ?0, 'taskStatus': ?1}")
    List<Task> findTasksByEmployeeAndStatus(String assignedEmployee, String taskStatus);
}
