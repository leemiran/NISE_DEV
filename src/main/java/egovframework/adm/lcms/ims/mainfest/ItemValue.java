package egovframework.adm.lcms.ims.mainfest;

import java.util.Vector;

public class ItemValue {

	private String identifier = "";				// 아이디값
	private String identifierref = "";			// 참조자원
	private String type = "";
    //title
	private String title = "";
	//completionThreshold
	private String completionThreshold = ""; // 학습완료기준
	//sequencing
	private String sequencing = "";
	//rollupRules
	private String rolluprules = "";
	private String rollupObjectiveSatisfied = "";
	private String rollupProgressCompletion = "";
	private String objectiveMeasureWeight = "";
	//presentation
	private String presentation = "";
	//navigationInterface
	private String navigationInterface = "";
	//hideLMSUI 
	private String hideLMSUI = "";
	//resource
	private String rsrcType = "";
	private String RsrcScormType = "";
	private String rsrcHref = "";
	private Vector fileHref = new Vector();
	//
	private String rsrcSeq = "";
	
	
	public String getRsrcSeq() {
		return rsrcSeq;
	}
	public void setRsrcSeq(String rsrcSeq) {
		this.rsrcSeq = rsrcSeq;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getIdentifierref() {
		return identifierref;
	}
	public void setIdentifierref(String identifierref) {
		this.identifierref = identifierref;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCompletionThreshold() {
		return completionThreshold;
	}
	public void setCompletionThreshold(String completionThreshold) {
		this.completionThreshold = completionThreshold;
	}
	public String getSequencing() {
		return sequencing;
	}
	public void setSequencing(String sequencing) {
		this.sequencing = sequencing;
	}
	public String getRolluprules() {
		return rolluprules;
	}
	public void setRolluprules(String rolluprules) {
		this.rolluprules = rolluprules;
	}
	public String getRollupObjectiveSatisfied() {
		return rollupObjectiveSatisfied;
	}
	public void setRollupObjectiveSatisfied(String rollupObjectiveSatisfied) {
		this.rollupObjectiveSatisfied = rollupObjectiveSatisfied;
	}
	public String getRollupProgressCompletion() {
		return rollupProgressCompletion;
	}
	public void setRollupProgressCompletion(String rollupProgressCompletion) {
		this.rollupProgressCompletion = rollupProgressCompletion;
	}
	public String getObjectiveMeasureWeight() {
		return objectiveMeasureWeight;
	}
	public void setObjectiveMeasureWeight(String objectiveMeasureWeight) {
		this.objectiveMeasureWeight = objectiveMeasureWeight;
	}
	public String getPresentation() {
		return presentation;
	}
	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}
	public String getNavigationInterface() {
		return navigationInterface;
	}
	public void setNavigationInterface(String navigationInterface) {
		this.navigationInterface = navigationInterface;
	}
	public String getHideLMSUI() {
		return hideLMSUI;
	}
	public void setHideLMSUI(String hideLMSUI) {
		this.hideLMSUI = hideLMSUI;
	}
	public String getRsrcType() {
		return rsrcType;
	}
	public void setRsrcType(String rsrcType) {
		this.rsrcType = rsrcType;
	}
	public String getRsrcScormType() {
		return RsrcScormType;
	}
	public void setRsrcScormType(String RsrcScormType) {
		this.RsrcScormType = RsrcScormType;
	}
	public String getRsrcHref() {
		return rsrcHref;
	}
	public void setRsrcHref(String rsrcHref) {
		this.rsrcHref = rsrcHref;
	}
	public Vector getFileHref() {
		return fileHref;
	}
	public void setFileHref(String fileHref) {
		this.fileHref.add(fileHref);
	}
	
	
	
}
