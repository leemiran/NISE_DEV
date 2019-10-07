<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>


<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");opener.docuemnt.location.reload();window.close();</c:if>
-->
</script>



<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
<input type="hidden" value="${p_userid}" name="p_userid" id="p_userid">
<input type="hidden" value="${p_no}" name="p_no">
<input type="hidden" value="" name="p_process">


<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>상담입력/조회</h2>
         </div>
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="25%" />
					<col width="75%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">과정선택</th>
						<td>
						<select id="p_subj" name="p_subj"></select>
						</td>
					</tr>
					<tr>
						<th scope="col">분류</th>
						<td>
						<c:if test="${not empty p_no}">
							<ui:code id="p_mcode" selectItem="${view.mcode}" gubun="" codetype="0047"  upper="" levels="1" title="분류" className="" type="select" selectTitle="::선택::" event="" />
						</c:if>
						<c:if test="${empty p_no}">
							<ui:code id="p_mcode" selectItem="${p_subjyearsubjseq}" gubun="" codetype="0047"  upper="" levels="1" title="분류" className="" type="select" selectTitle="::선택::" event="" />
						</c:if>
						 - 
						<select name="p_gubun">
		                	<option value="in" <c:if test="${view.gubun eq 'in'}">selected</c:if>>수신</option>
		                	<option value="out" <c:if test="${view.gubun eq 'out'}">selected</c:if>>발신</option>
		                </select>
						</td>
					</tr>
					<tr>
						<th scope="col">내역</th>
						<td>
						<input name="p_title" type="text" size="80" value="<c:out value="${view.title}"/>">
						</td>
					</tr>
					<tr>
						<th scope="col">상담내용</th>
						<td>
						
						<textarea name="p_ctext" rows="10" cols="80"><c:out value="${view.ctext}"/></textarea>
						</td>
					</tr>
					<tr>
						<th scope="col">상태</th>
						<td>
							<select name="p_status">
			                <option value="1" <c:if test="${view.status eq '1'}">selected</c:if>>미처리</option>
			                <option value="2" <c:if test="${view.status eq '2'}">selected</c:if>>처리중</option>
			                <option value="3" <c:if test="${view.status eq '3'}">selected</c:if>>완료</option>
			              </select>
						</td>
					</tr>
					<tr>
						<th scope="col">원격지원</th>
						<td>
						<input type="checkbox" name="p_remote" value="Y" <c:if test="${view.remote eq 'Y'}">checked</c:if>> 
						
						</td>
					</tr>
					<tr>
						<th scope="col">상담일자</th>
						<td>
						
						
						<c:set var="p_sdate_view">${fn2:getFormatDateNow('yyyy.MM.dd')}</c:set>
						<c:if test="${not empty view.sdate}">
							<c:set var="p_sdate_view">${fn2:getFormatDate(view.sdate, 'yyyy.MM.dd')}</c:set>
						</c:if>
						
							<input name="p_sdate_view" type="text" size="10" maxlength="10" 
							OnClick="popUpCalendar(this, this, 'yyyy.mm.dd')" 
							readonly value="${p_sdate_view}"/>
							
              				<input name="p_sdate" type="hidden" value="<c:out value="${view.sdate}"/>"/>
              				
						</td>
					</tr>
					
					<tr>
						<th scope="col">상담소요시간</th>
						<td>
						<div id="time"></div>
						<input name="p_etime" type="hidden" size="10" value="${view.etime}">
						</td>
					</tr>
					<tr>
						<th scope="col">처리내용</th>
						<td>
						<textarea name="p_ftext" rows="10" cols="80"><c:out value="${view.ftext}"/></textarea>
						</td>
					</tr>
									
				</tbody>
			</table>
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
		
		<c:if test="${empty p_no}">
			<li><a href="#none" onclick="insert_check('insert')" class="pop_btn01"><span>저 장</span></a></li>
		</c:if>
		
		<c:if test="${not empty p_no}">
			<li><a href="#none" onclick="insert_check('update')" class="pop_btn01"><span>수 정</span></a></li>
			
			<li><a href="#none" onclick="insert_check('delete')" class="pop_btn01"><span>삭 제</span></a></li>
		</c:if>
		
		
			
			<li><a href="#none" onclick="window.close()" class="pop_btn01"><span>닫 기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>



<c:set var="startTimer" value="0"/>

<c:if test="${not empty view.etime}">
	<c:set var="startTimer" value="${view.etime}"/>
</c:if>


