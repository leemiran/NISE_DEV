<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/egovframework/com/lcms/len/common/INC_PopHeader.jsp"%>
<%

String strVod_Full_Title = ""; // 강의제목
String strProf_Name		 = ""; //: 강사명
String strProf_Job		 = ""; // 강사직업
String strBook_Name		 = ""; //교재명
String strLec_Content	 = ""; //강의설명
String IntVod_Continue	 = "0"; //이어듣기
String Sample_Chk		 = ""; //샘플강의 여부 (Y-샘플,N-일반)
String MemId			 = ""; //회원아이디
String Screen_W			 = ""; //화면사이즈
String Screen_H			 = "";  //화면사이즈
String VOD_URL			 = "";  // 동영상경로
String VOD_SKIN			 = "D"; //동영상타입 : 일반사이즈 - C, 이론/실기 -> D
String VOD_Skin          = "";
String OrderBY           = "";
String M_LectureID       = "";
String infoValue         = "";


String divine            =   ",";
String protect_val	     = "";
String domain_val		 = "";
String manual_capture	 = "";
String auto_capture	     = "";
String captureurl_val	 = "";
String serverurl_val	 = "";
String activeUrl		 = "";
String activeVersion	 = "";
String activeWidth		 = "";
String activeHeight	      = "";

String IntVod_PlayTime = "0";
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>epasskorea!</title>
<link rel="stylesheet" href="/style/public.css" type="text/css">
<link href="/common/css/main_1.css" rel="stylesheet" type="text/css">
<script type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>

<!--<SCRIPT language="JavaScript" SRC="/Library_New/JScript.js"></SCRIPT>

-->
<script language="javascript">

	function Win_Resize(strType) {

		if (strType == "Default") {
			top.resizeTo(723,790);		//윈도우 리사이즈 : 초기는
		} else if (strType == "Open") {
			top.resizeTo(885,910);		//윈도우 리사이즈 : 실기
		}

	}

</script>

<SCRIPT language="javascript">
<!--
var playerPos=false;
var oldPos = 10;		//클릭시 마우스좌표
var oldPixel = 10;		//클릭시 슬라이더 좌표
var bDrag = false;		//움직임
var bPlay = false;		//플레이중
var all_end_time=0;
var old_xpos=0;

var stime=-1;
var etime=-1;
var eloop=false;


var isCommit = true;

//강의수강타이머
var timercount = <%=IntVod_PlayTime%>;
var speedRate = 1;

function isplaying() {

	if (MediaPlayer.playState == 3)
		return true
	else
		return false
		
}

function counting() {
	if (isplaying()) {
		timercount = timercount + 1 * speedRate;
		
	}
	setTimeout('counting()',1000);	
}


/********************************************************************
재생 관련
********************************************************************/
function play_back(smode) 
{

	
 	stime = document.MediaPlayer.controls.currentPosition;
	if (smode == 1)
	{
		stime = stime - 20;
		if (stime < 0)
			stime = 0;
		MediaPlayer.controls.currentPosition = stime;
	}
	else
	{
		stime = stime + 20;
		if (stime < 0)
			stime = 0;
		MediaPlayer.controls.currentPosition = stime;
	}
}

var progressBar_StartPixel = 82;		//플레이바 시작위치
var progressBar_Size = 314;				//플레이바 영역
function progressBar_onmousedown() 
{
	if (!bPlay)
		return ;
		
	if (document.MediaPlayer.controls.currentPosition == 0)		//Player.FileName이 설정되어 있지 않으면 걍 리턴해 버린다.
		return false;

	oldPos = event.clientX;
	bDrag  = true;
	TrackBar = event.srcElement.parentElement;
	oldPixel = progressBar.style.pixelLeft; 
	document.onmousemove = PlayMoveSlider;
	if(document.all)
	{
		document.onmouseup = PlayStopSlider;
	}
	
	PlayProgressBar.style.width = (progressBar.style.pixelLeft - progressBar_StartPixel);
	

}

