<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%//@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/common/admCommonHead.jsp" %>
<link href="/css/pop.css" rel="stylesheet" type="text/css" />
<base target="_self">
<style>
body {overflow-x:hidden; overflow-y:auto}
</style>
</head> 

<body>
<div id="wrap">
<!-- TOP 시작-->
    <div id="title">강사조회</div>
<!-- TOP 끝-->
    <div id="contents">
 	    <form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
      
        <!-- 검색 시작-->
        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="box2">
            <tr>
                <td class="box2_top"></td>
            </tr>
            <tr>
                <td class="box2_in">
                    <div class="center" style="padding:0 120px 0 90px ;">
                        <div class="flLeft">
                            <span class="text_olive">강사명</span> <input type="text" id="searchValue" name="searchValue" class="infut_text" title="검색" value="${searchValue}" />
                        </div>
                        <div class="flRight"><div class="btn_01"><a href="#" onclick="CtlExecutor.doPageList()">조회</a></div></div>
                    </div>
                </td>
            </tr>
            <tr>
            	<td class="box2_bottom"></td>
            </tr>
        </table>
        <!-- 검색끝 -->
      
        <%@ include file = "/WEB-INF/jsp/egovframework/adm/common/admCommonPageUnit.jsp" %>
      
        <!-- 테이블 시작-->
        <table class="tbl" summary="번호, 강사명, 직위, 기관명, 생년월일로 구성">
	        <caption>강사리스트</caption>
		        <colgroup>
		            <col width="50px" />
					      <col width="100px" />
					      <col width="100px" />
					      <col width="100px" />
					      <col width="100px" />
					      <col width="100px" />
		        </colgroup>
		    <thead>
			    <tr>
				      <th>번호</th>            
				      <th>강사명</th>
				      <th>직위</th>
				      <th>기관명</th>
				      <th>생년월일</th>
			    </tr>
		    </thead>
		    <tbody>
		    	<c:forEach var="result" items="${list_user}" varStatus="i">
			    <tr>
					<td><c:out value="${pageTotCnt - (((recordCountPerPage * pageIndex) - recordCountPerPage) + (i.count-1))}"/></td>
			      	<td><a href="JavaScript:CtlExecutor.doSendRank( '${i.index}' )"><c:out value="${result.userName}"/></a></td>
			      	<td><c:out value="${result.rankNm}"/></td>
			      	<td><c:out value="${result.posNm}"/></td>
			      	<td><c:out value="${result.birthday}"/></td>
			      	<input type="hidden" id="userId"       name="userId"        value="<c:out value="${result.userId}"/>">
			      	<input type="hidden" id="seqId"        name="seqId"     	value="<c:out value="${result.seqId}"/>">
			      	<input type="hidden" id="userName"     name="userName"      value="<c:out value="${result.userName}"/>">
			      	<input type="hidden" id="rankNm"       name="rankNm"       	value="<c:out value="${result.rankNm}"/>">
			      	<input type="hidden" id="rankCd"       name="rankCd"        value="<c:out value="${result.rankCd}"/>">
			      	<input type="hidden" id="posNm"        name="posNm"      	value="<c:out value="${result.posNm}"/>">
			      	<input type="hidden" id="posCd"        name="posCd"        	value="<c:out value="${result.posCd}"/>">
			      	<input type="hidden" id="birthday"     name="birthday"      value="<c:out value="${result.birthday}"/>">
			      	<input type="hidden" id="aspCode"      name="aspCode"       value="<c:out value="${result.aspCode}"/>">
			      	<input type="hidden" id="gradeCd"      name="gradeCd"      	value="<c:out value="${result.gradeCd}"/>">
			      	<input type="hidden" id="lectCd"       name="lectCd"        value="<c:out value="${result.lectCd}"/>">
			      	<input type="hidden" id="officialYn"   name="officialYn"    value="<c:out value="${result.officialYn}"/>">
			      	<input type="hidden" id="telNo"        name="telNo"       	value="<c:out value="${result.telNo}"/>">
			      	<input type="hidden" id="mobileNo"     name="mobileNo"      value="<c:out value="${result.mobileNo}"/>">
			      	<input type="hidden" id="email"        name="email"       	value="<c:out value="${result.email}"/>">
			      	<input type="hidden" id="bankCd"       name="bankCd"        value="<c:out value="${result.bankCd}"/>">
			      	<input type="hidden" id="account"      name="account"       value="<c:out value="${result.account}"/>">
			      	<input type="hidden" id="bankOwner"    name="bankOwner"     value="<c:out value="${result.bankOwner}"/>">
				</tr>
				</c:forEach>	
		    </tbody>
	    </table>
        </form>         
        <!-- 테이블 끝-->
        
        <br class="clear" />

        <!-- 페이징 시작 -->
        <c:if test="${not empty paginationInfo}">
	    <div class="paging">
	        <ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="CtlExecutor.doLinkPage"/>
	    </div>
	    </c:if>
	    <!-- 페이징 끝 -->
    </div>
    <div id="footer"><a href="#" onclick="window.close();"><img src="/images/pop/btn_close.gif" alt="닫기" width="46" height="11" /></a></div>
