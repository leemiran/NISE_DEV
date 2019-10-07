// **********************************************************
//  1. ��      ��: ���� QNA DATA
//  2. ���α׷���: SubjQnaBean.java
//  3. ��      ��: ���� QNA bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: �迵�� 2003. 9. 7
//  7. ��      ��:
// **********************************************************
package com.ziaan.study;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.course.SubjGongData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeConfigBean;
import com.ziaan.system.MemberAdminBean;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SubjGongBean { 
    private ConfigSet config;
    private int row;
	private	static final String	FILE_TYPE =	"p_file";			// 		���Ͼ��ε�Ǵ� tag name
	private	static final int FILE_LIMIT	= 5;					// 	  �������� ���õ� ����÷�� ����


    
    public SubjGongBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    /**
    �������� ���� ȭ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   �������� ����  ����Ʈ
    */
    public ArrayList selectListGong(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        SubjGongData data = null;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_admin   = box.getString("p_admin");  // ��� ������

        String v_typesgubun = CodeConfigBean.getConfigValue("noticeCategory");    // ī�װ���������Ʈ

        String  v_orderColumn   = box.getString("p_orderColumn");               // ������ �÷���
        String  v_orderType     = box.getString("p_orderType");                 // ������ ����

        try { 
            connMgr = new DBConnectionManager();


            list = new ArrayList();

            sql  = "\n select tg.seq , tg.types, tg.addate, tg.title , tg.userid userid, tg.adcontent, tm.name, decode(tg.gadmin, 'ZZ', 'ZZ', 'P1', '����',  '���') gadmin, tg.isimport,tg.cnt ";
            sql += "\n   from tz_gong   tg ";
            sql += "\n      , Tz_member tm ";
            sql += "\n  where tg.subj    = " + StringManager.makeSQL(v_subj);
            sql += "\n    and tg.year    = " + StringManager.makeSQL(v_year);
            sql += "\n    and tg.subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "\n    and tg.userid = tm.userid ";
            sql += "\n    and tg.types != 'H' ";

            if ( v_orderColumn.equals("") ) { 
                sql += "\n  order by tg.addate desc ";
            } else { 
                sql += "\n  order by tg.addate desc, " + v_orderColumn + v_orderType;
            }
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new SubjGongData();

                data.setSeq( ls.getInt("seq") );
                data.setTypes( ls.getString("types") );
                data.setAddate( ls.getString("addate") );
                data.setTitle( ls.getString("title") );
                data.setUserid( ls.getString("userid") );
                data.setName( ls.getString("name") );
                data.setAdcontent( ls.getCharacterStream("adcontent") );
                data.setGadmin( ls.getString("gadmin") );
                data.setCnt( ls.getInt("cnt") );
                // data.setAdcontent( ls.getString("adcontent") );

                list.add(data);
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
    �������� ��ü ���� ȭ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   �������� ����  ����Ʈ
    */
    public ArrayList selectListGongAll_H(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        SubjGongData data = null;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");

        String v_typesgubun = CodeConfigBean.getConfigValue("noticeCategory");    // ī�װ���������Ʈ

        String  v_orderColumn   = box.getString("p_orderColumn");               // ������ �÷���
        String  v_orderType     = box.getString("p_orderType");                 // ������ ����

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select seq , types, typesnm, addate,title, userid, adcontent,  rownum   ";
            sql += " from   (select a.seq seq , a.types types, b.codenm typesnm, a.addate addate, ";
            sql += "          a.title title, a.userid userid, a.adcontent adcontent ,a.cnt        ";
            sql += "          from TZ_GONG a, TZ_CODE b                                          ";
            sql += "          where a.types   = b.code                                            ";
            sql += "            and b.gubun   = " + StringManager.makeSQL(v_typesgubun);
            sql += "            and b.levels  = 1 and a.types = 'H'                               ";
            sql += "            and a.subj    = " + StringManager.makeSQL(v_subj);
            sql += "            and a.year    = " + StringManager.makeSQL(v_year);
            sql += "            and a.subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "          order by a.addate desc ) ";
            //sql += " where rownum <3 ";


            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new SubjGongData();

                data.setSeq( ls.getInt("seq") );
                data.setTypes( ls.getString("types") );
                data.setTypesnm( ls.getString("typesnm") );
                data.setAddate( ls.getString("addate") );
                data.setTitle( ls.getString("title") );
                data.setUserid( ls.getString("userid") );
                data.setAdcontent( ls.getCharacterStream("adcontent") );
                data.setCnt( ls.getInt("cnt") );
                // data.setAdcontent( ls.getString("adcontent") );

                list.add(data);
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
     * �������� ���� �󼼺���
     * @param box          receive from the form object and session
     * @return data        SubjGongData ��������Ÿ��
     * @throws Exception
     */
    public SubjGongData selectViewGong(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        String sql1       = "";
        SubjGongData data = null;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        int    v_seq     = box.getInt("p_seq");
        String              v_upcnt 	= "Y";
        
        String v_typesgubun = CodeConfigBean.getConfigValue("noticeCategory");    // ī�װ���������Ʈ

        try { 
            connMgr = new DBConnectionManager();

            // sql  = " select a.seq seq , a.types types, b.codenm typesnm, a.addate addate, ";
            // sql += "        a.title title, a.userid userid, a.adcontent adcontent         ";
            // sql += "   from TZ_GONG a, TZ_CODE b                                          ";
            // sql += "  where a.types   = b.code                                            ";
            // sql += "    and b.gubun   = " + StringManager.makeSQL(v_typesgubun);
            // sql += "    and b.levels  = 1                                                 ";
            // sql += "    and a.subj    = " + StringManager.makeSQL(v_subj);
            // sql += "    and a.year    = " + StringManager.makeSQL(v_year);
            // sql += "    and a.subjseq = " + StringManager.makeSQL(v_subjseq);
            // sql += "    and a.seq     = " + v_seq;
            
			sql  = " select tg.seq , tg.types, tg.addate, tg.title, tg.userid userid, tg.adcontent, tm.name, decode(tg.gadmin, 'ZZ', 'ZZ', 'P1', '����',  '���') gadmin, isimport,upfile,realfile,cnt  ";
            sql += "   from TZ_GONG   tg                                          ";
            sql += "   ,    TZ_Member tm                                          ";
            sql += "  where tg.subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and tg.year    = " + StringManager.makeSQL(v_year);
            sql += "    and tg.subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "    and tg.seq     = " + v_seq;
            sql += "    and tg.userid  = tm.userid ";

//System.out.println("sql :::" + sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new SubjGongData();

                data.setSeq( ls.getInt("seq") );
                data.setTypes( ls.getString("types") );
                data.setAddate( ls.getString("addate") );
                data.setTitle( ls.getString("title") );
                data.setUserid( ls.getString("userid") );
                data.setAdcontent( ls.getCharacterStream("adcontent") );
                data.setName( ls.getCharacterStream("name") );
                data.setGadmin( ls.getCharacterStream("gadmin") );
                data.setIsimport ( ls.getString("isimport"));
                
                //÷������ ����
                data.setUpfile( ls.getString("upfile"));
                data.setRealfile( ls.getString("realfile"));
                
                // data.setAdcontent( ls.getString("adcontent") );
            }
            
            if ( !v_upcnt.equals("N") ) {  // ��ȸ�� ����
                
            	sql1 ="update TZ_GONG set cnt = cnt + 1 where subj = "+StringManager.makeSQL(v_subj)
						+" and year = "+StringManager.makeSQL(v_year)
						+" and subjseq = "+StringManager.makeSQL(v_subjseq)
						+" and seq = "+v_seq;
            	connMgr.executeUpdate(sql1);
           // System.out.println("cnt update :::::" + sql1);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;

    }
}
