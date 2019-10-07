package org.adl.validator;

import java.io.File;
import java.io.Serializable;
import java.util.logging.Logger;
import org.adl.parsers.dom.ADLDOMParser;
import org.adl.util.*;
import org.w3c.dom.*;

// Referenced classes of package org.adl.validator:
//            ADLValidatorOutcome

public class ADLSCORMValidator implements Serializable
{
    protected Document mDocument;
    protected boolean mIsWellformed;
    protected boolean mIsValidToSchema;
    protected boolean mIsValidToApplicationProfile;
    protected boolean mIsRequiredFiles;
    protected boolean mIsIMSManifestPresent;
    protected boolean mIsExtensionsUsed;
    protected String mValidatorType;
    private String mSchemaLocation;
    private Logger mLogger;
    private boolean mPerformFullValidation;

    public ADLSCORMValidator(String iValidator)
    {
        mLogger = Logger.getLogger("org.adl.util.debug.validator");
        mLogger.entering("ADLSCORMValidator", "ADLSCORMValidator()");
        mLogger.finest("      iValidator coming in is " + iValidator);
        mDocument = null;
        mIsIMSManifestPresent = true;
        mIsWellformed = false;
        mIsValidToSchema = false;
        mSchemaLocation = null;
        mIsValidToApplicationProfile = false;
        mIsExtensionsUsed = false;
        mValidatorType = iValidator;
        mLogger.exiting("ADLSCORMValidator", "ADLSCORMValidator()");
    }

    public ADLValidatorOutcome getADLValidatorOutcome()
    {
        mLogger.entering("ADLSCORMValidator", "getADLValidatorOutcome()");
        ADLValidatorOutcome outcome = new ADLValidatorOutcome(getDocument(), getIsIMSManifestPresent(), getIsWellformed(), getIsValidToSchema(), getIsValidToApplicationProfile(), getIsExtensionsUsed(), getIsRequiredFiles(), mPerformFullValidation);
        mLogger.exiting("ADLSCORMValidator", "getADLValidatorOutcome()");
        return outcome;
    }

    public void setSchemaLocation(String iSchemaLocation)
    {
        mLogger.entering("ADLSCORMValidator", "setSchemaLocation()");
        if(mSchemaLocation == null)
            mSchemaLocation = iSchemaLocation;
        else
            mSchemaLocation = mSchemaLocation + " " + iSchemaLocation;
        mLogger.finest("mSchemaLocation set to " + mSchemaLocation);
        mLogger.exiting("ADLSCORMValidator", "setSchemaLocation()");
    }

    public Document getDocument()
    {
        return mDocument;
    }

    public boolean getIsWellformed()
    {
        return mIsWellformed;
    }

    public boolean getIsValidToSchema()
    {
        return mIsValidToSchema;
    }

    public boolean getIsRequiredFiles()
    {
        return mIsRequiredFiles;
    }

    protected void setIsRequiredFiles(boolean requiredFilesResult)
    {
        mIsRequiredFiles = requiredFilesResult;
    }

    public boolean getIsIMSManifestPresent()
    {
        return mIsIMSManifestPresent;
    }

    protected void setIsIMSManifestPresent(boolean imsManifestResult)
    {
        mIsIMSManifestPresent = imsManifestResult;
    }

    public boolean getIsValidToApplicationProfile()
    {
        return mIsValidToApplicationProfile;
    }

    protected void setIsValidToApplicationProfile(boolean isValidToAppProf)
    {
        mIsValidToApplicationProfile = isValidToAppProf;
    }

    protected void setIsWellformed(boolean iIsWellformed)
    {
        mIsWellformed = iIsWellformed;
    }

    public void setIsValidToSchema(boolean iIsValidToSchema)
    {
        mIsValidToSchema = iIsValidToSchema;
    }

    public void setPerformFullValidation(boolean iPerformFullValidation)
    {
        mPerformFullValidation = iPerformFullValidation;
    }

    public boolean getIsExtensionsUsed()
    {
        return mIsExtensionsUsed;
    }

