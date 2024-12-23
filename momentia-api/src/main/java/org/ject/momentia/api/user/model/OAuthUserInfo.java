package org.ject.momentia.api.user.model;

import org.ject.momentia.common.domain.user.type.OAuthProvider;

public record OAuthUserInfo(
	OAuthProvider provider,
	String userId,
	String userNickname
) {
}
