package org.example.ceid_v2.service;

import org.example.ceid_v2.model.User;
import org.example.ceid_v2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User registerUser(User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save user
        return userRepository.save(user);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public User updateProfile(String currentUsername, String newFirstName, String newLastName, String newEmail) {
        User user = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUsername));
        
        if (newEmail != null && !newEmail.equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(newEmail, user.getId())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(newEmail);
        }
        
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        
        return userRepository.save(user);
    }

    public void deleteUserAndLogout(String username) {
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        java.util.List<org.springframework.security.core.GrantedAuthority> authorities = new java.util.ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.getEmail() != null && user.getEmail().toLowerCase().endsWith("admin.com")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isActive(),
            true,
            true,
            true,
            authorities
        );
    }
}
