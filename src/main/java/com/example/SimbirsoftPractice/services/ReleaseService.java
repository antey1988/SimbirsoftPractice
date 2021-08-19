package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;

import java.util.List;
import java.util.Locale;

public interface ReleaseService {
    ReleaseResponseDto createRelease(ReleaseRequestDto releaseRequestDto, Locale locale);
    ReleaseResponseDto readRelease(Long id, Locale locale);
    ReleaseResponseDto updateRelease(ReleaseRequestDto releaseRequestDto, Long id, Locale locale);
    void deleteRelease(Long id, Locale locale);
    List<ReleaseResponseDto> readListReleaseByProjectId(Long id);
}
