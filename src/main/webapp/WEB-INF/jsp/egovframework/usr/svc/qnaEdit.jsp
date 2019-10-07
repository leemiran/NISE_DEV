<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>

<!-- recaptcha -->
<script type="text/javascript">
      var onloadCallback = function() {
        grecaptcha.render('html_element', {
          'sitekey' : '6LerwDkUAAAAAAlxIUdy0wdNfIF4LxH-p3AIxw-s',
          'theme' : 'light'
        });
      };
</script>

<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype" value="<c:out value="${p_contenttype}"  escapeXml="true" />" />
<input type="hidden" name="p_subj" value="<c:out value="${p_subj}"  escapeXml="true" />" />
<input type="hidden" name="p_year" value="<c:out value="${p_year}"  escapeXml="true" />" />
<input type="hidden" name="p_subjseq" value="<c:out value="${p_subjseq}"  escapeXml="true" />" />
<input type="hidden" name="p_studytype" value="<c:out value="${p_studytype}"  escapeXml="true" />" />
<input type="hidden" name="p_process" value="<c:out value="${p_process}"  escapeXml="true" />" />
<input type="hidden" name="p_next_process" value="<c:out value="${p_next_process}"  escapeXml="true" />" />
<input type="hidden" name="p_height" value="<c:out value="${p_height}"  escapeXml="true" />" />
<input type="hidden" name="p_width" value="<c:out value="${p_width}"  escapeXml="true" />" />
<input type="hidden" name="p_lcmstype" value="<c:out value="${p_lcmstype}"  escapeXml="true" />" />
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" enctype="multipart/form-data" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

	<input type = "hidden" name="p_process" value="<c:out value="${p_process}"  escapeXml="true" />" />
	<input type = "hidden" name="p_seq"     value="<c:out value="${p_seq}"  escapeXml="true" />" />
	<input type = "hidden" id="pageIndex" name = "pageIndex"  value="<c:out value="${pageIndex}"  escapeXml="true" />" />
<c:if test="${p_tabseq eq '1'}">	
	<input type = "hidden" name="p_tabseq" value="<c:out value="${p_tabseq}"  escapeXml="true" />" />
</c:if>
	<input type = "hidden" name="p_search" value="<c:out value="${p_search}"  escapeXml="true" />" />
	<input type = "hidden" name="p_searchtext"  value = "<c:out value="${search_text}"  escapeXml="true" />" />
	


<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="작성자, 공개여부, 제목, 이메일, 내용, 첨부파일로 구성" cellspacing="0" width="100%">
                <caption>상담내용작성</caption>
                <colgroup>
                    <col width="90px" />
                    <col width="" />
                </colgroup>
                <tbody>
                    <tr class="title">
                        <th scope="col">작성자</th>
                        <td>
<c:if test="${empty p_seq}">
                        	<c:out value="${sessionScope.name}"/>(<c:out value="${sessionScope.userid}"/>)
</c:if>
<c:if test="${not empty p_seq}">
                        	<c:out value="${view.name}"/>(<c:out value="${view.userid}"/>)
</c:if>
                        </td>
                    </tr>
<c:if test="${p_tabseq eq '1'}">
						<tr>
							<th scope="col">공개여부</th>
							<td>
							<input id="p_isopen_1" name="p_isopen" type="radio" value="Y" class="input_border vrM" <c:if test="${view.isopen eq 'Y'}">checked</c:if> /><label for="p_isopen_1">공개</label>
							<input id="p_isopen_2" name="p_isopen" type="radio" value="N" class="input_border vrM" <c:if test="${view.isopen ne 'Y'}">checked</c:if> /><label for="p_isopen_2">비공개</label>
							</td>
						</tr>
</c:if>

<c:if test="${p_tabseq ne '1'}">
						<tr style="display:none;">
							<th scope="col">공개여부</th>
                            <td>
							<input id="i_isopeny" name="p_isopen" type="hidden" value="N"/>
                            </td>
                        </tr>
                        <tr>
							<th scope="col">구분</th>
							<td>
<c:if test="${empty p_seq}">							
							<input id="p_tabseq_1" name="p_tabseq" type="radio" value="2" class="input_border vrM" onClick="p_content_txt('1')" <c:if test="${view.tabseq ne '3'}">checked</c:if> /><label for="p_tabseq_1">입금확인</label>
							<input id="p_tabseq_2" name="p_tabseq" type="radio" value="3" class="input_border vrM" onClick="p_content_txt('2')" <c:if test="${view.tabseq eq '3'}">checked</c:if> /><label for="p_tabseq_2">환불요청</label>
</c:if> 		
<c:if test="${not empty p_seq}">		
							<c:if test="${view.tabseq ne '2'}">입금확인</c:if>
							<c:if test="${view.tabseq eq '2'}">환불요청</c:if>
							<input type = "hidden" name="p_tabseq"     value = "${view.tabseq}" />
</c:if>			
							</td>
						</tr>
</c:if>                   
                    <tr>
                        <th scope="row"><label for="p_title">제목</label></th>
                        <td><input type="text" name="p_title" id="p_title" style="width:500px;" value="<c:out value="${view.title}" escapeXml="true" />" maxlength="200"/></td>
                    </tr>
                    <tr>
                          <th scope="row"><label for="p_email">이메일</label></th>
                          <td>
<c:if test="${empty p_seq}">
                        	<input type="text" name="p_email" id="p_email" style="width:200px;" value="<c:out value="${sessionScope.email}" escapeXml="true"/>" maxlength="50"/>
