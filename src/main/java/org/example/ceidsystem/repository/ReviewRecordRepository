package org.example.ceid_v2.repository;

import org.example.ceid_v2.model.NicApplication;
import org.example.ceid_v2.model.ReviewRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRecordRepository extends JpaRepository<ReviewRecord, Long> {
	List<ReviewRecord> findByApplicationOrderByCreatedAtDesc(NicApplication app);

    @Query("SELECT r FROM ReviewRecord r WHERE r.application = :app AND UPPER(r.decision) = 'APPROVED' ORDER BY r.createdAt DESC")
    List<ReviewRecord> findApprovedByApplicationOrderByCreatedAtDesc(@Param("app") NicApplication app);

    default Optional<ReviewRecord> findLatestApprovedByApplication(NicApplication app){
        List<ReviewRecord> list = findApprovedByApplicationOrderByCreatedAtDesc(app);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}



