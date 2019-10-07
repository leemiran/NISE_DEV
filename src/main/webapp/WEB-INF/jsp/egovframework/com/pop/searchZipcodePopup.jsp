<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<!-- popup wrapper 팝업사이즈width=650,height=500-->


<div id="popwrapper">
	<div class="popIn" style="height:550px">
		 <!-- tab -->
        <div class="conwrap2">
            <ul class="mtab2">
                <li><a href="javascript:go_searchTab('')" <c:if test="${p_gubun ne 'road'}">class="on"</c:if>>지번이름으로 우편번호찾기</a></li>
                <li><a href="javascript:go_searchTab('road')" <c:if test="${p_gubun eq 'road'}">class="on"</c:if>>도로명으로 우편번호찾기</a></li>
         	</ul>
        </div>
        
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" action="/com/pop/searchZipcodePopup.do">
		<fieldset>
        <legend>우편번호 검색</legend>
        <input type="hidden" name="action" id="action"/>
		<input type="hidden" name="p_gubun" id="p_gubun" value="${p_gubun}"/>
		<div class="in" style="width:100%;">		
			<c:if test="${p_gubun ne 'road'}">
				<strong class="shTit"><label for="search_dong">※ 동(읍/리/면) 입력하신 후 검색을 누르세요</label> : </strong>
				<input type="text" name="search_dong" id="search_dong" value="${search_dong}" size="20" style="ime-mode:active" onkeyup="fn_keyEvent2('1')" />				
				<a href="#" onclick="searchList(1)" class="btn_search"><span>검 색</span></a>		
			</c:if>
			
			<c:if test="${p_gubun eq 'road'}">
			<!-- <strong><label for="search_dong">※ 시/도 + 시/군/구 + 도로명을 입력하신 후 검색하세요</label> : </strong> -->
			
				<div style="padding-top:3px;">시/도 : 
					<select name="sido" id="sido" onChange="searchGugun()">
						<option value="">시/도를 선택하세요</option>
						<c:forEach items="${sidoList}" var="result">
							<option value="${result.sido}" <c:if test="${result.sido eq sido}">selected</c:if>>${result.sido}</option>						
						</c:forEach>
					</select>
					
					시/군/구 : 
					<select name="gugun" id="gugun">
						<option value="">시/군/구를 선택하세요</option>			
												
						
					</select>
					
				</div>
				<div style="padding-top:3px;padding-bottom:3px;">
					도로명 <input type="text" name="search_dong" id="search_dong" value="${search_dong}" size="20" style="ime-mode:active" onkeyup="fn_keyEvent2('2')" />
					건물번호<input type="text" name="build_mst_no" id="build_mst_no" value="${build_mst_no}" size="20" style="ime-mode:active"  onkeyup="fn_keyEvent2('2')" />
					<a href="#" onclick="searchList(2)" class="btn_search"><span>검 색</span></a>
				</div>
			</c:if>			
			
			
		</div>
        </fieldset>
		</form>		
		<p style="height:3px;"/>
		<!-- contents -->
		<div class="tbList">
			<table summary="우편번호, 주소로 구분" cellspacing="0" width="100%">
                <caption>
                우편번호
                </caption>
                <colgroup>
					<col width="80px" />
					<col />
				</colgroup>
				<tr>
					<th scope="row">우편번호</th>
					<th scope="row">주소</th>
				</tr>
			</table>
			<div style="overflow-y:scroll; height:375px;">
				<table summary="우편번호, 주소로 구분" cellspacing="0" width="100%">
                <caption>우편번호</caption>
                <colgroup>
					<col width="80px" />
					<col/>
				</colgroup>
				<tbody>
				<c:forEach items="${list}" var="result">
				
					<c:if test="${p_gubun ne 'road'}">
						<tr style="cursor:pointer" onmouseover="overRow(this);" onclick="returnZipCode1('${result.post1}', '${result.post2}', '${result.sido}', '${result.gugun}', '${result.dong}')">
							<td><c:out value="${result.zipcode}"/></td>
							<td class="left"><a href="#">
								<c:out value="${result.sido}"/>
								<c:out value="${result.gugun}"/>
								<c:out value="${result.dong}"/>
								<c:out value="${result.bunji}"/>
								</a>
							</td>
						</tr>
					</c:if>
					<c:if test="${p_gubun eq 'road'}">
						<tr style="cursor:pointer" onmouseover="overRow(this);" onclick="returnZipCode2('${result.post1}', '${result.post2}', '${result.sido}', '${result.gugun}', '${result.upmyun}', '${result.roadNm}', '${result.buildNo}')">
							<td><c:out value="${result.zipCd}"/></td>
							<td class="left"><a href="#">
								<c:out value="${result.sido}"/>
								<c:out value="${result.gugun}"/>
								<c:out value="${result.upmyun}"/>
								<c:out value="${result.roadNm}"/>
								<c:out value="${result.buildNo}"/>
								</a>
							</td>
						</tr>
					</c:if>				
					
				</c:forEach>	
                			
				<c:if test="${empty list}">
					<tr>
						<td colspan="2" width="100%">검색된 내용이 없습니다.</td>
					</tr>
				</c:if>			
					
				</tbody>
			</table>
			</div>
		</div>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫 기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<script type="text/javascript">
