// **********************************************************
//  1. 제      목: 교육그룹대상 회사OPERATION BEAN
//  2. 프로그램명: GrcompBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.3
//  5. 버      젼: 0.1
//  6. 작      성: LeeSuMin 2003. 07. 16
//  7. 수      정:
// **********************************************************
package com.ziaan.course    ;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class GrcompBean { 
	public String  compTxt = null;
	
	public GrcompBean() { }

	/**
	교육그룹리스트 조회
	@param box          receive from the form object and session
	@return ArrayList   교육그룹리스트
	*/      
	public ArrayList SelectGrcompList(RequestBox box) throws Exception {               
		String    v_grcode    = box.getString("p_grcode");
        
		return SelectGrcompList(box,v_grcode);
	}
    
    public ArrayList SelectGrcompList(RequestBox box, String p_grcode) throws Exception {               
        DBConnectionManager connMgr     = null;
		ListSet             ls          = null;
		ArrayList           list1       = new ArrayList();
        GrcompData          data        = null;
        
		StringBuffer        sbSQL       = new StringBuffer("");
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

		try {
            connMgr     = new DBConnectionManager();
            
            sbSQL.append(" SELECT  grcode                                   \n")
                 .append("     ,   a.comp                                   \n")
                 .append("     ,   a.indate                                 \n")
                 .append("     ,   a.luserid                                \n")
                 .append("     ,   a.ldate                                  \n")
                 .append("     ,   compnm                                   \n")
                 /*.append("     ,   groups                                   \n")
                 .append("     ,   company                                  \n")
                 .append("     ,   gpm                                      \n")
                 .append("     ,   dept                                     \n")
                 .append("     ,   part                                     \n")
                 .append("     ,   groupsnm                                 \n")
                 .append("     ,   companynm                                \n")
                 .append("     ,   gpmnm                                    \n")
                 .append("     ,   deptnm                                   \n")
                 .append("     ,   partnm                                   \n")
                 .append("     ,   comptype                                 \n")*/
                 .append(" FROM    tz_grcomp       a                        \n")
                 .append("     ,   tz_compclass    b                        \n")
                 .append(" WHERE   a.comp  = b.comp                         \n")
                 .append(" AND     grcode  = nvl(" + StringManager.makeSQL(p_grcode) + ",'')      \n");
            
            System.out.println(this.getClass().getName() + "." + "SelectGrcompList() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

			ls           = connMgr.executeQuery(sbSQL.toString());
            
			while ( ls.next() ) { 
				data    = new GrcompData();
                
                data.setGrcode      ( ls.getString  ("grcode"   ) );             
                data.setComp        ( ls.getString  ("comp"     ) );             
                data.setIndate      ( ls.getString  ("indate"   ) );             
                data.setLuserid     ( ls.getString  ("luserid"  ) );             
                data.setLdate       ( ls.getString  ("ldate"    ) );
                data.setCompnm      ( ls.getString  ("compnm"   ) );
                /*data.setGroups      ( ls.getString  ("groups"   ) );
                data.setCompany     ( ls.getString  ("company"  ) );
                data.setGpm         ( ls.getString  ("gpm"      ) );
                data.setDept        ( ls.getString  ("dept"     ) );
                data.setPart        ( ls.getString  ("part"     ) );
                data.setGroupsnm    ( ls.getString  ("groupsnm" ) );
                data.setCompanynm   ( ls.getString  ("companynm") );
                data.setGpmnm       ( ls.getString  ("gpmnm"    ) );
                data.setDeptnm      ( ls.getString  ("deptnm"   ) );
                data.setPartnm      ( ls.getString  ("partnm"   ) );
                data.setComptype    ( ls.getInt     ("comptype" ) );*/
                
                list1.add(data);
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

        return list1;
    }

    
    public String getCompTxt(ArrayList lists, String p_grcode) { 
		GrcompData    gd          = null;
		compTxt                   = "";
		int	          v_comptype  = 0;
		
		for ( int i = 0;i<lists.size();i++ ) { 
			gd           = (GrcompData)lists.get(i);
			//v_comptype   = gd.getComptype();
            
			compTxt      = compTxt + "<br> ";
            
			compTxt		+= gd.getCompnm();
			
			// 2008.10.14 수정. KT인재개발원에서는 comptype과 무관하게 사용함. 
			/*
			if      ( v_comptype == 1)	 compTxt += gd.getGroupsnm() + "그룹";
			else if ( v_comptype == 2)	 compTxt += gd.getCompanynm();
			else if ( v_comptype == 3)	 compTxt += gd.getCompanynm() + "-" +gd.getGpmnm();
			else if ( v_comptype == 4)	 compTxt += gd.getCompanynm() + "-" +gd.getGpmnm() + "-" +gd.getDeptnm();
			else if ( v_comptype == 5)	 compTxt += gd.getCompanynm() + "-" +gd.getGpmnm() + "-" +gd.getDeptnm() +gd.getPartnm();
			else	                     compTxt += "??";
			*/
		}
        
		return compTxt;
	}
}
