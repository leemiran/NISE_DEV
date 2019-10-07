/*******************************************************************************
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
*******************************************************************************/
package org.adl.validator.contentpackage;

// native java imports
import java.util.Vector;
import java.io.Serializable;
import java.lang.String;
import java.util.logging.*;

// xerces imports
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

// adl imports
import org.adl.validator.contentpackage.LaunchData;
import org.adl.validator.contentpackage.MetadataData;
import org.adl.parsers.dom.*;
import org.adl.util.*;

/**
 * <strong>Filename: </strong><br>ManifestHandler.java<br><br>
 *
 * <strong>Description: </strong>This method tracks, stores and retrieves  the
 * Launch Data information of SCOs and the Metadata information, all of which is
 * found or referenced from within the content package test subject. <br><br>
 *
 *
 * <strong>Design Issues: </strong><br>None<br>
 * <br>
 *
 * <strong>Implementation Issues: </strong><br>None<br><br>
 *
 * <strong>Known Problems: </strong><br>None<br><br>
 *
 * <strong>Side Effects: </strong><br>None<br><br>
 *
 * <strong>References: </strong><br>None<br><br>
 *
 * @author ADL Technical Team
 */
public class ManifestHandler implements Serializable
{
   /**
    * This attribute serves as the Logger object used for debug logging.<br>
    */
   private Logger mLogger;

   /**
    * This attribute describes whether or not SCO launch data has been
    * tracked. <br>
    */
   private boolean mLaunchDataTracked;

   /**
    * This attribute describes whether or not the metadata information was
    * tracked. <br>
    */
   private boolean mMetadataTracked;

   /**
    * This attribute serves as the storage list of the tracked metadata
    * information. This list will contain the following information:  If inline
    * metadata, than the root node will be stored here along with the
    * metadata application profile type.  If external metadata, than the URI to
    * the metadata will be stored along with the metadata application profile
    * type.<br>
    */
   private Vector mMetadataDataList;


   /**
    * This attribute serves as the flag that determines if the qualified lom
    * namespace is detected.
    */
   private boolean mLomNamespaceExistsAtRoot;

   /**
    * This attribute serves as the storage list of the tracked SCO launch data.
    * This list uses the default organization and does not comply with given
    * sequencing rules.  This list can be used for default behavior and
    * testing purposes. <br>
    */
   private Vector mLaunchDataList;

   /**
    * This attribute contains the xml:base value created from the <manifest>
    * and <resources> elements. <br>
    *
    */
   private String mManifestResourcesXMLBase;

   /**
    * This attribute contains the xml:base value created from each <resource>
    * element.  It will complete the xml:base value after being appended to
    * the mManifestResourceXMLBase attribute. <br>
    *
    */
   private String mResourceXMLBase;

