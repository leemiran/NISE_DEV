<%@ page pageEncoding="UTF-8"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"		value="${p_contenttype}"/>
<input type="hidden" name="p_subj"				value="${p_subj}"/>
<input type="hidden" name="p_year"				value="${p_year}"/>
<input type="hidden" name="p_subjseq"			value="${p_subjseq}"/>
<input type="hidden" name="p_studytype"			value="${p_studytype}"/>
<input type="hidden" name="p_process"			value="${p_process}"/>
<input type="hidden" name="p_next_process"		value="${p_next_process}"/>
<input type="hidden" name="p_height"			value="${p_height}"/>
<input type="hidden" name="p_width"				value="${p_width}"/>
<input type="hidden" name="p_lcmstype"			value="${p_lcmstype}"/>
<input type="hidden" name="p_menu"				value="${p_menu}"/>
<input type="hidden" name="p_tabseq"			value="${p_tabseq}"/>
<input type="hidden" name="studyPopup"  		value="${studyPopup}" />
<input type="hidden" name="preview"				value="${preview}"/>
<input type="hidden" name="pageType"  			value="pop" />

<input type="hidden" name="p_review_study_yn"  			value="${p_review_study_yn}" />
</form>
<c:if test="${empty studyPopup }">
			<div class="myheader_n">
				<p class="mytit">나의강의실</p>
				<ul class="mytab_n">
				<c:if test="${sessionScope.gadmin eq 'Q1' }">
					<li><a href="/usr/stu/std/userStudyItemList.do" onclick="changePage('02');return false;"><span>학습시작</span></a></li>				
				</c:if>
				
				<c:if test="${sessionScope.gadmin ne 'Q1' }">
					<li><a href="/usr/stu/std/userStudyHome.do" onclick="changePage('01');return false;"><span>홈</span></a></li>
                    <li><a href="/usr/stu/std/userStudyItemList.do" onclick="changePage('02');return false;"><span>학습시작</span></a></li>
                    <li><a href="/usr/stu/std/userStudyExamList.do" onclick="changePage('03');return false;"><span>시험</span></a></li>
                    <li><a href="/usr/stu/std/userStudySurveyList.do" onclick="changePage('04');return false;"><span>설문</span></a></li>
                    <li><a href="/usr/stu/std/userStudyReportList.do" onclick="changePage('05');return false;"><span>과제</span></a></li>
                    <li><a href="/com/pop/courseScoreListPopup.do" onclick="changePage('06');return false;"><span>성적보기</span></a></li>				
				</c:if>				
										
				</ul>
			</div>
</c:if>
<script type="text/javascript">

	function changePage(menu) {
		var frm = eval('document.studyForm');
		frm.action = getPageURL(menu);
		frm.p_menu.value = menu;
		frm.p_tabseq.value = "";
		frm.target = "_self";
		frm.submit();
	}

	function getPageURL(menu){
		var url = "";
		switch(menu){
			case '01': //홈
				url = "/usr/stu/std/userStudyHome.do";
			break;
			
			case '02': //학습시작목록
				url = "/usr/stu/std/userStudyItemList.do";
			break;
			
			case '03': //시험
				url = "/usr/stu/std/userStudyExamList.do";
			break;
			
			case '04': //설문
				url = "/usr/stu/std/userStudySurveyList.do";
			break;
			
			case '05': //과제
			url = "/usr/stu/std/userStudyReportList.do";
			break;

			case '06': //성적보기
			url = "/com/pop/courseScoreListPopup.do?p_studymode=only";
			break;
		
			case '10': //공지사항
				url = "/usr/stu/std/userStudyNoticeList.do";
			break;
			
			case '11': //자료실
				url = "/usr/stu/std/userStudyDataList.do";
			break;
			
			case '12': //Q&A
				url = "/usr/stu/std/userStudyQnaList.do";
			break;


			default: //홈
				url = "/usr/stu/std/userStudyHome.do";
			break;
		}

		return url;
	}

	function fn_download(realfile, savefile, dir){
		var url = "/com/commonFileDownload.do?";
		url += "realfile="+realfile;
		url += "&savefile="+savefile;
		url += "&dir="+dir;
		window.location.href = url;
		return;
	}
	
</script>