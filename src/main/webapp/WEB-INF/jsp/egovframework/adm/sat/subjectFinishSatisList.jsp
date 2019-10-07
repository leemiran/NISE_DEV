<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "com.ziaan.research.*" %>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<style>
.tdAlign {text-align:left, padding-left:20px}
</style>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<%
DecimalFormat  df = new DecimalFormat("0.0");

int     v_replycount  = 0;	
	if(request.getAttribute("p_replycount") != null){
		v_replycount  = Integer.parseInt(request.getAttribute("p_replycount") + "");
	}
int     v_studentcount= 0;
	if(request.getAttribute("p_replycount") != null){
		v_studentcount= Integer.parseInt(request.getAttribute("p_studentcount") + "");
	}

double  v_replyrate   = 0;
if (v_studentcount != 0) {
    v_replyrate = (double)Math.round((double)v_replycount/v_studentcount*100*100)/100;
}

ArrayList list = (ArrayList)request.getAttribute("SulmunResultList");
%>	

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  				name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 		name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 			name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="p_process" 				name="p_process">
	
	<input type="hidden" id="p_year" 					name="p_year">
	<input type="hidden" id="p_subj" 					name="p_subj">
	<input type="hidden" id="p_subjseq" 				name="p_subjseq">
	<input type="hidden" id="p_isgraduated" 			name="p_isgraduated">
	<input type="hidden" id="rd_gubun" 					name="rd_gubun" 				value="C">
	<input type="hidden" id="tab_gubun" 				name="tab_gubun" 				value="${tab_gubun}">
	<input type="hidden" id="search_att_name_value" 	name="search_att_name_value">	
	<input type="hidden" id="upperclass" 				name="upperclass">			
	
	<!-- 인쇄 파라메터 start-->
	<input type="hidden" id="print_subjnm" 				name="print_subjnm">			<!-- 과정명 -->
	<input type="hidden" id="print_edustart" 			name="print_edustart">			<!-- 연수기간 -->
	<input type="hidden" id="print_edutimes" 			name="print_edutimes">			<!-- 이수시간 및 학점 -->
	<input type="hidden" id="print_studentTotalCnt" 	name="print_studentTotalCnt">	<!-- 수강인원 -->
	<input type="hidden" id="print_isgraduatedY" 		name="print_isgraduatedY">		<!-- 이수인원 -->
	<input type="hidden" id="print_isgraduatedN" 		name="print_isgraduatedN">		<!-- 미이수인원 -->
	<input type="hidden" id="print_ischargeNm" 			name="print_ischargeNm">		<!-- 연수구분 -->
	<input type="hidden" id="print_upperclassNm" 		name="print_upperclassNm">		<!-- 연수구분 -->
	<input type="hidden" id="print_replycount" 			name="print_replycount">		<!-- 총응답자수 -->
	<input type="hidden" id="print_replyrate" 			name="print_replyrate">			<!-- 설문응시율 -->
	
	<!-- 인쇄 파라메터 end-->
	
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<div class="listTop">
                <div class="btnR MR05">
                <!-- <a href="#" onclick="subjectPrint()" class="btn01"><span>인쇄</span> -->
               		<a href="#none" onclick="whenXlsDownLoad1()" class="btn_excel"><span>엑셀출력(등재용)</span></a>
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>               		
                </div>
  </div>		
  <!-- tab menu -->        
		<div class="listTop">				
                <!-- tab -->
                <div class="conwrap2 MT20" style="margin-bottom:5px;">
                    <ul class="mtab2">
                    	<li><a href="#none" class="on"  		id="div_a_userEdu_1" name="div_a_userEdu_1" onClick="view_UserEduList(1)">결과보고서</a></li>
	                 	<li><a href="#none" 						id="div_a_userEdu_2" name="div_a_userEdu_2" onClick="view_UserEduList(2)">수강현황</a></li>
                        <li ><a href="#none"  	id="div_a_userEdu_3" name="div_a_userEdu_3" onClick="view_UserEduList(3)">전체설문결과분석</a></li>                    
                        </ul>
                </div>
                <!-- // tab-->      
	 	</div>
