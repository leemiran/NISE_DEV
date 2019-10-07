package org.adl.sequencer;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import com.sun.org.apache.xpath.internal.XPathAPI;

import egovframework.adm.lcms.cts.domain.LcmsGlobalobj;
import egovframework.adm.lcms.cts.service.LcmsGlobalobjService;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jdom.Element;
import org.lcms.api.com.AOFException;
import org.lcms.api.com.AOFSQLException;
import org.lcms.api.com.AbstractModel;
//import or.keris.ngedu.web.lcms.api.common.QueryLoader;
//import or.keris.ngedu.web.lcms.api.common.VOMapper;
//import or.keris.ngedu.web.lcms.api.study.vo.GlobalObjVO;
//import com.sun.org.apache.xpath.internal.XPathAPI;

// Referenced classes of package org.adl.sequencer:
//            SeqActivityTree, ADLTOC, SeqActivity, ADLAuxiliaryResource, 
//            SeqRule, SeqRuleset, SeqConditionSet, SeqCondition, 
//            SeqRollupRule, SeqRollupRuleset, SeqObjective, SeqObjectiveMap

public class ADLSeqUtilities extends AbstractModel implements Serializable{

    private static boolean _Debug = false;
	//QueryLoader sqlLoader = new QueryLoader();
	String queryid = null;
	String sql = null;

	
	/** LcmsGlobalobj */
	@Resource(name = "lcmsGlobalobjService")
	private static LcmsGlobalobjService lcmsGlobalobjService;
	
    public ADLSeqUtilities()
    {
    }

