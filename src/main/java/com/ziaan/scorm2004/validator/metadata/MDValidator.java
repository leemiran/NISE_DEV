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
package com.ziaan.scorm2004.validator.metadata;

// native java imports
import java.util.Vector;
import java.util.logging.Logger;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ziaan.scorm2004.util.Message;
import com.ziaan.scorm2004.util.MessageCollection;
import com.ziaan.scorm2004.util.MessageType;
import com.ziaan.scorm2004.validator.ADLSCORMValidator;
import com.ziaan.scorm2004.validator.RulesValidator;

/**
 * <strong>Filename: </strong>MDValidator.java<br><br>
 *
 * <strong>Description: </strong>The <code>MDValidator</code> object determines
 *                    whether the test subject metadata XML instance is
 *                    conformant with the Metadata Application Profiles
 *                    (asset, sco, sca, activity, content aggregation, package)
 *                    as defined in the Content Aggregation Model of the SCORM.
 *                    The MDValidator inherites from the ADLSCORMValidator to
 *                    determine if the metadata test subject is wellformed and
 *                    valid to the xsd(s).  Next, the MDValidator object
 *                    validates the metadata test subject against the rules and
 *                    requirements necessary for meeting each Metadata
 *                    Application Profile.<br><br>
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
 * @author ADL Technical Team<br><br>
 */
public class MDValidator extends ADLSCORMValidator
{
  /**
   * Logger object used for debug logging<br>
   */
  private Logger mLogger;

  /**
   * MetadataRulesValidator object<br>
   */
  private RulesValidator mMetadataRulesValidator;

  /**
   * The vocabulary token of the technical.requirement.orComposite.type element
   * for enforcement of the type/name best practice vocabulary<br>
   */
  private String mTypeValue;

  /**
   * The vocabulary token of the technical.requirement.orComposite.name element
   * for enforcement of the type/name best practice vocabulary<br>
   *
   */
  private String mNameValue;

  /**
   * Boolean describing whether or not the restricted string values of the
   * <metadataSchema> element have been validated
   */
  private boolean mMetadataSchemaTracked;


  /**
   * The metadataSchema elements that shall contain restricted string values<br>
   */
  private Vector mMetadataSchemaNodeList;

  /**
   * The application profile type being tested against<br>
   *     -"asset"<br>
   *     -"sco"<br>
   *     -"activity"<br>
   *     -"contentorganization<br>
   *     -"contentaggregation"<br>
   *     -"other"<br>
   */
  private String mApplicationProfileType;

  /**
   * String containing the environment variable for determination of the Test
   * Suite installation directory.<br>
   */
  private String mEnvironmentVariable;

  /**
   * Default Constructor.  Sets the attributes to their initial values.<br>
   */
  public MDValidator( String iApplicationProfileType,
                      String iEnvironmentVariable )
  {
     super( "metadata" );
     mLogger = Logger.getLogger( "org.adl.util.debug.validator" );
     mMetadataRulesValidator = new RulesValidator( "metadata" );
     mTypeValue = new String();
     mNameValue = new String();
     mApplicationProfileType = iApplicationProfileType;
     mEnvironmentVariable = iEnvironmentVariable;
     mMetadataSchemaNodeList = new Vector();
     mMetadataSchemaTracked = false;
  }

  /**
   * This method is called by the user to initiate the validation process.
   * This method will trigger the parsing activity done by the
   * ADLSCORMValidator. Next, the dom will be used to validate the remaining
   * checks required for full SCORM Validation.<br>
   *
   * @param iXMLFileName
   *               - name of the metadata XML test subject<br>
   *
   * @return boolean: describes if the checks passed or failed. A true value
   * implies that the checks passed, false otherwise.<br>
   */
  public boolean validate( String iXMLFileName )
  {
     boolean validateResult = true;
     String msgText;

     mLogger.entering( "MDValidator", "validate(iXMLFileName)" );
     mLogger.finer( "iXMLFileName coming in is " + iXMLFileName );

     // Perform Wellformedness Parse
     msgText = "Testing the metadata for Well-formedness";
     mLogger.info( "INFO: " + msgText );

     // Well-formedness and Validity to Schema Check
     validateResult = checkWellformedness( iXMLFileName ) && validateResult;

     // only continue to validate against Application Profile rules if
     // the document was wellformed - does not need to be valid to xsds
     boolean wellformed = super.getIsWellformed();

     // determine if we are dealing with a root lom element that belongs to the
     // IEEE LOM Namespace.
     boolean isValidRoot = false;

     if( wellformed )
     {
        isValidRoot = super.isRootElementValid();
        super.setIsRootElement( isValidRoot );
     }

     if( isValidRoot )
     {
        // Perform Validation to the Schema Parse
        msgText = "Testing the metadata for Validity to the Controlling Documents";
        mLogger.info( "INFO: " + msgText );

        // Well-formedness and Validity to Schema Check
        validateResult = checkSchema( iXMLFileName ) && validateResult;

        if ( wellformed )
        {
           if ( !mApplicationProfileType.equals("other") )
           {
              // Check the SCORM Application Profile Rules
              msgText = "****************************************";
              mLogger.info( "OTHER: " + msgText );
              MessageCollection.getInstance().add( new Message( MessageType.OTHER,
                                                             msgText ) );

              msgText = "Validating the metadata against the Application " +
                        "Profile Rules";
              mLogger.info( "INFO: " + msgText );
              MessageCollection.getInstance().add( new Message( MessageType.INFO,
                                                                msgText ) );

              mMetadataRulesValidator.readInRules( mApplicationProfileType,
                                                   mEnvironmentVariable );

              boolean isAppProfileResult = compareToRules( super.getDocument(), "" );

              // set the ADLSCORMValidator protected attribute with this value
              // this value is needed for summary logging and ADLValidatorOutcome
              super.setIsValidToApplicationProfile( isAppProfileResult );

              validateResult = isAppProfileResult && validateResult;
           }
           else
           {
              // set the ADLSCORMValidator protected attribute with this value
              // this value is needed for summary logging and ADLValidatorOutcome
              super.setIsValidToApplicationProfile( true );
           }
        }
     }
     else
     {
        validateResult = false;
     }
     mLogger.exiting("MDValidator", "validate()" );
     return validateResult;
  }

