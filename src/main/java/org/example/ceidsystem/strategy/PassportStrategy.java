package org.example.ceid_v2.strategy;

import org.example.ceid_v2.dto.NicApplicationWizardDto;
import org.example.ceid_v2.model.NicApplication;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation for Passport applications
 */
@Component
public class PassportStrategy implements ApplicationTypeStrategy {
    
    @Override
    public String getApplicationType() {
        return "PASSPORT";
    }
    
    @Override
    public String getDisplayName() {
        return "Passport";
    }
    
    @Override
    public String getIconClass() {
        return "fas fa-passport";
    }
    
    @Override
    public boolean validateApplication(NicApplicationWizardDto dto) {
        // Passport-specific validation
        if (dto.getNationality() == null || dto.getNationality().trim().isEmpty()) {
            return false;
        }
        
        // Check if nationality is Sri Lankan (assuming this is a Sri Lankan system)
        if (!"Sri Lankan".equalsIgnoreCase(dto.getNationality().trim())) {
            return false;
        }
        
        // Additional passport validations can be added here
        return true;
    }
    
    @Override
    public void processApplication(NicApplication application, NicApplicationWizardDto dto) {
        // Passport-specific processing
        application.setApplicationType("PASSPORT");
        application.setApplicationCategory(dto.getApplicationCategory());
        
        // Set passport-specific fields
        // For example, you might want to set a different status or add specific fields
        
        // Additional passport-specific processing can be added here
    }
    
    @Override
    public String getRedirectUrl() {
        return "/nic/my";
    }
    
    @Override
    public String getValidationErrorMessage() {
        return "Only Sri Lankan nationals can apply for a passport";
    }
}
