<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
	<title>국립특수교육원부설 원격교육연수원</title>
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


