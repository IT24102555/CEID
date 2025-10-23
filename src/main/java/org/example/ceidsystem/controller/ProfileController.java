package org.example.ceid_v2.controller;

import org.example.ceid_v2.model.User;
import org.example.ceid_v2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    private void addUser(Authentication authentication, Model model){
        if(authentication != null){
            String username = authentication.getName();
            model.addAttribute("username", username);
            User u = userService.findByUsername(username).orElse(null);
            model.addAttribute("fullName", u != null ? u.getFullName() : username);
            if (u != null) {
                model.addAttribute("user", u);
            }
        }
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model){
        addUser(authentication, model);
        return "profile/profile";
    }

    @PostMapping("/profile/update")
    public String update(Authentication authentication,
                         @RequestParam(required = false) String firstName,
                         @RequestParam(required = false) String lastName,
                         RedirectAttributes ra){
        try {
            String username = authentication.getName();
            User updated = userService.updateProfile(username, firstName, lastName, null);
            ra.addFlashAttribute("successMessage", "Profile updated");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/profile";
    }
}




