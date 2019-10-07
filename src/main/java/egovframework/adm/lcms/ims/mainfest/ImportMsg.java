package egovframework.adm.lcms.ims.mainfest;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


public class ImportMsg {
	// DEBUG FLAG
    //public static boolean DEBUG = Constants.DEBUG_MODE;
    public static boolean DEBUG = false;

	// Manifest Parsing & Validation (0~49)
	public static final int STEP_DEFAULT 					= 0;
	public static final int STEP_MANI_DEFAULT 				= 1;
	public static final int STEP_MANI_PATH 					= 2;
	public static final int STEP_MANI_PARSE_ROOT 			= 3;
	public static final int STEP_MANI_PARSE_IDENTIFIER 		= 4;
	public static final int STEP_MANI_PARSE_CHILD 			= 5; 
	public static final int STEP_MANI_PARSE_META 			= 6;
	public static final int STEP_MANI_PARSE_META_LOCATION 	= 7;
	public static final int STEP_MANI_PARSE_ORGANIZATIONS 	= 8;
	public static final int STEP_MANI_PARSE_RESOURCES 		= 9;
	public static final int STEP_MANI_PARSE_ORGANIZATION 	= 10;
	public static final int STEP_MANI_PARSE_ITEM 			= 11;
	public static final int STEP_MANI_PARSE_RESOURCE 		= 12;
	public static final int STEP_MANI_PARSE_ITEM_SEQUENCING = 13;
	public static final int STEP_MANI_PARSE_ORGANIZATION_SEQUENCING = 14;
	
	public static final int STEP_MANI_VALID_WELLFORMED		= 15;
	public static final int STEP_MANI_VALID_SCHEMA			= 16;
	public static final int STEP_MANI_VALID_APPLICATIONPROFILE 		= 17;
	public static final int STEP_MANI_VALID_REQUIREDCPFILESEXIST 	= 18;
	
	// Manifest Database (50~99)
	public static final int STEP_MANI_DB_GET_COURSE 		= 50;
	public static final int STEP_MANI_DB_SET_COURSE 		= 51;
	public static final int STEP_MANI_DB_SET_ORGANIZATION 	= 52;
	public static final int STEP_MANI_DB_SET_RESOURCE 		= 53;
	public static final int STEP_MANI_DB_SET_FILE 			= 54;
	public static final int STEP_MANI_DB_SET_DEPENDENCY 	= 55;
	public static final int STEP_MANI_DB_SET_ITEM 			= 56;
	public static final int STEP_MANI_DB_SET_SEQUENCING 	= 57;
	public static final int STEP_MANI_DB_SET_COURSE_ITEM 	= 58;
	public static final int STEP_MANI_DB_SET_SERIALIZER		= 59;

	// KEM Parsing (100~149)
	public static final int STEP_KEM_DEFAULT 				= 100;
	public static final int STEP_KEM_DB_GET_META_LOCATION 	= 101;
	public static final int STEP_KEM_FILE_NOT_FOUND 		= 102;
	public static final int STEP_KEM_PARSE_GENERAL 			= 103;
	public static final int STEP_KEM_PARSE_LIFECYCLE 		= 104;
	public static final int STEP_KEM_PARSE_METAMETADATA 	= 105;
	public static final int STEP_KEM_PARSE_TECHNICAL 		= 106;
	public static final int STEP_KEM_PARSE_EDUCATIONAL 		= 107;
	public static final int STEP_KEM_PARSE_RIGHTS 			= 108;
	public static final int STEP_KEM_PARSE_RELATION 		= 109;
	public static final int STEP_KEM_PARSE_ANNOTATION 		= 110;
	public static final int STEP_KEM_PARSE_CLASSIFICATION 	= 111;
	
	// KEM Database (150~199)
	public static final int STEP_KEM_DB_DELETE_META 		= 150;
	public static final int STEP_KEM_DB_SET_BASICINFO 		= 151;
	public static final int STEP_KEM_DB_SET_CLASSIFICATION 	= 152;
	public static final int STEP_KEM_DB_SET_GENERAL 		= 153;
	public static final int STEP_KEM_DB_SET_LIFECYCLE 		= 154;
	public static final int STEP_KEM_DB_SET_METAMETADATA 	= 155;
	public static final int STEP_KEM_DB_SET_RELATION 		= 156;
	public static final int STEP_KEM_DB_SET_TECHNICAL 		= 157;
	public static final int STEP_KEM_DB_SET_EDUCATIONAL 	= 158;
	public static final int STEP_KEM_DB_SET_ANNOTATION 		= 159;
	
