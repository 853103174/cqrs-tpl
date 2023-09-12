package com.sdnc.trade.infrastructure.dao.area;

import com.sdnc.trade.domain.persistantobject.area.AreaPO;
import com.sdnc.trade.domain.valueobject.area.AreaPageVO;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.Param;
import org.beetl.sql.mapper.annotation.SqlResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * 行政区划表
 *
 *
 */
@Repository
@SqlResource("area")
public interface AreaDao extends BaseMapper<AreaPO> {

	default <K> K single(Integer code, Class<K> retType){
		List<K> list = this.createLambdaQuery()
				.andEq(AreaPO::getCode, code)
				.select(retType);

		return list.get(0);
	}

	List<AreaPageVO> allList(@Param("parentCode") Integer parentCode, @Param("name") String name);

}
