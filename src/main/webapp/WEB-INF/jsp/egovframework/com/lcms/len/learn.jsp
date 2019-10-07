<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/egovframework/com/lcms/len/common/INC_PopHeader.jsp"%>

<script type="text/javascript">
var jp_subj = "<c:out value="${param.subj}"/>";
var testid  =  '${sessionScope.userid}';
</script>


<c:choose>
	<c:when test="${param.subj eq 'PAR130001' || param.subj eq 'PAR130002' ||  param.subj eq 'PAR130003'}">
				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/normalStudy.js"></script>
				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/dtree1.js"></script>
			    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/treeInit_normal.js"></script>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${output.contenttype eq 'P'}"><!-- wmv P -->		
				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/normalStudy.js"></script>
				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/dtree1.js"></script>
			    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/treeInit_normal.js"></script>
			</c:when>
			<c:when test="${output.contenttype eq 'X'}"><!-- xinice X -->
			    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/XiniceStudy.js"></script>  
			</c:when>
			<c:when test="${output.contenttype eq 'C'}"><!-- 중공교 C -->
				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/cotiStudy.js"></script>
				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/dtree1.js"></script>
			    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/treeInit_coti.js"></script>
			</c:when>
			<c:when test="${output.contenttype eq 'S'}"><!-- scorm S -->
			    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/scormStudy.js"></script>
				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/dtree.js"></script>
			    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/treeInit.js"></script>
				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/ClientRTS.js"></script>
			</c:when>
			<c:otherwise>
				<!-- nonscrm N -->
<!--				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/normalStudy.js"></script>-->
<!--				<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/dtree1.js"></script>-->
<!--			    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/treeInit_normal.js"></script>-->

		<!-- nonscrm N 페이지당 진도체크이며, 학습창 트리가 차시단위일 경우 변경한다.-->
		<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/normalStudy_page.js"></script>
		<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/dtree1.js"></script>
	    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/treeInit_normal_page.js"></script>
	    
			</c:otherwise>
		</c:choose>    
	</c:otherwise>
</c:choose>
		
		
<script type="text/javascript">
<!--
/*==========================================*
IE6 png 투명 처리를 위한 스크립트 
*==========================================*/

function setPng24(obj) { 
    obj.width=obj.height=1; 
    obj.className=obj.className.replace(/\bpng24\b/i,''); 
    obj.style.filter = 
    "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+ obj.src +"',sizingMethod='image');" 
    obj.src='';  
    return '';   
} 

/*==========================================*

엘리먼트 showHide

*==========================================*/

function MM_showHideLayers() { //v9.0

  var i,p=0,v,obj,args=MM_showHideLayers.arguments;

	//alert('args.length : ' + args.length);
  for (i=0; i<args.length; i+=3){ 
	  with (document) {
	  	if (getElementById && ((obj=getElementById(args[i]))!= null)) {   
	  		v=args[i+2];
	    	if (obj.style) { 
	    		obj=obj.style; 
	    		
	    		if(v=='show'){
	    		$(".mediaWrap").css("margin","30px 0px 0px 268px");
	    		$(".learnListpop").css("width","230px","position","absolute","bottom","0","left","0","z-index","20");
	    		}else if(v=='hide'){
	    		$(".mediaWrap").css("margin","30px 0px 0px 40px");
	    		$(".learnListpop").css("width","10px","position","absolute","bottom","0","left","0","z-index","20");
	    		}
	    		v=(v=='show')?'block':(v=='hide')?'none':v; 
	    	}
		    obj.display =v; 
		    }
	    }
	}
	if( v == 'block' ){
		document.getElementById("lcmsTitle").style.paddingLeft = 200;
	}else{
		document.getElementById("lcmsTitle").style.paddingLeft = 20;
	}	
}
-->
</script>
<body>

