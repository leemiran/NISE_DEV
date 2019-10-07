// flash 인덱스 메뉴 링크
//-----------------------------------------------------------------------------------------------
function rgb_index(val){
  var url = "1";
  if (val == "1"){url = "01";} 
  else if (val == "2"){url = "02";}
  else if (val == "3"){url = "03";}
  else if (val == "4"){url = "04";}
  else if (val == "5"){url = "05";}
  else if (val == "6"){url = "06";}
  else if (val == "7"){url = "07";}
  else if (val == "8"){url = "08";}
  else if (val == "9"){url = "09";}
  else if (val == "10"){url = "10";}
  else if (val == "11"){url = "11";}
  else if (val == "12"){url = "12";}
  else if (val == "13"){url = "13";}
  else if (val == "14"){url = "14";}
  else if (val == "15"){url = "15";}
  else if (val == "16"){url = "16";}


  if(isForm == "local"){

	  var page = document.location.href;
		var nowFile = page.indexOf(".html"); // 해당문자의 위치를 반환
		var chCode = page.substring(nowFile-4, nowFile-2);
		var nowCode = page.substring(nowFile-2, nowFile);
	    var chaseCode =parseInt(chCode,10)
		if (chaseCode < 10)
		{
			chaseCode = "0"+chaseCode;
		}
	  chaseCode = chaseCode
	  url = chaseCode+url
	  moveframe(url)
  }else{
	//
  }
}

// 이전 페이지
function prevpage() {
	if(isForm == "local"){
		var page = document.location.href;
		var nowFile = page.indexOf(".html"); // 해당문자의 위치를 반환	
		var chCode = page.substring(nowFile-4, nowFile-2);
		var nowCode = page.substring(nowFile-2, nowFile);
		var chaseCode =parseInt(chCode,10)
		if (chaseCode < 10)
		{
			chaseCode = "0"+chaseCode;
		}
		chaseCode = chaseCode
		var prevCode =parseInt(nowCode,10) - 1;
		if (prevCode < 10)
		{
			prevCode = "0"+prevCode;
		}
		moveframe(String(chaseCode)+String(prevCode));
	}else{
		//
	}
}
//다음 페이지
function nextpage() {
	if(isForm == "local"){
		var page = document.location.href;
		var nowFile = page.indexOf(".html"); // 해당문자의 위치를 반환
		var chCode = page.substring(nowFile-4, nowFile-2);
		var nowCode = page.substring(nowFile-2, nowFile);
	    var chaseCode =parseInt(chCode,10)
		if (chaseCode < 10)
		{
			chaseCode = "0"+chaseCode;
		}
		chaseCode = chaseCode
		var nextCode =parseInt(nowCode,10) + 1;
		if (nextCode < 10)
		{
			nextCode = "0"+nextCode;
		}
		moveframe(String(chaseCode)+String(nextCode));
	}else{
		//
	}
}

//페이지 이동
function moveframe(movecode) {
	var movecode;
	//alert(movecode);
	var thispage = movecode.substr(2,3)

	if(isForm == "local"){
		//returnFile = movecode+".htm";
		//document.location.href = returnFile;
		//alert(thispage);	
		if (thispage > isTotalNum){
			alert("이번 차시의 마지막 페이지 입니다.");
		}else if (thispage < 1){
			alert("이번 차시의 첫 페이지 입니다.");
		}else{		
			returnFile = movecode+".html";
			document.location.href = returnFile;
		}
	}else{
		//
	}
}