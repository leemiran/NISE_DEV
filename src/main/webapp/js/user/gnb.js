
function tabNmenu(tabName,firstChar){
	var nav=document.getElementById(tabName);
	var menuNum=nav.getElementsByTagName("ul");
	var mainNum=nav.children;
	
	for(var k=0; k<menuNum.length; k++){
			var subNum=menuNum[k].getElementsByTagName("li");  //각 서브메뉴 
			//alert(subNum.length);
			for(var h=0;  h<subNum.length; h++){
				subNum[h].children[0].onfocus=subNum[h].children[0].onmouseover=function(){

						
					if(this.children[0])this.children[0].src=this.children[0].src.replace(".gif","_on.gif");
				}

				subNum[h].children[0].onblur=subNum[h].children[0].onmouseout=function(){
					if(this.children[0])this.children[0].src=this.children[0].src.replace("_on.gif",".gif");
				}
			}
	}
	
	
	

	function chMenu(num){
		for(var i=0; i<mainNum.length; i++){
			if(i==num-1){
				
				if(menuNum[i])menuNum[i].style.display='block';
				
				var theBtn=mainNum[i].children[0].children[0];
				theBtn.src=theBtn.src.replace(".gif","_on.gif");
			}else{
				if(menuNum[i])menuNum[i].style.display='none';

				var theBtn=mainNum[i].children[0].children[0];
				theBtn.src=theBtn.src.replace("_on.gif",".gif");
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
	var nav=document.getElementById(tabName);
	var menuNum=nav.getElementsByTagName("ul");
	var mainNum=nav.children;
	var over_color="red";
	var out_color="#333";
	for(var k=0; k<menuNum.length; k++){
			var subNum=menuNum[k].getElementsByTagName("li");  //각 서브메뉴 
			//alert(subNum.length);
			for(var h=0;  h<subNum.length; h++){
				subNum[h].children[0].onfocus=subNum[h].children[0].onmouseover=function(){

						
					if(this.children[0]){
						if(this.children[0].src.indexOf("_on")==-1)this.children[0].src=this.children[0].src.replace(".gif","_on.gif");
						keyNum=1;
					}else{
						this.style.color=over_color;
					}
					clearInterval(nowMenu);
				}

				subNum[h].children[0].onblur=subNum[h].children[0].onmouseout=function(){
					if(this.children[0]){
						this.children[0].src=this.children[0].src.replace("_on.gif",".gif");
					}else{
						this.style.color=out_color;
					}
					nowMenu=setInterval(function(){preSel(tabName,firstChar,Num100,Num200)},2000);
				}
			}
	}
	
	
	

	function chMenu(num){
		for(var i=0; i<mainNum.length; i++){
			if(i==num-1){
				
				if(menuNum[i])menuNum[i].style.display='block';
				
				var theBtn=mainNum[i].children[0].children[0];
				theBtn.src=theBtn.src.replace(".gif","_on.gif");
			}else{
				if(menuNum[i])menuNum[i].style.display='none';

				var theBtn=mainNum[i].children[0].children[0];
				theBtn.src=theBtn.src.replace("_on.gif",".gif");
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

	
	
	chMenu(Num100);
	var	mNum=menuNum[Num100-1].getElementsByTagName("li");
		if(mNum[Num200-1].children[0].children[0]){
		if(keyNum==1)mNum[Num200-1].children[0].children[0].src=mNum[Num200-1].children[0].children[0].src.replace(".gif","_on.gif");
		keyNum++;
		}else{
		mNum[Num200-1].children[0].style.color="orange";
		}
}