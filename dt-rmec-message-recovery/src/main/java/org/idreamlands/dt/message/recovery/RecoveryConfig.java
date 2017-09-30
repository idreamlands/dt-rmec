package org.idreamlands.dt.message.recovery;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "message")
@Configuration
public class RecoveryConfig {
	
	private Map<Integer, Integer> send ;
	
	private int maxsend = 0;

	public Map<Integer, Integer> getSend() {
		return send;
	}

	public void setSend(Map<Integer, Integer> send) {
		this.send = send;
	}

	public int getMaxsend() {
		return maxsend;
	}

	public void setMaxsend(int maxsend) {
		this.maxsend = maxsend;
	}

	
}
