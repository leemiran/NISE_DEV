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
package com.ziaan.scorm2004.util.debug;

// native java imports
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * <strong>Filename: </strong><br>
 * ADLSimpleFormatter.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <code>ADLSimpleFormatter</code> extends Java's SimpleFormatter class and
 * overrides that class's format function.  This is so we can modify the
 * messages that are output using Java's logging output messages.  Specifically, we do
 * not want the  date/timestamp written on each and every message
 * written to the Console.<br>
 *
 * <strong>Design Issues:</strong><br>
 * None<br>
 * <br>
 *
 * <strong>Implementation Issues:</strong><br>
 * The configure() method must be called to instantiate the logger for the
 * log management/handling properties to be read from the properties file
 * and initialized.<br><br>
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
public class ADLSimpleFormatter extends SimpleFormatter
{
   private String lineSeparator = (String) java.security.AccessController.
      doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

   /**
    * Overrides SimpleFormatter format function.  Writes the output without
    * displaying the date/timestamp.
    */
   public synchronized String format(LogRecord record)
   {
      StringBuffer sb = new StringBuffer();

      if (record.getSourceClassName() != null)
      {
         sb.append(record.getSourceClassName());
      }
      else
      {
         sb.append(record.getLoggerName());
      }
      if (record.getSourceMethodName() != null)
      {
         sb.append(" ");
         sb.append(record.getSourceMethodName());
      }
      sb.append(" ");

      String message = formatMessage(record);
      sb.append(record.getLevel().getLocalizedName());
      sb.append(": ");
      sb.append(message);
      sb.append(lineSeparator);

      return sb.toString();
   }
}