<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

	
	//시간을 설정 하세요

	var limit= '<c:out value="${startTimer}"/>' + ':01' ;
	
	var parselimit=limit.split(":");
	parselimit=parselimit[0]*60+parselimit[1]*1;
	
	function begintimer(){
	 	parselimit+=1;
		 curmin=Math.floor(parselimit/60);
		 cursec=parselimit%60;
		
		 if (curmin!=0){
		   curtime=curmin+" 분 "+cursec+" 초";
		 }
		 else{
		   curtime="0 분 "+cursec+" 초";
		 }
		
		time.innerText = curtime;
		
		thisForm.p_etime.value = curmin;
		setTimeout("begintimer()",1000);
	}


	$(document).ready(function () {

		//시간
		begintimer();
		//과정리스트 
		getUserSubjList();
	});

    // 체크 후 등록
    function insert_check(mode) {
        if (thisForm.p_subj.value == "") {
            alert("과정을 선택하세요");
            thisForm.p_subj.focus();
            return;
        }
        
        if (thisForm.p_mcode.value == "") {
            alert("분류를 선택하세요");
            thisForm.p_mcode.focus();
            return;
        }
        
        if (thisForm.p_title.value == "") {
            alert("내역을 입력하세요");
            thisForm.p_title.focus();
            return;
        }
        if (realsize(thisForm.p_title.value) > 200) {
            alert("내역은 한글기준 100자를 초과하지 못합니다.");
            thisForm.p_title.focus();
            return;
        }
        if (thisForm.p_ctext.value == "") {
            alert("상담내용을 입력하세요");
            thisForm.p_contents.focus();
            return;
        }
        if (realsize(thisForm.p_ctext.value) > 4000) {
            alert("상담내용은 한글기준 2000자를 초과하지 못합니다.");
            thisForm.p_ctext.focus();
            return;
        }
        if (realsize(thisForm.p_ftext.value) > 4000) {
            alert("처리내용은 한글기준 2000자를 초과하지 못합니다.");
            thisForm.p_ftext.focus();
            return;
        }
        
        //if (!number_chk_noalert(thisForm.p_etime.value)) {
        //  alert('상담시간이 잘못입력되었습니다.');
        //  thisForm.p_etime.focus();
        //  return;
        //}

        var p_sdate = make_date(thisForm.p_sdate_view.value);
        if (p_sdate == "") {
            alert("상담일자을 입력하세요");
            return;
        }
        thisForm.p_sdate.value = p_sdate;

        thisForm.action = "/adm/pop/searchCounselAction.do";
        thisForm.p_process.value = mode;
        thisForm.submit();
    }



    //사용자의 과정 리스트 가져오기
    function getUserSubjList() { 
   		$("#p_subj").html('');
   		$("#p_subj").append('<option value=\"\">== 과목을 선택하세요 ==</option>');

   		<c:if test="${not empty p_no}">
   		var p_subjseq_value = '<c:out value="${view.subj}"/>' + '/' + '<c:out value="${view.year}"/>' + '/' + '<c:out value="${view.subjseq}"/>';
		</c:if>
		<c:if test="${empty p_no}">
		var p_subjseq_value = '<c:out value="${p_subjyearsubjseq}"/>';
		</c:if>
   		
   		$.ajax({  
   			url: "<c:out value="${gsDomainContext}" />/com/aja/sch/selectUserSubjList.do",  
   			data: {searchUserid : function() {return $("#p_userid").val();}},
   			dataType: 'json',
   			contentType : "application/json:charset=utf-8",
   			success: function(data) {   
   				data = data.result;
   				for (var i = 0; i < data.length; i++) {
   					var subj = data[i].subj;
   					var subjnm = data[i].subjnm;
   					var subjseq = parseInt(data[i].subjseq);

   					if( p_subjseq_value == subj)
   						$("select#p_subj").append("<option value='" + subj + "' selected>" + subjnm + " - " + subjseq + "기</option>");
   					else
   						$("select#p_subj").append("<option value='" + subj + "'>" + subjnm + " - " + subjseq + "기</option>");
   				}

   				//기타 
   				var other = '<option value=\"0000/0000/0000\" ';
   				if(p_subjseq_value == '0000/0000/0000') other += ' selected ';
   				other += '>==== 기 타 ====';
   				other += '</option>';
   				
   				$("#p_subj").append(other);

   				
   			},    
   			error: function(xhr, status, error) {   
   				alert(status);   
   				alert(error);    
   			}   
   		});   
   	} 	
</script>