<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import="java.io.*" %>
<%!

boolean write_success(String noti[]) throws IOException
{
    //������ ���� log����� �˴ϴ�. log path���� �� dbó����ƾ�� �߰��Ͽ� �ֽʽÿ�.
    //write_log("/opt/kise/NewKnise/home/log/note/write_success.log", noti);
    write_log("/usr/local/tomcat6/write_success.log", noti);
    return true;
}

boolean write_failure(String noti[]) throws IOException
{
    //������ ���� log����� �˴ϴ�. log path���� �� dbó����ƾ�� �߰��Ͽ� �ֽʽÿ�.
    //write_log("/opt/kise/NewKnise/home/log/note/write_failure.log", noti);
    write_log("/usr/local/tomcat6/write_failure.log", noti);
    return true;
}

boolean write_hasherr(String noti[]) throws IOException
{
    //������ ���� log����� �˴ϴ�. log path���� �� dbó����ƾ�� �߰��Ͽ� �ֽʽÿ�.
    //write_log("/opt/kise/NewKnise/home/log/note/write_hasherr.log", noti);
    write_log("/usr/local/tomcat6/write_hasherr.log", noti);
    return true;
}

void write_log(String file, String noti[]) throws IOException
{

    StringBuffer strBuf = new StringBuffer();

    strBuf.append("�޼���Ÿ��:" + noti[0] + "\n");
    strBuf.append("�ŷ���ȣ:" + noti[1] + "\n");
    strBuf.append("�������̵�:" + noti[2] + "\n");
    strBuf.append("�ֹ���ȣ:" + noti[3] + "\n");
    strBuf.append("�ŷ��ݾ�:" + noti[4] + "\n");
    strBuf.append("��ȭ�ڵ�:" + noti[5] + "\n");
    strBuf.append("��������:" + noti[6] + "\n");
    strBuf.append("�����Ͻ�:" + noti[7] + "\n");
    strBuf.append("������:" + noti[8] + "\n");
    strBuf.append("��ǰ����:" + noti[9] + "\n\n");
	strBuf.append("�����ڵ�:" + noti[10] + "\n\n");
	strBuf.append("����޼���:" + noti[11] + "\n\n");
	strBuf.append("�������ֹε�Ϲ�ȣ:" + noti[12] + "\n\n");
	strBuf.append("������ID:" + noti[13] + "\n\n");
	strBuf.append("�������ּ�:" + noti[14] + "\n\n");
	strBuf.append("��������ȭ��ȣ:" + noti[15] + "\n\n");
	strBuf.append("�������̸����ּ�:" + noti[16] + "\n\n");


    byte b[] = strBuf.toString().getBytes("EUC-KR");
    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file, true));
    stream.write(b);
    stream.close();
}

%>



