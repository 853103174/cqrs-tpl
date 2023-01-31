package com.sdnc.account.interfaces.controller.passport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sdnc.account.application.service.passport.PassportCmdSvc;
import com.sdnc.account.interfaces.businessobject.passport.LoginBO;
import com.sdnc.account.interfaces.businessobject.passport.PhoneCheckBO;
import com.sdnc.account.interfaces.businessobject.passport.PhoneSubmitBO;
import com.sdnc.account.interfaces.businessobject.passport.RefreshTokenBO;
import com.sdnc.account.interfaces.businessobject.passport.RegisterBO;
import com.sdnc.account.interfaces.businessobject.passport.ResetPasswordBO;
import com.sdnc.common.kits.KV;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 *
 * APP用户登录注册
 *
 *
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cmd/passport")
public class PassportCmdExe {

    private final PassportCmdSvc service;

    /**
     * 注册
     */
    @PostMapping("/register")
    public KV register(@Valid @RequestBody RegisterBO bo) {
        return service.register(bo);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public KV login(@Valid @RequestBody LoginBO bo) {
        return service.login(bo);
    }

    /**
     * 刷新token
     */
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public KV refresh(@Valid @RequestBody RefreshTokenBO bo) {
        return service.refresh(bo.getRefreshToken());
    }

    /**
     * 修改密码
     *
     * @param bo 新密码
     * @return
     */
    @PostMapping("/reset-pwd")
    public void resetPwd(@Valid @RequestBody ResetPasswordBO bo) {
        // resetPasswordService.execute(assembler.resetPwdAssembler(dto));
    }

    /**
     * 更换手机号时，验证原手机号
     *
     * @param bo 原手机号
     * @return
     */
    @PostMapping("/phone-check")
    public void phoneCheck(@Valid @RequestBody PhoneCheckBO bo) {
        // phoneCheckService.execute(assembler.phoneCheckAssembler(dto));
    }

    /**
     * 更换手机号，提交
     *
     * @param bo 新手机号
     * @return
     */
    @PostMapping("/phone-submit")
    public void phoneSubmit(@Valid @RequestBody PhoneSubmitBO bo) {
        // phoneSubmitService.execute(assembler.phoneSubmitAssembler(dto));
    }

}
