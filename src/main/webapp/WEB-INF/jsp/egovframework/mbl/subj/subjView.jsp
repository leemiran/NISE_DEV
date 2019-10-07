<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/userLoginCheck.jsp" %>

	<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>교육과정</a></span>
	</div>

	<!-- 본문 -->
	<div id="wcontainer">
		<!-- 교육과정 상세 -->
		<div class="yun mrt15">
			<h3 class="mrb10">[${view.subj}] ${view.subjnm}</h3>
			<div class="yunimg"><img src="/images/mbl/temp/sample_big.jpg" alt="" /></div>
			<div class="listTable bview">
				<table summary="과정분류,교육기간,복습기간,수강료로 구분" cellspacing="0" width="100%">
					<caption>연수상세</caption>
					<colgroup>
							<col width="33%" />
							<col width="" />
							<col width="17%" />
							<col width="20%" />
					</colgroup>
					<tr>
						<th scope="col">과정분류</th>
						<td colspan="3">${view.uppcheck}</td>
					</tr>
					<tr>
						<th scope="col">교육기간</th>
						<td colspan="3">
									${(view.eduperiod eq '') ? '0' : view.eduperiod}주 (${view.edutimes}시간)
						</td>
					</tr>
					<tr>
						<th scope="col">복습기간</th>
						<td colspan="3">
									<c:if test="${view.isablereview eq 'Y'}">
										/ 수료 후 ${view.reviewdays}개월간 복습가능
									</c:if>
									
									<c:if test="${view.isablereview ne 'Y'}">
										/ 복습없음
									</c:if>
						</td>
					</tr>
					<tr>
						<th scope="col">수강료</th>
						<td><fmt:formatNumber value="${view.biyong}" type="number"/>원</td>
						<th scope="col">정원</th>
						<td><fmt:formatNumber value="${view.studentlimit}" type="number"/>명</td>
					</tr>
				</table>
			</div>
		</div>
		<!-- //교육과정 상세 -->
		
		
		
		
		<!-- 리스트 -->
		<ul class="imgList mrt5" id="tableDisPlayMenu1" style="display:block;">
		<c:forEach items="${subjSeqList}" var="result" varStatus="status">
			<li>
				<dl class="blist">
					<dt>
						${result.subjnm}-${fn2:toNumber(result.subjseq)}기
					</dt>
					<dd>
					<span class="tit">연수기간</span> 
					${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}
					</dd>
					<dd>
					<span class="tit">정원[신청수]</span> 
					<fmt:formatNumber value="${result.studentlimit}" type="number"/>[<fmt:formatNumber value="${result.propcnt}" type="number"/>]
					</dd>

					<dd class="btn">
							<c:choose>
									<c:when test="${result.empGubun eq 'P' && fn:indexOf(result.subj, 'PAR') != -1}">
									<a class="btn01"><span>신청불가</span></a>
									</c:when>
									<c:otherwise>
										<c:if test="${result.propstatus eq 'A'}">
	                                    	<c:if test="${result.isPropose > 0}">
	                                    		<a class="btn01"><span>신청중</span></a>
	                                    	</c:if>
	                                    	<c:if test="${result.isPropose <= 0}">
	                                    		 <a href="javascript:alert('수강신청은 온라인에서만 가능합니다.')" class="btn_blu"><span>신청가능</span></a>
	                                    	</c:if>
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'B'}">
	                                    	 <a class="btn_blu"><span>신청전</span></a>
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'C'}">
	                                    	<a class="btn01"><span>신청마감</span></a>
	                                    </c:if>	
									</c:otherwise>
								</c:choose>
					</dd>
				</dl>
			</li>
		</c:forEach>
		</ul>
		
		
		
		
		
		
		
		
		<!-- 연수정보 -->
		<div class="info mrt10">
		  <table summary="교육목표,교육대상,교육내용으로 구성" cellspacing="0" width="100%">
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
		      <th scope="col">교육목표</th>
		      <td colspan="3"><c:out value="${fn:replace(view.intro, lf, '<br>')}" escapeXml="false"/></td>
	        </tr>
		    <tr>
		      <th scope="col">교육대상</th>
		      <td colspan="3"><c:out value="${fn:replace(view.edumans, lf, '<br>')}" escapeXml="false"/></td>
	        </tr>
	        <tr>
		      <th scope="col">교육내용</th>
		      <td colspan="3"><c:out value="${fn:replace(view.explain, lf, '<br>')}" escapeXml="false"/></td>
	        </tr>
	        
	        <!--  
		    <tr>
		      <th scope="col">평가기준</th>
		      <td colspan="3">
		      
							<table summary="" cellspacing="0" width="100%">
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
    
    
    
    
    

		<!-- button -->
		<ul class="btnR">
			<c:url var="selectBoardArticleUrl" value="/mbl/subj/subjList.do">
              <c:param name="recordCountPerPage" value="${recordCountPerPage}" />
              <c:param name="firstIndex" value="${firstIndex}" />
              <c:param name="search_upperclass" value="${search_upperclass}" />
              <c:param name="search_text" value="${search_text}" />
            </c:url>
			<li><a href="${selectBoardArticleUrl}" class="btn02"><span>목록</span></a></li>
		</ul>
		
	</div>
	<!-- //본문 -->
</div>
</body>


<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>


