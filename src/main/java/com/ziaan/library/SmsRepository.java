package com.ziaan.library;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SmsRepository implements Runnable {
	private static SmsRepository instance;	
	private Object[] smsinfos = null;
	static int SW = 0;
	private static BufferedOutputStream output;
	private static BufferedInputStream input;
	
	public static SmsRepository getInstance() throws Exception {
		if (instance == null)
			instance = new SmsRepository();
		return instance;
	}

	public SmsRepository(Object[] smsinfos) {
		this.smsinfos = smsinfos;
	}

	private SmsRepository() {
	}

	public SmsBean makeSms(ResultSet results) throws Exception {
		try {
			SmsBean ab = new SmsBean();
			
			ab.emp_id = results.getString("emp_id") == null ? "" : results.getString("emp_id");
			ab.message = results.getString("message") == null ? "" : results.getString("message");
			ab.tel = results.getString("tel") == null ? "" : results.getString("tel");
			ab.ilja = results.getString("ilja") == null ? "" : results.getString("ilja");
			ab.result = results.getString("result") == null ? "" : results.getString("result");
			return ab;
		} catch(SQLException e) {
			throw new Exception(e.getMessage());
		}
	}

	public void run() {
		SmsBean[] ab = (SmsBean[])smsinfos[0];
		String sender_tel = (String)smsinfos[1];
		String poi_cd = (String)smsinfos[2];
		String poi_round = (String)smsinfos[3];
		try {
			//System.out.println("........");
			sendSms1(ab, sender_tel, poi_cd, poi_round);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void sendSms(Object[] smsObjs) throws Exception {
		//System.out.println("queue insert start..");
		notifyAll();
	}

	public synchronized SmsBean[] sendSms1(SmsBean[] smsBeans,String sender_tel,String poi_cd, String poi_round) throws Exception {
		String[] result_msg = {
			"전송성공","시스템 장애","실패","데이터 형식 오류","BIND실패","착신가입자 없음","X","X",
			"X","X","X","X","10초당 전송건수 초과","X","X","X","SPAM 메시지"
			};
		// 서버 기본 정보 설정
		String[] SP_NUM= new String[2];
    	SP_NUM[0]= "900143";
	    SP_NUM[1]= "900160";
	    SmsBean[] sbs = new SmsBean[smsBeans.length];
	    
		String cpid = null;
		String sn = null;
		String dstaddr = null;
		
		byte[] readbuff = new byte[8];
		try {
			Buffer data = new Buffer();
			String ls = SmsLink();
		    if (!"7".equals(ls)) {
		    	System.out.println("link가 끊어져 있는 상태 입니다. 접속을 시도합니다.");
	            String rs="1";
	            for(int i = 0 ; i < 4 && !"0".equals(rs) ; i++) {
			        rs = SmsConnect();
			        if(!"0".equals(rs))
			    	   System.out.println("접속 실패!!");
			        else
			    	    System.out.println("접속 성공!!");
	            }
		    } else {
		    	System.out.println("연결 상태 입니다.");
		    }
			int read = 0;
			String type = null, result = null;
			int len=0;
			
            for(int i = 0 ; i < smsBeans.length ; i++) {
            	try {
            		data = new Buffer();
            		data.appendCString("2", 4);
            		data.appendCString("462", 4);
            		data.appendCString("0", 10);
            		data.appendCString(SP_NUM[SW], 32);			
            		data.appendCString(smsBeans[i].tel.replaceAll("-", ""), 32);
            		data.appendCString(sender_tel, 32);
            		data.appendCString(smsBeans[i].message, 160);
            		data.appendCString("", 160);  //url
            		data.appendCString(i+"", 16);
            		data.appendCString("43200", 16);
            		data.appendCString("N", 4);
            		System.out.println("sending data SP_NUM="+SP_NUM[SW]+"tel ="+smsBeans[i].tel+"sender ="+sender_tel+"msg ="+smsBeans[i].message);
            		if(i != 0 &&  i % 25 == 0) {  //25건 마다 2초씩 대기 합니다.
            			System.out.println("  process wait 1 seconds for 25 message....");
            			Thread.sleep(2000);
            		}
            		if(i != 0 && i % 500 == 0) {  //500건 마다 60초씩 대기 합니다.
            			System.out.println("  process wait 60 seconds for 500 message....");
            			Thread.sleep(120000);
            		}
            		output.write(data.getBuffer());
            		output.flush();
            		
        			SmsSendBean sb = new SmsSendBean();
            	} catch(Exception e) {
            		e.printStackTrace();
            	}
            }
            
			for(int i = 0 ; i < smsBeans.length ; i++) {
				try {
					readbuff = new byte[8];
					input.read(readbuff, 0, 8);
					data.setBuffer(readbuff);
					
					type = data.readCString(4);
					len = Integer.parseInt(data.readCString(4));
					readbuff = new  byte[len];
					input.read(readbuff, 0, len);
					data.setBuffer(readbuff);
					
					result = data.readCString(4);
					cpid = data.readCString(32);
					dstaddr = data.readCString(32);
					sn = data.readCString(16);
					
					//System.out.println("deliver_ack data tel="+ab[i].tel+" msg="+ab[i].message+" type=["+type+"] result=["+result_msg[Integer.parseInt(result)]+"] sn=["+sn+"] dstaddr=["+dstaddr+"]");
					sbs[i] = new SmsBean();
					sbs[i].emp_id = smsBeans[i].emp_id;
					sbs[i].emp_nm = smsBeans[i].emp_nm;
					sbs[i].message = smsBeans[i].message;
					sbs[i].tel = dstaddr;
					sbs[i].result = result_msg[Integer.parseInt(result)];
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbs;
	}

	public String SmsLink() {
		byte[] readbuff = new byte[8];
		String linktype = null;
		Buffer data = new Buffer();
		data.appendCString("6", 4);
		data.appendCString("0", 4);
		try {
			
			System.out.println(" write before : " + input.available() ) ;
			output.write(data.getBuffer());
			System.out.println(" flush before : " + input.available() ) ;
			output.flush();
			
			Thread.sleep(100); // 잠시 쉬고..
			if (input.available() >= 8) { 
			System.out.println(" read before : " + input.available() ) ;
			input.read(readbuff, 0, 8); 
			System.out.println(" flush setbuffer : " + input.available() ) ;
			data.setBuffer(readbuff);
			linktype = data.readCString(4);
			int linklen = Integer.parseInt(data.readCString(4));
			System.out.println("link_ack msgtype=["+linktype+"], msglen=["+linklen+"]");
						} 
 
			System.out.println("link_ack msgtype=[빠지기 ] " );

		} catch(Exception e) {
			
			System.out.println("SMS 관련 Exception 발생 1 " );
			e.printStackTrace();
		}
		return linktype;
	}

	public String SmsConnect() {
		// 서버 기본 정보 설정
		String[] ID= new String[2];
		ID[0] = "e_learning";
		ID[1] = "e_learning9";
		String PW = "e_learning";
		// IT 진미지 과장요청 ip변경
		String SERVER ="222.122.244.25";
		// String SERVER ="211.39.134.67";
		int PORT=3491;
		String[] SP_NUM= new String[2];
    	SP_NUM[0]= "900143";
	    SP_NUM[1]= "900160";
	    Socket socket;
	    String result=null;
	    byte[] readbuff = new byte[8];
	    
	    try {
	    	socket = new Socket(SERVER,PORT);
	    	String ip = socket.getLocalAddress().getHostAddress();
	    	
	    	if(ip.equals("147.6.87.89"))
	    		SW = 1;
	    	else
	    		SW = 0;
	    	
	    	output = new BufferedOutputStream(socket.getOutputStream());
	    	input = new BufferedInputStream(socket.getInputStream(),500);				
		
	    	/**  login **/
	    	Buffer data = new Buffer();
	    	data.appendCString("0", 4);
	    	data.appendCString("32", 4);
	    	data.appendCString(ID[SW], 16);
	    	data.appendCString(PW, 16);
	    	output.write(data.getBuffer());
	    	output.flush();
	    	
	    	int read = input.available();
	    	readbuff = new byte[8];
	    	input.read(readbuff, 0, 8);
	    	data.setBuffer(readbuff);
	    	String type = data.readCString(4);
	    	int len = Integer.parseInt(data.readCString(4));
	    	readbuff = new  byte[len];
	    	input.read(readbuff, 0, len);
	    	data.setBuffer(readbuff);
	    	result = data.readCString(4);
	    	String prefix = data.readCString(16);
	    	System.out.println("recv data type=["+type+"] len=["+len+"] result=["+result+"] prefix=["+prefix+"]");
	    } catch(Exception e) {
	    	System.out.println("SMS 관련 Exception 발생 2 " );
	    	e.printStackTrace();
	    }
	    return result;
	}
}