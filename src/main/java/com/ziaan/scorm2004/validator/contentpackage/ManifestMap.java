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
package com.ziaan.scorm2004.validator.contentpackage;

// native java imports

import java.util.Vector;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ziaan.scorm2004.parsers.dom.DOMTreeUtility;
import com.ziaan.scorm2004.util.Message;
import com.ziaan.scorm2004.util.MessageCollection;
import com.ziaan.scorm2004.util.MessageType;

/**
 *
 * <strong>Filename: </strong><br>ManifestMap.java<br><br>
 *
 * <strong>Description: </strong><br> A <CODE>ManifestMap</CODE> is a Data
 * Structure used to store manifest information that is necessary for for the
 * validation and processing of (Sub) manifests.<br><br>
 *
 * <strong>Design Issues: </strong><br>None<br>
 * <br>
 *
 * <strong>Implementation Issues: </strong><br>None<br><br>
 *
 * <strong>Known Problems: </strong><br>None<br><br>
 *
 * <strong>Side Effects: </strong><br>Populates the MessageCollection<br><br>
 *
 * <strong>References: </strong><br>None<br><br>
 *
 * @author ADL Technical Team
 */

public class ManifestMap
{
   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;

   /**
    * The identifier attribute of the <manifest> element.<br>
    */
   private String mManifestId;

   /**
    * The identifier attributes of all <resource> elements that belong to the
    * <manifest> element of mManifestId.<br><br>
    */
   private Vector mResourceIds;

   /**
    * The identifier attributes of all <item> elements that belong to the
    * <manifest> element of mManifestId.<br>
    */
   private Vector mItemIds;

   /**
    * The identifier reference values of all <item> elements that belong to the
    * <manifest> element of mManifestId.<br>
    */
   private Vector mItemIdrefs;

   /**
    * The identifier reference values of all <dependency> elements that belong to
    * the <manifest> element of mManifestId.<br>
    */
   private Vector mDependencyIdrefs;


   /**
    * The ManifestMap objects for all (Sub) manifest elements.<br>
    */
   private Vector mManifestMaps;

   /**
    * The boolean describing if the manifest utilizes (Sub) manifest.<br>
    */
   private boolean mDoSubmanifestExist;

   /**
    * A vector containing a list of Idrefs that reference subManifest elements.
    */
   private Vector mSubManifestIDrefs;

   /**
    * The identifier determining what type of (Sub) manifest is to be validated
    * for.  If an item identifierref points to a (Sub) manifest identifier value,
    * than the (Sub) manifest qualifies as "content aggregation" for this
    * attribute value - allowing 1 and only 1 organization element in the
    * (Sub) manifest.  If an item identfiierref value points to only a resource
    * identifier in a (Sub) manifest, the the (Sub) manifest qualifies as
    * "other" for this attribute - allowing 0 or 1 organization element in the
    * (Sub) manifest.
    */
   private String mApplicationProfile;


