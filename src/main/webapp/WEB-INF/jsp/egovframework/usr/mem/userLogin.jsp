<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%//@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>
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
<script type="text/javascript"> 
//<![CDATA[
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
function ShowBx(obj){document.getElementById(obj).style.display = "block";} 
function HideBx(obj){document.getElementById(obj).style.display = "none";} 
//]]>
</script> 

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


<center>
<div id="loading_login"><img alt="로딩중..." src="/images/loading_login.gif" /></div>
</center>




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
<div class="aside_box"> 
	<div class="mstab">
	<div id="tab_01" style="display:block;">
	<div class="aside_tab">
	<div class="on sub01"><a href="#" onclick="ShowBx('tab_01');HideBx('tab_02');return false;"><strong>일반 로그인</strong></a></div> 
	<div class="off sub02"><a href="#" onclick="ShowBx('tab_02');HideBx('tab_01');return false;"><strong>공인 인증서 로그인</strong></a></div>
	<div class="clear"></div> 
	</div> 
		<div>
		<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post"  action="https://iedu.nise.go.kr/usr/lgn/portalUserLogin.do" onsubmit="return login();">
			<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
				<input type="hidden" name="SelectedAlgID"/>
				<input type="hidden" name="loginGubun" value="main"/>
				<input type="hidden" name="p_d_type" id="p_d_type"	value="T"/>
								
				
	     		<dl class="idfind leftp">
				<dt class="cen"><img src="/images/user/login_tit.gif" alt="로그인을 하시면 다양한 서비스를 이용하실 수 있습니다."/></dt>
				<dt class="idfind1"><label for="userId">아이디</label></dt>
	            <dd class="idfind2"><input type="text" name="userId" id="userId" size="38" style="ime-mode:disabled" onkeypress="action_enter(event);"/></dd>
				<dt class="idfind3"><label for="pwd">비밀번호</label></dt>
	            <dd class="idfind4"><input type="password" name="pwd" id="pwd"  size="20" style="ime-mode:disabled" onkeypress="action_enter(event);"/></dd>											
				<dd class="idfind5"><a href="#"  onclick="login()"><img src="/images/user/btn_login01.gif" alt="로그인" /></a></dd>
				<dt class="cenbtn">
					<a href="#" onclick="changeMenu(7, 1)"><img src="/images/user/btn_join.gif" alt="회원가입"/></a> 
					<a href="#" onclick="changeMenu(7, 2)"><img src="/images/user/btn_idpw.gif" alt="아이디/비밀번호 찾기"/></a>
				</dt>
			</dl>
			
                                    
		</form>
		</div>
	</div>
	<div id="tab_02" style="display:none;">
	<div class="aside_tab"> 
	<div class="off sub01"><a href="#" onclick="ShowBx('tab_01');HideBx('tab_02');return false;"><strong>일반 로그인</strong></a></div> 
	<div class="on sub02"><a href="#" onclick="ShowBx('tab_02');HideBx('tab_01');return false;"><strong>공인 인증서 로그인</strong></a></div>
	<div class="clear"></div>
	</div> 
		<div>
		<!-- <form name="loginCertForm" id="loginCertForm" method="post"  action="/usr/lgn/actionCrtfctLogin.action" onsubmit="return checkCertLogin2()"> -->
		<!-- <form name="loginCertForm" id="loginCertForm" method="post"  action="/EPKI/channel/kc_channelandlogin_result.jsp"> -->
		<form name="loginCertForm" id="loginCertForm" method="post"  action="/usr/lgn/actionCrtfctLoginHtml5.do">
		
			<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
				<input type="hidden" name="requestData"/>
				
				
	     		<dl class="idfind leftp">
				<dt class="cen"><img src="/images/user/login_tit.gif" alt="로그인을 하시면 다양한 서비스를 이용하실 수 있습니다."/></dt>
				<dt class="idfind3"><label for="userId2">아이디</label></dt>
	            <dd class="idfind4"><input type="text" name="userId" id="userId2" size="20" style="ime-mode:disabled" onkeypress="action_enter(event);" style=" height: 15px; padding: 2px; border: 1px solid #c9c9c9; font-size: 12px; font-family: '돋움'; color: #777;"/></dd>
	            <dd class="idfind5"><a href="#"><img src="/images/user/btn_login01.gif" alt="로그인" /></a></dd>
				<dt class="cenbtn">
					<a href="#" onclick="changeMenu(7, 1)"><img src="/images/user/btn_join.gif" alt="회원가입"/></a> 
					<a href="#" onclick="changeMenu(7, 2)"><img src="/images/user/btn_idpw.gif" alt="아이디/비밀번호 찾기"/></a>
				</dt>
				<div style="display: none">
					<textarea name="reqSecLoginData" ondblclick="textareaResize(this)"></textarea>
				</div>
			</dl>
			
		</form>
		</div>
	</div>
	</div>
</div>

