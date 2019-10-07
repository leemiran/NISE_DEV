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
    <div id="title">우편번호찾기</div>
<!-- TOP 끝-->
    <div id="contents">
 	    <form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
            <input type="hidden" id="userId"  name="userId"  value="${userId}" />
      		<input type="hidden" id="aspCode" name="aspCode" value="${aspCode}" />
      		<input type="hidden" id="cmd"     name="cmd"     value="${cmd}" />
      
        <!-- 검색 시작-->
        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="box2">
            <tr>
                <td class="box2_top"></td>
            </tr>
            <tr>
                <td class="box2_in">
                    <div class="center" style="padding:0 110px 0 80px ;">
                        <div class="flLeft">
                            <span class="text_olive">읍,면,동,리,건물명</span> <input type="text" id="searchValue" name="searchValue" class="infut_text" title="검색" value="${searchValue}" />
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
        <table class="tbl" summary="우편번호,주소로 구성">
	        <caption>우편번호찾기</caption>
		        <colgroup>
		            <col width="50px" />
					<col width="100px" />
		        </colgroup>
		    <thead>
			    <tr>
				  <th>우편번호</th>
				  <th>주소</th>
			    </tr>
		    </thead>
		    <tbody>
		    	<c:forEach var="result" items="${list_zipSearch}" varStatus="status">
			    <tr>
				  <td><c:out value="${result.zipcode}"/></td>
				  <td><a href="#" onclick="CtlExecutor.doSendZip('<c:out value="${result.zip1}"/>', '<c:out value="${result.zip2}"/>', '<c:out value="${result.addr1}"/>')"><c:out value="${result.addr1}"/></a></td>
				</tr>
				</c:forEach>	
		    </tbody>
	    </table>
        </form>         
        <!-- 테이블 끝-->
        
        <br class="clear" />

        <!-- 페이징 시작 -->
        <c:if test="${not empty paginationInfo && not empty list_zipSearch}">
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

    	        var $searchValue = $("#searchValue");

                if($.trim($searchValue.val()) == "") {
    			    alert("검색어를 입력하시기 바랍니다.");
    			    $searchValue.val("");
    			    $searchValue.focus();
    			    return;
                }
		        
                $("#<c:out value="${gsPopForm}"/>").onSubmit(
                    options = {
                        url         : "<c:out value="${gsDomainContext}"/>/com/zip/ZipSearchSelect.do",
                        method      : "pageList",
                        callpost    : function() {
    					                  $("#pageIndex").val(1),
    					                  $("#cmd").val("GO");
    					              },
                        validation  : false
                    }
                );
            },

            doLinkPage : function(pageNo) {
                $("#<c:out value="${gsPopForm}"/>").onSubmit(
                    options = {
                        url         : "<c:out value="${gsDomainContext}"/>/com/zip/ZipSearchSelect.do",
                        callpost    : function() {
    		                              $("#pageIndex").val(pageNo),
    		                              $("#cmd").val("GO");
    		                          },
                        validation  : false
                    }
                );
            },
            
            doSendZip : function(zip1, zip2, addr) {
                var opener = window.dialogArguments;
				opener.$("#firstZipCode").val(zip1);
				opener.$("#secondZipCode").val(zip2);
				opener.$("#addr1").val(addr);
                window.close();
            }
        };
</script>
</body>
</html>
