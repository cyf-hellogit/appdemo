package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.entity.AppCategory;
import com.entity.AppInfo;
import com.entity.AppVersion;
import com.entity.DataDictionary;

public interface AppInfoMapper {
	//分页查询
	public List<AppInfo> findList(@Param("softwareName")String softwareName,
			@Param("status")Integer status,@Param("categoryLevel1")Integer categoryLevel1,
			@Param("categoryLevel2")Integer categoryLevel2,@Param("categoryLevel3")Integer categoryLevel3,
			@Param("flatformId")Integer flatformId,@Param("devId")Integer devId,@Param("pageIndex")Integer pageIndex,@Param("pagesize")Integer pagesize);
	
	//分页总记录查询
	public int findCount(@Param("softwareName")String softwareName,
			@Param("status")Integer status,@Param("categoryLevel1")Integer categoryLevel1,
			@Param("categoryLevel2")Integer categoryLevel2,@Param("categoryLevel3")Integer categoryLevel3,
			@Param("flatformId")Integer flatformId,@Param("devId")Integer devId);
	
	
	//查询状态下拉框
	public List<DataDictionary> findZT();
	
	//查询所属平台的下拉框
	public List<DataDictionary> findPT();
	
	//查询一级分类
	public List<AppCategory> findFLONE();
	
	//查询二级分类
	public List<AppCategory> findFLTWO(@Param("parentId")Integer parentId);
	
	//查询二级分类
	public List<AppCategory> findFLTHREE(@Param("parentId")Integer parentId);
	
	//添加信息
	public int Addappinfo(AppInfo appinfo);
	
	//验证APK的方法
	public List<AppInfo> isok(@Param("APKName")String APKName);
	
	//修改基本信息的方法
	public int updateAppInfo(AppInfo appInfo);
	
	//按ID传值的方法
	public AppInfo GetByappId(@Param("id")Integer id);
	
	//新增版本的方法
	public int versionadd(AppVersion aVersion);
	
	//查询版本集合
	public List<AppVersion> getversionList(@Param("id")Integer id);
	
	//更新最新版本信息
	public int updateappinfoversion(@Param("versionId")Integer versionId,@Param("id")Integer id);
	
	//查询最新版本的id
	public AppVersion selectvv(@Param("appId")Integer appId);
	
	//修改版本信息
	public int updateAppVersion(AppVersion aVersion);
	
	//查看
	public AppInfo selectAll(@Param("id")Integer id);
	
	//删除
	public int delApp(@Param("id")Integer id);
	
	//删除子表的版本信息
	public int delAppChid(@Param("id")Integer id);
}
