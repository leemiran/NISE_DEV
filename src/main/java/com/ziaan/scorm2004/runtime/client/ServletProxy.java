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

package com.ziaan.scorm2004.runtime.client;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;

import com.ziaan.scorm2004.runtime.server.*;
import com.ziaan.scorm2004.datamodels.*;
import com.ziaan.scorm2004.datamodels.ieee.*;
import com.ziaan.scorm2004.datamodels.datatypes.*;
import com.ziaan.scorm2004.api.ecmascript.*;
import com.ziaan.scorm2004.runtime.client.*;
import com.ziaan.scorm2004.util.debug.DebugIndicator;
import com.ziaan.scorm2004.datamodels.nav.*;
import com.ziaan.scorm2004.sequencer.*;



/**
 * This class encapsulates communication between the API Adapter applet and
 * the <code>LMSCMIServlet</code>.<br><br> 
 * 
 * <strong>Filename:</strong> ServletProxy<br><br>
 *
 * <strong>Description:</strong><br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE 1.3. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br><br>
 * 
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:</strong><br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class ServletProxy
{
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;


   /**
    * The URL of the target servlet.
    */
   private URL mServletURL = null;


   /**
    * Constructor
    * 
    * @param iURL  The URL of the target servlet.
    */
   public ServletProxy(URL iURL) 
   {
      mServletURL = iURL;
   }

   /**
    * Reads from the LMS server via the <code>LMSCMIServlet</code>; the
    * <code>SCODataManager</code> object containing all of the run-time data
    *  model elements relevant for the current user (student) and current SCO.
    * 
    * @param iRequest A <code>LMSCMIServletRequest</code> object that
    *                 provides all the data neccessary to POST a call to
    *                 the <code>LMSCMIServlet</code>.
    * 
    * @return The <code>LMSCMIServletResponse</code> object provided by the
    *         <code>LMSCMIServlet</code>.
    */
   public LMSCMIServletResponse postLMSRequest(LMSCMIServletRequest iRequest)
   {

      if ( _Debug )
      {
         System.out.println( "In ServletProxy::postLMSRequest()" );
      }

      LMSCMIServletResponse response = new LMSCMIServletResponse();

      try
      {
         if ( _Debug )
         {
            System.out.println("In ServletProxy::postLMSRequest()");
         }

         Serializable[] data = { iRequest};

         if ( _Debug )
         {
            System.out.println("Before postObjects()");
         }

         ObjectInputStream in =
         ServletWriter.postObjects(mServletURL, data);

         if ( _Debug )
         {
            System.out.println( "Back In ServletProxy::postLMSRequest()" );
            System.out.println( "Attempting to read servlet " + 
                                "response now..." );
         }

         response = (LMSCMIServletResponse) in.readObject();

         in.close();
         response.mError = "OK";
      }
      catch ( Exception e )
      {
         if ( _Debug )
         {
            System.out.println("Exception caught in " + 
                               "ServletProxy::postLMSRequest()" );
            System.out.println( e.getMessage() );
         }

         e.printStackTrace();
         response.mError = "FAILED"; 
      }

      return response;
   }

} // ServletProxy
