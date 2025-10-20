package org.example.ceid_v2.service;

import org.example.ceid_v2.model.NicApplication;
import org.example.ceid_v2.model.User;
import org.example.ceid_v2.repository.NicApplicationRepository;
import org.example.ceid_v2.repository.ReviewRecordRepository;
import org.example.ceid_v2.repository.DocumentReviewRepository;
import org.example.ceid_v2.repository.DeliveryDetailRepository;
import org.example.ceid_v2.repository.ApplicationDocumentRepository;
import org.example.ceid_v2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class NicApplicationService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Autowired
    private NicApplicationRepository nicApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRecordRepository reviewRecordRepository;

    @Autowired
    private DocumentReviewRepository documentReviewRepository;

    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Autowired
    private ApplicationDocumentRepository applicationDocumentRepository;

    public NicApplication create(String username, String fullName, String address, String contactNumber, MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow();

        NicApplication app = new NicApplication();
        app.setUser(user);
        app.setFullName(fullName);
        app.setAddress(address);
        app.setContactNumber(contactNumber);

        if (file != null && !file.isEmpty()) {
            Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(root);
            String storedName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path target = root.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            app.setUploadedFilePath(target.toString());
            app.setUploadedFileName(file.getOriginalFilename());
        }

        return nicApplicationRepository.save(app);
    }

    public NicApplication save(NicApplication app){
        return nicApplicationRepository.save(app);
    }

    public List<NicApplication> listForUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return nicApplicationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public NicApplication findOwned(String username, Long id) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return nicApplicationRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow();
    }

    public NicApplication updateOwned(String username, Long id, String address, String contactNumber, String status) {
        NicApplication app = findOwned(username, id);
        if (address != null) app.setAddress(address);
        if (contactNumber != null) app.setContactNumber(contactNumber);
        if (status != null) app.setStatus(status);
        return nicApplicationRepository.save(app);
    }

    public void deleteOwned(String username, Long id) {
        NicApplication app = findOwned(username, id);
        // Delete dependent child records to satisfy FK constraints
        java.util.List<org.example.ceid_v2.model.ReviewRecord> reviews = reviewRecordRepository.findByApplicationOrderByCreatedAtDesc(app);
        if (!reviews.isEmpty()) {
            reviewRecordRepository.deleteAll(reviews);
        }
        java.util.List<org.example.ceid_v2.model.DocumentReview> docReviews = documentReviewRepository.findByApplicationOrderByCreatedAtDesc(app);
        if (!docReviews.isEmpty()) {
            documentReviewRepository.deleteAll(docReviews);
        }
        java.util.List<org.example.ceid_v2.model.DeliveryDetail> deliveries = deliveryDetailRepository.findByApplicationOrderByCreatedAtDesc(app);
        if (!deliveries.isEmpty()) {
            deliveryDetailRepository.deleteAll(deliveries);
        }
        java.util.List<org.example.ceid_v2.model.ApplicationDocument> docs = applicationDocumentRepository.findByApplication(app);
        if (!docs.isEmpty()) {
            applicationDocumentRepository.deleteAll(docs);
        }
        nicApplicationRepository.delete(app);
    }
}



