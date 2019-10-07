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
                    <th>제목</th>
                    <td>
                    	<input type="text" name="p_title" id="p_title" value="" size="80">
                    	중요글<input type="checkbox" name="p_isimport" value="Y" <c:if test="${data.isimport eq 'Y'}">checked</c:if>>
                    </td>
                </tr>
                <tr class="title">
                    <th>공지구분</th>
                    <td>
                    	<ui:code id="p_types" selectItem="${p_types}" gubun="" codetype="0008"  upper="" title="" className="" type="select" selectTitle="" event="" />
                    </td>
                </tr>
                <tr class="title">
                    <th>내용</th>
                    <td style="padding-left:0px">
                    	<c:set var="content" />
                    	<c:set var="width"  value="600"/>
                    	<c:set var="height" value="400" />
                    	<%@ include file="/WEB-INF/jsp/egovframework/com/lib/DhtmlEditor.jsp" %>
						<textarea rows="10" cols="10" name="p_adcontent" id="p_adcontent" style="display:none;"></textarea>
                    </td>
                </tr>
                <tr class="title">
                    <th>첨부파일</th>
                    <td>
                    	<input type="file" 	 name="p_file" 	 size="80" class="input">
                    	<input type="hidden" name="p_upload_dir" value="bulletin">
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
		frm.action = "/adm/cmg/not/courseNoticeSubList.do"
		frm.submit();
	}
	
	function insertData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if (frm.p_title.value == "") {
            alert("제목을 입력하세요");
            frm.p_title.focus();
            return;
        }
        if (realsize(frm.p_title.value) > 200) {
            alert("제목은 한글기준 100자를 초과하지 못합니다.");
            frm.p_title.focus();
            return;
        }
        frm.p_adcontent.value = ObjEditor.document.all.tags("html")[0].outerHTML;
		frm.action = "/adm/cmg/not/courseNoticeInsert.do"
		frm.submit();
	}
	
</script>