<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	session.setAttribute("hideSearchBar", true);
	session.setAttribute("isHideMyFavor", true);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户登录-323趋势</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/png" href="${cdn}/resources/images/fav.gif">
<link rel="stylesheet" href="${cdn}/resources/stylesheets/basic.css" />
<link rel="stylesheet" href="${cdn}/resources/stylesheets/main.css" />
<script src="${cdn}/resources/javascripts/jquery-1.10.1.min.js" type="text/javascript"></script>
<script src="${cdn}/resources/javascripts/main.js"></script>
<script src="${cdn}/resources/javascripts/idangerous.swiper.min.js"></script>
<script src="${cdn}/resources/javascripts/Validform_v5.3.2_min.js"></script> 
<script src="${cdn}/resources/javascripts/RegExp-ext.js"></script>
<script src="${cdn}/resources/javascripts/jquery.form.js"></script>
<script src="${cdn}/resources/javascripts/jquery.md5.js"></script>
</head>
<body>
<jsp:include page="/page/head.jsp" />
<div class="big_wrap">
	<div class="login_box">
		<div class="reg_title">
			<h2>用户登录</h2>
		</div>
		<form id="LoginForm" method="post">
			<dl>
				<dd><span id="showError" style="text-align:center;color:red;"></span></dd>
				<dd>
					<input type="text" name="uname" id="mobile" class="log_name inp3" placeholder="请输入用户名" maxlength="11">
				
				</dd>
				<dd>
					<input type="password" name="pass" id="pass" class="log_pass inp3" placeholder="登录密码" maxlength="20">
				</dd>
				<dd id="verifyCodeArea">
					<span>
						<input type="text" name="verifyCode" id="code" placeholder="请输入验证码"  maxlength="4" class="inp3 yzm"/>
						<input type="hidden" id="code_id" name="code_id" value="${_rand}"/>
						<input type="hidden" id="rurl" name="rurl" value="${rurl}"/>
					</span>
					<span>
						<a href="javascript:void(0);" class="flash_verify" title ="点击图片刷新" >
							<img id="verify_image" style="width: 93px; height: 36px; vertical-align: middle;"
							src="${basePath}/UserAction/buildVerifyImage.html?_rand=${_rand}"/>
						</a>
					</span>
				</dd>
                <dd>
                	<label>
                    	<input name="" type="checkbox" id="rememberUname" value="" /> 记住用户名
                    </label>
                    <a class="forget_btn" href="${basePath}/UserAction/forgetPassInit.html">忘记密码？</a>
                </dd>
				<dd>
                	<span class="submit_btn" id="btnLogin">立即登录</span>
				</dd>
                <dd>
                	<a class="reg_btn" href="${basePath}/UserAction/registerInit.html">免费注册</a>
				</dd>
			</dl>
		</form>
		<p></p>
	</div>
</div>
<jsp:include page="/page/about.jsp"/>

<script>

	var _rand=new Date().getTime() + Math.random();
	var remUnameKey="www.rstrend.com";
	
	$(function(){
		var forgetUnam="${uname}";
		var checkRemember=getCookie(remUnameKey+"-check");
		if(checkRemember==1){
			forgetUnam = null!=forgetUnam && ""!=forgetUnam ? forgetUnam : getCookie(remUnameKey);
			$("#rememberUname").attr("checked",true);
		}
		$("#mobile").val(forgetUnam);
		getVerifyImage();
		$(".flash_verify").click(function(){
			getVerifyImage();
		});
		
		$("#btnLogin").click(function(){
			logining();
		});
		
		$(document).bind("keydown",function(event){
			if (event.keyCode==13) {
				logining();
			} 
		});
		
		$("#mobile").blur(function(){
			if(!checkMobile($(this).val())) {
				$("#showError").css("color","red");
				$("#showError").html("请填写正确的手机号码");
			}else{
				$("#showError").html("");
			}
		});
		
		$("#rememberUname").click(function(){
			if ($(this).is(":checked")) {
				var mobile=$("#mobile").val();
				if(checkMobile(mobile)){
					SetCookie(remUnameKey,mobile);
					SetCookie(remUnameKey+"-check",1);
				}else{
					$(this).attr("checked",false);
				}
			}else{
				delCookie(remUnameKey+"-check");
			}
		});
		
	});
	
	function getVerifyImage(){
		_rand=new Date().getTime() + Math.random();
		$("#verify_image").attr("src","${basePath}/UserAction/buildVerifyImage.html?_rand="+_rand);
		$("#code_id").attr({"value":_rand});
	}
	
	function logining(){
		$("#showError").html("");
		$("#showError").css("color","red");
		var mobile=$("#mobile").val().trim();
		var password=$("#pass").val().trim();
		var code=$("#code").val().trim();
		
		if(!checkMobile(mobile)){
			$("#showError").html("请填写正确的手机号码");
		   	 return;
	    }
		
	    if(""==password.trim()){
	    	$("#showError").html("请填写密码");
	    	return ;
	    }
		
	    if(""==code.trim()){
	    	$("#showError").html("请填写验证码");
	    	return ;
	    }
	    $("#btnLogin").text("登录中...");
	    
		$("#LoginForm").ajaxSubmit({
			url:"${basePath}/UserAction/login.html",
			success:function(data){
				data=eval(data); 
				$("#showError").html(data.info.msg);
				$("#btnLogin").text("登录中……");
				switch(data.info.code){
					case 1:
						SetCookie("www.rstrend.com",mobile);
						$("#showError").css("color","green");
						$("#showError").html(data.msg);
						var redirectUrl = data.data==null ? "${basePath}/HomeAction/index.html":encodeURI(data.data.rurl);
						window.location.href = redirectUrl;
						break;
					case 0:
						$("#login_password").focus();getVerifyImage();
						break;
					case -1:
						$("#code").focus();getVerifyImage();
						break;
					case -2:
						$("#mobile").focus();getVerifyImage();
						break;
					case -3:
						$("#login_password").focus();getVerifyImage();
						break;
					default:getVerifyImage();break;
				}
				$("#btnLogin").text("重新登录");
			},
			error:function(){$("#btnLogin").text("重新登录");getVerifyImage();$("#showError").html(""); }
		});
	}
	
	function checkMobile(mobile){
		return RegExp.mobileNum.test(mobile)
	}
	
	//写cookies函数
	function SetCookie(name,value)//两个参数，一个是cookie的名子，一个是值
	{
	    var Days = 30; //此 cookie 将被保存 30 天
	    var exp  = new Date();    //new Date("December 31, 9998");
	    exp.setTime(exp.getTime() + Days*24*60*60*1000);
	    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
	}
	//取cookies函数
	function getCookie(name)       
	{
	    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	     if(arr != null) return unescape(arr[2]); return null;

	}
	//删除cookie
	function delCookie(name)
	{
	    var exp = new Date();
	    exp.setTime(exp.getTime() - 1);
	    var cval=getCookie(name);
	    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
	}
</script>
</html>
<%
	session.setAttribute("hideSearchBar", false);
	session.setAttribute("isHideMyFavor", false);
%>