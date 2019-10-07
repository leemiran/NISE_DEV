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
                    <h4><strong>교원직무연수</strong></h4>   
                    <p class="course_down"><a href="#none" onclick="fn_download('교과목_및_시간배정.hwp', 'timeresult.hwp', 'down')"><img src="/images/user/c_d_download.gif" alt="2014년 연수일정표 다운로드" /></a></p>
      <ul>
                    	
                        <li><strong>교원직무연수 : 31과정, 총11,844명 </strong>
                          <div class="courseList">
                        	<table width="100%" summary="기별, 과정명, 인원, 등록기간, 연수기간, 시간, 대상으로 구성되어짐">
                        	<caption>교원직무연수 과정과 총 인원</caption>
                                         <colgroup>
                                            <col width="5%" />
                                            <col width="5%" />
                                            <col width="5%" />
                                            <col width="30%" /> 
                                            <col width="7%" />
                                            <col width="12%" />
                                            <col width="12%" />
                                            <col width="8%" />
                                            <col width="10%" /> 
                                            <col />
                                         </colgroup>
                                         <thead>
                                              <tr>
                                                    <th scope="row">기별</th>
                                                    <th scope="row">과정<br/>구분</th>
                                                    <th scope="row">운영<br/>형태</th>
                                                    <th scope="row">과정명</th>
                                                    <th scope="row">인원<br />
                                                      (명)</th>
                                                    <th scope="row">등록기간</th>
                                                    <th scope="row">연수기간</th>
                                                    <th scope="row">시간</th>
                                                    <th  scope="row">대상</th>
                                                    <th  scope="row" class="last">모바일<br/>지&nbsp;&nbsp;원</th>
                                              </tr>
                                         </thead>
                                         <tbody>
                                         
                                         
                                         
    <tr>
      <td>
      <p>1</p>      
      </td>
      <td>
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td>
      <p>특수교육대상학생의 국어과 교수․학습 지도</p>
      </td>
      <td>
      <p>100</p>
      </td>
      <td>
      <p>3.30-4.10</p>
      </td>
      <td >
      <p>4.20-5.8</p>
      </td>
      <td >
      <p>30(3주)</p>      </td>
      <td>
      <p>교원 및 교육전문직 등</p>
      </td>
      <td>
      <p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p>
      </td>
    </tr>
    <tr>
      <td>
      <p>2</p>      
      </td>
      <td>
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td>
      <p>특수교육대상학생의 수학과 교수․학습 지도</p>
      </td>
      <td>
      <p>100</p>
      </td>
      <td>
      <p>3.30-4.10</p>
      </td>
      <td >
      <p>4.20-5.8</p>
      </td>
      <td >
      <p>30(3주)</p>      </td>
      <td>
      <p>〃</p>
      </td>
      <td>
      <p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p>
      </td>
    </tr>
    <tr>
      <td rowspan="2">
      <p>3</p>
      </td>
      <td rowspan="2">
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>
      <td rowspan="2">
      <p><img src="/images/user/ischarge_02.png" alt="일반"/></p>      
      </td>
      <td rowspan="2">
      <p 
 >장애학생 이해와 통합학급 운영1,2(서울, 전남, 경남 특별과정)</p>      </td>
      <td rowspan="2"
 
 >
      <p 
 >1,650</p>      </td>
      <td rowspan="2"
 
 >
      <p 
 >3.30-4.10</p>      </td>
      <td
 
 >
      <p 
 >4.20-5.8</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td rowspan="2"
 >
      <p 
 >해당 시․도 통합학급교사</p>      </td>
 <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p> </td>
    </tr>
    <tr>
     <td
 
 >
      <p 
 >5.11-5.29</p>      </td>
 <td
 
 >
      <p 
 >30(3주)</p>      </td>
    </tr>
