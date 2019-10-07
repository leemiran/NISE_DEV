<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<script type="text/javascript">
	//SSL 보안 페이지
	var c_url   = this.location+"";	
	if(c_url.match("https:")){		
		location.href="http://iedu.nise.go.kr/";
		//location.href="http://test.nise.go.kr/";		
	}
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonHead.jsp" %>

<script type="text/javascript">
	//공지사항 보기
	function doNoticeView(p_seq)
	{
		mainSubmitForm.menu_main.value = "4";
		mainSubmitForm.menu_sub.value = "1";
		mainSubmitForm.menu_tab_title.value = "참여마당";
		mainSubmitForm.menu_sub_title.value = "공지사항";

		mainSubmitForm.p_seq.value = p_seq;
		mainSubmitForm.p_tabseq.value = "12";
		mainSubmitForm.action = "/usr/bod/noticeView.do";
		mainSubmitForm.target = "_self";
		mainSubmitForm.submit();
			
	}
	//로그인
	function login(){
		
		var frm = document.getElementById("frmLogin");		
		
		//d_type
		frm.p_d_type.value = "T";		
		
		if(frm.userId.value.length < 1){alert("사용자 아이디를 입력해 주세요");frm.userId.focus();return false;}
		if(frm.pwd.value.length < 1){alert("사용자 비밀번호를 입력해 주세요");frm.pwd.focus();return false;}
    	var c_url = this.location+"";
    	var url = "https://iedu.nise.go.kr/usr/lgn/portalUserMainLogin.do";
    	//var url = "https://test.nise.go.kr/usr/lgn/portalUserMainLogin.do";
        if(c_url.match("localhost")){
        	url = "/usr/lgn/portalUserMainLogin.do";
        }
		frm.action = url;
    	frm.submit();
	} 

	//로그 아웃 처리
	function logout() {
	    var frm = document.getElementById("frmLogin");
	    if (confirm("로그아웃 하시겠습니까?")) {
	        frm.action = "/usr/lgn/portalUserLogout.do";
	        frm.submit();
	    } else {
	        return;
	    }
	}
	//권한변경
    function authChange() {
	    var frm = document.getElementById("frmLogin");
	    
	    //d_type
		frm.p_d_type.value = "T";
	  
        frm.action = "/usr/hom/portalActionMainPage.do";
        frm.submit();
    }
	//관리자 페이지 열기
	function adminOpenPage() {
	    var frm = document.getElementById("frmLogin");
	    
	    //d_type
		frm.p_d_type.value = "T";
	  
        window.self.name = "winSelectView";
        farwindow = window.open("", "openWinAdminSTX", "");
		frm.target = "openWinAdminSTX"
		frm.action = "/adm/com/main/admActionMainPage.do?s_menu=31040000&s_submenu=31050000";
        frm.submit();

        farwindow.window.focus();
        frm.target = window.self.name;
    }

	//엔터키
	function fn_keyEvent(fun){
		if( event.keyCode == 13 ){
			eval("this."+fun+"()");
		}
	}

	//과정 분류별로 보기
	function doSubjClsView(search_upperclass)
	{
		mainSubmitForm.search_upperclass.value = search_upperclass;
		mainSubmitForm.action = "/usr/subj/subjInfoList.do";
		mainSubmitForm.target = "_self";
		mainSubmitForm.submit();
	}

	
	//과정팝업 보기 : 로그인시, 로그아웃시 별도로 구성
	function doSubjView(param)
	{
		//과정상세로 가기
		<c:if test="${not empty sessionScope.userid}">
			var url = "/usr/subj/subjInfoView.do?p_target=view&"
				+ param;
			document.location.href = url;
		</c:if>
		//과정정보 팝업
		<c:if test="${empty sessionScope.userid}">
			var url = "/usr/subj/subjInfoViewPopUp.do?"
				+ param
			window.open(url,"searchSubjSeqInfoPopupWindowPop","width=820,height=650,scrollbars=yes");
		</c:if>
	}
	
	//과정 분류별로 리스트 보기 탭메뉴
	function doSubjTabView(idx)
	{
		for(var i=1; i<=5; i++)
		{
			var obj = eval("tab_sm" + i);
			var objImg = eval("tab_smimage" + i);

			
			if(i == idx)
			{
				obj.style.display = 'block';
				objImg.src = "/images/user/m_tab_menu0" + i + "_on.gif";
			}
			else
			{
				obj.style.display = 'none';
				objImg.src = "/images/user/m_tab_menu0" + i + "_off.gif";
			}
		}
	}
