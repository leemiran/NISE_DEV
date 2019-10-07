<%@page import="m2soft.rdsystem.server.log.*" %>
<%!
public class JLCurUserTree extends JLTreeCtrl
{
   private Vector nodeData = null;
	JLCurUserTree(javax.servlet.jsp.JspWriter out,Vector v)
	{
		super(out);
		setClassName("JLCurUserTree");
		setNoteData(v);
	}
	
	void setNoteData(Vector v){
	   this.nodeData = v;
	}
	Vector getSegment(String sParent)
	{
	   if(nodeData != null){
	     return nodeData;
	   }
		Vector vec = new Vector();
		if (sParent.equalsIgnoreCase("0"))
		{
			{
				Vector vRcd = new Vector();
				vRcd.addElement("192.168.1.22");
				vRcd.addElement("0");
				vRcd.addElement("1");
				vec.addElement(vRcd);
			}
			{
				Vector vRcd = new Vector();
				vRcd.addElement("select * from schedule");
				vRcd.addElement("192.168.1.22");
				vRcd.addElement("0");
				vec.addElement(vRcd);
			}
			{
				Vector vRcd = new Vector();
				vRcd.addElement("c:/a.mrd");
				vRcd.addElement("192.168.1.22");
				vRcd.addElement("0");
				vec.addElement(vRcd);
			}

		}
		return vec;
	}

	// getSegment는 시스템에서 db에 있는 내용을 Vector로 가지고 오는 기능을 합니다.
	// Vector를 tree형식으로 보여주기 위해서는 
	// 아래의 java script를 생성해 주어야 합니다.
	/*
	<script language="javascript">
	function JLTreeCtrl_printTree_Doc()
	{
		var vTree = new Array(
	"0","Seoul","Seoul","./images/log16.gif","./images/log16.gif",0,"onclickDoc('Seoul')",1,
	"0","대구","대구","./images/log16.gif","./images/log16.gif",0,"onclickDoc('대구')",0,
	"0","광주","광주","./images/log16.gif","./images/log16.gif",0,"onclickDoc('광주')",0,
	"0","Pusan","Pusan","./images/log16.gif","./images/log16.gif",0,"onclickDoc('Pusan')",1
		);
	var treeidx=root2.treeidx;
	JLTreeCtrl_initTree(root2,vTree,treeidx);
	JLTreeCtrl_setClass(0,"JLDocTree");
	JLTreeCtrl_printTree(root2);
	}
	JLTreeCtrl_printTree_Doc();
	</script>
	
	위의 javascript에서 아래의 부분은 개발자가 원하는 아이콘 이미지, 혹은 treenode가 선택이 되었
	을때 호출되는 javascript function을 기입할 수 있습니다.
	이때 하나의 tree node 는 아래의 한줄에 해당하고, 이 한줄을 만들기 위해서
	개발자가 정의한,getNodeData를 호출합니다. getNodeData에서는 아래의 한줄을 String으로 만들어
	return하면 됩니다.
	예를 들어, Node가 4개가 있다면, getNodeData는 4번 호출됩니다.
	"0","Seoul","Seoul","./images/log16.gif","./images/log16.gif",0,"onclickDoc('Seoul')",1,
	"0","대구","대구","./images/log16.gif","./images/log16.gif",0,"onclickDoc('대구')",0,
	"0","광주","광주","./images/log16.gif","./images/log16.gif",0,"onclickDoc('광주')",0,
	"0","Pusan","Pusan","./images/log16.gif","./images/log16.gif",0,"onclickDoc('Pusan')",1
	*/
	/*
	Root Node에 3개의 child node (a,b,c)가 있고, a node에 child가 x, y가 있다면,
	Root
	  A
	    X
	    Y
	  B
	  C
	이것을 표현하기 위해서는 
	0,1,"A",...
	0,2,"B",...
	0,3,"C",...
	1,10,"X",...
	1,11,"Y",...
	
	순서는 동일한 Level에서는 data가 나오는 순서로, tree node가 구성이 되며,
	child node는 parent가 나오기 다음과 같이 나와도 상관이 없습니다.

	1,11,"Y",...
	1,10,"X",...
	0,1,"A",...
	0,2,"B",...
	0,3,"C",...
	
	Root
	  A
	    Y
	    X
	  B
	  C
	  
	Expand (Shrink) 설정 기능
	개발자가 , 특정 node가 expand되게 보이려면, 해당 node의 expand 필드를 1로 설정하면 됩니다.
	expand - Minus아이콘: 1, shrink - Plus 아이콘 : 0
	                                                            V ()
	"0","광주","광주","./images/log16.gif","./images/log16.gif",1,"onclickDoc('광주')",0,
	*/
	
	// format : parentid, id, text, icon, iconexpand, bexpand, sAction, nChildcount
	// bExpand : 1 - expand, 0 shrink
	String getNodeData(Vector vNode)
	{
		if (vNode == null)
			return "";
		String sHtml = "";
		String parentid = (String)vNode.elementAt(1);
		parentid.trim();
		String id = (String)vNode.elementAt(0);
		id.trim();
		String sName = (String)vNode.elementAt(0);
		sName.trim();
		String sAction = "onclickDoc(m_obj,'"+id+"')";
		String sChildCount = (String)vNode.elementAt(2);
		sChildCount.trim();
		int nExpand = 0;
		sHtml += "\""+parentid+"\",\""+id+"\",\""+sName+"\",\"./images/customer.gif\",\"./images/log16.gif\","+nExpand+",\""+this.insertDQuoteEsc(sAction)+"\","+sChildCount+"";
		return sHtml;
	}
}

%>
