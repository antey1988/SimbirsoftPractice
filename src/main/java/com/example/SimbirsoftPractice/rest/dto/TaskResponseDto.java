package com.example.SimbirsoftPractice.rest.dto;

import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Данные о задаче (from back to front)")
public class TaskResponseDto {
    @Schema(description = "ID задачи")
    private Long id;
    @Schema(description = "Наименование задачи")
    private String name;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Создатель задачи")
    private UserResponseDto creator;
    @Schema(description = "Исполнитель задачи")
    private UserResponseDto executor;
    @Schema(description = "Дата создания задачи")
    private Date startDate;
    @Schema(description = "Дата решения задачи")
    private Date stopDate;
    @Schema(description = "Статус задачи")
    private StatusTask status;
    @Schema(description = "Проект")
    private ProjectResponseDto project;
    @Schema(description = "Номер доски проекта")
    private int border;

    public TaskResponseDto(Long id, String name, String description, UserResponseDto creator, ProjectResponseDto project) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.project = project;
        this.border = 1;
        this.startDate = new Date();
        this.status = StatusTask.BACKLOG;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UserResponseDto getCreator() {
        return creator;
    }

    public UserResponseDto getExecutor() {
        return executor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public StatusTask getStatus() {
        return status;
    }

    public ProjectResponseDto getProject() {
        return project;
    }

    public int getBorder() {
        return border;
    }
}
