package com.sunbasedata.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;				



@RestControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorDetails> handleException(Exception ex, WebRequest req) {
        CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage("Something went wrong.");
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);  
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorDetails> ConstraintViolationExceptionpHandler(ConstraintViolationException ex, WebRequest req){
    
    	CustomErrorDetails err = new CustomErrorDetails();
    	err.setTimestamp(LocalDateTime.now());
    	err.setMessage(ex.getMessage());
    	err.setDetails(req.getDescription(false));
    	return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.BAD_REQUEST);
    	
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorDetails> notFoundExceptionHandler(NotFoundException ex, WebRequest req){

        CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.NOT_FOUND);

    }
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorDetails> handleException(HttpMessageNotReadableException ex, WebRequest req) {
		StringBuilder msg = new StringBuilder(ex.getMessage());
		msg.replace(0, 16, "Invalid input");
		CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(msg.toString());
        err.setDetails(req.getDescription(false));

        return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(
			
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return ResponseEntity.badRequest().body(errors);
	}
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorDetails> exceptionHandler(AccessDeniedException ex,WebRequest req){
        CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage("You do not have permission to access this resource");
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomErrorDetails> exceptionHandler(NoHandlerFoundException ex,WebRequest req){
        CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
    
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorDetails> exceptionHandler(BadCredentialsException ex,WebRequest req){
        CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<CustomErrorDetails> exceptionHandler(JwtException ex,WebRequest req){
        CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage("Invalid token");
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<CustomErrorDetails> exceptionHandler(CustomerException ex,WebRequest req){
        CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RemoteApiException.class)
    public ResponseEntity<CustomErrorDetails> exceptionHandler(RemoteApiException ex,WebRequest req){
        CustomErrorDetails err = new CustomErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<CustomErrorDetails>(err, HttpStatus.NOT_FOUND);
    }
}
