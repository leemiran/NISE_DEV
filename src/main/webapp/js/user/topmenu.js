$(document).ready(function(){


	$('#gnb > ul > li').mouseover(function(){
		//$('#gnb .close').show();
		$('.depth02').slideDown();
	});

	function gnbOut(){
		$('.depth02').stop().slideUp();
		//$('#gnb .close').hide();
	};
	$('#header').mouseleave(gnbOut);
	
	
	$('#gnb > ul > li').find('a').focus(function(){		
		$('.depth02').slideDown();	
	});
	
	$('.logo').find('a').focus(function(){
		gnbOut();
	});
	
	$('#menu_end').focus(function(){
		gnbOut();
	});
	

	function gnbOn_healthy(){
		$('#gnb .close').show();
		$(this).next().show().removeClass('on');
		$('.depth02').css('display','none');
		
	};


	
	function gnb_healthyOff(e){
		e.preventDefault();
		$('.healthy_depth').hide().removeClass('on');
	};
	$('#header').mouseleave(gnb_healthyOff);

	function gnbClose(e){
		e.preventDefault();
		$(this).parent().removeClass();
		$('.depth02').stop().slideUp();
	};
	$('#gnb > .close').click(gnbClose);


//window.onload = function(){ 
		//$(".pop_banner_dv").slideDown();
//	}; 

	//$(".close_btn a").click(function(){
	//	$(this).find("img").css({display:"none"});
	//	$(".pop_banner_dv").stop().slideUp();
	//	$(".pop_banner_dv").stop().animate({height:"0px"});
	//	$(".pop_banner_dv").css({display:"none"});
	//});
	//$("#close_btn_1").click(function () {
		  //  $(".pop_banner_dv").slideUp();
	//});



/*$('#articles').SimpleSlider({
		imgX : 191//이미지 넓이 픽셀
		, spacing : 8//이미지 간격 픽셀
		, speed : 500//슬라이드 속도 (클수록 늦게)
		, delay : 2000//자동 플레이 간격
		, auto : true//자동 플레이 스위키 true / false
	});*/


/*$('#slider001').flexslider({
	animation: "fade",
	animationLoop: true,
	smoothHeight: false,
	slideshow: true,
	slideshowSpeed:5000,
	animationSpeed: 1500,
	controlNav: true,
	directionNav: true,
	prevText: ".",
	nextText: ".",
	pausePlay: true
});*/

$(".left_menu_ul1 > li > a").click(function(){
		if($(this).next('.lf_s_menu').slideDown('fast').height()  > 1){
			$(".lf_s_menu").slideUp('fast');
		}else{
			$(".lf_s_menu").css('display','none');
			$(".left_menu_ul1 > li").removeClass('current_dd');
			$(".left_menu_ul1 > li > a").removeClass('lf_mouse_on');
			$(".left_menu_ul1 > li").css('borderTop','0');
			$(this).parent().addClass('current_dd');
			$(this).addClass('lf_mouse_on');
			$(this).next('.lf_s_menu').slideDown('fast');
			$(".bor_tp").css('borderTop','0');
			$(this).parent().next().css('borderTop','0px');
		}
	});


$(".flexslider").not("#slider001").each(function(k,e){
	var Main_S_flex_right=($(e).find('.flex-control-nav').outerWidth(true))+"px";
	$(e).find(".flex-direction-nav").css("right",Main_S_flex_right);

});

$('.flexslider').each(function(k,e){
	$(".slides > li > a",e).each(function(c,v){
		$(v).focus(function(){
		
			//$(".flex-control-nav  a",e).eq(c).click();
			
			
			$(".flex-control-nav  a.flex-active",e).removeClass("flex-active");
			$(".flex-control-nav a",e).eq(c).addClass("flex-active");
			
			$(".slides li.flex-active-slide",e).stop().animate({
				opacity:"0"
			},600).css("z-index","1").removeClass("flex-active-slide");
			
			$(this).parent("li").stop().animate({
				opacity:"1"
			},600).css("z-index","2").addClass("flex-active-slide");
			
		});
		
	});
});
});

function noEvent() {
    if (event.keyCode == 116) {
        event.keyCode= 2;
        return false;
    } else if(event.ctrlKey && (event.keyCode==78 || event.keyCode == 82)) {
        return false;
    }
}

// document.onkeydown = noEvent;

function gotwo(n){
	
	for(var i=1;i<=2;i++){
		if(i != n){	
			document.getElementById('main_tab'+i).style.display="none";
			document.getElementById('main_tab'+i+'_on').style.display="none";
			document.getElementById('main_tab'+i+'_off').style.display="block";
		}
	}
	document.getElementById('main_tab'+n).style.display="block";
	document.getElementById('main_tab'+n+'_on').style.display="block";
	document.getElementById('main_tab'+n+'_off').style.display="none";
}

function gotwo1(n){
	
	for(var i=1;i<=2;i++){
		if(i != n){	
			document.getElementById('main_tab'+i).style.display="none";
			document.getElementById('main_tab'+i+'_on').style.display="none";
			document.getElementById('main_tab'+i+'_off').style.display="block";
		}
	}
	document.getElementById('main_tab'+n).style.display="block";
	document.getElementById('main_tab'+n+'_on').style.display="block";
	document.getElementById('main_tab'+n+'_off').style.display="none";
}
	var vbm = 100;

	function plus() {

				vbm = vbm + 20;

				if(vbm >= 500) vbm = 500;

				processes();

		}

	function processes(){

			if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
				
			}else{
					document.body.style.zoom = vbm + '%';
			}
		

	}






