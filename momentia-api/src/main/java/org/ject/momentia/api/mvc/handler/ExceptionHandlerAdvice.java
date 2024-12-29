package org.ject.momentia.api.mvc.handler;

import java.util.Objects;

import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.common.exception.ServiceException;
import org.ject.momentia.common.util.MomentiaStringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Request 처리 시 발생 가능한 예외에 대한 handler advice
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
	public static final String ERROR_KEY = ExceptionHandlerAdvice.class.getName();

	/**
	 * service exception
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ServiceException.class})
	public ResponseEntity<Response> serviceException(HttpServletRequest req, ServiceException e) {
		ErrorCd errorCd;

		try {
			errorCd = ErrorCd.valueOf(e.getErrCode());
		} catch (IllegalArgumentException iae) {
			log.warn("invalid serviceException errCode: {}", e.getErrCode());
			errorCd = ErrorCd.INTERNAL_SERVER_ERROR;
		}

		return handle(req, e, errorCd, e.getErrMessage());
	}

	/**
	 * spring - invalid parameter
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler({
		MissingServletRequestParameterException.class,
		ServletRequestBindingException.class,
		MissingServletRequestPartException.class,
		ConversionNotSupportedException.class,
		TypeMismatchException.class,
		HttpMessageNotReadableException.class,
		MethodArgumentNotValidException.class,
		BindException.class,
		HandlerMethodValidationException.class
	})
	public ResponseEntity<Response> parameterException(HttpServletRequest req, Exception e) {
		// TODO - 세부 param 정보 제공
		return handle(req, e, ErrorCd.INVALID_PARAMETER, null);
	}

	/**
	 * spring exception - no handler found
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler({NoHandlerFoundException.class})
	public ResponseEntity<Response> noHandlerFoundException(HttpServletRequest req, Exception e) {
		return handle(req, e, ErrorCd.NO_HANDLER_FOUND, null);
	}

	/**
	 * spring exception - method not allowed
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<Response> httpRequestMethodNotSupportedException(HttpServletRequest req, Exception e) {
		return handle(req, e, ErrorCd.METHOD_NOT_ALLOWED, null);
	}

	/**
	 * spring exception - unsupported media type
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	public ResponseEntity<Response> httpMediaTypeNotSupportedException(HttpServletRequest req, Exception e) {
		return handle(req, e, ErrorCd.UNSUPPORTED_MEDIA_TYPE, null);
	}

	/**
	 * spring exception - not acceptable
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
	public ResponseEntity<Response> httpMediaTypeNotAcceptableException(HttpServletRequest req, Exception e) {
		return handle(req, e, ErrorCd.NOT_ACCEPTABLE, null);
	}

	/**
	 * 이외의 에러
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Response> exception(HttpServletRequest req, Exception e) {
		log.error("Internal Server Error - exception:", e);
		return handle(req, e, ErrorCd.INTERNAL_SERVER_ERROR, null);
	}

	private ResponseEntity<Response> handle(HttpServletRequest request, Exception exception,
		ErrorCd errorCd, String errorMessage) {

		log.error("{} {}, parameter: {}, status: {}, exception: {}", request.getMethod(), request.getRequestURI(),
			MomentiaStringUtils.reflectionToString(request.getParameterMap()), errorCd.getHttpStatus(), exception);

		request.setAttribute(ERROR_KEY, true);
		var response = new Response(errorCd.name(), Objects.toString(errorMessage, errorCd.getErrorMessage()));
		return new ResponseEntity<>(response, errorCd.getHttpStatus());
	}

	public record Response(
		String code,
		String message
	) {

	}
}
