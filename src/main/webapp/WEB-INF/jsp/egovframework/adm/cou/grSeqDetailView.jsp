<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">

	<input type="hidden" name="p_subj"      value="${p_subj}">
	<input type="hidden" name="p_year"      value="${p_year}">
	<input type="hidden" name="p_subjseq"   value="${p_subjseq}"> 
	<input type="hidden" name="p_isonoff"		value="ON"                          >
	<input type="hidden" name="p_grcode"    value="${view.grcode}">
	<input type="hidden" name="p_gyear"     value="${view.gyear}">
	<input type="hidden" name="p_grseq" 	value="${view.grseq}">

	<input type="hidden" id="tmp1" 			name="tmp1"			value="">	
    <input type="hidden" name="p_gubun">
    <input type="hidden" name="p_key1">
    <input type="hidden" name="p_key2">
    <input type="hidden" name="p_process">
    

	
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>기수정보</h2>
         </div>
		<!-- contents -->
		<div class="popCon MR20">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="12%" />
                    <col width="5%" />
					<col width="20%" />
                    <col width="63%" />
				</colgroup>
				<tbody>
					<tr>
						<th rowspan="5" scope="col">일자정보</th>
						<td colspan="2" class="bold">수강신청 시작일시</td>
						<td>
                        	<span>
								<input type="text" value="${fn2:getFormatDate(view.propstart, 'yyyy-MM-dd')}" size="10" id="p_propstart1" name="p_propstart1" maxlength="10" readonly/>
								<a href="#none" onclick="popUpCalendar(this, document.all.p_propstart1, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
								
								<c:set var="p_propstart2">${fn2:getFormatDate(view.propstart, 'HH')}</c:set>
								
								<select name="p_propstart2">
									<c:out value="${fn2:getDateOptions('0', '23', p_propstart2)}" escapeXml="false"></c:out>
								</select> 시 ${view.propstart}
                                <a href="#none" onClick="whenclick(document.all.p_propstart1)" class="btn_del"><span>삭제</span></a>
                                
                                <input type="hidden" name="p_propstart"  value="${view.propstart}">
                          </span>
                        </td>
					</tr>
					<tr>
                          <td colspan="2" class="bold">수강신청 종료일시</td>
                          <td>
                                <span>
                                    <input type="text" value="${fn2:getFormatDate(view.propend, 'yyyy-MM-dd')}" size="10" name="p_propend1" maxlength="10" readonly/>
                                    <a href="#none" onclick="popUpCalendar(this, document.all.p_propend1, 'yyyy-mm-dd')" ><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
                                    
                                    <c:set var="p_propend2">${fn2:getFormatDate(view.propend, 'HH')}</c:set>
                                    
                                    <select name="p_propend2">
                                        <c:out value="${fn2:getDateOptions('0', '23', p_propend2)}" escapeXml="false"></c:out>
                                    </select> 시 ${view.propend}
                                    <a href="#none" onClick="whenclick(document.all.p_propend1)" class="btn_del"><span>삭제</span></a>
                                    
                                    <input type="hidden" name="p_propend"  value="${view.propend}">
                              </span>
                          </td>
				  </tr>
					<tr>
                          <td colspan="2" class="bold">학습 시작일시</td>
                          <td>
                          		<span>
                                    <input type="text" value="${fn2:getFormatDate(view.edustart, 'yyyy-MM-dd')}" size="10" name="p_edustart1" maxlength="10" readonly />
                                    <a href="#none" onclick="popUpCalendar(this, document.all.p_edustart1, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
                                    
                                    <c:set var="p_edustart2">${fn2:getFormatDate(view.edustart, 'HH')}</c:set>
                                    
                                    <select name="p_edustart2">
                                        <c:out value="${fn2:getDateOptions('0', '23', p_edustart2)}" escapeXml="false"></c:out>
                                    </select> 시 ${view.edustart}
                                    <a href="#none" onClick="whenclick(document.all.p_edustart1)" class="btn_del"><span>삭제</span></a>
                                    
                                    <input type="hidden" name="p_edustart"  value="${view.edustart}">
                          		</span>
                          </td>
				  </tr>
					<tr>
                          <td colspan="2" class="bold">학습 종료일시</td>
                          <td>
                          		<span>
                                    <input type="text" value="${fn2:getFormatDate(view.eduend, 'yyyy-MM-dd')}" size="10" name="p_eduend1" maxlength="10" readonly />
                                    <a href="#none" onclick="popUpCalendar(this, document.all.p_eduend1, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
                                    
                                    <c:set var="p_eduend2">${fn2:getFormatDate(view.eduend, 'HH')}</c:set>
                                    
                                    <select name="p_eduend2">
                                        <c:out value="${fn2:getDateOptions('0', '23', p_eduend2)}" escapeXml="false"></c:out>
                                    </select> 시 ${view.eduend}
                                    <a href="#none" onClick="whenclick(document.all.p_eduend1)" class="btn_del"><span>삭제</span></a>
                                    
                                    <input type="hidden" name="p_eduend"  value="${view.eduend}">
                         		</span>
                          </td>
				  </tr>
				  <tr>
                          <td colspan="2" class="bold">시험응시일시</td>
                          <td> 날짜 <input type="text" name="testday" size="20" maxlength="30" value="${view.testDay}"> 
                        	시간 <input type="text" name="testdaytime" size="20" maxlength="30" value="${view.testDayTime}">
                          </td>
				  </tr>
					<tr>
					  <!-- 
					  <th rowspan="19" scope="col">기본정보</th>
					   -->
					  <!-- 2017 추가 -->
					  <th rowspan="23" scope="col">기본정보</th>
					  <!-- 2017 추가 끝 -->
					  <td colspan="2" class="bold">교육그룹</td>
					  <td>국립특수교육원 [<c:out value="${sessionScope.grcode}"></c:out>]</td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">과정명</td>
					  <td>
                      	<input type="text" name="p_subjnm" value="${view.subjnm}" size="40" maxlength="40">
						[<c:out value="${view.subj}"></c:out>]
                      </td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">년도</td>
					  <td><c:out value="${view.year}"></c:out></td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">과정기수</td>
					  <td><input type="text" name="p_newsubjseq" value="${fn2:toNumber(view.subjseq)}" size="6" maxlength="4"></td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">수강신청취소기간</td>
					  <td>수강신청 종료일로 부터 <input type="text" name="p_canceldays" value="${view.canceldays}" size="5"> 일</td>
				  </tr>
					<tr>
					  <td colspan="2" rowspan="3" class="bold">담당자</td>
					  <td>아이디 :&nbsp;&nbsp;<input id="p_muserid" name="p_muserid" type="text" size="14" maxlength="13" value="${view.muserid}" readonly></td>
				  </tr>
					<tr>
					  <td>
						  이&nbsp;&nbsp;&nbsp;름 :&nbsp;&nbsp;<input id="p_museridnm" name="p_museridnm" type="text" size="14" maxlength="13" value="${view.musernm}" />
						 <a href="#none" onclick="searchMuser()" class="btn_search01"><span>조회</span></a>
					   </td>
				  </tr>
					<tr>
					  <td>연락처 :&nbsp;&nbsp;<input type="text" id="p_musertel" name="p_musertel" value="${view.musertel}" size="14" /></td>
				   </tr>
					<tr>
					  <td colspan="2" class="bold">학습자에게 보여주기</td>
					  <td>
                      		<ui:code id="p_isvisible" selectItem="${view.isvisible}" gubun="defaultYN" codetype=""  upper="" title="학습자에게 보여주기" className="" type="select" selectTitle="" event="" />
                      </td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">수강신청 자동승인여부</td>
					  <td>
                      		<ui:code id="p_proposetype" selectItem="${view.proposetype}" gubun="defaultYN" codetype=""  upper="" title="수강신청 자동승인여부" className="" type="select" selectTitle="" event="" />
                      		<strong>Y : 수강신청시 자동입과됩니다.</strong> 
                      </td>
				  </tr>
					<tr>
					  <!-- <td colspan="2" class="bold">수강료 납부방식</td> -->
					  <td colspan="2" class="bold">운영형태</td>
					  <td>
                      		<select id='p_ischarge' name='p_ischarge' class='' title='운영형태' onchange="whenIsCharge()" >
								<%-- <option value='C' <c:if test="${view.ischarge eq 'C'}">selected</c:if>>일반결제(전자결제, 교육청일괄납부)</option>
								<option value='S' <c:if test="${view.ischarge eq 'S'}">selected</c:if>>특별과정(교육청일괄납부)</option>
								<option value='F' <c:if test="${view.ischarge eq 'F'}">selected</c:if>>무료</option> --%>
								<option value='C' <c:if test="${view.ischarge eq 'C'}">selected</c:if>>정규</option>
								<option value='S' <c:if test="${view.ischarge eq 'S'}">selected</c:if>>특별</option>								
							</select>

                      </td>
				  </tr>
				  <tr>
				  	<td colspan="2" class="bold">자격</td>
				  	<td><input type="checkbox" name="licenseYn" value="Y" <c:if test="${view.licenseYn eq 'Y'}">checked</c:if>/></td>
				  </tr>
				  
				  <tr>
				  	<td colspan="2" class="bold">직무</td>
				  	<td><input type="checkbox" name="dutyYn" value="Y" <c:if test="${view.dutyYn eq 'Y'}">checked</c:if>/></td>
				  </tr>
                  
                  <tr>
					  <td colspan="2" class="bold">수강료(결제금액)</td>
					  <td>
					  <input name="p_biyong" type="text" size="8" maxlength="8" value="${view.biyong}" 
					  
					  <c:if test="${view.ischarge eq 'N'}">disabled</c:if>
					  
					  >원
					  
					  </td>
				  </tr>
				  <tr>
					  <td colspan="2" class="bold">교육청일괄납부 수강료(통계)</td>
					  <td>
					  <input name="p_biyong2" type="text" size="8" maxlength="8" value="${view.biyong2}" 
					  
					  <c:if test="${view.ischarge eq 'N'}">disabled</c:if>
					  
					  >원
					  
					  </td>
				  </tr>				  
				  
					<tr>
					  <td colspan="2" class="bold">정원</td>
					  <td><input name="p_studentlimit" type="text" size="4" maxlength="5" value="${view.studentlimit}"> 명</td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">복습가능여부</td>
					  <td>
                      		<ui:code id="p_isablereview" selectItem="${view.isablereview}" gubun="defaultYN" codetype=""  upper="" title="복습가능여부" className="" type="select" selectTitle="" event="whenIsablereview()" />
                            <input type="text" id="p_reviewdays" name="p_reviewdays" value="${view.reviewdays}" size="2" maxlength="2"/> 개월
                            
                            <span class="btnCen">					      		
					      		<a href="#none" onclick="reviewSubmit()" class="pop_btn01"><span>복습 / 수료처리 여부 저장</span></a>        
							</span>
		
                      </td>
				  </tr>
				  <tr>
					  <td colspan="2" class="bold">수료처리 여부</td>
					  <td>
                      		<ui:code id="p_isclosed" selectItem="${view.isclosed}" gubun="defaultYN" codetype=""  upper="" title="수료처리 여부" className="" type="select" selectTitle="" event="whenIsclosed()" />                            
                      </td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">교재여부</td>
					  <td>
                      		<ui:code id="p_usebook" selectItem="${view.usebook}" gubun="defaultYN" codetype=""  upper="" title="교재여부" className="" type="select" selectTitle="" event="whenUsebook()" />
                      </td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">교재명</td>
					  <td><input type="text" name="p_bookname" size="38" maxlength="100" value="${view.bookname}" ></td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">교재비</td>
					  <td><input type="text" name="p_bookprice" size="10" maxlength="5" value="${view.bookprice}" > 원</td>
				  </tr>
				  <tr>
					  <td colspan="2" class="bold">당해연도운영현황 보여주기</td>
					  <td>
                      		<ui:code id="p_isyearedustatus" selectItem="${view.isyearedustatus}" gubun="defaultYN" codetype=""  upper="" title="당해연도운영현황 보여주기" className="" type="select" selectTitle="" event="" />
                      </td>
				  </tr>
				  <!-- 2017 추가 -->
				  <tr>
					  <td colspan="2" class="bold">지역</td>
					  <td>
					  	<fmt:parseNumber var="yearToNumber" integerOnly="true" type="number" value="${p_year}" />
					  	<c:choose>
					  		<c:when test="${2017 <= yearToNumber}">
							  	<c:forEach var="areaCode" items="${areaCodeList}" varStatus="status">
							  		<label for="areaCode${status.count}">
							  			<input type="checkbox" name="areaCode" id="areaCode${status.count}" value="${areaCode.code}"
								  			<c:if test="${fn:contains(subjseqAreaCodeConcat, areaCode.code) }">
								  				checked
								  			</c:if>
							  			/>
							  			${areaCode.codenm}
							  		</label>
							  		<c:if test="${status.count == 1}"><br/></c:if>
							  	</c:forEach>
						  		<br/>
						  		<input type="checkbox" id="AllAreaChk"/><label for="AllAreaChk"> 지역 전체선택</label>
					  		</c:when>
					  		<c:otherwise><strong>지역 선택은 2017년 과정부터 가능합니다.</strong></c:otherwise>
					  	</c:choose>
                      </td>
				  </tr>
				  <!-- 2017 추가 끝 -->
				  
					<tr>
					  <th rowspan="15" scope="col">기타정보</th>
					  <td rowspan="6" class="ct bold">수<br />료<br />기<br />준</td>
					  <td class="bold">총 점</td>
					  <td><input type="text" name="p_gradscore" size="10" maxlength="5" value="${view.gradscore}"> 이상</td>
				  </tr>
					<tr>
					  <td class="bold">출석시험</td>
					  <td><input type="text" size="10" name="p_gradexam" value="${view.gradexam}"> 이상</td>
				  </tr>
					<tr>
					  <td class="bold">온라인시험</td>
					  <td><input type="text" size="10" name="p_gradftest" value="${view.gradftest}"> 이상</td>
				  </tr>
					<tr>
					  <td class="bold">온라인과제</td>
					  <td><input type="text" size="10" name="p_gradreport" value="${view.gradreport}"> 이상</td>
				  </tr>
					<tr>
					  <td class="bold">참여도(학습진도율)</td>
					  <td><input type="text" name="p_wetc1" size="10" maxlength="5" value="${view.wetc1}"> 이상</td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">수료하기 위한 최소값을 입력해 주셔야 합니다.</td>
				  </tr>
					<tr>
					  <td rowspan="5" class="ct bold">가<br />중<br /> 치<br />(%)</td>
					  <td class="bold">출석시험</td>
					  <td><input name="p_wmtest" type="text" size="4" value="${view.wmtest}"></td>
				  </tr>
					<tr>
					  <td class="bold">온라인시험</td>
					  <td><input name="p_wftest" type="text" size="4" value="${view.wftest}"></td>
				  </tr>
					<tr>
					  <td class="bold">온라인과제</td>
					  <td>
					  	<input name="p_wreport" type="text" size="4" value="${view.wreport}"> 
						<input name="p_wact" type="hidden" value="${view.wact}">
					</td>
				  </tr>
					<tr>
					  <td class="bold">참여도(학습진도율)</td>
					  <td><input name="p_wetc2" type="text" size="4" value="${view.wetc2}"> * 가중치 참여도(학습진도율) <!-- :60시간 이상 10 % 이하는 20% 입니다. --> </td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">가중치(%)의 합은 100이어야 합니다.</td>
				  </tr>
					<tr>
					  <td colspan="2" class="bold">1일 최대학습량</td>
					  <td><input name="p_edulimit" type="text" size="3" maxlength="3" value="${view.edulimit}"> %</td>
				  </tr>
				  
				  
					<tr>
					  <td colspan="2" class="bold">과정설문지</td>
					  <td>
                      		<ui:code id="p_sulpapernum" selectItem="${view.sulpapernum}" gubun="sulPaper" codetype=""  upper="" title="과정설문지" className="" type="select" selectTitle="==  ==  == =설문지를 선택하세요 ==  ==  ==" event="" />
                      </td>
				  </tr>
				  <tr>
					  <td colspan="2" class="bold">과정설문기간</td>
					  <td>
                            <input type="text" value="${fn2:getFormatDate(view.aftersulsdate, 'yyyy-MM-dd')}" size="10" name="p_aftersdate" maxlength="10" readonly />
                                    <a href="#none" onclick="popUpCalendar(this, document.all.p_aftersdate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
                                     일 ~ <input type="text" value="${fn2:getFormatDate(view.aftersuledate, 'yyyy-MM-dd')}" size="10" name="p_afteredate" maxlength="10" readonly />
                                    <a href="#none" onclick="popUpCalendar(this, document.all.p_afteredate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>
                                     일
                                     
                        <input type="hidden" name="p_aftersdate1" value="${view.aftersulsdate}">
						<input type="hidden" name="p_afteredate1" value="${view.aftersuledate}"> 
                      </td>
				  </tr>
				  
				  
					<tr>
					  <td colspan="2" class="bold">출석고사장</td>
					  <td>
							<ui:code id="_Array_p_eroomseq" selectItem="${view.neweroom}" gubun="schoolRoom" codetype=""  upper="" title="출석고사장" className="" type="check" selectTitle="" event="" itemRowCount="1"/>
							<br/><br/>
                             <font color="red">* 출석고사장이 지정되지 않으면 기본값으로  전체가 지정됩니다.</font>

                      </td>
				  </tr>					
				</tbody>
			</table>
