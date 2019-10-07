package egovframework.adm.lcms.cts.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.adl.sequencer.SeqActivityTree;
import org.adl.validator.ADLValidatorOutcome;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.lcms.api.com.SetVO;
import org.lcms.api.com.Util;
import org.lcms.api.coursemapdao.MapStudyParamObject;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.bind.WebDataBinder;




import egovframework.adm.lcms.cts.controller.LcmsOrganizationController;
import egovframework.adm.lcms.cts.dao.LcmsCourseOrgDAO;
import egovframework.adm.lcms.cts.dao.LcmsOrganizationDAO;
import egovframework.adm.lcms.ims.mainfest.ImportMsg;
import egovframework.adm.lcms.ims.mainfest.ServerManifestHandler;
import egovframework.adm.lcms.ims.mainfest.manifestTableBean;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;

public class LcmsScormModel {
	/** log */ 
	protected static final Log log = LogFactory.getLog( LcmsOrganizationController.class);

	@Resource(name="lcmsOrganizationDAO")
    private LcmsOrganizationDAO lcmsOrganizationDAO;
	
	@Resource(name="lcmsCourseOrgDAO")
    private LcmsCourseOrgDAO lcmsCourseOrgDAO;
	
	
	public String userid = "";

	public String prog_id = "";

	public String course_seq = "";

	private ImportMsg msgHandler = null;

	private String msgId = null;
	
	public Document document;

	public Element rootEmt;
	public String path = "";
	public int deepth = 1;
	public Vector ele_name = new Vector();
	public Vector ele_val = new Vector();
	public Vector ele_lang_val = new Vector();
	public Vector ele_path = new Vector();
	public Vector parent_group = new Vector();
	public Vector self_group = new Vector();
	public Vector rs_ele_name = new Vector();
	public Vector rs_ele_val = new Vector();
	public Vector rs_ele_lang_val = new Vector();
	public Vector rs_ele_path = new Vector();
	public Vector rs_parent_group = new Vector();
	public Vector rs_self_group = new Vector();
	
	public String[][] rsrc_no; // item resource's rsrc_no
	
