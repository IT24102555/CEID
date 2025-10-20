package org.example.ceid_v2.strategy;

import org.example.ceid_v2.dto.NicApplicationWizardDto;
import org.example.ceid_v2.model.NicApplication;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation for Driving License applications
 */
@Component
public class DrivingLicenseStrategy implements ApplicationTypeStrategy {
    
    @Override
    public String getApplicationType() {
        return "DRIVING_LICENSE";
    }
    
    @Override
    public String getDisplayName() {
        return "Driving License";
    }
    
    @Override
    public String getIconClass() {
        return "fas fa-id-card";
    }
    
    @Override
    public boolean validateApplication(NicApplicationWizardDto dto) {
        // Driving License-specific validation
        if (dto.getDateOfBirth() == null) {
            return false;
        }
        
        // Check if user is at least 18 years old for driving license
        int age = java.time.LocalDate.now().getYear() - dto.getDateOfBirth().getYear();
        if (age < 18) {
            return false;
        }
        
        // Additional driving license validations can be added here
        return true;
    }
    
    @Override
    public void processApplication(NicApplication application, NicApplicationWizardDto dto) {
        // Driving License-specific processing
        application.setApplicationType("DRIVING_LICENSE");
        application.setApplicationCategory(dto.getApplicationCategory());
        
        // Set driving license-specific fields
        // For example, you might want to set a different status or add specific fields
        
        // Additional driving license-specific processing can be added here
    }
    
    @Override
    public String getRedirectUrl() {
        return "/nic/my";
    }
    
    @Override
    public String getValidationErrorMessage() {
        return "You must be at least 18 years old to apply for a driving license";
    }
}
