package org.ject.momentia.api.exception;

import org.ject.momentia.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCd {
	// Common Error
	NO_HANDLER_FOUND(HttpStatus.NOT_FOUND, "no handler found"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "method not allowed"),
	NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "not acceptable"),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "unsupported media type"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter"),
	NO_PERMISSION(HttpStatus.FORBIDDEN, "no permission"),
	NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "not authorized"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

	// User Error
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
	INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "Invalid Nickname"),
	DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "Duplicate Nickname"),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Duplicate Email");

	private final HttpStatus httpStatus;
	private final String errorMessage;

	public ServiceException serviceException() {
		throw new ServiceException(this.httpStatus.name(), this.errorMessage);
	}

	public ServiceException serviceException(String debugMessage) {
		throw new ServiceException(this.httpStatus.name(), this.errorMessage, debugMessage);
	}
}

