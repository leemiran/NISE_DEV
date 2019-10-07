// flashWrite(파일경로, 가로, 세로, 아이디, 배경색, 변수)
function flashWrite(url,w,h,id,bg,vars,win) {
//플래시 코드 정의
var flashStr="<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0' width='"+w+"' height='"+h+"' id='"+id+"' align='middle'>"+
"<param name='allowScriptAccess' value='always'>"+
"<param name='movie' value='"+url+"'>"+
"<param name='FlashVars' VALUE='"+vars+"'>"+
"<param name='wmode' value='"+win+"'>"+
"<param name='menu' value='false'>"+
"<param name='quality' value='high'>"+
"<embed src='"+url+"' FlashVars='"+vars+"' menu='false' quality='high' bgcolor='"+bg+"' width='"+w+"' height='"+h+"' name='"+id+"' align='middle' allowScriptAccess='always' type='application/x-shockwave-flash' pluginspage='http://www.macromedia.com/go/getflashplayer'>"+
"</embed>"+
"</object>";
//플래시 코드 출력
document.write(flashStr);
}

function processKey() { 
	//alert(event.keyCode);
	if( (event.ctrlKey == true && (event.keyCode == 78 || event.keyCode == 82)) || (event.keyCode >= 112 && event.keyCode <= 123) || event.keyCode == 2 || event.keyCode == 8 || event.keyCode == 116) { 
		event.keyCode = 0; 
		event.cancelBubble = true; 
		event.returnValue =  false; 
	} 
} 
document.onkeydown = processKey;

// 맛보기
function Greeting() {
	alert("맛보기 과정에서는 본 기능이 지원되지 않습니다.");
}

function itostr(inum) {
	return inum<10?"0"+inum:inum;
}

//reset window(full)
function WindowResetFullSize() {
	var wid = eval(screen.availWidth);
	var hig = eval(screen.availHeight);
	
	top.moveTo(0,0);
	top.resizeTo(wid,hig);
}


function CloseWin() {
	top.close();
}

//OpenWindow
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

//ie ativeX 출력 변경 함수 
function mEmbed(wid,hei,SwfName) {
		document.write('<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" name="mov" width="'+wid+'" height="'+hei+'" id="mov">')
		document.write('<param name="movie" value="'+SwfName+'">')
		document.write('<param name="quality" value="high">')
		document.write('<param name="allowScriptAccess" value="always">')
		document.write('<embed src="'+SwfName+'" width="'+wid+'" height="'+hei+'" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" name="mov"></embed>')
		document.write('</object>')	
}


var InternetExplorer = navigator.appName.indexOf("Microsoft") != -1;
// Handle all the the FSCommand messages in a Flash movie
function mov_DoFSCommand(command, args) 
{
  var movObj = InternetExplorer ? mov : document.mov;
	
	switch(command)
	{ 	
		
		case "openSite":
			openSite(args);
			break; 			  
		
		case "FileDown":       //파일다운로드
			FileDown(args);
			break; 	
		
		case "openPopup":      //팝어창 제시
			openPopup(args);
			break;
		
		case "open_learnmap":	 //러링맵 버튼 클릭시
		  open_learnmap();
		  break;	
	
	}  
}

// Hook for Internet Explorer 
if (navigator.appName && navigator.appName.indexOf("Microsoft") != -1 && 
	  navigator.userAgent.indexOf("Windows") != -1 && navigator.userAgent.indexOf("Windows 3.1") == -1) 
{
	document.write('<SCRIPT LANGUAGE=VBScript\> \n');
	document.write('on error resume next \n');
	document.write('Sub mov_FSCommand(ByVal command, ByVal args)\n');
	document.write('  call mov_DoFSCommand(command, args)\n');
	document.write('end sub\n');
	document.write('</SCRIPT\> \n');
}



function OpenReviewPop(popNum) {
	var leNum = window["con_mov"].GetVariable("curLesson");
	var pageNum = window["con_mov"].GetVariable("curPage");

	var wid = 1014;
	var hig = 652;
	var leftPos = 0;		//(eval(screen.availWidth)-Number(wid))/2;
	var topPos = 0;		//(eval(screen.availHeight)-Number(hig))/2;	
	var winName = "0203_p0"+popNum;
	var loca = "0203_p0"+popNum+".htm";
	//alert(loca);
	MM_openBrWindow(loca,winName,'left='+leftPos+',top='+topPos+',width='+wid+',height='+hig+',status=yes');
}

function OpenDict() {
	var wid = 500;
	var hig = 700;
	var leftPos = 0;		//(eval(screen.availWidth)-Number(wid))/2;
	var topPos = 0;		//(eval(screen.availHeight)-Number(hig))/2;	
	var winName = "dict";
	var loca = "../common/dict/glossary.htm";
	//alert(loca);
	MM_openBrWindow(loca,winName,'left='+leftPos+',top='+topPos+',width='+wid+',height='+hig+',status=yes');
}

//러링맵
function open_learnmap()
{ 
	
	var wid = eval(screen.availWidth); // 모니터 가로
	var hig = eval(screen.availHeight); // 모니터 세로
	var winPosWidth = 1014;
	var winPosHeight = 597;
	var width = 1014;
	var height = 597;
	var xPos = wid/2  - winPosWidth/2;
	var yPos = hig/2 - winPosHeight/2;
	var path = "map.html";
	MM_openBrWindow(path, 'popupWin', 'left='+xPos+',top='+yPos+',width='+width+',height='+height);
	//window.open("map.html", "map", "left=0. top=0, width=1014, height=680, menubar=no, toolbar=no, status=no, resizable=no, scrollbars=no");  
	//alert("LearningMap");	
}

