//ActiveX CodeBase String
var gCodeBaseString = "common/component/";

//ActiveX CodeBase Version
//sn3outer, sn3inner, ViewerUtil
//var gCodeBaseVersion = new Array("1,0,0,80", "1,0,0,11", "2,5,0,22");
var gCodeBaseVersion ;

var eduCheckFlag = "N";

//Loading time for delay (step1~step4),  unit: miliseconds
var gDelay4Step={
	"step2":1000, 
	"step3":500
};

//Controller Flash's tooltips
function ShowToolTip()
{
	SetVariable("ToolTip_Play", "재생");
	SetVariable("ToolTip_Pause", "일시 정지");
	SetVariable("ToolTip_Stop", "정지");
	SetVariable("ToolTip_Mute_Active", "음소거");
	SetVariable("ToolTip_Mute_Deactive", "소리");
	SetVariable("ToolTip_Volume", "음량");
	SetVariable("ToolTip_Prev", "이전 슬라이드");
	SetVariable("ToolTip_Sync_Active", "동영상에 슬라이드 맞추기");
	SetVariable("ToolTip_Sync_Deactive", "동영상에 슬라이드 맞추기");
	SetVariable("ToolTip_Next", "다음 슬라이드");
	SetVariable("ToolTip_Swap_Active", "동영상/슬라이드 위치 바꾸기");
	SetVariable("ToolTip_Swap_Deactive", "동영상/슬라이드 위치 바꾸기");
	SetVariable("ToolTip_VideoMax_Max", "동영상 전체 화면");
	SetVariable("ToolTip_VideoMax_Normal", "동영상 기본 화면");
	SetVariable("ToolTip_SlideMax_Max", "슬라이드 전체 화면");
	SetVariable("ToolTip_SlideMax_Normal", "슬라이드 기본 화면");
	SetVariable("ToolTip_Toc", "목차");
	SetVariable("ToolTip_Memo", "메모");
	SetVariable("ToolTip_Print", "인쇄");
	SetVariable("ToolTip_Help", "도움말");
}

//Location(position) of the current slide
function getPagePosition() {
	return gCurListIndex;
}

//Location(position) of the current video
function getWMPPosition() {
	return MP_CurrentPosition();
}

//Full time for video play
function getWMPDuration() {
	if (typeof(gCurContent.totalDuration) == "undefined") {
		return MP_Duration();
	} else {
		return gCurContent.totalDuration;
	}
}

//Total number of slides
function getPageCount() {
	return gListNum;
}

//Content information(summary)
function getContentInfo() {
	return gLectureTitle;		//subject
	return gLectureAuthor;		//writer
}

function afterWinLoad() {
	//do something ... after window.onload() 
}

function afterWinUnload() {
	//do something ... after window.onunload()
	//document.all.ProgressFrame.src = "http://127.0.0.1/writelocation.asp?location="+getWMPPosition();
	if( eduCheckFlag == "N" ){
		alert("winUnload : 재생시간 >> "+getWMPPosition()+",  전체재생시간 >> "+getWMPDuration());
	}
	//alert("winUnload : 진도 >> "+(getWMPPosition() / getWMPDuration()) * 100);
	opener.xinicsEduCheck( getWMPPosition(), getWMPDuration() ); 
}

function afterGotoSlide(PlayMode) {
	//do something ... after GotoSlide()
	var PageCount;
	var PlayTime;
	PlayTime = getWMPDuration();
	PageCount = getPageCount();
	
	//document.all.ProgressFrame.src = "http://127.0.0.1/writePage.asp?PageNo="+getPagePosition()+"&PageCount="+PageCount+"&PlayTime="+PlayTime;
}

function beforeMPDynamicCreate() { 
	//do something ... before MPDynamicCreate() - Windows Media Player Loaded
}

function afterMPDynamicCreate() {
	//do something ... after MPDynamicCreate() - Windows Media Player Loaded
}

function afterPlayReady() {
	//do something ... after PlayReady() - Media file connected and opened on Windows Media Player (only the first run)
}

function afterPlayEnd(playstate) {
	if( playstate == 10 ){ 
		eduCheckFlag = "Y";
		alert("동영상 종료 : "+playstate+", 총시간 : "+ getWMPDuration());
		opener.xinicsEduCheck( getWMPDuration(), getWMPDuration() ); 
	}
	//do something ... after video's play in onPlayStateChange() - Media file stopped play on Windows Media Player (1: stop, 10: end of file)
}

function afterExpandScreen(m_mode, s_mode) {
	//do something ... after ExpandScreen() - viewer resized
	//m_mode: VIDEO/SLIDE, s_mode: SCREEN, EMBED
}

function doInitializedSlidesByCScript(n) {
	//do something ... update 'VisitLog' array with last session logs
	//for(var i=0; i<n; i++) WriteVisitLog(i);
}

function IsSessionCompletedByCScript() {
	//do something ... check the learning objectives (progress)
	return true;

	//return IsObjectivesCompleted(0, 0);	//always completed 
	//return IsObjectivesCompleted(0, 1.0);	//completed if session_time is longger than MP_Duration
	//return IsObjectivesCompleted(1.0, 1.0);	//completed if session_time is longger than MP_Duration and every slides is checked
}

function beforeWinLoad() {
	//do something ... before window.onload() 
}