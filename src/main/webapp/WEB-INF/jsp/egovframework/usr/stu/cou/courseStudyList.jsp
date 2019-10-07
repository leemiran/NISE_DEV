<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>

<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
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
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_seq"/>
<input type="hidden" id="studyPopup" name="studyPopup"  value="Y" />
<input type="hidden" id="popupClose" name="popupClose"  value="Y" />
<input type="hidden" id="pageIndex" name="pageIndex"/>


	<!-- tab -->     
	<div class="conwrap2">
		<ul class="mtab2">
			<li><a href="#"  onclick="changeTab('Study')" class="on">학습중인과정</a></li>
<!--			<li><a href="#" onclick="changeTab('Propose')">수강신청한과정</a></li>-->
			<li class="end"><a href="#" onclick="changeTab('Stold')">학습완료과정(복습)</a></li>                
		</ul>
	</div>
	<!-- //tab -->
       
	<!-- search wrap-->
	<div class="stduyWrap">
		<ul class="floatL">
			<li>학습중인 과정 내역입니다. </li>
		</ul>
		<div class="txt">
            <label for="p_sel_subj">과정명</label>
			<select id="p_sel_subj" name="p_sel_subj" onchange="whenSelection();">
				<option value="">전체과정</option>                    
				<c:forEach items="${subjList}" var="result">
				<option value="${result.subj}" <c:if test="${p_sel_subj eq result.subj}">selected</c:if>>${result.subjnm}</option>                    
				</c:forEach>
			</select>
		</div>
	</div>
	<!-- // search wrap -->	
	
	<div class="sub_text_1">
		<h4>학습대기 과정</h4>
	</div>
       
	<!-- list table-->
	<div class="studyList">
		<table summary="학습구분, 과정명, 연수기간, 남은학습일, 학습시작으로 구분" cellspacing="0" width="100%">
			<caption>학습대기 과정</caption>
            <colgroup>
				<col width="7%" />
				<col width="7%" />
				<col width="*" />
				<col width="20%" />
				<col width="10%" />					
				<col width="10%" />
				<col width="10%" />					
			</colgroup>
			<thead>
				<tr>
                	<th scope="col">연수<br>구분</th>
                    <th scope="col">운영<br>형태</th>
					<th scope="col">과정명</th>
					<th scope="col">연수기간</th>
					<th scope="col">승인여부</th>
					<th scope="col">배송추적</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${dList}" var="result">
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
							<c:if test="${result.course ne '000000'}"><span style="color:#3366cc;">[ 패키지:${result.coursenm} ]</span><br/></c:if>${result.subjnm}
					</td>
					<td class="num">${fn2:getFormatDate(result.edustart, 'yyyy-MM-dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy-MM-dd')}</td>
					<td class="num">
						${result.chkfinalnm }
					</td>
					<td>
						<c:if test="${result.chkfinalnm  eq '승인'}">
							<c:if test="${result.course eq '000000' }">
								<c:if test="${result.usebook eq 'Y'}">
									<c:if test="${empty result.deliveryStatus}">배송대기중</c:if>							
								</c:if>						
							</c:if>
							
							<c:if test="${result.course ne '000000' }">
								<c:if test="${result.usebook eq 'Y'}">
									<c:if test="${empty result.deliveryStatus}">운영 1.2과정 통권</c:if>							
								</c:if>						
							</c:if>
														
							<c:if test="${not empty result.deliveryStatus}">
										${result.deliveryComp }[<a href="#" onclick="openDeliveryFoward('${result.deliveryNumber}', '${result.deliveryUrl}')"><font color="red">${result.deliveryNumber }</font></a>]
							</c:if>
							
							<c:if test="${result.usebook ne 'Y'}">
									교재사용안함
							</c:if>	
						</c:if>
						
						<c:if test="${result.chkfinalnm  ne '승인'}">
							${result.chkfinalnm }
						</c:if>
						
						
					</td>
				</tr>					
			</c:forEach>
			<c:if test="${empty dList}">
				<tr>
					<td colspan="6" class="num last">
						조회하신 자료가 없습니다.<br/>
					</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	
	
	<br/><br/>
	     
	<div class="sub_text_1">
		<h4>학습진행</h4>
	</div>
       
	<!-- list table-->
	<div class="studyList">
		<table summary="학습구분, 과정명, 연수기간, 남은학습일, 학습시작으로 구분" cellspacing="0" width="100%">
			<caption>학습진행</caption>
            <colgroup>
				<col width="7%" />
				<col width="7%" />
				<col width="*" />
				<col width="20%" />
				<col width="20%" />
				<col width="10%" />
				<col width="10%" />					
			</colgroup>
			<thead>
				<tr>
                	<th scope="col">연수<br>구분</th>
                    <th scope="col">운영<br>형태</th>
					<th scope="col">과정명</th>
					<th scope="col">연수기간</th>
					<th scope="col">배송추적</th>
					<th scope="col">남은학습일</th>
					<th scope="col" class="last">학습시작</th>						
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
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
							<c:if test="${result.course ne '000000'}"><span style="color:#3366cc;">[ 패키지:${result.coursenm} ]</span><br/></c:if>${result.subjnm}
						<!-- </a> -->
					</td>
					<td class="num">${fn2:getFormatDate(result.studystart, 'yyyy-MM-dd')} ~ ${fn2:getFormatDate(result.studyend, 'yyyy-MM-dd')}</td>
					<td class="num">
					
					
					<c:if test="${result.course eq '000000' }">
							<c:if test="${result.usebook eq 'Y'}">
							<c:if test="${empty result.deliveryStatus}">배송대기중</c:if>							
						</c:if>						
					</c:if>
					
					<c:if test="${result.course ne '000000' }">
						<c:if test="${result.usebook eq 'Y'}">
							<c:if test="${empty result.deliveryStatus}">운영 1.2과정 통권</c:if>							
						</c:if>						
					</c:if>
					
					<c:if test="${not empty result.deliveryStatus}">
								${result.deliveryComp }[<a href="#" onclick="openDeliveryFoward('${result.deliveryNumber}', '${result.deliveryUrl}')"><font color="red">${result.deliveryNumber }</font></a>]
							</c:if>
					
					<c:if test="${result.usebook ne 'Y'}">
						교재사용안함
						</c:if>
					
					</td>
					<td class="num">
						<c:if test="${result.remainDays < 0}">수료처리중</c:if>
						<c:if test="${result.remainDays >= 0}">${result.remainDays}일</c:if>
					</td>
					<td class="num last">
						<c:set var="ieduurl" value="${empty result.eduurl ? 0 : 1}"/>
						
					
						<c:if test="${result.isonoff eq 'ON'}">
							<c:if test="${result.contenttype eq 'L'}">
								<a href="#none" onclick="studyOpen('${result.eduurl}','${ieduurl}')" class="stBtn" title="새창"><img src="/images/user/btn_mBlearnStart.gif" alt="학습하기" /></a>
							</c:if>
							<c:if test="${result.contenttype ne 'L'}">								
							<!-- 조건문 -->
								
									<a href="#none" onclick="doStudy('${result.contenttype}', '${result.subj}', '${result.year}', '${result.subjseq}', '${result.height}', '${result.width}', '${result.lcmstype}')" class="stBtn"  title="새창">
										<img src="/images/user/btn_mBlearnStart.gif" alt="학습하기" />
									</a>
							</c:if>
						</c:if>
					</td>						
				</tr>					
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="6" class="num last">
						조회하신 자료가 없습니다.<br/>
						<span style="color:#fc6817;">학습이 종료된 과정의 공지 및 질문방 이용은 상단 [복습/교육후기] 메뉴의 [복습하기] 버튼을 클릭하십시오.</span>
					</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
	
	
	
	
	
	
	<div class="sub_text">
		<h4>학습공지사항</h4>
	</div>
	<!-- list table-->
	<div class="studyList">
	<c:if test="${noticeTotCnt > 10}">
	<div style="height:320px; overflow-y:scroll; overflow-x:hidden;">
	</c:if>
		<table summary="과정명, 제목, 작성일로 구성" cellspacing="0" style="width:738px;">
			<caption>학습공지사항</caption>
            <colgroup>
				<col width="30%" />
				<col width="50%" />
				<col width="20%" />					
			</colgroup>
			<thead>
				<tr>
					<th scope="row">과정명</th>
					<th scope="row">제목</th>
					<th scope="row" class="last">작성일</th>						
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${noticeList}" var="result">
				<tr>
					<td class="left">${result.subjnm}</td>						
					<td class="left">
						<a href="#none" onclick="goSubjGongInfo('${result.seq}','${result.subj}','${result.year}','${result.subjseq}')" title="새창">${result.title}</a>
						<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.addate, 'yyyyMMddhh24'), PCurrentDate) < 0}">
							<img src="/images/adm/ico/ico_new.gif" alt="NEW" />
						</c:if>
					</td>
					<td class="num last">${fn2:getFormatDate(result.addate, 'yyyy-MM-dd')}</td>						
				</tr>					
			</c:forEach>
			<c:if test="${empty noticeList}">
				<tr>
					<td colspan="3" class="num last">조회하신 자료가 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
		<c:if test="${noticeTotCnt > 10}">
		</div>
		</c:if>
	</div>
	<!-- list table-->
   <!-- 페이징 시작 -->
