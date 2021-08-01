package com.example.SimbirsoftPractice.repos;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByCustomerId(Long id);

    @Query(value = "select p.status from ProjectEntity p " +
            "where p.id = (select r.project from ReleaseEntity r " +
            "where r.id = (select t.release from TaskEntity t " +
            "where t = :taskId))")
    StatusProject getStatus(Long taskId);
}