<!-- 	<p style="width:570px;margin:10px auto;">"EPKI Client Toolkit의 Object선언이 올바르지 않습니다."의 메시지나 인증서 로그인하기 클릭시 아무런 반응이 없는 경우 (화면 진행이 안됨) -->
<!-- 	<a href="#none" onclick="fn_download('로그인_인증_오류_해결방법.hwp', '로그인_인증_오류_해결방법.hwp', 'down')"><strong style="color:red">로그인 인증 오류 해결</strong></a> 을 참고하시어 자바프로그램 삭제후 재 설치 하시기 바랍니다.</p> -->
	<div>
		<a href="http://www.epki.go.kr/sub/info.do?m=0501&s=epki" title="새창으로 사이트열기" target="blank" alt="EPKI고객센터"><img src="/images/user/faq001.gif" alt="EPKI고객센터"/></a>
		&nbsp;&nbsp;<a href="http://www.epki.go.kr/faq/list.do?m=0504&s=epki" title="새창으로 사이트열기" target="blank" alt="인증서 FAQ" ><img src="/images/user/faq002.gif" alt="인증서 FAQ"/></a>
		<!-- &nbsp;&nbsp;<a href="http://java.com/ko/" title="새창으로 사이트열기" target="blank" alt="java프로그램설치"><img src="/images/user/faq003.gif" alt="java프로그램설치"/></a>
		&nbsp;&nbsp;<a href="#none" onclick="fn_download('로그인_인증_오류_해결방법.hwp', '로그인_인증_오류_해결방법.hwp', 'down')"><img src="/images/user/faq004.gif" alt="로그인 인증 오류 해결"/></a> -->
	</div>

                  
                        
	
<!-- <script src="/EPKI/EPKICommon.js" type="text/javascript"></script> -->
<script type="text/javascript">

/* SetupObjECT(true);
InitObjECT(); */

$(document).ready(function(){
	$("#userId").focus();	
});

function action_enter(e)  {
    if (e.keyCode =="13"){
    	login();
        e.returnValue = false; 
        e.cancelBubble = true; 
    }
}

function login(){
	var theForm = eval('document.<c:out value="${gsMainForm}"/>');
	var strEnvelopedData;
	var strEnvelopedData1;
	var nError = 0;
	
	if(theForm.userId.value == "")
	{
		alert("아이디를 입력하세요");
		theForm.userId.focus();
		return false;
	}
	if(theForm.pwd.value == "")
	{
		alert("비밀번호를 입력하세요");
		theForm.pwd.focus();
		return false;
	}
	/*
	<c:if test="${!isDevelopmentMode}">
		var recipientCerts = new Array();
		recipientCerts[0] = "${serverCert}";

		strEnvelopedData = Envelop("SEED", recipientCerts, theForm.userId.value);
		strEnvelopedData1 = Envelop("SEED", recipientCerts, theForm.pwd.value);

		if(strEnvelopedData == "")
		{
			ECTErrorInfo();
			return;
		}
	    else
		{
			theForm.SelectedAlgID.value = "SEED";
			theForm.userId.value = strEnvelopedData;
			theForm.pwd.value = strEnvelopedData1;
		}
	</c:if>
	*/
	var c_url = this.location+"";
    var url = "https://iedu.nise.go.kr/usr/lgn/portalUserMainLogin.do";
    //var url = "https://test.nise.go.kr/usr/lgn/portalUserMainLogin.do";
    if(c_url.match("localhost")){
    	url = "/usr/lgn/portalUserMainLogin.do";
    }
	theForm.action = url;
	theForm.submit();
	$("#loading_login").show();
}

<%--
function checkCertLogin2() {

	//alert("준비중입니다.");
	//return;
	var frm = document.loginCertForm;
	if(frm.userId.value == "") {
		alert("아이디를  입력해주세요");
		frm.userId.focus();
		return;
	}

	<c:if test="${!isDevelopmentMode}">
		var strRequestData;
		var errorMessage = null;

		strRequestData = RequestSession("${serverCert}","SEED", "${sessionId}");

		if(strRequestData == ""){
			errorMessage = "EPKIWCtl 모듈에서 오류가 발생하였습니다.\n오류정보 : [" + document.ObjEWC.GetErrorNum() + "] - " + document.ObjEWC.GetErrorMsg();
		} else if(strRequestData == "100"){		//인증서 창 취소버튼 클릭시 .. return
			frm.id.focus();
			return;
		} else{
			frm.requestData.value = strRequestData;
		}

		if(errorMessage != null){
			alert(errorMessage);
			frm.id.focus();
			return;
		}
	</c:if>
	

	
	frm.submit();
	$("#loading_login").show();
}
--%>
</script>




<script type="text/javascript">

$(document).ready(function() {

	<c:if test="${!isDevelopmentMode}">
	
	    var sessionId = '${sessionId}';
	    var serverCert = '${serverCert}';
		var algorithm = "SEED";
		
	    epki.init(sessionId);
	    
	    
	    var frm = document.loginCertForm;
	    
	    epkiLogin = function() {
	    	
        	if(frm.userId.value == "") {
        		alert("아이디를  입력해주세요");
        		frm.userId.focus();
        		return false;
        	}
        	
        	/* generate secure channel and login message */
            var channelAndLoginTexts = $("#tab_02").find("textarea");
            /* Call Back Function Definition */
            var success = function(output) {
                channelAndLoginTexts.eq(0).val(output);
                $("#tab_02").find("form").submit();	
                $("#loading_login").show();
            };
            var error = function(errCode, errMsg) {
                alert(errCode + ": " + errMsg);
            };

            epki.reqSecChannelAndLogin(serverCert, algorithm, sessionId, success, error);
	    }

	   
        $("#loginCertForm .idfind5 img").eq(0).click(function() {		
        	epkiLogin();
        });
        
        $("#userId2").keydown(function (key) {
        	        
            if(key.keyCode == 13){//키가 13이면 실행 (엔터는 13)
            	if(frm.userId.value == "") {
            		alert("아이디를  입력해주세요");
            		frm.userId.focus();
            		return false;
            	}
            	epkiLogin();
            }
     
        });
        
    </c:if>
		
});
</script>




<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

