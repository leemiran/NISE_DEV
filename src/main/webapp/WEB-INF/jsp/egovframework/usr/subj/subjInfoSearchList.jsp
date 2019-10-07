<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
		<input type="hidden"  name="p_subj" value=""/>
		<input type="hidden"  name="p_year" value=""/>
		<input type="hidden"  name="p_subjseq" value=""/>
		<input type="hidden"  name="p_subjnm" value=""/>
		<input type="hidden"  name="p_process" value=""/>
		<input type="hidden" name="p_course" value=""/>
		<input type="hidden" name="search_knise_subj_module" value="allSearch"/>
				
				
				
				<!-- search wrap-->
                <div class="courseWrapSubj" style="height:100px;">
                	<ul>
                        <li>
                        	<!-- 연수구분 -->
                        	연수구분
                            <select name="search_upperclassgroup" title="연수구분">
                             		<option value="">전체</option>
                             		<option value="PRF" <c:if test="${search_upperclassgroup eq 'PRF'}">selected</c:if>>교원</option>                             		
                             		<option value="EXT" <c:if test="${search_upperclassgroup eq 'EXT'}">selected</c:if>>보조</option>
                             		<option value="PAR" <c:if test="${search_upperclassgroup eq 'PAR'}">selected</c:if>>교양</option>                             		
                             		<option value="SCP" <c:if test="${search_upperclassgroup eq 'SCP'}">selected</c:if>>학부모</option>
                             </select>
                         </li>
                         <li>
                         	운영형태
                         	<select name="search_ischarge" title="운영형태">
                             		<option value="">전체</option>
                             		<option value="C" <c:if test="${search_ischarge eq 'C'}">selected</c:if>>정규</option>
                             		<option value="S" <c:if test="${search_ischarge eq 'S'}">selected</c:if>>특별</option>                             		
                             		<%-- <option value="F" <c:if test="${search_ischarge eq 'F'}">selected</c:if>>무료</option> --%>                             		
                             </select>
                         </li>
                         <li>
                         	연수시간
                         	<select name="search_time" title="연수시간">
                             		<option value="">전체</option>
                             		<option value="N" <c:if test="${search_time eq 'N'}">selected</c:if>>45시간이하</option>
                             		<option value="Y" <c:if test="${search_time eq 'Y'}">selected</c:if>>60시간이상</option>
                             </select>
                         </li>
                         <li>
                         	모바일지원여부
                         	<select name="search_mobile" title="모바일지원여부">
                             		<option value="">전체</option>
                             		<option value="Y" <c:if test="${search_mobile eq 'Y'}">selected</c:if>>Yes</option>
                             		<option value="N" <c:if test="${search_mobile eq 'N'}">selected</c:if>>No</option>
                             </select>
                         </li>
                    </ul>   
                    <ul>
                         <li>연수기간
                         
                         <c:set var="nowYear">${fn2:getFormatDateNow('yyyy')}</c:set>
                         <c:set var="nowMonth">${fn2:getFormatDateNow('MM')}</c:set>
                         
                         
                            <select name="search_year" title="연도선택">
                            	<option value="">연도전체</option>
	                            <c:forEach var="i" begin="${nowYear-1}" end="${nowYear+1}" step="1">
	                            		<c:set var="str">${i}</c:set>
	                                    <option value="${str}"
	                                    <c:if test="${not empty search_year && str == search_year}">selected</c:if>
	                                    <c:if test="${empty search_year && str == nowYear}">selected</c:if>
	                                    >${str}년</option>
	                            </c:forEach>                                
                            </select> 
                                
                              /
                               
                             <select name="search_month" title="월선택">
                             		<option value="">월전체</option>
	                             <c:forEach var="j" begin="1" end="12" step="1">
	                             	<c:if test="${j < 10}">
	                             		<c:set var="str">0${j}</c:set>
	                             		<option value="${str}"
	                             		<c:if test="${not empty search_month && (str) == search_month}">selected</c:if>
	                             		>${str}월</option>
	                             	</c:if>
	                             	<c:if test="${j >= 10}">
	                             		<c:set var="str">${j}</c:set>
	                             		<option value="${str}"
	                             		<c:if test="${not empty search_month && (str) == search_month}">selected</c:if>
	                             		>${str}월</option>
	                             	</c:if>
	                            </c:forEach>
                             </select> 
                             
                             
                         </li>
                         <li>
                         과정명 
                         	<input name="search_text" type="text" size="48" value="${search_text}" onkeydown="fn_keyEvent('doSearchList')" title="검색어"/>
                             
							 <a href="#none" onclick="doSearchList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
                         </li>
                    </ul>            
                </div>
                <!-- // search wrap -->
                
                
                
 
                <div class="mb5">                    
                    <span><img src="/images/user/btn_new.gif" alt="신규" /></span>            
                    <span class="pl5"><img src="/images/user/btn_choose.gif" alt="추천" /></span>
                </div>
                <div class="mb5">                    
                    <span><img src="/images/user/upperclass_info.gif" alt="연수종별" /></span>            
                </div>
                
                <!-- list table-->
                    <div class="studyList">
                        <table summary="패키지, 과정명, 연수기간, 수강여부로 구성" cellspacing="0" width="100%">
                            <caption>교육분류</caption>
                            <colgroup>
                            	<col width="6%" />
                                <col width="6%" />
                                <col width="6%" />
                                <col width="%" />
                                <col width="10%" />
                                <col width="10%" />
                                <col width="6%" />
                                <col width="6%" />
                                <col width="8%" />
                            </colgroup>
                            <thead>
                                <tr>
                                	<th scope="col">기별</th>
                                	<th scope="col">연수<br>구분</th>
                                    <th scope="col">운영<br>형태</th>
                                    <th scope="col">과정명</th>
                                    <th scope="col">등록기간</th>
                                    <th scope="col">연수기간</th>
                                    <th scope="col">시간</th>
                                    <th scope="col">모바일<br>지&nbsp;&nbsp;원</th>
                                    <th scope="col" class="last">수강여부</th>
                                    
                                </tr>
                            </thead>
                            <tbody>	


