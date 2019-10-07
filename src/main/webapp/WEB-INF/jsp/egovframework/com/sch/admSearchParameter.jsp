<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<c:if test="${selectParameter eq 'DIC'}">
<div class="shLine"></div>
<div>		
	<ul class="datewrap">
		<li class="floatL">
			<strong class="shTit">용어</strong>
			<input type="text" id="search_text" name="search_text" value="${search_text}" onkeypress="fn_checkEnter(event)">(입력값 없이 조회를 누르면 전체용어를 볼 수 있습니다.)
		</li>
	</ul>		
</div>
</c:if>


<c:if test="${selectParameter eq 'PROP'}">
<div class="shLine"></div>
<div>		
	<ul class="datewrap">
		<li><strong class="shTit">처리상태</strong>
            <select name="search_appstatus">
				<option value="" 	>전체</option>
				<option value="B" 	<c:if test="${search_appstatus eq 'B'}">selected</c:if>>미처리</option>
				<option value="Y" 	<c:if test="${search_appstatus eq 'Y'}">selected</c:if>>승인</option>
				<option value="N"	<c:if test="${search_appstatus eq 'N'}">selected</c:if>>반려</option>
			</select>
        </li>
        <li><strong class="shTit">결제수단</strong>
        	<select name="search_payType">
				<option value=""		>전체</option>
				<option value="PB"		<c:if test="${search_payType eq 'PB'}">selected</c:if>>무통장</option>
				<option value="OB"		<c:if test="${search_payType eq 'OB'}">selected</c:if>>교육청일괄납부</option>
				<option value="RE"		<c:if test="${search_payType eq 'RE'}">selected</c:if>>재수강</option>
				<option value="SC0010"	<c:if test="${search_payType eq 'SC0010'}">selected</c:if>>신용카드결제</option>
				<option value="SC0030"	<c:if test="${search_payType eq 'SC0030'}">selected</c:if>>계좌이체</option>
				<option value="SC0040"	<c:if test="${search_payType eq 'SC0040'}">selected</c:if>>가상계좌</option>
				<option value="FE"	<c:if test="${search_payType eq 'FE'}">selected</c:if>>무료</option>
				<option value="etc"		<c:if test="${search_payType eq 'etc'}">selected</c:if>>기타</option>
			</select>
        </li> 
		<li class="floatL">
			<strong class="shTit">학습자</strong>
			<select name="search_group">
				<option value="name"		<c:if test="${search_group eq 'name'}">selected</c:if>>이름</option>
				<option value="userid"		<c:if test="${search_group eq 'userid'}">selected</c:if>>ID</option>
				<option value="handphone"	<c:if test="${search_group eq 'handphone'}">selected</c:if>>휴대폰</option>
				<option value="user_path"	<c:if test="${search_group eq 'user_path'}">selected</c:if>>학교명</option>
			</select>
			
			<input type="text" id="search_text" name="search_text" value="${search_text}" onkeypress="fn_checkEnter(event)">
		</li>
		<li>
			<strong class="shTit">지역</strong>
			<ui:code id="search_area" selectItem="${search_area}" codetype="0118" levels="1" title="지역구분" type="select" selectTitle="전체" deleteItem="A00"/>
		</li>
		
		
	</ul>		
</div>
</c:if>
<c:if test="${selectParameter eq 'PROPCANCEL'}">
<div class="shLine"></div>
<div>		
	<ul class="datewrap">
        <li><strong class="shTit">결제수단</strong>
        	<select name="search_payType">
				<option value=""		>전체</option>
				<option value="PB"		<c:if test="${search_payType eq 'PB'}">selected</c:if>>무통장</option>
				<option value="OB"		<c:if test="${search_payType eq 'OB'}">selected</c:if>>교육청일괄납부</option>
				<option value="RE"		<c:if test="${search_payType eq 'RE'}">selected</c:if>>재수강</option>
				<option value="SC0010"	<c:if test="${search_payType eq 'SC0010'}">selected</c:if>>신용카드결제</option>
				<option value="SC0030"	<c:if test="${search_payType eq 'SC0030'}">selected</c:if>>계좌이체</option>
				<option value="SC0040"	<c:if test="${search_payType eq 'SC0040'}">selected</c:if>>가상계좌</option>
				<option value="etc"		<c:if test="${search_payType eq 'etc'}">selected</c:if>>기타</option>
			</select>
        </li> 
		<li class="floatL">
			<strong class="shTit">학습자</strong>
			<select name="search_group">
				<option value="name"		<c:if test="${search_group eq 'name'}">selected</c:if>>이름</option>
				<option value="userid"		<c:if test="${search_group eq 'userid'}">selected</c:if>>ID</option>
				<option value="handphone"	<c:if test="${search_group eq 'handphone'}">selected</c:if>>휴대폰</option>
				<option value="user_path"	<c:if test="${search_group eq 'user_path'}">selected</c:if>>학교명</option>
			</select>
			
			<input type="text" id="search_text" name="search_text" value="${search_text}" onkeypress="fn_checkEnter(event)">
		</li>
		<li>
			<strong class="shTit">지역</strong>
			<ui:code id="search_area" selectItem="${search_area}" codetype="0118" levels="1" title="지역구분" type="select" selectTitle="전체" deleteItem="A00"/>
		</li>
		
	</ul>		
