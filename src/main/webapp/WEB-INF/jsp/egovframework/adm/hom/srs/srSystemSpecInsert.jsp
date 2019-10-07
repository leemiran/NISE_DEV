<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<script type="text/javascript" src="<%=request.getContextPath()%>/smartEditor/js/HuskyEZCreator.js" charset="utf-8"></script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_subjnm" 	id="p_subjnm"	value="${p_subjnm}">
	<input type="hidden" name="p_subjseq2" 	id="p_subjseq2"	value="${p_subjseq2}">
	<input type="hidden" name="pageIndex" 	id="p_subjseq2"	value="${pageIndex}">
	<input type = "hidden" name="p_type"     value = "${p_type}" />
	
    <input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
    <input type = "hidden" name="p_seq"     value = "${p_seq}" />
    <input type = "hidden" name="p_process"  value = "" />
    
    <input type = "hidden" name="p_startdate"  value = "" />
    <input type = "hidden" name="p_enddate"    value = "" />
    <input type = "hidden" name="p_compcd"     value = "" />
    <input type = "hidden" name="p_uncompcd"   value = "" />
    <input type = "hidden" name="p_grcodecd"   value = "N000001" />
    <input type = "hidden" name="p_selcomp"    value = "" />
    <input type = "hidden" name="req_content_val"    value = "" />
    <input type = "hidden" name="p_isAllvalue" value = "" />
    <input type = "hidden" name="p_loginynPre" value = "N" />
    <input type = "hidden" name="p_gubun" value = "N" />
    <input type = "hidden" name="gubun" value = "N" />    
    
    	
	
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
            </colgroup>
            <tbody>
