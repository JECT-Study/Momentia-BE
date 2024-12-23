package org.ject.momentia.api.user.converter;

import org.ject.momentia.api.user.model.NormalRegisterRequest;
import org.ject.momentia.common.domain.user.User;
import org.ject.momentia.common.domain.user.type.AccountStatus;
import org.ject.momentia.common.domain.user.type.AccountType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {
	public static User registerRequestOf(NormalRegisterRequest registerRequest) {
		return User.builder()
			.email(registerRequest.email())
			.nickname(registerRequest.nickname())
			.followerCount(0)
			.followingCount(0)
			.accountType(AccountType.NORMAL)
			.accountStatus(AccountStatus.REGISTERED) // TODO: 이메일 기능 추가 후 PENDING
			.build();
	}

	public static User nicknameOf(String nickname) {
		return User.builder()
			.nickname(nickname)
			.followerCount(0)
			.followingCount(0)
			.accountType(AccountType.SOCIAL)
			.accountStatus(AccountStatus.REGISTERED)
			.build();
	}
}
