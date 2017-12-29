package com.ikilun.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikilun.service.ConfigTrigger;
import com.ikilun.service.ConfigWatcher;
import com.ikilun.service.TestService;
import com.ikilun.service.ZookeeperService;

@Service
public class SysConfigRegister implements InitializingBean {
	@Autowired
	private TestService testService;
	@Autowired
	private ZookeeperService zookeeperService;

	@Override
	public void afterPropertiesSet() throws Exception {
		zookeeperService.addMonitor(new ConfigWatcher("/LQLTEST", new ConfigTrigger() {
			@Override
			public void refreshCurrent(String newConfig) {
				System.out.println(newConfig);
				testService.test();
			}
		}));
	}

}
