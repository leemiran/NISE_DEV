document.write("<div id='vodLayer' style='position:absolute; left:"+movie_layer_left+"px; top:"+movie_layer_top+"px; width:"+movie_layer_width+"px; height:"+movie_layer_height+"px; z-index:1; visibility:hidden; '>");
if(navigator.appName == "Microsoft Internet Explorer"){
	document.write("<object ID='MediaPlayer1' classid='CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6' codebase='http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701' standby='Loading Microsoft Windows Media Player components...' type='video/x-ms-wmv' width='"+movie_width+"' height='"+movie_height+"'>");
	document.write("<param name='uiMode' value='none'>");
	document.write("<param name='autoStart' value='0'>");
	document.write("<param name='AutoResize' value='1'>");
	document.write("<PARAM  NAME='enableContextMenu'  VALUE='0'>");
	document.write("<PARAM NAME='stretchToFit' VALUE='1'>");
	document.write("<param name=filename value='"+movie_URL+"'>");
	document.write("<param name=URL value='"+movie_URL+"'>");
	document.write("<param name='showControls' value='1'>");
	document.write("</object>");
}else{
	document.write("<object ID='MediaPlayer1' type='video/x-ms-wmv' data='"+movie_URL+"' width='"+movie_width+"' height='"+movie_height+"'>");
	document.write("<param name='src' value='"+movie_URL+"' /> ");
	document.write("<param name='autostart' value='false' /> ");
	document.write("<param name='controller' value='false' /> ");
	document.write("</object>");
}
document.write("</div>");