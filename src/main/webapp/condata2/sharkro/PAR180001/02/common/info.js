

// html 기본 정보
//-----------------------------------------------------------------------------------------------
var swf = "swf/main.swf"
var swfWidth = 1024
var swfHeight = 710
var bgColor = "#FFFFFF"
var id = "contents"
var access = "always"
var flashVars = "pageNum="
//-----------------------------------------------------------------------------------------------

// Flash 기본 정보
//-----------------------------------------------------------------------------------------------
var isForm = "local" // 로컬용 local, 웹용 web 선택
var isDrag = "Y" // 제어바 드래그 가능여부 선택
var isFinishPage = "Y" // 종료 말풍선 선택
var isChaseNum = "02" // 현재 차시 입력
var isTotalNum = "08" // 현재 차시 전체 페이지 수 입력
var isNextPage = "N" // 학습 끝까지 다 보기 말풍선 선택





// next말풍선 비활성화    
balloonArr =new Array();
balloonArr = ["07","08"]// 페이지입력    없을땐 "00"

// 과정명 비활성화  
visibleObjectP =new Array();
visibleObjectP = ["01"]// 페이지입력

// 차시명 비활성화  
visibleObjectT =new Array();
visibleObjectT = ["01"]// 페이지입력

// 메뉴 비활성화    
visibleObjectM =new Array();
visibleObjectM = ["01"]// 페이지입력

// 컨트롤바 비활성화    
visibleObjectC =new Array();
visibleObjectC = ["00"]// 페이지입력





//flv 페이지 셋팅 
//flv 차시별 페이지 입력
FLVpage =new Array();
FLVpage = ["00"]// 차시입력  flv페이지 없을땐 "00"
//-----------------------------------------------------------------------------------------------

// wmv 페이지 셋팅
// wmv 차시별 페이지 입력
WMVpage =new Array();
WMVpage = ["00"]// 페이지입력 없을땐 "00"
//-----------------------------------------------------------------------------------------------



/*

// 현재 페이지 번호 추출
//-----------------------------------------------------------------------------------------------
try{
	// 현재 URL, Chapter, Page NO.
	var cURL = this.location.href;
//	var preStr = cURL.substring( cURL.lastIndexOf('/') + 1 , cURL.lastIndexOf('/') + 3);  // ak
	var curPageStr = cURL.substring( cURL.lastIndexOf('/') + 5 , cURL.lastIndexOf('/') + 3); // 01
//	var curChapStr = cURL.substring( cURL.lastIndexOf('/') + 4 , cURL.lastIndexOf('/') + 3); // 01
//	var curPageNo = Number(curChapStr); // 1
//	var swfSrc =  curPageStr +"_" +curChapStr + ".swf";  // 01_01.swf 형식일대 
//	var curFoler = cURL.substring( cURL.lastIndexOf('/')-5 , cURL.lastIndexOf('/')  );
	//var swfSrc =  "swf/0" +curChapStr + ".swf";  // 001.swf 형식일때
	//alert(curPageStr)
}catch(e){}

*/
//-----------------------------------------------------------------------------------------------




function wmvSet() {
	//IE8에서 동영상이 보이지 않는 오류를 해결하기 위해 patch레이어 사용. text가 삽입이 되어야 동영상이 보임(원인 파악 불가)
	//patch레이어 삭제 금지 (2014년 5월 31일 RGB 김종완 팀장)
	document.write("<div id='patch' style='position:absolute; left:0px; top:0px; width:0px; height:0px; z-index:0; visibility:hidden; '>.</div>");
	document.write("<script type='text/javascript' src='./common/wmv/jquery.js'></script>");
	document.write("<script type='text/javascript' src='./common/wmv/movie.js'></script>");
	document.write("<script type='text/javascript' src='./common/wmv/player.js'></script>");
	document.write("<script type='text/javascript' src='./common/wmv/loader.js'></script>");
}


function setDrag() { 
	return isDrag
}

function setFinishPage() { 
	return isFinishPage
}

function setChaseNum() { 
	return isChaseNum
}

function setTotalNum() { 
	return isTotalNum
}

