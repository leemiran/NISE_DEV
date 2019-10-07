var intValue = '0123456789.';
var upperValue = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
var lowerValue = 'abcdefghijklmnopqrstuvwxyz';
//var etcValue = ' ~`!@#$%%^&*()-_=+\|[{]};:\'\",<.>/?';
//"-", "_" 사용가능하게 수
var etcValue = ' ~`!@#$%%^&*()=+\|[{]};:\'\",<.>/?';
var dateType = 1;	// 1: yyyymmdd, 2: ddmmyyyy

var len_4 = 4;
var len_10 = 10;
var len_13 = 13;
var len_15 = 15;
var len_20 = 20;
var len_30 = 30;
var len_40 = 40;
var len_50 = 50;
var len_100 = 100;
var len_200 = 200;
var len_400 = 400;
var len_500 = 500;
var len_2000 = 2000;
var len_1500 = 1500;
var len_1000 = 1000;
var len_4000 = 4000;

function checkLen(obj, len) {
	var str = obj.value;

	if (bytes(str) > len) {
		alert("최대 입력 글자 제한 수를 초과하였습니다");
		obj.value = cut(str, len);
		obj.focus();
	}  
}

function bytes(str) {
	var l = 0;
	
	for(var i = 0; i < str.length; i++) {
		l += (str.charCodeAt(i) > 128) ? 2 : 1;
	}

	return l;
}

function cut(str, len) {
	var l = 0;

	for(var i = 0; i < str.length; i++) {
		l += (str.charCodeAt(i) > 128) ? 2 : 1;

		if (l > len) {
			return str.substring(0, i);
		}
	}

	return str;
}

// ?? ?????? ???? ?????????? ????
function isUpper(value) {
	var i;

	for(i = 0; i < upperValue.length; i++) {
		if(value == upperValue.charAt(i)) {
			return true;
		}
	}

	return false;
}

// ?? ?????? ???? ?????????? ????
function isLower(value) {
	var i;

	for(i = 0; i < lowerValue.length; i++) {
		if(value == lowerValue.charAt(i)) {
			return true;
		}
	}

	return false;
}

// ?? ?????? ???????? ????
function isInt(value) {
	var j;

	for(j = 0; j < intValue.length; j++) {
		if(value == intValue.charAt(j)) {
			return true;
		}
	}

	return false;
}

// 특수 문자 check
function isEtc(value) {
	var j;

	for(j = 0; j < etcValue.length; j++) {
		if(value == etcValue.charAt(j)) {
			return true;
		}
	}

	return false;
}

function getBytes(text) {
    var length = 0;
    var ch;
    
    for (var i = 0; i < text.length; i++) {
        ch = escape(text.charAt(i));
       
        if ( ch.length == 1 ) {
            length++;
        }else if (ch.indexOf("%u") != -1) {
            length += 2;
        }else if (ch.indexOf("%") != -1) {
            length += ch.length/3;
        }

    }
    return length;
}

// 기존 getBytes 함수인데 성능이 무지 후졌다. 그래서 사장시킨다.
/*
function getBytes(str) {
	var i, ch, bytes;
	var app, isNe = 0;

	if(str == '') {
		return 0;
	}

	app = navigator.appName;

	if(app == 'Netscape') {
		isNe = 1;
	}

	for(i = 0, bytes = 0; i < str.length; i++) {
		ch = str.charAt(i);

		if(isInt(ch)) {
			bytes++;
		} else if(isLower(ch)) {
			bytes++;
		} else if(isUpper(ch)) {
			bytes++;
		} else if(isEtc(ch)) {
			bytes++;
		} else {
			bytes += 2;

			if(isNe) {
				i++;
			}
		}
	}

	return bytes;
}
*/

// ?????? ?????? ???? ???? ???? ????
function ltrim(para) {
	while(para.substring(0, 1) == ' ') {
		para = para.substring(1, para.length);
	}

	return para;
}

// ?????? ?????? ???? ???? ???? ????
function rtrim(para) {
	while(para.substring(para.length-1, 1) == ' ') {
		para = para.substring(0, para.length-1);
	}

	return para;
}

// ?????? ???????? ???? ???? ???? ????
function trim(para) {
	return rtrim(ltrim(para));
}