	// File Upload (200~249)
	public static final int STEP_PIF_DEFAULT				= 200;
	public static final int STEP_PIF_UPLOAD					= 201;
	public static final int STEP_PIF_UNZIP					= 202;
	public static final int STEP_PIF_DIR					= 203;
	public static final int STEP_PIF_ORG					= 204;
	public static final int STEP_PIF_MANI_COPY				= 205;
	public static final int STEP_PIF_EXT_VALIDATION			= 206;
	
	// STEP Completed
	public static final int STEP_COMPLETED 					= 300;
	
	// Manifest Parsing Warning
	public static final int WARN_MANI_PARSE_NONE_IDENTIFIER 			= 2301;
	public static final int WARN_MANI_PARSE_NONE_META_LOCATION 			= 302;
	public static final int WARN_MANI_PARSE_NONE_ORGANIZATIONS_DEFAULT 	= 303;
	public static final int WARN_MANI_PARSE_NONE_ORGANIZATION_IDENTIFIER 	= 304;
	public static final int WARN_MANI_PARSE_NONE_ITEM_IDENTIFIER 		= 305;
	public static final int WARN_MANI_PARSE_NONE_ITEM_PARENT_IDENTIFIER = 306;
	public static final int WARN_MANI_PARSE_NONE_RESOURCE_IDENTIFIER 	= 307;
	
	//private int nowStep = 0;
	private Hashtable nowStep 			= null;
	private Hashtable addOnStepMsg		= null;
	private Hashtable msgTable 			= null;
	private Hashtable errMsgs 			= null;
	private Hashtable addOnMsg			= null;
	private ArrayList sessionID 		= null;
	private static ImportMsg instance 	= null;
	
	public static ImportMsg getInstance() {
		if (instance == null) {
			instance = new ImportMsg();
		}
		return instance;
	}
	
