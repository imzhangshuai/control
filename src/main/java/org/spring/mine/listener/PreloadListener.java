package org.spring.mine.listener;

import javax.servlet.http.HttpServlet;

import org.spring.mine.redis.RedisInitBean;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("serial")
public class PreloadListener extends HttpServlet {


	public void init() {
		//javax.servlet.ServletContainerInitializer
	}
	
	public void destroy(){
		RedisInitBean.returnResource();
		RedisInitBean.destroy();
	}
}
