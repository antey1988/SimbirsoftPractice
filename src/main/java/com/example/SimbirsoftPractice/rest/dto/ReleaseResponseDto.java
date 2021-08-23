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

    public ReleaseResponseDto() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReleaseResponseDto)) return false;
        final ReleaseResponseDto other = (ReleaseResponseDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$startDate = this.getStartDate();
        final Object other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !this$startDate.equals(other$startDate)) return false;
        final Object this$stopDate = this.getStopDate();
        final Object other$stopDate = other.getStopDate();
        if (this$stopDate == null ? other$stopDate != null : !this$stopDate.equals(other$stopDate)) return false;
        final Object this$project = this.getProject();
        final Object other$project = other.getProject();
        if (this$project == null ? other$project != null : !this$project.equals(other$project)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReleaseResponseDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $startDate = this.getStartDate();
        result = result * PRIME + ($startDate == null ? 43 : $startDate.hashCode());
        final Object $stopDate = this.getStopDate();
        result = result * PRIME + ($stopDate == null ? 43 : $stopDate.hashCode());
        final Object $project = this.getProject();
        result = result * PRIME + ($project == null ? 43 : $project.hashCode());
        return result;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getStopDate() {
        return this.stopDate;
    }

    public Long getProject() {
        return this.project;
    }
}
