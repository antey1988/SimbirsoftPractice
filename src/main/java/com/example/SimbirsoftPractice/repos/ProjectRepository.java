package com.example.SimbirsoftPractice.repos;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByCustomerId(Long id);

    @Query(value = "select p from ProjectEntity p " +
            "where p.id = (select r.project.id from ReleaseEntity r " +
            "where r.id = :releaseId)")
    ProjectEntity getProjectByRelease(Long releaseId);
}
