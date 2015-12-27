package com.yt.vo.route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.yt.business.bean.ResourceBean;
import com.yt.business.bean.RouteActivityBean;
import com.yt.business.bean.RouteMainBean;
import com.yt.business.bean.RouteProvisionBean;
import com.yt.business.bean.RouteScheduleBean;
import com.yt.core.utils.CollectionUtils;
import com.yt.core.utils.DateUtils;
import com.yt.core.utils.StringUtils;

public class RouteLoadVO implements Serializable {
	private static final long serialVersionUID = -5772201577521181151L;

	private static enum TYPE{Provision, ProvisionItem, Schedule, ScheduleItem};
	
	private RouteMainBean route;
	
	public RouteLoadVO(RouteMainBean route) {
		this.route = route;
	}
	
	public String getId(){
		return this.route.getGraphId().toString();
	}
	
	public String getName() {
		return route.getName();
	}

	public String getLineName() {
		return route.getLineName();
	}

	public String getImageUrl() {
		//return route.getImageUrl();
		
		return "/resources/images/yunnan.jpg";
	}
	
	public List<RouteSchedule> getSchedules(){
		List<RouteSchedule> schedules = new ArrayList<>();
		
		schedules.addAll(this.getProvisions());
		
		schedules.addAll(this.getActivities());
		
		return schedules;
	}
	
	private List<RouteSchedule> getProvisions(){
		List<RouteSchedule> provisions = new ArrayList<>();
		
		RouteSchedule group = new RouteSchedule();
		group.setId("0");
		group.setTitle("准备事项");
		group.setType(TYPE.Provision);
		provisions.add(group);
		
		for(RouteProvisionBean provisionBean : this.route.getProvisions()){
			RouteSchedule provision = new RouteSchedule();
			provision.setParentId(group.getId());
			provision.setTitle(provisionBean.getTitle());
			provision.setMemo(provisionBean.getMemo());
			provision.setType(TYPE.ProvisionItem);
			provision.setIndex(provisionBean.getIndex());
			provision.setId(provisionBean.getGraphId().toString());
			provisions.add(provision);
		}
		return provisions;
	}
	
	private List<RouteSchedule> getActivities(){
		List<RouteSchedule> schedules = new ArrayList<>();
		
		for(RouteScheduleBean scheduleBean : this.route.getSchedules()){
			RouteSchedule group = new RouteSchedule();
			group.setId(scheduleBean.getGraphId().toString());
			group.setTitle(DateUtils.formatDate(scheduleBean.getDate()));
			group.setStartTime(DateUtils.formatDate(scheduleBean.getDate()));
			group.setPlaces(scheduleBean.getPlaces());
			group.setMemo(scheduleBean.getMemo());
			group.setType(TYPE.Schedule);
			group.setDate(scheduleBean.getDate());
			schedules.add(group);
			
			if(CollectionUtils.isNotEmpty(scheduleBean.getActivities())){
				for(RouteActivityBean activityBean : scheduleBean.getActivities()){
					RouteSchedule activity = new RouteSchedule();
					activity.setTitle(activityBean.getTitle());
					activity.setMemo(activityBean.getMemo());
					activity.setType(TYPE.ScheduleItem);
					activity.setParentId(group.getId());
					activity.setId(activityBean.getGraphId().toString());
					activity.setStartTime(activityBean.getStartTime());
					activity.setEndTime(activityBean.getEndTime());
					activity.setIndex(activityBean.getIndex());
					
					ResourceBean resource = activityBean.getResource();
					if(resource != null){
						activity.setResourceId(resource.getGraphId().toString());
						activity.setResourceType(resource.getType().toString());
					}
					
					schedules.add(activity);
				}
			}
		}
		
		return schedules;
	}
	
	private class RouteSchedule{
		private String 	id;
		private String  parentId;
		private String  resourceId;
		private String  resourceType;
		private String 	title;
		private String	memo;
		private String  places;
		private TYPE 	type;
		private String 	status;
		private long    date;
		private String 	startTime;
		private String 	endTime;
		private String 	address;
		private int 	index;
		
		public RouteSchedule(){}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getParentId() {
			return parentId;
		}

		public void setParentId(String parentId) {
			this.parentId = parentId;
		}

		public String getResourceId() {
			return resourceId;
		}

		public void setResourceId(String resourceId) {
			this.resourceId = resourceId;
		}

		public String getResourceType() {
			return resourceType;
		}

		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getType() {
			return type.toString();
		}

		public void setType(TYPE type) {
			this.type = type;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public long getDate() {
			return date;
		}

		public void setDate(long date) {
			this.date = date;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getPlaces() {
			return places;
		}

		public void setPlaces(String places) {
			this.places = places;
		}

		public String getMemo() {
			return StringUtils.isNull(memo)?"":memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}
		
	}
}