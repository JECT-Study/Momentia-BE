package org.ject.momentia.api.artwork.model;

import java.time.LocalDateTime;

public interface FollowingUserPostProjection {
	Long getId();

	String getStatus();

	String getTitle();

	Long getViewCount();

	Long getLikeCount();

	Long getCommentCount();

	Long getUserId();

	LocalDateTime getCreatedAt();
}
