var flv_info=new Array();
flv_info[1]=[3];
flv_info[2]=[3];
flv_info[3]=[3];
flv_info[4]=[3];
flv_info[5]=[3];
flv_info[6]=[3];
flv_info[7]=[0];
flv_info[8]=[0];
flv_info[9]=[0];
flv_info[10]=[0];
flv_info[11]=[0];
flv_info[12]=[0];
flv_info[13]=[0];
flv_info[14]=[0];
flv_info[15]=[0];
flv_info[16]=[0];
flv_info[17]=[0];
flv_info[18]=[0];
flv_info[19]=[0];
flv_info[20]=[0];
flv_info[21]=[0];
flv_info[22]=[0];
flv_info[23]=[0];
flv_info[24]=[0];
flv_info[25]=[0];
flv_info[26]=[0];
flv_info[27]=[0];
flv_info[28]=[0];
flv_info[29]=[0];
flv_info[30]=[0];
flv_info[31]=[0];
flv_info[32]=[0];
flv_info[33]=[0];
flv_info[34]=[0];
flv_info[35]=[0];
flv_info[36]=[0];
flv_info[37]=[0];
flv_info[38]=[0];
flv_info[39]=[0];
flv_info[40]=[0];
flv_info[41]=[0];
flv_info[42]=[0];
flv_info[43]=[0];
flv_info[44]=[0];
flv_info[45]=[0];
flv_info[46]=[0];
flv_info[47]=[0];
flv_info[48]=[0];
flv_info[49]=[0];
flv_info[50]=[0];
flv_info[51]=[0];
flv_info[52]=[0];
flv_info[53]=[0];
flv_info[54]=[0];
flv_info[55]=[0];
flv_info[56]=[0];
flv_info[57]=[0];
flv_info[58]=[0];
flv_info[59]=[0];
flv_info[60]=[0];

var docuPath=location.href.split("/");
var docuName=docuPath[docuPath.length-1].replace(".htm","");

var module=Number(docuPath[docuPath.length-2]);
var nowPage=Number(docuName.substring(2));

var page_mode="";
var mode_num=0;
for(i=1;i<=flv_info[module].length;i++){
	if(nowPage==flv_info[module][i-1]){
		mode_num=mode_num+1;
	}
}
if(mode_num==0){
	page_mode="swf";
}else{
	page_mode="flv";
}

var flvPath="../common/flv/"+itostr(module)+"_"+itostr(nowPage)+".flv";
var xmlPath="../common/xml/"+itostr(module)+"_"+itostr(nowPage)+".xml";

function get_flvPath(){
	return flvPath;
}

function get_xmlPath(){
	return xmlPath;
}

function itostr(inum){
    return(inum<10?("0"+inum):(inum));
}

function swf_include(swfUrl,swfWidth,swfHeight,bgColor,swfName,access,flashVars){
	// 플래시 코드 정의
var isCD=0;
try{
	isCD=parent.isCD;
}catch(e){}
document.writeln('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ');
document.writeln('codebase="http://fpdownload.macromedia.com/1/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="'+swfWidth+'"height="'+swfHeight+'" id="'+swfName+'">');
document.writeln('	<param name="movie" value="'+swfUrl+'?isCD='+isCD+'&loadFrame=swf/main.swf&thisPageNum='+thisPage+'">');
document.writeln('	<param name="FlashVars" value="'+flashVars+'">');
document.writeln('	<param name="loop" value="false">');
document.writeln('	<param name="quality" value="high">');
document.writeln('	<param name="wmode" value="window">');
document.writeln('	<param name="allowScriptAccess" value="'+access+'" />');

if(page_mode=="swf"){
	document.writeln('	<embed src="'+swfUrl+'?isCD='+isCD+'&loadFrame=swf/main.swf&thisPageNum='+thisPage+'" FlashVars="'+flashVars+'" quality="high" menu="false" pluginspage="http://www.macromedia.com/go/getflashplayer" ');
}else{	
	document.writeln('	<embed src="'+swfUrl+'?isCD='+isCD+'&loadFrame=swf/main_flv.swf&thisPageNum='+thisPage+'" FlashVars="'+flashVars+'" quality="high" menu="false" pluginspage="http://www.macromedia.com/go/getflashplayer" ');
}

document.writeln('	type="application/x-shockwave-flash" width="'+swfWidth+'"height="'+swfHeight+'" name="'+swfName+'"></embed></object>');

try{
	document.getElementById("myFlash").focus();
}catch(e){
	document.getElementsByName("myFlash").focus();
}


};

function get_pageMode(){
	return page_mode;
}




