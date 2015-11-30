<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.team.mine.util.Settings"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	session.setAttribute("keywords", "");
	session.setAttribute("subTitle", "");
%>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" type="image/png" href="${cdn}/resources/images/fav.gif">
<link rel="stylesheet" href="${cdn}/resources/stylesheets/basic.css" />
<link rel="stylesheet" href="${cdn}/resources/stylesheets/public.css" />
<link rel="stylesheet" href="${cdn}/resources/stylesheets/main.css" />
<link rel="stylesheet" href="${cdn}/resources/stylesheets/member.css" />
<link rel="stylesheet" href="${cdn}/resources/stylesheets/validform.css" />
<link rel="stylesheet" href="${cdn}/resources/stylesheets/idangerous.swiper.css">
<link rel="stylesheet" href="${cdn}/resources/stylesheets/jquery.fancybox.css">
<link rel="stylesheet" href="${cdn}/resources/stylesheets/index-home.css" />

<script src="${cdn}/resources/javascripts/jquery-1.10.1.min.js" type="text/javascript"></script>
<script src="${cdn}/resources/javascripts/main.js"></script>
<script src="${cdn}/resources/javascripts/idangerous.swiper.min.js"></script>
<script src="${cdn}/resources/javascripts/Validform_v5.3.2_min.js"></script> 
<script src="${cdn}/resources/javascripts/RegExp-ext.js"></script>
<script src="${cdn}/resources/javascripts/jquery.form.js"></script>
<script src="${cdn}/resources/javascripts/jquery.md5.js"></script>