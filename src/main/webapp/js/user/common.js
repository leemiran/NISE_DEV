/* 이미지 on/off */
function imgOver(obj){
	obj.src = obj.src.replace('.gif', '_on.gif');
}
function imgOut(obj, type){
	if(type != "on") {
		obj.src = obj.src.replace('_on.gif', '.gif');
	}
}
// png 투명 처리
function setPng24(obj) {
 obj.width=obj.height=1;
 obj.className=obj.className.replace(/\bpng24\b/i,'');
 obj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+ obj.src +"',sizingMethod='image');"
 obj.src='';
 return '';
}

//테이블 tr over효과
$(document).ready(function() {
	$('table tr:gt(0)').hover(function() { $(this).addClass('hover'); },
	 function() { $(this).removeClass('hover'); });
	$('table tr').slice(1, 3).addClass('');
});
//'table tr:gt(0) 0보다 큰것만 마우스 오버 효과가 나타남

// footer 셀렉트효과
function viewLayer(result, idNum){
	if (result){
	document.getElementById(idNum).style.display='block';
	}else{
	document.getElementById(idNum).style.display='none';
	}
}


// 확대축소
var nowZoom = 100;
var maxZoom = 115;
var minZoom = 95;

function zoomIn() {
	
	if (nowZoom < maxZoom){
		nowZoom += 5;
    } else {
    	alert('더이상 화면을 확대 하실 수 없습니다.');
    	return false;
	}
	document.body.style.zoom = nowZoom + "%";
}

function zoomOut() {
	if (nowZoom > minZoom){
		nowZoom -= 5;
    } else {
    	alert('더이상 화면을 축소 하실 수 없습니다.');
    	return false;
	}
	
	document.body.style.zoom = nowZoom + "%";
}

function zoomFrist() {	
	nowZoom = 100;   
	document.body.style.zoom = nowZoom + "%";
}

// 상단 GNB
function tabNmenu(tabName,firstChar){
	var nav=document.getElementById(tabName);
	var menuNum=nav.getElementsByTagName("ul");
	var mainNum=nav.children;
	for(var k=0; k<menuNum.length; k++){
			var subNum=menuNum[k].getElementsByTagName("li");  //각 서브메뉴 
			//alert(subNum.length);
			for(var h=0;  h<subNum.length; h++){
				subNum[h].children[0].onfocus=subNum[h].children[0].onmouseover=function(){

						
					if(this.children[0])this.children[0].src=this.children[0].src.replace("out.gif","over.gif");
				}

				subNum[h].children[0].onblur=subNum[h].children[0].onmouseout=function(){
					if(this.children[0])this.children[0].src=this.children[0].src.replace("over.gif","out.gif");
				}
			}
	}
	
	
	

	function chMenu(num){
		for(var i=0; i<mainNum.length; i++){
			if(i==num-1){
				
				if(menuNum[i])menuNum[i].style.display='block';
				
				var theBtn=mainNum[i].children[0].children[0];
				theBtn.src=theBtn.src.replace("out.gif","over.gif");
			}else{
				if(menuNum[i])menuNum[i].style.display='none';

				var theBtn=mainNum[i].children[0].children[0];
				theBtn.src=theBtn.src.replace("over.gif","out.gif");
			}

		}
	}

	for(var i=0; i<mainNum.length; i++){
		var aTag=mainNum[i].children[0];
		aTag.onfocus=aTag.onmouseover=function(){
			var idName=this.getAttribute("id");
			var theNum=idName.replace(firstChar,"");
			chMenu(theNum);
		}
	}

}


