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
	
	<input type="hidden" id="p_userid" name="p_userid" value="">
	<input type="hidden" id="p_name" name="p_name" value="">
	<input type="hidden" id="p_birthDate" name="p_birthDate" value="">
	<input type="hidden" id="p_handphone" name="p_handphone" value="">
	<input type="hidden" id="p_email_id" name="p_email_id" value="">		
	
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in" style="width:700px;">			
			<select id="p_search" name="p_search">
				<option value="name" 		<c:if test="${p_search == 'name'}">selected</c:if>>이름</option>
				<option value="userid" 		<c:if test="${p_search == 'userid'}">selected</c:if>>아이디</option>
				<option value="handphone" 	<c:if test="${p_search == 'handphone'}">selected</c:if>>핸드폰번호</option>
				<option value="email" 		<c:if test="${p_search == 'email'}">selected</c:if>>EMAIL</option>
				<option value="user_path" 	<c:if test="${p_search == 'user_path'}">selected</c:if>>학교</option>
				<option value="birth_date" 	<c:if test="${p_search == 'birth_date'}">selected</c:if>>생년월일</option>
				<option value="nice_personal_num" 	<c:if test="${p_search == 'nice_personal_num'}">selected</c:if>>나이스개인번호</option>
			</select>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" style="ime-mode:active" onkeypress="javascript:fn_keyEvent('searchList')"/>
			<a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a>
		</div>
	</div>
	<!-- // search wrap -->
	
	<div class="listTop">	
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
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">성명</th>
					<th scope="row">아이디</th>
					<th scope="row">생년월일</th>					
					<th scope="row">휴대전화</th>
					<th scope="row">이메일</th>
					<th scope="row">중복 수</th>
					<th scope="row">통합</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td><c:out value="${i.count}"/></td>
					<td><a href="#none" onclick="whenPersonGeneralPopup('<c:out value="${result.userid}"/>')"><b><c:out value="${result.name}"/></b></a></td>
					<td><c:out value="${result.userid}"/></td>
					<td><c:out value="${result.birthDate}"/></td>
					<td><c:out value="${result.handphone}"/></td>
					<td><c:out value="${result.email}"/></td>					
					<td><c:out value="${result.duptCnt}"/></td>
					<td><a href="#none" onclick="goMerge('${result.userid}', '${result.name}','${result.birthDate}','${result.handphone}','${result.email}')">통합</a></td>
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
	
   
	function goMerge(userid, name, birthDate, handphone, email){
		frm.p_userid.value = userid;
		frm.p_name.value = name;
		frm.p_birthDate.value = birthDate;
		frm.p_handphone.value = handphone;
		frm.p_email_id.value = email;
		frm.action = "/adm/cfg/mem/memberMergeDetailList.do";
		frm.target = "_self";
		frm.submit();
		
	}
	
</script>