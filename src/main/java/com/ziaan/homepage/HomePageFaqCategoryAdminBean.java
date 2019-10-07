
// **********************************************************
//  1. ��      ��: HomePage FAQ ī�װ� ����
//  2. ���α׷��� : HomePageFaqCategoryAdminBean.java
//  3. ��      ��: HomePage FAQ ī�װ� ����
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: �̿��� 2005. 6. 27
//  7. ��      ��:
// **********************************************************

package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeConfigBean;

/**
 * FAQ ī�װ� ����(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class HomePageFaqCategoryAdminBean { 

    public HomePageFaqCategoryAdminBean() { }

    /**
    * FAQ ī�װ�ȭ�� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   ��з� �ڵ� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListHomePageFaqCategory(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        ListSet             ls       = null;
        ArrayList           list     = null;
        String              sql      = "";
        DataBox             dbox     = null;
        String              v_comp   = box.getString("p_comp");
        String              v_grcode = box.getString("p_grcode");

        String v_faqcategorynm = box.getString("p_faqcategorynm2");
        String v_faqgubun = box.getStringDefault("p_faqgubun","FAQ");
        /*if ( v_faqcategorynm.equals("") ) { 
           v_faqcategorynm = "%";
        }*/
        String v_order      = box.getString("p_order");
        String v_orderType     = box.getString("p_orderType");                 // ������ ����


        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

