package egovframework.com.aja.lcm.domain;

import java.io.Serializable;


import org.adl.sequencer.ADLLaunch;
import org.adl.sequencer.SeqActivityTree;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @Class Name : board.java
 * @Description : 파일정보 처리를 위한 VO 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 25.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */

public class ScormStudyRoomConfigBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String itemId;
    private String itemURL;
    private String HtmlSCORMTree;
    private String prevButton;
    private String nextButton;
    private String quitButton;
    private String suspendAllButton;
    private SCORMTreeBEAN treeBean[];
    private String req_apply_seq;
    
	private SeqActivityTree mSeqActivityTree;
	private ADLLaunch mlaunch;
    


    public ScormStudyRoomConfigBean()
    {
        itemURL = null;
        HtmlSCORMTree = null;
        prevButton = "";
        nextButton = "";
        quitButton = "";
        suspendAllButton = "";
        treeBean = null;
        mSeqActivityTree = null;
        mlaunch = null;
    }

	/**
	 * @return the itemURL
	 */
	public String getItemURL() {
		return itemURL;
	}

	/**
	 * @param itemURL the itemURL to set
	 */
	public void setItemURL(String itemURL) {
		this.itemURL = itemURL;
	}

	/**
	 * @return the htmlSCORMTree
	 */
	public String getHtmlSCORMTree() {
		return HtmlSCORMTree;
	}

	/**
	 * @param htmlSCORMTree the htmlSCORMTree to set
	 */
	public void setHtmlSCORMTree(String htmlSCORMTree) {
		HtmlSCORMTree = htmlSCORMTree;
	}

	/**
	 * @return the prevButton
	 */
	public String getPrevButton() {
		return prevButton;
	}

	/**
	 * @param prevButton the prevButton to set
	 */
	public void setPrevButton(String prevButton) {
		this.prevButton = prevButton;
	}

	/**
	 * @return the nextButton
	 */
	public String getNextButton() {
		return nextButton;
	}

	/**
	 * @param nextButton the nextButton to set
	 */
	public void setNextButton(String nextButton) {
		this.nextButton = nextButton;
	}

	/**
	 * @return the quitButton
	 */
	public String getQuitButton() {
		return quitButton;
	}

	/**
	 * @param quitButton the quitButton to set
	 */
	public void setQuitButton(String quitButton) {
		this.quitButton = quitButton;
	}

	/**
	 * @return the suspendAllButton
	 */
	public String getSuspendAllButton() {
		return suspendAllButton;
	}

	/**
	 * @param suspendAllButton the suspendAllButton to set
	 */
	public void setSuspendAllButton(String suspendAllButton) {
		this.suspendAllButton = suspendAllButton;
	}

	/**
	 * @return the treeBean
	 */
	public SCORMTreeBEAN[] getTreeBean() {
		return treeBean;
	}

	/**
	 * @param treeBean the treeBean to set
	 */
	public void setTreeBean(SCORMTreeBEAN[] treeBean) {
		this.treeBean = treeBean;
	}

	/**
	 * @return the req_apply_seq
	 */
	public String getReq_apply_seq() {
		return req_apply_seq;
	}

	/**
	 * @param req_apply_seq the req_apply_seq to set
	 */
	public void setReq_apply_seq(String req_apply_seq) {
		this.req_apply_seq = req_apply_seq;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public SeqActivityTree getMSeqActivityTree() {
		return mSeqActivityTree;
	}

	public void setMSeqActivityTree(SeqActivityTree seqActivityTree) {
		mSeqActivityTree = seqActivityTree;
	}

	public ADLLaunch getMlaunch() {
		return mlaunch;
	}

	public void setMlaunch(ADLLaunch mlaunch) {
		this.mlaunch = mlaunch;
	}

	
}
