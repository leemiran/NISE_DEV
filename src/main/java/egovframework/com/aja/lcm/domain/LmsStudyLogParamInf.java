/**
* @(#)LmsStudyLogParamInf.java
* description :  LMS 진도 Table에 대한 Object.
* note : 
* date : 2008. 12. 31.
* @author : 4CSOFT-오일환.
* @version : 1.0.
* modified : 
* comment : 
...
*/
package egovframework.com.aja.lcm.domain;

import java.io.Serializable;

public interface LmsStudyLogParamInf extends Serializable{
	
	public void setItem_id(String item_id);
	public void setItem_seq(int item_seq);
	public void setOrg_seq(int org_seq);
	public void setProgress_measure(String progress_measure);
	public void setTotal_time(long total_time);
	public void setAttempt(int attempt);
	public void setSession_time(long study_time);
}
