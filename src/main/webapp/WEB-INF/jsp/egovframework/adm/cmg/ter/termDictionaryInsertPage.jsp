<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${search_subj}">
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>취소</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertData()"><span>저장</span></a></div>
	</div>
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="150px" />
                <col width="" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th>분류</th>
                    <td>
                    	<select id="p_groups" name="p_groups">
                    	<c:forEach items="${dicgroup}" var="result">
                    		<option value="${result.groups}">${result.groups}</option>
                    	</c:forEach>
                    	</select>
                    </td>
                </tr>
                <tr class="title">
                    <th>용어</th>
                    <td>
                    	<input type="text" id="p_words" name="p_words" style="width:95%">
                    </td>
                </tr>
                <tr class="title">
                    <th>설명</th>
                    <td style="padding-left:0px">
                    	<c:set var="content" />
                    	<c:set var="width"  value="600"/>
                    	<c:set var="height" value="400" />
                    	<%@ include file="/WEB-INF/jsp/egovframework/com/lib/DhtmlEditor.jsp" %>
						<input type="hidden" name="p_descs" id="p_descs">
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/ter/termDictionaryList.do"
		frm.submit();
	}
	
	function insertData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if (frm.p_words.value == "") {
            alert("단어를 입력하세요");
            frm.p_words.focus();
            return;
        }
        if (realsize(frm.p_words.value) > 200) {
            alert("단어는 한글기준 100자를 초과하지 못합니다.");
            frm.p_words.focus();
            return;
        }
        frm.p_descs.value = ObjEditor.document.all.tags("html")[0].outerHTML;
		frm.action = "/adm/cmg/ter/termDictionaryInsert.do"
		frm.submit();
	}
	
</script>