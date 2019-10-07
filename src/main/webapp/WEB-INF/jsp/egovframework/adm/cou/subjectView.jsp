<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<script type="text/javascript">
window.addEventListener('load', function(){
	document.getElementById("youtubeDiv").style.display ="none";	
	if(document.getElementById("youtubeYn").value == "Y" ){
		document.getElementById("youtubeDiv").style.display ="block";
		document.getElementById("fileDiv").style.display ="none";
	}else{
		document.getElementById("youtubeDiv").style.display ="none";
		document.getElementById("fileDiv").style.display ="block";
	}
});

function selectChkYoutube(chkbox){
	if(chkbox.checked){
		document.getElementById("youtubeYn").value = "Y";
		document.getElementById("youtubeDiv").style.display ="block";
		document.getElementById("fileDiv").style.display ="none";
		
		
	}
	else{
		document.getElementById("youtubeYn").value = "N";
		document.getElementById("youtubeDiv").style.display ="none";
		document.getElementById("fileDiv").style.display ="block";
	}
}

</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype = "multipart/form-data" >
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="subj" 		name="subj"			value="${view.subj}">
	<input type="hidden" name="isonoff"		value="ON"                          >
	<input type="hidden" name="isintroduction"value="Y">
	
	<input type="hidden" id="tmp1" 		name="tmp1"			value="">	
    <input type="hidden" id="specials" 		name="specials">
    <input type="hidden" name="p_gubun">
    <input type="hidden" name="p_key1">
    <input type="hidden" name="p_key2">
    <input type="hidden" name="process">
    <input type="hidden" name="upperclassnum">
    <input type="hidden" id="youtubeYn" name="youtubeYn" value="${view.youtubeYn}" />

<!--	교육그룹코드-->
	<input type="hidden" id="grcode"        name="grcode"        value="N000001">
	
	
	
        <!-- tab -->
        <div class="conwrap2">
            <ul class="mtab2">
                <li><a href="#" class="on">기본정보</a></li>
          </ul>
        </div>
		<!-- // tab-->
        
		<!-- btn -->
        <div class="listTop">            			
                <div class="btnR">
                	<a href="#none" onclick="doFormReset()" class="btn01"><span>취 소</span></a>
                </div>
                
<c:if test="${ !empty view.subj }">

				<div class="btnR MR05">
	<c:if test="${ view.subjseqcoun == 0 }">
		<c:if test="${ view.subjobjcount == 0 }">
					<a href="#none" onclick="doSubjAction('delete')" class="btn01"><span>삭 제</span></a>
		</c:if>
		<c:if test="${ view.subjobjcount > 0 }">
                	<a href="#none" onclick="alert('과정에 컨텐츠가 매핑되어있어 삭제할 수 없습니다.')" class="btn01"><span>삭 제</span></a>
		</c:if>
	</c:if>

	<c:if test="${ view.subjseqcoun > 0 }">
                	<a href="#none" onclick="alert('과정기수 정보가 있어 삭제할 수 없습니다.')" class="btn01"><span>삭 제</span></a>
	</c:if>
				</div>
				
				
                <div class="btnR MR05">
                	<a href="#none" onclick="doSubjAction('update')" class="btn01"><span>저 장</span></a>
                </div>
</c:if>

<c:if test="${ empty view.subj }">
                <div class="btnR MR05">
                	<a href="#none" onclick="doSubjAction('insert')" class="btn01"><span>저 장</span></a>
                </div>
