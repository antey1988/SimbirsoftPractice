package com.example.SimbirsoftPractice.repos;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    //список задач, привязанные к релизу с возможностью фильтрации по статусу,
    //пустой список статусов означает все возможные статусы
    @Query(value = "select t from TaskEntity t " +
            "where t.release.id = :id and ((:statuses) is null or t.status in (:statuses))")
    List<TaskEntity> findAllByReleaseId(Long id, List<StatusTask> statuses);

    //список задач, созданных указанным пользователем
    List<TaskEntity> findAllByCreatorId(Long id);

    //список задач, решенных и решаемых указанным пользователем
    List<TaskEntity> findAllByExecutorId(Long id);

    //количество задач проекта в указанном статусе
    @Query(value = "select count(p.id) from TaskEntity t " +
            "join t.release r " +
            "join r.project p " +
            "where p.id = :id and " +
            "t.status != :status")
    Long countTasksInProcessByProjectId(Long id, StatusTask status);

//    список задач, с фильтрацией по релизу, создателю, исполнителю и статусам
    @Query(value = "select t from TaskEntity t " +
            "where (:rId is null or t.release.id = :rId) and " +
            "(:cId is null or t.creator.id = :cId) and " +
            "(:eId is null or t.executor.id = :eId) and " +
            "((:statuses) is null or t.status in (:statuses))")
    List<TaskEntity> findAllByFilters(@Param("rId") Long rId, @Param("cId") Long cId,
                                      @Param("eId") Long eId, @Param("statuses") List<StatusTask> statuses);


}