<!----------------- 이수현홍(이수자) B START ---------------------------------------------------------------------------------------->		
		<div id="tbl_userEduList_2" class="tbList"  style =  "width:auto; overflow:hidden; overflow-x:scroll; display:none;" >
		<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0"  width="100%"  >
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="*" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th>과정명</th>
                    <td style="text-align:left; padding-left: 20px;" id="subjnm_B">${view.subjnm} - ${view.subjseq}기, (이수등록 기수번호 : )</td>
                </tr>
                <tr class="title">
                    <th>연수기간</th>
                    <td style="text-align:left; padding-left: 20px;" id="edustart_B"><c:out value="${fn2:getFormatDate(view.edustart, 'yyyy년 MM월 dd일')}"/> ~ <c:out value="${fn2:getFormatDate(view.eduend, 'MM월 dd일')}"/></td>
                </tr>
                <tr class="title">
                    <th>이수시간 및 학점</th>
                    <td style="text-align:left; padding-left: 20px;" id="edutimes_B">${view.edutimes}시간 (${view.point}학점)</td>
                </tr>
                <tr class="title">
                    <th>수강인원</th>
                    <td style="text-align:left; padding-left: 20px;" id="studentTotalCnt_B">${view.studentTotalCnt} 명</td>
                </tr>
                <tr class="title">
                    <th>이수인원</th>
                    <td style="text-align:left; padding-left: 20px;" id="isgraduatedY_B"><c:out value="${view.isgraduatedY}"/> 명</td>
                </tr>
                <tr class="title">
                    <th>미이수인원</th>
                    <td style="text-align:left; padding-left: 20px;" id="isgraduatedN_B"><c:out value="${view.isgraduatedN}"/> 명</td>
                </tr>      
                 <!-- 20180425 주석 -->          
               <%--  <tr class="title">
                    <th>연수구분</th>
                    <td style="text-align:left; padding-left: 20px;" id="upperclassNm_B"><c:out value="${view.upperclassNm}"/>
                    	 <c:out value="${view.ischargeNm}"/> 
                    </td>
                </tr>
                <tr class="title">
                    <th>연수구분</th>
                    <td style="text-align:left; padding-left: 20px;" id="ischargeNm_B"><c:out value="${view.ischargeNm}"/>
                    	<c:choose>
                    		<c:when test="${view.upperclassNm eq  '교원직무연수'}">
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
                </tr> --%>
            </tbody>
        </table>
    </div>
    <!-- // detail wrap -->
			<table summary="" cellspacing="0" width="100%" border="1" style="margin-top: 10px">
                <caption>목록</caption>
                <colgroup>
				</colgroup>
				<thead>
					<tr>
						<th scope="row">연번</th>
						<th scope="row"><a href="#none" onclick="doOderList('ischarge')" >				연수구분</a>		</th>
						<!-- <th scope="row"><a href="#none" onclick="doOderList('pay')" >					납부방법</a>		</th> -->
						<th scope="row"><a href="#none" onclick="doOderList('isgraduated')" >		이수구분</a>		</th>
						<th scope="row"><a href="#none" onclick="doOderList('dept_nm')" >			시도교육청</a>		</th>
						<th scope="row"><a href="#none" onclick="doOderList('agency_nm')" >			하위교육청</a>		</th>
						<th scope="row"><a href="#none" onclick="doOderList('user_path')" >			소속</a>				</th>
						<th scope="row"><a href="#none" onclick="doOderList('emp_gubun_nm')" >	회원<br>구분</a>	</th>
						<th scope="row"><a href="#none" onclick="doOderList('name')" >					성명</a>				</th>
						<th scope="row"><a href="#none" onclick="doOderList('nice_personal_num')" >NEIS개인번호</a></th>
						<!-- <th scope="row">																				연수지명번호		</th> -->
						<th scope="row"><a href="#none" onclick="doOderList('birth_date')" >			생년월일</a>		</th>
						<th scope="row">																				연수과정명			</th>
						<th scope="row"><a href="#none" onclick="doOderList('compnm')" >				연수기관명</a>		</th>
						<th scope="row">																				연수<br>시작일		</th>
						<th scope="row">																				연수<br>종료일		</th>
						<th scope="row"><a href="#none" onclick="doOderList('upperclassNm')" >		연수<br>구분</a>	</th>
						<th scope="row">																				연수<br>시간		</th>
						<th scope="row"><a href="#none" onclick="doOderList('score')" >					연수<br>성적</a>	</th>
						<th scope="row"><a href="#none" onclick="doOderList('upperclassnm')" >		직무<br>관련</a>	</th>
						<th scope="row"><a href="#none" onclick="doOderList('point')" >					학점</a>				</th>
						<th scope="row"><a href="#none" onclick="doOderList('serno')" >					이수번호</a>		</th>
						<th scope="row">합격증번호</th>
					</tr>
				</thead>
				<tbody>
				
