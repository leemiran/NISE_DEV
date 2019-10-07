/******************************************************************************
 **
 ** Advanced Distributed Learning Co-Laboratory (ADL Co-Lab) grants you
 ** ("Licensee") a non-exclusive, royalty free, license to use, modify and
 ** redistribute this software in source and binary code form, provided that
 ** i) this copyright notice and license appear on all copies of the software;
 ** and ii) Licensee does not utilize the software in a manner which is
 ** disparaging to ADL Co-Lab.
 **
 ** This software is provided "AS IS," without a warranty of any kind.  ALL
 ** EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 ** ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 ** OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.  ADL Co-Lab AND ITS LICENSORS
 ** SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 ** USING, MODIFYING OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES.  IN NO
 ** EVENT WILL ADL Co-Lab OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE,
 ** PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 ** INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE
 ** THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE
 ** SOFTWARE, EVEN IF ADL Co-Lab HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH
 ** DAMAGES.
 **
 ******************************************************************************/
package com.ziaan.scorm2004.validator.contentpackage;

// native java imports
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ziaan.scorm2004.parsers.dom.DOMTreeUtility;
import com.ziaan.scorm2004.util.Message;
import com.ziaan.scorm2004.util.MessageCollection;
import com.ziaan.scorm2004.util.MessageType;
import com.ziaan.scorm2004.util.zip.UnZipHandler;
import com.ziaan.scorm2004.validator.ADLSCORMValidator;
import com.ziaan.scorm2004.validator.RulesValidator;
import com.ziaan.scorm2004.validator.sequence.SequenceValidator;

/**
 * <strong>Filename: </strong>CPValidator.java<br>
 *
 * <strong>Description: </strong>The <code>CPValidator</code> determines
 * whether the content package test subject is conformant with the Content
 * Package Application Profiles( resource, content aggregation) as defined in
 * the Content Aggregation Model of the SCORM.  The Content Package Validator
 * performs the following checks:  1) Determines if the required files exist at
 * the root of the package,  2) Parses the manifest for wellformedness,
 * 3) Parses the manifest for validation to the controlling documents, 4)
 * Determines whether or not extensions were used in the manifest, 5) Determines
 * if the manifest abides to the rules defined for the Content Package
 * Application Profile rules, and 6) Validates external metadata and scos if the
 * using system chooses to.
 *
 * The CPValidator inherites from the ADLSCORMValidator to determine if the
 * imsmanifest is wellformed and valid to the xsd(s).  The CPValidator object
 * validates the content package test subject against the rules and requirements
 * necessary for meeting each Content Package Application Profile.<br><br>
 *
 * <strong>Design Issues: </strong>none<br><br>
 *
 * <strong>Implementation Issues: </strong>none<br><br>
 *
 * <strong>Known Problems: </strong>none<br><br>
 *
 * <strong>Side Effects: </strong>Populates the MessageCollection<br><br>
 *
 * <strong>References: </strong>SCORM<br><br>
 *
 * @author ADL Technical Team <br><br>
 */
public class CPValidator extends ADLSCORMValidator
{

    /**
     * Logger object used for debug logging <br>
     */
    private Logger mLogger;

    /**
     * This attribute serves as the object that contains the information required
     * for the SCORM Validation checks, containing the DOM of the XML rules that
     * are neccessary for meeting conformance to each of the Application Profiles.
     * <br>
     */
    private RulesValidator mRulesValidator;

    /**
     * This attribute contains the base directory of where the test subject
     * is located. It is used by the validator to determine the location
     * of the package resources, including the imsmanifest, sco's, and/or
     * metadata.  <br>
     */
    private String mBaseDirectory;

    /**
     * This attribute contains the list of URLs as defined in the XML base
     * attributes.  The XML base attribute specifies a base URI other than the
     * base URI of the document or external entity.  <br>
     */
    private String[][] mXMLBase = new String[3][2];

    /**
     * This attribute contains the list of organization level identifier
     * values found.  This value is tracked to check that the IDREFs point to
     * valid identifiers.<br>
     */
    private Vector mOrganizationIdentifierList;

    /**
     * This attribute serves as the data structure used to store the Launch Data
     * information of SCOs and Metadata referenced within the content package.
     * <br>
     */
    private ManifestHandler mManifestHandler;

    /**
     *  This attribute holds the value containing the environment variable.  The
     * environment variable is set to the using systems install directory.<br>
     */
    private String mEnvironmentVariable;

    /**
     * This attribute allows full validation activities (required files check,
     * schema validation, application profile checks, extension detection) to be
     * turned off, allowing only wellformedness checks to occur.<br>
     */
    protected boolean mPerformFullValidation;

    /**
     * This attribute contains the populated ManifestMap object, containing all
     * the information needed to validate manifest elements with/without the
     * existance of (Sub) manifest(s).<br>
     */
    private ManifestMap mManifestInfo;

    /**
     * This attribute identifies the manifest that is up for validation during
     * the application profile checks.
     */
    private String mManifestID;

    /**
     * This attribute houses the resource nodes needed to validate that
     * initialization data is pointing to a sco.<br>
     */
    private Vector mResourceNodes;

    /**
     * This attribute houses the idref values that have been found to be valid
     * references.<br>
     */
    private Vector mValidIdrefs;

    /**
     * Default Constructor.  Sets the attributes to their initial values.<br>
     *
     * @param iEnvironmentVariable the value specific to the location of the
     * using system's install.<br>
     */
    public CPValidator(String iEnvironmentVariable)
    {
        super("contentpackage");
        mLogger = Logger.getLogger("org.adl.util.debug.validator");
        mBaseDirectory = "";

        // Initialize the 2-dimensional array of xml:base attributes
        mXMLBase[0][0] = "manifest";
        mXMLBase[1][0] = "resources";
        mXMLBase[2][0] = "resource";
        mXMLBase[0][1] = "";
        mXMLBase[1][1] = "";
        mXMLBase[2][1] = "";

        mOrganizationIdentifierList = new Vector();
        mRulesValidator = new RulesValidator("contentpackage");
        mManifestHandler = new ManifestHandler();
        mEnvironmentVariable = iEnvironmentVariable;
        mManifestInfo = new ManifestMap();
        mManifestID = "";
        mValidIdrefs = new Vector();
    }

