package com.ikilun.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikilun.util.StringUtil;

public abstract class DataChangeWatcher extends ZookeeperWatcher implements NodeCacheListener{
	protected Logger dbLogger = LoggerFactory.getLogger(this.getClass());
	private String prevalue;
	private NodeCache nodeCache;
	public DataChangeWatcher(String path){
		this.path = path;
	}
	
	protected abstract void processChange(String oldvalue, String newvalue);
	
	@Override
	public void nodeChanged() throws Exception {
		String tmp = prevalue;
		prevalue = getData();
		processChange(tmp, prevalue);
	}

	@Override
	public ZookeeperWatcher init(CuratorFramework zc) {
		this.client = zc;
		this.nodeCache = new NodeCache(client, path);
		this.nodeCache.getListenable().addListener(this);
		try {
			this.nodeCache.start(true);
		} catch (Exception e) {
			dbLogger.warn(e.getMessage());
		}
		this.prevalue = getData();
		return this;
	}
	private String getData(){
		ChildData cur = nodeCache.getCurrentData();
		if(cur!=null){
			byte[] data = cur.getData();
			if(data!=null){
				return StringUtil.getString(data, "utf-8");
			}
		}
		return null;
	}
}
