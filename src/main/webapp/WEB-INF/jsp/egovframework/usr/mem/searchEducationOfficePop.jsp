<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<!-- popup wrapper 팝업사이즈width=650,height=500-->


<div id="popwrapper">
	<div class="popIn" style="height:480px">
		 <!-- tab -->
        <div class="tit_bg">
			<h2>교육청검색</h2>
       		</div>
        
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" action="/usr/mem/searchEducationOfficePop.do">
		<fieldset>
        <legend>교육청검색</legend>
        <input type="hidden" name="action" id="action"/>
		<input type="hidden" name="p_gubun" id="p_gubun" value="${p_gubun}"/>
		<div class="in" style="width:100%; text-align: left; ">		
			<b>찾고자 하는 <font color="red"><b>상위</b></font> '교육청명'을 입력해 주십시오.</b><br/>
			<font color="red"><b>예)서울특별시교육청</b></font><br/>
			<input type="text" name="p_school_nm" id="p_school_nm" value="${search_dong}" size="40" style="ime-mode:active" onkeyup="fn_keyEvent('searchList')"/>
			<a href="#" onclick="searchList()" class="btn_search"><span>검 색</span></a>
		</div>
        </fieldset>
		</form>		
		<p style="height:3px;"/>
		<!-- contents -->
		<div class="tbList">
			<table summary="교육청명으로 구분" cellspacing="0" width="100%">
                <caption>
                교육청검색
                </caption>
                <colgroup>					
					<col width="30%" />
					<col width="70%" />
				</colgroup>
				<tr>
					<th scope="row">시도교육청</th>					
					<th scope="row">교육지원청</th>
				</tr>
			</table>
			<div style="overflow-y:scroll; height:300px;">
				<table summary="교육청명으로 구분" cellspacing="0" width="100%">
                <caption>학교검색</caption>
                <colroup>					
					<col width="30%" />
					<col width="70%" />
				</colgroup>
				<tbody>
				
				
				
				<c:forEach items="${list}" var="result">				
					<tr style="cursor:pointer" onmouseover="overRow(this);" onclick="onSchInfoPut('${result.fstOrgCd}', '${result.sndOrgCd}', '${result.fstOrgNm}', '${result.sndOrgNm}')">
						<td><c:out value="${result.fstOrgNm}"/></td>
						<td><c:out value="${result.sndOrgNm}"/></td>
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
            alert("교육청명을 입력해주세요");
            return;
        }
		frm.action.value = "go";
		frm.submit();
	}

	
	function onSchInfoPut(fstOrgCd, sndOrgCd, fstOrgNm, sndOrgNm) {   
	//var	frm = opener.document.forms[0];
	
	//frm.p_user_path.value = schNm;
	//frm.p_dept_cd.value = fdnDivCd;
	//frm.p_agency_cd.value = schDivCd;
	//frm.p_handphone_no1.value = schTelNo;
	//window.close();

	var arr = new Array();
	arr[0] = fstOrgCd;
	arr[1] = sndOrgCd;
	arr[2] = fstOrgNm;
	arr[3] = sndOrgNm;
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