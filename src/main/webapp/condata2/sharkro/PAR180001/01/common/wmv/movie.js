/* 
ActionSript : 이러닝 개발 템플릿
만 든 이 : nanpjs
만든날짜 : 2014.5
Question : come to nanpjs!!

-- 고화질 저화질 교체, 영상&교안 크로스, 교안만 보기 등 기본 소스가 포함 되어 있습니다. --

*/

// wmv 로더 기본 정보
//-----------------------------------------------------------------------------------------------
var loader_swf_name="./common/wmv/loading.swf"
var loader_swf_width=100
var loader_swf_height=100
var loader_width=67
var loader_height=18
var loader_left=180
var loader_top=150
//-----------------------------------------------------------------------------------------------

// wmv 동영상 기본 정보
//-----------------------------------------------------------------------------------------------
var movie_width="100%"; // 교안 교차 기능 사용을 위해 100%로 고정
var movie_height="100%"; // 교안 교차 기능 사용을 위해 100%로 고정
var movie_layer_width=420; // 영상 레이어 폭
var movie_layer_height=236; // 영상 레이어 높이

var movie_layer_left=500; // 영상 레이어 좌측부터의 위치
var movie_layer_top=75; // 영상 레이어 상단부터의 위치
//-----------------------------------------------------------------------------------------------

//동영상 공통 주소입력
var vod_HD_URL = "./vod/" + pagenum + ".asx";
//var vod_SD_URL = "./vod/" + curPageStr + "_" + lessonInfo[parseInt(curPageStr,10)] + "_SD.asx"; // 저화질 주소( 일반적으로 사용 안함 )
var movie_URL=vod_HD_URL; //시작시에는 HD로 세팅

//차시번호
var lcode=1
var movie_num=0


//고화질 부터 시작
function video_play(){
	vodLayer.style.visibility	=	"visible"; 
	vod_l();
}


//동영상 바꿈
//고화질
function vod_l(){
    var ctime = MediaPlayer1.controls.CurrentPosition;
	MediaPlayer1.URL = vod_HD_URL;
    MediaPlayer1.controls.currentPosition = ctime;
	MediaPlayer1.controls.play();
}
//저화질 ( 일반적으로 사용 안함 )
function vod_s(){
    var ctime = MediaPlayer1.controls.CurrentPosition;
	MediaPlayer1.URL = vod_SD_URL;
    MediaPlayer1.controls.currentPosition = ctime;
	MediaPlayer1.controls.play();
}


var qualityFlag = "high";
function vodChange(){
   if (qualityFlag == "high")
   {
	   alert("저화질 영상으로 전환 됩니다.");
	   vod_s();
	   qualityFlag = "low";
   }else{
	   alert("고화질 영상으로 전환 됩니다.");
	   vod_l();
	   qualityFlag = "high";
   }
	  
}


var isInternetExplorer = navigator.appName.indexOf("Microsoft") != -1;
// Handle all the FSCommand messages in a Flash movie.
function flashVar_DoFSCommand(command, args) {
	var contentsObj = isInternetExplorer ? document.all.contents : document.contents;
	//
	// Place your code here.
	//
	if(command=="full_screen"){
		full_screen();
	}else if(command=="click_download"){
		click_download();
	}
}

// Hook for Internet Explorer.
if (navigator.appName && navigator.appName.indexOf("Microsoft") != -1 && navigator.userAgent.indexOf("Windows") != -1 && navigator.userAgent.indexOf("Windows 3.1") == -1) {
	document.write('<script language=\"VBScript\"\>\n');
	document.write('On Error Resume Next\n');
	document.write('Sub flashVar_FSCommand(ByVal command, ByVal args)\n');
	document.write('	Call flashVar_DoFSCommand(command, args)\n');
	document.write('End Sub\n');
	document.write('</script\>\n');
}


/*
maxs = subject_time.length-1;
ct = new Array();
for (z=1; z<=maxs; z++) {
	ct[z] = 0;
}
*/
function itostr(num) {
	if (num < 10) str = "0";
	else str = "";

	str = str + num;
	return str;
}


var cpos=0;
var cpos_chk="N";

function getCurPosition()
{
	if (MediaPlayer1.playState != 3) return;
	cpos = MediaPlayer1.controls.CurrentPosition;	

	document.ff.ctime.value = cpos; // 학습시간 저장을 위해 ctime으로 전달
	
	//콘트롤바에 진행률 전달
	pos = cpos / MediaPlayer1.currentMedia.duration * 100;
	document.contents.setVariable("curpos", pos);
	//진행시간 표시
	icpos = parseInt(cpos,10);
	mm = parseInt(icpos/60, 10);
	ss = icpos - mm * 60;

	document.contents.setVariable("this_time", cpos)
    document.contents.setVariable("total_time", MediaPlayer1.currentMedia.duration);
	
}
//로딩
function Freeloader(){
	FState = document.MediaPlayer1.PlayState;
	document.contents.setVariable("FStatea", FState)
//alert(FState);
    //프리로더 설정
  	switch (FState){
	case 1 : //명령어 중지시
//		 alert ("1-재생중지") 
		break;
	case 2 :
		//document.all['loader'].style.visibility = 'visible';
	//alert ("프리로더") 
        break;
	case 3 : //플레이리스트의 제일 처음 시작할 때 발생 
		document.all['loader'].style.visibility = 'hidden';
	    break;
	case 5 :
		document.all['loader'].style.visibility = 'visible';
		break;
	case 6 :
		document.all['loader'].style.visibility = 'visible';
		break;
	case 7 :
		document.all['loader'].style.visibility = 'visible';
//		alert('연결중');
		break;
	case 8 :
		break;
	case 9 :
		document.all['loader'].style.visibility = 'visible';
		break;
	case 10 :
		document.all['loader'].style.visibility = 'hidden';

   	    break;
	}
}
function setCurPosition(pos)
{
	MediaPlayer1.controls.CurrentPosition = pos;
	playMovie();
	send_Command('play');	
}

