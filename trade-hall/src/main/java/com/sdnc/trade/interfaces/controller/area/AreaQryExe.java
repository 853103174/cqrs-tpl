package com.sdnc.trade.interfaces.controller.area;

import com.sdnc.trade.application.service.area.AreaQrySvc;
import com.sdnc.trade.domain.businessobject.area.AreaPageBO;
import com.sdnc.trade.domain.valueobject.area.AreaPageVO;
import com.sdnc.trade.domain.valueobject.area.AreaViewVO;
import lombok.RequiredArgsConstructor;
import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

	@GetMapping("/list")
	public List<AreaPageVO> list(Integer parentCode, String name) {
		return service.list(parentCode, name);
	}

    @GetMapping("/view/{code}")
    public AreaViewVO view(@PathVariable Integer code) {
        return service.view(code);
    }

}
