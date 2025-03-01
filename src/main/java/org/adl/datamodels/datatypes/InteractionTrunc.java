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
*******************************************************************************/

package org.adl.datamodels.datatypes;


import org.adl.datamodels.*;

import java.io.Serializable;
import java.util.*;


/**
 * Provides support for the SCORM Data Model Interaction data types, as defined 
 * in the SCORM 2004.<br><br>
 * 
 * <strong>Filename:</strong> InteractionTrunc.java<br><br>
 * 
 * <strong>Description:</strong><br><br>
 * 
 * <strong>Design Issues:</strong><br><br>
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
public class InteractionTrunc implements Serializable
{

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Truncates all parts of an interaction datatype to their SPMs
    * 
    * @param iValue    The value being truncated
    * 
    * @param iType     The type of the value being truncated
    * 
    * @return Returns the Truncated value
    */
   public static String trunc(String iValue, int iType)
   {
      String trunc = new String("");

      // SCORM defined separators
      String comma  = new String("\\[,\\]");   
      String period = new String("\\[.\\]");
      String colon  = new String("\\[:\\]");

      int idx = -1;

      // Swith on the interaction type
      switch ( iType )
      {
         case InteractionValidator.MULTIPLE_CHOICE :

            // Check for an empty set
            if ( iValue.trim().length() == 0 )
            {
               // Value OK
               break;
            }

            String choices[] = iValue.split(comma);
            trunc = "";

            // Check to determine if each choice is within the SPM range  
            for ( int i = 0; i < 36; i++ )
            {
               if ( choices[i].length() > 250 )
               {
                  trunc = trunc + choices[i].substring(0, 250);
               }
               else
               {
                  trunc = trunc + choices[i];
               }

               if ( i != 35 )
               {
                  trunc = trunc + "[,]";
               }
            }

            break;

         case InteractionValidator.FILL_IN :

            // Extract each part of the match_text
            String match_text[] = iValue.split(comma);
            trunc = "";

            for ( int i = 0; i < 10; i++ )
            {
               String matchString = null;
               String langString = null;

               // Look for the 'lang' delimiter
               if ( match_text[i].startsWith("{lang=") )
               {
                  // Find the closing '}'
                  idx = match_text[i].indexOf('}');
                  if ( idx != -1 )
                  {
                     matchString = match_text[i].substring(idx + 1);
                     langString = match_text[i].substring(6, idx);
                  }
                  else
                  {
                     matchString = match_text[i];
                  }
               }
               else
               {
                  matchString = match_text[i];
               }

               if ( langString.length() > 250)
               {
                  trunc = trunc + "{lang=" + 
                     langString.substring(0, 250) + "}";
               }
               else
               {
                  trunc = trunc + "{lang=" + 
                     langString + "}"; 
               }


               if ( matchString.length() > 250 )
               {               
                  trunc = trunc + matchString.substring(0, 250);
               }
               else
               {
                  trunc = trunc + matchString;
               }

               if ( i != 9 )
               {
                  trunc = trunc + "[,]";
               }
            }

            break;

         case InteractionValidator.LONG_FILL_IN :

            if ( iValue.length() > 4000 )
            {
               trunc = iValue.substring(0, 4000);
            }
            else
            {
               trunc = iValue;
            }

            break;

         case InteractionValidator.LIKERT :

            if ( iValue.length() > 250 )
            {
               trunc = iValue.substring(0, 250);
            }

            break;

         case InteractionValidator.MATCHING :

            if ( iValue.trim().length() == 0 )
            {
               // Value OK
               break;
            }

            String commas[] = iValue.split(comma);
            trunc = "";

            for ( int i = 0; i < 36; i++ )
            {
               idx = commas[i].indexOf("[.]");

               String target = commas[i].substring(0, idx);
               String source = commas[i].substring(idx + 3, 
                                                   commas[i].length());

               if ( target.length() > 250 )
               {
                  trunc = trunc + target.substring(0, 250);
               }
               else
               {
                  trunc = trunc + target;
               }

               trunc = trunc + "[.]";

               if ( source.length() > 250 )
               {
                  trunc = trunc + source.substring(0, 250);  
               }
               else
               {
                  trunc = trunc + source;
               }
                 
               if ( i != 35 )
               {
                  trunc = trunc + "[,]";
               }
            } 

            break;

         case InteractionValidator.PERFORMANCE : 

            String commaCheck[] = iValue.split(comma);  
            trunc = "";

            for ( int i = 0; i < 125; i++ )
            {
               idx = commaCheck[i].indexOf("[.]");

               String sn = commaCheck[i].substring(0, idx);
               String sa = commaCheck[i].substring(idx + 3, 
                              commaCheck[i].length());


               if ( sn.length() > 250 )
               {
                  trunc = trunc + sn.substring(0, 250);
               }
               else
               {
                  trunc = trunc + sn;
               }

               trunc = trunc + "[.]";

               if ( sa.length() > 250 )
               {
                  trunc = trunc + sa.substring(0, 250);  
               }
               else
               {
                  trunc = trunc + sa;
               } 

               if ( i != 124 )
               {
                  trunc = trunc + "[,]";
               }
            } 

            break;

         case InteractionValidator.SEQUENCING :

            String array[] = iValue.split(comma);
            trunc = "";
        
            for ( int i = 0; i < 36; i++ )
            {
 
               if ( array[i].length() > 250 )
               {
                  trunc = trunc + array[i].substring(0, 250);
               }
               else
               {
                  trunc = array[i];
               }

               if ( i != 35 )
               {
                  trunc = trunc + "[,]";
               }
            }

            break;

         case InteractionValidator.NUMERIC :  

            trunc = iValue;

            break;
      }

      return trunc;
   }

} // end InteractionTrunc