</c:if>

                <div class="btnR MR05">
               		<a href="#none" class="btn01" onclick="doPageList()"><span>목 록</span></a>
                </div>                        
		</div>
        <!-- //btn -->



        <!-- detail wrap -->
        <div class="tbDetail">
            <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                    <col width="15%" />
                    <col width="35%" />
                    <col width="15%" />
                    <col width="35%" />                    
                </colgroup>
                <tbody>
                    <tr class="title">
                        <th>과정명<font color="red">(*)</font></th>
                        <td class="" colspan="3">
                               <input type="text" name="subjnm" size="80" maxlength="100" value="${view.subjnm}"> [<c:out value="${view.subj}"></c:out>]
                            &nbsp;&nbsp;연수영역 : 
                          	<select id='pjobcd' name='pjobcd' class='' title='연수영역'  >
								<option value='1' <c:if test="${view.pjobcd eq '1'}">selected</c:if>>교직기본소양</option>
								<option value='2' <c:if test="${view.pjobcd eq '2'}">selected</c:if>>교수학습지도</option>
								<option value='3' <c:if test="${view.pjobcd eq '3'}">selected</c:if>>학급경영(생활지도)</option>
								<option value='4' <c:if test="${view.pjobcd eq '4'}">selected</c:if>>일반교양</option>
							</select>                            
                        </td>
                    </tr>
                    <tr class="title">
                        <th>모바일지원여부<font color="red">(*)</font></th>
                        <td class="" colspan="3">
                          	<ui:code id="mobile" selectItem="${view.mobile}" gubun="defaultYN" codetype=""  
                          	upper="" title="모바일지원여부" className="" type="select" selectTitle="::선택::" event="" />
                          	※ 모바일과정을 지원할 경우 Yes를 선택하세요. 모바일과정에 필요한 콘텐츠를 꼭 등록하셔야 합니다.
                        </td>
                    </tr>
                    <tr>
                        <th>과정분류<font color="red">(*)</font></th>
                        <td>
                      		<ui:code id="upperclass" selectItem="${view.upperclass}" gubun="cursBunryu" codetype=""  upper="" title="과정분류" className="" type="select" selectTitle="ALL" event="" />
							<input type="hidden" name="middleclass" value="000">
							<input type="hidden" name="lowerclass"  value="000">
                        </td> 
                        <th>컨텐츠타입<font color="red"><font color="red">(*)</font></font></th>
                        <td>
                        	<ui:code id="contenttype" selectItem="${view.contenttype}" gubun="" codetype="0007" levels="1" upper="" title="컨텐츠타입" className="" type="select" selectTitle="" event="changeContentType(this)" />
                        </td>
                    </tr>
                    <tr>
						<th>1일최대학습량<font color="red">(*)</font></th>
                        <td>
                        <c:if test="${ !empty view.subj }">
                        	<input type="text" name="edulimit" size="3" maxlength="3" value="${view.edulimit}">&nbsp;%
                        </c:if>
                        <c:if test="${ empty view.subj }">
                        	<input type="text" name="edulimit" size="3" maxlength="3" value="0">&nbsp;%
                        </c:if>
                        
                        </td>
                        <th>과정특성</th>
                        <td class="borderR">
                        <c:set var="specials" value="${view.specials}"/>
						<c:set var="length" value="${fn:length(specials)}"/>
						<c:set var="new" value="N"/>
						<c:set var="hit" value="N"/>
						<c:set var="recom" value="N"/>
						<c:if test="${!empty specials}">
							<c:if test="${fn:length(specials) > 0}">
								<c:set var="new" value="${fn:substring(specials,0,1)}"/>
							</c:if>
							<c:if test="${fn:length(specials) > 1}">
								<c:set var="hit" value="${fn:substring(specials,1,2)}"/>
							</c:if>
							<c:if test="${fn:length(specials) > 2}">
								<c:set var="recom" value="${fn:substring(specials,2,3)}"/>
							</c:if>
						</c:if>
                        
							 신규 
			                <ui:code id="p_new" selectItem="${new}" gubun="defaultYN" codetype=""  upper="" title="신규" className="" type="select" selectTitle="" event="" />
			                /  인기
			                <ui:code id="hit" selectItem="${hit}" gubun="defaultYN" codetype=""  upper="" title="인기" className="" type="select" selectTitle="" event="" />
			                /  추천 
			                <ui:code id="recom" selectItem="${recom}" gubun="defaultYN" codetype=""  upper="" title="추천" className="" type="select" selectTitle="" event="" />                       
                        </td>
                    </tr>
                    <tr>
                          <th>과정사용</th>
                          <td class="borderR">
                          	<ui:code id="isuse" selectItem="${view.isuse}" gubun="defaultYN" codetype=""  upper="" title="과정사용" className="" type="select" selectTitle="" event="whenIsuse()" />
                          </td>
                          <th>학습자에게<br/>보여주기</th>
                          <td class="borderR">
                          	<ui:code id="isvisible" selectItem="${view.isvisible}" gubun="defaultYN" codetype=""  upper="" title="학습자에게 보여주기" className="" type="select" selectTitle="" event="" />
                          </td>                        
                    </tr>
                    
                   
                    <tr>
                        <!-- <th>수강료 납부방식</th> -->
                        <th>운영형태</th>
                        <td class="borderR">
                          	<select id='ischarge' name='ischarge' class='' title='운영형태'  >
								<%-- <option value='C' <c:if test="${view.ischarge eq 'C'}">selected</c:if>>일반결제(전자결제, 교육청일괄납부)</option>
								<option value='S' <c:if test="${view.ischarge eq 'S'}">selected</c:if>>특별과정(교육청일괄납부)</option>
								<option value='F' <c:if test="${view.ischarge eq 'F'}">selected</c:if>>무료</option> --%>
								<option value='C' <c:if test="${view.ischarge eq 'C'}">selected</c:if>>정규</option>
								<option value='S' <c:if test="${view.ischarge eq 'S'}">selected</c:if>>특별</option>
							</select>
                        </td>
                        <th>수강료<font color="red">(*)</font></th>
                        <td>
                        <c:if test="${ !empty view.subj }">
                        	<input type="text" name="biyong" size="10" maxlength="7" value="${view.biyong}"> 원
                        </c:if>
                        <c:if test="${ empty view.subj }">
                        	<input type="text" name="biyong" size="10" maxlength="7" value="0"> 원
                        </c:if>
                        
                        </td>                        
                    </tr>
                    
                    <tr>
                        <th>컨텐츠 lesson 전체보기</th>
                        <td class="borderR">
                          	<select id='contentLessonAllView' name='contentLessonAllView' class='' title='컨텐츠 lesson 전체보기'  >								
								<option value='N' <c:if test="${view.contentLessonAllView eq 'N'}">selected</c:if>>No</option>
								<option value='Y' <c:if test="${view.contentLessonAllView eq 'Y'}">selected</c:if>>Yes</option>
							</select>
                        </td>
                        
                        <th>학습창 왼쪽 학습현황 보기</th>
                        <td class="borderR">
                          	<select id='leftContentItemListView' name='leftContentItemListView' class='' title='학습창 왼쪽 학습현황 보기'  >								
								<option value='N' <c:if test="${view.leftContentItemListView eq 'N'}">selected</c:if>>No</option>
								<option value='Y' <c:if test="${view.leftContentItemListView eq 'Y'}">selected</c:if>>Yes</option>
							</select>
                        </td>                   
                    </tr>
                    
                    
                    <tr>                        
                        <th>제작년도<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" size="2" maxlength="2" name="conyear" value="${view.conyear}"> 예)16
                        </td>
                        <th>리뉴얼년도<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" size="2" maxlength="2" name="renewalYear" value="${view.renewalYear}"> 예)16
                        </td>
                    </tr>
                    <tr>
                        <th>콘텐츠 버전<font color="red">(*)</font></th>
                        <td>
                        	<select name="confr">
                        		<option value=''>선택</option>
                        		<option value="F" <c:if test="${view.confr eq 'F' }">selected</c:if>>최초버전</option>
                        		<option value="R" <c:if test="${view.confr eq 'R' }">selected</c:if>>리뉴얼버전</option>                        		
                        	</select> 
                        </td>                                                
                        
                        <th>리뉴얼 차순<font color="red">(*)</font></th>
                        <td>
                        	<select name="conrenum">
                        		<option value=''>선택</option>
                        		<option value="0" <c:if test="${view.conrenum eq '0' }">selected</c:if>>0</option>
                        		<option value="1" <c:if test="${view.conrenum eq '1' }">selected</c:if>>1</option>                        		
                        		<option value="2" <c:if test="${view.conrenum eq '2' }">selected</c:if>>2</option>
                        		<option value="3" <c:if test="${view.conrenum eq '3' }">selected</c:if>>3</option>
                        		<option value="4" <c:if test="${view.conrenum eq '4' }">selected</c:if>>4</option>
                        		<option value="5" <c:if test="${view.conrenum eq '5' }">selected</c:if>>5</option>
                        		<option value="6" <c:if test="${view.conrenum eq '6' }">selected</c:if>>6</option>
                        		<option value="7" <c:if test="${view.conrenum eq '7' }">selected</c:if>>7</option>
                        		<option value="8" <c:if test="${view.conrenum eq '8' }">selected</c:if>>8</option>
                        		<option value="9" <c:if test="${view.conrenum eq '9' }">selected</c:if>>9</option>
                        		<option value="10" <c:if test="${view.conrenum eq '10' }">selected</c:if>>10</option>
                        	</select> (최초버전은 0)
                        </td>
                    </tr>
                    <!-- 2017 추가 -->
                    <tr>
                        <th>콘텐츠 사용<font color="red">(*)</font></th>
                        <td>
                        	<select name="iscontentsuse">
                        		<option value='Y' <c:if test="${view.iscontentsuse eq 'Y' }">selected</c:if>>Yes</option>
                        		<option value='N' <c:if test="${view.iscontentsuse eq 'N' }">selected</c:if>>No</option>
                        	</select>
                        </td>                                                                       
                    	<th>포팅날짜</th>
                        <td>
                        	<input id="portingDate" name="portingDate" type="text" size="8" maxlength="8" value="${view.portingDate}">
			            	<img src="/images/adm/ico/ico_calendar.gif" onclick="popUpCalendar(this, document.all.portingDate, 'yyyymmdd')" align="middle" style="cursor: pointer;"/>
                        </td>
                    </tr>
                    <tr>
                        <th>콘텐츠 수정일</th>
                        <td>
                        	<input id="contentModifyDate" name="contentModifyDate" type="text" size="8" maxlength="8" value="${view.contentModifyDate}">
			            	<img src="/images/adm/ico/ico_calendar.gif" onclick="popUpCalendar(this, document.all.contentModifyDate, 'yyyymmdd')" align="middle" style="cursor: pointer;"/>
                        </td>
                    	<th>최종콘텐츠</th>
                        <td>
                        	<select name="finalContentYn">
                        		<option value='N' <c:if test="${view.finalContentYn eq 'N' }">selected</c:if>>NO</option>
                        		<option value='Y' <c:if test="${view.finalContentYn eq 'Y' }">selected</c:if>>YES</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                        <th>mp3 보유여부</th>
                        <td >
                        	<select name="mp3PossessYn">
                        		<option value='N' <c:if test="${view.mp3PossessYn eq 'N' }">selected</c:if>>NO</option>
                        		<option value='Y' <c:if test="${view.mp3PossessYn eq 'Y' }">selected</c:if>>YES</option>
                        	</select>
                        </td>
                        <th>합격증번호</th>
                        <td>	                        
	     					<input type="text" name="certificate_no" size="15" maxlength="15" value="${view.certificateNo}">
                        </td>
                    </tr>
                    <!-- 2017 추가 끝 -->
                    <!-- <tr>
                        <th colspan="4" style="text-align:center">
                        과정코드 뒷자리 4~10자리 의미<br/>
						4~5자리 - 제작연도 또는 리뉴얼연도<br/>
						6자리    - 최초버젼은 F, 리뉴얼버젼은 R<br/>
						7자리    - 리뉴얼 차순(최초버젼은 리뉴얼하지 않아 0표시, 1차 리뉴얼본은1표시, 2차 리뉴얼본은 2표시<br/> 
						8자리    - 과정분류(교원직무1, 보조2, 기타3, 패키지4, 시범5)<br/>
						9~10자리- 차시(61차시 과정은 61, 30차시 과정은 30)<br/>

                        </th>
                    </tr> -->
                    
                   
                </tbody>
            </table>				
		</div>
        <!-- // detail wrap -->  




        <!-- tab -->
        <div class="conwrap2 mrt30">
            <ul class="mtab2">
                <li><a href="#" class="on">학습정보</a></li>
         	</ul>
        </div>
        
        
		<div class="tbDetail">
            <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                    <col width="15%" />
                    <col width="35%" />
                    <col width="15%" />
                    <col width="35%" />                    
                </colgroup>
                <tbody>
                	<tr>
                        <th colspan="4" style="text-align:center">학습정보</th>
                    </tr>
					<tr>
                        <th>학습일차<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<c:if test="${ !empty view.subj }">
	                        	<input type="text" name="edudays" size="10" maxlength="5" value="${view.edudays}">
	                        </c:if>
	                        <c:if test="${ empty view.subj }">
	                        	<input type="text" name="edudays" size="10" maxlength="5" value="0">
	                        </c:if>
                        </td>
                        <th>연수기간<font color="red">(*)</font></th>
                        <td>
                        	<c:if test="${ !empty view.subj }">
	                        	<input type="text" name="eduperiod" size="5" maxlength="2" value="${view.eduperiod}"> 주
	                        </c:if>
	                        <c:if test="${ empty view.subj }">
	                        	<input type="text" name="eduperiod" size="5" maxlength="2" value="0"> 주
	                        </c:if>
                        	
                        </td>                        
                    </tr>
                    <tr>
                        <th>학습시간<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<c:if test="${ !empty view.subj }">
	                        	<input type="text" name="edutimes" size="10" maxlength="5" value="${view.edutimes}"/> 시간
	                        </c:if>
	                        <c:if test="${ empty view.subj }">
	                        	<input type="text" name="edutimes" size="10" maxlength="5" value="0"/> 시간
	                        </c:if>
                        	
                        </td>
                        <th>정원<font color="red">(*)</font></th>
                        <td>
                        	<c:if test="${ !empty view.subj }">
	                        	<input type="text" name="studentlimit" size="10" maxlength="5" value="${view.studentlimit}"> 명
	                        </c:if>
	                        <c:if test="${ empty view.subj }">
	                        	<input type="text" name="studentlimit" size="10" maxlength="5" value="0"> 명
	                        </c:if>
                        	
                        </td>                        
                    </tr>
					<tr>
                        <th>학습방법(WBT%)<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<c:if test="${ !empty view.subj }">
	                        	<input type="text" name="ratewbt" size="10" maxlength="3" value="${view.ratewbt}">

	                        </c:if>
	                        <c:if test="${ empty view.subj }">
	                        	<input type="text" name="ratewbt" size="10" maxlength="3" value="90">
	                        </c:if>
                        	<strong>※ (학습 참여도) 90% 평가점수 변경</strong>
                        </td>
                        <th>학습방법(VOD%)<font color="red">(*)</font></th>
                        <td>
                        	<c:if test="${ !empty view.subj }">
	                        	<input type="text" name="ratevod" size="10" maxlength="3" value="${view.ratevod}">
	                        </c:if>
	                        <c:if test="${ empty view.subj }">
	                        	<input type="text" name="ratevod" size="10" maxlength="3" value="0">
	                        </c:if>
                        	
                        </td>                        
                    </tr>
                    <tr>
                        <th>진도체크랜덤여부<br/>(랜덤/순차)</th>
                        <td class="borderR">
                        	<ui:code id="contentprogress" selectItem="${view.contentprogress}" gubun="defaultYN" codetype=""  upper="" title="진도체크랜덤여부" className="" type="select" selectTitle="" event="" />
                        </td>
                        <th>복습가능</th>
                        <td>
                        	<ui:code id="isablereview" selectItem="${view.isablereview}" gubun="defaultYN" codetype=""  upper="" title="복습가능" className="" type="select" selectTitle="" event="whenIsablereview()" />
                        	/ 
                        	<c:if test="${ !empty view.subj }">
	                        	<input type="text" id="reviewdays" name="reviewdays" value="${view.reviewdays}" size="2" maxlength="2"> 개월
	                        </c:if>
	                        <c:if test="${ empty view.subj }">
	                        	<input type="text" id="reviewdays" name="reviewdays" value="0" size="2" maxlength="2"> 개월
	                        </c:if>
                        	
                        	
                        </td>                        
                    </tr> 
                    <tr>
                        <th>학점<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<c:if test="${ !empty view.subj }">
	                        	<input type="text" name="point" value="${view.point}" size="4" maxlength="3">
	                        </c:if>
	                        <c:if test="${ empty view.subj }">
	                        	<input type="text" name="point" value="0" size="4" maxlength="3">
	                        </c:if>
                        	
                        </td>
                        <th>교육수준</th>
                        <td>
                        	<ui:code id="lev" selectItem="${view.lev}" gubun="eduCode" codetype="013"  upper="" title="교육수준" className="" type="select" selectTitle="" event="" />
                        </td>             
                    </tr>
                	
                	
                	
                	
                	
<c:if test="${ !empty view.subj }">
					<tr>
                        <th colspan="4" style="text-align:center">가중치 / <font color="red">※가중치(%)의 합은 100%이어야 합니다.</font></th>
                    </tr>
                    <tr>
                        <th>온라인평가<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<input type="text" name="wmtest" size="10" maxlength="5" value="${view.wmtest}"> (%)
                        </td>
                        <th>중간평가<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" name="wftest" size="10" maxlength="5" value="${view.wftest}"> (%)
                        </td>                        
                    </tr>
                    <tr>
                        <th>온라인과제<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<input type="text" name="wreport" size="10" maxlength="5" value="${view.wreport}"> (%)
							<input type="hidden" name="wact" value="0">
                        </td>
                        <th>참여도(출석일)<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" name="wetc2" size="10" maxlength="5" value="${view.wetc2}"> (%)
                        	<br/>
                        	* 가중치 참여도(학습진도율) <!-- :60시간 이상 10 %, 이하는 20% 입니다. -->
                        </td>                        
                    </tr>
                    
                    <tr>
                        <th colspan="4" style="text-align:center">수료기준</th>
                    </tr>
					<tr>
                        <th>총 점<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<input type="text" name="gradscore" size="10" maxlength="5" value="${view.gradscore}"> 이상
                        </td>
                        <th>출석시험<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" size="10" name="gradexam" value="${view.gradexam}"> 이상
                        </td>                        
                    </tr>
					<tr>
                        <th>온라인시험<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<input type="text" size="10" name="gradftest" value="${view.gradftest}"> 이상
                        </td>
                        <th>온라인과제<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" size="10" name="gradreport" value="${view.gradreport}"> 이상
                        </td>                        
                    </tr>
                    <tr>
                        <th>참여도(학습진도율)<font color="red">(*)</font></th>
                        <td colspan="3">
                        	<input type="text" name="wetc1" size="10" maxlength="5" value="${view.wetc1}"> 이상
                        </td>                                               
                    </tr>
                    
</c:if>
 
<c:if test="${ empty view.subj }">                    
                    <tr>
                        <th colspan="4" style="text-align:center">가중치 / <font color="red">※가중치(%)의 합은 100%이어야 합니다.</font></th>
                    </tr>
                    <tr>
                        <th>출석시험<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<input type="text" name="wmtest" size="10" maxlength="5" value="0"> (%)
                        </td>
                        <th>온라인시험<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" name="wftest" size="10" maxlength="5" value="0"> (%)
                        </td>                        
                    </tr>
                    <tr>
                        <th>온라인과제<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<input type="text" name="wreport" size="10" maxlength="5" value="0"> (%)
							<input type="hidden" name="wact" value="0">
                        </td>
                        <th>참여도(학습진도율)<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" name="wetc2" size="10" maxlength="5" value="0"> (%)
                        	<br/>
                        	* 가중치 참여도(학습진도율) <!-- :60시간 이상 10 %, 이하는 20% 입니다. -->
                        </td>                        
                    </tr>
                    
                    <tr>
                        <th colspan="4" style="text-align:center">수료기준</th>
                    </tr>
					<tr>
                        <th>총 점<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<input type="text" name="gradscore" size="10" maxlength="5" value="0"> 이상
                        </td>
                        <th>출석시험<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" size="10" name="gradexam" value="0"> 이상
                        </td>                        
                    </tr>
					<tr>
                        <th>온라인시험<font color="red">(*)</font></th>
                        <td class="borderR">
                        	<input type="text" size="10" name="gradftest" value="0"> 이상
                        </td>
                        <th>온라인과제<font color="red">(*)</font></th>
                        <td>
                        	<input type="text" size="10" name="gradreport" value="0"> 이상
                        </td>                        
                    </tr>
                    <tr>
                        <th>참여도(학습진도율)<font color="red">(*)</font></th>
                        <td colspan="3">
                        	<input type="text" name="wetc1" size="10" maxlength="5" value="0"> 이상
                        </td>                      
                                               
                    </tr>
</c:if>
                    
                    
                </tbody>
            </table>				
		</div>



        <!-- tab -->
        <div class="conwrap2 mrt30">
            <ul class="mtab2">
                <li><a href="#" class="on">부가정보</a></li>
         	</ul>
        </div>
        
        
		<div class="tbDetail">
            <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                    <col width="15%" />
                    <col width="35%" />
                    <col width="15%" />
                    <col width="35%" />                    
                </colgroup>
                <tbody>
					<tr>
                          <th>열린과정</th>
                          <td class="borderR">
                          	<ui:code id="isopenedu" selectItem="${view.isopenedu}" gubun="defaultYN" codetype=""  upper="" title="열린과정" className="" type="select" selectTitle="" event="" />
                          </td>
                          <th>수강신청<br/>자동승인여부</th>
                          <td class="borderR">
                          	<ui:code id="proposetype" selectItem="${view.proposetype}" gubun="defaultYN" codetype=""  upper="" title="수강신청 자동승인여부" className="" type="select" selectTitle="" event="" />
                          </td>
                    </tr>



                    <tr>
                        <th>교재사용</th>
                        <td class="borderR">
                        	<ui:code id="usebook" selectItem="${view.usebook}" gubun="defaultYN" codetype=""  upper="" title="교재사용" className="" type="select" selectTitle="" event="whenUsebook()" />
                        	<script>
                        		if(null == '${view.usebook}' || '' == '${view.usebook}') {
                        			eval('document.<c:out value="${gsMainForm}"/>').usebook.value='N';
                        		}
                        	</script>
                        </td>
                        <th>교재비<font color="red">(*)</font></th>
                        <td class="borderR">
 
							<c:if test="${ !empty view.subj }"> 
								<input type="text" name="bookprice" size="10" maxlength="5" value="${view.bookprice}"> 원
                        	</c:if>
 
							<c:if test="${ empty view.subj }"> 
								<input type="text" name="bookprice" size="10" maxlength="5" value="0"> 원
							</c:if>
                        	
                        </td>                
                    </tr>
                    <tr>
						<th>교재명</th>
                        <td colspan="3">
                        	<input type="text" name="bookname" size="38" maxlength="100" value="${view.bookname}">
                        </td>
                    </tr>
 					
					<tr>
                        <th>고용보험</th>
                        <td colspan="3">
                        	고용보험여부 : <ui:code id="isgoyong" selectItem="${view.isgoyong}" gubun="defaultYN" codetype=""  upper="" title="고용보험여부" className="" type="select" selectTitle="" event="whenIsgoyong()" />
                        	
                        	<c:if test="${ !empty view.subj }"> 
                        	/ 대 기 업 : <input type="text" id="goyongprice_major" name="goyongprice_major" size="15" maxlength="10" value="${view.goyongprice_major}"> 원
                        	/ 우선지원대상 :    <input type="text" id="goyongprice_minor" name="goyongprice_minor" size="15" maxlength="10" value="${view.goyongprice_minor}"> 원
                        	</c:if>
 
							<c:if test="${ empty view.subj }"> 
                        	/ 대 기 업 : <input type="text" id="goyongprice_major" name="goyongprice_major" size="15" maxlength="10" value="0"> 원
                        	/ 우선지원대상 :    <input type="text" id="goyongprice_minor" name="goyongprice_minor" size="15" maxlength="10" value="0"> 원
							</c:if>
							                    	
                        </td>                        
                    </tr>
                    <tr>
                    	  <th>주강사</th>
                          <td colspan="3">
                          	아이디 : <input name="tutor" type="text" size="14" maxlength="14" value="${view.tutor}" readonly/>
                          	/ 이름 : <input name="tutornm" type="text" size="14" maxlength="14" value="${view.tutornm}" readonly>
                          	<a href="#none" onclick="searchTutor()" class="btn_search01"><span>조회</span></a>
                          </td>
                    </tr>
					<tr>
                          <th>컨텐츠담당자</th>
                          <td colspan="3">
                          	아이디 : <input name="cuserid" type="text" size="14" maxlength="14" value="${view.cuserid}" readonly>
                          	/ 이름 : <input name="cuseridnm" type="text" size="14" maxlength="14" value="${view.cuseridnm}" readonly>
                          	<a href="#none" onclick="searchCuser()" class="btn_search01"><span>조회</span></a>
                          </td>                        
                    </tr>
                    <tr>
                          <th>담당자</th>
                          <td colspan="3">
                          <c:if test="${ !empty view.subj }">
                          	아이디 : <input name="muserid" type="text" size="14" maxlength="14" value="${view.muserid}" readonly />
                          	/ 이름 : <input name="museridnm" type="text" size="14" maxlength="14" value="${view.museridnm}" readonly>
                          	/ 연락처 : <input type="text" name="musertel" value="${view.musertel}" size="14">
                          </c:if>
                          <c:if test="${ empty view.subj }">
                          	아이디 : <input name="muserid" type="text" size="14" maxlength="14" value="${sessionScope.userid}" readonly />
                          	/ 이름 : <input name="museridnm" type="text" size="14" maxlength="14" value="${sessionScope.name}" readonly>
                          	/ 연락처 : <input type="text" name="musertel" value="${sessionScope.handphone}" size="14">
                          </c:if>	
                          	<a href="#none" onclick="searchMuser()" class="btn_search01"><span>조회</span></a>
                          </td> 
                    </tr>
                    
                    
                    
					<tr>
                          <th>학습 URL</th>
                          <td colspan="3">
                          	<input type="text" name="eduurl" size="110" maxlength="150" value="${view.eduurl}">
			            	<br/>ex) http://iedu.nise.go.kr/sample/contents/intro.html
                          </td> 
                    </tr>
					<tr>
                          <th>맛보기 URL</th>
                          <td colspan="3">
                          	<input type="text" name="preurl" size="110" maxlength="150" value="${view.preurl}">
			            	<br/>ex) http://iedu.nise.go.kr/sample/contents/sample.html
                          </td> 
                    </tr>

					<tr>
                          <th>소개이미지</th>
                          <td colspan="3">
                          	
                          	<input type="checkbox" id="chkYoutubeYn" name="chkYoutubeYn" <c:if test="${view.youtubeYn eq 'Y'}">checked </c:if>  onClick="selectChkYoutube(this);"/> <label for="chkYoutubeYn">유튜브 동영상 사용</label><br/>
                          	(소개 이미지로 이미지/유투브 동영상 둘 중 한가지를 올릴수 있습니다. )
                          	 유튜브 동영상 ex) https://youtu.be/TOq6naMEuhg -> TOq6naMEuhg   
                          	<c:if test="${view.youtubeYn ne 'Y'}">
                          	
                          	</c:if>
                          	<div id="fileDiv">
	                          	<input name="introducefile" type="file" size="80" maxlength="100">
								&nbsp;※ 이미지 사이즈 (245 * 173 권장)<br/>
								<c:if test="${view.introducefilenamereal != null && view.introducefilenamereal != ''}">
									<img src="/dp/subject/<c:out value="${view.introducefilenamenew}"/>" border="0" width="210"/><br/>
									
									<a href="/dp/subject/<c:out value="${view.introducefilenamenew}"/>" target="_blank">
									<c:out value="${view.introducefilenamereal}"/>
									</a>
									<input type = "checkbox" name = "introducefile0" value = "1"><font color="red">(*삭제시 체크)</font>
								</c:if>
								<input type="hidden" name = "introducefile1" value="${view.introducefilenamereal}">
								<input type="hidden" name = "introducefile2" value="${view.introducefilenamenew}">
							</div>
							<div id="youtubeDiv">
								<input type="text" id="youtubeUrl" name="youtubeUrl" value="${view.youtubeUrl}">
							</div>
							
                          </td> 
                    </tr>
                    <tr>
                          <th>교육목표</th>
                          <td colspan="3">
                          	<textarea name="intro" cols="100" rows="3"><c:out value="${view.intro}"/></textarea>
                          </td> 
                    </tr>
                    <tr>
                          <th>교육개요</th>
                          <td colspan="3">
                          	<textarea name="edumans" cols="100" rows="3"><c:out value="${view.edumans}"/></textarea>
                          </td> 
                    </tr>
                    
                    <tr>
                          <th>교육내용</th>
                          <td colspan="3">
                          	<textarea name="explain" cols="100" rows="3"><c:out value="${view.explain}"/></textarea>
                          </td> 
                    </tr>
                    <tr>
                          <th>교육내용 첨부파일</th>
                          <td colspan="3">
                          	<input name="explainfile" type="file" size="80" maxlength="100">
							&nbsp;※ 이미지 사이즈 (가로 : 600px)<br/>
							<c:if test="${view.explainfilereal != null && view.explainfilereal != ''}">
								<img src="/dp/subject/<c:out value="${view.explainfile}"/>" border="0" width="210"/><br/>
								
								<a href="/dp/subject/<c:out value="${view.explainfile}"/>" target="_blank">
								<c:out value="${view.explainfilereal}"/>
								</a>
								<input type = "checkbox" name = "explainfile0" value = "1"><font color="red">(*삭제시 체크)</font>
							</c:if>
							<input type="hidden" name = "explainfile1" value="${view.explainfilereal}">
							<input type="hidden" name = "explainfile1" value="${view.explainfile}">
                          </td> 
                    </tr>
                    <tr>
                          <th>평가기준</th>
                          <td colspan="3">
                          	<textarea name="memo" cols="100" rows="3"><c:out value="${view.memo}"/></textarea>
                          </td> 
                    </tr>

                </tbody>
            </table>				
		</div>


        
        
        
        <!-- tab -->
        <div class="conwrap2 mrt30">
            <ul class="mtab2">
                <li><a href="#" class="on">CP 컨텐츠정보</a></li>
         	</ul>
        </div>
		<!-- // tab--> 

        <!-- detail wrap -->
        
        <!-- // detail wrap -->

		<div class="tbDetail">
            <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                    <col width="15%" />
                    <col width="35%" />
                    <col width="15%" />
                    <col width="35%" />                    
                </colgroup>
                <tbody>
                	<tr>
                        <th>위탁과정여부</th>
                        <td class="borderR">
                        	<ui:code id="isoutsourcing" selectItem="${view.isoutsourcing}" gubun="defaultYN" codetype=""  upper="" title="위탁과정여부" className="" type="select" selectTitle="" event="" />
                        </td>
                        <th>CP과정코드 </th>
                        <td class="borderR">
                        	<input type="text" id="cpsubj" name="cpsubj" size="14" value="${view.cpsubj}">
                        </td>                
                    </tr>
                    <tr>
                        <th>소유회사</th>
                        <td class="borderR">
                        	<input type="text" id="ownernm" name="ownernm" size="14" maxlength="13" value="${view.ownernm}" onkeyup="clearObj(this, thisForm.owner);">
			                <input type="hidden" id="owner" name="owner" value="${view.owner}">
			                <a href="#none" onclick="searchOwner()" class="btn_search01"><span>조회</span></a>
                        </td>
                        <th>컨텐츠등급</th>
                        <td class="borderR">
                        	<ui:code id="contentgrade" selectItem="${view.contentgrade}" gubun="" levels="1" codetype="0056"  upper="" title="컨텐츠등급" className="" type="select" selectTitle="" event="" />
                        </td>                
                    </tr>
                    <tr>
                        <th>보유년도</th>
                        <td class="borderR">
                        	 최초확보: <input type="text" id="firstdate" name="firstdate" size="6" maxlength="6" value="${view.firstdate}"><br/>
							심사연월: <input type="text" id="judgedate" name="judgedate" size="6" maxlength="6" value="${view.judgedate}"> 
							/ 최종변경: <input type="text" id="getdate"   name="getdate"   size="6" maxlength="6" value="${view.getdate}">
                        </td>
                        <th>컨텐츠확보</th>
                        <td class="borderR"><p>
                        	방&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;법 : <ui:code id="getmethod" selectItem="${view.getmethod}" gubun="" levels="1" codetype="0106"  upper="" title="컨텐츠확보" className="" type="select" selectTitle="선택" event="" /><br/>
                        	
                        	CP업체&nbsp;&nbsp;: <input type="text" id="cpnm" name="cpnm" size="14" maxlength="13" value="${view.cpnm}" onkeyup="clearObj(this, thisForm.cp);">
                        	<input type="hidden" id="cp" name="cp" value="${view.cp}">
			                <a href="#none" onclick="searchCp()" class="btn_search01"><span>조회</span></a><br/>
							
							<c:if test="${ !empty view.subj }">
							정&nbsp;산&nbsp;율 : <input type="text" id="cp_accrate" name="cp_accrate" size="3" maxlength="3" value="${view.cp_accrate}" onblur="javascript:numeric_chk(this)" />&nbsp;% / 
							
							정산금액 : <input type="text" id="cp_account" name="cp_account" size="6" maxlength="7" value="${view.cp_account}" onblur="javascript:numeric_chk(this)" />&nbsp;원<br/>
							</c:if>
							
							<c:if test="${ empty view.subj }">
							정&nbsp;산&nbsp;율 : <input type="text" id="cp_accrate" name="cp_accrate" size="3" maxlength="3" value="0" onblur="javascript:numeric_chk(this)" />&nbsp;% / 
							
							정산금액: <input type="text" id="cp_account" name="cp_account" size="6" maxlength="7" value="0" onblur="javascript:numeric_chk(this)" />&nbsp;원<br/>
							</c:if>
							
							
							
							
							VAT별도여부 <input type="checkbox" id="cp_vat" name="cp_vat" value="${view.cp_vat}"><br/>
							
							제작업체: <input type="text" id="producernm" name="producernm" size="14" maxlength="13" value="${view.producernm}"  onkeyup="clearObj(this, thisForm.producer);">
			                <input type="hidden" id="producer" name="producer" value="${view.producer}">
			                <a href="#none" onclick="searchProducer()" class="btn_search01"><span>조회</span></a><br/>
			                
			               	 제작일자: <input id="crdate" name="crdate" type="text" size="10" maxlength="10" value="${view.crdate}">
			            	 <img src="/images/adm/ico/ico_calendar.gif" onclick="popUpCalendar(this, document.all.crdate, 'yyyymmdd')" align="middle" style="cursor: pointer;"/>
			            	 </p>
                        </td>
                    </tr>
                </tbody>
            </table>				
		</div>


