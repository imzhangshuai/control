<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	session.setAttribute("hideSearchBar", true);
	session.setAttribute("isHideMyFavor", true);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户注册</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/png" href="/rsst-spring/resources/images/bitbug_favicon.ico">
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
<jsp:include page="/page/head.jsp"/>
<div class="big_wrap">
	<div class="register_box">
		<div class="reg_title">
			<h2>用户注册</h2>
		</div>
	<form id="registerForm" method="post">
		<dl class="forget1">
			<dt>手机号码：</dt>
			<dd>
				<input type="text" name="accountName" id="mobile" placeholder="请输入手机号码" maxlength="11"  class="inp3">
				<span class="tip mobileTip"><label style='color:red;'>*</label>仅支持中国大陆地区</span>
			</dd>

			<dt>短信验证码：</dt>
			<dd>
				<input type="text" name="smsCode" id="smsCode" placeholder="请输入短信验证码"  maxlength="6" style="width:165px;" class="inp3">
				<span class="sms_code" id="btnSmsCode">发送验证码</span>
				<span class="tip"><label id="tipCode"></label>&nbsp;</span>
			</dd>

			<dt>密码：</dt>
			<dd>
				<input type="password" name="password" id="login_password"  maxlength="16" placeholder="请输入密码" class="inp3">
				<span class="tip"><label id="tipPass"><label style='color:red;'>*</label>6-16位字母与数字</label></span>
			</dd>

			<dt>确认密码：</dt>
			<dd>
				<input type="password" name="confirmPassword" id="confirm_login_password" maxlength="16" placeholder="请输入确认密码" class="inp3">
				 <span class="tip"><label id="tipRePass"><label style='color:red;'>*</label>请再次输入登录密码</label> </span>
			</dd>

			<dt>名称：</dt>
			<dd>
				<input type="text" name="nickName" placeholder="请输入企业全名/个人姓名" style="width:300px;" class="inp3">
				<input type="hidden" name="company" placeholder="请输入企业全名/个人全名" style="width:200px;" class="inp3">
				<span class="tip"><label id="tipUname"></label> </span>
			</dd>

			<dt></dt>
			<dd>
                <span class="submit_btn" id="btnRegister" style="width:300px;" >立即注册</span>
			</dd>
			
			<p>
				<span style="float:middle;">
					<label>
						<input name="" type="checkbox"  id="agreement" checked>同意XX趋势《<a style="color:green" href="javascript:;" onclick="alert('SB');">服务条款</a>》
					</label>
				</span>
				<span  style="float:right;"><label class="tip" id="tipAgree"></label>&nbsp;</span>
			</p>
		</dl>
	</form>
		<p></p>
	</div>

</div>
<jsp:include page="/page/about.jsp"/>
</body>

<script type="text/javascript">

var sentIntv;
var initSecond=60;
var second,flag=true,isReg=false;

$(document).ready(function (){
	$("#mobile").val("");
	$("#smsCode").val("");
	$("#login_password").val("");
	$("#confirm_login_password").val("");
	
	 $("#btnRegister").click(function (){
		 registering();
	 });
	 
	 $("#mobile").blur(function(){
		 isMobileExists( $(this).val());
	 });
	 
	 $("#login_password").blur(function(){
		 checkParams(false);
	 });
	 
	 $("#confirm_login_password").blur(function(){
		 checkParams(false);
	 });
	 
	 $("#smsCode").blur(function(){
		 checkParams(false);
	 });
	 
	 $("#btnSmsCode").click(function(){
		 clickSendSms();
	 });
	 
	 $("#agreement").click(function(){
		 if($(this).is(":checked")){
			 $("#tipAgree").html("");
		 }
	 });
})

function clickSendSms(){
	 var info=checkMobile();
	 if(info && flag){
		 $("#btnSmsCode").text("正在发送...");
		 $.ajax({
			type:'post',
			url:"${basePath}/UserAction/sendSMSCode.html",
			data:{"mobile":$("#mobile").val()},
			success:function(data){
				if(data!=null){
					data=eval(data.info);
					if(1==data.code){
					    second=initSecond;
						btnSmsActive();
						sentIntv=self.setInterval("btnSmsActive()",1000);
				    }else{
				    	$("#btnSmsCode").text("重新发送");
				    }
					alert(data.msg);
				}
			},
			error:function(data){
				$("#btnSmsCode").text("重新发送");
				alert(data.msg);
			}
		});
	}
}

/**
 * 激活短信发送按钮
 */
