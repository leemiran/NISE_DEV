<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>


<script type="text/javascript"> 
//<![CDATA[
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
//]]>
</script>

<form name="<c:out value="${gsMainForm}" escapeXml="true" />" id="<c:out value="${gsMainForm}" escapeXml="true" />" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" name="p_userId" 	id="p_userId"  	value="<c:out value="${userid}" escapeXml="true" />"/>
	<input type="hidden" name="t_userIds" 	id="t_userIds" 	value=""/>
	<input type="hidden" name="p_create"	id="p_create" 		value="<c:out value="${p_create}" escapeXml="true" />">
		<div class="sub_text_2">
        	<h4>아이디 통합 내역 ( <c:out value="${name}" escapeXml="true" /> [ <c:out value="${userid}" escapeXml="true" /> ] )</h4>
      </ul>    
        </div>
        <!-- search wrap-->
                <div class="courseWrap">
                    <ul>
                        <li>구분
                            <select name="search_type" title="구분">
                            	<option value="id">아이디</option>
	                            <option value="name">이름</option>
                            </select>
                             <input name="search_text" type="text" size="20" value="<c:out value="${search_text}" escapeXml="true" />" onkeydown="fn_keyEvent('doSearchList')" title="검색어"/>                             
							 <a href="#none" onclick="doSearchList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
                         </li>
                    </ul>            
                </div>
                <!-- // search wrap --> 
                <%-- <div> <strong style="color: #0000ff;">선택한 아이디를 현재 로그인한 아이디(${userid})로 통합합니다.</strong><span id="dataCreate" style="color:#ff0000; margin-left: 50px;"></span></div> --%>
                <div> <strong style="color: #0000ff;">고객센터로 아이디 통합 요청 하여 주시기 바랍니다.</strong><span id="dataCreate" style="color:#ff0000; margin-left: 50px;"></span></div>
        <!-- list table-->
		<div class="studyList">
			<table summary="아이디, 이름, 핸드폰, 이메일, 생년월일로 구성됨" cellspacing="0" width="100%">
				<caption>아이디 통합 내역</caption>
                <colgroup>
					<col width="5%" />
					<col width="10%" />
                    <col width="20%" />
                    <col width="20%" />
                    <col width="30%" />
                    <col width="10%" />
					<!-- <col width="5%" />		 -->			
				</colgroup>
				<thead>
					<tr>
						<th scope="row">순번</th>
						<th scope="row">아이디</th>
                        <th scope="row">이름</th>
                        <th scope="row">핸드폰</th>
                        <th scope="row">이메일</th>
                        <th scope="row">생년월일</th>
						<!-- <th scope="row"><input type="checkbox" class="allchk" /></th>		 -->				
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${list}" var="result" varStatus="status">				
					<tr>
						<td>${result.rn}</td>
                        <td>${result.userid}</td>
                        <td >${result.name}</td>		
                        <td >${result.handphone}</td>		
                        <td >${result.email}</td>	
                        <td >${result.birthDate}</td>		
						<%-- <td ><input type="checkbox" name="p_interId" id="p_interId" class="chk"  value="${result.userid}"/></td> --%>						
					</tr>					
				</c:forEach>				
				<c:if test="${empty list}">
					<tr>
						<td colspan="6">조회된 내역이 없습니다.</td>
					</tr>
				</c:if>
				</tbody>
			</table>
		</div>
		<!-- list table-->
         <ul class="btnR">
         
        <!-- <li><a href="#" onclick="frmSubmit()"><img src="/images/user/btn_save.gif" alt="저장" /></a></li> -->
         
      </ul>      
</form>
        
<script type="text/javascript">
<!--

var frm = eval('document.<c:out value="${gsMainForm}" escapeXml="true" />');

$(function(){
	$(".allchk").change(function(){
		if($(this).is(":checked")){
			$(".chk").attr("checked", true);
		}else{
			$(".chk").attr("checked", false);
		}		
	});	
	if( frm.p_create.value == "C"){	
		$("#dataCreate").css("color","#00aa00");
		$("#dataCreate").text("아이디 통합 완료");
		setTimeout("resultTime()", 3000);
		frm.p_create.value="";
	}
});

function resultTime(){
	$("#dataCreate").css("color","#ff0000");
	$("#dataCreate").text("");
}

function doSearchList()
{	frm.p_create.value ="";
	frm.action = "/usr/mem/idIntergrationIdSearch.do";
	frm.target = "_self";
	frm.submit();
}


//아이디 통합
function frmSubmit() {
	var userids=[];
	$(".chk").each(function(){
		if($(this).is(":checked")){
			userids.push($(this).val());
		}
	});
	
	if (userids.length < 1) {
		alert("선택된 내역이 없습니다.");
		return;
	}
	if(confirm("아이디 통합 하시겠습니까?"))
	{
		setInterval(function(){ 
			if($("#dataCreate").text() == ""){
				$("#dataCreate").text("아이디 통합 중");
			}else{
				$("#dataCreate").text("");
			}	
			//timecnt = timecnt+1;
		}, 1000);
		frm.p_create.value="C";
		frm.action = "/usr/mem/idIntergrationIdAction.do";
		frm.target = "_self";
		frm.t_userIds.value = userids.join(',');
		frm.submit();
	}
}

//-->
</script>
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