function checkBytes(obj, len){
	var text = obj.value;
	var code = "";
	var bytes = 0;
	var BOOLEAN	= false;

	if(text) {
		for(var i = 0; i < text.length; i++) {

			code = text.charCodeAt(i);

			if(32 < code && code < 128) {
				bytes ++;
			} else {
				bytes += 2;
			}

			if(bytes > len) {
				BOOLEAN = true;
				break;
			}
		}
	}

	return BOOLEAN;
}

function isFloat(value) {
	var count = 0;
	var ch;

	for(i = 0; i < value.length; i++) {
		ch = value.charAt(i);

		if(isNaN(ch)) {
			if(ch == ".") {
				count++;
			} else {
				return false;
			}
		}
	}

	if(count > 1) {
		return false;
	} else {
		return true;
	}

	return result;
}

// 숫자로 구성된 문자열인지 채크
function isNumber(value) {
	var result = true;

	for(j = 0; result && (j < value.length); j++) {

		if((value.substring(j, j+1) < "0") || (value.substring(j, j+1) > "9")) {
			result = false;
		}
	}

	return result;
}

// 전화번호로 구성된 문자열인지 채크
function isTelNo(value) {
    var result = true;
    
    for(j = 0; result && (j < value.length); j++) {
    
        if((value.substring(j, j+1) < "0") || (value.substring(j, j+1) > "9")) {
            
            if((value.substring(j, j+1) != "-")){
                result = false;
            }
        }
    }
    
    return result;
}

function monthArray(m0, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11) {
	this[0] = m0; this[1] = m1; this[2] = m2; this[3] = m3;
	this[4] = m4; this[5] = m5; this[6] = m6; this[7] = m7;
	this[8] = m8; this[9] = m9; this[10] = m10; this[11] = m11;
}

function isDay(yyyy, mm, value) {
	var result = false;
	var monthDay = new monthArray(31,28,31,30,31,30,31,31,30,31,30,31);

	var im = eval(mm) - 1;

	if (value.length != 2) {
		return false;
	}

	if (!isNumber(value)) {
		return false;
	}

	if (((yyyy % 4 == 0) && (yyyy % 100 != 0)) || (yyyy % 400 == 0)) {
		monthDay[1] = 29;
	}

	var dd = eval(value);

	if ((0 < dd) && (dd <= monthDay[im])) {
		result = true;
	}

	return result;
}

function isMonth(value) {
	return((value.length > 0) && (isNumber(value)) && (0 < eval(value)) && (eval(value) < 13));
}

function isYear(value) {
	return((value.length == 4) && (isNumber(value)) && (value != "0000"));
}

function isDate(value) {
	var year, month, year;

	if(dateType == 1) {
		//Korea Version 2004.1.16
		year  = value.substring(0, 4);
		month = value.substring(4, 6);
		day   = value.substring(6, 8);

	} else if(dateType == 2) {
		//Malay Version 2004.1.16
		day   = value.substring(0, 2);
		month = value.substring(2, 4);
		year  = value.substring(4, 8);
	}

	return(isYear(year) && isMonth(month) && isDay(year, month, day));
}

function isHour(value) {
	if(!isNumber(value)) {
		return false;
	}

	if(value > 23 || value < 0) {
		return false;
	}

	if(getBytes(value) != 2) {
		return false;
	}

	return true;
}

function isMinute(value) {
	if(!isNumber(value)) {
		return false;
	}

	if(value > 59 || value < 0) {
		return false;
	}

	if(getBytes(value) != 2) {
		return false;
	}

	return true;
}

function isSecond(value) {
	if(!isNumber(value)) {
		return false;
	}

	if(value > 59 || value < 0) {
		return false;
	}

	if(getBytes(value) != 2) {
		return false;
	}

	return true;
}

function checkDateFormat(strDate) {
	if(dateType == 1) {
		return strDate;
	} else if(dateType == 2) {
		return strDate.substring(4, 8) + strDate.substring(2, 4) + strDate.substring(0, 2);	    
	}
}

function checkFromToDate(sDate, eDate) {

	var SDATE = checkDateFormat(sDate.value);
	var EDATE = checkDateFormat(eDate.value);

	if(SDATE <= EDATE) {
		return false;
	} else {
		return true;
	}
}

function checkNumber() {
	var key = String.fromCharCode(event.keyCode);
	var re = new RegExp('[0-9]');

	if(!re.test(key)) {
		event.returnValue = false;
	}
}

