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



<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">    
    
    
    <input type="hidden" name="p_year"     value="">        
    <input type="hidden" name="p_grseq"     value="">   
    <%-- p_exam_subj<input type="text" name="p_exam_subj"  id="p_exam_subj"    value="${p_exam_subj}"> --%>    
	<input type="hidden" name="p_subj"  value="${p_subj}">
	<input type="hidden" name="p_gyear"  value="${p_gyear}">
	<input type="hidden" name="p_subjseq"  value="${p_subjseq}">
	<input type="hidden" name="p_papernum"  value="${view.papernum}">
	<input type="hidden" name="o_subj"  value="${o_subj}">
	<input type="hidden" name="ori_subj"  value="${ses_search_subj}">	
	<input type="hidden" name="p_lesson" value="${not empty p_lesson ? p_lesson : '001'}">
	<input type="hidden" name="p_lessonstart" value="001">
	<input type="hidden" name="p_lessonend" value="001">
	<input type="hidden" name="p_process" value="${p_process}">	
    <input type="hidden" name="p_totalscore"        value="100">
    <input type="hidden" name="p_level1text"        value="">
    <input type="hidden" name="p_level2text"        value="">
    <input type="hidden" name="p_level3text"        value="">
    <input type="hidden" name="p_cntlevel1"        value="">
    <input type="hidden" name="p_cntlevel2"        value="">
    <input type="hidden" name="p_cntlevel3"        value="">    
    <input type="hidden" name="p_progress"        value="${p_progress}">
    <input type="hidden" name="p_reloadlist" value="">
	<input type="hidden" name="p_userid"  value="${sessionScope.userid}">
	
	
	
	  

	      
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="D"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<!-- <div class="listTop">			
                <div class="btnR MR05">
               		<a href="javascript:InsertPaperGrseq()" class="btn01"><span>교욱기수별 문제지 추가</span></a>
                </div>                                
		</div> -->
        
   <%--  <div class="searchWrap txtL MT10">
		<ul class="datewrap">
				<li><strong class="shTit">검색 : <select name="p_searchtype" class="input">
	              <option value="1" <c:if test="${p_searchtype eq '1'}">selected</c:if>>콘텐츠명</option>
	            </select></strong></li>
				
				<li><input type="text" name="p_searchtext" id="p_searchtext" size="60" value="${p_searchtext}"/></li>
				<li><a href="#none" class="btn_search" onclick="InsertPaperPage()"><span>검색</span></a></li>
		</ul>
	</div> --%>
	
	 <div class="searchWrap txtL MT10">
		<ul class="datewrap">
				<li><strong class="shTit">콘텐츠명 : </strong></li>
				
				<li><select name="p_exam_subj" id="p_exam_subj" onChange="doPageList()">
					<option value="">선택</option>
					<c:forEach items="${examBankList}" var="examBankList" varStatus="1">					
						<option value="${examBankList.examSubj }" <c:if test="${p_exam_subj eq examBankList.examSubj}">selected</c:if>>${examBankList.examSubjnm }</option>						
					</c:forEach>				
				</select></li>
				<!-- <li><a href="#none" class="btn_search" onclick="InsertPaperPage()"><span>검색</span></a></li> -->
		</ul>
	</div>
	
	
		<c:if test="${not empty view.papernum && empty view.examsubj}">
			<span>평가 방법이 변경되기 전 문제지는 볼 수 없습니다.<br/><br/></span>
			<a href="/adm/exm/examQuestList.do?admMenuInitOption=Y" target="_blank">[평가문제등록] </a>
			<a href="/adm/exm/examQuestPoolList.do?admMenuInitOption=Y" target="_blank">[평가Pool관리]</a>
			<a href="/adm/exm/examMasterList.do?admMenuInitOption=Y" target="_blank">[평가마스터 관리]</a>
			<a href="/adm/exm/examPaperList.do?admMenuInitOption=Y" target="_blank">[평가문제지 관리]</a>
			<br/><br/>
		</c:if>
		
		<!-- tab -->
        <div class="conwrap2">
            <ul class="mtab2">
                <li><a href="#" class="on">평가 기본정보</a></li>
          </ul>
        </div>
		<!-- // tab-->		
		<!-- detail wrap -->        
        <div class="tbDetail">
        		
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="40%" />
                        <col width="10%" />
                        <col width="40%" />
                    </colgroup>
                    <tbody>
                        <tr>
                          <th scope="col">콘텐츠명</th>
                            <td>
                            	${examSubj.examSubjnm }
                            </td>
                        </tr>
                        <%-- <tr>
                            <th scope="col">평가종류</th>
                            <td>
