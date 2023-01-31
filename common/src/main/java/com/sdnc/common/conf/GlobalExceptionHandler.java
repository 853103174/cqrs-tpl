package com.sdnc.common.conf;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sdnc.common.exception.BizException;
import com.sdnc.common.exception.TokenException;

/**
 *
 * 全局异常处理器
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 业务异常
	 */
	@ExceptionHandler(BizException.class)
	public ResponseEntity<String> bizException(BizException e) {
		return ResponseEntity
				.status(HttpStatus.NOT_EXTENDED)
				.header("Content-Type", "text/plain;charset=UTF-8")
				.body(e.getMessage());
	}

	/**
	 * 校验异常
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<String> bindException(BindException e) {
		FieldError error = e.getBindingResult().getFieldErrors().get(0);

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.header("Content-Type", "text/plain;charset=UTF-8")
				.body(String.format("%s:%s", error.getField(), error.getDefaultMessage()));
	}

	/**
	 * Token过期
	 */
	@ExceptionHandler(TokenException.class)
	public ResponseEntity<Void> tokenException(TokenException e) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}

	/**
	 * 其他异常
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> otherException(Exception e) {
		return ResponseEntity
				.status(HttpStatus.NOT_EXTENDED)
				.header("Content-Type", "text/plain;charset=UTF-8")
				.body("服务器异常");
	}

}
