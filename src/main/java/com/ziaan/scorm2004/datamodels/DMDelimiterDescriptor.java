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

package com.ziaan.scorm2004.datamodels;


import java.io.Serializable;


/**
 * Encapsulation of information required for processing a data model request.
 * <br><br>
 * 
 * <strong>Filename:</strong> DMDelimiterDescriptor.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * Encapsulation of all information required to describe one dot-notation bound
 * delimiter.  This information will be used to create instances of delimiters
 * assocaited with data model elements.<br><br>
 * 
 * <strong>Design Issues:</strong><br><br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * All fields are purposefully public to allow immediate access to known data
 * elements.<br><br>
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
public class DMDelimiterDescriptor implements Serializable 
{

   /**
    * Describes the name of this delimiter
    */
   public String mName = null;


   /**
    * Describes if the default value of this delimiter
    */
   public String mDefault = null;


   /**
    * Describes the SPM for the value.
    */
   public int mValueSPM = -1;


   /**
    * Describes the method used to validate the value of this delimiter.
    */
   public DMTypeValidator mValidator = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   public DMDelimiterDescriptor(String iName, 
                                String iDefault,
                                DMTypeValidator iValidator)
   {
      mName = iName;
      mDefault = iDefault;
      mValidator = iValidator;
   }


   public DMDelimiterDescriptor(String iName, 
                                String iDefault,
                                int iValueSPM,
                                DMTypeValidator iValidator)
   {
      mName = iName;
      mDefault = iDefault;
      mValueSPM = iValueSPM;
      mValidator = iValidator;
   }


}  // end DMDelimiterDescriptor
