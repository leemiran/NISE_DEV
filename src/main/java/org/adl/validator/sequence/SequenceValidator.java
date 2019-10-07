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
package org.adl.validator.sequence;

// native java imports
import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.util.logging.*;

// xerces imports
import org.w3c.dom.*;

// xalan imports
import com.sun.org.apache.xpath.internal.XPathAPI;
import javax.xml.transform.TransformerException;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.NodeSet;

// adl imports
import org.adl.validator.*;
import org.adl.util.zip.*;
import org.adl.util.*;
import org.adl.util.debug.*;
import org.adl.parsers.dom.DOMTreeUtility;

/**
 * <strong>Filename: </strong>SequenceValidator.java<br>
 *
 * <strong>Description: </strong>The <code>SequenceValidator</code> object
 * determines whether the content package test subject is conformant with the
 * Sequence Application Profile. The SequenceValidator is spawned from
 * the CPValidator to validate the content package test subject
 * against the rules and requirements necessary for meeting each
 * Sequence Application Profile.<br>
 *
 * <strong>Design Issues: </strong>none<br>
 *
 * <strong>Implementation Issues: </strong>none<br>
 *
 * <strong>Known Problems: </strong>none<br>
 *
 * <strong>Side Effects: </strong>none<br>
 *
 * <strong>References: </strong>SCORM<br>
 *
 * @author ADL Technical Team<br>
 */
public class SequenceValidator implements Serializable
{

  /**
   * Logger object used for debug logging.<br>
   */
  private Logger mLogger;

  /**
   * RulesValidator object<br>
   */
  private RulesValidator mRulesValidator;

  /**
   * String containing the environment variable for determination of the Test
   * Suite installation directory.<br>
   */
  private String mEnvironmentVariable;

  /**
   * This attribute contains the populated ObjectiveMap object, containing all
   * the information needed to validate global objectives.<br>
   */
  private ObjectiveMap mObjectiveInfo;


  /**
   * Default Constructor.<br>
   */
  public SequenceValidator( String iEnvironmentVariable )
  {
     mLogger = Logger.getLogger("org.adl.util.debug.validator");

     mRulesValidator = new RulesValidator( "sequence" );
     mEnvironmentVariable = iEnvironmentVariable;
     mObjectiveInfo = new ObjectiveMap();
  }

