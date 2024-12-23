package org.ject.momentia.api.user.converter;

import org.ject.momentia.api.user.model.NormalRegisterRequest;
import org.ject.momentia.common.domain.user.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {
	public static User registerRequestOf(NormalRegisterRequest registerRequest) {
		return User.builder()
			.email(registerRequest.email())
			.nickname(registerRequest.nickname())
			.build();
	}

	public static User nicknameOf(String nickname) {
		return User.builder()
			.nickname(nickname)
			.build();
	}
}
