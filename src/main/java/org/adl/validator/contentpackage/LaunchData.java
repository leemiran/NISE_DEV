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
package org.adl.validator.contentpackage;

// native java imports

import java.lang.String;
import java.util.Vector;
import java.io.File;
import java.io.Serializable;
import java.util.logging.Logger;

// xerces imports

// adl imports

/**
 *
 * <strong>Filename: </strong><br>LaunchData.java<br><br>
 *
 * <strong>Description: </strong><br> A <CODE>LaunchData</CODE> is a Data
 * Structure used to store information for the Launch Data of SCOs.<br><br>
 *
 * <strong>Design Issues: </strong><br>None<br>
 * <br>
 *
 * <strong>Implementation Issues: </strong><br>None<br><br>
 *
 * <strong>Known Problems: </strong><br>None<br><br>
 *
 * <strong>Side Effects: </strong><br>None<br><br>
 *
 * <strong>References: </strong><br>None<br><br>
 *
 * @author ADL Technical Team
 */

public class LaunchData implements Serializable
{
   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;

   /**
    * The identifier attribute of the <organization> element.<br>
    */
   private String mOrganizationIdentifier; 

   /**
    * The identifier attribute of the <item> element.<br>
    */
   private String mItemIdentifier;

   /**
    * The identifier attribute of the <resource> element.<br>
    */
   private String mResourceIdentifier;

   /**
    * The xml:base attribute of the <manifest> element.<br>
    */
   private String mManifestXMLBase;

   /**
    * The xml:base attribute of the <resources> element.<br>
    */
   private String mResourcesXMLBase;

   /**
    * The xml:base attribute of the <resource> element.<br>
    */
   private String mResourceXMLBase;

   /**
    * The parameter value of the item.<br>
    */
   private String mParameters;

   /**
    * The persistState attribute of an item.
    */
   private String mPersistState;

   /**
    * The location of the item.<br>
    */
   private String mLocation;

   /**
    * The SCORM type of the item (i.e. sco, sca, asset).<br>
    */
   private String mSCORMType;

   /**
    * The title value of the item.<br>
    */
   private String mItemTitle;

   /**
    * The variable representing the datafromlms element of the item.<br>
    */
   private String mDataFromLMS;

   /**
    * The variable representing the timelimitaction element of the item.<br>
    */
   private String mTimeLimitAction;

   /**
    * The variable representing the minNormalizedMeasure element.<br>
    */
   private String mMinNormalizedMeasure;

   /**
    * The variable representing the attemptAbsoluteDurationLimit element.<br>
    */
   private String mAttemptAbsoluteDurationLimit;

   /**
    * The variable representing the completionThreshold element.<br>
    */
   private String mCompletionThreshold;

   /**
    * The variable representing the objectives found in the sequencing.<br>
    */
   private String mObjectivesList;

   /**
    * The variable representing the hideRTSUI value of "previous".<br>
    */
   public boolean mPrevious;

   /**
    * The variable representing the hideRTSUI value of "continue".<br>
    */
   public boolean mContinue;

   /**
    * The variable representing the hideRTSUI value of "exit".<br>
    */
   public boolean mExit;

   /**
    * The variable representing the hideRTSUI value of "abandon".<br>
    */
   public boolean mAbandon;


