// **********************************************************
// 1. 제      목:
// 2. 프로그램명: SulmunTargetPaperBean.java
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
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.system.SelectionUtil;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SulmunTargetPaperBean { 

    public SulmunTargetPaperBean() { }

    public ArrayList selectQuestionList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_gubun    = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj     = box.getStringDefault("s_subjcourse", "ALL");
        String v_distcode = box.getStringDefault("s_distcode","ALL");
        String v_grcode = box.getString("s_grcode");
        String v_action   = box.getStringDefault("p_action",  "change");
      
        try { 
                connMgr = new DBConnectionManager();
                    v_subj = v_gubun;
                list = selectQuestionList(connMgr, v_grcode, v_subj, v_distcode);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }



    /**
    대상자설문 문제지  리스트
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
    대상자설문 문제지  리스트
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList selectPaperQuestionList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_gubun    = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_grcode      = box.getString("p_grcode");
        String v_subj        = box.getString("p_subj");
        int    v_sulpapernum = box.getInt("p_sulpapernum");

        try { 
            connMgr = new DBConnectionManager();
                    v_subj = v_gubun;
            list = selectPaperQuestionList(connMgr, v_grcode, v_subj, v_sulpapernum, box);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    대상자설문 문제지  리스트
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList selectPaperQuestionList(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum, RequestBox box) throws Exception { 
        ArrayList list = new ArrayList();
        Hashtable hash = new Hashtable();
        StringTokenizer st = null;

        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        String v_sulnums = "";
        String v_sulnum  = "";
        String v_sulpapernm = "";
        try { 
            sql = "select sulpapernm, totcnt, sulnums";
            sql += "  from tz_sulpaper ";
            sql += " where grcode      = " + SQLString.Format(p_grcode);
            sql += " and subj        = " + SQLString.Format(p_subj);
            sql += "   and sulpapernum = " + SQLString.Format(p_sulpapernum);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_sulnums = ls.getString("sulnums");
                v_sulpapernm = ls.getString("sulpapernm");
            }
            if ( box != null ) { 
                box.put("p_sulpapernm",v_sulpapernm);
            }

            if ( v_sulnums.length() > 0 ) { 
                sql = "select a.subj,     a.sulnum,  ";
                sql += "       a.distcode, b.codenm  distcodenm, ";
                sql += "       a.sultype,  c.codenm  sultypenm,  ";
                sql += "       a.sultext    ";
                sql += "  from tz_sul    a, ";
                sql += "       tz_code   b, ";
                sql += "       tz_code   c  ";
                sql += " where a.distcode = b.code ";
                sql += "   and a.sultype  = c.code ";
                sql += "   and b.gubun    = " + SQLString.Format(SulmunTargetBean.DIST_CODE);
                sql += "   and c.gubun    = " + SQLString.Format(SulmunTargetBean.SUL_TYPE);
                sql += "   and a.subj     = " + SQLString.Format(p_subj);
                sql += "    and c.levels  = 1  ";
                if ( v_sulnums.equals("")) v_sulnums = "-1";
                sql += "   and a.sulnum in (" + v_sulnums + ")";

                sql += " order by a.sulnum ";

                ls.close();
                ls = connMgr.executeQuery(sql);

                st = new StringTokenizer(v_sulnums,SulmunTargetBean.SPLIT_COMMA);

                while ( ls.next() ) { 
                    dbox = ls.getDataBox();

                    v_sulnum = String.valueOf( ls.getInt("sulnum") );

                    hash.put(v_sulnum, dbox);
                }

                while ( st.hasMoreElements() ) { 
                    v_sulnum = (String)st.nextToken();
                    dbox = (DataBox)hash.get(v_sulnum);
                    if ( dbox != null ) { 
                        list.add(dbox);
                    }
                }
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
    대상자설문 문제지  리스트
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public String getPaperListSQL(String p_grcode, String p_subj, String p_gyear, String p_subjsel, String p_upperclass, int p_sulpapernum) throws Exception { 
        String              sql     = "";

			sql = "select grcode,       subj,    subjseq,     ";
            sql += "       sulpapernum,  sulpapernm, year, subjseq, ";
            sql += "       totcnt,       sulnums, sulmailing, sulstart, sulend, ";
            sql += "       'TARGET'      subjnm ";
            sql += "  from tz_sulpaper ";
            sql += " where grcode = " + SQLString.Format(p_grcode);
            sql += "   and subj   = " + SQLString.Format(p_subj);
            sql += "   and year   = " + SQLString.Format(p_gyear);
            if ( p_sulpapernum > 0 ) { 
                sql += "   and sulpapernum   = " + p_sulpapernum;
            }
            sql += " order by subj, sulpapernum ";
System.out.println(sql);
		return sql;
    }

    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제지 리스트
    */
    public ArrayList selectPaperList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox     dbox = null;

        String v_grcode    = box.getStringDefault("p_grcode", SulmunTargetBean.DEFAULT_GRCODE);
        String v_gyear    = box.getStringDefault("p_gyear", box.getString("s_gyear") );
        String v_action    = box.getStringDefault("p_action", "change");
        String v_gubun     = box.getStringDefault("p_gubun",   SulmunTargetBean.DEFAULT_SUBJ);

        String  ss_upperclass  = box.getStringDefault("s_upperclass","ALL");
        String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");
        String  ss_lowerclass  = box.getStringDefault("s_lowerclass","ALL");
        String  ss_subjcourse  = box.getStringDefault("s_subjcourse","ALL");

           try { 

            if ( v_action.equals("go") ) { 
    
    				ss_subjcourse = v_gubun;
                    sql = getPaperListSQL(v_grcode, v_gubun, v_gyear, ss_subjcourse, ss_upperclass, -1);

                connMgr = new DBConnectionManager();
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                    
                    dbox.put("d_membercount", new Integer(this.getSulmemberCount(connMgr, v_grcode, v_gyear, dbox.getInt("d_sulpapernum"))));
                     list.add(dbox);
                }
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
    대상자설문 문제지  데이타 
    @param box          receive from the form object and session
    @return DataBox   
    */
    public DataBox getPaperData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        DataBox     dbox = null;

        String v_grcode    = box.getString("p_grcode");
        String v_subj      = box.getStringDefault("p_subj", "TARGET");
        String v_gyear    = box.getStringDefault("p_gyear", box.getString("s_gyear") );
        String v_subjsel   = box.getString("p_subjsel");
        String v_upperclass= box.getStringDefault("p_upperclass","ALL");

        int    v_sulpapernum = box.getInt("p_sulpapernum");

        try { 
            connMgr = new DBConnectionManager();
            dbox =  getPaperData(connMgr, v_grcode, v_subj, v_gyear, v_subjsel, v_upperclass, v_sulpapernum);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }

    /**
    대상자설문 문제지  데이타 
    @param box          receive from the form object and session
    @return DataBox   
    */
    public DataBox getPaperData(DBConnectionManager connMgr, String p_grcode, String p_subj, String p_gyear, String p_subjsel, String p_upperclass, int p_sulpapernum) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        DataBox     dbox = null;

        try { 
                sql = getPaperListSQL(p_grcode, p_subj, p_gyear, p_subjsel, p_upperclass, p_sulpapernum);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
				dbox = ls.getDataBox();
            
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        if ( dbox == null ) dbox = new DataBox("resoponsebox");

        return dbox;
    }
    

    /**
    대상자설문 대상자 수 
    @param box          receive from the form object and session
    @return int   
    */    
    public int getSulmemberCount(DBConnectionManager connMgr, String p_grcode, String p_gyear, int p_sulpapernum) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        int result = 0;

        try { 
            sql = "select count(*) from tz_sulmember";
            sql += " where grcode = " + SQLString.Format(p_grcode);
            sql += "   and subj   = 'TARGET'";
            sql += "   and year   = " + SQLString.Format(p_gyear);
            if ( p_sulpapernum > 0) 
                sql += "   and sulpapernum   = " + p_sulpapernum;
                
            ls = connMgr.executeQuery(sql);System.out.println(sql);

            if ( ls.next() ) { 
		result = ls.getInt(1);
            
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return result;
    }


    /**
    대상자설문 문제지  등록  
    @param box          receive from the form object and session
    @return int   
    */
    public int insertTZ_sulpaper(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum, String p_sulpapernm, int p_totcnt, String p_sulnums, String p_sulmailing, String p_sulstart, String p_sulend, String p_luserid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_SULPAPER table
            sql =  "insert into TZ_SULPAPER ";
            sql +=  "(grcode,    subj,     sulpapernum, sulpapernm, "; 
            sql +=  " year,      subjseq,     ";
            sql +=  " totcnt,       sulnums,     sulmailing,   ";
            sql +=  " sulstart, sulend,  luserid,  ldate )   ";
            sql +=  " values ";
            sql +=  "(?,         ?,       ?,         ?,   ";
            sql +=  " ?,         ?,            ";
            sql +=  " ?,         ?,       ?, ";
            sql +=  " ?,         ?,       ?,         ?) ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, p_grcode);
            pstmt.setString( 2, p_subj);
            pstmt.setInt   ( 3, p_sulpapernum);
            pstmt.setString( 4, p_sulpapernm);
            pstmt.setString( 5, p_sulstart.substring(0,4));
            pstmt.setString( 6, "0001");
            pstmt.setInt( 7, p_totcnt);
            pstmt.setString( 8, p_sulnums);
            pstmt.setString( 9, p_sulmailing);
            pstmt.setString(10, p_sulstart);
            pstmt.setString(11, p_sulend);
            pstmt.setString(12, p_luserid);
            pstmt.setString(13, FormatDate.getDate("yyyyMMddHHmmss") );

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
    대상자설문 문제지  수정   
    @param box          receive from the form object and session
    @return int   
    */
    public int updateTZ_sulpaper(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum, String p_sulpapernm, int p_totcnt, String p_sulnums, String p_sulmailing, String p_sulstart, String p_sulend, String p_luserid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // update TZ_SULPAPER table
            sql = " update TZ_SULPAPER ";
            sql += "    set sulpapernm = ?, ";
            sql += "        year       = ?, ";
			sql += "        totcnt       = ?, ";
            sql += "        sulnums      = ?, ";
            sql += "        sulmailing       = ?, ";
            sql += "        sulstart       = ?, ";
            sql += "        sulend       = ?, ";
			sql += "        luserid      = ?, ";
            sql += "        ldate        = ?  ";
            sql += "  where grcode       = ?  ";
            sql += "    and subj         = ?  ";
            sql += "    and sulpapernum  = ?  ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, p_sulpapernm);
            pstmt.setString(2, p_sulstart.substring(0,4));
			pstmt.setInt   (3, p_totcnt);
            pstmt.setString(4, p_sulnums);
            pstmt.setString(5, p_sulmailing);
            pstmt.setString(6, p_sulstart);
            pstmt.setString(7, p_sulend);
            pstmt.setString(8, p_luserid);
            pstmt.setString(9, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(10, p_grcode);
            pstmt.setString(11, p_subj);
            pstmt.setInt   (12, p_sulpapernum);

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
    대상자설문 문제지  삭제   
    @param box          receive from the form object and session
    @return int   
    */
    public int deleteTZ_sulpaper(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum, String p_duserid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        ListSet             ls      = null;
        int isOk = 0;

        try { 
            // 삭제체크
            sql = "select sulpapernum from TZ_SULMEMBER where subj='TARGET' and  sulpapernum=" +p_sulpapernum + " ";
            ls = connMgr.executeQuery(sql);
    		if ( ls.next() ) { 
    		    isOk = -2;
    		}
    		
            if ( isOk == 0) {  

				sql =  "delete from TZ_SULPAPER ";
				sql +=  " where grcode     = ?  ";
				sql +=  "   and subj       = ?  ";
				sql +=  "   and sulpapernum= ?  ";

				pstmt = connMgr.prepareStatement(sql);
				pstmt.setString(1, p_grcode);
				pstmt.setString(2, p_subj);
				pstmt.setInt   (3, p_sulpapernum);

				isOk = pstmt.executeUpdate();

			}

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
    대상자설문 문제지  등록  
    @param box          receive from the form object and session
    @return int   
    */
    public int insertPaper(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int isOk = 0;

        String v_grcode       = box.getStringDefault("p_grcode",SulmunTargetBean.DEFAULT_GRCODE);
        String v_gubun        = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj         = box.getStringDefault("s_subjcourse", "ALL");
            v_subj = v_gubun;
 
        String v_sulpapernm = box.getString("p_sulpapernm");
        int    v_totcnt       = box.getInt("p_totcnt");
        String v_sulnums      = box.getString("p_sulnums");
        String v_sulmailing      = box.getString("p_sulmailing");
       
        int    v_sulpapernum  = 0;

        String v_sulstart = box.getString("p_sulstart");
        String v_sulend   = box.getString("p_sulend");
 
        String v_luserid   = box.getSession("userid");


        try { 

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            v_sulpapernum = getPapernumSeq(v_subj, v_grcode);

            isOk = insertTZ_sulpaper(connMgr,  v_grcode, v_subj, v_sulpapernum, v_sulpapernm, v_totcnt, v_sulnums, v_sulmailing, v_sulstart, v_sulend, v_luserid);
		
		} catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    대상자설문 문제지  수정   
    @param box          receive from the form object and session
    @return int   
    */
    public int updatePaper(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int isOk = 0;

        String v_grcode = box.getString("p_grcode");
        String v_gubun        = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj         = box.getStringDefault("s_subjcourse", "ALL");
            v_subj = v_gubun;

		int    v_sulpapernum  = box.getInt("p_sulpapernum");
        String v_sulpapernm = box.getString("p_sulpapernm");
        int    v_totcnt       = box.getInt("p_totcnt");
        String v_sulnums      = box.getString("p_sulnums");
        String v_sulmailing      = box.getString("p_sulmailing");

        String v_sulstart = box.getString("p_sulstart");
        String v_sulend   = box.getString("p_sulend");

        String v_luserid  = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            isOk = updateTZ_sulpaper(connMgr,  v_grcode, v_subj, v_sulpapernum, v_sulpapernm, v_totcnt, v_sulnums, v_sulmailing, v_sulstart, v_sulend, v_luserid);

		} catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    대상자설문 문제지  삭제   
    @param box          receive from the form object and session
    @return int   
    */
    public int deletePaper(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        String              sql     = "";
        int isOk = 0;

        String v_grcode = box.getString("p_grcode");
        String v_gubun        = box.getStringDefault("p_gubun", SulmunTargetBean.DEFAULT_SUBJ);
        String v_subj         = box.getStringDefault("s_subjcourse", "ALL");
            v_subj = v_gubun;
 
		int    v_sulpapernum  = box.getInt("p_sulpapernum");
        String v_duserid  = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            isOk = deleteTZ_sulpaper(connMgr,  v_grcode, v_subj, v_sulpapernum, v_duserid);

        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }



    public int getPapernumSeq(String p_subj, String p_grcode) throws Exception { 
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","sulpapernum");
        maxdata.put("seqtable","tz_sulpaper");
        maxdata.put("paramcnt","2");
        maxdata.put("param0","subj");
        maxdata.put("param1","grcode");
        maxdata.put("subj",   SQLString.Format(p_subj));
        maxdata.put("grcode",   SQLString.Format(p_grcode));

        return SelectionUtil.getSeq(maxdata);
    }


    /**
    대상자설문   문제번호 구하기   
    @param box          receive from the form object and session
    @return Vector   
    */
	public Vector getSulnums(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        Vector v_sulnums = new Vector();
        String v_tokens  = "";
        StringTokenizer st = null;

        try { 
            sql = "select sulnums  ";
            sql += "  from tz_sulpaper ";
            sql += " where grcode      = " + SQLString.Format(p_grcode);
  //          sql += " where grcode      = " + SQLString.Format(SulmunTargetBean.DEFAULT_GRCODE);
            sql += "   and subj        = " + SQLString.Format(p_subj);
            sql += "   and sulpapernum = " + p_sulpapernum;
// System.out.println(sql);	
			ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_tokens = ls.getString("sulnums");
            }

            st = new StringTokenizer(v_tokens,SulmunTargetBean.SPLIT_COMMA);
            while ( st.hasMoreElements() ) { 
                v_sulnums.add((String)st.nextToken() );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_sulnums;
    }

    /**
    대상자설문   설문페이지 보기 
    @param box          receive from the form object and session
    @return ArrayList   
    */    
    public ArrayList getSelnums(DBConnectionManager connMgr, String p_grcode, String p_subj, Vector p_sulnums) throws Exception { 
        Hashtable hash = new Hashtable();
        ArrayList blist = new ArrayList();
		ArrayList           list    = null;

        ListSet ls  = null;
        String sql  = "";
        DataBox             dbox    = null;
        StringTokenizer st = null;

        String v_sulnums = "";
        for ( int i = 0; i < p_sulnums.size(); i++ ) { 
            v_sulnums += (String)p_sulnums.get(i);
            if ( i<p_sulnums.size()-1) { 
                v_sulnums += ",";
            }
        }
        if ( v_sulnums.equals("")) v_sulnums = "-1";

        try { 
			
			st = new StringTokenizer(v_sulnums, ",");

            while ( st.hasMoreElements() ) { 

                int sulnum = Integer.parseInt(st.nextToken() );

	            sql = "select a.subj,     a.sulnum, a.selmax, ";
	            sql +="        a.distcode, c.codenm distcodenm, ";
	            sql += "       a.sultype,  d.codenm sultypenm, ";
	            sql += "       a.sultext,  b.selnum, b.seltext, b.selpoint ";
	            sql += "  from tz_sul     a, ";
	            sql += "       tz_sulsel  b, ";
	            sql += "       tz_code    c, ";
	            sql += "       tz_code    d  ";
	            sql += " where a.subj     = b.subj( +)    ";
	            sql += "   and a.sulnum   = b.sulnum( +)  ";
	            sql += "   and a.grcode = b.grcode( +) ";
				sql += "   and a.distcode = c.code ";
	            sql += "   and a.sultype  = d.code ";
	            sql += "   and a.grcode     = " + SQLString.Format(p_grcode);
				sql += "   and a.subj     = " + SQLString.Format(p_subj);
	            sql += "   and a.sulnum = " + sulnum ;
	            sql += "   and c.gubun    = " + SQLString.Format(SulmunTargetBean.DIST_CODE);
	            sql += "   and d.gubun    = " + SQLString.Format(SulmunTargetBean.SUL_TYPE);
	            sql += "   and d.levels    =  1 ";
	            sql += " order by a.subj, a.sulnum, b.selnum ";
				ls = connMgr.executeQuery(sql);
				list =  new ArrayList();
	
	            while ( ls.next() ) { 
	                  dbox = ls.getDataBox();
					  list.add(dbox);
	            }
			    blist.add(list);
			    ls.close();
			}
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return blist;
    }
    /**
    대상자설문   설문내용 보기 
    @param box          receive from the form object and session
    @return ArrayList   
    */  
    public ArrayList selectPaperQuestionExampleList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_grcode      = box.getString("p_grcode");
        String v_subj        = "TARGET";
        int    v_sulpapernum = box.getInt("p_sulpapernum");

        try { 
            if ( v_sulpapernum == 0 ) { 
                v_sulpapernum = getPapernumSeq(v_subj, v_grcode)-1;
                box.put("p_sulpapernum", String.valueOf(v_sulpapernum));
            }

            connMgr = new DBConnectionManager();
            list = selectPaperQuestionExampleList(connMgr, v_grcode, v_subj, v_sulpapernum, box);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    대상자설문   설문내용 보기 
    @param box          receive from the form object and session
    @return ArrayList   
    */  
    public ArrayList selectPaperQuestionExampleList(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum, RequestBox box) throws Exception { 
        Vector    v_sulnums = null;
        ArrayList QuestionExampleDataList  = null;

        try { 
                // 설문지번호에 해당하는 설문번호를 vector로 받아온다. 벡터(설문번호1,3,5 ....)
            v_sulnums = getSulnums(connMgr, p_grcode, p_subj, p_sulpapernum);

            if ( !v_sulnums.equals("") ) { 
                // 설문번호에 해당하는 문제리스트를 만든다. 리스트((설문번호1, 보기1,2,3..))
                QuestionExampleDataList = getSelnums(connMgr, p_grcode, p_subj, v_sulnums);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return QuestionExampleDataList;
    }
}