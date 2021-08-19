package com.example.SimbirsoftPractice.services.validators;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;

import java.util.Locale;

public interface ReleaseValidatorService {
    ReleaseEntity validate(ReleaseRequestDto dto, ReleaseEntity entity, Locale locale);
}
