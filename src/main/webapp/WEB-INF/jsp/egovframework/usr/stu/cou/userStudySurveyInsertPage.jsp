<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>

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

<input type="hidden" name="s_subj" value="ALL"/>
<input type="hidden" name="s_grcode" value="ALL"/>
<input type="hidden" name="p_grcode"/>
<input type="hidden" name="p_seq"/>
<input type="hidden" name="p_userid" value="${p_userid }"/>
<input type="hidden" name="p_sulpapernum" 	id="p_sulpapernum" 	value="${p_sulpapernum}" />

			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/cou/userStudyHidden.jsp" %>
				<p class="contit">
				
				</p>

<!--    public final static String OBJECT_QUESTION  = "1";  // 단일선택-->
<!--    public final static String MULTI_QUESTION   = "2";  // 복수선택     	-->
<!--    public final static String SUBJECT_QUESTION = "3";	// 서술형			-->
<!--	public final static String COMPLEX_QUESTION = "4";	// 복합형			-->
<!--    public final static String FSCALE_QUESTION = "5";	// 5점척도-->
<!--    public final static String SSCALE_QUESTION = "6";	// 7점척도-->

			<div class="mycon">
                <div class="sub_text">
                    <h4><c:out value="${p_subjnm}"/> >> 원격연수 만족도 조사</h4>
                </div>
                
                <div class="poll">
                <c:set var="sulIdx" value="1"/>
				<c:set var="tempSul" value=""/>
				<c:set var="tempSelCount" value="0"/>
				<c:forEach items="${PaperQuestionExampleList}" var="result"  varStatus="i">
				
					<c:if test="${tempSul ne result.sulnum}">
						<input type="hidden" id="checkVal" name="checkVal"/>
						<c:set var="tempSelCount" value="0"/>
						<p class="poll_tit">문제 <c:out value="${sulIdx}"></c:out> ${result.sultext}</p>
						<c:set var="sulIdx" value="${sulIdx+1}"/>
						<ul>
					</c:if>
					<c:set var="tempSelCount" value="${tempSelCount+1}"/>
					<c:choose>
						<c:when test="${result.sultype eq '2'}">
								<li><input name="${result.sulnum}" type="checkbox"  class="chr" onclick="setCheckVal(this.value, ${sulIdx-1})" value="${result.selnum}"/> ${result.selnum}) ${result.seltext }</li>
						</c:when>
						<c:when test="${result.sultype eq '3'}">
								<li><textarea name="${result.sulnum}<c:if test="${result.distcode eq '10'}">|10</c:if>" rows="8" style="width:98%" onblur="javascript:setCheckVal(this.value, ${sulIdx-1})"></textarea></li>
						</c:when>
						<c:when test="${result.sultype eq '4'}">
								<li><input name="${result.sulnum}" type="radio"  class="chr"  onclick="setCheckVal(this.value, ${sulIdx-1})" value="${result.selnum}"/> ${result.selnum}) ${result.seltext }</li>
							<c:if test="${tempSelCount eq result.selcount}">
								<li><textarea name="${result.sulnum}|C" rows="8" style="width:98%"></textarea></li>
							</c:if>
						</c:when>
						<c:otherwise>
								<li><input name="${result.sulnum}" type="radio"  class="chr"  onclick="setCheckVal(this.value, ${sulIdx-1})" value="${result.selnum}"/> ${result.selnum}) ${result.seltext }</li>
						</c:otherwise>
					</c:choose>
					<c:if test="${tempSelCount eq result.selcount}">
					</ul>
					</c:if>
					<c:set var="tempSul" value="${result.sulnum}"/>
				</c:forEach>
                </div>
                <div class="sub_text">
                    <h4><c:out value="${p_subjnm}"/> >> 교육 후기</h4>
                    <c:set var="sulIdx" value="${sulIdx+1}"/>
                </div>
                
                <div class="commentWrite">				
                    <span>${result.distcodenm}</span>
                    <textarea name="p_comments" id="p_comments" style="width:95%;height:80px;"></textarea>
				</div>
                
            
            <!-- button -->
            <ul class="btnCen">
                <li><a href="#" onclick="whenSubmit()" class="pop_btn01"><span>제출</span></a></li>                
            </ul>
			<!-- // button -->
                
                
		  </div>
		<!-- // right -->
		</form>
<script type="text/javascript">
//<![CDATA[
function setCheckVal(val, num){
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	var target = frm.checkVal;
	for( i=0; i<target.length; i++ ){
		if( num-1 == i ){
			target[i].value = val;
		}
	}
}

function whenSubmit(){
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	var target = frm.checkVal;
	for( i=0; i<target.length; i++ ){
		if( target[i].value == "" ){
			alert("응답하지 않은 설문이 있습니다.모든 설문에 응답해주시길 바랍니다.");
			return;
		}
	}
	if(frm.p_comments.value.length < 20 ) {
	  	alert("교육후기는 20자이상 입력하셔야 설문이 완료됩니다.");
	  	frm.p_comments.focus();
	  	return;
	}
	frm.action = "/usr/stu/std/userStudySurveyInsertData.do?";
	frm.target = "_self";
	frm.submit();
}
//]]>
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->