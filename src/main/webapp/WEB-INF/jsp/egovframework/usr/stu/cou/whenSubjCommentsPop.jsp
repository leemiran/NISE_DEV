<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<link rel="stylesheet" type="text/css" href="/css/user/popup.css" />
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript">
//<![CDATA[
<c:if test="${!empty resultMsg}">alert("${resultMsg}");opener.docuemnt.location.reload();window.close();</c:if>
//]]>
</script>

<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" action="">
<input type="hidden" value="${p_userid}" name="p_userid" />
<input type="hidden" value="${p_year}" name="p_year"/>
<input type="hidden" value="${p_subjseq}" name="p_subjseq"/>
<input type="hidden" value="" name="p_seq"/>
<input type="hidden" value="${p_subj}" name="p_subj"/>
<input type="hidden" value="${p_subjnm}" name="p_subjnm"/>
<input type="hidden" value="${pageIndex}" name="pageIndex"/>
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<!-- header -->
			<div class="tit_bg">
			<h2>과정 학습후기</h2>
       		</div>
			<!-- //header -->
			<!-- contents -->
			<div class="mycon">				
            	<div class="sub_text">
                    <h4>함께하는 특수교육</h4>
                </div>
                
                <div class="commentWrite">				
				<span>의견남기기</span>
				<textarea name="p_comments" style="width:76%;height:44px;"></textarea>
				<a href="#none" onclick="whenSubmit()">
					<input type="image" src="/images/user/commentInput.gif" alt="입력" style="width:66px; height:51px; border:0px;"/>
				</a>
			</div>
			<div class="commentView">
				<ul>
				<c:forEach items="${list}" var="result" varStatus="i">
					<li class="first-child">
						<span class="writer">${result.name}</span>
                        <span class="date">${result.ldate}</span>
                        <c:if test="${result.userid eq p_userid}">
						<a href="#none" onclick="whenDelete('${result.seq}')">
						    <span class="btn">
						    	<img src="/images/user/btn_del.gif" alt="삭제" />
						    </span>
						</a>
                        </c:if>
						<span class="context">${result.comments}</span>					  
					</li>	
				</c:forEach>
              </ul>
			</div>
            <!-- 페이징 시작 -->
			<div class="paging">
				<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
			</div>
			<!-- 페이징 끝 -->
              <!-- button -->
              <ul class="btnCen">
                <li><a href="#" onclick="window.close()" class="pop_btn01"><span>닫기</span></a></li>                
              </ul>
              <!-- // button -->
		  </div>
			<!-- //contents -->
		</div>
	</div>
</div>
</form>
<script type="text/javascript">
//<![CDATA[
	// 등록
	function whenSubmit() {
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if (confirm("등록 하시겠습니까?")) {
			if (checkValue()) {
				frm.action = "/usr/stu/whenSubjCommentsInsert.do";
				frm.submit();
			}
		}
	}

	function doLinkPage(index){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.action = "/usr/stu/whenSubjComments.do";
		frm.pageIndex.value = index;
		frm.target = "_self";
		frm.submit();
	}

	// 유효성체크
	function checkValue() {
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var val = frm.p_comments.value;
		if (val.length < 20 ) {
			alert("교육후기는 20자 이상 입력해주세요.");
			return false;
		}
		return true;
	}

	// 삭제
	function whenDelete(p_seq) {
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		
		if (confirm("삭제 하시겠습니까?")) {
			frm.p_seq.value = p_seq;
			frm.action =  "/usr/stu/whenSubjCommentsDelete.do";
			frm.submit();
		}
		
	}
//]]>
</script>