<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ziaan.research.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<% 

String realFileName = "결과보고서_" + session.getAttribute("ses_search_subjinfo"); 
//response.reset();
response.setHeader("Pragma", "no-cache;");
response.setHeader("Expires", "-1;");
response.setHeader("Content-Transfer-Encoding", "binary;");
response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");

System.out.println("realFileName["+realFileName+"]");


	DecimalFormat  df = new DecimalFormat("0.00");

	
%>

<html>
<head>
<title>결과보고서</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<style>
body { margin-left : 0; margin-top : 0; margin-right : 0; margin-bottom : 0;}
.xl26
	{
	/* mso-style-parent:style0; */
	font-size : 9pt;
	color:black;
	/* font-weight:700; */
	text-align:center;
	/*border:.5pt solid black;
	background:#C2C2C2;*/
	/* mso-pattern:auto none; */
	/* white-space:normal; */	
	padding:0px 0px 0px 0px;
	margin-left : 0; margin-top : 0; margin-right : 0; margin-bottom : 0;
	celspacing : 0;
	celpadding : 0;
	}
.xl26 td{width:25px}	
.graph { /* 그래프 이미지 */
        height: 20px;
	    border-style: none;
	    background-color: orange;
	    background-repeat: repeat-x;
         }  	
.tbcenter{
	text-align:center;
}         
</style>


</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">

<!----------------- 결과보고서(이수현황) A  START---------------------------------------------------------------------------------------->
		<div id="tbl_userEduList_1" >
		<!-- detail wrap -->
        <table summary="" cellspacing="0" width="100%" border=1 class="xl26" >
                <caption>결과보고서</caption>
                <colgroup>
                <col />
                <col />
                <col />
                <col  />
                <col />
                <col />
                <col />
                <col />
            </colgroup>
            <tbody>
                <tr class="tbcenter">
                 	<td >연수구분</td><!-- 과정분류(0001) -->
                    <td>
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
                    <td >운영형태</td><!-- 수강료 납부방식 -->
                    <td><c:out value="${view.ischargeNm}"/></td>
                    <td colspan="16"><h3><c:out value="${view.subjnm}"/> - <c:out value="${view.subjseq}"/>기</h3></td>
                </tr>
                <tr >
                	<td colspan="20" style="text-align:right; padding-right: 20px;">담당자 : <c:out value="${view.musernm}"/></td>
                </tr>
                <tr class="tbcenter">
                    <td colspan="4">과 정 명</td>
                    <td colspan="16"><c:out value="${view.subjnm}"/></td>
                </tr>
                <tr class="tbcenter">
                    <td colspan="4">기     간</td>
                    <td colspan="16"><c:out value="${fn2:getFormatDate(view.edustart, 'yyyy년 MM월 dd일')}"/>&nbsp;&nbsp;&nbsp;~&nbsp;&nbsp;&nbsp;<c:out value="${fn2:getFormatDate(view.eduend, 'yyyy년 MM월 dd일')}"/></td>
                </tr>
                <tr class="tbcenter">
                    <td colspan="4">수강인원</td>
                    <td colspan="16"><c:out value="${view.studentTotalCnt}"/> 명</td>
                </tr>
 <!-- 이수현황 START -->   
 <c:set var="payCnt" value="0" />   
