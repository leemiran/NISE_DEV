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
        	smsQueue.notifyAll(); //SMSSender를 깨운다.
        	//System.out.println("SMSSenderController 에게 깨어나라고 알린다.");
        }	        
    }
	
    private Object[] getSMSInfo() {
    	Object[] smsInfos = null;
        
        synchronized(smsQueue) {
            try {
                while(smsInfos == null) {
	            	if(smsQueue.size() > 0) {
	            		//System.out.println("SMSSenderController 깨어나다.");
	            		smsInfos = (Object[])smsQueue.removeFirst();
	            	} else {
	            		//System.out.println("SMSSenderController 잠들다.");
	            		smsQueue.wait();  //smsQueue에 아무것도 없다면 SMSSender는 잠든다.
	            	}
                }
            } catch(InterruptedException silent) {
            }
            //밀리지 않고 처리되는지를 검사
            //System.out.println("queue save size : " + smsQueue.size());
        }
        return smsInfos;
    }
	
	public class SmsSenderController extends Thread {
	    public void run() {
	    	while(true) {
	    		//System.out.println("SenderController입니다....");
	    		Object[] smsInfos = getSMSInfo(); //Queue에서 가져오기
		    	//실제 SMSSender에게 보내기
		        cachedThreadPool.execute(new SmsRepository(smsInfos));
	    	}
	    }
	}
}