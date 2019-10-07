/******************************************************************/
/*  KPC 포팅용 스크립트                                                                               */
/*  작성자 : 임채언(imchaeun@hanmail.net)                                          */
/*  작성일 : 2007년 04월 09일                                                                       */
/*  내   용 : 프레임용                                                                                    */
/******************************************************************/

//로컬용 서버용 구분
var mode = true; //Server : true
//var mode = false; //HDD : false

//과정 차시 정보
var Chasi_Num = 4; //해당 차시 개수를 입력

/********************************************/
// Cookies 초기화 셋팅 시작
/********************************************/
function setCookie (name,value,expires,path,domain,secure) {
  document.cookie = name + "=" + escape (value) +
    ((expires) ? "; expires=" + expires.toGMTString() : "") +
    ((path) ? "; path=" + path : "") +
    ((domain) ? "; domain=" + domain : "") +
    ((secure) ? "; secure" : "");
}

function getCookieVal (offset) {
  var endstr = document.cookie.indexOf (";", offset);
  if (endstr == -1)
    endstr = document.cookie.length;
  return unescape(document.cookie.substring(offset, endstr));
}

function getCookie (name) {
        var arg = name + "=";
        var alen = arg.length;
        var clen = document.cookie.length;
        var i = 0;
        while (i < clen) {
            var j = i + alen;
            if (document.cookie.substring(i, j) == arg)
              return getCookieVal (j);
            i = document.cookie.indexOf(" ", i) + 1;
            if (i == 0) break; 
        }
        return null;
}
/********************************************/
// Cookies 초기화 셋팅 끝
/********************************************/

// 진도 저장
/********************************************/
function complete_html(pinf) {
	var href = document.location.href; //파일 url 가져오기
	var s = href.split("/"); // "/"으로 배열 만들기
	chasi = s[s.length-2]; //폴더명(차시) 가져오기
	var sinf=chasi+pinf+"01"+pinf;
	setCookie("sinf", sinf, null, "/");
	if(mode==true){
		top.navi.on_exit();
	}else{
		//HDD로 설정시 상단에 저장되는 값 확인가능, 예: 01010101을 DB에 저장
		//top.document.title = sinf+"을 DB에 저장";
		top.status=sinf+"을 DB에 저장";
	}
}
/********************************************/

/********************************************/
// 다음 차시로 이동
/********************************************/
function next_chasi() {
	var now_chasi = Number(getCookie("sinf").substring(0,2));

	if(now_chasi >= Chasi_Num) {
		var ans = confirm("\n수고하셨습니다.\n수강중인 과정의 마지막 페이지 입니다.\n목차로 이동하시겠습니까?   \n");
		if (ans == true) {
			top.location = "../common/main.htm";
		}
	} else {
		var next_chasi = itostr(eval(now_chasi+1));
		alert("\n수고 하셨습니다. \n\n다음 차시로 이동합니다.   \n");
		callhtml(next_chasi);
	}
}
/********************************************/

/********************************************/
//목차에서 차시 이동
/********************************************/
function callhtml(i) {
        cinf = i + "010101";
        setCookie("sinf", cinf, null, "/");
        location.href ="../"+i+"/"+i+"_01.html";
}
/********************************************/

/********************************************/
//목차에서 차시 이동2
/********************************************/
function callhtml2(i) {
        cinf = i + "010101";
        setCookie("sinf", cinf, null, "/");
        location.href ="../"+i+"/"+i+"_01.html";
}
/********************************************/

/********************************************/
//숫자를 문자로 치환
/********************************************/
function itostr(num) {
        if (num < 10) str = "0";
        else str = "";

        str = str + num;
        return str;
}
/********************************************/

/********************************************/
//학습 종료 목차로 이동
/********************************************/
function callexit2() {
		ans = confirm("\n학습을 끝마치려 합니다.\n\n정말 종료하시겠습니까?\n\n확인 버튼을 클릭하면 종료합니다.\n");
		if (ans == true) {
		top.close();
	}
}
/********************************************/

/********************************************/
//목차로 이동
/********************************************/
function on_list() {
	parent.location = "main.htm";
}
/********************************************/

/********************************************/
//목차로 이동
/********************************************/
function on_list1() {
	parent.location = "../common/main.htm";
}
/********************************************/

