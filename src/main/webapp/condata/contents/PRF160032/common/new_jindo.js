/******************************************************************************************************************************/
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


/********************************************/
// Cookies 초기화 셋팅 끝
/********************************************/
// - 현제 페이지 를 검사
var href = document.location.href; //파일 url 가져오기
var s = href.split("/"); // "/"으로 배열 만들기
s_name = s[s.length-1]; //화일명 및 확장명 가져오기

/*01_01_01.htm : length 12
차시, 절, 페이지*/

//s_chasi = s_name.substring(s_name.length-9,s_name.length-7); // 차시 번호
s_frame = s_name.substring(s_name.length-7,s_name.length-5); // 프레임 번호

//alert("차시:페이지     "+s_chasi+":"+s_frame);
//jindo = s_chasi+s_frame; //쿠키에 저장할 진도  //itostr() 함수는 jindo.js

complete_html(s_frame); //진도저장

/******************************************************************************************************************************/
