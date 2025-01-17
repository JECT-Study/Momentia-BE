package org.ject.momentia.api.artwork.model;

import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.api.mvc.annotation.NullableButNotBlank;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;

import jakarta.validation.constraints.Size;

public record ArtworkPostUpdateRequest(

	@NullableButNotBlank(message = "제목은 빈 문자열을 허용하지 않습니다.")
	@Size(min = 1, max = 50, message = "제목은 1자 이상 50자 미만이여야 합니다.")
	String title,

	@Size(max = 1000, message = "작품 설명은 1000자 이하여야 합니다.")
	String explanation,

	@EnumValue(enumClass = Category.class, nullable = true, message = "작품 카테고리 값이 잘못되었습니다.")
	String artworkField,

	@EnumValue(enumClass = ArtworkPostStatus.class, ignoreCase = true, nullable = true, message = "상태 값은 PUBLIC,PRIVATE 값이여야 합니다.")
	String status
) {
}
