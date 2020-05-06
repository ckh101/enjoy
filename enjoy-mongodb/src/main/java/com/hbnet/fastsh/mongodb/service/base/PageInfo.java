package com.hbnet.fastsh.mongodb.service.base;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 分页
 */
public class PageInfo<T> implements Serializable {
	private static final long serialVersionUID = 4221452855354549345L;
	
	/** 每页最大记录数限制 */
	public static final Integer MAX_PAGE_SIZE = 500;
	
	/** 当前页码 */
	private Integer pageNumber = 1;
	
	/** 每页记录数 */
	private Integer pageSize = 30;
	
	/** 总记录数 */
	private Long totalCount = 0L;
	
	/** 总页数 */
	private Long pageCount = 0L;

	public PageInfo() {}

	public PageInfo(Integer pageNo, Integer pageSize) {
        if (pageNo != null && pageNo > 0) {
            pageNumber = pageNo;
        }
        if (pageSize != null && pageSize > 0) {
            this.pageSize = pageSize;
        }
        setPageNumber(pageNumber);
        setPageSize(this.pageSize);
    }

	/**
	 * 获取下标
	 * 
	 * @return
	 */
	public int getOffset() {
		return (pageNumber - 1) * pageSize;
	}



	/**  数据List */
	private List<T> list;

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		}
		/**
		 * else if(pageSize > MAX_PAGE_SIZE) { pageSize = MAX_PAGE_SIZE; }
		 */
		this.pageSize = pageSize;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getPageCount() {
		pageCount = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;

		return pageCount;
	}

	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}

	public List<T>  getList() {
		if (this.list == null) {
			return new ArrayList<T>();
		}
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
