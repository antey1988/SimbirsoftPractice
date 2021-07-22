package com.example.SimbirsoftPractice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Данные о релизе проекта (from front to back)")
public class ReleaseRequestDto {
    @Schema(description = "Наименование релиза")
    private String name;
    @Schema(description = "Дата начала релиза")
    private Date startDate;
    @Schema(description = "Дата окончания релиза")
    private Date stopDate;

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
}
