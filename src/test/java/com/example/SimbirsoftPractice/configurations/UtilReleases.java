package com.example.SimbirsoftPractice.configurations;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;

import java.util.Date;

public class UtilReleases {
    private static UtilReleases.ReleaseBuilder defaultRelease = builder()
            .id(1L).name("Name").project(UtilProjects.defaultEntity()).startDate(new Date()).stopDate(new Date());

    public static ReleaseResponseDto defaultResponse() {
        return defaultRelease.buildResponse();
    }

    public static ReleaseRequestDto defaultRequest() {
        return defaultRelease.buildRequest();
    }

    public static ReleaseEntity defaultEntity() {
        return defaultRelease.buildEntity();
    }

    public static UtilReleases.ReleaseBuilder builder() {
        return new UtilReleases.ReleaseBuilder();
    }

    private static class ReleaseBuilder {
        private Long id;
        private String name;
        private ProjectEntity project;
        private Date start;
        private Date stop;

        public UtilReleases.ReleaseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UtilReleases.ReleaseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UtilReleases.ReleaseBuilder project(ProjectEntity project) {
            this.project = project;
            return this;
        }

        public UtilReleases.ReleaseBuilder startDate(Date date) {
            this.start = date;
            return this;
        }

        public UtilReleases.ReleaseBuilder stopDate(Date date) {
            this.stop = date;
            return this;
        }

        public ReleaseEntity buildEntity() {
            ReleaseEntity value = new ReleaseEntity();
            value.setId(this.id);
            value.setName(this.name);
            value.setProject(this.project);
            value.setStartDate(this.start);
            value.setStopDate(this.stop);
            return value;
        }

        public ReleaseResponseDto buildResponse() {
            ReleaseResponseDto value = new ReleaseResponseDto();
            value.setId(this.id);
            value.setName(this.name);
            value.setProject(this.project.getId());
            value.setStartDate(this.start);
            value.setStopDate(this.stop);
            return value;
        }

        public ReleaseRequestDto buildRequest() {
            ReleaseRequestDto value = new ReleaseRequestDto();
            value.setName(this.name);
            value.setProject(this.project.getId());
            value.setStartDate(this.start);
            value.setStopDate(this.stop);
            return value;
        }
    }

}
