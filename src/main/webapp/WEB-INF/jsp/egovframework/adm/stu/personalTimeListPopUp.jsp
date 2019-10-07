<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>


<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>
			수강생 진도율 관리
			</h2>
		</div>
		
		
		
    <!-- list table-->
		<div class="popList">
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">

			<input type="hidden" name="p_subj"      value="${p_subj}">
			<input type="hidden" name="p_year"      value="${p_year}">
			<input type="hidden" name="p_subjseq"   value="${p_subjseq}"> 
			<input type="hidden" name="p_userid"   value="${p_userid}">
			<input type="hidden" name="p_checkHidden" id="p_checkHidden" value="">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="4%"/>
                    <col width="10%"/>
                    <col width="%"/>
                    <col width="%"/> 
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/> 
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>                                                                            			
                    <col width="%"/>
                    <col width="%"/>
		    </colgroup>
				<thead>
					<tr>
					  <th scope="row" rowspan="2">차시</th>
					  <th scope="row" rowspan="2">차시명</th>
					  <th scope="row" rowspan="2">표준</br> 학습시간</th>					  
					  <th scope="row" rowspan="2">총</br>학습시간</th>
					  <th scope="row" colspan="3">웹</th>
					  <th scope="row" colspan="3">모바일</th>
					  <th scope="row" rowspan="2">수강완료 <input type="checkbox" name="checkedAlls" onclick="checkAll(this.checked)"></th>
					  <th scope="row" rowspan="2">수강여부</th>
					</tr>
					<tr>
					  <th scope="row">학습시작시간</th>
					  <th scope="row">학습종료</th>
					  <th scope="row">학습시간</th>					  
					  <th scope="row">학습시간</th>
					  <th scope="row">학습종료</th>
					  <th scope="row">학습시간</th>
					</tr>
				</thead>
				<tbody>
				<c:set var="oldModule" value=""/>
				<c:set var="depth" value="1"/>
				<c:forEach var="result" items="${list}" varStatus="status">
				<c:if test="${result.module != oldModule}">
				<tr>
						<td class="num">${result.module }</td>
						<td style="text-align:left;padding-left:4px;"><c:out value="${result.moduleNm}"/><!-- <br> -<c:out value="${result.lesson}"/>. <c:out value="${result.lessonNm}" />--></td>
						<td class="num">${result.lessonTime }</td>
						<td class="num"><c:choose>
								<c:when test="${result.finalStatus eq 'Y' and result.studyTotalTime eq 0}">
								${result.lessonTime }
								</c:when>
								<c:otherwise>${result.hrMinuteSecT }</c:otherwise>
							</c:choose></td>
						<td>
							<c:out value="${fn2:getFormatDate(result.firstTime, 'yyyy/MM/dd HH:mm')}"/>
						</td>
						<td>
						<c:out value="${fn2:getFormatDate(result.lastTime, 'yyyy/MM/dd HH:mm')}"/>
						</td>
						<td>
							<c:choose>
								<c:when test="${result.finalStatus eq 'Y' and result.studyTotalTime eq 0}">
								${result.lessonTime }
								</c:when>
								<c:otherwise>${result.hrMinuteSec }</c:otherwise>
							</c:choose>
						</td>
						<td>
						<c:choose>
								<c:when test="${result.firstTimeM eq null and result.lastTimeM > 0}">
								<c:out value="${fn2:getFormatDate(result.firstTime, 'yyyy/MM/dd HH:mm')}"/>
								</c:when>
								<c:otherwise><c:out value="${fn2:getFormatDate(result.firstTimeM, 'yyyy/MM/dd HH:mm')}"/></c:otherwise>
							</c:choose>						
						</td>
						<td><c:out value="${fn2:getFormatDate(result.lastTimeM, 'yyyy/MM/dd HH:mm')}"/></td>
						<td><c:choose>
								<c:when test="${result.finalStatus eq 'Y' and result.studyTotalTime eq 0}">
								${result.lessonTime }
								</c:when>
								<c:otherwise>${result.hrMinuteSecM }</c:otherwise>
							</c:choose></td>
						<td>
						<c:if test="${ result.studyCount eq '0'}">
							<c:set var="check_status" value="A"/>
						</c:if>
						<c:if test="${ result.studyCount ne '0' and result.finalStatus eq 'N' }">
							<c:set var="check_status" value="N"/>
						</c:if>
						<c:if test="${  result.studyCount ne '0' and result.finalStatus eq 'Y' }">
							<c:set var="check_status" value="Y"/>
						</c:if>
					
							<input type="checkbox" name="p_check" id="p_check" <c:if test="${ result.totalLessonCount == result.studyEndCount }">checked</c:if> value="${result.module },${check_status}" onclick="changeCheckValue()">
							
						</td>
						<td>
						<!-- 	<c:if test="${ result.studyCount eq '0'}">
								<font color="red">
								학습전</font>
							</c:if>
							<c:if test="${ result.totalLessonCount != result.studyEndCount and result.studyCount ne '0' }">
								<font color="green">
								학습중</font>
							</c:if>
							<c:if test="${ result.totalLessonCount == result.studyEndCount }">
								<font color="blue">학습완료</font>
							</c:if> -->
							<c:choose>
								<c:when test="${result.finalStatus eq 'Y'}">
									<font color="blue">학습완료</font>
								</c:when>
								<c:when test="${result.finalStatus eq 'N'}">
									<font color="green">학습중</font>
								</c:when>
								<c:otherwise>
									<font color="red">학습전</font>
								</c:otherwise>
							</c:choose>
							<!--<c:if test="${result.lessonstatus eq 'Y'}">
							<input type="hidden" name="status_${result.lesson}" value="Y"/>
								<font color="blue">학습완료</font>
							</c:if>
							<c:if test="${result.lessonstatus ne 'Y'}">
								<c:if test="${empty result.firstEdu}"><font color="red">
								<input type="hidden" name="status_${result.lesson}" value="A"/>
								학습전</font>
								</c:if>
								<c:if test="${not empty result.firstEdu}"><font color="green">
								<input type="hidden" name="status_${result.lesson}" value="N"/>
								학습중</font>
								</c:if>
							</c:if>
						--></td>
			      </tr>
			      <c:set var="oldModule" value="${result.module}"/>
			      </c:if>
			      </c:forEach>
			      
