	var WMVpageYN = setWmvPage();
	if (WMVpageYN == true) //동영상 페이지일때 프리로더 실행
	{

		document.write("<div id=loader\ style='position:absolute; left:"+loader_left+"px; top:"+loader_top+"px; width:"+loader_width+"px; height:"+loader_height+"px; z-index:5; visibility:visible; '>");
		document.write("<object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0' width='"+loader_swf_width+"' height='"+loader_swf_height+"' id='mainflash' align='middle'>");
		document.write("<param name='allowScriptAccess' value='always' />");
		document.write("<param name='movie' value='"+loader_swf_name+"' />");
		document.write("<param name='quality' value='high' />");
		document.write("<param name='wmode' value='transparent'>");
		document.write("<embed src='"+loader_swf_name+"' quality='high' bgcolor='#ffffff' width='"+loader_swf_width+"' height='"+loader_swf_height+"' name='mainflash' align='middle' allowScriptAccess='always' type='application/x-shockwave-flash' pluginspage='http://www.macromedia.com/go/getflashplayer' />");
		document.write("</object>");
		document.write("</div>");

	}