</script>

<!--  레이어 팝업 -->
<style type="text/css">
.pop_layer{position:absolute;z-index:1000000;display:none;border:1px solid #1d213f;font-size:11px;letter-spacing:-1px;font-family:dotum;background:#fff;top:100px;cursor: move;}
#pop_type_a*{background:transparent url(/images/adm/common/notice1_bg.jpg) no-repeat;width:473px;height:387px;border:1px solid #444;font-size:12px;}
#pop_type_a* .pop_content{overflow-y:auto;overflow-x:hidden;padding:110px 90px 0 100px; height:220px;}
#pop_type_a* .close{padding:20px 40px 0 90px; text-align:right;}
#pop_type_a* a.close2{display:inline-block;padding:3px 7px;margin:0 10px;background:#262626;color:#fff;border-radius:.3em;}

#pop_type_b*{overflow:hidden;background:transparent url(/images/adm/common/notice2_bg.gif) no-repeat;width:600px;height:500px;border:1px solid #444;font-size:12px;}
#pop_type_b* .pop_top{height:131px;text-align:center;}
#pop_type_b* .pop_bottom{color:#fff;padding:10px 40px 0 90px; text-align:right;background:transparent url(/images/adm/common/notice2_bottom.gif) no-repeat;height:80px}
#pop_type_b* .pop_content{overflow-y:auto;overflow-x:hidden;background:transparent url(/images/adm/common/notice2_center.gif) repeat-y; height:290px; padding:0px 50px 10px 50px;color:#fff;}
#pop_type_b* a.close2{display:inline-block;padding:3px 7px;margin:0 10px;background:#262626;color:#fff;border-radius:.3em;}

#pop_type_c*{background:transparent url(/images/adm/common/notice3_bg.jpg) no-repeat;width:560px;height:696px;border:1px solid #444;font-size:12px;}
#pop_type_c* .pop_content{overflow-y:auto;overflow-x:hidden;padding:120px 135px 15px 115px; height:520px;}
#pop_type_c* .close{padding:10px 40px 0 90px; text-align:right;color:#fff;}
#pop_type_c* a.close2{display:inline-block;padding:3px 7px;margin:0 10px;background:#262626;color:#fff;border-radius:.3em;}

.popup_layer1{position:relative;z-index:1000000;display:none;border:0;font-size:11px;letter-spacing:-1px;font-family:dotum;background:url(/images/user/popup_bg.gif);top:0px;left:240px;width:420px;height:120px;cursor:pointer}
.popup_layer1 #pop_content1{padding:12px 0px 0px 120px}
.popup_layer1 #pop_content2{padding:0px 0px 0px 120px}
</style>

</head>
<body>
<div id="u_skip">
	<a href="#gnb"><span>메뉴 영역으로 바로가기</span></a>
	<a href="#xmain"><span>본문 영역으로 바로가기</span></a>
</div>

<div id="xpop" style="display: none;">
	<div class="xpop_in">
		<div class="xpop_img">
			<img src="/images/user/topban1.png" alt="상단팝업" />
		</div>
		<div class="xnotice">
			<c:forEach items="${view}" var="result" varStatus="status">
				<div class="notice_con" style="layout: fixed;">
					<span class="alert_img"><img src="/images/user/xpop_notice.png" alt="notice" /></span>
					<dl class="dl_con">
						<dt class="dt_con" style="color:white;">${result.adtitle}</dt>
						<dd class="dd_con">
							<a href="#" onclick="doNoticeView('${result.seq}');return false;" style="color:white; display:block;"><c:out value="${result.adcontent }" escapeXml="false" /></a>
						</dd>
					</dl>
				</div>
				<c:if test="${!status.last}">
					<div class="notice_con2" style="layout:fixed;">
		  				<span class="alert_img"><img src="/images/user/topban4.png" alt="notice"></span>
	      			</div>
				</c:if>
			</c:forEach>
		</div>
		<div id="xpop_control">
			<label> <a href="#" onclick='javascript:closeFunOneDay()' class="close_bt2">[X]오늘 다시보지 않기</a></label>
			<a href="#" class="close_bt"><img src="/images/user/xpop2_close.png" alt="닫기" /></a>
		</div>
	</div>
</div>


<div id="popupArea" style='position:absolute;z-index:1000001;'></div>
<!--  레이어 팝업  --> 
<form id="mainSubmitForm" name="mainSubmitForm" method="post" onsubmit="return false;" action="">
	<input type="hidden" name="menu_main"		value=""/>
	<input type="hidden" name="menu_sub"		value=""/>
	<input type="hidden" name="menu_tab_title"	value=""/>
	<input type="hidden" name="menu_sub_title"	value=""/>
	<input type="hidden" name="p_tabseq"     value = "" />
	<input type="hidden" name="p_seq"	value=""/>
	<input type="hidden" name="p_subj"	value=""/>
	<input type="hidden" name="search_upperclass"	value=""/>
</form>

<!-- header -->
<%@ include file = "/WEB-INF/jsp/egovframework/svt/main/commonTop.jsp" %>
<!-- header -->

<!-- 컨텐츠 메인 -->
<div class="main_img"><img src="/images/main/topimg.jpg" alt="개성을 살리는 특수교육국립특수교육원이 함께 합니다 " /></div>

<div id="xmain">

	<!-- content03 -->
	<div id="content03">
		<!-- 로그인 -->
		<c:if test="${empty sessionScope.userid}">
			<div class="login" style="float: left; margin-left: 0px">
				<h1 style="font-size:0px;">로그인</h1>
				<form id="frmLogin" method="post" action="/usr/lgn/portalUserLogin.do" onsubmit="javascript:return login();">
					<input type="hidden" name="p_d_type" id="p_d_type" value="T" />
					
					<ul>
						<li>
							<input style="margin-left: 0px;" type="text" id="userId" name="userId" value="" size="13" onkeypress="fn_keyEvent('login')" title="사용자 아이디" /><label for="userId" class="blind">사용자 아이디</label>
						</li>
						<li style="align: left;">
							<input type="password" id="pwd" name="pwd" value="" size="13" onkeypress="fn_keyEvent('login')" title="사용자 비밀번호" /><label for="pwd" class="blind">사용자 비밀번호</label>
						</li>
						<li class="m_login">
							<a href="#" onclick="login();return false;"><img src="/images/user/m_login.gif" alt="로그인" /></a>
						</li>
						<li class="join">
							<a href="#" onclick="changeMenu(7, 1);return false;"><img src="/images/user/m_join.gif" alt="회원가입" /></a>
							<span style="padding-left: 10px;"><a href="#" onclick="changeMenu(7, 2);return false;"><img src="/images/user/m_idpw.gif" alt="아이디/비밀번호찾기" /></a></span>
						</li>
						<li>
							<a href="#" onclick="changeMenu(7, 3);return false;"><img src="/images/common/in_login.jpg" alt="인증서 로그인" /></a>
						</li>
					</ul>
				</form>
			</div>
		</c:if>
		<!-- //로그인 -->
		<!-- 로그인아웃 -->
		<c:if test="${not empty sessionScope.userid}">
			<div class="login ct" style="float: left; margin-left: 0px">
				<h1 style="font-size:0px;">로그아웃</h1>
				<form id="frmLogin" method="post" action="" onsubmit="javascript:return false;">
					<input type="hidden" name="p_d_type" id="p_d_type" value="O" />
					<fieldset>
						<legend>로그인</legend>
						<ul>
							<li style="height:20px;"><strong><c:out value="${sessionScope.name}" /></strong> 환영합니다.</li>
							<li>
								<label for="p_auth" class="blind">회원유형</label>
								<select id="p_auth" name="p_auth" onchange="authChange();return false;" title="회원유형">
									<c:forEach items="${authList}" var="result">
										<c:if test="${result.gadmin == p_auth}">
											<c:set var="selected" value="selected" />
										</c:if>
										<option value="${result.gadmin}" <c:if test="${result.gadmin == p_auth}">selected</c:if>><c:out value="${result.gadminnm}" /></option>
									</c:forEach>
									<option value="ZZ" <c:if test="${empty selected}">selected</c:if>>학습자</option>
								</select>
								<c:if test="${not empty p_auth and p_auth ne 'ZZ' and p_auth ne ''}">
									<a href="#" onclick="adminOpenPage();return false;"><img src="/images/user/btn_go.gif" alt="GO" /></a>
								</c:if>
							</li>
							<li class="join">
								<a href="#" onclick="changeMenu(6, 3);return false;"><img src="/images/user/m_edit.gif" alt="정보수정" /></a>
								<span style="padding-left: 10px;"><a href="#" onclick="logout();return false;"><img src="/images/user/m_logout.gif" alt="로그아웃" /></a></span>
							</li>
							<li style="padding-top: 10px;">
								<strong>${sessionScope.lglast} 최종접속</strong>
							</li>
						</ul>
					</fieldset>
				</form>
			</div>
		</c:if>
		<!-- 로그인아웃 -->
		
		<div class="kyotit"  >
		
		<!-- 공지사항 -->
		<div class="notice m1" style="float: left; width: 300px;margin-left:0px;margin-top:0px;border:0px solid #4ec53d;">
			<h1 style="font-size:0px;">공지사항</h1>
			<ul>
				<li class="m1"><a href="/usr/bod/noticeList.do" onclick="changeMenu(4, 1);return false;"><img src="/images/user/m_notice_tit.gif" alt="공지사항" /></a>
					<ul id="sm1" class="list">
						<c:forEach items="${topNoticeList}" var="result" varStatus="status">
							<li><a href="#none" onclick="doNoticeView('${result.seq}')" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; width: 100%;"> <c:if test="${result.noticeGubun eq 'A'}">
										<img src="/images/adm/ico/ico_notice.gif" alt="공지" />
									</c:if> <c:if test="${result.noticeGubun eq 'B'}">
										<img src="/images/adm/ico/ico_event.gif" alt="이벤트" />
									</c:if> <c:if test="${result.noticeGubun eq 'C'}">
										<img src="/images/adm/ico/ico_happy.gif" alt="축하" />
									</c:if> <c:if test="${result.noticeGubun eq 'D'}">
										<img src="/images/adm/ico/ico_guide.gif" alt="안내" />
									</c:if> <c:if test="${result.noticeGubun eq 'E'}">
										<img src="/images/adm/ico/ico_poll.gif" alt="설문" />
									</c:if> <c:if test="${result.noticeGubun eq 'F'}">
										<img src="/images/adm/ico/ico_busy.gif" alt="긴급" />
									</c:if> <c:if test="${result.noticeGubun eq 'G'}">
										<img src="/images/adm/ico/ico_others.gif" alt="기타" />
									</c:if> ${result.adtitle}
							</a></li>
						</c:forEach>
						<li class="more"><a href="/usr/bod/noticeList.do" onclick="changeMenu(4, 1);return false;"><img src="/images/user/btn_more.gif" alt="공지사항 더보기" /></a></li>
					</ul></li>
			</ul>
		</div>
		<!-- //공지사항 -->
		
		<!-- 연수안내 -->
		<div class="ys">
			<h1 style="font-size:0px;">연수안내</h1>
			<ul>
				<li class="yst">${calendarTitle.titleNm}</li>				
				<li class="yst3"><a href="#" onclick="changeMenu(1, 1);return false;"><img src="/images/user/dayclick.jpg" alt="전체일정 보기" /></a></li>
			</ul>
		</div>
		
		<div class="ys2">
			<h1 style="font-size:0px;">신청기간</h1>
			<ul>
				<li class="yst4">
					<img src="/images/user/one1.jpg" alt="신청기간" />
				</li>
				
				<li class="yst7"><c:if test="${not empty calendarPeriodList[0]}">${calendarPeriodList[0].requestPeriod}</c:if></li>

			</ul>
		</div>
		<div class="ys2">
			<h1 style="font-size:0px;">연수기간</h1>
			<ul>
				<li class="yst4">
					<img src="/images/user/one2.jpg" alt="연수기간" />
				</li>				
				<li class="yst7"><c:if test="${not empty calendarPeriodList[0]}">${calendarPeriodList[0].trainPeriod}</c:if></li>
			</ul>
		</div>
		
		</div>
		<!-- //연안내 -->
				
		
		<!-- 교육청 
		<div class="kyoicon">
			<ul style="float: right; margin-bottom: 2px">
				<li><a href="javascript:"><img src="/images/main/kyo01.jpg" alt="서울특별시 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo02.jpg" alt="부산광역시 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo03.jpg" alt="대구광역시 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo04.jpg" alt="인천광역시 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo05.jpg" alt="광주광역시 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo06.jpg" alt="대전광역시 교육청" /></a></li>
	
			</ul>
			<ul style="float: right; margin-bottom: 2px">
				<li><a href="javascript:"><img src="/images/main/kyo07.jpg" alt="울산광역시 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo08.jpg" alt="세종특별자치시 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo09.jpg" alt="경기도 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo10.jpg" alt="강원도 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo11.jpg" alt="충청북도 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo12.jpg" alt="충청남도 교육청" /></a></li>
			</ul>
			<ul style="float: right; margin-bottom: 2px">
				<li><a href="javascript:"><img src="/images/main/kyo13.jpg" alt="전라북도 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo14.jpg" alt="전라남도 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo15.jpg" alt="경상북도 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo16.jpg" alt="경상남도 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo17.jpg" alt="제주특별자치도 교육청" /></a></li>
				<li><a href="javascript:"><img src="/images/main/kyo18.jpg" alt="교육청소속 외(교양연수)" /></a></li>
			</ul>
		</div>
		 //교육청 -->
	</div>
	<!-- // content03 -->

	<!-- #container -->
	<div class="ysinfor">
		<img src="/images/user/ysinfo.jpg" height="43" width="114" alt="연수안내">
	</div>
	<link href="http://fonts.googleapis.com/css?family=Raleway" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="/css/sea.tabs.Default.css" media="all">
	<link rel="stylesheet" href="/css/sea.tabs.skinGrey.css" media="all">
	<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
	<script src="/js/sea.tabs.js"></script>
	<link href="http://www.jqueryscript.net/css/jquerysctipttop.css" rel="stylesheet" type="text/css">
	<div class="seaTabs">
		<h1 style="font-size:0px;">연수안내</h1>
		<div class="seaTabs_switch">
			<div class="seaTabs_tab seaTabs_switch_active"><a href="#none">교원직무</a></div>
			<div class="seaTabs_tab"><a href="#none">특수교육보조인력</a></div>
			<div class="seaTabs_tab"><a href="#none">교양연수</a></div>
		</div>
		<script type="text/javascript" charset="utf-8">
		var pages = {};
		(function () {
		    $.fn.infiniteCarousel = function () {
		        function repeat(str, n) {
		            return new Array( n + 1 ).join(str);
		        }
		        
		        return this.each(function () {
		        	var idx = $('.infiniteCarousel').index(this);
		        	
		            // magic!
		            var $wrapper = $('> div', this).css('overflow', 'hidden'),
		                $slider = $wrapper.find('> ul').width(9999),
		                $items = $slider.find('> li'),
		                $single = $items.filter(':first')
		                
		                singleWidth = $single.outerWidth(),
		                visible = Math.ceil($wrapper.innerWidth() / singleWidth),
		                currentPage = 1;
		            
		            pages[idx] = Math.ceil($items.length / visible);
		                
		            /* TASKS */
		            
		            // 1. pad the pages with empty element if required
		            if ($items.length % visible != 0) {
		                // pad
		                $slider.append(repeat('<li class="empty" />', visible - ($items.length % visible)));
		                $items = $slider.find('> li');
		            }
		            
		            // 2. create the carousel padding on left and right (cloned)
		            $items.filter(':first').before($items.slice(-visible).clone().addClass('cloned'));
		            $items.filter(':last').after($items.slice(0, visible).clone().addClass('cloned'));
		            $items = $slider.find('> li');
		            
		            // 3. reset scroll
		            $wrapper.scrollLeft(singleWidth * visible);
		            
		            // 4. paging function
		            function gotoPage(page) {
		                var dir = page < currentPage ? -1 : 1,
		                    n = Math.abs(currentPage - page),
		                    left = singleWidth * dir * visible * n;
		                
		                $wrapper.filter(':not(:animated)').animate({
		                    scrollLeft : '+=' + left
		                }, 500, function () {
		                    // if page == last page - then reset position
		                    if (page > pages[idx]) {
		                        $wrapper.scrollLeft(singleWidth * visible);
		                        page = 1;
		                    } else if (page == 0) {
		                        page = pages[idx];
		                        $wrapper.scrollLeft(singleWidth * visible * pages[idx]);
		                    }
		                    
		                    currentPage = page;
		                });
		            }
		            
		            // 5. insert the back and forward link
		            $wrapper.after('<a href="#" class="arrow back" title="오른쪽으로 이동 화살표"></a><a href="#" class="arrow forward" title="왼쪽으로 이동 화살표"></a>');
		            
		            // 6. bind the back and forward links
		            $('a.back', this).click(function () {
		                gotoPage(currentPage - 1);
		                return false;
		            });
		            
		            $('a.forward', this).click(function () {
		                gotoPage(currentPage + 1);
		                return false;
		            });
		            
		            $(this).bind('goto', function (event, page) {
		                gotoPage(page);
		            });
		                        
		            $(this).bind('next', function () {
		                gotoPage(currentPage + 1);
		            });
		        });
		    };
		})(jQuery);
		
		$(document).ready(function () {    
		    var autoscrolling = true;
		    
		    $('.infiniteCarousel').each(function () {
		    	if($(this).find('li').length > 4) {
				    $(this).infiniteCarousel().mouseover(function () {
				        autoscrolling = false;
				    }).mouseout(function () {
				        autoscrolling = true;
				    });			    
				    $(this).find('a').focus(function () {
		    			autoscrolling = false;
				    }).blur(function () {
		    			autoscrolling = true;
				    });	
		    	}
		    });
		    
		    setInterval(function () {
		        if (autoscrolling) {
		            $('.seaTabs_content_active .infiniteCarousel').trigger('next');
		        }
		    }, 3000);
		});
		</script>
		<div class="seaTabs_content">
			<div class="seaTabs_item seaTabs_content_active" style="height: 270px;">
				<div class="infiniteCarousel">
					<div class="wrapper" style="overflow: hidden;">
						<ul style="width: 970px;">
							<c:forEach var="trainSubj" items="${trainSubjList}">
								<c:if test="${'1' eq trainSubj.trainSeq}">
									<li class="cloned"><a href="#" style="display:block" onclick="doSubjView('${trainSubj.linkUrl}');return false;" <c:if test="${empty sessionScope.userid}">title="새창"</c:if>><img src="/dp/train/${trainSubj.imgId}" height="233" width="206" alt="${trainSubj.subjNm}" onerror="this.src='/images/edu01.jpg'"></a></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
			<div class="seaTabs_item" style="height: 270px;">
				<div class="infiniteCarousel">
					<div class="wrapper" style="overflow: hidden;">
						<ul style="width: 970px;">
							<c:forEach var="trainSubj" items="${trainSubjList}">
								<c:if test="${'2' eq trainSubj.trainSeq}">
									<!-- <li class="cloned"><a href="#" title=""><img src="/images/edu03.jpg" height="233" width="206" alt="교육1"></a></li> -->
									<li class="cloned"><a href="#" style="display:block" onclick="doSubjView('${trainSubj.linkUrl}');return false;" <c:if test="${empty sessionScope.userid}">title="새창"</c:if>><img src="/dp/train/${trainSubj.imgId}" height="233" width="206" alt="${trainSubj.subjNm}" onerror="this.src='/images/edu01.jpg'"></a></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
			<div class="seaTabs_item" style="height: 270px;">
				<div class="infiniteCarousel">
					<div class="wrapper" style="overflow: hidden;">
						<ul style="width: 970px;">
							<c:forEach var="trainSubj" items="${trainSubjList}">
								<c:if test="${'3' eq trainSubj.trainSeq}">
									<!-- <li class="cloned"><a href="#" title=""><img src="/images/edu02.jpg" height="233" width="206" alt="교육1"></a></li> -->
									<li class="cloned"><a href="#" style="display:block" onclick="doSubjView('${trainSubj.linkUrl}');return false;" <c:if test="${empty sessionScope.userid}">title="새창"</c:if>><img src="/dp/train/${trainSubj.imgId}" height="233" width="206" alt="${trainSubj.subjNm}" onerror="this.src='/images/edu01.jpg'"></a></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- content02 -->
	<!-- <div id="content02"> -->
		<div id="adcontentAreaTemp" style="display: none;">
			<c:forEach items="${view}" var="result" varStatus="status">
				<span><c:out value="${result.adcontent }" escapeXml="false" /></span>
			</c:forEach>
		</div>
		

		

		<!-- 문의안내 -->
		<!-- <div class="cso">
			<ul>
				<li><img src="/images/user/m_tit04_1.gif" alt="고객센터 평일:09:30~17:50 점심:11:50~13:00 원격연수운영팀 041)537-1475~9 기술문의/원격지원 02-6345-6787" /></li>
				<li><a href="https://988.co.kr/ek" onclick="window.open('https://988.co.kr/ek');return false;" title="새창"><img src="/images/user/oneclick.jpg" alt="PC원격지원서비스" /></a></li>
			</ul>
		</div> -->		
		<div class="cso">
			<h1 style="font-size:0px;">고객센터</h1>
			<img src="/images/user/cso1.gif" alt="고객센터 평일:09:30~17:50 점심:11:50~13:00 원격연수운영팀 041)537-1475~9 기술문의/원격지원 02-6345-6787" />
			<a href="https://988.co.kr/ek" onclick="window.open('https://988.co.kr/ek');return false;" title="새창"><img src="/images/user/cso2.gif" alt="PC원격지원서비스" /></a>
		</div>
		
	<!-- </div> -->
	<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonMainBottom.jsp" %>
</div>
<!-- // content02 -->

<script type="text/javascript">
	function notice_getCookie(name) {
	    var nameOfCookie = name + "=";
	    var x = 0;
	    while (x <= document.cookie.length) {
	        var y = (x+nameOfCookie.length);
	        if (document.cookie.substring( x, y ) == nameOfCookie) {
	            if ((endOfCookie=document.cookie.indexOf( ";", y )) == -1)
	                endOfCookie = document.cookie.length;
	            return unescape( document.cookie.substring( y, endOfCookie ));
	        }
	        x = document.cookie.indexOf( " ", x ) + 1;
	        if ( x == 0 )
	            break;
	    }
	    return "";
	}
	
	function notice_setCookie( name, value, expiredays )
	{
	    var todayDate = new Date();
	    todayDate.setDate( todayDate.getDate() + expiredays );
	    document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
	}
	function closeFunOneDay() 
	{ 
		//$(target).parent().parent().parent().fadeOut("fast")
		notice_setCookie( "popUpPreview", "done" , 1); // 1=하룻동안 공지창 열지 않음
		//$("#xpop").animate({
		///    "margin-top": -204,
		//}); 
		$("#xpop").hide();
	}
</script>
<script type="text/javascript">
	$(document).ready(function(){
		<c:if test="${fn:length(popupListAttr) > 0}">
		if ( notice_getCookie( 'popUpPreview' ) != "done" ) {
			$("#xpop").show();
		}
		</c:if>
		
		$("#xpop .close_bt").click(function(e){
		  e.preventDefault();
		  $("#xpop").hide();
		})
		setToday();
		
		var areaCode = ['B10','C10','D10','E10','F10','G10','H10','I10','J10','K10','M10','N10','P10','Q10','R10','S10','T10','A00'];
		$('.kyoicon a').on('click', function() {
			$inputAreaCode = $('<input type="hidden" name="areaCode"/>');
			$inputAreaCode.val(areaCode[$('.kyoicon a').index($(this))]);
			$('#mainSubmitForm').append($inputAreaCode);
			
			mainSubmitForm.menu_main.value = "2";
			mainSubmitForm.menu_sub.value = "1";
			mainSubmitForm.menu_tab_title.value = "연수신청";
			mainSubmitForm.menu_sub_title.value = "신청 가능한 연수";

			mainSubmitForm.action = "/usr/subj/subjInfoList.do";
			mainSubmitForm.target = "_self";
			mainSubmitForm.submit();
		});
	});
	
	
	function setToday() {
		var d = new Date();
		var week = new Array('일', '월', '화', '수', '목', '금', '토');
		$('#today').html(new Date().format("yyyy.MM.dd E"));
	}
	
	Date.prototype.format = function(f) {
	    if (!this.valueOf()) return " ";
	 
	    var weekName = ["일", "월", "화", "수", "목", "금", "토"];
	    var d = this;
	     
	    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
	        switch ($1) {
	            case "yyyy": return d.getFullYear();
	            case "yy": return (d.getFullYear() % 1000).zf(2);
	            case "MM": return (d.getMonth() + 1).zf(2);
	            case "dd": return d.getDate().zf(2);
	            case "E": return weekName[d.getDay()];
	            case "HH": return d.getHours().zf(2);
	            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
	            case "mm": return d.getMinutes().zf(2);
	            case "ss": return d.getSeconds().zf(2);
	            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
	            default: return $1;
	        }
	    });
	};
	String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
	String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
	Number.prototype.zf = function(len){return this.toString().zf(len);};
</script>