<!--
 gubun			구분
 reg_id			등록자아이디	
 reg_DATE		등록일자
 reqdate_from	요청일자 from
 reqdate_to		요청일자 to
 finish_date	완료일자
 proc_rate		처리율
 req_title		요청제목
 req_content	요청내용
 list_seq		리스트순번
 busy_gubun		긴급구분 
 -->            
            <c:if test="${empty p_seq}">
                <tr>
                    <th scope="row">작성자</th>
                    <td scope="row">
                    	${sessionScope.name}(${sessionScope.userid})
                    	<input type = "hidden" name="reg_id" value = "${sessionScope.userid}" />
                    </td>
                    <th scope="row">등록일</th>
                    <td scope="row">
                    	<input name="reg_date" type="text" size="10" maxlength="10" readonly value="${fn2:getFormatDateNow('yyyy.MM.dd')}">
                    </td>
                </tr>
            </c:if>
            <c:if test="${not empty p_seq}">
                <tr>
                    <th scope="row">작성자</th>
                    <td scope="row">
                    	${view.userName}(${view.userid})
                    	<input type = "hidden" name="reg_id" value = "${view.userid}" />  
                    </td>
                    <th scope="row">등록일</th>
                    <td scope="row">
                    	 
                    ${fn2:getFormatDate(view.regDate, 'yyyy.MM.dd')}</td>
                </tr>
            </c:if>
                <tr>
                    <th scope="row">요청일자</th>
                    <td scope="row">
						<input name="reqdate_from" type="text" size="10" maxlength="10" readonly value="${fn2:getFormatDate(view.reqdateFrom, 'yyyy.MM.dd')}"> 
                        <img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.reqdate_from, 'yyyy.mm.dd')" /> ~
                        <input name="reqdate_to" type="text" size="10" maxlength="10" readonly value="${fn2:getFormatDate(view.reqdateTo, 'yyyy.MM.dd')}"> 
                        <img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.reqdate_to, 'yyyy.mm.dd')" />
                    </td>
                    
                    <th scope="row">완료일자</th>
                    <td scope="row">
						<input name="finish_date" type="text" size="10" maxlength="10" readonly value="${fn2:getFormatDate(view.finishDate, 'yyyy.MM.dd')}"> 
                        <img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.finish_date, 'yyyy.mm.dd')" />
                    </td>
                </tr>
                <tr> 
		            <th scope="row">시스템구분</th>
		            <td scope="row">
                    	<ui:code id="sys_gubun" selectItem="${view.sys_gubun}" gubun="" codetype="0122"  upper="" title="" className="" type="select" selectTitle="" event="" />						
		            </td>
                    <th scope="row">긴급여부</th>
                    <td scope="row"	>
                    	<ui:code id="busy_gubun" selectItem="${view.busyGubun}" gubun="" codetype="0131"  upper="" title="" className="" type="select" selectTitle="" event="" />
                    </td>
                </tr>
                <tr>
                    <th scope="row">제목</th>
                    <td scope="row" colspan="3">
                    	<input type="text" name="req_title" size="100" maxlength="80" value="${view.reqTitle}">
                    </td>
                </tr>
                
                <tr>
                    <th scope="row">내용</th>
                    <td style="padding-left:0px" scope="row"  colspan="3">
                    	<%-- <c:set var="content">${view.adcontent}</c:set>
                    	<c:set var="width"  value="600"/>
                    	<c:set var="height" value="400" />
                    	<%@ include file="/WEB-INF/jsp/egovframework/com/lib/DhtmlEditor.jsp" %>
						<input type="hidden" name="p_adcontent" id="p_adcontent"> --%>
						
						<textarea name="req_content" id="req_content" rows="10" cols="100" style="width:100%; height:412px; display:none;">${view.reqContent}</textarea>
                	<!--  input type="hidden" name="req_content" id="req_content" -->
                	
                    </td>
                </tr>
                <tr>
                    <th scope="row">첨부파일</th>
                    <td scope="row" colspan="3">
                    	
                    	
                    	<c:forEach items="${fileList}" var="result" varStatus="i">
                    	* <a href="#none" onclick="fn_download('${result.realfile}', '${result.savefile}', 'bulletin')">
                    	<c:out value="${empty result.realfile ? result.savefile : result.realfile}"/>
                    	</a> 
                    	<input type = "checkbox"  name = "_Array_p_fileseq" value = "${result.fileseq}"> (삭제시 체크)<br/>
                    	</c:forEach>
                    	
                    	
                    	
                    	<input type="FILE" name="p_file1" size="80"><br/>
						<input type="FILE" name="p_file2" size="80"><br/>
						<input type="FILE" name="p_file3" size="80"><br/>
						<input type="FILE" name="p_file4" size="80"><br/>
						<input type="FILE" name="p_file5" size="80"><br/>
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    
    
    
    <!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>취소</span></a></div>
	    
<c:if test="${empty p_seq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('insert')"><span>저장</span></a></div>
</c:if>

