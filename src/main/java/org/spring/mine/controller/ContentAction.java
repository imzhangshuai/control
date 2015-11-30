package org.spring.mine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ContentAction")
public class ContentAction extends BaseAction{

	/**
	 * 关于我们
	 * 
	 * @return
	 */
	@RequestMapping("aboutUs")
	public ModelAndView aboutUs() {
		return getModelAndView();
	}

	/**
	 * 会员信息
	 */
	@RequestMapping("bmberInfo")
	public ModelAndView bmberInfo() {
		return getModelAndView();
	}

	/**
	 * 联系我们
	 */
	@RequestMapping("contact")
	public ModelAndView contact() {
		return getModelAndView();
	}

	/**
	 * 版权声明
	 */
	@RequestMapping("copyright")
	public ModelAndView copyright() {
		return getModelAndView();
	}

	/***
	 * 法律声明
	 */
	@RequestMapping("legal")
	public ModelAndView legal() {
		return getModelAndView();
	}

	/**
	 * 服务条款
	 */
	@RequestMapping("service")
	public ModelAndView service() {
		return getModelAndView();
	}

	/**
	 * 站点地图
	 */
	@RequestMapping("siteMap")
	public ModelAndView siteMap() {
		return getModelAndView();
	}

	/**
	 * 新手指引
	 */
	@RequestMapping("guide")
	public ModelAndView guide() {
		return getModelAndView();
	}

	/**
	 * 品牌申明
	 */
	@RequestMapping("issue")
	public ModelAndView issue() {
		return getModelAndView();
	}

	/**
	 * 服务说明
	 */
	@RequestMapping("server")
	public ModelAndView server() {
		return getModelAndView();
	}

	/**
	 * 支付类型
	 */
	@RequestMapping("payType")
	public ModelAndView payType() {
		return getModelAndView();
	}

}
