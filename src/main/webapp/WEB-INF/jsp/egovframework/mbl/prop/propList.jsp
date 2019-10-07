<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/userLoginCheck.jsp" %>

	<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>과정신청내역</a></span>
	</div>
	
	<!-- 본문 -->
	<div id="wcontainer">
		<!-- 교육과정 상세 -->		
		<!-- 탭 -->
		<ul class="tab mrt20" id="tableMenu1" style="display:block;">
			<li><a href="#none" onclick="displayTabMenu(1);return false;" class="on">수강신청중</a></li>
			<!-- <li><a href="#none" onclick="displayTabMenu(2);return false;">취소/반려</a></li> -->
		</ul>		
		<ul class="tab mrt20" id="tableMenu2" style="display:none;">
			<li><a href="#none" onclick="displayTabMenu(1);return false;">수강신청중</a></li>
			<li><a href="#none" onclick="displayTabMenu(2);return false;" class="on">취소/반려</a></li>
		</ul>		
		<div class="yun mrt15" id="tableDisPlayMenu1" style="display:block;">
			
			
			
<c:forEach items="${cancPosList}" var="result" varStatus="status">
			<div class="bview">
				<table summary="과정명,신청기간,연수기간,신청일,상태로 구분" cellspacing="0" width="100%">
                    <caption>연수상세</caption>
					<colgroup>
							<col width="22%" />
							<col width="" />
							<col width="22%" />
							<col width="30%" />
					</colgroup>
					<tr>
						<th scope="row">과정명</th>
						<td colspan="3">${result.subjnm}</td>
					</tr>
					<tr>
						<th scope="row">신청기간</th>
						<td colspan="3">${fn2:getFormatDate(result.propstart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.propend, 'yyyy.MM.dd')}</td>
					</tr>
					<tr>
						<th scope="row">연수기간</th>
						<td colspan="3">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
					</tr>
					<tr>
						<th scope="row">신청일</th>
						<td>${result.proposeDate}</td>
						<th scope="row">상태</th>
						<td>
									<c:if test="${result.chkfinal eq 'Y'}"><a class="btn_blu"><span>승인</span></a></c:if>
                                    <c:if test="${result.chkfinal eq 'N'}"><a class="btn_blu"><span>반려</span></a></c:if>
                                    <c:if test="${result.chkfinal eq 'B'}">
	                                   	<c:if test="${fn2:getFormatDate(result.edustart, 'yyyyMMdd') <= fn2:getFormatDateNow('yyyyMMdd')}">
	                                   		<!--  <a href="javascript:alert('학습이 시작되어  수강취소가 불가능합니다.')" class="btn_red"><span>수강취소</span></a> -->
	                                   	</c:if>
	                                    	
	                                   	<c:if test="${fn2:getFormatDate(result.edustart, 'yyyyMMdd') > fn2:getFormatDateNow('yyyyMMdd')}">
	                                   		<!--  <a href="javascript:CtlExecutor.requestAjaxSubj('${result.subj}','${result.year}','${result.subjseq}','${result.orderId}');" class="btn_red"><span>수강취소</span></a>-->
	                                   	</c:if>
                                    </c:if>
						</td>
					</tr>
				</table>
			</div>					
</c:forEach>

<c:forEach items="${couCancList}" var="result" varStatus="status">
			<div class="bview">
				<table summary="과정명,신청기간,연수기간,신청일,상태로 구분" cellspacing="0" width="100%">
					<caption>수강신청항목</caption>
                    <colgroup>
							<col width="22%" />
							<col width="" />
							<col width="22%" />
							<col width="30%" />
					</colgroup>
					<tr>
						<th scope="row">과정명</th>
						<td colspan="3">${result.subjnm}</td>
					</tr>
					<tr>
						<th scope="row">신청기간</th>
						<td colspan="3">${fn2:getFormatDate(result.propstart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.propend, 'yyyy.MM.dd')}</td>
					</tr>
					<tr>
						<th scope="row">연수기간</th>
						<td colspan="3">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
					</tr>
					<tr>
						<th scope="row">신청일</th>
						<td>${result.proposeDate}</td>
						<th scope="row">상태</th>
						<td>
									<c:if test="${result.chkfinal eq 'Y'}"><a class="btn_blu"><span>승인</span></a></c:if>
                                    <c:if test="${result.chkfinal eq 'N'}"><a class="btn_blu"><span>반려</span></a></c:if>
                                    <c:if test="${result.chkfinal eq 'B'}">
	                                   	<c:if test="${fn2:getFormatDate(result.edustart, 'yyyyMMdd') <= fn2:getFormatDateNow('yyyyMMdd')}">
	                                   		<a href="javascript:alert('학습이 시작되어  수강취소가 불가능합니다.')" class="btn_red"><span>수강취소</span></a>
	                                   	</c:if>
	                                    	
	                                   	<c:if test="${fn2:getFormatDate(result.edustart, 'yyyyMMdd') > fn2:getFormatDateNow('yyyyMMdd')}">
	                                   		<a href="javascript:CtlExecutor.requestAjaxSubj('${result.subj}','${result.year}','${result.subjseq}','${result.orderId}');" class="btn_red"><span>수강취소</span></a>
	                                   	</c:if>
                                    </c:if>
						</td>
					</tr>
				</table>
			</div>					
