package org.ject.momentia.api.artwork.model;

import lombok.Builder;

@Builder
public record ArtworkPostModel(
        Long postId,
        String title,
        String postImage,
        Long userId,
        String nickname,
        Long view,
        Long likeCount,
        Long commentCount,
        boolean isLiked // 좋아요 여부
) {

}
