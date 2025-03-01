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


import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Parses and encapsulates a dot notation bound string for a SCORM data model
 * request processed through the SCORM API.
 * 
 * <strong>Filename:</strong> DMRequest.java<br><br>
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
public class DMRequest
{

   /**
    * Describes the condition where no more tokens are available.<br>
    * <br><b>-1</b>
    * <br><br>[DATA MODEL IMPLEMENTATION CONSTANT]
    */
   public final static int NO_MORE_TOKENS          =    -1;


   /**
    * Describes if the value for a SetValue() call should be validated
    */
   private boolean mAdmin = false;

   /**
    * Describes the current token being processed in this request
    */
   private int mCurToken = NO_MORE_TOKENS;

   /**
    * Describes if default delimiters should be supplied for a GetValue() call
    */
   private boolean mDefDelimiters = false;

   /**
    * Describes if this request is for a GetValue() call
    */
   private boolean mGetValue = false;

   /**
    * Describes the dot notation bound string as a series of 
    * <code>DMToken</code> objects
    */
   private Vector mTokens = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Constructors
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    *  Parses a SCORM API <code>getValue()</code> request.
    * 
    * @param iElement       The dot-notation bound data model element name
    *                       (parameter #1) being requested.
    * 
    * @param iAdmin         Describes if this request is an administrative
    *                       action.
    * 
    * @param iDefDelimiters Describes if the default delimiters (if they
    *                       exist) should be returned as part of the data
    *                       model element's value.
    * 
    * @exception NullPointerException
    *                   If the element name is omitted.
    * @exception IllegalArgumentException
    *                   If the element name is improperly formatted.
    */
   public DMRequest(String iElement,
                    boolean iAdmin,
                    boolean iDefDelimiters) throws NullPointerException,
                                                   IllegalArgumentException
   {

      // Make sure the data model element name is specified
      if ( iElement != null )
      {
         // This is a GetValue() request
         mAdmin = iAdmin;
         mDefDelimiters = iDefDelimiters;
         mGetValue = true;

         try
         {
            parseElement(iElement);   
         }
         catch ( IllegalArgumentException iae )
         {
            throw iae;
         }
      }
      else
      {
         throw new NullPointerException("Element name not specified");
      }
   }

   /**
    *  Parses a SCORM API <code>getValue()</code> request.
    * 
    * @param iElement       The dot-notation bound data model element name
    *                       (parameter #1) being requested.
    *
    * @exception NullPointerException
    *                   If the element name is omitted.
    * @exception IllegalArgumentException
    *                   If the element name is improperly formatted.
    */
   public DMRequest(String iElement) throws NullPointerException, 
                                            NumberFormatException
   {

      if ( iElement != null )
      {
         mGetValue = true;

         try
         {
            parseElement(iElement);   
         }
         catch ( NumberFormatException nf )
         {
            throw nf;
         }
      }
      else
      {
         throw new NullPointerException();
      }
   }

   /**
    *  Parses a SCORM API <code>setValue()</code> request.
    * 
    * @param iElement       The dot-notation bound data model element name
    *                       (parameter #1) being requested.
    * 
    * @param iValue         The dot-notation bound value (parameter #2) to be  
    *                       applied to the data model element
    *
    * @param iAdmin         Describes if this request is an administrative
    *                       action.
    *    
    * @exception NullPointerException
    *                   If the element name is omitted.
    * @exception NumberFormatException
    *                   If the element name is improperly formatted.
    */
   public DMRequest(String iElement,
                    String iValue,
                    boolean iAdmin) throws NullPointerException,
                                           NumberFormatException
   {
      if ( iElement != null )
      {
         mAdmin = iAdmin;

         try
         {
            parseElement(iElement);
            parseValue(iValue);
         }
         catch ( NumberFormatException nf )
         {
            throw nf;
         }
      }
      else
      {
         throw new NullPointerException();
      }
   }

   /**
    *  Parses a SCORM API <code>setValue()</code> request.
    * 
    * @param iElement       The dot-notation bound data model element name
    *                       (parameter #1) being requested.
    * 
    * @param iValue         The dot-notation bound value (parameter #2) to be  
    *                       applied to the data model element
    *    
    * @exception NullPointerException
    *                   If the element name is omitted.
    * @exception NumberFormatException
    *                   If the element name is improperly formatted.
    */
   public DMRequest(String iRequest, 
                    String iValue) throws NullPointerException,
                                          NumberFormatException
   {
      if ( iRequest != null )
      {
         try
         {
            parseElement(iRequest);
            parseValue(iValue);
         }
         catch ( NumberFormatException nf )
         {
            throw nf;
         }
      }
      else
      {
         throw new NullPointerException();
      }
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Provides the 'current' token of this request without moving to the 'next'
    * token.
    * 
    * @return The current token (<code>DMToken</code>) referenced by this
    *         request or <code>null</code> if the current token is undefined.
    */
   public RequestToken getCurToken()
   {
      // Assume no token is defined
      RequestToken token = null;

      if ( mTokens != null )
      {
         // Make sure the current token is defined
         if ( mCurToken < mTokens.size() )
         {
            token = (RequestToken)mTokens.elementAt(mCurToken);
         }
      }

      return token;
   }

   /**
    * Provides the (zero-based) index of the 'current' token of this request 
    * without moving to the 'next' token.
    * 
    * @return The index of the current token referenced by this request or 
    *         <code>NO_MORE_TOKENS</code> if the current token is undefined.
    */
   public int getCurTokenCount()
   {
      // Assume the current token is undefined
      int count = NO_MORE_TOKENS;

      if ( mTokens != null )
      {
         // Make sure the current token is defined
         if ( mCurToken < mTokens.size() )
         {
            count = mCurToken;
         }
      }

      return count;
   }

   /**
    * Provides the 'current' token of this request and moves to the 'next'
    * token.
    * 
    * @return The current token (<code>DMToken</code>) referenced by this
    *         request or <code>null</code> if the current token is undefined.
    */
   public RequestToken getNextToken()
   {
      // Assume the current token is undefined
      RequestToken token = null;

      if ( mTokens != null )
      {
         // Make sure the current token is defined
         if ( mCurToken < mTokens.size() )
         {
            token = (RequestToken)mTokens.elementAt(mCurToken);

            // Move to the next token
            mCurToken++;
         }
      }

      return token;
   }

   /**
    * Provides the nth token this request without changing the current token.
    * 
    * @param iIndex The index of the token requested.
    * 
    * @return The token (<code>DMToken</code>) referenced by this request at the
    *         indicated index or <code>null</code> if the token at the index is
    *         undefined.
    */
   public RequestToken getToken(int iIndex)
   {
      // Assume the token will be undefined
      RequestToken token = null;

      if ( mTokens != null )
      {
         // Make sure the index is within the range of the request's tokens     
         if ( iIndex >= 0 && iIndex < mTokens.size() )
         {
            token = (RequestToken)mTokens.elementAt(iIndex);
         }
      }

      return token;
   }

   /**
    * Indicates if there are more tokens, including the current token,
    * available for processing from this request.
    * 
    * @return <code>true</code> if there are more tokens available, otherwise
    *         <code>false</code>.
    */
   public boolean hasMoreTokens()
   {
      // Assume there are no more tokens available
      boolean more = false;

      if ( mTokens != null )
      {
         if ( mCurToken != NO_MORE_TOKENS && mCurToken < mTokens.size() )
         {
            more = true;
         }
      }

      return more;
   }

   /**
    * Indicates if this <code>SetValue()</code> request was invoked as an 
    * administrative action -- without validation of the value.
    * 
    * @return <code>true</code> if this request was a from an adminstrative
    *         <code>SetValue()</code> API method.
    */
   public boolean isAdminRequest()
   {
      return mAdmin;
   }

   /**
    * Indicates if this request was invoked as part of the
    * <code>GetValue()</code> API method.
    * 
    * @return <code>true</code> if this request was from of a
    *         <code>GetValue()</code> API method.
    */
   public boolean isGetValueRequest()
   {
      return mGetValue;
   }

   /**
    * Describes the number of tokens associated with this request.
    * 
    * @return The number of tokens associated with this request or
    *         <code>NO_MORE_TOKENS</code> if the request does not have tokens.
    */
   public int numTokens()
   {

      if ( mTokens != null )
      {
         return mTokens.size();
      }
      else
      {
         return NO_MORE_TOKENS;
      }
   }

   /**
    * Provides a dot notation bound string for the request object
    *
    * @return The dot notation bound string for this request object.
    */
   public String showDotNotation()
   {

      String dot = "";
      Vector dels = null;


      if ( mTokens != null )
      {
         // The tokens are ordered, so loop through the list and add appropriate
         // annotation to form the correct dot notation bound string
         for ( int i = 0; i < mTokens.size(); i++ )
         {
            // Get the next token
            RequestToken tok = (RequestToken)mTokens.elementAt(i);

            // Depending on the token type, add additional annotation
            switch ( tok.getType() )
            {
               case RequestToken.TOKEN_ARGUMENT:

                  // Add the final '.' 
                  dot = dot + ".";

                  // Loop the set of defined delimiters, adding each in turn
                  dels = tok.getDelimiters();
                  for ( int j = 0; j < dels.size(); j++ )
                  {
                     RequestDelimiter del = 
                     (RequestDelimiter)dels.elementAt(j);
                     dot = dot + del.showDotNotation();
                  }

                  break;

               case RequestToken.TOKEN_DATA_MODEL:

                  // The data model token must come first, no '.' needed
                  dot = dot + tok.getValue();

                  break;

               case RequestToken.TOKEN_ELEMENT:
               case RequestToken.TOKEN_INDEX:

                  // Simply add the '.' and the value
                  dot = dot + "." + tok.getValue();

                  break;


               case RequestToken.TOKEN_VALUE:

                  // Add the ',' that seperates the element being set from 
                  // the value to be set
                  dot = dot + ",";

                  // Loop the set of defined delimiters, adding each in turn
                  dels = tok.getDelimiters();
                  if ( dels != null )
                  {
                     for ( int j = 0; j < dels.size(); j++ )
                     {
                        RequestDelimiter del = 
                        (RequestDelimiter)dels.elementAt(j);
                        dot = dot + del.showDotNotation();
                     }                    
                  }

                  // Add the value
                  dot = dot + tok.getValue();

                  break;


               default:

                  // This is an error, the request was not parsed correctly
                  ;
            }
         }
      }

      return dot;
   }

   /**
    * Indicates if the result of a <code>GetValue()</code> request should
    * include the data models's default delimiters, if the data model has
    * delimiters.
    * 
    * @return <code>true</code> if the result of a <code>GetValue()</code>
    *         request should include default delimiters.
    */
   public boolean supplyDefaultDelimiters()
   {
      return mDefDelimiters;
   }

   /**
    * Moves the current token to the first token associated with this request.
    */
   public void reset()
   {
      // Make sure the request is valid before resetting it
      if ( mTokens != null )
      {
         mCurToken = 0;
      }
      else
      {
         mCurToken = NO_MORE_TOKENS;
      }
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Private Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Parses the element name portion of a dot-notation bound string.
    * 
    * @param iElement The dot-notation bound string referencing a data model
    *                 element.
    * 
    * @exception IllegalArgumentException
    *                   If the data model token is specified as an index.
    */
   private void parseElement(String iElement) throws IllegalArgumentException
   {

      // Initiazlize this request's set of tokens
      mTokens = new Vector();
      mCurToken = 0;

      RequestToken tok = null;

      // Parse the element string on '.'s
      StringTokenizer st = new StringTokenizer(iElement, ".");

      // The first token must be the data model designator -- not an integer
      String name = null;
      try
      {
         name = st.nextToken();
               
         if ( name.length() > 0 )
         {
            tok = new RequestToken(name, RequestToken.TOKEN_DATA_MODEL);
            mTokens.add(tok);           
         }
         else
         {
            throw new IllegalArgumentException("Empty token");
         }
      }
      catch ( Exception e )
      {
         throw new IllegalArgumentException("Data Model improperly specified");
      }

      // Walk the rest of the tokens for this request
      while ( st.hasMoreTokens() )
      {
         name = st.nextToken();

         try
         {
            if ( name.length() > 0 )
            {

               // Attempt to parse the token
               tok = new RequestToken(name, false);

            }
            else
            {
               throw new IllegalArgumentException("Empty token");
            }
         }
         catch ( Exception e )
         {
            throw new IllegalArgumentException("Element improperly specified");
         }

         // Add the new token to the request
         mTokens.add(tok);
      }
   }

   /**
    * Parses the value portion of a dot notation bound string.
    * 
    * @param iValue The dot notation bound string referencing the value to be
    *               applied to some data model element.
    */
   private void parseValue(String iValue)
   {

      // Make sure the set tokens has been initialized
      if ( mTokens != null )
      {
         try
         {
            // Build a value token corresponding to this string
            RequestToken tok = new RequestToken(iValue, true);

            // Add the new token to the request
            mTokens.add(tok);
         }
         catch ( NumberFormatException nf )
         {
            throw nf;
         }
      }
   }

} // end DMRequest
