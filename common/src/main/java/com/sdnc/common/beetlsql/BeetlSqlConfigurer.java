package com.sdnc.common.beetlsql;

import cn.beecp.BeeDataSource;
import com.sdnc.common.auth.AccessContext;
import com.sdnc.common.redis.RedisCache;
import jakarta.annotation.Resource;
import org.beetl.sql.core.IDAutoGen;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.loader.MarkdownClasspathLoader;
import org.beetl.sql.firewall.FireWall;
import org.beetl.sql.firewall.FireWallConfig;
import org.beetl.sql.starter.SQLManagerCustomize;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.sql.DataSource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * BeetlSql配置信息
 *
 */
@Configuration
public class BeetlSqlConfigurer {

	@Resource
	private RedisCache<Integer> cache;
	 @Lazy
	 @Resource
	 private SQLManager primarySQLManager;

	@Bean
	// @Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().type(BeeDataSource.class).build();
	}

	@Bean
	public SQLManagerCustomize sqlManagerCustomize() {
		SQLManager.javabeanStrict(false);

		return (sqlManagerName, manager) -> {
			Interceptor[] inters = manager.getInters();
			Interceptor[] newInterceptors = new Interceptor[inters.length + 1];
			newInterceptors[0] = new CustomizeSQLInterceptor();
			for (int i = 0, length = inters.length; i < length; ++i) {
				newInterceptors[i + 1] = inters[i];
			}
			manager.setInters(newInterceptors);
			// 配置xml插件
			MarkdownClasspathLoader sqlLoader = (MarkdownClasspathLoader) manager.getSqlLoader();
			CustomizeXMLBeetlSQL xmlBeetlSQL = new CustomizeXMLBeetlSQL();
			xmlBeetlSQL.config(sqlLoader.getSqlRoot(), manager);
			// 防火墙功能
			FireWall fireWall = new FireWall();
			fireWall.setDmlCreateEnable(false);
			fireWall.setSqlMaxLength(1000);
			FireWallConfig fireWallConfig = new FireWallConfig(fireWall);
			fireWallConfig.config(manager);
			// 支持多租户，数据权限，逻辑删除等 需要重写 sql，增加额外过滤条件的场景
			// 只有集成了 RewriteBaseMapper 的 Mapper 发出的操作才能触发 sql 改写
			//TenantConfig tenantConfig = new TenantConfig();
			//tenantConfig.config(manager);
			//tenantConfig.addColRewriteConfig(new ColRewriteParam("tenant_id", new ColValueProvider() {
			//	@Override
			//	public Object getCurrentValue() {
			//		return 1;
			//	}
			//}));
			//tenantConfig.addColRewriteConfig(new ColRewriteParam("dept_id", new ColValueProvider() {
			//	@Override
			//	public Object getCurrentValue() {
			//		return Arrays.asList(1,2,3);
			//	}
			//}));

			// 将BigDecimal映射为BigInteger
			// BigIntTypeHandler bigIntTypeHandler = new BigIntTypeHandler();
			// manager.getDefaultBeanProcessors().addHandler(BigInteger.class,
			// bigIntTypeHandler);

			// 自动生成自增主键,微服务可以使用github上滴滴开源的Tinyid方案
			manager.addIdAutoGen("autoId", new IDAutoGen<Long>() {

				@Override
				public Long nextID(String params) {
					String sql = "select nextval('all_id_seq')";
					return primarySQLManager.executeQueryOne(new SQLReady(sql), Long.class);
				}

			});
			// 订单号
			manager.addIdAutoGen("orderId", new IDAutoGen<Long>() {

				@Override
				public Long nextID(String params) {
					Date now = new Date();
					String date = String.format("order-%tF", now);
					if (!cache.hasKey(date)) {
						cache.expire(date, 1, TimeUnit.DAYS);
					}
					Long count = cache.increment(date);
					if (count < 100) {
						Long remainderUserId = AccessContext.getAccessUser().getId() % 100;
						String orderId = String.format("%1$ty%1$tm%1$td%2$02d%3$03d", now, remainderUserId, count);

						return Long.valueOf(orderId);
					} else if (count >= 100 && count < 10000) {
						Long remainderUserId = AccessContext.getAccessUser().getId() % 10;
						String orderId = String.format("%1$ty%1$tm%1$td%2$d%3$04d", now, remainderUserId, count);

						return Long.valueOf(orderId);
					} else {
						String orderId = String.format("%1$ty%1$tm%1$td%2$d", now, count);

						return Long.valueOf(orderId);
					}
				}

			});
		};
	}

}