<tr>
      <td rowspan="2">
      <p>4</p>
      </td>
      <td rowspan="2">
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>
      <td rowspan="2">
      <p><img src="/images/user/ischarge_02.png" alt="일반"/></p>      
      </td>
      <td rowspan="2">
      <p 
 >장애학생 이해와 통합학급 운영1,2(경기 특별과정)</p>      </td>
      <td rowspan="2"
 
 >
      <p 
 >1,670</p>      </td>
      <td rowspan="2"
 
 >
      <p 
 >3.30-4.10</p>      </td>
      <td
 
 >
      <p 
 >4.20-5.8</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td rowspan="2"
 >
      <p 
 >해당 시․도 통합학급교사</p>      </td>
 <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p> </td>
    </tr>
    <tr>
     <td
 
 >
      <p 
 >5.11-5.29</p>      </td>
 <td
 
 >
      <p 
 >30(3주)</p>      </td>
    </tr>
    <tr>
      <td 
 
 >
      <p 
 >5</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육대상학생의 사회과 교수․학습 지도 </p>      </td>
      <td 
 
 >
      <p 
 >80</p>      </td>
      <td 
 
 >
      <p 
 >4.13-4.24</p>      </td>
      <td 
 
 >
      <p 
 >4.27-5.15</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td 
 
 >
      <p 
 >〃</p>      </td>
 <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
        <tr>
      <td 
 
 >
      <p 
 >6</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육대상학생의 과학과 교수․학습 지도 </p>      </td>
      <td 
 
 >
      <p 
 >80</p>      </td>
      <td 
 
 >
      <p 
 >4.13-4.24</p>      </td>
      <td 
 
 >
      <p 
 >4.27-5.15</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td 
 
 >
      <p 
 >〃</p>      </td>
 <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td 

 >
      <p 
 >7</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>      
      </td>
      <td 
 
 >
      <p 
 >치료교육 표시과목 변경 직무연수</p>      </td>
 
      <td 
 
 >
      <p 
 >미정</p>      </td>
      <td 
 >
      <p 
 >4.13-4.24</p>      </td>
      <td 
 
 >
        <p 
 >5.4-5.30</p></td>
      <td 
 
 >
        <p 
 >61(4주)</p></td>
      <td 
 >
      <p 
 >치료교육 표시과목 미변경자</p>      </td>
 <td></td>
    </tr>
    
    <tr>
      <td 
 
 >
      <p 
 >8</p>      </td>
    <td>
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>  
      </td>
      <td 
 
 >
      <p 
 >장애학생 이해와 통합학급 운영 </p></td>
      
      <td
 
 >
      <p 
 >80</p>      </td>
      <td
 
 >
      <p 
 >4.13-4.24</p>      </td>
      <td 
 
 >
      <p 
 >5.4-5.30</p>      </td>
      <td 
 
 >
      <p 
 >61(4주)</p>      </td>
      <td 
 
 >
      <p 
 >교원 및 교육전문직 등</p></td>
 <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td 
 
 >9</td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >특수교육교원 역량강화(신규) </td>
      <td 
 
 >100</td>
      <td 
 >4.13-4.23</td>
      <td 
 >5.4-5.30</td>
      <td 
 >61(4주)</td>
      <td 
 >〃 </td>
 <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td
 
 >
      <p 
 >10</p>      </td>
    <td>
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td
 
 >
      <p 
 >장애학생 진로․직업교육</p>      </td>
      <td
 
 >
      <p 
 >200</p>      </td>
      <td
 >
      <p 
 >5.4-5.15</p>      </td>
      <td
 >
      <p 
 >5.18-6.5</p>      </td>
      <td
 >
      <p 
 >30(3주)</p>      </td>
      <td
 >
      <p 
 >〃</p>      </td>
 <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p> </td>
    </tr>
    <tr>
      <td rowspan="2" 
 
 >
      <p 
 >11</p>      </td>
    <td rowspan="2" >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td rowspan="2" >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>      
      </td>
      <td  rowspan="2"
 
 >
      <p 
 >장애학생 이해와 통합학급 운영1,2(부산,광주,세종 특별과정)</p>      </td>
      <td  rowspan="2"
 
 >
      <p 
 >1,100</p>      </td>
      <td  rowspan="2"
 
 >
      <p 
 >5.4-5.15</p>      </td>
      <td 
 
 >
      <p 
 >5.25-6.12</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td  rowspan="2"
 
 >
      <p 
 >해당 시․도 통합학급교사</p>      </td>
  <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
    <td 
 
 >
      <p 
 >6.15-7.3</p>      </td>
 <td 
 
 >
      <p 
 >30(3주)</p>      </td>
    </tr>
    <tr>
      <td rowspan="2" 
 
 >
      <p 
 >12</p>      </td>
    <td rowspan="2" >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td rowspan="2" >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>      
      </td>
      <td  rowspan="2"
 
 >
      <p 
 >장애학생 이해와 통합학급 운영1,2(강원,경북,충북 특별과정)</p>      </td>
      <td  rowspan="2"
 
 >
      <p 
 >1,294</p>      </td>
      <td  rowspan="2"
 
 >
      <p 
 >5.18-5.29</p>      </td>
      <td 
 
 >
      <p 
 >6.8-6.26</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td  rowspan="2"
 
 >
      <p 
 >해당 시․도 통합학급교사</p>      </td>
  <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
    <td 
 
 >
      <p 
 >6.29-7.17</p>      </td>
 <td 
 
 >
      <p 
 >30(3주)</p>      </td>
    </tr>
    <tr>
      <td 
 
 >13</td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >장애학생 이해와 통합학급 운영1</td>
      <td 
 
 >50</td>
      <td 
 >5.18-5.29
</td>
      <td 
 >6.8-6.26
</td>
      <td 
 >30(3주)</td>
      <td 
 >교원 및 교육전문직 등
</td>
  <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td 
 
 >14</td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >장애학생 이해와 통합학급 운영2</td>
      <td 
 
 >50</td>
      <td 
 >5.18-5.29
</td>
      <td 
 >6.8-6.26