/*            sql  = " select faqcategory, faqcategorynm, luserid, ldate from TZ_FAQ_CATEGORY    ";
            sql += " where faqgubun = '5' and faqcategorynm like " +  SQLString.Format("%" +v_faqcategorynm + "%") ;


			if ( v_order.equals("faqcategory"))      v_order ="faqcategory";
			if ( v_order.equals("faqcategorynm"))     v_order ="faqcategorynm";
			if ( v_order.equals("") ) { 
				sql += " order by faqcategory asc ";
			} else { 
				sql += "order by " + v_order + v_orderType;
			}
*/
            sql  = " select faqcategory, faqcategorynm, luserid, ldate from TZ_FAQ_CATEGORY    ";
            sql += " where faqgubun = '"+v_faqgubun+"' " ;
            if ( !v_faqcategorynm.equals("") ) {      //    �˻�� ������
                // v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�
 
                sql += " and faqcategorynm like " +  SQLString.Format("%" +v_faqcategorynm + "%") ;
                
            }

            //System.out.println("v_order::::::::" +v_order);
			if ( v_order.equals("faqcategory"))      v_order ="faqcategory";
			if ( v_order.equals("faqcategorynm"))    v_order ="faqcategorynm";
			if ( v_order.equals("") ) { 
				sql += " order by faqcategory asc ";
			} else { 
				sql += "order by " + v_order + v_orderType;
			}

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
     * ī�װ� �ڵ带 ���� - VLC
     */
    public DataBox HomePageFaqCategorySetting(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox             dbox    = null;
        ListSet             ls      = null;
        String              sql     = "";
        
        String v_faqcategory = "";
        //String v_comp   = box.getString("p_comp");
        //String v_grcode = box.getString("p_grcode");        
        
        try { 
            connMgr = new DBConnectionManager();
            
            // ----------------------ī�װ� �ڵ带 ���� ----------------------------
            sql = "select nvl(max(faqcategory),'0000') as faqcategory from TZ_FAQ_CATEGORY ";
            ls = connMgr.executeQuery(sql);
            ls.next();
            v_faqcategory = ls.getString("faqcategory");
            if ( v_faqcategory == "") { 
                v_faqcategory = "0000";
            }
            int v_faqcategory_int = Integer.parseInt(v_faqcategory);  // ī�װ��ڵ带 int������ ��ȯ

            v_faqcategory_int = v_faqcategory_int + 1; // ��ȯ�� ī�װ��ڵ忡 + 1
            
            v_faqcategory = "" + v_faqcategory_int;  // ī�װ��ڵ带 �ٽ� String������ ��ȯ

            v_faqcategory = CodeConfigBean.addZero(StringManager.toInt(v_faqcategory), 4);  // '0'�� �ٿ��ִ� �޼ҵ� (4�ڸ�)

            // ------------------------------------------------------------------------------------  
            dbox = ls.getDataBox();

            dbox.put("d_faqcategory", v_faqcategory);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }   

    /**
    * HomePage FAQ ī�װ�ȭ�� �󼼺���
    * @param box          receive from the form object and session
    * @return ArrayList   ��ȸ�� ������
    * @throws Exception
    */  
   public DataBox selectHomePageFaqCategory(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String v_faqgubun = box.getStringDefault("p_faqgubun","FAQ");
        String v_faqcategory = box.getString("p_faqcategory");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select faqcategory, faqcategorynm, luserid, ldate  from TZ_FAQ_CATEGORY  ";
            sql += "  where v_faqgubun = '"+v_faqgubun+"' AND faqcategory  = " +  SQLString.Format(v_faqcategory) ;

            ls = connMgr.executeQuery(sql);
            
            ls.next();
            
            dbox = ls.getDataBox();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }

    /**
    * FAQ ī�װ� ����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */  
    public int insertHomePageFaqCategory(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;

        String v_faqcategory = box.getString("p_faqcategory");
        String v_faqcategorynm = box.getString("p_faqcategorynm");
        String s_userid = box.getSession("userid");
        String v_faqgubun = box.getStringDefault("p_faqgubun","FAQ");
        try { 
           connMgr = new DBConnectionManager();

           sql1 =  "insert into TZ_FAQ_CATEGORY(faqcategory, faqcategorynm, luserid, ldate, faqgubun)           ";
           sql1 += "            values (?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), '"+v_faqgubun+"')  ";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setString(1, v_faqcategory);
           pstmt.setString(2, v_faqcategorynm);
           pstmt.setString(3, s_userid);

           isOk = pstmt.executeUpdate();

           if ( isOk > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    * FAQ ī�װ� �����Ͽ� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */  
     public int updateHomePageFaqCategory(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        String s_userid = box.getSession("userid");

        String v_faqcategory   = box.getString("p_faqcategory");
        String v_faqcategorynm = box.getString("p_faqcategorynm");
        
        try { 
            connMgr = new DBConnectionManager();

            sql  = " update TZ_FAQ_CATEGORY set faqcategorynm = ? , luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += "  where faqcategory = ?                                                                                    ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_faqcategorynm);
            pstmt.setString(2, s_userid);
            pstmt.setString(3, v_faqcategory);

            isOk = pstmt.executeUpdate();

            if ( isOk > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * FAQ ī�װ� �����Ҷ� - ���� FAQ ����
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteHomePageFaqCategory(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";

        int isOk1 = 0;
        int isOk2 = 0;

        String v_faqcategory  = box.getString("p_faqcategory");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1  = " delete from TZ_FAQ_CATEGORY  ";
            sql1 += "   where faqcategory = ?             ";

            sql2  = " delete from TZ_FAQ ";
            sql2 += "   where faqcategory = ?   ";


            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_faqcategory);
            isOk1 = pstmt1.executeUpdate();


            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setString(1, v_faqcategory);
            isOk2 = pstmt2.executeUpdate();

            if ( isOk1 > 0 ) connMgr.commit();        // ���� �з��� �ο찡 ������� isOk2 �� 0�̹Ƿ� isOk2 > 0 ���� ����
            else connMgr.rollback();

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1 + "\r\n" +sql2);
            throw new Exception("sql = " + sql1 + "\r\n" + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { };
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
//        return isOk1*isOk2;
        return isOk1;
    }
    
    /**
    * FAQ ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListHomePageFaq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_searchtext  = box.getString("p_searchtext");
        String v_search      = box.getString("p_search");
        String v_faqcategory = box.getString("p_faqcategory");
        String v_order       = box.getString("p_order");
		//System.out.println("v_order:::" +v_order);
        String v_orderType   = box.getString("p_orderType");                 // ������ ����

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select fnum, title, contents, indate, luserid, ldate  ";
            sql += "   from TZ_FAQ                                         ";
            sql += "  where faqcategory   = " + SQLString.Format(v_faqcategory) ;

            if ( !v_searchtext.equals("") ) {                            //    �˻�� ������

                if ( v_search.equals("title") ) {                          //    �������� �˻��Ҷ�
                    sql += " and lower(title) like lower(" + SQLString.Format("%" + v_searchtext + "%") + ")";
                } else if ( v_search.equals("contents") ) {                //    �������� �˻��Ҷ�
                    sql += " and contents like lower(" + SQLString.Format("%" + v_searchtext + "%") + ")";
                }
            }
			if ( v_order.equals("fnum"))      v_order ="fnum";
			if ( v_order.equals("title"))     v_order ="title";
			if ( (v_order.equals("name"))||(v_order.equals(""))) { 
				sql += " order by fnum desc ";
			} else { 
				sql += " order by " + v_order + v_orderType;
			}
			//System.out.println("sql::::" +sql);
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
     * FAQ ����Ʈ
     * @param box          receive from the form object and session
     * @return ArrayList   ����Ʈ
     * @throws Exception
     */
     public ArrayList selectListCompList(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;

         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         DataBox             dbox    = null;

         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql  = " select compnm, comp from tz_compclass ";

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
      * FAQ ����Ʈ
      * @param box          receive from the form object and session
      * @return ArrayList   ����Ʈ
      * @throws Exception
      */
      public ArrayList selectListGrcodeList(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;

          ListSet             ls      = null;
          ArrayList           list    = null;
          String              sql     = "";
          DataBox             dbox    = null;

          try { 
              connMgr = new DBConnectionManager();

              list = new ArrayList();

              sql  = " select grcodenm, grcode from tz_grcode  ";
              
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
     * faq �ڵ带 ���� - VLC
     */
    public DataBox HomePageFaqSetting(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox             dbox    = null;
        ListSet             ls      = null;
        String              sql     = "";
        
        int v_fnum = 0;
        String v_faqcategory = box.getString("p_faqcategory");
        
        try { 
            connMgr = new DBConnectionManager();
            
            // ----------------------ī�װ� �ڵ带 ���� ----------------------------
            sql = "select nvl(max(fnum),'0000') as fnum from TZ_FAQ ";
            
            
            sql += " where faqcategory  = " + SQLString.Format(v_faqcategory) ;

            ls = connMgr.executeQuery(sql);
            ls.next();

            v_fnum = ls.getInt("fnum") + 1; 
          
            dbox = ls.getDataBox();

            dbox.put("d_fnum", "" + v_fnum);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }   
    
    /**
    * FAQ ����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */  
    public int insertHomePageFaq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk = 0;
        
        String v_faqcategory = box.getString("p_faqcategory");
        String v_title = box.getString("p_title");
        String v_contents = box.getString("p_contents");
        int v_fnum = box.getInt("p_fnum");
        String s_userid = box.getSession("userid");
        String v_comp = box.getString("p_comp");
        String v_grcode = box.getString("p_grcode");

        /*********************************************************************************************/
        // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_contents); // ��ü���� 
        boolean result = namo.parse(); // ���� �Ľ� ���� 
        if ( !result ) { // �Ľ� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
            String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
            String prefix = "faq" + v_faqcategory;         // ���ϸ� ���ξ�
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
        }
        if ( !result ) { // �������� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        v_contents = namo.getContent(); // ���� ����Ʈ ���
*/        
        /*********************************************************************************************/

        try { 
           connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
           sql1 =  "insert into TZ_FAQ(faqcategory, fnum, title, contents, indate, luserid, ldate ) ";
           sql1 += "values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))  ";
//           sql1 += "values (?, ?, ?, empty_clob(), to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))  ";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setString(1, v_faqcategory);
           pstmt.setInt(2, v_fnum);
           pstmt.setString(3, v_title);
           pstmt.setString(4, v_contents);
           pstmt.setString(5, s_userid);
           //pstmt.setString(5, v_comp);
           //pstmt.setString(6, v_grcode);

           isOk = pstmt.executeUpdate();
       
           sql2 = "select contents from TZ_FAQ";
           sql2 += "  where faqcategory    = " + StringManager.makeSQL(v_faqcategory);
           sql2 += "    and fnum           = " + v_fnum;
           //System.out.println("sql2:::::::::::::" +sql2);
//           connMgr.setOracleCLOB(sql2, v_contents);       //      (��Ÿ ���� ���)   



           if ( isOk > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { };
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
    /**
    * HomePage FAQ ȭ�� �󼼺���
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public DataBox selectHomePageFaq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_faqcategory = box.getString("p_faqcategory");
        String v_faqcategorynm = box.getString("p_faqcategorynm");
        int v_fnum = box.getInt("p_fnum");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select title, contents  from TZ_FAQ  ";
            sql += "  where ";
            //sql += "faqcategory  = " +  SQLString.Format(v_faqcategory)+" and " ;
            sql += "  fnum  = " +  v_fnum ;
//System.out.println(" ==  ==  ==  ==  ==  ==  ==  == " +sql);
            ls = connMgr.executeQuery(sql);
            
            ls.next();
            
            dbox = ls.getDataBox();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
    
    /**
    * FAQ �����Ͽ� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */  
     public int updateHomePageFaq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        String s_userid = box.getSession("userid");

        String v_faqcategory   = box.getString("p_faqcategory");
        String v_pre_faqcategory = box.getString("p_pre_faqcategory");
        int v_fnum = box.getInt("p_fnum");
        String v_title = box.getString("p_title");
        String v_contents = box.getString("p_content");

        /*********************************************************************************************/
        // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_contents); // ��ü���� 
        boolean result = namo.parse(); // ���� �Ľ� ���� 
        if ( !result ) { // �Ľ� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
            String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
            String prefix = "faq" + v_faqcategory;         // ���ϸ� ���ξ�
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
        }
        if ( !result ) { // �������� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        v_contents = namo.getContent(); // ���� ����Ʈ ���
*/        
        /*********************************************************************************************/





        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            sql  = " update TZ_FAQ set faqcategory = ?, title = ? , contents = ?, luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
//            sql  = " update TZ_FAQ set faqcategory = ?, title = ? , contents = empty_clob(), luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += "  where faqcategory = ? and fnum = ? ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_faqcategory);
            pstmt.setString(2, v_title);
            pstmt.setString(3, v_contents);
            pstmt.setString(4, s_userid);
            pstmt.setString(5, v_pre_faqcategory);
            pstmt.setInt(6, v_fnum);



            isOk = pstmt.executeUpdate();
            sql = "select contents from TZ_FAQ";
            sql += "  where faqcategory = " + v_faqcategory + " and fnum = " + v_fnum  ;
            
//            connMgr.setOracleCLOB(sql, v_contents);       //      (ORACLE 9i ����)

            if ( isOk > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    * FAQ �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteHomePageFaq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_faqcategory  = box.getString("p_faqcategory");
        int v_fnum = box.getInt("p_fnum");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = " delete from TZ_FAQ ";
            sql += "   where faqcategory = ? and fnum = ? ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_faqcategory);
            pstmt.setInt(2, v_fnum);

            isOk = pstmt.executeUpdate();
        
            if ( isOk > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n"  + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
//        return isOk1*isOk2;
        return isOk;
    }

/************************************* �����Լ��� ***********************************************************/


    /**
     * FAQ ī�װ��� 
     * @param faqcategory      ī�װ� �ڵ�
     * @return result   ī�װ� �ڵ��
     * @throws Exception
     */    
    public static String getFaqCategoryName (String faqcategory) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        FaqCategoryData data = null;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select faqcategorynm from TZ_FAQ_CATEGORY        ";
            sql += "  where faqcategory = " + SQLString.Format(faqcategory); 
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new FaqCategoryData();
                data.setFaqCategorynm( ls.getString("faqcategorynm") );
                result = data.getFaqCategorynm();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /**
     * FAQ ī�װ� ����Ʈ�ڽ�
     * @param name           ����Ʈ�ڽ���
     * @param selected       ���ð�
     * @param event          �̺�Ʈ��
     * @param allcheck       ��ü����
     * @return
     * @throws Exception
     */
    public static String getFaqCategorySelecct (String name, String selected, String event, int allcheck) throws Exception {
    	return getFaqCategorySelecct(name, selected, event, allcheck, "FAQ");
    }
    public static String getFaqCategorySelecct (String name, String selected, String event, int allcheck, String faqgubun) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        FaqCategoryData data = null;



        result = "  <SELECT name= " + "\"" +name + "\"" + " " + event + " > \n";
        if ( allcheck == 1) { 
          result += "    <option value='' >== �����ϼ��� == </option > \n";
        }

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select faqcategory,faqcategorynm from TZ_FAQ_CATEGORY where faqgubun = '"+faqgubun+"' ";
            sql += " order by faqcategory asc                        ";

            ls = connMgr.executeQuery(sql);

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
        return result;

    }

    /**
     * FAQ ī�װ� ����Ʈ�ڽ�
     * @param name           ����Ʈ�ڽ���
     * @param selected       ���ð�
     * @param event          �̺�Ʈ��
     * @param allcheck       ��ü����
     * @return
     * @throws Exception
     */
    public static String getCompnm (String comp) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls    = null;
        String  sql   = "";
        String result = "";
        
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select compnm from tz_compclass where comp = " +SQLString.Format(comp);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                result = ls.getString("compnm");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;

    }
    
    /**
     * FAQ ī�װ� ����Ʈ�ڽ�
     * @param name           ����Ʈ�ڽ���
     * @param selected       ���ð�
     * @param event          �̺�Ʈ��
     * @param allcheck       ��ü����
     * @return
     * @throws Exception
     */
    public static String getGrcodenm (String grcode) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls    = null;
        String  sql   = "";
        String result = "";
        
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select grcodenm from tz_grcode where grcode = " +SQLString.Format(grcode);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                result = ls.getString("grcodenm");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;

    }
    

}
