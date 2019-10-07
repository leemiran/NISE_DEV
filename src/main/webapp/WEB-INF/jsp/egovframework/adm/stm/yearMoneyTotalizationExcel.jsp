<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>

<% 
	String realFileName = "연도별입금내역수정"; 
	//response.reset();
	response.setHeader("Pragma", "no-cache;");
	response.setHeader("Expires", "-1;");
	response.setHeader("Content-Transfer-Encoding", "binary;");
	response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
	response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
	//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");
%>
<html>
<head>
<title>평가결과분석</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<style>
.xl26
	{mso-style-parent:style0;
	color:white;
	font-weight:700;
	text-align:center;
	border:.5pt solid black;
	background:#C2C2C2;
	mso-pattern:auto none;
	white-space:normal;}
.graph { /* 그래프 이미지 */
        height: 14px;
	    border-style: none;
	    background-color: orange;
	    background-repeat: repeat-x;
         }  	
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">


		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" border="1" width="100%">
              <thead>	       
		          <tr>
			        <th scope="row" width="5.5%">기별</th>
			        <th scope="row" width="5.5%">연수과정</th>
			        <th scope="row" width="5.5%">연수기간</th>
			        <th scope="row" width="5.5%">시간</th>
			        <th scope="row" width="5.5%" >대상</th>
			        <th scope="row" width="5.5%">1인당수강료</th>
			        <th scope="row" width="5.5%" width="5.5%">수강인원</th>
			        <th scope="row" width="5.5%">이수인원</th>
			        <th scope="row" width="5.5%" >징수인원</th>
			        <th scope="row" width="5.5%">입금자</th>
			        <th scope="row" width="5.5%" >입금방법</th>
			        <th scope="row" width="5.5%">인원</th>
			        <th scope="row" width="5.5%">입금</th>
			        <th scope="row" width="5.5%">실입금액</th>
			        <th scope="row" width="5.5%">총금액</th>
			        <th scope="row" width="5.5%">미이수인원</th>
			        <th scope="row" width="5.5%">교육청<br/>수납일자</th>
		          </tr>
			
	        </thead>
		    <tbody>		    
