package com.sdnc.trade.application.service.area;

import java.util.function.Function;

import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;

import com.sdnc.common.kits.QueryKit;
import com.sdnc.trade.domain.businessobject.area.AreaPageBO;
import com.sdnc.trade.domain.persistantobject.area.AreaPO;
import com.sdnc.trade.domain.valueobject.area.AreaPageVO;
import com.sdnc.trade.domain.valueobject.area.AreaViewVO;
import com.sdnc.trade.infrastructure.dao.area.AreaDao;

import io.github.linpeilie.Converter;
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
    private final Converter converter;

    /**
     * 分页
     *
     * @param bo
     * @return
     */
    public PageResult<AreaPageVO> page(AreaPageBO bo) {
        PageResult<AreaPO> result = dao.createLambdaQuery()
                .andLike(AreaPO::getName, QueryKit.like(bo.getName()))
                .page(bo.getPageNum(), bo.getPageSize());
        Function<AreaPO, AreaPageVO> function = po -> converter.convert(po, AreaPageVO.class);

        return result.convert(function);
    }

    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    public AreaViewVO view(Long id) {
        return converter.convert(dao.single(id), AreaViewVO.class);
    }

}