</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
      		<c:if test="${view.isclosed eq 'Y'}">
      		<li><a href="#none" onclick="alert('수료처리되어 수정할 수 없습니다.');" class="pop_btn01"><span>저장</span></a></li>
			</c:if>
			<c:if test="${view.isclosed ne 'Y'}">
      		<li><a href="#none" onclick="whenSubmit()" class="pop_btn01"><span>저장</span></a></li>
      		</c:if>
			<li><a href="#none" onclick="window.close()" class="pop_btn01"><span>닫기</span></a></li>            
		</ul>
		<!-- // button -->
	</div>
</div>
</form>


<script type="text/javascript">

var frm = document.<c:out value="${gsPopForm}"/>;


//수강료 유무체크
function whenIsCharge() {

	var f = frm;
   	if (f.p_ischarge.value=="C" || f.p_ischarge.value =="S") {
        f.p_biyong.disabled = false;
        f.p_biyong2.disabled = false;
    } else {
        f.p_biyong.disabled = true;
        f.p_biyong2.disabled = true;
        f.p_biyong.value = 0;
        f.p_biyong2.value = 0;
    }
}


//복습가능 여부체크
function whenIsablereview() {
    if (frm.p_isablereview.value=="Y") {
        frm.p_reviewdays.disabled = false;
    } else {
        frm.p_reviewdays.disabled = true;
        frm.p_reviewdays.value = 0;
    }
}


