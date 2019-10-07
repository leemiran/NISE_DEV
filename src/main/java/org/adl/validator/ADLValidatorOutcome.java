
package org.adl.validator;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Logger;
import org.adl.parsers.dom.DOMTreeUtility;
import org.adl.validator.contentpackage.ManifestMap;
import org.w3c.dom.*;

public class ADLValidatorOutcome implements Serializable
{
    private Document mDocument;
    private Document mRolledUpDocument;
    private boolean mIsWellformed;
    private boolean mIsValidToSchema;
    private boolean mIsValidToApplicationProfile;
    private boolean mIsExtensionsUsed;
    private boolean mDoRequiredCPFilesExist;
    private boolean mDoesIMSManifestExistExist;
    private boolean mDoesIMSManifestExist;
    private Logger mLogger;

    public ADLValidatorOutcome(Document iDoc, boolean iRequiredManifest, boolean iWell, boolean iValidToSchema, boolean iValidToAppProfile, boolean iExt, boolean iRequiredFiles, 
            boolean iPerformValidation)
    {
        mLogger = Logger.getLogger("org.adl.util.debug.validator");
        mLogger.entering("ADLValidatorOutcome", "ADLValidatorOutcome()");
        mDocument = iDoc;
        mDoesIMSManifestExist = iRequiredManifest;
        mRolledUpDocument = null;
        mIsWellformed = iWell;
        mIsValidToSchema = iValidToSchema;
        mIsValidToApplicationProfile = iValidToAppProfile;
        mIsExtensionsUsed = iExt;
        mDoRequiredCPFilesExist = iRequiredFiles;
        mLogger.exiting("ADLValidatorOutcome", "ADLValidatorOutcome()");
    }

    public Document getDocument()
    {
        return mDocument;
    }

    public Document getRolledUpDocument()
    {
        return mRolledUpDocument;
    }

    public Node getRootNode()
    {
        return mDocument.getDocumentElement();
    }

    public boolean getIsWellformed()
    {
        return mIsWellformed;
    }

    public boolean getIsValidToSchema()
    {
        return mIsValidToSchema;
    }

    public boolean getIsValidToApplicationProfile()
    {
        return mIsValidToApplicationProfile;
    }

    public boolean getIsExtensionsUsed()
    {
        return mIsExtensionsUsed;
    }

    public boolean getDoRequiredCPFilesExist()
    {
        return mDoRequiredCPFilesExist;
    }

    public boolean getDoesIMSManifestExist()
    {
        return mDoesIMSManifestExist;
    }

    public void printToConsole()
    {
        System.out.println("###############################################");
        System.out.println("### Well-formed = " + getIsWellformed());
        System.out.println("### Valid = " + getIsValidToSchema());
        System.out.println("### App Prof = " + getIsValidToApplicationProfile());
        System.out.println("### ExtensionsUsed = " + getIsExtensionsUsed());
        System.out.println("###############################################");
    }

    public String getXMLBaseValue(Node node)
    {
        mLogger.entering("ADLValidatorOutcome", "getXMLBaseValue()");
        String result = new String();
        if(node != null)
        {
            Attr baseAttr = null;
            baseAttr = DOMTreeUtility.getAttribute(node, "base");
            if(baseAttr != null)
            {
                result = baseAttr.getValue();
                DOMTreeUtility.removeAttribute(node, "xml:base");
            }
        }
        mLogger.exiting("ADLValidatorOutcome", "getXMLBaseValue()");
        return result;
    }

