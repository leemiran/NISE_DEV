<%
/*
*  JDC (Java DHTML Control) by Hank Moon (Han-Goo Moon in Korean)
*  Copyright (C) 2003 Hank Moon
*
*  This program is distributed in the hope that it will be useful,
*  You can use it and/or modify it, If you do not use it for commercial purpose
*  If you want it to use for commercial purpose, 
*  You have to buy or receive a copy of the License to use this software from 
*  Hank Moon or HMSOFT Inc., Ltd. (www.hmsoft.org)
*
*  Hank Moon , HMSOFT Inc., Ltd. <hankmoon@hmsoft.org>
*
*/
%>

<!-- JSTreeCtrl.jsp : start -->

<script>

	function JLTreeCtrl_getFocusNode()
	{
		if (!(Number(m_nFocusTree) >= 0) ||
			!(Number(m_nFocusIdx) >= 0))
		{
			return null;
		}
		var node = JLTreeCtrl_getNode(m_nFocusTree,m_nFocusIdx);
		return node;
	}
	
	function _onchangeEx()
	{
	}

	function JLTreeCtrl_onchangeEdit(obj)
	{
	}

	function JLTreeCtrl_editNodeEx(node)
	{
		if (node == null)
			return;
		JLTreeCtrl_editNode(node.treeidx,node.idx);
	}

	function JLTreeCtrl_editNode(treeidx,nodeidx)
	{
		var tagName = "td_"+treeidx+"_"+nodeidx;
		JLTreeCtrl_blurNode(treeidx,nodeidx);
		var elmt = document.getElementById(tagName);
		if (elmt == null)
			return;

		var rect = elmt.getClientRects();
		var nLen = rect.length;
		var nMarginLeft = window.screenLeft - top.screenLeft;
		var nMarginTop = window.screenTop - top.screenTop;
		var elmtBox = document.getElementById("divTreeEditBox");
		elmtBox.style.posLeft = rect[0].left - nMarginLeft + 1 + window.document.body.scrollLeft;
		elmtBox.style.posTop = rect[0].top - nMarginTop - 1 + window.document.body.scrollTop;

		var elmtInput = document.getElementById("_treenodeeitbox");
		if (elmtInput == null)
			return;
		var sText = elmt.innerText;
		elmtInput.value = sText;
		elmtInput.focus();

		var nCellWidth = _getHtmlWidth(elmt.innerHTML);
		elmtInput.style.width = nCellWidth;
	}

	function JLTreeCtrl_editFocusNode()
	{
		if (!(Number(m_nFocusTree) >= 0) ||
			!(Number(m_nFocusIdx) >= 0))
		{
			return;
		}
		JLTreeCtrl_editNode(m_nFocusTree,m_nFocusIdx);
	}

	function JLTreeCtrl_onblurEdit(obj)
	{
		if (!(Number(m_nFocusTree) >= 0) ||
			!(Number(m_nFocusIdx) >= 0))
		{
			return;
		}
		var elmt = document.getElementById("_treenodeeitbox");
		if (elmt == null)
			return;
		var sText = elmt.value;

		JLTreeCtrl_setNodeText(m_nFocusTree,m_nFocusIdx,sText);
		JLTreeCtrl_hideEditBox();
		JLTreeCtrl_focusNode(m_nFocusTree,m_nFocusIdx);

		var root = m_aTree[m_nFocusTree];
		if (root == null)
			return;
		var node = root.node[m_nFocusIdx];
		if (node == null)
			return;
		JLTreeCtrl_onUpdateEx(node);
	}

	function JLTreeCtrl_getNode(treeidx,nodeidx)
	{
		if (!(Number(treeidx) >= 0) ||
			!(Number(nodeidx) >= 0))
		{
			return null;
		}
		var root = m_aTree[treeidx];
		if (root == null)
			return null;
		var node = root.node[nodeidx];
		if (node == null)
			return null;
		return node;
	}

	function JLTreeCtrl_onUpdateEx(node)
	{
		// alert(sText);
	}

	function JLTreeCtrl_setNodeText(treeidx,nodeidx,sText)
	{
		var elmt = document.getElementById("td_"+treeidx+"_"+nodeidx);
		if (elmt == null)
			return;

		var sImgPath = JLTreeCtrl_getImgPath(m_aTree[treeidx]);
		var sInnerHtml = "<img alt border=0 height=3 src='"+sImgPath+"/"+"trans.gif' width=3><font color=\"#000000\" id='fnt_"+treeidx+"_"+nodeidx+"' style=\"FONT: 9pt\">"+sText+"</font><img alt border=0 height=3 src='"+sImgPath+"/"+"trans.gif' width=4>";
		var nCellWidth = _getHtmlWidth(sInnerHtml);
		var sHtml = "<img alt border=0 height=2 src=images/trans.gif width="+nCellWidth+"><br>"+sInnerHtml;
		elmt.innerHTML = sHtml;
		
		var node = JLTreeCtrl_getNode(treeidx,nodeidx);
		if (node == null)
			return;
		node.name = sText;
	}

	function JLTreeCtrl_hideEditBox()
	{
		var elmt = document.getElementById("divTreeEditBox");
		if (elmt == null)
			return;
		elmt.style.top = -1000;
		elmt.style.left = -1000;
	}

	function JLTreeCtrl_editNodeKeyDown()
	{
		var elmt = document.getElementById("_treenodeeitbox");
		if (elmt == null)
			return;
		var sText = elmt.value;
		var treeidx = m_nFocusTree;
		var nodeidx = m_nFocusIdx;
		var sImgPath = JLTreeCtrl_getImgPath(m_aTree[treeidx]);
		var sInnerHtml = "<img alt border=0 height=3 src='"+sImgPath+"/"+"trans.gif' width=3><font color=\"#000000\" id='fnt_"+treeidx+"_"+nodeidx+"' style=\"FONT: 9pt\">"+sText+"</font><img alt border=0 height=3 src='"+sImgPath+"/"+"trans.gif' width=4>";
		var nCellWidth = _getHtmlWidth(sInnerHtml);
		elmt.style.width = nCellWidth;
	}

	function JLTreeCtrl_printEditBox()
	{
		var sHtml = "";
		sHtml += "<div id=divTreeEditBox style=\"POSITION: absolute; top:-1000px; left:-1000px; width:0px; height:0px;\">";
		sHtml += "<input id=_treenodeeitbox type=text value=\"Pusan\" maxlength=512 style=\"BACKGROUND-COLOR: white; BORDER-BOTTOM: #000000 1px solid; BORDER-LEFT: #000000 1px solid; BORDER-RIGHT: #000000 1px solid; BORDER-TOP: #000000 1px solid; FONT-FAMILY: ; FONT-SIZE: 9pt\" onchange=\"javascript:return JLTreeCtrl_onchangeEdit(m_obj)\" onblur=\"javascript:JLTreeCtrl_onblurEdit(m_obj)\" onkeydown=\"javascript:JLTreeCtrl_editNodeKeyDown()\">";
		sHtml += "</div>";
		document.write(sHtml);
	}

	JLTreeCtrl_printEditBox();

	function JLTreeCtrl_setEditBoxPosition(obj,x,y,width,height)
	{
		if (obj == null)
			return;
		var elmt = obj.m_wnd.document.getElementById("divTreeEditBox");
		if (elmt == null)
			return;
		elmt.style.left = x;
		elmt.style.top = y;
	}

	function JLTreeCtrl_getImgPath(node)
	{
		var form = JLTreeCtrl_getTreeForm(node);
		if (form == null)
			return "";
		if (form._imgpath == null)
			return "";
		var sPath = form._imgpath.value;
		return sPath;
	}

	var m_aTree = new Array();

	function JLTreeCtrl_dumpHtml(sArg)
	{
		document.write("<font style=\"FONT: 9pt\">");
		var sHtml = new String(sArg);
		var size = sHtml.length;
		for (var i=0;i<size;i++)
		{
			if (sHtml.charAt(i) == '<')
				document.write("&lt;");
			else
			{
				if (sHtml.charAt(i) == '>')
					document.write("&gt;<br>");
				else
					document.write(sHtml.charAt(i));
			}
		}
		document.write("</font>");
	}

	function JLTreeCtrl_debugHtml(treeidx)
	{
		{
			var nodeid = "treenode_"+treeidx+"_"+0;
			var obj = document.getElementById(nodeid);
			if (obj != null)
				JLTreeCtrl_dumpHtml(obj.outerHTML);
		}
		{
			var nodeid = "treespan_"+treeidx+"_"+0;
			var obj = document.getElementById(nodeid);
			if (obj != null)
				JLTreeCtrl_dumpHtml(obj.outerHTML);
		}
	}

	function JLTreeCtrl_getRoot(node)
	{
		if (node == null)
			return null;
		var oParent = node;
		while (true)
		{
			if (oParent.parent == null)
				break;
			oParent = oParent.parent;
		}
		return oParent;
	}

	function JLTreeCtrl_addTree(oParent,vTree,treeidx)
	{
		if (vTree == null)
			return;
		var nSize = vTree.length/8;
		var idx = 0;
		var parentid = -1;
		if (oParent != null)
			parentid = oParent.id;
		for (var i=0;i<nSize;i++)
		{
			idx = i*8;
			if (vTree[idx+0] == parentid)
			{
				// id,oParent,name,icon,iconex,expand,treeidx,action
				var node = JLTreeCtrl_insertNode(vTree[idx+1],oParent,vTree[idx+2],vTree[idx+3],vTree[idx+4],vTree[idx+5],treeidx,vTree[idx+6]);
				JLTreeCtrl_addTree(node,vTree,treeidx);
			}
		}
	}

	function JLTreeCtrl_dumpTree(node)
	{
		if (node == null)
			return;
		var sHtml = JLTreeCtrl_getNodeHtmlEx(node);
		JLTreeCtrl_dumpHtml(sHtml);
		var nCount = Number(node.childcount);
		for (var i=0;i<nCount;i++)
		{
			if (node.child == null)
				break;
			JLTreeCtrl_dumpTree(node.child[i]);
		}
	}

	function JLTreeCtrl_getTreeHtml(node)
	{
		if (node == null)
			return;
		var sHtml = JLTreeCtrl_getNodeHtml(node);
		var spanid = "treespan_"+node.treeidx+"_"+node.idx;
		if (node.expand > 0)
			sHtml += "<SPAN id=\""+spanid+"\" style=\"display : visible\">";
		else
			sHtml += "<SPAN id=\""+spanid+"\" style=\"display : none\">";

		var nCount = Number(node.childcount);
		for (var i=0;i<nCount;i++)
		{
			if (node.child == null)
				break;
			sHtml += JLTreeCtrl_getTreeHtml(node.child[i]);
		}
		sHtml += "</SPAN>";
		return sHtml;
	}

	function JLTreeCtrl_printTree(node)
	{
		if (node == null)
			return;
		var sHtml = JLTreeCtrl_getNodeHtml(node);
		var spanid = "treespan_"+node.treeidx+"_"+node.idx;
		if (node.expand > 0)
			sHtml += "<SPAN id=\""+spanid+"\" style=\"display : visible\">";
		else
			sHtml += "<SPAN id=\""+spanid+"\" style=\"display : none\">";
		document.write(sHtml);
		var nCount = Number(node.childcount);
		for (var i=0;i<nCount;i++)
		{
			if (node.child == null)
				break;
			JLTreeCtrl_printTree(node.child[i]);
		}
		document.write("</SPAN>");
	}

	function JLTreeCtrl_initTree(oParent,vTree,treeidx)
	{
		if (vTree == null)
			return;
		var nSize = vTree.length/8;
		var idx = 0;
		var parentid = -1;
		if (oParent != null)
			parentid = oParent.id;
		for (var i=0;i<nSize;i++)
		{
			idx = i*8;
			if (vTree[idx+0] == parentid)
			{
				// id,oParent,name,icon,iconex,expand,treeidx,action
				var node = JLTreeCtrl_addNode(vTree[idx+1],oParent,vTree[idx+2],vTree[idx+3],vTree[idx+4],vTree[idx+5],treeidx,vTree[idx+6],vTree[idx+7]);
				if (node != null)
					JLTreeCtrl_initTree(node,vTree,treeidx);
			}
		}
	}

	function JLTreeCtrl_setClass(treeidx,sClass)
	{
		// root
		if (m_aTree[treeidx] != null)
		{
			var root = m_aTree[treeidx];
			if (root != null)
				root.classname = sClass;
		}
	}

	function JLTreeCtrl_setName(treeidx,sName)
	{
		// root
		if (m_aTree[treeidx] != null)
		{
			var root = m_aTree[treeidx];
			if (root != null)
				root.instname = sName;
		}
	}

	function JLTreeCtrl_setRoot(sTreename,id,oParent,name,icon,iconex,expand,treeidx,action,childcount,nTreeIcon)
	{
		var oRoot = JLTreeCtrl_addNode(id,oParent,name,icon,iconex,expand,treeidx,action,childcount);
		JLTreeCtrl_setName(treeidx,sTreename);
		return oRoot;
	}

	function JLTreeCtrl_addNode(id,oParent,name,icon,iconex,expand,treeidx,action,childcount)
	{
		// oParent could be null
		// if oParent == null : root node

		var node = new Object();
		node.id = id;
		node.treeidx = treeidx;
		node.name = name;
		node.icon = icon;
		node.iconex = iconex;
		node.next = null;
		node.action = action;
		node.parent = oParent;
		if (expand > 0)
			node.expand = 1;
		if (childcount > 0)
			node.childcount = childcount;

		if (oParent != null)
		{
			if (oParent.child == null)
			{
				oParent.child = new Array();
				oParent.childcount = 0;
			}
			if (oParent.childcount > 0)
				oParent.child[oParent.childcount-1].next = node;
			oParent.child[oParent.childcount] = node;
			oParent.childcount ++;
		}

		if (oParent == null)
		{
			m_aTree[treeidx] = node;
			// default inst name
			JLTreeCtrl_setName("treectrl");
		}

		var root = m_aTree[treeidx];
		if (root != null)
		{
			if (root.node == null)
			{
				root.node = new Array();
				root.nodecount = 0;
			}
			root.node[root.nodecount] = node;
			node.idx = root.nodecount;
			root.nodecount ++;
			node.root = root;
		}
		return node;
	}
	
   // sjs 06.23 
   // 컨트롤 트리의 마우스 액션을 정의합니다.
   // note: "JRptMoniter.jsp" is an example where calls these methods.
   function onclicknode(node) 
   {
      //root node의 원클릭 액션 정의
      //alert("node: "+node);
   }
   function onclickDoc(node) 
   {
      //하위 노드의 원클릭 액션 정의
      //alert("node: "+node);
   }
   function ondblclicknode()
   {
   }
   
	function JLTreeCtrl_getChildIdx(node)
	{
		if (node == null)
			return -1;
		var oParent = node.parent;
		if (oParent != null)
		{
			for (var i=0;i<oParent.childcount;i++)
			{
				if (oParent.child[i] == node)
					return i;
			}
		}
		return -1;
	}

	function JLTreeCtrl_deleteNode(node)
	{
		if (node == null)
			return;
		var oParent = node.parent;
		if (oParent == null)
			return;
		var prev = JLTreeCtrl_getPrev(node);
		if (prev != null)
			prev.next = node.next;

		var idx = JLTreeCtrl_getChildIdx(node);
		if (idx < 0)
			return;
		for (var i=idx;i<oParent.childcount;i++)
			oParent.child[i] = oParent.child[i+1];
		oParent.childcount --;

		if (oParent.childcount <= 0)
			JLTreeCtrl_setState(oParent);

		var nodeid = "treenode_"+node.treeidx+"_"+node.idx;
		var elNode = document.getElementById(nodeid);
		if (elNode != null)
		{
			elNode.outerHTML = "";
		}
		var spanid = "treespan_"+node.treeidx+"_"+node.idx;
		var elSpan = document.getElementById(spanid);
		if (elSpan != null)
		{
			elSpan.outerHTML = "";
		}
		if (prev != null)
		{
			JLTreeCtrl_printNode(prev);
		}

		JLTreeCtrl_onDeleteEx(node);
	}

	function JLTreeCtrl_onDeleteEx(node)
	{
		if (node == null)
			return;
	}

	function JLTreeCtrl_getPrev(node)
	{
		if (node == null)
			return null;
		var oParent = node.parent;
		if (oParent == null)
			return null;
		var idx = JLTreeCtrl_getChildIdx(node);
		if (idx > 0)
		{
			return oParent.child[idx-1];
		}
		return null;
	}

	function JLTreeCtrl_getLastChild(node)
	{
		if (node == null)
			return null;
		var ret = null;
		if (node.child != null)
		{
			if (node.childcount > 0)
			{
				ret = node.child[node.childcount-1];
			}
		}
		return ret;
	}

	function JLTreeCtrl_submitCmd(obj,cmd,node,arg)
	{
		var param = JLTreeCtrl_getTreeForm(node);
		if (param == null)
			return null;

		param._cmd.value = cmd;
		param._argcount.value = arg.length;
		for (var i=0;i<param._argcount.value;i++)
			eval("param._arg"+i+".value = arg[i]");

		param.method = "post";
		param.target = "ftreedata_"+param._name.value;
		if (param._debug.value == 2)
			param.target = "_blank";
		param.submit();
	}

  
	function JLTreeCtrl_setDebug(nDebug)
	{
		var node = JLTreeCtrl_getFocusNode();
		if (node == null)
			return null;
		var param = JLTreeCtrl_getTreeForm(node);
		if (param == null)
			return null;
		param._debug.value = nDebug;
	}

	function JLTreeCtrl_insertNodeSvr(parentNode,id,text,icon,iconex,expand,action,childcount)
	{
		if (parentNode == null)
			parentNode = JLTreeCtrl_getFocusNode();
		if (parentNode == null)
			return;

		var arg = new Array();
		arg[0] = id;
		arg[1] = text;
		arg[2] = icon;
		arg[3] = iconex;
		arg[4] = expand;
		arg[5] = action;
		arg[6] = childcount;
		arg[7] = parentNode.id;
		arg[8] = parentNode.treeidx;
		arg[9] = parentNode.idx;
		
		JLTreeCtrl_submitCmd(m_obj,1,parentNode,arg);
	}

	function JLTreeCtrl_updateNodeSvr(oNode,arg)
	{
		JLTreeCtrl_submitCmd(m_obj,2,oNode,arg);
	}
	
	function JLTreeCtrl_deleteNodeSvr(oNode,arg)
	{
		JLTreeCtrl_submitCmd(m_obj,3,oNode,arg);
	}

	function JLTreeCtrl_onFinishInsert(arg)
	{
		if (arg == null)
			return;

		/*
		arg[0] = id;
		arg[1] = text;
		arg[2] = icon;
		arg[3] = iconex;
		arg[4] = expand;
		arg[5] = action;
		arg[6] = childcount;
		arg[7] = parentid;
		arg[8] = parenttreeidx;
		arg[9] = parentnodeidx;
		*/

		var id = arg[0];
		var text = arg[1];
		var icon = arg[2];
		var iconex = arg[3];
		var expand = arg[4];
		var action = arg[5];
		var childcount = arg[6];
		var node = JLTreeCtrl_getNode(arg[8],arg[9]);
		JLTreeCtrl_insertNodeEdit(node,id,text,icon,iconex,expand,action,childcount);
	}

	function JLTreeCtrl_onFinishUpdate(arg)
	{
	}

	function JLTreeCtrl_onFinishDelete(arg)
	{
		// alert("onfinishdelete : " + arg[1] + ":" + arg[2]);
		var node = JLTreeCtrl_getNode(arg[1],arg[2]);
		JLTreeCtrl_deleteNode(node);
	}

	function JLTreeCtrl_insertNodeEdit(node,id,text,icon,iconex,expand,action,childcount)
	{
		if (node == null)
			node = JLTreeCtrl_getFocusNode();
		if (node == null)
			return;
		JLTreeCtrl_blurNode(m_nFocusTree,m_nFocusIdx);
		var node = JLTreeCtrl_insertNode(id,node,text,icon,iconex,expand,node.treeidx,action,childcount);
		JLTreeCtrl_focusNode(node.treeidx,node.idx);
		JLTreeCtrl_editNode(node.treeidx,node.idx);
		return node;
	}

	function JLTreeCtrl_insertNode(id,oParent,name,icon,iconex,expand,treeidx,action,childcount)
	{
		var prev = null;
		if (oParent != null)
		{
			if (oParent.child != null)
			{
				if (oParent.childcount > 0)
				{
					prev = oParent.child[oParent.childcount-1];
				}
			}
		}
		var node = JLTreeCtrl_addNode(id,oParent,name,icon,iconex,expand,treeidx,action,childcount);
		if (prev != null && node != null)
		{
			JLTreeCtrl_printNode(prev);
		}

		if (oParent != null)
		{
			oParent.expand = 1;
			JLTreeCtrl_setState(oParent);
		}

		var sHtml = JLTreeCtrl_getNodeHtmlEx(node);
		if (oParent != null)
		{
			var parentSpanid = "treespan_"+oParent.treeidx+"_"+oParent.idx;
			var elParent = document.getElementById(parentSpanid);
			if (elParent != null)
			{
				elParent.innerHTML += sHtml;
			}
		}
		else
			document.write(sHtml);
		return node;
	}

	function JLTreeCtrl_getFocusNode()
	{
      //sjs 04.22 When a user clicks on the background, it returns with the default root, node values.
      if (m_nFocusTree < 0 || m_nFocusIdx < 0)
      {
         var root = m_aTree[0];
         var node = root.node[1];
         return node;
      }
      
		var root = m_aTree[m_nFocusTree];
		var node = root.node[m_nFocusIdx];
		return node;
	}

	var m_nFocusTree = -1;
	var m_nFocusIdx = -1;

	function JLTreeCtrl_blurNode(treeidx,idx)
	{
		var obj = document.getElementById("td_"+treeidx+"_"+idx);
		if (obj != null)
			obj.style.background = "#ffffff";
		obj = document.getElementById("fnt_"+treeidx+"_"+idx);
		if (obj != null)
			obj.color = "#000000";
	}

	function JLTreeCtrl_focusNode(treeidx,idx)
	{
		var obj = document.getElementById("td_"+treeidx+"_"+idx);
		if (obj != null)
			obj.style.background = "#122071";
		obj = document.getElementById("fnt_"+treeidx+"_"+idx);
		if (obj != null)
			obj.color = "#ffffff";
		m_nFocusTree = treeidx;
		m_nFocusIdx = idx;
	}

	function JLTreeCtrl_onmousedown(treeidx,idx)
	{
		if (idx == m_nFocusIdx && treeidx == m_nFocusTree)
			return;
		if (m_nFocusTree >= 0 && m_nFocusIdx >= 0)
		{
			JLTreeCtrl_blurNode(m_nFocusTree,m_nFocusIdx);
		}
		JLTreeCtrl_focusNode(treeidx,idx);
	}
	
