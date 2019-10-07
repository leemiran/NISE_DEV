<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp"%>
<!--login check-->
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp"%>

	<form name="studyForm" id="studyForm" method="post" action="">
		<input type="hidden" name="p_contenttype" /> <input type="hidden" name="p_subj" /> 
		<input type="hidden" name="p_year" /> 
		<input type="hidden" name="p_subjseq" /> 
		<input type="hidden" name="p_studytype" /> 
		<input type="hidden" name="p_process" /> 
		<input type="hidden" name="p_next_process" /> 
		<input type="hidden" name="p_height" /> 
		<input type="hidden" name="p_width" /> 
		<input type="hidden" name="p_lcmstype" />
		<input type="hidden" name="p_review_study_yn"  value="N"/>
		
	</form>
	<form name="kniseForm" id="kniseForm" method="post" action="">

		<input type="hidden" name="menu_main" value="3" /> <input type="hidden" name="menu_sub" value="1" /> <input type="hidden" name="menu_tab_title" value="나의강의실" /> <input type="hidden" name="menu_sub_title" value="나의 학습과정" />
		<!-- hidden param end-->
		<input type="hidden" name="p_subj" /> <input type="hidden" name="p_year" /> <input type="hidden" name="p_subjseq" /> <input type="hidden" name="p_seq" /> <input type="hidden" id="studyPopup" name="studyPopup" value="Y" /> <input type="hidden" id="popupClose" name="popupClose" value="Y" /> <input type="hidden" id="pageIndex" name="pageIndex" />
		
		<div id="content04">			
			
			<!-- tab -->
			<div class="conwrap4" style="float: right; width: 755px;">
				<ul class="mtab3">
					<c:forEach var="noticeSubj" items="${noticeSubjList}" varStatus="status" end="4">
						<li class="end"><a href="#" onclick="changeTab('${status.index}');return false;" <c:if test="${status.first}">class="on"</c:if> style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; width: 150px;">${noticeSubj}</a></li>
					</c:forEach>
					<!-- <li class="end"><a href="#" onclick="changeTab('1');return false;" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; width: 88px;">테스트24124151352365234652</a></li>
					<li class="end"><a href="#" onclick="changeTab('2');return false;" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; width: 88px;">테스트24124151352365234652</a></li>
					<li class="end"><a href="#" onclick="changeTab('3');return false;" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; width: 88px;">테스트24124151352365234652</a></li>
					<li class="end"><a href="#" onclick="changeTab('4');return false;" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; width: 88px;">테스트24124151352365234652</a></li> -->
				</ul>
			</div>
			<!-- //tab -->

			<!-- search wrap-->
			<c:forEach var="noticeSubj" items="${noticeSubjList}" varStatus="status" end="4">
				<div class="stduyWrap2" style="width: 755px; float: right;">
					<!-- 공지사항 -->
					<div class="stduytit" style="float:left; width:740px;">
						<img src="/images/user/ystitle.jpg" alt="연수 제목"> ${noticeSubj}
					</div>
					
					<div class="notice2 m1" style="float: left; width: 740px;">
						<ul>
							<li class="m1">
								<ul style="float: left; width: 640px;">
									<c:set var="noticeSubjListIdx">${status.index}</c:set>
									<c:choose>
										<c:when test="${not empty noticeMap[noticeSubjListIdx]}">
											<c:forEach var="notice" items="${noticeMap[noticeSubjListIdx]}">
												<li>
													<a href="#none" onclick="goSubjGongInfo('${notice.seq}','${notice.subj}','${notice.year}','${notice.subjseq}')" title="새창">
														<img src="/images/adm/ico/ico_notice.gif" alt="공지">
														<c:if test="${fn2:getMinDifference(fn2:getFormatDate(notice.addate, 'yyyyMMddhh24'), PCurrentDate) < 0}">
															<img src="/images/adm/ico/ico_new.gif" alt="NEW" />
														</c:if>
														${notice.title}</a>
													<span class="date">${fn2:getFormatDate(notice.addate, 'yyyy-MM-dd')}</span>
												</li>
											</c:forEach>
										</c:when>
										<c:otherwise><li>공지사항이 없습니다.</li></c:otherwise>
									</c:choose>
								</ul>
							</li>
						</ul>
					</div>
					<!-- //공지사항 -->
				</div>
				<!-- // search wrap -->
			</c:forEach>
		</div>
		
		
				
		<div class="sub_text_1">
			<h4>진행 중인 과정</h4>
		</div>
		
		

		<!-- list table-->
		<div class="studyList">
			<table summary="신청일,연수구분,연수명,학점,연수기간,연수지명번호,학습하기로 구분" cellspacing="0" width="100%">
				<caption>진행 중인 과정</caption>
				<colgroup>
					<col width="10%" />
					<col width="7%" />
					<col width="*" />
					<col width="7%" />
					<col width="20%" />
					<col width="10%" />
					<col width="10%" />
					<!-- <col width="10%" /> -->
				</colgroup>
				<thead>
					<tr>
						<th scope="col">신청일</th>
						<th scope="col">연수<br>구분</th>
						<th scope="col">연수명</th>
						<th scope="col">학점</th>
						<th scope="col">연수기간</th>
						<th scope="col">연수<br>지명번호</th>
						<th scope="col" class="last">학습하기</th>
						<!-- <th scope="col" class="last">배송추적</th> -->
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${!empty ingList}">
							<c:forEach var="ing" items="${ingList}">
								<tr>
				                    <td>${ing.appdate}</td>
									<td>
										<c:if test="${ing.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
										<c:if test="${ing.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
										<c:if test="${ing.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
										<c:if test="${ing.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
										<c:if test="${ing.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
				                    </td>
				                               					
									<td class="left">
										<%-- <a href="#none" onclick="whenSubjInfoViewPopup('${ing.subj}');" title="새창"> --%>
											<c:if test="${ing.course ne '000000'}"><span style="color:#3366cc;">[ 패키지:${ing.coursenm} ]</span><br/></c:if>${ing.subjnm}
										<!-- </a> -->
									</td>
									<td class="num">${ing.point}</td>
									<td class="num">${fn2:getFormatDate(ing.studystart, 'yyyy-MM-dd')} ~ ${fn2:getFormatDate(ing.studyend, 'yyyy-MM-dd')}</td>
									<td><c:if test="${not empty ing.lecSelNo && ing.lecSelNo ne 'null'}">${ing.lecSelNo}</c:if></td>
									<td class="num last">
										<c:set var="ieduurl" value="${empty ing.eduurl ? 0 : 1}"/>
									
										<c:if test="${ing.isonoff eq 'ON'}">
											<c:if test="${ing.contenttype eq 'L'}">
												<a href="#none" onclick="studyOpen('${ing.eduurl}','${ieduurl}')" class="stBtn" title="새창"><img src="/images/user/btn_mBlearnStart.gif" alt="학습하기" /></a>
											</c:if>
											<c:if test="${ing.contenttype ne 'L'}">								
												<a href="#none" onclick="doStudy('${ing.contenttype}', '${ing.subj}', '${ing.year}', '${ing.subjseq}', '${ing.height}', '${ing.width}', '${ing.lcmstype}')" class="stBtn" title="새창">
													<img src="/images/user/btn_mBlearnStart.gif" alt="학습하기" />
												</a>
											</c:if>
										</c:if>
									</td>
									<%-- <td class="num last">
										<c:if test="${ing.course eq '000000' }">
											<c:if test="${ing.usebook eq 'Y'}">
												<c:if test="${empty ing.deliveryStatus}">배송대기중</c:if>							
											</c:if>						
										</c:if>
										<c:if test="${ing.course ne '000000' }">
											<c:if test="${ing.usebook eq 'Y'}">
												<c:if test="${empty ing.deliveryStatus}">운영 1.2과정 통권</c:if>							
											</c:if>						
										</c:if>
										<c:if test="${not empty ing.deliveryStatus}">
											${ing.deliveryComp }[<a href="#" onclick="openDeliveryFoward('${ing.deliveryNumber}', '${ing.deliveryUrl}')"><font color="red">${ing.deliveryNumber }</font></a>]
										</c:if>
										<c:if test="${ing.usebook ne 'Y'}">
											교재사용안함
										</c:if>
									</td> --%>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr><td colspan="7" class="num last">진행 중인 연수가 없습니다.</td></tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>


		<br />
		<br />

		<div class="sub_text_1">
			<h4>신청/대기 중인 연수</h4>
		</div>

		<!-- list table-->
		<div class="studyList">
			<table summary="신청일,연수,연수명,학점,연수기간,연수지명번호,수강신청으로 구분" cellspacing="0" width="100%">
				<caption>신청/대기 중인 연수</caption>
				<colgroup>
					<col width="10%" />
					<col width="7%" />
					<col width="*" />
					<col width="7%" />
					<col width="20%" />
					<col width="10%" />
					<col width="10%" />
					<col width="10%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="col">신청일</th>
						<th scope="col">연수<br>구분</th>
						<th scope="col">연수명</th>
						<th scope="col">학점</th>
						<th scope="col">연수기간</th>
						<th scope="col">연수<br>지명번호</th>
						<th scope="col">승인여부</th>
						<th scope="col" class="last">수강신청</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${!empty waitList}">
							<c:set var="rownumCnt" value="0"/>
							<c:forEach var="wait" items="${waitList}">
								<tr>
									<c:if test="${rownumCnt == 0}">
										<td<c:if test="${wait.coursecount > 1 && rownumCnt == 0}"> rowspan="${wait.coursecount}"</c:if>>${wait.appdate}</td>
										<td<c:if test="${wait.coursecount > 1 && rownumCnt == 0}"> rowspan="${wait.coursecount}"</c:if>>
											<c:if test="${wait.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
											<c:if test="${wait.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
											<c:if test="${wait.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
											<c:if test="${wait.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
											<c:if test="${wait.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
										</td>
									</c:if>
									<td class="left">
										<c:if test="${wait.course ne '000000'}">
											[패키지]  ${wait.subjnm} ${fn2:toNumber(wait.subjseq)}기
										</c:if>
										<c:if test="${wait.course eq '000000'}">
											${wait.subjnm}
										</c:if>
									</td>
									<td class="num">${wait.point}</td>
									<td class="num">${wait.edustart} ~ ${wait.eduend}</td>
									<td><c:if test="${not empty wait.lecSelNo && wait.lecSelNo ne 'null'}">${wait.lecSelNo}</c:if></td>
									<c:if test="${rownumCnt == 0}">
										<td class="num"<c:if test="${wait.coursecount > 1 && rownumCnt == 0}"> rowspan="${wait.coursecount}"</c:if>>
											${wait.chkfinalNm}
										</td>
									</c:if>
									<c:if test="${rownumCnt == 0}">
										<td class="num last"<c:if test="${wait.coursecount > 1 && rownumCnt == 0}"> rowspan="${wait.coursecount}"</c:if>>
											<a href="#none" onclick="whenSubjCancel('${wait.subj}','${wait.year}','${wait.subjseq}');">
												<img src="/images/user/subj_study_cancel.gif" alt="신청취소"/>
											</a>
										</td>
									</c:if>
								</tr>
								<c:if test="${wait.coursecount > 1}">
									<c:choose>
										<c:when test="${wait.coursecount == rownumCnt + 1}">
											<c:set var="rownumCnt" value="0"/>
										</c:when>
										<c:otherwise>
											<c:set var="rownumCnt" value="${rownumCnt + 1}"/>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr><td colspan="8" class="num last">신청/대기 중인 연수가 없습니다.</td></tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
		<!-- list table-->

		<div class="sub_text">
			<h4>완료/복습 중인 연수</h4>
		</div>
		<!-- list table-->
		<div class="studyList">

			<table summary="연수구분,연수명,학점,연수지명번호,복습기간,성적/이수,복습하기 내역로 구분" cellspacing="0" width="100%">
				<caption>완료/복습 중인 연수</caption>
				<colgroup>
					<col width="10%" />
					<col width="*" />
					<col width="7%" />
					<col width="10%" />
					<col width="20%" />
					<col width="10%" />
					<col width="10%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="col">연수<br>구분</th>
						<th scope="col">연수명</th>
						<th scope="col">학점</th>
						<th scope="col">연수<br>지명번호</th>
						<th scope="col">복습기간</th>
						<th scope="col">성적/이수</th>
						<th scope="col" class="last">복습하기</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${!empty reviewList}">
							<c:forEach var="review" items="${reviewList}">
								<tr>
									<td>
										<c:if test="${review.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
										<c:if test="${review.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
										<c:if test="${review.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
										<c:if test="${review.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
										<c:if test="${review.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
									</td>
									<td class="left">
										<%-- <a href="#none" onclick="whenSubjInfoViewPopup('${review.subj}');" title="새창"> --%>
										<c:if test="${review.course ne '000000'}"><span style="color:#3366cc;">[패키지:${review.coursenm}]</span></c:if>${review.subjnm}
										<!-- </a> -->
									</td>
									<td class="num">${review.point}</td>
									<td class="num"><c:if test="${not empty review.lecSelNo && review.lecSelNo ne 'null'}">${review.lecSelNo}</c:if></td>
									<td class="num">${fn2:getFormatDate(review.studystart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(review.studyend, 'yyyy.MM.dd')}</td>
									<td class="num">
										<a href="#none" onclick="fn_statusPopup('${review.subj}', '${review.year}', '${review.subjseq}', '${review.userid}');" title="새창">
											<c:if test="${empty review.score}">0</c:if>
											<c:if test="${not empty review.score}">${review.score}</c:if>
										</a> / <br/>
										<c:choose>
			                        		<c:when test="${empty review.isgraduated && (review.edustart > fn2:getFormatDateNow('yyyyMMddHH'))}">
			                        			수강전
			                        		</c:when>
				                        	<c:when test="${empty review.isgraduated && (review.eduend >= fn2:getFormatDateNow('yyyyMMddHH'))}">
				                        		수강중
				                        	</c:when>
			                        		<c:when test="${empty review.isgraduated && (review.eduend < fn2:getFormatDateNow('yyyyMMddHH'))}">
			                        			수료처리중
			                        		</c:when>
			                        		<c:otherwise>
			                        			${review.isgraduatedValue}
			                        		</c:otherwise>
				                        </c:choose>
									</td>
									<td class="last"><!-- 쿼리에서 복습기간을 계산하여 오늘날짜가 복습기간안에 있으면 복습가능여부를 'Y'로 가져온다. -->
										<c:if test="${review.subjseqisablereview eq 'Y' and review.isreview eq 'Y'}">											
											<c:if test="${review.reviewStudyYn eq 'Y'}">
												<c:if test="${review.isonoff eq 'ON'}">
													<a href="#none" 
														<c:if test="${review.contenttype eq 'L'}">
														onclick="studyOpen('${eduurl}','${ieduurl}')"
														</c:if>
														<c:if test="${review.contenttype ne 'L'}">
														onclick="doReview('${review.contenttype}', '${review.subj}', '${review.year}', '${review.subjseq}','99999','99999')"
														</c:if>
														 title="새창"
													>
													<img src="/images/user/btn_restudy.gif" width="50" height="18" alt="복습하기" />
													</a>	
												</c:if>
											</c:if>
										</c:if>
										<c:if test="${review.subjseqisablereview ne 'Y' or review.isreview ne 'Y'}">-</c:if>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr><td colspan="7" class="num last">완료/복습 중인 연수가 없습니다.</td></tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</form>
	<!-- button -->

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


	/* function changeTab(type){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.pageIndex.value = '1';
		frm.action = "/usr/stu/cou/course"+type+"List.do";
		frm.target = "_self";
		frm.submit();
	} */

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
		frm.p_review_study_yn.value = "N";

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
	function doReview(contenttype, subj, year, subjseq, height, width){
		var frm = eval('document.studyForm');
		frm.p_contenttype.value = contenttype;
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.p_height.value = height;
		frm.p_width.value = width;
		//frm.preview.value = "Y";
		frm.p_review_study_yn.value = "Y";
		
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
	
	function changeTab(idx) {
		$('.mtab3 li').eq(idx).find('a').addClass('on');
		$('.mtab3 li').eq(idx).siblings().find('a').removeClass('on');
		//stduyWrap2
		$('.stduyWrap2').eq(idx).show();
		$('.stduyWrap2').not(':eq(' + idx + ')').hide();
	}
	
	function whenSubjCancel(p_subj, p_year, p_subjseq) {
		if (confirm("정말로 수강을 취소하시겠습니까?")) {
			var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
			
			thisForm.p_subj.value = p_subj;
			thisForm.p_year.value = p_year;
			thisForm.p_subjseq.value = p_subjseq;
			thisForm.method = 'post';
			thisForm.action = "/usr/subj/subjProposeCancelAction.do?fromPage=studyInfo";
			thisForm.submit();
		}
	}
	
 	 changeTab('0');
	   
//]]>	
</script>

<!-- button -->
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp"%>
<!-- // button -->