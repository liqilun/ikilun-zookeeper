package com.ikilun.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikilun.ZookeeperConfig;
import com.ikilun.service.ZookeeperService;
import com.ikilun.service.ZookeeperWatcher;

@Service
public class ZookeeperServiceImpl implements ZookeeperService, InitializingBean, DisposableBean {
	protected Logger dbLogger = LoggerFactory.getLogger(this.getClass());
	private static final String ENCODING = "UTF-8";
	private List<ZookeeperWatcher> watcherList = new ArrayList<ZookeeperWatcher>();
	@Autowired
	private ZookeeperConfig zookeeperConfig;

	private CuratorFramework zkClient;
	
	public ZookeeperServiceImpl() {
	}

	private ConnectionState state;

	private void setState(ConnectionState state) {
		this.state = state;
	}

	private void init() throws IOException {
		Builder builder = CuratorFrameworkFactory.builder().connectString(zookeeperConfig.getServers()).retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
				.connectionTimeoutMs(zookeeperConfig.getSessionTimeout());
		zkClient = builder.build();
		zkClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
			public void stateChanged(CuratorFramework c, ConnectionState s) {
				setState(s);
				dbLogger.warn("zookeeper connection " + s);
			}
		});
		zkClient.start();
		int i = 1, max = 50;
		while (state != ConnectionState.CONNECTED) {
			dbLogger.warn("zookeeper try connecting...," + i++ + " times!");
			if (i > max) {
				throw new IOException("connect to zookeeper failure:" + zookeeperConfig.getServers());
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void destroy() throws Exception {
		if (zkClient != null) {
			zkClient.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	@Override
	public boolean addPresistentNode(String path, String data) {
		try {
			byte[] d = StringUtils.isNotBlank(data)? data.getBytes(ENCODING):null;
			zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(Ids.OPEN_ACL_UNSAFE).forPath(path, d);
			return true;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public List<String> getChildren(String path) {
		if (zkClient != null) {
			try {
				return zkClient.getChildren().forPath(path);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return null;
	}
	
	@Override
	public boolean updateNode(String path, String data) {
		try {
			zkClient.setData().forPath(path, data.getBytes(ENCODING));
			return true;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	@Override
	public boolean delNode(String path) {
		try {
			zkClient.delete().forPath(path);
			return true;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public boolean exist(String path) {
		try {
			return zkClient.checkExists().forPath(path)!=null;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	@Override
	public String getNodeData(String path) {
		try {
			byte[] b = zkClient.getData().forPath(path);
			if(b==null) return null;
			return new String(b, ENCODING);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	@Override
	public Map<String, String> getChildrenData(String path) {
		Map<String, String> result = new HashMap<String, String>();
		List<String> children = getChildren(path);
		if (!StringUtils.endsWith(path, "/"))
			path += "/";
		if (children != null) {
			for (String child : children) {
				result.put(child, getNodeData(path + child));
			}
		}
		return result;
	}

	@Override
	public void addMonitor(ZookeeperWatcher watcher) {
		synchronized (this) {
			for(ZookeeperWatcher zw : watcherList){
				if(zw.getPath().equals(watcher.getPath()) && zw.getClass().equals(watcher)){
					return;
				}
			}
			if(!exist(watcher.getPath())){
				addPresistentNode(watcher.getPath(), "");
			}
			watcherList.add(watcher);
			watcher.init(zkClient);
		}
	}
}
