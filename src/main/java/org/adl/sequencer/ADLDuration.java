/*******************************************************************************
**
** Concurrent Technologies Corporation (CTC) grants you ("Licensee") a non-
** exclusive, royalty free, license to use, modify and redistribute this
** software in source and binary code form, provided that i) this copyright
** notice and license appear on all copies of the software; and ii) Licensee
** does not utilize the software in a manner which is disparaging to CTC.
**
** This software is provided "AS IS," without a warranty of any kind.  ALL
** EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
** IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-
** INFRINGEMENT, ARE HEREBY EXCLUDED.  CTC AND ITS LICENSORS SHALL NOT BE LIABLE
** FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
** DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES.  IN NO EVENT WILL CTC  OR ITS
** LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
** INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
** CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
** OR INABILITY TO USE SOFTWARE, EVEN IF CTC HAS BEEN ADVISED OF THE POSSIBILITY
** OF SUCH DAMAGES.
**
*******************************************************************************/

package org.adl.sequencer;

import java.io.Serializable;

import org.adl.util.debug.DebugIndicator;

/**
 * <br><br>
 * 
 * <strong>Filename:</strong> ADLDuration.java<br><br>
 *
 * <strong>Description:</strong>none<br><br>
 * 
 * <strong>Design Issues:</strong>none<br><br>
 * 
 * <strong>Implementation Issues:</strong>none<br><br>
 *
 * 
 * <strong>Known Problems:</strong>none<br><br>
 * 
 * <strong>Side Effects:</strong>none<br><br>
 * 
 * <strong>References:</strong>none<br>
 * <ul>
 *     <li>SCORM 1.3
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class ADLDuration implements Serializable
{

   /**
    * Enumeration of possible relations between two <code>ADLDuration</code>
    * objects.
    * <br>Unknown
    * <br><b>-999</b>
    */
   public final static int UNKNOWN   =  -999;

   /**
    * Enumeration of possible relations between two <code>ADLDuration</code>
    * objects.
    * <br>Less Than
    * <br><b>-1</b>
    */
   public final static int LT   =  -1;

   /**
    * Enumeration of possible relations between two <code>ADLDuration</code>
    * objects.
    * <br>Less Than
    * <br><b>0</b>
    */
   public final static int EQ   =   0;

   /**
    * Enumeration of possible relations between two <code>ADLDuration</code>
    * objects.
    * <br>Greater Than
    * <br><b>1</b>
    */
   public final static int GT   =   1;

   /**
    * Enumeration of possible formats for duration information.
    * <br>Seconds /w one tenth second accuracy
    * <br><b>0</b>
    */
   public final static int FORMAT_SECONDS      =  0;

   /**
    * Enumeration of possible formats for duration information.
    * <br>XML Schema -- Duration Type
    * <br><b>1</b>
    */
   public final static int FORMAT_SCHEMA       =  1;


   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;


   /**
    * The duration being tracked in milliseconds
    */
   public long mDuration = 0;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
  
   Constructors
  
  -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
   public ADLDuration()
   {
      mDuration = 0;
   }

   public ADLDuration(int iFormat, String iValue)
   {

      String hours = null;
      String min = null;
      String sec = null;

      switch ( iFormat )
      {
         
         case FORMAT_SECONDS:

            double secs = 0.0;

            try
            {
               secs = (new Double(iValue)).doubleValue();
            }
            catch ( Exception e )
            {
               if ( _Debug )
               {
                  System.out.print("  Invalid Format ::  " + iFormat +  
                                   " // " + iValue);
               }
            }

            mDuration = (long)(secs * 1000.0);

            break;


         case FORMAT_SCHEMA:

            int locStart = iValue.indexOf('T');
            int loc = 0;

            if ( locStart != -1 )
            {
               locStart++;

               loc = iValue.indexOf("H", locStart);

               if ( loc != -1 )
               {
                  hours = iValue.substring(locStart, loc);
                  mDuration = (new Long(hours)).longValue() * 3600;

                  locStart = loc + 1;
               }

               loc = iValue.indexOf("M", locStart);
               if ( loc != -1 )
               {
                  min = iValue.substring(locStart, loc);
                  mDuration += (new Long(min)).longValue() * 60;

                  locStart = loc + 1;
               }

               loc = iValue.indexOf("S", locStart);
               if ( loc != -1 )
               {
                  sec = iValue.substring(locStart, loc);
                  mDuration += (new Long(sec)).longValue();
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println(" ERROR : Invalid format  --> " +
                                     iValue);
               }
            }

            break;


         default:

      }
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   public String format(int iFormat)
   {

      String out = null;
      String subSec = null;

      long countHours = 0;
      long countMin = 0;
      long countSec = 0;

      long temp = 0;

      switch ( iFormat )
      {
         
         case FORMAT_SECONDS:

            double sec = mDuration / 1000.0;

            out = (new Double(mDuration)).toString();

            break;


         case FORMAT_SCHEMA:

            out = "";

            countHours = 0;
            countMin = 0;
            countSec = 0;

            temp = mDuration / 1000;

            if ( temp >= 1000 )
            {
               if ( temp >= 3600 )
               {
                  countHours = temp / 3600;
                  temp %= 3600;
               }

               if ( temp > 60 )
               {
                  countMin = temp / 60;
                  temp %= 60;
               }

               countSec = temp;
            }

            out = "PT";

            if ( countHours > 0 )
            {
               out += Long.toString(countHours, 10);
               out +="H";
            }

            if ( countMin > 0 )
            {
               out += Long.toString(countMin, 10);
               out +="M";
            }

            if ( countSec > 0 )
            {
               out += Long.toString(countSec, 10);
               out +="S";
            }

            break;


         default:

      }

      return out;
   }


   public void add(ADLDuration iDur)
   {
      mDuration += iDur.mDuration;
   }


   public int compare(ADLDuration iDur)
   {
      int relation = ADLDuration.UNKNOWN;

      if ( mDuration < iDur.mDuration )
      {
         relation = ADLDuration.LT;
      }
      else if ( mDuration == iDur.mDuration )
      {
         relation = ADLDuration.EQ;
      }
      else if ( mDuration > iDur.mDuration )
      {
         relation = ADLDuration.GT;
      }

      return relation;
   }


}  // end ADLDuration