<c:set var="rownumCnt" value="0"/>
<c:set var="rownumCnt1" value="0"/>
 
<c:forEach items="${list}" var="result" varStatus="status">
		
                                <tr>
<c:if test="${result.coursecount > 0 && rownumCnt == 0}">
<!--  			<td rowspan="${result.coursecount}" colspan="3" class="left">
										<c:if test="${result.course ne '000000'}">
											<span style="color:#3366cc;">${result.coursenm}</span>
										</c:if>
										<c:if test="${result.course eq '000000'}">
											${result.upperclassnm}
										</c:if>
                                    </td>
-->
									<td  rowspan="${result.coursecount}" >${fn2:toNumber(result.subjseq)}</td>
									<td  rowspan="${result.coursecount}" >
										<c:if test="${result.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
										<c:if test="${result.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
										<c:if test="${result.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
										<c:if test="${result.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
										<c:if test="${result.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
                                    </td>
                                    <td  rowspan="${result.coursecount}" >
	                                    <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
	                                    <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
	                                    <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
                                    </td>
	                                    
</c:if>


<c:if test="${result.coursecount > 0}">
		<c:set var="rownumCnt" value="${rownumCnt+1}"/>
</c:if>


<c:if test="${result.coursecount <= 0}">
									<td>${fn2:toNumber(result.subjseq)}</td>
									<td>
										<c:if test="${result.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
										<c:if test="${result.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
										<c:if test="${result.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
										<c:if test="${result.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
										<c:if test="${result.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
                                    </td>
                                    <td>
	                                    <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
	                                    <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
	                                    <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
                                    </td>
                                    
</c:if>   

                                    <td class="left">
<!--                                    [${result.subj}]-->
                                    <a href="#none" onclick="goSubjInfo('${result.subj}', '${result.course}', '${result.subjseq}')">
                                    	<c:if test="${result.course ne '000000'}">
											[패키지]  ${result.subjnm} ${fn2:toNumber(result.subjseq)}기
										</c:if>
										<c:if test="${result.course eq '000000'}">
											${result.subjnm}
										</c:if>
                                    </a>
