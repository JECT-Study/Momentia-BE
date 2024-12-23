package org.ject.momentia.api.user.infra.oauth;

import org.ject.momentia.api.apiclient.KakaoClient;
import org.ject.momentia.api.user.model.OAuthUserInfo;
import org.ject.momentia.common.domain.user.type.OAuthProvider;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoOAuthResolver implements AbstractOAuthResolver {
	private final KakaoClient kakaoClient;

	@Override
	public OAuthUserInfo resolve(String code) {
		var kakaoToken = kakaoClient.requestToken(code).accessToken();
		var kakaoUserInfo = kakaoClient.requestUser(kakaoToken);

		return new OAuthUserInfo(OAuthProvider.KAKAO, kakaoUserInfo.id().toString(), kakaoUserInfo.nickname());
	}
}
