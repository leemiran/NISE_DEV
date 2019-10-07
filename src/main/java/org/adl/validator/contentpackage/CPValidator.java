package org.adl.validator.contentpackage;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;
import org.adl.parsers.dom.ADLDOMParser;
import org.adl.parsers.dom.DOMTreeUtility;
import org.adl.util.*;
import org.adl.util.zip.UnZipHandler;
import org.adl.validator.ADLSCORMValidator;
import org.adl.validator.RulesValidator;
import org.adl.validator.sequence.SequenceValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.*;

import egovframework.adm.lcms.cts.controller.LcmsOrganizationController;

// Referenced classes of package org.adl.validator.contentpackage:
//            ManifestHandler, ManifestMap

public class CPValidator extends ADLSCORMValidator implements Serializable
{
	
	protected static final Log log = LogFactory.getLog( LcmsOrganizationController.class);

    private Logger mLogger;
    private RulesValidator mRulesValidator;
    private String mBaseDirectory;
    private String mXMLBase[][];
    private Vector mOrganizationIdentifierList;
    private ManifestHandler mManifestHandler;
    private String mEnvironmentVariable;
    protected boolean mPerformFullValidation;
    private ManifestMap mManifestInfo;
    private String mManifestID;
    private Vector mResourceNodes;
    private Vector mValidIdrefs;
 
    public CPValidator(String iEnvironmentVariable)
    {
        super("contentpackage");
        mXMLBase = new String[3][2];
        mLogger = Logger.getLogger("org.adl.util.debug.validator");
        mBaseDirectory = new String();
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
        mManifestID = new String();
        mValidIdrefs = new Vector();
    }

