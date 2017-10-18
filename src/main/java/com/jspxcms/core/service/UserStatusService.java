package com.jspxcms.core.service;

import java.util.List;

import com.jspxcms.core.domain.UserStatus;

/**
 * OrderService
 * 
 * @author liufang
 * 
 */
public interface UserStatusService {

	public UserStatus save(UserStatus userStatus);

	public UserStatus get(Integer id);
	
	public UserStatus getByMacAddress(String macAddress);

	public void update(UserStatus bean);

	public void delete(Integer id);

	public List<UserStatus> getListById(Integer id);

	
}
