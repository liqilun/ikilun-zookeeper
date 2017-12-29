package com.ikilun.service;

public class ConfigWatcher extends DataChangeWatcher{
	private ConfigTrigger trigger;
	public ConfigWatcher(String path, ConfigTrigger trigger) {
		super(path);
		this.trigger = trigger;
	}
	@Override
	protected void processChange(String oldvalue, String newvalue) {
		trigger.refreshCurrent(newvalue);
	}

	public ConfigTrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(ConfigTrigger trigger) {
		this.trigger = trigger;
	}

}
