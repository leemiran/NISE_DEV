<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>		
		<!-- tab -->
<!--        <div class="conwrap2">-->
<!--            <ul class="mtab2">-->
<!--                <li><a href="#" class="on">오늘의 교육 운영현황</a></li>-->
<!--                <li><a href="main_sub01.html">현재의 교육 운영현황</a></li>-->
<!--                <li class="end"><a href="#">이달의 교육 운영현황</a></li>                -->
<!--            </ul>-->
<!--        </div>-->
		<!-- // tab-->

		<!-- box -->
        <ul class="box_line">
        		<li>
                    <div class="m_box">
                    	<div class="sub_tit">
							<h4>신청중인 과정</h4>			
						</div>
			            <div class="tbList">
			                <table summary="" cellspacing="0" width="100%">
			                <caption>목록</caption>
			                <colgroup>
			                        <col width="80%" />
			                        <col width="20%" />                 
			                    </colgroup>
			                    <thead>
			                        <tr>
			                            <th scope="row">교육기수</th>
			                            <th scope="row">정원/신청</th>                            
			                        </tr>
			                    </thead>
			                    <tbody>
<c:set var="listcount" value="0"></c:set>			                    
<c:forEach items="${subjList}" var="result" varStatus="i">
	<c:if test="${result.stnum eq '1'}">
			                        <tr>
			                        	<td style="text-align:left;padding-left:4px;">
				                        	<a href="#none" onclick="boardView('${result.grseq}','${result.subj}','${result.year}','${result.subjseq}','${result.subjnm}','${result.subjseqnum}','${result.seq}', '${result.tabseq}', '02000000', '02010000', '/adm/prop/approvalList.do')">
				                        		${result.grseqnm}
				                        	</a>
			                        	</td>
			                            <td class="num">${result.studentlimit}/${result.educnt}</td>                                                        
			                        </tr>
	<c:set var="listcount" value="1"></c:set>			                        
	</c:if>			                        
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="2">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
			                    </tbody>
			                </table>
			            </div>
        			</div>
         		</li>
                <li>
                    <div class="m_box">
			        	<div class="sub_tit">
							<h4>학습중인 과정</h4>			
						</div>
			            <div class="tbList">
			                <table summary="" cellspacing="0" width="100%">
			                <caption>목록</caption>
			                <colgroup>
			                        <col width="80%" />
			                        <col width="20%" />                        
			                    </colgroup>
			                    <thead>
			                        <tr>
			                            <th scope="row">교육기수</th>
			                            <th scope="row">정원/수강</th>               
			                        </tr>
			                    </thead>
			                    <tbody>
<c:set var="listcount" value="0"></c:set>				                    
<c:forEach items="${subjList}" var="result" varStatus="i">
	<c:if test="${result.stnum eq '2'}">
			                        <tr>
			                        	<td style="text-align:left;padding-left:4px;">
			                        	<a href="#none" onclick="boardView('${result.grseq}','${result.subj}','${result.year}','${result.subjseq}','${result.subjnm}','${result.subjseqnum}','${result.seq}', '${result.tabseq}', '03000000', '03100000', '/adm/stu/totalScoreMemberList.do')">
			                        	${result.grseqnm}
			                        	</a>
			                        	</td>
			                            <td class="num">${result.studentlimit}/${result.educnt}</td>                                   
			                        </tr>
	<c:set var="listcount" value="1"></c:set>	
	</c:if>			                        
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="2">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
			                    </tbody>
			                </table>
			            </div>
        			</div>
         		</li>
         		<li class="bg_none">
                    <div class="m_box">
			        	<div class="sub_tit">
							<h4>종료된 과정</h4>			
						</div>
			            <div class="tbList">
			                <table summary="" cellspacing="0" width="100%">
			                <caption>목록</caption>
			                <colgroup>
			                        <col width="60%" />
			                        <col width="20%" />
			                        <col width="20%" />                        
			                        <col width="20%" />                        
			                    </colgroup>
			                    <thead>
			                        <tr>
			                            <th scope="row">교육기수</th>
			                            <th scope="row">수강/이수</th>
			                            <th scope="row">수료율</th>                            
			                            <th scope="row">종료일자</th>                            
			                        </tr>
			                    </thead>
			                    <tbody>
