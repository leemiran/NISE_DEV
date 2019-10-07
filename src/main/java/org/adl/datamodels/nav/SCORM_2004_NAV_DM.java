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

package org.adl.datamodels.nav;


import org.adl.datamodels.*;
import org.adl.datamodels.datatypes.*;
import org.adl.sequencer.*;

import java.util.*;
import java.io.*;


/**
 * <br><br>
 * 
 * <strong>Filename:</strong> SCORM_2004_NAV_DM.java<br><br>
 * 
 * <strong>Description:  This class implements the set of navigation events 
 * defined in the SCORM 2004.</strong><br><br>
 * 
 * <strong>Design Issues:  none</strong><br><br>
 * 
 * <strong>Implementation Issues:  none</strong><br><br>
 * 
 * <strong>Known Problems:  none</strong><br><br>
 * 
 * <strong>Side Effects:  none<strong><br><br>
 * 
 * <strong>References:  SCORM 2004</strong><br>
 * 
 * 
 * @author ADL Technical Team
 */
public class SCORM_2004_NAV_DM extends DataModel implements Serializable
{

   /**
    * Describes the dot-notation binding string for this data model.
    */
   private String mBinding = "adl";

   /**
    * Describes the data model elements managed by this data model.
    */
   private Hashtable mElements = null;


   /**
    * Describes the current known 'valid' set of navigation requests.
    */
   /* package */
   ADLValidRequests mNavRequests = null;