	public ArrayList getData( HttpServletRequest request, Map<String, Object> commandMap, ArrayList imsPath) throws Exception {
    	
		Map<String, Object> inputMap = new HashMap<String, Object>();
    	String[] organization_seq = null;
    	ArrayList dataList = new ArrayList();
//    	try{
			MapStudyParamObject mapobj = new MapStudyParamObject();
			
			SetVO mapsetter=new SetVO(mapobj,request);
			mapsetter.setProperty("*");				
			//String service_yn = Util.NullCheck(request.getParameter("service_yn"),"N");
			String service_yn = "Y";
			//String lms_course_cd = Util.NullCheck(request.getParameter("course_seq"),"0");
			String lms_course_cd = (String)commandMap.get("subj");
			String yn_lms = Util.NullCheck((String)commandMap.get("yn_lms"),"N");
			mapobj.setService_yn(service_yn);
			mapobj.setLms_course_cd(lms_course_cd);
			mapobj.setYn_lms(yn_lms);
			String userid = (String)commandMap.get("userid");
			
			boolean isValidation = true; // 매니페스트 파일 ADL Validation Check를 할지 여부
			HashMap hData = new HashMap();
			String manifest_id = "";
			String manifest_meta = "";
			String orgs_default = "";
			
			String[] ims_path = new String[imsPath.size()];
			if( imsPath != null && imsPath.size() > 0 ){
				for( int i=0; i<imsPath.size(); i++ ){
					log.info(" set ims : "+(String)imsPath.get(i));
					ims_path[i]	= (String)imsPath.get(i);
				}
			}
			String[] changepath = ims_path;
			
			String course_seq = (String)commandMap.get("subj");
			String org_seq = (String)commandMap.get("week_no");
			String script = (String)commandMap.get("script");
			
			String validationCheck = (String)commandMap.get("validation");
			String scormYN = (String)commandMap.get("scorm_yn");
			String group_seq = (String)commandMap.get("group_seq");
			
			// 차시 생성인지 차시 수정인지 확인
			String org_state = (String)commandMap.get("org_state");
			org_state = "insert";
			
			String crs_org_no = (String)commandMap.get("crs_org_no");
			String item_seq = (String)commandMap.get("item_seq");
			String org_item_no = (String)commandMap.get("org_item_no");
			String[] template_seq = (String[])commandMap.get("selecttemplatetitle");
			boolean flagtemplate = true;
			int templatecount = 0;
			if(template_seq != null){
				for(int i=0;i<template_seq.length;i++){
					if("0".equals(template_seq[i])){
						templatecount++;
					}
				}
			}
			if(templatecount < 3){
				flagtemplate = false;
			}

			// SAMPLECONTENT
			isValidation = validationCheck != null && validationCheck.equals("true") ? true : false;
			
			ImportMsg msgHandler = ImportMsg.getInstance();
			String msgId = msgHandler.init(request.getRequestedSessionId());
			msgHandler.setNowStep(msgId, ImportMsg.STEP_DEFAULT);
			
			String[][] table;
			String[][] table_organization;
			String[][] table_item;
			String[][] table_resource;
			String[][] table_file;
			String[][] table_dependency;
			String[][] table_sequencing;
			String[][] table_conditonType;
			String[][] table_ruleCondition;
			String[][] table_objective;
			String[][] table_rolluprule;
			String[][] table_rollupcond;
			String[][] table_mapinfo;

			String theWebPath = request.getRealPath("/");
			String theXSDPath = theWebPath.substring(0, theWebPath.lastIndexOf(java.io.File.separator));
			
			manifestTableBean tablebean = null;
			ServerManifestHandler myManifestHandler = null;
		
//			if (key != null) {
				for (int i = 0; i < ims_path.length; i++) {
//		 			if(Constants.FROM_CHARSET!=null && Constants.TO_CHARSET!=null &&
//							!"".equals(Constants.FROM_CHARSET) && !"".equals(Constants.TO_CHARSET)){
//		 				ims_path[i]=new String(ims_path[i].getBytes(Constants.TO_CHARSET),Constants.FROM_CHARSET);
//					}
		 			
					myManifestHandler = new ServerManifestHandler(theXSDPath);
					String rootFilePath = "";

					rootFilePath = ims_path[i]; 

					ADLValidatorOutcome result = myManifestHandler.processPackage(
							rootFilePath, isValidation);

//					if (!((result.getDoesIMSManifestExist()
//							&& result.getIsWellformed() && !isValidation)
//							|| (result.getDoesIMSManifestExist()
//									&& result.getIsValidToSchema()
//									&& result.getIsValidToApplicationProfile() && result
//									.getDoRequiredCPFilesExist()))) {
//						
//						if (!(result.getIsWellformed())) {
//							msgHandler.setError(msgId, ImportMsg.STEP_MANI_VALID_WELLFORMED);
//							throw new Exception();
//						}
//						if (!(result.getIsValidToSchema())) {
//							msgHandler.setError(msgId, ImportMsg.STEP_MANI_VALID_SCHEMA);
//							throw new Exception();
//						}
//						if (!(result.getIsValidToApplicationProfile())) {
//							msgHandler.setError(msgId, ImportMsg.STEP_MANI_VALID_APPLICATIONPROFILE);
//							throw new Exception();
//						}
//						if (!(result.getDoRequiredCPFilesExist())) {
//							msgHandler.setError(msgId, ImportMsg.STEP_MANI_VALID_REQUIREDCPFILESEXIST);
//							throw new Exception();
//						}
//					}

					Hashtable seqActivityTable = myManifestHandler.getSeqActivityTree(course_seq, true);

					tablebean = new manifestTableBean();

					log.info("IMS >>>>>>>>>> "+ims_path[i]);
					tablebean.setXmlFile( ims_path[i], "imsmanifest.xml", msgId );

					table_organization = tablebean.getOrgValue();
					table_item = tablebean.getItemValue();
					table_resource = tablebean.getRsrcValue();
					table_file = tablebean.getRsrcFileValue();
					table_dependency = tablebean.getRsrcDependencyValue();
					table_sequencing = tablebean.getSequencing();
					table_conditonType = tablebean.getConditionType();
					table_ruleCondition = tablebean.getRuleCondition();
					table_rolluprule = tablebean.getRollupRule();
					table_rollupcond = tablebean.getRollupCondition();
					table_objective = tablebean.getObjective();
					table_mapinfo = tablebean.getMapInfo();

					table = tablebean.getTableValue();

					manifest_id = tablebean.getManifestID();
					orgs_default = tablebean.getOrgsDefault();
					manifest_meta = tablebean.getManifestMeta();
					if (manifest_meta == null)
						manifest_meta = "";

					// LCMS_COURSE Insert Data
					hData.put("manifest_id", manifest_id);
					hData.put("orgs_default", orgs_default);
					hData.put("userid", userid); // userId
					hData.put("course_seq", course_seq); // 코스코드

					// LCMS_ORGANIZATION
					hData.put("orgvalue", table_organization);
					// LCMS_ITEM
					hData.put("item", table_item);
					// LCMS_ITEM_RESOURCE
					hData.put("resource", table_resource);
					// LCMS_FILE
					hData.put("file", table_file);
					// LCMS_RESOURCE_DEPENDENCY
					hData.put("dependency", table_dependency);
					// SCORM_SEQUENCING
					hData.put("Sequencing", table_sequencing);
					// SEQ_CONDITION_TYPE
					hData.put("conditionType", table_conditonType);
					// SEQ_RULE_CONDITION
					hData.put("ruleCondition", table_ruleCondition);
					// SEQ_ROLLUP_RULE
					hData.put("rolluprule", table_rolluprule);
					// SEQ_ROLLUP_CONDITION
					hData.put("rollupcond", table_rollupcond);
					// SEQ_OBJECTIVE
					hData.put("objective", table_objective);
					// SEQ_MAP_INFO
					hData.put("mapinfo", table_mapinfo);

					// SCORM or NONE SCORM 지정
					boolean isScorm = scormYN == null || scormYN.equals("") || scormYN.toUpperCase().equals("N") ? false : true;

					String to_folder = (String)commandMap.get("strSavePath");				
					
					log.info("setData START ++++++++++++++++++++++++++++++");
					inputMap = setData(hData, ims_path[i], 
							msgId, seqActivityTable, 
							isScorm, true, course_seq,	 
							org_state, org_seq, crs_org_no, item_seq,
							org_item_no, rootFilePath, group_seq,to_folder,mapobj,template_seq,flagtemplate);	
					inputMap.put("course", course_seq);
					inputMap.put("userid", userid);
					
					dataList.add(inputMap);
				}
//			}
			
//    	}catch(Exception ex){
//    		log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> LcmsOrganizationServiceImpl Exception insertLcmsOrganization");
//    		log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> Exception : "+ex);
//    	}
        return dataList;
    }
	
	
	public Map setData(HashMap pData, String ims_path, 
			String msgId, Hashtable seqActivityTree,
			boolean isScorm, boolean pif, String course, 
			String org_state, String org_seq, String crs_org_no,
			String item_seq_edit, String org_item_no, String rootFilePath,
			String group_seq,String to_folder,MapStudyParamObject mapobj,String[] template_seq,boolean flagtemplate
			) throws Exception {
    	
    	this.msgHandler = ImportMsg.getInstance();
		this.msgId = msgId;
    	
		userid = (String) pData.get("userid");
		prog_id = (String) pData.get("prog_id");

		int isOk = 1;
		String[][] table_organization;
		String[][] table_item;
		String[][] table_resource;
		String[][] table_file;
		String[][] table_dependency;
		String[][] table_sequencing;
		String[][] table_conditionType;
		String[][] table_ruleCondition;
		String[][] table_objective;
		String[][] table_rolluprule;
		String[][] table_rollupcond;
		String[][] table_mapinfo;


		int addMaxCnt = 0; // contents 추가시 이전 organization의 순번
		String course_seq = ""; // lcms_course_item 에 사용될 course_seq
		int rsrcCnt = 0; // 추가입력인 경우 기존 입력되어있는 resource 의 갯수.
    	
		Map<String, Object> insertMap = new HashMap<String, Object>();
		try {
			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_GET_COURSE);
			course_seq = (String) pData.get("course_seq");// 받아온 과정코드에
			
			HashMap hashMap=new HashMap();
			hashMap.put("courseSeq",course_seq);
			
			LcmsScormModel model = new LcmsScormModel();

//			addMaxCnt = lcmsCourseOrgDAO.selectCrsOrgNo(hashMap);

			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_COURSE);

			
			// LCMS_ORGANIZATION && LCMS_METADATA && LCMS_METADATA_ELEMENT
			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_ORGANIZATION);
			table_organization = (String[][]) pData.get("orgvalue");
			insertMap = this.setOrganization(table_organization, (String) pData.get("course_seq"), userid, addMaxCnt, org_state, ims_path);

			// LCMS_SERIALIZER
			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_SERIALIZER);
			insertMap.put("serializerList", this.setSerializer(table_organization, org_state, seqActivityTree, isScorm, course_seq ));
			
			// LCMS_ITEM_RESOURCE && LCMS_METADATA && LCMS_METADATA_ELEMENT
			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_RESOURCE);
			table_resource = (String[][]) pData.get("resource");
			insertMap.put("itemResourceList", this.setResource(table_resource, userid, rsrcCnt, ims_path, org_state, group_seq,to_folder,flagtemplate));
			
			// LCMS_FILE
			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_FILE);
			table_file = (String[][]) pData.get("file");
			insertMap.put("file", this.getFile(table_file));
			
			// LCMS_RESOURCE_DEPENDENCY
			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_DEPENDENCY);
			table_dependency = (String[][]) pData.get("dependency");
			insertMap.put("dependencyList", this.getDependency(table_dependency));
			
			// LCMS_ITEM 테이블 INSERT
			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_ITEM);
			table_item = (String[][]) pData.get("item");
			insertMap.put("itemList", this.setItem( table_item, userid, rsrc_no, pif, org_state, ims_path, course_seq));
			

			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_SEQUENCING);
			// SCORM_SEQUENCING TABLE INSERT
			table_sequencing = (String[][]) pData.get("Sequencing");
			insertMap.put("sequencingList", this.setSequencing(table_sequencing));

			// SEQ_CONDITION_TYPE TABLE INSERT
			table_conditionType = (String[][]) pData.get("conditionType");
			insertMap.put("conditionList", this.setConditionType(table_conditionType));
			
			// SEQ_RULE_CONDITION TABLE INSERT
			table_ruleCondition = (String[][]) pData.get("ruleCondition");
			insertMap.put("ruleConditionList", this.setRuleCondition( table_ruleCondition ));
			
			// SEQ_ROLLUP_RULE
			table_rolluprule = (String[][]) pData.get("rolluprule");
			insertMap.put("rollupRuleList", this.setRollupRule( table_rolluprule ));
			
			// SEQ_ROLLUP_CONDITION
			table_rollupcond = (String[][]) pData.get("rollupcond");
			insertMap.put("rollupcondList", this.setRollupCondition( table_rollupcond ));
			
			// SEQ_OBJECTIVE TABLE INSERT
			table_objective = (String[][]) pData.get("objective");
			insertMap.put("objectiveList", this.setObjective(table_objective));
			
			// SEQ_MAP_INFO TABLE INSERT
			table_mapinfo = (String[][]) pData.get("mapinfo");
			insertMap.put("mapInfoList", this.setMapInfo(table_mapinfo));
			
			// LCMS_ORGANIZATION_LOG INSERT