    /**
     * This method initiates the validation process.  The checks called from this
     * method include the required files check, manifest checks (wellformedness
     * and schema validation), and application profile checks.  If the test
     * subject is in the form of a zip, than the extract if performed prior to the
     * calls mentioned above.<br>
     *
     * @param iFileName The name of the SCORM Content Package test subject<br>
     * @param iTestType The type of test subject ( pif or media )<br>
     * @param iApplicationProfileType The Application Profile type of the test
     *    subject (content aggregation or resource )
     * @param iManifestOnly The boolean describing whether or not the IMS Manifest
     *    is to be the only subject validated.  True implies that validation
     *    occurs only on the IMS Manifest (checks include wellformedness, schema
     *    validation, and application profile checks).  False implies that the
     *    entire Content Package be validated (IMS Manifest checks with the
     *    inclusion of the required files checks, metadata, and sco testing).
     *
     * @return - Boolean value indicating the outcome of the validation checks<br>
     * True implies that validation was error free, false implies otherwise.<br>
     */
    public boolean validate(String iFileName, String iTestType, String iApplicationProfileType, boolean iManifestOnly)
    {
        boolean validateResult = true;
        String msgText;

        mLogger.entering("CPValidator", "validate()");
        mLogger.finer("      iXMLFileName coming in is " + iFileName);
        mLogger.finer("      iApplicationProfileType coming in is " + iApplicationProfileType);
        mLogger.finer("      iTestType coming in is " + iTestType);

        // If the CTS is testing a Package Interchange File, then import the
        // content package (unzip and set up the CTS).
        if (iTestType.equals("pif"))
        {
            importContentPackage(iFileName);

            mBaseDirectory = mEnvironmentVariable + File.separator + "PackageImport" + File.separator;
        }
        else
        {
            mBaseDirectory = getPathOfFile(iFileName);
        }

        // Create the absolute URL for the location of the imsmanifest.xml
        String imsManifestFile = new String(mBaseDirectory + "imsmanifest.xml");

        // Now check to see if the manifest file is present.
        File manifestFile = new File(imsManifestFile);
        boolean manifestFound = true;

        // Test to see whether or not the imsmanifest.xml file exists as defined
        // by the pathname.
        if (!manifestFile.exists())
        {
            msgText = "Required file \"imsmanifest.xml\" not " + "found at the root of the package";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

            manifestFound = false;
        }

        // Set the ADLSCORMValidator protected attribute with this value.
        // This value is needed for summary logging and ADLValidatorOutcome
        super.setIsIMSManifestPresent(manifestFound);

        // Retrieve imsmanifest for wellformedness and validation parse if
        // high level checks passed
        if (manifestFound)
        {
            msgText = "Testing the manifest for Well-formedness";
            mLogger.info("INFO: " + msgText);

            validateResult = checkWellformedness(imsManifestFile) && validateResult;

            boolean wellformed = super.getIsWellformed();

            // determine if the root element belongs to the IMS namespace
            // can only perform this check if we have a wellformed document.
            boolean validRoot = false;
            if (wellformed)
            {
                validRoot = super.isRootElementValid();
                super.setIsRootElement(validRoot);
            }

            // continue to validate only if we are dealing with an IMS Manifest
            if (validRoot)
            {
                if (wellformed && !iManifestOnly && mPerformFullValidation)
                {
                    // If the imsmanifest.xml is well-formed, then check to make
                    // sure all of the required files are present.
                    validateResult = checkForRequiredFiles(mBaseDirectory) && validateResult;
                }

                if (validateResult && mPerformFullValidation)
                {
                    msgText = "Testing the manifest for Validity to the Controlling Documents";
                    mLogger.info("INFO: " + msgText);

                    // The imsmanifest.xml file is well-formed and all of the required
                    // files were present, now check to see if the imsmanifest.xml
                    // is valid according to the controlling schemas.
                    validateResult = checkValidityToSchema(imsManifestFile) && validateResult;

                    msgText = "****************************************";
                    mLogger.info("OTHER: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.OTHER, msgText));

                    msgText = "Validating against the Application Profile Rules";
                    mLogger.info("INFO: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));

                    // Prepare for manifest idref verification with the inclusion
                    // of (sub)manifests if used

                    msgText = "Performing Validation on ID/IDRef mappings";
                    mLogger.info("INFO: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));

                    boolean populateResult = mManifestInfo.populateManifestMap(super.getDocument());

                    boolean idrefResult = true;
                    if (populateResult)
                    {
                        Vector idrefs = mManifestInfo.checkAllIdReferences();

                        if (!idrefs.isEmpty())
                        {
                            mLogger.info("invalid idrefs exist");
                            validateResult = false && validateResult;

                            idrefResult = false;

                            super.setIsValidToApplicationProfile(super.getIsValidToApplicationProfile() && false);
                        }
                        else
                        {
                            msgText = "ID/IDRef Validation checks passed";
                            mLogger.info("INFO: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                        }
                    }

                    // Read in XML rules for Application Profile Checks
                    boolean doXMLRulesExist = mRulesValidator.readInRules(iApplicationProfileType, mEnvironmentVariable);

                    if (doXMLRulesExist)
                    {
                        trackOrgIdentifiers(super.getDocument());
                        boolean isAppProfileResult = compareToRules(super.getDocument(), "");

                        // Set the ADLSCORMValidator protected attribute with this value
                        // This value is needed for summary logging and
                        // ADLValidatorOutcome
                        super.setIsValidToApplicationProfile(idrefResult && isAppProfileResult);

                        validateResult = isAppProfileResult && validateResult;
                    }
                    else
                    {
                        // XML rules cannot be found to perform app profile checking,
                        // system error
                        msgText = "Testing the manifest against the Application Profile "
                                + "Rules cannot be performed due to a system error. "
                                + "This is due to the inaccessability of certain key "
                                + "files of the Application.  It is recommended to "
                                + "repair or reinstall the Application and re-run.";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                        validateResult = false && validateResult;
                    } // end if the XML Rules Exist
                } // end if perform full validation is requested
            } // end if dealing with IMS Namespace Manifest
            else
            {
                validateResult = false;
            }
        } // end if the manifest was found

        mLogger.exiting("CPValidator", "validate()");

        return validateResult;
    }

    /**
     * This method extracts the inputed test subject, in the form of a zip file,
     * to a temporary "/PackageImport" directory in order for validation
     * activites to be performed.<br>
     *
     * @param iPIF The content package zip test file URI<br>
     */
    private void importContentPackage(String iPIF)
    {
        // get the extract dir
        mLogger.entering("CPValidator", "importContentPackage()");

        String extractDir = mEnvironmentVariable + File.separator + "PackageImport" + File.separator;

        mLogger.info("extractDir = " + extractDir);

        // Unzip the content package into a local directory for processing
        UnZipHandler uzh = new UnZipHandler(iPIF, extractDir);
        uzh.extract();

        mLogger.exiting("CPValidator", "importContentPackage()");
    }

    /**
     * This method performs the validation on the imsmanifest.xml file - including
     * wellformedness and validation to the schema checks.  This method is called
     * when the validate method is spanned.<br>
     *
     * @param iManifestFileName - The URI location of the manifest that is to be
     *    parsed for wellformedness and validation to the schema(s).<br>
     *
     * @return - boolean result of the parse.  True implies that the manifest
     *    was wellformed and valid to the schema, false implies otherwise.<br>
     */
    private boolean checkValidityToSchema(String iManifestFileName)
    {
        mLogger.entering("CPValidator", "checkValidityToSchema()");
        boolean manifestResult = true;

        // Send imsmanifest.xml for wellformedness and validation parse
        super.setPerformFullValidation(mPerformFullValidation);
        super.performValidatorParse(iManifestFileName);
        boolean isValid = super.getIsValidToSchema();

        boolean isWellformed = super.getIsWellformed();
        if (mPerformFullValidation)
        {
            if (isValid)
            {
                mLogger.info("PASSED: " + "The manifest instance is valid to the controlling documents");
            }
            else
            {
                mLogger.info("FAILED: " + "The manifest instance is not valid to the controlling documents");
            }

            manifestResult = (isWellformed && isValid);
        }
        else
        {
            manifestResult = isWellformed;
        }

        mLogger.exiting("CPValidator", "checkValidityToSchema()");

        return manifestResult;
    }

    /**
     * This method performs the validation on the imsmanifest.xml file - including
     * wellformedness.  This method is called when the CPValidator.validate method
     * is spanned.  When we complete this method, we will have a wellformed
     * document that has been stripped of extenions attributes/elements, comments,
     * and whitespace.  <br>
     *
     * @param iManifestFileName - The URI location of the manifest that is to be
     *    parsed for wellformedness and validation to the schema(s).<br>
     *
     * @return - boolean result of the parse.  True implies that the manifest
     *    was wellformed and valid to the schema, false implies otherwise.<br>
     */
    private boolean checkWellformedness(String iManifestFileName)
    {
        mLogger.entering("CPValidator", "checkWellformedness()");
        boolean wellnessResult = true;
        String msgText;

        // Send imsmanifest for wellformedness and validation parse
        super.setPerformFullValidation(false);
        super.performValidatorParse(iManifestFileName);

        boolean isWellformed = super.getIsWellformed();

        wellnessResult = isWellformed;

        mLogger.exiting("CPValidator", "checkWellformedness");

        return wellnessResult;
    }

    /**
     * This method performs the check that the required files - imsmanifest and
     * all supporting controlling documents - exist at the root of the package.
     * This method throws an error if the imsmanifest.xml, ADL CP xsd, or IMS CP
     * controlling documents are not detected at the root of the content package
     * test subject. Warnings are thrown if the IMS SS or IEEE LOM Custom
     * controlling documents are not detected at the root of the content package
     * test subject. At this time, the controlling documents are hardcoded.
     * Future enhancements include the detection of the controlling documents
     * based off of the root manifest declaration.<br>
     *
     * @param iDirectory The directory to check to see if the required files
     *                   are present.
     *
     * @return
     */
    private boolean checkForRequiredFiles(String iDirectory)
    {
        mLogger.entering("CPValidator", "checkForRequiredFiles()");
        boolean result = true;
        String msgText = "";

        msgText = "****************************************";
        mLogger.info("OTHER: " + msgText);
        MessageCollection.getInstance().add(new Message(MessageType.OTHER, msgText));
        msgText = "Searching for Files Required For XML Parsing";
        mLogger.info("INFO: " + msgText);
        MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));

        int requiredFileResult = findAndLocateRequiredFiles(super.getSchemaLocation(), iDirectory);

        // Determines if default checking is needed
        if (requiredFileResult == 0)
        {
            result = false;
        }
        else if (requiredFileResult == 1)
        {
            result = true;
        }

        msgText = "****************************************";
        mLogger.info("OTHER: " + msgText);
        MessageCollection.getInstance().add(new Message(MessageType.OTHER, msgText));

        mLogger.finest("returning the following result " + result);
        mLogger.exiting("CPValidator", "checkForRequiredFiles()");

        // Set the ADLSCORMValidator protected attribute for summary log info
        super.setIsRequiredFiles(result);

        return result;
    }

    /**
     *  This method parses the mSchemaLocation value and retrieves only the
     *  schema name.  Once the schema name is tokenized, it is then checked for
     *  existance at the root of the package. <br>
     *
     * @param iSchemaLocations -- the string value that houses the schemaLocations
     * <br>
     * @param iDirectory -- the base directory of the test suite that is used to
     *                      build the location of the schemas.<br>
     *
     *  @return  1 if files are found,
     *           0 if files are not found.<br>
     */
    private int findAndLocateRequiredFiles(String iSchemaLocations, String iDirectory)
    {
        int result = 1;
        //String schemaStr = "";
        File currentFile;
        boolean currentResult;
        String msgText = "";

        String xsdLocation = iDirectory.replace('\\', '/');

        StringTokenizer st = new StringTokenizer(iSchemaLocations, " ");

        //Parse the schemaLocation value to obtain only the schemaName
        // then check each to see if it exists in the package

        while (st.hasMoreTokens())
        {
            //This is the namespace value
            String tok = st.nextToken();

            if (st.hasMoreTokens())
            {
                // this is the schemaLocation value
                tok = st.nextToken();

                StringTokenizer schemaLocationTokens = new StringTokenizer(tok, "/");

                String schemaName = new String();
                String toke = new String();
                String previousToke = new String();
                boolean verifyExistance = true;

                while (schemaLocationTokens.hasMoreTokens())
                {
                    // Need to obtain only the schema files name from the string
                    previousToke = toke;
                    toke = schemaLocationTokens.nextToken();

                    if (!schemaLocationTokens.hasMoreTokens())
                    {
                        if (!previousToke.equals("vocab"))
                        {
                            schemaName = xsdLocation + toke;
                        }
                        else
                        {
                            // Do not need to verify schemas that aren't located
                            // at the root (i.e.  custom vocab schemas).
                            verifyExistance = verifyExistance && false;
                        }
                    }

                }

                if (verifyExistance)
                {
                    // Test if this file exists
                    currentFile = new File(schemaName);
                    currentResult = currentFile.exists();

                    if (currentResult)
                    {
                        msgText = "File \"" + toke + "\" found " + "at the root of the content package";
                        mLogger.info("PASSED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                    }
                    else
                    {
                        msgText = "File \"" + toke + "\" not " + "found at the root of the content package.  All "
                                + "directly referenced XML control " + "documents (XSD, DTD, etc.) "
                                + "must be placed at the root of the content package.";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = 0;

                    }
                }
            }
        }

        return result;

    }

    /**
     * This method retrieves the directory location of the test subject by
     * truncating the filename off of the URL passed in.<br>
     *
     * @param iFileName The absolute path of the test subject file<br>
     *
     * @return - the directory that the file is located<br>
     */
    private String getPathOfFile(String iFileName)
    {
        mLogger.entering("CPValidator", "getPathOfFile()");

        String result = new String("");
        String tmp = new String("");

        try
        {
            StringTokenizer token = new StringTokenizer(iFileName, File.separator, true);

            int numTokens = token.countTokens();

            // We want all but the last token added
            numTokens--;

            for (int i = 0; i < numTokens; i++)
            {
                tmp = token.nextToken();

                mLogger.finest("token = " + tmp);

                result = result + tmp;
            }
        }
        catch (NullPointerException npe)
        {
            npe.printStackTrace();
        }

        mLogger.exiting("CPValidator", "getPathOfFile()");

        return result;
    }

    /**
     * This method tracks all identifier values found in the manifest for the
     * organization elements only. Tracking of organization identifiers is
     * performed in order to verify that default attribute points to valid
     * organization identifier value.  These identifers are tracked
     * recursively by walking through test subject dom and adding the identifier
     * elements found to a list.<br>
     *
     * @param iParentNode
     *               Root node of the test subject<br>
     *
     * @param iPath
     *           Path of the element<br>
     */
    private void trackOrgIdentifiers(Node iParentNode)
    {
        // recursively find the organization ids and add them to the vector

        mLogger.entering("CPValidator", "trackOrgIdentifiers()");
        String msgText = new String();

        if (iParentNode != null)
        {
            int type = iParentNode.getNodeType();

            switch (type)
            {
            case Node.DOCUMENT_NODE:
            {
                Node rootNode = ((Document) iParentNode).getDocumentElement();

                trackOrgIdentifiers(rootNode);

                break;
            }

            case Node.ELEMENT_NODE:
            {
                String nodeName = iParentNode.getLocalName();

                if (nodeName.equals("manifest"))
                {
                    Node orgsNode = DOMTreeUtility.getNode(iParentNode, "organizations");

                    if (orgsNode != null)
                    {
                        Vector orgNodes = DOMTreeUtility.getNodes(orgsNode, "organization");

                        // Loop through the oganization elements to retrieve the
                        // identifier attribute values

                        int orgNodesSize = orgNodes.size();
                        for (int i = 0; i < orgNodesSize; i++)
                        {
                            Node currentChild = (Node) orgNodes.elementAt(i);
                            String OrgId = DOMTreeUtility.getAttributeValue(currentChild, "identifier");

                            mOrganizationIdentifierList.add(OrgId);
                            msgText = "Just added " + OrgId + "to the org vector";
                            mLogger.finest(msgText);
                        }
                    }
                }

                // Get (sub)manifests and make call recursively
                Vector subManifestList = DOMTreeUtility.getNodes(iParentNode, "manifest");

                int subManifestListSize = subManifestList.size();
                for (int j = 0; j < subManifestListSize; j++)
                {
                    Node currentSubManifest = (Node) subManifestList.elementAt(j);
                    trackOrgIdentifiers(currentSubManifest);
                }
                break;
            }
            }
        }

        mLogger.exiting("CPValidator", "trackOrgIdentifiers()");
    }

    /**
     * This method performs the application profile checks for the
     * persistState attribute.  The application profile checks include
     * verifying that the attribute belongs to the ADL CP namespace and that
     * it exists as an attribute of the IMS resource element only.<br>
     *
     * @param iCurrentAttribute  the adlcp:persistState attribute to be tested<br>
     * @param iParentNode the parent element that the attribute belongs to. <br>
     *
     * @return boolean True implies that the application profile checks passed;
     * false implies that they did not<br>
     *
     */
    private boolean checkPersistStateReq(Attr iCurrentAttribute, Node iParentNode)
    {
        boolean result = false;

        if (DOMTreeUtility.isAppropriateElement(iParentNode, "resource", "http://www.imsglobal.org/xsd/imscp_v1p1"))
        {
            result = true;
        }
        else
        {
            String msgText = "The attribute \"" + iCurrentAttribute.getLocalName()
                    + "\" can only exist as an attribute of a <resource> element";

            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

        }
        return result;
    }

    /**
     * This method performs the application profile checks for the
     * adlcp:scormType attribute.  The application profile checks include
     * verifying that the attribute belongs to the ADL CP namespace and that
     * it exists as an attribute of the IMS resource element only.<br>
     *
     * @param iCurrentAttribute  the scormType attribute to be tested<br>
     * @param iParentNode the parent element that the attribute belongs to. <br>
     *
     * @return boolean True implies that the application profile checks passed;
     * false implies that they did not<br>
     *
     */
    private boolean checkSCORMTypeReq(Attr iCurrentAttribute, Node iParentNode)
    {
        boolean result = false;

        if (DOMTreeUtility.isAppropriateElement(iParentNode, "resource", "http://www.imsglobal.org/xsd/imscp_v1p1"))
        {
            result = true;
        }
        else
        {
            String msgText = "The attribute \"" + iCurrentAttribute.getLocalName()
                    + "\" can only exist as an attribute of a <item> element";

            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

        }
        return result;
    }

    /**
     * This method performs the application profile checks for the
     * adlcp:objectivesGlobalToSystem attribute.  The application profile
     * checks include verifying that the attribute belongs to the IMS
     * namespace and that it exists as an attribute of the IMS organization
     * element only.<br>
     *
     * @param iCurrentAttribute  the objectivesGlobalToSystem attribute to
     *                           be tested<br>
     * @param iParentNode the parent element that the attribute belongs to. <br>
     *
     * @return boolean True implies that the application profile checks passed;
     * false implies that they did not<br>
     *
     */
    private boolean checkObjGlogalToSystemReq(Attr iCurrentAttribute, Node iParentNode)
    {
        boolean result = false;

        if (DOMTreeUtility.isAppropriateElement(iParentNode, "organization", "http://www.imsglobal.org/xsd/imscp_v1p1"))
        {
            result = true;
        }
        else
        {
            String msgText = "The attribute \"" + iCurrentAttribute.getLocalName()
                    + "\" can only exist as an attribute of a <organization> element";

            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

        }
        return result;
    }

    /**
     * This method performs the meat of the application profile checks.  The
     * application profiles are described in XML format.  Each application
     * profile has its own XML representation.  These XML rules are parsed in
     * order to form a document object.  The test subject manifest is also
     * available in document format.  This method compares the manifest to the
     * rules described in the XML dom.  This recursive method is driven by the
     * test subject dom. <br>
     *
     * @param iTestSubjectNode
     *               Test Subject DOM<br>
     * @param iPath
     *               Path of the rule to compare to<br>
     *
     * @return - boolean result of the checks performed.  True if the check was
     * conformant, false otherwise.<br>
     */
    private boolean compareToRules(Node iTestSubjectNode, String iPath)
    {
        mLogger.entering("CPValidator", "compareToRules");
        mLogger.finest("Node: " + iTestSubjectNode.getLocalName());
        mLogger.finest("Namespace: " + iTestSubjectNode.getNamespaceURI());
        mLogger.finest("Path: " + iPath);

        boolean result = true;
        String msgText = new String();

        // is there anything to do?
        if (iTestSubjectNode == null)
        {
            result = false;
            return result;
        }

        // Determine which type of DOM Tree Node we are dealing with
        switch (iTestSubjectNode.getNodeType())
        {
        case Node.PROCESSING_INSTRUCTION_NODE:
        {
            // Skip any processing instructions, nothing for us to do
            break;
        }
        case Node.DOCUMENT_NODE:
        {
            // Found the root document node

            Node rootNode = ((Document) iTestSubjectNode).getDocumentElement();
            String rootNodeName = rootNode.getLocalName();

            mLogger.finest("DOCUMENT_NODE found");
            mLogger.finest("Namespace: " + rootNode.getNamespaceURI());
            mLogger.finest("Node Name: " + rootNodeName);

            msgText = "Testing element <" + rootNodeName + "> for minimum conformance";
            mLogger.info("INFO: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
            msgText = "Multiplicity for element <" + rootNodeName + "> has been verified";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));

            result = compareToRules(rootNode, "") && result;

            break;
        }
        case Node.ELEMENT_NODE:
        {
            // Found an Element Node
            String parentNodeName = iTestSubjectNode.getLocalName();

            mLogger.finest("ELEMENT_NODE found");
            mLogger.finest("Namespace: " + iTestSubjectNode.getNamespaceURI());
            mLogger.finest("Node Name: " + parentNodeName);

            // If the node is an <imscp:manifest>, then retrieve <resource>
            // nodes in the manifest.  This is needed later for SCO Resource
            // reference validation.
            mLogger.finest("Looking for <imscp:resource> elements defined " + "as children of the <imscp:manifest> element");
            Node resourcesNode = DOMTreeUtility.getNode(iTestSubjectNode, "resources");
            if (resourcesNode != null)
            {
                mResourceNodes = DOMTreeUtility.getNodes(resourcesNode, "resource");
            }

            String dataType = null;
            int multiplicityUsed = -1;
            int minRule = -1;
            int maxRule = -1;
            int spmRule = -1;

            mLogger.finest("Looping through attributes for the input " + "element <" + parentNodeName + ">");

            // Look for the attributes of this element
            NamedNodeMap attrList = iTestSubjectNode.getAttributes();
            int numAttr = attrList.getLength();
            mLogger.finer("There are " + numAttr + " attributes of <" + parentNodeName + "> elememt to test");

            Attr currentAttrNode = null;
            String currentNodeName = "";
            String attributeValue = "";

            // Loop throught attributes
            for (int i = 0; i < numAttr; i++)
            {
                currentAttrNode = (Attr) attrList.item(i);
                currentNodeName = currentAttrNode.getLocalName();
                mLogger.finest("Processing the [" + currentAttrNode.getNamespaceURI() + "] " + currentNodeName + " attribute of the <"
                        + parentNodeName + "> element.");

                //  If the current attribute is persistState then additional
                // checks may be necessary
                if (currentNodeName.equalsIgnoreCase("persistState"))
                {
                    // Application Profile Check: Check that adlcp:persistState
                    // attribute can only appear on an <imscp:resource> element.
                    result = checkPersistStateReq(currentAttrNode, iTestSubjectNode) && result;
                }

                // If the current attribute is scormType then additional
                // checks may be necessary
                if (currentNodeName.equalsIgnoreCase("scormType"))
                {
                    // Application Profile Check:  Check to make sure that the
                    // adlcp:scormType attribute can only appear on an
                    // <imscp:resource> element

                    result = checkSCORMTypeReq(currentAttrNode, iTestSubjectNode) && result;

                }

                // If the current attribute is objectivesGlobalToSystem then additional
                // checks may be necessary
                if (currentNodeName.equalsIgnoreCase("objectivesGlobalToSystem"))
                {
                    // Application Profile Check:  Check that the
                    // adlseq:objectivesGlobalToSystem attribute can only appear on
                    // an <imscp:organization> element.
                    result = checkObjGlogalToSystemReq(currentAttrNode, iTestSubjectNode) && result;
                }

                // Retrieve the application profile rules only if the the current
                // attribute being processed has SCORM application profile
                // requirements
                mLogger.finest("Additional checks needed for attribute [" + currentNodeName + "].\n");
                // Retreive the data type rules
                dataType = mRulesValidator.getRuleValue(parentNodeName, iPath, "datatype", currentNodeName);

                // If the data type rules are for an xml:base, then there is
                // more processing that needs to take place.
                if (dataType.equalsIgnoreCase("xmlbase"))
                {
                    // This is a xml:base data type
                    msgText = "Testing attribute \"" + currentNodeName + "\" for minimum conformance";
                    mLogger.info("INFO: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));

                    multiplicityUsed = getMultiplicityUsed(attrList, currentNodeName);

                    // We will assume that no attribute can exist more than
                    // once (ever).  According to W3C.  Therefore, min and max
                    // rules must exist.

                    // Get the min rule and convert to an int
                    minRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "min", currentNodeName));

                    // Get the max rule and convert to an int
                    maxRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "max", currentNodeName));

                    if ((minRule != -1) || (maxRule != -1))
                    {
                        if (multiplicityUsed >= minRule && multiplicityUsed <= maxRule)
                        {
                            msgText = "Multiplicity for attribute \"" + currentNodeName + "\" has been verified";
                            mLogger.info("PASSED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                        }
                        else
                        {
                            if ((!iPath.startsWith("manifest.manifest"))
                                    && !((parentNodeName.equals("manifest")) && (iPath.equals("manifest"))))
                            {
                                msgText = "The attribute \"" + currentNodeName + "\" is not within the multiplicity bounds.";
                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                result = false && result;
                            }
                            else
                            {
                                msgText = "The attribute \"" + currentNodeName + "\" attribute is not within the "
                                        + "multiplicity bounds for (sub)manifest " + "elements.  At this time, there is no "
                                        + "guidance given in the SCORM on the " + "application of xml:base in " + "(sub)manifest(s).";
                                mLogger.info("WARNING: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                            }
                        } // mult used >= minRule && mult used <= maxRule
                    } // end minRule != -1, maxRule != -1

                    // Get the spm rule and convert to an int
                    spmRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "spm", currentNodeName));

                    attributeValue = currentAttrNode.getValue();

                    // Check the attributes for smallest permitted maximum(spm)
                    // conformance.

                    result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;

                    // Check to make sure slashes are correct
                    if (attributeValue.length() > 4)
                    {
                        if (!(attributeValue.substring(0, 4).equals("http")) && !(attributeValue.substring(0, 3).equals("ftp")))
                        {
                            result = checkForSlashes("xml:base", attributeValue) && result;
                        }
                    }
                    else
                    {

                        result = checkForSlashes("xml:base", attributeValue) && result;
                    }

                    if (parentNodeName.equals("manifest"))
                    {
                        mXMLBase[0][1] = attributeValue;
                        mLogger.info(" XML:base found in manifest, value is " + attributeValue);
                    }
                    else if (parentNodeName.equals("resources"))
                    {
                        mXMLBase[1][1] = attributeValue;
                        mLogger.info(" XML:base found in resources, value is " + attributeValue);
                    }
                    else if (parentNodeName.equals("resource"))
                    {
                        mXMLBase[2][1] = attributeValue;
                        mLogger.info(" XML:base found in resource, value is " + attributeValue);
                    }
                } // end if xml:base
            } // end looping over set of attributes for the element

            // If we are processing an <imscp:manifest> element, then there
            // are special cases application profile checks needed.
            if (parentNodeName.equalsIgnoreCase("manifest"))
            {
                mLogger.finest("Manifest node, additional check's being done.");
                mLogger.finest("Determining how many times the " + "identifier attribute is present.");

                multiplicityUsed = getMultiplicityUsed(attrList, "identifier");

                if (multiplicityUsed < 1)
                {
                    msgText = "Mandatory attribute \"identifier\" could not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false && result;
                }
            }
            else if (parentNodeName.equalsIgnoreCase("organizations")
                    && (mRulesValidator.getApplicationProfile()).equals("contentaggregation"))
            {
                // multiple <organization> elements exist, but there is no
                // default attribute.
                // not a conformance check, warning only
                multiplicityUsed = getMultiplicityUsed(attrList, "default");

                int numOrganizations = getMultiplicityUsed(iTestSubjectNode, "organization");

                if ((multiplicityUsed < 1) && !iPath.startsWith("manifest.manifest"))
                {
                    msgText = "Mandatory attribute \"default\" could not be found";
                    mLogger.info("ERROR: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false && result;
                }

            }
            else if (parentNodeName.equalsIgnoreCase("organization")
                    && (mRulesValidator.getApplicationProfile()).equals("contentaggregation"))
            {
                multiplicityUsed = getMultiplicityUsed(attrList, "identifier");
                if (multiplicityUsed < 1)
                {
                    msgText = "Mandatory attribute \"identifier\" could not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false && result;
                }
            }
            else if (parentNodeName.equalsIgnoreCase("item") && (mRulesValidator.getApplicationProfile()).equals("contentaggregation"))
            {
                multiplicityUsed = getMultiplicityUsed(attrList, "identifier");
                if (multiplicityUsed < 1)
                {
                    msgText = "Mandatory attribute \"identifier\" could not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false && result;
                }

                // need to perform a special check to warn when parameters
                // are present but no identifierref is
                int idrefMult = -1;
                int paramMult = -1;

                idrefMult = getMultiplicityUsed(attrList, "identifierref");
                paramMult = getMultiplicityUsed(attrList, "parameters");

                if ((idrefMult < 1) && !(paramMult < 1))
                {
                    // we have a parameters but no identifierref - warning
                    msgText = "The parameters attribute should not " + "be used when an identifierref attribute "
                            + "does not exist on an <item>";

                    mLogger.info("WARNING: " + msgText);

                    MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));

                }
                // have to store the idref values in a vector for future
                // app profile checking of resource attributes

                if (idrefMult >= 1)
                {
                    String iDREFValue = DOMTreeUtility.getAttributeValue(iTestSubjectNode, "identifierref");

                    boolean validIdref = checkForReferenceToScoOrAsset(iDREFValue);

                    if (validIdref)
                    {
                        mValidIdrefs.add(iDREFValue);
                    }
                }

                // Perform a special check to ensure that initialization data
                // only exists on items that reference SCOs
                NodeList childrenOfItem = iTestSubjectNode.getChildNodes();
                if (childrenOfItem != null)
                {
                    Node currentItemChild;
                    String currentItemChildName;
                    int len = childrenOfItem.getLength();
                    for (int k = 0; k < len; k++)
                    {
                        currentItemChild = childrenOfItem.item(k);
                        currentItemChildName = currentItemChild.getLocalName();

                        if (currentItemChildName.equals("timeLimitAction") || currentItemChildName.equals("dataFromLMS")
                                || currentItemChildName.equals("completionThreshold"))
                        {
                            if (idrefMult < 1)
                            {
                                // we have an item that contains initialization data
                                // and does not reference a resource at all

                                result = false && result;

                                msgText = "Only those <item> elements that reference" + " a SCO resource can contain the <"
                                        + currentItemChildName + "> element";

                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                            }
                            else
                            {
                                // we must verify that the resource it is referencing
                                // is a sco

                                String idrefValue = DOMTreeUtility.getAttributeValue(iTestSubjectNode, "identifierref");

                                result = result && checkForReferenceToSco(idrefValue);

                            }
                        }
                    }
                }
            }
            else if (parentNodeName.equalsIgnoreCase("resource"))
            {
                boolean resourceResult = checkResourceAttributes(iTestSubjectNode, attrList);

                result = result && resourceResult;
            }

            // test the attributes

            for (int j = 0; j < numAttr; j++)
            {
                currentAttrNode = (Attr) attrList.item(j);
                currentNodeName = currentAttrNode.getLocalName();

                dataType = mRulesValidator.getRuleValue(parentNodeName, iPath, "datatype", currentNodeName);

                // we do not want to test for xml namespaces or extensions

                if (dataType.equalsIgnoreCase("idref") || dataType.equalsIgnoreCase("id") || dataType.equalsIgnoreCase("vocabulary")
                        || dataType.equalsIgnoreCase("text") || dataType.equalsIgnoreCase("uri"))
                {
                    msgText = "Testing attribute \"" + currentNodeName + "\" for minimum conformance";
                    mLogger.info("INFO: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));

                    multiplicityUsed = getMultiplicityUsed(attrList, currentNodeName);

                    // We will assume that no attribute can exist more than
                    // once (ever).  According to W3C.  Therefore, min and max
                    // rules must exist.

                    //get the min rule and convert to an int

                    minRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "min", currentNodeName));

                    //get the max rule and convert to an int

                    maxRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "max", currentNodeName));

                    if ((minRule != -1) || (maxRule != -1))
                    {
                        if (multiplicityUsed >= minRule && multiplicityUsed <= maxRule)
                        {
                            msgText = "Multiplicity for attribute \"" + currentNodeName + "\" has been verified";
                            mLogger.info("PASSED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                        }
                        else
                        {
                            msgText = "The \"" + currentNodeName + "\" attribute is not within the " + "multiplicity bounds.";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false && result;
                        }
                    }

                    //get the spm rule and convert to an int
                    spmRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "spm", currentNodeName));

                    attributeValue = currentAttrNode.getValue();

                    if (dataType.equalsIgnoreCase("idref"))
                    {
                        // This is a IDREF data type
                        // check the attributes for smallest permitted maximum
                        // (spm) conformance.

                        result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;

                        // check the Default Idref to make sure it points to an
                        // valid identifier.

                        if (currentNodeName.equalsIgnoreCase("default"))
                        {
                            boolean foundDefaultIdentifier = false;
                            // check for this identifer in the organization list
                            int numOrganizationIdentifiers = mOrganizationIdentifierList.size();

                            for (int i = 0; i < numOrganizationIdentifiers; i++)
                            {
                                String identifier = (String) (mOrganizationIdentifierList.elementAt(i));

                                if (identifier.equals(attributeValue))
                                {
                                    foundDefaultIdentifier = true;

                                    break;
                                }
                            }
                            if (foundDefaultIdentifier)
                            {
                                msgText = "The \"" + currentNodeName + "\" attribute references a valid identifier";
                                mLogger.info("PASSED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                            }
                            else
                            {
                                msgText = "The \"" + currentNodeName + "\" attribute does not reference a "
                                        + "valid identifier of an <organization>";
                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                result = false && result;
                            }
                        }
                    }
                    else if (dataType.equalsIgnoreCase("id"))
                    {
                        // This is a id data type
                        // check the attributes for smallest permitted maximum
                        // (spm) conformance.

                        result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;

                        if (parentNodeName.equals("manifest"))
                        {
                            mManifestID = currentAttrNode.getValue();
                            msgText = "mManifestID is " + mManifestID;
                            mLogger.info(msgText);
                        }
                    }
                    else if (dataType.equalsIgnoreCase("uri"))
                    {
                        // This is a URI data type
                        // check the attributes for smallest permitted maximum
                        // (spm) conformance.  Only perform these checks if
                        // the value is not an empty string

                        String myAttributeValue = currentAttrNode.getValue();
                        if (!myAttributeValue.equals(""))
                        {
                            // check to ensure there are no leading slashes
                            result = checkForSlashes("href", myAttributeValue) && result;
                            result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;

                            // check if the file exists
                            // only apply XML:Base if root manifest level, not sub

                            boolean doApplyXMLBase = true;
                            if (iPath.startsWith("manifest.manifest"))
                            {
                                // first determine if XML Base exists in the manifest
                                if (doesXMLBaseExist())
                                {
                                    // XML base does exist, we are at the submanifest
                                    // must set flag to throw warning
                                    doApplyXMLBase = false;
                                }
                            }

                            // apply xml:base on href value before href checks only
                            // when the doApplyXMLBase flag is set to true.

                            if (doApplyXMLBase)
                            {
                                mLogger.finest("APPLYING XML BASE");
                                myAttributeValue = applyXMLBase(myAttributeValue);
                            }

                            if (myAttributeValue.indexOf('\\') != -1)
                            {
                                msgText = "A URI cannot contain a backwards slash " + "(\\) character. If this character is required "
                                        + "in the following URI \"" + myAttributeValue + "\" it must be properly escaped.";

                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                result &= false;
                            }

                            result = checkHref(myAttributeValue, doApplyXMLBase) && result;

                        }
                    }
                    else if (dataType.equalsIgnoreCase("vocabulary"))
                    {
                        // This is a VOCAB data type
                        // retrieve the vocab rule values and check against the
                        // vocab values that exist within the test subject

                        msgText = "Testing attribute \"" + currentNodeName + "\" for valid vocabulary";
                        mLogger.info("INFO: " + msgText);

                        // check for deprecated adlcp:persistState attribute on a
                        // <resource> element
                        if (currentNodeName.equals("persistState"))
                        {
                            String nodeName = iTestSubjectNode.getLocalName();
                            if (nodeName.equals("resource"))
                            {
                                msgText = "The \"adlcp:" + currentNodeName + "\" attribute has been deprecated.  This"
                                        + " attribute may result in undefined behavior.";
                                mLogger.info("WARNING: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                            }
                        }

                        Vector vocabAttribValues = mRulesValidator.getAttribVocabRuleValues(parentNodeName, iPath, currentNodeName);
                        // we are assuming that only 1 vocabulary value may
                        // exist for an attribute

                        result = checkVocabulary(currentNodeName, attributeValue, vocabAttribValues, true) && result;
                    }
                    else if (dataType.equalsIgnoreCase("text"))
                    {
                        //This is a TEXT data type
                        // check the attributes for smallest permitted maximum
                        // (spm) conformance.

                        result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;
                    }
                }
            } //done with attributes

            // Test the child Nodes

            NodeList children = iTestSubjectNode.getChildNodes();

            if (children != null)
            {
                int numChildren = children.getLength();

                // update the path for this child element

                String path;

                if (iPath.equals(""))
                {
                    // the node is a DOCUMENT or a root <manifest>
                    path = parentNodeName;
                }
                else if ((iPath.equals("manifest.manifest")) && (parentNodeName.equals("manifest")))
                {
                    path = iPath;
                }
                else if (parentNodeName.equalsIgnoreCase("item"))
                {
                    // the Node is an <imscp:item>
                    if (iPath.equals("manifest.organizations.organization.item"))
                    {
                        path = iPath;
                    }
                    else
                    {
                        path = iPath + "." + parentNodeName;
                    }
                }
                else
                {
                    path = iPath + "." + parentNodeName;
                }

                // SPECIAL CASE: check for mandatory elements

                if (parentNodeName.equalsIgnoreCase("manifest"))
                {

                    // check for mandatory metadata element at package level
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "metadata");
                    if (multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <metadata> could " + "not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false && result;
                    }
                    else
                    // check for mandatory children
                    {
                        Node caMetadataNode = DOMTreeUtility.getNode(iTestSubjectNode, "metadata");

                        // check for mandatory <imscp:schema> element
                        multiplicityUsed = getMultiplicityUsed(caMetadataNode, "schema");

                        if (multiplicityUsed < 1)
                        {
                            msgText = "Mandatory element <schema> could " + "not be found";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false && result;
                        }

                        // check for mandatory <imscp:schemaversion> element
                        multiplicityUsed = getMultiplicityUsed(caMetadataNode, "schemaversion");

                        if (multiplicityUsed < 1)
                        {
                            msgText = "Mandatory element <schemaversion> could " + "not be found";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false && result;
                        }
                    }

                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "organizations");
                    if (multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <organizations> could " + "not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false && result;
                    }

                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "resources");
                    if (multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <resources> could " + "not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false && result;
                    }
                }
                else if (parentNodeName.equalsIgnoreCase("organizations")
                        && (mRulesValidator.getApplicationProfile()).equals("contentaggregation"))
                {
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "organization");

                    // have special cases of multiplicity of organization when
                    // submanifest referencing is involved
                    if (path.startsWith("manifest.manifest"))
                    {

                        // have special cases of multiplicity of organization when
                        // submanifest referencing is involved
                        result = checkOrganizationMultiplicity(iTestSubjectNode, mManifestInfo) && result;

                    }
                    else
                    // don't need special case, not dealing with submanifest
                    {
                        if (multiplicityUsed < 1)
                        {
                            msgText = "Mandatory element <organization> could " + "not be found";

                            mLogger.info("FAILED: " + msgText);

                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                            result = false && result;

                        }
                    }

                }
                // have to check to ensure that empty organizations exist
                // for resource package

                else if (parentNodeName.equalsIgnoreCase("organizations")
                        && (mRulesValidator.getApplicationProfile()).equals("resource"))
                {
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "organization");
                    if (multiplicityUsed > 0)
                    {
                        msgText = "The <organizations> element must contain no  " + "children in a resource package";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false && result;
                    }
                }
                else if (parentNodeName.equalsIgnoreCase("organization")
                        && (mRulesValidator.getApplicationProfile()).equals("contentaggregation"))
                {
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "title");
                    if (multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <title> could " + "not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false && result;
                    }

                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "item");
                    if (multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <item> could " + "not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false && result;
                    }

                    // special checks for item
                    result = checkItem(iTestSubjectNode, mManifestInfo) && result;

                }