<div class="learnWrap" id="studyBody">

	<!-- 상단 메뉴-->
	<div class="menuWrap">
		<!-- 메뉴 over 이미지 있습니다. 메뉴명_over.gif -->
		<ul class="leftMenu">
			<li>
				<font color="white">
					<strong id="lcmsTitle" style="padding-left:200px;">
						<c:if test="${param.preview eq 'Y'}">
						* 미리보기 실행중입니다.(진도 데이터는 학습로그를 통해 확인하세요.)
						</c:if>
						<c:if test="${param.preview ne 'Y'}">
						
						</c:if>
					</strong>
				</font>
			</li>
		</ul>
	</div>
	<!-- /상단 메뉴-->

	<!-- 학습 현황 -->
	<div class="learnListpop" id="listpopHide" style="display: none;" onClick="MM_showHideLayers('listpopHide','','hide','listpopShow','','show')" >
		<img src="<c:out value="${gsDomainContext}"/>/images/learn/admin_user_collapse_btn_open.gif"  class="pointer png24" onClick="MM_showHideLayers('listpopHide','','show','listpopShow','','hide')" alt="트리메뉴보이기/숨기기"/>
		<div class="poplistBody2" style="background:#323131; width:100%; height:680px; padding-top:60px;"></div>
	</div>

	<div class="learnListpop" id="listpopShow" style="display: block;">
		<div><img src="<c:out value="${gsDomainContext}"/>/images/learn/btn_learnlist_02.png" class="pointer png24" onClick="MM_showHideLayers('listpopHide','','show','listpopShow','','hide')" alt="학습현황보이기/숨기기"/></div>
		<div class="poplistBody2" style="background:#323131; overflow:scroll; width:100%; height:680px; padding-top:60px;">
			<div style="width:500px;" id="contentItemList"></div>
		</div>
	</div>
	<input type="hidden" name="attend_tot_time" id="attend_tot_time" value="0">
	<!-- 학습 현황 -->
	<div class="mediaWrap">
		<iframe src="" id="content" name="content" width="1024px" height="710px" frameborder="0" marginwidth="0" marginheight="0" scrolling="auto" align="middle" title="학습현황"></iframe>
	</div>
	

</div>





<!-- /하단 이전, 다음 버튼 -->
<div id="test" class="menuWrap" style="height:100px;border:1px;">
	<ul class="leftMenu" style="padding-left : 10px">
		<li><a href="#none"  onclick="expand('/usr/stu/std/userStudyNoticeList.do');return false;" title="새창"><img src="/images/user/study/btn01.gif" alt="공지사항"/></a></li>
		<li><a href="#none"  onclick="expand('/usr/stu/std/userStudyDataList.do');return false;" title="새창"><img src="/images/user/study/btn03.gif" alt="자료실"/></a></li>
		<li><a href="#none"  onclick="expand('/usr/stu/std/userStudyQnaList.do');return false;" title="새창"><img src="/images/user/study/btn04.gif"  alt="질문방"/></a></li>
	</ul>
	
	<ul class="rightMenu" style="display:none">   
	    <li><div id="pageNum" style="display: block"></div></li>
	    <li><div id="prev" style="display: block"></div></li>
	    <li><div id="next" style="display: block"></div></li>
	    <li><div id="quit" style="display: block"></div></li>  
		<li>&nbsp;</li>
	</ul>
</div>






<div id="debug_div" style="overflow: auto; height: 115px; padding-left:10px;" ></div>
<div style="position: absolute; left: 1024; top: 25; width:1; height:1; background-color: #FFFFFF; border-style: double; display:none;" id="boardDiv">
	<iframe src="blankLean.jsp" frameborder="0" width="0" height="0"  marginwidth="0" marginheight="0" scrolling="no" id="board" name="board" title="게시판"></iframe>
	<div style="position: relative; top: -568px; left: 730; " id="closeDiv" name="closeDiv">
		<a href="#none" onclick="javascript:closeBoard();"><img src="/images/user/study/btn_close.gif" alt="학습창닫기"></a>
	</div>
