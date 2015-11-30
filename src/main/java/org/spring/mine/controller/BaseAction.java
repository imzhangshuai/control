package org.spring.mine.controller;

import java.util.Map;

import org.spring.mine.redis.RedisInitBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.team.mine.service.UserService;
import com.team.mine.util.Settings;

@Controller
@Scope("session")
public class BaseAction {

	@Value("${cdn}")
	protected String cdn;
	
	@Value("${basePath}")
	protected String basePath;
	
	protected String namespace="/"+this.getClass().getSimpleName();
	
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected RedisInitBean redisInitBean;
	
	@Autowired
	protected Settings setting;
	
	public ModelAndView getModelAndView(){
		ModelAndView modelandview=new ModelAndView();
		modelandview.addObject("cdn", cdn);
		modelandview.addObject("basePath", basePath);
		return modelandview;
	}
	
	public ModelAndView getModelAndView(String view){
		ModelAndView modelandview=new ModelAndView(view);
		modelandview.addObject("cdn", cdn);
		modelandview.addObject("basePath", basePath);
		return modelandview;
	}
	
	public ModelAndView getModelAndView(Map<String,?>paramMap){
		ModelAndView modelandview=new ModelAndView();
		modelandview.addAllObjects(paramMap);
		modelandview.addObject("cdn", cdn);
		modelandview.addObject("basePath", basePath);
		return modelandview;
	}
	
	public ModelAndView getModelAndView(Map<String,?>paramMap,String view){
		ModelAndView modelandview=new ModelAndView(view);
		modelandview.addAllObjects(paramMap);
		modelandview.addObject("cdn", cdn);
		modelandview.addObject("basePath", basePath);
		return modelandview;
	}
	
	public String getCdn() {
		return cdn;
	}
	public void setCdn(String cdn) {
		this.cdn = cdn;
	}
	
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

}