</td>
      <td 
 >30(3주)</td>
      <td 
 >교원 및 교육전문직 등
</td>
  <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td 
 
 >
      <p 
 >15</p>      </td>
      <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >장애학생 이해와 통합학급 운영
</p>      </td>
      <td 
 
 >
      <p 
 >80</p>      </td>
      <td 
 >
      <p 
 >5.18-5.29
</p>      </td>
      <td 
 >
      <p 
 >6.8-7.4
</p>      </td>
      <td 
 >
      <p 
 >61(4주)
</p>      </td>
      <td 
 >
      <p 
 >〃</p>      </td>
  <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td 
 
 >
      <p 
 >16</p>      </td>
      <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육교원 역량강화(신규)
</p>      </td>
      <td 
 
 >
      <p 
 >100</p>      </td>
      <td 
 >
      <p 
 >5.18-5.29
</p>      </td>
      <td 
 >
      <p 
 >6.8-7.4
</p>      </td>
      <td 
 >
      <p 
 >61(4주)
</p>      </td>
      <td 
 >
      <p 
 >〃</p>      </td>
  <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td 
 
 >
      <p 
 >17</p>      </td>
      <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육교원 상위자격 취득을 위한 사전연수
</p>      </td>
      <td 
 
 >
      <p 
 >미정</p>      </td>
      <td 
 >
      <p 
 >6.1-6.12
</p>      </td>
      <td 
 >
      <p 
 >6.22-7.10
</p>      </td>
      <td 
 >
      <p 
 >30(3주)
</p>      </td>
      <td 
 >
      <p 
 >희소과목 자격증 소지자
</p>      </td>
  <td></td>
    </tr>
    <tr>
      <td 
 
 >
      <p 
 >18</p>      </td>
      <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >장애인 등에 대한 특수교육법 및 관련법규의 이해
</p>      </td>
      <td 
 
 >
      <p 
 >150</p>      </td>
      <td 
 >
      <p 
 >6.1-6.12
</p>      </td>
      <td 
 >
      <p 
 >6.15-7.3
</p>      </td>
      <td 
 >
      <p 
 >30(3주)
</p>      </td>
      <td 
 >
      <p 
 >〃</p>      </td>
  <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td 
 
 >
      <p 
 >19</p>      </td>
      <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육관련 검사도구의 사용법
</p>      </td>
      <td 
 
 >
      <p 
 >100</p>      </td>
      <td 
 >
      <p 
 >6.1-6.12
</p>      </td>
      <td 
 >
      <p 
 >6.15-7.10
</p>      </td>
      <td 
 >
      <p 
 >45(4주)
</p>      </td>
      <td 
 >
      <p 
 >〃</p>      </td>
  <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td>
    </tr>
    <tr>
      <td rowspan="2" 

 >
      <p 
 >20</p>      </td>
    <td rowspan="2" >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td rowspan="2" >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>  
      </td>
      <td rowspan="2" 
 
 >
      <p 
 >장애학생 이해와 통합학급 운영1,2(서울, 부산 특별과정)</p></td>
      <td rowspan="2"  >
      <p >1,150</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >6.1-6.12
</p>      </td>
      <td 
 
 >
      <p 
 >6.22-7.10
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >해당 시․도 통합학급교사</p>      </td>
    <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >
      <p 
 >7.13-7.31</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
</tr>
    <tr>
      <td rowspan="2" 

 >
      <p 
 >21</p>      </td>
    <td rowspan="2" >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td rowspan="2" >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>  
      </td>
      <td rowspan="2" 
 
 >
      <p 
 >장애학생 이해와 통합학급
운영1,2(부산, 경남 특별과정)
</p></td>
      <td rowspan="2" 
 
 >
      <p 
 >1,300
</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >7.6-7.17
</p>      </td>
      <td 
 
 >
      <p 
 >7.27-8.14
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >해당 시․도 통합학급교사</p>      </td>
    <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >
      <p 
 >8.17-9.4
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
</tr>
<tr>
      <td rowspan="2" 

 >
      <p 
 >22</p>      </td>
    <td rowspan="2" >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td rowspan="2" >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>  
      </td>
      <td rowspan="2" 
 
 >
      <p 
 >장애학생 이해와 통합학급
운영1,2(경기 특별과정)
</p></td>
      <td rowspan="2" 
 
 >
      <p 
 >1,670
</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >7.6-7.17
</p>      </td>
      <td 
 
 >
      <p 
 >7.27-8.14
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >해당 시․도 통합학급교사</p>      </td>
    <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >
      <p 
 >8.17-9.4
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
</tr>
    <tr>
      <td
 
 >
      <p 
 >23</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td
 
 >
      <p 
 >장애학생 이해와 통합학급 운영1
</p>      </td>
      <td
 
 >
      <p 
 >50</p>      </td>
      <td
 >
      <p 
 >8.10-8.21
</p>      </td>
      <td
 >
      <p 
 >8.31-9.18
</p>      </td>
      <td
 >
      <p 
 >30(3주)
