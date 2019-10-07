<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_ordseq" 	id="p_ordseq"	value="1">
	<input type="hidden" name="p_action" 	id="p_action"	value="go">
	<input type="hidden" name="p_class" 	id="p_class"	value="1">
	
	<input type="hidden" name="p_submitfiletype" value="">
	<input type="hidden" name="p_reptype" value="P" >
	<input type="hidden" name="p_weeklyseq" value="">
	<input type="hidden" name="p_weeklysubseq"  value="">
	<input type="hidden" name="p_isopen" value="3" >
	<!-- // search wrap -->
		    
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<div class="searchWrap txtL MT10">
		<ul class="datewrap">
				<li><strong class="shTit">콘텐츠명 : </strong></li>
				
				<li><select name="p_exam_subj" id="p_exam_subj" onChange="doPageList()">
					<option value="">선택</option>
					<c:forEach items="${reportQuestionList}" var="reportQuestionList" varStatus="1">
							<option value="${reportQuestionList.examSubj }" <c:if test="${p_exam_subj eq reportQuestionList.examSubj}">selected</c:if>>${reportQuestionList.examSubjnm }</option>
					</c:forEach>				
				</select></li>
				<!-- <li><a href="#none" class="btn_search" onclick="InsertPaperPage()"><span>검색</span></a></li> -->
		</ul>
	</div>
	
	<!-- list table-->
	<div class="tbList1">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col />
				<col width="150px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />				
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>문항</th>
					<th>출제자</th>
					<th>등록연도</th>
					<th>최종수정</th>
					<th>최초사용</th>
					<th>최종사용</th>
					<th>사용횟수</th>
					<th><input type="checkbox" name="checkedAlls" onclick="checkAll(this.checked)"></th>
				</tr>				
			</thead>

			<tbody>			
			<c:forEach items="${reportQuesList}"  var="result" varStatus="i">
				<tr>
					<td>${fn:length(reportQuesList) - (i.count - 1)}</td>
					<td>${result.questext}</td>
					<td>${result.examiner}</td>
					<td>${result.indate}</td>
					<td>${result.ldate}</td>
					<td>${result.fusedate}</td>
					<td>${result.lusedate}</td>
					<td>${result.usecnt}</td>
					<td>						
						<input type="checkbox" name="_Array_appcheck" id="_Array_appcheck" <c:if test="${result.quesnumchk eq 'Y' }">checked</c:if> onclick="chkvalue(thisForm._Array_checkvalue, this.checked, ${i.index})">
						
						<c:if test="${result.quesnumchk eq 'Y' }">
							<c:set var="checkvalue" value="1"/>
						</c:if>
						<c:if test="${result.quesnumchk eq 'N' }">
							<c:set var="checkvalue" value="0"/>
						</c:if>
						 
						<input type="hidden" name="_Array_checkvalue" id="_Array_checkvalue" value="${checkvalue}">
						<input type="hidden" name="_Array_p_params" value="${result.quesnum}">
					</td>	
				</tr>
			</c:forEach>
			<c:if test="${empty reportQuesList}">
				<tr>
					<td colspan="9">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	
	
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="150px" />
                <col width="" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th>제목</th>
                    <td><input name="p_title" type="text" class="input" size="120" value="${view.title}"/></td>
                </tr>
                <tr>
                    <th>만점</th>                    
                    <c:if test="${empty view.perfectscore}">
                    	<td><input type="text" name="p_perfectscore" class="input" size="5" value="0" maxlength="5" onkeyup="lfn_CheckField(this, '100.0');" onfocus="this.select()"/> </td>		
                    </c:if>
                    <c:if test="${not empty view.perfectscore}">
                    	<td><input type="text" name="p_perfectscore" class="input" size="5" value="${view.perfectscore}" maxlength="5" onkeyup="lfn_CheckField(this, '100.0');" onfocus="this.select()"/> </td>		
                    </c:if>                    
                </tr>
                <tr>
                    <th>과제제출 기본점수</th>
                    <c:if test="${empty view.submitscore}">
                    	<td><input type="text" name="p_submitscore" class="text" size="5" value="0" maxlength="4" onkeyup="lfn_CheckField(this, '0');" onfocus="this.select()"/></td>
                    </c:if>
                     <c:if test="${not empty view.submitscore}">
                     	<td><input type="text" name="p_submitscore" class="text" size="5" value="${view.submitscore}" maxlength="4" onkeyup="lfn_CheckField(this, '0');" onfocus="this.select()"/></td>
                    </c:if>
                    
                </tr>
                <tr>
                    <th>미제출 기본점수</th>
                    <c:if test="${empty view.notsubmitscore}">
                    	<td><input type="text" name="p_notsubmitscore" class="text" size="5" value="0" maxlength="4" onkeyup="lfn_CheckField(this, '0');" onfocus="this.select()"/></td>
                    </c:if>
                     <c:if test="${not empty view.notsubmitscore}">
                     	<td><input type="text" name="p_notsubmitscore" class="text" size="5" value="${view.notsubmitscore}" maxlength="4" onkeyup="lfn_CheckField(this, '0');" onfocus="this.select()"/></td>
                    </c:if>                    
                </tr>
                <tr>
                    <th>점수공개</th>
                    <td><input type="checkbox" name="p_isopenscore" value="Y" <c:if test="${view.isopenscore eq 'Y'}">checked</c:if>/> 과제물 채점점수를 공개합니다</td>
                </tr>
                <tr>
                    <th>제출기한</th>
                    <td>
                    	<input type="text" value="${fn2:getFormatDate(view.startdate, 'yyyy-MM-dd')}" size="10" name="p_startdate" maxlength="10" readonly="readonly"/>
						<a href="#none" onclick="popUpCalendar(this, document.all.p_startdate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
						<select name="p_starthour">${fn2:getDateOptions(0, 23, 0)}</select> 시
						<select name="p_startmin">${fn2:getDateOptions(0, 59, 0)}</select> 분
						<select name="p_startsec">${fn2:getDateOptions(0, 59, 0)}</select> 초
						&nbsp;&nbsp;
						~
						&nbsp;&nbsp;
						<input type="text" value="${fn2:getFormatDate(view.expiredate, 'yyyy-MM-dd')}" size="10" name="p_expiredate" maxlength="10" readonly="readonly"/>
						<a href="#none" onclick="popUpCalendar(this, document.all.p_expiredate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
						<select name="p_expirehour">${fn2:getDateOptions(0, 23, 23)}</select> 시
						<select name="p_expiremin">${fn2:getDateOptions(0, 59, 59)}</select> 분
						<select name="p_expiresec">${fn2:getDateOptions(0, 59, 59)}</select> 초
                    </td>
                </tr>
                <tr>
                    <th>추가제출기한</th>
                    <td>
                    	<input type="text" value="${fn2:getFormatDate(view.restartdate, 'yyyy-MM-dd')}" size="10" name="p_restartdate" maxlength="10" readonly="readonly"/>
						<a href="#none" onclick="popUpCalendar(this, document.all.p_restartdate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
						<select name="p_restarthour">${fn2:getDateOptions(0, 23, 0)}</select> 시
						<select name="p_restartmin">${fn2:getDateOptions(0, 59, 0)}</select> 분
						<select name="p_restartsec">${fn2:getDateOptions(0, 59, 0)}</select> 초
						&nbsp;&nbsp;
						~
						&nbsp;&nbsp;
						<input type="text" value="${fn2:getFormatDate(view.reexpiredate, 'yyyy-MM-dd')}" size="10" name="p_reexpiredate" maxlength="10" readonly="readonly"/>
						<a href="#none" onclick="popUpCalendar(this, document.all.p_reexpiredate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
						<select name="p_reexpirehour">${fn2:getDateOptions(0, 23, 23)}</select> 시
						<select name="p_reexpiremin">${fn2:getDateOptions(0, 59, 59)}</select> 분
						<select name="p_reexpiresec">${fn2:getDateOptions(0, 59, 59)}</select> 초
                    </td>
                </tr>
                <tr>
                    <th>내용/주의사항</th>
                    <td><textarea name="p_contents" rows="22" scrollbar="no" style="width:80%;height:120px">${view.contents}</textarea></td>
                </tr>
                <tr>
                    <th>제출양식</th>
                    <td>
			              <span style="padding-right:28px;"><input type="checkbox" name="p_fileformat" value="1" onclick="whenCheck();" <c:out value="${format1}"/>/> 상관없음</span>
				          <span style="padding-right:55px;"><input type="checkbox" name="p_fileformat" value="2"  <c:out value="${format2}"/>/> zip</span>
				          <span style="padding-right:27px;"><input type="checkbox" name="p_fileformat" value="3"  <c:out value="${format3}"/>/> hwp.doc </span>
				          <span style="padding-right:53px;"><input type="checkbox" name="p_fileformat" value="4"  <c:out value="${format4}"/>/> txt </span>
				          <span><input type="checkbox" name="p_fileformat" value="5"  <c:out value="${format5}"/>/> gif, jpg, bmp</span> <br />
				          <span style="padding-right:27px;"><input type="checkbox" name="p_fileformat" value="6"  <c:out value="${format6}"/>/> html, htm</span>
				          <span style="padding-right:24px;"><input type="checkbox" name="p_fileformat" value="7"  <c:out value="${format7}"/>/> wav, asf</span>
				          <span style="padding-right:60px;"><input type="checkbox" name="p_fileformat" value="8"  <c:out value="${format8}"/>/> ppt </span>
				          <span style="padding-right:47px;"><input type="checkbox" name="p_fileformat" value="9"  <c:out value="${format9}"/>/> pdf </span>
				          <span><input type="checkbox" name="p_fileformat" value="10" <c:out value="${format10}"/>/>기타 </span> <%//기타%>
                    </td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td>
                    	<c:if test="${empty selectProfFiles}"><input type="file" name="p_file" id="p_file" style="width:80%"/></c:if>
                    	<c:if test="${not empty selectProfFiles}">
                    		<c:forEach items="${selectProfFiles}" var="file">
		                    <a href="#none" onclick="fn_download('${file.realfile}', '${file.newfile}', 'reportprof')"><c:out value="${file.realfile}"/></a>
		                    <input type="checkbox"  name="p_filedel" value="Y"> (삭제시 체크)<br/>
							<input type="hidden" name="p_realfile"  value="<c:out value="${file.realfile}"/>">
							<input type="hidden" name="p_savefile"  value="<c:out value="${file.newfile}"/>">
		                    <br/>
                    		</c:forEach> 
                    	</c:if>
                    	<input type="hidden" name="p_upload_dir" value="reportprof">
                    </td>
                </tr>
            </tbody>
                <tr>
                    <th>파일용량제한</th>
                    <td>
                    	<input type="text" name="p_filelimit" class="text" size="5" value="15" value="${view.filelimit}"/> MB
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    
   	<c:if test="${empty view.subj}">
		<div class="btnCen"><a href="#" class="btn01" onclick="insertData('in')"><span>저장</span></a></div>
	</c:if>
	
	<c:if test="${not empty view.subj}">
		<div class="btnCen"><a href="#" class="btn01" onclick="insertData('up')"><span>수정</span></a></div>
	</c:if>
    
    
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/rep/reportProf.do"
		frm.target = "_self";
		frm.p_action.value = "go";
		frm.submit();
	}

	/* 필드값 숫자 체크 */
	function lfn_CheckField(numObj, defaultNum) {
		if (isNaN(numObj.value)) {
			alert("숫자만 입력가능합니다."); <%//숫자만 입력가능합니다.%>
			numObj.value = defaultNum;
			numObj.select();
			return;
		}
	}

	function insertData(action_type){
		
		var url = "/adm/rep/reportProfInsert.do";
		var confirm_msg ="저장";
		
		if(action_type=="up"){
			url = "/adm/rep/reportProfUpdate.do";
			confirm_msg ="수정";
		}
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		
		if( frm.p_subj.value == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		
		var varCnt = 0;
	
		for(var i=0;i<thisForm.length;i++){
			if (thisForm.elements[i].name == "_Array_appcheck" && thisForm.elements[i].checked) {
				varCnt++; 
			}
		}

		if ( varCnt == 0 ) { 
			alert("선택된 문항이 없습니다. 콘텐츠명을 선택하시고 문항을 선택하세요");
			return;
		}
		
		
		if (frm.p_title.value == ""){
			alert("제목을 입력하세요");		//제목을 입력하세요
			frm.p_title.focus();
			return;
		}

		if (realsize(frm.p_title.value) > 120) {
			alert("제목은 한글기준 60자를 초과하지 못합니다.");		//제목은 한글기준 60자를 초과하지 못합니다.
			frm.p_title.focus();
      		return;
    	}

		if (frm.p_perfectscore.value < 1 || frm.p_perfectscore.value > 100){
			alert("만점은 1~100 사이의 값으로 입력하세요."); // 만점은 1~100 사이의 값으로 입력하세요.
			frm.p_perfectscore.select();
			return;
		}

    	if (frm.p_submitscore.value < 0 || frm.p_submitscore.value > 100){
			alert("기본점수는 0~100 사이의 값으로 입력하세요.");		//기본점수는 0~100 사이의 값으로 입력하세요.
			frm.p_submitscore.select();
			return;
		}

    	if (frm.p_notsubmitscore.value < 0 || frm.p_notsubmitscore.value > 100){
			alert("기본점수는 0~100 사이의 값으로 입력하세요.");		//기본점수는 0~100 사이의 값으로 입력하세요.
			frm.p_notsubmitscore.select();
			return;
		}

		if (frm.p_startdate.value == ""){
			alert("시작일을 입력하세요.");		//시작일을 입력하세요.
			frm.p_startdate.focus();
			return;
		}

		if (frm.p_expiredate.value == ""){
			alert("종료일을 입력하세요.");		//종료일을 입력하세요.
			frm.p_expiredate.focus();
			return;
		}

		if (frm.p_startdate.value > frm.p_expiredate.value){
			alert("시작일이 종료일보다 큽니다.");		//시작일이 종료일보다 큽니다.
			frm.p_startdate.focus();
			return;
		}

		if (frm.p_restartdate.value == "" && !frm.p_reexpiredate.value == ""){
			alert("시작일을 입력하세요.");		//시작일을 입력하세요.
			frm.p_restartdate.focus();
			return;
		}

		if (!frm.p_restartdate.value == "" && frm.p_reexpiredate.value == ""){
			alert("종료일을 입력하세요.");		//종료일을 입력하세요.
			frm.p_reexpiredate.focus();
			return;
		}

		if (frm.p_restartdate.value > frm.p_reexpiredate.value){
			alert("시작일이 종료일보다 큽니다.");		//시작일이 종료일보다 큽니다.
			frm.p_restartdate.focus();
			return;
		}

		if (frm.p_contents.value == ""){
			alert("내용을 입력하세요");		//내용을 입력하세요
			frm.p_contents.focus();
			return;
		}

    	if (realsize(frm.p_contents.value) > 2400) {
      		alert("내용은 한글기준 1200자를 초과하지 못합니다.");		//내용은 한글기준 1200자를 초과하지 못합니다.
      		frm.p_contents.focus();
      		return;
    	}

	  	var check;

    	for(var i=0; i < frm.p_fileformat.length ; i++) {
      		if(frm.p_fileformat[i].checked) {
        		check="Y";
        		break;
      		}else{
        		check="N";
      		}
    	}

    	if(check == "N"){
      		alert("제출양식을 적어도 1개이상 선택하세요.");		//제출양식을 적어도 1개이상 선택하세요.
      		return;
    	}

	  	var data="";

	  	for(var i=0; i < frm.p_fileformat.length ; i++){
      		if(frm.p_fileformat[i].checked == true){
        		data+=frm.p_fileformat[i].value+",";
      		}
    	} 
	  	
	  	if (!confirm(confirm_msg+" 하시겠습니까?")) {
			return;
		}
	  	

    	data = data.substring(0,data.length - 1);
    	frm.p_submitfiletype.value=data;

		frm.action = url;
		frm.target = "_self";
		frm.submit();
	}

	function whenCheck() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if(frm.p_fileformat[0].checked) {
			for(var i=1; i < frm.p_fileformat.length; i++) {
				frm.p_fileformat[i].checked = false;
				frm.p_fileformat[i].disabled = true;
			}
		} else {
			for(var i=1; i < frm.p_fileformat.length; i++) {
				frm.p_fileformat[i].disabled = false;
			}
		}
	}
	
	
	//search 함수
	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		if( frm.ses_search_subj.value == "" ){
			alert("과정을 선택하세요.");
			return;
		}

		frm.p_subj.value = frm.ses_search_subj.value;
		frm.p_year.value = frm.ses_search_year.value;
		frm.p_subjseq.value = frm.ses_search_subjseq.value;
		
		frm.p_action.value = "go";
		frm.action = "/adm/rep/reportProf.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function chkvalue(pobj,val,i){		
		
		var obj = document.getElementsByName("_Array_appcheck");
		if(obj)
		{
		    if(obj.length > 1){
		    	if(val){
					pobj[i].value="1";
				}else{
					pobj[i].value="0";
				}
			}else{
			    if(val){
					pobj.value="1";
				}else{
					pobj.value="0";
				}
			}
		}
	}
	
	
	function checkAll(val){
		var obj1 = document.getElementsByName("_Array_appcheck");
		var obj2 = document.getElementsByName("_Array_checkvalue");
		
		if(val==true){
			if(obj1.length > 1){	
				for(i=0;i < obj1.length;i++){
					obj1[i].checked = true;
					obj2[i].value="1";
				}
			}else{
				obj1[0].checked = true;
				obj2[0].value="1";
			}
		}else{
			if(obj1.length > 1){
				for(i=0;i < obj1.length;i++){
					obj1[i].checked = false;
					obj2[i].value="0";
				}
			}else{
				obj1[0].checked = false;
				obj2[0].value="0";
			}
		}
	}
	
	
</script>