var loca;
loca = document.location;

function __ws__(id){
document.write(id.text);id.id='';
}

function prevpage() {
	var page = document.location.href;
	var nowFile = page.indexOf(".htm"); // 해당문자의 위치를 반환	
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
	var nowFile = page.indexOf(".htm"); // 해당문자의 위치를 반환
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

//페이지 이동
function moveframe(movecode) {
    var movecode;
	//alert(movecode);
	var thispage = movecode.substr(2,3)
	//returnFile = movecode+".htm";
	//document.location.href = returnFile;
	//alert(endpage);	
    if (thispage > endpage){
		alert("이번 차시의 마지막 페이지 입니다.");
	}else if (thispage < 1){
		alert("이번 차시의 첫 페이지 입니다.");
	}else{		
		returnFile = movecode+".htm";
		document.location.href = returnFile;
	}
}


//lms commit하기 -- 진도완료
function lms_commit()
{
	//alert("진도저장!!");
	top.ContentExecutor.commit(true, 'Y');
}

var statusVal = "";
function returnLessonstatus(){

	try{
		//alert("aaaaaa");
		//statusVal = top.ContentExecutor.lessonstatus();
		statusVal = top.ContentExecutor.lessonstatus;
		//alert("statusVal = "+statusVal);
	}catch(e){
		statusVal="N";
	}
	return statusVal;
}

returnLessonstatus();