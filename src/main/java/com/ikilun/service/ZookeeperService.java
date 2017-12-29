package com.ikilun.service;

import java.util.List;
import java.util.Map;

public interface ZookeeperService {
	boolean addPresistentNode(String path, String data);
	List<String> getChildren(String path);
	boolean updateNode(String path, String data);
	boolean delNode(String path);
	boolean exist(String path);
	String getNodeData(String path);
	Map<String, String> getChildrenData(String path);
	void addMonitor(ZookeeperWatcher watcher);
}
