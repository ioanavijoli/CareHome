package com.servustech.carehome.web;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.servustech.carehome.util.exception.BusinessError;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;
import com.servustech.carehome.web.model.ErrorResponse;
import com.servustech.mongo.utils.exception.EntityNotFoundException;

@ControllerAdvice
public class RestExceptionProcessor {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(RestExceptionProcessor.class);

	@Autowired
	private MessageSource		messageSource;

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse<RestAPIError> entityNotFound(
			final HttpServletRequest request,
			final EntityNotFoundException exception) {
		LOGGER.warn("Entity not found exception", exception);
		final String messageKey = "entity.not.found";

		return new ErrorResponse<RestAPIError>(new RestAPIError(RestAPIError.Type.BUSINESS, messageKey,
				this.messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale()), null));
	}

	@ExceptionHandler(ValidationError.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse<RestAPIError> validationError(
			final HttpServletRequest request,
			final ValidationError validationError) {
		final ErrorModel errorModel = validationError.errorModels().get(0);
		LOGGER.info("Validation exception, invalid input: {}", errorModel.code());

		return new ErrorResponse<>(new RestAPIError(RestAPIError.Type.VALIDATION, errorModel.code(),
				this.messageSource.getMessage(errorModel.code(), null, LocaleContextHolder.getLocale()), null));
	}

	@ExceptionHandler(BusinessError.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse<RestAPIError> businessError(
			final HttpServletRequest request,
			final BusinessError exception) {
		String errorCode;
		if (exception.message().isPresent()) {
			errorCode = exception.message().get().getCode();
		} else {
			errorCode = "business.exception";
		}

		final String message = this.messageSource.getMessage(errorCode, null, LocaleContextHolder.getLocale());
		LOGGER.info("Business exception: {}", message);

		return new ErrorResponse<>(new RestAPIError(RestAPIError.Type.BUSINESS, errorCode, message, null));
	}

	// @ExceptionHandler({ EmptyResultDataAccessException.class, NoResultException.class })
	// @ResponseStatus(value = HttpStatus.NOT_FOUND)
	// @ResponseBody
	// public ErrorResponse<RestAPIError> springEntityNotFound(
	// final HttpServletRequest request,
	// final Exception exception) {
	// this.LOGGER.error("Entity not found", exception);
	// final Locale locale = LocaleContextHolder.getLocale();
	// return new ErrorResponse<>(
	// new RestAPIError(RestAPIError.Type.PERSISTENCE, "entity.not.found", this.messageSource.getMessage("entity.not.found", null, locale), null));
	// }

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorResponse<RestAPIError> accessDeniedException(
			final HttpServletRequest request,
			final AccessDeniedException exception) {
		final String accessDeniedMessage = this.messageSource.getMessage("access.denied", null, LocaleContextHolder.getLocale());
		LOGGER.info("Access denied exception {}", exception.getLocalizedMessage());

		return new ErrorResponse<>(new RestAPIError(RestAPIError.Type.AUTHORIZATION, "access.denied", accessDeniedMessage, null));
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorResponse<RestAPIError> authenticationException(
			final HttpServletRequest request,
			final AuthenticationException exception) {
		final String accessDeniedMessage = this.messageSource.getMessage("access.denied", null, LocaleContextHolder.getLocale());
		LOGGER.info("Authentication exception {}", exception.getLocalizedMessage());

		return new ErrorResponse<>(new RestAPIError(RestAPIError.Type.AUTHENTICATION, "access.denied", accessDeniedMessage, null));
	}

	@ExceptionHandler(AuthenticationServiceException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorResponse<RestAPIError> authenticationServiceException(
			final HttpServletRequest request,
			final AuthenticationServiceException exception) {
		final String accessDeniedMessage = this.messageSource.getMessage("access.denied", null, LocaleContextHolder.getLocale());
		LOGGER.info("Authentication exception {}", exception.getLocalizedMessage());

		return new ErrorResponse<>(new RestAPIError(RestAPIError.Type.AUTHENTICATION, "access.denied", accessDeniedMessage, null));
	}

	// @ExceptionHandler(Exception.class)
	// @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	// @ResponseBody
	// public ErrorResponse<RestAPIError> generalException(
	// final HttpServletRequest request,
	// final Exception e) {
	// LOGGER.error("Error", e);
	// final Locale locale = LocaleContextHolder.getLocale();
	// return new ErrorResponse<>(new RestAPIError(RestAPIError.Type.GENERAL, "general.exception",
	// messageSource.getMessage("general.exception", null, locale), null));
	// }

}