<!--                                    ${rownumCnt} / ${result.coursecount }-->
                                    </td>
                                    
                                    <td class="num">${fn2:getFormatDate(result.propstart, 'yyyy.MM.dd')}<br>~ ${fn2:getFormatDate(result.propend, 'yyyy.MM.dd')}</td>
                                    <td class="num">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}<br>~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
                                    <td class="num">${result.edutimes}(${result.eduperiod}주)</td>
                                    <td class="num">
                                    <c:if test="${result.mobile eq 'Y'}"><img src="/images/user/ico_mobile.gif" alt="모바일지원"></c:if>
                                    <c:if test="${result.mobile ne 'Y'}">-</c:if>
                                    </td>
                                    
                               <c:if test="${result.coursecount > 0 && rownumCnt1 == 0}">
                                    <td class="num last" rowspan="${result.coursecount}">
<!-- 수화기초과정 11기 수강신청 구분 제외 시키기  -->
								<c:if test="${!(result.subj eq 'OTH130003' and fn2:toNumber(result.subjseq) eq '11' )  }">
<!--								회원구분이 학부모인데 학부모 연수가 아닌 과정인 경운 신청불가로 한다.-->
								<c:choose>
									<c:when test="${result.empGubun eq 'E' && fn:indexOf(result.upperclassnm, '교원') != -1}">
									<img src="/images/user/subj_status05.gif" alt="신청불가"/>
									</c:when>
									<c:when test="${result.empGubun eq 'P' && fn:indexOf(result.upperclassnm, '교원') != -1}">
									<img src="/images/user/subj_status05.gif" alt="신청불가"/>
									</c:when>
									<c:otherwise>
										<c:if test="${result.propstatus eq 'A'}">
	                                    	<c:if test="${result.isPropose > 0}">
	                                    		<c:if test="${result.chkfinal eq 'Y'}">
	                                    			<img src="/images/user/subj_status06.gif" alt="승인"/>
	                                    		</c:if>
	                                    		<c:if test="${result.chkfinal ne 'Y'}">
	                                    			<img src="/images/user/subj_status04.gif" alt="신청중"/>
	                                    		</c:if>	
	                                    	</c:if>
	                                    	<c:if test="${result.isPropose <= 0}">
	                                    		<img src="/images/user/subj_status02.gif" alt="신청가능"/>
	                                    	</c:if>
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'B'}">
	                                    	<img src="/images/user/subj_status01.gif" alt="준비중"/>
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'C'}">
	                                    	<img src="/images/user/subj_status03.gif" alt="신청마감"/>
	                                    </c:if>	
									</c:otherwise>
								</c:choose>
								</c:if>
<!-- 수화기초과정 11기 수강신청 구분 제외 시키기  -->				
								<c:if test="${result.subj eq 'OTH130003' and fn2:toNumber(result.subjseq) eq '11'  }">
									<c:if test="${result.propstatus eq 'A'}">
                                    	<c:if test="${result.isPropose > 0}">
                                    		<img src="/images/user/subj_status04.gif" alt="신청중"/>
                                    	</c:if>
                                    	<c:if test="${result.isPropose <= 0}">
                                    		<img src="/images/user/subj_status02.gif" alt="신청가능"/>
                                    	</c:if>
                                    </c:if>
                                    <c:if test="${result.propstatus eq 'B'}">
                                    	<img src="/images/user/subj_status01.gif" alt="준비중"/>
                                    </c:if>
                                    <c:if test="${result.propstatus eq 'C'}">
                                    	<img src="/images/user/subj_status03.gif" alt="신청마감"/>
                                    </c:if>	
								</c:if>
                                    </td>
                                </c:if>
  <c:if test="${result.coursecount > 0}">
		<c:set var="rownumCnt1" value="${rownumCnt11+1}"/>
</c:if>
                                <c:if test="${result.coursecount <= 0}">
                                 <td  class="num last" >
                                 <!-- 수화기초과정 11기 수강신청 구분 제외 시키기  -->
								<c:if test="${!(result.subj eq 'OTH130003' and fn2:toNumber(result.subjseq) eq '11' )  }">