</div>

<div id="totalTime1"></div>
	<div id="totalTime2"></div>

<script type="text/javascript">
<!--
var idx = 1;
var time;
var url = "";
function expand(param){
	window.open('about:blank', 'windows_board', 'top=100px, left=100px, height=600px, width=820px, scrollbars=yes, resizable=yes');
	var frm = document.boardForm;
	frm.action = param;
	frm.target = "windows_board";	
	frm.submit();

	
	/*
	if( time ) time.clearTimeout;
	var target = document.getElementById("boardDiv");
	var frame = document.getElementById("board");
	target.style.display = "";
	if( param ){
		target.style.width = 1;
		target.style.height = 1;
		target.style.left = 230;
		frame.width = 800;
		frame.height = 600;
		url = param;
		frame.src = "about:blank";
	}
	*/
	/*
	if( parseInt(target.style.width) < 800 ){
		target.style.width = parseInt(target.style.width)+5;
		target.style.height = parseInt(target.style.height)+(idx%2 == 1 ? 3 : 4);
		target.style.left = parseInt(target.style.left) - 5;
		frame.width = parseInt(target.style.width)+5;
		frame.height = parseInt(target.style.height)+(idx%2 == 1 ? 3 : 4);
		idx++;
		time = setTimeout(expand,1);
	}else{*/
		//document.getElementById("closeDiv").style.display = "";
		//idx = 0;	
		//var frm = document.boardForm;
		//frm.action = url;
		//frm.target = "board";	
		//frm.submit();
		//frame.width = 800;
		//frame.height = 600;
	//}
		
}

function closeBoard(){
	if( time ) time.clearTimeout;
	document.getElementById("closeDiv").style.display = "none";
	var target = document.getElementById("boardDiv");
	var frame = document.getElementById("board");
	/*if( parseInt(target.style.width) > 1 ){
		frame.width = parseInt(frame.width)-5;
		target.style.width = parseInt(target.style.width)-5;
		target.style.height = parseInt(target.style.height)-(idx%2 == 1 ? 3 : 4);
		target.style.left = parseInt(target.style.left) + 5;
		idx++;
		time = setTimeout(closeBoard,1);
	}else{
	*/
		idx = 1;	
		target.style.display = "none";
		frame.height = 0;
		frame.src = "about:blank";
	//}
}
-->
</script>

<script type="text/javascript">
<!--
	function debug(str){
		var debugArea=document.getElementById("debug_div");
		debugArea.innerHTML+= "<br/><font color='#FFFFFF'>"+str+"</font>";
		debugArea.scrollTop = debugArea.scrollHeight;
	}
	function error(str){
		var debugArea=document.getElementById("debug_div");
		debugArea.innerHTML+= "<font color='RED'>"+str+"</font>";
		debugArea.scrollTop = debugArea.scrollHeight;
	}
-->
</script>
<form id="boardForm" name="boardForm" method="post" onsubmit="return false" action="">
	<input type="hidden" id="p_subj" 	 name="p_subj" 		value="<c:out value="${param.subj}"/>" />
	<input type="hidden" id="p_year" 	 name="p_year" 		value="<c:out value="${param.year}"/>" />
	<input type="hidden" id="p_subjseq"  name="p_subjseq" 	value="<c:out value="${param.subjseq}"/>" />
	<input type="hidden" id="studyPopup" name="studyPopup"  value="Y" />
