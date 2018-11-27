package com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.entity.AppCategory;
import com.entity.AppInfo;
import com.entity.AppVersion;
import com.entity.BackendUser;
import com.entity.DataDictionary;
import com.interceptor.Constants;
import com.interceptor.PageSupport;
import com.service.AppInfoService;
import com.service.UserService;

@Controller
@RequestMapping("/manager")
public class AdminController {
	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping("/login")
	public String index(){
		return "backendlogin";
	}
	
	@Resource(name="appInfoService")
	private AppInfoService appInfoService;
	
	@RequestMapping("/dologin")
	public String dologin(@RequestParam String userCode,
			  @RequestParam String userPassword,
			   HttpSession session,
			   HttpServletRequest request){
		BackendUser user=userService.backdenglu(userCode, userPassword);
		if (user !=null) {
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/manager/main";
		}else {
			request.setAttribute("error","用户名或密码不正确");
			return "backendlogin";	
		}
	}
	
	@RequestMapping("/main")
	public String main(HttpSession session) {
		if(session.getAttribute(Constants.USER_SESSION)==null) {
			return "backendlogin";
		}
		return "/backend/main";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "backendlogin";
	}
	
	@RequestMapping("/list")
	public String list(
			@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryCategoryLevel1",required=false) Integer queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) Integer queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) Integer queryCategoryLevel3,
			@RequestParam(value="queryFlatformId",required=false) Integer queryFlatformId,
			@RequestParam(value="devId",required=false) Integer devId,
			@RequestParam(value="pageIndex",required=false) String pageIndex,Model model) {
		List<AppInfo> list=new ArrayList<AppInfo>();
		int pagesize=5;
		int currentPageNo=1;
		int queryStatus=1;
		if (querySoftwareName==null) {
			querySoftwareName="";
		}
		if (pageIndex!=null) {
			currentPageNo=Integer.parseInt(pageIndex);
		}
		//总页数
		PageSupport pageSupport=new PageSupport();
		int totalCount=appInfoService.findCount(querySoftwareName, queryStatus, queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,devId);
		int totalPageCount=pageSupport.getTotalPageCount();
		totalPageCount = totalCount%pagesize==0 ?totalCount/pagesize:totalCount/pagesize+1;
		if (currentPageNo<1) {
			currentPageNo=1;
		}else if (currentPageNo>totalPageCount) {
			currentPageNo=totalPageCount;
		}
		
		pageSupport.setCurrentPageNo(currentPageNo);
		pageSupport.setPageSize(pagesize);
		pageSupport.setTotalCount(totalCount);
		
		
		
		list=appInfoService.findList(querySoftwareName,queryStatus, queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,devId, (currentPageNo-1)*pagesize, pagesize);
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
		return "/backend/applist";
	}
	
	@RequestMapping(value = "/categorylevellist.json",produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object categorylevellist(@RequestParam String pid) {
		if(pid==null) {
			pid="";
		}
		List<AppCategory> list = appInfoService.findFLTHREE(pid==""?null:Integer.parseInt(pid));
		return JSONArray.toJSONString(list);
	}
	
	@RequestMapping("/check")
	public String check(@RequestParam String aid,@RequestParam String vid,Model model) {
		AppInfo appInfo = userService.getAppInfoById(Integer.parseInt(aid));
		AppVersion appVersion = userService.getAppVersionById(Integer.parseInt(vid));
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appInfo", appInfo);
		return "/backend/appcheck";
	}
	
	@RequestMapping("/checksave")
	public String checksave(@RequestParam String id,@RequestParam String status) {
		userService.updateAppInfo(Integer.parseInt(id),Integer.parseInt(status));
		return "redirect:/manager/list";
	}
	
}
