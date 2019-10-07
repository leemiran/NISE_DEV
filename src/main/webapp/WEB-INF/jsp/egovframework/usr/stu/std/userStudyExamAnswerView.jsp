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
<body onbeforeunload="return false" onContextmenu="return false">
<%
        ArrayList    blist = (ArrayList)request.getAttribute("paperList");

        ArrayList    blist2 = (ArrayList)request.getAttribute("examList");
		Vector 		v_answer = (Vector)blist2.get(0);
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
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onSubmit="return false;">
    <input type="hidden" name="p_subj"         value="${p_subj}">
    <input type="hidden" name="p_gyear"        value="${p_gyear}">
    <input type="hidden" name="p_subjseq"      value="${p_subjseq}">
    <input type="hidden" name="p_lesson"       value="${p_lesson}">
    <input type="hidden" name="p_examtype"     value="${p_examtype}">
    <input type="hidden" name="p_papernum"     value="${p_papernum}">
    <input type="hidden" name="p_userid"       value="${p_userid}">
    <input type="hidden" name="p_exam"         value="">
    <input type="hidden" name="p_answer"       value="">    
    <input type="hidden" name="p_process"      value="">   
    <input type="hidden" name="p_ended"        value="">   
    <input type="hidden" name="p_change1"       value=""><!--객관식-->      
    <input type="hidden" name="p_change2"       value=""><!--주관식--> 


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
                <table summary="문제수, 정답수, 득점, 응시자로 구분" cellspacing="0" width="100%">
                <caption>평가결과</caption>
                <colgroup>
                        <col width="%"/>
                        <col width="%"/>
                        <col width="%"/>
                        <col width="%"/>
					</colgroup>
                    <thead>
                        <tr>
                            <th scope="row">문제수</th>
                            <th scope="row">정답수</th>
                            <th scope="row">득점</th>
                            <th scope="row">응시자</th>
                      </tr>
                    </thead>
                    <tbody>
                        <tr>
                        	<td>${view.examcnt}</td>
                            <td>${userExamView.answercnt}</td> 
                            <td>${userExamView.score}</td>
                            <td>${view.usercnt}</td> 
                      </tr>                        
                    </tbody>
                </table>
        </div>
        
        
        
        
        
<!-- 20170321 start-->        
<%-- <c:if test="${examtabletemp eq 'temp'}"> --%>
        
        <!-- list table-->
		<div class="tbList ML10 MR10 MT10">
		  <table summary="문제, 인정여부, 점수로 구분" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="5%"/>		      
		      <col width="%"/>
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
<!--				<tbody>-->
<!--                  <tr>-->
<!--                    <td class="bg" >해설</td>-->
<!--                    <td colspan="2" class="left"><%=(dbox.get("exptext") != null ? dbox.get("exptext") : "")%></td>-->
<!--                  </tr>    -->
<!--                </tbody>  -->
<% 
			}
%>
                <thead>
                  <tr style="<%if(((String)v_corrected.get(valueCnt)).equals("1")){%>display:none<% } %>">
                        <th scope="row"><span class="point">문제<%=String.valueOf(valueCnt + 1)%></span></th>
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
                        <th scope="row">
                        <%if(((String)v_corrected.get(valueCnt)).equals("1")){%>
							${userExamView.exampoint}점
						<%}else{%>
							0점
						<%}%>
                        </th>
                  </tr>
                </thead>
<% 
			examCheck = dbox.get("examnum")+"";
		}
%>
		
<%      if (dbox.get("examtype").equals(ExamBean.OBJECT_QUESTION) || dbox.get("examtype").equals(ExamBean.OX_QUESTION)) {   // 단일선택 || ox선택 %>
                <tbody>
                  <tr style="<%if(((String)v_corrected.get(valueCnt)).equals("1")){%>display:none<% } %>">
                  
                  <%if(dbox.get("isanswer") != null && dbox.get("isanswer").equals("Y")){%>
                        <td class="num"><%= dbox.get("selnum") %></td>
                  <%}else{%>
                  		<td class="num"><%= dbox.get("selnum") %></td>
                  <%} %>
                        <td class="left" colspan="2">
                  <%
				
					if((dbox.get("selnum")+"").equals(v_answer.get(valueCnt)+"") && !((String)v_corrected.get(valueCnt)).equals("1")) {
				  %>      
                  <span class="point"><strong>
                  		<%= dbox.get("seltext") %><%= dbox.get("isselimage").equals("Y") ? "<img src='" + v_urldir + dbox.get("saveselimage") + "'>" : "" %>
                  </strong></span>
                  <img src="/images/adm/ico/ico_my.gif" alt="본인답" />
                  <%}else{%>
				  <%= dbox.get("seltext") %><%= dbox.get("isselimage").equals("Y") ? "<img src='" + v_urldir + dbox.get("saveselimage") + "'>" : "" %>
				  <%}%>
                        </td>
                    
                  </tr>
                 </tbody>
<% 
	 }else if (dbox.get("examtype").equals(ExamBean.SUBJECT_QUESTION)) {   //  주관식
%>
				<tbody>
                  <tr>
                  		<td class="num">본인답</td>
                        <td class="left" colspan="2">
                        	<%=(String)v_answer.get(valueCnt)%>
                        </td>
                        
                  </tr>
                  <tr>
                    <td class="bg" >인정<br/>답안</td>
                    <td colspan="2" class="left">
                    <%=dbox.get("seltext")%>
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
            	<span class="shTit ML10 ">총점 : <span class="num1">${userExamView.score}점</span> </span>                            
		</div>
        
        
<%-- </c:if> --%>        
<!-- 20170321 end-->
        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
            <li><a href="javascript:self.close()" class="pop_btn01"><span>닫 기</span></a></li>            
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->



    
	

</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>



<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

	document.title="시험결과보기 : 나의강의실 : 국립특수교육원부설원격교육연수원";
</script>