</form>
<form id="<c:out value="${gsPopForm}"/>" name="<c:out value="${gsPopForm}"/>"  action="">
	<input type="hidden" id="subj" name="subj" value="<c:out value="${param.subj}"/>" />
	<input type="hidden" id="p_subjnm" name="p_subjnm" value="<c:out value="${output.subjnm}"/>" />
	<input type="hidden" id="year" name="year" value="<c:out value="${param.year}"/>" />
	<input type="hidden" id="subjseq" name="subjseq" value="<c:out value="${param.subjseq}"/>" />
	<input type="hidden" id="module" name="module" value="<c:out value="${param.module}"/>" />
	<input type="hidden" id="lesson" name="lesson" value="<c:out value="${param.lesson}"/>" />
	<input type="hidden" id="learningType" name="learningType" value="<c:out value="${param.learningType}"/>" />
	
	<input type="hidden" id="orgCd"     name="orgCd"      value="<c:out value="${param.orgCd}"/>" />	
	<input type="hidden" id="orgSeq"    name="orgSeq"     value="<c:out value="${param.orgSeq}"/>" />
	<input type="hidden" id="crsCode"   name="crsCode"   value="<c:out value="${param.crsCode}"/>" />
	<input type="hidden" id="courseSeq" name="courseSeq"  value="<c:out value="${param.courseSeq}"/>" />
	<input type="hidden" id="suryoLessonTime" name="suryoLessonTime"  value="<c:out value="${param.suryoLessonTime}"/>" />
	<input type="hidden" id="test1"     name="test1" />
	<input type="hidden" id="time_limit"     name="time_limit" value="N" />
	<input type="hidden" id="preview"     name="preview" value="${preview }" />
	
	<input type="hidden" id="p_review_study_yn"     name="p_review_study_yn" value="${param.p_review_study_yn }" />
	
</form>	
<form id="wmvForm" name="wmvForm"  action="">
	<input type="hidden" id="starting"    name="starting" value="" />
	<input type="hidden" id="location"     name="location" value="" />
	<input type="hidden" id="contentTitle" name="contentTitle" value="" />
	<input type="hidden" id="times" name="times" value="0" />
	<input type="hidden" id="times1" name="times1" value="0" />
</form>	
 

<script type="text/javascript">
<!--	

