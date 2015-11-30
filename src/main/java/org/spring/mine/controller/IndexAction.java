package org.spring.mine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexAction extends BaseAction {

	@RequestMapping("index")
	public ModelAndView index() {
		return getModelAndView("/HomeAction/index");
	}
}