function setNextPage() { 
	return isNextPage
}
	
function setForm() { 
	return isForm
}

// next 말풍선 비활성 지정    
function setballoon(){ 
	 var pageNum;
		//alert(this_page);
	for(var i=0; i<=balloonArr.length; i++){
    	if(balloonArr[i] == pagenum){
            pageNum = balloonArr[i] 
			//alert(pageNum);
		}
	}		
	return pageNum;
}

// 비활성화 과정명 페이지넘버 비교    
function setVisPageP(){ 
	 var pageNum;
		//alert(this_page);
	for(var i=0; i<=visibleObjectP.length; i++){
    	if(visibleObjectP[i] == pagenum){
            pageNum = visibleObjectP[i] 
			//alert(pageNum);
		}
	}		
	return pageNum;
}

// 비활성화 차시명 페이지넘버 비교    
function setVisPageT(){ 
	 var pageNum;
		//alert(this_page);
	for(var i=0; i<=visibleObjectT.length; i++){
    	if(visibleObjectT[i] == pagenum){
            pageNum = visibleObjectT[i] 
			//alert(pageNum);
		}
	}		
	return pageNum;
}
// 비활성화 메뉴 페이지넘버 비교    
function setVisPageM(){ 
	 var pageNum;
		//alert(this_page);
	for(var i=0; i<=visibleObjectM.length; i++){
    	if(visibleObjectM[i] == pagenum){
            pageNum = visibleObjectM[i] 
			//alert(pageNum);
		}
	}		
	return pageNum;
}
// 비활성화 컨트롤 페이지넘버 비교    
function setVisPageC(){ 
	 var pageNum;
		//alert(this_page);
	for(var i=0; i<=visibleObjectC.length; i++){
    	if(visibleObjectC[i] == pagenum){
            pageNum = visibleObjectC[i] 
			//alert(pageNum);
		}
	}		
	return pageNum;
}
// flv 페이지넘버 지정    
function setFlvPage(){ 
	 var FLVFlag = false;
		//alert(this_page);
	for(var i=0; i<=FLVpage.length; i++){
    	if(FLVpage[i] == pagenum){
            pageNum = FLVpage[i] 
			FLVFlag = true;
			break;
		 //  alert(pageNum);
		}
	}		
	return FLVFlag;
}

// flv 페이지넘버 지정    
function setFlvNum(){ 
	// var FLVFlag = false;
		//alert(this_page);
	for(var i=0; i<=FLVpage.length; i++){
    	if(FLVpage[i] == pagenum){
            pageNum = FLVpage[i] 
		//	FLVFlag = true;
			break;
		 //  alert(pageNum);
		}
	}		
	return pageNum;
}


// WMV 페이지넘버 지정    
function setWmvPage(){ 
	 var WMVFlag = false;
		//alert(this_page);
	for(var i=0; i<=WMVpage.length; i++){
    	if(WMVpage[i] == pagenum){
            WMVFlag = true;
			break;
		}
	}		
	return WMVFlag;
}


// wmv 시작
function WmvInit(){
	var WMVpageYN = setWmvPage();
	//alert(WMVpageYN);
	if (WMVpageYN == true) 
	{
		video_play();
	}
}

// 학습 시작
function onStart() { 
	document.getElementById('contents').focus()
	//alert("학습 시작")
}

// 현재 페이지 완료
function onEnd() { 
	//alert("페이지 완료")
}

// 이번 차시 마지막 페이지 완료
function onFinish() { 
	//alert("마지막 페이지 완료")
}


//진도 제어 추가 2016.04.28
function setPageControl(value){
//시스템에서 페이지 정보 확인


}
function getPageControl(){
//시스템에서 페이지 정보 확인

}


//교안 다운로드
function chkDown() {
	//alert("시연용에서는 지원하지 않습니다.")
	downVal = "guideline.zip";
	authwin = window.open("down/down.htm", "downPage", "menubar=no,toolbar=no,location=no,status=no,scrollbars=no,resizable=no,left=100,top=100,width=250,height=150");
}


//링크열기
function popLink() { 
	authwin = window.open("http://www.ips.co.kr", "_blank");
}