function whenUsebook() {
	//교재여부 체크
	if (frm.p_usebook.value=="Y") {
		frm.p_bookname.disabled = false;
		frm.p_bookprice.disabled = false;
	} else {
		frm.p_bookname.disabled = true;
		frm.p_bookname.value = "";
		frm.p_bookprice.disabled = true;
		frm.p_bookprice.value = 0;
	}
}

//운영자담당 검색
function searchMuser() {
    var f  = frm;
	window.open("","openWinMuser","top=0, left=0, width=580, height=550, scrollbars=yes").focus();
	f.tmp1.value = "p_muser";
	f.action="/com/pop/searchMemberPopup.do";
	f.target="openWinMuser";
	f.submit();
}


//멤버 검색 후 처리
function receiveMember(userid, name, email, compnm, telno, hometel, comptel, position_nm, lvl_nm, tmp1, tmp2, tmp3){
     var f  = frm;
		if (tmp1 == 'p_cuser') {
			f.p_cuseridnm.value = name;
			f.p_cuserid.value   = userid;  
		} else if (tmp1 == 'p_muser') {
			f.p_museridnm.value = name;
			f.p_muserid.value   = userid; 
			f.p_musertel.value   = telno;
		}
	}

//날자 입력폼 삭제
function whenclick(caldate) {
	caldate.value = "";
}

