package com.sdnc.account.interfaces.businessobject.passport;

import jakarta.validation.constraints.Pattern;
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
public class PhoneCheckBO {

    /**
     * 手机号
     */
    @Pattern(regexp = "^1(3|4|5|6|7|8|9)\\d{9}", message = "请输入11位的手机号")
    private String phone;

}
