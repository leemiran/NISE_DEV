<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">

	<input type="hidden" name="p_subj"      value="${p_subj}">
	<input type="hidden" name="p_year"      value="${p_year}">
	<input type="hidden" name="p_subjseq"   value="${p_subjseq}"> 
	<input type="hidden" name="p_userid"   value="${p_userid}">
	<input type="hidden" name="p_name"   value="${p_name}"> 	
	
	
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>
			출석부 관리
			</h2>
		</div>
		
		
			<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="25%" />
					<col width="75%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">이&nbsp;&nbsp;&nbsp;름</th>
						<td>
						<c:out value="${p_name}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">아이디</th>
						<td>
						<c:out value="${p_userid}"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
			
    <!-- list table-->
		<div class="popList">
		
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="5%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="*"/>                                                                            			
		    </colgroup>
				<thead>
					<tr>
					  <th scope="row">NO</th>
					  <th scope="row">학습일자</th>
					  <th scope="row">출석여부</th>
					  <th scope="row">변경사유</th>
					</tr>
				</thead>
				<tbody>
<c:forEach items="${list}" var="result" varStatus="status" >				
					<tr>
						<td class="num"><c:out value="${status.count}"/></td>
						<td>
							<c:out value="${fn2:getFormatDate(result.dateSeq, 'yyyy.MM.dd')}"/>
							<input type="hidden" name="_Array_p_attdate" value="${result.dateSeq}">
              				<input type="hidden" name="_Array_p_attendyn_chk" value="${result.ist}">
						</td>
						<td>
						
						<select name="_Array_p_attendyn">
							<option value="O" <c:if test="${result.ist eq 'O'}">selected</c:if>>O</option>
							<option value="X" <c:if test="${result.ist ne 'O'}">selected</c:if>>X</option>
						</select>
						
						
						</td>
						<td><textarea name="_Array_p_reason" rows="3" cols="70" ><c:out value="${result.reason}"/></textarea></td>
			      </tr>
</c:forEach>
				</tbody>
			</table>
			</div>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="whenSave();"><span>저장</span></a></li>
			
			<li><a href="#" class="pop_btn01" onclick="window.close();"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>

</form>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>


<script type="text/javascript">

var frm = document.<c:out value="${gsPopForm}"/>;
function whenSave(){
	 if(confirm('수정하시겠습니까?')){
		frm.action='/adm/stu/personalAttendAction.do';
		frm.tartget = "_self";
       	frm.submit();
	 }
}
</script>