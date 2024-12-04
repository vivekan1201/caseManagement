package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Endpoint to fetch all tasks
    @GetMapping("/api/tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
    @GetMapping("/api/getstatus")
    public List<Task> getTasksByStatus(String status) {
        return taskService.getTasksByStatus(status);
    }

    @GetMapping("/api/getopstaff")
    public List<Map<String, Object>> getTopStaff(String status) {
        return taskService.getTopStaff();
    }

    @GetMapping("/api/staffTasks")
    public List<Task> getStaffTasks(String staff) {
        return taskService.getAllTasksForEmployee(staff);
    }


    @GetMapping("/api/staffstatus")
    public List<Task> getTasksByStatusAndStaff(
            @RequestParam String staff,
            @RequestParam String status
    ) {
        return taskService.getTasksByStatusAndStaff(staff, status);
    }

    @GetMapping("/api/updateStatus")
    public boolean updateStatus(String taskId, String newStatus) {
        return taskService.updateTaskStatus(taskId,newStatus);
    }
}
