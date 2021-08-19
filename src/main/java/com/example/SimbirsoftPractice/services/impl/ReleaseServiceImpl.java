package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.mappers.ReleaseMapper;
import com.example.SimbirsoftPractice.repos.ReleaseRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import com.example.SimbirsoftPractice.services.ReleaseService;
import com.example.SimbirsoftPractice.services.validators.ReleaseValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReleaseMapper mapper;
    private final ReleaseRepository repository;
    private final ReleaseValidatorService validator;
    private final MessageSource messageSource;

    public ReleaseServiceImpl(ReleaseMapper mapper, ReleaseRepository repository,
                              ReleaseValidatorService validator, MessageSource messageSource) {
        this.mapper = mapper;
        this.repository = repository;
        this.validator = validator;
        this.messageSource = messageSource;
    }

    @Override
    public ReleaseResponseDto createRelease(ReleaseRequestDto releaseRequestDto, Locale locale) {
        ReleaseEntity releaseEntity = validator.validate(releaseRequestDto, new ReleaseEntity(), locale);
        releaseEntity = repository.save(releaseEntity);
        logger.info("New Release added to DB");
        return mapper.entityToResponseDto(releaseEntity);
    }

    @Override
    public ReleaseResponseDto readRelease(Long id, Locale locale) {
        ReleaseEntity releaseEntity = getOrElseThrow(id, locale);
        return mapper.entityToResponseDto(releaseEntity);
    }

    @Override
    @Transactional
    public ReleaseResponseDto updateRelease(ReleaseRequestDto releaseRequestDto, Long id, Locale locale) {
        ReleaseEntity releaseEntity = getOrElseThrow(id, locale);
        releaseEntity = validator.validate(releaseRequestDto, releaseEntity, locale);
        logger.info("Release updated in the DB");
        return mapper.entityToResponseDto(releaseEntity);
    }

    @Override
    public void deleteRelease(Long id, Locale locale) {
        getOrElseThrow(id, locale);
        repository.deleteById(id);
        logger.info("Release deleted from DB");
    }

    @Override
    public List<ReleaseResponseDto> readListReleaseByProjectId(Long id) {
        List<ReleaseEntity> list = repository.findAllByProjectId(id);
        logger.info("All Releases retrieved from the DB");
        return mapper.listEntityToListResponseDto(list);
    }

    private ReleaseEntity getOrElseThrow(Long id, Locale locale ) {
        logger.info(String.format("Extracting Release with identifier(id) = %d from DB", id));
        Optional<ReleaseEntity> optional = repository.findById(id);
        return optional.orElseThrow(() -> {
            logger.error(String.format("Release with identifier (id) =% d does not exist", id));
            String error = messageSource.getMessage("error.NotFound", null, locale);
            String record = messageSource.getMessage("record.Release", null, locale);
            return new NotFoundException(String.format(error, record, id));
        });
    }
}