    public void applyXMLBase(Node manifestNode)
    {
        mLogger.entering("ADLValidatorOutcome", "applyXMLBase()");
        String x = new String();
        String y = new String();
        String currentNodeName = new String();
        String currentHrefValue = new String();
        Attr currentHrefAttr = null;
        String fileNodeName = new String();
        String fileHrefValue = new String();
        x = getXMLBaseValue(manifestNode);
        Node resourcesNode = DOMTreeUtility.getNode(manifestNode, "resources");
        String resourcesBase = getXMLBaseValue(resourcesNode);
        if(!x.equals("") && !resourcesBase.equals("") && !x.endsWith("/"))
            x = x + "/";
        x = x + resourcesBase;
        NodeList resourceList = resourcesNode.getChildNodes();
        if(resourceList != null)
        {
            String resourceBase = new String();
            for(int i = 0; i < resourceList.getLength(); i++)
            {
                Node currentNode = resourceList.item(i);
                currentNodeName = currentNode.getNodeName();
                if(currentNodeName.equals("resource"))
                {
                    resourceBase = getXMLBaseValue(currentNode);
                    if(!x.equals("") && !resourceBase.equals("") && !x.endsWith("/"))
                        y = x + "/" + resourceBase;
                    else
                        y = x + resourceBase;
                    currentHrefAttr = DOMTreeUtility.getAttribute(currentNode, "href");
                    if(currentHrefAttr != null)
                    {
                        currentHrefValue = currentHrefAttr.getValue();
                        if(!y.equals("") && !currentHrefValue.equals("") && !y.endsWith("/"))
                            currentHrefAttr.setValue(y + "/" + currentHrefValue);
                        else
                            currentHrefAttr.setValue(y + currentHrefValue);
                    }
                    NodeList fileList = currentNode.getChildNodes();
                    if(fileList != null)
                    {
                        for(int j = 0; j < fileList.getLength(); j++)
                        {
                            Node currentFileNode = fileList.item(j);
                            fileNodeName = currentFileNode.getNodeName();
                            if(fileNodeName.equals("file"))
                            {
                                Attr fileHrefAttr = DOMTreeUtility.getAttribute(currentFileNode, "href");
                                fileHrefValue = fileHrefAttr.getValue();
                                if(!y.equals("") && !fileHrefValue.equals("") && !y.endsWith("/"))
                                    fileHrefAttr.setValue(y + "/" + fileHrefValue);
                                else
                                    fileHrefAttr.setValue(y + fileHrefValue);
                            }
                        }

                    }
                }
            }

        }
        mLogger.exiting("ADLValidatorOutcome", "applyXMLBase()");
    }

    public Vector getItems(Node item)
    {
        mLogger.entering("ADLValidatorOutcome", "getItems()");
        Vector result = new Vector();
        Vector itemList = new Vector();
        Node currentItem = null;
        if(item != null)
            itemList = DOMTreeUtility.getNodes(item, "item");
        result.addAll(itemList);
        for(int itemCount = 0; itemCount < itemList.size(); itemCount++)
        {
            currentItem = (Node)itemList.elementAt(itemCount);
            result.addAll(getItems(currentItem));
        }

        mLogger.exiting("ADLValidatorOutcome", "getItems()");
        return result;
    }

    public Vector getItemsInManifest(Node manifest)
    {
        mLogger.entering("ADLValidatorOutcome", "getItemsInManifest()");
        Vector organizationList = new Vector();
        Vector itemList = new Vector();
        Vector resultList = new Vector();
        Node currentOrg = null;
        Node currentItem = null;
        organizationList = getOrganizationList(manifest);
        for(int orgCount = 0; orgCount < organizationList.size(); orgCount++)
        {
            currentOrg = (Node)organizationList.elementAt(orgCount);
            itemList = DOMTreeUtility.getNodes(currentOrg, "item");
            for(int itemCount = 0; itemCount < itemList.size(); itemCount++)
            {
                currentItem = (Node)itemList.elementAt(itemCount);
                resultList.addElement(currentItem);
                resultList.addAll(getItems(currentItem));
            }

        }

        mLogger.exiting("ADLValidatorOutcome", "getItemsInManifest()");
        return resultList;
    }

    public Vector getOrganizationList(Node manifest)
    {
        Vector organizationList = new Vector();
        Node organizationsNode = null;
        organizationsNode = DOMTreeUtility.getNode(manifest, "organizations");
        if(organizationsNode != null)
            organizationList = DOMTreeUtility.getNodes(organizationsNode, "organization");
        return organizationList;
    }

    public Node getItemWithID(String itemID)
    {
        mLogger.entering("ADLValidatorOutcome", "getItemsWithID()");
        Node manifestNode = mDocument.getDocumentElement();
        Vector manifestList = getAllManifests(manifestNode);
        Node currentItem = null;
        Node currentManifest = null;
        Node theNode = null;
        String rootID = DOMTreeUtility.getAttributeValue(manifestNode, "identifier");
        String currentItemID = new String();
        Vector itemList = new Vector();
        boolean isFound = false;
        itemList = getItemsInManifest(manifestNode);
        for(int itemCount = 0; itemCount < itemList.size(); itemCount++)
        {
            currentItem = (Node)itemList.elementAt(itemCount);
            if(currentItem != null)
            {
                currentItemID = DOMTreeUtility.getAttributeValue(currentItem, "identifier");
                if(currentItemID.equals(itemID))
                {
                    theNode = currentItem;
                    isFound = true;
                }
            }
        }

        if(!isFound)
        {
            for(int manCount = 0; manCount < manifestList.size(); manCount++)
            {
                currentManifest = (Node)manifestList.elementAt(manCount);
                if(currentManifest != null)
                {
                    itemList = getItemsInManifest(currentManifest);
                    for(int count = 0; count < itemList.size(); count++)
                    {
                        currentItem = (Node)itemList.elementAt(count);
                        if(currentItem == null)
                            continue;
                        currentItemID = DOMTreeUtility.getAttributeValue(currentItem, "identifier");
                        if(!currentItemID.equals(itemID))
                            continue;
                        theNode = currentItem;
                        isFound = true;
                        break;
                    }

                }
            }

        }
        mLogger.exiting("ADLValidatorOutcome", "getItemWithID()");
        return theNode;
    }

