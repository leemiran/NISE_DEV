<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/userLoginCheck.jsp" %>
<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>	

<% 

	String reqHost = request.getServerName().toLowerCase();
	String url = "http://iedu.nise.go.kr";
	
	//String serverPort = request.getLocalPort() + "";
	
	//국립특수교육원이 아니면 테스트 서버로 결제한다.
	if(reqHost.indexOf("iedu.nise.go.kr") == -1)
	{
		url = "http://localhost";
	}


	//동영상 경로
	url += "/condata/contents/";




%>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<input type="hidden" name="p_seq" id="p_seq"/>
	<input type="hidden" name="p_subj" id="p_subj"/>
	<input type="hidden" name="p_year" id="p_year"/>
	<input type="hidden" name="p_subjseq" id="p_subjseq"/>
	<input type="hidden" name="studyPopup" value="Y"/>

	<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>나의학습방</a></span>
	</div>

	<!-- 본문 -->
	<div id="wcontainer">
		<!-- 교육과정 상세 -->
		<div class="yun mrt15">
			<h3 class="mrb10">${EduScore.subjnm} - ${fn2:toNumber(EduScore.subjseq)}기</h3>
			<div class="yunimg2"><img src="/images/mbl/temp/sample_big.jpg" width="60" height="70" alt="" /></div>
			<div class="listTable bview">
				<table summary="수강기간,전체진도율,차시진도로 구분" cellspacing="0" width="100%">
					<caption>연수상세</caption>
					<colgroup>
							<col width="33%" />
							<col width="" />
					</colgroup>
					<tr>
						<th scope="row">수강기간</th>
						<td>${fn2:getFormatDate(EduScore.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(EduScore.eduend, 'yyyy.MM.dd')}</td>
					</tr>
					<tr>
						<th scope="row">전체진도율</th>
						<td>
							<p class="graphtext">${progress}%</p> 
							<div class="graph2"><div class="graph_in" style="width:${progress}%"></div></div>
						</td>
					</tr>
					<tr>
						<th scope="row">차시진도(학습차시/총차시)</th>
						<td>${edudatecnt} / ${datecnt}</td>
					</tr>
				</table>
			</div>
		</div>
		<!-- //교육과정 상세 -->
		
		<!-- 탭 -->
		<ul class="tab mrt20" id="tableMenu1" style="display:block;">
			<li><a href="#none" onclick="displayTabMenu(1)" class="on">학습목록</a></li>
			<li><a href="#none" onclick="displayTabMenu(2)">과정상세정보</a></li>
			<li><a href="#none" onclick="displayTabMenu(3)">공지사항</a></li>
		</ul>
		
		<ul class="tab mrt20" id="tableMenu2" style="display:none;">
			<li><a href="#none" onclick="displayTabMenu(1)">학습목록</a></li>
			<li><a href="#none" onclick="displayTabMenu(2)" class="on">과정상세정보</a></li>
			<li><a href="#none" onclick="displayTabMenu(3)">공지사항</a></li>
		</ul>
		
		<ul class="tab mrt20" id="tableMenu3" style="display:none;">
			<li><a href="#none" onclick="displayTabMenu(1)">학습목록</a></li>
			<li><a href="#none" onclick="displayTabMenu(2)">과정상세정보</a></li>
			<li><a href="#none" onclick="displayTabMenu(3)" class="on">공지사항</a></li>
		</ul>
		
		

		<!-- 리스트 -->
		<ul class="imgList mrt5" id="tableDisPlayMenu1" style="display:block;">
		<c:forEach items="${itemList}" var="result">
		

<!--			모바일지원-->
			<c:if test="${view.mobile eq 'Y'}">
				<c:set var="v_eduTime" value="${result.eduTime}"/>
				<c:set var="v_totalTime" value="${result.totalTime}"/>
				
				
				<!--			미디어 url 셋팅 : 만약 파일 경로가 절대경로라면 파일경로 자체만을 넣어준다.-->
				<c:set var="mediaUrl"><%=url %>${result.mobileUrl}</c:set>
				
				<c:if test="${fn:indexOf(result.mobileUrl, 'http://') > -1}">
					<c:set var="mediaUrl">${result.mobileUrl}</c:set>
				</c:if>
			</c:if>
			
<!--			순수모바일과정-->
			<c:if test="${view.mobile ne 'Y'}">
				<c:set var="v_eduTime" value="${fn2:ceil(result.eduTime * 60)}"/>
				<c:set var="v_totalTime" value="${fn2:ceil(result.totalTime)}"/>
			
			
				<!--			미디어 url 셋팅 : 만약 파일 경로가 절대경로라면 파일경로 자체만을 넣어준다.-->
				<c:set var="mediaUrl"><%=url %>${result.starting}</c:set>
				
				<c:if test="${fn:indexOf(result.starting, 'http://') > -1}">
					<c:set var="mediaUrl">${result.starting}</c:set>
				</c:if>
			</c:if>
			
			
								
<!--					CONTENT_ID : 과정코드||년도||기수코드||모듈||레슨 -- 정보 보내기 url -->
					<c:set var="localUrl">${fn:replace(gsDomain, 'http','UM2M')}</c:set>
					<c:url var="selectMobileResponseUrl" value="${localUrl}/mbl/std/studyItemView.do">
		              <c:param name="file">${mediaUrl}</c:param>
		              <c:param name="CONTENT_ID" value="${p_subj}||${p_year}||${p_subjseq}||${result.module}||${result.lesson}" />
		              <c:param name="TYPE" value="play" />		              
		            </c:url>
		            
		            <c:url var="selectMobileResponseUrlDown" value="${localUrl}/mbl/std/studyItemView.do">
		              <c:param name="file">${mediaUrl}</c:param>
		              <c:param name="CONTENT_ID" value="${p_subj}||${p_year}||${p_subjseq}||${result.module}||${result.lesson}" />
		              <c:param name="TYPE" value="down" />		              
		            </c:url>
		            
		            
<!--		            정보받아오기 url : 테스트용 관리자만 해당됨 ..-->
		            <c:url var="selectMobileRequestUrl" value="${gsDomain}/mbl/std/studyItemProgress.do">
		              <c:param name="ID">${sessionScope.userid}</c:param>
		              <c:param name="NAME">${sessionScope.name}</c:param>
		              <c:param name="COURSE_NO">${p_subj}</c:param>
		              <c:param name="COURSE_NAME">과정명</c:param>
		              <c:param name="CONTINUE_TIME">10</c:param>
		              <c:param name="PLAY_START_TIME">10</c:param>
		              <c:param name="PLAY_END_TIME">300</c:param>
		              <c:param name="PLAYING_TIME">3000</c:param>
		              <c:param name="SCHEDULE_NO" value="${p_subj}||${p_year}||${p_subjseq}||${result.module}||${result.lesson}" />
		              <c:param name="CONTENT_ID" value="${p_subj}||${p_year}||${p_subjseq}||${result.module}||${result.lesson}" />
		              <c:param name="CHAPTER_ID" value="0004" />
		              <c:param name="p_end" value="N" />
		              <c:param name="book_mark">10|135|172</c:param>
		            </c:url>
		            
	
	
	
	
			<li>
				<dl class="blist">
					<dt>
						${result.module}. ${result.lessonName}
						
						<c:if test="${result.lessonstatus ne 'Y' && result.lessonstatus ne 'N'}"><font color="green"><span class="tit">[학습전]</span></font></c:if>
						<c:if test="${result.lessonstatus eq 'Y'}"><font color="red"><span class="tit">[학습완료]</span></font></c:if>
						<c:if test="${result.lessonstatus eq 'N'}"><font color="blue"><span class="tit">[학습중]</span></font></c:if>
						
					</dt>
					<dd><span class="tit">동영상시간</span> 
					<c:if test="${empty v_eduTime || v_eduTime == 0}">
						00분00초
					</c:if>
					<c:if test="${not empty v_eduTime && v_eduTime > 0}">
						<c:out value="${fn2:toStudyTimeConvert(v_eduTime)}" />
					</c:if>
					
					
					</dd>
					<dd><span class="tit">학습시간</span> 
					<c:if test="${empty v_totalTime || v_totalTime == 0}">
						00분00초
					</c:if>
					<c:if test="${result.rsrcSeq != 0  and not empty v_totalTime }">
					<c:out value="${fn2:toStudyTimeConvert(v_totalTime)}" />
					</c:if>
					</dd>
		            

					<dd class="btn">
					
						<a href="${selectMobileResponseUrl}" class="btn_red">
						<span>
						학습시작
						</span>
						</a>
						
<!--						관리자만 보이기-->
					<c:if test="${sessionScope.userid eq 'admin'}">
						<br/>
						<a href="${selectMobileRequestUrl}" class="btn_red">
						<span>진도저장</span>
						</a>
					</c:if>
					
					<a href="${selectMobileResponseUrlDown}" class="btn_red">
					<span>다운로드.</span>
					</a>
					
					</dd>
				</dl>
			</li>
		</c:forEach>
		</ul>


		<!-- 연수정보 -->
		<div class="info mrt10" id="tableDisPlayMenu2" style="display:none;">
		  <table summary="교육목표, 교육대상,교육내용으로 구성" cellspacing="0" width="100%">
		    <caption>
		      연수정보
	        </caption>
		    <colgroup>
		      <col width="20%" />
		      <col width="30%" />
		      <col width="20%" />
		      <col width="" />
	        </colgroup>
		    <tr>
		      <th scope="row">교육목표</th>
		      <td colspan="3"><c:out value="${fn:replace(view.intro, lf, '<br/>')}" escapeXml="false"/></td>
	        </tr>
		    <tr>
		      <th scope="row">교육대상</th>
		      <td colspan="3"><c:out value="${fn:replace(view.edumans, lf, '<br/>')}" escapeXml="false"/></td>
	        </tr>
	        <tr>
		      <th scope="row">교육내용</th>
		      <td colspan="3"><c:out value="${fn:replace(view.explain, lf, '<br/>')}" escapeXml="false"/></td>
	        </tr>
	        <!-- 
		    <tr>
		      <th scope="col">평가기준</th>
		      <td colspan="3">
		      
							<table summary="">
                                    <caption>평가기준</caption>
                                    <colgroup>
                                        <col width="40%"/>
                                        <col width="20%"/>
                                        <col width="40%"/>                                        
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>구분</th>
                                            <th>가중치</th>
                                            <th class="last">수료기준</th>                                           
                                        </tr>
                                    </thead>
                                    <tbody>
                                    
                                    <c:if test="${view.wmtest > 0}">
                                        <tr>
                                            <td>중간평가</td>
                                            <td class="num">${view.wmtest}%</td>
                                            <td class="last">${view.wmtest > 0 ? '반드시 제출' : '-'}</td>                                            
                                        </tr>
                                    </c:if>    
                                    <c:if test="${view.wftest > 0}">
                                        <tr>
                                            <td>최종평가</td>
                                            <td class="num">${view.wftest}%</td>
                                            <td class="last">${view.wftest > 0 ? '반드시 제출' : '-'}</td>                                         
                                        </tr>
									</c:if>
									<c:if test="${view.wreport > 0}">
                                        <tr>
                                            <td>리포트</td>
                                            <td class="num">${view.wreport}%</td>
                                            <td class="last">${view.wreport > 0 ? '반드시 제출' : '-'}</td>                                         
                                        </tr>
									</c:if>
									<c:if test="${view.wetc1 > 0}">
                                        <tr>
                                            <td>참여도</td>
                                            <td class="num">${view.wetc1}%</td>
                                            <td class="last">-</td>                                         
                                        </tr>
									</c:if>

                                        <tr>
                                            <td>총점</td>
                                            <td class="num">-</td>
                                            <td class="last">${view.gradscore}점 이상</td>                                            
                                        </tr>
                                        
                                    <c:if test="${view.wstep > 0}">
                                        <tr>
                                            <td>진도율</td>
                                            <td class="num">필수</td>
                                            <td class="last">${view.wetc1}% 이상</td>                                         
                                        </tr>
									</c:if>
									
									
															
                                    </tbody>
                                </table>
		      </td>
	        </tr>
	         -->
	      </table>
    </div>
    
    <div class="mrt10" id="tableDisPlayMenu3" style="display:none;">
    	<!-- 목록 -->
		<ul class="imgList mrt5">
		
		<c:forEach items="${noticeList}" var="result" varStatus="i">
			<li onclick="#">
				<dl class="blist">
					<dt>
					<a href="#none" onclick="getSubjNoticeView('${result.seq}','${result.subj}','${result.year}','${result.subjseq}')" title="새창">
						<c:if test="${result.types eq 'A'}">[공지]</c:if>
						<c:if test="${result.types eq 'B'}">[이벤트]</c:if>
						<c:if test="${result.types eq 'C'}">[축하]</c:if>
						<c:if test="${result.types eq 'D'}">[안내]</c:if>
						<c:if test="${result.types eq 'E'}">[설문]</c:if>
						<c:if test="${result.types eq 'F'}">[긴급]</c:if>
						<c:if test="${result.types eq 'G'}">[기타]</c:if>
						${result.title}
					</a>
					</dt>
					<dd><span class="tit">작성자</span> ${result.name}</dd>
					<dd><span class="tit">작성일</span> ${fn2:getFormatDate(result.addate, 'yyyy.MM.dd')}</dd>
				</dl>
			</li>
		</c:forEach>
		</ul>
		
        <input id="firstIndex" name="firstIndex" type="hidden" value="<c:out value='${firstIndex}'/>"/>
        <input id="recordCountPerPage" name="recordCountPerPage" type="hidden" value="<c:out value='${recordCountPerPage+15}'/>"/>
		
    </div>
    
    
    
    
		<!-- 더보기 -->
		<div class="more"></div>
		<!-- 맨위로 -->
		<div class="totop"><a href="#wrap">맨위로</a></div>
		
	</div>
	<!-- //본문 -->




</form>




<!--아래는 팝업과 뷰를 공통으로  스크립트를 프로그램한다.-->
<script type="text/javascript">
<!--
/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList() {
	thisForm.action = "/mbl/std/studyItemList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//tab menu
function displayTabMenu(num)
{
	for(var i=1; i<=3; i++)
	{
		if(num == i)
		{
			document.all("tableMenu" + i).style.display = "block";
			document.all("tableDisPlayMenu" + i).style.display = "block";
		}
		else
		{
			document.all("tableMenu" + i).style.display = "none";
			document.all("tableDisPlayMenu" + i).style.display = "none";
		}
	}	
}


//-->
function getSubjNoticeList() {
	$.ajax({
		type: 'post'
		, url: '/mbl/std/subjNoticeListAjax.do'
		, data: {
			subj: '${p_subj}'
			, year: '${p_year}'
			, subjseq: '${p_subjseq}'
			, firstIndex: 0
			, recordCountPerPage: 100
		}
		, success: function(data) {
			$('#tableDisPlayMenu3').html(data);
		}
		, error: function(e) {
			alert(e);
		}
	});
}

function getSubjNoticeView(seq, subj, year, subjseq) {
	$.ajax({
		type: 'post'
		, url: '/mbl/std/subjNoticeViewAjax.do'
		, data: {
			p_seq: seq
			, p_subj: subj
			, p_year: year
			, p_subjseq: subjseq
		}
		, success: function(data) {
			$('#tableDisPlayMenu3').html(data);
		}
		, error: function(e) {
			alert(e);
		}
	});
}
</script>
</div>
</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>

