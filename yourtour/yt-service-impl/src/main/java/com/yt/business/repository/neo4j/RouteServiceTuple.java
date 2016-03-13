package com.yt.business.repository.neo4j;

import com.yt.business.bean.*;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

@QueryResult
public class RouteServiceTuple {
	@ResultColumn("routeService")
	private RouteServiceBean routeService;

	@ResultColumn("expertService")
	private ExpertServiceBean expertService;

	@ResultColumn("user")
	private UserProfileBean  user;

	public RouteServiceTuple(){
	}

	public RouteServiceBean getRouteService() {
		routeService.setService(this.getExpertService());
		return routeService;
	}

	public void setRouteService(RouteServiceBean routeService) {
		this.routeService = routeService;
	}

	public ExpertServiceBean getExpertService() {
		expertService.setUser(user);
		return expertService;
	}

	public void setExpertService(ExpertServiceBean expertService) {
		this.expertService = expertService;
	}

	public UserProfileBean getUser() {
		return user;
	}

	public void setUser(UserProfileBean user) {
		this.user = user;
	}
}