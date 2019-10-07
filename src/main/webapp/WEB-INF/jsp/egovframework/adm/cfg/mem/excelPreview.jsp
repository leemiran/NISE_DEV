<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:485px">
    	<div class="tit_bg">
			<h2>
				<c:out value="${p_type eq 'C' ? '회사선택회원등록' : '회원엑셀등록'}"/>
			</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" name="p_type" id="p_type" value="${p_type}">
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="150px" />
					<col width="" />
				</colgroup>
				<tbody>
				<c:if test="${p_type eq 'C'}">
					<tr>
						<th scope="col">회사</th>
						<td class="bold">
							<select>
							</select>
						</td>
					</tr>
				</c:if>
					<tr>
						<th scope="col">Excel File</th>
						<td class="bold"><input name="p_file" type="FILE" class="input" style="width:98%"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:insertExcel();"><span>저장</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="javascript:preview();"><span>미리보기</span></a></li>
		</ul>
		<!-- // button -->
		
		<table>
			<tr>
				<td style="padding-left:10px;color:#FF7F50"><Strong>( 주의사항 - 필독 )</Strong></td>
			</tr>
			<tr>
				<td style="padding-left:10px;color:#FF7F50">1. 엑셀파일 저장시 엑셀 형식(xls)으로 저장하여 사용할것.</td>
			</tr>
			<tr>
				<td style="padding-left:10px;color:#FF7F50">2. 엑셀파일 작성시 샘플 파일을 참고할 것.( <b><font color="black">샘플문서</font></b> )</td>
			</tr>
			<tr>
				<td style="padding-left:10px;color:#FF7F50">3. 회사선택등록, 전체회사등록을 구분해서 정확하게 사용할것.</td>
			</tr>
			<tr>
				<td style="padding-left:20px;color:#FF7F50">(<b>회사선택등록</b>시 엑셀파일에 회사구분코드를 입력하셔도 선택된 회사로 등록됩니다.)</td>
			</tr>
			<tr>
				<td style="padding-left:10px;color:#FF7F50">4. 회사코드는 다음 파일을 참고할 것( <b><font color="black">[회사코드]</font></b> )</td>
			</tr>
			<tr height="5px;"><td></td></tr>
			<tr>
				<td style="padding-left:10px;">
					<c:if test="${p_type eq 'C'}">
					<img src="/images/adm/sample/MemberExcel_compselect.gif" border="0" style="width:99%">
					</c:if>
					<c:if test="${p_type eq 'M'}">
					<img src="/images/adm/sample/MemberExcel.gif" border="0" style="width:99%">
					</c:if>
				</td>
			</tr>
		</table>
		
		
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">


	
</script>