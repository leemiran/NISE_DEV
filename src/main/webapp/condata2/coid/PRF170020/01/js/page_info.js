//////////////////////////////////////////차시별 수정 시작////////////////////////////////
var thisModuleNum=01;/////////차시 번호
pageinfo=new Array();
// 페이지별 정보입니다, 절, page, 해당하는 실제페이지명, 동영상페이지여부(m), 하위절 입니다.

//////////////////들어가기///////////////////////////////////////////////////////////////
pageinfo[0]=new Array(1,1,"0101.html","0101.swf","f",1,"bg01.swf",0);//Opening


//////////////////안전뉴스///////////////////////////////////////////////////////////////
pageinfo[1]=new Array(2,2,"0102.html","0102.swf","f",1,"bg01.swf",1);//


//////////////////학습안내///////////////////////////////////////////////////////////////
pageinfo[2]=new Array(3,3,"0103.html","0103.swf","f",1,"bg01.swf",1);//
pageinfo[3]=new Array(3,4,"0104.html","0104.swf","f",1,"bg01.swf",1);//
pageinfo[4]=new Array(3,5,"0105.html","0105.swf","f",2,"bg01.swf",1);//
pageinfo[5]=new Array(3,6,"0106.html","0106.swf","f",2,"bg01.swf",1);//
pageinfo[6]=new Array(3,7,"0107.html","0107.swf","f",2,"bg01.swf",1);//
pageinfo[7]=new Array(3,8,"0108.html","0108.swf","f",2,"bg01.swf",1);//
pageinfo[8]=new Array(3,9,"0109.html","0109.swf","f",2,"bg01.swf",1);//
pageinfo[9]=new Array(3,10,"0110.html","0110.swf","f",2,"bg01.swf",1);//
pageinfo[10]=new Array(3,11,"0111.html","0111.swf","f",2,"bg01.swf",1);//
pageinfo[11]=new Array(3,12,"0112.html","0112.swf","f",3,"bg01.swf",1);//
pageinfo[12]=new Array(3,13,"0113.html","0113.swf","f",3,"bg01.swf",1);//
pageinfo[13]=new Array(3,14,"0114.html","0114.swf","f",3,"bg01.swf",1);//
pageinfo[14]=new Array(3,15,"0115.html","0115.swf","f",4,"bg01.swf",1);//
pageinfo[15]=new Array(3,16,"0116.html","0116.swf","f",4,"bg01.swf",1);//

//////////////////평가하기///////////////////////////////////////////////////////////////
pageinfo[16]=new Array(4,17,"0117.html","0117.swf","f",1,"bg01.swf",1);//핵심포인트

//////////////////정리하기///////////////////////////////////////////////////////////////
pageinfo[17]=new Array(5,18,"0118.html","0118.swf","f",1,"bg01.swf",1);//핵심포인트

//////////////////학습마무리///////////////////////////////////////////////////////////////
pageinfo[18]=new Array(6,19,"0119.html","0119.swf","f",1,"bg01.swf",1);//핵심포인트




///////////////////////////////////////////차시별 수정 끝///////////////////////////////////////

var opnTitle="기술과 평가";


var pageCueArray=new Array();
pageCueArray[7]=new Array();
pageCueArray[10]=new Array();
pageCueArray[13]=new Array();
pageCueArray[16]=new Array();
pageCueArray[20]=new Array();

//pageCueArray[페이지][프레임 번호]=동영상의 시작시간
pageCueArray[7][1]=0;
pageCueArray[7][2]=5;
pageCueArray[7][3]=3*60+16;
pageCueArray[7][4]=5*60;

pageCueArray[10][1]=0;
pageCueArray[10][2]=5;
pageCueArray[10][3]=3*60+9;

pageCueArray[13][1]=0;
pageCueArray[13][2]=6;

pageCueArray[16][1]=0;


pageCueArray[20][1]=0;


var tmpName=document.location.href.split("?")[0];
tmpName=tmpName.split("/");
thisPageName=tmpName[tmpName.length-1];

var thisChapNum=thisModuleNum;
thisPageNum=1;
thisPageInfo=1;//절대 페이지 번호
thisSubjectNum=1;
//totalPageNum=pageinfo.length-1;
totalPageNum=pageinfo.length;
loadPage="";
movieInfo="f";
thisS_SubNum=0;

var swfWidth=1014;
var swfHeight=656;
var swfID="content"
var bg_name="bg01.swf";
var hideFlag=1;

for(i=0;i<pageinfo.length;i++){
	if(thisPageName==pageinfo[i][2]){
		thisPageNum=pageinfo[i][1];
		thisSubjectNum=pageinfo[i][0];
		loadPage="swf/"+pageinfo[i][3];
		thisPageInfo=i;
		movieInfo=pageinfo[i][4];
		thisS_SubNum=pageinfo[i][5];
		bg_name=pageinfo[i][6];
		hideFlag=pageinfo[i][7];
	}
}
thisPageInfoNum=thisPageInfo;
var my_flashVars = "thisModuleNum="+thisModuleNum+"&thisSubjectNum="+thisSubjectNum+"&thisPageNum="+thisPageNum;
my_flashVars += "&bg_name="+bg_name+"&totalPageNum="+totalPageNum+"&loadPage="+loadPage+"&movieInfo="+movieInfo;
my_flashVars += "&thisS_SubNum="+thisS_SubNum+"&thisChapNum="+thisChapNum+"&thisPageInfoNum="+thisPageInfoNum+"&hideFlag="+hideFlag;

///////////////////2009.7.3 //////////////////////////////////
nowPgName=thisPageName.split(".")[0];
///////////////////2009.7.3 //////////////////////////////////
