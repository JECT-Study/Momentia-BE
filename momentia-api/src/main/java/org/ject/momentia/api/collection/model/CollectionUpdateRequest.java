package org.ject.momentia.api.collection.model;

import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.api.mvc.annotation.NullableButNotBlank;
import org.ject.momentia.common.domain.collection.type.CollectionStatus;

import jakarta.validation.constraints.Size;

public record CollectionUpdateRequest(

	@NullableButNotBlank(message = "컬렉션 이름은 공백을 허용하지 않습니다.")
	@Size(min = 1, max = 10, message = "컬렉션 이름은 1자 이상 10자 이하여야합니다.")
	String name,

	@EnumValue(enumClass = CollectionStatus.class, nullable = true, message = "컬렉션 status 값이 잘못되었습니다.(PUBLIC or PRIVATE)")
	String status

) {
}
