<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>



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


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_userid"     value = "${sessionScope.userid}" />
	<input type = "hidden" name="p_subj" />
	<input type = "hidden" name="p_year" />
	<input type = "hidden" name="p_subjseq" />





	
  <!-- search wrap-->
		<div class="stduyWrap">
			<ul class="floatL">
            	<li>학습중인 과정 :</li>
            </ul>
            <div class="floatL">    
            
<!--            	넘어온 값    -->
            	<c:set var="selectedSubjSeq">${p_subj},${p_year},${p_subjseq}</c:set>
            	    	
            	<select onchange="whenSelectSubj(this.value);" style="width:400px;">
           			<option value="">과정을 선택하세요.</option>
           			
	            	<c:forEach var="result" items="${list}" varStatus="status">
	            	
	            		<c:set var="resultSubjSeq">${result.subj},${result.year},${result.subjseq}</c:set>
	            		
						<option value="${result.subj},${result.year},${result.subjseq}"
						<c:if test="${selectedSubjSeq eq resultSubjSeq}">selected</c:if>
						>${result.subjnm}</option>
						
					</c:forEach>
					
					<c:if test="${empty list}"><option value="">수강중인 과목이 없습니다.</option></c:if>
			  </select>
			  
            </div>
		</div>
		<!-- // search wrap -->		
		
		<!-- search wrap-->
		<div class="myWrap">
			<ul>
            	<li>
            	<strong>학습시간</strong> : 
	            	${fn2:getNumberToString(timeview.totalTime)} : 
	            	${fn2:getNumberToString(timeview.totalMinute)} :  
	            	${fn2:getNumberToString(timeview.totalSec)}
            	</li>
                <li style="padding-right:140px;"><strong>최근학습일</strong> : ${fn2:getFormatDate(dateview.ldate, 'yyyy.MM.dd')}<span id="span_lastStudyDate"></span></li>
			</ul>
            <ul>                
                <li style="padding-right:179px;"><strong>강의실접근횟수</strong> : ${termview.attcnt}</li>
                <li><strong>진도율</strong> : ${progview.tstep}%</li>                               
            </ul>            
		</div>






<c:if test="${not empty checklist}">

         <div class="sub_text">
                    <h4>온라인 출석부</h4>
                </div>
                <!-- list -->
				<div class="tbDetail">
					<table summary="연수기간, 출석일수로 구성">
						<caption>온라인 출석부</caption>
						<colgroup>
							<col width="15%"/>
							<col width="40%"/>
							<col width="15%"/>
							<col width="30%"/>							
						</colgroup>
						<thead>
							<tr>
								<th scope="col">연수기간</th>
								<td class="num">${fn2:getFormatDate(termview.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(termview.eduend, 'yyyy.MM.dd')}</td>
								<th scope="col">출석일수</th>
								<td class="num">${termview.attcnt} 일</td>
							</tr>
						</thead>
						<tbody>							
						</tbody>
					</table>
				</div>
                
                
                
                
                <div >
					<table class="tbList" summary="온라인 출석부 상세내용">
						<caption>온라인 출석부</caption>
						<tbody>
						<tr>

<c:set var="cnt">0</c:set>
<c:set var="lastStudyDate"></c:set>
						
<c:forEach var="result" items="${checklist}" varStatus="status">
<c:set var="cnt">${status.count}</c:set>
<c:if test="${result.ist eq 'O'}"><c:set var="lastStudyDate">${fn2:getFormatDate(result.dateSeq, 'yyyy/MM/dd')}</c:set></c:if>
							
								<td style="padding-top:0px;padding-bottom:0px;">
								<table>
									<thead>
										<tr>
											<th scope="col">${fn2:getFormatDate(result.dateSeq, 'MM/dd')}</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>${result.ist}</td>
										</tr>                            					
									</tbody>
								</table>
								</td>
								
			<c:if test="${status.count % 10 == 0}">
				</tr>
				<tr>
			</c:if>
								
</c:forEach>

<c:if test="${not empty lastStudyDate}">
	<script type="text/javascript">
		document.all["span_lastStudyDate"].innerHTML = '${lastStudyDate}';
	</script>
</c:if>

<c:if test="${cnt % 10 != 0}">
	<c:forEach var="i" begin="1" end="${10 - (cnt % 10)}">
								<td style="padding-top:0px;padding-bottom:0px;">
								<table>
									<thead>
										<tr>
											<th scope="col">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>&nbsp;</td>
										</tr>                            					
									</tbody>
								</table>
								</td>
	</c:forEach>
</c:if>

							</tr>
							
							
						</tbody>
                        
					</table>
				</div>     
				
</c:if>

				          
        <!-- text -->
        <div style="padding-bottom:30px;">
            <ul class="sub_text">
            	<h4>강의를 매일 나누어서 학습해야 출석점수가 감점되지 않습니다. </h4>
                <li>(강의수강 후 학습현황 조회에서 출석되었는지 꼭 확인해 주세요.)</li>                
            </ul>
            <ul class="sub_text">
            	<h4>[강의듣기]에서 강의를 학습한 날짜만 출석으로 인정됩니다. (로그인만 하시면 출석으로 인정하지 않습니다.) </h4>
                <h4>하루에 1차시(3강의)를 매일 매일 학습하는 것을 권장하고 있습니다.</h4>               
            </ul>            
        </div>
        <!-- text -->






</form>



        
        
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



function whenSelectSubj(option) {

	textarr = option.split(",");
	var subj ='';
	var year = '';
	var subjseq = '';
	for(var j=0; j<textarr.length; j++) {
		subj = textarr[0];
		year = textarr[1];
		subjseq = textarr[2];
	}

   	if(option != ""){		       
       thisForm.p_subj.value = subj;
       thisForm.p_year.value = year;
       thisForm.p_subjseq.value = subjseq;
       thisForm.action='/usr/myh/myActivityList.do';
       thisForm.target = '_self';
       thisForm.submit();
   	}else{
		alert('과정을 선택하세요.');
		return;
   	}
	
}



//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
