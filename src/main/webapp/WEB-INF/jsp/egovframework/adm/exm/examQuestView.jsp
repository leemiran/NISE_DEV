<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<style>
table.ex1 {width:100%; margin:0 auto; text-align:left; border-collapse:collapse; text-decoration: none; }
 .ex1 th, .ex1 td {padding:0px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>

<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
<input type="hidden" name="p_subj"  value="${p_subj}">
<input type="hidden" name="p_exam_subj" 				id="p_exam_subj" 			value="${p_subj}">
<input type="hidden" name="p_examnum"    value="${p_examnum}">
<input type="hidden" name="p_lesson" value="001">
<input type="hidden" name="p_process" value="">
<input type="hidden" name="imgNo" value="">
<input type="hidden" name="imgSelNo" value="">



<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>평가문제관리</h2>
         </div>
		<!-- contents -->
        
        
            <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="40%" />
                        <col width="10%" />
                        <col width="40%" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="col">문제분류</th>
                            <td>
                                <select name="p_examtype" onchange="display()">
                                    <option value="1" <c:if test="${view.examtype eq '1'}">selected</c:if>>객관식</option>
                                    <option value="2" <c:if test="${view.examtype eq '2'}">selected</c:if>>주관식</option>
                                    <option value="3" <c:if test="${view.examtype eq '3'}">selected</c:if>>OX문제</option>
                               </select>
                               
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">사용여부</th>
                            <td>
                                <ui:code id="p_isuse" selectItem="${view.isuse}" gubun="defaultYN" codetype=""  upper="" title="사용여부" className="" type="select" selectTitle="" event="" />
                        	
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">난이도</th>
                            <td>
                                <ui:code id="p_levels" selectItem="${view.levels}" gubun="" codetype="0014" levels="1" upper="" title="난이도" className="" type="select" selectTitle="" event="" />
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">출제자</th>
                            <td>
                                <input type="text" id="p_examiner" name="p_examiner" value="${view.examiner}" alt="출제자">
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">문제제출차시</th>
                            <td>
                                <input type="text" id="p_lessonnum" name="p_lessonnum" value="${view.lessonnum}" alt="문제제출차시">
                            </td>
                        </tr>
                        
                        <%-- <tr>
                          <th scope="col">등록연도</th>
                            <td>
                                <input type="text" id="p_regyear" name="p_regyear" value="${view.regyear}" alt="등록연도">
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">최초사용연도</th>
                            <td>
                                <input type="text" id="p_firstyear" name="p_firstyear" value="${view.firstyear}" alt="최초사용연도">
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">최종사용연도</th>
                            <td>
                                <input type="text" id="p_lastyear" name="p_lastyear" value="${view.lastyear}" alt="최종사용연도">
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">사용횟수</th>
                            <td>
                                <input type="text" id="p_usecnt" name="p_usecnt" value="${view.usecnt}" alt="사용횟수">
                            </td>
                        </tr>  --%>                       
                        <tr>
                            <th scope="col">문제</th>
                            <td>
                            <textarea name="p_examtext" style="width:95%" rows="10">${view.examtext}</textarea>
                            			<font color="red">
                            			<strong>
                            			<br/>※ 문제 입력중 "<font color="blue">보기&gt;</font>" 입력을 하시면 "<font color="blue">보기&gt;</font>" 다음의 내용은 보기 테이블로 보여집니다.
                            			<br/> 예)
                            			<table class='ex1'><tr><th scope='col'>보기</th><td>
                            					"<font color="blue">보기&gt;</font>"<br/>아래(하위) 내용이 모두 담깁니다.
                            				</td>
                            			</tr>
                            			</table>
                            			</strong>
                            			</font>
                        		<br/>
			               		<a href="javascript:doQuestImage('0','')" class="btn01"><span>이미지 등록</span></a>
                            </td>
                        </tr>
                        <tr>
                            <th scope="col">문제해설</th>
                            <td><textarea name="p_exptext" style="width:95%" rows="5">${view.exptext}</textarea>
                            <br/>                            	
			               		<a href="javascript:doQuestImage('4','')" class="btn01"><span>이미지 등록</span></a>
                            </td>
                        </tr>					
                    </tbody>
                </table>
      	  </div>
      
      
<!--      객관식-->
          <div class="popCon MR20" id="answerTbl1">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="8%" />
                        <col width="80%" />	
                        <col width="12%" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th colspan="3" class="txtC">보기</th>                            
                        </tr>
                        
                        
