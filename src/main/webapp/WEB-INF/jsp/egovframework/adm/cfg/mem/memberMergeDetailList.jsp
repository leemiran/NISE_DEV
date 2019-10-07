<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" id="p_action" name="p_action">
	<input type="hidden" id="p_type" name="p_type">
	<input type="hidden" id="logIpVal" value="${logIp}" />
	
	<input type="hidden" id="userid" name="userid" value="">
	<input type="hidden" id="name" name="name" value="">
	<input type="hidden" id="birthDate" name="birthDate" value="">
	<input type="hidden" id="handphone" name="handphone" value="">
	<input type="hidden" id="email_id" name="email_id" value="">		
	
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="mergeAction()"><span>아이디 통합</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col width="150px" />
				<col width="180px"/>
				<col width="80px" />
				<col width="200px" />
				<col />
				<col width="150px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">성명</th>
					<th scope="row">아이디</th>
					<th scope="row">생년월일</th>					
					<th scope="row">휴대전화</th>
					<th scope="row">이메일</th>
					<th scope="row">통합기준</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<input type="hidden" name="_Array_t_userIds" id="_Array_t_userIds" value="${result.userid}">
					<td><c:out value="${i.count}"/></td>
					<td><a href="#none" onclick="whenPersonGeneralPopup('<c:out value="${result.userid}"/>')"><b><c:out value="${result.name}"/></b></a></td>
					<td><c:out value="${result.userid}"/></td>
					<td><c:out value="${result.birthDate}"/></td>
					<td><c:out value="${result.handphone}"/></td>
					<td><c:out value="${result.email}"/></td>
					<td><input type="radio" name="mergeId" id="mergeId" value="${result.userid}"></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
</form>



	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	function searchList(){		
		frm.p_action.value = "go";
		frm.action = "/adm/cfg/mem/memberMergeList.do";
		frm.target = "_self";
		frm.submit();
	}

	function clickAll(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var checkType = frm.checkAll.checked;
		var target = frm.p_key1;
		if( target ){
			if(target.length){
				for( i=0; i<target.length; i++ ){
					target[i].checked = checkType;
				}
			}else{
				target.checked = checkType;
			}
		}else{
			alert("조회된 대상이 없습니다.");
			frm.checkAll.checked = false;
		}
	}	
   
	function mergeAction(){
		
		var mergeId = checkRadio(frm.mergeId);
		if( mergeId == "" ){
			alert("통합기준을 선택하세요");
			return;
		}
		
		
		if(confirm("\""+mergeId+"\" 아이디를 기준으로 통합하시겠습니까?")){
			frm.action = "/adm/cfg/mem/memberMergeUpdate.do";
			frm.target = "_self";
			frm.submit();
		}
	}
	
</script>