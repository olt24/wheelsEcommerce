package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Role;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a user with the USER role by default
    public void registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER); // Default role is USER

        userRepository.save(user);
    }

    // Promote a user to ADMIN role
    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}
