package com.icarat.amazons3.sample.advice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.icarat.amazons3.sample.dto.ErrorMessageDto;
import com.icarat.amazons3.sample.exception.Conflict;
import com.icarat.amazons3.sample.exception.DataNotFoundException;
import com.icarat.amazons3.sample.exception.HandleAmazonClientException;
import com.icarat.amazons3.sample.exception.HandleAmazonServiceException;

/**
 * RestExceptionHandler is Exception Advice controller
 * @author Icarat1
 *
 */
@ControllerAdvice
public class RestExceptionHandler {
	
	/**
	 * 
	 * @param e
	 * @return
	 */

	@ExceptionHandler(HandleAmazonClientException.class)
	public ResponseEntity<?> handleAmazonClientException(HandleAmazonClientException e) {
		return new ResponseEntity<ErrorMessageDto>(new ErrorMessageDto(
				e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	/**
	 * 
	 * @param e
	 * @return
	 */
	
	
	@ExceptionHandler(HandleAmazonServiceException.class)
	public ResponseEntity<?> handleAmazonServiceException(HandleAmazonServiceException e) {
		return new ResponseEntity<ErrorMessageDto>(new ErrorMessageDto(
				e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Conflict.class)
	public ResponseEntity<?> handleConflictCase(Conflict e) { 
		return new ResponseEntity<ErrorMessageDto>(new ErrorMessageDto(
				e.getMessage()), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<?> handleDataNotFoundException(DataNotFoundException e) { 
		return new ResponseEntity<ErrorMessageDto>(new ErrorMessageDto(
				e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
}
