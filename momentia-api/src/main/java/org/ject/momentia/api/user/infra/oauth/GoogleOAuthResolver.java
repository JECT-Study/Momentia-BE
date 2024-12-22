package org.ject.momentia.api.user.infra.oauth;

import org.ject.momentia.api.apiclient.GoogleClient;
import org.ject.momentia.api.user.model.OAuthUserInfo;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleOAuthResolver implements AbstractOAuthResolver {
	private final GoogleClient googleClient;

	@Override
	public OAuthUserInfo resolve(String code) {
		return null;
	}
}