    public boolean validate(String iFileName, String iTestType, String iApplicationProfileType, boolean iManifestOnly)
    {
        boolean validateResult = true;
        mLogger.entering("CPValidator", "validate()");
        mLogger.finer("      iXMLFileName coming in is " + iFileName);
        mLogger.finer("      iApplicationProfileType coming in is " + iApplicationProfileType);
        mLogger.finer("      iTestType coming in is " + iTestType);
        mBaseDirectory = getPathOfFile(iFileName);
        File manifestFile = new File(mBaseDirectory + "imsmanifest.xml");
        boolean manifestFound = true;
        if(!manifestFile.exists())
        {
            String msgText = "Required file \"imsmanifest.xml\" not found at the root of the package";
            log.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            manifestFound = false;
        }
        super.setIsIMSManifestPresent(manifestFound);
        if(manifestFound)
        {
            String msgText = "Testing the manifest for Well-formedness";
            log.info("INFO: " + msgText);
            validateResult = checkWellformedness(mBaseDirectory + "imsmanifest.xml") && validateResult;
            boolean wellformed = super.getIsWellformed();
            if(wellformed && !iManifestOnly && mPerformFullValidation)
                validateResult = checkForRequiredFiles(mBaseDirectory, iApplicationProfileType) && validateResult;
            if(validateResult && mPerformFullValidation)
            {
                msgText = "Testing the manifest for Validity to the Controlling Documents";
                log.info("INFO: " + msgText);
                validateResult = checkValidityToSchema(mBaseDirectory + "imsmanifest.xml") && validateResult;
                msgText = "****************************************";
                log.info("OTHER: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.OTHER, msgText));
                msgText = "Validating against the Application Profile Rules";
                log.info("INFO: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                msgText = "Performing Validation on ID/IDRef mappings";
                log.info("INFO: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                boolean populateResult = mManifestInfo.populateManifestMap(super.getDocument());
                boolean idrefResult = true;
                if(populateResult)
                {
                    Vector idrefs = mManifestInfo.checkAllIdReferences();
                    if(!idrefs.isEmpty())
                    {
                        log.info("invalid idrefs exist");
                        validateResult = false;
                        idrefResult = false;
                        super.setIsValidToApplicationProfile(super.getIsValidToApplicationProfile() ? false : false);
                    } else
                    {
                        msgText = "ID/IDRef Validation checks passed";
                        log.info("INFO: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                    }
                }
                boolean doXMLRulesExist = mRulesValidator.readInRules(iApplicationProfileType, mEnvironmentVariable);
                if(doXMLRulesExist)
                {
                    trackOrgIdentifiers(super.getDocument());
                    boolean isAppProfileResult = compareToRules(super.getDocument(), "");
                    super.setIsValidToApplicationProfile(idrefResult && isAppProfileResult);
                    validateResult = isAppProfileResult && validateResult;
                } else
                {
                    msgText = "Testing the manifest against the Application Profile Rules cannot be performed due to a system error. This is due to the inaccessability of certain key files of the Application.  It is recommended to repair or reinstall the Application and re-run.";
                    log.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    validateResult = false;
                }
            }
        }
        log.info("CPValidator  validate()");
        return validateResult;
    }

    private void importContentPackage(String iPIF)
    {
        mLogger.entering("CPValidator", "importContentPackage()");
        String extractDir = mEnvironmentVariable + File.separator + "PackageImport" + File.separator;
        mLogger.info("extractDir = " + extractDir);
        UnZipHandler uzh = new UnZipHandler(iPIF, extractDir);
        uzh.extract();
        mLogger.exiting("CPValidator", "importContentPackage()");
    }

    private boolean checkValidityToSchema(String iManifestFileName)
    {
        mLogger.entering("CPValidator", "checkManifest()");
        boolean manifestResult = true;
        super.setPerformFullValidation(mPerformFullValidation);
        super.performValidatorParse(iManifestFileName);
        boolean isWellformed = super.getIsWellformed();
        if(mPerformFullValidation)
        {
            boolean isValid = super.getIsValidToSchema();
            if(isValid)
            {
                String msgText = "The manifest instance is valid to the controlling documents";
                mLogger.info("PASSED: " + msgText);
            } else
            {
                String msgText = "The manifest instance is not valid to the controlling documents";
                mLogger.info("FAILED: " + msgText);
            }
            manifestResult = isWellformed && isValid;
        } else
        {
            manifestResult = isWellformed;
        }
        mLogger.exiting("CPValidator", "checkManifest()");
        return manifestResult;
    }

    private boolean checkWellformedness(String iManifestFileName)
    {
        mLogger.entering("CPValidator", "checkWellformedness()");
        boolean wellnessResult = true;
        super.setPerformFullValidation(false);
        super.performValidatorParse(iManifestFileName);
        boolean isWellformed = super.getIsWellformed();
        wellnessResult = isWellformed;
        mLogger.exiting("CPValidator", "checkWellformedness");
        return wellnessResult;
    }

    private boolean checkForRequiredFiles(String iDirectory, String iApplicationProfile)
    {
        mLogger.entering("CPValidator", "checkForRequiredFiles()");
        boolean result = true;
        String msgText = "****************************************";
        mLogger.info("OTHER: " + msgText);
        MessageCollection.getInstance().add(new Message(MessageType.OTHER, msgText));
        msgText = "Searching for Required Files";
        mLogger.info("INFO: " + msgText);
        MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
        ADLDOMParser adldomparser = new ADLDOMParser();
        String xsdLocation = iDirectory.replaceAll(" ", "%20");
        xsdLocation = xsdLocation.replace('\\', '/');
        adldomparser.parseForWellformedness(xsdLocation + "imsmanifest.xml", false);
        Document manifestDocument = adldomparser.getDocument();
        if(manifestDocument != null)
            try
            {
                String nodeNameStr = manifestDocument.getDocumentElement().getNodeName();
                if(nodeNameStr.equalsIgnoreCase("manifest"))
                    if(manifestDocument.getDocumentElement().hasAttributes())
                    {
                        NamedNodeMap AttributesList = manifestDocument.getDocumentElement().getAttributes();
                        int namespaceResult = checkNamespaces(AttributesList, xsdLocation);
                        if(namespaceResult == 0)
                            result = false;
                        else
                        if(namespaceResult == 1)
                            result = true;
                        else
                            result = testDefaultRequiredFiles(iApplicationProfile, iDirectory, manifestDocument);
                    } else
                    {
                        result = testDefaultRequiredFiles(iApplicationProfile, iDirectory, manifestDocument);
                    }
            }
            catch(NullPointerException npe)
            {
                mLogger.severe("NullPointerException thrown when creating File of optional file");
            }
        msgText = "****************************************";
        mLogger.info("OTHER: " + msgText);
        MessageCollection.getInstance().add(new Message(MessageType.OTHER, msgText));
        mLogger.finest("returning the following result " + result);
        mLogger.exiting("CPValidator", "checkForRequiredFiles()");
        super.setIsRequiredFiles(result);
        return result;
    }

    private int checkNamespaces(NamedNodeMap iAttributesList, String iXsdLocation)
    {
        int result = 1;
        String schemaStr = "";
        String msgText = "";
        boolean schemaLocationFound = false;
        int size = iAttributesList.getLength();
        for(int j = 0; j < size; j++)
            if(iAttributesList.item(j).getNodeName().equalsIgnoreCase("xsi:schemaLocation"))
            {
                schemaLocationFound = true;
                String namespaceURIs = iAttributesList.item(j).getNodeValue();
                for(StringTokenizer st = new StringTokenizer(namespaceURIs, " "); st.hasMoreTokens();)
                {
                    String tok = st.nextToken();
                    if(st.hasMoreTokens())
                    {
                        tok = st.nextToken();
                        StringTokenizer schemaLocationTokens = new StringTokenizer(tok, "/");
                        String schemaName = new String();
                        String toke = new String();
                        String previousToke = new String();
                        boolean verifyExistance = true;
                        while(schemaLocationTokens.hasMoreTokens()) 
                        {
                            previousToke = toke;
                            toke = schemaLocationTokens.nextToken();
                            if(!schemaLocationTokens.hasMoreTokens())
                                if(!previousToke.equals("vocab"))
                                    schemaName = iXsdLocation + toke;
                                else
                                    verifyExistance = false;
                        }
                        if(verifyExistance)
                        {
                            File currentFile = new File(schemaName);
                            boolean currentResult = currentFile.exists();
                            if(currentResult)
                            {
                                msgText = "Required file \"" + toke + "\" found " + "at the root of the package";
                                mLogger.info("PASSED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                            } else
                            {
                                msgText = "Required file \"" + toke + "\" not " + "found at the root of the package";
                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                result = 0;
                            }
                        }
                    }
                }

            }

        if(!schemaLocationFound)
            result = -1;
        return result;
    }

    private boolean testDefaultRequiredFiles(String iApplicationProfile, String iDirectory, Document iManifestDoc)
    {
        boolean result = true;
        boolean lomFound = false;
        ArrayList requiredFiles = new ArrayList();
        requiredFiles.add("imsmanifest.xml");
        requiredFiles.add("imscp_v1p1.xsd");
        requiredFiles.add("adlcp_v1p3.xsd");
        ArrayList lomFiles = new ArrayList();
        NodeList currNodes = iManifestDoc.getElementsByTagName("lom");
        if(currNodes.getLength() > 0)
        {
            lomFiles.add("lomCustom.xsd");
            lomFiles.add("lomStrict.xsd");
            lomFiles.add("lom.xsd");
            lomFiles.add("lomLoose.xsd");
        }
        currNodes = iManifestDoc.getElementsByTagName("imsss:sequencing");
        if(currNodes.getLength() > 0)
        {
            requiredFiles.add("imsss_v1p0.xsd");
        } else
        {
            currNodes = iManifestDoc.getElementsByTagName("sequencing");
            if(currNodes.getLength() > 0)
                requiredFiles.add("imsss_v1p0.xsd");
        }
        currNodes = iManifestDoc.getElementsByTagName("adlnav:presentation");
        if(currNodes.getLength() > 0)
        {
            requiredFiles.add("adlnav_v1p3.xsd");
        } else
        {
            currNodes = iManifestDoc.getElementsByTagName("presentation");
            if(currNodes.getLength() > 0)
                requiredFiles.add("adlnav_v1p3.xsd");
        }
        currNodes = iManifestDoc.getElementsByTagName("adlseq:constrainedChoiceConsiderations");
        if(currNodes.getLength() > 0)
        {
            requiredFiles.add("adlseq_v1p3.xsd");
        } else
        {
            currNodes = iManifestDoc.getElementsByTagName("constrainedChoiceConsiderations");
            if(currNodes.getLength() > 0)
                requiredFiles.add("adlseq_v1p3.xsd");
        }
        try
        {
            if(lomFiles.size() > 0)
            {
                lomFound = false;
                int size = lomFiles.size();
                for(int j = 0; j < size; j++)
                {
                    File currentFile = new File(iDirectory + lomFiles.get(j));
                    boolean currentResult = currentFile.exists();
                    if(currentResult && !lomFound)
                    {
                        String msgText = "Required file \"" + lomFiles.get(j) + "\" found " + "at the root of the package";
                        mLogger.info("PASSED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                        lomFound = true;
                    }
                }

                if(!lomFound)
                {
                    String msgText = "Required file(s) \"lom.xsd, lomStrict.xsd, lomCustom.xsd, or lomLoose.xsd\" not found at the root of the package";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            }
            int requiredFilesSize = requiredFiles.size();
            for(int i = 0; i < requiredFilesSize; i++)
            {
                File currentFile = new File(iDirectory + requiredFiles.get(i));
                boolean currentResult = currentFile.exists();
                if(currentResult)
                {
                    String msgText = "Required file \"" + requiredFiles.get(i) + "\" found " + "at the root of the package";
                    mLogger.info("PASSED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                } else
                {
                    String msgText = "Required file \"" + requiredFiles.get(i) + "\" not " + "found at the root of the package";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            }

        }
        catch(NullPointerException npe)
        {
            mLogger.severe("NullPointerException thrown when creating File of required file");
        }
        return result;
    }

    private String getPathOfFile(String iFileName)
    {
        mLogger.entering("CPValidator", "getPathOfFile()");
        String result = new String("");
        String tmp = new String("");
        try
        {
            StringTokenizer token = new StringTokenizer(iFileName, File.separator, true);
            int numTokens = token.countTokens();
            numTokens--;
            for(int i = 0; i < numTokens; i++)
            {
                tmp = token.nextToken();
                mLogger.finest("token = " + tmp);
                result = result + tmp;
            }

        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
        }
        mLogger.exiting("CPValidator", "getPathOfFile()");
        return result;
    }

    private void trackOrgIdentifiers(Node iParentNode)
    {
        mLogger.entering("CPValidator", "trackOrgIdentifiers()");
        String msgText = new String();
        if(iParentNode != null)
        {
            int type = iParentNode.getNodeType();
            switch(type)
            {
            default:
                break;

            case 9: // '\t'
                Node rootNode = ((Document)iParentNode).getDocumentElement();
                trackOrgIdentifiers(rootNode);
                break;

            case 1: // '\001'
                String nodeName = iParentNode.getNodeName();
                if(nodeName.equals("manifest"))
                {
                    Vector orgNodes = DOMTreeUtility.getNodes(DOMTreeUtility.getNode(iParentNode, "organizations"), "organization");
                    int orgNodesSize = orgNodes.size();
                    for(int i = 0; i < orgNodesSize; i++)
                    {
                        Node currentChild = (Node)orgNodes.elementAt(i);
                        String OrgId = DOMTreeUtility.getAttributeValue(currentChild, "identifier");
                        mOrganizationIdentifierList.add(OrgId);
                        msgText = "Just added " + OrgId + "to the org vector";
                        mLogger.finest(msgText);
                    }

                }
                Vector subManifestList = DOMTreeUtility.getNodes(iParentNode, "manifest");
                int subManifestListSize = subManifestList.size();
                for(int j = 0; j < subManifestListSize; j++)
                {
                    Node currentSubManifest = (Node)subManifestList.elementAt(j);
                    trackOrgIdentifiers(currentSubManifest);
                }

                break;
            }
        }
        mLogger.exiting("CPValidator", "trackOrgIdentifiers()");
    }

    private boolean compareToRules(Node iTestSubjectNode, String iPath)
    {
        mLogger.entering("CPValidator", "compareToRules");
        boolean result = true;
        String msgText = new String();
        if(iTestSubjectNode == null)
        {
            result = false;
            return result;
        }
        int type = iTestSubjectNode.getNodeType();
        switch(type)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
        case 7: // '\007'
        case 8: // '\b'
        default:
            break;

        case 9: // '\t'
            Node rootNode = ((Document)iTestSubjectNode).getDocumentElement();
            String rootNodeName = rootNode.getLocalName();
            msgText = "Testing element <" + rootNodeName + "> for minimum comformance";
            mLogger.info("INFO: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
            msgText = "Multiplicity for element <" + rootNodeName + "> has been verified";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            result = compareToRules(rootNode, "") && result;
            break;

        case 1: // '\001'
            String parentNodeName = iTestSubjectNode.getLocalName();
            if(parentNodeName.equals("manifest"))
            {
                Node resourcesNode = DOMTreeUtility.getNode(iTestSubjectNode, "resources");
                if(resourcesNode != null)
                    mResourceNodes = DOMTreeUtility.getNodes(resourcesNode, "resource");
            }
            String dataType = null;
            int multiplicityUsed = -1;
            int minRule = -1;
            int maxRule = -1;
            int spmRule = -1;
            NamedNodeMap attrList = iTestSubjectNode.getAttributes();
            int numAttr = attrList.getLength();
            mLogger.finer("There are " + numAttr + " attributes of " + parentNodeName + " to test");
            String attributeValue = null;
            for(int i = 0; i < numAttr; i++)
            {
                Attr currentAttrNode = (Attr)attrList.item(i);
                String currentNodeName = currentAttrNode.getLocalName();
                if(currentNodeName.equals("persistState") && !parentNodeName.equals("resource"))
                {
                    result = false;
                    msgText = "\"" + currentNodeName + "\" can only " + "exist as an attribute of a <resource> element";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                }
                if(currentNodeName.equals("scormType") && !parentNodeName.equals("resource"))
                {
                    result = false;
                    msgText = "\"" + currentNodeName + "\" can only " + "exist as an attribute of a <resource> element";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                }
                if(currentNodeName.equals("objectivesGlobalToSystem") && !parentNodeName.equals("organization"))
                {
                    result = false;
                    msgText = "\"" + currentNodeName + "\" can only " + "exist as an attribute of a <organization> element";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                }
                dataType = mRulesValidator.getRuleValue(parentNodeName, iPath, "datatype", currentNodeName);
                if(dataType.equalsIgnoreCase("xmlbase"))
                {
                    msgText = "Testing attribute \"" + currentNodeName + "\" for minimum comformance";
                    mLogger.info("INFO: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                    multiplicityUsed = getMultiplicityUsed(attrList, currentNodeName);
                    minRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "min", currentNodeName));
                    maxRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "max", currentNodeName));
                    if(minRule != -1 || maxRule != -1)
                        if(multiplicityUsed >= minRule && multiplicityUsed <= maxRule)
                        {
                            msgText = "Multiplicity for attribute \"" + currentNodeName + "\" has been verified";
                            mLogger.info("PASSED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                        } else
                        if(!iPath.startsWith("manifest.manifest") && (!parentNodeName.equals("manifest") || !iPath.equals("manifest")))
                        {
                            msgText = "The \"" + currentNodeName + "\" attribute is not within the " + "multiplicity bounds.";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false;
                        } else
                        {
                            msgText = "The \"" + currentNodeName + "\" attribute is not within the " + "multiplicity bounds for (Sub) manifest " + "elements.  At this time, there is no " + "guidance given in the SCORM on the " + "application of xml:base in " + "(Sub) manifest(s).";
                            mLogger.info("WARNING: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                        }
                    spmRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "spm", currentNodeName));
                    attributeValue = currentAttrNode.getValue();
                    result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;
                    if(attributeValue.length() > 4 && !attributeValue.substring(0, 5).equals("http:") && !attributeValue.substring(0, 6).equals("https:") && !attributeValue.substring(0, 4).equals("ftp:") && !attributeValue.substring(0, 5).equals("ftps:"))
                        attributeValue = attributeValue.replace('/', File.separatorChar);
                    char lastChar = attributeValue.charAt(attributeValue.length() - 1);
                    if(lastChar != File.separatorChar && lastChar != '/')
                        attributeValue = attributeValue + File.separatorChar;
                    if(parentNodeName.equals("manifest"))
                    {
                        mXMLBase[0][1] = attributeValue;
                        mLogger.info(" XML:base found in manifest, value is " + attributeValue);
                    } else
                    if(parentNodeName.equals("resources"))
                    {
                        mXMLBase[1][1] = attributeValue;
                        mLogger.info(" XML:base found in resources, value is " + attributeValue);
                    } else
                    if(parentNodeName.equals("resource"))
                    {
                        mXMLBase[2][1] = attributeValue;
                        mLogger.info(" XML:base found in resource, value is " + attributeValue);
                    }
                }
            }

            if(parentNodeName.equalsIgnoreCase("manifest"))
            {
                multiplicityUsed = getMultiplicityUsed(attrList, "identifier");
                if(multiplicityUsed < 1)
                {
                    msgText = "Mandatory attribute \"identifier\" could not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            } else
            if(parentNodeName.equalsIgnoreCase("organizations") && mRulesValidator.getApplicationProfile().equals("contentaggregation"))
            {
                multiplicityUsed = getMultiplicityUsed(attrList, "default");
                int numOrganizations = getMultiplicityUsed(iTestSubjectNode, "organization");
                if(multiplicityUsed < 1 && !iPath.startsWith("manifest.manifest"))
                {
                    msgText = "Mandatory attribute \"default\" could not be found";
                    mLogger.info("ERROR: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            } else
            if(parentNodeName.equalsIgnoreCase("organization") && mRulesValidator.getApplicationProfile().equals("contentaggregation"))
            {
                multiplicityUsed = getMultiplicityUsed(attrList, "identifier");
                if(multiplicityUsed < 1)
                {
                    msgText = "Mandatory attribute \"identifier\" could not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            } else
            if(parentNodeName.equalsIgnoreCase("item") && mRulesValidator.getApplicationProfile().equals("contentaggregation"))
            {
                multiplicityUsed = getMultiplicityUsed(attrList, "identifier");
                if(multiplicityUsed < 1)
                {
                    msgText = "Mandatory attribute \"identifier\" could not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
                int idrefMult = -1;
                int paramMult = -1;
                idrefMult = getMultiplicityUsed(attrList, "identifierref");
                paramMult = getMultiplicityUsed(attrList, "parameters");
                if(idrefMult < 1 && paramMult >= 1)
                {
                    msgText = "The \"parameters\" attribute should not be used when an \"identifierref\" attribute does not exist on an <item>";
                    mLogger.info("WARNING: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                }
                if(idrefMult >= 1)
                {
                    String iDREFValue = DOMTreeUtility.getAttributeValue(iTestSubjectNode, "identifierref");
                    boolean validIdref = checkForReferenceToScoOrAsset(iDREFValue);
                    if(validIdref)
                        mValidIdrefs.add(iDREFValue);
                }
                NodeList childrenOfItem = iTestSubjectNode.getChildNodes();
                if(childrenOfItem != null)
                {
                    int len = childrenOfItem.getLength();
                    for(int k = 0; k < len; k++)
                    {
                        Node currentItemChild = childrenOfItem.item(k);
                        String currentItemChildName = currentItemChild.getLocalName();
                        if(currentItemChildName.equals("timeLimitAction") || currentItemChildName.equals("dataFromLMS") || currentItemChildName.equals("completionThreshold") || currentItemChildName.equals("presentation"))
                            if(idrefMult < 1)
                            {
                                result = false;
                                msgText = "Only those <item> elements that reference a SCO resource can contain the <" + currentItemChildName + "> element";
                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            } else
                            {
                                String idrefValue = DOMTreeUtility.getAttributeValue(iTestSubjectNode, "identifierref");
                                result = result && checkForReferenceToSco(idrefValue);
                            }
                    }

                }
            } else
            if(parentNodeName.equalsIgnoreCase("resource"))
                result = result && checkResourceAttributes(iTestSubjectNode, attrList);
            for(int j = 0; j < numAttr; j++)
            {
                Attr currentAttrNode = (Attr)attrList.item(j);
                String currentNodeName = currentAttrNode.getLocalName();
                dataType = mRulesValidator.getRuleValue(parentNodeName, iPath, "datatype", currentNodeName);
                if(dataType.equalsIgnoreCase("idref") || dataType.equalsIgnoreCase("id") || dataType.equalsIgnoreCase("vocabulary") || dataType.equalsIgnoreCase("text") || dataType.equalsIgnoreCase("uri"))
                {
                    msgText = "Testing attribute \"" + currentNodeName + "\" for minimum comformance";
                    mLogger.info("INFO: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                    multiplicityUsed = getMultiplicityUsed(attrList, currentNodeName);
                    minRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "min", currentNodeName));
                    maxRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "max", currentNodeName));
                    if(minRule != -1 || maxRule != -1)
                        if(multiplicityUsed >= minRule && multiplicityUsed <= maxRule)
                        {
                            msgText = "Multiplicity for attribute \"" + currentNodeName + "\" has been verified";
                            mLogger.info("PASSED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                        } else
                        {
                            msgText = "The \"" + currentNodeName + "\" attribute is not within the " + "multiplicity bounds.";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false;
                        }
                    spmRule = Integer.parseInt(mRulesValidator.getRuleValue(parentNodeName, iPath, "spm", currentNodeName));
                    attributeValue = currentAttrNode.getValue();
                    if(dataType.equalsIgnoreCase("idref"))
                    {
                        result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;
                        if(currentNodeName.equalsIgnoreCase("default"))
                        {
                            boolean foundDefaultIdentifier = false;
                            int numOrganizationIdentifiers = mOrganizationIdentifierList.size();
                            for(int i = 0; i < numOrganizationIdentifiers; i++)
                            {
                                String identifier = (String)mOrganizationIdentifierList.elementAt(i);
                                if(!identifier.equals(attributeValue))
                                    continue;
                                foundDefaultIdentifier = true;
                                break;
                            }

                            if(foundDefaultIdentifier)
                            {
                                msgText = "The \"" + currentNodeName + "\" attribute references a valid identifier";
                                mLogger.info("PASSED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                            } else
                            {
                                msgText = "The \"" + currentNodeName + "\" attribute does not reference a " + "valid identifier of an <organization>";
                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                result = false;
                            }
                        }
                    } else
                    if(dataType.equalsIgnoreCase("id"))
                    {
                        result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;
                        if(parentNodeName.equals("manifest"))
                        {
                            mManifestID = currentAttrNode.getValue();
                            msgText = "mManifestID is " + mManifestID;
                            mLogger.info(msgText);
                        }
                    } else
                    if(dataType.equalsIgnoreCase("uri"))
                    {
                        String myAttributeValue = currentAttrNode.getValue();
                        if(!myAttributeValue.equals(""))
                        {
                            result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;
                            boolean doApplyXMLBase = true;
                            if(iPath.startsWith("manifest.manifest") && doesXMLBaseExist())
                                doApplyXMLBase = false;
                            if(doApplyXMLBase)
                                myAttributeValue = applyXMLBase(myAttributeValue);
                            result = checkHref(myAttributeValue, doApplyXMLBase) && result;
                        }
                    } else
                    if(dataType.equalsIgnoreCase("vocabulary"))
                    {
                        msgText = "Testing attribute \"" + currentNodeName + "\" for valid vocabulary";
                        mLogger.info("INFO: " + msgText);
                        if(currentNodeName.equals("persistState"))
                        {
                            msgText = "The \"adcp:" + currentNodeName + "\" attribute has been deprecated.  This" + " attribute may result in undefined behavior.";
                            mLogger.info("WARNING: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                        }
                        Vector vocabAttribValues = mRulesValidator.getAttribVocabRuleValues(parentNodeName, iPath, currentNodeName);
                        result = checkVocabulary(currentNodeName, attributeValue, vocabAttribValues, true) && result;
                    } else
                    if(dataType.equalsIgnoreCase("text"))
                        result = checkSPMConformance(currentNodeName, attributeValue, spmRule, true) && result;
                }
            }

            NodeList children = iTestSubjectNode.getChildNodes();
            if(children != null)
            {
                int numChildren = children.getLength();
                String path;
                if(iPath.equals(""))
                    path = parentNodeName;
                else
                if(iPath.equals("manifest.manifest") && parentNodeName.equals("manifest"))
                    path = iPath;
                else
                if(parentNodeName.equalsIgnoreCase("item"))
                {
                    if(iPath.equals("manifest.organizations.organization.item"))
                        path = iPath;
                    else
                        path = iPath + "." + parentNodeName;
                } else
                {
                    path = iPath + "." + parentNodeName;
                }
                if(parentNodeName.equalsIgnoreCase("manifest"))
                {
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "metadata");
                    if(multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <metadata> could not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    } else
                    {
                        Node caMetadataNode = DOMTreeUtility.getNode(iTestSubjectNode, "metadata");
                        multiplicityUsed = getMultiplicityUsed(caMetadataNode, "schema");
                        if(multiplicityUsed < 1)
                        {
                            msgText = "Mandatory element <schema> could not be found";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false;
                        }
                        multiplicityUsed = getMultiplicityUsed(caMetadataNode, "schemaversion");
                        if(multiplicityUsed < 1)
                        {
                            msgText = "Mandatory element <schemaversion> could not be found";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false;
                        }
                    }
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "organizations");
                    if(multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <organizations> could not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "resources");
                    if(multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <resources> could not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                } else
                if(parentNodeName.equalsIgnoreCase("organizations") && mRulesValidator.getApplicationProfile().equals("contentaggregation"))
                {
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "organization");
                    if(path.startsWith("manifest.manifest"))
                        result = checkOrganizationMultiplicity(iTestSubjectNode, mManifestInfo) && result;
                    else
                    if(multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <organization> could not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                } else
                if(parentNodeName.equalsIgnoreCase("organizations") && mRulesValidator.getApplicationProfile().equals("resource"))
                {
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "organization");
                    if(multiplicityUsed > 0)
                    {
                        msgText = "The <organizations> element must contain no  children in a resource package";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                } else
                if(parentNodeName.equalsIgnoreCase("organization") && mRulesValidator.getApplicationProfile().equals("contentaggregation"))
                {
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "title");
                    if(multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <title> could not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, "item");
                    if(multiplicityUsed < 1)
                    {
                        msgText = "Mandatory element <item> could not be found";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                    result = checkItem(iTestSubjectNode, mManifestInfo) && result;
                }
                for(int z = 0; z < numChildren; z++)
                {
                    Node currentChild = children.item(z);
                    String currentChildName = currentChild.getLocalName();
                    msgText = "Currentchild is " + currentChildName + " and path is " + path;
                    mLogger.info(msgText);
                    if(currentChildName != null)
                    {
                        if((currentChildName.equals("timeLimitAction") || currentChildName.equals("dataFromLMS") || currentChildName.equals("completionThreshold") || currentChildName.equals("presentation")) && !parentNodeName.equals("item"))
                        {
                            result = false;
                            msgText = "<" + currentChildName + "> can only " + "exist as a child of an <item> element";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        }
                        if((currentChildName.equals("constrainedChoiceConsiderations") || currentChildName.equals("rollupConsiderations")) && !parentNodeName.equals("sequencing"))
                        {
                            result = false;
                            msgText = "<" + currentChildName + "> can only " + "exist as a child of an <sequencing> element";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        }
                        if(currentChildName.equals("location") && !parentNodeName.equals("metadata"))
                        {
                            result = false;
                            msgText = "<" + currentChildName + "> can only " + "exist as a child of an <metadata> element";
                            mLogger.info("WARNING: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                        }
                        if(currentChildName.equals("sequencing") && !parentNodeName.equals("item") && !parentNodeName.equals("organization"))
                        {
                            result = false;
                            msgText = "<" + currentChildName + "> can only " + "exist as a child of an <item > or an " + "<organization> element";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        }
                        dataType = mRulesValidator.getRuleValue(currentChildName, path, "datatype");
                        if(dataType.equalsIgnoreCase("parent") || dataType.equalsIgnoreCase("vocabulary") || dataType.equalsIgnoreCase("text") || dataType.equalsIgnoreCase("sequencing") || dataType.equalsIgnoreCase("metadata"))
                        {
                            msgText = "Testing element <" + currentChildName + "> for minimum comformance";
                            mLogger.info("INFO: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                            if(!currentChildName.equals("organization") && !path.startsWith("manifest.manifest"))
                            {
                                multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, currentChildName);
                                minRule = Integer.parseInt(mRulesValidator.getRuleValue(currentChildName, path, "min"));
                                maxRule = Integer.parseInt(mRulesValidator.getRuleValue(currentChildName, path, "max"));
                                if(minRule != -1 && maxRule != -1)
                                {
                                    if(multiplicityUsed >= minRule && multiplicityUsed <= maxRule)
                                    {
                                        msgText = "Multiplicity for element <" + currentChildName + "> has been verified";
                                        mLogger.info("PASSED: " + msgText);
                                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                                    } else
                                    {
                                        msgText = "The <" + currentChildName + "> element is not within the " + "multiplicity bounds.";
                                        mLogger.info("FAILED: " + msgText);
                                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                        result = false;
                                    }
                                } else
                                if(minRule != -1 && maxRule == -1)
                                    if(multiplicityUsed >= minRule)
                                    {
                                        msgText = "Multiplicity for element <" + currentChildName + "> has been verified";
                                        mLogger.info("PASSED: " + msgText);
                                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                                    } else
                                    {
                                        msgText = "The <" + currentChildName + "> element is not within the " + "multiplicity bounds.";
                                        mLogger.info("FAILED: " + msgText);
                                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                        result = false;
                                    }
                            }
                            if(dataType.equalsIgnoreCase("parent"))
                            {
                                if(currentChildName.equalsIgnoreCase("manifest"))
                                    trackOrgIdentifiers(currentChild);
                                result = compareToRules(currentChild, path) && result;
                            } else
                            if(dataType.equalsIgnoreCase("sequencing"))
                            {
                                SequenceValidator sequenceValidator = new SequenceValidator(mEnvironmentVariable);
                                result = sequenceValidator.validate(currentChild) && result;
                            } else
                            if(dataType.equalsIgnoreCase("metadata"))
                            {
                                if(currentChildName.equals("location"))
                                {
                                    String currentLocationValue = mRulesValidator.getTaggedData(currentChild);
                                    result = result && checkHref(currentLocationValue, true);
                                }
                            } else
                            if(dataType.equalsIgnoreCase("text"))
                            {
                                String currentChildValue = mRulesValidator.getTaggedData(currentChild);
                                spmRule = Integer.parseInt(mRulesValidator.getRuleValue(currentChildName, path, "spm"));
                                result = checkSPMConformance(currentChildName, currentChildValue, spmRule, false) && result;
                            } else
                            if(dataType.equalsIgnoreCase("vocabulary"))
                            {
                                msgText = "Testing element \"" + currentChildName + "\" for valid vocabulary";
                                mLogger.info("INFO: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                                String currentChildValue = mRulesValidator.getTaggedData(currentChild);
                                Vector vocabValues = mRulesValidator.getVocabRuleValues(currentChildName, path);
                                result = checkVocabulary(currentChildName, currentChildValue, vocabValues, false) && result;
                            } else
                            {
                                dataType.equalsIgnoreCase("decimal");
                            }
                        }
                    }
                }

            }
            if(parentNodeName.equals("manifest"))
            {
                mXMLBase[0][1] = "";
                break;
            }
            if(parentNodeName.equals("resources"))
            {
                mXMLBase[1][1] = "";
                break;
            }
            if(parentNodeName.equals("resource"))
                mXMLBase[2][1] = "";
            break;
        }
        mLogger.exiting("CPValidator", "compareToRules()");
        return result;
    }

    private boolean checkOrganizationMultiplicity(Node iOrgsNode, ManifestMap iManifestInfo)
    {
        mLogger.entering("CPValidator", "checkOrganizationMultiplicity");
        int multiplicityUsed = -1;
        boolean result = true;
        String msgText = "mManifestID is " + mManifestID;
        mLogger.info(msgText);
        if(!mManifestID.equals(""))
            if(mManifestID.equals(iManifestInfo.getManifestId()))
            {
                multiplicityUsed = getMultiplicityUsed(iOrgsNode, "organization");
                if(multiplicityUsed < 1)
                {
                    msgText = "Mandatory element <organization> could not be found";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                } else
                {
                    msgText = "Multiplicity for  element <organization> has been verified";
                    mLogger.info("PASSED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                }
            } else
            if(!iManifestInfo.getManifestMaps().isEmpty())
            {
                for(int j = 0; j < iManifestInfo.getManifestMaps().size(); j++)
                {
                    ManifestMap currentMap = (ManifestMap)iManifestInfo.getManifestMaps().elementAt(j);
                    if(mManifestID.equals(currentMap.getManifestId()))
                    {
                        String profile = currentMap.getApplicationProfile();
                        if(profile.equals("other"))
                        {
                            multiplicityUsed = getMultiplicityUsed(iOrgsNode, "organization");
                            if(multiplicityUsed >= 0 && multiplicityUsed <= 1)
                            {
                                msgText = "Mutiplicity for element <organization> has been verified";
                                mLogger.info("PASSED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                                result = result;
                            } else
                            {
                                msgText = " Referencing a <resource> only within a  (Sub) manifests must have 0 or 1 <organization> element";
                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                                result = false;
                            }
                            break;
                        }
                        if(!profile.equals("contentaggregation"))
                            continue;
                        multiplicityUsed = getMultiplicityUsed(iOrgsNode, "organization");
                        if(multiplicityUsed >= 1 && multiplicityUsed <= 1)
                        {
                            msgText = "Multiplicity for element <organization>  has been verified.  1 and only 1 may exist in this case";
                            mLogger.info("PASSED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                            result = result;
                        } else
                        {
                            msgText = "Referenced (Sub) manifests must have 1 and only 1 <organization> element";
                            mLogger.info("FAILED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            result = false;
                        }
                        break;
                    }
                    checkOrganizationMultiplicity(iOrgsNode, currentMap);
                }

            }
        return result;
    }

    private boolean checkResourceAttributes(Node iResourceNode, NamedNodeMap iAttrList)
    {
        mLogger.entering("CPValidator", "checkResource");
        int idMultiplicityUsed = -1;
        int typeMultiplicityUsed = -1;
        int scormMultiplicityUsed = -1;
        int hrefMultiplicityUsed = -1;
        boolean result = true;
        idMultiplicityUsed = getMultiplicityUsed(iAttrList, "identifier");
        if(idMultiplicityUsed < 1)
        {
            String msgText = "Mandatory attribute \"identifier\" could not be found";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false;
        }
        typeMultiplicityUsed = getMultiplicityUsed(iAttrList, "type");
        if(typeMultiplicityUsed < 1)
        {
            String msgText = "Mandatory attribute \"type\" could not be found";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false;
        }
        scormMultiplicityUsed = getMultiplicityUsed(iAttrList, "scormType");
        if(scormMultiplicityUsed < 1)
        {
            String msgText = "Mandatory attribute \"scormType\" could not be found";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false;
        }
        if(mRulesValidator.getApplicationProfile().equals("contentaggregation"))
        {
            String resourceID = DOMTreeUtility.getAttributeValue(iResourceNode, "identifier");
            boolean referencesAResource = false;
            if(!resourceID.equals(""))
            {
                for(int i = 0; i < mValidIdrefs.size(); i++)
                {
                    String currentIdref = (String)mValidIdrefs.elementAt(i);
                    if(resourceID.equals(currentIdref))
                        referencesAResource = true;
                }

                if(referencesAResource)
                {
                    hrefMultiplicityUsed = getMultiplicityUsed(iAttrList, "href");
                    if(hrefMultiplicityUsed < 1)
                    {
                        String msgText = "Mandatory attribute \"href\" could not be found on a <resource> that is referenced by an <item>";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                    String typeValue = DOMTreeUtility.getAttributeValue(iResourceNode, "type");
                    if(!typeValue.equals("webcontent"))
                    {
                        String msgText = "The type attribute shall be set to   \"webcontent\" when an <item> references the <resource>";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    private boolean checkItem(Node iOrgNode, ManifestMap iManifestInfo)
    {
        mLogger.entering("CPValidator", "checkItem");
        int multiplicityUsed = -1;
        boolean result = true;
        result = checkItemIdentifierRef(iOrgNode) && result;
        result = checkItemChildMultiplicity(iOrgNode, iManifestInfo) && result;
        return result;
    }

    private boolean checkForReferenceToScoOrAsset(String idrefValue)
    {
        mLogger.entering("CPValidator", "checkForReferenceToSCO");
        boolean result = false;
        int len = mResourceNodes.size();
        for(int i = 0; i < len; i++)
        {
            Node currentResource = (Node)mResourceNodes.elementAt(i);
            String id = DOMTreeUtility.getAttributeValue(currentResource, "identifier");
            if(id.equals(idrefValue))
                result = true;
        }

        return result;
    }

    private boolean checkForReferenceToSco(String idrefValue)
    {
        mLogger.entering("CPValidator", "checkForReferenceToScoOrAsset");
        boolean result = true;
        int len = mResourceNodes.size();
        for(int i = 0; i < len; i++)
        {
            Node currentResource = (Node)mResourceNodes.elementAt(i);
            String id = DOMTreeUtility.getAttributeValue(currentResource, "identifier");
            if(id.equals(idrefValue))
            {
                String type = DOMTreeUtility.getAttributeValue(currentResource, "scormType");
                if(!type.equalsIgnoreCase("sco"))
                {
                    result = false;
                    String msgText = "The <item> shall reference a SCO resource only for ID/IDREF " + idrefValue;
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                }
            }
        }

        return result;
    }

    private boolean checkItemIdentifierRef(Node iOrgNode)
    {
        mLogger.entering("CPValidator", "checkItemIdentifierRef");
        String msgText = new String();
        NodeList orgChildren = iOrgNode.getChildNodes();
        int orgChildSize = orgChildren.getLength();
        boolean result = true;
        for(int j = 0; j < orgChildSize; j++)
        {
            Node currentNode = orgChildren.item(j);
            String currentName = currentNode.getNodeName();
            if(currentName.equals("item"))
            {
                NodeList itemChildren = currentNode.getChildNodes();
                int itemChildrenSize = itemChildren.getLength();
                for(int k = 0; k < itemChildrenSize; k++)
                {
                    Node currentItemChild = itemChildren.item(k);
                    String currentItemChildName = currentItemChild.getNodeName();
                    if(currentItemChildName != null && currentItemChildName.equals("item"))
                    {
                        NamedNodeMap attrList = currentNode.getAttributes();
                        int numAttr = attrList.getLength();
                        for(int i = 0; i < numAttr; i++)
                        {
                            Attr currentAttrNode = (Attr)attrList.item(i);
                            String currentNodeName = currentAttrNode.getLocalName();
                            if(currentNodeName.equals("identifierref"))
                            {
                                result = false;
                                msgText = "The \"identifierref\" attribute shall not be used on a non-leaf <item> element";
                                mLogger.info("FAILED: " + msgText);
                                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                            }
                        }

                    }
                }

            }
        }

        return result;
    }

    private boolean checkItemChildMultiplicity(Node iNode, ManifestMap iManifestInfo)
    {
        mLogger.entering("CPValidator", "checkTitleMultiplicity");
        String msgText = new String();
        boolean result = true;
        String iNodeName = iNode.getNodeName();
        if(iNodeName.equals("organization"))
        {
            NodeList iNodeChildren = iNode.getChildNodes();
            int iNodeChildSize = iNodeChildren.getLength();
            for(int j = 0; j < iNodeChildSize; j++)
            {
                Node currentNode = iNodeChildren.item(j);
                String currentName = currentNode.getNodeName();
                if(currentName.equals("item"))
                    result = checkItemChildMultiplicity(currentNode, iManifestInfo) && result;
            }

        }
        if(iNodeName.equals("item"))
        {
            boolean itemPointToSubmanifest = false;
            Vector subManifestIdrefs = iManifestInfo.getSubManifestIDrefs();
            int subIdrefSize = subManifestIdrefs.size();
            NamedNodeMap attrList = iNode.getAttributes();
            int numAttr = attrList.getLength();
            for(int i = 0; i < numAttr; i++)
            {
                Attr currentAttrNode = (Attr)attrList.item(i);
                String currentNodeName = currentAttrNode.getLocalName();
                if(currentNodeName.equals("identifierref"))
                {
                    String identifierrefValue = currentAttrNode.getValue();
                    for(int k = 0; k < subIdrefSize; k++)
                    {
                        String currentIdref = (String)subManifestIdrefs.elementAt(k);
                        if(currentIdref.equals(identifierrefValue))
                            itemPointToSubmanifest = true;
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
            for(int v = 0; v < itemSize; v++)
            {
                Node currentItemChild = itemChild.item(v);
                currentItemChildName = currentItemChild.getLocalName();
                if(currentItemChildName != null)
                {
                    if(currentItemChildName.equals("title"))
                        titleFound = true;
                    if(currentItemChildName.equals("timeLimitAction"))
                        timeLimitActionFound = true;
                    if(currentItemChildName.equals("dataFromLMS"))
                        dataFromLMSFound = true;
                    if(currentItemChildName.equals("presentation"))
                        presentationFound = true;
                    if(currentItemChildName.equals("sequencing"))
                        sequencingFound = true;
                    if(currentItemChildName.equals("completionThreshold"))
                        thresholdFound = true;
                    if(currentItemChildName.equals("item"))
                        result = checkItemChildMultiplicity(currentItemChild, iManifestInfo) && result;
                }
            }

            if(itemPointToSubmanifest)
            {
                if(titleFound)
                {
                    msgText = "Element <title> is not permitted on a leaf item that references a (sub)manifest";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
                if(timeLimitActionFound || dataFromLMSFound || presentationFound || thresholdFound)
                {
                    msgText = "ADL Content Packaging namespaced elements are not permitted on a leaf item that references a (sub)manifest";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
                if(sequencingFound)
                {
                    msgText = "Sequencing information can not be defined on a leaf item that references a (sub)manifest";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            } else
            if(!titleFound)
            {
                msgText = "Mandatory element <title> could not be found";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            }
        }
        return result;
    }

    private boolean checkHref(String iURIString, boolean doApplyXMLBase)
    {
        mLogger.entering("CPValidator", "checkHref()");
        mLogger.info("iURISting is " + iURIString);
        boolean result = true;
        String msgText = new String();
        if(!iURIString.equals(""))
        {
            StringTokenizer token = new StringTokenizer(iURIString, ":");
            if(iURIString.substring(0, 5).equals("http:") || iURIString.substring(0, 6).equals("https:") || iURIString.substring(0, 4).equals("ftp:") || iURIString.substring(0, 5).equals("ftps:"))
                try
                {
                    URL url = new URL(iURIString);
                    java.net.URLConnection urlConn = url.openConnection();
                    HttpURLConnection httpUrlConn = (HttpURLConnection)urlConn;
                    int code = httpUrlConn.getResponseCode();
                    if(code == 200)
                    {
                        msgText = "URL \"" + iURIString + "\" has been detected";
                        mLogger.info("PASSED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                    } else
                    if(doApplyXMLBase)
                    {
                        msgText = "URL  \"" + iURIString + "\" could not be detected";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    } else
                    {
                        msgText = "URL  \"" + iURIString + "\" will not be " + "detected due to unclairity in the " + "SCORM of applying xml:base within " + "(Sub) manifest elements";
                        mLogger.info("WARNING: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                    }
                }
                catch(MalformedURLException mfue)
                {
                    mLogger.fine("MalformedURLException thrown when creating URL with \"" + iURIString + "\"");
                    msgText = "URL  \"" + iURIString + "\" is malformed";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
                catch(IOException ioe)
                {
                    mLogger.fine("IOException thrown when opening a connection to \"" + iURIString + "\"");
                    msgText = "Could not open a URL connection to  \"" + iURIString + "\"";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            else
            if(iURIString.substring(0, 5).equals("file:"))
            {
                msgText = "File  \"" + iURIString + "\" is referenced to the " + "local file system.  The \"href\" attribute must " + "reference a file that is local to the package or " + "reference an external file";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            } else
            if(iURIString.charAt(0) == '/')
            {
                msgText = "File  \"" + iURIString + "\" is referenced to the " + "users home directory.  The \"href\" attribute must " + "reference a file that is local to the package or " + "reference an external file";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            } else
            {
                String absolutePath = getBaseDirectory() + iURIString;
                mLogger.info("Absolute path is " + absolutePath);
                int queryIndex = absolutePath.indexOf('?');
                if(queryIndex > 0)
                    absolutePath = absolutePath.substring(0, queryIndex);
                int fragmentIndex = absolutePath.indexOf('#');
                if(fragmentIndex > 0)
                    absolutePath = absolutePath.substring(0, fragmentIndex);
                URLDecoder urlDecoder = new URLDecoder();
                try
                {
                    absolutePath = URLDecoder.decode(absolutePath, "UTF-8");
                }
                catch(UnsupportedEncodingException uee)
                {
                    mLogger.severe("UnsupportedEncodingException thrown while decoding the file path.");
                    uee.printStackTrace();
                }
                try
                {
                    File fileToFind = new File(absolutePath);
                    if(fileToFind.isFile())
                    {
                        msgText = "File \"" + iURIString + "\" has been detected";
                        mLogger.info("PASSED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                    } else
                    if(doApplyXMLBase)
                    {
                        msgText = "File  \"" + iURIString + "\" could not be detected";
                        mLogger.info("FAILED: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                        result = false;
                    } else
                    {
                        msgText = "File  \"" + iURIString + "\" will not be " + "detected.  At this time, SCORM does not " + "impose requirements on how to handle " + "xml:base in conjunction with (Sub) manifest " + "elements";
                        mLogger.info("WARNING: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                    }
                }
                catch(NullPointerException npe)
                {
                    mLogger.severe("NullPointerException thrown when accessing " + absolutePath);
                    msgText = "File \"" + iURIString + "\" could not be detected";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
                catch(SecurityException se)
                {
                    mLogger.severe("SecurityException thrown when accessing " + absolutePath);
                    msgText = "File \"" + iURIString + "\" could not be detected";
                    mLogger.info("FAILED: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                    result = false;
                }
            }
        }
        return result;
    }

    private boolean checkSPMConformance(String iElementName, String iElementValue, int iSPMRule, boolean iAmAnAttribute)
    {
        boolean result = true;
        String msgText = new String();
        int elementValueLength = iElementValue.length();
        if(iSPMRule != -1)
        {
            if(elementValueLength > iSPMRule)
            {
                if(iAmAnAttribute)
                    msgText = "The text contained in attribute \"" + iElementName + "\" is greater than " + iSPMRule + ".";
                else
                    msgText = "The text contained in element <" + iElementName + "> is greater than " + iSPMRule + ".";
                mLogger.info("WARNING: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
            } else
            if(elementValueLength < 1)
            {
                if(iAmAnAttribute)
                    msgText = "No text was found in attribute \"" + iElementName + "\" and fails the minimum character length test";
                else
                    msgText = "No text was found in element <" + iElementName + "> and fails the minimum character length test";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            } else
            {
                if(iAmAnAttribute)
                    msgText = "Character length for attribute \"" + iElementName + "\" has been verified";
                else
                    msgText = "Character length for element <" + iElementName + "> has been verified";
                mLogger.info("PASSED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            }
        } else
        if(elementValueLength < 1)
        {
            if(iAmAnAttribute)
                msgText = "No text was found in attribute \"" + iElementName + "\" and fails the minimum character length test";
            else
                msgText = "No text was found in element <" + iElementName + "> and fails the minimum character length test";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false;
        } else
        {
            if(iAmAnAttribute)
                msgText = "Character length for attribute \"" + iElementName + "\" has been verified";
            else
                msgText = "Character length for element <" + iElementName + "> has been verified";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        }
        return result;
    }

    private boolean checkVocabulary(String iName, String iValue, Vector iVocabValues, boolean iAmAnAttribute)
    {
        mLogger.entering("CPValidator", "checkVocabulary()");
        boolean result = false;
        for(int i = 0; i < iVocabValues.size(); i++)
            if(iValue.equals((String)iVocabValues.elementAt(i)))
                result = true;

        if(result)
        {
            String msgText;
            if(iAmAnAttribute)
                msgText = "\"" + iValue + "\"  is a valid value for attribute \"" + iName + "\"";
            else
                msgText = "\"" + iValue + "\"  is a valid value for element <" + iName + ">";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        } else
        {
            String msgText;
            if(iAmAnAttribute)
                msgText = "\"" + iValue + "\"  is not a valid value for " + "attribute \"" + iName + "\"";
            else
                msgText = "\"" + iValue + "\"  is not a valid value for element <" + iName + ">";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
        }
        mLogger.exiting("CPValidator", "checkVocabulary()");
        return result;
    }

    public int getMultiplicityUsed(Node iParentNode, String iNodeName)
    {
        NodeList kids = iParentNode.getChildNodes();
        int count = 0;
        int kidsLength = kids.getLength();
        for(int i = 0; i < kidsLength; i++)
            if(kids.item(i).getNodeType() == 1)
            {
                String currentNodeName = kids.item(i).getLocalName();
                if(currentNodeName.equalsIgnoreCase(iNodeName))
                    count++;
            }

        return count;
    }

    public int getMultiplicityUsed(NamedNodeMap iAttributeMap, String iNodeName)
    {
        int result = 0;
        int length = iAttributeMap.getLength();
        for(int i = 0; i < length; i++)
        {
            String currentName = ((Attr)iAttributeMap.item(i)).getLocalName();
            if(currentName.equals(iNodeName))
                result++;
        }

        return result;
    }

    private String getBaseDirectory()
    {
        return mBaseDirectory;
    }

    private boolean doesXMLBaseExist()
    {
        boolean xmlBaseExists = true;
        if(mXMLBase[0][1].equals("") && mXMLBase[1][1].equals("") && mXMLBase[2][1].equals(""))
            xmlBaseExists = false;
        return xmlBaseExists;
    }

    private String applyXMLBase(String hrefValue)
    {
        return mXMLBase[0][1] + mXMLBase[1][1] + mXMLBase[2][1] + hrefValue;
    }

    public Vector getLaunchData(boolean iDefaultOrganizationOnly, boolean iRemoveAssets)
    {
        return mManifestHandler.getLaunchData(super.getDocument().getDocumentElement(), iDefaultOrganizationOnly, iRemoveAssets);
    }

    public Vector getLaunchData(Document iRolledUpDocument, boolean iDefaultOrganizationOnly, boolean iRemoveAssets)
    {
        return mManifestHandler.getLaunchData(iRolledUpDocument.getDocumentElement(), iDefaultOrganizationOnly, iRemoveAssets);
    }

    public Vector getMetadataDataList()
    {
        return mManifestHandler.getMetadata(super.getDocument().getDocumentElement(), mBaseDirectory);
    }

    public void setPerformValidationToSchema(boolean iValue)
    {
        mPerformFullValidation = iValue;
    }

}