<c:forEach items="${list_b}" var="result" varStatus="status" >
					<tr>
						<td class="num">
							<c:out value="${status.count}"/>
						</td>
						<td>
							<c:if test="${result.ischarge eq 'C'}">
							일반
							</c:if>
							<c:if test="${result.ischarge eq 'S'}">
							특별
							</c:if>
							<c:if test="${result.ischarge eq 'F'}">
							무료
							</c:if>
						</td>
						<%-- <td><c:out value="${result.pay}"/></td> --%>
						<td>
						<c:if test="${result.isgraduated eq 'Y'}">	이수</c:if>
						<c:if test="${result.isgraduated ne 'Y'}">미이수</c:if>
						</td>
						<td><c:out value="${result.deptNm}"/></td>
						<td><c:out value="${result.agencyNm}"/></td>
						<td><%-- ${result.empGubunNm} 교: ${result.userPath} 일: ${result.positionNm}<br/> --%>
							<c:if test="${result.empGubunNm eq '교원' || result.empGubunNm eq '보조인력'}">
								<c:if test="${fn:length(result.userPath) == 0}"> -</c:if>
						      <c:if test="${fn:length(result.userPath) > 0}"> <c:out value="${result.userPath}"/></c:if>
							</c:if>
							
							<c:if test="${result.empGubunNm ne '교원' &&  result.empGubunNm ne '보조인력'}">
								<c:if test="${fn:length(result.positionNm) == 0}"> -</c:if>
						      <c:if test="${fn:length(result.positionNm) > 0}"> <c:out value="${result.positionNm}"/></c:if>
							</c:if>
							
						
						</td>
												
						<td><c:out value="${result.empGubunNm}"/></td>
						
						<td><c:out value="${result.name}"/></td>
						<td><c:out value="${result.nicePersonalNum}"/></td>
						<%--<td><c:if test="${result.lecSelNo eq 'null'}"></c:if> 
							<c:if test="${result.lecSelNo ne 'null'}"><c:out value="${result.lecSelNo}"/></c:if>							
						</td> --%>
						<td><c:out value="${fn2:getFormatDate(result.birthDate, 'yyyy.MM.dd')}"/></td>
						<td align="left"><c:out value="${view.subjnm}"/></td>
						<td align="left"><c:out value="${result.compnm}"/></td>
						<td><c:out value="${fn2:getFormatDate(view.edustart, 'yyyy.MM.dd')}"/></td>
						<td><c:out value="${fn2:getFormatDate(view.eduend, 'yyyy.MM.dd')}"/></td>
						<td>
							<%-- <c:if test="${not empty result.serno && result.upperclassnm ne '일반연수'}">
							직무
							</c:if>
							<c:if test="${not empty result.serno && result.upperclassnm eq '일반연수'}">
							-
							</c:if>	
							
							<c:if test="${empty result.serno}">
							-
							</c:if>	 --%>
							
							<!-- 20180425 주석 -->
							<%-- <c:choose>
                    		<c:when test= "${view.upperclassNm eq  '교원직무연수'}">
                    			직무
                    		</c:when>
                    		<c:when test="${view.upperclassNm eq  '보조인력연수'}">
                    			보조
                    		</c:when>
                    		<c:otherwise>
                    			${view.upperclassNm}
                    		</c:otherwise>
                    		</c:choose> --%>
                    		<!-- 20180425 수정 -->
                    		${result.ysGubun}
							
						</td>
						<td><c:out value="${view.edutimes}"/></td>
						<td>
						
						<c:if test="${view.edutimes > 60}">
							<c:out value="${result.editscore}"/>
						</c:if>
						<c:if test="${view.edutimes <= 60}">
							<c:out value="${result.score}"/>
						</c:if>
						
						</td>
						<td>
							<%-- <c:if test="${not empty result.serno && result.upperclassnm ne '일반연수'}">
							Y
							</c:if>
							<c:if test="${not empty result.serno && result.upperclassnm eq '일반연수'}">
							N
							</c:if>	
							
							<c:if test="${empty result.serno}">
							N
							</c:if>	 --%>
							
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
						
						<c:if test="${not empty result.serno}">
							${result.isuNo}
						</c:if>
						
							
						<c:if test="${empty result.serno}">
						-
						</c:if>
						</td>
						<td>${result.certificateNo}</td>
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
<!----------------- 이수현홍(이수자) END ---------------------------------------------------------------------------------------->	   
        
<!----------------- 전체설문결과분석(미이수자) C START--------------------------------------------------------------------------->
	
<div id="tbl_userEduList_3" class="tbList" style =  "width:auto; overflow:hidden; overflow-x:scroll; display:none;" >

<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0"  width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="*" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th>과정명</th>
                    <td style="text-align:left; padding-left: 20px;" id="subjnm_C">${view.subjnm} - ${view.subjseq}기</td>
                </tr>
                <tr class="title">
                    <th>연수기간</th>
                    <td style="text-align:left; padding-left: 20px;" id="edustart_C"><c:out value="${fn2:getFormatDate(view.edustart, 'yyyy년 MM월 dd일')}"/> ~ <c:out value="${fn2:getFormatDate(view.eduend, 'MM월 dd일')}"/></td>
                </tr>
                <tr class="title">
                    <th>이수시간 및 학점</th>
                    <td style="text-align:left; padding-left: 20px;" id="edutimes_C">${view.edutimes}시간 (${view.point}학점)</td>
                </tr>
                <tr class="title">
                    <th>수강인원</th>
                    <td style="text-align:left; padding-left: 20px;" id="studentTotalCnt_C">${view.studentTotalCnt} 명</td>
                </tr>
                <tr class="title">
                    <th>이수인원</th>
                    <td style="text-align:left; padding-left: 20px;" id="isgraduatedY_C"><c:out value="${view.isgraduatedY}"/> 명</td>
                </tr>
                <tr class="title">
                    <th>미이수인원</th>
                    <td style="text-align:left; padding-left: 20px;" id="isgraduatedN_C"><c:out value="${view.isgraduatedN}"/> 명</td>
                </tr>
                 <tr class="title">
                    <th>총응답자수</th>
                    <td style="text-align:left; padding-left: 20px;" id="replycount_C"><%=v_replycount%> 명</td>
                </tr>
                 <tr class="title">
                    <th>설문응시율</th>
                    <td style="text-align:left; padding-left: 20px;" id="replyrate_C"><%=df.format(v_replyrate)%>%</td>
                </tr>
            </tbody>
        </table>
    </div>
   
   <!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="40px"/>		      
		      <col width="10%"/>
		      <col width="20%"/>
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
	        <thead>
		      <tr>
		        <th rowspan="2">순번</th>
		        <th rowspan="2">평가영역</th>
		        <th rowspan="2">평가문항</th>
		        <th colspan="2">매우 불만족(1점)</th>		        
		        <th colspan="2">불만족(2점)</th>		        
		        <th colspan="2">보통(3점)</th>		        
		        <th colspan="2">만족(4점)</th>		       
		        <th colspan="2">매우만족(5점)</th>		       
		        <th rowspan="2">응답인원(명)</th>
		        <th colspan="2">평점(점)</th>		   
	          </tr>
	          <tr>		       
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>		        
		        <th >평점(점)</th>
		        <th >백분율(%)</th>
	          </tr>
	        </thead>
<%  
System.out.print("list ; "+list);
    SulmunQuestionExampleData data    = null;
    SulmunExampleData         subdata = null;
	int k = 0;
	int l = 0;
	int questNumber = 0;
	int sulCount = 0;
	double sulAvg = 0; 
	double sulAvgPer = 0;
	
	int vcnt = 0; 
	
	if(list != null){
    for (int i=0; i < list.size(); i++) {
        data = (SulmunQuestionExampleData)list.get(i);
           if (data.getSultype().equals(SulmunSubjBean.SUBJECT_QUESTION) && false) {	//단답형
        	   questNumber++;
%>	        
            <tbody>
		      <tr>
		        <td scope="row">문제<%=questNumber%></td>
		        <td scope="row"><%=data.getDistcodenm()%></td>
		        <td colspan="14" scope="row" class="left"><%=data.getSultext()%></td>
	          </tr>
		    
<%        int m = 0;   
			if(v_replycount > data.getSubjectAnswer().size())	v_replycount = data.getSubjectAnswer().size();
			for (int j=0; j < data.getSubjectAnswer().size(); j++) {
				if(((data.getSubjectAnswer().size() / v_replycount)*m + (k+1)) == (j+1)) {
					if(m < v_replycount) m++;
%>		    
		      <tr>
		        <td class="num"></td>
		        <td class="num"></td>
		        <td class="left" colspan="14"><%=(String)data.getSubjectAnswer().get(j)%></td>
	          </tr>
	        </tbody>
<%           }   
		   }
           k++;	
       } else if (data.getSultype().equals(SulmunSubjBean.FSCALE_QUESTION)) {			//객관식5
    	   	questNumber++;
	          double d = 0; 
			  int person = 0;
			  double v_point = 0;
      
		   for (int j=1; j <= data.size(); j++) {
	            subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 

					d +=  (subdata.getReplycnt()) * subdata.getSelpoint();
					person += subdata.getReplycnt();
				}
            }	
               
		v_point = d / person;	  
		
		sulCount++;
		sulAvg += v_point;
		
		sulAvgPer += (v_point/5)*100;
		
%>
		    <tbody>
		      <tr>
		        <td >문제<%=questNumber%></td>
		        <td ><%=data.getDistcodenm()%></td>
		        <td  class="left"><%=data.getSultext()%></td>
	        
<%
		 int cnt = 0;
		   for (int j=1; j <= data.size(); j++) {
                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) {
                cnt 	+= subdata.getReplycnt();
                %>	        		       
		        <td class="num"><%=subdata.getReplycnt()%>명</td>
		        <td class="num"><%=df.format(subdata.getReplyrate())%>%</td>
<%           }    
             vcnt =j;   
            }	
%>		 <td ><%=cnt%>명</td>
			<td ><%if(v_point >=0){%><%=df.format(v_point)%>점<%}%></td>
			<td ><%if(v_point >=0){%><%=df.format((v_point/5)*100)%><%}%></td>
	          </tr>
	        </tbody>
<%
       } 
			
    } 
    }
