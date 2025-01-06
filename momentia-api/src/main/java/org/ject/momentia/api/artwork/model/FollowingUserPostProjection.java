package org.ject.momentia.api.artwork.model;

import java.time.LocalDateTime;

public interface FollowingUserPostProjection {
    Long getId();
    String getStatus();
    String getTitle();
//    String getExplanation();
    Long getViewCount();
    Long getLikeCount();
    Long getCommentCount();
    Long getUserId();
//    String getCategory();
//    Long getNum();
    LocalDateTime getCreatedAt();
//    LocalDateTime getUpdatedAt();
}