//			this.setOrganizationLog(organization_seq, table_item, userid,
//					org_state);
			
			msgHandler.setNowStep(msgId, ImportMsg.STEP_MANI_DB_SET_COURSE_ITEM);
			
			mapobj.setCourse_seq(course);
			
//			if(template_seq != null){
//				for(int i=0;i<template_seq.length;i++){
//					if(!"0".equals(template_seq[i])){
//						this.metadatatemplate(course_seq,template_seq[i],organization_seq,item_seq,rsrc_no);
//					}
//				}
//			}
			insertMap.put("mapobj", mapobj);
			
			
		} catch (SQLException se) {
			se.printStackTrace();
			msgHandler.setNowStep(msgId, msgHandler.getLastStep(msgId), se);
		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			try{
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
		return insertMap;
    }
	
	public Map setOrganization(String[][] table, String course_seq, String userid,
			int addMaxCnt, String org_state, String ims_path) throws Exception {

		int tb_cnt = 0;
		Map map = new HashMap();
		ArrayList org = new ArrayList();
		ArrayList met = new ArrayList();
		
		try {

			tb_cnt = table.length;
			Map<String, Object> inputMap;
			Map<String, Object> metadataMap;
			for (int i = 0; i < tb_cnt; i++) {
				inputMap = new HashMap();
				metadataMap = new HashMap();
				// 신규주차 등록인지 수정인지 확인
				inputMap.put("courseSeq", course_seq);
				
				inputMap.put("crsOrgNo", (Integer.parseInt(table[i][0])));
				inputMap.put("registId", (userid));
				inputMap.put("userId", (userid));

				table[i][4] = table[i][4].equals("") ? "true" : table[i][4];
				// 학습주제를 위한 데이터
				inputMap.put("courseCd", 		course_seq);
				inputMap.put("courseType", 		("import"));
				inputMap.put("orgId", 			(table[i][1]));
				inputMap.put("orgTitle", 		(table[i][2]));
				inputMap.put("orgDir", 			("test"));
				inputMap.put("orgStructure", 	(table[i][3]));
				inputMap.put("orgGlobalToSys", 	(table[i][4]));
				inputMap.put("metaLocation", 	(table[i][5]));
				inputMap.put("modelInfo", 		(table[i][6]+table[i][7]));
				
				if (table[i][5] != null) {
					// 메타데이터
					metadataMap.put("objType", "ORG");
					metadataMap.put("userId", userid);
					metadataMap.put("metaLocation", ims_path + File.separator + table[i][5]);
				}
				org.add(inputMap);
				met.add(metadataMap);
			}
			map.put("organization", org);
			map.put("metadata", met);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	public String setMetaData(String meta_location) throws Exception{
		String metatype = "";
		int selfGroup = 1;
		int parentGroup = 1;
		int p_parentGroup = 1;
		SAXBuilder builder = new SAXBuilder(false);
		try{
			document = builder.build(new File(meta_location));
			rootEmt = document.getRootElement();
			metatype = rootEmt.getName();
			List child_lst = rootEmt.getChildren();
			for (int i = 0; i < child_lst.size(); i++) {
				parsingEmt((Element) child_lst.get(i));
				parent_group.addElement(String.valueOf(parentGroup));
				self_group.addElement(String.valueOf(selfGroup));
				setGroup((Element) child_lst.get(i), deepth, p_parentGroup, parentGroup);
			}
			addElement();
		}catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> Exception setMetaData");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return metatype;
	}
	
	/**
	 * description: MataData Parsing
	 * @return 
	 * @throws Exception
	 */	
	public void parsingEmt(Object obj) {

		if (obj instanceof Element) {
			Element tempEmt = (Element) obj;

			ele_name.addElement(tempEmt.getName());
			if (tempEmt.getName().toLowerCase().equals("string")) {
				ele_lang_val.addElement(tempEmt.getAttributeValue("language"));
			} else {
				ele_lang_val.addElement("");
			}
			path = "";

			getPath(tempEmt);

			List child_tempEmts = tempEmt.getChildren();

			if (child_tempEmts.size() > 0) {
				ele_val.addElement("");
				for (int i = 0; i < child_tempEmts.size(); i++) {
					parsingEmt((Element) child_tempEmts.get(i));
				}
			} else {
				// 더이상 자식노드가 없는 element 출력
				ele_val.addElement(tempEmt.getValue());
			}// end if
		} // end if
	}
	/**
	 * description: MataData path
	 * @return 
	 * @throws Exception
	 */	
	public void getPath(Object obj) {

		Element tempEmt = (Element) obj;

		Element tempEmt2 = tempEmt.getParentElement();

		path = tempEmt2.getName() + "/" + path;

		if (tempEmt.getParentElement().getName().toLowerCase().equals("lom")
				|| tempEmt.getParentElement().getName().toLowerCase().equals(
						"kem")) {
			path = path.substring(0, path.length() - 1);
			ele_path.addElement(path);
		} else {
			getPath(tempEmt2);
		}
	}
	/**
	 * description: MataData group
	 * @return 
	 * @throws Exception
	 */	
	public void setGroup(Object obj, int deepth, int p_parentGroup,
			int parentGroup) {
		String bar = " ";
		for (int j = 0; j < deepth; j++) {
			bar = "   " + bar;
		}

		Element tempEmt = (Element) obj;
		List child_lst2 = tempEmt.getChildren();

		if (child_lst2.size() > 0) {
			for (int i = 0; i < child_lst2.size(); i++) {
				String tmpname = ((Element) child_lst2.get(i)).getName();
				int selfGroup = searchGroup(child_lst2, tmpname, i);
				if (tmpname.toLowerCase().equals("string") && deepth > 2) {
					// 만일 taxon 과 같은 상황이 여러건이 발생할 경우 Element name으로 제어하도록 한다.
					// if(((Element)tempEmt.getParent()).getName().equals("taxon")){}

					parent_group.addElement(String.valueOf(p_parentGroup));
				} else {
					parent_group.addElement(String.valueOf(parentGroup));
				}
				self_group.addElement(String.valueOf(selfGroup));
				setGroup((Element) child_lst2.get(i), deepth + 1, parentGroup,
						selfGroup);
			}
		}
	}
	/**
	 * description: MataData group 조회
	 * @return returnVal
	 * @throws Exception
	 */
	public int searchGroup(List lst, String name, int position) {
		int returnVal = 1;
		for (int i = 0; i < position; i++) {
			if (((Element) lst.get(i)).getName().toLowerCase().equals(
					name.toLowerCase())) {
				returnVal = returnVal + 1;
			}
		}
		return returnVal;
	}
	
	/**
	 * description: MataData 추가
	 * @return returnVal
	 * @throws Exception
	 */
	public void addElement() {

		for (int i = 0; i < ele_val.size(); i++) {
			if (ele_val.elementAt(i).toString().trim().equals("") || ele_val.elementAt(i) != null) {
				rs_ele_name.addElement(ele_name.elementAt(i));
				rs_ele_val.addElement(ele_val.elementAt(i));
				rs_ele_lang_val.addElement(ele_lang_val.elementAt(i));
				rs_ele_path.addElement(ele_path.elementAt(i));
				rs_parent_group.addElement(parent_group.elementAt(i));
				rs_self_group.addElement(self_group.elementAt(i));
			}
		}
	}
	
	/**
	 * description: 객체 직렬화 수정.
	 * @return String[] idx
	 * @throws Exception
	 */
	public ArrayList setSerializer(String[][] orgTable, String org_state,
			Hashtable seqActivityTree, boolean isScorm, String courseSeq) throws Exception{
		
		ResultSet rs = null;
		ArrayList list = new ArrayList();
		Map<String, Object> serialMap;
//		HashMap map = new HashMap();
		try{
			for( int i=0; i<orgTable.length; i++ ){
				serialMap = new HashMap();
				SeqActivityTree mySeqActivityTree = (SeqActivityTree)seqActivityTree.get(orgTable[i][1].trim());
				mySeqActivityTree.setScrom(isScorm);
				if( mySeqActivityTree != null ){
					serialMap.put("orgState", org_state);
					serialMap.put("courseSeq", courseSeq);
					serialMap.put("serializer", this.write(mySeqActivityTree));
					list.add(serialMap);
				}
			}
//			map.put("serializerMap", list);
		}catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> updateSerializer Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		
		return list;
		
	}
	
	public static byte[] write(Object serialize)
	throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oop = new ObjectOutputStream(baos);
		oop.writeObject(serialize);
		oop.close();
		byte[] data = baos.toByteArray();
		return data;
	}
	
	
	/**
	 * LCMS_ITEM_RESOURCE 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 resource 항목 데이터.
	 * @param org_cd
	 *            기관코드
	 * @param item_seq
	 *            학습요소 key값
	 * @param userid
	 *            사용자ID
	 * @param prog_id
	 *            프로그램ID
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setResource( String[][] table,
			String userid, int rsrcCnt, String ims_path, String org_state,
			String group_seq, String to_folder,boolean flagtemplate) throws Exception {
		
		String sSqlText = null;
		String to_file = "";
		String sQueryId = null;
//		int lmt_Idx = 1;
		int tb_cnt = 0;
		// int j = 0;
		int maniseq = 0;
		String[][] retVal; 
		Map<String, Object> returnMap = new HashMap<String, Object>();
		ArrayList returnList = new ArrayList();
		try {
			
			tb_cnt = table.length;
			retVal = new String[tb_cnt][2];
			returnList = this.metadatainsert(tb_cnt,table,retVal,maniseq,to_folder,ims_path,sSqlText,flagtemplate, userid);
			
		}catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> updateSerializer Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return returnList;
	}
	
	public ArrayList metadatainsert(int tb_cnt,String[][] table,String[][] retVal,int maniseq,
			String to_folder,String ims_path,String sSqlText,boolean flagtemplate, String userid) throws Exception{
		Map<String, Object> returnMap;
		ArrayList returnList = new ArrayList();
		try{
			for (int i = 0; i < tb_cnt; i++) {
				returnMap = new HashMap();
				if (table[i][2] != null && table[i][2] != "") {
					retVal[i][0] = "";
					retVal[i][1] = table[i][1];
					returnMap.put("imsPath", ims_path);
					returnMap.put("toFolder", to_folder);
					returnMap.put("maniseq", maniseq);
					returnMap.put("rsrcId", table[i][1]);
					returnMap.put("rsrcTitle", table[i][2]);
					returnMap.put("rsrcType", table[i][3]);
					String rsrcHref = "";
					if (table[i][4].substring(0, 1).equals("/")) {
						rsrcHref =  table[i][4].replaceAll("\\\\", "/");
						// Href
					} else {
						rsrcHref =  "/" + table[i][4].replaceAll("\\\\", "/");
						// Href
					}
					returnMap.put("rsrcHref", rsrcHref);
					
//					if(!"null".equals(to_folder) && null != to_folder && !"".equals(to_folder)){
//						String rsrc_base_dir = to_folder;
//						String relatePath = rsrcHref.substring(0,rsrcHref.lastIndexOf("/"));
//						rsrc_base_dir = rsrc_base_dir.substring(Globals.CONTNET_REAL_PATH.length());
//						rsrc_base_dir += relatePath;
//						
////						int len = rsrc_base_dir.length();
////						returnMap.put("rsrcBaseDir", rsrc_base_dir.substring(0,len));
//						returnMap.put("rsrcBaseDir", rsrc_base_dir);
//					}else{	
//						returnMap.put("rsrcBaseDir", table[i][5]);
//					}
					
					returnMap.put("rsrcScormType", table[i][6]);
					returnMap.put("rsrcScoVersion", "1.00.00");
					returnMap.put("rsrcPstState", table[i][7]);
					returnMap.put("mataLocation", table[i][8]);
					returnMap.put("fileName", ims_path);
					returnMap.put("rsrcScoSize", FileController.getScoSize(ims_path + table[i][4].substring(0, table[i][4].lastIndexOf("/"))));
					
					
					
					if(!"null".equals(to_folder) && null != to_folder && !"".equals(to_folder)){
						String rsrc_base_dir = to_folder;
						String relatePath = rsrcHref.substring(0,rsrcHref.lastIndexOf("/"));
						//Globals.fileStorePath
						//String strSavePath = EgovProperties.getProperty("Globals.contentFileStore")+commandMap.get("userid")+"/"+commandMap.get("subj");
						rsrc_base_dir = rsrc_base_dir.substring( (EgovProperties.getProperty("Globals.contentFileStore").length()) + userid.length() )+ims_path.substring(ims_path.lastIndexOf("/"));
						
						
						rsrc_base_dir += relatePath;
//						int len = rsrc_base_dir.length();
//						returnMap.put("rsrcBaseDir", rsrc_base_dir.substring(1,len));
						returnMap.put("rsrcBaseDir", rsrc_base_dir);
						returnMap.put("rsrcBaseDirType", "Y");
					}else{
						returnMap.put("rsrcBaseDir", rsrcHref.substring(0, rsrcHref.lastIndexOf("/")));
						returnMap.put("rsrcBaseDirType", "N");
					}
					
					
					if (table[i][8] != null) {
						returnMap.put("metadataType", "Y");
						returnMap.put("objType", "SCO");
						returnMap.put("metaLocation", ims_path + File.separator + table[i][8]);
						
						if(flagtemplate){
							returnMap.put("flagtemplate", "Y");
						}else{
							returnMap.put("flagtemplate", "N");
						}
					}else{
						returnMap.put("metadataType", "N");
					}
				}
				returnList.add(returnMap);
			}	
			rsrc_no = retVal;
			
		}catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> metadatainsert Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return returnList;
	}
	
	
	public ArrayList getFile(String[][] table) throws Exception{
		Map<String, Object> returnMap;
		ArrayList list = new ArrayList();
		try{
			for( int i=0; i<table.length; i++ ){
				returnMap = new HashMap();
				
				returnMap.put("idx", table[i][0]);
				returnMap.put("fileType", table[i][1]);
				returnMap.put("fileHref", table[i][2]);
				list.add(returnMap);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
	 * LCMS_ITEM_RESOURCE 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 resource 항목 데이터.
	 * @param org_cd
	 *            기관코드
	 * @param course_seq
	 *            과정마스터 key값
	 * @param userid
	 *            사용자ID
	 * @param prog_id
	 *            프로그램ID
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList getDependency(String[][] table) throws Exception {
		int tb_cnt = 0;
		Map<String, Object> returnMap;
		ArrayList list = new ArrayList();
		try {

			tb_cnt = table.length;
			for (int i = 0; i < tb_cnt; i++) {
				returnMap = new HashMap();
				returnMap.put("rsrcIdx", table[i][0]);
				returnMap.put("rsrcIdRef", table[i][2]);
				
				list.add(returnMap);
			}
		}catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> getDependency Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	/**
	 * LCMS_ITEM 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 학습요소(item) 항목 데이터.
	 * @param org_cd
	 *            기관코드
	 * @param org_seq
	 *            학습요소묶음 key값
	 * @param userid
	 *            사용자 ID
	 * @param prog_id
	 *            프로그램 ID
	 * @return 사용된 학습요소 key값 배열
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setItem(
			String[][] table, String userid, String[][] rsrc_id, boolean pif,
			String org_state, String ims_path, String course_seq) throws Exception {
		
		int tb_cnt = 0;
		ArrayList list = new ArrayList();
		try {

			tb_cnt = table.length;
			Map<String, Object> returnMap;
			for (int i = 0; i < tb_cnt; i++) {
				returnMap = new HashMap();
				returnMap.put("courseCd", course_seq);
				returnMap.put("sampleItemYn", "N");
				returnMap.put("rsrcSeq", 0);
				returnMap.put("itemStartFile", 	table[i][10]);
				returnMap.put("parekey", 		table[i][2]);
				returnMap.put("itemIdRef", 		table[i][4]);
				returnMap.put("seqIdx", 		table[i][0]);
				returnMap.put("itemId", 		table[i][3]);
				returnMap.put("itemTitle", 		table[i][5]);
				returnMap.put("itemType", 		table[i][6]);
				returnMap.put("itemOpen", 		table[i][7]);
				returnMap.put("itemTlAction", 	table[i][8]);
				returnMap.put("itemMaxTime", 	NullCheck(table[i][9],"0"));
				returnMap.put("itemParameters", table[i][12]);
				returnMap.put("dataFromLms", 	table[i][13]);
				returnMap.put("itemThreshold", 	table[i][14]);
				returnMap.put("metaLocation", 	table[i][16]);
				returnMap.put("imsPath", 	ims_path);
				
				list.add(returnMap);
			}
		}catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> setItem Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	/**
	 * LCMS_SEQUENCING 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 계열화(sequencing) 항목 데이터.
	 * @param org_cd
	 *            기관코드
	 * @param item_seq
	 *            학습요소 key값
	 * @param org_seq
	 *            학습요소묶음 key값
	 * @param userid
	 *            사용자 ID
	 * @param prog_id
	 *            프로그램 ID
	 * @return
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setSequencing(String[][] table) throws Exception {

		int tb_cnt = 0;
		ArrayList list = new ArrayList();
		Map returnMap;
		try {
			tb_cnt = table.length;
			String hide_pre = "";
			String hide_cont = "";
			String hide_exit = "";
			String hide_abnd = "";

			for (int i = 0; i < tb_cnt; i++) {
				returnMap = new HashMap();
				// <adlnav:presentation> 처리.
				hide_pre = table[i][28].indexOf("previous") < 0 ? "" : "previous";
				hide_cont = table[i][28].indexOf("continue") < 0 ? "" : "continue";
				hide_exit = table[i][28].indexOf("exit") < 0 ? "" : "exit";
				hide_abnd = table[i][28].indexOf("abandon") < 0 ? "" : "abandon";

				// initializing
				table[i][3] = table[i][3].equals("") ? "true" : table[i][3]; // vSeqChoice
				table[i][4] = table[i][4].equals("") ? "true" : table[i][4]; // vSeqChoiceExit
				table[i][5] = table[i][5].equals("") ? "false" : table[i][5]; // vSeqFlow
				table[i][6] = table[i][6].equals("") ? "false" : table[i][6]; // vSeqForwardOnly
				table[i][7] = table[i][7].equals("") ? "true" : table[i][7]; // vSeqUseAttemptObjInfo
				table[i][8] = table[i][8].equals("") ? "true" : table[i][8]; // vSeqUseAttemptProgressInfo
				table[i][9] = table[i][9].equals("") ? "0" : table[i][9]; // vSeqAttemptLimit
				table[i][10] = table[i][10].equals("") ? "0.0" : table[i][10]; // vSeqAttemptDurationLimit
				table[i][11] = table[i][11].equals("") ? "never" : table[i][11]; // vSeqRandomTiming
				table[i][12] = table[i][12].equals("") ? "0" : table[i][12]; // vSeqSelectCount
				table[i][13] = table[i][13].equals("") ? "false" : table[i][13]; // vSeqReorderChildren
				table[i][14] = table[i][14].equals("") ? "never" : table[i][14]; // vSeqSelectionTiming
				table[i][15] = table[i][15].equals("") ? "true" : table[i][15]; // vSeqTracked
				table[i][16] = table[i][16].equals("") ? "false" : table[i][16]; // vSeqCompletSetByContent
				table[i][17] = table[i][17].equals("") ? "false" : table[i][17]; // vSeqObjSetByContent
				table[i][18] = table[i][18].equals("") ? "false" : table[i][18]; // vSeqPreventActivation
				table[i][19] = table[i][19].equals("") ? "false" : table[i][19]; // vSeqConstrainChoice
				table[i][20] = table[i][20].equals("") ? "always" : table[i][20]; // vSeqReqSatisfied
				table[i][21] = table[i][21].equals("") ? "always" : table[i][21]; // vSeqReqNotSatisfied
				table[i][22] = table[i][22].equals("") ? "always" : table[i][22]; // vSeqReqCompleted
				table[i][23] = table[i][23].equals("") ? "always" : table[i][23]; // vSeqReqIncomplete
				table[i][24] = table[i][24].equals("") ? "false" : table[i][24]; // vSeqMeasureSatIfActive
				table[i][25] = table[i][25].equals("") ? "true" : table[i][25]; // vSeqRollupObjSatisfied
				table[i][26] = table[i][26].equals("") ? "true" : table[i][26]; // vSeqRollupProgressComplet
				table[i][27] = table[i][27].equals("") ? "1.0000" : table[i][27]; // vSeqObjMeasureWeight

				
				returnMap.put("orgIndex", table[i][0].equals("") ? "0" : table[i][0]);
				returnMap.put("itemIndex", table[i][1].equals("") ? "0" : table[i][1]);

				returnMap.put("choice", table[i][3]);
				returnMap.put("choiceExit", table[i][4]);

				returnMap.put("flow", table[i][5]);
				returnMap.put("forwardOnly", table[i][6]);
				returnMap.put("useAttemptObjInfo", table[i][7]);
				returnMap.put("useAttemptProgressInfo", table[i][8]);

				returnMap.put("attemptLimit", table[i][9]);
				returnMap.put("attemptDurationLimit", table[i][10]);
				returnMap.put("randomTiming", table[i][11]);
				returnMap.put("selectCount", table[i][12]);

				returnMap.put("reorderChildren", table[i][13]);
				returnMap.put("selectionTiming", table[i][14]);
				returnMap.put("tracked", table[i][15]);
				returnMap.put("completSetbyContent", table[i][16]);
				

				returnMap.put("objSetbyContent", table[i][17]);
				returnMap.put("preventActivation", table[i][18]);
				returnMap.put("constrainChoice", table[i][19]);
				returnMap.put("requiredSatisfied", table[i][20]);

				returnMap.put("requiredNotSatisfied", table[i][21]);
				returnMap.put("requiredCompleted", table[i][22]);
				returnMap.put("requiredIncompleted", table[i][23]);
				returnMap.put("measureSatisfiIfAction", table[i][24]);

				returnMap.put("rollupObjSatisfied", table[i][25]);
				returnMap.put("rollupProgressCompletion", table[i][26]);
				returnMap.put("objMeasureWeight", table[i][27]);
				returnMap.put("hideUiPre", hide_pre); // HIDE_UI_PRE

				returnMap.put("hideUiCon", hide_cont); // HIDE_UI_CON
				returnMap.put("hideUiEx", hide_exit); // HIDE_UI_EX
				returnMap.put("hideUiAbd", hide_abnd); // HIDE_UI_ABD

				list.add(returnMap);
			}
		} catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> setSequencing Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	
	/**
	 * SEQ_CONDITION_TYPE 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 규칙조건타입(seq_condition_type) 항목 데이터.
	 * @param cond_seq
	 *            계열화 번호(sequencing key)
	 * @param userid
	 *            사용자 ID
	 * @param prog_id
	 *            프로그램 ID
	 * @return 규칙조건타입 key 값
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setConditionType( String[][] table ) throws Exception {
		int tb_cnt = 0;
		ArrayList list = new ArrayList();
		Map<String, Object> returnMap;
		try {

			tb_cnt = table.length;
			for (int i = 0; i < tb_cnt; i++) {

				table[i][4] = table[i][4].equals("") ? "all" : table[i][4];// vTSeqcondiCombination
				returnMap = new HashMap();
				returnMap.put("idx", table[i][0].equals("") ? "0" : table[i][0]);
				returnMap.put("conditionRuleType", table[i][2]);
				returnMap.put("ruleAction", table[i][3]);
				returnMap.put("conditioncombination", table[i][4]);
				list.add(returnMap);
			}
		} catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> setConditionType Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	/**
	 * SEQ_RULE_CONDITION 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 규칙조건(rule_condition) 항목 데이터.
	 * @param cond_seq
	 *            규칙조건타입(condition_type) key 값
	 * @param userid
	 *            사용자 ID
	 * @param prog_id
	 *            프로그램 ID
	 * @return 규칙조건(rule_condition) key 값
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setRuleCondition( String[][] table ) throws Exception {

		ArrayList list = new ArrayList();
		Map returnMap;
		try {
			for (int i = 0; i < table.length; i++) {
				returnMap = new HashMap();

				table[i][4] = table[i][4].equals("") ? "0.0" : table[i][4];
				table[i][5] = table[i][5].equals("") ? "noOp" : table[i][5];
				table[i][6] = table[i][6].equals("") ? "always" : table[i][6];

				returnMap.put("index", table[i][1].equals("") ? "0" : table[i][1]);
				returnMap.put("referenceObjective", table[i][3]);
				returnMap.put("measureThreshold", table[i][4]);
				returnMap.put("operator", table[i][5]);
				returnMap.put("condition", table[i][6]);
				
				list.add(returnMap);
			}
		} catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> setRuleCondition Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	/**
	 * SEQ_ROLLUP_RULE 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 롤업규칙(rollup_rule) 항목 데이터.
	 * @param cond_seq
	 *            계열화 번호(sequencing key)
	 * @param userid
	 *            사용자 ID
	 * @param prog_id
	 *            프로그램 ID
	 * @return 롤업규칙(rollup_rule) key 값
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setRollupRule( String[][] table ) throws Exception {
		ArrayList list = new ArrayList();
		Map returnMap;
		try {
			for (int i = 0; i < table.length; i++) {
				returnMap = new HashMap();

				table[i][2] = table[i][2].equals("") ? "all" : table[i][2];
				table[i][3] = table[i][3].equals("") ? "0" : table[i][3];
				table[i][4] = table[i][4].equals("") ? "0.0000" : table[i][4];
				returnMap.put("index", table[i][0].equals("") ? "0" : table[i][0]);
				returnMap.put("childActivitySet", table[i][2]);
				returnMap.put("minimumCount", table[i][3]);
				returnMap.put("minimumPercent", table[i][4]);
				returnMap.put("rollupAction", table[i][5]);
				returnMap.put("conditionCombination", table[i][6]);

				list.add(returnMap);
			}
		} catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> setRollupRule Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	/**
	 * SEQ_ROLLUP_CONDITION 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 롤업조건(rollup_condition) 항목 데이터.
	 * @param cond_seq
	 *            롤업규칙(rollup_rule) key 값
	 * @param userid
	 *            사용자 ID
	 * @param prog_id
	 *            프로그램 ID
	 * @return 롤업조건(rollup_condition) key 값
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setRollupCondition( String[][] table ) throws Exception {

		ArrayList list = new ArrayList();
		Map returnMap;
		try {
			for (int i = 0; i < table.length; i++) {
				returnMap = new HashMap();
				table[i][3] = table[i][3].equals("") ? "noOp" : table[i][3];
				
				returnMap.put("index", table[i][1].equals("") ? "0" : table[i][1]);
				returnMap.put("operator", table[i][3]);
				returnMap.put("condition", table[i][4]);
				
				list.add(returnMap);
			}
		} catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> setRollupCondition Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	/**
	 * SEQ_OBJECTIVE 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 학습목표(objective) 항목 데이터.
	 * @param sequencing_seq
	 *            계열화 번호(sequencing key)
	 * @param userid
	 *            사용자 ID
	 * @param prog_id
	 *            프로그램 ID
	 * @return 학습목표(objective) key 값
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setObjective( String[][] table ) throws Exception {
		ArrayList list = new ArrayList();
		Map returnMap;
		try {

			for (int i = 0; i < table.length; i++) {
				returnMap = new HashMap();

				table[i][3] = table[i][3].equals("") ? "false" : table[i][3];
				table[i][4] = table[i][3].equals("true") && table[i][4].equals("") ? "1.0" : table[i][4]; // satisfiedByMeasure
				// 가
				// true
				// 이고,
				// minnormalizedMeasuer가
				// ""
				// 일때만
				// default
				// set.

				returnMap.put("index", table[i][0].equals("") ? "0" : table[i][0]);
				returnMap.put("objId", table[i][2]);
				returnMap.put("satisfiedMeasure", table[i][3]);
				if( !table[i][4].equals("") ){
					returnMap.put("minnormalMeasure", table[i][4]);
				}
				returnMap.put("objType", table[i][5]);
				
				list.add(returnMap);
			}
		} catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> setObjective Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	
	/**
	 * SEQ_MAP_INFO 테이블 INSERT
	 * 
	 * @param table
	 *            파싱된 맵정보(map_info) 항목 데이터.
	 * @param cond_seq
	 *            학습목표 번호(objective key)
	 * @param userid
	 *            사용자 ID
	 * @param prog_id
	 *            프로그램 ID
	 * @return 맵정보(map_info) key값
	 * @throws SQLException
	 * @throws CmException
	 */
	public ArrayList setMapInfo(String[][] table) throws Exception {
		ArrayList list = new ArrayList();
		Map returnMap;
		try {
			for (int i = 0; i < table.length; i++) {
				returnMap = new HashMap();

				table[i][4] = table[i][4].equals("") ? "true" : table[i][4];
				table[i][5] = table[i][5].equals("") ? "true" : table[i][5];
				table[i][6] = table[i][6].equals("") ? "false" : table[i][6];
				table[i][7] = table[i][7].equals("") ? "false" : table[i][7];

				returnMap.put("index", table[i][1].equals("") ? "0" : table[i][1]);
				
				returnMap.put("targetObjId", table[i][3]);
				returnMap.put("readStatus", table[i][4]);
				returnMap.put("readMeasure", table[i][5]);
				returnMap.put("writeStatus", table[i][6]);
				returnMap.put("writeMeasure", table[i][7]);
				
				list.add(returnMap);
			}
		} catch(Exception ex){
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> setObjective Exception");
			log.info(" ========== >>>>>>>>>>>>>>>>>>>>>> exception : "+ex.getMessage());
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String NullCheck(String val, String val1)
	{
		if(val == null  || val.equals("null") ||"".equals(val))
			return val1;
		else
			return val;
	}
	
	
	public static Object read(Object serialize)
	throws Exception {
		Object obj = null;
		
		if(serialize != null){
			Blob tbblob =  (Blob)serialize;
			InputStream is = tbblob.getBinaryStream();
			ObjectInputStream iop = new ObjectInputStream(is);
			obj = iop.readObject();
			iop.close();
			is.close(); 
		}
		return obj;
	}
	
	public static void bind(Map input, Object obj)
	throws Exception {
	    new WebDataBinder(obj).bind(new MutablePropertyValues(input));
	    setUpdInfo(input, obj);
	}
	
	public static void setUpdInfo(Map input, Object domain)
	    throws Exception
	{
	    if ((input == null) || (domain == null)) {
	      return;
	    }
	    invokeMethod(domain, "setCremanConnectId", String.class, input.get("cremanConnectId"));
	    invokeMethod(domain, "setCreClass", String.class, input.get("creClass"));
	    invokeMethod(domain, "setCremanConnectIp", String.class, input.get("cremanConnectIp"));
	    invokeMethod(domain, "setUpdmanConnectId", String.class, input.get("updmanConnectId"));
	    invokeMethod(domain, "setUpdClass", String.class, input.get("updClass"));
	    invokeMethod(domain, "setUpdmanConnectIp", String.class, input.get("updmanConnectIp"));
	    invokeMethod(domain, "setLuserid", String.class, input.get("luserid"));
	    invokeMethod(domain, "setInuserid", String.class, input.get("inuserid"));

	 }
	
	  public static void invokeMethod(Object object, String methodName, Class parameterType, Object argument)
	    throws Exception
	  {
	    Class[] parameterTypes = new Class[1];
	    parameterTypes[0] = parameterType;

	    Object[] arguments = new Object[1];
	    arguments[0] = argument;

	    invokeMethod(object, methodName, parameterTypes, arguments);
	  }
	  
	  public static void invokeMethod(Object object, String methodName, Class[] parameterTypes, Object[] arguments)
	    throws Exception
	  {
	    if (object == null) {
	      return;
	    }
	    if (methodName == null)
	      return;
	    try
	    {
	      Method method = object.getClass().getMethod(methodName, parameterTypes);

	      if (method == null) {
	        return;
	      }
	      method.invoke(object, arguments);
	    }
	    catch (NoSuchMethodException localNoSuchMethodException)
	    {
	    }
	  }
}
