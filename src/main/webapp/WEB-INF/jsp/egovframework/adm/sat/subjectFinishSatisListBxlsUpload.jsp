<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ziaan.research.*" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<% 

String realFileName = "이수현황_" + session.getAttribute("ses_search_subjinfo"); 
//response.reset();
response.setHeader("Pragma", "no-cache;");
response.setHeader("Expires", "-1;");
response.setHeader("Content-Transfer-Encoding", "binary;");
response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");

System.out.println("realFileName["+realFileName+"]");

	DecimalFormat  df = new DecimalFormat("0.0");
	Map<String, Object> view = (Map<String, Object>)request.getAttribute("view");
	String year = view.get("edustart").toString().substring(0, 4);
	System.out.println(year);
%>

<html>
<head>
<title>과정별입금내역</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<style>
.xl26
	{mso-style-parent:style0;
	color:black;
	font-weight:700;
	text-align:center;
	border:.5pt solid black;
	background:#C2C2C2;
	mso-pattern:auto none;
	white-space:normal;}
.graph { /* 그래프 이미지 */
        height: 20px;
	    border-style: none;
	    background-color: orange;
	    background-repeat: repeat-x;
         }  
.tbtitlecenter{
	text-align:center;background-color: #f5f5f5;
	height:30px;
}
.tbcenter{
	text-align:center;
}
.tbleft{
	text-align:left;
}
         	
         	
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">
		
		<div id="tbl_userEduList_2" class="tbList"  style =  "width:auto; overflow:hidden; overflow-x:scroll; display:none;" >
		<!-- detail wrap -->
    <!-- div class="tbDetail">
        <table summary="" cellspacing="0"  width="1800" border=1 >
                <caption class="tbleft">이수현황</caption>
                <colgroup>
                	<col width="15%" />
                	<col width="*" />
            	</colgroup>
            <tbody>
                <tr>
                    <th class="tbtitlecenter">과정명</th>
                    <td style="height:30px;text-align:left; padding-left: 20px;" colspan="20">${view.subjnm} - ${view.subjseq}기</td>
                </tr>
                <tr class="title">
                    <th  class="tbtitlecenter" >연수기간</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="20"><c:out value="${fn2:getFormatDate(view.edustart, 'yyyy년 MM월 dd일')}"/>&nbsp;&nbsp;&nbsp;~&nbsp;&nbsp;&nbsp;<c:out value="${fn2:getFormatDate(view.eduend, 'MM월 dd일')}"/></td>
                </tr>
                <tr class="title">
                    <th  class="tbtitlecenter" >이수시간 및 학점</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="20">${view.edutimes}시간 (${view.point}학점)</td>
                </tr>
                <tr class="title">
                    <th  class="tbtitlecenter" >수강인원</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="20">${view.studentTotalCnt} 명</td>
                </tr>
                <tr >
                    <th class="tbtitlecenter" >이수인원</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="20"><c:out value="${view.isgraduatedY}"/> 명</td>
                </tr>
                <tr class="title">
                    <th  class="tbtitlecenter" >미이수인원</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="20"><c:out value="${view.isgraduatedN}"/> 명</td>
                </tr>
                <tr>
                    <th  class="tbtitlecenter" >납부구분</th>
                    <td align="center" style="padding:2px" colspan="20">                 
	                    <c:choose>                    	
	                    	<c:when test="${view.ischargeNm eq '특별'}">
	                    		<img src="http://iedu.nise.go.kr/images/user/sat/att1.png" border="0" width="179" height="22"> 
	                    	</c:when>
	                    	<c:when test="${view.ischargeNm eq '무료'}">
	                    		<img src="http://iedu.nise.go.kr/images/user/sat/att3.png" border="0" width="179" height="22"> 
	                    	</c:when>
	                    	<c:when test="${view.ischargeNm eq '일반'}">
	                    		<img src="http://iedu.nise.go.kr/images/user/sat/att2.png" border="0" width="179" height="22">
	                    		 
	                    	</c:when>
	                    </c:choose>
	                </td>                    
                </tr>        
                <tr class="title">
                    <th class="tbtitlecenter" >과정구분</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="20">
                    <c:choose>
                    		<c:when test= "${view.upperclassNm eq  '교원직무연수'}">
                    			직무
                    		</c:when>
                    		<c:when test="${view.upperclassNm eq  '보조인력연수'}">
                    			보조
                    		</c:when>
                    		<c:otherwise>
                    			${view.upperclassNm}
                    		</c:otherwise>
                    	</c:choose>
                    <c:out value=""/>     
                    </td>
                </tr>
            </tbody>
        </table>
    </div -->
    <!-- // detail wrap -->
			<table summary="" cellspacing="0" width="1800" border="1" style="margin-top: 10px">
                <caption></caption>
                <colgroup>
				</colgroup>
				<thead>
					<tr class="tbtitlecenter">
						<th scope="row">연번</th>
						<th scope="row">나이스 개인번호</th>
						<!-- <th scope="row">연수지명번호</th> -->
						<th scope="row">연수과정</th>
						<th scope="row">연수기관</th>
						<th scope="row">연수 시작일</th>
						<th scope="row">연수 종료일</th>
						<th scope="row">연수구분</th>
						<th scope="row">연수시간</th>
						<th scope="row">성적</th>
						<th scope="row">직무관련성</th>
						<th scope="row">평점학점</th>						
						<th scope="row">이수번호</th>
						<th scope="row">성명</th>
						<th scope="row">생년월일</th>
						<th scope="row">학교명</th>												
						<th scope="row">초/중등</th>
						<th scope="row">연수영역</th>
						<th scope="row">합격증번호</th>						
					</tr>
				</thead>
				<tbody>
				
