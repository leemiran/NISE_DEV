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
import java.io.File;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ziaan.scorm2004.parsers.dom.ADLDOMParser;
import com.ziaan.scorm2004.parsers.dom.DOMTreeUtility;
import com.ziaan.scorm2004.util.Message;
import com.ziaan.scorm2004.util.MessageCollection;
import com.ziaan.scorm2004.util.MessageType;

/**
 * <strong>Filename: </strong>ADLSCORMValidator.java<br><br>
 *
 * <strong>Description: </strong>The <code>ADLSCORMValidator</code> object
 *               serves as the main interface for obtaining a Metadata or
 *               Content Package Validator.  This object houses the common
 *               functionality of both types of Validators (Metadata/CP) -
 *               serving as the parent for inheritance.<br><br>
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
 * @author ADL Technical Team
 */
public class ADLSCORMValidator
{
   /**
    * The <code>Document</code> object is an electronic representation of the
    * XML produced if the parse was successful. A parse for wellformedness
    * creates a document object while the parse for validation against the
    * controlling documents creates a document object as well.  This attribute
    * houses the document object that is created last.  In no document object is
    * created, the value remains null. This value is determined by the
    * ADLDOMParser class.<br>
    */
   protected Document mDocument;

   /**
    * This attribute describes if the XML instance is found to be wellformed by
    * the parser.  The value "false" indicates that the XML instance is not
    * wellformed XML, "true" indicates it is wellformed XML.This value is
    * determined by the ADLDOMParser class.<br>
    */
   protected boolean mIsWellformed;

   /**
    * This attribute describes if the XML instance is found to be valid against
    * the controlling documents by the parser.  The value "false" indicates that
    * the XML instance is not valid against the controlling documents, "true"
    * indicates that the XML instance is valid against the controlling
    * documents. This value is determined by the ADLDOMParser class.<br>
    */
   protected boolean mIsValidToSchema;

   /**
    * This attribute describes if the XML instance is valid to the SCORM
    * Application Profiles. A true value implies that the instance is valid to
    * the rules defined by the Application Profiles, false implies otherwise.<br>
    */
   protected boolean mIsValidToApplicationProfile;

  /**
    * This attribute is specific to the content package validator only.  It
    * describes if the required schemas exist at the root of a content package test
    * subject that are necessary for the validation parse.  A true
    * value implies that the required schemas were detected at the root package,
    * false implies otherwise.<br>
   **/
   protected boolean mIsRequiredFiles;

   /**
     * This attribute is specific to the content package validator only.  It
     * describes if the required IMS Manifest exists at the root of a content
     * package test subject. A true value implies that the required IMS Manifest
     * was detected at the root package, false implies otherwise.<br>
    **/
   protected boolean mIsIMSManifestPresent;

   /**
    * This attribute describes if the XML instance uses extension elements. A
    * true value implies that extension elements were detected, false implies
    * they were not used.<br>
    */
   protected boolean mIsExtensionsUsed;

   /**
    * Describes what SCORM Validator is in use.  Valid values include:<br>
    *    - "metadata"<br>
    *    - "contentpackage"<br>
    */
   protected String mValidatorType;

   /**
    * This attribute contains the string describing the location of the
    * controlling documents that the parser shall parse against.  The format of
    * this string value shall be identical to the representation of the
    * schemaLocation attribute in the XML declation
    * (ie, [namespace of schema] [location of schema] ).<br>
    */
   private String mSchemaLocation;

   /**
    * This attribute contains describes if we are dealing with a root element
    * that belongs to a valid namespace (IMS, IEEE).  True implies that the root
    * element belongs to a valid namespace, false implies that it belongs to
    * an extended namespace.<br>
    *
    */
   private boolean mIsRootElement;

   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;

   /**
    * Describes whether or not full validation occurs.  True implies that full
    * validation is performed, false implies wellformedness check only.
    * <br>
    */
   private boolean mPerformFullValidation;

   /**
    * Constructor.  Sets the attributes to their initial values.<br>
    *
    * @param iValidator The type of validator in use.  Valid values include:<br>
    *                  - "metadata"<br>
    *                  - "contentpackage"<br>
    */
   public ADLSCORMValidator( String iValidator )
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mLogger.entering( "ADLSCORMValidator", "ADLSCORMValidator()" );
      mLogger.finest("      iValidator coming in is " + iValidator );