<!--								회원구분이 학부모인데 학부모 연수가 아닌 과정인 경운 신청불가로 한다.-->
								<c:choose>
									<c:when test="${result.empGubun eq 'E' && fn:indexOf(result.upperclassnm, '교원') != -1}">
									<img src="/images/user/subj_status05.gif" alt="신청불가"/>
									</c:when>
									<c:when test="${result.empGubun eq 'P' && fn:indexOf(result.upperclassnm, '교원') != -1}">
									<img src="/images/user/subj_status05.gif" alt="신청불가"/>
									</c:when>
									<c:otherwise>
										<c:if test="${result.propstatus eq 'A'}">
	                                    	<c:if test="${result.isPropose > 0}">
	                                    		<img src="/images/user/subj_status04.gif" alt="신청중"/>
	                                    	</c:if>
	                                    	<c:if test="${result.isPropose <= 0}">
	                                    		<img src="/images/user/subj_status02.gif" alt="신청가능"/>
	                                    	</c:if>
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'B'}">
	                                    	<img src="/images/user/subj_status01.gif" alt="준비중"/>
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'C'}">
	                                    	<img src="/images/user/subj_status03.gif" alt="신청마감"/>
	                                    </c:if>	
									</c:otherwise>
								</c:choose>
								</c:if>
<!-- 수화기초과정 11기 수강신청 구분 제외 시키기  -->				
								<c:if test="${result.subj eq 'OTH130003' and fn2:toNumber(result.subjseq) eq '11'  }">
									<c:if test="${result.propstatus eq 'A'}">
                                    	<c:if test="${result.isPropose > 0}">
                                    		<img src="/images/user/subj_status04.gif" alt="신청중"/>
                                    	</c:if>
                                    	<c:if test="${result.isPropose <= 0}">
                                    		<img src="/images/user/subj_status02.gif" alt="신청가능"/>
                                    	</c:if>
                                    </c:if>
                                    <c:if test="${result.propstatus eq 'B'}">
                                    	<img src="/images/user/subj_status01.gif" alt="준비중"/>
                                    </c:if>
                                    <c:if test="${result.propstatus eq 'C'}">
                                    	<img src="/images/user/subj_status03.gif" alt="신청마감"/>
                                    </c:if>	
								</c:if>
                                 </td>
                                </c:if>
                                </tr>
                                
		<c:if test="${result.coursecount > 0 && result.coursecount == rownumCnt}">
			<c:set var="rownumCnt" value="0"/>
			<c:set var="rownumCnt1" value="0"/>
		</c:if>
		
		
		
</c:forEach>

<c:if test="${empty list}">
								<tr>
									<td colspan="8" class="last">검색된 정보가 없습니다.</td>
								</tr>
</c:if>							

                            </tbody>
                        </table>
                    </div>
                    <!-- list table-->
                    
                    
                    <!-- button 
                      <ul class="btnR m20">
                        <li><a href="#none" onclick="saveSubjConcernForCheckbox();"><img src="/images/user/btn_course_in.gif" alt="관심목록담기"/></a></li>                
                      </ul>
                       button -->


		
           
</form>


<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


function doPageList()
{
	thisForm.action = "/usr/subj/subjInfoSearchList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function doSearchList() {

	if(thisForm.search_text.value == ''){
		//if(!confirm("검색어 없이 검색하시면 전체가 검색됩니다. 검색하시겠습니까?")){
		//	thisForm.search_text.focus(); 
		//	return;
		//}else{
			thisForm.action = "/usr/subj/subjInfoSearchList.do";
			thisForm.target = "_self";
			thisForm.submit();
			
		//}
	}else{
		
		thisForm.action = "/usr/subj/subjInfoSearchList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	
}

//선택박스에서 선택해서 하는 유형의  관심과정 저장
function saveSubjConcernForCheckbox() {

	if(!hasCheckedBox(thisForm._Array_p_checks)) {
	     alert("하나이상 선택해야 합니다.");
	     return;
	}
	 
	if(confirm("선택하신 과정을 관심과정으로 저장하시겠습니까?"))
	{
		thisForm.p_process.value = "insert";
		thisForm.action = "/usr/mpg/concernInfoAction.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
}

//과정 상세 페이지로 전환
function goSubjInfo(subj, course, subjseq) {
	thisForm.p_course.value = course;
	thisForm.p_subj.value = subj;
	thisForm.p_subjseq.value = subjseq;
	thisForm.action = "/usr/subj/subjInfoView.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->