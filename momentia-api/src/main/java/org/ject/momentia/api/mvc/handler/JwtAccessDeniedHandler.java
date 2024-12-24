package org.ject.momentia.api.mvc.handler;

import java.io.IOException;

import org.ject.momentia.api.exception.ErrorCd;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAccessDeniedHandler implements AuthenticationEntryPoint {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException)
		throws IOException
	{
		res.setContentType("application/json; charset=UTF-8");
		res.setStatus(403);

		var response = new ExceptionHandlerAdvice.Response(
			ErrorCd.NOT_AUTHORIZED.name(),
			ErrorCd.NOT_AUTHORIZED.getErrorMessage()
		);

		MAPPER.writeValue(res.getOutputStream(), response);
	}
}
