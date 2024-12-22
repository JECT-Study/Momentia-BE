package org.ject.momentia.api.user.service;

import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.user.converter.NormalAccountConverter;
import org.ject.momentia.api.user.converter.UserConverter;
import org.ject.momentia.api.user.infra.JwtTokenProvider;
import org.ject.momentia.api.user.infra.RefreshTokenHolder;
import org.ject.momentia.api.user.infra.oauth.OAuthResolver;
import org.ject.momentia.api.user.model.AuthorizationToken;
import org.ject.momentia.api.user.model.NormalLoginRequest;
import org.ject.momentia.api.user.model.NormalRegisterRequest;
import org.ject.momentia.api.user.model.RefreshTokenRequest;
import org.ject.momentia.api.user.model.SocialLoginResponse;
import org.ject.momentia.common.domain.user.type.OAuthProvider;
import org.ject.momentia.api.user.repository.NormalAccountRepository;
import org.ject.momentia.api.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final UserRepository userRepository;
	private final NormalAccountRepository normalAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final OAuthResolver oauthResolver;

	/**
	 * 이메일을 검증한다.
	 * input validation 을 통해 이메일 형식은 검증되었으므로, 존재 여부만 검증한다.
	 * @param email
	 */
	public void validateEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw ErrorCd.DUPLICATE_EMAIL.serviceException();
		}
	}

	/**
	 * 닉네임을 검증한다.
	 * input validation 을 통해 닉네임 형식은 검증되었으므로, 존재 여부만 검증한다.
	 * @param nickname
	 */
	public void validateNickname(String nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw ErrorCd.DUPLICATE_NICKNAME.serviceException();
		}

		// TODO: 금칙어 설정
	}

	@Transactional
	public AuthorizationToken register(NormalRegisterRequest registerRequest) {
		validateEmail(registerRequest.email());
		validateNickname(registerRequest.nickname());

		var encodedPassword = passwordEncoder.encode(registerRequest.password());
		var user = UserConverter.registerRequestOf(registerRequest);

		var insertedUser = userRepository.save(user);
		var normalAccountInfo = NormalAccountConverter.userOf(insertedUser, encodedPassword);
		normalAccountRepository.save(normalAccountInfo);

		return jwtTokenProvider.createTokenInfo(user);
	}

	public SocialLoginResponse socialLogin(OAuthProvider provider, String code) {
		var result = oauthResolver.resolve(provider, code);
		return new SocialLoginResponse(result.getRight(), jwtTokenProvider.createTokenInfo(result.getLeft()));
	}

	public AuthorizationToken normalLogin(NormalLoginRequest normalLoginRequest) {
		var password = normalLoginRequest.password();

		var encodedPassword = passwordEncoder.encode(password);
		var user = userRepository.findByEmail(normalLoginRequest.email())
			.orElseThrow(ErrorCd.NOT_AUTHORIZED::serviceException);

		if (!normalAccountRepository.existsByIdAndPassword(user, encodedPassword)) {
			throw ErrorCd.NOT_AUTHORIZED.serviceException();
		}

		return jwtTokenProvider.createTokenInfo(user);
	}

	public AuthorizationToken refreshToken(RefreshTokenRequest refreshTokenRequest) {
		var refreshToken = refreshTokenRequest.refreshToken();
		validateRefreshToken(refreshToken);

		return jwtTokenProvider.createTokenInfo(refreshToken);
	}

	private void validateRefreshToken(String refreshToken) {
		if (!jwtTokenProvider.validateToken(refreshToken) || RefreshTokenHolder.getRefreshToken(refreshToken) == null) {
			throw ErrorCd.NOT_AUTHORIZED.serviceException();
		}
	}
}