</div>
</c:if>
<c:if test="${selectParameter eq 'STUDENT'}">
<div class="shLine"></div>
<div>		
	<ul class="datewrap">
		<li class="floatL">
			<strong class="shTit">학습자</strong>
			<select name="search_group">
				<option value="name"		<c:if test="${search_group eq 'name'}">selected</c:if>>이름</option>
				<option value="userid"		<c:if test="${search_group eq 'userid'}">selected</c:if>>ID</option>
				<option value="handphone"	<c:if test="${search_group eq 'handphone'}">selected</c:if>>휴대폰</option>
				<option value="user_path"	<c:if test="${search_group eq 'user_path'}">selected</c:if>>학교명</option>
			</select>
			
			<input type="text" id="search_text" name="search_text" value="${search_text}" onkeypress="fn_checkEnter(event)">
		</li>
		<li>
			<strong class="shTit">지역</strong>
			<ui:code id="search_area" selectItem="${search_area}" codetype="0118" levels="1" title="지역구분" type="select" selectTitle="전체" deleteItem="A00"/>
		</li>
		
	</ul>		
</div>
</c:if>
<c:if test="${selectParameter eq 'TOTSCORE'}">
<div class="shLine"></div>
<div>
	<ul class="datewrap">
		<li class="floatL">
			<strong class="shTit">출석일수</strong>
			<select name="search_selGubun">
				<option value="1" <c:if test="${search_selGubun eq '1'}">selected</c:if>>출석일수</option>
				<option value="2" <c:if test="${search_selGubun eq '2'}">selected</c:if>>총점</option>
				<option value="3" <c:if test="${search_selGubun eq '3'}">selected</c:if>>리포트</option>
				<option value="4" <c:if test="${search_selGubun eq '4'}">selected</c:if>>온라인평가</option>
			</select>
      		<input name="search_selStart" type="text" size="5" value="${search_selStart}">부터
			<input name="search_selEnd" type="text" size="5"  value="${search_selEnd}">까지
       </li>
       
       
        <li class="floatL">	             		
			<strong class="shTit">평가응시여부</strong>				
			<select name="search_isexam">
				<option value="" >전체</option>
				<option value="Y" <c:if test="${search_isexam eq 'Y'}">selected</c:if>>응시</option>
				<option value="N" <c:if test="${search_isexam eq 'N'}">selected</c:if>>미응시</option>
			</select>
		</li>
		
		
        <li class="floatL">	
			<strong class="shTit">리포트제출여부</strong>				
			<select name="search_isreport">
				<option value="" >전체</option>
				<option value="Y" <c:if test="${search_isreport eq 'Y'}">selected</c:if>>제출</option>
				<option value="N" <c:if test="${search_isreport eq 'N'}">selected</c:if>>미제출</option>
			</select>
		</li>
		
		
        <li class="floatL">	
			<strong class="shTit">수료여부</strong>				
			<select name="search_isgrad">
				<option value="" >전체</option>
				<option value="M" <c:if test="${search_isgrad eq 'M'}">selected</c:if>>진행중</option>
				<option value="Y" <c:if test="${search_isgrad eq 'Y'}">selected</c:if>>수료</option>
				<option value="N" <c:if test="${search_isgrad eq 'N'}">selected</c:if>>미수료</option>
			</select>
		</li>
	</ul>		
</div>
<div class="shLine"></div>
<div>		
	<ul class="datewrap">
	
		<li class="floatL">
			<strong class="shTit">학습자</strong>
			<select name="search_group">
				<option value="name"		<c:if test="${search_group eq 'name'}">selected</c:if>>이름</option>
				<option value="userid"		<c:if test="${search_group eq 'userid'}">selected</c:if>>ID</option>
				<option value="handphone"	<c:if test="${search_group eq 'handphone'}">selected</c:if>>휴대폰</option>
				<option value="user_path"	<c:if test="${search_group eq 'user_path'}">selected</c:if>>학교명</option>
			</select>
			
			<input type="text" id="search_text" name="search_text" value="${search_text}" onkeypress="fn_checkEnter(event)">
		</li>
		<li>
			<strong class="shTit">지역</strong>
			<ui:code id="search_area" selectItem="${search_area}" codetype="0118" levels="1" title="지역구분" type="select" selectTitle="전체" deleteItem="A00"/>
		</li>
		
	</ul>
