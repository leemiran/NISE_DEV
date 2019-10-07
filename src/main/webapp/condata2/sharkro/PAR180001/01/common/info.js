

// html �⺻ ����
//-----------------------------------------------------------------------------------------------
var swf = "swf/main.swf"
var swfWidth = 1024
var swfHeight = 710
var bgColor = "#FFFFFF"
var id = "contents"
var access = "always"
var flashVars = "pageNum="
//-----------------------------------------------------------------------------------------------

// Flash �⺻ ����
//-----------------------------------------------------------------------------------------------
var isForm = "local" // ���ÿ� local, ���� web ����
var isDrag = "Y" // ����� �巡�� ���ɿ��� ����
var isFinishPage = "Y" // ���� ��ǳ�� ����
var isChaseNum = "02" // ���� ���� �Է�
var isTotalNum = "08" // ���� ���� ��ü ������ �� �Է�
var isNextPage = "N" // �н� ������ �� ���� ��ǳ�� ����





// next��ǳ�� ��Ȱ��ȭ    
balloonArr =new Array();
balloonArr = ["07","08"]// �������Է�    ������ "00"

// ������ ��Ȱ��ȭ  
visibleObjectP =new Array();
visibleObjectP = ["01"]// �������Է�

// ���ø� ��Ȱ��ȭ  
visibleObjectT =new Array();
visibleObjectT = ["01"]// �������Է�

// �޴� ��Ȱ��ȭ    
visibleObjectM =new Array();
visibleObjectM = ["01"]// �������Է�

// ��Ʈ�ѹ� ��Ȱ��ȭ    
visibleObjectC =new Array();
visibleObjectC = ["00"]// �������Է�





//flv ������ ���� 
//flv ���ú� ������ �Է�
FLVpage =new Array();
FLVpage = ["00"]// �����Է�  flv������ ������ "00"
//-----------------------------------------------------------------------------------------------

// wmv ������ ����
// wmv ���ú� ������ �Է�
WMVpage =new Array();
WMVpage = ["00"]// �������Է� ������ "00"
//-----------------------------------------------------------------------------------------------



/*

// ���� ������ ��ȣ ����
//-----------------------------------------------------------------------------------------------
try{
	// ���� URL, Chapter, Page NO.
	var cURL = this.location.href;
//	var preStr = cURL.substring( cURL.lastIndexOf('/') + 1 , cURL.lastIndexOf('/') + 3);  // ak
	var curPageStr = cURL.substring( cURL.lastIndexOf('/') + 5 , cURL.lastIndexOf('/') + 3); // 01
//	var curChapStr = cURL.substring( cURL.lastIndexOf('/') + 4 , cURL.lastIndexOf('/') + 3); // 01
//	var curPageNo = Number(curChapStr); // 1
//	var swfSrc =  curPageStr +"_" +curChapStr + ".swf";  // 01_01.swf ����ϴ� 
//	var curFoler = cURL.substring( cURL.lastIndexOf('/')-5 , cURL.lastIndexOf('/')  );
	//var swfSrc =  "swf/0" +curChapStr + ".swf";  // 001.swf ����϶�
	//alert(curPageStr)
}catch(e){}

*/
//-----------------------------------------------------------------------------------------------




function wmvSet() {
	//IE8���� �������� ������ �ʴ� ���� �ذ��ϱ� ���� patch���̾� ���. text�� ������ �Ǿ�� �������� ����(���� �ľ� �Ұ�)
	//patch���̾� ���� ���� (2014�� 5�� 31�� RGB ������ ����)
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

// next ��ǳ�� ��Ȱ�� ����    
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

// ��Ȱ��ȭ ������ �������ѹ� ��    
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

// ��Ȱ��ȭ ���ø� �������ѹ� ��    
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
// ��Ȱ��ȭ �޴� �������ѹ� ��    
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
// ��Ȱ��ȭ ��Ʈ�� �������ѹ� ��    
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
// flv �������ѹ� ����    
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

// flv �������ѹ� ����    
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


// WMV �������ѹ� ����    
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


// wmv ����
function WmvInit(){
	var WMVpageYN = setWmvPage();
	//alert(WMVpageYN);
	if (WMVpageYN == true) 
	{
		video_play();
	}
}

// �н� ����
function onStart() { 
	document.getElementById('contents').focus()
	//alert("�н� ����")
}

// ���� ������ �Ϸ�
function onEnd() { 
	//alert("������ �Ϸ�")
}

// �̹� ���� ������ ������ �Ϸ�
function onFinish() { 
	//alert("������ ������ �Ϸ�")
}


//�� ���� �߰� 2016.04.28
function setPageControl(value){
//�ý��ۿ��� ������ ���� Ȯ��


}
function getPageControl(){
//�ý��ۿ��� ������ ���� Ȯ��

}


//���� �ٿ�ε�
function chkDown() {
	//alert("�ÿ��뿡���� �������� �ʽ��ϴ�.")
	downVal = "guideline.zip";
	authwin = window.open("down/down.htm", "downPage", "menubar=no,toolbar=no,location=no,status=no,scrollbars=no,resizable=no,left=100,top=100,width=250,height=150");
}


//��ũ����
function popLink() { 
	authwin = window.open("http://www.ips.co.kr", "_blank");
}
