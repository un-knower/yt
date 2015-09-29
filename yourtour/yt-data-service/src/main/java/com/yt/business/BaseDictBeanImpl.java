package com.yt.business;

import org.springframework.data.neo4j.annotation.Indexed;

import com.yt.hbase.annotation.HbaseColumn;
import com.yt.neo4j.bean.Neo4jBaseDictBean;

public abstract class BaseDictBeanImpl extends BaseBeanImpl implements
		Neo4jBaseDictBean {
	private static final long serialVersionUID = -6769385550184467649L;

	@HbaseColumn(name = "code")
	@Indexed(unique = true)
	private String code = "";

	@HbaseColumn(name = "name")
	@Indexed
	private String name = "";

	/**
	 * 默认的构造函数
	 */
	public BaseDictBeanImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseDictBean#getCode()
	 */
	@Override
	public String getCode() {
		return code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseDictBean#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		this.code = code;
		super.setRowKey(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseDictBean#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseDictBean#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

}