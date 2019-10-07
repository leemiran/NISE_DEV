/**
* @(#)SCORMTreeBEAN.java
* description :  학습트리에 대한 등록/수정/삭제/조회를 수행하는 Data Access Object.
* note : .
* date : 2008. 12. 31.
* @author : 4CSOFT-오일환.
* @version : 1.0.
* modified : 
* comment : 
...
*/
package egovframework.com.aja.lcm.domain;

import java.io.Serializable;
 
public class SCORMTreeBEAN implements Serializable{
    private int count;
    private String Title;
    private String ScoId;
    private long attempt;
    private boolean leaf;
    private String completionStatus;
    private String successStatus;
    private String totalTime;
    private boolean current;
    private boolean select;
    private int hasNodeCondition;
    private int parentId;
    private boolean currentSCO;
    private int depth;
    private boolean hasUserInfo;
    private long hasChildNodeCount;

    public SCORMTreeBEAN()
    {
        count = 0;
        Title = null;
        ScoId = null;
        attempt = 0L;
        leaf = true;
        completionStatus = null;
        successStatus = null;
        totalTime = null;
        current = false;
        select = false;
        hasNodeCondition = 0;
        parentId = 0;
        currentSCO = false;
        depth = 0;
        hasUserInfo = false;
        hasChildNodeCount = 0L;
    }

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return Title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		Title = title;
	}

	/**
	 * @return the scoId
	 */
	public String getScoId() {
		return ScoId;
	}

	/**
	 * @param scoId the scoId to set
	 */
	public void setScoId(String scoId) {
		ScoId = scoId;
	}

	/**
	 * @return the attempt
	 */
	public long getAttempt() {
		return attempt;
	}

	/**
	 * @param attempt the attempt to set
	 */
	public void setAttempt(long attempt) {
		this.attempt = attempt;
	}

	/**
	 * @return the leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return the completionStatus
	 */
	public String getCompletionStatus() {
		return completionStatus;
	}

	/**
	 * @param completionStatus the completionStatus to set
	 */
	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}

	/**
	 * @return the successStatus
	 */
	public String getSuccessStatus() {
		return successStatus;
	}

	/**
	 * @param successStatus the successStatus to set
	 */
	public void setSuccessStatus(String successStatus) {
		this.successStatus = successStatus;
	}

	/**
	 * @return the totalTime
	 */
	public String getTotalTime() {
		return totalTime;
	}

	/**
	 * @param totalTime the totalTime to set
	 */
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * @return the current
	 */
	public boolean isCurrent() {
		return current;
	}

	/**
	 * @param current the current to set
	 */
	public void setCurrent(boolean current) {
		this.current = current;
	}

	/**
	 * @return the select
	 */
	public boolean isSelect() {
		return select;
	}

	/**
	 * @param select the select to set
	 */
	public void setSelect(boolean select) {
		this.select = select;
	}

	/**
	 * @return the hasNodeCondition
	 */
	public int getHasNodeCondition() {
		return hasNodeCondition;
	}

	/**
	 * @param hasNodeCondition the hasNodeCondition to set
	 */
	public void setHasNodeCondition(int hasNodeCondition) {
		this.hasNodeCondition = hasNodeCondition;
	}

	/**
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the currentSCO
	 */
	public boolean isCurrentSCO() {
		return currentSCO;
	}

	/**
	 * @param currentSCO the currentSCO to set
	 */
	public void setCurrentSCO(boolean currentSCO) {
		this.currentSCO = currentSCO;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * @return the hasUserInfo
	 */
	public boolean isHasUserInfo() {
		return hasUserInfo;
	}

	/**
	 * @param hasUserInfo the hasUserInfo to set
	 */
	public void setHasUserInfo(boolean hasUserInfo) {
		this.hasUserInfo = hasUserInfo;
	}

	/**
	 * @return the hasChildNodeCount
	 */
	public long getHasChildNodeCount() {
		return hasChildNodeCount;
	}

	/**
	 * @param hasChildNodeCount the hasChildNodeCount to set
	 */
	public void setHasChildNodeCount(long hasChildNodeCount) {
		this.hasChildNodeCount = hasChildNodeCount;
	}

  
}
