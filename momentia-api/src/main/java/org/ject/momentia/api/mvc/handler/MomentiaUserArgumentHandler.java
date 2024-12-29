package org.ject.momentia.api.mvc.handler;

import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.api.user.infra.JwtTokenProvider;
import org.ject.momentia.api.user.repository.UserRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MomentiaUserArgumentHandler implements HandlerMethodArgumentResolver {
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(MomentiaUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		var authentication = getAuthentication();

		// 비회원 처리
		if (authentication == null) {
			return null;
		}

		var accessToken = authentication.getCredentials().toString();

		var userId = jwtTokenProvider.parseAccessToken(accessToken);

		return userRepository.findById(userId)
			.orElseThrow(ErrorCd.USER_NOT_FOUND::serviceException);
	}

	private Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
			return null;
		}
		return authentication;
	}
}
