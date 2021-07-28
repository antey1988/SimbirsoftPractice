package com.example.SimbirsoftPractice.repos;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    //список задач, привязанные к списку релизов
    List<TaskEntity> findAllByReleaseId(Long id);
    //список задач, созданных указанным пользователем
    List<TaskEntity> findAllByCreatorId(Long id);
    //список задач, решенных и решаемых указанным пользователем
    List<TaskEntity> findAllByExecutorId(Long id);
}
