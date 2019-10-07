<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<script type="text/javascript">
	//window.name = "Parent_window";
	
	function namechk(){
		
		
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		
		if(frm.p_userid_ok.value.search(/\S/) < 0) {
	        alert("아이디를 입력하세요.");
	        frm.p_userid_ok.focus();
	        return;
	    }
		
		window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
		document.form_chk.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
		document.form_chk.target = "popupChk";
		
		document.form_chk.submit();
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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="/usr/mem/userIdPwdSearch02.do">
	<fieldset>
	<legend>아이디/비밀번호 찾기</legend>
		<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
		<input type="hidden" name="p_process" />
		<input type="hidden" name="p_resno" />
		<input type="hidden" name="p_mode" />
		<input type="hidden" name="SelectedAlgID"/>
		<input type="hidden" name="p_searchGubun" value="main"/>
	
	<input type="hidden" name="virtualNo" value="${virtualNo}"/>
	<input type="hidden" name="authInfo" value="${authInfo}"/>
	<input type="hidden" name="realName" value="${realName}"/>
	<input type="hidden" name="birthDate" value="${birthDate}"/>
	
	<input type="hidden" name="developMode"/>
	
		
		
		<!--아이디 찾기-->
		<div class="sub_text_2" style="padding-left:25px;padding-bottom:10px;">
			<h4>아이디찾기</h4>                
		</div>
		<div class="mstab">
			<dl class="idfind leftp">
			<dt class="cen"></dt>
			<dt class="idfind1"><label for="p_name">이 름</label></dt>
			<dd class="idfind2"><input type="text" name="p_name" id="p_name" size="38"/></dd>
			<dt class="idfind3"><label for="p_email">이메일</label></dt>
			<dd class="idfind7"><input type="text" name="p_email" id="p_email" size="25"  title="이메일"/> </dd>											
			<dd class="idfind6"><a href="javascript:;" onclick="ok();"><img src="/images/user/btn_jumin.gif" alt="확인" /></a></dd>
			</dl>
		</div>
		<!--비밀번호 찾기-->
		<div class="sub_text_2" style="padding-left:25px;padding-bottom:10px;">
			<h4>비밀번호찾기</h4>                
		</div>
		<div class="mstab">
			<dl class="idfind leftp">
			<dt class="cen"></dt>
			<dt class="idfind1"><label for="p_userid_1">아이디</label></dt>
			<dd class="idfind2"><input type="text" name="p_userid_1" id="p_userid_1" size="38" /></dd>
			<dt class="idfind1"><label for="p_name_1">이름</label></dt>
			<dd class="idfind2"><input type="text" name="p_name_1" id="p_name_1" size="38" /></dd>
			
			<dt class="idfind3"><label for="p_hp1">핸드폰</label></dt>
			<dd class="idfind7">
			<input type="text" name="p_hp1" id="p_hp1" maxlength="3" size="4" title="핸드폰 국번" value="010"/> - 
			<input type="text" name="p_hp2" id="p_hp2" maxlength="4" size="5" title="핸드폰 앞자리"/> - 
			<input type="text" name="p_hp3" id="p_hp3" maxlength="4" size="5" title="핸드폰 뒷자리"/>
			</dd>
			
			<dt class="idfind3"><label for="p_email_1">이메일</label></dt>
			<dd class="idfind7"><input type="text" name="p_email_1" id="p_email_1" size="25" title="이메일"/> </dd>									
			<dd class="idfind6"><a href="javascript:;" onclick="pwdSearch();return false;"><img src="/images/user/btn_jumin.gif" alt="확인"/></a></dd>	
			
			
			</dl>
		</div>
		<div class="mstab">
			<dl class="idfind leftp">
				<dt class="cen"></dt>
				<dt class="idfind1"><label for="p_userid_1">아이디</label></dt>
				<dd class="idfind2"><input type="text" name="p_userid_ok" id="p_userid_ok" size="38"></dd>
				<dd class="idfind8"><a href="#none" onclick="javascript:namechk();" title="새창"><img src="/images/user/btn_sil.gif" alt="안심본인인증"></a></dd>
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
<!--
SetupObjECT(true);
InitObjECT();
<%//주민등록번호(뒷번호 첫자리가 1-4), 법인번호(뒷번호 첫자리가 0), 외국인 번호(뒷번호 첫자리가 5-9) 유효성 검증%>
function validSsn( ssn ) {
    if ( -1>=ssn.search( /^\d\d[01]\d[0-3]\d\d\d{6}$/ ) )
        return false;
    var parity = 0;
    for ( i=0; i<12; i++ )
        parity += ssn.charAt( i ) * ( i%8 + 2 )
    parity  = ( 11 - parity % 11 ) % 10;
    return parity == ssn.charAt(12);
}

<%// 확인버튼 클릭시 함수 호출 %>
function ok() {
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	var strEnvelopedData;
	var resNo1;
	var nError = 0;	
    if(frm.p_name.value.search(/\S/) < 0) {
        alert("이름을 입력하세요.");
        frm.p_name.focus();
        return;
    }
    if(frm.p_email.value == ''  ){
        alert( "이메일을 입력하세요.");
        frm.p_email.focus();
        return;
    }
 
    frm.p_mode.value = "";

    /*
    <c:if test="${!isDevelopmentMode}">
		var recipientCerts = new Array();
		recipientCerts[0] = "${serverCert}";
	
		strEnvelopedData = Envelop("SEED", recipientCerts, frm.p_resno.value);
		resNo1 = Envelop("SEED", recipientCerts, frm.p_resno2.value);
	
		if(strEnvelopedData == "")
		{
			ECTErrorInfo();
			return;
		}
	    else
		{
	    	frm.SelectedAlgID.value = "SEED";
	    	frm.p_resno.value = strEnvelopedData;
	    	frm.p_resno2.value = resNo1;
		}
	</c:if>
    */
    frm.submit();
}
function goMemberJoinStep3(){
	var frm = document.nameForm;
	var frm1 = eval('document.<c:out value="${gsMainForm}"/>');
	frm.p_userid_ok.value = frm1.p_userid_ok.value;
	frm.action ="/usr/mem/userIdPwdSearch0201.do";
	frm.submit();
}

function pwdSearch() {
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	var strEnvelopedUserId;
	var strEnvelopedResNo;
	var strEnvelopedResNo2;
	var strEnvelopedHp1;
	var strEnvelopedHp2;
	var strEnvelopedHp3;
	var nError = 0;

	
	if(frm.p_userid_1.value.search(/\S/) < 0) {
        alert("아이디를 입력하세요.");
        frm.p_userid_1.focus();
        return;
    }

	if(frm.p_name_1.value.search(/\S/) < 0) {
        alert("이름을 입력하세요.");
        frm.p_name_1.focus();
        return;
    }

	if(frm.p_hp1.value.search(/\S/) < 0 ){
        alert( "핸드폰번호를 입력하세요.");
        frm.p_hp1.focus();
        return;
    }

	if(frm.p_hp2.value.search(/\S/) < 0  ){
        alert( "핸드폰번호를 입력하세요.");
        frm.p_hp2.focus();
        return;
    }

	if(frm.p_hp3.value.search(/\S/) < 0 ){
        alert( "핸드폰번호를 입력하세요.");
        frm.p_hp3.focus();
        return;
    }
    
    frm.p_mode.value = "pwdcheck";

    /*
    <c:if test="${!isDevelopmentMode}">
		var recipientCerts = new Array();
		recipientCerts[0] = "${serverCert}";
	
		strEnvelopedUserId = Envelop("SEED", recipientCerts, frm.p_userid_1.value);
		strEnvelopedResNo = Envelop("SEED", recipientCerts, frm.p_resno.value);
		strEnvelopedResNo2 = Envelop("SEED", recipientCerts, frm.p_resno2_1.value);

		//strEnvelopedHp1 = Envelop("SEED", recipientCerts, frm.p_hp1.value);
		//strEnvelopedHp2 = Envelop("SEED", recipientCerts, frm.p_hp2.value);
		//strEnvelopedHp3 = Envelop("SEED", recipientCerts, frm.p_hp3.value);
		
	
		if(strEnvelopedResNo == "")
		{
			ECTErrorInfo();
			return;
		}
	    else
		{
	    	frm.SelectedAlgID.value = "SEED";
	    	frm.p_userid_1.value = strEnvelopedUserId;
			frm.p_resno.value = strEnvelopedResNo;
			frm.p_resno2_1.value = strEnvelopedResNo2;

			//frm.p_hp1.value = strEnvelopedHp1;
			//frm.p_hp2.value = strEnvelopedHp2;
			//frm.p_hp3.value = strEnvelopedHp3;
		}
	</c:if>
	*/
    
    frm.submit();
}
<%// 주민등록번호 입력완료후 엔터처리 %>
function enter(e)  {
    if (e.keyCode =='13'){
        ok();
        e.returnValue = false;
        e.cancelBubble = true;
    }
}

window.onload = function() {
   	if(document.getElementById("p_name"))
       	document.getElementById("p_name").focus();
}

//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->