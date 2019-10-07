<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "com.ziaan.exam.*" %>
<%@ page import = "egovframework.rte.psl.dataaccess.util.EgovMap" %>
<%!
public static String ReplaceBR(String instr)
{
	String str = "";
	int i;
	
	if(instr == null) return "";
	
	for(i = 0; i < instr.length(); i++)
	{
		if(instr.charAt(i) == '\n')
		{
			str += "<br/>";
		}
		else
		{
			str += instr.charAt(i);
		}
	}
	return str;
} 

%> 
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>


<%
        ArrayList    blist = (ArrayList)request.getAttribute("paperList");

        ArrayList    blist2 = (ArrayList)request.getAttribute("examList");
		Vector 		v_answer = (Vector)blist2.get(0);
		//out.println(v_answer.size());
		Vector 		v_corrected = (Vector)blist2.get(1);

	    String  	v_urldir        = "/dp/exam/";
%>


<style>
table.ex1 {width:100%; margin:0 auto; text-align:left; border-collapse:collapse}
 .ex1 th, .ex1 td {padding:5px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>

<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
    <input type="hidden" name="p_subj"         value="${p_subj}">
    <input type="hidden" name="p_gyear"        value="${p_gyear}">
    <input type="hidden" name="p_year"        value="${p_gyear}">
    <input type="hidden" name="p_subjseq"      value="${p_subjseq}">
    <input type="hidden" name="p_lesson"       value="${p_lesson}">
    <input type="hidden" name="p_examtype"     value="${p_examtype}">
    <input type="hidden" name="p_papernum"     value="${p_papernum}">
    <input type="hidden" name="p_userid"       value="${p_userid}">
    <input type="hidden" name="examtabletemp"       value="${examtabletemp}">
    <input type="hidden" name="p_process"      value="">   
    <input type="hidden" name="p_ended"        value="">   
    <input type="hidden" name="p_change1"       value=""><!--객관식-->      
    <input type="hidden" name="p_change2"       value=""><!--주관식--> 
	<input type="hidden" 	 name="p_exam" 		id="p_exam" 	value="${userExamView.exam}"/>
	<input type="hidden" 	 name="answer" 		id="answer" 	value=""/>
	<input type="hidden" name="p_exampoint" 	id="p_exampoint" 	value="${empty userExamView.exampoint ? 100/userExamView.examcnt : userExamView.exampoint}"/>
	<input type="hidden" 	 name="examBankchangeYn" 		id="examBankchangeYn" 	value="${userExamView.examBankchangeYn}"/>
	<input type="hidden" 	 name="p_exam_subj" 		id="p_exam_subj" 	value="${userExamView.examsubj}"/>
	<input type="hidden" 	 name="p_ended" 		id="p_ended" 	value="${userExamView.ended}"/>
	
	
	
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>평가결과</h2>
         </div>
		<!-- contents -->
        
        
<div class="searchWrap txtL ML10 MR10 MT10">
			<ul class="datewrap">
				<li><strong class="shTit">과정명 : ${userExamView.subjnm} / ${userExamView.year} </strong>
	 			 
				</li>
				<li class="floatL ML20"><strong class="shTit">기수 : ${fn2:toNumber(userExamView.subjseq)}기</strong></li>
                <li class="floatL ML20"><strong class="shTit">평가타입 : ${userExamView.examtypenm}</strong></li>
            </ul>
			<div class="shLine"></div>
			<div class="floatL">
				<strong class="shTit">응시기간 : ${fn2:getFormatDate(userExamView.started, 'yyyy.MM.dd HH:mm:ss')} ~ ${fn2:getFormatDate(userExamView.ended, 'yyyy.MM.dd HH:mm:ss')}</strong>	
			</div>			
		</div>
        
       
		
<div class="tbList ML10 MR10 MT10">
                <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                        <col width="%"/>
                        <col width="%"/>
                        <col width="%"/>
                        <col width="%"/>
                        <col width="%"/>
</colgroup>
                    <thead>
                        <tr>
                            <th scope="row">문제수</th>
                            <th scope="row">정답수<ss/th>
                            <th scope="row">득점</th>
                            <th scope="row">대상자/응시자</th>
                            <th scope="row">전체평균</th>                                                                                  
                      </tr>
                    </thead>
                    <tbody>
                        <tr>
                        	<td>${view.examcnt}</td>
                            <td>${userExamView.answercnt}</td> 
                            <td>${userExamView.score}</td>
                            <td>${p_personcnt}/${view.usercnt}</td> 
                            <td><span class="point"><strong>${view.avgscore}</strong></span></td>                                                 
                      </tr>                        
                    </tbody>
                </table>
        </div>
         
        <!-- button -->
		<ul class="btnCen">
			<c:if test="${not empty userExamView.ended}">
				<li><a href="javascript:fnReRating()" class="pop_btn01"><span>저 장</span></a></li>
			</c:if>			  
			<c:if test="${empty userExamView.ended}">
				<li><a href="javascript:fnExamresultEnded()" class="pop_btn01"><span>제 출</span></a></li>
			</c:if>
            <li><a href="javascript:self.close()" class="pop_btn01"><span>닫 기</span></a></li>            
		</ul>
		<!-- // button -->
		
        <!-- list table-->
		<div class="tbList ML10 MR10 MT10">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="5%"/>		      
		      <col width="%"/>
		      <col width="7%"/>
		      <col width="7%"/>
		      <col width="7%"/>
	        </colgroup>
<% 
	String examCheck = "";
	int valueCnt = -1;
	for (int i=0; i < blist.size(); i++) {
		EgovMap dbox = (EgovMap)blist.get(i);

		if(!examCheck.equals(dbox.get("examnum")+""))
		{
			valueCnt++;
			
			if(i > 0)
			{
%>
				<tbody>
                  <tr>
                    <td class="bg" >해설</td>
                    <td colspan="4" class="left">
                    <% 
                    String txt = (dbox.get("exptext") != null ? dbox.get("exptext") : "") + ""; 
                    if(txt != null && !txt.equals("")) out.println(ReplaceBR(txt));
                    %>
                    </td>
                  </tr>    
                </tbody>  
<% 
			}
%>
                <thead>
                  <tr>
                        <th scope="row">문제<%=String.valueOf(valueCnt + 1)%></th>
                        <th scope="row" class="left">
                            <span class="point"><strong><%if(((String)v_corrected.get(valueCnt)).equals("1")){%>(O)<%}else{%>(X)<%}%></strong></span>
                           <%//=dbox.get("examtext")%>
                           <% 
		                   	String txt = (dbox.get("examtext") != null ? dbox.get("examtext") : "") + ""; 
		                    if(txt != null && !txt.equals("")) 
		                    {
		                    	
		                    	if(txt.indexOf("보기>") > -1)
		                    	{
		                    		txt = ReplaceBR(txt);
		                    		txt = txt.replaceAll("보기>", "<table class='ex1'><tr><th scope='col'>보기</th><td>");
		                    		txt += "</td></tr></table>";
		                    	}
		                    	
		                    	out.println(ReplaceBR(txt));
		                    }
		                   %>
                    
                        </th>
                        <th scope="row" >난이도</th>
                        <th scope="row">득점</th>
                        <th scope="row">재채점</th>
                  </tr>
                </thead>
<% 
			examCheck = dbox.get("examnum")+"";
		}
%>
		
<%      if (dbox.get("examtype").equals(ExamBean.OBJECT_QUESTION) || dbox.get("examtype").equals(ExamBean.OX_QUESTION)) {   // 단일선택 || ox선택 %>
                <tbody>
                  <tr>
                  
                  <%if(dbox.get("isanswer") != null && dbox.get("isanswer").equals("Y")){%>
                        <td class="num check"><%= dbox.get("selnum") %></td>
                  <%}else{%>
                  		<td class="num"><%= dbox.get("selnum") %></td>
                  <%} %>
                        <td class="left">
                  <%
                  	String selnumAnswer = "";
                  
					if(valueCnt < v_answer.size())
					{
						selnumAnswer = v_answer.get(valueCnt)+"";
					}
					
					//out.print(valueCnt + "/" + v_answer.size());
					
					
					if((dbox.get("selnum")+"").equals(selnumAnswer)) {
				  %>      
                  <span class="point"><strong>
                  		<%= dbox.get("seltext") %><%= dbox.get("isselimage").equals("Y") ? "<img src='" + v_urldir + dbox.get("saveselimage") + "'>" : "" %>
                  </strong></span>
                  <img src="/images/adm/ico/ico_my.gif" alt="본인답" />
                  <%}else{%>
				  <%= dbox.get("seltext") %><%= dbox.get("isselimage").equals("Y") ? "<img src='" + v_urldir + dbox.get("saveselimage") + "'>" : "" %>
				  <%}%>
                        </td>
                        <td class="num bg"><%=dbox.get("levelsnm")%></td>
                        <td class="num bg">
                        <%if(((String)v_corrected.get(valueCnt)).equals("1")){%>
							${userExamView.exampoint}점
						<%}else{%>
							0점
						<%}%>
                        </td>
                        <td class="num bg"><input type="radio" name='answer_<%=valueCnt%>' value='<%=dbox.get("selnum")%>' <% if((dbox.get("selnum")+"").equals(selnumAnswer)) out.print("checked"); %>></td>
                  </tr>
                 </tbody>
<% 
	 }else if (dbox.get("examtype").equals(ExamBean.SUBJECT_QUESTION)) {   //  주관식
%>
				<tbody>
                  <tr>
                  		<td class="num">인정<br/>답안</td>
                        <td class="left">
                        	<%=dbox.get("seltext")%>
                        </td>
                        <td class="num bg"><%=dbox.get("levelsnm")%></td>
                        <td class="num bg">
                        <%if(((String)v_corrected.get(valueCnt)).equals("1")){%>
							${userExamView.exampoint}점
						<%}else{%>
							0점
						<%}%>
                        </td>
                        <td class="num bg">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="bg" >본인답</td>
                    <td colspan="4" class="left">
                    <input type="text" name='answer_<%=valueCnt%>' size='50' value='<%=(String)v_answer.get(valueCnt)%>'>
                    </td>
                  </tr>
                 </tbody>
<% 
	 }else if (dbox.get("examtype").equals(ExamBean.MULTI_QUESTION)) {   //  다답식
%>
				<tbody>
                  <tr>
                  		<td class="num"></td>
                        <td class="left"></td>
                        <td class="num bg"></td>
                        <td class="num bg"></td>
                        <td class="num bg">&nbsp;</td>
                  </tr>
                 </tbody>
<% 
	 }
%>
                
<%
	}
%>               
                
                
        
	      </table>
		</div>
		<!-- list table-->
        
        <div class="m_searchWrap ML10 MR10 MT10">
            	<span class="shTit">계 : <span class="num1">${userExamView.score}</span> </span>                            
		</div>
        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<c:if test="${not empty userExamView.ended}">
				<li><a href="javascript:fnReRating()" class="pop_btn01"><span>저 장</span></a></li>
			</c:if>		
			<c:if test="${empty userExamView.ended}">
				<li><a href="javascript:fnExamresultEnded()" class="pop_btn01"><span>제 출</span></a></li>
			</c:if>
            <li><a href="javascript:self.close()" class="pop_btn01"><span>닫 기</span></a></li>            
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->



    <input type="hidden" name="p_examcnt" value="<%=valueCnt+1%>" readonly>
	

</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>



<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

	// 재채점 등록
	function fnReRating(){
	  if(confirm('재채점 하시겠습니까?')){
	  var exam ="";
	  var a_name ="";
	  var examnum = "";
	  var i=0;
	  var b_name="";
	  var c_name="";
	  var c_value="";
	  var b_type=""; 
	  var result="";
	  var result2="";
	  var temp=""; 
	  var textarr ="";
	  var answercnt=0;
	  var replycnt =0;

	  var v_current = new Date();
	  var v_ended = "";
	  var v_temp = "";

	  v_ended += String(v_current.getFullYear());

	  v_temp = String((v_current.getMonth()+1));
	  if(v_temp.length == 1){
	      v_ended += "0" + v_temp;       
	  }else{
		  v_ended += v_temp;
	  }
	  v_temp = String(v_current.getDate());
	  if(v_temp.length == 1){
	      v_ended += "0" + v_temp;       
	  }else{
		  v_ended += v_temp;
	  }
	  v_temp = String(v_current.getHours());
	  if(v_temp.length == 1){
	      v_ended += "0" + v_temp;       
	  }else{
		  v_ended += v_temp;
	  }
	  v_temp = String(v_current.getMinutes());
	  if(v_temp.length == 1){
	      v_ended += "0" + v_temp;       
	  }else{
		  v_ended += v_temp;
	  }
	  v_temp = String(v_current.getSeconds());
	  if(v_temp.length == 1){
	      v_ended += "0" + v_temp;       
	  }else{
		  v_ended += v_temp;
	  }
	  

	  
	  var v_answer = "";
	  for(var i=0; i<thisForm.p_examcnt.value; i++)
	  {
		  	var chk = false;
		  	var answernum = eval("document.<c:out value="${gsPopForm}"/>.answer_" + i);
			if(answernum.length)
			{
				for(var j=0; j<answernum.length; j++)
				{
					if(answernum[j].checked)
					{
						v_answer += answernum[j].value+",";						
						chk = true;
						break;
					}
				}
			}
			else
			{
				v_answer += answernum.value+",";				
				chk = true;
			}

			if(chk != true)
			{
				alert("등록되지 않은 답안이 있습니다. 모든 답안을 넣어주세요");
				
				return;

				break;
			}
	  }
	//alert(v_answer + "/" + v_ended);

	//return;

	
	  thisForm.p_ended.value  = v_ended;
	  thisForm.answer.value  = v_answer;
	  thisForm.action = "/adm/exm/examUserReRatingAction.do";
	  thisForm.target = "_self";
	  thisForm.submit();
	  
	  }
	}
	
	// 재출
	function fnExamresultEnded(){
		if(confirm('제출 하시겠습니까?')){
			thisForm.action = "/adm/exm/examUserExamresultEnded.do";
			thisForm.target = "_self";
			thisForm.submit();
	  	}
	}

	
</script>