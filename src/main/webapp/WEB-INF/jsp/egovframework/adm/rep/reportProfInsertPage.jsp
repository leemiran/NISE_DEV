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
	<input type="hidden" name="p_action" 	id="p_action"	value="go">
	<input type="hidden" name="p_class" 	id="p_class"	value="1">
	
	<input type="hidden" name="p_submitfiletype" value="">
	<input type="hidden" name="p_reptype" value="P" >
	<input type="hidden" name="p_weeklyseq">
	<input type="hidden" name="p_weeklysubseq">
	<input type="hidden" name="p_isopen" value="3" >
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertData()"><span>저장</span></a></div>
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
                    <td><input name="p_title" type="text" class="input" size="80"/></td>
                </tr>
                <tr>
                    <th>만점</th>
                    <td><input type="text" name="p_perfectscore" class="input" size="5" value="100.0" maxlength="5" onkeyup="lfn_CheckField(this, '100.0');" onfocus="this.select()"/> </td>
                </tr>
                <tr>
                    <th>과제제출 기본점수</th>
                    <td><input type="text" name="p_submitscore" class="text" size="5" value="0" maxlength="4" onkeyup="lfn_CheckField(this, '0');" onfocus="this.select()"/></td>
                </tr>
                <tr>
                    <th>미제출 기본점수</th>
                    <td><input type="text" name="p_notsubmitscore" class="text" size="5" value="0" maxlength="4" onkeyup="lfn_CheckField(this, '0');" onfocus="this.select()"/></td>
                </tr>
                <tr>
                    <th>점수공개</th>
                    <td><input type="checkbox" name="p_isopenscore" value="Y"/> 과제물 채점점수를 공개합니다</td>
                </tr>
                <tr>
                    <th>제출기한</th>
                    <td>
                    	<input type="text" value="${fn2:getFormatDate(PCurrentDate, 'yyyy-MM-dd')}" size="10" name="p_startdate" maxlength="10" readonly="readonly"/>
						<a href="#none" onclick="popUpCalendar(this, document.all.p_startdate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
						<select name="p_starthour">${fn2:getDateOptions(0, 23, 0)}</select> 시
						<select name="p_startmin">${fn2:getDateOptions(0, 59, 0)}</select> 분
						<select name="p_startsec">${fn2:getDateOptions(0, 59, 0)}</select> 초
						&nbsp;&nbsp;
						~
						&nbsp;&nbsp;
						<input type="text" value="" size="10" name="p_expiredate" maxlength="10" readonly="readonly"/>
						<a href="#none" onclick="popUpCalendar(this, document.all.p_expiredate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
						<select name="p_expirehour">${fn2:getDateOptions(0, 23, 23)}</select> 시
						<select name="p_expiremin">${fn2:getDateOptions(0, 59, 59)}</select> 분
						<select name="p_expiresec">${fn2:getDateOptions(0, 59, 59)}</select> 초
                    </td>
                </tr>
                <tr>
                    <th>추가제출기한</th>
                    <td>
                    	<input type="text" value="" size="10" name="p_restartdate" maxlength="10" readonly="readonly"/>
						<a href="#none" onclick="popUpCalendar(this, document.all.p_restartdate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
						<select name="p_restarthour">${fn2:getDateOptions(0, 23, 0)}</select> 시
						<select name="p_restartmin">${fn2:getDateOptions(0, 59, 0)}</select> 분
						<select name="p_restartsec">${fn2:getDateOptions(0, 59, 0)}</select> 초
						&nbsp;&nbsp;
						~
						&nbsp;&nbsp;
						<input type="text" value="" size="10" name="p_reexpiredate" maxlength="10" readonly="readonly"/>
						<a href="#none" onclick="popUpCalendar(this, document.all.p_reexpiredate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
						<select name="p_reexpirehour">${fn2:getDateOptions(0, 23, 23)}</select> 시
						<select name="p_reexpiremin">${fn2:getDateOptions(0, 59, 59)}</select> 분
						<select name="p_reexpiresec">${fn2:getDateOptions(0, 59, 59)}</select> 초
                    </td>
                </tr>
                <tr>
                    <th>내용/주의사항</th>
                    <td><textarea name="p_contents" rows="22" scrollbar="no" style="width:80%;height:120px"></textarea></td>
                </tr>
                <tr>
                    <th>제출양식</th>
                    <td>
			              <span style="padding-right:28px;"><input type="checkbox" name="p_fileformat" value="1" onclick="whenCheck();" /> 상관없음</span>
				          <span style="padding-right:55px;"><input type="checkbox" name="p_fileformat" value="2" /> zip</span>
				          <span style="padding-right:27px;"><input type="checkbox" name="p_fileformat" value="3" /> hwp.doc </span>
				          <span style="padding-right:53px;"><input type="checkbox" name="p_fileformat" value="4" /> txt </span>
				          <span><input type="checkbox" name="p_fileformat" value="5" /> gif, jpg, bmp</span> <br />
				          <span style="padding-right:27px;"><input type="checkbox" name="p_fileformat" value="6" /> html, htm</span>
				          <span style="padding-right:24px;"><input type="checkbox" name="p_fileformat" value="7" /> wav, asf</span>
				          <span style="padding-right:60px;"><input type="checkbox" name="p_fileformat" value="8" /> ppt </span>
				          <span style="padding-right:47px;"><input type="checkbox" name="p_fileformat" value="9" /> pdf </span>
				          <span><input type="checkbox" name="p_fileformat" value="10" />기타 </span> <%//기타%>
                    </td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td>
                    	<input type="file" name="p_file" id="p_file" style="width:80%"/>
                    	<input type="hidden" name="p_upload_dir" value="reportprof">
                    </td>
                </tr>
            </tbody>
                <tr>
                    <th>파일용량제한</th>
                    <td>
                    	<input type="text" name="p_filelimit" class="text" size="5" value="15" /> MB
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/rep/reportProfList.do"
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

	function insertData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
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

    	data = data.substring(0,data.length - 1);
    	frm.p_submitfiletype.value=data;

		frm.action = "/adm/rep/reportProfInsertData.do";
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
</script>