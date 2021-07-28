package com.example.SimbirsoftPractice.repos;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {
    //Все релизы по указанному проекту
    List<ReleaseEntity> findAllByProjectId(Long id);
}