                for (int z = 0; z < numChildren; z++)
                {

                    Node currentChild = children.item(z);
                    String currentChildName = currentChild.getLocalName();

                    msgText = "Currentchild is " + currentChildName + " and path is " + path;

                    mLogger.info(msgText);

                    if (currentChildName != null)
                    {

                        if (((currentChildName.equals("timeLimitAction")) || (currentChildName.equals("dataFromLMS"))
                                || (currentChildName.equals("completionThreshold")) || (currentChildName.equals("presentation")))
                                && (!parentNodeName.equals("item")))
                        {
                            result = false && result;

                            msgText = "<" + currentChildName + "> can only " + "exist as a child of an <item> element";

                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        }

                        if (((currentChildName.equals("constrainedChoiceConsiderations")) || (currentChildName
                                .equals("rollupConsiderations")))
                                && (!parentNodeName.equals("sequencing")))
                        {

                            result = false && result;

                            msgText = "<" + currentChildName + "> can only " + "exist as a child of an <sequencing> element";

                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        }

                        // must enforce that the adlcp:location exist
                        // as a child of metadata only - warning for best practice.

                        if ((currentChildName.equals("location")) && (!parentNodeName.equals("metadata")))
                        {

                            result = false && result;

                            msgText = "<" + currentChildName + "> can only " + "exist as a child of an <metadata> element";

                            mLogger.info("WARNING: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                        }

                        if ((currentChildName.equals("sequencing")) && (!parentNodeName.equals("item"))
                                && (!parentNodeName.equals("organization")))
                        {

                            result = false && result;

                            msgText = "<" + currentChildName + "> can only " + "exist as a child of an <item > or an "
                                    + "<organization> element";

                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        }

                        dataType = mRulesValidator.getRuleValue(currentChildName, path, "datatype");

                        // we do not want to test for extensions here

                        if (dataType.equalsIgnoreCase("parent") || dataType.equalsIgnoreCase("vocabulary")
                                || dataType.equalsIgnoreCase("text") || dataType.equalsIgnoreCase("sequencing")
                                || dataType.equalsIgnoreCase("metadata"))
                        {
                            msgText = "Testing element <" + currentChildName + "> for minimum conformance";

                            mLogger.info("INFO: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));

                            // only want to test multiplicity of organization in
                            // the root manifest.  Special cases with useage
                            // of (Sub) manifest
                            if ((!currentChildName.equals("organization")) && (!path.startsWith("manifest.manifest")))
                            {
                                multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, currentChildName);

                                //get the min rule and convert to an int

                                minRule = Integer.parseInt(mRulesValidator.getRuleValue(currentChildName, path, "min"));

                                //get the max rule and convert to an int

                                maxRule = Integer.parseInt(mRulesValidator.getRuleValue(currentChildName, path, "max"));

                                if ((minRule != -1) && (maxRule != -1))
                                {
                                    if (multiplicityUsed >= minRule && multiplicityUsed <= maxRule)
                                    {
                                        msgText = "Multiplicity for element <" + currentChildName + "> has been verified";
                                        mLogger.info("PASSED: " + msgText);
                                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                                    }
                                    else
                                    {
                                        msgText = "The <" + currentChildName + "> element is not within the " + "multiplicity bounds.";
                                        mLogger.info("FAILED: " + msgText);
                                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                        result = false && result;
                                    }
                                }
                                else if ((minRule != -1) && (maxRule == -1))
                                {
                                    if (multiplicityUsed >= minRule)
                                    {
                                        msgText = "Multiplicity for element <" + currentChildName + "> has been verified";
                                        mLogger.info("PASSED: " + msgText);
                                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                                    }
                                    else
                                    {
                                        msgText = "The <" + currentChildName + "> element is not within the " + "multiplicity bounds.";
                                        mLogger.info("FAILED: " + msgText);
                                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                        result = false && result;
                                    }
                                }
                            }
                            // test for each particular data type

                            if (dataType.equalsIgnoreCase("parent"))
                            {
                                //This is a parent element

                                // if this is a sub-manifest, track it's identifiers

                                if (currentChildName.equalsIgnoreCase("manifest"))
                                {
                                    trackOrgIdentifiers(currentChild);
                                }

                                result = compareToRules(currentChild, path) && result;

                            }
                            else if (dataType.equalsIgnoreCase("sequencing"))
                            {
                                // This is a sequencing data type

                                SequenceValidator sequenceValidator = new SequenceValidator(mEnvironmentVariable);

                                result = sequenceValidator.validate(currentChild) && result;
                            }
                            else if (dataType.equalsIgnoreCase("metadata"))
                            {
                                // This is a metadata data type - no longer need to
                                // check for lom and location to coexist
                                // must detect that the metadata exists in location
                                if (currentChildName.equals("location"))
                                {
                                    String currentLocationValue = mRulesValidator.getTaggedData(currentChild);

                                    // check to ensure there are no leading slashes
                                    result = checkForSlashes("location", currentLocationValue) && result;

                                    if (!iPath.startsWith("manifest.manifest"))
                                    {
                                        currentLocationValue = applyXMLBase(currentLocationValue);
                                    }

                                    result = result && checkHref(currentLocationValue, true);
                                }
                            }
                            else if (dataType.equalsIgnoreCase("text"))
                            {
                                // This is a text data type
                                // check spm

                                // first must retrieve the value of this child element

                                String currentChildValue = mRulesValidator.getTaggedData(currentChild);

                                //get the spm rule and convert to an int

                                spmRule = Integer.parseInt(mRulesValidator.getRuleValue(currentChildName, path, "spm"));

                                result = checkSPMConformance(currentChildName, currentChildValue, spmRule, false) && result;
                            }
                            else if (dataType.equalsIgnoreCase("vocabulary"))
                            {
                                // This is a vocabulary data type
                                // more than one vocabulary token may exist

                                msgText = "Testing element \"" + currentChildName + "\" for valid vocabulary";
                                mLogger.info("INFO: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));

                                // retrieve the value of this element

                                String currentChildValue = mRulesValidator.getTaggedData(currentChild);

                                Vector vocabValues = mRulesValidator.getVocabRuleValues(currentChildName, path);

                                result = checkVocabulary(currentChildName, currentChildValue, vocabValues, false) && result;
                            }
                            else if (dataType.equalsIgnoreCase("decimal"))
                            {
                                // This is a decimal data type
                                // only adlcp:completionThreshold is of this type
                                // and currently all checks are enforced by the schema.
                                // no additional checks needed at this time.
                            }
                        }
                    }
                }

            }
            // remove the xml:base value for this particular element

            if (parentNodeName.equals("manifest"))
            {
                mXMLBase[0][1] = "";
            }
            else if (parentNodeName.equals("resources"))
            {
                mXMLBase[1][1] = "";
            }
            else if (parentNodeName.equals("resource"))
            {
                mXMLBase[2][1] = "";
            }

            break;
        }

        // handle entity reference nodes
        case Node.ENTITY_REFERENCE_NODE:
        {
            break;
        }

        // text
        case Node.COMMENT_NODE:
        {
            break;
        }

        case Node.CDATA_SECTION_NODE:
        {
            break;
        }

        case Node.TEXT_NODE:
        {
            break;
        }
        }// end switch statement

