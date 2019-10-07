package egovframework.adm.lcms.ims.mainfest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Vector;

import egovframework.adm.lcms.ims.mainfest.ImportMsg;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class manifestTableBean {
	
	
	//File Name
	private static String FileName="";
	private static String subDir = "";

    private Document document;
    private Element rootEmt;
    private List manifestChildsEmts;
//    Name Space
//    private Namespace defaultNameSpace;
//    private Namespace adlcpNameSpace;
    private Namespace imsssNameSpace;
//    private Namespace adlseqNameSpace;
    private Namespace adlnavNameSpace;
    //Error Commment
    //private Vector vErrorMent;

    //Manifest의 Attribute
    private String strManifestID;
//    private String strOrgDefault;

    //Meta Data
    private String strManifestMeta;
    private Vector vOrgMeta;
    private Vector vItemMeta;
 
    //Organiaztions의 Attribute
    private String strOrgsDefault;

    //Organization의 Attribute
    private Vector vOrgTitle;
    private Vector vOrgStructure;
    private Vector vOrgGlobalSystem;
    private Vector vOrgIdentifier;

	//Comment Parse(MODEL, OPTION, COUNT)
	private Vector vComModel;
	private Vector vComOption;
	private Vector vComCount;

    //Item Key
    private Vector vItemOrgMkey;				//Item Org Index
    //Item Reference
//    private Vector vItemOrgIdentifier;
    private Vector vItemParentIdentifier;
    private Vector vItemdepth;
    //Item Attribute
    private Vector vItemIdentifier;
    private Vector vItemIdentifierRef;
    private Vector vItemTitle;
    private Vector vItemIsVisible;
    private Vector vItemParameters;
    private Vector vItemRsrcSeq;
    private Vector vItemSample_item_yn;
    //Item Element
    private Vector vItemTimeLimitAction;
    private Vector vItemComThreshold;
    private Vector vItemDataFromLms;
    

    //Resource Key
    private Vector vRsrcNoMkey;				//Resource No Index
    private Vector vRsrcNoFileMkey;			//Resource File Resource No Index
    private Vector vRsrcNoDependeceMkey;		//Resource Dependency Resource No Index
    private Vector vRsrcDependenceNoMkey;		//Resource Dependency No Index
    //Resource Attribute
    private Vector vRsrcIdentifier;
    private Vector vRsrcType;
    private Vector vRsrcHref;
    private Vector vRsrcXmlBase;
    private Vector vRsrcScormType;
    private Vector vRsrcPersistState;
    //Resource File
    private Vector vRsrcFileType;				//File Type ("sco","asset")
    private Vector vRsrcFileHref;
    //Resource Dependency
    private Vector vRsrcDepIdentifierRef;
    //Resource Asset Resource
//    private Vector vRsrcAssetIdentifier;
//    private Vector vRsrcAssetType;
//    private Vector vRsrcAssetHref;
//    private Vector vRsrcAssetXmlBase;
    //Resource Asset File
//    private Vector vRsrcAssetFileHref;
    //Resource Matadata
    private Vector vRsrcMetadata;

    //Sequence Key Count
    int intOrgkey				= 0;
    int intItemkey				= 0;
    int intSeqkey				= 0;
    int intSeqRuleContCnt		= 0;			//SequencingRule
    int intRollUpContCnt		= 0;			//Roll up
    int intObjectivesContCnt	= 0;			//Objective
    //Sequence key
    private Vector vSeqOrgMkey;				//Sequence Org Index
    private Vector vSeqItemMkey;				//Sequence Item Index
    private Vector vSeqMkey;                   //Sequence Index
    private Vector vSeqRuleSeqMkey1;           //SequencingRule 1st Sequence Index
    private Vector vSeqRuleContMkey1;          //SequencingRule 1st ConditionRule Index
    private Vector vSeqRuleSeqMkey2;           //SequencingRule 2nd Sequence Index
    private Vector vSeqRuleContMkey2;          //SequencingRule 2nd ConditionRule Index
    private Vector vSeqRuleContDetailKey;      //SequencingRule ruleCondition Index
    private Vector vRollUpSeqMkey1;            //RollupRules 1st Sequence Index
    private Vector vRollUpContMkey1;           //RollupRules 1st rollupRule Index
    private Vector vRollUpSeqMkey2;            //RollupRules 2nd Sequence Index
    private Vector vRollUpContMkey2;           //RollupRules 2nd rollupRule Index
    private Vector vRollUpContDetailKey;       //RollupRules rollupCondition Index
    private Vector vObjectivesSeqMkey1;        //Objectives 1st Sequence Index
    private Vector vObjectivesContMkey1;       //Objectives 1st objective
    private Vector vObjectivesSeqMkey2;		//Objectives 2nd Sequence Index
    private Vector vObjectivesContMkey2;		//Objectives 2nd objective Index
    private Vector vObjectivesContDetailKey;	//Objectives mapInfo Index

    //Navigation NavigationInterface Element
    private Vector vNavHideLMSUI;
    private Vector vNavHideLmsCont;
    private Vector vNavHideLmsExit;
    private Vector vNavHideLmsPre;
    private Vector vNavHideLmsAbd;
    //ControlMode Attribute
    private Vector vSeqChoice;
    private Vector vSeqChoiceExit;
    private Vector vSeqFlow;
    private Vector vSeqForwardOnly;
    private Vector vSeqUseAttemptObjInfo;
    private Vector vSeqUseAttemptProgressInfo;
    //RuleConditions Attribute
    private Vector vTSeqcondiCombination;
    //RuleAction Attribute
    private Vector vTSeqAction;
    //RullCondition Attribute
    private Vector vTSeqrefObjective;
    private Vector vTSeqmeaThreshold;
    private Vector vTSeqoperator;
    private Vector vTSeqcondition;
    //LimitConditions Element
    private Vector vSeqAttemptLimit;
    private Vector vSeqAttemptDurationLimit;
    //Sequencing RollupRules Element
    private Vector vTSeqType;					//Sequcncing Type ("pre","exit","post")
    private Vector vSeqRollupObjSatisfied;
    private Vector vSeqRollupProgressComplet;
    private Vector vSeqObjMeasureWeight;
    private Vector vSeqChildActivitySet;
    private Vector vSeqMinimumCount;
    private Vector vSeqMinimumPercent;
    private Vector vSeqRollupConditionComb;
    private Vector vSeqRollupAction;
    private Vector vSeqRollupOperator;
    private Vector vSeqRollupCondition;
    //objectives Attribute
    private Vector vSeqObjType;				//Objective Type (primaryObj:pr, Obj:ob)
    private Vector vSeqSatisfiedByMeasure;
    private Vector vSeqObjID;
    private Vector vSeqminNormalMeasure;
    //Mapinfo Attributes.
    private Vector vSeqTargetObjID;
    private Vector vSeqReadSatisStatus;
    private Vector vSeqReadNormMeasure;
    private Vector vSeqWriteSatisStatus;
    private Vector vSeqWriteNormalMeasure;
    //RandomizationControls Element
    private Vector vSeqRandomTiming;
    private Vector vSeqSelectCount;
    private Vector vSeqReorderChildren;
    private Vector vSeqSelectionTiming;
    //deliverControls Element
    private Vector vSeqTracked;
    private Vector vSeqCompletSetByContent;
    private Vector vSeqObjSetByContent;
    //ConstrainedChoiceConsideration Element
    private Vector vSeqPreventActivation;
    private Vector vSeqConstrainChoice;
    //rollupConsiderations Element
    private Vector vSeqReqSatisfied;
    private Vector vSeqReqNotSatisfied;
    private Vector vSeqReqCompleted;
    private Vector vSeqReqIncomplete;
    private Vector vSeqMeasureSatIfActive;

   /**
    * constructor
    */
    public manifestTableBean() {
       	//Error Comment
    	//vErrorMent                      = new Vector();

        //Meta data
        vOrgMeta                        = new Vector();
        vItemMeta                       = new Vector();

        //Organization Attribute
        vOrgTitle                       = new Vector();
        vOrgStructure                   = new Vector();
        vOrgGlobalSystem                = new Vector();
        vOrgIdentifier                  = new Vector();

		//Comment Parse(MODEL, OPTION, COUNT)
		vComModel						= new Vector();
		vComOption                      = new Vector();
		vComCount                       = new Vector();

        //Item key
        vItemOrgMkey                    = new Vector();
        //Item Use
//        vItemOrgIdentifier              = new Vector();
        vItemParentIdentifier           = new Vector();
        vItemdepth                      = new Vector();
        //Item Attribute
        vItemIdentifier                 = new Vector();
        vItemIdentifierRef              = new Vector();
        vItemTitle                      = new Vector();
        vItemIsVisible                  = new Vector();
        vItemParameters                 = new Vector();
        vItemRsrcSeq                    = new Vector();
        vItemSample_item_yn				= new Vector();
        //Item Element
        vItemTimeLimitAction            = new Vector();
        vItemComThreshold               = new Vector();
        vItemDataFromLms                = new Vector();

        //Resource key
        vRsrcNoMkey						= new Vector();	//Resource No Index
        vRsrcNoFileMkey					= new Vector();	//Resource File Resource No Index
        vRsrcNoDependeceMkey			= new Vector();	//Resource Dependency Resource No Index
        vRsrcDependenceNoMkey			= new Vector();	//Resource Dependency No Index
        //Resource Attribute
        vRsrcIdentifier                 = new Vector();
        vRsrcType                       = new Vector();
        vRsrcHref                       = new Vector();
        vRsrcXmlBase                    = new Vector();
        vRsrcScormType                  = new Vector();
        vRsrcPersistState               = new Vector();
        //Resource File
        vRsrcFileType					= new Vector();	//File Type ("sco","asset")
        vRsrcFileHref					= new Vector();
        //Resource Dependency
        vRsrcDepIdentifierRef			= new Vector();
        //Resource Asset Resource
//        vRsrcAssetIdentifier			= new Vector();
//        vRsrcAssetType					= new Vector();
//        vRsrcAssetHref					= new Vector();
//        vRsrcAssetXmlBase				= new Vector();
        //Resource Asset File
//        vRsrcAssetFileHref				= new Vector();
        //Resource Metadate(SCO)
        vRsrcMetadata                   = new Vector();

        //Navigation NavigationInterface Element
        vNavHideLMSUI                   = new Vector();
        vNavHideLmsAbd                  = new Vector();
        vNavHideLmsCont                 = new Vector();
        vNavHideLmsExit                 = new Vector();
        vNavHideLmsPre                  = new Vector();

        //Sequence key
        vSeqOrgMkey                     = new Vector();
        vSeqItemMkey                    = new Vector();
        vSeqMkey                        = new Vector();	//Sequence Index
        vSeqRuleSeqMkey1                = new Vector();	//SequencingRule 1st Sequence Index
        vSeqRuleContMkey1               = new Vector();	//SequencingRule 1st ConditionRule Index
        vSeqRuleSeqMkey2                = new Vector();	//SequencingRule 2nd Sequence Index
        vSeqRuleContMkey2               = new Vector();	//SequencingRule 2nd ConditionRule Index
        vSeqRuleContDetailKey           = new Vector();	//SequencingRule ruleCondition Index
        vRollUpSeqMkey1                 = new Vector();	//RollupRules 1st Sequence Index
        vRollUpContMkey1                = new Vector();	//RollupRules 1st rollupRule Index
        vRollUpSeqMkey2                 = new Vector();	//RollupRules 2nd Sequence Index
        vRollUpContMkey2                = new Vector();	//RollupRules 2nd rollupRule Index
        vRollUpContDetailKey            = new Vector();	//RollupRules rollupCondition Index
        vObjectivesSeqMkey1             = new Vector();	//Objectives 1st Sequence Index
        vObjectivesContMkey1            = new Vector();	//Objectives 1st objective Index
        vObjectivesSeqMkey2             = new Vector();	//Objectives 2nd Sequence Index
        vObjectivesContMkey2            = new Vector();	//Objectives 2nd objective Index
        vObjectivesContDetailKey        = new Vector();	//Objectives mapInfo Index

        //Sequencing ControlMode Element
        vSeqChoice                      = new Vector();
        vSeqChoiceExit                  = new Vector();
        vSeqFlow                        = new Vector();
        vSeqForwardOnly                 = new Vector();
        vSeqUseAttemptObjInfo           = new Vector();
        vSeqUseAttemptProgressInfo      = new Vector();
        //Sequencing SequencingRules Element
        vTSeqType                       = new Vector();	//Sequcncing Type ("pre","exit","post")
        vTSeqcondiCombination           = new Vector();
        vTSeqAction                     = new Vector();
        vTSeqrefObjective               = new Vector();
        vTSeqmeaThreshold               = new Vector();
        vTSeqoperator                   = new Vector();
        vTSeqcondition                  = new Vector();
        //Sequencing LimitConditions Element
        vSeqAttemptLimit                = new Vector();
        vSeqAttemptDurationLimit        = new Vector();
        //Sequencing RollupRules Element
        vSeqRollupObjSatisfied          = new Vector();
        vSeqRollupProgressComplet       = new Vector();
        vSeqObjMeasureWeight            = new Vector();
        vSeqChildActivitySet            = new Vector();
        vSeqMinimumCount                = new Vector();
        vSeqMinimumPercent              = new Vector();
        vSeqRollupConditionComb         = new Vector();
        vSeqRollupAction                = new Vector();
        vSeqRollupOperator              = new Vector();
        vSeqRollupCondition             = new Vector();
        //Sequencing Objectives Element
        vSeqObjType                     = new Vector();	//Objective Type (primaryObj:pr, Obj:ob)
        vSeqSatisfiedByMeasure          = new Vector();
        vSeqObjID                       = new Vector();
        vSeqminNormalMeasure            = new Vector();
        //Mapinfo Attribute
        vSeqTargetObjID                 = new Vector();
        vSeqReadSatisStatus             = new Vector();
        vSeqReadNormMeasure             = new Vector();
        vSeqWriteSatisStatus            = new Vector();
        vSeqWriteNormalMeasure          = new Vector();
        //Sequencing RandomizationControls Element
        vSeqRandomTiming                = new Vector();
        vSeqSelectCount                 = new Vector();
        vSeqReorderChildren             = new Vector();
        vSeqSelectionTiming             = new Vector();
        //Sequencing DeliverControls Element
        vSeqTracked                     = new Vector();
        vSeqCompletSetByContent         = new Vector();
        vSeqObjSetByContent             = new Vector();
        //Sequencing ConstrainedChoiceConsideration Element
        vSeqPreventActivation           = new Vector();
        vSeqConstrainChoice             = new Vector();
        //Sequencing RollupConsiderations Element
        vSeqReqSatisfied                = new Vector();
        vSeqReqNotSatisfied             = new Vector();
        vSeqReqCompleted                = new Vector();
        vSeqReqIncomplete               = new Vector();
        vSeqMeasureSatIfActive          = new Vector();
    }

    /**
     * Manifest 파일을 읽어들여, 각 해당항목을 멤버변수로 저장
     * @param dir		manifest 파일의 경로
	 * @param FileName	manifest 파일의 이름(imsmanifest.xml)
	 * @return errorFlag
	 */
    public boolean setXmlFile(String dir, String FileName, String msgId) throws Exception {
    	ImportMsg msgHandler = null;
    	if (msgId != null) msgHandler = ImportMsg.getInstance();
    	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DEFAULT);
//        try{
            SAXBuilder builder = new SAXBuilder(false);
			// 2005.3.19 linuzer, 변경
			// FileName에 subDir이 붙은 상태로 사용됨
			FileName = dir + "/" + FileName;
			//subDir = subDir_tmp;

			document = builder.build(new File(FileName));

            //Get Root Element
			if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_ROOT);
			rootEmt = document.getRootElement();
            
            if (rootEmt.getName().equals("manifest")) {
            	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_IDENTIFIER);
                if (rootEmt.getAttribute("identifier") !=null) {
                    strManifestID = rootEmt.getAttributeValue("identifier");
                }else {
                	if (msgId != null) msgHandler.setError(msgId, ImportMsg.WARN_MANI_PARSE_NONE_IDENTIFIER, "manifest의 identifier가 없습니다.");
                    //vErrorMent.add("manifest의 identifier가 없습니다.");
                }
            }

            //Get Name Space
