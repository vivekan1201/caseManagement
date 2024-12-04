package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;  // Assuming you have a UserRepository to access the User collection

    // Fetch the current logged-in username
    public String getLoggedInUsername() {
        // Fetch the session document by ID ("1")
        Session session = sessionRepository.findById("1").orElse(null);
        return session != null ? session.getUsername() : "Guest"; // Default to "Guest" if no session exists
    }

    // Fetch the current logged-in email
    public String getLoggedInEmail() {
        // Fetch the session document by ID ("1")
        Session session = sessionRepository.findById("1").orElse(null);
        return session != null ? session.getEmail() : "No email"; // Default to "No email" if no session exists
    }

    // Save the username and email to MongoDB (this will overwrite the existing document with the same ID)
    public void saveLoggedInDetails(String email) {
        // Look up the user by email
        User user = userRepository.findByEmail(email);  // Assuming a method findByEmail exists in UserRepository

        if (user != null) {
            // Fetch the username from the user and save to session
            String username = user.getUsername();

            // Try to find the session document
            Session session = sessionRepository.findById("1").orElse(null);

            if (session != null) {
                // Update existing session document with the new username and email
                session.setUsername(username);
                session.setEmail(email);
            } else {
                // If no session exists, create a new one
                session = new Session();
                session.setId("1"); // Use a fixed ID so it can be overwritten
                session.setUsername(username);
                session.setEmail(email);
            }
            // Save (or overwrite) the session document
            sessionRepository.save(session);
        } else {
            throw new RuntimeException("User with email " + email + " not found.");
        }
    }

    // Logout by deleting the session
    public void logout() {
        sessionRepository.deleteById("1"); // Remove the document to logout
    }
}
