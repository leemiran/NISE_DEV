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

/**
 * This class implements the error handling capabilities of the RTE API.<br><br>
 *
 * <strong>Filename:</strong> APIErrorCodes<br><br>
 *
 * <strong>Description:</strong><br>
 * This class contains an enumeration of the abstract API error codes
 * <br><br>
 *
 * <strong>Known Problems:</strong><br><br>
 *
 * <strong>Side Effects:</strong><br><br>
 *
 * <strong>References:</strong><br>
 * <ul>
 *     <li>SCORM 2004</li>
 *     <li>SCORM 2004</li>
 * </ul>
 *
 * @author ADL Technical Team
 */
public class APIErrorCodes implements Serializable
{
   /**
    * Enumeration of possible error codes (int representation)
    * <br>No Error
    * <br><b>0</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int NO_ERROR                      =  0;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>General Exception
    * <br><b>5000</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int GENERAL_EXCEPTION             =  101;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>General Initialization Failure
    * <br><b>5001</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int GENERAL_INIT_FAILURE          =  102;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Already Initialized
    * <br><b>5002</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int ALREADY_INITIALIZED           =  103;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Content Instance Terminated
    * <br><b>5003</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int CONTENT_INSTANCE_TERMINATED   =  104;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>General Termination Failure
    * <br><b>5004</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int GENERAL_TERMINATION_FAILURE   =  111;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Termination Before Initialization
    * <br><b>5005</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int TERMINATE_BEFORE_INIT         =  112;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Termination After Termination
    * <br><b>5006</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int TERMINATE_AFTER_TERMINATE     =  113;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Retrieve Data Before Initialization
    * <br><b>5007</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int GET_BEFORE_INIT               =  122;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Retrieve Data After Termination
    * <br><b>5008</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int GET_AFTER_TERMINATE           =  123;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Store Data Before Initialization
    * <br><b>5009</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int SET_BEFORE_INIT               =  132;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Store Data After Termination
    * <br><b>5010</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int SET_AFTER_TERMINATE           =  133;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Commit Before Initialization
    * <br><b>5011</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int COMMIT_BEFORE_INIT            =  142;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>Commit After Termination
    * <br><b>5012</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int COMMIT_AFTER_TERMINATE        =  143;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>General Argument Error
    * <br><b>201</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int GEN_ARGUMENT_ERROR       =  201;

   /**
    * Enumeration of possible error codes (int representation)
    * <br>General Commit Failure
    * <br><b>5013</b>
    * <br>[API SUBSYSTEM CONSTANT]
    */
   public final static int GENERAL_COMMIT_FAILURE        =  391;

}

