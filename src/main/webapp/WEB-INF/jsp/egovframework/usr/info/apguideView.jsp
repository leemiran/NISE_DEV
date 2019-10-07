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
    <legend>모바일 연수안내</legend>
    <%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
    <div class="sub_txt_1depth ">
      <!-- <h4>모바일 연수안내</h4> -->
      <ul>
        <li class="frist mrb10">
  			<img src="/images/user/ap_guide_03.gif" alt="1. 안드로이드 구글 플레이에서 어플 다운로드방법 및 설치, 구글플레이-국립특수교육원 검색-어플 설치-어플리케이션 메인-교육과정 목차-나의학습방 학습하기" />
        </li>
        <li class="frist mrb10">
  			<img src="/images/user/ap_guide_06.gif" alt="1. 구글플레이에서 설치" />
  			<img src="/images/user/ap_guide_14.gif" alt="화살표" />
  			<img src="/images/user/ap_guide_08.gif" alt="2. 어플리케이션 실행" />
  			<img src="/images/user/ap_guide_14.gif" alt="화살표" />
  			<img src="/images/user/ap_guide_10.gif" alt="3. 교육과정 목차" />
  			<img src="/images/user/ap_guide_14.gif" alt="화살표" />
  			<img src="/images/user/ap_guide_12.gif" alt="4. 나의학습방 학습하기" />
        </li>
         <li class="frist m40">
  			<img src="/images/user/ap_guide_20.gif" alt="2. 아이폰 앱스토어에서 어플 다운로드방법 및 설치, 앱스토어-국립특수교육원 검색-앱 설치-앱 메인-교육과정 목차-나의학습방 학습하기" />
        </li>
        <li class="frist mrb10">
  			<img src="/images/user/ap_guide_23.gif" alt="1. 앱스토어에서 어플 설치" />
  			<img src="/images/user/ap_guide_14.gif" alt="화살표" />
  			<img src="/images/user/ap_guide_25.gif" alt="2. 앱 메인" />
  			<img src="/images/user/ap_guide_14.gif" alt="화살표" />
  			<img src="/images/user/ap_guide_27.gif" alt="3. 교육과정 목차" />
  			<img src="/images/user/ap_guide_14.gif" alt="화살표" />
  			<img src="/images/user/ap_guide_28.gif" alt="4. 나의학습방 학습하기" />
        </li>
        <!-- 
        <li class="frist">
          <iframe src="/html/usr/info/if_01.htm" class="scr" id="edutime" name="edutime"  width="725" height="500" frameborder="0" marginwidth="0" marginheight="0"  scrolling="yes" style="overflow-x:hidden;" title="교과목 및 시간배당 표"></iframe>
        </li>
         -->
      </ul>
    </div>
  </fieldset>
</form>


<script type="text/javascript">
//<![CDATA[
	function change_frame(v)
	{
		edutime.location.href = v + '.htm';
	}
//]]>
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->