//nowPgName ->	������������	-> ex) m02_01_08
//nowMxxChap ->	���� ���_����	-> ex) m02_01
//alert("nowPgName="+nowPgName);
//alert("nowMxxChap="+nowMxxChap);


//part-01 : LMS �߰�
function saveProgress(){//��������
	doLMSSetValue("cmi.core.lesson_location",nowPgName+".html");//���
	
	if(typeof(top.studyMenuFrame)!="undefined"){//�����Ⱑ �ƴ϶�� -> �����⿡�� studyMenuFrame�� ��� ������ ��
		setProgress(NowPage+"/"+TotalPage); //SCOFunctions.js�� �߰����� �Ǿ� ���� : 'cmi.suspend_data'��ü ����Ͽ� �������� + �������� ����
	}
	
	if(NowPage==TotalPage)doLMSSetValue("cmi.core.lesson_status", "completed");//�Ϸ�

	var jindoCk=doLMSGetValue("cmi.core.lesson_status");
	if(jindoCk!="passed" && jindoCk!="failed" && jindoCk!="completed" && jindoCk!="browsed"){
		var tProgress=doLMSGetValue("cmi.suspend_data");
		var tP=tProgress.split("/");
		LastPage=tP[0];
	}
	//alert("cmi.suspend_data=tProgress="+tProgress);
	//alert("LastPage="+LastPage);
	//alert("cmi.core.lesson_status="+doLMSGetValue("cmi.core.lesson_status"));
	//doLMSCommit(); //�ݿ��ϸ� �ϴ��� �ʱ�ȭ��(������)
}


//�ǰ�
function f_send_opinion(exam_no, frm){//�ǰ߿ø���
	try{
		frm.d_opn_yy.value  =top.studyMenuFrame.menuForm.d_opn_yy.value;
		frm.i_cour.value    =top.studyMenuFrame.menuForm.i_cour.value;
		frm.o_sim_num.value =top.studyMenuFrame.menuForm.o_sim_num.value;
		frm.n_qna_pos1.value=exam_no;
		frm.action = "/nhome/lHNQuestionAction.do?method=insertFromContents";
		frm.submit();
		//window.close();
	}catch (ERROR){
		alert("LMS���� �޴��Դϴ�.");
	}
}
function f_dsp_opinion(exam_no){//�ٸ���� �ǰߺ���
	try{
		var v_opn_yy	=top.studyMenuFrame.menuForm.d_opn_yy.value;
		var v_cour	    =top.studyMenuFrame.menuForm.i_cour.value;
		var v_sim_num	=top.studyMenuFrame.menuForm.o_sim_num.value;
		var v_n_cour	=top.studyMenuFrame.menuForm.lecture_name.value;
		var url = "&z_d_opn_yy="+v_opn_yy+"&z_i_cour="+v_cour+"&z_o_sim_num="+v_sim_num+"&z_n_cour="+v_n_cour+"&z_i_noti=052008&z_n_qna_pos1="+exam_no;
		openWin = window.open("/nhome/lHNQuestionAction.do?method=selectsContents"+url,"","width=720,height=550,scrollbars=1");
	}catch (ERROR){
		alert("LMS���� �޴��Դϴ�.");
	}
}
function rtnOpNo(){var tOpN=Number(nowMxxChap.substr(1,2)+nowMxxChap.substr(4,2)+NowPage);return tOpN}//opinion number
function opinLst(){(typeof(parent.sno)!="undefined")?f_dsp_opinion(rtnOpNo()):alert("LMS���� ��� �Դϴ�. - �ٸ� �н��� �ǰߺ���");}//<-
function opinWrt(_s){//<-
	if(typeof(parent.sno)!="undefined"){
		var tTxt2="";
		var tTxt=_s;
		var tOpTit=(tTxt.length>10)?tTxt.substr(0,28)+"...":tTxt;	
		for(i=0;i<tTxt.length;i++)(tTxt.charCodeAt(i)==13)?tTxt2+="<br>":tTxt2+=tTxt.charAt(i);
		SendFrom.n_titl.value=tOpTit;//����
		SendFrom.n_ctnt.value=tTxt2;//����
		f_send_opinion(rtnOpNo(),document.SendFrom);
	}else alert("LMS���� ��� �Դϴ�. - �ǰ�����");
}


//part-02
var expDays=30;var exp = new Date();exp.setTime(exp.getTime()+(expDays*24*60*60*1000));
function inCookie(name,value){
	//document.cookie = name + "=" +value+"; expires=" + exp.toGMTString() + "; path=/;"
	document.cookie = name + "=" +value+"; expires=" + exp.toGMTString() + ";";
	//document.cookie = name + "=" +value+";";//<-�ӽÿ�
}
function outCookie(name){var from_idx=document.cookie.indexOf(name+'=');if(from_idx != -1){from_idx+= name.length+1;to_idx = document.cookie.indexOf(';', from_idx);if(to_idx == -1)to_idx=document.cookie.length;return unescape(document.cookie.substring(from_idx, to_idx));}else{return ""}}

