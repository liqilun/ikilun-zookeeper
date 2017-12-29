package com.ikilun.web.controller.zk;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ikilun.service.ZookeeperService;
import com.ikilun.web.controller.BaseController;

@RestController
public class ZkController extends BaseController{
	@Autowired
	private ZookeeperService zookeeperService;
	@RequestMapping("/zk/zkconfig")
	@ResponseBody
	public String zkconfig(String data, ModelMap model, HttpServletRequest request) {
		String path = "/LQLTEST";
		if(!zookeeperService.exist(path)){
			zookeeperService.addPresistentNode(path, data);
		}else{
			zookeeperService.updateNode(path, data);
		}
		return "success";
	}
	
	@RequestMapping("/zk/clidrenList")
	@ResponseBody
	public String clidrenList(String path, ModelMap model, HttpServletRequest request) {
		Map<String, String> map = zookeeperService.getChildrenData(path);
		return map.toString();
	}
}
