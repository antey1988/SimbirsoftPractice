package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

public interface TaskCSVService {
    String saveFile(MultipartFile file, Locale locale);
    String createFromCSV(String filename, Locale locale);
}
