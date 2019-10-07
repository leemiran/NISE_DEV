<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_subjnm" 	id="p_subjnm"	value="${p_subjnm}">
	<input type="hidden" name="p_subjseq2" 	id="p_subjseq2"	value="${p_subjseq2}">
	<input type="hidden" name="pageIndex" 	id="p_subjseq2"	value="${pageIndex}">
	<input type = "hidden" name="p_type"     value = "${p_type}" />
	
    <input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
    <input type = "hidden" name="p_seq"     value = "${p_seq}" />
    <input type = "hidden" name="p_process"  value = "" />
    
    <input type = "hidden" name="p_startdate"  value = "" />
    <input type = "hidden" name="p_enddate"    value = "" />
    <input type = "hidden" name="p_compcd"     value = "" />
    <input type = "hidden" name="p_uncompcd"   value = "" />
    <input type = "hidden" name="p_grcodecd"   value = "N000001" />
    <input type = "hidden" name="p_selcomp"    value = "" />
    <input type = "hidden" name="p_content"    value = "" />
    <input type = "hidden" name="p_isAllvalue" value = "" />
    <input type = "hidden" name="p_loginynPre" value = "N" />
    <input type = "hidden" name="p_gubun" value = "N" />
    
    	
	
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="85%" />
            </colgroup>
            <tbody>
                <tr>
                    <th scope="row">작성자</th>
                    <td scope="row">${view.adname}</td>
                </tr>
                <tr>
                    <th scope="row">등록일</th>
                    <td scope="row">${fn2:getFormatDate(view.addate, 'yyyy.MM.dd')}</td>
                </tr>
                <tr>
                    <th scope="row">전체공지여부</th>
                    <td scope="row">${view.isall}</td>
                </tr>
                <tr>
                    <th scope="row">로그인 유무</th>
                    <td scope="row">
                    	<c:choose>
                    		<c:when test="${view.loginyn eq 'Y'}">로그인후</c:when>
                    		<c:when test="${view.loginyn eq 'N'}">로그인전</c:when>
                    		<c:otherwise>전체</c:otherwise>
                    	</c:choose>
                    </td>
                </tr>
                <tr> 
		            <th scope="row">대상교육그룹<br/>유무</th>
		            <td scope="row">
		              ${(view.grcodegubun eq 'Y') ? '전체' : '선택'}
		            </td>
		          </tr>  
                <tr>
                    <th scope="row">팝업여부</th>
                    <td scope="row">
                    	${view.popup}
                    </td>
                </tr>
                <!--<tr>
                    <th scope="row">팝업설정</th>
                    <td scope="row">
                    	${fn2:getFormatDate(view.startdate, 'yyyy.MM.dd')}  ~ ${fn2:getFormatDate(view.enddate, 'yyyy.MM.dd')}
						<br/>사이즈 : 가로&nbsp; ${view.popwidth} / 세로&nbsp; ${view.popheight}
						<br/>위치   : x&nbsp;=  ${view.popxpos} /  y&nbsp;= ${view.popypos}
						<br/>작성내용만보이기 : ${not empty view.useframe ? view.useframe : 'N'} 홈페이지리스트사용 : ${not empty view.uselist ? view.uselist : 'N'}
                    </td>
                </tr>
                <tr>
                    <th scope="row">팝업템플릿타입</th>
                    <td scope="row">
                    	  ${view.temType}
                    </td>
                </tr>
                --><tr>
                    <th scope="row">사용유무</th>
                    <td scope="row">
                    	${view.useyn eq 'Y' ? '사용' : '미사용'}
                    </td>
                </tr>
                <tr>
                    <th scope="row">공지구분</th>
                    <td scope="row">
                    	<c:if test="${view.noticeGubun eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
						<c:if test="${view.noticeGubun eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
						<c:if test="${view.noticeGubun eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
						<c:if test="${view.noticeGubun eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
						<c:if test="${view.noticeGubun eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
						<c:if test="${view.noticeGubun eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
						<c:if test="${view.noticeGubun eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
                    </td>
                </tr>
                <tr>
                    <th scope="row">제목</th>
                    <td scope="row">
                    	${view.adtitle}
                    </td>
                </tr>
                
                <tr>
                    <th scope="row">내용</th>
                    <td style="padding-left:5px" scope="row">
                    	<c:out value="${view.adcontent}" escapeXml="false"/>
                    </td>
                </tr>
                <tr>
                    <th scope="row">첨부파일</th>
                    <td scope="row">
                    	<c:forEach items="${fileList}" var="result" varStatus="i">
                    		* <a href="#none" onclick="fn_download('${result.realfile}', '${result.savefile}', 'bulletin')">
                    		<c:out value="${empty result.realfile ? result.savefile : result.realfile}"/>
                    		</a> <br/>        	
                    	</c:forEach>
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    
    
    
    <!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>목 록</span></a></div>
		
		
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('delete')"><span>삭 제</span></a></div>
		
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeEdit()"><span>수 정</span></a></div>
		
	</div>
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		thisForm.action = "/adm/hom/not/selectNoticeList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}


	//수정하기
	function whenNoticeEdit() {
        thisForm.action = "/adm/hom/not/noticeInsertPage.do";
        thisForm.target = "_self";
        thisForm.submit();
    }
    
	//삭제하기
	function whenNoticeSave(mode) {

		if(confirm("현재글을 삭제하시겠습니까?"))
		{
	        thisForm.pageIndex.value = 1;
	        thisForm.action = "/adm/hom/not/noticeActionPage.do";
	        thisForm.p_process.value = mode;
	        thisForm.target = "_self";
	        thisForm.submit();
		}
    }
</script>