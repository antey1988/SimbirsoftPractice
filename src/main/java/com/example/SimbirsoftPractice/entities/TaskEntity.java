package com.example.SimbirsoftPractice.entities;

import com.example.SimbirsoftPractice.rest.domain.StatusTask;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserEntity creator;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity executor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ReleaseEntity release;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusTask status;

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

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public UserEntity getExecutor() {
        return executor;
    }

    public void setExecutor(UserEntity executor) {
        this.executor = executor;
    }

    public ReleaseEntity getRelease() {
        return release;
    }

    public void setRelease(ReleaseEntity release) {
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
