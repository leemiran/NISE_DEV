<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>

<!-- 부트스트랩  CSS -->
<%-- <link rel="stylesheet" href="<c:out value="${gsDomainContext}"/>/kcase/lib/css/bootstrap.min.css"/> --%>
<!-- KSign Style Sheet -->
<link rel="stylesheet" href="<c:out value="${gsDomainContext}"/>/kcase/lib/css/kcase.css"/>
<!-- Sample CSS -->
<link rel="stylesheet" href="<c:out value="${gsDomainContext}"/>/kcase/lib/css/sample.css"/>
<script src="<c:out value="${gsDomainContext}"/>/kcase/lib/js/jquery-1.11.3.js"></script>
<script src="<c:out value="${gsDomainContext}"/>/kcase/lib/js/jquery-ui.js"></script>
<script src="<c:out value="${gsDomainContext}"/>/kcase/lib/js/kcase_os_check.js"></script>
<script src="<c:out value="${gsDomainContext}"/>/kcase/EPKICommon.js"></script>

<style>
#loading_login {
	display:	none;
	position:	absolute;
	left:		50%;
	margin-left:-500px;
    z-index:    1000;
    width:      100%;
	height:      100%;
}

</style>

<center>
<div id="loading_login"><img alt="로딩중..." src="/images/loading_login.gif" /></div>
</center>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="/usr/mem/userIdPwdSearch02.do">
	<input type="hidden" name="p_mode" />
	<fieldset>
		<legend>아이디/비밀번호 찾기</legend>
		<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

		<div class="conwrap2">
			<ul class="mtab2" id="idTab">
				<li><a href="javascript:void(0);" onclick="idTabChange('Basic', $(this));" class="on">아이디찾기</a></li>
				<li><a href="javascript:void(0);" onclick="idTabChange('Epki', $(this));">아이디찾기(인증서)</a></li>
				<li class="end"><a href="javascript:void(0);" onclick="idTabChange('Gpin', $(this));">아이디찾기(I-PIN)</a></li>
			</ul>
		</div>
		
		<div class="mstab" id="idInputDiv"></div>

		<div class="conwrap2" style="margin-top: 70px;">
			<ul class="mtab2" id="pwdTab">
				<li><a href="javascript:void(0);" onclick="pwdTabChange('Basic', $(this))" class="on">비밀번호찾기</a></li>
				<li><a href="javascript:void(0);" onclick="pwdTabChange('Epki', $(this))">비밀번호찾기(인증서)</a></li>
				<li class="end"><a href="javascript:void(0);" onclick="pwdTabChange('Gpin', $(this))">비밀번호찾기(I-PIN)</a></li>
			</ul>
		</div>
		
		<div class="mstab" id="pwdInputDiv"></div>
	</fieldset>
</form>

<div style="display: none;">
	<!-- id -->
	<div id="idBasic">
		<div class="mstab">
			<dl class="idfind leftp">
				<dt class="cen"></dt>
				<dt class="idfind1"><label for="p_name">이 름</label></dt>
				<dd class="idfind2"><input type="text" name="p_name" id="p_name" size="38"/></dd>
				<dt class="idfind3"><label for="p_email">이메일</label></dt>
				<dd class="idfind7"><input type="text" name="p_email" id="p_email" size="25"  title="이메일"/> </dd>											
				<dd class="idfind6"><a href="javascript:;" onclick="findIdBasic();"><img src="/images/user/btn_jumin.gif" alt="확인" /></a></dd>
			</dl>
		</div>
	</div>
	<div id="idEpki">
		<div class="mstab">
			<dl class="idfind leftp">
				<dt class="cen"></dt>
				<dt class="idfind1"><label for="realName">이 름</label></dt>
				<dd class="idfind2"><input type="text" name="id_realName" id="id_realName" size="38"/></dd>
				<!--				 
				<dt class="idfind3"><label for="jumin">주민등록번호</label></dt>
				<dd class="idfind7">
					<input type="password" name="jumin1" id="jumin1" maxlength="6" size="10" title="주민번호 앞자리"/> - 
					<input type="password" name="jumin2" id="jumin2" maxlength="7" size="10" title="주민번호 뒷자리"/>
					<input type="hidden" name="RequestData" />
				</dd> 															
				 <dd class="idfind6" style="padding-left: 40px;"><a href="javascript:;" onclick="findIdEpki();" title="새창"><img src="/images/user/btn_epki.gif" alt="EPKI인증" /></a></dd> 
				 -->
				<dd class="idfind6" style="padding-left: 40px;"><a href="javascript:;" onclick="findIdEpkihtml5Id();" title="새창"><img src="/images/user/btn_epki.gif" alt="EPKI인증" /></a></dd>
			</dl>
		</div>
		<strong>
