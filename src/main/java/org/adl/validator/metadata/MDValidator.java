
package org.adl.validator.metadata;

import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Logger;
import org.adl.util.*;
import org.adl.validator.ADLSCORMValidator;
import org.adl.validator.RulesValidator;
import org.w3c.dom.*;

public class MDValidator extends ADLSCORMValidator implements Serializable
{
    private Logger mLogger;
    private RulesValidator mMetadataRulesValidator;
    private String mTypeValue;
    private String mNameValue;
    private boolean mMetadataSchemaTracked;
    private Vector mMetadataSchemaNodeList;
    private String mApplicationProfileType;
    private String mEnvironmentVariable;

    public MDValidator(String iApplicationProfileType, String iEnvironmentVariable)
    {
        super("metadata");
        mLogger = Logger.getLogger("org.adl.util.debug.validator");
        mMetadataRulesValidator = new RulesValidator("metadata");
        mTypeValue = new String();
        mNameValue = new String();
        mApplicationProfileType = iApplicationProfileType;
        mEnvironmentVariable = iEnvironmentVariable;
        mMetadataSchemaNodeList = new Vector();
        mMetadataSchemaTracked = false;
    }

    public boolean validate(String iXMLFileName)
    {
        boolean validateResult = true;
        mLogger.entering("MDValidator", "validate(iXMLFileName)");
        mLogger.finer("iXMLFileName coming in is " + iXMLFileName);
        String msgText = "Testing the metadata for Well-formedness";
        mLogger.info("INFO: " + msgText);
        validateResult = checkWellformedness(iXMLFileName) && validateResult;
        boolean wellformed = super.getIsWellformed();
        msgText = "Testing the metadata for Validity to the Controlling Documents";
        mLogger.info("INFO: " + msgText);
        validateResult = checkSchema(iXMLFileName) && validateResult;
        if(wellformed)
            if(!mApplicationProfileType.equals("other"))
            {
                msgText = "****************************************";
                mLogger.info("OTHER: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.OTHER, msgText));
                msgText = "Validating the metadata against the Application Profile Rules";
                mLogger.info("INFO: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                mMetadataRulesValidator.readInRules(mApplicationProfileType, mEnvironmentVariable);
                boolean isAppProfileResult = compareToRules(super.getDocument(), "");
                super.setIsValidToApplicationProfile(isAppProfileResult);
                validateResult = isAppProfileResult && validateResult;
            } else
            {
                super.setIsValidToApplicationProfile(true);
            }
        mLogger.exiting("MDValidator", "validate()");
        return validateResult;
    }

    public boolean validate(Node iRootLOMNode, boolean iDidValidationToSchemaPass)
    {
        boolean validateResult = true;
        mLogger.entering("MDValidator", "validate(iRootLOMNode)");
        String msgText = "Testing the metadata for Well-formedness";
        mLogger.info("INFO: " + msgText);
        msgText = "The instance is well-formed ";
        mLogger.info(msgText);
        MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        super.setIsWellformed(true);
        if(iDidValidationToSchemaPass)
        {
            msgText = "The instance is valid against the controlling documents";
            mLogger.info(msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            super.setIsValidToSchema(true);
        } else
        {
            msgText = "The instance is NOT valid against the controlling documents";
            mLogger.info(msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            msgText = "Schema validation for in-line metadata is performed  during the manifest schema validation.  Since the manifest failed, it is assumed that inline metadata failed. Please see the manifest detailed log for details.";
            mLogger.info(msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
        }
        if(!mApplicationProfileType.equals("other"))
        {
            msgText = "****************************************";
            mLogger.info("OTHER: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.OTHER, msgText));
            msgText = "Validating the metadata against the Application Profile Rules";
            mLogger.info("INFO: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
            mMetadataRulesValidator.readInRules(mApplicationProfileType, mEnvironmentVariable);
            boolean isAppProfileResult = compareToRules(iRootLOMNode, "");
            super.setIsValidToApplicationProfile(isAppProfileResult);
            validateResult = isAppProfileResult && validateResult;
        } else
        {
            super.setIsValidToApplicationProfile(true);
        }
        mLogger.exiting("MDValidator", "validate()");
        return validateResult;
    }

    private boolean checkWellformedness(String iXMLFileName)
    {
        boolean wellnessResult = true;
        super.setPerformFullValidation(false);
        super.performValidatorParse(iXMLFileName);
        mLogger.info("************************************");
        mLogger.info(" WELLFORMED Result is " + super.getIsWellformed());
        mLogger.info("************************************");
        if(!super.getIsWellformed())
            wellnessResult = false;
        return wellnessResult;
    }

    private boolean checkSchema(String iXMLFileName)
    {
        boolean schemaResult = true;
        super.setPerformFullValidation(true);
        super.performValidatorParse(iXMLFileName);
        mLogger.info("************************************");
        mLogger.info(" VALIDSCHEMA Result is " + super.getIsValidToSchema());
        mLogger.info(" mIsExtensionsUsed is " + super.getIsExtensionsUsed());
        mLogger.info("************************************");
        if(!super.getIsValidToSchema())
            schemaResult = false;
        return schemaResult;
    }

    private boolean checkDataTypes(String iCurrentChildName, String iDataType, Node iCurrentChild, String iPath, String appProfileType)
    {
        String msgText = new String();
        boolean result = true;
        if(iDataType.equalsIgnoreCase("parent"))
            result = compareToRules(iCurrentChild, iPath);
        else
        if(iDataType.equalsIgnoreCase("langstring") && !appProfileType.equals("contentaggregation"))
        {
            msgText = "Testing element <" + iCurrentChildName + "> for the LangString Type";
            mLogger.info("INFO: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
            result = checkLangString(iCurrentChild);
        } else
        if((iDataType.equalsIgnoreCase("datetime") || iDataType.equalsIgnoreCase("duration")) && !appProfileType.equals("contentaggregation"))
        {
            msgText = "Testing element <" + iCurrentChildName + "> for the " + iDataType + " Type";
            mLogger.info("INFO: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
            result = checkDatetimeOrDurationPair(iCurrentChild, iCurrentChildName, iDataType);
        } else
        if(iDataType.equalsIgnoreCase("nametypepair") && !appProfileType.equals("contentaggregation"))
        {
            msgText = "Testing element <" + iCurrentChildName + "> for the vocabulary type";
            mLogger.info("INFO: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
            result = checkSourceValuePair(iCurrentChild, iCurrentChildName, "bestpractice", iPath);
            checkNameTypePair(iCurrentChildName);
        } else
        if(iDataType.equalsIgnoreCase("text") && !appProfileType.equals("contentaggregation"))
            result = checkText(iCurrentChild, iCurrentChildName, iPath) && result;
        else
        if(iDataType.equalsIgnoreCase("bestpracticevocabulary") && !appProfileType.equals("contentaggregation"))
        {
            msgText = "Testing element <" + iCurrentChildName + "> for the vocabulary type";
            mLogger.info("INFO: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
            result = checkSourceValuePair(iCurrentChild, iCurrentChildName, "bestpractice", iPath);
        } else
        if(iDataType.equalsIgnoreCase("restrictedvocabulary") && !appProfileType.equals("contentaggregation"))
        {
            msgText = "Testing element <" + iCurrentChildName + "> for the Vocabulary Type";
            mLogger.info("INFO: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
            result = checkSourceValuePair(iCurrentChild, iCurrentChildName, "restricted", iPath);
        }
        return result;
    }

    private boolean compareToRules(Node iTestSubjectNode, String iPath)
    {
        mLogger.entering("MDValidator", "compareToRules");
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
            if(!mApplicationProfileType.equalsIgnoreCase("contentaggregation"))
            {
                msgText = "Testing element <" + rootNodeName + "> for minimum comformance";
                mLogger.info("INFO: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                msgText = "Multiplicity for element <" + rootNodeName + "> has been verified";
                mLogger.info("PASSED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            } else
            if(rootNodeName.equals("metadataSchema") || rootNodeName.equals("metaMetadata"))
            {
                msgText = "Testing element <" + rootNodeName + "> for minimum comformance";
                mLogger.info("INFO: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                msgText = "Multiplicity for element <" + rootNodeName + "> has been verified";
                mLogger.info("PASSED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            }
            result = compareToRules(rootNode, "") && result;
            break;

        case 1: // '\001'
            String parentNodeName = iTestSubjectNode.getLocalName();
            String dataType = null;
            int multiplicityUsed = -1;
            int minRule = -1;
            int maxRule = -1;
            int spmRule = -1;
            NodeList children = iTestSubjectNode.getChildNodes();
            if(children == null)
                break;
            int numChildren = children.getLength();
            String path;
            if(iPath.equals("") || parentNodeName.equalsIgnoreCase("lom"))
                path = parentNodeName;
            else
                path = iPath + "." + parentNodeName;
            if(parentNodeName.equalsIgnoreCase("lom"))
            {
                if(mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("asset") || mApplicationProfileType.equals("contentorganization"))
                {
                    result = checkForMandatory(iTestSubjectNode, "general") && result;
                    result = checkForMandatory(iTestSubjectNode, "technical") && result;
                    result = checkForMandatory(iTestSubjectNode, "rights") && result;
                }
                if(mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("contentorganization"))
                    result = checkForMandatory(iTestSubjectNode, "lifeCycle") && result;
                result = checkForMandatory(iTestSubjectNode, "metaMetadata") && result;
            } else
            if(parentNodeName.equalsIgnoreCase("general") && (mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("contentorganization")))
            {
                result = checkForMandatory(iTestSubjectNode, "identifier") && result;
                result = checkForMandatory(iTestSubjectNode, "title") && result;
                result = checkForMandatory(iTestSubjectNode, "description") && result;
                if(mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("contentorganization"))
                    result = checkForMandatory(iTestSubjectNode, "keyword") && result;
            } else
            if(parentNodeName.equalsIgnoreCase("lifeCycle"))
            {
                if(mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("contentorganization"))
                {
                    result = checkForMandatory(iTestSubjectNode, "version") && result;
                    result = checkForMandatory(iTestSubjectNode, "status") && result;
                }
            } else
            if(parentNodeName.equalsIgnoreCase("metaMetadata"))
            {
                if(mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("contentorganization"))
                    result = checkForMandatory(iTestSubjectNode, "identifier") && result;
                result = checkForMandatorySchemas(iTestSubjectNode, "metadataSchema") && result;
                NodeList metaMetadataChildren = iTestSubjectNode.getChildNodes();
                if(metaMetadataChildren != null)
                {
                    int metaMetadataChildrenLength = metaMetadataChildren.getLength();
                    for(int i = 0; i < metaMetadataChildrenLength; i++)
                    {
                        Node currentChild = metaMetadataChildren.item(i);
                        if(currentChild.getLocalName().equals("metadataSchema"))
                            mMetadataSchemaNodeList.add(currentChild);
                    }

                }
            } else
            if(parentNodeName.equalsIgnoreCase("technical") && (mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("contentorganization")))
                result = checkForMandatory(iTestSubjectNode, "format") && result;
            else
            if(parentNodeName.equalsIgnoreCase("rights") && (mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("contentorganization")))
            {
                result = checkForMandatory(iTestSubjectNode, "cost") && result;
                result = checkForMandatory(iTestSubjectNode, "copyrightAndOtherRestrictions") && result;
            } else
            if(parentNodeName.equalsIgnoreCase("identifier") && (mApplicationProfileType.equals("sco") || mApplicationProfileType.equals("activity") || mApplicationProfileType.equals("contentorganization")))
                result = checkForMandatory(iTestSubjectNode, "entry") && result;
            for(int z = 0; z < numChildren; z++)
            {
                Node currentChild = children.item(z);
                String currentChildName = currentChild.getLocalName();
                dataType = mMetadataRulesValidator.getRuleValue(currentChildName, path, "datatype");
                if(dataType.equalsIgnoreCase("parent") || dataType.equalsIgnoreCase("langstring") || dataType.equalsIgnoreCase("text") || dataType.equalsIgnoreCase("restrictedvocabulary") || dataType.equalsIgnoreCase("bestpracticevocabulary") || dataType.equalsIgnoreCase("nametypepair") || dataType.equalsIgnoreCase("datetime") || dataType.equalsIgnoreCase("metadataschema") || dataType.equalsIgnoreCase("duration"))
                {
                    multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, currentChildName);
                    minRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue(currentChildName, path, "min"));
                    maxRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue(currentChildName, path, "max"));
                    if(!mApplicationProfileType.equals("contentaggregation"))
                    {
                        msgText = "Testing element <" + currentChildName + "> for minimum comformance";
                        mLogger.info("INFO: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                        result = checkMultiplicity(multiplicityUsed, minRule, maxRule, currentChildName) && result;
                    } else
                    if(currentChildName.equals("metadataSchema") || currentChildName.equals("metaMetadata"))
                    {
                        msgText = "Testing element <" + currentChildName + "> for minimum comformance";
                        mLogger.info("INFO: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                        result = checkMultiplicity(multiplicityUsed, minRule, maxRule, currentChildName) && result;
                    }
                    if(dataType.equals("metadataschema") && !mMetadataSchemaTracked)
                        result = checkMetadataSchema() && result;
                    else
                        result = checkDataTypes(currentChildName, dataType, currentChild, path, mApplicationProfileType) && result;
                }
            }

            break;
        }
        mLogger.exiting("MDValidator", "compareToRules()");
        return result;
    }

    private boolean checkSPMConformance(String iElementName, String iElementValue, int iSPMRule)
    {
        boolean result = true;
        String msgText = new String();
        int elementValueLength = iElementValue.length();
        if(iSPMRule != -1)
        {
            if(elementValueLength > iSPMRule)
            {
                msgText = "The text contained in element <" + iElementName + "> is greater than " + iSPMRule + ".";
                mLogger.info("WARNING: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
            } else
            if(elementValueLength < 1)
            {
                msgText = "No text was found in element <" + iElementName + "> and fails the minimum character length test";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            } else
            {
                msgText = "Character length for element <" + iElementName + "> has been verified";
                mLogger.info("PASSED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            }
        } else
        if(elementValueLength < 1)
        {
            msgText = "No text was found in element <" + iElementName + "> and fails the minimum character length test";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false;
        } else
        {
            msgText = "Character length for element <" + iElementName + "> has been verified";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        }
        return result;
    }

    private boolean checkVocabulary(String iName, String iValue, Vector iVocabValues)
    {
        mLogger.entering("MDValidator", "checkVocabulary()");
        boolean result = false;
        int iVocabValuesSize = iVocabValues.size();
        for(int i = 0; i < iVocabValuesSize; i++)
            if(iValue.equals((String)iVocabValues.elementAt(i)))
                result = true;

        if(result)
        {
            String msgText = "\"" + iValue + "\"  is a valid value for element <" + iName + ">";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        } else
        {
            String msgText = "\"" + iValue + "\"  is not a valid value for element <" + iName + ">";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
        }
        mLogger.exiting("MDValidator", "checkVocabulary()");
        return result;
    }

    private boolean checkSPMAttributeConformance(String iAttributeName, String iAttributeValue, int iSPMRule)
    {
        boolean result = true;
        String msgText = new String();
        int attributeValueLength = iAttributeValue.length();
        if(iSPMRule != -1)
        {
            if(attributeValueLength > iSPMRule)
            {
                msgText = "The text contained in attribute \"" + iAttributeName + "\" is greater than " + iSPMRule + ".";
                mLogger.info("WARNING: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
            } else
            if(attributeValueLength < 1)
            {
                msgText = "No text was found in attribute \"" + iAttributeName + "\" and fails the minimum character length test";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            } else
            {
                msgText = "Character length for attribute \"" + iAttributeName + "\" has been verified";
                mLogger.info("PASSED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            }
        } else
        if(attributeValueLength < 1)
        {
            msgText = "No text was found in attribute \"" + iAttributeName + "\" and fails the minimum character length test";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false;
        } else
        {
            msgText = "Character length for attribute \"" + iAttributeName + "\" has been verified";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        }
        return result;
    }

    private int getMultiplicityUsed(Node iParentNode, String iNodeName)
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

    private boolean checkForMandatory(Node iTestSubjectNode, String iNodeName)
    {
        boolean result = true;
        String msgText = new String();
        int multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, iNodeName);
        if(multiplicityUsed < 1)
        {
            msgText = "Mandatory element <" + iNodeName + "> does not exist";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false;
        }
        return result;
    }

    private boolean checkForMandatorySchemas(Node iTestSubjectNode, String iNodeName)
    {
        boolean result = true;
        String msgText = new String();
        int multiplicityUsed = getMultiplicityUsed(iTestSubjectNode, iNodeName);
        if(multiplicityUsed < 2)
        {
            msgText = "Mandatory elements <" + iNodeName + "> does not exist " + "two or more times";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
            result = false;
        }
        return result;
    }

    private boolean checkMultiplicity(int iMultiplicityUsed, int iMinRule, int iMaxRule, String iElementName)
    {
        boolean result = true;
        String msgText = new String();
        if(iMinRule != -1 && iMaxRule != -1)
        {
            if(iMultiplicityUsed >= iMinRule && iMultiplicityUsed <= iMaxRule)
            {
                msgText = "Multiplicity for element <" + iElementName + "> has been verified";
                mLogger.info("PASSED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            } else
            if(iMaxRule > 1 || iMaxRule > 2)
            {
                msgText = "The <" + iElementName + "> element is not within the SPM multiplicity bounds.";
                mLogger.info("WARNING: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
            } else
            {
                msgText = "The <" + iElementName + "> element is not within the multiplicity bounds.";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            }
        } else
        if(iMinRule != -1 && iMaxRule == -1)
            if(iMultiplicityUsed >= iMinRule)
            {
                msgText = "Multiplicity for element <" + iElementName + "> has been verified";
                mLogger.info("PASSED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
            } else
            {
                msgText = "The <" + iElementName + "> element is not within the multiplicity bounds.";
                mLogger.info("FAILED: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
                result = false;
            }
        return result;
    }

    private boolean checkLangString(Node iCurrentLangstringElement)
    {
        boolean result = true;
        String msgText = new String();
        int multiplicityUsed = -1;
        int minRule = -1;
        int maxRule = -1;
        int spmRule = -1;
        String langstringElement = "string";
        String stringValue = new String();
        String currentLangstringElementName = iCurrentLangstringElement.getLocalName();
        boolean stringElementExists = checkForMandatory(iCurrentLangstringElement, "string");
        if(stringElementExists)
        {
            multiplicityUsed = getMultiplicityUsed(iCurrentLangstringElement, "string");
            minRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue("string", "", "min"));
            maxRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue("string", "", "max"));
            result = checkMultiplicity(multiplicityUsed, minRule, maxRule, "string") && result;
            NodeList langStringChild = iCurrentLangstringElement.getChildNodes();
            int numChild = langStringChild.getLength();
            for(int i = 0; i < numChild; i++)
            {
                Node stringNode = langStringChild.item(i);
                String stringName = stringNode.getLocalName();
                if(stringName.equals("string"))
                {
                    NamedNodeMap attrList = stringNode.getAttributes();
                    int numAttr = attrList.getLength();
                    for(int j = 0; j < numAttr; j++)
                    {
                        Attr stringAttribute = (Attr)attrList.item(j);
                        String attributeName = stringAttribute.getName();
                        String attributeValue = null;
                        if(attributeName.equals("language"))
                        {
                            msgText = "Testing attribute \"language\" of the LangString Type for minimum comformance";
                            mLogger.info("INFO: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                            msgText = "Multiplicity for attribute \"language\" has been verified";
                            mLogger.info("PASSED: " + msgText);
                            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
                            spmRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue(langstringElement, "", "spm", attributeName));
                            attributeValue = stringAttribute.getValue();
                            result = checkSPMAttributeConformance(attributeName, attributeValue, spmRule) && result;
                        }
                    }

                    spmRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue(currentLangstringElementName, "", "spm"));
                    stringValue = mMetadataRulesValidator.getTaggedData(stringNode);
                    result = checkSPMConformance(langstringElement, stringValue, spmRule) && result;
                }
            }

        } else
        {
            result = stringElementExists && result;
        }
        return result;
    }

    private boolean checkText(Node iTextElement, String iTextElementName, String iPath)
    {
        int spmRule = -1;
        boolean result = true;
        String textElementValue = mMetadataRulesValidator.getTaggedData(iTextElement);
        spmRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue(textElementValue, iPath, "spm"));
        result = checkSPMConformance(iTextElementName, textElementValue, spmRule) && result;
        return result;
    }

    private boolean checkMetadataSchema()
    {
        mLogger.entering("MDValidator", "checkMetadataSchema()");
        boolean result = false;
        String currentElementName = "metadataSchema";
        boolean foundLOMSchema = false;
        boolean foundSCORMCAMSchema = false;
        String msgText = "Testing element \"" + currentElementName + "\" for SCORM schema values";
        mLogger.info("INFO: " + msgText);
        MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
        Vector vocabValues = mMetadataRulesValidator.getVocabRuleValues(currentElementName, "lom.metaMetadata");
        int mMetadataSchemaNodeListSize = mMetadataSchemaNodeList.size();
        int vocabVectorSize = vocabValues.size();
        for(int i = 0; i < mMetadataSchemaNodeListSize; i++)
        {
            Node currentMetadataSchemaNode = (Node)mMetadataSchemaNodeList.elementAt(i);
            String currentMetadataSchemaValue = mMetadataRulesValidator.getTaggedData(currentMetadataSchemaNode);
            int vocabValuesSize = vocabValues.size();
            for(int j = 0; j < vocabValuesSize; j++)
            {
                String currentVocabToken = (String)vocabValues.elementAt(j);
                if(!currentMetadataSchemaValue.equals(currentVocabToken))
                    continue;
                if(currentVocabToken.equals("LOMv1.0"))
                {
                    foundLOMSchema = true;
                    mLogger.finer("Found LOMv1.0");
                    break;
                }
                if(!currentVocabToken.equals("SCORM_CAM_v1.3"))
                    continue;
                foundSCORMCAMSchema = true;
                mLogger.finer("Found SCORM_CAM_v1.3");
                break;
            }

        }

        if(foundLOMSchema)
        {
            msgText = "\" LOMv1.0 \" was detected for element <metadataSchema>";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        } else
        {
            msgText = "\" LOMv1.0 \" was not detected for element <metadataSchema>";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
        }
        if(foundSCORMCAMSchema)
        {
            msgText = "\" SCORM_CAM_v1.3 \" was detected for element <metadataSchema>";
            mLogger.info("PASSED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.PASSED, msgText));
        } else
        {
            msgText = "\" SCORM_CAM_v1.3 \" was not detected for element <metadataSchema>";
            mLogger.info("FAILED: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.FAILED, msgText));
        }
        mMetadataSchemaTracked = true;
        mLogger.exiting("MDValidator", "checkMetadataSchema");
        result = foundLOMSchema && foundSCORMCAMSchema;
        return result;
    }

    private boolean checkDatetimeOrDurationPair(Node iElement, String iElementName, String typeOfPair)
    {
        boolean result = true;
        boolean descriptionExists = false;
        int spmRule = -1;
        String msgText = "";
        String stringValue = new String();
        if(typeOfPair.equals("datetime"))
            result = checkForMandatory(iElement, "dateTime") && result;
        else
        if(typeOfPair.equals("duration"))
            result = checkForMandatory(iElement, "duration") && result;
        int multiplicityUsed = getMultiplicityUsed(iElement, "description");
        if(multiplicityUsed > 0)
            descriptionExists = true;
        if(result)
        {
            NodeList children = iElement.getChildNodes();
            int numChildren = children.getLength();
            for(int i = 0; i < numChildren; i++)
            {
                Node currentChild = children.item(i);
                String currentChildName = currentChild.getLocalName();
                if(currentChildName.equals("dateTime") || currentChildName.equals("duration"))
                {
                    String currentChildValue = mMetadataRulesValidator.getTaggedData(currentChild);
                    spmRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue(currentChildName, "", "spm"));
                    result = checkSPMConformance(currentChildName, currentChildValue, spmRule) && result;
                }
            }

        }
        if(descriptionExists)
        {
            NodeList children = iElement.getChildNodes();
            int numChildren = children.getLength();
            for(int i = 0; i < numChildren; i++)
            {
                Node currentChild = children.item(i);
                String currentChildName = currentChild.getLocalName();
                if(currentChildName.equals("description"))
                {
                    msgText = "Testing element <" + currentChildName + "> for the LangString Type";
                    mLogger.info("INFO: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.INFO, msgText));
                    result = checkLangString(currentChild) && result;
                }
            }

        }
        return result;
    }

    private boolean checkSourceValuePair(Node iVocabularyElement, String iElementName, String iVocabularyType, String iPath)
    {
        boolean result = true;
        int spmRule = -1;
        String msgText = "";
        String vocabularyElementName = iVocabularyElement.getLocalName();
        boolean checkBestPracticeVocabulary = true;
        result = checkForMandatory(iVocabularyElement, "source") && result;
        result = checkForMandatory(iVocabularyElement, "value") && result;
        if(result)
        {
            NodeList vocabularyChildren = iVocabularyElement.getChildNodes();
            int numVocabChildren = vocabularyChildren.getLength();
            for(int i = 0; i < numVocabChildren; i++)
            {
                Node currentVocabChild = vocabularyChildren.item(i);
                String currentVocabChildName = currentVocabChild.getLocalName();
                if(currentVocabChildName.equals("source"))
                {
                    String currentVocabChildValue = mMetadataRulesValidator.getTaggedData(currentVocabChild);
                    spmRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue(currentVocabChildName, "", "spm"));
                    result = checkSPMConformance(currentVocabChildName, currentVocabChildValue, spmRule) && result;
                    if(!currentVocabChildValue.equalsIgnoreCase("LOMv1.0") && iVocabularyType.equals("bestpractice"))
                    {
                        msgText = "<" + iElementName + "> does not exercise " + "the best practice LOM vocabularies";
                        mLogger.info("WARNING: " + msgText);
                        MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                        checkBestPracticeVocabulary = false;
                    }
                } else
                if(currentVocabChildName.equals("value"))
                {
                    String currentVocabChildValue = mMetadataRulesValidator.getTaggedData(currentVocabChild);
                    if(vocabularyElementName.equalsIgnoreCase("type"))
                        mTypeValue = currentVocabChildValue;
                    else
                    if(vocabularyElementName.equalsIgnoreCase("name"))
                        mNameValue = currentVocabChildValue;
                    spmRule = Integer.parseInt(mMetadataRulesValidator.getRuleValue(currentVocabChildName, "", "spm"));
                    result = checkSPMConformance(currentVocabChildName, currentVocabChildValue, spmRule) && result;
                    Vector vocabValues = mMetadataRulesValidator.getVocabRuleValues(iElementName, iPath);
                    if(checkBestPracticeVocabulary)
                        result = checkVocabulary(iElementName, currentVocabChildValue, vocabValues) && result;
                }
            }

        }
        return result;
    }

    private void checkNameTypePair(String iCurrentChildName)
    {
        String msgText = new String();
        if(iCurrentChildName.equalsIgnoreCase("name"))
            if(!mTypeValue.equals(""))
            {
                if(mTypeValue.equals("operating system") && !mNameValue.equals("pc-dos") && !mNameValue.equals("ms-windows") && !mNameValue.equals("macos") && !mNameValue.equals("unix") && !mNameValue.equals("multi-os") && !mNameValue.equals("none"))
                {
                    msgText = "Element <type> and Element <" + iCurrentChildName + "> do not " + "exercise the best practice LOM " + "vocabularies";
                    mLogger.info("WARNING: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                } else
                if(mTypeValue.equals("browser") && !mNameValue.equals("any") && !mNameValue.equals("netscape communicator") && !mNameValue.equals("ms-internet explorer") && !mNameValue.equals("opera") && !mNameValue.equals("amaya"))
                {
                    msgText = "Element <type> and Element <" + iCurrentChildName + "> do not " + "exercise the best practice LOM " + "vocabularies";
                    mLogger.info("WARNING: " + msgText);
                    MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
                }
            } else
            {
                msgText = "Element <type> and Element <name> must co-exist in order to exercise the best practice LOM vocabularies";
                mLogger.info("WARNING: " + msgText);
                MessageCollection.getInstance().add(new Message(MessageType.WARNING, msgText));
            }
    }

}