function checkCode(filter) {

	if(filter){
		var key = String.fromCharCode(event.keyCode);
		var re = new RegExp(filter);

		if(!re.test(key)) {
			event.returnValue = false;
		}
	}
}

function checkFilter(filter) {

	if(filter){
		var key = String.fromCharCode(event.keyCode);
		var re = new RegExp(filter);

		if(!re.test(key)) {
			event.returnValue = false;
		}
	}
}

function checkEnter(e) {
	if(e.keyCode == 13) {
		event.returnValue = false;
	}
}

// ?????? "-" ?????????????? Check (????????, ????????????) (??????)
function isDigitOrBar( str ) {
	for(var i=0; i < str.length; i++) {
		var ch= str.charAt(i);
		if((ch < "0" || ch > "9") && ch!="-") {
			return false;
		}
	}
	return true;
}

function isChecked(num, checkValue) {
	var retVal = false;

	if (num == 1) {
		if (checkValue.checked) {
			retVal = true;
		}

	} else {

		for(i = 0; i < checkValue.length; i++) {
			if(checkValue[i].checked) {
				retVal = true;
			}
		}
	}

	return retVal;
}

function sendSms(count, frm){
	if(count == 0) {
		return;
	}

	if(count == 1) {
		if(!frm.pCheck.checked) {
			alert("SMS를 발송할 대상을 선택하세요.");
			//alert("Please select 'Target Group' to send Short Message.");
			return;
		}
	}

	var checked = false;

	if(count > 1) {
		for(var i = 0; i < frm.pCheck.length; i++) {
			if(frm.pCheck[i].checked) {
				checked = true;
			}
		}

		if(!checked) {
			alert("SMS를 발송할 대상을 선택하세요.");
			//alert("Please select 'Target Group' to send Short Message.");
			return;
		}
	}

	var memoSendWindow;

	memoSendWindow = window.open("","Msms","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,copyhistory=no,width=660,height=490");
	memoSendWindow.opener = self;
	memoSendWindow.focus();

	frm.target = "Msms";
	frm.action = '/common/back/sms/smsSendForm.jsp';
	frm.submit();
	return;
}

var selectMode = 0;

function selectCheckBox(num, checkValue) {
	if(num == 0) {
		return;
	}

	var value;

	if(selectMode == false) {
		value = true;
		selectMode = 1;
	} else {
		value = false;
		selectMode = 0;
	}

	if(num == 1) {
		checkValue.checked = value;
	}

	if(num > 1) {
		for(i = 0; i < checkValue.length; i++ ) {
		    if(!checkValue[i].disabled){
			    checkValue[i].checked = value;
		    }
		}
	}

	return;
}

function sendMemo(count, frm){
	if(count == 0) {
		return;
	}

	if(count == 1) {
		if(!frm.pCheck.checked) {
			alert(JS_MSG_MEMBER_122);
			return;
		}
	}

	var checked = false;

	if(count > 1) {
		for(var i = 0; i < frm.pCheck.length; i++) {
			if(frm.pCheck[i].checked) {
				checked = true;
			}
		}

		if(!checked) {
			alert(JS_MSG_MEMBER_122);
			return;
		}
	}

	var memoSendWindow;

	//memoSendWindow = window.open("","Mmemo","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no,width=400,height=350");
	memoSendWindow = window.open("","Mmemo","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no,width=390,height=430");
	memoSendWindow.opener = self;
	memoSendWindow.focus();

	frm.target = "Mmemo";
	frm.action = '/common/back/memo/comMemoSendFrm.jsp';
	frm.submit();
	return;
}

function sendMail(count, frm){
	if(count == 0) {
		return;
	}

	if(count == 1) {
		if(!frm.pCheck.checked) {
			alert(JS_MSG_MEMBER_104);
			return;
		}
	}

	var checked = false;

	if(count > 1) {
		for(var i = 0; i < frm.pCheck.length; i++) {
			if(frm.pCheck[i].checked) {
				checked = true;
			}
		}

		if(!checked) {
			alert(JS_MSG_MEMBER_104);
			return;
		}
	}

	var mailSendWindow;

	mailSendWindow = window.open("","Mmail","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no,width=650,height=600");
	mailSendWindow.opener = self;
	mailSendWindow.focus();

	frm.target = "Mmail";
	frm.action = '/common/back/mail/comMailSendFrm2nd.jsp';
	frm.submit();
	return;
}

