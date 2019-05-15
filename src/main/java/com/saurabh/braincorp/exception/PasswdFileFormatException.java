package com.saurabh.braincorp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswdFileFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PasswdFileFormatException(String msg) {
		super(msg);
	}

}
