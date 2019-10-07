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

	// getSegment�� �ý��ۿ��� db�� �ִ� ������ Vector�� ������ ���� ����� �մϴ�.
	// Vector�� tree�������� �����ֱ� ���ؼ��� 
	// �Ʒ��� java script�� ������ �־�� �մϴ�.
	/*
	<script language="javascript">
	function JLTreeCtrl_printTree_Doc()
	{
		var vTree = new Array(
	"0","Seoul","Seoul","./images/log16.gif","./images/log16.gif",0,"onclickDoc('Seoul')",1,
	"0","�뱸","�뱸","./images/log16.gif","./images/log16.gif",0,"onclickDoc('�뱸')",0,
	"0","����","����","./images/log16.gif","./images/log16.gif",0,"onclickDoc('����')",0,
	"0","Pusan","Pusan","./images/log16.gif","./images/log16.gif",0,"onclickDoc('Pusan')",1
		);
	var treeidx=root2.treeidx;
	JLTreeCtrl_initTree(root2,vTree,treeidx);
	JLTreeCtrl_setClass(0,"JLDocTree");
	JLTreeCtrl_printTree(root2);
	}
	JLTreeCtrl_printTree_Doc();
	</script>
	
	���� javascript���� �Ʒ��� �κ��� �����ڰ� ���ϴ� ������ �̹���, Ȥ�� treenode�� ������ �Ǿ�
	���� ȣ��Ǵ� javascript function�� ������ �� �ֽ��ϴ�.
	�̶� �ϳ��� tree node �� �Ʒ��� ���ٿ� �ش��ϰ�, �� ������ ����� ���ؼ�
	�����ڰ� ������,getNodeData�� ȣ���մϴ�. getNodeData������ �Ʒ��� ������ String���� �����
	return�ϸ� �˴ϴ�.
	���� ���, Node�� 4���� �ִٸ�, getNodeData�� 4�� ȣ��˴ϴ�.
	"0","Seoul","Seoul","./images/log16.gif","./images/log16.gif",0,"onclickDoc('Seoul')",1,
	"0","�뱸","�뱸","./images/log16.gif","./images/log16.gif",0,"onclickDoc('�뱸')",0,
	"0","����","����","./images/log16.gif","./images/log16.gif",0,"onclickDoc('����')",0,
	"0","Pusan","Pusan","./images/log16.gif","./images/log16.gif",0,"onclickDoc('Pusan')",1
	*/
	/*
	Root Node�� 3���� child node (a,b,c)�� �ְ�, a node�� child�� x, y�� �ִٸ�,
	Root
	  A
	    X
	    Y
	  B
	  C
	�̰��� ǥ���ϱ� ���ؼ��� 
	0,1,"A",...
	0,2,"B",...
	0,3,"C",...
	1,10,"X",...
	1,11,"Y",...
	
	������ ������ Level������ data�� ������ ������, tree node�� ������ �Ǹ�,
	child node�� parent�� ������ ������ ���� ���͵� ����� �����ϴ�.

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
	  
	Expand (Shrink) ���� ���
	�����ڰ� , Ư�� node�� expand�ǰ� ���̷���, �ش� node�� expand �ʵ带 1�� �����ϸ� �˴ϴ�.
	expand - Minus������: 1, shrink - Plus ������ : 0
	                                                            V ()
	"0","����","����","./images/log16.gif","./images/log16.gif",1,"onclickDoc('����')",0,
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
