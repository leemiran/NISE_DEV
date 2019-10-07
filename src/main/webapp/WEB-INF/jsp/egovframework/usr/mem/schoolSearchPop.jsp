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
	<title>학교검색</title>
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




<!-- popup wrapper 팝업사이즈width=650,height=500-->


<div id="popwrapper">
	<div class="popIn" style="width:750px;height:650px">
		 <!-- tab -->
        <div class="tit_bg">
			<h2>학교검색</h2>
       		</div>
        
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" action="/usr/mem/searchSchoolPop.do">
		<fieldset>
        <legend>우편번호 검색</legend>
        <input type="hidden" name="action" id="action"/>
		<input type="hidden" name="p_gubun" id="p_gubun" value="${p_gubun}"/>
		<div class="in" style="width:100%; text-align: left; ">		
			<b>찾고자 하는 학교의 '전체 학교명'을 입력해 주십시오.</b><br/>
			<font color="red"><b>예)서울사대부고[X] - 서울대학교사범대학부설고등학교[O]</b></font><br/>
			<input type="text" name="p_school_nm" id="p_school_nm" value="${search_dong}" size="40" style="ime-mode:active" onkeyup="fn_keyEvent('searchList')"/>
			<a href="#" onclick="searchList()" class="btn_search"><span>검 색</span></a>
		</div>
        </fieldset>
		</form>		
		<p style="height:3px;"/>
		<!-- contents -->
		<div class="tbList">
			<table summary="상위교육청, 하위교육청, 학교명으로 구분" cellspacing="0" width="100%">
                <caption>
                학교검색
                </caption>
                <colgroup>
					<col width="10%" />
					<col width="30%" />
					<col width="20%" />
					<col width="20%" />
					<col width="20%" />
				</colgroup>
				<tr>
					<th scope="row">기관코드</th>
					<th scope="row">전체기관명</th>
					<th scope="row">상위교육청</th>
					<th scope="row">하위교육청</th>
					<th scope="row">학교명</th>
				</tr>
			</table>
			<div style="overflow-y:scroll; height:455px;">
				<table summary="상위교육청, 하위교육청, 학교명으로 구분" cellspacing="0" width="100%">
                <caption>학교검색</caption>
                <colgroup>
					<col width="10%" />
					<col width="30%" />
					<col width="20%" />
					<col width="20%" />
					<col width="20%" />
				</colgroup>
				<tbody>
				<c:forEach items="${list}" var="result">
				<tr style="cursor:pointer" onmouseover="overRow(this);" onclick="onSchInfoPut('${result.orgNm}', '${result.fstOrgNm}', '${result.sndOrgNm}', '${result.telNo}', '${result.fstOrgCd}', '${result.sndOrgCd}')">
					<td><c:out value="${result.orgCd}"/></td>
					<td><c:out value="${result.fullOrgNm}"/></td>
					<td><c:out value="${result.fstOrgNm}"/></td>
					<td><c:out value="${result.sndOrgNm}"/></td>
					<td><c:out value="${result.orgNm}"/></td>
				</tr>
				</c:forEach>	
                			
				<c:if test="${empty list}">
					<tr>
						<td colspan="2" width="100%">검색된 내용이 없습니다.</td>
					</tr>
				</c:if>			
					
				</tbody>
			</table>
			</div>
		</div>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫 기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<script type="text/javascript">
//<![CDATA[
	var frm = document.<c:out value="${gsPopForm}"/>;

	function go_searchTab(p_gubun)
	{
		frm.action.value = "";
		frm.search_dong.value = "";
		frm.p_gubun.value = p_gubun;
		//frm.action = "/com/pop/searchZipcodePopup.do"
		frm.target = "_self";
		frm.submit();
	}
	
	function searchList(){
		
		if (frm.p_school_nm.value == "") {
            alert("학교명을 입력해주세요");
            return;
        }
		frm.action.value = "go";
		frm.submit();
	}

	function onSchInfoPut(schNm, fdnDivCd, schDivCd, schTelNo, fstOrgCd, sndOrgCd) {   
	//var	frm = opener.document.forms[0];
	
	//frm.p_user_path.value = schNm;
	//frm.p_dept_cd.value = fdnDivCd;
	//frm.p_agency_cd.value = schDivCd;
	//frm.p_handphone_no1.value = schTelNo;
	//window.close();

	var arr = new Array();
	arr[0] = schNm;
	arr[1] = fdnDivCd;
	arr[2] = schDivCd;
	arr[3] = schTelNo;
	arr[4] = fstOrgCd;
	arr[5] = sndOrgCd;
	opener.receiveSchool(arr);
    self.close();
}

	var tmpRow;
	function overRow(obj){
		if( tmpRow ){
			tmpRow.style.backgroundColor = "#FFFFFF";
		}
		tmpRow = obj;
		obj.style.backgroundColor = "#DCDCDC";
	}
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>