<%@ page pageEncoding="UTF-8"%>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate value="${now}" pattern="yyyyMMddhh" var="currentDate"/>

<c:if test="${empty studyPopup}">
				<div class="mybox">
					<h3>${EduScore.subjnm} - ${fn2:toNumber(EduScore.subjseq)}기</h3>					
					<dl>
						<dt style="width:5px;"><img src="/images/adm/study/bulp.png" alt="화살표" /></dt>
						<dt style="width:130px;">나의진도율 : 
						<span class="txtred2"><strong>			
								<c:choose>
									<c:when test="${currentDate >= EduScore.edustart && currentDate <= EduScore.eduend}">
										${progress}%
									</c:when>
									<c:otherwise>
										<c:if test="${not empty EduScore.tstep}">
											${EduScore.tstep}%
										</c:if>
										<c:if test="${empty EduScore.tstep}">
											0%
										</c:if>
									</c:otherwise>
								</c:choose>
							
						</strong></span>
						</dt>
						<dd style="width:135px; border:1px solid #fff;" class="left" >
                        	<div class="graph_1" style="width:${progress}%"></div>
                        </dd>
                        <dd class="dot"></dd>
					
					   <dt style="width:5px;"><img src="/images/adm/study/bulp.png" alt="화살표" /></dt>
                        <dt>교육기간 : ${fn2:getFormatDate(EduScore.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(EduScore.eduend, 'yyyy.MM.dd')}</dt>
						<dd class="dot"></dd>
						
					   <dt style="width:5px;"><img src="/images/adm/study/bulp.png" alt="화살표" /></dt>
						<dt style="width:130px;">권장진도율 : <span class="txtblue2"><strong>${promotion}%</strong></span></dt>
						<dd style="width:135px; border:1px solid #fff;" class="left" >
                        	<div class="graph_2" style="width:${promotion}%"></div>
                        </dd>
						<dd class="dot"></dd>
						
					   <dt style="width:5px;"><img src="/images/adm/study/bulp.png" alt="화살표" /></dt>
                        <dt>차시진도(완료/전체) : ${edudatecnt} / ${datecnt} (출석일 : ${attendCnt}일)</dt>	
                        
					</dl>
				</div>
</c:if>