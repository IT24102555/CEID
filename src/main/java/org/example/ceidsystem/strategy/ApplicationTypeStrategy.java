package org.example.ceid_v2.strategy;

import org.example.ceid_v2.dto.NicApplicationWizardDto;
import org.example.ceid_v2.model.NicApplication;

/**
 * Strategy interface for handling different application types
 * Each application type (NIC, Driving License, Passport) will have its own implementation
 */
public interface ApplicationTypeStrategy {
    
    /**
     * Get the application type name
     * @return the type name (e.g., "NIC", "DRIVING_LICENSE", "PASSPORT")
     */
    String getApplicationType();
    
    /**
     * Get the display name for the application type
     * @return the display name (e.g., "NIC Application", "Driving License", "Passport")
     */
    String getDisplayName();
    
    /**
     * Get the icon class for the application type
     * @return the FontAwesome icon class
     */
    String getIconClass();
    
    /**
     * Validate application-specific requirements
     * @param dto the wizard DTO containing form data
     * @return true if validation passes, false otherwise
     */
    boolean validateApplication(NicApplicationWizardDto dto);
    
    /**
     * Process application-specific logic before saving
     * @param application the application entity
     * @param dto the wizard DTO
     */
    void processApplication(NicApplication application, NicApplicationWizardDto dto);
    
    /**
     * Get the redirect URL after successful submission
     * @return the redirect URL
     */
    String getRedirectUrl();
    
    /**
     * Get application-specific validation error message
     * @return the error message if validation fails
     */
    String getValidationErrorMessage();
}
