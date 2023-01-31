package com.sdnc.common.exception;

/**
 *
 * 业务异常
 *
 */
public class BizException extends RuntimeException {

	/**
	 * 所属模块
	 */
	private String module;

	/**
	 * 错误码
	 */
	private Integer code;

	/**
	 * 错误码对应的参数
	 */
	private Object[] args;

	/**
	 * 错误消息
	 */
	private String message;

	public BizException(String message) {
		this(null, null, null, message);
	}

	public BizException(Integer code, String message) {
		this(null, code, null, message);
	}

	public BizException(Integer code, Object[] args) {
		this(null, code, args, null);
	}

	public BizException(String module, Integer code, Object[] args) {
		this(module, code, args, null);
	}

	public BizException(String module, Integer code, Object[] args, String message) {
		this.module = module;
		this.code = code;
		this.args = args;
		this.message = message;
	}

	public String getModule() {
		return module;
	}

	public Integer getCode() {
		return code;
	}

	public Object[] getArgs() {
		return args;
	}

	public String getMessage() {
		return message;
	}

}
