<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type="hidden" id="p_process" name="p_process">
	<input type="hidden" id="p_seq" name="p_seq" value="${p_seq}">
    	
	
	
	<!-- list table-->
		<div class="tbDetail">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="10%" />
					<col width="90%" />					
				</colgroup>
				<thead>
                        <tr>
                            <th scope="row">지역</th>
                            <td scope="row">
                            <input type="text" name="p_area" size ="20" maxlength="80" value="${view.lowEdumin}">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">구분</th>
                            <td scope="row">
                            	<select name="p_gubun">
				                   <option  value="">::구분::</option>
				                   <option  value="UN" <c:if test="${view.schoolClass eq 'UN'}">selected</c:if> >교육청</option>
				                   <option  value="HS" <c:if test="${view.schoolClass eq 'HS'}">selected</c:if> >고등학교</option>
				                   <option  value="MS" <c:if test="${view.schoolClass eq 'MS'}">selected</c:if> >중학교</option>
				                   <option  value="SS" <c:if test="${view.schoolClass eq 'SS'}">selected</c:if> >기타</option>
				                </select>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">고사장명</th>
                            <td scope="row">
                            <input type="text" name="p_edunm" size ="80" maxlength="80" value="${view.schoolNm}">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">주소</th>
                            <td scope="row" style=" line-height:250%;">                            	
                                    <input type="text" name="p_post1" size="5" maxlength="5" readonly value="${view.zipCd1}">
                                   <a href="javascript:sample6_execDaumPostcode()"><img src ="/images/adm/btn/btn_post.gif" alt="우편번호" /></a><br />
                                   <input type="text" name="p_addr" size="90" value="${view.juso}"><br />
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">전화번호</th>
                            <td scope="row"><input type="text" name="p_shtel" size ="15" maxlength="15" value="${view.phoneNo}"></td>
                        </tr>
                        <tr>
                            <th scope="row">홈페이지</th>
                            <td scope="row"><input type="text" name="p_homepage" size ="90" maxlength="100" value="${view.highEdumin}"></td>
                        </tr>
                        <tr>
                            <th scope="row">위치정보</th>
                            <td scope="row"><input type="text" name="p_area_map" size ="90" maxlength="200" value="${view.areaMap}"></td>
                        </tr>
                        <tr>
                            <th scope="row">사용유무</th>
                            <td scope="row">
                            	<ui:code id="p_useyn" selectItem="${view.isuse}" gubun="defaultYN" codetype=""  upper="" title="사용유무" className="" type="select" selectTitle="" event="" />
                            </td>
                        </tr>
				</thead>				
			</table>
   		</div>
		<!-- list table-->	
    
    
    <!-- // search wrap -->
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>목 록</span></a></div>
	    
<c:if test="${empty p_seq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('insert')"><span>저 장</span></a></div>
</c:if>

<c:if test="${not empty p_seq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('delete')"><span>삭 제</span></a></div>
		
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('update')"><span>저 장</span></a></div>
</c:if>	    
		
		
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		thisForm.action = "/adm/hom/sch/selectSchoolRoomList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	

	function whenAction(mode) {
		 if(thisForm.p_area.value==""){
            alert('지역을 입력하세요 ');
            thisForm.p_area.focus();
            return;
          }

        if(thisForm.p_gubun.value==""){
            alert('구분을 입력하세요 ');
            thisForm.p_gubun.focus();
            return;
          }


        if(thisForm.p_edunm.value==""){
            alert('고사장명을 입력하세요 ');
            thisForm.p_edunm.focus();
            return;
          }

        /*if(thisForm.p_post1.value==""){
            alert('우편번호 찾기를 통해 주소를 선택하세요');
            return;
          }

        if(thisForm.p_shtel.value==""){
            alert('전화번호를 입력하세요');
            thisForm.p_shtel.focus();
            return;
          }

        if(thisForm.p_homepage.value==""){
            alert('홈페이지 주소를 입력하세요');
            thisForm.p_homepage.focus();
            return;
          }

        
    	if( thisForm.p_shtel.value && isNaN( thisForm.p_shtel.value.replace(/-/g, ''))  ) {
       		alert( "전화번호는  숫자와 -만 입력 가능합니다." );
       		thisForm.p_shtel.focus();
       		return;
       	}

    	*/

        if(thisForm.p_area_map.value==""){
            alert('위치정보를 입력하세요');
            thisForm.p_area_map.focus();
            return;
          }
		
		var msg = "저장하시겠습니까?";
		if(mode == 'delete') msg = "삭제하시겠습니까?\n삭제하실 경우 카테고리의 등록된 글들은 모두 삭제됩니다.\n그래도 삭제하시겠습니까?";
		if (confirm(msg + "")) {
			thisForm.action = "/adm/hom/sch/selectSchoolRoomAction.do";
			thisForm.p_process.value = mode;
			thisForm.target = "_self";
			thisForm.submit();
		}else{
			return;
		}

	}

</script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;

                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if(data.userSelectedType === 'R'){
                    //법정동명이 있을 경우 추가한다.
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                // 커서를 상세주소 필드로 이동한다.
                thisForm.p_post1.value = data.zonecode; //5자리 새우편번호 사용
                thisForm.p_addr.value = fullAddr;
            }
        }).open();
    }
</script>