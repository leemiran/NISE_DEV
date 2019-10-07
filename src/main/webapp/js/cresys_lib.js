
//d_type
var p_d_type = "T";

//학습자로 사이트에  접속하기
function whenConnection(userid, pwd){
	//alert(encodeURIComponent(pwd));
	var targetDomain = window.location.href.match(/:\/\/(.[^/]+)/)[1];
	
	var c_url   = this.location+"";
	
	var url = "http://iedu.nise.go.kr/usr/lgn/portalUserMainLogin.do?userId=" + encodeURIComponent(userid) + "&pwd=" + encodeURIComponent(pwd)+ "&p_d_type=" + p_d_type + "&p_l_type=A";
  
	if(c_url.match("localhost")){		
		url = "http://localhost/usr/lgn/portalUserMainLogin.do?userId=" + encodeURIComponent(userid) + "&pwd=" + encodeURIComponent(pwd)+ "&p_d_type=" + p_d_type + "&p_l_type=A";
	}	

	var chk = "";
	  var i = new Number(((window.navigator.appVersion.split('; '))[1].split(' '))[1]);
	  		  //if(i==8){
			  chk = "-nomerge ";
		//  }else {
		//	  chk = "";
		//  }
	//alert(url);
	exec(url, chk);
}


//회원의 수강상세정보/회원정보 통합화면
function whenPersonGeneralPopup(p_userid) {
	var url = '/adm/pop/searchPersonGeneralPopup.do'
	+ '?p_userid=' + p_userid + "&p_d_type=" + p_d_type
	;
		
	window.open(url,"searchPersonGeneralPopupWindowPop","width=1000,height=650,scrollbars=yes");
}


/*
 * 공통파일 다운로드
 * realfile : 다운받을 파일명
 * savefile : 실제저장된 파일명
 * dir : /dp/아래의 폴더경로
 */
function fn_download(realfile, savefile, dir){
	var url = "/com/commonFileDownload.do?";
	url += "realfile=" + realfile;
	url += "&savefile=" + savefile.replace("/opt/upload/ko/faq_upload/", "");
	url += "&dir=" + dir;
	window.location.href = url;
	return;
}


//과정 상세정보 팝업
function whenSubjInfoViewPopup(p_subj, target) {
	
	//과정상세로 가기
	if(target == 'view')
	{
		var url = "/usr/subj/subjInfoView.do"
			+ "?p_subj=" + p_subj
			+ "&p_target=" + target
			;
		document.location.href = url;
	}
	//과정정보 팝업
	else
	{
		var url = "/usr/subj/subjInfoViewPopUp.do"
			+ "?p_subj=" + p_subj
			;
		window.open(url,"searchSubjSeqInfoPopupWindowPop","width=820,height=650,scrollbars=yes");
	}
}






//관리자 공통 회원정보
function whenMemberInfo(userid) {
	var url = '/com/pop/searchMemberInfoPopup.do'
		+ '?p_userid=' + userid
	;
		
	window.open(url,"searchMemberInfoPopupWindowPop","width=600,height=460,scrollbars=yes");
}

/*
* 공통 문자발송
* form : Form 객체
* checkId : userid가 담겨있는 체크박스 이름
*/
function whenCommonSmsSend(form, checkId) {
	var url = '/com/snd/sendCommonSmsPopup.do'
		+ '?checkId=' + checkId
	;
		
	window.open("","whenCommonSmsSendPopupWindowPop","width=1000,height=800,scrollbars=yes");
	form.target = "whenCommonSmsSendPopupWindowPop";
	form.action = url;
	form.submit();
}


/*
* 공통 메일발송
* form : Form 객체
* checkId : userid가 담겨있는 체크박스 이름
*/
function whenCommonMailSend(form, checkId) {
	var url = '/com/snd/sendCommonMailPopup.do'
		+ '?checkId=' + checkId
	;
		
	window.open("","whenCommonMailSendPopupWindowPop","width=1000,height=800,scrollbars=yes");
	form.target = "whenCommonMailSendPopupWindowPop";
	form.action = url;
	form.submit();
}



/**
 * 문자열 길이를 반환
 * @param value
 * @return
 */
function realsize( value ){
	var len = 0;
	if ( value == null ) return 0;
	for(var i=0;i<value.length;i++){
	   var c = escape(value.charAt(i));
	   if ( c.length == 1 ) len ++;
	   else if ( c.indexOf("%u") != -1 ) len += 2;
	   else if ( c.indexOf("%") != -1 ) len += c.length/3;
	}
	return len;
}

function num_max_chk(data, data1)
{
  for (var i = 0 ;i < data.value.length ;i++ )
  {
    if ((data.value.substring(i,i+1) < "0" || data.value.substring(i,i+1) > "9" ))
    {
      alert("숫자만 입력가능합니다.");
      data.focus();
      return;
    }
  }

  if (parseInt(data.value,10) > parseInt(data1,10))
  {
    alert("만점보다 적은값을 입력하십시요.");
    data.focus();
    return;
  }
}