	private ImportMsg() {
//		CmProperties prop = new CmProperties();
//		try {
//			URL url = this.getClass().getClassLoader().getResource("import_msg.properties");
//			String msgFilePath = url.getPath();
//			prop.loadFromFile(msgFilePath);
//
//		}catch(Exception e) {
//			e.printStackTrace();
//		}

		sessionID 	= new ArrayList();
		msgTable 	= new Hashtable();
		nowStep 	= new Hashtable();
		addOnStepMsg= new Hashtable();
		addOnMsg	= new Hashtable();
		msgTable.put(new Integer(STEP_DEFAULT), 		
				"과정등록중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DEFAULT), 	
				"Manifest 작업중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PATH), 		
				"Manifest File을 찾는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_ROOT), 
				"Manifest ROOT Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_IDENTIFIER), 
				"Manifest Identifier를 찾을수 없습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_CHILD), 
				"Manifest 하위 Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_META), 
				"Manifest의 Meta Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_META_LOCATION), 
				"Manifest의 Meta Location Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_ORGANIZATIONS), 
				"Manifest의 Organizations Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_RESOURCES), 
				"Manifest의 Resources Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_ORGANIZATION), 
				"Manifest의 Organization Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_ITEM), 
				"Manifest의 Item Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_RESOURCE), 
				"Manifest의 Resource Element Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_ITEM_SEQUENCING), 
				"Manifest의 Item Sequencing 정보 Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_PARSE_ORGANIZATION_SEQUENCING), 
				"Manifest의 Organization Sequencing 정보 Parsing중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_GET_COURSE), 
				"과정 정보를 가져오는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_COURSE), 
				"과정 정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_ORGANIZATION), 
				"Organization정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_RESOURCE), 
				"Resource정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_FILE), 
				"Resource의 File정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_DEPENDENCY), 
				"Resource의 Dependency정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_ITEM), 
				"Item정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_SEQUENCING), 
				"Sequencing정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_COURSE_ITEM), 
				"과정객체(COURSE_ITEM)정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_DB_SET_SERIALIZER),
				"Serializer정보를 입력하는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_MANI_VALID_WELLFORMED), 
				"ADL SCORM2004 Validation Check :\\nXML의 WELL-FORMED 형식에 맞지 않는 메니피스트 파일입니다.");
		msgTable.put(new Integer(STEP_MANI_VALID_SCHEMA), 
				"ADL SCORM2004 Validation Check :\\nXML SCHEMA에 유효 하지 않는 메니피스트 파일 입니다.");
		msgTable.put(new Integer(STEP_MANI_VALID_APPLICATIONPROFILE), 
				"ADL SCORM2004 Validation Check :\\nSCORM 어플리케이션 프로파일에 유효하지 않는 메니피스트 파일 입니다.");
		msgTable.put(new Integer(STEP_MANI_VALID_REQUIREDCPFILESEXIST), 
				"ADL SCORM2004 Validation Check :\\n필수 파일들이 존재 하지 않습니다.");
		msgTable.put(new Integer(STEP_KEM_DEFAULT), 
				"KEM Meta Data 처리중  에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_GET_META_LOCATION), 
				"KEM Meta Location정보를 가져오는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_FILE_NOT_FOUND), 
				"KEM Meta 정보 파일을 찾을수 없습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_GENERAL), 
				"Meta Data Parsing(<general>)중  에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_LIFECYCLE), 
				"Meta Data Parsing(<lifecycle>)중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_METAMETADATA), 
				"Meta Data Parsing(<metaMetadata>)중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_TECHNICAL), 
				"Meta Data Parsing(<technical>)중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_EDUCATIONAL), 
				"Meta Data Parsing(<educational>)중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_RIGHTS), 
				"Meta Data Parsing(<rights>)중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_RELATION), 
				"Meta Data Parsing(<relation>)중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_ANNOTATION), 
				"Meta Data Parsing(<annotation>)중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_PARSE_CLASSIFICATION), 
				"Meta Data Parsing(<classification>)중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_DELETE_META), 
				"기존 메타정보 삭제중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_BASICINFO), 
				"Meta 기본정보(KEM_BASIC_INFO) 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_CLASSIFICATION), 
				"Meta CLASSIFICATION 정보 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_GENERAL), 
				"Meta GENERAL 정보 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_LIFECYCLE), 
				"Meta LIFECYCLE 정보 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_METAMETADATA), 
				"Meta META_METADATA 정보 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_RELATION), 
				"Meta RELATION 정보 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_TECHNICAL), 
				"Meta TECHNICAL 정보 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_EDUCATIONAL), 
				"Meta EDUCATIONAL 정보 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_KEM_DB_SET_ANNOTATION), 
				"Meta ANNOTATION 정보 입력중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_PIF_DEFAULT),
				"PIF 파일 처리중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_PIF_UPLOAD),
				"PIF 파일 업로드중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_PIF_UNZIP),
				"PIF 파일 압축해제중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_PIF_DIR),
				"PIF 파일 처리중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_PIF_ORG),
				"과정정보를 가져오는중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_PIF_MANI_COPY),
				"압축파일내에 imsmanifest.xml 파일이 없습니다.");
		msgTable.put(new Integer(STEP_PIF_EXT_VALIDATION),
				"PIF 압축포맷 검사중 에러가 발생하였습니다.");
		msgTable.put(new Integer(STEP_COMPLETED),
				" 모든작업이 종료되었습니다.");
		msgTable.put(new Integer(WARN_MANI_PARSE_NONE_IDENTIFIER), 
				"Manifest의 Identifier 정보가 없습니다.");
		msgTable.put(new Integer(WARN_MANI_PARSE_NONE_META_LOCATION), 
				"Manifest의 Meta Location 정보가 없습니다.");
		msgTable.put(new Integer(WARN_MANI_PARSE_NONE_ORGANIZATIONS_DEFAULT), 
				"Manifest의 Organizations Default 정보가 없습니다.");
		msgTable.put(new Integer(WARN_MANI_PARSE_NONE_ORGANIZATION_IDENTIFIER), 
				"Manifest의 Organization Identifier 정보가 없습니다.");
		msgTable.put(new Integer(WARN_MANI_PARSE_NONE_ITEM_IDENTIFIER), 
				"Manifest의 Item Identifier 정보가 없습니다.");
		msgTable.put(new Integer(WARN_MANI_PARSE_NONE_ITEM_PARENT_IDENTIFIER), 
				"Manifest의 Item 상위 Element에 대한 Identifier 정보가 없습니다.");
		msgTable.put(new Integer(WARN_MANI_PARSE_NONE_RESOURCE_IDENTIFIER), 
				"Manifest의 Resource Identifier 정보가 없습니다.");
	}

	/**
	 * 현재 request에 대하여 메시지를 초기화 시킴
	 * @param requestId
	 * @return request의 requestSessionId
	 */
	public String init(String requestId) {
		//String ID = sessionID.size() + "_" +System.currentTimeMillis();
		if (sessionID != null) {
			for ( int i = 0 ; i < sessionID.size() ; i++) {
				String val = (String)sessionID.get(i);
				if (requestId.equals(val)) {
					break;
				}else {
					if (i+1 <= sessionID.size()) sessionID.add(requestId);
				}
			}
		}
		if (errMsgs == null) errMsgs = new Hashtable();
		if (addOnMsg == null) addOnMsg = new Hashtable();
		return requestId;
	}

