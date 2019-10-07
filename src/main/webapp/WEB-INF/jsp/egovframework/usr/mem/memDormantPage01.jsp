<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>

<script type="text/javascript"> 
//<![CDATA[
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
//]]>
</script> 
<!-- sci 인증 -->
<%@ include file = "/WEB-INF/jsp/egovframework/svt/cert/sci/sciInclude.jsp" %>
<script type="text/javascript">
	//window.name = "Parent_window";
	
	function namechk(){
		/* 
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		
		if(frm.p_userid_ok.value.search(/\S/) < 0) {
	        alert("아이디를 입력하세요.");
	        frm.p_userid_ok.focus();
	        return;
	    }
		
		window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
		document.form_chk.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
		document.form_chk.target = "popupChk";
		
		document.form_chk.submit(); */
		window.name = "parentPage";
		
		if($('#p_userid_ok').val() == '') {
			alert('아이디를 입력해 주세요.');
			$('#p_userid_ok').focus();
			return;
		}
		
		reqSci('dormant', $('#p_userid_ok').val());
	}
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/commCheckPlusSafe.jsp" %>
<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="/usr/mem/memDormantPage01.do">
	<fieldset>
	<legend>아이디/비밀번호 찾기</legend>
		<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
		<input type="hidden" name="p_process" />
		<input type="hidden" name="p_resno" />
		<input type="hidden" name="p_mode" />
		<input type="hidden" name="SelectedAlgID"/>
		<input type="hidden" name="p_searchGubun" value="main"/>
		
		
		<!--아이디 찾기-->
		<div class="sub_text_2" style="padding-left:25px;padding-bottom:10px;">
			<h4>휴면계정 본인 인증</h4>                
		</div>

		
		<div class="mstab">
			<dl class="idfind leftp">
				<dt class="cen"></dt>
				<dt class="idfind1"><label for="p_userid_1">아이디</label></dt>
				<dd class="idfind2"><input type="text" name="p_userid_ok" id="p_userid_ok"  size="38" title="아이디"></dd>
				<dd class="idfind8"><a href="javascript:;" onclick="namechk();" title="새창"><img src="/images/user/btn_sil.gif" alt="안심본인인증"></a></dd>
			</dl>
		</div>
					
		
		
		
		
	</fieldset>           
</form>
<!--<script type="text/javascript" src="/EPKI/EPKICommon.js"></script>-->
<!-- 본인인증 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
			<form name="form_chk" method="post">
				<input type="hidden" name="m" value="checkplusSerivce">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
				<input type="hidden" name="EncodeData" value="<%= sEncData %>">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
			    
			    <!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
			    	 해당 파라미터는 추가하실 수 없습니다. -->
				<input type="hidden" name="param_r1" value="">
				<input type="hidden" name="param_r2" value="">
				<input type="hidden" name="param_r3" value="">
			</form>
			
			<form name="nameForm" id="nameForm" method="post">
				
				<input type="hidden" name="sCipherTime" value=""/>
				<input type="hidden" name="REQ_SEQ" value=""/>
				<input type="hidden" name="RES_SEQ" value=""/>
				<input type="hidden" name="AUTH_TYPE" value=""/>
				<input type="hidden" name="realName" value=""/>
				<input type="hidden" name="DI" value=""/>
				<input type="hidden" name="CI" value=""/>
				<input type="hidden" name="birthDate" value=""/>
				<input type="hidden" name="sex" value=""/>
				<input type="hidden" name="NATIONAINFO" value=""/>
				<input type="hidden" name="certgubun" value="C"/>
				<input type="hidden" name="virtualNo" value=""/>
				<input type="hidden" name="developMode"/>
				<input type="hidden" name="p_process"/>
				<input type="hidden" name="p_agreement1"	value="Y"/>
				<input type="hidden" name="p_agreement2"	value="Y"/>
				<input type="hidden" name="p_agreement3"	value="Y"/>
				<input type="hidden" name="p_userid_ok" id="p_name_1" />
				
			</form>    

<script type="text/javascript">
function enter(e)  {
    if (e.keyCode =='13'){
        ok();
        e.returnValue = false;
        e.cancelBubble = true;
    }
}

function goMemberJoinStep3(){
	var frm = document.nameForm;
	var frm1 = eval('document.<c:out value="${gsMainForm}"/>');
	frm.p_userid_ok.value = frm1.p_userid_ok.value;
	frm.action ="/usr/mem/memDormantPage02.do";
	frm.submit();
}
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->