
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

package com.ziaan.scorm2004.api.ecmascript;

/**
 * Provides an interface for a SCO to communicate with the LMS.  This API is
 * defined in the SCORM 2004 Run-Time Environment Book<br><br>.
 *
 * <strong>Filename:</strong> SCORM1.2_API_Interface.java<br><br>
 *
 * <strong>Description:</strong><br>
 * This interface represents the API Interface for SCO to LMS communication as
 * defined in the SCORM 2004.  This class contains all method signatures
 * that are available for SCORM 2004 Conformant SCOs.<br><br>
 *
 * <strong>Design Issues:</strong><br>
 * <br>
 *
 * <strong>Implementation Issues:</strong><br>
 * It is the responsibility of the implementation of this interface to
 * perform any and all LMS behaviors including but not limited to error code
 * management as defined in the SCORM 2004.<br><br>
 *
 * <strong>Known Problems:</strong><br><br>
 *
 * <strong>Side Effects:</strong><br><br>
 *
 * <strong>References:</strong><br>
 * <ul>
 *     <li>SCORM 2004
 *     <li>Note to Rob - Put in the AICC API doc reference
 * </ul>
 *
 * @author ADL Technical Team
 */
public interface SCORM13APIInterface
{

   /**
    *  The function is used to initiate the communication session.  It allows
    *  the LMS to handle LMS specific initialization issues.
    *
    * @param iParam ("") empty characterstring.  An empty characterstring shall
    * be passed as a parameter.
    *
    * @return The function can return one of two values.  The return value shall
    * be represented as a characterstring.  The quotes ("") are not part of the
    * characterstring returned, they are used purely to set off the values
    * returned.<br>
    *    <ul>
    *   <li>   <code>true</code> - The characterstring "true" shall be returned
    *           if communication session initialization,as determined by the LMS,
    *           was successful.</li>
    *     <li> <code>false</code> - The characterstring "false" shall be returned
    *           if communication session initialization, as determined by the LMS,
    *           was unsuccessful.  The API Instance shall set the error code to a
    *           value specific to the error encountered.  The SCO may call
    *           <code>GetLastError()</code> to determine the type of error.
    *           More detailed information pertaining to the error may be provided
    *           by the LMS through the <code>GetDiagnostic()</code> function.</li>
    *      </ul>
    */
   public String Initialize( String iParam );

   /**
    *  The function is used to terminate the communication session.  It is used by
    *  the SCO when the SCO has determined that it no longer needs to communicate
    *  with the LMS. The Terminiate() function also shall cause the persistence of
    *  any data (i.e., an implicit Commit("") call) set by the SCO since the last
    *  successful call to <code>Initialize("")</code> or <code>Commit("")</code>,
    *  whichever occurred most recently.  This guarantees to the SCO that all data
    *  set by the SCO has been persisted by the LMS. Once the communication
    *  session has been successfully terminated, the SCO is only permitted to
    *  call the Support Methods.
    *
    * @param iParam ("") - empty characterstring.  An empty characterstring shall
    * be passed as a parameter.
    *
    * @return The method can return one of two values.  The return value shall be
    * represented as a characterstring.
    *    The quotes ("") are not part of the characterstring returned, they are used
    *    purely to set off the values returned.
    *    <ul>
    *       <li>  <code>true</code> - The characterstring "true" shall be
    *               if termination of the communication session,
    *               as determined by the LMS, was successful. </li>
    *       <li>  <code>false</code> - The characterstring "false" shall be
    *               returned if termination of the communication session,
    *               as determined by the LMS, was unsuccessful.  The API
    *               Instance shall set the error code to a value
    *               specific to the error encountered.  The SCO may call
    *               <code>GetLastError()</code> to determine the type of error.
    *          More detailed information pertaining to the error may be
    *               provided by the LMS through the
    *               <code>GetDiagnostic()</code> function. </li>
    *    </ul>
    */
   public String Terminate( String iParam );

   /**
    *   The function requests information from an LMS.  It permits the SCO to
    *   request information from the LMS to determine among other things:
    *    <ul>
    *      <li>   Values for data model elements supported by the LMS. </li>
    *      <li>   Version of the data model supported by the LMS. </li>
    *      <li>   Whether or not specific data model elements are supported. </li>
    *    </ul>
    *
    * @param iElement The parameter represents the complete identification of
    *    a data model element within a data model.
    *
    * @return The method can return one of two values.  The return value shall
    *    be represented as a characterstring.
    *       <ul>
    *         <li>    A characterstring containing the value associated with the
    *               parameter </li>
    *         <li>    If an error occurs, then the API Insance shall set an error
    *                code to a value specific to the error and return an empty
    *                characterstring (""). The SCO may call <code>GetLastError()
    *                 </code> to determine the type of error.  More detailed
    *            information pertaining to the error may be provided by
    *                the LMS through the
    *        <code>GetDiagnostic()</code> function. </li>
    *        </ul>
    */
   public String GetValue( String iDataModelElement );

