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

import java.lang.String;
import java.util.Vector;
import java.io.File;
import java.io.Serializable;
import java.util.logging.Logger;

// xerces imports

import org.w3c.dom.Node;

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
public class MetadataData implements Serializable
{
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
    * The default constructor. Sets the attributes to their initial values.<br>
    */
   public MetadataData()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mApplicationProfileType = new String();
      mRootLOMNode = null;
      mLocation = new String();
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


}
