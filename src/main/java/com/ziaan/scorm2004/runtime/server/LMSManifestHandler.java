/*
 * @(#)LMSManifestHandler.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004.runtime.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.scorm2004.ScormBlobBean;
import com.ziaan.scorm2004.common.StringUtil;
import com.ziaan.scorm2004.parsers.dom.DOMTreeUtility;
import com.ziaan.scorm2004.sequencer.ADLSeqUtilities;
import com.ziaan.scorm2004.sequencer.SeqActivityTree;
import com.ziaan.scorm2004.validator.ADLValidatorOutcome;
import com.ziaan.scorm2004.validator.LOMMetadata;
import com.ziaan.scorm2004.validator.contentpackage.CPValidator;
import com.ziaan.scorm2004.validator.contentpackage.LaunchData;
import com.ziaan.scorm2004.validator.contentpackage.ManifestHandler;
import com.ziaan.scorm2004.validator.contentpackage.MetadataData;
import com.ziaan.scorm2004.validator.contentpackage.ObjectivesData;
import com.ziaan.scorm2004.validator.contentpackage.ObjectivesMapInfoData;
import com.ziaan.scorm2004.validator.contentpackage.RollupRuleConditionData;
import com.ziaan.scorm2004.validator.contentpackage.RollupRuleData;
import com.ziaan.scorm2004.validator.contentpackage.SeqRuleConditionData;
import com.ziaan.scorm2004.validator.contentpackage.SeqRuleData;
import com.ziaan.scorm2004.validator.contentpackage.SequencingData;

/**
 * IMSManifest.xml File의 Validation 수행 클래스
 * 
 * 1. 업로드된 파일이 PIF 일 경우,
 *    tempUploads 디렉토리에 ZIP 파일 업로드 후
 *    임시로 PackageImport 디렉토리에 압축을 푼 후 imsmanifest.xml file 유효성 검사를 실시한다.
 * 
 *    업로드된 파일이 imsmanifest.xml file 일 경우, 
 *    tempUploads 디렉토리에 XML 파일 업로드 후 유효성 검사를 실시한다.
 * 
 * 2. 유효성 검사를 통과하면,
 *    PackageImport 디렉토리 내의 파일들을 컨텐츠 업로드 경로/CourseCode(임의값) 디렉토리에 복사한다.
 *    PackageImport 디렉토리 내의 파일은 삭제한다.
 * 
 *    유효성 검사에 실패하면,
 *    PackageImport 디렉토리 내의 파일을 삭제한다.
 *
 * @version 1.0 2006. 4. 24.
 * @author Jin-pil Chung
 *
 */
public class LMSManifestHandler implements Serializable 
{
    private static boolean _Debug = false;
    
   /**
    * ADLValidatorOutcome 클래스에서 반환될 DOM 객체. 
    * imsmanifest.xml file의 모든 정보를 담고 있다.
    */
   protected Document mDocument;

   /**
    * COURSE_CODE 값
    */
   protected String mCourseCode;

   /**
    * COURSE_NM 값
    */
   protected String mCourseName;

   /**
    * form의 Parameter 값들을 갖고 있는 Map
    */
   protected Map mFormMap;
   
   /**
    * 웹 경로 (DB에 sco,asset 정보 넣을 때 사용)
    * 예) /SCORM/CourseImports
    */
   protected String mWebPath;   
   
   /**
    * 실제 경로 (파일 복사시 사용)
    * 예) d:\tomcat\webapps\SCORM\CourseImports
    */
   protected String mRealPath;
   
   /**
    * schema xsd files의 경로
    */
   private String mXSDLocation;

   /**
    * organization element의 list 
    */
   protected Vector mOrganizationList;

   /**
    * database에 저장될 LaunchData 객체들의 리스트
    * organization의 item과 resource element의 정보와 mapping 되어 있다.
    */
   protected Vector mLaunchDataList;
   
   /**
    * database에 저장될 SequencingData 객체들의 리스트
    * organization과 item의 sequencig 정보와 mapping 되어 있다.
    */
   protected Vector mSequencingDataList;
   
   /**
    * database에 저장될 MetaData 객체들의 리스트
    */
   protected Vector mMetaDataList;
   
   /**
    * imsmanifest.xml file의 manifest element
    */
   private Node mManifest;

   /**
    * Logger object used for debug logging.
    */
   private Logger mLogger = Logger.getLogger("org.adl.util.debug.samplerte");

   /**
    * Default constructor method which initializes member variables
    * 
    * @param iXSDLocation
    *           The location where the XSDs can be found for use 
    *           during validation.
    */ 
   public LMSManifestHandler( String iXSDLocation )
   {
      mDocument = null;
      mCourseCode = "";
      mCourseName = "";
      mFormMap = null;
      mWebPath = "";
      mRealPath = "";
      mXSDLocation = iXSDLocation;
      mOrganizationList = new Vector();
      mLaunchDataList = new Vector();
      mSequencingDataList = new Vector();
      mMetaDataList = new Vector();
      mManifest = null;
   }

   /**
    * Sets up the String of schema locations
    *
    * @param iExtendedSchemaLocations
    *               The schema locations extended by the vendor.
    *               <br><br>
    *
    *               <strong>Implementation Issues:</strong><br>
    *               The 80th column Java Coding Standard is not followed here
    *               due to the need to represent an exact string for schema
    *               locations.<br><br>
    *
    * @return String representing all of the schema locations needed.
    */
   private String getSRTESchemaLocations( String iExtendedSchemaLocations )
   {
      mLogger.entering("---LMSManifestHandler", "getSRTESchemaLocations()");
      String result = new String();
      //String xsdLocation = EnvironmentVariable.getValue("ADL_SRTE_HOME");
      
      String xsdLocation = mXSDLocation;
      xsdLocation = "file:///" + xsdLocation + File.separator + "xml" + File.separator + "xsd" + File.separator;
      
      if ( _Debug )
      {
          System.out.println("####### XSD LOCATION : " + xsdLocation );
      }
      
      xsdLocation = xsdLocation.replaceAll( " ", "%20");

      xsdLocation = xsdLocation.replace( '\\', '/');

      //String xmlLocation = "http://www.w3.org/2001/03/xml.xsd ";

      mLogger.info("+++++++++++xsdLocation IS: " + xsdLocation + "+++++++++++++++++++");

      result = "http://www.imsglobal.org/xsd/imscp_v1p1 " +
                xsdLocation +"imscp_v1p1.xsd " +
               "http://www.w3.org/XML/1998/namespace " +
                "xml.xsd " +
               "http://www.adlnet.org/xsd/adlcp_v1p3 " +
                xsdLocation + "adlcp_v1p3.xsd " +
               "http://www.adlnet.org/xsd/adlseq_v1p3 " +
                xsdLocation + "adlseq_v1p3.xsd " +
               "http://www.adlnet.org/xsd/adlnav_v1p3 " +
                xsdLocation + "adlnav_v1p3.xsd " +
               "http://www.imsglobal.org/xsd/imsss " +
                xsdLocation + "imsss_v1p0.xsd " +
               "http://ltsc.ieee.org/xsd/LOM " +
               xsdLocation + "lomCustom.xsd " +
               "http://ltsc.ieee.org/xsd/LOM/vocab " +
               xsdLocation + "vocab/custom.xsd " +
               "http://ltsc.ieee.org/xsd/LOM/unique " +
               xsdLocation + "unique/strict.xsd " +
               "http://ltsc.ieee.org/xsd/LOM/extend " +
               xsdLocation + "extend/custom.xsd " +
               "http://ltsc.ieee.org/xsd/LOM/custom " +
               xsdLocation + "vocab/adlmd_vocabv1p0.xsd";


      mLogger.info("+++++++++++RESULT IS: " + result + "+++++++++++++++++++");
      if ( ! iExtendedSchemaLocations.equals("") )
      {
         result = result + " " + iExtendedSchemaLocations;
      }

      return result;
   }
   
