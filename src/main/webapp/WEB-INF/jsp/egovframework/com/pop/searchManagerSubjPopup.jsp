<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:500px">
    	<div class="tit_bg">
			<h2>과정검색</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" name="pageIndex" id="pageIndex" value="1"/>
		
		<div  style="width:100%; text-align: left">				
					<strong class="shTit">과정명</strong>
					<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}"/>
					<a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a>
					
					<span style="padding-left:270px;"><a href="#none" class="btn_search" onclick="sendSubj()"><span>과정선택</span></a></span>	
			
		</div>		
		</form>
		
		
		<form name="gsMainForm" id="gsMainForm" method="post" action="">
		<!-- contents -->
		<div class="tbList">
			<table summary="선택, 년도, 기수, 과정코드, 과정명로 구성" cellspacing="0" width="100%">
                <caption>과정검색</caption>
                <colgroup>
				<col width="40px" />
				<col width="50px"/>				
				<col />
				<col width="50px"/>
				
			</colgroup>
			<thead>
				<tr>
					<!-- <th scope="row">선택</th> -->
					<th scope="row">년도</th>
				<!-- 	<th scope="row">기수</th> -->
					<th scope="row">과정코드</th>
					<th scope="row">과정명</th>
					<th scope="row"><input type="checkbox" name="checkedAlls" onclick="chkeckall()"></th>					
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<%-- <td><input type="radio" name="no" value="<c:out value="${result.subj}"/>" onclick="javascipt:selectSubj('<c:out value="${result.subj}"/>' ,'<c:out value="${result.subjnm}"/>','<c:out value="${p_key2}"/>','','')" style="cursor:pointer"/></td> --%>
					<td><c:out value="${result.year}"/></td>
					<%-- <td><c:out value="${result.subjseq}"/></td> --%>
					<td><c:out value="${result.subj}"/></td>
					<td class="left"><c:out value="${result.subjnm}"/></td>
					<td><input type="checkbox" id="subjVal" name="subjVal" value="<c:out value="${result.subj}"/>||<c:out value="${result.subjnm}"/>||<c:out value="${p_key2}"/>"></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		</form>
		<!-- // contents -->
		<!-- 페이징 시작 -->
		<div class="paging">
			<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="linkPage"/>
		</div>
		<!-- 페이징 끝 -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>

<script type="text/javascript">
//<![CDATA[
           
   var thisForm = document.gsMainForm;

	function searchList(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.action = "/com/pop/searchManagerSubjPopup.do";
		frm.submit();
	}
	
	function linkPage(idx){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.pageIndex.value = idx;
		frm.action = "/com/pop/searchManagerSubjPopup.do";
		frm.submit();
	}

	function selectSubj(subj , subjnm, tmp1, tmp2, tmp3){
		opener.receiveSubj(subj , subjnm, tmp1, tmp2, tmp3);
        self.close();
	}
	
	function sendSubj(){		
		for(i=0; thisForm.subjVal.length; i++){
			if(thisForm.subjVal[i].checked == true){
				var subjVal =thisForm.subjVal[i].value;
				var subjVal_a = subjVal.split("||");
				selectSubj(subjVal_a[0] , subjVal_a[1], '', '', '');
			}
		}		
	}
	
	//전체선택
	function whenAllSelect() {
		//alert(thisForm.all['_Array_p_checks']);
		if(document.getElementsByTagName('subjVal')) {
			if (thisForm.subjVal.length > 0) {
				for (i=0; i<thisForm.subjVal.length; i++) {
					thisForm.subjVal[i].checked = true;
				}
			} else {
				thisForm.subjVal.checked = true;
			}
		}
	}	 


	//전체선택취소
	function whenAllSelectCancel() {
		if(document.getElementsByTagName('subjVal')) {
			if (thisForm.subjVal.length > 0) {
				for (i=0; i<thisForm.subjVal.length; i++) {
					thisForm.subjVal[i].checked = false;
				}
			} else {
				thisForm.subjVal.checked = false;
			}
		}
	}
	
	function chkeckall(){
	    if(thisForm.checkedAlls.checked){
	      whenAllSelect();
	    }
	    else{
	      whenAllSelectCancel();
	    }
	}
	
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>