function OpenWindow(url, name, left, top, width, height, toolbar, menubar, location, status, scrollbars, resizable)
{
	toolbar_str = toolbar ? 'yes' : 'no';
	menubar_str = menubar ? 'yes' : 'no';
	location_str = location ? 'yes' : 'no';
	status_str = status ? 'yes' : 'no';
	scrollbars_str = scrollbars ? 'yes' : 'no';
	resizable_str = resizable ? 'yes' : 'no';
	window.open( url, name, 'left='+left+',top='+top+',width='+width+',height='+height+',toolbar='+toolbar_str+',menubar='+menubar_str+',location='+location_str+',status='+status_str+',scrollbars='+scrollbars_str+',resizable='+resizable_str);
}

function openPopupFromIndex(url){
	OpenWindow(url, 'chbizwin', 0, 0, 1014, 652, 0, 0, 0, 0, 0, 0);
}
function openPopupMovie(url){
	OpenWindow(url, 'movieWin', 0, 0, 640, 540, 0, 0, 0, 0, 0, 0);
}

function openHelp(url){
	OpenWindow(url, 'help', 0, 0, 610, 440, 0, 0, 0, 0, 0, 0);
}

