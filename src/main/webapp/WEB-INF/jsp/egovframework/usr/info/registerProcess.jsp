<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>


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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
  <fieldset>
    <legend>연수 수강 절차</legend>
    <%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
    <div class="sub_txt_1depth">
      <h4>연수 수강 절차</h4>
      	<ul>
      		<li>
      		회원구분 : &nbsp;&nbsp;&nbsp;<input name="empgubun" id="empgubun1" type="radio" value="1" checked onchange="empChange(this.value);" />
      		<label for="empgubun1">교원</label> 
      		&nbsp;&nbsp;<input name="empgubun" id="empgubun2" type="radio" value="2"  onchange="empChange(this.value);"/>
      		<label for="empgubun2">교육 전문직</label> 
      		&nbsp;&nbsp;<input name="empgubun" id="empgubun3" type="radio" value="3"  onchange="empChange(this.value);"/>
      		<label for="empgubun3">보조인력</label>  
      		&nbsp;&nbsp;<input name="empgubun" id="empgubun4" type="radio" value="4"  onchange="empChange(this.value);"/>
      		<label for="empgubun4">일반회원</label> 
      		&nbsp;&nbsp;<input name="empgubun" id="empgubun5" type="radio" value="4"  onchange="empChange(this.value);"/>
      		<label for="empgubun5">공무원</label>
      		</li>
      	</ul>
      <div id="gubun1" style="display:none;">
       	<ul>
	  		<p><img src="/images/user/h4_02_step1.gif" alt="연수 수강 절차 > 회원가입>로그인 > 지역선택 > 수강신청 > 지명번호입력 나이스개인번호 입력 > 연수 승인 > 연수 수강" /></p>
<!--	  		<li>교원(교원회원으로 가입)<br />-->
<!--            -회원가입(교원회원) ➜ 수강신청 ➜ 연수지명번호 및 나이스 개인번호입력 → 수강 승인(결제 확인) → 연수   수강</li>-->
	  	</ul>
      </div>
      <div id="gubun2" style="display:none;" >
       	<ul>
	  		<p><img src="/images/user/h4_02_step.gif" alt="연수 수강 절차 > 회원가입>로그인 > 지역선택 > 수강신청 > 지명번호입력 > 연수 승인 > 연수 수강" /></p>
	  	</ul>
      </div>
      <div id="gubun3" style="display:none;" > 
       	<ul>
	  		<p><img src="/images/user/h4_02_step2.gif" alt="연수 수강 절차 > 회원가입>로그인 > 지역선택 > 수강신청 > 연수 승인 > 연수 수강" /></p>
	  	</ul>
      </div>
      <div id="gubun4" style="display:none;" >
       	<ul>
	  		<p><img src="/images/user/h4_02_step2.gif" alt="연수 수강 절차 > 회원가입>로그인 > 지역선택 > 수강신청 > 연수 승인 > 연수 수강" /></p>
	  	</ul>
      </div>
