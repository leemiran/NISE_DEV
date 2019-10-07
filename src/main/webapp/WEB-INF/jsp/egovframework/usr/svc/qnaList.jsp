<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype" value="<c:out value="${p_contenttype}"  escapeXml="true" />" />
<input type="hidden" name="p_subj" value="<c:out value="${p_subj}"  escapeXml="true" />" />
<input type="hidden" name="p_year" value="<c:out value="${p_year}"  escapeXml="true" />" />
<input type="hidden" name="p_subjseq" value="<c:out value="${p_subjseq}"  escapeXml="true" />" />
<input type="hidden" name="p_studytype" value="<c:out value="${p_studytype}"  escapeXml="true" />" />
<input type="hidden" name="p_process" value="<c:out value="${p_process}"  escapeXml="true" />" />
<input type="hidden" name="p_next_process" value="<c:out value="${p_next_process}"  escapeXml="true" />" />
<input type="hidden" name="p_height" value="<c:out value="${p_height}"  escapeXml="true" />" />
<input type="hidden" name="p_width" value="<c:out value="${p_width}"  escapeXml="true" />" />
<input type="hidden" name="p_lcmstype" value="<c:out value="${p_lcmstype}"  escapeXml="true" />" />
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
	<fieldset>
    <legend>연수관련상담</legend>
	<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex" value="<c:out value="${pageIndex}"  escapeXml="true" />" />
	<input type = "hidden" name="p_seq"     value="<c:out value="${p_seq}"  escapeXml="true" />" />
	<input type = "hidden" name="p_openchk"     value="<c:out value="${p_openchk}"  escapeXml="true" />" />
	<input type = "hidden" name="p_tabseq"     value="<c:out value="${p_tabseq}"  escapeXml="true" />" />
	
	    <!-- search wrap-->
		<div class="searchWrap7">
			<div  class="ysq">
				<!-- <div class="ysq1"><img src="/images/user/qna001.jpg" alt="자주 묻는 질문을 확인 하셨나요? 질문게시판 답변은 평일 24시간 이내로 답변 드립니다." /></div> -->
				<div class="ysq2"><a href="#" onclick="changeMenu(4, 2);return false;"><img src="/images/user/qna002.jpg" alt="자주묻는질문 바로가기" /></a></div>
				<div class="ysq3"><a href="#" onclick="changeMenu(3, 2);return false;"><img src="/images/user/qna003.jpg" alt="나의 질문 목록" /><c:if test="${not empty myQnaCnt }"><p></p>${myQnaCnt}개</c:if></a></div>
				<div class="ysq2" onclick="popupOpnion();"><a href="#"><img src="/images/user/qna004.jpg" alt="연수개선의견 바로가기" /></a></div>
			</div>			
			<%-- <div class="in" style="float:left; margin-left:27px;">
				<label for="p_search" class="blind">검색항목</label>
				<select name="p_search" id="p_search" title="검색항목">
					<option value='title' <c:if test="${p_search eq 'title'}">selected</c:if>>제목</option>
					<option value='content' <c:if test="${p_search eq 'content'}">selected</c:if>>내용</option>
					<c:if test="${p_tabseq eq '0' || p_tabseq eq '1'}">
						<option value='name' <c:if test="${p_search eq 'name'}">selected</c:if>>작성자</option>
						<option value='userid' <c:if test="${p_search eq 'userid'}">selected</c:if>>아이디</option>
						<option value='ldate' <c:if test="${p_search eq 'ldate'}">selected</c:if>>작성일자</option>
					</c:if>
				</select>
				<label for="p_searchtext" class="blind">검색어</label>
				<input type="text" name="p_searchtext" id="p_searchtext" value="<c:out value="${p_searchtext}"  escapeXml="true" />" size="87" onkeypress="fn_keyEvent('doSearchList')" style="ime-mode: active;" title="검색어" />
				<a href="#" onclick="doSearchList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
			</div> --%>
		</div>
		<!-- // search wrap -->		
		
		<!-- list table-->
		<div class="tbList">
			<table summary="번호, 제목, 상태, 작성자, 등록일, 조회수로 구성" cellspacing="0" width="100%">
            	<caption>연수관련상담목록</caption>
				<colgroup>
					<col width="10%" />
					<!-- <col width="10%" /> -->
					<col width="*" />
					<col width="10%" />
					<col width="8%" />
					<col width="10%" />
                    <col width="10%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row">번호</th>
						<!-- <th scope="row">구분</th> -->
						<th scope="row">제목</th>
						<th scope="row">상태</th>
						<th scope="row">작성자</th>
						<th scope="row">등록일</th>
                        <th scope="row">조회수</th>
					</tr>
				</thead>
				<tbody>
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-result.rn}"/></td>
						<%-- <td>
							<c:if test="${result.tabseq eq '0'}">
								연수후기
							</c:if>
							<c:if test="${result.tabseq eq '1'}">
								연수문의
							</c:if>
							<c:if test="${result.tabseq ne '1'}">
								<c:if test="${result.tabseq eq '3'}">
									환불요청
								</c:if>
								<c:if test="${result.tabseq ne '3'}">
									입금확인
								</c:if>
							</c:if>						
						</td> --%>
						<td class="left">
						<c:if test="${result.openchk eq 'Y'}">
							<a href="#none" onclick="doView('${result.seq}', '${result.openchk}')"><c:out value="${result.title}" escapeXml="true" /></a>
						</c:if>
						<c:if test="${result.openchk ne 'Y'}">
							<a href="#none" onclick="alert('본인글 또는 공개글만 조회가능 합니다.');return false;"><c:out value="${result.title}" escapeXml="true" /></a>							
						</c:if>
						</td>
						<td>
						
							<c:if test="${result.hasanswer eq 'Y'}">
								<img src="/images/user/end.gif" alt="완료"/>
							</c:if>
							<c:if test="${result.hasanswer ne 'Y'}">
								<img src="/images/user/ing.gif" alt="처리중"/>
							</c:if>
						
						</td>
						<td>${result.name}</td>
						<td >${fn2:getFormatDate(result.indate, 'yyyy.MM.dd')}</td>
                        <td class="num">${result.cnt}</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="7">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>				
				
				</tbody>
			</table>
		</div>
		<!-- list table-->

		<!-- 페이징 시작 -->
		<div class="paging">
			<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
		</div>
		<!-- 페이징 끝 -->
        <!-- button -->
		<ul class="btnR">
        	<li><a href="#"  onclick="doEdit('')"><img src="/images/user/btn_write.gif" alt="등록" /></a></li>
		</ul> 
		<!-- // button -->
	</fieldset>       
