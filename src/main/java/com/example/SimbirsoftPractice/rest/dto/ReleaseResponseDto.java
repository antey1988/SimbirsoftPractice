package com.example.SimbirsoftPractice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Данные о релизе проекта (from back to front)")
public class ReleaseResponseDto {
    @Schema(description = "ID релиза")
    private Long id;
    @Schema(description = "Наименование релиза")
    private String name;
    @Schema(description = "Дата начала релиза")
    private Date startDate;
    @Schema(description = "Дата окончания релиза")
    private Date stopDate;
    @Schema(description = "Проект")
    private Long project;

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

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }
}
