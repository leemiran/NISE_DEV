//���ù�ȣ �޾ƿ���
var hnURL2 = location.href, hnNum2, hnNum2_n;
hnURL2 = hnURL2.split('.html');
hnNum2 = hnURL2[0].substring(hnURL2[0].length-5, hnURL2[0].length-3);

if(Number(hnURL2[0].substring(hnURL2[0].length-5, hnURL2[0].length-4)) == 0){
	hnNum2_n = Number(hnURL2[0].substring(hnURL2[0].length-4, hnURL2[0].length-3));
}
else{
	hnNum2_n = Number(hnURL2[0].substring(hnURL2[0].length-5, hnURL2[0].length-3));
}

//��������ȣ �޾ƿ���
var hnURL = location.href, hnNum;
hnURL = hnURL.split('.html');

if(Number(hnURL[0].substring(hnURL[0].length-2, hnURL[0].length-1)) == 0){
	hnNum = Number(hnURL[0].substring(hnURL[0].length-1, hnURL[0].length-0));
}
else{
	hnNum = Number(hnURL[0].substring(hnURL[0].length-2, hnURL[0].length-0));
}
hnNumS=hnURL[0].substring(hnURL[0].length-2, hnURL[0].length-0);

//�н������� �ε�
if(Number(thisPageNum)==1){
	//loadswf("swf/"+hnNum2+'_'+hnNumS+".swf",1024,710,true,my_flashVars+'&n_pos='+hnNum+'&c_pos='+hnNum2+'&ckValue1='+getCookie('ckValue1'));
}else{
	//loadswf("swf/"+hnNum2+'_'+hnNumS+".swf",1024,710,true,my_flashVars+'&n_pos='+hnNum+'&c_pos='+hnNum2+'&ckValue1='+getCookie('ckValue1'));
}

loadswf("swf/default.swf",1024,710,true,my_flashVars);
//����üũ
