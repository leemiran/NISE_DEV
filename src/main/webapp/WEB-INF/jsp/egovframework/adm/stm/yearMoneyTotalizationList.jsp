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
	<input type="hidden" name="p_create" value="">
	<input type="hidden" name="viewPage" value="">
	<input type="hidden" id="totalCnt" name="totalCnt"	value="${totalCnt}">
	
	
            
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
                
                <span id="dataCreate" style="color:#ff0000; margin-right: 800px;"></span>
                <div class="btnR MR05">                
                	<a href="#none" onclick="yearSave()" class="btn01"><span>저장</span></a>
                	<a href="#none" onclick="dataCreate()" class="btn01"><span>데이터생성</span></a>               	
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>                
               		<!-- <a href="javascript:subjReceipMomeyPrint();" class="btn01"><span>인쇄</span></a> -->
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
			         <td width="5.5%" ><input type="text" name="biyong_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.biyong}" size="6" ><!-- biyong1 --></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="biyong_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.biyong}"  size="6" /><!-- biyong2 --></td>	
			      	</c:if>
		        </c:if>
		      </c:if>
		      <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><input type="text" name="biyong_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.biyong}"  size="6" /><!-- biyong3 --></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="biyong_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.biyong}" size="6" /><!-- biyong4 --></td>	
			      	</c:if>
		      </c:if>	
		      
		      <c:if test="${p_subj eq r_subj }">  
		        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><input type="text" name="deptCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptCnt}"  size="6" /><!-- deptCnt1 --></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="deptCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptCnt}"  size="6" /><!-- deptCnt2 --></td>	
			      	</c:if>
		        </c:if>
		       </c:if> 
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><input type="text" name="deptCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptCnt}"  size="6" /><!-- deptCnt3 --></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="deptCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptCnt}"  size="6" /><!-- deptCnt4 --></td>	
			      	</c:if>
		        </c:if>
		        		  
		        <c:if test="${p_subj eq r_subj }">
			        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
				      	<c:if test="${result.allDeptCnt eq '1'}">
				         <td width="5.5%" ><input type="text" name="isgraduatedYDeptcnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.isgraduatedYDeptcnt}"  size="6" /><!-- isgraduatedYDeptcnt1 --></td>	
				      	</c:if>
				      	<c:if test="${result.allDeptCnt ne '1'}">
				         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="isgraduatedYDeptcnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.isgraduatedYDeptcnt}"  size="6" /><!-- isgraduatedYDeptcnt2 --></td>	
				      	</c:if>
			        </c:if>
		        </c:if>
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><input type="text" name="isgraduatedYDeptcnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.isgraduatedYDeptcnt}"  size="6" /><!-- isgraduatedYDeptcnt3 --></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="isgraduatedYDeptcnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.isgraduatedYDeptcnt}"  size="6" /><!-- isgraduatedYDeptcnt4 --></td>	
			      	</c:if>
		        </c:if>	
		        
		        <c:if test="${p_subj eq r_subj }">
			        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
				      	<c:if test="${result.allDeptCnt eq '1'}">
				         <td width="5.5%" ><input type="text" name="payDeptCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.payDeptCnt}"  size="6" /><!-- payDeptCnt1 --></td>	
				      	</c:if>
				      	<c:if test="${result.allDeptCnt ne '1'}">
				         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="payDeptCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.payDeptCnt}"  size="6" /><!-- payDeptCnt2 --></td>	
				      	</c:if>
			        </c:if>
		        </c:if>
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><input type="text" name="payDeptCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.payDeptCnt}"  size="6" /><!-- payDeptCnt3 --></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="payDeptCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.payDeptCnt}"  size="6" /><!-- payDeptCnt4 --></td>	
			      	</c:if>
		        </c:if>	
		        
		        <c:if test="${p_subj eq r_subj }">
			        <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			        	<input type="hidden" name="deptNm_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptNm}" />
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
		         <td width="5.5%"><input type="text" name="payCnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}_${result.type}" value="${result.payCnt}"  size="6" /><!-- payCnt1 --></td>
		         <td width="5.5%">
		         	<c:if test="${result.type ne 'RE'}">
		         		<input type="text" name="payAmount_${result.subj}_${result.oSubjseq}_${result.deptIdx}_${result.type}" value="${result.payAmount}"  size="6" /><!-- payAmount1 -->
		         	</c:if>
		         	<c:if test="${result.type eq 'RE'}">
		         		<input type="text" name="payAmount_${result.subj}_${result.oSubjseq}_${result.deptIdx}_${result.type}" value="${result.payAmount}"  size="6" /><!-- payAmount2 -->
		         	</c:if>
		         </td>
        
		        
		        <c:if test="${p_subj_subjseq eq r_subj_subjseq }">
		        	<c:if test="${result.deptNm eq '개인' && result.payLag eq '교육청일괄납부'}">		        	
		        		<c:if test="${result.allDeptPayCnt eq '1'}">
					         <td width="5.5%" >
					         	<input type="text" name="deptAmount_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptAmount}"  size="6" /><!-- deptAmount1 -->
					         </td>
					      	</c:if>
					      	<c:if test="${result.allDeptPayCnt ne '1'}">
					        	<td rowspan="${result.allDeptPayCnt}"><input type="text" name="deptAmount_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptAmount}"  size="6" /><!-- deptAmount2 --></td>	
					    	</c:if>
					</c:if>
		        </c:if>		        
		        <c:if test="${p_subj_subjseq ne r_subj_subjseq }">
		        	
			        	<c:if test="${result.allDeptPayCnt eq '1'}">
					         <td width="5.5%" >
					          <c:if test="${result.deptNm eq '개인'}">
					          	<input type="text" name="deptAmount_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptAmount}"  size="6" /><!-- deptAmount3 -->
					          </c:if>
					          <c:if test="${result.deptNm ne '개인'}">
					          	<input type="text" name="deptAmount_${result.subj}_${result.oSubjseq}_${result.type}" value="${result.deptAmount}"  size="6" /><!-- deptAmount3 -->
					          </c:if>
					         	
					         	
					         </td>	
					    </c:if>
					    <c:if test="${result.allDeptPayCnt ne '1'}">
					         <c:if test="${result.deptNm eq '개인'}">
					         	<td rowspan="${result.allDeptPayCnt}"><input type="text" name="deptAmount_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.deptAmount}" size="6" /><!-- deptAmount4 --></td>
					         </c:if>
					         <c:if test="${result.deptNm ne '개인'}">
					         	<td rowspan="${result.allDeptPayCnt}"><input type="text" name="deptAmount_${result.subj}_${result.oSubjseq}_${result.type}" value="${result.deptAmount}" size="6" /><!-- deptAmount5 --></td>
					         </c:if>
					         	
					    </c:if>
					    			      	
		        </c:if>      	
		        
		       
		        
		        <c:if test="${p_subj eq r_subj }">	
			        <c:if test="${result.subjseqLag ne result.subjseq}">		         
				      	<c:if test="${result.allSubjseqCnt eq 1}">
				         <td width="5.5%" ><input type="text" name="subjseqAmount_${result.subj}_${result.oSubjseq}" value="${result.subjseqAmount}" size="6" /><!-- subjseqAmount1 --></td>	
				      	</c:if>
				      	<c:if test="${result.allSubjseqCnt ne 1}">
				         <td width="5.5%" rowspan="${result.allSubjseqCnt}"><input type="text" name="subjseqAmount_${result.subj}_${result.oSubjseq}" value="${result.subjseqAmount}" size="6" /><!-- subjseqAmount2 --></td>	
				      	</c:if>
			      	</c:if>		      	
		      	</c:if>
		      	<c:if test="${p_subj ne r_subj }">		         
			      	<c:if test="${result.allSubjseqCnt eq 1}">
			         <td width="5.5%" ><input type="text" name="subjseqAmount_${result.subj}_${result.oSubjseq}" value="${result.subjseqAmount}" size="6" /><!-- subjseqAmount3 --></td>	
			      	</c:if>
			      	<c:if test="${result.allSubjseqCnt ne 1}">
			         <td width="5.5%" rowspan="${result.allSubjseqCnt}"><input type="text" name="subjseqAmount_${result.subj}_${result.oSubjseq}" value="${result.subjseqAmount}" size="6" /><!-- subjseqAmount4 --></td>	
			      	</c:if>
		      	</c:if>
		      	
		      	<c:if test="${p_subj eq r_subj }">
		         <c:if test="${result.subjseqLag ne result.subjseq || result.deptLag ne result.deptIdx}">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><input type="text" name="isgraduatedNDeptcnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.isgraduatedNDeptcnt}" size="6" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="isgraduatedNDeptcnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.isgraduatedNDeptcnt}" size="6" /></td>	
			      	</c:if>
		        </c:if>
		        </c:if>
		        <c:if test="${p_subj ne r_subj }">
			      	<c:if test="${result.allDeptCnt eq '1'}">
			         <td width="5.5%" ><input type="text" name="isgraduatedNDeptcnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.isgraduatedNDeptcnt}" size="6" /></td>	
			      	</c:if>
			      	<c:if test="${result.allDeptCnt ne '1'}">
			         <td width="5.5%" rowspan="${result.allDeptCnt}"><input type="text" name="isgraduatedNDeptcnt_${result.subj}_${result.oSubjseq}_${result.deptIdx}" value="${result.isgraduatedNDeptcnt}" size="6" /></td>	
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


