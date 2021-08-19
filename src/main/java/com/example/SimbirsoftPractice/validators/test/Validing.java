package com.example.SimbirsoftPractice.validators.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validing {
    String field();
    TypeValid type() default TypeValid.BOTH;
}