//            defaultNameSpace    = rootEmt.getNamespace("");
//            adlcpNameSpace  	= rootEmt.getNamespace("adlcp");
            imsssNameSpace  	= rootEmt.getNamespace("imsss");
//            adlseqNameSpace     = rootEmt.getNamespace("adlseq");
            adlnavNameSpace     = rootEmt.getNamespace("adlnav");

            //Get Child Element For Root Element
            if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_CHILD);
            manifestChildsEmts = rootEmt.getChildren();

            //Looping
            for (int i=0;i< manifestChildsEmts.size();i++) {
                parsingEmt((Element)manifestChildsEmts.get(i),1, msgId);
            }
			//COMMENT PARSE
			setElementStr(FileName);
			return true;
//        }catch(Exception e) {
//        	if (msgId != null) msgHandler.setNowStep(msgId, msgHandler.getLastStep(msgId), e);
//			e.printStackTrace();
//            /*CmLog.writeLog(	"Log Name   : Exception",
//							"Class Name : "+getClass().getName(),
//							"Method Name: setXmlFile()",
//							"Exception  :\n        "+e,
//							"",
//							"elearn_"
//						  );*/
//            //throw e;
//			return false;
//        }
    }//Method setXmlFile() End

    /**
	 * 단계별로 자식노드를 파싱
	 * @param obj 자식노드
	 * @param depth 단계
	 */
    private void parsingEmt(Object obj,int depth, String msgId) {
    	ImportMsg msgHandler = null;
    	if (msgId != null) msgHandler = ImportMsg.getInstance();
    	
        //Depth Display
        String depth_space = "";
        for (int i=0;i < depth ; i++) {
            //depth_space=depth_space+"\t";
            depth_space=depth_space+" ";
        }
        //Get Element
        if (obj instanceof Element) {
            Element tempEmt=(Element)obj;
            //Manifest Meta Data
            
            if (tempEmt.getName().equals("metadata") && ((Element)tempEmt.getParent()).getName().equals("manifest")) {
            	
            	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_META);
                List ManifestMetaEmts = tempEmt.getChildren();
                for (int i=0; i<ManifestMetaEmts.size(); i++) {

                    Element ManifestMetaEmt = (Element)ManifestMetaEmts.get(i);
                    if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_META_LOCATION);
                    if (ManifestMetaEmt.getName().equals("location")) {
                        strManifestMeta = ManifestMetaEmt.getText();
                    }
                }
            }
            
            //Organizations
            if(tempEmt.getName().equals("organizations")) {
            	
            	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_ORGANIZATIONS);
                if(tempEmt.getAttribute("default") !=null) {
                    strOrgsDefault = tempEmt.getAttributeValue("default");
                }else {
                	if (msgId != null) msgHandler.setError(msgId, ImportMsg.WARN_MANI_PARSE_NONE_ORGANIZATIONS_DEFAULT);
                    strOrgsDefault = "";
                }
            }
            
            //Resources
            if(tempEmt.getName().equals("resources")) {
            	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_RESOURCES);
            	this.getResource(tempEmt, msgId);

            }

            //Organization
            if ((tempEmt.getName()).equals("title") && (((Element)tempEmt.getParent()).getName().equals("organization"))) {
            	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_ORGANIZATION);
                vOrgTitle.add(tempEmt.getText());
                if (((Element)tempEmt.getParent()).getAttribute("identifier") !=null ) {
                    vOrgIdentifier.add(((Element)tempEmt.getParent()).getAttributeValue("identifier"));
//                    strOrgDefault = ((Element)tempEmt.getParent()).getAttributeValue("identifier");
                }else {
                	if (msgId != null) msgHandler.setError(msgId, ImportMsg.WARN_MANI_PARSE_NONE_ORGANIZATION_IDENTIFIER, "organization에 identifier가 없습니다");
                    //vErrorMent.add("organization에 identifier가 없습니다");
                }

                //Organization Attribute
                List orgAttrs = ((Element)tempEmt.getParent()).getAttributes();

                vOrgStructure.add("");
                vOrgGlobalSystem.add("");

                for (int j=0; j<orgAttrs.size(); j++) {

                    if(((Attribute)orgAttrs.get(j)).getName().equals("structure")) {

                        vOrgStructure.removeElementAt(vOrgStructure.lastIndexOf(vOrgStructure.lastElement()));
                        vOrgStructure.add(((Attribute)orgAttrs.get(j)).getValue());

                    }else if (((Attribute)orgAttrs.get(j)).getName().equals("objectivesGlobalToSystem")) {

                        vOrgGlobalSystem.removeElementAt(vOrgGlobalSystem.lastIndexOf(vOrgGlobalSystem.lastElement()));
                        vOrgGlobalSystem.add(((Attribute)orgAttrs.get(j)).getValue());
                    }
                }

                //Organization count increase
                intOrgkey ++;
                //Except first organization
                if (intSeqkey == 0) {
                    intOrgkey =0;
                }

                //Sequencing
                //org main count
                vSeqOrgMkey.add(Integer.toString(intOrgkey));
                //item main count
                vSeqItemMkey.add("");
                //seq main count
                vSeqMkey.add(Integer.toString(intSeqkey));
                //seq controlmode
                vSeqChoice.add("");
                vSeqChoiceExit.add("");
                vSeqFlow.add("");
                vSeqForwardOnly.add("");
                vSeqUseAttemptObjInfo.add("");
                vSeqUseAttemptProgressInfo.add("");
                //seq limitconditions
                vSeqAttemptLimit.add("");
                vSeqAttemptDurationLimit.add("");
                //seq rullupRules
                vSeqRollupObjSatisfied.add("");
                vSeqRollupProgressComplet.add("");
                vSeqObjMeasureWeight.add("");
                //seq randomization
                vSeqRandomTiming.add("");
                vSeqSelectCount.add("");
                vSeqReorderChildren.add("");
                vSeqSelectionTiming.add("");
                //seq deliveryControls
                vSeqTracked.add("");
                vSeqCompletSetByContent.add("");
                vSeqObjSetByContent.add("");
                //seq constrainedChoiceConsideration
                vSeqPreventActivation.add("");
                vSeqConstrainChoice.add("");
                //seq rollupConsiderations
                vSeqReqSatisfied.add("");
                vSeqReqNotSatisfied.add("");
                vSeqReqCompleted.add("");
                vSeqReqIncomplete.add("");
                vSeqMeasureSatIfActive.add("");
                //navigation
                vNavHideLMSUI.add("");

                //Organization Meta Data
                vOrgMeta.add("");

                //Organization Meta Data & Sequencing method call
                List orgEmts = ((Element)tempEmt.getParent()).getChildren();

                for (int i=0; i<orgEmts.size(); i++) {

                    Element orgEmt = (Element)orgEmts.get(i);
                    //Meta Data
                    if ( orgEmt.getName().equals("metadata") ) {
                        List OrgMetaEmts = orgEmt.getChildren();
                        for (int j=0; j<OrgMetaEmts.size(); j++) {
                            Element OrgMetaEmt = (Element)OrgMetaEmts.get(j);
                            if (OrgMetaEmt.getName().equals("location")) {
                                vOrgMeta.removeElementAt(vOrgMeta.lastIndexOf(vOrgMeta.lastElement()));
                                vOrgMeta.add(OrgMetaEmt.getText());
                            }
                        }
                    }

                    //Sequencing
                    if (orgEmt.getName().equals("sequencing")) {
                    	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_ORGANIZATION_SEQUENCING);
                        this.getSeqNav(orgEmt, intSeqkey);

                        intSeqkey ++;

                    }
                }
            }//Organization End
           
            //Item
            if ((tempEmt.getName()).equals("title") && (((Element)tempEmt.getParent()).getName().equals("item"))) {
            	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_ITEM);
