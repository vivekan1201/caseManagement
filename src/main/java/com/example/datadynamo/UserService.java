package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Validate user using email and password
    public boolean validateUser(String email, String password) {
        User user = userRepository.findByEmail(email); // Find by email instead of username
        return user != null && user.getPassword().equals(password); // Consider hashing passwords in a real app
    }
}
