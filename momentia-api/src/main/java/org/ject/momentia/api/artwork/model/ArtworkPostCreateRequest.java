package org.ject.momentia.api.artwork.model;

import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.common.domain.artwork.type.Category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArtworkPostCreateRequest(
	@NotEmpty
	@Size(min = 1, max = 50, message = "제목은 1자 이상 50자 이하여야 합니다.")
	String title,

	// category 검증 어노테이션
	@NotEmpty
	@EnumValue(enumClass = Category.class, message = "카테고리 값이 잘못되었습니다.")
	String artworkField,

	@NotNull(message = "이미지 ID(정수) 값은 필수입니다.")
	Long postImage,

	@Size(max = 1000, message = "작품 설명은 1000자 이하여야 합니다.")
	String explanation,

	@NotEmpty(message = "상태 값은 'PUBLIC' or 'PRIVATE' 이여야합니다.")
	String status
) {
}

