
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>





<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post">
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

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
<fieldset>
<%
	int i = 1;
%>
	<div class="sub_txt_1depth m30">
		<h4><strong>원격연수 연간연수 일정</strong></h4>   
	    <p class="course_down"><a href="#none" onclick="fn_download('연수과정 안내(2017)-교과목 및 시간배당(0531).pdf', '2017_timeresult_1.pdf', 'down')"><img src="/images/user/c_d_download.gif" alt="2014년 연수일정표 다운로드" /></a></p>
	    <ul>                	
			
		    	<div class="courseList">
		    		<table width="100%" summary="월, 과정, 등록기간, 연수기간(주) 으로 구성되어짐">
		    			<caption>원격연수 연간연수 일정</caption>
	                    <colgroup>
	                    	<col width="10%" />
	                        <col width="20%" />
	                        <col width="30%" />
	                        <col width="40%" /> 
	                        <col />
	                    </colgroup>
	                    <thead>
	                    	<tr>
		                        <th scope="row">월</th>
		                        <th scope="row">과정</th>
		                        <th scope="row">등록기간</th>
		                        <th scope="row" class="last">연수기간(주)</th>		                        
	                        </tr>
	                    </thead>
	                    <tbody>
	                    	<tr>
								<td rowspan="2">3</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">2.22(수)~3.7(화)</td>
							  	<td class="last">3.8(수)~3.21(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">3.8(수)~3.28(화)(3주)</td>							  	
							</tr>
							<tr>
								<td rowspan="2">4</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">3.22(수)~4.4(화)</td>
							  	<td class="last">4.5(수)~4.18(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">4.5(수)~4.25(화)(3주)</td>							  	
							</tr>
							<tr>
								<td rowspan="2">5</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">4.26(수)~5.9(화)</td>
							  	<td class="last">5.10(수)~5.23(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">5.10(수)~5.30(화)(3주)</td>							  	
							</tr>
							<tr>
								<td rowspan="2">6</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">5.24(수)~6.6(화)</td>
							  	<td class="last">6.7(수)~6.20(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">6.7(수)~6.27(화)(3주)</td>							  	
							</tr>
							<tr>
								<td rowspan="2">7</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">6.21(수)~7.4(화)</td>
							  	<td class="last">7.5(수)~7.18(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">7.5(수)~7.25(화)(3주)</td>							  	
							</tr>
							<tr>
								<td rowspan="2">8</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">7.26(수)~8.8(화)</td>
							  	<td class="last">8.9(수)~8.22(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">8.9(수)~8.29(화)(3주)</td>							  	
							</tr>
							<tr>
								<td rowspan="2">9</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">8.23(수)~9.5(화)</td>
							  	<td class="last">9.6(수)~9.19(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">9.6(수)~9.26(화)(3주)</td>							  	
							</tr>
							<tr>
								<td rowspan="2">10</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">9.27(수)~10.10(화)</td>
							  	<td class="last">10.11(수)~10.24(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">10.11(수)~10.31(화)(3주)</td>							  	
							</tr>
							<tr>
								<td rowspan="2">11</td>
							  	<td>30시간 미만과정</td>
							  	<td rowspan="2">10.25(수)~11.7(화)</td>
							  	<td class="last">11.8(수)~11.21(화)(2주)</td>							  	
							</tr>
							<tr>								
							  	<td>30시간 이상과정</td>							  	
							  	<td class="last">11.8(수)~11.28(화)(3주)</td>							  	
							</tr>
	                    </tbody>
	                </table>                                    
	            </div>
	            <div>
	            	<p></br>※ 우리원의 모든 원격연수과정을 매월 접수받아 5명 이상일 경우 개강, 선착순 승인방식으로 운영되며 과정별 최대인원은 2016년 10월 수요조사 결과를 기초로 산정함.</p>
	            </div>
	            </br>
	           
	           <h4><strong>원격연수 연수과정</strong></h4>
	           <div class="courseList">
		    		<table width="100%" summary="구분, 과정명, 시간, 대상, 인정대상 으로 구성되어짐">
		    			<caption>원격연수 연간연수 일정</caption>
	                    <colgroup>
	                    	<col width="10%" />
	                        <col width="50%" />
	                        <col width="10%" />
	                        <col width="10%" /> 
	                        <col width="10%" />
	                        <col width="10%" />
	                        <col />
	                    </colgroup>
	                    <thead>
	                    	<tr>
		                        <th scope="row" rowspan="2">구분</th>
		                        <th scope="row" rowspan="2" >과정명</th>
		                        <th scope="row" rowspan="2">시간</th>
		                        <th scope="row" rowspan="2">대상</th>
		                        <th scope="row" class="last" colspan="2">인정대상</th>		                        
	                        </tr>
	                        <tr>		                        
		                        <th scope="row" class="last">통합교육</th>
		                        <th scope="row" class="last">보조인력</th>		                        
	                        </tr>
	                    </thead>
	                    <tbody>
	                    	<tr>
								<td rowspan="17">교원직무</td>
							  	<td>특수교육교원 역량강화1</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>특수교육교원 역량강화2</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>장애인 등에 대한 특수교육법 및  관련법규의 이해</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>특수교육관련 검사도구의 사용법</td>
							  	<td>45</td>
							  	<td>교원 등</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>특수교육대상학생의 국어과 교수·학습지도</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>특수교육대상학생의 수학과 교수·학습지도</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>특수교육대상학생의 사회과 교수·학습지도</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>특수교육대상학생의 과학과 교수·학습지도</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>장애학생 진로·직업교육</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>행복한 교실을 만들어 가는 긍정적  행동지원 기초</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>(신규)행복한 교실을 만들어 가는  긍정적 행동지원 심화</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<!-- <tr>								
							  	<td>장애학생 이해와 통합학급 운영1(패키지)</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>장애학생 이해와 통합학급 운영2(패키지)</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>행복한 교실을 만들어가는 긍정적 행동지원1_기초(패키지)</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>(신규)행복한 교실을 만들어가는 긍정적 행동지원2_심화(패키지)</td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr> 
							<tr>								
							  	<td>장애학생을 위한 지원 및 인권․안전교육  길라잡이</br>	
(장애학생의 인권과 성교육 + 장애학생 안전교육의 첫 걸음) </td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							-->
							<tr>								
							  	<td>장애학생 이해와 통합학급 운영1 </td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>장애학생 이해와 통합학급 운영2 </td>
							  	<td>30</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>(신규)장애학생 안전교육의 첫 걸음 </td>
							  	<td>15</td>
							  	<td>교원 등</td>
							  	<td>○</td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>2015 특수교육 교육과정 및 교과용 도서 현장 활용</td>
							  	<td>15</td>
							  	<td>교원 등</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>재활복지 미전환 치료교사 자격연수 </td>
							  	<td>61</td>
							  	<td>해당교원</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>특수교육 희소교과 상위자격 취득을 위한  사전연수 </td>
							  	<td>30</td>
							  	<td>해당교원</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>
								<td rowspan="3">보조인력</td>
							  	<td>특수교육 보조인력 역량강화 기초</td>
							  	<td>30</td>
							  	<td>보조인력</td>
							  	<td></td>							  	
							  	<td class="last">○</td>							  	
							</tr>
							<tr>								
							  	<td>특수교육 보조인력 역량강화 심화 </td>
							  	<td>30</td>
							  	<td>보조인력</td>
							  	<td></td>							  	
							  	<td class="last">○</td>					  	
							</tr>
							<tr>								
							  	<td>특수교육 보조인력 역량강화 고급</td>
							  	<td>30</td>
							  	<td>보조인력</td>
							  	<td></td>							  	
							  	<td class="last">○</td>					  	
							</tr>
							<tr>
								<td rowspan="5">교양연수</td>
							  	<td>장애학생의 인권과 성교육</td>
							  	<td>15</td>
							  	<td>공통</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>수화언어 기초과정</td>
							  	<td>15</td>
							  	<td>공통</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>시각장애학생 보행훈련</td>
							  	<td>15</td>
							  	<td>공통</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>장애학생을 위한 특수교육지원능력 개발</td>
							  	<td>20</td>
							  	<td>공통</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>							
							<tr>								
							  	<td>보조교과서 및 교수학습자료 현장 활용</td>
							  	<td>3</td>
							  	<td>공통</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>
								<td rowspan="3">학부모</td>
							  	<td>행복한 가정을 만들어가는 긍정적 행동지원</td>
							  	<td>15</td>
							  	<td>학부모</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>학부모가 알아야할 특수교육개론</td>
							  	<td>30</td>
							  	<td>학부모</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
							<tr>								
							  	<td>학부모가 알아야할 치료지원 서비스</td>
							  	<td>30</td>
							  	<td>학부모</td>
							  	<td></td>							  	
							  	<td class="last"></td>							  	
							</tr>
	                    </tbody>
	                </table>                                    
	            </div>
	    		 	    	        
		</ul>            
	</div>   
	
	    
</fieldset>
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

<script type="text/javascript">
	function change2(bb){
		time.location.href=""+bb+".htm";
		}	
</script>