var MediaReady = false;
var OldState = "";

function changePlayState(NewState)
{
	window.setInterval("Freeloader()", 100);
	if (!MediaReady && NewState==3)
	{
		MediaReady = true;
		window.setInterval("getCurPosition()", 100);
	}

	if (NewState == OldState) return;

	if (NewState == 1) {
		send_Command('stop');
		OldState = NewState;
	} else if (NewState == 2) {
		send_Command('pause');
		OldState = NewState;
	} else if (NewState == 3) {
		
		send_Command('play');
		OldState = NewState;
	} else if (OldState == 3 && NewState == 10) {
		send_Command('stop');
		OldState = 1;
	} else if(NewState == 8 ){
        send_Command('stop');
		OldState = 1;
    }
}

function playMovie() {
	MediaPlayer1.controls.play();
}

function stopMovie() {
	MediaPlayer1.controls.stop();
}

function replayMovie() {
	MediaPlayer1.controls.CurrentPosition = 1;
	playMovie();
}
function pauseMovie() {
	MediaPlayer1.controls.pause();
}
function seek(num) {
	MediaPlayer1.controls.CurrentPosition = num;
	playMovie();
}

// 130225 IE 10 버전 fscommand 미지원으로 do_contents_FSCommand 는 사용되지 않음
function do_contents_FSCommand(command, args)
{
	if (!MediaReady) return;

	if (command == "drag") {
		if (OldState == 10) playMovie();
		pos = document.getElementById('MediaPlayer1').currentMedia.duration * args / 100;
		setCurPosition(pos);
	}
	else if (args == "play") {
		playMovie();		
	} else if (args == "pause"){ 
		pauseMovie();
	}
	else if (args == "stop"){
		stopMovie();
	}
	else if (args == "replay"){
		replayMovie();
	}
	else if (command == "seek"){
		seek(args);
	}
	else if (command == "setVolume")
	{
  	    document.getElementById('MediaPlayer1').settings.volume = args;
	}
}
// do_contents_FSCommand 는 사용되지 않음

// FScommand 미사용 버전 (시작)
function drag(args){
		if (OldState == 10) playMovie();
		pos = document.getElementById('MediaPlayer1').currentMedia.duration * args / 100;
		setCurPosition(pos);
}

function click_event(command){
		if (command == "play") {
			playMovie();		
		} else if (command == "pause"){ 
			pauseMovie();
		}
		else if (command == "stop"){
			stopMovie();
		}
		else if (command == "replay"){
			replayMovie();
		}
		else if (command == "seek"){
			seek(args);
		}
}

function setVolume(args){
		document.getElementById('MediaPlayer1').settings.volume = args;
}

// FScommand 미사용 버전 (끝)

function send_Command(args)
{
	document.contents.setVariable("bt_mode", args);
}

function getCookie(name) {
        var arg = name + "=";
        var alen = arg.length;
        var clen = document.cookie.length;
        var i = 0;
        while (i < clen) {
            var j = i + alen;
            if (document.cookie.substring(i, j) == arg)
              return getCookieVal (j);
            i = document.cookie.indexOf(" ", i) + 1;
            if (i == 0) break; 
        }
        return null;
}

function full_screen(){ 
//화면크기 변환 7.0 버전 이상
	if  (document.MediaPlayer1.PlayState == 3){
		alert("'Esc 버튼'을 클릭하시면 원래 크기로 돌아갑니다.");
		document.MediaPlayer1.fullscreen = true; }
	else {
		alert("강의가 재생중일때 전체화면이 가능합니다."); 
	}
} 

function click_download()
{
    window.open("./vod/down/01.zip","_blank","status=yes, scrollbars=yes, resizable=yes, width=500, height=400"); 
} 
//동영상 배속
function player_setRate(no)
{
	MediaPlayer1.settings.rate = no;
}


var WMV_Big    = {  x:457, y:73, width:524, height:294 } // 영상확대
var WMV_Small  = {  x:21 , y:75, width:420, height:236 } //  영상축소


function onFullVod(){ // 영상 풀창
		full_screen();
}

var sizeFlag = 0;
function onCross(){ // 교안 크로스
   if (sizeFlag)
   {
		showWmv();
  		$("div.vodLayer").animate({ top:WMV_Big.y, left:WMV_Big.x, width:WMV_Big.width,height:WMV_Big.height}, 200, function(){});
		sizeFlag = 0;
   }else{
		showWmv();
  		$("div.vodLayer").animate({ top:WMV_Small.y, left:WMV_Small.x, width:WMV_Small.width,height:WMV_Small.height}, 200, function(){});
		sizeFlag = 1;
   }
}

function onReturn(){ // 교안 닫기
		showWmv();
  		$("div.vodLayer").animate({ top:WMV_Small.y, left:WMV_Small.x, width:WMV_Small.width,height:WMV_Small.height}, 200, function(){});
		sizeFlag = 1;
}


function onHidden(){ // 교안 풀창
		hideWmv();
}


/////////////////////////////////////////   showhide
function hideWmv(){	
	vodLayer.style.visibility	=	"hidden"; 
}

function showWmv(){	
	vodLayer.style.visibility	=	"visible"; 
}