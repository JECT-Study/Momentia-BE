package org.ject.momentia.api.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NormalLoginRequest(
	@Email String email,
	@NotEmpty @Size(min = 6) String password
) {
}
