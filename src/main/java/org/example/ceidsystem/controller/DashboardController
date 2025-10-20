package org.example.ceid_v2.controller;

import org.example.ceid_v2.model.User;
import org.example.ceid_v2.service.UserService;
import org.example.ceid_v2.service.ApplicationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.ceid_v2.repository.ContactPersonRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private ContactPersonRepository contactPersonRepository;
    @Autowired
    private ApplicationTypeService applicationTypeService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userService.findByUsername(username).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("fullName", user.getFullName());
        } else {
            model.addAttribute("username", username);
            model.addAttribute("fullName", username);
        }
        
        model.addAttribute("contacts", contactPersonRepository.findAllByOrderByDepartmentAscNameAsc());
        model.addAttribute("applicationTypes", applicationTypeService.getAllApplicationTypes());
        return "dashboard/dashboard";
    }
}

