/******************************************************************************************************************************/
/********************************************/
// Cookies �ʱ�ȭ ���� ����
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
// Cookies �ʱ�ȭ ���� ��
/********************************************/
// - ���� ������ �� �˻�
var href = document.location.href; //���� url ��������
var s = href.split("/"); // "/"���� �迭 �����
s_name = s[s.length-1]; //ȭ�ϸ� �� Ȯ��� ��������

/*01_01_01.htm : length 12
����, ��, ������*/

//s_chasi = s_name.substring(s_name.length-9,s_name.length-7); // ���� ��ȣ
s_frame = s_name.substring(s_name.length-7,s_name.length-5); // ������ ��ȣ

//alert("����:������     "+s_chasi+":"+s_frame);
//jindo = s_chasi+s_frame; //��Ű�� ������ ����  //itostr() �Լ��� jindo.js

complete_html(s_frame); //��������

/******************************************************************************************************************************/
