<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">

    <input type="hidden" name="p_process"     value="${p_process}">
    <input type="hidden" name="p_subj"  		value="${p_subj}">
    <input type="hidden" name="p_lesson"        value="${p_lesson}">
	<input type="hidden" name="p_examtype"        value="${p_examtype}">
    <input type="hidden" name="p_lessonstart"        value="${p_lessonstart}">
    <input type="hidden" name="p_lessonend"        value="${p_lessonend}">
	<input type="hidden" name="p_examtime"        value="${p_examtime}">
    <input type="hidden" name="p_totalscore"        value="100">
    <input type="hidden" name="p_level1text"        value="">
    <input type="hidden" name="p_level2text"        value="">
    <input type="hidden" name="p_level3text"        value="">
    <input type="hidden" name="p_cntlevel1"        value="">
    <input type="hidden" name="p_cntlevel2"        value="">
    <input type="hidden" name="p_cntlevel3"        value="">
	<input type="hidden" name="p_isopenanswer"        value="${p_isopenanswer}">
    <input type="hidden" name="p_isopenexp"        value="${p_isopenexp}">
    <input type="hidden" name="p_retrycnt"        value="${p_retrycnt}">
    <input type="hidden" name="p_progress"        value="${p_progress}">
    <input type="hidden" name="p_startdt"        value="${p_startdt}">
    <input type="hidden" name="p_enddt"        value="${p_enddt}">
    <input type="hidden" name="p_reloadlist" value="">
	<input type="hidden" name="p_gyear"  value="${p_gyear}">
	<input type="hidden" name="p_subjseq"  value="${p_subjseq}">
	<input type="hidden" name="p_papernum"  value="${p_papernum}">
	<input type="hidden" name="p_userid"  value="${sessionScope.userid}">



<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>평가문제지관리</h2>
         </div>
		<!-- contents -->
        
		<div>
			<p class="pop_tit">평가문제지관리 Step 2</p>			
		</div>
        
        <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="33.33%" />
                        <col width="33.33%" />
                        <col width="33.33%" />
                    </colgroup>
                    <thead>
                        <tr>
                          <th scope="col">난이도 상</th>
                          <th scope="col">난이도 중</th>
                          <th scope="col">난이도 하</th>
                          
                        </tr>					
                    </thead>
                    <tbody>
                    
                    
<c:set var="v_examcnt" value="0"/>
<c:set var="s_lesson" value="0"/>
 

