<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<style>
table.ex1 {width:97%; margin:0 auto; text-align:left; border-collapse:collapse; text-decoration: none; }
 .ex1 th, .ex1 td {padding:0px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">

 	<input type="hidden" name="p_process" value="">
	<input type="hidden" name="p_action"  value="">
	<input type="hidden" name="p_grcode"  value="">
	<input type="hidden" name="p_subj"  value="">
	<input type="hidden" name="p_examnum"  value="">
	<input type="hidden" name="p_examtype"  value="">
	<input type="hidden" name="p_chknum"  value="">
	<input type="hidden" name="rd_gubun"  value="B">
	<input type="hidden" name="ses_search_att_name" id="ses_search_att_name"  value="">
	<input type="hidden" name="p_searchYN"  value="Y">
	<input type="hidden" name="p_subjseq"  value="">	
	<input type="hidden" name="p_year"  value="">
	<input type="hidden" name="p_deptNm"  value="">
	
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="AA_year"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<div class="listTop">
		<!-- 
		  <div class="sub_tit floatL">
			<h4>과정명 : [${ses_search_subjnm}]</h4>	
		  </div>		
		 -->
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="javascript:subjReceipMomeyPrint();" class="btn01"><span>인쇄</span></a>
                </div>
               
		</div>
        
		
	  <!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
              <caption>목록</caption>
              <thead>
	          <tr>
	          	<td>
	          		<table summary="" cellspacing="0" border="1" width="100%">
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
			        </table>
			     </td>
	          </tr>
	        </thead>
		    <tbody>		    
<c:set var="titletxt" value=""/>

	<tr>
				<td>
				<div style="height:300px;overflow-y:auto;">
					<table summary="" cellspacing="0" width="100%">
										
			<c:set var="p_subj" value="${result.subj }" />		
   			<c:forEach items="${list}" var="result" varStatus="status" >
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
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.edumans}<!-- 대상 --></td>	
			      	</c:if>
			      </c:if>	
		      </c:if>
		      <c:if test="${p_subj ne r_subj }">
		      	<c:if test="${result.allSubjseqCnt eq '1'}">
		         <td width="5.5%" >${result.edumans}</td>	
		      	</c:if>
		      	<c:if test="${result.allSubjseqCnt ne '1'}">
		         <td width="5.5%" rowspan="${result.allSubjseqCnt}">${result.edumans}<!-- 대상 --></td>	
		      	</c:if>
		      </c:if>
		      
		      <c:if test="${p_subj eq r_subj }">   
		        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			      		<c:if test="${result.type eq 'OB'}">
			      			<td width="5.5%" ><fmt:formatNumber type="currency" value="${result.biyong2}" pattern="###,###" /></td>
			      		</c:if>
			      		<c:if test="${result.type ne 'OB'}">
			      			<td width="5.5%" ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /></td>
			      		</c:if>			         	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			      		<c:if test="${result.type eq 'OB'}">
			      			<td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.biyong2}" pattern="###,###" /></td>
			      		</c:if>
			      		<c:if test="${result.type ne 'OB'}">
			      			<td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /></td>
			      		</c:if>	      		
			         	
			      	</c:if>
		        </c:if>
		      </c:if>
		      <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			      		<c:if test="${result.type eq 'OB'}">
			      			<td width="5.5%" ><fmt:formatNumber type="currency" value="${result.biyong2}" pattern="###,###" /></td>
			      		</c:if>
			      		<c:if test="${result.type ne 'OB'}">
			      			<td width="5.5%" ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /></td>
			      		</c:if>
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			      		<c:if test="${result.type eq 'OB'}">
			      			<td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.biyong2}" pattern="###,###" /></td>
			      		</c:if>
			      		<c:if test="${result.type ne 'OB'}">
			      			<td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /></td>
			      		</c:if>
			      	</c:if>
		      </c:if>	
		      
		      <c:if test="${p_subj eq r_subj }">  
		        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.deptCnt}" pattern="###,###" /><!-- 수강인원 --></td>	
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
				         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.isgraduatedYDeptcnt}" pattern="###,###" /><!-- 이수인원 --></td>	
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
		        
		        <%-- <c:if test="${p_subj eq r_subj }">
			        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
				      	<c:if test="${result.allDeptCnt eq '1'}">
				         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.payDeptCnt}" pattern="###,###" /><!-- 징수인원 --></td>	
				      	</c:if>
				      	<c:if test="${result.allDeptCnt ne '1'}">
				         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.payDeptCnt - result.reamountcnt}" pattern="###,###" /></td>	
				      	</c:if>
			        </c:if>
		        </c:if>
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.payDeptCnt}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><fmt:formatNumber type="currency" value="${result.payDeptCnt - result.reamountcnt}" pattern="###,###" /></td>	
			      	</c:if>
		        </c:if>	 --%>
		        
		        <c:if test="${p_subj eq r_subj }">  
		        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.deptCnt}" pattern="###,###" /><!-- 징수인원 --></td>	
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
				         <td width="5.5%" >${result.deptNm}<!-- 입금자 --></td>	
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
		        	
		         <td width="5.5%">${result.pay} <!-- 입금방법 --></td>
		         <td width="5.5%"><fmt:formatNumber type="currency" value="${result.payCnt}" pattern="###,###" />  <!-- 인원 --></td>
		         <td width="5.5%">
		         	<c:if test="${result.type ne 'RE'}">
		         		<fmt:formatNumber type="currency" value="${result.payAmount}" pattern="###,###" />
		         	</c:if>
		         	<c:if test="${result.type eq 'RE'}">
		         		0
		         	</c:if>
		         </td>
        
		        
		        <%-- <c:if test="${p_subj_subjseq eq r_subj_subjseq }">
		        	<c:if test="${result.deptNm eq '개인' && result.payLag eq '교육청일괄납부'}">		        	
		        		<c:if test="${result.allDeptPayCnt eq '1'}">
					         <td width="5.5%" >
					         <fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" />
					         </td>	
					      	</c:if>
					      	<c:if test="${result.allDeptPayCnt ne '1'}">
					         <td rowspan="${result.allDeptPayCnt}"><fmt:formatNumber type="currency" value="${result.deptAmount - result.reamount}" pattern="###,###" /></td>	
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
					         	<td rowspan="${result.allDeptPayCnt}"><fmt:formatNumber type="currency" value="${result.deptAmount -  result.reamount}" pattern="###,###" /></td>
					         </c:if>
					         <c:if test="${result.deptNm ne '개인'}">
					         	<td rowspan="${result.allDeptPayCnt}"><fmt:formatNumber type="currency" value="${result.deptAmount }" pattern="###,###" /></td>
					         </c:if>
					         	
					    </c:if>
					    			      	
		        </c:if>    --%>   	
		        
		        <c:if test="${p_subj_subjseq eq r_subj_subjseq }">
		        	<c:if test="${result.deptNm eq '개인' && result.payLag eq '교육청일괄납부'}">		        	
		        		<c:if test="${result.allDeptPayCnt eq '1'}">
					         <td width="5.5%" >
					         <fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" /><!-- 입금 -->
					         </td>	
					      	</c:if>
					      	<c:if test="${result.allDeptPayCnt ne '1'}">
					         <td rowspan="${result.allDeptPayCnt}"><fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" /></td>	
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
				         <td width="5.5%" rowspan="${result.allSubjseqCnt}"><fmt:formatNumber type="currency" value="${result.subjseqAmount - result.reamount}" pattern="###,###" /></td>	
				      	</c:if>
			      	</c:if>		      	
		      	</c:if>
		      	<c:if test="${p_subj ne r_subj }">		         
			      	<c:if test="${result.allSubjseqCnt eq 1}">
			         <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.subjseqAmount}" pattern="###,###" /></td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne 1}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}"><fmt:formatNumber type="currency" value="${result.subjseqAmount - result.reamount}" pattern="###,###" /></td>	
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
			
			</table>
					</div>
				</td>
			</tr>
			
	        </tbody>            
	      </table>
    </div>
		<!-- list table-->
		
