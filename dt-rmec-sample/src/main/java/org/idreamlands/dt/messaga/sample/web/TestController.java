package org.idreamlands.dt.messaga.sample.web;

import org.idreamlands.dt.message.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping
	@ResponseBody
	public  StatusConfirm getInfo() throws Exception {
		StatusConfirm sc = new StatusConfirm();
		sc.setStatus("success");
		sc.setMessage("successmessage");
		return sc;
	}
	
	@PostMapping
	public  StatusConfirm consumer(@RequestBody Message message) throws Exception {
		StatusConfirm sc = new StatusConfirm();
		sc.setStatus("success");
		sc.setMessage("successhandle");
		System.out.println(message);
		return sc;
	}


}
