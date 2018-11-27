package com.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.entity.AppCategory;
import com.entity.AppInfo;
import com.entity.AppVersion;
import com.entity.DataDictionary;
import com.entity.DevUser;
import com.interceptor.Constants;
import com.interceptor.PageSupport;
import com.mysql.jdbc.StringUtils;
import com.service.AppInfoService;
import com.service.UserService;


@Controller
@RequestMapping("/dev")
public class UserController {
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="appInfoService")
	private AppInfoService appInfoService;
	
	@RequestMapping("/login")
	public String index(){
		return "devlogin";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "devlogin";
	}
	
	@RequestMapping("/dologin")
	public String dologin(@RequestParam String devCode,
			  @RequestParam String devPassword,
			   HttpSession session,
			   HttpServletRequest request){
		DevUser user=userService.denglu(devCode, devPassword);
		if (user !=null) {
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/dev/main";
		}else {
			request.setAttribute("error","用户名或密码不正确");
			return "devlogin";	
		}
	}
	
	@RequestMapping("/main")
	public String main(HttpSession session) {
		if(session.getAttribute(Constants.USER_SESSION)==null) {
			return "devlogin";
		}
		return "/developer/main";
	}
	
	//分页查询
	@RequestMapping("/list")
	public String list(@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) Integer queryStatus,
			@RequestParam(value="queryCategoryLevel1",required=false) Integer queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) Integer queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) Integer queryCategoryLevel3,
			@RequestParam(value="queryFlatformId",required=false) Integer queryFlatformId,
			@RequestParam(value="devId",required=false) Integer devId,
			@RequestParam(value="pageIndex",required=false) String pageIndex,Model model) {
		List<AppInfo> list=new ArrayList<AppInfo>();
		int pagesize=5;
		int currentPageNo=1;
		if (querySoftwareName==null) {
			querySoftwareName="";
		}
//		if (status==null) {
//			status="";
//		}
//		if (categoryLevel1==null) {
//			categoryLevel1="";
//		}
//		if (categoryLevel2==null) {
//			categoryLevel2="";
//		}
//		if (categoryLevel3==null) {
//			categoryLevel3="";
//		}
//		if (flatformId==null) {
//			flatformId="";
//		}
//		if (devId==null) {
//			devId="";
//		}
		if (pageIndex!=null) {
			currentPageNo=Integer.parseInt(pageIndex);
		}
		int totalCount=appInfoService.findCount(querySoftwareName, queryStatus, queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,devId);
		
		//总页数
		PageSupport pageSupport=new PageSupport();
		pageSupport.setCurrentPageNo(currentPageNo);
		pageSupport.setPageSize(pagesize);
		pageSupport.setTotalCount(totalCount);
		pageSupport.setTotalPageCountByRs();
		int totalPageCount=pageSupport.getTotalPageCount();
		//totalPageCount = totalCount%pagesize==0 ?totalCount/pagesize:totalCount/pagesize+1;
		
		if (currentPageNo<1) {
			currentPageNo=1;
		}else if (currentPageNo>totalPageCount) {
			currentPageNo=totalPageCount;
		}
		list=appInfoService.findList(querySoftwareName,queryStatus, queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,devId,(pageSupport.getCurrentPageNo()-1)*pagesize, pagesize);
		System.out.println(list.size());
		List<DataDictionary> statusList = appInfoService.findZT();//状态
		List<DataDictionary> flatFormList = appInfoService.findPT();//平台
		List<AppCategory> categoryLevel1List = appInfoService.findFLONE();//一级分类
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("appInfoList", list);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		model.addAttribute("pages", pageSupport);
		return "/developer/appinfolist";
	}
	
	//动态加载下拉框
	@RequestMapping(value = "/categorylevellist.json",produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object categorylevellist(@RequestParam String pid) {
		if(pid==null) {
			pid="";
		}
		List<AppCategory> list = appInfoService.findFLTHREE(pid==""?null:Integer.parseInt(pid));
		return JSONArray.toJSONString(list);
	}
	
	//调到增加基本信息页面
	@RequestMapping("/appinfoadd")
	public String appinfoadd() {
		return "/developer/appinfoadd";
	}
	
	//加载下拉框
	@RequestMapping(value = "/datadictionarylist.json",produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object datadictionarylist() {
		List<DataDictionary> flatFormList = appInfoService.findPT();//平台
		return JSONArray.toJSONString(flatFormList);
	}
	
	//增加基本信息
	@RequestMapping("/appinfoaddsave")
	public String appinfoaddsave(AppInfo appinfo,HttpSession session,HttpServletRequest request,@RequestParam(value="a_logoPicPath")MultipartFile aFile) {
		String logoLocPath = null;
		//判断文件是否为空
		if(!aFile.isEmpty()) {
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = aFile.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(aFile.getSize()>filesize) {
				request.setAttribute("uploadFileError", "上传大小不能超过500KB");
				return "/developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg")||prefix.equalsIgnoreCase("png")||prefix.equalsIgnoreCase("jpeg")||prefix.equalsIgnoreCase("pneg")) {
				String fileName = System.currentTimeMillis()+"_Personal.kpg";
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					aFile.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "上传失败");
					return "/developer/appinfoadd";
				}
				logoLocPath = path+File.separator+fileName;
			}else {
				request.setAttribute("uploadFileError", "上传的图片格式错误");
				return "/developer/appinfoadd";
			}
			
		}
		appinfo.setCreatedBy(((DevUser)session.getAttribute(Constants.USER_SESSION)).getId());
		appinfo.setCreationDate(new Date());
		appinfo.setLogoPicPath(logoLocPath);
		appinfo.setLogoLocPath(logoLocPath);
		appinfo.setDevId(((DevUser)session.getAttribute(Constants.USER_SESSION)).getId());
		appinfo.setStatus(1);
		if(appInfoService.Addappinfo(appinfo)>0) {
			return "redirect:/dev/list";
		}
		return "/developer/appinfoadd";
	}
	
	//判断APKNAME是否重复
	@RequestMapping(value = "/apkexist.json",produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object apkexist(@RequestParam String APKName) {
		HashMap<String, String> resultmap = new HashMap<String,String>();
		List<AppInfo> list = appInfoService.isok(APKName);
		if(StringUtils.isNullOrEmpty(APKName)) {
			resultmap.put("APKName", "empty");
		}else if(list.size()>0) {
			resultmap.put("APKName", "exist");
		}else {
			resultmap.put("APKName", "noexist");
		}
		return JSONArray.toJSONString(resultmap);
	}
	
	//跳转修改基本信息界面
	@RequestMapping("/appinfomodify")
	public String appinfomodify(@RequestParam String id,Model model) {
		AppInfo appInfo = appInfoService.GetByappId(Integer.parseInt(id));
		model.addAttribute("appInfo", appInfo);
		return "/developer/appinfomodify";
	}
	
	//修改基本信息
	@RequestMapping("/appinfomodifysave")
	public String appinfomodifysave(AppInfo appInfo) {
		appInfoService.updateAppInfo(appInfo);
		return "redirect:/dev/list";
	}
	
	//进入新增版本页面
	@RequestMapping("/appversionadd")
	public String appversionadd(String id,Model model) {
		List<AppVersion> list = appInfoService.getversionList(Integer.parseInt(id));
		AppVersion appVersion = new AppVersion();
		appVersion.setAppId(Integer.parseInt(id));
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appVersionList", list);
		return "/developer/appversionadd";
	}
	
	//新增版本
	@RequestMapping("/addversionsave")
	public String addversionsave(AppVersion aVersion,Integer appId,HttpSession session,HttpServletRequest request,@RequestParam(value="a_downloadLink")MultipartFile aFile) {
		String logoLocPath = null;
		//判断文件是否为空
		if(!aFile.isEmpty()) {
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = aFile.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(aFile.getSize()>filesize) {
				request.setAttribute("uploadFileError", "上传大小不能超过500KB");
				return "/developer/appversionadd";
			}else if(prefix.equalsIgnoreCase("apk")) {
				String fileName = System.currentTimeMillis()+"_Personal.apk";
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					aFile.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "上传失败");
					return "/developer/appversionadd";
				}
				logoLocPath = path+File.separator+fileName;
			}else {
				request.setAttribute("uploadFileError", "上传的图片格式错误");
				return "/developer/appversionadd";
			}
			
		}
		aVersion.setApkLocPath(logoLocPath);
		aVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.USER_SESSION)).getId());
		aVersion.setCreationDate(new Date());
		aVersion.setModifyBy(((DevUser)session.getAttribute(Constants.USER_SESSION)).getId());
		aVersion.setModifyDate(new Date());
		if(appInfoService.versionadd(aVersion)>0) {
			AppVersion zid = appInfoService.selectvv(appId);
			appInfoService.updateappinfoversion(zid.getId(),appId);
			return "redirect:/dev/list";
		}
		return "/developer/appversionadd";
	}
	
	//跳转修改版本界面并传值过去
	@RequestMapping("/appversionmodify")
	public String appversionmodify(Integer vid,Integer aid,Model model) {
		List<AppVersion> list = appInfoService.getversionList(aid);
		AppVersion appVersion = appInfoService.selectvv(aid);
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appVersionList", list);
		return "/developer/appversionmodify";
	}
	
	//修改版本信息
	@RequestMapping("/appversionmodifysave")
	public String appversionmodifysave(AppVersion aVersion) {
		appInfoService.updateAppVersion(aVersion);
		return "redirect:/dev/list";
	}
	
	//查看的方法
	@RequestMapping("/appview/{appinfoid}")
	public String appview(@PathVariable Integer appinfoid,Model model) {
		List<AppVersion> list = appInfoService.getversionList(appinfoid);
		model.addAttribute("appVersionList", list);
		AppInfo appInfo = appInfoService.selectAll(appinfoid);
		model.addAttribute("appInfo", appInfo);
		return "/developer/appinfoview";
	}
	
	
	//删除
	@RequestMapping(value = "/delapp.json",produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object delapp(@RequestParam Integer id) {
		int nums = appInfoService.delApp(id);
		HashMap<String, String> say = new HashMap<String, String>();
		if(nums > 0) {
			say.put("delResult", "true");
		}else if(id==null) {
			say.put("delResult", "notexist");
		}else {
			say.put("delResult", "notexist");
		}
		return JSONArray.toJSONString(say);
	}
	
}