        mLogger.exiting("CPValidator", "compareToRules()");
        return result;
    }

    /**
     *
     * This method assists with the application profile check for the validation
     * with the inclusion of (Sub) manifest elements. This method validates the
     * mutliplicity of the <organization> element based on the way the manifest
     * was referenced by an item identifierref.  If an item identifierref points
     * to a (Sub) manifest identifier value, than the (Sub) manifest shall
     * contain 1 and only 1 organization element in the (Sub) manifest.  If an
     * item identfiierref value points to only a resource identifier in a
     * (Sub) manifest, the the (Sub) manifest shall contain 0 or 1 organization
     * element. <br>
     *
     * @param iOrgsNode The organizations node<br>
     *
     * @param iManifestInfo - the populated ManifestMap object
     *
     * @return boolean - result of the organization multiplicity check.
     * True if the href checks passed, false otherwise.<br>
     */
    private boolean checkOrganizationMultiplicity(Node iOrgsNode, ManifestMap iManifestInfo)
    {
        mLogger.entering("CPValidator", "checkOrganizationMultiplicity");
        int multiplicityUsed = -1;
        boolean result = true;
        String msgText;

        msgText = "mManifestID is " + mManifestID;
        mLogger.info(msgText);

        if (!mManifestID.equals(""))
        {
            // search through manifestMaps to find a match to the manifestID
            if (mManifestID.equals(iManifestInfo.getManifestId()))
            {
                // we are dealing with a root manifest, multiplicity applies to the
                // content aggregation rules as documented in the SCORM
                multiplicityUsed = getMultiplicityUsed(iOrgsNode, "organization");

                if (multiplicityUsed < 1)
                {
                    msgText = "Mandatory element <organization> could " + "not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                    result = result && false;
                }
                else
                {
                    msgText = "Multiplicity for  element <organization> has " + "been verified";
                    mLogger.info("PASSED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                }
            }
            else
            {
                // search through manifestMap Vector to find mManifestId
                if (!iManifestInfo.getManifestMaps().isEmpty())
                {
                    for (int j = 0; j < iManifestInfo.getManifestMaps().size(); j++)
                    {
                        ManifestMap currentMap = (ManifestMap) iManifestInfo.getManifestMaps().elementAt(j);

                        if (mManifestID.equals(currentMap.getManifestId()))
                        {
                            String profile = currentMap.getApplicationProfile();

                            if (profile.equals("other"))
                            {
                                // we are dealing with a special case where an idref
                                // points to a resource within the manifest,
                                // multiplicity in this case is 0 or 1

                                multiplicityUsed = getMultiplicityUsed(iOrgsNode, "organization");

                                if ((multiplicityUsed >= 0) && (multiplicityUsed <= 1))
                                {
                                    msgText = "Mutiplicity for element <organization> " + "has been verified";
                                    mLogger.info("PASSED: " + msgText);
                                    MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));

                                    result = result && true;
                                    break;
                                }
                                else
                                {
                                    msgText = " Referencing a <resource> only within a" + "  (Sub) manifests must have"
                                            + " 0 or 1 <organization> element";
                                    mLogger.info("FAILED: " + msgText);
                                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                                    result = result && false;
                                    break;

                                }
                            }
                            else if (profile.equals("contentaggregation"))
                            {
                                // we are dealing with a special case where an idref
                                // points to a (Sub) manifest identifier,
                                // multiplicity in this case is 1 and only 1

                                multiplicityUsed = getMultiplicityUsed(iOrgsNode, "organization");

                                if (multiplicityUsed >= 1 && multiplicityUsed <= 1)
                                {
                                    msgText = "Multiplicity for element <organization> " + " has been verified.  1 and only 1 may "
                                            + "exist in this case";
                                    mLogger.info("PASSED: " + msgText);
                                    MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                                    result = result && true;
                                    break;
                                }
                                else
                                {
                                    msgText = "Referenced (Sub) manifests must have " + "1 and only 1 <organization> element";
                                    mLogger.info("FAILED: " + msgText);
                                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                                    result = result && false;
                                    break;
                                }

                            }
                        }
                        else
                        {
                            // recursively call with the next ManifestMap object
                            checkOrganizationMultiplicity(iOrgsNode, currentMap);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     * This method assists with the application profile check for the validation
     * of the resource attributes. <br>
     *
     * @param iResourceNode The resources node<br>
     *
     * @param iManifestInfo - the populated ManifestMap object
     *
     * @return boolean - result of the xxx check.
     * True if the xxx checks passed, false otherwise.<br>
     */
    private boolean checkResourceAttributes(Node iResourceNode, NamedNodeMap iAttrList)
    {
        mLogger.entering("CPValidator", "checkResourceAttributes");

        int idMultiplicityUsed = -1;
        int typeMultiplicityUsed = -1;
        int scormMultiplicityUsed = -1;
        int hrefMultiplicityUsed = -1;

        String msgText;
        boolean result = true;

        // check for mandatory attributes

        idMultiplicityUsed = getMultiplicityUsed(iAttrList, "identifier");
        if (idMultiplicityUsed < 1)
        {
            msgText = "Mandatory attribute \"identifier\" could " + "not be found";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false && result;
        }

        typeMultiplicityUsed = getMultiplicityUsed(iAttrList, "type");

        if (typeMultiplicityUsed < 1)
        {
            msgText = "Mandatory attribute \"type\" could " + "not be found";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false && result;
        }

        scormMultiplicityUsed = getMultiplicityUsed(iAttrList, "scormType");
        if (scormMultiplicityUsed < 1)
        {
            msgText = "Mandatory attribute \"scormType\" could " + "not be found";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

            result = false && result;
        }

        // special rules only apply to content aggregation application profile
        if (mRulesValidator.getApplicationProfile().equals("contentaggregation"))
        {
            // special checks to be enforced when an <item> references a <resource>

            // retrieve resource.identifier value and compare to idref values in the
            // valid mValidIdrefs vector.  If a match is found, it is assumed that
            // the special checks should be enforced.

            String resourceID = DOMTreeUtility.getAttributeValue(iResourceNode, "identifier");
            boolean referencesAResource = false;

            if (!resourceID.equals(""))
            {
                // loop through mValidIdrefs to find a matching reference

                for (int i = 0; i < mValidIdrefs.size(); i++)
                {
                    String currentIdref = (String) mValidIdrefs.elementAt(i);

                    if (resourceID.equals(currentIdref))
                    {
                        referencesAResource = true;
                    }
                }

                if (referencesAResource)
                {
                    // (1) href is mandatory when referenced by an item

                    hrefMultiplicityUsed = getMultiplicityUsed(iAttrList, "href");

                    if (hrefMultiplicityUsed < 1)
                    {
                        msgText = "Mandatory attribute \"href\" could " + "not be found on a <resource> that is referenced"
                                + " by an <item>";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                        result = false && result;
                    }

                    // (2) type attribute shall be set to "webcontent" when
                    // referenced by an item

                    String typeValue = DOMTreeUtility.getAttributeValue(iResourceNode, "type");

                    if (!typeValue.equals("webcontent"))
                    {
                        msgText = "The type attribute shall be set to " + "  \"webcontent\" when an <item> references "
                                + "the <resource>";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                        result = false && result;
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     * This method assists with the application profile check for the validation
     * with of items.  It first checks the identifierref attribute of an item
     * for existance on non-leaf items. It than checks the title multiplcity,
     * as a title is not permitted on an item that references a (sub)manifest.
     * <br>
     *
     * @param iOrgsNode The organizations node<br>
     * @param iManifestInfo - the populated ManifestMap object
     *
     * @return boolean - result of the organization multiplicity check.
     * True if the href checks passed, false otherwise.<br>
     */
    private boolean checkItem(Node iOrgNode, ManifestMap iManifestInfo)
    {
        mLogger.entering("CPValidator", "checkItem");
        int multiplicityUsed = -1;
        boolean result = true;
        String msgText;

        result = checkItemIdentifierRef(iOrgNode) && result;
        result = checkItemChildMultiplicity(iOrgNode, iManifestInfo) && result;

        return result;
    }

    /**
     *
     * This method assists with the application profile checking.  It determines
     * whether or not a resource is refernced by an item.<br>
     *
     * @param idrefValue - the idrentifierref value to be matched to a resource.
     * <br>
     *
     * @return boolean - result of the reference to a resource.<br>
     *
     */
    private boolean checkForReferenceToScoOrAsset(String idrefValue)
    {
        mLogger.entering("CPValidator", "checkForReferenceToScoOrAsset");

        boolean result = false;
        int len = mResourceNodes.size();
        String id;
        String type;
        String msgText;

        for (int i = 0; i < len; i++)
        {
            Node currentResource = (Node) mResourceNodes.elementAt(i);
            id = DOMTreeUtility.getAttributeValue(currentResource, "identifier");

            if (id.equals(idrefValue))
            {
                result = true;
            }
        }
        return result;
    }

    /**
     *
     * This method assists with the application profile check for validation that
     * a sco resource is referenced by an item identifierref.<br>
     *
     * @param idrefValue - the idrentifierref value to be matched to a sco
     * resource.* <br>
     *
     * @return boolean - result of the reference to sco check.<br>
     *
     */
    private boolean checkForReferenceToSco(String idrefValue)
    {
        mLogger.entering("CPValidator", "checkForReferenceToSco");
        mLogger.finest("Input Identifierref: " + idrefValue);

        boolean result = true;
        int len = mResourceNodes.size();
        String id;
        String type;
        String msgText;

        for (int i = 0; i < len; i++)
        {
            Node currentResource = (Node) mResourceNodes.elementAt(i);
            id = DOMTreeUtility.getAttributeValue(currentResource, "identifier");
            mLogger.finest("Identifier of <resource> #" + i + " is: " + id);

            if (id.equals(idrefValue))
            {
                // we have a matching reference
                // now check scormType and error if not sco

                type = DOMTreeUtility.getAttributeValue(currentResource, "scormType");

                mLogger.finest("SCORM Type of <resource> #" + i + " is: " + type);

                if (!type.equalsIgnoreCase("sco"))
                {
                    result = false;

                    msgText = "The <item> shall reference a SCO resource only for " + "ID/IDREF " + idrefValue;

                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                }
            }
        }
        return result;
    }

    /**
     *
     * This method checks the item to ensure that the identifierref attribute is
     * not used on a non-leaf item.<br>
     *
     * @param iOrgNode - the organization node containing the item element(s)<br>
     *
     * @return boolean - result of the check for the item identifierref attribute
     * True if the identifierref passes, false otherwise.<br>
     */
    private boolean checkItemIdentifierRef(Node iOrgNode)
    {

        mLogger.entering("CPValidator", "checkItemIdentifierRef");

        String msgText = new String();
        NodeList orgChildren = iOrgNode.getChildNodes();
        int orgChildSize = orgChildren.getLength();
        Node currentNode;
        String currentName;
        boolean result = true;

        for (int j = 0; j < orgChildSize; j++)
        {
            currentNode = orgChildren.item(j);
            currentName = currentNode.getLocalName();

            if (currentName.equals("item"))
            {
                NodeList itemChildren = currentNode.getChildNodes();
                int itemChildrenSize = itemChildren.getLength();

                for (int k = 0; k < itemChildrenSize; k++)
                {

                    // see if we have a child item of item
                    // if so, this signals that the currentNode is not a leaf and should
                    // not have an identifierref

                    Node currentItemChild = itemChildren.item(k);
                    String currentItemChildName = currentItemChild.getLocalName();

                    if (currentItemChildName != null)
                    {

                        if (currentItemChildName.equals("item"))
                        {

                            NamedNodeMap attrList = currentNode.getAttributes();
                            int numAttr = attrList.getLength();
                            Attr currentAttrNode = null;
                            String currentNodeName = "";

                            for (int i = 0; i < numAttr; i++)
                            {
                                currentAttrNode = (Attr) attrList.item(i);
                                currentNodeName = currentAttrNode.getLocalName();

                                if (currentNodeName.equals("identifierref"))
                                {
                                    result = result && false;
                                    msgText = "The \"identifierref\" attribute shall not " + "be used on a non-leaf <item> element";
                                    mLogger.info("FAILED: " + msgText);
                                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                                }

                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     * This method validates the multiplicity of the children of the item element.
     * If the item references a (Sub)manifest, than the title element and all
     * ADL,and IMSSS namespaced elements are not permitted.  If the item does not
     * reference a (sub)manifest, than the title is required to be present.<br>
     *
     * @param iNode - the item element<br>
     *
     * @param iManifestInfo - the populated ManifestMap object <br>
     *
     * @return boolean - result of the title multiplicity check.  True implies that
     * the title multiplicity was properly adhered to, false implies otherwise.<br>
     *
     */
    private boolean checkItemChildMultiplicity(Node iNode, ManifestMap iManifestInfo)
    {
        mLogger.entering("CPValidator", "checkItemChildMultiplicity");

        String msgText = new String();
        boolean result = true;

        String iNodeName = iNode.getLocalName();

        if (iNodeName.equals("organization"))
        {
            NodeList iNodeChildren = iNode.getChildNodes();
            int iNodeChildSize = iNodeChildren.getLength();

            Node currentNode;
            String currentName;

            for (int j = 0; j < iNodeChildSize; j++)
            {
                currentNode = iNodeChildren.item(j);
                currentName = currentNode.getLocalName();

                if (currentName.equals("item"))
                {
                    // search for item element and recurse
                    result = checkItemChildMultiplicity(currentNode, iManifestInfo) && result;

                }

            }
        }

        if (iNodeName.equals("item"))
        {
            boolean itemPointToSubmanifest = false;
            Vector subManifestIdrefs = iManifestInfo.getSubManifestIDrefs();
            int subIdrefSize = subManifestIdrefs.size();

            // check for the identifierref to determine if it points to
            // a (sub)manifest

            NamedNodeMap attrList = iNode.getAttributes();
            int numAttr = attrList.getLength();
            Attr currentAttrNode = null;
            String currentNodeName = "";

            for (int i = 0; i < numAttr; i++)
            {
                currentAttrNode = (Attr) attrList.item(i);
                currentNodeName = currentAttrNode.getLocalName();

                if (currentNodeName.equals("identifierref"))
                {
                    String identifierrefValue = currentAttrNode.getValue();

                    // loop thru subManfiestIdrefs to determine if it points to a
                    // subManifest.  If it does, title is not permitted to exist
                    // if it does not, title is mandatory.
                    String currentIdref;
                    for (int k = 0; k < subIdrefSize; k++)
                    {
                        currentIdref = (String) subManifestIdrefs.elementAt(k);

                        if (currentIdref.equals(identifierrefValue))
                        {
                            // we are pointing to a submanifest here
                            itemPointToSubmanifest = true;
                        }

                    }
                }
            }

            NodeList itemChild = iNode.getChildNodes();
            int itemSize = itemChild.getLength();
            String currentItemChildName = new String();

            boolean titleFound = false;
            boolean timeLimitActionFound = false;
            boolean dataFromLMSFound = false;
            boolean presentationFound = false;
            boolean sequencingFound = false;
            boolean thresholdFound = false;

            for (int v = 0; v < itemSize; v++)
            {
                Node currentItemChild = itemChild.item(v);
                currentItemChildName = currentItemChild.getLocalName();

                if (currentItemChildName != null)
                {
                    if (currentItemChildName.equals("title"))
                    {
                        titleFound = true;
                    }
                    if (currentItemChildName.equals("timeLimitAction"))
                    {
                        timeLimitActionFound = true;

                    }
                    if (currentItemChildName.equals("dataFromLMS"))
                    {
                        dataFromLMSFound = true;

                    }
                    if (currentItemChildName.equals("presentation"))
                    {
                        presentationFound = true;

                    }
                    if (currentItemChildName.equals("sequencing"))
                    {
                        sequencingFound = true;

                    }
                    if (currentItemChildName.equals("completionThreshold"))
                    {
                        thresholdFound = true;
                    }
                    if (currentItemChildName.equals("item"))
                    {
                        result = checkItemChildMultiplicity(currentItemChild, iManifestInfo) && result;
                    }
                }
            }

            if (itemPointToSubmanifest)
            {
                // we are referencing a (sub)manifest, so title, adlcp, imsss, and
                // adlnav elements not permitted
                if (titleFound)
                {
                    msgText = "Element <title> is not permitted on a leaf item that " + "references a (sub)manifest";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                    result = false && result;
                }
                if (timeLimitActionFound || dataFromLMSFound || presentationFound || thresholdFound)
                {
                    msgText = "ADL Content Packaging namespaced elements are not " + "permitted on a leaf item that references a "
                            + "(sub)manifest";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                    result = false && result;
                }

                if (sequencingFound)
                {

                    msgText = "Sequencing information can not be defined on a leaf " + "item that references a (sub)manifest";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                    result = false && result;
                }

            }
            else
            {
                // we are not referencing a (sub)manifest here, so title is mandatory
                if (!titleFound)
                {
                    msgText = "Mandatory element <title> could not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                    result = false && result;
                }
            }
        }

        return result;
    }

    /**
     * This method assists with the application profile check for the validation
     * of the href attribute(s). This method attempts to verify that the href
     * values can be detected.<br>
     *
     * @param iURIString The URI value of the href attribute<br>
     *
     * @return boolean - result of the href check. True if the href checks
     * passed, false otherwise.<br>
     *
     */
    private boolean checkHref(String iURIString, boolean doApplyXMLBase)
    {
        mLogger.entering("CPValidator", "checkHref()");
        mLogger.info("iURISting is " + iURIString);

        boolean result = true;
        String msgText = "";

        if (!(iURIString.equals("")))
        {
            // check for a valid protocol

            StringTokenizer token = new StringTokenizer(iURIString, ":");
            //int numTokens = token.countTokens();
            //String protocol = token.nextToken();

            if (iURIString.substring(0, 5).equals("http:") || iURIString.substring(0, 6).equals("https:")
                    || iURIString.substring(0, 4).equals("ftp:") || iURIString.substring(0, 5).equals("ftps:"))
            {
                // This is an external SCO
                try
                {
                    URL url = new URL(iURIString);
                    URLConnection urlConn = url.openConnection();
                    HttpURLConnection httpUrlConn = (HttpURLConnection) urlConn;
                    int code = httpUrlConn.getResponseCode();

                    // try to access the address

                    if (code == 200)
                    {
                        msgText = "URL \"" + iURIString + "\" has been detected";
                        mLogger.info("PASSED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                    }
                    else
                    {
                        if (doApplyXMLBase)
                        {
                            msgText = "URL  \"" + iURIString + "\" could not be detected";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false;
                        }
                        else
                        {
                            // throw a warning if dealing with (Sub) manifest
                            // file detection
                            msgText = "URL  \"" + iURIString + "\" will not be " + "detected due to unclairity in the "
                                    + "SCORM of applying xml:base within " + "(Sub) manifest elements";
                            mLogger.info("WARNING: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                        }
                    }
                }
                catch (MalformedURLException mfue)
                {
                    mLogger.fine("MalformedURLException thrown when creating " + "URL with \"" + iURIString + "\"");

                    msgText = "URL  \"" + iURIString + "\" is malformed";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
                catch (IOException ioe)
                {
                    mLogger.fine("IOException thrown when opening a connection " + "to \"" + iURIString + "\"");

                    msgText = "Could not open a URL connection to  \"" + iURIString + "\"";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            }
            else if (iURIString.substring(0, 5).equals("file:"))
            {
                // This is the local file system

                msgText = "File  \"" + iURIString + "\" is referenced to the " + "local file system.  The \"href\" attribute must "
                        + "reference a file that is local to the package or " + "reference an external file";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            }
            else
            {
                // Check the local URLs

                if (iURIString.charAt(0) == '/')
                {
                    // This is referencing the users home directory

                    msgText = "File  \"" + iURIString + "\" is referenced to the "
                            + "users home directory.  The \"href\" attribute must "
                            + "reference a file that is local to the package or " + "reference an external file";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
                else
                {

                    String absolutePath = getBaseDirectory() + iURIString;

                    mLogger.info("Absolute path is " + absolutePath);

                    // strip off the query string and parameters

                    int queryIndex = absolutePath.indexOf('?');

                    if (queryIndex > 0)
                    {
                        absolutePath = absolutePath.substring(0, queryIndex);
                    }

                    // strip off the fragment string and parameters

                    int fragmentIndex = absolutePath.indexOf('#');

                    if (fragmentIndex > 0)
                    {
                        absolutePath = absolutePath.substring(0, fragmentIndex);
                    }

                    // decode any encrypted URL syntax

                    URLDecoder urlDecoder = new URLDecoder();

                    try
                    {
                        absolutePath = urlDecoder.decode(absolutePath, "UTF-8");
                    }
                    catch (UnsupportedEncodingException uee)
                    {
                        mLogger.severe("UnsupportedEncodingException thrown while " + "decoding the file path.");
                        uee.printStackTrace();
                    }

                    // try to access the file

                    try
                    {
                        File fileToFind = new File(absolutePath);

                        if (fileToFind.isFile())
                        {
                            msgText = "File \"" + iURIString + "\" has been detected";
                            mLogger.info("PASSED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                        }
                        else
                        {
                            if (doApplyXMLBase)
                            {
                                msgText = "File  \"" + iURIString + "\" could not be detected";
                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                result = false;
                            }
                            else
                            {
                                // throw a warning if dealing with (Sub) manifest
                                // file detection
                                msgText = "File  \"" + iURIString + "\" will not be " + "detected.  At this time, SCORM does not "
                                        + "impose requirements on how to handle " + "xml:base in conjunction with (Sub) manifest "
                                        + "elements";
                                mLogger.info("WARNING: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                            }
                        }
                    }
                    catch (NullPointerException npe)
                    {
                        mLogger.severe("NullPointerException thrown when accessing " + absolutePath);
                        msgText = "File \"" + iURIString + "\" could not be detected";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                    catch (SecurityException se)
                    {
                        mLogger.severe("SecurityException thrown when accessing " + absolutePath);
                        msgText = "File \"" + iURIString + "\" could not be detected";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    /**
     * This method assists with the application profile check of the smallest
     * permitted maximums.  The smallest permitted maximum value of an element
     * describes the maximum number of characters that a system that is going to
     * process that data at a minimum must support. <br>
     *
     * @param iElementName
     *                Name of the element being checked for spm<br>
     * @param iElementValue
     *                value being checked for smp<br>
     * @param iSPMRule
     *               value allowed for spm ( value retrieved from rules )<br>
     * @param iAmAnAttribute
     *               flags determines if its an attribute (true), or an
     *               element that is being validated for valid vocabulary
     *               tokens.<br>
     *
     * @return - boolean result of spm check.  True if the spm checks passed,
     *           false otherwise.<br>
     */
    private boolean checkSPMConformance(String iElementName, String iElementValue, int iSPMRule, boolean iAmAnAttribute)
    {
        boolean result = true;
        String msgText = new String();

        int elementValueLength = iElementValue.length();

        if (iSPMRule != -1)
        {
            if (elementValueLength > iSPMRule)
            {
                if (iAmAnAttribute)
                {
                    msgText = "The text contained in " + "attribute \"" + iElementName + "\" is greater than " + iSPMRule + ".";
                }
                else
                {
                    msgText = "The text contained in " + "element <" + iElementName + "> is greater than " + iSPMRule + ".";
                }
                mLogger.info("WARNING: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
            }
            else if (elementValueLength < 1)
            {
                if (iAmAnAttribute)
                {
                    msgText = "No text was found in attribute \"" + iElementName + "\" and fails the minimum character length test";
                }
                else
                {
                    msgText = "No text was found in element <" + iElementName + "> and fails the minimum character length test";
                }
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

                result = false;
            }
            else
            {
                if (iAmAnAttribute)
                {
                    msgText = "Character length for attribute \"" + iElementName + "\" has been verified";
                }
                else
                {
                    msgText = "Character length for element <" + iElementName + "> has been verified";
                }
                mLogger.info("PASSED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            }
        }
        else if (elementValueLength < 1)
        {
            if (iAmAnAttribute)
            {
                msgText = "No text was found in attribute \"" + iElementName + "\" and fails the minimum character length test";
            }
            else
            {
                msgText = "No text was found in element <" + iElementName + "> and fails the minimum character length test";
            }
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));

            result = false;
        }
        else
        {
            if (iAmAnAttribute)
            {
                msgText = "Character length for attribute \"" + iElementName + "\" has been verified";
            }
            else
            {
                msgText = "Character length for element <" + iElementName + "> has been verified";
            }
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        }

        return result;
    }

    /**
     * This method assists with the application profile check for valid
     * vocabularies.  The vocabulary value is compared to those defined by the
     * application profile rules. It is assumed that only 1 vocabulary token may
     * exist for an element/attribute.<br>
     *
     * @parm iName
     *                Name of the element/attribute being checked
     *                for valid vocabulary.<br>
     * @param iValue
     *               Vocabulary string value that exists for the
     *               element/attribute in the test subject<br>
     * @param vocabValues
     *               Vector containing a list of the valid vocabulary values
     *               for the element/attribute.<br>
     *
     * @param iAmAnAttribute
     *               flags determines if its an attribute (true), or an
     *               element that is being validated for valid vocabulary
     *               tokens.<br>
     *
     * @return     true if the value is a valid vocab token, false otherwise<br>
     */
    private boolean checkVocabulary(String iName, String iValue, Vector iVocabValues, boolean iAmAnAttribute)
    {
        mLogger.entering("CPValidator", "checkVocabulary()");

        boolean result = false;
        String msgText;

        // loop through the valid vocabulary vector to see if the
        // attribute value matches a valid token

        for (int i = 0; i < iVocabValues.size(); i++)
        {
            if (iValue.equals((String) iVocabValues.elementAt(i)))
            {
                result = true;
            }
        }

        if (result)
        {
            if (iAmAnAttribute)
            {
                msgText = "\"" + iValue + "\"  is a valid value for attribute \"" + iName + "\"";

            }
            else
            {
                msgText = "\"" + iValue + "\"  is a valid value for element <" + iName + ">";
            }

            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));

        }
        else
        {
            if (iAmAnAttribute)
            {
                msgText = "\"" + iValue + "\"  is not a valid value for " + "attribute \"" + iName + "\"";
            }
            else
            {
                msgText = "\"" + iValue + "\"  is not a valid value for element <" + iName + ">";
            }

            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
        }
        mLogger.exiting("CPValidator", "checkVocabulary()");

        return result;
    }

    /**
     * This method assists with the application profile check for the
     * multiplicity of elements.  This method returns the number of times the
     * element was detected based on the given element name and the given
     * parent node of that element name.<br>
     *
     * @param iParentNode The parent node of the element being searched<br>
     * @param iNodeName The name of the element being searched for<br>
     * @param iNamespace The namespace of the element being searched for<br>
     *
     * @return - int: number of instances of a given element<br>
     */
    public int getMultiplicityUsed(Node iParentNode, String iNodeName)
    {
        mLogger.entering("CPValidator", "getMultiplicityUsed() - Elements");
        mLogger.finest("Input Parent Node: " + iParentNode.getLocalName());
        mLogger.finest("Input Node we are looking for: " + iNodeName);

        // Need a list to find how many kids to cycle through
        NodeList kids = iParentNode.getChildNodes();
        int count = 0;

        int kidsLength = kids.getLength();
        for (int i = 0; i < kidsLength; i++)
        {
            if (kids.item(i).getNodeType() == Node.ELEMENT_NODE)
            {
                String currentNodeName = kids.item(i).getLocalName();
                //String  currentNodeNamespace = kids.item(i).getNamespaceURI();

                if (currentNodeName.equals(iNodeName))
                {
                    count++;
                } // end if the current node name equals the name we are looking for
            } // end of the node type is ELEMENT_NODE
        } // end looping over children

        mLogger.finest("The " + iNodeName + ", appeared " + count + " times.");

        return count;
    }

    /**
     * This method assists with the application profile check for the
     * multiplicity of attributes.  This method returns the number of times the
     * attribue was detected based on the given attribute name and the given
     * parent node of that element name.<br>
     *
     * @param iAttributeMap A list of attributes<br>
     * @param iNodeName The name of the element being searched for<br>
     * @param iNamespace The namespace of the element being searched for<br>
     *
     * @return - int: number of instances of a given attribute<br>
     */
    public int getMultiplicityUsed(NamedNodeMap iAttributeMap, String iNodeName)
    {
        mLogger.entering("CPValidator", "getMultiplicityUsed() - Attributes");
        mLogger.finest("Input Node we are looking for: " + iNodeName);

        int result = 0;
        int length = iAttributeMap.getLength();
        String currentName;
        String namespace;

        for (int i = 0; i < length; i++)
        {
            currentName = ((Attr) iAttributeMap.item(i)).getLocalName();

            if (currentName.equals(iNodeName))
            {

                result++;

            } // end if current name equals node name
        } // end looping over attributes

        mLogger.finest("The " + iNodeName + ", appeared " + result + " times.");

        return result;
    }

    /**
     * Returns the mBaseDirectory attribute that contains the base directory of
     * where the test subject is located. It is used by the validator to
     * determine the location of the package resources, including the
     * imsmanifest, sco's, and/or metadata.
     *
     * @return
     *   String that contains the path of the base directory<br>
     */
    private String getBaseDirectory()
    {

        return mBaseDirectory;
    }

    /**
     * This method determines if xml:base is declared in the IMS Manifest.<br>
     *
     * @return boolean describing if xml:base is declared.  True implies that
     * xml:base does exist within the IMS Manifest, false implies that xml:base
     * was not declared in the IMS Manifest.<br>
     *
     */
    private boolean doesXMLBaseExist()
    {
        boolean xmlBaseExists = true;

        // determine if the xml:base array contains values
        if ((mXMLBase[0][1].equals("")) && (mXMLBase[1][1].equals("")) && (mXMLBase[2][1].equals("")))
        {
            // xml:base was not declared for a manifest, resources, or resource
            // element
            xmlBaseExists = false;
        }

        return xmlBaseExists;
    }

    /**
     * This method builds the XML base value that is to be pre-appended to
     * the href attribute values prior to any attempts to located the href values
     * locations.<br>
     *
     * @param String href value to apply xml:base values to<br>
     *
     * @return String that contains the href value pre-appended with the xml:base
     */
    private String applyXMLBase(String hrefValue)
    {

        mLogger.finest("mXMLBase[0][1]: " + mXMLBase[0][1]);
        mLogger.finest("mXMLBase[1][1]: " + mXMLBase[1][1]);
        mLogger.finest("mXMLBase[2][1]: " + mXMLBase[2][1]);
        mLogger.finest("href: " + hrefValue);
        return (mXMLBase[0][1] + mXMLBase[1][1] + mXMLBase[2][1] + hrefValue);

    }

    /**
     * This method retrives the launch data from the manifestHandler object. The
     * launch data returned contains information for the launching of SCOs found
     * within the content package test subject.  This method is only called if
     * the user chooses to validate SCOs. <br>
     * <br>
     *
     * @param iDefaultOrganizationOnly
     *           A boolean representing whether or not launch data should be
     *           collected from the default organization only.
     * @param iRemoveAssets
     *           A boolean representing whether or not assets should be removed
     *           in the LaunchData list.
     *
     * @return Vector:  Containing the launch information<br>
     */
    public void parseData(boolean iDefaultOrganizationOnly, boolean iRemoveAssets)
    {
        mManifestHandler.parseData((super.getDocument()).getDocumentElement(), iDefaultOrganizationOnly, iRemoveAssets);
    }

    public void parseData(Document iRolledUpDocument, boolean iDefaultOrganizationOnly, boolean iRemoveAssets)
    {
        mManifestHandler.parseData(iRolledUpDocument.getDocumentElement(), iDefaultOrganizationOnly, iRemoveAssets);
    }
    
    public Vector getLaunchDataList()
    {
        return mManifestHandler.getLaunchDataList();
    }
    
    public Vector getSequencingDataList()
    {
        return mManifestHandler.getSequencingDataList();
    }
    
    /**
     * This method retrives the metadata information from the manifestHandler
     * object. The metadata information returned contains information for the
     * validation of the metadata found within the content package test subject.
     * This method is only called if the user chooses to validate metadata. <br>
     *
     * @return Vector:  Containing the metadata launch information<br>
     */
    public Vector getMetadataDataList()
    {
        return mManifestHandler.getMetadata((super.getDocument()).getDocumentElement(), mBaseDirectory);
    }

    /**
     * This method is used to turn full content package validation off -
     * including required files check, validation to the schema, and application
     * profile checks.  Turning full validation off allows only a parse for
     * well-formedness to be preformed. <br>
     *
     * @param boolean True implies to parse for well-formedness and validation
     * to the schema, false implies to parse for well-formedness only.<br>
     */
    public void setPerformValidationToSchema(boolean iValue)
    {
        mPerformFullValidation = iValue;
    }

    /**
     * This method checks the values of xml:base, href, and adlcp:location to
     * ensure that they DO NOT begin with a slash '/'  It also checks xml:base
     * for the required trailing slash '/'<br>
     * <br>
     *
     * @param String iName
     *           Contains the name of the attribute/element being passed in
     * @param String iValue
     *           Contains the value of the attribute/element being passed in
     * @return boolean:  True if xml:base contains a trailing slash '/' and/or if
     *           href, xml:base, and location DO NOT contain leading slashes.<br>
     */
    private boolean checkForSlashes(String iName, String iValue)
    {
        mLogger.entering("CPValidator", "checkForSlashes()");
        mLogger.finest("Name: " + iName);
        mLogger.finest("Value: " + iValue);

        String msgText = "";
        boolean result = true;

        // Getting the last character in the string
        char tempChar = iValue.charAt(iValue.length() - 1);

        // if the name is xml:base, check to make sure the last character is "\"
        if (iName.equals("xml:base"))
        {
            if (tempChar != '/')
            {
                msgText = "Trailing slashes are required to be at the end of the " + iName + " value";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result &= false;
            }
        }

        // gets the first character in the string
        tempChar = iValue.charAt(0);
        // make sure the first character IS NOT a "\"
        if (tempChar == File.separatorChar || tempChar == '/')
        {
            msgText = iName + " values are not permitted to start with a slash";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false && result;
        }
        return result;

    } // end checkForSlashes()

}// end class CPValidator