<!-- 	   <h4>(특별과정) 수강 절차&nbsp;&nbsp;<strong>※특별과정은 시·도교육청에서 요청한 연수대상자만 수강 가능함</strong></h4>
       <ul>
        <li>해당 시·도 교원 및 교육전문직<br />
            - 회원가입(교원회원) ➜ 수강신청 ➜ 연수지명번호입력 ➜ <strong>수강승인(시·도교육청 명단 확인)</strong> ➜ 연수수강 </li>
        <li>해당 시·도 보조인력, 학부모 등<br />
		    - 회원가입(일반회원) ➜ 수강신청 ➜ <strong>수강승인(시·도교육청 명단 확인)</strong> ➜ 연수수강 </li>
       </ul> -->
    </div>
    
	
	<div class="sub_txt_1depth m30">
      <h4>수강신청</h4>
      <ul>      
      	<li>홈페이지 상단의 [연수신청 - 개설교육과정 검색/신청] 클릭</li>
      	<li>수강하고자 하는 과정명 클릭하여 신청하기</li>
      	<li>중복수강 관련 지침<br />
             ● (동일과정 재이수 불가) 본 연수원 이수과정은 2년 이내 재신청 불가<br />
		     <!-- ● (동일․유사과정 중복) 2강좌 중 1강좌 인정 다만, 해당 연수생이 동일‧유사과정이 아님을 증명할 경우 2강좌 모두 인정<br />
		     	※ 2개 강과명이 동일‧유사하여도 해당 연수과정표 등을 통해 과목의 70%이상 다른 과정임을 증명할 경우 2강좌 모두 인정<br/> -->
		     ● (연수기간 중복 허용) 같은 기간 동안 2개 이상 연수과정을 이수하여도 전부 인정<br/>
				  <!-- - 같은 기간 중 다양한 원격연수과정 수강 가능<br/> -->
				  - 단 물리적으로 불가능한 경우 허용 불가<br/>
				 ※ 같은 기간 동안에 서로 다른 집합연수를 2개 이상 이수하는 등 물리적으로 불가능한 경우에는 허용되지 아니함.<br/>
				 (사례 : 대학원 연수 파견 기간 중 교감 자격연수 이수, 80% 이상 출석하지 못할 중복된 집합연수 등)<br/>
				 <!-- ※ 기존과 달리 연수기간에 관계없이 이수한 모든 과정을 허용<br/> -->
				 ※「2019년도 교원 연수 중점 추진 방향」(’18. 10. 교육부)에 근거
		</li>
	  </ul>
