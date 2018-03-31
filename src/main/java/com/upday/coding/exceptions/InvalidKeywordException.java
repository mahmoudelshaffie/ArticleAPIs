package com.upday.coding.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.upday.coding.validation.ValidationMessages;

@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class InvalidKeywordException extends RuntimeException {

	public InvalidKeywordException() {
		super(ValidationMessages.INVALID_KEYWORD_MSG);
	}
}
