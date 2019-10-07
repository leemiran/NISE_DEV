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

package com.ziaan.scorm2004.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Provides an abstraction of a message box for relaying imparitive information
 * to the user.  Messages are expressed in four different categories:
 * <ul>
 *    <li>
 *       SEVERE - Message notifying the user that a there is a critical problem
 *                preventing the program from continuing.
 *    </li>
 *    <li>
 *       ERROR - Message notifying the user that there is a substantial problem
 *               that may result in un-expected results.
 *    </li>
 *    <li>
 *       WARNING - Message notifying the user that there is a potential problem
 *                 that may result in un-expected results.
 *    </li>
 *    <li>
 *       INFO - Message notifying the user of imparitive information.
 *    </li>
 * </ul>
 *
 * <strong>Filename:</strong><br>
 * MessageBox.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <CODE>MessageBox</CODE> abstracts the implementation of messages displayed
 * in a dialog box.<br><br>
 *
 *
 * <strong>Design Issues:</strong><br>
 * None<br>
 * <br>
 *
 * <strong>Implementation Issues:</strong><br>
 * None<br><br>
 *
 * <strong>Known Problems:</strong><br>
 * None<br><br>
 *
 * <strong>Side Effects:</strong><br>
 * If the <code>MessageBox.SEVERE</code> option is used, the program will
 * terminate.<br><br>
 *
 * <strong>References:</strong><br>
 * None<br><br>
 *
 * @author ADL Technical Team
 */
public class MessageBox extends JPanel
{
   public static final int SEVERE  = 0;
   public static final int ERROR   = 1;
   public static final int WARNING = 2;
   public static final int INFO    = 3;


   /**
    * This default constructor is private and shall not be used.
    */
   private MessageBox()
   {
   }


   /**
    * This constructor interprets the type of message wanted and executes the
    * appropriate dialog box.<br><br>
    *
    * @param iType   The type of dialog box desired to display the message:
    *               <ol>
    *                  <li><code>SEVERE</code> dialog box.</li>
    *                  <li><code>ERROR</code> dialog box.</li>
    *                  <li><code>WARNING</code> dialog box.</li>
    *                  <li><code>INFO</code> dialog box.</li>
    *               </ol>
    * @param iMsg - The message to be displayed.
    * @param iTtl - The title of this message.
    */
   public MessageBox( int iType, String iMsg, String iTtl )
   {
      switch( iType )
      {
         case SEVERE:
            severeBox( iMsg, iTtl );
            break;
         case ERROR:
            errorBox( iMsg, iTtl );
            break;
         case WARNING:
            warningBox( iMsg, iTtl );
            break;
         case INFO:
            infoBox( iMsg, iTtl );
            break;
         default:
            System.out.println("message type was not specified");
      }
   }


   /**
    * This method displays a dialog box with the ERROR_MESSAGE symbol and exits
    * the program.
    *
    * @param iMsg - The message to be displayed.
    * @param iTtl - The title of this message.
    */
   public void severeBox( String iMsg, String iTtl )
   {
      JOptionPane.showMessageDialog( new JFrame(), iMsg, iTtl,
                                     JOptionPane.ERROR_MESSAGE );
      System.exit(0);
   }


   /**
    * This method displays a dialog box with the ERROR_MESSAGE symbol.
    *
    * @param iMsg - The message to be displayed.
    * @param iTtl - The title of this message.
    */
   public void errorBox( String iMsg, String iTtl )
   {
      JOptionPane.showMessageDialog( new JFrame(), iMsg, iTtl,
                                     JOptionPane.ERROR_MESSAGE );
   }


   /**
    * This method displays a dialog box with the WARNING_MESSAGE symbol.
    *
    * @param iMsg - The message to be displayed.
    * @param iTtl - The title of this message.
    */
   public void warningBox( String iMsg, String iTtl )
   {
      JOptionPane.showMessageDialog( new JFrame(), iMsg, iTtl,
                                     JOptionPane.WARNING_MESSAGE );
   }


   /**
    * This method displays a dialog box with the INFORMATION_MESSAGE symbol.
    *
    * @param iMsg - The message to be displayed.
    * @param iTtl - The title of this message.
    */
   public void infoBox( String iMsg, String iTtl )
   {
      JOptionPane.showMessageDialog( new JFrame(), iMsg, iTtl,
                                     JOptionPane.INFORMATION_MESSAGE );
   }
}
