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
package org.adl.validator.sequence;

// native java imports

import java.io.Serializable;
import java.lang.String;
import java.util.Vector;
import java.util.logging.*;


// xerces imports

import org.w3c.dom.*;

// adl imports
import org.adl.util.debug.*;
import org.adl.util.*;


import org.adl.parsers.dom.DOMTreeUtility;

/**
 *
 * <strong>Filename: </strong><br>ObjectiveMap.java<br><br>
 *
 * <strong>Description: </strong><br> A <CODE>ObjectiveMap</CODE> is a Data
 * Structure used to store objective information that is necessary for for the
 * validation and processing of read/write attributes to global objectives.<br>
 * <br>
 *
 * <strong>Design Issues: </strong><br>None<br>
 * <br>
 *
 * <strong>Implementation Issues: </strong><br>None<br><br>
 *
 * <strong>Known Problems: </strong><br>None<br><br>
 *
 * <strong>Side Effects: </strong><br>Populates the MessageCollection<br><br>
 *
 * <strong>References: </strong><br>None<br><br>
 *
 * @author ADL Technical Team
 */

public class ObjectiveMap implements Serializable
{
   /**
    * Logger object used for debug logging.<br>
    */
   private Logger mLogger;

   /**
    * The identifier attribute of the <objective> or <primaryObjective>
    * element.<br>
    */
   private String mObjectiveID;

   /**
    * The identifier attribute of the global <objective>.<br>
    *
    */
   private String mTargetObjectiveID;

   /**
    * Indicates that the satisfaction status for the local objective should
    * be retrieved from the global objective when undefined.<br>
    *
    */
   private String mReadSatisfiedStatus;

   /**
    * Indicates that the satisfaction status for the local objective should be
    * transferred to the global objective upon termination.<br>
    *
    */
   private String mWriteSatisfiedStatus;

   /**
    * Indicates that the normalized measure for the local objective should be
    * retrieved from the global objective when undefined.<br>
    *
    */
   private String mReadNormalizedMeasure;

   /**
    * Indicates that the normalized measure for the local objective should be
    * transferred to the global objective upon termination.<br>
    *
    */
   private String mWriteNormalizedMeasure;

   /**
    * The ObjectiveMap objects for all <objective> and <primaryObjective>
    * children of the <objectives> element.
    */
   private Vector mObjectiveMaps;


   /**
    * The default constructor.<br>
    */
   public ObjectiveMap()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.validator");

