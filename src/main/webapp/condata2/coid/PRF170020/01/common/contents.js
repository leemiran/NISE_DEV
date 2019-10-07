//nowPgName ->	현재페이지명	-> ex) m02_01_08
//nowMxxChap ->	현재 모듈_차시	-> ex) m02_01
//alert("nowPgName="+nowPgName);
//alert("nowMxxChap="+nowMxxChap);


//part-01 : LMS 추가
function saveProgress(){//진도저장
	doLMSSetValue("cmi.core.lesson_location",nowPgName+".html");//기억
	
	if(typeof(top.studyMenuFrame)!="undefined"){//맛보기가 아니라면 -> 맛보기에는 studyMenuFrame가 없어서 에러가 남
		setProgress(NowPage+"/"+TotalPage); //SCOFunctions.js에 추가정의 되어 있음 : 'cmi.suspend_data'객체 사용하여 진도저장 + 마지막값 유지
	}
	
	if(NowPage==TotalPage)doLMSSetValue("cmi.core.lesson_status", "completed");//완료

	var jindoCk=doLMSGetValue("cmi.core.lesson_status");
	if(jindoCk!="passed" && jindoCk!="failed" && jindoCk!="completed" && jindoCk!="browsed"){
		var tProgress=doLMSGetValue("cmi.suspend_data");
		var tP=tProgress.split("/");
		LastPage=tP[0];
	}
	//alert("cmi.suspend_data=tProgress="+tProgress);
	//alert("LastPage="+LastPage);
	//alert("cmi.core.lesson_status="+doLMSGetValue("cmi.core.lesson_status"));
	//doLMSCommit(); //반영하면 하단이 초기화됨(사용못함)
}


//의견
function f_send_opinion(exam_no, frm){//의견올리기
	try{
		frm.d_opn_yy.value  =top.studyMenuFrame.menuForm.d_opn_yy.value;
		frm.i_cour.value    =top.studyMenuFrame.menuForm.i_cour.value;
		frm.o_sim_num.value =top.studyMenuFrame.menuForm.o_sim_num.value;
		frm.n_qna_pos1.value=exam_no;
		frm.action = "/nhome/lHNQuestionAction.do?method=insertFromContents";
		frm.submit();
		//window.close();
	}catch (ERROR){
		alert("LMS지원 메뉴입니다.");
	}
}
function f_dsp_opinion(exam_no){//다른사람 의견보기
	try{
		var v_opn_yy	=top.studyMenuFrame.menuForm.d_opn_yy.value;
		var v_cour	    =top.studyMenuFrame.menuForm.i_cour.value;
		var v_sim_num	=top.studyMenuFrame.menuForm.o_sim_num.value;
		var v_n_cour	=top.studyMenuFrame.menuForm.lecture_name.value;
		var url = "&z_d_opn_yy="+v_opn_yy+"&z_i_cour="+v_cour+"&z_o_sim_num="+v_sim_num+"&z_n_cour="+v_n_cour+"&z_i_noti=052008&z_n_qna_pos1="+exam_no;
		openWin = window.open("/nhome/lHNQuestionAction.do?method=selectsContents"+url,"","width=720,height=550,scrollbars=1");
	}catch (ERROR){
		alert("LMS지원 메뉴입니다.");
	}
}
function rtnOpNo(){var tOpN=Number(nowMxxChap.substr(1,2)+nowMxxChap.substr(4,2)+NowPage);return tOpN}//opinion number
function opinLst(){(typeof(parent.sno)!="undefined")?f_dsp_opinion(rtnOpNo()):alert("LMS지원 기능 입니다. - 다른 학습자 의견보기");}//<-
function opinWrt(_s){//<-
	if(typeof(parent.sno)!="undefined"){
		var tTxt2="";
		var tTxt=_s;
		var tOpTit=(tTxt.length>10)?tTxt.substr(0,28)+"...":tTxt;	
		for(i=0;i<tTxt.length;i++)(tTxt.charCodeAt(i)==13)?tTxt2+="<br>":tTxt2+=tTxt.charAt(i);
		SendFrom.n_titl.value=tOpTit;//제목
		SendFrom.n_ctnt.value=tTxt2;//내용
		f_send_opinion(rtnOpNo(),document.SendFrom);
	}else alert("LMS지원 기능 입니다. - 의견전송");
}


