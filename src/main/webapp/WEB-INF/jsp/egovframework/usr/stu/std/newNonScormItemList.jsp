<%@ page pageEncoding="UTF-8"%>
				<div class="tbList1">
					<table summary="제목, 학습횟수, 학습시간, 이수여부로 구성" width="100%">
						<caption>학습시작 목록</caption>
						<colgroup>							
							<col width="55%" />
							<col width="15%" />
							<col width="15%" />
                            <col width="15%" />
						</colgroup>
						<thead>
							<tr>
								<th scope="col">제목</th>
								<th scope="col">학습횟수</th>
								<th scope="col">학습시간</th>
								<th scope="col">이수여부</th>
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
									<a href="#none" onclick="actionListNew('<c:out value="${result.module}"/>');">
									[<FONT ID="SIGN_<c:out value="${result.module}"/>">+</FONT>]
									[<c:out value="${result.module}"/>]
									<c:out value="${result.moduleName}"/>
									</a>
									<c:if test="${empty studyUse}">
									<a href="#none" title="새창" onclick="doStudyPopup('<c:out value="${result.module}"/>', '');">
										<img src="/images/adm/btn/btn_my01.gif" alt="학습시작" />
									</a>
									</c:if>
								</td>
								<td></td>
								<td></td>
								<td>
									<c:if test="${ result.studyCnt eq '0' and result.incomplete eq '0' }">
				                	<img src="/images/adm/btn/btn_my03.gif" alt="학습전" />
				                	</c:if>
				                	<c:if test="${ result.lessonCnt != result.studyCnt and result.incomplete ne '0' }">
				                	<img src="/images/adm/btn/btn_my04.gif" alt="학습중" />
				                	</c:if>
				                	<c:if test="${ result.lessonCnt == result.studyCnt }">
				                	<img src="/images/adm/btn/btn_my02.gif" alt="학습완료" />
				                	</c:if>
								</td>
							</tr>
							<c:set var="oldModule" value="${result.module}"/>
						</c:if>
							<c:set var="depth" value="${result.depth}"/>
							<c:if test="${p_contenttype == 'C'}">
							<c:set var="depth" value="${result.depth - 1}"/>
							</c:if>
							<tr id="TR_<c:out value='${result.module}'/>" value="N" style="display:none">
								<td class="left" style="padding-left:<c:out value="${25 * depth}"/>px;">
									<c:out value="${result.lesson}"/>. <c:out value="${result.lessonName}"/>
								</td>
								<td>
								<c:if test="${ result.progressYn == 'Y' }">
									<c:out value="${result.lessonCount}"/>회
								</c:if>
								</td>
								<td></td>
								<td>
								<c:if test="${ result.progressYn == 'Y' }">
									<c:if test="${empty result.lessonstatus}">학습전</c:if>
									<c:if test="${result.lessonstatus eq 'Y'}">학습완료</c:if>
									<c:if test="${result.lessonstatus eq 'N'}">학습중</c:if>
								</c:if>
								</td>
							</tr>
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
</script>