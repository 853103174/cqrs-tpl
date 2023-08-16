package com.sdnc.common.beetlsql;

import java.util.Map;

import org.beetl.sql.core.ExecuteContext;
import org.beetl.sql.core.extend.ParaExtend;

import com.sdnc.common.kits.KV;

/**
 * 为BeetlSQL执行sql提供额外的参数, 可实现多租户数据隔离
 */
public final class CustomizeParaExtend extends ParaExtend {

    @Override
    public Map morePara(ExecuteContext ctx) {
        return KV.by("sort", 1);
    }

}
