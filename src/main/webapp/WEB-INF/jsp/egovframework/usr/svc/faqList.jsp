<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype" value="<c:out value="${p_contenttype}"  escapeXml="true" />" />
<input type="hidden" name="p_subj" value="<c:out value="${p_subj}"  escapeXml="true" />" />
<input type="hidden" name="p_year" value="<c:out value="${p_year}"  escapeXml="true" />" />
<input type="hidden" name="p_subjseq" value="<c:out value="${p_subjseq}"  escapeXml="true" />" />
<input type="hidden" name="p_studytype" value="<c:out value="${p_studytype}"  escapeXml="true" />" />
<input type="hidden" name="p_process" value="<c:out value="${p_process}"  escapeXml="true" />" />
<input type="hidden" name="p_next_process" value="<c:out value="${p_next_process}"  escapeXml="true" />" />
<input type="hidden" name="p_height" value="<c:out value="${p_height}"  escapeXml="true" />" />
<input type="hidden" name="p_width" value="<c:out value="${p_width}"  escapeXml="true" />" />
<input type="hidden" name="p_lcmstype" value="<c:out value="${p_lcmstype}"  escapeXml="true" />" />
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
	<fieldset>
    <legend>자주하는 질문검색</legend>
	<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" name="p_faqcategory" value="<c:out value="${p_faqcategory}"  escapeXml="true" />" />
	<div><img src="/images/user/faq_img.gif" alt="본 사이트를 이용하시는 분들이 가장 궁금해 하시는 대표적인 질문과 자주하시는 질문들을 모아 놓았습니다. Q&nbsp;A에 문의글을 등록하시거나 관리자에게 메일을 보내시기전에 미리 FAQ를 확인하세요. 빠르고 쉽게 많은 정보를 얻으실수 있습니다."/></div>
	<!-- search wrap-->
		
		<%-- <div class="searchWrap">
			<div class="in">				
                <select name="search_group" title="검색영역">
					<option value='title'    >제목</option>
                	<option value='contents' >내용</option>
				</select>
                <label for="search_word" class="blind">검색어</label>                
				<input type="text" name="search_word" id="search_word" value="<c:out value="${search_word}" escapeXml="true" />" size="50" onkeypress="fn_keyEvent('doSearchList')" style="ime-mode:active;" title="검색어"/>
				<a href="#none" onclick="doSearchList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
			</div>
		</div> --%>
		<!-- // search wrap -->	
            
            
            
        <!-- category wrap-->
		<div>
			<c:set var="categorySum" value="0"></c:set>
			<c:set var="count">1</c:set>
			<c:set var="allcount">0</c:set>
			
			<c:forEach items="${categoryList}" var="result" varStatus="i">
				<c:set var="categorySum">${categorySum + result.catecount}</c:set>
				<c:set var="allcount">${i.count }</c:set>
			</c:forEach>
			
			<%-- <div class="floatL">
                <ul>
				<li><a href="#" onclick="doCategoryList('')">전체 (${categorySum})</a></li>
				
	            <c:forEach items="${categoryList}" var="result" varStatus="i">
	                <li><a href="#" onclick="doCategoryList('${result.faqcategory}')">${result.faqcategorynm}(${result.catecount})</a></li>
	                <c:set var="count">${count+1}</c:set>
	            <c:if test="${count % 3 == 0}">
	                	</ul> 
	         </div>	            
	         <div class="floatL">
	                	<ul>
	            </c:if>
				</c:forEach>
    			</ul> 
            </div>    --%>      
           
		</div>
		<!-- // category wrap -->	

		
		
		<!-- list table-->
		<div class="sub_txt_1depth m30">
		<c:if test="${p_faqcategory eq ''}"><h4>전체</h4></c:if>
		<c:forEach items="${categoryList}" var="result" varStatus="i">
			<c:if test="${result.faqcategory eq p_faqcategory}"><h4>${result.faqcategorynm}</h4></c:if>
		</c:forEach>
		</div>
		<div class="tbdown">
			<ul class="faqlist">
<c:forEach items="${list}" var="result" varStatus="i">				
					<li>
						<c:if test="${i.count < 10}">
							<strong><font color="#6B66FF">[질문]</font></strong><a href="#" onclick="clickHandler('${i.count}');return false;">0<c:out value="${i.count}"/>.
							<c:out value="${result.title}"/></a>
						</c:if>
						
						<c:if test="${i.count >= 10}">
							<strong><font color="#6B66FF">[질문]</font></strong><a href="#" onclick="clickHandler('${i.count}');return false;"><c:out value="${i.count}"/>.
							<c:out value="${result.title}"/></a>
						</c:if>
					</li>
                    <li class="hide" id='Out<c:out value="${i.count}"/>details'>

                        	<ul>
                            	<li><strong><font color="#008299">[답변]</font></strong><br/><c:out value="${result.contents}" escapeXml="false"/></li>                                
                            </ul>
                    </li>
</c:forEach>
			</ul>
		</div>
		<!-- list table-->
 	</fieldset>          
</form>
<script type="text/javascript">
//<![CDATA[
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {
	thisForm.action = "/usr/svc/faqList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function doSearchList() {

	if(thisForm.search_word.value == ''){
		if(!confirm("검색어 없이 검색하시면 전체가 검색됩니다. 검색하시겠습니까?")){
			thisForm.search_word.focus(); 
			return;
		}else{
			thisForm.action = "/usr/svc/faqList.do";
			thisForm.target = "_self";
			thisForm.submit();
			
		}
	}else{
		
		thisForm.action = "/usr/svc/faqList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	
}

function doCategoryList(p_faqcategory) {
	thisForm.action = "/usr/svc/faqList.do";
	thisForm.search_word.value = '';
	thisForm.p_faqcategory.value = p_faqcategory;
	thisForm.target = "_self";
	thisForm.submit();
}

var old = '';
function clickHandler(i) {
    answer = document.getElementById("Out" + i + "details").style;
    if( old != answer )
    {
        if(old!='')
        {
            old.display='none';
        }
        answer.display ='block';
        old = answer;
    }
    else
    {
        answer.display = 'none';
        old='';
    }
}

//]]>
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
