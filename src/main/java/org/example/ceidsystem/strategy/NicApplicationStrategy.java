package org.example.ceid_v2.strategy;

import org.example.ceid_v2.dto.NicApplicationWizardDto;
import org.example.ceid_v2.model.NicApplication;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation for NIC applications
 */
@Component
public class NicApplicationStrategy implements ApplicationTypeStrategy {
    
    @Override
    public String getApplicationType() {
        return "NIC";
    }
    
    @Override
    public String getDisplayName() {
        return "NIC Application";
    }
    
    @Override
    public String getIconClass() {
        return "fas fa-user-alt";
    }
    
    @Override
    public boolean validateApplication(NicApplicationWizardDto dto) {
        // NIC-specific validation
        if (dto.getNic() == null || dto.getNic().trim().isEmpty()) {
            return false;
        }
        
        // Validate NIC format (basic validation)
        String nic = dto.getNic().trim();
        if (nic.length() != 12 && nic.length() != 15) {
            return false;
        }
        
        // Additional NIC-specific validations can be added here
        return true;
    }
    
    @Override
    public void processApplication(NicApplication application, NicApplicationWizardDto dto) {
        // NIC-specific processing
        application.setApplicationType("NIC");
        application.setApplicationCategory(dto.getApplicationCategory());
        
        // Set NIC-specific fields
        if (dto.getNic() != null) {
            application.setNic(dto.getNic().trim());
        }
        
        // Additional NIC-specific processing can be added here
    }
    
    @Override
    public String getRedirectUrl() {
        return "/nic/my";
    }
    
    @Override
    public String getValidationErrorMessage() {
        return "NIC number is required and must be 12 or 15 digits";
    }
}
