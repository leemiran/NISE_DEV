<%@ page pageEncoding="UTF-8"%>
				<div class="tbList1">
					<table summary="제목, 학습시작, 학습종료, 수강여부로 구성" width="100%">
						<caption>학습시작 목록</caption>
						<colgroup>							
							<col width="%" />
							<col width="12%" />
							<col width="12%" />
							<col width="13%" />
                            <col width="13%" />
                            <col width="10%" />
						</colgroup>
						<thead>
							<tr>
								<th scope="col">제목</th>
								<th scope="col">최초<br/>학습일자</th>
								<th scope="col">마지막<br/>학습일자</th>
								<th scope="col">나의<br/>학습시간</th>
								<th scope="col">표준<br/>학습시간</th>
								<th scope="col">수강여부</th>
							</tr>
						</thead>
						<tbody>
<!--						<img src="/images/adm/btn/btn_my02.gif" alt="학습완료" />-->
<!--						<img src="/images/adm/btn/btn_my03.gif" alt="학습전" />-->
<!--						<img src="/images/adm/btn/btn_my04.gif" alt="학습중" />-->

						<c:set var="oldModule" value=""/>
						<c:set var="depth" value="1"/>
						<c:forEach var="result" items="${itemList}" varStatus="status">
						<c:if test="${result.module != oldModule}">
							<tr>
								<td class="left">
<!--									<a href="#none" onclick="actionListNew('<c:out value="${result.module}"/>');">-->
<!--									[<FONT ID="SIGN_<c:out value="${result.module}"/>">+</FONT>]-->
									
									<c:if test="${p_contentLessonAllView ne 'Y'}">
										[<c:out value="${result.module}"/>]
										<c:out value="${result.moduleNm}"/>
									</c:if>
									
									<c:if test="${p_contentLessonAllView eq 'Y'}">
										[<c:out value="${result.lesson}"/>]
										<c:out value="${result.lessonNm}"/>
									</c:if>
							
									
									
									
<!--									</a>-->
										
									
									<c:if test="${empty studyUse}">
										<%-- <c:choose>
											<c:when test="${p_subj eq 'PRF150012' && (result.module eq '08' || result.module eq '10' ) }">
											
											</c:when>
											<c:otherwise> --%>
												<c:if test="${p_contentLessonAllView ne 'Y'}">
													<a href="#none" title="새창" onclick="doStudyPopup('<c:out value="${result.module}"/>', '', '', '${result.suryoLessonTime }' );">
														<img src="/images/adm/btn/btn_my01.gif" alt="학습시작" />
													</a>
												</c:if>
												<c:if test="${p_contentLessonAllView eq 'Y'}">
													<a href="#none" title="새창" onclick="doStudyPopup('<c:out value="${result.module}"/>', '<c:out value="${result.lesson}"/>', '', '${result.suryoLessonTime }' );">
														<img src="/images/adm/btn/btn_my01.gif" alt="학습시작" />
													</a>
												</c:if>
											<%--</c:otherwise>
												
											 
										</c:choose>  --%>
										
									
										
									
									</c:if>
									
									
								</td>
								<td>
									<c:if test="${not empty result.startEdu}">
										<c:out value="${fn2:getFormatDate(result.startEdu, 'yyyy/MM/dd HH:mm')}"/>
									</c:if>
								</td>
								<td>
									<c:if test="${not empty result.lastEdu}">
										<c:out value="${fn2:getFormatDate(result.lastEdu, 'yyyy/MM/dd HH:mm')}"/>
									</c:if>
								</td>
								<td>
									<c:choose>
										<c:when test="${result.userLessonTimeNum > result.totalLessonTimeNum }">
											<font color="red">${result.totalLessonTime }</font>
										</c:when>
										<c:when test="${result.finalStatus eq 'Y' and result.userLessonTimeNum eq 0 }">
											<font color="red">${result.totalLessonTime }</font>
										</c:when>
										<c:otherwise>
											<font color="red">${result.userLessonTime }</font>
										</c:otherwise>
									</c:choose>
								</td>
								<td>${result.totalLessonTime }</td>
								<td>
									<!--<c:if test="${ result.studyCount eq '0'}">
				                	<img src="/images/adm/btn/btn_my03.gif" alt="학습전" />
				                	</c:if>
				                	<c:if test="${ result.totalLessonCount != result.studyEndCount and result.studyCount ne '0' }">
				                	<img src="/images/adm/btn/btn_my04.gif" alt="학습중" />
				                	</c:if>
				                	<c:if test="${ result.totalLessonCount == result.studyEndCount }">
				                	<img src="/images/adm/btn/btn_my02.gif" alt="학습완료" />
				                	</c:if>-->
				                	<c:if test="${result.lessonfinalstatus ne 'Y' and result.lessonfinalstatus ne 'N' }">
				                	<img src="/images/adm/btn/btn_my03.gif" alt="학습전" />
				                	</c:if>
				                	<c:if test="${ result.lessonfinalstatus eq 'N'}">
				                	<img src="/images/adm/btn/btn_my04.gif" alt="학습중" />
				                	</c:if>
				                	<c:if test="${ result.lessonfinalstatus eq 'Y'}">
				                	<img src="/images/adm/btn/btn_my02.gif" alt="학습완료" />
				                	</c:if>
								</td>
							</tr>
							
							<c:if test="${p_contentLessonAllView ne 'Y'}">
								<c:set var="oldModule" value="${result.module}"/>
							</c:if>
							
							
							
						</c:if>
