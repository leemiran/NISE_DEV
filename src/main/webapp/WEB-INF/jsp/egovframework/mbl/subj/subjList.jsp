<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/userLoginCheck.jsp" %>

	<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>교육과정</a></span>
	</div>
	
	<!-- 검색 -->
	<div class="total_search sbg">
			<form action="" onsubmit="return go_search()">
			<ui:code id="search_upperclass" selectItem="${search_upperclass}" gubun="cursBunryu" codetype=""  upper="" title="분류선택" className="ts" type="select" selectTitle="ALL" event="" />
			<input class="ts_input" type="text" name="search_text" id="search_text" title="검색어 입력" value="${search_text}"/>
			<input type="image" src="/images/mbl/btn/btn_search.gif" alt="검색" style="width:47px;height:25px;border:none;"/>
			</form>
	</div>
	
	
	
	<!-- 본문 -->
	<div id="wcontainer">
		<%@ include file = "./subjListAjax.jsp" %>
	</div>
	<!-- //본문 -->







<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
//<![CDATA[
	function go_search()
	{
		$('#firstIndex').val(0);
		$('#recordCountPerPage').val(15);
		CtlExecutor.requestAjaxSubj();
		return false;
	}
	
	var CtlExecutor = {
       
		requestAjaxSubj : function () {
			
			 //alert($('#firstIndex').val());
			 var param = "firstIndex="+$('#firstIndex').val()+"&recordCountPerPage="+$('#recordCountPerPage').val()+"&search_upperclass="+$('#search_upperclass').val()+"&search_text="+$('#search_text').val()+"&pageGubun=ajax";
			 
			 $.ajax({
			  type:"post",
			  url : "/mbl/subj/subjList.do",
			  data: param,
			  success:function(data){
				//alert(data);
			   	$("#wcontainer").html(data);
			  },
			  error:function(e){
			   alert(e);
			  }
			 });
			}
      
              
	}  

//]]> 
</script>
</div>
</body>

<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>