<c:set var="titletxt" value=""/>

											
			<c:set var="p_subj" value="${result.subj }" />		
   			<c:forEach items="${resultList}" var="result" varStatus="status" >
		      <tr class="dataList">
		      <c:set var="p_subj" value="${result.subj }" />
		      <c:set var="p_subj_subjseq" value="${p_subj}${result.subjseq}" />
		      
		      		      
		      <c:if test="${p_subj eq r_subj }">
			      <c:if test="${result.subjseqLag ne result.subjseq}">
			      	<c:if test="${result.allSubjseqCnt eq '1'}">
			        <td width="3%">${result.subjseq}</td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne '1'}">
			        <td width="3%" rowspan="${result.allSubjseqCnt}">${result.subjseq}</td>	
			      	</c:if>
			      </c:if>		       
		      </c:if>		      
		      <c:if test="${p_subj ne r_subj }">			      
			      	<c:if test="${result.allSubjseqCnt eq '1'}">
			         <td width="5.5%" >${result.subjseq} </td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.subjseq} </td>	
			      	</c:if>			      		       
		      </c:if>
		      
		      
		       <c:if test="${p_subj eq r_subj }">		     
			      <c:if test="${result.subjseqLag ne result.subjseq}">
			      	<c:if test="${result.allSubjseqCnt eq '1'}">
			         <td width="5.5%" >${result.subjnm}</td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.subjnm}</td>	
			      	</c:if>
			      </c:if>		
		      </c:if>		      
		      <c:if test="${p_subj ne r_subj }">			      
			      	<c:if test="${result.allSubjseqCnt eq '1'}">
			         <td width="5.5%" >${result.subjnm}</td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.subjnm}</td>	
			      	</c:if>
			      		
		      </c:if>
		      
		      <c:if test="${p_subj eq r_subj }">
			      <c:if test="${result.subjseqLag ne result.subjseq}">
			      	<c:if test="${result.allSubjseqCnt eq '1'}">
			         <td width="5.5%" ><c:out value="${fn2:getFormatDate(result.edustart, 'yyyy-MM-dd')}"/><br>~<br><c:out value="${fn2:getFormatDate(result.eduend, 'yyyy-MM-dd')}"/></td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}"><c:out value="${fn2:getFormatDate(result.edustart, 'yyyy-MM-dd')}"/><br>~<br><c:out value="${fn2:getFormatDate(result.eduend, 'yyyy-MM-dd')}"/></td>	
			      	</c:if>
			      </c:if>
		      </c:if>		      
		      <c:if test="${p_subj ne r_subj }">
		      	<c:if test="${result.allSubjseqCnt eq '1'}">
		         <td width="5.5%" ><c:out value="${fn2:getFormatDate(result.edustart, 'yyyy-MM-dd')}"/><br>~<br><c:out value="${fn2:getFormatDate(result.eduend, 'yyyy-MM-dd')}"/></td>	
		      	</c:if>
		      	<c:if test="${result.allSubjseqCnt ne '1'}">
		         <td width="5.5%" rowspan="${result.allSubjseqCnt}"><c:out value="${fn2:getFormatDate(result.edustart, 'yyyy-MM-dd')}"/><br>~<br><c:out value="${fn2:getFormatDate(result.eduend, 'yyyy-MM-dd')}"/></td>	
		      	</c:if>
		      </c:if>
		      		
		      
		      <c:if test="${p_subj eq r_subj }">
			      <c:if test="${result.subjseqLag ne result.subjseq}">
			      	<c:if test="${result.allSubjseqCnt eq '1'}">
			         <td width="5.5%" >${result.edutimes}</td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.edutimes}</td>	
			      	</c:if>
		      </c:if>	
		      </c:if>	
		      <c:if test="${p_subj ne r_subj }">
		      	<c:if test="${result.allSubjseqCnt eq '1'}">
		         <td width="5.5%" >${result.edutimes}</td>	
		      	</c:if>
		      	<c:if test="${result.allSubjseqCnt ne '1'}">
		         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.edutimes}</td>	
		      	</c:if>
		      </c:if>
		      
		      <c:if test="${p_subj eq r_subj }">
			      <c:if test="${result.subjseqLag ne result.subjseq || result.subjseqLag ne result.subjseq}">
			      	<c:if test="${result.allSubjseqCnt eq '1'}">
			         <td width="5.5%" >${result.edumans}</td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.edumans}</td>	
			      	</c:if>
			      </c:if>	
		      </c:if>
		      <c:if test="${p_subj ne r_subj }">
		      	<c:if test="${result.allSubjseqCnt eq '1'}">
		         <td width="5.5%" >${result.edumans}</td>	
		      	</c:if>
		      	<c:if test="${result.allSubjseqCnt ne '1'}">
		         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.edumans}</td>	
		      	</c:if>
		      </c:if>
		      
		      <c:if test="${p_subj eq r_subj }">   
		        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /></td>	
			      	</c:if>
		        </c:if>
		      </c:if>
		      <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /></td>	
			      	</c:if>
		      </c:if>	
		      
		      <c:if test="${p_subj eq r_subj }">  
		        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.deptCnt}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.deptCnt}" pattern="###,###" /></td>	
			      	</c:if>
		        </c:if>
		       </c:if> 
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.deptCnt}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.deptCnt}" pattern="###,###" /></td>	
			      	</c:if>
		        </c:if>
		        		  
		        <c:if test="${p_subj eq r_subj }">
			        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
				      	<c:if test="${result.allDeptCnt eq '1'}">
				         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.isgraduatedYDeptcnt}" pattern="###,###" /></td>	
				      	</c:if>
				      	<c:if test="${result.allDeptCnt ne '1'}">
				         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.isgraduatedYDeptcnt}" pattern="###,###" /></td>	
				      	</c:if>
			        </c:if>
		        </c:if>
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.isgraduatedYDeptcnt}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.isgraduatedYDeptcnt}" pattern="###,###" /></td>	
			      	</c:if>
		        </c:if>	
		        
		        <c:if test="${p_subj eq r_subj }">
			        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
				      	<c:if test="${result.allDeptCnt eq '1'}">
				         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.payDeptCnt}" pattern="###,###" /></td>	
				      	</c:if>
				      	<c:if test="${result.allDeptCnt ne '1'}">
				         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.payDeptCnt}" pattern="###,###" /></td>	
				      	</c:if>
			        </c:if>
		        </c:if>
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.payDeptCnt}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.payDeptCnt}" pattern="###,###" /></td>	
			      	</c:if>
		        </c:if>	
		        
		        <c:if test="${p_subj eq r_subj }">
			        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
				      	<c:if test="${result.allDeptCnt eq '1'}">
				         <td width="5.5%" >${result.deptNm}</td>	
				      	</c:if>
				      	<c:if test="${result.allDeptCnt ne '1'}">
				         <td width="5.5%" rowspan="${result.allDeptCnt}">${result.deptNm}</td>	
				      	</c:if>
		        	</c:if>
		        </c:if>
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" >${result.deptNm}</td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}">${result.deptNm}</td>	
			      	</c:if>
		        </c:if>
		        	
		         <td width="5.5%">${result.pay}</td>
		         <td width="5.5%"><fmt:formatNumber type="currency" value="${result.payCnt}" pattern="###,###" />  </td>
		         <td width="5.5%">
		         	<c:if test="${result.type ne 'RE'}">
		         		<fmt:formatNumber type="currency" value="${result.payAmount}" pattern="###,###" />
		         	</c:if>
		         	<c:if test="${result.type eq 'RE'}">
		         		0
		         	</c:if>
		         </td>
        
		        
		        <c:if test="${p_subj_subjseq eq r_subj_subjseq }">
		        	<c:if test="${result.deptNm eq '개인' && result.payLag eq '교육청일괄납부'}">		        	
		        		<c:if test="${result.allDeptPayCnt eq '1'}">
					         <td width="5.5%" >
					         <fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" />
					         </td>	
					      	</c:if>
					      	<c:if test="${result.allDeptPayCnt ne '1'}">
					         <td rowspan="${result.allDeptPayCnt}"><fmt:formatNumber type="currency" value="${result.deptAmount }" pattern="###,###" /></td>	
					    </c:if>
					</c:if>
		        </c:if>		        
		        <c:if test="${p_subj_subjseq ne r_subj_subjseq }">
		        	
			        	<c:if test="${result.allDeptPayCnt eq '1'}">
					         <td width="5.5%" >
					         <fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" />
					         </td>	
					    </c:if>
					    <c:if test="${result.allDeptPayCnt ne '1'}">
					         <c:if test="${result.deptNm eq '개인'}">
					         	<td rowspan="${result.allDeptPayCnt}"><fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" /></td>
					         </c:if>
					         <c:if test="${result.deptNm ne '개인'}">
					         	<td rowspan="${result.allDeptPayCnt}"><fmt:formatNumber type="currency" value="${result.deptAmount }" pattern="###,###" /></td>
					         </c:if>
					         	
					    </c:if>
					    			      	
		        </c:if>      	
		        
		       
		        
		        <c:if test="${p_subj eq r_subj }">	
			        <c:if test="${result.subjseqLag ne result.subjseq}">		         
				      	<c:if test="${result.allSubjseqCnt eq 1}">
				         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.subjseqAmount}" pattern="###,###" /></td>	
				      	</c:if>
				      	<c:if test="${result.allSubjseqCnt ne 1}">
				         <td width="5.5%" rowspan="${result.allSubjseqCnt}"><fmt:formatNumber type="currency" value="${result.subjseqAmount }" pattern="###,###" /></td>	
				      	</c:if>
			      	</c:if>		      	
		      	</c:if>
		      	<c:if test="${p_subj ne r_subj }">		         
			      	<c:if test="${result.allSubjseqCnt eq 1}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.subjseqAmount}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne 1}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}"><fmt:formatNumber type="currency" value="${result.subjseqAmount }" pattern="###,###" /></td>	
			      	</c:if>
		      	</c:if>
		      	
		      	<c:if test="${p_subj eq r_subj }">
		         <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.isgraduatedNDeptcnt}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.isgraduatedNDeptcnt}" pattern="###,###" /></td>	
			      	</c:if>
		        </c:if>
		        </c:if>
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.isgraduatedNDeptcnt}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.isgraduatedNDeptcnt}" pattern="###,###" /></td>	
			      	</c:if>
		        </c:if>
		        
		        	
		       
		       <c:if test="${p_subj eq r_subj }">
		       		<c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      		<c:if test="${result.allDeptCnt eq '1'}">
			      	
					       	<td width="4.8%">
					         	<c:if test="${result.type eq 'OB' }">
					         		<c:if test="${fn:length(result.deptCd)>0}">
					         			${result.depositDate}
					         		</c:if>
					         		<c:if test="${fn:length(result.deptCd) == 0}">
					         			
					         		</c:if>
					         	</c:if>
					         	<c:if test="${result.type ne 'OB' }">
					         		
					         	</c:if>			         	
					         </td>	
			    		</c:if>
			    		<c:if test="${result.allDeptCnt ne '1'}">
					         <td rowspan="${result.allDeptPayCnt}" width="4.8%"></td>
			    		</c:if>
			    	
		        	</c:if>
		        </c:if>		        
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			      	
				       <td width="4.8%">
				         	<c:if test="${result.type eq 'OB' }">
				         		<c:if test="${fn:length(result.deptCd)>0}">
				         			${result.depositDate}
				         		</c:if>
				         		<c:if test="${fn:length(result.deptCd) == 0}">
				         			
				         		</c:if>
				         	</c:if>
				         	<c:if test="${result.type ne 'OB' }">
				         		
				         	</c:if>			         	
				         </td>	
			    	</c:if>
			    	<c:if test="${result.allDeptCnt ne '1'}">
						<td rowspan="${result.allDeptPayCnt}" width="4.8%"></td>
			    	</c:if>
		        </c:if>
  
		         
			         
			         
		      </tr>
		      
		      <c:set var="r_subj" value="${result.subj }" />
		      <c:set var="r_subj_subjseq" value="${p_subj}${result.subjseq}" />
		      
			</c:forEach>
			
			
	        </tbody>            
	      </table>
    </div>
		<!-- list table-->
		