%>
	      <tr>
	      	<td colspan="<%=(vcnt*2)+4 %>"></td>
	      	<td><%if(sulAvg >=0){%><%=df.format((sulAvg/questNumber))%><%}%></td>
	      	<td><%if(sulAvg >=0){%><%=df.format((sulAvgPer/questNumber))%><%}%></td>
	      </tr>      
	        
	      </table>
		</div>
			<br/>
		<!-- list table-->
		<div class="sub_tit">
			<h4 id="sulScoreTotalH4">교육후기</h4>	
		</div>
		
		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="10%"/>		      
		      <col width="10%"/>
		      <col width=""/>
	        </colgroup>
	        <thead>
		      <tr>
		        <th scope="row">아이디</th>
		        <th scope="row">이름</th>
		        <th scope="row" class="left">교육후기</th>
	          </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${hukiList}" var="result" varStatus="status">	
	          <tr>
	          	<td>${result.userid }</td>
	          	<td>${result.name }</td>
	          	<td style="text-align:left">${result.comments }</td>
	          </tr>
	        </c:forEach>
	        </tbody>
	      </table>
	    </div>
  </div>
		<!-- list table-->
<!----------------- 전체설문결과분석(미이수자)  END---------------------------------------------------------------------------------------->			

<!----------------- 결과보고서(이수현황) A  START---------------------------------------------------------------------------------------->
		<div id="tbl_userEduList_1" class="tbList" >
		<!-- detail wrap -->
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="4%" />
                <col width="4%"/>
                <col width="4%"/>
                <col width="4%" />
                <col width="21%"/>
                <col width="21%"/>
                <col width="21%"/>
                <col width="21%"/>
            </colgroup>
            <tbody>
                <tr >
                 	<td >연수<br>구분</td><!-- 과정분류(0001) -->
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
                    <td >운영<br>형태</td><!-- 수강료 납부방식 -->
                    <td><c:out value="${view.ischargeNm}"/></td>
                    <td colspan="4"><h3><c:out value="${view.subjnm}"/> - <c:out value="${view.subjseq}"/>기</h3></td>
                </tr>
                <tr >
                	<td colspan="8" style="text-align:right; padding-right: 20px;">담당자 : <c:out value="${view.musernm}"/></td>
                </tr>
                <tr >
                    <td colspan="4">과 정 명</td>
                    <td colspan="4" id="subjnm_A"><c:out value="${view.subjnm}"/></td>
                </tr>
                <tr >
                    <td colspan="4">기     간</td>
                    <td colspan="4" id="edustart_A"><c:out value="${fn2:getFormatDate(view.edustart, 'yyyy년 MM월 dd일')}"/> ~ <c:out value="${fn2:getFormatDate(view.eduend, 'yyyy년 MM월 dd일')}"/></td>
                </tr>
                <tr >
                    <td colspan="4">수강인원</td>
                    <td colspan="4" id="studentTotalCnt_A"><c:out value="${view.studentTotalCnt}"/> 명</td>
                </tr>
<c:set var="payCnt" value="0" />   
<c:set var="chkfinalCnt" value="0" />   
<c:set var="biyong" value="0" />
<c:set var="p_isgraduatedN" value="0" />
 <!-- 이수현황 START -->   
 <c:forEach items="${list_a}" var="result" varStatus="status"> 
                <tr  <c:if test="${result.subjseqLag ne result.subjseq}">style="border-top: 2px solid #bcbcbc"</c:if>>
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
			         		<td colspan="2" >${result.payGubun}</td>
			         	</c:if>	
			         	
			         	<c:if test="${result.payGubun ne '개인'}">
			         		 <c:if test="${result.gubunCnt == 1 }">
			         		 	<td colspan="2" rowspan="2">${result.payGubun}</td>
			         		 	
			         		 </c:if>
			         		 <c:if test="${result.gubunCnt > 1 }">
			         		 	<td colspan="2"  rowspan="${result.gubunCnt+1}" >${result.payGubun}</td>
			         		 </c:if>
			         			
			         	</c:if>
			      	</c:if>
			      	
			      	<c:if test="${result.gubunCnt ne '1'}">
			        	<td rowspan="${result.gubunCnt+1}"  colspan="2">${result.payGubun}</td>	
			      	</c:if>
		        </c:if>
                
                <c:if test="${result.payGubun ne '개인'}">
                	<c:if test="${result.gubunCnt > 1}">
			        	<td>${result.deptNm} </td>
			        </c:if>
			        <c:if test="${result.gubunCnt == 1}">
			        	<td>${result.deptNm}</td>
			        </c:if>	
			    </c:if>
			    
                <c:if test="${result.payGubun eq '개인'}">
                	<td  colspan="2">${result.isgraduatedYDeptcnt} 명 </td>
                	<c:set var="p_isgraduatedYDeptcnt" value="${result.isgraduatedYDeptcnt}" />
                </c:if>
                
                <c:if test="${result.payGubun ne '개인'}">
                	<td  colspan="2">${result.isgraduatedYDeptcnt} 명 </td>                    	
                </c:if>
                    
                </tr>
                
                <c:if test="${result.deptGubunLead ne result.payGubun && result.payGubun ne '개인'}">	               	
		                <tr >		                
		                 	<td style="background-color: #f5f5f5;">계</td>
		                 	<td style="background-color: #f5f5f5;">${result.isgraduatedYGubun} 명</td>	
		                </tr>
		                <tr >		                
		                 	<td  colspan="2" style="background-color: #f5f5f5;">총 이수인원</td>
		                 	<td  colspan="2" style="background-color: #f5f5f5;">${result.isgraduatedYGubun+p_isgraduatedYDeptcnt} 명</td>	
		                </tr>
                </c:if>
