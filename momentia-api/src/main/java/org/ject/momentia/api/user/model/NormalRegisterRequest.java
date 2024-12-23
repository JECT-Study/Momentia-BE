package org.ject.momentia.api.user.model;

import org.ject.momentia.common.util.CommonConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 *
 * @param email
 * @param password
 * @param nickname
 */
public record NormalRegisterRequest(
	@Email String email,
	@NotEmpty String password,
	@Pattern(regexp = CommonConstants.NICKNAME_FORMAT) String nickname
) {
}