   /**
    * The default constructor.<br>
    */
   public LaunchData()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mOrganizationIdentifier       = new String();
      mItemIdentifier               = new String();
      mResourceIdentifier           = new String();
      mManifestXMLBase              = new String();
      mResourcesXMLBase             = new String();
      mResourceXMLBase              = new String();
      mParameters                   = new String();
      mPersistState                 = new String();
      mLocation                     = new String();
      mSCORMType                    = new String();
      mItemTitle                    = new String();
      mDataFromLMS                  = new String();
      mTimeLimitAction              = new String();
      mMinNormalizedMeasure         = new String();
      mAttemptAbsoluteDurationLimit = new String();
      mCompletionThreshold          = new String();
      mObjectivesList               = new String();
      mPrevious                     = false;
      mContinue                     = false;
      mExit                         = false;
      mAbandon                      = false;
   }

   /**
    * 
    * Assigns the given value to the mOrganizationIdentifier attribute. <br>
    * 
    * @param iOrganizationIdentifier - The organization identitifier value to 
    * be assigned. <br>
    */
   public void setOrganizationIdentifier( String iOrganizationIdentifier )
   {
      mOrganizationIdentifier = iOrganizationIdentifier;
   }

   /**
    *
    * Assigns the given value to the mItemIdentifier attribute.<br>
    *
    * @param iItemIdentifier - The item identifier value to be assigned.<br>
    */
   public void setItemIdentifier( String iItemIdentifier )
   {
      mItemIdentifier = iItemIdentifier;
   }

   /**
    *
    * Assigns the given value to the mResourceIdentifier attribute.<br>
    *
    * @param iResourceIdentifier - The resource identifier value to be assigned.
    * <br>
    */
   public void setResourceIdentifier( String iResourceIdentifier )
   {
      mResourceIdentifier = iResourceIdentifier;
   }

   /**
    *
    * Assigns the given value to the mManifestXMLBase attribute.<br>
    *
    * @param iManifestXMLBase - The manifest xml:base value to be assigned.<br>
    */
   public void setManifestXMLBase( String iManifestXMLBase )
   {
      mManifestXMLBase = iManifestXMLBase;
   }

   /**
    *
    * Assigns the given value to the mResourcesXMLBase attribute.<br>
    *
    * @param iResourcesXMLBase - The resources xml:base value to be assigned.<br>
    */
   public void setResourcesXMLBase( String iResourcesXMLBase )
   {
      mResourcesXMLBase = iResourcesXMLBase;
   }

   /**
    *
    * Assigns the given value to the mResourceXMLBase attribute.<br>
    *
    * @param iResourceXMLBase - The resource xml:base value to be assigned.<br>
    */
   public void setResourceXMLBase( String iResourceXMLBase )
   {
      mResourceXMLBase = iResourceXMLBase;
   }

   /**
    *
    * Assigns the given value to the mParameters attribute.<br>
    *
    * @param iParameters - The parameters value to be assigned.<br>
    */
   public void setParameters( String iParameters )
   {
      mParameters = iParameters;
   }

   /**
    * 
    * Assigns the given value to the mPersistState attribute of an item.<br>
    * 
    * @param iPersistState - The persistState value to be assigned.<br>
    */
   public void setPersistState( String iPersistState )
   {
      mPersistState = iPersistState;
   }

   /**
    *
    * Assigns the given value to the mLocation attribute.<br>
    *
    * @param iLocation - The location value to be assigned.<br>
    */
   public void setLocation( String iLocation )
   {
      mLocation = iLocation;
   }

   /**
    *
    * Assigns the given value to the mSCORMType attribute.<br>
    *
    * @param iSCORMType - The scormtype value to be assigned.<br>
    */
   public void setSCORMType( String iSCORMType )
   {
      mSCORMType = iSCORMType;
   }

   /**
    *
    * Assigns the given value to the mItemTitle attribute.<br>
    *
    * @param iSCORMType - The item value to be assigned.<br>
    */
   public void setItemTitle( String iItemTitle )
   {
      mItemTitle = iItemTitle;
   }

   /**
    * Assigns the given value to the mDataFromLMS attribute.<br>
    *
    * @param iDataFromLMS - The datafromlms value to be assigned.<br>
    */
   public void setDataFromLMS( String iDataFromLMS )
   {
      mDataFromLMS = iDataFromLMS;
   }

   /**
    * Assigns the given value to the mTimeLimitAction attribute.<br>
    *
    * @param iTimeLimitAction - The timelimitaction value to be assigned.<br>
    */
   public void setTimeLimitAction( String iTimeLimitAction )
   {
      mTimeLimitAction = iTimeLimitAction;
   }

   /**
    * Assigns the given value to the minNormalizedMeasure attribute.<br>
    *
    * @param iMinNormalizedMeasure - The minnormalizedmeasure value to be
    * assigned.<br>
    */
   public void setMinNormalizedMeasure( String iMinNormalizedMeasure )
   {
      mMinNormalizedMeasure = iMinNormalizedMeasure;
   }

   /**
    * Assigns the given value to the attemptAbsoluteDurationLimit attribute.<br>
    *
    * @param iAttemptAbsoluteDurationLimit - The attemptabsolutedurationlimit
    * value to be assigned.<br>
    */
   public void setAttemptAbsoluteDurationLimit( String
                                                iAttemptAbsoluteDurationLimit )
   {
      mAttemptAbsoluteDurationLimit = iAttemptAbsoluteDurationLimit;
   }

   /**
    * Assigns the given value to the mCompletionThreshold attribute.<br>
    * 
    * @param iCompletionThreshold - The completionThreshold value to be 
    * assigned.<br>
    */
   public void setCompletionThreshold( String iCompletionThreshold )
   {
      mCompletionThreshold = iCompletionThreshold;
   }

   /**
    * Assigns the given value to the mObjectivesList attribute.<br>
    * 
    * @param iObjectivesList - The objectives to be  
    * assigned.<br>
    */
   public void setObjectivesList( String iObjectivesList )
   {
      mObjectivesList = iObjectivesList;
   }

   /**
    * Assigns the given value to the mPrevious attribute.<br>
    *
    * @param iPrevious - The previous value to be assigned.<br>
    */
   public void setPrevious( boolean iPrevious )
   {
      mPrevious = iPrevious;
   }

   /**
    * Assigns the given value to the mContinue attribute.<br>
    *
    * @param iContinue - The continue value to be assigned.<br>
    */
   public void setContinue( boolean iContinue )
   {
      mContinue = iContinue;
   }

   /**
    * Assigns the given value to the mExit attribute.<br>
    *
    * @param iExit - The exit value to be assigned.<br>
    */
   public void setExit( boolean iExit )
   {
      mExit = iExit;
   }

   /**
    * Assigns the given value to the mAbandon attribute.<br>
    *
    * @param iAbandon - The abandon value to be assigned.<br>
    */
   public void setAbandon( boolean iAbandon )
   {
      mAbandon = iAbandon;
   }

   /**
    * Gives access to the indentifier value of the <organization> element.<br>
    * 
    * @return - The identifier value of the <organization> element.<br>
    */
   public String getOrganizationIdentifier()
   {
      return mOrganizationIdentifier;
   }

   /**
    *
    * Gives access to the identifier value of the <item> element.<br>
    *
    * @return - The identifier value of the <item> element.<br>
    */
   public String getItemIdentifier()
   {
      return mItemIdentifier;
   }

   /**
    *
    * Gives access to the identifier value of the <resource> element.<br>
    *
    * @return - The identifier value of the <resource> element.<br>
    */
   public String getResourceIdentifier()
   {
      return mResourceIdentifier;
   }

   /**
    *
    * Gives access to the xml:base value of the <manifest> element.<br>
    *
    * @return - The xml:base value of the <manifest> element.<br>
    */
   public String getManifestXMLBase()
   {
      return mManifestXMLBase;
   }

   /**
    *
    * Gives access to the xml:base value of the <resources> element.<br>
    *
    * @return - The xml:base value of the <resources> element.<br>
    */
   public String getResourcesXMLBase()
   {
      return mResourcesXMLBase;
   }

   /**
    *
    * Gives access to the xml:base value of the <resource> element.<br>
    *
    * @return - The xml:base value of the <resource> element.<br>
    */
   public String getResourceXMLBase()
   {
      return mResourceXMLBase;
   }

   /**
    *
    * Gives access to the full xml:base value.<br>
    *
    * @return - The full xml:base value as determined in the manifest.<br>
    */
   public String getXMLBase()
   {
      String result = mManifestXMLBase;

      // add a file separator only if there is a directory before and after it.
      if ( (! result.equals("")) &&
           (! mResourcesXMLBase.equals("")) &&
           (! result.endsWith("/")) )
      {
         result += "/";
      }

      result += mResourcesXMLBase;

      // add a file separator only if there is a directory before and after it.
      if ( (! result.equals("")) &&
           (! mResourceXMLBase.equals("")) &&
           (! result.endsWith("/")) )
      {
         result += "/";
      }

      result += mResourceXMLBase;

      return result;
   }

   /**
    *
    * Gives access to the parameters of the item.<br>
    *
    * @return - The parameter value of the item.<br>
    */
   public String getParameters()
   {
      return mParameters;
   }

   /**
    * Gives access to the persistState attribute of the item.<br>
    * 
    * @return = The value of the persistState attribute of the item.<br>
    */
   public String getPersistState()
   {
      return mPersistState;
   }

   /**
    *
    * Gives access to the location of the item.<br>
    *
    * @return - The location value of the item.<br>
    */
   public String getLocation()
   {
      return mLocation;
   }

   /**
    *
    * Gives access to the SCORM type value of the item.<br>
    *
    * @return - The SCORM type value of the item.<br>
    */
   public String getSCORMType()
   {
      return mSCORMType;
   }

   /**
    *
    * Gives access to the title value of the item.<br>
    *
    * @return - The title value of the item.<br>
    */
   public String getItemTitle()
   {
      return mItemTitle;
   }

   /**
    *
    * Gives access to the datafromlms element value of the item.<br>
    *
    * @return - The value of the datafromlms element.<br>
    */
   public String getDataFromLMS()
   {
      return mDataFromLMS;
   }

   /**
    *
    * Gives access to the timelimitaction element value of the item.<br>
    *
    * @return - The value of the timelimitaction element.<br>
    */
   public String getTimeLimitAction()
   {
      return mTimeLimitAction;
   }

   /**
    *
    * Gives access to the minNormalizedMeasure element value.<br>
    *
    * @return - The value of the minNormalizedMeasure element.<br>
    */
   public String getMinNormalizedMeasure()
   {
      return mMinNormalizedMeasure;
   }

   /**
    *
    * Gives access to the attemptAbsoluteDurationLimit element value.<br>
    *
    * @return - The value of the attemptAbsoluteDurationLimit element.<br>
    */
   public String getAttemptAbsoluteDurationLimit()
   {
      return mAttemptAbsoluteDurationLimit;
   }

   /**
    * Gives access to the completionThreshold element value.<br>
    * 
    * @return - The value of the completionThreshold element.<br>
    */
   public String getCompletionThreshold()
   {
      return mCompletionThreshold;
   }

   /**
    * Gives access to the objectiveslist element value.<br>
    * 
    * @return - The value of the objectiveslist element.<br>
    */
   public String getObjectivesList()
   {
      return mObjectivesList;
   }

   /**
    *
    * Gives access to the value of mPrevious, which is a boolean
    * representing whether a hideRTSUI element for the item had the value of
    * "previous".<br>
    *
    * @return - The value of the mPrevious.<br>
    */
   public boolean getPrevious()
   {
      return mPrevious;
   }

   /**
    *
    * Gives access to the value of mContinue, which is a boolean
    * representing whether a hideRTSUI element for the item had the value of
    * "continue".<br>
    *
    * @return - The value of the mContinue.<br>
    */
   public boolean getContinue()
   {
      return mContinue;
   }

   /**
    *
    * Gives access to the value of mExit, which is a boolean
    * representing whether a hideRTSUI element for the item had the value of
    * "exit".<br>
    *
    * @return - The value of the mExit.<br>
    */
   public boolean getExit()
   {
      return mExit;
   }

   /**
    *
    * Gives access to the value of mAbandon, which is a boolean
    * representing whether a hideRTSUI element for the item had the value of
    * "abandon".<br>
    *
    * @return - The value of the mAbandon.<br>
    */
   public boolean getAbandon()
   {
      return mAbandon;
   }

   /**
    *
    * Gives access to the full launch line of the item including the full
    * xml:base of the item.<br>
    *
    * @return - The full launch location of the item.<br>
    */
   public String getLaunchLine()
   {
      String xmlBase = getXMLBase();

      if ( (! xmlBase.equals("")) &&
           (! xmlBase.endsWith("/")) )
      {
          xmlBase += "/";
      }

      return xmlBase + mLocation + mParameters;

   }

   /**
    *
    * Displays a string representation of the data structure for the SCO
    * Integration to the Java logger. <br>
    *
    */
   public void print()
   {
      mLogger.fine( "##################################################");
      mLogger.fine( "####   resourceIdentifier = '" + mResourceIdentifier +
                       "'");
      mLogger.fine( "####   itemIdentifier = '" + mItemIdentifier + "'");
      mLogger.fine( "####   itemTitle = '" + mItemTitle + "'");
      mLogger.fine( "####   manifestXMLBase = '" + mManifestXMLBase + "'");
      mLogger.fine( "####   resourcesXMLBase = '" + mResourcesXMLBase + "'");
      mLogger.fine( "####   resourceXMLBase = '" + mResourceXMLBase + "'");
      mLogger.fine( "####   scormType = '" + mSCORMType + "'");
      mLogger.fine( "####   parameters = '" + mParameters + "'");
      mLogger.fine( "####   location = '" + mLocation + "'");
      mLogger.fine( "####   LaunchLine = '" + getLaunchLine() + "'");
      mLogger.fine( "##################################################");
   }

   /**
    * Displays a string representation of the data structure for Integration to
    * the Java Console.<br>
    *
    */
   public void printToConsole()
   {
      System.out.println( "###############################################");
      System.out.println( "###   resourceIdentifier = '" +
                          mResourceIdentifier + "'");
      System.out.println( "###  itemIdentifier = '" + mItemIdentifier + "'");
      System.out.println( "###  itemTitle = '" + mItemTitle + "'");
      System.out.println( "###  manifestXMLBase = '" + mManifestXMLBase +
                          "'");
      System.out.println( "###  resourcesXMLBase = '" + mResourcesXMLBase +
                          "'");
      System.out.println( "###  resourceXMLBase = '" + mResourceXMLBase +
                          "'");
      System.out.println( "###  scormType = '" + mSCORMType + "'");
      System.out.println( "###  parameters = '" + mParameters + "'");
      System.out.println( "###  location = '" + mLocation + "'");
      System.out.println( "###  LaunchLine = '" + getLaunchLine() + "'");
      System.out.println( "###############################################");
   }
}
