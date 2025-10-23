package org.example.ceid_v2.repository;

import org.example.ceid_v2.model.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    List<ContactPerson> findAllByOrderByDepartmentAscNameAsc();
}




