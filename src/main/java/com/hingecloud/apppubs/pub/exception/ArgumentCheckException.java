package com.hingecloud.apppubs.pub.exception;

public class ArgumentCheckException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ArgumentCheckException(String message) {
		super(message);
	}
}
