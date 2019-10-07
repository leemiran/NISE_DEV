<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");opener.docuemnt.location.reload();window.close();</c:if>
-->
</script>



<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_userid" 	id="p_userid"		value="${p_userid}">
	<input type="hidden" name="p_class" id="p_class" value="${p_class}">
	<input type="hidden" name="p_ordseq" id="p_ordseq" value="${p_ordseq}">
	<input type="hidden" name="p_weeklyseq" id="p_weeklyseq" value="${selectViewOrderStu.weeklyseq}">
	<input type="hidden" name="p_weeklysubseq" id="p_weeklysubseq" value="${selectViewOrderStu.weeklysubseq}">
				
	<input type="hidden" name="p_submitType" id="p_submitType" value="in">	
				
	
	
	
					
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>과제</h2>
         </div>
				
				<div class="tbDetail">
					<table summary="과제" cellspacing="0" width="100%">
						<caption>과제</caption>
						<colgroup>
							<col width="20%" />
							<col />
							<col width="15%" />
							<col width="20%" />
						</colgroup>
						<tbody>
							
							<tr class="title">
								<th scope="col">제목</th>
								<td colspan="3">${selectViewOrderStu.title}</td>
							</tr>
							<tr>
								<th scope="col">제출기한</th>
								<td>${fn2:getFormatDate(selectViewOrderStu.startdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(selectViewOrderStu.expiredate, 'yyyy.MM.dd')}</td>
								<th scope="col">제출방법</th>
								<td>${selectViewOrderStu.reptype eq 'P' ? '개인별' : '팀별'}</td>
							</tr>
							<tr>
								<th scope="col">추가제출기한</th>
								<td>${fn2:getFormatDate(selectViewOrderStu.restartdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(selectViewOrderStu.reexpiredate, 'yyyy.MM.dd')}</td>
								<th scope="col">상태</th>
								<td>${selectViewOrderStu.submityn eq 'Y' ? '제출' : '미제출'}</td>
							</tr>
							<tr class="title">
								<th scope="col">내용/주의사항</th>
								<td colspan="3">
									<c:out value="${selectViewOrderStu.contents}" />
								</td>
							</tr>
							<tr>
								<th scope="col">제출양식</th>
								<td colspan="3">${text_filetype}</td>
							</tr>
							<tr class="title">
								<th scope="col">첨부파일</th>
								<td colspan="3">
									<c:if test="${empty selectProfFiles}">첨부파일이 없습니다.</c:if>
			                    	<c:if test="${not empty selectProfFiles}">
			                    		<c:forEach items="${selectProfFiles}" var="file">
					                    <a href="#none" onclick="javascript:fn_download('${file.realfile}', '${file.newfile}', 'reportprof')"><c:out value="${file.realfile}"/></a>
					                    <input type="hidden" name="p_savefile"  value="<c:out value="${file.newfile}"/>" />
					                    <br/>
			                    		</c:forEach> 
			                    	</c:if>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<div class="tit_bg">
					<h2>과제 제출</h2>
		         </div>
				<div class="tbDetail">
					<table summary="과제" cellspacing="0" width="100%">
						<caption>과제</caption>
						<colgroup>
							<col width="20%" />
							<col width="" />
						</colgroup>
						<tbody>
							<tr class="title">
								<th scope="col">제목</th>
								<td>
									<input name="p_title" type="text" class="text" style="width:95%" value="${profData.title}"/>
								</td> 
							</tr>
							<tr class="title" style="display:none;">
								<th scope="col">내용</th>
								<td>
									<textarea name="p_contents" rows="10" scrollbar="no" style="width:100%;height:90px;">${profData.contents}</textarea>
								</td> 
							</tr>
							<tr class="title">
								<th scope="col">이름(아이디)</th>
								<td >${selectViewOrderStu.name} (${p_userid})</td>
							</tr>
							<tr class="title">
								<th scope="col">제출일자</th>
								<td>
								<input type="text" value="${fn2:getFormatDate(profData.ldate, 'yyyy-MM-dd')}" size="10" name="p_ldate" maxlength="10" readonly="readonly"/>
								<a href="#none" onclick="popUpCalendar(this, document.all.p_ldate, 'yyyy-mm-dd')"><img src="/images/adm/ico/ico_calendar.gif" alt="달력" align="absmiddle"/></a>
								<font color="red">※ 제출일자를 입력하지 않을시에는 현재날짜로 등록됩니다.</font>
								</td>
							</tr>
							<tr class="title">
								<th scope="col">파일</th>
								<td>
									<c:if test="${not empty profData.realfile}">
										<a href="#none" onclick="javascript:fn_download('${profData.realfile}', '${profData.newfile}', 'reportstu/${p_year}/${p_subjseq}/${sessionScope.grcode}/${p_subj}/${p_class}/${p_ordseq}')"><c:out value="${profData.realfile}"/></a>
					                    <input type="checkbox"  name="p_filedel" value="<c:out value="1"/>"> (삭제시 체크)<br>
										</br>
										<input type="hidden" name="p_realfile1"  value="<c:out value="${profData.realfile}"/>">
										<input type="hidden" name="p_savefile1"  value="<c:out value="${profData.newfile}"/>">
										<input type="hidden" name="p_fileseq1"  value="<c:out value="1"/>">
									</c:if>
									<input type="file" name="p_files" style="width:96%"/>
									<input type="hidden" name="p_upload_dir" value="reportstu/${p_year}/${p_subjseq}/${sessionScope.grcode}/${p_subj}/${p_class}/${p_ordseq}" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>
	</div>

</div>				
				</form>
				<ul class="btnR">
					<li><a class="btn02" href="#" onclick="javascript:doSubmit()"><span>저장</span></a></li>
					<li><a class="btn02" href="#" onclick="javascript:reset();"><span>다시</span></a></li>
					<li><a class="btn02" href="#" onclick="javascript:window.close()"><span>닫기</span></a></li>
				</ul>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

//재설정
function reset(){
	var frm = eval('document.<c:out value="${gsPopForm}"/>');
	frm.reset();
}


function doSubmit(){
	var frm = eval('document.<c:out value="${gsPopForm}"/>');
	if( frm.p_title.value == "" ){
		alert("제목을 입력하세요.");
        frm.p_title.focus();
		return;
	}

	if (realsize(frm.p_title.value) > 120) {
        alert("제목은 한글기준 60자를 초과하지 못합니다.");	//제목은 한글기준 60자를 초과하지 못합니다.
        frm.p_title.focus();
        return;
    }

    frm.target = "_self";
    frm.action = "/adm/rep/userStudyReportInsertData.do";
    frm.submit();

}
</script>