package com.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.AppInfoMapper;
import com.entity.AppCategory;
import com.entity.AppInfo;
import com.entity.AppVersion;
import com.entity.DataDictionary;
import com.service.AppInfoService;

@Transactional
@Service("appInfoService")
public class AppInfoServiceImpl implements AppInfoService{
	
	@Resource(name="appInfoMapper")
	private AppInfoMapper appInfoMapper;
	
	@Override
	public List<AppInfo> findList(String softwareName, Integer status, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3, Integer flatformId, Integer devId, int pageIndex, int pagesize) {
		// TODO Auto-generated method stub
		return appInfoMapper.findList(softwareName, status, categoryLevel1, categoryLevel2, categoryLevel3, flatformId, devId, pageIndex, pagesize);
	}

	@Override
	public int findCount(String softwareName, Integer status, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3, Integer flatformId, Integer devId) {
		// TODO Auto-generated method stub
		return appInfoMapper.findCount(softwareName, status, categoryLevel1, categoryLevel2, categoryLevel3, flatformId, devId);
	}

	@Override
	public List<DataDictionary> findZT() {
		// TODO Auto-generated method stub
		return appInfoMapper.findZT();
	}

	@Override
	public List<DataDictionary> findPT() {
		// TODO Auto-generated method stub
		return appInfoMapper.findPT();
	}

	@Override
	public List<AppCategory> findFLONE() {
		// TODO Auto-generated method stub
		return appInfoMapper.findFLONE();
	}

	@Override
	public List<AppCategory> findFLTWO(Integer parentId) {
		// TODO Auto-generated method stub
		return appInfoMapper.findFLTWO(parentId);
	}

	@Override
	public List<AppCategory> findFLTHREE(Integer parentId) {
		// TODO Auto-generated method stub
		return appInfoMapper.findFLTHREE(parentId);
	}

	@Override
	public int Addappinfo(AppInfo appinfo) {
		// TODO Auto-generated method stub
		return appInfoMapper.Addappinfo(appinfo);
	}

	@Override
	public List<AppInfo> isok(String APKName) {
		// TODO Auto-generated method stub
		return appInfoMapper.isok(APKName);
	}

	@Override
	public int updateAppInfo(AppInfo appInfo) {
		// TODO Auto-generated method stub
		return appInfoMapper.updateAppInfo(appInfo);
	}

	@Override
	public AppInfo GetByappId(Integer id) {
		// TODO Auto-generated method stub
		return appInfoMapper.GetByappId(id);
	}

	@Override
	public int versionadd(AppVersion aVersion) {
		// TODO Auto-generated method stub
		return appInfoMapper.versionadd(aVersion);
	}

	@Override
	public List<AppVersion> getversionList(Integer id) {
		// TODO Auto-generated method stub
		return appInfoMapper.getversionList(id);
	}

	@Override
	public int updateappinfoversion(Integer versionId, Integer id) {
		// TODO Auto-generated method stub
		return appInfoMapper.updateappinfoversion(versionId, id);
	}

	@Override
	public AppVersion selectvv(Integer appId) {
		// TODO Auto-generated method stub
		return appInfoMapper.selectvv(appId);
	}

	@Override
	public int updateAppVersion(AppVersion aVersion) {
		// TODO Auto-generated method stub
		return appInfoMapper.updateAppVersion(aVersion);
	}

	@Override
	public AppInfo selectAll(Integer id) {
		// TODO Auto-generated method stub
		return appInfoMapper.selectAll(id);
	}

	@Override
	public int delApp(Integer id) {
		appInfoMapper.delAppChid(id);//先删除他子表的信息
		return appInfoMapper.delApp(id);
	}
	
	

}
