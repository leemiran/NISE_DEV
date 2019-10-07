
function processkey()
{
   //alert(event.keyCode);

   if( event.ctrlKey )
   {
	  alert( "CTRL 키는 사용할수 없습니다." );
	  if (event.keyCode == 116)
		   event.keyCode=0;

	  event.cancelBubble = true;
	  event.returnValue = false;
   }

}

function right(e)
{
   if(navigator.appName == 'Netscape' && (e.which==3 || e.which == 2))
   {
	   alert('마우스 오른쪽 버튼은 사용할수 없습니다');
	   return;
   }else if(navigator.appName == 'Microsoft Internet Explorer' && (event.button == 2 || event.button == 3))
   {
	   alert('마우스 오른쪽 버튼은 사용할수 없습니다.');
	   return;
   }
}

document.onclick 		= right;
document.ondblclick 	= right;
document.onmousedown    = right;
document.onmousesetup   = right;
document.onmousemove    = right;

document.onkeydown 	    = processkey;
document.onkeypress 	= processkey;
document.onkeyup 	    = processkey;

/*
function myEvent() {
   if(event.ctrlKey == true || event.ctrlLeft == true){
	   alert("사용할 수 없는 키입니다.");
   }else if(event.button ==2){
	   alert("마우스 오른쪽 버튼은 사용하실 수 없습니다.");
	   return;
   }
}

document.onclick 		= myEvent;
document.ondblclick 	= myEvent;
document.onkeydown 	    = myEvent;
document.onkeypress 	= myEvent;
document.onkeyup 	    = myEvent;
document.onmousedown    = myEvent;
document.onmousesetup   = myEvent;
*/