//            	SAMPLE 지정
            	try{   
//            		LcmsItemResourceVO vo = new LcmsItemResourceVO();   
//            		vo = model.samplersrcseq();
	                vItemOrgMkey.add(Integer.toString(intOrgkey));
	                //vItemdepth.add(String.valueOf(depth+1));
	                vItemdepth.add(depth_space);
	                //주차가 없을때 샘플로 대입시킴
	                if(!tempEmt.getText().equals("NewItem")){
	                	vItemTitle.add(tempEmt.getText());
	                }else{
	                	vItemTitle.add("");
//	                	vItemTitle.add(vo.getRsrc_title());
	                }
	                //Item Attribute
	                if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.WARN_MANI_PARSE_NONE_ITEM_IDENTIFIER);
	                vItemIdentifier.add(((Element)tempEmt.getParent()).getAttributeValue("identifier"));
	                String ref_null = String.valueOf(((Element)tempEmt.getParent()).getAttribute("identifierref"));
	                if (((Element)tempEmt.getParent()).getAttribute("identifierref") !=null && !ref_null.equals("[Attribute: identifierref=\"\"]") && !ref_null.equals("[Attribute: identifierref=\"0\"]")) {	                	
	                    vItemIdentifierRef.add(((Element)tempEmt.getParent()).getAttributeValue("identifierref"));
	                }else {
	                	//String itemTitle = tempEmt.getText();
	                	//msgHandler.setError(msgId, ImportMsg.WARN_MANI_PARSE_NONE_ITEM_IDENTIFIER, "<"+itemTitle+">"+" 아이탬의 identifierref 속성이 없습니다.");
	                	vItemIdentifierRef.add("");
//	                	if(vo!=null){
//	                		vItemIdentifierRef.add(vo.getRsrc_id());
//	                	}else{
//	                		vItemIdentifierRef.add("");
//	                	}
	                }
	                if (((Element)tempEmt.getParent()).getAttribute("rsrc_seq") !=null) {
	                	vItemRsrcSeq.add(((Element)tempEmt.getParent()).getAttributeValue("rsrc_seq"));	                    
	                }else {
//	                	for(int i=0;i<vItemIdentifier.size();i++){		                	  
		                	//String itemTitle = tempEmt.getText();
		                	//msgHandler.setError(msgId, ImportMsg.WARN_MANI_PARSE_NONE_ITEM_IDENTIFIER, "<"+itemTitle+">"+" 아이탬의 identifierref 속성이 없습니다.");
	                	vItemRsrcSeq.add("");
//	                	if(vo!=null){
//	                		vItemRsrcSeq.add(String.valueOf(vo.getRsrc_seq()));
//	                	}else{
//	                		vItemRsrcSeq.add("");
//	                	}
		                			                			               
//	                	}	                	
	                }
            	}catch(Exception e){
            		System.err.print(e);
            	}     
                if (((Element)tempEmt.getParent()).getAttribute("sample_item_yn") !=null) {
                	vItemSample_item_yn.add(((Element)tempEmt.getParent()).getAttributeValue("sample_item_yn"));
                }else{
                	//String itemTitle = tempEmt.getText();
                	//msgHandler.setError(msgId, ImportMsg.WARN_MANI_PARSE_NONE_ITEM_IDENTIFIER, "<"+itemTitle+">"+" 아이탬의 identifierref 속성이 없습니다.");
                	vItemSample_item_yn.add("Y");
                }
                if (((Element)tempEmt.getParent()).getAttribute("isvisible") !=null) {
                    vItemIsVisible.add(((Element)tempEmt.getParent()).getAttributeValue("isvisible"));
                }else { //default
                    vItemIsVisible.add("true");
                }
                if (((Element)tempEmt.getParent()).getAttribute("parameters") !=null) {
                    vItemParameters.add(((Element)tempEmt.getParent()).getAttributeValue("parameters"));
                }else {
                    vItemParameters.add("");
                }
                if (tempEmt.getParent().getParent() != null) {
                    if (((Element)((Element)tempEmt.getParent()).getParent()).getName().equals("item")) {
                    	if (((Element)((Element)tempEmt.getParent()).getParent()).getAttribute("identifier") != null) {
                    		vItemParentIdentifier.add(((Element)((Element)tempEmt.getParent()).getParent()).getAttributeValue("identifier"));
                    	}else {
                    		if (msgId != null) msgHandler.setError(msgId, ImportMsg.WARN_MANI_PARSE_NONE_ITEM_PARENT_IDENTIFIER);
                    	}
                    }else {
                        vItemParentIdentifier.add("");
                    }
                }else {
                    vItemParentIdentifier.add("");
                }
                //Item Child Element
                if (tempEmt.getParent() != null) {

                    List childEmts = ((Element)tempEmt.getParent()).getChildren();

                    vItemTimeLimitAction.add("");
                    vItemDataFromLms.add("");
                    vItemComThreshold.add("");
                    //Navigation
                    vNavHideLMSUI.add("");

                    for (int j=0; j<childEmts.size(); j++) {
                        if (((Element)childEmts.get(j)).getName().equals("timeLimitAction")) {
                            vItemTimeLimitAction.removeElementAt(vItemTimeLimitAction.lastIndexOf(vItemTimeLimitAction.lastElement()));
                            vItemTimeLimitAction.add(((Element)childEmts.get(j)).getTextTrim());

                        }else if (((Element)childEmts.get(j)).getName().equals("dataFromLMS")) {
                            vItemDataFromLms.removeElementAt(vItemDataFromLms.lastIndexOf(vItemDataFromLms.lastElement()));
                            vItemDataFromLms.add(((Element)childEmts.get(j)).getText());

                        }else if (((Element)childEmts.get(j)).getName().equals("completionThreshold")) {
                            vItemComThreshold.removeElementAt(vItemComThreshold.lastIndexOf(vItemComThreshold.lastElement()));
                            vItemComThreshold.add(((Element)childEmts.get(j)).getTextTrim());

                        }else if (((Element)childEmts.get(j)).getName().equals("presentation")) {
                            if (((Element)childEmts.get(j)).getChild("navigationInterface",adlnavNameSpace) != null) {
                                List nav = ((Element)childEmts.get(j)).getChild("navigationInterface",adlnavNameSpace).getChildren();
                                int nav_cnt = nav.size();
                                String buffer = "";
                                for (int n=0; n<nav_cnt;n++) {
                                    if (((Element)nav.get(n)).getName().equals("hideLMSUI")) {
                                        buffer = buffer+":"+((Element)nav.get(n)).getText();
                                        vNavHideLMSUI.removeElementAt(vNavHideLMSUI.lastIndexOf(vNavHideLMSUI.lastElement()));
                                        vNavHideLMSUI.add(buffer);
                                    }
                                }
                            }
                        }
                    }
                }

                //Sequencing
                //org main count
                vSeqOrgMkey.add(Integer.toString(intOrgkey));
                //item main count
                vSeqItemMkey.add(Integer.toString(intItemkey));
                //seq main count
                vSeqMkey.add(Integer.toString(intSeqkey));
                //seq controlmode
                vSeqChoice.add("");
                vSeqChoiceExit.add("");
                vSeqFlow.add("");
                vSeqForwardOnly.add("");
                vSeqUseAttemptObjInfo.add("");
                vSeqUseAttemptProgressInfo.add("");
                //seq limitconditions
                vSeqAttemptLimit.add("");
                vSeqAttemptDurationLimit.add("");
                //seq rullupRules
                vSeqRollupObjSatisfied.add("");
                vSeqRollupProgressComplet.add("");
                vSeqObjMeasureWeight.add("");
                //seq randomization
                vSeqRandomTiming.add("");
                vSeqSelectCount.add("");
                vSeqReorderChildren.add("");
                vSeqSelectionTiming.add("");
                //seq deliveryControls
                vSeqTracked.add("");
                vSeqCompletSetByContent.add("");
                vSeqObjSetByContent.add("");
                //seq constrainedChoiceConsideration
                vSeqPreventActivation.add("");
                vSeqConstrainChoice.add("");
                //seq rollupConsiderations
                vSeqReqSatisfied.add("");
                vSeqReqNotSatisfied.add("");
                vSeqReqCompleted.add("");
                vSeqReqIncomplete.add("");
                vSeqMeasureSatIfActive.add("");

                //Item Meta Data
                vItemMeta.add("");

                //Item Sequencing method call
                List itemEmts = ((Element)tempEmt.getParent()).getChildren();

                for(int i=0; i<itemEmts.size(); i++){
                    Element itemEmt = (Element)itemEmts.get(i);
                    //Meta Data
                    if( itemEmt.getName().equals("metadata") ){
                        List ItemMetaEmts = itemEmt.getChildren();
                        for(int j=0; j<ItemMetaEmts.size(); j++){
                            Element ItemMetaEmt = (Element)ItemMetaEmts.get(j);
                            if(ItemMetaEmt.getName().equals("location")){
                                vItemMeta.removeElementAt(vItemMeta.lastIndexOf(vItemMeta.lastElement()));
                                vItemMeta.add(ItemMetaEmt.getText());
                            }
                        }
                    }
                    if( itemEmt.getName().equals("sequencing") ){
                    	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_ITEM_SEQUENCING);
                        this.getSeqNav(itemEmt, intSeqkey);
                        intSeqkey ++;
                    }
                }//Item Sequencing End
                intItemkey ++;
            }//Item End

            //get Child Element
            List Emts = ((Element)obj).getChildren();

            for(int j=0;j<Emts.size();j++){
                parsingEmt((Element)Emts.get(j),depth+1, msgId);
            }
        }//Obj Element End
    }//Method parsingEmt() End

    /**
	 * 시퀀싱 정보를 파싱
	 * @param seqEmt
	 * @param intIndex
	 */
    public void getSeqNav(Element seqEmt, int intSeqIndex) {

        List seqChildEmts = seqEmt.getChildren();

        for	(int m=0;m<seqChildEmts.size();m++) {
                Element emt = (Element)seqChildEmts.get(m);
                /*------------------------------------
                 			controlMode
                -------------------------------------*/
                if (emt.getName().equals("controlMode")) {
                    if (emt.getAttribute("choice") != null) {
                        vSeqChoice.removeElementAt(intSeqIndex);
                        vSeqChoice.add(intSeqIndex,emt.getAttributeValue("choice"));
                    }
                    if (emt.getAttribute("choiceExit") != null) {
                        vSeqChoiceExit.removeElementAt(intSeqIndex);
                        vSeqChoiceExit.add(intSeqIndex, emt.getAttributeValue("choiceExit"));
                    }
                    if (emt.getAttribute("flow") != null) {
                        vSeqFlow.removeElementAt(intSeqIndex);
                        vSeqFlow.add(intSeqIndex, emt.getAttributeValue("flow"));
                    }
                    if (emt.getAttribute("forwardOnly") != null) {
                        vSeqForwardOnly.removeElementAt(intSeqIndex);
                        vSeqForwardOnly.add(intSeqIndex, emt.getAttributeValue("forwardOnly"));
                    }
                    if (emt.getAttribute("useCurrentAttemptObjectiveInfo") != null) {
                        vSeqUseAttemptObjInfo.removeElementAt(intSeqIndex);
                        vSeqUseAttemptObjInfo.add(intSeqIndex, emt.getAttributeValue("useCurrentAttemptObjectiveInfo"));
                    }
                    if (emt.getAttribute("useCurrentAttemptProgressInfo") != null) {
                        vSeqUseAttemptProgressInfo.removeElementAt(intSeqIndex);
                        vSeqUseAttemptProgressInfo.add(intSeqIndex, emt.getAttributeValue("useCurrentAttemptProgressInfo"));
                    }
                }

                /*------------------------------------
                 			sequencingRule
                -------------------------------------*/
                if (emt.getName().equals("sequencingRules")) {
                    List seqRuleEmts = emt.getChildren();

                    for (int p=0;p<seqRuleEmts.size();p++) {
                        Element seqRuleEmt = (Element)seqRuleEmts.get(p);
                        if (seqRuleEmt.getName().equals("preConditionRule")) {
                            /* 1th sequencingRules ConditionRule */
                            vSeqRuleSeqMkey1.add(Integer.toString(intSeqIndex));        //Sequence Index
                            vSeqRuleContMkey1.add(Integer.toString(intSeqRuleContCnt)); //ConditionRule Index
                            vTSeqType.add("pre");                                       //type key
                            vTSeqcondiCombination.add("");
                            if (seqRuleEmt.getChild("ruleConditions",imsssNameSpace) != null) {
                                if (seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getAttribute("conditionCombination") != null) {
                                    vTSeqcondiCombination.removeElementAt(vTSeqcondiCombination.lastIndexOf(vTSeqcondiCombination.lastElement()));
                                    vTSeqcondiCombination.add(seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getAttributeValue("conditionCombination"));
                                }
                                /* 2nd sequencingRule ruleCondition */
                                List ruleList = seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getChildren();
                                int intSeqRuleContDetailCnt = 0;

                                for (int i = 0; i<ruleList.size(); i++) {

                                    Element ruleListEmt = (Element)ruleList.get(i);

                                    if (ruleListEmt.getName().equals("ruleCondition")) {

                                        vSeqRuleSeqMkey2.add(Integer.toString(intSeqIndex));                   	//Sequence Index
                                        vSeqRuleContMkey2.add(Integer.toString(intSeqRuleContCnt));         	//ConditionRule Index
                                        vSeqRuleContDetailKey.add(Integer.toString(intSeqRuleContDetailCnt));   //ruleCondition Index

                                        vTSeqrefObjective.add("");
                                        vTSeqmeaThreshold.add("");
                                        vTSeqoperator.add("");
                                        vTSeqcondition.add("");

                                        if (ruleListEmt.getAttribute("referencedObjective") != null) {

                                            vTSeqrefObjective.removeElementAt(vTSeqrefObjective.lastIndexOf(vTSeqrefObjective.lastElement()));
                                            vTSeqrefObjective.add(ruleListEmt.getAttributeValue("referencedObjective"));
                                        }
                                        if (ruleListEmt.getAttribute("measureThreshold") != null ) {

                                            vTSeqmeaThreshold.removeElementAt(vTSeqmeaThreshold.lastIndexOf(vTSeqmeaThreshold.lastElement()));
                                            vTSeqmeaThreshold.add(ruleListEmt.getAttributeValue("measureThreshold"));
                                        }
                                        if (ruleListEmt.getAttribute("operator") != null ) {

                                            vTSeqoperator.removeElementAt(vTSeqoperator.lastIndexOf(vTSeqoperator.lastElement()));
                                            vTSeqoperator.add(ruleListEmt.getAttributeValue("operator"));
                                        }
                                        if (ruleListEmt.getAttribute("condition") != null ) {

                                            vTSeqcondition.removeElementAt(vTSeqcondition.lastIndexOf(vTSeqcondition.lastElement()));
                                            vTSeqcondition.add(ruleListEmt.getAttributeValue("condition"));
                                        }
                                        intSeqRuleContDetailCnt++;
                                    }
                                }

                            }

                            vTSeqAction.add("");
                            if (seqRuleEmt.getChild("ruleAction",imsssNameSpace) != null) {

                                if (seqRuleEmt.getChild("ruleAction",imsssNameSpace).getAttribute("action") != null) {

                                    vTSeqAction.removeElementAt(vTSeqAction.lastIndexOf(vTSeqAction.lastElement()));
                                    vTSeqAction.add(seqRuleEmt.getChild("ruleAction",imsssNameSpace).getAttributeValue("action"));
                                }
                            }
                            intSeqRuleContCnt++;
                        }//preConditionRule End

                        if (seqRuleEmt.getName().equals("exitConditionRule")) {

                            /* 1th sequencingRules ConditionRule */
                            vSeqRuleSeqMkey1.add(Integer.toString(intSeqIndex));        //Sequence Index
                            vSeqRuleContMkey1.add(Integer.toString(intSeqRuleContCnt)); //ConditionRule Index
                            vTSeqType.add("exit");                                      //type key

                            vTSeqcondiCombination.add("");
                            if (seqRuleEmt.getChild("ruleConditions",imsssNameSpace) != null) {

                                if (seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getAttribute("conditionCombination") != null) {
                                    vTSeqcondiCombination.removeElementAt(vTSeqcondiCombination.lastIndexOf(vTSeqcondiCombination.lastElement()));
                                    vTSeqcondiCombination.add(seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getAttributeValue("conditionCombination"));
                                }

                                /* 2nd sequencingRule ruleCondition */
                                List ruleList = seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getChildren();
                                int intSeqRuleContDetailCnt = 0;

                                for (int i = 0; i<ruleList.size(); i++) {

                                    Element ruleListEmt = (Element)ruleList.get(i);

                                    if(ruleListEmt.getName().equals("ruleCondition")) {

                                        vSeqRuleSeqMkey2.add(Integer.toString(intSeqIndex));                    //Sequence Index
                                        vSeqRuleContMkey2.add(Integer.toString(intSeqRuleContCnt));         	//ConditionRule Index
                                        vSeqRuleContDetailKey.add(Integer.toString(intSeqRuleContDetailCnt));   //ruleCondition Index

                                        vTSeqrefObjective.add("");
                                        vTSeqmeaThreshold.add("");
                                        vTSeqoperator.add("");
                                        vTSeqcondition.add("");

                                        if(ruleListEmt.getAttribute("referencedObjective") != null)
                                        {
                                            vTSeqrefObjective.removeElementAt(vTSeqrefObjective.lastIndexOf(vTSeqrefObjective.lastElement()));
                                            vTSeqrefObjective.add(ruleListEmt.getAttributeValue("referencedObjective"));
                                        }
                                        if(ruleListEmt.getAttribute("measureThreshold") != null )
                                        {
                                            vTSeqmeaThreshold.removeElementAt(vTSeqmeaThreshold.lastIndexOf(vTSeqmeaThreshold.lastElement()));
                                            vTSeqmeaThreshold.add(ruleListEmt.getAttributeValue("measureThreshold"));
                                        }
                                        if(ruleListEmt.getAttribute("operator") != null )
                                        {
                                            vTSeqoperator.removeElementAt(vTSeqoperator.lastIndexOf(vTSeqoperator.lastElement()));
                                            vTSeqoperator.add(ruleListEmt.getAttributeValue("operator"));
                                        }
                                        if(ruleListEmt.getAttribute("condition") != null )
                                        {
                                            vTSeqcondition.removeElementAt(vTSeqcondition.lastIndexOf(vTSeqcondition.lastElement()));
                                            vTSeqcondition.add(ruleListEmt.getAttributeValue("condition"));
                                        }
                                        intSeqRuleContDetailCnt++;
                                    }
                                }

                            }

                            vTSeqAction.add("");
                            if(seqRuleEmt.getChild("ruleAction",imsssNameSpace) != null)
                            {
                                if(seqRuleEmt.getChild("ruleAction",imsssNameSpace).getAttribute("action") != null)
                                {
                                    vTSeqAction.removeElementAt(vTSeqAction.lastIndexOf(vTSeqAction.lastElement()));
                                    vTSeqAction.add(seqRuleEmt.getChild("ruleAction",imsssNameSpace).getAttributeValue("action"));
                                }
                            }

                            intSeqRuleContCnt++;
                        }//exitConditionRule End

                        if(seqRuleEmt.getName().equals("postConditionRule"))
                        {
                            /* 1th sequencingRules ConditionRule */
                            vSeqRuleSeqMkey1.add(Integer.toString(intSeqIndex));        //Sequence Index
                            vSeqRuleContMkey1.add(Integer.toString(intSeqRuleContCnt)); //ConditionRule Index
                            vTSeqType.add("post");                                      //type key

                            vTSeqcondiCombination.add("");
                            if(seqRuleEmt.getChild("ruleConditions",imsssNameSpace) != null)
                            {
                                if(seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getAttribute("conditionCombination") != null)
                                {
                                    vTSeqcondiCombination.removeElementAt(vTSeqcondiCombination.lastIndexOf(vTSeqcondiCombination.lastElement()));
                                    vTSeqcondiCombination.add(seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getAttributeValue("conditionCombination"));
                                }

                                /* 2nd sequencingRule ruleCondition */
                                List ruleList = seqRuleEmt.getChild("ruleConditions",imsssNameSpace).getChildren();
                                int intSeqRuleContDetailCnt = 0;

                                for(int i = 0; i<ruleList.size(); i++)
                                {
                                    Element ruleListEmt = (Element)ruleList.get(i);

                                    if(ruleListEmt.getName().equals("ruleCondition"))
                                    {
                                        vSeqRuleSeqMkey2.add(Integer.toString(intSeqIndex));                	//Sequence Index
                                        vSeqRuleContMkey2.add(Integer.toString(intSeqRuleContCnt));         	//ConditionRule Index
                                        vSeqRuleContDetailKey.add(Integer.toString(intSeqRuleContDetailCnt));   //ruleCondition Index

                                        vTSeqrefObjective.add("");
                                        vTSeqmeaThreshold.add("");
                                        vTSeqoperator.add("");
                                        vTSeqcondition.add("");

                                        if(ruleListEmt.getAttribute("referencedObjective") != null)
                                        {
                                            vTSeqrefObjective.removeElementAt(vTSeqrefObjective.lastIndexOf(vTSeqrefObjective.lastElement()));
                                            vTSeqrefObjective.add(ruleListEmt.getAttributeValue("referencedObjective"));
                                        }
                                        if(ruleListEmt.getAttribute("measureThreshold") != null )
                                        {
                                            vTSeqmeaThreshold.removeElementAt(vTSeqmeaThreshold.lastIndexOf(vTSeqmeaThreshold.lastElement()));
                                            vTSeqmeaThreshold.add(ruleListEmt.getAttributeValue("measureThreshold"));
                                        }
                                        if(ruleListEmt.getAttribute("operator") != null )
                                        {
                                            vTSeqoperator.removeElementAt(vTSeqoperator.lastIndexOf(vTSeqoperator.lastElement()));
                                            vTSeqoperator.add(ruleListEmt.getAttributeValue("operator"));
                                        }
                                        if(ruleListEmt.getAttribute("condition") != null )
                                        {
                                            vTSeqcondition.removeElementAt(vTSeqcondition.lastIndexOf(vTSeqcondition.lastElement()));
                                            vTSeqcondition.add(ruleListEmt.getAttributeValue("condition"));
                                        }
                                        intSeqRuleContDetailCnt++;
                                    }
                                }
                            }

                            vTSeqAction.add("");
                            if(seqRuleEmt.getChild("ruleAction",imsssNameSpace) != null)
                            {
                                if(seqRuleEmt.getChild("ruleAction",imsssNameSpace).getAttribute("action") != null)
                                {
                                    vTSeqAction.removeElementAt(vTSeqAction.lastIndexOf(vTSeqAction.lastElement()));
                                    vTSeqAction.add(seqRuleEmt.getChild("ruleAction",imsssNameSpace).getAttributeValue("action"));
                                }
                            }
                            intSeqRuleContCnt++;
                        }//postCondiditonRule End

                    }//for End
                }//sequencingRule End

                /*------------------------------------
                 			limitConditions
                -------------------------------------*/
                if(emt.getName().equals("limitConditions"))
                {
                    if(emt.getAttribute("attemptLimit") != null)
                    {
                        vSeqAttemptLimit.removeElementAt(intSeqIndex);
                        vSeqAttemptLimit.add(intSeqIndex, emt.getAttributeValue("attemptLimit"));
                    }
                    if(emt.getAttribute("attemptAbsoluteDurationLimit") != null)
                    {
                        vSeqAttemptDurationLimit.removeElementAt(intSeqIndex);
                        vSeqAttemptDurationLimit.add(intSeqIndex, emt.getAttributeValue("attemptAbsoluteDurationLimit"));
                    }
                }

                /*------------------------------------
                 			rollupRules
                -------------------------------------*/
                if(emt.getName().equals("rollupRules"))
                {
                    if(emt.getAttribute("rollupObjectiveSatisfied") != null)
                    {
                        vSeqRollupObjSatisfied.removeElementAt(intSeqIndex);
                        vSeqRollupObjSatisfied.add(intSeqIndex, emt.getAttributeValue("rollupObjectiveSatisfied"));
                    }
                    if(emt.getAttribute("rollupProgressCompletion") != null)
                    {

                        vSeqRollupProgressComplet.removeElementAt(intSeqIndex);
                        vSeqRollupProgressComplet.add(intSeqIndex, emt.getAttributeValue("rollupProgressCompletion"));
                    }
                    if(emt.getAttribute("objectiveMeasureWeight") != null)
                    {
                        vSeqObjMeasureWeight.removeElementAt(intSeqIndex);
                        vSeqObjMeasureWeight.add(intSeqIndex, emt.getAttributeValue("objectiveMeasureWeight"));
                    }

                    /* 1th rollupRules rollupRule */
                    List rollList = emt.getChildren();

                    for(int i = 0; i<rollList.size(); i++)
                    {
                        Element rollListEmt = (Element)rollList.get(i);

                        if(rollListEmt.getName().equals("rollupRule"))
                        {
                            vRollUpSeqMkey1.add(Integer.toString(intSeqIndex));      	//Sequence Index
                            vRollUpContMkey1.add(Integer.toString(intRollUpContCnt));   //rollupRule Index

                            vSeqChildActivitySet.add("");
                            vSeqMinimumCount.add("");
                            vSeqMinimumPercent.add("");
                            if(rollListEmt.getAttribute("childActivitySet") != null)
                            {
                                vSeqChildActivitySet.removeElementAt(vSeqChildActivitySet.lastIndexOf(vSeqChildActivitySet.lastElement()));
                                vSeqChildActivitySet.add(rollListEmt.getAttributeValue("childActivitySet"));
                            }
                            if(rollListEmt.getAttribute("minimumCount") != null)
                            {
                                vSeqMinimumCount.removeElementAt(vSeqMinimumCount.lastIndexOf(vSeqMinimumCount.lastElement()));
                                vSeqMinimumCount.add(rollListEmt.getAttributeValue("minimumCount"));
                            }
                            if(rollListEmt.getAttribute("minimumPercent") != null)
                            {
                                vSeqMinimumPercent.removeElementAt(vSeqMinimumPercent.lastIndexOf(vSeqMinimumPercent.lastElement()));
                                vSeqMinimumPercent.add(rollListEmt.getAttributeValue("minimumPercent"));
                            }
                            // attribute
                            vSeqRollupConditionComb.add("");
                            if(rollListEmt.getChild("rollupConditions",imsssNameSpace) != null)
                            {
                                if(rollListEmt.getChild("rollupConditions",imsssNameSpace).getAttribute("conditionCombination") != null)
                                {
                                    vSeqRollupConditionComb.removeElementAt(vSeqRollupConditionComb.lastIndexOf(vSeqRollupConditionComb.lastElement()));
                                    vSeqRollupConditionComb.add(rollListEmt.getChild("rollupConditions",imsssNameSpace).getAttributeValue("conditionCombination"));
                                }

                                /* 2nd rollupRules rollupCondition */
                                List CondList = rollListEmt.getChild("rollupConditions",imsssNameSpace).getChildren();

                                int intRollUpContDetailCnt = 0;

                                for(int j = 0; j<CondList.size(); j++)
                                {
                                    Element CondListEmt = (Element)CondList.get(j);

                                    if(CondListEmt.getName().equals("rollupCondition"))
                                    {
                                        vRollUpSeqMkey2.add(Integer.toString(intSeqIndex));                 //Sequence Index
                                        vRollUpContMkey2.add(Integer.toString(intRollUpContCnt));           //rollupRule Index
                                        vRollUpContDetailKey.add(Integer.toString(intRollUpContDetailCnt)); //rollupCondition Index

                                        vSeqRollupOperator.add("");
                                        vSeqRollupCondition.add("");
                                        if(CondListEmt.getAttribute("operator") != null)
                                        {
                                            vSeqRollupOperator.removeElementAt(vSeqRollupOperator.lastIndexOf(vSeqRollupOperator.lastElement()));
                                            vSeqRollupOperator.add(CondListEmt.getAttributeValue("operator"));
                                        }
                                        if(CondListEmt.getAttribute("condition") != null)
                                        {
                                            vSeqRollupCondition.removeElementAt(vSeqRollupCondition.lastIndexOf(vSeqRollupCondition.lastElement()));
                                            vSeqRollupCondition.add(CondListEmt.getAttributeValue("condition"));
                                        }
                                        intRollUpContDetailCnt++;
                                    }
                                }
                            }

                            vSeqRollupAction.add("");
                            if(rollListEmt.getChild("rollupAction",imsssNameSpace) != null)
                            {
                                if(rollListEmt.getChild("rollupAction",imsssNameSpace).getAttribute("action") != null)
                                {
                                    vSeqRollupAction.removeElementAt(vSeqRollupAction.lastIndexOf(vSeqRollupAction.lastElement()));
                                    vSeqRollupAction.add(rollListEmt.getChild("rollupAction",imsssNameSpace).getAttributeValue("action"));
                                }
                            }
                            intRollUpContCnt++;
                        }// 1th if End
                    } // 1th for End
                } // rollupRules End


                /*------------------------------------
                 			Objectives
                -------------------------------------*/
                if(emt.getName().equals("objectives"))
                {

                    List seqObjEmts = emt.getChildren();

                    for(int p=0;p<seqObjEmts.size();p++)
                    {
                        Element seqObjEmt = (Element)seqObjEmts.get(p);

                        if(seqObjEmt.getName().equals("primaryObjective"))
                        {
                            /* 1th Objectives Objective */
                            vObjectivesSeqMkey1.add(Integer.toString(intSeqIndex));             //Sequence Index
                            vObjectivesContMkey1.add(Integer.toString(intObjectivesContCnt));   //objective Index
                            vSeqObjType.add("pr");                                              //primaryObjective/objective Type

                            vSeqSatisfiedByMeasure.add("");
                            vSeqObjID.add("");
                            if(seqObjEmt.getAttribute("satisfiedByMeasure") != null)
                            {
                                vSeqSatisfiedByMeasure.removeElementAt(vSeqSatisfiedByMeasure.lastIndexOf(vSeqSatisfiedByMeasure.lastElement()));
                                vSeqSatisfiedByMeasure.add(seqObjEmt.getAttributeValue("satisfiedByMeasure"));
                            }
                            if(seqObjEmt.getAttribute("objectiveID") != null)
                            {
                                vSeqObjID.removeElementAt(vSeqObjID.lastIndexOf(vSeqObjID.lastElement()));
                                vSeqObjID.add(seqObjEmt.getAttributeValue("objectiveID"));
                            }
                            // attribute
                            vSeqminNormalMeasure.add("");
                            if(seqObjEmt.getChild("minNormalizedMeasure",imsssNameSpace) != null)
                            {
                                vSeqminNormalMeasure.removeElementAt(vSeqminNormalMeasure.lastIndexOf(vSeqminNormalMeasure.lastElement()));
                                vSeqminNormalMeasure.add(seqObjEmt.getChild("minNormalizedMeasure",imsssNameSpace).getText());
                            }

                            /* 2nd Objectives mapInfo */
                            List objList = seqObjEmt.getChildren();

                            int intObjectivesContDetail = 0;

                            for(int i = 0; i<objList.size(); i++)
                            {
                                Element objListEmt = (Element)objList.get(i);

                                if(objListEmt.getName().equals("mapInfo"))
                                {
                                    vObjectivesSeqMkey2.add(Integer.toString(intSeqIndex));                     //Sequence Index
                                    vObjectivesContMkey2.add(Integer.toString(intObjectivesContCnt));           //objective Index
                                    vObjectivesContDetailKey.add(Integer.toString(intObjectivesContDetail));    //mapInfo Index


                                    vSeqTargetObjID.add("");
                                    vSeqReadSatisStatus.add("");
                                    vSeqReadNormMeasure.add("");
                                    vSeqWriteSatisStatus.add("");
                                    vSeqWriteNormalMeasure.add("");

                                    if(objListEmt.getAttribute("targetObjectiveID") != null)
                                    {
                                        vSeqTargetObjID.removeElementAt(vSeqTargetObjID.lastIndexOf(vSeqTargetObjID.lastElement()));
                                        vSeqTargetObjID.add(objListEmt.getAttributeValue("targetObjectiveID"));
                                    }
                                    if(objListEmt.getAttribute("readSatisfiedStatus") != null)
                                    {
                                        vSeqReadSatisStatus.removeElementAt(vSeqReadSatisStatus.lastIndexOf(vSeqReadSatisStatus.lastElement()));
                                        vSeqReadSatisStatus.add(objListEmt.getAttributeValue("readSatisfiedStatus"));
                                    }
                                    if(objListEmt.getAttribute("readNormalizedMeasure") != null)
                                    {
                                        vSeqReadNormMeasure.removeElementAt(vSeqReadNormMeasure.lastIndexOf(vSeqReadNormMeasure.lastElement()));
                                        vSeqReadNormMeasure.add(objListEmt.getAttributeValue("readNormalizedMeasure"));
                                    }
                                    if(objListEmt.getAttribute("writeSatisfiedStatus") != null)
                                    {
                                        vSeqWriteSatisStatus.removeElementAt(vSeqWriteSatisStatus.lastIndexOf(vSeqWriteSatisStatus.lastElement()));
                                        vSeqWriteSatisStatus.add(objListEmt.getAttributeValue("writeSatisfiedStatus"));
                                    }
                                    if(objListEmt.getAttribute("writeNormalizedMeasure") != null)
                                    {
                                        vSeqWriteNormalMeasure.removeElementAt(vSeqWriteNormalMeasure.lastIndexOf(vSeqWriteNormalMeasure.lastElement()));
                                        vSeqWriteNormalMeasure.add(objListEmt.getAttributeValue("writeNormalizedMeasure"));
                                    }
                                    intObjectivesContDetail++;
                                }
                            }
                            intObjectivesContCnt++;
                        }


                        if(seqObjEmt.getName().equals("objective"))
                        {
                            /* 1th Objectives Objective */
                            vObjectivesSeqMkey1.add(Integer.toString(intSeqIndex));             //Sequence Index
                            vObjectivesContMkey1.add(Integer.toString(intObjectivesContCnt));   //objective Index
                            vSeqObjType.add("ob");                                              //primaryObjective/objective Type

                            vSeqSatisfiedByMeasure.add("");
                            vSeqObjID.add("");
                            if(seqObjEmt.getAttribute("satisfiedByMeasure") != null)
                            {
                                vSeqSatisfiedByMeasure.removeElementAt(vSeqSatisfiedByMeasure.lastIndexOf(vSeqSatisfiedByMeasure.lastElement()));
                                vSeqSatisfiedByMeasure.add(seqObjEmt.getAttributeValue("satisfiedByMeasure"));
                            }
                            if(seqObjEmt.getAttribute("objectiveID") != null)
                            {
                                vSeqObjID.removeElementAt(vSeqObjID.lastIndexOf(vSeqObjID.lastElement()));
                                vSeqObjID.add(seqObjEmt.getAttributeValue("objectiveID"));
                            }
                            // attribute
                            vSeqminNormalMeasure.add("");
                            if(seqObjEmt.getChild("minNormalizedMeasure",imsssNameSpace) != null)
                            {
                                vSeqminNormalMeasure.removeElementAt(vSeqminNormalMeasure.lastIndexOf(vSeqminNormalMeasure.lastElement()));
                                vSeqminNormalMeasure.add(seqObjEmt.getChild("minNormalizedMeasure",imsssNameSpace).getText());
                            }

                            /* 2nd Objectives mapInfo */
                            List objList = seqObjEmt.getChildren();

                            int intObjectivesContDetail = 0;

                            for(int i = 0; i<objList.size(); i++)
                            {
                                Element objListEmt = (Element)objList.get(i);

                                if(objListEmt.getName().equals("mapInfo"))
                                {
                                    vObjectivesSeqMkey2.add(Integer.toString(intSeqIndex));                     //Sequence Index
                                    vObjectivesContMkey2.add(Integer.toString(intObjectivesContCnt));           //objective Index
                                    vObjectivesContDetailKey.add(Integer.toString(intObjectivesContDetail));    //mapInfo Index

                                    vSeqTargetObjID.add("");
                                    vSeqReadSatisStatus.add("");
                                    vSeqReadNormMeasure.add("");
                                    vSeqWriteSatisStatus.add("");
                                    vSeqWriteNormalMeasure.add("");

                                    if(objListEmt.getAttribute("targetObjectiveID") != null)
                                    {
                                        vSeqTargetObjID.removeElementAt(vSeqTargetObjID.lastIndexOf(vSeqTargetObjID.lastElement()));
                                        vSeqTargetObjID.add(objListEmt.getAttributeValue("targetObjectiveID"));
                                    }
                                    if(objListEmt.getAttribute("readSatisfiedStatus") != null)
                                    {
                                        vSeqReadSatisStatus.removeElementAt(vSeqReadSatisStatus.lastIndexOf(vSeqReadSatisStatus.lastElement()));
                                        vSeqReadSatisStatus.add(objListEmt.getAttributeValue("readSatisfiedStatus"));
                                    }
                                    if(objListEmt.getAttribute("readNormalizedMeasure") != null)
                                    {
                                        vSeqReadNormMeasure.removeElementAt(vSeqReadNormMeasure.lastIndexOf(vSeqReadNormMeasure.lastElement()));
                                        vSeqReadNormMeasure.add(objListEmt.getAttributeValue("readNormalizedMeasure"));
                                    }
                                    if(objListEmt.getAttribute("writeSatisfiedStatus") != null)
                                    {
                                        vSeqWriteSatisStatus.removeElementAt(vSeqWriteSatisStatus.lastIndexOf(vSeqWriteSatisStatus.lastElement()));
                                        vSeqWriteSatisStatus.add(objListEmt.getAttributeValue("writeSatisfiedStatus"));
                                    }
                                    if(objListEmt.getAttribute("writeNormalizedMeasure") != null)
                                    {
                                        vSeqWriteNormalMeasure.removeElementAt(vSeqWriteNormalMeasure.lastIndexOf(vSeqWriteNormalMeasure.lastElement()));
                                        vSeqWriteNormalMeasure.add(objListEmt.getAttributeValue("writeNormalizedMeasure"));
                                    }
                                    intObjectivesContDetail++;
                                }
                            }
                            intObjectivesContCnt++;
                        }// objective End

                    }// for End

                }// Objectives End


                /*------------------------------------
                 		randomizationControls
                -------------------------------------*/
                if(emt.getName().equals("randomizationControls"))
                {
                    if(emt.getAttribute("randomizationTiming") != null)
                    {
                        vSeqRandomTiming.removeElementAt(intSeqIndex);
                        vSeqRandomTiming.add(intSeqIndex, emt.getAttributeValue("randomizationTiming"));
                    }
                    if(emt.getAttribute("selectCount") != null)
                    {
                        vSeqSelectCount.removeElementAt(intSeqIndex);
                        vSeqSelectCount.add(intSeqIndex, emt.getAttributeValue("selectCount"));
                    }
                    if(emt.getAttribute("reorderChildren") != null)
                    {
                        vSeqReorderChildren.removeElementAt(intSeqIndex);
                        vSeqReorderChildren.add(intSeqIndex, emt.getAttributeValue("reorderChildren"));
                    }
                    if(emt.getAttribute("selectionTiming") != null)
                    {
                        vSeqSelectionTiming.removeElementAt(intSeqIndex);
                        vSeqSelectionTiming.add(intSeqIndex, emt.getAttributeValue("selectionTiming"));
                    }
                }

                /*------------------------------------
                 			deliveryControls
                -------------------------------------*/
                if(emt.getName().equals("deliveryControls"))
                {
                    if(emt.getAttribute("tracked") != null)
                    {
                        vSeqTracked.removeElementAt(intSeqIndex);
                        vSeqTracked.add(intSeqIndex, emt.getAttributeValue("tracked"));
                    }
                    if(emt.getAttribute("completionSetByContent") != null)
                    {
                        vSeqCompletSetByContent.removeElementAt(intSeqIndex);
                        vSeqCompletSetByContent.add(intSeqIndex, emt.getAttributeValue("completionSetByContent"));
                    }
                    if(emt.getAttribute("objectiveSetByContent") != null)
                    {
                        vSeqObjSetByContent.removeElementAt(intSeqIndex);
                        vSeqObjSetByContent.add(intSeqIndex, emt.getAttributeValue("objectiveSetByContent"));
                    }

                }

                /*------------------------------------
                 	constrainedChoiceConsiderations
                -------------------------------------*/
                if(emt.getName().equals("constrainedChoiceConsiderations"))
                {
                    if(emt.getAttribute("preventActivation") != null)
                    {
                        vSeqPreventActivation.removeElementAt(intSeqIndex);
                        vSeqPreventActivation.add(intSeqIndex, emt.getAttributeValue("preventActivation"));
                    }
                    if(emt.getAttribute("constrainChoice") != null)
                    {
                        vSeqConstrainChoice.removeElementAt(intSeqIndex);
                        vSeqConstrainChoice.add(intSeqIndex, emt.getAttributeValue("constrainChoice"));
                    }

                }

                /*------------------------------------
                 		rollupConsiderations
                -------------------------------------*/
                if(emt.getName().equals("rollupConsiderations"))
                {
                    if(emt.getAttribute("requiredForSatisfied") != null)
                    {
                        vSeqReqSatisfied.removeElementAt(intSeqIndex);
                        vSeqReqSatisfied.add(intSeqIndex, emt.getAttributeValue("requiredForSatisfied"));
                    }
                    if(emt.getAttribute("requiredForNotSatisfied") != null)
                    {
                        vSeqReqNotSatisfied.removeElementAt(intSeqIndex);
                        vSeqReqNotSatisfied.add(intSeqIndex, emt.getAttributeValue("requiredForNotSatisfied"));
                    }
                    if(emt.getAttribute("requiredForCompleted") != null)
                    {
                        vSeqReqCompleted.removeElementAt(intSeqIndex);
                        vSeqReqCompleted.add(intSeqIndex, emt.getAttributeValue("requiredForCompleted"));
                    }
                    if(emt.getAttribute("requiredForIncomplete") != null)
                    {
                        vSeqReqIncomplete.removeElementAt(intSeqIndex);
                        vSeqReqIncomplete.add(intSeqIndex, emt.getAttributeValue("requiredForIncomplete"));
                    }
                    if(emt.getAttribute("measureSatisfactionIfActive") != null)
                    {
                        vSeqMeasureSatIfActive.removeElementAt(intSeqIndex);
                        vSeqMeasureSatIfActive.add(intSeqIndex, emt.getAttributeValue("measureSatisfactionIfActive"));
                    }
                }
        }

    }//Method getSeqNav() End

    /**
	 * 리소스 정보를 파싱
	 * @param resourcesEmt
	 */
    public void getResource(Element resourcesEmt, String msgId) {

    	ImportMsg msgHandler = null;
    	if (msgId != null) msgHandler = ImportMsg.getInstance();
        List resourceEmts=null;

        resourceEmts = resourcesEmt.getChildren();

        //Resources Element
        for (int i=0;i<resourceEmts.size();i++) {
        	if (msgId != null) msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_PARSE_RESOURCE);
        	Element resourceEmt = (Element)resourceEmts.get(i);

    		vRsrcNoMkey.add(Integer.toString(i));	//Resource No Index
    		if (resourceEmt.getAttribute("identifier") != null) {
    			vRsrcIdentifier.add(resourceEmt.getAttributeValue("identifier"));
    		}else {
    			if (msgId != null) msgHandler.setError(msgId, ImportMsg.WARN_MANI_PARSE_NONE_RESOURCE_IDENTIFIER);
    		}

            vRsrcType.add("");
            vRsrcHref.add("");
            vRsrcXmlBase.add("");
            vRsrcScormType.add("");
            vRsrcPersistState.add("");
            vRsrcMetadata.add("");
            //Resource Attribute
            List resourceAttrs = resourceEmt.getAttributes();
            String strRsrcFileType = "";
            for (int j=0;j<resourceAttrs.size();j++) {
                if (((Attribute)resourceAttrs.get(j)).getName().equals("type")) {
                    vRsrcType.removeElementAt(vRsrcType.lastIndexOf(vRsrcType.lastElement()));
                    vRsrcType.add(((Attribute)resourceAttrs.get(j)).getValue());
                }else if (((Attribute)resourceAttrs.get(j)).getName().equals("href")) {
                    vRsrcHref.removeElementAt(vRsrcHref.lastIndexOf(vRsrcHref.lastElement()));
                    vRsrcHref.add(((Attribute)resourceAttrs.get(j)).getValue());

                }else if (((Attribute)resourceAttrs.get(j)).getName().equals("base")) {
                    vRsrcXmlBase.removeElementAt(vRsrcXmlBase.lastIndexOf(vRsrcXmlBase.lastElement()));
                    vRsrcXmlBase.add(((Attribute)resourceAttrs.get(j)).getValue());

                }else if (((Attribute)resourceAttrs.get(j)).getName().equals("scormType")) {
                    vRsrcScormType.removeElementAt(vRsrcScormType.lastIndexOf(vRsrcScormType.lastElement()));
                    vRsrcScormType.add(((Attribute)resourceAttrs.get(j)).getValue());
                    strRsrcFileType = ((Attribute)resourceAttrs.get(j)).getValue();

                }else if (((Attribute)resourceAttrs.get(j)).getName().equals("persistState")) {
                    vRsrcPersistState.removeElementAt(vRsrcPersistState.lastIndexOf(vRsrcPersistState.lastElement()));
                    vRsrcPersistState.add(((Attribute)resourceAttrs.get(j)).getValue());
                }

            }

            //Resource Child Element
            List rsrcChildEmts = resourceEmt.getChildren();

            //Dependency No
        	int intDependNo = 0;

            for (int j=0; j<rsrcChildEmts.size(); j++) {

                Element rsrcChildEmt = (Element)rsrcChildEmts.get(j);

                if (rsrcChildEmt.getName().equals("file")) {

                	vRsrcNoFileMkey.add(Integer.toString(i));						//Resource File Resource No Index
                	vRsrcFileType.add(strRsrcFileType);								//File Type ("sco","asset")
                	vRsrcFileHref.add(rsrcChildEmt.getAttributeValue("href"));//File Href
                }

                if (rsrcChildEmt.getName().equals("dependency")) {

                	vRsrcNoDependeceMkey.add(Integer.toString(i));				//Resource Dependency Resource No Index
                	vRsrcDependenceNoMkey.add(Integer.toString(intDependNo));	//Resource Dependency No Index
                	vRsrcDepIdentifierRef.add(rsrcChildEmt.getAttributeValue("identifierref"));

                	intDependNo++;
                }
                
                if (rsrcChildEmt.getName().equals("metadata")){
                    List RercMetaEmts = rsrcChildEmt.getChildren();
                    for(int k=0; j<RercMetaEmts.size(); j++){
                        Element RsrcMetaEmt = (Element)RercMetaEmts.get(k);
                        if(RsrcMetaEmt.getName().equals("location")){
                        	vRsrcMetadata.removeElementAt(vRsrcMetadata.lastIndexOf(vRsrcMetadata.lastElement()));
                        	vRsrcMetadata.add(RsrcMetaEmt.getText());
                        }
                    }

                }
            }//rsrcChildEmt For End

        }//resourceEmts For End

    }//getResource End


	/**
	 * imsmanifest.xml 에서 주석부분의 MODEL,OPTION,COUNT
	 * 의 Element 값 Return.
	 * @author shocky
	 * @param fileName    xml 파일명(경로)
	 * @return 해당 Element Value Return.
	 */
    public void setElementStr(String fileName)
	{
		//Vector retVal  = new Vector();
		//String pData   = "";
		String findElement1 = "MODEL";
		String findElement2 = "OPTION";
		String findElement3 = "COUNT";
		int startindex = 0;
		int endindex   = 0;
		try
		{
			File file = new File(fileName);

			FileReader reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			String string;
			while ((string = in.readLine()) != null)
			{

				if(string.indexOf("<organization ") != -1)
				{
					vComModel.add("");
					vComOption.add("");
					vComCount.add("");
				}

				startindex = string.indexOf("<"+findElement1+">");			//MODEL Element 는 Organization 당 하나씩 존재한다는 전제조건.
				if(startindex != -1)
				{

					endindex   = string.indexOf("</"+findElement1+">");

					vComModel.removeElementAt(vComModel.lastIndexOf(vComModel.lastElement()));
					vComModel.add(string.substring(startindex+findElement1.length()+2, endindex));

					startindex = string.indexOf("<"+findElement2+">");
					if(startindex != -1)
					{
						endindex   = string.indexOf("</"+findElement2+">");
						//vComOption.removeElementAt(vComOption.lastIndexOf(vComOption.lastElement()));
						vComOption.removeElementAt(vComOption.size()-1);
						vComOption.add(string.substring(startindex+findElement2.length()+2, endindex));
					}

					startindex = string.indexOf("<"+findElement3+">");
					if(startindex != -1)
					{
						endindex   = string.indexOf("</"+findElement3+">");
						//vComCount.removeElementAt(vComCount.lastIndexOf(vComCount.lastElement()));
						vComCount.removeElementAt(vComCount.size()-1);
						vComCount.add(string.substring(startindex+findElement3.length()+2, endindex));
					}
				}
			}
			in.close();

		}
		catch (IOException e)
		{
	/*		CmLog.writeLog(	"Log Name   : IOException",
							"Class Name : "+getClass().getName(),
							"Method Name: getElementStr()",
							"Exception  :\n        "+e,
							"",
							"elearn_"
						  );
	*/	}
		catch(Exception e)
		{
	/*		CmLog.writeLog(	"Log Name   : Exception",
							"Class Name : "+getClass().getName(),
							"Method Name: getElementStr()",
							"Exception  :\n        "+e,
							"",
							"elearn_"
						  );
	*/	}

	}


    /**
	 * Manifest ID return
	 * @return manifest ID
	 */
    public String getManifestID() {

        return this.strManifestID;
    }


    /**
	 * Manifest meta data location return
	 * @return manifest's metadata location
	 */
    public String getManifestMeta() {

        return this.strManifestMeta;
    }


    /**
	 * Organization default return
	 * @return	organization default
	 */
    public String getOrgsDefault() {

        return this.strOrgsDefault;
    }


    /**
	 * LCMS_ORGANIZATION Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getOrgValue() {

        int row_count = vOrgIdentifier.size();
        String[][] table = new String[row_count][8];

        for(int i = 0; i<row_count; i++) {

            table[i][0] = Integer.toString(i);                  				//Organization Index
            table[i][1] = (String)vOrgIdentifier.get(i);        				//Organization Id
            table[i][2] = (String)vOrgTitle.get(i);             				//Oragnization Title
            table[i][3] = (String)vOrgStructure.get(i);         				//Oragnization Structure
            table[i][4] = (String)vOrgGlobalSystem.get(i);      				//Oragnization GlobalSystem

			// 2005.3.19 linuzer, 변경
            //table[i][5] = (String)vOrgMeta.get(i);              				//Oragnization Meta Data

			if((String)vOrgMeta.get(i) != null && !((String)vOrgMeta.get(i)).equals("")) {
				table[i][5] = (String)vOrgMeta.get(i);              	//Oragnization Meta Data
			}

            //table[i][6] = (String)vComModel.get(i)+(String)vComOption.get(i);	//Comment MODEL+OPTION
            //table[i][7] = (String)vComCount.get(i);             				//Comment COUNT

        }
        return table;
    }


    /**
	 * LCMS_ITEM Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getItemValue() {

        int row_count 	= vItemIdentifier.size();
        int seq_count	= vSeqMkey.size();
        int rsrc_count	= vRsrcIdentifier.size();
        String[][] table = new String[row_count][19];

        for(int i = 0; i<row_count; i++) {

            table[i][0]     = (String)vItemOrgMkey.get(i);          //Organization Index
            table[i][1]     = Integer.toString(i);                  //Item Index
            table[i][2]     = (String)vItemParentIdentifier.get(i); //Item Parent Id
            table[i][3]     = (String)vItemIdentifier.get(i);       //Item Id
            table[i][4]     = (String)vItemIdentifierRef.get(i);    //Item Id Ref
            table[i][5]     = (String)vItemTitle.get(i);            //Item Title
            table[i][6]     = "";							        //Item Type
            table[i][7]     = (String)vItemIsVisible.get(i);        //Item Open
            table[i][8]     = (String)vItemTimeLimitAction.get(i);  //Item Time Limt Action
            table[i][9]     = "";                                   //Item Max Time
            table[i][10]    = "";									//Item Start File
            table[i][11]    = "";                                   //Item Lom File
            table[i][12]    = (String)vItemParameters.get(i);       //Item Parameter
            table[i][13]    = (String)vItemDataFromLms.get(i);      //Item Data From Lms
            table[i][14]    = (String)vItemComThreshold.get(i);     //Item Item Threshold
            table[i][15]    = (String)vItemdepth.get(i);            //Item Depth
            table[i][17]    = (String)vItemRsrcSeq.get(i);				//Item Rsrc_seq
            table[i][18]	= (String)vItemSample_item_yn.get(i);	//Sample_item_yn

			// 2005.3.19 linuzer, 변경
            //table[i][16]    = (String)vItemMeta.get(i);             //Item Meta Data

			if((String)vItemMeta.get(i) != null && !((String)vItemMeta.get(i)).equals("")) {
	            table[i][16]    = (String)vItemMeta.get(i);    //Item Meta Data
//	 			if(Constants.FROM_CHARSET!=null && Constants.TO_CHARSET!=null &&
//						!"".equals(Constants.FROM_CHARSET) && !"".equals(Constants.TO_CHARSET)){
//	 				try{
//	 					table[i][16]=new String(table[i][16].getBytes(Constants.TO_CHARSET),Constants.FROM_CHARSET);
//	 				}catch(Exception ex){
//	 					ex.printStackTrace();
//	 				}
//				}            
			}

            //Item
            for (int j=0; j<seq_count; j++) {
            	if (vSeqItemMkey.get(j).equals(Integer.toString(i))) {

            table[i][9]     = (String)vSeqAttemptDurationLimit.get(j);	//Item Max Time

            	}
            }

            for (int k=0; k<rsrc_count; k++) {
            	if (vItemIdentifierRef.get(i).equals(vRsrcIdentifier.get(k))) {

            table[i][6]     = (String)vRsrcScormType.get(k);        //Item Type

			// 2005.3.19 linuzer, 변경
            // table[i][10]    = (String)vRsrcXmlBase.get(k)+(String)vRsrcHref.get(k)+(String)vItemParameters.get(i);//Item Start File
            table[i][10]    = subDir + "/" + (String)vRsrcXmlBase.get(k)+(String)vRsrcHref.get(k)+(String)vItemParameters.get(i);//Item Start File

            	}
            }
        }
        return table;
    }


    /**
	 * LCMS_ITEM_RESOURCE Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getRsrcValue() {

        int row_count = vRsrcIdentifier.size();
        String[][] table = new String[row_count][9];
        String[][] itemtable = getItemValue();
        for(int i = 0; i<row_count; i++) {

        	table[i][0] = (String)vRsrcNoMkey.get(i);		//Resource No Index
            table[i][1] = (String)vRsrcIdentifier.get(i);	//Resource Id
            for(int j = 0; j < itemtable.length; j++){
            	if(table[i][1].equals(itemtable[j][4])){
            		table[i][2] = itemtable[j][5];                          //rsrc_title(아이템의 title사용)
            	}
            }
            table[i][3] = (String)vRsrcType.get(i);			//Resource Type
            table[i][4] = (String)vRsrcHref.get(i);			//Resource Href
            table[i][5] = (String)vRsrcXmlBase.get(i);		//Resource Xml Base

			// 2005.3.19 linuzer 추가
			// base_dir이 있으면 rsrc_base_dir쪽에 생성디렉토리 삽입
			// base_dir이 없으면 rsrc_href쪽에 생성디렉토리삽입
			if((String)vRsrcXmlBase.get(i) != null &&
				!((String)vRsrcXmlBase.get(i)).equals("")) {
				table[i][5] = subDir + "/" + (String)vRsrcXmlBase.get(i);		//Resource Xml Base
			} else {
				table[i][4] = subDir + "/" + (String)vRsrcHref.get(i);			//Resource Href
			}

            table[i][6] = (String)vRsrcScormType.get(i);	//Resource Scorm Type
            table[i][7] = (String)vRsrcPersistState.get(i);	//Resource PersistState
			if((String)vRsrcMetadata.get(i) != null && !((String)vRsrcMetadata.get(i)).equals("")) {
				table[i][8] = (String)vRsrcMetadata.get(i);     //Resource metadata
			}
        }
        return table;
    }


    /**
	 * LCMS_FILE Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getRsrcFileValue() {

        int row_count = vRsrcFileHref.size();

        String[][] table = new String[row_count][3];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vRsrcNoFileMkey.get(i);	//Resource File Resource No Index
            table[i][1] = (String)vRsrcFileType.get(i);		//File Type ("sco","asset")
	        table[i][2] = (String)vRsrcFileHref.get(i);		//Resource File

			int idx = Integer.parseInt((String)vRsrcNoFileMkey.get(i));
			String rsrcVal = (String)vRsrcXmlBase.get(idx);

			// 2005.3.19 linuzer, 추가
			// base_dir이 있으면 생성디렉토리 삽입하지 않고 원래값만
			// base_dir이 없으면 생성디렉토리 끼워넣기

			if(rsrcVal != null && ! rsrcVal.equals("")) {
				table[i][2] = (String)vRsrcFileHref.get(i);						//Resource File
			} else {
				table[i][2] = subDir + "/" + (String)vRsrcFileHref.get(i);		//Resource File
			}
        }
        return table;
    }


    /**
	 * LCMS_RESOURCE_DEPENDENCY Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getRsrcDependencyValue() {

        int row_count = vRsrcDepIdentifierRef.size();
        String[][] table = new String[row_count][3];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vRsrcNoDependeceMkey.get(i);	//Resource Dependency Resource No Index
            table[i][1] = (String)vRsrcDependenceNoMkey.get(i);	//Resource Dependency No Index
            table[i][2] = (String)vRsrcDepIdentifierRef.get(i);	//Denpendency IdentifierRef
        }
        return table;
    }


    /**
	 * SCORM_SEQUENCING Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getSequencing()
    {
        int row_count = vSeqMkey.size();
        String[][] table = new String[row_count][29];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vSeqOrgMkey.get(i);					//Organization Index
            table[i][1] = (String)vSeqItemMkey.get(i);                  //Item Index
            table[i][2] = (String)vSeqMkey.get(i);                      //Sequencing Index
            table[i][3] = (String)vSeqChoice.get(i);
            table[i][4] = (String)vSeqChoiceExit.get(i);
            table[i][5] = (String)vSeqFlow.get(i);
            table[i][6] = (String)vSeqForwardOnly.get(i);
            table[i][7] = (String)vSeqUseAttemptObjInfo.get(i);
            table[i][8] = (String)vSeqUseAttemptProgressInfo.get(i);
            table[i][9] = (String)vSeqAttemptLimit.get(i);
            table[i][10] = (String)vSeqAttemptDurationLimit.get(i);
            table[i][11] = (String)vSeqRandomTiming.get(i);
            table[i][12] = (String)vSeqSelectCount.get(i);
            table[i][13] = (String)vSeqReorderChildren.get(i);
            table[i][14] = (String)vSeqSelectionTiming.get(i);
            table[i][15] = (String)vSeqTracked.get(i);
            table[i][16] = (String)vSeqCompletSetByContent.get(i);
            table[i][17] = (String)vSeqObjSetByContent.get(i);
            table[i][18] = (String)vSeqPreventActivation.get(i);
            table[i][19] = (String)vSeqConstrainChoice.get(i);
            table[i][20] = (String)vSeqReqSatisfied.get(i);
            table[i][21] = (String)vSeqReqNotSatisfied.get(i);
            table[i][22] = (String)vSeqReqCompleted.get(i);
            table[i][23] = (String)vSeqReqIncomplete.get(i);
            table[i][24] = (String)vSeqMeasureSatIfActive.get(i);
            table[i][25] = (String)vSeqRollupObjSatisfied.get(i);
            table[i][26] = (String)vSeqRollupProgressComplet.get(i);
            table[i][27] = (String)vSeqObjMeasureWeight.get(i);
            table[i][28] = (String)vNavHideLMSUI.get(i);
        }
        return table;
    }


    /**
	 * SEQ_CONDITION_TYPE Table 에 insert 할 데이터를 생성
	 * @return
	 */
    public String[][] getConditionType()
    {
        int row_count = vSeqRuleSeqMkey1.size();
        String[][] table = new String[row_count][5];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vSeqRuleSeqMkey1.get(i);      //Sequencing Index
            table[i][1] = (String)vSeqRuleContMkey1.get(i);     //Condition Type Index
            table[i][2] = (String)vTSeqType.get(i);             //Condition Rule Type
            table[i][3] = (String)vTSeqAction.get(i);           //Rule Action
            table[i][4] = (String)vTSeqcondiCombination.get(i); //Condition Combination
        }
        return table;
    }


    /**
	 * SEQ_RULE_CONDITION Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getRuleCondition()
    {
        int row_count = vSeqRuleSeqMkey2.size();
        String[][] table = new String[row_count][7];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vSeqRuleSeqMkey2.get(i);      //Sequencing Index
            table[i][1] = (String)vSeqRuleContMkey2.get(i);     //Condition Type Index
            table[i][2] = (String)vSeqRuleContDetailKey.get(i); //Rule Condition No
            table[i][3] = (String)vTSeqrefObjective.get(i);     //Reference Objective
            table[i][4] = (String)vTSeqmeaThreshold.get(i);     //Measure Threshold
            table[i][5] = (String)vTSeqoperator.get(i);         //Operator
            table[i][6] = (String)vTSeqcondition.get(i);        //Condition
        }
        return table;
    }


    /**
	 * SEQ_ROLLUP_RULE Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getRollupRule()
    {
        int row_count = vRollUpSeqMkey1.size();
        String[][] table = new String[row_count][7];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vRollUpSeqMkey1.get(i);           //Sequencing Index
            table[i][1] = (String)vRollUpContMkey1.get(i);          //Rollup Rule Index
            table[i][2] = (String)vSeqChildActivitySet.get(i);      //Child Activity Set
            table[i][3] = (String)vSeqMinimumCount.get(i);          //Minimum Count
            table[i][4] = (String)vSeqMinimumPercent.get(i);        //Minimum Percent
            table[i][5] = (String)vSeqRollupAction.get(i);          //Rollup Action
            table[i][6] = (String)vSeqRollupConditionComb.get(i);   //Condition Combination

        }
        return table;
    }


    /**
	 * SEQ_ROLLUP_CONDITION Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getRollupCondition()
    {
        int row_count = vRollUpSeqMkey2.size();
        String[][] table = new String[row_count][5];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vRollUpSeqMkey2.get(i);           //Sequencing Index
            table[i][1] = (String)vRollUpContMkey2.get(i);          //Rollup Rule Index
            table[i][2] = (String)vRollUpContDetailKey.get(i);      //Rollup Condition No
            table[i][3] = (String)vSeqRollupOperator.get(i);        //Operator
            table[i][4] = (String)vSeqRollupCondition.get(i);       //Condition
        }
        return table;
    }


    /**
	 * SEQ_OBJECTIVE Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getObjective()
    {
        int row_count = vObjectivesSeqMkey1.size();
        String[][] table = new String[row_count][6];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vObjectivesSeqMkey1.get(i);       //Sequencing Index
            table[i][1] = (String)vObjectivesContMkey1.get(i);      //Objective Index
            table[i][2] = (String)vSeqObjID.get(i);                 //Objective Id
            table[i][3] = (String)vSeqSatisfiedByMeasure.get(i);    //Satisfied Measure
            table[i][4] = (String)vSeqminNormalMeasure.get(i);      //Minnormal Measure
            table[i][5] = (String)vSeqObjType.get(i);               //Obj Type
        }
        return table;
    }


    /**
	 * SEQ_MAP_INFO Table 에 insert 할 데이터를 생성
	 * @return table data
	 */
    public String[][] getMapInfo()
    {
        int row_count = vObjectivesSeqMkey2.size();
        String[][] table = new String[row_count][8];
        for(int i = 0; i<row_count; i++)
        {
            table[i][0] = (String)vObjectivesSeqMkey2.get(i);       //Sequencing Index
            table[i][1] = (String)vObjectivesContMkey2.get(i);      //Objective Index
            table[i][2] = (String)vObjectivesContDetailKey.get(i);  //Map Info No
            table[i][3] = (String)vSeqTargetObjID.get(i);           //Target Obj Id
            table[i][4] = (String)vSeqReadSatisStatus.get(i);       //Read Status
            table[i][5] = (String)vSeqReadNormMeasure.get(i);       //Read Measure
            table[i][6] = (String)vSeqWriteSatisStatus.get(i);      //Write Status
            table[i][7] = (String)vSeqWriteNormalMeasure.get(i);    //Write Measure

        }
        return table;
    }



    /**
     * Applet 화면 View
     */
    public String[][] getTableValue()
    {


	        int org_count	= 0;
	        int item_count	= 0;
	        int rsrc_count	= vRsrcIdentifier.size();
	        int row_count	= vOrgIdentifier.size()+vItemIdentifier.size();

	        String[][] table=new String[row_count][10];
	    try
        {
	        for(int i=0; i< row_count ; i++){

	        	//Organization Title Display
	            if(vItemOrgMkey.get(item_count).equals(String.valueOf(org_count))){

	                table[i][0] = "["+Integer.toString(i+1) + "] Organization";
	                table[i][1] = (String)vOrgTitle.get(org_count);
	                table[i][2] = "";
	                table[i][3] = "";
	                table[i][4] = "";
	                table[i][5] = "";
	                table[i][6] = "";
	                table[i][7] = "";
	                table[i][8] = (String)vComModel.get(org_count);
	                table[i][9] = (String)vComOption.get(org_count);


	                org_count ++;


	            }else{

	                table[i][0] = "["+Integer.toString(i+1) + "] Item";
	                //Item Title & Depth Display
	                //table[i][1] = vItemdepth.get(item_count)+"-"+ (String)vItemTitle.get(item_count);
	                if(vItemParentIdentifier.get(item_count).equals("")){
	                    table[i][1] = (String)vItemTitle.get(item_count);
	                }else{
	                    table[i][1] = "-"+(String)vItemTitle.get(item_count);
	                }

	                //Resource Display
	                for (int j=0; j<rsrc_count; j++) {
	                	if (vItemIdentifierRef.get(item_count).equals(vRsrcIdentifier.get(j))) {

	                		table[i][2] = (String)vRsrcHref.get(j);
	                        table[i][3] = (String)vRsrcXmlBase.get(j);
	                        table[i][4] = (String)vRsrcScormType.get(j);
	                        table[i][5] = (String)vRsrcPersistState.get(j);
	                        table[i][6] = (String)vItemDataFromLms.get(item_count);
	                        table[i][7] = (String)vItemComThreshold.get(item_count);

	                	}
	                }



	                item_count ++;

	            }

	        }

	        //데이타 검증
	        this.getTableValue2();

	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
/*	    	CmLog.writeLog(	"Log Name   : Exception",
							"Class Name : "+getClass().getName(),
							"Method Name: getTableValue()",
							"Exception  :\n        "+e,
							"",
							"elearn_"
						  );
*/	    }
        return table;
    }




    //public String[][] getTableValue2(){
    public void getTableValue2(){

        try
        {
//	        int row_count = vItemTitle.size();
//	        String[][] rollup_rule_tb;
//	        String[][] rollup_cond_tb;
//	        String[][] obj_tb;
//	        String[][] map_tb;
//	        String[][] cond_type_tb;
//	        String[][] rule_cond_tb;

//	        Vector retVal = new Vector();
//
//	        String[][] table=new String[row_count][26];


	        /* LCMS_COURSE  */
//	        SharedMethods.println("==================LCMS_COURSE==============================");
//	        SharedMethods.println("strManifestID       :"+strManifestID);
//	        SharedMethods.println("strOrgsDefault      :"+strOrgsDefault);
//	        SharedMethods.println("strManifestMeta     :"+strManifestMeta);
//	        SharedMethods.println("\n");


	        /* LCMS_ORGANIZATION  */
//	        SharedMethods.println("==================LCMS_ORGANIZATION==============================");
//	        SharedMethods.println("vOrgIdentifier          :"+vOrgIdentifier.size());
//	        SharedMethods.println("vOrgTitle               :"+vOrgTitle.size());
//	        SharedMethods.println("vOrgStructure           :"+vOrgStructure.size());
//	        SharedMethods.println("vOrgGlobalSystem        :"+vOrgGlobalSystem.size());
//	        SharedMethods.println("vOrgMeta                :"+vOrgMeta.size());
//	        SharedMethods.println("vComModel               :"+vComModel.size());
//	        SharedMethods.println("vComOption              :"+vComOption.size());
//	        SharedMethods.println("vComCount               :"+vComCount.size());

//	        int orgcnt = vOrgIdentifier.size();
/**
	        for(int num = 0; num<orgcnt; num++)
	        {
	            SharedMethods.println("organization = vOrgIdentifier : "+(String)vOrgIdentifier.get(num)+"  |  vOrgTitle:"+(String)vOrgTitle.get(num)+"  |  vOrgStructure:"+
	            (String)vOrgStructure.get(num)+"  |  vOrgGlobalSystem: "+(String)vOrgGlobalSystem.get(num)+"  |  vOrgMeta: "+(String)vOrgMeta.get(num)+
	            "| vComModel:"+(String)vComModel.get(num)+"  |  vComOption:"+(String)vComOption.get(num)+"  |  vComCount:"+(String)vComCount.get(num));
	        }
	        SharedMethods.println("\n");
**/

	        /* LCMS_ITEM  */
//	        SharedMethods.println("==================LCMS_ITEM==============================");
//	        SharedMethods.println("vItemOrgMkey            :"+vItemOrgMkey.size());
//	        SharedMethods.println("vItemParentIdentifier   :"+vItemParentIdentifier.size());
//	        SharedMethods.println("vItemIdentifier         :"+vItemIdentifier.size());
//	        SharedMethods.println("vItemIdentifierRef      :"+vItemIdentifierRef.size());
//	        SharedMethods.println("vItemTitle              :"+vItemTitle.size());
//	        SharedMethods.println("vRsrcScormType          :"+vRsrcScormType.size());
//	        SharedMethods.println("vItemIsVisible          :"+vItemIsVisible.size());
//	        SharedMethods.println("vItemTimeLimitAction    :"+vItemTimeLimitAction.size());
//	        SharedMethods.println("vSeqAttemptDurationLimit:"+vSeqAttemptDurationLimit.size());
//	        SharedMethods.println("vRsrcHref               :"+vRsrcHref.size());
//	        SharedMethods.println("vItemParameters         :"+vItemParameters.size());
//	        SharedMethods.println("vItemDataFromLms        :"+vItemDataFromLms.size());
//	        SharedMethods.println("vItemComThreshold       :"+vItemComThreshold.size());
//	        SharedMethods.println("vItemMeta               :"+vItemMeta.size());

//	        int itemcnt = vItemOrgMkey.size();
//	        int seq_count	= vSeqMkey.size();
//	        int rsrc_count	= vRsrcIdentifier.size();
//	        int seqnum = 0;
//	        int rsnum = 0;
/**
	        for(int num = 0; num<itemcnt; num++)
	        {

	        	for (int j=0; j<seq_count; j++) {
	            	if (vSeqItemMkey.get(j).equals(Integer.toString(num))) {
	            		seqnum = j;
	            	}
	            }

	            for (int k=0; k<rsrc_count; k++) {
	            	if (vItemIdentifierRef.get(num).equals(vRsrcIdentifier.get(k))) {
	            		rsnum=k;
	            	}
	            }

	            SharedMethods.println("item = vItemOrgMkey : "+(String)vItemOrgMkey.get(num)+"  |  vItemParentIdentifier:"+(String)vItemParentIdentifier.get(num)+"  |  vItemIdentifier:"+
	            (String)vItemIdentifier.get(num)+"  |  vItemTitle: "+(String)vItemTitle.get(num)+"  |  vRsrcScormType: "+
	            (String)vRsrcScormType.get(rsnum)+"  |  vItemIsVisible: "+(String)vItemIsVisible.get(num)+"  |  vItemTimeLimitAction: "+(String)vItemTimeLimitAction.get(num)+"  |  vSeqAttemptDurationLimit: "+
	            (String)vSeqAttemptDurationLimit.get(seqnum)+"  |  vRsrcHref: "+(String)vRsrcHref.get(rsnum)+"  |  vItemParameters: "+
	            (String)vItemParameters.get(num)+"  |  vItemDataFromLms: "+(String)vItemDataFromLms.get(num)+"  |  vItemComThreshold: "+
	            (String)vItemComThreshold.get(num)+"  |  vItemMeta: "+(String)vItemMeta.get(num)  );
	        }
	        SharedMethods.println("\n");

**/

	        /* LCMS_ITEM_RESOURCE  */
//	        SharedMethods.println("==================LCMS_ITEM_RESOURCE==============================");
//	        SharedMethods.println("vRsrcNoMkey         :"+vRsrcNoMkey.size());
//	        SharedMethods.println("vRsrcIdentifier     :"+vRsrcIdentifier.size());
//	        SharedMethods.println("vRsrcType           :"+vRsrcType.size());
//	        SharedMethods.println("vRsrcHref           :"+vRsrcHref.size());
//	        SharedMethods.println("vRsrcXmlBase        :"+vRsrcXmlBase.size());
//	        SharedMethods.println("vRsrcScormType      :"+vRsrcScormType.size());
//	        SharedMethods.println("vRsrcPersistState   :"+vRsrcPersistState.size());

//	        int rsrccnt = vRsrcIdentifier.size();
/**
	        for(int num = 0; num<rsrccnt; num++)
	        {
	            SharedMethods.println("resource = vRsrcNoMkey : "+(String)vRsrcNoMkey.get(num)+"  |  vRsrcIdentifier:"+
	            (String)vRsrcIdentifier.get(num)+"  |  vRsrcType:"+(String)vRsrcType.get(num)+"  |  vRsrcHref: "+(String)vRsrcHref.get(num)+"  |  vRsrcXmlBase: "+
	            (String)vRsrcXmlBase.get(num)+"  |  vRsrcScormType:"+(String)vRsrcScormType.get(num)+"  |  vRsrcPersistState: "+(String)vRsrcPersistState.get(num) );
	        }
	        SharedMethods.println("\n");
**/


	        /* LCMS_FILE  */
//	        SharedMethods.println("==================LCMS_FILE==============================");
//	        SharedMethods.println("vRsrcNoFileMkey	:"+vRsrcNoFileMkey.size());
//	        SharedMethods.println("vRsrcFileType	:"+vRsrcFileType.size());
//	        SharedMethods.println("vRsrcFileHref	:"+vRsrcFileHref.size());

//	        int filecnt = vRsrcFileHref.size();
/**
	        for(int num = 0; num<filecnt; num++)
	        {
	            SharedMethods.println("file = vRsrcNoFileMkey : "+(String)vRsrcNoFileMkey.get(num)+"  |  vRsrcFileType:"+(String)vRsrcFileType.get(num)+"  |  vRsrcFileHref:"+(String)vRsrcFileHref.get(num) );
	        }
	        SharedMethods.println("\n");
**/


	        /* LCMS_RESOURCE_DEPENDENCY  */
//	        SharedMethods.println("==================LCMS_RESOURCE_DEPENDENCY==============================");
//	        SharedMethods.println("vRsrcNoDependeceMkey	:"+vRsrcNoDependeceMkey.size());
//	        SharedMethods.println("vRsrcDependenceNoMkey	:"+vRsrcDependenceNoMkey.size());
//	        SharedMethods.println("vRsrcDepIdentifierRef	:"+vRsrcDepIdentifierRef.size());

//	        int dependcnt = vRsrcDepIdentifierRef.size();
/***
	        for(int num = 0; num<dependcnt; num++)
	        {
	            SharedMethods.println("file = vRsrcNoDependeceMkey : "+(String)vRsrcNoDependeceMkey.get(num)+"  |  vRsrcDependenceNoMkey:"+(String)vRsrcDependenceNoMkey.get(num)+"  |  vRsrcDepIdentifierRef:"+
	            (String)vRsrcDepIdentifierRef.get(num) );
	        }
	        SharedMethods.println("\n");
****/


	        /* SCORM_SEQUENCING TABLE  */
//	        SharedMethods.println("====================SCORM_SEQUENCING============================");
//	        SharedMethods.println("vSeqOrgMkey                     :"+vSeqOrgMkey.size());
//	        SharedMethods.println("vSeqItemMkey                    :"+vSeqItemMkey.size());
//	        SharedMethods.println("vSeqMkey                        :"+vSeqMkey.size());
//	        SharedMethods.println("vSeqChoice                      :"+vSeqChoice.size());
//	        SharedMethods.println("vSeqChoiceExit                  :"+vSeqChoiceExit.size());
//	        SharedMethods.println("vSeqFlow                        :"+vSeqFlow.size());
//	        SharedMethods.println("vSeqForwardOnly                 :"+vSeqForwardOnly.size());
//	        SharedMethods.println("vSeqUseAttemptObjInfo           :"+vSeqUseAttemptObjInfo.size());
//	        SharedMethods.println("vSeqUseAttemptProgressInfo      :"+vSeqUseAttemptProgressInfo.size());
//
//	        SharedMethods.println("vSeqAttemptLimit                :"+vSeqAttemptLimit.size());
//	        SharedMethods.println("vSeqAttemptDurationLimit        :"+vSeqAttemptDurationLimit.size());
//	        SharedMethods.println("vSeqRandomTiming                :"+vSeqRandomTiming.size());
//	        SharedMethods.println("vSeqSelectCount                 :"+vSeqSelectCount.size());
//
//	        SharedMethods.println("vSeqReorderChildren             :"+vSeqReorderChildren.size());
//	        SharedMethods.println("vSeqSelectionTiming             :"+vSeqSelectionTiming.size());
//	        SharedMethods.println("vSeqTracked                     :"+vSeqTracked.size());
//	        SharedMethods.println("vSeqCompletSetByContent         :"+vSeqCompletSetByContent.size());
//	        SharedMethods.println("vSeqObjSetByContent             :"+vSeqObjSetByContent.size());
//	        SharedMethods.println("vSeqPreventActivation           :"+vSeqPreventActivation.size());
//	        SharedMethods.println("vSeqConstrainChoice             :"+vSeqConstrainChoice.size());
//	        SharedMethods.println("vSeqReqSatisfied                :"+vSeqReqSatisfied.size());
//	        SharedMethods.println("vSeqReqNotSatisfied             :"+vSeqReqNotSatisfied.size());
//	        SharedMethods.println("vSeqReqCompleted                :"+vSeqReqCompleted.size());
//	        SharedMethods.println("vSeqReqIncomplete               :"+vSeqReqIncomplete.size());
//	        SharedMethods.println("vSeqMeasureSatIfActive          :"+vSeqMeasureSatIfActive.size());
//	        SharedMethods.println("vSeqRollupObjSatisfied          :"+vSeqRollupObjSatisfied.size());
//	        SharedMethods.println("vSeqRollupProgressComplet       :"+vSeqRollupProgressComplet.size());
//	        SharedMethods.println("vSeqObjMeasureWeight            :"+vSeqObjMeasureWeight.size());
//	        SharedMethods.println("vNavHideLMSUI                   :"+vNavHideLMSUI.size());

//	        int allcnt = vSeqMkey.size();
/****
	        for(int num = 0; num<allcnt; num++)
	        {
	            SharedMethods.println("item roll & sequence = vSeqOrgMkey : "+(String)vSeqOrgMkey.get(num)+"  |  vSeqItemMkey:"+(String)vSeqItemMkey.get(num)+"  |  vSeqMkey:"+
	            (String)vSeqMkey.get(num)+"  |  vSeqChoice: "+(String)vSeqChoice.get(num)+"  |  vSeqChoiceExit: "+
	            (String)vSeqChoiceExit.get(num)+"  |  vSeqFlow: "+(String)vSeqFlow.get(num)+"  |  vSeqForwardOnly: "+
	            (String)vSeqForwardOnly.get(num)+"  |  vSeqUseAttemptObjInfo: "+(String)vSeqUseAttemptObjInfo.get(num)+"  |  vSeqUseAttemptProgressInfo: "+
	            (String)vSeqUseAttemptProgressInfo.get(num)+"  |  vSeqAttemptLimit: "+(String)vSeqAttemptLimit.get(num)+"  |  vSeqAttemptDurationLimit: "+
	            (String)vSeqAttemptDurationLimit.get(num)+"  |  vSeqRandomTiming: "+(String)vSeqRandomTiming.get(num)+"  |  vSeqSelectCount: "+
	            (String)vSeqSelectCount.get(num)+"  |  vSeqReorderChildren: "+(String)vSeqReorderChildren.get(num)+"  |  vSeqSelectionTiming: "+
	            (String)vSeqSelectionTiming.get(num)+"  |  vSeqTracked: "+(String)vSeqTracked.get(num)+"  |  vSeqCompletSetByContent: "+
	            (String)vSeqCompletSetByContent.get(num)+"  |  vSeqObjSetByContent: "+(String)vSeqObjSetByContent.get(num)+"  |  vSeqPreventActivation: "+
	            (String)vSeqPreventActivation.get(num)+"  |  vSeqConstrainChoice: "+(String)vSeqConstrainChoice.get(num)+"  |  vSeqReqSatisfied: "+
	            (String)vSeqReqSatisfied.get(num)+"  |  vSeqReqNotSatisfied: "+(String)vSeqReqNotSatisfied.get(num)+"  |  vSeqReqCompleted: "+
	            (String)vSeqReqCompleted.get(num)+"  |  vSeqReqIncomplete: "+(String)vSeqReqIncomplete.get(num)+"  |  vSeqMeasureSatIfActive: "+
	            (String)vSeqMeasureSatIfActive.get(num)+"  |  vSeqRollupObjSatisfied: "+(String)vSeqRollupObjSatisfied.get(num)+"  |  vSeqRollupProgressComplet: "+
	            (String)vSeqRollupProgressComplet.get(num)+"  |  vSeqObjMeasureWeight: "+(String)vSeqObjMeasureWeight.get(num)+"  |  vNavHideLMSUI: "+(String)vNavHideLMSUI.get(num)   );
	        }
	        SharedMethods.println("\n");
****/

	        /* SEQ_CONDITION_TYPE  */
//	        SharedMethods.println("==================SEQ_CONDITION_TYPE==============================");
//	        SharedMethods.println("vSeqRuleSeqMkey1            :"+vSeqRuleSeqMkey1.size());
//	        SharedMethods.println("vSeqRuleContMkey1           :"+vSeqRuleContMkey1.size());
//	        SharedMethods.println("vTSeqType                   :"+vTSeqType.size());
//	        SharedMethods.println("vTSeqcondiCombination       :"+vTSeqcondiCombination.size());
//	        SharedMethods.println("vTSeqAction                 :"+vTSeqAction.size());


//	        int condcnt = vSeqRuleSeqMkey1.size();
/**
	        for(int num = 0; num<condcnt; num++)
	        {
	            SharedMethods.println("conditions = vSeqRuleSeqMkey1 : "+(String)vSeqRuleSeqMkey1.get(num)+"  |  vSeqRuleContMkey1:"+(String)vSeqRuleContMkey1.get(num)+"  |  vTSeqType:"+
	            (String)vTSeqType.get(num)+"  |  vTSeqcondiCombination: "+(String)vTSeqcondiCombination.get(num)+"  |  vTSeqAction: "+(String)vTSeqAction.get(num) );
	        }
	        SharedMethods.println("\n");
**/

	        /* SEQ_RULE_CONDITION  */
//	        SharedMethods.println("==================SEQ_RULE_CONDITION==============================");
//	        SharedMethods.println("vSeqRuleSeqMkey2            :"+vSeqRuleSeqMkey2.size());
//	        SharedMethods.println("vSeqRuleContMkey2           :"+vSeqRuleContMkey2.size());
//	        SharedMethods.println("vSeqRuleContDetailKey       :"+vSeqRuleContDetailKey.size());
//	        SharedMethods.println("vTSeqrefObjective           :"+vTSeqrefObjective.size());
//	        SharedMethods.println("vTSeqmeaThreshold           :"+vTSeqmeaThreshold.size());
//	        SharedMethods.println("vTSeqoperator               :"+vTSeqoperator.size());
//	        SharedMethods.println("vTSeqcondition              :"+vTSeqcondition.size());

//	        int condrulecnt = vSeqRuleSeqMkey2.size();
/**
	        for(int num = 0; num<condrulecnt; num++)
	        {
	            SharedMethods.println("condition = vSeqRuleSeqMkey2 : "+(String)vSeqRuleSeqMkey2.get(num)+"  |  vSeqRuleContMkey2:"+(String)vSeqRuleContMkey2.get(num)+"  |  vSeqRuleContDetailKey:"+
	            (String)vSeqRuleContDetailKey.get(num)+"  |  vTSeqrefObjective: "+(String)vTSeqrefObjective.get(num)+"  |  vTSeqmeaThreshold: "+(String)vTSeqmeaThreshold.get(num)+"  |  vTSeqoperator: "+
	            (String)vTSeqoperator.get(num)+"  |  vTSeqcondition: "+(String)vTSeqcondition.get(num) );
	        }
	        SharedMethods.println("\n");

**/

	        /* SEQ_ROLLUP_RULE  */
//	        SharedMethods.println("==================SEQ_ROLLUP_RULE==============================");
//	        SharedMethods.println("vRollUpSeqMkey1                 :"+vRollUpSeqMkey1.size());
//	        SharedMethods.println("vRollUpContMkey1                :"+vRollUpContMkey1.size());
//	        SharedMethods.println("vSeqChildActivitySet            :"+vSeqChildActivitySet.size());
//	        SharedMethods.println("vSeqMinimumCount                :"+vSeqMinimumCount.size());
//	        SharedMethods.println("vSeqMinimumPercent              :"+vSeqMinimumPercent.size());
//	        SharedMethods.println("vSeqRollupAction                :"+vSeqRollupAction.size());
//	        SharedMethods.println("vSeqRollupConditionComb         :"+vSeqRollupConditionComb.size());

//	        int rulecnt = vRollUpSeqMkey1.size();
/**
	        for(int num = 0; num<rulecnt; num++)
	        {
	            SharedMethods.println("rollup = vRollUpSeqMkey1 : "+(String)vRollUpSeqMkey1.get(num)+"  |  vRollUpContMkey1:"+(String)vRollUpContMkey1.get(num)+"  |  vSeqChildActivitySet:"+
	            (String)vSeqChildActivitySet.get(num)+"  |  vSeqMinimumCount: "+(String)vSeqMinimumCount.get(num)+"  |  vSeqMinimumPercent: "+(String)vSeqMinimumPercent.get(num)+"  |  vSeqRollupAction: "+
	            (String)vSeqRollupAction.get(num)+"  |  vSeqRollupConditionComb: "+(String)vSeqRollupConditionComb.get(num) );
	        }
	        SharedMethods.println("\n");
**/


	        /* SEQ_ROLLUP_CONDITION */
//	        SharedMethods.println("==================SEQ_ROLLUP_CONDITION==============================");
//	        SharedMethods.println("vRollUpSeqMkey2         :"+vRollUpSeqMkey2.size());
//	        SharedMethods.println("vRollUpContMkey2        :"+vRollUpContMkey2.size());
//	        SharedMethods.println("vRollUpContDetailKey    :"+vRollUpContDetailKey.size());
//	        SharedMethods.println("vSeqRollupOperator      :"+vSeqRollupOperator.size());
//	        SharedMethods.println("vSeqRollupCondition     :"+vSeqRollupCondition.size());

//	        int rulecondcnt = vRollUpSeqMkey2.size();
/**
	        for(int num = 0; num<rulecondcnt; num++)
	        {
	            SharedMethods.println("rollupCondition = vRollUpSeqMkey2 : "+(String)vRollUpSeqMkey2.get(num)+"  |  vRollUpContMkey2:"+(String)vRollUpContMkey2.get(num)+"  |  vRollUpContDetailKey:"+
	            (String)vRollUpContDetailKey.get(num)+"  |  vSeqRollupOperator: "+(String)vSeqRollupOperator.get(num)+"  |  vSeqRollupCondition: "+(String)vSeqRollupCondition.get(num) );
	        }
	        SharedMethods.println("\n");
**/

	        /* SEQ_OBJECTIVE */
//	        SharedMethods.println("==================SEQ_OBJECTIVE==============================");
//	        SharedMethods.println("vObjectivesSeqMkey1     :"+vObjectivesSeqMkey1.size());
//	        SharedMethods.println("vObjectivesContMkey1    :"+vObjectivesContMkey1.size());
//	        SharedMethods.println("vSeqObjType             :"+vSeqObjType.size());
//	        SharedMethods.println("vSeqSatisfiedByMeasure  :"+vSeqSatisfiedByMeasure.size());
//	        SharedMethods.println("vSeqObjID               :"+vSeqObjID.size());
//	        SharedMethods.println("vSeqminNormalMeasure    :"+vSeqminNormalMeasure.size());

//	        int objcnt = vObjectivesSeqMkey1.size();
/**
	        for(int num = 0; num<objcnt; num++)
	        {
	            SharedMethods.println("objective = vObjectivesSeqMkey1 : "+(String)vObjectivesSeqMkey1.get(num)+"  |  vObjectivesContMkey1:"+(String)vObjectivesContMkey1.get(num)+"  |  vSeqObjType:"+
	            (String)vSeqObjType.get(num)+"  |  vSeqSatisfiedByMeasure: "+(String)vSeqSatisfiedByMeasure.get(num)+"  |  vSeqObjID: "+(String)vSeqObjID.get(num)+"  |  vSeqminNormalMeasure: "+(String)vSeqminNormalMeasure.get(num) );
	        }
	        SharedMethods.println("\n");
**/


	        /* SEQ_MAP_INFO  */
//	        SharedMethods.println("==================SEQ_MAP_INFO==============================");
//	        SharedMethods.println("vObjectivesSeqMkey2         :"+vObjectivesSeqMkey2.size());
//	        SharedMethods.println("vObjectivesContMkey2        :"+vObjectivesContMkey2.size());
//	        SharedMethods.println("vObjectivesContDetailKey    :"+vObjectivesContDetailKey.size());
//	        SharedMethods.println("vSeqTargetObjID             :"+vSeqTargetObjID.size());
//	        SharedMethods.println("vSeqReadSatisStatus         :"+vSeqReadSatisStatus.size());
//	        SharedMethods.println("vSeqReadNormMeasure         :"+vSeqReadNormMeasure.size());
//	        SharedMethods.println("vSeqWriteSatisStatus        :"+vSeqWriteSatisStatus.size());
//	        SharedMethods.println("vSeqWriteNormalMeasure      :"+vSeqWriteNormalMeasure.size());

//	        int objmapcnt = vObjectivesSeqMkey2.size();
/**
	        for(int num = 0; num<objmapcnt; num++)
	        {
	            SharedMethods.println("object map = vObjectivesSeqMkey2 : "+(String)vObjectivesSeqMkey2.get(num)+"  |  vObjectivesContMkey2:"+(String)vObjectivesContMkey2.get(num)+"  |  vObjectivesContDetailKey:"+
	            (String)vObjectivesContDetailKey.get(num)+"  |  vSeqTargetObjID: "+(String)vSeqTargetObjID.get(num)+"  |  vSeqReadSatisStatus: "+(String)vSeqReadSatisStatus.get(num)+"  |  vSeqReadNormMeasure: "+
	            (String)vSeqReadNormMeasure.get(num)+"  |  vSeqWriteSatisStatus: "+(String)vSeqWriteSatisStatus.get(num)+"  |  vSeqWriteNormalMeasure: "+(String)vSeqWriteNormalMeasure.get(num) );
	        }
	        SharedMethods.println("\n");
**/
        }catch(Exception e) {
/*        	CmLog.writeLog(	"Log Name   : Exception",
							"Class Name : "+getClass().getName(),
							"Method Name: getTableValue2()",
							"Exception  :\n        "+e,
							"",
							"elearn_"
						  );
*/        }

        //return table;
    }
}
