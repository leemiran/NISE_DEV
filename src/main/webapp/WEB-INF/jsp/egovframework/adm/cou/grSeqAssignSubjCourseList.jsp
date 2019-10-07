<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
	<input type="hidden" name="p_grcode"    value="${p_grcode}">
	<input type="hidden" name="p_gyear"     value="${p_gyear}">
	<input type="hidden" name="p_grseq" 	value="${p_grseq}">
	
	
	
	<input type="hidden" name="p_course" 	value="${view.course}">
	<input type="hidden" name="p_cyear" 	value="${view.cyear}">
	<input type="hidden" name="p_courseseq" 	value="${view.courseseq}">
	
	
	<input type="hidden" name="p_process" 	value="">
    
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
  
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>
			일괄 과정 지정
			</h2>
		</div>
		
		
<div class="tdtitList">
            <table width="100%">
                      <colgroup>
                            <col width="15%" />
                            <col width="85%" />                            
                      </colgroup>
                      <thead>
                            <tr>
                                  <th>교육그룹</th>
                                  <td><strong>원격교육연수원</strong></td>
                            </tr>
                     </thead>    
                     <tbody>
                            <tr>
                                  <th>교육기수</th>
                                  <td><strong><c:out value="${view.grseqnm}"></c:out> (<c:out value="${p_gyear}"></c:out>년 <c:out value="${p_grseq}"></c:out>기)</strong></td>
                            </tr>
                      </tbody>
             </table>
</div>



<!-- 검색창  -->		
<%@include file="./incAssignSubjSeqSearch.jsp" %>
<!-- 검색창  -->	







<!-- 1일 최대학습량 -->
        <div class="tbDetail">
            <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                    <col width="15%" />
                    <col width="85%" />                                       
                </colgroup>
                <tbody>
                    <tr>
                        <th>1일 최대학습량</th>
                        <td><input name="p_edulimit" type="text" size="3" maxlength="3" value="0"> % <a href="#none" onclick="whenEdulimit()" class="btn_save"><span>저장</span></a></td>                                      
                    </tr>                                   
                </tbody>
            </table>				
		</div>
        <!-- // 1일 최대학습량 -->
        
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
                    <tr>
                        <th>과정설문지선택</th>
                        <td>
                        	<ui:code id="p_sulpapernum" selectItem="${view.sulpapernum}" gubun="sulPaper" codetype=""  upper="" title="과정설문지" className="" type="select" selectTitle="==  ==  == =설문지를 선택하세요 ==  ==  ==" event="" />
                        </td>
                        <th>수강신청취소기간</th>
                        <td>수강신청 종료일로 부터 <input type="text" name="p_canceldays" value="0" size="5" > 일</td>                       
                    </tr>
                    <tr>
                        <th>수강신청시작일시</th>
                        <td>
                        	<input type="text" value="" size="10" name="p_propstart1" maxlength="10" readonly/>
								<a href="#none" onclick="popUpCalendar(this, document.all.p_propstart1, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
								일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="p_propstart2">
									<c:out value="${fn2:getDateOptions('0', '23', '0')}" escapeXml="false"></c:out>
								</select> 시
                                <a href="#none" onclick="whenclick(document.all.p_propstart1)" class="btn_del"><span>삭제</span></a><br />
                                <input type="hidden" name="p_propstart" value="">
                        </td>
                        <th>수강신청종료일시</th>
                        <td>
                      		<input type="text" value="" size="10" name="p_propend1" maxlength="10" readonly/>
								<a href="#none" onclick="popUpCalendar(this, document.all.p_propend1, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
								일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="p_propend2">
									<c:out value="${fn2:getDateOptions('0', '23', '23')}" escapeXml="false"></c:out>
								</select> 시
                                <a href="#none" onclick="whenclick(document.all.p_propend1)" class="btn_del"><span>삭제</span></a>
                                <input type="hidden" name="p_propend" value="">
                        </td>                        
                    </tr>
                    <tr>
                          <th>학습 시작일시</th>
                          <td>
                          	  <input type="text" value="" size="10" name="p_edustart1" maxlength="10" readonly/>
								<a href="#none" onclick="popUpCalendar(this, document.all.p_edustart1, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
								일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="p_edustart2">
									<c:out value="${fn2:getDateOptions('0', '23', '0')}" escapeXml="false"></c:out>
								</select> 시
                                <a href="#none" onclick="whenclick(document.all.p_edustart1)" class="btn_del"><span>삭제</span></a><br />
                                <input type="hidden" name="p_edustart" value="">
                          </td>
                          <th>학습 종료일시</th>
                          <td>
                          		<input type="text" value="" size="10" name="p_eduend1" maxlength="10" readonly/>
								<a href="#none" onclick="popUpCalendar(this, document.all.p_eduend1, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
								일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="p_eduend2">
									<c:out value="${fn2:getDateOptions('0', '23', '23')}" escapeXml="false"></c:out>
								</select> 시
                                <a href="#none" onclick="whenclick(document.all.p_eduend1)" class="btn_del"><span>삭제</span></a>
                                <input type="hidden" name="p_eduend" value="">
                          </td>                        
                    </tr>                    
                </tbody>
            </table>				
		</div>
        <!-- // detail wrap -->
        
        
        
        
    <!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="4%"/>
                    <col width="8%"/>
                    <col width="8%"/>
                    <col width="8%"/>
                    <col width="8%"/>
                    <col width="%"/>
                    <col width="8%"/>
                    <col width="5%"/>
                    <col width="8%"/>                                                        			
		    </colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row"><a href="javascript:whenOrder('ldateyear')">개설년도</a></th>
						<th scope="row"><a href="javascript:whenOrder('ldateyear')">과정분류</a></th>
						<th scope="row"><a href="javascript:whenOrder('ldateyear')">교육구분</a></th>
						<th scope="row"><a href="javascript:whenOrder('ldateyear')">코드</a></th>
						<th scope="row"><a href="javascript:whenOrder('ldateyear')">과정명</a></th>
						<th scope="row">담당자</th>
						<th scope="row">사용</th>
						<th scope="row"><input type="checkbox" name="p_chkeckall" id="p_chkeckall" class="borderNone" onclick="chkeckall()"/></th>											
				  </tr>
				</thead>
				<tbody>
