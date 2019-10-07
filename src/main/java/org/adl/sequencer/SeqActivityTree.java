
package org.adl.sequencer;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.adl.util.debug.DebugIndicator;

// Referenced classes of package org.adl.sequencer:
//            SeqActivity, SeqObjective, SeqObjectiveMap, ADLValidRequests

public class SeqActivityTree
    implements Serializable
{
    private static boolean _Debug;
    private SeqActivity mRoot;
    private ADLValidRequests mValidReq;
    private String mLastLeaf;
    private String mScopeID;
    private String mCourseID;
    private String mLearnerID;
    private SeqActivity mCurActivity;
    private SeqActivity mFirstCandidate;
    private SeqActivity mSuspendAll;
    private Hashtable mActivityMap;
    private Vector mObjSet;
    private Hashtable mObjMap;
    private boolean mObjScan;
    private boolean isScrom = true;

    static 
    {
        _Debug = false;
    }

    public SeqActivityTree()
    {
        mRoot = null;
        mValidReq = null;
        mLastLeaf = null;
        mScopeID = null;
        mCourseID = null;
        mLearnerID = null;
        mCurActivity = null;
        mFirstCandidate = null;
        mSuspendAll = null;
        mActivityMap = null;
        mObjSet = null;
        mObjMap = null;
        mObjScan = false;
        isScrom = true;
    }

    public SeqActivityTree(String iCourseID, String iLearnerID, String iScopeID, SeqActivity iRoot)
    {
        mRoot = null;
        mValidReq = null;
        mLastLeaf = null;
        mScopeID = null;
        mCourseID = null;
        mLearnerID = null;
        mCurActivity = null;
        mFirstCandidate = null;
        mSuspendAll = null;
        mActivityMap = null;
        mObjSet = null;
        mObjMap = null;
        mObjScan = false;
        isScrom = true;
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - constructor");
            System.out.println("  ::--> Course ID     : " + iCourseID);
            System.out.println("  ::--> Student ID    : " + iLearnerID);
            System.out.println("  ::--> Scope ID      : " + iScopeID);
        }
        mCourseID = iCourseID;
        mLearnerID = iLearnerID;
        mScopeID = iScopeID;
        mRoot = iRoot;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - constructor");
    }

    public void setLearnerID(String iLearnerID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setLearnerID");
            System.out.println("  ::--> Learner ID    : " + iLearnerID);
        }
        mLearnerID = iLearnerID;
        buildActivityMap();
        if(mActivityMap != null && iLearnerID != null)
        {
            SeqActivity act;
            for(Enumeration enume = mActivityMap.elements(); enume.hasMoreElements(); act.setLearnerID(iLearnerID))
                act = (SeqActivity)enume.nextElement();

        }
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setLearnerID");
    }

    public String getLearnerID()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - getLearnerID");
            System.out.println("  ::--> Learner ID    : " + mLearnerID);
            System.out.println("  :: SeqActivityTree  --> END   - getLearnerID");
        }
        return mLearnerID;
    }

    public void setCourseID(String iCourseID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setCourseID");
            System.out.println("  ::--> Course ID     : " + iCourseID);
        }
        mCourseID = iCourseID;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setCourseID");
    }

    public String getCourseID()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - getCourseID");
            System.out.println("  ::--> Student ID    : " + mCourseID);
            System.out.println("  :: SeqActivityTree  --> END   - getCourseID");
        }
        return mCourseID;
    }

    public void setScopeID(String iScopeID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setScopeID");
            System.out.println("  ::-->  " + iScopeID);
        }
        mScopeID = iScopeID;
        if(mScopeID != null)
        {
            buildActivityMap();
            if(mActivityMap != null)
            {
                SeqActivity act;
                for(Enumeration enume = mActivityMap.elements(); enume.hasMoreElements(); act.setScopeID(mScopeID))
                    act = (SeqActivity)enume.nextElement();

            }
        }
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setScopeID");
    }

    public String getScopeID()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - getScopeID");
            System.out.println("  ::--> " + mScopeID);
            System.out.println("  :: SeqActivityTree  --> END   - getScopeID");
        }
        return mScopeID;
    }

    public void dumpState()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivtyTree   --> BEGIN - dumpState");
            System.out.println("  ::--> Course ID:       " + mCourseID);
            System.out.println("  ::--> Student ID:      " + mLearnerID);
            System.out.println("  ::--> Scope ID  :      " + mScopeID);
            System.out.println("  ::--> Last Leaf:       " + mLastLeaf);
            if(mObjSet == null)
            {
                System.out.println("  ::--> Global Obj Set:       NULL");
            } else
            {
                System.out.println("  ::--> Global Obj Set:       [" + mObjSet.size() + "]");
                for(int i = 0; i < mObjSet.size(); i++)
                    System.out.println("\t" + (String)mObjSet.elementAt(i));

            }
            if(mSuspendAll == null)
                System.out.println("  ::--> SuspendAll:           NULL");
            else
                System.out.println("  ::--> SuspendAll:           " + mSuspendAll.getID());
            if(mCurActivity == null)
                System.out.println("  ::--> Current Activity:     NULL");
            else
                System.out.println("  ::--> Current Activity:     " + mCurActivity.getID());
            if(mFirstCandidate == null)
                System.out.println("  ::--> First Candidate:      NULL");
            else
                System.out.println("  ::--> First Candidate:      " + mFirstCandidate.getID());
            if(mRoot == null)
            {
                System.out.println("  ::--> Root:                 NULL");
            } else
            {
                System.out.println("  ::--> Activity Tree ::");
                SeqActivity walk = mRoot;
                long depth = 0L;
                Vector lookAt = new Vector();
                Vector depthTrack = new Vector();
                while(walk != null) 
                {
                    walk.dumpState();
                    if(walk.hasChildren(true))
                    {
                        lookAt.add(walk);
                        depthTrack.add(new Long(depth + 1L));
                    }
                    walk = walk.getNextSibling(true);
                    if(walk == null)
                        if(lookAt.size() != 0)
                        {
                            walk = (SeqActivity)lookAt.elementAt(0);
                            lookAt.remove(0);
                            walk = (SeqActivity)walk.getChildren(true).elementAt(0);
                            depth = ((Long)depthTrack.elementAt(0)).longValue();
                            depthTrack.remove(0);
                        } else
                        {
                            walk = null;
                        }
                }
            }
            System.out.println("  :: SeqActivityTree   --> END   - dumpState");
        }
    }

    void setRoot(SeqActivity iRoot)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setRoot");
            if(iRoot != null)
                System.out.println("  ::-->  " + iRoot.getID());
            else
                System.out.println("  ::-->  NULL root.");
        }
        mRoot = iRoot;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setRoot");
    }

    SeqActivity getRoot()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - getRoot");
            if(mRoot != null)
                System.out.println("  ::-->  " + mRoot.getID());
            else
                System.out.println("  ::-->  NULL Root");
            System.out.println("  :: SeqActivityTree  --> END   - getRoot");
        }
        return mRoot;
    }

    void setLastLeaf(String iLastLeaf)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setLastLeaf");
            System.out.println("  ::-->  " + iLastLeaf);
        }
        mLastLeaf = iLastLeaf;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setLastLeaf");
    }
    
    public void newsetLastLeaf(String iLastLeaf){
    	setLastLeaf(iLastLeaf);
    }

    String getLastLeaf()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - getLastLeaf");
            System.out.println("  ::-->  " + mLastLeaf);
            System.out.println("  :: SeqActivityTree  --> END   - GetLastLeaf");
        }
        return mLastLeaf;
    }

    void setValidRequests(ADLValidRequests iValidRequests)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setValidRequests");
            if(iValidRequests == null)
                System.out.println("  ::-->  NULL set");
        }
        mValidReq = iValidRequests;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setValidRequests");
    }

    ADLValidRequests getValidRequests()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - getValidRequests");
            if(mValidReq == null)
                System.out.println("  ::-->  NULL Set");
            System.out.println("  :: SeqActivityTree  --> END   - getValidRequests");
        }
        return mValidReq;
    }

    SeqActivity getCurrentActivity()
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - getCurrentActivity");
        if(_Debug)
        {
            if(mCurActivity != null)
                System.out.println("  ::-->  " + mCurActivity.getID());
            else
                System.out.println("  ::-->  NULL current activity");
            System.out.println("  :: SeqActivityTree  --> END   - getCurrentActivity");
        }
        return mCurActivity;
    }

    public void setCurrentActivity(SeqActivity iCurrent)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setCurrentActivity");
            if(iCurrent != null)
                System.out.println("  ::-->  " + iCurrent.getID());
            else
                System.out.println("  ::-->  NULL current activity.");
        }
        mCurActivity = iCurrent;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setCurrentActivity");
    }

    SeqActivity getFirstCandidate()
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - getFirstCandidate");
        if(_Debug)
        {
            if(mFirstCandidate != null)
                System.out.println("  ::-->  " + mFirstCandidate.getID());
            else
            if(mCurActivity != null)
                System.out.println("  ::--> [Cur] " + mCurActivity.getID());
            else
                System.out.println("  ::-->  NULL first candidate");
            System.out.println("  :: SeqActivityTree  --> END   - getFirstCandidate");
        }
        if(mFirstCandidate == null)
            return mCurActivity;
        else
            return mFirstCandidate;
    }

    public void setFirstCandidate(SeqActivity iFirst)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setFirstCandidate");
            if(iFirst != null)
                System.out.println("  ::-->  " + iFirst.getID());
            else
                System.out.println("  ::-->  NULL current activity.");
        }
        mFirstCandidate = iFirst;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setFirstCandidate");
    }

    void setSuspendAll(SeqActivity iSuspendTarget)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - setSuspendAll");
            if(iSuspendTarget != null)
                System.out.println("  ::-->  " + iSuspendTarget.getID());
            else
                System.out.println("  ::-->  NULL suspend target.");
        }
        mSuspendAll = iSuspendTarget;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setSuspendAll");
    }

    public SeqActivity getSuspendAll()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivityTree  --> BEGIN - getSuspendAll");
            if(mSuspendAll != null)
                System.out.println("  ::-->  " + mSuspendAll.getID());
            else
                System.out.println("  ::-->  NULL suspend all activity.");
            System.out.println("  :: SeqActivityTree  --> END   - getSuspendAll");
        }
        return mSuspendAll;
    }

    public SeqActivity getActivity(String iActivityID)
    {
        if(mActivityMap == null)
            buildActivityMap();
        SeqActivity temp = null;
        if(iActivityID != null)
            temp = (SeqActivity)mActivityMap.get(iActivityID);
/*        
        //07-01-15(mskim)modify
        if(temp == null){
        	buildActivityMap();
            if(iActivityID != null)
                temp = (SeqActivity)mActivityMap.get(iActivityID);

        }
*/        
        return temp;
    }

    public Vector getObjMap(String iObjID)
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - getObjMap");
        Vector actSet = null;
        if(!mObjScan)
        {
            scanObjectives();
            if(mObjMap != null && mObjMap.size() == 0)
                mObjMap = null;
        }
        if(mObjMap != null)
            actSet = (Vector)mObjMap.get(iObjID);
        if(_Debug)
        {
            if(actSet == null)
                System.out.println("  ::-->  NULL");
            else
                System.out.println("  ::--> [" + actSet.size() + "]");
            System.out.println("  :: SeqActivityTree  --> END   - getObjMap");
        }
        return actSet;
    }

    public Vector getGlobalObjectives()
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - getGlobalObjectives");
        if(!mObjScan)
            scanObjectives();
        if(_Debug)
        {
            if(mObjSet == null)
                System.out.println("  ::-->  NULL");
            else
                System.out.println("  ::--> [" + mObjSet.size() + "]");
            System.out.println("  :: SeqActivityTree  --> END   - getGlobalObjectives");
        }
        if(mObjSet != null && mObjSet.size() == 0)
            mObjSet = null;
        return mObjSet;
    }

    public void clearSessionState()
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - clearSessionState");
        mActivityMap = null;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - clearSessionState");
    }

    public void setDepths()
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - setDepths");
        if(mRoot != null)
        {
            SeqActivity walk = mRoot;
            int depth = 0;
            Vector lookAt = new Vector();
            Vector depths = new Vector();
            while(walk != null) 
            {
                if(walk.hasChildren(true))
                {
                    lookAt.add(walk);
                    depths.add(new Integer(depth + 1));
                }
                walk.setDepth(depth);
                walk = walk.getNextSibling(true);
                if(walk == null && lookAt.size() != 0)
                {
                    walk = (SeqActivity)lookAt.elementAt(0);
                    lookAt.remove(0);
                    depth = ((Integer)depths.elementAt(0)).intValue();
                    depths.remove(0);
                    walk = (SeqActivity)walk.getChildren(true).elementAt(0);
                }
            }
        }
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setDepths");
    }

    public void setTreeCount()
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - setTreeCount");
        if(mRoot != null)
        {
            SeqActivity walk = mRoot;
            int count = 0;
            Vector lookAt = new Vector();
            while(walk != null) 
            {
                count++;
                walk.setCount(count);
                if(walk.hasChildren(true))
                {
                    lookAt.add(walk);
                    walk = (SeqActivity)walk.getChildren(true).elementAt(0);
                } else
                {
                    walk = walk.getNextSibling(true);
                }
                for(; lookAt.size() != 0 && walk == null; walk = walk.getNextSibling(true))
                {
                    walk = (SeqActivity)lookAt.elementAt(0);
                    lookAt.remove(0);
                }

            }
        }
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - setTreeCount");
    }

    private void buildActivityMap()
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - buildActivityMap");
        if(mActivityMap != null)
            mActivityMap.clear();
        else
            mActivityMap = new Hashtable();
        if(mRoot != null)
            addChildActivitiestoMap(mRoot);
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - buildActivityMap");
    }

    private void addChildActivitiestoMap(SeqActivity iNode)
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - addChildActivitiestoMap");
        if(iNode != null)
        {
            Vector children = iNode.getChildren(true);
            int i = 0;
            if(_Debug)
                System.out.println("  ::--> Adding node : " + iNode.getID());
            mActivityMap.put(iNode.getID(), iNode);
            if(children != null)
                for(i = 0; i < children.size(); i++)
                    addChildActivitiestoMap((SeqActivity)children.elementAt(i));

        }
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - addChildActivitiestoMap");
    }

    private void scanObjectives()
    {
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> BEGIN - scanObjectives");
        SeqActivity walk = mRoot;
        Vector lookAt = new Vector();
        while(walk != null) 
        {
            if(walk.hasChildren(true))
                lookAt.add(walk);
            Vector objs = walk.getObjectives();
            if(objs != null)
            {
                for(int i = 0; i < objs.size(); i++)
                {
                    SeqObjective obj = (SeqObjective)objs.elementAt(i);
                    if(obj.mMaps != null)
                    {
                        for(int j = 0; j < obj.mMaps.size(); j++)
                        {
                            SeqObjectiveMap map = (SeqObjectiveMap)obj.mMaps.elementAt(j);
                            String target = map.mGlobalObjID;
                            if(mObjSet == null)
                            {
                                mObjSet = new Vector();
                                mObjSet.add(target);
                            } else
                            {
                                boolean found = false;
                                for(int k = 0; k < mObjSet.size() && !found; k++)
                                {
                                    String ID = (String)mObjSet.elementAt(k);
                                    found = ID.equals(target);
                                }

                                if(!found)
                                    mObjSet.add(target);
                            }
                            if(map.mReadStatus || map.mReadMeasure)
                            {
                                if(mObjMap == null)
                                    mObjMap = new Hashtable();
                                Vector actList = (Vector)mObjMap.get(target);
                                if(actList == null)
                                    actList = new Vector();
                                actList.add(walk.getID());
                                mObjMap.put(target, actList);
                            }
                        }

                    }
                }

            }
            walk = walk.getNextSibling(true);
            if(walk == null && lookAt.size() != 0)
            {
                walk = (SeqActivity)lookAt.elementAt(0);
                lookAt.remove(0);
                walk = (SeqActivity)walk.getChildren(true).elementAt(0);
            }
        }
        mObjScan = true;
        if(_Debug)
            System.out.println("  :: SeqActivityTree  --> END   - scanObjectives");
    }

    public SeqActivity getMCurActivity()
    {
        return mCurActivity;
    }

    public boolean isScrom()
    {
        return isScrom;
    }

    public void setScrom(boolean isScrom)
    {
    	System.out.println("testtesttesttest");
        this.isScrom = isScrom;
    }

}