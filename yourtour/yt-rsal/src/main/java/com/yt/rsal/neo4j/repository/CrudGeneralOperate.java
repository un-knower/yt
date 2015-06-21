/** 
 * @(#)CrudGeneralOperate.java
 *
 * Copyright 2015, 迪爱斯通信设备有限公司保留.<br>
 *
 * @author John Peng
 */
package com.yt.rsal.neo4j.repository;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.yt.rsal.neo4j.bean.INeo4JBaseBean;
import com.yt.rsal.neo4j.bean.Neo4JBaseBean;
import com.yt.rsal.neo4j.bean.Neo4JSaveFailRecordBean;
import com.yt.rsal.neo4j.bean.Neo4JSaveFailRecordBean.OperateType;

/**
 * 面向Neo4J实体对象进行常规CRUD操作的操作类。
 * 
 * <p>
 * <b>修改历史：</b>
 * <table border="1">
 * <tr>
 * <th>修改时间</th>
 * <th>修改人</th>
 * <th>备注</th>
 * </tr>
 * <tr>
 * <td>2015年6月21日</td>
 * <td>john</td>
 * <td>Create</td>
 * </tr>
 * </table>
 * 
 * @author john
 * 
 * @version 1.0
 * @since 1.0
 */
public class CrudGeneralOperate implements ICrudOperate {
	/** 静态变量：系统日志 */
	private static final Log LOG = LogFactory.getLog(CrudGeneralOperate.class);

	@Autowired
	private Neo4jTemplate template;

	@Autowired
	private com.yt.dal.hbase.ICrudOperate hbaseCrud;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.rsal.neo4j.repository.ICrudOperate#count(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public long count(Class<? extends INeo4JBaseBean> clazz) throws Exception {
		return template.count((Class<Neo4JBaseBean>) clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.rsal.neo4j.repository.ICrudOperate#get(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public INeo4JBaseBean get(Class<? extends INeo4JBaseBean> clazz,
			String rowKey) throws Exception {
		Result<INeo4JBaseBean> result = template.findByIndexedValue(clazz,
				"rowKey", rowKey);
		INeo4JBaseBean bean = result.singleOrNull();
		if (bean == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format(
						"The Neo4JBean[rowKey='%s'] not exist.", rowKey));
			}
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format(
						"Found a Neo4JBean, rowKey: %s, graphId: %d.", rowKey,
						((Neo4JBaseBean) bean).getGraphId()));
			}
		}
		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.rsal.neo4j.repository.ICrudOperate#get(java.lang.Class)
	 */
	@Override
	public List<? extends INeo4JBaseBean> get(
			Class<? extends INeo4JBaseBean> clazz) throws Exception {
		@SuppressWarnings("unchecked")
		Result<INeo4JBaseBean> result = template
				.findAll((Class<INeo4JBaseBean>) clazz);
		List<INeo4JBaseBean> list = new Vector<INeo4JBaseBean>();
		for (INeo4JBaseBean bean : result) {
			list.add(bean);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format(
					"Fetch Neo4J[class='%s'] success, total: %d.",
					clazz.getName(), list.size()));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.rsal.neo4j.repository.ICrudOperate#delete(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public void delete(Class<? extends INeo4JBaseBean> clazz, String rowKey)
			throws Exception {
		INeo4JBaseBean bean = get(clazz, rowKey);
		if (bean != null) {
			template.delete(bean);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String
						.format("Delete the Neo4JBean success, rowKey: %s, graphId: %d.",
								rowKey, ((Neo4JBaseBean) bean).getGraphId()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.rsal.neo4j.repository.ICrudOperate#delete(java.lang.Class)
	 */
	@Override
	public void delete(Class<? extends INeo4JBaseBean> clazz) throws Exception {
		@SuppressWarnings("unchecked")
		Result<INeo4JBaseBean> result = template
				.findAll((Class<INeo4JBaseBean>) clazz);
		long count = 0;
		for (INeo4JBaseBean bean : result) {
			template.delete(bean);
			count++;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format(
					"Delete the Neo4J[class='%s'] success, total: %d.",
					clazz.getName(), count));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.rsal.neo4j.repository.ICrudOperate#save(java.lang.Object)
	 */
	@Override
	public INeo4JBaseBean save(INeo4JBaseBean neo4jBean) throws Exception {
		if (neo4jBean == null) {
			String msg = "The Neo4JBean is null.";
			if (LOG.isWarnEnabled()) {
				LOG.warn(msg);
			}
			return null;
		}
		INeo4JBaseBean tar = template.save(neo4jBean);
		if (LOG.isDebugEnabled()) {
			Neo4JBaseBean bean = (Neo4JBaseBean) tar;
			LOG.debug(String.format(
					"Save Neo4JBean success, rowKey: %s, graphId: %d.",
					bean.getRowKey(), bean.getGraphId()));
		}
		return tar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.rsal.neo4j.repository.ICrudOperate#save(java.lang.Object,
	 * boolean)
	 */
	@Override
	public INeo4JBaseBean save(INeo4JBaseBean neo4jBean, boolean saveFail2Hbase)
			throws Exception {
		OperateType ot = OperateType.INSERT;
		Neo4JBaseBean bean = (Neo4JBaseBean) neo4jBean;
		if (bean.getGraphId() != null) {
			ot = OperateType.UPDATE;
		}
		try {
			return save(neo4jBean);
		} catch (Exception ex) {
			if (saveFail2Hbase) {
				// 保存失败信息到hbase，便于后续补救处理。
				Neo4JSaveFailRecordBean fail = new Neo4JSaveFailRecordBean();
				fail.setClassName(neo4jBean.getClass().getName());
				fail.setOperateType(ot);
				fail.setRelatedRowkey(bean.getRowKey());
				fail.setRowKey(String.valueOf(bean.hashCode()));
				hbaseCrud.save(fail);
				if (LOG.isWarnEnabled()) {
					LOG.warn(String
							.format("Save Neo4JBean[class='%s', rowKey='%s'] fail, but the fail information had been saved into hbase.",
									bean.getClass().getName(), bean.getRowKey()));
				}
			}
			throw ex;
		}
	}
}