      mObjectiveID                   = new String();
      mTargetObjectiveID             = new String();
      mReadSatisfiedStatus           = new String();
      mWriteSatisfiedStatus          = new String();
      mReadNormalizedMeasure         = new String();
      mWriteNormalizedMeasure        = new String();
      mObjectiveMaps                 = new Vector();
   }

   /**
    *
    * Gives access to identifier attribute of the <objective> or
    * <primaryObjective> element.<br>
    *
    * @return - identifier attribute of the objective/primaryObjective
    * element.<br>
    */
   public String getObjectiveID()
   {
      return mObjectiveID;
   }

   /**
    *
    * Gives access to the identifier attribute of the global <objective><br>
    *
    * @return - The identifier attribute of the global <objective>.<br>
    */
   public String getTargetObjectiveID()
   {
      return mTargetObjectiveID;
   }

   /**
    *
    * The ObjectiveMap objects for all <objective> and <primaryObjective>
    * children of the <objectives> element.<br>
    *
    * @return - The vector containing all <objective> and <primaryObjective>
    * information in the <objectives> .<br>
    */
   public Vector getObjectiveMaps()
   {
      return mObjectiveMaps;
   }

   /**
    *
    * Gives access to whether or not the satisfaction status for the local
    * objective should be retrieved from the global objective when undefined<br>
    *
    * @return - Whether or not the satisfaction status for the local objective
    * should be retrieved from the global objective when undefined. "true"
    * implies it should, "false" implies otherwise.<br>
    */
   public String getWriteSatisfiedStatus()
   {
      return mWriteSatisfiedStatus;
   }

   /**
    *
    * Gives access to whether or not the satisfaction status for the local
    * objective should be transferred to the global objective upon termination.
    *
    * @return - Whether or not the satisfaction status for the local objective
    * should be transferred to the global objective upon termination.<br>
    */
   public String getReadSatisfiedStatus()
   {
      return mReadSatisfiedStatus;
   }

   /**
    *
    * Gives access to whether or not the normalized measure for the local
    * objective should be retrieved from the global objective when undefined.
    *
    * @return - Whether or not the normalized measure for the local objective
    * should be retrieved from the global objective when undefined.<br>
    */
   public String getReadNormalizedMeasure()
   {
      return mReadNormalizedMeasure;
   }


   /**
    *
    * Gives access to whether or not the normalized measure for the local
    * objective should be retrieved from the global objective when undefined.
    *
    * @return - Whether or not the normalized measure for the local objective
    * should be retrieved from the global objective when undefined.<br>
    */
   public String getWriteNormalizedMeasure()
   {
      return mWriteNormalizedMeasure;
   }

   /**
    *
    * Sets the value of the identifier attribute of the <objective> or
    * <primaryObjective> element.<br>
    *
    * @return - void <br>
    *
    */
   public void setObjectiveID( String iObjectiveID )
   {
      mObjectiveID = iObjectiveID;
   }

   /**
    *
    * Sets the value of the identifier attribute of the global <objective><br>
    *
    * @return - void <br>
    */
   public void setTargetObjectiveID( String iTargetObjectiveID )
   {
      mTargetObjectiveID = iTargetObjectiveID;
   }


   /**
    *
    * Sets whether or not the satisfaction status for the local
    * objective should be retrieved from the global objective when undefined<br>
    *
    * @return - void <br>
    */
   public void setWriteSatisfiedStatus( String iWriteSatisfiedStatus )
   {
      mWriteSatisfiedStatus = iWriteSatisfiedStatus;
   }

   /**
    *
    * Sets whether or not the satisfaction status for the local
    * objective should be transferred to the global objective upon termination.
    *
    * @return - void <br>
    */
   public void setReadSatisfiedStatus( String iReadSatisfiedStatus )
   {
      mReadSatisfiedStatus = iReadSatisfiedStatus;
   }

   /**
    *
    * Sets whether or not the normalized measure for the local
    * objective should be retrieved from the global objective when undefined.
    *
    * @return - void <br>
    */
   public void setReadNormalizedMeasure( String iReadNormalizedMeasure )
   {
      mReadNormalizedMeasure = iReadNormalizedMeasure;
   }


   /**
    *
    * Sets whether or not the normalized measure for the local
    * objective should be retrieved from the global objective when undefined.
    *
    * @return - void <br>
    */
   public void setWriteNormalizedMeasure( String iWriteNormalizedMeasure )
   {
      mWriteNormalizedMeasure = iWriteNormalizedMeasure;
   }




   /**
    *
    * This method populates the ObjectiveMap object by traversing down
    * the objectives node and storing all information necessary for the
    * validation reading and writing to global objectives.  Information stored
    * for the objectives element includes:
    * objective identifier,taget objective identifer, and flags determining
    * whether read or write normalized measure and satisfifed status are set.<br>
    *
    * @param Node iNode - the objectives element node, to be used to traverse
    * and populate the ObjectiveMap object.<br>
    *
    * @return void
    *
    */
   public void populateObjectiveMap( Node iNode )
   {
      mLogger.entering( "ObjectiveMap", "populateObjectiveMap" );

      boolean addToVectorFlag = false;
      String mapObjectiveID = new String();

      if ( iNode.getLocalName().equals("objectives") )
      {
         NodeList objectivesChildren = iNode.getChildNodes();
         int length = objectivesChildren.getLength();

         for ( int i = 0; i < length; i++ )
         {
            Node currentChild = objectivesChildren.item(i);
            populateObjectiveMap(currentChild);
         }

      }

      String nodeName = iNode.getLocalName();

      if ( nodeName.equals("primaryObjective") ||
           nodeName.equals("objective")  )
      {
         //look for the attributes of this element
         NamedNodeMap attrList = iNode.getAttributes();

         int numAttr = attrList.getLength();
         mLogger.finer( "There are " + numAttr + " attributes of " +
                         nodeName + " to test" );

         Attr currentAttrNode;
         String currentNodeName;
         String attributeValue = null;
         ObjectiveMap currentObjectiveMap = null;

         // loop thru attributes to find ObjectiveID value
         for ( int i = 0; i < numAttr; i++ )
         {
            currentAttrNode = (Attr)attrList.item(i);
            currentNodeName = currentAttrNode.getLocalName();

            if ( currentNodeName.equals("objectiveID") )
            {
               attributeValue = currentAttrNode.getValue();

               if ( mObjectiveID.equals("") )
               {
                  setObjectiveID( attributeValue );
               }
               else
               {
                  // need to indicate that we need to create a new object and
                  // store in vector atttibute
                  addToVectorFlag = true;
                  currentObjectiveMap = new ObjectiveMap();
                  currentObjectiveMap.setObjectiveID( attributeValue );
               }
               // store this id for the multiple mapInfo elements to have an
               // objectiveID
               mapObjectiveID = attributeValue;
            }
         }

         // find mapInfo children of objective/primaryObjective element
         NodeList children = iNode.getChildNodes();
         int length = children.getLength();

         // flag determining if we have to create new objects to hold multiple
         // mapInfo information.
         boolean createNewMapInfo = false;

         if (length == 0)
         {
            //Put in to prevent null pointer error that is thrown when testing
            //Photoshop_Competency.zip testcase.
            //Not sure if this is the correct fix or not...investigate later.
            if (currentObjectiveMap != null)
            {
               //No mapInfo children, but still need to add the objective
               mObjectiveMaps.add(currentObjectiveMap);
            }
         }
         else
         {

            for ( int j = 0; j < length; j++ )
            {
               Node currentChild = children.item(j);
               String currentChildName = currentChild.getLocalName();

               if ( currentChildName.equals("mapInfo") )
               {

                  //look for the attributes of the mapInfo element
                  NamedNodeMap mapAttrList = currentChild.getAttributes();

                  int numMapAttr = mapAttrList.getLength();
                  mLogger.finer( "There are " + numMapAttr + " attributes of " +
                                  currentChildName + " to test" );

                  Attr currentMapAttrNode;
                  String currentMapAttrNodeName;

                  // read/write attributes initialized to default values
                  String readMeasure = "true";
                  String readStatus = "true";
                  String writeMeasure = "false";
                  String writeStatus = "false";
                  String targetID = new String();

                  // find the mapInfo attribute to populate the ObjectiveMap
                  for ( int k = 0; k < numMapAttr; k++ )
                  {
                     currentMapAttrNode = (Attr)mapAttrList.item(k);
                     currentMapAttrNodeName = currentMapAttrNode.getLocalName();

                     if ( currentMapAttrNodeName.equals("targetObjectiveID") )
                     {
                        targetID = currentMapAttrNode.getValue();
                     }
                     else if ( currentMapAttrNodeName.equals("readNormalizedMeasure") )
                     {
                        readMeasure = currentMapAttrNode.getValue();
                     }
                     else if (currentMapAttrNodeName.equals("writeNormalizedMeasure") )
                     {
                        writeMeasure = currentMapAttrNode.getValue();
                     }
                     else if ( currentMapAttrNodeName.equals("readSatisfiedStatus") )
                     {
                        readStatus = currentMapAttrNode.getValue();
                     }
                     else if (currentMapAttrNodeName.equals("writeSatisfiedStatus") )
                     {
                        writeStatus = currentMapAttrNode.getValue();
                     }
                  }

                  if ( createNewMapInfo )
                  {

                     // have to handle the creation of a new object when
                     // multiple mapInfo exist for a given objective
                     ObjectiveMap newCurrentObjectiveMap = new ObjectiveMap();
                     newCurrentObjectiveMap.setObjectiveID( mapObjectiveID );
                     newCurrentObjectiveMap.setTargetObjectiveID( targetID );
                     newCurrentObjectiveMap.setReadSatisfiedStatus( readStatus );
                     newCurrentObjectiveMap.setReadNormalizedMeasure( readMeasure );
                     newCurrentObjectiveMap.setWriteSatisfiedStatus( writeStatus );
                     newCurrentObjectiveMap.setWriteNormalizedMeasure( writeMeasure );

                     mObjectiveMaps.add(newCurrentObjectiveMap);
                  }
                  else if ( addToVectorFlag )
                  {
                     currentObjectiveMap.setTargetObjectiveID( targetID );
                     currentObjectiveMap.setReadSatisfiedStatus( readStatus );
                     currentObjectiveMap.setReadNormalizedMeasure( readMeasure );
                     currentObjectiveMap.setWriteSatisfiedStatus( writeStatus );
                     currentObjectiveMap.setWriteNormalizedMeasure( writeMeasure );

                     mObjectiveMaps.add(currentObjectiveMap);
                  }
                  else
                  {
                     setTargetObjectiveID( targetID );
                     setReadSatisfiedStatus( readStatus );
                     setReadNormalizedMeasure( readMeasure );
                     setWriteSatisfiedStatus( writeStatus );
                     setWriteNormalizedMeasure( writeMeasure );
                  }
                  createNewMapInfo = true;
               }
            }
         }
      }

      mLogger.exiting( "ObjectiveMap", "populateObjectiveMap" );
   }


   /**
    *
    * This method validates the populated ObjectiveMap objects to determine
    * if read/write to global/local objectives is valid.
    *
    * @return - The boolean describing if the ObjectiveMap object(s) has been
    * found to be valid.<br>
    */
   public boolean validateObjectiveMaps( ObjectiveMap iObjectiveMap)
   {
      boolean result = true;

      result = checkReadAttributes( iObjectiveMap, -1 ) && result;
      result = checkWriteAttributes( iObjectiveMap, -1 ) && result;

      return result;
   }

   /**
    * This method performs the validation of the objectives element and their
    * the read status to global objectives.
    * An error is thrown when an objectiveId is found that reads
    * information (readSatisfiedStatus, readSatisfiedMeasure) more than once
    * from the the same global objective.<br>
    *
    * @param iObjectiveMap - the objectiveMap objective being validated for
    * the read status. <br>
    *
    * @param iMapLoc - the index of the objectiveMap object in the
    * mObjectiveMaps vector.<br>
    *
    * @return boolean - whether or not validation of the read status passes.
    * "True" implies it passes, "False" implies otherwise.<br>
    *
    */
   private boolean checkReadAttributes( ObjectiveMap iObjectiveMap, int iMapLoc )
   {
      mLogger.entering( "ObjectiveMap", "checkReadAttributes" );
      boolean result = true;
      String msgText = new String();
      boolean foundDuplicateReadStatus = false;
      boolean foundDuplicateReadMeasure = false;

      // loop through checking for matches to the mObjectiveID

      int mObjectiveMapsSize = mObjectiveMaps.size();
      for ( int i = 0; i < mObjectiveMapsSize; i++ )
      {
         // need to position i so that it skips over itself as a comparison
         if ( iMapLoc != -1 )
         {
            if ( ( i == iMapLoc ) )
            {
               if ( !( i >= ( mObjectiveMaps.size()-1 ) ) )
               {
                  i++;
               }
               else
               {
                  break;
               }
            }
         }
         try
         {
            ObjectiveMap currentObjectiveMap =
                                      (ObjectiveMap)mObjectiveMaps.elementAt(i);

            String objectiveID2 = currentObjectiveMap.getObjectiveID();

            if ( iObjectiveMap.getObjectiveID().equals( objectiveID2 ) )
            {
               // have a match, need to validate that both do not read the same info
               if ( iObjectiveMap.getReadNormalizedMeasure().equalsIgnoreCase("true")
                    &&
                    currentObjectiveMap.getReadNormalizedMeasure().equalsIgnoreCase("true") )
               {
                  result = false && result;
                  foundDuplicateReadMeasure = true;
               }

               if ( iObjectiveMap.getReadSatisfiedStatus().equalsIgnoreCase("true")
                    &&
                   currentObjectiveMap.getReadSatisfiedStatus().equalsIgnoreCase("true") )
               {
                  result = false && result;
                  foundDuplicateReadStatus = true;

               }
            }
         }

         catch( ArrayIndexOutOfBoundsException iAIOBE )
         {
            mLogger.severe( "ArrayIndexOutOfBoundsException thrown" );
         }
    catch(NullPointerException NPE)
    {
    }
      }
      // log error messages
      if ( foundDuplicateReadStatus )
      {
         msgText = "Objective " + iObjectiveMap.getObjectiveID() +
                   " is set up to read the success status more than once from " +
                   "the global objective.";

         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message(
                                                MessageType.FAILED, msgText ) );
      }

      if ( foundDuplicateReadMeasure)
      {
         msgText = "Objective " + iObjectiveMap.getObjectiveID() +
                   " is set up to read the normalized measure more than once " +
                   "from the global objective.";

         mLogger.info( "FAILED: " + msgText );
         MessageCollection.getInstance().add( new Message(
                                                MessageType.FAILED, msgText ) );
      }

      // loop through the remaining ObjectiveMaps in the vector and recurse

      Vector maps = iObjectiveMap.getObjectiveMaps();
      int mapsSize = maps.size();
      for ( int i = 0; i < maps.size(); i++ )
      {

         try
         {
            ObjectiveMap nextObjectiveMap =
                                      (ObjectiveMap)maps.elementAt(i);

            result = checkReadAttributes( nextObjectiveMap, i ) && result;
         }
         catch( ArrayIndexOutOfBoundsException iAIOBE )
         {
            mLogger.severe( "ArrayIndexOutOfBoundsException thrown" );
         }
    catch(NullPointerException NPE)
    {
    }

      }

      mLogger.exiting( "ObjectiveMap", "checkReadAttributes" );
      return result;


   }

   /**
    * This method performs the validation of objectives and their write status.
    * An error is thrown when targetObjectiveIDs are equal and are found to be
    * write the same information (writeSatisfiedStatus, writeSatisfiedMeasure)
    * to the global objective.<br>
    *
    * @param iObjectiveMap - the objectiveMap objective being validated for
    * the read status. <br>
    *
    * @param iMapLoc - the index of the objectiveMap object in the
    * mObjectiveMaps vector.<br>
    *
    * @return boolean - whether or not validation of the read status passes.
    * "True" implies it passes, "False" implies otherwise.<br>
    *
    */
   private boolean checkWriteAttributes( ObjectiveMap iObjectiveMap, int iMapLoc )
   {

      mLogger.entering( "ObjectiveMap", "checkWriteAttributes" );
      boolean result = true;
      String msgText = new String();

      // loop through checking for matches to the mObjectiveID

      int mObjectiveMapsSize = mObjectiveMaps.size();
      for ( int i = 0; i < mObjectiveMapsSize; i++ )
      {
         // need to position i so that it skips over itself as a comparison
         if ( iMapLoc != -1 )
         {
            if ( ( i == iMapLoc ) )
            {
               if ( !( i >= ( mObjectiveMaps.size()-1 ) ) )
               {
                  i++;
               }
               else
               {
                  break;
               }
            }
         }

         try
         {
            ObjectiveMap currentObjectiveMap =
                                      (ObjectiveMap)mObjectiveMaps.elementAt(i);

            String objectiveID2 = currentObjectiveMap.getTargetObjectiveID();


            msgText = "targetID1 is " + iObjectiveMap.getTargetObjectiveID();
            mLogger.info( "INFO: " + msgText );

            msgText = "targetID2 is " + currentObjectiveMap.getTargetObjectiveID();
            mLogger.info( "INFO: " + msgText );

            if ( iObjectiveMap.getTargetObjectiveID().equals( objectiveID2 ) )
            {
               // have a match, need to validate that both do not read the same info
               if ( iObjectiveMap.getWriteNormalizedMeasure().equalsIgnoreCase("true")
                    &&
                    currentObjectiveMap.getWriteNormalizedMeasure().equalsIgnoreCase("true") )
               {
                  result = false && result;

                  msgText = "The normalized measure is set up to be written to "
                            + "global objective " +
                            iObjectiveMap.getTargetObjectiveID() +
                            " by more than one objective map";

                  mLogger.info( "FAILED: " + msgText );
                  MessageCollection.getInstance().add( new Message(
                                                MessageType.FAILED, msgText ) );
               }

               if ( iObjectiveMap.getWriteSatisfiedStatus().equalsIgnoreCase("true")
                    &&
                   currentObjectiveMap.getWriteSatisfiedStatus().equalsIgnoreCase("true") )
               {
                  result = false && result;

                  msgText = "The success status is set up to be written to "
                            + "global objective " +
                            iObjectiveMap.getTargetObjectiveID() +
                            " by more than one objective map";

                  mLogger.info( "FAILED: " + msgText );
                  MessageCollection.getInstance().add( new Message(
                                                   MessageType.FAILED, msgText ) );
               }
            }
         }
         catch( ArrayIndexOutOfBoundsException iAIOBE )
         {
            mLogger.severe( "ArrayIndexOutOfBoundsException thrown" );
         }
    catch(NullPointerException NPE)
    {
    }
      }

      // loop through the remaining ObjectiveMaps in the vector and recurse

      Vector maps = iObjectiveMap.getObjectiveMaps();
      int mapsSize = maps.size();
      for ( int i = 0; i < mapsSize; i++ )
      {

         try
         {
            ObjectiveMap nextObjectiveMap =
                                      (ObjectiveMap)maps.elementAt(i);

            result = checkWriteAttributes( nextObjectiveMap, i ) && result;
         }
         catch( ArrayIndexOutOfBoundsException iAIOBE )
         {
            mLogger.severe( "ArrayIndexOutOfBoundsException thrown" );
         }
    catch(NullPointerException NPE)
    {
    }

      }

      mLogger.exiting( "ObjectiveMap", "checkWriteAttributes" );
      return result;

   }

}

