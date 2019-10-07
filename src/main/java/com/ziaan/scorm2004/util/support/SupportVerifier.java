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

package com.ziaan.scorm2004.util.support;

// Native java imports
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.ziaan.scorm2004.util.EnvironmentVariable;
import com.ziaan.scorm2004.util.MessageBox;

/**
 * Provides the ability to display support errors.  If the software was not
 * tested for support, messages will be displayed in the following manner:
 * <ul>
 *    <li>
 *       Operating System - If the user is running the software on an
 *                          unsupported or untested Operating System, a warning
 *                          is displayed and the user is allowed to continue.
 *    </li>
 *    <li>
 *       Java Version - If the user is running the software with an unsupported
 *                      or untested Java Version, an error is displayed and the
 *                      program is terminated.
 *    </li>
 * </ul>
 *
 * <strong>Filename:</strong><br>
 * SupportVerifier.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <CODE>SupportVerifier</CODE> provides the ability to display support
 * errors.<br><br>
 *
 *
 * <strong>Design Issues:</strong><br>
 * None<br>
 * <br>
 *
 * <strong>Implementation Issues:</strong><br>
 * <br><br>
 *
 * <strong>Known Problems:</strong><br>
 * None<br><br>
 *
 * <strong>Side Effects:</strong><br>
 * If an unsupported Java version is detected, the program will terminate.
 * <br><br>
 *
 * <strong>References:</strong><br>
 * None<br><br>
 *
 * @author ADL Technical Team
 */
public class SupportVerifier
{

   /**
    * Array of supported Java Versions.
    */
   private static final String[] mSupportedJRE = { "1.5.0_02",
                                                   "1.5.0_01",
                                                   "1.5.0",
                                                   "1.4.2_07",
                                                   "1.4.2_06",
                                                   "1.4.2_05",
                                                   "1.4.2_04",
                                                   "1.4.2_03",
                                                   "1.4.2_02",
                                                   "1.4.2_01",
                                                   "1.4.2",
                                                   "1.4.1_07",
                                                   "1.4.1_06",
                                                   "1.4.1_05",
                                                   "1.4.1_04",
                                                   "1.4.1_03",
                                                   "1.4.1_02",
                                                   "1.4.1_01",
                                                   "1.4.1"  };


   /**
    * Array of supported Operating Systems.
    */
   private static final String[] mSupportedOS = { "Windows XP",
                                                  "Windows 2000" };
   public SupportVerifier()
   {
   }

   /**
    * This method is the default constructor of the <code>SupportVerifer</code>
    * class.  It controls all support verification sequences.
    */
   public void verifySupport()
   {
      verifyOSSupport();
      verifyJRESupport();
   }

   /**
    * This method handles all Java Version support verification sequences.
    */
   private void verifyJRESupport()
   {
      String jreVersion = System.getProperty("java.version");

      //System.out.println("jreVersion = \"" + jreVersion + "\"");

      boolean jreSupported = false;
      int arrayLength = mSupportedJRE.length;

      for ( int i = 0; i < arrayLength; i++ )
      {
         if ( jreVersion.equals( mSupportedJRE[i] ) )
         {
            jreSupported = true;
            break;
         }
      }

      if ( ! jreSupported )
      {
         String title = new String("Environment Error");
         String messageText = "The " +
                       "installed\n Java Runtime Environment Version \"" +
                       jreVersion + "\" is not\nsupported.  See the Readme for " +
                       "detailed installation\ninstructions and support " +
                       "information prior to operating\nthe software!";
         MessageBox mb = new MessageBox( MessageBox.WARNING, messageText,
                                         title );
      }
   }

   /**
    * This method handles all Operating System support verification sequences.
    */
   private void verifyOSSupport()
   {
      String osName = System.getProperty("os.name");
      //System.out.println("osName = \"" + osName + "\"");

      //String osVersion = System.getProperty("os.version");
      //System.out.println("osVersion = \"" + osVersion + "\"");

      //String osArch = System.getProperty("os.arch");
      //System.out.println("osArch = \"" + osArch + "\"");

      boolean osSupported = false;
      int arrayLength = mSupportedOS.length;

      for ( int i = 0; i < arrayLength; i++ )
      {
         if ( osName.equalsIgnoreCase( mSupportedOS[i] ) )
         {
            osSupported = true;
            break;
         }
      }

      if ( ! osSupported )
      {
         String title = new String("Environment Warning");
         String messageText = "Support for the \"" + osName + "\" Operating " +
                       "System has not been \ntested.  Use of this " +
                       "Operating System is permitted, however ADL will\n" +
                       "not offer support.  See the Readme for detailed " +
                       "installation\n instructions and support information " +
                       "prior to operating the\nsoftware!";
         MessageBox mb = new MessageBox( MessageBox.WARNING, messageText, title );
      }
   }

   /**
    *
    */
   public void verifyEnvironmentVariable( String iKey )
   {
      String value = EnvironmentVariable.getValue( iKey );

      //System.out.println( iKey + " = \"" + value + "\"");

      if ( value.equals("") )
      {
         String title = new String("Environment Error");
         String messageText = "The \"" + iKey + "\" Environment Variable could " +
                              "not be detected.  This Environment\n Variable " +
                              "must be set correctly for successful " +
                              "operation of this software";
         MessageBox mb = new MessageBox( MessageBox.ERROR, messageText, title );
      }
   }


   /**
    * This method handles all Java Version support verification sequences.
    */
   public boolean verifyJRESupportBoolean()
   {
      String jreVersion = System.getProperty("java.version");

      boolean jreSupported = false;
      int arrayLength = mSupportedJRE.length;

      for ( int i = 0; i < arrayLength; i++ )
      {
         if ( jreVersion.equals( mSupportedJRE[i] ) )
         {
            jreSupported = true;
            break;
         }
      }

      return jreSupported;
   }

   /**
    * This method handles all Operating System support verification sequences.
    */
   public boolean verifyOSSupportBoolean()
   {
      String osName = System.getProperty("os.name");

      boolean osSupported = false;
      int arrayLength = mSupportedOS.length;

      for ( int i = 0; i < arrayLength; i++ )
      {
         if ( osName.equalsIgnoreCase( mSupportedOS[i] ) )
         {
            osSupported = true;
            break;
         }
      }

      return osSupported;
   }

   /**
    * This method handles all Operating System support verification sequences.
    */
   public String getCurrentOS()
   {
      String osName = System.getProperty("os.name");
      PrivilegedGetSP psp = new PrivilegedGetSP("sun.os.patch.level");
      
      String patch = (AccessController.doPrivileged(psp)).toString();
      String abbreviatedPatch = patch.replaceAll("Service Pack", "SP");
      String os = osName + " - " + abbreviatedPatch;
      

      return os;
   }

   /**
    * This method handles all Java Version support verification sequences.
    */
   public String getCurrentJRE()
   {
      String jreVersion = System.getProperty("java.version");

      return jreVersion;
   }

   private class PrivilegedGetSP implements PrivilegedAction
   {
      String mSPKey;
      Object oSPValue;

      /**
       * Constructor of the inner class
       *
       * @param iMsgType -
       *
       * @param iMsgText -
       *
       */
      PrivilegedGetSP( String iSPKey )
      {
         mSPKey     = iSPKey;
      }

      /**
       *
       * This run method grants privileged applet code access to write
       * to the summary log.  This allows the applet to work in Netscape 6.
       *
       * @return Object
       *
       */
      public Object run()
      {
         try
         {
            oSPValue = System.getProperty(mSPKey);
         }
         catch(Exception e)
         {
            e.printStackTrace();
         }

         return oSPValue;
      }
   }
}
