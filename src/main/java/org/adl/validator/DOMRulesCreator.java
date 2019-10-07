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
package org.adl.validator;

// native java imports
import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.jar.*;
import java.net.URL.*;

// xerces imports
import org.apache.xerces.parsers.*;
import org.w3c.dom.*;

// adl imports
import org.adl.parsers.dom.*;
import org.adl.util.*;

/**
 *
 * <strong>Filename: </strong>DOMRulesCreator.java<br><br>
 *
 * <strong>Description: </strong>The <code>DOMRulesCreator</code> will create
 * a DOM of the XML rules that are neccessary for each Application Profile
 * (ie.,  Content Package Validator - resource and content aggregation /
 * Metadata Validator - asset, sco, sca, activity, content aggregation ) /
 * Sequence Validator - sequence<br><br>
 *
 * <strong>Design Issues: </strong>none<br><br>
 *
 * <strong>Implementation Issues: </strong>none<br><br>
 *
 * <strong>Known Problems: </strong>none<br><br>
 *
 * <strong>Side Effects: </strong>none<br><br>
 *
 * <strong>References: </strong>SCORM <br><br>
 *
 * @author ADL Technical Team
 */
public class DOMRulesCreator implements Serializable
{
   /**
    *
    * The application profile type to read the rules for the following:<br>
    *
    *   <strong>Metadata</strong><br>
    *   - "asset"<br>
    *   - "sco"<br>
    *   - "content aggregation"<br>
    *   - "sca"<br>
    *   - "activity"<br>
    *
    *   <strong>Content Package</strong><br>
    *   - "resource"<br>
    *   - "content aggregation"<br>
    *
    *   <strong>Sequence</strong><br>
    *   - "sequence"<br>
    *
    */
   private String mApplicationProfileType;

   /**
    * The validator type that this class is providing for the following:<br>
    *   - metadata<br>
    *   - contentpackage<br>
    *   - sequence<br>
    */
   private String mValidatorType;

   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;

   /**
    *
    * Constructor that sets the application profile and validator attributes
    * values.<br>
    *
    * @param iApplicationProfileType Application Profile Rules to be retrieved.
    * <br>
    * @param iValidatorType Type of validator being used.  Valid values
    * include: contentpackage, metadata, sequence <br>
    *
    */
   public DOMRulesCreator( String iApplicationProfileType,
                           String iValidatorType )
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mLogger.entering( "DOMRulesCreator", "DOMRulesCreator()" );
      mLogger.info("      iApplicationProfileType coming in is " +
                           iApplicationProfileType );
      mLogger.info("      iValidatorType coming in is " +
                           iValidatorType );

      mApplicationProfileType = iApplicationProfileType;
      mValidatorType = iValidatorType;

      mLogger.exiting( "DOMRulesCreator", "DOMRulesCreator()" );
   }

   /**
    *
    * Performs the reading in and parsing of the xml rules.<br>
    *
    * @param Environment Variable used to locate the rules.<br>
    * @return Document DOM of the parsed xml rules<br>
    */
   public Document provideRules( String iEnvironmentVariable )
   {
      mLogger.entering( "DOMRulesCreator", "provideRules()" );

      // create an ADLDOMParser object to parse the rules and provide a dom
      ADLDOMParser mParser = new ADLDOMParser();
      java.net.URL urlLocation = null;
      Document doc = null;
      String validatorDirectory = iEnvironmentVariable +
                                  File.separator + "AppProfileXMLRules";


      // now we must determine which XML rules document it's location
      if ( mValidatorType.equals("metadata") &&
           mApplicationProfileType.equals("asset") )
      {
         urlLocation = DOMRulesCreator.class.getResource("metadata/rules/md_assetRules.xml");

         mLogger.info( "asset fileLocation is" + urlLocation );
      }
      else if ( mValidatorType.equals("metadata") &&
                mApplicationProfileType.equals("sco") )
      {
         urlLocation = DOMRulesCreator.class.getResource("metadata/rules/md_scoRules.xml");

         mLogger.info( "sco fileLocation is" + urlLocation );
      }
      else if( mValidatorType.equals("metadata") &&
               mApplicationProfileType.equals("contentorganization") )
      {
         urlLocation = DOMRulesCreator.class.getResource("metadata/rules/md_contentorganizationRules.xml");

         mLogger.info( "contentorganization fileLocation is" + urlLocation );
      }
      else if( mValidatorType.equals("metadata") &&
               mApplicationProfileType.equals("contentaggregation") )
      {
         urlLocation = DOMRulesCreator.class.getResource("metadata/rules/md_contentaggregationRules.xml");

         mLogger.info( "contentorganization fileLocation is" + urlLocation );
      }
      else if( mValidatorType.equals("metadata") &&
               mApplicationProfileType.equals("activity") )
      {
    urlLocation = DOMRulesCreator.class.getResource("metadata/rules/md_activityRules.xml");

         mLogger.info( "activity fileLocation is" + urlLocation );
      }
      else if ( mValidatorType.equals("contentpackage") &&
                mApplicationProfileType.equals("resource") )
      {
         urlLocation = DOMRulesCreator.class.getResource("contentpackage/rules/cp_resourceRules.xml");

         mLogger.info( "resource fileLocation is" + urlLocation );
      }
      else if ( mValidatorType.equals("contentpackage") &&
                mApplicationProfileType.equals("contentaggregation") )
      {
         urlLocation = DOMRulesCreator.class.getResource("contentpackage/rules/cp_contentaggregationRules.xml");

         mLogger.info( "contentaggregation fileLocation is" + urlLocation );
      }
      else if ( mValidatorType.equals("sequence") &&
                mApplicationProfileType.equals("sequence") )
      {
    urlLocation = DOMRulesCreator.class.getResource("sequence/rules/sequenceRules.xml");

         mLogger.info( "sequence fileLocation is" + urlLocation );
      }
      else
      {
         mLogger.severe( "Error, ApplicationProfile and/or ValidatorType DNE" );
      }

      if ( urlLocation != null )
      {
         // parse XML rules document to provide a dom
         mParser.parseForWellformedness( urlLocation, false );

         if( mParser.getIsWellformed() )
         {
            doc = mParser.getDocument();
         }
      }
      mLogger.exiting( "DOMRulesCreator", "provideRules()" );

      return doc;
    }
}
