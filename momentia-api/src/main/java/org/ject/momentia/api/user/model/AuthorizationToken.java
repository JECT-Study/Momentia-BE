package org.ject.momentia.api.user.model;

import jakarta.validation.constraints.NotEmpty;

public record AuthorizationToken(
	@NotEmpty String accessToken,
	@NotEmpty String refreshToken
) {
}
