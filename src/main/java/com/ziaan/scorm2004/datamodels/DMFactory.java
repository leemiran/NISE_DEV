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

package com.ziaan.scorm2004.datamodels;

import com.ziaan.scorm2004.datamodels.ieee.SCORM_2004_DM;
import com.ziaan.scorm2004.datamodels.nav.SCORM_2004_NAV_DM;


/**
 * <br><br>
 * 
 * <strong>Filename:</strong> DMFactory.java<br><br>
 * 
 * <strong>Description:</strong><br><br>
 * 
 * <strong>Design Issues:</strong><br><br>
 * 
 * <strong>Implementation Issues:</strong><br><br>
 * 
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:<strong><br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>SCORM 1.2
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class DMFactory
{

   /**
    * Enumeration of the run-time data model's supported by the SCORM<br>
    * <br>Unknown
    * <br><b>-1</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int DM_UNKNOWN                =   -1;


   /**
    * Enumeration of the run-time data model's supported by the SCORM<br>
    * <br>SCORM 1.2 Data Model
    * <br><b>0</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
//   public final static int DM_SCORM_1_2              =    0;


   /**
    * Enumeration of the run-time data model's supported by the SCORM<br>
    * <br>SCORM 2004 Data Model
    * <br><b>1</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int DM_SCORM_2004             =    1;


   /**
    * Enumeration of the run-time data model's supported by the SCORM<br>
    * <br>SCORM 2004 Navigation Data Model
    * <br><b>1</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int DM_SCORM_NAV              =    2;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   public static DataModel createDM(int iType)
   {
      DataModel dm = null;

      switch ( iType )
      {
      case DM_SCORM_2004:

         dm = new SCORM_2004_DM();
         break;

      case DM_SCORM_NAV:

         dm = new SCORM_2004_NAV_DM();
         break;

      default:
         // Do nothing -- this is an error
         ;
      }

      return dm;
   }

} // end DMFactory
