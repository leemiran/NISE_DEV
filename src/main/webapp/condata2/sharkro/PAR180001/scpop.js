function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function goPreviewAct() {
	gotoPageNum=thisPageInfoNum-1;
	
	if(gotoPageNum<0){
		alert("첫 페이지입니다");
		return;
	}
	
	document.location=pageinfo[gotoPageNum][2]
	return;
}

//////////2015.5.7(마지막페이지액션수정)//////////////////////////////////////////
function goNextAct() {
	gotoPageNum=thisPageInfoNum+1;
	if(thisPageNum+1>totalPageNum){
		alert("마지막 페이지입니다");
		//next_chasi();
		return;
	}
	document.location=pageinfo[gotoPageNum][2]
	return;
}
//////////2012.9.18//////////////////////////////////////////

function gotoSubAct(num1,num2,num3){
	//alert(num1+" , "+num2+" , "+num3);
	gotoPageNum=1;
	if(num2==0){
		num2=1;
	}
	for(i=0;i<pageinfo.length;i++){
		if(num1==pageinfo[i][0] && num2==pageinfo[i][5]){
			gotoPageNum=i;
			break;
		}
	}
	if(typeof(parent.sno)!="undefined"){
		if(!LastPage){
			LastPage=1;
		}
		
		if(thisPageInfoNum>LastPage){
			LastPage=thisPageInfoNum;
		}

		if(gotoPageNum>(LastPage+1)){
			alert("학습은 순차적으로 진행됩니다.")
			return;
		}
	}
	
	document.location=pageinfo[gotoPageNum][2];
}

function my_pop(val,w,h){
	authwin = window.open(val, "pop", "menubar=no,toolbar=no,location=no,status=yes,scrollbars=no,resizable=yes,top=0,left=0,width="+w+",height="+h+"").focus();
}

function my_down(val){
	//window.open("down/"+val);
	authwin = window.open("down/down.html?downVal="+val, "down", "menubar=no,toolbar=no,location=no,status=yes,scrollbars=no,resizable=yes,top=0,left=0,width=300,height=150").focus();
}

function my_gang_down(val){
	authwin = window.open("../common/down/down.html?downVal="+val, "down", "menubar=no,toolbar=no,location=no,status=yes,scrollbars=no,resizable=yes,top=0,left=0,width=300,height=150").focus();
}

function my_down_book(){
	val="book_"+itostr(thisModuleNum)+".zip";
	authwin = window.open("down/down.html?downVal="+val, "down", "menubar=no,toolbar=no,location=no,status=yes,scrollbars=no,resizable=yes,top=0,left=0,width=300,height=150").focus();	
}

function on_help(){
	//학습도우미
	window.open("../common/help.html","01",'left=0 top=0, width=1000,height=640,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
  // alert("본 메뉴는 지원하지 않습니다.")
}

function on_note(){
	//교안
	window.open("../common/down.html","note",'left=0 top=0, width=1000,height=640,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
  //alert("본 메뉴는 지원하지 않습니다.")
}

//////////////////////////2012.09.18//////////////////////////
function pop01(){
	//러님맵
	window.open("../common/map.html?chap="+thisModuleNum,"01",'left=0 top=0, width=1000,height=640,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
  // alert("본 메뉴는 지원하지 않습니다.")
}	
//////////////////////////2012.09.18//////////////////////////



function pop03(){
	//강사소개1
	window.open("../common/teacher1.html","03",'left=0 top=0, width=975,height=580,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
	 //alert("본 메뉴는 지원하지 않습니다.")
}	

function pop04(){
	//강사소개2
	window.open("../common/teacher2.html","03",'left=0 top=0, width=975,height=580,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
	 //alert("본 메뉴는 지원하지 않습니다.")
}

function pop05(){
	//강사소개3
	window.open("../common/teacher3.html","03",'left=0 top=0, width=975,height=580,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
	 //alert("본 메뉴는 지원하지 않습니다.")
}	


var opinion_cd=thisModuleNum;
function callfunc_input(contents) {
	SendFrom.n_titl.value = opnTitle;
	SendFrom.n_ctnt.value = contents;
	f_send_opinion(opinion_cd,document.SendFrom);	
}

function callfunc_list() {
     f_dsp_opinion(opinion_cd, 'sendresult/board_view.jsp')	
}


function hnAlert(){
	alert("프로토에서는 지원하지 않는 기능입니다.");
	return;
}

function flvStartInit(){
	//alert("flvStartInit : "+thisPageNum);
	//document.info.setVariable("watchObject.flv_info", cueArray[thisPageNum]);
	return cueArray[thisPageNum];
}
function itostr(num){
	if(num<10){
		return "0"+num;
	}else{
		return num;
	}
}

function returnCuePoint(){
	return cueArray;
}

function returnPageCue(){
	//top.status="returnPageCue : "+thisPageNum
	return pageCueArray[thisPageNum];

}

function mSearch(txt,num){
	if(num==1){
		window.open("http://search.naver.com/search.naver?where=nexearch&query="+txt+"&sm=top_hty&fbm=1","")
	}else if(num==2){
		window.open("http://search.daum.net/search?w=tot&t__nil_searchbox=btn&nil_id=tot&top_sp=&stype=tot&q="+txt+"","")
	}else if(num==3){
		window.open("http://www.google.co.kr/search?complete=1&hl=ko&q="+txt+"&lr=&aq=f","")
	}
}

function mySave(txt){
	text1 = txt;
	page=thisModuleNum+" 차시 메모장.html";
	
	hidden_frame.document.open("text/html","replace")
	hidden_frame.document.writeln("<html><head><meta http-equiv='content-type' content='text/html; charset=euc-kr'>");
	hidden_frame.document.writeln("<title>"+thisModuleNum+" 차시 메모</title>");
	hidden_frame.document.writeln("<style type='text/css'>");
	hidden_frame.document.writeln("td{font-family:굴림; font-size:12px; line-height:16px; color:#000000;}");
	hidden_frame.document.writeln(".line{border: 1px solid #4F4F4F;}");
	hidden_frame.document.writeln("</style></head>");
	hidden_frame.document.writeln("<body leftmargin='8' topmargin='8' marginwidth='8' marginheight='8'>");
	hidden_frame.document.writeln("<table><tr><td>");
	hidden_frame.document.writeln("<b>메모장</b><br><br>");
	hidden_frame.document.writeln("</td></tr><tr><td>");
	hidden_frame.document.writeln("<pre>"+text1+"</pre><p>");
	hidden_frame.document.writeln("</td></table></body></html>");
	hidden_frame.document.close();
	hidden_frame.focus();
	hidden_frame.document.execCommand('SaveAs', false, page);
}

function returnFlv(str){
	//returnVal="rtmpt://cyber.kbi.or.kr/0000000090/06/"; /// 동영상 경로
	//returnVal="../flv/"+str;       // 로컬 경로
	returnVal="../flv/";
	return returnVal;
}