// **********************************************************
//  1. 제      목: 운영본부 메뉴 OPERATION BEAN
//  2. 프로그램명: MenuBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.4
//  5. 버      젼: 0.1
//  6. 작      성: S.W.Kang 2004. 12. 18
//  7. 수      정:
// **********************************************************
package com.ziaan.system;

import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

public class MenuBean { 
	public MenuBean() { 	}
	/** 권한별 사용메뉴리스트 Select
	@param box          receive from the form object and session
	@return ArrayList   
	Menu는 2Level로 한정함을 원칙으로 함
	*/
	public ArrayList SelectList(RequestBox box) throws Exception {               
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList list1 = null;
		ArrayList list2 = null;
		String sql  = "";
		MenuData data = null;
		String	 v_upper= "";
		int		 v_rownum=0;
		
		String v_systemgubun = box.getString("p_systemgubun");
		try {
			// sql 	= getSqlString(box);
			connMgr = new DBConnectionManager();
			list1 	= new ArrayList();

/*
				sql  = "select a.menu menu,";
				sql += "       a.menunm menunm,";
				sql += "       para1,";
				sql += "       para2,";
				sql += "       para3,";
				sql += "       para4,";
				sql += "       para5,";
				sql += "       para6,";
				sql += "       para7,";
				sql += "       para8,";
				sql += "       para9,";
				sql += "       para10,";
				sql += "       para11,";
				sql += "       para12,";
				sql += "       levels,";
				sql += "       upper,";
				sql += "       parent,";
				sql += "       pgm,";
				sql += "       isdisplay";
				sql += "  from TZ_MENU a, ";
				// sql += "       TZ_ADMINMENUAUTH b, ";
				sql += "       TZ_MENUAUTH b, ";
				sql += "       TZ_MENUSUB c ";
				sql += " where a.grcode=nvl('" +box.getString("s_grcode") + "','N000001') ";
				sql += "   and a.grcode=b.grcode and a.menu=b.menu ";
				sql += "   and a.grcode=c.grcode and a.menu=c.menu ";
				sql += "   and b.menusubseq=c.seq and c.seq=0 ";
				// sql += "   and b.userid='" +box.getSession("userid") + "'";
				sql += "   and b.gadmin='" +box.getSession("gadmin") + "'";
				sql += "   and b.control like '%r%' and a.isdisplay = 'Y'  ";
				// if ( v_systemgubun.equals("1") || v_systemgubun.equals("2") ) { 
				// 	sql += "   and a.systemgubun='" +v_systemgubun + "'";
				// }
				// sql += "   and a.systemgubun='1' ";	// tz_menu테이블에서 메인시스템과 4개시스템의 메뉴를 모두 관리하기때문에 구분을 두기 위함.
				sql += " order by a.parent,a.upper,a.levels,a.orders,a.menu ";
				
				System.out.println(sql);
			*/

			sql  = "select a.menu menu,";
			sql += "       a.menunm menunm,";
			sql += "       para1,";
			sql += "       para2,";
			sql += "       para3,";
			sql += "       para4,";
			sql += "       para5,";
			sql += "       para6,";
			sql += "       para7,";
			sql += "       para8,";
			sql += "       para9,";
			sql += "       para10,";
			sql += "       para11,";
			sql += "       para12,";
			sql += "       levels,";
			sql += "       upper,";
			sql += "       parent,";
			sql += "       pgm,";
			sql += "       isdisplay";
			sql += "  from TZ_MENU a, ";
			sql += "       TZ_MENUAUTH b, ";
			sql += "       TZ_MENUSUB c, ";
            sql += "   (                                      ";
            sql += "   select                                 ";
            sql += " 	grcode,                               ";
            sql += "     substr(menu, 1, 2) twocharmenu,      ";
            sql += "     orders uporders                      ";
            sql += "   from                                   ";
            sql += "     tz_menu                              ";
            sql += "   where                                  ";
            sql += "     levels =1                            ";
            sql += "   order by orders                        ";
            sql += "   ) d ";
			sql += " where a.grcode=nvl('"+box.getString("s_grcode")+"','N000001') ";
			sql += "   and a.grcode=b.grcode and a.menu=b.menu ";
			sql += "   and a.grcode=c.grcode and a.menu=c.menu ";
			sql += " and a.grcode = d.grcode                  ";
			//sql += "   and a.grcode=e.grcode and a.menu = e.menu ";
			sql += " and substr(a.menu, 1,2) = d.twocharmenu  ";
			sql += "   and b.menusubseq=c.seq and c.seq=0 ";
			sql += "   and b.gadmin='"+box.getSession("gadmin")+"'";
			sql += "   and b.control like '%r%' and a.isdisplay = 'Y'  ";
			sql += "   and substr(a.menu, 1, 2) not in(select substr(menu, 1, 2) from tz_menu where levels = 1 and isdisplay='N') ";
			sql += "order by d.uporders,a.levels,a.orders,a.menu";
			
			System.out.println(sql);
            ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				data = new MenuData();                   
				data.setMenu      ( ls.getString("menu") );      
				data.setMenunm    ( ls.getString("menunm") );
				data.setPara1     ( ls.getString("para1") );     
				data.setPara2     ( ls.getString("para2") );     
				data.setPara3     ( ls.getString("para3") );     
				data.setPara4     ( ls.getString("para4") );     
				data.setPara5     ( ls.getString("para5") );     
				data.setPara6     ( ls.getString("para6") );     
				data.setPara7     ( ls.getString("para7") );     
				data.setPara8     ( ls.getString("para8") );
				data.setPara9     ( ls.getString("para9") );     
				data.setPara10    ( ls.getString("para10") );     
				data.setPara11    ( ls.getString("para11") );     
				data.setPara12    ( ls.getString("para12") );
				data.setLevels    ( ls.getInt("levels") );    
				data.setUpper     ( ls.getString("upper") );     
				data.setParent    ( ls.getString("parent") );    
				data.setPgm       ( ls.getString("pgm") );       
				data.setIsdisplay ( ls.getString("isdisplay") ); 
				list1.add(data);
			}
           
			list2 = new ArrayList();
			for ( int i = 0; i<list1.size(); i++ ) { 
				data = (MenuData)list1.get(i);
				// 대분류 코드
				if ( !data.getUpper().equals(v_upper)) { 
					v_upper = data.getUpper();
					data.setRowspannum(getUpperCodeCnt(list1,data.getUpper() ));
				} else { 
					data.setRowspannum(1);
				}
				list2.add(data);
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list2;
	}
	
	/**
	하위메뉴코드수 Count
	@param ArrayList   메뉴코드 리스트 
	@param String      상위메뉴코드 
	@return int   	   하위메뉴코드 갯수
	*/      
	public int getUpperCodeCnt(ArrayList list1, String uppercode) { 
		int ncnt = 0;
		MenuData data = null;            

		for ( int i = 0; i<list1.size(); i++ ) { 
			data = (MenuData)list1.get(i);
			
			if ( data.getUpper().equals(uppercode)) { 
				ncnt++;				
			}
		}
		return ncnt;
	}
	
	/**
	Make SQL Query String
	@param box   		parameterBox 
	@return String   	Sql String
	*/      
	public String getSqlString(RequestBox box) { 
		String sqlTxt = "";
		sqlTxt = "select a.menu menu,a.menunm menunm,para1,para2,para3,para4,para5,para6,para7,para8,para9,para10,para11,para12,levels,upper, "
				 + "       parent,pgm,isdisplay"
				 + "  from TZ_MENU a, TZ_ADMINMENUAUTH b, TZ_MENUSUB c "
				 + " where a.grcode=nvl('" +box.getString("s_grcode") + "','N000001') "
				 + "   and a.grcode=b.grcode and a.menu=b.menu "
				 + "   and a.grcode=c.grcode and a.menu=c.menu "
				 + "   and b.menusubseq=c.seq and c.seq=0 "
				 + "   and b.userid='" +box.getSession("userid") + "'"
				 + "   and b.control like '%r%' and a.isdisplay = 'Y'  "
				// + "   and a.systemgubun='" +box.getString("p_systemgubun") + "'"
				// + "   and a.systemgubun='1' "	// tz_menu테이블에서 메인시스템과 4개시스템의 메뉴를 모두 관리하기때문에 구분을 두기 위함.
				 + " order by a.parent,a.upper,a.levels,a.orders,a.menu ";
		return sqlTxt;
	}	

}


