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
// 이 페이지는 링크창 + 웹전송 방식의 note_url 샘플입니다.
//
//============================================================================================================
// 주의1 : 이 페이지를 ret_url에 지정하시면 안됩니다. ret_url과 note_url로 돌려드리는 결과값은 차이가 있습니다.
//            본 페이지는 반드시 note_url 에만 지정하셔야 합니다. (* ret_url 은 메뉴얼을 참고로 별도 제작하셔야 합니다)
//
// 주의2 : note_url 페이지의 경우 hashdata 검증이 필수 입니다. 따라서 상점관리자에서 확인된 mertkey값을 아래 
//           샘플내용에 꼭 지정해 주셔야 합니다.
//
// 주의3 : note_url은 LG데이콤 결제서버가 호출하는 페이지로 OK 출력이외의 메시지가 수신될 경우 결과전송에 문제가
//           발생하게 됩니다. html 태그나 자바스크립트 코드를 사용하는 경우 동작을 보장할 수 없습니다.
//
// 주의4 : 정상적으로 data를 처리한 경우에도 LG데이콤에서 OK 응답을 받지 못한 경우는 결제결과가 중복해서 나갈 수 
//           있으므로 관련한 처리도 고려되어야 합니다
//
// 주의5 : 결제에 대한 결과 처리는 각각의 경우에 따라 write_success(),write_failure(),write_hasherr()에서 관련 루틴을
//           추가 하시면 됩니다.
//
// 주의6 : note_url 페이지가 호출이 안되는 경우 상점관리자 페이지(test/service)에서 계약정보 -> 상점정보관리 -> 
//			시스템연동정보에 웹전송 셋팅이 되어 있는지 확인바랍니다.
//============================================================================================================
//
// [ note_url 페이지가 문제가 있는 경우 ]
//
// 상점관리자 페이지-> 결제내역조회 -> 전체거래내역조회 -> 전송실패내역조회 에서 note_url이 LG데이콤으로 응답한 
// 내용을 확인 하실수 있습니다.
// 
// - 서비스 상점관리자 URL : http://pgweb.dacom.net
// - 테스트 상점관리자 URL : http://pgweb.dacom.net/tmert
//
	WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	UserSubjectService userSubjectService = (UserSubjectService)ctx.getBean("userSubjectService");
	
	
	request.setCharacterEncoding("euc-kr");

	String respcode="";			// 응답코드: 0000(성공) 그외 실패
	String respmsg="";			// 응답메세지
	String hashdata="";			// 해쉬값
	String transaction="";		// 데이콤이 부여한 거래번호
	String mid="";				// 상점아이디 
	String oid="";				// 주문번호
	String amount="";			// 거래금액
	String currency="";			// 통화코드('410':원화, '840':달러)
	String paytype="";			// 결제수단코드
	String msgtype="";			// 거래종류에 따른 데이콤이 정의한 코드
	String paydate="";			// 거래일시(승인일시/이체일시)
	String buyer="";			// 구매자명
	String productinfo="";		// 상품정보
	String buyerssn="";			// 구매자주민등록번호
	String buyerid="";			// 구매자ID
	String buyeraddress="";		// 구매자주소
	String buyerphone="";		// 구매자전화번호
	String buyeremail="";		// 구매자이메일주소
	String receiver="";			// 수취인명
	String receiverphone="";	// 수취인전화번호
	String deliveryinfo="";		// 배송정보
	String producttype="";		// 상품유형
	String productcode="";		// 상품코드
	String financecode="";		// 결제기관코드(카드종류/은행코드/이통사코드)
	String financename="";		// 결제기관이름(카드이름/은행이름/이통사이름)

	String authnumber="";		// 승인번호(신용카드)
	String cardnumber="";		// 카드번호(신용카드)
	String cardexp="";			// 유효기간(신용카드)
	String cardperiod="";		// 할부개월수(신용카드)	
	String nointerestflag="";	//무이자할부여부(신용카드) - '1'이면 무이자할부 '0'이면 일반할부
	String transamount="";		// 환율적용금액(신용카드)
	String exchangerate="";		// 환율(신용카드)

	String pid="";				// 예금주/휴대폰소지자 주민등록번호(계좌이체/휴대폰) 
	String accountowner="";		// 계좌소유주이름(계좌이체) 
	String accountnumber="";	// 계좌번호(계좌이체, 무통장입금) 

	String telno="";			// 휴대폰번호(휴대폰)

    String payer="";            // 입금인(무통장입금)
    String cflag="";            // 무통장입금 플래그(무통장입금) - 'R':계좌할당, 'I':입금, 'C':입금취소
    String tamount="";          // 입금총액(무통장입금)
    String camount="";          // 현입금액(무통장입금)
    String bankdate="";         // 입금또는취소일시(무통장입금)
	String seqno="";			// 입금순서(무통장입금)
	String receiptnumber="";	// 현금영수증 승인번호
	String receiptkind="";		// 현금영수증 종류 - 0: 소득공제용 , 1: 지출증빙용0
	String receiptself="";		//
	String useescrow="";		// 최종 에스크로 적용 여부 - Y : 에스크로 적용, N : 에스크로 미적용
	String hashdata2="";		// 해쉬값2
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

	//교육원에서 사용하는 파라메터
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
	
	
    String mertkey = "072c17c58fb48418b2c296ec630ab203"; //데이콤에서 발급한 상점키로 변경해 주시기 바랍니다.

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
    
    if (hashdata3.trim().equals(hashdata2)) {     //해쉬값 검증이 성공이면
        if (respcode.trim().equals("0000")){     //결제가 성공이면
        	
        	HashMap<String, Object> box = new HashMap<String, Object>();
        	
        	box.put("p_chkfinal", "Y");	//최종승인여부
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
        	
        	//결제데이터
        	box.put("p_order_id", oid);	//주문번호
        	box.put("p_tid", transaction);//거래승인번호
   	     	box.put("p_amount", amount);//금액
   	     	box.put("p_financename", financename);//카드종류
   	     	box.put("p_financecode", financecode);//카드종류(숫자)
   	     	box.put("p_respcode", respcode);//승인번호
   	     	
   	     	
	   	     if("SC0030".equals(paytype)){
	   	     	box.put("p_cardnumber", accountnumber);//카드번호
	   	  		box.put("p_cardperiod", "0");//할부개월수
	   			box.put("p_authnumber", "");//승인번호
	   	     } else {
		     	box.put("p_cardnumber", cardnumber);//카드번호
		     	box.put("p_cardperiod", cardperiod);//할부개월수
		     	box.put("p_authnumber", authnumber);//승인번호
	   	     }
	   	  	
	   	  	try {
		   	     	//수강신청 프로세스
		   	     	isok = userSubjectService.insertUserProposePG(box);
		   	     //	isok = true;

		   	     	System.out.println("################## 결제결과 등록 성공여부 ##################");
		   	  		System.out.println("- 결제결과 : " + isok + "-");
		   			System.out.println("###################################################");
	   		
	        } catch ( Exception ex ) { 
	            System.out.println(ex);
	            out.println("FAIL");
				isok = false;
	        }
	        
	        
   	     	//성공
   	     	if(isok)
   	     		resp = write_success(noti);
   	     	//실패
   	     	else
   	     		resp = write_failure(noti);
            
        } else {                                 //결제가 실패이면
            resp = write_failure(noti);
        }
    } else {                                     //해쉬값이 검증이 실패이면
        write_hasherr(noti);
    }

    if(isok){                                   //결과연동이 성공이면
        out.println("OK");                      
    } else {                                    //결과 연동이 실패이면
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
