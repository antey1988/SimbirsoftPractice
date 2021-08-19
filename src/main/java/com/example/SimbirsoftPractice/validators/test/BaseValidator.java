package com.example.SimbirsoftPractice.validators.test;

import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseValidator<DTO, ENTITY> {

    private static final String WARN_NULL_VALUE_FIELD = "Поле %s должно быть заполнено";
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Set<MethodValidation> methodsOnCreation = new HashSet<>();
    protected Set<MethodValidation> methodsOnUpdate = new HashSet<>();

    public BaseValidator(Class<DTO> dto, Class<ENTITY> entity) throws NoSuchMethodException, NoSuchFieldException {
        //ссылки на методы проверки по умолчанию (при создании и обновлении)
        Method defaultMethodNotNullField = this.getClass().getMethod("validateNotNullFieldDefault",
                Object.class, Object.class, Field.class, Field.class);
        Method defaultMethodMayBeNullField = this.getClass().getMethod("validateMayBeNullFieldDefault",
                Object.class, Object.class, Field.class, Field.class);
        //все поля в классе сущности и классе дто
        Map<String, Field> fieldsEntity = Arrays.stream(entity.getDeclaredFields())
                .filter(f->!f.getName().equals("id")).collect(Collectors.toMap(Field::getName, f->f));
        Map<String, Field> fieldsDto = Arrays.stream(dto.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, f->f));
        List<String> badField = new ArrayList<>();
        //для каждого поля из класса сущности ищем соответствующее поле в классе дто,
        //если не найдено добавляем в список "плохих" полей
        for (Map.Entry<String, Field> field : fieldsEntity.entrySet()) {
            if (field.getValue().isAnnotationPresent(ValidIgnore.class)) {
                continue;
            }
            String fieldName = field.getKey();
            if (!fieldsDto.containsKey(fieldName)) {
                badField.add(fieldName);
                continue;
            }
            //добавляем класс с методом проверки по умолчанию,
            //передавая в параметрах поля-источник и поле-цель
            if (field.getValue().isAnnotationPresent(ValidNotNull.class)) {
                methodsOnCreation.add(new MethodValidation(fieldName, defaultMethodNotNullField, fieldsDto.get(fieldName), field.getValue()));
            } else {
                methodsOnCreation.add(new MethodValidation(fieldName, defaultMethodMayBeNullField, fieldsDto.get(fieldName), field.getValue()));
            }
            methodsOnUpdate.add(new MethodValidation(fieldName, defaultMethodMayBeNullField, fieldsDto.get(fieldName), field.getValue()));
        }
        //поиск методов, в которых самостоятельно описана проверка полей
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(Validing.class)) {
                Validing valid = method.getAnnotation(Validing.class);
                String fieldName = valid.field();
                if (!fieldsEntity.containsKey(valid.field())) {
                    logger.warn(String.format("В классе %s нет поля с наименованием %s", entity, fieldName));
                    continue;
                }
                //удаляем из списка "плохих" полей
                badField.remove(fieldName);
                TypeValid typeValid = valid.type();
                MethodValidation fieldValidator = new MethodValidation(fieldName, method);
                if (typeValid == TypeValid.BOTH) {                    
                    methodsOnCreation.add(fieldValidator);
                    methodsOnUpdate.add(fieldValidator);
                } else if (typeValid == TypeValid.ON_CREATION) {
                    methodsOnCreation.add(fieldValidator);
                } else {
                    methodsOnUpdate.add(fieldValidator);
                }
            }
        }
        if (!badField.isEmpty()) {
            String text = String.format("Для ниже указанных полей из класса %s " +
                    "не найдены соответствующие поля c таким же именем в классе %s, " +
                    "либо не реализованы собственные методы проверок заполнения \n %s", entity, dto, badField);
            logger.error(text);
            throw new NoSuchFieldException(text);
        }

    }

    public ENTITY validateOnCreation(DTO dto, ENTITY entity) {
        System.out.println("");
        methodsOnCreation.forEach(m-> {
            try {
                m.invoke(dto, entity);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return entity;
    }

    public ENTITY validateOnUpdate(DTO dto, ENTITY entity) {
        System.out.println("");
        methodsOnUpdate.forEach(m-> {
            try {
                m.invoke(dto, entity);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return entity;
    }

    //стандартный метод проверки обязательного поля
    public final void validateNotNullFieldDefault(DTO source, ENTITY target, Field fieldSource, Field fieldTarget)
            throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        fieldSource.setAccessible(true);
        Object valueSource = fieldSource.get(source);
        if (valueSource == null) {
            String text = String.format(WARN_NULL_VALUE_FIELD, "fdf");
            logger.warn(text);
            throw new NullValueFieldException(text);
        }
        setFromSourceToTarget(source, target, fieldSource, fieldTarget);
    }
    //стандартный метод проверки не обязательного поля
    public final void validateMayBeNullFieldDefault(DTO source, ENTITY target, Field fieldSource, Field fieldTarget)
            throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        fieldSource.setAccessible(true);
        Object valueSource = fieldSource.get(source);
        if (valueSource == null) {
            return;
        }
        setFromSourceToTarget(source, target, fieldSource, fieldTarget);
    }
    //метод заполнения или обновление поля, если новое значение не null
    private void setFromSourceToTarget(Object source, Object target, Field fieldSource, Field fieldTarget)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        fieldSource.setAccessible(true);
        Object valueSource = fieldSource.get(source);
        Class<?> typeFieldInSource = fieldSource.getType();
        Class<?> typeFieldInTarget = fieldTarget.getType();
        Object valueTarget;
        if (!typeFieldInTarget.equals(typeFieldInSource)) {
            valueTarget = typeFieldInTarget.newInstance();
            Field id = typeFieldInTarget.getField("id");
            id.setAccessible(true);
            id.set(valueTarget, valueSource);
            valueSource = valueTarget;
        }
        fieldTarget.setAccessible(true);
        fieldTarget.set(target, valueSource);
    }

    private class MethodValidation {
        private final String fieldName;
        private final Method methodValidation;
        private final Object[] args;

        private MethodValidation(String fieldName, Method methodValidation, Object ... args) {
            this.fieldName = fieldName;
            this.methodValidation = methodValidation;
            this.args = (args == null) || (args.length == 0) ? new Object[2] : new Object[]{null, null, args[0], args[1]};
        }

        private void invoke(DTO source, ENTITY target) throws InvocationTargetException, IllegalAccessException {
            args[0] = source;
            args[1] = target;
            methodValidation.invoke(BaseValidator.this, args);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodValidation that = (MethodValidation) o;

            return fieldName.equals(that.fieldName);
        }

        @Override
        public int hashCode() {
            return fieldName.hashCode();
        }
    }

}
