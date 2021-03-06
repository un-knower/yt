package com.yt.hbase;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;

import com.yt.hbase.cache.BeanDescriptor;
import com.yt.hbase.cache.BeanDescriptor.Family;
import com.yt.hbase.cache.BeanDescriptor.Qualifier;
import com.yt.hbase.cache.BeanDescriptorCache;
import com.yt.hbase.utils.HBaseUtils;

/**
 * hbase中进行CRUD操作的实现类。
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
 * <td>2015年5月18日</td>
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
public class CrudGeneralOperate implements CrudOperate {
	private static final Log LOG = LogFactory.getLog(CrudGeneralOperate.class);

	@Autowired
	private HbaseManager manager;

	@Autowired
	private DdlOperate ddlOperate;

	@Autowired
	private BeanDescriptorCache cache;

	/**
	 * 默认构造方法
	 */
	public CrudGeneralOperate() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.dal.hbase.CrudOperate#save(com.yt.dal.hbase.BaseBean)
	 */
	@Override
	public void saveRow(BaseBean bean) throws Exception {
		if (bean == null) {
			return;
		}
		List<BaseBean> beans = new Vector<BaseBean>(1);
		beans.add(bean);
		saveBatch(beans, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.dal.hbase.CrudOperate#save(java.util.List)
	 */
	@Override
	public void saveRows(List<? extends BaseBean> beans) throws Exception {
		saveBatch(beans, true);
	}

	// 以Batch的方式批量保存数据
	private void saveBatch(List<? extends BaseBean> beans, boolean saveTimestamp)
			throws Exception {
		if (beans == null || beans.isEmpty()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("The bean is null or em.");
			}
			return;
		}
		Map<String, List<Put>> batch = new HashMap<String, List<Put>>();
		for (BaseBean bean : beans) {
			Class<? extends BaseBean> clazz = bean.getClass();
			BeanDescriptor bd = cache.get(clazz);
			if (bd == null) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(String.format("The BeanDescriptor[%s] not exist.",
							clazz.getName()));
				}
			}
			List<Put> puts = getBatchPuts(bd, bean, saveTimestamp);
			String ftn = bd.getFullTableName();
			if (batch.containsKey(ftn)) {
				batch.get(ftn).addAll(puts);
			} else {
				batch.put(ftn, puts);
			}
		}
		// batch save a table
		int num = 0;
		for (String ftn : batch.keySet()) {
			Table table = manager.getConnection().getTable(
					TableName.valueOf(ftn));
			List<Put> list = batch.get(ftn);
			Object[] putResult = new Object[list.size()];
			// table.batch(list, putResult);
			table.put(list);
			table.close();
			num += putResult.length;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format(
					"Save data successfully, total put %d operate.", num));
		}
	}

	// 判断对应的数据是否相等，如果相等则返回true，表示未修改，否则返回false。
	private boolean judgeNotUpdated(BaseBean bean, String fieldName,
			byte[] value) throws Exception {
		if (bean == null) {
			return false;
		}
		byte[] src = HBaseUtils.get(bean, fieldName);
		return Arrays.equals(src, value);
	}

	// 转换并获取一个实体对应的Put批处理列表，便于后续高效存储数据。
	private List<Put> getBatchPuts(BeanDescriptor bd, BaseBean bean,
			boolean saveTimestamp) throws Exception {
		BaseBean srcBean = getRow(bean.getClass(), bean.getRowKey());
		Vector<Put> puts = new Vector<Put>();
		byte[] rowkey = Bytes.toBytes(bean.getRowKey());
		for (BeanDescriptor.Family family : bd.getFamilies().values()) {
			byte[] byteFamily = family.getByteFamily();
			for (BeanDescriptor.Qualifier qualifier : family.getQualifiers()
					.values()) {
				Put put = null;
				if (saveTimestamp) {
					put = new Put(rowkey, System.currentTimeMillis());
				} else {
					put = new Put(rowkey);
				}
				byte[] byteQualifier = qualifier.getByteQualifier();
				byte[] byteValue = HBaseUtils.get(bean, qualifier.getName());
				// 判断数据是否有更新
				if (judgeNotUpdated(srcBean, qualifier.getName(), byteValue)) {
					// 跟hbase库中数据相同，没有更新数据，不需要进行保存
					continue;
				}
				if (saveTimestamp) {
					put.addColumn(byteFamily, byteQualifier,
							System.currentTimeMillis(), byteValue);
				} else {
					put.addColumn(byteFamily, byteQualifier, byteValue);
				}
				put.setDurability(Durability.SYNC_WAL);
				puts.add(put);
			}
		}
		return puts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.dal.hbase.CrudOperate#deleteRow(com.yt.dal.hbase.BaseBean)
	 */
	@Override
	public void deleteRow(BaseBean bean) throws Exception {
		if (bean == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("The bean is null, the delete operate be ignored.");
			}
			return;
		}
		List<BaseBean> beans = new Vector<BaseBean>(1);
		beans.add(bean);
		this.deleteRows(beans);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#deleteRows(java.lang.Class)
	 */
	@Override
	public void deleteRows(Class<? extends BaseBean> clazz) throws Exception {
		List<? extends BaseBean> beans = this.getRows(clazz);
		this.deleteRows(beans);
	}

	private void deleteRows(List<? extends BaseBean> beans) throws Exception {
		if (beans == null || beans.isEmpty()) {
			return;
		}
		Map<String, List<Delete>> batch = new HashMap<String, List<Delete>>();
		String ftn = "";
		for (BaseBean bean : beans) {
			Class<? extends BaseBean> clazz = bean.getClass();
			BeanDescriptor bd = cache.get(clazz);
			if (bd == null) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(String.format("The BeanDescriptor[%s] not exist.",
							clazz.getName()));
				}
			}
			ftn = bd.getFullTableName();
			Delete del = new Delete(Bytes.toBytes(bean.getRowKey()));
			for (Family family : bd.getFamilies().values()) {
				for (Qualifier qualifier : family.getQualifiers().values()) {
					del.addColumn(family.getByteFamily(),
							qualifier.getByteQualifier());
				}
			}
			if (batch.containsKey(ftn)) {
				batch.get(ftn).add(del);
			} else {
				List<Delete> list = new Vector<Delete>();
				list.add(del);
				batch.put(ftn, list);
			}
		}
		// batch save a table
		int rows = 0;
		for (String tableName : batch.keySet()) {
			Table table = manager.getConnection().getTable(
					TableName.valueOf(tableName));
			List<Delete> list = batch.get(tableName);
			Object[] delResult = new Object[list.size()];
			// table.batch(list, delResult);
			table.delete(list);
			table.close();
			rows += delResult.length;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format(
					"delete Hbase[table = '%s'] successfully, total: %d rows.",
					ftn, rows));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.dal.hbase.CrudOperate#get(java.lang.String, java.lang.String)
	 */
	@Override
	public BaseBean getRow(String className, String rowkey) throws Exception {
		BeanDescriptor bd = cache.get(className);
		@SuppressWarnings("unchecked")
		Class<BaseBean> clazz = (Class<BaseBean>) Class.forName(className);
		return get(bd, clazz, rowkey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.dal.hbase.CrudOperate#getNewest(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public BaseBean getRow(Class<? extends BaseBean> clazz, String rowKey)
			throws Exception {
		BeanDescriptor bd = cache.get(clazz);
		if (bd == null) {
			throw new Exception(String.format(
					"The BeanDescriptor[%s] is not exist.", clazz.getName()));
		}
		return get(bd, clazz, rowKey);
	}

	// 从Result中填充一个Bean
	private BaseBean fillBean(Result rs, BeanDescriptor bd,
			Class<? extends BaseBean> clazz) throws Exception {
		if (rs.isEmpty()) {
			return null;
		}
		BaseBean bean = clazz.newInstance();
		bean.setRowKey(Bytes.toString(rs.getRow()));
		for (Cell cell : rs.rawCells()) {
			String family = Bytes.toString(CellUtil.cloneFamily(cell));
			String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
			byte[] value = CellUtil.cloneValue(cell);
			HBaseUtils.set(bean,
					HBaseUtils.getFieldName(bd, family, qualifier), value);
		}
		return bean;
	}

	// 从hbase中获取指定的实体对象。
	private BaseBean get(BeanDescriptor bd, Class<? extends BaseBean> clazz,
			String rowKey) throws Exception {
		try (Table table = manager.getConnection().getTable(
				TableName.valueOf(bd.getFullTableName()))) {
			Get get = new Get(Bytes.toBytes(rowKey));
			Result rs = table.get(get);
			return fillBean(rs, bd, clazz);
		} catch (IOException ex) {
			throw new Exception(String.format(
					"Get the data[class=%s, rowkey=%s] fail.", clazz.getName(),
					rowKey), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.dal.hbase.CrudOperate#get(java.lang.Class)
	 */
	@Override
	public List<? extends BaseBean> getRows(Class<? extends BaseBean> clazz)
			throws Exception {
		BeanDescriptor bd = cache.get(clazz);
		if (bd == null) {
			throw new Exception(String.format(
					"The BeanDescriptor[%s] is not exist.", clazz.getName()));
		}
		List<BaseBean> result = new Vector<BaseBean>();
		try (Table table = manager.getConnection().getTable(
				TableName.valueOf(bd.getFullTableName()))) {
			Scan scan = new Scan();
			for (Family f : bd.getFamilies().values()) {
				scan.addFamily(f.getByteFamily());
			}
			ResultScanner rss = table.getScanner(scan);
			for (Result rs = rss.next(); rs != null; rs = rss.next()) {
				BaseBean bean = fillBean(rs, bd, clazz);
				if (bean != null) {
					result.add(bean);
				}
			}
			rss.close();
		} catch (IOException ex) {
			throw new Exception(String.format("Scan the data[class=%s] fail.",
					clazz.getName()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.dal.hbase.CrudOperate#get(java.lang.Class, long, long)
	 */
	@Override
	public List<? extends BaseBean> getRows(Class<? extends BaseBean> clazz,
			long ts1, long ts2) throws Exception {
		BeanDescriptor bd = cache.get(clazz);
		if (bd == null) {
			throw new Exception(String.format(
					"The BeanDescriptor[%s] is not exist.", clazz.getName()));
		}
		List<BaseBean> result = new Vector<BaseBean>();
		try (Table table = manager.getConnection().getTable(
				TableName.valueOf(bd.getFullTableName()))) {
			Scan scan = new Scan();
			for (Family f : bd.getFamilies().values()) {
				scan.addFamily(f.getByteFamily());
			}
			if (ts1 < 0) {
				ts1 = 0;
			}
			if (ts2 > 0) {
				ts2 = Long.MAX_VALUE;
			}
			scan.setTimeRange(ts1, ts2);
			ResultScanner rss = table.getScanner(scan);
			for (Result rs = rss.next(); rs != null; rs = rss.next()) {
				BaseBean bean = fillBean(rs, bd, clazz);
				if (bean != null) {
					result.add(bean);
				}
			}
			rss.close();
		} catch (IOException ex) {
			throw new Exception(String.format("Scan the data[class=%s] fail.",
					clazz.getName()));
		}
		return result;
	}

}
