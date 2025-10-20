package org.example.ceid_v2.controller;

import org.example.ceid_v2.dto.NicApplicationWizardDto;
import org.example.ceid_v2.model.ApplicationDocument;
import org.example.ceid_v2.model.NicApplication;
import org.example.ceid_v2.model.User;
import org.example.ceid_v2.service.NicApplicationService;
import org.example.ceid_v2.service.UserService;
import org.example.ceid_v2.strategy.ApplicationTypeContext;
import org.example.ceid_v2.strategy.ApplicationTypeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@SessionAttributes("nicWizard")
@RequestMapping("/nic/wizard")
public class NicWizardController {

    @Autowired
    private NicApplicationService nicApplicationService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ApplicationTypeContext applicationTypeContext;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @ModelAttribute("nicWizard")
    public NicApplicationWizardDto wizardDto() {
        return new NicApplicationWizardDto();
    }

    @GetMapping("/step1")
    public String step1(@ModelAttribute("nicWizard") NicApplicationWizardDto dto, 
                       @RequestParam(value = "type", defaultValue = "nic") String applicationType,
                       Authentication auth, Model model) {
        if (auth != null) {
            String username = auth.getName();
            model.addAttribute("username", username);
            User u = userService.findByUsername(username).orElse(null);
            model.addAttribute("fullName", u != null ? u.getFullName() : username);
        }
        
        // Get the strategy for the application type
        try {
            ApplicationTypeStrategy strategy = applicationTypeContext.getStrategy(applicationType);
            model.addAttribute("applicationType", strategy.getApplicationType());
            model.addAttribute("displayName", strategy.getDisplayName());
            model.addAttribute("iconClass", strategy.getIconClass());
            
            // Set the main application type in the DTO
            dto.setMainApplicationType(strategy.getApplicationType());
            System.out.println("DEBUG Step1: Set mainApplicationType = " + dto.getMainApplicationType());
        } catch (IllegalArgumentException e) {
            // Default to NIC if invalid type is provided
            ApplicationTypeStrategy strategy = applicationTypeContext.getStrategy("nic");
            model.addAttribute("applicationType", strategy.getApplicationType());
            model.addAttribute("displayName", strategy.getDisplayName());
            model.addAttribute("iconClass", strategy.getIconClass());
            dto.setMainApplicationType(strategy.getApplicationType());
            System.out.println("DEBUG Step1 (fallback): Set mainApplicationType = " + dto.getMainApplicationType());
        }
        
        model.addAttribute("step", 1);
        return "nic/wizard/step1";
    }

    @PostMapping("/step1")
    public String step1Post(@ModelAttribute("nicWizard") NicApplicationWizardDto dto,
                            @RequestParam String firstName,
                            @RequestParam String lastName,
                            @RequestParam(required = false) String nic,
                            @RequestParam String dateOfBirth,
                            @RequestParam String gender,
                            @RequestParam String nationality,
                            @RequestParam String email,
                            @RequestParam String phone,
                            @RequestParam String addressLine1,
                            @RequestParam String city,
                            @RequestParam String district,
                            @RequestParam String postalCode) {
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setNic(nic);
        dto.setDateOfBirth(java.time.LocalDate.parse(dateOfBirth));
        dto.setGender(gender);
        dto.setNationality(nationality);
        dto.setEmail(email);
        dto.setPhone(phone);
        dto.setAddressLine1(addressLine1);
        dto.setCity(city);
        dto.setDistrict(district);
        dto.setPostalCode(postalCode);
        return "redirect:/nic/wizard/step2";
    }

    @GetMapping("/step2")
    public String step2(@ModelAttribute("nicWizard") NicApplicationWizardDto dto, Authentication auth, Model model) {
        if (auth != null) {
            String username = auth.getName();
            model.addAttribute("username", username);
            User u = userService.findByUsername(username).orElse(null);
            model.addAttribute("fullName", u != null ? u.getFullName() : username);
        }
        model.addAttribute("step", 2);
        return "nic/wizard/step2";
    }

    @PostMapping("/step2")
    public String step2Post(@ModelAttribute("nicWizard") NicApplicationWizardDto dto,
                            @RequestParam String applicationCategory,
                            @RequestParam String reasonForRequest,
                            @RequestParam(required = false) String remarks) {
        
        // Debug logging
        System.out.println("DEBUG Step2: Before setting - mainApplicationType = " + dto.getMainApplicationType());
        System.out.println("DEBUG Step2: applicationCategory from form = " + applicationCategory);
        
        dto.setApplicationCategory(applicationCategory); // This is the category (New/Renewal/Replacement)
        dto.setReasonForRequest(reasonForRequest);
        dto.setRemarks(remarks);
        
        // Debug logging
        System.out.println("DEBUG Step2: After setting - mainApplicationType = " + dto.getMainApplicationType());
        System.out.println("DEBUG Step2: applicationCategory = " + dto.getApplicationCategory());
        
        return "redirect:/nic/wizard/step3";
    }

