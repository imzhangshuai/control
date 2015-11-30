package org.spring.mine.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.team.mine.common.MD5Util;
import com.team.mine.common.ResultInfo;
import com.team.mine.common.SysConstants;
import com.team.mine.domain.UserAccount;
import com.team.mine.domain.UserStatus;
import com.team.mine.util.CacheUtil;
import com.team.mine.util.VerifyCodeUtil;

@Controller
@RequestMapping("/UserAction")
public class UserAction  extends BaseAction{

	/**短信帐号**/
	@Value("${SMS.platform.uid}")
	public  String sms_uid;
	
	/**短信签名**/
	@Value("${SMS.platform.pwd}")
	public  String sms_pwd;
	
	/**
	 * 登录页面初始
	 */
	@RequestMapping("loginit")
	public   ModelAndView loginit(String rurl,HttpServletRequest request ) {
		UserAccount logUser = CacheUtil.getCurrUserAccount(request);
		rurl = null == rurl 
				|| rurl.matches(".*[logout|updatepass].*") 
				|| "".equals(rurl.trim())
			? "/" : rurl;
		if (null != logUser) {
			
			return new ModelAndView("redirect:"+rurl); 
		}
		
		//Http.Cookie remember = request.cookies.get("rememberme");

		String _rand = System.currentTimeMillis() + Math.random() + "";
		ModelAndView model=getModelAndView(); 
		model.addObject("rurl", rurl);
		model.addObject("_rand", _rand);
		return model;
	}

	/**
	 * 用户登录
	 * 
	 * @param user
	 */
	@SuppressWarnings("serial")
	@RequestMapping("login")
	public  ModelAndView login(String uname,String pass,HttpServletRequest request) {
		UserAccount user=new UserAccount();
		user.setAccountName(uname);
		user.setPassword(pass);
		ResultInfo info = new ResultInfo();
		ModelAndView model=getModelAndView(); //org.codehaus.jackson.map.ObjectMapper
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String,Object> json = new HashMap<String,Object>();
	       
		String code_id = request.getParameter("code_id");
		String verifyCode = request.getParameter("verifyCode");
		final String rurl = request.getParameter("rurl"); // 登录重定向
		if (null == user.getAccountName() || StringUtils.isEmpty(user.getAccountName().trim())) {
			info.set(-1, "用户名不能为空");
			json.put("info", info);
			view.setAttributesMap(json);
	        model.setView(view);
			return model;
		}
		if (null == user.getPassword() || StringUtils.isEmpty(user.getPassword().trim())) {
			info.set(-2, "密码不能为空");
			json.put("info", info);
			view.setAttributesMap(json);
	        model.setView(view);
			return model;
		}
		if (null == verifyCode || StringUtils.isEmpty(verifyCode.trim())) {
			info.set(-3, "验证码不能为空");
			json.put("info", info);
			view.setAttributesMap(json);
	        model.setView(view);
			return model;
		}
		String getCode = request.getSession().getAttribute(code_id + SysConstants.VERIFY_CODE_PREFIX)+"";
		if (!verifyCode.equalsIgnoreCase(getCode)) {
			info.set(-3, "验证码错误");
			json.put("info", info);
			view.setAttributesMap(json);
	        model.setView(view);
			return model;
		}
		// String
		// secretKey=Play.configuration.getProperty("application.secret");
		user.setPassword(MD5Util.getMD5code(user.getPassword()));
		// System.out.println(secretKey+"\t"+user.password);
		final UserAccount loginUser = userService.login(user);
		if (null == loginUser) {
			info.set(-4, "用户名或密码错误");
		} else {
			if (loginUser.getStatus().getStatus().equals(UserStatus.DISABLED.getStatus())) {
				info.set(-5, "用户已被禁用，请联系管理员");
				view.setAttributesMap(json);
		        model.setView(view);
				return model;
			}
			if (loginUser.getStatus().getStatus().equals(UserStatus.BLACKLIST.getStatus())) {
				info.set(-5, "用户已被拉黑，请联系管理员");
				json.put("info", info);
				view.setAttributesMap(json);
		        model.setView(view);
				return model;
			}

			info.set(1, "登录成功", new HashMap<String, Object>() {
				{
					put("rurl", rurl);
					put("vip",loginUser.getVipLevel());
				}
			});
			CacheUtil.setCurrentUser(request,loginUser);
		}
		json.put("info", info);
		view.setAttributesMap(json);
        model.setView(view);
		return model;
	}

