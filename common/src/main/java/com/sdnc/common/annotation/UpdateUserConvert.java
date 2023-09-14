package com.sdnc.common.annotation;

import com.sdnc.common.auth.AccessContext;
import org.beetl.sql.annotation.builder.AttributeConvert;
import org.beetl.sql.annotation.builder.FillStrategy;
import org.beetl.sql.clazz.SQLType;
import org.beetl.sql.clazz.kit.BeanKit;
import org.beetl.sql.core.ExecuteContext;

/**
 * 在插入或者更新的时候,填充当前登录人ID的注解转换器
 */
public class UpdateUserConvert implements AttributeConvert {

	@Override
	public Object toDb(ExecuteContext ctx, Class cls, String name, Object pojo) {
		UpdateUser fillUpdateUser = BeanKit.getAnnotation(cls, name, UpdateUser.class);
		FillStrategy fillStrategy = fillUpdateUser.value();
		SQLType sqlType = ctx.sqlSource.getSqlType();
		//判断是否insert语句
		if (FillStrategy.INSERT == fillStrategy && SQLType.INSERT != sqlType) {
			return null;
		}
		//判断是否update语句
		if (FillStrategy.UPDATE == fillStrategy && SQLType.UPDATE != sqlType) {
			return null;
		}
		//判断是否insert或update语句
		if (FillStrategy.INSERT_UPDATE == fillStrategy && !sqlType.isUpdate()) {
			return null;
		}
		Long userId = AccessContext.getAccessUser().getId();
		BeanKit.setBeanProperty(pojo, userId, name);

		return userId;
	}

}
