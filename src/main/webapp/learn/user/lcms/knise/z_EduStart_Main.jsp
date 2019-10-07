<%
/**
 *  1. 제      목: 학습창 메인
 *  2. 프로그램명: z_EduStartMain.jsp
 *  3. 개      요: 학습창 메인 (Normal,OBC,OBC-Author,KT,SCORM2004)
 *  4. 환      경: JDK 1.5
 *  5. 버      젼: 1.0
 *  6. 작      성: 정진필, 2008/12/04
 *  7. 수      정:
 **/
%>
<%@ page contentType = "text/html;charset=euc-kr" %>
<%@ page errorPage = "/learn/library/error.jsp" %>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "com.ziaan.library.*" %>
<%@ page import = "com.ziaan.lcms.*" %>
<jsp:useBean id = "conf" class = "com.ziaan.library.ConfigSet"  scope = "page" />
<%
	RequestBox box = null;
	box = (RequestBox) request.getAttribute("requestbox");
	if (box == null) {
		box = RequestManager.getBox(request);
	}

	String v_process = box.getString("p_process");

	String v_subj = box.getString("p_subj");
	String v_year = box.getString("p_year");
	String v_subjseq = box.getString("p_subjseq");
	String v_contenttype = box.getString("p_contenttype");
	String v_subjGubun = box.getString("p_subjgubun");
	String v_studytype = box.getString("p_studytype");
	String s_userid = box.getSession("userid");

	String v_ispreview = box.getString("p_ispreview"); // 맛보기 여부
	
	String v_aftersuledate = "";
	String v_aftersulsdate = "";
	
	
	// 권장 진도율, 나의 진도율
	double v_promotion = Double.parseDouble((String) request.getAttribute("promotion"));
	double v_progress = Double.parseDouble((String) request.getAttribute("progress"));

	// 총차시, 학습한 차시
	int v_datecnt = Integer.parseInt((String) request.getAttribute("datecnt"));
	int v_edudatecnt = Integer.parseInt((String) request.getAttribute("edudatecnt"));
	double v_wstep = Double.parseDouble((String) request.getAttribute("wstep"));
	int v_wstep_edudatecnt =  (int)Math.ceil(v_datecnt*v_wstep/100);

	// 남은 학습시간
	String  v_edutime = "";  // 학습시간
	String  v_edudate = "";  // 최근학습일
	int     v_educount= 0;   // 강의실접근횟수
	ArrayList  list2 = (ArrayList)request.getAttribute("EduTime");  // 학습시간
    EduListData edutime = null;
	if(list2!=null && list2.size()>0) {
		edutime = (EduListData)list2.get(0);
		v_edutime = edutime.getTotal_time();
		v_edudate = edutime.getFirst_edu();
		v_educount= edutime.getLesson_count();
    }

	// 학습정보
    EduScoreData    sd = (EduScoreData)request.getAttribute("EduScore");
    EduScoreDataSub sds;
    
    // 강사정보
    DataBox tutorInfo = (DataBox)request.getAttribute("tutorInfo");

    // 추가
    DecimalFormat df = new DecimalFormat("###.00");
    double v_totscore =0.0;
    String v_isgraduated = "";

	String v_iseduend="";
	v_iseduend= box.getString("p_iseduend");    // 복습(복습:'Y')

	// 제약사항 Check
	boolean allowStudy = ((Boolean) request.getAttribute("allowStudy")).booleanValue();
	boolean isSulpaper = ((Boolean) request.getAttribute("isSulpaper")).booleanValue();
	boolean isInfoAgree = ((Boolean) request.getAttribute("isInfoAgree")).booleanValue();
	boolean isGoyong = ((Boolean) request.getAttribute("isGoyong")).booleanValue();

	// SCORM2004 관련
    String courseCode = box.getString("p_course_code");
    String orgID = box.getString("p_org_id");

    // 게시판 more권한
    boolean isBBSAuth = true;
    if ( v_year.equals("2000") || v_year.equals("PREV") || v_studytype.equals("openedu") ) {
    	isBBSAuth = false;
    }
    
    
    //리포트 정보 추가
    ArrayList list  = (ArrayList)request.getAttribute("ReportInfo");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<title><%= com.ziaan.common.GetCodenm.getTitle(box) %> &gt; 학습창</title>
<meta http-equiv="content-type" content="text/html;charset=euc-kr" />
<meta name="Keywords" content="<%= com.ziaan.common.GetCodenm.getTitle(box) %>, 학습창" />
<meta name="Description" content="학습창" />
<link rel="stylesheet" type="text/css" href="/css/common.css" />
<link rel="stylesheet" type="text/css" href="/css/k.css" />
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="/css/IE_style.css" />
<![endif]-->
<script type="text/javascript" src="/script/prototype.js"></script>
<script type="text/javascript" src="/eportfolio/js/common.js"></script>