    @GetMapping("/step3")
    public String step3(@ModelAttribute("nicWizard") NicApplicationWizardDto dto, Authentication auth, Model model) {
        if (auth != null) {
            String username = auth.getName();
            model.addAttribute("username", username);
            User u = userService.findByUsername(username).orElse(null);
            model.addAttribute("fullName", u != null ? u.getFullName() : username);
        }
        model.addAttribute("step", 3);
        return "nic/wizard/step3";
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> uploadDoc(@ModelAttribute("nicWizard") NicApplicationWizardDto dto,
                                            @RequestParam("documentType") String documentType,
                                            @RequestParam("file") MultipartFile file) throws IOException {
        Path tmp = Paths.get(uploadDir, "tmp").toAbsolutePath().normalize();
        Files.createDirectories(tmp);
        String storedName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path target = tmp.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        dto.getUploadedTempFiles().add(target.toString());
        dto.getUploadedOriginalNames().add(file.getOriginalFilename());
        dto.getUploadedTypes().add(file.getContentType());
        dto.getDocumentTypes().add(documentType);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/step4")
    public String step4(@ModelAttribute("nicWizard") NicApplicationWizardDto dto, Authentication auth, Model model) {
        if (auth != null) {
            String username = auth.getName();
            model.addAttribute("username", username);
            User u = userService.findByUsername(username).orElse(null);
            model.addAttribute("fullName", u != null ? u.getFullName() : username);
        }
        model.addAttribute("step", 4);
        return "nic/wizard/step4";
    }

    @PostMapping("/submit")
    public String submit(Authentication auth,
                         @ModelAttribute("nicWizard") NicApplicationWizardDto dto,
                         RedirectAttributes ra) throws IOException {
        
        // Debug logging
        System.out.println("DEBUG: mainApplicationType = " + dto.getMainApplicationType());
        System.out.println("DEBUG: applicationCategory = " + dto.getApplicationCategory());
        System.out.println("DEBUG: applicationType (legacy) = " + dto.getApplicationType());
        
        // Get the strategy for the main application type
        String strategyType = dto.getMainApplicationType();
        if (strategyType == null || strategyType.trim().isEmpty()) {
            // Fallback to legacy field if main type is not set
            strategyType = dto.getApplicationType();
        }
        
        ApplicationTypeStrategy strategy = applicationTypeContext.getStrategy(strategyType);
        
        // Validate using the strategy
        if (!strategy.validateApplication(dto)) {
            ra.addFlashAttribute("errorMessage", strategy.getValidationErrorMessage());
            return "redirect:/nic/wizard/step4";
        }
        
        // create base application using service (minimal required fields used earlier), then enrich
        NicApplication app = nicApplicationService.create(
                auth.getName(),
                dto.getFirstName() + " " + dto.getLastName(),
                dto.getAddressLine1(),
                dto.getPhone(),
                null
        );

        // enrich with all wizard details
        app.setFirstName(dto.getFirstName());
        app.setLastName(dto.getLastName());
        app.setNic(dto.getNic());
        app.setDateOfBirth(dto.getDateOfBirth());
        app.setGender(dto.getGender());
        app.setNationality(dto.getNationality());
        app.setEmail(dto.getEmail());
        app.setPhone(dto.getPhone());
        app.setAddressLine1(dto.getAddressLine1());
        app.setCity(dto.getCity());
        app.setDistrict(dto.getDistrict());
        app.setPostalCode(dto.getPostalCode());
        app.setApplicationType(dto.getMainApplicationType());
        app.setApplicationCategory(dto.getApplicationCategory());
        app.setReasonForRequest(dto.getReasonForRequest());
        app.setRemarks(dto.getRemarks());
        
        // Process application using the strategy
        strategy.processApplication(app, dto);
        
        nicApplicationService.save(app);

        // move uploaded files from tmp to final folder and persist as ApplicationDocument
        java.nio.file.Path finalDir = Paths.get(uploadDir, "applications", String.valueOf(app.getId()))
                .toAbsolutePath().normalize();
        Files.createDirectories(finalDir);

        for (int i = 0; i < dto.getUploadedTempFiles().size(); i++) {
            String tmpPath = dto.getUploadedTempFiles().get(i);
            String original = dto.getUploadedOriginalNames().get(i);
            String type = dto.getUploadedTypes().get(i);
            String docType = dto.getDocumentTypes().get(i);
            java.nio.file.Path tmpFile = Paths.get(tmpPath);
            String storedName = System.currentTimeMillis() + "_" + original;
            java.nio.file.Path dest = finalDir.resolve(storedName);
            Files.move(tmpFile, dest, StandardCopyOption.REPLACE_EXISTING);

            ApplicationDocument doc = new ApplicationDocument();
            doc.setApplication(app);
            doc.setDocumentType(docType);
            doc.setFileName(original);
            doc.setFileType(type);
            doc.setFilePath(dest.toString());
            // Optionally save via repo if mapped cascade isn't set
        }

        ra.addFlashAttribute("successMessage", "Application submitted successfully");
        return "redirect:" + strategy.getRedirectUrl();
    }
}


