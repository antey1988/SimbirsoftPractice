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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TaskResponseDto)) return false;
        final TaskResponseDto other = (TaskResponseDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$creator = this.getCreator();
        final Object other$creator = other.getCreator();
        if (this$creator == null ? other$creator != null : !this$creator.equals(other$creator)) return false;
        final Object this$executor = this.getExecutor();
        final Object other$executor = other.getExecutor();
        if (this$executor == null ? other$executor != null : !this$executor.equals(other$executor)) return false;
        final Object this$release = this.getRelease();
        final Object other$release = other.getRelease();
        if (this$release == null ? other$release != null : !this$release.equals(other$release)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        if (this.getBorder() != other.getBorder()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TaskResponseDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $creator = this.getCreator();
        result = result * PRIME + ($creator == null ? 43 : $creator.hashCode());
        final Object $executor = this.getExecutor();
        result = result * PRIME + ($executor == null ? 43 : $executor.hashCode());
        final Object $release = this.getRelease();
        result = result * PRIME + ($release == null ? 43 : $release.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        result = result * PRIME + this.getBorder();
        return result;
    }
}
