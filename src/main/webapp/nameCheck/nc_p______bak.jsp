<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ page language="java" import="common.NameCheck"%>
<jsp:useBean id="NC" class="common.NameCheck" scope="request"/>
<%

String SITECODE = "Q590"; //�����򿡼� �߱޹��� ���̵�� �������ּ���
String SITEPW = "63218325"; 	  //�����򿡼� �߱޹��� �н������ �������ּ���

//nc.jsp ���� ������ ���ǰ��� Ȯ���Ѵ�. 
HttpSession s = request.getSession(true);
if(!s.getValue("NmChkSec").equals("98u9iuhuyg87"))
{
	// ������ �ȵǴ� ��� �ʿ��Ͻ� �������� ó�����ּ���.
}

//s.invalidate();

//nc.jsp ���� �Ķ���ͷ� ���� ���޹޴´�.
String sJumin1 = request.getParameter("jumin1");	
String sJumin2 = request.getParameter("jumin2");
String p_nGubun = request.getParameter("p_nGubun");
String sJumin = sJumin1 + sJumin2;

String birthDay = "";

String b1 = sJumin1.substring(0,1);
int b2 = Integer.parseInt(b1);
if(b2 > 0){
	birthDay = "19"+sJumin1;
}else{
	birthDay = "20"+sJumin1;
}
String sex = "";
String s1 = sJumin2.substring(0,1);
int s2 = Integer.parseInt(s1);

if(s2 == 1 || s2 == 3){
	sex = "M";
}else if(s2 == 2 || s2 == 4){
	sex = "F";
}

System.out.println("############################################"+b1);
System.out.println("############################################"+birthDay);
System.out.println("############################################"+sex);
System.out.println("############################################"+p_nGubun);
//String sName = request.getParameter("name");
//�ѱ� ���ڵ� �����Ͽ� �Ѱ��ֽô� �̸��� ������� �Ʒ��� �����ؼ� euc-kr �� �̸��� �Ѱ��ּ���
String sName = new String(request.getParameter("name").getBytes("8859_1"), "KSC5601");

