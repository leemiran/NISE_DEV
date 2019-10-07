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
package com.ziaan.scorm2004.validator;

// native java imports
import java.util.Vector;
import java.util.logging.Logger;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ziaan.scorm2004.parsers.dom.DOMTreeUtility;
import com.ziaan.scorm2004.validator.contentpackage.ManifestMap;

/**
 * <strong>Filename: </strong>ADLValidatorOutcome.java<br><br>
 *
 * <strong>Description: </strong> The <code>ADLValidatorOutcome</code> object
 * is returned upon request by the user via the use of the public method
 * available by the ADLSCORMValidator object.  The ADLValidatorOutcome object
 * serves as the central storage of the status of checks performed during the
 * validation activities, including the stored DOM. This object serves as an
 * efficient means for passing the outcome of the validation activites
 * throughout the utilizing system.<br><br>
 *
 * <strong>Design Issues: </strong>none<br><br>
 *
 * <strong>Implementation Issues: </strong>none<br><br>
 *
 * <strong>Known Problems: </strong>none<br><br>
 *
 * <strong>Side Effects: </strong>none<br><br>
 *
 * <strong>References: </strong>SCORM<br><br>
 *
 * @author ADL Technical Team
 */

public class ADLValidatorOutcome
{

   /**
    * The <code>Document</code> object is an electronic representation of the
    * XML produced if the parse was successful. A parse for wellformedness
    * creates a document object while the parse for validation against the
    * controlling documents creates a document object as well.  This attribute
    * houses the document object that is created last.  In no document object is
    * created, the value remains null. <br>
    */
   private Document mDocument;

   /**
    * The modified <code>Document</code> object after rollup is performed.
    * Rollup is performed when an IMS Manifest contains one or more
    * (sub)manifest elements.  All resources of (sub)manifest elements are
    * rolled up into the root manifest element.  If a (sub)manifest is
    * referenced by an item, than the contents of the organization in the
    * (sub)manifest is rolled up into the item performing the referencing.<br>
    */
   private Document mRolledUpDocument;

   /**
    * This attribute describes if the XML instance is found to be wellformed by
    * the parser.  The value "false" indicates that the XML instance is not
    * wellformed XML, "true" indicates it is wellformed XML.<br>
    */
   private boolean mIsWellformed;

   /**
    * This attribute describes if the XML instance is found to be valid against
    * the controlling documents by the parser.  The value "false" indicates that
    * the XML instance is not valid against the controlling documents, "true"
    * indicates that the XML instance is valid against the controlling
    * documents.<br>
    */
   private boolean mIsValidToSchema;

   /**
    * This attribute describes if the XML instance is valid to the SCORM
    * Application Profiles. A true value implies that the instance is valid to
    * the rules defined by the Application Profiles, false implies otherwise.<br>
    */
   private boolean mIsValidToApplicationProfile;

   /**
    * This attribute describes if the XML instance uses extension elements. A
    * true value implies that extension elements were detected, false implies
    * they were not used.<br>
    */
   private boolean mIsExtensionsUsed;

  /**
    * This attribute is specific to the content package validator only.  It
    * describes if the required schemas exist at the root of a content package
    * test subject necessary for the validation parse.  A true
    * value implies that the required schemas were detected at the root package,
    * false implies otherwise.<br>
    */
   private boolean mDoRequiredCPFilesExist;

  /**
    * This attribute is specific to the content package validator only.  It
    * describes if the required IMS Manifest exists at the root of a content
    * package test subject.  A true value implies that the required IMS Manifest
    * was detected at the root package, false implies otherwise.<br>
    */
   private boolean mDoesIMSManifestExistExist;

  /**
    * This attribute is specific to the content package validator only.  It
    * describes if the required IMS Manifest file exists at the root of the
    * package.  The check is performed before wellformedness due to the order
    * of events (wellformedness, requiredFilesCheck, schemaValidation, etc.)A
    * true value implies that the IMS Manifest was detected at the root package,
    * false implies otherwise.<br>
    */
   private boolean mDoesIMSManifestExist;

