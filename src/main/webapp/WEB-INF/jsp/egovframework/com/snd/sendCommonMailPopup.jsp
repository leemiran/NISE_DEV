<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">
alert("${resultMsg}");
</c:if>


<c:if test="${isClose}">
window.close();
</c:if>
-->
</script>

<script type="text/javascript" src="<%=request.getContextPath()%>/smartEditor/js/HuskyEZCreator.js" charset="utf-8"></script>


<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
	<input type = "hidden" name="p_content"    value = "" />
	
		
<div id="popwrapper"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<!-- header -->
			<h2>메일 발송</h2>
		
		
		<!-- contents -->
			<table summary="제목,발송대상,내용으로 구성" width="97%" class="tbDetail">
				<colgroup>
					<col width="20%" />
					<col width="80%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="row">제목</th>
						<td><input type="text" id="p_title" name="p_title" size="100" style="ime-mode:active"></td>
					</tr>
					<tr>
						<th scope="row">발송대상</th>
						<td style="margin-top:0px;margin-bottom:0px;">
						
								<div class="popTb tbAuto" style="margin-top:0px; margin-left:0px; margin-bottom:0px;">
									<table summary="이름, 이메일로 구성" cellspacing="0" width="100%">
                <caption>발송대상</caption>
                <colgroup>
											<col width="50%" />
											<col width="50%" />
										</colgroup>
										<thead>
											<tr>
												<th scope="row" class="boC">이름</th>
												<th scope="row" >이메일</th>
											</tr>
										</thead>
										<tbody>
					<c:forEach items="${memberList}" var="result">
											<tr>
												<td class="boR">
												<input type="hidden" name="p_userid" value="${result.userid}">
												<input type="hidden" name="p_email" value="${result.email}">
												<input type="hidden" name="p_name" value="${result.name}">
												${result.name}(${result.userid})
												</td>
												<td class="left">${result.email}</td>
											</tr>
					</c:forEach>
										</tbody>
									</table>
								</div>
							
			
						</td>
					</tr>

					
					<tr>
						<th scope="row" align="center">
							내용
							<!--  <br/>
				            <a href="javascript:insertMappingString('{name}');">{학습자명}</a><br/>
				            <a href="javascript:insertMappingString('{subjnm}');">{과정명}</a><br/>
				            <a href="javascript:insertMappingString('{edustart}');">{학습시작일}</a><br/>
				            <a href="javascript:insertMappingString('{eduend}');">{학습종료일}</a><br/>
				            <a href="javascript:insertMappingString('{tstep}');">{차시진도}</a><br/>
				            <a href="javascript:insertMappingString('{study_time}');">{접속시간}</a>
				            //-->
						</th>
						<td>
						<c:set var="content"></c:set>
                    	<c:set var="width"  value="650"/>
                    	<c:set var="height" value="250" />
                    	
                    	<%-- <%@ include file="/WEB-INF/jsp/egovframework/com/lib/DhtmlEditor.jsp" %>
						<input type = "hidden" name="p_content"    value = "${view.contents}" /> --%>
						
						<textarea name="textAreaContent" id="textAreaContent" rows="10" cols="100" style="width:100%; height:412px; display:none;">${view.adcontent}</textarea>
                		<input type="hidden" name="p_adcontent" id="p_adcontent">
						
						</td>
					</tr>
				</tbody>
			</table>
		<!-- // contents -->
		
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="sendMail()"><span>메일보내기</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫 기</span></a></li>
		</ul>
		<!-- // button -->
		</div>
	</div>
</div>
</form>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

	function insertMappingString(code){
		thisForm.p_content1.value += code;
	}
	function sendMail(){
		
		//alert(0);
		if ( thisForm.p_title.value == "" ){
	        alert("제목을 입력하세요!");
	        frm.p_title.focus();
	        return;
	    }
		//alert(1);
	    if (realsize(thisForm.p_title.value) > 200) {
	        alert("제목은 한글기준 100자를 초과하지 못합니다.");
	        thisForm.p_title.focus();
	        return;
	    }
	    //alert(2);
		//smart Editor
	    oEditors.getById["textAreaContent"].exec("UPDATE_CONTENTS_FIELD", []);
	    thisForm.p_content.value =document.getElementById("textAreaContent").value;
	    document.getElementById("textAreaContent").value="";
	    //alert(5);

		if(thisForm.p_content.value == "")
		{
			alert("내용을 입력하세요!");
	        return;
		}
		//alert(6);
		//setInterval(AjaxCall , 3000);
		
		//alert(7);
		setInterval(function(){ 
			if($("#dataCreate").text() == ""){
				$("#dataCreate").text('메일 발송 중');
			}else{
				$("#dataCreate").text();
			}	
			timecnt = timecnt+1;
		}, 1000);
		//alert(8);	
		//AjaxCallSubmit();
		
		thisForm.target = "_self";
		thisForm.action = "/com/snd/sendCommonMailAction.do";
		thisForm.submit();
		
		
		
		
	}
	
	/* function sendMail(){
		if ( thisForm.p_title.value == "" ){
	        alert("제목을 입력하세요!");
	        frm.p_title.focus();
	        return;
	    }
	    if (realsize(thisForm.p_title.value) > 200) {
	        alert("제목은 한글기준 100자를 초과하지 못합니다.");
	        thisForm.p_title.focus();
	        return;
	    }

	   
		
		//smart Editor
	    oEditors.getById["textAreaContent"].exec("UPDATE_CONTENTS_FIELD", []);
	    thisForm.p_content.value =document.getElementById("textAreaContent").value;
	    document.getElementById("textAreaContent").value="";
	    

		if(thisForm.p_content.value == "")
		{
			alert("내용을 입력하세요!");
	        return;
		}
	    
		thisForm.target = "_self";
		thisForm.action = "/com/snd/sendCommonMailAction.do";
		thisForm.submit();
	} */

	<c:if test="${empty resultMsg}">	
		<c:if test="${empty memberList || fn:length(memberList) == 0}">
			alert("발송대상자가 존재하지 않습니다. 발송대상자가 존재하여야 메일발송이 가능합니다.");
			window.close();
		</c:if>	
	</c:if>

	//smart Editor	
	var oEditors = [];
	nhn.husky.EZCreator.createInIFrame({
	oAppRef: oEditors,
	elPlaceHolder: "textAreaContent",
	sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin.html",
	//sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin_noimg.html",
	fCreator: "createSEditor2"
	});
	
	/* 
	//‘저장’ 버튼을 누르는 등 저장을 위한 액션을 했을 때 submitContents가 호출된다고 가정한다.
	function submitContents(elClickedObj) {
	// 에디터의 내용이 textarea에 적용된다.
	oEditors.getById["textAreaContent"].exec("UPDATE_CONTENTS_FIELD", []);
	// 에디터의 내용에 대한 값 검증은 이곳에서
	// document.getElementById("ir1").value를 이용해서 처리한다.
	try {
	elClickedObj.form.submit();
	} catch(e) {}

	} */
	
	

	//textArea에 이미지 첨부
	var fileName="";
	var upfileName="";
	function pasteHTML(filepath){
	    var sHTML = '<img src="http://iedu.nise.go.kr/dp/smartEditor/photo_uploader/uploadFolder/'+fileName+'"'+" alt="+upfileName+">";

	    oEditors.getById["textAreaContent"].exec("PASTE_HTML", [sHTML]); 

	}
	
</script>