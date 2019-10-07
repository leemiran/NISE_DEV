package com.ziaan.library;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SMSProcessor {
	private static SMSProcessor smsProcessor = null;
	private static ExecutorService cachedThreadPool = null;
	static {
		smsProcessor = new SMSProcessor();
		cachedThreadPool = Executors.newCachedThreadPool();
	}
	private SmsSenderController senderController = null;
	private LinkedList smsQueue = null;
	
	private SMSProcessor() {
		senderController = new SmsSenderController();
		senderController.start();
		smsQueue = new LinkedList();
	}
	
	public static SMSProcessor getInstance() {
		return smsProcessor;
	}
	
	public void clear() {
	    synchronized(smsQueue) {
			smsQueue.clear();
	    }
	}
	
    public void sendSMS(Object[] smsInfo) {
        synchronized(smsQueue) {
        	smsQueue.add(smsInfo);
        	smsQueue.notifyAll(); //SMSSender�� �����.
        	//System.out.println("SMSSenderController ���� ������ �˸���.");
        }	        
    }
	
    private Object[] getSMSInfo() {
    	Object[] smsInfos = null;
        
        synchronized(smsQueue) {
            try {
                while(smsInfos == null) {
	            	if(smsQueue.size() > 0) {
	            		//System.out.println("SMSSenderController �����.");
	            		smsInfos = (Object[])smsQueue.removeFirst();
	            	} else {
	            		//System.out.println("SMSSenderController ����.");
	            		smsQueue.wait();  //smsQueue�� �ƹ��͵� ���ٸ� SMSSender�� ����.
	            	}
                }
            } catch(InterruptedException silent) {
            }
            //�и��� �ʰ� ó���Ǵ����� �˻�
            //System.out.println("queue save size : " + smsQueue.size());
        }
        return smsInfos;
    }
	
	public class SmsSenderController extends Thread {
	    public void run() {
	    	while(true) {
	    		//System.out.println("SenderController�Դϴ�....");
	    		Object[] smsInfos = getSMSInfo(); //Queue���� ��������
		    	//���� SMSSender���� ������
		        cachedThreadPool.execute(new SmsRepository(smsInfos));
	    	}
	    }
	}
}