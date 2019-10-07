// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: CommunityMsMangeBean.java
// 3. ��      ��:
// 4. ȯ      ��: JDK 1.3
// 5. ��      ��: 0.1
// 6. ��      ��: Administrator 2003-08-29
// 7. ��      ��:
// 
// **********************************************************

package com.ziaan.community;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityMsMangeBean { 
    private ConfigSet config;
    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      ���Ͼ��ε�Ǵ� tag name
//    private static final int FILE_LIMIT = 1;                    //    �������� ���õ� ����÷�� ����


    public CommunityMsMangeBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // �� ����� �������� row ���� �����Ѵ�
            row = 10; // ������ ����
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    * Ŀ�´�Ƽ ȸ������ȭ��
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ ȸ�����ø���Ʈ
    * @throws Exception
    */
    public ArrayList selectMemberList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

        String v_static_cmuno  = box.getString("p_static_cmuno");
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");
        String v_searchtext    = box.getString("p_searchtext");
        String v_select        = box.getString("p_select");
        int v_pageno        = box.getInt("p_pageno");
        try { 
            connMgr = new DBConnectionManager();

            // ��������
//            sql  = " select a.*,rownum rowseq from ("
              sql = " select     a.cmuno           cmuno         , a.userid          userid        "
                  + "          , a.kor_name        kor_name      , a.eng_name        eng_name      "
                  + "          , a.email           email         , a.tel             tel           "
                  + "          , a.mobile          mobile        , a.office_tel      office_tel    "
                  + "          , a.duty            duty          , a.wk_area         wk_area       "
                  + "          , a.grade           grade         , a.request_dte     request_dte   "
                  + "          , a.license_dte     license_dte   , a.license_userid  license_userid"
                  + "          , a.close_fg        close_fg      , a.close_reason    close_reason  "
                  + "          , a.close_dte       close_dte     , a.intro           intro         "
                  + "          , a.recent_dte      recent_dte    , a.visit_num       visit_num     "
                  + "          , a.search_num      search_num    , a.register_num    register_num  "
                  + "          , a.modifier_dte    modifier_dte  , c.kor_nm          grade_kor_nm"
                  //+ "          ,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                  + "   from tz_cmuusermst a,tz_member b,tz_cmugrdcode c "
                  + "  where a.userid = b.userid "  
                  + "    and a.cmuno  = c.cmuno"
                  + "    and a.grade  = c.grcode"
                 + "    and a.cmuno  = '" +v_static_cmuno + "'" 
              	 + "    and close_fg != 0	\n";
            if ( !v_searchtext.equals("") ) {      // �˻�� ������
                 if ( v_select.equals("userid"))   sql += " and lower(a.userid) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("kor_name")) sql += " and a.kor_name like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
            }

            sql += " order by a.kor_name asc";
//                 + " ) a";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);                         // �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                 // ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();    // ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    // ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum"  , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount" , new Integer(row));
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }


    /**
    * Ŀ�´�Ƽ ����Ÿ�����Ф�ȸ������ȭ��
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ ����Ÿ����ȸ�����ø���Ʈ
    * @throws Exception
    */
    public ArrayList selectMemberNoneList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

        String v_static_cmuno  = box.getString("p_static_cmuno");
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");
        String v_searchtext    = box.getString("p_searchtext");
        String v_select        = box.getString("p_select");
        int v_pageno        = box.getInt("p_pageno");
        try { 
            connMgr = new DBConnectionManager();

            // ��������
//            sql  = " select a.*,rownum rowseq from ("
              sql = " select     a.cmuno           cmuno         , a.userid          userid        "
                  + "          , a.kor_name        kor_name      , a.eng_name        eng_name      "
                  + "          , a.email           email         , a.tel             tel           "
                  + "          , a.mobile          mobile        , a.office_tel      office_tel    "
                  + "          , a.duty            duty          , a.wk_area         wk_area       "
                  + "          , a.grade           grade         , a.request_dte     request_dte   "
                  + "          , a.license_dte     license_dte   , a.license_userid  license_userid"
                  + "          , a.close_fg        close_fg      , a.close_reason    close_reason  "
                  + "          , a.close_dte       close_dte     , a.intro           intro         "
                  + "          , a.recent_dte      recent_dte    , a.visit_num       visit_num     "
                  + "          , a.search_num      search_num    , a.register_num    register_num  "
                  + "          , a.modifier_dte    modifier_dte  , c.kor_nm          grade_kor_nm"
                  //+ "          ,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                  + "   from tz_cmuusermst a,tz_member b,tz_cmugrdcode c "
                  + "  where a.userid = b.userid "  
                  + "    and a.cmuno  = c.cmuno"
                  + "    and a.grade  = c.grcode"
                  + "    and a.cmuno  = '" +v_static_cmuno + "'" 
                  + "    and a.grade != '01'";    
            if ( !v_searchtext.equals("") ) {      // �˻�� ������
                 if ( v_select.equals("userid"))   sql += " and lower(a.userid) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("kor_name")) sql += " and a.kor_name like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
            }

            sql += " order by a.kor_name asc";
//                 + " ) a";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);                         // �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                 // ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();    // ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    // ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum"  , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount" , new Integer(row));
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }



    /**
    * Ŀ�´�Ƽ ȸ���ʴ�ȭ��
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ ȸ��������
    * @throws Exception
    */
    public ArrayList selectMemberInvitation(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

        String v_cmuno         = box.getString("p_cmuno");
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");
        String v_searchtext    = box.getString("p_searchtext");
//        String v_select        = box.getString("p_select");

//        String v_display_fg    = "";
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select display_fg"
                 + "   from tz_cmubasemst"
                 + "  where cmuno    = '" +v_cmuno + "'"; 
                 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) 
//            	v_display_fg=ls.getString(1);

            
            sql  = "\n select     b.userid  userid  ,b.email   email,b.name name"
                  //+ "\n           ,b.deptnam deptnam ,b.jikupnm jikupnm,b.jikwinm jikwinm"
                  + "\n   from tz_member b							"
                  + "\n   where b.userid not in (select userid from tz_cmuusermst where cmuno ='" +v_cmuno + "')";
            
            /*
            if ( "1".equals(v_display_fg)) sql += "\n and b.comp = '0101000000'";
            if ( "2".equals(v_display_fg)) sql += "\n and (b.comp = '0101000000' or a.gubun='2')";
            if ( "3".equals(v_display_fg)) sql += "\n and (a.gubun='1' or a.gubun='2')";
            */
             


            if ( !v_searchtext.equals("") ) {      // �˻�� ������
               sql += "\n and (lower(b.userid) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")"
                    + "\n  or b.name like " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
            }

            sql += "\n order by b.name asc";
            if ( v_searchtext.length() > 0) { 
               ls = connMgr.executeQuery(sql);
               while ( ls.next() ) { 
                   dbox = ls.getDataBox();
                   list.add(dbox);
               }
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

    /**
     * Ŀ�´�Ƽ ȸ������ ����������(ȸ���������� üũ�ڽ��� �����Ͽ� ���Խ�)
     * @param box          receive from the form object and session
     * @return ArrayList   ����Ʈ�� ȸ��������
     * @throws Exception
     */
    public ArrayList sendSlip(RequestBox box) throws Exception { 
    	DBConnectionManager	connMgr	= null;
    	ListSet             ls      = null;
    	ArrayList           list    = new ArrayList();
    	String              sql     = "";
    	DataBox             dbox    = null;
    	
    	String v_cmuno         = box.getString("p_cmuno");
    	String v_searchtext    = box.getString("p_searchtext");
    	
    	/* in userid */
    	String v_checksTemp = "";
    	Vector v_checks = box.getVector("p_checks");  
    	for(int i=0; i<v_checks.size(); i++){
    		if(i==0){
    			v_checksTemp += ("(");
    		}
    		v_checksTemp += StringManager.makeSQL((String)v_checks.elementAt(i)) + ",";
    		if(i==v_checks.size()-1){
    			v_checksTemp = v_checksTemp.substring(0, v_checksTemp.length() - 1) + ")";
    		}
    	}
    	
    	try { 
    		connMgr = new DBConnectionManager();
    		
    		sql  = " select display_fg"
    			+ "   from tz_cmubasemst"
    			+ "  where cmuno    = '" +v_cmuno + "'"; 
    		
    		ls = connMgr.executeQuery(sql);
    		while ( ls.next() ) 
    			sql  = "\n select     b.userid  userid  ,b.email   email,b.name name"
    				+ " \n   from tz_member b							"
    			    + " \n   where 1=1 ";
    		if(!v_checksTemp.equals("")){
    			sql += "\n   and b.userid in " + v_checksTemp;
    		}
    		if ( !v_searchtext.equals("") ) {      // �˻�� ������
    			sql += "\n and (lower(b.userid) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")"
    			+ "\n  or b.name like " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
    		}
    		
    		sql += "\n order by b.name asc";
    		ls = connMgr.executeQuery(sql);
    		while ( ls.next() ) { 
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	}
    	catch ( Exception ex ) { 
    		ErrorManager.getErrorStackTrace(ex, box, sql);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	}
    	finally { 
    		if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	}
    	return list;
    }
    
    /**
    * Ŀ�´�Ƽ ȸ������
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ ȸ������
    * @throws Exception
    */
    public ArrayList selectMemberSingleData(RequestBox box,String v_flag) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
//        DataBox             dbox    = null;


        try { 
            connMgr = new DBConnectionManager();

            sql  = " select     a.cmuno           cmuno         , a.userid          userid        "
                  + "          , a.kor_name        kor_name      , a.eng_name        eng_name      "
                  + "          , a.email           email         , a.tel             tel           "
                  + "          , a.mobile          mobile        , a.office_tel      office_tel    "
                  + "          , a.duty            duty          , a.wk_area         wk_area       "
                  + "          , a.grade           grade         , a.request_dte     request_dte   "
                  + "          , a.license_dte     license_dte   , a.license_userid  license_userid"
                  + "          , a.close_fg        close_fg      , a.close_reason    close_reason  "
                  + "          , a.close_dte       close_dte     , a.intro           intro         "
                  + "          , a.recent_dte      recent_dte    , a.visit_num       visit_num     "
                  + "          , a.search_num      search_num    , a.register_num    register_num  "
                  + "          , a.modifier_dte    modifier_dte  , c.kor_nm          grade_kor_nm"
                  //+ "          ,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                  + "   from tz_cmuusermst a,tz_member b,tz_cmugrdcode c "
                  + "  where a.userid = b.userid "  
                  + "    and a.cmuno  = c.cmuno"
                  + "    and a.grade  = c.grcode"
                  + "    and a.cmuno  = '" +box.getString("p_cmuno") + "'" 
                  + "    and a.userid = '" +box.getSession("userid") + "'"
            ; 
            if ( "01".equals(v_flag)) { 
              sql += "    and a.grade = '01'";
            } else { 
              sql += "    and a.grade != '01'";
            } 
            ls = connMgr.executeQuery(sql);


            while ( ls.next() ) { 
                list.add( ls.getDataBox() );
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }



    /**
    * Ŀ�´�Ƽ ȸ������
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ ȸ������
    * @throws Exception
    */
    public ArrayList selectMemSingleData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
//        DataBox             dbox    = null;


        try { 
            connMgr = new DBConnectionManager();

            sql  = "\n  select     a.cmuno           cmuno         , a.userid          userid        "
                  + "\n          , a.kor_name        kor_name      , a.eng_name        eng_name      "
                  + "\n          , a.email           email         , a.tel             tel           "
                  + "\n          , a.mobile          mobile        , a.office_tel      office_tel    "
                  + "\n          , a.duty            duty          , a.wk_area         wk_area       "
                  + "\n          , a.grade           grade         , a.request_dte     request_dte   "
                  + "\n          , a.license_dte     license_dte   , a.license_userid  license_userid"
                  + "\n          , a.close_fg        close_fg      , a.close_reason    close_reason  "
                  + "\n          , a.close_dte       close_dte     , a.intro           intro         "
                  + "\n          , a.recent_dte      recent_dte    , a.visit_num       visit_num     "
                  + "\n          , a.search_num      search_num    , a.register_num    register_num  "
                  + "\n          , a.modifier_dte    modifier_dte  , c.kor_nm          grade_kor_nm"
                  + "\n          , a.fax             fax"
                  //+ "          ,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                  + "\n   from tz_cmuusermst a,tz_member b,tz_cmugrdcode c "
                  + "\n  where a.userid = b.userid "  
                  + "\n    and a.cmuno  = c.cmuno"
                  + "\n    and a.grade  = c.grcode"
                  + "\n    and a.cmuno  = '" +box.getString("p_cmuno") + "'" 
                  + "\n    and a.userid = '" +box.getSession("userid") + "'"
                  + "\n    and a.close_fg = '1' \n"; 

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                list.add( ls.getDataBox() );
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }


    /**
    * Ŀ�´�Ƽ ����Ÿȸ��������
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ ȸ������
    * @throws Exception
    */
    public ArrayList selectMemMasterSingleData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select     a.cmuno           cmuno         , a.userid          userid        "
                  + "          , a.kor_name        kor_name      , a.eng_name        eng_name      "
                  + "          , a.email           email         , a.tel             tel           "
                  + "          , a.mobile          mobile        , a.office_tel      office_tel    "
                  + "          , a.duty            duty          , a.wk_area         wk_area       "
                  + "          , a.grade           grade         , a.request_dte     request_dte   "
                  + "          , a.license_dte     license_dte   , a.license_userid  license_userid"
                  + "          , a.close_fg        close_fg      , a.close_reason    close_reason  "
                  + "          , a.close_dte       close_dte     , a.intro           intro         "
                  + "          , a.recent_dte      recent_dte    , a.visit_num       visit_num     "
                  + "          , a.search_num      search_num    , a.register_num    register_num  "
                  + "          , a.modifier_dte    modifier_dte  , c.kor_nm          grade_kor_nm"
                  + "          , a.fax             fax"
                  //+ "          ,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                  + "   from tz_cmuusermst a,tz_member b,tz_cmugrdcode c "
                  + "  where a.userid = b.userid "  
                  + "    and a.cmuno  = c.cmuno"
                  + "    and a.grade  = c.grcode"
                  + "    and a.cmuno  = '" +box.getString("p_cmuno") + "'" 
                  + "    and a.userid in (select userid from tz_cmuusermst where cmuno  = '" +box.getString("p_cmuno") + "' and grade='01')";
            
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                list.add( ls.getDataBox() );
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

    /**
    * ����Ŀ�´�Ƽ
    * @param box          receive from the form object and session
    * @return String   ����Ŀ�´�Ƽ ����Ʈ
    * @throws Exception
    */
    public String  selectMainIndex(String v_userid,String v_cmuno) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
//        ArrayList           list    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        String              ret     = "";
//        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();

            // ��������
            sql  = " select b.cmuno cmuno,b.cmu_nm cmu_nm"
                 + "   from tz_cmuusermst a,tz_cmubasemst b"
                 + "  where a.cmuno     = b.cmuno"  
                 + "    and a.userid    = '" +v_userid + "'" 
                 + "    and b.close_fg  ='1'"
                 + " order by b.cmu_nm asc";
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                if ( ls.getString("cmuno").equals(v_cmuno)) { 
                  ret += " <option value='" +ls.getString("cmuno") + "' selected > " +ls.getString("cmu_nm") + "</option > ";
                } else { 
                  ret += " <option value='" +ls.getString("cmuno") + "' > " +ls.getString("cmu_nm") + "</option > ";
                }
            }
            ls.close();

        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return ret;
    }

    /**
    * ��ü�˸�ȭ��
    * @param box          receive from the form object and session
    * @return String   ��ü�˸� ��� �޼���
    * @throws Exception
    */
    public int  sendMsNotice(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet             ls      = null;
        ListSet             ls1      = null;
//        ArrayList           list    = new ArrayList();

        String              sql     = "";
        String              sql1    = "";
        int                 isOk       = 1;
//        DataBox             dbox    = null;


        String v_cmuno         = box.getString("p_cmuno");
        String v_title         = box.getString("p_title");
        String v_tmp_userid    = box.getString("p_tmp_userid");
        String v_allselect     = box.getStringDefault("p_allselect","N");
        String v_intro         = StringManager.replace(box.getString("content"),"<br > ","\n");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        try { 
            connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 
            // �Ϸù�ȣ ���ϱ�
            int v_mailno=0;
            sql1 = "select isnull(max(MAILNO), 0)   from TZ_CMUMAIL ";
            ls = connMgr.executeQuery(sql1);
            while ( ls.next() ) v_mailno = ls.getInt(1);
            sql  =" insert into TZ_CMUMAIL ( mailno, userid, kor_nm, recv_email"
                 + "                       ,cmuno, cmu_nm, SEND_USERID,send_email, title, content"
                 + "                       ,loc_fg,loc_nm,regster_dte, send_fg)"
                 + "               values  (?,?,?,?"
                 + "                       ,?,?,?,?,?,?"
                 + "                       ,?,?,dbo.to_date(getdate(),'YYYYMMDDHH24MISS'),'N')"
                 ;
            pstmt = connMgr.prepareStatement(sql);


            if ( "Y".equals(v_allselect)) { // ��üȸ���ΰ��
              sql1 = "select userid,kor_name,email  from tz_cmuusermst where cmuno='" +v_cmuno + "'";
              ls = connMgr.executeQuery(sql1);
              while ( ls.next() ) { 

                   // Ŀ�´�Ƽ���ϱ�
                   String v_tmp_cmu_nm= "";
                   sql1 = "select cmu_nm   from tz_cmubasemst where cmuno = '" +v_cmuno + "' ";
                   
                   ls1 = connMgr.executeQuery(sql1);
                   while ( ls1.next() ) v_tmp_cmu_nm = ls1.getString(1);

                    // �߽��� �̸���
                    String v_tmp_send_email= "";
                    sql1 = "select email   from tz_member where userid = '" +s_userid + "' ";
                    ls1 = connMgr.executeQuery(sql1);
                    while ( ls1.next() ) v_tmp_send_email = ls1.getString(1);

                    v_mailno =v_mailno +1;
                    pstmt.setInt   (1, v_mailno                                );// �Ϸù�ȣ
                    pstmt.setString(2, ls.getString(1)                         );// �����ھ��̵�
                    pstmt.setString(3, ls.getString(2)                         );// �����ڸ�
                    pstmt.setString(4, ls.getString(3)                         );// �������̸���
                    pstmt.setString(5, v_cmuno                                 );// Ŀ�´�Ƽ��ȣ
                    pstmt.setString(6, v_tmp_cmu_nm                            );// Ŀ�´�Ƽ��
                    pstmt.setString(7 ,s_userid                                );// �߽��ھ��̵�
                    pstmt.setString(8 ,v_tmp_send_email                        );// �߽����̸���
                    pstmt.setString(9 , v_title                                );// ����
                    pstmt.setString(10, v_intro                                );
                    pstmt.setString(11, "2"                                    );// ����
                    pstmt.setString(12, "��ü�˶��޼���"                       );// ���и�

                    isOk = pstmt.executeUpdate();
                    
                    if ( pstmt != null ) { pstmt.close(); }
//                    sql1 = "select content  from TZ_CMUMAIL where mailno = '" +v_mailno + "'";
//                    connMgr.setOracleCLOB(sql1, v_intro);
                    if ( isOk > 0 ) { 
                        if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
                    }
               }

            }  else { 
               StringTokenizer stok    = new StringTokenizer(v_tmp_userid, "/");
               String[]        sTokens = new String[stok.countTokens()];
               for ( int i = 0; stok.hasMoreElements();i++ ) { 
                   sTokens[i] = ((String)stok.nextElement() ).trim();
               }
               for ( int j=0;j<sTokens.length;j++ ) { 

                   // �����ڸ��ϱ�
                   String v_recv_nm= "";
                   String v_recv_mail= "";
                   sql1 = "select kor_name,email   from tz_cmuusermst where userid = '" +sTokens[j] + "' ";
                   ls1 = connMgr.executeQuery(sql1);
                   while ( ls1.next() ) { 
                         v_recv_nm = ls1.getString(1); v_recv_mail = ls1.getString(2);
                   }


                   // Ŀ�´�Ƽ���ϱ�
                   String v_tmp_cmu_nm= "";
                   sql1 = "select cmu_nm   from tz_cmubasemst where cmuno = '" +v_cmuno + "' ";
                   
                   ls1 = connMgr.executeQuery(sql1);
                   while ( ls1.next() ) v_tmp_cmu_nm = ls1.getString(1);

                    // �߽��� �̸���
                    String v_tmp_send_email= "";
                    sql1 = "select email   from tz_member where userid = '" +s_userid + "' ";
                    ls1 = connMgr.executeQuery(sql1);
                    while ( ls1.next() ) v_tmp_send_email = ls1.getString(1);

                    v_mailno =v_mailno +1;
                    pstmt.setInt   (1, v_mailno                                );// �Ϸù�ȣ
                    pstmt.setString(2, sTokens[j]                              );// �����ھ��̵�
                    pstmt.setString(3, v_recv_nm                               );// �����ڸ�
                    pstmt.setString(4, v_recv_mail                             );// �������̸���
                    pstmt.setString(5, v_cmuno                                 );// Ŀ�´�Ƽ��ȣ
                    pstmt.setString(6, v_tmp_cmu_nm                            );// Ŀ�´�Ƽ��
                    pstmt.setString(7 ,s_userid                                );// �߽��ھ��̵�
                    pstmt.setString(8 ,v_tmp_send_email                        );// �߽����̸���
                    pstmt.setString(9 , v_title                                );// ����
                    pstmt.setString(10, v_intro                                );
                    pstmt.setString(11, "2"                                    );// ����
                    pstmt.setString(12, "��ü�˶��޼���"                       );// ���и�

                    isOk = pstmt.executeUpdate();
//                    sql1 = "select content  from TZ_CMUMAIL where mailno = '" +v_mailno + "'";
//                    connMgr.setOracleCLOB(sql1, v_intro);
                    if ( isOk > 0 ) { 
                        if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
                    }
               }


            }// end if

        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }



    /**
    * �Ϲݸ�������
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int sendMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";
        int         isOk        = 1;
//        int         v_seq       = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_static_cmuno");
        String v_parent_userid         = box.getString("p_parent_userid");
        String v_title         = box.getString("p_title");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           // �Ϸù�ȣ ���ϱ�
           int v_mailno=0;
           sql1 = "select isnull(max(MAILNO), 0)   from TZ_CMUMAIL ";
           ls = connMgr.executeQuery(sql1);
           while ( ls.next() ) v_mailno = ls.getInt(1);


             sql  =" insert into TZ_CMUMAIL ( mailno, userid, kor_nm, recv_email"
                  + "                       ,cmuno, cmu_nm, SEND_USERID,send_email, title, content"
                  + "                       ,loc_fg,loc_nm,regster_dte, send_fg)"
                  + "               values  (?,?,?,?"
                  + "                       ,?,?,?,?,?,?"
                  + "                       ,?,?,dbo.to_date(getdate(),'YYYYMMDDHH24MISS'),'N')"
                  ;
             pstmt = connMgr.prepareStatement(sql);
             // �����ڸ��ϱ�
             String v_tmp_nm= "";String v_tmp_recv_emial= "";
             sql1 = "select name,email   from tz_member where userid = '" +v_parent_userid + "' ";
             
             ls = connMgr.executeQuery(sql1);
             while ( ls.next() ) { 
                   v_tmp_nm = ls.getString(1);
                   v_tmp_recv_emial = ls.getString(2);

             }

             // Ŀ�´�Ƽ���ϱ�
             String v_tmp_cmu_nm= "";
             sql1 = "select cmu_nm   from tz_cmubasemst where cmuno = '" +v_cmuno + "' ";
             ls = connMgr.executeQuery(sql1);
             while ( ls.next() ) v_tmp_cmu_nm = ls.getString(1);


             // �߽��� �̸���
             String v_tmp_send_email= "";
             sql1 = "select email   from tz_member where userid = '" +s_userid + "' ";
             ls = connMgr.executeQuery(sql1);
             while ( ls.next() ) v_tmp_send_email = ls.getString(1);

             v_mailno =v_mailno +1;
             pstmt.setInt   (1, v_mailno                                );// �Ϸù�ȣ
             pstmt.setString(2, v_parent_userid                         );// �����ھ��̵�
             pstmt.setString(3, v_tmp_nm                                );// �����ڸ�
             pstmt.setString(4, v_tmp_recv_emial                        );// �������̸���
             pstmt.setString(5, v_cmuno                                 );// Ŀ�´�Ƽ��ȣ
             pstmt.setString(6, v_tmp_cmu_nm                            );// Ŀ�´�Ƽ��
             pstmt.setString(7 ,s_userid                                );// �߽��ھ��̵�
             pstmt.setString(8 ,v_tmp_send_email                        );// �߽����̸���
             pstmt.setString(9 , v_title                                );// ����
             pstmt.setString(10, v_intro                                );
             pstmt.setString(11, "4"                                    );// ����
             pstmt.setString(12, "�Ϲݸ���"                           );// ���и�
             isOk = pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
//             sql1 = "select content  from TZ_CMUMAIL where mailno = '" +v_mailno + "'";
//             connMgr.setOracleCLOB(sql1, v_intro);
             if ( isOk > 0 ) { 
                 if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
             }


        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
    
    /**
     * �ش���ѿ� ���������� ���� üũ
     * @param box
     * @param gadmin
     * @return
     * @throws Exception
     */
    public static boolean getCheckGadmin(RequestBox box, String gadmin) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int cnt = 0;
        boolean result = false;

        try { 
            connMgr = new DBConnectionManager();

            // ��������
            sql  = " select count(userid) cnt \n "
                 + "   from tz_manager    \n "
                 + "  where userid = "+StringManager.makeSQL(box.getSession("userid")) + " \n "
                 + "    and gadmin = "+StringManager.makeSQL(gadmin) + " \n  "; 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
            	cnt = ls.getInt("cnt");
            }
            ls.close();
        
	        if(cnt > 0){
	        	result = true;
	        }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }

}