<c:if test="${not empty p_seq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('update')"><span>저장</span></a></div>
</c:if>	    
		
		
	</div>
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/srs/srSystemSpecList.do"
		frm.target = "_self";
		frm.submit();
	}
	


	//로긴구분 선택시
	function whenLoginSelect(){
	  if(thisForm.p_login[2].checked == true) { //로그인후
          thisForm.p_loginynPre.value = 'Y'
      }
      else if(thisForm.p_login[0].checked == true || thisForm.p_login[1].checked == true){ //전체, 로그인전
          if(thisForm.p_loginynPre.value == 'Y'){	  
	          thisForm.p_grcodegubun[0].checked = false; 
	          thisForm.p_grcodegubun[0].disabled = false; 
	          thisForm.p_grcodegubun[1].checked = true; 
	          thisForm.p_grcodegubun[1].disabled = false; 
          }
    	  thisForm.p_loginynPre.value = 'N'; 
      }
	}


	function whenGrcodeGubunSelect(){  //대상교육그룹유무
		
	}	




	//팝업여부선택시
	function whenPopupSelect(){
	  if(thisForm.p_popup[0].checked == true) { //Y
	    //thisForm.p_popsize1.value = "686";
	    //thisForm.p_popsize2.value = "600";
	    thisForm.p_popposition1.value = "0";
	    thisForm.p_popposition2.value = "0";
	    thisForm.p_sdate.value = "";
	    thisForm.p_edate.value = "";

	    thisForm.p_popposition1.disabled = false;
	    thisForm.p_popposition2.disabled = false;
	    thisForm.p_sdate.disabled = false;
	    thisForm.p_edate.disabled = false;
    	thisForm.p_useframe.disabled = false;
    	thisForm.p_uselist.disabled = false;
	    
	    if(thisForm.p_useframe.checked == true) {
		    thisForm.p_popsize1.disabled = false;
		    thisForm.p_popsize2.disabled = false;
	    } else {
	    	thisForm.p_popsize1.disabled = true;
		    thisForm.p_popsize2.disabled = true;
	    }

	    for(j = 0; j < thisForm.p_tem_type.length; j++){
		    thisForm.p_tem_type[j].disabled = false;
		}

	    thisForm.p_popsize1.disabled = false;
	    thisForm.p_popsize2.disabled = false;
	  }
	  
	  else if(thisForm.p_popup[1].checked == true){ //N
	    thisForm.p_popsize1.value = "";
	    thisForm.p_popsize2.value = "";
	    thisForm.p_popposition1.value = "";
	    thisForm.p_popposition2.value = "";
	    thisForm.p_sdate.value = "";
	    thisForm.p_edate.value = "";
	    
	    thisForm.p_popsize1.disabled = true;
	    thisForm.p_popsize2.disabled = true;
	    thisForm.p_popposition1.disabled = true;
	    thisForm.p_popposition2.disabled = true;
	    thisForm.p_sdate.disabled = true;
	    thisForm.p_edate.disabled = true;
	    thisForm.p_useframe.disabled = true;
	    thisForm.p_uselist.disabled = true;

	    for(j = 0; j < thisForm.p_tem_type.length; j++){
		    thisForm.p_tem_type[j].checked = false;
		    thisForm.p_tem_type[j].disabled = true;
		}
	  
	  } 
	}

	//작성내용만보기
	function whenUseframe(){
		  if(thisForm.p_useframe.checked == true){ //N
		    thisForm.p_popsize1.disabled = false;
		    thisForm.p_popsize2.disabled = false;
		    thisForm.p_popsize1.value = "";
		    thisForm.p_popsize2.value = "";

		    for(j = 0; j < thisForm.p_tem_type.length; j++){
			    thisForm.p_tem_type[j].checked = false;
			    thisForm.p_tem_type[j].disabled = true;
			}
			
		  }else{
		    thisForm.p_popsize1.disabled = true;
		    thisForm.p_popsize2.disabled = true;
		    //thisForm.p_popsize1.value = "686";
		    //thisForm.p_popsize2.value = "600";

		    for(j = 0; j < thisForm.p_tem_type.length; j++){
			    thisForm.p_tem_type[j].disabled = false;
			}
		  }
		}

	//473*387
	//600*500
	//560*696
	function whenUseTemType(type){
		if(type == 'A'){
		    thisForm.p_popsize1.value = "473";
		    thisForm.p_popsize2.value = "387";
		    thisForm.p_popsize1.disabled = false;
		    thisForm.p_popsize2.disabled = false;
	    }else if(type == 'B'){
		    thisForm.p_popsize1.value = "600";
		    thisForm.p_popsize2.value = "500";
		    thisForm.p_popsize1.disabled = false;
		    thisForm.p_popsize2.disabled = false;
		}else if(type == 'C'){
		    thisForm.p_popsize1.value = "560";
		    thisForm.p_popsize2.value = "696";
		    thisForm.p_popsize1.disabled = false;
		    thisForm.p_popsize2.disabled = false;
		    
	    }		
	}


	//팝업미리보기
	function popUpPreview(){
		var form = thisForm;
		var useframe = form.p_useframe.checked ? "Y" : "N";
		
		var  width  = thisForm.p_popsize1.value;
		var  height = thisForm.p_popsize2.value;
		var  left   = thisForm.p_popposition1.value;
		var  top    = thisForm.p_popposition2.value;
        var  tem_type = "";
        
		for(j = 0; j < thisForm.p_tem_type.length; j++){
			if(thisForm.p_tem_type[j].checked == true){
				tem_type = thisForm.p_tem_type[j].value;
				break;
			}
	    }

		if(useframe == "N" && tem_type == ""){
			alert("팝업 템플릿타입을 선택하여주십시오");
			return;
		}
		if(form.p_popup[0].checked) {
			var url = "/html/pop/pop_" + tem_type + ".html";
			window.open(url, "popUpPreviewWindowPop", "left=" + left + ", top=" + top+ ", width="+width+", height=" + height + ", toolbar=n, menubar=no, statusbar=no, scrollbar=yes, resizable=no");
		} else {
			alert("팝업여부가 Y일때만 미리보기를 보실 수 있습니다.")
		}
	}