//자동 선택 메뉴부분 
function preSel(tabName,firstChar,Num100,Num200){

	if(tabName)
	{
		var nav=document.getElementById(tabName);
		var menuNum=nav.getElementsByTagName("ul");
		var mainNum=nav.children;
		var over_color="red";
		var out_color="#333";
		for(var k=0; k<menuNum.length; k++){
				var subNum=menuNum[k].getElementsByTagName("li");  //각 서브메뉴 
				//console.log(subNum);
				for(var h=0;  h<subNum.length; h++){
					subNum[h].children[0].onfocus=subNum[h].children[0].onmouseover=function(){

							
						if(this.children[0]){
							if(this.children[0].src.indexOf("_over")==-1)this.children[0].src=this.children[0].src.replace("out.gif","over.gif");
							keyNum=1;
						}else{
							this.style.color=over_color;
						}
						clearInterval(nowMenu);
					}

					subNum[h].children[0].onblur=subNum[h].children[0].onmouseout=function(){
						if(this.children[0]){
							this.children[0].src=this.children[0].src.replace("over.gif","out.gif");
						}else{
							this.style.color=out_color;
						}
						nowMenu=setInterval(function(){preSel(tabName,firstChar,Num100,Num200)},2000);
					}
				}
		}
	}
	
	
	

	function chMenu(num){
		for(var i=0; i<mainNum.length; i++){
			if(i==num-1){
				
				if(menuNum[i])menuNum[i].style.display='block';
				
				var theBtn=mainNum[i].children[0].children[0];
				theBtn.src=theBtn.src.replace("out.gif","over.gif");
			}else{
				if(menuNum[i])menuNum[i].style.display='none';

				var theBtn=mainNum[i].children[0].children[0];
				theBtn.src=theBtn.src.replace("over.gif","out.gif");
			}

		}
	}

	for(var i=0; i<mainNum.length; i++){
		var aTag=mainNum[i].children[0];
		aTag.onfocus=aTag.onmouseover=function(){
			var idName=this.getAttribute("id");
			var theNum=idName.replace(firstChar,"");
			chMenu(theNum);

			clearInterval(nowMenu);
		}

		aTag.blur=aTag.onmouseout=function(){
			nowMenu=setInterval(function(){preSel(tabName,firstChar,Num100,Num200)},2000);
		}
	}

	
	try {
		chMenu(Num100);
		var	mNum=menuNum[Num100-1].getElementsByTagName("li");
		if(mNum[Num200-1].children[0].children[0]){
			if(keyNum==1)mNum[Num200-1].children[0].children[0].src=mNum[Num200-1].children[0].children[0].src.replace("out.gif","over.gif");
			keyNum++;
		}else{
			mNum[Num200-1].children[0].style.color="orange";
		}
	} catch (e) {}
}


//자동롤링 배너 만들기 함수
function autoRollingBanner(rName,delay){
		var rollNum=0;
		var imgNum=0;

		var rollBox=document.getElementById("auto_banner");	
		var pNum=rollBox.getElementsByTagName("p");
		var btnNum=rollBox.getElementsByTagName("li");

		function chNumbtn(nowNum){
			var firstNum=0;
			nowNum=nowNum-1;
			for(var i=0; i<btnNum.length; i++){
				if(btnNum[i].children[0].className.indexOf("autobtn")>-1){	
					if(firstNum==nowNum){
						if(btnNum[i].children[0].children[0].src.indexOf("_over")==-1)btnNum[i].children[0].children[0].src=btnNum[i].children[0].children[0].src.replace("out.gif","over.gif");
					}else{
						btnNum[i].children[0].children[0].src=btnNum[i].children[0].children[0].src.replace("over.gif","out.gif");
					}
					firstNum++;
				}
				
			}
		}

		for(i=0; i<btnNum.length; i++){
			if(btnNum[i].children[0].className=="play"){
					btnNum[i].onclick=function(){
					 if(kk==0)kk=setInterval(function(){autoRoll("auto_banner",0)},2000);
					}
			}else if(btnNum[i].children[0].className=="stop"){
					btnNum[i].onclick=function(){
						clearInterval(kk);
						kk=0;
					}
			}else{
					btnNum[i].children[0].onclick=function(){
						clearInterval(kk);
						kk=0;
						imgNum=Number(this.className.replace("autobtn",""));
						autoRoll("auto_banner",imgNum);
						chNumbtn(imgNum);
						return false;
					}
			}
		}


		for(i=0; i<pNum.length;i++){
				if(i==0){
					pNum[i].style.display="block";
				}else{
					pNum[i].style.display="none";
				}
		}
		function autoRoll(rollName,overNum){
			if(overNum>0){rollNum=overNum-1;}
			for(i=0; i<pNum.length;i++){
				if(i==rollNum){
					pNum[i].style.display="block";
					chNumbtn(i+1);
				}else{
					pNum[i].style.display="none";
				}
			}
		rollNum++;
		if(rollNum>pNum.length-1){
				rollNum=0;
		}
	  }
	  kk=setInterval(function(){autoRoll(rName,0)},delay);
  }
  // TAB
 jQuery(function($){
		var tab = $('.tab');
		tab.removeClass('jsOff');
		tab.css('height', tab.find('>ul>li>ul:visible').height()+30);
		function onSelectTab(){
			var t = $(this);
			var myClass = t.parent('li').attr('class');
			t.parents('.tab:first').attr('class', 'tab '+myClass);
			tab.css('height', t.next('ul').height()+30);
			return false;
		}
		tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
		
		});