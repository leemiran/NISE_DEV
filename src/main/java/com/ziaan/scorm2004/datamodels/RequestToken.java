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


import java.util.Vector;


/**
 * Encapsulates one portion of a dot notation bound string defined by a
 * <code>DMRequest</code>
 * 
 * <strong>Filename:</strong> RequestToken.java<br><br>
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
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class RequestToken
{

   /**
    * Enumeration of the token types found in a SCORM dot notation bound data
    * model request.<br>
    * <br>Unknown
    * <br><b>-1</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int TOKEN_UNKNOWN             =   -1;


   /**
    * Enumeration of the token types found in a SCORM dot notation bound data
    * model request.<br>
    * <br>Data Model
    * <br><b>0</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int TOKEN_DATA_MODEL          =    0;


   /**
    * Enumeration of the token types found in a SCORM dot notation bound data
    * model request.<br>
    * <br>Data Model Element
    * <br><b>1</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int TOKEN_ELEMENT             =    1;


   /**
    * Enumeration of the token types found in a SCORM dot notation bound data
    * model request.<br>
    * <br>Index
    * <br><b>2</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int TOKEN_INDEX               =    2;


   /**
    * Enumeration of the token types found in a SCORM dot notation bound data
    * model request.<br>
    * <br>Value
    * <br><b>3</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int TOKEN_VALUE               =    3;


   /**
    * Enumeration of the token types found in a SCORM dot notation bound data
    * model request.<br>
    * <br>Argument
    * <br><b>4</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int TOKEN_ARGUMENT            =    4;


   /**
    * Describes the type of token represented by this token
    */
   private int mType = TOKEN_UNKNOWN;

   /**
    * Describes the value of this token
    */
   private String mValue = null;

   /**
    * Describes the set of delimiters associated with this token.<br><br>
    * <b>NOTE: Delimiters only apply to <code>TOKEN_VALUE</code> 
    * type tokens.</b>
    */
   private Vector mDelimiters = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Attempts to interpret the value as a valid token and determine it's type.
    * <br><strong>NOTE: This method does not differentiate between data model
    * and data model element dot-notation bindings.  All 'strings' are
    * considered data model elements.</strong>
    * 
    * @param iValue   The value of this token.
    * 
    * @param iIsValue Is this token a value 'type'?
    * 
    * @exception IllegalArgumentException
    *                   If the type of token is invalid or the value does not
    *                   meet the token's format.
    */
   public RequestToken(String iValue, boolean iIsValue)
   throws IllegalArgumentException
   {

      if ( !iIsValue )
      {
         // Determine which type of token this is and call the 'specific' 
         // constructor

         // If the first character is a '{', this must be an argument token
         if ( iValue.charAt(0) == '{' )
         {
            try
            {
               createRequestToken(iValue, TOKEN_ARGUMENT);
            }
            catch ( IllegalArgumentException iae )
            {
               throw iae;
            }
         }
         else
         {
            // If the this is an integer, it must be an index token
            try
            {
               int i = Integer.parseInt(iValue, 10);

               try
               {
                  createRequestToken(iValue, TOKEN_INDEX);
               }
               catch ( IllegalArgumentException iae )
               {
                  throw iae;
               }
            }
            catch ( NumberFormatException nf )
            {
               // This must be a data model element
               try
               {
                  createRequestToken(iValue, TOKEN_ELEMENT);
               }
               catch ( IllegalArgumentException iae )
               {
                  throw iae;
               }
            }
         }
      }
      else
      {
         if ( iValue != null )
         {
            int endDelimit = 0;

            // Parse this argument's delimiters
            try
            {
               endDelimit = parseDelimiters(iValue);
            }
            catch ( IllegalArgumentException iae )
            {
               // Errors in processing value delimiters can be ignored, we
               // simply add the invaild delimiters to the value string
               ;
            }

            // Find the last delimiter, if there are any
            if ( mDelimiters != null )
            {
               iValue = iValue.substring(endDelimit);
            }
         }

         mType = TOKEN_VALUE;
         mValue = iValue;
      }
   }

   /**
    * Attempts to interpret the value as a specific type of token.
    * 
    * @param iValue The value of this token.
    * 
    * @param iType  The type of this token
    * 
    * @exception NumberFormatException
    *                   If an index token is not formatted properly.
    * @exception IllegalArgumentException
    *                   If the type of token is invalid or the value does not
    *                   meet the token's format.
    */
   public RequestToken(String iValue, int iType) throws NumberFormatException,
   IllegalArgumentException
   {

      try
      {
         createRequestToken(iValue, iType);
      }
      catch ( IllegalArgumentException iae )
      {
         throw iae;
      }
   }


   /**
    * Retrives the type of this token.
    * 
    * @return Value of this token's type from the enumeration of token types
    *         provided by <code>DMToken</code> class.
    */
   public int getType()
   {
      return mType;
   }

   /**
    * Describes the delimiters associated with this token.
    * 
    * @return The set of delimiters associated with this token.
    */
   public Vector getDelimiters()
   {
      return mDelimiters;
   }

   /**
    * Describes the number of delimiters associated with this token.
    * 
    * @return The number of delimiters associated with this token.
    */
   public int getDelimiterCount()
   {
      int count = 0;

      if ( mDelimiters != null )
      {
         count = mDelimiters.size();
      }

      return count;
   }

   /**
    * Provides the index of a requested delimiter.
    * 
    * @param iName  The name of the requested delimiter.
    * 
    * @return The index of the requested delimiter or <code>-1</code> if the
    *         delimiter is not associated with this token.
    */
   public int getDelimiterIndex(String iName)
   {
      int index = -1;

      if ( mDelimiters != null )
      {
         // Look at all available delimiters
         for ( int i = 0; i < mDelimiters.size(); i++ )
         {
            RequestDelimiter del = (RequestDelimiter)mDelimiters.elementAt(i);

            if ( del.getName().equals(iName) )
            {
               index = i;
               break;
            }
         }
      }

      return index;
   }

   /**
    * Provides the value of a requested delimiter.
    * 
    * @param iName  The name of the requested delimiter.
    * 
    * @return The value of the requested delimiter or <code>null</code> if the
    *         delimiter is not associated with this token.
    */
   public String getDelimiterValue(String iName)
   {
      String val = null;

      if ( mDelimiters != null )
      {
         for ( int i = 0; i < mDelimiters.size(); i++ )
         {
            RequestDelimiter del = (RequestDelimiter)mDelimiters.elementAt(i);

            if ( del.getName().equals(iName) )
            {
               val = del.getValue();
               break;
            }
         }
      }

      return val;
   }

   /**
    * Provides the value of delimiter, given its index
    * 
    * @param iIndex The index of the requested delimiter.
    * 
    * @return The value of the delimiter at the specified index or
    *         <code>null</code> if the index is out of range.
    */
   public String getDelimiterValueAt(int iIndex)
   {
      String val = null;

      if ( mDelimiters != null )
      {
         if ( iIndex < mDelimiters.size() )
         {
            RequestDelimiter del = 
            (RequestDelimiter)mDelimiters.elementAt(iIndex);

            val = del.getValue();
         }
      }

      return val;
   }

   /**
    * Provides the delimiter, given its index
    * 
    * @param iIndex The index of the requested delimiter.
    * 
    * @return The the delimiter at the specified index or <code>null</code>
    *         if the index is out of range.
    */
   public RequestDelimiter getDelimiterAt(int iIndex)
   {
      RequestDelimiter del = null;

      if ( mDelimiters != null )
      {
         if ( iIndex < mDelimiters.size() )
         {
            del = (RequestDelimiter)mDelimiters.elementAt(iIndex);
         }
      }

      return del;
   }


   /**
    * Retrives the value of this token.
    * 
    * @return The value of this token, or <code>null</code> if the token does
    *         not have a type.
    */
   public String getValue()
   {
      String val = null;

      if ( mType != TOKEN_UNKNOWN )
      {
         val = mValue;
      }

      return val;
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Private Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Attempts to interpret the value as a specific type of token.
    * 
    * @param iValue The value of this token.
    * 
    * @param iType  The type of this token
    * 
    * @exception NumberFormatException
    *                   If an index token is not formatted properly.
    * @exception IllegalArgumentException
    *                   If the type of token is invalid or the value does not
    *                   meet the token's format.
    */
   private void createRequestToken(String iValue, int iType) 
   throws NumberFormatException, IllegalArgumentException
   {

      // Look at the type of token
      switch ( iType )
      {
         case TOKEN_ARGUMENT:

            // Parse this argument's delimiters
            try
            {
               parseDelimiters(iValue);
            }
            catch ( IllegalArgumentException iae )
            {
               // There can be no errors processing arguements; it must 
               // consist entirely of delimiters
               throw iae;
            }

            mType = iType;
            mValue = null;     

            break;

         case TOKEN_DATA_MODEL:
         case TOKEN_ELEMENT:

            // Make sure this is not an integer
            try
            {
               int i = Integer.parseInt(iValue, 10);

               // This shouldn't work
               throw new IllegalArgumentException("Element name cannot" +
                                                  " be an integer");
            }
            catch ( NumberFormatException nf )
            {
               // This is good -- do nothing
               ; 
            }

            mType = iType;
            mValue = iValue;            

            break;

         case TOKEN_INDEX:

            // Make sure this is an non-negative integer
            try
            {
               int i = Integer.parseInt(iValue, 10);

               if ( i < 0 )
               {
                  throw new NumberFormatException("Negative Index");
               }
            }
            catch ( NumberFormatException nf )
            {
               throw nf; 
            }

            mType = iType;
            mValue = iValue;            

            break;


         default:

            // Invalid type of token
            throw new IllegalArgumentException("Unknown token type");
      }
   }


   /**
    * Parses all delimiters from the identified string.
    * 
    * @param iValue String to parse
    *
    * @exception IllegalArgumentException
    *                   If a delimiter is improperly formatted.
    */
   private int parseDelimiters(String iValue) throws IllegalArgumentException
   {

      RequestDelimiter del = null;
      int start = -1;
      int end = -1;
      int equals = -1;
      int lookAt = 0;

      // Loop until the entire string is processed
      while ( lookAt != -1 && lookAt < iValue.length() )
      {
         
         if ( iValue.charAt(lookAt) != '{' )
         {
            // Done
            break;
         }
         else
         {
            start = lookAt;
         }

         // Find the closing '}'
         end = iValue.indexOf('}', start);

         // If we didn't find it, we have an improperly formed delimiter
         if ( end == -1 )
         {
            // Done
            break;
         }

         String consider = iValue.substring(start + 1, end);

         // Find the equal '='
         equals = consider.indexOf("=");

         // If we didn't find it, we have an improperly formed delimiter
         // Note: Only the first occurance of '=' is signifigant, any further
         // occurances are considered part of the value
         if ( equals == -1 )
         {
            // Done
            break;
         }

         // Attempt to create a new RequestDelimiter based on the information
         // we currently have
         try
         {
            del = new RequestDelimiter(consider.substring(0, equals),
                                       consider.substring(equals + 1));
         }
         catch ( Exception e )
         {
            throw new IllegalArgumentException(e.getMessage());
         }

         if ( mDelimiters == null )
         {
            mDelimiters = new Vector();
         }

         mDelimiters.add(del);

         // Move to where the next delimiter should start
         lookAt = end + 1;
      }

      return lookAt;
   }

} // end RequestToken