<c:forEach begin="1" end="5" var="i">
		<c:set var="v_seltext"></c:set>
		<c:set var="v_isanswer"></c:set>
		
		<c:if test="${view.examtype eq '1'}">
			<c:forEach items="${list}" var="result" varStatus="status">
				<c:if test="${result.selnum eq i}">
					<c:set var="v_seltext">${result.seltext}</c:set>
					<c:set var="v_isanswer">${result.isanswer eq 'Y' ? 'checked' : ''}</c:set>
				</c:if>
			</c:forEach>
		</c:if>
		
                        <tr>
                            <th scope="col" class="bold txtC">${i}번</th>
                            <td>
                            	<input type="text" name="p_seltext1_${i}" style="width:82%" value="${v_seltext}"/>
                            	&nbsp;<a href="javascript:doQuestImage('1',${i})" class="btn01"><span>이미지 등록</span></a>
                            </td>
                            <td><input type="checkbox" name="p_isanswer1_${i}" value="Y" class="vrM" ${v_isanswer}/> 정답여부</td>
                        </tr>
</c:forEach>
                    </tbody>
                </table>
    	</div>
    	
<!--    	주관식-->
        <div class="popCon MR20" id="answerTbl2">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="8%" />
                        <col width="%" />	
                    </colgroup>
                    <tbody>
                        <tr>
                            <th colspan="3" class="txtC">보기</th>                            
                        </tr>
                        
                        
<c:forEach begin="1" end="1" var="i">
		<c:set var="v_seltext"></c:set>
		
		<c:if test="${view.examtype eq '2'}">
			<c:forEach items="${list}" var="result" varStatus="status">
				<c:if test="${result.selnum eq i}">
					<c:set var="v_seltext">${result.seltext}</c:set>
				</c:if>
			</c:forEach>
		</c:if>	
                        <tr>
                            <th scope="col" class="bold txtC">${i}번</th>
                            <td>
                            	<input type="text" name="p_seltext2_${i}" style="width:82%" value="${v_seltext}"/>
                            	&nbsp;<a href="javascript:doQuestImage('2',${i})" class="btn01"><span>이미지 등록</span></a>
                            </td>
                        </tr>
</c:forEach>
                    </tbody>
                </table>
    	</div>
        
        
        
<!--      OX-->
          <div class="popCon MR20" id="answerTbl3">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="8%" />
                        <col width="80%" />	
                        <col width="12%" />				
                    </colgroup>
                    <tbody>
                        <tr>
                            <th colspan="3" class="txtC">보기</th>                            
                        </tr>
                        
                        
<c:forEach begin="1" end="2" var="i">	
		<c:set var="v_seltext"></c:set>
		<c:set var="v_isanswer"></c:set>
		
		<c:if test="${view.examtype eq '3'}">
			<c:forEach items="${list}" var="result" varStatus="status">
				<c:if test="${result.selnum eq i}">
					<c:set var="v_seltext">${result.seltext}</c:set>
					<c:set var="v_isanswer">${result.isanswer eq 'Y' ? 'checked' : ''}</c:set>
				</c:if>
			</c:forEach>
		</c:if>
                        <tr>
                            <th scope="col" class="bold txtC">${i}번</th>
                            <td>
                            	<input type="text" name="p_seltext3_${i}" style="width:82%"  value="${v_seltext}"/>
                            	&nbsp;<a href="javascript:doQuestImage('3',${i})" class="btn01"><span>이미지 등록</span></a>
                            </td>
                            <td><input type="checkbox" name="p_isanswer3_${i}" value="Y" class="vrM" ${v_isanswer}/> 정답여부</td>
                        </tr>
</c:forEach>
                    </tbody>
                </table>
    	</div>        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
		<c:if test="${empty p_examnum}">
			<li><a href="javascript:ActionPage('insert');" class="pop_btn01"><span>등록</span></a></li>
		</c:if>	
		<c:if test="${not empty p_examnum}">
			<li><a href="javascript:ActionPage('update');" class="pop_btn01"><span>수정</span></a></li> 
			<li><a href="#" class="pop_btn01" onclick="examPriview()"><span>미리보기</span></a></li>
            <li><a href="javascript:ActionPage('delete');" class="pop_btn01"><span>삭제</span></a></li>
        </c:if>
             
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