<c:set var="listcount" value="0"></c:set>
<c:forEach items="${subjList}" var="result" varStatus="i">
	<c:if test="${result.stnum eq '3'}">
			                        <tr>
			                        	<td style="text-align:left;padding-left:4px;">
			                        	<a href="#none" onclick="boardView('${result.grseq}','${result.subj}','${result.year}','${result.subjseq}','${result.subjnm}','${result.subjseqnum}','${result.seq}', '${result.tabseq}', '05000000', '05010000', '/adm/fin/finishCourseList.do')">
			                        	${result.grseqnm}
			                        	</a>
			                        	</td>
			                        	<td class="num">${result.educnt}/${result.gradcnt}</td>
			                            <td class="num">${result.gradper}%</td>                                                        
			                            <td class="num">${fn:substring(result.eduend, 0,4)}.${fn:substring(result.eduend, 4,6)}.${fn:substring(result.eduend, 6,8)}</td>                                                        
			                        </tr>
	<c:set var="listcount" value="1"></c:set>	
	</c:if>			                        
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="4">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
			                    </tbody>
			                </table>
			            </div>
        			</div>
         		</li>
         </ul>
		<!-- //box -->
		<br/>
		
		
		
		<div class="sub_tit">
			<h4>과정 질문방</h4>			
		</div>
		
		<!-- list table-->
        <div class="m_box">
            <div class="tbList">
                <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                        <col width="30%" />
                        <col width="40%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="10%" />                        
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="row">교육기수</th>
                            <th scope="row">제목</th>
                            <th scope="row">작성자</th>
                            <th scope="row">등록일</th>
                            <th scope="row">답변여부</th>
                        </tr>
                    </thead>
                    <tbody>
<c:set var="listcount" value="0"></c:set>
<c:forEach items="${pdsList}" var="result" varStatus="i">
	<c:if test="${result.type eq 'SQ'}">                    
                        <tr>
                        	<td style="text-align:left;padding-left:4px;">${result.grseqnm}</td>
                        	<td style="text-align:left;padding-left:4px;">
                        	<a href="#none" onclick="boardView('${result.grseq}','${result.subj}','${result.year}','${result.subjseq}','${result.subjnm}','${result.subjseqnum}','${result.seq}', '${result.tabseq}', '04000000', '04350000', '/adm/cmg/stu/studyAdminDataBoardView.do')">
                        	${result.title}
                        	</a>
                        	</td>
                        	<td>${result.name}</td>
                        	<td>${result.indate}</td>
                            <td class="num">${result.answercnt > 0 ? 'Y' : 'N'}</td>                                                        
                        </tr>
	<c:set var="listcount" value="1"></c:set>	     
	</c:if>
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="5">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
                    </tbody>
                </table>
            </div>
        </div>

<br/>

<c:if test="${'P1' ne sessionScope.gadmin}">
<div class="sub_tit">
			<h4>Q&amp;A 관리</h4>			
		</div>
		
		<!-- list table-->
        <!--<div class="m_box">
            <div class="tbList">
                <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                        <col width="30%" />
                        <col width="40%" />
                        <col width="20%" />
                        <col width="10%" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="row">과정명</th>
                            <th scope="row">제목</th>
                            <th scope="row">첨부파일</th>
                            <th scope="row">등록일</th>
                        </tr>
                    </thead>
                    <tbody>
<c:set var="listcount" value="0"></c:set>
<c:forEach items="${pdsList}" var="result" varStatus="i">
	<c:if test="${result.type eq 'SD'}">                 
                        <tr>
                        	<td style="text-align:left;padding-left:4px;">${result.subjnm}</td>
                        	<td style="text-align:left;padding-left:4px;">
                        	<a href="#none" onclick="boardView('${result.grseq}','${result.subj}','${result.year}','${result.subjseq}','${result.subjnm}','${result.subjseqnum}','${result.seq}', '${result.tabseq}', '04000000', '04090000', '/adm/cmg/stu/studyAdminDataBoardView.do')">
                        	${result.title}
                        	</a>
                        	</td>
                        	<td>
	                        	<c:if test="${result.upfilecnt > 0 }">
									<img src="/images/adm/ico/ico_file01.gif" border="0"/>
								</c:if>
                        	</td>
                        	<td>${result.indate}</td>
                        </tr> 
	<c:set var="listcount" value="1"></c:set>	      
	</c:if>
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="4">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
                    </tbody>
                </table>
            </div>
        </div>
        
        --><div class="m_box">
            <div class="tbList">
                <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                        <col width="40%" />
                        <col width="10%" />
                        <col width="20%" />
                        <col width="20%" />
                        <col width="10%" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="row">제목</th>
                            <th scope="row">답변여부</th>
                            <th scope="row">작성자</th>
                            <th scope="row">등록일</th>
                            <th scope="row">답변</th>
                        </tr>
                    </thead>
                    <tbody>