</form>




		 <!-- text -->
        
<c:if test="${p_tabseq ne '1'}">        
        <div>
            <div  class="sub_text"><h4>연수비의 반환 기준은 다음과 같습니다. (운영규정 제 13조 제 3항)</h4></div>
            <ul class="sub_text">
                <li>
                	<div class="txtList">
                        <table summary="사이버 일반연수, 사이버 직무연수, 반환금액으로 구성" cellspacing="0" width="100%">
                        	<caption>연수비의 반환 기준</caption>
                            <colgroup>
                                <!-- <col width="33%" /> -->
                                <col width="33%" />
                                <col width="33%" />                                
                            </colgroup>
                            <thead>
                                <tr>
                                    <!-- <th scope="col">사이버 일반연수</th> -->
                                    <th scope="col">사이버 직무연수</th>
                                    <th scope="col" class="last">반환금액</th>                                    
                                </tr>
                            </thead>
                            <tbody>					
                                <tr>
                                    <!-- <td>연수 시작 전</td>	 -->					
                                    <td>연수 시작 전</td>
                                    <td class="last">납입금 전액</td>                                    
                                </tr> 
                                <tr>
                                    <!-- <td>연수 시작 후 2일 이내</td>			 -->			
                                    <td>연수 시작 후 7일 이내</td>
                                    <td class="last">납입금의 70%</td>                                    
                                </tr>
                                <tr>
                                   <!--  <td>연수 시작 후 2일 경과</td>	 -->					
                                    <td>연수 시작 후 7일 경과</td>
                                    <td class="last">반환하지 아니함</td>                                    
                                </tr>                                               	
                            </tbody>
                        </table>
					</div>
                </li>
                <li>* 반환기준은 실 입금액에서 기본경비, 수수료 등을 제외한 범위로 한다.</li>               
             </ul>
            <div  class="sub_text"><h4>결제방법에 따른 환불처리 안내</h4></div>
            <ul class="sub_text">
                <li>
                	<div class="txtList">
                        <table summary="사이버 일반연수, 사이버 직무연수, 반환금액으로 구성" cellspacing="0" width="100%">
                        	<caption>결제방법에 따른 환불처리 안내</caption>
                            <colgroup>
                                <col width="20%" />
                                <col width="32%" />                                
                                <col width="48%" />                                
                            </colgroup>
                            <thead>
                                <tr>
                                    <th scope="col">결제방법</th>
                                    <th scope="col">환불과정</th>
                                    <th scope="col" class="last">환불처리기간</th>                                    
                                </tr>
                            </thead>
                            <tbody>					
                                <tr>
                                    <td>무통장</td>						
                                    <td class="left">
                                    	환불 요청 시 남겨주신 계좌번호로 <br>환불해 드립니다. 
									</td>
									<td class="left last"><!-- 일괄지급으로 3주 이상 소요되니 이점 양해 부탁드립니다. -->
									매월 1일 ~ 15일 입금 건은 그 달 말일 경 환불<br/>
 매월 16일 ~ 말일까지 입금 건은 다음 달 말일 경 환불
									</td>                                    
                                </tr> 
                                <tr>
                                    <td>신용카드</td>						
                                    <td class="left">
                                    	결제 대금이 청구되지 않도록 신용카드 <br>승인을 취소합니다.
                                    </td>
                                    <td  class="left last">
                                    	취소 확인은 취소 요청 7일 후 카드사에 문의해 주세요.
                                    </td>                                    
                                </tr>
                                <tr>
                                    <td>실시간 계좌이체</td>						
                                    <td class="left">
                                    	결제 대금이 자동입금 될 수 있도록 <br>실시간 계좌이체 취소합니다. 
                                    </td>
                                    <td class="left last">취소 후 계좌로 입금은 은행영업일 기준 3~4일 소요 됩니다.</td>                                    
                                </tr>                                               	
                            </tbody>
                        </table>
					</div>
                </li>
                <li>* 반환기준은 실 입금액에서 기본경비, 수수료 등을 제외한 범위로 한다.</li>               
             </ul>
           
            <div  class="sub_text"><h4>입금 확인 요청 게시 요령안내</h4></div>
	            <ul class="sub_text">            	
	                <!-- <li>1. 학교,교육청 등 연수비를 한꺼번에 무통장 입금하신 경우와 대신 납부하신 경우 글을 남겨주십시오.</li>
	                <li>2. 무통장 입금 내용을 연수관련상담 게시판에 남기셔야 빠른 수강승인이 가능합니다</li> -->
	                <li>연수신청인와 입금인이 다를 경우(학교명, 교육청명으로 단체 입금등),<br/>
 연수행정코너 입금확인요청 게시판에 글을 남겨주시면 빠른 수강승인 가능합니다.</li>
	                
	                <li><strong>[입력양식]</strong></li>
	                <li>
	                	  - 과정명 : 장애인 등에 대한 특수교육법 및 관련 법규의 이해<br/>
	                      - 입금일/금액: 2008.3.3. 100,000원<br/>
	                      - 무통장 입금자명 : 상록초등학교<br/>
	                      - 수강신청자 명 : 홍길동, 심순애 수강료 (2명분)<br/>
					</li>
	            </ul>
             <div  class="sub_text"><h4>환불요청 기재 요령 안내</h4></div>
             <ul class="sub_text">
                <li>* 환불을 요청하실 경우 아래 서식에 맞추어서 꼭 작성해 주세요. 모두 기재되어야 빠른 처리가 가능합니다.</li> 
                <li><strong>[입력양식]</strong></li>
                <li>
                	  - 결재방법 : 신용카드/실시간 계좌이체/무통장입금/가상계좌 중 택1<br/>
                      - 결재일/금액/수강신청자: 2008.3.3. 50,000 원 / 홍길동<br/>
                      - 환불 은행명(계좌번호) :<br/>
                      - 예금주 명 : 홍길동 (수강 신청자와 동일해야 환불이 가능합니다.)<br/>
                      - 전하실 말씀 :
				</li>
            </ul>
            <div  class="sub_text"><h4>교재반환안내</h4></div>
             <ul class="sub_text">
                <li>* 개인 사정으로 인한 환불시에는 교재를 반환해야 하며 교재반환 배송비를 부담하셔야 합니다.</li> 
                <li>* 교재반환주소: 충남 아산시 배방읍 공원로 40. 국립특수교육원 부설 원격교육연수원</li> 
            </ul>
            
        </div>