//날자형식변환 
function dateChk(){
	ff = frm;

	if(ff.p_propstart1.value != null){
		ff.p_propstart.value=make_date(ff.p_propstart1.value)+ff.p_propstart2.options[ff.p_propstart2.selectedIndex].value;
	}
	if(ff.p_propend1.value != null){
		ff.p_propend.value=make_date(ff.p_propend1.value)+ff.p_propend2.options[ff.p_propend2.selectedIndex].value;
	}

	if(ff.p_edustart1.value != null){
		ff.p_edustart.value=make_date(ff.p_edustart1.value)+ff.p_edustart2.options[ff.p_edustart2.selectedIndex].value;
	}       
	if(ff.p_eduend1.value != null){
		ff.p_eduend.value=make_date(ff.p_eduend1.value)+ff.p_eduend2.options[ff.p_eduend2.selectedIndex].value;
	}

	return true;
}

//입력값 체크
function weightChk(){
	var sum = 0;
	ff = frm;

	/*
	if (ff.p_propstart1.value=="") {
		alert("수강신청 일을 입력하세요.");
		return false;
	}
	if (ff.p_propend1.value=="") {
		alert("수강신청 종료일을 입력하세요.");
		return false;
	}
	*/
	
	if (ff.p_edustart1.value=="") {
		alert("학습 시작일을 입력하세요.");
		return false;
	}
	if (ff.p_eduend1.value=="") {
		alert("학습 종료일을 입력하세요.");
		return false;
	}

/*
	// 달력으로 입력하지 않고 직접 입력했을 경우 체크
	if( calendarCheck(frm.p_propstart1) ) {
		return;
	}

	if( calendarCheck(frm.p_propend1) ) {
		return;
	}

	if( calendarCheck(frm.p_edustart1) ) {
		return;
	}

	if( calendarCheck(frm.p_eduend1) ) {
		return;
	}
*/
	if (ff.p_propstart.value > ff.p_propend.value) {
		alert("수강신청 시작일은 수강신청 종료일 이전이어야 합니다.");
		return false;
	}
/*
	if (ff.p_propend.value > ff.p_edustart.value) {
		alert("수강신청 종료일은 학습일 이전이어야 합니다.");
		return false;
	}	
*/
	if (ff.p_edustart.value > ff.p_eduend.value) {
		alert("학습시작일은 학습종료일 이전이어야 합니다.");
		return false;
	}

	if (!number_chk_noalert(ff.p_biyong.value)) {
		alert('수강료가 잘못입력되었습니다.');
		return false;
	}
	if (!number_chk_noalert(ff.p_biyong2.value)) {
		alert('교육청일괄납부 수강료가 잘못입력되었습니다.');
		return false;
	}

	if (!number_chk_noalert(ff.p_wetc1.value)) {
		alert('수료기준-진도율이 잘못입력되었습니다.');
		return false;
	}
	if (!number_chk_noalert(ff.p_gradexam.value)) {
		alert('수료기준-중간평가가 잘못입력되었습니다.');
		return false;
	}

	if (!number_chk_noalert(ff.p_gradftest.value)) {
		alert('수료기준-온라인평가가 잘못입력되었습니다.');
		return false;
	}

	if (ff.p_wetc1.value > 100) {
		alert('수료기준-진도율이 100 이상입니다.');
		return false;
	}

	if (!number_chk_noalert(ff.p_gradreport.value)) {
		alert('수료기준-리포트가 잘못입력되었습니다.');
		return false;
	}
	if (ff.p_gradscore.value > 100) {
		alert('수료기준-점수가 100 이상입니다.');
		return false;
	}

	if (ff.p_gradexam.value > 100) {
		alert('수료기준-출석시험가 100 이상입니다.');
		return false;
	}

	if (ff.p_gradftest.value > 100) {
		alert('수료기준-온라인시험가 100 이상입니다.');
		return false;
	}

	if (!number_chk_noalert(ff.p_wmtest.value)) {
		alert('가중치-출석시험(%)가 잘못입력되었습니다.');
		return false;
	}
	if (!number_chk_noalert(ff.p_wftest.value)) {
		alert('가중치-온라인시험(%)가 잘못입력되었습니다.');
		return false;
	}
	if (!number_chk_noalert(ff.p_wetc1.value)) {
		alert('가중치-참여도(학습진도율)(%)이 잘못입력되었습니다.');
		return false;
	}
	if (!number_chk_noalert(ff.p_edulimit.value)) {
		alert('1일최대학습량이 잘못입력되었습니다.');
		return false;
	}
	if (!number_chk_noalert(ff.p_wetc2.value)) {
		alert('가중치-참여도(학습진도율)(%)가 잘못입력되었습니다.');
		return false;
	}
	
	if (ff.p_gradreport.value > 100) {
		alert('수료기준-온라인과제가 100 이상입니다.');
		return false;
	}

	if (!number_chk_noalert(ff.p_wreport.value)) {
		alert('가중치-온라인과제(%)가 잘못입력되었습니다.');
		return false;
	}
	
   
	var weight =  parseFloat(ff.p_wmtest.value,10) 
				+ parseFloat(ff.p_wftest.value,10) 
				+ parseFloat(ff.p_wreport.value,10) 
				+ parseFloat(ff.p_wetc2.value,10)
				//+ parseFloat(ff.p_wetc2.value,10)
				;


	if (weight < 0) {
		alert('가중치가 0% 보다 적습니다');
		return false;
	}       

	if (weight < 100) {
		alert('가중치가 100% 보다 적습니다');
		return false;
	} 

	if (weight > 100) {
		alert('가중치가 100% 보다 많습니다.');
		return false;
	}
	
	/* 2017 추가 */
	if(2017 <= Number('${p_year}')
			&& 1 > $('input[name=areaCode]:checked').length) {
		alert('2017년도 과정부터는 지역을 선택해 주세요.');
		$('input[name=areaCode]:eq(0)').focus();
		return false;
	}
	/* 2017 추가 끝 */

	return true;
}