<!--<c:forEach items="${list1}" var="result" varStatus="status" >				
					<tr>
						<td class="num"><c:out value="${status.count}"/></td>
						<td><c:out value="${fn2:getFormatDate(result.ldate, 'yyyy.MM.dd')}"/></td>
						<td><c:out value="${fn2:getFormatDate(result.ldate, 'HH:mm:ss')}"/></td>
						<td><c:out value="${result.lgip}"/></td>
			      </tr>
</c:forEach>-->
				</tbody>
			</table>
			</form>
			</div>
		<!-- // contents -->
		
		
		
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="goWhenAction();"><span>수강완료처리</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close();"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var frm = document.<c:out value="${gsPopForm}"/>;

$(window).bind('unload', function(){
	opener.doPageList('1');
	
});
 
function goWhenAction(){
	checkboxToHidden(frm, frm.p_check);
//	alert(frm.p_check_dummy.value);
	//return;
	if(confirm("체크된 항목은 수료완료처리 되며, 체크가 안된 항목들은 초기화가 됩니다. 계속 하시겠습니까?")){
		frm.action = "/adm/stu/personalTimeAction.do";
		frm.submit();
	}
}

function checkAll(val){
	var obj1 = document.getElementsByName("p_check");
	if(val==true){
		if(obj1.length > 1){	
			for(i=0;i < obj1.length;i++){
				obj1[i].checked = true;
			}
		}else{
			obj1[0].checked = true;
		}
	}else{
		if(obj1.length > 1){
			for(i=0;i < obj1.length;i++){
				obj1[i].checked = false;
			}
		}else{
			obj1[0].checked = false;
		}
	}
}

function checkboxToHidden(f, ele){
	var ele_h;
	var val;
	var hiddenVal = f.p_checkHidden;
	var aa = "";
	//alert(ele.length);
	if(typeof(ele.length) != "undefined"){ //배열일경우
		for(var i=0; i < ele.length; i++){
			//hidden 객체 생성, 이름은 checkbox와 같게한다.
			ele_h = document.createElement("input");
			ele_h.setAttribute("type", "hidden");
			ele_h.setAttribute("name", ele[i].name+"_real");
			ele[i].checked ? val = ele[i].value+",Y" : val = ele[i].value+",N";
		//	ele[i].checked ? hiddenVal[i].value = 'Y' : hiddenVal[i].value = 'N';
			ele_h.setAttribute("value", val);
			f.appendChild(ele_h);
		//	console.log(ele_h);
//alert(i);
//alert(ele.length);
			//기존 checkbox의 이름을 이름_dummy로 변경한후 checked = false해준다.
			ele[i].checked= false;
			ele[i].setAttribute("name", ele[i].name + "_dummy");
			//ele[i].setAttribute("value", val);
			
			aa = aa+ele_h.value + "!";
		//	console.log(ele[i]);
		}
		//frm.p_checkHidden.value = aa;
		//alert(frm.p_checkHidden.value);
	}else{ //checkbox가 한개일경우
		ele_h = document.createElement("input");
		ele_h.setAttribute("type", "hidden");
		ele_h.setAttribute("name", ele.name+"_real");
		ele.checked ? val = ele.value+",Y" : val = ele.value+",N";
		ele_h.setAttribute("value", val);
		f.appendChild(ele_h);
	
		//기존 checkbox의 이름을 이름_dummy로 변경한후 checked = false해준다.
		ele.checked= false;
		ele.setAttribute("name", ele.name + "_dummy");
	}
}

function changeCheckValue(){
	//if($('input[name=p_check]').is(":checked")){
	//	$('input[name=p_checkHidden]').val("Y");
	//}else{
	//	$('input[name=p_checkHidden]').val("N");
	//}
	//alert($('input[name=p_checkHidden]').val());
}
</script>