</c:if>        
        <!-- text -->

<script type="text/javascript">
//<![CDATA[

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {
	<c:if test="${p_tabseq eq '1'}">	
	thisForm.action = "/usr/svc/qnaList01.do";
	</c:if>
	<c:if test="${p_tabseq ne '1'}">	
	thisForm.action = "/usr/svc/qnaList02.do";
	</c:if>
	
	thisForm.target = "_self";
	thisForm.pageIndex.value = "1";
	thisForm.submit();
}

function doSearchList() {

	if(thisForm.p_searchtext.value == ''){
		if(!confirm("검색어 없이 검색하시면 전체가 검색됩니다. 검색하시겠습니까?")){
			thisForm.p_searchtext.focus(); 
			return;
		}else{
			<c:if test="${p_tabseq eq '1'}">	
			thisForm.action = "/usr/svc/qnaList01.do";
			</c:if>
			<c:if test="${p_tabseq ne '1'}">	
			thisForm.action = "/usr/svc/qnaList02.do";
			</c:if>
			thisForm.target = "_self";
			thisForm.pageIndex.value = "1";
			thisForm.submit();
			
		}
	}else{
		
		<c:if test="${p_tabseq eq '1'}">	
		thisForm.action = "/usr/svc/qnaList01.do";
		</c:if>
		<c:if test="${p_tabseq ne '1'}">	
		thisForm.action = "/usr/svc/qnaList02.do";
		</c:if>
		thisForm.target = "_self";
		thisForm.pageIndex.value = "1";
		thisForm.submit();
	}
	
}