</p>      </td>
      <td
 >
      <p 
 >교원 및 교육전문직 등
</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p> </td></tr>
    <tr>
      <td
 
 >
      <p 
 >24</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td
 
 >
      <p 
 >장애학생 이해와 통합학급 운영2
 </p>      </td>
      <td
 
 >
      <p 
 >50</p>      </td>
      <td
 
 >
      <p 
 >8.10-8.21
</p>      </td>
      <td
 
 >
      <p 
 >8.31-9.18
 </p>      </td>
      <td
 
 >
      <p 
 >30(3주)
</p>      </td>
      <td
 
 >
      <p 
 >〃
</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p> </td></tr>
    <tr>
      <td
 
 >
      <p 
 >25</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td
 
 >
      <p 
 >장애학생 이해와 통합학급 운영
</p>      </td>
      <td
 
 >
      <p 
 >80</p>      </td>
      <td
 
 >
      <p 
 >8.31-9.11
</p>      </td>
      <td
 >
      <p 
 >9.21-10.17
</p>      </td>
      <td
 >
      <p 
 >61(4주)
</p>      </td>
      <td
 >
      <p 
 >〃</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p> </td></tr>
    <tr>
      <td
 
 >
      <p 
 >26</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td
 
 >
      <p 
 >특수교육교원 역량강화(신규)
</p>      </td>
      <td
 
 >
      <p 
 >100</p>      </td>
      <td
 
 >
      <p 
 >8.31-9.11
</p>      </td>
      <td
 
 >
      <p 
 >9.21-10.17
</p>      </td>
      <td
 
 >
      <p 
 >61(4주)
</p>      </td>
      <td
 
 >
      <p 
 >〃</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p> </td></tr>
    <tr>
      <td  

 >
      <p 
 >27</p>      </td>
    <td  >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>  
      </td>
      <td
 
 >
      <p 
 >특수교육대상학생의 국어과 교수․학습 지도
</p></td>
      <td
 
 >
      <p 
 >70</p>      </td>
      <td
 >
      <p 
 >8.31-9.11
</p>      </td>
      <td 
 
 >
      <p 
 >9.14-10.2
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td
 >
      <p 
 >〃
</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td

 >
      <p 
 >28</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td>
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>  
      </td>
      <td >
      <p >특수교육대상학생의 수학과 교수․학습 지도
</p>  </td>
      <td >
      <p >70</p>      </td>
      <td
 >
      <p 
 >8.31-9.11
</p>      </td>
      <td 
 
 >
      <p 
 >9.14-10.2
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td 
 >
      <p 
 >〃
</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >
      <p 
 >29</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육대상학생의 사회과 교수․학습 지도
 </p>      </td>
      <td 
 
 >
      <p 
 >60</p>      </td>
      <td 
 
 >
      <p 
 >9.21-10.2
</p>      </td>
      <td 
 
 >
      <p 
 >10.5-10.23
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)
</p>      </td>
      <td 
 
 >
      <p 
 >〃
</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >
      <p 
 >30</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육대상학생의 과학과 교수․학습 지도
</p>      </td>
      <td 
 
 >
      <p 
 >60</p>      </td>
      <td 
 
 >
      <p 
 >9.21-10.2
</p>      </td>
      <td 
 
 >
      <p 
 >10.5-10.23
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td 
 
 >
      <p 
 >〃</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 

 >
      <p 
 >31</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >장애학생 진로․직업교육</p>      </td>
      <td 
 
 >
      <p 
 >200</p>      </td>
      <td 
 >
      <p 
 >10.12-10.23</p>      </td>
      <td 
 
 >
      <p 
 >10.26-11.13</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td 
 >
      <p 
 >〃</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>


<tr>
      <td rowspan="2" 

 >
      <p 
 >32</p>      </td>
    <td rowspan="2" >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td rowspan="2" >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>  
      </td>
      <td rowspan="2" 
 
 >
      <p 
 >장애학생 이해와 통합학급 운영 1,2</p></td>
      <td rowspan="2"  >
      <p >800</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >9.14-9.25
</p>      </td>
      <td 
 
 >
      <p 
 >10.5-10.23
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >교원 및 교육전문직</p>      </td>
    <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >
      <p 
 >10.26-11.13</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
</tr>
 <tr>
      <td 

 >
      <p 
 >33</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육대상학생의 국어과 교수․학습지도</p>      </td>
      <td 
 
 >
      <p 
 >50</p>      </td>
      <td 
 >
      <p 
 >10.12-10.23</p>      </td>
      <td 
 
 >
      <p 
 >10.26-11.13</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td 
 >
      <p 
 >〃</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>   
<tr>
      <td 

 >
      <p 
 >34</p>      </td>
    <td >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p 
 >특수교육대상학생의 수학과 교수․학습지도</p>      </td>
      <td 
 
 >
      <p 
 >50</p>      </td>
      <td 
 >
      <p 
 >10.12-10.23</p>      </td>
      <td 
 
 >
      <p 
 >10.26-11.13</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td 
 >
      <p 
 >〃</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr> 
    

