var loca;
loca = document.location;

function __ws__(id){
document.write(id.text);id.id='';
}

////2017.1.6//////////////////////
function goPreviewAct() {
	prevpage();
}

function goNextAct() {
	nextpage();
}

function gotoSubAct(num1,num2,num3){
	gotoPageNum=1;
	if(num2==0){
		num2=1;
	}
	for(i=0;i<pageinfo.length;i++){
		if(num1==pageinfo[i][0] && num2==pageinfo[i][5]){
			
			gotoPageNum=pageinfo[i][1];
			break;
		}
	}
	//return;
	moveframe(itostr(thisModuleNum)+itostr(gotoPageNum));
}

/////2017.1.6/////////////////////
function prevpage() {
        var page = document.location.href;
        var nowFile = page.indexOf(".html"); // 해당문자의 위치를 반환
        var chCode = page.substring(nowFile-4, nowFile-2);
        var nowCode = page.substring(nowFile-2, nowFile);
        var chaseCode =parseInt(chCode,10)
        if (chaseCode < 10)
        {
                chaseCode = "0"+chaseCode;
        }
        chaseCode = chaseCode
        var prevCode =parseInt(nowCode,10) - 1;
        if (prevCode < 10)
        {
                prevCode = "0"+prevCode;
        }
        moveframe(String(chaseCode)+String(prevCode));
}
//다음페이지
function nextpage() {
        var page = document.location.href;
        var nowFile = page.indexOf(".html"); // 해당문자의 위치를 반환
        var chCode = page.substring(nowFile-4, nowFile-2);
        var nowCode = page.substring(nowFile-2, nowFile);
    var chaseCode =parseInt(chCode,10)
        if (chaseCode < 10)
        {
                chaseCode = "0"+chaseCode;
        }
        chaseCode = chaseCode
        var nextCode =parseInt(nowCode,10) + 1;
        if (nextCode < 10)
        {
                nextCode = "0"+nextCode;
        }
    moveframe(String(chaseCode)+String(nextCode));
}
//전체 페이지 수신
function totalPage(num){
   endpage = num
}


//lms commit하기 -- 진도완료
function lms_commit()
{
        //alert("진도저장!!");
        top.ContentExecutor.commit(false, 'N');
}

var statusVal = "";
function returnLessonstatus(){
        try{
                //statusVal = top.ContentExecutor.lessonstatus();
                statusVal = top.ContentExecutor.lessonstatus;
        }catch(e){
                statusVal="N";
        }
        return statusVal;
}

returnLessonstatus();