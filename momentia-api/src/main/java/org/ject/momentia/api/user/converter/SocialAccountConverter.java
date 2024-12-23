package org.ject.momentia.api.user.converter;

import org.ject.momentia.api.user.model.OAuthUserInfo;
import org.ject.momentia.common.domain.user.SocialAccount;
import org.ject.momentia.common.domain.user.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialAccountConverter {
	public static SocialAccount oAuthUserInfoOf(User user, OAuthUserInfo oAuthUserInfo) {
		return new SocialAccount(user.getId(), user, oAuthUserInfo.userId(), oAuthUserInfo.provider());
	}
}
