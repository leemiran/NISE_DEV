<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_action" 	id="p_action">
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_ordseq" 	id="p_ordseq">
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="whenInsertPage()"><span>등록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="whenWeight()"><span>저장</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col />
				<col width="150px" />
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th rowspan="2">No</th>
					<th rowspan="2">과제명</th>
					<th >제출기한</th>
					<th rowspan="2">만점</th>
					<th rowspan="2">가중치</th>
				</tr>
				<tr>
					<th>추가체줄기한</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}"  var="result" varStatus="i">
				<tr>
					<td rowspan="2">${fn:length(list) - (i.count - 1)}</td>
					<td rowspan="2"><a href="javascript:goModify('${result.ordseq}');">${result.title}</a></td>
					<td>
						<c:if test="${result.indate eq 'Y'}">
							<font color="blue">${fn2:getFormatDate(result.startdate, 'yyyy.MM.dd') } ~ ${fn2:getFormatDate(result.expiredate, 'yyyy.MM.dd') }</font>
						</c:if>
						<c:if test="${result.indate ne 'Y'}">
							${fn2:getFormatDate(result.startdate, 'yyyy.MM.dd') } ~ ${fn2:getFormatDate(result.expiredate, 'yyyy.MM.dd') }
						</c:if>
					</td>
					<td rowspan="2">${result.perfectscore}</td>
					<td rowspan="2">
						<input type="text" class="text" name="p_weight" style="text-align: right" value="${result.weight}" maxlength="3" size="3" onkeyup="lfn_CheckField(this, 0);" />%
            			<input type="hidden" name="p_reportseq" value="${result.ordseq}">
					</td>
				</tr>
				<tr>
					<td>
						<c:if test="${result.adddate eq 'Y'}">
							<font color="blue">${fn2:getFormatDate(result.restartdate, 'yyyy.MM.dd') } ~ ${fn2:getFormatDate(result.reexpiredate, 'yyyy.MM.dd') }</font>
						</c:if>
						<c:if test="${result.adddate ne 'Y'}">
							${fn2:getFormatDate(result.restartdate, 'yyyy.MM.dd') } ~ ${fn2:getFormatDate(result.reexpiredate, 'yyyy.MM.dd') }
						</c:if>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="5">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	//search 함수
	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		if( frm.ses_search_subj.value == "" ){
			alert("과정을 선택하세요.");
			return;
		}

		frm.p_subj.value = frm.ses_search_subj.value;
		frm.p_year.value = frm.ses_search_year.value;
		frm.p_subjseq.value = frm.ses_search_subjseq.value;
		
		frm.p_action.value = "go";
		frm.action = "/adm/rep/reportProfList.do";
		frm.target = "_self";
		frm.submit();
	}

	function whenInsertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( frm.p_subj.value == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		frm.action = "/adm/rep/reportProfInsertPage.do";
		frm.target = "_self";
		frm.submit();
	}


	function goModify(v_ordseq) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		
		//frm.action = "/servlet/controller.learn.ReportProfServlet";
		frm.action = "/adm/rep/reportProfUpdatePage.do";
		frm.p_ordseq.value = v_ordseq;
		frm.target = "_self";
		frm.submit();
	}

	/* 필드값 숫자 체크 */
	function lfn_CheckField(numObj, defaultNum) {
		if (isNaN(numObj.value)) {
	      	alert("숫자만 입력가능합니다."); <%//숫자만 입력가능합니다.%>
	      	numObj.value = defaultNum;
	      	numObj.select();
	      	return;
		}
	}

	function whenWeight(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( frm.p_subj.value == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		if( '<c:out value="${fn:length(list)}"/>' == '0' ){
			alert("출제된 과제가 없습니다.");
			return;
		}

	   	var sumWeight = 0;

		if(frm.p_weight.length != null && frm.p_weight.length > 1) {
			for( var i=0; i < frm.p_weight.length; i++) {
	        	var stepvalue = frm.p_weight[i].value;

	        	for( var j=0; j<stepvalue.length; j++) {
	          		var perchar = stepvalue.charAt(j);
					if(perchar!='0'&&perchar!='1'&&perchar!='2'&&perchar!='3'&&perchar!='4'&&perchar!='5'&&perchar!='6'&&perchar!='7'&&perchar!='8'&&perchar!='9') {
	            		alert("숫자만 입력 하세요"); //숫자만 입력 하세요.
	            		frm.p_weight[i].select();
						return;
					}
				}
			}

	   		for(i=0; i<frm.p_weight.length; i++) {
	        	if(frm.p_weight[i].value==0) {
	          		alert("어떤 가중치도 0%로 설정할 수 없습니다.\n\n다시 설정하여 주십시오."); //어떤 가중치도 0%로 설정할 수 없습니다.\n\n다시 설정하여 주십시오.
	          		frm.p_weight[i].select();
		    		return;
		    	}else {
	          		sumWeight = (sumWeight)*1 + (frm.p_weight[i].value)*1;
	        	}
	      	}

	    	if(sumWeight!=100) {
	        	alert("모든 가중치의 합은 100%이어야 합니다.\n\n다시 설정하여 주십시오.");//모든 가중치의 합은 100%이어야 합니다.\n\n다시 설정하여 주십시오.
	        	frm.p_weight[0].select();
	        	return;
	      	}
		}else {
	    	var stepvalue = frm.p_weight.value;

	      	for(var j=0; j<stepvalue.length; j++) {
	        	var perchar = stepvalue.charAt(j);

	        	if(perchar!='0'&&perchar!='1'&&perchar!='2'&&perchar!='3'&&perchar!='4'&&perchar!='5'&&perchar!='6'&&perchar!='7'&&perchar!='8'&&perchar!='9') {
	          		alert("숫자만 입력 하세요"); //숫자만 입력 하세요.
	          		frm.p_weight.select();
					return;
				}
			}

	    	if(frm.p_weight.value!=100) {
	        	alert("모든 가중치의 합은 100%이어야 합니다.\n\n다시 설정하여 주십시오.");//모든 가중치의 합은 100%이어야 합니다.\n\n다시 설정하여 주십시오.
	        	frm.p_weight.select();
	        	return;
	      	}
	    }
		frm.p_action.value = "go";
		frm.action = "/adm/rep/reportProfWeightUpdateData.do";
		frm.target = "_self";
		frm.submit();
	}
		
</script>