function doLinkPage(index) {
	<c:if test="${p_tabseq eq '1'}">	
	thisForm.action = "/usr/svc/qnaList01.do";
	</c:if>
	<c:if test="${p_tabseq ne '1'}">	
	thisForm.action = "/usr/svc/qnaList02.do";
	</c:if>
	thisForm.pageIndex.value = index;
	thisForm.target = "_self";
	thisForm.submit();
}



function doView(p_seq, p_openchk){
	thisForm.p_seq.value = p_seq;
	thisForm.p_openchk.value = p_openchk;
	<c:if test="${p_tabseq eq '1'}">	
	thisForm.action = "/usr/svc/qnaView01.do";
	</c:if>
	<c:if test="${p_tabseq ne '1'}">	
	thisForm.action = "/usr/svc/qnaView02.do";
	</c:if>
	thisForm.target = "_self";
	thisForm.submit();
}

function doEdit(){
	thisForm.p_seq.value = '';
	<c:if test="${p_tabseq eq '1'}">	
	thisForm.action = "/usr/svc/qnaEdit01.do";
	</c:if>
	<c:if test="${p_tabseq ne '1'}">	
	thisForm.action = "/usr/svc/qnaEdit02.do";
	</c:if>
	thisForm.target = "_self";
	thisForm.submit();
}
//]]>

function popupOpnion() {
	var frm = eval('document.<c:out value="${gsMainForm}"/>');

	var target  = "popupOpnion";
   	
	var options = "toolbar=0,location=0,directories=0,status=no,menubar=0,scrollbars=auto,resizable=no,top=30,left=30,copyhistory=0,width=790,height=600";
    window.open('', target, options);
	
	frm.action = "/usr/bod/opinionUpgradePopup.do";
	frm.target = target;
	frm.submit();
}
</script>

<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
