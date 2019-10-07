<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

<input type="hidden" name="p_subj" 		id="p_subj"/>
<input type="hidden" name="p_year" 		id="p_year"/>
<input type="hidden" name="p_subjseq" 	id="p_subjseq"/>
<input type = "hidden" name="p_userid" id="p_userid" value = "${sessionScope.userid}" />
<input type="hidden" name="p_subjnm" 	id="p_subjnm"/>

<input type = "hidden" name="p_scoreYn" id="p_scoreYn" value="N" />
<input type="hidden" name="p_upperclass"/>


	<!-- tab -->     
	<div class="conwrap2">
		<ul class="mtab2">
			<li><a href="#"  onclick="changeTab('Study')">학습중인과정</a></li>
<!--			<li><a href="#" onclick="changeTab('Propose')">수강신청한과정</a></li>-->
			<li class="end"><a href="#" onclick="changeTab('Stold')" class="on">학습완료과정(복습)</a></li>                
		</ul>
	</div>
	<!-- //tab -->
       
	<!-- search wrap-->
	<div class="stduyWrap">
		<ul class="floatL">
			<li>수강완료한 과정 내역입니다. </li>
		</ul>
		<div class="txt">
			<label for="p_stoldyear">조회년도</label>
			<select id="p_stoldyear" name="p_stoldyear" onchange="whenSelection();">
				<option value="">ALL</option>                    
				<c:forEach items="${yearList}" var="result">
				<option value="${result.year}" <c:if test="${p_stoldyear eq result.year}">selected</c:if>>${result.year}</option>                    
				</c:forEach>
			</select>
		</div>
	</div>
	<!-- // search wrap -->	
	
	<!-- list table-->
	<div class="studyList">
		<table summary="교육구분, 과정명, 교육기간, 성적정보, 수료여부, 복습하기, 교육후기, 수료증으로 구분" cellspacing="0" width="100%">
			<caption>수강완료 과정</caption>
            <colgroup>
				<col width="7%" />
				<col width="7%" />
				<col width="*" />
				<col width="10%" />
				<col width="10%" />
				<col width="8%" />	
				<col width="8%" />
				<col width="8%" />
				<!-- <col width="8%" />			 -->		
			</colgroup>
			<thead>
				<tr>
					<th scope="col">연수<br>구분</th>
                    <th scope="col">운영<br>형태</th>
					<th scope="row">과정명</th>
					<th scope="row">교육기간</th>
					<th scope="row">성적보기</th>
					<th scope="row">수료여부</th>
					<th scope="row">복습하기</th>
                    <th scope="row">교육후기</th>
					<!-- <th scope="row" class="last">이수증</th>			 -->			
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>
					<c:if test="${result.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
					<c:if test="${result.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
					<c:if test="${result.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
					<c:if test="${result.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
					<c:if test="${result.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
                    </td>
                    <td>
                     <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
                     <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
                     <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
                    </td>			
					<td class="left">
						<%-- <a href="#none" onclick="whenSubjInfoViewPopup('${result.subj}');" title="새창"> --%>
						<c:if test="${result.course ne '000000'}"><span style="color:#3366cc;">[패키지:${result.coursenm}]</span></c:if>${result.subjnm}
						<!-- </a> -->
					</td>
					<td class="num">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
					<td>
						<c:if test="${result.isonoff eq 'ON'}">
							<c:if test="${result.contenttype eq 'L'}">
								${result.score}
							</c:if>
							<c:if test="${result.contenttype ne 'L'}">
								<a href="#none" onclick="fn_statusPopup('${result.subj}', '${result.year}', '${result.subjseq}', '${result.userid}');" title="새창">
								<img src="/images/user/btn_study_view.gif" alt="성적보기" />
								</a>
							</c:if>
						</c:if>
					</td>
					<td class="num">
						<c:set var="isgraduated_value" value="-"/>
						<c:if test="${result.isgraduated ne 'N'}">
							<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.eduend, 'yyyyMMddhh24mm'), result.today) > 0}">
								<c:if test="${result.isgraduated eq 'Y'}"><c:set var="isgraduated_value" value="수료"/></c:if>
								<c:if test="${result.isgraduated eq 'N'}"><c:set var="isgraduated_value" value="미수료"/></c:if>
								<c:if test="${empty result.isgraduated}"><c:set var="isgraduated_value" value="수료처리중"/></c:if>
							</c:if>
						</c:if>
						<c:if test="${result.isgraduated eq 'N'}">
							<!-- s: 미수료 사유 팝업레이어 -->
							<div id="isGraduatedLayer${i.count}">
							<a href="#none"
							   onmouseover="viewInfo('notGraduNmLayer${i.count}',${i.count},'isGraduatedLayer${i.count}',${fn:length(list)},'over','-60px');"
							   onmouseout="viewInfo('notGraduNmLayer${i.count}',${i.count},'isGraduatedLayer${i.count}',${fn:length(list)},'out','-60px');"><c:out value="${isgraduated_value}"/></a>
							
							<div class="e_popLayer" id="notGraduNmLayer${i.count}"> 
								<div class="e_pop_top">&nbsp;</div>
								<div class="e_pop_middle">
									<dl>
										<dt>미수료사유 : </dt>
										<dd>${result.notgradunm}</dd>
									</dl>
								</div>
								<div class="e_pop_bottom"></div>
							</div>
							</div>
							<!-- e: 미수료 사유 팝업레이어 -->
						</c:if>
						<c:if test="${result.isgraduated ne 'N'}">
							<c:out value="${isgraduated_value}"/>
						</c:if>
					</td>
					<td>
						<!-- 쿼리에서 복습기간을 계산하여 오늘날짜가 복습기간안에 있으면 복습가능여부를 'Y'로 가져온다. -->
						<c:if test="${result.subjseqisablereview eq 'Y' and result.isreview eq 'Y'}">
							<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.eduend, 'yyyyMMddhh24mm'), result.today) > 0}">
								<c:if test="${result.isonoff eq 'ON'}">
									<a href="#none" 
										<c:if test="${result.contenttype eq 'L'}">
										onclick="studyOpen('${eduurl}','${ieduurl}')"
										</c:if>
										<c:if test="${result.contenttype ne 'L'}">
										onclick="doReview('${result.contenttype}', '${result.subj}', '${result.year}', '${result.subjseq}','99999','99999')"
										</c:if>
									>
									<img src="/images/user/btn_restudy.gif" width="50" height="18" alt="복습하기" />
									</a>	
								</c:if>
							</c:if>
						</c:if>
						<c:if test="${result.subjseqisablereview ne 'Y' or result.isreview ne 'Y'}">-</c:if>
					
					</td>
					<td>
						<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.eduend, 'yyyyMMddhh24mm'), result.today) > 0}">
							<a href="#none" onclick="whenSubjComments('${result.subj}','${result.year}','${result.subjseq}','${result.subjnm}','${result.userid}');">
								<img src="/images/user/icon_note.gif" alt="교육후기" />
							</a>
						</c:if>
						<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.eduend, 'yyyyMMddhh24mm'), result.today) <= 0}">-</c:if>
					</td>                        
					<%-- <td class="last">
						<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.eduend, 'yyyyMMddhh24mm'), result.today) > 0}">
							<c:if test="${result.isgraduated eq 'Y' and result.isonoff eq 'ON'}">
								<a href="#none" onclick="suRoyJeung('${result.subj}','${result.year}','${result.subjseq}','${userid}');">
								<a href="javascript:suRoyJeung('${result.curYear}', '${result.subj}', '${result.year}', '${result.subjseq}', '${status.count }', '${result.lecselno}', '${result.upperclass }')">								
								<img src="/images/user/icon_print.gif" alt="수료증" />
								</a>
							</c:if>
							<c:if test="${result.isgraduated ne 'Y' or result.isonoff ne 'ON'}">-</c:if>
						</c:if>
						<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.eduend, 'yyyyMMddhh24mm'), result.today) <= 0}">-</c:if>
					</td>		 --%>				
				</tr>					
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
</form>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
<input type="hidden" name="preview"/>
</form>


