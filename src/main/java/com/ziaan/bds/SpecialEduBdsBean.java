package com.ziaan.bds;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class SpecialEduBdsBean {
	 public SpecialEduBdsBean() {
        try {
        	
        } catch(Exception e) { 
            e.printStackTrace();
        }
    }
	 
	public ArrayList SelectList(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr 		= null;
        ListSet 			ls 				= null;
        ArrayList 			list 			= null;
        String 				sql 			= "";
        DataBox 			dbox			= null;
       
       String v_category = box.getString("p_category");
       
       if("".equals(v_category)){
    	   v_category = "6";
       }
        try { 
            connMgr 						= new DBConnectionManager();

            list 							= new ArrayList();
			
            sql = " SELECT "+ 
				  "		A.FAQCATEGORY,"+
				  "		B.TITLE,"+
				  "		B.CONTENTS,"+
				  "		B.REAL_FILE,"+
				  "		B.SAVE_FILE" +
				  "	FROM TZ_FAQ_CATEGORY A, TZ_FAQ B "+ 
            	  "	WHERE A.FAQGUBUN='DTA' "+
            	  "	AND A.FAQCATEGORY = B.FAQCATEGORY "+
            	  "	AND A.FAQCATEGORY = '"+v_category+"' "+
            	  "	ORDER BY A.LDATE ";
           //System.out.println(sql);
            ls 		= connMgr.executeQuery(sql);		// 페이지당 row 갯수를 세팅한다.
           
            while ( ls.next() ) { 
            	dbox 	= ls.getDataBox();
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
        	ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { 
            	try { 
            		ls.close(); 
            	} catch ( Exception e ) { 
            	} 
            }
            
            if ( connMgr != null ) { 
            	try { 
            		connMgr.freeConnection(); 
            	} catch ( Exception e10 ) { 
            	} 
            }
        }
        
        return list;
    }

	public ArrayList SelectListCnt(RequestBox box) throws Exception{
		DBConnectionManager connMgr 		= null;
        ListSet 			ls 				= null;
        ArrayList 			list 			= null;
        String 				sql 			= "";
        DataBox 			dbox			= null;

        try { 
            connMgr 						= new DBConnectionManager();

            list 							= new ArrayList();
			
            sql = " SELECT "+ 
				  "		A.FAQCATEGORY,"+
				  "		count(1) as cnt "+
				  "	FROM TZ_FAQ_CATEGORY A, TZ_FAQ B "+ 
            	  "	WHERE A.FAQGUBUN='DTA' "+
            	  "	AND A.FAQCATEGORY = B.FAQCATEGORY "+
            	  " group by A.FAQCATEGORY ";
            
            ls 		= connMgr.executeQuery(sql);		// 페이지당 row 갯수를 세팅한다.
           
            while ( ls.next() ) { 
            	dbox 	= ls.getDataBox();
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
        	ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { 
            	try { 
            		ls.close(); 
            	} catch ( Exception e ) { 
            	} 
            }
            
            if ( connMgr != null ) { 
            	try { 
            		connMgr.freeConnection(); 
            	} catch ( Exception e10 ) { 
            	} 
            }
        }
        
        return list;
	}
	
}
