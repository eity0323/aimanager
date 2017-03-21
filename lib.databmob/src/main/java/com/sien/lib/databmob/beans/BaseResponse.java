package com.sien.lib.databmob.beans;

/**
 * [A brief description]
 * 
 * @author devin.hu
 * @version 1.0
 * @date 2013-9-30
 **/
public class BaseResponse extends CPBaseVO {

	private int total;
	private int pages;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}
}
