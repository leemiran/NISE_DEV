// **********************************************************
// 1. 제      목:
// 2. 프로그램명: SulmunTargetQuestionBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-18
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.research;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.system.SelectionUtil;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SulmunTargetQuestionBean { 

    public SulmunTargetQuestionBean() { }

    /**
    대상자설문 문제 리스트
    @param box          receive from the form object and session
    @return ArrayList 
    */
    public ArrayList selectQuestionList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_gubun    = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj     = box.getStringDefault("s_subjcourse", "ALL");
        String v_distcode = box.getStringDefault("s_distcode","ALL");
        String v_grcode = box.getString("s_grcode");
        String v_action   = box.getStringDefault("p_action",  "change");
    
        try { 
            if ( v_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                v_subj = v_gubun;
                list = selectQuestionList(connMgr, v_grcode, v_subj, v_distcode);
            } else { 
                list = new ArrayList();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }



    /**
    대상자설문 문제 리스트
    @param box          receive from the form object and session
    @return ArrayList 
    */
    public ArrayList selectQuestionList(DBConnectionManager connMgr, String p_grcode, String p_subj, String p_distcode) throws Exception { 
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            sql = "select a.subj,     a.sulnum,  a.grcode, ";
            sql += "       a.distcode, b.codenm  distcodenm, ";
            sql += "       a.sultype,  c.codenm  sultypenm,  ";
            sql += "       a.sultext    ";
            sql += "  from tz_sul    a, ";
            sql += "       tz_code   b, ";
            sql += "       tz_code   c  ";
            sql += "   where a.distcode  = b.code ";
			sql += "   and a.sultype  = c.code ";
            sql += "   and a.grcode    = " + SQLString.Format(p_grcode);
			sql += "   and b.gubun    = " + SQLString.Format(SulmunTargetBean.DIST_CODE);
            sql += "   and c.gubun    = " + SQLString.Format(SulmunTargetBean.SUL_TYPE);
            sql += "   and c.levels    =  1 ";
            if ( !p_subj.equals("ALL") ) { 
                sql += "   and a.subj     = " + SQLString.Format(p_subj);
            }
            if ( !p_distcode.equals("ALL") ) { 
                sql += "   and a.distcode = " + SQLString.Format(p_distcode);
            }
            sql += " order by a.sulnum ";
            ls = connMgr.executeQuery(sql); System.out.println(sql);

			while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }

    /**
    대상자설문 문제 리스트
    @param box          receive from the form object and session
    @return ArrayList 
    */
    public ArrayList selectQuestionExample(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_gubun    = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj     = box.getString("p_subj");
        String v_grcode     = box.getString("p_grcode");
        int    v_sulnum   = box.getInt("p_sulnum");
        String v_action   = box.getStringDefault("p_action","change");

		try { 
            if ( v_action.equals("go") && v_sulnum > 0 ) { 
                connMgr = new DBConnectionManager();
                v_subj = v_gubun;
                list = getSelnums(connMgr, v_grcode, v_subj, v_sulnum);
            } else { 
                list = new ArrayList();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }



    /**
    대상자설문 문제 리스트
    @param box          receive from the form object and session
    @return ArrayList 
    */
    public ArrayList getSelnums(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulnum) throws Exception { 
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            sql = "select a.subj, a.sulnum, a.distcode, a.sultype, a.sultext, a.sulreturn, a.selmax, a.scalecode, b.selnum, b.seltext, b.selpoint ";
            sql += "  from tz_sul     a, ";
            sql += "       tz_sulsel  b  ";
            sql += " where a.subj   = b.subj( +)    ";
            sql += "   and a.sulnum = b.sulnum( +)  ";
            sql += "   and a.grcode = b.grcode( +)  ";
            sql += "   and a.grcode   = " +   SQLString.Format(p_grcode);
			sql += "   and a.subj   = " +   SQLString.Format(p_subj);
            sql += "   and a.sulnum = " +   SQLString.Format(p_sulnum);
            sql += " order by b.selnum ";
            ls = connMgr.executeQuery(sql);
 
			while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }

    /**
    대상자설문 문제 리스트
    @param box          receive from the form object and session
    @return ArrayList 
    */
    public ArrayList selectQuestionExampleList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        String v_subj     = box.getString("p_subj");
        int    v_sulnum   = box.getInt("p_sulnum");

        try { 
            sql = "select subj,  sulnum,  selnum,  seltext, selpoint ";
            sql += "  from tz_sulsel ";
            sql += " where subj   = " + SQLString.Format(v_subj);
            sql += "   and sulnum = " + SQLString.Format(v_sulnum);
            sql += " order by selnum ";

            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    대상자설문 문제 등록 
    @param box          receive from the form object and session
    @return int 
    */
    public int insertTZ_sul(DBConnectionManager connMgr, String p_subj, String p_grcode, int p_sulnum, String p_distcode, String p_sultype, String p_sultext, int p_selcount, int p_selmax, String p_sulreturn, int p_scalecode, String p_luserid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_SUL table
            sql =  "insert into TZ_SUL(subj, grcode, sulnum, distcode, sultype, sultext, selcount, selmax, sulreturn, scalecode, luserid, ldate) ";
            sql +=  " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_grcode);
            pstmt.setInt   (3, p_sulnum);
            pstmt.setString(4, p_distcode);
            pstmt.setString(5, p_sultype);
            pstmt.setString(6, p_sultext);
            pstmt.setInt(7, p_selcount);
            pstmt.setInt(8, p_selmax);
            pstmt.setString(9, p_sulreturn);
            pstmt.setInt(10, p_scalecode);
            pstmt.setString(11, p_luserid);
            pstmt.setString(12, FormatDate.getDate("yyyyMMddHHmmss") );
 
			isOk = pstmt.executeUpdate();
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
       }
       return isOk;
    }



    /**
    대상자설문 문제 수정  
    @param box          receive from the form object and session
    @return int 
    */
    public int updateTZ_sul(DBConnectionManager connMgr, String p_subj, String p_grcode, int p_sulnum, String p_distcode, String p_sultype, String p_sultext, int p_selcount, int p_selmax, String p_sulreturn, int p_scalecode, String p_luserid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // update TZ_SUL table
            sql =  " update TZ_SUL ";
            sql +=  "    set distcode = ?, ";
            sql +=  "        sultype  = ?, ";
            sql +=  "        sultext  = ?, ";
            sql +=  "        selcount  = ?, ";
            sql +=  "        selmax  = ?, ";
            sql +=  "        sulreturn  = ?, ";
            sql +=  "        scalecode  = ?, ";
            sql +=  "        luserid  = ?, ";
            sql +=  "        ldate    = ?  ";
            sql +=  "  where subj     = ?  ";
            sql +=  "    and grcode   = ?  ";
            sql +=  "    and sulnum   = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_distcode);
            pstmt.setString(2, p_sultype);
            pstmt.setString(3, p_sultext);
            pstmt.setInt(4, p_selcount);
            pstmt.setInt(5, p_selmax);
            pstmt.setString(6, p_sulreturn);
            pstmt.setInt(7, p_scalecode);
            pstmt.setString(8, p_luserid);
            pstmt.setString(9, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(10, p_subj);
            pstmt.setString(11, p_grcode);
            pstmt.setInt   (12, p_sulnum);

            isOk = pstmt.executeUpdate();
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
       }
       return isOk;
    }


    /**
    대상자설문 문제 수정  
    @param box          receive from the form object and session
    @return int 
    */
    public int updateTZ_sulS(DBConnectionManager connMgr, String p_subj, String p_grcode, int p_sulnum, String p_distcode, String p_sultype, String p_sultext, int p_selcount, int p_selmax, String p_sulreturn, int p_scalecode, String p_luserid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // update TZ_SUL table
            sql =  " update TZ_SUL ";
            sql +=  "    set sultype = ?, ";
            sql +=  "        scalecode  = ?, ";
			sql +=  "        luserid  = ?, ";
            sql +=  "        ldate    = ?  ";
            sql +=  "  where subj     = ?  ";
            sql +=  "    and grcode   = ?  ";
            sql +=  "    and sulnum   = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_sultype);
            pstmt.setInt(2, p_scalecode);
			pstmt.setString(3, p_luserid);
            pstmt.setString(4, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(5, p_subj);
            pstmt.setString(6, p_grcode);
            pstmt.setInt   (7, p_sulnum);

            isOk = pstmt.executeUpdate();
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
       }
       return isOk;
    }


    /**
    대상자설문 문제 삭제  
    @param box          receive from the form object and session
    @return int 
    */
    public int deleteTZ_sul(DBConnectionManager connMgr, String p_subj, String p_grcode, int p_sulnum, String p_duserid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
		ListSet             ls      = null;
        int isOk = 0;

        try { 
            // 삭제체크
            sql = " select sulnums  from tz_sulpaper where  subj='TARGET' ";
            sql += " and ((sulnums like '%" +SQLString.Format(p_sulnum) + ",%') or (sulnums  like '%," +SQLString.Format(p_sulnum) + ",%') or (sulnums = '" +SQLString.Format(p_sulnum) + "') or (sulnums like '%," +SQLString.Format(p_sulnum) + "%')) ";
            ls = connMgr.executeQuery(sql);
    		if ( ls.next() ) { 
    		    isOk = -2;
    		}
            
            if ( isOk == 0) { 
				sql =  " delete from TZ_SUL ";
				sql +=  "  where subj     = ?  ";
				sql +=  "    and grcode   = ?  ";
				sql +=  "    and sulnum   = ?  ";

				pstmt = connMgr.prepareStatement(sql);
				pstmt.setString(1, p_subj);
				pstmt.setString   (2, p_grcode);
				pstmt.setInt   (3, p_sulnum);

				isOk = pstmt.executeUpdate();
			}
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       }
       finally { 
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
       }
       return isOk;
    }

    /**
    대상자설문 문제 보기 등록 
    @param box          receive from the form object and session
    @return int 
    */
    public int insertTZ_sulsel(PreparedStatement pstmt, String p_subj, String p_grcode, int p_sulnum, int p_selnum, String p_seltext, int p_selpoint, String p_luserid) throws Exception { 
        int isOk = 0;

        try { 
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_grcode);
            pstmt.setInt   (3, p_sulnum);
            pstmt.setInt   (4, p_selnum);
            pstmt.setString(5, p_seltext);
            pstmt.setInt   (6, p_selpoint);
            pstmt.setString(7, p_luserid);
            pstmt.setString(8, FormatDate.getDate("yyyyMMddHHmmss") );
            isOk = pstmt.executeUpdate();
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception(ex.getMessage() );
       }
       return isOk;
    }


    /**
    대상자설문 문제 보기 삭제  
    @param box          receive from the form object and session
    @return int 
    */
    public int deleteTZ_sulsel(DBConnectionManager connMgr, String p_subj, String p_grcode, int p_sulnum) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 

            // delete TZ_SUL table
            sql =  " delete from TZ_SULSEL ";
            sql +=  "  where subj     = ?  ";
            sql +=  "    and grcode   = ?  ";
            sql +=  "    and sulnum   = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_grcode);
            pstmt.setInt   (3, p_sulnum);

            isOk = pstmt.executeUpdate();
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception(ex.getMessage() );
       }
       finally { 
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
       }
       return isOk;
    }


    /**
    대상자설문 문제 등록 
    @param box          receive from the form object and session
    @return int 
    */
    public int insertQuestion(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_gubun    = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj       = box.getString("p_subj");
         v_subj = v_gubun;
 
		String v_grcode       = box.getString("p_grcode");

        int    v_sulnum     = 0;
        String v_distcode   = box.getString("p_distcode");
        String v_sultype    = box.getString("p_sultype");
        String v_sultext    = box.getString("p_sultext");

        String v_sulreturn = "Y";

        int    v_selnum     = 0;
		int v_selcount    = 0;
        int v_selmax    = 0;
		int v_scalecode    = 0;
        String v_seltext    = "";
        Vector v_seltexts   = null;
		Enumeration em      = null;
        String v_selpoint    = "";
        Vector v_selpoints   = null;
		Enumeration em1      = null;
        
		if ( v_sultype.equals("1") ) { 
        v_selcount    = box.getInt("p_selcount1");
        v_selmax    = box.getInt("p_selmax1");
        v_scalecode    = box.getInt("p_scalecode1");
        v_seltexts   = box.getVector("p_seltext1");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint1");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("2") ) { 
        v_selcount    = box.getInt("p_selcount2");
        v_selmax    = box.getInt("p_selmax2");
        v_scalecode    = box.getInt("p_scalecode2");
        v_seltexts   = box.getVector("p_seltext2");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint2");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("3") ) { 
        v_selcount    = box.getInt("p_selcount3");
        v_selmax    = box.getInt("p_selmax3");
        v_scalecode    = box.getInt("p_scalecode3");
        v_seltexts   = box.getVector("p_seltext3");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint3");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("4") ) { 
        v_selcount    = box.getInt("p_selcount4");
        v_selmax    = box.getInt("p_selmax4");
        v_scalecode    = box.getInt("p_scalecode4");
        v_seltexts   = box.getVector("p_seltext4");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint4");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("5") ) { 
        v_selcount    = box.getInt("p_selcount5");
        v_selmax    = box.getInt("p_selmax5");
        v_scalecode    = box.getInt("p_scalecode");
        v_seltexts   = box.getVector("p_seltext5");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint5");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("6") ) { 
        v_selcount    = box.getInt("p_selcount6");
        v_selmax    = box.getInt("p_selmax6");
        v_scalecode    = box.getInt("p_scalecode");
        v_seltexts   = box.getVector("p_seltext6");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint6");
		em1      = v_selpoints.elements();
		}
        
		String v_luserid    = box.getSession("userid");

        try { 
            v_sulnum = getSulnumSeq(v_subj, v_grcode);

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            isOk = insertTZ_sul(connMgr, v_subj, v_grcode,  v_sulnum, v_distcode, v_sultype, v_sultext, v_selcount, v_selmax, v_sulreturn, v_scalecode, v_luserid);

            sql =  "insert into TZ_SULSEL(subj, grcode, sulnum, selnum, seltext, selpoint, luserid, ldate) ";
            sql +=  " values (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = connMgr.prepareStatement(sql);
            while ( em.hasMoreElements() ) { 
                v_seltext   = (String)em.nextElement();

                v_selpoint   = (String)em1.nextElement();

                if ( !v_seltext.trim().equals("") ) { 
                    v_selnum++;
                    isOk = insertTZ_sulsel(pstmt, v_subj, v_grcode, v_sulnum, v_selnum, v_seltext, Integer.parseInt(v_selpoint), v_luserid);
						Log.err.println("A" +v_seltext);
						Log.err.println("B" +v_selpoint);
                }
            }
       }
       catch ( Exception ex ) { 
           isOk = 0;
           connMgr.rollback();
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( isOk > 0 ) { connMgr.commit(); }
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.setAutoCommit(true);  connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }
       return isOk;
   }


    /**
    대상자설문 문제 수정  
    @param box          receive from the form object and session
    @return int 
    */
   public int updateQuestion(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_gubun    = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj       = box.getString("p_subj");
            v_subj = v_gubun;
   
		String v_grcode       = box.getString("p_grcode");

        int    v_sulnum     = box.getInt("p_sulnum");
        String v_distcode   = box.getString("p_distcode");
        String v_sultype    = box.getString("p_sultype");
        String v_sultext    = box.getString("p_sultext");

        String v_sulreturn = "Y";

        int    v_selnum     = 0;
		int v_selcount    = 0;
        int v_selmax    = 0;
		int v_scalecode    = 0;
        String v_seltext    = "";
        Vector v_seltexts   = null;
		Enumeration em      = null;
        String v_selpoint    = "";
        Vector v_selpoints   = null;
		Enumeration em1      = null;
        
		if ( v_sultype.equals("1") ) { 
        v_selcount    = box.getInt("p_selcount1");
        v_selmax    = box.getInt("p_selmax1");
        v_scalecode    = box.getInt("p_scalecode1");
        v_seltexts   = box.getVector("p_seltext1");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint1");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("2") ) { 
        v_selcount    = box.getInt("p_selcount2");
        v_selmax    = box.getInt("p_selmax2");
        v_scalecode    = box.getInt("p_scalecode2");
        v_seltexts   = box.getVector("p_seltext2");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint2");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("3") ) { 
        v_selcount    = box.getInt("p_selcount3");
        v_selmax    = box.getInt("p_selmax3");
        v_scalecode    = box.getInt("p_scalecode3");
        v_seltexts   = box.getVector("p_seltext3");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint3");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("4") ) { 
        v_selcount    = box.getInt("p_selcount4");
        v_selmax    = box.getInt("p_selmax4");
        v_scalecode    = box.getInt("p_scalecode4");
        v_seltexts   = box.getVector("p_seltext4");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint4");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("5") ) { 
        v_selcount    = box.getInt("p_selcount5");
        v_selmax    = box.getInt("p_selmax5");
        v_scalecode    = box.getInt("p_scalecode");
        v_seltexts   = box.getVector("p_seltext5");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint5");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("6") ) { 
        v_selcount    = box.getInt("p_selcount6");
        v_selmax    = box.getInt("p_selmax6");
        v_scalecode    = box.getInt("p_scalecode");
        v_seltexts   = box.getVector("p_seltext6");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint6");
		em1      = v_selpoints.elements();
		}
        
		String v_luserid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            isOk = updateTZ_sul(connMgr, v_subj, v_grcode,  v_sulnum, v_distcode, v_sultype, v_sultext, v_selcount, v_selmax, v_sulreturn, v_scalecode, v_luserid);
             if ( !v_sultype.equals("3") ) { 
            isOk = deleteTZ_sulsel(connMgr, v_subj, v_grcode, v_sulnum);
			 }

            sql =  "insert into TZ_SULSEL(subj, grcode, sulnum, selnum, seltext, selpoint, luserid, ldate) ";
            sql +=  " values (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = connMgr.prepareStatement(sql);
            while ( em.hasMoreElements() ) { 
                v_seltext   = (String)em.nextElement();

                v_selpoint   = (String)em1.nextElement();


                if ( !v_seltext.trim().equals("") ) { 
                    v_selnum++;
                    isOk = insertTZ_sulsel(pstmt, v_subj, v_grcode, v_sulnum, v_selnum, v_seltext, Integer.parseInt(v_selpoint), v_luserid);
                }
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    대상자설문 문제 수정  
    @param box          receive from the form object and session
    @return int 
    */
   public int updateSQuestion(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_gubun    = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj       = box.getString("p_subj");
            v_subj = v_gubun;

		String v_grcode       = box.getString("p_grcode");

        int    v_sulnum     = box.getInt("p_sulnum");
        String v_distcode   = box.getString("p_distcode");
        String v_sultype    = box.getString("p_sultype");
        String v_sultext    = box.getString("p_sultext");

        String v_sulreturn = "Y";

        int    v_selnum     = 0;
		int v_selcount    = 0;
        int v_selmax    = 0;
		int v_scalecode    = 0;
        String v_seltext    = "";
        Vector v_seltexts   = null;
		Enumeration em      = null;
        String v_selpoint    = "";
        Vector v_selpoints   = null;
		Enumeration em1      = null;
        
		if ( v_sultype.equals("1") ) { 
        v_selcount    = box.getInt("p_selcount1");
        v_selmax    = box.getInt("p_selmax1");
        v_scalecode    = box.getInt("p_scalecode1");
        v_seltexts   = box.getVector("p_seltext1");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint1");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("2") ) { 
        v_selcount    = box.getInt("p_selcount2");
        v_selmax    = box.getInt("p_selmax2");
        v_scalecode    = box.getInt("p_scalecode2");
        v_seltexts   = box.getVector("p_seltext2");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint2");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("3") ) { 
        v_selcount    = box.getInt("p_selcount3");
        v_selmax    = box.getInt("p_selmax3");
        v_scalecode    = box.getInt("p_scalecode3");
        v_seltexts   = box.getVector("p_seltext3");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint3");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("4") ) { 
        v_selcount    = box.getInt("p_selcount4");
        v_selmax    = box.getInt("p_selmax4");
        v_scalecode    = box.getInt("p_scalecode4");
        v_seltexts   = box.getVector("p_seltext4");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint4");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("5") ) { 
        v_selcount    = box.getInt("p_selcount5");
        v_selmax    = box.getInt("p_selmax5");
        v_scalecode    = box.getInt("p_scalecode");
        v_seltexts   = box.getVector("p_seltext5");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint5");
		em1      = v_selpoints.elements();
		} else if ( v_sultype.equals("6") ) { 
        v_selcount    = box.getInt("p_selcount6");
        v_selmax    = box.getInt("p_selmax6");
        v_scalecode    = box.getInt("p_scalecode");
        v_seltexts   = box.getVector("p_seltext6");
        em      = v_seltexts.elements();
        v_selpoints   = box.getVector("p_selpoint6");
		em1      = v_selpoints.elements();
		}
        
		String v_luserid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            isOk = updateTZ_sulS(connMgr, v_subj, v_grcode,  v_sulnum, v_distcode, v_sultype, v_sultext, v_selcount, v_selmax, v_sulreturn, v_scalecode, v_luserid);
           
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    대상자설문 문제 삭제  
    @param box          receive from the form object and session
    @return int 
    */
    public int deleteQuestion(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         int isOk = 0;

        String v_gubun    = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj       = box.getString("p_subj");
            v_subj = v_gubun;
    
		String v_grcode       = box.getString("p_grcode");

         int    v_sulnum     = box.getInt("p_sulnum");
		String v_duserid    = box.getSession("userid");
        String v_sultype    = box.getString("p_sultype");

         try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);

             isOk = deleteTZ_sul(connMgr, v_subj, v_grcode, v_sulnum, v_duserid);

			 if ( isOk > 0)			 { 
				 if ( !v_sultype.equals("3") ) { 
					isOk = deleteTZ_sulsel(connMgr, v_subj, v_grcode, v_sulnum);
				 }
			 }
         }
         catch ( Exception ex ) { 
             isOk = 0;
             connMgr.rollback();
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception(ex.getMessage() );
         }
         finally { 
             if ( isOk > 0 ) { connMgr.commit(); box.put("p_sulnum", String.valueOf("0") ); }
             if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return isOk;
     }


    /**
    일련번호 구하기 
    @param box          receive from the form object and session
    @return int 
    */
    public int getSulnumSeq(String p_subj, String p_grcode) throws Exception { 
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","sulnum");
        maxdata.put("seqtable","tz_sul");
        maxdata.put("paramcnt","2");
        maxdata.put("param0","subj");
        maxdata.put("param1","grcode");
        maxdata.put("subj",   SQLString.Format(p_subj));
        maxdata.put("grcode",   SQLString.Format(p_grcode));

        return SelectionUtil.getSeq(maxdata);
    }

}