</c:forEach>   
<c:forEach items="${list_a}" var="result" varStatus="status">            
                <tr  >
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
				         	<td colspan="2">${result.payGubun}</td>	
				         </c:if> 
				         
				         <c:if test="${result.payGubun ne '개인'}">
				         	<td colspan="2" rowspan="2">${result.payGubun}</td>	
				         </c:if>
				          
			      	</c:if>
			      	
			      	<c:if test="${result.gubunCnt ne '1'}">
			        	<td rowspan="${result.gubunCnt+1}"  colspan="2">${result.payGubun} </td>	
			      	</c:if>
		         </c:if>
		         
			     <c:if test="${result.payGubun ne '개인'}">
			     	<td >${result.deptNm}</td>	
			     </c:if>			       
			     
                 <td  <c:if test="${result.payGubun eq '개인'}">colspan='2'</c:if>>${result.isgraduatedNDeptcnt} 명
                 		<c:if test="${result.payGubun eq '개인' && p_isgraduatedN == 0}"> 
                 			<c:set var="p_isgraduatedNDeptcnt" value="${result.isgraduatedNDeptcnt}" />
                 			<c:set var="p_isgraduatedN" value="1" />
                 		</c:if>                 		
                 </td>
                </tr>
                               
                <c:if test="${result.deptGubunLead ne result.payGubun && result.payGubun ne '개인'}">             
	            
		                <tr >
		                 	<td  style="background-color: #f5f5f5;">계</td>
		                 	<td  style="background-color: #f5f5f5;">${result.isgraduatedNGubun} 명</td>	
	                	</tr>
	                	<tr >
	                		<td colspan="2" style="background-color: #f5f5f5;">총 미이수인원</td>
		                 	<td colspan="2" style="background-color: #f5f5f5;">${result.isgraduatedNGubun+p_isgraduatedNDeptcnt} 명</td>	
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
          
         
          
                <tr  <c:if test="${result.subjseqLag ne result.subjseq}">style="border-top: 2px solid #bcbcbc"</c:if>>
                 <c:if test="${result.subjseqLag ne result.subjseq}">
                <td  rowspan="${(result.allSubjseqCnt*2)+2}">입금현황</td>	
                </c:if>
                 <c:if test="${result.deptGubunLag ne result.payGubun}">
		         		<td rowspan="${result.gubunCnt+1}" colspan="3">${result.payGubun}</td>	
		      	 </c:if>
		      	  	<c:if test="${result.payGubun eq '개인'}">
			      		<td >${result.pay} :</td>	
			      		<td >${result.chkfinalCnt} 명 * </td>
			      			
			      		<c:if test="${result.pay eq '재수강'}" >
	                		<td >0 원</td>
	                	</c:if>		
			      		<c:if test="${result.pay ne '재수강'}" >
			      			<td ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /> 원 = </td>
			      		</c:if>	                		                	
	                	<c:if test="${result.pay eq '재수강'}" >
	                		<td >0 원</td>
		                	<c:set var="re_type" value="${result.chkfinalCnt * result.biyong}" />		                	
		                	<c:set var="re_type_cnt" value="${result.chkfinalCnt}" />
		                </c:if>
		                <c:if test="${result.pay ne '재수강'}" >
	                		<td ><fmt:formatNumber type="currency" value="${result.chkfinalCnt * result.biyong}" pattern="###,###" /> 원</td>
	                		<c:set var="chkfinalCnt_biyong" value="${chkfinalCnt_biyong + (result.chkfinalCnt * result.biyong)}" />		                			                	
		                </c:if>
		                
			      	</c:if>
			      	<c:if test="${result.payGubun ne '개인'}">
			      		<td >${result.deptNm} :</td>	
			      		<td >${result.payCnt} 명 * </td>
			      		<td ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /> 원 = </td>	
	                	<td ><fmt:formatNumber type="currency" value="${result.payCnt * result.biyong}" pattern="###,###" /> 원
	                		<c:set var="paycnt_biyong"  value ="${paycnt_biyong + (result.payCnt * result.biyong)}" />
	                	</td>
			      	</c:if>
		         	
		         	
                </tr>
                <c:if test="${result.deptGubunLead ne result.payGubun && result.payGubun eq '개인'}">
	                <tr style="background-color: #f5f5f5;">
	                 	<td >${result.payGubun}(징수) :</td>	
			         	<td >${result.chkfinalDeptCnt-re_type_cnt} 명*
			         	<c:set var="chkfinalCnt" value="${chkfinalCnt+result.chkfinalDeptCnt}" /> 
			         	<c:set var="chkfinalAmount" value="${(result.chkfinalDeptCnt * result.biyong)-(fe_type_cnt * result.biyong)-re_type}" />
			         	</td>	
			         	<td ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /> 원 = <c:set var="biyong" value="${result.biyong}" />    </td>		
		                <td ><fmt:formatNumber type="currency" value="${(result.chkfinalDeptCnt * result.biyong)-(fe_type_cnt * result.biyong)-re_type}" pattern="###,###" /> 원 </td>
		                
	                </tr>
                </c:if>
                <c:if test="${result.deptGubunLead ne result.payGubun && result.payGubun ne '개인'}">
	                <tr style="background-color: #f5f5f5;">
	                 	<td >${result.payGubun}(징수) :</td>	
			         	<td >${result.gubunTotalCnt} 명 *
			         	<c:set var="payCnt" value="${payCnt+result.gubunTotalCnt}" />
			         	<c:set var="payCntAmount" value="${result.biyong * result.gubunTotalCnt}" />    
			         	</td>
			         	<c:if test="${result.biyong3Cnt == 0}">			         		
				         	<td ><fmt:formatNumber type="currency" value="${result.biyong}" pattern="###,###" /> 원 = <c:set var="biyong" value="${result.biyong}" /></td>		
			                <td ><fmt:formatNumber type="currency" value="${result.gubunAmount}" pattern="###,###" /> </td>
			                <td ><fmt:formatNumber type="currency" value="${result.biyong * result.gubunTotalCnt}" pattern="###,###" /> 원</td>
		                </c:if>
		                <c:if test="${result.biyong3Cnt > 0}">
		                	<td ></td>			                
			                <td ><fmt:formatNumber type="currency" value="${paycnt_biyong}" pattern="###,###" /> 원</td>
		                </c:if>
	                </tr>
                </c:if>
