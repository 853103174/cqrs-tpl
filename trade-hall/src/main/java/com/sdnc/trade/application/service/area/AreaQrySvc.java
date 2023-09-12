package com.sdnc.trade.application.service.area;

import java.util.List;

import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;

import com.sdnc.common.kits.QueryKit;
import com.sdnc.trade.domain.businessobject.area.AreaPageBO;
import com.sdnc.trade.domain.persistantobject.area.AreaPO;
import com.sdnc.trade.domain.valueobject.area.AreaPageVO;
import com.sdnc.trade.domain.valueobject.area.AreaViewVO;
import com.sdnc.trade.infrastructure.dao.area.AreaDao;

import lombok.RequiredArgsConstructor;

/**
 *
 * 行政区划表
 *
 *
 */
@Service
@RequiredArgsConstructor
public class AreaQrySvc {

    private final AreaDao dao;

    /**
     * 分页
     *
     * @param bo
     * @return
     */
    public PageResult<AreaPageVO> page(AreaPageBO bo) {
        PageResult<AreaPageVO> result = dao.createLambdaQuery()
                .andEq(AreaPO::getCode, QueryKit.notNull(bo.getCode()))
                .page(bo.getPageNum(), bo.getPageSize(), AreaPageVO.class);

        return result;
    }

    /**
     * 获取父编码下的子区域
     *
     * @param parentCode
     * @param name
     * @return
     */
    public List<AreaPageVO> allList(Integer parentCode, String name) {
        return dao.allList(parentCode, name);
    }

    /**
     * 查询单个
     *
     * @param code 编码
     * @return
     */
    public AreaViewVO view(Integer code) {
        return dao.single(code, AreaViewVO.class);
    }

}