   /**
    * imsmanifest file Parsing 및 DOM tree를 생성하기 위해
    * ADLValidator의 CPValidator, ADLValidatorOutcome 클래스를 사용한다.
    * 
    * Uses the CPValidator and ADLValidatorOutcome classes of the 
    * <code>ADLValidator</code> to parse a manifest file and to create the 
    * corresponding DOM tree.  This tree is then traversed (with the use of 
    * additional <code>LMSManifestHandler</code> methods, appropriate 
    * database inserts are performed, a template activity tree is created
    * using the ADLSeqUtilities class, and serialized files are created for 
    * each organization element in the manfest.  
    * <br><br>
    * 
    * @param iFilePath - 유효성 검사를 수행할 파일의 경로
    *        iTestType - pif or media
    *        iValidate - 유효성 검사를 수행할지 여부를 나타내는 boolean 값
    * 
    * @return An ADLValidator DOM 객체 및 유효성 검사결과를 담고 있는 ADLValidator 객체
    */
   public ADLValidatorOutcome processPackage( String iFilePath, String iTestType, boolean iValidate ) throws Exception
   {
       boolean validateResult;
       String iExtendedSchemaLocations = "";
       
       mLogger.entering("---LMSManifestHandler", "processPackage()");
       
       String SRTE_EnvironmentVariable = mXSDLocation;

       CPValidator scormvalidator = new CPValidator( SRTE_EnvironmentVariable );
       scormvalidator.setSchemaLocation( getSRTESchemaLocations( iExtendedSchemaLocations ) );
       scormvalidator.setPerformValidationToSchema( iValidate );

       validateResult = scormvalidator.validate( iFilePath, iTestType, "contentaggregation", false );

       // retrieve object that stores the results of the validation activites
       ADLValidatorOutcome outcome = scormvalidator.getADLValidatorOutcome();

       try
       {
          mLogger.info( "Document parsing complete." ); 

          if ( (!iValidate 
                  && outcome.getDoesIMSManifestExist()  && outcome.getIsWellformed() && outcome.getIsValidRoot()) ||
               (iValidate
                  && (outcome.getDoesIMSManifestExist() && outcome.getIsWellformed() && outcome.getIsValidRoot() && 
                  outcome.getIsValidToSchema() && outcome.getIsValidToApplicationProfile() && outcome.getDoRequiredCPFilesExist())) )
          {
             // mDocument = outcome.getRolledUpDocument();
             outcome.rollupSubManifests( false );

             mDocument = outcome.getDocument();
             mManifest = mDocument.getDocumentElement();
             mOrganizationList = this.getOrganizationList();

             scormvalidator.parseData( mDocument, false, false );
             mLaunchDataList = scormvalidator.getLaunchDataList();
             mSequencingDataList = scormvalidator.getSequencingDataList();
             mMetaDataList = scormvalidator.getMetadataDataList();  // MetadataData 정보를 가져온다

             //outcome.rollupSubManifests( this.mManifest, false );
             
             // database에 insert
             updateDB();
          }
          else
          {
             // 유효성 검사 실패시 로그에 남김
             if (!(outcome.getIsWellformed())) 
             {
                mLogger.info("NOT WELL FORMED!!!");
             }
             if (!(outcome.getIsValidRoot()))
             {
                mLogger.info("INVALID ROOT!!!");
             }
             if (!(outcome.getIsValidToSchema()))
             {
                mLogger.info("NOT VALID TO SCHEMA!!!");
             }
             if (!(outcome.getIsValidToApplicationProfile())) 
             {
                mLogger.info("NOT VALID TO APP PROFILE!!!");
             }
             if (!(outcome.getDoRequiredCPFilesExist())) 
             {
                mLogger.info("REQUIRED FILES DO NOT EXIST!!!");
             }

             mLogger.info("-----NOT CONFORMANT!!!----");
          }
       }
       catch(Exception e)
       {
          e.printStackTrace();
          throw new Exception( e.getMessage());
       }

       scormvalidator.cleanImportDirectory( mXSDLocation + File.separator + "PackageImport" );

       mLogger.exiting( "---LMSManifestHandler", "processPackage()" ); 
       //  Return boolean signifying whether or not the parsing was successful

       return outcome;   
   }

/*
    * LaunchData 및 SequencingData 를 DB에 Insert 후,
    * Course Contents를 임시 디렉토리에서 대상 디렉토리로 복사한다.
    * 
    * @throws Exception
    */
   private void updateDB() throws Exception
   {
       DBConnectionManager connMgr = null;
       
       try
       {
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);
           
           setPath();                               // 웹경로, 실제경로 Set
           selectCourseCode( connMgr );             // Course Code Set
    
           updateLaunchDataDB(connMgr);             // LaunchData DB Insert
           updateSequencingDataDB(connMgr);         // SequencingData DB Insert
           updateMetadataDB(connMgr);               // Metadata DB Insert
           
           //Copy course files from the temp directory and serialize
           String copyInDirName = mXSDLocation + File.separator + "PackageImport";
           String copyOutDirName = mRealPath  + File.separator + mCourseCode;
           
           copyCourse( copyInDirName, copyOutDirName );

           // # DB -> Object 로 변환시 삭제할 부분.
           // # Sequencing 정보를 Object로  물리적 파일에 저장.
           insertTreeObject(connMgr);
           
           // Insert 및 Copy 작업 완료시 DB Commit
           connMgr.commit();
       }
       catch( IOException ioe )
       {
           ioe.printStackTrace();
           
           connMgr.rollback();
           ErrorManager.getErrorStackTrace(ioe);
           throw new IOException( ioe.getMessage());
       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           connMgr.rollback();
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(connMgr != null) { try { connMgr.setAutoCommit(true); } catch (Exception e2) {} }            
           if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
       }        
   }

   private void insertTreeObject( DBConnectionManager connMgr ) throws Exception
   {
       ADLSeqUtilities mySeqUtilities;
       SeqActivityTree mySeqActivityTree;
       
       for ( int j=0; j < mOrganizationList.size(); j++ ) 
       {
           Node tempOrganization = (Node)mOrganizationList.elementAt(j);
           String tempOrgaID = DOMTreeUtility.getAttributeValue(tempOrganization, "identifier");
           
           //create a SeqActivityTree and serialize it
           mySeqUtilities= new ADLSeqUtilities();
           mySeqActivityTree = new SeqActivityTree();
    
           String tempObjectivesGlobalToSystem = DOMTreeUtility.getAttributeValue( tempOrganization,"objectivesGlobalToSystem" );
    
           // include sequencing collection as a parameter as well as 
           // the organization node.
           mySeqActivityTree = mySeqUtilities.buildActivityTree(tempOrganization, getSeqCollection());
    
           if( tempObjectivesGlobalToSystem.equals("false") )
           {
              mySeqActivityTree.setScopeID(mCourseCode);
           }
    
           mySeqActivityTree.setCourseID(mCourseCode);
           
           mySeqActivityTree.dumpState();
    
           ScormBlobBean sbb = new ScormBlobBean();
           boolean isOk = sbb.insertTreeObjectInOrganizationTable( connMgr, mCourseCode, tempOrgaID, mySeqActivityTree );
           
           if ( !isOk )
           {
               throw new Exception("InsertTreeOjbectInOrganizationTable Exception");
           }
       }
       
   }

   /*
    * 원본 디렉토리에서 대상 디렉토리로 Course를 복사한다.
    * 
    * @param iInFilePath - 복사하고자하는 원본 파일이나 디렉토리 경로
    *        iOutFilePath - 파일이 복사 되어질 대상 디렉토리 경로
    * 
    */
   private void copyCourse( String iInFilePath, String iOutFilePath ) throws IOException
   {
       String inDirName = iInFilePath;
       inDirName.replace('/',java.io.File.separatorChar);
       
       File tempFile = new File(inDirName);
       File[] fileNames = tempFile.listFiles();
       
       String outDirName = iOutFilePath;
       
       outDirName = outDirName.replace('/',java.io.File.separatorChar);
       File tempDir = new File(outDirName);
       tempDir.mkdirs();
       
       for ( int i=0; i < fileNames.length; i++ )
       {
           String tempString = outDirName + java.io.File.separatorChar + 
           fileNames[i].getName();
           
           if ( fileNames[i].isDirectory() )
           {
               File dirToCreate = new File(tempString);
               dirToCreate.mkdirs();
               copyCourse( fileNames[i].getAbsolutePath(), tempString );
           }
           else
           {
               BufferedInputStream in = new BufferedInputStream
               ( new FileInputStream(fileNames[i]) );
               BufferedOutputStream out = new BufferedOutputStream
               ( new FileOutputStream(tempString) );
               int c;
               while ((c = in.read()) != -1) 
               {
                   out.write(c);
               }
               
               in.close();
               out.close();
           }
       }
   }

   /*
    * organization Node 리스트를 가져온다.
    * 
    * @return Vector organization Node List
    */
   private Vector getOrganizationList()
   {
      return ManifestHandler.getOrganizationNodes(mManifest, false);
   }

   /**
    * CourseName 값을 Get한다.
    * 
    * @param courseName
    */
   public String getCourseName( String courseName )
   {
       return mCourseName;
   }
   
   /**
    * CourseName 값을 Set한다.
    * 
    * @param courseName
    */
   public void setCourseName( String courseName )
   {
      this.mCourseName = courseName;
   }
   
   /**
    * CourseCode값을 Get한다.
    * 
    * @return String    CourseCode 값
    */
   public String getCourseCode()
   {
      return this.mCourseCode;
   }
   
   /**
    * form값의 정보를 갖고 있는 formMap을 Set한다.
    * 
    * @param formMap
    */
   public void setFormMap( Map formMap )
   {
       this.mFormMap = formMap;
   }
   
   /*
    * LaunchData 정보들을 DB에 Insert한다. 
    * 
    * @param connMgr
    * @throws Exception
    */
   private void updateLaunchDataDB( DBConnectionManager connMgr ) throws Exception
   {
       PreparedStatement pstmtInsertCourse = null;
       PreparedStatement pstmtInsertOrganization = null;
       PreparedStatement pstmtInsertItem = null;
       PreparedStatement pstmtInsertResource = null;
       PreparedStatement pstmtInsertResourceFile = null;
       PreparedStatement pstmtInsertResourceDep = null;
       
       String sqlInsertCourse = 
           "INSERT INTO TYS_COURSE ( "+
           "   COURSE_CODE, COURSE_NM, COURSE_TYPE, "+
           "   UPLOAD_PATH, MANIFEST_ID, IS_SEQUENCING, "+
           "   ORGS_DEFAULT, DIST_TYPE, DIST_CODE, "+
           "   LUSERID, LDATE, ISBETA, PRODUCER )  "+
           "VALUES ( ?, ?, ?, ?, ?, "+
           "     ?, ?, ?, ?, ?, "+
           "     TO_CHAR( sysdate, 'YYYYMMDDHH24MISS'), ?, ? ) ";
       
       String sqlInsertOrganization =
           "INSERT INTO TYS_ORGANIZATION ( "+
           "   COURSE_CODE, ORG_ID, ORG_TITLE, "+
           "   ORG_STRUCTURE, OBJ_GLOBAL_TO_SYS, META_LOCATION, "+
           "   TREE_ORD, LUSERID, LDATE) "+
           "VALUES ( ?, ?, ?, ?, ?, "+
           "    ?, ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ) ";

       String sqlInsertItem =
           "INSERT INTO TYS_ITEM ( "+
           "   COURSE_CODE, ORG_ID, ITEM_ID, "+
           "   ITEM_PID, ITEM_ID_REF, ITEM_IS_VISIBLE, "+
           "   ITEM_PARAMETERS, ITEM_TITLE, ITEM_TYPE, "+
           "   ITEM_TIME_LIMIT, ITEM_MAX_TIME_ALLOW, ITEM_DATA_FROM_LMS, "+
           "   ITEM_THRESHOLD, META_LOCATION, TREE_ORD, "+
           "   OBJ_ID, PREVIOUS, NEXT, " +
           "   EXIT, ABANDON, LUSERID, " +
           "   LDATE) "+
           "VALUES ( ?, ?, ?, ?, ?, "+
           "    ?, ?, ?, ?, ?, "+
           "    ?, ?, ?, ?, ?, "+
           "    ?, ?, ?, ?, ?," +
           "    ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ) ";
       
       String sqlInsertResource =
           "INSERT INTO TYS_RESOURCE ( "+
           "   COURSE_CODE, ORG_ID, ITEM_ID, "+
           "   RES_ID, RES_TYPE, RES_HREF, RES_BASE_DIR, "+
           "   RES_SCORM_TYPE, RES_PERSIST_STATE, "+
           "   META_LOCATION, LUSERID, LDATE) "+
           "VALUES ( ?, ?, ?, ?, ?, "+
           "    ?, ?, ?, ?, ?, "+
           "    ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ) ";
       
       String sqlInsertResourceFile =
           "INSERT INTO TYS_RESOURCE_FILE ( "+
           "   COURSE_CODE, ORG_ID, ITEM_ID, "+
           "   RES_ID, RES_FILE_SEQ, RES_FILE_HREF, "+
           "   META_LOCATION, LUSERID, LDATE) "+
           "VALUES ( ?, ?, ?, ?, ?, "+
           "    ?, ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ) ";
       
       String sqlInsertResourceDep =
           "INSERT INTO TYS_RESOURCE_DEPENDENCY ( "+
           "   COURSE_CODE, ORG_ID, ITEM_ID, "+
           "   RES_ID, RES_DEP_SEQ, RES_ID_REF," +
           " LUSERID, LDATE) "+
           "VALUES ( ?, ?, ?, ?, ?, "+
           "    ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ) ";

       try
       {
           pstmtInsertCourse = connMgr.prepareStatement( sqlInsertCourse );
           pstmtInsertOrganization = connMgr.prepareStatement( sqlInsertOrganization );
           pstmtInsertItem = connMgr.prepareStatement( sqlInsertItem );
           pstmtInsertResource = connMgr.prepareStatement( sqlInsertResource );
           pstmtInsertResourceFile = connMgr.prepareStatement( sqlInsertResourceFile );
           pstmtInsertResourceDep = connMgr.prepareStatement( sqlInsertResourceDep );

           String courseNm = (String) mFormMap.get("p_course_nm");
           String courseType = (String) mFormMap.get("p_course_type");
           String isSequencing = (String) mFormMap.get("p_is_sequencing");
           String uploadPath = (String) mFormMap.get("p_upload_path");
           String distType = (String) mFormMap.get("p_dist_type");
           String distCode = (String) mFormMap.get("p_upperclass") + (String) mFormMap.get("p_middleclass") + (String) mFormMap.get("p_lowerclass");
           String isBeta = (String) mFormMap.get("p_isbeta");
           String producer = (String) mFormMap.get("p_producer");
           
           
           String manifestId = DOMTreeUtility.getAttributeValue( mManifest, "identifier" );
           
           Node organizationsNode = DOMTreeUtility.getNode( mManifest, "organizations" );
           String orgsDefault = DOMTreeUtility.getAttributeValue( organizationsNode, "default" );
           
           synchronized( pstmtInsertCourse )
           {
               pstmtInsertCourse.setString( 1, mCourseCode );
               pstmtInsertCourse.setString( 2, courseNm );
               pstmtInsertCourse.setString( 3, courseType );
               pstmtInsertCourse.setString( 4, uploadPath );
               pstmtInsertCourse.setString( 5, manifestId );
               pstmtInsertCourse.setString( 6, isSequencing );
               pstmtInsertCourse.setString( 7, orgsDefault );
               pstmtInsertCourse.setString( 8, distType );
               pstmtInsertCourse.setString( 9, distCode );
               pstmtInsertCourse.setString( 10, (String) mFormMap.get("userid") );
               pstmtInsertCourse.setString( 11, isBeta );
               pstmtInsertCourse.setString( 12, producer );

               pstmtInsertCourse.executeUpdate();
           }
           
           if ( _Debug )
           {
               System.out.println( "TYS_COURSE==============================" );
               System.out.println( "COURSE_CODE      : " + mCourseCode );
               System.out.println( "COURSE_NM        : " + courseNm );
               System.out.println( "COURSE_TYPE      : " + courseType );
               System.out.println( "UPLOAD_PATH      : " + uploadPath );
               System.out.println( "MANIFEST_ID      : " + manifestId );
               System.out.println( "IS_SEQUENCING    : " + isSequencing );
               System.out.println( "ORGS_DEFAULT     : " + orgsDefault );
               System.out.println( "DIST_TYPE        : " + distType );
               System.out.println( "DIST_CODE        : " + distCode );
               System.out.println( "ISBETA           : " + isBeta );
               System.out.println( "PRODUCER         : " + producer );
               System.out.println( "=======================================" );
           }
           
           for ( int j=0; j < mOrganizationList.size(); j++ ) 
           {
               Node tempOrganization = (Node) mOrganizationList.elementAt(j);

               String tempOrgId = DOMTreeUtility.getAttributeValue(tempOrganization, "identifier" );
               Node tempOrgTitleNode = DOMTreeUtility.getNode( tempOrganization, "title" );
               String tempOrgTitle = DOMTreeUtility.getNodeValue( tempOrgTitleNode );

               String tempOrgStructure = DOMTreeUtility.getAttributeValue(tempOrganization, "structure" );
               String tempObjGlobalSys = DOMTreeUtility.getAttributeValue(tempOrganization, "objectivesGlobalToSystem" );

               if ( tempObjGlobalSys.equals("false"))
               {
                   tempObjGlobalSys = "N";
               }
               else
               {
                   tempObjGlobalSys = "Y";
               }

               int tempTreeOrd = j+1;
              
               String tempMetaLocation = getMetaLocation( tempOrganization ); 

               synchronized( pstmtInsertOrganization )
               {
                   pstmtInsertOrganization.setString( 1, mCourseCode );
                   pstmtInsertOrganization.setString( 2, tempOrgId );
                   pstmtInsertOrganization.setString( 3, tempOrgTitle );
                   pstmtInsertOrganization.setString( 4, tempOrgStructure );
                   pstmtInsertOrganization.setString( 5, tempObjGlobalSys );
                   pstmtInsertOrganization.setString( 6, tempMetaLocation );
                   pstmtInsertOrganization.setInt( 7, tempTreeOrd );
                   pstmtInsertOrganization.setString( 8, (String) mFormMap.get("userid") );
                   
                   pstmtInsertOrganization.executeUpdate();
               }               

               if ( _Debug )
               {
                   System.out.println( "TYS_ORGANIZATION=========================" );
                   System.out.println( "COURSE_CODE      : " + mCourseCode );
                   System.out.println( "ORG_ID           : " + tempOrgId );
                   System.out.println( "ORG_TITLE        : " + tempOrgTitle );
                   System.out.println( "ORG_STRUCTURE    : " + tempOrgStructure );
                   System.out.println( "OBJ_GLOBAL_TO_SYS: " + tempObjGlobalSys );
                   System.out.println( "META_LOCATION    : " + tempMetaLocation );
                   System.out.println( "TREE_ORD         : " + tempTreeOrd );
                   System.out.println( "========================================" );
               }
               
               LaunchData ld = new LaunchData();
               int maxObjID = selectMaxObjID( connMgr );
               
               for ( int i = 0; i < mLaunchDataList.size(); i++ )
               {  
                   ld = (LaunchData)mLaunchDataList.elementAt(i);
                   
                   // If the organization identifier of the current launch data
                   // equals the identifier of the current entry of the
                   // organization list, perform the database updates.
                   if (ld.getOrganizationIdentifier().equals(tempOrgId))
                   {   
                       String objID = "S" + StringUtil.fillPadding(String.valueOf(++maxObjID), '0', 9, StringUtil.LEFT_PADDING);
                       ld.setObjID(objID);
                       
                       // Decode the URL before inserting into the database
                       URLDecoder urlDecoder = new URLDecoder();
                       String alteredLocation = new String();
                       
                       //If its external, don't concatenate to the local Web root.
                       if ((ld.getLocation().startsWith("http://")) || (ld.getLocation().startsWith("https://")))
                       {
                           alteredLocation = urlDecoder.decode((String)ld.getLocation(), "UTF-8" ); 
                       }
                       else
                       {
                           alteredLocation = "/" + mCourseCode +"/" + urlDecoder.decode( (String) ld.getLocation(), "UTF-8" );
                       }                       
                       
                       synchronized( pstmtInsertItem )
                       {
                           pstmtInsertItem.setString( 1, mCourseCode );
                           pstmtInsertItem.setString( 2, ld.getOrganizationIdentifier() );
                           pstmtInsertItem.setString( 3, ld.getItemIdentifier() );
                           pstmtInsertItem.setString( 4, ld.getItemPIdentifier() );
                           pstmtInsertItem.setString( 5, ld.getResourceIdentifier() );
                           pstmtInsertItem.setString( 6, convertStr(ld.getIsVisible()) );
                           pstmtInsertItem.setString( 7, ld.getParameters() );
                           pstmtInsertItem.setString( 8, ld.getItemTitle() );
                           pstmtInsertItem.setString( 9, "" );
                           pstmtInsertItem.setString( 10, ld.getTimeLimitAction() );
                           pstmtInsertItem.setString( 11, ld.getAttemptAbsoluteDurationLimit() );
                           pstmtInsertItem.setString( 12, ld.getDataFromLMS() );
                           pstmtInsertItem.setString( 13, ld.getCompletionThreshold() );
                           pstmtInsertItem.setString( 14, ld.getItemMetaLocation() );
                           pstmtInsertItem.setInt( 15, ld.getTreeOrder() );
                           pstmtInsertItem.setString( 16, ld.getObjID() );
                           pstmtInsertItem.setString( 17, convertStr(ld.getPrevious()) );
                           pstmtInsertItem.setString( 18, convertStr(ld.getContinue()) );
                           pstmtInsertItem.setString( 19, convertStr(ld.getExit()) );
                           pstmtInsertItem.setString( 20, convertStr(ld.getAbandon()) );
                           pstmtInsertItem.setString( 21, (String) mFormMap.get("userid") );

                           pstmtInsertItem.executeUpdate();
                       }
                       
                       if ( _Debug )
                       {
                           System.out.println( "TYS_ITEM================================" );
                           System.out.println( "COURSE_CODE      : " + mCourseCode );
                           System.out.println( "ORG_ID           : " + tempOrgId );
                           System.out.println( "ITEM_ID          : " + ld.getItemIdentifier() );
                           System.out.println( "ITEM_PID         : " + ld.getItemPIdentifier() );
                           System.out.println( "ITEM_ID_REF      : " + ld.getResourceIdentifier() );
                           System.out.println( "ITEM_IS_VISIBLE  : " + ld.getIsVisible() );
                           System.out.println( "ITEM_PARAMETERS  : " + ld.getParameters() );
                           System.out.println( "ITEM_TITLE       : " + ld.getItemTitle() );
                           System.out.println( "ITEM_TYPE        : " + ld.getSCORMType() );
                           System.out.println( "ITEM_TIME_LIMIT  : " + ld.getTimeLimitAction() );
                           System.out.println( "*ITEM_MAX_TIME_ALLOW: " + ld.getAttemptAbsoluteDurationLimit() );
                           System.out.println( "ITEM_DATA_FROM_LMS  : " + ld.getDataFromLMS() );
                           System.out.println( "ITEM_THRESHOLD   : " + ld.getCompletionThreshold() );
                           System.out.println( "META_LOCATION   : " + ld.getItemMetaLocation() );
                           System.out.println( "TREE_ORD         : " + ld.getTreeOrder() );
                           System.out.println( "OBJ_ID           : " + ld.getObjID() );
                           System.out.println( "========================================" );
                       }
                       
                       if ( !ld.getResourceIdentifier().equals("") )
                       {
                           synchronized( pstmtInsertResource )
                           {
                               pstmtInsertResource.setString( 1, mCourseCode);
                               pstmtInsertResource.setString( 2, ld.getOrganizationIdentifier());
                               pstmtInsertResource.setString( 3, ld.getItemIdentifier() );
                               pstmtInsertResource.setString( 4, ld.getResourceIdentifier() );
                               pstmtInsertResource.setString( 5, ld.getResType() );
                               pstmtInsertResource.setString( 6, alteredLocation );
                               pstmtInsertResource.setString( 7, ld.getResourceXMLBase() );
                               pstmtInsertResource.setString( 8, ld.getSCORMType() );
                               pstmtInsertResource.setString( 9, ld.getPersistState() );
                               pstmtInsertResource.setString( 10, ld.getResourceMetaLocation() );
                               pstmtInsertResource.setString( 11, (String) mFormMap.get("userid") );
                               
                               pstmtInsertResource.executeUpdate();
                           }

                           if ( _Debug )
                           {
                               System.out.println( "TYS_RESOURCE============================" );
                               System.out.println( "COURSE_CODE      : " + mCourseCode );
                               System.out.println( "ORG_ID           : " + tempOrgId );
                               System.out.println( "ITEM_ID          : " + ld.getItemIdentifier() );
                               System.out.println( "RES_ID           : " + ld.getResourceIdentifier());;
                               System.out.println( "RES_TYPE         : " + ld.getResType());;
                               System.out.println( "RES_HREF         : " + ld.getLocation());;
                               System.out.println( "*RES_BASE_DIR     : " + "");
                               System.out.println( "RES_SCORM_TYPE   : " + ld.getSCORMType());
                               System.out.println( "PERSISTSTATE     : " + ld.getPersistState());;
                               System.out.println( "META_LOCATION    : " + ld.getResourceMetaLocation() );
                               System.out.println( "========================================" );
                           }
                           
                           ArrayList resFileUrl = ld.getResFileUrl();
//                           ArrayList tempFileUrl = null;
                           ArrayList refResId = ld.getRefResId();
    
                           synchronized( pstmtInsertResourceFile )
                           {
                               String resFileHref = "";
                               String resFileMetaLocation = "";
                               
                               for ( int k=0; k<resFileUrl.size(); k++ )
                               {
                                   resFileHref = "";
                                   resFileMetaLocation = "";
                                   
                                   List tempFileUrl = (ArrayList) resFileUrl.get(k);

                                   if ( _Debug )
                                   {
                                       System.out.println( "TYS_RESOURCE_FILE=======================" );
                                       System.out.println( "COURSE_CODE      : " + mCourseCode );
                                       System.out.println( "ORG_ID           : " + tempOrgId );
                                       System.out.println( "ITEM_ID          : " + ld.getItemIdentifier() );
                                       System.out.println( "RES_ID           : " + ld.getResourceIdentifier());;
                                   }
                                   
                                   if ( tempFileUrl.size() == 1 )
                                   {
                                       resFileHref = (String) tempFileUrl.get(0);
                                       
                                       if ( _Debug )
                                       {
                                           System.out.println( "RES_FILE_HREF 1      : " + tempFileUrl.get(0) );
                                       }
                                   }
                                   else if ( tempFileUrl.size() == 2 )
                                   {
                                       resFileHref = (String) tempFileUrl.get(0);
                                       resFileMetaLocation = (String) tempFileUrl.get(1);
                                       
                                       if ( _Debug )
                                       {
                                           System.out.println( "RES_FILE_HREF 2 : " + tempFileUrl.get(0) );
                                           System.out.println( "*META_LOCATION 2  : " + tempFileUrl.get(1) );
                                       }
                                   }
                                   
                                   if ( _Debug )
                                   {
                                       System.out.println( "========================================" );
                                   }

                                   pstmtInsertResourceFile.setString( 1, mCourseCode );
                                   pstmtInsertResourceFile.setString( 2, ld.getOrganizationIdentifier() );
                                   pstmtInsertResourceFile.setString( 3, ld.getItemIdentifier() );
                                   pstmtInsertResourceFile.setString( 4, ld.getResourceIdentifier() );
                                   pstmtInsertResourceFile.setInt( 5, k+1 );
                                   pstmtInsertResourceFile.setString( 6, resFileHref );
                                   pstmtInsertResourceFile.setString( 7, resFileMetaLocation );
                                   pstmtInsertResourceFile.setString( 8, (String) mFormMap.get("userid") );                                   
                                   
                                   pstmtInsertResourceFile.executeUpdate();
                               }
                           }
    

                           synchronized( pstmtInsertResourceDep )
                           {
                               for ( int k=0; k<refResId.size(); k++ )
                               {
                                   pstmtInsertResourceDep.setString( 1, mCourseCode );
                                   pstmtInsertResourceDep.setString( 2, ld.getOrganizationIdentifier() );
                                   pstmtInsertResourceDep.setString( 3, ld.getItemIdentifier() );
                                   pstmtInsertResourceDep.setString( 4, ld.getResourceIdentifier() );
                                   pstmtInsertResourceDep.setInt( 5, k+1 );
                                   pstmtInsertResourceDep.setString( 6, (String) refResId.get(k) );
                                   pstmtInsertResourceDep.setString( 7, (String) mFormMap.get("userid") );                             
                                   
                                   pstmtInsertResourceDep.executeUpdate();

                                   if ( _Debug )
                                   {
                                       System.out.println( "TYS_RESOURCE_DEPENDENCY================" );
                                       System.out.println( "COURSE_CODE      : " + mCourseCode );
                                       System.out.println( "ORG_ID           : " + tempOrgId );
                                       System.out.println( "ITEM_ID          : " + ld.getItemIdentifier() );
                                       System.out.println( "RES_ID           : " + ld.getResourceIdentifier());;
                                       System.out.println( "REF_ID_REF_      : " + refResId.get(k) );
                                       System.out.println( "========================================" );
                                   }
                               }
                           }
                       }

                   }
               }
           }
       }
       catch( IOException ioe )
       {
           ioe.printStackTrace();
           
           ErrorManager.getErrorStackTrace(ioe);
           throw new IOException( ioe.getMessage());
       }
       catch ( Exception e )
       {
           e.printStackTrace();

           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(pstmtInsertCourse != null) { try { pstmtInsertCourse.close(); } catch (Exception e1) {} }
           if(pstmtInsertOrganization != null) { try { pstmtInsertOrganization.close(); } catch (Exception e1) {} }
           if(pstmtInsertItem != null) { try { pstmtInsertItem.close(); } catch (Exception e1) {} }
           if(pstmtInsertResource != null) { try { pstmtInsertResource.close(); } catch (Exception e1) {} }
           if(pstmtInsertResourceFile != null) { try { pstmtInsertResourceFile.close(); } catch (Exception e1) {} }
           if(pstmtInsertResourceDep != null) { try { pstmtInsertResourceDep.close(); } catch (Exception e1) {} }
       }  
   }

   /*
    * SequencingData 정보들을 DB에 Insert한다.
    * 
    * @param connMgr
    * @throws Exception
    */
   private void updateSequencingDataDB( DBConnectionManager connMgr ) throws Exception
   {
       PreparedStatement pstmtInsertSequence = null;
       PreparedStatement pstmtInsertRollupRule = null;
       PreparedStatement pstmtInsertRollupRuleCondition = null;
       PreparedStatement pstmtInsertSeqRule = null;
       PreparedStatement pstmtInsertSeqRuleCondition = null;
       PreparedStatement pstmtInsertObjectives = null;
       PreparedStatement pstmtInsertObjectivesMapInfo = null;
       
       String sqlInsertSequence =
           "\n  INSERT INTO TYS_SEQUENCE                                                        "+
           "\n     ( SEQ_IDX, COURSE_CODE, ORG_ID,                                              "+
           "\n     ITEM_ID, SEQ_TYPE, SEQ_IDREF,                                                "+
           "\n     CHOICE, CHOICE_EXIT, FLOW,                                                   "+
           "\n     FORWARD_ONLY, USE_ATTEMPT_OBJ_INFO, USE_ATTEMPT_PROGRESS_INFO,               "+
           "\n     ATTEMPT_LIMIT, ATTEMPT_DURATION_LIMIT, RANDOM_TIMING,                        "+
           "\n     SELECTION_COUNT, REORDER_CHILDREN, SELECTION_TIMING,                         "+
           "\n     TRACKED, COMPLET_SETBY_CONTENT, OBJ_SETBY_CONTENT,                           "+
           "\n     PREVENT_ACTIVATION, CONSTRAIN_CHOICE, REQUIRED_FOR_SATISFIED,                "+
           "\n     REQUIRED_FOR_NOT_SATISFIED, REQUIRED_FOR_COMPLETED, REQUIRED_FOR_INCOMPLETE, "+
           "\n     MEASURE_SATISFACTION, ROLLUP_OBJ_SATISFIED, ROLLUP_PROGRESS_COMPLETE,        "+
           "\n     OBJ_MEASURE_WEIGHT, LUSERID, LDATE )                                         "+
           "\n  VALUES                                                                          "+
           "        ( ?, ?, ?,                                                                  "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, ?,                                                                    "+
           "\n      ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )                               ";
       
       String sqlInsertRollupRule =
           "\n  INSERT INTO TYS_ROLLUP_RULE (                      "+
           "\n     SEQ_IDX, ROLLUP_RULE_IDX, CHILD_ACTIVITY_SET,   "+
           "\n     MINIMUM_COUNT, MINIMUM_PERCENT, ROLLUP_ACTION,  "+
           "\n     CONDITION_COMBINATION, LUSERID, LDATE)          "+
           "\n  VALUES                                             "+
           "\n    ( ?, ?, ?,                                       "+
           "\n      ?, ?, ?,                                       "+
           "\n      ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )  ";

       String sqlInsertRollupRuleCondition =
           "\n  INSERT INTO TYS_ROLLUP_RULE_CONDITION (            "+
           "\n     SEQ_IDX, ROLLUP_RULE_IDX, ROLLUP_CONDITION_IDX, "+
           "\n     OPERATOR, CONDITION, LUSERID,                   "+
           "\n     LDATE)                                          "+
           "\n  VALUES                                             "+
           "\n    ( ?, ?, ?,                                       "+
           "\n      ?, ?, ?,                                       "+
           "\n      TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )        ";

       String sqlInsertSeqRule =
           "\n  INSERT INTO TYS_SEQ_RULE (                         "+
           "\n     SEQ_IDX, RULE_IDX, RULE_TYPE,                   "+
           "\n     CONDITION_COMBINATION, RULE_ACTION, LUSERID,    "+
           "\n     LDATE)                                          "+
           "\n  VALUES                                             "+
           "\n    ( ?, ?, ?,                                       "+
           "\n      ?, ?, ?,                                       "+
           "\n      TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )        ";

       String sqlInsertSeqRuleCondition =
           "\n  INSERT INTO TYS_SEQ_RULE_CONDITION (                   "+
           "\n     SEQ_IDX, RULE_IDX, RULE_CONDITION_IDX,              "+
           "\n     REFERENCED_OBJECTIVE, MEASURE_THRESHOLD, OPERATOR,  "+
           "\n     CONDITION, LUSERID, LDATE)                          "+
           "\n  VALUES                                                 "+
           "\n    ( ?, ?, ?,                                           "+
           "\n      ?, ?, ?,                                           "+
           "\n      ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )      ";

       String sqlInsertObjectives =
           "\n  INSERT INTO TYS_OBJECTIVES (                           "+
           "\n     SEQ_IDX, OBJ_IDX, OBJ_TYPE,                         "+
           "\n     SATISFIED_MEASURE, OBJECTIVE_ID, MIN_NORMAL_MEASURE,"+
           "\n     LUSERID, LDATE)                                     "+
           "\n  VALUES                                                 "+
           "\n    ( ?, ?, ?,                                           "+
           "\n      ?, ?, ?,                                           "+
           "\n      ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )         ";

       String sqlInsertObjectivesMapInfo =
           "\n  INSERT INTO TYS_OBJECTIVES_MAPINFO (           "+
           "\n     SEQ_IDX, OBJ_IDX, OBJ_MAPINFO_IDX,          "+
           "\n     TARGET_OBJ_ID, READ_STATUS, READ_MEASURE,   "+
           "\n     WRITE_STATUS, WRITE_MEASURE, LUSERID,       "+
           "\n     LDATE)                                      "+
           "\n  VALUES                                         "+
           "\n    ( ?, ?, ?,                                   "+
           "\n      ?, ?, ?,                                   "+
           "\n      ?, ?, ?,                                   "+
           "\n      TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )    ";

       
       try
       {
           pstmtInsertSequence = connMgr.prepareStatement( sqlInsertSequence );
           pstmtInsertRollupRule = connMgr.prepareStatement( sqlInsertRollupRule );
           pstmtInsertRollupRuleCondition = connMgr.prepareStatement( sqlInsertRollupRuleCondition );
           pstmtInsertSeqRule = connMgr.prepareStatement( sqlInsertSeqRule );
           pstmtInsertSeqRuleCondition = connMgr.prepareStatement( sqlInsertSeqRuleCondition );
           pstmtInsertObjectives = connMgr.prepareStatement( sqlInsertObjectives );
           pstmtInsertObjectivesMapInfo = connMgr.prepareStatement( sqlInsertObjectivesMapInfo );
           
           SequencingData sd = new SequencingData();
           
           int nextSeqIdx = selectMaxSeqIdx( connMgr ) + 1; 
           
           for ( int i = 0; i < mSequencingDataList.size(); i++ )
           {  
               sd = (SequencingData) mSequencingDataList.elementAt(i);
               
               synchronized( pstmtInsertSequence )
               {
                   int cnt = 1;
                   
                   pstmtInsertSequence.setInt( cnt++, nextSeqIdx+i );
                   pstmtInsertSequence.setString( cnt++, mCourseCode );
                   pstmtInsertSequence.setString( cnt++, sd.getOrganizationIdentifier() );
                   pstmtInsertSequence.setString( cnt++, sd.getItemIdentifier() );
                   pstmtInsertSequence.setString( cnt++, sd.getSeqType() );
                   pstmtInsertSequence.setString( cnt++, sd.getSeqIdRef() );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isChoice()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isChoiceExit()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isFlow()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isForwardOnly()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isUseAttemptObjInfo()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isUseAttemptProgressInfo()) );
                   pstmtInsertSequence.setInt( cnt++, sd.getAttemptLimit() );
                   pstmtInsertSequence.setDouble( cnt++, sd.getAttemptDurationLimit() );
                   pstmtInsertSequence.setString( cnt++, sd.getRandomTiming() );
                   pstmtInsertSequence.setInt( cnt++, sd.getSelectCount() );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isReorderChildren()) );
                   pstmtInsertSequence.setString( cnt++, sd.getSelectionTiming() );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isTracked()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isCompletSetbyContent()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isObjSetbyContent()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isPreventActivation()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isConstrainChoice()) );
                   pstmtInsertSequence.setString( cnt++, sd.getRequiredSatisfied() );
                   pstmtInsertSequence.setString( cnt++, sd.getRequiredNotSatisfied() );
                   pstmtInsertSequence.setString( cnt++, sd.getRequiredComplete() );
                   pstmtInsertSequence.setString( cnt++, sd.getRequiredIncomplete() );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isMeasureSatisfyIfAction()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isRollupObjSatisfied()) );
                   pstmtInsertSequence.setString( cnt++, convertStr(sd.isRollupProgressComplete()) );
                   pstmtInsertSequence.setDouble( cnt++, sd.getObjMeasureWeight() );
                   pstmtInsertSequence.setString( cnt++, (String) mFormMap.get("userid") );
                   
                   pstmtInsertSequence.executeUpdate();
               }
               
               List tempRollupRuleList = sd.getRollupRuleList();
               for( int j=0; j<tempRollupRuleList.size(); j++ )
               {
                   RollupRuleData tempRollupRuleData = (RollupRuleData) tempRollupRuleList.get(j);

                   // #rolluprule set
                   pstmtInsertRollupRule.setInt( 1,  nextSeqIdx+i );
                   pstmtInsertRollupRule.setInt( 2, j+1 );
                   pstmtInsertRollupRule.setString( 3, tempRollupRuleData.getChildActivitySet() );
                   pstmtInsertRollupRule.setInt( 4, tempRollupRuleData.getMinimumCount() );
                   pstmtInsertRollupRule.setDouble( 5, tempRollupRuleData.getMinimumPercent() );
                   pstmtInsertRollupRule.setString( 6, tempRollupRuleData.getRollupAction() );
                   pstmtInsertRollupRule.setString( 7, tempRollupRuleData.getConditionCombination() );
                   pstmtInsertRollupRule.setString( 8, (String) mFormMap.get("userid") );

                   pstmtInsertRollupRule.executeUpdate();

                   List tempRollupRuleConditionList = tempRollupRuleData.getRollupRuleConditionList();
                   for ( int k=0; k<tempRollupRuleConditionList.size(); k++ )
                   {
                       RollupRuleConditionData tempRollupRuleConditionData = (RollupRuleConditionData) tempRollupRuleConditionList.get(k);
                       
                       // #rolluprulecondition set
                       pstmtInsertRollupRuleCondition.setInt( 1,  nextSeqIdx+i );
                       pstmtInsertRollupRuleCondition.setInt( 2, j+1 );
                       pstmtInsertRollupRuleCondition.setInt( 3, k+1 );
                       pstmtInsertRollupRuleCondition.setString( 4, tempRollupRuleConditionData.getOperator() );
                       pstmtInsertRollupRuleCondition.setString( 5, tempRollupRuleConditionData.getCondition() );
                       pstmtInsertRollupRuleCondition.setString( 6, (String) mFormMap.get("userid") );
                       
                       pstmtInsertRollupRuleCondition.executeUpdate();
                   }
               }

               List tempSeqRuleList = sd.getSeqRuleList();
               for( int j=0; j<tempSeqRuleList.size(); j++ )
               {
                   SeqRuleData tempSeqRuleData = (SeqRuleData) tempSeqRuleList.get(j);
                   
                   // #seqrule set
                   pstmtInsertSeqRule.setInt( 1, nextSeqIdx+i );
                   pstmtInsertSeqRule.setInt( 2, j+1 );
                   pstmtInsertSeqRule.setString( 3, tempSeqRuleData.getRuleType() );
                   pstmtInsertSeqRule.setString( 4, tempSeqRuleData.getConditionCombination() );
                   pstmtInsertSeqRule.setString( 5, tempSeqRuleData.getRuleAction() );
                   pstmtInsertSeqRule.setString( 6, (String) mFormMap.get("userid") );
                   
                   pstmtInsertSeqRule.executeUpdate();                   
                   
                   List tempSeqRuleConditionList = tempSeqRuleData.getRuleConditionList();
                   for ( int k=0; k<tempSeqRuleConditionList.size(); k++ )
                   {
                       SeqRuleConditionData tempSeqRuleConditionData = (SeqRuleConditionData) tempSeqRuleConditionList.get(k);
                       
                       // #seqrulecondition set
                       pstmtInsertSeqRuleCondition.setInt( 1, nextSeqIdx+i );
                       pstmtInsertSeqRuleCondition.setInt( 2, j+1 );
                       pstmtInsertSeqRuleCondition.setInt( 3, k+1 );
                       pstmtInsertSeqRuleCondition.setString( 4, tempSeqRuleConditionData.getReferencedObjective() );
                       pstmtInsertSeqRuleCondition.setDouble( 5, tempSeqRuleConditionData.getMeasureThreshold() );
                       pstmtInsertSeqRuleCondition.setString( 6, tempSeqRuleConditionData.getOperator() );
                       pstmtInsertSeqRuleCondition.setString( 7, tempSeqRuleConditionData.getCondition() );
                       pstmtInsertSeqRuleCondition.setString( 8, (String) mFormMap.get("userid") );
                       
                       pstmtInsertSeqRuleCondition.executeUpdate();                       
                   }
               }
                   
               List tempObjectivesList = sd.getObjectivesList();
               for( int j=0; j<tempObjectivesList.size(); j++ )
               {
                   ObjectivesData tempObjectivesData = (ObjectivesData) tempObjectivesList.get(j);
                   
                   // #Objectives set
                   pstmtInsertObjectives.setInt( 1, nextSeqIdx+i );
                   pstmtInsertObjectives.setInt( 2, j+1 );
                   pstmtInsertObjectives.setString( 3, tempObjectivesData.getObjType() );
                   pstmtInsertObjectives.setString( 4, convertStr(tempObjectivesData.isSatisfiedByMeasure()) );
                   pstmtInsertObjectives.setString( 5, tempObjectivesData.getObjectiveID() );
                   pstmtInsertObjectives.setDouble( 6, tempObjectivesData.getMinNormalizedMeasure() );
                   pstmtInsertObjectives.setString( 7, (String) mFormMap.get("userid") );
                   
                   pstmtInsertObjectives.executeUpdate();
                   
                   List tempObjectivesMapInfoList = tempObjectivesData.getMapInfoList();
                   for ( int k=0; k<tempObjectivesMapInfoList.size(); k++ )
                   {
                       ObjectivesMapInfoData tempObjectivesMapInfoData = (ObjectivesMapInfoData) tempObjectivesMapInfoList.get(k);
                       
                       // #MapInfo set
                       pstmtInsertObjectivesMapInfo.setInt( 1, nextSeqIdx+i );
                       pstmtInsertObjectivesMapInfo.setInt( 2, j+1 );
                       pstmtInsertObjectivesMapInfo.setInt( 3, k+1 );
                       pstmtInsertObjectivesMapInfo.setString( 4, tempObjectivesMapInfoData.getTargetObjectiveID() );
                       pstmtInsertObjectivesMapInfo.setString( 5, convertStr(tempObjectivesMapInfoData.isReadSatisfiedStatus()) );
                       pstmtInsertObjectivesMapInfo.setString( 6, convertStr(tempObjectivesMapInfoData.isReadNormalizedMeasure()) );
                       pstmtInsertObjectivesMapInfo.setString( 7, convertStr(tempObjectivesMapInfoData.isWriteSatisfiedStatus()) );
                       pstmtInsertObjectivesMapInfo.setString( 8, convertStr(tempObjectivesMapInfoData.isWriteNormalizedMeasure()) );
                       pstmtInsertObjectivesMapInfo.setString( 9, (String) mFormMap.get("userid") );
                       
                       pstmtInsertObjectivesMapInfo.executeUpdate();                       
                   }
               }
           }
       }
       catch( IOException ioe )
       {
           ioe.printStackTrace();
           
           ErrorManager.getErrorStackTrace(ioe);
           throw new IOException( ioe.getMessage());
       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(pstmtInsertSequence != null) { try { pstmtInsertSequence.close(); } catch (Exception e1) {} }
           if(pstmtInsertRollupRule != null) { try { pstmtInsertRollupRule.close(); } catch (Exception e1) {} }
           if(pstmtInsertRollupRuleCondition != null) { try { pstmtInsertRollupRuleCondition.close(); } catch (Exception e1) {} }
           if(pstmtInsertSeqRule != null) { try { pstmtInsertSeqRule.close(); } catch (Exception e1) {} }
           if(pstmtInsertSeqRuleCondition != null) { try { pstmtInsertSeqRuleCondition.close(); } catch (Exception e1) {} }
           if(pstmtInsertObjectives != null) { try { pstmtInsertObjectives.close(); } catch (Exception e1) {} }
           if(pstmtInsertObjectivesMapInfo != null) { try { pstmtInsertObjectivesMapInfo.close(); } catch (Exception e1) {} }
       } 
   }

   private void updateMetadataDB( DBConnectionManager connMgr ) throws Exception
   {
       PreparedStatement pstmtInsertBasic = null;
       PreparedStatement pstmtInsertMetadata = null;
       
       String sqlInsertBasic = 
           "\n  INSERT INTO LOM_BASIC (  " +
           "\n     COURSE_CODE, METADATA_SEQ, ID,   " +
           "\n     RES_FILE_SEQ, APP_PROFILE_TYPE, LUSERID,   " +
           "\n     LDATE)   " +
           "\n  VALUES ( ?, ?, ?,  " +
           "\n      ?, ?, ?,  " +
           "\n      TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )  ";
       
       String sqlInsertMetadata =
           "\n  INSERT INTO LOM_METADATA (  " +
           "\n     COURSE_CODE, METADATA_SEQ, META_TYPE,   " +
           "\n     SEQ, PSEQ, ELEMENT_NAME,   " +
           "\n     VALUE1, VALUE2, LANG_STRING,   " +
           "\n     LUSERID, LDATE)   " +
           "\n  VALUES ( ?, ?, ?,  " +
           "\n      ?, ?, ?,  " +
           "\n      ?, ?, ?,  " +
           "\n      ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )  ";

       try
       {
           int size = mMetaDataList.size();
           
           MetadataData md = null;
           
           for ( int i=0; i<size; i++ )
           {
               md = (MetadataData) mMetaDataList.get(i);
    
               pstmtInsertBasic = connMgr.prepareStatement( sqlInsertBasic );
               pstmtInsertMetadata = connMgr.prepareStatement( sqlInsertMetadata );
               
               synchronized( pstmtInsertBasic )
               {
                   pstmtInsertBasic.setString( 1, mCourseCode );
                   pstmtInsertBasic.setInt( 2, md.getMetadataSeq() );
                   pstmtInsertBasic.setString( 3, md.getIdentifier() );
                   pstmtInsertBasic.setInt( 4, md.getResourceFileSeq() );
                   pstmtInsertBasic.setString( 5, md.getApplicationProfileType() );
                   pstmtInsertBasic.setString( 6, (String) mFormMap.get("userid") );

                   pstmtInsertBasic.executeUpdate();
               }
               
               if ( _Debug )
               {
                   System.out.println( "LOM_BASIC ==============================" );
                   System.out.println( "COURSE_CODE      : " + mCourseCode );
                   System.out.println( "METADATA_SEQ     : " + md.getMetadataSeq() );
                   System.out.println( "ID               : " + md.getIdentifier() );
                   System.out.println( "RES_FILE_SEQ     : " + md.getResourceFileSeq() );
                   System.out.println( "APP_PROFILE_TYPE : " + md.getApplicationProfileType() );
                   System.out.println( "=======================================" );
               }


               Vector lomMetadataList = md.getLOMMetadataList();
               
               LOMMetadata lomMetadata = null;
               
               for ( int j=0; j<lomMetadataList.size(); j++ )
               {
                   lomMetadata = (LOMMetadata) lomMetadataList.get(j);
                   //System.out.println( lomMetadata );

                   synchronized( pstmtInsertMetadata )
                   {
                       pstmtInsertMetadata.setString( 1, mCourseCode );
                       pstmtInsertMetadata.setInt( 2, md.getMetadataSeq() );
                       pstmtInsertMetadata.setString( 3, lomMetadata.getMetaType() );
                       pstmtInsertMetadata.setInt( 4, lomMetadata.getSeq() );
                       pstmtInsertMetadata.setInt( 5, lomMetadata.getPseq() );
                       pstmtInsertMetadata.setString( 6, lomMetadata.getElementName() );
                       pstmtInsertMetadata.setString( 7, lomMetadata.getValue1() );
                       pstmtInsertMetadata.setString( 8, lomMetadata.getValue2() );
                       pstmtInsertMetadata.setString( 9, lomMetadata.getLangString() );
                       pstmtInsertMetadata.setString( 10, (String) mFormMap.get("userid") );
                       
                       pstmtInsertMetadata.executeUpdate();
                   }               

                   if ( _Debug )
                   {
                       System.out.println( "LOM_METADATA===========================" );
                       System.out.println( "COURSE_CODE  : " + mCourseCode );
                       System.out.println( "METADATA_SEQ : " + md.getMetadataSeq() );
                       System.out.println( "META_TYPE    : " + lomMetadata.getMetaType() );
                       System.out.println( "SEQ          : " + lomMetadata.getSeq() );
                       System.out.println( "PSEQ         : " + lomMetadata.getPseq() );
                       System.out.println( "ELEMENT_NAME : " + lomMetadata.getElementName() );
                       System.out.println( "VALUE1       : " + lomMetadata.getValue1() );
                       System.out.println( "VALUE2       : " + lomMetadata.getValue2() );
                       System.out.println( "LANG_STRING  : " + lomMetadata.getLangString() );
                       System.out.println( "========================================" );
                   }
               }
           }
       }
       catch ( Exception e )
       {
           e.printStackTrace();

           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(pstmtInsertBasic != null) { try { pstmtInsertBasic.close(); } catch (Exception e1) {} }
           if(pstmtInsertMetadata != null) { try { pstmtInsertMetadata.close(); } catch (Exception e1) {} }
       }  
   }   
   
   /*
    * TYS_ITEM 테이블 OBJ_ID 필드의 최대값을 반환한다. (숫자변환함)
    * 
    * @param connMgr
    * @return
    * @throws Exception
    */
   private int selectMaxObjID(DBConnectionManager connMgr) throws Exception
   {
       int maxObjID = 0;
       
       String sql =
           "\n  SELECT                                                          " +
           "\n      NVL (MAX (TO_NUMBER (SUBSTR (obj_id, 2))), 0) AS maxObjID   " +
           "\n  FROM tys_item                                                   ";
       
       ListSet ls = connMgr.executeQuery( sql );
       
       if ( ls.next() )
       {
           maxObjID= ls.getInt( "maxObjID" );
       }
       
       return maxObjID;
   }
   
   /*
    * TYS_SEQUENCE 테이블 SEQ_IDX 필드의 최대값을 반환한다.
    * 
    * @param connMgr
    * @return
    * @throws Exception
    */
   private int selectMaxSeqIdx(DBConnectionManager connMgr) throws Exception
   {
       int maxSeqIdx = 0;
       
       String sql =
           "\n  SELECT                                     "+
           "\n        NVL( MAX(SEQ_IDX), 0 ) as maxSeqIdx  "+
           "\n  FROM                                       "+
           "\n        TYS_SEQUENCE                         ";
       
       ListSet ls = connMgr.executeQuery( sql );
       
       if ( ls.next() )
       {
           maxSeqIdx = ls.getInt( "maxSeqIdx" );
       }
       
       return maxSeqIdx;
   }

   /*
    * boolean 값에 따른 문자열을 반환한다.
    * 
    * @param b
    * @return String    b == true 이면 "Y", b == false 이면 "N" 반환
    */
   private String convertStr(boolean b)
   {
       String result = "";
       
       if ( b == true )
       {
           result = "Y";
       }
       else
       {
           result = "N";
       }
       
       return result;
   }

   /*
    * 웹경로, 실서버경로를 Set한다.
    * 
    */
   private void setPath() throws Exception
   {
       String uploadCode = (String) mFormMap.get("p_upload_path");
       
       ConfigSet conf = new ConfigSet();
       
       String realPathKey = "SCORM2004.REALPATH." + uploadCode;
       String webPathKey = "SCORM2004.WEBPATH." + uploadCode;
       
       mRealPath = conf.getProperty( realPathKey );
       mWebPath = conf.getProperty( webPathKey );
   }
   
   /*
    * Course Code를 가져온다.
    * 
    * @return String Course Code
    */
   private void selectCourseCode( DBConnectionManager connMgr ) throws Exception
   {
       String sql =
           "\n  SELECT                  "+
           "\n      'C' || LPAD(SUBSTR(NVL(MAX(COURSE_CODE),'C000000000'), 2, 10)+1 , 9, '0' ) as COURSE_CODE "+
           "\n  FROM                    "+
           "\n      TYS_COURSE          ";
       
       ListSet ls = connMgr.executeQuery( sql );
       
       if ( ls.next() )
       {
           mCourseCode = ls.getString( "course_code" );
       }
   }
   
   /*
    * node에서 <metadata>의 <location> element 값을 가져온다. 
    * 
    * @param node
    * @return <metadata>의 <locataion> element 값, node가 존재하지 않으면 "" 값을 반환한다.
    */
   private String getMetaLocation( Node node )
   {
       String metaLocation  = "";
       Node metaDataNode = DOMTreeUtility.getNode( node, "metadata" );
       
       if ( metaDataNode != null )
       {
           Node metaLocationNode = DOMTreeUtility.getNode( metaDataNode, "location" );
           
           if ( metaLocationNode != null )
           {
               metaLocation = DOMTreeUtility.getNodeValue( metaLocationNode );
           }
       }
       
       return metaLocation;
   }
   
   /**
    * This method adds parameters to a URL using the following algorithm 
    * from the SCORM CAM Version 1.3:
    * While first char of parameters is in "?&"
    *    Clear first char of parameters
    * If first char of parameters is "#"
    *    If URL contains "#" or "?"
    *        Discard parameters
    *        Done processing URL
    * If URL contains "?"
    *    Append "&" to the URL
    * Else
    *    Append "?" to the URL
    * Append parameters to URL
    *
    * 
    * @param iURL  URL of content
    * 
    * @param iParameters  Parameters to be appended
    */                                                      
   public String addParameters(String iURL, String iParameters)
   {
       if ( (iURL.length() == 0) || (iParameters.length() == 0) )
       {
          return iURL;
       }
        while ( (iParameters.charAt(0) == '?') || 
                                    (iParameters.charAt(0) == '&') )
        {
           iParameters = iParameters.substring(1);
        }
        if ( iParameters.charAt(0) == '#' )
        {
             if ( (iURL.indexOf('#') != -1) || (iURL.indexOf('?') != -1) )
             {
                return iURL;
             }
        }
        if ( iURL.indexOf('?') != -1 )
        {   
           iURL = iURL + '&';
        }
        else
        {
           iURL = iURL + '?';
        }
        iURL = iURL + iParameters;

        return iURL;
   }
   
   /*
    * DOM Tree에서 sequencingCollection 노드를 가져온다.
    *
    * @return The sequencingCollection node
    */
   public Node getSeqCollection()
   {
      return DOMTreeUtility.getNode( mManifest, "sequencingCollection" );
   }
}
