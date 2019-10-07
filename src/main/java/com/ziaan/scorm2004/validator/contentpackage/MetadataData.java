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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ziaan.scorm2004.parsers.dom.DOMTreeUtility;
import com.ziaan.scorm2004.validator.LOMMetadata;
import com.ziaan.scorm2004.validator.MetadataCommonDataType;

// adl imports

/**
 *
 * <strong>Filename: </strong><br>MetadataData.java<br><br>
 *
 * <strong>Description: </strong><br>A <CODE>MetadataData</CODE> is a Data
 * Structure used to store information for the validation of Metadata found
 * in the Manifest.  This data structure tracks inline metadata (extensions to
 * the imsmanifest) as well as the location of the external metadata instances.
 * The metadata application profile type of each metadata tracked is stored in
 * this data structure as well.<br><br>
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
public class MetadataData
{
    private static boolean _Debug = false;
    
   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;

   /**
    * This attribute stores the Application Profile of the metadata found within
    * the <metadata> tag of the Content Package.  Valid values include:
    *  -"asset"<br>
    *  -"sco" <br>
    *  -"asset" <br>
    *  -"contentorganization" <br>
    *  -"activity"
    */
   private String mApplicationProfileType;

   /**
    * This attribute stores the inline metadata, specifically in the form of its
    * root node.<br>
    */
   private Node mRootLOMNode;

   /**
    * This attribute serves as the file location of the external metadata test
    * subject.  The attribute value "inline" denotes that an inline metadata
    * lom element exists.  Otherwise, the uri location of the stand alone test
    * subject is stored here.<br>
    */
   private String mLocation;

   /**
    * This attribute stores the identifier value of the major elements
    * (item, orgs, etc/) that house the metadata instance.<br>
    */
   private String mIdentifier;

   /**
    * Metadata Seq
    */
   private int mMetadataSeq;
   
   /**
    * Resource File Seq
    */
   private int mResourceFileSeq;
   
   /**
    * List of LOMMetadata. This is data of LOM_METADATA Table. 
    */
   private Vector mLOMMetadataList;   

   
   /**
    * The default constructor. Sets the attributes to their initial values.<br>
    */
   public MetadataData()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mApplicationProfileType = new String();
      mRootLOMNode = null;
      mLocation = new String();
      mLOMMetadataList = new Vector();
   }

   /**
    * This method returns the application profile type of the metadata instance.
    * Valid values include:<br>
    * -"asset" <br>
    * -"contentaggregation" <br>
    * -"sco" <br>
    * -"contentorganization" <br>
    * -"activity"<br>
    */
   public String getApplicationProfileType()
   {
      return mApplicationProfileType;
   }

   /**
    * This method returns the uri location value of the external metadata
    * instance. If the metadata instance is in the form of inline metadata,
    * then the value returned will be "inline".<br>
    *
    * @return String location value of the metadata test subject.<br>
    */
   public String getLocation()
   {
      return mLocation;
   }

   /**
    * This method retruns the root node of the inline metadata if it exists
    * in the form of extensions to the imsmanifest file.<br>
    *
    * @return Node root lom node of the inline metadata.<br>
    */
   public Node getRootLOMNode()
   {
      return mRootLOMNode;
   }

   /**
    * This method returns the identifier attribute which stores the identifier
    * value of the major elements (item, orgs, etc/) that house the metadata
    * instance.<br>
    *
    * @return String The identifier value of the parent of the metadata.<br>
    */
   public String getIdentifier()
   {
      return mIdentifier;
   }

   /**
    * This method returns a boolean value based on the form of metadata.  If the
    * metadata is in the form of inline metadata, than the boolean value
    * "true" is returned.  If the metadata is in the form of external standalone
    * metadata, than the boolean value of "false" is returned.<br>
    *
    * @return boolean True if the metadata is inline, false otherwise.<br>
    */
   public boolean isInlineMetadata()
   {
      boolean result = true;
      if ( getRootLOMNode() == null )
      {
         result = false;
      }
      return result;
   }

   /**
    * This method sets the application profile type of the metadata.
    * Valid set values include:<br>
    * - "asset"<br>
    * - "sco"<br>
    * - "contentorganization"<br>
    *
    * @param String indicating the application profile value to be set.<br>
    */
   public void setApplicationProfileType( String iApplicationProfileType )
   {
      if ( _Debug )
      {
          System.out.println( "  -> App Profile Type : " + iApplicationProfileType );
      }
      
      mApplicationProfileType = iApplicationProfileType;
   }

   /**
    * This method sets the file location of the external metadata test
    * subject if the metadata is external to the package. If the metadata is
    * inline, than the value of "inline" is set.<br>
    *
    * @param String indicating the location value to be set.<br>
    */
   public void setLocation( String iLocation )
   {
      mLocation = iLocation;
   }

   /**
    * This method sets the root document node, if the metadata exists in the
    * form of inline metadata.<br>
    *
    * @param Node indicating the root lom node to be set.<br>
    */
   public void setRootLOMNode( Node iNode )
   {
      mRootLOMNode = iNode;
      
      setLOMMetadataList( mRootLOMNode );
   }

   /**
    * This method sets the identifier value of the major elements
    * (item, orgs, etc/) that house the metadata instance.<br>
    *
    * @param String indicating the identifier value to be set.<br>
    */
   public void setIdentifier( String iIdentifier )
   {
      mIdentifier = iIdentifier;
   }

   /**
    *  Tbis method prints to the java console for debug purposes of
    * the developer only.<br>
    */
   public void printToConsole()
   {
      System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
      System.out.println("@@@ MetadataData Info");
      System.out.println("mApplicationProfileType is " + mApplicationProfileType);
      System.out.println("mLocation is " + mLocation);
      System.out.println("mRootLOMNode is " + mRootLOMNode );
      System.out.println("mIdentifier is " + mIdentifier );
      System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
   }

   /**
    *  This method print to the java console for debug purposes
    * using the java logger.<br>
    */
   public void print()
   {
      mLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
      mLogger.info("@@@ MetadataData Info");
      mLogger.info("mApplicationProfileType is " + mApplicationProfileType);
      mLogger.info("mLocation is " + mLocation);
      mLogger.info("mRootLOMNode is " + mRootLOMNode );
      mLogger.info("mIdentifier is " + mIdentifier );
      mLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
   }

   
   public void setMetadataSeq( int metadataSeq )
   {
       mMetadataSeq = metadataSeq;
   }
   
   public int getMetadataSeq()
   {
       return mMetadataSeq;
   }

   public void setResourceFileSeq( int resourceFileCount )
   {
       mResourceFileSeq = resourceFileCount;
   }
   
   public int getResourceFileSeq()
   {
       return mResourceFileSeq;
   }

   public Vector getLOMMetadataList()
   {
       return mLOMMetadataList;
   }

   private void setLOMMetadataList( Node lomNode )
   {
       if ( lomNode != null )
       {
           NodeList nodeChildren = lomNode.getChildNodes();
           if (nodeChildren != null)
           {
               for ( int i=0; i<nodeChildren.getLength(); i++ )
               {
                   mSeq++;
                   Node node = nodeChildren.item(i);

                   String metaType = node.getNodeName();
                   setLOMMetadata( node, metaType, mSeq, 0 );
               }
           }
       }
   }
   
   private int mSeq = 0;
   
   /**
    * <lom> element를 parsing 해서,
    * LOM_METADATA Object를 List에 Set 한다. 
    * 
    * @param lomNode
    * @return
    */   
   private void setLOMMetadata( Node node, String metaType, int seq, int pseq )
   {
       
       /*
       LOMMetadata lomMetadata = new LOMMetadata();
       
       lomMetadata.setMetaType( "general" );
       lomMetadata.setSeq( 1 );
       lomMetadata.setPseq( 0 );
       lomMetadata.setElementName( "keyword" );
       lomMetadata.setValue1( "this is keyword" );
       lomMetadata.setLangString( "EN" );
       
       lomMetadataList.add( lomMetadata );
       */
       
       if ( node != null && node.getNodeType() == Node.ELEMENT_NODE )
       {
           LOMMetadata lomMetadata = new LOMMetadata();
           
           lomMetadata.setMetaType( metaType );
           lomMetadata.setSeq( seq );
           lomMetadata.setPseq( pseq );
           lomMetadata.setElementName( node.getNodeName() );
           
           if ( _Debug )
           {
               System.out.println("    --> Full : " + MetadataCommonDataType.getFullElementName(node) );
           }
           
           int type = MetadataCommonDataType.getDataType(MetadataCommonDataType.getFullElementName(node));

           switch( type )
           {
               case MetadataCommonDataType.VOCABULARY:
               {
                   Node sourceNode = DOMTreeUtility.getNode(node, "source" );
                   Node valueNode =  DOMTreeUtility.getNode(node, "value" );
                   
                   lomMetadata.setValue1( DOMTreeUtility.getNodeValue(sourceNode) );
                   lomMetadata.setValue2( DOMTreeUtility.getNodeValue(valueNode) );
    
                   if ( _Debug )
                   {
                       System.out.println("      --> Vocabulary Source : " + DOMTreeUtility.getNodeValue(sourceNode) );
                       System.out.println("      --> Vocabulary Value  : " + DOMTreeUtility.getNodeValue(valueNode) );
                   }
                   
                   mLOMMetadataList.add( lomMetadata );
                   break;
               }
               case MetadataCommonDataType.LANGSTRING:
               {
                   Node stringNode = DOMTreeUtility.getNode(node, "string" );
                   lomMetadata.setValue1( DOMTreeUtility.getNodeValue(stringNode) );
                   lomMetadata.setLangString( DOMTreeUtility.getAttributeValue(stringNode, "language") );
    
                   if ( _Debug )
                   {
                       System.out.println("      --> LangString Value  : " + DOMTreeUtility.getNodeValue(stringNode) );
                       System.out.println("      --> LangString Attribute language : " + DOMTreeUtility.getAttributeValue(stringNode, "language") );
                   }
                   
                   mLOMMetadataList.add( lomMetadata );
                   break;
               }
               case MetadataCommonDataType.DATE:
               {
                   Node dateTimeNode = DOMTreeUtility.getNode(node, "dateTime" );
                   lomMetadata.setValue1( DOMTreeUtility.getNodeValue(dateTimeNode) );
    
                   Node descriptionNode = DOMTreeUtility.getNode(node, "description" );
                   Node stringNode = DOMTreeUtility.getNode(descriptionNode, "string" );
                   lomMetadata.setValue2( DOMTreeUtility.getNodeValue(stringNode) );
                   lomMetadata.setLangString( DOMTreeUtility.getAttributeValue(stringNode, "language") );
    
                   if ( _Debug )
                   {
                       System.out.println("      --> dateTime Value  : " + DOMTreeUtility.getNodeValue(dateTimeNode) );
                       System.out.println("      --> dateTime description LangString Value : " + DOMTreeUtility.getNodeValue(stringNode) );
                       System.out.println("      --> dateTime description LangString Attribute language  : " + DOMTreeUtility.getAttributeValue(stringNode, "language") );
                   }
                   
                   mLOMMetadataList.add( lomMetadata );
                   break;
               }
               case MetadataCommonDataType.DURATION: 
               {
                   Node durationNode = DOMTreeUtility.getNode(node, "duration" );

                   if ( _Debug )
                   {
                       System.out.println( "durationNode : " + durationNode );
                   }
                   lomMetadata.setValue1( DOMTreeUtility.getNodeValue(durationNode) );
    
                   Node descriptionNode = DOMTreeUtility.getNode(node, "description" );
                   Node stringNode = DOMTreeUtility.getNode(descriptionNode, "string" );
                   lomMetadata.setValue2( DOMTreeUtility.getNodeValue(stringNode) );
                   lomMetadata.setLangString( DOMTreeUtility.getAttributeValue(stringNode, "language") );
    
                   if ( _Debug )
                   {
                       System.out.println("      --> duration Value  : " + DOMTreeUtility.getNodeValue(durationNode) );
                       System.out.println("      --> duration description LangString Value : " + DOMTreeUtility.getNodeValue(stringNode) );
                       System.out.println("      --> duration description LangString Attribute language  : " + DOMTreeUtility.getAttributeValue(stringNode, "language") );
                   }
                
                   mLOMMetadataList.add( lomMetadata );
                   break;
               }
               case MetadataCommonDataType.UNKNOWN:
               {
                   lomMetadata.setValue1( DOMTreeUtility.getNodeValue(node) );
                   mLOMMetadataList.add( lomMetadata );
    
                   NodeList nodeChildren = node.getChildNodes();
                   
                   if ( nodeChildren != null )
                   {
                       for( int i=0; i<nodeChildren.getLength(); i++ )
                       {
                           Node childNode = nodeChildren.item(i);
        
                           if ( childNode.getNodeType() == Node.ELEMENT_NODE )
                           {
                               mSeq++;
                               setLOMMetadata( childNode, metaType, mSeq, seq );
                           }
                       }
                   }
                   
                   break;
               }
               default :
               {
                   //lomMetadata.setValue1( );
                   //lomMetadata.setValue2( );
                   //lomMetadata.setLangString( );
                   break;
               }
           }
       }
   }
   
}