function sendMail2nd(count, frm){
	if(count == 0) {
		return;
	}

	if(count == 1) {
		if(!frm.pCheck.checked) {
			alert(JS_MSG_MEMBER_104);
			return;
		}
	}

	var checked = false;

	if(count > 1) {
		for(var i = 0; i < frm.pCheck.length; i++) {
			if(frm.pCheck[i].checked) {
				checked = true;
			}
		}

		if(!checked) {
			alert(JS_MSG_MEMBER_104);
			return;
		}
	}

	var mailSendWindow;

	mailSendWindow = window.open("","Mmail","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no,width=650,height=600");
	mailSendWindow.opener = self;
	mailSendWindow.focus();

	frm.target = "Mmail";
	frm.action = '/common/back/mail/comMailSendFrm.jsp';
	frm.submit();
	return;
}

function sendMailOne(frm){

	var mailSendWindow;

	mailSendWindow = window.open("","Mmail","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no,width=650,height=600");
	mailSendWindow.opener = self;
	mailSendWindow.focus();

	frm.target = "Mmail";
	frm.action = '/common/back/mail/comMailSendFrm.jsp';
	frm.submit();
	return;
}

function fn_order_asc(orderKey, orderValue, frm, url) {
	frm.pOrderKey.value = orderKey;
	frm.pOrderValue.value = orderValue;
	frm.pOrderMethod.value = "ASC";

	frm.action = url;
	frm.submit();
}

function fn_order_desc(orderKey, orderValue, frm, url) {
	frm.pOrderKey.value = orderKey;
	frm.pOrderValue.value = orderValue;
	frm.pOrderMethod.value = "DESC";

	frm.target = "_self";
	frm.action = url;
	frm.submit();
}

function fn_send_mail(count, frm) {
	if(count == 0) {
		return;
	}

	if(count == 1) {
		if(!frm.pCheck.checked) {
			alert(JS_MSG_MEMBER_104);
			return;
		}
	}

	var checked = false;

	if(count > 1) {
		for(var i = 0; i < frm.pCheck.length; i++) {
			if(frm.pCheck[i].checked) {
				checked = true;
			}
		}

		if(!checked) {
			alert(JS_MSG_MEMBER_104);
			return;
		}
	}

	var mailSendWindow;

	mailSendWindow = window.open("","Mmail","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no,width=650,height=600");
	mailSendWindow.opener = self;
	mailSendWindow.focus();

	frm.target = "Mmail";
	frm.action = '/common/back/mail/comMailSendCompanyFrm.jsp';
	frm.submit();
	return;
}

//???????????? ???? ????(INPUT: object)
function addComma(theObj)
{
    var data = theObj.value;
	var len  = data.length;
	if(len>3) {
	  var rest = len%3;
	  var commaCnt = (len - rest)/3;
	  if(rest == 0) {
         temp = data.substr(0,3);
		 commaCnt -- ;
		 rest = 3 ;
	  } else {
        temp = data.substr(0,rest);
	  }

      for(i=0;i<commaCnt;i++) {
        temp = temp + "," + data.substr(rest,3);
		rest+=3;
	  }
	  theObj.value = temp;
	}
}

//?????????? ???? ????????(INPUT: object)
function delComma(theObj) {
    var data = theObj.value;
    //alert("***"+data);
    var len  = data.length;
    var temp = "";
	for ( i=0;i<len;i++) {
      if( data.substr(i,1) != ",") {
          temp = temp + data.substr(i,1);
	   }
	}
	theObj.value = temp;
}

var isSelected = 0;

function fn_select_check_box(num, checkValue) {
	if(num == 0) {
		return;
	}

	var value;

	if(isSelected == false) {
		value = true;
		isSelected = 1;
	} else {
		value = false;
		isSelected = 0;
	}

	if(num == 1) {
		if(!checkValue.disabled) {
			checkValue.checked = value;
		}
	}

	if(num > 1) {
		for(i = 0; i < checkValue.length; i++ ) {
			if(!checkValue[i].disabled) {
				checkValue[i].checked = value;
			}
		}
	}

	return;
}


function createObject(obj) {
	var str1 = obj
	document.writeln(str1);
}