//sunhee 2003.09.01 add 
   function JLTreeCtrl_onmousedblclick(treeidx,idx,action)
   {

      JLTreeCtrl_focusNode(treeidx,idx);
      var node = JLTreeCtrl_getFocusNode();
      ondblclicknode();
      
   }
   

	function JLTreeCtrl_getTreeNameByIdx(idx)
	{
		var root = m_aTree[idx];
		if (root == null)
			return null;
		return root.instname;
	}
	
	function JLTreeCtrl_getTreeName(node)
	{
		if (node == null)
			return null;
		var sName = node.root.instname;
		// alert("tree name by node " + sName);
		return sName;
	}
	
	function JLTreeCtrl_getTreeForm(node)
	{
		var sName = JLTreeCtrl_getTreeName(node);
		if (sName == null)
			return null;
		var form = eval("document."+sName);
		return form;			
	}

	function JLTreeCtrl_getData(node)
	{
		if (node == null)
			return;
		var form = JLTreeCtrl_getTreeForm(node);
		if (form != null)
		{
			form.treeidx.value = node.treeidx;
			form.idx.value = node.idx;
			form.parentid.value = node.id;

			form.method = "post";
			form.target = "ftreedata_"+JLTreeCtrl_getTreeName(node);
			// alert(form.action);
			// alert(form.target);
			// alert("debug mode : " +form.name+" : " + form._debug.value);
			// form.target = "_blank";
			// alert("form._debug.value : " + form._debug.value);
			if (form._debug.value == 1)
				form.target = "_blank";
			form.submit();
		}
	}

	function JLTreeCtrl_onDataReady(treeidx,idx,vTree)
	{
		var root = m_aTree[treeidx];
		if (root == null)
			return;
		var node = root.node[idx];
		if (node == null)
			return;

		JLTreeCtrl_initTree(node,vTree,treeidx)
		var sHtml = "";
		var nCount = Number(node.childcount);
		for (var i=0;i<nCount;i++)
		{
			if (node.child == null)
				break;
			sHtml += JLTreeCtrl_getTreeHtml(node.child[i]);
		}
		var elSpanid = "treespan_"+node.treeidx+"_"+node.idx;
		var elSpan = document.getElementById(elSpanid);
		if (elSpan != null)
			elSpan.innerHTML = sHtml;
	}

	function JLTreeCtrl_onExpand(treeidx,idx)
	{
		var root = m_aTree[treeidx];
		if (root == null)
			return;
		var node = root.node[idx];
		if (node == null)
			return;
		if (node.childcount > 0)
		{
			if (node.child == null)
				JLTreeCtrl_getData(node);
		}
		if (node.expand > 0)
			node.expand = 0;
		else
			node.expand = 1;
		JLTreeCtrl_setState(node)
	}

	function JLTreeCtrl_setState(node)
	{
		if (node == null)
			return;
		var obj = document.getElementById("treeimg_"+node.treeidx+"_"+node.idx);
		if (obj == null)
			return;
		var sHtml = "";
		sHtml += JLTreeCtrl_getNodeIconHtml(node);
		obj.outerHTML = sHtml;

		var spanid = "treespan_"+node.treeidx+"_"+node.idx;
		if (node.expand > 0)
			setIdProperty(spanid, "display", "inline");
		else
			setIdProperty(spanid, "display", "none");
		return;
	}

	function JLTreeCtrl_getNodeHtmlEx(node)
	{
		if (node == null)
			return;
		var sHtml = JLTreeCtrl_getNodeHtml(node);

		var spanid = "treespan_"+node.treeidx+"_"+node.idx;
		if (node.expand > 0)
			sHtml += "<SPAN id=\""+spanid+"\" style=\"display : visible\">";
		else
			sHtml += "<SPAN id=\""+spanid+"\" style=\"display : none\">";
		sHtml += "</SPAN>";
		return sHtml;
	}

	function JLTreeCtrl_getNodeIconHtml(node)
	{
		if (node == null)
			return;
		var sImgPath = JLTreeCtrl_getImgPath(node);
		var sHtml = "";
		var sImgScript = "href=\"javascript:JLTreeCtrl_onExpand("+node.treeidx+","+node.idx+");\"";
		var sImgHtml = "";
		if (node.parent == null)
		{
			// root
			if (!(node.childcount > 0))
			{
				sImgHtml += "<img id=treeimg_"+node.treeidx+"_"+node.idx+" alt border='0' src='"+sImgPath+"/"+"trans.gif' width=16 height=16>";
			}
			else
			{
				if (node.expand > 0)
					sImgHtml += "<img id=treeimg_"+node.treeidx+"_"+node.idx+" alt border='0' src='"+sImgPath+"/"+"icon_minus_r.gif'>";
				else
					sImgHtml += "<img id=treeimg_"+node.treeidx+"_"+node.idx+" alt border='0' src='"+sImgPath+"/"+"icon_plus_r.gif'>";
			}
		}
		else
		{
			if (node.next == null)
			{
				if (!(node.childcount > 0))
				{
					// leaf node
					m_sImgScript = "";
					sImgHtml = "<img alt border='0' src='"+sImgPath+"/"+"icon_leaf.gif'>";
				}
				else
				{
					if (node.expand > 0)
						sImgHtml = "<img alt border='0' src='"+sImgPath+"/"+"icon_minus_ns.gif'>";
					else
						sImgHtml = "<img alt border='0' src='"+sImgPath+"/"+"icon_plus_ns.gif'>";
				}
			}
			else
			{
				if (!(node.childcount > 0))
				{
					// leaf node
					m_sImgScript = "";
					sImgHtml = "<img alt border='0' src='"+sImgPath+"/"+"icon_leaf_s.gif'>";
				}
				else
				{
					if (node.expand > 0)
						sImgHtml = "<img alt border='0' src='"+sImgPath+"/"+"icon_minus_s.gif'>";
					else
						sImgHtml = "<img alt border='0' src='"+sImgPath+"/"+"icon_plus_s.gif'>";
				}
			}
		}
		sHtml += "<a id=treeimg_"+node.treeidx+"_"+node.idx+" "+sImgScript+" style=\"text-decoration:none;\">";
		sHtml += sImgHtml;
		sHtml += "</a>";
		return sHtml;
	}

	function JLTreeCtrl_getNodeHtml(node)
	{
		if (node == null)
			return;
		var sImgPath = JLTreeCtrl_getImgPath(node);
		var sHtml = "";
		sHtml += "<table style=\"FONT-FAMILY:; FONT-SIZE: 9pt\" height=16 id=\"treenode_"+node.treeidx+"_"+node.idx+"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"cursor: hand;\">";
		sHtml += "<tr height=16>";
		var sTmp = JLTreeCtrl_printNodeEx(node);
		if (sTmp != null)
			sHtml += sTmp;
		sHtml += "<td align=top>";
		sHtml += JLTreeCtrl_getNodeIconHtml(node);
		sHtml += "</td>";

		sIcon = node.icon;
		sHtml += "<td align=left><img src=\""+sIcon+"\"></td>";
		sHtml += "<td align=left width=\"2\"><img alt border=0 height=4 src=\"images/trans.gif\" width=2></td>";
		sHtml += "<td>";
//sunhee 2003.09.01 OnDblClick  
      sHtml += "<table border='0' cellPadding='0' cellSpacing='0' OnDblClick=\"javascript:JLTreeCtrl_onmousedblclick("+node.treeidx+","+node.idx+","+"'Viewer.jsp'"+"); \""+  "onmousedown=\"javascript:JLTreeCtrl_onmousedown("+node.treeidx+","+node.idx+"); "+node.action+"\">";
		sHtml += "<tr height=13>";
		// sHtml += "<td align=left width=\"1\"><img alt border=0 height=1 src=\"images/trans.gif\" width=1></td>";

		var sInnerHtml = "<img alt border=0 height=3 src='"+sImgPath+"/"+"trans.gif' width=3><font color=\"#000000\" id='fnt_"+node.treeidx+"_"+node.idx+"' style=\"FONT: 9pt\">"+node.name+"</font><img alt border=0 height=3 src='"+sImgPath+"/"+"trans.gif' width=4>";
		var nCellWidth = _getHtmlWidth(sInnerHtml);
		sHtml += "<td id=td_"+node.treeidx+"_"+node.idx+" bgcolor='#ffffff'><img alt border=0 height=2 src=images/trans.gif width="+nCellWidth+"><br>";
		// sHtml += "<a href=\"javascript:"+node.action+"\" onfocus='this.blur()'  title='"+node.name+"' style=\"text-decoration:none;\">";
		sHtml += sInnerHtml;
		// sHtml += "</a></td></tr></table>";
		sHtml += "</td></tr></table>";

		sHtml += "</td>";
		sHtml += "</tr>";
		sHtml += "</table>";

		return sHtml;
	}

	function JLTreeCtrl_printNode(node)
	{
		if (node == null)
			return;
		var sHtml = JLTreeCtrl_getNodeHtmlEx(node);
		var oParent = node.parent;
		if (oParent != null)
		{
			var nodeid = "treenode_"+node.treeidx+"_"+node.idx;
			var elNode = document.getElementById(nodeid);
			if (elNode != null)
			{
				var elSpanid = "treespan_"+node.treeidx+"_"+node.idx;
				var elSpan = document.getElementById(elSpanid);
				if (elSpan != null)
					elSpan.outerHTML = "";
				elNode.outerHTML = sHtml;
			}
			else
			{
				var parentSpanid = "treespan_"+oParent.treeidx+"_"+oParent.idx;
				var elParent = document.getElementById(parentSpanid);
				if (elParent != null)
				{
					if (oParent.child[0] == node)
						elParent.innerHTML = sHtml;
					else
						elParent.innerHTML += sHtml;
				}
			}
		}
		else
			document.write(sHtml);
		var nCount = Number(node.childcount);
		for (var i=0;i<nCount;i++)
		{
			JLTreeCtrl_printNode(node.child[i]);
		}
	}

	function JLTreeCtrl_printNodeEx(node)
	{
		if (node == null)
			return null;
		var oParent = node.parent;
		if (oParent == null)
			return null;

		var sImgPath = JLTreeCtrl_getImgPath(node);
		var sHtml = "";
		sHtml = JLTreeCtrl_printNodeEx(oParent);
		if (sHtml == null)
			sHtml = "";

		if (oParent.next != null)
			sHtml += "<td align=top><img alt border='0' src='"+sImgPath+"/"+"icon_vert.gif'></td>";
		else
			sHtml += "<td align=top><img alt border='0' src='"+sImgPath+"/"+"trans.gif' width=16 height=16></td>";
		return sHtml;
	}