<tr>
      <td rowspan="2" 

 >
      <p 
 >35</p>      </td>
    <td rowspan="2" >
      <p><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></p>      
      </td>      
      <td rowspan="2" >
      <p><img src="/images/user/ischarge_02.png" alt="특별"/></p>  
      </td>
      <td rowspan="2" 
 
 >
      <p 
 >장애학생 이해와 통합학급 운영 1,2</p></td>
      <td rowspan="2"  >
      <p >800</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >10.05-10.16
</p>      </td>
      <td 
 
 >
      <p 
 >10.19-11.06
</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
      <td rowspan="2" 
 >
      <p 
 >교원 및 교육전문직</p>      </td>
    <td rowspan="2"><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >
      <p 
 >11.09-11.27</p>      </td>
      <td 
 
 >
      <p 
 >30(3주)</p>      </td>
</tr>
    
      
    <tr>
      <td colspan="4"
 >
      <p 
 >소 계(34개 과정)</p>      </td>
      <td
 >
      <p 
 > 13,544 
</p>      </td>
      <td
 >
      <p 
 >&nbsp;</p>      </td>
      <td
 >
      <p 
 >&nbsp;</p>      </td>
      <td
 >
      <p 
 >&nbsp;</p>      </td>
      <td
 >
      <p 
 >&nbsp;</p>      </td> 
      <td class="last"
 >
      <p 
 >&nbsp;</p>      </td>
    </tr>    
    
    
    
    
                                    	</tbody>
                            </table>                                    
                          </div>
                         
                         <!--  
                          <p class="m10">※ 위표에 제시되지 않은 1기,4기,8기, 14기, 19기(시도교육청 맞춤연수) ,15기, 16기 (집합연수의 일부 과목을 원격으로 운영) 과정별도 계획에 의해 추진됨 </p>
                          <p class="m10">※ 7기 치료교육 표시과목 변경 직무연수 과정은 별도 계획에 의해 추진됩니다. 연수대상자 명단은 별도로 요청할 계획임</p>
                          -->
                      </li>
                      <li class="frist">※ 연수대상 특별과정은 시도교육청 단위로 실시하는 위탁연수입니다.</li>
                      
                      
                      
                        <li class="m20"><strong>특수교육 보조인력 직무연수 :6과정, 총 2,100명</strong>
                          <div class="courseList">
                        	<table width="100%" summary="기별, 과정명, 인원, 등록기간, 연수기간, 시간, 대상으로 구성되어짐">
                        	<caption>특수교육 보조인력 직무연수 과정 및 총인원</caption>
                                         <colgroup>
                                            <col width="5%" />
                                            <col width="5%" />
                                            <col width="5%" />
                                            <col width="30%" /> 
                                            <col width="7%" />
                                            <col width="11%" />
                                            <col width="11%" />
                                            <col width="8%" />
                                            <col width="12%" /> 
                                            <col />
                                         </colgroup>
                                         <thead>
                                              <tr>
                                                    <th scope="row">기별</th>
                                                    <th scope="row">과정<br/>구분</th>
                                                    <th scope="row">운영<br/>형태</th>
                                                    <th scope="row">과정명</th>
                                                    <th scope="row">인원<br />
                                                      (명)</th>
                                                    <th scope="row">등록기간</th>
                                                    <th scope="row">연수기간</th>
                                                    <th scope="row">시간</th>
                                                    <th  scope="row">대상</th>
                                                    <th  scope="row" class="last">모바일<br/>지&nbsp;&nbsp;원</th>
                                              </tr>
                                         </thead>
                                         <tbody>
                                              
    <tr>
      <td >
      <p style="line-height: 120%;">1</p>      </td>
    <td ><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p style="line-height: 110%;">특수교육 보조인력 역량강화 기초과정
</p></td>
      <td 
 >
      <p style="line-height: 120%;">500</p>      </td>
      <td 
 >
      <p style="line-height: 130%;">3.30-4.10</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">4.13-5.1</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">30(3주)</p>      </td>
      <td 
 >
      <p style="line-height: 100%;">보조인력 및 일반</p></td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    
    <tr>
      <td 
 >
      <p style="line-height: 120%;">2</p>      </td>
    <td ><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></td>  
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 >
      <p style="line-height: 110%;">특수교육 보조인력 역량강화 심화과정 </p>      </td>
      <td 
 >
      <p style="line-height: 120%;">400</p>      </td>
      <td 
 >
      <p style="line-height: 130%;">3.30-4.10</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">4.13-5.1</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">30(3주)</p>      </td>
      <td 
 >
      <p>보조인력 및 일반</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 >
      <p style="line-height: 120%;">3</p>      </td>
    <td ><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></td>  
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 >
      <p style="line-height: 110%;">특수교육 보조인력 역량강화 기초과정</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">400</p>      </td>
      <td 
 >
      <p style="line-height: 130%;">6.1-6.12
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">6.15-7.3
      </p>      </td>
      <td 
 >
      <p style="line-height: 120%;">30(3주)</p>      </td>
      <td 
 >
      <p>보조인력 및 일반</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 >
      <p style="line-height: 120%;">4</p>      </td>
    <td ><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></td>  
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 >
      <p style="line-height: 110%;">특수교육 보조인력 역량강화 심화과정
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">450</p>      </td>
      <td 
 
 >
      <p style="line-height: 120%;">6.1-6.12
