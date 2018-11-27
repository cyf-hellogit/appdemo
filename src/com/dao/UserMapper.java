package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.entity.AppInfo;
import com.entity.AppVersion;
import com.entity.BackendUser;
import com.entity.DevUser;

public interface UserMapper {
	//前台登录验证
	public DevUser denglu(@Param("username")String username,@Param("password")String password);
	//后台登录验证
	public BackendUser backdenglu(@Param("username")String username,@Param("password")String password);
	
	//分页查询
	public List<AppInfo> findList(@Param("softwareName")String softwareName,
			@Param("status")Integer status,
			@Param("categoryLevel1")Integer categoryLevel1,
			@Param("categoryLevel2")Integer categoryLevel2,
			@Param("categoryLevel3")Integer categoryLevel3,
			@Param("flatformId")Integer flatformId,
			@Param("devId")Integer devId,
			@Param("pageIndex")Integer pageIndex,
			@Param("pagesize")Integer pagesize);
	
	//分页总记录查询
	public int findCount(@Param("softwareName")String softwareName,
			@Param("status")Integer status,
			@Param("categoryLevel1")Integer categoryLevel1,
			@Param("categoryLevel2")Integer categoryLevel2,
			@Param("categoryLevel3")Integer categoryLevel3,
			@Param("flatformId")Integer flatformId,
			@Param("devId")Integer devId);
	
	//查看App界面
	public AppInfo getAppInfoById(@Param("id")Integer id);
	//查询版本
	public AppVersion getAppVersionById(@Param("id")Integer id);
	//审核
	public int updateAppInfo(@Param("id")Integer id ,@Param("status")Integer status);


}
