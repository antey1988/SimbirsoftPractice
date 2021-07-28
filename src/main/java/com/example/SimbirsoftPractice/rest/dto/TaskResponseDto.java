package com.example.SimbirsoftPractice.rest.dto;

import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные о задаче (from back to front)")
public class TaskResponseDto {
    @Schema(description = "ID задачи")
    private Long id;

    @Schema(description = "Наименование задачи")
    private String name;

    @Schema(description = "Описание задачи")
    private String description;

    @Schema(description = "Создатель задачи")
    private Long creator;

    @Schema(description = "Исполнитель задачи")
    private Long executor;

    @Schema(description = "Релиз проекта")
    private Long release;

    @Schema(description = "Статус задачи")
    private StatusTask status;

    @Schema(description = "Номер доски проекта")
    private int border;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getExecutor() {
        return executor;
    }

    public void setExecutor(Long executor) {
        this.executor = executor;
    }

    public Long getRelease() {
        return release;
    }

    public void setRelease(Long release) {
        this.release = release;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }
}