function btnSmsActive(){
	if(second>0){
		flag=false;
		$("#btnSmsCode").text("重新发送("+(second--)+")");
		$("#btnSmsCode").css({"background":"gray"});
	}else{
		window.clearInterval(sentIntv);
		$("#btnSmsCode").text("发送验证码");
		$("#btnSmsCode").css({"background":"#f08619 none repeat scroll 0 0"});
		second=initSecond;
		flag=true;
	}
}

function checkMobile(){
	 return RegExp.mobileNum.test($("#mobile").val());
}


function registering() {
	var mobile = $("#mobile").val();
    var password = $("#login_password").val();
    var confirmPassword = $("#confirm_login_password").val();
    var smsCode=$("#smsCode").val() ;
    var info=checkParams(false);

    if(info.code==0 && !isReg){
    	isReg=true;
    	$("#btnRegister").text("正在注册...");
		$("#registerForm").ajaxSubmit({
			url : "${basePath}/UserAction/register.html",
			success : function(data) {
				if(data!=null){
					data=eval(data.info);
					alert(data.msg);
					continueRegHandle();
					if(data.code > 0){
				  	   window.location.href="${basePath}/HomeAction/index.html";
				    }else{
					    return;
				    }
				}
		    },
			error : function() {
				alert("对不起，出现错误!");
				continueRegHandle();
			}
	    });
    }
}

/**
 * 注册参数检查
 */
function checkParams(isSendSms){
    var mobile = $("#mobile").val();
    var password = $("#login_password").val();
    var confirmPassword = $("#confirm_login_password").val();
    var smsCode=$("#smsCode").val() ;
	var info={"code":0,"msg":""};
	var result;
	if(!checkMobile()){
		result = {"code":-1,"msg":"请填写正确手机号"};
		$(".mobileTip").html("<label style='color:red'>╳ "+ result.msg +"</label>");
		return result;
	}
	
    if(password == "" ||password.length > 16 || password.length < 6){
    	result=  {"code":-2,"msg":"密码为6-16个字符"};
		$("#tipPass").html("<label style='color:red'>╳ "+ result.msg +"</label>");
		return result;
    }else{
    	var middle=/[A-z]{1,16}/.test(password) && /[\d]{1,16}/.test(password);
    	var strong=/[@#￥%……\*%\^\&\(\)\-\_\=\+\`\~\!\/\;\:\"\'\,\.\?]{1,16}/.test(password);
    	var low=/^[A-z]{1,16}$/.test(password) || /^[\d]{1,16}$/.test(password) ;
    	var passLevel="";
    	if(low){ passLevel="弱"; }
    	if(middle){ passLevel="中"; }
    	if(strong){ passLevel="强"; }
    	$("#tipPass").html("<label style='color:green;'>"+passLevel+"</label>");
    }
	
    if(confirmPassword == ""){
    	result=  {"code":-4,"msg":"请输入确认密码"};
		$("#tipRePass").html("<label style='color:red'>╳ "+ result.msg +"</label>");
		return result;
    }else if(confirmPassword != password){ 
    	result=  {"code":-5,"msg":"两次输入的密码不一致"};
		$("#tipRePass").html("<label style='color:red'>╳ "+ result.msg +"</label>");
		return result;
    }else{
    	$("#tipRePass").html("<label style='color:green'>√ </label>");
    }
    
    if(!isSendSms){
	    if(smsCode==undefined || smsCode==null || smsCode== "") {
	    	result=  {"code":-6,"msg":"请输入验证码"};
			$("#tipCode").html("<label style='color:red'>╳ "+ result.msg +"</label>");
			return result;
	    }
	    
	    if(!$("#agreement").is(":checked")) {
	    	result=  {"code":-7,"msg":"请勾选同意注册协议"};
			$("#tipAgree").html("<label style='color:red'>╳ "+ result.msg +"</label>");
			return result;
	    }else{
	    	$("#tipAgree").html("");
	    }
    }
    return info;
}

function continueRegHandle(){
	isReg=false;
	$("#btnRegister").text("重新注册");
}

function isMobileExists(mobile){
	if(checkMobile()){
		$.post(
			"${basePath}/UserAction/isMobileExists.html",
			{"mobile":mobile},
			function(data){
				if(data!=null){
					data=eval(data.info);
					if(data.code<=0){
						$(".mobileTip").html("<label style='color:green'>√ 可以注册</label>  ");					
					}else{
						$(".mobileTip").html("<label style='color:red'>╳  已被注册</label>");					
					}
				}
			}
		);
	}else{
		$(".mobileTip").html("<label style='color:red'>╳  请填写正确手机号</label>");
	}
}
</script>
</html>