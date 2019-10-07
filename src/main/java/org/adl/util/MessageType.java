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
package org.adl.util;

import java.io.Serializable;

// Native java imports

// Xerces imports

// ADL imports

/**
 * <strong>Filename: </strong><br>Message.java<br><br>
 *
 * <strong>Description:</strong><br> The <CODE>MessageType </CODE> data
 * structure serves as the classification system for the messages.
 * This data structure determines which  <br><br>
 *
 * <strong>Design Issues: </strong>None<br><br>
 *
 * <strong>Implementation Issues: </strong>None<br><br>
 *
 * <strong>Known Problems: </strong>None<br><br>
 *
 * <strong>Side Effects: </strong>None<br><br>
 *
 * <strong>References: </strong>None<br><br>
 *
 * @author ADL Technical Team
 */
public class MessageType implements Serializable
{
   // Public Data Members
   public static int INFO       = 0;
   public static int WARNING    = 1;
   public static int PASSED     = 2;
   public static int FAILED     = 3;
   public static int TERMINATE  = 4;
   public static int CONFORMANT = 5;
   public static int OTHER      = 9;

   public static String _INFO       = "0";
   public static String _WARNING    = "1";
   public static String _PASSED     = "2";
   public static String _FAILED     = "3";
   public static String _TERMINATE  = "4";
   public static String _CONFORMANT = "5";
   public static String _OTHER      = "9";
}
