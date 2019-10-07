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
    <legend>학습방법</legend>
    <%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
    <div class="sub_txt_1depth">
      <!-- <h4>학습방법</h4> -->
      <ul>
        <!-- <li class="mrb20">각 과정별 교육은 원격강의로 이루어지며, 필요시 보조방법으로 출석수업을 실시할 수 있습니다.<br/>
          또한 각 과정별로 전담강사와 강의운영자가 배치되어 연수생의 첨삭 지도와 과제, 시험, 토론, 
          발표 등을 진행하며 수강생들의 질문에 24시간 이내에 답변합니다</li> -->
        <li class="frist mrb20">
          <!-- <p class="font_red"><strong>1) 원격교육연수원에서의 학습</strong></p> -->
          <ul class="table_txt_3depth" style="border:1px solid #bababa;width:98%;">
            <li>강의파일은 강의실에 전체 탑재되어 학습 전/후로 예습 및 복습을 할 수 있습니다.</li>
            <li>강의는 연수기간 동안 휴일 없이 24시간 진행됩니다.</li>
            <li>연수종료 후 1년간 추가로 복습 하실 수 있습니다.</li>
          </ul>
        </li>
        <li class="frist mrb20">
          <p class="font_red"><strong>● 컴퓨터 및 프로그램 권장사양</strong></p>
          <ul class="table_txt_3depth">
            <li>cpu 팬티엄급 이상, 운영시스템 Windows 7 이상, 인터넷회선, 사운드카드, 스피커(또는 헤드폰), 인터넷</li>
            <li>익스플로러 11권장(그 외 학습에 필요한 프로그램은 별도 설치팝업이 뜨니 설치하시기 바랍니다.)</li>
            <li>해상도는 1024X768의 이상의 해상도를 기준으로 설계되었습니다. 1024X768의 이하의 해상도에서는 학습기능을 원활히 사용 할 수 없습니다.</li>
          </ul>
        </li>
        <li class="frist mrb20">
          <p class="font_red"><strong>● 나의강의실 학습하기 세부메뉴 설명</strong></p>
          <ul class="table_txt_3depth">
            <li><strong>강의실 공지사항</strong> : 연수일정과 관계된 모든 전달사항이 공지됩니다.(필독)</li>
            <li><strong>학습 시작</strong> : 신청 승인된 연수가 등록이 되어있습니다. 학습 시작에서 수강하시면 됩니다.</li>
            <li><strong>온라인평가</strong> : 진도율 90% 이상 시 시험에 응시하실 수 있습니다.</li>
            <li><strong>설문</strong> : 강의내용에 대한 평가 및 연수과정 관련 설문조사입니다. </li>
            <li><strong>과제</strong> : 60시간 이상 과정일 경우 과제제출기간에 제출하실 과제를 제출하는 메뉴입니다.</li>
            <!-- <li><strong>영수증/이수증 출력</strong> : 수강완료후 연수비 영수증과 이수증을 출력합니다.</li> -->
            <li><strong>성적보기</strong> : 연수와 관련해서 평가된 점수를 조회합니다.</li>
          </ul>
        </li>
      </ul>
    </div>
  </fieldset>
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

