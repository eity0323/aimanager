package com.sien.lib.datapp.beans;

/**
 * @author sien
 * @description 基类请求参数
 **/
public class BaseRequest extends CPBaseVO {

	/**
	 * serialVersionUID
	 */
	private int pageNum;
	private int pageSize;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