<c:set var="chkfinalCnt" value="0" />   
<c:set var="biyong" value="0" />    
<c:set var="p_isgraduatedN" value="0" />     
<c:set var="fe_type_cnt" value="0" />            
 <!-- 이수현황 START -->   
 <c:forEach items="${list_a}" var="result" varStatus="status"> 
          <c:if test="${result.pay eq '무료' }">
          		<c:set var="fe_type_cnt" value="${result.chkfinalCnt}" /> 
          </c:if>
          
                <tr  class="tbcenter"  <c:if test="${result.subjseqLag ne result.subjseq}">style="border-top: 2px solid #bcbcbc"</c:if>>
                 <c:if test="${result.subjseqLag ne result.subjseq}">
                	<c:if test="${result.allSubjseqCnt == 1}">
                		<c:if test="${result.payGubun eq '개인'}">
                			<td  rowspan="${(result.allSubjseqCnt*2)}">이수현황</td>
                		</c:if>
                		<c:if test="${result.payGubun ne '개인'}">
                			<td  rowspan="${(result.allSubjseqCnt*2)+2}">이수현황 </td>
                		</c:if>
                		
                	</c:if>
                	<c:if test="${result.allSubjseqCnt > 1}">
                		<td  rowspan="${(result.allSubjseqCnt*2)+4}">이수현황</td>
                	</c:if>	
                
                </c:if>
                 <c:if test="${result.subjseqLag ne result.subjseq}">
                        <c:if test="${result.allSubjseqCnt == 1}">
                 		<c:if test="${result.payGubun eq '개인'}">
                 			<td rowspan="${result.allSubjseqCnt}" colspan="3">이수인원 </td>
                 		</c:if>
		         		<c:if test="${result.payGubun ne '개인'}">
                 			<td rowspan="${result.allSubjseqCnt+1}" colspan="3">이수인원 </td>
                 		</c:if>
		         	</c:if>	 
		         	<c:if test="${result.allSubjseqCnt > 1}">
		         		<td rowspan="${result.allSubjseqCnt+2}" colspan="3">이수인원</td>
		         	</c:if>
                	<!-- 
                	<c:if test="${result.allSubjseqCnt ne '1'}">
		      		</c:if>
                	 -->		      		
		      	 </c:if>		      	 
		      	 <c:if test="${result.deptGubunLag ne result.payGubun}">
			      	<c:if test="${result.gubunCnt eq '1'}">
			      		<c:if test="${result.payGubun eq '개인'}">
			         		<td colspan="8" >${result.payGubun}</td>
			         	</c:if>	
			         	<c:if test="${result.payGubun ne '개인'}"> 
			         	     <c:if test="${result.gubunCnt == 1 }">
			         		 	<td colspan="8" rowspan="2">${result.payGubun}</td>
			         		 </c:if>
			         		 <c:if test="${result.gubunCnt > 1 }">
			         		 	<td colspan="8"  rowspan="${result.gubunCnt+1}" >${result.payGubun}</td>
			         		 </c:if>	
                                             	

			         	</c:if>
			      	</c:if>
			      	<c:if test="${result.gubunCnt ne '1'}">
			         <td rowspan="${result.gubunCnt+1}"  colspan="8">${result.payGubun}</td>	
			      	</c:if>
		        </c:if>
                    <c:if test="${result.payGubun ne '개인'}">
			         <td  colspan="4">${result.deptNm}</td>	
			       </c:if>

			       <c:if test="${result.payGubun eq '개인'}">
                   	<td colspan="8">${result.isgraduatedYDeptcnt} 명 </td>
                   	<c:set var="p_isgraduatedYDeptcnt" value="${result.isgraduatedYDeptcnt}" />
                   </c:if>
                   <c:if test="${result.payGubun ne '개인'}">
                   	<td colspan="4">${result.isgraduatedYDeptcnt} 명 </td>     
                   </c:if>
                </tr>
                <c:if test="${result.deptGubunLead ne result.payGubun && result.payGubun ne '개인'}">
	                <tr  class="tbcenter">
	                 	<td  colspan="4" style="background-color: #f5f5f5;">계</td>
	                 	<td  colspan="4" style="background-color: #f5f5f5;">${result.isgraduatedYGubun} 명</td>
	                </tr>
	                <tr  class="tbcenter">
	                 	<td  colspan="8" style="background-color: #f5f5f5;">총 이수인원</td>
	                 	<td  colspan="8" style="background-color: #f5f5f5;">${result.isgraduatedYGubun+p_isgraduatedYDeptcnt} 명</td>	
	                </tr>
                </c:if>
