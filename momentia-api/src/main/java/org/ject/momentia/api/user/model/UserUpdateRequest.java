package org.ject.momentia.api.user.model;

import org.ject.momentia.common.domain.user.type.FieldType;

import jakarta.validation.constraints.Positive;

public record UserUpdateRequest(
	FieldType field,
	String introduction,
	String nickname,
	@Positive Long profileImage
) {
}
