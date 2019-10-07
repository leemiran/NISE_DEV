<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonHead.jsp" %>
</head> 


<body style="background: none;">
	<form name="orgForm" id="orgForm" method="post" style = "padding-top: 10px;">
		<input type="hidden" name="subj" id="subj" value="${subj}"/>
		<input type="hidden" id="pageIndex" name="pageIndex" value=""/>
	<br/>
	<!-- list table-->
	<c:if test="${empty list}">
			<table summary="차시리스트" id="orgTable" width="100%">
				<tr>
					<td align="center" colspan="2">
						<b>등록된 내용이 없습니다. 콘텐츠를 업로드하세요.</b>
					</td>
				</tr>
			</table>
	</c:if>
	<c:if test="${not empty list}">
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="parent.doProgressLog()"><span>학습로그</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="parent.doPreview('','')"><span>미리보기</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="parent.doRemoveOrg()"><span>삭제</span></a></div>
	</div>
	
	<!-- detail wrap -->
	<div class="tbDetail" style="padding-left:0px;">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
			<col width="15%" />
			<col width="35%" />
			<col width="15%" />
			<col width="35%" />                    
			</colgroup>
			<tbody>
				<tr class="title">
					<th>등록자</th>
					<td class="borderR"><c:out value="${data.name}"/></td>
					<th>등록일</th>
					<td><c:out value="${data.indate}"/></td>                       
				</tr>
			</tbody>
		</table>
	</div>
		<input type="checkbox" name="allCheck" id="allCheck" onclick="allChecked()">전체선택
<c:set var="oldModule" value=""></c:set>
		<table summary="차시리스트" id="orgTable">
			<caption>차시리스트</caption>
			<colgroup>
				<col width="700px" />
			</colgroup>
			<thead>
<c:forEach items="${list}" var="result">
	<c:if test="${result.module != oldModule && oldModule != ''}">
						</div>
					</td>
				</tr>
	</c:if>
	<c:if test="${result.module != oldModule}">
				<tr>
					<td class="subject">
		<c:set var="oldModule" value="${result.module}"/>
						<br/>
						<p style="font-size:12px; text-align:left;">
							<input name="chk" id="chk" type="checkbox" value="${result.module}" class="orgCheck"/>
							<input name="lesson_<c:out value="${result.module}"/>" id="lesson_<c:out value="${result.module}"/>" type="hidden" value="${result.lesson}" />
							<a href="#none" onclick="doShowList('<c:out value="${result.module}"/>')"><c:out value="${result.module}"/> - <c:out value="${result.moduleName}"/></a>
						</p>
						<div class="edu" id="${result.module}" style="display:none">                         
	</c:if>
						<div>                         
							<dl>
								<dd>
									<c:out value="${result.lesson}"/> - <c:out value="${result.lessonName}"/>
									<a href="#none" onclick="parent.doContentFile('<c:out value="${result.module}"/>','<c:out value="${result.lesson}"/>')"><img src="/images/lcms/icon_mm.gif" alt="콘텐츠파일관리" title="콘텐츠파일관리"/></a>
									<a href="#none" onclick="parent.doPreview('<c:out value="${result.module}"/>','<c:out value="${result.lesson}"/>')"><img src="/images/lcms/icon_view.gif" alt="미리보기" title="미리보기"/></a>
									
								</dd>
							</dl>
						</div>