function enEnter(_t){var tTxt2="";var tTxt=_t;for(i=0;i<tTxt.length;i++)(tTxt.charCodeAt(i)==13)?tTxt2+="��":tTxt2+=tTxt.charAt(i);return tTxt2}//enter ���ڵ�
function deEnter(_t){var tTxt2="";var tTxt=_t;for(i=0;i<tTxt.length;i++)(tTxt.charAt(i)=="��")?tTxt2+=String.fromCharCode(13):tTxt2+=tTxt.charAt(i);return tTxt2}//enter ���ڵ�
function ckSaveNote(name,value){var tTxt=enEnter(value);inCookie(name,tTxt);}//��Ʈ cookie save
function ckLoadNote(name){var tTxt=deEnter(outCookie(name));return tTxt;}//��Ʈ cookie load
function mkFileNote(value,Tit){//��Ʈ save file - html
	var tTitle=Tit;
	var tTxt2="";
	var tTxt=value;
	for(i=0;i<tTxt.length;i++)(tTxt.charCodeAt(i)==13)?tTxt2+="<br>":tTxt2+=tTxt.charAt(i);

	var mkHTML="";
	mkHTML+="<html><head><title>"+tTitle+"</title><meta http-equiv='Content-Type' content='text/html; charset=euc-kr'><style>table{font-family:����;font-size:12px;line-height:18px;color:#000000}</style>"
	mkHTML+="<body leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>"
	mkHTML+="<br><table width='660' border='0' align='center' cellpadding='0' cellspacing='0' style='border:solid 1px #666666;'><tr><td height='35' align='center'>"
	mkHTML+="<table width='660' border='0' align='center' cellpadding='0' cellspacing='0' style='word-wrap:break-word;word-break:break-all'>"
	mkHTML+="<tr><td height='45' align='center'><font size='3'><strong>"+tTitle+"</strong></font></td></tr>"
	mkHTML+="<tr><td height='10'></td></tr>"
	mkHTML+="<tr><td valign='top' style='padding-left:33px;padding-right:30px;'>"+tTxt2+"<br><br></td></tr>"
	mkHTML+="</table></td></tr></table></body></html>"

	mkfile_frame.document.open("text/html","replace")
	mkfile_frame.document.write(mkHTML);
	mkfile_frame.document.close();
	mkfile_frame.focus();
	mkfile_frame.document.execCommand('saveas','null',tTitle+'.html');
}


//part-03 : ����
var NumPage=Number(thisPageNum);NowPage=NumPage;
var TotalPage=pageinfo.length-1;
var LastPage=TotalPage;//<-�߰�:�н��� ������������

//��������: �����ٲ�� �ȵ� NumPage,TotalPage �ʿ�
if(typeof(parent.sno)!="undefined")saveProgress();//saveProgress()<-��ܿ� ����
saveProgress();
//swfName=nowPgName+".swf";
//swfName="./common/player.swf?LodCnt=1&ConURL1=swf/"+swfName;
//swfName+="&NPage="+NowPage+"&TPage="+TotalPage+"&Script="+PageInfo[NowPage][0]+"&PlayView="+PageInfo[NowPage][1];
//swfName+="&nowMxxChap="+nowMxxChap+"&nowPgName="+nowPgName;
//swfName+="&GrpNum="+PageInfo[NowPage][2];//Group��
//swfName+="&plyTy="+PageInfo[NowPage][3];//play type: �Ϲ�, flv1/2 ����
//swfName+="&Sudden="+PageInfo[NowPage][4];//break
//swfName+="&treeIdx="+PageInfo[NowPage][5];//treeIdx
//swfName+="&FlvConnURL="+FlvConnURL+"&FlvDir="+FlvDir+"&flvFile="+nowPgName;//flv
//swfName+="&FpType="+FpType;//����
//swfName+="&FpMOpnPg="+FpMOpnPg;//��������������
//swfName+="&FpMClsPg="+FpMClsPg;//���Ŭ��¡��������
//alert(swfName);

