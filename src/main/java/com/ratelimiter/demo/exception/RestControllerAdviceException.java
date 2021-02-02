package com.ratelimiter.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonProperty;

@RestControllerAdvice
public class RestControllerAdviceException extends ResponseEntityExceptionHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(RestControllerAdviceException.class);

	@ExceptionHandler(RateLimiterException.class)
	@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS)
	public ReturnStatus processRateLimiterException(RateLimiterException e) {
		ReturnStatus status = new ReturnStatus();
		status.setHttpStatus(HttpStatus.TOO_MANY_REQUESTS);
		status.exceptionClass = e.getClass().getName();
		if (e.getMessage() == null || e.getMessage().trim().length() == 0) {
			status.message = "Too Many Requests!";
		} else {
			status.message = e.getMessage();
		}
		LOGGER.error("Handle RateLimiter Exception", e);
		return status;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ReturnStatus processException(Exception e) {
		ReturnStatus status = new ReturnStatus();
		status.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		status.exceptionClass = e.getClass().getName();
		if (e.getMessage() == null || e.getMessage().trim().length() == 0) {
			status.message = "The service request was rejected.";
		} else {
			status.message = e.getMessage();
		}
		LOGGER.error("Handle unexpected Exception", e);
		return status;
	}

	public static class ReturnStatus {

		private int code;

		@JsonProperty("reason-phrase")
		private String reasonPhrase;

		private String message;

		@JsonProperty("exception-class")
		private String exceptionClass;

		/**
		 * @return the code of HTTP status
		 */
		public int getCode() {
			return code;
		}

		/**
		 * @return the reason phrase of HTTP status
		 */
		public String getReasonPhrase() {
			return reasonPhrase;
		}

		public void setHttpStatus(HttpStatus status) {
			this.code = status.value();
			this.reasonPhrase = status.getReasonPhrase();
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @return the class name of exception
		 */
		public String getExceptionClass() {
			return exceptionClass;
		}
	}

}
