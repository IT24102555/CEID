package org.example.ceid_v2.repository;

import org.example.ceid_v2.model.NicApplication;
import org.example.ceid_v2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NicApplicationRepository extends JpaRepository<NicApplication, Long> {
    List<NicApplication> findByUserOrderByCreatedAtDesc(User user);
    List<NicApplication> findByStatusOrderByCreatedAtDesc(String status);
    List<NicApplication> findByStatusInOrderByCreatedAtDesc(List<String> statuses);

    long countByStatus(String status);

    @Query("select a.status as status, count(a) as cnt from NicApplication a group by a.status")
    List<Object[]> countGroupedByStatus();
}