</form>
<br/>


<div class="tbList">
            	
           <table summary="" cellspacing="0" border="1" width="100%">
           	<tbody>
		   	<tr>
		        <td width="5.5%">기별</td>
		        <td widtd="5.5%">연수과정</td>
		        <td widtd="5.5%">연수기간</td>
		        <td widtd="5.5%">시간</td>
		        <td widtd="5.5%" >대상</td>
		        <td widtd="5.5%">1인당수강료</td>
		        <td widtd="5.5%" widtd="5.5%">수강인원</td>
		        <td widtd="5.5%">이수인원</td>
		        <td widtd="5.5%" >징수인원</td>
		        <td widtd="5.5%">입금자</td>
		        <td widtd="5.5%" >입금방법</td>
		        <td widtd="5.5%">인원</td>
		        <td widtd="5.5%">입금</td>
		        <td widtd="5.5%">실입금액</td>
		        <td widtd="5.5%">총금액</td>
		        <td widtd="5.5%">미이수인원</td>
		        <td widtd="5.5%">교육청<br/>수납일자</td>
	        </tr>
	        
	        
	        <c:set var="type_count" value="0"/> 
	        <c:set var="type_count_2" value="0"/> 
	        <c:forEach items="${totalList}" var="result" varStatus="status" >		
	        	     
		        </tr>
			        <c:if test="${status.count == 1}">
			        	<td colspan="6" rowspan="${fn:length(totalList)}">집계</td>
			        </c:if>	
			        	
		        	
					<td width="5.5%" ><fmt:formatNumber type="currency" value="${result.deptCnt}" pattern="###,###" /></td>	
	        		<td width="5.5%" ><fmt:formatNumber type="currency" value="${result.isgraduatedYDeptcnt}" pattern="###,###" /></td>
	        		<c:if test="${result.type ne 'RE'}">        		
						<td width="5.5%" ><fmt:formatNumber type="currency" value="${result.payDeptCnt}" pattern="###,###" /></td>
					</c:if>
					<c:if test="${result.type eq 'RE'}">        		
						<td width="5.5%" >0</td>
					</c:if>
					
		        	<c:if test="${status.count == 1}">
		        		<td rowspan="${fn:length(totalList)}"></td>
		        	</c:if>
		        	<td>${result.pay}</td>
		        	<td><fmt:formatNumber type="currency" value="${result.payCnt}" pattern="###,###" /></td>
		        	<td>
		        		
		        		<c:if test="${result.type ne 'RE'}">
							<fmt:formatNumber type="currency" value="${result.payAmount}" pattern="###,###" />
						</c:if>
						<c:if test="${result.type eq 'RE'}">
							0
						</c:if>
		        	</td>      	
		        	
		        	<!-- 교육청일괄납부가 아닌것 -->
		        	<c:if test="${result.pay ne '교육청일괄납부'}">
		        		<c:set var="type_count" value="${type_count+1 }"/>      
				         <td width="5.5%" >
				         	<c:if test="${result.type ne 'RE' && result.type ne 'FE'}">
					         	<fmt:formatNumber type="currency" value="${result.payAmount}" pattern="###,###" />
					         </c:if>
					         <c:if test="${result.type eq 'RE' || result.type eq 'FE'}">		
					         	0
					         </c:if>        	
					         
				         </td>
				         
			         </c:if>
		        	<%-- <c:if test="${(result.type eq 'PB' || result.type eq 'SC0010' || result.type eq 'SC0030') && type_count == 0 }">
		        		<c:set var="type_count" value="${type_count+1 }"/>      
				         <td width="5.5%" rowspan="${result.allDeptPayCnt}"  >
					         <fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" />
					         <c:if test="${result.type eq 'OB'}">
					         	<br/>(수납완료<fmt:formatNumber type="currency" value="${result.dpDeptAmount}" pattern="###,###" />) 
					         </c:if>
				         </td>
				         
			         </c:if> --%>
			         
			         <!-- 교육청일괄납부 -->
			         <c:if test="${result.pay eq '교육청일괄납부'}">		        		      
				         <td width="5.5%" >				         	
					        	<fmt:formatNumber type="currency" value="${result.payAmount}" pattern="###,###" />
					         	<br/>(수납완료<fmt:formatNumber type="currency" value="${result.dpDeptAmount}" pattern="###,###" />)					         					         
				         </td>				         
			         </c:if> 
			         
			         <%-- <c:if test="${result.type ne 'RE' && result.type ne 'FE' && result.type ne 'PB' && result.type ne 'SC0010' && result.type ne 'SC0030'}">		        		      
				         <td width="5.5%" >
				         	<c:if test="${result.type ne 'RE'}">
					        	<fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" />
					         	<br/>(수납완료<fmt:formatNumber type="currency" value="${result.dpDeptAmount}" pattern="###,###" />)
					         </c:if>
					         					         
				         </td>				         
			         </c:if> --%>

		        	
			        <c:if test="${status.count == 1}">
	       				<td  rowspan="${fn:length(totalList)}">
	       					<fmt:formatNumber type="currency" value="${result.sumPayAmount}" pattern="###,###" />
	        				<br/>(수납완료<fmt:formatNumber type="currency" value="${result.sumDpDeptAmount}" pattern="###,###" />)
	        			</td>
        			</c:if>         	
        			
				    <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.isgraduatedNDeptcnt}" pattern="###,###" /></td>	
				    
			        
		        	<c:if test="${status.count == 1}">
	       				<td  rowspan="${fn:length(totalList)}"></td>
        			</c:if>  
		        </tr>
	       </c:forEach>
	       </tbody>
	       </table>
	        
			        
			         				
		</div>
        <!-- // detail wrap -->   


</body>
</html>
