/* �̹��� on/off */
function imgOver(obj){
	obj.src = obj.src.replace('.gif', '_on.gif');
}
function imgOut(obj, type){
	if(type != "on") {
		obj.src = obj.src.replace('_on.gif', '.gif');
	}
}

// png ���� ó��
function setPng24(obj) {
 obj.width=obj.height=1;
 obj.className=obj.className.replace(/\bpng24\b/i,'');
 obj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+ obj.src +"',sizingMethod='image');"
 obj.src='';
 return '';
}

//���̺� tr overȿ��
$(document).ready(function() {
	$('table tr:gt(0)').hover(function() { $(this).addClass('hover'); },
	 function() { $(this).removeClass('hover'); });
	$('table tr').slice(1, 3).addClass('');
});
//'table tr:gt(0) 0���� ū�͸� ���콺 ���� ȿ���� ��Ÿ��
