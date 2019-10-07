<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>

<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" name="p_faqcategory" value="${p_faqcategory}"/>
	
	
<!--	<div><img src="/images/user/faq_img.gif" /></div>-->
	<!-- search wrap-->
		<div class="searchWrap">
			<div class="in">
				<select name="search_group" title="항목선택">
					<option value='title'    >제목</option>
                	<option value='contents' >내용</option>
				</select>
				<input type="text" name="search_word" id="search_word" value="${search_word}" size="50" onkeypress="fn_keyEvent('doSearchList')" style="ime-mode:active;" title="검색어"/>
				<a href="#none" onclick="doSearchList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
			</div>
		</div>
		<!-- // search wrap -->	
            
            
            
        <!-- category wrap-->
		<div class="faqWrap">
			<c:set var="categorySum" value="0"></c:set>
			<c:set var="count">1</c:set>
			
			<c:forEach items="${categoryList}" var="result" varStatus="i">
				<c:set var="categorySum">${categorySum + result.catecount}</c:set>
			</c:forEach>
			<div class="floatL">
                <ul>
				<li><a href="#none" onclick="doCategoryList('')">전체 (${categorySum})</a></li>
				
            <c:forEach items="${categoryList}" var="result" varStatus="i">
                <li><a href="#none" onclick="doCategoryList('${result.faqcategory}')">${result.faqcategorynm}(${result.catecount})</a></li>
                <c:set var="count">${count+1}</c:set>
            <c:if test="${count % 2 == 0}">
                	</ul> 
            </div>
            <div class="floatL">
                <ul>
            </c:if>
            </c:forEach>
           		</ul> 
            </div> 
           
           
		</div>
		<!-- // category wrap -->		
		
		
		<div class="sub_txt_1depth m30">
		<c:if test="${p_faqcategory eq ''}"><h4>전체</h4></c:if>
		<c:forEach items="${categoryList}" var="result" varStatus="i">
			<c:if test="${result.faqcategory eq p_faqcategory}"><h4>${result.faqcategorynm}</h4></c:if>
		</c:forEach>
		</div>
		<!-- list table-->
		<div class="tbdown">
			<ul class="datalist">			

<c:forEach items="${list}" var="result" varStatus="i">				
			<li>
						<c:if test="${i.count < 10}">
							<a href="#none" onclick="clickHandler('${i.count}')">0<c:out value="${i.count}"/>.
							<c:out value="${result.title}"/></a>
						</c:if>
						
						<c:if test="${i.count >= 10}">
							<a href="#none" onclick="clickHandler('${i.count}')"><c:out value="${i.count}"/>.
							<c:out value="${result.title}"/></a>
						</c:if>
			</li> 
            <li class="hide" id='Out<c:out value="${i.count}"/>details'>

                        	<ul>
                            	<li><c:out value="${result.contents}" escapeXml="false"/></li>
                            	<c:if test="${!empty result.realFile}" >
                            	<li>첨부파일 : 
                            	<a href="#none" onclick="fn_download('${result.realFile}', '${result.saveFile}', 'faq_upload')">
	                    			<c:out value="${empty result.realFile ? result.saveFile : result.realFile}"/>
	                    		</a>
                            	</li>
                            	</c:if>                            
                            </ul>
           </li>
</c:forEach>

			</ul>
		</div>
		<!-- list table-->

           
</form>


<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {
	thisForm.action = "/usr/bod/dataList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function doSearchList() {

	if(thisForm.search_word.value == ''){
		if(!confirm("검색어 없이 검색하시면 전체가 검색됩니다. 검색하시겠습니까?")){
			thisForm.search_word.focus(); 
			return;
		}else{
			thisForm.action = "/usr/bod/dataList.do";
			thisForm.target = "_self";
			thisForm.submit();
			
		}
	}else{
		
		thisForm.action = "/usr/bod/dataList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	
}

function doCategoryList(p_faqcategory) {
	thisForm.action = "/usr/bod/dataList.do";
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

//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->