package org.ject.momentia.api.apiclient;

import java.util.Map;

import org.ject.momentia.api.apiclient.model.GoogleUserInfo;
import org.ject.momentia.api.apiclient.model.OAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GoogleClient {
	private final RestClient authClient = RestClient.create("https://oauth2.googleapis.com");
	private final RestClient restClient = RestClient.create("https://www.googleapis.com/oauth2");

	@Value("${api-client.google.client-id}")
	private String clientId;
	@Value("${api-client.google.client-secret}")
	private String clientSecret;
	@Value("${api-client.google.redirect-uri}")
	private String redirectUri;

	public OAuthToken requestToken(String code) {
		var requestBody = new LinkedMultiValueMap<String, String>();

		requestBody.add("grant_type", "authorization_code");
		requestBody.add("code", code);
		requestBody.add("client_id", clientId);
		requestBody.add("client_secret", clientSecret);
		requestBody.add("redirect_uri", redirectUri);

		return authClient.post()
			.uri("/token")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(requestBody)
			.retrieve()
			.body(OAuthToken.class);
	}

	public GoogleUserInfo requestUser(String accessToken) {
		var uriVariables = Map.of("accessToken", accessToken);

		return restClient.get()
			.uri("/v2/userinfo?access_token={accessToken}", uriVariables)
			.retrieve()
			.body(GoogleUserInfo.class);
	}
}