    public Vector getAllResources(Node manifest)
    {
        mLogger.entering("ADLValidatorOutcome", "getAllResources()");
        Vector resourceList = new Vector();
        Vector manifestList = new Vector();
        Node resourcesNode = DOMTreeUtility.getNode(manifest, "resources");
        resourceList = DOMTreeUtility.getNodes(resourcesNode, "resource");
        manifestList = DOMTreeUtility.getNodes(manifest, "manifest");
        Node currentManifest = null;
        for(int i = 0; i < manifestList.size(); i++)
        {
            currentManifest = (Node)manifestList.elementAt(i);
            resourceList.addAll(getAllResources(currentManifest));
        }

        mLogger.exiting("ADLValidatorOutcome", "getAllResources()");
        return resourceList;
    }

    public Vector getAllManifests(Node manifest)
    {
        mLogger.entering("ADLValidatorOutcome", "getAllManifests()");
        Vector resultList = new Vector();
        Vector manifestList = new Vector();
        Node currentManifest = null;
        if(manifest != null)
        {
            manifestList = DOMTreeUtility.getNodes(manifest, "manifest");
            resultList = new Vector(manifestList);
        }
        for(int manifestCount = 0; manifestCount < manifestList.size(); manifestCount++)
        {
            currentManifest = (Node)manifestList.elementAt(manifestCount);
            resultList.addAll(getAllManifests(currentManifest));
        }

        mLogger.exiting("ADLValidatorOutcome", "getAllManifests()");
        return resultList;
    }

    public Node getNodeWithID(Node manifest, String ID)
    {
        mLogger.entering("ADLValidatorOutcome", "getNodeWithID()");
        boolean isFound = false;
        Node theNode = null;
        Node currentManifest = null;
        Node currentResource = null;
        Vector allManifests = getAllManifests(manifest);
        int i = 0;
        int j = 0;
        String manifestID = new String();
        String resourceID = new String();
        for(; i < allManifests.size() && !isFound; i++)
        {
            currentManifest = (Node)allManifests.elementAt(i);
            manifestID = DOMTreeUtility.getAttributeValue(currentManifest, "identifier");
            if(!manifestID.equals(ID))
                continue;
            isFound = true;
            theNode = (Node)allManifests.elementAt(i);
            break;
        }

        if(!isFound)
        {
            mLogger.info("NOT FOUND" + ID);
            for(Vector allResources = getAllResources(manifest); j < allResources.size() && !isFound; j++)
            {
                currentResource = (Node)allResources.elementAt(j);
                resourceID = DOMTreeUtility.getAttributeValue(currentResource, "identifier");
                if(!resourceID.equals(ID))
                    continue;
                isFound = true;
                theNode = (Node)allResources.elementAt(j);
                break;
            }

        }
        mLogger.exiting("ADLValidatorOutcome", "getNodeWithID()");
        return theNode;
    }

