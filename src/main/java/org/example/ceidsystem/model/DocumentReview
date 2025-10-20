package org.example.ceid_v2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_reviews")
public class DocumentReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private NicApplication application;

    @Column(nullable = false)
    private String reviewerUsername;

    @Column(length = 2000)
    private String remarks;

    @Column(nullable = false)
    private String state = "DELAYED"; // DELAYED or READY_TO_ISSUE

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NicApplication getApplication() { return application; }
    public void setApplication(NicApplication application) { this.application = application; }
    public String getReviewerUsername() { return reviewerUsername; }
    public void setReviewerUsername(String reviewerUsername) { this.reviewerUsername = reviewerUsername; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}



