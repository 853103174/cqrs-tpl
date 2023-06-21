package com.sdnc.trade.interfaces.controller.area;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdnc.trade.application.service.area.AreaQrySvc;
import com.sdnc.trade.domain.valueobject.area.AreaPageVO;
import com.sdnc.trade.domain.valueobject.area.AreaViewVO;
import com.sdnc.trade.domain.businessobject.area.AreaPageBO;

import lombok.RequiredArgsConstructor;

/**
 *
 * 行政区划表
 *
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/qry/area")
public class AreaQryExe {

    private final AreaQrySvc service;

    @GetMapping("/page")
    public PageResult<AreaPageVO> page(AreaPageBO bo) {
        return service.page(bo);
    }

    @GetMapping("/view/{id}")
    public AreaViewVO view(@PathVariable Long id) {
        return service.view(id);
    }

}
