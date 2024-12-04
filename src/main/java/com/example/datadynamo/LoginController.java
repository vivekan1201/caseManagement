package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    // Show the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Return the login HTML page
    }

    // Handle login with email and password
    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        // Validate user with email and password
        if (userService.validateUser(email,password)) {
            // Save username and email in session
            sessionService.saveLoggedInDetails(email);

            // Redirect to the client-intake page (or other page after login)
            return "dashboardtrial";
        } else {
            // Invalid login, show the login page again
            return "login";
        }
    }

    // Handle logout and remove session
    @PostMapping("/api/logout")
    public String logout() {
        // Remove the session document from MongoDB
        sessionService.logout();

        // Redirect to login page after logout
        return "redirect:/login";
    }
}