<c:set var="listcount" value="0"></c:set>
<c:forEach items="${qnaList}" var="result" varStatus="i">
<c:if test="${result.tabseq eq '1'}">
                        <tr>
                        	<td style="text-align:left;padding-left:4px;">
                        	<a href="#none" onclick="qnaView('${result.seq}', '/adm/hom/qna/selectQnaList.do', '1')">
                        	${result.title}
                        	</a>
                        	</td>
                        	<td>
                        	<c:if test="${result.hasanswer eq 'Y'}">완료</c:if>
                        	<c:if test="${result.hasanswer ne 'Y'}">미완료</c:if>
                        	</td>
                        	<td>
	                        	${result.name }
                        	</td>
                        	<td>${fn:substring(result.indate, 0,4)}.${fn:substring(result.indate, 4,6)}.${fn:substring(result.indate, 6,8)}</td>
                        	<td>
                        	<a href="#none" onclick="doWriteForm('${result.seq}', '1')" class="btn02"><span>답변등록</span></a>
                        	</td>
                        </tr>
</c:if> 
	<c:set var="listcount" value="1"></c:set>	      
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="4">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
                    </tbody>
                </table>
            </div>
        </div>
        
        <br/>
        
<!--        <div class="sub_tit">-->
<!--			<h4>과정 공지사항</h4>			-->
<!--		</div>-->
		
		<ul class="box_line">
        		<li style="width:500px;" class="bg_none">
                    <div class="m_box">
                    	<div class="sub_tit">
							<h4>입금 확인</h4>			
						</div>
			            <div class="tbList">
			                <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                        <col width="40%" />
                        <col width="20%" />
                        <col width="20%" />
                        <col width="20%" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="row">제목</th>
                            <th scope="row">답변여부</th>
                            <th scope="row">작성자</th>
                            <th scope="row">등록일</th>
                        </tr>
                    </thead>
                    <tbody>
<c:set var="listcount" value="0"></c:set>
<c:forEach items="${qnaList}" var="result" varStatus="i">
<c:if test="${result.tabseq eq '2'}">
                        <tr>
                        	<td style="text-align:left;padding-left:4px;">
                        	<a href="#none" onclick="qnaView('${result.seq}', '/adm/hom/qna/selectQnaList2.do', '2')">
                        	${result.title}
                        	</a>
                        	</td>
                        	<td>
                        	<c:if test="${result.hasanswer eq 'Y'}">완료</c:if>
                        	<c:if test="${result.hasanswer ne 'Y'}">미완료</c:if>
                        	</td>
                        	<td>
	                        	${result.name }
                        	</td>
                        	<td>${fn:substring(result.indate, 0,4)}.${fn:substring(result.indate, 4,6)}.${fn:substring(result.indate, 6,8)}</td>
                        </tr>
</c:if> 
	<c:set var="listcount" value="1"></c:set>	      
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="4">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
                    </tbody>
                </table>
			            </div>
        			</div>
         		</li>
                <li  style="width:500px;" class="bg_none">
                    <div class="m_box">
			        	<div class="sub_tit">
							<h4>환불 요청</h4>			
						</div>
			            <div class="tbList">
			                <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                        <col width="40%" />
                        <col width="20%" />
                        <col width="20%" />
                        <col width="20%" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="row">제목</th>
                            <th scope="row">답변여부</th>
                            <th scope="row">작성자</th>
                            <th scope="row">등록일</th>
                        </tr>
                    </thead>
                    <tbody>
<c:set var="listcount" value="0"></c:set>
<c:forEach items="${qnaList}" var="result" varStatus="i">
<c:if test="${result.tabseq eq '3'}">
                        <tr>
                        	<td style="text-align:left;padding-left:4px;">
                        	<a href="#none" onclick="qnaView('${result.seq}', '/adm/hom/qna/selectQnaList3.do', '3')">
                        	${result.title}
                        	</a>
                        	</td>
                        	<td>
                        	<c:if test="${result.hasanswer eq 'Y'}">완료</c:if>
                        	<c:if test="${result.hasanswer ne 'Y'}">미완료</c:if>
                        	</td>
                        	<td>
	                        	${result.name }
                        	</td>
                        	<td>${fn:substring(result.indate, 0,4)}.${fn:substring(result.indate, 4,6)}.${fn:substring(result.indate, 6,8)}</td>
                        </tr>
</c:if> 
	<c:set var="listcount" value="1"></c:set>	      
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="4">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
                    </tbody>
                </table>
			            </div>
        			</div>
         		</li>
         </ul>
		<!-- list table-->
        <!--<div class="m_box">
            <div class="tbList">
                <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                        <col width="30%" />
                        <col width="40%" />
                        <col width="20%" />
                        <col width="10%" />                 
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="row">과정명</th>
                            <th scope="row">제목</th>
                            <th scope="row">첨부파일</th>
                            <th scope="row">등록일</th>
                        </tr>
                    </thead>
                    <tbody>
