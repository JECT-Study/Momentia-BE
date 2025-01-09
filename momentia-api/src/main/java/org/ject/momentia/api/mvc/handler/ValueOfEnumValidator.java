package org.ject.momentia.api.mvc.handler;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ject.momentia.api.mvc.annotation.EnumValue;

public class ValueOfEnumValidator implements ConstraintValidator<EnumValue, String> {

    private EnumValue enumValue;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.enumValue = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean result = false;
        Enum<?>[] enumValues = this.enumValue.enumClass().getEnumConstants();

        if(value==null){ return enumValue.nullable(); }

        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString())
                        || this.enumValue.ignoreCase() && value.equalsIgnoreCase(enumValue.toString())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