</form>
<br/>


<div class="tbList">
            	
           <table summary="" cellspacing="0" width="100%">
           	<caption>목록</caption>
                <colgroup>
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                    <col />
                </colgroup>
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
		        <!-- <td widtd="5.5%">교육청<br/>수납일자</td> -->
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
		        	<c:if test="${result.type ne 'OB'}">
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
			         <c:if test="${result.type eq 'OB'}">		        		      
				         <td width="5.5%" >				         	
					        	<fmt:formatNumber type="currency" value="${result.deptAmount}" pattern="###,###" />
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
	       					<fmt:formatNumber type="currency" value="${result.totalAmount - result.reamount}" pattern="###,###" />
	        				<br/>(수납완료<fmt:formatNumber type="currency" value="${result.dpTotalAmount - result.reamount}" pattern="###,###" />)
	        			</td>
        			</c:if> 	        	
        			
				    <td width="5.5%" ><fmt:formatNumber type="currency" value="${result.isgraduatedNDeptcnt}" pattern="###,###" /></td>	
				    
			        
		        	<%-- <c:if test="${status.count == 1}">
		        		<td rowspan="${fn:length(totalList)}"></td>
		        	</c:if> --%>
		        </tr>
	       </c:forEach>
	       </tbody>
	       </table>
	        
			        
			         				
		</div>
        <!-- // detail wrap -->  