<script type="text/javascript">
//<![CDATA[
	/* ********************************************************
	 * 조회
	 ******************************************************** */
	function whenSelection() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/cou/courseStoldList.do";
		frm.target = "_self";
		frm.submit();
	}

	function changeTab(type){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/cou/course"+type+"List.do";
		frm.target = "_self";
		frm.submit();
	}


	//수료증출력
	/* function suRoyJeung(subj, year, subjseq, userid){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
		frm.p_userid.value = userid;
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.action = "/adm/fin/suRoyJeungPrint.do";
		frm.target = "suRoyJeungPop";
		frm.submit();
	} */

	//수료증출력
	function suRoyJeung(p_cur_year, p_subj, p_year, p_subjseq, cnt,p_lecselno,p_upperclass){
		var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
		
		thisForm.p_subj.value = p_subj;
		thisForm.p_year.value = p_year;
		thisForm.p_subjseq.value = p_subjseq;
		thisForm.p_upperclass.value = p_upperclass;
		
	    
		if(p_upperclass == 'PRF' && p_year >= '2015') {	
			
			if(p_lecselno == 'null'){
				alert('연수지명번호 미입력되셨습니다.\n연수지명번호 입력시 이수증 출력 가능합니다.');
				return;
			}
			if(p_lecselno == ''){
				alert('연수지명번호 미입력되셨습니다.\n연수지명번호 입력시 이수증 출력 가능합니다.');
				return;
			}
		
		}
		
		/* $('input:checkbox[name="p_check"]').each(function(){
			if(this.checked){
				if(this.value == cnt){
					$("#p_scoreYn").val("Y");
					return false;
				}
			}else{
				$("#p_scoreYn").val("N");
			}
		}); */
		
		
		
//		if(thisForm.p_check.length = "undefined"){
//			if(thisForm.p_check.checked){
//				alert("11");
//				 thisForm.p_scoreYn.value = "Y";
//			}else{
//				alert("22");
//				thisForm.p_scoreYn.value = "N";
//			}
//		}else{
//			if(thisForm.p_check[cnt-1].checked){
//				alert("!!");
//				 thisForm.p_scoreYn.value = "Y";
//			}else{
//				alert("@@");
//				thisForm.p_scoreYn.value = "N";
//			}	
//		}

		window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
		thisForm.action = "/usr/myh/suRoyJeungPrint.do";
		thisForm.target = "suRoyJeungPop";
		thisForm.submit();
	}

	
	//교육후기 작성
	function whenSubjComments(subj, year, subjseq, subjnm, userid){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'whenSubjCommentsPop', 'left=100,top=100,width=800,height=600,scrollbars=yes');
		
		frm.p_userid.value = userid;
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.p_subjnm.value = subjnm;
		frm.action = "/usr/stu/whenSubjComments.do";
		frm.target = "whenSubjCommentsPop";
		frm.submit();
	}

	function doReview(contenttype, subj, year, subjseq, height, width){
		var frm = eval('document.studyForm');
		frm.p_contenttype.value = contenttype;
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.p_height.value = height;
		frm.p_width.value = width;
		//frm.preview.value = "Y";
		
		var width  	= 820;
       	var height 	= 740;
       	var target  = "newStudy";
       	
		var options = "toolbar=0,location=0,directories=0,status=no,menubar=0,scrollbars=yes,resizable=no,top=30,left=30,copyhistory=0,width=" + width + ",height=" + height + "";
        var windowPopUpView=window.open('', target, options);

		frm.action = "/usr/stu/std/userStudyHome.do";
		frm.target = target;
		windowPopUpView.focus();
		frm.submit();
	}
//]]>
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->