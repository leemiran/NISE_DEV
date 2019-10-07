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

// Native java imports
import java.io.Serializable;
import java.lang.Object;
import java.util.Collection;
import java.util.Vector;
import java.lang.String;
import java.lang.Integer;


// ADL imports
import org.adl.util.MessageType;

/**
 *
 * <strong>Filename:</strong>
 * MessageCollection.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <CODE>MessageCollection</CODE> handles collections of <code>Message</code>
 * objects.  This class is implemented as a singleton so that it can be created
 * only once and accessed at anytime.<br>
 *
 * <br><strong>Design Issues:</strong><br>
 * Designed as a singleton so multiple objects can use the same instance<br><br>
 *
 * <strong>Implementation Issues:</strong><br>
 * The <code>Vector</code> data structure is used to take advantage of its
 * ordered incremental addition of elements, growable array and capacity
 * and synchronized access capabilities.<br><br>
 *
 * <strong>Known Problems:</strong> none<br><br>
 *
 * <strong>Side Effects:</strong> none<br><br>
 *
 * <strong>References:</strong> none<br><br>
 *
 * @author ADL Technical Team
 */
public class MessageCollection implements Serializable
{

   /**
    * Describes if 'add' methods should accept new messages to the collection.
    */
   private boolean mAccept = true;

   /**
    * The one and only instance of the <code>MessageCollection</code>.
    */
   private static MessageCollection mInstance = null;

   /**
    * The collection of stored Message objects.
    */
   private Vector mMessages;

