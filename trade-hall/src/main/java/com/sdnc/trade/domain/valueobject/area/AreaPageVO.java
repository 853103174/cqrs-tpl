package com.sdnc.trade.domain.valueobject.area;


import com.sdnc.trade.domain.persistantobject.area.AreaPO;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * 行政区划表
 *
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@AutoMapper(target = AreaPO.class)
public class AreaPageVO {

    /**
     * 编码
     */
    private Integer code;
    /**
     * 名称
     */
    private String name;
    /**
     * 父编码(0:省 其他:市县)
     */
    private Integer parentCode;
    /**
     * 完整名称
     */
    private String fullName;
    /**
     * 类型(1:省 2:市 3:县)
     */
    private Integer type;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 天气网编码
     */
    private Long weatherCode;

}