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
    <input type = "hidden" name="p_content"    value = "" />
    <input type = "hidden" name="p_isAllvalue" value = "" />
    <input type = "hidden" name="p_loginynPre" value = "N" />
    <input type = "hidden" name="p_gubun" value = "N" />
    
    	
	
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="30%" />
                <col width="70%" />
            </colgroup>
            <tbody>
            
            <c:if test="${empty p_seq}">
                <tr>
                    <th scope="row">작성자</th>
                    <td scope="row">${sessionScope.name}</td>
                </tr>
                <tr>
                    <th scope="row">등록일</th>
                    <td scope="row">${fn2:getFormatDateNow('yyyy.MM.dd')}</td>
                </tr>
            </c:if>
            <c:if test="${not empty p_seq}">
                <tr>
                    <th scope="row">작성자</th>
                    <td scope="row">${view.adname}</td>
                </tr>
                <tr>
                    <th scope="row">등록일</th>
                    <td scope="row">${fn2:getFormatDate(view.ldate, 'yyyy.MM.dd')}</td>
                </tr>
            </c:if>
           
           
                <tr>
                    <th scope="row">전체공지여부</th>
                    <td scope="row">
                    	<input type="checkbox" name="p_isAll" value="Y" 
                    	<c:if test="${view.isall eq 'Y'}">checked</c:if>
                    	> 
                    	전체공지(상단걸기) 사용시 선택하여 주십시오.
                    </td>
                </tr>
                <tr>
                    <th scope="row">로그인 유무</th>
                    <td scope="row">
                    	<input type="radio" name="p_login" value="AL" 
                    	<c:if test="${view.loginyn eq 'AL' || empty p_seq}">checked</c:if>
                    	onclick="whenLoginSelect()">전체&nbsp;
                    	<input type="radio" name="p_login" value="N" 
                    	<c:if test="${view.loginyn eq 'N'}">checked</c:if>
                    	onclick="whenLoginSelect()" >로그인전&nbsp;
                    	<input type="radio" name="p_login" value="Y" 
                    	<c:if test="${view.loginyn eq 'Y'}">checked</c:if>
                    	onclick="whenLoginSelect()" >로그인후
                    </td>
                </tr>
                <tr> 
		            <th scope="row">대상교육그룹<br/>유무</th>
		            <td scope="row">
		              <input type="radio" name="p_grcodegubun" value='Y' 
		              <c:if test="${view.grcodegubun eq 'Y'}">checked</c:if>
		              onclick="whenGrcodeGubunSelect()">전체교육그룹&nbsp;
		              <input type="radio" name="p_grcodegubun" value='N' 
		              <c:if test="${view.grcodegubun eq 'N' || empty p_seq}">checked</c:if>
		              onclick="whenGrcodeGubunSelect()">선택교육그룹&nbsp;
		            </td>
		          </tr>  
                <tr>
                    <th scope="row">팝업여부</th>
                    <td scope="row">
                    	<input type="radio" name="p_popup" value='Y' onclick="whenPopupSelect()"
                   		<c:if test="${view.popup eq 'Y'}">checked</c:if>
                    	>Y&nbsp;
              			<input type="radio" name="p_popup" value='N' onclick="whenPopupSelect()"
            			<c:if test="${view.popup eq 'N' || empty p_seq}">checked</c:if>
              			>N&nbsp;
                    </td>
                </tr>
                <tr>
                    <th scope="row">팝업설정</th>
                    <td scope="row">
                    	<!--<input name="p_sdate" type="text" size="12" maxlength="10" readonly 
                    	value="${fn2:getFormatDate(view.startdate, 'yyyy.MM.dd')}"  OnClick="popUpCalendar(this, this, 'yyyy-mm-dd')">
			             ~
			              <input name="p_edate" type="text" size="12" maxlength="10" readonly 
			              value="${fn2:getFormatDate(view.enddate, 'yyyy.MM.dd')}"  OnClick="popUpCalendar(this, this, 'yyyy-mm-dd')">
						  <br/>
						  사이즈 : 가로 <input type="text" size="5" name="p_popsize1" value="${view.popwidth}" maxlength="4"> 
						  / 세로 <input type="text" size="5" name="p_popsize2" value="${view.popheight}" maxlength="4">
			              <br/>
						  위치   : LEFT <input type="text" size="5" name="p_popposition1" value="${view.popxpos}" maxlength="3"> 
						  / TOP <input type="text" size="5" name="p_popposition2" value="${view.popypos}" maxlength="3">
						  <br/>
						  --><input type="checkbox" name="p_useframe" value='Y' 
						  <c:if test="${view.useframe eq 'Y'}">checked</c:if>
						  onclick="whenUseframe()">작성내용만보이기&nbsp;
						  <input type="checkbox" name="p_uselist"  value='Y' 
						  <c:if test="${view.uselist eq 'Y'}">checked</c:if>
						  >홈페이지리스트사용&nbsp;
						  <br/>
                    </td>
                </tr>
                <!--<tr>
                    <th scope="row">팝업템플릿타입</th>
                    <td scope="row">
                    	  <input type="radio" name="p_tem_type" value='A' 
                    	  <c:if test="${view.useframe eq 'Y' || view.popup eq 'N' || empty p_seq}">disabled</c:if>
                    	  <c:if test="${view.temType eq 'A'}">checked</c:if>
                    	  onclick="whenUseTemType(this.value);">Type A&nbsp;
			              <input type="radio" name="p_tem_type" value='B' 
			              <c:if test="${view.useframe eq 'Y' || view.popup eq 'N' || empty p_seq}">disabled</c:if>
			              <c:if test="${view.temType eq 'B'}">checked</c:if>
			              onclick="whenUseTemType(this.value);">Type B&nbsp;
			              <input type="radio" name="p_tem_type" value='C' 
			              <c:if test="${view.useframe eq 'Y' || view.popup eq 'N' || empty p_seq}">disabled</c:if>
			              <c:if test="${view.temType eq 'C'}">checked</c:if>
			              onclick="whenUseTemType(this.value);">Type C&nbsp;
			              <a href="javascript:popUpPreview()" class="btn_search"><span>미리보기</span></a>
                    </td>
                </tr>
                --><tr>
                    <th scope="row">사용유무</th>
                    <td scope="row">
                    	<c:set var="p_useyn">Y</c:set>
                    	<c:if test="${not empty view.useyn}">
                    		<c:set var="p_useyn">${view.useyn}</c:set>
                    	</c:if>
                    	
                    	
                    	<ui:code id="p_use" selectItem="${p_useyn}" gubun="defaultYn" codetype=""  upper="" title="사용유무" className="" type="radio" selectTitle="" event="" />
                    </td>
                </tr>
                <tr>
                    <th scope="row">공지구분</th>
                    <td scope="row">
                    	<ui:code id="p_notice_gubun" selectItem="${view.noticeGubun}" gubun="" codetype="0076"  upper="" title="" className="" type="select" selectTitle="" event="" />
                    </td>
                </tr>
                <tr>
                    <th scope="row">제목</th>
                    <td scope="row">
                    	<input type="text" name="p_adtitle" size="100" maxlength="80" value="${view.adtitle}">
                    </td>
                </tr>
                
                <tr>
                    <th scope="row">내용</th>
                    <td style="padding-left:0px" scope="row">
                    	<%-- <c:set var="content">${view.adcontent}</c:set>
                    	<c:set var="width"  value="600"/>
                    	<c:set var="height" value="400" />
                    	<%@ include file="/WEB-INF/jsp/egovframework/com/lib/DhtmlEditor.jsp" %>
						<input type="hidden" name="p_adcontent" id="p_adcontent"> --%>
						
						<textarea name="textAreaContent" id="textAreaContent" rows="10" cols="100" style="width:100%; height:412px; display:none;">${view.adcontent}</textarea>
                	<input type="hidden" name="p_adcontent" id="p_adcontent">
                	
                    </td>
                </tr>
                <tr>
                    <th scope="row">첨부파일</th>
                    <td scope="row">
                    	
                    	
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
		frm.action = "/adm/hom/not/selectNoticeList.do"
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


	function whenNoticeSave(mode) {
     //   var st_date = make_date(thisForm.p_sdate.value);
     //   var ed_date = make_date(thisForm.p_edate.value);
        
        if (blankCheck(thisForm.p_adtitle.value)) {
            alert("제목을 입력하세요");
            thisForm.p_adtitle.focus();
            return;
        }
        
        if (realsize(thisForm.p_adtitle.value) > 200) {
            alert("제목은 한글기준 100자를 초과하지 못합니다.");
            thisForm.p_adtitle.focus();
            return;
        }
        /* 

		if (document.all.ObjEditor.style.display == "none") {
			thisForm.p_content.value = thisForm.txtDetail.value;
		} else {
			thisForm.p_content.value = ObjEditor.document.all.tags("html")[0].outerHTML;
			thisForm.p_content.value.replace("&","&amp;");
		} */

		//smart Editor
	    oEditors.getById["textAreaContent"].exec("UPDATE_CONTENTS_FIELD", []);
	    thisForm.p_content.value =document.getElementById("textAreaContent").value;
	    document.getElementById("textAreaContent").value="";
	    
	    
        if(thisForm.p_isAll.checked == true){
            thisForm.p_isAllvalue.value = 'Y';
        }else {
            thisForm.p_isAllvalue.value = 'N';
        }
        
     //   if(thisForm.p_popup[0].checked == true){   //팝업설정시

     //     if(st_date > ed_date){
    //       alert("시작일이 종료일보다 큽니다.");
     //       return;
     //     }

     //     if (thisForm.p_popsize1.value == "") {
        //    alert("가로사이즈를 입력하여주십시오");
       ///     thisForm.p_popsize1.focus();
       //     return;
  //        }
          
         // if (!number_chk_noalert(thisForm.p_popsize1.value)) {
         //   alert('가로사이즈가 잘못입력되었습니다.');
         //   return;
        //  }

         // if (thisForm.p_popsize2.value == "") {
        //    alert("세로사이즈를 입력하여주십시오");
        //    thisForm.p_popsize2.focus();
         //   return;
         // }
          
        //  if (!number_chk_noalert(thisForm.p_popsize2.value)) {
        //    alert('세로사이즈가 잘못입력되었습니다.');
        //    return false;
        //  }
          
        //  if (thisForm.p_popposition1.value == "") {
        //    alert("X축 값을 입력하여주십시오");
        //    thisForm.p_popposition1.focus();
        //    return;
        //  }
          
        //  if (!number_chk_noalert(thisForm.p_popposition1.value)) {
        //    alert('X축 값이 잘못입력되었습니다.');
        //    return false;
        //  }
          
        //  if (thisForm.p_popposition2.value == "") {
        //    alert("Y축 값을 입력하여주십시오");
        //    thisForm.p_popposition2.focus();
        //    return;
        //  }
          
         // if (!number_chk_noalert(thisForm.p_popposition2.value)) {
        //    alert('Y축 값이 잘못입력되었습니다.');
         //   return false;
         // }
          
          
    //      if(thisForm.p_uselist.checked == true){
     //       thisForm.p_uselist.value = 'Y';
       //   }
      //    if(thisForm.p_useframe.checked == true){
      //      thisForm.p_useframe.value = 'Y';
      ///    }
      //  } 

        
     //   thisForm.p_popsize1.disabled = false;
    //    thisForm.p_popsize2.disabled = false;
        
      //  thisForm.p_startdate.value  = st_date;
     //   thisForm.p_enddate.value    = ed_date;

        
        thisForm.pageIndex.value = 1;
        thisForm.action = "/adm/hom/not/noticeActionPage.do";
        thisForm.p_process.value = mode;
        thisForm.target = "_self";
        thisForm.submit();
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
	
</script>