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
import java.util.Hashtable;
import java.util.Vector;

/**
 * Encapsulation of information required for delivery.<br><br>
 * 
 * <strong>Filename:</strong> ADLValidRequests.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>ADLUIState</code> encapsulates the information required by the
 * SCORM RTE 1.3 delivery system to determine which navigation UI controls
 * should be enabled on for the current launched activity.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE 1.3.<br>
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
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */ 
public class ADLValidRequests implements Serializable
{

   /**
    * Should a 'Continue' button be enabled during delivery of the current
    * activity.
    */
   public boolean mContinue = false;

   /**
    * Should a 'Continue' button be enabled during delivery of the current
    * activity that triggers an Exit navigation request.
    */
   public boolean mContinueExit = false;

   /**
    * Should a 'Previous' button be enabled during the delivery of the
    * current activity.
    */
   public boolean mPrevious = false;

   /**
    * Indictates if the sequencing session has begun and a 'SuspendAll'
    * navigation request is valid.
    */
   public boolean mSuspend = false;

   /**
    * Set of valid targets for a choice navigation request
    */
   public Hashtable mChoice = null;

   /**
    * The currently valid table of contents (list of <code>ADLTOC</code>) to be
    * provided during the current activity.
    */
   public Vector mTOC = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/  

   /**
    * This method compares two <code>ADLValidRequests</code> objects for 
    * equality.
    * 
    * @param iState The <code>ADLValidRequests</code> object being compared to 
    *               this object.
    * 
    * @return <code>true</code> if the iState is equal to this object,
    *         otherwise <code>false</code>.
    */
   public boolean equals(ADLValidRequests iState)
   {

      boolean result = true;

      result = result && (mContinue == iState.mContinue);
      result = result && (mContinueExit == iState.mContinueExit);
      result = result && (mPrevious == iState.mPrevious);

      if ( mChoice != null )
      {
         if ( iState.mChoice != null )
         {
            String val1 = mChoice.toString();
            String val2 = iState.mChoice.toString();

            result = result && (val1.equals(val2));
         }
         else
         {
            result = false;
         }
      }
      else
      {
         result = result && (iState.mChoice == null);
      }

      if ( mTOC != null )
      {
         if ( iState.mTOC != null )
         {
            String val1 = mTOC.toString();
            String val2 = iState.mTOC.toString();

            result = result && (val1.equals(val2));
         }
         else
         {
            result = false;
         }
      }
      else
      {
         result = result && (iState.mTOC == null);
      }

      return result;
   }


   /**
    * This method provides the state this <code>ADLUIState</code> object for
    * diagnostic purposes.<br><br>
    *
    * NOTE: The table of contents (TOC) is not provided with this method.  For
    * a dump of the current TOC, call the code>dumpTOC</code> method of on the
    * <code>ADLSeqUtilities</code> class.
    * 
    * @see <code>ADLSeqUtilities</code>
    */
/*
   public void dumpState()
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLValidRequests   --> BEGIN - dumpState");

         System.out.println("  ::--> Continue      : " + mContinue);
         System.out.println("  ::--> Continue Exit : " + mContinueExit);
         System.out.println("  ::--> Previous      : " + mPrevious);

         if ( mTOC != null )
         {
            System.out.println("  ::--> TOC:           YES");
            ADLSeqUtilities.dumpTOC(mTOC);
         }
         else
         {
            System.out.println("  ::--> TOC:           NO");
         }

         System.out.println("  :: ADLValidRequests    --> END   - dumpState");
      }
   }
*/

}  // end ADLValidRequests
