package org.ject.momentia.api.artwork.model.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ArtworkCommentModel(
	Long commentId,
	Long userId,
	String nickname,
	String profileImage,
	String content,
	LocalDateTime createdTime,
	boolean isMine
) {
}