   /**
    * Default Constructor.  Sets the attributes to their initial values.<br>
    */
   public ManifestHandler()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");
      mLaunchDataTracked = false;
      mMetadataTracked = false;
      mMetadataDataList = new Vector();
      mLaunchDataList = new Vector();
      mLomNamespaceExistsAtRoot = false;
      mManifestResourcesXMLBase = new String();
      mResourceXMLBase = new String();
   }

   /**
    * This method initiates the retrieval of the SCO launch data
    * information, if this information exists in the content package test
    * subject. <br>
    *
    * @param iRootNode root node manipulated for retrieval of launch data.<br>
    * @param iDefaultOrganizationOnly boolean describing the scope of the
    *     organization that should be traversed for SCO launch data. Specific to
    * SRTE uses - will no longer be needed in future development.<br>
    * @param iRemoveAssets boolean describing whether or not to remove assets
    *        from the LaunchData list.  The SRTE needs this to be false in
    *        in order to import assets as well.
    *
    * @return Vector containing the launch data information for SCOs.
    */
   public Vector getLaunchData( Node iRootNode,
                                boolean iDefaultOrganizationOnly,
                                boolean iRemoveAssets )
   {
      mLogger.entering("ManifestHandler", "getLaunchData(iRootNode)");
      if ( ! mLaunchDataTracked )
      {
         setLaunchData( iRootNode, iDefaultOrganizationOnly, iRemoveAssets );
      }
      return mLaunchDataList;
   }

   /**
    * This method initiates the retrieval of the metadata information,
    * if this information exists in the content package test subject. <br>
    *
    * @param iRootNode root node manipulated for retrieval of metadata info.<br>
    *
    * @param iBaseDirectory base directory for location of test subject
    *
    * @return Vector containing the metadata information.
    */
   public Vector getMetadata( Node iRootNode,
                              String iBaseDirectory )
   {
      if ( ! mMetadataTracked )
      {
         setMetadata( iRootNode, iBaseDirectory );
      }

      return mMetadataDataList;
   }

   /**
    * This method performs the actual retrieval of the SCO launch data
    * information, if this information exists in the content package test
    * subject.  This method walks through the test subject dom, storing all the
    * SCO launch data information to the LaunchData data structure. <br>
    *
    * @param iRootNode root node of test subject dom.<br>
    * @param iDefaultOrganizationOnly boolean describing the scope of the
    * organization that should be traversed for SCO launch data. Specific to
    * SRTE uses - will no longer be needed in future development.<br>
    * @param iRemoveAssets boolean describing whether or not to remove assets
    *        from the LaunchData list.  The SRTE needs this to be false in
    *        in order to get LaunchData for assets as well.
    * @return void<br>
    */
   private void setLaunchData( Node iRootNode,
                               boolean iDefaultOrganizationOnly,
                               boolean iRemoveAssets )
   {
      mLogger.entering("ManifestHandler", "SetLaunchData(iRootNode)");
      Vector organizationNodes = getOrganizationNodes( iRootNode,
                                                     iDefaultOrganizationOnly );

      int size = organizationNodes.size();

      // populate the Launch Data for the Organization level
      for ( int i = 0; i < size; i++ )
      {
         Node currentOrganization = (Node)organizationNodes.elementAt(i);

         String orgIdentifier = "";
         int num = i + 1;

         orgIdentifier = DOMTreeUtility.getAttributeValue( currentOrganization,
                                                           "identifier" );

         addItemInfo( currentOrganization, orgIdentifier );
      }

      Node xmlBaseNode = null;
      String manifestXMLBase = "";
      String resourcesXMLBase = "";

      // calculate the <manifest>s xml:base
      NamedNodeMap attributes = iRootNode.getAttributes();
      xmlBaseNode = attributes.getNamedItem("xml:base");

      if ( xmlBaseNode != null )
      {
         manifestXMLBase = xmlBaseNode.getNodeValue();
      }

      // calculate the <resources> xml:base
      Node resources = DOMTreeUtility.getNode( iRootNode, "resources" );
      attributes = resources.getAttributes();
      xmlBaseNode = attributes.getNamedItem("xml:base");

      if ( xmlBaseNode != null )
      {
         resourcesXMLBase = xmlBaseNode.getNodeValue();
      }

      // populate all Launch Data with the xml:base values
      size = mLaunchDataList.size();
      LaunchData currentLaunchData = null;

      for ( int j = 0; j < size; j++ )
      {
         currentLaunchData = (LaunchData)mLaunchDataList.elementAt(j);

         // update the xml:base data
         currentLaunchData.setManifestXMLBase( manifestXMLBase );
         currentLaunchData.setResourcesXMLBase( resourcesXMLBase );

         // replace the old LaunchData Object with the updated one
         mLaunchDataList.removeElementAt(j);
         mLaunchDataList.insertElementAt( currentLaunchData, j );
      }
      // populate the Launch Data for the Resource level
      addResourceInfo( iRootNode, iRemoveAssets );

      removeDuplicateLaunchData();

      mLaunchDataTracked = true;
   }

   /**
    * This method performs the actual retrieval of the metadata information,
    * if this information exists in the content package test
    * subject.  This method walks through the test subject dom, storing all
    * metadata information to the MetadataData data structure.   XML:base is
    * also being tracked for the <adlcp:location> element. <br>
    *
    * @param iNode element nodes traversed for metadata element.<br>
    *
    * @param iBaseDirectory base directory for location of test subject
    *
    * @return void<br>
    **/
   private void setMetadata( Node iNode,
                             String iBaseDirectory )
   {

      if ( iNode != null )
      {
         String nodeName = iNode.getLocalName();

         if ( nodeName != null )
         {
            if ( nodeName.equals( "manifest" ) )
            {
               if( isSCORM_2004_Metadata( iNode, true ) )
               {
                  mLomNamespaceExistsAtRoot = true;
               }

               // set and retrieve xml:base of manifest if it exists

               // must first clear out xml:base values if dealing with a sub
               if ( !mManifestResourcesXMLBase.equals("") )
               {
                  mManifestResourcesXMLBase = "";
                  mResourceXMLBase = "";
               }

               String manifestXMLBase =
                             DOMTreeUtility.getAttributeValue( iNode, "base" );

               if ( !manifestXMLBase.equals("") )
               {
                  mManifestResourcesXMLBase = manifestXMLBase;
               }


               trackMetadata( iNode, "contentaggregation", iBaseDirectory );
            }
            else if ( nodeName.equals( "organization" ) )
            {
               trackMetadata( iNode, "contentorganization", iBaseDirectory );
            }
            else if ( nodeName.equals ( "item" ) )
            {
               trackMetadata( iNode, "activity", iBaseDirectory );
            }
            else if ( nodeName.equals("resources") )
            {
               // set and retrieve xml:base of resources if it exists
               String resourcesXMLBase = DOMTreeUtility.getAttributeValue(
                                                                iNode, "base" );

               if ( !resourcesXMLBase.equals("") )
               {
                  mManifestResourcesXMLBase = mManifestResourcesXMLBase + resourcesXMLBase;
               }

            }
            else if ( nodeName.equals( "resource" ) )
            {
               String applicationProfileType = "";
               applicationProfileType = DOMTreeUtility.getAttributeValue(
                                                 iNode, "scormType" );

               // retrieve xml:base of resource if it exists
               // cannot set classattribute - applies to specified resource only

               mResourceXMLBase = DOMTreeUtility.getAttributeValue(
                                                                iNode, "base" );

               trackMetadata( iNode, applicationProfileType, iBaseDirectory );
            }
            else if ( nodeName.equals( "file" ) )
            {
               trackMetadata( iNode, "asset", iBaseDirectory );
            }

            NodeList nodeChildren = iNode.getChildNodes();
            int size = nodeChildren.getLength();
            if ( nodeChildren != null )
            {
               for ( int i=0; i < size; i++ )
               {
                  setMetadata( nodeChildren.item(i), iBaseDirectory );
               }
            }
         }
         mMetadataTracked = true;

      }
   }

   /**
    *
    * This method tracks the metadata information contained in the
    * metadata element and saves the information in the MetadataData object.
    * Such information saved includes the metaddata application profile type,
    * URI if the metadata is external stand-alone metadata, or the root node if
    * the metadata is inline in the form of extensions to the content package
    * manifest.<br>
    *
    * @param iNode node tracked for Metadata<br>
    * @param iApplicationProfileType Metadata Application Profile Typy (asset,
    * sco, sca, activity, contentaggregation).<br>
    * @return void<br>
    *
    */
   private void trackMetadata( Node iNode,
                               String iApplicationProfileType,
                               String iBaseDirectory )
   {
      Node metadataNode = DOMTreeUtility.getNode( iNode, "metadata");

      if ( metadataNode != null )
      {
         String identifier = DOMTreeUtility.getAttributeValue( iNode,
                                                               "identifier" );

         //Gets all the location metadata
         Vector locationNodeList =DOMTreeUtility.getNodes( metadataNode,
                                                           "location");

         // iterate through the vector and get the attribute names and values

         int locationNodeListSize = locationNodeList.size();
         for ( int i = 0; i < locationNodeListSize; i++ )
         {
            MetadataData metadataData = new MetadataData();
            metadataData.setApplicationProfileType( iApplicationProfileType );

            //Gets the location value of each node
            String locationValue =
               DOMTreeUtility.getNodeValue((Node)locationNodeList.elementAt(i));
            locationValue = mManifestResourcesXMLBase + mResourceXMLBase +
                           locationValue;

            metadataData.setIdentifier( identifier );
            metadataData.setLocation( locationValue );

            //Create an adldomparser object
            ADLDOMParser adldomparser = new ADLDOMParser();

            //Gets the path to the file to be parsed
            String metadataFile = iBaseDirectory;
            metadataFile = metadataFile.replace( '\\', '/');
            metadataFile = metadataFile + locationValue;

            // retrieve adldomparser attribute values and assign to the SCORMValidator
            adldomparser.parseForWellformedness( metadataFile, false );
            Document metaDocument = adldomparser.getDocument();

            //Checks if document was found
            if( metaDocument != null )
            {
               //Checks for scorm 2004
               if ( isSCORM_2004_Metadata( (Node)metaDocument.getDocumentElement(),
                                            false) )
               {
                  mMetadataDataList.add( metadataData );
               }
            }
            else
            {
               //meta data not found
            }

            //resets parser
            adldomparser = null;
         }


         //Gets all the inline metadata from the current node
         Vector lomNodelist = DOMTreeUtility.getNodes(metadataNode, "lom");

         // iterate through the vector and get the attribute names and values
         int lomNodeListSize = lomNodelist.size();
         for ( int j = 0; j < lomNodeListSize; j++ )
         {
            MetadataData metadataData = new MetadataData();
            metadataData.setApplicationProfileType( iApplicationProfileType );

            //Gets the location value of each node
            metadataData.setIdentifier( identifier );

            Node lomNode = (Node)lomNodelist.elementAt(j);
            metadataData.setRootLOMNode(lomNode);
            metadataData.setLocation( "inline" );

            //Tests to see if this in-line meta data should be added to the test list
            //must be SCORM 2004 metadata to be added.
            if ( isSCORM_2004_Metadata( lomNode, false ) )
            {
               mMetadataDataList.add( metadataData );
            }
            else
            {
               // special case dealing with qualified elements
               if ( !(lomNode.getLocalName().equals(lomNode.getNodeName()) ) )
               {
                  if (lomNode.getLocalName().equals("lom") )
                  {
                     if ( mLomNamespaceExistsAtRoot )
                     {
                        mMetadataDataList.add( metadataData );
                     }
                  }
               }
            }
         }

          //metadataData.printToConsole();
      }
   }


   /**
    *
    * This method tests to see if the current metadata is a SCORM 2004
    * metadata.  In order to qualify as SCORM 2004 metadata, the following
    * must be true:  (1) the lom namespace must be detected,
    * (2) the metadataSchema elements must exist with the valid values of
    * "LOMv1.0" and "SCORM_CAM_v1.3"<br>
    *
    * @param Node - the root metadata node<br>
    * @param boolean - true states to check only the root declaration for the
    * lom namespace, false implies to check both root for the lom namespace
    * as well as the metadataSchema element values.<br>
    *
    * @return boolean<br>
    *
    */
   private boolean isSCORM_2004_Metadata( Node iLomNode,
                                          boolean checkRootDeclarationOnly )
   {
      boolean foundLOMNamespace = false;
      boolean foundLOMValue = false;
      boolean foundSCORMCamValue = false;

      // first, determine if the lom namespace is being used
      if ( iLomNode.hasAttributes() )
      {
         // if it does, store it in a NamedNodeMap object
         NamedNodeMap AttributesList = iLomNode.getAttributes();

         // iterate through the NamedNodeMap and get the attribute names and values

         int attributeListLength = AttributesList.getLength();
         for( int k = 0; k < attributeListLength; k++ )
         {
            String namespaceURIs = AttributesList.item(k).getNodeValue();

            //Tests for "http://ltsc.ieee.org/xsd/LOM"
            if ( namespaceURIs.indexOf("http://ltsc.ieee.org/xsd/LOM") != -1 )
            {
               foundLOMNamespace = true;
            }
         }
      }

      // only continue to check for metadataSchema values if parameter flag
      // signals to

      if( !checkRootDeclarationOnly )
      {
         // Now determine if the metadataSchema elements contain the valid values

         Node metaMetadata = DOMTreeUtility.getNode( iLomNode, "metaMetadata" );

         if( metaMetadata != null )
         {
            //Gets all the location metadata
            Vector metadataSchemaNodesList =DOMTreeUtility.getNodes( metaMetadata,
                                                             "metadataSchema");

           // iterate through the vector and and get the values

           int metadataSchemaNodesListSize = metadataSchemaNodesList.size();
           for ( int i = 0; i < metadataSchemaNodesListSize; i++ )
           {

              String metadataSchemaValue = "";
              Node metadataSchemaNode = (Node)metadataSchemaNodesList.elementAt(i);
              metadataSchemaValue =
                              DOMTreeUtility.getNodeValue( metadataSchemaNode );

              if( metadataSchemaValue.equals("LOMv1.0") )
              {

                 // <metadataSchema>LOMv1.0</metadataSchema>
                 foundLOMValue = true;

              }
              else if( metadataSchemaValue.equals("SCORM_CAM_v1.3") )
              {

                 // <metadataSchema>SCORM_CAM_v1.3</metadataSchema>
                 foundSCORMCamValue = true;
              }
           }
         }
      }
      if( checkRootDeclarationOnly )
      {
         return foundLOMNamespace;
      }
      else
      {
         return foundLOMNamespace && foundLOMValue && foundSCORMCamValue;
      }

   }


   /**
    *
    * This method removes the duplicate LaunchData elements that are stored in
    * the list during tracking.  This removal is based on the Resource
    * Identifier, XML Base directories, Location and Parameters. <br>
    *
    * @return void<br>
    *
    */
   private void removeDuplicateLaunchData()
   {
      int size = mLaunchDataList.size();
      LaunchData ldA;
      LaunchData ldB;
      String ldAid;
      String ldBid;
      String ldAll;
      String ldBll;

      for ( int i = 0; i < size; i++ )
      {
         ldA = (LaunchData)mLaunchDataList.elementAt(i);
         ldAid = ldA.getResourceIdentifier();

         for ( int j = i + 1; j < size; j++ )
         {
            ldB = (LaunchData)mLaunchDataList.elementAt(j);
            ldBid = ldB.getResourceIdentifier();

            if ( ldBid.equals(ldAid) )
            {
               ldAll = ldA.getItemIdentifier();
               ldBll = ldB.getItemIdentifier();

               if ( ldBll.equals(ldAll) )
               {
                  mLaunchDataList.removeElementAt(j);
                  j--;
                  size = mLaunchDataList.size();
               }
            }
         }
      }
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
   public static Vector getOrganizationNodes( Node iRootNode,
                                              boolean iDefaultOrganizationOnly )
   {
      //mLogger.entering("ManifestHandler", "getOrganizationNodes()");
      Vector result = new Vector();

      if ( iDefaultOrganizationOnly )
      {
         result.add( getDefaultOrganizationNode( iRootNode ) );
      }
      else
      {
         // get the list of organization nodes
         Node organizationsNode = DOMTreeUtility.getNode( iRootNode,
                                                          "organizations" );
         NodeList children = organizationsNode.getChildNodes();

         if ( children != null )
         {
            int numChildren = children.getLength();

            for ( int i = 0; i < numChildren; i++ )
            {
               Node currentChild = children.item(i);
               String currentChildName = currentChild.getLocalName();

               if ( currentChildName.equals( "organization" ) )
               {
                  // add the organization node to the resulting list
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
   public static Node getDefaultOrganizationNode( Node iRootNode )
   {
      Node result = null;

      // find the value of the "default" attribute of the <organizations> node
      Node organizationsNode = DOMTreeUtility.getNode( iRootNode,
                                                       "organizations" );
      NamedNodeMap attrList = organizationsNode.getAttributes();
      String defaultIDValue = (attrList.getNamedItem("default")).getNodeValue();

      // traverse the <organization> nodes and find the matching default ID
      NodeList children = organizationsNode.getChildNodes();

      if ( children != null )
      {
         int numChildren = children.getLength();

         for ( int i = 0; i < numChildren; i++ )
         {
            Node currentChild = children.item(i);
            String currentChildName = currentChild.getLocalName();

            if ( currentChildName.equals( "organization" ) )
            {
               // find the value of the "identifier" attribute of the
               // <organization> node
               NamedNodeMap orgAttrList = currentChild.getAttributes();
               String idValue =
                  (orgAttrList.getNamedItem("identifier")).getNodeValue();

               if ( idValue.equals( defaultIDValue ) )
               {
                  result = currentChild;
                  break;
               }
            }
         }
      }

      return result;
   }

   /**
    * This method retrieves the minNormalizedMeasure element from the parent
    * sequencing element. <br>
    *
    * @param node to be manipulated for minnormalizedmeasure value.
    * <br>
    *
    * @return String containing the minNormalizedMeasure value.<br>
    */
   private String getMinNormalizedMeasure( Node iNode )
   {
      String minNormalizedMeasure = "";
      String nodeName = iNode.getLocalName();

      if ( nodeName.equals("item") )
      {
         Node sequencingNode = DOMTreeUtility.getNode( iNode, "sequencing" );
         if ( sequencingNode != null )
         {
            Node objectivesNode = DOMTreeUtility.getNode( sequencingNode,
                                                            "objectives" );
            if ( objectivesNode != null )
            {
               Node primaryObjectiveNode = DOMTreeUtility.
                                            getNode( objectivesNode,
                                                     "primaryObjective" );
               if ( primaryObjectiveNode != null)
               {
                  String SBMValue = "";
                  SBMValue = DOMTreeUtility.getAttributeValue(
                                            primaryObjectiveNode,
                                            "satisfiedByMeasure" );
                  if( SBMValue.equals("true") )
                  {
                     Node minNormalizedMeasureNode = DOMTreeUtility.getNode(
                                                    primaryObjectiveNode,
                                                    "minNormalizedMeasure" );
                     if( minNormalizedMeasureNode != null )
                     {
                       minNormalizedMeasure = DOMTreeUtility.getNodeValue(
                                                     minNormalizedMeasureNode );
                     }
                     else
                     {
                       minNormalizedMeasure = "1.0";
                     }
                  }
               }
            }
         }
      }
      return minNormalizedMeasure;
   }

   /**
    * This method retrieves the attemptAbsoluteDurationLimit element from the
    * parent sequencing element. <br>
    *
    * @param iNode  node to be manipulated for attemptAbsoluteDurationLimit
    * value.<br>
    *
    * @return String containing the attemptAbsoluteDurationLimit value.<br>
    */
   private String getAttemptAbsoluteDurationLimit( Node iNode )
   {
      String attemptAbsoluteDurationLimit = "";

      String nodeName = iNode.getLocalName();

      if ( nodeName.equals("item") )
      {
         Node sequencingNode = DOMTreeUtility.getNode( iNode, "sequencing" );
         if ( sequencingNode != null )
         {
            Node limitConditionsNode = DOMTreeUtility.getNode( sequencingNode,
                                                           "limitConditions" );
            if ( limitConditionsNode != null )
            {
               attemptAbsoluteDurationLimit = DOMTreeUtility.getAttributeValue(
                                              limitConditionsNode,
                                            "attemptAbsoluteDurationLimit" );
            }
         }
      }
      return attemptAbsoluteDurationLimit;
   }

   /**
    * This method retrieves the information described by the <item>
    * element and saves it for SCO launch data information.  This method
    * traverses the <item>s of the <organization> recursively and
    * retrieves the identifiers, referenced identifier references and
    * corresponding parameters from the <resources> element. <br>
    *
    * @param iNode The organization node.<br>
    *        iOrgID The ID of the organization.<br>
    *
    * @return void<br>
    */
   private void addItemInfo( Node iNode, String iOrgID )
   {
      mLogger.entering("ManifestHandler", "addItemInfo()");
      if ( iNode == null )
      {
         return;
      }

      int type = iNode.getNodeType();
      String orgID = iOrgID;

      switch ( type )
      {
         // document node
         // this is a fail safe case to handle an error where a document node
         // is passed
         case Node.DOCUMENT_NODE:
         {
            Node rootNode = ((Document)iNode).getDocumentElement();

            addItemInfo( rootNode, orgID );

            break;
         }

         // element node
         case Node.ELEMENT_NODE:
         {
            String nodeName = iNode.getLocalName();

            // get the needed values of the attributes
            if ( nodeName.equals("item") )
            {
               String orgIdentifier   = "";
               String identifier      = "";
               String identifierref   = "";
               String parameters      = "";
               String title           = "";
               String dataFromLMS     = "";
               String timeLimitAction = "";
               String completionThreshold = "";
               String objectiveslist = "";
               boolean previous       = false;
               boolean Continue       = false;
               boolean exit           = false;
               boolean abandon        = false;

               //Assign orgIdentifier the value of the parameter iOrgID
               orgIdentifier = iOrgID;

               // get the value of the following attributes:
               // - identifier
               // - identifierref
               // - parameters
               //
               // leave the value at "" is the attribute does not exist
               NamedNodeMap attrList = iNode.getAttributes();
               int numAttr = attrList.getLength();
               Attr currentAttrNode;
               String currentNodeName;

               // loop through the attributes and get their values assuming that
               // the multiplicity of each attribute is 1 and only 1.
               for ( int i = 0; i < numAttr; i++ )
               {
                  currentAttrNode = (Attr)attrList.item(i);
                  currentNodeName = currentAttrNode.getLocalName();

                  // store the value of the attribute
                  if ( currentNodeName.equalsIgnoreCase("identifier") )
                  {
                     identifier = currentAttrNode.getValue();
                  }
                  else if ( currentNodeName.equalsIgnoreCase("identifierref") )
                  {
                     identifierref = currentAttrNode.getValue();
                  }
                  else if ( currentNodeName.equalsIgnoreCase("parameters") )
                  {
                     parameters = currentAttrNode.getValue();
                  }

               }

               // get the value of the title element
               // assume that there is 1 and only 1 child named title
               title = DOMTreeUtility.getNodeValue(
                                      DOMTreeUtility.
                                      getNode( iNode, "title" ) );

               // get the value of the datafromlms element
               dataFromLMS = DOMTreeUtility.getNodeValue(
                                            DOMTreeUtility.
                                            getNode( iNode, "dataFromLMS" ) );

               // get the value of the timelimitaction element
               timeLimitAction = DOMTreeUtility.getNodeValue(
                                                DOMTreeUtility.
                                                getNode( iNode,
                                                        "timeLimitAction" ) );

               // get the value of the completionThreshold element
               completionThreshold = DOMTreeUtility.getNodeValue(
                                                DOMTreeUtility.
                                                getNode( iNode,
                                                      "completionThreshold" ) );


          //Gets the sequencing objectives list for this item
          objectiveslist = getObjectivesList(DOMTreeUtility.getNode(iNode, "sequencing"));

               //get the hideRTSUI elements and set the previous, continue,
               //exit and abandon variables accordingly.
               Node presentationNode =
                  DOMTreeUtility.getNode( iNode, "presentation" );
               if ( presentationNode != null )
               {
                  Node navInterfaceNode =
                     DOMTreeUtility.getNode( presentationNode,
                                             "navigationInterface" );
                  if ( navInterfaceNode != null )
                  {
                     NodeList children = navInterfaceNode.getChildNodes();
                     if (children != null)
                     {
                        int numChildren = children.getLength();
                        for ( int i = 0; i < numChildren; i++ )
                        {
                           Node currentChild = children.item( i );
                           String currentChildName =
                               currentChild.getLocalName();
                           if ( currentChildName.equals("hideLMSUI") )
                           {
                              String currentChildValue =
                                  DOMTreeUtility.getNodeValue( currentChild );
                              if (  currentChildValue.equals("previous") )
                              {
                                 previous = true;
                              }
                              else if ( currentChildValue.equals( "continue" ) )
                              {
                                 Continue = true;
                              }
                              else if ( currentChildValue.equals( "exit" ) )
                              {
                                 exit = true;
                              }
                              else if ( currentChildValue.equals( "abandon" ) )
                              {
                                 abandon = true;
                              }
                           }
                        }
                     }
                  }
               }

               // make sure this item actually points to a <resource>
               if ( ! identifierref.equals("") )
               {
                  // create an instance of the LaunchData data structure and
                  // add it to the LaunchDataList
                  LaunchData launchData = new LaunchData();

                  launchData.setOrganizationIdentifier( orgIdentifier );
                  launchData.setItemIdentifier( identifier );
                  launchData.setResourceIdentifier( identifierref );
                  launchData.setParameters( parameters );
                  launchData.setItemTitle( title );
                  launchData.setDataFromLMS( dataFromLMS );
                  launchData.setTimeLimitAction( timeLimitAction );
                  launchData.setCompletionThreshold( completionThreshold );
                  launchData.setPrevious( previous );
                  launchData.setContinue( Continue );
                  launchData.setExit( exit );
                  launchData.setAbandon( abandon );
                  launchData.setMinNormalizedMeasure(
                                             getMinNormalizedMeasure( iNode ) );
                  launchData.setAttemptAbsoluteDurationLimit(
                                     getAttemptAbsoluteDurationLimit( iNode ) );
        launchData.setObjectivesList(objectiveslist);

                  mLaunchDataList.add( launchData );
               }
            }

            // get the child nodes and add their items info
            NodeList children = iNode.getChildNodes();

            if ( children != null )
            {
               int numChildren = children.getLength();
               Node currentChild;

               for ( int z = 0; z < numChildren; z++ )
               {
                  currentChild = children.item(z);
                  addItemInfo( currentChild, orgID );
               }
            }
         }
         // handle all other node types
         default:
         {
            break;
         }
      }
   }


   /**
    * This method gets all the sequencing objectives associated with the
    * current item.<br>
    *
    * @param iNode root item node.<br>
    */
   private String getObjectivesList(Node iNode)
   {
      int j, k;
      NamedNodeMap AttributesList = null;
      String result = "";

      // Gets to the objectives node, if one exists
      if (iNode != null)
      {
         Node objNode = DOMTreeUtility.getNode(iNode, "objectives");

         if (objNode != null)
         {
            //Gets the primary objective id
            Node primaryObjNode = DOMTreeUtility.getNode( objNode, "primaryObjective" );
            if (primaryObjNode != null)
            {
               AttributesList = primaryObjNode.getAttributes();

               // iterate through the NamedNodeMap and get the attribute names and values
               for(j = 0; j < AttributesList.getLength(); j++)
               {
                  //Finds the schema location and parses out values
                  if (AttributesList.item(j).getLocalName().equalsIgnoreCase("objectiveID"))
                  {
                     result = AttributesList.item(j).getNodeValue();
                  }
               }
            }

            //Gets all objective ids
            Vector objNodes = DOMTreeUtility.getNodes(objNode, "objective");

            for(j = 0; j < objNodes.size(); j++)
            {
               Node currNode = (Node)objNodes.elementAt(j);
               AttributesList = currNode.getAttributes();

               // iterate through the NamedNodeMap and get the attribute names and values
               for(k = 0; k < AttributesList.getLength(); k++)
               {
                  //Finds the schema location and parses out values
                  if (AttributesList.item(k).getLocalName().equalsIgnoreCase("objectiveID"))
                  {
                     result = result + "," + AttributesList.item(k).getNodeValue();
                  }
               }
            } // end looping over nodes
         } // end if objNode != null
      } // end if iNode != null

      //returns objective list, if it was found.
      return result;
   }



   /**
    * This method uses the information stored in the SCO Launch Data List
    * to get the associated Resource level data.<br>
    *
    * @param iRootNode root node of the DOM.<br>
    * @param iRemoveAssets boolean representing whether or not the assets should
    *                      be removed.  (The Sample RTE will never want to
    *                      remove the assets, where as the TestSuite will.)
    */
   private void addResourceInfo( Node iRootNode, boolean iRemoveAssets )
   {
      // get the <resources> node
      Node resourcesNode = DOMTreeUtility.getNode( iRootNode, "resources" );

      String scormType = "";
      String location  = "";
      String xmlBase   = "";

      // launch data processing stuff
      int size = mLaunchDataList.size();
      LaunchData currentLaunchData;
      String resourceIdentifier = "";
      String persistState    = "";
      Node matchingResourceNode = null;

      // here we are dealing with a content aggregation package
      for ( int i = 0; i < size; i++ )
      {
         currentLaunchData = (LaunchData)mLaunchDataList.elementAt(i);
         resourceIdentifier = currentLaunchData.getResourceIdentifier();

         matchingResourceNode =
            getResourceNodeWithIdentifier( resourcesNode, resourceIdentifier );

         // get the value of the following attributes:
         // - adlcp:scormtype
         // - href
         // - xml:base
         //
         // leave the value at "" is the attribute does not exist
         scormType = DOMTreeUtility.getAttributeValue( matchingResourceNode,
                                                       "scormType" );
         location  = DOMTreeUtility.getAttributeValue( matchingResourceNode,
                                                       "href" );
         xmlBase   = DOMTreeUtility.getAttributeValue( matchingResourceNode,
                                                       "base" );
         persistState = DOMTreeUtility.getAttributeValue(matchingResourceNode,
                                                         "persistState");

         // populate the current Launch Data with the resource level values
         currentLaunchData.setSCORMType( scormType );
         currentLaunchData.setLocation( location );
         currentLaunchData.setResourceXMLBase( xmlBase );
         currentLaunchData.setPersistState( persistState );

         try
         {
            mLaunchDataList.set( i, currentLaunchData );
         }
         catch ( ArrayIndexOutOfBoundsException aioobe )
         {
            System.out.println( "ArrayIndexOutOfBoundsException caught on " +
                                "Vector currentLaunchData.  Attempted index " +
                                "access is " + i + "size of Vector is " +
                                mLaunchDataList.size() );
         }
      }

      if ( size == 0 ) // then we are dealing with a resource package
      {
         // loop through resources to retieve all resource information
         // loop through the children of <resources>
         NodeList children = resourcesNode.getChildNodes();
         int childrenSize = children.getLength();

         if ( children != null )
         {
            for ( int z = 0; z < childrenSize; z++ )
            {
                Node currentNode = children.item( z );
                String currentNodeName = currentNode.getLocalName();

                if ( currentNodeName.equals("resource") )
                {
                   // create an instance of the LaunchData data structure and
                   // add it to the LaunchDataList
                   LaunchData launchData = new LaunchData();

                   // get the value adlcp:scormtype, href, base attribute
                   // leave the value at "" is the attribute does not exist
                   scormType = DOMTreeUtility.getAttributeValue( currentNode,
                                                                 "scormType" );

                   location  = DOMTreeUtility.getAttributeValue( currentNode,
                                                                 "href" );

                   xmlBase   = DOMTreeUtility.getAttributeValue( currentNode,
                                                                 "base" );

                   resourceIdentifier = DOMTreeUtility.getAttributeValue(
                                                                 currentNode,
                                                                 "identifier" );

                   // populate the  Launch Data with the resource level values
                   launchData.setSCORMType( scormType );
                   launchData.setLocation( location );
                   launchData.setResourceXMLBase( xmlBase );
                   launchData.setResourceIdentifier( resourceIdentifier );

                   mLaunchDataList.add( launchData );
                } // end if current node == resource
            } // end looping over children
         } // end if there are no children
      } // end if size == 0

      if( iRemoveAssets )
      {
         removeAssetsFromLaunchDataList();
      }
   }

   /**
    * This method retrieves the resource node that matches the passed in
    * identifier value.  This method serves as a helper method.<br>
    *
    * @param iResourcesNode Parent resources node of the resource elements.<br>
    * @param iResourceIdentifier identifier value of the resource node being
    * retrieved.<br>
    *
    * @return Node resource element node that matches the identifier value.<br>
    */
   private Node getResourceNodeWithIdentifier( Node iResourcesNode,
                                               String iResourceIdentifier )
   {
      Node result = null;

      // loop through the children of <resources>
      NodeList children = iResourcesNode.getChildNodes();

      if ( children != null )
      {
         int numChildren = children.getLength();
         Node currentChild = null;
         String currentChildName = "";
         String currentResourceIdentifier = "";

         for ( int i = 0; i < numChildren; i++ )
         {
            currentChild = children.item(i);
            currentChildName = currentChild.getLocalName();

            // locate the <resource> Nodes
            if ( currentChildName.equals( "resource" ) )
            {
               // get the identifier attribute of the current <resource> Node
               currentResourceIdentifier =
                  DOMTreeUtility.getAttributeValue( currentChild, "identifier" );

               // match the identifier attributes and get the missing data
               if ( currentResourceIdentifier.equals(iResourceIdentifier)  )
               {
                  result = currentChild;
                  break;
               }
            } // end if currentChildName == resource
         } // end looping over children
      } // end if there are no children

      return result;
   }

   /**
    * This method removes the asset information from the launch data list.
    * Assets are not launchable resources. <br>
    *
    * @return void<br>
    */
   private void removeAssetsFromLaunchDataList()
   {

      int size = mLaunchDataList.size();
      LaunchData currentLaunchData;
      String scormType = "";

      for ( int i = 0; i < size; )
      {
         currentLaunchData = (LaunchData)mLaunchDataList.elementAt(i);
         scormType = currentLaunchData.getSCORMType();

         if ( scormType.equals( "asset" ) )
         {
            mLaunchDataList.removeElementAt( i );
            size = mLaunchDataList.size();
         }
         else
         {
            i++;
         }
      }
   }
}
