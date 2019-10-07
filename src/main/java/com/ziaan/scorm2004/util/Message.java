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
package com.ziaan.scorm2004.util;

// Native java imports

/**
 *
 * <strong>Filename:</strong><br> Message.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <CODE>Message</CODE> stores messages classified by the
 * <CODE>MessageType</CODE> class.<br><br>
 *
 *
 * <strong>Design Issues:</strong> None<br><br>
 *
 * <strong>Implementation Issues:</strong> None<br><br>
 *
 * <strong>Known Problems:</strong> None<br><br>
 *
 * <strong>Side Effects:</strong> None<br><br>
 *
 * <strong>References:</strong> None<br><br>
 *
 * @author ADL Technical Team
 */
public class Message extends Object
{
   /**
    * This attribute holds the type of message classified by the
    * <code>MessageType</code> class. Valid values include:<br>
    *  0 = INFO,<br>
    *  1 = WARNING, <br>
    *  2 = PASSED, <br>
    *  3 = FAILED, <br>
    *  4 = TERMINATE,<br>
    *  5 = CONFORMANT,and <br>
    *  9 = OTHER <br>
    */
   private int mMessageType;

   /**
    * This attribute holds the actual text to be communicated by the message.
    * <br>
    */
   private String mMessageText;

   /**
    * This attribute holds the location of where the message originated. <br>
    *
    * @deprecated This attribute is to be removed for the Beta 1 development
    * phase
    */
   private String mMessageLocation;


   /**
    * Default Constructor.  Initializes the attributes of this class.<br>
    */
   private Message()
   {
      mMessageType     = MessageType.INFO;
      mMessageText     = new String("");
      mMessageLocation = new String("");  //remove this
   }

   /**
    * This constructor initializes the message type and message text attributes
    * to the specified values.<br>
    *
    * @param iMessageType - The type of message this is. Typically, this
    * should be "INFORMATION", "WARNING", or "ERROR", but this is up to the
    * client.<br>
    *
    * @param iMessageText - The actual error message text.
    *
    * @since Version 1.3 Beta 1
    */
   public Message( int iMessageType, String iMessageText )
   {
      mMessageType     = iMessageType;
      mMessageText     = iMessageText;
   }

   /**
    * This constructor initializes the message type, message text, and
    * message location attributes to the specified values.<br>
    *
    * @param iTheMessageType
    *               - The type of message this is. Typically, this
    *               should be "INFORMAITON", "WARNING", or "ERROR",
    *               but this is up to the client.
    * @param iTheMessageText
    *               - The actual error message text.
    * @param iTheMessageLocation
    *               - An indicator of where the message is
    *               being generated from in the client code.
    * @param iTheMessageTrace
    *
    * @deprecated This method is to be removed for the Beta 1 development
    *             phase.
    */
   public Message( int iTheMessageType, String iTheMessageText,
                   String iTheMessageLocation )
   {
      mMessageType     = iTheMessageType;
      mMessageText     = iTheMessageText;
      mMessageLocation = iTheMessageLocation;
   }


   /**
    * Method: getMessageType()
    *
    * This accessor returns the message type.
    *
    * @return int - The message type classified by the <code>MessageType</code>
    * class.
    */
   public int getMessageType()
   {
      return mMessageType;
   }

   /**
    * Method: getMessageText()
    *
    * This accessor returns the message text.
    *
    * @return String - The message text.
    */
   public String getMessageText()
   {
      return mMessageText;
   }

   /**
    * Method: getMessageLocation()
    *
    * This accessor returns the message location.
    *
    * @return String - The message location.
    *
    * @deprecated This method is to be removed for the Beta 1 development
    *             phase.
    */
   public String getMessageLocation()
   {
      return mMessageLocation;
   }

   /**
    * Method: toString()
    *
    * This method returns a representation of this message in a
    * predefined string form.
    *
    * Overloads the toString() method of the java.lang.Object class
    *
    * @return String - The message.
    */
   public String toString()
   {
      String  result = new String("");

      if ( mMessageType == MessageType.INFO )
      {
         result = "INFO";
      }
      else if ( mMessageType == MessageType.WARNING )
      {
         result = "WARNING";
      }
      else if ( mMessageType == MessageType.PASSED )
      {
         result = "PASSED";
      }
      else if ( mMessageType == MessageType.FAILED )
      {
         result = "FAILED";
      }
      else // if ( mMessageType == MessageType.OTHER )
      {
         result = "OTHER";
      }

      result = result + " : " + mMessageLocation + " : " + mMessageText;

      return result;
   }
}
