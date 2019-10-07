
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
import java.io.Serializable;
import java.util.logging.*;

// xerces imports

// adl imports

/**
 *
 * <strong>Filename: </strong><br>XSDHandler.java<br><br>
 *
 * <strong>Description: </strong><br>The <CODE>XSDHandler </CODE>object handles
 * XSD manipulation, such as cononical compare of XSDs and required files check.
 * The XSDHandler class will produce a DOM of the provided XSD at the root of
 * the PIF as well as a DOM of the ADL owned XSDs.  The DOM's will then be
 * cononically compared .  The only difference allowed between the two DOM trees
 * is pointing to an external verses local copy of the ims_xml.xsd schema
 * imported by the IMS CP XSD.<br><br>
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
public class XSDHandler implements Serializable
{
   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;

   /**
    * The Application Profile of the Content Package test subject in question
    * Valid values include:
    *  -"resource"<br>
    *  -"contentaggregation" <br>
    */
   private String mApplicationProfileType;

   /**
    * The location that the content package test subject can be found.<br>
    */
   private String mDirectory;


   /**
    * The constructor.
    */
   public XSDHandler( String iDirectory,
                      String iApplicationProfile)
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mApplicationProfileType = iApplicationProfile;
      mDirectory = iDirectory;
   }

  /**
   * Performs the high-level content package checks, such that the
   * imsmanifest.xml file along with all required schemas exist
   * at the root of the package.<br>
   *
   * @return - boolean result that shows if schemas referenced in the root
   * manifest declation as well as the imsmanifest file existed at the
   * root of the package.  True implies that the schemas and manifest were
   * located at the root of the pif, false otherwise.<br>
   */
    private boolean checkForRequiredFiles()
    {
       return true;
       //move here from CPValidator
    }

  /**
   * Performs the cononical compare between the DOM of the provided XSD at the
   * found at the root of the package and the DOM of the ADL owned XSD.  The
   * only difference allowed between the two DOM trees is pointing to an
   * external verses local copy of the ims_xml.xsd imported schema. The DOM
   * structures are provided by wellformedness parsing with the ADLDOMParser.
   * <br>
   *
   * @param iDirectory - The location that the content package test subject
   *                   resides<br>
   * @param iApplicationProfile - The content package application profile
   * type being validated (resource/contentaggregation)
   *
   * @return - boolean result that shows if the two doms were compared
   * successfully, false implies modifications to the schemas at the root of the
   * package were detected.<br>
   */
    private boolean cononicalCompare()
    {
       return true;
    }




}