  /**
   * This overloaded method is called during content package integration to
   * initiate the validation process. This method will trigger the parsing
   * activity done by the ADLSCORMValidator. Next, the dom will be used to
   * validate the remaining checks required for full SCORM Validation.<br>
   *
   * @param iXMLFileName
   *               - name of the metadata XML test subject<br>
   *
   * @return boolean: describes if the checks passed or failed. A true value
   * implies that the checks passed, false implies otherwise.<br>
   */
  public boolean validate( Node iRootLOMNode,
                           boolean iDidValidationToSchemaPass )
  {
     boolean validateResult = true;
     String msgText;

     mLogger.entering( "MDValidator", "validate(iRootLOMNode)" );

     msgText = "Testing the metadata for Well-formedness";
     mLogger.info( "INFO: " + msgText );

     msgText = "The instance is well-formed ";
     mLogger.info( msgText );
     MessageCollection.getInstance().add( new Message ( MessageType.PASSED,
                                                        msgText ) );
     super.setIsWellformed( true );

          // determine if we are dealing with a root lom element that belongs to the
     // IEEE LOM Namespace.

     // setting isValidRoot to true for inline metadata.  All extensions have
     // been stripped at this point so we are sure we are dealing with
     // a LOM namespace.
     boolean isValidRoot = true;
     super.setIsRootElement( isValidRoot );

     // only continue if we have a root element that belongs to an expected
     // namespace
     if( isValidRoot )
     {
        if ( iDidValidationToSchemaPass )
        {
           msgText = "The instance is valid against the controlling documents";
           mLogger.info( msgText );
           MessageCollection.getInstance().add( new Message ( MessageType.PASSED,
                                                              msgText ) );
           super.setIsValidToSchema( true );
        }
        else
        {
           msgText = "The instance is NOT valid against the controlling documents";
           mLogger.info( msgText );
           MessageCollection.getInstance().add( new Message ( MessageType.FAILED,
                                                              msgText ) );
           msgText = "Schema validation for in-line metadata is performed " +
                     " during the manifest schema validation.  Since the" +
                     " manifest failed, it is assumed that inline metadata" +
                     " failed. Please see the manifest detailed log for details.";
           mLogger.info( msgText );
           MessageCollection.getInstance().add( new Message ( MessageType.INFO,
                                                              msgText ) );

        }
        // Continue to application profile checking only if we are not dealing with
        // package level metadata
        if ( !mApplicationProfileType.equals("other") )
        {
           // Check the SCORM Application Profile Rules
           msgText = "****************************************";
           mLogger.info( "OTHER: " + msgText );
           MessageCollection.getInstance().add( new Message( MessageType.OTHER,
                                                             msgText ) );

           msgText = "Validating the metadata against the Application " +
                     "Profile Rules";
           mLogger.info( "INFO: " + msgText );
           MessageCollection.getInstance().add( new Message( MessageType.INFO,
                                                             msgText ) );

           mMetadataRulesValidator.readInRules( mApplicationProfileType,
                                                mEnvironmentVariable );

           boolean isAppProfileResult = compareToRules( iRootLOMNode, "" );

           // set the ADLSCORMValidator protected attribute with this value
           // this value is needed for summary logging and ADLValidatorOutcome
           super.setIsValidToApplicationProfile( isAppProfileResult );

           validateResult = isAppProfileResult && validateResult;
        }
        else
        {
           // have to set isValidToApplicationProfile to true (default is fale)
           // here for overall content package conformance.
           super.setIsValidToApplicationProfile( true );
        }
     }
     mLogger.exiting("MDValidator", "validate()" );
     return validateResult;
  }

  /**
   * This method activates the parse for wellformedness.
   *
   * @param iXMLFileName - name of the metadata XML test subject<br>
   *
   * @return boolean - result of the parse for wellformedness. False implies
   * that errors occured during wellformedness parse, true implies the document
   * is wellformed
   *
   */
  private boolean checkWellformedness( String iXMLFileName )
  {
     boolean wellnessResult = true;

     super.setPerformFullValidation( false );
     super.performValidatorParse( iXMLFileName );

     mLogger.info( "************************************" );
     mLogger.info( " WELLFORMED Result is " + super.getIsWellformed() );
     mLogger.info( "************************************" );

     if ( !super.getIsWellformed() )
     {
        wellnessResult = false;
     }

     return wellnessResult;
  }

  /**
   * This method activates the parse for validation against the schema(s).
   *
   * @param iXMLFileName - name of the metadata XML test subject<br>
   *
   * @return boolean - result of the parse for validation to the schema. False
   * implies that errors occured during validation to the schema parse,
   * true implies the document is valid to the schema(s).<br>
   *
   */
  private boolean checkSchema( String iXMLFileName )
  {
     boolean schemaResult = true;

     super.setPerformFullValidation( true );
     super.performValidatorParse( iXMLFileName );

     mLogger.info( "************************************" );
     mLogger.info( " VALIDSCHEMA Result is " + super.getIsValidToSchema() );
     mLogger.info( " mIsExtensionsUsed is " + super.getIsExtensionsUsed() );
     mLogger.info( "************************************" );

     if ( !super.getIsValidToSchema() )
     {
        schemaResult = false;
     }

     return schemaResult;
  }


