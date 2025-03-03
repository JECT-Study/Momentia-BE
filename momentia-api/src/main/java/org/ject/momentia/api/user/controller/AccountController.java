package org.ject.momentia.api.user.controller;

import org.ject.momentia.api.user.model.AuthorizationToken;
import org.ject.momentia.api.user.model.NormalLoginRequest;
import org.ject.momentia.api.user.model.NormalRegisterRequest;
import org.ject.momentia.api.user.model.RefreshTokenRequest;
import org.ject.momentia.api.user.model.SocialLoginResponse;
import org.ject.momentia.api.user.service.AccountService;
import org.ject.momentia.common.domain.user.type.OAuthProvider;
import org.ject.momentia.common.util.CommonConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * 회원 관련 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class AccountController {
	private final AccountService accountService;

	@GetMapping("/validation/email")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void validateEmail(@RequestParam @Email String email) {
		accountService.validateEmail(email);
	}

	@GetMapping("/validation/nickname")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void validateNickname(@RequestParam @Pattern(regexp = CommonConstants.NICKNAME_FORMAT) String nickname) {
		accountService.validateNickname(nickname);
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthorizationToken register(@RequestBody @Valid NormalRegisterRequest registerRequest) {
		return accountService.register(registerRequest);
	}

	@GetMapping("/login/social/{provider}")
	@ResponseStatus(HttpStatus.OK)
	public SocialLoginResponse socialLogin(
		@PathVariable OAuthProvider provider,
		@RequestParam @NotEmpty String code
	) {
		return accountService.socialLogin(provider, code);
	}

	@PostMapping("/login/normal")
	@ResponseStatus(HttpStatus.OK)
	public AuthorizationToken normalLogin(@RequestBody @Valid NormalLoginRequest normalLoginRequest) {
		return accountService.normalLogin(normalLoginRequest);
	}

	@PostMapping("/login/normal/test")
	@ResponseStatus(HttpStatus.OK)
	public AuthorizationToken normalLoginForTest(@RequestBody @Valid NormalLoginRequest normalLoginRequest) {
		return accountService.normalLoginTest(normalLoginRequest);
	}

	@PostMapping("/refresh")
	public AuthorizationToken refresh(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
		return accountService.refreshToken(refreshTokenRequest);
	}
}
