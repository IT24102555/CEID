package org.example.ceid_v2.controller;

import org.example.ceid_v2.model.NicApplication;
import org.example.ceid_v2.model.User;
import org.example.ceid_v2.service.NicApplicationService;
import org.example.ceid_v2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/nic")
public class NicApplicationController {

    @Autowired
    private NicApplicationService nicApplicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private org.example.ceid_v2.repository.ReviewRecordRepository reviewRecordRepository;

    private void addUserModel(Authentication auth, Model model) {
        if (auth != null) {
            String username = auth.getName();
            model.addAttribute("username", username);
            User u = userService.findByUsername(username).orElse(null);
            if (u != null) {
                model.addAttribute("fullName", u.getFullName());
            } else {
                model.addAttribute("fullName", username);
            }
        }
    }

    @GetMapping("/new")
    public String newForm() {
        return "redirect:/nic/wizard/step1";
    }

    @PostMapping("/new")
    public String submitNew(Authentication auth,
                            @RequestParam String fullName,
                            @RequestParam String address,
                            @RequestParam String contactNumber,
                            @RequestParam(required = false) MultipartFile file,
                            RedirectAttributes ra) throws IOException {
        nicApplicationService.create(auth.getName(), fullName, address, contactNumber, file);
        ra.addFlashAttribute("successMessage", "Application submitted successfully");
        return "redirect:/nic/my";
    }

    @GetMapping("/my")
    public String myApplications(Authentication auth, Model model) {
        addUserModel(auth, model);
        model.addAttribute("applications", nicApplicationService.listForUser(auth.getName()));
        return "nic/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(Authentication auth, @PathVariable Long id, Model model) {
        addUserModel(auth, model);
        NicApplication app = nicApplicationService.findOwned(auth.getName(), id);
        model.addAttribute("app", app);
        return "nic/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(Authentication auth, @PathVariable Long id,
                             @RequestParam(required = false) String address,
                             @RequestParam(required = false) String contactNumber,
                             RedirectAttributes ra) {
        // Disallow status changes by end users
        nicApplicationService.updateOwned(auth.getName(), id, address, contactNumber, null);
        ra.addFlashAttribute("successMessage", "Application updated");
        return "redirect:/nic/my";
    }

    @PostMapping("/delete/{id}")
    public String delete(Authentication auth, @PathVariable Long id, RedirectAttributes ra) {
        nicApplicationService.deleteOwned(auth.getName(), id);
        ra.addFlashAttribute("successMessage", "Application deleted");
        return "redirect:/nic/my";
    }

    @GetMapping(value = "/details/{id}", produces = "application/json")
    @ResponseBody
    public Map<String, Object> ownedDetails(Authentication auth, @PathVariable Long id) {
        NicApplication app = nicApplicationService.findOwned(auth.getName(), id);
        Map<String, Object> body = new HashMap<>();
        body.put("id", app.getId());
        body.put("fullName", app.getFullName());
        body.put("address", app.getAddress());
        body.put("contactNumber", app.getContactNumber());
        body.put("status", app.getStatus());
        body.put("uploadedFileName", app.getUploadedFileName());
        body.put("createdAt", app.getCreatedAt() != null ? app.getCreatedAt().toString() : null);
        body.put("updatedAt", app.getUpdatedAt() != null ? app.getUpdatedAt().toString() : null);
        // latest review (if any) for user display
        java.util.List<org.example.ceid_v2.model.ReviewRecord> reviews = reviewRecordRepository.findByApplicationOrderByCreatedAtDesc(app);
        if(!reviews.isEmpty()){
            org.example.ceid_v2.model.ReviewRecord r = reviews.get(0);
            java.util.Map<String,Object> latest = new java.util.HashMap<>();
            latest.put("decision", r.getDecision());
            latest.put("remarks", r.getRemarks());
            latest.put("createdAt", r.getCreatedAt() != null ? r.getCreatedAt().toString() : null);
            body.put("latestReview", latest);
        }
        return body;
    }
}