</script>

<script language="javascript">
	var isNav4, isNav6, isIE4;
	function setBrowser()
	{
	    if (navigator.appVersion.charAt(0) == "4")
	    {
	        if (navigator.appName.indexOf("Explorer") >= 0)
	        {
	            isIE4 = true;
	        }
	        else
	        {
	            isNav4 = true;
	        }
	    }
	    else if (navigator.appVersion.charAt(0) > "4")
	    {
	        isNav6 = true;
	    }
	}
	function getStyleBySelector( selector )
	{
	    if (!isNav6)
	    {
	        return null;
	    }
	    var sheetList = document.styleSheets;
	    var ruleList;
	    var i, j;
	    /* look through stylesheets in reverse order that
	       they appear in the document */
	    for (i=sheetList.length-1; i >= 0; i--)
	    {
	        ruleList = sheetList[i].cssRules;
	        for (j=0; j<ruleList.length; j++)
	        {
	            if (ruleList[j].type == CSSRule.STYLE_RULE &&
	                ruleList[j].selectorText == selector)
	            {
	                return ruleList[j].style;
	            }   
	        }
	    }
	    return null;
	}
	function getIdProperty( id, property )
	{
	    if (isNav6)
	    {
	        var styleObject = document.getElementById( id );
	        if (styleObject != null)
	        {
	            styleObject = styleObject.style;
	            if (styleObject[property])
	            {
	                return styleObject[ property ];
	            }
	        }
	        styleObject = getStyleBySelector( "#" + id );
	        return (styleObject != null) ?
	            styleObject[property] :
	            null;
	    }
	    else if (isNav4)
	    {
	        return document[id][property];
	    }
	    else
	    {
	        return document.all[id].style[property];
	    }
	}
	function setIdProperty( id, property, value )
	{
	    if (isNav6)
	    {
	        var styleObject = document.getElementById( id );
	        if (styleObject != null)
	        {
	            styleObject = styleObject.style;
	            styleObject[ property ] = value;
	        }
	        
	    }
	    else if (isNav4)
	    {
	        document[id][property] = value;
	    }
	    else if (isIE4)
	    {
			// document.all[id].style[property] = value;
			var obj = document.all[id];
			if (obj != null)
				obj.style[property] = value;
	    }
	}
	function generic_move( id, xValue, yValue, additive )
	{
	    var left = getIdProperty(id, "left");
	    var top = getIdProperty(id, "top");
	    var leftMatch, topMatch;
	
	    if (isNav4)
	    {
	        leftMatch = new Array( 0, left, "");
	        topMatch = new Array( 0, top, "");
	    }
	    else if (isNav6 || isIE4 )
	    {
	        var splitexp = /([-0-9.]+)(\w+)/;
	        leftMatch = splitexp.exec( left );
	        topMatch = splitexp.exec( top );
	        if (leftMatch == null || topMatch == null)
	        {
	            leftMatch = new Array(0, 0, "px");
	            topMatch = new Array(0, 0, "px");
	        }
	    }
	    left = ((additive) ? parseFloat( leftMatch[1] ) : 0) + xValue;
	    top = ((additive) ? parseFloat( topMatch[1] ) : 0) + yValue;
	    setIdProperty( id, "left", left + leftMatch[2] );
	    setIdProperty( id, "top", top + topMatch[2] );
	}
	function moveTo( id, x, y )
	{
	    generic_move( id, x, y, false );
	}
	function moveBy( id, x, y)
	{
	    generic_move( id, x, y, true );
	}
	function hex( n )
	{
	    var hexdigits = "0123456789abcdef";
	    return ( hexdigits.charAt(n >> 4) + hexdigits.charAt(n & 0x0f) );
	}
	function getBackgroundColor( id )
	{
	    var color;
	
	    if (isNav4)
	    {
	        color = document[id].bgColor;
	    }
	    else if (isNav6)
	    {
	        var parseExp = /rgb.(\d+),(\d+),(\d+)./;
	        var rgbvals;
	        color = getIdProperty( id, "backgroundColor" );
	        if (color)
	        {
	            rgbvals = parseExp.exec( color );
	            if (rgbvals)
	            {
	                color = "#" + hex( rgbvals[1] ) + hex( rgbvals[2] ) +
	                    hex( rgbvals[3] );
	            }
	        }
	        return color;
	    }
	    else if (isIE4)
	    {
	        return document.all[id].backgroundColor;
	    }
	    return "";
	}
	function getDocument( divName )
	{
	    var doc;
	
	    if (isNav4)
	    {
	        doc = window.document[divName].document;
	    }
	    else if (isNav6)
	    {
	        doc = document;
	    }
	    else if (isIE4)
	    {
	        doc = document;
	    }
	    return doc;
	}
	function swapDiv( divNum, expanding )
	{
	    if (expanding)
	    {
	        setIdProperty("s" + divNum, "display", "none");
	        setIdProperty("e" + divNum, "display", "inline");
	    }
	    else
	    {
	        setIdProperty("e" + divNum, "display", "none");
	        setIdProperty("s" + divNum, "display", "inline");
	    }
	}
	
	function showMenu( divNum )
	{
	    if (getIdProperty( "s" + divNum, "display") != "block" )
	    {
	        setIdProperty("s" + divNum, "display", "block");
	        document.images["i" + divNum].src = "minus.gif";
	    }
	    else
	    {
	        setIdProperty("s" + divNum, "display", "none");
	        document.images["i" + divNum].src = "plus.gif";
	    }
	}
	setBrowser();
	var m_spanstate = new Array(1024);
	/*
	0 : non sibling, has children (non leaf), shrink : icon_plus_ns.gif
	1 : sibling, has children (non leaf), shrink : icon_plus_s.gif
	2 : non sibling, has children (non leaf),root, shrink : icon_plus_r.gif
	10 : non sibling, has children (non leaf), expand : icon_minus_ns.gif
	11 : sibling, has children (non leaf), expand : icon_minus_s.gif
	12 : non sibling, has children (non leaf),root, expand : icon_minus_r.gif
	*/
	function onExpandNode(treename,span,idx,type)
	{
		if (m_spanstate[span] != true)
		{
			swapDiv(span,true);
			m_spanstate[span] = true;
			var obj = document.getElementById(treename+"_nodeimg_"+idx);
			if (obj != null)
			{
				switch (type)
				{
				case 10:
				case 0:
					obj.src = "./images/icon_minus_ns.gif";
					break;
				case 11:
				case 1:
					obj.src = "./images/icon_minus_s.gif";
					break;
				case 12:
				case 2:
					obj.src = "./images/icon_minus_r.gif";
					break;
				}
			}
		}
		else
		{
			swapDiv(span,false);
			m_spanstate[span] = false;
			var obj = document.getElementById(treename+"_nodeimg_"+idx);
			if (obj != null)
			{
				switch (type)
				{
				case 10:
				case 0:
					obj.src = "./images/icon_plus_ns.gif";
					break;
				case 11:
				case 1:
					obj.src = "./images/icon_plus_s.gif";
					break;
				case 12:
				case 2:
					obj.src = "./images/icon_plus_r.gif";
					break;
				}
			}
		}
	}
</script>


<!-- JSTreeCtrl.jsp : end -->
