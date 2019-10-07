<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/jquery/jquery-latest.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/jquery/jquery-ui.js"></script>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type = "hidden" name="p_seq"     value = "${p_seq}" />
	<input type = "hidden" name="p_iseq"     value = "" />
	<input type = "hidden" name="p_process"     value = "" />
	
	
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doPageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="trainingScheduleMove()"><span>순서 수정</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>		
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%" id="listTable1">
                <caption>목록</caption>
                <colgroup>
				<col width="70px" />				
				<col width="70px" />
				<col width="100px" />
				<col width="100px" />
				<col />				
				<col width="200px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>월</th>
					<th>과정</th>
					<th>등록기간</th>
					<th>연수기간(주)</th>
					<th>등록일</th>
					<th>등록자</th>
					<th>사용여부</th>
					<th>수정</th>
					<th>삭제</th>
					<th>순서변경</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
				<input type="hidden" name="_Array_p_params" value="${result.iseq}">
					<td >${totCnt - result.rn + 1}</td>
					<td class="left">${result.mon}</td>
					<td >${result.course}</td>
					<td >${result.regTerm}</td>
					<td >${result.trainingTerm}</td>					
					<td >${result.ldate}</td>
					<td >${result.name}</td>
					<td >${result.useYn}</td>
					<td ><a href="#none" onclick="trainingScheduleEdit('${result.iseq}')"><span>수정</span></a></td>
					<td ><a href="#" onclick="whenTrainingScheduleSave('${result.iseq}', 'delete')"><span>삭제</span></a></td>
					<td ><button type="button" onclick="moveUp(this)">올리기</button>
						 <button type="button" onclick="moveDown(this)">내리기</button></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="50">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->	
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	
	function doPageList() {
		frm.action = "/adm/hom/trs/selectTrainingList.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function insertPage(){		
		frm.action = "/adm/hom/trs/trainingScheduleInsertPage.do";
		frm.target = "_self";
		frm.submit();
	}	
	
	//수정하기
	function trainingScheduleEdit(iseq) {		
		frm.p_iseq.value = iseq;
		frm.action = "/adm/hom/trs/trainingScheduleInsertPage.do";
		frm.target = "_self";
		frm.submit();
    }
    
	//삭제하기
	function whenTrainingScheduleSave(iseq, mode) {

		if(confirm("현재글을 삭제하시겠습니까?"))
		{   
			frm.p_iseq.value = iseq;
			frm.action = "/adm/hom/trs/trainingScheduleActionPage.do";
			frm.p_process.value = mode;
			frm.target = "_self";
			frm.submit();
		}
    }
	
	//순서수정하기
	function trainingScheduleMove() {
		if(confirm("순서 수정 하시겠습니까?")){
			frm.p_process.value = "update";
			frm.action = "/adm/hom/trs/trainingScheduleMove.do";
			frm.target = "_self";
			frm.submit();
		}	
    }
	
</script>


<script type="text/javascript">

	function moveUp(el){		
		var $tr = $(el).parent().parent(); // 클릭한 버튼이 속한 tr 요소
		$tr.prev().before($tr); // 현재 tr 의 이전 tr 앞에 선택한 tr 넣기
	}

	function moveDown(el){
		var $tr = $(el).parent().parent(); // 클릭한 버튼이 속한 tr 요소
		$tr.next().after($tr); // 현재 tr 의 다음 tr 뒤에 선택한 tr 넣기
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
    
    
    /* $(document).ready(function() {  
 
		$('table tbody tr:visible').each(function(cols) {  
		   $('#listTable2').rowspan2(cols);  
		 })  

    }); */
    
</script>  