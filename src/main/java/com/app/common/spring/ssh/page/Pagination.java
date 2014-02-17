package com.app.common.spring.ssh.page;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 * 本类是一个参数类。<br>
 * 主要在作分页显示时使用。其表示从什么地方开始取数据，取多少条数据。<br>
 * 在该参数中还可以可到合乎条件的数据的总数量。<br>
 * <br>
 * 使用说明:<br>
 * Pagination主要是记录在一个列表中每次需要列出多少条记录,以及相关的信息<br>
 * 其记录的信息包括<br>
 * <li>showAll 是否显示所有数据,如果不需要程序分页,则showAll = true</li> <li>start
 * (只读)起始位置,根据currentPage,list得到, 其主要指明, 从第几条数据开始取数和显示,如果start=11,那么程序就应该从满足条件的
 * 第11条数据开始显示</li> <li>maxSize 每次显示的数据数目 如果start=11,而maxSize=10,那么程序就应该显
 * 示从11到20的满足条件的10条数据</li> <li>totalSize 总的记录条数</li> <li>currentPage 当前页数 至少为1</li>
 * <li>totalPage (只读)总的记录条数, 根据list和size得到,当无法计算时,为1</li>
 * 
 * 
 * 
 */
@Component("pagination")
public class Pagination {

	/**
	 * 是否显示所有数据, 是则不用分页显示 否则分页显示
	 */
	protected boolean showAll;

	/**
	 * (只读)起始位置,根据currentPage,list得到,
	 * 其主要指明,从第几条数据开始取数和显示,如果start=11,那么程序就应该从满足条件的第11条数据开始显示
	 * 
	 */
	protected long start;

	/**
	 * 每次显示的数据数目 如果start=11,而maxSize=10,那么程序就应该显示从11到20的满足条件的10条数据
	 */
	protected int maxSize;

	/**
	 * 总的记录条数 该属性比较特殊,其在查询结果出来以后得到所有记录的条数,然后改变该属性
	 */
	protected long totalSize;

	/**
	 * 当前页数 至少为1
	 */
	protected long currentPage;

	/**
	 * (只读)总的记录条数 根据list和size得到,当无法计算时,为1
	 */
	protected long totalPage;

	/**
	 * 和该分页相对应的Object 该Object可以是参数类,也可以是Struts Formbean 在和Struts结合是, 该属性记录的是
	 */
	protected Serializable object;

	/**
	 * 是否使用旧的参数来列出数据 该条件一个版和object对应使用 如果useOldParams 为false, 那么object属性不再有用
	 * 如果useOldParams 为true, 那么应该判断object属性是否为空
	 */
	protected boolean useOldParams;

	/**
	 * 构造函数
	 */
	public Pagination() {
		init();
	}

	/**
	 * 构造函数
	 * 
	 * @param showAll
	 *            是否显示所有数据
	 */
	public Pagination(boolean showAll) {
		init();
		this.showAll = showAll;
	}

	/**
	 * 构造函数
	 * 
	 * @param currentPage
	 *            当前页面
	 * @param list
	 *            每一页的记录条数
	 */
	public Pagination(long currentPage, int list) {
		init();
		if (currentPage < 1) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}

		if (list < 1) {
			this.maxSize = 1;
		} else {
			this.maxSize = list;
		}
		changeStart();

	}

	/** 数据初始化 */
	protected void init() {
		showAll = false;
		maxSize = 20;
		totalSize = 0;
		currentPage = 1;
		totalPage = 1;
		object = null;
		useOldParams = false;
		changeStart();
		changTotalPage();
	}

	/**
	 * 根据currentPage和list修改start的值
	 */
	protected void changeStart() {
		start = (currentPage > 1) ? (currentPage - 1) * maxSize + 1 : 1;
	}

	/**
	 * Returns the list.
	 * 
	 * @return int
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * Returns the showAll.
	 * 
	 * @return boolean
	 */
	public boolean isShowAll() {
		return showAll;
	}

	/**
	 * Returns the size.
	 * 
	 * @return long
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * Returns the start.
	 * 
	 * @return long
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Returns the totalPage.
	 * 
	 * @return long
	 */
	public long getTotalPage() {
		changTotalPage();
		return totalPage;
	}

	/**
	 * Sets the list.
	 * 
	 * @param list
	 *            The list to set
	 */
	public void setMaxSize(int list) {
		this.maxSize = list;
		changeStart();
		// changTotalPage();
		showAll = false;
	}

	/**
	 * Sets the showAll.
	 * 
	 * @param showAll
	 *            The showAll to set
	 */
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	/**
	 * Sets the size.
	 * 
	 * @param size
	 *            The size to set
	 */
	public void setTotalSize(long size) {
		this.totalSize = size;
		changTotalPage();
	}

	/** 修改总共多少页 */
	private void changTotalPage() {
		if (totalSize > 0) {
			totalPage = ((totalSize % maxSize) > 0) ? totalSize / maxSize + 1
					: totalSize / maxSize;
		} else {
			totalPage = 1;
		}
		currentPage = (currentPage > totalPage) ? totalPage : currentPage;

	}

	/**
	 * Returns the currentPage.
	 * 
	 * @return long
	 */
	public long getCurrentPage() {
		return currentPage;
	}

	/**
	 * Sets the currentPage.
	 * 
	 * @param currentPage
	 *            The currentPage to set
	 */
	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
		changeStart();
		showAll = false;
	}

	/**
	 * 重新初始化
	 */
	public void reset() {
		init();
	}

	public Serializable getObject() {
		return object;
	}

	public void setObject(Serializable serializable) {
		object = serializable;
		if (object != null)
			useOldParams = true;
	}

	public boolean isUseOldParams() {
		return useOldParams;
	}

	public void setUseOldParams(boolean b) {
		useOldParams = b;
		if (!useOldParams) {
			object = null;
		}
	}

	public String toString() {
		if (showAll) {
			return "显示所有数据";
		} else {
			return "其实页面" + currentPage + ",每页最大记录条数" + maxSize + ",总记录条数为"
					+ totalSize;
		}
	}
}
