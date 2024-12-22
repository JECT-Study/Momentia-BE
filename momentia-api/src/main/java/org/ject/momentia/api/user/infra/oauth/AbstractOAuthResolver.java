package org.ject.momentia.api.user.infra.oauth;

import org.ject.momentia.api.user.model.OAuthUserInfo;

public interface AbstractOAuthResolver {
	OAuthUserInfo resolve(String code);
}
