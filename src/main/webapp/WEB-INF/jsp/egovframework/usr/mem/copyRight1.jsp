<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%//@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>
<script type="text/javascript">
//<![CDATA[
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
//]]>
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
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<fieldset>
    <legend>개인정보처리방침</legend>	

		<!-- contents -->
		<div class="contents">
          
            <!-- 개인정보처리방침 내용 -->


            <div class="sub_text_2 m30">
                <h4>국립특수교육원부설원격교육연수원 개인정보처리방침</h4>                
            </div>

			<p class="personal_box">
				<span class="personal_blue">국립특수교육원 부설원격교육연수원(‘이하 국립특수교육원’)은 개인정보보호법에 따라 이용자의 개인정보 보호 및 권익을 보호하고 개인정보와 관련한 이용자의 고충을 원활하게 처리할 수 있도록 다음과 같은 처리방침을 두고 있습니다. 
				<br />개인정보처리방침은 시행일로부터 적용되며, 법령 및 방침에 따른 변경 내용의 추가, 삭제 및 정정에 관해서는 공지사항(또는 개별 공지)을 통하여 공지할 것입니다(제정 2015.6.19., 개정 2016.4.30). </span><br />
			</p>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제1조(개인정보의 처리 목적)</strong>
            			국립특수교육원은 다음과 같은 목적으로 개인정보를 처리합니다. 처리한 개인정보는 다음의 목적 이외의 용도로는 사용되지 않으며 이용 목적이 변경될 시에는 사전 동의를 구할 것입니다.
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					① 홈페이지 회원 가입 및 관리
				</span><br /><br />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;회원가입 의사 확인, 회원제 서비스 제공에 따른 본인 식별 인증, 회원 자격의 유지, 관리, 제한적 본인 확인제 시행에 따른 본인
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;확인, 각종 고지 통지 등의 목적으로 개인정보를 처리합니다.<br /><br />

				<span class="personal_blue1">
					② 연수 관련 서비스 제공 및 소관부서의 업무수행
				</span><br /><br />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;원격연수 운영 및 증명서(연수이수증 등) 발급·관리 등 서비스 제공에 관련한 목적으로 개인정보를 처리합니다.<br /><br />
				
				<span class="personal_blue1">
					③ 민원 사무 처리
				</span><br /><br />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;민원인의 신원 확인, 민원사항 확인, 사실 조사를 위한 연락 통지, 처리결과 통보 등을 위하여 개인 정보를 처리합니다.<br /><br />
			</p>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제2조(처리하는 개인정보 및 보유 기간)</strong>
            			국립특수교육원에서는 원칙적으로 개인정보의 처리목적이 달성되면 지체없이 개인정보를 파기합니다. 단, 다음의 정보에 대하여는 보유근거에 따라 명시한 기간 동안 보유합니다.
            		</li>
				</ul>
			</div>
			<div class="courseList1">
				<table width="100%" summary="위탁업체, 위탁업무 내용, 개인정보의 보유 및 이용기간으로 구성" cellspacing="0" class="personal_t">
			      	<caption>위탁업체</caption>
			      	<colgroup>
				      	<col width="10%" />
				      	<col width="10%" />
				      	<col width="10%" />
				      	<col width="40%" />
				      	<col width="20%" />
				      	<col width="10%" />
			      	</colgroup>
			      	<tbody>
				        <tr>
				          	<th scope="row">개인정보<br>파일</th>
				          	<th scope="row">보유부서</th>
				          	<th scope="row">운영목적</th>
				          	<th scope="row">개인정보<br>항목</th>
				          	<th scope="row">보유근거</th>
				          	<th scope="row">보유기간</th>
			        	</tr>
			        	<tr align="center">
			          		<td class="">원격교육연수이력</td>
			          		<td class="">원격교육연수원</td>
			          		<td class="">원격교육연수운영</td>
			          		<td class="">성명, 나이스 개인번호, 연수지명번호, 생년월일, 소속기관명, 연수성적, 이수번호</td>
			          		<td class="">교육공무원 승진규정(제32조), 교원 등의 연수에 관한 규정</td>
			          		<td class="">10년</td>
			        	</tr>
			        	<tr align="center">
			          		<td class="">원격교육연수 서비스 이용 내역</td>
			          		<td class="">원격교육연수원</td>
			          		<td class="">웹 사이트 방문기록</td>
			          		<td class="">방문 기록</td>
			          		<td class="">통신비밀보호법</td>
			          		<td class="">3개월</td>
			        	</tr>
			        	<tr align="center">
			          		<td class="">원격교육연수 대금결제  내역</td>
			          		<td class="">원격교육연수원</td>
			          		<td class="">연수비 환불</td>
			          		<td class="">대금결제 및 재화 등의 공급에 관한 기록</td>
			          		<td class="">전자상거래 등에서의 소비자보호에 관한 법률 </td>
			          		<td class="">5년</td>
			        	</tr>
			      	</tbody>
			   	</table>
			</div>		
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제3조(개인정보의 제3자 제공에 관한 사항)</strong>
            			국립특수교육원에서는 다음과 같은 이유로 개인정보에 대한 제3자 제공을 실시합니다.
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					① 이용자가 사전에 제3자 제공 및 공개에 동의한 경우
				</span><br /><br />
				<span class="personal_blue1">
					② 법령 등에 의해 제공이 요구되는 경우
				</span><br /><br />
				<span class="personal_blue1">
					③ 정보주체 또는 그 법정대리인이 의사표시를 할 수 없는 상태에 있거나 주소 불명 등으로 사전 동의를 받을 수 없는 경우로서 명백
					&nbsp;&nbsp;&nbsp;&nbsp;히 정보주체 또는 제 3자의 급박한 생명, 신체, 재산의 이익을 위하여 필요하다고 인정되는 경우
				</span><br /><br />
				<span class="personal_blue1">
					④ 특정 개인을 알아볼 수 없는 형태로 개인정보를 제공하는 경우
				</span><br /><br />
			</p>
			<div class="courseList1">
				<table width="100%" summary="위탁업체, 위탁업무 내용, 개인정보의 보유 및 이용기간으로 구성" cellspacing="0" class="personal_t">
			      	<caption>위탁업체</caption>
			      	<colgroup>
				      	<col width="15%" />
				      	<col width="15%" />
				      	<col width="20%" />
				      	<col width="40%" />
				      	<col width="10%" />
			      	</colgroup>
			      	<tbody>
				        <tr>

				          	<th scope="row">피제공기관</th>
				          	<th scope="row">제공목적</th>
				          	<th scope="row">개인정보파일명</th>
				          	<th scope="row">개인정보 항목</th>
				          	<th scope="row">제공주기</th>
			        	</tr>
			        	<tr align="center">
			          		<td class="">연수수요기관<br>(시도교육청 등)</td>
			          		<td class="">나이스 등록</td>
			          		<td class="">원격교육연수 이력</td>
			          		<td class="">성명, 나이스 개인번호, 연수지명번호, 생년월일, 소속기관명, 연수성적, 이수번호</td>
			          		<td class="">연수<br>종료 시</td>
			        	</tr>
			        	<tr align="center">
			          		<td class="">국가평생교육 진흥원</td>
			          		<td class="">평생교육 학습계좌 등록</td>
			          		<td class="">원격교육연수 이력</td>
			          		<td class="">성명, 생년월일, 소속기관명, 연수성적, 이수번호</td>
			          		<td class="">연수<br>종료 시</td>
			        	</tr>
			        	<tr align="center">
			          		<td class="">㈜LG유플러스</td>
			          		<td class="">연수 대금 결제<br>확인</td>
			          		<td class="">원격교육연수 대금결제  내역</td>
			          		<td class="">대금 결제 및 재화등의 공급에 관한 기록</td>
			          		<td class="">결제시</td>
			        	</tr>
			      	</tbody>
			   	</table>
			</div>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제4조(개인정보 위탁에 관한 사항)</strong>
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					① 국립특수교육원에서는 다음과 같은 목적으로 개인정보를 위탁하고 있습니다. 국립특수교육원에서는 개인정보 보호법 제26조에 따
					&nbsp;&nbsp;&nbsp;&nbsp;라 위탁업무 수행목적 외 개인정보 처리금지, 기술적·관리적 보호조치, 재위탁 제한, 수탁자에 대한 관리·감독, 손해배상 등 책임에
					&nbsp;&nbsp;&nbsp;&nbsp;관한 사항을 계약서 등 문서에 명시하고, 수탁자가 개인정보를 안전하게 처리하는지를 감독하고 있습니다.
				</span><br /><br />
				<span class="personal_blue1">
					② 위탁업무의 내용이나 수탁자가 변경될 경우에는 지체 없이 본 개인정보 처리방침을 통하여 공개하도록 하겠습니다.
				</span><br /><br />
			</p>
			<div class="courseList1">
				<table width="100%" summary="위탁업체, 위탁업무 내용, 개인정보의 보유 및 이용기간으로 구성" cellspacing="0" class="personal_t">
			      	<caption>위탁업체</caption>
			      	<colgroup>
				      	<col width="15%" />
				      	<col width="15%" />
				      	<col width="30%" />
				      	<col width="20%" />
				      	<col width="20%" />
			      	</colgroup>
			      	<tbody>
				        <tr>
				          	<th scope="row">구분</th>
				          	<th scope="row">수탁자</th>
				          	<th scope="row">위탁업무</th>
				          	<th scope="row">전화번호</th>
				          	<th scope="row">근무시간</th>
			        	</tr>
			        	<tr align="center">
			          		<td class="" rowspan="2">원격교육연수원</td>
			          		<td class="">㈜한경아이넷</td>
			          		<td class="">원격교육시스템 서버관리 및 시스템 운영</td>
			          		<td class="">042-934-7944</td>
			          		<td class="">09:00 - 18:00</td>
			        	</tr>
			        	<tr align="center">
			          		<td class="">㈜성우애드컴</td>
			          		<td class="">연수생 교재·배송 </td>
			          		<td class="">02-890-0900</td>
			          		<td class="">09:00 - 18:00</td>
			        	</tr>
			      	</tbody>
			   	</table>
			</div>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제5조(개인정보 제공 주체의 권리와 의무, 그 행사 방법에 관한 사항)</strong>
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					①국립특수교육원에 개인정보를 제공한 개인(법인)은 다음과 같이 개인정보주체로서의 권리를 행사할 수 있습니다.
				</span><br /><br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;가. 개인정보 열람요구<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;나. 오류 등이 있을 경우 정정 요구<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;다. 삭제요구(준영구 항목의 경우 제외)<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;라. 처리정지 요구<br /><br />				

				<span class="personal_blue1">
					② 제1항에 따른 권리 국립특수교육원 홈페이지에 대해 개인정보 보호법 시행규칙 별지 제8호 서식에 따라 서면, 전자우편, 모사전송
					&nbsp;&nbsp;&nbsp;&nbsp;(FAX) 등을 통하여 하실 수 있으며 <국립특수교육원>은 이에 대해 지체 없이 조치하겠습니다.
				</span><br /><br />
				<span class="personal_blue1">
					③ 정보주체가 개인정보의 오류 등에 대한 정정 또는 삭제를 요구한 경우에는 <국립특수교육원>은 정정 또는 삭제를 완료할 때까지
					&nbsp;&nbsp;&nbsp;&nbsp;당해 개인정보를 이용하거나 제공하지 않습니다.
				</span><br /><br />
				<span class="personal_blue1">
					④ 제1항에 따른 권리 행사는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 하실 수 있습니다. 이 경우 개인정보 보
					&nbsp;&nbsp;&nbsp;&nbsp;호법 시행규칙 별지 제11호 서식에 따른 위임장을 제출하셔야 합니다.
				</span><br /><br />
			</p>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제6조(개인정보의 파기 절차 및 방법)</strong>
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					① 이용자가 입력한 정보는 목적 달성 후 별도의 DB에 옮겨져(종이의 경우 별도의 서류) 내부 방침 및 기타 관련 법령에 따라 일정기
					&nbsp;&nbsp;&nbsp;&nbsp;간 저장된 후 혹은 즉시 파기됩니다. 이 때, DB로 옮겨진 개인정보는 법률에 의한 경우가 아니고서는 다른 목적으로 이용되지 않습
					&nbsp;&nbsp;&nbsp;&nbsp;니다.
				</span><br /><br />
				<span class="personal_blue1">
					② 이용자의 개인정보는 개인정보의 보유기간이 경과된 경우에는 보유기간의 종료일로부터 5일 이내에, 개인정보의 처리 목적 달성,
					&nbsp;&nbsp;&nbsp;&nbsp;해당 서비스의 폐지, 사업의 종료 등 그 개인정보가 불필요하게 되었을 때에는 개인정보의 처리가 불필요한 것으로 인정되는 날로
					&nbsp;&nbsp;&nbsp;&nbsp;부터 5일 이내에 그 개인정보를 파기합니다.
				</span><br /><br />
				<span class="personal_blue1">
					③ 종이에 출력된 개인정보는 분쇄기로 분쇄하거나 소각을 통하여 파기합니다.
				</span><br /><br />
			</p>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제7조(개인정보의 안전성 확보 조치)</strong>
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					① 국립특수교육원에서는 개인정보보호법 제29조에 따라 다음과 같이 안전성 확보에 필요한 기술적/관리적 및 물리적 조치를 하고 있
					&nbsp;&nbsp;&nbsp;&nbsp;습니다.
				</span><br /><br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;가. 개인정보 취급 직원의 최소화 및 교육 : 개인정보를 취급하는 직원을 지정하고 담당자에 한정시켜 최소화 하여 개인정보를
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리하는 대책을 시행하고 있습니다.<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;나. 내부관리계획의 수립 및 시행 : 개인정보의 안전한 처리를 위하여 내부관리계획을 수립하고 시행하고 있습니다.<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;다. 개인정보의 암호화 : 이용자의 비밀번호는 암호화 되어 저장 및 관리되고 있어, 본인만이 알 수 있으며 중요한 데이터는
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;파일 및 전송 데이터를 암호화 하거나 파일 잠금 기능을 사용하는 등의 별도 보안기능을 사용하고 있습니다.<br /><br />
				<span class="personal_blue1">
					② 해킹 등에 대비한 기술적 대책
				</span><br /><br />
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;국립특수교육원에서 운영하는 각 인터넷사이트는 해킹이나 컴퓨터 바이러스 등에 의한 개인정보 유출 및 훼손을 막기 위하여
 		            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;보안프로그램을 설치하고 주기적인 갱신·점검을 하며 외부로부터 접근이 통제된 구역에 시스템을 설치하고 기술적/물리적으로
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;감시 및 차단하고 있습니다.<br /><br />
				<span class="personal_blue1">
					③ 개인정보에 대한 접근 제한
				</span><br /><br />
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;개인정보를 처리하는 데이터베이스시스템에 대한 접근권한의 부여, 변경, 말소를 통하여 개인정보에 대한 접근통제를 위하여
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;필요한 조치를 하고 있으며 침입차단시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있습니다.<br /><br />
				<span class="personal_blue1">
					④ 접속기록의 보관 및 위변조 방지
				</span><br /><br />
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 개인정보처리시스템에 접속한 기록을 최소 6개월 이상 보관, 관리하고 있으며, 접속 기록이 위변조 및 도난, 분실되지 않도록
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;보안기능 사용하고 있습니다.<br /><br />
				<span class="personal_blue1">
					⑤ 비인가자에 대한 출입 통제
				</span><br /><br />
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;개인정보를 보관하고 있는 물리적 보관 장소를 별도로 두고 이에 대해 출입통제 절차를 수립, 운영하고 있습니다.<br /><br />
			</p>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제8조(개인정보 보호책임자의 배치)</strong>
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					① 국립특수교육원은 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등
					&nbsp;&nbsp;&nbsp;&nbsp;을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.
				</span><br /><br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶ 개인정보 보호책임자<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;직책 :정보지원과장<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;연락처 :041-537-1480, durugy@moe.go.kr, (F)041-537-1489<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;※ 개인정보 보호 담당부서로 연결됩니다<br /><br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶ 개인정보 보호 담당자<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;부서명 :정보지원과<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;연락처 :041-537-1496, moon@moe.go.kr, (F)041-537-1497<br /><br />
				<span class="personal_blue1">
					② 정보주체께서는 국립특수교육원 홈페이지의 서비스(또는 사업)을 이용하시면서 발생한 모든 개인정보 보호 관련 문의, 불만처리,
					&nbsp;&nbsp;&nbsp;&nbsp;피해구제 등에 관한 사항을 개인정보 보호책임자 및 담당부서로 문의하실 수 있습니다. 국립특수교육원 홈페이지는 정보주체의 문
					&nbsp;&nbsp;&nbsp;&nbsp;의에 대해 지체 없이 답변 및 처리해드릴 것입니다.
				</span><br /><br />
			</p>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제9조(개인정보의 열람)</strong>
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					① 정보주체는 개인정보 보호법 제35조에 따른 개인정보의 열람 청구를 아래의 부서에 할 수 있습니다. 국립특수교육원 홈페이지는
					&nbsp;&nbsp;&nbsp;&nbsp;정보주체의 개인정보 열람청구가 신속하게 처리되도록 노력하겠습니다.
				</span><br /><br />
				<span class="personal_blue1">
					② 개인정보 열람청구 접수·처리 부서
				</span><br />
			</p>
			<div class="courseList1">
				<table width="100%" summary="위탁업체, 위탁업무 내용, 개인정보의 보유 및 이용기간으로 구성" cellspacing="0" class="personal_t">
			      	<caption>위탁업체</caption>
			      	<colgroup>
				      	<col width="25%" />
				      	<col width="25%" />
				      	<col width="25%" />
				      	<col width="25%" />
				      	<col width="25%" />
			      	</colgroup>
			      	<tbody>
				        <tr>
				          	<th scope="row" rowspan="2">열람정보명</th>
				          	<th scope="row" rowspan="2">처리부서</th>
				          	<th scope="row" colspan="3">연락처</th>
						</tr>
				        <tr>
				          	<th scope="row">성명(직위)</th>
				          	<th scope="row">전화</th>
				          	<th scope="row">팩스</th>
			        	</tr>
			        	<tr align="center">
			          		<td class="">원격교육연수원 이력</td>
			          		<td class="">원격교육연수원</td>
   			          		<td class="">강성구(교육연구사)</td>
			          		<td class="">041-537-1474</td>
			          		<td class="">041-537-1473</td>
			        	</tr>
			      	</tbody>
			   	</table>
			</div>
			<br /><br />
			<p class="personal_detail">
				<span class="personal_blue1">
					③ 정보주체는 제1항의 열람청구 접수·처리부서 이외에, 행정안전부의 ‘개인정보보호 종합지원 포털’ 웹 사이트(<a href="http://privacy.kisa.or.kr" target="_blank">www.privacy.go.kr</a>)
					&nbsp;&nbsp;&nbsp;&nbsp;를 통하여서도 개인정보 열람청구를 하실 수 있습니다.
				</span><br /><br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶ 행정안전부 개인정보보호 종합지원 포털 → 개인정보 민원 → 개인정보 열람등 요구<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(본인확인을 위하여 아이핀(I-PIN)이 있어야 함)<br /><br />
			</p>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제10조(권익침해 구제방법)</strong>
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				<span class="personal_blue1">
					① 개인정보침해신고센터에서는 누구든지 개인정보보호에 관한 문의사항이 있거나, 개인정보를 침해당한 경우에는 다음의  방법으로
					&nbsp;&nbsp;&nbsp;&nbsp;개인정보침해신고센터에 상담 또는 피해구제를 신청할 수 있습니다.
				</span><br /><br />			
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶ 개인정보 침해신고센터 (한국인터넷진흥원 운영)<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 소관업무 : 개인정보 침해사실 신고, 상담 신청<br />
  					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 홈페이지 : <a href="http://privacy.kisa.or.kr" target="_blank">privacy.kisa.or.kr</a><br />
  					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 전화 : (국번없이) 118<br />
  					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  주소 : (138-950) 서울시 송파구 중대로 135 한국인터넷진흥원 개인정보침해신고센터<br /><br />

					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶ 개인정보 분쟁조정위원회 (한국인터넷진흥원 운영)<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 소관업무 : 개인정보 분쟁조정신청, 집단분쟁조정 (민사적 해결)<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 홈페이지 : <a href="http://privacy.kisa.or.kr" target="_blank">privacy.kisa.or.kr</a><br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 전화 : (국번없이) 118<br /> 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  주소 : (138-950) 서울시 송파구 중대로 135 한국인터넷진흥원 개인정보침해신고센터<br /><br />

					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶ 대검찰청 사이버범죄수사단 : 02-3480-3573 (<a href="http://www.spo.go.kr" target="_blank">www.spo.go.kr</a>)<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶ 경찰청 사이버범죄수사단 : 1566-0112 (<a href="http://www.netan.go.kr" target="_blank">www.netan.go.kr</a>)<br /><br />
				<span class="personal_blue1">
					② 접수된 내용에 대해서는 양 당사자의 의견청취, 증거수집 등 관련 법률에 의거한 사실 확인을 거친 후, 법률위반 사업자에 대한
					&nbsp;&nbsp;&nbsp;&nbsp;재발방지 조치, 관계기관에 위법사실 통보 등을 시행하고 있습니다.
				</span><br /><br />			
				<span class="personal_blue1">
					③ 개인정보침해에 관한 신고는 신청인이 직접 또는 대리로 신청할 수 있으며,침해신고사건이 접수되면, 신청자와 해당기관에게 
					&nbsp;&nbsp;&nbsp;&nbsp;접수사실이 통보됩니다.
				</span><br /><br />			
			</p>
			<div class="sub_title m20">
				<ul>
            		<li>
            			<strong>제11조(개인정보 처리방침 변경)</strong>
            			이 개인정보처리방침은 2016년 5월 27일로부터 적용되며, 이전의 개인정보처리방침은 아래에서 확인할 수 있습니다.<br /><br />		
            		</li>
				</ul>
			</div>
			<p class="personal_detail">
				&nbsp;&nbsp;<a href="#" target="">법 시행일부터 ~ 2016.5.27. 적용지침(클릭)</a><br />	
				&nbsp;&nbsp;<a href="#" target="">2012.~ 2016.5.26</a><br />	
				&nbsp;&nbsp;<a href="#" target="">2016.5.27.~</a><br />	
			</p>
		</div>
	</fieldset>
</form>     
<script type="text/javascript">
//<![CDATA[
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
//]]>
</script>      

<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

