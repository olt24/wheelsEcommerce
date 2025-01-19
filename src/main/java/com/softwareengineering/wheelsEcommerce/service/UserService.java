// src/main/java/com/softwareengineering/wheelsEcommerce/service/UserService.java
package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Role;
import com.softwareengineering.wheelsEcommerce.model.User;
import com.softwareengineering.wheelsEcommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Load user by username for Spring Security.
     *
     * @param username the username to search for.
     * @return UserDetails required by Spring Security.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Map roles and permissions
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())
        );
    }


    /**
     * Registers a new user.
     *
     * @param user the user to register.
     * @throws IllegalArgumentException if the username or email already exists.
     */
    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER); // Assign default role to the user
        userRepository.save(user);
    }

    /**
     * Maps roles to Spring Security authorities.
     *
     * @param role the role to map.
     * @return a collection of GrantedAuthority.
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Retrieves all users (Admin functionality).
     *
     * @return a list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }




        public User findByUsername(String username) {
            Optional<User> user = userRepository.findByUsername(username);
            return user.orElse(null);
        }

        public void updateUser(User user) {
            userRepository.save(user);
        }



}
