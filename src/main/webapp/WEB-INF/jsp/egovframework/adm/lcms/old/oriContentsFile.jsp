<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<script type="text/javascript" src="<%=request.getContextPath()%>/innorix/common/innorix.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/smartEditor/js/HuskyEZCreator.js" charset="utf-8"></script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	    
    
    <input type = "hidden" name="p_process"  value = "" />        
    <input type = "hidden" name="p_contents"    value = "" />    
    <input type = "hidden" name="p_exam_subj" value = "${p_exam_subj}" />
    <input type = "hidden" name="realfile" id="realfile" value = "" />
     <input type = "hidden" name="savefile" id="savefile" value = "" />
	
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="25%" />
                <col width="75%" />
            </colgroup>
            <tbody>
            
            <c:if test="${empty view.examSubj}">
                <tr>
                    <th scope="row">작성자</th>
                    <td scope="row">${sessionScope.name}</td>
                </tr>
                <tr>
                    <th scope="row">등록일</th>
                    <td scope="row">${fn2:getFormatDateNow('yyyy.MM.dd')}</td>
                </tr>
            </c:if>
            <c:if test="${not empty view.examSubj}">
                <tr>
                    <th scope="row">작성자</th>
                    <td scope="row">${view.lname}</td>
                </tr>
                <tr>
                    <th scope="row">등록일</th>
                    <td scope="row">${fn2:getFormatDate(view.ldate, 'yyyy.MM.dd')}</td>
                </tr>
            </c:if>
           
                <tr>
                    <th scope="row">제목</th>
                    <td scope="row">
                    	<input type="text" name="p_subject" size="100" maxlength="80" value="${view.subject}">
                    </td>
                </tr>
                
                <tr>
                    <th scope="row">내용</th>
                    <td style="padding-left:0px" scope="row">                   	
						<textarea name="textAreaContent" id="textAreaContent" rows="10" cols="100" style="width:100%; height:200px; display:none;">${view.contents}</textarea>                	                   	
                    </td>
                </tr>               
            </tbody>
        </table>				
    </div>
    
    <div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="50px" />
				<col width="200px" />
				<col width="200px" />
				<col />
				<col width="70px" />
				<col width="150px" />
				<col width="150px" />
				<col width="150px" />
				<col width="150px" />
				<col width="50px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>					
					<th scope="row">원본파일</th>
					<th scope="row">저장파일</th>					
					<th scope="row">저장경로</th>
					<th scope="row">사이즈</th>
					<th scope="row">등록자</th>
					<th scope="row">등록일</th>
					<th scope="row">수정자</th>
					<th scope="row">수정일</th>
					<th scope="row">삭제</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
					<td><c:out value="${result.rn}"/></td>					
					<td><a href="#" onclick="javascript:fn_oriContentsFileDownload('${result.realfile }','${result.savepath }');"><c:out value="${result.realfile}"/></a></td>
					<td class="left"><c:out value="${result.savefile}"/></td>
					<td class="left"><c:out value="${result.savepath}"/></td>
					<td class="left"><c:out value="${result.filesize}"/></td>
					<td class="left"><c:out value="${result.inname}"/></td>
					<td class="left"><c:out value="${result.indate}"/></td>
					<td class="left"><c:out value="${result.lname}"/></td>
					<td class="left"><c:out value="${result.ldate}"/></td>
					<td class="left"><input type = "checkbox"  name = "_Array_p_fileseq" value = "${result.seq}"></td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(list) == 0}">
				<tr>
					<td colspan="10">데이터가 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
		</div>
    
   <div style="width:500px; height:200px">
   		<div style="margin-top:30px;position: absolute; left: 50%; margin-left:-250px"><div id="innorix_component" style="border: 1px solid #97b4cc; width:500px; height:200px"></div></div>   		
   </div>
   <div style="margin-top:30px;text-align:center;"><input class="innoGreenBtn" type="button" value="파일추가" onClick="File.OpenFileDialog();" /></div>
	
  <!-- <input class="innoGreenBtn" type="button" value="파일추가" onClick="File.OpenFileDialog();" />
<input class="innoBlueBtn" type="button" value="전송하기" onClick="File.Upload();" /> -->
    
    <!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>취소</span></a></div>
	    