   /**
    * The default constructor.<br>
    */
   public ManifestMap()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mManifestId                   = new String();
      mResourceIds                  = new Vector();
      mItemIds                      = new Vector();
      mItemIdrefs                   = new Vector();
      mManifestMaps                 = new Vector();
      mDoSubmanifestExist           = false;
      mApplicationProfile           = new String();
      mDependencyIdrefs             = new Vector();
      mSubManifestIDrefs            = new Vector();
   }


   /**
    *
    * Gives access to the identifier value of the <manifest> element.<br>
    *
    * @return - The identifier value of the <manifest> element.<br>
    */
   public String getManifestId()
   {
      return mManifestId;
   }

   /**
    *
    * Gives access to the identifier attributes of all <resource> elements that
    * belong to the <manifest> element of mManifestId.<br>
    *
    * @return - The identifier attributes of all <resource> elements that
    * belong to the <manifest> element of mManifestId.<br>
    */
   public Vector getResourceIds()
   {
      return mResourceIds;
   }

   /**
    *
    * Gives access to the identifier attributes of all <item> elements that
    * belong to the <manifest> element of mManifestId.<br>
    *
    * @return - The identifier attributes of all <item> elements that
    * belong to the <manifest> element of mManifestId.<br>
    */
   public Vector getItemIds()
   {
      return mItemIds;
   }

   /**
    *
    * Gives access to the identifier reference values of all <item> elements
    * that belong to the <manifest> element of mManifestId.<br>
    *
    * @return - The identifier reference values of all <item> elements that
    * belong to the <manifest> element of mManifestId.<br>
    */
   public Vector getItemIdrefs()
   {
      return mItemIdrefs;
   }

   /**
    *
    * Gives access to the identifier reference values of all <dependency>
    * elements that belong to the <manifest> element of mManifestId.<br>
    *
    * @return - The identifier reference values of all <dependency> elements
    * that belong to the <manifest> element of mManifestId.<br>
    */
   public Vector getDependencyIdrefs()
   {
      return mDependencyIdrefs;
   }

   /**
    *
    * Gives access to the ManifestMap objects for all (Sub) manifest elements.
    * <br>
    *
    * @return - The ManifestMap objects for all (Sub) manifest elements.<br>
    */
   public Vector getManifestMaps()
   {
      return mManifestMaps;
   }

   /**
    *
    * Gives access to the boolean describing if the manifest utilizes
    * (Sub) manifest.<br>
    *
    * @return - The boolean describing if the manifest utilizes (Sub) manifest.
    * <br>
    */
   public boolean getDoSubmanifestExist()
   {
      return mDoSubmanifestExist;
   }

   /**
    *
    * Gives access to the String describing which Application Profile the
    * (Sub) manifest adheres to.<br>
    *
    * @return - The boolean describing if the manifest utilizes (Sub) manifest.
    * <br>
    */
   public String getApplicationProfile()
   {
      return mApplicationProfile;
   }

   /**
    *
    * Gives access to the String describing which Application Profile the
    * (Sub) manifest adheres to.<br>
    *
    * @return - The boolean describing if the manifest utilizes (Sub) manifest.
    * <br>
    */
   public void setApplicationProfile( String iApplicationProfile )
   {
      mApplicationProfile = iApplicationProfile;
   }

   /**
    *
    * Gives access to the list of IDRefs that reference (Sub) manifest elements.
    * <br>
    *
    * @return - The ManifestMap objects for all (Sub) manifest elements.<br>
    */
   public Vector getSubManifestIDrefs()
   {
      return mSubManifestIDrefs;
   }



   /**
    *
    * This method populates the ManifestMap object by traversing down
    * the document node and storing all information necessary for the validation
    * of (Sub) manifests.  Information stored for each manifest element includes:
    * manifest identifiers,item identifers, item identifierrefs, and
    * resource identifiers<br>
    *
    * @return - The boolean describing if the ManifestMap object(s) has been
    * populated properly.<br>
    */
   public boolean populateManifestMap( Node iNode )
   {
      // looks exactly like prunetree as we walk down the tree
      mLogger.entering( "ManifestMap", "populateManifestMap" );

      boolean result = true;

      // is there anything to do?
      if ( iNode == null )
      {
         result = false;
         return result;
      }

      int type = iNode.getNodeType();
      String msgText = new String();

      switch ( type )
      {
         case Node.PROCESSING_INSTRUCTION_NODE:
         {
            break;
         }
         case Node.DOCUMENT_NODE:
         {
            Node rootNode = ((Document)iNode).getDocumentElement();

            result = populateManifestMap( rootNode ) && result;

            break;
         }
         case Node.ELEMENT_NODE:
         {
            String parentNodeName = iNode.getLocalName();

            if ( parentNodeName.equalsIgnoreCase("manifest") )
            {
               // We are dealing with an IMS <manifest> element, get the IMS
               // CP identifier for the <manifest> elememnt
               mManifestId =
                  DOMTreeUtility.getAttributeValue( iNode,
                                                    "identifier" );

               mLogger.finest( "ManifestMap:populateManifestMap, " +
                               "Just stored a Manifest Id value of " +
                                mManifestId );

               // Recurse to populate mItemIdrefs and mItemIds

               // Find the <organization> elements

               Node orgsNode = DOMTreeUtility.getNode( iNode, "organizations" );

               if( orgsNode != null )
               {
                  Vector orgElems = DOMTreeUtility.getNodes( orgsNode, "organization" );

                  mLogger.finest( "ManifestMap:populateManifestMap, " +
                                  "Number of <organization> elements: " +
                                   orgElems.size() );

                  if ( !orgElems.isEmpty() )
                  {
                     int orgElemsSize = orgElems.size();
                     for (int i = 0; i < orgElemsSize; i++ )
                     {
                        Vector itemElems = DOMTreeUtility.getNodes(
                                            (Node)orgElems.elementAt(i), "item" );

                        mLogger.finest( "ManifestMap:populateManifestMap, " +
                                        "Number of <item> elements: " +
                                         itemElems.size() );

                        if ( !itemElems.isEmpty() )
                        {
                           int itemElemsSize = itemElems.size();
                           for (int j = 0; j < itemElemsSize; j++ )
                           {
                              result = populateManifestMap(
                                 (Node)(itemElems.elementAt(j)) ) && result;
                           }
                        }
                     }
                  }
               }

               //recurse to populate mResourceIds

               Node resourcesNode = DOMTreeUtility.getNode( iNode, "resources" );

               if( resourcesNode != null )
               {
                  Vector resourceElems = DOMTreeUtility.getNodes(
                                                  resourcesNode, "resource" );

                  mLogger.finest( "ManifestMap:populateManifestMap, " +
                               "Number of <resource> elements: " +
                                resourceElems.size() );

                  int resourceElemsSize = resourceElems.size();
                  for (int k = 0; k < resourceElemsSize; k++ )
                  {
                     result = populateManifestMap(
                                 (Node)(resourceElems.elementAt(k)) ) && result;

                  }
               }

               //recurse to populate mManifestMaps

               //find the <manifest> elements (a.k.a sub-manifests)
               Vector subManifests =
                                   DOMTreeUtility.getNodes( iNode, "manifest" );

               mLogger.finest( "ManifestMap:populateManifestMap, " +
                               "Number of (Sub) manifest elements: " +
                                subManifests.size() );

               if ( !subManifests.isEmpty() )
               {
                  mDoSubmanifestExist = true;
                  int subManifestSize = subManifests.size();
                  for (int l = 0; l < subManifestSize; l++ )
                  {
                     ManifestMap manifestMapObject = new ManifestMap();
                     result = manifestMapObject.populateManifestMap(
                                    (Node)subManifests.elementAt(l) ) && result;
                     mManifestMaps.add( manifestMapObject );
                  }

               }
            }
            else if ( parentNodeName.equalsIgnoreCase("item") )
            {
               //store item identifier value
               String itemId =
                        DOMTreeUtility.getAttributeValue( iNode, "identifier" );
               mItemIds.add( itemId );

               mLogger.finest( "ManifestMap:populateManifestMap, " +
                               "Just stored an Item Id value of " +
                                itemId );

               //store item identifier reference value
               String itemIdref =
                     DOMTreeUtility.getAttributeValue( iNode, "identifierref" );
               mItemIdrefs.add( itemIdref  );

               mLogger.finest( "ManifestMap:populateManifestMap, " +
                               "Just stored an Item Idref value of " +
                                itemIdref );

               //recurse to populate all child item elements
               Vector items = DOMTreeUtility.getNodes( iNode, "item" );
               if ( !items.isEmpty() )
               {
                  int itemsSize = items.size();
                  for ( int z = 0; z < itemsSize; z++ )
                  {
                     result = populateManifestMap(
                        (Node)items.elementAt(z) ) && result;
                  }
               }
            }
            else if ( parentNodeName.equalsIgnoreCase("resource") )
            {
               //store resource identifier value
               String resourceId =
                        DOMTreeUtility.getAttributeValue( iNode, "identifier" );
               mResourceIds.add( resourceId );

               mLogger.finest( "ManifestMap:populateManifestMap, " +
                               "Just stored a Resource Id value of " +
                                resourceId );

               // populate <dependency> element

               Vector dependencyElems = DOMTreeUtility.getNodes( iNode,
                                                                 "dependency" );

               int dependencyElemsSize= dependencyElems.size();

               for(int w=0; w < dependencyElemsSize; w++ )
               {
                  Node dependencyElem = (Node)dependencyElems.elementAt(w);

                  //store resource identifier value
                  String dependencyIdref =
                        DOMTreeUtility.getAttributeValue( dependencyElem,
                                                          "identifierref" );
                  mDependencyIdrefs.add( dependencyIdref );

                  mLogger.finest( "ManifestMap:populateManifestMap, " +
                                  "Just stored a Dependency Idref value of " +
                                   mDependencyIdrefs );

               }



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
      }

      mLogger.exiting( "ManifestMap", "populateManifestMap" );

      return result;
   }

   /**
    *
    * This method drives the recursive validation of the referencing of
    * identifierref values.  It spans the validation of identifierrefs for
    * each identifierref value.<br>
    *
    * @return - The Vector containing the identifierref value(s) that do not
    * reference valid identifers.<br>
    *
    */
   public Vector checkAllIdReferences()
   {
     Vector resultVector = new Vector();
     String msgText = new String();
     String idrefValue = new String();

     if ( !mItemIdrefs.isEmpty() )
     {
        int mItemIdrefsSize = mItemIdrefs.size();
        for ( int i = 0; i < mItemIdrefsSize; i++ )
        {
           idrefValue = (String)mItemIdrefs.elementAt(i);

           if ( !idrefValue.equals("") )
           {
              msgText = "Testing identifierRef value \"" + idrefValue +
                      "\" for valid referencing";
              mLogger.info( "INFO: " + msgText );
              MessageCollection.getInstance().add( new Message( MessageType.INFO,
                                                              msgText ) );

              boolean iItemdrefResult = checkIdReference( idrefValue, false );

              // track all idref values whose reference was not valid

              if ( !iItemdrefResult )
              {
                 msgText = "The identifierref value \"" + idrefValue +
                      "\" does not properly reference a valid identifier.";
                 mLogger.info( "FAILED: " + msgText );
                 MessageCollection.getInstance().add( new Message(
                    MessageType.FAILED, msgText ) );

                 resultVector.add( idrefValue );
              }
           }
        }
     }

     if ( !mDependencyIdrefs.isEmpty() )
     {
        int mDependencyIdrefsSize = mDependencyIdrefs.size();
        for ( int i = 0; i < mDependencyIdrefsSize; i++ )
        {
           idrefValue = (String)mDependencyIdrefs.elementAt(i);

           if ( !idrefValue.equals("") )
           {
              msgText = "Testing identifierRef value \"" + idrefValue +
                      "\" for valid referencing";
              mLogger.info( "INFO: " + msgText );
              MessageCollection.getInstance().add( new Message( MessageType.INFO,
                                                              msgText ) );

              boolean iDependencydrefResult = checkIdReference( idrefValue, false );

              // track all idref values whose reference was not valid

              if ( !iDependencydrefResult )
              {
                 msgText = "The identifierref value \"" + idrefValue +
                      "\" does not properly reference a valid identifier.";
                 mLogger.info( "FAILED: " + msgText );
                 MessageCollection.getInstance().add( new Message(
                    MessageType.FAILED, msgText ) );

                 resultVector.add( idrefValue );
              }
           }
        }
     }

     // test all identifierref values in (Sub) manifest elements as well

     if ( !mManifestMaps.isEmpty() )
     {
        int mManifestMapsSize =  mManifestMaps.size();
        for ( int j = 0; j < mManifestMapsSize; j++ )
        {
           Vector manifestVec =
              ((ManifestMap)mManifestMaps.elementAt(j)).checkAllIdReferences();

           // append all idref values that are not valid to the result vector

           int manifestVecSize = manifestVec.size();
           for ( int k = 0; k < manifestVecSize; k++ )
           {
              resultVector.add( (String)manifestVec.elementAt(k) );
           }
        }
     }
     return resultVector;
   }

   /**
    *
    * This method validates that the incoming identifierref value properly
    * references a valid identifier.  An error is throw for identifierref
    * values that performs backwards or sideward referencing, or does not
    * reference an identifier value at all. <br>
    *
    * @return - The Vector containing the identifierref value(s) that do not
    * reference valid identifers.<br>
    *
    */
   public boolean checkIdReference( String iIdref, boolean iInSubManifest )
   {
      boolean result = true;
      String msgText = new String();

      // loop through resourceIds and compare to incoming idref value
      if ( !mResourceIds.isEmpty() )
      {
         int mResourceIdsSize = mResourceIds.size();
         for ( int i = 0; i < mResourceIdsSize; i++ )
         {
            String resourceId = (String)mResourceIds.elementAt(i);
            msgText = "Comparing " + iIdref + " to " + resourceId;
            mLogger.info( msgText );

            if ( iIdref.equals( resourceId ) )
            {
               result = true;

               msgText = "Identifierref value " + iIdref + " properly " +
                         "references the valid resource identifier value " +
                          resourceId;
               mLogger.info( "PASSED: " + msgText );
               MessageCollection.getInstance().add( new Message(
                                                            MessageType.PASSED,
                                                            msgText ) );
               // set application profile to other only if it does not already
               // equal content aggregation.  Other triggers the need for
               // an additional check that will allow 0 or more orgs.
               String currentAppProfile = getApplicationProfile();

               if ( !currentAppProfile.equals("contentaggregation") &&
                    iInSubManifest )
               {
                  setApplicationProfile("other");
               }
               else
               {
                  setApplicationProfile("contentaggregation");
               }

               msgText = "IDRef " + iIdref + " points to a resource " +
                          resourceId + " , app profile is " +
                          getApplicationProfile() + " for " + getManifestId();
               mLogger.info(msgText);

               return result;
            }
         }
      }
      // compare to manifestId of (Sub) manifest elements

      msgText = iIdref + " did not match the resourceIds, " +
                " now checking ManifestMaps";
      mLogger.info( msgText );


      if ( !mManifestMaps.isEmpty() )
      {
         int mManifestMapsSize = mManifestMaps.size();
         for (int j = 0; j < mManifestMapsSize; j++ )
         {
            ManifestMap map = (ManifestMap)mManifestMaps.elementAt(j);
            String mapManifestId = map.getManifestId();
            if ( iIdref.equals( mapManifestId ) )
            {
               result = true;

               msgText = "Identifierref value " + iIdref + " properly " +
                         "references the valid manifest identifier value " +
                          mapManifestId;
               mLogger.info( "PASSED: " + msgText );
               MessageCollection.getInstance().add( new Message(
                                                         MessageType.PASSED,
                                                         msgText ) );

               // set application profile to be content aggregation, triggering
               // the check that 1 and only 1 org may exist.
               map.setApplicationProfile("contentaggregation");

               msgText = "Idref " + iIdref + " points to a manifestId " +
                          mapManifestId + " app profile is " +
                          map.getApplicationProfile() + " for " +
                          map.getManifestId();
               mLogger.info( msgText );

               mSubManifestIDrefs.add( iIdref );

               return result;

            }
            else
            {
               //loop thru mapManifest to recuse with idref values
               result = map.checkIdReference( iIdref, true ) && result;
            }

         }
      }
      else  // manifestMaps is empty
      {
         result = false;
         msgText= "Identifierref value " + iIdref + " does have a valid " +
                  "identifier match";
         mLogger.info( msgText );
      }

      msgText = "Returning " + result + "from checkIdReference";
      mLogger.info( msgText );

      return result;
   }



}
