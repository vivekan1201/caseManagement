package com.example.datadynamo;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private SessionService sessionService;
    @GetMapping("/api/getUsername")
    public String getUsername() {
        // Retrieve the username from the session
        String username = sessionService.getLoggedInUsername();

        if (username != null) {
            return username; // Return the username if it's present in the session
        } else {
            return "Guest"; // Return a fallback value if the user is not logged in
        }
    }
    @GetMapping("/api/getLoggedinEmail")
    public String getEmail() {
        // Retrieve the username from the session
        String email = sessionService.getLoggedInEmail();

        if (email != null) {
            return email; // Return the username if it's present in the session
        } else {
            return "sarah.insight@outlook.com"; // Return a fallback value if the user is not logged in
        }
    }
}