</c:forEach>   
<c:forEach items="${list_a}" var="result" varStatus="status">            
                <tr  class="tbcenter" >
                 <c:if test="${result.subjseqLag ne result.subjseq}">
		         		<c:if test="${result.allSubjseqCnt == 1}">

                                          <c:if test="${result.payGubun eq '개인'}">
                 			<td rowspan="${result.allSubjseqCnt}" colspan="3">미이수인원</td>
                 		</c:if>
		         		<c:if test="${result.payGubun ne '개인'}">
                 			<td rowspan="${result.allSubjseqCnt+2}" colspan="3">미이수인원</td>
                 		</c:if>
		         		

		         	</c:if>
		         	<c:if test="${result.allSubjseqCnt > 1}">
		         		<td rowspan="${result.allSubjseqCnt+2}" colspan="3">미이수인원</td>
		         	</c:if>	
		      	 </c:if>	      	 
		      	 <c:if test="${result.deptGubunLag ne result.payGubun}">
			      	<c:if test="${result.gubunCnt eq '1'}">
			      		 <c:if test="${result.payGubun eq '개인'}">
				         	<td colspan="8">${result.payGubun}</td>	
				         </c:if> 
				          <c:if test="${result.payGubun ne '개인'}">
				         	<td colspan="8" rowspan="${result.gubunCnt+1}">${result.payGubun}</td>		
				         </c:if> 
			      	</c:if>
			      	<c:if test="${result.gubunCnt ne '1'}">
			         <td rowspan="${result.gubunCnt+1}"  colspan="8">${result.payGubun}</td>	
			      	</c:if>
		        </c:if>
			       <c:if test="${result.payGubun ne '개인'}">
			         <td  colspan='4'>${result.deptNm}</td>	
			       </c:if>
			       	<c:if test="${result.payGubun eq '개인'}">	       
                    	<td colspan='8'>${result.isgraduatedNDeptcnt} 명</td>
                    	<c:if test="${result.payGubun eq '개인' && p_isgraduatedN == 0}"> 
                 			<c:set var="p_isgraduatedNDeptcnt" value="${result.isgraduatedNDeptcnt}" />
                 			<c:set var="p_isgraduatedN" value="1" />
                 		</c:if>
                    </c:if>
                    <c:if test="${result.payGubun ne '개인'}">	       
                    <td colspan='4'>${result.isgraduatedNDeptcnt} 명</td>
                    </c:if>
                </tr>
                <c:if test="${result.deptGubunLead ne result.payGubun && result.payGubun ne '개인'}">
	                <tr  class="tbcenter" >
	                 	<td  colspan="4" style="background-color: #f5f5f5;">계</td>
	                 	<td  colspan="4" style="background-color: #f5f5f5;">${result.isgraduatedNGubun} 명</td>	
	                </tr>
	                <tr  class="tbcenter" >
	                 	<td  colspan="8" style="background-color: #f5f5f5;">총 미이수인원</td>
	                 	<td  colspan="8" style="background-color: #f5f5f5;">${result.isgraduatedNGubun+p_isgraduatedNDeptcnt} 명</td>	
	                </tr>
                </c:if>
