package org.ject.momentia.api.config;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

	// 쿠키 값 가져오기
	public static Optional<String> getCookieValue(HttpServletRequest request, String name) {
		if (request.getCookies() != null) {
			return Arrays.stream(request.getCookies())
				.filter(cookie -> name.equals(cookie.getName()))
				.map(Cookie::getValue)
				.findFirst();
		}
		return Optional.empty();
	}

	// 쿠키 추가하기
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");  // 모든 경로에서 접근 가능하도록 설정
		cookie.setHttpOnly(true); // JavaScript에서 접근 불가능하도록 보안 설정
		cookie.setMaxAge(maxAge); // 쿠키 만료 시간 설정 (초 단위)
		response.addCookie(cookie);
	}
}
