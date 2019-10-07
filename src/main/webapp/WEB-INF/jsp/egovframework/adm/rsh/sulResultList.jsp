<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "com.ziaan.research.*" %>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<% 
	DecimalFormat  df = new DecimalFormat("0.00");

	int     v_replycount  = Integer.parseInt(request.getAttribute("p_replycount") + "");
	int     v_studentcount= Integer.parseInt(request.getAttribute("p_studentcount") + "");
	
	double  v_replyrate   = 0;
	if (v_studentcount != 0) {
	    v_replyrate = (double)Math.round((double)v_replycount/v_studentcount*100*100)/100;
	}
	
	ArrayList list = (ArrayList)request.getAttribute("SulmunResultList");
%>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">

   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<div class="listTop">
		
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn01"><span>엑셀출력</span></a>
                </div>      
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDetailDownLoad()" class="btn01"><span>엑셀상세출력</span></a>
                </div>                 
		</div>
        
        <div class="sub_tit">
			<h4 id="sulScoreTotalH4">총응답자수 : <%=v_replycount%>명 / 전체수강자 : <%=v_studentcount%>명 / 설문응시율 : <%=df.format(v_replyrate)%>%</h4>	
		</div>
		
		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="7%"/>		      
		      <col width="49%"/>
		      <col width="7%"/>
		      <col width="7%"/>
		      <col width="30%"/>
	        </colgroup>
<%  
    SulmunQuestionExampleData data    = null;
    SulmunExampleData         subdata = null;

	int k = 0;
	int l = 0;
	
	int sulCount = 0;
	double sulAvg = 0; 
	
	//out.println("###############" +  list.size());
    for (int i=0; i < list.size(); i++) {
        data = (SulmunQuestionExampleData)list.get(i);
        
        if (data.getSultype().equals(SulmunSubjBean.OBJECT_QUESTION) || data.getSultype().equals(SulmunSubjBean.MULTI_QUESTION)) { 		//객관식
        
        	double d = 0; 
			  int person = 0;
			  double v_point = 0;
    
		   for (int j=1; j <= data.size(); j++) {
	            subdata  = (SulmunExampleData)data.get(j); 
	            
	            System.out.println("subdata ----> "+ subdata);
	            
              if (subdata != null) { 
					
            	  	System.out.println("subdata.getReplycnt() ----> "+ subdata.getReplycnt());
					System.out.println("subdata.getSelnum() ----> "+ subdata.getSelnum());
					System.out.println("subdata.getReplycnt() ----> "+ subdata.getReplycnt());
					
					d +=  (subdata.getReplycnt()) * subdata.getSelnum();
					person += subdata.getReplycnt();
				}
          }	
             
		v_point = d / person;	  
		sulCount++;
		sulAvg += v_point;
		
		
		System.out.println("SulmunSubjBean.OBJECT_QUESTION ----> "+ SulmunSubjBean.OBJECT_QUESTION);
		System.out.println("SulmunSubjBean.MULTI_QUESTION ----> "+ SulmunSubjBean.MULTI_QUESTION);
		System.out.println("data.getSultype() ----> "+ data.getSultype());
		
		System.out.println("v_point ----> "+ v_point);
		System.out.println("sulCount ----> "+ sulCount);
		System.out.println("sulAvg ----> "+ sulAvg);
		
        %>
		    <thead>
		      <tr>
		        <th scope="row">문제<%=i+1%></th>
		        <th colspan="3" scope="row" class="left"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></th>
		        <th scope="row"><%if(v_point >=0){%><%=df.format(v_point)%>점<%}%></th>
	          </tr>
	        </thead>
<%          for (int j=1; j <= data.size(); j++) {
                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { %>	        
		    <tbody>
              <tr>
		        <td class="num"><%=subdata.getSelnum()%></td>
		        <td class="left"><%=subdata.getSeltext()%></td>
		        <td class="num"><%=subdata.getReplycnt()%>명</td>
		        <td class="num"><%=subdata.getReplyrate()%>%</td>
		        <td class="left"><div class="graph_1" style="width:<%=(int)subdata.getReplyrate()%>px;"></div></td>
	          </tr>
	        </tbody>
<%              }
            }
       } else if (data.getSultype().equals(SulmunSubjBean.SUBJECT_QUESTION)) {
    	   System.out.println("SulmunSubjBean.OBJECT_QUESTION 111111 ----> "+ SulmunSubjBean.SUBJECT_QUESTION);
    	   System.out.println("data.getSultype() ----> "+ data.getSultype());
%>	        
            <thead>
		      <tr>
		        <th scope="row">문제<%=i+1%></th>
		        <th colspan="4" scope="row" class="left"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></th>
	          </tr>
	        </thead>
		    
<%        int m = 0;   
				//out.println(data.getSubjectAnswer().size());
				//out.println("/"+v_replycount);
			if(v_replycount > data.getSubjectAnswer().size())	v_replycount = data.getSubjectAnswer().size();
			for (int j=0; j < data.getSubjectAnswer().size(); j++) {
				if(((data.getSubjectAnswer().size() / v_replycount)*m + (k+1)) == (j+1)) {
			//	if((m + (k+1)) == (j+1)) {
					if(m < v_replycount) m++;
				//	if(m < data.getSubjectAnswer().size()) m++;
%>		    
			<tbody>
		      <tr>
		        <td class="num"></td>
		        <td class="left" colspan="4"><%=(String)data.getSubjectAnswer().get(j)%></td>
	          </tr>
	        </tbody>
<%           }   
		   }
           k++;	
       } else if (data.getSultype().equals(SulmunSubjBean.COMPLEX_QUESTION)) {
    	   System.out.println("SulmunSubjBean.COMPLEX_QUESTION 111111 ----> "+ SulmunSubjBean.COMPLEX_QUESTION);
    	   System.out.println("data.getSultype() ----> "+ data.getSultype());
%>
	        <thead>
		      <tr>
		        <th scope="row">문제<%=i+1%></th>
		        <th colspan="4" scope="row" class="left"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></th>
	          </tr>
	        </thead>
<%          for (int j=1; j <= data.size(); j++) {
                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { %>
		    <tbody>
		      <tr>
		        <td class="num"><%=subdata.getSelnum()%></td>
		        <td class="left"><%=subdata.getSeltext()%></td>
		        <td class="num"><%=subdata.getReplycnt()%>명</td>
		        <td class="num"><%=subdata.getReplyrate()%>%</td>
		        <td class="left"><div class="graph_1" style="width:<%=(int)subdata.getReplyrate()%>px;"></div></td>
	          </tr>
	        </tbody>
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
	        <thead>
		      <tr>
		        <th scope="row"><%if(etc==1){%>기타<%}%></th>
		        <td colspan="4" class="left"><%=(String)data.getComplexAnswer().get(j)%></td>
	          </tr>
	        </thead>
<%
					}
                }   
		   }
           l++;
       } else if (data.getSultype().equals(SulmunSubjBean.FSCALE_QUESTION)) {			//객관식
    	   
    	   System.out.println("SulmunSubjBean.FSCALE_QUESTION 111111 ----> "+ SulmunSubjBean.FSCALE_QUESTION);
    	   System.out.println("data.getSultype() ----> "+ data.getSultype());
    	   
	          double d = 0; 
			  int person = 0;
			  double v_point = 0;
      
		   for (int j=1; j <= data.size(); j++) {
	            subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 
                	
                	System.out.println("subdata.getReplycnt() ----> "+ subdata.getReplycnt());
					System.out.println("subdata.getSelpoint() ----> "+ subdata.getSelpoint());
					
					
					d +=  (subdata.getReplycnt()) * subdata.getSelpoint();
					person += subdata.getReplycnt();
				}
            }	
               
		   System.out.println("d ----> "+ d);
		   System.out.println("person ----> "+ person);
		   
		   System.out.println("v_point ----> "+ v_point);
		System.out.println("sulCount ----> "+ sulCount);
 		
			
		v_point = d / person;	  
		
		sulCount++;
		sulAvg += v_point;
		
		System.out.println("sulAvg ----> "+ sulAvg);