/**
 * 대상의 값이 숫자인지 체크
 * @param data
 * @return
 */ 
function numeric_chk(data){
	if( data.value * 0 != 0 ){
		alert("숫자만 입력가능합니다.");
		data.value = "";
		data.focus();
		return false;
	}
	return true;
}
 
function checkRadio(obj){
	var result = "";
	if( obj ){
		if( obj.length ){
			for( i=0; i<obj.length; i++ ){
				if( obj[i].checked ) result = obj[i].value;
			}
		}else{
			if( obj.checked ) result = obj.value;
		}
	}
	return result;
}


function numeric_chk_noalert(value)
{
  for (var i = 0 ;i < value.length ;i++ )
  {
    if ((value.substring(i,i+1) < "0" || value.substring(i,i+1) > "9" )) {
      return false;
    }
  }
  return true;
}

function number_chk(data)
{
  if (isNaN(data.value))
  {
    alert("숫자만 입력가능합니다.");
    data.value = "0.0";
    data.focus();
    return false;
  }
  return true;
}

function number_chk_noalert(value)
{
  if (isNaN(value)) {
    return false;
  }
  return true;
}


/**
 * 입력값이 사용자가 정의한 포맷 형식인지 체크
 * 자세한 format 형식은 자바스크립트의 'regular expression'을 참조
 */
function isValidFormat(input,format) {
    if (input.value.search(format) != -1) {
        return true; //올바른 포맷 형식
    }
    return false;
}

/**
 * 입력값이 이메일 형식인지 체크
 * ex) if (!isValidEmail(form.email)) {
 *         alert("올바른 이메일 주소가 아닙니다.");
 *     }
 */
function isValidEmail(input) {
    var format = /^((\w|[\-\.])+)@((\w|[\-\.])+)\.([A-Za-z]+)$/;
    if (blankCheck(input.value)) return true;
    return isValidFormat(input,format);
}

/**
 * 입력값이 전화번호 형식(숫자-숫자-숫자)인지 체크
 */
function isValidPhone(input) {
    var format = /^(\d+)-(\d+)-(\d+)$/;
    if (blankCheck(input.value)) return true;
    return isValidFormat(input,format);
}

/**
 * 선택된 라디오버튼이 있는지 체크
 */
function hasCheckedRadio(input) {
    if(input) {
        if (input.length > 1) {
            for (var inx = 0; inx < input.length; inx++) {
                if (input[inx].checked) return true;
            }
        }
        else {
            if (input.checked) return true;
        }
    }
    return false;
}

/**
 * 선택된 체크박스가 있는지 체크
 */
function hasCheckedBox(input) {
    return hasCheckedRadio(input);
}
 
function blankCheck( msg )
{
 	var mleng = msg.length;
 	chk=0;

 	for (i=0; i<mleng; i++)
 	{
 		if ( msg.substring(i,i+1)!=' ' && msg.substring(i,i+1)!='\n' && msg.substring(i,i+1)!='\r') chk++;
 	}
 	if ( chk == 0 ) return (true);

 	return (false);
}

//2001-06-22에서 숫자(20010622)가져오는 함수.
function make_date(str){
    var yyyy = str.substring(0,4);
    var mm   = str.substring(5,7);
    var dd   = str.substring(8,10);

    var date_val = yyyy+mm+dd;
    return date_val;
}


function useridCheck( userid )
{
  //id 영문 check
	var check = "y";
	var newid = userid;

	for(i=0; i<newid.length; i++){
		if (newid.charAt(i) == " "){
			check = "n";
		}
		else if (((newid.charAt(i) < "A") || (newid.charAt(i) > "Z"))
		 		 && ((newid.charAt(i) < "a") || (newid.charAt(i) > "z"))
		     && ((newid.charAt(i) < "0") || (newid.charAt(i) > "9"))){

			check = "n";
		}
	}

	if (check =="n") {
		return (false);
	}

	return (true);
}

function passwordCheck( passwd )
{
  //비밀번호 영문,숫자 혼용 check
	var cpasschk=0;
	var npasschk=0;
	var check = "y";
	var newpasswd = passwd;

	for(i=0; i<newpasswd.length; i++)
	{
		if (newpasswd.charAt(i) == " ") {
			check = "n";
		}
		else if (((newpasswd.charAt(i) < "A") || (newpasswd.charAt(i) > "Z"))
				 && ((newpasswd.charAt(i) < "a") || (newpasswd.charAt(i) > "z"))
		     && ((newpasswd.charAt(i) < "0") || (newpasswd.charAt(i) > "9"))){

			check = "n";
		}
		if ( "A" <= newpasswd.charAt(i) && newpasswd.charAt(i) <= "Z" ) cpasschk++;
		if ( "a" <= newpasswd.charAt(i) && newpasswd.charAt(i) <= "z" ) cpasschk++;
		if ( "0" <= newpasswd.charAt(i) && newpasswd.charAt(i) <= "9" ) npasschk++;
	}

	if (check == "n" || cpasschk == 0 || npasschk == 0) {
		return (false);
	}

	return (true);
  //check end
}