<!--		<div class="paging">-->
<!--			<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>-->
<!--		</div>-->
		<!-- 페이징 끝 -->
  
</form>


<script type="text/javascript">
//<![CDATA[
	/* ********************************************************
	 * 조회
	 ******************************************************** */
	function whenSelection() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.pageIndex.value = '1';
		frm.action = "/usr/stu/cou/courseStudyList.do";
		frm.target = "_self";
		frm.submit();
	}

	function doLinkPage(index) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.pageIndex.value = index;
		frm.action = "/usr/stu/cou/courseStudyList.do";
		frm.target = "_self";
		frm.submit();
	}


	function changeTab(type){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.pageIndex.value = '1';
		frm.action = "/usr/stu/cou/course"+type+"List.do";
		frm.target = "_self";
		frm.submit();
	}

	function goSubjGongInfo(seq, subj, year, subjseq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		var target  = "noticePopup";
       	
		var options = "toolbar=0,location=0,directories=0,status=no,menubar=0,scrollbars=auto,resizable=no,top=30,left=30,copyhistory=0,width=790,height=600";
        window.open('', target, options);
		
		frm.action = "/usr/stu/std/userStudyNoticeView.do";
		frm.p_seq.value = seq;
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.target = target;
		frm.submit();
	}

	function doStudy(contenttype, subj, year, subjseq, height, width, lcmstype){
		var frm = eval('document.studyForm');
		frm.p_contenttype.value = contenttype;
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.p_height.value = height;
		frm.p_width.value = width;
		frm.p_lcmstype.value = lcmstype;

		var width  	= 820;
       	var height 	= 740;
       	var target  = "newStudy";
       	
		var options = "toolbar=0,location=0,directories=0,status=no,menubar=0,scrollbars=yes,resizable=no,top=30,left=30,copyhistory=0,width=" + width + ",height=" + height + "";
        window.open('', target, options);

		frm.action = "/usr/stu/std/userStudyHome.do";
		frm.target = target;
		frm.submit();
	}

	function openDeliveryFoward(number, url){
		window.open(url+number, "delivery", "width=600, height=480, scrollbars=auto");
	}
//]]>	
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->