var timecnt=0;
//$("#ses_search_gyear option:eq(0)").remove();
var year = $("#ses_search_gyear").val();
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
if("${totalCnt}" > 0 & thisForm.p_create.value == "C"){	
	$("#dataCreate").css("color","#00aa00");
	$("#dataCreate").text(year+"년도 연도별입금내역 데이터 생성 완료");
	setTimeout("resultTime()", 3000);
}


var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
$(function(){
	$("#ses_search_gyear option:eq(0)").remove();	
	//$("#ses_search_gyear option:eq(1)").attr("selected","selected");	
});


/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	
	thisForm.viewPage.value = "";
	
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
	thisForm.action = "/adm/stm/yearMoneyTotalizationList.do";
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


/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	if($(".dataList").length == 0){
		alert("조회된 내역이 없습니다.");
		return;
	}	
	thisForm.viewPage.value = "E";
	thisForm.action = "/adm/stm/yearMoneyTotalizationList.do";
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

function resultTime(){
	$("#dataCreate").css("color","#ff0000");
	$("#dataCreate").text("");
}

function dataCreate(pageNo){
	
	if(!confirm(year+"년도 연도별입금내역의 데이터를 생성하면 \n기존에 등록된 내역은 삭제 됩니다. \n생성 하시겠습니까?(생성시 1분정도의 시간이 소요될 수 있습니다)"))return;
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	var timecnt = 0;
	setInterval(function(){ 
		if($("#dataCreate").text() == ""){
			$("#dataCreate").text("연도별입금내역 데이터 생성 중");
		}else{
			$("#dataCreate").text("");
		}	
		timecnt = timecnt+1;
	}, 1000);
	
	
	thisForm.action = "/adm/stm/yearReceiptMoneyStatus.do";	
	thisForm.pageIndex.value = pageNo;	
	thisForm.p_create.value="C";	
	thisForm.target = "_self";	
	thisForm.submit();	
}


function yearSave(pageNo){
	if($("#totalCnt").val() == 0){
		alert("조회된 내역이 없습니다.");
		return;
	}
	if(!confirm(year+"년도 연도별입금내역의 수정된 내역을 저장 하시겠습니까?"))return;
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/stm/yearReceiptMoneyStatusUpdate.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.p_create.value="U";
	thisForm.target = "_self";
	thisForm.submit();
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>