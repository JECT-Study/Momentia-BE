package org.ject.momentia.api.artwork.model;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ArtworkFollowingUserPostModel (
        Long postId,
        String postImage,
        String title,
        Long likeCount,
        Long viewCount,
        Long commentCount,
        Boolean isLiked,
        LocalDateTime createdTime
){
}
