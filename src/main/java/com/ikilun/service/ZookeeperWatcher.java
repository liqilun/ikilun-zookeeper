package com.ikilun.service;

import org.apache.curator.framework.CuratorFramework;

public abstract class ZookeeperWatcher {
	protected String path;
	protected CuratorFramework client;
	public String getPath() {
		return path;
	}
	public abstract ZookeeperWatcher init(CuratorFramework zc);
}