	/**
	 * 현재까지의 에러(경고)의 수를 리턴 
	 * @param requestId
	 * @return
	 */
	public int getErrSize(String requestId) {
		int ret = 0;
		Enumeration eKey = errMsgs.keys();
		
		while(eKey.hasMoreElements()) {
			String key = (String)eKey.nextElement();
			if (key.indexOf(requestId) > -1) ret++;
		}

		return ret;
	}
	
	/**
	 * 현재 request에 대한 에러 내용을 리턴
	 * @param requestId
	 * @return Hashtable
	 */
	public Hashtable getError(String requestId) {
		Hashtable ret = new Hashtable();
		Enumeration eKey = errMsgs.keys();
		
		while(eKey.hasMoreElements()) {
			String key = (String)eKey.nextElement();
			if (key.indexOf(requestId) > -1) {
				ret.put(key, errMsgs.get(key));
			}
		}
		return ret;
	}
	
	/**
	 * 현재 request에 대해 error(warning)을 세팅
	 * @param requestId
	 * @param errMsgFlag
	 */
	public void setError(String requestId, int errMsgFlag) {
		String msg = (String)msgTable.get(new Integer(errMsgFlag));
		errMsgs.put(requestId + "_" + errMsgFlag, msg);
	}
	
	/**
	 * 현재 request에 대해 errMsg를 세팅
	 * @param requestId
	 * @param errMsgFlag
	 * @param errMsg
	 */
	public void setError(String requestId, int errMsgFlag, String errMsg) {
		String msg = (String)msgTable.get(new Integer(errMsgFlag));
		errMsgs.put(requestId + "_" + errMsgFlag, msg);
		String _errMsg = errMsg.toString().replaceAll("\"", "'").replaceAll("\\\\", "/");
		addOnMsg.put(requestId + "_" + errMsgFlag, _errMsg);
	}
	
	/**
	 * 현재 request의 작업 진행중인 Step을 세팅
	 * @param requestId
	 * @param errMsgFlag
	 */
	public void setNowStep(String requestId, int errMsgFlag) {
		nowStep.put(requestId, new Integer(errMsgFlag));
	}
	
	/**
	 * 현재 request의 작업 진행중인 Step을 errMsg로 세팅함.
	 * @param requestId
	 * @param errMsgFlag
	 * @param errMsg
	 */
	public void setNowStep(String requestId, int errMsgFlag, String errMsg) {
		nowStep.put(requestId, new Integer(errMsgFlag));
		String msg = (String)addOnStepMsg.get(requestId);
		msg = errMsg;
		addOnStepMsg.put(requestId, msg);
	}
	
	/**
	 * 현재 request의 작업 진행중인 Step을 Exception으로 세팅함.
	 * @param requestId
	 * @param errMsgFlag
	 * @param e
	 */
	public void setNowStep(String requestId, int errMsgFlag, Exception e) {
		if (requestId != null && errMsgFlag >=0 && e != null) {
			String errMsg = null;
			if (e.getCause() != null) {
				errMsg = e.getCause().toString().replaceAll("\"", "'").replaceAll("\\\\", "/");
				this.setNowStep(requestId, errMsgFlag, errMsg);
			}else {
				this.setNowStep(requestId, errMsgFlag);
			}
		}
	}
	
	/**
	 * 마지막 진행했던 Step의 메시지 값을 가져옴.
	 * @param requestId
	 * @return
	 */
	public int getLastStep(String requestId) {
		return ((Integer)nowStep.get(requestId)).intValue();
	}
	
	/**
	 * 마지막 진행했던 Step의 메시지를 가져옴.
	 * @param requestId
	 * @return
	 */
	public String getLastStepMsg(String requestId) {
		return (String)msgTable.get(nowStep.get(requestId));
	}

	/**
	 * error(warning)에 추가 지정된 메시지를 가져옴.
	 * @param msgKey
	 * @return
	 */
	public String getAddOnMsg(String msgKey) {
		Object obj = addOnMsg.get(msgKey);
		String ret = null;
		if (obj != null) ret = obj.toString();
		return ret;
	}
	
	/**
	 * 마지막진행했던 Step의 추가 메시지를 가져옴.
	 * @param stepKey
	 * @return
	 */
	public String getAddOnStepMsg(String stepKey) {
		Object obj = addOnStepMsg.get(stepKey);
		String ret = null;
		if (obj != null) ret = obj.toString();
		return ret;
	}
	
	/**
	 * 현재 request에 세팅된 메시지 내용을 모두 지움.
	 * @param requestId
	 */
	public void closeMsgByMsgId(String requestId) {
		sessionID.remove(requestId);
		nowStep.remove(requestId);
		addOnStepMsg.remove(requestId);
		errMsgs.remove(requestId);
		addOnMsg.remove(requestId);
	}
}
