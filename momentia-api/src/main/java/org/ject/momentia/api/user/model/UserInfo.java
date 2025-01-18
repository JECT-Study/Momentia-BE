package org.ject.momentia.api.user.model;

import org.ject.momentia.common.domain.user.type.FieldType;

import jakarta.validation.constraints.Email;
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
	@Email String email,
	@PositiveOrZero int followerCount,
	@PositiveOrZero int followingCount,
	@NotNull FieldType field,
	String introduction,
	boolean isFollow
) {

}
