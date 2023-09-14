package com.sdnc.account.application.service.passport;

import com.sdnc.account.domain.businessobject.passport.LoginBO;
import com.sdnc.account.domain.businessobject.passport.RegisterBO;
import com.sdnc.common.constant.TokenConstants;
import com.sdnc.common.exception.SystemException;
import com.sdnc.common.exception.TokenException;
import com.sdnc.common.kits.KV;
import com.sdnc.common.kits.ULIDKit;
import com.sdnc.common.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 *
 * APP用户登录注册
 *
 *
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PassportCmdSvc {

    // private final UserMapper mapper;
    private final RedisCache<Object> cache;

    /**
     * 注册
     *
     * @param bo 注册请求数据
     * @return token信息
     */
    public KV register(RegisterBO bo) {
        // Date now = new Date();
        // UserPO po = assembler.registerAs(dto);
        // mapper.insertTemplate(po);
        // Long userId = po.getId();
        // po = new UserPO();
        // po.setId(userId);
        // po.setCreateBy(userId);
        // po.setCreateAt(now);
        // po.setUpdateBy(userId);
        // po.setUpdateAt(now);
        // mapper.updateTemplateById(po);
        Long userId = 0L;
        KV kv = KV.by("accessToken", createAccessToken(userId))
                .set("refreshToken", createRefreshToken(userId));

        return kv;
    }

    /**
     * 登录
     *
     * @param bo 登录请求数据
     * @return token信息
     */
    public KV login(LoginBO bo) {
        // UserPO execute = null;
        if ("1".equals(bo.getType())) {
            // execute = mapper.execute(dtoAssembler.loginAssembler(dto));
        } else if ("2".equals(bo.getType())) {
            // execute = codeLoginService.execute(dtoAssembler.loginAssembler(dto));
        } else {
            throw new SystemException("不支持当前登录方式");
        }
        Long userId = 1L;
        KV kv = KV.by("accessToken", createAccessToken(userId))
                .set("refreshToken", createRefreshToken(userId));

        return kv;
    }

    /**
     * 刷新访问Token
     *
     * @param refreshToken 刷新token
     * @return token信息
     */
    public KV refresh(String refreshToken) {
        String userKey = String.format(TokenConstants.APP_REFRESH_TOKEN, refreshToken);
        Long userId = (Long) cache.getValue(userKey);
        if (userId == null) {
            throw new TokenException();
        }
        KV kv = KV.by("accessToken", createAccessToken(userId))
                .set("refreshToken", createRefreshToken(userId));
        cache.unlink(userKey);

        return kv;
    }

    /**
     * 创建访问令牌
     *
     * @param userId 用户ID
     * @return 令牌
     */
    private String createAccessToken(Long userId) {
        String ulid = ULIDKit.createToken(userId);
        String userKey = String.format(TokenConstants.APP_ACCESS_TOKEN, ulid);
        cache.putValue(userKey, userId.toString(), 30, TimeUnit.MINUTES);

        return ulid;
    }

    /**
     * 创建刷新令牌
     *
     * @param userId 用户ID
     * @return 令牌
     */
    private String createRefreshToken(Long userId) {
        String ulid = ULIDKit.createToken(userId);
        String userKey = String.format(TokenConstants.APP_REFRESH_TOKEN, ulid);
        cache.putValue(userKey, userId.toString(), 3, TimeUnit.DAYS);

        return ulid;
    }

}