<c:if test="${empty view}">
	<c:set var="s_lesson" value="${fn:length(list)}"/> 
	<c:forEach items="${list}" var="resultLevel" varStatus="i">
		<tr>
		<c:forEach items="${resultLevel}" var="resultType" varStatus="j">
			<td class="txtC"> 
			<c:forEach items="${resultType}" var="typeList" varStatus="k">
				<c:choose>
					<c:when test="${k.count % 3 == 0}">
						O/X식 
					</c:when>
					<c:when test="${k.count % 2 == 0}">
						주관식
					</c:when>
					<c:otherwise>
						객관식
					</c:otherwise>
				</c:choose>        
				
	    		( ${typeList.levelscount}개문제 / 
	    		<input type="text" name="p_cntlevelM${i.count}M${j.count}M${k.count}"  size="3" style="width:30px;" value="0" />개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM${i.count}M${j.count}M${k.count}" value="${typeList.levelscount}"/>
	    		
	    		<c:set var="v_examcnt">${v_examcnt + typeList.levelscount}</c:set>
	    	</c:forEach>
	    	</td> 
		</c:forEach> 
		</tr>
	</c:forEach>      

</c:if>
<c:if test="${not empty view}">

<%
	int s_lesson = 0;
	int s_levels = 0;
	int s_type = 0;


    ArrayList  lessonlist = (ArrayList)request.getAttribute("list");
	ArrayList levelslist = null;
	ArrayList typelist = null;
	s_lesson = lessonlist.size();

    ArrayList  lessonlistM = (ArrayList)request.getAttribute("masterList");
	ArrayList levelslistM = null;
	ArrayList typelistM = null;
	
    
    for ( int i =0; i < lessonlist.size() ; i++){

%>
        <tr> 
<%
	      levelslist = (ArrayList)lessonlist.get(i);
          s_levels = levelslist.size();

	      levelslistM = (ArrayList)lessonlistM.get(i);
		  
		  for ( int j = 0 ; j < levelslist.size() ;  j++) {
%>
			<td class="txtC"> 
<%
			  typelist = (ArrayList)levelslist.get(j);
			  s_type = typelist.size();

			  typelistM = (ArrayList)levelslistM.get(j);
			  
			  
			  for ( int k = 0; k < typelist.size() ; k++) {
				  Map dbox = (Map)typelist.get(k);	
				  
				  int examcnt = 0;
				  
				  if(typelistM.size() > 0) examcnt = Integer.parseInt(((String)typelistM.get(k)));
				  
				  if ( (k+1)%3 == 0 ) {
				  
%>
					O/X식 
<%				  
				  } else if ( (k+1)%2 == 0 ) {
%>
				       주관식
<%
                 }  else {					  
%>
				       객관식
<%
                  } 
				  
%>				  
				( <%=dbox.get("levelscount")%> 개문제 / 
	    		<input name="p_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" type="text" class="input" size="3" value="<%=examcnt%>">개출제 )<br />
	    		<input type="hidden" name="v_cntlevelM<%=i+1%>M<%=j+1%>M<%=k+1%>" value="<%=dbox.get("levelscount")%>"/>
<%
			}
%>

			</td>
<%		  
		}
%>          
        </tr>
<%
   }
%>


</c:if>                  
                    </tbody>
                </table>
      	</div>    
        <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="40%" />
                        <col width="10%" />
                        <col width="40%" />
                    </colgroup>
                    <tbody>

<c:if test="${empty view}">
                    
                        <tr>
                          <th scope="col">문제당 배점</th>
                          <td>1문제당 
                          
                          <c:set var="v_exampoint">0</c:set>
                          
                          <c:if test="${v_examcnt > 0}">
                          	<c:set var="v_exampoint">${100/v_examcnt}</c:set>
                          </c:if>
                          
                          <input name="p_exampoint" type="text" class="input" size="5" value="<fmt:formatNumber value="${v_exampoint}" type="number"/>"> 점</td>
                          <th>문제수</th>
                          <td><input name="p_examcnt" type="text" class="input" size="5" value="${v_examcnt}"> 개문제출제</td>
                        </tr>
</c:if>
<c:if test="${not empty view}">                        
                        <tr>
                          <th scope="col">문제당 배점</th>
                          <td>1문제당 
                          <input name="p_exampoint" type="text" class="input" size="5" value="<fmt:formatNumber value="${view.exampoint}" type="number"/>"> 점</td>
                          <th>문제수</th>
                          <td><input name="p_examcnt" type="text" class="input" size="5" value="${view.examcnt}"> 개문제출제</td>
                        </tr>
</c:if>
                    </tbody>
                </table>
      	</div>  
        
 		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
        	<li><a href="javascript:history.go(-1)" class="pop_btn01"><span>이 전</span></a></li>
        	<c:if test="${not empty view}">
        	
        	<c:if test="${paperCnt > 0}">
				<li><a href="javascript:alert('시험지를 이미 등록하셨거나\n응시자가 존재하는 마스터입니다.\n수정이 불가능합니다.')" class="pop_btn01"><span>저 장</span></a></li> 
			</c:if>
				
			<c:if test="${paperCnt == 0}">
				<li><a href="javascript:paperBankAction('update')" class="pop_btn01"><span>저 장</span></a></li>
			</c:if>
			
			
			</c:if>
			<c:if test="${empty view}">
				<li><a href="javascript:paperBankAction('insert')" class="pop_btn01"><span>저 장</span></a></li> 
			</c:if>
            <li><a href="javascript:window.close()" class="pop_btn01"><span>취 소</span></a></li>                        
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->

</form>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var thisForm = eval('document.<c:out value="${gsPopForm}"/>');


function paperBankAction(p_process) {
	  if (!chkData()) {
	    return;
	  }

	  

	  thisForm.p_process.value = p_process;
	  thisForm.action = '/adm/exm/examBankAction.do';
	  thisForm.target = '_self';
	  thisForm.submit();
}



function chkData( ) {
	  var v_examcnt = 0;

	  if (!number_chk_noalert(thisForm.p_exampoint.value) || 1 > thisForm.p_exampoint.value) {
	    thisForm.p_exampoint.focus();
	    alert('문제당 배점이 잘못 입력되었습니다.');
	    return false;
	  }
	  if (!number_chk_noalert(thisForm.p_examcnt.value) || 1 > thisForm.p_examcnt.value) {
	    thisForm.p_examcnt.focus();
	    alert('문제수가 잘못 입력되었습니다.');
	    return false;
	  }
	  if (thisForm.p_exampoint.value * thisForm.p_examcnt.value != 100) {
	    thisForm.p_exampoint.focus();
	    alert('총점수는 100점입니다. 문제당 배점이나 문제수를 조정해 주십시요.');
	    return false;
	  }


   	    var s_level1text = "";
		var s_level2text = "";
		var s_level3text = ""; 
		var s_cntlevel1 = 0; 
		var s_cntlevel2 = 0; 
		var s_cntlevel3 = 0; 


		<c:forEach items="${list}" var="resultLevel" varStatus="i">
			<c:forEach items="${resultLevel}" var="resultType" varStatus="j">
				<c:forEach items="${resultType}" var="typeList" varStatus="k">
				
		 if ( ${j.count} == 1 ) {
		      s_level1text += ${i.count} + "," + thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value;
			  s_cntlevel1 += parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value);

			  if (${k.count} == 2) {
			
		          s_level1text += "|";	
			  }else if (${k.count} == 3) {	
				  if(${s_lesson}-${i.index} >1) s_level1text += "/";	  
			  }else{
			      s_level1text += "|";	       
			  }
		   }
		   if ( ${j.count} == 2 ) {
		      s_level2text += ${i.count} + "," + thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value;
			  s_cntlevel2 += parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value);
			  if (${k.count} == 2) {
				  //if(${s_lesson}-${i.index} >1) s_level2text += "/";
		          s_level2text += "|";
			  }else if (${k.count} == 3) {
				  if(${s_lesson}-${i.index} >1) s_level2text += "/";		  		  
			  }else{
			       s_level2text += "|";
			  }
		   }
		   if ( ${j.count} == 3 ) {
		      s_level3text += ${i.count} + "," + thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value;
			  s_cntlevel3 += parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value);
			  if (${k.count} == 2) {
				  //if(${s_lesson}-${i.index} >1) s_level3text += "/";
				  s_level3text += "|";
			  }else if (${k.count} == 3) {
				  if(${s_lesson}-${i.index} >1) s_level3text += "/";		  
			  }else{
			       s_level3text += "|";
			  }
		   }

		if (!number_chk_noalert(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value) || parseInt(thisForm.v_cntlevelM${i.count}M${j.count}M${k.count}.value) < parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value) ) {
		    alert('난이도별 문항수가 잘못입력되었습니다.');
		    thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.focus();
		    return false;
		  }
		 v_examcnt += parseInt(thisForm.p_cntlevelM${i.count}M${j.count}M${k.count}.value,10)		
		    	</c:forEach>
			</c:forEach> 
		</c:forEach>     

		 if (v_examcnt != thisForm.p_examcnt.value) {
		    thisForm.p_cntlevelM1M1M1.focus();
		    alert('입력한 문제수는 ' + v_examcnt + '개 입니다. \n문제수에 맞게 문제 배분을 해 주세요.');
		    return false;
		  }
			thisForm.p_level1text.value = s_level1text;
			thisForm.p_level2text.value = s_level2text;
			thisForm.p_level3text.value = s_level3text;
			
			thisForm.p_cntlevel1.value = s_cntlevel1;
			thisForm.p_cntlevel2.value = s_cntlevel2;
			thisForm.p_cntlevel3.value = s_cntlevel3;

		  return true;
		}
</script>


