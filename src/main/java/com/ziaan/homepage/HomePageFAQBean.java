
// **********************************************************
//  1. 제      목: Faq 관리
//  2. 프로그램명 : FaqAdminBean.java
//  3. 개      요: Faq 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7. 13
//  7. 수      정:
// **********************************************************

package com.ziaan.homepage;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

/**
 * Faq 관리(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class HomePageFAQBean { 
	private ConfigSet config;
    private int row;
	private String gubun = "FAQ";
	public HomePageFAQBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }



    /**
    Faq화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   Faq 리스트

    public ArrayList selectListFaq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_faqcategory = box.getString("p_faqcategory");
		String v_searchtxt = box.getString("p_searchtxt");
		
		// System.out.println("v_faqcategory =- == = > " + v_faqcategory);
       
		String v_searchtext = box.getString("p_searchtext");
        String v_search = box.getString("p_search");
		int v_pageno        = box.getInt("p_pageno");
        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.fnum, a.title, a.contents, a.indate, a.luserid, a.ldate  ";
            sql += "   from TZ_FAQ  a,tz_faq_category b                                       ";
            sql += "	where a.faqcategory = b.faqcategory";
			sql += "  and  b.faqgubun   = " + gubun;

			if ( v_faqcategory.equals("select") || v_faqcategory.equals("") ) { 
				
			} else { 
				sql += " and a.faqcategory   = " + SQLString.Format(v_faqcategory);
			}

			if ( !v_searchtxt.equals("") ) { 
				sql += " and a.title   like '%" + v_searchtxt + "%'";
			}

           
            sql += " order by a.faqcategory desc";
			//System.out.println("faq value ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == =" +sql);
            ls = connMgr.executeQuery(sql);
			ls.setPageSize(row);             //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();       //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();    //     전체 row 수를 반환한다

           while ( ls.next() ) { 
				// System.out.println("while돌면서 data에 담기");
                dbox = ls.getDataBox();
				
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                
                list.add(dbox);
               // System.out.println("list에 add하기");
               
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
    */
    /**
     * FAQ 카테고리 셀렉트박스
	 * @param name           셀렉트박스명
	 * @param selected       선택값
	 * @param event          이벤트명
	 * @param allcheck       전체유무
	 * @return
	 * @throws Exception
	 */
    public static String homepageGetFaqCategorySelecct (String name, String selected, String event, int allcheck, String faqgubun) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        FaqCategoryData data = null;
		// System.out.println("name == > "  + name);
		// System.out.println("selected == > " + selected);
		// System.out.println("event == > " + event);
		// System.out.println("allcheck == > " + allcheck);



        result = "  <SELECT name= " + "\"" +name + "\"" + " " + event + " > \n"; 
        if ( allcheck == 1) { 
			// System.out.println(" if ( allcheck == 1) { 실행하다");
          result += "    <option value='select' > ALL</option > \n";
        }
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select faqcategory,faqcategorynm from TZ_FAQ_CATEGORY where faqgubun = '"+faqgubun+"' ";
            sql += " order by faqcategory asc                        ";

            ls = connMgr.executeQuery(sql);
			// System.out.println(sql);


            while ( ls.next() ) { 

                data = new FaqCategoryData();
                data.setFaqCategory( ls.getString("faqcategory") );
                data.setFaqCategorynm( ls.getString("faqcategorynm") );
				

	
                result += "    <option value=" + "\"" +data.getFaqCategory()  + "\"";
                if ( selected.equals(data.getFaqCategory() )) { 
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
		// System.out.println("result ==  ==  ==  ==  ==  == = > " + result);
        return result;

    }

    /**
    Faq화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   Faq 리스트
**/
    public ArrayList selectListFaq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_faqcategory = box.getString("faqcategory");
		String v_searchtxt = box.getString("p_searchtxt");
		
		if (!"".equals(box.getString("p_faqgubun"))) {
			gubun = box.getString("p_faqgubun");
		}
		
		// System.out.println("v_faqcategory =- == = > " + v_faqcategory);
       
		String v_searchtext = box.getString("p_searchtext");
        String v_search = box.getString("p_search");
		int v_pageno        = box.getInt("p_pageno");
        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.fnum, a.title, a.contents, a.indate, a.luserid, a.ldate, a.save_file, a.real_file   \n";
            sql += "   from TZ_FAQ  a,tz_faq_category b  \n";
            sql += "	where a.faqcategory = b.faqcategory   \n";
			sql += "  and  b.faqgubun   = '"+gubun+"'";

//System.out.println("v_faqcategory::::::::::::::::::" +v_faqcategory);

			if ( v_faqcategory.equals("ALL") || v_faqcategory.equals("") ) { 
				
			} else { 
				sql += " and a.faqcategory   = " + v_faqcategory;
			}

			if ( !v_searchtxt.equals("") ) { 
				sql += " and ( upper(a.title)   like upper(" + SQLString.Format("%"+v_searchtxt+"%") + ") or upper(a.contents)   like upper(" + SQLString.Format("%"+v_searchtxt+"%") + ")) ";
			}

           
            sql += " order by a.faqcategory desc";
			//System.out.println("faq value ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == =" +sql);
            ls = connMgr.executeQuery(sql);
			/*ls.setPageSize(row);             //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();       //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();    //     전체 row 수를 반환한다*/

           while ( ls.next() ) { 
				// System.out.println("while돌면서 data에 담기");
                dbox = ls.getDataBox();
				
                /*dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));*/
                
                list.add(dbox);
               // System.out.println("list에 add하기");
               
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
     * FAQ 카테고리 테이블 셀렉트
	 * @param name           셀렉트박스명
	 * @param selected       선택값
	 * @param event          이벤트명
	 * @param allcheck       전체유무
	 * @return
	 * @throws Exception
	 */
     public static String homepageGetFaqCategoryTable(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
		ListSet ls1 = null;
		ListSet ls2 = null;
		ListSet ls3 = null;
        String              sql     = "";
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
        String result = "";
		int count =0;
        ResultSet rs1 = null;

	    Statement stmt1 = null;
        FaqCategoryData data = null;
		int i = 2;
		String v_faqcategory = box.getString("faqcategory");

		String v_faqgubun = box.getStringDefault("p_faqgubun","FAQ");
		
        try { 
            connMgr = new DBConnectionManager();


			sql3 = "select count(*) categoryTotCnt from TZ_FAQ WHERE FAQGUBUN = '"+v_faqgubun+"'";
			ls3 = connMgr.executeQuery(sql3);
			ls3.next();
			int categoryTotCnt = ls3.getInt(1);
			ls3.close();

			result = "<ul class='faqCategoryList'>" +
					 "\n	<li><a href=\"javascript:changeCategory('ALL')\"";
            if(v_faqcategory.equals("ALL") || v_faqcategory.equals("")) {
            	result += " class='selectCt'";
            	//result += "<span class='Popup_TextPoint2'>";
            } 
           	result += " >";
            
            result += "전체 (" +categoryTotCnt + ")";
            result += "</a></li>";

            sql1 = "select count(*) cnt from TZ_FAQ_CATEGORY where FAQGUBUN = '"+v_faqgubun+"'  order by faqcategory asc ";
            ls1 = connMgr.executeQuery(sql1);
            if ( ls1.next() ) { 
                count = ls1.getInt("cnt") ;
            }
			 ls1.close();

            sql  = " select faqcategory,faqcategorynm from TZ_FAQ_CATEGORY where FAQGUBUN = '"+v_faqgubun+"' ";
            sql += " order by faqcategory asc                        ";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new FaqCategoryData();
                data.setFaqCategory( ls.getString("faqcategory") );
                data.setFaqCategorynm( ls.getString("faqcategorynm") );

				sql2 = "select count(*) categoryCnt from TZ_FAQ where FAQGUBUN = '"+v_faqgubun+"' AND faqcategory = " + SQLString.Format(data.getFaqCategory() );
				ls2 = connMgr.executeQuery(sql2);
				ls2.next();
				int categoryCnt = ls2.getInt(1);
				ls2.close();

				result +="\n	<li><a href='#' onclick=\"javascript:changeCategory('" +data.getFaqCategory() + "')\" ";
                      
                      if(v_faqcategory.equals(data.getFaqCategory())) {
                    	  result += " class='selectCt'";
                      } 
                   	  result += " >";
                      
                      result += data.getFaqCategorynm() + " (" +categoryCnt + ")";
                      result += "</a></li>";                          
      	  
			}
            result += "</ul>";
		}
        
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        

        return result;
        //return "";

    }


     /**
      * FAQ 카테고리 테이블 셀렉트
 	 * @param name           셀렉트박스명
 	 * @param selected       선택값
 	 * @param event          이벤트명
 	 * @param allcheck       전체유무
 	 * @return
 	 * @throws Exception
 	 */
      public static String homepageGetFaqCategoryTable2(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
 		ListSet ls1 = null;
 		ListSet ls2 = null;
 		ListSet ls3 = null;
         String              sql     = "";
 		String sql1 = "";
 		String sql2 = "";
 		String sql3 = "";
         String result = "";
 		int count =0;
         ResultSet rs1 = null;

 	    Statement stmt1 = null;
         FaqCategoryData data = null;
 		int i = 2;
 		String v_faqcategory = box.getString("faqcategory");
 		String v_faqgubun = box.getStringDefault("p_faqgubun","FAQ");
         try { 
             connMgr = new DBConnectionManager();


 				sql3 = "select count(*) categoryTotCnt from TZ_FAQ where FAQGUBUN = '"+v_faqgubun+"'";
 				ls3 = connMgr.executeQuery(sql3);
 				ls3.next();
 				int categoryTotCnt = ls3.getInt(1);
 				ls3.close();
                 /*
                 result = "<table width='100%'  border='0' cellspacing='0' cellpadding='0'>"
                     + "\n   <tr>"
                     + "\n       <td class='CourseBox_topleft'></td>"
                     + "\n       <td class='CourseBox_topcenter'></td>"
                     + "\n       <td class='CourseBox_topright'></td>"
                     + "\n   </tr>"
                     + "\n   <tr>"
                     + "\n       <td class='CourseBox_middleleft'></td>"
                     + "\n       <td class='CourseBox_middlecenter'><table  border='0' cellspacing='0' cellpadding='0'>"
                     + "\n           <tr>"
                     + "\n               <td width='180' class='Helpdesk_Bullet1'><a href=\"javascript:changeCategory('ALL')\" >";
                 */
 				result = "<ul class=\"faqCategoryList\">" +
 						 "\n   <li><a href=\"javascript:changeCategory('ALL')\" ";
                 if(v_faqcategory.equals("ALL") || v_faqcategory.equals("")) {
                 	result += " class=\"selectCt\">";
                 	//result += "<span class='Popup_TextPoint2'>";
                 } else {
                 	result += " >";
                 }
                 
                 result += "전체 (" +categoryTotCnt + ")";
                 result += "</a></li>";

 			/*result =  "\n   <table width='675' border='0' cellpadding='0' cellspacing='0' background='../../../images/user/support/faq_bg.gif' > "
 				  + "\n 	  <tr > "
                   + "\n      <td valign='top' > <img src='../../../images/user/support/faq_top.gif' > </td > "
 				  + "\n 	  </tr > "
 				  + "\n 	  <tr > "
 				  + "\n 		<td align='center' > <table width='500' border='0' cellspacing='0' cellpadding='0' > " 
 				  + "\n    <tr > "
 				  + "\n    <td width='180' class='tbl_cfaq' > <img src='../../../images/user/support/bl_c.gif' width='9' height='9' > <a href=\"javascript:changeCategory('ALL')\" > &nbsp;전체</a > (" +categoryTotCnt + ")</td > ";*/

             sql1 = "select count(*) cnt from TZ_FAQ_CATEGORY where FAQGUBUN = '"+v_faqgubun+"'  order by faqcategory asc ";
             ls1 = connMgr.executeQuery(sql1);
             if ( ls1.next() ) { 
                 count = ls1.getInt("cnt") ;
             }
 			 ls1.close();

             sql  = " select faqcategory,faqcategorynm from TZ_FAQ_CATEGORY where FAQGUBUN = '"+v_faqgubun+"' ";
             sql += " order by faqcategory asc                        ";
             ls = connMgr.executeQuery(sql);

             while ( ls.next() ) { 
                 data = new FaqCategoryData();
                 data.setFaqCategory( ls.getString("faqcategory") );
                 data.setFaqCategorynm( ls.getString("faqcategorynm") );


 				sql2 = "select count(*) categoryCnt from TZ_FAQ where FAQGUBUN = '"+v_faqgubun+"' AND faqcategory = " + SQLString.Format(data.getFaqCategory() );
 				ls2 = connMgr.executeQuery(sql2);
 				ls2.next();
 				int categoryCnt = ls2.getInt(1);
 				ls2.close();


 					if ( i != 1&&i%3 == 1) { 
 					  result +="\n <tr > ";
 					}
                       result +="\n                  <td width='180' class='Helpdesk_Bullet1'><a href=\"javascript:changeCategory('" +data.getFaqCategory() + "')\" >";
                       
                       if(v_faqcategory.equals(data.getFaqCategory())) {
                           result += "<span class='Popup_TextPoint2'>";
                       } 
                       
                       result += data.getFaqCategorynm() + " (" +categoryCnt + ")";
                       
                       if(v_faqcategory.equals(data.getFaqCategory())) {
                           result += "</span>";
                       }
                       result += "</a></td>";                          
               //               + "\n       <a href=\"javascript:changeCategory('" +data.getFaqCategory() + "')\" > " + data.getFaqCategorynm() + "</a > (" +categoryCnt + ")</td > ";

 			       if ( i%3 == 0  && i != (count +1)) { 
                       result +="\n  </tr > ";							
                           /*+ "\n   <tr > "
                           + "\n    <td > <img src='../../../images/user/support/faq_line-1.gif' > </td > "
                           + "\n    <td > <img src='../../../images/user/support/faq_line-1.gif' > </td > "
                           + "\n    <td > <img src='../../../images/user/support/faq_line-1.gif' > </td > "
                           + "\n  </tr > ";*/
 					}                   

   				if ( i != 3&&i%3 == 0) { 
 					  result +="\n </tr > ";
 					}

  					i++;
 	  
 			}
             
 		int mod_cnt = (count +1)%3;

 		if ( mod_cnt != 0 ) { 
 			for ( int j=0; j<(3-mod_cnt); j++ ) { 
 				//result +="<td width='180' class='tbl_cfaq' > </td > ";
 			}


 		}

 		result +="</tr > ";

 		}

         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         
         result += "\n</table></td>"
                + "\n   <td class='CourseBox_middleright'></td>"
                + "\n</tr>"
                + "\n<tr>"
                + "\n    <td class='CourseBox_bottomleft'></td>"
                + "\n    <td class='CourseBox_bottomcenter'></td>"
                + "\n    <td class='CourseBox_bottomright'></td>"
                + "\n</tr>"
                + "\n</table>";

 		/*result +=	"\n   </table > "
 					 + "\n	</td > "
 					 + "\n  </tr > "
 					 + "\n  <tr > "
 					 + "\n	<td valign='bottom' > <img src='../../../images/user/support/faq_bo.gif' > </td > "
 					 + "\n  </tr > "
 					 + "\n</table > ";*/

         return result;

     }


     
     
     
}
