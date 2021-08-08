package com.example.SimbirsoftPractice.validators;

import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseValidator<DTO, ENTITY> {

    private static final String WARN_NULL_VALUE_FIELD = "Поле %s должно быть заполнено";
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Class<?> dto ;
    private Class<?> entity;
    protected Set< FieldValidator> methodsOnCreation = new HashSet<>();
    protected Set< FieldValidator> methodsOnUpdate = new HashSet<>();

    public BaseValidator(Class<?> dto, Class<?> entity) throws NoSuchMethodException {
        Map<String, Field> fields = Arrays.stream(entity.getFields()).collect(Collectors.toMap(Field::getName, f->f));
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(Valid.class)) {
                Valid valid = method.getAnnotation(Valid.class);
                String field = valid.field();
                if (!fields.containsKey(valid.field())) {
                    logger.warn(String.format("В классе %s нет поля с наименованием %s", entity, field));
                    continue;
                }
                fields.remove(field);
                TypeValid typeValid = valid.type();
                FieldValidator fieldValidator = new FieldValidator(method);
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

        for (Field field : fields.values()) {
            boolean isCreate
        }

    }


    protected Object validateOnCreation(Object source, Object target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Map<String, String> methodsOnCreation = new HashMap<>();
        Method method = this.getClass().getMethod("validateOnCreation", Object.class, Object.class);
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof Valid){
                Valid valid = (Valid) annotation;
                methodsOnCreation.put(valid.field(), valid.method());
            }
        }

        for(Field field : target.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }

            String fieldName = field.getName();
            if (methodsOnCreation.containsKey(fieldName)) {
                method = this.getClass().getMethod(methodsOnCreation.get(fieldName), Object.class, Object.class);
                method.invoke(this, source, target);
                continue;
            }

            Method getter = source.getClass().getMethod(String.format("get%s%s", fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1)));
            Method setter = target.getClass().getMethod(String.format("set%s%s", fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1)), Object.class);
            if (field.isAnnotationPresent(NotNullOnCreation.class)) {
//                Method getter = source.getClass().getMethod(String.format("get%s%s", fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1)));
                Class<?> clazz = getter.getReturnType();
            }

        }
        return target;
    }
    protected ENTITY validateOnUpdate(DTO source, ENTITY target) {
        return target;

    }

    private void validateField(Object source, Object target, Method getter, Method setter, boolean isCreate)
            throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Object fromSource = getter.invoke(source);
        //если создание и поле пустое, сообщаем об обязательности заполнения
        if (fromSource == null && isCreate) {
            String text = String.format(WARN_NULL_VALUE_FIELD, "fdf");
            logger.warn(text);
            throw new NullValueFieldException(text);
        }
        //если обновление и поле пустое, то заканчиваем проверку
        if (fromSource == null) {
            return;
        }

        Class<?> typeFieldInSource = getter.getReturnType();
        Class<?> typeFieldInTarget = setter.getParameterTypes()[0];
        Object newValue;
        if (!typeFieldInTarget.equals(typeFieldInSource)) {
            newValue = typeFieldInTarget.newInstance();
            newValue.getClass().getMethod("setId", typeFieldInSource).invoke(newValue, fromSource);
        } else {
            newValue = fromSource;
        }
        setter.invoke(target, newValue);
    }

    private class FieldValidator{
        private final Method methodValidation;
        private final Object[] args;

        private FieldValidator(Method methodValidation, Object ... args) {
            this.methodValidation = methodValidation;
            this.args = args;
        }

        private void invoke(Object source, Object target) throws InvocationTargetException, IllegalAccessException {
            methodValidation.invoke(BaseValidator.this, source, target, args);
        }
    }

}
