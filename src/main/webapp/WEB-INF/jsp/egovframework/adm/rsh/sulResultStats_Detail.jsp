<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ziaan.research.*" %>

<% 

String realFileName = "설문조사 시스템 전체설문상세결과분석_" + session.getAttribute("ses_search_subjinfo"); 
//response.reset();
response.setHeader("Pragma", "no-cache;");
response.setHeader("Expires", "-1;");
response.setHeader("Content-Transfer-Encoding", "binary;");
response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");




	DecimalFormat  df = new DecimalFormat("0.00");

	ArrayList list = (ArrayList)request.getAttribute("SulmunResultList");
	ArrayList answerList = (ArrayList)request.getAttribute("answerList");
%>

<html>
<head>
<title>설문분석</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<style>
.xl26
	{mso-style-parent:style0;
	color:white;
	font-weight:700;
	text-align:center;
	border:.5pt solid black;
	background:#C2C2C2;
	mso-pattern:auto none;
	white-space:normal;}
.graph { /* 그래프 이미지 */
        height: 14px;
	    border-style: none;
	    background-color: orange;
	    background-repeat: repeat-x;
         }  	
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">

<table cellspacing="1" cellpadding="1" border="1">
                <caption><%=session.getAttribute("ses_search_subjinfo") %></caption>
<%  
    SulmunQuestionExampleData data    = null;
    
	SulmunExampleData         subdata = null;
	
	SulmunSubjectAnswerData         ansdata = null;

	//out.println("###############" +  list.size());
    for (int i=0; i < list.size(); i++) {
    	data = (SulmunQuestionExampleData)list.get(i);
    	
        %>
		    <thead>
		      <tr>
		        <th class="xl26">문제<%=i+1%></th>
		        <th colspan="6" class="xl26"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></th>
	          </tr>
	          <tr>
		        <th class="xl26">No</th>
		        <th class="xl26">회원구분</th>
		        <th class="xl26">소속</th>
		        <th class="xl26">이름</th>
		        <th class="xl26">성별</th>
		        <th class="xl26">연령</th>
		        <th class="xl26">답안</th>
	          </tr>
	        </thead>
<%

for (int k=0; k < answerList.size(); k++) {
	ArrayList list2 = (ArrayList) answerList.get(k);
		ansdata  = (SulmunSubjectAnswerData)list2.get(i);
   		 if (ansdata != null) { %>	        
			<tbody>
			  <tr>
			    <td class=""><%=k+1%></td>
			    <td class=""><%=ansdata.getEmpnm()%></td>
			    <td class=""><%=ansdata.getPositionnm()%></td>
			    <td class=""><%=ansdata.getName()%></td>
			    <td class=""><%=ansdata.getSex()%></td>
			    <td class=""><%=ansdata.getAge()%>세</td>
<% 
if (!data.getSultype().equals(SulmunSubjBean.SUBJECT_QUESTION)) {
	subdata  = (SulmunExampleData)data.get(Integer.parseInt(ansdata.getAnstext())); 
	if (subdata != null) {
%>
				<td class=""><%=subdata.getSeltext()%></td>
<%
		}
		else
		{
%>
					<td class=""> </td>
<% 
		}
	}
	else
	{
%>
				<td class=""><%=ansdata.getAnstext() %></td>
<% 
	}
%>	
				</tr>
			</tbody>
<%            
		}
	}
}
%>
	            
	        
	      </table>
		<!-- list table-->


</body>
</html>