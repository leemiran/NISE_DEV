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
package com.ziaan.scorm2004.parsers.dom;

// native java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.StandardParserConfiguration;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.ziaan.scorm2004.util.Message;
import com.ziaan.scorm2004.util.MessageCollection;
import com.ziaan.scorm2004.util.MessageType;

/**
 * <strong>Filename: </strong>ADLDOMParser.java<br><br>
 *
 * <strong>Description:<br>
 *               </strong>The <code>ADLDOMParser</code> object interfaces
 *               with the open-source org.apache.xerces.dom.DomParser class to
 *               encapsulate and provide parsing activities  - including
 *               well-formedness and validation against the schema(s) checks.
 *               This object creates a DOM in memory if wellformedness is found.
 *               A DOM object is also created in memory after validation to the
 *               schema(s).<br><br>
 *
 * <strong>Design Issues: </strong>none<br><br>
 *
 * <strong>Implementation Issues: </strong>none<br><br>
 *
 * <strong>Known Problems: </strong>none<br><br>
 *
 * <strong>Side Effects: </strong>Populates the MessageCollection.<br><br>
 *
 * <strong>References: </strong>Apache Xerces<br><br>
 */
public class ADLDOMParser implements ErrorHandler
{

   /**
    * Performs the parsing activities, including the
    * parse for wellformedness and the parse for validation to the controlling
    * document.  This attribute represents an instance of the
    * org.apache.xerces.dom.DomParser class.<br>
    */
   private DOMParser mParser;

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
    * Describes if the XML instance is found to be wellformed by
    * the parser.  The value "false" indicates that the XML instance is not
    * wellformed XML, "true" indicates it is wellformed XML.<br>
    */
   private boolean mIsXMLWellformed;

   /**
    * Describes whether or not the XML instance that is being validated contains
    * extension elements or attributes.  These are extensions that are not
    * expected by SCORM.
    */
   private boolean mExtensionsFound;

   /**
    * This attribute describes if the XML instance is found to be valid against
    * the controlling documents by the parser.  The value "false" indicates that
    * the XML instance is not valid against the controlling documents, "true"
    * indicates that the XML instance is valid against the controlling
    * documents.<br>
    */
   private boolean mIsXMLValidToSchema;

   /**
    * Describes if errors occured during the parse
    * for validation against the controlling documents.  The value "true"
    * indicates that no errors have been thrown during the validation to
    * the controlling document parse, "false" indicates that errors
    * have occured during validation to the controlling document parse.<br>
    */
   private boolean mValidFlag;

   /**
    * Contains the string describing the location of the
    * controlling documents that the parser shall parse against.  The format of
    * this string value shall be identical to the representation of the
    * schemaLocation attribute in the XML declation
    * (ie, [namespace of schema] [location of schema] ).<br>
    */
   private String mSchemaLocation;

   /**
    * Describes whether or not the xsi:schemaLocation attribute was encountered
    * during the processing or not.  This state value is used when processing
    * an XML instance that contains more than one xsi:schemaLocation attribute.
    */
   private boolean mFirstTimeSchemaLocationFound;

   /**
    * Indicates if the parser is in the state of validating for
    * wellformedness or validation against the controlling documents. The value
    * "true" indicates that the parser is validating against the schemas, "false"
    * indicates that it is parsing only for well-formedness.<br>
    */
   private boolean mStateIsValidating;

   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;


