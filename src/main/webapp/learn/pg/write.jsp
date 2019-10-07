<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import="java.io.*" %>
<%!

boolean write_success(String noti[]) throws IOException
{
    //결제에 관한 log남기게 됩니다. log path수정 및 db처리루틴이 추가하여 주십시요.
    //write_log("/opt/kise/NewKnise/home/log/note/write_success.log", noti);
    write_log("/usr/local/tomcat6/write_success.log", noti);
    return true;
}

boolean write_failure(String noti[]) throws IOException
{
    //결제에 관한 log남기게 됩니다. log path수정 및 db처리루틴이 추가하여 주십시요.
    //write_log("/opt/kise/NewKnise/home/log/note/write_failure.log", noti);
    write_log("/usr/local/tomcat6/write_failure.log", noti);
    return true;
}

boolean write_hasherr(String noti[]) throws IOException
{
    //결제에 관한 log남기게 됩니다. log path수정 및 db처리루틴이 추가하여 주십시요.
    //write_log("/opt/kise/NewKnise/home/log/note/write_hasherr.log", noti);
    write_log("/usr/local/tomcat6/write_hasherr.log", noti);
    return true;
}

void write_log(String file, String noti[]) throws IOException
{

    StringBuffer strBuf = new StringBuffer();

    strBuf.append("메세지타입:" + noti[0] + "\n");
    strBuf.append("거래번호:" + noti[1] + "\n");
    strBuf.append("상점아이디:" + noti[2] + "\n");
    strBuf.append("주문번호:" + noti[3] + "\n");
    strBuf.append("거래금액:" + noti[4] + "\n");
    strBuf.append("통화코드:" + noti[5] + "\n");
    strBuf.append("결제수단:" + noti[6] + "\n");
    strBuf.append("결제일시:" + noti[7] + "\n");
    strBuf.append("구매자:" + noti[8] + "\n");
    strBuf.append("상품정보:" + noti[9] + "\n\n");
	strBuf.append("응답코드:" + noti[10] + "\n\n");
	strBuf.append("응답메세지:" + noti[11] + "\n\n");
	strBuf.append("구매자주민등록번호:" + noti[12] + "\n\n");
	strBuf.append("구매자ID:" + noti[13] + "\n\n");
	strBuf.append("구매자주소:" + noti[14] + "\n\n");
	strBuf.append("구매자전화번호:" + noti[15] + "\n\n");
	strBuf.append("구매자이메일주소:" + noti[16] + "\n\n");


    byte b[] = strBuf.toString().getBytes("EUC-KR");
    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file, true));
    stream.write(b);
    stream.close();
}

%>



