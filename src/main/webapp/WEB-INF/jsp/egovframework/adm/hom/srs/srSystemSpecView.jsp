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
	<input type="hidden" name="pageIndex" 	id="p_subjseq2"	value="${pageIndex}">
	<input type = "hidden" name="p_type"     value = "${p_type}" />
	
    <input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
    <input type = "hidden" name="p_seq"     value = "${p_seq}" />
    <input type = "hidden" name="p_srlevel"     value = "${p_srlevel}" />
    <input type = "hidden" name="list_seq"     value = "${viewlist.listSeq}" />    
    
    <input type = "hidden" name="p_process"  value = "" />
    
    <input type = "hidden" name="p_startdate"  value = "" />
    <input type = "hidden" name="p_enddate"    value = "" />
    <input type = "hidden" name="p_compcd"     value = "" />
    <input type = "hidden" name="p_uncompcd"   value = "" />
    <input type = "hidden" name="p_grcodecd"   value = "N000001" />
    <input type = "hidden" name="p_selcomp"    value = "" />
    <input type = "hidden" name="p_content"    value = "" />
    <input type = "hidden" name="p_isAllvalue" value = "" />
    <input type = "hidden" name="p_loginynPre" value = "N" />
    <input type = "hidden" name="p_gubun" value = "N" />
    <input type = "hidden" name="p_reseq" value = "" />
    <input type = "hidden" name="replyinsert"/>    
    	
	
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
            </colgroup>
            <tbody>
                <tr>
                    <th scope="row">작성자</th>
                    <td scope="row">${view.userName}</td>
                    <th scope="row">등록일</th>
                    <td scope="row">${fn2:getFormatDate(view.regDate, 'yyyy.MM.dd')}</td>
                </tr>
                <tr>
                    <th scope="row">요청일자</th>
                    <td scope="row">${fn2:getFormatDate(view.reqdateFrom, 'yyyy.MM.dd')}~${fn2:getFormatDate(view.reqdateTo, 'yyyy.MM.dd')}</td>
                    <th scope="row">완료일자</th>
                    <td scope="row">
                    	${fn2:getFormatDate(view.finishDate, 'yyyy.MM.dd')}
                    </td>
                </tr>
                <tr> 
		            <th scope="row">시스템구분</th>
		            <td scope="row">
		              ${view.sysGubun}&nbsp;&nbsp;
		            </td>
                    <th scope="row">긴급여부</th>
                    <td scope="row">
						<c:if test="${view.busyGubun eq 'A'}"><img src="/images/adm/ico/ico_normal.gif" alt="보통" /></c:if>
						<c:if test="${view.busyGubun eq 'B'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
                    </td>
                </tr>
                <tr>   
                    <th scope="row">제목</th>
                    <td scope="row">
                    	${view.reqTitle}
                    </td>
                    <th scope="row">내용확인일자</th>
                    <td scope="row">
                    	${fn2:getFormatDate(view.confirmDate, 'yyyy.MM.dd HH:mm:ss')}
                    </td>
                </tr>
                
                <tr>
                    <th scope="row">내용</th>
                    <td style="padding-left:5px" scope="row" colspan="3">
                    	<c:out value="${view.reqContent}" escapeXml="false"/>
                    </td>
                </tr>
                <tr>
                    <th scope="row">첨부파일</th>
                    <td scope="row" colspan="3">
                    	<c:forEach items="${fileList}" var="result" varStatus="i">
                    		* <a href="#none" onclick="fn_download('${result.realfile}', '${result.savefile}', 'bulletin')">
                    		<c:out value="${empty result.realfile ? result.savefile : result.realfile}"/>
                    		</a> <br/>        	
                    	</c:forEach>
                    </td>
                </tr>

            </tbody>
        </table>				
    </div>


	<c:if test="${viewlist.reSeq > 0}">    
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
            </colgroup>
            <tbody>
                <tr>
                    <th scope="row">완료일자</th>
                    <td scope="row">
                    	${fn2:getFormatDate(viewlist.finishDate, 'yyyy.MM.dd')}
                    </td>
                    <th scope="row">진행율</th>
                    <td scope="row">
                    	${viewlist.procRate}&nbsp;%
                    </td>
                </tr>
                <tr>
                    <th scope="row">처리자</th>
                    <td scope="row">
                    	${viewlist.userName}
                    </td>
                    <th scope="row">답변제목</th>
                    <td scope="row">
                    	${viewlist.reqTitle}
                    </td>
                </tr>
                
                <tr>
                    <th scope="row">답변내용</th>
                    <td style="padding-left:0px" scope="row"  colspan="3">
						<c:out value="${viewlist.reqContent}" escapeXml="false"/>
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    </c:if>
    

    
    
    <!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>목 록</span></a></div>
			<c:if test="${empty viewlist}">	
				<!-- div class="btnR"><a href="#none" class="btn02" onclick="doView('${view.seq}',0)"><span>답변등록</span></a></div -->
				<div class="btnR"><a href="#none" class="btn02" onclick="doViewNew('${view.seq}',0,'${view.srLevel}')"><span>답변등록</span></a></div>				
				<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('delete',0)"><span>삭 제</span></a></div>
				<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeEdit()"><span>수 정</span></a></div>
			</c:if>
			<c:if test="${!empty viewlist}">	
				<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('redelete','${viewlist.reSeq}')"><span>답변삭제</span></a></div>
				<div class="btnR"><a href="#" class="btn02" onclick="doView('${view.seq}','${viewlist.reSeq}')"><span>답변수정</span></a></div>
				<!-- div class="btnR"><a href="#" class="btn02" onclick="doView1('${view.seq}','${viewlist.reSeq}')"><span>(답변)답변등록</span></a></div-->				
				<div class="btnR"><a href="#" class="btn02" onclick="doViewNew1('${view.seq}','${viewlist.reSeq}','${viewlist.srLevel}')"><span>(답변)답변등록</span></a></div>				
			</c:if>
	</div>
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
//	function pageList(){
		//thisForm.action = "/adm/hom/not/selectNoticeList.do";
		//thisForm.target = "_self";
		//thisForm.submit();
	//}

	function pageList(){
		thisForm.action = "/adm/hom/srs/srSystemSpecList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}



	function whenNoticeEdit(){
        thisForm.action = "/adm/hom/srs/srSystemSpecInsert.do";
        thisForm.p_reseq.value = 0;
        thisForm.target = "_self";
        thisForm.submit();
    }

	function doView(p_seq,p_reseq){
		thisForm.action = "/adm/hom/srs/srSystemSpecViewReply.do";
		thisForm.p_reseq.value = p_reseq;
		thisForm.target = "_self";
		thisForm.submit();
	}	
	function doViewNew(p_seq,p_reseq,p_srlevel){
		thisForm.action = "/adm/hom/srs/srSystemSpecViewReply.do";
		thisForm.p_reseq.value = p_reseq;
		thisForm.p_srlevel.value = p_srlevel;		
		thisForm.target = "_self";
		thisForm.submit();
	}	
	function doView1(p_seq,p_reseq){
		thisForm.action = "/adm/hom/srs/srSystemSpecViewReplyRe.do";
		thisForm.p_reseq.value = p_reseq;
		thisForm.replyinsert.value = "insert";		
		thisForm.target = "_self";
		thisForm.submit();
	}	
	function doViewNew1(p_seq,p_reseq,p_srlevel){
		thisForm.action = "/adm/hom/srs/srSystemSpecViewReplyRe.do";
		thisForm.p_reseq.value = p_reseq;
		thisForm.p_srlevel.value = p_srlevel;		
		thisForm.replyinsert.value = "insert";		
		thisForm.target = "_self";
		thisForm.submit();
	}	
	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/srs/srSystemSpecList.do"
		frm.target = "_self";
		frm.submit();
	}
	//삭제하기
	function whenNoticeSave(mode,re_seq) {

		if(confirm("현재글을 삭제하시겠습니까?"))
		{
	        thisForm.pageIndex.value = 1;
	        thisForm.action = "/adm/hom/srs/srSystemActionPage.do";
	        thisForm.p_process.value = mode;
			thisForm.p_reseq.value = re_seq;	        
	        thisForm.target = "_self";
	        thisForm.submit();
		}
    }
</script>