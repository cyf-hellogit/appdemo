package com.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.UserMapper;
import com.entity.AppInfo;
import com.entity.AppVersion;
import com.entity.BackendUser;
import com.entity.DevUser;
import com.service.UserService;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource(name="userMapper")
	private UserMapper userMapper;
	
	@Override
	public DevUser denglu(String username, String password) {
		// TODO Auto-generated method stub
		return userMapper.denglu(username, password);
	}

	@Override
	public BackendUser backdenglu(String username, String password) {
		// TODO Auto-generated method stub
		return userMapper.backdenglu(username, password);
	}

	@Override
	public List<AppInfo> findList(String softwareName, Integer status, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3, Integer flatformId, Integer devId, Integer pageIndex, Integer pagesize) {
		// TODO Auto-generated method stub
		return userMapper.findList(softwareName, status, categoryLevel1, categoryLevel2, categoryLevel3, flatformId, devId, pageIndex, pagesize);
	}

	@Override
	public int findCount(String softwareName, Integer status, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3, Integer flatformId, Integer devId) {
		// TODO Auto-generated method stub
		return userMapper.findCount(softwareName, status, categoryLevel1, categoryLevel2, categoryLevel3, flatformId, devId);
	}

	@Override
	public AppInfo getAppInfoById(Integer id) {
		// TODO Auto-generated method stub
		return userMapper.getAppInfoById(id);
	}

	@Override
	public AppVersion getAppVersionById(Integer id) {
		// TODO Auto-generated method stub
		return userMapper.getAppVersionById(id);
	}

	@Override
	public int updateAppInfo(Integer id, Integer status) {
		// TODO Auto-generated method stub
		return userMapper.updateAppInfo(id, status);
	}

}
