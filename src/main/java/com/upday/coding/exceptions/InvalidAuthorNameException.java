package com.upday.coding.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.upday.coding.validation.ValidationMessages;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidAuthorNameException extends RuntimeException {

	public InvalidAuthorNameException() {
		super(ValidationMessages.INVALID_AUTHOR_NAME_MSG);
	}
}
