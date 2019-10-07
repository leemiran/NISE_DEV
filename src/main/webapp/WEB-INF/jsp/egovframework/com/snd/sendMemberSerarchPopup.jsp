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
	
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in" style="width:700px;">
			<strong class="shTit">회사</strong>
			<select id="p_company" name="p_company">
			<c:forEach items="${compList}" var="result">
				<option value="<c:out value="${result.comp}"/>" <c:if test="${result.comp == p_company}">selected</c:if>><c:out value="${result.companynm}"/></option>
			</c:forEach>
			</select>
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
		
	<div class="btnR"><a href="#none" onclick="whenCommonNewsLetterMailSend(<c:out value="${gsMainForm}"/>, 'p_key1')" class="btn01"><span>뉴스레터</span></a></div>
	<div class="btnR"><a href="#none" onclick="whenCommonSmsSend(<c:out value="${gsMainForm}"/>, 'p_key1')" class="btn01"><span>SMS</span></a></div>
	<div class="btnR"><a href="#none" onclick="whenCommonMailSend(<c:out value="${gsMainForm}"/>, 'p_key1')" class="btn01"><span>메일발송</span></a></div>
			
		
<!--		<div class="btnR"><a href="#" class="btn01" onclick="viewExcelPrintLog()"><span>엑셀출력 로그보기</span></a></div>-->
		<div class="btnR"><a href="#" class="btn01" onclick="excelPrint()"><span>엑셀출력</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="viewLogin()"><span>회원로그인 이력보기</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="viewPwd()"><span>비밀번호 재발급자 명단보기</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="viewLog()"><span>회원조회 로그기록 보기</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="excelUpload('M')"><span>회원엑셀등록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="excelUpload('C')"><span>회사선택회원등록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="memberInsertPopup()"><span>등록</span></a></div>
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
				<col width="120px" />
				<col width="80px" />
				<col width="30px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">성명</th>
					<th scope="row">아이디</th>
					<th scope="row">생년월일</th>
					<th scope="row">구분<br/>학교</th>
					<th scope="row">휴대전화<br/>이메일</th>
					<th scope="row">가입일<br/>최근접속일</th>
					<th scope="row">가입/탈퇴</th>
					<th scope="row"><input type="checkbox" id="checkAll" name="checkAll" onclick="clickAll()"></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td><c:out value="${i.count}"/></td>
					<td><a href="#none" onclick="whenPersonGeneralPopup('<c:out value="${result.userid}"/>')"><b><c:out value="${result.name}"/></b></a></td>
					<td>
					<c:out value="${result.userid}"/>
					</td>
					<td><c:out value="${result.birthDate}"/></td>
					<td><c:out value="${result.gb}"/><br/><c:out value="${result.snm}"/></td>
					<td><c:out value="${result.handphone}"/><br/><c:out value="${result.email}"/></td>
					<td><c:out value="${result.indate}"/><br/><c:out value="${result.lglast}"/></td>
					<td><c:out value="${result.isretire == 'N' ? '가입' : '탈퇴'}"/></td>
					<td><input type="checkbox" id="p_key1" name="p_key1" value="<c:out value="${result.userid}"/>"></td>
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
	function searchList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_action.value = "go";
		frm.action = "/adm/cfg/mem/memberSearchList.do";
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
	// 회원조회 로그기록 보기
	function viewLog(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mem/searchMemberLogList.do";
		frm.target = "_self";
		frm.submit();
	}
	// 비밀번호 재발급자 명단보기
	function viewPwd(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mem/reissueMemberPwdList.do";
		frm.target = "_self";
		frm.submit();
	}
	// 회원로그인 이력보기
	function viewLogin(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
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
			return;
		}
		if( cnt > 1 ){
			alert("대상을 한명만 선택하세요.");
			return;
		}
		frm.action = "/adm/cfg/mem/selectMemberLoginLogList.do";
		frm.target = "_self";
		frm.submit();
	}
	//엑셀출력
	function excelPrint(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mem/memberSearchExcelList.do";
		frm.target = "_self";
		frm.submit();
	}

	//엑셀출력 로그
	function viewExcelPrintLog(){
		var url = "/adm/cfg/mem/selectExcelPrintLogPopup.do";
		window.open(url, '', 'width=750,height=500');
	}

	function excelUpload(type){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'uploadPop', 'width=750,height=500');
		frm.p_type.value = type;
		frm.action = "/adm/cfg/mem/excelUploadPopup.do";
		frm.target = "uploadPop";
		frm.submit();
	}

	function memberInsertPopup(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'insertPop', 'width=1000,height=585');
		frm.action = "/adm/cfg/mem/memberInsertPopup.do";
		frm.target = "insertPop";
		frm.submit();
	}
	var frm2 = eval('document.<c:out value="${gsMainForm}"/>');
	//if("0:0:0:0:0:0:0:1" == frm2.logIpVal.value){
	if("211.173.70.113" == frm2.logIpVal.value || "203.249.17.105"  == frm2.logIpVal.value || "127.0.0.1"  == frm2.logIpVal.value || "0:0:0:0:0:0:0:1" == frm2.logIpVal.value || "192.168.0.9" == frm2.logIpVal.value){
		//alert("oky");
	}else{
		//alert("아이피 "+frm2.logIpVal.value+" 는 잘못된 접근입니다.");
	//	frm2.action ="/adm/com/main/admActionMainPage.do";
		//frm2.submit();
	}
	

	/*
	* 뉴스레터 메일발송
	* form : Form 객체 
	*/
	function whenCommonNewsLetterMailSend() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var url = '/com/snd/memberGubunMailForm.do';
			
		window.open("","whenCommonNewsLetterMailSendPopupWindowPop","width=1000,height=800,scrollbars=yes");
		frm.target = "whenCommonNewsLetterMailSendPopupWindowPop";
		frm.action = url;
		frm.submit();
	}

	
</script>