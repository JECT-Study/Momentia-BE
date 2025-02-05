package org.ject.momentia.api.artwork.model;

import lombok.Builder;

@Builder
public record ArtworkPostModel(
	Long postId,
	String title,
	String postImage,
	Long userId,
	String nickname,
	Long viewCount,
	Long likeCount,
	Long commentCount,
	boolean isLiked,
	String status
) {

}