<!-- 		※ 입력하시는 주민번호는 인증에만 사용되며 서버에 저장되지 않습니다.<br/> -->
		※ 개인정보수정시 인증서를 직접 등록하지 않으신 경우 아이디가 조회되지 않습니다.
		</strong>
	</div>
	<div id="idGpin">
		<div class="mstab">
			<dl class="idfind leftp">
				<dt class="cen"></dt>
				<dd class="idfind6" style="padding-left: 40px;"><a href="javascript:;" onclick="certGpin('ID');" title="새창"><img src="/images/user/btn_ipins.gif" alt="아이핀인증" /></a></dd>
			</dl>
		</div>
		<strong>
		※ 아이핀 인증을 통해 가입하신 회원에 한해서 조회가 가능합니다.
		</strong>
	</div>
	
	<!-- pwd -->
	<div id="pwdBasic">
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
				<dd class="idfind6"><a href="javascript:;" onclick="findPwdBasic();return false;"><img src="/images/user/btn_jumin.gif" alt="확인"/></a></dd>	
			</dl>
		</div>
	</div>
	<div id="pwdEpki">
		<div class="mstab">
			<dl class="idfind leftp">
			<dt class="cen"></dt>
			<dt class="idfind1"><label for="userid">아이디</label></dt>
			<dd class="idfind2"><input type="text" name="userid" id="userid" size="38"/></dd>
			<dt class="idfind1"><label for="realName">이 름</label></dt>
			<dd class="idfind2"><input type="text" name="realName" id="realName" size="38"/></dd>
			
			<!-- <dt class="idfind3"><label for="jumin">주민등록번호</label></dt>
			<dd class="idfind7">
				<input type="password" name="jumin1" id="jumin1" maxlength="6" size="10" title="주민번호 앞자리"/> - 
				<input type="password" name="jumin2" id="jumin2" maxlength="7" size="10" title="주민번호 뒷자리"/>
				<input type="hidden" name="RequestData" />
			</dd> -->
														
			<dd class="idfind6" style="padding-left: 40px;"><a href="javascript:;" onclick="findIdEpkihtml5Pw();" title="새창"><img src="/images/user/btn_epki.gif" alt="EPKI인증" /></a></dd>
			</dl>
		</div>
		<strong>
		<!-- ※ 입력하시는 주민번호는 인증에만 사용되며 서버에 저장되지 않습니다.<br/> -->
		※ 개인정보수정시 인증서를 직접 등록하지 않으신 경우 아이디가 조회되지 않습니다.
		</strong>
	</div>
	<div id="pwdGpin">
		<div class="mstab">
			<dl class="idfind leftp">
				<dt class="cen"></dt>
				<dd class="idfind6" style="padding-left: 40px;"><a href="javascript:;" onclick="certGpin('PWD');" title="새창"><img src="/images/user/btn_ipins.gif" alt="아이핀인증" /></a></dd>
			</dl>
		</div>
		<strong>
		※ 아이핀 인증을 통해 가입하신 회원에 한해서 조회가 가능합니다.
		</strong>
	</div>
</div>

<div id="epkidivId">
<form id="epki_form_id" name="epki_form_id" action="/cert/epki/findIdHtml5.do" method="post">
<input type="hidden" name="epki_id_realName" id="epki_id_realName" />
	<div style="display: none">
		<textarea name="reqSecLoginData" ></textarea>
	</div>
</form>
</div>

<div id="epkidivPw">
<form id="epki_form_pw" name="epki_form_pw" action="/cert/epki/findPwHtml5.do" method="post">
<input type="hidden" name="epki_pw_realName" id="epki_pw_realName" />
<input type="hidden" name="epki_pw_userid" id="epki_pw_userid" />

	<div style="display: none">
		<textarea name="reqSecLoginData" ></textarea>
	</div>
</form>
</div>

<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/EPKI/EPKICommon.js"></script> --%>
<script type="text/javascript">
//EPKI Client Toolkit 설치 확인, 객체 생성 및 공통 속성 지정
//SetupObjECT(true);
//InitObjECT();

$(document).ready(function () {
	if('${resultMsg}') {
		alert("${resultMsg}");
	}
	
	$('#idInputDiv').html($('#idBasic').html());
	$('#pwdInputDiv').html($('#pwdBasic').html());
});

function idTabChange(subId, _self) {
	_self.addClass('on');
	$('#idTab li a').not(_self).removeClass('on');
	
	$('#idInputDiv').html($('#id' + subId).html());
}

function pwdTabChange(subPwd, _self) {
	_self.addClass('on');
	$('#pwdTab li a').not(_self).removeClass('on');
	
	$('#pwdInputDiv').html($('#pwd' + subPwd).html());
}

function findIdBasic() {
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
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
    frm.submit();
}