   /**
    * This attribute describes if the root element belongs to a namepace that is
    * not categorized as an extension. For example, if we are dealing with a
    * content package, this boolean will be set to true if the root manifest
    * element belongs to the IMS namespace.  If we are dealing with metadata,
    * this boolean will be set to true if we are dealing with a root lom
    * element that belongs to the IEEE LOM namespace.<br>
    *
    */
   private boolean mIsValidRoot;

   /**
    * Logger object used for debug logging.
    */
   private Logger mLogger;

   /**
    * Default constructor. Sets the attributes to their initial values.<br>
    *
    * @param iDoc   Test subject DOM
    * @param iWell  Wellformedness boolean result
    * @param iValidToSchema  Validation against schema boolean result
    * @param iValidToAppProfile  Application profile boolean result
    * @param iExt   extensions boolean result
    * @param iRequiredFiles  Required files boolean result
    */
   public ADLValidatorOutcome( Document iDoc,
                               boolean iRequiredManifest,
                               boolean iWell,
                               boolean iValidToSchema,
                               boolean iValidToAppProfile,
                               boolean iExt,
                               boolean iRequiredFiles,
                               boolean iPerformValidation,
                               boolean iIsValidRoot
                              )
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mLogger.entering( "ADLValidatorOutcome", "ADLValidatorOutcome()" );

      mDocument = iDoc;
      mDoesIMSManifestExist= iRequiredManifest;
      mRolledUpDocument = null;
      mIsWellformed = iWell;
      mIsValidToSchema = iValidToSchema;
      mIsValidToApplicationProfile = iValidToAppProfile;
      mIsExtensionsUsed = iExt;
      mDoRequiredCPFilesExist = iRequiredFiles;
      mIsValidRoot = iIsValidRoot;

