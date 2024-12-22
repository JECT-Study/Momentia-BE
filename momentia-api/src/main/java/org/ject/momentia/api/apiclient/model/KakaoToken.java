package org.ject.momentia.api.apiclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * kakao 토큰 받기 기능을 통해 획득한 accessToken 정보
 * @param tokenType
 * @param accessToken
 * @see <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token-response-body">Kakao Docs</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoToken(
	@JsonProperty("token_type") String tokenType,
	@JsonProperty("access_token") String accessToken
) {
}
