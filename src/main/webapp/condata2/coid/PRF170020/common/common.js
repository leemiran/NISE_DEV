function swf_include(swfUrl,swfWidth,swfHeight,bgColor,swfName,access,flashVars){
	// 플래시 코드 정의
	var flashStr=
	"<object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0' width='"+swfWidth+"' height='"+swfHeight+"' id='"+swfName+"' align='middle' />"+
	"<param name='allowScriptAccess' value='"+access+"' />"+
	"<param name='movie' value='"+swfUrl+"' />"+
	"<param name='FlashVars' value='"+flashVars+"' />"+
	"<param name='loop' value='false' />"+
	"<param name='menu' value='false' />"+
	"<param name='quality' value='high' />"+
    "<param name='scale' value='noscale' />"+
	"<param name='bgcolor' value='"+bgColor+"' />"+
	"<embed src='"+swfUrl+"' FlashVars='"+flashVars+"'  quality='best' bgcolor='#EEF8FF' width='"+swfWidth+"' height='"+swfHeight+"' name='"+swfName+"' align='middle' allowScriptAccess='sameDomain' type='application/x-shockwave-flash' pluginspage='http://www.macromedia.com/go/getflashplayer' />"+
	"</object>";

	// 플래시 코드 출력
	document.write(flashStr);
};

function go_page(goURL){
	location.href = goURL;
};



/***************************************************************************
	 :+: 메모장 저장 관련 :+:
***************************************************************************/
function fileSave(_title, _txt)
{
  if (confirm("저장하시겠습니까?")) {//브라우저마다 동작시간차가 있어서 시간벌기로 alert창 띄움
    
  } else {		
    return;
  }
	
  var strFileName = _title +".txt";//저장할 파일명 만들기
 
  if (document.all) {
		
    /* iframe 동적으로 생성 */
    var hiddenIframe = document.createElement("iframe");
    hiddenIframe.id = "hidden_frame";
    hiddenIframe.style.display = "none";
    document.body.appendChild(hiddenIframe);
    
    var hiddenDocument = document.frames["hidden_frame"].document;

    hiddenDocument.open("text/plain","replace");
    hiddenDocument.charset = "UTF-8";//"euc-kr"; //"UTF-8";    
    hiddenDocument.write(_title +"\r\r");
    hiddenDocument.write(_txt);
    hiddenDocument.close();

    hiddenDocument.focus();
    hiddenDocument.execCommand('SaveAs', false, strFileName);
  } else {
    alert("인터넷 익스플로어에서만 실행이 가능합니다.");
  }
}