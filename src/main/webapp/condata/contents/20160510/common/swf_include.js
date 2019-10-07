function swf_include(swfUrl,swfWidth,swfHeight,bgColor,swfName,access,flashVars){
	// 플래시 코드 정의
document.writeln('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ');
document.writeln('codebase="http://fpdownload.macromedia.com/1/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="'+swfWidth+'"height="'+swfHeight+'" id="'+swfName+'">');
document.writeln('	<param name="movie" value="'+swfUrl+'?thisPageNum='+thisPage+'">');
document.writeln('	<param name="FlashVars" value="'+flashVars+'">');
document.writeln('	<param name="loop" value="false">');
document.writeln('	<param name="quality" value="high">');
document.writeln('	<param name="wmode" value="window">');
document.writeln('	<param name="allowScriptAccess" value="'+access+'" />');
document.writeln('	<embed src="'+swfUrl+'?thisPageNum='+thisPage+'" FlashVars="'+flashVars+'" quality="high" menu="false" pluginspage="http://www.macromedia.com/go/getflashplayer" ');
document.writeln('	type="application/x-shockwave-flash" width="'+swfWidth+'"height="'+swfHeight+'" name="'+swfName+'"></embed></object>');
};


