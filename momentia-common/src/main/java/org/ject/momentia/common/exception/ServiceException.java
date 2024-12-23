package org.ject.momentia.common.exception;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

/**
 * 서비스의 비즈니스 에러를 holding 하는 exception
 */
@Getter
public class ServiceException extends RuntimeException {
	private final String errCode;
	private final String errMessage;
	private final String debugMessage;

	public ServiceException(String errCode, String errMessage, String debugMessage) {
		super(getExceptionMessage(errCode, errMessage, debugMessage));
		this.errCode = errCode;
		this.errMessage = errMessage;
		this.debugMessage = debugMessage;
	}

	public ServiceException(String errCode, String errMessage, String debugMessage, Throwable cause) {
		super(getExceptionMessage(errCode, errMessage, debugMessage), cause);
		this.errCode = errCode;
		this.errMessage = errMessage;
		this.debugMessage = debugMessage;
	}

	public ServiceException(String errorCode, String errMessage) {
		super(getExceptionMessage(errorCode, errMessage, null));
		this.errCode = errorCode;
		this.errMessage = errMessage;
		this.debugMessage = null;
	}

	public ServiceException(String errorCode, String errMessage, Throwable cause) {
		super(getExceptionMessage(errorCode, errMessage, null), cause);
		this.errCode = errorCode;
		this.errMessage = errMessage;
		this.debugMessage = null;
	}

	private static String getExceptionMessage(String errorCode, String errMessage, String debugMessage) {
		var sb = new StringBuilder();
		sb.append(errorCode);
		sb.append(" : ");
		sb.append(errMessage);

		if (StringUtils.isNotBlank(debugMessage)) {
			sb.append(" - ");
			sb.append(debugMessage);
		}

		return sb.toString();
	}
}