/* ===================================================================
	Function : moneyFormTwo(obj)
	Return 	 :
	Usage 	 : form을 넘겨받아 소수점 처리 후 Setting한다.
=================================================================== */
function moneyFormTwo(obj){
  var nums = obj.value;
  var indexInt = nums.indexOf('.');
	var nLength = nums.length;
	var num  = "";
	var jjum = "";
	if(indexInt <= 0) {
		 num  = nums;
		 jjum = "";
	}else {
		num  = nums.substring(0, indexInt);
		jjum = nums.substring(indexInt, nLength);
	}
	if(num.length >= 4){
		// "$" and "," 입력 제거
		re = /^\$|,/g;
		num = num.replace(re, "");

		fl = "";
		if(isNaN(num)){
			alert("문자는 사용할 수 없습니다.");
			obj.value = "";
			return 0;
		}
		if(num==0) return num;

		if(num<0){
			num = num * (-1);
			fl = "-";
		}else{
			num = num * 1; //처음 입력값이 0부터 시작할때 이것을 제거한다.
		}

		num = new String(num);
		temp = "";
		co = 3;
		num_len = num.length;
		while(num_len>0){
			num_len = num_len-co;
			if(num_len < 0){
				co = num_len + co;
				num_len = 0;
			}
			temp = "," + num.substr(num_len, co) + temp;
		}
		if(indexInt <= 0) {
			obj.value =  fl+temp.substr(1)+jjum;
		}else{
			if(indexInt > 4) {
				obj.value =  fl+temp.substr(1)
				obj.focus();
				obj.value = obj.value+jjum
			}else{
				obj.value =  fl+temp.substr(1)+jjum;
			}
		}
 	}
}
/* ===================================================================
	Function : onlyNumberInput()
	Return 	 :
	Usage 	 : 숫자만 입력 가능 (onKeyDown 이벤트)
=================================================================== */
function onlyNumberInput() {
	var code = window.event.keyCode;
	if ((code > 32 && code < 48) || (code > 57 && code < 65) || (code > 90 && code < 97) || (code > 34 && code < 41) || (code > 47 && code < 58) || (code > 95 && code < 106) || code == 8 || code == 9 || code == 13 || code == 46){
		window.event.returnValue = true;
		return;
	}
	window.event.returnValue = false;
}

/* ===================================================================
	Function : onlyNumberInput2()
	Return 	 :
	Usage 	 : 0-9, 소수점, 백스페이스, delete키 만 입력 가능 
=================================================================== */
function onlyNumberInput2(aaa) {

	var code = window.event.keyCode;
	
	if ((code >= 48 && code <= 57) || code == 190 || code == 8 || code == 46) {
		window.event.returnValue = true;
		return;
	}

	window.event.returnValue = false;
	
}
/* ===================================================================
	Function : commaCut(money)
	Return 	 :
	Usage 	 : 입력된 문자열의 ','를 없앤 문자열을 리턴한다.
=================================================================== */
function commaCut(money){
	if(money == '') return '';
	return money.split(",").join("");
}

/* ============================================================================
	Function : getToday('')
	Return   : 오늘날짜
	Usage    : 현재 날짜(20030101 or 2003/01/01)를 리턴
	사용법   : getToday('')  ==> 20030101
			   getToday('/') ==> 2003/01/01
============================================================================ */
function getToday(gubun) 
{
	today = new Date();

	var year  = today.getFullYear();
	var month = today.getMonth()+1;
	var day   = today.getDate();

	if(month < 10)
		month = "0" + month;
	if(day < 10)
		day = "0" + day;

	return year+gubun+month+gubun+day;
}

