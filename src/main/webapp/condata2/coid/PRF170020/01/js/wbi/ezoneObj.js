//에러 발생시 메세지 무시
function errorMsg(){
	return true;
}
window.onerror=errorMsg;

//플래시로드
function loadswf(file,width,height,trans,num){
	tagObj='';
	tagObj+='<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="'+width+'" height="'+height+'" id="contents" align="middle">'
	tagObj+='<param name="allowScriptAccess" value="always" />'
	tagObj+='<param name="movie" value="'+file+'" />'
	tagObj+='<param name="menu" value=false>'
	tagObj+='<param name="flashvars" value="'+num+'"> '

   if (trans == true){
		tagObj+='<param name="wmode" value="window" />'
	}

	tagObj+='<embed src="'+file+'" quality="high" bgcolor="#ffffff" width="'+width+'" flashvars='+num+' height="'+height+'" name="contents" align="middle" allowScriptAccess="always" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />'
	tagObj+='</object>'
	document.write(tagObj);
	//alert(tagObj);
	//플래시 로드 관련
}

//팝업
function hnPopOpen(s){
	var sLst = s.split('$$');
	//sLst[0]:파일URL, sLst[1]:팝업ID, sLst[2]:width, sLst[3]:height
	window.open(sLst[0], sLst[1], 'width='+sLst[2]+', height='+sLst[3]);
}

//학습종료
function hnClose(){
	top.close();
}

//쿠키값 저장
function setCookie(name, value, expires) {
	var todayDate = new Date();
	todayDate.setDate( todayDate.getDate() + expires );

  	document.cookie = name + "=" + escape (value) + "; path=/" + ((expires) ? "; expires=" + todayDate.toGMTString() : "");
}

//쿠키값 저장(특정값)
function setCookie2(value) {
	name='ckValue1';
	expires=30;
	var todayDate = new Date();
	todayDate.setDate( todayDate.getDate() + expires );

  	document.cookie = name + "=" + escape (value) + "; path=/" + ((expires) ? "; expires=" + todayDate.toGMTString() : "");
}

//쿠키값 반환
function getCookie(strName) {
	var strArg = new String(strName + "=");
	var nArgLen, nCookieLen, nEnd;
	var i = 0, j;

	nArgLen    = strArg.length;
	nCookieLen = document.cookie.length;

	if(nCookieLen > 0) {

		while(i < nCookieLen) {
			j = i + nArgLen;

			if(document.cookie.substring(i, j) == strArg) {
				nEnd = document.cookie.indexOf (";", j);
				if(nEnd == -1) nEnd = document.cookie.length;
				return unescape(document.cookie.substring(j, nEnd));
			}

			i = document.cookie.indexOf(" ", i) + 1;

			if (i == 0) break;
		}
	}

	return("");
}

//얼럿
function hnAlert(s){
	alert(s);
}