<c:forEach items="${list}" var="result" varStatus="status" >				
					<tr>
						<td class="num"><c:out value="${status.count}"/></td>
						<td class="num"><c:out value="${result.ldateyear}"/></td>
						<td><c:out value="${result.classname}"/></td>
						<td class="num"><c:out value="${result.isonoff}"/></td>
						<td><c:out value="${result.subjcourse}"/></td>
						<td class="left"><c:out value="${result.coursenm}"/></td>
						<td><c:out value="${result.musernm}"/></td>
						<td class="num"><c:out value="${result.isuse}"/></td>
						<td>
						<c:if test="${result.cnts > 0}">
                        	<font color="red"><c:out value="${result.cnts}"/>개</font>
                        </c:if>
                        <c:if test="${result.cnts == 0}">
                        	<input type="checkbox" name="_Array_p_chk" value="${result.subjcourse}">
                        </c:if>
						</td>						
				  </tr>
</c:forEach>				  
				</tbody>
			</table>
		</div>
		<!-- list table-->    

        
        <!-- button -->
		<ul class="btnCen">
			<li><a href="#none" class="btn02" onclick="whenSubmit();"><span>저장</span></a></li>
			
			<li><a href="#none" class="btn03" onclick="window.close();"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->    
		
	</div>
</div>
</form>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
var frm = document.<c:out value="${gsPopForm}"/>;

function doPageList()
{
	frm.action='/adm/cou/grSeqAssignSubjCourseList.do';
	frm.target = "_self";
	frm.submit();
}


//날자 입력폼 삭제
function whenclick(caldate) {
	caldate.value = "";
}

//전체선택 / 취소
function chkeckall(){
	if(frm.p_chkeckall.checked){
		whenAllSelect();
	}
	else{
		whenAllSelectCancel();
	}
}
//전체선택
function whenAllSelect() {
	if(frm.all['_Array_p_chk']) {
		if (frm._Array_p_chk.length > 0) {
			for (i=0; i<frm._Array_p_chk.length; i++) {
				frm._Array_p_chk[i].checked = true;
			}
		} else {
			frm._Array_p_chk.checked = true;
		}
	}
}

// 전체선택취소
function whenAllSelectCancel() {
	if(frm.all['_Array_p_chk']) {
		if (frm._Array_p_chk.length > 0) {
			for (i=0; i<frm._Array_p_chk.length; i++) {
				frm._Array_p_chk[i].checked = false;
			}
		} else {
			frm._Array_p_chk.checked = false;
		}
	}
}

//일일 최대학습량 저장
function whenEdulimit() {
	frm.p_process.value="edulimit";
	frm.action='/adm/cou/grSeqAssignSubjCourseAction.do';
	frm.target = "_self";
	frm.submit();
}


/* ********************************************************
* 정렬처리 함수
******************************************************** */
function whenOrder(column) {
	frm.search_orderType.value = (frm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	frm.search_orderColumn.value = column;
	frm.action = "/adm/cou/grSeqAssignSubjCourseList.do";
	frm.target = "_self";
	frm.submit();
	
}



// 교육기수 정보 저장
function whenSubmit(){
	var cnt_chked=0;
	ff = frm;
	
	dateChk();
	for(var i=0;i<frm.length;i++){
		if(frm.elements[i].name=="_Array_p_chk" && frm.elements[i].checked==true) {
			cnt_chked++;
		}
	}

	if (cnt_chked==0){
		alert("선택된 과정정보가 없습니다");
		return;
	}


	if (ff.p_propstart.value > ff.p_propend.value) {
		alert("수강신청 시작일은 수강신청 종료일 이전이어야 합니다.");
		return;
	}

	if(confirm("선택된 과정을 일괄 저장하시겠습니까?")) {
		frm.p_process.value = "assignSave";
		frm.action='/adm/cou/grSeqAssignSubjCourseAction.do';
		frm.target = "_self";
		frm.submit();
	}
}

// 날자형식변환
function dateChk(){
	ff = frm;

	if(ff.p_propstart1.value != null && ff.p_propstart1.value != ""){
		ff.p_propstart.value=make_date(ff.p_propstart1.value)+ff.p_propstart2.options[ff.p_propstart2.selectedIndex].value;
		
	}
	if(ff.p_propend1.value != null && ff.p_propend1.value != ""){
		ff.p_propend.value=make_date(ff.p_propend1.value)+ff.p_propend2.options[ff.p_propend2.selectedIndex].value;
	}

	if(ff.p_edustart1.value != null && ff.p_edustart1.value != ""){
		ff.p_edustart.value=make_date(ff.p_edustart1.value)+ff.p_edustart2.options[ff.p_edustart2.selectedIndex].value;
	}
	if(ff.p_eduend1.value != null && ff.p_eduend1.value != ""){
		ff.p_eduend.value=make_date(ff.p_eduend1.value)+ff.p_eduend2.options[ff.p_eduend2.selectedIndex].value;
	}

	return true;
}

</script>