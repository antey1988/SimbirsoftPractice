package com.example.SimbirsoftPractice.rest.dto;

import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Данные о задаче (from front to back)")
public class TaskRequestDto {
    @Schema(description = "Наименование задачи")
    private String name;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Создатель задачи")
    private UserRequestDto creator;
    @Schema(description = "Исполнитель задачи")
    private UserRequestDto executor;
    @Schema(description = "Дата создания задачи")
    private Date startDate;
    @Schema(description = "Дата решения задачи")
    private Date stopDate;
    @Schema(description = "Статус задачи")
    private StatusTask status;
    @Schema(description = "Проект")
    private ProjectRequestDto project;
    @Schema(description = "Номер доски проекта")
    private int border;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserRequestDto getCreator() {
        return creator;
    }

    public void setCreator(UserRequestDto creator) {
        this.creator = creator;
    }

    public UserRequestDto getExecutor() {
        return executor;
    }

    public void setExecutor(UserRequestDto executor) {
        this.executor = executor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public ProjectRequestDto getProject() {
        return project;
    }

    public void setProject(ProjectRequestDto project) {
        this.project = project;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }
}
