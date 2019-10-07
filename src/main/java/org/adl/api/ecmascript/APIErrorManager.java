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
 
package org.adl.api.ecmascript;

import java.io.Serializable;
import java.lang.*;
import java.util.*;
import org.adl.datamodels.DMErrorCodes;


/**
 * This class implements the error handling capabilities of the RTE API.<br><br>
 *
 * <strong>Filename:</strong> APIErrorManager<br><br>
 *
 * <strong>Description:</strong><br>
 * It manages the error codes set by the API functions in the API Adapter
 * Applet.
 * <br><br>
 *
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE 1.3. <br>
 * <br>
 *
 * <strong>Implementation Issues:</strong><br>
 * For purposes of this example, this class uses a hardcoded array to store
 * the error mapping.<br><br>
 *
 * <strong>Known Problems:</strong><br><br>
 *
 * <strong>Side Effects:</strong><br><br>
 *
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS Specification
 *     <li>SCORM 2004
 * </ul>
 *
 * @author ADL Technical Team
 */
public class APIErrorManager implements Serializable
{
   /**
    * The abstract error code from the last API method invocation.
    */
   private static int mCurrentErrorCode = 0;

   /**
    * Enumeration of possible APIs supported by this error manager
    * <br>SCORM Version 1.2 API
    * <br><b>1</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int SCORM_1_2_API                =  1;

   /**
    * Enumeration of possible APIs supported by this error manager
    * <br>SCORM Version 1.3 API
    * <br><b>2</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int SCORM_2004_API               =  2;

   /**
    * Hashtable that holds all of the API Error Codes as Strings.  The
    * abstract error codes are used as the keys for the String error codes.
    */
   private Hashtable mErrorCodes;

   /**
    * Hashtable that holds all of the API Error Messages.  The abstract
    * error codes are used as the keys for the error messages.
    */
   private Hashtable mErrorMessages;

   /**
    * Hashtable that holds all of the API Error Diagnostics.  The abstract
    * error codes are used as the keys for the error diagnostics.
    */
   private Hashtable mErrorDiagnostics;

   /**
    * Hashtable that converts an error string to the abstract integer.  The
    * String representations of the SCORM error codes are used as the keys.
    */
   private Hashtable mAbstErrors;