</c:forEach>               
 <!-- 이수현황 END -->  
 <!-- 입금현황 START --> 
   <%-- <c:set var="re_type" value="0" /> 
   <c:set var="re_type_cnt" value="0" />
   <c:set var="fe_type_cnt" value="0" />
   
   <c:set var="paycnt_biyong" value="0" />
 	<c:set var="chkfinalCnt_biyong" value="0" />
 	
   <c:forEach items="${list_amount}" var="result" varStatus="status">
   
   		<c:if test="${status.count==1}">			
			<c:set var="p_biyong3Cnt" value="${result.biyong3Cnt}"/>
		</c:if>
		
   		<c:if test="${result.pay eq '무료' }">
        	<c:set var="fe_type_cnt" value="${result.chkfinalCnt}" /> 
        </c:if>
                      
                <tr   class="tbcenter" <c:if test="${result.subjseqLag ne result.subjseq}">style="border-top: 2px solid #bcbcbc"</c:if>>
                 <c:if test="${result.subjseqLag ne result.subjseq}">
                <td  rowspan="${(result.allSubjseqCnt)+4}">입금현황</td>	
                </c:if>
                 <c:if test="${result.deptGubunLag ne result.payGubun}">
		         		<td rowspan="${result.gubunCnt+1}" colspan="3">${result.payGubun}</td>	
		      	 </c:if>
		      	  	<c:if test="${result.payGubun eq '개인'}">
			      		<td  colspan="4">${result.pay} :</td>	
			      		<td colspan="4">${result.chkfinalCnt} 명 * </td>	
			      		
			      		<c:if test="${result.pay eq '재수강'}" >
			      			<td  colspan="4">0 원 = </td>
			      		</c:if>
			      		<c:if test="${result.pay ne '재수강'}" >
			      			<td  colspan="4"><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /> 원 = </td>
			      		</c:if>	
	                	<c:if test="${result.pay eq '재수강'}" >
	                		<td   colspan="4">0 원</td>
		                	<c:set var="re_type" value="${result.chkfinalCnt * result.biyong}" />		
		                	<c:set var="re_type_cnt" value="${result.chkfinalCnt}" />                	
		                </c:if>
		                <c:if test="${result.pay ne '재수강'}" >
	                		<td  colspan="4"><fmt:formatNumber type="currency" value="${result.chkfinalCnt * result.biyong}" pattern="###,###" /> 원</td>
	                		<c:set var="chkfinalCnt_biyong" value="${chkfinalCnt_biyong + (result.chkfinalCnt * result.biyong)}" />		                	
		                </c:if>	                	
			      	</c:if>
			      	<c:if test="${result.payGubun ne '개인'}">
			      		<td   colspan="4"v>${result.deptNm} :</td>	
			      		<td   colspan="4">${result.payCnt} 명 * </td>
			      		<td   colspan="4"><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /> 원 = </td>	
	                	<td   colspan="4"><fmt:formatNumber type="currency" value="${result.payCnt * result.biyong}" pattern="###,###" /> 원</td>
	                	<c:set var="paycnt_biyong"  value ="${paycnt_biyong + (result.payCnt * result.biyong)}" />
			      	</c:if>
		         	
		         	
                </tr>
                <c:if test="${result.deptGubunLead ne result.payGubun && result.payGubun eq '개인'}">
	                <tr  class="tbcenter" style="background-color: #f5f5f5;">
	                 	<td  colspan="4">${result.payGubun}(징수) :</td>	
			         	<td  colspan="4">${result.chkfinalDeptCnt-re_type_cnt} 명 *
			         	<c:set var="chkfinalCnt" value="${chkfinalCnt+result.chkfinalDeptCnt}" /> 
			         	<c:set var="chkfinalAmount" value="${(result.chkfinalDeptCnt * result.biyong)-(fe_type_cnt * result.biyong)-re_type}" />
			         	</td>	
			         	<td colspan="4" ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /> 원 = <c:set var="biyong" value="${result.biyong}" />    </td>		
		                <td colspan="4" ><fmt:formatNumber type="currency" value="${(result.chkfinalDeptCnt * result.biyong)-(fe_type_cnt * result.biyong)-re_type}" pattern="###,###" />원</td>
	                </tr>
                </c:if>
                <c:if test="${result.deptGubunLead ne result.payGubun && result.payGubun ne '개인'}">
	                <tr  class="tbcenter" style="background-color: #f5f5f5;">
	                 	<td  colspan="4">${result.payGubun}(징수) :</td>	
			         	<td  colspan="4">${result.gubunTotalCnt} 명 *
			         	<c:set var="payCnt" value="${payCnt+result.gubunTotalCnt}" />  
			         	<c:set var="payCntAmount" value="${result.biyong * result.gubunTotalCnt}" />   
			         	</td>	
			         	<c:if test="${result.biyong3Cnt == 0}">	
				         	<td  colspan="4"><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /> 원 = <c:set var="biyong" value="${result.biyong}" /></td>		
			                <td  colspan="4"><fmt:formatNumber type="currency" value="${result.gubunAmount}" pattern="###,###" /> 원</td>
			                <td  colspan="4"><fmt:formatNumber type="currency" value="${biyong * (payCnt+chkfinalCnt)}" pattern="###,###" /> 원</td>
			                <td  colspan="4"><fmt:formatNumber type="currency" value="${biyong * result.gubunTotalCnt}" pattern="###,###" /> 원</td>
		                </c:if>
		                <c:if test="${result.biyong3Cnt > 0}">
		                	<td  colspan="4"></td>		
			                <td  colspan="4"><fmt:formatNumber type="currency" value="${paycnt_biyong}" pattern="###,###" /> 원</td>
		                </c:if>
	                </tr>
                </c:if>
