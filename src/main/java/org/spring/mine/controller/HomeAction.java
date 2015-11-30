package org.spring.mine.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.team.mine.domain.UserAccount;
import com.team.mine.util.CacheUtil;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/HomeAction")
public class HomeAction extends BaseAction{
	

	@RequestMapping("index")
	public ModelAndView index(HttpServletRequest request) {
		UserAccount user=CacheUtil.getCurrUserAccount(request);
		ModelAndView mav=getModelAndView(namespace+"/index");
		mav.addObject("user",user);
		mav.addObject("channelId",1);
		return mav;
	}

	@RequestMapping("tendAnalys")
	public ModelAndView tendAnalys() {
		return getModelAndView("tendAnalys");
	}
	
	@RequestMapping("update")
	public ModelAndView  update(){
		return getModelAndView("update");
	}
}