<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	//二维码隐藏显示
	/* $(document).ready(function(){
		$("#weixin").mousemove(function(){
			$(".rs_weixin").css("display","block");
		});
			  
		$("#weixin").mouseout(function(){
			$(".rs_weixin").css("display","none");
		});
	}); */
</script>
<div class="top_bar">
  <div class="center2">
    <div class="social_bar"> 
    	<a href="javascript:;" id="weixin">
	  		<img src="${cdn}/resources/images/rs_weixin.jpg" width="120" height="120" class="rs_weixin" style="background:none;">
		</a>
        <a class="qq_icon" href="tencent://message/?uin=308405833"></a> 
        <a class="jiathis_button_tsina weibo_icon" href="#.html"></a> 
        <!-- JiaThis Button BEGIN --> 
        <script type="text/javascript" src="http://v2.jiathis.com/code/jia.js" charset="utf-8"></script> 
        <!-- JiaThis Button END --> 
        <a href="tel:4001882662" style="color: #FFF;">客服热线：</a> 
    </div>
    <div class="sub_rs_bar">
      <ul>
        <li>
        <c:if test="${!sessionScope.isHideMyFavor}">
        <li> <a class="fav_icon" href="${basePath }/FavorFolderAction/listMyFolder.html?channelId=${channelId}">我的最爱</a> </li>
        </c:if>
        <c:if test="${sessionScope.cacheUser!=null}">
        <li>
        <a class="track_icon" href="${basePath }/AccessAction/latelyAccess.html">我的足迹</a></li>
        </c:if>
        <%com.team.mine.domain.UserAccount cacheUser = com.team.mine.util.CacheUtil.getCurrUserAccount(request);%>
        <%if (cacheUser==null){ %>
        <li><a href="${basePath}/UserAction/loginit.html">登录</a></li>
        <li><a href="${basePath}/UserAction/registerInit.html">注册</a></li>
        <%}else{ %>
        <li><a href="${basePath}/UserAction/userinfo.html"><%=cacheUser.getNickName()!=null?cacheUser.getNickName():cacheUser.getAccountName() %></a></li>
        <li><a href="${basePath}/UserAction/logout.html">退出</a></li>
        <%}%>
      </ul>
    </div>
  </div>
</div>
<div class="head_wrap">
   <c:if test="${!sessionScope.hideSearchBar}">
  <div class="search_bar">
     
      <form action="http://www.rstrend.com/search" id="form1">
      <input type="hidden" name="sex" value="${sex}" />
      <div class="txt">
      	<input type="text" name="keywords" value="${prdName}" placeholder="请输入关键字">
      </div>
      <div class="btn">
      	<input type="submit" value="">
      </div> 
      </form>
  </div>
  </c:if>
</div>
<%
	session.setAttribute("isHideMyFavor", false);
	session.setAttribute("hideSearchBar", false);
%>
