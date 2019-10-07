// **********************************************************
//  1. 제      목: 과목 강사정보 조회
//  2. 프로그램명 : TutorInfoBean.java
//  3. 개      요: 과목 강사정보 조회
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 강성욱 2004. 12. 20
//  7. 수      정:
// **********************************************************

package com.ziaan.study;

import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

/**
 * 과목 수강생 현황(ADMIN)
 *
 * @date   : 2004. 12
 * @author : S.W.Kang
 */
public class TutorInfoBean { 
	private int row;
    public TutorInfoBean() { }


    /**
    과목기수 강사소개 리스트
    @param box          receive from the form object and session
    @return ArrayList   과목기수 강사소개 리스트
    */
    public ArrayList selectView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        String v_subj		= box.getString("p_subj");
        String v_year		= box.getString("p_year");
        String v_subjseq 	= box.getString("p_subjseq");
        String v_usernm		= box.getString("p_usernm");
        
        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();
			
			sql  = " select b.name,                                      \n";
			sql += "        b.academic,                                  \n";
			sql += "        b.major,                                     \n";
			sql += "        b.professional,                              \n";
			sql += "        b.career,                                    \n";
			sql += "        b.majorbook,                                 \n";
			sql += " 		b.intro,                                     \n";
			sql += "        get_compnm(b.compcd,2,4) compnm,             \n";
			sql += "        b.comp                                       \n";
			sql += "   from tz_subjman a,                                \n";
			sql += "        tz_tutor b                                   \n";
			sql += "  where a.userid=b.userid                            \n";
			sql += "    and a.subj='" +v_subj + "'                       \n";
			sql += "    and a.gadmin='P1'                                \n";
            sql += "  order by b.name asc                                \n";

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
}