//문제입력여부체크 
function chkData() {
  if (blankCheck(thisForm.p_examtext.value)) {
    thisForm.p_examtext.focus();
    alert('문제를 입력하십시요.');
    return false;
  }
  
  var v_examtype = thisForm.p_examtype.value;

  
  var v_selcount = 0;
  var v_seltextcnt = 0;
  var v_test      = '';
  var v_isanswer = false;
  var i=1;

  var v_answercnt = 0;		

  var Answer = 0; 	//답안개수
  
  if(v_examtype == '1') //객관식
  {
	  Answer = 5;
  }
  else if(v_examtype == '3')	//ox
  {
	  Answer = 2;
  }
  
  
  for (k=1; k<=Answer; k++) {
    v_isanswer = eval('thisForm.p_isanswer' + v_examtype + '_' + k);
    
    if (v_isanswer.checked) {
      v_test = eval('thisForm.p_seltext' + v_examtype + '_' + k + '.value');
      if (blankCheck(v_test) && (v_examtype=='1'||v_examtype=='3')) {
        thisForm.p_seltext1_1.focus();
        alert('정답을 선택한 보기가 없습니다.');
        return false;
      }
      v_answercnt++;
    }
  }


  if (v_examtype == '1') {
	    if (v_answercnt == 0) {
	      alert('정답여부를 선택해 주십시요.');
	      return false;
	    } 
	  }

  if (v_examtype == '3') {
	    if (v_answercnt == 0) {
	      alert('정답여부를 선택해 주십시요.');
	      return false;
	    } else if (v_answercnt > 1) {
	      alert('정답여부를 1개만 선택해 주십시요.');
	      return false;
	    }
	}
  
	/* if (isNumberCheck(thisForm.p_usecnt.value, '사용횟수') == false) {
		thisForm.p_usecnt.focus();   	
	   	return;
	} */

  return true;
}


//문제 추가 등록
function ActionPage(p_process) {
    if(!chkData()) {
      return;
    }

    var msg = "";
    if(p_process == 'insert') msg = "등록하시겠습니까?";
    if(p_process == 'update') msg = "수정하시겠습니까?";
    if(p_process == 'delete') msg = "삭제하시겠습니까?";

	if(confirm(msg))
	{
	    thisForm.action = "/adm/exm/examBankAction.do";
		thisForm.p_process.value = p_process;
		thisForm.target = "_self";
		thisForm.submit();
	}
}


function display()
{
	var v_examtype = parseInt(thisForm.p_examtype.value);

	for(var i=1; i<=3; i++)
	{
		if(v_examtype == i)
			document.all("answerTbl" + i).style.display = "block";
		else
			document.all("answerTbl" + i).style.display = "none";
	}
}

function doQuestImage(imgNo,imgSelNo){
	
	thisForm.imgNo.value=imgNo;
	thisForm.imgSelNo.value=imgSelNo;
	
    var win = window.open("", "imagePopup", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,copyhistory=no, width = 600, height = 300, top=0, left=0");
	thisForm.target = "imagePopup";
	thisForm.action = "/adm/exm/examImageUploadPopup.do";
	thisForm.submit();
	win.focus();
}

function doSetImageTag(path, w, h, imgNo, imgSelNo){
	
	if(imgNo == 0){
		thisForm.p_examtext.value = thisForm.p_examtext.value + "<br/><img src='"+path+"' style='width:"+w+"px; hegiht:"+h+"px;'  />";	
	}
	if(imgNo == 1){
		var o_selText = eval('thisForm.p_seltext1_'+imgSelNo);	
		var selText = o_selText.value + "<br/><img src='"+path+"' style='width:"+w+"px; hegiht:"+h+"px;'  />";
		o_selText.value = selText;	
	}
	if(imgNo == 2){
		var o_selText = eval('thisForm.p_seltext2_'+imgSelNo);	
		var selText = o_selText.value + "<br/><img src='"+path+"' style='width:"+w+"px; hegiht:"+h+"px;'  />";
		o_selText.value = selText;	
	}
	if(imgNo == 3){
		var o_selText = eval('thisForm.p_seltext3_'+imgSelNo);	
		var selText = o_selText.value + "<br/><img src='"+path+"' style='width:"+w+"px; hegiht:"+h+"px;'  />";
		o_selText.value = selText;	
	}
	if(imgNo == 4){
		thisForm.p_exptext.value = thisForm.p_exptext.value + "<br/><img src='"+path+"' style='width:"+w+"px; hegiht:"+h+"px;'  />";	
	}
	
}

if(window.addEventListener) {
    window.addEventListener("load", display, false);
} else if(window.attachEvent) {
    window.attachEvent("onload", display);
}



function examPriview(){
	
	farwindow = window.open("", "examPriviewPopUpWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 667, top=0, left=0");
	thisForm.action = "/adm/exm/examBankPaperExamView.do";
	thisForm.target = "examPriviewPopUpWindow";
	thisForm.submit();
	farwindow.window.focus();
}

</script>