</form>


		<!-- btn -->
        <div class="listTop">            			
                <div class="btnR">
                	<a href="#none" onclick="doFormReset()" class="btn01"><span>취 소</span></a>
                </div>
                
<c:if test="${ !empty view.subj }">

				<div class="btnR MR05">
	<c:if test="${ view.subjseqcoun == 0 }">
		<c:if test="${ view.subjobjcount == 0 }">
					<a href="#none" onclick="doSubjAction('delete')" class="btn01"><span>삭 제</span></a>
		</c:if>
		<c:if test="${ view.subjobjcount > 0 }">
                	<a href="#none" onclick="alert('과정에 컨텐츠가 매핑되어있어 삭제할 수 없습니다.')" class="btn01"><span>삭 제</span></a>
		</c:if>
	</c:if>

	<c:if test="${ view.subjseqcoun > 0 }">
                	<a href="#none" onclick="alert('과정기수 정보가 있어 삭제할 수 없습니다.')" class="btn01"><span>삭 제</span></a>
	</c:if>
				</div>
				
				
                <div class="btnR MR05">
                	<a href="#none" onclick="doSubjAction('update')" class="btn01"><span>저 장</span></a>
                </div>
</c:if>

<c:if test="${ empty view.subj }">
                <div class="btnR MR05">
                	<a href="#none" onclick="doSubjAction('insert')" class="btn01"><span>저 장</span></a>
                </div>