      mDocument = null;
      mIsIMSManifestPresent = true;
      mIsWellformed = false;
      mIsValidToSchema = false;
      mSchemaLocation = null;
      mIsValidToApplicationProfile = false;
      mIsExtensionsUsed = false;
      mValidatorType = iValidator;
      mIsRootElement = false;

      mLogger.exiting( "ADLSCORMValidator", "ADLSCORMValidator()" );
   }

   /**
    * This method provides the outcome of the validate at the time of call.  The
    * returned object serves as the storage for the checks performed during
    * validation and their outcomes. This object serves as an efficent means for
    * passing the outcome of the validation activites throughout the
    * utilizing system.<br>
    *
    * @return ADLValidator Object containing the outcome of validation. <br>
    */
   public ADLValidatorOutcome getADLValidatorOutcome()
   {
      mLogger.entering( "ADLSCORMValidator", "getADLValidatorOutcome()" );

      // create an instance of the ADLValidator object with the current state of
      // of the ADLSCORMValidator attributes values

      ADLValidatorOutcome outcome = new ADLValidatorOutcome(
                                               getDocument(),
                                               getIsIMSManifestPresent(),
                                               getIsWellformed(),
                                               getIsValidToSchema(),
                                               getIsValidToApplicationProfile(),
                                               getIsExtensionsUsed(),
                                               getIsRequiredFiles(),
                                               mPerformFullValidation,
                                               getIsRootElement() );

      mLogger.exiting( "ADLSCORMValidator", "getADLValidatorOutcome()" );
      return outcome;
   }

   /**
    * The parser allows a using system to hardcode the location of the
    * controlling documents that are to be used during the parse for validation.
    * This method permits the setting of these controlling document locations.
    * <br>
    *
    * @param iSchemaLocation - The schemaLocation string in the exact format as
    * it would appear in the xsi:schemaLocation attribute of an XML instance.
    * <br>
    */
   public void setSchemaLocation( String iSchemaLocation )
   {
      mLogger.entering( "ADLSCORMValidator", "setSchemaLocation()" );

      mSchemaLocation = iSchemaLocation;

      mLogger.finest( "mSchemaLocation set to " + mSchemaLocation );
      mLogger.exiting( "ADLSCORMValidator", "setSchemaLocation()" );
    }

   /**
    * This method returns the schemaLocation string that contains the schema
    * locations values retrieved after walking the wellformeness DOM, or, if no
    * schemalocation values were found in the dom, then this value contains the
    * default schema location values.
    *
    * @return mSchemaLocation -- the schemaLocation string.<br>
    */
   public String getSchemaLocation()
   {
      return mSchemaLocation;
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
    * This method returns whether or not the XML instance was found to be
    * wellformed.  The value "false" indicates that the XML instance is not
    * wellformed XML, "true" indicates it is wellformed XML.
    *
    * @return boolean - describes if the instance was found to be wellformed.
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
    * @return boolean - describes if the instance was found to be valid
    *                   against the schema(s).<br>
    */
   public boolean getIsValidToSchema()
   {
      return mIsValidToSchema;
   }

   /**
    * This method is specific to the Content Package Validator only.  This
    * method returns whether or not the content package test subject
    * contains the required schemas at the root of the package
    * needed for the validation parse.<br>
    *
    * @return boolean Describes if the required files were found at the root
    * of the package, false implies otherwise.<br>
    */
   public boolean getIsRequiredFiles()
   {
      return mIsRequiredFiles;
   }


   /**
    * This method returns a boolean describing if the required IMS manifest
    * is at the root of the package.<br>
    *
    * @return boolean Describes if the required imsmanifest.xml was found at
    * the root of the package, false implies otherwise.<br>
    */
   public boolean getIsIMSManifestPresent()
   {
      return mIsIMSManifestPresent;
   }


   /**
    * This method is specific to the Content Package Validator only.  This
    * method sets whether or not the content package test subject
    * contains the required files at the root of the package.  The required
    * files include the imsmanifest.xml file as well as the controlling
    * documents needed for the validation parse.<br>
    *
    * @param requiredFilesResult Boolean indicating the result of the
    * required files check<br>
    */
   protected void setIsRequiredFiles( boolean requiredFilesResult )
   {
      mIsRequiredFiles = requiredFilesResult;
   }


   /**
    * This method is used by both the MD and CP Validators.  It is used
    * to describe if the root element of the test subject belongs to a valid
    * namespace (IMS or IEEE LOM).  <br>
    *
    * @return boolean Describes if the root element belongs to a valid
    * namespace<br>
    */
   public boolean getIsRootElement()
   {
      return mIsRootElement;
   }

   /**
    * This method is used by both the MD and CP Validators.  It is used
    * to set the value that describes if the root element of the test subject
    * belongs to a valid namespace (IMS or IEEE LOM)
    *
    */
   public void setIsRootElement( boolean iIsRootElement )
   {
      mIsRootElement = iIsRootElement;
   }

   /**
    * This method is specific to the Content Package Validator only.  This
    * method sets whether or not the content package test subject
    * contains the required files at the root of the package.  The required
    * files include the imsmanifest.xml file as well as the controlling
    * documents needed for the validation parse.<br>
    *
    * @param requiredFilesResult Boolean indicating the result of the
    * required files check<br>
    */
   protected void setIsIMSManifestPresent( boolean imsManifestResult )
   {
      mIsIMSManifestPresent = imsManifestResult;
   }


   /**
    * This method returns whether or not the XML instance was valid to the
    * application profile checks.  The value "false" indicates that the XML
    * instance is not valid to the application profiles, "true" indicates that
    * the XML instance is valid to the application profiles.<br><br>
    *
    * @return boolean Describes if the instance was found to be valid
    * against the SCORM Application Profiles.<br>
    */
   public boolean getIsValidToApplicationProfile()
   {
      return mIsValidToApplicationProfile;
   }

   /**
    * This method sets whether or not the XML instance was valid to the
    * application profile checks.  The value "false" indicates that the XML
    * instance is not valid to the application profiles, "true" indicates that
    * the XML instance is valid to the application profiles.<br><br>
    *
    * @param isValidToAppProf Boolean indicating the application profile
    * check result.<br>
    */
   protected void setIsValidToApplicationProfile( boolean isValidToAppProf )
   {
      mIsValidToApplicationProfile = isValidToAppProf;
   }

   /**
    * This method sets whether or not the XML instance was found to be
    * wellformed.  The value "false" indicates that the XML instance is not
    * wellformed XML, "true" indicates it is wellformed XML.<br><br>
    *
    * @param boolean Describes if the instance was found to be wellformed.
    * <br>
    */
   protected void setIsWellformed( boolean iIsWellformed )
   {
      mIsWellformed = iIsWellformed;
   }

   /**
    * This method sets whether or not the XML instance was valid to the
    * schema.  The value "false" indicates that the XML instance is not valid
    * against the controlling documents, "true" indicates that the XML instance
    * is valid against the controlling documents.<br>
    *
    * @param boolean Describes if the instance was found to be valid
    * against the schema(s)<br>
    */
   public void setIsValidToSchema( boolean iIsValidToSchema )
   {
      mIsValidToSchema = iIsValidToSchema;
   }

   /**
    * This method sets whether or not full validation is to be performed
    * by the Validator.  <br>
    *
    * @param boolean Describes if full validation occurs.  <br>
    */
   public void setPerformFullValidation( boolean iPerformFullValidation )
   {
      mPerformFullValidation = iPerformFullValidation;
   }


   /**
    * This method returns whether or not the XML instance contained extension
    * elements and/or attributes.  The value "false" indicates that the XML
    * instance does not contain extended elements and/or attributes, "true"
    * indicates that the XML instance did.<br>
    *
    * @return boolean Describes if extension elements were found.<br>
    */
   public boolean getIsExtensionsUsed()
   {
      return mIsExtensionsUsed;
   }

   /**
    * This method parses the provided XML file for wellformedness
    * and validation against the controlling documents through interaction
    * with the DOMParser.  The usage of extended elements and/or attributes is
    * also determined here.<br>
    *
    * @param iXMLFileName The xml file test subject<br>
    *
    */
   protected void performValidatorParse( String iXMLFileName )
   {

     mLogger.entering( "ADLSCORMValidator", "performValidatorParse()" );
     mLogger.finest( "   iXMLFileName coming in is " + iXMLFileName );

     // create an adldomparser object
     ADLDOMParser adldomparser = new ADLDOMParser();

     if( mSchemaLocation != null )
     {
        //set schemaLocation property and perform parsing on the test subject
        adldomparser.setSchemaLocation( getSchemaLocation() );

        // call the appropriate parse method based on what type of parse is
        // indicated by the mPerformaFullValidation parameter

        if ( !mPerformFullValidation )
        {
           adldomparser.parseForWellformedness( iXMLFileName, true );
           setSchemaLocation( adldomparser.getSchemaLocation() );
           mDocument = adldomparser.getDocument();

          // extensions are detected and the flag is set during prunetree
          // of wellformedness parse only
          mIsExtensionsUsed = adldomparser.isExtensionsFound();

        }
        else
        {
           adldomparser.setSchemaLocation( mSchemaLocation );
           adldomparser.parseForValidation( iXMLFileName );
        }
     }
     else
     {
        String msgText =  "Can not parse, schema location has not been set";
        mLogger.severe( "TERMINATE: " + msgText );
        MessageCollection.getInstance().add( new Message (
                                                          MessageType.TERMINATE,
                                                          msgText ) );
     }

     // retrieve adldomparser attribute values and assign to the SCORMValidator
     //mDocument = adldomparser.getDocument();
     mIsWellformed = adldomparser.getIsWellformed();
     mIsValidToSchema = adldomparser.getIsValidToSchema();

     // perform garabage cleanup
     (Runtime.getRuntime()).gc();

     mLogger.exiting( "ADLSCORMValidator", "performValidatorParse()" );
   }


      /**
       * This method cleans up the temporary folder used by the CPValidator
       * for extraction of the test subject package.  This method loops through
       * the temporary PackageImport folder to remove all files that have been
       * extracted during the content package extract.<br>
       *
       * @param iPath Temporary directory location where package was
       * extracted and in need of cleanup.<br>
       */
      public void cleanImportDirectory( String iPath )
      {
         try
         {
            File theFile = new File( iPath );
            File allFiles[] = theFile.listFiles();

            for ( int i = 0; i < allFiles.length; i++ )
            {
               if ( allFiles[i].isDirectory() )
               {
                  cleanImportDirectory( allFiles[i].toString() );
                  allFiles[i].delete();
               }
               else
               {
                  allFiles[i].delete();
               }
            }
         }
         catch ( NullPointerException npe )
         {
            mLogger.severe( iPath + " did not exist and was not cleaned!!" );
         }
      }

      /**
       * This method determines if the IMS Manifest being tested is truely an
       * IMS Manifest.  It does this by comparing the root elements local name
       * and namespace with the defined IMS root node name and namespace.  If
       * the root node is not what is expected, then the method logs an error
       * and returns false.
       *
       * @return Returns a boolean value that indicates whether or not the
       * validator is processing an IMS manifest.
       */
      public boolean isRootElementValid()
      {
         boolean result = false;

         Node rootNode = mDocument.getDocumentElement();
         String rootNodeName = rootNode.getLocalName();
         String rootNodeNamespace = rootNode.getNamespaceURI();

         if( rootNodeName.equals("manifest") )
         {
            if ( rootNodeNamespace.equals( DOMTreeUtility.IMSCP_NAMESPACE ) )
            {
               result = true;
            }
            else
            {
               String msgText = "The imsmanifest.xml file did not contain the " +
                  "root element that was expected. The root element of the " +
                  "imsmanifest.xml file was the <" + rootNodeName + "> " +
                  "element from the " + rootNodeNamespace + " namespace. " +
                  "The <manifest> element from the " + 
                  DOMTreeUtility.IMSCP_NAMESPACE + " namespace was expected.";

               mLogger.info( "FAILED: " + msgText );
               MessageCollection.getInstance().add( new Message(MessageType.FAILED,
                                                                msgText ) );
            }
         }
         else if( rootNodeName.equals("lom") )
         {
            if ( rootNodeNamespace.equals( DOMTreeUtility.IEEE_LOM_NAMESPACE ) )
            {
               result = true;
            }
            else
            {
               String msgText = "The metadata XML instance did not contain " +
                  "the root element that was expected. The metadata XML " +
                  "instance root element was the <" + rootNodeName + "> " +
                  "element from the " + rootNodeNamespace + " namespace.  " +
                  "The <lom> element from the " + 
                  DOMTreeUtility.IEEE_LOM_NAMESPACE + " namespace was expected.";

               mLogger.info( "FAILED: " + msgText );
               MessageCollection.getInstance().add( new Message(MessageType.FAILED,
                                                                msgText ) );
            }
         }
         return result;
      }
}
