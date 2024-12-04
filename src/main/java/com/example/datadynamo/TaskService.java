package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Fetch all tasks for an employee
    public List<Task> getAllTasksForEmployee(String assignedEmployee) {
        return taskRepository.findByAssignedEmployee(assignedEmployee);
    }

    // Fetch pending tasks for an employee
    public List<Task> getPendingTasksForEmployee(String assignedEmployee) {
        return taskRepository.findByAssignedEmployeeAndTaskStatus(assignedEmployee, "Pending");
    }

    // Fetch completed tasks for an employee
    public List<Task> getCompletedTasksForEmployee(String assignedEmployee) {
        return taskRepository.findByAssignedEmployeeAndTaskStatus(assignedEmployee, "Completed");
    }

    // Fetch all tasks with a specific status (e.g., "Pending")
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByTaskStatus(status);
    }

    // Fetch all tasks for a client
    public List<Task> getTasksForClient(String clientName) {
        return taskRepository.findByClientName(clientName);
    }

    // Update the status of a task
    public boolean updateTaskStatus(String taskId, String newStatus) {
        // Fetch the task by ID
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        
        // Check if the task exists
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTaskStatus(newStatus);  // Update the task status
            //task.setLastUpdated(new java.util.Date());  // Update the timestamp
            taskRepository.save(task);  // Save the updated task
            return true;
        } else {
            return false;  // Task not found
        }
    }
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Map<String, Object>> getTopStaff() {
        // Fetch all completed tasks from the database
        List<Task> completedTasks = taskRepository.findByTaskStatus("Completed");

        // Map each employee to their task count
        Map<String, Integer> employeeTaskCount = new HashMap<>();
        for (Task task : completedTasks) {
            String employee = task.getAssignedEmployee();
            employeeTaskCount.put(employee, employeeTaskCount.getOrDefault(employee, 0) + 1);
        }

        // Sort employees based on the number of completed tasks in descending order
        List<Map.Entry<String, Integer>> sortedEmployeeList = new ArrayList<>(employeeTaskCount.entrySet());
        sortedEmployeeList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Prepare a list of top staff
        List<Map<String, Object>> topStaffList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedEmployeeList) {
            Map<String, Object> staffDetails = new HashMap<>();
            staffDetails.put("employeeName", entry.getKey());
            staffDetails.put("completedTasks", entry.getValue());
            topStaffList.add(staffDetails);
        }

        return topStaffList;
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }
    public List<Task> getTasksByStatusAndStaff(String staff, String status) {
        return taskRepository.findTasksByEmployeeAndStatus(staff, status);
    }


}
