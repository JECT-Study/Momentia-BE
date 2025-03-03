package org.ject.momentia.api.artwork.model.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ArtworkFollowingUserPostModel(
	Long postId,
	String postImage,
	String title,
	Long likeCount,
	Long viewCount,
	Long commentCount,
	Boolean isLiked,
	LocalDateTime createdTime
) {
}
