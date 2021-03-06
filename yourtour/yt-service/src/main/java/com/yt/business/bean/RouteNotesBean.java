package com.yt.business.bean;

import org.springframework.data.neo4j.annotation.NodeEntity;

import com.yt.business.BaseBeanImpl;
import com.yt.business.common.Constants;
import com.yt.hbase.annotation.HbaseColumn;
import com.yt.hbase.annotation.HbaseTable;
import com.yt.neo4j.annotation.Neo4jRelationship;
import com.yt.neo4j.annotation.Neo4jRelationship.Direction;

@HbaseTable(name = "T_ROUTE_NOTES")
@NodeEntity
public class RouteNotesBean extends BaseBeanImpl implements Cloneable{

	private static final long serialVersionUID = 5125188883896673886L;
	private static final String INDEX_NAME = "routeProvision";

	@HbaseColumn(name = "memo")
	private String 	memo; // 备注

	private long 	noteDate;

	private String 	images; //签到图片

	@Neo4jRelationship(relationship = Constants.RELATION_TYPE_HAS, type = UserProfileBean.class, direction = Direction.INCOMING)
	private transient UserProfileBean  user = null;

	public RouteNotesBean() {
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

	public UserProfileBean getUser() {
		return user;
	}

	public void setUser(UserProfileBean user) {
		this.user = user;
	}

	public long getNoteDate() {
		return noteDate;
	}

	public void setNoteDate(long noteDate) {
		this.noteDate = noteDate;
	}
}