    protected void performValidatorParse(String iXMLFileName)
    {
        mLogger.entering("ADLSCORMValidator", "performValidatorParse()");
        mLogger.finest("   iXMLFileName coming in is " + iXMLFileName);
        ADLDOMParser adldomparser = new ADLDOMParser();
        if(mSchemaLocation != null)
        {
            adldomparser.setSchemaLocation(mSchemaLocation);
            if(!mPerformFullValidation)
                adldomparser.parseForWellformedness(iXMLFileName, true);
            else
                adldomparser.parseForValidation(iXMLFileName);
        } else
        {
            String msgText = "Can not parse, schema location has not been set";
            mLogger.severe("TERMINATE: " + msgText);
            MessageCollection.getInstance().add(new Message(MessageType.TERMINATE, msgText));
        }
        mDocument = adldomparser.getDocument();
        mIsWellformed = adldomparser.getIsWellformed();
        mIsValidToSchema = true;
        isExtensionsUsed(mDocument);
        Runtime.getRuntime().gc();
        mLogger.exiting("ADLSCORMValidator", "performValidatorParse()");
    }

    private void isExtensionsUsed(Node iNode)
    {
        mLogger.entering("ADLSCORMValidator", "isExtensionsUsed()");
        String MDNamespace = "http://ltsc.ieee.org/xsd/LOM";
        String CPNamespace = "http://www.imsglobal.org/xsd/imscp_v1p1";
        String ADLCPNamespace = "http://www.adlnet.org/xsd/adlcp_v1p3";
        String XMLNamespace = "http://www.w3.org/XML/1998/namespace";
        String nsNamespace = "http://www.w3.org/2000/xmlns/";
        String xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance";
        String sssNamespace = "http://www.imsglobal.org/xsd/imsss";
        String ADLSSNamespace = "http://www.adlnet.org/xsd/adlseq_v1p3";
        String ADLNavNamespace = "http://www.adlnet.org/xsd/adlnav_v1p3";
        if(iNode == null)
            return;
        int type = iNode.getNodeType();
        if(type == 9)
            isExtensionsUsed(((Node) (((Document)iNode).getDocumentElement())));
        else
        if(type == 1)
        {
            String nodeName = iNode.getLocalName();
            NamedNodeMap attrList = iNode.getAttributes();
            int numAttr = attrList.getLength();
            for(int i = 0; i < numAttr; i++)
            {
                Attr currentAttrNode = (Attr)attrList.item(i);
                String attrNamespace = currentAttrNode.getNodeValue();
                String attrName = currentAttrNode.getNodeName();
                mLogger.finest("attrName is " + attrName);
                mLogger.finest("attrNamespace is " + attrNamespace);
                if(mValidatorType.equals("contentpackage") && attrNamespace != null && !attrName.equals("identifier") && !attrName.equals("version") && !attrNamespace.equals(CPNamespace) && !attrNamespace.equals(MDNamespace) && !attrNamespace.equals(XMLNamespace) && !attrNamespace.equals(ADLCPNamespace) && !attrNamespace.equals(nsNamespace) && !attrNamespace.equals(xsiNamespace) && !attrNamespace.equals(sssNamespace) && !attrNamespace.equals(ADLSSNamespace) && !attrNamespace.equals(ADLNavNamespace) && !attrName.equals("xml:base") && !attrName.equals("xsi:schemaLocation"))
                {
                    mLogger.finest("Just found an extension attribute");
                    mLogger.finest("Name: " + currentAttrNode.getNodeName());
                    mLogger.finest("attr ns: " + attrNamespace);
                    mIsExtensionsUsed = true;
                }
                if(mValidatorType.equals("metadata") && attrNamespace != null && !attrNamespace.equals(MDNamespace) && !attrNamespace.equals(XMLNamespace) && !attrNamespace.equals(nsNamespace) && !attrNamespace.equals(xsiNamespace) && !attrName.equals("xml:base") && !attrName.equals("xsi:schemaLocation"))
                {
                    mLogger.finest("Just found an extension attribute");
                    mLogger.finest("Name: " + currentAttrNode.getNodeName());
                    mLogger.finest("attr ns: " + attrNamespace);
                    mIsExtensionsUsed = true;
                }
            }

        }
        mLogger.exiting("ADLSCORMValidator", "isExtensionsUsed()");
    }

    public void cleanImportDirectory(String iPath)
    {
        try
        {
            File theFile = new File(iPath);
            File allFiles[] = theFile.listFiles();
            for(int i = 0; i < allFiles.length; i++)
                if(allFiles[i].isDirectory())
                {
                    cleanImportDirectory(allFiles[i].toString());
                    allFiles[i].delete();
                } else
                {
                    allFiles[i].delete();
                }

        }
        catch(NullPointerException npe)
        {
            mLogger.severe(iPath + " did not exist and was not cleaned!!");
        }
    }

}