    public static SeqActivityTree buildActivityTree(Node iOrg, Node iColl)
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - buildActivityTree");
        SeqActivityTree tree = new SeqActivityTree();
        SeqActivity root = bulidActivityNode(iOrg, iColl);
        if(root != null)
        {
            tree.setRoot(root);
            tree.setDepths();
            tree.setTreeCount();
        } else
        {
            tree = null;
        }
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> END   - buildActivityTree");
        return tree;
    }

    public static void dumpTOC(Vector iTOC)
    {
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - dumpTOC");
            if(iTOC != null)
            {
                System.out.println("  ::-->  " + iTOC.size());
                ADLTOC temp = null;
                for(int i = 0; i < iTOC.size(); i++)
                {
                    temp = (ADLTOC)iTOC.elementAt(i);
                    temp.dumpState();
                }

            } else
            {
                System.out.println("  ::--> NULL");
            }
            System.out.println("  :: ADLSeqUtilities  --> END   - dumpTOC");
        }
    }

    public static void createGlobalObjs(String iLearnerID, String iScopeID, Vector iObjList) throws Exception
    {
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - createGlobalObjs");
            System.out.println("  ::-->  " + iLearnerID);
            System.out.println("  ::-->  " + iScopeID);
        }
        ADLSeqUtilities sequtil = new ADLSeqUtilities();
        Connection con = null;
        
        
        /** 테스트 해야됨  
        for(int i = 0; i < iObjList.size(); i++) {
        
        	String objID = (String)iObjList.elementAt(i);
        	
	        Map<String,Object> map = new HashMap();
	        map.put("userID",iLearnerID);
	        map.put("objId", objID);

	        LcmsGlobalobj lcmsGlobalobj = (LcmsGlobalobj)lcmsGlobalobjService.selectLcmsGlobalobj(map);
        }
        **/
        /**
        VOMapper dao = new VOMapper();
        HashMap map = new HashMap();
        try
        {
        	
        	
        	con = sequtil.getConnection();
            con.setAutoCommit(false);
            for(int i = 0; i < iObjList.size(); i++)
            {
                String objID = (String)iObjList.elementAt(i);
                if(_Debug)
                    System.out.println("  ::--> Checking for objective --> " + iLearnerID + " [" + iScopeID + "]" + " // " + objID);
                
                map.put("objid", objID);
                map.put("user_id", iLearnerID);
                sequtil.queryid = "lcms.study.selectLCMS_GLOBALOBJ";
                sequtil.sql = sequtil.sqlLoader.getQuery(sequtil.queryid);
                Collection col = dao.getVO(con, sequtil.sql, GlobalObjVO.class.getName(), map);
                GlobalObjVO globalObj = null;
                if(col.size() > 0){
                	globalObj = (GlobalObjVO)col.toArray(new GlobalObjVO[0])[0];
                }
                if(globalObj == null)
                {
                    if(_Debug)
                        System.out.println("  ::--> Creating objective --> " + iLearnerID + " [" + iScopeID + "]" + " // " + objID);
                    globalObj = new GlobalObjVO();
                    globalObj.setObjid(objID);
                    globalObj.setScopeid(iScopeID);
                    globalObj.setUser_id(iLearnerID);
                    globalObj.setMeasure("unknown");
                    globalObj.setSatisfied("unknown");
                    sequtil.queryid = "lcms.study.insertLCMS_GLOBALOBJ";
                    sequtil.sql = sequtil.sqlLoader.getQuery(sequtil.queryid);
                    dao.executeUpdate(con, sequtil.sql, globalObj);
                }
            }

            con.commit();
        
		}catch(SQLException e){
			dao.release(con);
			throw new AOFSQLException(e,sequtil.queryid,sequtil.sql);			
		}catch (Exception ex) {
			dao.release(con);
			throw new AOFException(ex);			
		}
		finally{
			dao.release(con);
		}
		**/
        return;
    }

    public static void deleteGlobalObjs(String iLearnerID, String iScopeID, Vector iObjList)
    {
    }

    public static void clearGlobalObjs(String iLearnerID, String iScopeID, Vector iObjList) throws AOFException
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - clearGlobalObjs");
        ADLSeqUtilities sequtil = new ADLSeqUtilities();
        Connection con = null;
        /**
        VOMapper dao = new VOMapper();
        HashMap map = new HashMap();
        try
        {
        	con = sequtil.getConnection();
        	con.setAutoCommit(false);
            for(int i = 0; i < iObjList.size(); i++)
            {
                String objID = (String)iObjList.elementAt(i);
                if(_Debug)
                    System.out.println("  ::--> Attempting to clear record for --> " + iLearnerID + " [" + iScopeID + "]" + " // " + objID);
                map.put("objid", objID);
                map.put("userid", iLearnerID);
                sequtil.queryid = "lcms.study.selectLCMS_GLOBALOBJ";
                sequtil.sql = sequtil.sqlLoader.getQuery(sequtil.queryid);
                Collection col = dao.getVO(con, sequtil.sql, GlobalObjVO.class.getName(), map);
                GlobalObjVO globalObj = null;
                if(col.size() > 0){
                	globalObj = (GlobalObjVO)col.toArray(new GlobalObjVO[0])[0];
                }
                if(globalObj != null)
                {
                    if(_Debug)
                        System.out.println("  ::--> Creating objective --> " + iLearnerID + " [" + iScopeID + "]" + " // " + objID);
                    globalObj = new GlobalObjVO();
                    globalObj.setObjid(objID);
                    globalObj.setScopeid(iScopeID);
                    globalObj.setUser_id(iLearnerID);
                    globalObj.setMeasure("unknown");
                    globalObj.setSatisfied("unknown");
                    sequtil.queryid = "lcms.study.updateLCMS_GLOBALOBJ";
                    sequtil.sql = sequtil.sqlLoader.getQuery(sequtil.queryid);
                    dao.executeUpdate(con, sequtil.sql, globalObj);
                }
            }

            con.commit();
		}catch(SQLException e){
			dao.release(con);
			throw new AOFSQLException(e,sequtil.queryid,sequtil.sql);			
		}catch (Exception ex) {
			dao.release(con);
			throw new AOFException(ex);			
		}finally{
			dao.release(con);
		}
		*/
        return;
    }

    public static String getGlobalObjSatisfied(String iObjID, String iLearnerID, String iScopeID) throws AOFException
    {
        String satisfiedStatus;
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getGlobalObjSatisfied");
            System.out.println("  ::--> " + iObjID);
            System.out.println("  ::--> " + iLearnerID);
            System.out.println("  ::--> " + iScopeID);
        }
        satisfiedStatus = null;
        ADLSeqUtilities sequtil = new ADLSeqUtilities();
        
        /**
        VOMapper dao = new VOMapper();
        HashMap map = new HashMap();
        try
        {
            map.put("objid", iObjID);
            map.put("user_id", iLearnerID);
            sequtil.queryid = "lcms.study.selectLCMS_GLOBALOBJ";
            sequtil.sql = sequtil.sqlLoader.getQuery(sequtil.queryid);
            Collection col = dao.getVO(sequtil.sql, GlobalObjVO.class.getName(), map);
            GlobalObjVO globalObj = null;
            if(col.size() > 0){
            	globalObj = (GlobalObjVO)col.toArray(new GlobalObjVO[0])[0];
            }
            if(globalObj == null)
            {
                if(_Debug)
                    System.out.println("  ::--> No result set");
                satisfiedStatus = null;
            } else
            {
                satisfiedStatus = globalObj.getSatisfied();
            }
		}catch(SQLException e){
			throw new AOFSQLException(e,sequtil.queryid,sequtil.sql);			
		}catch (Exception ex) {
			throw new AOFException(ex);			
		}
        if(_Debug)
            System.out.println("  ::--> ERROR : NULL comp ID");
        if(_Debug)
            System.out.println("  ::--> ERROR : NULL learnerID");
        if(_Debug)
        {
            System.out.println("  ::-->  " + satisfiedStatus);
            System.out.println("  :: ADLSeqUtilities  --> END   - getGlobalObjSatisfied");
        }
        
        **/
        return satisfiedStatus;
    }

    public static boolean setGlobalObjSatisfied(String iObjID, String iLearnerID, String iScopeID, String iSatisfied)throws AOFException
    {
        boolean success;
        
        /**
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - setGlobalObjSatisfied");
            System.out.println("  ::--> " + iObjID);
            System.out.println("  ::--> " + iLearnerID);
            System.out.println("  ::--> " + iScopeID);
            System.out.println("  ::--> " + iSatisfied);
        }
        success = true;
        if(!iSatisfied.equals("unknown") && !iSatisfied.equals("satisfied") && !iSatisfied.equals("notSatisfied"))
        {
            success = false;
            if(_Debug)
            {
                System.out.println("  ::--> Invalid value: " + iSatisfied);
                System.out.println("  ::-->  " + success);
                System.out.println("  :: ADLSeqUtilities  --> END   - setSharedCompMastery");
            }
            return success;
        }
        
   
        ADLSeqUtilities sequtil = new ADLSeqUtilities();
        Connection con = null;
        VOMapper dao = new VOMapper();
        HashMap map = new HashMap();
        try
        {
        	con = sequtil.getConnection();
        	int i = 0;
            con.setAutoCommit(false);
            GlobalObjVO globalObj = new GlobalObjVO();
            globalObj.setObjid(iObjID);
            globalObj.setScopeid(iScopeID);
            globalObj.setUser_id(iLearnerID);
            globalObj.setMeasure("unknown");
            globalObj.setSatisfied(iSatisfied);
            sequtil.queryid = "lcms.study.updateLCMS_GLOBALOBJ-satisfied";
            sequtil.sql = sequtil.sqlLoader.getQuery(sequtil.queryid);
            i = dao.executeUpdate(con, sequtil.sql, globalObj);
            if(i!=0){
            	success = true;
            }
            con.commit();
		}catch(SQLException e){
			dao.release(con);
			success = false;
			throw new AOFSQLException(e,sequtil.queryid,sequtil.sql);
		}catch (Exception ex) {
			dao.release(con);
			success = false;
			throw new AOFException(ex);			
		}finally{
			dao.release(con);
		}
        if(_Debug)
        {
            System.out.println("  ::--> " + success);
            System.out.println("  :: ADLSeqUtilities  --> END   - setGlobalObjSatisfied");
        }
        **/
        
        success = true;
        return success;
    }

    public static String getGlobalObjMeasure(String iObjID, String iLearnerID, String iScopeID)throws AOFException
    {
        String measure;
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getGlobalObjMeasure");
        measure = null;
        
        /**
        ADLSeqUtilities sequtil = new ADLSeqUtilities();
        VOMapper dao = new VOMapper();
        HashMap map = new HashMap();
        try
        {
            map.put("objid", iObjID);
            map.put("user_id", iLearnerID);
            sequtil.queryid = "lcms.study.selectLCMS_GLOBALOBJ";
            sequtil.sql = sequtil.sqlLoader.getQuery(sequtil.queryid);
            Collection col = dao.getVO(sequtil.sql, GlobalObjVO.class.getName(), map);
            GlobalObjVO globalObj = null;
            if(col.size() > 0){
            	globalObj = (GlobalObjVO)col.toArray(new GlobalObjVO[0])[0];
            }
            if(globalObj == null)
            {
                if(_Debug)
                    System.out.println("  ::--> No result set");
                measure = null;
            } else
            {
                measure = globalObj.getMeasure();
            }
		}catch(SQLException e){
			throw new AOFSQLException(e,sequtil.queryid,sequtil.sql);
		}catch (Exception ex) {
			throw new AOFException(ex);			
		}
        if(_Debug)
        {
            System.out.println("  ::-->  " + measure);
            System.out.println("  :: ADLSeqUtilities  --> END   - getGlobalObjMeasure");
        }
        **/
        return measure;
    }

    public static boolean setGlobalObjMeasure(String iObjID, String iLearnerID, String iScopeID, String iMeasure)throws AOFException
    {
        boolean success;
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - setGlobalObjMeasure");
            System.out.println("  ::--> " + iObjID);
            System.out.println("  ::--> " + iLearnerID);
            System.out.println("  ::--> " + iScopeID);
            System.out.println("  ::--> " + iMeasure);
        }
        boolean goodMeasure = true;
        success = true;
        if(!iMeasure.equals("unknown"))
        {
            try
            {
                Double tempMeasure = new Double(iMeasure);
                double range = tempMeasure.doubleValue();
                if(range < -1D || range > 1.0D)
                {
                    if(_Debug)
                        System.out.println("  ::--> Invalid range:  " + iMeasure);
                    goodMeasure = false;
                }
            }
            catch(NumberFormatException e)
            {
                if(_Debug)
                    System.out.println("  ::--> Invalid value:  " + iMeasure);
                goodMeasure = false;
            }
            if(!goodMeasure)
            {
                success = false;
                if(_Debug)
                {
                    System.out.println("  ::--> " + success);
                    System.out.println("  :: ADLSeqUtilities  --> END   - getGlobalObjMeasure");
                }
                return success;
            }
        }
        
        /**
        ADLSeqUtilities sequtil = new ADLSeqUtilities();
        Connection con = null;
        VOMapper dao = new VOMapper();
        HashMap map = new HashMap();
        try
        {
        	con = sequtil.getConnection();
        	int i = 0;
            con.setAutoCommit(false);
            if(iScopeID == null)
                iScopeID = " ";
            GlobalObjVO globalObj = new GlobalObjVO();
            globalObj.setObjid(iObjID);
            globalObj.setScopeid(iScopeID);
            globalObj.setUser_id(iLearnerID);
            globalObj.setMeasure(iMeasure);
            globalObj.setSatisfied("unknown");
            sequtil.queryid = "lcms.study.updateLCMS_GLOBALOBJ-measure";
            sequtil.sql = sequtil.sqlLoader.getQuery(sequtil.queryid);
            i = dao.executeUpdate(con, sequtil.sql, globalObj);
            if(i!=0){
            	success = true;
            }
            con.commit();
		}catch(SQLException e){
			dao.release(con);
			success = false;
			throw new AOFSQLException(e,sequtil.queryid,sequtil.sql);
		}catch (Exception ex) {
			dao.release(con);
			success = false;
			throw new AOFException(ex);			
		}finally{
			dao.release(con);
		}
        if(_Debug)
        {
            System.out.println("  ::--> " + success);
            System.out.println("  :: ADLSeqUtilities  --> END   - getGlobalObjMeasure");
        }
        
        **/
        return success;
    }

    public static void createCourseStatus(String iCourseID, String iLearnerID)
    {
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - createCourseStatus");
            System.out.println("  ::-->  " + iCourseID);
            System.out.println("  ::-->  " + iLearnerID);
        }
    }

    public static boolean setCourseStatus(String iCourseID, String iLearnerID, String iSatisfied, String iMeasure, String iCompleted)
    {
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - setCourseStatus");
            System.out.println("  ::--> " + iCourseID);
            System.out.println("  ::--> " + iLearnerID);
            System.out.println("  ::--> " + iSatisfied);
            System.out.println("  ::--> " + iMeasure);
            System.out.println("  ::--> " + iCompleted);
        }
        boolean success = true;
        return success;
    }

    public static void deleteCourseStatus(String iCourseID, String iLearnerID)
    {
         if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - deleteCourseStatus");
    }

    private static SeqActivity bulidActivityNode(Node iNode, Node iColl)
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - buildActivityNode");
        SeqActivity act = new SeqActivity();
        boolean error = false;
        boolean parent = false;
        String tempVal = null;
        act.setID(getAttribute(iNode, "identifier"));
        tempVal = getAttribute(iNode, "identifierref");
        if(tempVal != null)
            act.setResourceID(tempVal);
        tempVal = getAttribute(iNode, "isvisible");
        if(tempVal != null)
            act.setIsVisible((new Boolean(tempVal)).booleanValue());
        NodeList children = iNode.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node curNode = children.item(i);
            if(curNode.getNodeType() == 1)
                if(curNode.getLocalName().equals("item"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found an <item> element");
                    SeqActivity nestedAct = bulidActivityNode(curNode, iColl);
                    if(nestedAct != null)
                    {
                        if(_Debug)
                            System.out.println("  ::--> Adding child");
                        act.addChild(nestedAct);
                        parent = true;
                    } else
                    {
                        error = true;
                    }
                } else
                if(curNode.getLocalName().equals("title"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <title> element");
                    act.setTitle(getElementText(curNode, null));
                } else
                if(curNode.getLocalName().equals("sequencing"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <sequencing> element");
                    Node seqInfo = curNode;
                    tempVal = getAttribute(curNode, "IDRef");
                    if(tempVal != null)
                    {
                        String search = "imsss:sequencing[@ID='" + tempVal + "']";
                        if(_Debug)
                            System.out.println("  ::--> Looking for XPATH --> " + search);
                        Node seqGlobal = null;
                        try
                        {
                            seqGlobal = XPathAPI.selectSingleNode(iColl, search);
                        }
                        catch(Exception e)
                        {
                            if(_Debug)
                            {
                                System.out.println("  ::--> ERROR : In transform");
                                e.printStackTrace();
                            }
                        }
                        if(seqGlobal != null)
                        {
                            if(_Debug)
                                System.out.println("  ::--> FOUND");
                        } else
                        {
                            if(_Debug)
                                System.out.println("  ::--> ERROR: Not Found");
                            seqInfo = null;
                            error = true;
                        }
                        if(!error)
                        {
                            seqInfo = seqGlobal.cloneNode(true);
                            NodeList seqChildren = curNode.getChildNodes();
                            for(int j = 0; j < seqChildren.getLength(); j++)
                            {
                                Node curChild = seqChildren.item(j);
                                if(curChild.getNodeType() == 1)
                                {
                                    if(_Debug)
                                    {
                                        System.out.println("  ::--> Local definition");
                                        System.out.println("  ::-->   " + j);
                                        System.out.println("  ::-->  <" + curChild.getLocalName() + ">");
                                    }
                                    try
                                    {
                                        seqInfo.appendChild(curChild);
                                    }
                                    catch(DOMException e)
                                    {
                                        if(_Debug)
                                        {
                                            System.out.println("  ::--> ERROR: ");
                                            e.printStackTrace();
                                        }
                                        error = true;
                                        seqInfo = null;
                                    }
                                }
                            }

                        }
                    }
                    if(seqInfo != null)
                    {
                        error = !extractSeqInfo(seqInfo, act);
                        if(_Debug)
                            System.out.println("  ::--> Extracted Sequencing Info");
                    }
                }
        }

        if(act.getResourceID() == null && !act.hasChildren(true))
            error = true;
        if(error)
            act = null;
        if(_Debug)
        {
            System.out.println("  ::--> error == " + error);
            System.out.println("  :: ADLSeqUtilities  --> END   - buildActivityNode");
        }
        return act;
    }

    private static boolean extractSeqInfo(Node iNode, SeqActivity ioAct)
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - extractSeqInfo");
        boolean OK = true;
        String tempVal = null;
        NodeList children = iNode.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node curNode = children.item(i);
            if(curNode.getNodeType() == 1)
                if(curNode.getLocalName().equals("controlMode"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <controlMode> element");
                    tempVal = getAttribute(curNode, "choice");
                    if(tempVal != null)
                        ioAct.setControlModeChoice((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "choiceExit");
                    if(tempVal != null)
                        ioAct.setControlModeChoiceExit((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "flow");
                    if(tempVal != null)
                        ioAct.setControlModeFlow((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "forwardOnly");
                    if(tempVal != null)
                        ioAct.setControlForwardOnly((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "useCurrentAttemptObjectiveInfo");
                    if(tempVal != null)
                        ioAct.setUseCurObjective((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "useCurrentAttemptProgressInfo");
                    if(tempVal != null)
                        ioAct.setUseCurProgress((new Boolean(tempVal)).booleanValue());
                } else
                if(curNode.getLocalName().equals("sequencingRules"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <sequencingRules> element");
                    OK = getSequencingRules(curNode, ioAct);
                } else
                if(curNode.getLocalName().equals("limitConditions"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <limitConditions> element");
                    tempVal = getAttribute(curNode, "attemptLimit");
                    if(tempVal != null)
                        ioAct.setAttemptLimit(new Long(tempVal));
                    tempVal = getAttribute(curNode, "attemptAbsoluteDurationLimit");
                    if(tempVal != null)
                        ioAct.setAttemptAbDur(tempVal);
                    tempVal = getAttribute(curNode, "attemptExperiencedDurationLimit");
                    if(tempVal != null)
                        ioAct.setAttemptExDur(tempVal);
                    tempVal = getAttribute(curNode, "activityAbsoluteDurationLimit");
                    if(tempVal != null)
                        ioAct.setActivityAbDur(tempVal);
                    tempVal = getAttribute(curNode, "activityExperiencedDurationLimit");
                    if(tempVal != null)
                        ioAct.setActivityExDur(tempVal);
                    tempVal = getAttribute(curNode, "beginTimeLimit");
                    if(tempVal != null)
                        ioAct.setBeginTimeLimit(tempVal);
                    tempVal = getAttribute(curNode, "endTimeLimit");
                    if(tempVal != null)
                        ioAct.setEndTimeLimit(tempVal);
                } else
                if(curNode.getLocalName().equals("auxiliaryResources"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <auxiliaryResourcees> element");
                    OK = getAuxResources(curNode, ioAct);
                } else
                if(curNode.getLocalName().equals("rollupRules"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <rollupRules> element");
                    OK = getRollupRules(curNode, ioAct);
                } else
                if(curNode.getLocalName().equals("objectives"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <objectives> element");
                    OK = getObjectives(curNode, ioAct);
                } else
                if(curNode.getLocalName().equals("randomizationControls"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <randomizationControls> element");
                    tempVal = getAttribute(curNode, "randomizationTiming");
                    if(tempVal != null)
                        ioAct.setRandomTiming(tempVal);
                    tempVal = getAttribute(curNode, "selectCount");
                    if(tempVal != null)
                        ioAct.setSelectCount((new Integer(tempVal)).intValue());
                    tempVal = getAttribute(curNode, "reorderChildren");
                    if(tempVal != null)
                        ioAct.setReorderChildren((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "selectionTiming");
                    if(tempVal != null)
                        ioAct.setSelectionTiming(tempVal);
                } else
                if(curNode.getLocalName().equals("deliveryControls"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <deliveryControls> element");
                    tempVal = getAttribute(curNode, "tracked");
                    if(tempVal != null)
                        ioAct.setIsTracked((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "completionSetByContent");
                    if(tempVal != null)
                        ioAct.setSetCompletion((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "objectiveSetByContent");
                    if(tempVal != null)
                        ioAct.setSetObjective((new Boolean(tempVal)).booleanValue());
                } else
                if(curNode.getLocalName().equals("constrainedChoiceConsiderations"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <constrainedChoiceConsiderations> element");
                    tempVal = getAttribute(curNode, "preventActivation");
                    if(tempVal != null)
                        ioAct.setPreventActivation((new Boolean(tempVal)).booleanValue());
                    tempVal = getAttribute(curNode, "constrainChoice");
                    if(tempVal != null)
                        ioAct.setConstrainChoice((new Boolean(tempVal)).booleanValue());
                } else
                if(curNode.getLocalName().equals("rollupConsiderations"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found the <rollupConsiderations> element");
                    tempVal = getAttribute(curNode, "requiredForSatisfied");
                    if(tempVal != null)
                        ioAct.setRequiredForSatisfied(tempVal);
                    tempVal = getAttribute(curNode, "requiredForNotSatisfied");
                    if(tempVal != null)
                        ioAct.setRequiredForNotSatisfied(tempVal);
                    tempVal = getAttribute(curNode, "requiredForCompleted");
                    if(tempVal != null)
                        ioAct.setRequiredForCompleted(tempVal);
                    tempVal = getAttribute(curNode, "requiredForIncomplete");
                    if(tempVal != null)
                        ioAct.setRequiredForIncomplete(tempVal);
                    tempVal = getAttribute(curNode, "measureSatisfactionIfActive");
                    if(tempVal != null)
                        ioAct.setSatisfactionIfActive((new Boolean(tempVal)).booleanValue());
                }
        }

        if(_Debug)
        {
            System.out.println("  ::-->  " + OK);
            System.out.println("  :: ADLSeqUtilities  --> END   - extractSeqInfo");
        }
        return OK;
    }

    private static boolean getAuxResources(Node iNode, SeqActivity ioAct)
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getAuxResources");
        boolean OK = true;
        String tempVal = null;
        Vector auxRes = new Vector();
        NodeList children = iNode.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node curNode = children.item(i);
            if(curNode.getNodeType() == 1 && curNode.getLocalName().equals("auxiliaryResource"))
            {
                if(_Debug)
                    System.out.println("  ::--> Found a <auxiliaryResource> element");
                ADLAuxiliaryResource res = new ADLAuxiliaryResource();
                tempVal = getAttribute(curNode, "purpose");
                if(tempVal != null)
                    res.mType = tempVal;
                tempVal = getAttribute(curNode, "auxiliaryResourceID");
                if(tempVal != null)
                    res.mResourceID = tempVal;
                auxRes.add(res);
            }
        }

        ioAct.setAuxResources(auxRes);
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> END   - getAuxResources");
        return OK;
    }

    private static boolean getSequencingRules(Node iNode, SeqActivity ioAct)
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getSequencingRules");
        boolean OK = true;
        String tempVal = null;
        Vector preRules = new Vector();
        Vector exitRules = new Vector();
        Vector postRules = new Vector();
        NodeList children = iNode.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node curNode = children.item(i);
            if(curNode.getNodeType() == 1)
                if(curNode.getLocalName().equals("preConditionRule"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found a <preConditionRule> element");
                    SeqRule rule = new SeqRule();
                    NodeList ruleInfo = curNode.getChildNodes();
                    for(int j = 0; j < ruleInfo.getLength(); j++)
                    {
                        Node curRule = ruleInfo.item(j);
                        if(curRule.getNodeType() == 1)
                            if(curRule.getLocalName().equals("ruleConditions"))
                            {
                                if(_Debug)
                                    System.out.println("  ::--> Found a <ruleConditions> element");
                                rule.mConditions = extractSeqRuleConditions(curRule);
                            } else
                            if(curRule.getLocalName().equals("ruleAction"))
                            {
                                if(_Debug)
                                    System.out.println("  ::--> Found a <ruleAction> element");
                                tempVal = getAttribute(curRule, "action");
                                if(tempVal != null)
                                    rule.mAction = tempVal;
                            }
                    }

                    if(rule.mConditions != null && rule.mAction != null)
                        preRules.add(rule);
                    else
                    if(_Debug)
                        System.out.println("  ::--> ERROR : Invaild Pre SeqRule");
                } else
                if(curNode.getLocalName().equals("exitConditionRule"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found a <exitConditionRule> element");
                    SeqRule rule = new SeqRule();
                    NodeList ruleInfo = curNode.getChildNodes();
                    for(int k = 0; k < ruleInfo.getLength(); k++)
                    {
                        Node curRule = ruleInfo.item(k);
                        if(curRule.getNodeType() == 1)
                            if(curRule.getLocalName().equals("ruleConditions"))
                            {
                                if(_Debug)
                                    System.out.println("  ::--> Found a <ruleConditions> element");
                                rule.mConditions = extractSeqRuleConditions(curRule);
                            } else
                            if(curRule.getLocalName().equals("ruleAction"))
                            {
                                if(_Debug)
                                    System.out.println("  ::--> Found a <ruleAction> element");
                                tempVal = getAttribute(curRule, "action");
                                if(tempVal != null)
                                    rule.mAction = tempVal;
                            }
                    }

                    if(rule.mConditions != null && rule.mAction != null)
                        exitRules.add(rule);
                    else
                    if(_Debug)
                        System.out.println("  ::--> ERROR : Invaild Exit SeqRule");
                } else
                if(curNode.getLocalName().equals("postConditionRule"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found a <postConditionRule> element");
                    SeqRule rule = new SeqRule();
                    NodeList ruleInfo = curNode.getChildNodes();
                    for(int j = 0; j < ruleInfo.getLength(); j++)
                    {
                        Node curRule = ruleInfo.item(j);
                        if(curRule.getNodeType() == 1)
                            if(curRule.getLocalName().equals("ruleConditions"))
                            {
                                if(_Debug)
                                    System.out.println("  ::--> Found a <ruleConditions> element");
                                rule.mConditions = extractSeqRuleConditions(curRule);
                            } else
                            if(curRule.getLocalName().equals("ruleAction"))
                            {
                                if(_Debug)
                                    System.out.println("  ::--> Found a <ruleAction> element");
                                tempVal = getAttribute(curRule, "action");
                                if(tempVal != null)
                                    rule.mAction = tempVal;
                            }
                    }

                    if(rule.mConditions != null && rule.mAction != null)
                        postRules.add(rule);
                    else
                    if(_Debug)
                        System.out.println("  ::--> ERROR : Invaild Post SeqRule");
                }
        }

        if(preRules.size() > 0)
        {
            SeqRuleset rules = new SeqRuleset(preRules);
            ioAct.setPreSeqRules(rules);
        }
        if(exitRules.size() > 0)
        {
            SeqRuleset rules = new SeqRuleset(exitRules);
            ioAct.setExitSeqRules(rules);
        }
        if(postRules.size() > 0)
        {
            SeqRuleset rules = new SeqRuleset(postRules);
            ioAct.setPostSeqRules(rules);
        }
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> END   - getSequencingRules");
        return OK;
    }

    private static SeqConditionSet extractSeqRuleConditions(Node iNode)
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - extractSeqRuleConditions");
        String tempVal = null;
        SeqConditionSet condSet = new SeqConditionSet();
        Vector conditions = new Vector();
        tempVal = getAttribute(iNode, "conditionCombination");
        if(tempVal != null)
            condSet.mCombination = tempVal;
        else
            condSet.mCombination = SeqConditionSet.COMBINATION_ALL;
        NodeList condInfo = iNode.getChildNodes();
        for(int i = 0; i < condInfo.getLength(); i++)
        {
            Node curCond = condInfo.item(i);
            if(curCond.getNodeType() == 1 && curCond.getLocalName().equals("ruleCondition"))
            {
                if(_Debug)
                    System.out.println("  ::--> Found a <Condition> element");
                SeqCondition cond = new SeqCondition();
                tempVal = getAttribute(curCond, "condition");
                if(tempVal != null)
                    cond.mCondition = tempVal;
                tempVal = getAttribute(curCond, "referencedObjective");
                if(tempVal != null)
                    cond.mObjID = tempVal;
                tempVal = getAttribute(curCond, "measureThreshold");
                if(tempVal != null)
                    cond.mThreshold = (new Double(tempVal)).doubleValue();
                tempVal = getAttribute(curCond, "operator");
                if(tempVal != null)
                    if(tempVal.equals("not"))
                        cond.mNot = true;
                    else
                        cond.mNot = false;
                conditions.add(cond);
            }
        }

        if(conditions.size() > 0)
            condSet.mConditions = conditions;
        else
            condSet = null;
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> END   - extractSeqRuleConditions");
        return condSet;
    }

    private static boolean getRollupRules(Node iNode, SeqActivity ioAct)
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getRollupRules");
        boolean OK = true;
        String tempVal = null;
        Vector rollupRules = new Vector();
        tempVal = getAttribute(iNode, "rollupObjectiveSatisfied");
        if(tempVal != null)
            ioAct.setIsObjRolledUp((new Boolean(tempVal)).booleanValue());
        tempVal = getAttribute(iNode, "objectiveMeasureWeight");
        if(tempVal != null)
            ioAct.setObjMeasureWeight((new Double(tempVal)).doubleValue());
        tempVal = getAttribute(iNode, "rollupProgressCompletion");
        if(tempVal != null)
            ioAct.setIsProgressRolledUp((new Boolean(tempVal)).booleanValue());
        NodeList children = iNode.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node curNode = children.item(i);
            if(curNode.getNodeType() == 1 && curNode.getLocalName().equals("rollupRule"))
            {
                if(_Debug)
                    System.out.println("  ::--> Found a <rollupRule> element");
                SeqRollupRule rule = new SeqRollupRule();
                tempVal = getAttribute(curNode, "childActivitySet");
                if(tempVal != null)
                    rule.mChildActivitySet = tempVal;
                tempVal = getAttribute(curNode, "minimumCount");
                if(tempVal != null)
                    rule.mMinCount = (new Long(tempVal)).longValue();
                tempVal = getAttribute(curNode, "minimumPercent");
                if(tempVal != null)
                    rule.mMinPercent = (new Double(tempVal)).doubleValue();
                rule.mConditions = new SeqConditionSet();
                Vector conditions = new Vector();
                NodeList ruleInfo = curNode.getChildNodes();
                for(int j = 0; j < ruleInfo.getLength(); j++)
                {
                    Node curRule = ruleInfo.item(j);
                    if(curRule.getNodeType() == 1)
                        if(curRule.getLocalName().equals("rollupConditions"))
                        {
                            if(_Debug)
                                System.out.println("  ::--> Found a <rollupConditions> element");
                            tempVal = getAttribute(curRule, "conditionCombination");
                            if(tempVal != null)
                                rule.mConditions.mCombination = tempVal;
                            else
                                rule.mConditions.mCombination = SeqConditionSet.COMBINATION_ANY;
                            NodeList conds = curRule.getChildNodes();
                            for(int k = 0; k < conds.getLength(); k++)
                            {
                                Node con = conds.item(k);
                                if(con.getNodeType() == 1 && con.getLocalName().equals("rollupCondition"))
                                {
                                    if(_Debug)
                                        System.out.println("  ::--> Found a <rollupCondition> element");
                                    SeqCondition cond = new SeqCondition();
                                    tempVal = getAttribute(con, "condition");
                                    if(tempVal != null)
                                        cond.mCondition = tempVal;
                                    tempVal = getAttribute(con, "operator");
                                    if(tempVal != null)
                                        if(tempVal.equals("not"))
                                            cond.mNot = true;
                                        else
                                            cond.mNot = false;
                                    conditions.add(cond);
                                }
                            }

                        } else
                        if(curRule.getLocalName().equals("rollupAction"))
                        {
                            if(_Debug)
                                System.out.println("  ::--> Found a <rollupAction> element");
                            tempVal = getAttribute(curRule, "action");
                            if(tempVal != null)
                                rule.setRollupAction(tempVal);
                        }
                }

                rule.mConditions.mConditions = conditions;
                rollupRules.add(rule);
            }
        }

        if(rollupRules != null)
        {
            SeqRollupRuleset rules = new SeqRollupRuleset(rollupRules);
            ioAct.setRollupRules(rules);
        }
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> END   - getRollupRules");
        return OK;
    }

    private static boolean getObjectives(Node iNode, SeqActivity ioAct)
    {
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getObjectives");
        boolean OK = true;
        String tempVal = null;
        Vector objectives = new Vector();
        NodeList children = iNode.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node curNode = children.item(i);
            if(curNode.getNodeType() == 1)
                if(curNode.getLocalName().equals("primaryObjective"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found a <primaryObjective> element");
                    SeqObjective obj = new SeqObjective();
                    obj.mContributesToRollup = true;
                    tempVal = getAttribute(curNode, "objectiveID");
                    if(tempVal != null)
                        obj.mObjID = tempVal;
                    tempVal = getAttribute(curNode, "satisfiedByMeasure");
                    if(tempVal != null)
                        obj.mSatisfiedByMeasure = (new Boolean(tempVal)).booleanValue();
                    tempVal = getElementText(curNode, "minNormalizedMeasure");
                    if(tempVal != null)
                        obj.mMinMeasure = (new Double(tempVal)).doubleValue();
                    Vector maps = getObjectiveMaps(curNode);
                    if(maps != null)
                        obj.mMaps = maps;
                    obj.mContributesToRollup = true;
                    objectives.add(obj);
                } else
                if(curNode.getLocalName().equals("objective"))
                {
                    if(_Debug)
                        System.out.println("  ::--> Found a <objective> element");
                    SeqObjective obj = new SeqObjective();
                    tempVal = getAttribute(curNode, "objectiveID");
                    if(tempVal != null)
                        obj.mObjID = tempVal;
                    tempVal = getAttribute(curNode, "satisfiedByMeasure");
                    if(tempVal != null)
                        obj.mSatisfiedByMeasure = (new Boolean(tempVal)).booleanValue();
                    tempVal = getElementText(curNode, "minNormalizedMeasure");
                    if(tempVal != null)
                        obj.mMinMeasure = (new Double(tempVal)).doubleValue();
                    Vector maps = getObjectiveMaps(curNode);
                    if(maps != null)
                        obj.mMaps = maps;
                    objectives.add(obj);
                }
        }

        ioAct.setObjectives(objectives);
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> END   - getObjectives");
        return OK;
    }

    private static Vector getObjectiveMaps(Node iNode)
    {
       if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getObjectiveMaps");
        String tempVal = null;
        Vector maps = new Vector();
        NodeList children = iNode.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node curNode = children.item(i);
            if(curNode.getNodeType() == 1 && curNode.getLocalName().equals("mapInfo"))
            {
                if(_Debug)
                    System.out.println("  ::--> Found a <mapInfo> element");
                SeqObjectiveMap map = new SeqObjectiveMap();
                tempVal = getAttribute(curNode, "targetObjectiveID");
                if(tempVal != null)
                    map.mGlobalObjID = tempVal;
                tempVal = getAttribute(curNode, "readSatisfiedStatus");
                if(tempVal != null)
                    map.mReadStatus = (new Boolean(tempVal)).booleanValue();
                tempVal = getAttribute(curNode, "readNormalizedMeasure");
                if(tempVal != null)
                    map.mReadMeasure = (new Boolean(tempVal)).booleanValue();
                tempVal = getAttribute(curNode, "writeSatisfiedStatus");
                if(tempVal != null)
                    map.mWriteStatus = (new Boolean(tempVal)).booleanValue();
                tempVal = getAttribute(curNode, "writeNormalizedMeasure");
                if(tempVal != null)
                    map.mWriteMeasure = (new Boolean(tempVal)).booleanValue();
                maps.add(map);
            }
        }

        if(maps.size() == 0)
            maps = null;
        if(_Debug)
            System.out.println("  :: ADLSeqUtilities  --> END   - getObjectiveMaps");
        return maps;
    }

    private static String getElementText(Node iNode, String iElement)
    {
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getElementText");
            System.out.println("  ::-->  " + iElement);
        }
        String value = null;
        Node curNode = null;
        NodeList children = null;
        if(iElement != null && iNode != null)
        {
            children = iNode.getChildNodes();
            for(int i = 0; i < children.getLength(); i++)
            {
                curNode = children.item(i);
                if(curNode.getNodeType() != 1)
                    continue;
                if(_Debug)
                {
                    System.out.println("  ::-->   " + i);
                    System.out.println("  ::-->  <" + curNode.getLocalName() + ">");
                }
                if(!curNode.getLocalName().equals(iElement))
                    continue;
                if(_Debug)
                    System.out.println("  ::--> Found <" + iElement + ">");
                break;
            }

            if(curNode != null)
            {
                String comp = curNode.getLocalName();
                if(comp != null)
                {
                    if(!comp.equals(iElement))
                        curNode = null;
                } else
                {
                    curNode = null;
                }
            }
        } else
        {
            curNode = iNode;
        }
        if(curNode != null)
        {
            if(_Debug)
                System.out.println("  ::--> Looking at children");
            children = curNode.getChildNodes();
            if(children != null)
            {
                value = new String("");
                for(int i = 0; i < children.getLength(); i++)
                {
                    curNode = children.item(i);
                    if(curNode.getNodeType() == 3 || curNode.getNodeType() == 4){
                    	value = value + curNode.getNodeValue();
                    }
                }

            }
        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + value);
            System.out.println("  :: ADLSeqUtilities  --> END   - getElementText");
        }
        return value;
    }

    private static String getAttribute(Node iNode, String iAttribute)
    {
        if(_Debug)
        {
            System.out.println("  :: ADLSeqUtilities  --> BEGIN - getAttribute");
            System.out.println("  ::-->  " + iAttribute);
        }
        String value = null;
        NamedNodeMap attrs = iNode.getAttributes();
        if(attrs != null)
        {
            Node attr = attrs.getNamedItem(iAttribute);
            if(attr != null)
                value = attr.getNodeValue();
            else
            if(_Debug)
                System.out.println("  ::-->  The attribute \"" + iAttribute + "\" does not exist.");
        } else
        if(_Debug)
            System.out.println("  ::-->  This node has no attributes.");
        if(_Debug)
        {
            System.out.println("  ::-->  " + value);
            System.out.println("  :: ADLSeqUtilities  --> END - getAttribute");
        }
        return value;
    }


}