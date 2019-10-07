<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<!-- sci 인증 -->
<%@ include file = "/WEB-INF/jsp/egovframework/svt/cert/sci/sciInclude.jsp" %>
<!-- epki 인증 -->
<%@ include file = "/WEB-INF/jsp/egovframework/svt/cert/epki/epkiInclude.jsp" %>

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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
	<fieldset>
	<legend>회원가입여부 확인</legend>
	<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>	
	<input type="hidden" name="virtualNo" value="${virtualNo}"/>
	<input type="hidden" name="authInfo" value="${authInfo}"/>
	<input type="hidden" name="realName" value="${realName}"/>
	<input type="hidden" name="birthDate" value="${birthDate}"/>
	<input type="hidden" name="p_process"/>
	<input type="hidden" name="p_resno"/>

 			<div class="wrap_step">
				<ul>
					<li><img src="/images/user/img_step01_off.gif" alt="약관동의" /></li>
					<li><img src="/images/user/img_step02_on.gif" alt="현재단계 가입인증" /></li>
					<li><img src="/images/user/img_step03_off.gif" alt="기본정보입력" /></li>
					<li><img src="/images/user/img_step04_off.gif" alt="가입완료" /></li>
				</ul>
			</div><!--
            
            <div class="sub_text_2 m30">
			     <h4>실명인증</h4>                
			 </div>
			
			<p class="personal_box">
				본 사이트는 NICE신용평가정보(주)의 명의도용방지서비스 협약사이트 입니다.<br/>
				개정 "주민등록법"에 의해 타인의 주민등록번호를 부정사용하는 자는 3년 이하의 징역 또는 1천 만원 이하의 벌금이 부과될 수 있습니다. 
				- <span class="personal_blue1">관련법률</span> : 주민등록법 제37조(벌칙) 제10호(시행일 : 2009.04.01) 
				만약, 타인의 주민번호를 도용하신분들은 지금 즉시 명의 도용을 중단하시길 바랍니다<br/>
				※ <span class="personal_blue1">14세 미만</span>은 회원 가입을 할 수 없습니다.
			</p>
			
			<ul class="btnC m15">
			            <li><a href="#none" onclick="javascript:frmSubmit(1);"><img src="/images/user/btn_sil.gif" alt="실명인증" /></a></li>
			</ul>   
			     -->
			     
			 <div class="sub_text_2 m30">
			     <h4>안심본인 인증</h4>                
			 </div>
			
			<p class="personal_box">
				개정 "주민등록법"에 의해 타인의 주민등록번호를 부정사용하는 자는 3년 이하의 징역 또는 1천 만원 이하의 벌금이 부과될 수 있습니다. 
				- <span class="personal_blue1">관련법률</span> : 주민등록법 제37조(벌칙) 제10호(시행일 : 2009.04.01) 
				만약, 타인의 주민번호를 도용하신분들은 지금 즉시 명의 도용을 중단하시길 바랍니다<br/>
				※ <span class="personal_blue1">14세 미만</span>은 회원 가입을 할 수 없습니다.
			</p>
			
			<ul class="btnC m15">
			            <li><a href="javascript:reqSci('join');" title="새창"><img src="/images/user/btn_sil.gif" alt="안심본인인증" /></a></li>
			</ul>     
			     
			
			 <div class="sub_text_2 m30">
			     <h4>아이핀 (I-PIN)인증</h4>                
			 </div>
			
			<p class="personal_box">
				<span class="personal_blue1"><strong>아이핀이란?</strong></span><br />
				회원가입 시 개인 정보 보호를 위해 주민등록번호를 사용하지 않고도 본인임을 확인할 수 있는 안전한 본인인증 수단입니다.<br/>
				아이핀 ID가 없으신 사용자께서는 아이핀 인증버튼을 선택 후 아이핀 인증 화면에서 아이핀을 신규발급 받으시기 바랍니다.<br/>
			 	(아이핀에 한번 가입하시면 아이핀인증을 지원하는 모든 홈페이지에서 회원가입 시 아이핀을 이용하실 수 있습니다.) <br/>
				NICE평가정보(주) : <a href="http://www.niceipin.co.kr" target="_blank" title="새창"><span class="personal_blue1">http://www.g-pin.go.kr</span></a> 
				("개인정보 보험법 시행령 제20조"에 의거) 공인인증서, 신용카드, 휴대폰등으로 본인임을 확인하여 별도의 비용이 없습니다.
			</p>
			
			<ul class="btnC m15">
			            <li><a href="#none" onclick="javascript:certGpin();"  title="새창"><img src="/images/user/btn_ipins.gif" alt="아이핀인증" /></a></li>
			</ul> 
			 <div class="sub_text_2 m30">
			     <h4>EPKI 인증</h4>                
			 </div>
			
			<p class="personal_box">
				<span class="personal_blue1"><strong>EPKI란?</strong></span><br />
				교육부 소속 기관(공공 기관 포함) 및 학교, 공무원, 교원, 일반직, 기간제 교사 등에 대한 본인임을 확인할 수 있는 교육부<br/> 
				전자서명인증 수단입니다.<br/>
				(교육부 전자서명인증센터: <a href="http://www.epki.go.kr" target="_blank" title="새창"><span class="personal_blue1">http;//www.epki.go.kr</span></a>)				
			</p>
			
			<ul class="btnC m15">
			            <li><a href="#none" name="run" onclick="javascript:RequestSessionData();" title="새창"><img src="/images/user/btn_epki.gif" alt="EPKI인증" /></a></li>
			</ul> 
            <!-- 이용약관 -->
            <!--<div class="sub_txt_1depth m30 mrb10">
                <h4>가입여부확인</h4> 
                	<ul>
                    	<li>회원가입을 위해 <strong>이름</strong>과 <strong>주민등록번호</strong>를 정확히 입력해주시기 바랍니다.</li>
                        <li>입력하신 소중한 <strong>개인정보</strong>는 회원님의 명백한 <strong>동의없이</strong> 공개 또는 제3자에게 제공되지 않습니다.</li>    
                        <li>주민번호앞 6자리를 입력하시면 다음 칸으로 자동으로 이동합니다.</li>                                                 
                    </ul>                
            </div>
            <div class="mstab">
            	<dl class="idfind leftp">
				<dt class="cen"></dt>
				<dt class="idfind1"><label for="nameput">이름</label></dt>
                <dd class="idfind2"><input type="text" name="p_name" value="${realName}" id="nameput" size="38"  maxlength="20" style="ime-mode:active;" readonly="readonly"/></dd>
				<dt class="idfind3"><label for="p_resno1">주민번호</label></dt>
                <dd class="idfind7"><input type="text" id="p_resno1" name="p_resno1" value="${jumin1}"  size="6" maxlength="6" style="ime-mode:disabled;" title="주민번호앞 6자리"/>-<label for="p_resno2" class="blind">주민번호 뒤7자리</label><input type="password" name="p_resno2" value="${jumin2}" id="p_resno2"  size="14"  maxlength="7" onkeypress="action_enter(event);" style="ime-mode:disabled;" title="주민번호 뒤7자리"/>
                </dd>											
				<dd class="idfind6">
					<a href="#" onclick="frmSubmit();"><img src="/images/user/btn_jumin.gif" alt="확인" /></a>
				</dd>										
				</dl>
            </div>-->
            
	</fieldset> 	          
</form>
			  
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
function frmSubmit1( ) 
{
	//thisForm.action = "/usr/mem/memJoinStep03.do";
	//thisForm.submit();	
	count=0;
	var flag = true;
    if ( !thisForm.p_name.value ) {
        flag = false;
    	alert("이름을 입력하세요.");
    	thisForm.p_name.focus();
    	return;
    } else if ( thisForm.p_name.value.search(' ') > 0) {
    	alert("성과 이름 사이 혹은 입력창에 공백이 들어가지 않도록 해주세요");
    	return;
    } else if(thisForm.p_name.value.length > 0 ){ 
    	 for (i=0;i<thisForm.p_name.value.length;i++){
			var nmvalue = thisForm.p_name.value.charAt(i);
    		if(nmvalue.search(/[a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]/) == -1 ) {
    			count++;
    		} 
    		if(count != 0 ) {
    			alert("영문,한글만 입력가능합니다.");
    			thisForm.p_name.focus();
    			return;
    		}
    	}
    
    }

	if(thisForm.p_resno1.value.search(/\S/) < 0 || thisForm.p_resno2.value.search(/\S/) < 0 ){
        alert( "주민등록번호를 입력하세요.");
        thisForm.p_resno1.focus();
        return;
    }

	thisForm.p_resno.value = thisForm.p_resno1.value + thisForm.p_resno2.value;
    if(!validSsn(thisForm.p_resno.value)){
        alert("주민등록번호 양식에 어긋납니다.");
        return;
    }

    if(thisForm.virtualNo.value == ""){
        alert("아이핀 인증이 이루어지지 않았습니다. 아이핀 인증을 다시 해주세요.");
        return;
    }
    
    if ( flag ) {
    	//thisForm.action = "https://iedu.nise.go.kr/usr/mem/memJoinStep03.do"; //우편번호 찾기 안됨
    	thisForm.action = "/usr/mem/memJoinStep03.do";
	    thisForm.target	= "_self";
		thisForm.submit();
    }
}

function frmSubmit(gubun) {
	//아이핀 적용시...
   	 wWidth = 650;
	 wHight = 550;
	    
	 url = "/I-PIN/IPin_checkplus_main.jsp";
	 winname = "iPinLoginWin";

	 if(gubun == 2)		//g-ping
	 {
		  url = "/I-PIN/IPin_checkplus_main.jsp";
		  winname = "iPinLoginWin";

		  wWidth = 360;
		  wHight = 120;
	 }

	 wX = (window.screen.width - wWidth) / 2;
	 wY = (window.screen.height - wHight) / 2;
	
	 var w = window.open(url, winname, "directories=no,toolbar=no,left="+wX+",top="+wY+",width="+wWidth+",height="+wHight);
   
    //일반적용시
   /*
 
	    thisForm.action = "/usr/mem/memJoinStep03.do";
	    thisForm.target	= "_self";
		thisForm.submit();
	*/
}

function certGpin() {
	var url = "${pageContext.request.contextPath}/I-PIN/IPin_checkplus_main.jsp";
	var winname = "iPinLoginWin";
	
	var wWidth = 360;
	var wHight = 120;
	
	var wX = (window.screen.width - wWidth) / 2;
	var wY = (window.screen.height - wHight) / 2;
	
	var w = window.open(url, winname, "directories=no,toolbar=no,left="+wX+",top="+wY+",width="+wWidth+",height="+wHight);
}



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

function action_enter(e)  {
    if (e.keyCode =='13'){
    	frmSubmit(); 
        e.returnValue = false; 
        e.cancelBubble = true; 
    }
}

function body_onLoad() {
	//thisForm.p_name.focus();
}

if(window.addEventListener) {
    window.addEventListener("load", body_onLoad, false);
} else if(window.attachEvent) {
    window.attachEvent("onload", body_onLoad);
}
document.title="회원가입(가입여부확인) :회원가입";
//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->