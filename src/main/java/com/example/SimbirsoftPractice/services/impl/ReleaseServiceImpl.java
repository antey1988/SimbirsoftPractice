package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.mappers.ReleaseMapper;
import com.example.SimbirsoftPractice.repos.ReleaseRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import com.example.SimbirsoftPractice.services.ReleaseService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ReleaseServiceImpl implements ReleaseService {
    private final ReleaseMapper mapper;
    private final ReleaseRepository repository;

    public ReleaseServiceImpl(ReleaseMapper mapper, ReleaseRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public ReleaseResponseDto createRelease(ReleaseRequestDto releaseRequestDto) {
        ReleaseEntity releaseEntity = mapper.requestDtoToEntity(releaseRequestDto, new ReleaseEntity());
        releaseEntity = repository.save(releaseEntity);
        return mapper.entityToResponseDto(releaseEntity);
    }

    @Override
    public ReleaseResponseDto readRelease(Long id) {
        ReleaseEntity releaseEntity = getOrElseThrow(id);
        return mapper.entityToResponseDto(releaseEntity);
    }

    @Override
    @Transactional
    public ReleaseResponseDto updateRelease(ReleaseRequestDto releaseRequestDto, Long id) {
        ReleaseEntity releaseEntity = getOrElseThrow(id);
        releaseEntity = mapper.requestDtoToEntity(releaseRequestDto, releaseEntity);
        return mapper.entityToResponseDto(releaseEntity);
    }

    @Override
    public void deleteRelease(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
    }

    @Override
    public List<ReleaseResponseDto> readListReleaseByProjectId(Long id) {
        List<ReleaseEntity> list = repository.findAllByProjectId(id);
        return mapper.listEntityToListResponseDto(list);
    }

    private ReleaseEntity getOrElseThrow(Long id) {
        Optional<ReleaseEntity> ReleaseEntity = repository.findById(id);
        return ReleaseEntity.orElseThrow(() -> new NotFoundException(String.format("Релиз с id = %d не существует", id)));
    }
}
