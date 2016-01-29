package com.yt.business.bean;

import com.yt.business.BaseBeanImpl;
import com.yt.business.common.Constants;
import com.yt.core.utils.DateUtils;
import com.yt.hbase.annotation.HbaseColumn;
import com.yt.hbase.annotation.HbaseTable;
import com.yt.neo4j.annotation.Neo4jRelationship;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@HbaseTable(name = "T_ROUTE_CHECKIN")
@NodeEntity
public class RouteCheckinBean extends BaseBeanImpl implements Cloneable{

	private static final long serialVersionUID = 5125188883896673886L;
	private static final String INDEX_NAME = "routeProvision";

	@HbaseColumn(name = "memo")
	private String 	memo; // 备注

	private long 	checkinDate;

	private String 	images; //签到图片

	@Neo4jRelationship(relationship = Constants.RELATION_TYPE_AT, type = RouteActivityBean.class, direction = Direction.OUTGOING)
	private transient RouteActivityBean activity = null; // 行程准备关联的行程

	@Neo4jRelationship(relationship = Constants.RELATION_TYPE_HAS, type = UserProfileBean.class, direction = Direction.INCOMING)
	private transient UserProfileBean  user = null;

	public RouteCheckinBean() {
		super();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public RouteActivityBean getActivity() {
		return activity;
	}

	public void setActivity(RouteActivityBean activity) {
		this.activity = activity;
	}

	public UserProfileBean getUser() {
		return user;
	}

	public void setUser(UserProfileBean user) {
		this.user = user;
	}

	public long getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(long checkinDate) {
		this.checkinDate = checkinDate;
	}
}
