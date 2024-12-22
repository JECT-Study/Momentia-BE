package org.ject.momentia.api.apiclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoUserInfo(
	@JsonProperty("id") Long id,
	@JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
	public String nickname() {
		return this.kakaoAccount.profile.nickname;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record KakaoAccount(
		@JsonProperty("profile") KakaoProfile profile
	) {

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record KakaoProfile(
		@JsonProperty("nickname") String nickname
	) {

	}
}
