// **********************************************************
// 1. 제      목:
// 2. 프로그램명: MyPageStudyInfoBean.java
// 3. 개      요: 마이페이지 - 나의 학습정보 관련 빈
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: 김민수 2006.07.11
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.mypage;

import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author 김민수
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class MyPageStudyInfoBean { 
    public static String SINGLE_CLASS = "1";
    public static String PLURAL_CLASS = "2";

    public static String CLASSNM = "클래스";
    public static String SINGLE_CLASS_CODE = "0001";

    private     ConfigSet   config;
    private     int         row;
    
    public MyPageStudyInfoBean() {
        try { 
            config          = new ConfigSet();
            row             = Integer.parseInt(config.getProperty("page.bulletin.row") ); // 이 모듈의 페이지당 row 수를 셋팅한다
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    수료증 신청 리스트 보여주기
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectMySuRoyList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        DataBox dbox        = null;

        
        String  v_user_id   = box.getSession("userid");
        int v_pageno = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1  =
                "\n	select tc.subj, tc.year, tc.subjseq, tc.userid, tc.seq, tc.ea, 		" +
                "\n		tc.appdate, tc.senddate, tc.issend,				" +
                "\n		ts.subjnm, ts.isonoff							" +
                "\n	from tz_certapp tc, tz_subj ts					" +
                "\n	where tc.userid		= ':userid'					" +
                "\n		and tc.subj	= ts.subj					" +                
                "\n	order by tc.appdate desc						";
                                

            sql1 = sql1.replaceAll( ":userid", v_user_id );

           ls1 = connMgr.executeQuery(sql1);
            ls1.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
            ls1.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
            int totalpagecount = ls1.getTotalPage();    //     전체 페이지 수를 반환한다
            int totalrowcount = ls1.getTotalCount();    //     전체 row 수를 반환한다

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls1.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
                list1.add(dbox);
            }
            ls1.close();
            
                        
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

     /**
     나의 발급 완료된 수료증수
     @param box      receive from the form object and session
     @return ArrayList
     */
    public DataBox selectMySuRoyCount(RequestBox box) throws Exception 
    {
         
         DBConnectionManager    connMgr = null;
         ListSet ls1          = null;
         ArrayList list1      = null;
         String sql1          = "";
         DataBox dbox         = null;
         String  v_user_id    = box.getSession("userid");

         try { 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();
             
             sql1  = "";        
             sql1 += "  select count(*) CNT				";
             sql1 += "	from tz_certapp 				";
             sql1 += "	where userid=" + SQLString.Format(v_user_id)	 ;
             sql1 += "	 and issend='Y' ";
             
             System.out.println("sql1 ==  ==  ==  ==  ==  == > 완료" +sql1);
             ls1 = connMgr.executeQuery(sql1);

             for ( int i = 0; ls1.next(); i++ ) {                  
                 dbox = ls1.getDataBox();
             }
             ls1.close();

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return dbox;
     }

    /**
    취소신청가능 과목리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   취소신청가능 과목리스트
    */
    public ArrayList selectCancelPossibleList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        String  v_comp     = box.getSession("comp");
        String  gyear      = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
        String  v_userid     = box.getSession("userid");
        int                 v_pageno        = box.getInt("p_pageno");
        String v_lsearchtext = box.getString("p_lsearchtext");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql= "select a.userid, b.subj, b.year, b.subjseq, b.subjseqgr, b.subjnm,\n"
                +"  b.propstart, b.propend, b.edustart, b.eduend, b.isonoff, a.chkfinal, a.cancelkind, classname,\n"
                +"  to_char(TO_DATE(substr(nvl(b.propend,'2000010100'),0,8),'YYYYMMDD')+nvl(b.canceldays,0), 'YYYYMMDD') cancelend\n"
                +"from TZ_PROPOSE a, VZ_SCSUBJSEQ b, tz_subjatt c\n"  
                +"where a.subj = b.subj\n" 
                +"and a.year = b.year\n" 
                +"and a.subjseq = b.subjseq\n"     
                +"and a.userid = '" +v_userid + "'\n"
                +"and a.chkfinal != 'N'\n"    
                +"and to_char(sysdate,'YYYYMMDDHH') between b.propstart and\n"
                +"(to_char(TO_DATE(substr(nvl(b.propend,'2000010100'),0,8),'YYYYMMDD')+b.canceldays, 'YYYYMMDD') || '00')\n"
                +"and b.scupperclass = c.upperclass\n"
                +"and b.scsubjclass = c.subjclass\n";
            if(!"".equals(v_lsearchtext)) {
                sql += " and b.subjnm like " + StringManager.makeSQL("%" + v_lsearchtext + "%");
            }            
                sql+="order by b.scupperclass, b.scmiddleclass, b.subjnm, b.edustart desc";
            System.out.println("sql_cancellist ==  ==  ==  ==  ==  == > " +sql);

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                                // 페이지당 row 갯수를 세팅한다.
            ls.setCurrentPage(v_pageno);                        // 현재페이지번호를 세팅한다.
            int     total_page_count    = ls.getTotalPage();    // 전체 페이지 수를 반환한다.
            int     total_row_count     = ls.getTotalCount();   // 전체 row 수를 반환한다.

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                
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


}












































