package com.sdnc.common.query;

/**
 *
 * 分页查询参数
 *
 */
public class PageQO {

	/**
	 * 第几页, 从1开始
	 */
    private Integer pageNum;
	/**
	 * 每页有几条数据
	 */
    private Integer pageSize;

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
