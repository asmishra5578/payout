package io.asktech.payout.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.asktech.payout.dto.error.ErrorResponseDto;
import io.asktech.payout.exceptions.ValidationExceptions;

@ControllerAdvice
public class ExceptionHandlerController {
	static Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

	@ExceptionHandler(ValidationExceptions.class)
	ResponseEntity<Object> sessionException(final ValidationExceptions see) {
		
		ErrorResponseDto err = new ErrorResponseDto();
		err.getMsg().add(see.getMessage());
		err.setExceptionEnum(see.getException());
		// Log4jLogger.saveLog(see.getMessage()+"==>");
		logger.error(see.getMessage());
		return ResponseEntity.ok().body(err);
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<Object> exception(final Exception e) {
		ErrorResponseDto err = new ErrorResponseDto();

		err.getMsg().add(e.getMessage());
		// Log4jLogger.saveLog(e.getMessage()+"==>");
		logger.error("Exception :: "+e.getMessage());
		return ResponseEntity.ok().body(err);
	}
}
