package egovframework.com.aja.lcm.domain;

import java.io.Serializable;


import org.adl.api.ecmascript.APIErrorManager;
import org.adl.datamodels.SCODataManager;
import org.adl.sequencer.ADLValidRequests;
import org.adl.sequencer.SeqActivityTree;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @Class Name : SessionDMInstance.java
 * @Description : SessionDMInstance 대한 등록/수정/삭제/조회를 수행하는 Data Access Object
 * @Modification Information
 *
 *    수정일          수정자         수정내용
 *    -------        -------     -------------------
 *    2011.09.08   
 *
 * @author 김종도
 * @since 2011.09.08
 * @version 1.0
 * @see
 *
 */

public class SessionDMInstance implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String mUserID;
    private String mUserName;
    private String mSCOID;
    private String mNumAttempt;
    private String mActivityID;
    private APIErrorManager APIEM;
    private SCODataManager SCODM;
    private SeqActivityTree mSeqActivityTree;
    private ADLValidRequests mValidRequests;
    private long startTime;
    private String orgCd;
    private int courseSeq;
    private int orgSeq;
    private String course_map;
    private String progress_measure;
    private String total_time;
    private int attempt;
    private String user_ip;

    public String getCourse_map() {
		return course_map;
	}

	public void setCourse_map(String course_map) {
		this.course_map = course_map;
	}

	public void setSessionDMInstance()
    {
        mUserID = null;
        mUserName = null;
        mSCOID = null;
        mNumAttempt = null;
        mActivityID = null;
        APIEM = null;
        SCODM = null;
        mSeqActivityTree = null;
        mValidRequests = null;
        startTime = 0L;
        orgCd = null;
        courseSeq = 0;
        orgSeq = 0;
        progress_measure = null;
        total_time = null;
        attempt = 0;
        user_ip = null;
    }

	/**
	 * @return the mUserID
	 */
	public String getMUserID() {
		return mUserID;
	}

	/**
	 * @param userID the mUserID to set
	 */
	public void setMUserID(String userID) {
		mUserID = userID;
	}

	/**
	 * @return the mUserName
	 */
	public String getMUserName() {
		return mUserName;
	}

	/**
	 * @param userName the mUserName to set
	 */
	public void setMUserName(String userName) {
		mUserName = userName;
	}

	/**
	 * @return the mSCOID
	 */
	public String getMSCOID() {
		return mSCOID;
	}

	/**
	 * @param mscoid the mSCOID to set
	 */
	public void setMSCOID(String mscoid) {
		mSCOID = mscoid;
	}

	/**
	 * @return the mNumAttempt
	 */
	public String getMNumAttempt() {
		return mNumAttempt;
	}

	/**
	 * @param numAttempt the mNumAttempt to set
	 */
	public void setMNumAttempt(String numAttempt) {
		mNumAttempt = numAttempt;
	}

	/**
	 * @return the mActivityID
	 */
	public String getMActivityID() {
		return mActivityID;
	}

	/**
	 * @param activityID the mActivityID to set
	 */
	public void setMActivityID(String activityID) {
		mActivityID = activityID;
	}

	/**
	 * @return the aPIEM
	 */
	public APIErrorManager getAPIEM() {
		return APIEM;
	}

	/**
	 * @param apiem the aPIEM to set
	 */
	public void setAPIEM(APIErrorManager apiem) {
		APIEM = apiem;
	}

	/**
	 * @return the sCODM
	 */
	public SCODataManager getSCODM() {
		return SCODM;
	}

	/**
	 * @param scodm the sCODM to set
	 */
	public void setSCODM(SCODataManager scodm) {
		SCODM = scodm;
	}

	/**
	 * @return the mSeqActivityTree
	 */
	public SeqActivityTree getMSeqActivityTree() {
		return mSeqActivityTree;
	}

	/**
	 * @param seqActivityTree the mSeqActivityTree to set
	 */
	public void setMSeqActivityTree(SeqActivityTree seqActivityTree) {
		mSeqActivityTree = seqActivityTree;
	}

	/**
	 * @return the mValidRequests
	 */
	public ADLValidRequests getMValidRequests() {
		return mValidRequests;
	}

	/**
	 * @param validRequests the mValidRequests to set
	 */
	public void setMValidRequests(ADLValidRequests validRequests) {
		mValidRequests = validRequests;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the orgCd
	 */
	public String getOrgCd() {
		return orgCd;
	}

	/**
	 * @param orgCd the orgCd to set
	 */
	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}

	/**
	 * @return the courseSeq
	 */
	public int getCourseSeq() {
		return courseSeq;
	}

	/**
	 * @param courseSeq the courseSeq to set
	 */
	public void setCourseSeq(int courseSeq) {
		this.courseSeq = courseSeq;
	}

	/**
	 * @return the orgSeq
	 */
	public int getOrgSeq() {
		return orgSeq;
	}

	/**
	 * @param orgSeq the orgSeq to set
	 */
	public void setOrgSeq(int orgSeq) {
		this.orgSeq = orgSeq;
	}

	/**
	 * @return the progress_measure
	 */
	public String getProgress_measure() {
		return progress_measure;
	}

	/**
	 * @param progress_measure the progress_measure to set
	 */
	public void setProgress_measure(String progress_measure) {
		this.progress_measure = progress_measure;
	}

	/**
	 * @return the total_time
	 */
	public String getTotal_time() {
		return total_time;
	}

	/**
	 * @param total_time the total_time to set
	 */
	public void setTotal_time(String total_time) {
		this.total_time = total_time;
	}

	/**
	 * @return the attempt
	 */
	public int getAttempt() {
		return attempt;
	}

	/**
	 * @param attempt the attempt to set
	 */
	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

	/**
	 * @return the user_ip
	 */
	public String getUser_ip() {
		return user_ip;
	}

	/**
	 * @param user_ip the user_ip to set
	 */
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
  
}

