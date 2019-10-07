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

package org.adl.sequencer;

import java.io.Serializable;

import org.adl.util.debug.DebugIndicator;

/**
 * Encapsulation of information describing an available concurrent auxillary
 * resource<br>
 * 
 * <strong>Filename:</strong> ADLAuxiliaryResource.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>ADLAuxiliaryResource</code> encapsulates the information required
 * by the SCORM RTE 1.3 to identify, provide access to, and request avaliable
 * auxiliary resources.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE 1.3. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * All fields are purposefully public to allow immediate access to known data
 * elements.<br><br>
 * 
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:</strong><br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS 1.0
 *     <li>SCORM 1.3
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class ADLAuxiliaryResource implements Serializable 
{
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * The type of the available auxillary resource.
    */
   public String mType = null;

   /**
    * The resource ID used to deliver the auxillary resource.
    */
   public String mResourceID = null;

   /**
    * Describes the delivery parameter for the auxillary resource.
    */
   public String mParameter = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * This method provides the state this <code>ADLAuxiliaryResource</code> obje
    * diagnostic purposes.
    */
   public void dumpState()
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLAuxiliaryResource --> BEGIN - dumpState");

         System.out.println("  ::--> Type:        " + mType);
         System.out.println("  ::--> Resource ID: " + mResourceID);
         System.out.println("  ::--> Parameter:   " + mParameter);

         System.out.println("  :: ADLAuxiliaryResource --> END   - dumpState");
      }
   }

}  // end ADLAuxiliaryResource