   /**
    * A list of MessageTypes the user wants filtered on.
    */
   private Vector mMessageTypeProperties;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Constructors
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Default Constructor.  Initializes the attributes of this class.  This is
    * declared as private to ensure that an instance can not be created by
    * anyone but itself.
    */
   private MessageCollection()
   {
      mMessages = new Vector();
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Appends the specified <code>Message</code> object to the end of the
    * collection.  The Message will be added whether or not the
    * MessageTypeProperties filter is set.
    *
    * @param iMessage The <code>Message</code> object to be appended to this
    *                 collection.
    *
    * @return true if the collection changed as a result of the call.
    */
   public boolean add(Message iMessage)
   {
      if ( mAccept )
      {
         return mMessages.add(iMessage);

      }
      else
      {
         return mAccept;
      }
   }

   /**
    *
    * Appends all of the elements in the specified Collection to the end
    * of this collection, in the order that they are returned by the
    * specified Collection's Iterator.
    *
    * <strong>Side Effects:</strong><br>
    * The behavior of this operation is undefined if the specified Collection is
    * modified while the operation is in progress. (This implies that the
    * behavior of this call is undefined if the specified Collection is this
    * Vector, and this Vector is nonempty.)<br><br>
    *
    * @param iMessages elements to be appended to this collection.
    *
    * @return true if the collection changed as a result of the call.
    */
   public boolean add(Collection iMessages)
   {
      boolean result = false;

      if ( mAccept )
      {

         result = true;                                      
         Vector messages = new Vector(iMessages);
         int messagesSize = messages.size();
         int messageTypePropertiesSize = mMessageTypeProperties.size();
         Message currentMessage;
         int currentMessageType;
         int messageType;

         // cycle through the given collection of Message's sequentially
         for ( int i = 0; i < messagesSize; i++ )
         {
            currentMessage = (Message)messages.get(i);
            currentMessageType = currentMessage.getMessageType();

            // cycle through the wanted MessageTypes and add them to the
            // collection
            for ( int j = 0; j < messageTypePropertiesSize; j++ )
            {

               messageType = 
               ((Integer)mMessageTypeProperties.get(j)).intValue();

               if ( currentMessageType == messageType )
               {
                  result = add(currentMessage) && result;
                  break;
               }
            }
         }
      }

      return result;
   }

   /**
    * Method: addAll()
    *
    * Appends all of the elements in the specified Collection to the end
    * of this collection, in the order that they are returned by the
    * specified Collection's Iterator.<br>
    *
    * <br><strong>Side Effects:</strong><br>
    * The behavior of this operation is undefined if the specified Collection is
    * modified while the operation is in progress. (This implies that the
    * behavior of this call is undefined if the specified Collection is this
    * Vector, and this Vector is not empty.)<br><br>
    *
    * @param iMessages elements to be appended to this collection.
    *
    * @return true if the collection changed as a result of the call.
    */
   public boolean addAll(Collection iMessages)
   {
      if ( mAccept )
      {
         return mMessages.addAll(iMessages);
      }
      else
      {
         return mAccept;
      }
   }

   /**
    * Returns the <code>Message</code> at the specified position in this
    * collection.
    *
    * @param iIndex index of element to return.
    *
    * @return <code>Message</code> at the specified index.
    */
   public Message get( int iIndex )
   {
      return(Message)mMessages.get( iIndex );
   }

   /**
    *
    * Returns a <code>Collection</code> of the Messages based on the
    * <code>MessageType</code> properties set within this object.<br>
    *
    * <br><strong>Side Effects:</strong><br>
    * The <code>MessageTypeProperties</code> will be empty after this call
    * returns (unless it throws an exception).<br><br>
    *
    * @return <code>Collection</code> of <code>Messages</code>
    */
   public Collection getByType()
   {
      Vector resultMessages = new Vector();
      int messagesSize = mMessages.size();
      int messageTypePropertiesSize = mMessageTypeProperties.size();
      Message currentMessage;
      int currentMessageType;
      int messageType;
      boolean result = true;

      // cycle through the mMessages and get the Messages sequentially
      for ( int i = 0; i < messagesSize; i++ )
      {
         currentMessage = (Message)mMessages.get(i);
         currentMessageType = currentMessage.getMessageType();

         // cycle through the wanted MessageTypes and add then to the
         // resultMessages
         for ( int j = 0; j < messageTypePropertiesSize; j++ )
         {
            messageType = ((Integer)mMessageTypeProperties.get(j)).intValue();

            if ( currentMessageType == messageType )
            {
               result = resultMessages.add(currentMessage) && result;
               break;
            }
         }
      }

      if ( result )
      {
         clearMessageTypeProperties();
      }

      return resultMessages;
   }

   /**
    * Appends a MessageType to the properties list.<br>
    *
    * <br><strong>Side Effects:</strong><br>
    * None<br><br>
    *
    * @param iMessageType type of messages to filter on when the list is
    * accessed.
    *
    * @return true if the properties changed as a result of the call.
    */
   public boolean setMessageTypeProperties( int iMessageType )
   {
      return mMessageTypeProperties.add(new Integer(iMessageType));
   }

   /**
    * Removes all of the <code>MessageType</code>'s from the list of message
    * types.<br>
    *
    * <br><strong>Side Effects:</strong><br>
    * The <code>MessageTypeProperties</code> will be empty after this call
    * returns (unless an exception is encountered).<br><br>
    */
   private void clearMessageTypeProperties()
   {
      mMessageTypeProperties.clear();
   }

   /**
    *
    * Removes all of the <code>Message</code> objects from this collection.
    *
    * <br><strong>Side Effects:</strong><br>
    * The collection will be empty after this call returns (unless it throws an
    * exception).<br><br>
    */
   public void clear()
   {
      // un-pause if currently paused
      mAccept = true;

      mMessages.clear();
   }

   /**
    * Tests to see if this collection has no components.
    *
    * @return true if and only if this vector has no components, that is, its
    * size is zero; false otherwise.
    */
   public boolean isEmpty()
   {
      return mMessages.isEmpty();
   }

   /**
    * Returns the number of components in the collection.
    *
    * @return the number of components in the collection.
    */
   public int size()
   {
      return mMessages.size();
   }

   /**
    * Pauses the collection so that 'add' methods are not accpeted.
    * <br><br>This method does not affect the messages currently in the
    * collection.
    * 
    * @parm iPause  Indicates if the collction should be 'paused'.
    */
   public void pause(boolean iPause)
   {
      mAccept = !iPause;
   }

   /**
    * Returns the instance of the class.
    *
    * @return
    *   The MessageCollection object.
    */
   public static MessageCollection getInstance()
   {
      if ( mInstance == null )
      {
         mInstance = new MessageCollection();
      }

      return mInstance;
   }
}