/*
 gubun			구분
 reg_id			등록자아이디	
 reg_DATE		등록일자
 reqdate_from	요청일자 from
 reqdate_to		요청일자 to
 finish_date	완료일자
 proc_rate		처리율
 req_title		요청제목
 req_content	요청내용
 list_seq		리스트순번
 busy_gubun		긴급구분  
 */
	function whenNoticeSave(mode) {
//        alert("1");
        if (blankCheck(thisForm.reqdate_from.value)) {
            alert("요청일자(FROM)를 입력하세요");
            thisForm.reqdate_from.focus();
            return;
        }

        if (blankCheck(thisForm.reqdate_to.value)) {
            alert("요청일자(TO)를 입력하세요");
            thisForm.reqdate_to.focus();
            return;
        }

        if (blankCheck(thisForm.req_title.value)) {
            alert("요청제목을 입력하세요");
            thisForm.req_title.focus();
            return;
        }
        
        if (realsize(thisForm.req_title.value) > 300) {
            alert("요청제목은 한글기준 150자를 초과하지 못합니다.");
            thisForm.req_title.focus();
            return;
        }
        
        /* 
		if (document.all.ObjEditor.style.display == "none") {
			thisForm.p_content.value = thisForm.txtDetail.value;
		} else {
			thisForm.p_content.value = ObjEditor.document.all.tags("html")[0].outerHTML;
			thisForm.p_content.value.replace("&","&amp;");
		} */
//        alert("2");
		//smart Editor
	    oEditors.getById["req_content"].exec("UPDATE_CONTENTS_FIELD", []);
	    thisForm.req_content_val.value =document.getElementById("req_content").value;
	    document.getElementById("req_content").value="";
		
	    //alert(thisForm.req_content_val.value);
	    
        thisForm.pageIndex.value = 1;
        thisForm.action = "/adm/hom/srs/srSystemActionPage.do";
        thisForm.p_process.value = mode;
        thisForm.target = "_self";
        thisForm.submit();
    }
	
	
	//smart Editor	
	var oEditors = [];
	nhn.husky.EZCreator.createInIFrame({
	oAppRef: oEditors,
	elPlaceHolder: "req_content",
	sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin.html",
	//sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin_noimg.html",
	fCreator: "createSEditor2"
	});
	
	//textArea에 이미지 첨부
	var fileName="";
	var upfileName="";
	function pasteHTML(filepath){
	    var sHTML = '<img src="<%=request.getContextPath()%>/smartEditor/photo_uploader/uploadFolder/'+fileName+'"'+" alt="+upfileName+">";

	    oEditors.getById["req_content"].exec("PASTE_HTML", [sHTML]); 

	}
	
</script>