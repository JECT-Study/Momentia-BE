package org.ject.momentia.api.artwork.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtworkCommentRequest(

	@NotBlank(message = "댓글은 최소 1자 이상의 문자를 포함해야 하며, 공백만으로 구성된 내용은 입력할 수 없습니다.")
	String content
) {
}
