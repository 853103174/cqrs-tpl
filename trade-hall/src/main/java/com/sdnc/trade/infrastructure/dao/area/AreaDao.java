package com.sdnc.trade.infrastructure.dao.area;

import java.util.List;

import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.Param;
import org.beetl.sql.mapper.annotation.SqlResource;
import org.springframework.stereotype.Repository;

import com.sdnc.trade.domain.persistantobject.area.AreaPO;
import com.sdnc.trade.domain.valueobject.area.AreaPageVO;

/**
 *
 * 行政区划表
 *
 *
 */
@Repository
@SqlResource("area")
public interface AreaDao extends BaseMapper<AreaPO> {

    List<AreaPageVO> allList(@Param("parentCode") Integer parentCode);

}
