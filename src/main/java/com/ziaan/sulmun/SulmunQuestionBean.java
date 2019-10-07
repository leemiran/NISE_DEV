//**********************************************************
//  1. 제      목: 설문문제관리
//  2. 프로그램명: SulmunQuestionBean.java
//  3. 개      요: 설문문제관리
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 이창훈 2006. 4. 17
//  7. 수      정: 이창훈 2006. 4. 17
//**********************************************************

package com.ziaan.sulmun;

import java.util.*;

import com.ziaan.common.*;
import com.ziaan.library.*;
import com.ziaan.system.*;
import com.ziaan.sulmun.*;

/**
 * 운영자의 코드 관리 관련 DatabaseExecute Class
 *
 * @date   : 2006. 4
 * @author : s. c. park
 */
public class SulmunQuestionBean {

  public SulmunQuestionBean() {}

    /**
    설문문제 리스트
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectQuestionList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;
        String v_subj       = box.getString("p_subj");
        
        list = new ArrayList();

        try {
          connMgr = new DBConnectionManager();

          sql = "select ";
          sql+= "  sulnum  , subj    , distcode , sultype    , ";
          sql+= "  sultext , selcount, selmax   , sulreturn  , ";
          sql+= "  scalecode, luserid , ldate, ";
          sql+= "  get_codenm('"+SulmunBean.DIST_CODE+"', distcode) distcodenm, get_codenm('"+SulmunBean.SUL_TYPE+"', sultype) sultypenm ";
          sql+= "from tu_sul    ";
          sql+= "where ";
          sql+= "  subj = '"+v_subj+"'";
          sql+= " order by sulnum ";

          //System.out.println("sql===>>>"+sql);

		  ls = connMgr.executeQuery(sql);

		  while (ls.next()) {
              dbox = ls.getDataBox();
              list.add(dbox);
          }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    public boolean insertQuestion(RequestBox box) throws Exception {
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        boolean isCommit = false;
        DatabaseExecute db = null;
        
        String disc_type = SulmunBean.SUBJECT_QUESTION; //서술형코드

        String v_subj       = box.getStringDefault("p_subj", SulmunBean.DEFAULT_SUBJ);
        int    v_sulnum     = 0;
        String v_distcode   = box.getString("p_distcode");
        String v_sultype    = box.getString("p_sultype");
        String v_sultext    = box.getString("p_sultext");

        String v_sulreturn = box.getString("p_sulreturn");

		int v_selcount    = box.getInt("p_selcount");
        int v_selmax      = box.getInt("p_selmax");
		int v_scalecode   = box.getInt("p_scalecode");
		
        Vector v_seltexts   = box.getVector("p_seltext");
        Vector v_selpoints  = box.getVector("p_selpoint");
        
        Vector v_selnum = new Vector();
        Vector v_selpoint = new Vector();
        Vector v_seltext  = new Vector();

		String v_luserid = box.getSession("userid");


       try {
         //box.put("notTransaction", new Boolean(true));
           v_sulnum = getSulnumSeq();
           //System.out.println("v_sulnum====>>>>>>>>>>>"+v_sulnum);
            
            db = new DatabaseExecute(box);

            //////////////////////////////////   tu_sul table 에 입력  ///////////////////////////////////////////////////////////////////

            sql1 =  " INSERT INTO TU_SUL (      ";
            sql1 += "  SULNUM, SUBJ, DISTCODE, ";
            sql1 += "  SULTYPE, SULTEXT, SELCOUNT, ";
            sql1 += "  SELMAX, SULRETURN, SCALECODE, ";
            sql1 += "  LUSERID, LDATE) ";
            sql1 += "VALUES ( ?, ?, ?,";
            sql1 += "         ?, ?, ?,";
            sql1 += "         ?, ?, ?,";
            sql1 += "         ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
            
            
            PreparedBox pbox1 = new PreparedBox("preparedbox");

            pbox1.setInt   (1, v_sulnum);
            pbox1.setString(2, v_subj);
            pbox1.setString(3, v_distcode);
            pbox1.setString(4, v_sultype);
            pbox1.setString(5, v_sultext);
            pbox1.setInt   (6, v_selcount);
            pbox1.setInt   (7, v_selmax);
            pbox1.setString(8, v_sulreturn);
            pbox1.setInt   (9, v_scalecode);
            pbox1.setString(10, v_luserid);
            
            PreparedBox pbox2 = new PreparedBox("preparedbox");
            
            //주관식이 아닐경우만 등록한다.
            if(!v_sultype.equals(disc_type)){ 
            sql2 =  " INSERT INTO TU_SULSEL ( ";
            sql2+=  "   SULNUM, SELNUM, SELTEXT, ";
            sql2+=  "   SELPOINT, LUSERID, LDATE) ";
            sql2+=  " VALUES ( ";
            sql2+=  "   ?, ?, ?,";
            sql2+=  "   ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";
            
            
            
            Enumeration em = v_seltexts.elements();
            Enumeration em1 = v_selpoints.elements();
            
            String v_seltext_tmp   = "";
            String v_selpoint_tmp  = "";
            int i = 0;
            while(em.hasMoreElements()){
              v_seltext_tmp   = (String)em.nextElement();
              v_selpoint_tmp  = (String)em1.nextElement();

              if (!v_seltext_tmp.trim().equals("")) {
                v_selnum.add(new Integer(i+1));
                v_selpoint.add(new Integer(Integer.parseInt(v_selpoint_tmp)));
                v_seltext.add(v_seltext_tmp);
                i++;
              }
              
            }

            //----------------------   sulsel table Insert  --------------------------------
            pbox2.setInt   (1, v_sulnum);
            pbox2.setVector(2, v_selnum);
            pbox2.setVector(3, v_seltext);
            pbox2.setVector(4, v_selpoint);
            pbox2.setString(5, v_luserid);
            //-----------------------------------------------------------------------
            }else{
              sql2 = UpdateSpareTable.setSpareSql();
              UpdateSpareTable.setSparePbox(pbox2);

            }
            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2 },  new String [] {sql1, sql2});
            
            String updateSQL = db.getSQLString();
            String updateCount = db.getIsCommitString();
            
            System.out.println(updateSQL);
            System.out.println(updateCount);

       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
       }
       return isCommit;
   }
   
   
   
    public boolean updateQuestion(RequestBox box) throws Exception {
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        boolean isCommit = false;
        DatabaseExecute db = null;
        
        String disc_type = SulmunBean.SUBJECT_QUESTION; //서술형코드
        int    sulsel_oldcnt = 0; 
        
        String v_subj       = box.getStringDefault("p_subj", SulmunBean.DEFAULT_SUBJ);
        int    v_sulnum     = box.getInt("p_sulnum");
        String v_distcode   = box.getString("p_distcode");
        String v_sultype    = box.getString("p_sultype");
        String v_sultext    = box.getString("p_sultext");

        String v_sulreturn = box.getString("p_sulreturn");

		int v_selcount    = box.getInt("p_selcount");
        int v_selmax      = box.getInt("p_selmax");
		int v_scalecode   = box.getInt("p_scalecode");
		
        Vector v_seltexts   = box.getVector("p_seltext");
        Vector v_selpoints  = box.getVector("p_selpoint");
        
        Vector v_selnum = new Vector();
        Vector v_selpoint = new Vector();
        Vector v_seltext  = new Vector();

		String v_luserid = box.getSession("userid");


       try {
         //box.put("notTransaction", new Boolean(true));
           //v_sulnum = getSulnumSeq();
           //System.out.println("v_sulnum====>>>>>>>>>>>"+v_sulnum);
            
            db = new DatabaseExecute(box);

            //////////////////////////////////   tu_sul table 에 입력  ///////////////////////////////////////////////////////////////////

            sql1 =  "update tu_sul set      ";
            sql1+=  "  DISTCODE = ?, SULTYPE = ?, SULTEXT = ?, SELCOUNT = ?, ";
            sql1+=  "  SELMAX = ?, SULRETURN = ?, SCALECODE = ?, LUSERID = ?, ";
            sql1+=  "  LDATE = to_char(sysdate, 'YYYYMMDDHH24MISS')";
            sql1+=  "where ";
            sql1+=  "  sulnum = ? ";
            
            PreparedBox pbox1 = new PreparedBox("preparedbox");

            pbox1.setString(1, v_distcode);
            pbox1.setString(2, v_sultype);
            pbox1.setString(3, v_sultext);
            pbox1.setInt   (4, v_selcount);
            pbox1.setInt   (5, v_selmax);
            pbox1.setString(6, v_sulreturn);
            pbox1.setInt   (7, v_scalecode);
            pbox1.setString(8, v_luserid);
            pbox1.setInt   (9, v_sulnum);


            PreparedBox pbox2 = new PreparedBox("preparedbox");
            PreparedBox pbox3 = new PreparedBox("preparedbox");

            sulsel_oldcnt = getSulselCnt(v_sulnum);
            
            if(sulsel_oldcnt > 0){
              sql2 =  " DELETE FROM TU_SULSEL WHERE SULNUM = ? ";
              pbox2.setInt   (1, v_sulnum);
            }else{
              sql2 = UpdateSpareTable.setSpareSql();
              UpdateSpareTable.setSparePbox(pbox2);
            }
            

            //주관식이 아닐경우만 등록한다.
            if(!v_sultype.equals(disc_type)){ 
            

            sql3 =  " INSERT INTO TU_SULSEL ( ";
            sql3+=  "   SULNUM, SELNUM, SELTEXT, ";
            sql3+=  "   SELPOINT, LUSERID, LDATE) ";
            sql3+=  " VALUES ( ";
            sql3+=  "   ?, ?, ?,";
            sql3+=  "   ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";
            
            Enumeration em = v_seltexts.elements();
            Enumeration em1 = v_selpoints.elements();
            
            String v_seltext_tmp   = "";
            String v_selpoint_tmp  = "";
            int i = 0;
            while(em.hasMoreElements()){
              v_seltext_tmp   = (String)em.nextElement();
              v_selpoint_tmp  = (String)em1.nextElement();

              if (!v_seltext_tmp.trim().equals("")) {
                v_selnum.add(new Integer(i+1));
                v_selpoint.add(new Integer(Integer.parseInt(v_selpoint_tmp)));
                v_seltext.add(v_seltext_tmp);
                i++;
              }
              
            }

            //----------------------   sulsel table Insert  --------------------------------
            pbox3.setInt   (1, v_sulnum);
            pbox3.setVector(2, v_selnum);
            pbox3.setVector(3, v_seltext);
            pbox3.setVector(4, v_selpoint);
            pbox3.setString(5, v_luserid);
            //-----------------------------------------------------------------------
            
            }else{
              sql3 = UpdateSpareTable.setSpareSql();
              UpdateSpareTable.setSparePbox(pbox3);
            }
            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2, pbox3 },  new String [] {sql1, sql2, sql3});
            
            String updateSQL = db.getSQLString();
            String updateCount = db.getIsCommitString();
            
            System.out.println(updateSQL);
            System.out.println(updateCount);

       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
       }
       return isCommit;
   }
   
   
   
    public boolean deleteQuestion(RequestBox box) throws Exception {
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        boolean isCommit = false;
        DatabaseExecute db = null;
        
        String disc_type = SulmunBean.SUBJECT_QUESTION; //서술형코드
        int    sulsel_oldcnt = 0; 
        
        String v_subj       = box.getStringDefault("p_subj", SulmunBean.DEFAULT_SUBJ);
        int    v_sulnum     = box.getInt("p_sulnum");
        String v_distcode   = box.getString("p_distcode");
        String v_sultype    = box.getString("p_sultype");
        String v_sultext    = box.getString("p_sultext");

        String v_sulreturn = box.getString("p_sulreturn");

		int v_selcount    = box.getInt("p_selcount");
        int v_selmax      = box.getInt("p_selmax");
		int v_scalecode   = box.getInt("p_scalecode");
		
        Vector v_seltexts   = box.getVector("p_seltext");
        Vector v_selpoints  = box.getVector("p_selpoint");
        
        Vector v_selnum = new Vector();
        Vector v_selpoint = new Vector();
        Vector v_seltext  = new Vector();

		String v_luserid = box.getSession("userid");


       try {
            db = new DatabaseExecute(box);

            //////////////////////////////////   tu_sul table 에 입력  ///////////////////////////////////////////////////////////////////

            sql1 =  "DELETE FROM TU_SUL ";
            sql1+=  "WHERE ";
            sql1+=  "  SULNUM = ? ";
            
            PreparedBox pbox1 = new PreparedBox("preparedbox");
            pbox1.setInt   (1, v_sulnum);

            sulsel_oldcnt = getSulselCnt(v_sulnum);
            PreparedBox pbox2 = new PreparedBox("preparedbox");
            if(sulsel_oldcnt > 0){
              sql2 =  " DELETE FROM TU_SULSEL WHERE SULNUM = ? ";
              pbox2.setInt   (1, v_sulnum);
            }else{
              sql2 = UpdateSpareTable.setSpareSql();
              UpdateSpareTable.setSparePbox(pbox2);
            }
            
            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql1, sql2});
            
            String updateSQL = db.getSQLString();
            String updateCount = db.getIsCommitString();
            
            System.out.println(updateSQL);
            System.out.println(updateCount);

       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
       }
       return isCommit;
   }
   
    /**
    @param box          receive from the form object and session
    @return QuestionExampleData   설문문제
    */
    public ArrayList selectQuestionExample(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = new ArrayList();

        //String v_gubun    = box.getStringDefault("p_gubun", SulmunBean.DEFAULT_SUBJ);
        String v_subj       = box.getStringDefault("p_subj", SulmunBean.DEFAULT_SUBJ);
        String v_grcode     = box.getString("p_grcode");
        int    v_sulnum   = box.getInt("p_sulnum");
        
        try {
            
             connMgr = new DBConnectionManager();
             list = getSelnums(connMgr, v_grcode,  v_subj, v_sulnum);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
   public ArrayList getSelnums(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulnum) throws Exception {
        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        try {
            sql = "select a.subj, a.sulnum, a.distcode, a.sultype, ";
            sql+= "  a.sultext, a.sulreturn, a.selmax, ";
            sql+= "  a.scalecode, b.selnum, b.seltext, b.selpoint ";
            sql+= "  from tu_sul     a, ";
            sql+= "       tu_sulsel  b  ";
            sql+= " where ";
            sql+= "   a.sulnum = b.sulnum(+)  ";
            sql+= "   and a.subj   = " +   SQLString.Format(p_subj);
            sql+= "   and a.sulnum = " +   SQLString.Format(p_sulnum);
            sql+= " order by b.selnum ";
            ls = connMgr.executeQuery(sql);
 
		   while (ls.next()) {
                    dbox = ls.getDataBox();
                    list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return list;
    }
   
   public int getSulnumSeq() throws Exception {
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","sulnum");
        maxdata.put("seqtable","tu_sul");
        maxdata.put("paramcnt","0");

        return SelectionUtil.getSeq(maxdata);
    }
    
    public int getSulselCnt(int p_sulnum) throws Exception {
        Hashtable cntdata = new Hashtable();
        cntdata.put("seqtable","tu_sulsel");
        cntdata.put("paramcnt","1");
        cntdata.put("param0","sulnum");
        cntdata.put("sulnum",   SQLString.Format(String.valueOf(p_sulnum)));
        return SelectionUtil.getTableCount(cntdata);
    }

    //문제에 대한 보기 가져오기
    public ArrayList selectOption(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;    	
        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        try {
            connMgr = new DBConnectionManager();
            
            sql = " select selnum, seltext, selpoint ";
            sql+= "  from tu_sulsel ";
            sql+= " where ";
            sql+= "   sulnum = " +   SQLString.Format(box.getInt("p_sulnum"));
            sql+= " order by selnum ";

            ls = connMgr.executeQuery(sql);
 
		   while (ls.next()) {
                    dbox = ls.getDataBox();
                    list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return list;
    }
    
    /**
        설문답변 입력했는지 여부
    @param box          receive from the form object and session
    @return boolean
    */
    public boolean selectIsVoted(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;       	
        String sql = "";
        boolean isCommit = false;
        ListSet ls = null;
        DatabaseExecute db = null;

        try {
        	//설문에 답변했었는지 확인
            connMgr = new DBConnectionManager();
            
            sql = " select userid ";
            sql+= "  from tu_suleach ";
            sql+= " where ";
            sql+= "   sulpapernum = '" + box.getString("sulpapernum")+"'";
            sql+= "   and userid = '" + box.getString("userid")+"'";

            ls = connMgr.executeQuery(sql);
            
            if (ls.next()) { //답변 했었으면
            	isCommit=true;
            } else {
            	isCommit=false; //답변 한적이 없다면
            }
        }
        catch (Exception ex) {
        	ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
        	throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }

    /**
        설문답변 입력
    @param box          receive from the form object and session
    @return boolean
    */
    public boolean insertAnswer(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        boolean isCommit = false;
        ListSet ls = null;
        DatabaseExecute db = null;

        try {
            //삽입
        	db = new DatabaseExecute(box);
            
            sql1 = "";
            sql1 += "\r\n INSERT INTO TU_SULEACH (sulpapernum, userid, sulnum, answer, luserid, ldate) ";
            sql1 += "\r\n values (?,?,?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS')) ";     

            PreparedBox pbox1 = new PreparedBox("preparedbox");
        
	        pbox1.setInt(1, Integer.parseInt(box.getString("sulpapernum")));
	        pbox1.setString(2, box.getString("userid"));
	        pbox1.setInt(3, Integer.parseInt(box.getString("sulnum")));
	        pbox1.setString(4, box.getString("answer"));
	        pbox1.setString(5, box.getSession("userid"));

	        //CLOB 단일 테이블 입력 시 Transaction 처리 가능하게 하는 부분 선언
	        String tempsql = "";
	        PreparedBox temppbox = new PreparedBox("preparedbox");
	        tempsql = UpdateSpareTable.setSpareSql();
	        UpdateSpareTable.setSparePbox(temppbox);
	        
	    	isCommit = db.executeUpdate(new PreparedBox [] {pbox1, temppbox},  new String [] {sql1, tempsql});
	    }
	    catch (Exception ex) {             
	        ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
	        throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
	    }
	    return isCommit;
    }
    
    /**
    @param box          receive from the form object and session
    @return PaperData 설문 답변 가져오기
    */
    public ArrayList getAnswer(RequestBox box) throws Exception {
    	DatabaseExecute db = null;
    	ArrayList list = null;
        String sql  = "";
        DataBox     dbox = null;

        try {
            db = new DatabaseExecute();
            
            sql = "";
            sql += " select answer from tu_suleach ";
            sql += " 	where sulpapernum= "+box.getInt("p_sulpapernum");
            sql += "		and sulnum="+box.getInt("p_sulnum");

            list = db.executeQueryList(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }

        return list;
    }    
}