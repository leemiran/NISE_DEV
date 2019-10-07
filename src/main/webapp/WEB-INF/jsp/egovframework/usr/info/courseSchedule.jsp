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
    <legend>교과목 및 시간배당</legend>
    <%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
    <div class="sub_txt_1depth m40">
      <h4>교과목 및 시간배당</h4>
      <ul>
        <li class="frist mrb10">
          <select name="timeop" onchange="change_frame(this.value)" style="width:725px;" title="교과목선택">
            <option value="/html/usr/info/if_01" selected="selected">일반학교에서의 장애학생 지원(통합기초과정)</option>
            <option value="/html/usr/info/if_02">장애학생을 위한 특수교육지원능력 개발(통합심화과정)</option>
            <option value="/html/usr/info/if_03">장애학생 국어과 교수-학습지도</option>
            <option value="/html/usr/info/if_05">장애학생의 사회과 교수-학습지도</option>
            <option value="/html/usr/info/if_04">장애학생 수학과 교수-학습지도</option>
            <option value="/html/usr/info/if_06">장애인 등에 대한 특수교육법 및 관련 법규의 이해</option>
            <!--<option value="/html/usr/info/if_17">장애학생의 인권과 성교육</option>-->
            <option value="/html/usr/info/if_20">장애학생 이해와 통합학급 운영</option>
            <option value="/html/usr/info/if_07">개정 특수학교 교육과정 및 해설서 활용 방안</option>
            <option value="/html/usr/info/if_08">특수교육 관련 검사도구의 사용법</option>
            <option value="/html/usr/info/if_09">특수교육지원을 위한 보조자로서의 역활과 이해(특수교육보조인력 기초과정)</option>
            <option value="/html/usr/info/if_11">함께하는 특수교육(특수교육보조인력심화과정)</option>
            <option value="/html/usr/info/if_10">특수교육보조인력의 특수교육 지원능력 개발(특수교육보조인력 심화과정2)</option>
            <option value="/html/usr/info/if_12">가정에서의 장애학생지원 방안(학부모 연수 과정)</option>
            <option value="/html/usr/info/if_15">미래를 열어주는 장애학생의 진로직업교육(전문과정)</option>
            <option value="/html/usr/info/if_16">미래를 열어주는 장애학생의 진로직업교육(실무과정)</option>
            <option value="/html/usr/info/if_18">학부모가 알아야 할 특수교육개론</option>
            <option value="/html/usr/info/if_19">학부모가 알아야 할 치료지원 서비스</option>
            <option value="/html/usr/info/if_21">수학기초과정</option>
            <option value="/html/usr/info/if_22">장애학생 진로·직업교육</option>
            <option value="/html/usr/info/if_23">특수교육대상학생의 사회과 교수·학습지도</option>
            <option value="/html/usr/info/if_24">특수교육대상학생의 과학과 교수·학습지도</option>
            <option value="/html/usr/info/if_25">시각장애학생 보행훈련</option>
            <option value="/html/usr/info/if_26">장애학생의 인권과 성교육</option>
            <option value="/html/usr/info/if_27">장애학생 이해와 통합학급 운영 1, 2</option>
          </select>
        </li>
        <li class="frist">
          <iframe src="/html/usr/info/if_01.htm" class="scr" id="edutime" name="edutime"  width="725" height="500" frameborder="0" marginwidth="0" marginheight="0"  scrolling="yes" style="overflow-x:hidden;" title="교과목 및 시간배당 표"></iframe>
        </li>
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