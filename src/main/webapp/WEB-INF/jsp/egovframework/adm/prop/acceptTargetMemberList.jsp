<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
	<input type="hidden" id="p_grcode"  	name="p_grcode"    	value="${sessionScope.grcode}">
	<input type="hidden" id="p_grseq"  		name="p_grseq"    	value="${p_grseq}">
	<input type="hidden" id="p_subj"  		name="p_subj"    	value="${p_subj}">
	<input type="hidden" id="p_year"  		name="p_year"    	value="${p_year}">
	<input type="hidden" id="p_subjseq"  	name="p_subjseq"    value="${p_subjseq}">
<input type="hidden" name="p_subjseqgr" 	value="${view.subjseqgr}">
<input type="hidden" name="p_isclosed" 		value="${view.isclosed}">
<input type="hidden" name="p_studentlimit" 	value="${view.studentlimit}">
<input type="hidden" name="p_isaddpossible" value="${view.isaddpossible}">
<input type="hidden" name="p_propcnt" 		value="${view.propcnt}">
<input type="hidden" name="p_eduterm" 		value="${view.eduterm}">
<input type="hidden" name="p_isonoff" 		value="${view.isonoff}">
	
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>교육대상자 선정</h2>
         </div>
		<!-- contents -->
        
		<!-- search wrap -->
		<div class="searchWrap txtL" style="margin:10px;">
			<ul class="datewrap">
				<li>
				<strong class="shTit">검색 :</strong>
					
					<select name="search_group">
						<option value="name" 	<c:if test="${search_group == 'name'}">selected</c:if>>이름</option>
						<option value="userid" 	<c:if test="${search_group == 'userid'}">selected</c:if>>ID</option>
					</select>
					
				</li>
				<li class="ML10">
				<input name="search_word" type="text" style="width:300px;" value="${search_word}" onkeydown="if(event.keyCode == 13) doPageList()"/>
				</li> 
                <a href="#none" onclick="doPageList()" class="btn_search"><span>GO</span></a>         
	    </ul>			
	    </div>
        <!-- //search wrap -->
        
     	 <div class="searchWrap txtL" style="margin:10px;">
			<ul class="datewrap">
				<li><strong class="shTit">교육그룹 : </strong>원격교육연수원</li>
				<li class="ML10"><strong class="shTit">연도 : </strong><c:out value="${sessionScope.ses_search_year}"/></li>                         
	  		</ul>	
            <div class="shLine"></div>	
            <div class="datewrap">
				<li><strong class="shTit">과정/기수  : </strong><c:out value="${sessionScope.ses_search_subjinfo}"/></li>
	  		</div>	
 		 </div>
        
		<!-- //search wrap -->
        <div class="listTop MR10" style="padding-bottom:0px;">
				<div class="btnR MR05">
               		<a href="#none" onclick="whenSave()" class="btn01"><span>저장</span></a>
                </div>               
   		</div>
      
          <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="%" />
                        <col width="%" /> 
                        <col width="%" />
                        <col width="%" />
                        <col width="%" />
                        <col width="%" />
                        <col width="%" />                                              				
                    </colgroup>
                    <tbody>
                    	 <tr>
                            <th scope="col" class="txtC" colspan="8">
                    		※ 동일과정을 여러번 수강할 수 없습니다. 해당 사용자들은 '-'로 표시됩니다.
                    		</th>
                    	</tr>	
                        <tr>
                            <th scope="col" class="txtC">No</th>
                            <th scope="col" class="txtC">ID</th>
                            <th scope="col" class="txtC">성명</th>
                            <th scope="col" class="txtC">전화번호</th>
                            <th scope="col" class="txtC">휴대전화</th>
                            <th scope="col" class="txtC">이메일주소</th>
                            <th scope="col" class="txtC"><input type="checkbox" name="p_chkeckall" onclick="chkeckall()" class="borderNone"/></th>
                        </tr>
<c:forEach items="${list}" var="result" varStatus="status" >                        
                        <tr>
                            <td class="txtC"><c:out value="${status.count}"/></td>
                            <td class="txtC"><c:out value="${result.userid}"/></td>
                            <td class="txtC"><c:out value="${result.name}"/></td>
                            <td class="txtC"><c:out value="${result.hometel}"/></td>
                            <td class="txtC"><c:out value="${result.handphone}"/></td>
                            <td class="txtC"><c:out value="${result.email}"/></td>
                            <td class="txtC">
                            <c:if test="${result.ispropose eq 'Y'}">
                            	완료
                            </c:if>
                            <c:if test="${result.ispropose ne 'Y'}">
                            	<c:if test="${result.propcnt > 0}">
                            		-
                            	</c:if>
                            	<c:if test="${result.propcnt == 0}">
                            		<input type="checkbox" name="_Array_p_checks" value="${result.userid}" >
                            	</c:if>
<!--                            	<c:out value="${result.propcnt}"/>-->
                            </c:if>
                            </td>
                        </tr>                        	
</c:forEach>
                    </tbody>
                </table>
    	</div>        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01"><span>닫기</span></a></li>
        </ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->


</form>

<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var thisForm = document.<c:out value="${gsPopForm}"/>;

/* ********************************************************
 * 페이징처리 함수
 ******************************************************** */
function doPageList() {
	thisForm.action = "/adm/prop/acceptTargetMemberList.do";
	thisForm.target = "_self";
	thisForm.submit();
}


//전체체크시 데이타 유효성 체크
function chkeckall(){
	if(thisForm.p_isclosed.value == "Y"){
		alert("수료되어 해당기수에는 추가할수 없습니다.");
		thisForm.p_chkeckall.checked = false;
		return;
	}

	if(thisForm.p_chkeckall.checked){
		whenAllSelect();
	} else {
		whenAllSelectCancel();
	}

}

//전체선택
function whenAllSelect() {
	if(thisForm._Array_p_checks) {
		if (thisForm._Array_p_checks.length > 0) {
			for (i=0; i<thisForm._Array_p_checks.length; i++) {
				thisForm._Array_p_checks[i].checked = true;
			}
		} else {
			thisForm._Array_p_checks.checked = true;
		}
	}
}

//전체선택해제
function whenAllSelectCancel() {
	if(thisForm._Array_p_checks) {
		if (thisForm._Array_p_checks.length > 0) {
			for (i=0; i<thisForm._Array_p_checks.length; i++) {
				thisForm._Array_p_checks[i].checked = false;
			}
		} else {
			thisForm._Array_p_checks.checked = false;
		}
	}
}

//체크박스 선택 체크
function chkSelected() {
	var selectedcnt = 0;
	if(thisForm._Array_p_checks) {
		if (thisForm._Array_p_checks.length > 0) {
			for (i=0; i<thisForm._Array_p_checks.length; i++) {
				if (thisForm._Array_p_checks[i].checked == true) {
					selectedcnt++;
				}
			}
		} else {
			if (thisForm._Array_p_checks.checked == true) {
				selectedcnt++;
			}
		}
	}
	return selectedcnt;
}


// 저장
function whenSave() {

	if (chkSelected() < 1) {
		alert("대상자를 선택하세요.");
		return;
	}

	thisForm.action='/adm/prop/acceptTargetMemberAction.do';
	thisForm.target = "_self";
	thisForm.submit();
}



</script>