</c:forEach>   
					<tr  class="tbcenter" style="background-color: #e5e5e5;">
	                 	<td rowspan="2" colspan="3">총 계</td>	
			         	<td colspan="16">총징수인원 : ${payCnt+chkfinalCnt-re_type_cnt} 명</td>	
	                </tr>
	               
	                <tr  class="tbcenter" style="background-color: #e5e5e5;">
			         	<c:if test="${p_biyong3Cnt == 0}">
			         		<td colspan="16">총징수금액 : <fmt:formatNumber type="currency" value="${chkfinalAmount+payCntAmount}" pattern="###,###" /> 원</td>
			         	</c:if>
			         	<c:if test="${p_biyong3Cnt > 0}">
	                		<td colspan="16">총징수금액 : <fmt:formatNumber type="currency" value="${chkfinalCnt_biyong+paycnt_biyong}" pattern="###,###" /> 원</td>
	                	</c:if>		
	                </tr>
  --%>
 <!-- 입금현황 END -->                  
            </tbody>
        </table>
    <!-- // detail wrap -->
			<table summary="" cellspacing="0" border="1" width="100%" style="margin-top: 10px"  class="xl26" >
                <caption></caption>
                <colgroup>
				</colgroup>
				<thead>
					<tr  class="tbcenter">
						<th scope="row">시/도</th>
						<th scope="row">서울</th>
						<th scope="row">부산</th>
						<th scope="row">대구</th>
						<th scope="row">인천</th>
						<th scope="row">광주</th>
						<th scope="row">대전</th>
						<th scope="row">울산</th>
						<th scope="row">경기</th>
						<th scope="row">강원</th>
						<th scope="row">충북</th>
						<th scope="row">세종</th>
						<th scope="row">충남</th>
						<th scope="row">전북</th>
						<th scope="row">전남</th>
						<th scope="row">경북</th>
						<th scope="row">경남</th>
						<th scope="row">제주</th>
						<th scope="row">기타</th>
						<th scope="row">합계</th>
					</tr>
				</thead>
				<tbody>
<c:forEach items="${comList}" var="result" varStatus="status">
				<tr  class="tbcenter" height="35">
						<td><strong><c:out value="${result.grpNm}"/></strong></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00001}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00002}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00003}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00004}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00005}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00006}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00007}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00008}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00009}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00010}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00011}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00012}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00013}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00014}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00015}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00016}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00017}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>						
						<td><c:if test="${result.rn eq '3'}"><strong></c:if><c:out value="${result.group00020}"/><c:if test="${result.rn eq '3'}"></strong></c:if></td>
						<td><strong><c:out value="${result.totalCnt}"/></strong></td>
				</tr>
				</c:forEach>
				</tbody>
			</table>
    </div>
<!----------------- 결과보고서(이수현황)  END ---------------------------------------------------------------------------------------->  

</body>
</html>