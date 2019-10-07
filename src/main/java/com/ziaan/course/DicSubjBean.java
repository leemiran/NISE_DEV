// **********************************************************
//  1. 제      목: 용어사전 관리
//  2. 프로그램명: DicSubjBean.java
//  3. 개      요: 용어사전 관리(과목)
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 노희성 2004. 11. 14
//  7. 수      정:
// **********************************************************

package com.ziaan.course;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class DicSubjBean { 
    private ConfigSet   config  ;
    private int         row     ;
        
    public DicSubjBean() { 
        try { 
            config = new ConfigSet();  
            row    = Integer.parseInt(config.getProperty("page.bulletin.row") );        // 이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
    용어사전 화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   용어사전  리스트
    */
    public ArrayList selectListDicSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        DataBox             dbox            = null;
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        int                 v_pageno        = box.getInt("p_pageno");
        String              v_gubun         = "1";

        String              ss_upperclass   = box.getString("s_upperclass"  );  // 과목대분류
        String              ss_middleclass  = box.getString("s_middleclass" );  // 과목중분류
        String              ss_lowerclass   = box.getString("s_lowerclass"  );  // 과목소분류
        String              ss_subj         = box.getString("s_subjcourse"  );  // 과목코드
        String              v_searchtext    = box.getString("p_searchtext"  );
        String              v_groups        = box.getStringDefault("p_group" , ""); // ㄱ,ㄴ,ㄷ ....
        
        int                 total_page_count= 0;    // 전체 페이지 수를 반환한다
        int                 total_row_count = 0;    // 전체 row 수를 반환한다
        
        try { 
            connMgr     = new DBConnectionManager();

            list        = new ArrayList();

            sbSQL.append(" SELECT  a.seq                                                                        \n")
                 .append("     ,   a.subj                                                                       \n")
                 .append("     ,   b.subjnm                                                                     \n")
                 .append("     ,   a.words                                                                      \n")
                 .append("     ,   a.descs                                                                      \n")
                 .append("     ,   a.groups                                                                     \n")
                 .append("     ,   a.luserid                                                                    \n")
                 .append("     ,   a.ldate                                                                      \n")
                 .append(" FROM    TZ_DIC      a                                                                \n")
                 .append("     ,   TZ_SUBJ     b                                                                \n")
                 .append("     ,   TZ_DICGROUP c                                                                \n")
                 .append(" WHERE   a.subj      = b.subj                                                         \n")
                 .append(" AND     a.groups    = c.groups                                                       \n")
                 .append(" AND     a.gubun     = " + StringManager.makeSQL(v_gubun)                    + "      \n");
            
            if ( !ss_subj.equals("") )  // 과목이 있으면
                sbSQL.append(" AND     a.subj      = " + StringManager.makeSQL(ss_subj)                    + "      \n");
           
            if ( !v_searchtext.equals("") ) // 검색어가 있으면
                sbSQL.append(" AND     a.words like  " + StringManager.makeSQL("%" + v_searchtext + "%")   + "      \n");
           
            if ( !v_groups.equals("") ) // 용어분류로 검색할때
                sbSQL.append(" AND     a.groups    = " + StringManager.makeSQL(v_groups)                   + "      \n");
       
            sbSQL.append(" ORDER BY a.subj asc, a.groups asc, a.words asc                                       \n");
            
            System.out.println(this.getClass().getName() + "." + "selectListDicSubj() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls  = connMgr.executeQuery(sbSQL.toString());

            ls.setPageSize      (row        );          // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage   (v_pageno   );          // 현재페이지번호를 세팅한다.
            
            total_page_count    = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            total_row_count     = ls.getTotalCount();   // 전체 row 수를 반환한다
            
            box.put("total_row_count", String.valueOf(total_row_count));  // 총 갯수를 BOX에 세팅

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                dbox.put("d_dispnum"    , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage"  , new Integer(total_page_count));
                dbox.put("d_rowcount"   , new Integer(row));
                
                list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
                
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list;
    }


    /**
    용어사전 화면 리스트(컨텐츠 과목 용어사전)
    @param box          receive from the form object and session
    @return ArrayList   용어사전  리스트
    */
    public ArrayList selectListDicSubjStudy(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	            = null;
        ListSet             ls                  = null;
        ArrayList           list                = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        DataBox             dbox                = null;
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        int                 v_pageno            = box.getInt            ("p_pageno"     );
        String              v_gubun             = "1";
        String              p_subj              = box.getString         ("p_subj"       );  // 과목코드
        String              v_searchtext        = box.getString         ("p_searchtext" );
        String              v_groups            = box.getStringDefault  ("p_group", "ALL");  // ㄱ,ㄴ,ㄷ ....
        
        int                 total_page_count    = 0; //ls.getTotalPage();       //     전체 페이지 수를 반환한다
        int                 total_row_count     = 0; //ls.getTotalCount();    //     전체 row 수를 반환한다
        
        try { 
            connMgr     = new DBConnectionManager();

            list        = new ArrayList();

            sbSQL.append(" SELECT  a.seq                                                                        \n")
                 .append("     ,   a.subj                                                                       \n")
                 .append("     ,   b.subjnm                                                                     \n")
                 .append("     ,   a.words                                                                      \n")
                 .append("     ,   a.descs                                                                      \n")
                 .append("     ,   a.groups                                                                     \n")
                 .append("     ,   a.luserid                                                                    \n")
                 .append("     ,   a.ldate                                                                      \n")
                 .append(" FROM    TZ_DIC      a                                                                \n")
                 .append("     ,   TZ_SUBJ     b                                                                \n")
                 .append("     ,   TZ_DICGROUP c                                                                \n")
                 .append(" WHERE   a.subj      = b.subj                                                         \n")
                 .append(" AND     a.groups    = c.groups                                                       \n");
            
          System.out.println("v_gubun : " + v_gubun );
            
            if ( !v_gubun.equals("") ) {
                 sbSQL.append(" AND     a.gubun     = " + StringManager.makeSQL(v_gubun )                   + "      \n");
            }     
                 
            sbSQL.append(" AND     a.subj      = " + StringManager.makeSQL(p_subj  )                   + "      \n");
            
            if ( !v_searchtext.equals("") ) // 검색어가 있으면
                sbSQL.append(" AND     a.words like  " + StringManager.makeSQL("%" + v_searchtext + "%")   + "      \n");
                
            if ( !v_groups.equals("ALL") )     // 용어분류로 검색할때
                sbSQL.append(" AND     a.groups    = " + StringManager.makeSQL(v_groups)                   + "      \n");
                
            sbSQL.append(" ORDER BY a.subj asc, a.groups asc, a.words asc                                       \n");
                
            System.out.println(this.getClass().getName() + "." + "selectListDicSubjStudy() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls = connMgr.executeQuery(sbSQL.toString());

            ls.setPageSize      (row        );          // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage   (v_pageno   );          // 현재페이지번호를 세팅한다.
            
            total_page_count    = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            total_row_count     = ls.getTotalCount();   // 전체 row 수를 반환한다
            
            box.put("total_row_count", String.valueOf(total_row_count));  // 총 갯수를 BOX에 세팅
            
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                dbox.put("d_dispnum"    , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage"  , new Integer(total_page_count));
                dbox.put("d_rowcount"   , new Integer(row));
                
                list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
                
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list;
    }
    
    
    /**
    용어사전 화면 리스트(전체 용어사전)
    @param box          receive from the form object and session
    @return ArrayList   용어사전  리스트
    */
    public ArrayList selectListDicTotal(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr             = null;
        ListSet             ls                  = null;
        ArrayList           list                = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        DataBox             dbox                = null;
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        int                 v_pageno            = box.getInt            ("p_pageno"     );
        String              v_gubun             = "1";
        String              p_subj              = box.getString         ("p_subj"       );  // 과목코드 (사용하지 않음)
        String              v_searchtext        = box.getString         ("p_searchtext" );
        String              v_groups            = box.getStringDefault  ("p_group", ""  );  // ㄱ,ㄴ,ㄷ ....
        
        int                 total_page_count    = 0;    // 전체 페이지 수를 반환한다
        int                 total_row_count     = 0;    // 전체 row 수를 반환한다
        
        try { 
            connMgr     = new DBConnectionManager();

            list        = new ArrayList();

            sbSQL.append(" SELECT  a.seq                                                                \n")
                 .append("     ,   a.subj                                                               \n")
                 .append("     ,   a.words                                                              \n")
                 .append("     ,   a.descs                                                              \n")
                 .append("     ,   a.groups                                                             \n")
                 .append("     ,   a.luserid                                                            \n")
                 .append("     ,   a.ldate                                                              \n")
                 .append(" FROM    TZ_DIC      a                                                        \n")
                 .append("     ,   TZ_DICGROUP c                                                        \n")
                 .append(" WHERE   a.groups    = c.groups                                               \n")   
                 .append(" AND     a.gubun     = " + StringManager.makeSQL(v_gubun) + "                 \n");
       
           if ( !v_searchtext.equals("") ) // 검색어가 있으면
               sbSQL.append(" AND     a.words like " + StringManager.makeSQL("%" + v_searchtext + "%") + " \n");
           
           if ( !v_groups.equals("") ) // 용어분류로 검색할때
               sbSQL.append(" AND     a.groups    = " + StringManager.makeSQL(v_groups) + "                 \n");
           
           sbSQL.append(" ORDER BY a.groups asc, a.words asc                                            \n");
            
            //System.out.println(this.getClass().getName() + "." + "selectListDicTotal() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls = connMgr.executeQuery(sbSQL.toString());

            ls.setPageSize      (row        );          // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage   (v_pageno   );          // 현재페이지번호를 세팅한다.
            
            total_page_count    = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            total_row_count     = ls.getTotalCount();   // 전체 row 수를 반환한다
            
            box.put("total_row_count", String.valueOf(total_row_count));  // 총 갯수를 BOX에 세팅

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                dbox.put("d_dispnum"    , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage"  , new Integer(total_page_count));
                dbox.put("d_rowcount"   , new Integer(row));
                
                list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
                
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list;
    }

    
   /**
    용어사전 팝업상세(전체 용어사전)
    @param box          receive from the form object and session
    @return ArrayList   용어사전  리스트
    */
    public DataBox selectWordContent(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        DataBox             dbox    = null;
        
        int                 iSysAdd = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              p_subj  = box.getString("p_subj"    ); 
        String              p_gubun = box.getString("p_gubun"   );
        String              p_seq   = box.getString("p_seq"     );
        
        try { 
            connMgr     = new DBConnectionManager();
            
            sbSQL.append(" SELECT  words                                                \n")
                 .append("     ,   descs                                                \n")
                 .append(" FROM    tz_dic                                               \n")
                 .append(" WHERE   subj    = " + SQLString.Format(p_subj   ) + "        \n")
                 .append(" AND     gubun   = " + SQLString.Format(p_gubun  ) + "        \n")
                 .append(" AND     seq     = " + SQLString.Format(p_seq    ) + "        \n");
            
            System.out.println(this.getClass().getName() + "." + "selectWordContent() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
                
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return dbox;
    }
}
