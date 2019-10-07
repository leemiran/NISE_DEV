<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- <%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>


<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<title>아이디 중복확인</title>
	<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/adm/popup.css" />
	
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/cresys_lib.js"></script>
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/base_new.js"></script>
	
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/map_util.js"></script>
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/jquery-1-9.1.js"></script>
	
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-ui-1.10.3.custom.min.js"></script>
<!--	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/calendar/datePicker.js"></script>-->
</head>
<body>
<c:set var="gsPopForm" value="gsPopForm"/>
<script language="javascript" type="text/javascript">
//<![CDATA[
	function logOut(){
		location.href="/adm/hom/actionLogout.do";
	}
	
	function fn_admin_pageMove(menu, submenu, pgm){
		var frm = eval('document.<c:out value="${gsMenuForm}"/>');

		//테스트용 수정필요!!
		if( pgm.endsWith(".do") ){
			frm.action = pgm;
		}else{
			frm.action = "/adm/com/main/admActionMainPage.do";
		}
		frm.s_menu.value = menu;
		frm.s_submenu.value = submenu;
		frm.target = "_self";
		frm.submit();
	}

	function fn_keyEvent(fun){
		if( event.keyCode == 13 ){
			eval("this."+fun+"()");
		}
	}

	function fn_checkCount(frm){
		var target = frm.p_key1;
		var cnt = 0;
		if( target ){
			if(target.length){
				for( i=0; i<target.length; i++ ){
					if( target[i].checked ) cnt++;
				}
			}else{
				if( target.checked ) cnt++;
			}
		}
		if( cnt == 0 ){
			alert("대상을 선택하세요.");
			return false;
		}
		return true;
	}
	String.prototype.endsWith = function(str){
		return (str == this.substring(this.length-3, this.length));
	};

	//성적보기
	function fn_statusPopup(subj, year, subjseq){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		window.open('', 'eduListPop', 'width=813,height=1000,scrollbars=yes');
		frm.action = "/com/pop/courseScoreListPopup.do";
		frm.target = "eduListPop";
		frm.submit();
	}
//]]>
</script>




<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>I D중복확인</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<!-- contents -->
		<div class="tbDetail" style="padding-left:0px;">
						<c:if test="${empty isOk}">
							<div class="popCon">
								<font color="red">${p_userid}</font>는(은) 이미 등록된 아이디입니다.
								<div class="popTb"><label for="p_userid">ID</label> : <input type="text" id="p_userid" name="p_userid" class="ipt" style="IME-MODE:disabled" size="30" onfocus="this.select()" onkeypress="fn_keyEvent('existID')"/><c:out value="${isOk}"/>
								</div>
							</div>
						</c:if>
						<c:if test="${not empty isOk}">
							<div class="popCon" align="center">
								<font color="red">${p_userid}</font>는(은)<br/> 사용할 수 있는 아이디입니다.
								<input type="hidden" id="p_userid" name="p_userid" value="${p_userid}"/>
							</div>
						</c:if>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
		<c:if test="${empty isOk}">
			<li><a href="#" class="pop_btn01" onclick="existID()"><span>중복확인</span></a></li>
		</c:if>
		<c:if test="${not empty isOk}">
			<li><a href="#" class="pop_btn01" onclick="useId()"><span>사용하기</span></a></li>
		</c:if>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>

<script type="text/javascript">
//<![CDATA[
	function existID(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.p_userid.value == "" ){
			alert("아이디를 입력하세요.");
			frm.p_userid.focus();
			return;
		}
		frm.action = "/com/pop/existIdPopup.do";
		frm.target = "_self";
		frm.submit();
	}

	function useId(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		opener.document.getElementById("p_userid").value = frm.p_userid.value;
		opener.document.getElementById("p_userid_temp").value = frm.p_userid.value;
		window.close();
	}
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>