</c:if>

                <div class="btnR MR05">
               		<a href="#none" class="btn01" onclick="doPageList()"><span>목 록</span></a>
                </div>                        
		</div>
        <!-- //btn -->







<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


	//등록 
    function doSubjAction(process) {
        if(!chkData()) {
          return;
        }

        thisForm.specials.value = thisForm.p_new.value + thisForm.hit.value + thisForm.recom.value;
        thisForm.process.value = process;
        thisForm.action="/adm/cou/subjectAction.do";
        thisForm.target="_self";
        thisForm.submit();
    }

    
	


//form reset
function doFormReset() {
	thisForm.reset();
}


/* ********************************************************
 * 페이징처리 함수
 ******************************************************** */
function doPageList() {
	thisForm.action = "/adm/cou/subjectList.do";
	thisForm.target = "_self";
	thisForm.submit();
}


//주강사 검색
function searchTutor() {
    var f  = thisForm;
	window.open("","openWinTutor","top=0, left=0, width=580, height=550, scrollbars=yes").focus();
	f.action="/com/pop/searchTutorPopup.do";
	f.target="openWinTutor";
	f.submit();
}   

//컨텐츠담당 검색
function searchCuser() {
	var f  = thisForm;
	window.open("","openWinCuser","top=0, left=0, width=580, height=550, scrollbars=yes").focus();
	f.tmp1.value = "p_cuser";
	f.action="/com/pop/searchMemberPopup.do";
	f.target="openWinCuser";
	f.submit();
}   

