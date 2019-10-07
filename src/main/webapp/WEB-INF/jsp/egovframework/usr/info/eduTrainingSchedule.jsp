
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>

<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>


<style>  
.merge { background-color:#FF0; color:#03F; text-align:center}  
</style>  


<form name="studyForm" id="studyForm" method="post">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
<fieldset>
<%
	int i = 1;
%>
	<div class="sub_txt_1depth m30">
		<%-- <h4><strong>${view.subject}</strong></h4> --%>   
		
		<c:forEach items="${fileList}" var="result" varStatus="i">
        	<p class="course_down">
        		<a href="#none" onclick="fn_download('${result.realfile}', '${result.savefile}', 'training')"><img src="/images/user/c_d_download.gif" alt="교과목 및 시간 배당 다운로드" /></a></p>
            <br/>        	
        </c:forEach>
	    
	    <ul>
		    	<div class="courseList">
		    		<table width="100%" summary="월, 과정, 등록기간, 연수기간(주) 으로 구성되어짐" id="listTable1">  
		    			<caption>원격연수 연간연수 일정</caption>
	                    <colgroup>
	                    	<col width="10%" />
	                        <col width="20%" />
	                        <col width="30%" />
	                        <col width="40%" /> 
	                        <col />
	                    </colgroup>
	                    <thead>
	                    	<tr>
		                        <th scope="row">월</th>
		                        <th scope="row">과정</th>
		                        <th scope="row">등록기간</th>
		                        <th scope="row" class="last">연수기간(주)</th>		                        
	                        </tr>
	                    </thead>
	                    <tbody>
	                    <c:forEach items="${list}" var="result" varStatus="i">
	                    	<tr>
								<td>${result.mon}</td>
							  	<td>${result.course}</td>
							  	<td>${result.regTerm}</td>
							  	<td class="last">${result.trainingTerm}</td>							  	
							</tr>
						</c:forEach>	
	                    </tbody>
	                </table>                                    
	            </div>
	            <div>
	            	<p></br><c:out value="${fn:replace(view.contents, lf, '<br/>')}" escapeXml="false"/></p>
	            </div>
	            </br>
	           
	           <h4><strong>원격연수 연수과정</strong></h4>
	           <div class="courseList">
		    		<table width="100%" summary="구분, 과정명, 시간, 대상, 인정여부 으로 구성되어짐"  id="listTable2">  
		    			<caption>원격연수 연수과정</caption>
	                    <colgroup>
	                    	<col width="10%" />
	                        <col width="50%" />
	                        <col width="10%" />
	                        <col width="10%" /> 
	                        <col width="10%" />	                        
	                        <col />
	                    </colgroup>
	                    <thead>
	                    	<tr>
		                        <th scope="row" rowspan="2">구분</th>
		                        <th scope="row" rowspan="2" >과정명</th>
		                        <th scope="row" rowspan="2">시간</th>
		                        <th scope="row" rowspan="2">대상</th>
		                        <th scope="row" class="last" >인정여부</th>		                        
	                        </tr>
	                        <tr>		                        
		                        <th scope="row" class="last">통합교육연수</th>
		                        <!-- <th scope="row" class="last">보조인력</th> -->		                        
	                        </tr>
	                    </thead>
	                    <tbody>
	                    <c:forEach items="${courseList}" var="result" varStatus="i">	
	                    	<tr>
								<td>${result.gubun}</td>
							  	<td>${result.coursenm}</td>
							  	<td>${result.eduTime}</td>
							  	<td>${result.eduTarget}</td>
							  	<td class-"last">${result.totalEdu}</td>							  	
							  	<%-- <td class="last">${result.assist}</td> --%>							  	
							</tr>
						</c:forEach>
						
	                    </tbody>
	                </table>                                    
	            </div>
	    		 	    	        
		</ul>            
	</div>   
	
	    
</fieldset>
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

<script type="text/javascript">
	function change2(bb){
		time.location.href=""+bb+".htm";
		}	
</script>


<script type="text/javascript">  
  
  
/*   
 *   
 * 같은 값이 있는 열을 병합함  
 *   
 * 사용법 : $('#테이블 ID').rowspan(0);  
 *   
 */       
$.fn.rowspan1 = function(colIdx, isStats) {         
    return this.each(function(){        
        var that;       
        $('tr', this).each(function(row) {        
            $('td',this).eq(colIdx).filter(':visible').each(function(col) {  
                  
                if ($(this).html() == $(that).html()  
                    && (!isStats   
                            || isStats && $(this).prev().html() == $(that).prev().html()  
                            )  
                    && ($(this).html() != '&nbsp;')
					) {              
                    rowspan = $(that).attr("rowspan") || 1;  
                    rowspan = Number(rowspan)+1;  
  
                    $(that).attr("rowspan",rowspan);  
                      
                    // do your action for the colspan cell here              
                    $(this).hide();  
                      
                    //$(this).remove();   
                    // do your action for the old cell here  
                      
                } else {              
                    that = this;           
                }            
                  
                // set the that if not already set  
                that = (that == null) ? this : that;        
            });       
        });      
    });    
};   
  

$.fn.rowspan2 = function(colIdx, isStats) {         
    return this.each(function(){        
        var that;       
        $('tr', this).each(function(row) {
			 $('td',this).eq(colIdx).filter(':visible').each(function(col) {  
                 
		 
                if ($(this).html() == $(that).html()  
                    && (!isStats   
                            || isStats && $(this).prev().html() == $(that).prev().html()  
                            )  
                    && ($(this).html() != '&nbsp;')
                    && (colIdx == '0')
					) {              
                    rowspan = $(that).attr("rowspan") || 1;  
                    rowspan = Number(rowspan)+1;  
  
                    $(that).attr("rowspan",rowspan);  
                      
                    // do your action for the colspan cell here              
                    $(this).hide();  
                      
                    //$(this).remove();   
                    // do your action for the old cell here  
                      
                } else {              
                    that = this;           
                }            
                  
                // set the that if not already set  
                that = (that == null) ? this : that;        
            });        
        });      
    });    
};   
  
  
  
/*   
 *   
 * 같은 값이 있는 행을 병합함  
 *   
 * 사용법 : $('#테이블 ID').colspan (0);  
 *   
 */     
$.fn.colspan = function(rowIdx) {  
    return this.each(function(){  
          
 var that;  
  $('tr', this).filter(":eq("+rowIdx+")").each(function(row) {  
  $(this).find('td').filter(':visible').each(function(col) {  
      if ($(this).html() == $(that).html()) {  
	     if ($(this).html() != '&nbsp;') {  
			colspan = $(that).attr("colSpan");  
			if (colspan == undefined) {  
			  $(that).attr("colSpan",1);  
			  colspan = $(that).attr("colSpan");  
			}  
			colspan = Number(colspan)+1;  
			$(that).attr("colSpan",colspan);  
			$(this).hide(); // .remove();  
		}
      } else {  
        that = this;  
      }  
      that = (that == null) ? this : that; // set the that if not already set  
  });  
 });  
  
 });  
} 
  
</script>


<script type="text/javascript">  
    $(document).ready(function() {  
		/*   
		 * 특정행을 지정할때는 rowspan(행수)  
		 * $('#listTable ID').rowspan(0);  
		 */  
		$('table tbody tr:visible').each(function(cols) {  
		   $('#listTable1').rowspan1(cols);  
		 })  

    });  
  
    /* $(document).ready(function() {
		$('table tbody tr:visible').each(function(row) {  
		   $('#listTable1').colspan(row);  
		 })  
    }); */
    
    
    $(document).ready(function() {  
 
		$('table tbody tr:visible').each(function(cols) {  
		   $('#listTable2').rowspan2(cols);  
		 })  

    });
    
</script>  
