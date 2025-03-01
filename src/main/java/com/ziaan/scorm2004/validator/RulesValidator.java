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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <strong>Filename: </strong>RulesValidator.java<br><br>
 *
 * <strong>Description: </strong>The <code>RulesValidator</code>
 * object contains the information required for the SCORM Validation
 * Process performed by the SCORM Validator.  This object
 * will contain the DOM of the XML rules that are neccessary for
 * meeting conformance to each of the Application Profiles.<br><br>
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
public class RulesValidator
{
   /**
   * The DOM of the application profile rules defined in xml.<br>
   */
   private Document mRulesDocument;

   /**
    * The validator type:<br>
    *  - "contentpackage"<br>
    *  - "metadata"<br>
    *  - "sequence"<br>
    */
   private String mValidatorType;

   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;


   /**
    * Default constructor - inializes the attributes for this class.<br>
    *
    * @param iValidatorType The validator that is currently being worked.<br>
    *    -"contentpackage"<br>
    *    -"metadata"<br>
    *    -"sequence"<br>
    */
   public RulesValidator( String iValidatorType )
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mLogger.entering( "RulesValidator",
                        "RulesValidator()" );

      mRulesDocument = null;
      mValidatorType = iValidatorType;

      mLogger.exiting( "RulesValidator",
                       "RulesValidator()" );
   }

   /**
    * Creates a RulesManager to produce a DOM of the rules for the specified
    * Metadata/Content Package Application Profile.<br>
    *
    * @param iApplicationProfileType
    *               The application profile rules document to be read in<br>
    *               -"asset"<br>
    *               -"sco"<br>
    *               -"sca"<br>
    *               -"activity"<br>
    *               -"contentaggregation"<br>
    *               -"resource"<br>
    *               -"sequence"<br>
    */
   public boolean readInRules( String iApplicationProfileType,
                               String iEnvironmentVariable )
   {
     mLogger.entering( "RulesValidator", "readInRules()" );
     mLogger.finest( "      iApplicationProfileType coming in is " +
                            iApplicationProfileType );

     boolean result = true;

     // create an DOMRules object to provide the dom of rules
     DOMRulesCreator dom = new DOMRulesCreator( iApplicationProfileType,
                                                mValidatorType );

     Document rules = dom.provideRules( iEnvironmentVariable );

     if ( rules != null )
     {
        mRulesDocument = rules;
        mLogger.finest( "root element mRulesDoc = " +
                     ((Node)mRulesDocument.getDocumentElement()).getNodeName());
     }
     else
     {
        mLogger.severe( "Problem parsing XML rules" );
        result = false;
     }
     mLogger.exiting( "RulesValidator", "readInRules()" );
     return result;
   }

   /**
    * Retrieves the element node's value for the specified string name
    * at the specified path location.<br>
    *
    * @param iElementName Name of the element being searched for<br>
    *
    * @param iPath  Path of the element being searched for
    * (necessary for elements with same names but different values)<br>
    *
    * @param iRuleName Name of the rule being retrieved<br>
    *                  -"min"<br>
    *                  -" max"<br>
    *                  -"spm"<br>
    *                  -"attrib"<br>
    *
    * @param iAttribAttribName Name of the attribute rule being retrieved<br>
    *
    * @return String: Value of specified rule for the specified element with the
    * specified path.<br>
    */
   public String getRuleValue( String iElementName,
                               String iPath,
                               String iRuleName,
                               String iAttribAttribName )
   {
      mLogger.entering( "RulesValidator", "getRuleValue()" );
      mLogger.finest( "    iElementName coming in is " + iElementName );
      mLogger.finest( "    iPath coming in is " + iPath );
      mLogger.finest( "    iRuleName coming in is " + iRuleName );
      mLogger.finest( "    iAttribAttribName coming in is " + iAttribAttribName );

      String stringResult = new String("-1");
      Node ruleNode;
      String ruleNodeName;

      if ( mRulesDocument != null )
      {
         mLogger.finest( "mRulesDocument != null" );

         ruleNode = retrieveAttribRuleElement( iElementName, iPath, iRuleName,
                                               iAttribAttribName );

         if (ruleNode != null )
         {
            // retrieve value and return
            mLogger.finest( "rule found, retrieving value" );

            stringResult = getTaggedData( ruleNode );
         }
         else
         {
            mLogger.finest( "ruleNode is null - rull was not found" );
         }
      }
      else
      {
         mLogger.severe("Can not continue validation of rules, doc is null");
      }
         mLogger.finest( "Returning the following value " + stringResult );
         mLogger.exiting( "RulesValidator", "getRuleValue()" );

      return stringResult;
   }

   /**
    * Retrieves the element node's value for the specified string name
    * at the specified path location.<br>
    *
    * @param iElementName Name of the element being searched for<br>
    *
    * @param iPath  Path of the element being searched for
    * (necessary for elements with same names but different values)<br>
    *
    * @param iRuleName Name of the rule being retrieved<br>
    *                  - "min"<br>
    *                  - " max"<br>
    *                  - "spm"<br>
    *                  - "attrib"<br>
    *
    * @return Specified rule value of the specified element with the
    * specified path.<br>
    */
   public String getRuleValue( String iElementName,
                               String iPath,
                               String iRuleName )
   {
      mLogger.entering( "RulesValidator", "getRuleValue()" );

      mLogger.finest( "    iElementName coming in is " + iElementName );
      mLogger.finest( "    iPath coming in is " + iPath );
      mLogger.finest( "    iRuleName coming in is " + iRuleName );

      String stringResult = new String("-1");
      Node ruleNode;
      String ruleNodeName;

      if ( mRulesDocument != null )
      {
         mLogger.finest( "mRulesDocument != null" );

         ruleNode = retrieveRuleElement( iElementName, iPath, iRuleName );
         if (ruleNode != null )
         {
            // retrieve value and return
            mLogger.finest( "rule found, retrieving value" );

            stringResult = getTaggedData( ruleNode );
         }
         else
         {
            mLogger.finest( "ruleNode is null - rull was not found" );
         }
      }
      else
      {
         mLogger.severe("Can not continue validation of rules, doc is null");
      }
         mLogger.finest( "Returning the following value " + stringResult );
         mLogger.exiting( "RulesValidator", "getRuleValue()" );

      return stringResult;
   }

   /**
    *
    * Retrieves the vocabulary element for the specified element at the
    * at the specified path location.<br>
    *
    * @param iElementName Name of the element being searched for <br>
    *
    * @param iPath  Path of the element being searched for
    * (necessary for elements with same names but different values)<br>
    *
    * @return A Vector of vocabulary string values for the specified rule at
    * the specified element with the specified path.<br>
    *
    */
   public Vector getVocabRuleValues( String iElementName,
                                     String iPath )
   {
      mLogger.entering( "RulesValidator", "getVocabRuleValues()" );

      mLogger.finest( "    iElementName coming in is " + iElementName );
      mLogger.finest( "    iPath coming in is " + iPath );

      Vector ruleNodeVector = new Vector();
      Vector vocabVector = new Vector();

      if ( mRulesDocument != null )
      {
         mLogger.finest( "mRulesDocument != null" );

         ruleNodeVector = retrieveVocabRuleElements( iElementName, iPath,
                                                     "vocab" );

         int numVocabularies = ruleNodeVector.size();

         if ( numVocabularies != 0 )
         {
            // loop through vocabulary nodes to retrieve value and add them
            // to the string vector
            for ( int i = 0; i < numVocabularies; i++ )
            {
               vocabVector.add(getTaggedData(
                                (Node)(ruleNodeVector.elementAt(i)) ));
            }
         }
         else
         {
            mLogger.finest( "ruleNode is null - rules not found" );
         }
      }
      else
      {
         mLogger.severe("Can not continue validation of rules, doc is null");
      }
        mLogger.exiting( "RulesValidator", "getVocabRuleValues()" );

     return vocabVector;
   }


   /**
    * Retrieves the vocabulary element for the specified element at the
    * at the specified path location.<br>
    *
    * @param iElementName Name of the element being searched for <br>
    *
    * @param iPath  Path of the element being searched for
    * (necessary for elements with same names but different values)<br>
    *
    * @param iAttribName Name of the attribute being tested<br>
    *
    * @return Vector:  Vector containing the vocabulary string values for the
    * specified rule at the specified element with the specified path.<br>
    */
   public Vector getAttribVocabRuleValues( String iElementName,
                                           String iPath,
                                           String iAttribName )
   {
      mLogger.entering( "RulesValidator", "getAttribVocabRuleValues()" );
      mLogger.finest( "    iElementName coming in is " + iElementName );
      mLogger.finest( "    iPath coming in is " + iPath );
      mLogger.finest( "    iAttribName coming in is " + iAttribName );

      Vector ruleNodeVector;
      Vector vocabVector = new Vector();

      if ( mRulesDocument != null )
      {
         mLogger.finest( "mRulesDocument != null" );

         ruleNodeVector = retrieveAttribVocabRuleElements( iElementName,
                                                          iPath,
                                                          "vocab",
                                                          iAttribName );
         int numVocabularies = ruleNodeVector.size();

         if ( numVocabularies != 0 )
         {
            // loop through vocabulary nodes to retrieve value and add them
            // to the string vector
            for ( int i = 0; i < numVocabularies; i++ )
            {
               vocabVector.add(getTaggedData(
                                 (Node)(ruleNodeVector.elementAt(i)) ));
            }
         }
         else
         {
            mLogger.finest( "ruleNode is null - rules not found" );
         }
      }
      else
      {
         mLogger.severe("Can not continue validation of rules, doc is null");
      }
      mLogger.exiting( "RulesValidator", "getAttribVocabRuleValues()" );

     return vocabVector;
   }

   /**
    * Retrieves the actual attrib rule node being searched for.<br>
    *
    * @param iElementName Name of the element we are looking for rules for<br>
    *
    * @param iPath Path of the element we are looking for<br>
    *
    * @param iRuleName Rule we are looking for<br>
    *
    * @param iAttribName Name of the attrib element we are looking for<br>
    *
    * @return Node:  attrib's rule element being searched for<br>
    */
   private Node retrieveAttribRuleElement( String iElementName,
                                           String iPath,
                                           String iRuleName,
                                           String iAttribName )
   {
      mLogger.entering( "RulesValidator", "retrieveAttribRuleElement()" );
      Node resultNode = null;

      Node rootRulesNode = ( (Node)mRulesDocument.getDocumentElement() );
      NodeList kids = rootRulesNode.getChildNodes();
      int numKids = kids.getLength();

      Node currentElementNode;
      String nodeName;
      String path;

      for ( int i = 0; i < numKids; i++ )
      {
         // traverse the element nodes and find the one with the correct
         // name and path attribute value
         currentElementNode = kids.item(i);
         nodeName = getAttribute( currentElementNode, "name" );
         mLogger.finest( "currentElementNode's name is " + nodeName );

         path = getAttribute( currentElementNode, "path" );
         mLogger.finest( "currentElementNode's path is " + path );

         if ( nodeName.equals( iElementName ) && path.equals( iPath ) )
         {
            // traverse the child nodes and find the one with the attribute
            // elements
            NodeList currentElementNodeKids = currentElementNode.getChildNodes();
            int numCurrentNodeKids = currentElementNodeKids.getLength();
            mLogger.finest( "numCurrentNodeKids is " + numCurrentNodeKids );

            Node attributeNode;
            String attributeNodeName;
            for (int j = 0; j < numCurrentNodeKids; j++ )
            {
               // traverse the attribute nodes and find the one with
               // the name that we want
               attributeNode = currentElementNodeKids.item(j);
               attributeNodeName = attributeNode.getNodeName();
               mLogger.finest( "attributeNodeName is " + attributeNodeName );

               if ( attributeNodeName.equals( "attrib" ) )
               {
                  String attributeName = getAttribute( attributeNode, "name" );

                  if ( attributeName.equals(iAttribName) )
                  {
                     mLogger.finest("Attribute rule found for " + attributeName);

                     // traverse the children of the specified attribute and
                     // get the specified rule
                     NodeList attributeNodeKids = attributeNode.getChildNodes();
                     int numAttributeNodeKids = attributeNodeKids.getLength();

                     Node ruleNode;
                     String ruleNodeName;

                     for ( int k = 0; k < numAttributeNodeKids; k++ )
                     {
                        ruleNode = attributeNodeKids.item(k);
                        ruleNodeName = ruleNode.getNodeName();
                        mLogger.finest( "Found the " + ruleNodeName +
                                        " attribute");

                        if ( ruleNodeName.equals(iRuleName) )
                        {
                           mLogger.finest( "Found the " + iRuleName + " rule");
                           resultNode = ruleNode;
                           break;
                        }
                     }

                     break;
                  }
               }
            }

            break;
         }
      }
      mLogger.exiting( "RulesValidator", "retrieveAttribRuleElement()" );

      return resultNode;
   }


   /**
    * Retrieves a Vector of the vocabulary rules for the attribute element
    * being searched for. <br>
    *
    * @param iElementName Name of the element we are looking for rules for <br>
    *
    * @param iPath Path of the element we are looking for <br>
    *
    * @param iRuleName Rule being tested <br>
    *
    * @param iAttribName Name of the attrib element we are looking for <br>
    *
    * @return Vector: containing vocabulary for the attrib element being
    * searched for<br>
    */
   private Vector retrieveAttribVocabRuleElements( String iElementName,
                                                   String iPath,
                                                   String iRuleName,
                                                   String iAttribName )
   {
      mLogger.entering( "RulesValidator", "retrieveAttribVocabRuleElements()" );

      Vector resultVector = new Vector();

      Node rootRulesNode = ( (Node)mRulesDocument.getDocumentElement() );
      NodeList kids = rootRulesNode.getChildNodes();
      int numKids = kids.getLength();

      Node currentElementNode;
      String nodeName;
      String path;

      for ( int i = 0; i < numKids; i++ )
      {
         // traverse the element nodes and find the one with the correct
         // name and path attribute value
         currentElementNode = kids.item(i);
         nodeName = getAttribute( currentElementNode, "name" );
         mLogger.finest( "currentElementNode's name is " + nodeName );

         path = getAttribute( currentElementNode, "path" );
         mLogger.finest( "currentElementNode's path is " + path );

         if ( nodeName.equals( iElementName ) && path.equals( iPath ) )
         {
            // traverse the child nodes and find the one with the attribute
            // elements
            NodeList currentElementNodeKids = currentElementNode.getChildNodes();
            int numCurrentNodeKids = currentElementNodeKids.getLength();
            mLogger.finest( "numCurrentNodeKids is " + numCurrentNodeKids );

            Node attributeNode;
            String attributeNodeName;
            for (int j = 0; j < numCurrentNodeKids; j++ )
            {
               // traverse the attribute nodes and find the one with
               // the name that we want
               attributeNode = currentElementNodeKids.item(j);
               attributeNodeName = attributeNode.getNodeName();
               mLogger.info( "attributeNodeName is " + attributeNodeName );

               if ( attributeNodeName.equals("attrib") )
               {
                  String attributeName = getAttribute( attributeNode, "name" );

                  if ( attributeName.equals(iAttribName) )
                  {
                     mLogger.finest( "Attribute rule found for "
                                     + attributeName );

                     // traverse the children of the specified attribute and
                     // get the specified rule
                     NodeList attributeNodeKids = attributeNode.getChildNodes();
                     int numAttributeNodeKids = attributeNodeKids.getLength();

                     Node ruleNode;
                     String ruleNodeName;

                     for ( int k = 0; k < numAttributeNodeKids; k++ )
                     {
                        ruleNode = attributeNodeKids.item(k);
                        ruleNodeName = ruleNode.getNodeName();
                        mLogger.finest( "Found the " + ruleNodeName +
                                        " attribute" );

                        if ( ruleNodeName.equals(iRuleName) &&
                             iRuleName.equals( "vocab" ) )
                        {
                           mLogger.finest( "Found the " + iRuleName + " rule" );
                           resultVector.add( ruleNode );
                        }
                     }

                     break;
                  }
               }
            }

            break;
         }
      }
      mLogger.exiting( "RulesValidator", "retrieveAttribVocabRuleElements()" );

      return resultVector;
   }

   /**
    * Retrieves the actual rule node being searched for. <br>
    *
    * @param iElementName Name of the element we are looking for rules for<br>
    *
    * @param iPath Path of the element we are looking for<br>
    *
    * @param iRuleName Rule we are looking for<br>
    *
    * @return Node The node containing the element being searched for<br>
    */
   private Node retrieveRuleElement( String iElementName,
                                     String iPath,
                                     String iRuleName )
   {
      mLogger.entering( "RulesValidator", "retrieveRuleElement()" );

      Node resultNode = null;

      Node rootRulesNode = ( (Node)mRulesDocument.getDocumentElement() );
      NodeList kids = rootRulesNode.getChildNodes();
      int numKids = kids.getLength();

      Node currentElementNode;
      String nodeName;
      String path;

      for ( int i = 0; i < numKids; i++ )
      {
         // traverse the element nodes and find the one with the correct
         // name and path attribute value
         currentElementNode = kids.item(i);
         nodeName = getAttribute( currentElementNode, "name" );
         mLogger.finest( "currentElementNode's name is " + nodeName );

         path = getAttribute( currentElementNode, "path" );
         mLogger.finest( "currentElementNode's path is " + path );

         if ( nodeName.equals( iElementName ) && path.equals( iPath ) )
         {
            // traverse the child nodes and find the one with the attribute
            // elements
            NodeList currentElementNodeKids = currentElementNode.getChildNodes();
            int numCurrentNodeKids = currentElementNodeKids.getLength();
            mLogger.finest( "numCurrentNodeKids is " + numCurrentNodeKids );

            Node ruleNode;
            String ruleNodeName;
            for (int j = 0; j < numCurrentNodeKids; j++ )
            {
               // traverse the attribute nodes and find the one with
               // the name that we want
               ruleNode = currentElementNodeKids.item(j);
               ruleNodeName = ruleNode.getNodeName();
               mLogger.finest( "ruleNodeName is " + ruleNodeName );

               if ( iRuleName.equals( "min" ) || iRuleName.equals( "max" ) ||
                    iRuleName.equals( "spm" ) || iRuleName.equals( "datatype" ))
               {
                  if ( ruleNodeName.equals( iRuleName ) )
                  {
                     resultNode = ruleNode;
                     break;
                  }
               }
               else
               {
                  mLogger.severe("The rule searched for is not available");
               }
            }
            break;
         }
      }
      mLogger.exiting( "RulesValidator", "retrieveRuleElement()" );

      return resultNode;
   }

   /**
    * Retrieves a vector of the vocabulary rule node being searched for.<br>
    *
    * @param iElementName Name of the element we are looking for rules for<br>
    *
    * @param iPath Path of the element we are looking for<br>
    *
    * @param iRuleName Rule we are looking for<br>
    *
    * @return Vector Containing the valid vocabularies for the element being
    * tested<br>
    */
   private Vector retrieveVocabRuleElements( String iElementName,
                                             String iPath,
                                             String iRuleName )
   {
      mLogger.entering( "RulesValidator", "retrieveVocabRuleElements()" );

      Vector resultVector = new Vector();

      Node rootRulesNode = ( (Node)mRulesDocument.getDocumentElement() );
      NodeList kids = rootRulesNode.getChildNodes();
      int numKids = kids.getLength();

      Node currentElementNode;
      String nodeName;
      String path;

      for ( int i = 0; i < numKids; i++ )
      {
         // traverse the element nodes and find the one with the correct
         // name and path attribute value
         currentElementNode = kids.item(i);
         nodeName = getAttribute( currentElementNode, "name" );
         mLogger.finer( "currentElementNode's name is " + nodeName );

         path = getAttribute( currentElementNode, "path" );
         mLogger.finer( "currentElementNode's path is " + path );

         if ( nodeName.equals( iElementName ) && path.equals( iPath ) )
         {
            // traverse the child nodes and find the one with the attribute
            // elements
            NodeList currentElementNodeKids = currentElementNode.getChildNodes();
            int numCurrentNodeKids = currentElementNodeKids.getLength();
            mLogger.finer( "numCurrentNodeKids is " + numCurrentNodeKids );

            Node ruleNode;
            String ruleNodeName;
            for (int j = 0; j < numCurrentNodeKids; j++ )
            {
               // traverse the child nodes and find the one with
               // the name that we want
               ruleNode = currentElementNodeKids.item(j);
               ruleNodeName = ruleNode.getNodeName();
               mLogger.finer( "ruleNodeName is " + ruleNodeName );

               if ( iRuleName.equals("vocab") )
               {
                  if ( ruleNodeName.equals( iRuleName ) )
                  {
                     resultVector.add( ruleNode );
                  }
               }
            }
            break;
         }
      }
      mLogger.exiting( "RulesValidator", "retrieveVocabRuleElements()" );

      return resultVector;
   }

   /**
    * Retrieves the value of the desired attribute.<br>
    *
    * @param theNode Node which contains the attributes.<br>
    *
    * @param theAttribute Name of the attribute desired<br>
    *
    * @return String: Value of the desired attribute<br>
    */
   protected String getAttribute( Node theNode, String theAttribute)
   {
      String returnValue = new String();

      // grab attributes of the node
      Attr attrs[] = sortAttributes( theNode.getAttributes() );

      // now see if the asked for attribute exists and send
      // back the value
      Attr attribute;
      for ( int i = 0; i < attrs.length; i++ )
      {
         attribute = attrs[i];

         //if ( attribute.getName().equals( theAttribute ) )
         if ( attribute.getLocalName().equals( theAttribute ) )
         {
            returnValue = attribute.getValue();
            break;
         }
      }
      return returnValue;
   }

   /**
    * Sorts the elements attributes.<br>
    *
    * @param attrs list of attributes to be sorted<br>
    *
    * @return sorted array of attributes<br>
    */
   protected Attr[] sortAttributes(NamedNodeMap iAttrs)
   {
      int len = (iAttrs != null) ? iAttrs.getLength() : 0;
      Attr array[] = new Attr[len];
      for ( int i = 0; i < len; i++ )
      {
         array[i] = (Attr)iAttrs.item(i);
      }
      for ( int i = 0; i < len - 1; i++ )
      {
         String name  = array[i].getLocalName();
         int    index = i;
         for ( int j = i + 1; j < len; j++ )
         {
            String curName = array[j].getLocalName();
            if ( curName.compareTo(name) < 0 )
            {
               name  = curName;
               index = j;
            }
         }
         if ( index != i )
         {
            Attr temp    = array[i];
            array[i]     = array[index];
            array[index] = temp;
         }
      }

      return(array);
   }

   /**
    * Retrieves the text from a text node<br>
    *
    * @param node   TEXT_NODE that contains the needed text<br>
    *
    * @return    String: text contained in the given node<br>
    */
   public String getTaggedData( Node iNode )
   {
      mLogger.entering( "RulesValidator", "getTaggedData()" );

      String value= new String();
      NodeList kids = iNode.getChildNodes();

      //cycle through all children of node to get the text
      if ( kids != null )
      {
         for ( int i = 0; i < kids.getLength(); i++ )
         {
            //make sure this is a "text node"
            if ( ( kids.item(i).getNodeType() == Node.TEXT_NODE ) ||
                 ( kids.item(i).getNodeType() == Node.CDATA_SECTION_NODE ) )
            {
               value = value + kids.item(i).getNodeValue().trim();
            }
         }
      } else
      {
            mLogger.finest( "%%% no kids for value %%%" );

      }
      mLogger.exiting( "RulesValidator", "getTaggedData()" );

      return value;
   }

   /**
    * Retrieves the application profile that the xml rules describe.<br>
    *
    * @return String: The name of the application profile being tested<br>
    *
    */
   public String getApplicationProfile()
   {
      return getAttribute( (Node)mRulesDocument.getDocumentElement(),
                           "appprof" );
   }

   /**
    * Retrieves the type of XML subject the xml rules describe.<br>
    *
    * @return String containing the type of the test subject<br>
    *    ( "metadata" || "imsmanifest" || "sequence" )<br>
    */
   public String getType()
   {
      return getAttribute( (Node)mRulesDocument.getDocumentElement(),
                           "type" );
   }
}
