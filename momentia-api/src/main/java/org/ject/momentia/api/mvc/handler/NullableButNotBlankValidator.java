package org.ject.momentia.api.mvc.handler;

import org.ject.momentia.api.mvc.annotation.NullableButNotBlank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullableButNotBlankValidator implements ConstraintValidator<NullableButNotBlank, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || !value.trim().isEmpty();
	}
}
