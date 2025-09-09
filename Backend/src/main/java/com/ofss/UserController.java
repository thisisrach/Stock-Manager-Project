package com.ofss;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private CustomerRepository customerRepository; 

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid username or password!"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> regData) {
        String username = (String) regData.get("username");
        String password = (String) regData.get("password");
        String role = (String) regData.get("role");

        if (userRepository.findByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Username already exists."));
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);

        if ("CUSTOMER".equalsIgnoreCase(role)) {
            Customer cust = new Customer();
            cust.setUsername(username);         
            cust.setCustName(username);        
            cust.setPhoneNo((String)regData.get("phone"));
            cust.setEmail((String)regData.get("email"));
            cust.setAddress((String)regData.get("address"));
            customerRepository.save(cust);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Registration successful! Please login."));
    }
}
