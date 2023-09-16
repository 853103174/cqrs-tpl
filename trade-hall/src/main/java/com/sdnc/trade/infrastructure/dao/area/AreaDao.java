package com.sdnc.trade.infrastructure.dao.area;

import com.sdnc.trade.domain.persistantobject.area.AreaPO;
import com.sdnc.trade.domain.valueobject.area.AreaPageVO;
import com.sdnc.trade.domain.valueobject.area.AreaViewVO;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.SqlResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 行政区划表
 */
@Repository
@SqlResource("area")
public interface AreaDao extends BaseMapper<AreaPO> {

	default AreaViewVO view(Integer code) {
		List<AreaViewVO> list = this.createLambdaQuery().andEq(AreaPO::getCode, code).select(AreaViewVO.class);

		return list.get(0);
	}

	List<AreaPageVO> list(Integer parentCode, String name);

}