//part-02
var expDays=30;var exp = new Date();exp.setTime(exp.getTime()+(expDays*24*60*60*1000));
function inCookie(name,value){
	//document.cookie = name + "=" +value+"; expires=" + exp.toGMTString() + "; path=/;"
	document.cookie = name + "=" +value+"; expires=" + exp.toGMTString() + ";";
	//document.cookie = name + "=" +value+";";//<-임시용
}
function outCookie(name){var from_idx=document.cookie.indexOf(name+'=');if(from_idx != -1){from_idx+= name.length+1;to_idx = document.cookie.indexOf(';', from_idx);if(to_idx == -1)to_idx=document.cookie.length;return unescape(document.cookie.substring(from_idx, to_idx));}else{return ""}}

function enEnter(_t){var tTxt2="";var tTxt=_t;for(i=0;i<tTxt.length;i++)(tTxt.charCodeAt(i)==13)?tTxt2+="¿":tTxt2+=tTxt.charAt(i);return tTxt2}//enter 인코딩
function deEnter(_t){var tTxt2="";var tTxt=_t;for(i=0;i<tTxt.length;i++)(tTxt.charAt(i)=="¿")?tTxt2+=String.fromCharCode(13):tTxt2+=tTxt.charAt(i);return tTxt2}//enter 디코딩
function ckSaveNote(name,value){var tTxt=enEnter(value);inCookie(name,tTxt);}//노트 cookie save
function ckLoadNote(name){var tTxt=deEnter(outCookie(name));return tTxt;}//노트 cookie load
function mkFileNote(value,Tit){//노트 save file - html
	var tTitle=Tit;
	var tTxt2="";
	var tTxt=value;
	for(i=0;i<tTxt.length;i++)(tTxt.charCodeAt(i)==13)?tTxt2+="<br>":tTxt2+=tTxt.charAt(i);

	var mkHTML="";
	mkHTML+="<html><head><title>"+tTitle+"</title><meta http-equiv='Content-Type' content='text/html; charset=euc-kr'><style>table{font-family:굴림;font-size:12px;line-height:18px;color:#000000}</style>"
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


//part-03 : 고정
var NumPage=Number(thisPageNum);NowPage=NumPage;
var TotalPage=pageinfo.length-1;
var LastPage=TotalPage;//<-추가:학습한 마지막페이지

//진도저장: 순서바뀌면 안됨 NumPage,TotalPage 필요
if(typeof(parent.sno)!="undefined")saveProgress();//saveProgress()<-상단에 정의
saveProgress();
//swfName=nowPgName+".swf";
//swfName="./common/player.swf?LodCnt=1&ConURL1=swf/"+swfName;
//swfName+="&NPage="+NowPage+"&TPage="+TotalPage+"&Script="+PageInfo[NowPage][0]+"&PlayView="+PageInfo[NowPage][1];
//swfName+="&nowMxxChap="+nowMxxChap+"&nowPgName="+nowPgName;
//swfName+="&GrpNum="+PageInfo[NowPage][2];//Group값
//swfName+="&plyTy="+PageInfo[NowPage][3];//play type: 일반, flv1/2 구분
//swfName+="&Sudden="+PageInfo[NowPage][4];//break
//swfName+="&treeIdx="+PageInfo[NowPage][5];//treeIdx
//swfName+="&FlvConnURL="+FlvConnURL+"&FlvDir="+FlvDir+"&flvFile="+nowPgName;//flv
//swfName+="&FpType="+FpType;//유형
//swfName+="&FpMOpnPg="+FpMOpnPg;//모듈오픈페이지수
//swfName+="&FpMClsPg="+FpMClsPg;//모듈클로징페이지수
//alert(swfName);

function writeContents(){
	//var ContentsHTML="<div>"+SwfViewRtn(swfName,'swfcontent',1014,597)+"</div>";
	document.write("<iframe id='hidden_frame' width='0' height='0' frameborder='0' src=''></iframe>");
	var ContentsHTML="";

	//임시
	if(typeof(parent.sno)!="undefined"){ContentsHTML+="<font color='white'>"+doLMSGetValue("cmi.core.lesson_status")+":"+LastPage+"</font>";}
	
	//추가
	ContentsHTML+="<iframe name='lmspage' id='lmspage' src='about:blank' width='100%' marginwidth='0' height='100%' marginheight='0' scrolling='no' frameborder='0' style='display:none'></iframe>";
	ContentsHTML+="<form name='SendFrom' method='post' style='display:none' target = 'lmspage'>";
	//ContentsHTML+="<form name='SendFrom' method='post' style='display:none' target = 'sendFrame'>";
	ContentsHTML+="<input type='radio' name='n_qna_pos2' value='Y'><input type='radio' name='n_qna_pos2' value='N'>";//수강생답안
	ContentsHTML+="<input name='n_titl' value=''><textarea name='n_ctnt'></textarea>";//제목/내용
	ContentsHTML+="<input type='hidden' name='command' value=''>";//실행구분자
	ContentsHTML+="<input type='hidden' name='R0N' value='true'>";//데이터유무
	ContentsHTML+="<input type='hidden' name='d_opn_yy' value=''>";//년도
	ContentsHTML+="<input type='hidden' name='i_cour' value=''>";//과정
	ContentsHTML+="<input type='hidden' name='o_sim_num' value=''>";//기수
	ContentsHTML+="<input type='hidden' name='n_qna_pos1' value=''>";//문제번호
	ContentsHTML+="<input type='hidden' name='n_regi_psn' value=''>";//수강생아이디
	ContentsHTML+="</form>";
	ContentsHTML+="<iframe id='mkfile_frame' style='display:none'></iframe>";//PC에 저장하기용

	document.write(ContentsHTML);
	
	
}

function makePgURL(_n){return "./"+nowMxxChap+"_"+TransTen(_n)+".html";}
function goPrePage(){(NowPage==1)?alert('첫 페이지 입니다.'):location.href=makePgURL((NumPage-1))}
function goNxtPage(){(NowPage==TotalPage)?alert('마지막 페이지 입니다.'):location.href=makePgURL((NumPage+1));}
function goGrp(_n){
	var tT=1;
	for(var i=1;i<=TotalPage;i++){if(PageInfo[i][2]==_n){tT=i;break;}}
	(LastPage>=tT)?location.href="./"+nowMxxChap+"_"+TransTen(tT)+".html":alert("순차적으로 학습을 진행해 주십시요.");
	//제한풀기(검수용)//location.href="./"+nowMxxChap+"_"+TransTen(tT)+".html";
}

function popAll(_n){var popSize='';popSize=confPopSize(nowPgName+"_pop"+_n);MM_openBrWindow(nowPgName+'_pop'+_n+'.html',nowPgName+'_pop'+_n,popSize);}
function downAll(_n){var downURL="../"+confDownURL(nowPgName+"_down"+_n);MM_openBrWindow('./common/down.htm?URL='+downURL,'download','width=600,height=227,top=0,left=0');}
for(var i=1;i<=5;i++){var popNum=TransTen(i);eval("pop"+popNum+"=function(){popAll('"+popNum+"');}");eval("down"+popNum+"=function(){downAll('"+popNum+"');}");}

function ldownAll(_n){var downURL="../"+nowMxxChap.substr(0,3)+"_common/down/"+nowMxxChap.substr(0,3)+"_lesson_"+_n;MM_openBrWindow('../'+nowMxxChap.substr(0,3)+'_common/ldown.htm?URL='+downURL+'.zip','download','width=500,height=180,top=0,left=0');}
function mdownAll(_n){var downURL="../"+nowMxxChap.substr(0,3)+"_common/down/"+nowMxxChap.substr(0,3)+"_down_"+_n;MM_openBrWindow('../'+nowMxxChap.substr(0,3)+'_common/mdown.htm?URL='+downURL+'.zip','download','width=500,height=180,top=0,left=0');}
for(var i=1;i<=14;i++){var popNum=TransTen(i);eval("ldown"+popNum+"=function(){ldownAll('"+popNum+"');}");eval("mdown"+popNum+"=function(){mdownAll('"+popNum+"');}");}

//추가 : 러닝박스
function popLearnBox(){MM_openBrWindow('../'+nowMxxChap.substr(0,3)+'_common/'+nowMxxChap.substr(0,3)+'_learningBox.html','learningBox','width=780,height=520,top=0,left=0');}

//추가 : 종료버튼
function goNxtEnd(){
	ans=confirm("학습을 모두 마쳤습니다. 수고하셨습니다.\n\n학습을 종료하려면 확인 버튼을 클릭하세요.\n");
	if(ans==true){
		if(typeof(parent.sno)!="undefined")top.endAPI();//학습목차로 이동
		else top.window.close();//윈도우 종료
	}
}