/******************************************************************/
/*  KPC ���ÿ� ��ũ��Ʈ                                                                               */
/*  �ۼ��� : ��ä��(imchaeun@hanmail.net)                                          */
/*  �ۼ��� : 2007�� 04�� 09��                                                                       */
/*  ��   �� : �����ӿ�                                                                                    */
/******************************************************************/

//���ÿ� ������ ����
var mode = true; //Server : true
//var mode = false; //HDD : false

//���� ���� ����
var Chasi_Num = 4; //�ش� ���� ������ �Է�

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
// Cookies �ʱ�ȭ ���� ��
/********************************************/

// ���� ����
/********************************************/
function complete_html(pinf) {
	var href = document.location.href; //���� url ��������
	var s = href.split("/"); // "/"���� �迭 �����
	chasi = s[s.length-2]; //������(����) ��������
	var sinf=chasi+pinf+"01"+pinf;
	setCookie("sinf", sinf, null, "/");
	if(mode==true){
		top.navi.on_exit();
	}else{
		//HDD�� ������ ��ܿ� ����Ǵ� �� Ȯ�ΰ���, ��: 01010101�� DB�� ����
		//top.document.title = sinf+"�� DB�� ����";
		top.status=sinf+"�� DB�� ����";
	}
}
/********************************************/

/********************************************/
// ���� ���÷� �̵�
/********************************************/
function next_chasi() {
	var now_chasi = Number(getCookie("sinf").substring(0,2));

	if(now_chasi >= Chasi_Num) {
		var ans = confirm("\n�����ϼ̽��ϴ�.\n�������� ������ ������ ������ �Դϴ�.\n������ �̵��Ͻðڽ��ϱ�?   \n");
		if (ans == true) {
			top.location = "../common/main.htm";
		}
	} else {
		var next_chasi = itostr(eval(now_chasi+1));
		alert("\n���� �ϼ̽��ϴ�. \n\n���� ���÷� �̵��մϴ�.   \n");
		callhtml(next_chasi);
	}
}
/********************************************/

/********************************************/
//�������� ���� �̵�
/********************************************/
function callhtml(i) {
        cinf = i + "010101";
        setCookie("sinf", cinf, null, "/");
        location.href ="../"+i+"/"+i+"_01.html";
}
/********************************************/

/********************************************/
//�������� ���� �̵�2
/********************************************/
function callhtml2(i) {
        cinf = i + "010101";
        setCookie("sinf", cinf, null, "/");
        location.href ="../"+i+"/"+i+"_01.html";
}
/********************************************/

/********************************************/
//���ڸ� ���ڷ� ġȯ
/********************************************/
function itostr(num) {
        if (num < 10) str = "0";
        else str = "";

        str = str + num;
        return str;
}
/********************************************/

/********************************************/
//�н� ���� ������ �̵�
/********************************************/
function callexit2() {
		ans = confirm("\n�н��� ����ġ�� �մϴ�.\n\n���� �����Ͻðڽ��ϱ�?\n\nȮ�� ��ư�� Ŭ���ϸ� �����մϴ�.\n");
		if (ans == true) {
		top.close();
	}
}
/********************************************/

/********************************************/
//������ �̵�
/********************************************/
function on_list() {
	parent.location = "main.htm";
}
/********************************************/

/********************************************/
//������ �̵�
/********************************************/
function on_list1() {
	parent.location = "../common/main.htm";
}
/********************************************/

/********************************************/
//�����н� �̾��ϱ�
/********************************************/
function last_study(){
	var sLen = getCookie("sinf"); //��⿡�� �������� ��������
	var g_chasi = sLen.substring(0,2);	//��Ű���� ���� ������ �ڸ���
	var g_page = sLen.substring(6,8);	//��Ű���� page ������ �ڸ���

	//setCookie("bookmark", 1, null, "/");
	if (g_page == "" || g_page == null)
	{
		location.href = "../01/01_01.html";
	}else{
		location.href = "../"+g_chasi+"/"+g_chasi+"_"+g_page+".html";
	}
	
}

/********************************************/
// LMS �޴�
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
			case 2 : alert("������ �����⿡�� �������� �ʴ� ����Դϴ�.   ");
				break;
			case 3 : alert("������ �����⿡�� �������� �ʴ� ����Դϴ�.   ");  
				break;
			case 4 : alert("�ڷ���� �����⿡�� �������� �ʴ� ����Դϴ�.   ");  
				break;
			case 5 : alert("������ �����⿡�� �������� �ʴ� ����Դϴ�.   ");  
				break;
			case 6 : alert("Q&A�� �����⿡�� �������� �ʴ� ����Դϴ�.   ");  
				break;
			case 7 : alert("����� �����⿡�� �������� �ʴ� ����Դϴ�.   "); 
				break;
			case 8 : alert("������ �����⿡�� �������� �ʴ� ����Դϴ�.   "); 
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
// �ǰ��Է��ϱ�
//------------------------------------------//
// ���ǰ� �Է�
function my_on_opinion(opinionno,contents) {
	if(mode==false){
		alert("[����] " + opinionno + "�� �ǰ��Է� : " + contents );
		//alert("�̼� ���� : �����ϱ�� LMS ����Դϴ�.");
	}else{
	  //  alert("[test] " + opinionno + "�� �ǰ��Է� : " + contents );
		parent.parent.navi.on_opinion2(opinionno,contents);
	}
}


// �ٸ���� �ǰ� ����
function my_on_opinionlst(opinionno){
	if(mode==false){
		alert("[����] " + opinionno + "�� �ǰߺ��� ");
		//alert("�̼� ���� : �ٸ���� �ǰߺ���� LMS ����Դϴ�.");
	}else{
	    //alert("[test] " + opinionno + "�� �ǰߺ��� ");
		parent.parent.navi.on_opinionlst2(opinionno);
	}
}
//------------------------------------------//

//���ȴٿ�ε�
function nh_down(){
	var href = document.location.href; //���� url ��������
	var s = href.split("/"); // "/"���� �迭 �����
	chasi = s[s.length-2]; //������(����) ��������
	window.open('../down/down.htm?URL='+chasi+'.zip',"_blank","status=no, scrollbars=no, resizable=no, width=500, height=200");
	//window.open("../down/"+chasi+".zip","_blank","status=yes, scrollbars=yes, resizable=yes, width=500, height=400"); 
}