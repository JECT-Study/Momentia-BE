package org.ject.momentia.api.user.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record UserInfo(
	boolean isMine,
	@Positive long userId,
	String profileImage,
	@NotEmpty String nickname,
	@PositiveOrZero int followerCount,
	@PositiveOrZero int followingCount,
	@NotNull String userField,
	String introduction,
	Boolean isFollow
) {

}
