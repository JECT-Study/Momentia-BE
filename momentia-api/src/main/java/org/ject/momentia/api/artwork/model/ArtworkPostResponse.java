package org.ject.momentia.api.artwork.model;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ArtworkPostResponse(
	Long postId,
	String title,
	String postImage,
	String artworkField,
	Long viewCount,
	Long likeCount,
	Long commentCount,
	LocalDateTime createdTime,
	String explanation,
	boolean isLiked, // 좋아요 여부
	Long userId,
	String profileImage,
	String nickname,
	String userField,
	boolean isFollow,
	String introduction,
	boolean isMine //  본인 작품인지 여부
) {
}