//운영자담당 검색
function searchMuser() {
    var f  = thisForm;
	window.open("","openWinMuser","top=0, left=0, width=580, height=550, scrollbars=yes").focus();
	f.tmp1.value = "p_muser";
	f.action="/com/pop/searchMemberPopup.do";
	f.target="openWinMuser";
	f.submit();
}

// 멤버 검색 후 처리
function receiveMember(userid, name, email, compnm, telno, hometel, comptel, position_nm, lvl_nm, tmp1, tmp2, tmp3){
        var f  = thisForm;
		if (tmp1 == 'p_cuser') {
			f.cuseridnm.value = name;
			f.cuserid.value   = userid;  
		} else if (tmp1 == 'p_muser') {
			f.museridnm.value = name;
			f.muserid.value   = userid; 
			f.musertel.value   = telno;
		}
	}
// 강사 검색 후 처리
	function receiveTutor(userid, name){
        var f  = thisForm;
		f.tutornm.value  = name;
		f.tutor.value    = userid;  
	}	


 //제작회사 검색
    function searchProducer() {
        var f  = thisForm;
        if (f.producernm.disabled) {
	    	alert("컨텐츠확보방법이 임대가 아닌 경우에만 선택할 수 있습니다.");
        } else {
	        var p_key1  = f.producernm.value;
	        var p_key2  = 'p_producer';
	        
	        window.open("","openWinComp","top=0, left=0, width=580, height=550, scrollbars=yes").focus();
			f.action="/com/pop/searchCompPopup.do";
			f.p_gubun.value = "companynm";
			f.p_key1.value = p_key1;
			f.p_key2.value = p_key2;
			f.target="openWinComp";
	        f.submit();
	    }
    }
    
    //소유회사 검색
    function searchOwner() {
    	var f  = thisForm;
        var p_key1  = f.ownernm.value;
        var p_key2  = 'p_owner';
        
        window.open("","openWinOwnerComp","top=0, left=0, width=580, height=550, scrollbars=yes").focus();
		f.action="/com/pop/searchCompPopup.do";
		f.p_gubun.value = "companynm";
		f.p_key1.value = p_key1;
		f.p_key2.value = p_key2;
		f.target="openWinOwnerComp";
        f.submit();
    }

    // CP 검색
    function searchCp() {
        var f  = thisForm;
        
        if (f.cpnm.disabled) {
	    	alert("컨텐츠확보방법이 임대일 경우에만 선택할 수 있습니다.");
        } else {
	        var p_key1  = f.cpnm.value;
	        var p_key2  = "p_cp";
	        
	        window.open("","openWinCp","top=0, left=0, width=580, height=550, scrollbars=yes").focus();
			f.action="/com/pop/searchCompPopup.do";
			f.p_gubun.value = "companynm";
			f.p_key1.value = p_key1;
			f.p_key2.value = p_key2;
			f.target="openWinCp";
	        f.submit();
	    }
    }
      
    // 회사 검색 후 처리
    function receiveGrpComp(comp , compnm, tmp1) {
        var f  = thisForm;
        if (tmp1 == 'p_producer') {
            f.producernm.value = compnm;
            f.producer.value   = comp; 
        } else if (tmp1 == 'p_owner') {
            f.ownernm.value = compnm;
            f.owner.value   = comp;  
        } else if( tmp1 == 'p_taxcomp') {
            f.taxcompnm.value      = compnm;
            f.taxcomp.value        = comp;
        } else if( tmp1 == 'p_cp') {
            f.cpnm.value      = compnm;
            f.cp.value        = comp;
        }
    } 