  /**
   * This method tests for each particular data type and runs the
   * corresponding tests.
   * <br>
   *
   * @param iCurrentChildName
   *               The name of child node<br>
   *
   * @param iDataType
   *               The name of datatype to be tested<br>
   *
   * @param iCurrentChild
   *               The child node to be tested<br>
   *
   * @param iResult
   *               Current parser results<br>
   *
   * @param iPath
   *               Path of the rule to compare to<br>
   *
   * @return boolean: result of the parse.  A true value implies that the
   * well-formedness parse and the scheam validation parse passed, false
   * implies otherwise.<br>
   */
  private boolean checkDataTypes( String iCurrentChildName,
                                  String iDataType,
                                  Node iCurrentChild,
                                  String iPath , String appProfileType )
  {
     String msgText = new String();
     boolean result = true;

     if ( iDataType.equalsIgnoreCase("parent") )
     {
        //This is a parent element
        result = compareToRules( iCurrentChild, iPath );
     }

     else if ( iDataType.equalsIgnoreCase("langstring") )
     {

        //This is a Langstring data type element
        msgText = "Testing element <" + iCurrentChildName +
                  "> for the LangString Type";
        mLogger.info( "INFO: " + msgText );
        MessageCollection.getInstance().add( new Message(
                                             MessageType.INFO,
                                             msgText ) );

        result = checkLangString( iCurrentChild, iPath );
     }
     else if ( ( iDataType.equalsIgnoreCase("datetime") ||
                 iDataType.equalsIgnoreCase("duration")  ) )
     {

        //This is a datetime or a duration data type element
        msgText = "Testing element <" + iCurrentChildName +
                  "> for the " + iDataType + " Type";
        mLogger.info( "INFO: " + msgText );
        MessageCollection.getInstance().add( new Message(
                                                 MessageType.INFO,
                                                 msgText ) );
        result = checkDatetimeOrDurationPair( iCurrentChild,
                                              iCurrentChildName,
                                              iDataType );
     }
     else if ( iDataType.equalsIgnoreCase("nametypepair") )
     {
        // This is a nametypepair element
        //Step 1:  test for basic vocabulary data type
        msgText = "Testing element <" + iCurrentChildName +
                  "> for the vocabulary type";
        mLogger.info( "INFO: " + msgText );
        MessageCollection.getInstance().add( new Message(
                                             MessageType.INFO,
                                             msgText ) );
        result = checkSourceValuePair( iCurrentChild,
                                       iCurrentChildName,
                                       "bestpractice", iPath );
        // check for type/name vocabulary relationship
        checkNameTypePair( iCurrentChildName );

     }
     else if ( iDataType.equalsIgnoreCase("text") )
     {
        // This is a text data type
        result = checkText( iCurrentChild, iCurrentChildName,
                            iPath ) && result;
     }
     else if ( iDataType.equalsIgnoreCase("bestpracticevocabulary") )
     {
        // This is a bestpracticevocabulary data type
        msgText = "Testing element <" + iCurrentChildName +
                  "> for the vocabulary type";
        mLogger.info( "INFO: " + msgText );
        MessageCollection.getInstance().add( new Message(
                                             MessageType.INFO,
                                             msgText ) );
        result = checkSourceValuePair( iCurrentChild,
                                       iCurrentChildName,
                                       "bestpractice", iPath );
     }
     else if ( iDataType.equalsIgnoreCase("restrictedvocabulary") )
     {
        // This is a restrictedvocabulary data type
        msgText = "Testing element <" + iCurrentChildName +
                  "> for the Vocabulary Type";
        mLogger.info( "INFO: " + msgText );
        MessageCollection.getInstance().add( new Message(
                                             MessageType.INFO,
                                             msgText ) );
        result = checkSourceValuePair( iCurrentChild,
                                       iCurrentChildName,
                                       "restricted", iPath );
     }
     else
     {
        // This is an extension element
        // no additional checks needed
     }

     return result;
  }

