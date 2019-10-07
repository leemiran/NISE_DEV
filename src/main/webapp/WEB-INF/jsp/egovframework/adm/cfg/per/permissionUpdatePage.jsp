<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_gadmin" 		name="p_gadmin" 		value="<c:out value="${p_gadmin}"/>"/>
	<input type="hidden" id="p_padmin" 		name="p_padmin" 		value="<c:out value="${p_padmin}"/>"/>
	<input type="hidden" id="p_isneedgrcode" 	name="p_isneedgrcode"/>
	<input type="hidden" id="p_isneedsubj" 		name="p_isneedsubj"/>
	<input type="hidden" id="p_isneedcomp" 		name="p_isneedcomp"/>
	<input type="hidden" id="p_isneeddept" 		name="p_isneeddept"/>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="deleteData()"><span>삭제</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="updateData()"><span>수정</span></a></div>
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
                    <th>권한(기본)</th>
                    <td>
                    	<select id="p_gadminsel" name="p_gadminsel">
                    	<c:forEach items="${gadminList}" var="result">
                    		<option value="<c:out value="${result.gadmin}"/>" <c:if test="${result.padmin == p_padmin}">selected</c:if>
                    			    grcode="<c:out value="${result.isneedgrcode}"/>"
                    			    subj="<c:out value="${result.isneedsubj}"/>"
                    			    comp="<c:out value="${result.isneedcomp}"/>"
                    			    dept="<c:out value="${result.isneeddept}"/>"
                    		>
                    			<c:out value="${result.gadminnm}"/>
                    		</option>
                    	</c:forEach>
                    	</select>
                    </td>
                </tr>
				<c:choose>
				<c:when test="${view.gadmin == 'A1' or view.gadmin == 'A2' or view.gadmin == 'C1' or view.gadmin == 'C2' or view.gadmin == 'F1' or view.gadmin == 'H1'
                             or view.gadmin == 'K2' or view.gadmin == 'P1' or view.gadmin == 'R1' or view.gadmin == 'S1' or view.gadmin == 'T1' or view.gadmin == 'U1'}">
                <tr class="title">
                    <th>추가 권한명</th>
                    <td>
                    <c:out value="${view.gadminnm}"/>
                    <input type="hidden" id="p_gadminnm" name="p_gadminnm" value="<c:out value="${view.gadminnm}"/>" />
                    </td>
                </tr>
                <tr class="title">
                    <th>권한 사용 용도</th>
                    <td>
                    <c:out value="${view.comments}"/>
                    <input type="hidden" id="p_comments" name="p_comments" value="<c:out value="${view.comments}"/>" />
                    </td>
                </tr>
                </c:when>
                <c:otherwise>
                <tr class="title">
                    <th>추가 권한명</th>
                    <td><input type="text" id="p_gadminnm" name="p_gadminnm" value="<c:out value="${view.gadminnm}"/>" style="width:80%"/></td>
                </tr>
                <tr class="title">
                    <th>권한 사용 용도</th>
                    <td><input type="text" id="p_comments" name="p_comments" value="<c:out value="${view.comments}"/>" style="width:80%"/></td>
                </tr>
                </c:otherwise>
				</c:choose>
                <tr class="title">
                    <th>권한 순서</th>
                    <td><input type="text" id="p_seq" name="p_seq" value="<c:out value="${view.seq}"/>" style="width:2%"/></td>
                </tr>
            </tbody>
        </table>	
		<!-- list table-->			
    </div>
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/per/permissionList.do";
		frm.submit();
	}

	function updateData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		numeric_chk(frm.p_seq);

		frm.p_isneedgrcode.value = frm.p_gadminsel.options[frm.p_gadminsel.selectedIndex].getAttribute("grcode");
		frm.p_isneedsubj.value = frm.p_gadminsel.options[frm.p_gadminsel.selectedIndex].getAttribute("subj");
		frm.p_isneedcomp.value = frm.p_gadminsel.options[frm.p_gadminsel.selectedIndex].getAttribute("comp");
		frm.p_isneeddept.value = frm.p_gadminsel.options[frm.p_gadminsel.selectedIndex].getAttribute("dept");
		
        frm.action = "/adm/cfg/per/permissionUpdate.do";
        frm.submit();
	}

	function deleteData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( !confirm("정말 삭제하시겠습니까?") ){
			return;
		}
		frm.action = "/adm/cfg/per/permissionDelete.do";
		frm.submit();
	}
</script>