	/**
	 * 手机号码是否存在
	 * 
	 * @param mobile
	 */
	@RequestMapping("isMobileExists")
	public  ModelAndView isMobileExists(String mobile) {
		ModelAndView model=getModelAndView(); 
		//org.codehaus.jackson.map.ObjectMapper;
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String,Object> json = new HashMap<String,Object>();
		boolean exists = userService.isMobileExists(mobile);
		ResultInfo info = new ResultInfo(exists ? 1 : 0, exists ? "已存在" : "不存在");
		json.put("info",info);
		view.setAttributesMap(json);
		model.setView(view);
		return model;
	} 

	/***
	 * 生成验证码图片
	 * 
	 * @author ZhangShuai
	 * @version 1.0
	 * @created 2015年5月19日 下午2:28:44
	 * @param id
	 */
	@RequestMapping("buildVerifyImage")
	public  ModelAndView buildVerifyImage(String _rand,HttpServletResponse response,HttpServletRequest request ) {
		char []code=VerifyCodeUtil.generateCheckCode(4);
		try{
			OutputStream output = response.getOutputStream();
			BufferedImage image=VerifyCodeUtil.getVerifyImage(code);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ImageIO.write(image, "JPEG", bos);
	        byte[] buf = bos.toByteArray();
	        response.setContentLength(buf.length);
	        output.write(buf);
	        bos.close();
	        output.close();
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			request.getSession().setAttribute(_rand + SysConstants.VERIFY_CODE_PREFIX, new String(code));
		}
        return null;
	}

