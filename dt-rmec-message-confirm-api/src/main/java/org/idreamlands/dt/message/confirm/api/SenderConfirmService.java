package org.idreamlands.dt.message.confirm.api;

import org.idreamlands.dt.message.entity.Message;
import org.springframework.cloud.netflix.feign.FeignClient;

import com.alibaba.boot.dubbo.DubboClient;
import com.alibaba.dubbo.config.annotation.Reference;

@FeignClient(path = "/confirm")
@DubboClient(protocol = "dubbo", value = @Reference(timeout = 10000, version = "1.0.0"))
public interface SenderConfirmService {
	
	default boolean test(Message message) {
		return true;
	}
}
