package org.ject.momentia.api.user.infra.oauth;

import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.ject.momentia.api.user.converter.SocialAccountConverter;
import org.ject.momentia.api.user.converter.UserConverter;
import org.ject.momentia.common.domain.user.type.OAuthProvider;
import org.ject.momentia.api.user.repository.SocialAccountRepository;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.user.User;
import org.ject.momentia.common.util.MomentiaStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthResolver {
	private static final String OAUTH_NICKNAME_FORMAT = "{}_{}";
	private static final int MIN_RANDOM_NUMBER = 1;
	private static final int MAX_RANDOM_NUMBER = 1_000_000;

	private final AbstractOAuthResolver googleOAuthResolver;
	private final AbstractOAuthResolver kakaoOAuthResolver;
	private final SocialAccountRepository socialAccountRepository;
	private final UserRepository userRepository;

	private final SecureRandom secureRandom = new SecureRandom();

	@Transactional
	public Pair<User, Boolean> resolve(OAuthProvider provider, String code) {
		var oauthUserInfo = switch (provider) {
			case KAKAO -> kakaoOAuthResolver.resolve(code);
			case GOOGLE -> googleOAuthResolver.resolve(code);
		};

		// TODO: 현재 논의되지 않은 사항 - 일반 가입을 한 유저가 OAuth 로 재 로그인 시도 시 처리 방안
		var socialAccountOptional = socialAccountRepository.findBySocialIdAndSocialType(oauthUserInfo.userId(),
			oauthUserInfo.provider());

		if (socialAccountOptional.isPresent()) {
			var socialAccount = socialAccountOptional.get();
			return Pair.of(socialAccount.getUser(), true);
		}

		var nickname = getValidNickname(oauthUserInfo.userNickname());
		var user = userRepository.saveAndFlush(UserConverter.nicknameOf(nickname));
		socialAccountRepository.save(SocialAccountConverter.oAuthUserInfoOf(user, oauthUserInfo));

		return Pair.of(user, false);
	}

	public String getValidNickname(String nickname) {
		while (true) {
			var index = secureRandom.nextInt(MIN_RANDOM_NUMBER, MAX_RANDOM_NUMBER);
			var paddedIndex = StringUtils.leftPad(String.valueOf(index), 6, '0');
			var generatedNickname = MomentiaStringUtils.format(OAUTH_NICKNAME_FORMAT, nickname, paddedIndex);

			if (!userRepository.existsByNickname(generatedNickname)) {
				return generatedNickname;
			}
		}
	}
}
