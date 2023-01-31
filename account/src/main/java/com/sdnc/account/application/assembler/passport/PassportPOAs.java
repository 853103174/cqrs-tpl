package com.sdnc.account.application.assembler.passport;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 *
 * APP用户登录注册
 *
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassportPOAs {

    // UserPO registerAs(RegisterBO bo);
    // UserPO modifyAs(UserModifyDO bo);

}
