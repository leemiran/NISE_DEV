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
    <legend>문의안내</legend>
    <%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
    <div class="sub_txt_1depth">
      <h4>조직안내</h4>
      <img src="/images/user/organization_chart.gif">
      <h4>원격연수운영팀</h4>        
      <div>
        - 팩&nbsp;&nbsp;&nbsp;스 : 041-537-1473</br>
        - 이메일 : iedu@moe.go.kr
      </div>  
      <h4>원격연수운영팀</h4>
      <ul>
        <li class="frist">
          <div class="courseList">
            <table width="100%" summary="부서(팀)명, 성명, 직위(직급), 담당업무, 전화로 구성">
            <caption>원격연수운영팀</caption>
              <colgroup>
                <col width="20%" />
                <col width="20%" />
                <col width="20%" />
                <col width="20%" />
                <col width="20%" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">부서(팀)명</th>
                  <th scope="col">성명</th>
                  <th scope="col">직위(직급)</th>
                  <th scope="col">담당업무</th>
                  <th scope="col">전화</th>
                </tr>
              </thead>
              <tbody>
              
                <!-- <tr>
                  <td rowspan="7">원격교육연수원</td>
                  <td>김태준</td>
                  <td>팀장(교육연구관)</td>
                  <td>총괄</td>
                  <td>041-537-1505</td>
                </tr>
                <tr>
                  <td>강성구</td>
                  <td>교육연구사</td>
                  <td>기획</td>
                  <td>041-537-1474</td>
                </tr> -->
                <tr>
                  <td rowspan="7">원격교육연수원</td>                  
                  <td>안수경</td>
                  <td>팀장(교육연구관)</td>
                  <td>총괄·기획</td>
                  <td>041-537-1540</td>
                </tr>
                <tr>
                  <td>김미현</td>
                  <td>주무관</td>
                  <td>운영 및 평가</td>
                  <td>041-537-1477</td>
                </tr>
                <tr>
                  <td>안경화</td>
                  <td>주무관</td>
                  <td>수납 및 등재</td>
                  <td>041-537-1478</td>
                </tr>
                <tr>
                  <td>이진이</td>
                  <td>주무관</td>
                  <td>시스템관리</td>
                  <td>041-537-1479</td>
                </tr>
                <tr>
                  <td>김남희</td>
                  <td>주무관</td>
                  <td>콘텐츠개발</td>
                  <td>041-537-1476</td>
                </tr>
                <tr>
                  <td>박보람</td>
                  <td>주무관</td>
                  <td>홈페이지관리</td>
                  <td>041-537-1475</td>
                </tr>
              </tbody>
            </table>
          </div>
        </li>
      </ul>
      <h4>기술문의 및 원격지원</h4>        
      <div>
        - 전&nbsp;&nbsp;&nbsp;화 : 02-6345-6787</br>
        - 이메일 : coid@coidinc.com 
      </div>
        
      
    </div>
  </fieldset>
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
