function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function goPreviewAct() {
	gotoPageNum=thisPageInfoNum-1;
	
	if(gotoPageNum<0){
		alert("ù �������Դϴ�");
		return;
	}
	
	document.location=pageinfo[gotoPageNum][2]
	return;
}

//////////2015.5.7(�������������׼Ǽ���)//////////////////////////////////////////
function goNextAct() {
	gotoPageNum=thisPageInfoNum+1;
	if(thisPageNum+1>totalPageNum){
		alert("������ �������Դϴ�");
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
			alert("�н��� ���������� ����˴ϴ�.")
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
	//�н������
	window.open("../common/help.html","01",'left=0 top=0, width=1000,height=640,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
  // alert("�� �޴��� �������� �ʽ��ϴ�.")
}

function on_note(){
	//����
	window.open("../common/down.html","note",'left=0 top=0, width=1000,height=640,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
  //alert("�� �޴��� �������� �ʽ��ϴ�.")
}

//////////////////////////2012.09.18//////////////////////////
function pop01(){
	//���Ը�
	window.open("../common/map.html?chap="+thisModuleNum,"01",'left=0 top=0, width=1000,height=640,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
  // alert("�� �޴��� �������� �ʽ��ϴ�.")
}	
//////////////////////////2012.09.18//////////////////////////



function pop03(){
	//����Ұ�1
	window.open("../common/teacher1.html","03",'left=0 top=0, width=975,height=580,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
	 //alert("�� �޴��� �������� �ʽ��ϴ�.")
}	

function pop04(){
	//����Ұ�2
	window.open("../common/teacher2.html","03",'left=0 top=0, width=975,height=580,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
	 //alert("�� �޴��� �������� �ʽ��ϴ�.")
}

function pop05(){
	//����Ұ�3
	window.open("../common/teacher3.html","03",'left=0 top=0, width=975,height=580,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');
	 //alert("�� �޴��� �������� �ʽ��ϴ�.")
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
	alert("�����信���� �������� �ʴ� ����Դϴ�.");
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
	page=thisModuleNum+" ���� �޸���.html";
	
	hidden_frame.document.open("text/html","replace")
	hidden_frame.document.writeln("<html><head><meta http-equiv='content-type' content='text/html; charset=euc-kr'>");
	hidden_frame.document.writeln("<title>"+thisModuleNum+" ���� �޸�</title>");
	hidden_frame.document.writeln("<style type='text/css'>");
	hidden_frame.document.writeln("td{font-family:����; font-size:12px; line-height:16px; color:#000000;}");
	hidden_frame.document.writeln(".line{border: 1px solid #4F4F4F;}");
	hidden_frame.document.writeln("</style></head>");
	hidden_frame.document.writeln("<body leftmargin='8' topmargin='8' marginwidth='8' marginheight='8'>");
	hidden_frame.document.writeln("<table><tr><td>");
	hidden_frame.document.writeln("<b>�޸���</b><br><br>");
	hidden_frame.document.writeln("</td></tr><tr><td>");
	hidden_frame.document.writeln("<pre>"+text1+"</pre><p>");
	hidden_frame.document.writeln("</td></table></body></html>");
	hidden_frame.document.close();
	hidden_frame.focus();
	hidden_frame.document.execCommand('SaveAs', false, page);
}

function returnFlv(str){
	//returnVal="rtmpt://cyber.kbi.or.kr/0000000090/06/"; /// ������ ���
	//returnVal="../flv/"+str;       // ���� ���
	returnVal="../flv/";
	return returnVal;
}