// 컨텐츠타입 변경시 컨텐츠코드, 위탁여부 자동설정
	function changeContentType(obj) {
		var f  = thisForm;
		if (obj.value == "L") {
			f.isoutsourcing.value = "Y";	
		} else {
			f.isoutsourcing.value = "N";
		}
	}

//과정사용여부
	function whenIsuse() {
        var f  = thisForm;
		if (f.isuse.value=="Y") {
			f.isvisible.disabled = false;
		} else {
			f.isvisible.disabled = true;
			f.isvisible.value = "N";
		}
	}

//복습가능 여부체크
	function whenIsablereview() {
        var f  = thisForm;
	    if (f.isablereview.value=="Y") {
	        f.reviewdays.disabled = false;
	    } else {
	        f.reviewdays.disabled = true;
	        f.reviewdays.value = 0;
	    }
	}


//교재여부 체크
	function whenUsebook() {
        var f  = thisForm;
		if (f.usebook.value=="Y") {
			f.bookname.disabled = false;
			f.bookprice.disabled = false;
		} else {
			f.bookname.disabled = true;
			f.bookname.value = "";
			f.bookprice.disabled = true;
			f.bookprice.value = 0;
		}
	}


 //고용보험 사용여부체크
	function whenIsgoyong() {
        var f  = thisForm;
	    if (f.isgoyong.value=="Y") {
	        f.goyongprice_major.disabled = false;
	        f.goyongprice_minor.disabled = false;
	    } else {
	        f.goyongprice_major.disabled = true;
	        f.goyongprice_minor.disabled = true;
	        f.goyongprice_major.value = 0;
	        f.goyongprice_minor.value = 0;
	    }
	}	  

