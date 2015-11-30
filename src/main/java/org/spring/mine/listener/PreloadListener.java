package org.spring.mine.listener;

import javax.servlet.http.HttpServlet;

import org.spring.mine.redis.RedisInitBean;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("serial")
public class PreloadListener extends HttpServlet {


	public void init() {
		//javax.servlet.ServletContainerInitializer
		//感觉自己默默叼
		//456
	}
	
	public void destroy(){
		//清理 redis 连接池 缓存
		RedisInitBean.returnResource();
		RedisInitBean.destroy();
	}
}
