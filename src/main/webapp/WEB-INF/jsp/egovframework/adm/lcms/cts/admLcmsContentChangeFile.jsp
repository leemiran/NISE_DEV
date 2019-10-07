<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%//@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="egovframework.com.cmm.service.Globals"%><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/common/admCommonHead.jsp" %>
<link href="/css/pop.css" rel="stylesheet" type="text/css" />
<base target="_self">
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.CtlExecutor.doPageReload();
	window.close();
</c:if>
-->
</script>
<style>
body {overflow-x:hidden; overflow-y:auto}
</style>
</head> 

<body>
<div id="wrap">
	<!-- TOP 시작-->
    <div id="title2">파일치환</div>
	<!-- TOP 끝-->
	<div id="contents">
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" id="path" name="path" value="${path}"/>
		<input type="hidden" id="changeFile" value="${changeFile}"/>
      
		<table cellpadding="0" cellspacing="0" class="tbl02" summary="기본정보" id="contentsTb" style="width:680px;">
			<caption>파일치환</caption>
			<colgroup>
				<col width="120" />
				<col width="580" />
			</colgroup>
			<tr>
				<th class="thl_th">현재경로</th>
				<td class="thl_td">
					<%=Globals.CONTENT_PATH%><c:out value="${path}" />
				</td>
			</tr>	
			<tr>
				<th class="thl_th">파일명</th>
				<td class="thl_td"><c:out value="${changeFile}"/></td>
			</tr>
			<tr>
				<th class="thl_th">파일선택</th>
				<td class="thl_td">
				</td>
			</tr>
			<tr>
				<th class="thl_th">변경사유</th>
				<td class="thl_td"><c:out value="${fileName}"/></td>
			</tr>
		</table>
        <table width="680px;" cellpadding="0" cellspacing="0" border="0">
        	<tr>
        		<td width="95%">&nbsp;</td>
        		<td>
					<div class="btn_02"><a href="#none" onclick="CtlExecutor.doCreateFolder();">폴더추가</a></div>
        		</td>
        	</tr>
        </table>
      </form>
    </div>
    <div id="footer" style="width:700px;"><a href="#" onclick="window.close();"><img src="/images/pop/btn_close.gif" alt="닫기" width="46" height="11" /></a></div>
</div>
   
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
	var CtlExecutor = {

        /* ********************************************************
        * 교과 등록/수정
        ******************************************************** */
        doCreateFolder : function(){
			if( $("#folderName").val() == "" ){
				alert("폴더명을 입력하세요.");
				return;
			}
			$("#<c:out value="${gsPopForm}"/>").onSubmit(
                options = {
                    url         : "<c:out value="${gsDomainContext}"/>/adm/cts/createContentFolder.do",
                    method      : "pageList",
                    validation  : false
                }
            );
		}


    };
</script>
</body>
</html>