   /**
    * A recursive method that is driven by the test subject dom.
    * This method performs the application profiles rules checks.<br>
    *
    * @param iTestSubjectNode
    *               Test Subject DOM<br>
    * @param iPath
    *               Path of the rule to compare to<br>
    *
    * @return boolean: result of the application profile checks performed.  A
    * true value implies that the test subject passes the application profile
    * rules, a false implies otherwise.<br>
    */
   private boolean compareToRules( Node iTestSubjectNode, String iPath )
   {
      // looks exactly like prunetree as we walk down the tree
      mLogger.entering( "MDValidator", "compareToRules" );

      boolean result = true;
      String msgText = new String();

      // is there anything to do?
      if ( iTestSubjectNode == null )
      {
         result = false;
         return result;
      }

      int type = iTestSubjectNode.getNodeType();
      switch ( type )
      {
         case Node.PROCESSING_INSTRUCTION_NODE:
         {
            break;
         }

         // document
         case Node.DOCUMENT_NODE:
         {
            Node rootNode = ((Document)iTestSubjectNode).getDocumentElement();
            String rootNodeName = rootNode.getLocalName();

            msgText = "Testing element <" + rootNodeName +
                      "> for minimum conformance";
            mLogger.info( "INFO: " + msgText );
            MessageCollection.getInstance().add( new Message( MessageType.INFO,
                                                              msgText ) );
            msgText = "Multiplicity for element <" +
                       rootNodeName + "> has been verified";
            mLogger.info( "PASSED: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                                MessageType.PASSED, msgText ) );

            result = compareToRules( rootNode, "" ) && result;
            break;
         }

         // element node
         case Node.ELEMENT_NODE:
         {
            String parentNodeName = iTestSubjectNode.getLocalName();

            String dataType = null;
            int multiplicityUsed = -1;
            int minRule = -1;
            int maxRule = -1;
            int spmRule = -1;


            // Test the child Nodes
            NodeList children = iTestSubjectNode.getChildNodes();

            if ( children != null )
            {
               int numChildren = children.getLength();

               // update the path for this child element
               String path;

               if ( iPath.equals("") ||
                    parentNodeName.equalsIgnoreCase("lom") )
               {
                  // the Node is a DOCUMENT OR
                  // the Node is a <lom>

                  path = parentNodeName;
               }
               else
               {
                  path = iPath + "." + parentNodeName;
               }

               // SPECIAL CASE: check for mandatory elements
               if ( parentNodeName.equalsIgnoreCase("lom") )
               {

                  if ( mApplicationProfileType.equals("sco") ||
                       mApplicationProfileType.equals("activity") ||
                       mApplicationProfileType.equals("asset") ||
                       mApplicationProfileType.equals("contentorganization")
                     )
                  {
                     result = checkForMandatory( iTestSubjectNode, "general" )
                                 && result;
                     result = checkForMandatory( iTestSubjectNode, "technical" )
                              && result;

                     result = checkForMandatory( iTestSubjectNode, "rights" )
                              && result;
                  }

                  if ( mApplicationProfileType.equals("sco") ||
                       mApplicationProfileType.equals("activity") ||
                       mApplicationProfileType.equals("contentorganization")
                     )
                  {
                     result = checkForMandatory( iTestSubjectNode, "lifeCycle" )
                              && result;
                  }

                  result = checkForMandatory( iTestSubjectNode, "metaMetadata" )
                  && result;

               }
               else if ( parentNodeName.equalsIgnoreCase("general") &&
                         ( mApplicationProfileType.equals("sco") ||
                           mApplicationProfileType.equals("activity") ||
                           mApplicationProfileType.equals("contentorganization")
                           || mApplicationProfileType.equals("asset")
                       ) )
               {
                  result = checkForMandatory( iTestSubjectNode, "identifier" )
                              && result;
                  result = checkForMandatory( iTestSubjectNode, "title" )
                              && result;
                  result = checkForMandatory( iTestSubjectNode, "description" )
                              && result;


                  if ( mApplicationProfileType.equals("sco") ||
                       mApplicationProfileType.equals("activity") ||
                       mApplicationProfileType.equals("contentorganization")
                     )
                  {
                     result = checkForMandatory( iTestSubjectNode, "keyword" )
                              && result;
                  }
               }
               else if (parentNodeName.equalsIgnoreCase("lifeCycle") )
               {
                  if ( mApplicationProfileType.equals("sco") ||
                       mApplicationProfileType.equals("activity") ||
                       mApplicationProfileType.equals("contentorganization")
                     )
                  {
                     result = checkForMandatory( iTestSubjectNode, "version" )
                              && result;
                     result = checkForMandatory( iTestSubjectNode, "status" )
                              && result;
                  }
               }
               else if (parentNodeName.equalsIgnoreCase("metaMetadata") )
               {

                  if ( mApplicationProfileType.equals("sco") ||
                       mApplicationProfileType.equals("activity") ||
                       mApplicationProfileType.equals("contentorganization")
                     )
                  {
                     result = checkForMandatory( iTestSubjectNode, "identifier" )
                              && result;
                  }

                  // special case, multiplicity is 2 or more
                  result = checkForMandatorySchemas( iTestSubjectNode,
                                                     "metadataSchema" )
                                                     && result;

                  // need to add all metadataschema values to a vector for a
                  // later check for restricted strings.
                  NodeList metaMetadataChildren =
                                              iTestSubjectNode.getChildNodes();

                  if ( metaMetadataChildren != null )
                  {
                     int metaMetadataChildrenLength = metaMetadataChildren.getLength();
                     for ( int i = 0; i < metaMetadataChildrenLength; i++ )
                     {
                        Node currentChild = metaMetadataChildren.item(i);
                        if ( currentChild.getLocalName().equals("metadataSchema") )
                        {
                           mMetadataSchemaNodeList.add( currentChild );
                        }
                     }
                  }
               }
               else if ( parentNodeName.equalsIgnoreCase("technical") &&
                         ( mApplicationProfileType.equals("sco") ||
                           mApplicationProfileType.equals("activity") ||
                           mApplicationProfileType.equals("contentorganization")
                          )
                       )
               {
                     result = checkForMandatory( iTestSubjectNode, "format" )
                              && result;
               }
               else if ( parentNodeName.equalsIgnoreCase("rights") &&
                         ( mApplicationProfileType.equals("sco") ||
                           mApplicationProfileType.equals("activity") ||
                           mApplicationProfileType.equals("contentorganization")
                         )
                       )
               {
                  result = checkForMandatory( iTestSubjectNode, "cost" )
                           && result;
                  result = checkForMandatory( iTestSubjectNode,
                           "copyrightAndOtherRestrictions" )
                           && result;
               }
               else if ( parentNodeName.equalsIgnoreCase("identifier") &&
                         ( mApplicationProfileType.equals("sco") ||
                           mApplicationProfileType.equals("activity") ||
                           mApplicationProfileType.equals("contentorganization")
                         )
                       )
               {
                  result = checkForMandatory( iTestSubjectNode, "entry" )
                           && result;
               }
String lastChildChecked = "";
               for ( int z = 0; z < numChildren; z++ )
               {
                  Node currentChild = children.item(z);
                  String currentChildName = currentChild.getLocalName();

                  dataType = mMetadataRulesValidator.getRuleValue(
                                                             currentChildName,
                                                             path, "datatype" );

                  // we do not want to test for extensions here
                  if ( dataType.equalsIgnoreCase("parent") ||
                       dataType.equalsIgnoreCase("langstring") ||
                       dataType.equalsIgnoreCase("text") ||
                       dataType.equalsIgnoreCase("restrictedvocabulary") ||
                       dataType.equalsIgnoreCase("bestpracticevocabulary") ||
                       dataType.equalsIgnoreCase("nametypepair") ||
                       dataType.equalsIgnoreCase("datetime") ||
                       dataType.equalsIgnoreCase("metadataschema") ||
                       dataType.equalsIgnoreCase("duration") )
                  {

if(!lastChildChecked.equals(currentChildName) || lastChildChecked.equals("") )
{
   lastChildChecked = currentChildName;
                     multiplicityUsed = getMultiplicityUsed(iTestSubjectNode,
                                                             currentChildName );
                     //get the min rule and convert to an int
                     minRule = Integer.parseInt(
                                       mMetadataRulesValidator.getRuleValue(
                                                            currentChildName,
                                                            path, "min") ) ;
                     //get the max rule and convert to an int
                     maxRule = Integer.parseInt(
                                        mMetadataRulesValidator.getRuleValue(
                                                            currentChildName,
                                                            path, "max") );

                     msgText = "Testing element <" + currentChildName +
                               "> for minimum conformance";
                     mLogger.info( "INFO: " + msgText );
                     MessageCollection.getInstance().add( new Message(
                                                          MessageType.INFO,
                                                          msgText ) );


                     result = checkMultiplicity( multiplicityUsed, minRule,
                                                 maxRule, currentChildName ) &&
                                                 result;
      }
                     // special handling of validation for <metadataSchema>
                     if ( dataType.equals("metadataschema") &&
                          !mMetadataSchemaTracked )
                     {
                        result = checkMetadataSchema() && result;
                     }
                     else
                     {
                        // test for each particular data type
                        result = checkDataTypes( currentChildName, dataType,
                                                 currentChild, path,
                                                 mApplicationProfileType)
                                                 && result;
                     }
                  }
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
      mLogger.exiting( "MDValidator", "compareToRules()" );
      return result;
   }


    /**
     * This method performs the smallest permitted maximum check on an elements
     * value.<br>
     *
     * @param iElementName
     *               Name of the element being checked for spm<br>
     * @param iElementValue
     *                element value being checked for smp<br>
     * @param iSPMRule
     *               allowable spm value<br>
     *
     * @return boolean: result of spm check.  A true value implies that the
     * smallest permitted maximum checks passed, false implies otherwise.<br>
     */
   private boolean checkSPMConformance( String iElementName,
                                        String iElementValue,
                                        int iSPMRule )
   {
      boolean result = true;
      String msgText = new String();

      int elementValueLength = iElementValue.length();

      if ( iSPMRule != -1 )
      {
         if ( elementValueLength > iSPMRule )
         {
            msgText = "The text contained in element <" +
                      iElementName + "> is greater than " + iSPMRule + ".";
            mLogger.info( "WARNING: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                                            MessageType.WARNING,
                                                            msgText ) );
         }
         else if ( elementValueLength < 1 )
         {
            msgText = "No text was found in element <" + iElementName +
                      "> and fails the minimum character length test";
            mLogger.info( "FAILED: " + msgText );
            MessageCollection.getInstance().add( new Message(MessageType.FAILED,
                                                             msgText ) );

            result = false;
         }
         else
         {
            msgText = "Character length for element <" + iElementName +
                      "> has been verified";
            mLogger.info( "PASSED: " + msgText );
            MessageCollection.getInstance().add( new Message(MessageType.PASSED,
                                                             msgText ) );
         }
      }
      else if ( elementValueLength < 1 )
      {
         msgText = "No text was found in element <" + iElementName +
                   "> and fails the minimum character length test";
         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                           msgText ) );

         result = false;
      }
      else
      {
         msgText = "Character length for element <" + iElementName +
                   "> has been verified";
         mLogger.info( "PASSED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.PASSED,
                                                           msgText ) );
      }

      return result;
   }


   /**
    * This method assists with the application profile check for valid
    * vocabularies.  The vocabulary value is compared to those defined by the
    * application profile rules. It is assumed that only 1 vocabulary token may
    * exist for an element/attribute.<br>
    *
    * @parm iName
    *                Name of the element/attribute being checked
    *                for valid vocabulary.<br>
    * @param iValue
    *               Vocabulary string value that exists for the
    *               element/attribute in the test subject<br>
    * @param vocabValues
    *               Vector containing a list of the valid vocabulary values
    *               for the element/attribute.<br>
    *
    * @return     true if the value is a valid vocab token, false otherwise<br>
    */
   private boolean checkVocabulary( String iName,
                                    String iValue,
                                    Vector iVocabValues )
   {
      mLogger.entering("MDValidator", "checkVocabulary()" );

      boolean result = false;
      String msgText;

      // loop through the valid vocabulary vector to see if the
      // attribute value matches a valid token

      int iVocabValuesSize = iVocabValues.size();
      for ( int i = 0; i < iVocabValuesSize; i ++ )
      {
         if ( iValue.equals( (String)iVocabValues.elementAt(i) ) )
         {
             result = true;
         }
      }

      if ( result )
      {
          msgText = "\"" + iValue + "\"  is a valid value for element <" +
                    iName + ">";
          mLogger.info( "PASSED: " + msgText );
          MessageCollection.getInstance().add( new Message( MessageType.PASSED,
                                                            msgText ) );
      }
      else
      {
          msgText = "\"" + iValue + "\"  is not a valid value for element <" +
                    iName + ">";
          mLogger.info( "FAILED: " + msgText );
          MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                            msgText ) );
      }
      mLogger.exiting("MDValidator", "checkVocabulary()" );

