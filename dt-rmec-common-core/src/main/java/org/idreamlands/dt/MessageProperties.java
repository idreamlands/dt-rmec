package org.idreamlands.dt;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.dtremc.message")
public class MessageProperties {
	
	private int maxsendtimes;
	
	private int handleduration;
	
	private boolean enablehttp;
	
	private Map<Integer, Integer> timeinterval ;

	
	public int getMaxsendtimes() {
		return maxsendtimes;
	}

	public void setMaxsendtimes(int maxsendtimes) {
		this.maxsendtimes = maxsendtimes;
	}

	public int getHandleduration() {
		return handleduration;
	}

	public void setHandleduration(int handleduration) {
		this.handleduration = handleduration;
	}

	public boolean isEnablehttp() {
		return enablehttp;
	}

	public void setEnablehttp(boolean enablehttp) {
		this.enablehttp = enablehttp;
	}

	public Map<Integer, Integer> getTimeinterval() {
		return timeinterval;
	}

	public void setTimeinterval(Map<Integer, Integer> timeinterval) {
		this.timeinterval = timeinterval;
	}
	
	

	
}