<!--							<tr class="TR_<c:out value='${result.module}'/>" value="N" style="display:none">-->
<!--								<td class="left" style="padding-left:<c:out value="${25 * depth}"/>px;">-->
<!--								<a title="새창" href="javascript:doStudyPopup('<c:out value="${result.module}"/>', '${result.lesson}');">-->
<!--									<c:out value="${result.lesson}"/>. <c:out value="${result.lessonNm}" />-->
<!--								</a>-->
<!--								</td>-->
<!--								<td><c:out value="${fn2:getFormatDate(result.firstEdu, 'yyyy/MM/dd HH:mm')}"/></td>-->
<!--								<td><c:out value="${fn2:getFormatDate(result.ldate, 'yyyy/MM/dd HH:mm')}"/></td>-->
<!--								<td>-->
<!--									<c:if test="${result.lessonstatus eq 'Y'}">-->
<!--										<font color="blue">학습완료</font>-->
<!--									</c:if>-->
<!--									<c:if test="${result.lessonstatus ne 'Y'}">-->
<!--										<c:if test="${empty result.firstEdu}"><font color="red">학습전</font></c:if>-->
<!--										<c:if test="${not empty result.firstEdu}"><font color="green">학습중</font></c:if>-->
<!--									</c:if>-->
<!--								</td>-->
<!--							</tr>-->
						</c:forEach>
						
						
						</tbody>
					</table>
				</div>
<script>
	function actionListNew(Id){
		if($('#SIGN_'+Id).attr('name')!="on"){
			$('.TR_'+Id).attr('style','display:block;');
			$('#SIGN_'+Id).attr('name','on');
		}else{
			$('.TR_'+Id).attr('style','display:none;');
			$('#SIGN_'+Id).attr('name','off');
		}
	}
	function actionList(strObjectID){
	    var strObjTR = "TR_";
	    var strObjSign = "SIGN_";
	
	    var TRObjIdVal = "";
	    if(eval(strObjTR+strObjectID).value == undefined){
		    if(!eval(strObjTR+strObjectID).length){
	    		TRObjIdVal = eval(strObjTR+strObjectID).value;
		    }else{
	    		TRObjIdVal = eval(strObjTR+strObjectID)[0].value;
		    }
	    }else{
	    	TRObjIdVal = eval(strObjTR+strObjectID).value;
	    }
	
	    if(	TRObjIdVal == 'Y' ){
	        
	    	if(eval(strObjTR+strObjectID).length == undefined){
	            eval(strObjTR+strObjectID).style.display = "none";
	            eval(strObjTR+strObjectID).value='N';
	    	}else if(eval(strObjTR+strObjectID).length>1){
		        for(var i = 0 ; i < eval(strObjTR+strObjectID).length ; i++ ){
		       		eval(strObjTR+strObjectID)[i].style.display = "none";
			      	eval(strObjTR+strObjectID)[i].value='N';
				}			        
	        }
	        
	        eval(strObjSign+strObjectID).innerHTML=' ';
	        eval(strObjSign+strObjectID).innerHTML='+';
	        
	    }else {
	        //alert(eval(strObjTR+strObjectID).length);
	        if(eval(strObjTR+strObjectID).length == undefined){
	        	eval(strObjTR+strObjectID).style.display = "block";
	            eval(strObjTR+strObjectID).value='Y';
	        }else if(eval(strObjTR+strObjectID).length>1){
		        for(var i = 0 ; i < eval(strObjTR+strObjectID).length ; i++ ){
		        	eval(strObjTR+strObjectID)[i].style.display = "block";
		        	eval(strObjTR+strObjectID)[i].value='Y';
		        }			        
	        }
	        
	        eval(strObjSign+strObjectID).innerHTML=' ';
	        eval(strObjSign+strObjectID).innerHTML='-';
	    }
	    
	}

	//window.onload = function () { actionList('01'); }
</script>