<c:forEach items="${list_b}" var="result" varStatus="status" >
					<tr class="tbcenter">
						<td><c:out value="${status.count}"/></td>
						<td><c:out value="${result.nicePersonalNum}"/></td>
						<%--<td>
							<c:if test="${result.lecSelNo eq 'null'}"></c:if> 
							<c:if test="${result.lecSelNo ne 'null'}"><c:out value="${result.lecSelNo}"/></c:if>
						</td>	 --%>
						<td align="left"><c:out value="${view.subjnm}"/></td>
						<td align="left"><c:out value="${result.compnm}"/></td>
						<td><c:out value="${fn2:getFormatDate(view.edustart, 'yyyyMMdd')}"/></td>
						<td><c:out value="${fn2:getFormatDate(view.eduend, 'yyyyMMdd')}"/></td>
						<td>
							<c:out value="${result.upperclass}"/>
						</td>
						<td><c:out value="${view.edutimes}"/></td>
						<td>
							<c:if test="${view.edutimes >= 60}">
								<c:out value="${result.editscore}"/>
							</c:if>
							<c:if test="${view.edutimes < 60}">
								<c:out value="${result.score}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${result.upperclassnm eq  '교원직무연수'}">
							Y
							</c:if>
							<c:if test="${result.upperclassnm ne  '교원직무연수'}">
								 <c:choose>
								 	<c:when test="${subjEtcCount>=1}">
								 		Y
								 	</c:when>
								 	<c:otherwise>
								 		N
								 	</c:otherwise>
								 </c:choose>
							</c:if>	
						</td>
						<td>${view.point}</td>
						<td>
						<%/* %>
							<c:if test="${not empty result.serno}">
								<c:if test="${result.upperclassnm ne '일반연수'}">
									특수-${fn:replace(fn:replace(result.upperclassnm, '특별과정','교원'), '기타','')} 직무-${fn2:getFormatDate(view.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}
								</c:if>
								<c:if test="${result.upperclassnm eq '일반연수'}">
									특수-사이버-${fn2:getFormatDate(view.edustart, 'yyyy')}-${fn2:toNumber(view.subjseq)}-${result.serno}
								</c:if>
							</c:if>	
						<%*/ %>
						<%-- 
							<c:if test="${not empty result.serno}">
								<c:if test="${result.upperclassnm ne '교원직무연수'}">기타</c:if><c:if test="${result.upperclassnm eq '교원직무연수'}">직무</c:if>-${fn2:getFormatDate(view.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}
							</c:if>
							
							<c:if test="${empty result.serno}">
							-
							</c:if> --%>
							
							<c:if test="${result.isgraduated eq 'Y'}">${result.certnumber}</c:if>
							
						</td>
						<td><c:out value="${result.name}"/></td>
						<td><c:out value="${fn2:getFormatDate(result.birthDate, 'yyyyMMdd')}"/></td>
						<td>						
							<%-- ${result.empGubunNm} 교: ${result.userPath} 일: ${result.positionNm}<br/> --%>
							<c:if test="${result.empGubunNm eq '교원' || result.empGubunNm eq '보조인력'}">
								<c:if test="${fn:length(result.userPath) == 0}"> -</c:if>
						      <c:if test="${fn:length(result.userPath) > 0}"> <c:out value="${result.userPath}"/></c:if>
							</c:if>
							<c:if test="${result.empGubunNm ne '교원' &&  result.empGubunNm ne '보조인력'}">
								<c:if test="${fn:length(result.positionNm) == 0}"> -</c:if>
						      <c:if test="${fn:length(result.positionNm) > 0}"> <c:out value="${result.positionNm}"/></c:if>
							</c:if>
						</td>	
						<td><c:out value="${result.jobCd}"/></td>
						<td><c:out value="${result.pjobcd}"/></td>
						<td><c:out value="${result.certificateNo}"/></td>
					</tr>
</c:forEach>			

<c:if test="${empty list_b}">
					<tr>
						<td colspan="22">등록된 내용이 없습니다.</td>
					</tr>
</c:if>

				</tbody>
			</table>
    </div>

</body>
</html>