</c:forEach>   
					<tr style="background-color: #e5e5e5;">
	                 	<td rowspan="2" colspan="3">총 계</td>	
			         	<td colspan="4">총징수인원 : ${payCnt+chkfinalCnt-re_type_cnt} 명</td>	
	                </tr>
	               
	                <tr style="background-color: #e5e5e5;">
	                	<c:if test="${p_biyong3Cnt == 0}">
	                		<td colspan="4">총징수금액 : <fmt:formatNumber type="currency" value="${chkfinalAmount+payCntAmount}" pattern="###,###" /> 원</td>
	                	</c:if>
	                	<c:if test="${p_biyong3Cnt > 0}">
	                		<td colspan="4">총징수금액 : <fmt:formatNumber type="currency" value="${chkfinalCnt_biyong+paycnt_biyong}" pattern="###,###" /> 원</td>
	                	</c:if>
			         		
	                </tr>
	                --%>
 
 <!-- 입금현황 END -->                  
            </tbody>
        </table>
    <!-- // detail wrap -->
    </div>
			<br/>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" border="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				</colgroup>
				<thead>
					<tr>
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
				<tr height="35">
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
		
</form>
<br>
<br>
<br>
<br>
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

if($("#tab_gubun").val() == "")$("#tab_gubun").val("A");
if($("#tab_gubun").val() == "A"){
	document.all("tbl_userEduList_1").style.display = "block";
	document.all("tbl_userEduList_2").style.display = "none";
	document.all("tbl_userEduList_3").style.display = "none";
	document.getElementById("div_a_userEdu_1").className = "on";
	document.getElementById("div_a_userEdu_2").className = "";
	document.getElementById("div_a_userEdu_3").className = "";
}else if($("#tab_gubun").val() == "B"){
	document.all("tbl_userEduList_1").style.display = "none";
	document.all("tbl_userEduList_2").style.display = "block";
	document.all("tbl_userEduList_3").style.display = "none";
	document.getElementById("div_a_userEdu_1").className = "";
	document.getElementById("div_a_userEdu_2").className = "on";
	document.getElementById("div_a_userEdu_3").className = "";
}
else if($("#tab_gubun").val() == "C"){
	document.getElementById("tbl_userEduList_1").style.display = "none";
	document.getElementById("tbl_userEduList_2").style.display = "none";
	document.getElementById("tbl_userEduList_3").style.display = "block";
	document.getElementById("div_a_userEdu_1").className = "";
	document.getElementById("div_a_userEdu_2").className = "";
	document.getElementById("div_a_userEdu_3").className = "on";
}