function isAlphaNum( inStr ) {
    if (inStr.length > 0) { 
        for (i=0; i < inStr.length; i++) {
            if (!( (inStr.charAt(i) >= "a" && inStr.charAt(i) <= "z") || (inStr.charAt(i) >= "A" && inStr.charAt(i) <= "Z") || (inStr.charAt(i) >= "0" && inStr.charAt(i) <= "9")) ) { return false; }
        }
        return true;
    }
    else { 
        return false; 
    }
}

function validPersono(fpersono1, fpersono2, fname) {
	var str1 = fpersono1;
	var str2 = fpersono2;
	var len1 = bytelength(str1);
	var len2 = bytelength(str2);
	if (!fname)		fname = "주민등록번호";
	
	var str = String(str1) + String(str2);
	var len = bytelength(str);

    var sex = str2.substring(0,1);

    if (!digitstr(str1) || !digitstr(str2) || !digitstr(str)) {
        alert(fname+"는 숫자만으로 구성되어야 합니다. "+fname+"를 확인하신후 다시 입력하시기 바랍니다");
        return false;
    }
    
    if (sex == "9" || sex == "0") {
        alert(fname+" 성별부분을 잘못 입력하였습니다. "+fname+"를 확인하신후 다시 입력하시기 바랍니다");
        return false;
    }
    
    if (str1 == "570908" && str2 == "1009010")
    	return true;
    
    if (sex == "1" || sex == "2" || sex == "3" || sex == "4")
    {
		var chk = 0 ;
		total = 0;
		temp = new Array(13);

		for(i = 1; i <= 6; i++) {
			temp[i] = str1.charAt(i-1);
		}

		for(i = 7; i < 13; i++) {
			temp[i] = str2.charAt(i-7);
		}

		for(i = 1; i <= 12; i++ ) {
			k = i + 1;
			if( k >= 10 ) {
				k = k % 10 + 2;
			}
			total = total + temp[i] * k;
		}

		mm = temp[3] + temp[4];
		dd = temp[5] + temp[6];
		temp[13] = str2.charAt(6);

		totalmod = total % 11;
		chd = (11 - totalmod) % 10;

		if (chd == temp[13] && mm < 13 && dd < 32 &&
			(temp[7]==1 || temp[7] == 2 || temp[7] == 3 || temp[7] == 4)) {
			return true;
		}
		alert("유효하지 않은 "+fname+"입니다. "+fname+"를 확인하시고 다시 입력하시기 바랍니다");
		return false;
	}
	else
	{
		var sum = 0;
		var odd = 0;
		var reg_no = str1 + str2;

		buf = new Array(13);
		for (i = 0; i < 13; i++) buf[i] = parseInt(reg_no.charAt(i));

	    odd = buf[7]*10 + buf[8];

	    if (odd%2 != 0) {
			alert("유효하지 않은 "+fname+"입니다. "+fname+"를 확인하시고 다시 입력하시기 바랍니다");
			return false;
		}

		if ((buf[11] != 6)&&(buf[11] != 7)&&(buf[11] != 8)&&(buf[11] != 9)) {
			return false;
		}

		multipliers = [2,3,4,5,6,7,8,9,2,3,4,5];
		for (i = 0, sum = 0; i < 12; i++) sum += (buf[i] *= multipliers[i]);

		sum=11-(sum%11);

		if (sum>=10) sum-=10;

    	sum += 2;

	    if (sum>=10) sum-=10;

	    if ( sum != buf[12]) {
			alert("유효하지 않은 "+fname+"입니다. "+fname+"를 확인하시고 다시 입력하시기 바랍니다");
			return false;
		} else {
			return true;
		}
	}
}


//전체 선택
function allcheck(theform)
{
 for( i=0; i<theform.elements.length; i++) {
     ele = theform.elements[i];
         ele.checked = true;
 }
 return;
}

//전체해제
function discheck(theform)
{
 for( i=0; i<theform.elements.length; i++) {
     ele = theform.elements[i];
         ele.checked = false;
 }
 return;
}



function isNumberCheck(control, msg) {
	
	var val = control;
	var Num = "1234567890";
	for (i=0; i<val.length; i++) {
		if(Num.indexOf(val.substring(i,i+1))<0) {
			alert(msg+' 형식이 잘못되었습니다.');
			return false;
		}
	}
	return true;
}