    public void processManifestMap(ManifestMap manifestMap, Node manifestNode)
    {
        mLogger.entering("ADLValidatorOutcome", "processManifestMap()");
        boolean doSubManifestExist = manifestMap.getDoSubmanifestExist();
        boolean isInResources = false;
        Node theNode = null;
        String theNodeName = new String();
        String depIdref = new String();
        String manifestID = manifestMap.getManifestId();
        Vector resourceIDs = manifestMap.getResourceIds();
        Vector itemIDs = manifestMap.getItemIds();
        Vector itemIdrefs = manifestMap.getItemIdrefs();
        Vector manifestMaps = manifestMap.getManifestMaps();
        String itemIdref = new String();
        String resourceID = new String();
        Node organizationsNode = null;
        Vector organizationNodes = new Vector();
        int organizationIndex = 0;
        String defaultOrgID = new String();
        String tempOrgID = new String();
        Node tempOrgNode = null;
        Node organizationNode = null;
        String organizationID = new String();
        Vector itemList = new Vector();
        NodeList orgChildren = null;
        String IDToReplace = new String();
        Node oldItem = null;
        Node currentOldChild = null;
        Node theChild = null;
        Node currentChild = null;
        Attr identifierAttr = null;
        for(int IDRefCount = 0; IDRefCount < itemIdrefs.size(); IDRefCount++)
        {
            itemIdref = (String)itemIdrefs.elementAt(IDRefCount);
            if(!itemIdref.equals(""))
            {
                theNode = null;
                isInResources = false;
                for(int ResIDCount = 0; ResIDCount < resourceIDs.size(); ResIDCount++)
                {
                    resourceID = (String)resourceIDs.elementAt(ResIDCount);
                    if(!itemIdref.equals(resourceID))
                        continue;
                    isInResources = true;
                    break;
                }

                if(!isInResources)
                {
                    theNode = getNodeWithID(manifestNode, itemIdref);
                    if(theNode != null)
                    {
                        theNodeName = theNode.getNodeName();
                        if(theNodeName.equals("manifest"))
                        {
                            organizationsNode = null;
                            organizationsNode = DOMTreeUtility.getNode(theNode, "organizations");
                            organizationNodes = DOMTreeUtility.getNodes(organizationsNode, "organization");
                            organizationIndex = 0;
                            defaultOrgID = DOMTreeUtility.getAttributeValue(organizationsNode, "default");
                            for(int orgCount = 0; orgCount < organizationNodes.size(); orgCount++)
                            {
                                tempOrgNode = (Node)organizationNodes.elementAt(orgCount);
                                tempOrgID = DOMTreeUtility.getAttributeValue(tempOrgNode, "identifier");
                                if(!tempOrgID.equals(defaultOrgID))
                                    continue;
                                organizationIndex = orgCount;
                                break;
                            }

                            organizationNode = (Node)organizationNodes.elementAt(organizationIndex);
                            organizationID = DOMTreeUtility.getAttributeValue(organizationNode, "identifier");
                            orgChildren = organizationNode.getChildNodes();
                            IDToReplace = (String)itemIDs.elementAt(IDRefCount);
                            oldItem = getItemWithID(IDToReplace);
                            NodeList oldItemChildren = oldItem.getChildNodes();
                            for(int oldChildCount = 0; oldChildCount < oldItemChildren.getLength(); oldChildCount++)
                            {
                                currentOldChild = oldItemChildren.item(oldChildCount);
                                oldItem.removeChild(currentOldChild);
                            }

                            for(int z = 0; z < orgChildren.getLength(); z++)
                                theChild = orgChildren.item(z);

                            for(int childCount = 0; childCount < orgChildren.getLength();)
                            {
                                currentChild = orgChildren.item(childCount);
                                try
                                {
                                    oldItem.appendChild(currentChild);
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            oldItemChildren = oldItem.getChildNodes();
                            identifierAttr = DOMTreeUtility.getAttribute(oldItem, "identifier");
                            identifierAttr.setValue(organizationID);
                            DOMTreeUtility.removeAttribute(oldItem, "identifierref");
                        }
                    }
                }
            }
        }

        for(int mmCount = 0; mmCount < manifestMaps.size(); mmCount++)
        {
            ManifestMap currentManifestMap = (ManifestMap)manifestMaps.elementAt(mmCount);
            processManifestMap(currentManifestMap, manifestNode);
        }

        mLogger.exiting("ADLValidatorOutcome", "processManifestMap()");
    }

    public void rollupSubManifests(boolean isResPackage)
    {
        mLogger.entering("ADLValidatorOutcome", "rollupSubManifests()");
        Node manifest = mDocument.getDocumentElement();
        ManifestMap manifestMap = new ManifestMap();
        Vector manifestList = new Vector();
        Vector resourceList = new Vector();
        Node rootResources = DOMTreeUtility.getNode(manifest, "resources");
        Node currentManifest = null;
        Node currentResource = null;
        manifestMap.populateManifestMap(manifest);
        applyXMLBase(manifest);
        if(manifestMap.getDoSubmanifestExist())
        {
            if(!isResPackage)
                processManifestMap(manifestMap, manifest);
            manifestList = DOMTreeUtility.getNodes(manifest, "manifest");
            for(int i = 0; i < manifestList.size(); i++)
            {
                currentManifest = (Node)manifestList.elementAt(i);
                resourceList.addAll(getAllResources(currentManifest));
            }

            for(int j = 0; j < resourceList.size(); j++)
            {
                currentResource = (Node)resourceList.elementAt(j);
                rootResources.appendChild(currentResource);
            }

            if(!isResPackage)
            {
                for(int k = 0; k < manifestList.size(); k++)
                {
                    currentManifest = (Node)manifestList.elementAt(k);
                    if(currentManifest != null)
                        manifest.removeChild(currentManifest);
                }

            }
        }
        mLogger.exiting("ADLValidatorOutcome", "rollupSubManifests()");
    }

}