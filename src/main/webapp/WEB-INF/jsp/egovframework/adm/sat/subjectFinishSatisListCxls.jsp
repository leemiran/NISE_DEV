<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ziaan.research.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<% 

String realFileName = "전체설문결과분석_" + session.getAttribute("ses_search_subjinfo"); 
//response.reset();
response.setHeader("Pragma", "no-cache;");
response.setHeader("Expires", "-1;");
response.setHeader("Content-Transfer-Encoding", "binary;");
response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");

System.out.println("realFileName["+realFileName+"]");


	DecimalFormat  df = new DecimalFormat("0.0");

	int     v_replycount  = 0;	
	if(request.getAttribute("p_replycount") != null){
		v_replycount  = Integer.parseInt(request.getAttribute("p_replycount") + "");
	}
	int     v_studentcount= 0;
		if(request.getAttribute("p_replycount") != null){
			v_studentcount= Integer.parseInt(request.getAttribute("p_studentcount") + "");
		}
	
	double  v_replyrate   = 0;
	if (v_studentcount != 0) {
	    v_replyrate = (double)Math.round((double)v_replycount/v_studentcount*100*100)/100;
	}

ArrayList list = (ArrayList)request.getAttribute("SulmunResultList");
%>

<html>
<head>
<title>과정별입금내역</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<style>
.xl26
	{mso-style-parent:style0;
	color:black;
	font-weight:700;
	text-align:center;
	border:.5pt solid black;
	background:#C2C2C2;
	mso-pattern:auto none;
	white-space:normal;}
.graph { /* 그래프 이미지 */
        height: 20px;
	    border-style: none;
	    background-color: orange;
	    background-repeat: repeat-x;
         } 
.tbcenter{
	text-align:center;
} 
.tbtitlecenter{
	text-align:center;background-color: #f5f5f5;
}         	
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">

<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0"  width="100%" border=1>
                <caption>전체설문결과분석</caption>
                <colgroup>
                <col width="15%" />
                <col width="*" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th class="tbtitlecenter">과정명</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="15">${view.subjnm} - ${view.subjseq}기</td>
                </tr>
                <tr class="title">
                    <th class="tbtitlecenter">연수기간</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="15"><c:out value="${fn2:getFormatDate(view.edustart, 'yyyy년 MM월 dd일')}"/>&nbsp;&nbsp;&nbsp;~&nbsp;&nbsp;&nbsp;<c:out value="${fn2:getFormatDate(view.eduend, 'MM월 dd일')}"/></td>
                </tr>
                <tr class="title">
                    <th class="tbtitlecenter">이수시간 및 학점</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="15">${view.edutimes}시간 (${view.point}학점)</td>
                </tr>
                <tr class="title">
                    <th class="tbtitlecenter">수강인원</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="15">${view.studentTotalCnt} 명</td>
                </tr>
                <tr class="title">
                    <th class="tbtitlecenter">이수인원</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="15"><c:out value="${view.isgraduatedY}"/> 명</td>
                </tr>
                <tr class="title">
                    <th class="tbtitlecenter">미이수인원</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="15"><c:out value="${view.isgraduatedN}"/> 명</td>
                </tr>
                 <tr class="title">
                    <th class="tbtitlecenter">총응답자수</th>
                    <td style="text-align:left; padding-left: 20px;" colspan="15"><%=v_replycount%> 명</td>
                </tr>
                 <tr class="title">
                    <th class="tbtitlecenter">설문응시율</th>                    
                    <td  style="text-align:left; padding-left: 20px; mso-number-format:\@;" colspan="15"	><%=df.format(v_replyrate)%>%</td>
                </tr>
            </tbody>
        </table>
    </div>
   
   <!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%" border=1>
                <caption></caption>
                <colgroup>
		      <col width="40px"/>		      
		      <col width="10%"/>
		      <col width="20%"/>
		      <col />
		      <col />
		      <col />		      
		      <col />
		      <col />
		      <col />
		      <col />
		      <col />		      
		      <col />
		      <col />
		      <col />
		      <col />
		      <col />
	        </colgroup>
	        <thead>
		      <tr class="tbtitlecenter">
		        <th rowspan="2">순번</th>
		        <th rowspan="2">평가영역</th>
		        <th rowspan="2">평가문항</th>
		        <th colspan="2">매우 불만족(1점)</th>		        
		        <th colspan="2">불만족(2점)</th>		        
		        <th colspan="2">보통(3점)</th>		        
		        <th colspan="2">만족(4점)</th>		       
		        <th colspan="2">매우만족(5점)</th>		       
		        <th rowspan="2">응답인원(명)</th>
		        <th colspan="2">평점(점)</th>		   
	          </tr>
	          <tr class="tbtitlecenter">		       
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>
		        <th >빈도(명)</th>
		        <th >백분율(%)</th>		        
		        <th >평점(점)</th>
		        <th >백분율(%)</th>
	          </tr>
	        </thead>
