package com.sdnc.account.interfaces.businessobject.passport;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * APP用户登录注册
 *
 *
 */
@Getter
@Setter
public class RefreshTokenBO {

	@NotBlank(message = "请输入刷新令牌")
	private String refreshToken;

}
