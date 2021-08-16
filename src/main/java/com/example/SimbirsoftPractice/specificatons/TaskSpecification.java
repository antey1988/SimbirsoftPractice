package com.example.SimbirsoftPractice.specificatons;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.List;

public class TaskSpecification {

    public static Specification<TaskEntity> createByTaskName(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (name == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    public static Specification<TaskEntity> createByTaskDescription(String description) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (description == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("description"), "%" + description + "%");
        };
    }

    public static Specification<TaskEntity> createByTaskReleases(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("release").get("id"), id);
        };
    }

    public static Specification<TaskEntity> createByTaskCreator(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("creator").get("id"), id);
        };
    }

    public static Specification<TaskEntity> createByTaskExecutor(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("executor").get("id"), id);
        };
    }

    public static Specification<TaskEntity> createByTaskStatus(List<StatusTask> statuses) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.in(root.get("status")).value(statuses);
        };
    }

    public static Specification<TaskEntity> createByTasksByProject(Long id, StatusTask status) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<TaskEntity, ReleaseEntity> releaseEntityJoin = root.join("release");
            Join<ReleaseEntity, ProjectEntity> projectEntityJoin = releaseEntityJoin.join("project");
            return criteriaBuilder.and(criteriaBuilder.equal(projectEntityJoin.get("id"), id),
                    criteriaBuilder.notEqual(root.get("status"), status));
        };
    }
}