function Pixel2Pos(nPixel)
{
		return parseInt((nPixel) *  MediaPlayer.currentMedia.duration / progressBar_Size);
}

function PlayMoveSlider() 
{
	if (bDrag) 
	{
		var XPos = oldPixel + (event.clientX - oldPos);	//최초 마우스다운일때 좌표에서 mousemove한 좌표값
		if((progressBar_StartPixel <= XPos) && (XPos <= progressBar_StartPixel + (progressBar_Size)))	
		{
			//프로그래스바 이동
			document.all.progressBar.style.pixelLeft = XPos;
			if (XPos!=old_xpos)
			{
				playtime.innerHTML="<font color=white>"+TimeFormat(Pixel2Pos(progressBar.style.pixelLeft - progressBar_StartPixel))+" / "+TimeFormat(all_end_time)+"&nbsp;";
				old_xpos=XPos;
			}
		}
		return false;
	}
}




function PlayStopSlider() 
{
	bDrag = false;
		
	document.MediaPlayer.SetCurrentPosition(Pixel2Pos(progressBar.style.pixelLeft - progressBar_StartPixel)); 
	if (document.MediaPlayer.GetPlayState() == 2)	//일시 중지 일때, 즉 스라이더 움직임에 의해 중단되었을 때만, 다시 실행
		 document.MediaPlayer.Play();		
		
	document.onmousemove = null;
	if(document.all);
		document.onmouseup = null;

}	

// 플레이영역 클릭 이동 함수
function PointClick_Play() {
	
	//alert(bPlay)
	
	if(bPlay == false) {
		return false;
	}
	
	progressBar.style.pixelLeft = event.clientX-9;
	
	if(progressBar.style.pixelLeft < progressBar_StartPixel) {
		progressBar.style.pixelLeft = progressBar_StartPixel;
	}

	PlayProgressBar.style.width = (progressBar.style.pixelLeft - progressBar_StartPixel);
	document.MediaPlayer.SetCurrentPosition(Pixel2Pos(progressBar.style.pixelLeft - progressBar_StartPixel));
	
}


var currRate = 1;
var currImag = null;

function fPlayer_setRate(rate)
{
		player_setRate(rate);
}
//onUnLoad 시 ------------------------------------------
function isUnload(){
if(isCommit){
	parent.ContentExecutor.commit(false);
}
}
//플레이 트랙바 자동이동-----------------------------------------------------------------------------------
function ScrollBarState() 
{
    parent.ContentExecutor.playTime =  parseInt(document.MediaPlayer.controls.currentPosition,10);
    
    if( parseInt(document.MediaPlayer.controls.currentPosition,10)+1 >  parseInt($("#eduTime").val(),10)&& isCommit){
    	parent.ContentExecutor.commit(false);
   	    isCommit = false;
   	}
	
	bPlay = true;
	if (eloop==true && document.MediaPlayer.GetCurrentPosition()>=etime)
	{
		document.MediaPlayer.SetCurrentPosition(stime);
	}
	if(bDrag == false)
	{
		all_end_time = MediaPlayer.currentMedia.duration;
		playtime.innerHTML="<font color=white>"+TimeFormat(document.MediaPlayer.controls.currentPosition)+" / "+ TimeFormat(all_end_time)+"&nbsp;";
		
		progressBar.style.pixelLeft = progressBar_StartPixel + parseInt(document.MediaPlayer.controls.currentPosition * progressBar_Size / document.MediaPlayer.currentMedia.duration);
		
		PlayProgressBar.style.width = (progressBar.style.pixelLeft - progressBar_StartPixel);
		
	}
	if(progressBar.style.pixelLeft == 0) 
	{
		progressBar.style.pixelLeft = progressBar_StartPixel; 
		PlayProgressBar.style.width = 0;
	}
}




/*********************************************************************
volume관련
**********************************************************************/

var VolumeBar_StartPixel = 558;		//볼륨시작지점
var VolumeBar_Size = 72;			//볼륨영역크기
var MediaTimer=null;
var MediaDown=null;