<c:if test="${empty view.papernum }">
                            <ui:code id="p_examtype" selectItem="${p_examtype}" gubun="" codetype="0012"  levels="1"  condition=""
							upper="" title="평가종류" className="" type="select" selectTitle="" event="" />
</c:if>
<c:if test="${not empty view.papernum }">
                            <input type="hidden" name="p_examtype" value="${p_examtype}"/>
                            ${view.examtypenm}
</c:if>                            
                            </td>
                        </tr> --%>
                        <tr>
                          <th scope="col">결과점수공개</th>
                            <td>
                            <ui:code id="p_isopenanswer" selectItem="${empty view.isopenanswer ? 'Y' : view.isopenanswer}" gubun="defaultYN" codetype=""  levels=""  condition=""
							upper="" title="결과점수공개" className="vrM" type="radio" selectTitle="" event="" />
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">결과정답(해설)공개</th>
                            <td>
                            	<ui:code id="p_isopenexp" selectItem="${empty view.isopenexp ? 'N' : view.isopenexp}" gubun="defaultYN" codetype=""  levels=""  condition=""
								upper="" title="결과점수공개" className="" type="radio" selectTitle="" event="" />
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">시험기간</th>
                          <td>
                                <span>
                                    <input id="p_startdt" name="p_startdt" type="text" value="${fn2:getFormatDate(view.startdt, 'yyyy-MM-dd')}" size="10" maxlength="10" tabindex="15" readonly>
                                    <a href="#none" onclick="popUpCalendar(this, document.all.p_startdt, 'yyyy-mm-dd')"><img src="/images/adm/ico/ico_calendar.gif" alt="달력" /></a> ~
                                    <input id="p_enddt" name="p_enddt" type="text" value="${fn2:getFormatDate(view.enddt, 'yyyy-MM-dd')}" size="10" maxlength="10" tabindex="15" readonly>
                                	<a href="#none" onclick="popUpCalendar(this, document.all.p_enddt, 'yyyy-mm-dd')"><img src="/images/adm/ico/ico_calendar.gif" alt="달력" /></a>

									<%-- <c:if test="${not empty view.papernum }">  
                                        <a href="javascript:UpdateDate()" class="pop_btn01"><span>시험기간연장</span></a>
                                    </c:if> --%>
                            </span>                          		
                          </td>
                        </tr>
                        
                        <tr>
                            <th scope="col">시험시간 / 장애시험시간</th>
                            <td>
                            	<ui:code id="p_examtime" selectItem="${view.examtime}" gubun="" codetype="0094"  levels="1"  condition=""
								upper="" title="시험시간" className="" type="select" selectTitle="" event="" />
								/
								<select id="p_handicap_examtime" name="p_handicap_examtime">
									<option value="" <c:if test="${'' eq view.handicapExamtime}">selected</c:if>>선택</option>
									<option value="10" <c:if test="${'10' eq view.handicapExamtime}">selected</c:if>>10분</option>
									<option value="20" <c:if test="${'20' eq view.handicapExamtime}">selected</c:if>>20분</option>
									<option value="30" <c:if test="${'30' eq view.handicapExamtime}">selected</c:if>>30분</option>
									<option value="40" <c:if test="${'40' eq view.handicapExamtime}">selected</c:if>>40분</option>
									<option value="50" <c:if test="${'50' eq view.handicapExamtime}">selected</c:if>>50분</option>
									<option value="60" <c:if test="${'60' eq view.handicapExamtime}">selected</c:if>>60분</option>
									<option value="70" <c:if test="${'70' eq view.handicapExamtime}">selected</c:if>>70분</option>
									<option value="80" <c:if test="${'80' eq view.handicapExamtime}">selected</c:if>>80분</option>
									<option value="90" <c:if test="${'90' eq view.handicapExamtime}">selected</c:if>>90분</option>
									<option value="100" <c:if test="${'100' eq view.handicapExamtime}">selected</c:if>>100분</option>
									<option value="110" <c:if test="${'110' eq view.handicapExamtime}">selected</c:if>>110분</option>
									<option value="120" <c:if test="${'120' eq view.handicapExamtime}">selected</c:if>>120분</option>
								</select>
                            </td>
                        </tr>
                        <%-- <tr>
                          <th scope="col">재응시</th>
                          <td><input name="p_retrycnt" type="text" class="input" size="3" value="${empty view.retrycnt ? '0' : view.retrycnt}"> 회</td>
                        </tr> --%>
                        
                        <input type="hidden" name="p_retrycnt" class="input" size="3" value="0">
                        
                        
                        <%-- <tr>
                          <th scope="col">문제출제횟수 제한</th>
                          <td><input name="p_setexamcnt" type="text" class="input" size="3" value="${empty view.setexamcnt ? '0' : view.setexamcnt}"> 회</td>
                        </tr> --%>					
                    </tbody>
                </table>
      	  </div>
      	  <!-- // detail wrap -->
      	  <!-- button -->
		<ul class="btnCen">        	
        	<c:if test="${not empty view.papernum }">				
				<li><a href="javascript:PaperAction('basic')" class="pop_btn01"><span>평가 기본정보 수정</span></a></li>								
			</c:if>                                    
		</ul>
		<!-- // button -->
		
		
		
      	  
	      <!-- tab -->
	      <div class="conwrap2">
	      	<ul class="mtab2">
	                <li><a href="#" class="on">문제정보</a></li>
	          </ul>
	        </div>
		  <!-- // tab-->	
      	  <div class="tbDetail">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="33.33%" />
                        <col width="33.33%" />
                        <col width="33.33%" />
                    </colgroup>
                    <thead>
                        <tr>
                          <th scope="col">난이도 상</th>
                          <th scope="col">난이도 중</th>
                          <th scope="col">난이도 하</th>
                          
                        </tr>					
                    </thead>
                    <tbody>
                    
                    
