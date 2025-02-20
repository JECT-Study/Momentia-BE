package org.ject.momentia.api.mvc.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ject.momentia.api.config.SecurityConstants;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.exception.JwtTokenAuthenticationException;
import org.ject.momentia.api.user.infra.JwtTokenProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private final JwtTokenProvider jwtUtil;
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
		var path = req.getRequestURI();
		var shouldSkip = SecurityConstants.ALLOW_URLS.stream()
			.anyMatch(pattern -> antPathMatcher.match(pattern, path));

		log.debug("Should skip JwtFilter for path {}: {}", path, shouldSkip);
		return shouldSkip;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		if (shouldNotFilter(request)) {
			return;
		}

		String accessToken = resolveAccessToken(request);

		try {
			if (accessToken != null) {
				if (jwtUtil.validateToken(accessToken)) {
					SecurityContextHolder.getContext()
						.setAuthentication(jwtUtil.getAuthentication(accessToken));
				} else {
					log.debug("invalid accessToken: {}", accessToken);
					// 액세스 토큰을 담아 요청 보냈지만 유효하지 않은 액세스 토큰일 경우, 무조건 에러 코드 반환
					sendErrorResponse(response, ErrorCd.INVALID_TOKEN);
					return;
					//throw new JwtTokenAuthenticationException("invalid accessToken");
				}
			} else {
				throw new JwtTokenAuthenticationException("Access Token not exist");
			}
		} catch (JwtTokenAuthenticationException e) {
			request.setAttribute("exception", e);
		}

		filterChain.doFilter(request, response);
	}

	private String resolveAccessToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");

		if (authorization != null && authorization.startsWith("Bearer ")) {
			return authorization.split(" ")[1];
		}

		return null;
	}

	private void sendErrorResponse(HttpServletResponse response, ErrorCd errorCd) throws IOException {
		response.setStatus(errorCd.getHttpStatus().value());  // Set status from ErrorCd
		response.setContentType("application/json; charset=UTF-8");

		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("code", errorCd.name());
		errorResponse.put("message", errorCd.getErrorMessage());

		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}

