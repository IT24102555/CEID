package org.example.ceid_v2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "nic_applications")
public class NicApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column
    private String uploadedFilePath; // filesystem path

    @Column
    private String uploadedFileName; // original filename

    // Wizard-captured fields
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String nic; // existing NIC
    @Column
    private LocalDate dateOfBirth;
    @Column
    private String gender;
    @Column
    private String nationality;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String addressLine1;
    @Column
    private String city;
    @Column
    private String district;
    @Column
    private String postalCode;
    @Column
    private String applicationType; // Main type: NIC/DRIVING_LICENSE/PASSPORT
    @Column
    private String applicationCategory; // Category: New/Renewal/Replacement
    @Column(length = 2000)
    private String reasonForRequest;
    @Column(length = 2000)
    private String remarks;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getUploadedFilePath() { return uploadedFilePath; }
    public void setUploadedFilePath(String uploadedFilePath) { this.uploadedFilePath = uploadedFilePath; }
    public String getUploadedFileName() { return uploadedFileName; }
    public void setUploadedFileName(String uploadedFileName) { this.uploadedFileName = uploadedFileName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getApplicationType() { return applicationType; }
    public void setApplicationType(String applicationType) { this.applicationType = applicationType; }
    public String getApplicationCategory() { return applicationCategory; }
    public void setApplicationCategory(String applicationCategory) { this.applicationCategory = applicationCategory; }
    public String getReasonForRequest() { return reasonForRequest; }
    public void setReasonForRequest(String reasonForRequest) { this.reasonForRequest = reasonForRequest; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}



