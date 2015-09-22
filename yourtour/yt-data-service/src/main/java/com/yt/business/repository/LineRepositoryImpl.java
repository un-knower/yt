package com.yt.business.repository;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yt.business.bean.LineBean;
import com.yt.business.bean.PlaceBean;
import com.yt.business.bean.SceneResourceBean;
import com.yt.business.common.Constants.NodeRelationshipEnum;
import com.yt.business.neo4j.repository.LineBeanRepository;
import com.yt.business.neo4j.repository.LinePlaceTuple;
import com.yt.business.neo4j.repository.RouteBeanRepository;
import com.yt.business.neo4j.repository.SceneResourceBeanRepository;
import com.yt.business.utils.Neo4jUtils;
import com.yt.rsal.neo4j.bean.INeo4JBaseBean;
import com.yt.rsal.neo4j.bean.Neo4JBaseBean;
import com.yt.rsal.neo4j.repository.CrudGeneralOperate;
import com.yt.rsal.neo4j.repository.IFullTextSearchOperate;
import com.yt.rsal.neo4j.repository.IFullTextSearchOperate.QueryTerm;

@Component
public class LineRepositoryImpl extends CrudGeneralOperate implements
		LineRepository {
	private static final Log LOG = LogFactory.getLog(LineRepositoryImpl.class);

	@Autowired
	private SceneResourceBeanRepository sceneRepo;

	@Autowired
	private IFullTextSearchOperate ftsOperate;

	@Autowired
	private RouteBeanRepository routeRepo;

	@Autowired
	private LineBeanRepository lineRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.LineRepository#getLineByGraphId(java.lang.
	 * Long)
	 */
	@Override
	public LineBean getLineByGraphId(Long graphId) throws Exception {
		LinePlaceTuple tuple = lineRepo.getLineByGraphId(graphId);
		LineBean line = null;
		if (tuple != null) {
			line = tuple.getLine();
			line.setPlace(tuple.getPlace());
		}
		return line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.rsal.neo4j.repository.CrudGeneralOperate#save(com.yt.rsal.neo4j
	 * .bean.INeo4JBaseBean, java.lang.String, boolean)
	 */
	@Override
	public INeo4JBaseBean save(INeo4JBaseBean neo4jBean, String operator,
			boolean saveFail2Hbase) throws Exception {
		LineBean bean = (LineBean) super.save(neo4jBean, operator,
				saveFail2Hbase);
		LineBean line = (LineBean) neo4jBean;
		if (line.getPlace() != null && line.getPlace().getGraphId() != null) {
			// 建立线路到目的地的关系
			PlaceBean place = super.template.findOne(line.getPlace()
					.getGraphId(), PlaceBean.class);
			Neo4jUtils.maintainRelation(super.template,
					NodeRelationshipEnum.AT, line, place, null, true, false);
		}
		for (SceneResourceBean scene : line.getScenes()) {
			// 建立线路到景点的关系
			if (scene == null) {
				continue;
			}
			SceneResourceBean sceneGet = super.template.findOne(
					scene.getGraphId(), SceneResourceBean.class);
			if (sceneGet == null) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(String
							.format("The scene not exist, the relation: Line[%d]-[:CONTAIN]->Scene[%d] can not be created.",
									line.getGraphId(), scene.getGraphId()));
				}
				continue;
			}
			Neo4jUtils.maintainRelation(super.template,
					NodeRelationshipEnum.CONTAIN, line, sceneGet, null, true,
					false);
		}
		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.LineRepository#containScene(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void containScene(String lineId, String sceneId) throws Exception {
		NodeRelationshipEnum relationship = NodeRelationshipEnum.CONTAIN;
		Neo4jUtils.maintainRelateion(super.template, this, relationship,
				lineId, LineBean.class, sceneId, SceneResourceBean.class, null,
				true, false);
		if (LOG.isDebugEnabled()) {
			LOG.debug(String
					.format("Create a relationship: LineBean[%s] =%s=> SceneResourceBean[%s].",
							lineId, relationship.name(), sceneId));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.LineRepository#uncontainScene(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public void uncontainScene(String lineId, String sceneId) throws Exception {
		NodeRelationshipEnum relationship = NodeRelationshipEnum.CONTAIN;
		Neo4jUtils.maintainRelateion(super.template, this, relationship,
				lineId, LineBean.class, sceneId, SceneResourceBean.class, null,
				false, false);
		if (LOG.isDebugEnabled()) {
			LOG.debug(String
					.format("Remove a relationship: LineBean[%s] =%s=> SceneResourceBean[%s].",
							lineId, relationship.name(), sceneId));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.business.repository.LineRepository#getLinesByPage(int, int)
	 */
	@Override
	public List<LineBean> getLinesByPage(int start, int limit) throws Exception {
		List<LinePlaceTuple> result = lineRepo.getLinesByPage(start, limit);
		List<LineBean> list = new Vector<LineBean>(result.size());
		for (LinePlaceTuple tuple : result) {
			if (tuple == null) {
				continue;
			}
			LineBean line = tuple.getLine();
			line.setPlace(tuple.getPlace());
			list.add(line);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.LineRepository#queryRecommandLine(java.lang
	 * .String[], int, java.lang.String[])
	 */
	@Override
	public List<LineBean> queryRecommandLine(String[] places, int dayNum,
			String[] scenes) throws Exception {
		// 不支持全文检索的查询
		int min = dayNum - 1, max = dayNum + 1;
		min = min >= 0 ? min : 0;
		min *= 24 * 3600;
		max *= 24 * 3600; // 换算为秒

		List<LineBean> recommendLines = routeRepo.query(places, min, max,
				scenes);
		return recommendLines;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.LineRepository#queryRecommandLine2(java.lang
	 * .String[], int, java.lang.String[])
	 */
	public List<LineBean> queryRecommandLine2(String[] places, int dayNum,
			String[] scenes) throws Exception {
		// 首先对景点进行全文检索
		List<QueryTerm> terms = new Vector<QueryTerm>();
		for (String scene : scenes) {
			terms.add(new QueryTerm("name", scene));
		}
		List<Neo4JBaseBean> sceneBeans = ftsOperate.query(
				SceneResourceBean.class, terms, false);
		long[] ids = new long[sceneBeans.size()];
		for (int index = 0; index < ids.length; index++) {
			ids[index] = sceneBeans.get(index).getGraphId();
		}

		// 支持景点全文检索
		int min = dayNum - 1, max = dayNum + 1;
		min = min >= 0 ? min : 0;
		min *= 24 * 3600;
		max *= 24 * 3600; // 换算为秒

		List<LineBean> recommendLines = routeRepo.query(places, min, max, ids);
		return recommendLines;
	}
}