</p>      </td>
      <td 
 
 >
      <p style="line-height: 120%;">6.15-7.3
</p>      </td>
      <td 
 
 >
      <p style="line-height: 120%;">30(3주)</p>      </td>
      <td 
 
 >
      <p>보조인력 및 일반</p>      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >
      <p style="line-height: 120%;">5</p>      </td>
    <td ><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></td>  
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p style="line-height: 110%;">특수교육 보조인력 역량강화 기초과정
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">100</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">8.24-9.4
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">9.7-9.25
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">30(3주)</p>      </td>
      <td 
 >보조인력 및 일반</td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    
    <tr>
      <td 
 
 >
      <p style="line-height: 120%;">6</p>      </td>
    <td ><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></td>  
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p style="line-height: 110%;">특수교육 보조인력 역량강화 심화과정
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">250</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">8.24-9.4
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">9.7-9.25
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">30(3주)</p>      </td>
      <td 
 >보조인력 및 일반</td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
 
 <tr>
      <td 
 
 >
      <p style="line-height: 120%;">7</p>      </td>
    <td ><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></td>  
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p style="line-height: 110%;">특수교육 보조인력 역량강화 기초과정
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">100</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">10.12-10.23</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">10.26-11.13</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">30(3주)</p>      </td>
      <td 
 >보조인력 및 일반</td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>  

<tr>
      <td 
 
 >
      <p style="line-height: 120%;">8</p>      </td>
    <td ><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></td>  
      <td >
      <p><img src="/images/user/ischarge_01.png" alt="정규"/></p>      
      </td>
      <td 
 
 >
      <p style="line-height: 110%;">특수교육 보조인력 역량강화 심화과정
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">100</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">10.12-10.23
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">10.26-11.13
</p>      </td>
      <td 
 >
      <p style="line-height: 120%;">30(3주)</p>      </td>
      <td 
 >보조인력 및 일반</td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>      
    
    <tr>
      <td colspan="4"
 >
      <p style="line-height: 120%;">소 계</p>      </td>
      <td
 >
      <p style="line-height: 120%;">2,300</p>      </td>
      <td
 ></td>
      <td
 ></td>
      <td
 ></td>
       <td
 ></td>
      <td  class="last"
 ></td>
    </tr>
                                    	</tbody>
                            </table>                                                                      
                          </div>                		
                      </li>
                      
                      
                      
                      
                      
                      <!-- <li class="m20"><strong>학부모연수 : 3과정, 총 300명</strong>
					    <div class="courseList">
 						   <table width="100%" summary="기별, 과정명, 인원, 등록기간, 연수기간, 시간, 대상으로 구성되어짐">
 						   <caption>학부모연수 과정 및 총인원</caption>
 						     <colgroup>
                                            <col width="5%" />
                                            <col width="5%" />
                                            <col width="5%" />
                                            <col width="32%" /> 
                                            <col width="7%" />
                                            <col width="10%" />
                                            <col width="10%" />
                                            <col width="8%" />
                                            <col width="12%" /> 
                                            <col />
                                         </colgroup>
                                         <thead>
                                              <tr>
                                                    <th scope="row">기별</th>
                                                    <th scope="row">과정<br/>구분</th>
                                                    <th scope="row">운영<br/>형태</th>
                                                    <th scope="row">과정명</th>
                                                    <th scope="row">인원<br />
                                                      (명)</th>
                                                    <th scope="row">등록기간</th>
                                                    <th scope="row">연수기간</th>
                                                    <th scope="row">시간</th>
                                                    <th  scope="row">대상</th>
                                                    <th  scope="row" class="last">모바일<br/>지&nbsp;&nbsp;원</th>
                                              </tr>
                                         </thead>
 						     <tbody>
 						       
    <tr>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">1</p>      </td>
    <td><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>  
      <td>
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>      
      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">학부모가 알아야할
특수교육개론</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">100</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">4.21-5.2</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">5.5-5.23</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">30(3주)</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">학부모</p>      </td>
    <td> </td></tr>
    <tr>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">2</p>      </td>
    <td><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>  
      <td>
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>      
      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">가정에서의 장애학생 지원
방안</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">100</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">6.23-7.4</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">7.7-7.25</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">30(3주)</p>      </td>
      <td
 >
      <p class="0" style="text-align: center;">〃</p>      </td>
    <td> </td></tr>
    <tr>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">3</p>      </td>
    <td><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>  
      <td>
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>      
      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">학부모가 알아야할
