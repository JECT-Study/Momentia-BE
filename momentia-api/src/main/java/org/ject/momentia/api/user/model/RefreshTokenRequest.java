package org.ject.momentia.api.user.model;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequest(
	@NotEmpty String refreshToken
) {
}