      return result;
   }




   /**
    * This method performs the smallest permitted maximum check on
    * element attributes.<br>
    *
    * @param iAttributeName
    *               Name of the attribute being checked for spm<br>
    * @param iAttributeValue
    *                attribute value being checked for smp<br>
    * @param iSPMRule
    *               allowable spm value<br>
    *
    * @return - boolean result of spm check.  A true value implies that the
    * attribute value was within the bounds of the smallest permitted maximum,
    * false implies otherwise. <br>
    */
   private boolean checkSPMAttributeConformance( String iAttributeName,
                                                 String iAttributeValue,
                                                 int iSPMRule )
   {
      boolean result = true;
      String msgText = new String();

      int attributeValueLength = iAttributeValue.length();

      if ( iSPMRule != -1 )
      {
         if ( attributeValueLength > iSPMRule )
         {
            msgText = "The text contained in attribute \"" +
                      iAttributeName + "\" is greater than " + iSPMRule + ".";
            mLogger.info( "WARNING: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                                            MessageType.WARNING,
                                                            msgText ) );
         }
         else if ( attributeValueLength < 1 )
         {
            msgText = "No text was found in attribute \"" + iAttributeName +
                      "\" and fails the minimum character length test";
            mLogger.info( "FAILED: " + msgText );
            MessageCollection.getInstance().add( new Message(MessageType.FAILED,
                                                             msgText ) );

            result = false;
         }
         else
         {
            msgText = "Character length for attribute \"" + iAttributeName +
                      "\" has been verified";
            mLogger.info( "PASSED: " + msgText );
            MessageCollection.getInstance().add( new Message(MessageType.PASSED,
                                                             msgText ) );
         }
      }
      else if ( attributeValueLength < 1 )
      {
         msgText = "No text was found in attribute \"" + iAttributeName +
                   "\" and fails the minimum character length test";
         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                           msgText ) );

         result = false;
      }
      else
      {
         msgText = "Character length for attribute \"" + iAttributeName +
                   "\" has been verified";
         mLogger.info( "PASSED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.PASSED,
                                                           msgText ) );
      }

      return result;
   }

   /**
    * This method returns the number of elements with the given name based on
    * the given parent node of the dom tree.<br>
    *
    * @param iParentNode
    *                  parent node of the element being searched<br>
    * @param iNodeName
    *                  name of the element being searched for<br>
    *
    * @return int: number of instances of a given element with a given path.<br>
    */
   private int getMultiplicityUsed(Node iParentNode, String iNodeName)
   {
      //need a list to find how many kids to cycle through
      NodeList kids = iParentNode.getChildNodes();
      int count = 0;

      int kidsLength = kids.getLength();
      for ( int i = 0; i < kidsLength; i++ )
      {
         if ( kids.item(i).getNodeType() == Node.ELEMENT_NODE )
         {
            String  currentNodeName = kids.item(i).getLocalName();
            if ( currentNodeName.equalsIgnoreCase( iNodeName ) )
            {
               count++;
            }
         }
      }

      return count;
   }

   /**
     * This method determines if the given mandatory element is present within
     * the xml test subject.<br>
     *
     * @param iTestSubjectNode
     *               parent node of the element being searched<br>
     * @param iNodeName
     *               element node name being searched for mandatory presence.<br>
     *
     * @return boolean: A true value implies that the mandatory element was
     * detected in the xml met-data instance, a false implies otherwise.<br>
     */
   private boolean checkForMandatory(Node iTestSubjectNode, String iNodeName)
   {

      boolean result = true;
      String msgText = new String();
      int multiplicityUsed = getMultiplicityUsed( iTestSubjectNode, iNodeName);

      if ( multiplicityUsed < 1 )
      {
         msgText = "Mandatory element <" + iNodeName + "> does not exist";
         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                           msgText ) );
         result = false;
      }
      return result;
   }

   /**
    *
    */
   private boolean checkForMandatorySchemas( Node iTestSubjectNode,
                                             String iNodeName)
   {

      boolean result = true;
      String msgText = new String();
      int multiplicityUsed = getMultiplicityUsed( iTestSubjectNode, iNodeName);

      if ( multiplicityUsed < 2 )
      {
         msgText = "Mandatory elements <" + iNodeName + "> does not exist " +
                   "two or more times" ;
         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                           msgText ) );
         result = false;
      }
      return result;
   }


   /**
     * This method performs the check of the multiplicy used against the
     *  supplied min and max rule values.<br>
     *
     * @param iMultiplicityUsed
     *               the multiplicity value determined for the element<br>
     * @param iMinRule
     *                the minimum value allowed for the element<br>
     * @param iMaxRule
     *                the maximum value allowed for the element<br>
     *
     * @return boolean: if the multiplicty checks passed or failed.  A true
     * value implies that the element was found to be within the multiplicity
     * bounds, a false implies otherwise.<br>
     */
   private boolean checkMultiplicity( int iMultiplicityUsed, int iMinRule,
                                      int iMaxRule, String iElementName )
   {
      boolean result = true;
      String msgText = new String();

      if ( (iMinRule != -1) && (iMaxRule != -1) )
      {
         if ( iMultiplicityUsed >= iMinRule && iMultiplicityUsed <= iMaxRule )
         {
            msgText = "Multiplicity for element <" +
                      iElementName + "> has been verified";
            mLogger.info( "PASSED: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                                MessageType.PASSED, msgText ) );
         }
         else
         {
            if ( ( iMaxRule > 1 ) || ( iMaxRule > 2 ) )
                 // we are handing smp multiplicity
            {
               msgText = "The <" + iElementName +
                         "> element is not within the SPM multiplicity bounds.";
               mLogger.info( "WARNING: " + msgText );
               MessageCollection.getInstance().add( new Message(
                                               MessageType.WARNING, msgText ) );
            }
            else // we are dealing with no smp multiplicity
            {
               msgText = "The <" + iElementName +
                         "> element is not within the multiplicity bounds.";
               mLogger.info( "FAILED: " + msgText );
               MessageCollection.getInstance().add( new Message(
                                                MessageType.FAILED, msgText ) );
               result = false;
            }

         }
      }
      else if ( (iMinRule != -1) && (iMaxRule == -1) )
      {
         if ( iMultiplicityUsed >= iMinRule )
         {
            msgText = "Multiplicity for element <" + iElementName +
                      "> has been verified";
            mLogger.info( "PASSED: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                           MessageType.PASSED,
                                           msgText ) );
         }
         else
         {
            msgText = "The <" + iElementName +
                      "> element is not within the multiplicity bounds.";
            mLogger.info( "FAILED: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                           MessageType.FAILED,
                                           msgText ) );
            result = false;
         }
      }
      return result;
   }


   /**
     * This method valids a langstring typed element by performing the
     * multiplicity and smp checks  on the string element.<br>
     *
     * @param iCurrentLangstringElement
     *               the element node that is of type langstring <br>
     *
     * @return: boolean: if the langstring check passed or failed.  A true
     * value implies that the langstring typed element passed, false implies
     * otherwise. <br>
     */
   private boolean checkLangString( Node iCurrentLangstringElement, String iPath )
   {
      boolean result = true;
      String msgText = new String();
      int multiplicityUsed = -1;
      int minRule = -1;
      int maxRule = -1;
      int spmRule = -1;
      String langstringElement = "string";
      String stringValue = new String();
      String currentLangstringElementName =
                                       iCurrentLangstringElement.getLocalName();

      //ensure that the mandatory string exists
      boolean stringElementExists = checkForMandatory( iCurrentLangstringElement,
                                                       "string");

      if ( stringElementExists )
      {
         //only check multiplicity if string element exists
         multiplicityUsed = getMultiplicityUsed( iCurrentLangstringElement,
                                                 "string" );

         //get the min rule for string and convert to an int
         minRule = Integer.parseInt( mMetadataRulesValidator.getRuleValue(
                                                        "string", "", "min") ) ;
         //get the max rule for string and convert to an int
         maxRule = Integer.parseInt(
                         mMetadataRulesValidator.getRuleValue(
                                             "string", "", "max") );
         //check multiplicity of string
         result = checkMultiplicity( multiplicityUsed, minRule, maxRule,
                                     "string" ) && result;

         NodeList langStringChild = iCurrentLangstringElement.getChildNodes();
         int numChild = langStringChild.getLength();

         //test multiplicity of language attribute
         for ( int i = 0; i < numChild; i++ )
         {
             Node stringNode = langStringChild.item(i);
             String stringName = stringNode.getLocalName();

             if ( stringName.equals("string") )
             {
                //look for the attribute of this element
                NamedNodeMap attrList = stringNode.getAttributes();
                int numAttr = attrList.getLength();

                for ( int j = 0; j < numAttr; j++ )
                {
                   Attr stringAttribute = (Attr)attrList.item(j);
                   String attributeName = stringAttribute.getName();
                   String attributeValue = null;

                   if ( attributeName.equals("language") )
                   {
                      //check multiplicity of language attribute
                      msgText = "Testing attribute " + "\"language" +
                         "\" of the LangString Type for minimum conformance";
                      mLogger.info( "INFO: " + msgText );
                      MessageCollection.getInstance().add(
                                     new Message( MessageType.INFO, msgText ) );
                      msgText = "Multiplicity for attribute " +
                                          "\"language" + "\" has been verified";
                      mLogger.info( "PASSED: " + msgText );
                      MessageCollection.getInstance().add(
                                    new Message(MessageType.PASSED, msgText ) );

                      //get the spm rule and convert to an int
                      spmRule = Integer.parseInt(
                         mMetadataRulesValidator.getRuleValue(langstringElement,
                                                    "", "spm", attributeName) );

                      attributeValue = stringAttribute.getValue();

                      // check the attribute for spm conformance.
                      result = checkSPMAttributeConformance( attributeName,
                                                             attributeValue,
                                                             spmRule )
                                                             && result;
                   }
                }
                //check to see if smp is defined for element.
                //SPECIAL CASE:  this should only occur for description of
                //               datetime/duration type
               //get the spm rule and convert to an int

                spmRule = Integer.parseInt(
                         mMetadataRulesValidator.getRuleValue(
                                     currentLangstringElementName, iPath, "spm") );

               //check to see if smp is defined for element
               // retrieve the value of this child element
               stringValue = mMetadataRulesValidator.getTaggedData
                                                           ( stringNode );

               // check spm conformance.
               result = checkSPMConformance( langstringElement, stringValue,
                                             spmRule ) && result;
             }
         }
      }
      else
      {
         result = stringElementExists && result;
      }
      return result;
   }

   /**
     * This method validates the text typed elements by performing the
     * smallest permitted maximum. <br>
     *
     * @param iTextElement
     *               the element node that is of type text<br>
     * @param iTextElementName
     *               the element name that is of type text<br>
     * @param iPath
     *               the path identifier of the element in question<br>
     *
     * @return boolean: if the text check passed or failed.  A true value
     * implies that the text typed element passed the check, false implies
     * otherwise.<br>
     */
   private boolean checkText( Node iTextElement, String iTextElementName,
                              String iPath )
   {

      int spmRule = -1;
      boolean result = true;

      // first must retrieve the value of this child element
      String textElementValue =
         mMetadataRulesValidator.getTaggedData( iTextElement );

      //get the spm rule and convert to an int
      spmRule = Integer.parseInt(
                         mMetadataRulesValidator.getRuleValue( iTextElementName,
                                                               iPath, "spm" ) );

      result = checkSPMConformance( iTextElementName,textElementValue, spmRule )
                                     && result;
      return result;
   }

   /**
    * This method assists with the application profile check for valid
    * restricted string values.  The restricted string value is compared to
    * those defined by the application profile rules.
    *
    * @parm iName
    *                Name of the element/attribute being checked
    *                for valid vocabulary.<br>
    * @param iValue
    *               Vocabulary string value that exists for the
    *               element/attribute in the test subject<br>
    * @param vocabValues
    *               Vector containing a list of the valid restricted string
    *               value for the element/attribute.<br>
    *
    * @return     true if the value is a valid string token, false otherwise<br>
    */
   private boolean checkMetadataSchema()
   {
      mLogger.entering("MDValidator", "checkMetadataSchema()" );

      boolean result = false;
      String msgText;
      String currentElementName = "metadataSchema";
      boolean foundLOMSchema = false;
      boolean foundSCORMCAMSchema = false;

      // The <metadataSchema> element shall contain restricted string values

      msgText = "Testing element \"" + currentElementName +
                     "\" for SCORM schema values";

      mLogger.info( "INFO: " + msgText );
      MessageCollection.getInstance().add( new Message(
                                           MessageType.INFO,
                                           msgText ) );

      // retrieve the restricted string values for the <metadataSchema> element
      Vector vocabValues =
      mMetadataRulesValidator.getVocabRuleValues( currentElementName,
                                                  "lom.metaMetadata" );
      int mMetadataSchemaNodeListSize = mMetadataSchemaNodeList.size();

      int vocabVectorSize = vocabValues.size();

      for ( int i = 0; i < mMetadataSchemaNodeListSize; i++ )
      {
         Node currentMetadataSchemaNode =
            (Node)mMetadataSchemaNodeList.elementAt(i);
         // retrieve the value of this element
         String currentMetadataSchemaValue =
             mMetadataRulesValidator.getTaggedData( currentMetadataSchemaNode );

         // Now loop through the valid vocabulary vector to see if the
         // value matches a valid token

         int vocabValuesSize = vocabValues.size();
         for ( int j = 0; j < vocabValuesSize; j ++ )
         {
            String currentVocabToken = (String)vocabValues.elementAt(j);

            if ( currentMetadataSchemaValue.equals( currentVocabToken ) )
            {
               if ( currentVocabToken.equals("LOMv1.0") )
               {
                  foundLOMSchema = true;
                  mLogger.finer( "Found LOMv1.0" );
                  break;
               }
               else if ( currentVocabToken.equals("SCORM_CAM_v1.3") )
               {
                  foundSCORMCAMSchema = true;
                  mLogger.finer("Found SCORM_CAM_v1.3");
                  break;
               }
            }
         }
      }

      if ( foundLOMSchema )
      {
          msgText = "\" LOMv1.0 \" was detected " +
                    "for element <metadataSchema>" ;
          mLogger.info( "PASSED: " + msgText );
          MessageCollection.getInstance().add( new Message( MessageType.PASSED,
                                                            msgText ) );
      }
      else
      {
          msgText = "\" LOMv1.0 \" was not detected " +
                    "for element <metadataSchema>" ;
          mLogger.info( "FAILED: " + msgText );
          MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                            msgText ) );
      }
      if ( foundSCORMCAMSchema )
      {
          msgText = "\" SCORM_CAM_v1.3 \" was detected for element " +
                    "<metadataSchema>" ;
          mLogger.info( "PASSED: " + msgText );
          MessageCollection.getInstance().add( new Message( MessageType.PASSED,
                                                            msgText ) );
      }
      else
      {
         msgText = "\" SCORM_CAM_v1.3 \" was not detected for element " +
                   "<metadataSchema>" ;
         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                            msgText ) );
      }

      mMetadataSchemaTracked = true;
      mLogger.exiting("MDValidator", "checkMetadataSchema" );

      result =  foundLOMSchema &&  foundSCORMCAMSchema;
      return result;
   }

    /**
     * The method determines if the datetime and description pair of the
     * datetime typed element being tested is valid according to the rules
     * defined in the Application Profile.  The parser verifies only that no
     * more than datetime and description elements exist for an element.<br>
     *
     * @parm iElementName
     *                The name of the element being checked
     *                for the datetime type<br>
     * @parm typeOfPair
     *                Distinguishes what type is being checked for<br>
     *                -"datetime"<br>
     *                -"duration" <br>
     * @param iElement
     *                   The Datetime typed or Duration typed element node.<br>
     *
     * @return boolean: if the datetime/duration type check passed of failed.
     * A true value implies that the element passed the datetime/duration checks,
     * false implies otherwise.<br>
     */
    private boolean checkDatetimeOrDurationPair( Node iElement,
                                                 String iElementName,
                                                 String typeOfPair )
    {
       boolean result = true;
       boolean descriptionExists = false;
       int spmRule = -1;
       String msgText = "";
       String stringValue = new String();

       if ( typeOfPair.equals("datetime") )
       {
          //check to ensure datetime exists 1 and only 1 time
          result = checkForMandatory( iElement, "dateTime" ) && result;
       }
       else if ( typeOfPair.equals("duration") )
       {
          //check to ensure duration exists 1 and only 1 time
          result = checkForMandatory( iElement, "duration" ) && result;
       }
       //determine if optional description element exists
       int multiplicityUsed = getMultiplicityUsed( iElement, "description" );
       if ( multiplicityUsed > 0 )
       {
         descriptionExists = true;
       }

       //retrieve datetime and description elements OR
       // retrive duration and description element
       if ( result )
       {
          NodeList children = iElement.getChildNodes();
          int numChildren = children.getLength();

          for ( int i=0; i < numChildren; i++ )
          {
             Node currentChild = children.item(i);
             String currentChildName = currentChild.getLocalName();

             if ( ( currentChildName.equals("dateTime") ) ||
                  ( currentChildName.equals("duration") ) )
             {
                // retrieve the value of this element
                String currentChildValue =
                mMetadataRulesValidator.getTaggedData( currentChild );

                //get the spm rule and convert to an int
                spmRule = Integer.parseInt(
                            mMetadataRulesValidator.getRuleValue(
                                                currentChildName, "", "spm" ) );

                result = checkSPMConformance( currentChildName,
                                              currentChildValue,
                                              spmRule ) && result;
             }
          }
       }
       if ( descriptionExists )
       {
          NodeList children = iElement.getChildNodes();
          int numChildren = children.getLength();

          for ( int i=0; i < numChildren; i++ )
          {
             Node currentChild = children.item(i);
             String currentChildName = currentChild.getLocalName();

             if ( currentChildName.equals("description") )
             {
               //This is a Langstring data type element
               msgText = "Testing element <" + currentChildName +
                         "> for the LangString Type";
               mLogger.info( "INFO: " + msgText );
               MessageCollection.getInstance().add( new Message(
                                                  MessageType.INFO, msgText ) );

               result = checkLangString( currentChild, "" ) && result;
             }
          }
       }
       return result;
    }

    /**
     * The method validates the vocabulary typed elements by determining if the
     * source and value pair of the element being tested is valid according to
     * the rules defined in the Application Profile.  The parser validates only
     * that 1 source and value elements exist for an element.<br>
     *
     *  @parm iElementName
     *                 The name of the element being checked
     *                 for valid vocabulary<br>
     *  @param iVocabularyElement
     *                The vocabulary type element node.<br>
     *  @param iVocabularyType
     *                The vocabulary type being checked for<br>
     *                -"bestpractice"<br>
     *                -"restricted"
     *
     * @return boolean: if the vocabulary type check passed of failed. A true
     * value implies that the vocabulary typed element passed the checks, false
     * implies otherwise.<br>
     */
    private boolean checkSourceValuePair( Node iVocabularyElement,
                                          String iElementName,
                                          String iVocabularyType,
                                          String iPath )
    {
       boolean result = true;
       int spmRule = -1;
       String msgText = "";
       String vocabularyElementName = iVocabularyElement.getLocalName();
       boolean checkBestPracticeVocabulary = true;

       //check to ensure source exists 1 and only 1 time
       result = checkForMandatory( iVocabularyElement, "source" ) && result;

       //check to ensure value exists 1 and only 1 time
       result = checkForMandatory( iVocabularyElement, "value" ) && result;

       //only continue if these elements exist
       if ( result )
       {

          //retrieve source and value elements
          NodeList vocabularyChildren =
                                 iVocabularyElement.getChildNodes();
          int numVocabChildren = vocabularyChildren.getLength();

          for ( int i=0; i < numVocabChildren; i++ )
          {
             Node currentVocabChild = vocabularyChildren.item(i);
             String currentVocabChildName = currentVocabChild.getLocalName();

             if ( currentVocabChildName.equals("source") )
             {
                // retrieve the value of this element
                String currentVocabChildValue =
                mMetadataRulesValidator.getTaggedData( currentVocabChild );


                //get the spm rule and convert to an int
                spmRule = Integer.parseInt(
                   mMetadataRulesValidator.getRuleValue( currentVocabChildName,
                                                         "", "spm" ) );

                result = checkSPMConformance( currentVocabChildName,
                                             currentVocabChildValue,
                                             spmRule ) && result;

                if ( !( currentVocabChildValue.equalsIgnoreCase( "LOMv1.0" )) &&
                     iVocabularyType.equals( "bestpractice" ) )
                {
                   msgText = "<"+ iElementName + "> does not exercise " +
                            "the best practice LOM vocabularies";
                   mLogger.info( "WARNING: " + msgText );
                   MessageCollection.getInstance().add(
                                 new Message( MessageType.WARNING, msgText ) );
                   checkBestPracticeVocabulary = false;
                }
             }
             else if ( currentVocabChildName.equals("value") )
             {
                // retrieve the value of this element
                String currentVocabChildValue =
                mMetadataRulesValidator.getTaggedData( currentVocabChild );

                //SPECIAL CASE for the name/type pair
                if ( vocabularyElementName.equalsIgnoreCase("type") )
                {
                   mTypeValue = currentVocabChildValue;
                }
                else if ( vocabularyElementName.equalsIgnoreCase("name") )
                {
                   mNameValue = currentVocabChildValue;

                }

                //get the spm rule and convert to an int
                spmRule = Integer.parseInt(
                mMetadataRulesValidator.getRuleValue( currentVocabChildName,
                                                      "", "spm" ) );

                result = checkSPMConformance( currentVocabChildName,
                                              currentVocabChildValue,
                                              spmRule ) && result;
                // check vocab token
                Vector vocabValues =
                mMetadataRulesValidator.getVocabRuleValues( iElementName,
                                                            iPath );

                if ( checkBestPracticeVocabulary )
                {
                   result = checkVocabulary( iElementName,
                                             currentVocabChildValue,
                                             vocabValues ) && result;
                }
             }

          }
       }
       return result;
    }


    /**
     * The method determines if the type and name elements abide to the best
     * practice vocabulary rules as defined by LOM.<br>
     *
     * @param iCurrentChildName
     *           The element name in question<br>
     */
    private void checkNameTypePair( String iCurrentChildName )
    {
       String msgText = new String();

       if ( iCurrentChildName.equalsIgnoreCase("name") )
       {
          if ( !mTypeValue.equals("") )
          {
             if ( mTypeValue.equals("operating system") &&
                  ( !mNameValue.equals("pc-dos") &&
                    !mNameValue.equals("ms-windows") &&
                    !mNameValue.equals("macos") &&
                    !mNameValue.equals("unix") &&
                    !mNameValue.equals("multi-os") &&
                    !mNameValue.equals("none") ) )
             {
                msgText = "Element <type> and Element <" +
                           iCurrentChildName + "> do not " +
                           "exercise the best practice LOM " +
                           "vocabularies";
                mLogger.info( "WARNING: " + msgText );
                MessageCollection.getInstance().add( new Message(
                                               MessageType.WARNING, msgText ) );
             }
             else if ( mTypeValue.equals("browser") &&
                  ( !mNameValue.equals("any") &&
                    !mNameValue.equals("netscape communicator") &&
                    !mNameValue.equals("ms-internet explorer") &&
                    !mNameValue.equals("opera") &&
                    !mNameValue.equals("amaya") ) )
             {
                msgText = "Element <type> and Element <" +
                           iCurrentChildName + "> do not " +
                           "exercise the best practice LOM " +
                           "vocabularies";
                mLogger.info( "WARNING: " + msgText );
                MessageCollection.getInstance().add( new Message(
                                               MessageType.WARNING, msgText ) );
             }
          }
          else
          {
             msgText = "Element <type> and Element <name> must co-exist " +
                       "in order to exercise the best practice LOM vocabularies";
             mLogger.info( "WARNING: " + msgText );
             MessageCollection.getInstance().add( new Message(
                                               MessageType.WARNING, msgText ) );
          }
       }
    }
}

