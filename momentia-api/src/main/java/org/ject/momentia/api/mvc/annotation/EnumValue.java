package org.ject.momentia.api.mvc.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.ject.momentia.api.mvc.handler.ValueOfEnumValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface EnumValue {
	Class<? extends Enum<?>> enumClass();

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean ignoreCase() default false;

	boolean nullable() default false;
}
