<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE>
<%
	session.setAttribute("hideSearchBar", true);
	session.setAttribute("isHideMyFavor", true);
%>
<html>
<head>
<jsp:include page="/page/main.jsp" />
<jsp:include page="/page/head.jsp" />
<link rel="stylesheet" type="text/css"
	href="${cdn}'/resources/stylesheets/guide.css" />
<title>会员购买</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<!--end head-->
	<div class="big_wrap" style="border-top: 1px #ddd solid;">
		<div class="device">
			<a class="arrow-left" href="#"></a> <a class="arrow-right" href="#"></a>
			<div class="swiper-container">
				<div class="swiper-wrapper">
					<div class="swiper-slide hdp1">
						<div class="hdp_left">
							<img src="${cdn}/resources/images/ydy1.jpg" width="100%">
						</div>
						<div class="ydp_right">
							<h1>
								<img src="${cdn}/resources/images/ydy7.jpg'}">
							</h1>
							<p>Duncan先生一手创立了323趋势网</p>
							<p>323趋势网拥有全球最权威的时尚领域专家团队，网罗最新鲜的国际潮流资讯，以一手资讯来源、敏锐捕捉力、精准高端品味以及独立创新研发能力，成为中国服饰流行趋势输出的权威源头，以及时尚标杆参照，为服装企业、设计师提供时尚趋势分析，灵感来源，色彩、面料、款式、细节等设计素材，以及专业化的企划案例，使客户足不出户便可获取高性价比的国际最快、最新、最潮的优质时尚资源，为客户提升品牌竞争力、优化产品质量、抢占中国服饰市场提供强大后备信息来源和策划力量。</p>
						</div>
					</div>
					<div class="swiper-slide hdp2">
						<div class="m1">
							<img src="${cdn}/resources/images/ydy4.jpg'}">
							<div class="font">
								<h2>趋势分析简介</h2>
								<div class="clear"></div>
								<p>从品牌、风格、色彩、廓型、细节、面料、印花、展会、配饰、街拍等方面，专业而全面地解读流行趋势</p>
							</div>
						</div>
					</div>
					<div class="swiper-slide hdp3">
						<div class="login_reg">
							<div class="title">
								<img src="${cdn}/resources/images/ydy6.jpg">
							</div>
							<div class="img">
								<img src="${cdn}/resources/images/ydy5.jpg" width="688"
									height="322">
							</div>
							<div class="content">
								<p>展示伦敦、巴黎、米兰、纽约及其他地区的T台发布会，呈现最新的国际流行动态。</p>
								<div class="login_btn">
									<%
										com.team.mine.domain.UserAccount cacheUser = com.team.mine.util.CacheUtil
												.getCurrUserAccount(request);
									%>
									<%
										if (cacheUser == null) {
									%>
									<input type="button" value="会员登录" class="login"
										onClick="window.location.href='${basePath}/UserAction/loginit.html">
									<input type="button" value="立即注册" class="reg"
										onclick="window.location.href='${basePath}/UserAction/registerInit.html">
									<%
										}
										if (cacheUser.getVipLevel() == 0) {
									%>
									<input type="button" value="购买会员" class="login"
										onClick="window.location.href='${basePath}/PaymentAction/selectItem.html">
									<%
										}
									%>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="pagination" style="display: none"></div>
		</div>
		<script src="${cdn}/resources/javascripts/idangerous.swiper.min.js"></script>
		<script>
  var mySwiper = new Swiper('.swiper-container',{
    pagination: '.pagination',
	autoplay : 5000,//可选选项，自动滑动环
    loop:true,
  })
  $('.arrow-left').on('click', function(e){
    e.preventDefault()
    mySwiper.swipePrev()
  })
  $('.arrow-right').on('click', function(e){
    e.preventDefault()
    mySwiper.swipeNext()
  })
  </script>
	</div>
	<jsp:include page="/page/about.jsp" />
</body>
</html>