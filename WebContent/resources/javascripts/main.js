
$(function () {

			 
	$(document).ready(function(){
		$("#rs_weixin").mousemove(function(){
			$(".rs_weixin1").css("display","block");
		});
			  
		$("#rs_weixin").mouseout(function(){
			$(".rs_weixin1").css("display","none");
		});
	}); 
	

			
			$(".advanced_filter").click(function (){
										$(this).toggleClass("expand");
										$(this).prev(".filter_box").find(".advanced_box").stop().slideToggle(300);										
										$(this).hasClass("expand")? $(this).html("<span>收起选项</span>"):$(this).html("<span>更多选项</span>");
										});	
			
			
			var tt_Swiper = new Swiper("#tt_swiper",{
										
										mode:"vertical",
										loop : true,
										pagination:".pagination",
										paginationClickable:true,
										autoplay:5000
										});
			  
			var roll_Swiper = new Swiper("#roll_swiper",{
											 loop:true,
											 slidesPerView:"auto"
											 });
				
			$(".roll_banner_wrap .arrow-left").on("click", function(e){
																		e.preventDefault();
																		roll_Swiper.swipePrev();
																		});
				
			$(".roll_banner_wrap .arrow-right").on("click", function(e){
																		 e.preventDefault();
																		 roll_Swiper.swipeNext();
																		 });
			
			var details_swiper = $("#details_swiper").swiper({
															onSlideChangeStart: function(){
																updateNavPosition();
																}
																});
																
var mySwiper = new Swiper('#details_swiper',{
onInit: function(swiper){
      swiper.swipeNext()
    }
})
$('.arrow-left1').click(function(){
mySwiper.swipePrev(); 
})
$('.arrow-right1').click(function(){
mySwiper.swipeNext(); 
})			
			
			var details_swiper_nav = $('#details_swiper_nav').swiper({
													visibilityFullFit: true,
													slidesPerView:'auto',
													//Thumbnails Clicks
													onSlideClick: function(){
														details_swiper.swipeTo( details_swiper_nav.clickedSlideIndex )}
														});		
			
			$(".details_swiper_nav .arrow-left").on("click", function(e){
																	  e.preventDefault();
																	  details_swiper_nav.swipePrev();
																	  });
			
			$(".details_swiper_nav .arrow-right").on("click", function(e){
																	   e.preventDefault();
																	   details_swiper_nav.swipeNext();
																	   });
			
			$(".pop_title > span").click( function(){										   
										   $(".details_pop_div").stop().animate({top:"28%",opacity:"0"},"0.2",function(){
																											  $(this).hide();
																											  });
										   $(".details_overlay").hide();
										   });
			
			$(".swiper_pop").click( function(){
											 $(".details_overlay").show();
											 $(".details_pop_div").show().stop().animate({top:"18%",opacity:"1"},"0.2");
											 });
						
			
	function updateNavPosition(){
		$('#details_swiper_nav .active-nav').removeClass('active-nav')
		var activeNav = $('#details_swiper_nav .swiper-slide').eq(details_swiper.activeIndex).addClass('active-nav')
		if (!activeNav.hasClass('swiper-slide-visible')) {
			if (activeNav.index()>details_swiper_nav.activeIndex) {
				var thumbsPerNav = Math.floor(details_swiper_nav.width/activeNav.width())-1;
				details_swiper_nav.swipeTo(activeNav.index()-thumbsPerNav);
			}
			else {
				details_swiper_nav.swipeTo(activeNav.index());
			}	
		}
	}
	
	
			var details_swiper_v2 = $("#details_swiper_v2").swiper({
															onSlideChangeStart: function(){
																updateNavPosition_v2();
																}																
																});
			
			var details_swiper_nav_v2 = $('#details_swiper_nav_v2').swiper({
													visibilityFullFit: true,
													slidesPerView:'auto',
													mode: 'vertical',
													//Thumbnails Clicks
													onSlideClick: function(){
														details_swiper_v2.swipeTo( details_swiper_nav_v2.clickedSlideIndex )}
														});
			
			$(".details_swiper_nav_v2 .arrow-up").on("click", function(e){
																	  e.preventDefault();
																	  details_swiper_nav_v2.swipePrev();
																	  });
			
			$(".details_swiper_nav_v2 .arrow-down").on("click", function(e){
																	   e.preventDefault();
																	   details_swiper_nav_v2.swipeNext();
																	   });
			
		function updateNavPosition_v2(){
		$('#details_swiper_nav_v2 .active-nav').removeClass('active-nav')
		var activeNav = $('#details_swiper_nav_v2 .swiper-slide').eq(details_swiper_v2.activeIndex).addClass('active-nav')
		if (!activeNav.hasClass('swiper-slide-visible')) {
			if (activeNav.index()>details_swiper_nav_v2.activeIndex) {
				var thumbsPerNav = Math.floor(details_swiper_nav_v2.height/activeNav.height())-1
				details_swiper_nav_v2.swipeTo(activeNav.index()-thumbsPerNav)
			}
			else {
				details_swiper_nav_v2.swipeTo(activeNav.index())
			}	
		}
	}
	
						


			
			});



