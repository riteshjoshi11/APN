package com.ANP.bean;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Constraint(validatedBy = AtLeastOneNotEmptyValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@interface AtLeastOneNotEmpty {

    String message() default "Either firmname or name is required";

    String[] fields();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

public class AtLeastOneNotEmptyValidator
        implements ConstraintValidator<AtLeastOneNotEmpty, Object> {

    private String[] fields;

    public void initialize(AtLeastOneNotEmpty constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {

        List<String> fieldValues = new ArrayList<String>();

        for (String field : fields) {
            Object propertyValue = new BeanWrapperImpl(value).getPropertyValue(field);
            if (ObjectUtils.isEmpty(propertyValue)) {
                fieldValues.add(null);
            } else {
                fieldValues.add(propertyValue.toString());
            }
        }
        return fieldValues.stream().anyMatch(fieldValue -> fieldValue!= null);
    }
}