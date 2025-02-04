package org.ject.momentia.api.follow.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record FollowInfo(
	@Positive Long userId,
	String profileImage,
	@NotEmpty String nickname,
	String introduction,
	Boolean isFollow
) {
}
