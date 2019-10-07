document.write("<script type='text/javascript' src='/include/opinion.js'><"+"/script>");
var lmsMode = true;
var debugmode = false;
var objWid	= 1000;
var objHi	= 640;
var nowIndex = document.URL.split(".html")[0];
var h_chasi = Number(nowIndex.substring(nowIndex.length-5,nowIndex.length-3));
var h_page = Number(nowIndex.substring(nowIndex.length-2,nowIndex.length));

function flashWrite(){
	var mainSwf = "../common/main.swf";
	tagObj='';
	tagObj+='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width='+objWid+' height='+objHi+' id="info">'
	tagObj+='<param name="allowScriptAccess" value='+"true"+' />'
	tagObj+='<param name="allowFullScreen" value='+"true"+' />'
	tagObj+='<param name="movie" value="'+mainSwf+'"/>'
	tagObj+='<param name="FlashVars" value="thisChasi='+h_chasi+'&thisPage='+h_page+'&totalPage='+tot_page[h_chasi]+'&startFrameNum='+startFrameArr[h_chasi][h_page]+'&menuPage='+menu_page[h_chasi]+'">'
	tagObj+='<param name="quality" value="high" />'
	tagObj+='<param name="wmode" value="transparent" />'
	tagObj+='<embed src="'+mainSwf+'" FlashVars="thisChasi='+h_chasi+'&thisPage='+h_page+'&totalPage='+tot_page[h_chasi]+'&startFrameNum='+startFrameArr[h_chasi][h_page]+'&menuPage='+menu_page[h_chasi]+'" quality="high" width='+objWid+' height='+objHi+' name="info" align="middle" allowScriptAccess='+"true"+' allowFullScreen='+"true"+' type="application/x-shockwave-flash" wmode="transparent" swLiveConnect=true pluginspage="http://www.macromedia.com/go/getflashplayer" />' 
	tagObj+='</object>'
	document.write(tagObj);
}

function newWin(url){
    window.open(url,"","width=400,height=350,toolbar=no,status=no,scrollbars=no,resizable=no,location=no");
}


function DumpToFlash(){
	swfcontent.setVariable("tNum", totalpage);
}

// 페이지 이동
function movePage(val,num){
	location.href=itostr(h_chasi)+"_"+itostr(val)+'.html';
}

function naxtPage(val,num){
	var page = val+1;	
	location.href=itostr(h_chasi)+"_"+itostr(page)+'.html';
}

function prevPage(val,num){
	var page = val-1;		
	location.href=itostr(h_chasi)+"_"+itostr(page)+'.html';
}

// 인덱스로 메뉴 페이지 정보 보내기
function subMenuPageFn(){
	var menupage = 	menu_page[Number(h_chasi)];
	return menupage;
}
function mainIndexFn(){
	var menupage = 	main_index[Number(h_chasi)];
	return menupage;
}


//학습 싱크시간
function syncTimeFn(){
	var qtime = syncTimeArr[Number(h_chasi)][Number(h_page)];
	return qtime;
}

function keyboard(down){
    if(down=="37"){
    	window.document.info.SetVariable("keyLeftFn", "");
    }else if(down=="39"){
    	window.document.info.SetVariable("keyRightFn", "");
    }else if(down=="40"){
    	window.document.info.SetVariable("keyDownFn", "");
    }else if(down=="38"){
    	window.document.info.SetVariable("keyUpFn", "");
    }else if(down=="119"){
    	window.document.info.SetVariable("keyF8Fn", "");
    }else if(down=="120"){
    	window.document.info.SetVariable("keyF9Fn", "");
    }
}

function alertFn(txt){
	alert(txt);
}

function itostr(inum){
	return inum<10?"0"+inum:inum;
}

//lms commit하기 -- 진도완료
function lms_commit()
{
	//alert("진도저장!!");
	top.ContentExecutor.commit(false, 'N');
}


// 메모장 텍스트 파일로 저장
function memoSaveFn(txt){
	memoSave.document.open("text/plain","replace")
	memoSave.document.charset="utf-8";
	memoSave.document.write(txt)
	memoSave.document.close()
	memoSave.focus()
	memoSave.document.execCommand('SaveAs',true,'.txt');
}
sessionFrame();

function sessionFrame(){
	writeiFrame = '<iframe Name="memoSave" ID="memoSave" style="display:none"></iframe>';
	document.write(writeiFrame);
}

function getParameter(strParamName) {
	var arrResult;
	if(strParamName){
		arrResult = location.search.match(new RegExp("[&?]" + strParamName+"=(.*?)(&|$)"));
	}
	return arrResult && arrResult[1] ? arrResult[1] : null;
}

function getParam(id){
	var param = getParameter(id);
	return param;
}

function writeFlashFn(url, width, height, vars1, vars2){	
	var flashVars='cp1='+vars1+'&cp2='+vars2;
	writeswf = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="'+width+'" height="'+height+'" id="mainSwf">';
	writeswf = writeswf + '<param name="allowScriptAccess" value="always">';
	writeswf = writeswf + '<param name="menu" value="false">';
	writeswf = writeswf + '<param name="movie" value="' +url +'">';
	writeswf = writeswf + '<param name="flashVars" value="' +flashVars +'">';
	writeswf = writeswf + '<param name="quality" value=high>';
	writeswf = writeswf + '<embed src="' + url  + '" quality="high" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" flashVars="'+flashVars+'" width="'+width+'" height="'+height+'" name = "mainSwf" ></embed></object>';
	document.write(writeswf);
}

function openWin(url,winName,width,height){
	if(url != "" && url != undefined){
	  sw=screen.availWidth;
	  sh=screen.availHeight;
	  px=(sw-width)/2;
	  py=(sh-height)/2;
	  authwin = window.open(url,winName, "width="+width+",height="+height+",left="+px+",top="+py+",menubar=no,toolbar=no,location=no,status=no,scrollbars=no,resizable=no");
	  authwin.focus();
    }else{
	  alert("샘플에선 제공되지 않는 기능입니다.");
	}
}


function getCodeFn(){
	return code;
}


function down(){
	var filename = "down_"+itostr(h_chasi)+ ".zip";	
	window.open('../download/'+filename+'',"down","toolbar=no,scrollbars=no,location=no,status=no,menubar=no,resizable=no,width=100,height=100")
}

function help(){
	var filename = "help_"+itostr(h_chasi)+ ".zip";	
	window.open('../download/'+filename+'',"help","toolbar=no,scrollbars=no,location=no,status=no,menubar=no,resizable=no,width=100,height=100")
}


function checkDebugMode(){
	if(debugmode){
		return true;
	}else{
		return false;
	}
}