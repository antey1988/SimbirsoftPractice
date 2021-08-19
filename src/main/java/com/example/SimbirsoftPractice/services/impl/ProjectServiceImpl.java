package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.mappers.ProjectMapper;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.services.PaymentService;
import com.example.SimbirsoftPractice.services.ProjectService;
import com.example.SimbirsoftPractice.services.validators.ProjectValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProjectMapper mapper;
    private final ProjectRepository repository;
    private final ProjectValidatorService validator;
    private final MessageSource messageSource;
    private final PaymentService paymentService;

    public ProjectServiceImpl(ProjectMapper mapper, ProjectRepository repository, ProjectValidatorService validator,
                              MessageSource messageSource, PaymentService paymentService) {
        this.mapper = mapper;
        this.repository = repository;
        this.validator = validator;
        this.messageSource = messageSource;
        this.paymentService = paymentService;
    }

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto, Locale locale) {
        ProjectEntity projectEntity = validator.validate(projectRequestDto, new ProjectEntity(), locale);
        projectEntity = repository.save(projectEntity);
        logger.info("New Project added to DB");
        return mapper.entityToResponseDto(projectEntity);
    }

    @Override
    public ProjectResponseDto readProject(Long id, Locale locale) {
        ProjectEntity projectEntity = getOrElseThrow(id, locale);
        return mapper.entityToResponseDto(projectEntity);
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id, Locale locale) {
        ProjectEntity projectEntity = getOrElseThrow(id, locale);
        projectEntity =  validator.validate(projectRequestDto, projectEntity, locale);
        ProjectResponseDto response = mapper.entityToResponseDto(projectEntity);
        if (projectEntity.getStatus() == StatusProject.OPEN) {
            PaymentProjectRequestDto payment = mapper.entityToPaymentProjectRequestDto(projectEntity);
            paymentService.payProject(payment, locale);
        }
        logger.info("Project updated in the DB");
        return response;
    }

    @Override
    public void deleteProject(Long id, Locale locale) {
        getOrElseThrow(id, locale);
        repository.deleteById(id);
        logger.info("Project deleted from DB");
    }

    @Override
    public List<ProjectResponseDto> readListProjects(Long id) {
        List<ProjectEntity> list;
        if (id == null) {
            list = repository.findAll();
            logger.info("All records retrieved from the DB");
        } else {
            list = repository.findByCustomerId(id);
            logger.info(String.format("Records with field custom_id = %d retrieved from the DB", id));
        }
        return mapper.listEntityToListResponseDto(list);
    }

    private ProjectEntity getOrElseThrow(Long id, Locale locale ) {
        logger.info(String.format("Extracting Project with identifier(id) = %d from DB", id));
        Optional<ProjectEntity> optional = repository.findById(id);
        return optional.orElseThrow(() -> {
            logger.error(String.format("Project with identifier (id) =% d does not exist", id));
            String error = messageSource.getMessage("error.NotFound", null, locale);
            String record = messageSource.getMessage("record.Project", null, locale);
            return new NotFoundException(String.format(error, record, id));
        });
    }
}