//조회조건 표시여부
function body_onLoad() {
	if(frm.p_isonoff.value == "ON" || frm.p_isonoff.value == "RC") {

		if (frm.p_isablereview.value=="Y") {
			frm.p_reviewdays.disabled = false;
		} else {
			frm.p_reviewdays.disabled = true;
			frm.p_reviewdays.value = 0;
		}	
	}
	if(frm.p_isonoff.value == "ON") {
		if (frm.p_usebook.value=="Y") {
			frm.p_bookname.disabled = false;
			frm.p_bookprice.disabled = false;
		} else {
			frm.p_bookname.disabled = true;
			frm.p_bookname.value = "";
			frm.p_bookprice.disabled = true;
			frm.p_bookprice.value = 0;
		}	
	}
}

//저장
function whenSubmit(){
	var result;
	result = dateChk();
	result = weightChk();
	if(!result){
		//alert('다시 입력하여 주십시오.');
		return;
	}
	frm.action='/adm/cou/grSeqDetailAction.do';
	frm.p_process.value = 'update';       
	frm.target = "_self";
	frm.submit();
}

//복습 / 수료처리 여부
function reviewSubmit(){
	frm.action='/adm/cou/reviewIsclosedAction.do';
	frm.p_process.value = 'update';       
	frm.target = "_self";
	frm.submit();
}

