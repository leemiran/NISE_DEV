<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_userid" 		name="p_userid" 		value="<c:out value="${p_userid}"/>"/>
	<input type="hidden" id="p_gadmin" 		name="p_gadmin" 		value="<c:out value="${p_gadmin}"/>"/>
	<input type="hidden" id="p_comp" 		name="p_comp" 			value="<c:out value="${view.comp}"/>"/>
	
	<input type="hidden" id="p_isneedgrcode" 	name="p_isneedgrcode"	value="<c:out value="${view.isneedgrcode}"/>">
	<input type="hidden" id="p_isneedsubj" 		name="p_isneedsubj"		value="<c:out value="${view.isneedsubj}"/>">
	<input type="hidden" id="p_isneedcomp" 		name="p_isneedcomp"		value="<c:out value="${view.isneedcomp}"/>">
	<input type="hidden" id="p_isneeddept" 		name="p_isneeddept"		value="<c:out value="${view.isneeddept}"/>">
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>취소</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="updateData()"><span>저장</span></a></div>
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
                    <th>성명</th>
                    <td><c:out value="${view.name}"/></td>
                </tr>
                <tr class="title">
                    <th>권한</th>
                    <td><c:out value="${view.gadminnm}"/></td>
                </tr>
                <tr class="title">
                    <th>교육그룹</th>
                    <td>
                    	<div id="grcode" style="display:<c:if test="${view.isneedgrcode != 'Y'}">none</c:if>">
	                    <select name="p_grcode" size="4"  style="width:80%" multiple>
	                    <c:forEach items="${grcodeList}" var="result">
	                    	<option value="<c:out value="${result.grcode}"/>" ><c:out value="${result.grcodenm}"/></option>
	                    </c:forEach>
	                    </select>
	                    <a href="javascript:searchGrcode()"><img src="/images/adm/button/btn_groupsearch.gif" border="0"></a>
                    	<a href="javascript:delGrcode()"><img src="/images/adm/button/btn_groupdelete.gif" border="0"></a>
                    	</div>
                    </td>
                </tr>
                <tr class="title">
                    <th>과정</th>
                    <td>
                    	<div id="subj" style="display:<c:if test="${view.isneedsubj != 'Y'}">none</c:if>">
	                    <select name="p_subj" size="10"  style="width:80%" multiple>
	                    <c:forEach items="${subjList}" var="result">
	                    	<option value="<c:out value="${result.subj}"/>" ><c:out value="${result.subjnm}"/></option>
	                    </c:forEach>
	                    </select>
	                    <a href="javascript:searchSubj()"><img src="/images/adm/button/btn_csearch.gif" border="0"></a>
                    	<a href="javascript:delSubj()"><img src="/images/adm/button/btn_cdelete.gif" border="0"></a>
                    	</div>
                    </td>
                </tr>
                <tr class="title">
                    <th>회사</th>
                    <td>
                    	<div id="company" style="display:<c:if test="${view.isneedcomp != 'Y'}">none</c:if>">
	                    <select name="p_company" size="4"  style="width:80%" multiple>
	                    <c:forEach items="${compList}" var="result">
	                    	<option value="<c:out value="${result.comp}"/>" ><c:out value="${result.companynm}"/></option>
	                    </c:forEach>
	                    </select>
	                    <a href="javascript:searchCompany()"><img src="/images/adm/button/btn_comsearch.gif" border="0"></a>
                    	<a href="javascript:delCompany()"><img src="/images/adm/button/btn_comdelete.gif" border="0"></a>
                    	</div>
                    </td>
                </tr>
                <tr class="title">
                    <th>권한사용기간</th>
                    <td>
                    	<input type="text" id="p_fmon" name="p_fmon" value="<c:out value="${view.fmon}"/>" size="8" readonly/>
                    	<img src="/images/adm/ico/ico_calendar.gif" onclick="popUpCalendar(this, document.all.p_fmon, 'yyyy-mm-dd')" align="middle" style="cursor: pointer;"/>
                    	~
                    	<input type="text" id="p_tmon" name="p_tmon" value="<c:out value="${view.tmon}"/>" size="8" readonly/>
                    	<img src="/images/adm/ico/ico_calendar.gif" onclick="popUpCalendar(this, document.all.p_tmon, 'yyyy-mm-dd')" align="middle" style="cursor: pointer;"/>
                    	
                    </td>
                </tr>
                <tr class="title">
                    <th>권한사용용도</th>
                    <td><input type="text" id="p_commented" name="p_commented" value="<c:out value="${view.commented}"/>"/></td>
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
		frm.action = "/adm/cfg/mng/managerList.do";
		frm.submit();
	}

	function updateData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

	    if( frm.p_fmon.value == "" ){
			alert("시작일을 입력하세요.");
			return;
	    }
	    if( frm.p_tmon.value == "" ){
			alert("마감일을 입력하세요.");
			return;
	    }
	    if( frm.p_fmon.value > frm.p_tmon.value ){
		    alert("시작일이 마감일보다 큽니다.");
		    return;
	    }
	    if( document.getElementById("grcode").style.display == "" ){
		    for(var i=0; i<frm.p_grcode.length; i++){
		    	frm.p_grcode.options[i].selected = true;
	        }
	    }
	    if( document.getElementById("subj").style.display == "" ){
	        for(var i=0; i<frm.p_subj.length; i++){
	        	frm.p_subj.options[i].selected = true;
	        }
        }
	    if( document.getElementById("company").style.display == "" ){
	        for(var i=0; i<frm.p_company.length; i++){
	        	frm.p_company.options[i].selected = true;
	        }
        }

        frm.action = "/adm/cfg/mng/managerUpdate.do";
        frm.submit();
	}

	//교육그룹선택
    function searchGrcode() {
    	var url = "/com/pop/searchGrcodePopup.do";
        window.open( url, "", "width=580,height=500");
    }
    //교육그룹선택 후 처리
    function receiveGrcode(grcode , grcodenm, tmp1, tmp2, tmp3){
    	var frm = eval('document.<c:out value="${gsMainForm}"/>');
    	var j = frm.p_grcode.length;
        var f_exist = "";
        for(var i=0;i<j;i++){
            if(frm.p_grcode.options[i].value==grcode){
                f_exist = "Y";
                alert(grcodenm+"는(은) 이미 선정되어 있습니다");
            }
        }
        if (f_exist != "Y"){
        	frm.p_grcode.options[j] = new Option(grcodenm,grcode);
        	frm.p_grcode.options[j].selected = true;
        }
    }
    //교육그룹삭제
    function delGrcode(){
    	var frm = eval('document.<c:out value="${gsMainForm}"/>');
    	for(var i = 0 ;i<frm.p_grcode.length;i++){
            if(frm.p_grcode.options[i].selected==true)
            	frm.p_grcode.options[i] = null;
		}
    }

    //과정선택
    function searchSubj() {
        var url = "/com/pop/searchManagerSubjPopup.do";
        window.open( url, "", "width=580,height=520");
    }
	//과정선택 후 처리
    function receiveSubj(subj , subjnm, tmp1, tmp2, tmp3){
    	var frm = eval('document.<c:out value="${gsMainForm}"/>');
    	var j = frm.p_subj.length;
        var f_exist = "";
        for(var i=0;i<j;i++){
            if(frm.p_subj.options[i].value==subj){
                f_exist = "Y";
                alert(subjnm+"는(은) 이미 선정되어 있습니다");
            }
        }
        if (f_exist != "Y"){
        	frm.p_subj.options[j] = new Option(subjnm,subj);
        	frm.p_subj.options[j].selected = true;
        }
    }
    //과정삭제
    function delSubj(){
    	var frm = eval('document.<c:out value="${gsMainForm}"/>');
    	
    	for(var i = 0 ;i<frm.p_subj.length;i++){
    		
    		
            if(frm.p_subj.options[i].selected==true)
            	frm.p_subj.options[i] = null;
		}
    }

    //회사선택
    function searchCompany() {
        var url = "/com/pop/searchCompPopup.do";
        window.open( url, "", "width=580,height=500");
    }
	//회사선택 후 처리
    function receiveCompany(comp , compnm, tmp1, tmp2, tmp3){
    	var frm = eval('document.<c:out value="${gsMainForm}"/>');
    	var j = frm.p_company.length;
        var f_exist = "";
        for(var i=0;i<j;i++){
            if(frm.p_company.options[i].value==comp){
                f_exist = "Y";
                alert(compnm+"는(은) 이미 선정되어 있습니다");
            }
        }
        if (f_exist != "Y"){
        	frm.p_company.options[j] = new Option(compnm,comp);
        	frm.p_company.options[j].selected = true;
        }
    }
    //회사삭제
    function delCompany(){
    	var frm = eval('document.<c:out value="${gsMainForm}"/>');
    	for(var i = 0 ;i<frm.p_company.length;i++){
            if(frm.p_company.options[i].selected==true)
            	frm.p_company.options[i] = null;
		}
    }

    function receiveGrpComp(objCode, objName){
    	var frm = eval('document.<c:out value="${gsMainForm}"/>');
    	var Length = frm.p_company.length;
        var sComp = "";

        for(var i = 0 ; i < Length ; i++) {
            sComp += frm.p_company.options[i].value + "^";
        }

		if (objCode.length > 0) {
		    for(var i = 0 ; i < objCode.length ; i++) {
		        if(objCode[i].checked) {
		            if(sComp.indexOf(objCode[i].value) == -1) {
		                var newoption = new Option(objName[i].value, objCode[i].value, false, false);
		                v.p_company.options[Length++] = newoption;
		            }
		        }
		        isExist = false;
		    }
		} else {
			// 팝업창에서 조건검색으로 한건만 검색이 되서 선택했을 경우
	        if(sComp.indexOf(objCode.value) == -1) {
	            var newoption = new Option(objName.value, objCode.value, false, false);
	            frm.p_company.options[Length++] = newoption;
	        }
		}
    }
</script>