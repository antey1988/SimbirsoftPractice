package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.mappers.ReleaseMapper;
import com.example.SimbirsoftPractice.repos.ReleaseRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import com.example.SimbirsoftPractice.services.ReleaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        logger.info("Новая запись добавлена в базу данных");
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
        logger.info("Запись обновлена в базе данных");
        return mapper.entityToResponseDto(releaseEntity);
    }

    @Override
    public void deleteRelease(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
        logger.info("Запись удалена из базы данных");
    }

    @Override
    public List<ReleaseResponseDto> readListReleaseByProjectId(Long id) {
        List<ReleaseEntity> list = repository.findAllByProjectId(id);
        logger.info("Список записей извлечен из базы данных");
        return mapper.listEntityToListResponseDto(list);
    }

    private ReleaseEntity getOrElseThrow(Long id) {
        logger.info(String.format("Попытка извлечения записи c id = %d из базы данных", id));
        Optional<ReleaseEntity> ReleaseEntity = repository.findById(id);
        ReleaseEntity entity = ReleaseEntity.orElseThrow(() -> {
            logger.warn(String.format("Запись c id = %d отсутсвует в базе данных", id));
            return new NotFoundException(String.format("Релиз с id = %d не существует", id));
        });
        logger.info(String.format("Запись c id = %d успешно извлечена из базы данных", id));
        return entity;
    }
}
