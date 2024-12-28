package org.ject.momentia.api.artwork.model;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ArtworkPostResponse(
        Long postId,
        String title,
        String postImage,
        String category,
        Long view,
        Long likeCount,
        LocalDateTime createdTime,
        String explanation,
        boolean isLiked, // 좋아요 여부
        Long userId, // 작가 id -> 프로필 정보 api 이용
        boolean isMine //  본인 작품인지 여부
) {
}
