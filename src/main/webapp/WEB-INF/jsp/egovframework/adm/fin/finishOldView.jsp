<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="pageIndex" 	id="p_subjseq2"	value="${pageIndex}">
    <input type = "hidden" name="p_seq"     value = "${p_seq}" />
    <input type = "hidden" name="p_process"  value = "" />
    
    <input type = "hidden" name="p_educompname"  value = "국립특수교육원" />
    <input type = "hidden" name="p_jickyn"  value = "Y" />
    
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="30%" />
                <col width="70%" />
            </colgroup>
            <tbody>
            
            	<tr>
                    <th scope="row">수험번호</th>
                    <td scope="row">
                    	<input type="text" name="p_serno" value="${view.serno}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">지역</th>
                    <td scope="row">
                    	<input type="text" name="p_area" value="${view.area}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">소속</th>
                    <td scope="row">
                    	<input type="text" name="p_sosock" size="80" value="${view.sosock}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">직명</th>
                    <td scope="row">
                    	<input type="text" name="p_jickname" value="${view.jickname}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">이름</th>
                    <td scope="row">
                    	<input type="text" name="p_name" value="${view.name}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">생년월일</th>
                    <td scope="row">
                    	<input type="text" name="p_juminnum" value="${view.juminnum}">
                    </td>
                </tr>
                
                <tr>
                    <th scope="row">과정명</th>
                    <td scope="row">
                    	<input type="text" name="p_couname" size="100" maxlength="80" value="${view.couname}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">교육기간</th>
                    <td scope="row">
                    	<input name="p_st_date" type="text" size="12" maxlength="10" readonly 
                    	value="${view.stDate}"  OnClick="popUpCalendar(this, this, 'yyyy.mm.dd')">
			             ~
			              <input name="p_ed_date" type="text" size="12" maxlength="10" readonly 
			              value="${view.edDate}"  OnClick="popUpCalendar(this, this, 'yyyy.mm.dd')">
						 
                    </td>
                </tr>
                <tr>
                    <th scope="row">구분</th>
                    <td scope="row">
                    	<input type="text" name="p_gubun" value="${view.gubun}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">교육시간</th>
                    <td scope="row">
                    	<input type="text" name="p_edutime" value="${view.edutime}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">이수번호</th>
                    <td scope="row">
                    	<input type="text" name="p_compnumber" size="80" value="${view.compnumber}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">점수</th>
                    <td scope="row">
                    	<input type="text" name="p_score" value="${view.score}">점
                    </td>
                </tr>
                
            </tbody>
        </table>				
    </div>
    
    
    
    <!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>목 록</span></a></div>
	    
<c:if test="${empty p_seq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenSave('insert')"><span>등 록</span></a></div>
</c:if>

<c:if test="${not empty p_seq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenSave('delete')"><span>삭 제</span></a></div>
		<div class="btnR"><a href="#" class="btn02" onclick="whenSave('update')"><span>수 정</span></a></div>
		<div class="btnR"><a href="#" class="btn02" onclick="suRoyJeung()"><span>수료증출력</span></a></div>
</c:if>	    
		
		
	</div>
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/fin/finishOldList.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function whenSave(mode) {
		
		if (blankCheck(thisForm.p_serno.value)) {
            alert("수험번호를 입력하세요");
            thisForm.p_serno.focus();
            return;
        }
		if (blankCheck(thisForm.p_name.value)) {
            alert("이름을 입력하세요");
            thisForm.p_name.focus();
            return;
        }
	
		 if (blankCheck(thisForm.p_couname.value)) {
	            alert("과정명을 입력하세요");
	            thisForm.p_couname.focus();
	            return;
	        }

		 if (blankCheck(thisForm.p_compnumber.value)) {
	            alert("이수번호를 입력하세요");
	            thisForm.p_compnumber.focus();
	            return;
	        }
        thisForm.action = "/adm/fin/finishOldAction.do";
        thisForm.p_process.value = mode;
        thisForm.target = "_self";
        thisForm.submit();
    }


	//수료증출력
	function suRoyJeung(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'oldSuRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
		frm.action = "/adm/fin/oldSuRoyJeungPrint.do";
		frm.target = "oldSuRoyJeungPop";
		frm.submit();
	}
</script>