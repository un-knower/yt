package com.yt.business;

import java.io.Serializable;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;

import com.yt.hbase.BaseBean;
import com.yt.hbase.annotation.HbaseColumn;
import com.yt.neo4j.bean.Neo4jBaseBean;

public class BaseBeanImpl implements Serializable, BaseBean, Neo4jBaseBean {
	private static final long serialVersionUID = -916424014919620404L;

	@Indexed(unique = true)
	private String rowKey = "";

	@GraphId
	private Long graphId = null;

	@HbaseColumn(name = "cuid")
	private String createdUserId = "";

	@HbaseColumn(name = "ct")
	private long createdTime = 0l;

	@HbaseColumn(name = "uuid")
	private String updatedUserId = "";

	@HbaseColumn(name = "ut")
	private long updatedTime = 0l;

	/**
	 * 默认的构造函数
	 */
	public BaseBeanImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.neo4j.bean.Neo4jBaseBean#getGraphId()
	 */
	@Override
	public Long getGraphId() {
		return graphId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.neo4j.bean.Neo4jBaseBean#setGraphId(java.lang.Long)
	 */
	@Override
	public void setGraphId(Long id) {
		this.graphId = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getRowKey()
	 */
	@Override
	public String getRowKey() {
		return rowKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setRowKey(java.lang.String)
	 */
	@Override
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getCreatedUserId()
	 */
	@Override
	public String getCreatedUserId() {
		return createdUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setCreatedUserId(java.lang.String)
	 */
	@Override
	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getCreatedTime()
	 */
	@Override
	public long getCreatedTime() {
		return createdTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setCreatedTime(long)
	 */
	@Override
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getUpdatedUserId()
	 */
	@Override
	public String getUpdatedUserId() {
		return updatedUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setUpdatedUserId(java.lang.String)
	 */
	@Override
	public void setUpdatedUserId(String updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getUpdatedTime()
	 */
	@Override
	public long getUpdatedTime() {
		return updatedTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setUpdatedTime(long)
	 */
	@Override
	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

}