</div>
</c:if>
<c:if test="${selectParameter eq 'EXAM'}">
<div class="shLine"></div>
<div>		
	<ul class="datewrap">
		<li class="floatL">
			<strong class="shTit">평가종류</strong>
		<!--			
			1. codetype은 과정코드로
			2. levels는 년도로
			3. condition은 기수코드로 사용한다.
		//-->

			<c:if test="${not empty ses_search_subj && not empty ses_search_gyear  && not empty ses_search_subjseq}">		
				<ui:code id="search_papernum" selectItem="${search_papernum}" gubun="examPaper" codetype="${ses_search_subj}"  levels="${ses_search_gyear}"  condition="${ses_search_subjseq}"
			upper="" title="평가종류" className="" type="select" selectTitle="평가를 선택하세요." event="" />
			</c:if>
		</li>
		
		
	</ul>		
</div>
</c:if>

<c:if test="${selectParameter eq 'MEMBER'}">
<div class="shLine"></div>
<div>		
	<ul class="datewrap">
        
        <li><strong class="shTit">가입연도</strong>
				<select name="ses_search_indate" id="ses_search_indate" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_indate}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
        </li>
        <li><strong class="shTit">연수 최종 이수연도</strong>
				<select name="ses_search_stold_year" id="ses_search_stold_year" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_stold_year}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
        </li>
        <li><strong class="shTit">교원자격등급</strong>
			<select name="p_job_cd" id="p_job_cd" title="교원자격등급" onchange="selectBoxOnChange()">	                      
			    <option value="">===선택===</option>
			    <c:forEach items="${subjectList}" var="result" varStatus="i">
			    	<option value="${result.code }" <c:if test="${view.jobCd eq result.code }">selected</c:if> >${result.codenm }</option>	
			    </c:forEach>
			</select>
        </li>
        <li><strong class="shTit">회원구분</strong>
			<select name="p_emp_gubun">
				<option value="" <c:if test="${p_emp_gubun eq ''}">selected</c:if>>전체</option>
				<option value="T" <c:if test="${p_emp_gubun eq 'T'}">selected</c:if>>교원</option>
				<option value="E" <c:if test="${p_emp_gubun eq 'E'}">selected</c:if>>보조인력</option>
				<option value="R" <c:if test="${p_emp_gubun eq 'R'}">selected</c:if>>교육 전문직</option>
				<option value="P" <c:if test="${p_emp_gubun eq 'P'}">selected</c:if>>일반회원(학부모 등)</option>
			</select>
        </li>
       </ul>
</div>

<div class="shLine"></div>
<div>		
	<ul class="datewrap">  
        
	    <li class="floatL">
			<strong class="shTit">학습자</strong>
			<select id="p_search" name="p_search">
				<option value="b.name" 		<c:if test="${p_search == 'b.name'}">selected</c:if>>이름</option>
				<option value="b.userid" 		<c:if test="${p_search == 'b.userid'}">selected</c:if>>아이디</option>
				<option value="handphone" 	<c:if test="${p_search == 'handphone'}">selected</c:if>>핸드폰번호</option>
				<option value="email" 		<c:if test="${p_search == 'email'}">selected</c:if>>EMAIL</option>
				<option value="user_path" 	<c:if test="${p_search == 'user_path'}">selected</c:if>>학교</option>
				<option value="birth_date" 	<c:if test="${p_search == 'birth_date'}">selected</c:if>>생년월일</option>
				<option value="nice_personal_num" 	<c:if test="${p_search == 'nice_personal_num'}">selected</c:if>>나이스개인번호</option>
			</select>	
			
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" style="ime-mode:active" onkeypress="javascript:fn_keyEvent('searchList')"/>
			
		</li>
         
		<li class="floatL">
			<strong class="shTit">최근접속일</strong>
			<input name="p_search_lglast" type="text" size="10" maxlength="10" readonly value="${p_search_lglast}"> 
			<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_lglast, 'yyyymmdd')" />
			
		</li>
		<li class="floatL">
			<strong class="shTit">참여율</strong>			
			<input type="text" name="p_tstep" id="p_tstep" value="${p_tstep}" size="10" />%
			
		</li>
		<li class="floatL">
			<strong class="shTit">온라인평가 점수</strong>			
			<input type="text" name="p_tavftest" id="p_tavftest" value="${p_tavftest}"  size="10" />
			
		</li>
		
	</ul>		
</div>
</c:if>


<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>