<!--	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;※ 수강제한 : 본 연수원에서 이수한 과정을 2년 이내 다시 수강 신청 할 수 없음-->
    </div>
	<div class="sub_txt_1depth m30">
      <h4>연수지명번호</h4>
      <ul>      
      	<li>연수지명번호는 각 학교의 연수담당자에게 문의하여 입력</li>
      	<li>패키지 특별과정(30시간+30시간) 연수는 연수지명번호를 각각 다르게 2개 입력</li>
	  </ul>
    </div>
    <div class="sub_txt_1depth m30">
      <h4>NEIS(나이스) 개인번호</h4>
      <ul>      
      	<li>교원이신 경우, 반드시 NEIS(나이스) 개인번호를 입력해야 원격교육 직무연수 이수내역이 시.도 교육청  NEIS(나이스)에 등재 활용됨</li>
      	<li>NEIS(나이스) 개인번호는 교육행정정보시스템 사이트( www.neis.go.kr)에서 조회하여 입력</li>      	
	  </ul>
    </div>
	<div class="sub_txt_1depth m30">   
    
    <div class="sub_txt_1depth m20">
      <h4>연수 수강</h4>
      <ul>
      	<li>홈페이지 상단의 [나의강의실 - 진행중인 과정 -  학습하기 - 나의 강의실 - 학습시작] 클릭</li>
      	<li>이수(수료)기준 및 이수(수료)증 발급</li>
        <li class="frist">이수(수료)기준: 총점이 60점 이상이고 진도율이 90% 이상 일 때<br />	
          
          <div class="courseList">
            <table summary="연수 시간에  따른 진도율, 온라인과제 점수, 온라인평가, 출석평가, 계로 구성됨" cellspacing="0" width="100%">
               	<caption>연수 이수(수료)기준</caption>
                   <colgroup>
                       <col width="33%" />
                       <col width="20%" />
                       <col width="20%" />                                
                       <col width="20%" />                                
                       <col width="20%" />                                
                   </colgroup>
                   <thead>
                       <tr>
                           <th scope="col">평가 방법</th>
                           <th scope="col">61시간 연수</th>
                           <th scope="col">30시간/45시간 연수</th>
                           <th scope="col">15시간/20시간연수</th>
                           <th scope="col" class="last">학부모 연수</th>                                    
                       </tr>
                   </thead>
                   <tbody>					
                       <tr>
                           <td style="text-align:left">진도율</td>
                           <td>90%이상</td>
                           <td>90%이상</td>
                           <td>90%이상</td>
                           <td class="last">90%이상</td>                                    
                       </tr>
                       <tr>
                           <td style="text-align:left">온라인과제</td>
                           <td>20점</td>
                           <td>-</td>
                           <td>-</td>
                           <td class="last">-</td>                                    
                       </tr> 
                       <tr>
                           <td style="text-align:left">온라인평가</td>						
                           <td>20점</td>
                           <td>100점</td>
                           <td>100점</td>
                           <td class="last">-</td>                                    
                       </tr> 
                       <tr>
                           <td style="text-align:left">출석평가</td>						
                           <td>60점</td>
                           <td>-</td>
                           <td>-</td>
                           <td class="last">-</td>                                    
                       </tr>                       
                       <tr>
                       	   <td>계</td>
                       	   <td>100점</td>
                       	   <td>100점</td>
                       	   <td>100점</td>
                       	   <td class="last">수료</td>
                       </tr>                                                	
                   </tbody>
               </table>
          </div>
		<li class="frist">이수(수료)증 발급: 모든 교육연수이수증은 연수 과정 종료 후, 원격교육연수원 웹사이트에서 개별 출력함</li>
       </ul>
    </div>
    
    
    
    <!-- <div class="sub_txt_1depth m20">
      <h4>연수연기, 출석고사연기</h4>
      <ul>      	
        <li class="frist">
        - 출석고사연기는 연수시작후 7일 이후부터 출석고사전까지 해당 특별사유에 한하여 당해연도 동일연수로 1회 연기할 수 있음. 
          &nbsp;&nbsp;&nbsp;(해당사유: ①천재지변, ②비행기 또는 선박의 출항지연(선박의 경우, 도서지역 거주자만 가능),<br>
          &nbsp;&nbsp;&nbsp;③사고 또는 질병으로 인한 입원 ④직계 존.비속의 상)<br>
        - 출석고사 연기신청은 연기 신청서와 사유에 관한 증명서(진료확인서, 출항 지연 증명서등)를 팩스 및<br>
      	&nbsp;&nbsp;&nbsp;이메일로 제출하시기 바랍니다.<br>
         &nbsp;&nbsp;&nbsp;(FAX: 041.537.1473  이메일: <a href="mailto:iedu@moe.go.kr">iedu@moe.go.kr</a>)
          </li>
          <li class="frist">
          <span class="sub_check">
          <a href="#none" onclick="fn_download('연수연기_신청서.hwp', 'education_application.hwp', 'down')">
          <strong>[출석고사 연기신청서 양식]</strong>
          </a>
          </span>
          </li>
          
       </ul>
    </div>
     -->
    
    
	<div class="sub_txt_1depth m20">
      <h4>유의사항</h4>
      <ul>
        <li><!-- 개인정보수정<br /> -->
		     - <strong>인사이동 등으로 근무기관이 변경된 경우 수강 신청 전 개인정보 수정요망</strong><br />
		     - SMS 서비스를 위해 휴대폰 번호 확인 및 수정요망<br />
		     - 연수지명번호 및 근무기관 등 교육기간이 종료되기 전까지 수정가능</li>
		<li>수강제한 : 본 연수원에서 이수한 과정을 2년 이내 다시 신청할 수 없음</li>
       </ul>
    </div>
    
    
  </fieldset>
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
<script>
$(document).ready(function(){
	empChange('1');
});

function  empChange(val){
	if(val == '1'){
		$("#gubun1").show();
		$("#gubun2").hide();
		$("#gubun3").hide();
		$("#gubun4").hide();
	}else if(val == '2'){
		$("#gubun2").show();
		$("#gubun1").hide();
		$("#gubun3").hide();
		$("#gubun4").hide();
	}else if(val == '3'){
		$("#gubun3").show();
		$("#gubun1").hide();
		$("#gubun2").hide();
		$("#gubun4").hide();
	}else if(val == '4'){
		$("#gubun4").show();
		$("#gubun1").hide();
		$("#gubun2").hide();
		$("#gubun3").hide();
	}
}
</script>

