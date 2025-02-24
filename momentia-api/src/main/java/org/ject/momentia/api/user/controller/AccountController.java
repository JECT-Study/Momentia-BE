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

	/**
	 * 이메일 중복 검사
	 * @param email 중복 검사를 위한 이메일
	 * @see <a href="https://www.notion.so/dbff3ea0811a42eebfda43c5462ee2ba?pvs=4">API Docs</a>
	 */
	@GetMapping("/validation/email")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void validateEmail(@RequestParam @Email String email) {
		accountService.validateEmail(email);
	}

	/**
	 * 닉네임 중복 검사
	 * @param nickname 중복 검사를 위한 닉네임
	 * @see <a href="https://www.notion.so/0b7f02929b6b43dd8b828d63a81a973f?pvs=4">API Docs</a>
	 */
	@GetMapping("/validation/nickname")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void validateNickname(@RequestParam @Pattern(regexp = CommonConstants.NICKNAME_FORMAT) String nickname) {
		accountService.validateNickname(nickname);
	}

	/**
	 * 회원 가입 (일반 회원가입)
	 * @param registerRequest 회원 가입 요청 정보
	 * @return 인증 토큰
	 * @see <a href="https://www.notion.so/95ec01ef32cd44f5bf93c3830f05ad96?pvs=4">API Docs</a>
	 */
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthorizationToken register(@RequestBody @Valid NormalRegisterRequest registerRequest) {
		return accountService.register(registerRequest);
	}

	/**
	 * 소셜 로그인
	 * @param provider OAuthProvider
	 * @param code    OAuthProvider 가 제공한 인증 코드
	 * @return 최초 로그인 여부 및 인증 Token 정보
	 */
	@GetMapping("/login/social/{provider}")
	@ResponseStatus(HttpStatus.OK)
	public SocialLoginResponse socialLogin(
		@PathVariable OAuthProvider provider,
		@RequestParam @NotEmpty String code
	) {
		return accountService.socialLogin(provider, code);
	}

	/**
	 * 일반 로그인
	 * @param normalLoginRequest 일반 로그인 요청 정보
	 * @return 인증 Token 정보
	 */
	@PostMapping("/login/normal")
	@ResponseStatus(HttpStatus.OK)
	public AuthorizationToken normalLogin(@RequestBody @Valid NormalLoginRequest normalLoginRequest) {
		return accountService.normalLogin(normalLoginRequest);
	}

	/**
	 * AccessToken 갱신
	 * @param refreshTokenRequest 갱신을 위한 RefreshToken
	 * @return 갱신된 Token 정보
	 * @see <a href="https://www.notion.so/e3139fd48c514ffdb0736f919bfcb4ff?pvs=4">API Docs</a>
	 */
	@PostMapping("/refresh")
	public AuthorizationToken refresh(@Valid RefreshTokenRequest refreshTokenRequest) {
		return accountService.refreshToken(refreshTokenRequest);
	}
}
