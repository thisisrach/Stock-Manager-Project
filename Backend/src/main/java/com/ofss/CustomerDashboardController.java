package com.ofss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerDashboardController {

    @Autowired
    private CustomerDashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getCustomerDashboard(@RequestParam String username) {
        try {
            Map<String, Object> result = dashboardService.getDashboardForCustomer(username);
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}