<script type="text/javascript">
	function upWin(url,w,h){
		var studyPopup = window.open(url,"studyPopup1","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width="+w+",height="+h+" ");
		studyPopup.focus();
	}

	function popup( popupName, url, process, width, height ){
		var form1 = document.getElementById("form1");

		window.self.name = "studyMain";
		var studyPopup = window.open(url,popupName,"toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width="+width+",height="+height+" ");
		form1.target = popupName;
		form1.action = url;
		form1.p_process.value = process;
		form1.submit();

		form1.target = window.self.name;
		studyPopup.focus();
	}

    // 설문 팝업
    function popupSulpaper() {
    	var url = "/servlet/controller.study.StudySulmunServlet";
    	popup( "popupSulpaper", url, "StudySulmunListPage",  800, 600 );
    }

    // 고용보험안내
    function popupGoyong() {
    	var url = "/servlet/controller.study.SubjGongStudyServlet";
    	popup( "popupGoyong", url, "NoticeGoyong",  580, 655 );
    }

	// 정보동의
	function popupInfomation() {
		var form1 = document.getElementById("form1");

		var params = "?p_subj=" + form1["p_subj"].value +
		             "&p_year=" + form1["p_year"].value +
		             "&p_subjseq=" + form1["p_subjseq"].value;

	    upWin('/learn/user/homepage/zu_student_notice.jsp'+params, 550, 630);
	}

    // 나의 진도보기
    function popupEduList(){
        var url = "/servlet/controller.lcms.EduStart";
    	popup( "popupGoyongNotice", url, "eduList",  800,600 );
    }

	// 학습시작하기
	function studyStart(start_gubun){

	<%
		if ( !allowStudy ) {
			if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") ) {
				String starting = (String) request.getAttribute("starting");
	%>
		top.etop.starting(<%=starting%>, start_gubun);
	<%
			} else if ( v_contenttype.equals("S") ) {
	%>
		top.openTree();
		top.launch( "", "" );
	<%
			} else if ( v_contenttype.equals("K") ) {
	%>
    	var form1 = document.getElementById("form1");
	    form1["p_process"].value = "lessonListAx";

	    var target ="lessonListAx";
		var action = "/servlet/controller.lcms.KTEduStart";
		var params = collectParams("form1");

   		new Ajax.Updater(target, action, { method: 'post', parameters: params });
	<%
			}
		} else {
	%>
		alert('학습시간제한에 걸렸습니다.');
	<%
		}
	%>
	}

	// 학습시작하기 - 학습시간제한 적용
	function goStart(start_gubun) {

		var url = "/servlet/controller.lcms.EduUtil";
		var params = "p_process=limitStudyTime";
		var myAjax = new Ajax.Request( url,	{
				method : 'post'	,parameters : params, Loading : true,
				onSuccess : function (result) {
					var msg = result.responseText;

					if (msg == "true") {
						studyStart(start_gubun);
						return;
					} else {
						alert( msg );
						return;
					}
				}         
			} );
	}

	// 초기화
	function init() {
		<%= isSulpaper?"popupSulpaper();":"" %>
		<%= isInfoAgree?"popupInfomation();":"" %>
		<%= isGoyong?"popupGoyong();":"" %>
	}

	// 메인
	function goMain() {
		var form1 = document.getElementById("form1");
		form1.action = "/servlet/controller.lcms.EduStart";
        form1["p_process"].value = "fmain";
		form1.target = "_self";
        form1.submit();
	}

	var w = 800;
	var h = 660;
	// 공지사항 목록화면으로 이동
	function gongList() {
		window.open("","gongList","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width="+ w + ",height=" + h).focus();

        document.form1.action = "/servlet/controller.study.SubjGongServlet";
        document.form1.p_process.value = "selectList";
        document.form1.p_tabseq.value= "";
        document.form1.target="gongList";
        document.form1.submit();
    }

	// 공지사항 상세화면으로 이동
	function gongSelect(seq) {
		window.open("","gongView","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width="+ w + ",height=" + h).focus();

        document.form1.action = "/servlet/controller.study.SubjGongServlet";
        document.form1.p_process.value = "selectView";
        document.form1.p_seq.value = seq;
        document.form1.target="gongView";
        document.form1.submit();
    }

	// 과정별 자료실 목록화면으로 이동
	function sdList() {
		window.open("","sdList","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width="+ w + ",height=" + h).focus();

        document.form1.action = "/servlet/controller.study.StudyDataServlet";
        document.form1.p_process.value = "list";
        document.form1.p_tabseq.value= "";
        document.form1.target="sdList";
        document.form1.submit();
    }

	// 과정별 자료실 상세화면으로 이동
	function sdSelect(tabseq, seq) {
		window.open("","sdView","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width="+ w + ",height=" + h).focus();

        document.form1.action = "/servlet/controller.study.StudyDataServlet";
        document.form1.p_process.value = "select";
        document.form1.p_seq.value = seq;
        document.form1.p_tabseq.value= tabseq;
        document.form1.target="sdView";
        document.form1.submit();
    }

    // 과정별 질문방 목록화면으로 이동
	function sqList() {
		window.open("","sdList","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width="+ w + ",height=" + h).focus();

        document.form1.action = "/servlet/controller.study.SubjQnaStudyServlet";
        document.form1.p_process.value = "list";
        document.form1.p_tabseq.value= "";
        document.form1.target="sdList";
        document.form1.submit();
    }

	// 과정별 질문방 상세화면으로 이동
	function sqSelect(tabseq, seq) {
		window.open("","sdView","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width="+ w + ",height=" + h).focus();

        document.form1.action = "/servlet/controller.study.SubjQnaStudyServlet";
        document.form1.p_process.value = "SubjQnaDetail";
        document.form1.p_seq.value = seq;
        document.form1.p_tabseq.value= tabseq;
        document.form1.target="sdView";
        document.form1.submit();
    }
    
	// 강사 정보 div 보이기 설정
	function viewToturInfo(action){
		if (action == "over") {
			document.getElementById("tutorInfo").style.display = "block";
		} else {
			document.getElementById("tutorInfo").style.display = "none";
		}
		document.getElementById("tutorInfo").style.position ="absolute";
		document.getElementById("tutorInfo").style.zIndex ="9999";
		document.getElementById("tutorInfo").style.top ="20px";
		document.getElementById("tutorInfo").style.left ="-10px";
	}
	
	function examWright(plessonstart, p_lesson, p_examtype, p_papernum, p_userretry){
		if(confirm("평가 기간동안 재응시횟수에 남은 만큼 재응시가 가능합니다.\n평가지 상단의 시간이 0이 되기전에 제출하셔야 합니다.\n시간이 0이 되면 자동으로 시스템에서 제출합니다.")){
			farwindow = window.open("", "ExamUserWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes, resizable=yes,copyhistory=no, width=680, height=600, top=0, left=0");
		    document.getElementById("form1").target = "ExamUserWindow";
		    document.getElementById("form1").action = "/servlet/controller.exam.ExamUserServlet";
		    document.getElementById("form1").p_process.value = 'ExamUserPaperListPage';
		    document.getElementById("form1").p_lessonstart.value = plessonstart;  // 차시 시작 
		    document.getElementById("form1").p_lesson.value = p_lesson;
		    document.getElementById("form1").p_examtype.value = p_examtype;
		    document.getElementById("form1").p_papernum.value = p_papernum;
		    document.getElementById("form1").p_userretry.value = p_userretry;
			document.getElementById("form1").submit();
			document.getElementById("form1").target = window.self.name;
		}
	}

	// 결과보기
	function IndividualResult(lesson, type, papernum) {
		  window.self.name = "winIndividualResult";
		  farwindow = window.open("", "openIndividualResult", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes, resizable=yes,copyhistory=no, width=680, height=600, top=0, left=0");
		  document.getElementById("form1").target = "openIndividualResult";
		  document.getElementById("form1").action = "/servlet/controller.exam.ExamUserServlet";
		  document.getElementById("form1").p_process.value = "ExamUserPaperResult";
	
		  document.getElementById("form1").p_lesson.value     = lesson; 
		  document.getElementById("form1").p_examtype.value   = type; 
		  document.getElementById("form1").p_papernum.value   = papernum; 
		  
		  document.getElementById("form1").submit();
		  farwindow.window.focus();
		  document.getElementById("form1").target = window.self.name;
	}
	
	// 과제제출기한 지난경우
  function whenBasicScore(v_ordseq, v_weeklyseq, v_weeklysubseq) {
  		window.open("","whenBasicScore","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width=670,height=650").focus();
  		document.form1.action = "/servlet/controller.learn.ReportStuServlet";
	 	document.form1.p_ordseq.value = v_ordseq;
		document.form1.p_weeklyseq.value = v_weeklyseq;
		document.form1.p_weeklysubseq.value = v_weeklysubseq;
  		document.form1.p_process.value = "viewBasicScore";
		document.form1.target = "whenBasicScore";
  		document.form1.submit();
  }

  // 과제 작성
  function whenInsert(v_ordseq, v_weeklyseq, v_weeklysubseq) {
  		window.open("","whenInsert","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width=670,height=650"),focus();
  		document.form1.action = "/servlet/controller.learn.ReportStuServlet";
		document.form1.p_ordseq.value = v_ordseq;
		document.form1.p_weeklyseq.value = v_weeklyseq;
		document.form1.p_weeklysubseq.value = v_weeklysubseq;
  		document.form1.p_process.value = "insertPage";
		document.form1.target = "whenInsert";
  		document.form1.submit();
  }
    // 과제 제출한 경우 조회
  function whenDetail(v_ordseq) {
  		window.open("","whenDetail","toolbar=no,statusbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=YES,resizable=yes,width=670,height=650"),focus();
  		document.form1.action = "/servlet/controller.learn.ReportStuServlet";
		document.form1.p_ordseq.value = v_ordseq;
  		document.form1.p_process.value = "view";
		document.form1.target = "whenDetail";
  		document.form1.submit();
  }

</script>
</head>
<body onload="init();">
<form id="form1" name="form1" method="post">
    <input type="hidden" name="p_process">
    <input type="hidden" name="p_subj" value="<%=v_subj%>">
    <input type="hidden" name="p_year" value="<%=v_year%>">
    <input type="hidden" name="p_subjseq" value="<%=v_subjseq%>">
    <input type="hidden" name="p_contenttype" value="<%=v_contenttype%>">
    <input type="hidden" name="p_studytype" value="<%=v_studytype%>">

    <input type="hidden" name="p_subjgubun" value="<%=v_subjGubun%>">

    <input type="hidden" name="p_course_code" value="<%=courseCode%>">
    <input type="hidden" name="p_org_id" value="<%=orgID%>">
	<!--  게시판 정보 -->
	<input type="hidden" name="p_tabseq">
	<input type="hidden" name="p_seq">
	<!-- 리포트 정보 -->
	<input type="hidden" name="p_ordseq" value="">
	<input type="hidden" name="p_reptype" value="P">	
	<input type="hidden" name="p_weeklyseq" value="">
	<input type="hidden" name="p_weeklysubseq" value="">
	<input type="hidden" name="p_isreport" value="Y">
	<input type="hidden" name="p_class" value="1">
	<input type="hidden" name="p_grcode" value="N000001">
	
	<input type="hidden" name="p_lessonstart" value="">
	<input type="hidden" name="p_lesson" value=""> 
	<input type="hidden" name="p_examtype" value=""> 
	<input type="hidden" name="p_papernum" value=""> 
	<input type="hidden" name="p_userretry" value="">
    <input type="hidden" name="p_gyear" value="<%=v_year%>">
	<input type="hidden" name="p_userid" value="<%=box.getSession("userid")%>">
<div class="mainAlign">
  <div class="mainCenter">
	<!-- s: mainVisual -->
	<div class="mainVisual2">
	  <!-- lessonListAx -->
	  <div id="lessonListAx">
		<div class="boardGroup">
			<div class="boardNotice">
				<h2><img src="/images/user/k/tit_h204.gif" alt="공지사항" /></h2>
				<% if ( isBBSAuth ) { %><a href="#none" onclick="javascript:gongList()" class="more"><img src="/images/user/k/more.gif" alt="더보기" /></a><% } %>
				<ul>
					<%
						String v_title = "";
						String v_ldate = "";
						int v_seq = 0;
						ArrayList selectGongList = (ArrayList) request.getAttribute("selectGongList");
						if(selectGongList!=null && selectGongList.size() > 0) {
							for(int i=0; i<selectGongList.size(); i++) {
							DataBox dbox = (DataBox) selectGongList.get(i);
							v_title = dbox.getString("d_title");
							v_ldate = dbox.getString("d_addate");
							v_seq = dbox.getInt("d_seq");
							if(v_title.length() > 22)
								v_title = v_title.substring(0,21) + "...";
					%>
					<li <%= i== selectGongList.size()-1 ? "class='liEnd'" : ""  %>><a href="#none" onclick="javascript:gongSelect('<%= v_seq %>')"><%= v_title %><span class="boardDate"><%= FormatDate.getFormatDate(v_ldate, "yyyy.MM.dd") %></span></a></li>
					<%
							}
						} else {
					%>
					<li class="liEnd">해당 항목이 없습니다.</li>
					<%
						}
					%>
				</ul>
			</div>
			<div class="boardData">
				<div class="boardDataTit">
					<h2><img src="/images/user/k/tit_h205.gif" alt="자료실" /></h2>
					<% if ( !v_year.equals("2000") && !v_year.equals("PREV") ) { %><a href="#none" onclick="javascript:sdList()" class="more"><img src="/images/user/k/more.gif" alt="더보기" /></a><% } %>
					<img src="/images/user/k/img_boardData.gif" alt="학습과 관련된 유익한 자료들을 모아 놨습니다" class="imgData" />
				</div>
				<ul>
					<%
						int v_tabseq = 0;
						ArrayList sdList = (ArrayList) request.getAttribute("sdList");
						if(sdList!=null && sdList.size() > 0) {
							for(int i=0; i<sdList.size(); i++) {
							DataBox dbox = (DataBox) sdList.get(i);
							v_title = dbox.getString("d_title");
							v_ldate = dbox.getString("d_ldate");
							v_tabseq = dbox.getInt("d_tabseq");
							v_seq = dbox.getInt("d_seq");
							if(v_title.length() > 22)
								v_title = v_title.substring(0,21) + "...";
					%>
					<li <%= i== sdList.size()-1 ? "class='liEnd'" : ""  %>><a href="#none" onclick="javascript:sdSelect('<%= v_tabseq %>', '<%= v_seq %>')"><%= v_title %><span class="boardDate"><%= FormatDate.getFormatDate(v_ldate, "yyyy.MM.dd") %></span></a></li>
					<%
							}
						} else {
					%>
					<li class="liEnd">해당 항목이 없습니다.</li>
					<%
						}
					%>
				</ul>
			</div>
			<div class="boardQa">
				<div class="boardQaTit">
					<h2><img src="/images/user/k/tit_h206.gif" alt="QNA" /></h2>
					<% if ( isBBSAuth ) { %><a href="#none" onclick="javascript:sqList()" class="more"><img src="/images/user/k/more.gif" alt="더보기" /></a><% } %>
					<img src="/images/user/k/img_boardQa.gif" alt="학습과 관련된 궁금사항이 있으시면 바로 질문해 주세요" class="imgData" />
				</div>
				<ul>
					<%
						ArrayList sqList = (ArrayList) request.getAttribute("sqList");
						if(sqList!=null && sqList.size() > 0) {
							for(int i=0; i<sqList.size(); i++) {
							DataBox dbox = (DataBox) sqList.get(i);
							v_title = dbox.getString("d_title");
							v_ldate = dbox.getString("d_ldate");
							v_tabseq = dbox.getInt("d_tabseq");
							v_seq = dbox.getInt("d_seq");

							if ( v_title.startsWith("<font color=blue><b>[튜터답변]</b></font>") ) {
								if(v_title.length() > 55) {	
									v_title = v_title.substring(0,54) + "...";
								}
							} else {
								if(v_title.length() > 22) {
									v_title = v_title.substring(0,21) + "...";
								}
							}
					%>
					<%
						if ("Y".equals(dbox.getString("d_chk"))) {// 비공개시 본인만 확인
 					%>
							<li <%= i== sqList.size()-1 ? "class='liEnd'" : ""  %>><a href="#none" onclick="javascript:sqSelect('<%= v_tabseq %>', '<%= v_seq %>')"><%= v_title %><span class="boardDate"><%= FormatDate.getFormatDate(v_ldate, "yyyy.MM.dd") %></span></a></li>
					<%
						}else{
					%>	
							<li <%= i== sqList.size()-1 ? "class='liEnd'" : ""  %>><a href="#" onclick="javascript:alert('본인글 또는 공개글만 조회가능 합니다.');"><%= v_title %><span class="boardDate"><%= FormatDate.getFormatDate(v_ldate, "yyyy.MM.dd") %></span></a></li>
					<%
						}
					%>
					<%
							}
						} else {
					%>
					<li class="liEnd">해당 항목이 없습니다.</li>
					<%
						}
					%>
				</ul>
			</div>
		</div>
      </div>
	  <!-- lessonListAx -->
		<div class="courseGroup">
			<h2><img src="/images/user/k/tit_h207.gif" alt="과정상세정보" /></h2>
			<a href="javascript:top.etop.goContents();"><img src="/images/user/k/btn_courseStart.gif" alt="학습시작하기" class="courseStart" /></a>
			<div class="course">
				<dl class="course_04">
					<dt>
					<% if (tutorInfo == null) { %>
						<%=StringManager.cutZero(v_subjseq)%>기 담당강사 정보가 없습니다.
						<!-- 
						<img src="/images/user/k/btn_lecturer_view.jpg" alt="강사정보" />
						 -->
					<% } else { %>
						<%=StringManager.cutZero(v_subjseq)%>기 담당강사는 <b><%=tutorInfo.getString("d_name")%></b>님입니다.
						<!-- 
						<a href="#none" onclick="viewToturInfo('over');" ><img src="/images/user/k/btn_lecturer_view.jpg" alt="강사정보" /></a>
						 -->
					<% } %>
					</dt>
					<dd></dd>
					<% if (tutorInfo != null) { %>
					<!-- s: 강사정보 팝업레이어 class="pop_lecturer" -->
					<div class="pop_lecturer" id="tutorInfo">
						<div class="bg_lecturer_top"></div>
						<div class="bg_lecturer_mid">
							<dl>
								<dd class="close"><a href="#none" onclick="viewToturInfo('out');"><img src="/images/user/c/btn_closeSmall.gif" alt="닫기"/></a></dd>
								<dt>* 근무부서 :&nbsp;</dt>
								<dd><%=tutorInfo.getString("d_position_nm")%></dd>
								<dt>* 전화번호 :&nbsp;</dt>
								<dd><%=tutorInfo.getString("d_hometel")%></dd>
								<dt>* 핸&nbsp;드&nbsp;폰 :&nbsp;</dt>
								<dd><%=tutorInfo.getString("d_handphone")%></dd>
								<dt>* 이&nbsp;메&nbsp;일 :&nbsp;</dt>
								<dd><%=tutorInfo.getString("d_email")%></dd>
							</dl>
						</div>
						<div class="bg_lecturer_btm"></div>
					</div>
					<!-- e: 강사정보 팝업레이어 -->
					<% } %>
				</dl>
				<dl class="course_01">
					<dt>나의 진도율 : </dt>
					<dd>
						<span class="grap01Color"><%=v_progress%>%</span>
						<ul><li><img src="/images/user/k/bg_grap01.gif" alt="" width="<%=v_progress+1%>%" height="10" /></li></ul>
					</dd>
					<dt>권장 진도율 : </dt>
					<dd>
						<span class="grap02Color"><%=v_promotion%>%</span>
						<ul><li><img src="/images/user/k/bg_grap02.gif" alt="" width="<%=v_promotion+1%>%" height="10" /></li></ul>
					</dd>
				</dl>
				<dl class="course_02">
					<dt>남은 학습기간 : </dt>
					<dd>
					<%
						if(sd != null) {
							if(Integer.parseInt(FormatDate.getDate("yyyyMMdd")) <= Integer.parseInt(FormatDate.getFormatDate(sd.getEduend(),"yyyyMMdd"))) {
					%>
						<%=FormatDate.getDate("yyyy.MM.dd")%>~<%=FormatDate.getFormatDate(sd.getEduend(),"yyyy.MM.dd")%>
					<%
							} else {
								out.println("학습이 종료되었습니다.");
							}
						}
					%>
					</dd>
				</dl>
				<dl class="course_03">
					<dt>차시진도(학습차시/총차시) : </dt>
					<dd><%=v_edudatecnt%>/<%=v_datecnt%> <!-- (<%//=(v_wstep_edudatecnt-v_edudatecnt) < 1 ? "0" : String.valueOf(v_wstep_edudatecnt-v_edudatecnt) %>차시 부족) --></dd>
				</dl>
			</div>
		</div>
    </div>
    <br>
    
    <table summary="학습진행상황" width="100%">
		<caption>:+: 학습진행상황 :+:</caption>
		<colgroup>
			<col width="15%" align="center" />
			<col width="30%" align="center" />
			<col width="15%" align="center" />
			<col width="15%" align="center" />
			<col width="15%" align="center" />
			<col width="10%" align="center" />
		</colgroup>
		<tbody>
			<tr>
				<td colspan="7"> <B>학습진행상황</B>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				*시험에 응시하시려면 응시가능 글자를 클릭하십시오.</td>
			</tr>
			<%
			    ArrayList    examdata = (ArrayList)request.getAttribute("ExamData");
				ArrayList    suldate = (ArrayList)request.getAttribute("Suldate");
				ArrayList    examresultdata = (ArrayList)request.getAttribute("ExamResultData");
				ArrayList    examconditiondata = (ArrayList)request.getAttribute("ExamConditionData");
				ArrayList    examstatus = (ArrayList)request.getAttribute("ExamStatusData");
				System.out.println(examstatus.size());

				if ( examdata != null ) {
			%>
			<tr>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;"></th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">기간</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">응시가능횟수</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">점수</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">응시상태</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">결과</th>
			</tr>
			<tr>
				<th  style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">온라인시험</th>
				<%
						// 응시전조건
						// 1. 응시가능기간 전인가? => TERMYN = 'B' 
						// 응시가능조건
						// 1. 응시가능기간인가? => TERMYN = 'Y' 
						// 2. 재응가능횟수가 0보다 큰가? USERRETRY
						// 응시완료조건
						// 1. 응시가능기간 종료인가? => TERMYN = 'A' 
						// 2. 응시를 했는가? ended not null   (점수, SCORE)
						// 미응시조건
						// 1. 응시가능기간 종료인가? => TERMYN = 'A' 
						// 2. 응시를 했는가? ended is null 
								
						// ### 실제 문제지 올릴때 해당 문제지 날짜 확인 할것
								
						if ("0".equals(examdata.get(2))) {
							out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
							out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
							out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
							out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
							out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
						} else {
							if(examstatus.get(2) != null){ 
								DataBox examdbox2 = (DataBox) examstatus.get(2);
								String term = examdbox2.getString("d_termyn");
								String startdt = examdbox2.getString("d_startdt");
								if (!"".equals(startdt)) {
									//startdt = FormatDate.getFormatDate(examdbox2.getString("d_startdt"),"yyyy-MM-dd")+" 00:00:00";
									startdt = FormatDate.getFormatDate(examdbox2.getString("d_startdt"),"yyyy-MM-dd");
								}
								String enddt = examdbox2.getString("d_enddt");
								if (!"".equals(enddt)) {
									//enddt = FormatDate.getFormatDate(examdbox2.getString("d_enddt"),"yyyy-MM-dd")+" 23:59:59";
									enddt = FormatDate.getFormatDate(examdbox2.getString("d_enddt"),"yyyy-MM-dd");
								}
								String retry = examdbox2.getString("d_userretry");
								String oriretry = examdbox2.getString("d_retrycnt");
								String ended = examdbox2.getString("d_ended");
								
								out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">"+startdt+" ~ "+enddt+"</td>");
								
								// 시험시작전
								if ("B".equals(term)) {
									if ("0".equals(oriretry)) {
										oriretry = "1";
									}
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">"+oriretry+"</td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\"><span class=\"txtPoint\">시험기간전</span></td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
								// 응시가능
								} else if ("Y".equals(term) && (!"0".equals(retry) || ("0".equals(retry) && "".equals(ended)))) {
									out.println("<td align=\"center\">"+retry+"</td>");
									if (!"0".equals(retry)) {
										out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">"+examdbox2.getString("d_score")+"</td>");
									} else {
										out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
									}
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\"><span class=\"txtPoint\" onClick=\"javascript:examWright('"+examdbox2.getString("d_lessonstart")+"', '"+examdbox2.getString("d_lesson")+"', '"+examdbox2.getString("d_examtype")+"', '"+examdbox2.getString("d_papernum")+"', '"+examdbox2.getString("d_userretry")+"')\" style=\"cursor:hand\">응시가능</span></td>");
									if (!"0".equals(retry)) {
										out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\"><span onClick=\"javascript:IndividualResult('"+examdbox2.getString("d_lesson")+"', '"+examdbox2.getString("d_examtype")+"', '"+examdbox2.getString("d_papernum")+"')\" style=\"cursor:hand\">결과</span></td>");
									} else {
										out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
									}
								// 응시완료
								} else if (("A".equals(term) && !"".equals(ended)) || ("Y".equals(term) && "0".equals(retry))) {
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">"+examdbox2.getString("d_score")+"</td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\"><span class=\"txtPoint\">응시완료</span></td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\"><span onClick=\"javascript:IndividualResult('"+examdbox2.getString("d_lesson")+"', '"+examdbox2.getString("d_examtype")+"', '"+examdbox2.getString("d_papernum")+"')\" style=\"cursor:hand\">결과</span></td>");
								// 미응시
								} else if ("A".equals(term) && "".equals(ended)) {
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\"><span class=\"txtPoint\">미응시</span></td>");
									out.println("<td align=\"center\" style=\"line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;\">-</td>");
								// 데이터 없음
								} else {
									out.println("-");
								}
						}
				}
