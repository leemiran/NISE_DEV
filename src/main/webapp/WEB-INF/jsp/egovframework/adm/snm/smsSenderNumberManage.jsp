<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>	
	
	<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                    <col width="7%" />
                    <col width="35%" />
                </colgroup>
                <tbody>
                    <tr class="title">
                        <th>발신자명<font color="red">(*)</font></th>
                        <td class="" >
                               <input type="text" name="adminName" size="20" maxlength="20" value="<c:out value="${adminName}"/>" readonly="readonly">
                        </td>
                    </tr>
                    <tr class="title">
                        <th>발신자 전화번호<font color="red">(*)</font></th>
                        <td class="" >
                          	<input type="text" name="adminTel1" id="adminTel1" size="3" maxlength="3" value="<c:out value="${adminTel1}"/>"> -
                          	<input type="text" name="adminTel2" id="adminTel2" size="4" maxlength="4" value="<c:out value="${adminTel2}"/>"> -
                          	<input type="text" name="adminTel3" id="adminTel3" size="4" maxlength="4" value="<c:out value="${adminTel3}"/>">
                        </td>
                    </tr>
                </tbody>
            </table>				
		</div>
        <!-- // detail wrap -->  	
</form>
 <div class="btnR MR05">
  	<a href="#none" onclick="smsSenterUpdate();" class="btn01"><span>저 장</span></a>
 </div>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
var e;
var oldTr = "";

	/* ********************************************************
	 * 문자 발신자 전화 번호 수정
	 ******************************************************** */
	 function smsSenterUpdate() {
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if(frm.adminTel1.value == "" || frm.adminTel2.value == "" || frm.adminTel3.value == ""){
			alert("전화 번호는 필수 입력값입니다.");
			return;
		}
		 frm.action = "/adm/snm/updateSmsSenderNumberManage.do";
		 frm.target = "_self";
		 frm.submit();
	}
	
</script>