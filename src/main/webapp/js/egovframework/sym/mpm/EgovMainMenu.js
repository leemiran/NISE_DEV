/*
 * 노드 , 트리 구성 정보 선언
 */
var treeNodes			= new Array();
var openTreeNodes	    = new Array();
var treeIcons			= new Array(6);
var imgpath         = "/images/egovframework/cmm/utl/";
var treeYeobu       = false;
var chkValue        = "";
var vHtmlCode       = "";

/*
 * 노드 , 트리 구성 이미지 정보
 */
function preloadIcons() {
	treeIcons[0] = new Image();
	treeIcons[0].src = imgpath+"menu_plus.gif";
	treeIcons[1] = new Image();
	treeIcons[1].src = imgpath+"menu_plusbottom.gif";
	treeIcons[2] = new Image();
	treeIcons[2].src = imgpath+"menu_minus.gif";
	treeIcons[3] = new Image();
	treeIcons[3].src = imgpath+"menu_minusbottom.gif";
	treeIcons[4] = new Image();
	treeIcons[4].src = imgpath+"menu_folder.gif";
	treeIcons[5] = new Image();
	treeIcons[5].src = imgpath+"menu_folderopen.gif";
} 
/*
* 트리생성함수
*/
function createTree(arrName, vYeobu, checkValue) {
   var startNode, openNode;
	treeNodes = arrName;
	treeYeobu = vYeobu;
	chkValue = checkValue;//"2000000"
	startNode = chkValue;
	if (treeNodes.length > 0) {
		preloadIcons();
		
		vHtmlCode +="<table width='181' height='94' border='1' align='center' cellpadding='0' cellspacing='0'><tr>";
		vHtmlCode +="<td valign='bottom' background='/images/egovframework/left_menu_top.gif' style='background-repeat:no-repeat'>";
	
		if (startNode == null) startNode = 0;
		if (openNode != 0 || openNode != null) setOpenTreeNodes(openNode);
		if (startNode !=0) {
			var _getTreeArrayId = getTreeArrayId(startNode)
			var nodeValues = treeNodes[getTreeArrayId(startNode)].split("|");
			vHtmlCode +="<div class='LeftMenuTitle'><font color='#ffffff'>" + nodeValues[2] + "</font></div></td></tr>"
			vHtmlCode +="<tr>";
		} else vHtmlCode +="<img src='"+imgpath+"menu_base.gif' border='0' align='absbottom' alt='' >메뉴목록<br>";
		var recursedNodes = new Array();
		addTreeNode(startNode, recursedNodes);
		vHtmlCode +="<tr><td height='30' valign='bottom' background='/images/egovframework/left_menu_btm.gif' style='background-repeat:no-repeat'>&nbsp;</td></tr></table>";
		document.write(vHtmlCode);
	}
}
/*
* 노드위치 확인
*/
function getTreeArrayId(node) {
	for (i=0; i<treeNodes.length; i++) {
		var nodeValues = treeNodes[i].split("|");
		if (nodeValues[0]==node) return i;
	}
	return 0;
}
/*
* 트리 노드 열기
*/
function setOpenTreeNodes(openNode) {
	for (i=0; i<treeNodes.length; i++) {
		var nodeValues = treeNodes[i].split("|");
		if (nodeValues[0]==openNode) {
			openTreeNodes.push(nodeValues[0]);
			setOpenTreeNodes(nodeValues[1]);
		}
	} 
}
/*
* 트리노드 오픈 여부 확인
*/
function isTreeNodeOpen(node) {
   if (treeYeobu){ return true; }
   for (i=0; i<openTreeNodes.length; i++){
	   if (openTreeNodes[i]==node){ return true; }
   }
   return false;
}
/*
* 하위 트리노드 존재여부 확인
*/
function hasChildTreeNode(parentNode) {
	for (i=0; i< treeNodes.length; i++) {
		var nodeValues = treeNodes[i].split("|");
		if (nodeValues[1] == parentNode) return true;
	}
	return false;
}
/*
* 트리노드 최하위 여부 확인
*/
function lastTreeSibling (node, parentNode) {
	var lastChild = 0;
	for (i=0; i< treeNodes.length; i++) {
		var nodeValues = treeNodes[i].split("|");
		if (nodeValues[1] == parentNode)
			lastChild = nodeValues[0];
	}
	if (lastChild==node) return true;
	return false;
}
/*
* 신규 트리노드 추가
*/
function addTreeNode(parentNode, recursedNodes) {
	for (var i = 0; i < treeNodes.length; i++) {

		var nodeValues = treeNodes[i].split("|");
		if (nodeValues[1] == parentNode) {
		
			var lastSibling	= lastTreeSibling(nodeValues[0], nodeValues[1]);
			var hasChildNode	= hasChildTreeNode(nodeValues[0]);
			var isNodeOpen = isTreeNodeOpen(nodeValues[0]);
			if (lastSibling) recursedNodes.push(0);
			else recursedNodes.push(1);
			if (hasChildNode) {
				if (lastSibling) {
					vHtmlCode +="<tr><td><a href='javascript: openCloseEx(" + nodeValues[0] + ", 1);'><img id='join" + nodeValues[0] + "' src='"+imgpath;
					 	if (isNodeOpen) vHtmlCode +="menu_minus";
						else vHtmlCode +="menu_plus";
					vHtmlCode +="bottom.gif' border='0' align='absbottom' alt='Open/Close node' ></a>";
				} else { 
					vHtmlCode +="<tr><td><a href='javascript: openCloseEx(" + nodeValues[0] + ", 0);'><img id='join" + nodeValues[0] + "' src='"+imgpath;
						if (isNodeOpen) vHtmlCode +="menu_minus";
						else vHtmlCode +="menu_plus";
					vHtmlCode +=".gif' border='0' align='absbottom' alt='Open/Close node' /></a>";
				}
			} else {
				if (lastSibling) vHtmlCode +="<tr><td><img src='"+imgpath+"menu_joinbottom.gif' border='0' align='absbottom' alt='' >";
				else vHtmlCode +="<tr><td><img src='"+imgpath+"menu_join.gif' border='0' align='absbottom' alt='' >";
			}
			vHtmlCode +="<a href=javascript:fn_MovePage('" + i + "');>";
			if (hasChildNode) {
				vHtmlCode +="<img id='icon" + nodeValues[0] + "' src='"+imgpath+"menu_folder";
					if (isNodeOpen) vHtmlCode +="open";
				vHtmlCode +=".gif' border='0' alt='Folder' >"+nodeValues[2]+"</a></td></tr>";
			} else{
				vHtmlCode +="<img id='icon" + nodeValues[0] + "' src='"+imgpath+"menu_page.gif' border='0' align='absbottom' alt='Page'>"+nodeValues[2]+"</a></td></tr>";
			}
			if (hasChildNode) {
				vHtmlCode +="<div id='div" + nodeValues[0] + "'";
					if (!isNodeOpen) vHtmlCode +=" style='display: none;'";
				vHtmlCode +="><tr><td>";
				addTreeNode(nodeValues[0], recursedNodes);
				vHtmlCode +="</td></tr></div>";
			}
			recursedNodes.pop();
		}
	}
}