// 명칭 삭제시 코드까지 삭제
	function clearObj(obj, target) {
		if (obj.value == "") {
			target.value = "";
		}
	}

// 컨텐츠확보방법 변경시 처리
	function changeMethod(obj) {
		var f  = thisForm;
		// 임대일때만 cp선택가능
		if (obj.value == "L") {
			f.producernm.disabled = true;
			f.cpnm.disabled = false;
			f.producernm.value = "";
			f.producer.value = "";
		} else {
			f.producernm.disabled = false;
			f.cpnm.disabled = true;
			f.cpnm.value = "";
			f.cp.value = "";
		}
	}
	
	function chkData() {
        var f  = thisForm;
    
		if ( blankCheck(f.subjnm.value) ) {
            alert('과정명을 입력하십시요.');
            f.subjnm.focus();
            return false;
        }
    
    	// 대분류 선택
        if (f.upperclass.value=='') { 
            alert('대분류를 선택하세요.');
            return false;
        }
        
        // 중분류 선택
        if (f.middleclass.value=='') { 
            f.middleclass.value='000';
            //alert('중분류를 선택하세요.');
            //return false;
        }
        
        // 소분류 선택
        if (f.lowerclass.value=='') { 
            f.lowerclass.value='000';
        }
        
        // 수강료를 숫자만 입력했는지 체크
        if (!number_chk_noalert(f.biyong.value)) {
            alert('수강료가 잘못입력되었습니다.');
            f.biyong.focus();
            return false;
        }
        
        //코드
        if( f.conyear.value == "" ){
			alert("제작(리뉴얼)년도를 입력하세요.");
			f.conyear.focus();
			return;
		}
		if (!number_chk_noalert(f.conyear.value)) {
            alert('제작(리뉴얼)년도가 잘못입력되었습니다.');
            f.conyear.focus();
            return false;
        }
		if(f.conyear.value.length !=2 ){
			alert('제작(리뉴얼)년도는 두자리로 입력하세요');
			f.conyear.focus();
			return false;
		}
		
		if( f.confr.value == "" ){
			alert("콘텐츠 버전을 선택하세요.");
			f.confr.focus();
			return;
		}
		if( f.conrenum.value == "" ){
			alert("리뉴얼 차순을 선택하세요.");
			f.conrenum.focus();
			return;
		}
		if(f.confr.value == "F"){        	
			if(f.conrenum.value !=0 || f.conrenum.value ==''){
				alert("콘텐츠 버전을 확인하세요");
				return;
			}
		}
		if(f.confr.value == "R"){
			if(f.conrenum.value == 0 || f.conrenum.value ==''){
				alert("콘텐츠 버전을 확인하세요");
				return;
			}
		}
		
		if(f.upperclass.value == "PRF"){
			f.upperclassnum.value = "1";	
		}else if(f.upperclass.value == "EXT"){
			f.upperclassnum.value = "2";	
		}else if(f.upperclass.value == "PAR"){
			f.upperclassnum.value = "3";	
		}else if(f.upperclass.value == "COUR"){
			f.upperclassnum.value = "4";	
		}else if(f.upperclass.value == "OTH"){
			f.upperclassnum.value = "5";	
		}
		
		//코드
		
        
        // 학습일차를 숫자만 입력했는지 체크
        if (!number_chk_noalert(f.edudays.value)) {
            alert('학습일차가 잘못입력되었습니다.');
            f.edudays.focus();
            return false;
        }
        
        // 학습시간을 숫자만 입력했는지 체크
        if (!number_chk_noalert(f.edutimes.value)) {
            alert('학습시간이 잘못입력되었습니다.');
            f.edutimes.focus();
            return false;
        }

		// 정원을 숫자만 입력했는지 체크
        if (!number_chk_noalert(f.studentlimit.value)) {
            alert('정원이 잘못입력되었습니다.');
            f.studentlimit.focus();
            return false;
        }

		if (!number_chk_noalert(f.bookprice.value)) {
            alert('교재비가 잘못입력되었습니다.');
            f.bookprice.focus();
            return false;
        }

        if (!number_chk_noalert(f.cp_accrate.value)) {
            alert('CP정산율이 잘못입력되었습니다.');
            f.cp_accrate.focus();
            return false;
        }

        if (!number_chk_noalert(f.cp_account.value)) {
            alert('CP정산금액이 잘못입력되었습니다.');
            f.cp_account.focus();
            return false;
        }

        if (f.cp_accrate.value > 0 && f.cp_account.value > 0) {
            alert('CP정산율과 CP정산금액중 하나만 입력하세요.');
            f.cp_accrate.focus();
            return false;
        }
                
       if (f.cp_vat.checked == true){
    	   f.cp_vat.value = "10";
       }
        
		// 소유회사 체크
        //if (f.owner.value=="") {
        //    alert("컨텐츠 소유회사를 선택하세요.");
        //    return;
        //}
        
        // 제작일자
		//if( calendarCheck(f.crdate_view) ) {
		//	return;
		//}
		
		//위탁과정일경우
		if(f.isoutsourcing[f.isoutsourcing.selectedIndex].value == 'Y'){
			if ( blankCheck(f.cpsubj.value) ) {
	            alert('CP과정코드를 입력하십시요.');
	            f.cpsubj.focus();
	            return false;
	        }
			if ( blankCheck(f.eduurl.value) ) {
	            alert('학습 URL을 입력하십시요.');
	            f.eduurl.focus();
	            return false;
	        }
		}

        // 학습방법(WBT%)를 숫자만 입력했는지 체크
        if (!number_chk_noalert(f.ratewbt.value)) {
            alert('학습방법(WBT%)이 잘못입력되었습니다.');
            f.ratewbt.focus();
            return false;
        }
        
        // 학습방법(VOD%)를 숫자만 입력했는지 체크
        if (!number_chk_noalert(f.ratevod.value)) {
            alert('학습방법(VOD%)가 잘못입력되었습니다.');
            f.ratevod.focus();
            return false;
        }
        
        if (blankCheck(f.edulimit.value) || f.edulimit.value == "0") {
            alert('1일최대학습량을 입력해 주세요.');
            f.edulimit.focus();
            return false;
        }
                
		if (!number_chk_noalert(f.edulimit.value)) {
            alert('1일최대학습량이 잘못입력되었습니다.');
            f.edulimit.focus();            
            return false;
        }

        // 수료점수를 숫자만 입력했는지 체크
        if (!number_chk_noalert(f.point.value) || f.point.value == "") {
            alert('수료점수가 잘못입력되었습니다.');
            f.point.focus();
            return false;
        }
        
        if (!number_chk_noalert(f.gradscore.value)) {
            alert('수료기준-점수가 잘못입력되었습니다.');
            f.gradscore.focus();
            return false;
        }
        
        if (!number_chk_noalert(f.wetc1.value)) {
            alert('수료기준-참여도(학습진도율)가 잘못입력되었습니다.');
            f.gradstep.focus();
            return false;
        }
        
        if (f.gradscore.value > 100) {
            alert('수료기준-점수가 100 이상입니다.');
            f.gradscore.focus();
            return false;
        }
        if (f.wetc1.value > 100) {
            alert('수료기준-진도율이 100 이상입니다.');
            f.wetc1.focus();
            return false;
        }
		/*
        if (!number_chk_noalert(f.study_count.value)) {
            alert('수료기준-접속횟수가 잘못입력되었습니다.');
            f.study_count.focus();
            return false;
        }

        if (!number_chk_noalert(f.study_time.value)) {
            alert('수료기준-학습시간이 잘못입력되었습니다.');
            f.study_time.focus();
            return false;
        }
        */      
        if (!number_chk_noalert(f.wetc2.value)) {
            alert('가중치-참여도(%)이 잘못입력되었습니다.');
            f.wetc2.focus();
            return false;
        }
        
        if (!number_chk_noalert(f.wmtest.value)) {
            alert('가중치-출석시험(%)가 잘못입력되었습니다.');
            f.wmtest.focus();
            return false;
        }
        
        if (!number_chk_noalert(f.wftest.value)) {
            alert('가중치-온라인시험(%)가 잘못입력되었습니다.');
            f.wftest.focus();
            return false;
        }
        
        if (!number_chk_noalert(f.wreport.value)) {
            alert('가중치-온라인과제(%)가 잘못입력되었습니다.');
            f.wreport.focus();
            return false;
        }
      
      
        var weight =  parseFloat(f.wmtest.value  ,10)
                   +  parseFloat(f.wftest.value  ,10)
                   +  parseFloat(f.wreport.value ,10)
                   +  parseFloat(f.wetc2.value   ,10)
                   //+  parseFloat(f.wetc2.value   ,10);
                   
        if ( weight < 100 ) {
            alert('가중치가 100% 보다 적습니다');
            f.wmtest.focus();
            return false;
        } else if ( weight > 100 ) {
            alert('가중치가 100% 보다 많습니다.');
            f.wmtest.focus();
            return false;
        }
        
        changeContentType(f.contenttype);
        
        return true;
    }



//init
	window:onload = function () {
		changeContentType(thisForm.contenttype);
		//changeMethod(thisForm.getmethod);
	}

//-->
</script>






<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
</body>
</html>