package org.ject.momentia.api.collection.model;

import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.common.domain.collection.type.CollectionStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CollectionCreateRequest(

	@NotBlank(message = "컬렉션 이름은 null이나 공백을 허용하지 않습니다.")
	@Size(min = 1, max = 10, message = "컬렉션 이름은 1자 이상 10자 이하여야합니다.")
	String name,

	@EnumValue(enumClass = CollectionStatus.class, message = "컬렉션 status 값이 잘못되었습니다.(PUBLIC or PRIVATE)")
	String status
) {
}
