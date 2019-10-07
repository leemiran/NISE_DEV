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
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>





		
		    <div class="sub_txt_1depth">
                    <h4>유의사항</h4>   
                    <ul>
                    	<li>교원직무연수는 1기수에 1과정만 신청하실 수 있습니다. (유.초.중등 교원 및 교육 전문직) </li>
                        <li>중복연수로 인한 선의의 피해가 돌아가지 않도록, 연수 대상 선생님들의 각별한 주의 바랍니다.</li>
                        <li>교원직무연수과정은 타 기관의 연수일정과 겹칠 경우 그 중 한개의 연수만을 학점으로 인정받습니다. </li>
                        <li>수강 신청을 하시기 전에 [연수안내 - 모집요강]의 일정을 반드시 참고하시길 바랍니다. </li>
                        <li>연수비 입금 순서에 따라서 선발합니다. (수강신청순이 아니므로 유의하세요.) </li>
                        <li>교원직무연수의 경우 원격교육연수와 집합교육의 중복허용범위
                        	<ul class="sub_txt_2depth">
                            	<li><strong>평일</strong> : 원격교육연수와 집합교육의 중복허용 범위는 허용되지 않습니다.<br />
                               	    * 단, 원격교육연수와 집합교육의 중복이 연수기간의 1/4이하인 경우에는 인정<br />
                                    (중복기간의 계산은 기간이 짧은 연수를 기준으로 함)
								</li>
                                <li><strong>방학중</strong> : 원격교육연수와 집합교육연수를 포함한 2강좌의 중복은 허용<br />
                               		(원격연수 1강좌 + 집합연수 1강좌 또는 원격연수 2강좌)<br />
                                    * 연수비는 무통장입금, 폰 뱅킹, 인터넷 뱅킹으로 결제하실 수 있습니다.
								</li>
                            </ul>
                        </li>
                    </ul>                
           </div>
           
           <div class="sub_txt_1depth m30">
                    <h4>신청절차</h4>   
                    <ul>
                    	<li class="frist">① 회원가입 - 국립특수교육원부설원격교원연수원 (iedu.kise.go.kr) 사이트에 회원가입 후 로그인.<br />
                        	<span class="sub_check">회원구분에서 교원/특수교육보조인력을 선택하십시오.(학부모 과정신청자만 학부모 회원가입)</span>
                        </li>
                        <li class="frist">② 연수강좌 신청 - 개설된 강좌 중 수강을 원하는 과목을 선택한다.(신청중인 과정만 가능)</li>
                        <li class="frist">- 회원가입약관에 동의 후 , ' I-PIN 인증 보인확인 '버튼을 클릭합니다. 아이핀 ID와 비밀번호 입력 후 확인을 클릭하여 본인확인절차를 완료합니다.</li>
                        <li class="frist">※ 아이핀 ID와 비밀번호가 없는 분들은 ' 아이핀 신규발급' 을 클릭하여 일반회원(클릭)하여 약관동의- 회원가입 진행하시면 됩니다.</li>
                        <li class="frist mb5"><span class="tblue"><a href="#none" onclick="fn_download('공공_I-PIN_서비스_관계법령.hwp', 'I-PIN.hwp', 'down')">공공 I-PIN 서비스 관계법령.hwp (다운로드)</a></span></li>
                        <li class="frist">③ 연수지명번호 입력 - 학교에서 부여받은 지명번호를 입력한다(4주과정의 경우에만 출석고사장을 선택)<br />
                        <li class="frist">③ 연수지명번호 입력 - 학교에서 부여받은 지명번호를 입력한다(4주과정의 경우에만 출석고사장을 선택)<br />
                            <span class="sub_check">특수교육 보조인력 직무연수 및 학부모 과정은 연수지명번호가 필요 없습니다.(결제하기로 생략하고 다음단계   진행)</span>                        	
                        </li>
                        <li class="frist">④ 연수비 납부 - 무통장입금, 신용카드, 실시간 계좌이체, 가상계좌로 선택하여 납부합니다.<br />
                            <span class="sub_check">특별모집과정(교육청 위탁연수)은 [무통장입금]를 선택하십시오.</span>                        	
                        </li>
                    </ul>                
           </div>
           
           
           <div class="sub_txt_1depth m30">
                    <h4>입금계좌</h4>   
                    <ul>
                    	<li>각 과정별 연수기간, 신청기간, 연수비, 과정내용을 확인합니다.</li>
                        <li>결제는 무통장입금, 신용카드, 실시간 계좌이체, 가상계좌로 하실 수 있습니다. 
                        <a href="javascript:go_view('http://ecredit.dacom.net/renewal/html/guide_popup/menu_01.htm','550','600','yes');">
                        <b><font color=blue>[결제 오류시 해결방법 안내]</font></b>
                        </a>
                        </li>
                        <li>무통장입금을 제외한 모든 결제는 팝업창으로 진행됩니다. 팝업차단을 해제해 주십시오. (방법1,2중 택1)<br />
                            <span class="sub_check">(방법1) 익스플로러 -> 도구 -> 팝업차단 -> 팝업차단 사용 안함</span><br />
                            <span class="sub_check">(방법2) 익스플로러 -> 도구 -> 인터넷 옵션 -> 개인정보 탭 선택 -> 팝업차단 사용 (녹색 체크표시 클릭)해제 -> 확인</span>
                        </li>
                        <li>수강신청인 이름으로 꼭 입금해 주세요. (타인명의 입금시 고객센터에 글을 꼭 남겨주세요.)</li>
                        <li>학교에서 입금하실 경우에는 학교명성명으로 입금해 주십시요. (예시) 안산초홍길동, 중앙고황신혜 등 </li>
                        <li><strong>시도교육청 위탁·단체 연수과정</strong>은 시도교육청에서 입금하므로 <strong>[무통장입금 / 연수비 일괄납부]</strong>를 선택해 주십시요. </li>
                        <li class="frist">
                        		<div class="courseList">
                                    <table summary="선택, 설명으로 구성" cellspacing="0" width="100%">
                <caption>결제수단 안내</caption>
                <colgroup>
                                            <col width="10%" />
                                            <col width="90%" />                                                                         
                                        </colgroup>
                                        <thead>
                                            <tr>
                                                <th scope="row">선택</th>                                                
                                                <th scope="row" class="last">설명</th>                                    
                                            </tr>
                                        </thead>
                                        <tbody>					
                                            <tr>
                                                <td>무통장입금</td>
                                                <td class="last left">
                                                		<ul class="table_txt_1depth">
                                                            <li class="frist">1. 연수원 계좌로 직급 송금 하실 경우 선택
                                                                <ul class="table_txt_2depth">
                                                                    <li> 수강신청 전 선결제, 타행환 입금, 은행창구 무통장 입금, 폰뱅킹, 인터넷 뱅킹,학교에서 대신 납부, 
      																	타인명의 납부, 교육청위탁연수 및 보조인력특별과정 수강신청시 이용
                                                                    </li>
                                                                    <li><strong>무통장 입금 계좌 안내</strong><br /> 
                                                                   		 <strong>- 은   행   명 : 우체국</strong><br />
                                                                         <strong>- 계 좌 번 호 : 301622-01-000041</strong><br />
                                                                         <strong>- 예   금   주 : 국립특수교육원</strong> 
                                                                    </li>
                                                                </ul>
                                                            </li>
                                                            <li class="frist">2. 무통장 입금하신 후에 [입금확인 요청] 게시판에 글을 남겨 주시면 바로 수강 승인이 가능합니다.</li>
                                                        </ul>
                                                </td>                                    
                                            </tr> 
                                            <tr>
                                                <td>신용카드</td>
                                                <td class="last left">
                                                		<ul class="table_txt_1depth">
                                                            <li class="frist">1. 신용카드 결제시 개인정보 보호 및 카드 정보 도용 방지를 위하여 카드사 별로 ISP 결재 또는 안심 클릭을 의무적으로 사용합니다. 
                                                                <ul class="table_txt_2depth">
                                                                    <li>KB, BC, 우리 카드 : ISP결재 
                                                                    <a href="javascript:go_view('http://pgweb.dacom.net/WEB_SERVER/auth/help03.html','420','500','no');">
                                                                    <img src="/images/user/btn_isp.gif" alt="ISP안내" class="vrM"/>
                                                                    </a></li>
                                                                    <li>신한(구LG), 신한, 삼성, 현대 외한카드 : 안심클릭 
                                                                    <a href="javascript:go_view('http://pgweb.dacom.net/WEB_SERVER/auth/help02.html','420','380','no');">
                                                                    <img src="/images/user/btn_sim.gif" alt="안심클릭안내" class="vrM"/>
                                                                    </a>
                                                                    </li>
                                                                </ul>
                                                            </li>
                                                            <li class="frist">2. 신용카드 및 계좌와 관련된 모든 정보 입력은 LG데이콤의 eCredit에서 이루어지며 귀하의 신용카드 관련 정보를 저장하거나 입력 받지 않습니다.</li>
                                                        </ul>
                                                </td>                                    
                                            </tr> 
                                            <tr>
                                                <td>실시간 계좌이체</td>
                                                <td class="last left">
                                                		<ul class="table_txt_1depth">
                                                            <li class="frist">1. . 전국 은행과 실시간으로 연결하여 결제하는 방식으로 입력하신 정보(계좌번호, 암호 등)는 2중으로 암호화되어 처리되므로 안심하시고 사용하실 수 있습니다. 
 
                                                                <ul class="table_txt_2depth">
                                                                    <li>단, <strong>국민은행은 인터넷뱅킹 가입고객</strong>만 결재가 가능합니다.</li>
                                                                    <li>결재가 안되실 경우 거래은행 홈페이지 또는 결재창의 도움말을 참조하여 주십시오.</li>
                                                                </ul>
                                                            </li>
                                                            <li class="frist">2. 이체하실 계좌의 잔액이 연수비보다 많아야 합니다. </li>
                                                            <li class="frist">3. 타행 이체수수료 부담이 없습니다.(연수원에서 부담합니다.) </li>
                                                        </ul>
                                                </td>                                    
                                            </tr>  
                                            <tr>
                                                <td>가상계좌</td>
                                                <td class="last left">
                                                		<ul class="table_txt_1depth">
                                                        	<li class="frist">1. 가상계좌(무통장입금)을 선택하시면 본인에게 가상 계좌번호가 부여됩니다.</li>
                                                            <li class="frist">2. 반드시 해당 계좌로 입금하시기 바라며 입금 전까지는 수강신청중으로 유지됩니다.</li>
                                                            <li class="frist">3. 발급받으신 가상계좌로 연수비를 입금하여 주십시오.(무통장, 폰뱅킹, 인터넷뱅킹 사용가능)  
                                                                <ul class="table_txt_2depth">
                                                                    <li><strong>수강신청 기간 내에 입금</strong>하셔야 정상적으로 수강승인이 이루어 집니다.</li>                                                                    
                                                                </ul>
                                                            </li>
                                                            <li class="frist">4. 현금영수증 발급여부는 결재창에서 결재시 선택하실 수 있습니다. 
                                                            <a href="http://ecredit.dacom.net/renewal/html/AddiService/addser03.htm" target="_blank">
                                                            <img src="/images/user/btn_money.gif" alt="현금영수증 안내보기" class="vrM"/>
                                                            </a>
                                                            </li>
                                                            
                                                        </ul>
                                                </td>                                    
                                            </tr>                                                                                     	
                                        </tbody>
                                    </table>
                                </div>
                        </li>                        	                      
                    </ul>                
           </div>
           
           <div class="sub_txt_1depth m30">
                    <h4>수강철회</h4>   
                    <ul>
                    	<li class="frist"><span class="sub_check"><strong>연수비의 반환 기준은 다음과 같습니다.(운영규정 제13조 제3항)</strong></span></li>
                        <li class="frist">
                        		<div class="courseList">
                                    <table summary="반환 사유 발생일, 반환금액으로 구성" cellspacing="0" width="100%">
                <caption>수강철회</caption>
                <colgroup>
                                            <col width="50%" />
                                            <col width="50%" />                                                                         
                                        </colgroup>
                                        <thead>
                                            <tr>
                                                <th scope="row">반환 사유 발생일</th>                                                
                                                <th scope="row" class="last">반환금액</th>                                    
                                            </tr>
                                        </thead>
                                        <tbody>					
                                            <tr>
                                                <td>연수시작 전</td>
                                                <td class="last">납입금 전액</td>                                    
                                            </tr> 
                                            <tr>
                                                <td>연수시작 후 7일 이내</td>
                                                <td class="last">납입금의 70%</td>                                    
                                            </tr>
                                            <tr>
                                                <td>연수시작 후 7일 경과</td>
                                                <td class="last">반환하지 아니함</td>                                    
                                            </tr>                                                                                                                       	
                                        </tbody>
                                    </table>
                                </div>
                        </li>
                        <li class="frist"><span class="sub_check"><strong>반환기준은 실입금액에서 기본경비, 수수료 등을 제외한 범위로 한다.</strong></span></li>
                    </ul>                
           </div>
           
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

<script type="text/javascript">
<!--
function go_view(url,w,h,s)
{
	win = window.open(url, "pay", "resizable=yes,width="+w+",height="+h+",location=no,scrollbars="+s+", menubar=no");
}
//-->
</script>