<c:if test="${empty view.examSubj}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('insert')"><span>저장</span></a></div>
        <!-- <div class="btnR"><input class="innoBlueBtn" type="button" value="전송하기" onClick="File.Upload();" /></div> -->

</c:if>

<c:if test="${not empty view.examSubj}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('update')"><span>저장</span></a></div>
</c:if>	    
		
		
	</div>
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>


<script>
window.onload = function()
{
	innoInit({
		ContainElementID : "innorix_component", // 컴포넌트 출력객체 ID
		ActionElementID : "<c:out value="${gsMainForm}"/>",			// 메타정보 전송폼 ID
		UploadURL : "<%=request.getContextPath()%>/innorix/InnoDS/upload.jsp", 				// 업로드 처리 페이지
		//ResultURL : "result.jsp", 				// 업로드 결과 페이지
		ResultURL : "/adm/lcms/old/oriContentsFileInsert.do", 				// 업로드 결과 페이지		
		UseCompress: "true",					// 압축전송 사용여부				
		MaxTotalSize : "10GB",					// 첨부가능 전체용량
		MaxFileSize : "10GB",					// 첨부가능 1개파일 용량
    LogDirAlert : "false", // 로그기록 경로 확인창 띄움 !!! 운영에 반영시 알림창이 출력되니 개발/테스트에만 적용
    LogLevel : "7",         // 7레벨의 로그기록 남김설정		
		MaxFileCount : "200",					// 첨부가능 파일개수
		TransferMode : "innods"				// InnoDS 업로드 모드 설정		
	});
}

function innoOnEvent(msgEvent, arrParam, objName){
	// 파일 첨부 전 이벤트 
	if(msgEvent == Event.msgBeforeAddFile){
		// arrParam[0] = filename | arrParam[1] = filesize
		var addFileSize = arrParam[1];
		// filesize가 0인 경우 예외처리
		if(addFileSize == 0){
			// return을 false로 받아 첨부 하지 않도록 구성, true로 retrun을 받는 경우만 첨부
			return false;
		}
	}
}


</script>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/lcms/old/oriContentsSubjList.do"
		frm.target = "_self";
		frm.submit();
	}
	


	

	function whenNoticeSave(mode) {
                
        if (blankCheck(thisForm.p_subject.value)) {
            alert("제목을 입력하세요");
            thisForm.p_subject.focus();
            return;
        }
        
        if (realsize(thisForm.p_subject.value) > 200) {
            alert("제목은 한글기준 100자를 초과하지 못합니다.");
            thisForm.p_subject.focus();
            return;
        }
       

		//smart Editor
	    oEditors.getById["textAreaContent"].exec("UPDATE_CONTENTS_FIELD", []);
	    thisForm.p_contents.value =document.getElementById("textAreaContent").value;
	    document.getElementById("textAreaContent").value="";
	    
        thisForm.p_process.value = mode;
       
        File.Upload();
        
    }
	
	
	//smart Editor	
	var oEditors = [];
	nhn.husky.EZCreator.createInIFrame({
	oAppRef: oEditors,
	elPlaceHolder: "textAreaContent",
	sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin.html",
	//sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin_noimg.html",
	fCreator: "createSEditor2"
	});
	
	//textArea에 이미지 첨부
	var fileName="";
	var upfileName="";
	function pasteHTML(filepath){
	    var sHTML = '<img src="<%=request.getContextPath()%>/smartEditor/photo_uploader/uploadFolder/'+fileName+'"'+" alt="+upfileName+">";

	    oEditors.getById["textAreaContent"].exec("PASTE_HTML", [sHTML]); 

	}
	
	
	function fn_oriContentsFileDownload(realfile, savefile){
		var url = "/com/oriContentsFileDownload.do?";
		/* url += "realfile=" + realfile;
		url += "&savefile=" + savefile.replace("/opt/upload/ko/faq_upload/", "");
		window.location.href = url; */
		
		thisForm.realfile.value =realfile;
		thisForm.savefile.value =savefile;
		 
		thisForm.action = url;
		thisForm.target = "_self";
		thisForm.submit();
			
		return;
	}
	
</script>