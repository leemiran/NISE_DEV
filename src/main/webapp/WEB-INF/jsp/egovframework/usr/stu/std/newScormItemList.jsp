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
						<c:set var="oldSeq" value=""/>
						<c:set var="oldItem" value=""/>
						<c:set var="depth" value="1"/>
						<c:forEach items="${itemList}" var="result">
						<c:if test="${oldSeq ne result.orgSeq}">
							<tr>
								<td class="left">
									<a href="#none" onclick="actionListNew('<c:out value="${result.orgSeq}"/>');">
										[<FONT ID="SIGN_<c:out value="${result.orgSeq}"/>">+</FONT>]${result.orgTitle}
									</a>
									<c:if test="${empty studyUse}">
									<a href="#none"title="새창" onclick="doStudyPopup('<c:out value="${result.orgSeq}"/>', '');">
										<img src="/images/adm/btn/btn_my01.gif" alt="학습시작" />
									</a>
									</c:if>
								</td>
								<td></td>
								<td></td>
								<td>
									<c:if test="${result.completeYn eq 0}"><img src="/images/adm/btn/btn_my03.gif" alt="학습완료" /></c:if>
									<c:if test="${result.completeYn eq 1}"><img src="/images/adm/btn/btn_my04.gif" alt="학습완료" /></c:if>
									<c:if test="${result.completeYn eq 2}"><img src="/images/adm/btn/btn_my02.gif" alt="학습완료" /></c:if>
								</td>
							</tr>
							<c:set var="oldSeq" value="${result.orgSeq}"/>
						</c:if>
						<c:if test="${oldSeq eq result.orgSeq}">
							<tr id="TR_<c:out value="${result.orgSeq}"/>" value="N" style="display:none">
								<c:if test="${result.highItemSeq == 0 }">
									<c:set var="depth" value="1"/>
								</c:if>
								<c:if test="${result.highItemSeq != 0 and result.highItemSeq != oldItem }">
									<c:set var="depth" value="${depth + 1}"/>
									<c:set var="oldItem" value="${result.highItemSeq}"/>
								</c:if>
								<td class="left" style="padding-left:<c:out value="${25 * depth}"/>px;">
									${result.itemTitle}
								</td>
								<td>
									<c:if test="${result.rsrcSeq != 0 }">
										${result.attempt}회
									</c:if>
								</td>
								<td>
									<c:if test="${result.rsrcSeq != 0  and not empty result.totalTime }">
									<c:out value="${customFn:toScormTimeConvert(result.totalTime)}" />
									</c:if>
								</td>
								<td>
									<c:if test="${result.rsrcSeq != 0 }">
										<c:if test="${empty result.completionStatus}">학습전</c:if>
										<c:if test="${result.completionStatus eq 'completed'}">학습완료</c:if>
										<c:if test="${result.completionStatus eq 'incomplete'}">학습중</c:if>
									</c:if>
								</td>
							</tr>
						</c:if>
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