/* function findIdEpki() {
	var strRequestData;
	var theForm = eval('document.<c:out value="${gsMainForm}"/>');

	if(theForm.realName.value == "")
	{
		alert("이름을 입력하십시오");
		return;
	}
	var jumin = theForm.jumin1.value + theForm.jumin2.value;
	if(jumin == "")
	{
		alert("식별번호를 입력하십시오");
		return;
	}
        
	// 서버측에서 사용자 ID를 입력하는 경우
	strRequestData = RequestVerifyVID("${strServerCert}", jumin);
	if(strRequestData == "")
	{
        ECTErrorInfo();
	}else if(strRequestData == "100"){
		// 취소버튼 클릭시
		return;
	}
    else
	{	
		theForm.RequestData.value = strRequestData;
		theForm.action = "${pageContext.request.contextPath}/cert/epki/findId.do";
    	theForm.target = "_self";
		theForm.submit();
	}
} */

// password
function findPwdBasic() {
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
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
    frm.submit();
}

/* function findPwdEpki() {
	var strRequestData;
	var theForm = eval('document.<c:out value="${gsMainForm}"/>');

	if(theForm.userid.value == "")
	{
		alert("아이디를 입력하십시오");
		return;
	}
	if(theForm.realName.value == "")
	{
		alert("이름을 입력하십시오");
		return;
	}
	var jumin = theForm.jumin1.value + theForm.jumin2.value;
	if(jumin == "")
	{
		alert("식별번호를 입력하십시오");
		return;
	}
        
	// 서버측에서 사용자 ID를 입력하는 경우
	strRequestData = RequestVerifyVID("${strServerCert}", jumin);
	if(strRequestData == "")
	{
        ECTErrorInfo();
	}else if(strRequestData == "100"){
		// 취소버튼 클릭시
		return;
	}
    else
	{	
		theForm.RequestData.value = strRequestData;
		theForm.action = "${pageContext.request.contextPath}/cert/epki/findPwd.do";
    	theForm.target = "_self";
		theForm.submit();
	}
} */

// ipin
function certGpin(findGubun) {
	var url = "${pageContext.request.contextPath}/I-PIN/IPin_checkplus_main.jsp?findGubun=" + findGubun;
	var winname = "gPinFindWin";
	
	var wWidth = 360;
	var wHight = 120;
	
	var wX = (window.screen.width - wWidth) / 2;
	var wY = (window.screen.height - wHight) / 2;
	
	var w = window.open(url, winname, "directories=no,toolbar=no,left="+wX+",top="+wY+",width="+wWidth+",height="+wHight);
}
</script>

<script type="text/javascript">

$(document).ready(function() {

	<c:if test="${!isDevelopmentMode}">
	
	    var sessionId = '${sessionId}';
	    var serverCert = '${serverCert}';
		var algorithm = "SEED";
		
	    epki.init(sessionId);
	        
	    var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
	    
	    html5EpkiId = function() {	
	    	var id_realName = $("#id_realName").val();
			if(id_realName == ""){
				alert("이름을 입력하세요.");
				$("#id_realName").focus();
			    return;
			}
			$("#epki_id_realName").val(id_realName);
            var vidTexts = $("#epki_form_id").find("textarea"); 
            
            /* Call Back Function Definition */
            var success = function(output) {            
            	vidTexts.eq(0).val(output);
                $("#epkidivId").find("form").submit();	
              	$("#loading_login").show();
            };
            var error = function(errCode, errMsg) {
                alert(errCode + ": " + errMsg);
            };

            epki.reqSecChannelAndLogin(serverCert, algorithm, sessionId, success, error);//주민번호 없이
            //epki.requestVerifyVID(serverCert, userId, success, error); //주민번호

	    }
	   
        /* $("#epkiBtn img").eq(0).click(function() {        	
        	html5Epki();
        	
        }); */        
        html5EpkiPw = function() {	
	    	var userid = $("#userid").val();
	    	if(userid == ""){
				alert("아이디를 입력하세요.");
				$("#userid").focus();
			    return;
			}
	    	
	    	var realName = $("#realName").val();
			if(realName == ""){
				alert("이름을 입력하세요.");
				$("#realName").focus();
			    return;
			}
			$("#epki_pw_realName").val(realName);
			$("#epki_pw_userid").val(userid);
			
            var vidTexts = $("#epki_form_pw").find("textarea"); 
            
            /* Call Back Function Definition */
            var success = function(output) {            
            	vidTexts.eq(0).val(output);
                $("#epkidivPw").find("form").submit();	
              	$("#loading_login").show();
            };
            var error = function(errCode, errMsg) {
                alert(errCode + ": " + errMsg);
            };

            epki.reqSecChannelAndLogin(serverCert, algorithm, sessionId, success, error);//주민번호 없이
            //epki.requestVerifyVID(serverCert, userId, success, error); //주민번호

	    }
	        
    </c:if>
		
});
function findIdEpkihtml5Id(){	
	html5EpkiId();	
}   
function findIdEpkihtml5Pw(){	
	html5EpkiPw();	
}   
</script>

<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>