// 볼륨아이콘 드래그
function VolumeBar_onmousedown() 
{
	
	// 음소거일경우 이동 불가
	if(document.MediaPlayer.settings.mute ==true) {
		return;
	}
		
	oldPos = event.clientX;							//최초 마우스다운일때 좌표
	oldPixel = VolumeBar.style.pixelLeft;			//최초 마우스다운 일때 슬라이더좌표

	bDrag = true;
	document.onmousemove = VolumeMoveSlider;		//onmousemove캡쳐
	if (document.all)
		document.onmouseup=VolumeStopSlider;		//onmousemove 해제
}

function VolumeMoveSlider() 
{
	if (bDrag) 
	{
		var XPos = oldPixel + event.clientX - oldPos;	//최초 마우스다운일때 좌표에서 mousemove한 좌표값
		
		if ((VolumeBar_StartPixel <= XPos) && (XPos <= VolumeBar_StartPixel + VolumeBar_Size))	
		{
			VolumeBar.style.pixelLeft = XPos;	//마우스 이동한 만큼 슬라이더 이동
			MediaPlayer.settings.volume =(XPos - VolumeBar_StartPixel) / VolumeBar_Size * 100;
			
			PointClick_Volume();
		}
		return false;
	}
}


// 볼륨영역 클릭 이동 함수
function PointClick_Volume() {
	
	// 음소거일경우 이동 불가
	if(document.MediaPlayer.settings.mute==true) {
		return;
	}
	
	VolumeBar.style.pixelLeft = event.clientX-9;
	
	if(VolumeBar.style.pixelLeft < VolumeBar_StartPixel) {
		VolumeBar.style.pixelLeft = VolumeBar_StartPixel
	}

	VolumeProgressBar.style.width = (VolumeBar.style.pixelLeft - VolumeBar_StartPixel);
	
	MediaPlayer.settings.volume =(VolumeBar.style.pixelLeft - VolumeBar_StartPixel)*100/VolumeBar_Size;
}



function VolumeStopSlider() 
{
	bDrag = false;
}

//볼륨아이콘 초기화
function VolumeInit() 
{
	VolumeBar.style.pixelLeft = MediaPlayer.settings.volume / 100 * VolumeBar_Size + VolumeBar_StartPixel;
}

//음소거  ---------------------------------------------------------------------------------------
function setMute() 
{
	if (!bPlay)
		return ;
		
	if(this.MediaPlayer.settings.mute == false)
	{
		VolumeMute.src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_icon_off.gif";
		this.MediaPlayer.settings.mute = true;
	}
	else
	{
		VolumeMute.src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_icon.gif";
		this.MediaPlayer.settings.mute = false;
	}
}


/*********************************************************************
volume관련
**********************************************************************/


/**
 * 기본생성자
 * @param obj:Player 객체
 */
function WindowMediaPlayer()
{
    //this.player = obj;
    document.MediaPlayer.url = "<c:out value="${param.starting}"/>";
    document.MediaPlayer.settings.autoStart = true;
    document.MediaPlayer.settings.rate = 1;
    document.MediaPlayer.controls.currentPosition = <c:out value="${param.location}"/> + 0;    
    this.fnControlType("play");
}


//컨트롤변경
function fnControlType(staus) {	
	
	if (staus == "play" || (staus == "open")) {


        document.MediaPlayer.controls.play();
		fnControlChangeImg(staus);		//이미지변경 호출
		
	} else if (staus == "pause") {		
		
		document.MediaPlayer.controls.pause();
		fnControlChangeImg(staus);		//이미지변경 호출


	} else if (staus == "stop")	{
	
		document.MediaPlayer.controls.stop();
		document.MediaPlayer.controls.currentPositionString = 0;
		
		clearTimeout(MediaTimer);
		MediaTimer = null;
		progressBar.style.pixelLeft = progressBar_StartPixel;
		PlayProgressBar.style.width = 0;

		bPlay = false;
		
		fnControlChangeImg(staus);		//이미지변경 호출

	} else if (staus == "open") {

		VolumeInit();		//볼륨셋팅
		
		fnControlChangeImg(staus);		//이미지변경 호출
		
			
	} else if (staus == "full") {
	
		if (!bPlay) {
		
			alert("정지상태에서는 전체화면보기가 지원되지 않습니다.");
			
			return ;
		}

		document.MediaPlayer.fullScreen = true;
	}

}


