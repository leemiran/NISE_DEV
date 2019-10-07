<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ziaan.research.*" %>


<% 
	String realFileName = "전체설문결과분석_" + session.getAttribute("ses_search_subjinfo"); 
	//response.reset();
	response.setHeader("Pragma", "no-cache;");
	response.setHeader("Expires", "-1;");
	response.setHeader("Content-Transfer-Encoding", "binary;");
	response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
	response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
	//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");



	DecimalFormat  df = new DecimalFormat("0.00");

	int     v_replycount  = Integer.parseInt(request.getAttribute("p_replycount") + "");
	int     v_studentcount= Integer.parseInt(request.getAttribute("p_studentcount") + "");
	
	double  v_replyrate   = 0;
	if (v_studentcount != 0) {
	    v_replyrate = (double)Math.round((double)v_replycount/v_studentcount*100*100)/100;
	}
	
	ArrayList list = (ArrayList)request.getAttribute("SulmunResultList");
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
	<tr>
		<td colspan="5" class="xl26">총응답자수 : <%=v_replycount%>명 / 전체수강자 : <%=v_studentcount%>명 / 설문응시율 : <%=df.format(v_replyrate)%>%</td>
	</tr>
<%  
    SulmunQuestionExampleData data    = null;
    SulmunExampleData         subdata = null;

	int k = 0;
	int l = 0;
    for (int i=0; i < list.size(); i++) {
        data = (SulmunQuestionExampleData)list.get(i);
        if (data.getSultype().equals(SulmunSubjBean.OBJECT_QUESTION) || data.getSultype().equals(SulmunSubjBean.MULTI_QUESTION)) { 
        	
        	double d = 0; 
			  int person = 0;
			  double v_point = 0;
  
		   for (int j=1; j <= data.size(); j++) {
	            subdata  = (SulmunExampleData)data.get(j); 
            if (subdata != null) { 

					d +=  (subdata.getReplycnt()) * subdata.getSelnum();
					person += subdata.getReplycnt();
				}
        }	
           
		v_point = d / person;	
%>        
	<tr>
		<td class="xl26">문제<%=i+1%></td>
		<td class="xl26" colspan="3"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></td>
		<td class="xl26"><%if(v_point >=0){%><%=df.format(v_point)%>점<%}%></td>
	</tr>
<%
			for (int j=1; j <= data.size(); j++) {
                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 
%>
	<tr>
		<td><%=subdata.getSelnum()%></td>
		<td><%=subdata.getSeltext()%></td>
		<td><%=subdata.getReplycnt()%>명</td>
		<td><%=subdata.getReplyrate()%>%</td>
		<td>&nbsp;</td>
	</tr>
<%              }
            }
       } else if (data.getSultype().equals(SulmunSubjBean.SUBJECT_QUESTION)) {
          
%>
	<tr>
		<td class="xl26">문제<%=i+1%></td>
		<td class="xl26" colspan="4"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></td>
	</tr>
<%        int m = 0;   
			for (int j=0; j < data.getSubjectAnswer().size(); j++) {
				if(((data.getSubjectAnswer().size() / v_replycount)*m + (k+1)) == (j+1)) {
					if(m < v_replycount) m++;
%>
	<tr>
		<td class="xl26"></td>
		<td class="xl26" colspan="4"><%=(String)data.getSubjectAnswer().get(j)%></td>
	</tr>

<%           }   
		   }
           k++;	
       } else if (data.getSultype().equals(SulmunSubjBean.COMPLEX_QUESTION)) {
          
%>
	<tr>
		<td class="xl26">문제<%=i+1%></td>
		<td class="xl26" colspan="4"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></td>
	</tr>
<%          for (int j=1; j <= data.size(); j++) {
                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { %>
	<tr>
		<td><%=subdata.getSelnum()%></td>
		<td><%=subdata.getSeltext()%></td>
		<td><%=subdata.getReplycnt()%>명</td>
		<td><%=subdata.getReplyrate()%>%</td>
		<td>&nbsp;</td>
	</tr>
<%           } 
           }	
%>
<%       int n = 0;    
			int etc = 0;
			for (int j=0; j < data.getComplexAnswer().size(); j++) {			
				if(((data.getComplexAnswer().size() / v_replycount)*n + (l+1)) == (j+1)) {   
					if(n < v_replycount) n++;
						if( !((String)data.getComplexAnswer().get(j)).equals("")) {
							etc++;
%>
	<tr>
		<td class="xl26"><%if(etc==1){%>기타<%}%></td>
		<td class="xl26" colspan="4"><%=(String)data.getComplexAnswer().get(j)%></td>
	</tr>
<%
					}
                }   
		   }
           l++;
       } else if (data.getSultype().equals(SulmunSubjBean.FSCALE_QUESTION)) {
          
	          double d = 0; 
			  int person = 0;
			  double v_point = 0;
      
		   for (int j=1; j <= data.size(); j++) {
	            subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 

					d +=  (subdata.getReplycnt()) * subdata.getSelpoint();
					person += subdata.getReplycnt();
				}
            }	
               
		v_point = d / person;	  
%>
	<tr>
		<td class="xl26">문제<%=i+1%></td>
		<td class="xl26" colspan="3"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></td>
		<td class="xl26"><%if(v_point >=0){%><%=df.format(v_point)%>점<%}%></td>
	</tr>
<%
		   for (int j=1; j <= data.size(); j++) {
                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 
%>
	<tr>
		<td><%=subdata.getSelnum()%></td>
		<td><%=subdata.getSeltext()%></td>
		<td><%=subdata.getReplycnt()%>명</td>
		<td><%=subdata.getReplyrate()%>%</td>
		<td>&nbsp;</td>
	</tr>
<%           }    
            }	
       } else if (data.getSultype().equals(SulmunSubjBean.SSCALE_QUESTION)) {
          
	          double d = 0; 
			  int person = 0;
			  double v_point = 0;
      
		   for (int j=1; j <= data.size(); j++) {
	            subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 

					d +=  (subdata.getReplycnt()) * subdata.getSelpoint();
					person += subdata.getReplycnt();
				}
            }	
               
		v_point = d / person;	  
%>
	<tr>
		<td class="xl26">문제<%=i+1%></td>
		<td class="xl26" colspan="3"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></td>
		<td class="xl26"><%if(v_point >=0){%><%=df.format(v_point)%>점<%}%></td>
	</tr>
<%

		   for (int j=1; j <= data.size(); j++) {

                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 
%>
	<tr>
		<td><%=subdata.getSelnum()%></td>
		<td><%=subdata.getSeltext()%></td>
		<td><%=subdata.getReplycnt()%>명</td>
		<td><%=subdata.getReplyrate()%>%</td>
		<td>&nbsp;</td>
	</tr>
<%           
			}    
          }	
	   } 
    } 
%>
</table>


</body>
</html>
