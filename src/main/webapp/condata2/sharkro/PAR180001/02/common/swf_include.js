function swf_include(swfUrl,swfWidth,swfHeight,bgColor,swfName,access,flashVars){
	// 플래시 코드 정의
document.writeln('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ');
document.writeln('codebase="http://fpdownload.macromedia.com/1/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="'+swfWidth+'"height="'+swfHeight+'" id="'+swfName+'">');
document.writeln('	<param name="movie" value="'+swfUrl+'">');
document.writeln('	<param name="FlashVars" value="'+flashVars+pagenum+'">');
document.writeln('	<param name="loop" value="false">');
document.writeln('	<param name="quality" value="high">');
document.writeln('	<param name="wmode" value="transparent">');
document.writeln('	<param name="allowScriptAccess" value="'+access+'" />');

document.writeln('	<param name="allowFullScreen" value="true" />');

document.writeln('	<embed src="'+swfUrl+'" FlashVars="'+flashVars+pagenum+'" allowScriptAccess="'+access+'"  quality="high" menu="false" pluginspage="http://www.macromedia.com/go/getflashplayer" ');
document.writeln('	type="application/x-shockwave-flash" width="'+swfWidth+'"height="'+swfHeight+'" name="'+swfName+'"></embed></object>');
};



function ctime_include(){
	// 학습시간 폼
document.writeln('<form name="ff">');
document.writeln('<input type="hidden" name="ctime" value="0">');
document.writeln('</form>');
};