   /**
    *  The method is used to request the transfer to the LMS of the value
    *  of parameter_2 for the data element specified as parameter_1.
    *  This method allows the SCO to send information to the LMS for storage.
    *  The API Instance may be designed to immediately persist data that was
    *  set (to the server-side component) or store data in a local
    *  (client-side) cache.
    *
    * @param iElement
    *    <ul>
    *     <li>  parameter_1 - The complete identification of a data model
    *            element within a data model to be set. </li>
    *
    *     <li> parameter_2 - The value to which the contents of parameter_1
    *           is to be set.  The value of parameter_2 shall be a characterstring
    *           that shall be convertible to the data type defined for the data
    *        model element identified in parameter_1. <li>
    *    </ul>
    *
    * @return The method can return one of two values.  The return value shall be
    *         represented as a characterstring.  The quotes ("") are not part of
    *         the characterstring returned, they are used purely to set
    *         off the values returned.
    *        <ul>
    *          <li>  <code>true</code> - The characterstring "true" shall be
    *                   returned if the LMS accepts the content of parameter_2
    *                   to set the value of parameter_1. </li>
    *          <li>  <code>false</code> - The characterstring "false" shall
    *                   be returned if the LMS encounters an error in setting
    *                   the contents of parameter_1 with the value of
    *                   parameter_2.  The SCO may call <code>GetLastError()</code>
    *                   to determine the type of error. More detailed information
    *                   pertaining to the error may be provided by the LMS through
    *                   the <code>GetDiagnostic()</code> function. </li>
    *             
    *        </ul>
    */
   public String SetValue( String iDataModelElement, String iValue );

   /**
    *  The method requests forwarding to the persistent data store any data from
    *  the SCO that may have been cached by the API Implementation since the last
    *  call to Initialize("") or Commit(""),  whichever occurred most recently.
    *
    *
    * @param  iParam ("") - empty characterstring.  An empty characterstring
    *  shall be passed as a parameter.
    *
    * @return The method can return one of two values.  The return value shall
    *  be represented as a characterstring.  The quotes ("") are not part
    *  of the characterstring returned, they are used purely to set off
    *  the values returned.
    *     <ul>
    *       <li>  <code>true</code> - The characterstring "true" shall be
    *               returned if the data was successfully persisted to a
    *               successfully persisted to a long-term data store. </li>
    *       <li>  <code>false</code> - The characterstring "false" shall
    *               be returned if the data was unsuccessfully persisted to
    *               a long-term data store.  The API Instance shall set the
    *               error code to a value specific to the error encountered.
    *               The SCO may call <code>GetLastError()</code> to
    *               determine the type of error.  More detailed information
    *               pertaining to the error may be provided by the LMS through
    *               the <code>GetDiagnostic()</code> function. </li>
    *     </ul>
    */
   public String Commit( String iParam );

   /**
    *  This method requests the error code for the current error state of the
    *  API Instance. If a SCO calls this method, the API Instance
    *  shall not alter the current error state, but simply return the
    *  requested information.
    *
    * @param The API method shall not accept any parameters.
    *
    * @return The API Instance shall return the error code reflecting the
    *   current error state of the API Instance.  The return value shall be
    *   a characterstring (convertible to an integer in the range from 0 to
    *   65536 inclusive) representing the error code of the last error
    *   encountered.
    */
   public String GetLastError();

   /**
    * The GetErrorString() function can be used to retrieve a textual description
    * of the current error state.  The function is used by a SCO to request the
    * textual description for the error code specified by the value of the parameter.
    * The API Instance shall be responsible for supporting the error codes identified
    * in Section 3.1.7 API Implementation Error Codes.  This call has no effect on the
    * current error state; it simply returns the requested information.
    *
    * @param iErrorCode:  Represents the characterstring of the
    *            error code (integer value) corresponding to an error message.
    *
    * @return  The method shall return a textual message containing a description
    *   of the error code specified by the value of the parameter.  The following
    *   requirements shall be adhered to for all return values:
    *   <ul>
    *      <li> The return value shall be a characterstring that has a maximum
    *           length of 256 bytes (including null terminator). </li>
    *      <li> The SCORM makes no requirement on what the text of the characterstring
    *           shall contain.  The error codes themselves are explicitly and
    *           exclusively defined.  The textual description for the error code is
    *           LMS specific. </li>
    *      <li> If the requested error code is unknown by the LMS, an empty
    *           characterstring ("") shall be returned  This is the only time
    *           that an empty characterstring shall be returned. </li>
    *   </ul>
    */
   public String GetErrorString( String iErrorCode );

   /**
    * The GetDiagnostic() function exists for LMS specific use.  It  allows
    * the LMS to define additional diagnostic information through the API
    * Instance.  This call has no effect on the current error state; it simply
    * returns the requested information.
    *
    * @param iErrorCode  An implementer-specific value for diagnostics.
    *   The maximum length of the parameter value shall be 256 bytes
    *   (including null terminator).  The value of the parameter may be
    *   an error code, but is not limited to just error codes.
    *
    * @return The API Instance shall return a characterstring representing
    *    the diagnostic information.  The maximum length of the characterstring
    *    returned shall be 256 bytes (including null terminator).
    */
   public String GetDiagnostic( String iErrorCode );


}  // end SCORM1_2_API_Interface