window:onload = function () {
	body_onLoad();
}

/* 2017 추가 */
$(document).ready(function() {
	$('input[name=areaCode]').click(function() {
		if('A00' == $(this).val()) {
			if($(this).is(':checked')) {
				$('input[name=areaCode]').not(this).attr('disabled', 'disabled');
				$('input[name=areaCode]').not(this).attr('checked', '');
			} else {
				$('input[name=areaCode]').not(this).attr('disabled', '');
			}
		} else {
			if($(this).is(':checked')) {
				$('input[name=areaCode][value=A00]').attr('checked', '');
			}
			/* if($('input[name=areaCode]').not('input[name=areaCode][value=A00]').length == 
				$('input[name=areaCode]').not('input[name=areaCode][value=A00]').filter(':checked').length) {
				$('input[name=areaCode][value=A00]').attr('checked', 'checked');
				$('input[name=areaCode]').not('input[name=areaCode][value=A00]').attr('disabled', 'disabled');
				$('input[name=areaCode]').not('input[name=areaCode][value=A00]').attr('checked', '');
			} */
		}
	});
	$('#AllAreaChk').click(function() {
		if($(this).is(':checked')) {
			$('input[name=areaCode]').not('input[name=areaCode][value=A00]').attr('disabled', '');
			$('input[name=areaCode]').not('input[name=areaCode][value=A00]').attr('checked', 'checked');
			$('input[name=areaCode][value=A00]').attr('checked', '');
		} else {
			$('input[name=areaCode]').not('input[name=areaCode][value=A00]').attr('checked', '');
		}
	});
});
/* 2017 추가 끝 */
</script>

<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>

