
function getCookie(b) {
	for (var d = false, a, c, e = 0; e <= document.cookie.length; ) {
		a = e;
		c = a + b.length;
		if (document.cookie.substring(a, c) == b) {
			d = true;
			break;
		}
		e++;
	}
	if (d == true) {
		a = c + 1;
		c = document.cookie.indexOf(";", a);
		if (c < a) {
			c = document.cookie.length;
		}
		return document.cookie.substring(a, c);
	}
	return "";
}
function callSwfUpload(b, d) {
	finishFunction = d;
	arrMovie = [];
	d = 0;
	var a = document.getElementsByTagName("object"), c;
	for (i = 0; i < a.length; i++) {
		if (a[i].getAttribute("method") == "single_upload" || a[i].getAttribute("method") == "multi_upload") {
			c = document.getElementsByName(a[i].getAttribute("id"))[0] ? document.getElementsByName(a[i].getAttribute("id"))[0] : document.getElementById(a[i].getAttribute("id"));
			var e = 0;
			if ($("uploadDiv")) {
				if ($("uploadDiv").style.display != "none") {
					e = c.GetVariable("totalSize");
				}
			} else {
				e = c.GetVariable("totalSize");
			}
			if (e > 0) {
				arrMovie[d] = c;
				d++;
			}
		}
	}
	if ($F("fileValidType") == "1") {
		if (b == "insert") {
			if (arrMovie.length == 0) {
				alert(message.getMessageString("message_247"));
				return;
			}
		} else {
			if (b == "update") {
				if ($F("deleteFileSeqs") != "") {
					if (arrMovie.length == 0) {
						alert(message.getMessageString("message_247"));
						return;
					}
				}
			}
		}
	}
	if (arrMovie.length > 0) {
/*		
		if (b == "insert") {
			b = new JMap;
			b.put("fileMasterKey", $("fileMasterKey").value);
			b.put("fileDistriSymbol", $("fileDistriSymbol").value);
			b = dwrUtil.invokeMap(b.toMap(), {beanId:"fileDWR", method:"insertFileMaster"});
		}
*/
		if (arrMovie[0].getAttribute("method") == "single_upload" || arrMovie[0].parentNode.getAttribute("method") == "single_upload") {
			arrMovie[0].height = 70;
		}
		if (arrMovie[0].getAttribute("method") == "multi_upload" || arrMovie[0].parentNode.getAttribute("method") == "multi_upload") {
			arrMovie[0].height = parseInt(20 * arrMovie[0].GetVariable("listRows") + 25 + 45, 10);
		}
		arrMovie[0].SetVariable("startUpload", "");
		arr_mov = 0;
	} else {
//		if (b == "insert") {
//			$('fileMasterKey').value = '';
//		}
		finishFunction();
	}
}
var finishFunction;
function swfUploadComplete() {
	arr_mov++;
	if (arrMovie.length > arr_mov) {
		if (arrMovie[arr_mov].getAttribute("method") == "single_upload" || arrMovie[arr_mov].parentNode.getAttribute("method") == "single_upload") {
			arrMovie[arr_mov].height = 70;
		}
		if (arrMovie[arr_mov].getAttribute("method") == "multi_upload" || arrMovie[arr_mov].parentNode.getAttribute("method") == "multi_upload") {
			arrMovie[arr_mov].height = parseInt(20 * arrMovie[arr_mov].GetVariable("listRows") + 25 + 45, 10);
		}
		arrMovie[arr_mov].SetVariable("startUpload", "");
	} else {
		finishFunction();
	}
}
function fileTypeError(b) {
	alert(message.getMessageString("message_369"));
}
function overSize(b) {
	alert(message.getMessageString("message_370"));
}
function versionError() {
}
function httpError() {
	alert(message.getMessageString("message_372"));
}
function ioError() {
	alert(message.getMessageString("message_373"));
}
function onSecurityError() {
	alert(message.getMessageString("message_374"));
}

