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
import java.util.Vector;


/**
 * Encapsulation of information required for processing a data model request.
 * <br><br>
 * 
 * <strong>Filename:</strong> DMElementDescriptor.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * Encapsulation of all information required to describe a data model element.
 * This information will be used to create instances of the data model element
 * during runtime.<br><br>
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
public class DMElementDescriptor implements Serializable, Cloneable  
{

   /**
    * Describes the dot-notation binding of the data model element.
    */
   public String mBinding = null;


   /**
    * Describes the children of the element
    */
   public Vector mChildren = null;


   /**
    * Describes if the data model element's valud can be read.
    */
   public boolean mIsReadable = true;


   /**
    * Describes if the data model element's valud can be written.
    */
   public boolean mIsWriteable = true;


   /**
    * Describes the set of delimiters available to this element.
    */
   public Vector mDelimiters = null;


   /**
    * Describes the initial value for this element.
    */
   public String mInitial = null;


   /**
    * Describes an order dependency of this data model element in relation to 
    * its siblings.
    */
   public Vector mDependentOn = null;


   /** 
    * Describes if the value of this data model element is unique within its
    * container.
    */
   public boolean mIsUnique = false;

   /**
    * Describes the SPM for the value.
    */
   public int mValueSPM = -1;

   /**
    * Describes the SPM number of children records that should be allowed.
    */
   public int mSPM = -1;


   /**
    * Describes the old SPM of the number of child records, after one exists.
    */
   public int mOldSPM = -1;


   /**
    * Describes the SPM number of children records is an absolute maximum 
    **/
   public boolean mMaximum = false;


   /**
    * Describes if the _children keyword should be allowed for this collection 
    **/
   public boolean mShowChildren = true;



   /**
    * Describes the method used to validate the value of the data model element.
    */
   public DMTypeValidator mValidator = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


   public DMElementDescriptor(String iBinding, Vector iChildren, int iSPM)
   {
      mBinding = iBinding;
      mChildren = iChildren;
      mSPM = iSPM;
   }


   public DMElementDescriptor(String iBinding, Vector iChildren)
   {
      mBinding = iBinding;
      mChildren = iChildren;
   }


   public DMElementDescriptor(String iBinding,
                              String iInitial,
                              DMTypeValidator iValidator)
   {
      mBinding = iBinding;
      mInitial = iInitial;
      mValidator = iValidator;
   }


   public DMElementDescriptor(String iBinding,
                              String iInitial,
                              int iValueSPM,
                              DMTypeValidator iValidator)
   {
      mBinding = iBinding;
      mInitial = iInitial;
      mValueSPM = iValueSPM;
      mValidator = iValidator;
   }


   public Object clone() {
      try
      {
         return super.clone();
      }
      catch ( CloneNotSupportedException e )
      {
         throw new InternalError(e.toString());
      }
   }


}  // end DMElementDescriptor