<c:set var="v_examcnt" value="0"/>
<c:set var="s_lesson" value="0"/>
 
<c:if test="${empty view.papernum }">
	
	<tr>
		
			<td class="txtC">객관식
	    		( ${examBankLevelsCnt.levelscount1}개문제 / 
	    		<input type="text" name="p_cntlevelM1M1M1"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M1M1" value="${examBankLevelsCnt.levelscount1}"/>
	    		
				주관식
	    		( ${examBankLevelsCnt.levelscount4}개문제 / 
	    		<input type="text" name="p_cntlevelM1M1M2"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M1M2" value="${examBankLevelsCnt.levelscount4}"/>	
					
				O/X식
	    		( ${examBankLevelsCnt.levelscount7}개문제 / 
	    		<input type="text" name="p_cntlevelM1M1M3"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M1M3" value="${examBankLevelsCnt.levelscount7}"/>
	    	</td> 
		
			<td class="txtC"> 					
				객관식
	    		( ${examBankLevelsCnt.levelscount2}개문제 / 
	    		<input type="text" name="p_cntlevelM1M2M1"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M2M1" value="${examBankLevelsCnt.levelscount2}"/>				
					
					
				주관식
	    		( ${examBankLevelsCnt.levelscount5}개문제 / 
	    		<input type="text" name="p_cntlevelM1M2M2"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M2M2" value="${examBankLevelsCnt.levelscount5}"/>
	    		
				O/X식
	    		( ${examBankLevelsCnt.levelscount8}개문제 / 
	    		<input type="text" name="p_cntlevelM1M2M3"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M2M3" value="${examBankLevelsCnt.levelscount8}"/>
	    	</td> 
		
			<td class="txtC">
				객관식
	    		( ${examBankLevelsCnt.levelscount3}개문제 / 
	    		<input type="text" name="p_cntlevelM1M3M1"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M3M1" value="${examBankLevelsCnt.levelscount3}"/>
					
					
				주관식
	    		( ${examBankLevelsCnt.levelscount6}개문제 / 
	    		<input type="text" name="p_cntlevelM1M3M2"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M3M2" value="${examBankLevelsCnt.levelscount6}"/>

				O/X식
	    		( ${examBankLevelsCnt.levelscount9}개문제 / 
	    		<input type="text" name="p_cntlevelM1M3M3"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM1M3M3" value="${examBankLevelsCnt.levelscount9}"/>	    	
	    	</td> 
		 
		</tr>	
</c:if>




<c:if test="${not empty view.papernum }">

<%
	int s_lesson = 0;
	int s_levels = 0;
	int s_type = 0;


    //ArrayList  lessonlist = (ArrayList)request.getAttribute("list");
	ArrayList levelslist = null;
	ArrayList typelist = null;
	//s_lesson = lessonlist.size();

    ArrayList  lessonlistM = (ArrayList)request.getAttribute("masterList");
	ArrayList levelslistM = null;
	ArrayList typelistM = null;
	
	//System.out.println("lessonlist.size() ----> "+lessonlist.size());
	System.out.println("lessonlistM.size() ----> "+lessonlistM.size());
	
    
    for ( int i =0; i < 1 ; i++){

%>
        <tr> 
<%
	      //levelslist = (ArrayList)lessonlist.get(i);
          //s_levels = levelslist.size();

	      levelslistM = (ArrayList)lessonlistM.get(i);
		  
	      
	      System.out.println("levelslistM.size() ----> "+levelslistM.size());
		  for ( int j = 0 ; j < 3 ;  j++) {
%>
			<td class="txtC"> 
<%
			  //typelist = (ArrayList)levelslist.get(j);
			  //s_type = typelist.size();

			  typelistM = (ArrayList)levelslistM.get(j);
			  
			  System.out.println("typelistM.size() ----> "+typelistM.size());
			  for ( int k = 0; k < 3 ; k++) {
				  //Map dbox = (Map)typelist.get(k);	
				  
				  int examcnt = 0;
				  
				  if(typelistM.size() > 0) examcnt = Integer.parseInt(((String)typelistM.get(k)));
				  
				  if ( (k+1)%3 == 0 ) {
				  
%>
					O/X식 
<%				  
				  } else if ( (k+1)%2 == 0 ) {
%>
				       주관식
<%
                 }  else {					  
%>
				       객관식
<%
                 }
				 
				 if(j==0 && k==0){				
%>				  
					( ${examBankLevelsCnt.levelscount1} 개문제 /
					<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount1}"/> 
				<%
				 }
				 if(j==1 && k==0){
						
				 %>				  
	 				( ${examBankLevelsCnt.levelscount2} 개문제 /
	 				<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount2}"/> 
 				<%
				 }
				 if(j==2 && k==0){
						
				 %>				  
	 				( ${examBankLevelsCnt.levelscount3} 개문제 /
	 				<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount3}"/> 
 				<%
				 }
				 if(j==0 && k==1){
						
				 %>				  
	 				( ${examBankLevelsCnt.levelscount4} 개문제 /
	 				<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount4}"/> 
 				<%
				 }
				 if(j==1 && k==1){
						
				 %>				  
	 				( ${examBankLevelsCnt.levelscount5} 개문제 /
	 				<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount5}"/> 
 				<%
				 }
				 if(j==2 && k==1){
						
				 %>				  
	 				( ${examBankLevelsCnt.levelscount6} 개문제 /
	 				<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount6}"/> 
 				<%
				 }
				 if(j==0 && k==2){
						
				 %>				  
	 				( ${examBankLevelsCnt.levelscount7} 개문제 /
	 				<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount7}"/> 
 				<%
				 }
				 if(j==1 && k==2){
						
				 %>				  
	 				( ${examBankLevelsCnt.levelscount8} 개문제 /
	 				<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount8}"/> 
 				<%
				 }
				 if(j==2 && k==2){
						
				 %>				  
	 				( ${examBankLevelsCnt.levelscount9} 개문제 /
	 				<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
		    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="${examBankLevelsCnt.levelscount9}"/> 
 				<%
 				 }
				%>
	    		
<%
			}
%>

			</td>
<%		  
		}
%>          
        </tr>
<%
   }
%>


</c:if>                  
                    </tbody>
                </table>
      	</div> 
      	
      	
      	<div class="tbDetail">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="40%" />
                        <col width="10%" />
                        <col width="40%" />
                    </colgroup>
                    <tbody>
						
<c:if test="${empty view.papernum }">
                    
                        <tr>
                          <th scope="col">문제당 배점</th>
                          <td>1문제당 
                          
                          <c:set var="v_exampoint">0</c:set>
                          
                          <c:if test="${v_examcnt > 0}">
                          	<c:set var="v_exampoint">${100/v_examcnt}</c:set>
                          </c:if>
                          
                          <input name="p_exampoint" type="text" class="input" size="5" value="<fmt:formatNumber value="${v_exampoint}" type="number"/>"> 점</td>
                          <th>문제수</th>
                          <td><input name="p_examcnt" type="text" class="input" size="5" value="${v_examcnt}"> 개문제출제</td>
                        </tr>
</c:if>
<c:if test="${not empty view.papernum }">                      
                        <tr>
                          <th scope="col">문제당 배점</th>
                          <td>1문제당 
                          <input name="p_exampoint" type="text" class="input" size="5" value="<fmt:formatNumber value="${view.exampoint}" type="number"/>"> 점</td>
                          <th>문제수</th>
                          <td><input name="p_examcnt" type="text" class="input" size="5" value="${view.examcnt}"> 개문제출제</td>
                        </tr>
</c:if>
                    </tbody>
                </table>
      	</div>  
      	
		<table cellspacing="0" cellpadding="0">
			<tr> 
				<td>※ 문제당 배점 * 문제수 = 100</td>
			</tr>
		</table>
		
		
		<!-- button -->
		<ul class="btnCen">
        	
        	<c:if test="${not empty view.papernum }">      	
	        	<c:if test="${paperCnt > 0}">
					<li><a href="javascript:alert('응시자가 존재하는 문제지입니다.\n수정이 불가능합니다.')" class="pop_btn01"><span>저 장</span></a></li> 
				</c:if>
					
				<c:if test="${paperCnt == 0}">
					<li><a href="javascript:PaperAction('update')" class="pop_btn01"><span>수 정</span></a></li>
					<li><a href="javascript:PaperAction('delete')" class="pop_btn01"><span>삭 제</span></a></li>
					<li><a href="javascript:doPaperModify('${view.subj}','${view.year}','${view.subjseq}','${view.lesson}','${view.examtype}','${view.papernum}');" class="pop_btn01"><span>문제수정</span></a></li>					
				</c:if>
				<li><a href="javascript:doPaperPreview('${view.subj}','${view.year}','${view.subjseq}','${view.lesson}','${view.examtype}','${view.papernum}');" class="pop_btn01"><span>미리보기</span></a></li>
			</c:if>
			
			<c:if test="${empty view.papernum }"> 
				<li><a href="javascript:PaperAction('insertGrSeq')" class="pop_btn01"><span>저 장</span></a></li> 
			</c:if>
                                    
		</ul>
		<!-- // button -->
			
</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



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
	
	if($("#ses_search_subj").val() == '' || $("#ses_search_subjseq").val() == '')
	{	
		alert("과정-기수를 선택하세요");
		return;
	}
	
	var v_setexamcnt = parseInt($("#p_setexamcnt").val());
	if (v_setexamcnt < 1) {		
		alert('문제출제횟수 제한을 입력해 주십시요.');
		return false;
	}
	
	
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/exm/examBankPaperList.do";
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
	thisForm.action = "/adm/exm/examBankPaperList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}


//평가 문제지  보기화면
function doPaperView(p_subj, p_gyear, p_subjseq, p_lesson, p_examtype, p_papernum) {
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
	
      farwindow = window.open("", "examPaperViewStep01PopUpWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 1020, height = 667, top=0, left=0");
      
		thisForm.p_subj.value  = p_subj;
		thisForm.p_gyear.value   = p_gyear;
		thisForm.p_subjseq.value   = p_subjseq;
		thisForm.p_lesson.value  = p_lesson;
		thisForm.p_examtype.value   = p_examtype;
		thisForm.p_papernum.value   = p_papernum;
		
		thisForm.target = "examPaperViewStep01PopUpWindow";
		thisForm.action = "/adm/exm/examPaperViewStep01.do";
		thisForm.submit();

      farwindow.window.focus();

}




//평가 문제지  미리보기 
function doPaperPreview(p_subj, p_gyear, p_subjseq, p_lesson, p_examtype, p_papernum) {
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
	
      farwindow = window.open("", "examMasterPreviewPopUpWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 667, top=0, left=0");
      
		thisForm.p_subj.value  = p_subj;
		thisForm.p_gyear.value   = p_gyear;
		thisForm.p_year.value   = p_gyear;
		thisForm.p_subjseq.value   = p_subjseq;
		thisForm.p_lesson.value  = p_lesson;
		thisForm.p_examtype.value   = p_examtype;
		thisForm.p_papernum.value   = p_papernum;
		
		thisForm.target = "examMasterPreviewPopUpWindow";
		thisForm.action = "/adm/exm/examBankPaperPreview.do";
		thisForm.submit();

      farwindow.window.focus();

}

//평가 기수별 문제지 추가
function InsertPaperGrseq() {
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


	  thisForm.p_grseq.value    = $("#ses_search_grseq").val();           
	  thisForm.p_gyear.value    = $("#ses_search_gyear").val();
	  thisForm.p_process.value = 'insertGrSeq';
	  
	  thisForm.target = "_self";
	  thisForm.action = "/adm/exm/examPaperAction.do";
	  thisForm.submit();
}

//문제지 추가 
function InsertPaperPage(p_subj, p_gyear, p_subjseq) {
	
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
	
     farwindow = window.open("", "examBankPopUpWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 667, top=0, left=0");
      
		thisForm.p_subj.value  = p_subj;		
		thisForm.p_year.value   = p_gyear;
		thisForm.p_subjseq.value   = p_subjseq;		
		
		thisForm.target = "examBankPopUpWindow";
		thisForm.action = "/adm/exm/examBankPaperPop.do";
		thisForm.submit();

      farwindow.window.focus();

}


function PaperAction(p_process) {
	
	var msg = "";
	if(p_process == "insertGrSeq"){
		msg = "저장 하시겠습니까?";
	}
	if(p_process == "update"){
		msg = "수정 하시겠습니까?";
	}
	if(p_process == "delete"){
		msg = "삭제 하시겠습니까?";
	}
	if(p_process == "basic"){
		msg = "평가 기본정보를 수정 하시겠습니까?";
	}
	
	if (!confirm(msg)) {
		return;
	}
	
	if(p_process != "delete"){ 
		if (!chkData()) {
			return;
		}	
	}
	
	thisForm.p_process.value = p_process;
	thisForm.action = '/adm/exm/examBankPaperAction.do';
	thisForm.target = '_self';
	thisForm.submit();
}

function chkData( ) {
	
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return false;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return false;
	}

	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하고 검색버튼을 클릭해 주세요");
		return false;
	}
	
	if($("#p_exam_subj").val() == '')
	{	
		alert("콘텐츠명을 선택하세요");
		return false;
	}
	
	var _startdt	= thisForm.p_startdt.value.replace(/-/g,"");
	var _enddt	= thisForm.p_enddt.value.replace(/-/g,"");

	if (_startdt == "") {
		alert("시험 시작일을 입력하세요.");
		return false;
	} else if (_enddt == "") {
		alert("시험 종료일을 입력하세요.");
		return false;
	} else if (_startdt*1 > _enddt*1) {
		alert("시험 시작일이 종료일 이후 입니다.");
		return false;
	}

	var v_examtime = parseInt(thisForm.p_examtime.value);
	if (v_examtime < 1) {
		thisForm.p_examtime.focus();
		alert('시험시간을 입력해 주십시요.');
		return false;
	}
	
	var v_setexamcnt = parseInt(thisForm.p_setexamcnt.value);
	if (v_setexamcnt < 1) {
		thisForm.p_setexamcnt.focus();
		alert('문제출제횟수 제한을 입력해 주십시요.');
		return false;
	}
	
	

	if (thisForm.p_lesson.value.length < 1) {
		alert('과정의 차시가 등록되어 있지 않습니다.');
		return false;
	}  

	thisForm.p_startdt.value = _startdt;
	thisForm.p_enddt.value = _enddt;
	
	
	
	var v_examcnt = 0;
	
	if (!number_chk_noalert(thisForm.p_exampoint.value) || 1 > thisForm.p_exampoint.value) {
		thisForm.p_exampoint.focus();
	    alert('문제당 배점이 잘못 입력되었습니다.');
	    return false;
	}
	if (!number_chk_noalert(thisForm.p_examcnt.value) || 1 > thisForm.p_examcnt.value) {
		thisForm.p_examcnt.focus();
	    alert('문제수가 잘못 입력되었습니다.');
	    return false;
	}
	if (thisForm.p_exampoint.value * thisForm.p_examcnt.value != 100) {
		thisForm.p_exampoint.focus();
	    alert('총점수는 100점입니다. 문제당 배점이나 문제수를 조정해 주십시요.');
	    return false;
	}


	var s_level1text = "";
	var s_level2text = "";
	var s_level3text = ""; 
	var s_cntlevel1 = 0; 
	var s_cntlevel2 = 0; 
	var s_cntlevel3 = 0; 
	

	<c:forEach begin="1" end="1" var="resultLevel" varStatus="i">
		<c:forEach  begin="1" end="3" var="resultType" varStatus="j">
			<c:forEach begin="1" end="3" var="typeList" varStatus="k">
				
		 		if ( ${j.count} == 1 ) {
		      		s_level1text += ${i.count} + "," + thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value;
			  		s_cntlevel1 += parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value);

			  		if (${k.count} == 2) {		
		          		s_level1text += "|";	
			  		}else if (${k.count} == 3) {	
				  		if(${s_lesson}-${i.index} >1) s_level1text += "/";	  
			  		}else{
			      		s_level1text += "|";	       
			  		}
		   		}
		   		
		 		if ( ${j.count} == 2 ) {
		      		s_level2text += ${i.count} + "," + thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value;
			  		s_cntlevel2 += parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value);
			  		if (${k.count} == 2) {
				  		//if(${s_lesson}-${i.index} >1) s_level2text += "/";
		          		s_level2text += "|";
			  		}else if (${k.count} == 3) {
				  		if(${s_lesson}-${i.index} >1) s_level2text += "/";		  		  
			  		}else{
			       	s_level2text += "|";
			  		}
		   		}
		   
		 		if ( ${j.count} == 3 ) {
		     		s_level3text += ${i.count} + "," + thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value;
			  		s_cntlevel3 += parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value);
			  		if (${k.count} == 2) {
				  	//if(${s_lesson}-${i.index} >1) s_level3text += "/";
				  		s_level3text += "|";
			  		}else if (${k.count} == 3) {
				  		if(${s_lesson}-${i.index} >1) s_level3text += "/";		  
			  		}else{
			       		s_level3text += "|";
			  		}
		   		}

				if (!number_chk_noalert(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value) || parseInt(thisForm.v_cntlevelM${i.count}M${j.count}M${k.count}.value) < parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value) ) {
		    		alert('난이도별 문항수가 잘못입력되었습니다.');
		    		thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.focus();
		    		return false;
		  		}
		 		
				v_examcnt += parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value,10)		
		    </c:forEach>
		</c:forEach> 
	</c:forEach>     

	if (v_examcnt != thisForm.p_examcnt.value) {
		thisForm.p_cntlevelM1M1M1.focus();
	    alert('입력한 문제수는 ' + v_examcnt + '개 입니다. \n문제수에 맞게 문제 배분을 해 주세요.');
	    return false;
	}
	thisForm.p_level1text.value = s_level1text;
	thisForm.p_level2text.value = s_level2text;
	thisForm.p_level3text.value = s_level3text;
	
	thisForm.p_cntlevel1.value = s_cntlevel1;
	thisForm.p_cntlevel2.value = s_cntlevel2;
	thisForm.p_cntlevel3.value = s_cntlevel3;
	
	thisForm.p_grseq.value    = $("#ses_search_grseq").val();           
	thisForm.p_gyear.value    = $("#ses_search_gyear").val();
	

	return true;
	
}


//시험기간 연장하기
function UpdateDate() {
  if (!confirm("평가문제지 시험기간을 연장하시겠습니까?")) {
    return;
  }
  thisForm.p_process.value   = "updatedate";
  thisForm.action = '/adm/exm/examBankPaperAction.do';
  thisForm.target = '_self';
  thisForm.submit();
}


//평가 문제지  미리보기 
function doPaperModify(p_subj, p_gyear, p_subjseq, p_lesson, p_examtype, p_papernum) {
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
	
      farwindow = window.open("", "examModifyPopUpWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 667, top=0, left=0");
      
		thisForm.p_subj.value  = p_subj;
		thisForm.p_gyear.value   = p_gyear;
		thisForm.p_year.value   = p_gyear;
		thisForm.p_subjseq.value   = p_subjseq;
		thisForm.p_lesson.value  = p_lesson;
		thisForm.p_examtype.value   = p_examtype;
		thisForm.p_papernum.value   = p_papernum;
		
		thisForm.target = "examModifyPopUpWindow";
		thisForm.action = "/adm/exm/examBankPaperExamModify.do";
		thisForm.submit();

      farwindow.window.focus();

}
//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