%>
	        <thead>
		      <tr>
		        <th scope="row">문제<%=i+1%></th>
		        <th colspan="3" scope="row" class="left"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></th>
		        <th scope="row"><%if(v_point >=0){%><%=df.format(v_point)%>점<%}%></th>
	          </tr>
	        </thead>
<%

		   for (int j=1; j <= data.size(); j++) {

                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 
%>	        
		    <tbody>
		      <tr>
		        <td class="num"><%=subdata.getSelnum()%></td>
		        <td class="left"><%=subdata.getSeltext()%></td>
		        <td class="num"><%=subdata.getReplycnt()%>명</td>
		        <td class="num"><%=subdata.getReplyrate()%>%</td>
		        <td class="left"><div class="graph_1" style="width:<%=(int)subdata.getReplyrate()%>px;"></div></td>
	          </tr>
	        </tbody>
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
		sulCount++;
		sulAvg += v_point;
%>
	        <thead>
		      <tr>
		        <th scope="row">문제<%=i+1%></th>
		        <th colspan="3" scope="row" class="left"><%="["+data.getDistcodenm()+"] [" + data.getSultypenm() + "] " + data.getSultext()%></th>
		        <th scope="row"><%if(v_point >=0){%><%=df.format(v_point)%>점<%}%></th>
	          </tr>
	        </thead>
<%

		   for (int j=1; j <= data.size(); j++) {

                subdata  = (SulmunExampleData)data.get(j); 
                if (subdata != null) { 
%>
	        <tbody>
		      <tr>
		        <td class="num"><%=subdata.getSelnum()%></td>
		        <td class="left"><%=subdata.getSeltext()%></td>
		        <td class="num"><%=subdata.getReplycnt()%>명</td>
		        <td class="num"><%=subdata.getReplyrate()%>%</td>
		        <td class="left"><div class="graph_1" style="width:<%=(int)subdata.getReplyrate()%>px;"></div></td>
	          </tr>
	        </tbody>
<%           }    
            }	
	   } 
    } 
%>
	            
	        
	      </table>
		</div>
		<!-- list table-->
		<br/>
		<div class="sub_tit">
			<h4 id="sulScoreTotalH4">교육후기</h4>	
		</div>
		
		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="10%"/>		      
		      <col width="10%"/>
		      <col width=""/>
	        </colgroup>
	        <thead>
		      <tr>
		        <th scope="row">아이디</th>
		        <th scope="row">이름</th>
		        <th scope="row" class="left">교육후기</th>
	          </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${hukiList}" var="result" varStatus="status">	
	          <tr>
	          	<td>${result.userid }</td>
	          	<td>${result.name }</td>
	          	<td style="text-align:left">${result.comments }</td>
	          </tr>
	        </c:forEach>
	        </tbody>
	      </table>
	    </div>
		
</form>


<%
	String sulScore = "0.0";

	if(sulCount > 0)
	{
		sulScore = df.format(sulAvg/sulCount) + "";
	}
	
%>


<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--



var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/rsh/sulResultList.do?p_process=xlsdown";
	thisForm.target = "_self";
	thisForm.submit();
}



/* ********************************************************
 * 엑셀상세다운로드 함수
 ******************************************************** */
function whenXlsDetailDownLoad() {
	thisForm.action = "/adm/rsh/sulResultList.do?p_process=xlsdetaildown";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/rsh/sulResultList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/rsh/sulResultList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}



window.onload = function () {

	document.all("sulScoreTotalH4").innerHTML = document.all("sulScoreTotalH4").innerHTML + " / 설문평균 : <%=sulScore%>점";
}
//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