//이미지변경
function fnControlChangeImg(staus) {
	
	//플레이
	if(staus == "play") {
		document.images['IMGplay'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_play_on.gif";
	} else {
		document.images['IMGplay'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_play.gif";
	}
	
	//포즈
	if(staus == "pause") {
		document.images['IMGpause'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_pause_on.gif";
	} else {
		document.images['IMGpause'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_pause.gif";
	}
	
	//스탑
	if(staus == "stop") {
		document.images['IMGstop'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_stop_on.gif";
	} else {
		document.images['IMGstop'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_stop.gif";
	}
	
}


//------------------------------------------------------
// 동영상 재생 배속 변경
//------------------------------------------------------
function player_setRate(rate) {

		if(rate==0.5)
			speedColor("0.5");
		
		else if(rate==0.8)
			speedColor("0.8");
		
		else if(rate==1)
			speedColor("1");
		
		else if(rate==1.2)
			speedColor("1.2");
		
		else if(rate==1.4)
			speedColor("1.4");
		
		else if(rate==1.6)
			speedColor("1.6");
		
		else if(rate==1.8)
			speedColor("1.8");
		
		else if(rate==2)
			speedColor("2");

		//this.MediaPlayer.SetRate(rate);

		 this.MediaPlayer.settings.rate = rate;

}

function speedColor(Color) {

	if(Color == "0.5") {
		document.images['speed05'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif";
	} else {
		document.images['speed05'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif";
	}
	
	if(Color == "0.8") {
		document.images['speed08'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif";
	} else {
		document.images['speed08'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif";
	}


	if(Color == "1") {
		document.images['speed1'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif";
	} else {
		document.images['speed1'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif";
	}
	
	if(Color == "1.2") {
		document.images['speed1_2'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif";
	} else {
		document.images['speed1_2'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif";
	}
	
	if(Color == "1.4") {
		document.images['speed1_4'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif";
	} else {
		document.images['speed1_4'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif";
	}
	
	if(Color == "1.6") {
		document.images['speed1_6'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif";
	} else {
		document.images['speed1_6'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif";
	}
	
	if(Color == "1.8") {
		document.images['speed1_8'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif";
	} else {
		document.images['speed1_8'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif";
	}
	
	if(Color == "2") {
		document.images['speed2'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif";
	} else {
		document.images['speed2'].src = "<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif";
	}
}
//-->
</SCRIPT>




<script language="javascript">

//$$$--------------------------------------------- 책갈피 관련 스크립트 -----------------------------------------------------------$$$
var Seconds= 0;
function BookmarkTimeCheck() {

	Seconds = Math.round (document.MediaPlayer.GetCurrentPosition());

	var sc = parseInt(Seconds) % 60;
	var mn = parseInt(Seconds / 60);
	var hr = parseInt(mn / 60);
	BookMarkForm.bmTime.value = hr + ":" + mn + ":" + sc;
	aaa=(hr*600)+(mn*60)+sc
	
	document.BookMarkForm.bm_Time.value = Seconds
	
}


//$$$------------------------------------------- 강의종료 클릭시  --------------------------------------------------------$$$

function vodExit_Before(M_LectureID) {
	document.getElementById('End_Event').value = "Y";
	vodExit(M_LectureID);		//강의시간 저장 스크립트 호출
}
//$$$------------------------------------------- 강의종료 클릭시  --------------------------------------------------------$$$


</script>
</head>

<body background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/page_bg.gif" leftmargin="0" topmargin="0"  onUnload="" onBeforeUnload="" onLoad="WindowMediaPlayer();MM_preloadImages('<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_classinfo_on.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_bookmark.gif_on','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_help_on.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_bookmark_on.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/tab01_on.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/tab02_on.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_play_on.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_pause.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_stop_on.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg07_r.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/right_g_bg01.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg01_r.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg09_r.gif','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_total_bg.gif')" oncontextmenu="javascript: return false;" ondragstart="javascript: return false;" onselectstart="javascript: return false;" >

<input type="hidden" name="StopChk" id="StopChk" value="N"><%//임의로 스탑버튼을 클릭했을경우에는 두번째 파일로 넘어가지 못하게 함%>
<input type="hidden" name="End_Event" id="End_Event" value="N"><%//강의종료 버튼 클릭시%>
<input type="hidden" name="playTime" id="playTime"   value=""><!-- 학습시간  -->
<input type="hidden" name="eduTime" id="eduTime" value="<c:out value="${param.eduTime}"/>"><!-- 동영상 총시간  -->

	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" ID="offsetTable">
		<tr>
			<td valign="top" style="padding:7px 5px 0 7px">

				<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table2">
					<tr>
						<td>
							
							<table width="100%" border="0" cellspacing="0" cellpadding="0" ID="Table3">
								<tr>
									<td valign="top" style="padding:0 0 0 23px">
									<!-- 스킨이미지위치 -->
									</td>
									<td align="right" style="padding:0 37px 4px 0">

									</td>
								</tr>
							</table>
						
						</td>
						<td width="32">&nbsp;</td>
					</tr>
					<tr>
						<td>
<!--------------------------------------------------------- 과정명 ------------------------------------------------------//-->
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table4">
								<tr>
									<td width="15"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg01.gif" width="15" height="15"></td>
									<td background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg01_r.gif"></td>
									<td width="15"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg02.gif" width="15" height="15"></td>
								</tr>
								<tr>
									<td background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg01_b.gif">&nbsp;</td>
									<td bgcolor="a18e76" height="35" style="padding:3px 5px"><!-- 강좌명 -->
								<strong><font color="#000000"><c:out value="${param.contentTitle}"/></font></strong></td>
									<td background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg02_b.gif">&nbsp;</td>
								</tr>
								<tr>
									<td height="5"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg03.gif" width="15" height="5"></td>
									<td background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg03_r.gif"></td>
									<td><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg04.gif" width="15" height="5"></td>
								</tr>
							</table>
<!--------------------------------------------------------- 과정명 ------------------------------------------------------//-->
						</td>
						<td></td>
					</tr>
					<tr>
						<td height="100%" valign="top">
<!--------------------------------------------------------- 화 면 ------------------------------------------------------//-->
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table5">
								<tr>
									<td><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg05.gif" width="10" height="3"></td>
									<td height="3" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_total_bg.gif"></td>
									<td><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg06.gif" width="10" height="3"></td>
								</tr>
								<tr>
									<td width="10" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg05.gif"></td>
									<td height="100%" align="center" valign="middle" bgcolor="#000000" style="padding:5px 5px">



									<object id="MediaPlayer" name="MediaPlayer" 
									classid="clsid:6BF52A52-394A-11D3-B153-00C04F79FAA6" 
									codebase='#Version=7,0,0,1954' standby="영상을 불러오고 있습니다.."
									type="application/x-oleobject" width="100%" height="100%" 
									style="border: 1px solid #000000; padding: 0px; margin: 0px; background-color: #000000" >
									<param name="uimode" value="none">  
									<param name="AutoPlay" value="true">
									<param name="volume" value="50">
									<param name="stretchToFit" value="true">
									<param name="info" value="<%=infoValue%>">
									<param name="disableFullscreenControl" value="true">
								</object> 

								</td>
									<td width="10" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg06.gif"></td>
								</tr>
								<tr>
									<td><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg05.gif" width="10" height="3"></td>
									<td height="3" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_total_bg.gif"></td>
									<td><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg06.gif" width="10" height="3"></td>
								</tr>
							</table>
<!--------------------------------------------------------- 화 면 ------------------------------------------------------//-->
						</td>
						<td valign="top">&nbsp;</td>
					</tr>
					<tr>
						<td valign="top">
							
							<table width="100%" height="9" border="0" cellpadding="0" cellspacing="0" ID="Table7">
								<tr>
									<td width="15"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg07.gif" width="15" height="9"></td>
									<td><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg07_r.gif" width="730" height="9"></td>
									<!-- td background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg07_r.gif">&nbsp;</td-->
									<td width="15"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg08.gif" width="15" height="9"></td>
								</tr>
							</table>
						
						</td>
						<td valign="top"></td>
					</tr>
					<tr>
						<td valign="top">
							
							<table width="100%" border="0" cellspacing="0" cellpadding="0" ID="Table8">
								<tr>
									<!--  td width="19" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg07_b.gif">&nbsp;</td-->
									<td width="15"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg07_b.gif" width="15" height = "80"></td>
									<td valign="top" bgcolor="86755b">
										
										<table border="0" cellspacing="0" cellpadding="0" ID="Table9">
											<tr>
												<td height="21" colspan="5" valign="top" style="padding:3px 0 0 0">
													
													<table width="100%" border="0" cellspacing="0" cellpadding="0" ID="Table10">
														<tr>
															<td width="57" align="center">
<!-- 현재상태 -->
																<strong><font color="#FFFFFF"><span id="Playerstatus">준비</span></font></strong>
																
															</td>
															<td>
																
																<table width="100%" height="7" border="0" cellpadding="0" cellspacing="0" ID="Table11">
																	<tr>
<!--------------------------------------------------------- 플레이 ------------------------------------------------------//-->
																		<td valign="top" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/play_bar_bg.gif" width=314 onmousedown="javascript:PointClick_Play();" style="z-index:1;" ><img id="PlayProgressBar" NAME="PlayProgressBar" style="position:absolute; z-index:2; visibility: visible;  POSITION: absolute;width:0px;" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/play_bar_ing.gif" width="10" height="7"><img id="progressBar" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/play_bar_stic.gif" width="12" height="7" border="0" style="position:absolute; left:82px; z-index:3;" LANGUAGE="javascript" onMouseDown="progressBar_onmousedown()" onMouseOver="this.style.cursor='hand'"></td>
<!--------------------------------------------------------- 플레이 ------------------------------------------------------//-->
																	</tr>
																</table>
															
															</td>

															<td width="135" style="padding:0 0 0 8px"><font color="#FFFFFF">( <strong><span id="playtime" name="playtime"> 00:00 / 00:00 </span></strong> )</font></td>
															
															<td width="92">
																
																<table width="100%" border="0" cellspacing="0" cellpadding="0" ID="Table12">
																	<tr>
																		<td width="7"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_bg01.gif" width="7" height="18"></td>
																		<td align="center" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_bg02.gif">
<!--------------------------------------------------------- 볼 륨 ------------------------------------------------------//-->
																			<table width="92" height="10" border="0" cellpadding="0" cellspacing="0" ID="Table13">
																				<tr>
																					<td width="18" height="10"><img id="VolumeMute" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_icon.gif" width="15" height="10" border="0" onclick="setMute()" style="cursor:hand;z-index:4; "></td>
																					<td valign="top" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_bar_bg.gif" width=100% onmousedown="javascript:PointClick_Volume();">
																					<img id="VolumeProgressBar" NAME="VolumeProgressBar"  style="position:absolute; z-index:5; visibility: visible;  POSITION: absolute;width:33px;" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_bar_ing.gif" width="10" height="10">
																					<img id="VolumeBar" id="VolumeBar" style="position:absolute; z-index:6; visibility: visible;cursor:hand;LEFT: 603px;" onMouseDown="VolumeBar_onmousedown()" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_bar_stic.gif" width="12" height="10" border="0"></td>
																				</tr>
																			</table>
<!--------------------------------------------------------- 볼 륨 ------------------------------------------------------//-->
																		</td>
																		<td width="8"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/volume_bg03.gif" width="8" height="18"></td>
																	</tr>
																</table>
															
															</td>
														</tr>
													</table>
												
												</td>
											</tr>
											<tr>
												<td height="5" colspan="5"></td>
											</tr>
											
											<tr>
												<td height="2" colspan="5" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/line_w.gif"></td>
											</tr>
											<tr>
												<td height="5" colspan="5"></td>
											</tr>
											<tr>
												<td width="120" align="center">
<!-- play -->
													<img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_play.gif" name="IMGplay" width="36" height="36" border="0" onclick="javascript:fnControlType('play');" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('IMG_play','','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_play_on.gif',1)"  style="cursor:hand" >
<!-- pause -->
													<img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_pause.gif" name="IMGpause" width="36" height="36" border="0" onclick="javascript:fnControlType('pause');" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('IMG_pause','','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_pause_on.gif',1)" style="cursor:hand" >
<!-- stop -->
													<img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_stop.gif" name="IMGstop" width="36" height="36" border="0" onclick="javascript:fnControlType('stop');" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('IMG_stop','','<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_stop_on.gif',1)" style="cursor:hand" ></td>
												<td width="22" align="center"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/line_h.gif" width="2" height="23"></td>
												<td width="284">

<!--------------------------------------------------------- 속도조절 ------------------------------------------------------//-->
													<table width="284" border="0" cellspacing="0" cellpadding="0" ID="Table14">
														<tr>
															<td width="32"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_slow.gif" width="27" height="11"></td>
															<td width="3" valign="top" style="padding:3px 0 0 0"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_bg02.gif" width="3" height="3"></td>
															<td align="center" valign="top" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_bg01.gif">
																
																<table width="100%" border="0" cellspacing="0" cellpadding="0" ID="Table15">
																	<tr>
																		<td height="9" align="center" valign="top"><img id="speed05" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif" style="cursor:hand" onclick="player_setRate('0.5')" width="9" height="9" border="0"></td>
																		<td align="center" valign="top"><img name="speed08" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif" style="cursor:hand" onclick="player_setRate('0.8')" width="9" height="9" border="0"></td>
																		<td align="center" valign="top"><img name="speed1" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon_on.gif" style="cursor:hand" onclick="player_setRate('1')" width="9" height="9" border="0"></td>
																		<td align="center" valign="top"><img name="speed1_2" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif" style="cursor:hand" onclick="player_setRate('1.2')" width="9" height="9" border="0"></td>
																		<td align="center" valign="top"><img name="speed1_4" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif" style="cursor:hand" onclick="player_setRate('1.4')" width="9" height="9" border="0"></td>
																		<td align="center" valign="top"><img name="speed1_6" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif" style="cursor:hand" onclick="player_setRate('1.6')" width="9" height="9" border="0"></td>
																		<td align="center" valign="top"><img name="speed1_8" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif" style="cursor:hand" onclick="player_setRate('1.8')" width="9" height="9" border="0"></td>
																		<td align="center" valign="top"><img name="speed2" src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_icon.gif" style="cursor:hand" onclick="player_setRate('2')" width="9" height="9" border="0"></td>
																	</tr>
																	<tr>
																		<td height="17" align="center" valign="bottom"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_05.gif" style="cursor:hand" onclick="player_setRate('0.5')" width="15" height="11"></td>
																		<td align="center" valign="bottom"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_08.gif" style="cursor:hand" onclick="player_setRate('0.8')" width="15"  height="11"></td>
																		<td align="center" valign="bottom"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_10.gif" style="cursor:hand" onclick="player_setRate('1')" width="14" height="11"></td>
																		<td align="center" valign="bottom"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_12.gif" style="cursor:hand" onclick="player_setRate('1.2')" width="14" height="11"></td>
																		<td align="center" valign="bottom"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_14.gif" style="cursor:hand" onclick="player_setRate('1.4')" width="14" height="11"></td>
																		<td align="center" valign="bottom"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_16.gif" style="cursor:hand" onclick="player_setRate('1.6')" width="14" height="11"></td>
																		<td align="center" valign="bottom"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_18.gif" style="cursor:hand" onclick="player_setRate('1.8')" width="14" height="11"></td>
																		<td align="center" valign="bottom"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_20.gif" style="cursor:hand" onclick="player_setRate('2')" width="15" height="11"></td>
																	</tr>
																</table>
															
															</td>
															<td width="3" valign="top" style="padding:3px 0 0 0"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_bg02.gif" width="3" height="3"></td>
															<td width="32" align="right"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/speed_fast.gif" width="27" height="11"></td>
														</tr>
													</table>
<!--------------------------------------------------------- 속도조절 ------------------------------------------------------//-->
												</td>
												<td width="22" align="center"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/line_h.gif" width="2" height="23"></td>
												<td align="center">
<!-- 전체화면보기 -->
													<img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/btn_full.gif" width="89" height="29" border="0" align="absmiddle" onclick="javascript:fnControlType('full');" style="cursor:hand">
<!-- 강의종료 -->
												</td>
											</tr>
										</table>
									</td>
									<!-- td width="19" background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg08_b.gif">&nbsp;</td-->
									<td width="15"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg08_b.gif" width="15" height = "80"></td>
								</tr>
								<tr>
									<td height="9"><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg09.gif" width="15" height="9"></td>
									<td><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg09_r.gif" width="730" height="9"></td>
									<!-- td background="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg09_r.gif">&nbsp;</td-->
									<td><img src="<c:out value="${gsDomainContext}"/>/images/learning/player/Skin_Img/skin_tbg10.gif" width="15" height="9"></td>
								</tr>
							</table>
						</td>
						<td valign="top">&nbsp;</td>
					</tr>
				</table>

			</td>

		</tr>
	</table>
<br><br>

<SCRIPT LANGUAGE="JScript">
var MediaDown=null;
function TimeFormat(totalsecond)
{
	var second = parseInt(totalsecond) % 60;
	var minute = parseInt(totalsecond / 60);
	var hour = parseInt(minute / 60);
	return ((minute < 10)?"0":"")+minute+":" + ((second < 10)?"0":"")+second;
}
</script>


<SCRIPT language="javascript" event="PlayStateChange(state);" for="MediaPlayer">
<!--

	if (MediaTimer!=null) 
	{
	
			try
			{
				clearTimeout(MediaTimer);
				MediaTimer = null;
				progressBar.style.pixelLeft = progressBar_StartPixel;
				PlayProgressBar.style.width = 0;
			}
			catch(e)
			{
				return;
			}
	}

	switch (state) {
		case 1 :	//정지
			
			bPlay = false;
			Playerstatus.innerHTML = "중지됨";
			fnControlChangeImg('stop')		// 컨트롤이미지 초기화
			
			break;
		
		case 2 :	//일시중지
			Playerstatus.innerHTML = "일시중지";
			break;
        case 3 :	//플레이리스트의 제일 처음 시작할 때 발생
			if (MediaTimer==null) 
			{
            	
	            //fulltime = document.MediaPlayer.currentMedia.durationString ;
				//currenttime =  document.MediaPlayer.controls.currentPositionString ;
				//document.MediaPlayer.controls.currentPosition =<%=IntVod_Continue%>;

				MediaTimer = window.setInterval("ScrollBarState()", 100);			
				fnControlChangeImg('play')		// 컨트롤이미지 초기화
				VolumeInit();					// 볼륨위치 초기화
				
				//counting();		// 실질적인 플레이 시간 카운트 (분)
				
			}
			Playerstatus.innerHTML = "재생중";
			break;
		
		case 6 :	// 버퍼
			Playerstatus.innerHTML = "버퍼링";
			break;
		
	}
//-->
</SCRIPT>

<script language="javascript">
	//Win_Resize('Default')		//윈도우 리사이즈
</script>

</body>
</html>