package com.sdnc.account.infrastructure.config;

import org.springframework.stereotype.Component;

import io.github.linpeilie.annotations.ComponentModelConfig;
import io.github.linpeilie.annotations.MapperConfig;

/**
 * mapstruct-plus配置信息
 */
@Component
@ComponentModelConfig
@MapperConfig(mapperPackage = "com.sdnc.account.application.assembler", adapterPackage = "com.sdnc.account.application.adapter", adapterClassName = "AccountConvertAdapter", mapAdapterClassName = "AccountMapConvertAdapter")
public class MapStructPlusConfiguration {

}