//<![CDATA[
	var frm = document.<c:out value="${gsPopForm}"/>;

	function go_searchTab(p_gubun)
	{
		frm.action.value = "";
		frm.search_dong.value = "";
		frm.p_gubun.value = p_gubun;
		//frm.action = "/com/pop/searchZipcodePopup.do"
		frm.target = "_self";
		frm.submit();
	}
	
	function searchList(_num){
		
		if(_num == 1){
			if (frm.search_dong.value == "") {
	            alert("동(읍/리/면)을 입력하세요");
	            return;
	        }
		}
		
		if(_num == 2){

			if (frm.sido.value == "") {
	            alert("시/도를 선택하세요");
	            return;
	        }
			if(frm.sido.value != "세종특별자치시"){
				if (frm.gugun.value == "") {
		            alert("시/군/구를 선택하세요");
		            return;
		        }
			}
			if (frm.search_dong.value == "") {
	            alert("도로명을 입력하세요");
	            return;
	        }
			/* if (frm.build_mst_no.value == "") {
	            alert("건물번호를 입력해주세요");
	            return;
	        } */
		}
		
		frm.action.value = "go";
		frm.submit();
	}

	function returnZipCode1(post1, post2, sido, gugun, dong){
		var arr = new Array();
		arr[0] = post1;
		arr[1] = post2;
		arr[2] = sido +" "+ gugun +" "+ dong;
		opener.receiveZipcode(arr);
        self.close();
	}
	
	function returnZipCode2(post1, post2, sido, gugun, upmyun, roadNm, buildNo){
		var arr = new Array();
		arr[0] = post1;
		arr[1] = post2;
		var addr = "";
		if(upmyun!=""){
			addr += " "+ upmyun
		}
		if(roadNm!=""){
			addr += " "+ roadNm
		}
		if(buildNo!=""){
			addr += " "+ buildNo
		}
		
		arr[2] = sido +" "+ gugun +addr;
		opener.receiveZipcode(arr);
        self.close();
	}


	var tmpRow;
	function overRow(obj){
		if( tmpRow ){
			tmpRow.style.backgroundColor = "#FFFFFF";
		}
		tmpRow = obj;
		obj.style.backgroundColor = "#DCDCDC";
	}
	
	
	// gugun 검색
	function searchGugun(){
		
		
		
		if($.trim($('#sido').val()) == ""){
			alert("시도를 선택하세요.");
			$('#sido').focus();
			return;
		}
		

			
		$("#gugun").html('');
		$("#gugun").append('<option value=\"\">::선택::</option>');
		var sido = $("#sido").val();	
		
		if(sido != "세종특별자치시"){
		
			//로컬 한글 깨짐
			//sido = escape(encodeURIComponent(sido));
	
			$.ajax({  
				//url: "<c:out value="${gsDomainContext}" />/com/pop/searchGugun.do?sido="+sido, //로컬 한글깨짐		
				url: "<c:out value="${gsDomainContext}" />/com/pop/searchGugun.do",	//서버				
				method: 'post',
				data: {sido:sido},		//로컬일 때 주석
				dataType: 'json',
				contentType : "application/json:charset=utf-8",			 
				success: function(data) {   
					data = data.result;
					for (var i = 0; i < data.length; i++) {
						var value = data[i].gugun;
		
						if('<c:out value="${gugun}"/>' == value)
							$("select#gugun").append("<option value='"+value+"' selected>"+value+"</option>");
						else
							$("select#gugun").append("<option value='"+value+"'>"+value+"</option>");
					}
				},    
				error: function(xhr, status, error) {   
					alert(status);   
					alert(error);    
				}   
			});  
		}
	
		
	}
	//엔터입력시 
	function fn_keyEvent2(_num){	
		 if(event.keyCode == 13){			 
			 searchList(_num);
		 }
			 
	}
	<c:if test="${sido!=null && sido!='' }">
		searchGugun();
	</c:if>
	
	
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>