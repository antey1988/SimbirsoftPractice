/**
 * разрешения на работу с ресурсами
 * ROLE_READ_USERS - позволяет просматривать информацию о пользователях
 * ROLE_CRUD_USERS - позволяет создавать, редактировать и удалять пользователей
 * ROLE_CRUD_CUSTOMERS - позволяет просматривать, создавать, редактировать и удалять заказчиков
 * ROLE_READ_OTHERS - позволяет просматривать информацию о проектах, релизах и задачах
 * ROLE_CRUD_OTHERS - позволяет создавать, редактировать и удалять проекты, релизы и задачи
 */
package com.example.SimbirsoftPractice.rest.domain;

public enum Role {
    ROLE_READ_USERS,
    ROLE_CRUD_USERS,
    ROLE_CRUD_CUSTOMERS,
    ROLE_READ_OTHERS,
    ROLE_CRUD_OTHERS
}
