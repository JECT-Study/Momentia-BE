package org.ject.momentia.common.exception;

import org.ject.momentia.common.util.MomentiaStringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 외부 API 호출 시 발생하는 에러를 holding 하는 exception
 */
public class HttpClientException extends RuntimeException {
	private final @NotNull String invokerName;
	private final @NotNull HttpMethod method;
	private final @NotNull String url;
	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String errorMessage;
	private final String debugMessage;
	private final Object params;

	public HttpClientException(@NotNull String invokerName, @NotNull HttpMethod method, @NotEmpty String url,
		Object params, String debugMessage, @NotNull Throwable cause) {
		super(makeMessage(invokerName, method, url, params, null, null, null, debugMessage), cause);
		this.invokerName = invokerName;
		this.method = method;
		this.url = url;
		this.params = params;
		this.httpStatus = null;
		this.errorCode = null;
		this.errorMessage = null;
		this.debugMessage = debugMessage;
	}

	public HttpClientException(@NotNull String invokerName, @NotNull HttpMethod method, @NotEmpty String url,
		Object params, @NotNull HttpStatus httpStatus) {
		this(invokerName, method, url, params, httpStatus, null, null, null);
	}

	public HttpClientException(@NotNull String invokerName, @NotNull HttpMethod method, @NotEmpty String url,
		Object params, @NotNull HttpStatus httpStatus, String debugMessage) {
		this(invokerName, method, url, params, httpStatus, null, null, debugMessage);
	}

	public HttpClientException(@NotNull String invokerName, @NotNull HttpMethod method, @NotEmpty String url,
		Object params, @NotNull HttpStatus httpStatus, String errorCode, String errorMessage) {
		this(invokerName, method, url, params, httpStatus, errorCode, errorMessage, null);
	}

	public HttpClientException(@NotNull String invokerName, @NotNull HttpMethod method, @NotEmpty String url,
		Object params, @NotNull HttpStatus httpStatus, String errorCode, String errorMessage,
		String debugMessage) {
		super(makeMessage(invokerName, method, url, params, httpStatus, errorCode, errorMessage, debugMessage));
		this.invokerName = invokerName;
		this.method = method;
		this.url = url;
		this.params = params;
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.debugMessage = debugMessage;
	}

	public HttpClientException(@NotNull String invokerName, @NotNull HttpMethod method, @NotNull String url,
		Object params, @NotNull HttpStatus httpStatus, @NotNull Throwable cause) {
		super(makeMessage(invokerName, method, url, params, httpStatus, null, null, null), cause);
		this.invokerName = invokerName;
		this.method = method;
		this.url = url;
		this.params = params;
		this.httpStatus = httpStatus;
		this.errorCode = null;
		this.errorMessage = null;
		this.debugMessage = null;
	}

	private static String makeMessage(String invokerName, HttpMethod method, String url, Object params,
		HttpStatus httpStatus, String errorCode, String errorMessage, String debugMessage) {
		return MomentiaStringUtils.format(
			"[{}] - {} {}, params:{}, httpStatus:{}, errorCode:{}, errorMessage:{}, debugMessage:{}", invokerName,
			method, url, MomentiaStringUtils.reflectionToString(params), httpStatus, errorCode, errorMessage,
			debugMessage);
	}
}