/** 
 * @(#)UserBeanRepository.java
 *
 * Copyright 2015, 迪爱斯通信设备有限公司保留.<br>
 *
 * @author John Peng
 */
package com.yt.business.neo4j.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.yt.business.bean.UserAccountBean;

/**
 * 
 * @author Tony.Zhang
 *
 */
public interface UserAccountBeanRepository extends GraphRepository<UserAccountBean> {
	/**
	 * 获取指定账户的注册信息 
	 * @param userName
	 * @return
	 */
	@Query("MATCH (account:UserAccountBean) -[:BELONG] -> (profile:UserProfileBean) WHERE account.userName={0} RETURN account, profile")
	public UserTuple getUserAccountInfo(String userName);
}