</c:forEach>

<c:if test="${empty couCancList && empty cancPosList}">
			<div class="bview">
				등록된 내용이 없습니다.
			</div>
</c:if>          
	</div>









	
	
	
<!--	취소/반려-->
	<div class="yun mrt15" id="tableDisPlayMenu2" style="display:none;">         
           
<c:forEach items="${cancelList}" var="result" varStatus="status">             
            <div class="bview">
				<table summary="과정명,신청기간,연수기간,신청일,상태로 구분" cellspacing="0" width="100%">
                        <caption>취소/반려</caption>
                        <colgroup>
                                <col width="22%" />
                                <col width="" />
                                <col width="20%" />
                                <col width="20%" />
                        </colgroup>
                       
                        <tr>
                            <th scope="row">과정명</th>
                            <td colspan="3">${result.subjnm}</td>
                        </tr>
                        <tr>
                            <th scope="row">취소/반려일</th>
                            <td>${fn2:getFormatDate(result.canceldate, 'yyyy.MM.dd')}</td>
                            <th scope="row">취소/반려</th>
                            <td>
                            ${result.cancelkind}
                            </td>
                        </tr>
                        
				</table>
			</div>
</c:forEach>		

<c:if test="${empty cancelList}">	
            <div class="bview">
				<table summary="과정명,신청기간,연수기간,신청일,상태로 구분" cellspacing="0" width="100%">
					<colgroup>
							<col width="" />
					</colgroup>
					<tr>
						<td align="center">등록된 내용이 없습니다.</td>
					</tr>
				</table>
			</div>
</c:if>
			
		</div>
		
		<!-- //교육과정 상세 -->
		
		
		
		
		
		
		<!-- 더보기 -->
		<div class="more"></div>

		<!-- 맨위로 -->
		<div class="totop"><a href="#wrap">맨위로</a></div>
		
	</div>
	<!-- //본문 -->

<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--
//tab menu
function displayTabMenu(num)
{
	for(var i=1; i<=2; i++)
	{
		if(num == i)
		{
			document.all("tableMenu" + i).style.display = "block";
			document.all("tableDisPlayMenu" + i).style.display = "block";
		}
		else
		{
			document.all("tableMenu" + i).style.display = "none";
			document.all("tableDisPlayMenu" + i).style.display = "none";
		}
	}	
}





var CtlExecutor = {
       
requestAjaxSubj : function (p_subj, p_year, p_subjseq, p_order_id) {

	if(confirm("신청하신 과정을 취소하시겠습니까?"))
	{
			 //alert($('#firstIndex').val());
			 var param = "p_subj=" + p_subj
			 + "&p_year=" + p_year
			 + "&p_subjseq=" + p_subjseq
			 + "&p_order_id=" + p_order_id
			 + "&p_reasoncd=99"			//취소사유코드 : 기타(99)
			 + "&p_reason=모바일본인취소"		
			 ;
			//alert(param);
			 
			 $.ajax({
			  type:"post",
			  url : "/mbl/prop/propListAction.do",
			  data: param,
			  success:function(data){
				result = data.result;
				if(result == true)
				{
					alert("정상적으로 취소되었습니다.");
					document.location.replace('/mbl/prop/propList.do');
				}
				else
				{
					alert("수강취소중 오류가 발생하였습니다.");
				}
			  },
			  error:function(e){
			   alert(e);
			  }
			 });
			}
	}
}  
//-->
</script>

</div>
</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>
