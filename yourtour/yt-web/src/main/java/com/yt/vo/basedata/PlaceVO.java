package com.yt.vo.basedata;

import java.util.List;
import java.util.Vector;

import com.yt.business.bean.PlaceBean;
import com.yt.business.common.Constants.Status;
import com.yt.core.utils.CollectionUtils;

public class PlaceVO {
	private Long graphId = -1l, parentId = null;
	private String parentCode, code, shorter, text, memo, imageUrl, name;
	private boolean expandable = false, leaf = false;
	private Status status = Status.ACTIVED;
	private int followedNum = 0; //关注人数
	private int goneNum = 0;  //去过人数
	private int goingNum = 0;  //想去人数
	private int num = 0;  //下辖目的地个数

	public static PlaceBean transform(PlaceVO vo) {
		if (vo == null) {
			return null;
		}
		PlaceBean bean = new PlaceBean();
		bean.setCode(vo.getCode());
		if (vo.getGraphId() != null && vo.getGraphId().longValue() != -1l) {
			bean.setGraphId(vo.getGraphId());
		}
		bean.setName(vo.getText());
		bean.setMemo(vo.getMemo());
		bean.setShorter(vo.getShorter());
		bean.setStatus(vo.getStatus());
		if (vo.getParentId() != null && vo.getParentId().longValue() != -1l) {
			PlaceBean parent = new PlaceBean();
			parent.setGraphId(vo.getParentId());
			bean.setParent(parent);
		}
		return bean;
	}

	public static PlaceVO transform(PlaceBean bean) {
		if (bean == null) {
			return null;
		}

		PlaceVO vo = new PlaceVO();
		vo.setCode(bean.getCode());
		vo.setId(bean.getGraphId());
		vo.setLeaf(bean.isLeaf());
		vo.setName(bean.getName());
		vo.setMemo(bean.getMemo());
		vo.setShorter(bean.getShorter());
		vo.setStatus(bean.getStatus());
		vo.setText(bean.getName());
		vo.setImageUrl(bean.getImageUrl());
		vo.setGoingNum(bean.getGoingNum());
		vo.setGoneNum(bean.getGoneNum());
		vo.setFollowedNum(bean.getFollowedNum());
		vo.setNum(bean.getSubs().size());
		vo.setExpandable(CollectionUtils.isNotEmpty(bean.getSubs()));

		if(bean.getParent() != null){
			vo.setParentCode(bean.getParent().getCode());
		}
		return vo;
	}

	public PlaceVO() {
		super();
	}

	public Long getGraphId() {
		return graphId;
	}

	public Long getId() {
		return graphId;
	}

	public void setId(Long graphId) {
		this.graphId = graphId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getShorter() {
		return shorter;
	}

	public void setShorter(String shorter) {
		this.shorter = shorter;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isExpandable() {
		return expandable;
	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getFollowedNum() {
		return followedNum;
	}

	public void setFollowedNum(int followedNum) {
		this.followedNum = followedNum;
	}

	public int getGoneNum() {
		return goneNum;
	}

	public void setGoneNum(int goneNum) {
		this.goneNum = goneNum;
	}

	public int getGoingNum() {
		return goingNum;
	}

	public void setGoingNum(int goingNum) {
		this.goingNum = goingNum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setName(String name) {
		this.name = name;
	}


}