/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
 function whenXlsDownLoad() {
		var v = $("#ses_search_subjseq").val();
		if(v == "")
		{
			alert("과정-기수를 검색하셔야 엑셀출력이 가능합니다.");
			return;
		}
		thisForm.upperclass.value="";
		thisForm.action = "/adm/sat/subjectFinishSatisExcelDown.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
 function whenXlsDownLoad1() {
		var v = $("#ses_search_subjseq").val();
		if(v == "")
		{
			alert("과정-기수를 검색하셔야 엑셀출력이 가능합니다.");
			return;
		}
		thisForm.upperclass.value="PRF";
		thisForm.action = "/adm/sat/subjectFinishSatisExcelDown1.do";
		thisForm.target = "_self";
		thisForm.submit();
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
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/sat/subjectFinishSatisList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

//통계 RD 출력
function subjectPrint(){
	
	$("#print_subjnm").val($("#subjnm_"+$("#tab_gubun").val()).text());
	$("#print_edustart").val($("#edustart_"+$("#tab_gubun").val()).text());
	$("#print_edutimes").val($("#edutimes_"+$("#tab_gubun").val()).text());
	$("#print_studentTotalCnt").val($("#studentTotalCnt_"+$("#tab_gubun").val()).text());
	$("#print_isgraduatedY").val($("#isgraduatedY_"+$("#tab_gubun").val()).text());
	$("#print_isgraduatedN").val($("#isgraduatedN_"+$("#tab_gubun").val()).text());

	if($("#tab_gubun").val() == "A"){
		
	}else if($("#tab_gubun").val() == "B"){
		
		$("#print_ischargeNm").val($("#ischargeNm_"+$("#tab_gubun").val()).text());
		$("#print_upperclassNm").val($("#upperclassNm_"+$("#tab_gubun").val()).text());
		
	}else if($("#tab_gubun").val() == "C"){
		
		$("#print_replycount").val($("#replycount_"+$("#tab_gubun").val()).text());
		$("#print_replyrate").val($("#replyrate_"+$("#tab_gubun").val()).text());
		
	}
	
	window.open('', 'subjectPop', 'left=100,top=100,width=1050,height=550,scrollbars=yes');
	thisForm.search_att_name_value.value=$("#ses_search_att option:selected").text();
	thisForm.action = "/adm/sat/subjectFinishSatisPrint.do";
	thisForm.target = "subjectPop";
	thisForm.submit();
}

function view_UserEduList(num)
{
	if(num == 1)
	{
		$("#tab_gubun").val("A");
		document.all("tbl_userEduList_1").style.display = "block";
		document.all("tbl_userEduList_2").style.display = "none";
		document.all("tbl_userEduList_3").style.display = "none";

		document.getElementById("div_a_userEdu_1").className = "on";
		document.getElementById("div_a_userEdu_2").className = "";
		document.getElementById("div_a_userEdu_3").className = "";
		doPageList();
	}

	if(num == 2)
	{
		$("#tab_gubun").val("B");
		document.all("tbl_userEduList_1").style.display = "none";
		document.all("tbl_userEduList_2").style.display = "block";
		document.all("tbl_userEduList_3").style.display = "none";
		
		document.getElementById("div_a_userEdu_1").className = "";
		document.getElementById("div_a_userEdu_2").className = "on";
		document.getElementById("div_a_userEdu_3").className = "";
		doPageList();
	}

	if(num == 3)
	{
		$("#tab_gubun").val("C");
		document.getElementById("tbl_userEduList_1").style.display = "none";
		document.getElementById("tbl_userEduList_2").style.display = "none";
		document.getElementById("tbl_userEduList_3").style.display = "block";

		document.getElementById("div_a_userEdu_1").className = "";
		document.getElementById("div_a_userEdu_2").className = "";
		document.getElementById("div_a_userEdu_3").className = "on";
		doPageList();
	}
	//alert($("#tab_gubun").val());
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/sat/subjectFinishSatisList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//-->
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