var cnt = -1;
var totTime = -1;
	var API_1484_11 = null; 
	var closeEvent = false; 
	var crrtime = "";
	var d=new Date();
	$(window).bind('load', function(){getContentInfo();});
	

	$(window).bind('unload', function(){
		if($("#time_limit").val() == 'Y'){
			ContentExecutor.onExit('Y');
		}else{
			ContentExecutor.onExit();
		}  
		
    });


	var API_1484_11 = null; 

	$("#content").load(function(e) {
		if( '<c:out value="${output.contenttype}"/>' == "X" ){
			this.style.width ="1024";
			this.style.height = "710";
		    MM_showHideLayers('listpopHide','','show','listpopShow','','hide');
		}else{
			/**
		    this.style.width ='<c:out value="${output.width}"/>';
		    this.style.height = '<c:out value="${output.height}"/>';

			**/
			this.style.width ='1024';
		    this.style.height = '710';
		    
		    if('${output.leftContentItemListView}' == "Y"){		    
		    	MM_showHideLayers('listpopHide','','show','listpopShow','','show');
		    }else if('${output.leftContentItemListView}' == "N"){		    
		    	MM_showHideLayers('listpopHide','','hide','listpopShow','','hide');
		    }
		    
		}
	});	

	
	var map = new JMap();
	
	if('<c:out value="${param.preview}"/>' !='Y'){
		map.put("year",'<c:out value="${param.year}"/>');
		map.put("subjseq",'<c:out value="${param.subjseq}"/>');
		map.put("courseMapSeq",'<c:out value="${courseMapSeq}"/>');
		map.put("nowYear",'<c:out value="${param.nowYear}"/>');   // 존나 중요함..
		$("#debug_div").css("display","none");
		//var hi = $("#studyBody").outerHeight() - document.body.scrollHeight;
		//window.resizeBy(0, hi);
	}else{
		map.put("courseMapSeq",'<c:out value="${courseMapSeq}"/>');
		map.put("nowYear","");  
		map.put("year",'1900');
		map.put("subjseq",'000');
	}

	if('<c:out value="${param.review}"/>' == 'Y' ){
		map.put("nowYear","_REVIEW");  
		map.put("review","Y");  
	}
	if('<c:out value="${param.sample}"/>' == 'Y' ){
		map.put("review","Y");   
		$("#debug_div").css("display","none");
	}
	

	
	map.put("subj",'<c:out value="${subj}"/>');
	map.put("orgSeq",'<c:out value ="${param.orgSeq}"/>');
	map.put("userId",'<c:out value ="${userid}"/>');
	map.put("scoId",'<c:out value  ="${param.itemId}"/>');
	map.put("learnerName",'<c:out value="${name}"/>');
	
	map.put("module",'<c:out value="${module}"/>');
	map.put("lesson",'<c:out value="${lesson}"/>');
	map.put("studyType",'<c:out value="${p_studyType}"/>');
	map.put("p_review_study_yn",'<c:out value="${p_review_study_yn}"/>');
	
	//map.put("save","Y"); // 진도저장 구분자. 
	//map.put("reqApplySeq",""); // 잘모르겠습 알아봐야함

	var contentType = null;
	var itemTree = null;
	var lessonTree = null;
	var classboardList = null;
	
	function getContentInfo() {
		var contentsType = '<c:out value="${output.contenttype}"/>';
		if( contentsType == "S" ){ 
			ContentExecutor.init(map, '<c:out value="${param.learningType}"/>', '<c:out value="${param.otype}"/>');
		}else{
			var link = "";
			switch(contentsType){
				case 'X':
					link = '<c:out value="${gsDomainContext}" />/com/lcms/len/xiniceStudyLesson.do';
					break;

				case 'C':
					link = '<c:out value="${gsDomainContext}" />/com/lcms/len/cotiStudyLesson.do';
					break;

				default :
					link = '<c:out value="${gsDomainContext}" />/com/lcms/len/normalStudyLesson.do';
			}
			var result = ajaxCall({
				url		: link,
			    async 	: false,
			    type	: 'POST',
			    ajaxId 	: "",
			    method 	: "",
			    data 	: map, 
			    validationForm : ""
			});
			//alert(result); 
			//ContentExecutor.init(result, contentsType, '<c:out value="${param.otype}"/>', '<c:out value="${param.module}"/>');
			ContentExecutor.init(result, contentsType, '<c:out value="${param.otype}"/>', '<c:out value="${param.module}"/>', '<c:out value="${p_contentLessonAllView}"/>', '<c:out value="${param.subj}"/>');
		}
		document.title=tempTitle;
	}
	function add(str){
		cnt++;
		
	//	document.getElementById("totalTime1").innerHTML = "학습 시간 : " + cnt;
		$("#times").val(cnt);

	//	if(cnt == 20){
			//$("#time_limit").val("Y");
		//	stopTimer();
		//	if(confirm("학습을 시작한지 10분이 경과하였습니다. 계속하시겠습니까? ")){
		//		myinit();
			//}else{
			//	ContentExecutor.commit1(true, 'N');
			//	opener.close();
			//	self.close(); 
			//}
		//	alert("학습을 시작한지 10분이 경과하여 학습을 종료합니다.");
			
		//}
	}

	
	function myinit(){
		refreshTimer = setInterval(add, 1000);
	}

	function totTimeInit(a){
		setInterval(function(){
			a++;
		//	document.getElementById("totalTime2").innerHTML = "총 학습시간 : " + a;
			$("#times1").val(a);
			if(parseInt(a) == parseInt($("#suryoLessonTime").val())){
				ContentExecutor.commit1(true, 'Y');
			}
		}, 1000);
	}

	function stopTimer(){
		clearInterval(refreshTimer);
		cnt = -1;
	}

	function padZero(n) {
	  return n<10?"0"+n:n;
	}

-->
</script>
</body>
</html>




