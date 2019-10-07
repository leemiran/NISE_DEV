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
		
			
			<input type="hidden" name="dataCreatetxt" id="dataCreatetxt" >
		<!-- contents -->
			<br/>
			<br/>
			<span id="dataCreate" style="color:#ff0000; margin-right: 750px;"></span>
			<table summary="제목,발송대상,내용으로 구성" width="97%" class="tbDetail">
				<colgroup>
					<col width="20%" />
					<col width="80%" />
				</colgroup>
				<tbody>
					<!-- <tr>
						<th scope="row">템플릿</th>
						<td style="margin-top:0px;margin-bottom:0px;">
							<input type="text" id="goo2">
							<input type="radio" name="p_emp_gubuna" id="p_emp_gubuna" value="T" style="cursor:pointer;" onClick="templet(1)"><img src="/images/user/m_go03.gif">&nbsp;&nbsp;							
							<input type="radio" name="p_emp_gubuna" id="p_emp_gubuna" value="E" style="cursor:pointer;" onClick="templet(2)"><img src="/images/user/m_go02.gif">

						</td>
					</tr> -->
					
					<tr>
						<th scope="row">제목</th>
						<td><input type="text" id="p_title" name="p_title" size="100" value="${memberGubunCount.mailtitle}" style="ime-mode:active"></td>
					</tr>
					<tr>
						<th scope="row">발송대상</th>
						<td style="margin-top:0px;margin-bottom:0px;">
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun" value="T" style="cursor:pointer;" > 교원(<fmt:formatNumber value="${memberGubunCount.tCount}" type="number"/>)
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun" value="E" style="cursor:pointer;"> 보조인력(<fmt:formatNumber value="${memberGubunCount.eCount}" type="number"/>)
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun" value="R" style="cursor:pointer;"> 교육 전문직(<fmt:formatNumber value="${memberGubunCount.rCount}" type="number"/>)
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun" value="P" style="cursor:pointer;"> 일반회원(학부모 등)(<fmt:formatNumber value="${memberGubunCount.pCount}" type="number"/>)
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
						
						<textarea name="textAreaContent" id="textAreaContent" rows="10" cols="100" style="width:100%; height:412px; display:none;">${memberGubunCount.mailContent}</textarea>
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
	    
	    var cnt=0;
	    for(i=0; i<thisForm.p_emp_gubun.length; i++){
	    	if(thisForm.p_emp_gubun[i].checked){
	    		cnt++;	
	    	}
	    }
	    if(cnt==0){
	    	alert("발송대상을 선택하여 주시기 바랍니다.");	    	
	    	return;
	    }
	    
	    

	    /* if (document.all.ObjEditor.style.display == "none") {
			thisForm.p_content.value = thisForm.txtDetail.value;
		} else {
			thisForm.p_content.value = ObjEditor.document.all.tags("html")[0].outerHTML;
			thisForm.p_content.value.replace("&","&amp;");
		} */
		
		//smart Editor
	    oEditors.getById["textAreaContent"].exec("UPDATE_CONTENTS_FIELD", []);
	    thisForm.p_content.value =document.getElementById("textAreaContent").value;
	    document.getElementById("textAreaContent").value="";
	    

		if(thisForm.p_content.value == "")
		{
			alert("내용을 입력하세요!");
	        return;
		}
		
		//setInterval(AjaxCall , 3000);
		
		
		setInterval(function(){ 
			if($("#dataCreate").text() == ""){
				$("#dataCreate").text('메일 발송 중');
			}else{
				$("#dataCreate").text();
			}	
			timecnt = timecnt+1;
		}, 1000);
			
		//AjaxCallSubmit();
		
		thisForm.target = "_self";
		thisForm.action = "/com/snd/memberGubunSendMailAction.do";
		thisForm.submit();
		
		
		
		
	}

	/* <c:if test="${empty resultMsg}">	
		<c:if test="${empty memberList || fn:length(memberList) == 0}">
			alert("발송대상자가 존재하지 않습니다. 발송대상자가 존재하여야 메일발송이 가능합니다.");
			window.close();
		</c:if>	
	</c:if>
 */
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
	
	function templet(_num){
		var sHTML = "";		
		oEditors.getById["textAreaContent"].exec("SET_CONTENTS", [""]);


		if(_num==1){
			sHTML ='<span>				</span>			<strong>			    <span style=\"font-size: 24pt;\">			        test11		    </span>			</strong>			';
		}
		if(_num==2){
			sHTML ='<span>				</span>			<strong>			    <span style=\"font-size: 24pt;\">			        test2		    </span>			</strong>			';
		}
		oEditors.getById["textAreaContent"].exec("PASTE_HTML", [sHTML]);
	}

	function resultTime(){
		$("#dataCreate").css("color","#ff0000");
		$("#dataCreate").text("");
	}
	
	
	function AjaxCallSubmit() {

			
		var p_emp_gubun = $('input:radio[name="p_emp_gubun"]:checked').val();		
		var p_title = $("#p_title").val();
		var p_content = thisForm.p_content.value;
		
		//로컬 한글 깨짐
		//sido = escape(encodeURIComponent(sido));
 
		$.ajax({  
			//url: "<c:out value="${gsDomainContext}" />/com/pop/searchGugun.do?sido="+sido, //로컬 한글깨짐		
			url: "<c:out value="${gsDomainContext}" />/com/snd/memberGubunSendMailAction.do",	//서버				
			method: 'post',
			data: {p_emp_gubun:p_emp_gubun,
				p_title:p_title,
				p_content:p_content
			},		//로컬일 때 주석
			dataType: 'json',
			contentType : "application/json:charset=utf-8",			 
			success: function(data) {  
				isOk = data.isOk;
				msg = data.resultMsg;
				
				$("#dataCreate").css("color","#00aa00");
				$("#dataCreate").text("메일발송 완료");
				setTimeout("resultTime()", 3000);
				
				alert(isOk+"건이 "+msg);
				
			},    
			error: function(xhr, status, error) {   
				alert(status);   
				alert(error);    
			}   
		});  
	
	}
	
	/* 
	function AjaxCall() {

		
		var sido = "서울특별시";	
		
		if(sido != "세종특별자치시"){
		
			//로컬 한글 깨짐
			//sido = escape(encodeURIComponent(sido));

			$.ajax({  
				//url: "<c:out value="${gsDomainContext}" />/com/pop/searchGugun.do?sido="+sido, //로컬 한글깨짐		
				url: "<c:out value="${gsDomainContext}" />/com/pop/searchGugun.do",	//서버				
				method: 'post',
				data: {sido:sido},		//로컬일 때 주석
				dataType: 'json',
				contentType : "application/json:charset=utf-8",			 
				success: function(data) {   
					data = data.result;
					$("#goo2").val("test");
				},    
				error: function(xhr, status, error) {   
					alert(status);   
					alert(error);    
				}   
			});  
		}
		

	} */

	
</script>