/**
* @(#)AOFException.java
* description :  LCMS AofException 처리에 대한 Class
* note : 
* date : 2008. 12. 31.
* @author : 4CSOFT-오일환.
* @version : 1.0.
* modified : 
* comment : 
...
*/
package org.lcms.api.com;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AOFException extends Exception {
	String stackTraceMessage=null;
	
	public AOFException(){
		super();
	}
	
	public AOFException(String message){
		super(message);
	} 
	/**
	 * description: AOFException Message 처리.
	 * @return 
	 * @throws Exception
	 */	
	public AOFException(Throwable e){
		super(e.getMessage());
		this.setStackTrace(e.getStackTrace());	
		StringWriter sw=new StringWriter();
		PrintWriter pr=new PrintWriter(sw);			
		e.printStackTrace(pr);			
		pr.flush();
		sw.flush();			
		stackTraceMessage=sw.toString();
	}

	/**
	 * @return Returns the stackTraceMessage.
	 */
	public String getStackTraceMessage() {
		return stackTraceMessage;
	}

	/**
	 * @param stackTraceMessage The stackTraceMessage to set.
	 */
	public void setStackTraceMessage(String stackTraceMessage) {
		this.stackTraceMessage = stackTraceMessage;
	}

}
