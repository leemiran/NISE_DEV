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



		<div style="overflow:hidden;">
				
				<div class="sitemap">
                	<h3>연수안내</h3>
                	<ul>
                    	<li><a href="#" onclick="changeMenu(1, 1);return false;">연수일정</a></li>
                        <li><a href="#" onclick="changeMenu(1, 2);return false;">연수수강 절차</a></li>
                        <li><a href="#" onclick="changeMenu(1, 3);return false;">학습방법</a></li>
                        <li><a href="#" onclick="changeMenu(1, 4);return false;">평가방법</a></li>
                        <li><a href="#" onclick="changeMenu(1, 5);return false;">모바일 연수안내</a></li>                        
                        <!-- <li><a href="#" onclick="changeMenu(1, 5);return false;">문의안내</a></li> -->
                    </ul>
                </div>
                <div class="sitemap">
                	<h3>연수신청</h3>
                	<ul>
                        <li><a href="#" onclick="changeMenu(2, 1);return false;">신청 가능한 연수</a></li>
                        <!-- <li><a href="#" onclick="changeMenu(2, 2);return false;">개설교육과정</a></li> -->
                    </ul>
                </div> 
        		<div class="sitemap">
                	<h3>나의강의실</h3>
                	<ul>
                    	<li><a href="#" onclick="changeMenu(3, 1);return false;">강의실</a></li>
                        <li><a href="#" onclick="changeMenu(3, 2);return false;">나의문의함</a></li>
                        <li><a href="#" onclick="changeMenu(3, 3);return false;">영수증/이수증</a></li>
                        <li><a href="#" onclick="changeMenu(3, 5);return false;">개인정보수정</a></li>
                        <!-- <li><a href="#" onclick="changeMenu(3, 2);return false;">나의 연수 이력</a></li> -->
                        <!-- <li><a href="#" onclick="changeMenu(3, 3);return false;">연수수강 내역</a></li> -->
                        <!-- <li><a href="#" onclick="changeMenu(3, 4);return false;">출석고사 내역</a></li> -->
                        <!-- <li><a href="#" onclick="changeMenu(3, 5);return false;">연수결제내역</a></li> -->
                    </ul>
                </div>
        </div>        
		<!-- sitemap 1단-->
        
        <!-- sitemap 2단 -->
        <div style=" overflow:hidden; clear:both; padding-top:30px;">
        		<div class="sitemap">
                	<h3>참여마당</h3>
                	<ul>
                    	<li><a href="#" onclick="changeMenu(4, 1);return false;">공지사항</a></li>
                        <li><a href="#" onclick="changeMenu(4, 2);return false;">자주하는 질문</a></li>      
                        <li><a href="#" onclick="changeMenu(4, 3);return false;">연수문의</a></li>      
                        <!-- <li><a href="#" onclick="changeMenu(4, 2);return false;">연수개선의견</a></li> -->      
                        <!-- <li><a href="#" onclick="changeMenu(4, 2);return false;">특수교육자료실</a></li> -->
                        <!-- <li><a href="#" onclick="changeMenu(4, 4);return false;">연수후기</a></li> -->      
                                                             
                    </ul>
                </div>
                
                <div class="sitemap">
                	<h3>연수행정</h3>
                	<ul>
                    	<li><a href="#" onclick="changeMenu(5, 1);return false;">지명번호 등록/변경</a></li>
                    	<li><a href="#" onclick="changeMenu(5, 2);return false;">교육부인가서</a></li>
                        <!-- <li><a href="#" onclick="changeMenu(5, 2);return false;">출석고사장 입력</a></li> -->
                        <!-- <li><a href="#" onclick="changeMenu(5, 3);return false;">이수증 출력</a></li> -->
                        <!-- <li><a href="#" onclick="changeMenu(5, 4);return false;">영수증 출력</a></li> -->
                        <!-- <li><a href="#" onclick="changeMenu(5, 5);return false;">입금 확인 및 환불 요청</a></li> -->                        
                    </ul>
                </div>
                
                <div class="sitemap">
                	<h3>마이페이지</h3>
                	<ul>
                    	<!-- <li><a href="#" onclick="changeMenu(6, 1);return false;">나의 상담내역</a></li>
                        <li><a href="#" onclick="changeMenu(6, 2);return false;">나의 관심과정</a></li> -->                         
<!--                        <li><a href="#" onclick="changeMenu(6, 3);return false;">쪽지관리</a></li>-->
                        <li><a href="#" onclick="changeMenu(6, 3);return false;">개인정보 수정</a></li>
                        <li><a href="#" onclick="changeMenu(6, 6);return false;">비밀번호변경</a></li>
                        <!-- <li><a href="#" onclick="changeMenu(6, 4);return false;">개인정보처리방침</a></li> -->
                        <li><a href="#" onclick="changeMenu(6, 5);return false;">회원탈퇴</a></li>
                        <li><a href="#" onclick="changeMenu(6, 8);return false;">아이디통합</a></li>
                    </ul>
                </div>
        </div> 
           
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->