	/**
	 * 注册页面
	 * 
	 * @param user
	 */
	@RequestMapping("registerInit")
	public  ModelAndView registerInit() {
		return getModelAndView(namespace+"/registerInit"); 
	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 */
	@RequestMapping("register")
	public  ModelAndView register(@ModelAttribute UserAccount user,HttpServletRequest request) {
		ResultInfo info = new ResultInfo();
		ModelAndView model=new ModelAndView(); 
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String,Object> json = new HashMap<String,Object>();
		String confirmPass = request.getParameter("confirmPassword");
		String smsCode = request.getParameter("smsCode");
		if (null == user.getAccountName() || StringUtils.isEmpty(user.getAccountName().trim())) {
			info.set(-1, "用户名不能为空");
			json.put("info",info);
			view.setAttributesMap(json);
			model.setView(view);
			return model;
		}
		if (null == user.getPassword() || StringUtils.isEmpty(user.getPassword().trim())) {
			info.set(-2, "密码不能为空");
			json.put("info",info);
			view.setAttributesMap(json);
			model.setView(view);
			return model;
		}
		if (null == confirmPass || !confirmPass.equals(user.getPassword())) {
			info.set(-3, "两次密码不一致");
			json.put("info",info);
			view.setAttributesMap(json);
			model.setView(view);
			return model;
		}

		if (StringUtils.isEmpty(smsCode.trim()) && !smsCode.matches("^\\d{4,6}$")) {
			info.set(-4, "验证码格式错误");
			json.put("info",info);
			view.setAttributesMap(json);
			model.setView(view);
			return model;
		}

//		Object getSmsCode = request.getSession().getAttribute(user.getAccountName()+SysConstants.REG_SMS_CACHE);
//		System.out.println(getSmsCode);
//		if (null == getSmsCode || !getSmsCode.toString().equals(smsCode)) {
//			info.set(-5, "验证码不正确");
//			json.put("info",info);
//			view.setAttributesMap(json);
//			model.setView(view);
//			return model;
//		}

		user.setPassword(MD5Util.getMD5code(user.getPassword()));
		user.setCreateTime(new Timestamp(System.currentTimeMillis()));
		user.setStatus(UserStatus.ENABLED);
		int result = userService.insert(user);
		// 写入登录缓存
		if (result > 0) {
			UserAccount getUser=userService.findByMobile(user.getAccountName());
			request.getSession().setAttribute(request.getSession().getId() + SysConstants.LOGIN_CACHE_AFTFIX, getUser.getAccountName());
			CacheUtil.setCurrentUser(request, getUser);
		}

		info.set(result > 0 ? 1 : 0, result > 0 ? "注册成功" : "注册失败");
		json.put("info",info);
		view.setAttributesMap(json);
		model.setView(view);
		return model;
	}

	/***
	 * 发送短信验证码
	 * 
	 * @author ZhangShuai
	 * @version 1.0
	 * @created 2015年5月21日 下午5:37:21
	 * @param mobile
	 */
	@RequestMapping("sendSMSCode")
	public  ModelAndView sendSMSCode(String mobile,HttpServletRequest request) {

		ModelAndView model=new ModelAndView(); 
		ResultInfo info = new ResultInfo();

		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String,Object> json = new HashMap<String,Object>();
//		Object getSmsCode = request.getSession().getAttribute(mobile+SysConstants.REG_SMS_CACHE);
//
//		if (null == getSmsCode) {
//			SMSUtil.sendCode(request,mobile,sms_uid, sms_pwd,info);
//		} else {
//			info.set(0, "系统繁忙，请10分钟后再试");
//		}
//		json.put("info", info);
//		view.setAttributesMap(json);
//		model.setView(view);
		info.set(1, "短信发送成功");
		json.put("info", info);
		view.setAttributesMap(json);
		model.setView(view);
		return model;
	}

	/***
	 * 获得用户详细信息
	 * 
	 * @param id
	 */
	@RequestMapping("userinfo")
	public  ModelAndView userinfo(HttpServletRequest request) {

		ModelAndView model=getModelAndView(); 
		UserAccount user = CacheUtil.getCurrUserAccount(request);
		if(null==user){
			return getModelAndView("/UserAction/loginit"); 
		}
		model.addObject(user);
		return model;
	}

	/**
	 * 修改个人信息
	 * 
	 * @param user
	 */
	@RequestMapping("updateUser")
	public  ModelAndView updateUser(UserAccount user,HttpServletRequest request) {
		ModelAndView model=getModelAndView(); 
		ResultInfo info = new ResultInfo();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String,Object> json = new HashMap<String,Object>();
		UserAccount modifyUser=new UserAccount();
		if(null!=user){
			modifyUser.setAccountId(user.getAccountId());
			modifyUser.setAccountName(user.getAccountName());
			modifyUser.setNickName(user.getAccountName());
		}
		
		int result = userService.updateUserInfo(modifyUser, info);
		if(result>0){
			CacheUtil.updateCurrentUser(request);
		}
		info.set(result > 0 ? 1 : 0, result > 0 ? "修改信息成功" : "修改信息失败");
		json.put("info", info);
		model.setView(view);
		return model;
	}

	/**
	 * 修改登录密码
	 * 
	 * @param userName
	 * @param oldPass
	 * @param userpassword
	 */
	@RequestMapping("updatePassword")
	public  ModelAndView updatePassword( String oldPass, String userpassword, String userpassword2,HttpServletRequest request) {
		ModelAndView model=getModelAndView(); 
		ResultInfo info = new ResultInfo();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String,Object> json = new HashMap<String,Object>();
		UserAccount currentUser = CacheUtil.getCurrUserAccount(request);
		if (null == currentUser) {
			info.set(0, "用户登录信息异常，请重新登录");
			json.put("info", info);
			model.setView(view);
			return model;
		}
		String pass =currentUser.getPassword();
		if (!MD5Util.getMD5code(oldPass).equals(pass)) {
			info.set(-1, "旧密码错误，请重新输入");
			json.put("info", info);
			model.setView(view);
			return model;
		}
		if(!userpassword.equals(userpassword2)){
			info.set(-2, "两次密码不一致，请重新输入");
			json.put("info", info);
			model.setView(view);
			return model;
		}
		currentUser.setPassword(MD5Util.getMD5code(userpassword));;
		int result = userService.updatePassword(currentUser);
		if(result>0){
			CacheUtil.remove(request,currentUser.getAccountName()+SysConstants.LOGIN_CACHE_AFTFIX);
		}
		info.set(result > 0 ? 1 : 0, result > 0 ? "修改成功" : "修改失败");
		json.put("info", info);
		model.setView(view);
		return model;
	}

	/**
	 * 修改登录密码初始页面
	 */
	@RequestMapping("updatePassInit")
	public  ModelAndView updatePassInit() {
		return getModelAndView(); 
	}
	
	/***
	 * 忘记密码初始页
	 */
	@RequestMapping("forgetPassInit")
	public ModelAndView forgetPassInit(){
		return getModelAndView(); 
	}

	/**
	 * 修改交易密码初始页面
	 */

	@RequestMapping("updateTradePassInit")
	public  ModelAndView updateTradePassInit() {
		return getModelAndView(); 
	}

	/**
	 * 修改交易密码
	 * 
	 * @param userName
	 * @param oldTradePass
	 * @param newTradePass
	 */

	@RequestMapping("updateTradePassword")
	public  ModelAndView updateTradePassword(String userName, String oldTradePass, String newTradePass,HttpServletRequest request) {

		ModelAndView model= getModelAndView(); 
		ResultInfo info = new ResultInfo();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String,Object> json = new HashMap<String,Object>();
		UserAccount currentUser = CacheUtil.getCurrUserAccount(request);
		if (null == currentUser) {
			info.set(0, "用户登录信息异常，请重新登录");
			json.put("info", info);
			model.setView(view);
			return model;
		}
		String tradePass =currentUser.getTradePassword();
		String md5OldTradePass = MD5Util.getMD5code(oldTradePass);
		if (!md5OldTradePass.equals(tradePass)) {
			info.set(-1, "旧交易密码错误，请重新输入");
			json.put("info", info);
			model.setView(view);
			return model;
		}
		if (md5OldTradePass.equals(currentUser.getPassword())) {
			info.set(-2, "交易密码不能与登录密码一致，请重新设置");
			json.put("info", info);
			model.setView(view);
			return model;
		}
		UserAccount user = new UserAccount();
		user.setAccountName(userName);
		user.setPassword(MD5Util.getMD5code(newTradePass));
		int result = userService.updatePassword(user);
		info.set(result > 0 ? 1 : 0, result > 0 ? "修改成功" : "修改失败");
		json.put("info", info);
		model.setView(view);
		return model;
	}

	/**
	 * 退出
	 */
	@RequestMapping("logout")
	public  ModelAndView logout(HttpServletRequest request) {
		UserAccount getUser = CacheUtil.getCurrUserAccount(request);
		
		if (null != getUser) {
			CacheUtil.remove(request,SysConstants.LOGIN_CACHE_AFTFIX);
		}
		return getModelAndView("/UserAction/loginit"); // 重定向到登录页
	}

	/**
	 * 黑名单列表
	 */
	@RequestMapping("gotoBlack")
	public  ModelAndView gotoBlack() {
		return getModelAndView(namespace+"/gotoBlack");
	}

	/**
	 * 用户购买引导页
	 */

	@RequestMapping("guidePage")
	public  ModelAndView guidePage(){
		
		return getModelAndView(namespace+"/guidePage");
	}
}
