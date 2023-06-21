package com.sdnc.trade.infrastructure.dao.area;

import org.beetl.sql.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import com.sdnc.trade.domain.persistantobject.area.AreaPO;

/**
 *
 * 行政区划表
 *
 *
 */
@Repository
public interface AreaDao extends BaseMapper<AreaPO> {

}
