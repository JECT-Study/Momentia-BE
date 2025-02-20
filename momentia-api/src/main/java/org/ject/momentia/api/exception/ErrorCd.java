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
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Token. Please request a new token."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

	// User Error
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
	INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "Invalid Nickname"),
	DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "Duplicate Nickname"),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Duplicate Email"),

	// Follow Error
	ALREADY_FOLLOW(HttpStatus.CONFLICT, "Already Follow"),
	CANNOT_FOLLOW_SELF(HttpStatus.CONFLICT, "Cannot Follow Self"),
	FOLLOW_NOT_FOUND(HttpStatus.CONFLICT, "follow not found"),

	// Artwork Error
	ARTWORK_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "artwork post not found"),
	ARTWORK_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "artwork comment not found"),

	ALREADY_lIKE(HttpStatus.CONFLICT, "already like"),
	LIKE_ALREADY_REMOVED(HttpStatus.CONFLICT, "already removed or not exist"),

	// collection Error
	DUPLICATE_COLLECTION_NAME(HttpStatus.CONFLICT, "duplicate collection name"),
	COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "collection not found"),
	DUPLICATE_COLLECTION_ARTWORK(HttpStatus.CONFLICT, "duplicate artwork"),
	COLLECTION_ARTWORK_ALREADY_REMOVED(HttpStatus.CONFLICT, "already removed or not exist"),

	// Image Error
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "image not found"),
	IMAGE_ALREADY_PROCESSED(HttpStatus.CONFLICT, "Image already processed"),
	IMAGE_NOT_UPLOADED(HttpStatus.BAD_REQUEST, "image not uploaded");

	private final HttpStatus httpStatus;
	private final String errorMessage;

	public ServiceException serviceException() {
		throw new ServiceException(this.name(), this.errorMessage);
	}

	public ServiceException serviceException(String debugMessage) {
		throw new ServiceException(this.name(), this.errorMessage, debugMessage);
	}
}