</c:if>
<c:if test="${not empty p_seq}">
                        	<input type="text" name="p_email" id="p_email" style="width:200px;" value="<c:out value="${view.email}" escapeXml="true" />" maxlength="50"/>
</c:if>
                          
                          </td>
                    </tr>
                    <tr>
                          <th scope="row"><label for="p_content">내용</label></th>
                          <td><textarea name="p_content" id="p_content" rows="15" cols="10" style="width:600px;"><c:out value="${view.content}" escapeXml="true" /><c:if test="${menu_main eq '5' && menu_sub eq '5'}"><c:if test="${fn:length(view.content)==0 }">·신청하신 과정명:
·입금하신 분 성함:
·입금 일자:
·입금 금액:</c:if></c:if></textarea></td>
                    </tr>  
                    <tr>
                        <th scope="row"><label for="files">첨부파일</label></th>
                        <td>
<c:if test="${not empty p_seq}">
                        <c:if test="${not empty view.realfile}">
                    		<a href="#none" onclick="fn_download('${view.realfile}', '${view.savefile}', 'bulletin')">
                    		<img src="/images/user/icon_file.gif" alt="첨부파일"/>
                    		<c:out value="${empty view.realfile ? view.savefile : view.realfile}"/>
                    		</a>
                    		&nbsp;&nbsp;
                    		<input type="checkbox" name="p_check" id="p_check" value="Y" class="input_border vrM"> <label for="p_check">(삭제시체크)</label>
                    	</c:if>
                    	
                    	<br/>
</c:if>
                        <input type="file" name="files" id="files" style="width:500px;"/>
                        
                        
                        </td>
                    </tr>                  
                </tbody>
            </table>				
        </div>
        <div id="html_element"></div>     
        
                
        <!-- // detail wrap -->
        
        
        
		<!-- button -->
		<ul class="btnR">
<c:if test="${empty p_seq}">
        	<li><a href="#none" onclick="doPageSave('insert')"><img src="/images/user/btn_ok.gif" alt="확인"/></a></li>
</c:if>
<c:if test="${not empty p_seq}">
        	<li><a href="#none" onclick="doPageSave('update')"><img src="/images/user/btn_ok.gif" alt="확인"/></a></li>
</c:if>        	
        	<li><a href="#none" onclick="doPageList()"><img src="/images/user/btn_cencal.gif" alt="취소"/></a></li>        	
		</ul>      
		<!-- // button -->
		     
</form>


<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {
	<c:if test="${p_tabseq eq '1'}">		
	thisForm.action = "/usr/svc/qnaList01.do";
	</c:if>
	<c:if test="${p_tabseq ne '1'}">		
	thisForm.action = "/usr/svc/qnaList02.do";
	</c:if>
	thisForm.target = "_self";
	thisForm.submit();
}


function doPageSave(mode){

	var userid="<c:out value='${sessionScope.userid}'/>";
	var name="<c:out value='${sessionScope.name}'/>";
	
	if(userid=="" || name==""){
		alert("세션 시간 종료되어 로그아웃 되었습니다.");
		doPageList();
	}
	if(thisForm.p_title.value == "")
	{
		alert("제목을 입력해 주십시오.");
		thisForm.p_title.focus();
		return;
	}

	
	if (!isValidEmail(thisForm.p_email)) {
         alert("올바른 이메일 주소가 아닙니다.");
         thisForm.p_email.focus();
         return;
    }

    
	if(thisForm.p_content.value == "")
	{
		alert("내용을 입력해 주십시오.");
		thisForm.p_content.focus();
		return;
	}
	

	if(typeof(grecaptcha) !='undefined'){
		if(grecaptcha.getResponse() == ""){
			alert("스팸방지코드(로봇이 아닙니다)를 확인해 주세요.");
			return;
		}
	}
	
	if( $("#files").val() != "" ){
		var ext = $('#files').val().split('.').pop().toLowerCase();
	    if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg','hwp','doc','docx','ppt','ppt','pptx','pps','ppsx','xls','xlsx','ppt','pdf', 'zip']) == -1) {
			alert('gif, png, jpg, jpeg, hwp, doc, docx, ppt, ppt, pptx, pps, ppsx, xls, xlsx, ppt, pdf, zip 파일만 업로드 할수 있습니다.');
			return;
 	    }
	}


	
	if(confirm("작성하신 글을 저장하시겠습니까?"))
	{
		thisForm.action = "/usr/svc/qnaAction.do";
		thisForm.p_process.value = mode;
		thisForm.target = "_self";
		thisForm.submit();
	}
}	
	var tempTitleValString	=$('title').text();
	var tempTitleValArr = tempTitleValString.split(':');
	var NewTitleString	="";
	for(var i=0; i < tempTitleValArr.length ; ++i ){
		if(i==0){
			NewTitleString+=tempTitleValArr[i]+"(쓰기)";
		}else{
			NewTitleString+=" : "+tempTitleValArr[i];
		}
	}
	document.title=NewTitleString;
	
	function p_content_txt(p_tabseq){
		if(p_tabseq ==1){
			thisForm.p_content.value="·신청하신 과정명:\r\n·입금하신 분 성함:\r\n·입금 일자:\r\n·입금 금액:";
		}else{
			thisForm.p_content.value="·환불과정명:\r\n·환불사유:\r\n·환불받으실 계좌번호:\r\n·환불계좌 은행명:\r\n·환불계좌 예금주:\r\n※신용카드 또는 실시간 계좌이체로 결재하신 분께서는 환불계좌 정보를 입력하지 않아도 되십니다.\r\n";
		}
	}
	
//-->
</script>

<script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit" async defer></script>
    
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->