<%@ page contentType = "text/html;charset=euc-kr" %>
<%@ page errorPage = "/code500.jsp" %>
<%@ include file = "write.jsp" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="java.util.*" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="egovframework.usr.subj.service.*" %>

<%
//
// �� �������� ��ũâ + ������ ����� note_url �����Դϴ�.
//
//============================================================================================================
// ����1 : �� �������� ret_url�� �����Ͻø� �ȵ˴ϴ�. ret_url�� note_url�� �����帮�� ������� ���̰� �ֽ��ϴ�.
//            �� �������� �ݵ�� note_url ���� �����ϼž� �մϴ�. (* ret_url �� �޴����� ����� ���� �����ϼž� �մϴ�)
//
// ����2 : note_url �������� ��� hashdata ������ �ʼ� �Դϴ�. ���� ���������ڿ��� Ȯ�ε� mertkey���� �Ʒ� 
//           ���ó��뿡 �� ������ �ּž� �մϴ�.
//
// ����3 : note_url�� LG������ ���������� ȣ���ϴ� �������� OK ����̿��� �޽����� ���ŵ� ��� ������ۿ� ������
//           �߻��ϰ� �˴ϴ�. html �±׳� �ڹٽ�ũ��Ʈ �ڵ带 ����ϴ� ��� ������ ������ �� �����ϴ�.
//
// ����4 : ���������� data�� ó���� ��쿡�� LG�����޿��� OK ������ ���� ���� ���� ��������� �ߺ��ؼ� ���� �� 
//           �����Ƿ� ������ ó���� ����Ǿ�� �մϴ�
//
// ����5 : ������ ���� ��� ó���� ������ ��쿡 ���� write_success(),write_failure(),write_hasherr()���� ���� ��ƾ��
//           �߰� �Ͻø� �˴ϴ�.
//
// ����6 : note_url �������� ȣ���� �ȵǴ� ��� ���������� ������(test/service)���� ������� -> ������������ -> 
//			�ý��ۿ��������� ������ ������ �Ǿ� �ִ��� Ȯ�ιٶ��ϴ�.
//============================================================================================================
//
// [ note_url �������� ������ �ִ� ��� ]
//
// ���������� ������-> ����������ȸ -> ��ü�ŷ�������ȸ -> ���۽��г�����ȸ ���� note_url�� LG���������� ������ 
// ������ Ȯ�� �ϽǼ� �ֽ��ϴ�.
// 
// - ���� ���������� URL : http://pgweb.dacom.net
// - �׽�Ʈ ���������� URL : http://pgweb.dacom.net/tmert
//
	WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	UserSubjectService userSubjectService = (UserSubjectService)ctx.getBean("userSubjectService");
	
	
	request.setCharacterEncoding("euc-kr");

	String respcode="";			// �����ڵ�: 0000(����) �׿� ����
	String respmsg="";			// ����޼���
	String hashdata="";			// �ؽ���
	String transaction="";		// �������� �ο��� �ŷ���ȣ
	String mid="";				// �������̵� 
	String oid="";				// �ֹ���ȣ
	String amount="";			// �ŷ��ݾ�
	String currency="";			// ��ȭ�ڵ�('410':��ȭ, '840':�޷�)
	String paytype="";			// ���������ڵ�
	String msgtype="";			// �ŷ������� ���� �������� ������ �ڵ�
	String paydate="";			// �ŷ��Ͻ�(�����Ͻ�/��ü�Ͻ�)
	String buyer="";			// �����ڸ�
	String productinfo="";		// ��ǰ����
	String buyerssn="";			// �������ֹε�Ϲ�ȣ
	String buyerid="";			// ������ID
	String buyeraddress="";		// �������ּ�
	String buyerphone="";		// ��������ȭ��ȣ
	String buyeremail="";		// �������̸����ּ�
	String receiver="";			// �����θ�
	String receiverphone="";	// ��������ȭ��ȣ
	String deliveryinfo="";		// �������
	String producttype="";		// ��ǰ����
	String productcode="";		// ��ǰ�ڵ�
	String financecode="";		// ��������ڵ�(ī������/�����ڵ�/������ڵ�)
	String financename="";		// ��������̸�(ī���̸�/�����̸�/������̸�)

	String authnumber="";		// ���ι�ȣ(�ſ�ī��)
	String cardnumber="";		// ī���ȣ(�ſ�ī��)
	String cardexp="";			// ��ȿ�Ⱓ(�ſ�ī��)
	String cardperiod="";		// �Һΰ�����(�ſ�ī��)	
	String nointerestflag="";	//�������Һο���(�ſ�ī��) - '1'�̸� �������Һ� '0'�̸� �Ϲ��Һ�
	String transamount="";		// ȯ������ݾ�(�ſ�ī��)
	String exchangerate="";		// ȯ��(�ſ�ī��)

	String pid="";				// ������/�޴��������� �ֹε�Ϲ�ȣ(������ü/�޴���) 
	String accountowner="";		// ���¼������̸�(������ü) 
	String accountnumber="";	// ���¹�ȣ(������ü, �������Ա�) 

	String telno="";			// �޴�����ȣ(�޴���)

    String payer="";            // �Ա���(�������Ա�)
    String cflag="";            // �������Ա� �÷���(�������Ա�) - 'R':�����Ҵ�, 'I':�Ա�, 'C':�Ա����
    String tamount="";          // �Ա��Ѿ�(�������Ա�)
    String camount="";          // ���Աݾ�(�������Ա�)
    String bankdate="";         // �ԱݶǴ�����Ͻ�(�������Ա�)
	String seqno="";			// �Աݼ���(�������Ա�)
	String receiptnumber="";	// ���ݿ����� ���ι�ȣ
	String receiptkind="";		// ���ݿ����� ���� - 0: �ҵ������ , 1: ����������0
	String receiptself="";		//
	String useescrow="";		// ���� ����ũ�� ���� ���� - Y : ����ũ�� ����, N : ����ũ�� ������
	String hashdata2="";		// �ؽ���2
    boolean resp = false;

    
	respcode = request.getParameter("respcode");
	respmsg = request.getParameter("respmsg");
	hashdata = request.getParameter("hashdata");
	transaction = request.getParameter("transaction");
	mid = request.getParameter("mid");
	oid = request.getParameter("oid");
	amount = request.getParameter("amount");
	currency = request.getParameter("currency");
	paytype = request.getParameter("paytype");
	msgtype = request.getParameter("msgtype");
	paydate = request.getParameter("paydate");
	buyer = request.getParameter("buyer");
	productinfo = request.getParameter("productinfo");
	buyerssn = request.getParameter("buyerssn");
	buyerid = request.getParameter("buyerid");
	buyeraddress = request.getParameter("buyeraddress");
	buyerphone = request.getParameter("buyerphone");
	buyeremail = request.getParameter("buyeremail");
	receiver = request.getParameter("receiver");
	receiverphone = request.getParameter("receiverphone");
	deliveryinfo = request.getParameter("deliveryinfo");
	producttype = request.getParameter("producttype");
	productcode = request.getParameter("productcode");
	financecode = request.getParameter("financecode");
	financename = request.getParameter("financename");
	authnumber = request.getParameter("authnumber");
	cardnumber = request.getParameter("cardnumber");
	cardexp = request.getParameter("cardexp");
	cardperiod = request.getParameter("cardperiod");
	nointerestflag = request.getParameter("nointerestflag");
	transamount = request.getParameter("transamount");
	exchangerate = request.getParameter("exchangerate");
	pid = request.getParameter("pid");
	accountnumber = request.getParameter("accountnumber");
	accountowner = request.getParameter("accountowner");
	telno = request.getParameter("telno");
	payer = request.getParameter("payer");
	cflag = request.getParameter("cflag");
	tamount = request.getParameter("tamount");
	camount = request.getParameter("camount");
	bankdate = request.getParameter("bankdate");
	seqno = request.getParameter("seqno");
	receiptnumber = request.getParameter("receiptnumber");
	receiptkind = request.getParameter("receiptkind");
	receiptself = request.getParameter("receiptself");
	useescrow = request.getParameter("useescrow");
	hashdata2 = request.getParameter("hashdata2");

	//���������� ����ϴ� �Ķ����
	String p_subj = request.getParameter("p_subj") + "";
	String p_year = request.getParameter("p_year") + "";
	String p_subjseq = request.getParameter("p_subjseq") + "";
	String p_lec_sel_no = java.net.URLDecoder.decode(request.getParameter("p_lec_sel_no") + "", "UTF-8");



	String p_hrdc2 = request.getParameter("p_hrdc2") + "";
	String p_post1 = request.getParameter("p_post1") + "";
	String p_post2 = request.getParameter("p_post2") + "";
	String p_pay_sel = request.getParameter("p_pay_sel") + "";
	String p_enterance_dt = request.getParameter("p_enterance_dt") + "";
	String p_comp = request.getParameter("p_comp") + "";
	String p_is_attend = request.getParameter("p_is_attend") + "";

	
	System.out.println("### p_lec_sel_no : " + p_lec_sel_no);
	System.out.println("### p_pay_sel : " + p_pay_sel);
	System.out.println("### p_subjseq : " + p_subjseq);
	System.out.println("### p_hrdc2 : " + p_hrdc2);
	
	
    String mertkey = "072c17c58fb48418b2c296ec630ab203"; //�����޿��� �߱��� ����Ű�� ������ �ֽñ� �ٶ��ϴ�.

    StringBuffer sb = new StringBuffer();
    sb.append(transaction);
    sb.append(mid);
    sb.append(oid);
	sb.append(paydate);
    sb.append(respcode);
	sb.append(amount);
    sb.append(mertkey);

    byte[] bNoti = sb.toString().getBytes();

    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digest = md.digest(bNoti);

    StringBuffer strBuf = new StringBuffer();

    for (int i=0 ; i < digest.length ; i++) {
        int c = digest[i] & 0xff;
        if (c <= 15){
            strBuf.append("0");
        }
        strBuf.append(Integer.toHexString(c));
    }

    String hashdata3 = strBuf.toString();
    String[] noti = { 
        msgtype, 
        transaction, 
        mid, 
        oid, 
        amount, 
        currency, 
        paytype, 
        paydate, 
        buyer, 
        productinfo, 
        respcode, 
        respmsg, 
        buyerssn, 
        buyerid, 
        buyeraddress, 
        buyerphone, 
        buyeremail, 
        receiver, 
        receiverphone, 
        deliveryinfo, 
        producttype, 
        productcode, 
        financecode, 
        financename, 
        authnumber, 
        cardnumber, 
        cardexp, 
        cardperiod, 
        nointerestflag, 
        transamount, 
        exchangerate, 
        pid, 
        accountnumber, 
        accountowner, 
        telno, 
        payer, 
        cflag, 
        tamount, 
        camount, 
        bankdate, 
        hashdata,         
		seqno,
		receiptnumber,
		receiptkind,
		receiptself,
		useescrow,
		hashdata2	};
    
    
    boolean isok = false;
    
    String p_chkfinal = "";
    String userid = "";
    String p_jik = "";
    String p_address1 = "";
    String p_amount = "";
    String p_tid = "";
    String p_order_id = "";
    String p_financename = "";
    String p_financecode = "";
    String p_respcode = "";
    
    System.out.println(" hashdata3 --------------> " + hashdata3.trim() );
    System.out.println(" hashdata2 --------------> " + hashdata2 );
    System.out.println(" respcode --------------> " + respcode.trim() );
    System.out.println(" p_year --------------> " + p_year );
    System.out.println(" p_subj --------------> " + p_subj );
    System.out.println(" p_subjseq --------------> " + p_subjseq );
    System.out.println(" p_comp --------------> " + p_comp );
    System.out.println(" p_comp --------------> " + p_comp );
    System.out.println(" oid --------------> " + oid );
    
    if (hashdata3.trim().equals(hashdata2)) {     //�ؽ��� ������ �����̸�
        if (respcode.trim().equals("0000")){     //������ �����̸�
        	
        	HashMap<String, Object> box = new HashMap<String, Object>();
        	
        	box.put("p_chkfinal", "Y");	//�������ο���
        	box.put("p_subj", p_subj);
        	box.put("p_year", p_year);
        	box.put("p_subjseq", p_subjseq);
        	box.put("userid", buyerid);
        	box.put("p_comp", p_comp);
        	box.put("p_jik", "");
        	box.put("p_lec_sel_no", p_lec_sel_no);
        	box.put("p_is_attend", p_is_attend);
        	box.put("p_pay_sel", p_pay_sel);
        	box.put("p_post1", p_post1);
        	box.put("p_post2", p_post2);
        	box.put("p_address1", buyeraddress);
        	box.put("p_hrdc2", p_hrdc2);
        	
        	//����������
        	box.put("p_order_id", oid);	//�ֹ���ȣ
        	box.put("p_tid", transaction);//�ŷ����ι�ȣ
   	     	box.put("p_amount", amount);//�ݾ�
   	     	box.put("p_financename", financename);//ī������
   	     	box.put("p_financecode", financecode);//ī������(����)
   	     	box.put("p_respcode", respcode);//���ι�ȣ
   	     	
   	     	
	   	     if("SC0030".equals(paytype)){
	   	     	box.put("p_cardnumber", accountnumber);//ī���ȣ
	   	  		box.put("p_cardperiod", "0");//�Һΰ�����
	   			box.put("p_authnumber", "");//���ι�ȣ
	   	     } else {
		     	box.put("p_cardnumber", cardnumber);//ī���ȣ
		     	box.put("p_cardperiod", cardperiod);//�Һΰ�����
		     	box.put("p_authnumber", authnumber);//���ι�ȣ
	   	     }
	   	  	
	   	  	try {
		   	     	//������û ���μ���
		   	     	isok = userSubjectService.insertUserProposePG(box);
		   	     //	isok = true;

		   	     	System.out.println("################## ������� ��� �������� ##################");
		   	  		System.out.println("- ������� : " + isok + "-");
		   			System.out.println("###################################################");
	   		
	        } catch ( Exception ex ) { 
	            System.out.println(ex);
	            out.println("FAIL");
				isok = false;
	        }
	        
	        
   	     	//����
   	     	if(isok)
   	     		resp = write_success(noti);
   	     	//����
   	     	else
   	     		resp = write_failure(noti);
            
        } else {                                 //������ �����̸�
            resp = write_failure(noti);
        }
    } else {                                     //�ؽ����� ������ �����̸�
        write_hasherr(noti);
    }

    if(isok){                                   //��������� �����̸�
        out.println("OK");                      
    } else {                                    //��� ������ �����̸�
        out.println("FAIL");
        out.println("\n respcode  : "+respcode);
        out.println("\n hashdata3  : "+hashdata3);
        out.println("\n hashdata2  : "+hashdata2);
        out.println("\n p_chkfinal  : "+p_chkfinal);
        out.println("\n p_subj  : "+p_subj);
        out.println("\n p_year  : "+p_year);
        out.println("\n p_subjseq  : "+p_subjseq);
        out.println("\n userid  : "+userid);
        out.println("\n p_comp  : "+p_comp);
        out.println("\n p_jik  : "+p_jik);
        out.println("\n p_lec_sel_no  : "+p_lec_sel_no);
        out.println("\n p_is_attend  : "+p_is_attend);
        out.println("\n p_pay_sel  : "+p_pay_sel);
        out.println("\n p_post1  : "+p_post1);
        out.println("\n p_post2  : "+p_post2);
        out.println("\n p_address1  : "+p_address1);
        out.println("\n p_hrdc2  : "+p_hrdc2);
        out.println("\n p_order_id  : "+p_order_id);
        out.println("\n p_tid  : "+p_tid);
        out.println("\n p_amount  : "+p_amount);
        out.println("\n p_financename  : "+p_financename);
        out.println("\n p_financecode  : "+p_financecode);
        out.println("\n p_respcode  : "+p_respcode);
    
    }
%>
