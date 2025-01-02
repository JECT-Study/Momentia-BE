package org.ject.momentia.api.apiclient.model;

import jakarta.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleUserInfo(
	@JsonProperty("id") String id,
	@JsonProperty("name") @Nullable String name
) {
}