   /** 
    * Describes the current navigation request.
    */
   /* package */
   String mCurRequest = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Constructors
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Default constructor required for serialization support.  Creates a complete 
    * set of navigation data model information.
    */
   public SCORM_2004_NAV_DM()
   {
      Vector children = null;

      SCORM_2004_NAV_DMElement element = null;
      DMElementDescriptor desc = null;
      DMDelimiterDescriptor del = null;

      mElements = new Hashtable();


      // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
      // nav

      children = new Vector();

      // request
      String [] vocab = { "continue", "previous", "choice", "exit", "exitAll",
         "abandon", "abandonAll", "_none_"};

      desc = new DMElementDescriptor("request", "_none_", 
                                     new VocabularyValidator(vocab));

      // The 'choice' request will include a delimiter
      del = new DMDelimiterDescriptor("target", null, 
                                      new URIValidator()); 

      desc.mDelimiters = new Vector();
      desc.mDelimiters.add(del);

      children.add(desc);

      Vector subChildren = new Vector();

      // continue
      String [] status = { "true", "false", "unknown"};
      desc = new DMElementDescriptor("continue", "unknown", 
                                     new VocabularyValidator(status));

      desc.mIsWriteable = false;
      subChildren.add(desc);

      // previous
      desc = new DMElementDescriptor("previous", "unknown", 
                                     new VocabularyValidator(status));

      desc.mIsWriteable = false;
      subChildren.add(desc);

      // choice
      desc = new DMElementDescriptor("choice", "unknown", 
                                     new VocabularyValidator(status));

      desc.mIsWriteable = false;
      subChildren.add(desc);

      // request_valid
      desc = new DMElementDescriptor("request_valid", subChildren);
      children.add(desc);

      desc = new DMElementDescriptor("nav", children);

      // Create and add this element to the data model
      element = new SCORM_2004_NAV_DMElement(desc, null, this);

      mElements.put(desc.mBinding, element);
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Processes an equals() request against this data model.
    * 
    * @param iRequest The (<code>DMRequest</code>) being processed.
    * 
    * @return A data model error code indicating the result of this
    *         operation.
    */
   public int equals(DMRequest iRequest)
   {

      // Assume no processing errors
      int result = DMErrorCodes.NO_ERROR;

      // Create an 'out' variable
      DMProcessingInfo pi = new DMProcessingInfo();

      // Process this request
      result = findElement(iRequest, pi);

      // If we found the 'leaf' elmeent, finish the request
      if ( result == DMErrorCodes.NO_ERROR )
      {
         RequestToken tok = iRequest.getNextToken();

         // Before processing, make sure this is the last token in the request
         if ( !iRequest.hasMoreTokens() )
         {
            // Make sure this is a  Value token
            if ( tok.getType() == RequestToken.TOKEN_VALUE )
            {
               result = pi.mElement.equals(tok);
            }
            else
            {
               // Wrong type of token -- value expected
               result = DMErrorCodes.INVALID_REQUEST;
            }
         }
         else
         {
            // Too many tokens
            result = DMErrorCodes.INVALID_REQUEST;
         }
      }

      return result;
   }

   /**
    * Describes this data model's binding string.
    * 
    * @return This data model's binding string.
    */
   public String getDMBindingString()
   {
      return mBinding;
   }

   /**
    * Provides the requested data model element.
    * 
    * @param iElement Describes the requested element's dot-notation bound name.
    * 
    * @return The <code>DMElement</code> corresponding to the requested element
    *         or <code>null</code> if the element does not exist in the data
    *         model.
    */
   public DMElement getDMElement(String iElement)
   {
      DMElement element = (DMElement)mElements.get(iElement);

      return element;
   }


   /**
    * Provides the current navigation request communicated by the SCO.
    * 
    * @return The current navigation request.
    */
   public String getNavRequest()
   {
      String request = null;
      int navEvent = SeqNavRequests.NAV_NONE;

      if ( mCurRequest != null )
      {
         if ( mCurRequest.equals("continue") )
         {
            navEvent = SeqNavRequests.NAV_CONTINUE;
         }
         else if ( mCurRequest.equals("previous") )
         {
            navEvent = SeqNavRequests.NAV_PREVIOUS;
         }
         else if ( mCurRequest.equals("exit") )
         {
            navEvent = SeqNavRequests.NAV_EXIT;
         }
         else if ( mCurRequest.equals("exitAll") )
         {
            navEvent = SeqNavRequests.NAV_EXITALL;
         }
         else if ( mCurRequest.equals("abandon") )
         {
            navEvent = SeqNavRequests.NAV_ABANDON;
         }
         else if ( mCurRequest.equals("abandonAll") )
         {
            navEvent = SeqNavRequests.NAV_ABANDONALL;
         }
         else if ( mCurRequest.equals("_none_") )
         {
            navEvent = SeqNavRequests.NAV_NONE;
         }
         else
         {
            // This must be a target for choice
            request = mCurRequest;
         }
      }

      if ( request == null )
      {
         request = Integer.toString(navEvent);
      }

      return request;
   }


   /**
    * Processes a GetValue() request against this data model.
    * 
    * @param iRequest The (<code>DMRequest</code>) being processed.
    * 
    * @param oInfo    Provides the value returned by this request.
    * 
    * @return A data model error code indicating the result of this
    *         operation.
    */
   public int getValue(DMRequest iRequest, DMProcessingInfo oInfo)
   {
      // Assume no processing errors
      int result = DMErrorCodes.NO_ERROR;

      // Create an 'out' variable
      DMProcessingInfo pi = new DMProcessingInfo();

      // Process this request
      result = findElement(iRequest, pi);

      // If we found the 'leaf' elmeent, finish the request
      if ( result == DMErrorCodes.NO_ERROR )
      {
         RequestToken tok = iRequest.getNextToken();

         // Before processing, make sure this is the last token in the request
         if ( !iRequest.hasMoreTokens() )
         {
            result = pi.mElement.getValue(tok,
                                          iRequest.isAdminRequest(),
                                          iRequest.supplyDefaultDelimiters(), 
                                          oInfo);
         }
         else
         {
            // Too many tokens
            result = DMErrorCodes.INVALID_REQUEST;
         }
      }

      return result;
   }

   /**
    * Performs data model specific initialization.
    * 
    * @return An abstract data model error code indicating the result of this
    *         operation.
    */
   public int initialize()
   {
      // -+- TO DO -+-

      return DMErrorCodes.NO_ERROR;
   }


   /**
    * Sets the current 'known' set of valid navigation requests for the SCO
    * to the SCO's instance of the SCORM Navigation Data Model.
    * 
    * @param iValid  The current 'known' set of valid navigation requests.
    */
   public void setValidRequests(ADLValidRequests iValid)
   {
      mNavRequests = iValid;
   }


   /**
    * Processes a SetValue() request against this data model.  Checks the 
    * request for validity.
    * 
    * @param iRequest The request (<code>DMRequest</code>) being processed.
    * 
    * @return A data model error code indicating the result of this
    *         operation.
    */
   public int setValue(DMRequest iRequest)
   {
      // Assume no processing errors
      int result = DMErrorCodes.NO_ERROR;

      // Create an 'out' variable
      DMProcessingInfo pi = new DMProcessingInfo();

      // Process this request
      result = findElement(iRequest, pi);

      // If we found the 'leaf' element, finish the request
      if ( result == DMErrorCodes.NO_ERROR )
      {
         RequestToken tok = iRequest.getNextToken();

         // Before processing, make sure this is the last token in the requset
         if ( !iRequest.hasMoreTokens() )
         {

            // Make sure this is a Value token
            if ( tok.getType() == RequestToken.TOKEN_VALUE )
            {
               if ( result == DMErrorCodes.NO_ERROR )
               {
                  result = pi.mElement.setValue(tok, iRequest.isAdminRequest());
               }
            }
            else
            {
               // Wrong type of token -- value expected
               result = DMErrorCodes.INVALID_REQUEST;
            }
         }
         else
         {
            // Too many tokens
            result = DMErrorCodes.INVALID_REQUEST;
         }
      }

      return result;
   }

   /**
    * Displays the contents of the entire data model.
    */
   public void showAllElements()
   {

      // -+- TO DO -+-
   }

   /**
    * Performs data model specific termination.
    * 
    * @return A data model error code indicating the result of this
    *         operation.
    */
   public int terminate()
   {
      // Clear the current nav request
      DMRequest req = new DMRequest("adl.nav.request", "_none_", true);
      
      // Remove the data model token 
      RequestToken tok = req.getNextToken();

      int result = setValue(req);
      mCurRequest = null;

      // Clear the current set of valid navigation requests
      mNavRequests = null;

      return DMErrorCodes.NO_ERROR;
   }

   /**
    * Processes a validate() request against this data model.
    * 
    * @param iRequest The (<code>DMRequest</code>) being processed.
    * 
    * @return A data model error code indicating the result of this
    *         operation.
    */
   public int validate(DMRequest iRequest)
   {
      // Assume no processing errors
      int result = DMErrorCodes.NO_ERROR;

      // Create an 'out' variable
      DMProcessingInfo pi = new DMProcessingInfo();

      // Process this request
      result = findElement(iRequest, pi);

      // If we found the 'leaf' element, finish the request
      if ( result == DMErrorCodes.NO_ERROR )
      {
         RequestToken tok = iRequest.getNextToken();

         // Before processing, make sure this is the last token in the request
         if ( !iRequest.hasMoreTokens() )
         {
            // Make sure this is a Value token
            if ( tok.getType() == RequestToken.TOKEN_VALUE )
            {
               result = pi.mElement.validate(tok);
            }
            else
            {
               // Wrong type of token -- value expected
               result = DMErrorCodes.INVALID_REQUEST;
            }
         }
         else
         {
            // Too many tokens
            result = DMErrorCodes.INVALID_REQUEST;
         }
      }

      return result;
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

    Private Methods

   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Processes a data model request by finding the target leaf element.
    * If the requested value is found, it is returned in the parameter
    * oInfo.
    * 
    * @param iRequest The (<code>DMRequest</code>) being processed.
    * 
    * @parma oInfo    Provides the value returned by this request.
    * 
    * @return An abstract data model error code indicating the result of this
    *         operation.
    */
   private int findElement(DMRequest iRequest, DMProcessingInfo oInfo)
   {
      // Assume no processing errors
      int result = DMErrorCodes.NO_ERROR;

      // Get the first specified element
      RequestToken tok = iRequest.getNextToken();

      if ( tok != null && tok.getType() == RequestToken.TOKEN_ELEMENT )
      {

         DMElement element = (DMElement)mElements.get(tok.getValue());

         if ( element != null )
         {

            oInfo.mElement = element;

            // Check if we need to stop before the last token
            tok = iRequest.getCurToken();
            boolean done = false;

            if ( tok != null )
            {
               if ( iRequest.isGetValueRequest() )
               {
                  if ( tok.getType() == RequestToken.TOKEN_ARGUMENT )
                  {
                     // We're done
                     done = true;
                  }
                  else if ( tok.getType() == RequestToken.TOKEN_VALUE )
                  {
                     // Get requests cannot have value tokens
                     result = DMErrorCodes.INVALID_REQUEST;

                     done = true;
                  }
               }
               else
               {
                  if ( tok.getType() == RequestToken.TOKEN_VALUE )
                  {
                     // We're done
                     done = true;
                  }
                  else if ( tok.getType() == RequestToken.TOKEN_ARGUMENT )
                  {
                     // Set requests cannot have argument tokens
                     result = DMErrorCodes.INVALID_REQUEST;

                     done = true;
                  }
               }
            }

            // Process remaining tokens
            while ( !done && iRequest.hasMoreTokens() && 
                    result == DMErrorCodes.NO_ERROR )
            {
               result = element.processRequest(iRequest, oInfo);

               // Move to the next element if processing was successful
               if ( result == DMErrorCodes.NO_ERROR )
               {
                  element = oInfo.mElement;
               }
               else
               {
                  oInfo.mElement = null;
               }

               // Check if we need to stop before the last token
               tok = iRequest.getCurToken();

               if ( tok != null )
               {
                  if ( iRequest.isGetValueRequest() )
                  {
                     if ( tok.getType() == RequestToken.TOKEN_ARGUMENT )
                     {
                        // We're done
                        done = true;
                     }
                     else if ( tok.getType() == RequestToken.TOKEN_VALUE )
                     {
                        // Get requests cannot have value tokens
                        result = DMErrorCodes.INVALID_REQUEST;
   
                        done = true;
                     }
                  }
                  else
                  {
                     if ( tok.getType() == RequestToken.TOKEN_VALUE )
                     {
                        // We're done
                        done = true;
                     }
                     else if ( tok.getType() == RequestToken.TOKEN_ARGUMENT )
                     {
                        // Set requests cannot have argument tokens
                        result = DMErrorCodes.INVALID_REQUEST;
   
                        done = true;
                     }
                  }
               }
            }
         }
         else
         {
            // Unknown element
            result = DMErrorCodes.UNDEFINED_ELEMENT;
         }
      }
      else
      {
         // No initial element specified
         result = DMErrorCodes.INVALID_REQUEST;
      }

      return result;
   }

} // end SCORM_2004_NAV_DM