   /**
    * APIErrorManager Constuctor -- <br>
    * Initializes this <code>LMSErrorManager</code> to 'No Error' and
    * initializes the Hashtables based on the API version.
    *
    * @param iAPIVersion - The API version that will use this error manager.
    */
   public APIErrorManager(int iAPIVersion)
   {

      mCurrentErrorCode = APIErrorCodes.NO_ERROR;
      mErrorCodes = new Hashtable();
      mErrorMessages = new Hashtable();
      mErrorDiagnostics = new Hashtable();
      mAbstErrors = new Hashtable();

      if(iAPIVersion == SCORM_2004_API)
      {
         //  Initialize the SCORM RTE Version 1.3 API Error Codes Hash Table
         mErrorCodes.put(new Integer(APIErrorCodes.NO_ERROR), new String("0"));
         mErrorCodes.put(new Integer(APIErrorCodes.GENERAL_EXCEPTION),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.GENERAL_INIT_FAILURE),
                         new String("102"));
         mErrorCodes.put(new Integer(APIErrorCodes.ALREADY_INITIALIZED),
                         new String("103"));
         mErrorCodes.put(new Integer(APIErrorCodes.CONTENT_INSTANCE_TERMINATED),
                         new String("104"));
         mErrorCodes.put(new Integer(APIErrorCodes.GENERAL_TERMINATION_FAILURE),
                         new String("111"));
         mErrorCodes.put(new Integer(APIErrorCodes.TERMINATE_BEFORE_INIT),
                         new String("112"));
         mErrorCodes.put(new Integer(APIErrorCodes.TERMINATE_AFTER_TERMINATE),
                         new String("113"));
         mErrorCodes.put(new Integer(APIErrorCodes.GET_BEFORE_INIT),
                         new String("122"));
         mErrorCodes.put(new Integer(APIErrorCodes.GET_AFTER_TERMINATE),
                         new String("123"));
         mErrorCodes.put(new Integer(APIErrorCodes.SET_BEFORE_INIT),
                         new String("132"));
         mErrorCodes.put(new Integer(APIErrorCodes.SET_AFTER_TERMINATE),
                         new String("133"));
         mErrorCodes.put(new Integer(APIErrorCodes.COMMIT_BEFORE_INIT),
                         new String("142"));
         mErrorCodes.put(new Integer(APIErrorCodes.COMMIT_AFTER_TERMINATE),
                         new String("143"));
         mErrorCodes.put(new Integer(DMErrorCodes.GEN_ARGUMENT_ERROR),
                         new String("201"));
         mErrorCodes.put(new Integer(DMErrorCodes.GEN_GET_FAILURE),
                         new String("301"));
         mErrorCodes.put(new Integer(DMErrorCodes.GEN_SET_FAILURE),
                         new String("351"));
         mErrorCodes.put(new Integer(APIErrorCodes.GENERAL_COMMIT_FAILURE),
                         new String("391"));
         mErrorCodes.put(new Integer(DMErrorCodes.UNDEFINED_ELEMENT),
                         new String("401"));
         mErrorCodes.put(new Integer(DMErrorCodes.NOT_IMPLEMENTED),
                         new String("402"));
         mErrorCodes.put(new Integer(DMErrorCodes.NOT_INITIALIZED),
                         new String("403"));
         mErrorCodes.put(new Integer(DMErrorCodes.READ_ONLY),
                         new String("404"));
         mErrorCodes.put(new Integer(DMErrorCodes.WRITE_ONLY),
                         new String("405"));
         mErrorCodes.put(new Integer(DMErrorCodes.TYPE_MISMATCH),
                         new String("406"));
         mErrorCodes.put(new Integer(DMErrorCodes.VALUE_OUT_OF_RANGE),
                         new String("407"));
         mErrorCodes.put(new Integer(DMErrorCodes.DEP_NOT_ESTABLISHED),
                         new String("408"));
         mErrorCodes.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_CHILDREN),
                         new String("301"));
         mErrorCodes.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_COUNT),
                         new String("301"));
         mErrorCodes.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_VERSION),
                         new String("301"));
         mErrorCodes.put(new Integer(DMErrorCodes.SET_OUT_OF_ORDER),
                         new String("351"));
         mErrorCodes.put(new Integer(DMErrorCodes.OUT_OF_RANGE),
                         new String("301"));
         mErrorCodes.put(new Integer(DMErrorCodes.ELEMENT_NOT_SPECIFIED),
                         new String("351"));
         mErrorCodes.put(new Integer(DMErrorCodes.NOT_UNIQUE),
                         new String("351"));
         mErrorCodes.put(new Integer(DMErrorCodes.MAX_EXCEEDED),
                         new String("351"));
         mErrorCodes.put(new Integer(DMErrorCodes.SET_KEYWORD),
                         new String("404"));
         mErrorCodes.put(new Integer(DMErrorCodes.INVALID_REQUEST),
                         new String("401"));
         mErrorCodes.put(new Integer(DMErrorCodes.INVALID_ARGUMENT),
                         new String("301"));


         //  Initialize the SCORM RTE Version 1.3 API Error Messages Hash Table
         mErrorMessages.put(new Integer(APIErrorCodes.NO_ERROR),
                            new String("No Error"));
         mErrorMessages.put(new Integer(APIErrorCodes.GENERAL_EXCEPTION),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(APIErrorCodes.GENERAL_INIT_FAILURE),
                            new String("General Initialization Error"));
         mErrorMessages.put(new Integer(APIErrorCodes.ALREADY_INITIALIZED),
                            new String("Already Initialized"));
         mErrorMessages.put(new Integer(
                            APIErrorCodes.CONTENT_INSTANCE_TERMINATED),
                            new String("Content Instance Terminated"));
         mErrorMessages.put(new Integer(
                            APIErrorCodes.GENERAL_TERMINATION_FAILURE),
                            new String("General Termination Failure"));
         mErrorMessages.put(new Integer(APIErrorCodes.TERMINATE_BEFORE_INIT),
                            new String("Termination Before Initialization"));
         mErrorMessages.put(new Integer(
                            APIErrorCodes.TERMINATE_AFTER_TERMINATE),
                            new String("Termination After Termination"));
         mErrorMessages.put(new Integer(APIErrorCodes.GET_BEFORE_INIT),
                            new String("Retrieve Data Before Initialization"));
         mErrorMessages.put(new Integer(APIErrorCodes.GET_AFTER_TERMINATE),
                            new String("Retrieve Data After Termination"));
         mErrorMessages.put(new Integer(APIErrorCodes.SET_BEFORE_INIT),
                            new String("Store Data Before Initialization"));
         mErrorMessages.put(new Integer(APIErrorCodes.SET_AFTER_TERMINATE),
                            new String("Store Data After Termination"));
         mErrorMessages.put(new Integer(APIErrorCodes.COMMIT_BEFORE_INIT),
                            new String("Commit Before Initialization"));
         mErrorMessages.put(new Integer(APIErrorCodes.COMMIT_AFTER_TERMINATE),
                            new String("Commit After Termination"));
         mErrorMessages.put(new Integer(DMErrorCodes.GEN_ARGUMENT_ERROR),
                            new String("General Argument Error"));
         mErrorMessages.put(new Integer(DMErrorCodes.GEN_GET_FAILURE),
                            new String("General Get Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.GEN_SET_FAILURE),
                            new String("General Set Failure"));
         mErrorMessages.put(new Integer(APIErrorCodes.GENERAL_COMMIT_FAILURE),
                            new String("General Commit Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.UNDEFINED_ELEMENT),
                            new String("Undefined Data Model Element"));
         mErrorMessages.put(new Integer(DMErrorCodes.NOT_IMPLEMENTED),
                            new String("Unimplemented Data Model Element"));
         mErrorMessages.put(new Integer(DMErrorCodes.NOT_INITIALIZED),
                            new String("Data Model Element Value Not " +
                                        "Initialized"));
         mErrorMessages.put(new Integer(DMErrorCodes.READ_ONLY),
                            new String("Data Model Element Is Read Only"));
         mErrorMessages.put(new Integer(DMErrorCodes.WRITE_ONLY),
                            new String("Data Model Element Is Write Only"));
         mErrorMessages.put(new Integer(DMErrorCodes.TYPE_MISMATCH),
                            new String("Data Model Element Type Mismatch"));
         mErrorMessages.put(new Integer(DMErrorCodes.VALUE_OUT_OF_RANGE),
                            new String("Data Model Element Value Out Of " +
                                       "Range"));
         mErrorMessages.put(new Integer(DMErrorCodes.DEP_NOT_ESTABLISHED),
                            new String("Data Model Dependency Not " +
                                       "Established"));
         mErrorMessages.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_CHILDREN),
                            new String("General Get Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_COUNT),
                            new String("General Get Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_VERSION),
                            new String("General Get Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.SET_OUT_OF_ORDER),
                            new String("General Set Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.OUT_OF_RANGE),
                            new String("General Get Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.ELEMENT_NOT_SPECIFIED),
                            new String("General Get Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.NOT_UNIQUE),
                            new String("General Set Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.MAX_EXCEEDED),
                            new String("General Set Failure"));
         mErrorMessages.put(new Integer(DMErrorCodes.SET_KEYWORD),
                            new String("Data Model Element Is Read Only"));
         mErrorMessages.put(new Integer(DMErrorCodes.INVALID_REQUEST),
                            new String("Undefined Data Model Element"));
         mErrorMessages.put(new Integer(DMErrorCodes.INVALID_ARGUMENT),
                            new String("General Get Failure"));


         //  Initialize the SCORM RTE Version 1.3 API Error
         //  Diagnostics Hash Table
         mErrorDiagnostics.put(new Integer(APIErrorCodes.NO_ERROR),
                               new String("No Error"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.GENERAL_EXCEPTION),
                               new String("General Exception"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.GENERAL_INIT_FAILURE),
                               new String("General Initialization Error"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.ALREADY_INITIALIZED),
                               new String("Already Initialized"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.CONTENT_INSTANCE_TERMINATED),
                               new String("Content Instance Terminated"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.GENERAL_TERMINATION_FAILURE),
                               new String("General Termination Failure"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.TERMINATE_BEFORE_INIT),
                               new String("Termination Before Initialization"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.TERMINATE_AFTER_TERMINATE),
                               new String("Termination After Termination"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.GET_BEFORE_INIT),
                               new String("Retrieve Data Before " +
                                          "Initialization"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.GET_AFTER_TERMINATE),
                               new String("Retrieve Data After Termination"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.SET_BEFORE_INIT),
                               new String("Store Data Before Initialization"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.SET_AFTER_TERMINATE),
                               new String("Store Data After Termination"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.COMMIT_BEFORE_INIT),
                               new String("Commit Before Initialization"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.COMMIT_AFTER_TERMINATE),
                               new String("Commit After Termination"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.GEN_ARGUMENT_ERROR),
                               new String("General Argument Error"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.GEN_GET_FAILURE),
                               new String("General Get Failure"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.GEN_SET_FAILURE),
                               new String("General Set Failure"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.GENERAL_COMMIT_FAILURE),
                               new String("General Commit Failure"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.UNDEFINED_ELEMENT),
                               new String("Undefined Data Model Element"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.NOT_IMPLEMENTED),
                               new String("Unimplemented Data Model Element"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.NOT_INITIALIZED),
                               new String("Data Model Element Value Not " +
                                        "Initialized"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.READ_ONLY),
                               new String("Data Model Element Is Read Only"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.WRITE_ONLY),
                               new String("Data Model Element Is Write Only"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.TYPE_MISMATCH),
                               new String("Data Model Element Type Mismatch"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.VALUE_OUT_OF_RANGE),
                               new String("Data Model Element Value Out Of " +
                                          "Range"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.DEP_NOT_ESTABLISHED),
                               new String("Data Model Dependency Not " +
                                          "Established"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_CHILDREN),
                               new String("Data Model Element does not have " +
                                          "Children"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_COUNT),
                               new String("Data Model Element does not have " +
                                          "Count"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_VERSION),
                               new String("Data Model Element does not have " +
                                          "Version"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.SET_OUT_OF_ORDER),
                               new String("Data Model Array Set out of Order"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.OUT_OF_RANGE),
                               new String("Value Out of Range"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.ELEMENT_NOT_SPECIFIED),
                               new String("No Element Specified"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.NOT_UNIQUE),
                               new String("Value is not Unique"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.MAX_EXCEEDED),
                               new String("Error - Maximum Exceeded"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.SET_KEYWORD),
                               new String("Data Model Element Is a Keyword"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.INVALID_REQUEST),
                               new String("Request was Invalid"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.INVALID_ARGUMENT),
                               new String("Invalid Argument Error"));


         // Hash Table used to get the abstract error code from an error String
         mAbstErrors.put(new String("0"), new Integer(APIErrorCodes.NO_ERROR));
         mAbstErrors.put(new String("101"),
                         new Integer(APIErrorCodes.GENERAL_EXCEPTION));
         mAbstErrors.put(new String("102"),
                         new Integer(APIErrorCodes.GENERAL_INIT_FAILURE));
         mAbstErrors.put(new String("103"),
                         new Integer(APIErrorCodes.ALREADY_INITIALIZED));
         mAbstErrors.put(new String("104"),
                        new Integer(APIErrorCodes.CONTENT_INSTANCE_TERMINATED));
         mAbstErrors.put(new String("111"),
                        new Integer(APIErrorCodes.GENERAL_TERMINATION_FAILURE));
         mAbstErrors.put(new String("112"),
                         new Integer(APIErrorCodes.TERMINATE_BEFORE_INIT));
         mAbstErrors.put(new String("113"),
                         new Integer(APIErrorCodes.TERMINATE_AFTER_TERMINATE));
         mAbstErrors.put(new String("122"),
                         new Integer(APIErrorCodes.GET_BEFORE_INIT));
         mAbstErrors.put(new String("123"),
                         new Integer(APIErrorCodes.GET_AFTER_TERMINATE));
         mAbstErrors.put(new String("132"),
                         new Integer(APIErrorCodes.SET_BEFORE_INIT));
         mAbstErrors.put(new String("133"),
                         new Integer(APIErrorCodes.SET_AFTER_TERMINATE));
         mAbstErrors.put(new String("142"),
                         new Integer(APIErrorCodes.COMMIT_BEFORE_INIT));
         mAbstErrors.put(new String("143"),
                         new Integer(APIErrorCodes.COMMIT_AFTER_TERMINATE));
         mAbstErrors.put(new String("201"),
                         new Integer(DMErrorCodes.GEN_ARGUMENT_ERROR));
         mAbstErrors.put(new String("301"),
                         new Integer(DMErrorCodes.GEN_GET_FAILURE));
         mAbstErrors.put(new String("351"),
                         new Integer(DMErrorCodes.GEN_SET_FAILURE));
         mAbstErrors.put(new String("391"),
                         new Integer(APIErrorCodes.GENERAL_COMMIT_FAILURE));
         mAbstErrors.put(new String("401"),
                         new Integer(DMErrorCodes.UNDEFINED_ELEMENT));
         mAbstErrors.put(new String("402"),
                         new Integer(DMErrorCodes.NOT_IMPLEMENTED));
         mAbstErrors.put(new String("403"),
                         new Integer(DMErrorCodes.NOT_INITIALIZED));
         mAbstErrors.put(new String("404"),
                         new Integer(DMErrorCodes.READ_ONLY));
         mAbstErrors.put(new String("405"),
                         new Integer(DMErrorCodes.WRITE_ONLY));
         mAbstErrors.put(new String("406"),
                         new Integer(DMErrorCodes.TYPE_MISMATCH));
         mAbstErrors.put(new String("407"),
                         new Integer(DMErrorCodes.VALUE_OUT_OF_RANGE));
         mAbstErrors.put(new String("408"),
                         new Integer(DMErrorCodes.DEP_NOT_ESTABLISHED));
      }
      else if(iAPIVersion == SCORM_1_2_API)
      {
         //  Initialize the SCORM Version 1.2 API Error Codes Hash Table
         mErrorCodes.put(new Integer(APIErrorCodes.NO_ERROR), new String("0"));
         mErrorCodes.put(new Integer(APIErrorCodes.GENERAL_EXCEPTION),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.GENERAL_INIT_FAILURE),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.ALREADY_INITIALIZED),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.CONTENT_INSTANCE_TERMINATED),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.GENERAL_TERMINATION_FAILURE),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.TERMINATE_BEFORE_INIT),
                         new String("301"));
         mErrorCodes.put(new Integer(APIErrorCodes.TERMINATE_AFTER_TERMINATE),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.GET_BEFORE_INIT),
                         new String("301"));
         mErrorCodes.put(new Integer(APIErrorCodes.GET_AFTER_TERMINATE),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.SET_BEFORE_INIT),
                         new String("301"));
         mErrorCodes.put(new Integer(APIErrorCodes.SET_AFTER_TERMINATE),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.COMMIT_BEFORE_INIT),
                         new String("301"));
         mErrorCodes.put(new Integer(APIErrorCodes.COMMIT_AFTER_TERMINATE),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.GEN_ARGUMENT_ERROR),
                         new String("201"));
         mErrorCodes.put(new Integer(DMErrorCodes.GEN_GET_FAILURE),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.GEN_SET_FAILURE),
                         new String("101"));
         mErrorCodes.put(new Integer(APIErrorCodes.GENERAL_COMMIT_FAILURE),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.UNDEFINED_ELEMENT),
                         new String("401"));
         mErrorCodes.put(new Integer(DMErrorCodes.NOT_IMPLEMENTED),
                         new String("401"));
         mErrorCodes.put(new Integer(DMErrorCodes.NOT_INITIALIZED),
                         new String("301"));
         mErrorCodes.put(new Integer(DMErrorCodes.READ_ONLY),
                         new String("403"));
         mErrorCodes.put(new Integer(DMErrorCodes.WRITE_ONLY),
                         new String("404"));
         mErrorCodes.put(new Integer(DMErrorCodes.TYPE_MISMATCH),
                         new String("405"));
         mErrorCodes.put(new Integer(DMErrorCodes.VALUE_OUT_OF_RANGE),
                         new String("405"));
         mErrorCodes.put(new Integer(DMErrorCodes.DEP_NOT_ESTABLISHED),
                         new String("405"));
         mErrorCodes.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_CHILDREN),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_COUNT),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_VERSION),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.SET_OUT_OF_ORDER),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.OUT_OF_RANGE),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.ELEMENT_NOT_SPECIFIED),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.NOT_UNIQUE),
                         new String("101"));
         mErrorCodes.put(new Integer(DMErrorCodes.MAX_EXCEEDED),
                         new String("405"));
         mErrorCodes.put(new Integer(DMErrorCodes.SET_KEYWORD),
                         new String("402"));
         mErrorCodes.put(new Integer(DMErrorCodes.INVALID_REQUEST),
                         new String("401"));


         //  Initialize the SCORM Version 1.2 API Error Messages Hash Table
         mErrorMessages.put(new Integer(APIErrorCodes.NO_ERROR),
                            new String("No Error"));
         mErrorMessages.put(new Integer(APIErrorCodes.GENERAL_EXCEPTION),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(APIErrorCodes.GENERAL_INIT_FAILURE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(APIErrorCodes.ALREADY_INITIALIZED),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(
                                 APIErrorCodes.CONTENT_INSTANCE_TERMINATED),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(
                                 APIErrorCodes.GENERAL_TERMINATION_FAILURE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(APIErrorCodes.TERMINATE_BEFORE_INIT),
                            new String("Not Initialized"));
         mErrorMessages.put(new Integer(
                                 APIErrorCodes.TERMINATE_AFTER_TERMINATE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(APIErrorCodes.GET_BEFORE_INIT),
                            new String("Not Initialized"));
         mErrorMessages.put(new Integer(APIErrorCodes.GET_AFTER_TERMINATE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(APIErrorCodes.SET_BEFORE_INIT),
                            new String("Not Initialized"));
         mErrorMessages.put(new Integer(APIErrorCodes.SET_AFTER_TERMINATE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(APIErrorCodes.COMMIT_BEFORE_INIT),
                            new String("Not Initialized"));
         mErrorMessages.put(new Integer(APIErrorCodes.COMMIT_AFTER_TERMINATE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.GEN_ARGUMENT_ERROR),
                            new String("Invalid Argument Error"));
         mErrorMessages.put(new Integer(DMErrorCodes.GEN_GET_FAILURE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.GEN_SET_FAILURE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(APIErrorCodes.GENERAL_COMMIT_FAILURE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.UNDEFINED_ELEMENT),
                            new String("Not Implemented Error"));
         mErrorMessages.put(new Integer(DMErrorCodes.NOT_IMPLEMENTED),
                            new String("Not Implemented Error"));
         mErrorMessages.put(new Integer(DMErrorCodes.NOT_INITIALIZED),
                            new String("Not Initialized"));
         mErrorMessages.put(new Integer(DMErrorCodes.READ_ONLY),
                            new String("Element is Read Only"));
         mErrorMessages.put(new Integer(DMErrorCodes.WRITE_ONLY),
                            new String("Element is Write Only"));
         mErrorMessages.put(new Integer(DMErrorCodes.TYPE_MISMATCH),
                            new String("Incorrect Data Type"));
         mErrorMessages.put(new Integer(DMErrorCodes.VALUE_OUT_OF_RANGE),
                            new String("Incorrect Data Type"));
         mErrorMessages.put(new Integer(DMErrorCodes.DEP_NOT_ESTABLISHED),
                            new String("Incorrect Data Type"));
         mErrorMessages.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_CHILDREN),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_COUNT),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_VERSION),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.SET_OUT_OF_ORDER),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.OUT_OF_RANGE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.ELEMENT_NOT_SPECIFIED),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.NOT_UNIQUE),
                            new String("General Exception"));
         mErrorMessages.put(new Integer(DMErrorCodes.MAX_EXCEEDED),
                            new String("Incorrect Data Type"));
         mErrorMessages.put(new Integer(DMErrorCodes.SET_KEYWORD),
                            new String("Invalid Set Value.  Element is "+
                                       "a Keyword"));
         mErrorMessages.put(new Integer(DMErrorCodes.INVALID_REQUEST),
                            new String("Not Implemented Error"));

         //  Initialize the SCORM 1.2 API Error Diagnostics
                  //  Initialize the SCORM RTE Version 1.3 API Error
         //  Diagnostics Hash Table
         mErrorDiagnostics.put(new Integer(APIErrorCodes.NO_ERROR),
                               new String("No Error"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.GENERAL_EXCEPTION),
                               new String("General Exception"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.GENERAL_INIT_FAILURE),
                               new String("General Initialization Error"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.ALREADY_INITIALIZED),
                               new String("Already Initialized"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.CONTENT_INSTANCE_TERMINATED),
                               new String("Content Instance Terminated"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.GENERAL_TERMINATION_FAILURE),
                               new String("General Termination Failure"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.TERMINATE_BEFORE_INIT),
                               new String("Termination Before Initialization"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.TERMINATE_AFTER_TERMINATE),
                               new String("Termination After Termination"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.GET_BEFORE_INIT),
                               new String("Retrieve Data Before " +
                                          "Initialization"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.GET_AFTER_TERMINATE),
                               new String("Retrieve Data After Termination"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.SET_BEFORE_INIT),
                               new String("Store Data Before Initialization"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.SET_AFTER_TERMINATE),
                               new String("Store Data After Termination"));
         mErrorDiagnostics.put(new Integer(APIErrorCodes.COMMIT_BEFORE_INIT),
                               new String("Commit Before Initialization"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.COMMIT_AFTER_TERMINATE),
                               new String("Commit After Termination"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.GEN_ARGUMENT_ERROR),
                               new String("General Argument Error"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.GEN_GET_FAILURE),
                               new String("General Get Failure"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.GEN_SET_FAILURE),
                               new String("General Set Failure"));
         mErrorDiagnostics.put(new Integer(
                               APIErrorCodes.GENERAL_COMMIT_FAILURE),
                               new String("General Commit Failure"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.UNDEFINED_ELEMENT),
                               new String("Undefined Data Model Element"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.NOT_IMPLEMENTED),
                               new String("Unimplemented Data Model Element"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.NOT_INITIALIZED),
                               new String("Data Model Element Value Not " +
                                        "Initialized"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.READ_ONLY),
                               new String("Data Model Element Is Read Only"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.WRITE_ONLY),
                               new String("Data Model Element Is Write Only"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.TYPE_MISMATCH),
                               new String("Data Model Element Type Mismatch"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.VALUE_OUT_OF_RANGE),
                               new String("Data Model Element Value Out Of " +
                                          "Range"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.DEP_NOT_ESTABLISHED),
                               new String("Data Model Dependency Not " +
                                          "Established"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_CHILDREN),
                               new String("Data Model Element does not have " +
                                          "Children"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_COUNT),
                               new String("Data Model Element does not have " +
                                          "Count"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.DOES_NOT_HAVE_VERSION),
                               new String("Data Model Element does not have " +
                                          "Version"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.SET_OUT_OF_ORDER),
                               new String("Data Model Array Set out of Order"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.OUT_OF_RANGE),
                               new String("Value Out of Range"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.ELEMENT_NOT_SPECIFIED),
                               new String("No Element Specified"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.NOT_UNIQUE),
                               new String("Value is not Unique"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.MAX_EXCEEDED),
                               new String("Error - Maximum Exceeded"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.SET_KEYWORD),
                               new String("Data Model Element Is a Keyword"));
         mErrorDiagnostics.put(new Integer(DMErrorCodes.INVALID_REQUEST),
                               new String("Request was Invalid"));

         //  Initialize the SCORM 1.2 API Error Codes
         mAbstErrors.put(new String("0"), new Integer(APIErrorCodes.NO_ERROR));
         mAbstErrors.put(new String("101"),
                         new Integer(APIErrorCodes.GENERAL_EXCEPTION));
         mAbstErrors.put(new String("201"),
                         new Integer(DMErrorCodes.GEN_ARGUMENT_ERROR));
         mAbstErrors.put(new String("202"),
                         new Integer(DMErrorCodes.DOES_NOT_HAVE_CHILDREN));
         mAbstErrors.put(new String("203"),
                         new Integer(DMErrorCodes.SET_OUT_OF_ORDER));
         mAbstErrors.put(new String("301"),
                         new Integer(DMErrorCodes.NOT_INITIALIZED));
         mAbstErrors.put(new String("401"),
                         new Integer(DMErrorCodes.NOT_IMPLEMENTED));
         mAbstErrors.put(new String("402"),
                         new Integer(DMErrorCodes.GEN_SET_FAILURE));
         mAbstErrors.put(new String("403"),
                         new Integer(DMErrorCodes.READ_ONLY));
         mAbstErrors.put(new String("404"),
                         new Integer(DMErrorCodes.WRITE_ONLY));
         mAbstErrors.put(new String("405"),
                         new Integer(DMErrorCodes.TYPE_MISMATCH));

      }
   }


   /**
    * Retrieves the current avaliable error code (String).
    *
    * @return the value of the current error code that was set by the most
    *         recent API call.
    */
   public String getCurrentErrorCode()
   {
      Integer errInt = new Integer(mCurrentErrorCode);
      String err = (String)mErrorCodes.get(errInt);

      if(err == null)
      {
         err = new String("0");
      }

      return err;
   }

   /**
    * Retrieves the current avaliable error code (int).
    *
    * @return the value of the current error code that was set by the most
    *         recent API call.
    */
   //public int getCurrentErrorCode()
   //{
   //   return mCurrentErrorCode;
   //}

   /**
    * Sets the error code (from the predefined list of codes).
    *
    * @param iCode  The error code being set.
    */
   public void setCurrentErrorCode(int iCode)
   {

      mCurrentErrorCode = iCode;
   }


   /**
    * Sets the current error code to 'No Error'
    */
   public void clearCurrentErrorCode()
   {
      mCurrentErrorCode = APIErrorCodes.NO_ERROR;
   }

   /**
    * Retrieves the text associated with a given error code.
    *
    * @param iCode  The specified error code.
    *
    * @return The text associated with the specfied error code.
    */
   public String getErrorDescription(String iCode)
   {
      String message = "";

      if ( (iCode != null) && (!iCode.equals("")) )
      {
         // Retrieves and returns the description of the provided error code
         Integer errInt = (Integer)mAbstErrors.get(iCode);

         if ( errInt != null )
         {
            message = (String)mErrorMessages.get(errInt);

            if(message == null)
            {
               message = "";
            }
         }
         else
         {
            message = "";
         }
      }

      return message;
   }

   /**
    * Retrieves the text associated with the current error code.
    *
    * @return The text associated with the specfied error code.
    */
   public String getErrorDescription()
   {
      // Retrieves and returns the description of the current error code
      Integer errInt = new Integer(mCurrentErrorCode);
      return (String)mErrorMessages.get(errInt);
   }

   /**
    * Retrieves the diagnostic text associated with an error code.
    *
    * @param iCode  The specified error code.
    *
    * @return The diagnostic text associated with the specificed error code.
    */
   public String getErrorDiagnostic(String iCode)
   {
      if ( (iCode != null) && (!iCode.equals("")) )
      {
         // Returns the diagnostic text of the provided error code
         Integer errInt =  (Integer)mAbstErrors.get(iCode);

         if(errInt != null)
         {
            String diagnostic = (String)mErrorDiagnostics.get(errInt);

            if(diagnostic != null)
            {
               return diagnostic;
            }
            else
            {
               return new String("");
            }
         }
         else
         {
            return new String("");
         }

      }
      else
      {
         // returns diagnostic text of previous error
         Integer errInt = new Integer(mCurrentErrorCode);
         return (String)mErrorDiagnostics.get(errInt);
      }
   }

   /**
    * Retrieves the diagnostic text associated with the current error code.
    *
    * @return The diagnostic text associated with the specificed error code.
    */
   public String getErrorDiagnostic()
   {
      // returns diagnostic text of previous error
      Integer errInt = new Integer(mCurrentErrorCode);
      return (String)mErrorDiagnostics.get(errInt);
   }



   /**
    * Determines whether or not the Real Error Code passed in is a valid
    * error code.
    *
    * @param iErrorCode The error code.
    * @return Indicates whether or not the error code is valid.
    */
   public boolean isValidErrorCode( String iErrorCode )
   {
      boolean result = false;
      Enumeration enume = mErrorCodes.elements();

      while ( !result && enume.hasMoreElements() )
      {
         String comp = (String)enume.nextElement();

         if ( comp.equals(iErrorCode) == true )

         {
            result = true;
         }
      }

      return result;
   }


} // APIErrorManager
