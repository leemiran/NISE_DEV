/* 
ActionSript : �̷��� ���� ���ø�
�� �� �� : nanpjs
���糯¥ : 2014.5
Question : come to nanpjs!!

-- ��ȭ�� ��ȭ�� ��ü, ����&���� ũ�ν�, ���ȸ� ���� �� �⺻ �ҽ��� ���� �Ǿ� �ֽ��ϴ�. --

*/

// wmv �δ� �⺻ ����
//-----------------------------------------------------------------------------------------------
var loader_swf_name="./common/wmv/loading.swf"
var loader_swf_width=100
var loader_swf_height=100
var loader_width=67
var loader_height=18
var loader_left=180
var loader_top=150
//-----------------------------------------------------------------------------------------------

// wmv ������ �⺻ ����
//-----------------------------------------------------------------------------------------------
var movie_width="100%"; // ���� ���� ��� ����� ���� 100%�� ����
var movie_height="100%"; // ���� ���� ��� ����� ���� 100%�� ����
var movie_layer_width=420; // ���� ���̾� ��
var movie_layer_height=236; // ���� ���̾� ����

var movie_layer_left=500; // ���� ���̾� ���������� ��ġ
var movie_layer_top=75; // ���� ���̾� ��ܺ����� ��ġ
//-----------------------------------------------------------------------------------------------

//������ ���� �ּ��Է�
var vod_HD_URL = "./vod/" + pagenum + ".asx";
//var vod_SD_URL = "./vod/" + curPageStr + "_" + lessonInfo[parseInt(curPageStr,10)] + "_SD.asx"; // ��ȭ�� �ּ�( �Ϲ������� ��� ���� )
var movie_URL=vod_HD_URL; //���۽ÿ��� HD�� ����

//���ù�ȣ
var lcode=1
var movie_num=0


//��ȭ�� ���� ����
function video_play(){
	vodLayer.style.visibility	=	"visible"; 
	vod_l();
}


//������ �ٲ�
//��ȭ��
function vod_l(){
    var ctime = MediaPlayer1.controls.CurrentPosition;
	MediaPlayer1.URL = vod_HD_URL;
    MediaPlayer1.controls.currentPosition = ctime;
	MediaPlayer1.controls.play();
}
//��ȭ�� ( �Ϲ������� ��� ���� )
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
	   alert("��ȭ�� �������� ��ȯ �˴ϴ�.");
	   vod_s();
	   qualityFlag = "low";
   }else{
	   alert("��ȭ�� �������� ��ȯ �˴ϴ�.");
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

	document.ff.ctime.value = cpos; // �н��ð� ������ ���� ctime���� ����
	
	//��Ʈ�ѹٿ� ����� ����
	pos = cpos / MediaPlayer1.currentMedia.duration * 100;
	document.contents.setVariable("curpos", pos);
	//����ð� ǥ��
	icpos = parseInt(cpos,10);
	mm = parseInt(icpos/60, 10);
	ss = icpos - mm * 60;

	document.contents.setVariable("this_time", cpos)
    document.contents.setVariable("total_time", MediaPlayer1.currentMedia.duration);
	
}
//�ε�
function Freeloader(){
	FState = document.MediaPlayer1.PlayState;
	document.contents.setVariable("FStatea", FState)
//alert(FState);
    //�����δ� ����
  	switch (FState){
	case 1 : //��ɾ� ������
//		 alert ("1-�������") 
		break;
	case 2 :
		//document.all['loader'].style.visibility = 'visible';
	//alert ("�����δ�") 
        break;
	case 3 : //�÷��̸���Ʈ�� ���� ó�� ������ �� �߻� 
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
//		alert('������');
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

// 130225 IE 10 ���� fscommand ���������� do_contents_FSCommand �� ������ ����
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
// do_contents_FSCommand �� ������ ����

// FScommand �̻�� ���� (����)
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

// FScommand �̻�� ���� (��)

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
//ȭ��ũ�� ��ȯ 7.0 ���� �̻�
	if  (document.MediaPlayer1.PlayState == 3){
		alert("'Esc ��ư'�� Ŭ���Ͻø� ���� ũ��� ���ư��ϴ�.");
		document.MediaPlayer1.fullscreen = true; }
	else {
		alert("���ǰ� ������϶� ��üȭ���� �����մϴ�."); 
	}
} 

function click_download()
{
    window.open("./vod/down/01.zip","_blank","status=yes, scrollbars=yes, resizable=yes, width=500, height=400"); 
} 
//������ ���
function player_setRate(no)
{
	MediaPlayer1.settings.rate = no;
}


var WMV_Big    = {  x:457, y:73, width:524, height:294 } // ����Ȯ��
var WMV_Small  = {  x:21 , y:75, width:420, height:236 } //  �������


function onFullVod(){ // ���� Ǯâ
		full_screen();
}

var sizeFlag = 0;
function onCross(){ // ���� ũ�ν�
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

function onReturn(){ // ���� �ݱ�
		showWmv();
  		$("div.vodLayer").animate({ top:WMV_Small.y, left:WMV_Small.x, width:WMV_Small.width,height:WMV_Small.height}, 200, function(){});
		sizeFlag = 1;
}


function onHidden(){ // ���� Ǯâ
		hideWmv();
}


/////////////////////////////////////////   showhide
function hideWmv(){	
	vodLayer.style.visibility	=	"hidden"; 
}

function showWmv(){	
	vodLayer.style.visibility	=	"visible"; 
}