<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
$(function(){
	$("#ses_search_gyear option:eq(0)").remove();	
	//$("#ses_search_gyear option:eq(1)").attr("selected","selected");	
});


//선택한 문제번호 모으기
function chkExamNum(frm) {
	var v_chkcnt = 0;
	var v_chknum = "";
	
	if (frm.all['p_checks']) {
          
		if (frm.p_checks != null && frm.p_checks.length > 0) {
			for (i=0; i<frm.p_checks.length; i++) {
				if (frm.p_checks[i].checked == true) {

					v_chkcnt += 1;
					v_chknum += frm.p_checks[i].value + ",";
				}
			}
			v_chknum = v_chknum.substring(0,v_chknum.length-1);
		} else if ( frm.p_checks != null ) {
			if (frm.p_checks.checked) {

				v_chkcnt = 1;
				v_chknum = frm.p_checks.value;
			}
		}
		if (v_chkcnt==0) {
			alert("문제를 선택하세요");
			return "-1";           
		}
	} else {
		alert("선택할 문제가 없습니다.");
		return "-1";
	}
	return v_chknum;
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {

	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}
/*
	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}
	*/


	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/stm/yearReceiptMoneyList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/exm/examQuestList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}



//등록 / 수정
function doView(p_subj, p_examnum) {
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	
	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}

	
     farwindow = window.open("", "examQuestViewPopWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 700, top=0, left=0");
     thisForm.p_subj.value = $("#ses_search_subj").val();
     thisForm.p_examnum.value = p_examnum;
     thisForm.target = "examQuestViewPopWindow"
     thisForm.action = "/adm/exm/examQuestView.do";
 	 thisForm.submit();

     farwindow.window.focus();
}




//문제 미리보기
function previewQuestion() {

	var frm = thisForm;
	
	var v_chknum = chkExamNum(frm);
	if (v_chknum != "-1") {
		window.open("", "popExamQuestionPreview", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes, resizable=yes,copyhistory=no, width=800, height=600, top=0, left=0");
	
	  	frm.target = "popExamQuestionPreview";
	  	frm.action = "/adm/exm/examQuestPreview.do";
	  	frm.p_subj.value = $("#ses_search_subj").val();
	  	frm.p_chknum.value = v_chknum;
		frm.submit();
	}
}


//폴 추가 
function insertPool() {
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	
	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}

	
    //farwindow = window.open("", "examQuestPoolListPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 1017, height = 700, top=0, left=0");
    thisForm.p_subj.value = $("#ses_search_subj").val();
    
    //thisForm.target = "examQuestPoolListPopWindow"
    thisForm.action = "/adm/exm/examQuestPoolList.do";
	thisForm.submit();

    //farwindow.window.focus();
}


	

