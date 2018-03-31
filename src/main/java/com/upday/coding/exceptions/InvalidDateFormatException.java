package com.upday.coding.exceptions;

import java.text.ParseException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import static com.upday.coding.validation.ValidationMessages.INVALID_DATE_FORMAT_MSG;;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = INVALID_DATE_FORMAT_MSG)
public class InvalidDateFormatException extends RuntimeException {

	public InvalidDateFormatException(ParseException cause) {
		super(INVALID_DATE_FORMAT_MSG, cause);
	}
}