/********************************************/
//지난학습 이어하기
/********************************************/
function last_study(){
	var sLen = getCookie("sinf"); //쿠기에서 차시정보 가져오기
	var g_chasi = sLen.substring(0,2);	//쿠키에서 차시 정보만 자르기
	var g_page = sLen.substring(6,8);	//쿠키에서 page 정보만 자르기

	//setCookie("bookmark", 1, null, "/");
	if (g_page == "" || g_page == null)
	{
		location.href = "../01/01_01.html";
	}else{
		location.href = "../"+g_chasi+"/"+g_chasi+"_"+g_page+".html";
	}
	
}

/********************************************/
// LMS 메뉴
/********************************************/
function lmsFunction(num){
	if(mode==false){
		switch(num){
			case 0 : window.open('../index_00.htm','','status=no,scrollbars=no,resizable=no,width=890,height=685'); 
				break;
			case 1 : parent.parent.navi.on_list1();
				break;
			case 2 : parent.parent.navi.on_proc(); 
				break;
			case 3 : parent.parent.navi.on_qna(); 
				break;
			case 4 : parent.parent.navi.on_data(); 
				break;
			case 5 : parent.parent.navi.on_test(); 
				break;
			case 6 : parent.parent.navi.on_report(); 
				break;
			case 7 : parent.parent.navi.on_discuss(); 
				break;
			case 8 : parent.parent.navi.on_survey(); 
				break;
			case 9 : parent.parent.navi.callexit2(); 
				break;
				/*
			case 0 : window.open('../index_00.htm','','status=no,scrollbars=no,resizable=no,width=890,height=685'); 
				break;
			case 1 : parent.parent.navi.on_list1();
				break;
			case 2 : alert("진도는 맛보기에서 제공하지 않는 기능입니다.   ");
				break;
			case 3 : alert("질문은 맛보기에서 제공하지 않는 기능입니다.   ");  
				break;
			case 4 : alert("자료실은 맛보기에서 제공하지 않는 기능입니다.   ");  
				break;
			case 5 : alert("시험은 맛보기에서 제공하지 않는 기능입니다.   ");  
				break;
			case 6 : alert("Q&A는 맛보기에서 제공하지 않는 기능입니다.   ");  
				break;
			case 7 : alert("토론은 맛보기에서 제공하지 않는 기능입니다.   "); 
				break;
			case 8 : alert("설문은 맛보기에서 제공하지 않는 기능입니다.   "); 
				break;
			case 9 : parent.parent.navi.callexit2(); 
				break;
				*/
		}
	}else{
		switch(num){
			case 0 : window.open('../index_00.htm','','status=no,scrollbars=no,resizable=no,width=890,height=685'); 
				break;
			case 1 : parent.parent.navi.on_list1();
				break;
			case 2 : parent.parent.navi.on_proc(); 
				break;
			case 3 : parent.parent.navi.on_qna(); 
				break;
			case 4 : parent.parent.navi.on_data(); 
				break;
			case 5 : parent.parent.navi.on_test(); 
				break;
			case 6 : parent.parent.navi.on_report(); 
				break;
			case 7 : parent.parent.navi.on_discuss(); 
				break;
			case 8 : parent.parent.navi.on_survey(); 
				break;
			case 9 : parent.parent.navi.callexit2(); 
				break;
		}
	}
}
// 의견입력하기
//------------------------------------------//
// 내의견 입력
function my_on_opinion(opinionno,contents) {
	if(mode==false){
		alert("[로컬] " + opinionno + "번 의견입력 : " + contents );
		//alert("미션 수행 : 저장하기는 LMS 기능입니다.");
	}else{
	  //  alert("[test] " + opinionno + "번 의견입력 : " + contents );
		parent.parent.navi.on_opinion2(opinionno,contents);
	}
}


// 다른사람 의견 보기
function my_on_opinionlst(opinionno){
	if(mode==false){
		alert("[로컬] " + opinionno + "번 의견보기 ");
		//alert("미션 수행 : 다른사람 의견보기는 LMS 기능입니다.");
	}else{
	    //alert("[test] " + opinionno + "번 의견보기 ");
		parent.parent.navi.on_opinionlst2(opinionno);
	}
}
//------------------------------------------//

//교안다운로드
function nh_down(){
	var href = document.location.href; //파일 url 가져오기
	var s = href.split("/"); // "/"으로 배열 만들기
	chasi = s[s.length-2]; //폴더명(차시) 가져오기
	window.open('../down/down.htm?URL='+chasi+'.zip',"_blank","status=no, scrollbars=no, resizable=no, width=500, height=200");
	//window.open("../down/"+chasi+".zip","_blank","status=yes, scrollbars=yes, resizable=yes, width=500, height=400"); 
}