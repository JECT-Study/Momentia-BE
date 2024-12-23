package org.ject.momentia.api.apiclient;

import java.util.Map;

import org.ject.momentia.api.apiclient.model.KakaoToken;
import org.ject.momentia.api.apiclient.model.KakaoUserInfo;
import org.ject.momentia.common.util.MomentiaStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KakaoClient {
	private static final String AUTHORIZATION_HEADER_FORMAT = "Bearer {}";
	private final RestClient restClient = RestClient.create("https://kapi.kakao.com");

	@Value(("${api-client.kakao.client-id}"))
	private String clientId;
	@Value(("${api-client.kakao.redirect-uri}"))
	private String redirectUri;

	/**
	 * 카카오 token 요청
	 * @param code
	 * @return
	 */
	public KakaoToken requestToken(String code) {
		var requestBody = Map.of(
			"grant_type", "authorization_code",
			"client_id", clientId,
			"redirect_uri", redirectUri,
			"code", code
		);

		return restClient.post()
			.uri("/oauth/token")
			.body(requestBody)
			.retrieve()
			.body(KakaoToken.class);
	}

	/**
	 * 카카오 유저 정보 조회
	 * @param kakaoAccessToken
	 * @see <a href=https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info>Kakao docs</a>
	 * @return
	 */
	public KakaoUserInfo requestUser(String kakaoAccessToken) {
		return restClient.post()
			.uri("/v2/user/me")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.header("Authorization", MomentiaStringUtils.format(AUTHORIZATION_HEADER_FORMAT, kakaoAccessToken))
			.body("property_keys=[\"kakao_account.profile\"]")
			.retrieve()
			.body(KakaoUserInfo.class);
	}
}
