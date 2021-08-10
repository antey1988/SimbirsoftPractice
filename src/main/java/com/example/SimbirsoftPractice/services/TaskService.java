package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {

    TaskResponseDto createTask(TaskRequestDto taskRequestDto);
    TaskResponseDto readTask(Long id);
    TaskResponseDto updateTask(TaskRequestDto taskRequestDto, Long id);
    void deleteTask(Long id);
    List<TaskResponseDto> readListTasksByReleaseId(Long id, List<StatusTask> statuses);
    List<TaskResponseDto> readListTasksByCreatorId(Long id);
    List<TaskResponseDto> readListTasksByExecutorId(Long id);
    List<TaskResponseDto> readListAllTasksByFilters(Long rId, Long cId, Long eId, List<StatusTask> statuses);
}