</div>

<!-- 페이지 정보 -->
<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
<!-- 페이지 정보 -->
   
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">

	$().ready(function() {
		$("input:text").keydown(function(evt){
			if (evt.keyCode==13)
				CtlExecutor.doPageList();
		});
	});

    var CtlExecutor = {
            
            doPageList : function() {

                $("#<c:out value="${gsPopForm}"/>").onSubmit(
                    options = {
                        url         : "<c:out value="${gsDomainContext}"/>/com/lec/comLecturerSearch_P.do",
                        method      : "pageList",
                        callpost    : function() {
    					                  $("#pageIndex").val(1);
    					              },
                        validation  : false
                    }
                );
            },

            doLinkPage : function(pageNo) {
                $("#<c:out value="${gsPopForm}"/>").onSubmit(
                    options = {
                        url         : "<c:out value="${gsDomainContext}"/>/com/lec/comLecturerSearch_P.do",
                        callpost    : function() {
    		                              $("#pageIndex").val(pageNo);
    		                          },
                        validation  : false
                    }
                );
            },
            
            doSendRank : function(seq) {

                var arr = new Array(18);

                arr[0]  = $($("input[name=userId]").get(seq)).val();		// 강사아이디
                arr[1]  = $($("input[name=seqId]").get(seq)).val();			// 강사코드
                arr[2]  = $($("input[name=userName]").get(seq)).val();		// 강사명
                arr[3]  = $($("input[name=rankNm]").get(seq)).val();		// 직위명
                arr[4]  = $($("input[name=rankCd]").get(seq)).val();		// 직위코드
                arr[5]  = $($("input[name=posNm]").get(seq)).val();			// 기관명
                arr[6]  = $($("input[name=posCd]").get(seq)).val();			// 기관코드
                arr[7]  = $($("input[name=birthday]").get(seq)).val();		// 생년월일
                arr[8]  = $($("input[name=aspCode]").get(seq)).val();		// ASP코드
                arr[9]  = $($("input[name=gradeCd]").get(seq)).val();		// 강사등급
                arr[10] = $($("input[name=lectCd]").get(seq)).val();		// 강사유형
                arr[11] = $($("input[name=officialYn]").get(seq)).val();	// 공무원여부
                arr[12] = $($("input[name=telNo]").get(seq)).val();			// 전화번호
                arr[13] = $($("input[name=mobileNo]").get(seq)).val();		// 휴대전화번호
                arr[14] = $($("input[name=email]").get(seq)).val();			// 이메일
                arr[15] = $($("input[name=bankCd]").get(seq)).val();		// 은행코드
                arr[16] = $($("input[name=account]").get(seq)).val();		// 계좌번호
                arr[17] = $($("input[name=bankOwner]").get(seq)).val();		// 예금주명

                parent.returnValue = arr;

                window.close();

                /**
				부모창에서 값받기
				var arr = window.showModalDialog('','','');
				if( arr ){
					alert(arr[0]);  // 아이디
				}
                */
                
                
            }
        };
</script>
</body>
</html>
