package com.yt.vo.route;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.yt.business.bean.RouteMainBean;
import com.yt.core.utils.DateUtils;
import com.yt.core.utils.StringUtils;
import com.yt.vo.member.UserBriefVO;
import org.apache.hadoop.hbase.security.User;

public class RouteItemVO implements Serializable{
	private static final long serialVersionUID = 8326191052746173505L;

	private RouteMainBean route;
	
	public RouteItemVO(RouteMainBean route){
		this.route = route;
	}
	
	public Long getId(){
		return route.getGraphId();
	}
	
	public String getName() {
		return route.getName();
	}

	public String getLineName(){
		return StringUtils.isNull(route.getLineName())?route.getName():route.getLineName();
	}

	public String getStartDate() {
		return DateUtils.formatDate(route.getStartDate());
	}

	public String getEndDate(){
		return DateUtils.formatDate(route.getEndDate());
	}

	public String getFromPlace(){return route.getFromPlace();}

	public String getToPlaces(){return route.getToPlaces();}

	public String getDuration(){
		return String.valueOf(((route.getEndDate() - route.getStartDate())/(1000 * 3600 * 24)));
	}

	public String getImageUrl(){
		return route.getImageUrl();
	}

	public String getImpression(){
		return route.getImpression();
	}

	public int getStep(){return route.getStep();}
}