      mLogger.exiting( "ADLValidatorOutcome", "ADLValidatorOutcome()" );
   }

   /**
    * This method returns the document created during a parse. A parse for
    * wellformedness creates a document object while the parse for validation
    * against the controlling documents creates a seperate document object.
    *
    * @return Document -  An electronic representation of the XML produced by
    * the parse.<br>
    */
   public Document getDocument()
   {
      return mDocument;
   }

   /**
    * This method returns the document created during a parse. A parse for
    * wellformedness creates a document object while the parse for validation
    * against the controlling documents creates a seperate document object.
    *
    * @return Document -  An electronic representation of the XML produced by
    * the parse.<br>
    */
   public Document getRolledUpDocument()
   {
      return mRolledUpDocument;
   }


   /**
    * This method returns the root node of the document created during a parse.
    * A parse for wellformedness creates a document object while the parse for
    * validation against the controlling documents creates a seperate document
    * object.<br>
    *
    * @return Node - the root node of the DOM stored in memory<br>
    */
   public Node getRootNode()
   {
      return (Node)mDocument.getDocumentElement();
   }

   /**
    * This method returns whether or not the XML instance was found to be
    * wellformed.  The value "false" indicates that the XML instance is not
    * wellformed XML, "true" indicates it is wellformed XML.<br><br>
    *
    * @return boolean - describes if the instance was found to be wellformed.<br>
    */
   public boolean getIsWellformed()
   {
      return mIsWellformed;
   }

   /**
    * This method returns whether or not the XML instance was valid to the
    * schema.  The value "false" indicates that the XML instance is not valid
    * against the controlling documents, "true" indicates that the XML instance
    * is valid against the controlling documents.<br>
    *
    * @return boolean - decribes if the XML Instance is valid against the
    * schema(s).<br>
    */
   public boolean getIsValidToSchema()
   {
      return mIsValidToSchema;
   }

   /**
    * This method returns whether or not the XML instance was valid to the
    * application profile checks.  The value "false" indicates that the XML
    * instance is not valid to the application profiles, "true" indicates that
    * the XML instance is valid to the application profiles.<br><br>
    *
    * @return boolean - decribes if the XML Instance is valid according to the
    * SCORM Profiles.<br>
    */
   public boolean getIsValidToApplicationProfile()
   {
      return mIsValidToApplicationProfile;
   }

   /**
    * This method returns whether or not the XML instance contained extension
    * elements and/or attributes.  The value "false" indicates that the XML
    * instance does not contain extended elements and/or attributes, "true"
    * indicates that the XML instance did.<br>
    *
    * @return boolean - describes if the XML Instance contains extension
    * elements/attributes.<br>
    */
   public boolean getIsExtensionsUsed()
   {
      return mIsExtensionsUsed;
   }

   /**
    * This method is specific to the Content Package Validator only.  This
    * method returns whether or not the content package test subject
    * contains the required schemas at the root of the package needed for the
    * validation parse.<br>
    *
    * @return boolean - describes if the required schemas were detected at the
    * root of the pif, false otherwise.<br>
    */
   public boolean getDoRequiredCPFilesExist()
   {
      return mDoRequiredCPFilesExist;
   }

   /**
    * This method is specific to the Content Package Validator only.  This
    * method returns whether or not the content package test subject
    * contains the required IMS Manifest at the root of the package.<br>
    *
    * @return boolean - describes if the required IMS Manifest was detected at
    * the root of the pif, false otherwise.<br>
    */
   public boolean getDoesIMSManifestExist()
   {
      return mDoesIMSManifestExist;
   }


   /**
    * This method returns the boolean that describes if we are dealing with
    * a valid root manifest (belongs to the IMS namespace) or a valid root
    * lom element (belongs to the IEEE LOM namespace).<br>
    *
    */
   public boolean getIsValidRoot()
   {
      return mIsValidRoot;
   }

   /**
    * This method is used to print to the java console for debug purposes only.
    * <br>
    */
   public void printToConsole()
   {
      System.out.println( "###############################################" );
      System.out.println( "### Well-formed = " + getIsWellformed() );
      System.out.println( "### Valid = " + getIsValidToSchema() );
      System.out.println( "### App Prof = " + getIsValidToApplicationProfile() );
      System.out.println( "### ExtensionsUsed = " + getIsExtensionsUsed() );
      System.out.println( "###############################################" );
   }

   /**
    * This method will find the xml:base attribute of the node passed into it
    * and return it if it has one, if it doesn't, it will return an empty
    * string.  If the node does have an xml:base attribute, this method will
    * also set that attribute to an empty string after retrieving it's value.
    *
    * @param Node - the node whose xml:base attribute value is needed.
    * @return String - the value of the xml:base attribute of this node.
    */
   public String getXMLBaseValue( Node node)
   {
      mLogger.entering( "ADLValidatorOutcome", "getXMLBaseValue()" );
      String result = new String();

      if ( node != null )
      {
         Attr baseAttr = null;
         baseAttr = DOMTreeUtility.getAttribute( node, "base" );
         if( baseAttr != null )
         {
            result = baseAttr.getValue();
            DOMTreeUtility.removeAttribute( node, "xml:base" );
         }
      }
      mLogger.exiting( "ADLValidatorOutcome", "getXMLBaseValue()" );
      return result;
   }

   /**
    * This method will apply the value of any xml:base attributes of a root
    * manifest to any file elements in it's resource elements.
    *
    * @param Node - The root <manfiest> node of a manifest.
    */
   public void applyXMLBase( Node manifestNode)
   {
      mLogger.entering( "ADLValidatorOutcome", "applyXMLBase()" );
      String x = new String();
      String y = new String();
      Node currentNode;
      String currentNodeName = new String();
      String currentHrefValue = new String();
      Attr currentHrefAttr = null;
      Node currentFileNode;
      String fileNodeName = new String();
      String fileHrefValue = new String();
      //Get base of manifest node
      x = getXMLBaseValue(manifestNode);

      //get base of resources node
      Node resourcesNode = DOMTreeUtility.getNode( manifestNode, "resources" );
      String resourcesBase = getXMLBaseValue(resourcesNode);
      if( (!x.equals( "" )) &&
          (!resourcesBase.equals( "" )) &&
          (!x.endsWith("/")) )
      {
         //x += File.separator;
         x += "/";
      }
      x += resourcesBase;

      NodeList resourceList = resourcesNode.getChildNodes();
      if( resourceList != null )
      {
         String resourceBase = new String();
         for (int i = 0; i < resourceList.getLength(); i++)
         {
            currentNode = resourceList.item(i);
            currentNodeName = currentNode.getLocalName();

            //Apply to resource level
            if ( currentNodeName.equals("resource") )
            {
               resourceBase = getXMLBaseValue(currentNode);

               if( (!x.equals( "" )) &&
                   (!resourceBase.equals( "" )) &&
                   (!x.endsWith("/")) )
               {
                  //y = x + File.separator + resourceBase;
                   y = x + "/" + resourceBase;
               }
               else
               {
                  y = x + resourceBase;
               }

               currentHrefAttr = DOMTreeUtility.
                  getAttribute( currentNode, "href" );
               if( currentHrefAttr != null )
               {
                  currentHrefValue = currentHrefAttr.getValue();
                  if( (!y.equals( "" )) &&
                      (!currentHrefValue.equals( "" )) &&
                      (!y.endsWith("/")) )
                  {
                     currentHrefAttr.setValue( y + "/" + currentHrefValue );
                  }
                  else
                  {
                     currentHrefAttr.setValue( y + currentHrefValue );
                  }
               }

               NodeList fileList = currentNode.getChildNodes();
               if( fileList != null )
               {
                  for( int j = 0; j < fileList.getLength(); j++ )
                  {
                     currentFileNode = fileList.item(j);
                     fileNodeName = currentFileNode.getLocalName();
                     if( fileNodeName.equals("file") )
                     {
                        Attr fileHrefAttr = DOMTreeUtility.
                                        getAttribute( currentFileNode, "href" );
                        fileHrefValue = fileHrefAttr.getValue();
                        if( (!y.equals( "" )) &&
                            (!fileHrefValue.equals( "" )) &&
                            (!y.endsWith("/")) )
                        {
                           fileHrefAttr.setValue( y + "/" +
                                                  fileHrefValue );
                        }
                        else
                        {
                            fileHrefAttr.setValue( y + fileHrefValue );
                        }
                     }
                  }
               }
            }
         }
      }
      mLogger.exiting( "ADLValidatorOutcome", "applyXMLBase()" );
   }

   /**
    * Returns a vector of any sub-items of a given item Node.
    *
    * @param Node - the item Node whose sub-items you wish to obtain.
    *
    * @return Vector - a Vector of all sub-items of the given item.
    *
    */
   public Vector getItems( Node item )
   {
      mLogger.entering( "ADLValidatorOutcome", "getItems()" );
      Vector result = new Vector();
      Vector itemList = new Vector();
      Node currentItem = null;
      if ( item != null )
      {
         itemList = DOMTreeUtility.getNodes( item, "item" );
      }
      result.addAll(itemList);
      for( int itemCount = 0; itemCount < itemList.size(); itemCount++ )
      {
         currentItem = (Node)itemList.elementAt(itemCount);
         result.addAll( getItems(currentItem) );
      }
      mLogger.exiting( "ADLValidatorOutcome", "getItems()" );
      return result;
   }


   /**
    * Returns a Vector filled with all of the item Nodes in a manifest node.
    * This method is scoped only to one level of manifest, as such, it does not
    * return items in sub-manifests.
    *
    * @param Node - the manifest node you wish to perform this operation on
    *
    * @return Vector - a Vector containing all of the item nodes in the
    *                  manifest.
    */
   public Vector getItemsInManifest( Node manifest )
   {
      mLogger.entering( "ADLValidatorOutcome", "getItemsInManifest()" );
      Node organizationsNode = null;
      Vector organizationList = new Vector();
      Vector itemList = new Vector();
      Vector resultList = new Vector();
      Node currentOrg = null;
      Node currentItem = null;
      organizationsNode = DOMTreeUtility.getNode( manifest, "organizations" );
      if( organizationsNode != null )
      {
         organizationList =
            DOMTreeUtility.getNodes( organizationsNode, "organization" );
      }

      for( int orgCount = 0; orgCount < organizationList.size(); orgCount++ )
      {
         currentOrg = (Node)organizationList.elementAt(orgCount);
         itemList = DOMTreeUtility.getNodes( currentOrg, "item" );
         for( int itemCount = 0; itemCount < itemList.size(); itemCount++ )
         {
            currentItem = (Node)itemList.elementAt(itemCount);
            resultList.addElement(currentItem);
            resultList.addAll( getItems( currentItem ) );
         }
      }
      mLogger.exiting( "ADLValidatorOutcome", "getItemsInManifest()" );
      return resultList;
   }


   /**
    * Returns an item Node whose identifier matches the ID passed in.
    *
    * @param String - the value of the identifier attribute of the item to be
    *                 found.
    *
    * @return Node - the item Node whose identifier matches the ID passed in.
    */
   public Node getItemWithID( String itemID )
   {
      mLogger.entering( "ADLValidatorOutcome", "getItemsWithID()" );
      Node manifestNode = mDocument.getDocumentElement();

      Vector manifestList = getAllManifests(manifestNode);

      Node currentItem = null;
      Node currentManifest = null;
      Node theNode = null;
      String rootID =
         DOMTreeUtility.getAttributeValue( manifestNode, "identifier" );
      String currentItemID = new String();
      Vector itemList = new Vector();
      boolean isFound = false;

      itemList = getItemsInManifest( manifestNode );
      for( int itemCount = 0; itemCount < itemList.size(); itemCount++ )
      {
         currentItem = (Node)itemList.elementAt( itemCount );
         if( currentItem != null )
         {
            currentItemID =
               DOMTreeUtility.getAttributeValue( currentItem, "identifier" );
            if( currentItemID.equals(itemID) )
            {
               theNode = currentItem;
               isFound = true;
            }
         }
      }
      if( !isFound )
      {
         for( int manCount = 0; manCount < manifestList.size(); manCount++ )
         {
            currentManifest = (Node)manifestList.elementAt( manCount );
            if( currentManifest != null )
            {
               itemList = getItemsInManifest( currentManifest );
               for( int count = 0; count < itemList.size(); count++ )
               {
                  currentItem = (Node)itemList.elementAt( count );
                  if( currentItem != null )
                  {
                     currentItemID =
                        DOMTreeUtility.getAttributeValue( currentItem,
                                                          "identifier" );
                     if( currentItemID.equals(itemID) )
                     {
                        theNode = currentItem;
                        isFound = true;
                        break;
                     }
                  }
               }
            }
         }
      }
      mLogger.exiting( "ADLValidatorOutcome", "getItemWithID()" );
      return theNode;
   }

   /**
    * Returns a Vector filled with all of the resource Nodes in a DOM
    *
    * @param Node - the manifest node you wish to perform this operation on
    *
    * @return Vector - a Vector containing all of the resource nodes in the
    *                  manifest.
    */
   public Vector getAllResources( Node manifest )
   {
      mLogger.entering( "ADLValidatorOutcome", "getAllResources()" );
      Vector resourceList = new Vector();
      Vector manifestList = new Vector();
      Node resourcesNode = DOMTreeUtility.getNode( manifest, "resources" );
      resourceList = DOMTreeUtility.getNodes( resourcesNode, "resource" );
      manifestList = DOMTreeUtility.getNodes( manifest, "manifest" );
      Node currentManifest = null;

      for( int i = 0; i < manifestList.size(); i++ )
      {
         currentManifest = (Node) manifestList.elementAt(i);
         resourceList.addAll( getAllResources( currentManifest ) );
      }
      mLogger.exiting( "ADLValidatorOutcome", "getAllResources()" );
      return resourceList;
   }


   /**
    * Returns a Vector filled with all of the manifest Nodes in a DOM
    *
    * @param Node - the manifest node you wish to perform this operation on
    *
    * @return Vector - a Vector containing all of the manifest nodes in the
    *                  manifest.
    */
   public Vector getAllManifests( Node manifest )
   {
      mLogger.entering( "ADLValidatorOutcome", "getAllManifests()" );
      Vector resultList = new Vector();
      Vector manifestList = new Vector();
      Node currentManifest = null;
      if( manifest != null)
      {
         manifestList = DOMTreeUtility.getNodes( manifest, "manifest" );
         resultList = new Vector(manifestList);
      }

      for( int manifestCount = 0; manifestCount < manifestList.size(); manifestCount++ )
      {
         currentManifest = (Node) manifestList.elementAt(manifestCount);
         resultList.addAll( getAllManifests( currentManifest ) );
      }
      mLogger.exiting( "ADLValidatorOutcome", "getAllManifests()" );
      return resultList;
   }


   /**
    * Returns the resource or manifest node in a manifest whose identifier
    * attribute matches the ID passed in.
    *
    * @param Node - the manifest node you wish to perform this operation on
    * @param String - the value of the identifier of the node you are looking
    *                 for.
    *
    * @return Node - the Node that has the identifier matching the ID passed in.
    */
   public Node getNodeWithID( Node manifest, String ID )
   {
      mLogger.entering( "ADLValidatorOutcome", "getNodeWithID()" );
      boolean isFound = false;
      Node theNode = null;
      Node currentManifest = null;
      Node currentResource = null;
      Vector allManifests = getAllManifests( manifest );
      int i = 0;
      int j = 0;
      String manifestID = new String();
      String resourceID = new String();

      while( (i < allManifests.size()) && (!isFound) )
      {
         currentManifest = (Node) allManifests.elementAt( i );
         manifestID = DOMTreeUtility.
                 getAttributeValue( currentManifest, "identifier" );
         if( manifestID.equals( ID ) )
         {
            isFound = true;
            theNode = (Node) allManifests.elementAt( i );
            break;
         }
         else
         {
            i++;
         }
      }
      if( !isFound )
      {
         mLogger.info("NOT FOUND" + ID);
         Vector allResources = getAllResources( manifest );
         while( (j < allResources.size()) && (!isFound) )
         {
            currentResource = (Node) allResources.elementAt( j );
            resourceID = DOMTreeUtility.
                 getAttributeValue( currentResource, "identifier");
            if( resourceID.equals( ID ) )
            {
               isFound = true;
               theNode = (Node) allResources.elementAt( j );
               break;
            }
            else
            {
               j++;
            }
         }
      }
      mLogger.exiting( "ADLValidatorOutcome", "getNodeWithID()" );
      return theNode;
   }

   /**
    * This method loops through the elements in the mItemIdrefs vector of the
    * ManifestMap object. If the element doesn't match an element in the
    * mResourceIds vector, it searches the DOM tree of the given manifest Node
    * for the node with the ID matching the itemIdrefs value.  If the node found
    * to have the matching ID is a manifest node, the sub-manifest is rolled up,
    * merging the organization node of the sub-manifest with the item node that
    * referenced the sub-manifest.  It then recursivly loops through the
    * mManifestMaps vector, performing these operations on each element.
    *
    * @param ManifestMap - The ManifestMap that this method is to be performed
    *                      on.
    * @param Node - The root manifest node of the DOM tree.
    *
    */
   public void processManifestMap( ManifestMap manifestMap, Node manifestNode )
   {
      mLogger.entering( "ADLValidatorOutcome", "processManifestMap()" );
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
      NodeList oldItemChildren;
      Node currentOldChild = null;
      Node theChild = null;
      Node currentChild = null;
      Attr identifierAttr = null;
      ManifestMap currentManifestMap;

      //Loop through the itemIdref Vector
      for( int IDRefCount = 0; IDRefCount < itemIdrefs.size(); IDRefCount++ )
      {
         itemIdref = (String) itemIdrefs.elementAt(IDRefCount);
         if( !itemIdref.equals("") )
         {
            theNode = null;
            isInResources = false;
            for( int ResIDCount = 0; ResIDCount < resourceIDs.size(); ResIDCount++ )
            {
               resourceID = (String) resourceIDs.elementAt(ResIDCount);
               if( itemIdref.equals(resourceID) )
               {
                  isInResources = true;
                  break;
               }
            }

            //If the itemIdref is not in the resourceIDs vector
            if( !isInResources )
            {
               //Get the node whose identifier is in this position of the
               //itemIdref vector.
               theNode = getNodeWithID(manifestNode, itemIdref);

               if( theNode != null )
               {
                  theNodeName = theNode.getLocalName();

                  //If theNode is a manifest node
                  if( theNodeName.equals("manifest") )
                  {
                     organizationsNode = null;

                     organizationsNode = DOMTreeUtility.
                                            getNode( theNode, "organizations" );
                     organizationNodes = DOMTreeUtility.
                                  getNodes( organizationsNode, "organization" );

                     organizationIndex = 0;

                     //Find the organization node referanced by the "default"
                     //attribute of the organizations node.  If no match is
                     //found, use the first organization.
                     defaultOrgID = DOMTreeUtility.
                              getAttributeValue( organizationsNode, "default" );
                     for( int orgCount = 0;
                            orgCount < organizationNodes.size(); orgCount++ )
                     {
                        tempOrgNode = (Node) organizationNodes.elementAt(orgCount);
                        tempOrgID = DOMTreeUtility.getAttributeValue(tempOrgNode, "identifier" );
                        if( tempOrgID.equals(defaultOrgID) )
                        {
                           organizationIndex = orgCount;
                           break;
                        }
                     }

                     organizationNode = (Node)organizationNodes.
                                        elementAt(organizationIndex);

                     organizationID = DOMTreeUtility.
                            getAttributeValue( organizationNode, "identifier" );

                     orgChildren = organizationNode.getChildNodes();

                     IDToReplace = (String)itemIDs.elementAt(IDRefCount);
                     oldItem = getItemWithID( IDToReplace );

                     oldItemChildren = oldItem.getChildNodes();

                     for ( int oldChildCount = 0; oldChildCount < oldItemChildren.getLength(); oldChildCount++ )
                     {
                        currentOldChild = oldItemChildren.item(oldChildCount);
                        oldItem.removeChild(currentOldChild);
                     }

                     for( int z=0; z < orgChildren.getLength(); z++ )
                     {
                        theChild = orgChildren.item(z);
                     }

                     //Loop through all of the children of the sub-manifest
                     //organization and append them to the item (oldItem) that
                     //referenced the sub-manifest.
                     for ( int childCount = 0; childCount < orgChildren.getLength();  )
                     {
                        currentChild = orgChildren.item(childCount);
                        try
                        {
                           oldItem.appendChild( currentChild );
                        }
                        catch(Exception e){e.printStackTrace();}


                     }

                     oldItemChildren = oldItem.getChildNodes();

                     identifierAttr =
                        DOMTreeUtility.getAttribute( oldItem, "identifier");

                     identifierAttr.setValue(organizationID);

                     DOMTreeUtility.removeAttribute( oldItem, "identifierref");
                  }
               }
            }
         }
      }
      for( int mmCount = 0; mmCount < manifestMaps.size(); mmCount++ )
      {
         currentManifestMap = (ManifestMap)manifestMaps.elementAt(mmCount);
         processManifestMap( currentManifestMap, manifestNode );
      }
      mLogger.exiting( "ADLValidatorOutcome", "processManifestMap()" );
   }

   /**
    * This method is a control method which deals with rolling up sub-manifests.
    * It first populates a ManifestMap object and then calls processManifestMap.
    * It then rolls all resources in any sub-manifest to the root manifest, and
    * deletes any sub-manifest nodes in the DOM tree.
    *
    * @param Node - The root manifest node of the DOM tree.
    * @param boolean - Whether or not the package is a resource package.
    *
    */
   public void rollupSubManifests( boolean isResPackage )
   {
      mLogger.entering( "ADLValidatorOutcome", "rollupSubManifests()" );
      Node manifest = mDocument.getDocumentElement();
      ManifestMap manifestMap = new ManifestMap();
      Vector manifestList = new Vector();
      Vector resourceList = new Vector();
      Node rootResources = DOMTreeUtility.getNode( manifest, "resources");
      Node currentManifest = null;
      Node currentResource = null;

      manifestMap.populateManifestMap(manifest);
      applyXMLBase(manifest);

      // Are there any sub-manifests?
      if ( manifestMap.getDoSubmanifestExist() )
      {
         if( !isResPackage )
         {
            processManifestMap( manifestMap, manifest );
         }

         manifestList = DOMTreeUtility.getNodes( manifest, "manifest");
         for( int i=0; i < manifestList.size(); i++ )
         {
            currentManifest = (Node)manifestList.elementAt(i);
            resourceList.addAll( getAllResources(currentManifest) );
         }

         //rollup all resources to the root manifest
         for( int j=0; j < resourceList.size(); j++ )
         {
            currentResource = (Node)resourceList.elementAt(j);
            rootResources.appendChild( currentResource );
         }

         if( !isResPackage )
         {
            //delete all sub-manifests
            for( int k=0; k < manifestList.size(); k++ )
            {
               currentManifest = (Node)manifestList.elementAt(k);
               if( currentManifest != null )
               {
                  manifest.removeChild(currentManifest);
               }
            }
         }
      }
      mLogger.exiting( "ADLValidatorOutcome", "rollupSubManifests()" );
   }
}
