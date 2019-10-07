<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<fieldset>
<legend>회원가입약관동의</legend>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
<input name="developMode" type="hidden" value=""/>
	
	
			<div class="wrap_step">
				<ul>
					<li><img src="/images/user/img_step01_on.gif" alt="현재단계 약관동의" /></li>
					<li><img src="/images/user/img_step02_off.gif" alt="가입인증" /></li>
					<li><img src="/images/user/img_step03_off.gif" alt="기본정보입력" /></li>
					<li><img src="/images/user/img_step04_off.gif" alt="가입완료" /></li>
				</ul>
			</div>
            
            <!-- 이용약관 -->
            <div class="sub_text_2 m30">
                <h4>이용약관</h4>                
            </div>
            <label for="p_contnet2" class="blind">이용약관</label>
            <textarea cols="" rows="" name="p_contnet1" id="p_contnet2" class="ta"  readonly="readonly" title="이용약관" tabindex="0">
제 1 장 총 칙
 
제1조 목 적
이 약관은 국립특수교육원(이하 교육원이라고 함)부설 원격교육연수원(http://iedu.nise.go.kr)의 이용조건 및 절차에 관한 사항과 기타 필요한 사항을 규정함을 목적으로 한다.
 
제2조 약관의 효력과 변경
(1) 약관은 이용자에게 공시함으로서 효력을 발생한다.
(2) 교육원은 사정 변경의 경우와 영업상 중요 사유가 있을 때 약관을 변경할 수 있으며, 변경된 약관은 (1)항과 같은 방법으로 효력을 발생한다.
 
제3조 약관 외 준칙
본 약관에 명시되지 않은 사항은 사회에서 인정되는 범위 안에서 상호 협의 조정토록 노력하며 불가시 관계법령에 따라 처리하고 이 약관이 관계 법령과 배치되는 부분은 관계법령을 우선한다.
 
 
제 2 장 회원 가입과 서비스 이용
 
제1조 이용 계약의 성립
(1) 이용계약은 가입 희망자의 가입신청에 대한 교육원의 이용 승낙과 가입희망자의 약관  내용에 대한 동의로 성립된다.
(2) 회원에 가입하여 서비스를 이용하고자 하는 희망자는 교육원에서 요청하는 개인신상정보를 제공해야 한다.
(3) 교육원이 이용 신청을 승낙한 때에는 다음 각 호의 사항을 이용자에게 통지한다.
    ① 이용자 ID
    ② 기타 교육원이 필요하다고 인정하는 사항
 
제2조 가입 승낙의 제한
교육원은 다음 각 호에 해당하는 이용계약신청에 대하여는 이를 승낙하지 아니한다.
    ① 타인의 명의를 사용하여 신청하였을 때
    ② 이용 계약 신청서의 내용을 허위 기재하였거나 허위서류를 첨부하여 신청하였을 때
    ③ 사회의 안녕 질서 혹은 미풍양속을 저해할 목적으로 신청하였을 때
 
제3조 서비스 이용
(1) 서비스 이용은 교육원의 업무상 또는 기술상 특별한 지장이 없는 한 연중무휴,1일 24시간을 원칙으로 한다.
(2) 제1항의 이용시간은 정기점검 등의 필요로 인하여 교육원이 정한 날 또는 시간은 그러하지 아니한다.
 
 
제 3 장 책 임
 
제1조 교육원의 의무
(1) 교육원은 본 약관 및 관계법령에 따라 본 서비스를 실시하여야 한다.
(2) 교육원은 특별한 사정이 없는 한 이용자가 신청한 서비스 제공 개시일에 서비스를 이용할 수 있도록 한다.
 
(3)교육원은 약관에서 정한 바에 따라 지속적, 안정적으로 서비스를 제공할 의무가 있다.
교육원은 이용자의 개인 신상 정보를 본인의 승낙 없이 타인에게 누설, 배포하여서는 아니 된다. 다만, 전기통신관련법령 등 관계법령에 의하여 관계 국가기관 등의 요구가 있는 경우에는 그러하지 아니한다.
(4)본 서비스는 1년 365일, 1일 24시간 운영됨을 원칙으로 하나 서비스 개선을 위한 개정 및 하드웨어의 교체 등을 위해 일시 중지 될 수 있으며 이 경우 해당 홈페이지에 서비스의 일시 중지됨을 교육원은 사전에 회원에게 통지하여야 한다. 단, 천재지변이나 불의의 사고 시에는 제외되나 사후 이유를 회원에게 통지하여야 한다.
 
제2조 회원의 의무
(1) 아이디와 비밀 번호에 관한 모든 관리 및 이용은 각 회원의 책임이다.
(2) 회원 아이디 및 비밀번호에 의하여 발생되는 사용상의 과실 또는 제3자에 의한 부정사용 등에 대한 모든 책임은 회원에게 있다.
 
(3) 회원 아이디 등의 부득이한 갱신 및 수정/말소에 관하여는 교육원과 협의를 통하여 처리한다.
(4) 교육원 서비스의 모든 게시물은 실명제로 운영이 되며, 게시판을 통해서 인터넷 주소가 타인에게 공개될 수 있다.
 
제 4 장 계약 해지 및 서비스 이용 제한
 
제1조 계약 해지 및 이용제한
이용자가 이용 계약을 해지하고자 할 때에는 이용자 본인이 직접 온라인을 통해 이용안내/ 탈퇴 메뉴를 선택해 해지 신청을 하여야 한다.
 
교육원은 이용자가 다음 사항에 해당하는 행위를 하였을 경우 사전 통지 없이 이용 계약을 해지 하거나 또는 기간을 정하여 서비스 이용을 중지할 수 있다.
 
(1) 공공 질서 및 미풍 양속에 반하는 경우
(2) 범죄적 행위에 관련되는 경우
(3) 이용자가 국익 또는 사회적 공익을 저해할 목적으로 서비스 이용을 계획 또는 실행 할 경우
(4) 타인의 서비스 아이디 및 비밀 번호를 도용한 경우
(5) 타인의 명예를 손상시키거나 불이익을 주는 경우
(6) 같은 사용자가 다른 아이디로 이중 등록을 한 경우
(7) 서비스에 위해를 가하는 등 서비스의 건전한 이용을 저해하는 경우
(8) 기타 관련법령이나 교육원이 정한 이용조건에 위배되는 경우
 
제2조 이용자의 게시물
교육원은 이용자가 게시하거나 등록하는 서비스 내의 내용물이 다음 각 사항에 해당된다고 판단 되는 경우에 사전 통지 없이 삭제할 수 있습니다.
 
(1) 다른 이용자 또는 제 3자를 비방하거나 중상 모략으로 명예를 손상시키는 내용인 경우
(2) 공공질서 및 미풍양속에 위반되는 내용인 경우
(3) 범죄적 행위에 결부된다고 인정되는 내용일 경우
(4) 제 3자의 저작권 등 기타 권리를 침해하는 내용인 경우
(5) 기타 관계 법령이나 교육원에서 정한 규정에 위배되는 경우
 
제 5 장  기   타
 
제1조 정보의 제공
(1) 교육원은 이용자가 서비스 이용 중 필요가 있다고 인정되는 다양한 정보를 전자우편이나 서신우편 등의 방법을 통해 이용자에게 제공할 수 있다.
 
부칙
이 약관은 2004년 7월 5일부터 시행한다. 
</textarea>
<p class="m10"><input name="p_agreement1" id="p_agreement1" type="checkbox" value="Y" class="input_border vrM" /> <label for="p_agreement1">회원약관에 동의 합니다.</label></p>
           <!-- 개인정보 보호정책  -->
            <div class="sub_text_2 m30">
                <h4>개인정보 수집 및 이용에 대한 안내</h4>                
            </div>
<div class="textarea_test"  tabindex="0">
	<pre>
[수집하는 개인정보의 항목]
첫째, 국립특수교육원 부설원격교육연수원(이하 ‘원격교육연수원’ )은 회원가입, 연수상담, 연수생 편의 서비스를 제공을 위해 최초 
회원가입 당시 아래와 같은 최소한의 개인정보를 수집하고 있습니다.
- 성명, 아이디, 회원구분, 나이스개인번호(또는 생년월일), 전자우편, 학교명, 주소 및 연락처(직장 또는 자택)
- 실명확인 회원가입 : 가상주민등록번호 확인(아이핀, 공인인증서 등)


둘째, 서비스 이용과정이나 사업처리 과정에서 아래와 같은 정보들이 자동으로 생성되어 수집될 수 있습니다.
- IP Address, 쿠키, 방문 일시, 서비스 이용 기록, 불량 이용 기록

[개인정보 수집방법]
원격교육연수원은 다음과 같은 방법으로 개인정보를 수집합니다.
- 홈페이지, 서면양식, 팩스, 전화, 상담 게시판, 전자우편 등

[개인정보의 수집 및 이용목적]
가. 연수서비스 제공에 관한 계약 이행 및 서비스 제공에 따른  컨텐츠 제공, 출석평가, 이수증 발급 등 

나. 회원관리
회원제 서비스 이용 및 제한적 본인 확인제에 따른 본인확인, 개인식별, 가입의사 확인, 가입 및 가입횟수 제한, 추후 법정 
대리인 본인확인, 분쟁 조정을 위한 기록보존, 불만처리 등 민원처리, 고지사항 전달, 회원탈퇴 의사의 확인

다. 신규 서비스 개발 및 연수만족도 조사의 활용
신규 서비스 개발 및 통계학적 특성에 따른 서비스 제공 및 서비스의 유효성 확인, 접속빈도 파악, 회원의 서비스이용에 대한 통계

[개인정보의 공유 및 제공] 
원격교육연수원은 이용자들의 개인정보를 "2. 개인정보의 수집목적 및 이용목적"에서 고지한 범위 내에서 사용하며, 이용자의 사전 
동의 없이는 동 범위를 초과하여 이용하거나 원칙적으로 이용자의 개인정보를 외부에 공개하지 않습니다. 
다만, 아래의 경우에는 예외로 합니다.
- 이용자들이 사전에 공개에 동의한 경우
- 법령의 규정에 의거하거나, 수사 목적으로 법령에 정해진 절차와 방법에 따라 수사기관의 요구가 있는 경우

[개인정보의 취급위탁]
원격교육연수원은 서비스 향상을 위해서 아래와 같이 개인정보를 위탁하고 있으며, 관계 법령에 따라 위탁계약 시 개인정보가 
안전하게 관리될 수 있도록 필요한 사항을 규정하고 있습니다.

회사의 개인정보 위탁처리 기관 및 위탁업무 내용은 아래와 같습니다.
	</pre>
 	<table width="100%" summary="위탁업체, 위탁업무 내용, 개인정보의 보유 및 이용기간으로 구성" cellspacing="0" class="personal_t">
      <caption>위탁업체</caption>
      <colgroup>
      <col width="30%" />
      <col width="70%" />
      </colgroup>
      <tbody>
        <tr>
          <td scope="row" style="background-color:#f4fafc;border-left:0px;">㈜이패스코리아</td>
          <td>원격연수시스템 유지보수</td>
        </tr>        
      </tbody>
    </table>
	<pre>			
[개인정보의 보유 및 이용기간] 
가. 연수자의 개인정보는 원칙적으로 개인정보의 수집 및 이용목적이 달성되면 지체 없이 파기합니다. 

나. 관련법령에 의한 정보보유 사유
상법, 전자상거래 등에서의 소비자보호에 관한 법률 등 관계법령의 규정에 의하여 보존할 필요가 있는 경우 원격교육연수원은 
관계법령에서 정한 일정한 기간 동안 회원정보를 보관합니다. 이 경우 원격교육연수원은 보관하는 정보를 그 보관의 목적으로만 
이용하며 보존기간은 아래와 같습니다.

- 연수생 나이스개인번호(생년월일) 및 연수 성적 등에 관한 기록 
보존 이유 : 교육공무원 승진규정(제32조), 교원 등의 연수에 관한 규정 
보존 기간 : 준영구 

- 본인확인에 관한 기록 
보존 이유 : 정보통신망 이용촉진 및 정보보호 등에 관한 법률 
보존 기간 : 6개월

- 웹사이트 방문기록 
보존 이유 : 통신비밀보호법 
보존 기간 : 3개월 
	</pre>
</div>




<br/>

<span class="m20"><input name="p_agreement2" id="p_agreement2" type="checkbox" value="Y" class="input_border vrM" /> <label for="p_agreement2">개인정보 수집 및 이용에 동의합니다.</label></span>
<br />


<!-- 선택적 개인정보 수집 및 이용 동의  -->
 <div class="sub_text_2 m30">
     <h4>선택적 개인정보 수집 및 이용 동의</h4>                
 </div>


<div class="textarea_test" style="height:80px;">
	<pre>

개인정보보호법 제 22조 3항에 의해 전화번호 등 선택정보 사항을 획득하지 못한 사유로 인해 
서비스 제공을 거부할 수 없습니다.
	</pre>
</div>
<div class="m10"><input name="p_agreement4" id="p_agreement4" type="checkbox" value="Y" class="input_border vrM" /> <label for="p_agreement4">선택적 개인정보 수집 및 이용에 동의합니다.</label></div>   
<br/>

<!-- 개인정보 제3자 제공 동의  -->
 <div class="sub_text_2 m30">
     <h4>개인정보 제3자 제공 동의</h4>                
 </div>

<div class="textarea_test" style="height:140px;">
	<pre>
연수자의 편의를 위해 국립특수교육원 부설원격교육연수원(이하 ‘원격교육연수원’ )은 연수를 운영하고 있습니다. 이를 위해 원격교육연수원이 수집한 개인정보는 아래의 내용으로 제공하게 됩니다.

○ 개인정보를 제공받는 자 : 시도교육청, 한국교육학술정보원
○ 제공하는 개인정보 항목 : 이름, 나이스 개인번호, 연수지명번호, 생년월일, 직장정보, 연수성적, 이수번호
○ 정보의 이용목적: 원격연수 연수이력
○ 정보의 보유 및 이용기간 : 연수종료시
○ 정보주체는 개인정보 제3자 제공 동의 사항에 대해 거부할 수 있으며, 미 동의로 인해 이용 상의 불이익이 발생하지 않습니다.
	</pre>
</div>
<div class="m10"><input name="p_agreement5" id="p_agreement5" type="checkbox" value="Y" class="input_border vrM" /> <label for="p_agreement5">제3자 제공 사항에 동의합니다.</label></div>   
<br/>

<!-- 개인정보 제3자 제공 동의  -->
 <div class="sub_text_2 m30">
     <h4>개인정보 제3자 제공 동의</h4>                
 </div>

<div class="textarea_test" style="height:140px;">
	<pre>
연수자의 편의를 위해 국립특수교육원 부설원격교육연수원(이하 ‘원격교육연수원’ )은 연수를 운영하고 있습니다. 이를 위해 원격교육연수원이 수집한 개인정보는 아래의 내용으로 제공하게 됩니다.

○ 개인정보를 제공받는 자 : 국가평생교육진흥원
○ 제공하는 개인정보 항목 : 이름, 생년월일, 직장정보, 연수성적, 이수번호
○ 정보의 이용목적: 평생교육 학습계좌 등록
○ 정보의 보유 및 이용기간 : 연수종료시
○ 정보주체는 개인정보 제3자 제공 동의 사항에 대해 거부할 수 있으며, 미 동의로 인해 이용 상의 불이익이 발생하지 않습니다.
	</pre>
</div>
<div class="m10"><input name="p_agreement6" id="p_agreement6" type="checkbox" value="Y" class="input_border vrM" /> <label for="p_agreement6">제3자 제공 사항에 동의합니다.</label></div>   
<br/>


<div class="m10" style="text-align: center;">
	<input id="allAgree" type="checkbox" class="input_border vrM"> <label for="allAgree"><span style="font-size: 15px;font-weight: bold;">전체 동의합니다.</span></label>
</div>
<br/>

<ul class="btnC m15">
	<li>
		<a href="#" class="pop_btn01" onclick="frmSubmit('1')" title="회원가입약관동의함" style="cursor:hand;"><span>동의함</span></a>
		<a href="#" class="pop_btn01" onclick="frmSubmit('2')" title="회원가입약관동의안함" style="cursor:hand;"><span>동의안함</span></a>
	</li>
</ul>

</fieldset>          
</form>
<script type="text/javascript">
	<!--
	//윈도 이름 셋팅
	window.name = 'openername';
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
	//약관동의
	function frmSubmit(gubun) {
	    var flag = true;
	    if ( !thisForm.p_agreement1.checked ) {
	        alert('회원 이용약관에 동의하셔야 가입이 가능합니다.');
	        thisForm.p_agreement1.focus();
	        flag = false;
	    } 
	    
	    else if ( !thisForm.p_agreement2.checked ) {
	        alert('개인정보 수집 및 이용에 동의하셔야 가입이 가능합니다.');
	        thisForm.p_agreement2.focus();
	        flag = false;
	    }
	    
	    else if ( !thisForm.p_agreement4.checked ) {
	       alert('선택적 개인정보 수집 및 이용에 동의하셔야 가입이 가능합니다.');
	        thisForm.p_agreement4.focus();
	       flag = false;
	    }
	    
	    else if ( !thisForm.p_agreement5.checked ) {
		       alert('제3자 제공 사항에 동의하셔야 가입이 가능합니다.');
		        thisForm.p_agreement5.focus();
		       flag = false;
		 }
	    
	    else if ( !thisForm.p_agreement6.checked ) {
		       alert('제3자 제공 사항에 동의하셔야 가입이 가능합니다.');
		        thisForm.p_agreement6.focus();
		       flag = false;
		 }

	    
	    if(flag){
		    if(gubun == '1'){
		    	thisForm.developMode.value = "Y";
			    thisForm.action = "/usr/mem/memJoinStep02.do";
			    //thisForm.action = "/usr/mem/memJoinStepHtml502.do";			    
			    thisForm.target	= "_self";
				thisForm.submit();
		    }else{
			    location.href = "/usr/hom/portalActionMainPage.do";
		    }
	    }
	
	}
	document.title="회원가입(이용약관동의&본인인증) :회원가입";
//-->
$(document).ready(function() {
	$('#allAgree').click(function() {
		if($(this).is(':checked'))
			$('input[type=checkbox]').prop('checked', true);
		else
			$('input[type=checkbox]').prop('checked', false);
	});
});
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->