<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrStudyHead.jsp" %>
</head> 

<body>
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2" style="height:720px; overflow: auto;">
		<div class="popCon">
			<!-- header -->
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>
			<!-- //header -->
			<!-- contents -->
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				
				<form name="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				<input type="hidden" name="p_class" id="p_class" value="${p_class}">
				<input type="hidden" name="p_ordseq" id="p_ordseq" value="${p_ordseq}">
				<input type="hidden" name="p_weeklyseq" id="p_weeklyseq" value="${selectViewOrderStu.weeklyseq}">
				<input type="hidden" name="p_weeklysubseq" id="p_weeklysubseq" value="${selectViewOrderStu.weeklysubseq}">
				<input type="hidden" name="p_adddate" id="p_adddate" value="${selectViewOrderStu.adddate}">
				<input type="hidden" name="p_addsubmitcnt" id="p_addsubmitcnt" value="${selectViewOrderStu.addsubmitcnt}">
				<input type="hidden" name="p_submitType" id="p_submitType" value="">
				
				
				<div style="overflow: auto; height: 470px;">
				<h4 class="tit">과제</h4>
				<div class="tbDetail">
					<table summary="제목, 제출기한, 제출방법, 추가제출기한, 상태, 내용/주의사항, 제출양식, 첨부파일로 구성" cellspacing="0" width="100%">
						<caption>과제</caption>
						<colgroup>
							<col width="20%" />
							<col />
							<col width="15%" />
							<col width="20%" />
						</colgroup>
						<tbody>
							<tr class="title">
								<th scope="col">제목</th>
								<td colspan="3">${selectViewOrderStu.title}</td>
							</tr>
							<tr>
								<th scope="col">제출기한</th>
								<td>${fn2:getFormatDate(selectViewOrderStu.startdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(selectViewOrderStu.expiredate, 'yyyy.MM.dd')}</td>
								<th scope="col">제출방법</th>
								<td>${selectViewOrderStu.reptype eq 'P' ? '개인별' : '팀별'}</td>
							</tr>
							<tr>
								<th scope="col">추가제출기한</th>
								<td>${fn2:getFormatDate(selectViewOrderStu.restartdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(selectViewOrderStu.reexpiredate, 'yyyy.MM.dd')}</td>
								<th scope="col">상태</th>
								<td>${selectViewOrderStu.submityn eq 'Y' ? '제출' : '미제출'}</td>
							</tr>
							<tr class="title">
								<th scope="col">내용/주의사항</th>
								<td colspan="3">
									<textarea rows="9" style="width:100%"  title="내용/주의사항"><c:out value="${selectViewOrderStu.contents}" /></textarea>
								</td>
							</tr>
							<tr>
								<th scope="col">제출양식</th>
								<td colspan="3">${text_filetype}</td>
							</tr>
							<tr class="title">
								<th scope="col">첨부파일</th>
								<td colspan="3">
									<c:if test="${empty selectProfFiles}">첨부파일이 없습니다.</c:if>
			                    	<c:if test="${not empty selectProfFiles}">
			                    		<c:forEach items="${selectProfFiles}" var="file">
					                    <a href="#none" onclick="fn_download('${file.realfile}', '${file.newfile}', 'reportprof')"><c:out value="${file.realfile}"/></a>
					                    <input type="hidden" name="p_savefile"  value="<c:out value="${file.newfile}"/>" />
					                    <br/>
			                    		</c:forEach> 
			                    	</c:if>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<h4 class="tit">과제 제출</h4>
				<div class="tbDetail">
					<table summary="제목, 제출기한, 제출방법, 추가제출기한, 상태, 내용/주의사항, 제출양식, 첨부파일로 구성" cellspacing="0" width="100%">
						<caption>과제 제출</caption>
						<colgroup>
							<col width="20%" />
							<col width="" />
						</colgroup>
						<tbody>
							<c:if test="${not empty profData.ldate}">
								<tr class="title">
									<th scope="col">제출일</th>
									<td>
										${fn2:getFormatDate(profData.ldate, 'yyyy.MM.dd')}
									</td> 
								</tr>
							</c:if>
							<tr class="title">
								<th scope="col">제목</th>
								<td>
									<input name="p_title" type="text" class="text" style="width:95%" value="${profData.title}" title="제목"/>
								</td> 
							</tr>
							<tr class="title" style="display:none;">
								<th scope="col">내용</th>
								<td>
									<textarea name="p_contents" rows="10" scrollbar="no" style="width:100%;height:90px;"  title="내용">${profData.contents}</textarea>
								</td> 
							</tr>
							<tr class="title">
								<th scope="col">파일</th>
								<td>
									<c:if test="${not empty profData.realfile}">
										<a href="#none" onclick="fn_download('${profData.realfile}', '${profData.newfile}', 'reportstu/${p_year}/${p_subjseq}/${sessionScope.grcode}/${p_subj}/${p_class}/${p_ordseq}')"><c:out value="${profData.realfile}"/></a>
<!--					                    <input type="checkbox"  name="p_filedel" value="<c:out value="1"/>"> (삭제시 체크)<br/>-->
										</br>
										<input type="hidden" name="p_realfile1"  value="<c:out value="${profData.realfile}"/>">
										<input type="hidden" name="p_savefile1"  value="<c:out value="${profData.newfile}"/>">
										<input type="hidden" name="p_fileseq1"  value="<c:out value="1"/>">
									</c:if>
									<c:if test="${(selectViewOrderStu.indate eq 'Y' || selectViewOrderStu.adddate eq 'Y')}">
									<input type="file" name="p_files" style="width:96%" title="파일"/>
									<input type="hidden" name="p_upload_dir" value="reportstu/${p_year}/${p_subjseq}/${sessionScope.grcode}/${p_subj}/${p_class}/${p_ordseq}" />
									</c:if>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				</div>
				</form>
				<ul class="btnR">
				<c:if test="${(selectViewOrderStu.indate eq 'Y' || selectViewOrderStu.adddate eq 'Y') && selectViewOrderStu.submityn eq 'N'}">
					<li><a class="btn02" href="#" onclick="doSubmit('in')"><span>제출</span></a></li>
				</c:if>
				
				<c:if test="${(selectViewOrderStu.indate eq 'Y' || selectViewOrderStu.adddate eq 'Y') && selectViewOrderStu.submityn eq 'Y'}">
				
					<c:if test="${selectViewOrderStu.indate eq 'Y'}">
						<li><a class="btn02" href="#" onclick="doSubmit('mo');"><span>과제변경</span></a></li>
					</c:if>
					
					<c:if test="${selectViewOrderStu.adddate eq 'Y'}">
						<c:if test="${selectViewOrderStu.addsubmitcnt >=1}">
							<li><a class="btn02" href="#" onclick="doNotSubmit();"><span>과제변경</span></a></li>
						</c:if>
						<c:if test="${selectViewOrderStu.addsubmitcnt == 0}">
							<li><a class="btn02" href="#" onclick="doSubmit('up');"><span>과제변경</span></a></li>
						</c:if>
					</c:if>
					
					
					
					
					
				</c:if>
				
					
					<li><a class="btn02" href="#" onclick="doPageList()"><span>목록</span></a></li>
				</ul>
				<br/><br/>
				<div style="text-align:center;">1차 제출기간에는 계속 변경 가능하지만, 추가제출기간에 과제 변경할 시에는 1회만 변경 가능. 문의사항 있을시 전화주세요.</div>
			
		  	</div>
			<!-- //contents -->
		</div>
	</div>
</div>
</body>
</html>
<script type="text/javascript">

	/* ********************************************************
	 * 조회
	 ******************************************************** */
	function doPageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyReportList.do";
		frm.target = "_self";
		frm.submit();
	}

	// 재설정
	function reset(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.reset();
	}

	
	function doSubmit(submitType){
		
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		
		
		if( frm.p_title.value == "" ){
			alert("제목을 입력하세요.");
	        frm.p_title.focus();
			return;
		}

		if (realsize(frm.p_title.value) > 120) {
	        alert("제목은 한글기준 60자를 초과하지 못합니다.");	//제목은 한글기준 60자를 초과하지 못합니다.
	        frm.p_title.focus();
	        return;
	    }

		frm.p_submitType.value = submitType;
		
	    frm.target = "_self";
	    frm.action = "/usr/stu/std/userStudyReportInsertData.do";
	    frm.submit();

	}
	
	function doNotSubmit(){
		alert('1차 제출기간에는 계속 변경 가능하지만, 추가제출기간에 과제 변경할 시에는 1회만 변경 가능. 문의사항 있을시 전화주세요.');
	}

	document.title="과제제출 : 나의 강의실 : 국립특수교육원부설원격교육연수원";

</script>
