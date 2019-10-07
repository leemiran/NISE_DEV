package egovframework.adm.lcms.ims.mainfest;
 
import java.io.File;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;

import org.adl.parsers.dom.DOMTreeUtility;
import org.adl.sequencer.ADLSeqUtilities;
import org.adl.sequencer.SeqActivityTree;
import org.adl.validator.ADLValidatorOutcome;
import org.adl.validator.contentpackage.CPValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.adm.lcms.cts.controller.LcmsOrganizationController;

public class ServerManifestHandler implements Serializable {
	/** log */ 
	protected static final Log log = LogFactory.getLog( LcmsOrganizationController.class);

	static final long serialVersionUID = 1864726486242049591L;

	/**
	* This is the DOM structure that will be returned with the 
	* ADLValidatorOutcome class.  It will contain all of the 
	* information contained in the imsmanifest file and will serve as a means
	* of accessing that information.
	*/
	protected Document mDocument;
	
	/**
	* This is the title of the course.  It will be populated with the value of
	* the title attribute of an organization element.
	*/
	//protected String mCourseTitle;
	
	/**
	* This is the ID of the course.  It will be given the value of the
	* nextCourseID stored in the Application Data in the RTE database.
	*/
	protected String mCourseID;
	
	/**
	* This is the path that will be used when copying files.
	*/
	//protected String mWebPath;
	 
	/**
	* This is a list of the <organization> elements in the manifest.
	*/
	protected Vector organizationList;
	
	/**
	* This vector will consist of LaunchData objects containing information
	* from the organization and resource elements which will be stored in the
	* RTE database.
	*/
	protected Vector launchDataList;
	   
	/**
	* Logger object used for debug logging.
	*/
	private Logger mLogger = Logger.getLogger("lw.scorm.xmlrpc");
	
	/**
	* This vector will consist of resouces' Data objects containing information
	* from the resource elements which will be stored in the
	* RTE database.
	*/
	protected Vector resoucesList;

	/**
	* The manifest element of the imsmanifest.xml file.
	*/
	private Node manifest;
	
	/**
	* The location of the schema xsd files.
	*/
	private String mXSDLocation;

	/**
	* Real Root Path of Contents imported.
	*/
	private String fileRootPath;
	
	/**
	 * Default constructor method which initializes member variables
	 * 
	 * @param iXSDLocation
	 *           The location where the XSDs can be found for use 
	 *           during validation.
	 * 
	 */ 
	
