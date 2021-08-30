package com.example.SimbirsoftPractice.utils;

import com.example.SimbirsoftPractice.entities.*;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.*;

public class UtilTasks {
    private static UtilTasks.TaskBuilder defaultTask = builder()
            .id(1L).name("Name").description("Description").release(UtilReleases.defaultEntity())
            .creator(UtilUsers.defaultEntity()).executor(UtilUsers.defaultEntity())
            .status(StatusTask.IN_PROGRESS).border(0);

    public static TaskResponseDto defaultResponse() {
        return defaultTask.buildResponse();
    }

    public static TaskRequestDto defaultRequest() {
        return defaultTask.buildRequest();
    }

    public static TaskEntity defaultEntity() {
        return defaultTask.buildEntity();
    }

    public static UtilTasks.TaskBuilder builder() {
        return new UtilTasks.TaskBuilder();
    }

    private static class TaskBuilder {
        private Long id;
        private String name;
        private String description;
        private ReleaseEntity release;
        private UserEntity creator;
        private UserEntity executor;
        private StatusTask status;
        private int border;

        public UtilTasks.TaskBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UtilTasks.TaskBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UtilTasks.TaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public UtilTasks.TaskBuilder release(ReleaseEntity release) {
            this.release = release;
            return this;
        }

        public UtilTasks.TaskBuilder creator(UserEntity user) {
            this.creator = user;
            return this;
        }

        public UtilTasks.TaskBuilder executor(UserEntity user) {
            this.executor = user;
            return this;
        }

        public UtilTasks.TaskBuilder status(StatusTask status) {
            this.status = status;
            return this;
        }

        public UtilTasks.TaskBuilder border(int border) {
            this.border = border;
            return this;
        }

        public TaskEntity buildEntity() {
            TaskEntity value = new TaskEntity();
            value.setId(this.id);
            value.setName(this.name);
            value.setDescription(this.description);
            value.setRelease(this.release);
            value.setCreator(this.creator);
            value.setExecutor(this.executor);
            value.setStatus(this.status);
            value.setBorder(this.border);
            return value;
        }

        public TaskResponseDto buildResponse() {
            TaskResponseDto value = new TaskResponseDto();
            value.setId(this.id);
            value.setName(this.name);
            value.setDescription(this.description);
            value.setRelease(this.release.getId());
            value.setCreator(this.creator.getId());
            value.setExecutor(this.executor.getId());
            value.setStatus(this.status);
            value.setBorder(this.border);
            return value;
        }

        public TaskRequestDto buildRequest() {
            TaskRequestDto value = new TaskRequestDto();
            value.setName(this.name);
            value.setDescription(this.description);
            value.setRelease(this.release.getId());
            value.setCreator(this.creator.getId());
            value.setExecutor(this.executor.getId());
            value.setStatus(this.status);
            value.setBorder(this.border);
            return value;
        }
    }

}