//�ֹι�ȣ�� �̸� ����Ʈ���̵� �н������� ���� ������ �Ѱ��ְ� Rtn �� ���ϰ��� �޴´�.
if((!sJumin.equals("")) && (!sName.equals("")))
{
	String Rtn = "";
	NC.setChkName(sName);				// 
	Rtn = NC.setJumin(sJumin+SITEPW);
	
	//����ó���� ���
	if(Rtn.equals("0")) 
	{
		NC.setSiteCode(SITECODE);
		NC.setTimeOut(30000);
		Rtn = NC.getRtn().trim(); 
	}				
	
				
				//Rtn �������� ���� �Ʒ� �����ϼż� ó�����ּ���.(������� �ڼ��� ������ �����ڵ�.txt ������ ������ �ּ���~)
								//Rtn :	1 �̸� --> �Ǹ����� ���� : XXX.jsp �� ������ �̵�. 
								//			2 �̸� --> �Ǹ����� ���� : �ֹΰ� �̸��� ��ġ���� ����. ����ڰ� ���� www.namecheck.co.kr �� �����Ͽ� ��� or 1600-1522 �ݼ��ͷ� ������û.
								//									�Ʒ��� ���� �����򿡼� ������ �ڹٽ�ũ��Ʈ �̿��ϼŵ� �˴ϴ�.		
								//									���������� ��ϵ� ������ӿ��� ������ ������ �ѱ۱����� Ȯ���� �ּ���. 
								//									�ѱ��� euc-kr�� �Ѱ��ּž� �մϴ�.
								//			3 �̸� --> ������ �ش��ڷ� ���� : ����ڰ� ���� www.namecheck.co.kr �� �����Ͽ� ��� or 1600-1522 �ݼ��ͷ� ������û.
								//									�Ʒ��� ���� �����򿡼� ������ �ڹٽ�ũ��Ʈ �̿��ϼŵ� �˴ϴ�.
								//			5 �̸� --> üũ�����(�ֹι�ȣ������Ģ�� ��߳� ���: ���Ƿ� ������ ���Դϴ�.)
								//			50�̸� --> ũ������ũ�� ���ǵ������� ���� �������� : ���� ���ǵ������� ���� �� �Ǹ����� ��õ�.
								//								�Ʒ��� ���� �����򿡼� ������ �ڹٽ�ũ��Ʈ �̿��ϼŵ� �˴ϴ�.
								//			�׹ۿ� --> 30����, 60���� : ��ſ��� ip: 203.234.219.72 port: 81~85(5��) ��ȭ�� ���� ���µ�����ش�. 
								//								(������� �ڼ��� ������ �����ڵ�.txt ������ ������ �ּ���~) 

		//Rtn = "1";
								
        if (Rtn.equals("1")){
        //out.println(Rtn);
        	if(p_nGubun.equals("join")){
        	
%>
			<script language='javascript'>     
		      	//alert("��������!! ^^");    
		      	
			function goParent(){
			    f = document.form1;
		
			    f.action = "/usr/mem/memJoinStep02.do";
			    f.target = opener.window.name;
			   //dbclick ���� 
			    document.charset="utf-8";
			    f.submit();
			    window.close();
			}
		
		
		
			      	      
		     </script>
		
					<!-- ������ ó���� �ϽǶ����� �Ʒ��Ͱ��� ó���ϼŵ� �˴ϴ�. �̵��� �������� �����ؼ� ����Ͻø� �˴ϴ�. -->
					<html>
						<head>
						</head>
						<body onLoad="goParent()">
							<form name="form1" method="post" accept-charset="utf-8" action="/usr/mem/memJoinStep02.do">
								<!--<input type="hidden" name="jumin1" value="<%=sJumin1%>">
								<input type="hidden" name="jumin2" value="<%=sJumin2%>">
								--><input type="hidden" name="realName" value="<%=sName%>">
								<input type="hidden" name="virtualNo" value="NameCheck">
								<input type="hidden" name="birthDate" value="<%= birthDay%>">
								<input type="hidden" name="sex" value="<%= sex %>">
							</form>
						</body>
					</html>
			
						
<%
            }else if(p_nGubun.equals("pw_check")){
%>
			<script language='javascript'>     
		      	//alert("��������!! ^^");    
		      	
			function goParent(){
			    f = document.form1;
		
			    f.action = "/usr/mem/memJoinStep01112.do";
			    f.target = opener.window.name;
			   //dbclick ���� 
			    document.charset="utf-8";
			    f.submit();
			    window.close();
			}
		
		
		
			      	      
		     </script>
		
					<!-- ������ ó���� �ϽǶ����� �Ʒ��Ͱ��� ó���ϼŵ� �˴ϴ�. �̵��� �������� �����ؼ� ����Ͻø� �˴ϴ�. -->
					<html>
						<head>
						</head>
						<body onLoad="goParent()">
							<form name="form1" method="post" accept-charset="utf-8" action="/usr/mem/memJoinStep02.do">
								<!--<input type="hidden" name="jumin1" value="<%=sJumin1%>">
								<input type="hidden" name="jumin2" value="<%=sJumin2%>">
								--><input type="hidden" name="realName" value="<%=sName%>">
								<input type="hidden" name="virtualNo" value="NameCheck">
								<input type="hidden" name="birthDate" value="<%= birthDay%>">
								<input type="hidden" name="sex" value="<%= sex %>">
							</form>
						</body>
					</html>
<%
            }
		}else if (Rtn.equals("2")){
		//���ϰ� 2�� ������� ���, www.namecheck.co.kr �� �Ǹ���Ȯ�� �Ǵ� 02-1600-1522 �ݼ��ͷ� �����ֽñ� �ٶ��ϴ�.		
%>
            <script language="javascript">
               history.go(-1); 
               var URL ="http://www.creditbank.co.kr/its/its.cb?m=namecheckMismatch"; 
               var status = "toolbar=no,directories=no,scrollbars=no,resizable=no,status=no,menubar=no, width= 640, height= 480, top=0,left=20"; 
               window.open(URL,"",status); 
            </script> 
<%
		}else if (Rtn.equals("3")){
		//���ϰ� 3�� ������� ���, www.namecheck.co.kr �� �Ǹ���Ȯ�� �Ǵ� 02-1600-1522 �ݼ��ͷ� �����ֽñ� �ٶ��ϴ�.   			
%>
            <script language="javascript">
               history.go(-1); 
               var URL ="http://www.creditbank.co.kr/its/its.cb?m=namecheckMismatch"; 
               var status = "toolbar=no,directories=no,scrollbars=no,resizable=no,status=no,menubar=no, width= 640, height= 480, top=0,left=20"; 
               window.open(URL,"",status); 
            </script> 
<%
		}else if (Rtn.equals("50")){
		//���ϰ� 50 ���ǵ������� ���� �������� ���, www.creditbank.co.kr ���� ���ǵ����������� �� ��õ� ���ֽø� �˴ϴ�. 
		// �Ǵ� 02-1600-1533 �ݼ��ͷι����ּ���.  	
			
%>	
            <script language="javascript">
               history.go(-1); 
               var URL ="http://www.creditbank.co.kr/its/itsProtect.cb?m=namecheckProtected"; 
               var status = "toolbar=no,directories=no,scrollbars=no,resizable=no,status=no,menubar=no, width= 640, height= 480, top=0,left=20"; 
               window.open(URL,"",status); 
            </script> 

<%
		}else{
		//������ ������ ���� �����ڵ�.txt �� �����Ͽ� ���ϰ��� Ȯ���� �ּ���~	
%>
			<script language='javascript'>
				alert("������ ���� �Ͽ����ϴ�.�����ڵ�:[<%=Rtn%>]");
				//history.go(-1);
				function goParent1(){
				    f = document.form1;

				    f.action = "/usr/mem/memJoinStep03.do";
				    f.target = opener.window.name;
				   //dbclick ���� 
				    document.charset="utf-8";
				    f.submit();
				    window.close();
				}
			</script>	
			
			<html>
				<head>
				</head>
				<body onLoad="goParent1()">
					<form name="form1" method="post" accept-charset="utf-8" action="/usr/mem/memJoinStep03.do">
						<!--<input type="hidden" name="jumin1" value="<%=sJumin1%>">
						<input type="hidden" name="jumin2" value="<%=sJumin2%>">
						--><input type="hidden" name="realName" value="<%=sName%>">
						<input type="hidden" name="virtualNo" value="NameCheck">
						<input type="hidden" name="birthDate" value="<%= birthDay%>">
						<input type="hidden" name="sex" value="<%= sex %>">
					</form>
				</body>
			</html>		
<%
		}
}else{
	out.println("�����̳� �ֹι�ȣ�� Ȯ���ϼ���.");
}
%>