<c:set var="listcount" value="0"></c:set>
<c:forEach items="${noticeList}" var="result" varStatus="i">
                        <tr>
                        	<td style="text-align:left;padding-left:4px;">${result.subjnm}</td>
                        	<td style="text-align:left;padding-left:4px;">
                        	<a href="#none" onclick="boardView('${result.grseq}','${result.subj}','${result.year}','${result.subjseq}','${result.subjnm}','${result.subjseqnum}','${result.seq}', '', '04000000', '04010000', '/adm/cmg/not/courseNoticeView.do')">
                        	${result.title}
                        	</a>
                        	</td>
                        	<td>
	                    	<c:if test="${not empty result.upfile}">
			                    <a href="#none" onclick="fn_download('${result.realfile}', '${result.upfile}', 'bulletin')"><c:out value="${result.realfile}"/></a>
	                    	</c:if>
                        	</td>
                        	<td>${result.addate}</td>                                             
                        </tr> 
	<c:set var="listcount" value="1"></c:set>	      
</c:forEach>
<c:if test="${listcount == 0}">
	<tr>
		<td colspan="4">
			검색된 내용이 없습니다.
		</td>
	</tr>
</c:if>
                    </tbody>
                </table>
            </div>
        </div>-->
</c:if>
        
                      
	<!-- // contents -->    
	<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"/>
	<input type="hidden" name="p_year" 		id="p_year"/>
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"/>
	<input type="hidden" name="p_subjnm" 	id="p_subjnm"/>
	<input type="hidden" name="p_seq" 	id="p_seq"/>
	<input type="hidden" name="p_tabseq" 	id="p_tabseq">
	<input type="hidden" name="p_subjseq2" 	id="p_subjseq2"/>
	
	
	<input type="hidden" name="ses_search_subj" 		id="ses_search_subj"/>
	<input type="hidden" name="ses_search_year" 		id="ses_search_year"/>
	<input type="hidden" name="ses_search_subjseq" 	id="ses_search_subjseq"/>
	<input type="hidden" name="ses_search_subjnm" 	id="ses_search_subjnm"/>
	<input type="hidden" name="ses_search_grseq" 	id="ses_search_grseq"/>
	<input type="hidden" name="ses_search_gyear" 	id="ses_search_gyear"/>
	
	
	
	</form>
	
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>

function boardView(grseq, subj, year, subjseq, subjnm, p_subjseq2, seq, tabseq, menu, submenu, url){
	 var frm = eval('document.<c:out value="${gsMainForm}"/>');
	 
	 frm.s_menu.value = menu;
	 frm.s_submenu.value = submenu;

	 frm.p_subj.value = subj;
	 frm.p_year.value = year;
	 frm.p_subjseq.value = subjseq;
	 frm.p_subjnm.value = subjnm;
	 frm.p_subjseq2.value = p_subjseq2;
	 frm.p_seq.value = seq;
	 frm.p_tabseq.value = tabseq;


	 frm.ses_search_subj.value = subj;
	 frm.ses_search_year.value = year;
	 frm.ses_search_subjseq.value = subjseq;
	 frm.ses_search_subjnm.value = subjnm;
	 frm.ses_search_grseq.value = grseq;
	 frm.ses_search_gyear.value = year;


	 
	 
	 frm.action = url;
	 frm.target = "_self";
	 frm.submit();
}

function qnaView(seq, url, tabseq){
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	frm.p_tabseq.value = tabseq;
	frm.p_seq.value = seq;
	frm.action = url;	
	frm.submit();
}

function doWriteForm(seq, tabseq){
	 var frm = eval('document.<c:out value="${gsMainForm}"/>');

	 frm.p_tabseq.value = tabseq;
	 frm.p_seq.value = seq;

	 window.open('', 'qnaForm', 'width=800, height=550, top=100, left=300, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbars=yes');
	 frm.action = "/adm/com/main/qnaInsertForm.do";
	 frm.target = "qnaForm";
	 frm.submit(); 
}

function doList(){
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	 frm.action = "/adm/com/main/admActionMainPage.do";
	 frm.submit(); 
}

function subjView(grseq, subj, year, subjseq, subjnm, menu, submenu, url){
	 var frm = eval('document.<c:out value="${gsMainForm}"/>');
	 
	 frm.s_menu.value = menu;
	 frm.s_submenu.value = submenu;
	 
	 frm.ses_search_subj.value = subj;
	 frm.ses_search_year.value = year;
	 frm.ses_search_subjseq.value = subjseq;
	 frm.ses_search_subjnm.value = subjnm;
	 frm.ses_search_grseq.value = grseq;
	 frm.ses_search_gyear.value = year;


	 frm.action = url;
	 frm.target = "_self";
	 frm.submit();
}




-->
</script>
	
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