%>
			</tr>
			<!-- 설문 -->
			<tr>
				<td colspan="6" style="height:1px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;"></td>
			</tr>
			<tr>
				<th  style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">설문</th>
			<% 
			if( suldate != null && suldate.size() > 0 ){
			 DataBox suldatedbox = (DataBox) suldate.get(0);
			 v_aftersulsdate = suldatedbox.getString("d_aftersulsdate");
			 v_aftersuledate = suldatedbox.getString("d_aftersuledate");
			
				if(Integer.parseInt(FormatDate.getFormatDate(v_aftersulsdate,"yyyyMMdd")) <= Integer.parseInt(FormatDate.getRelativeDate(0))
						&& Integer.parseInt(FormatDate.getFormatDate(v_aftersuledate,"yyyyMMdd")) >= Integer.parseInt(FormatDate.getRelativeDate(0)))//학습종료일 오늘날짜와 비교
				{
			%>
				<td  style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" align="center">
				 <%=FormatDate.getFormatDate(v_aftersulsdate,"yyyy-MM-dd") %> ~ <%= FormatDate.getFormatDate(v_aftersuledate ,"yyyy-MM-dd")%>
				 </td>
					<% if ("0".equals(box.getString("p_suldata"))) { %>
						-
					<% } else { %>
					<%	if(!box.getString("p_suldata").equals(box.getString("p_sulresult"))){ %>
								<%//= box.getString("p_suldata") + " 개중 " + box.getString("p_sulresult") + " 개 "%>  
								
								
								<td align="center" style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" colspan="4">
								<a href="javascript:popupSulpaper();">[응시가능]</a></td>
					<%		} else{ %>
								<%//= box.getString("p_suldata") + " 개중 " + box.getString("p_sulresult") + " 개 "%> 
								<td align="center" style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" colspan="4">
								[응시완료]</td>
					<%		}
					 	} %>
				
			<% 
				}else{  
			
			%>
				<td  style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" colspan="5" align="center">-</td>
			<%  
				} 
			%>
			</tr>
			<% 
			}else{
				
			%>
				<td  style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" colspan="5" align="center">-</td>
			<%  
			}
				
			}
			%>
			<!-- 설문 -->
			<!-- 반복 -->
		</tbody>
	</table>
    <br>
    <table summary="과제물 출제현황" width="100%">
		<caption>:+: 과제물 출제현황 :+:</caption>
		<colgroup>
			<col width="5%" align="center" />
			<col width="40%" align="center" />
			<col width="20%" align="center" />
			<col width="10%" align="center" />
			<col width="10%" align="center" />
			<col width="5%" align="center" />
			<col width="10%" align="center" />
		</colgroup>
		<tbody>
			<tr>
				<td colspan="7"> <B>과제물 출제현황</B></td>
			</tr>
			<tr>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">NO</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">과제제목</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">제출기한</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">상태</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">조회</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">채점</th>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">점수</th>
			</tr>
			<tr>
				<th scope="col" style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">추가제출기한</th>
			</tr>
			<!-- 반복 -->
			<%
	        if(list != null && list.size() > 0) {
	          
	            int v_dispnum = 0;
	            String v_ordseq = "";
	            String v_weeklyseq = "";
	            String v_weeklysubseq = "";
	            v_title = "";
	            String v_startdate = "";
	            String v_expiredate = "";
	            String v_restartdate = "";
	            String v_reexpiredate = "";
	            String v_score = "";
	            String v_submityn = "";
	            String v_isopen = "";
	            String v_indate = "";
	            String v_adddate = "";
				String  v_user_score = "";
	          for(int i=0; i<list.size(); i++) {
	
	            DataBox dbox = (DataBox)list.get(i);  
	
	            v_ordseq        = dbox.getString("d_ordseq");
	            v_weeklyseq     = dbox.getString("d_weeklyseq");
	            v_weeklysubseq  = dbox.getString("d_weeklysubseq");
	            v_title         = dbox.getString("d_title");
	            v_startdate     = dbox.getString("d_startdate");
	            v_expiredate    = dbox.getString("d_expiredate");
	            v_restartdate   = dbox.getString("d_restartdate");
	            v_reexpiredate  = dbox.getString("d_reexpiredate");
	            v_score         = dbox.getString("d_score");
	            v_submityn      = dbox.getString("d_submityn");
	            v_isopen        = dbox.getString("d_isopen");
	            v_indate        = dbox.getString("d_indate");
	            v_adddate       = dbox.getString("d_adddate");
	            v_user_score    = dbox.getString("d_user_score");
	            v_dispnum       = list.size() - i;
	
	            %>
			<tr>
				<th  style="height:22px; font-weight:normal; color:#fff; background:#9db4c4; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;"  rowspan="2"><%=v_dispnum%></th>
				<td  style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">&nbsp;&nbsp;<%=v_title%></td>
				<td  style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" align="center">
				<%if(v_indate.equals("Y")){	//제출기한내인 경우 %>
                  <font color="blue"><%= FormatDate.getFormatDate(v_startdate.substring(0,8),"yyyy/MM/dd") %> ~ <%= FormatDate.getFormatDate(v_expiredate.substring(0,8),"yyyy/MM/dd") %></font>
                <%}else{	%>
                  <%= FormatDate.getFormatDate(v_startdate.substring(0,8),"yyyy/MM/dd") %> ~ <%= FormatDate.getFormatDate(v_expiredate.substring(0,8),"yyyy/MM/dd") %>
                <%}%>
				</td>
				<td  align="center" style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">
				<%if(v_submityn.equals("Y")){%>
                  	제출<!-- 제출 -->
                <%}else{%>
                  	미제출<!-- 미제출 -->
                <%}%>  
				</td>
				<td  align="center" style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">
				<%if(v_submityn.equals("Y")){	//과제제출한 경우 조회%>
                  <span class="btn_inner_gr" onClick="whenDetail('<%=v_ordseq%>');"><a href="#none"><img src="/images/user/button/b_j.gif" border="0"><!--조회--></a></span>
                  <%
                  }else{		//과제 미제출
                    if( v_indate.equals("Y") || v_adddate.equals("Y") ){	//과제제출기한인 경우 제출화면
                      %>
                      <a href="#none"><span class="btn_inner_gr" onClick="whenInsert('<%=v_ordseq%>','<%=v_weeklyseq%>','<%=v_weeklysubseq%>');">
                      		<img src="/images/user/button/b_j.gif" border="0"><!--조회--></span></a>
                      <%
                    }else{	//과제제출기한 지난경우 기본 점수를 보여준다.
                      %>
                      <span class="btn_inner_gr" onClick="whenBasicScore('<%=v_ordseq%>','<%=v_weeklyseq%>','<%=v_weeklysubseq%>');">
                      		<img src="/images/user/button/b_j.gif" border="0"><!--조회--></span>
                      <%
                    }
                  }
                %>	
				</td>
				<td  align="center" style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">
				<%if("".equals(v_user_score)||v_user_score==null||"0".equals(v_user_score)){ %>
					X
				<%}else{ %>
					O
				<%} %>
				</td>
				<td  align="center" style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;" rowspan="2">
				<%if("".equals(v_user_score)||v_user_score==null){ %>
					0
				<%}else{ %>
					<%=v_user_score%>
				<%} %>
				</td> 
			</tr>
			<tr>
				<td align="center" style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">
				<%
                  if(v_adddate.equals("Y")){ //추가제출기한내인 경우
                %>
                    <font color="blue"><%= FormatDate.getFormatDate(v_restartdate.substring(0,8),"yyyy/MM/dd") %> ~ <%= FormatDate.getFormatDate(v_reexpiredate.substring(0,8),"yyyy/MM/dd") %></font>
                <%		
                  }else{          		
                    if ( v_restartdate.length() > 0 ){
                %>
                    <%= FormatDate.getFormatDate(v_restartdate.substring(0,8),"yyyy/MM/dd") %> ~ <%= FormatDate.getFormatDate(v_reexpiredate.substring(0,8),"yyyy/MM/dd") %>
                <%
                    }else{
                      out.println("-");
                    }
                  }
                %>
				</td>
			</tr>
			<%
          }
        } else {
          %>
          <tr>
            <td colspan="7" align="center" style="line-height:22px; border-right:1px solid #bdbcc1; border-bottom:1px solid #bdbcc1;">출제된 과제가 없습니다<!-- 출제된 과제가 없습니다. --></td>
          </tr>
          <%
        }
      %>
			<!-- 반복 -->
		</tbody>
	</table>
	<!-- e: mainVisual -->
  </div>
</div>
<%@ include file = "/learn/library/getJspName.jsp" %>
</form>
</body>
</html>
<%!
public String   get_isEducatedTxt(String val){
    if(val.equals("Y"))
        return  "<font color=blue><b>O</b></font>";
    else
        return  "<font color=red><b>X</b></font>";
}

public String   get_examtypenm(String val){
    if(val.equals("M"))         return  "중간 평가";
    else if(val.equals("T"))    return  "최종 평가";
    else                        return  "";
}
public String   get_datatypenm(String val){
    if      (val.equals("STEP"  ))      return  "진도";
    else if (val.equals("MTEST" ))      return  "중간 평가";
    else if (val.equals("FTEST" ))      return  "최종 평가";
    else if (val.equals("REPORT"))      return  "리포트";
    //else if (val.equals("ACT"   ))      return  "교수가점";
    else if (val.equals("ETC1"  ))      return  "토론점수";
    else if (val.equals("ETC2"  ))      return  "토론참여도";

    return " ";
}
public String   get_reason(String val){
    if  (val.equals("Y")){
        return "축하합니다. 수료기준을 통과하셨습니다";
    }else{
        return "수료기준 취득점수/진도율에 미달하여 미수료상태입니다";
    }
}
%>