/* ============================================================================
	Function : fOpenBanks('')
	Return   : 은행코드 
	Usage    : 은행코드.명 
	사용법   : 은행코드 팝업 호출 
============================================================================ */
function fOpenBanks(){
	window.open("/lms/back/common/popup/comBankPopupList.jsp","banks","height=400,width=450,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

/* ============================================================================
	Function : fOpenBanks('')
	Return   : 강사찾기 팝업  
	Usage    : 강사찾기 팝업  
	사용법   : 강사찾기 팝업  
============================================================================ */
function fOpenLecturer(){
	window.open("/lms/back/common/popup/comLecturerPopupList.jsp","banks","height=590,width=650,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function fOpenLecturer1(gubun){
	window.open("/lms/back/common/popup/comLecturerPopupList.jsp?gubun=" + gubun,"banks","height=590,width=650,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function fOpenLecturer2(gubun1, gubun2){
	window.open("/lms/back/common/popup/comLecturerPopupList.jsp?gubun1=" + gubun1 + "&gubun2=" + gubun2,"banks","height=590,width=650,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

/* ============================================================================
	Function : fOpenLecturer('')
	Return   : 직급코드 찾기  팝업  
	Usage    : 직급코드 팝업  
	사용법   : 직급코드 팝업  
============================================================================ */
function fOpenCodeManagerChk(){
	window.open("/lms/back/common/popup/comCodeManagerPopupList.jsp","Code","height=250,width=450,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

/*
 *	숫자만 입력하는 함수 - style='ime-mode:disabled' onkeypress="chkNumber(this)"
 */
function chkNumber(obj) { 
	if((event.keyCode<48) || (event.keyCode>57)) { 
		event.returnValue=false; 
	}
}

/*
 *	입력 disabled,readOnly 색깔 표시
 */
function fDisabledInit(obj){

	if(obj){
		for(var i=0;i<obj.elements.length;i++){
			if(obj.elements[i].disabled){
				obj.elements[i].style.backgroundColor = "#F0F5FB";
			}
			if(obj.elements[i].readOnly){
				obj.elements[i].style.backgroundColor = "#F0F5FB";
			}
		}
	}
	
}

/**
 *	숫자입력체크
 */
function jsOnlyNumberKey() {
	if ( event != null) {
			alert(event);
		if ( event.keyCode < 48 || event.keyCode > 57 ) {
			event.returnValue = false;
		}
	}
}

function delComma(fValue) {
	var tmp = fValue.split(",")
	var rtnTmp="";
	for(i=0;i<tmp.length;i++) {
		rtnTmp+=tmp[i];
	}
	return rtnTmp;
}
	
function addComma(aString)		{				
	var reverseMaskedNumber = "", maskedNumber = "";
	var integerCount = 0, maskCount = 0, isPoint = 0;	
	var integerIndex ;												
	var tmpValue, i;

	//	aString = Number(aString);							

	integerIndex = aString.length;							

	for (i = aString.length - 1 ; i >= 0 ; i--)	{
		reverseMaskedNumber += aString.charAt(i);			
		if (aString.charAt(i) == ".")			{
			integerIndex = i - 1
			isPoint = 1;			
			break;
		}
	}

	if (isPoint == 0)	{
		reverseMaskedNumber = "";
		integerIndex -= 1;			
	}

	for ( i = integerIndex ; i >= 0 ; i--)		{
		integerCount++;							
		reverseMaskedNumber += aString.charAt(i);
		if (integerCount % 3 == 0 && i != 0 && aString.charAt(i-1) != "-")		{
			reverseMaskedNumber += ",";						
			maskCount++;											
		}
	}

	for ( i = maskCount + aString.length ; i >= 0 ; i--)			{
		maskedNumber += reverseMaskedNumber.charAt(i);	
	}

	return maskedNumber;											
}

// 숫자와 소수점만 입력 가능
/* ===================================================================
	Function : onlyNumDecimalInput(obj, number, maxDecimal)
	Return 	 :
	Usage 	 : 숫자만 입력 가능 (onKeyDown 이벤트)
=================================================================== */
	function onlyNumDecimalInput(){
		var code = window.event.keyCode;

 		if ((code >= 48 && code <= 57) || (code >= 96 && code <= 105) || code == 110 || code == 190 || code == 8 || code == 9 || code == 13 || code == 46){
			window.event.returnValue = true;
	 		return;
 		}
 		window.event.returnValue = false;
	}
	
//Email 유효성 검사
function checkEmail(email) { 
   // regular expression 지원 여부 점검 
   var supported = 0;    
   if (window.RegExp) { 
      var tempStr = "a"; 
      var tempReg = new RegExp(tempStr); 
      if (tempReg.test(tempStr)) supported = 1 
   } 
   if (!supported) 
      return (email.indexOf(".") > 2) && (email.indexOf("@") > 0); 
   var r1 = new RegExp("(@.*@)|(\\.\\.)|(@\\.)|(^\\.)"); 
   var r2 = new RegExp("^.+\\@(\\[?)[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3}|[0-9]{1,3})(\\]?)$"); 
   return (!r1.test(email) && r2.test(email)); 
}

function GetTextByte(text){
    var length = 0;
    var ch;
    
    for (var i = 0; i < text.length; i++) {
        ch = escape(text.charAt(i));
       
        if(ch != '%20') {
            if ( ch.length == 1 ) {
                length++;
            }else if (ch.indexOf("%u") != -1) {
                length += 2;
            }else if (ch.indexOf("%") != -1) {
                length += ch.length/3;
            }
        }
    }
    return length;
}

//교육대상자 자동계산 스크립트 - 직급코드, 근무기관 코드 받아서 대상자 자동 계산
function getTargetDiv(frmName, pRankDiv, pLclsCd, pTargetDiv) {
	var frm = eval("document."+frmName);
	var lcls = eval("document."+frmName+"."+pLclsCd+".value");
	var rank_div = eval("document."+frmName+"."+pRankDiv+".value");
	var target_div = eval("document."+frmName+"."+pTargetDiv+".value");
//alert("lcls :" +lcls+", rank_div :" +rank_div+", target_div :" +target_div);
	if(rank_div != "") {
		if(rank_div == "10" || rank_div == "20" || rank_div == "30" || rank_div == "40" || rank_div == "50" || rank_div == "60" || rank_div == "70" || rank_div == "80" || rank_div == "90" || rank_div == "900" || rank_div == "170" || rank_div == "180") {
			eval("document."+frmName+"."+pTargetDiv+".value='06'");
		} else if(rank_div == "130" || rank_div == "140" || rank_div == "150" || rank_div == "160") {
			eval("document."+frmName+"."+pTargetDiv+".value='05'");
		} else if(rank_div == "105" || rank_div == "115") {
			eval("document."+frmName+"."+pTargetDiv+".value='01'");
		} else {
			if(lcls != "") {
				if(lcls == "11") {
					eval("document."+frmName+"."+pTargetDiv+".value='01'");
				} else if(lcls == "12") {
					eval("document."+frmName+"."+pTargetDiv+".value='02'");
				} else if(lcls == "13" || lcls == "14") {
					eval("document."+frmName+"."+pTargetDiv+".value='03'");
				} else if(lcls == "16" || lcls == "17") {
					eval("document."+frmName+"."+pTargetDiv+".value='04'");
				} else {
					eval("document."+frmName+"."+pTargetDiv+".value='04'");
				}
			} else {
				eval("document."+frmName+"."+pTargetDiv+".value='04'");
			}
		}
	} else {
		eval("document."+frmName+"."+pTargetDiv+".value='06'");
	}
alert("lcls :" +lcls+", rank_div :" +rank_div+", target_div :" +target_div);
}



//Element를 생성후 반환
 function createElement(source) {
	var element = document.createElement(source);
	return element;
 }

 //source의 이름이 name인 Element를 생성후 반환
 function createElement1(source, name) {
	var element = document.createElement(source);
	element.name = name;
	element.id = name;
	return element;
 }
 //source의 Element를 생성하여 text, value을 설정후 target에 붙임
 function createOptionElement(source, text, value,target) {
	  var oOption = document.createElement(source);
	  oOption.text = text;
	  oOption.value = value;
	  target.add(oOption);
 }
 //source의 Element를 생성하여 text, value을 설정후 target에 붙임
 function createOptionElement1(source, text, value,target,tmp) {
	  var oOption = createElement(source);
	  oOption.text = text;
	  oOption.value = value;
	  if(tmp == value) {
		  oOption.selected = true;
	  }
	  target.add(oOption);
 }
 //TD 생성["TD",폭,높이]
 function createTDElement(obj,width,height,className) {
	  var TDElement = document.createElement(obj);
	  TDElement.width = width;
	  TDElement.height = height;
	  TDElement.className = className;
	  return TDElement;
 }
//INPUT["INPUT",TYPE[button, image, checkbox, file, hidden, password, radio, reset, submit, text ],NAME,SIZE,VALUE]
function createTEXTElement(obj, type, name, size,value,readOnly,className,maxlength) {
	var TEXTElement = document.createElement(obj);
	TEXTElement.type = type;
	TEXTElement.name = name;
	TEXTElement.id = name;
	TEXTElement.size = size;
	TEXTElement.value = value;
	TEXTElement.readOnly=readOnly;
	TEXTElement.className=className;
	if(readOnly == "true") {
		TEXTElement.style.backgroundColor = "#F0F5FB";
	}
	TEXTElement.maxLength = maxlength;
	return TEXTElement;
}