   /**
    * Default Constructor.  Sets the attributes to their initial values.<br>
    */
   public ADLDOMParser()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mParser = null;
      mDocument = null;
      mIsXMLWellformed = false;
      mIsXMLValidToSchema = false;
      mSchemaLocation = null;
      mStateIsValidating = false;
      mValidFlag = true;
      mExtensionsFound = false;
      mFirstTimeSchemaLocationFound = true;
   }

   /**
    * The DOMParser allows using a system to hardcode the location of the
    * controlling documents that are to be used during the parse for validation.
    * This method permits the setting of these controlling document locations.
    * <br>
    *
    * @param iSchemaLocation
    *               The schemaLocation string in the exact format as it
    *               would appear in the xsi:schemaLocation attribute of an XML
    *               instance.<br>
    */
   public void setSchemaLocation( String iSchemaLocation )
   {
      mSchemaLocation = iSchemaLocation;
   }

   /**
    * Returns the document created during a parse. A parse for
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
    * Returns the schema location attribute.  This attribute represents the 
    * namespace and location pair(s) found during the processing of an 
    * XML instance.
    */
   public String getSchemaLocation()
   {
      return mSchemaLocation;
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
      return mIsXMLWellformed;
   }

   /**
    * Returns whether or not the XML instance was valid to the
    * schema.  The value "false" indicates that the XML instance is not valid
    * against the controlling documents, "true" indicates that the XML instance
    * is valid against the controlling documents.<br><br>
    *
    * @return boolean - describes if the instance was found to be valid
    *                   against the schema(s).<br>
    */
   public boolean getIsValidToSchema()
   {
      return mIsXMLValidToSchema;
   }

   /**
    * Returns whether or not extensions attributes/elements were found during
    * the processing of an XML instance.  These are extensions that are not
    * expected by SCORM
    */
   public boolean isExtensionsFound()
   {
      return mExtensionsFound;
   }

   /**
    * This method is part of the org.xml.sax.ErrorHandler interface.  The parser
    * calls this method when it wants to generate a warning.  It is an interface
    * that is implemented by the ADLDOMParser.<br>
    *
    * @param Exception object created by the parser.<br>
    */
   public void warning( SAXParseException spe )
   {
      mLogger.entering( "ADLDOMParser", "warning()" );

      // determine what state we are in to set the appropriate flags
      if ( mStateIsValidating )
      {
         mValidFlag = false;
      }
      else
      {
         mIsXMLWellformed = false;
      }

      String msgText = spe.getMessage() + " line: " +
                       spe.getLineNumber() + ", col: " +
                       spe.getColumnNumber();

      mLogger.info( "FAILED: " + msgText );

      MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                        msgText ) );

      mLogger.exiting( "ADLDOMParser", "warning(SAXParseException)" );
   }

   /**
    * Part of the org.xml.sax.ErrorHandler interface.  The parser
    * calls this method when it wants to generate a error.  It is an interface
    * that is implemented by the ADLDOMParser.<br>
    *
    * @param spe    Exception object created by the parser.<br>
    */
   public void  error( SAXParseException spe )
   {
      mLogger.entering( "ADLDOMParser", "error()" );

      // determine what state we are in to set the appropriate flags
      if ( mStateIsValidating )
      {
         mValidFlag = false;
      }
      else
      {
         mIsXMLWellformed = false;
      }

      String msgText = spe.getMessage() + " line: " +
                       spe.getLineNumber() + ", col: " +
                       spe.getColumnNumber();

      mLogger.info( "FAILED: " + msgText );

      MessageCollection.getInstance().add( new Message( MessageType.FAILED,
                                                        msgText ) );

      mLogger.exiting( "ADLDOMParser", "error()" );
   }

   /**
    * Part of the org.xml.sax.ErrorHandler interface.  The parser
    * calls this method when it wants to generate a fatal error.  It is an
    * interface that is implemented by the ADLDOMParser.<br>
    *
    * @param spe    Exception object created by the parser.<br>
    */
   public void fatalError( SAXParseException spe )
   {
      mLogger.entering( "ADLDOMParser", "fatalError()" );

      // determine what state we are in to set the appropriate flags
      if ( mStateIsValidating )
      {
         mValidFlag = false;
      }
      else
      {
         mIsXMLWellformed = false;
      }

      String msgText = spe.getMessage()  + " line: " +
                       spe.getLineNumber() + ", col: " +
                       spe.getColumnNumber() ;

      mLogger.info( "FAILED: " + msgText );

      MessageCollection.getInstance().add( new Message ( MessageType.FAILED,
                                                         msgText ) );

      mLogger.exiting( "ADLDOMParser", "fatalError()" );
   }

   /**
    * Configures the parser by setting the appopropriate properties
    * and features of the DOMParser object. Such properties and features
    * include configuring a wellformedness parser or a validation parser,
    * setting schema locations, turning on/off namespaces, etc.  This method
    * must be called prior to any attempts to parse.  <br>
    *
    */
   private void configureParser()
   {
     mLogger.entering( "ADLDOMParser", "configureParser()" );

     // instantiate the Xerces DOMParser object to use the standard
     // configuration
     mParser = new DOMParser(new StandardParserConfiguration()  );

     if (mParser != null)
     {
        // check to see if we are configuring a validating or non-validating
        //parser
        if ( mStateIsValidating )  // configuring a validating parser
        {
           if ( mSchemaLocation != null )
           {
              try
              {
                 mParser.setFeature("http://xml.org/sax/features/validation",
                                     true);
                 mParser.setFeature(
                         "http://apache.org/xml/features/validation/schema",
                          true);
                 mParser.setFeature(
                         "http://xml.org/sax/features/namespaces",
                          true);
                 mParser.setFeature("http://apache.org/xml/features/continue-after-fatal-error",
                                    false );

                 mLogger.finest( "setting schemas to - " + mSchemaLocation );

                 mParser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
                                      mSchemaLocation );


                 mParser.setErrorHandler( (ErrorHandler)this );
              }
              catch ( SAXException se )
              {
                 String msgText = " SAXException thrown when configuring" +
                                  " validing parser";
                 mLogger.severe( msgText );
                 MessageCollection.getInstance().add( new Message (
                                                       MessageType.TERMINATE,
                                                       msgText ) );
              }
           }
           else
           {
              String msgText = "Schema Location(s) not set";
              mLogger.severe( "SEVERE: " + msgText );

              mValidFlag = false;
           }
        }
        else  // configuring a non-validating parser
        {
           try
           {
              mParser.setFeature("http://xml.org/sax/features/validation",
                                  false);
              mParser.setFeature("http://xml.org/sax/features/namespaces",
                                  true);
              mParser.setFeature("http://apache.org/xml/features/continue-after-fatal-error",
                                 false );

              mParser.setErrorHandler( (ErrorHandler)this );
           }
           catch (SAXException se )
           {
              String msgText = "SAXException thrown when configuring" +
                              " non-validating parser";

              mLogger.severe("TERMINATE: " + msgText );
              MessageCollection.getInstance().add( new Message (
                                                          MessageType.TERMINATE,
                                                          msgText ) );
           }
        }
     }
     else
     {
        String msgText = "Instantiation of parser returned null";
        mLogger.severe( "TERMINATE: " + msgText );
        MessageCollection.getInstance().add( new Message (
                                                          MessageType.TERMINATE,
                                                          msgText ) );
     }
     mLogger.exiting( "ADLDOMParser", "configureParser()" );
   }


   /**
    * This method performs the non-validating parse - parsing for well-formed
    * XML only.<br>
    *
    * @param iXMLFileName - the xml document to be parsed for wellformedness in
    * the form of a string<br>
    *
    * @param iLogRelated - boolean determining whether or not the parse
    * should log messages.  True implies log messages, false implies otherwise.
    * Messages do not need to be logged when parsing to create a document for
    * system operation purposes only.<br>
    *
    * @return void<br>
    */
   public void parseForWellformedness( String iXMLFileName, boolean iLogRelated )
   {
      mLogger.entering( "ADLDOMParser", "parseForWellformedness()" );
      mLogger.finest( "   iXMLFileName coming in is " + iXMLFileName );
      mLogger.finest( "   iLogRelated coming in is " + iLogRelated );

      configureParser();

      Document wellformednessDocument = null;
      String msgText;

      String fileName = searchFile( iXMLFileName, "xml" );

      //Gets the current directory from the xml file path
      File tempXMLfile = new File(fileName);
      String xsdLocation = "file:///" + tempXMLfile.getParent() + "/";
      xsdLocation = xsdLocation.replaceAll( " ", "%20");
      xsdLocation = xsdLocation.replace( '\\', '/');

      if ( fileName != "" )
      {
         if ( mParser != null )
         {
            try
            {
               msgText = "Validating for Well-Formedness";
               mLogger.info( msgText );

               if ( iLogRelated )
               {
                  MessageCollection.getInstance().add( new Message (
                                                               MessageType.INFO,
                                                               msgText ) );
               }

               mParser.parse( setUpInputSource( fileName ) );
               msgText = "The instance is well-formed ";
               mLogger.info( msgText );

               if ( iLogRelated )
               {
                  MessageCollection.getInstance().add( new Message (
                                                             MessageType.PASSED,
                                                             msgText ) );
               }
               wellformednessDocument = mParser.getDocument();

               if ( wellformednessDocument != null )
               {
                  mIsXMLWellformed = true;
               }
            }
            catch ( SAXException se )
            {
               msgText = "SAXException thrown during non-validating parse";
               mLogger.severe( msgText );
            }
            catch ( IOException ioe )
            {
               msgText = "IOException thrown during non-validating parse";
               mLogger.severe( msgText );

            }
         }
      }

      // assign the appropriate value to the document attribute
      setDocumentAttribute( wellformednessDocument, iLogRelated, iXMLFileName );
      mLogger.finest("FINAL SCHEMA LOCATION:  " + mSchemaLocation);

      mLogger.finest( "wellformed = " + getIsWellformed() );

      mLogger.exiting( "ADLDOMParser", "parseForWellformedness()" );
   }

   /**
    * Performs the non-validating parse - parsing for well-formed
    * XML only.<br>
    *
    * @param iXMLFileName - the xml document to be parsed for wellformedness in
    * the form of a URL <bR>
    *
    * @param iLogRelated - boolean determining whether or not the parse<br>
    * should log messages.  True implies log messages, false implies otherwise.
    * Messages do not need to be logged when parsing to create a dom for
    * system operation purposes only.<br>
    *
    * @return void<br>
    *
    */
   public void parseForWellformedness( java.net.URL iXMLFileName,
                                       boolean iLogRelated )
   {
      mLogger.entering( "ADLDOMParser", "parseForWellformedness()" );
      mLogger.finest( "   iXMLFileName coming in is " + iXMLFileName );
      mLogger.finest( "   iLogRelated coming in is " + iLogRelated );

      configureParser();

      Document wellformednessDocument = null;
      String msgText;

      if ( mParser != null )
      {
         try
         {
            msgText = "Validating for Well-Formedness";
            mLogger.info( msgText );

            if ( iLogRelated )
            {
               MessageCollection.getInstance().add( new Message (
                                                            MessageType.INFO,
                                                            msgText ) );
            }

            //gets an input source from the URL passed in.
            mParser.parse(new InputSource( iXMLFileName.openStream()));

            msgText = "The instance is well-formed ";
            mLogger.info( msgText );

            if ( iLogRelated )
            {
               MessageCollection.getInstance().add( new Message (
                                                          MessageType.PASSED,
                                                          msgText ) );
            }
            wellformednessDocument = mParser.getDocument();



            if ( wellformednessDocument != null )
            {
               mIsXMLWellformed = true;
            }
         }
         catch ( SAXException se )
         {
            msgText = "SAXException thrown during non-validating parse";
            mLogger.severe( msgText );
         }
         catch ( IOException ioe )
         {
            msgText = "IOException thrown during non-validating parse";
            mLogger.severe( msgText );
         }
      }
      else
      {
            // parser is null, can not continue wellformedness
            //mWellnessFlag = false;
      }

      // assign the appropriate value to the document attribute
      setDocumentAttribute( wellformednessDocument, iLogRelated,
                            iXMLFileName.toString() );

      mLogger.finest( "wellformed = " + getIsWellformed() );

      mLogger.exiting( "ADLDOMParser", "parseForWellformedness()" );
   }

   /**
    * Performs the validating parse - parsing for well-formed
    * XML and validation to the controlling documents.<br>
    *
    * @param iXMLFileName - the xml document to be parsed for validation
    * against the controlling documents.<br>
    *
    * @return void<br>
    */
   public void parseForValidation( String iXMLFileName )
   {
      mLogger.entering( "ADLDOMParser", "parseForValidation()" );
      mLogger.finest( "   iXMLFileName coming in is " + iXMLFileName );

      configureParser();

      Document wellformednessDocument = null;
      Document validityDocument = null;
      String msgText;

      String fileName = searchFile( iXMLFileName, "xml" );

      //Gets the current directory from the xml file path
      File tempXMLfile = new File(fileName);

      if ( fileName != "" )
      {
         if ( mParser != null )
         {
            try
            {
               msgText = "Parsing for Well-Formedness first to obtain document";
               mLogger.info( msgText );

               mParser.parse( setUpInputSource( fileName ) );
               wellformednessDocument = mParser.getDocument();

               if ( wellformednessDocument != null )
               {
                  mIsXMLWellformed = true;
               }
            }
            catch ( SAXException se )
            {
               msgText = "SAXException thrown during non-validating parse";
               mLogger.severe( msgText );
            }
            catch ( IOException ioe )
            {
               msgText = "IOException thrown during non-validating parse";
               mLogger.severe( msgText );

            }
            catch ( NullPointerException npe )
            {
               msgText = "NullPointerException thrown during non-validating parse";
               mLogger.severe( msgText );

            }
            if ( mIsXMLWellformed )
            {
               //we have wellformed XML, now parsing for validity to xsd(s)
               mStateIsValidating = true;

               configureParser();

               if ( mParser != null )
               {
                  msgText = "Validating against controlling documents";
                  mLogger.info( msgText );
                  MessageCollection.getInstance().add( new Message (
                                                               MessageType.INFO,
                                                               msgText ) );

                  if ( mSchemaLocation == null )
                  {
                     /// schema locations have not been set
                     mValidFlag = false;
                  }
                  else
                  {
                     try
                     {
                        mParser.parse( setUpInputSource( fileName ) );

                        validityDocument = mParser.getDocument();
                     }
                     catch ( SAXException se )
                     {
                        msgText = "SAXException thrown during validating" +
                                  " parse";
                        mLogger.severe( msgText );
                     }
                     catch ( IOException ioe )
                     {
                        msgText = "IOException thrown during" +
                                  " validating parse";
                        mLogger.severe( msgText );
                     }
                     catch ( NullPointerException npe )
                     {
                        msgText = "NullPointerException thrown during non-validating parse";
                        mLogger.severe( msgText );

                     }
                  }
               }
               else
               {
                  mValidFlag = false;
               }
            }
            else
            {
               //instance is not well-formed, therefore, validity not checked
               //ensure that the mValidFlag is set to false;
               msgText = "Istance is not wellformed, cannot continue with validation parse";
               mLogger.finest( msgText );

               mValidFlag = false;
            }
         }
         else
         {
             //parser is null, can not continue parse for validation
             mValidFlag = false;
         }
      }
      else
      {
         // error in file name
         mValidFlag = false;
      }

     // assign the apporopriate value to the mIsXMLValidToSchema attribute
     setValidXMLToSchemaAttribute();

     mLogger.finest( "valid = " + getIsValidToSchema() );

     if ( getIsValidToSchema() )
     {
        msgText = "The instance is valid against the controlling documents ";
        mLogger.info( msgText );
        MessageCollection.getInstance().add( new Message ( MessageType.PASSED,
                                                            msgText ) );
     }

     mLogger.exiting( "ADLDOMParser", "parseForValidation()" );
   }


   /**
    * Traverses the DOM Tree and removes Ignorable Whitespace Text Nodes and
    * Comment Text.  The function also removes extension elements and
    * attributes that are not defined by SCORM.  If extensions are found
    * the function also sets a flag stating that extensions are present in the
    * input XML instance.<br>
    *
    * @param iNode he node to be pruned of whitespace and comments<br>
    */
    private void pruneTree ( Node iNode, String iXMLFileName )
    {
       String value;

       // is there anything to do?
       if ( iNode == null )
       {
          return;
       }

       switch ( iNode.getNodeType() )
       {
          case Node.PROCESSING_INSTRUCTION_NODE:
          {
             break;
          }
          case Node.DOCUMENT_NODE:
          {
             pruneTree( ((Document)iNode).getDocumentElement(), iXMLFileName );
             break;
          }
          case Node.ELEMENT_NODE:
          {
             mLogger.finest("*******************************************************");
             mLogger.finest("Processing Element Node: [" + iNode.getLocalName() + "]");

             checkForSchemaLocations(iNode, iXMLFileName);

             // Get the list of attributes of the element
             NamedNodeMap attrList = iNode.getAttributes();

             // Loop over the attributes for this element, remove any attributes
             // that are extensions
             mLogger.finest("Processing " + attrList.getLength() + " attributes" );
             for ( int i = 0; i < attrList.getLength(); i++ )
             {
                Attr currentAttribute = (Attr)attrList.item(i);

                if ( !(DOMTreeUtility.isSCORMAppProfileNode( currentAttribute,
                                                            iNode ) ) )
                {
                   mLogger.finest("Extension attribute, removing: [" +
                                 currentAttribute.getNamespaceURI() + "] " +
                                 currentAttribute.getLocalName() +
                                 " from the its parent node [" +
                                 iNode.getNamespaceURI() + "] " +
                                 iNode.getLocalName());

                   // Remove the Element Node from the DOM
                   attrList.removeNamedItemNS( currentAttribute.
                             getNamespaceURI(),currentAttribute.getLocalName());
                   i--;
                   mExtensionsFound = true;
                }
                else
                {
                   mLogger.finest( "Valid SCORM attribute, keeping attribute: [" +
                                  currentAttribute.getNamespaceURI() + "] " +
                                  currentAttribute.getLocalName());
                }
             } 

             mLogger.finest( "Done processing attributes for node: [" +
                             iNode.getNamespaceURI() + "] " + 
                             iNode.getLocalName() );
             mLogger.finest("*******************************************************");

             // Done looping over the attributes for this element, now loop over
             // the set of children nodes.

             mLogger.finest("");
             mLogger.finest("*******************************************************");
             mLogger.finest( "Processing direct-descendances for node: [" +
                            iNode.getNamespaceURI() + "] " +
                            iNode.getLocalName() );

             String nodeName = iNode.getLocalName();
             NodeList children = iNode.getChildNodes();
             if ( children != null )
             {
                // Loop over set of children elements for this element, remove
                // any elements that are extensions
                mLogger.finest("Processing " + children.getLength() + " elements" );
                for ( int z = 0; z < children.getLength(); z++ )
                {
                   Node childNode = children.item(z);

                   if (childNode.getNodeType() == Node.ELEMENT_NODE)
                   {
                      mLogger.finest( "Processing element: [" + childNode + "]");
                      mLogger.finest( "Elements Namespace: [" +
                                    childNode.getNamespaceURI() + "]");
                      mLogger.finest( "Elements Parent Node: [" +
                                    iNode.getLocalName() + "]");
                      mLogger.finest( "Parent Nodes Namespace: [" +
                                    iNode.getNamespaceURI() + "]");

                      if ( !(DOMTreeUtility.isSCORMAppProfileNode(
                         children.item(z), children.item(z).getParentNode() ) ) )
                      {
                         // Before we remove the element see if the elemen
                         // contains any xsi:schemaLocations.  We need
                         // to add them to the list of schema locations for 
                         // parsing
                         checkForSchemaLocations(childNode,iXMLFileName);

                         mLogger.finest("Extension Element Found, removing element from DOM Tree");

                         // Remove the Element Node from the DOM
                         children.item(z).getParentNode().removeChild(children.item(z));
                         z--;
                         mExtensionsFound = true;
                      }
                      else
                      {
                         mLogger.finest("ADL SCORM Element Found, leaving element in DOM Tree");
                         pruneTree(children.item(z), iXMLFileName);
                      }
                   } // end if NodeType == ELEMENT_NODE

                if ( childNode instanceof TextImpl )
                {
                   value = new String( children.item(z).getNodeValue().trim());

                   if ( ((TextImpl)children.item(z)).isIgnorableWhitespace() )
                   {
                      iNode.removeChild( children.item(z) );
                      z--;
                   }
                   else if ( value.length() == 0 )
                   {
                      iNode.removeChild( children.item(z) );
                      z--;
                   }
                }
                else if ( children.item(z).getNodeType() == Node.COMMENT_NODE )
                {
                   iNode.removeChild( children.item(z) );
                   z--;
                }
             } // end looping over children nodes
          } // end if there are children

          mLogger.finest( "Done processing direct-descendants for node: [" +
                          iNode.getNamespaceURI() + "] " +
                          iNode.getLocalName() );
          mLogger.finest("*******************************************************");

          break;

          }

          // handle entity reference nodes
       case Node.ENTITY_REFERENCE_NODE:
          {

             NodeList children = iNode.getChildNodes();
             if ( children != null )
             {
                int len = children.getLength();
                for ( int i = 0; i < len; i++ )
                {
                   pruneTree( children.item(i), iXMLFileName );
                }
             }
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
    }

    /**
     *  This method addes the input schema location value to the set of 
     *  schema locations to be used by the parser during a validating parse.
     * 
     * @param iSchemaLocation The schema location (namespace and location)
     * @param iXMLFileName
     */
    public void addSchemaLocationToList( String iSchemaLocation,
                                         String iXMLFileName )
    {
      mLogger.entering("ADLDOMParser","addSchemaLocationToList()");
      mLogger.finest("Schema Location: " + iSchemaLocation);

      String fileName = searchFile( iXMLFileName, "xml" );

      //Gets the current directory from the xml file path
      File tempXMLfile = new File(fileName);
      String xsdLocation = "file:///" + tempXMLfile.getParent() + "/";
      xsdLocation = xsdLocation.replaceAll( " ", "%20");
      xsdLocation = xsdLocation.replace( '\\', '/');
      String schemaStr = "";

      StringTokenizer st = new StringTokenizer(iSchemaLocation, " ");

      // Builds the schema list
      while ( st.hasMoreTokens() )
      {
         // get the namespace part of the schema location
         String namespace = st.nextToken();

         // check to see if the namespace is already in the set of schema
         // locations.  If so, no need to add the namespace or location
         // to the set of namespaces.
         if ( !isSchemaLocationPresent(namespace) )
         {
            schemaStr += namespace + " ";

            if ( st.hasMoreTokens() )
            {
               String location = st.nextToken();

               if ( (location.substring(0,5).equals("http:")) ||
                    (location.substring(0,6).equals("https:")) ||
                    (location.substring(0,4).equals("ftp:")) ||
                    (location.substring(0,5).equals("ftps:")) )
               {
                  // No need to append the XSD location (xsdLocation) to the
                  // schema location
                  schemaStr = schemaStr + location + " ";
               }
               else
               {
                  // The schema location is not an external URL, append the 
                  // local XSD location (xsdLocation) to the schema location 
                  schemaStr = schemaStr + xsdLocation + location + " ";
               }
            }
         } // end if isSchemaLocationPresent
         else
         {
            // advance to the next token.  This will place the curser on the
            // location of the namespace we just processed.
            st.nextToken();
         }
      } // end looping over tokens

      if ( mSchemaLocation == null )
      {
         mSchemaLocation = schemaStr;
      }
      else
      {
         mSchemaLocation += schemaStr;
      }
    }

   /**
    * Prints all needed information for a given DOM node,
    * including the node type, node name, and node value.<br>
    *
    * @param nodeTypeString
    *               type of node to print<br>
    *
    * @param node   DOM node to print<br>
    */
   protected void printNodeInfo( String  nodeTypeString,
                                 Node  node )
   {
      StringBuffer  typeStr = new StringBuffer( "(null)" );
      StringBuffer  nodeType = new StringBuffer( "(null)" );
      StringBuffer  nodeName = new StringBuffer( "(null)" );
      StringBuffer  nodeValue = new StringBuffer( "(null)" );
      String        sp = new String( " -- " );

      if ( node != null )
      {
         if ( nodeTypeString != null )
         {
            typeStr = new StringBuffer( nodeTypeString );
         }

         nodeType = new StringBuffer(
                                 new Integer( node.getNodeType() ).toString() );

         if ( node.getNodeName() != null )
         {
            nodeName = new StringBuffer( node.getNodeName() );
         }

         if ( node.getNodeValue() != null )
         {
            nodeValue = new StringBuffer( node.getNodeValue() );
         }
      }
      while ( typeStr.length() < 42 )
      {
         typeStr.append( " " );
      }
      while ( nodeName.length() < 15 )
      {
         nodeName.append( " " );
      }
      while ( nodeValue.length() < 10 )
      {
         nodeValue.append( " " );
      }

         mLogger.finest( typeStr + sp + nodeType + sp +
                             nodeName + sp + nodeValue );

   }

    /**
     * A seperate document object is created for a wellformedness parse and a
     * parse for validation against the controlling documents.  The validity
     * document contains inserts of the the default element/attribute
     * values as defined in the XSDs.  The wellformedness document contains the
     * elements/attributes as they appear in the XML instance.  For this reason,
     * the wellformedness document is set, if the XML was found to be wellformed,
     * and used during the Application Profiles checks.<br>
     *
     * @param wellformednessDoc
     *               DOM created after wellformedness parse.<br>
     * @param validityDoc
     *               DOM created after validation to schema parse.<br>
     */
    private void setDocumentAttribute( Document wellformednessDoc,
                                       boolean iLogRelated,
                                       String iXMLFileName )
    {
       mLogger.entering( "ADLDOMParser", "setDocumentAttribute()" );

       if ( mIsXMLWellformed )
       {
          if ( iLogRelated )
          {
             pruneTree( wellformednessDoc, iXMLFileName );
          }

          mDocument = wellformednessDoc;
       }
       //else mDocument shall remain = null

       mLogger.exiting( "ADLDOMParser", "setDocumentAttribute()" );
    }

   /**
    * Sets the mIsXMLValidToSchema attribute to it's final value.
    * This is necessary to work around the problem with Xerces now throwing
    * an exception when the schema location can not be determined.<br>
    */
   private void setValidXMLToSchemaAttribute()
   {
      mLogger.entering( "ADLDOMParser", "setValidXMLSchemaAttribute()" );

      mIsXMLValidToSchema = mValidFlag;

      mLogger.exiting( "ADLDOMParser", "setValidXMLSchemaAttribute()" );
   }


   /**
    * This method sets up the input source for the test subject file.<br>
    *
    * @param fileName -
    *        name of the file we are are setting up the input source for.<br>
    *
    * @return  InputSource<br>
    */
   private InputSource setUpInputSource(String iFileName)
   {
      mLogger.entering( "ADLDOMParser", "setUpInputSource()" );

      InputSource is = new InputSource();
      is = setupFileSource(iFileName);

      mLogger.exiting( "ADLDOMParser", "setUpInputSource()" );
      return is;
   }

   /**
    * Sets up the file source for the test subject file.<br>
    *
    * @param iFileName file to setup input source for.<br>
    *
    * @return InputSource<br>
    */
   private InputSource setupFileSource( String iFileName)
   {
      mLogger.entering( "ADLDOMParser", "setupFileSource()" );
      String msgText;
      boolean isUnicode = false;

      try
      {
         File xmlFile = new File( iFileName );
         mLogger.info( xmlFile.getAbsolutePath() );

         if ( xmlFile.isFile() )
         {
            InputSource is;

            FileReader fr = new FileReader( xmlFile );
            isUnicode = false;
            if (xmlFile.length() > 1)
            {
               //Reads the first two bytes of the file to determine if it is unicode
               char[] cbuff = new char[2];
               fr.read(cbuff, 0, cbuff.length);

               //If the unicode file header is found this set flag for unicode
               if (cbuff[0] == (char)0xFF && cbuff[1] == (char)0xFE)
               {
                  isUnicode = true;
               }
            }
            fr.close();

            if (isUnicode == false)
            {   //Reads in ASCII file.
               fr = new FileReader( xmlFile );
               is = new InputSource(fr);
            }
            else
            {  //Reads in a unicode file

               //Creates a buffer with the size of the xml unicode file
               BufferedReader in =  new BufferedReader(new InputStreamReader(
                                new
               FileInputStream(xmlFile),"utf-16"), (int)xmlFile.length() + 1);
               StringBuffer dataString = new StringBuffer();
               String s = "";

               //Builds the unicode file to be parsed
               while ((s = in.readLine()) != null)
               {
                  dataString.append(s);
               }

               in.close();
               is = new InputSource(new StringReader(dataString.toString()));
            }

            is.setEncoding("UTF-16");
            return is;
         }
         else if ( ( iFileName.length() > 6 ) &&
                   ( iFileName.substring(0,5).equals("http:") ||
                    iFileName.substring(0,6).equals("https:") ) )
         {
            URL xmlURL = new URL( iFileName );
            InputStream xmlIS = xmlURL.openStream();
            InputSource is = new InputSource(xmlIS);
            return is;
         }
         else
         {
            msgText = "XML File: " + iFileName + " is not a file or URL";
            mLogger.severe( msgText );
         }
      }
      catch ( NullPointerException  npe )
      {
         msgText = "Null pointer exception" + npe;
         mLogger.severe( msgText );
      }
      catch ( SecurityException se )
      {
         msgText = "Security Exception" + se;
         mLogger.severe( msgText );
      }
      catch ( FileNotFoundException fnfe )
      {
         msgText = "File Not Found Exception" + fnfe;
         mLogger.severe( msgText );
      }
      catch ( Exception e )
      {
         msgText = "General Exception" + e;
         mLogger.severe( msgText );
      }

      mLogger.exiting( "ADLDOMParser", "setUpFileSource()" );

      return new InputSource();
   }

   /**
    * Searches for the full path of the specified file and returns
    * it's string name.<br>
    *
    * @param iFileName Name of the file to be checked.<br>
    * @param iFileType File extension of this file. (ie, java, xml).<br>
    *
    * @return The full absolute path and filename of the given file.<br>
    */
   private String searchFile( String iFileName,
                              String iFileType )
   {
      mLogger.entering( "ADLDOMParser", "searchFile()" );
      mLogger.finest( "   iFileName coming in is: " + iFileName );
      mLogger.finest( "   iFileType coming in is: " + iFileType );

      boolean fileFound = false;
      String outFileName = new String("");

      //check to make sure the file exists then assign the full path and name to
      //outFileName to be returned
      try
      {
         if ( iFileName.length() > 6 &&
              (iFileName.substring(0,5).equals("http:") ||
               iFileName.substring(0,6).equals("https:")) )
         {
            outFileName = iFileName;
         }
         else
         {
            File inFile = new File( iFileName );

            if ( inFile.isFile() == true )
            {
               fileFound = true;
               outFileName = inFile.getAbsolutePath();
            }
            else
            {
               String msgText = "File Not A Normal File";
               mLogger.severe( msgText );
            }
         }
      }
      catch ( NullPointerException npe )
      {
         String msgText = "File is Empty";
         mLogger.severe( msgText );
      }
      catch ( SecurityException se )
      {
         String msgText = "File Not Accessible";
         mLogger.severe( msgText );
      }
      mLogger.exiting( "ADLDOMParser", "searchFile()" );

      return outFileName;
   }

   /**
    * This method checks to see if the node that is being processed contains
    * an xsi:schemaLocation attribute.  If it does, the method adds the name
    * value of the attribute to the list of schemaLocations.
    * 
    * @param Node The node that is being processed
    */
   private void checkForSchemaLocations(Node iNode, String iXMLFileName)
   {
      mLogger.entering("ADLDOMParser","checkForSchemaLocations()");
      mLogger.finest("Processing Node: [" + iNode.getLocalName() + "]");
      mLogger.finest("Processing Nodes Namespace: [" + iNode.getNamespaceURI() +
                      "]");

      // Get the list of attributes of the element
      NamedNodeMap attrList = iNode.getAttributes();

      // Loop over the attributes for this element, remove any attributes
      // that are extensions
      mLogger.finest("Processing " + attrList.getLength() + " attributes" );
      for ( int i = 0; i < attrList.getLength(); i++ )
      {
         Attr currentAttribute = (Attr)attrList.item(i);

         mLogger.finest( "Processing attribute [" + i + "]: [" +
                                currentAttribute.getLocalName() + "]");
         mLogger.finest( "Attributes Namespace [" + i + "]: [" +
                               currentAttribute.getNamespaceURI() + "]");
         mLogger.finest( "Attributes Parent Node [" + i + "]: [" +
                               iNode.getLocalName() + "]");
         mLogger.finest( "Parent Nodes Namespace [" + i +
                               "]: [" + iNode.getNamespaceURI() + "]");

         // If one of the attributes is xsi:schemaLocation, then
         // save off the namespace and the schema location.  These
         // will be used later during the validation process
         if (currentAttribute.getLocalName().equals("schemaLocation") &&
             currentAttribute.getNamespaceURI().
                          equals("http://www.w3.org/2001/XMLSchema-instance") )
         {
            // A schema location has been defined in the XML, don't use the defaults
            if (mFirstTimeSchemaLocationFound)
            {
               // Don't use the default schema locations
               mSchemaLocation = null;
               mFirstTimeSchemaLocationFound = false;
            }

            addSchemaLocationToList( currentAttribute.getValue(),iXMLFileName );
         }
      } // end looping over attributes

      mLogger.exiting( "ADLDOMParser", "checkForSchemaLocations()" );
   }

   /**
    * This method determine inf the input namespace has already been accounted
    * for in the schema location listing.  If it has, then there is no reason
    * to add it to the set of schema locations
    * 
    * @param iNamespace The namespace we are looking for
    * 
    * @return Returns whether or not the namespace has already been accounted
    * for.
    */
   private boolean isSchemaLocationPresent(String iNamespace)
   {
      boolean result = false;

      if ( mSchemaLocation != null )
      {
         StringTokenizer st = new StringTokenizer(mSchemaLocation, " ");

         // Builds the schema list
         while ( st.hasMoreTokens() )
         {
            // get the token
            String token = st.nextToken();

            if ( token.equals(iNamespace) )
            {
               result = true;
               break;
            }
         }
      }

      return result;
   }
}
