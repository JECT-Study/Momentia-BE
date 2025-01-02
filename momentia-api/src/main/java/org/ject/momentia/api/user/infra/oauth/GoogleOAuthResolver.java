package org.ject.momentia.api.user.infra.oauth;

import org.ject.momentia.api.apiclient.GoogleClient;
import org.ject.momentia.api.user.model.OAuthUserInfo;
import org.ject.momentia.common.domain.user.type.OAuthProvider;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleOAuthResolver implements AbstractOAuthResolver {
	private final GoogleClient googleClient;

	@Override
	public OAuthUserInfo resolve(String code) {
		var googleToken = googleClient.requestToken(code).accessToken();
		var googleUserInfo = googleClient.requestUser(googleToken);

		return new OAuthUserInfo(OAuthProvider.GOOGLE, googleUserInfo.id(), googleUserInfo.name());
	}
}