치료지원 서비스</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">100</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">9.29-10.10</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">10.13-10.31</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">30(3주)</p>      </td>
      <td
 >
      <p class="0" style="text-align: center;">〃</p>      </td>
    <td> </td></tr>
    <tr>
      <td colspan="4"
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">소 계</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">300</p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">&nbsp; </p>      </td>
      <td
 >
      <p class="0"
 style="line-height: 120%; text-align: center;">&nbsp; </p>      </td>
      <td
 >
       <p class="0"
 style="line-height: 120%; text-align: center;">&nbsp; </p>      </td>
       <td
 >
       <p class="0"
 style="line-height: 120%; text-align: center;">&nbsp; </p>      </td>
      <td class="last"
 >

    </tr>
					         </tbody>
					       </table>
					    </div>                		
                      </li>  -->
                      
                      <li class="m20"><strong>기타연수 : 11과정, 총 1,700명</strong>
					    <div class="courseList">
                        	<table width="100%" summary="기별, 과정명, 인원, 등록기간, 연수기간, 시간, 대상으로 구성되어짐">
                        	<caption>기타연수 과정 및 총인원</caption>
                                         <colgroup>
                                            <col width="5%" />
                                            <col width="5%" />
                                            <col width="5%" />
                                            <col width="30%" /> 
                                            <col width="7%" />
                                            <col width="12%" />
                                            <col width="12%" />
                                            <col width="8%" />
                                            <col width="10%" /> 
                                            <col />
                                         </colgroup>
                                         <thead>
                                              <tr>
                                                    <th scope="row">기별</th>
                                                    <th scope="row">과정<br/>구분</th>
                                                    <th scope="row">운영<br/>형태</th>
                                                    <th scope="row">과정명</th>
                                                    <th scope="row">인원<br />
                                                      (명)</th>
                                                    <th scope="row">등록기간</th>
                                                    <th scope="row">연수기간</th>
                                                    <th scope="row">시간</th>
                                                    <th  scope="row">대상</th>
                                                    <th  scope="row" class="last">모바일<br/>지&nbsp;&nbsp;원</th>
                                              </tr>
                                         </thead>
                                         <tbody>
                                              

    <tr>
      <td 
 
 >
 1      </td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>      
      </td>
      <td 
 
 >
 수화기초과정      </td>
      <td 
 
 >
200      </td>
      <td 
 
 >
 3.30-4.10
     </td>
      <td 
 
 >
 4.13-4.24
      </td>
      <td 
 
 >
15(2주)      </td>
      <td 
 
 >
교원 및 교육전문직 등
      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 >
2      </td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 
 >
 시각장애학생 보행훈련      </td>
      <td 
 >
200      </td>
      <td 
 >3.30-4.10
</td>
      <td 
 >4.13-4.24
 </td>
      <td 
 >
15(2주)      </td>
      <td 
 >
      〃      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 >
3      </td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 
 >
장애학생의 인권과 성교육      </td>
      <td 
 >
200      </td>
      <td 
 >
 4.13-4.24
     </td>
      <td 
 >
4.27-5.8
      </td>
      <td 
 >
15(2주)      </td>
      <td 
 >
      〃      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 >4</td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 
 >장애학생을 위한 특수교육지원능력 개발 </td>
      <td 
 >100</td>
      <td 
 
 >4.13-4.24
</td>
      <td 
 
 >4.27-5.8
</td>
      <td 

 >20(2주) </td>
      <td 

 >〃</td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >5</td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 

 >수화기초과정
</td>
      <td 
 
 >200 </td>
      <td 
 
 >9.28-10.9
</td>
      <td 
 
 >10.12-10.23
</td>
      <td 

 >15(2주)
 </td>
      <td 

 >교원 및 교육전문직 등
</td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >6</td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 

 >
시각장애학생 보행훈련
      </td>
      <td 
 
 >
200      </td>
      <td 
 
 >
9.28-10.9
     </td>
      <td 
 
 >
10.12-10.23
      </td>
      <td 

 >
15(2주)      </td>
      <td 

 >
      〃      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >7</td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 

 >
장애학생의 인권과 성교육
    </td>
      <td 
 
 >
200      </td>
      <td 
 
 >10.12-10.23
 </td>
      <td 
 
 >10.26-11.6
</td>
      <td 

 >
15(2주)      </td>
      <td 

 >
      〃      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >8</td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 

 >
장애학생을 위한 특수교육지원능력 개발
      </td>
      <td 
 
 >
100      </td>
      <td 
 
 >10.12-10.23
</td>
      <td 
 
 >10.26-11.6
</td>
      <td 

 >
20(2주)
      </td>
      <td 

 >
      〃      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>    
    <tr>
      <td 
 >9</td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 
 >
학부모가 알아야할 특수교육개론      </td>
      <td 
 >
100      </td>
      <td 
 
 >
3.16-3.27
      </td>
      <td 
 
 >
