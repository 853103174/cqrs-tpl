package com.sdnc.common.exception;

/**
 *
 * 令牌刷新值过期异常
 *
 */
public class TokenException extends RuntimeException {

    public TokenException() {
        super("令牌刷新值过期");
    }

}
