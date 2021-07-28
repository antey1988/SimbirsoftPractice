package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;

import java.util.List;

public interface ReleaseService {
    ReleaseResponseDto createRelease(ReleaseRequestDto releaseRequestDto);
    ReleaseResponseDto readRelease(Long id);
    ReleaseResponseDto updateRelease(ReleaseRequestDto releaseRequestDto, Long id);
    void deleteRelease(Long id);
    List<ReleaseResponseDto> readListReleaseByProjectId(Long id);
}
