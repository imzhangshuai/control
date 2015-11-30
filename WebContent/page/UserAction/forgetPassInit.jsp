<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<%
	session.setAttribute("hideSearchBar", true);
	session.setAttribute("isHideMyFavor", true);
%>
<jsp:include page="/page/main.jsp" />
<jsp:include page="/page/head.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户忘记密码</title>
</head>

<style>
.register_box .tip {
	color: gray;
	font-size: 12px;
	text-align: center;
}
</style>

<body>
<div class="big_wrap">
	<div class="register_box">
		<div class="reg_title">
			<h2>忘记密码</h2>
		</div>
	<form id="registerForm" method="post">
		<dl class="forget1">
			<dt>手机号码：</dt>
			<dd>
				<input type="text" name="mobile" id="mobile" placeholder="请输入手机号码" maxlength="11" class="inp1">
				<span class="tip mobileTip">仅支持中国大陆地区</span>
			</dd>

			<dt>短信验证码：</dt>
			<dd>
				<input type="text" name="smsCode" id="smsCode" placeholder="请输入短信验证码"  maxlength="6" style="width:165px;"  class="inp1">
				<span class="sms_code" id="btnSmsCode">发送验证码</span>
				<span class="tip"><label id="tipCode"></label>&nbsp;</span>
				<input type="hidden" value="1" name="forget" id="forget"/>
			</dd>
			<dt></dt>
			<dd>
                <span class="submit_btn" id="btnRegister" style="width:323px;">提交信息</span>
			</dd>
		</dl>
	</form>
		<p></p>
	</div>
</div>
<script>

var sentIntv;
var initSecond=60;
var second,flag=true,isReg=false;

$(function(){
	//UserAction.forgetPass(String, String)
	$("#btnRegister").click(function(){
		$("#registerForm").ajaxSubmit({
			url:"${basePath}/UserAction/forgetPass.html",
			success:function(data){
				alert(data.msg);
				if(data>0){
					window.location.href="@{UserAction.loginit()}";
				}
			},
			error:function(data){
				alert(data.msg);
			}
		});
	});
	
	 $("#btnSmsCode").click(function(){
		 clickSendSms();
	 });
});

function checkParams(){
	var mobile=$("#mobile").val();
	var smsCode=$("#smsCode").val();
	
	if(!checkMobile(mobile)){
		$(".mobileTip").html("手机号码格式错误");
		$(".mobileTip").css("color","red");
		return ;
	}else{
		$(".mobileTip").html("");
		$(".mobileTip").css("color","gray");
	}
	if(smsCode==null){
		$("#tipCode").html("请输入验证码");
		$("#tipCode").css("color","red");
	}else{
		$("#tipCode").html("");
		$("#tipCode").css("color","gray");
	}
}

function clickSendSms(){
	 var info=checkMobile($("#mobile").val());
	 if(!info){
		 alert("请输入正确手机号！");
	 }
	 var forget=$("#forget").val();
	 if(info && flag){
		 $("#btnSmsCode").text("正在发送...");
		 $.ajax({
			type:'post',
			url:"${basePath}/UserAction/sendSMSCode.html",
			data:{"mobile":$("#mobile").val(),"forget":forget},
			success:function(data){
				if(1==data.code){
				    second=initSecond;
					btnSmsActive();
					sentIntv=self.setInterval("btnSmsActive()",1000);
			    }else{
			    	$("#btnSmsCode").text("重新发送");
			    }
				alert(data.msg);
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

function checkMobile(mobile){
	return RegExp.mobileNum.test(mobile);
}

</script>
<jsp:include page="/page/about.jsp" />
</body>
</html>