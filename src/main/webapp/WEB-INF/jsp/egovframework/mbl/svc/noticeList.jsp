<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>


	<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>연수안내</a></span>
		<span><a>공지사항</a></span>
	</div>
	<!-- 본문 -->
	<div id="wcontainer">
		<%@ include file = "./noticeListAjax.jsp" %>
	</div>
	<!-- //본문 -->
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
//<![CDATA[		
	var CtlExecutor = {       
		requestAjaxSubj : function () {			
			 //alert($('#firstIndex').val());
			 var param = "firstIndex="+$('#firstIndex').val()+"&recordCountPerPage="+$('#recordCountPerPage').val()+"&pageGubun=ajax";
			 
			 $.ajax({
			  type:"post",
			  url : "/mbl/svc/noticeList.do",
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

