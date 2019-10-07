var http;
	function getHTTPObject(){
		var xmlhttp;
		if (window.XMLHttpRequest){
			xmlhttp = new XMLHttpRequest();
		}else if (window.ActiveXObject){
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		return xmlhttp;
	}

	// GET방식으로 url 호출
	function goGetURL(url, param){
		http = getHTTPObject();
		http.onreadystatechange = callbackReal;
		http.open("GET", url + param, true);
		http.setRequestHeader("If-Modified-Since","Sat, 1 Jan 2000 00:00:00 GMT");
		http.send(null);
	}

	// POST방식으로 url 호출
	function goPostURL(url, param){
	    http = getHTTPObject();
	    http.onreadystatechange = callbackReal;
	    http.open("POST", url, true);
	    http.setRequestHeader("If-Modified-Since","Sat, 1 Jan 2000 00:00:00 GMT");
	    http.send(param);
	}
	

	function callbackReal(){
		if(http.readyState == 4){	//대기 완료
			if(http.status == 200){	//오류 없음
				var data = http.responseText;
				try{
					//비어있는 xml문서 object를 생성
					var xmlDoc2 = document.implementation.createDocument("", "", null);
					//XML문서 object를 생성
					var parser = new DOMParser();
					//String을 로드하기 위해서 파서에 알리는 부분
					var dom = parser.parseFromString(data, "text/xml");
					var xmlData = dom.getElementsByTagName("ajax_xml");
					var result = xmlData[0].getElementsByTagName("result");
					var arr = new Array();
					for( i=0; i<result.length; i++ ){
						var map = new Map();
						for( j=0; j<result[i].childNodes.length; j++ ){
							if(result[i].childNodes[j].childNodes.length > 0 ){
								map.put(result[i].childNodes[j].nodeName, result[i].childNodes[j].childNodes[0].nodeValue);
							}else{
								map.put(result[i].nodeName, result[i].childNodes[j].nodeValue);
							}
						}
						arr[i] = map;
					}
					
					finishAjax(arr);
				}catch(ex){}
			}else{
				alert("작업처리중 오류가 발생하였습니다. CODE : "+http.status);
			}
		}
	}
	
	function isLogin(options) {
		var result = false;
		$.ajax({
		    url: '/valid/isLogin.do',
		    async : false,
		    type: 'post',
		    dataType : "json",
		    data: options.toMap(),
		    error: function(){
		        alert('죄송합니다. 시스템의 오류가 발생하였습니다.\n\n문제가 계속 발생할 경우 관리자에 문의하시기 바랍니다.');
		        result = false;
		    },
		    success: function(data){
		    	if(data.resultCode) {
		    		result = true;
		    		if(data.resultMsg != "1"){
	    				alert(data.resultMsg);
	    			}
		    	} else {
		    		alert(data.resultMsg);
		    	}
		    }
		});
		return result;
	}