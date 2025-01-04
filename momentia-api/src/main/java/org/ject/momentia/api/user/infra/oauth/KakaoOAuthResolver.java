package org.ject.momentia.api.user.infra.oauth;

import org.ject.momentia.api.apiclient.KakaoClient;
import org.ject.momentia.api.user.model.OAuthUserInfo;
import org.ject.momentia.common.domain.user.type.OAuthProvider;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuthResolver implements AbstractOAuthResolver {
	private final KakaoClient kakaoClient;

	@Override
	public OAuthUserInfo resolve(String code) {
		log.debug("kakao code {}", code);
		var kakaoToken = kakaoClient.requestToken(code).accessToken();
		log.debug("kakao token: {}", kakaoToken);
		var kakaoUserInfo = kakaoClient.requestUser(kakaoToken);

		return new OAuthUserInfo(OAuthProvider.KAKAO, kakaoUserInfo.id().toString(), kakaoUserInfo.nickname());
	}
}
