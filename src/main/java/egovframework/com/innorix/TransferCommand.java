package egovframework.com.innorix;

import java.lang.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransferCommand {
	private String test1;

	
    protected final Log logger = LogFactory.getLog(getClass());

    public TransferCommand()
	{
	}

	public String getTest1() 
	{
		return test1;
	}

	public void setTest1(String test1) 
	{
		this.test1 = test1;
	}
}
