package org.ject.momentia.api.user.model;

import org.ject.momentia.common.util.CommonConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record NormalLoginRequest(
	@Email String email,
	@Pattern(regexp = CommonConstants.PASSWORD_FORMAT) String password
) {
}
