package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;

import java.util.List;
import java.util.Locale;

public interface TaskService {

    TaskResponseDto createTask(TaskRequestDto taskRequestDto, Locale locale);
    TaskResponseDto readTask(Long id, Locale locale);
    TaskResponseDto updateTask(TaskRequestDto taskRequestDto, Long id, Locale locale);
    void deleteTask(Long id, Locale locale);
    List<TaskResponseDto> readListTasksByReleaseId(Long id, List<StatusTask> statuses);
    List<TaskResponseDto> readListTasksByCreatorId(Long id);
    List<TaskResponseDto> readListTasksByExecutorId(Long id);
    List<TaskResponseDto> readListAllTasksByFilters(String name, String description, Long rId, Long cId, Long eId, List<StatusTask> statuses);
}