//사이트링크
function openSite(str){
	var siteUrl = str;
	//alert(siteUrl.substr(0,4));
	if(siteUrl.substr(0,4) != "http") {
		siteUrl = "http://"+siteUrl;
	}
	MM_openBrWindow(siteUrl,'sitePop','');
}


//file down용 함수 - OnLine용
function FileDown(type) {
	var wid = 353;
	var hig = 182;
	var leftPos = (eval(screen.availWidth)-Number(wid))/2;
	var topPos = (eval(screen.availHeight)-Number(hig))/2;	
	var winName = "win"+type;
	var loca = "down/"+type+"down.htm";	
	//alert(winName);
	//alert(loca);	
	MM_openBrWindow(loca,winName,'left='+leftPos+',top='+topPos+',width='+wid+',height='+hig+',status=yes');
}

/* 
function FileDown(fileNum)
{
//alert(fileNum);	
	var wid = 353;
	var hig = 182;
	var leftPos = (eval(screen.availWidth)-Number(wid))/2;
	var topPos = (eval(screen.availHeight)-Number(hig))/2;
	var loca = "down/"+ fileNum+"down.html";
//alert(loca);
	MM_openBrWindow(loca, fileNum,'left='+leftPos+',top='+topPos+',width='+wid+',height='+hig);
	
}
*/

// IE ActiveX 출력 드림위버 버전
//v1.0
//Copyright 2006 Adobe Systems, Inc. All rights reserved.
function AC_AddExtension(src, ext)
{
  if (src.indexOf('?') != -1)
    return src.replace(/\?/, ext+'?'); 
  else
    return src + ext;
}

function AC_Generateobj(objAttrs, params, embedAttrs) 
{ 
  var str = '<object ';
  for (var i in objAttrs)
    str += i + '="' + objAttrs[i] + '" ';
  str += '>';
  for (var i in params)
    str += '<param name="' + i + '" value="' + params[i] + '" /> ';
  str += '<embed ';
  for (var i in embedAttrs)
    str += i + '="' + embedAttrs[i] + '" ';
  str += ' ></embed></object>';

  document.write(str);
}

function AC_FL_RunContent(){
  var ret = 
    AC_GetArgs
    (  arguments, ".swf", "movie", "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
     , "application/x-shockwave-flash"
    );
  AC_Generateobj(ret.objAttrs, ret.params, ret.embedAttrs);
}

function AC_SW_RunContent(){
  var ret = 
    AC_GetArgs
    (  arguments, ".dcr", "src", "clsid:166B1BCA-3F9C-11CF-8075-444553540000"
     , null
    );
  AC_Generateobj(ret.objAttrs, ret.params, ret.embedAttrs);
}

function AC_GetArgs(args, ext, srcParamName, classid, mimeType){
  var ret = new Object();
  ret.embedAttrs = new Object();
  ret.params = new Object();
  ret.objAttrs = new Object();
  for (var i=0; i < args.length; i=i+2){
    var currArg = args[i].toLowerCase();    

    switch (currArg){	
      case "classid":
        break;
      case "pluginspage":
        ret.embedAttrs[args[i]] = args[i+1];
        break;
      case "src":
      case "movie":	
        args[i+1] = AC_AddExtension(args[i+1], ext);
        ret.embedAttrs["src"] = args[i+1];
        ret.params[srcParamName] = args[i+1];
        break;
      case "onafterupdate":
      case "onbeforeupdate":
      case "onblur":
      case "oncellchange":
      case "onclick":
      case "ondblClick":
      case "ondrag":
      case "ondragend":
      case "ondragenter":
      case "ondragleave":
      case "ondragover":
      case "ondrop":
      case "onfinish":
      case "onfocus":
      case "onhelp":
      case "onmousedown":
      case "onmouseup":
      case "onmouseover":
      case "onmousemove":
      case "onmouseout":
      case "onkeypress":
      case "onkeydown":
      case "onkeyup":
      case "onload":
      case "onlosecapture":
      case "onpropertychange":
      case "onreadystatechange":
      case "onrowsdelete":
      case "onrowenter":
      case "onrowexit":
      case "onrowsinserted":
      case "onstart":
      case "onscroll":
      case "onbeforeeditfocus":
      case "onactivate":
      case "onbeforedeactivate":
      case "ondeactivate":
      case "type":
      case "codebase":
        ret.objAttrs[args[i]] = args[i+1];
        break;
      case "width":
      case "height":
      case "align":
      case "vspace": 
      case "hspace":
      case "class":
      case "title":
      case "accesskey":
      case "name":
      case "id":
      case "tabindex":
        ret.embedAttrs[args[i]] = ret.objAttrs[args[i]] = args[i+1];
        break;
      default:
        ret.embedAttrs[args[i]] = ret.params[args[i]] = args[i+1];
    }
  }
  ret.objAttrs["classid"] = classid;
  if (mimeType) ret.embedAttrs["type"] = mimeType;
  return ret;
}