</c:forEach>
			</thead>
		</table>
	</c:if>
	<!-- list table-->
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	
	var height = "";
	window.resizeTo(window.outerWidth - 300, 0);
	if( parent.e.contentDocument ){
		height = parent.e.contentDocument.body.scrollHeight;
	}else{
		height = parent.e.contentWindow.document.body.scrollHeight;
	}
	window.resizeTo(window.outerWidth - 300, height);


	// 차시 리스트 toggle
	function doShowList( showId ){
		var frm = document.orgForm;
		window.resizeTo(window.outerWidth - 300, 0);
		var target = eval("document.getElementById('"+showId+"')");
		target.style.display = target.style.display == "" ? "none" : "";
		var height = parent.e.contentDocument.body.scrollHeight;
		window.resizeTo(window.outerWidth - 300, height);
	}

	function doCheckYn(){
		var frm = document.orgForm;
		var chkBox = frm.chk;
		var checkCount = 0;
		if( chkBox ){
			if( chkBox.length ){
				for( i=0; i<chkBox.length; i++ ){
					if( chkBox[i].checked ) checkCount++;
				}
			}else{
				if( chkBox.checked ) checkCount++;
			}
		}
		if( checkCount == 0 ){
			alert("선택된 차시가 없습니다.");
			return false;
		}
		return true;
	}

	//전체선택
	function allChecked(){
		var frm = document.orgForm;
		var chkBox = frm.chk;
		if( chkBox ){
			if( chkBox.length ){
				for( i=0; i<chkBox.length; i++ ){
					chkBox[i].checked = frm.allCheck.checked;
				}
			}else{
				chkBox.checked = frm.allCheck.checked;
			}
		}
	}

	function doSelectCheckBox(){
		var frm = document.orgForm;
		var chkBox = frm.chk;
		var selectRow = 0;
		var checkCount = 0;
		if( chkBox ){
			if( chkBox.length ){
				for( var i=0; i<chkBox.length; i++ ){
					if(chkBox[i].checked){
						checkCount++;
					}
					if( checkCount < 1 ){
						selectRow++;
					}
				}
			}else{
				if(chkBox.checked){
					checkCount++;
				}
				if( checkCount < 1 ){
					selectRow++;
				}
			}
		}
		if( checkCount < 1 ){
			alert("선택된 차시가 없습니다.");
			return -1;
		}
		if( checkCount > 1 ){
			alert("차시는 하나만 선택하세요.");
			return -1;
		}

		return selectRow;
	}

	function doCheckOrgVal(){
		var frm = document.orgForm;
		var chkBox = frm.chk;
		var resultVal = "";
		if( chkBox ){
			if( chkBox.length ){
				for( var i=0; i<chkBox.length; i++ ){
					if(chkBox[i].checked) resultVal = chkBox[i].value;
				}
			}else{
				if( chkBox.checked ) resultVal = chkBox.value;
			}
		}
		return resultVal;
	}


	function doOrgCheckList(){
		var frm = document.orgForm;
		var chkBox = frm.chk;
		var str = "";
		if( chkBox ){
			if( chkBox.length ){
				for( var i=0; i<chkBox.length; i++ ){
					if( chkBox[i].checked ){
						if( str != "" ) str += "','";
						str += chkBox[i].value;
					}
				}
			}else{
				if( chkBox.checked ) str = chkBox.value;
			}
		}
		return str;
	}

	function doOrgList(){
		var frm = document.orgForm;
		var chkBox = frm.chk;
		var str = "";
		if( chkBox ){
			if( chkBox.length ){
				for( var i=0; i<chkBox.length; i++ ){
					str += chkBox[i].value +",";
				}
			}else{
				str = chkBox.value;
			}
		}
		return str;
	}

	function doSelectModule(){

		var frm = document.orgForm;
		var chkBox = frm.chk;
		var checkCount = 0;
		if( chkBox ){
			if( chkBox.length ){
				for( var i=0; i<chkBox.length; i++ ){
					if(chkBox[i].checked){
						checkCount++;
						module = chkBox[i].value;
					}
				}
			}else{
				if(chkBox.checked){
					checkCount++;
					module = chkBox.value;
				}
			}
		}
		if( checkCount < 1 ){
			alert("선택된 차시가 없습니다.");
			return "";
		}
		if( checkCount > 1 ){
			alert("차시는 하나만 선택하세요.");
			return "";
		}
		return module;
	}

</script>