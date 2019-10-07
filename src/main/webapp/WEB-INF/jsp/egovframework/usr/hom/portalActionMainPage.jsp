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
	}
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonHead.jsp" %>

<script type="text/javascript">
//<![CDATA[	
           

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
	function doSubjView(p_subj, p_subjseq)
	{
		<c:if test="${empty sessionScope.userid}">
			whenSubjInfoView(p_subj, '', '');
		</c:if>
		<c:if test="${not empty sessionScope.userid}">
			whenSubjInfoView(p_subj, 'view', p_subjseq);
		</c:if>

	}
	
	
	//과정 상세정보 팝업
	function whenSubjInfoView(p_subj, target, p_subjseq) {
		
		//과정상세로 가기
		if(target == 'view')
		{
			var url = "/usr/subj/subjInfoView.do"
				+ "?p_subj=" + p_subj
				+ "&p_target=" + target
				+ "&p_subjseq=" + p_subjseq
				;
			document.location.href = url;
		}
		//과정정보 팝업
		else
		{
			var url = "/usr/subj/subjInfoViewPopUp.do"
				+ "?p_subj=" + p_subj
				;
			window.open(url,"searchSubjSeqInfoPopupWindowPop","width=820,height=650,scrollbars=yes");
		}
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
//]]>    
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
<body >
<div id="u_skip"> <a href="#gnb"><span>메뉴 영역으로 바로가기</span></a> <a href="#content01"><span>본문 영역으로 바로가기</span></a> </div>

<div id="xpop" style="display:none;">
  <div class="xpop_in">
    <div class="xpop_img"><img src="/images/user/xpop_img.png" alt="상단팝업"/></div>
    <div class="xnotice">
    <c:forEach items="${view}" var="result" varStatus="status">
      <div class="notice_con" style="layout:fixed;"> <span class="alert_img"><img src="/images/user/xpop_notice.png" alt="notice"/></span>
        <dl class="dl_con">
          <dt class="dt_con">${result.adtitle}</dt>
          <dd class="dd_con"><a href="#" onclick="doNoticeView('${result.seq}')" style="display:block"><c:out value="${result.adcontent }"  escapeXml="false" /></a></dd>
        </dl>
      </div>
    </c:forEach>
    </div>
    <div id="xpop_control">
      <label>
      <a href="#" onclick='javascript:closeFunOneDay()'>[X]오늘 다시보지 않기</a></label>
      <a href="#" class="close_bt"><img src="/images/user/xpop_close.png" alt="닫기"/></a></div>
  </div>
</div>
<!--<div id="popup_layer" class="popup_layer1" >
	<div id="pop_content1" >
		<c:forEach items="${view}" var="result" varStatus="status">
			<ul>
				<li>
					${result.adtitle}
				</li>
				<li>
					<c:out value="${result.adcontent }"  escapeXml="false" />
				</li>
			</ul>
		</c:forEach>
	</div>
</div> 
--><div id="popupArea" style='position:absolute;z-index:1000001;'></div>
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

<!-- wrapper -->
<div id="xmain">
<div id="wrapper" >

	<!-- header -->
	<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonTop.jsp" %>
	<!-- header -->
    <!-- visual -->
    <!-- <div id="visual"><img src="/images/user/m_visual.jpg" alt="개성을 살리는 특수교육, E-LEARNING , 국립특수교육원이 함께합니다. 국립특수교육원은 교직원 연수 및 학생들에게 바른 인성을 갖춘 창의적인 인간육성과 미래지향적인 학생상 정립을 위해 다양한 연수 활동을 전개하고 있습니다." /></div> -->
    <div id="visual">
    
    	
    	
    	<div id="jqb_object">
	
			<div class="jqb_slides">
				<div class="jqb_slide" ><img src="/images/user/m_visual.jpg" style="width:971px; height:268px;" alt="개성을 살리는 특수교육, E-LEARNING , 국립특수교육원이 함께합니다. 국립특수교육원은 교직원 연수 및 학생들에게 바른 인성을 갖춘 창의적인 인간육성과 미래지향적인 학생상 정립을 위해 다양한 연수 활동을 전개하고 있습니다." /></div>
				<div class="jqb_slide" ><img src="/images/user/m_visual_1.jpg" style="width:971px; height:268px;" alt="개성을 살리는 특수교육, E-LEARNING , 국립특수교육원이 함께합니다. 국립특수교육원은 교직원 연수 및 학생들에게 바른 인성을 갖춘 창의적인 인간육성과 미래지향적인 학생상 정립을 위해 다양한 연수 활동을 전개하고 있습니다." /></div>
				<div class="jqb_slide" ><img src="/images/user/m_visual_2.jpg" style="width:971px; height:268px;" alt="개성을 살리는 특수교육, E-LEARNING , 국립특수교육원이 함께합니다. 국립특수교육원은 교직원 연수 및 학생들에게 바른 인성을 갖춘 창의적인 인간육성과 미래지향적인 학생상 정립을 위해 다양한 연수 활동을 전개하고 있습니다." /></div>
			</div>
		
			
			<div class="jqb_bar"> 
				<div class="jqb_info"></div>	
				<div id="btn_next" class="jqb_btn jqb_btn_next"></div>
				<div id="btn_pauseplay" class="jqb_btn jqb_btn_pause"></div>
				<div id="btn_prev" class="jqb_btn jqb_btn_prev"></div>
			</div>

		</div>
    
    </div>
    
	<!-- content01 -->
	<div id="content01">
    
    		<!-- 로그인 --> 
            <c:if test="${empty sessionScope.userid}">
            <div class="login" style="float:left; margin-left:0px">
            <form id="frmLogin" method="post" action="/usr/lgn/portalUserLogin.do" onsubmit="javascript:return login();">
            	
            	<input type="hidden" name="p_d_type" id="p_d_type"	value="T"/>
            	
            
            		 <ul>
                			<li><input  style="margin-left:0px;" type="text" id="userId" name="userId" value="" size="13" onkeypress="fn_keyEvent('login')" title="사용자 아이디"/><label for="userId" class="blind">사용자 아이디</label></li>
                            <li style="align:left;"><input type="password" id="pwd" name="pwd" value="" size="13" onkeypress="fn_keyEvent('login')" title="사용자 비밀번호"/><label for="pwd" class="blind">사용자 비밀번호</label></li>
                            <li class="m_login"><a href="#" onclick="login();"><img src="/images/user/m_login.gif" alt="로그인" /></a></li>
                            
                            
                            <li class="join"><a href="#" onclick="changeMenu(7, 1)"><img src="/images/user/m_join.gif" alt="회원가입" /></a> 
                            <span style="padding-left:10px;"><a href="#" onclick="changeMenu(7, 2)"><img src="/images/user/m_idpw.gif" alt="아이디/비밀번호찾기" /></a></span></li>
                            <li><a href="#" onclick="changeMenu(1, 6)"><img src="/images/user/m_guide_img_new.gif" alt="모바일연수안내, 모바일앱 다운로드" /></a></li>
                            <li><a href="#" onclick="changeMenu(1, 2)"><img src="/images/user/m_guide_img01.gif" alt="처음오셨나요?,수강신청안내입니다." /></a></li>                            
               		 </ul>
            </form>
            </div>
            </c:if>
            <!-- //로그인 -->
            
            <!-- 로그인아웃 --> 
            <c:if test="${not empty sessionScope.userid}">
            <div class="login ct" style="float:left; margin-left:0px">
            <form id="frmLogin" method="post" action="" onsubmit="javascript:return false;">
            	<input type="hidden" name="p_d_type" id="p_d_type"	value="O"/>
            	<fieldset>
                <legend>로그인</legend>
                	 <ul>
                			<li><strong><c:out value="${sessionScope.name}"/></strong> 환영합니다.</li>
                            <li>                            	
                                <label for="p_auth" class="blind">회원유형</label>
                                <select id="p_auth" name="p_auth" onchange="authChange()" title="회원유형">
								<c:forEach items="${authList}" var="result">
									<c:if test="${result.gadmin == p_auth}"><c:set var="selected" value="selected"/></c:if>
									<option value="${result.gadmin}" <c:if test="${result.gadmin == p_auth}">selected</c:if>><c:out value="${result.gadminnm}"/></option>
								</c:forEach>
									<option value="ZZ" <c:if test="${empty selected}">selected</c:if>>학습자</option>
								</select>
                                
                                <c:if test="${not empty p_auth and p_auth ne 'ZZ' and p_auth ne ''}">
                                	<a href="#" onclick="adminOpenPage()"><img src="/images/user/btn_go.gif" alt="GO"/></a>
								</c:if>
                            </li>
                            <li class="join"><a href="#" onclick="changeMenu(6, 3)"><img src="/images/user/m_edit.gif" alt="정보수정" /></a> 
                            <span style="padding-left:10px;"><a href="#" onclick="logout()"><img src="/images/user/m_logout.gif" alt="로그아웃" /></a></span></li>
                            <li><a href="#" onclick="changeMenu(1, 6)"><img src="/images/user/m_guide_img_new.gif" alt="모바일연수안내, 모바일앱 다운로드" /></a></li>
                            <li><a href="#" onclick="changeMenu(1, 2)"><img src="/images/user/m_guide_img01.gif" alt="처음오셨나요?,수강신청안내입니다." /></a></li>                            
               		 </ul>
            	</fieldset>
            </form>
            </div>
            </c:if>
            <!-- 로그인아웃 -->
            
            <!-- 연수과정안내 -->              		
            <div class="con1 mtab1 m1" style="float:left; width:490px; margin-left:20px"> 
            	<h4 style="margin-bottom:5px;"><img src="/images/user/m_tit01.gif" alt="연수과정안내"/></h4>
                <ul>                	
                    <li class="m1"><a href="#none" onclick="doSubjTabView(1)"><img src="/images/user/m_tab_menu01_on.gif" alt="전체"  id="tab_smimage1"/></a>
                        <ul  id="tab_sm1" class="list" style="display:block">
                        <c:forEach items="${trainingList}" var="result" varStatus="status">
                        	<c:if test="${status.count < 6}">
	                            <li>
	                            
	                            <a href="#" onclick="doSubjView('${result.subj}', '${result.subjseq}')">
	                            <c:if test="${result.cls eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
								<c:if test="${result.cls eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
								<c:if test="${result.cls eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="일반연수"/></c:if>
								<c:if test="${result.cls eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
								<c:if test="${result.cls eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모"/></c:if>
	                            ${result.gisu}기 &gt; 
	                            <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
	                            <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
	                            <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
	                              &gt;	                              
	                             <c:set var="subjnm" value="${result.subjnm}" />
	                             <c:if test="${fn:length(result.subjnm)>30 }">
	                             	<c:set var="subjnm" value="${fn:substring(subjnm, 0, 30)}" />
	                             	<c:out value="${subjnm}"/>...
	                             </c:if>
								<c:if test="${fn:length(result.subjnm)<=30 }">	                             	
	                             	<c:out value="${subjnm}"/>
	                             </c:if>							
	                             <%--  <c:out value="${result.subjnm}"/> --%>
	                              
	                              </a>
	                            <span class="date">
	                            <c:choose>
	                            	<c:when test="${result.gubun eq '0'}"><img src="/images/user/ico_applying.gif" alt="신청중"   /></c:when>
	                            	<c:when test="${result.gubun eq '1'}"><img src="/images/user/ico_prev.gif" alt="신청전"  /></c:when>
	                            	<c:when test="${result.gubun eq '2'}"><img src="/images/user/m_course_end.gif" alt="마감"   /></c:when>
	                            </c:choose>
	                            </span>
	                            </li>
                        	</c:if>    
                        </c:forEach>
                        
                            <li class="more"><a href="#none" onclick="doSubjClsView('')"><img src="/images/user/m_tab_plus.gif" alt="전체과정 더보기" /></a></li>
                        
                        
                        </ul>
                    </li>
                    <li class="m2"><a href="#none" onclick="doSubjTabView(2)"><img src="/images/user/m_tab_menu02_off.gif" alt="교원직무연수"  id="tab_smimage2"/></a>
                        <ul id="tab_sm2" class="list" style="display:none">
                        <c:set var="i">0</c:set>
                        <c:forEach items="${trainingList}" var="result" varStatus="status">
                        	<c:if test="${i < 5 && result.cls eq 'PRF'}">
	                            <li>
	                            <a href="#" onclick="doSubjView('${result.subj}', '${result.subjseq}')">
	                            <c:if test="${result.cls eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
								<c:if test="${result.cls eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
								<c:if test="${result.cls eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="일반연수"/></c:if>
								<c:if test="${result.cls eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
								${result.gisu}기   &gt; 
	                            <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
	                            <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
	                            <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
	                             &gt;	                              
	                             <c:set var="subjnm" value="${result.subjnm}" />
	                             <c:if test="${fn:length(result.subjnm)>30 }">
	                             	<c:set var="subjnm" value="${fn:substring(subjnm, 0, 30)}" />
	                             	<c:out value="${subjnm}"/>... 
	                             </c:if>
								<c:if test="${fn:length(result.subjnm)<=30 }">	                             	
	                             	<c:out value="${subjnm}"/> 
	                             </c:if>							
	                             <%--  <c:out value="${result.subjnm}"/> --%>
	                            
	                            </a>
	                            <span class="date">
	                            <c:choose>
	                            	<c:when test="${result.gubun eq '0'}"><img src="/images/user/ico_applying.gif" alt="신청중"  /></c:when>
	                            	<c:when test="${result.gubun eq '1'}"><img src="/images/user/ico_prev.gif" alt="신청전"   /></c:when>
	                            	<c:when test="${result.gubun eq '2'}"><img src="/images/user/m_course_end.gif" alt="마감"  /></c:when>
	                            </c:choose>	       
	                            </span>                     
	                            </li>
								<c:set var="i">${i+1}</c:set>                        	
                        	</c:if>    
                        </c:forEach>
                            <li class="more"><a href="#none" onclick="doSubjClsView('PRF')"><img src="/images/user/m_tab_plus.gif" alt="교원직무연수과정 더보기" /></a></li>
                        </ul>
                    </li>
                    
                    <li class="m3"><a href="#none" onclick="doSubjTabView(3)"><img src="/images/user/m_tab_menu03_off.gif" alt="보조인력"  id="tab_smimage3"/></a>
                        <ul id="tab_sm3" class="list" style="display:none">
                        <c:set var="i">0</c:set>
                        <c:forEach items="${trainingList}" var="result" varStatus="status">
                        	<c:if test="${i < 5 && result.cls eq 'EXT'}">
	                            <li>
	                            <a href="#" onclick="doSubjView('${result.subj}', '${result.subjseq}')">
	                            <c:if test="${result.cls eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
								<c:if test="${result.cls eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
								<c:if test="${result.cls eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="일반연수"/></c:if>
								<c:if test="${result.cls eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
								${result.gisu}기   &gt; 
	                            <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
	                            <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
	                            <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
	                             &gt;	                              
	                             <c:set var="subjnm" value="${result.subjnm}" />
	                             <c:if test="${fn:length(result.subjnm)>30 }">
	                             	<c:set var="subjnm" value="${fn:substring(subjnm, 0, 30)}" />
	                             	<c:out value="${subjnm}"/>...
	                             </c:if>
								<c:if test="${fn:length(result.subjnm)<=30 }">	                             	
	                             	<c:out value="${subjnm}"/>
	                             </c:if>							
	                             <%--  <c:out value="${result.subjnm}"/> --%>
	                             
	                             </a>
	                             
	                            <span class="date">
	                            <c:choose>
	                            	<c:when test="${result.gubun eq '0'}"><img src="/images/user/ico_applying.gif" alt="신청중"  /></c:when>
	                            	<c:when test="${result.gubun eq '1'}"><img src="/images/user/ico_prev.gif" alt="신청전"  /></c:when>
	                            	<c:when test="${result.gubun eq '2'}"><img src="/images/user/m_course_end.gif" alt="마감"  /></c:when>
	                            </c:choose>
	                            </span>
	                            </li>
	                            <c:set var="i">${i+1}</c:set>
                        	</c:if>    
                        </c:forEach>
                            <li class="more"><a href="#none" onclick="doSubjClsView('EXT')"><img src="/images/user/m_tab_plus.gif" alt="보조인력과정 더보기" /></a></li>
                        </ul>
                    </li>
                    
                    <li class="m4"><a href="#none" onclick="doSubjTabView(4)"><img src="/images/user/m_tab_menu04_off.gif" alt="학부모"  id="tab_smimage4"/></a>
                        <ul id="tab_sm4" class="list" style="display:none">
                        <c:set var="i">0</c:set>
                        <c:forEach items="${trainingList}" var="result" varStatus="status">
                        	<c:if test="${i < 5 && result.cls eq 'PAR'}">
	                            <li>
	                            <a href="#" onclick="doSubjView('${result.subj}', '${result.subjseq}')">
	                            <c:if test="${result.cls eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
								<c:if test="${result.cls eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
								<c:if test="${result.cls eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="일반연수"/></c:if>
								<c:if test="${result.cls eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
								${result.gisu}기   &gt; 
	                            <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
	                            <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
	                            <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
	                             &gt;	                              
	                             <c:set var="subjnm" value="${result.subjnm}" />
	                             <c:if test="${fn:length(result.subjnm)>30 }">
	                             	<c:set var="subjnm" value="${fn:substring(subjnm, 0, 30)}" />
	                             	<c:out value="${subjnm}"/>...
	                             </c:if>
								<c:if test="${fn:length(result.subjnm)<=30 }">	                             	
	                             	<c:out value="${subjnm}"/>
	                             </c:if>							
	                             <%--  <c:out value="${result.subjnm}"/> --%>
	                             
	                             </a>
	                             	
	                            <span class="date">
	                            <c:choose>
	                            	<c:when test="${result.gubun eq '0'}"><img src="/images/user/ico_applying.gif" alt="신청중"  /></c:when>
	                            	<c:when test="${result.gubun eq '1'}"><img src="/images/user/ico_prev.gif" alt="신청전"  /></c:when>
	                            	<c:when test="${result.gubun eq '2'}"><img src="/images/user/m_course_end.gif" alt="마감"  /></c:when>
	                            </c:choose>
	                            </span>
	                            </li>
	                            <c:set var="i">${i+1}</c:set>
                        	</c:if>    
                        </c:forEach>
                            <li class="more"><a href="#none" onclick="doSubjClsView('PAR')"><img src="/images/user/m_tab_plus.gif" alt="학부모과정 더보기" /></a></li>
                        </ul>
                    </li>
                    
                    <!--<li class="m5"><a href="#none" onclick="doSubjTabView(5)"><img src="/images/user/m_tab_menu05_off.gif" alt="무료"  id="tab_smimage5"/></a>
                        <ul id="tab_sm5" class="list" style="display:none">
                        <c:set var="i">0</c:set>
                        <c:forEach items="${trainingList}" var="result" varStatus="status">
                        	<c:if test="${i < 6 && result.cls eq 'OTH'}">
	                            <li>
	                            <a href="#" onclick="doSubjView('${result.subj}')">${result.year}년 ${result.gisu}기  ${result.tnm} &gt; <c:out value="${result.subjnm}"/></a>
	                            <span class="date">
	                            <c:choose>
	                            	<c:when test="${result.gubun eq '0'}"><img src="/images/user/ico_applying.gif" alt="신청중" /></c:when>
	                            	<c:when test="${result.gubun eq '1'}"><img src="/images/user/ico_prev.gif" alt="신청전" /></c:when>
	                            	<c:when test="${result.gubun eq '2'}"><img src="/images/user/m_course_end.gif" alt="마감" /></c:when>
	                            </c:choose>
	                            </span>
	                            </li>
	                            <c:set var="i">${i+1}</c:set>
                        	</c:if>    
                        </c:forEach>
                            <li class="more"><a href="#none" onclick="doSubjClsView('OTH')"><img src="/images/user/m_tab_plus.gif" alt="무료과정 더보기" /></a></li>
                        </ul>
                    </li>
                    
                --></ul>
            </div>           
            <!-- //연수과정안내 -->
            
            <!-- 바로가기메뉴 -->
    		<div class="icon" style="float:left; margin-left:30px">
            		<ul class="mrb15">
                        <li><a href="#" onclick="changeMenu(2, 1)"><img src="/images/user/m_go01.gif" alt="수강신청"/></a></li>
                        <li><a href="#" onclick="changeMenu(3, 1)"><img src="/images/user/m_go02.gif" alt="수강중인과정"/></a></li>
                        <li><a href="#" onclick="changeMenu(4, 6)"><img src="/images/user/m_go03.gif" alt="연수관련상담"/></a></li>
                    </ul>
                    <ul>
                        <li><a href="#" onclick="changeMenu(5, 3)"><img src="/images/user/m_go04.gif" alt="이수증발급"/></a></li>
                        <li><a href="#" onclick="changeMenu(5, 1)"><img src="/images/user/m_go05.gif" alt="연수행정실"/></a></li>
                        <li><a href="#" onclick="window.open(' https://988.co.kr/ek')" title="새창"><img src="/images/user/m_go06_new.gif" alt="원격지원서비스"/></a></li>
                    </ul>
            </div>
            <!-- //바로가기메뉴 -->
            		
	</div>
	<!-- // content01 -->
    <!-- content02 -->
	<div id="content02">
			<div id="adcontentAreaTemp" style="display:none;">
			<c:forEach items="${view}" var="result" varStatus="status">
				<span><c:out value="${result.adcontent }"  escapeXml="false" /></span>
			</c:forEach>
			</div>
    		<!-- 공지사항 -->
    		<div class="notice m1" style="float:left; width:570px;">
                <ul>
                    <li class="m1"><a href="#"><img src="/images/user/m_notice_tit.gif" alt="공지사항" /></a>
                        <ul  id="sm1" class="list">
                        <c:forEach items="${topNoticeList}" var="result" varStatus="status">
                            <li>
                            <a href="#none" onclick="doNoticeView('${result.seq}')">
	                            <c:if test="${result.noticeGubun eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
								<c:if test="${result.noticeGubun eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
								<c:if test="${result.noticeGubun eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
								<c:if test="${result.noticeGubun eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
								<c:if test="${result.noticeGubun eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
								<c:if test="${result.noticeGubun eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
								<c:if test="${result.noticeGubun eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
								<c:if test="${fn:length(result.adtitle) > 50}">
									<c:out value="${fn:substring(result.adtitle, 0, 50)}"></c:out>...
								</c:if>
								<c:if test="${fn:length(result.adtitle) <= 50}">
                            		<c:out value="${result.adtitle}"></c:out>
                            	</c:if>
                            </a>
                            <span class="date"><c:out value="${fn2:getFormatDate(result.addate, 'yyyy.MM.dd')}"/></span>
                            </li>
                        </c:forEach>
	                        <c:if test="${empty topNoticeList}">
	                        	<li>등록된 공지사항이 없습니다 <span class="date"><c:out value="${fn2:getFormatDateNow('yyyy.MM.dd')}"/></span></li>
	                        </c:if>
                            <li class="more"><a href="#"  onclick="changeMenu(4, 1)"><img src="/images/user/btn_more.gif" alt="공지사항 더보기" /></a></li>
                        </ul>
                    </li>                   
                </ul>
            </div>  
            <!-- //공지사항 --> 
            
            
            <!-- 연수지명번호 -->
            <div class="down_img">
            		<ul>
                    		<li class="mrb10"><img src="/images/user/m_tit03.gif" alt="연수지명번호 입력방법 안내" /></li>
                            <li><a href="#"  onclick="fn_download('연수지명번호입력방법.hwp', 'yensu_number.hwp', 'bulletin')"><img src="/images/user/m_download.gif" alt="연수지명번호 입력방법 안내 : 학교에서 부여하는 것으로 아래의 파일을 참조하여 수강신청 하세요., 특수교육보조인력과정 및 학부모 과정은 연수지명번호가 필요없습니다., 클릭시 다운로드" /></a></li>                                                       
                            <li><a href="#"  onclick="fn_download('나이스개인번호안내.pdf', 'nice_number.pdf', 'bulletin')"><img src="/images/user/nice_num_info.gif" alt="나이스개인번호안내., 클릭시 다운로드" /></a></li>                                                       
                    </ul>
            </div>
            <!-- //연수지명번호 -->  
            
            <!-- 문의안내 -->
            <div class="phone_img">
            		<ul>
                    		<li class="mrb10"><img src="/images/user/m_tit04_1.gif" alt="고객센터 평일:09:30~17:50 점심:11:50~13:00 원격연수운영팀 041)537-1475~9 기술문의/원격지원 02-6345-6787" /></li>
                    		<!-- <li class="mrb10"><img src="/images/user/m_tit04_1.gif" alt="연수문의/안내, 원격연수운영팀, 연수문의: 041)537-1475~8 평일:09시30분~17시50분, 팩스 : 041)537-1479 , 이메일:jmx386@moe.go.kr, 원격서비스 지원센터, 원격지원 : 070)7434-2581 평일:09시30분~17시50분, 이메일: nurisys@nurisys.co.kr" /></li> -->
<!--                            <li><img src="/images/user/m_phon.gif" alt="평일:09시30분~17시50분, 팩스 : 0415371479 , 이메일:seonggoo@moe.go.kr , 연수문의: 041)537-1475~8 , 원격문의: 070)7434-2581" /></li>                                                        -->
                    </ul>          
	<!-- // content02 -->
<script type="text/javascript">
//<![CDATA[
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

function popup_show(){
	//기존팝업	
	//<c:forEach items="${popupList}" var="result" varStatus="status">
		
	//if ( notice_getCookie( 'popUpPreview${result.seq}' ) != "done" ) {
			//window.open('/usr/hom/noticePopup.do?p_seq=${result.seq}', "popUpPreview${result.seq}", 'left=${result.popxpos}, top=${result.popypos},width=${result.popwidth},height=${result.popheight}');
	//}	
	//</c:forEach>
	//레이어팝업 
	var leftArea=20;
	var topArea;
	var count=0;
	<c:forEach items="${popupListAttr}" var="result" varStatus="status">
	if ( notice_getCookie( 'popUpPreview${result.seq}' ) != "done" ) {
		count++;
	}
	</c:forEach>
	if(count>0){
		//if(confirm('공지 알림이 있습니다. 알림 내용을 보시겠습니까')){
		<c:forEach items="${popupListAttr}" var="result" varStatus="status">
			var totalCount = "${status.count}";
			totalCount= totalCount -1;
			var popupArea=$('#popupArea');
			if('${result.temType}' == 'A'){
				var HTMLDIV="<div id='pop_type_"+leftArea+"' style='left:"+leftArea+"px;' class='pop_layer'>";
				HTMLDIV+="<div class='pop_content'><c:out value='' escapeXml='false'></c:out></div>";
				HTMLDIV+="<div class='close'><label><a herf='' class='close' onclick='javascript:closeFun(this)'>닫기(ESC)</a> | <a herf='' class='close2' onclick='javascript:closeFunOneDay(this,${result.seq})'>오늘 하루 창 닫기 </a></label></div>";
				HTMLDIV+="</div>";
				popupArea.append(HTMLDIV);
				var popCss=popupArea.find("#pop_type_"+leftArea);
				popCss.attr('class','pop_layer');
				popCss.css('background','transparent url(/images/adm/common/notice1_bg.jpg) no-repeat');
				popCss.css('width','473px');
				popCss.css('height','387px');
				popCss.css('border','1px solid #444');
				popCss.css('font-size','12px');
		
				popCss.find('.pop_content').css('overflow-y','auto');
				popCss.find('.pop_content').css('overflow-x','hidden');
				popCss.find('.pop_content').css('padding','110px 90px 0 100px');
				popCss.find('.pop_content').css('height','220px');		
				
				popCss.find('.close').css('padding','20px 40px 0 90px');
				popCss.find('.close').css('text-align','right');
				popCss.find('.close').css('color','#fff');
				
				popCss.find('.close2').css('display','inline-block');
				popCss.find('.close2').css('padding','3px 7px');
				popCss.find('.close2').css('margin','0 10px');
				popCss.find('.close2').css('background','#262626');
				popCss.find('.close2').css('color','#fff');
				popCss.find('.close2').css('border-radius','.3em');	
				
				popCss.find('.pop_content').append($('#adcontentAreaTemp').find('span:first'));
				if ( notice_getCookie( 'popUpPreview${result.seq}' ) != "done" ) {
					popupArea.find('#pop_type_'+leftArea).delay(1*500).fadeIn("slow");
					leftArea= leftArea +200;
					popCss.draggable();
				}
			}
			if('${result.temType}' == 'C'){
				var HTMLDIV="<div id='pop_type_"+leftArea+"' style='left:"+leftArea+"px;' class='pop_layer'>";
				HTMLDIV+="<div class='pop_content'><c:out value='' escapeXml='false'></c:out></div>";
				HTMLDIV+="<div class='close'><label><a herf='' class='close' onclick='javascript:closeFun(this)'>닫기(ESC)</a> | <a herf='' class='close2' onclick='javascript:closeFunOneDay(this,${result.seq})'>오늘 하루 창 닫기 </a></label></div></div>";
				popupArea.append(HTMLDIV);
				var popCss=popupArea.find('#pop_type_'+leftArea);
				popCss.attr('class','pop_layer');
				popCss.css('background','transparent url(/images/adm/common/notice3_bg.jpg) no-repeat');
				popCss.css('width','560px');
				popCss.css('height','696px');
				popCss.css('border','1px solid #444');
				popCss.css('font-size','12px');
				popCss.find('.pop_content').css('overflow-y','auto');
				popCss.find('.pop_content').css('overflow-x','hidden');
				popCss.find('.pop_content').css('padding','120px 135px 15px 115px');
				popCss.find('.pop_content').css('height','520px');
				popCss.find('.close').css('padding','10px 40px 0 90px');
				popCss.find('.close').css('text-align','right');
				popCss.find('.close').css('color','#fff');
				popCss.find('.close2').css('display','inline-block');
				popCss.find('.close2').css('padding','3px 7px');
				popCss.find('.close2').css('margin','0 10px');
				popCss.find('.close2').css('background','#262626');
				popCss.find('.close2').css('color','#fff');
				popCss.find('.close2').css('border-radius','.3em');
				
				popCss.find('.pop_content').append($('#adcontentAreaTemp').find('span:first'));
				if ( notice_getCookie( 'popUpPreview${result.seq}' ) != "done" ) {
					popupArea.find('#pop_type_'+leftArea).delay(1*500).fadeIn("slow");
					leftArea= leftArea +200;
					popCss.draggable();
				}
			}
			if('${result.temType}' == 'B' || '${result.temType}' ==""){
				
				var HTMLDIV="<div id='pop_type_"+leftArea+"' style='left:"+leftArea+"px;' class='pop_layer'>";
				HTMLDIV+="<div class='pop_top'><img src='/images/adm/common/notice2_top.gif' alt='공지팝업'/></div>";
				HTMLDIV+="<div class='pop_content'></div>";
				HTMLDIV+="<div class='pop_bottom'><label><a herf=''class='close' onclick='javascript:closeFun(this)'>닫기(ESC)</a> | <a herf='' class='close2' onclick='javascript:closeFunOneDay(this,${result.seq})'>오늘 하루 창 닫기 </a></label></div>";
				HTMLDIV+="</div>";
				popupArea.append(HTMLDIV);
				var popCss=popupArea.find('#pop_type_'+leftArea);
				
				popCss.css('overflow','hidden');
				popCss.css('background','transparent url(/images/adm/common/notice2_bg.gif) no-repeat');
				popCss.css('width','600px');
				popCss.css('height','500px');
				popCss.css('border','1px solid #444');
				popCss.css('font-size','12px');
				
				popCss.find('.pop_top').css('height','131px');
				popCss.find('.pop_top').css('text-align','center');
				popCss.find('.pop_bottom').css('color','#fff');
				popCss.find('.pop_bottom').css('padding','10px 40px 0 90px');
				popCss.find('.pop_bottom').css('text-align','right');
				popCss.find('.pop_bottom').css('background','transparent url(/images/adm/common/notice2_bottom.gif) no-repeat');
				popCss.find('.pop_bottom').css('height','80px');
				popCss.find('.pop_content').css('overflow-y','auto');
				popCss.find('.pop_content').css('overflow-x','hidden');
				popCss.find('.pop_content').css('background','transparent url(/images/adm/common/notice2_center.gif) repeat-y');
				popCss.find('.pop_content').css('height','290px');
				popCss.find('.pop_content').css('padding','0px 50px 10px 50px');
				popCss.find('.pop_content').css('color','#fff');
				
				popCss.find('.close').css('padding','10px 40px 0 90px');
				popCss.find('.close').css('text-align','right');
				popCss.find('.close').css('color','#fff');
				popCss.find('.close2').css('display','inline-block');
				popCss.find('.close2').css('padding','3px 7px');
				popCss.find('.close2').css('margin','0 10px');
				popCss.find('.close2').css('background','#262626');
				popCss.find('.close2').css('color','#fff');
				popCss.find('.close2').css('border-radius','.3em');	
				
				popCss.find('.pop_content').append($('#adcontentAreaTemp').find('span:first'));
				if ( notice_getCookie( 'popUpPreview${result.seq}' ) != "done" ) {
					popupArea.find('#pop_type_'+leftArea).delay(1*500).fadeIn("slow");
					leftArea= leftArea +200;
					popCss.draggable();
				}
			}
				
		</c:forEach>
		//}
	}	
	$(document).keydown(function(event) {
	  if (event.which == 27) {
		 $("#popupArea").hide();
	   }
	});
}
//alert($('#adcontentAreaTemp').html());

function closeFun(target){
	$(target).parent().parent().parent().fadeOut("fast")
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
//]]>
</script>
<script type="text/javascript">
$(document).ready(function(){
	//layer_open('layer1');
	
	//var layer = document.getElementById("xpop"); 
	//layer.style.visibility="visible"; //반대는 hidden
	//<c:if test="${fn:length(popupListAttr) > 0}">
	//$("#xpop").slideDown('slow', function(){
	//	<c:forEach items="${popupListAttr}" var="result" varStatus="status">
	//	document.getElementById("pop_content${status.index+1}").innerHTML = "<a href='#' onclick=doNoticeView('${result.seq}')>${result.adtitle}</a>";
	//	</c:forEach> 
	//}); 
	//</c:if>
	
	<c:if test="${fn:length(popupListAttr) > 0}">
	if ( notice_getCookie( 'popUpPreview' ) != "done" ) {
		//$("#xpop").animate({
		//    "margin-top": 0,
		//});
		//$("#xpop").slideDown('slow', function(){
		//});
		$("#xpop").show();
	}
	</c:if>
	
	$("#xpop .close_bt").click(function(e){
	  e.preventDefault();
	  //$("#xpop").animate({
	    //"margin-top": -204,
	  //});      
	  $("#xpop").hide();
	})

	//$("#close").click(function(){
	//	$("#popup_layer").hide();
	//});

	//$("#popup_layer").click(function(){
	//	$("#popup_layer").slideUp('slow');
		//popup_show();
	//});
		
});

function layer_open(el){
	var tmp = $.cookie('popup_'+el);
	if(tmp != "off"){
		$.cookie('popup_'+el, 'on', {path:'/usr/hom/', expires:1});
	}

	$("p.pop_close").css({"cursor":"pointer"});
	$("p.today01").css({"cursor":"pointer"});

	var temp = $('#'+el);
	var bh=temp.prev().hasClass('bg');

	if(bg){

		$("div.layer:not(:animated)").slideToggle("slow");
	}else{
		temp.css("display","none");
		temp.slideDown("slow");
	}

	temp.find('.pop_close img').click(function(e){
		if(bg){
			$("div.layer:not(:animated)").slideToggle("slow");
		}else{
			temp.slideToggle("slow");
		}
		e.preventDefault();
	});

	$('.layer .bg').click(function(e){
		$("div.layer:not(:animated)").slideToggle("slow");
		e.preventDefault();
	});

	$('.today01 input:checkbox').click(function(e){
		var thisid2 = $(this).attr('id');
		thisid3 = thisid2.substring(6,12);
		$('#'+thisid3).slideToggle("slow");
		e.preventDefault();
		$.cookie(thisid2, 'off', {path:'/usr/hom/', expires: 1});
	});
}
</script>


<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonMainBottom.jsp" %>
<!-- // button -->