<%  
System.out.print("list ; "+list);
    SulmunQuestionExampleData data    = null;
    SulmunExampleData         subdata = null;
	int k = 0;
	int l = 0;
	int questNumber = 0;
	int sulCount = 0;
	double sulAvg = 0; 
        double sulAvgPer = 0;
	
	int vcnt = 0;
	
	if(list != null){
    for (int i=0; i < list.size(); i++) {
        data = (SulmunQuestionExampleData)list.get(i);
           if (data.getSultype().equals(SulmunSubjBean.SUBJECT_QUESTION) & false) {	//단답형
        	   questNumber++;
%>	        
            <tbody>
		      <tr>
		        <td scope="row">문제<%=questNumber%></td>
		        <td scope="row"><%=data.getDistcodenm()%></td>
		        <td colspan="14" scope="row" class="left"><%=data.getSultext()%></td>
	          </tr>
		    
<%        int m = 0;   
				//out.println(data.getSubjectAnswer().size());
				//out.println("/"+v_replycount);
			if(v_replycount > data.getSubjectAnswer().size())	v_replycount = data.getSubjectAnswer().size();
			for (int j=0; j < data.getSubjectAnswer().size(); j++) {
				if(((data.getSubjectAnswer().size() / v_replycount)*m + (k+1)) == (j+1)) {
					if(m < v_replycount) m++;
%>		    
		      <tr>
		        <td class="num"></td>
		        <td class="num"></td>
		        <td class="left" colspan="14"><%=(String)data.getSubjectAnswer().get(j)%></td>
	          </tr>
	        </tbody>
<%           }   
		   }
           k++;	
       } else if (data.getSultype().equals(SulmunSubjBean.FSCALE_QUESTION)) {			//객관식5
    	   	questNumber++;
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
		
		sulCount++;
		sulAvg += v_point;
		
		sulAvgPer += (v_point/5)*100;
		
%>
		    <tbody>
		      <tr class="tbcenter">
		        <td >문제<%=questNumber%></td>
		        <td ><%=data.getDistcodenm()%></td>
		        <td  class="left"><%=data.getSultext()%></td>
	        
<%
		 int cnt = 0;
		   for (int j=1; j <= data.size(); j++) {
                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) {
                cnt 	+= subdata.getReplycnt();
                %>	        		       
		        <td class="num"><%=subdata.getReplycnt()%></td>
		        <td class="num"><%=df.format(subdata.getReplyrate())%></td>
<%           }  
             vcnt =j;
            }	
%>		 <td ><%=cnt%></td>
			<td ><%if(v_point >=0){%><%=df.format(v_point)%><%}%></td>
			<td ><%if(v_point >=0){%><%=df.format((v_point/5)*100)%><%}%></td>
	          </tr>
	        </tbody>
<%
       } 
			
    } 
    }
%>
	            
	       <tr class="tbcenter">
	      	<td colspan="<%=(vcnt*2)+4 %>">계</td>
	      	<td><%if(sulAvg >=0){%><%=df.format((sulAvg/questNumber))%><%}%></td>
	      	<td><%if(sulAvg >=0){%><%=df.format((sulAvgPer/questNumber))%><%}%></td>
	      </tr>      
	      </table>
		</div>
		<!-- list table-->
<br/>
		<!-- list table-->
		<%-- <div class="sub_tit">
			<h4 id="sulScoreTotalH4">교육후기</h4>	
		</div>
		
		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%"  border=1>
                <caption></caption>
                <colgroup>
		      <col width="10%"/>		      
		      <col width="10%"/>
		      <col width=""/>
	        </colgroup>
	        <thead>
		      <tr>
		        <th scope="row">아이디</th>
		        <th scope="row">이름</th>
		        <th scope="row" class="left" colspan="14">교육후기</th>
	          </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${hukiList}" var="result" varStatus="status">	
	          <tr>
	          	<td>${result.userid }</td>
	          	<td>${result.name }</td>
	          	<td style="text-align:left" colspan="14">${result.comments }</td>
	          </tr>
	        </c:forEach>
	        </tbody>
	      </table>
	    </div> --%>

</body>
</html>