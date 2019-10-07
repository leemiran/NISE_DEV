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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
    <input type = "hidden" name="p_seq"     value = "${p_seq}" />
    <input type = "hidden" name="p_process"  value = "" />
    
    	
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
                    <td scope="row">${view.name}</td>
                </tr>
                <tr>
                    <th scope="row">등록일</th>
                    <td scope="row">${view.ldate}</td>
                </tr>
                <tr>
                    <th scope="row">사용유무</th>
                    <td scope="row">
                    	${view.useYn eq 'Y' ? '사용' : '미사용'}
                    </td>
                </tr>                
                <tr>
                    <th scope="row">년도</th>
                    <td scope="row">${view.year}</td>
                </tr>
                <tr>
                    <th scope="row">제목</th>
                    <td scope="row">${view.subject}</td>
                </tr>
                <tr>
                    <th scope="row">내용</th>
                    <td scope="row"><c:out value="${fn:replace(view.contents, lf, '<br/>')}" escapeXml="false"/></td>
                </tr>
                <tr>
                    <th scope="row">첨부파일</th>
                    <td scope="row">
                    	<c:forEach items="${fileList}" var="result" varStatus="i">
                    		* <a href="#none" onclick="fn_download('${result.realfile}', '${result.savefile}', 'training')">
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
		
		
		<div class="btnR"><a href="#" class="btn02" onclick="whenTrainingSave('delete')"><span>삭 제</span></a></div>
		
		<div class="btnR"><a href="#" class="btn02" onclick="whenTrainingEdit()"><span>수 정</span></a></div>
		
	</div>
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button --> 
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		thisForm.action = "/adm/hom/trs/selectTrainingList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}


	//수정하기
	function whenTrainingEdit() {
        thisForm.action = "/adm/hom/trs/trainingInsertPage.do";
        thisForm.target = "_self";
        thisForm.submit();
    }
    
	//삭제하기
	function whenTrainingSave(mode) {

		if(confirm("세부일정까지 삭제됩니다.현재글을 삭제하시겠습니까?"))
		{   
	        thisForm.action = "/adm/hom/trs/trainingActionPage.do";
	        thisForm.p_process.value = mode;
	        thisForm.target = "_self";
	        thisForm.submit();
		}
    }
</script>