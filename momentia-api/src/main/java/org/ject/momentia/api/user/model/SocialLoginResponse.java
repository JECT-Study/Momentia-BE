package org.ject.momentia.api.user.model;

import jakarta.validation.Valid;

public record SocialLoginResponse(
	boolean isRegistered,
	@Valid AuthorizationToken token
) {
}
