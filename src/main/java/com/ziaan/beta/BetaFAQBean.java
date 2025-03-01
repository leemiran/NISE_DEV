/**
*베타시스템의 FAQ빈 클래스
*<p> 제목:BetaFAQBean.java</p> 
*<p> 설명:FAQ빈</p> 
*<p> Copyright: Copright(c)2004</p> 
*<p> Company: VLC</p> 
*@author 박준현
*@version 1.0
*/
package com.ziaan.beta;

import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

public class BetaFAQBean { 
    private ConfigSet  config     ;
    private int         row       ;
    private int        gubun   = 4;
    
    public BetaFAQBean() { 
       
    }

    /**
    Faq화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   Faq 리스트
    */
    public ArrayList selectListFaq(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        String              sql             = "";
        DataBox             dbox            = null;

        String              v_faqcategory   = box.getString("p_faqcategory" );
        String              v_searchtext    = box.getString("p_searchtext"  );
        String              v_search        = box.getString("p_search"      );
        int                 v_pageno        = box.getInt("p_pageno"         );
        
        try { 
            connMgr = new DBConnectionManager();

            list    = new ArrayList();

            sql  = " select a.fnum, a.title, a.contents, a.indate, a.luserid, a.ldate  ";
            sql += "   from TZ_FAQ  a,tz_faq_category b                                       ";
            sql += "    where a.faqcategory = b.faqcategory";
            sql += "  and  b.faqgubun   = " + gubun;
            sql += " and a.faqcategory   = " + SQLString.Format(v_faqcategory);
            sql += " order by a.faqcategory desc";
        
            ls      = connMgr.executeQuery(sql);
            
            ls.setPageSize   ( row      );                  // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage( v_pageno );                  // 현재페이지번호를 세팅한다.
            int total_page_count    = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            int total_row_count     = ls.getTotalCount();   // 전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                dbox.put("d_dispnum"    , new Integer(total_row_count - ls.getRowNum() + 1) );
                dbox.put("d_totalpage"  , new Integer(total_page_count)                     );
                dbox.put("d_rowcount"   , new Integer(row)                                  );
                
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
     * FAQ 카테고리 셀렉트박스
     * @param name           셀렉트박스명
     * @param selected       선택값
     * @param event          이벤트명
     * @param allcheck       전체유무
     * @return
     * @throws Exception
     */
    public static String betaGetFaqCategorySelecct (String name, String selected, String event, int allcheck) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        String              result  = "";
        FaqCategoryData     data    = null;
        
        result = "  <SELECT name= " + "\"" +name + "\"" + " " + event + " > \n";
        
        if ( allcheck == 1) { 
            result += "    <option value='' >== 선택하세요 == </option > \n";
        }
        
        try { 
            connMgr = new DBConnectionManager();

            sql     = " select faqcategory,faqcategorynm from TZ_FAQ_CATEGORY where faqgubun = '4' ";
            sql     += " order by faqcategory asc                        ";

            ls      = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                data = new FaqCategoryData();
                
                data.setFaqCategory     ( ls.getString("faqcategory")   );
                data.setFaqCategorynm   ( ls.getString("faqcategorynm") );
                
                result += "    <option value=" + "\"" +data.getFaqCategory()  + "\"";
                
                if ( selected.equals(data.getFaqCategory()) ) { 
                    result += " selected ";
                }
                
                result += " > " + data.getFaqCategorynm() + "</option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        
        return result;
    }
}
