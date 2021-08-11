package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskCSVService {
    String saveFile(MultipartFile file);
    List<TaskResponseDto> createFromCSV(String filename);
}