	public ServerManifestHandler(String iXSDLocation) {
		
		mDocument = null;
		organizationList = new Vector();
        resoucesList = new Vector();
		manifest = null;
		mXSDLocation = iXSDLocation;
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
    private String getRTESchemaLocations(String iExtendedSchemaLocations) {
		mLogger.entering("---LMSManifestHandler", "getSRTESchemaLocations()");
		String result = new String();
		//String xsdLocation = EnvironmentVariable.getValue("ADL_SRTE_HOME");

		String xsdLocation = mXSDLocation;
		xsdLocation = "file:///" + xsdLocation + File.separator + "xml"
				+ File.separator + "xsd" + File.separator;

		xsdLocation = xsdLocation.replaceAll(" ", "%20");

		xsdLocation = xsdLocation.replace('\\', '/');

		mLogger.info("+++++++++++xsdLocation IS: " + xsdLocation
				+ "+++++++++++++++++++");

		result = "http://www.imsglobal.org/xsd/imscp_v1p1 " + xsdLocation
				+ "imscp_v1p1.xsd " + "http://www.w3.org/XML/1998/namespace "
				+ "xml.xsd " + "http://www.adlnet.org/xsd/adlcp_v1p3 "
				+ xsdLocation + "adlcp_v1p3.xsd "
				+ "http://www.adlnet.org/xsd/adlseq_v1p3 " + xsdLocation
				+ "adlseq_v1p3.xsd " + "http://www.adlnet.org/xsd/adlnav_v1p3 "
				+ xsdLocation + "adlnav_v1p3.xsd "
				+ "http://www.imsglobal.org/xsd/imsss " + xsdLocation
				+ "imsss_v1p0.xsd " + "http://ltsc.ieee.org/xsd/LOM "
				+ xsdLocation + "lomCustom.xsd "
				+ "http://ltsc.ieee.org/xsd/LOM/vocab " + xsdLocation
				+ "vocab/custom.xsd " + "http://ltsc.ieee.org/xsd/LOM/unique "
				+ xsdLocation + "unique/strict.xsd "
				+ "http://ltsc.ieee.org/xsd/LOM/extend " + xsdLocation
				+ "extend/custom.xsd " + "http://ltsc.ieee.org/xsd/LOM/custom "
				+ xsdLocation + "vocab/adlmd_vocabv1p0.xsd";

		mLogger.info("+++++++++++RESULT IS: " + result + "+++++++++++++++++++");
		if (!iExtendedSchemaLocations.equals("")) {
			result = result + " " + iExtendedSchemaLocations;
		}

		return result;
	}


	/**
	 * Uses the CPValidator and ADLValidatorOutcome classes of the
	 * <code>ADLValidator</code> to parse a manifest file and to create the
	 * corresponding DOM tree. This tree is then traversed (with the use of
	 * additional <code>LMSManifestHandler</code> methods, appropriate
	 * database inserts are performed, a template activity tree is created using
	 * the ADLSeqUtilities class, and serialized files are created for each
	 * organization element in the manfest. <br>
	 * <br>
	 * 
	 * @param iFilePath -
	 *            A string representing the path of the file to be validated.
	 *            iValidate - A boolean value representing whether or not
	 *            validation should be performed.
	 * 
	 * @return An ADLValidator object containing the DOM object as well as
	 *         validation results.
	 */
    public ADLValidatorOutcome processPackage(String iFilePath, boolean iValidate) {
   		
		String iExtendedSchemaLocations = "";
		//RTEDataHandler fileHandler = new RTEDataHandler();
		fileRootPath = iFilePath;
		String manifestFilePath = fileRootPath+ java.io.File.separator + "imsmanifest.xml";

		mLogger.entering("---ManifestHandler", "processManifest()");

		String RTE_EnvironmentVariable = mXSDLocation;
		CPValidator scormvalidator = new CPValidator(RTE_EnvironmentVariable);
		scormvalidator.setSchemaLocation(getRTESchemaLocations(iExtendedSchemaLocations));
		//Turn validation on or off
		scormvalidator.setPerformValidationToSchema(iValidate);

		scormvalidator.validate(manifestFilePath, "pif",
				"contentaggregation", false);

		// retrieve object that stores the results of the validation activites
		ADLValidatorOutcome outcome = scormvalidator.getADLValidatorOutcome();

		try {
			mLogger.info("Document parsing complete.");

			if ((!iValidate && outcome.getDoesIMSManifestExist() && outcome.getIsWellformed())
					|| (iValidate && (outcome.getDoesIMSManifestExist()
							&& outcome.getIsWellformed()
							&& outcome.getIsValidToSchema()
							&& outcome.getIsValidToApplicationProfile() 
							&& outcome.getDoRequiredCPFilesExist()))) {
				
				outcome.rollupSubManifests(false);
				mDocument = outcome.getDocument();
				this.manifest = outcome.getRootNode();
				organizationList = outcome.getOrganizationList(manifest);
		        resoucesList = outcome.getAllResources(manifest);
		        
			} else {
				if (!(outcome.getIsWellformed())) mLogger.info("NOT WELL FORMED!!!");
				if (!(outcome.getIsValidToSchema())) mLogger.info("NOT VALID TO SCHEMA!!!");
				if (!(outcome.getIsValidToApplicationProfile())) mLogger.info("NOT VALID TO APP PROFILE!!!");
				if (!(outcome.getDoRequiredCPFilesExist())) mLogger.info("REQUIRED FILES DO NOT EXIST!!!");
				mLogger.info("-----NOT CONFORMANT!!!----");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		mLogger.exiting("---ManifestHandler", "processManifest()");
		return outcome;
	}

    /**
     * 
     * @param courseSeq
     * @return
     * @throws Exception
     */
    public Hashtable getSeqActivityTree(String courseSeq, boolean pif) {
    	Hashtable seqActivityTable = new Hashtable();
    	
    	try {
    		Node manifestNode = (Node)mDocument.getDocumentElement();
    		Vector orgList = this.getOrganizationNodes(manifestNode, false);
    		
    		if (orgList != null) {

				for (int i = 0; i < orgList.size(); i++){
					log.info("orgList.elementAt("+i+") : "+orgList.elementAt(i));
					SeqActivityTree mySeqActivityTree = new SeqActivityTree();
					Node currentOrganization = (Node)orgList.elementAt(i);
					DOMTreeUtility.getNodeValue(DOMTreeUtility.getNode( currentOrganization, "title" ));					
					
					String currentIdentifier = DOMTreeUtility.getAttributeValue( currentOrganization,"identifier");
					
			        //create a SeqActivityTree and serialize it----------
		            String tempObjectivesGlobalToSystem = DOMTreeUtility.getAttributeValue( currentOrganization,"objectivesGlobalToSystem");
		            mySeqActivityTree = ADLSeqUtilities.buildActivityTree(currentOrganization, this.getSeqCollection());
	
				    if( tempObjectivesGlobalToSystem.equals("false") ) {
				       mySeqActivityTree.setScopeID(courseSeq);
				    }
				
				    mySeqActivityTree.setCourseID(courseSeq);
				    seqActivityTable.put(currentIdentifier, mySeqActivityTree);       
				}
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return seqActivityTable;
    }
    
    /**
     * This method retrieves all the organization nodes from the content package
     * manifest dom.  This method serves as a helper for retrieving SCO
     * launch data. <br>
     *
     * @param iDefaultOrganizationOnly boolean describing the scope of the
     * organization that should be traversed for SCO launch data. Specific to
     * SRTE uses - will no longer be needed in future development.<br>
     * @param iRootNode root node of test subject dom.<br>
     * @return Vector Containing a list of organization nodes.<br>
     */
    public Vector getOrganizationNodes( Node iRootNode, boolean iDefaultOrganizationOnly ) {
    	
		Vector result = new Vector();
		
		if ( iDefaultOrganizationOnly ) {
			result.add( this.getDefaultOrganizationNode( iRootNode ) );
		}else {
			Node organizationsNode = DOMTreeUtility.getNode( iRootNode, "organizations" );
			NodeList children = organizationsNode.getChildNodes();
		
			if ( children != null ) {
				int numChildren = children.getLength();
			
				for ( int i = 0; i < numChildren; i++ ) {
					Node currentChild = children.item(i);
					String currentChildName = currentChild.getLocalName();
				
					if ( currentChildName.equals( "organization" ) ) {
						result.add( currentChild );
					}
				}
			}
		}
		
		return result;
	}
    
    /**
     * This method returns the default organization node that is flagged
     * by the default attribute. This method serves as a helper method. <br>
     *
     * @param iRootNode root node of test subject dom.<br>
     * @return Node default organization<br>
     */
    public Node getDefaultOrganizationNode( Node iRootNode ) {
       Node result = null;

       // find the value of the "default" attribute of the <organizations> node
       Node organizationsNode = DOMTreeUtility.getNode( iRootNode, "organizations" );
       NamedNodeMap attrList = organizationsNode.getAttributes();
       String defaultIDValue = (attrList.getNamedItem("default")).getNodeValue();

       // traverse the <organization> nodes and find the matching default ID
       NodeList children = organizationsNode.getChildNodes();

       if ( children != null ) {
          int numChildren = children.getLength();

          for ( int i = 0; i < numChildren; i++ ) {
             Node currentChild = children.item(i);
             String currentChildName = currentChild.getLocalName();

             if ( currentChildName.equals( "organization" ) ) {
                // find the value of the "identifier" attribute of the
                // <organization> node
                NamedNodeMap orgAttrList = currentChild.getAttributes();
                String idValue =
                   (orgAttrList.getNamedItem("identifier")).getNodeValue();

                if ( idValue.equals( defaultIDValue ) ) {
                   result = currentChild;
                   break;
                }
             }
          }
       }

       return result;
    }
    
	private Node getSeqCollection(){
		Node manifestNode = (Node)mDocument.getDocumentElement();
		return DOMTreeUtility.getNode( manifestNode, "sequencingCollection" );
		
	}
}
