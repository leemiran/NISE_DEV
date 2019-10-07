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
package org.adl.util;

// native java imports
import java.lang.Process;
import java.lang.InterruptedException;
import java.lang.String;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Serializable;

// xerces imports

// adl imports

/**
 * Intro
 *
 * <strong>Filename:</strong><br>
 * EnvironmentVariable.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <CODE>EnvironmentVariable</CODE> is used to access a specified
 * environment variable.<br><br>
 *
 *
 * <strong>Design Issues:</strong><br>
 * None<br>
 * <br>
 *
 * <strong>Implementation Issues:</strong><br>
 * The getValue() method is Windows implementation specific<br><br>
 *
 * <strong>Known Problems:</strong><br>
 * None<br><br>
 *
 * <strong>Side Effects:</strong><br>
 * None<br><br>
 *
 * <strong>References:</strong><br>
 * None<br><br>
 *
 * @author ADL Technical Team<br>
 */
public final class EnvironmentVariable implements Serializable
{
   /**
    * Retrieves the value of the specified environment variable.<br>
    *
    * @param iKey   Name of the environment variable.<br>
    *
    * @return Value of the specified environment variable.<br>
    */
   public static String getValue( String iKey )
   {
      String value = new String();

      try
      {
         Process p;
         String osName = System.getProperty("os.name");

         if ((osName.equalsIgnoreCase( "Windows 95" )) ||
             (osName.equalsIgnoreCase( "Windows 98" )) ||
             (osName.equalsIgnoreCase( "Windows Me" )))
         {
            p = Runtime.getRuntime().exec("command.com /c echo %" +
                                                  iKey + "%");
         }
         else
         {
            p = Runtime.getRuntime().exec("cmd.exe /c echo %" +
                                                  iKey + "%");
         }

         p.waitFor();
         BufferedReader br = new BufferedReader
              ( new InputStreamReader( p.getInputStream() ) );
         value = br.readLine();

         if ( value.startsWith("%") )
         {
            value = "";
         }

         br.close();
         p.destroy();
      }
      catch ( IOException ioe )
      {
         System.out.println("Could not read environment variable key " + iKey);
      }
      catch ( InterruptedException ie )
      {
         System.out.println("The process has been interrupted");
      }

      return value;
   }
}
