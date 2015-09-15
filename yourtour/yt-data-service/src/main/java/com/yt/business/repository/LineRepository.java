package com.yt.business.repository;

import java.util.List;

import com.yt.business.bean.LineBean;
import com.yt.rsal.neo4j.repository.ICrudOperate;

public interface LineRepository extends ICrudOperate {

	public LineBean getLineByGraphId(Long graphId) throws Exception;

	public void containScene(String lineId, String sceneId) throws Exception;

	public void uncontainScene(String lineId, String sceneId) throws Exception;

	public List<LineBean> getLinesByPage(int start, int limit) throws Exception;

	public List<LineBean> queryRecommandLine(String[] places, int dayNum,
			String[] scenes) throws Exception;

	public List<LineBean> queryRecommandLine2(String[] places, int dayNum,
			String[] scenes) throws Exception;
}