function writeContents(){
	//var ContentsHTML="<div>"+SwfViewRtn(swfName,'swfcontent',1014,597)+"</div>";
	document.write("<iframe id='hidden_frame' width='0' height='0' frameborder='0' src=''></iframe>");
	var ContentsHTML="";

	//�ӽ�
	if(typeof(parent.sno)!="undefined"){ContentsHTML+="<font color='white'>"+doLMSGetValue("cmi.core.lesson_status")+":"+LastPage+"</font>";}
	
	//�߰�
	ContentsHTML+="<iframe name='lmspage' id='lmspage' src='about:blank' width='100%' marginwidth='0' height='100%' marginheight='0' scrolling='no' frameborder='0' style='display:none'></iframe>";
	ContentsHTML+="<form name='SendFrom' method='post' style='display:none' target = 'lmspage'>";
	//ContentsHTML+="<form name='SendFrom' method='post' style='display:none' target = 'sendFrame'>";
	ContentsHTML+="<input type='radio' name='n_qna_pos2' value='Y'><input type='radio' name='n_qna_pos2' value='N'>";//���������
	ContentsHTML+="<input name='n_titl' value=''><textarea name='n_ctnt'></textarea>";//����/����
	ContentsHTML+="<input type='hidden' name='command' value=''>";//���౸����
	ContentsHTML+="<input type='hidden' name='R0N' value='true'>";//����������
	ContentsHTML+="<input type='hidden' name='d_opn_yy' value=''>";//�⵵
	ContentsHTML+="<input type='hidden' name='i_cour' value=''>";//����
	ContentsHTML+="<input type='hidden' name='o_sim_num' value=''>";//���
	ContentsHTML+="<input type='hidden' name='n_qna_pos1' value=''>";//������ȣ
	ContentsHTML+="<input type='hidden' name='n_regi_psn' value=''>";//���������̵�
	ContentsHTML+="</form>";
	ContentsHTML+="<iframe id='mkfile_frame' style='display:none'></iframe>";//PC�� �����ϱ��

	document.write(ContentsHTML);
	
	
}

function makePgURL(_n){return "./"+nowMxxChap+"_"+TransTen(_n)+".html";}
function goPrePage(){(NowPage==1)?alert('ù ������ �Դϴ�.'):location.href=makePgURL((NumPage-1))}
function goNxtPage(){(NowPage==TotalPage)?alert('������ ������ �Դϴ�.'):location.href=makePgURL((NumPage+1));}
function goGrp(_n){
	var tT=1;
	for(var i=1;i<=TotalPage;i++){if(PageInfo[i][2]==_n){tT=i;break;}}
	(LastPage>=tT)?location.href="./"+nowMxxChap+"_"+TransTen(tT)+".html":alert("���������� �н��� ������ �ֽʽÿ�.");
	//����Ǯ��(�˼���)//location.href="./"+nowMxxChap+"_"+TransTen(tT)+".html";
}

function popAll(_n){var popSize='';popSize=confPopSize(nowPgName+"_pop"+_n);MM_openBrWindow(nowPgName+'_pop'+_n+'.html',nowPgName+'_pop'+_n,popSize);}
function downAll(_n){var downURL="../"+confDownURL(nowPgName+"_down"+_n);MM_openBrWindow('./common/down.htm?URL='+downURL,'download','width=600,height=227,top=0,left=0');}
for(var i=1;i<=5;i++){var popNum=TransTen(i);eval("pop"+popNum+"=function(){popAll('"+popNum+"');}");eval("down"+popNum+"=function(){downAll('"+popNum+"');}");}

function ldownAll(_n){var downURL="../"+nowMxxChap.substr(0,3)+"_common/down/"+nowMxxChap.substr(0,3)+"_lesson_"+_n;MM_openBrWindow('../'+nowMxxChap.substr(0,3)+'_common/ldown.htm?URL='+downURL+'.zip','download','width=500,height=180,top=0,left=0');}
function mdownAll(_n){var downURL="../"+nowMxxChap.substr(0,3)+"_common/down/"+nowMxxChap.substr(0,3)+"_down_"+_n;MM_openBrWindow('../'+nowMxxChap.substr(0,3)+'_common/mdown.htm?URL='+downURL+'.zip','download','width=500,height=180,top=0,left=0');}
for(var i=1;i<=14;i++){var popNum=TransTen(i);eval("ldown"+popNum+"=function(){ldownAll('"+popNum+"');}");eval("mdown"+popNum+"=function(){mdownAll('"+popNum+"');}");}

//�߰� : ���׹ڽ�
function popLearnBox(){MM_openBrWindow('../'+nowMxxChap.substr(0,3)+'_common/'+nowMxxChap.substr(0,3)+'_learningBox.html','learningBox','width=780,height=520,top=0,left=0');}

//�߰� : �����ư
function goNxtEnd(){
	ans=confirm("�н��� ��� ���ƽ��ϴ�. �����ϼ̽��ϴ�.\n\n�н��� �����Ϸ��� Ȯ�� ��ư�� Ŭ���ϼ���.\n");
	if(ans==true){
		if(typeof(parent.sno)!="undefined")top.endAPI();//�н������� �̵�
		else top.window.close();//������ ����
	}
}