3.30-4.17
      </td>
      <td 

 >
30(3주)
      </td>
      <td 

 >
      학부모 등     </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 > 10</td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 

 >
가정에서의 장애학생 지원 방안      </td>
      <td 
 
 >
100      </td>
      <td 
 
 >6.22-7.3
</td>
      <td 
 
 >7.6-7.24
</td>
      <td 

 >
30(3주)
      </td>
      <td 

 >
      〃      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    <tr>
      <td 
 
 >11</td>
    <td ><img src="/images/user/upperclass_03.gif" alt="일반연수"/></td>      
      <td >
      <p><img src="/images/user/ischarge_03.png" alt="무료"/></p>            
      </td>
      <td 

 >
학부모가 알아야할 치료지원 서비스
      </td>
      <td 
 
 >
100      </td>
      <td 
 
 >
9.28-10.9
   </td>
      <td 
 
 >
10.12-10.30
    </td>
      <td 

 >
30(3주)
     </td>
      <td 

 >
      〃      </td>
    <td><p><img src="/images/user/ico_mobile.gif" alt="모바일지원"/></p></td></tr>
    
    <tr>
      <td colspan="4"
 >
      소 계      </td>
      <td
 >
    1,700      </td>
      <td
 >&nbsp;      </td>
      <td
 >&nbsp;      </td>
      <td
 >&nbsp;      </td>
      <td
 >&nbsp;      </td>
       <td class="last"
 >&nbsp;      </td>
    </tr>
                                    	</tbody>
                           </table> 
                           
                           
                           </div>                		
                      </li>                                                           
                    </ul> 
                    <br>
<!--                    <table border="0" bgcolor="#FFFFFF" height="20">-->
<!--                      <tr>-->
<!--                        <td width="35">&nbsp;</td>-->
<!--                        <td width="13"><strong>※</strong></td>-->
<!--                        <td width="2">&nbsp;</td>-->
<!--                        <td width="20" >&nbsp;</td>-->
<!--                        <td width="5">&nbsp;</td>-->
<!--                        <td><strong>: 모바일일 기기 활용 가능 연수</strong></td>-->
<!--                      </tr>-->
<!--                    </table>-->
                                   
</div>
           
                    
                    
           
<div class="sub_txt_1depth m40">
      <h4>교육과정 편성비율</h4>
      <ul>
        <li class="frist">
          <div class="courseList">
            <table width="100%" summary="종별, 영역, 과목수, 시간수, 비율(%)로 구성되어짐">
            <caption>교육과정 편성비율</caption>
              <colgroup>
                <col width="25%" />
                <col width="15%" />
                <col width="15%" />
                <col width="15%" />
                <col width="15%" />
                <col width="15%" />
              </colgroup>
              <thead>
                <tr>
                  <th colspan="2" scope="col">종별</th>
                  <th scope="col">영역</th>
                  <th scope="col">과목수</th>
                  <th scope="col">시간수</th>
                  <th class="last" scope="col">비율(%)</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td rowspan="8">교원직무연수</td>
                  <td rowspan="4">61(4주)</td>
                  <td>교양</td>
                  <td>2</td>
                  <td>6</td>
                  <td class="last">10</td>
                </tr>
                <tr>
                  <td>교직·전공</td>
                  <td>19 </td>
                  <td>54</td>
                  <td class="last">88</td>
                </tr>
                <tr>
                  <td>출석 평가</td>
                  <td>·</td>
                  <td>1</td>
                  <td class="last">2</td>
                </tr>
                <tr>
                  <td>합 계</td>
                  <td>21</td>
                  <td>61</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td rowspan="2">45(4주)</td>
                  <td>교직.전공</td>
                  <td>15</td>
                  <td>45</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td>합 계</td>
                  <td>15</td>
                  <td>45</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td rowspan="2">30(3주)</td>
                  <td>교직.전공</td>
                  <td>10</td>
                  <td>30</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td>합 계</td>
                  <td>10</td>
                  <td>30</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td rowspan="2">특수교육<br/>
                    보조인력연수</td>
                  <td rowspan="2">30(3주)</td>
                  <td>직무.전공</td>
                  <td>10</td>
                  <td>30</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td>합계</td>
                  <td>10</td>
                  <td>30</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td rowspan="4" >기타연수</td>
                  <td rowspan="2">30(3주)</td>
                  <td>직무.전공</td>
                  <td>10</td>
                  <td>30</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td>합계</td>
                  <td>10</td>
                  <td>30</td>
                  <td class="last">100</td>
                </tr>
                
                
				<tr>
                  <td rowspan="2">15(2주)</td>
                  <td>직무.전공</td>
                  <td>5</td>
                  <td>15</td>
                  <td class="last">100</td>
                </tr>
                <tr>
                  <td>합계</td>
                  <td>5</td>
                  <td>15</td>
                  <td class="last">100</td>
                </tr>
                
                                
                
              </tbody>
            </table>
          </div>
        </li>
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