/*
* 트리노드 액션(열기,닫기)
*/
function openCloseEx(node, bottom) {
	var treeDiv = document.getElementById("div" + node);
	var treeJoin	= document.getElementById("join" + node);
	var treeIcon = document.getElementById("icon" + node);
	
	if (treeDiv.style.display == 'none') {
		if (bottom==1) treeJoin.src = treeIcons[3].src;
		else treeJoin.src = treeIcons[2].src;
		treeIcon.src = treeIcons[5].src;
		treeDiv.style.display = '';
	} else {
		if (bottom==1) treeJoin.src = treeIcons[1].src;
		else treeJoin.src = treeIcons[0].src;
		treeIcon.src = treeIcons[4].src;
		treeDiv.style.display = 'none';
	}
}
if(!Array.prototype.push) {
	function fnArrayPush() {
		for(var i=0;i<arguments.length;i++)
			this[this.length]=arguments[i];
		return this.length;
	}
	Array.prototype.push = fnArrayPush;
}
if(!Array.prototype.pop) {
	function fnArrayPop(){
		lastElement = this[this.length-1];
		this.length = Math.max(this.length-1,0);
		return lastElement;
	}
	Array.prototype.pop = fnArrayPop;
}
/* ********************************************************
 * 상세내역조회 함수
 ******************************************************** */
 function fn_MovePage(nodeNum) {
		var nodeValues = treeNodes[nodeNum].split("|");
		parent.main_right.location.href = nodeValues[5];
} 