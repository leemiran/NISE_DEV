<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data">
<input type="hidden" name="p_subj"  value="${p_subj}">
<input type="hidden" name="p_process" value="">



<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>평가문제관리</h2>
         </div>
		<!-- contents -->
        
		<div>
			<p class="pop_tit">문제 일괄등록</p>			
		</div>
        
            <div class="popCon ML10 MR10">
                <!-- 업로드 선택 -->
                <div class="searchWrap txtL">
                    <ul class="datewrap">
                        <li>
                            <input type="radio" name="p_select" value="1" <c:if test="${empty p_select || p_select eq '1'}">checked</c:if>>문제입력
                            <%-- <input type="radio" name="p_select" value="2" <c:if test="${p_select eq '2'}">checked</c:if>>보기입력 --%>                    
                        	
                        	<input type="file" name="p_file" style="width:450px; height:21px; background:#FFF"/>
                        	
                            <a href="javascript:insert_check('insertFileToDB')" class="btn01"><span>저장</span></a>
                            <a href="javascript:insert_check('previewFileToDB')" class="btn01"><span>미리보기</span></a>
                        </li>				
                    </ul>
                </div>
      	  </div>
      
      
      
      
<c:if test="${not empty list}">

    <div>
		<p class="pop_tit">업로드 내역</p>			
	</div>
		
		
		
      <!-- list table-->
      <div class="tbList">
      
      
	<c:if test="${p_select eq '1'}">      
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="3%"/>		      
		      <col width="15%"/>
		      <col width="15%"/>
		      <col width="%"/>
		      <col width="%"/>
              <col width="%"/>
		      <col width="15%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
		      <col width="10%"/>
	        </colgroup>
		    <thead>
		      <tr>
		        <th scope="row">No</th>
		        <th scope="row">객(1)/주(2)/OX(3)</th>
		        <th scope="row">문제 TEXT</th>
		        <th scope="row">설명 TEXT</th>
		        <th scope="row">보기수</th>
		        <th scope="row">보기1</th>
		        <th scope="row">보기2</th>
		        <th scope="row">보기3</th>
		        <th scope="row">보기4</th>
		        <th scope="row">보기5</th>
		        <th scope="row">정답(보기번호)</th>
		        <th scope="row">출제자</th>
		        <th scope="row">난이도 상(1)/중(2)/하(3)</th>
		        <th scope="row">차시</th>
		        <!-- <th scope="row">등록연도</th>
		        <th scope="row">최초사용연도</th>
		        <th scope="row">최종사용연도</th>
		        <th scope="row">사용횟수</th> -->
		        <th scope="row">사용여부</th>
		        
	          </tr>
	        </thead>
		    <tbody>
		    
				<c:forEach items="${list}" var="result" varStatus="status">			    
			      <tr>
			        <td>${status.count}</td>
			        <td>${result.parameter0}</td>
			        <td>${result.parameter1}</td>
			        <td>${result.parameter2}</td>
			        <td>${result.parameter3}</td>
			        <td>${result.parameter4}</td>
			        <td>${result.parameter5}</td>
			        <td>${result.parameter6}</td>
			        <td>${result.parameter7}</td>
			        <td>${result.parameter8}</td>
			        <td>${result.parameter9}</td>
			        <td>${result.parameter10}</td>
			        <td>${result.parameter11}</td>
			        <td>${result.parameter12}</td>
			        <%-- <td>${result.parameter13}</td>
			        <td>${result.parameter14}</td>
			        <td>${result.parameter15}</td>
			        <td>${result.parameter16}</td> --%>
			        <td>${result.parameter13}</td>
		          </tr>
				</c:forEach>	          
	        </tbody>            
	      </table>
	</c:if>	
	
	</div>
		<!-- list table-->
</c:if>
      
      
      
          <div class="sub_tit ML10 MR10 MT20" style="overflow:hidden;">
                <h4><span class="point" style="font-size:14px;">주의사항 - 필독</span></h4>
                <p class="clear" style=" line-height:22px;">
                    1. 엑셀파일 작성시 아래의 샘플 내용의 <span class="point"> 타이틀</span> 부분을 절대로 추가하지 말것( <span class="point">오직 데이터로만 작성함</span>)<br />
                    2. 엑셀파일 저장시 Excel 97 - 2003 통합 문서(*.xls) 로 저장하여 사용할 것<br />                    
                    3. 샘플 양식 폼을 변경할시 치명적인 오류가 발생합니다.<br /> 
                    4. 샘플 파일을 참고할 것. <a href="#" class="btn01" onclick="javascript:doDownloadSample('examBankExcelFile.xls')"><span class="point">(문제 샘플파일)</span></a><br /> 
                    5. (객/주/OX는 1 : 객관식, 2: 주관식 3: OX식) (난이도는 1 : 상 , 2 : 중, 3: 하 )<br />                           
                  </p>   
                <p class="floatL mrb20"><img src="/images/adm/study/Sample_ExamBank.gif"/></p> 
                
                          
          </div>
        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">			
            <li><a href="javascript:window.close();" class="pop_btn01"><span>닫기</span></a></li>            
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->
</form>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var thisForm = eval('document.<c:out value="${gsPopForm}"/>');


<c:if test="${p_process eq 'insertFileToDB'}">
$(window).bind('unload', function(){
	opener.doPageList('1');
	window.close();
});
</c:if>


function insert_check(process) {
    if(blankCheck(thisForm.p_file.value)){
        alert("DB로 가져갈 파일을 선택해 주세요");
        return;
    }
    if (thisForm.p_file.value.length > 0 ){
        var data = thisForm.p_file.value;
        data = data.toUpperCase(data);

        if (data.indexOf(".XLS") < 0) {
            alert("DB로 입력되는 파일종류는 XLS 파일만 가능합니다.");
            return;
        }
    }

    thisForm.action = "/adm/exm/examBankFileUploadAction.do";
    thisForm.p_process.value = process;
    thisForm.submit();
}

function doDownloadSample(fileName){
	var frm = eval('document.gsPopForm');
 	var link = '/com/fileDownload.do?fileName=' + fileName;
	window.location.href = link;
	return;
}


</script>