//파일로 추가
function insertFileToDB() {
		if($("#ses_search_gyear").val() == '')
		{	
			alert("연도를 선택하세요");
			return;
		}
	
		if($("#ses_search_grseq").val() == '')
		{	
			alert("교육기수를 선택하세요");
			return;
		}
		
		if($("#ses_search_subj").val() == '')
		{	
			alert("과정을 선택하세요");
			return;
		}
	
		farwindow = window.open("", "examQuestFileUploadPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 830, height = 600, top=0, left=0");
		
		thisForm.p_subj.value = $("#ses_search_subj").val();
		thisForm.action = "/adm/exm/examQuestFileUpload.do";
		thisForm.target = "examQuestFileUploadPopWindow"
		thisForm.submit();
	
}



	 
function whenAllSelect() {
	if(thisForm.all['p_checks']) {
		if (thisForm.p_checks.length > 0) {
			for (i=0; i<thisForm.p_checks.length; i++) {
				thisForm.p_checks[i].checked = true;
			}
		} else {
			thisForm.p_checks.checked = true;
		}
	}
}

function whenAllSelectCancel() {
	if(thisForm.all['p_checks']) {
		if (thisForm.p_checks.length > 0) {
			for (i=0; i<thisForm.p_checks.length; i++) {
				thisForm.p_checks[i].checked = false;
			}
		} else {
			thisForm.p_checks.checked = false;
		}
	}
}

function chkeckall(){
    if(thisForm.p_chkeckall.checked){
      whenAllSelect();
    }
    else{
      whenAllSelectCancel();
    }
}



//문제 삭제하기
function deleteQuestion() {

	var frm = thisForm;
	
	var v_chknum = chkExamNum(frm);

	if (v_chknum != "-1") {

	    if(!confirm("정말 삭제하시겠습니까?\n이미 사용되어진 문제는 삭제되지 않습니다.")) {
	        return;
	    }
	
	  	
	  	frm.p_subj.value = $("#ses_search_subj").val();
	  	frm.p_chknum.value = v_chknum;
		frm.p_process.value = "checkDelete";
		frm.target = "_self";
	  	frm.action = "/adm/exm/examQuestAction.do";
		frm.submit();
	}
}

/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	if($(".dataList").length == 0){
		alert("조회된 내역이 없습니다.");
		return;
	}	
	thisForm.action = "/adm/stm/subjReceiptMoneyExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//통계 RD 출력
function subjReceipMomeyPrint(){
	//var frm = eval('document.<c:out value="${gsMainForm}"/>');
	window.open('', 'subjectPop', 'left=100,top=100,width=1000,height=700,scrollbars=yes');
	$("#ses_search_att_name").val($("#ses_search_att option:selected").text());
	thisForm.action = "/adm/stm/subjReceipMomeyPrint.do";
	thisForm.target = "subjectPop";
	thisForm.submit(); 
}


//교육청 수납일자
function fn_ob_ipdate(subj, year, oSubjseq, deptCd){
	
	thisForm.p_subj.value = subj;
	thisForm.p_year.value = year;
	thisForm.p_subjseq.value = oSubjseq;
	thisForm.p_deptNm.value = deptCd;
	
	window.open('', 'ob_ipdate', 'left=100,top=100,width=500,height=300,scrollbars=yes');
	thisForm.action = "/adm/stm/subjReceipMomeyObIpdate.do";
	thisForm.target = "ob_ipdate";
	thisForm.submit();	
}


//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>