  /**
   * This method is called to initiate the validation process.
   * This method will trigger the parsing activity done by the ADLSCORMValidator.
   * Next, the DOM will be used to validate the remaining checks required for full
   * SCORM Validation.<br>
   *
   * @param iRootNode Root sequence element.<br>
   *
   * @return - Boolean value indicating the outcome of the validation checks.
   * <br>
   */
  public boolean validate( Node iRootNode )
  {
     boolean validateResult = true;
     String msgText;
     String nodeName = iRootNode.getLocalName();

     mLogger.entering( "SequenceValidator", "validate()" );

     mLogger.finer( "      iRootNodeName coming in is " + nodeName );

     mRulesValidator.readInRules( "sequence", mEnvironmentVariable );

     // check the parent and make sure it is in the right place
     String parentNodeName = iRootNode.getParentNode().getLocalName();

     if ( nodeName.equals("sequencingCollection") )
     {
        if ( ! parentNodeName.equals("manifest") )
        {
           msgText = "Element <imsss:sequencingCollection> can only " +
                            "exist as a child of element <manifest>";
           mLogger.info( "FAILED: " + msgText );
           MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                             msgText ) );
           validateResult = false && validateResult;
        }

     }
        // check the <sequencing> element and its children
        validateResult = compareToRules( iRootNode, "" ) && validateResult;

        mLogger.exiting( "SequenceValidator", "validate()" );

        return validateResult;
  }

   /**
    * This method validates the sequencingcollection, if it exists, against the
    * rules defined in the Sequence Application Profile.<br>
    *
    * @param iSequencingcollectionNode  sequencingCollection node<br>
    *
    * @return - boolean describing if the sequencingCollection element is
    * value or not.<br>
    */
   private boolean checkSequencingcollection( Node iSequencingcollectionNode )
   {
      boolean result = true;  // innocent until proven guilty
      boolean foundSequencing = false;
      String msgText = new String();

      String nodeName = iSequencingcollectionNode.getLocalName();


      // loop through children and call compare
      NodeList kids = iSequencingcollectionNode.getChildNodes();
      Node currentNode = null;

      //cycle through all children of node to find the <sequencing> nodes
      if ( kids != null )
      {
         int n = kids.getLength();

         for ( int i = 0; i < n-1; i++ )
         {
            currentNode = kids.item(i);

            // find the <sequencing> nodes
            if ( currentNode.getLocalName().equals("sequencing") )
            {
               foundSequencing = true;
               // validate to the application profiles
               result = compareToRules( currentNode, "sequencingCollection" )
                        && result;
            }
         }
      }
      if ( !foundSequencing )
      {
         //report an error for not having mandatory sequencing children
         result = false && result;
         msgText = "Mandatory <sequencing> element not found " +
                   "in the <sequencingCollection>";
         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                           msgText ) );
      }
      return result;
   }

   /**
    *
    * A recursive method that is driven by the test subject dom.
    * This method performs the application profiles rules checks.<br>
    *
    * @param iTestSubjectNode Test Subject DOM<br>
    * @param iPath Path of the rule to compare to<br>
    *
    * @return - boolean result of the checks performed<br>
    *
    */
   private boolean compareToRules( Node iTestSubjectNode, String iPath )
   {
      // looks exactly like prunetree as we walk down the tree
      mLogger.entering( "SequenceValidator", "compareToRules" );

      boolean result = true;
      String msgText = new String();

      // is there anything to do?
      if ( iTestSubjectNode == null )
      {
         result = false && result;
         return result;
      }

      int type = iTestSubjectNode.getNodeType();

      switch ( type )
      {
         // element with attributes
         case Node.ELEMENT_NODE:
         {
            String parentNodeName = iTestSubjectNode.getLocalName();

            result = checkAttributes( iTestSubjectNode,
                                      parentNodeName,
                                      iPath ) && result;


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
                    parentNodeName.equalsIgnoreCase("sequencing") )
               {
                  // the Node is a DOCUMENT OR
                  // the Node is a <manifest>

                  path = parentNodeName;
               }
               else
               {
                  path = iPath + "." + parentNodeName;
               }

               // SPECIAL CASE: check for mandatory elements
               // there are currently no mandatory elements

               for ( int z = 0; z < numChildren; z++ )
               {
                  Node currentChild = children.item(z);
                  String currentChildName = currentChild.getLocalName();

                  // must enforce that the adlseq namespaced elements exist
                  // as a child of sequencing only.

                  if ( ( ( currentChildName.equals("constrainedChoiceConsiderations") ) ||
                         ( currentChildName.equals("rollupConsiderations") ) ) &&
                         ( !parentNodeName.equals("sequencing") )  )
                  {

                     result = false && result;

                     msgText = "<" + currentChildName + "> can only " +
                               "exist as a child of an <sequencing> element";

                     mLogger.info( "FAILED: " + msgText );
                     MessageCollection.getInstance().add( new Message(
                                                   MessageType.FAILED, msgText ) );
                  }


                  dataType = mRulesValidator.getRuleValue( currentChildName,
                                                          path, "datatype" );

                  // make sure that this is a SCORM recognized attribute
                  if ( ! dataType.equalsIgnoreCase("-1") )
                  {
                     msgText = "Testing element <" + currentChildName +
                               "> for minimum conformance";
                     mLogger.info( "INFO: " + msgText );
                     MessageCollection.getInstance().add( new Message(
                                                               MessageType.INFO,
                                                               msgText ) );

                     // check for multiplicity if the element is not deprecated
                     if ( ! dataType.equalsIgnoreCase("deprecated") )
                     {
                        multiplicityUsed = getMultiplicityUsed(iTestSubjectNode,
                                                             currentChildName );

                        //get the min rule and convert to an int
                        minRule = Integer.parseInt( mRulesValidator.getRuleValue(
                                                               currentChildName,
                                                               path, "min") ) ;
                        //get the max rule and convert to an int
                        maxRule = Integer.parseInt( mRulesValidator.getRuleValue(
                                                               currentChildName,
                                                               path, "max") );

                        if ( (minRule != -1) && (maxRule != -1) )
                        {
                           if ( multiplicityUsed >= minRule &&
                                multiplicityUsed <= maxRule )
                           {
                              msgText = "Multiplicity for element <" +
                                        currentChildName +
                                        "> has been verified";
                              mLogger.info( "PASSED: " + msgText );
                              MessageCollection.getInstance().add( new Message(
                                                             MessageType.PASSED,
                                                             msgText ) );
                           }
                           else
                           {
                              msgText = "The <" + currentChildName +
                                        "> element is not within the " +
                                        "multiplicity bounds.";
                              mLogger.info( "FAILED: " + msgText );
                              MessageCollection.getInstance().add( new Message(
                                                             MessageType.FAILED,
                                                             msgText ) );

                              result = false && result;
                           }
                        }
                        else if ( (minRule != -1) && (maxRule == -1) )
                        {
                           if ( multiplicityUsed >= minRule )
                           {
                              msgText = "Multiplicity for element <" +
                                        currentChildName +
                                        "> has been verified";
                              mLogger.info( "PASSED: " + msgText );
                              MessageCollection.getInstance().add( new Message(
                                                             MessageType.PASSED,
                                                             msgText ) );
                           }
                           else
                           {
                              msgText = "The <" + currentChildName +
                                        "> element is not within the " +
                                        "multiplicity bounds.";
                              mLogger.info( "FAILED: " + msgText );
                              MessageCollection.getInstance().add( new Message(
                                                             MessageType.FAILED,
                                                             msgText ) );
                              result = false && result;
                           }
                        }
                     }

                     // check the contents of the attribute
                     if ( dataType.equalsIgnoreCase("parent") )
                     {
                        //This is a parent element

                        // special validation for sequencingCollection
                        if ( currentChildName.equals("sequencingCollection")
                             && iPath.equals("") )
                        {

                           result = checkSequencingcollection( currentChild ) &&
                                    result;
                        }

                        if( currentChildName.equals("objectives") )
                        {
                           // we must enforce that objectiveIDs must be unique
                           // for a given activity only.

                           result = checkObjectiveIDsForUniqueness( currentChild )
                                    && result;
                        }

                        //objectiveID is mandatory for primaryObjective only if it
                        //contains a mapInfo as a child
                        if ( currentChildName.equals("primaryObjective") )
                        {
                           // Test the child Nodes
                           NodeList objChildren = currentChild.getChildNodes();

                           boolean isObjectiveIDMandatory = false;
                           for ( int i = 0; i < objChildren.getLength(); i++ )
                           {
                              Node objChild = objChildren.item(i);
                              String objChildName = objChild.getLocalName();

                              if ( objChildName.equals("mapInfo") )
                              {
                                 isObjectiveIDMandatory = true;
                              }
                           }

                           if ( isObjectiveIDMandatory )
                           {
                              // ObjectiveID attribute is mandatory if mapInfo
                              // child elements are present.
                              Attr currAttribute =
                                 DOMTreeUtility.getAttribute( currentChild,
                                                              "objectiveID" );

                              if ( currAttribute == null )
                              {
                                 result = result && false;
                                 msgText = "ObjectiveID attribute is mandatory " +
                                    "for a <primaryObjective> element that " +
                                    "contains <mapInfo> element(s).";

                                 mLogger.info( "FAILED: " + msgText );
                                 MessageCollection.getInstance().add(
                                 new Message( MessageType.FAILED, msgText ) );

                              }
                           }

                        }

                        // special validation for global objectives
                        if ( currentChildName.equals("objectives") &&
                                  path.equals("sequencing") )
                        {
                           msgText = "Performing Global/Local Objectives " +
                                     "Validation";
                           mLogger.info( "INFO: " + msgText );
                           MessageCollection.getInstance().add(
                                     new Message( MessageType.INFO, msgText ) );

                           mObjectiveInfo.populateObjectiveMap( currentChild );

                           result = mObjectiveInfo.validateObjectiveMaps( mObjectiveInfo)
                                    && result;
                        }

                        if ( currentChildName.equals("auxiliaryResources") &&
                                  path.equals("sequencing") )
                        {
                           // this element is out of scope of SCORM
                           msgText = "At this time, SCORM recommends use of " +
                                     "the <" + currentChildName + "> element " +
                                     "with caution due to the various " +
                                     "concerns that have risen dealing with " +
                                     "defining requirements on its usage.";
                           mLogger.info( "WARNING: " + msgText );
                           MessageCollection.getInstance().add( new Message(
                                                            MessageType.WARNING,
                                                            msgText ) );
                        }

                        result = compareToRules( currentChild, path )
                                 && result;


                     }
                     else if ( dataType.equalsIgnoreCase("deprecated") )
                     {
                        // This is a deprecated element
                        msgText = "The <" + currentChildName + "> element " +
                                  "has been deprecated.";
                        mLogger.info( "FAILED: " + msgText );
                        MessageCollection.getInstance().add( new Message(
                                                             MessageType.FAILED,
                                                             msgText ) );
                        result = false && result;
                     }
                     else if ( dataType.equalsIgnoreCase("text") )
                     {
                        // This is a text data type
                        // check spm

                       // first must retrieve the value of this child element
                       String currentChildValue =
                            mRulesValidator.getTaggedData( currentChild );

                       //get the spm rule and convert to an int
                       spmRule = Integer.parseInt(
                                 mRulesValidator.getRuleValue( currentChildName,
                                                              path, "spm" ) );

                       result = checkSPMConformance( currentChildName,
                                                     currentChildValue,
                                                     spmRule, false ) && result;
                     }
                     else if ( dataType.equalsIgnoreCase("vocabulary") )
                     {
                        // This is a vocabulary data type
                        // more than one vocabulary token may exist

                        msgText = "Testing element \"" + currentChildName +
                                  "\" for valid vocabulary";
                        mLogger.info( "INFO: " + msgText );
                        MessageCollection.getInstance().add( new Message(
                                                             MessageType.INFO,
                                                             msgText ) );

                        // retrieve the value of this element
                       String currentChildValue =
                            mRulesValidator.getTaggedData( currentChild );

                       Vector vocabValues =
                       mRulesValidator.getVocabRuleValues( currentChildName,
                                                          path );


                       result = checkVocabulary( currentChildName,
                                                 currentChildValue,
                                                 vocabValues, false ) && result;
                     }
                     if ( dataType.equalsIgnoreCase("decimal") )
                     {
                        //This is a decimal element
                     }
                     else if ( dataType.equalsIgnoreCase("leaf") )
                     {
                         // This is a leaf data type, must check attributes
                         result = checkAttributes( currentChild,
                                                   currentChildName,
                                                   path ) && result;
                     }
                     else
                     {
                        // This is an extension element
                        // no additional checks needed
                     }
                  }
               }
            }

            break;
         }

         default:
         {
            break;
         }
      }

      mLogger.exiting( "SequenceValidator", "compareToRules()" );


      return result;
   }

   /**
    * This method validates that all objectiveIDs on a primary objective and
    * an objective for a given activity are unique.<br>
    *
    * @param Node The objectives node that is the parent to the <primaryObject>
    * and <objective> elements.<br>
    *
    * @return boolean Returns true if the objectiveIDs for a given activity are
    * unique, false otherwise.<br>
    *
    */
   private boolean checkObjectiveIDsForUniqueness( Node iObjectivesNode )
   {
      boolean result = true;
      String msgText = "";

      // retrive ObjectiveNodes
      Vector objList = DOMTreeUtility.getNodes( iObjectivesNode, "objective");

      // retrieve <primaryObjective> node
      Node primObjNode = DOMTreeUtility.getNode( iObjectivesNode,
                                                 "primaryObjective");
      // add primaryOjective node to objList being both have objectiveIDs
      // that must be unique
      if( primObjNode != null )
      {
         objList.add( primObjNode );
      }

      // Loop through the objective and primaryObjective elements to retrieve
      // the objectiveID attribute values

      int objNodesSize = objList.size();

      Set objectiveIDList = new HashSet();

      for ( int i=0; i < objNodesSize; i++ )
      {
         Node currentChild = (Node)objList.elementAt(i);
         String objectiveID =
                 DOMTreeUtility.getAttributeValue( currentChild, "objectiveID");

         // this call will return a false if the id already exists in the list
         result = objectiveIDList.add( objectiveID );

         if( !result )
         {
            msgText = "Duplicate objectiveID detected:  " + objectiveID;

            mLogger.info( "FAILED: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                          MessageType.FAILED, msgText ) );
         }
      }

      return result;
   }


   /**
     * This method performs the smallest permitted maximum check.<br>
     *
     * @param iElementName Name of the element being checked for spm.<br>
     * @param iElementValue Value being checked for smp.<br>
     * @param iSPMRule Value allowed for spm ( value retrieved from rules ).<br>
     * @param iAmAnAttribute flags determines if its an attribute (true), or an
     * element that is being validated for valid vocabulary tokens.<br>
     *
     * @return - Boolean result of spm check.  A true value implies that the
     * smallest permitted checks passed, false implies otherwise.<br>
     *
     */
   private boolean checkSPMConformance( String iElementName,
                                        String iElementValue,
                                        int iSPMRule,
                                        boolean iAmAnAttribute )
   {
      boolean result = true;
      String msgText = new String();

      int elementValueLength = iElementValue.length();

      if ( iSPMRule != -1 )
      {
         if ( elementValueLength > iSPMRule )
         {
            if ( iAmAnAttribute )
            {
               msgText = "The text contained in " + "attribute \"" +
                         iElementName + "\" is greater than " + iSPMRule + ".";
            }
            else
            {
               msgText = "The text contained in element <" + iElementName +
                         "> is greater than " + iSPMRule + ".";
            }
            mLogger.info( "WARNING: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                                            MessageType.WARNING,
                                                            msgText ) );
         }
         else if ( elementValueLength < 1 )
         {
            if ( iAmAnAttribute )
            {
               msgText = "No text was found in attribute \"" + iElementName +
                         "\" and fails the minimum character length test";
            }
            else
            {
               msgText = "No text was found in element <" + iElementName +
                         "> and fails the minimum character length test";

            }
            mLogger.info( "FAILED: " + msgText );
            MessageCollection.getInstance().add( new Message(MessageType.FAILED,
                                                             msgText ) );

            result = false;
         }
         else
         {
            if ( iAmAnAttribute )
            {
               msgText = "Character length for attribute \"" + iElementName +
                         "\" has been verified";
            }
            else
            {
               msgText = "Character length for element <" + iElementName +
                         "> has been verified";
            }
            mLogger.info( "PASSED: " + msgText );
            MessageCollection.getInstance().add( new Message(MessageType.PASSED,
                                                             msgText ) );
         }
      }
      else if ( elementValueLength < 1 )
      {
         if ( iAmAnAttribute )
         {
            msgText = "No text was found in attribute \"" + iElementName +
                      "\" and fails the minimum character length test";
         }
         else
         {
            msgText = "No text was found in element <" + iElementName +
                      "> and fails the minimum character length test";
         }
         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                           msgText ) );

         result = false;
      }
      else
      {
         if ( iAmAnAttribute )
         {
            msgText = "Character length for attribute \"" + iElementName +
                      "\" has been verified";
         }
         else
         {
            msgText = "Character length for element <" + iElementName +
                      "> has been verified";
         }
         mLogger.info( "PASSED: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.PASSED,
                                                           msgText ) );
      }

      return result;
   }

   /**
    * Determines if the vocabulary value is a valid vocabulary token based on
    * the rules defined in the Application Profile. It is assumed that only
    * 1 vocabulary token may exist for an element/attribute.<br>
    *
    * @parm iName Name of the element/attribute being checked for valid
    * vocabulary.<br>
    * @param iValue Vocabulary string value that exists for the
    * element/attribute in the test subject.<br>
    * @param vocabValues Vector containing a list of the valid vocabulary values
    * for the element/attribute.<br>
    * @param iAmAnAttribute flags determines if its an attribute (true), or an
    * element that is being validated for valid vocabulary tokens.<br>
    *
    * @return boolean True if the value is a valid vocab token,
    * false otherwise.<br>
    *
    */
   private boolean checkVocabulary( String iName,
                                    String iValue,
                                    Vector iVocabValues,
                                    boolean iAmAnAttribute )
   {
      mLogger.entering("SequenceValidator", "checkVocabulary()" );

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

          if ( iAmAnAttribute )
          {

             msgText = "\"" + iValue + "\"  is a valid value for attribute \"" +
             iName + "\"";
          }
          else
          {

             msgText = "\"" + iValue + "\"  is a valid value for element <" +
             iName + ">";

          }
          mLogger.info( "PASSED: " + msgText );
          MessageCollection.getInstance().add( new Message( MessageType.PASSED,
                                                            msgText ) );
      }
      else
      {

          if ( iAmAnAttribute )
          {

             msgText = "\"" + iValue + "\"  is not a valid value for " +
                       "attribute \"" + iName + "\"";

          }
          else
          {

             msgText = "\"" + iValue + "\"  is not a valid value for element <" +
                       iName + ">";

          }
          mLogger.info( "FAILED: " + msgText );
          MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                            msgText ) );
      }

      if ( ( iName.equals("condition") ) &&
           ( ( iValue.equals("timeLimitExceeded") ) ||
             ( iValue.equals("outsideAvailableTimeRange") ) ) )
      {
         // this vocabulary token is out of scope of SCORM
         msgText = "At this time, SCORM recommends use of " +
                   "the \"" + iValue + "\" vocabulary token of the \"" + iName +
                   "\" attribute with caution due to the various " +
                   "concerns that have risen dealing with " +
                   "defining requirements on its usage.";

         mLogger.info( "WARNING: " + msgText );
         MessageCollection.getInstance().add( new Message(
                                          MessageType.WARNING,
                                          msgText ) );

      }
      mLogger.exiting("SequenceValidator", "checkVocabulary()" );

      return result;
   }


   /**
     * Returns the number of elements with the given
     * name based on the given parent node of the dom tree.<br>
     *
     * @param iParentNode Parent node of the element being searched.<br>
     * @param iNodeName Name of the element being searched for.<br>
     *
     * @return - int: number of instances of a given element<br>
     */
   public int getMultiplicityUsed(Node iParentNode, String iNodeName)
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
    * Returns the number of attributes with the given name based on the
    * attributelist passed in.<br>
    *
    * @param iAttributeMap list of attributes<br>
    * @param iNodeName Name of the element being searched for.<br>
    *
    * @return - int: Number of instances of a given attribute.<br>
    *
    */
   public int getMultiplicityUsed(NamedNodeMap iAttributeMap, String iNodeName)
   {
      int result = 0;
      int length = iAttributeMap.getLength();
      String currentName;

      for ( int i = 0; i < length; i++ )
      {
         currentName = ((Attr)iAttributeMap.item(i)).getLocalName();

         if ( currentName.equals(iNodeName) )
         {
            result ++;
         }
      }

      return result;
   }

   /**
    * This method validates the attribute values based on the rules defined
    * in the SCORM Application Profile rules.<br>
    *
    * @param iNode element parent node of the attribute being validated<br>
    * @param iNodeName Parent element node name<br>
    * @param iPath path of the rule to compare to
    *
    * @return boolean True if the value is a valid vocab token,
    * false otherwise.<br>
    *
    */
   public boolean checkAttributes( Node iNode,
                                   String iNodeName,
                                   String iPath )
   {
      String dataType = null;
      boolean result = true;
      String msgText = new String();
      int multiplicityUsed = -1;


      NamedNodeMap attrList = iNode.getAttributes();
      int numAttr = attrList.getLength();
      mLogger.finer( "There are " + numAttr + " attributes of " +
                      iNodeName + " to test" );

      // SPECIAL CASE: check for mandatory/shall not exist attributes on
      // sequencingCollection child elements

      if ( iNodeName.equalsIgnoreCase("sequencing") &&
           iPath.equals("sequencingCollection")  )
      {

         multiplicityUsed = getMultiplicityUsed( attrList,
                                                 "ID" );

         msgText = "Testing attributes of element <sequencing> of " +
                   "the <sequencingCollection>";
         mLogger.info( "INFO: " + msgText );
         MessageCollection.getInstance().add( new Message( MessageType.INFO,
                                                           msgText ) );

         if ( multiplicityUsed < 1 )
         {

            msgText = "Mandatory attribute \"ID\" could " +
                      "not be found";
            mLogger.info( "FAILED: " + msgText );
            MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                           msgText ) );

            result = false && result;

         }
         else
         {
            msgText = "Found mandatory attribute \"ID\" of element " +
                      "<sequencing>";

            mLogger.info( "PASSED: " + msgText );
            MessageCollection.getInstance().add( new Message( MessageType.PASSED,
                                                           msgText ) );
            result = true && result;
         }


         // IDRef is not allowed to be present
         multiplicityUsed = getMultiplicityUsed( attrList,
                                                 "IDRef" );
         if ( multiplicityUsed >= 1 )
         {
            msgText = "Attribute \"IDRef\" is not permitted on " +
                      "<sequencing> elements defined in the  " +
                      "<sequencingCollection>";
            mLogger.info( "FAILED: " + msgText );
            MessageCollection.getInstance().add( new Message(
                                           MessageType.FAILED,
                                           msgText ) );
            result = false && result;
         }

      }

      Attr currentAttrNode;
      String currentNodeName;
      String attributeValue = null;
      int minRule = -1;
      int maxRule = -1;
      int spmRule = -1;

      // test the attributes
      for ( int j = 0; j < numAttr; j++ )
      {
         currentAttrNode = (Attr)attrList.item(j);
         currentNodeName = currentAttrNode.getLocalName();

         dataType = mRulesValidator.getRuleValue( iNodeName,
                                                  iPath, "datatype",
                                                  currentNodeName );

         // make sure that this is a SCORM recognized attribute
         if ( ! dataType.equalsIgnoreCase("-1") )
         {
            msgText = "Testing attribute \"" + currentNodeName +
                      "\" for minimum conformance";
            mLogger.info( "INFO: " + msgText );
            MessageCollection.getInstance().add( new Message( MessageType.INFO,
                                                              msgText ) );

            // check for multiplicity if the attribute is not deprecated
            if ( ! dataType.equalsIgnoreCase("deprecated") )
            {
               multiplicityUsed = getMultiplicityUsed( attrList,
                                                       currentNodeName );

               // We will assume that no attribute can exist more than
               // once (ever).  According to W3C.  Therefore, min and max
               // rules must exist.

               //get the min rule and convert to an int
                 minRule = Integer.parseInt( mRulesValidator.getRuleValue(
                                                             iNodeName,
                                                             iPath, "min",
                                                             currentNodeName) );
               //get the max rule and convert to an int
               maxRule = Integer.parseInt( mRulesValidator.getRuleValue(
                                                             iNodeName,
                                                             iPath, "max",
                                                             currentNodeName) );

               if ( (minRule != -1) || (maxRule != -1) )
               {
                    if ( multiplicityUsed >= minRule &&
                         multiplicityUsed <= maxRule )
                    {
                       msgText = "Multiplicity for attribute \"" +
                                  currentNodeName + "\" has been verified";
                       mLogger.info( "PASSED: " + msgText );
                       MessageCollection.getInstance().add( new Message(
                                                            MessageType.PASSED,
                                                            msgText ) );
                     }
                     else
                     {
                        msgText = "The \"" + currentNodeName +
                                  "\" attribute is not within the " +
                                  "multiplicity bounds.";
                        mLogger.info( "FAILED: " + msgText );
                        MessageCollection.getInstance().add( new Message(
                                                          MessageType.FAILED,
                                                          msgText ) );

                        result = false && result;
                     }
                  }

                  //get the spm rule and convert to an int
                  spmRule = Integer.parseInt( mRulesValidator.getRuleValue(
                                                             iNodeName,
                                                             iPath, "spm",
                                                             currentNodeName) );

                  attributeValue = currentAttrNode.getValue();
               }

               // check the contents of the attribute
               if ( dataType.equalsIgnoreCase("idref") )
               {
                  // This is a IDREF data type
               }
               else if ( dataType.equalsIgnoreCase("id") )
               {
                  // This is a ID data type
               }
               else if ( dataType.equalsIgnoreCase("vocabulary") )
               {
                  // This is a VOCAB data type
                  // retrieve the vocab rule values and check against the
                  // vocab values that exist within the test subject

                  msgText = "Testing attribute \"" + currentNodeName +
                            "\" for valid vocabulary";
                  mLogger.info( "INFO: " + msgText );
                  MessageCollection.getInstance().add( new Message(
                                                       MessageType.INFO,
                                                       msgText ) );

                  Vector vocabAttribValues =
                     mRulesValidator.getAttribVocabRuleValues( iNodeName,
                                                               iPath,
                                                               currentNodeName);

                  // we are assuming that only 1 vocabulary value may
                  // exist for an attribute
                  result = checkVocabulary( currentNodeName,
                                            attributeValue,
                                            vocabAttribValues, true )
                                            && result;
               }
               else if ( dataType.equalsIgnoreCase("deprecated") )
               {
                  // This is a deprecated attribute
                  msgText = "The \"" + currentNodeName + "\" attribute " +
                            "has been deprecated.";
                  mLogger.info( "FAILED: " + msgText );
                  MessageCollection.getInstance().add( new Message(
                                                          MessageType.FAILED,
                                                          msgText ) );
                  result = false && result;
               }
               else if ( dataType.equalsIgnoreCase("text") )
               {
                  //This is a TEXT data type
                  // check the attributes for smallest permitted maximum
                  // (spm) conformance.
                  result = checkSPMConformance( currentNodeName,
                                                attributeValue, spmRule, true )
                                                && result;
               }
               else if ( dataType.equalsIgnoreCase("boolean") )
               {
                  //This is a BOOLEAN data type
               }
               else if ( dataType.equalsIgnoreCase("decimal") )
               {
                  //This is a DECIMAL data type
               }
               else if ( dataType.equalsIgnoreCase("integer") )
               {
                  //This is a INTEGER data type
               }
               else if ( dataType.equalsIgnoreCase("duration") ||
                         dataType.equalsIgnoreCase("dateTime") )
               {
                  msgText = "Testing attribute \"" + currentNodeName +
                            "\" for valid format.";
                  mLogger.info( "INFO: " + msgText );
                  MessageCollection.getInstance().add( new Message(
                                                          MessageType.INFO,
                                                          msgText ) );
                  // We can assume that the schema validation has validated
                  // the format.
                  msgText = "The attribute \"" + currentNodeName +
                            "\" represents a valid format.";
                  mLogger.info( "PASSED: " + msgText );
                  MessageCollection.getInstance().add(
                                   new Message( MessageType.PASSED, msgText ) );

                  if ( currentNodeName.equals("attemptExperiencedDurationLimit") ||
                       currentNodeName.equals("activityAbsoluteDurationLimit") ||
                       currentNodeName.equals("activityExperiencedDurationLimit") ||
                       currentNodeName.equals("beginTimeLimit") ||
                       currentNodeName.equals("endTimeLimit") )
                  {

                     // this attribute is out of scope of SCORM
                      msgText = "At this time, SCORM recommends use of " +
                                "the \"" + currentNodeName + "\" attribute " +
                                " of the <limitConditions> element with caution" +
                                " due to the various concerns that have risen" +
                                " dealing with defining requirements on its " +
                                "usage.";
                      mLogger.info( "WARNING: " + msgText );
                      MessageCollection.getInstance().add( new Message(
                                               MessageType.WARNING, msgText ) );

                  }



               }
               else
               {
                  // it is an extension element
               }
            }
      }
      return result;
   }
}
