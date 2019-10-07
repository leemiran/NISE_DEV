// **********************************************************
//  1. f      ��: QnA ��
//  2. �wα׷���: QnaAdminBean.java
//  3. ��      ��: QnA ��
//  4. ȯ      ��: JDK 1.3
//  5. ��      o: 1.0 QnA
//  6. ��      ��: �̿�d 2005. 7.  7
//  7. ��      d:
// **********************************************************
package com.ziaan.homepage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.PageList;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeData;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class QnaAdminBean { 
        private ConfigSet config;
    private int row;
    private String v_type = "PQ";
	private	static final String	FILE_TYPE =	"p_file";			// 		���Ͼ�ε�Ǵ� tag name
	private	static final int FILE_LIMIT	= 5;					// 	  ������ ���õ� ����÷�� ����

    public QnaAdminBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� ������� row �� �����Ѵ�
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    public QnaAdminBean(String type) { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� ������� row �� �����Ѵ�
            this.v_type = type;
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    * �ڷ�� ���̺��ȣ
    * @param box          receive from the form object and session
    * @return int         �ڷ�� ���̺��ȣ
    * @throws Exception
    */
    public int selectTableseq(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String              sql     = "";
         int result = 0;

         String v_type    = box.getStringDefault("p_type","");
         String v_grcode  = box.getStringDefault("p_grcode","0000000");
         String v_comp    = box.getStringDefault("p_comp","0000000000");
         String v_subj    = box.getStringDefault("p_subj","0000000000");
         String v_year    = box.getStringDefault("p_year","0000");
         String v_subjseq = box.getStringDefault("p_subjseq","0000");


         try { 
             connMgr = new DBConnectionManager();


             sql  = " select tabseq from TZ_BDS      ";
             sql += "  where type    = " + StringManager.makeSQL(v_type);
             sql += "    and grcode  = " + StringManager.makeSQL(v_grcode);
             sql += "    and comp    = " + StringManager.makeSQL(v_comp);
             sql += "    and subj    = " + StringManager.makeSQL(v_subj);
             sql += "    and year    = " + StringManager.makeSQL(v_year);
             sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
//System.out.println("sql���� -- > " +sql);
             ls = connMgr.executeQuery(sql);

             if ( ls.next() ) { 
                 result = ls.getInt("tabseq");
             }
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return result;
     }

    /**
     * QnA ī�װ? ���̺� ����Ʈ
     * @param name           ����Ʈ�ڽ���
     * @param selected       ���ð�
     * @param event          �̺�Ʈ��
     * @param allcheck       ��ü/��
     * @return
     * @throws Exception
*/
     public static String homepageGetQnaCategoryTable(String category_code) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ListSet ls1 = null;
        String              sql     = "";
        String sql1 = "";
        String result = "";
        int count =0;
        ResultSet rs1 = null;

        Statement stmt1 = null;
        CodeData data = null;
        int i = 1;

        result =  "\n   <table width='675' border='0' cellpadding='0' cellspacing='0' > ";
        try { 
            connMgr = new DBConnectionManager();

// Ȩ������ QnA�϶�
         sql1 = "select count(*) cnt from tz_code where gubun='0046' and levels='1' ";
            ls1 = connMgr.executeQuery(sql1);
            if ( ls1.next() ) { 
                count = ls1.getInt("cnt") ;
            }
             ls1.close();

            sql  = " select code, codenm from tz_code  where gubun  = '0046' and levels = '1'";
            sql += " order by code asc                        ";


            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new CodeData();
                data.setCode( ls.getString("code") );
                data.setCodenm( ls.getString("codenm") );

                    if ( i%4 == 1) { 
                      result +="\n <tr > ";
                    }
                      result +="\n     <td width='180' class='tbl_cfaq' > <input type='radio'  name='p_categorycd' value='" +data.getCode() + "'  onChange=changeAction() ";
                      if ( data.getCode().equals(category_code)) result +=" checked";
                      result +=" > " +data.getCodenm() + "</td > ";

                   if ( i%4 == 0  && i != (count)) { 
                      result +="\n  </tr > " ;

                    }

                if ( i != 4&&i%4 == 0) { 
                      result +="\n </tr > ";
                    }

                    i++;

            }
        int mod_cnt = (count)%5;

        if ( mod_cnt != 0 ) { 
            for ( int j=0; j<(5-mod_cnt); j++ ) { 
                result +="<td width='180' class='tbl_cfaq' > &nbsp;</td > ";
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

        result +=   "\n   </table > ";




        return result;

    }


    /**
    * QNAȭ�� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   QNA ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        String sql1 = "";
        DataBox             dbox    = null;

        String v_searchtext = box.getString("p_searchtext");
        String v_select = box.getString("p_select");
        String v_categorycd = box.getStringDefault("p_categorycd", "00");
        int    v_pageno     = box.getInt("p_pageno");
        String v_action     = box.getString("p_action");
        String v_startdate  = box.getString("p_startdate");
        String v_enddate    = box.getString("p_enddate");
        String ss_grcode    = box.getString("s_grcode");
        String v_replystate = box.getString("p_replystate");
        String v_order      = box.getString("p_order");
        String v_orderType     = box.getString("p_orderType");                 // d���� ��

        String v_type =  box.getStringDefault("p_type","PQ");// ��ü,Ȩ������,��ڿ���,������; ������ �� �ִ� Ű
        int v_tabseq = 0;
        int v_tablen = 0;
        String v_tabseqstr = "";

        try { 
            connMgr = new DBConnectionManager();


            // sql1 = "select tabseq from tz_bds where type =" + SQLString.Format(v_type);

            if ( !v_type.equals("ALL") ) { 
                sql1 = "select tabseq from tz_bds where type =" + SQLString.Format(v_type);
                ls = connMgr.executeQuery(sql1);

                if ( ls.next() ) { 
                  v_tabseq = ls.getInt(1);
                  v_tabseqstr += v_tabseq + ",";
                }
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

                v_tablen = v_tabseqstr.length()-1;
                v_tabseqstr = StringManager.substring(v_tabseqstr, 0, v_tablen);
            }

             list = new ArrayList();

            if ( v_type.equals("ALL") ) { 

                sql += "(select a.seq , a.types, a.title,  '' subj, '' year, '' scsubjnm,  a.grcode,a.indate, a.inuserid,a.upfile, a.isopen, a.luserid, a.ldate,'' subjseq,  a.categorycd,";
                sql += "      a.cnt,a.okyn1,a.okuserid1,a.okdate1,a.okyn2,a.okuserid2,a.okdate2,c.type, b.name,";
                sql += "      '' scsubj,'' subjseqgr,";
                sql += "       (select count(realfile) from tz_homefile where tabseq = a.tabseq and seq = a.seq and types = a.types) filecnt,  ";
                sql += "       (select count(*) from TZ_HOMEQNA where tabseq =a.tabseq and seq = a.seq and types > 0) replystate    ";
                sql += "from TZ_HOMEQNA a, tz_member b, tz_bds c  ";
                sql += "where  a.inuserid = b.userid( +) and a.tabseq = c.tabseq and a.types ='0'   ";
                sql += "     and c.type in('PQ','CU')     and a.tabseq in( 5,101 )  ";
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += "    and a.grcode=" + SQLString.Format(ss_grcode);
                }
                if ( !v_startdate.equals("") && !v_enddate.equals("") ) { 
                    sql += "  and a.indate between '" +v_startdate + "' and '" +v_enddate + "999999' ";
                } else if ( !v_startdate.equals("") && v_enddate.equals("") ) { 
                    sql += "  and a.indate > '" +v_startdate + "'";
                }

                if ( !v_searchtext.equals("") ) {      //    �˻� ��8��
                    v_pageno = 1;   //      �˻��� ��� ù��° ������ �ε�ȴ�

                    if ( v_select.equals("title") ) {     //    f��8�� �˻��Ҷ�
                        sql += " and a.title like " + StringManager.makeSQL("%" + v_searchtext + "%");
                    }
                    else if ( v_select.equals("content") ) {     //    ����8�� �˻��Ҷ�
                        sql += " and a.contents like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
                    }
                    else if ( v_select.equals("name") ) {     //    �̸�8�� �˻��Ҷ�
                        sql += " and b.name like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
                    }
                }

                sql +=" and a.categorycd = '" +v_categorycd + "'";

				sql += "     )";
				sql += "union all    ";
				sql += "(select a.seq, a.kind types, a.title , a.subj, a.year, c.scsubjnm, a.grcode, a.indate,a.inuserid, '' upfile,  a.isopen, a.luserid, a.ldate,  a.subjseq,   a.categorycd,";
				sql += "       0 cnt,a.okyn1,a.okuserid1,a.okdate1,a.okyn2,a.okuserid2,a.okdate2, '' type, b.name,";
				sql += "       c.scsubj,c.subjseqgr,   0 filecnt,                ";
				sql += "        (select count(*) from TZ_qna where subj=a.subj and subjseq=a.subjseq and year=a.year and seq = a.seq and kind > 0) replystate  ";
				sql += "from tz_qna a, tz_member b, vz_scsubjseq c      ";
				sql += "where a.inuserid = b.userid( +) and a.subj = c.subj and a.year=c.year and a.subjseq = c.subjseq and a.kind ='0'";
				if ( !ss_grcode.equals("ALL") ) { 
					  sql += " and a.grcode= " + SQLString.Format(ss_grcode);
				}

				if ( !v_searchtext.equals("") ) {      //    �˻� ��8��
					v_pageno = 1;   //      �˻��� ��� ù��° ������ �ε�ȴ�
				   if ( v_select.equals("title") ) {     //    f��8�� �˻��Ҷ�
						sql += " and a.title like " + StringManager.makeSQL("%" + v_searchtext + "%");
					}
					else if ( v_select.equals("content") ) {     //    ����8�� �˻��Ҷ�
						sql += " and a.contents like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
					}
					else if ( v_select.equals("name") ) {     //    �̸�8�� �˻��Ҷ�
						sql += " and b.name like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
					}
				}
				sql +=" and a.categorycd = '" +v_categorycd + "'";
				sql += ")   ";
				if ( v_order.equals("type"))      v_order ="type";
				if ( v_order.equals("title"))     v_order ="title";
				if ( v_order.equals("name"))      v_order ="name";
				if ( v_order.equals("indate"))    v_order ="indate";
				if ( v_order.equals("upfile"))    v_order ="upfile";
				if ( v_order.equals("replystate"))v_order ="replystate";
				if ( v_order.equals("okuserid1")) v_order ="okuserid1";
				if ( v_order.equals("okdate1"))   v_order ="okdate1";
				if ( v_order.equals("okuserid2")) v_order ="okuserid2";
				if ( v_order.equals("okdate2"))   v_order ="okdate2";
				if ( v_order.equals("")||v_order.equals("name") ) { 
					sql += " order by replystate,indate desc, seq desc,types asc ";
				} else { 
					sql += " order by " + v_order + v_orderType;
				}


			} else { 
                sql += " select a.seq , a.types, a.title, a.contents, a.indate, a.inuserid, ";
                sql += "        a.upfile, a.isopen, a.luserid, a.ldate, b.name,a.cnt, a.categorycd,a.okyn1,a.okuserid1,a.okdate1,a.okyn2,a.okuserid2,a.okdate2,c.type,";
                sql += "        (select count(realfile) from tz_homefile where tabseq = a.tabseq and seq = a.seq  and types = a.types) filecnt,";
                sql += "        (select count(*) from TZ_HOMEQNA where tabseq =a.tabseq and seq = a.seq and types > 0) replystate ";
                sql += "   from TZ_HOMEQNA a, tz_member b, tz_bds c";
                sql += "  where a.inuserid = b.userid( +) and a.tabseq = c.tabseq and a.types ='0' ";

				if ( v_type.equals("ALL") ) { 
					sql += "    and c.type in('PQ','CU') ";
				} else { 
					sql += "    and c.type = '" +v_type + "' ";
				}
				if ( !ss_grcode.equals("ALL") ) { 
					sql += "    and a.grcode=" + SQLString.Format(ss_grcode);
				}
				sql += "    and a.tabseq in( " + v_tabseqstr + " )" ;
				if ( !v_startdate.equals("") && !v_enddate.equals("") ) { 
					sql += "  and a.indate between '" +v_startdate + "' and '" +v_enddate + "999999' ";
				} else if ( !v_startdate.equals("") && v_enddate.equals("") ) { 
					sql += "  and a.indate > '" +v_startdate + "'";
				}

                if ( !v_searchtext.equals("") ) {      //    �˻� ��8��
					v_pageno = 1;   //      �˻��� ��� ù��° ������ �ε�ȴ�
					if ( v_select.equals("title") ) {     //    f��8�� �˻��Ҷ�
						sql += " and a.title like " + StringManager.makeSQL("%" + v_searchtext + "%");
					}
					else if ( v_select.equals("content") ) {     //    ����8�� �˻��Ҷ�
						sql += " and a.contents like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
					}
					else if ( v_select.equals("name") ) {     //    �̸�8�� �˻��Ҷ�
						sql += " and b.name like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
					}
				}
               sql +=" and a.categorycd = '" +v_categorycd + "'";

				if ( v_order.equals("type"))      v_order ="type";
				if ( v_order.equals("title"))     v_order ="title";
				if ( v_order.equals("name"))      v_order ="name";
				if ( v_order.equals("indate"))    v_order ="indate";
				if ( v_order.equals("upfile"))    v_order ="upfile";
				if ( v_order.equals("replystate"))v_order ="replystate";
				if ( v_order.equals("okuserid1")) v_order ="okuserid1";
				if ( v_order.equals("okdate1"))   v_order ="okdate1";
				if ( v_order.equals("okuserid2")) v_order ="okuserid2";
				if ( v_order.equals("okdate2"))   v_order ="okdate2";
				if ( v_order.equals("")||v_order.equals("name") ) { 
					sql += " order by replystate,a.indate desc, a.seq desc, a.types asc ";
				} else { 
					sql += " order by " + v_order + v_orderType ;
				}

			}
			//System.out.println(sql);

           ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);             //  ������� row ���� �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     �����������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ �� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row �� ��ȯ�Ѵ�

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

    /**
    * QNA ������ ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   QNA ����Ʈ
    * @throws Exception
**/
    public ArrayList selectListQnaCourse(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ListSet ls1 = null;
        ArrayList           list    = null;
        ArrayList list1 = null;
        String              sql     = "";
        String sql1 = "";
        DataBox             dbox    = null;


        String  v_scsubj= "", v_scyear= "", v_scsubjseq= "";

        String v_type =  box.getStringDefault("p_type","PQ");
        String p_grcode     = box.getString("s_grcode");
        String p_gyear      = box.getStringDefault("s_gyear","ALL");
        String p_grseq      = box.getStringDefault("s_grseq","ALL");


       // String p_mastercd   = box.getString("s_mastercd");
        String ss_uclass    = box.getStringDefault("s_upperclass","ALL");    // ���з�
        String ss_mclass    = box.getStringDefault("s_middleclass","ALL");    // ���з�
        String ss_lclass    = box.getStringDefault("s_lowerclass","ALL");    // ���з�
        String ss_subjcourse = box.getString("s_subjcourse");
        String ss_subjseq    = box.getString("s_subjseq");
        String  ss_company  = box.getString("s_company");


        String v_searchtext = box.getString("p_searchtext");
        String v_select = box.getString("p_select");
        String v_categorycd = box.getStringDefault("p_categorycd", "00");
        int v_pageno        = box.getInt("p_pageno");
        String v_action     = box.getString("p_action");
        String v_startdate  = box.getString("p_startdate");
        String v_enddate    = box.getString("p_enddate");
    //    String ss_grcode    = box.getString("s_grcode");
        String v_replystate = box.getString("p_replystate");
        String v_order      = box.getString("p_order");
        String v_orderType     = box.getString("p_orderType");                 // d���� ��


        try { 
            connMgr = new DBConnectionManager();


            list = new ArrayList();

            sql  += "  select a.title , a.grcode,b.name, a.isopen, a.luserid,a.subjseq, a.ldate,a.subj,a.year,a.seq,c.SCSUBJNM,   ";
            sql  += "           a.categorycd,a.okyn1,a.okuserid1,a.okdate1,a.okyn2,a.okuserid2,a.okdate2, a.indate,";
            sql += "            b.name,c.grcode,c.scsubj,c.subjseqgr,";
            sql += "            (select count(*) from TZ_qna where seq = a.seq and kind > 0) replystate ";
            sql += "    from tz_qna a, tz_member b, vz_scsubjseq c";
            sql += "    where a.inuserid = b.userid( +) and a.subj = c.subj and a.year=c.year and a.subjseq = c.subjseq  and a.kind ='0' ";
//System.out.println("p_grcode::::" +p_grcode + " ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == ");
              if ( !p_grcode.equals("ALL") ) { 
                    sql += " and a.grcode= " + SQLString.Format(p_grcode);
              }

              if ( !p_grseq.equals("ALL")) sql += "    and c.grseq="  + SQLString.Format(p_grseq);

              if ( !p_gyear.equals("ALL") ) { 
                    sql += " and a.year = " + SQLString.Format(p_gyear);
              }

              if ( !ss_uclass.equals("ALL") ) { 
                    sql1 += " and scupperclass = '" + ss_uclass + "'";
                }
                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1 += " and scsubj = '" + ss_subjcourse + "'";
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1 += " and scsubjseq = '" + ss_subjseq + "'";
                }

            if ( !v_searchtext.equals("") ) {      //    �˻� ��8��
                v_pageno = 1;   //      �˻��� ��� ù��° ������ �ε�ȴ�

               if ( v_select.equals("title") ) {     //    f��8�� �˻��Ҷ�
                    sql += " and a.title like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_select.equals("content") ) {     //    ����8�� �˻��Ҷ�
                    sql += " and a.contents like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
                }
                else if ( v_select.equals("name") ) {     //    �̸�8�� �˻��Ҷ�
                    sql += " and b.name like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
                }


            }

               sql +=" and a.categorycd = '" +v_categorycd + "'";

				if ( v_order.equals("scsubjnm"))   v_order ="c.scsubjnm";
				if ( v_order.equals("subjseqgr"))    v_order ="c.subjseqgr";
				if ( v_order.equals("title"))   v_order ="a.title";
				if ( v_order.equals("name"))   v_order ="b.name";
				if ( v_order.equals("indate"))    v_order ="a.indate";
				if ( v_order.equals("replystate"))   v_order ="replystate";
				if ( v_order.equals("okuserid1"))   v_order ="a.okuserid1";
				if ( v_order.equals("okdate1"))    v_order ="a.okdate1";
				if ( v_order.equals("okuserid2"))   v_order ="a.okuserid2";
				if ( v_order.equals("okdate2"))    v_order ="a.okdate2";
				if ( v_order.equals("") ) { 
					sql += " order by a.indate desc, a.seq desc ";
				} else { 
					sql += " order by " + v_order + v_orderType;
				}


//System.out.println("������ ����Ʈ ==  ==  ==  ==  == =" +sql);
           ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);             //  ������� row ���� �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     �����������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ �� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row �� ��ȯ�Ѵ�

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




    /*�Խ��� ��ȣ�ޱ�*/
     public static String printPageList(int totalPage, int currPage, int blockSize) throws Exception { 

        currPage = (currPage == 0) ? 1 : currPage;
        String str= "";
        if ( totalPage > 0 ) { 
            PageList pagelist = new PageList(totalPage,currPage,blockSize);


            str += "<table border='0' width='100%' align='center' > ";
            str += "<tr > ";
            str += "    <td width='100%' align='center' valign='middle' > ";

            if ( pagelist.previous() ) { 
                str += "<a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\" > <img src=\"/images/board/prev.gif\" border=\"0\" align=\"middle\" > </a > &nbsp;&nbsp;";
            } else { 
                str += "<img src=\"/images/board/prev.gif\" border=\"0\" align=\"middle\" > ";
            }

            for ( int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++ ) { 
                if ( i == currPage) { 
                    str += "<B > " + i + "</B > " + "&nbsp;";
                } else { 
                    str += "<a href=\"javascript:goPage('" + i + "')\" > " + i + "</a > &nbsp;";
                }
            }

            if ( pagelist.next() ) { 
                str += "<a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\" > &nbsp;<img src=\"/images/board/next.gif\"  border=\"0\" align=\"middle\" > </a > ";
            } else { 
                str += "<img src=\"/images/board/next.gif\" border=\"0\" align=\"middle\" > ";
            }

            if ( str.equals("") ) { 
                str += "�ڷᰡ ��4ϴ�.";
            }

            str += "    </td > ";
            str += "    <td width='15%' align='center' > ";



            str += "    </td > ";
            str += "</tr > ";
            str += "</table > ";
        }

        return str;
    }


    /**
   * ���õ� �ڷ�� �Խù� �󼼳��� select
   */
   public DataBox selectQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_seq = box.getInt("p_seq");

        String v_types = box.getString("p_types");

        String v_fileseq = box.getString("p_fileseq");
        String vv_type = box.getString("pp_type");
        String v_type  = box.getString("p_type");

		int	v_upfilecnt	= (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector  = new Vector();

		int	[] fileseq = new int [v_upfilecnt];

        try { 
            connMgr = new DBConnectionManager();
            // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
if ( !"".equals(vv_type)) { 
            sql = "select tabseq from tz_bds where type = " + SQLString.Format(vv_type);
} else { 
            sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);

}
//System.out.println(" ==  ==  ==  ==  ==  ==  == " +sql);
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_tabseq = ls.getInt(1);
            ls.close();
            // ------------------------------------------------------------------------------------
            sql = "select a.types, a.seq, a.inuserid, a.title, a.contents, a.categorycd,a.grcode, b.fileseq,b.realfile, b.savefile, a.indate ,c.name,d.type, ";
            sql += " okyn1, okuserid1, okyn2, okuserid2, okdate1, okdate2, ";
            sql += " (select grcodenm from tz_grcode where grcode = a.grcode) grcodenm, ";
            sql += " (select codenm from tz_code  where gubun  = '0046' and levels = '1' and code = a.categorycd) categorynm";
            sql += " from tz_homeqna a, tz_homefile b, tz_member c,tz_bds d";
            sql += " where  \n";
            sql += " a.tabseq = b.tabseq( +) \n";
            sql += " and a.tabseq = d.tabseq( +) \n";
            sql += " and a.seq = b.seq( +)       \n";
            sql += " and a.types = b.types( +)   \n";
            sql += " and a.inuserid = c.userid( +)\n";
            // sql += " and a.upfile = b.savefile( +) \n";
            sql += " and a.tabseq = " + v_tabseq + " and a.seq = " +v_seq + " and a.types = " + v_types ;

        //System.out.println("�󼼺��� ==  ==  == >>  >>  >>  >>  > " +sql);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            // -------------------   2003.12.25  ����     -------------------------------------------------------------------
                dbox = ls.getDataBox();
                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
				fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }

            sql = "update tz_homeqna set cnt = cnt + 1 where tabseq = " + v_tabseq + " and seq = " +v_seq + " and types = " + v_types;
            connMgr.executeUpdate(sql);
//System.out.println(sql);

			if ( realfileVector 	 != null ) dbox.put("d_realfile", realfileVector);
			if ( savefileVector 	 != null ) dbox.put("d_savefile", savefileVector);
			if ( fileseqVector 	 != null ) dbox.put("d_fileseq", fileseqVector);


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
   * ���õ� �ڷ�� �Խù� �󼼳��� select
   */
   public DataBox selectRepQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_repseq = box.getInt("p_repseq");
        int v_seq 	 = box.getInt("p_seq");
        String v_reptypes = box.getString("p_reptypes");
        String v_type  = box.getString("p_type");
        String v_types  = box.getString("p_types");

		int	v_upfilecnt	= (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector  = new Vector();

		int	[] fileseq = new int [v_upfilecnt];

        try { 
            connMgr = new DBConnectionManager();
            // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
            sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
//System.out.println(" ==  ==  ==  ==  ==  ==  == " +sql);
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_tabseq = ls.getInt(1);
            ls.close();
            // ------------------------------------------------------------------------------------
            sql  ="select a.types, a.seq, a.inuserid, a.title, a.contents, a.categorycd,a.grcode, b.fileseq,b.realfile, b.savefile, a.indate ,c.name,d.type, ";
            sql +=" okyn1, okuserid1, okyn2, okuserid2, okdate1, okdate2, ";
            sql +=" (select grcodenm from tz_grcode where grcode = a.grcode) grcodenm, ";
            sql +=" (select codenm from tz_code  where gubun  = '0046' and levels = '1' and code = a.categorycd) categorynm";
            sql +=" from tz_homeqna a, tz_homefile b, tz_member c,tz_bds d";
            sql +=" where  \n";
            sql +=" a.tabseq = b.tabseq( +) \n";
            sql +=" and a.tabseq = d.tabseq( +) \n";
            sql +=" and a.seq = b.seq( +)       \n";
            sql +=" and a.types = b.types( +)   \n";
            sql +=" and a.inuserid = c.userid( +)\n";
            // sql += " and a.upfile = b.savefile( +) \n";
            sql +=" and a.tabseq = " + v_tabseq + " and a.seq = " +v_repseq + " and a.types = " + v_reptypes ;

            //System.out.println("�󼼺��� ==  ==  == >>  >>  >>  >>  > " +sql);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            // -------------------   2003.12.25  ����     -------------------------------------------------------------------
                dbox = ls.getDataBox();
                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
				fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }

            // sql = "update tz_homeqna set cnt = cnt + 1 where tabseq = " + v_tabseq + " and seq = " +v_seq + " and types = " + v_types;
            // connMgr.executeUpdate(sql);
            // System.out.println(sql);

			if ( realfileVector 	 != null ) dbox.put("d_realfile", realfileVector);
			if ( savefileVector 	 != null ) dbox.put("d_savefile", savefileVector);
			if ( fileseqVector 	 != null ) dbox.put("d_fileseq", fileseqVector);


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
   * ���õ� �ڷ�� �Խù� �󼼳��� select
   */
   public DataBox selectRepCourseQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_repseq = box.getInt("p_repseq");
        int v_seq 	 = box.getInt("p_seq");

        String v_subj = box.getString("p_subj");
        String v_subjseq = box.getString("p_subjseq");
        String v_year = box.getString("p_year");

        String v_repkind = box.getString("p_repkind");
        String v_kind    = box.getString("p_kind");

		int	v_upfilecnt	= (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector  = new Vector();

		int	[] fileseq = new int [v_upfilecnt];

        try { 
            connMgr = new DBConnectionManager();

            sql += " select                                                       \n";
            sql += "   a.subj,   a.subjseq,   a.year,   a.seq,                    \n";
            sql += "   a.kind,  a.title,   a.categorycd,  a.grcode, a.contents,   \n";
            sql += "   a.indate ,  b.fileseq,  b.realfile,   b.savefile,          \n";
            sql += "   get_name(a.inuserid) name,   okyn1,   okuserid1,   okyn2,  \n";
            sql += "   okuserid2,   okdate1,   okdate2, \n";
            sql += "   (select count(*)  from tz_qnafile x where x.subj = a.subj and x.subjseq = a.subjseq and x.year = a.year and x.seq = a.seq and x.kind = a.kind ) upfilecnt, \n";
            sql += "   (select grcodenm from tz_grcode where grcode = a.grcode) grcodenm, \n";
            sql += "   (select codenm from tz_code  where gubun  = '0046' and levels = '1' and code = a.categorycd) categorynm\n";
            sql += " from \n";
            sql += "   tz_qna a, tz_qnafile b\n";
            sql += " where  \n";
            sql += "   a.subj = b.subj( +) \n";
            sql += "   and a.year = b.year( +)\n";
            sql += "   and a.subjseq = b.subjseq( +)\n";
            sql += "   and a.seq     = b.seq( +)\n";
            sql += "   and a.kind    = b.kind( +)\n";
            sql += "   and a.subj    = '" +v_subj + "'\n";
            sql += "   and a.subjseq = '" +v_subjseq + "'\n";
            sql += "   and a.year    = '" +v_year + "'\n";
            sql += "   and a.seq     =  " +v_repseq + "\n";
            sql += "   and a.kind    =  " +v_repkind + "\n";
            // System.out.println("�󼼺��� ==  ==  == >>  >>  >>  >>  > " +sql);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            // -------------------   2003.12.25  ����     -------------------------------------------------------------------
                dbox = ls.getDataBox();
                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
				fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }

            // sql = "update tz_homeqna set cnt = cnt + 1 where tabseq = " + v_tabseq + " and seq = " +v_seq + " and types = " + v_types;
            // connMgr.executeUpdate(sql);
            // System.out.println(sql);
			if ( realfileVector 	 != null ) dbox.put("d_realfile", realfileVector);
			if ( savefileVector 	 != null ) dbox.put("d_savefile", savefileVector);
			if ( fileseqVector 	 != null ) dbox.put("d_fileseq", fileseqVector);
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
   * ���õ� QnA �������� �󼼳��� select
   */
   public DataBox selectQnaCourse(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int    v_seq  = box.getInt("p_seq");
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_type    = box.getString("p_type");
        String v_categorycd = box.getString("p_categorycd");

        Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector  = new Vector();


        try { 
            connMgr = new DBConnectionManager();


        // sql  = "select  a.subj,a.year,a.subjseq,a.lesson,a.seq,a.kind,a.title,a.grcode, b.scsubjnm,b.subjseqgr,a.inuserid, a.title, a.contents, a.categorycd, a.indate ,c.name, ";
        // sql += " okyn1, okuserid1, okyn2, okuserid2, okdate1, okdate2, ";
        // sql += " (select grcodenm from tz_grcode where grcode = a.grcode) grcodenm,  ";
        // sql += " (select codenm from tz_code  where gubun  = '0046' and levels = '1' and code = a.categorycd) categorynm  ";
        // sql += " from tz_qna a, vz_scsubjseq b, tz_member c   ";
        // sql += " where a.subj = b.subj and a.inuserid = c.userid( +) and a.year=b.year and a.subjseq = b.subjseq and a.kind=0 ";
        // sql += "       and a.seq = " + v_seq + " and a.subj= '" + v_subj + "' and a.year = '" +v_year + "' and a.subjseq = '" + v_subjseq + "'  ";
        // if ( !v_categorycd.equals("") ) { 
        //   sql +=" and categorycd = '" +v_categorycd + "'";
        // }

        sql += " select                                                                                                     \n";
        sql += "   a.subj, a.year, a.subjseq,  a.lesson,                                                                    \n";
        sql += "   a.seq,  a.kind,  a.title,  a.contents, a.grcode,                                                         \n";
        sql += "   b.scsubjnm,  b.subjseqgr,  a.inuserid,                                                                   \n";
        sql += "   a.title,   a.categorycd,  a.indate ,  a.togubun, get_name(a.inuserid) name,                                         \n";
        sql += "   okyn1,   okuserid1,  okyn2,  okuserid2,  okdate1,  okdate2,                                              \n";
        sql += "   (select grcodenm from tz_grcode where grcode = a.grcode) grcodenm,                                       \n";
        sql += "   (select codenm from tz_code  where gubun  = '0046' and levels = '1' and code = a.categorycd) categorynm, \n";
        sql += "   c.fileseq,c.realfile, c.savefile                                                                         \n";
        sql += " from                                                                                                       \n";
        sql += "   tz_qna a, tz_qnafile c , vz_scsubjseq b                                                                  \n";
        sql += " where                                                                                                      \n";
        sql += "   a.subj        = b.subj                                                                                   \n";
        sql += "   and a.year    = b.year                                                                                   \n";
        sql += "   and a.subjseq = b.subjseq                                                                                \n";
        sql += "   and a.subj    = c.subj( +)                                                                                \n";
        sql += "   and a.year    = c.year( +)                                                                                \n";
        sql += "   and a.subjseq = c.subjseq( +)                                                                             \n";
        sql += "   and a.lesson  = c.lesson( +)                                                                              \n";
        sql += "   and a.seq     = c.seq( +)                                                                                 \n";
        sql += "   and a.kind    = c.kind( +)                                                                                \n";
        sql += "   and a.kind=0                                                                                             \n";
        sql += "   and a.seq = " +v_seq + "                                                                                    \n";
        sql += "   and a.subj= '" +v_subj + "'                                                                                 \n";
        sql += "   and a.year = '" +v_year + "'                                                                                \n";
        sql += "   and a.subjseq = '" +v_subjseq + "'                                                                          \n";
        if ( !v_categorycd.equals("") ) { 
          // sql += "   and categorycd = '" +v_categorycd + "'                                                                    \n";
        }
        //System.out.println("�󼼺��� ==  ==  == >>  >>  >>  >>  > " +sql);

        ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            // -------------------   2003.12.25  ����     -------------------------------------------------------------------
                dbox = ls.getDataBox();
                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
				fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }

			if ( realfileVector 	 != null ) dbox.put("d_realfile", realfileVector);
			if ( savefileVector 	 != null ) dbox.put("d_savefile", savefileVector);
			if ( fileseqVector 	 != null ) dbox.put("d_fileseq", fileseqVector);




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
    * QNA ��ȭ�鿡�� ��d�Ͽ� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int viewupdate(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;
        String sql   = "";
        String sql1  = "";
        String sql2  = "";
        String sql3  = "";
        ListSet ls   = null;
        ListSet ls1   = null;
        int isOk     = 0;
        int isOk1     = 0;

		String s_grcode     = box.getString("p_grcode");
        int v_seq           = box.getInt("p_seq");
        String v_type       = box.getString("p_type");
        String v_types      = box.getString("p_types");
        String v_categorycd = box.getString("p_categorycd");
        String v_okyn1      = box.getString("p_okyn1");
        String v_okyn2      = box.getString("p_okyn2");

        String v_approval1  = box.getString("p_approval1");
        String v_approval2  = box.getString("p_approval2");

//       String v_isopen  = "Y";
        String s_userid   = "";
        String s_usernm   = box.getSession("name");
        String s_gadmin   = box.getSession("gadmin");

     //   if ( s_gadmin.equals("A1") ) { 
     //       s_userid = "���";
     //   } else { 
            s_userid = box.getSession("userid");
    //    }
//System.out.println("111111111111111111111111111111");
        try { 
            connMgr = new DBConnectionManager();
            // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
                sql1 = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
//System.out.println("22222222222222222222222222222222222222" +sql1);
                ls = connMgr.executeQuery(sql1);
                ls.next();
                int v_tabseq = ls.getInt(1);
                ls.close();
            // ----------------�з� ��d�Ͽ� ����Ʈ�Ѵ�.---------------


                sql  = " update TZ_HOMEQNA set ";
                if ( v_okyn1.equals("Y") && v_approval1.equals("N") ) { 
                  sql += " okyn1 = '" +v_okyn1 + "' , \n";
                  sql += " okuserid1 = '" +s_usernm + "',  \n";
                  sql += " okdate1 = to_char(sysdate, 'YYYYMMDDHH24MISS'), \n";
                }

                if ( v_okyn2.equals("Y") && v_approval1.equals("Y") && v_approval2.equals("N") ) { 
                  sql += " okyn2 = '" +v_okyn2 + "' , \n";
                  sql += " okuserid2 = '" +s_usernm + "',   \n";
                  sql += " okdate2 = to_char(sysdate, 'YYYYMMDDHH24MISS'),\n";
                }

                sql += "  categorycd = ?   \n";
                sql += "  where tabseq = ? and seq = ? and types = ? \n";
//System.out.println("33333333333333333333333333333333333333" +sql);
                pstmt = connMgr.prepareStatement(sql);

                pstmt.setString(1,  v_categorycd);
                pstmt.setInt(2,  v_tabseq);
                pstmt.setInt(3,  v_seq);
                pstmt.setString(4,  v_types);

                isOk = pstmt.executeUpdate();


            // ----------------------   �Խ����� �亯�ۼ� üũ�Ѵ�.----------------------------

                sql3 = "select count(*) replyCnt from tz_homeqna where tabseq = " + v_tabseq + " and seq= " + v_seq + " and types > 0";
//System.out.println("�亯�ۼ�üũ::::" +sql3);
                ls1 = connMgr.executeQuery(sql3);
                ls1.next();
                int v_replyCnt = ls1.getInt(1);
                ls1.close();
//System.out.println("sssss:::" +v_replyCnt);
            // ----------------�з� �ٲ�� �亯�۵� ����Ʈ�Ѵ�.---------------


            if ( v_replyCnt > 0) { 
                 sql2  = " update TZ_HOMEQNA set ";
                 sql2 += " categorycd = '" +v_categorycd + "' ";
                 sql2 += " where tabseq =? and seq = ? and types > 0 ";
//System.out.println("�亯�۾���::::" +sql2);




                pstmt1 = connMgr.prepareStatement(sql2);

                pstmt1.setInt(1,  v_tabseq);
                pstmt1.setInt(2,  v_seq);

                isOk1 = pstmt1.executeUpdate();
//System.out.println("isOk1::::::::::" +isOk1);
            }

       } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


  /**
    * QNA ������ ��ȭ�鿡�� ��d�Ͽ� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int viewCourseupdate(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;
        String sql   = "";
        String sql1  = "";
        String sql2  = "";
        String sql3  = "";
        ListSet ls   = null;
        ListSet ls1   = null;
        int isOk     = 1;
        int isOk1     = 1;


		String s_grcode = box.getString("p_grcode");
        int    v_seq  = box.getInt("p_seq");
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_type    = box.getString("p_type");
        String v_categorycd = box.getString("p_categorycd");

        String v_okyn1      = box.getString("p_okyn1");
        String v_okyn2      = box.getString("p_okyn2");
        String v_approval1  = box.getString("p_approval1");
        String v_approval2  = box.getString("p_approval2");

//       String v_isopen  = "Y";
        String s_userid   = "";
        String s_usernm   = box.getSession("name");
        String s_gadmin   = box.getSession("gadmin");

        if ( s_gadmin.equals("A1") ) { 
            s_userid = "���";
        } else { 
            s_userid = box.getSession("userid");
        }
//System.out.println("111111111111111111111111111111");
        try { 
            connMgr = new DBConnectionManager();
            // ----------------�з� ��d�Ͽ� ����Ʈ�Ѵ�.---------------


                sql  = " update TZ_qna set ";
                if ( v_okyn1.equals("Y") && v_approval1.equals("N") ) { 
                  sql += " okyn1 = '" +v_okyn1 + "' , \n";
                  sql += " okuserid1 = '" +s_usernm + "',  \n";
                  sql += " okdate1 = to_char(sysdate, 'YYYYMMDDHH24MISS'), \n";
                }

                if ( v_okyn2.equals("Y") && v_approval1.equals("Y") && v_approval2.equals("N") ) { 
                  sql += " okyn2 = '" +v_okyn2 + "' , \n";
                  sql += " okuserid2 = '" +s_usernm + "',   \n";
                  sql += " okdate2 = to_char(sysdate, 'YYYYMMDDHH24MISS'),\n";
                }

                sql += "  categorycd = ?   \n";
                sql += "  where seq = ? and subj = ? and year = ? and subjseq = ? \n";
                pstmt = connMgr.prepareStatement(sql);

                pstmt.setString(1,  v_categorycd);
                pstmt.setInt(2,  v_seq);
                pstmt.setString(3,  v_subj);
                pstmt.setString(4,  v_year);
                pstmt.setString(5,  v_subjseq);
                isOk = pstmt.executeUpdate();
                //System.out.println("course_qna == = >>  > " +sql);
                //System.out.println("isOk == = >>  > " +isOk);
            // ----------------------   �Խ����� �亯�ۼ� üũ�Ѵ�. ----------------------------

                sql3 = "select count(*) replyCnt from tz_qna where seq = " + v_seq + " and subj = '" +v_subj + "' and year = '" + v_year + "' and subjseq = '" +v_subjseq + "' and kind > 0";
                //System.out.println("�亯�ۼ�üũ::::" +sql3);
                ls1 = connMgr.executeQuery(sql3);
                ls1.next();
                int v_replyCnt = ls1.getInt(1);
                ls1.close();
                //System.out.println("sssss:::" +v_replyCnt);
            // ----------------�з� �ٲ�� �亯�۵� ����Ʈ�Ѵ�.---------------

            if ( v_replyCnt > 0) { 
                 sql2  = " update TZ_qna set ";
                 sql2 += " categorycd = '" +v_categorycd + "' ";
                 sql2 += " where seq = " + v_seq + " and subj = '" +v_subj + "' and year = '" + v_year + "' and subjseq = '" +v_subjseq + "' and kind > 0" ;
                 //System.out.println("�亯�۾���::::" +sql2);

                 pstmt1 = connMgr.prepareStatement(sql2);


                isOk1 = pstmt1.executeUpdate();
            }
            isOk = isOk *isOk1;
            //System.out.println("isOk1111111111111111111 == = >>  > " +isOk);
            //System.out.println("isOk2222222222222222222 ==  == >>  > " +isOk1);
       } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }



    /**
    * QNA ����Ҷ�(�亯)
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
     */
	 public int insertQnaAns(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        int isOk  = 0;
        int isOk2  = 0;
        int v_cnt = 0;
		String s_grcode = box.getString("p_grcode");
        int    v_seq     = box.getInt("p_seq");
        String v_types   = "";
        String v_type =  box.getStringDefault("p_type","PQ");// ��ü,Ȩ������,��ڿ���,������; ������ �� �ִ� Ű
        String v_title   = box.getString("p_title");
        String v_contents   = box.getString("contents");
        String v_isopen  = "Y";
        String s_userid = "";
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");
        String v_categorycd   = box.getString("p_categorycd");

//System.out.println("v_contents::::" +v_contents);
//        if ( s_gadmin.equals("A1") ) { 
//            s_userid = "���";
//        } else { 
            s_userid = box.getSession("userid");
//        }
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);
           // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
                sql1 = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
                ls = connMgr.executeQuery(sql1);
                ls.next();
                int v_tabseq = ls.getInt(1);
                ls.close();

            // ------------------------------------------------------------------------------------
           sql  = " select max(to_number(types)) from TZ_HOMEQNA  ";
           sql += "  where tabseq = " + v_tabseq + " and seq = " + v_seq;
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_types = String.valueOf(( ls.getInt(1) + 1));
           } else { 
                v_types = "1";
           }

           String v_namoseq = String.valueOf(v_seq) + v_types;


           /*********************************************************************************************/
		   // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ��ε�)
/*           
		   ConfigSet conf = new ConfigSet();
		   SmeNamoMime namo = new SmeNamoMime(v_contents); // ��ü��
		   boolean result = namo.parse(); // ��f �Ľ� ����

		   if ( !result ) { // �Ľ� ���н�
		   	System.out.println( namo.getDebugMsg() ); // ���� �޽��� ���
		   	System.out.println( "1111111" ); // ���� �޽��� ���
		   	return 0;
		   }

		   if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ�
		   	String v_server = conf.getProperty("autoever.url.value");
		   	String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ��d
		   	String refUrl = conf.getProperty("url.namo");; // %���� ����� ����; b���ϱ� '�� ���
		   	String prefix =  "QnaAdmin" + v_namoseq;         // ���ϸ� b�ξ�
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // ��f ���� ����
		   }

		   if ( !result ) { // �������� ���н�
		   	System.out.println( namo.getDebugMsg() ); // ���� �޽��� ���
		   	System.out.println( "222222" ); // ���� �޽��� ���
		   	return 0;
		   }

		   v_contents = namo.getContent(); // ��~ ����Ʈ ���
*/           
		   /*********************************************************************************************/



            sql2 =  "insert into TZ_HOMEQNA(tabseq, seq, types, title, contents, indate, inuserid, isopen, luserid, ldate, cnt, grcode,categorycd ";
            if ( v_tabseq == 101)
            { 
            	sql2 += " ,retmailing ";
            }  ///dksljdfjkldsjfkl

            sql2 +=  ")                ";
            sql2 += " values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?,? ";
//            sql2 += " values (?, ?, ?, ?, empty_clob(), to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?,? ";
            if ( v_tabseq == 101) { 
            	  sql2 += " ,'N' ";
            }
            sql2 += " ) ";

            pstmt = connMgr.prepareStatement(sql2);
            pstmt.setInt(1, v_tabseq);
            pstmt.setInt(2, v_seq);
            pstmt.setString(3, v_types);
            pstmt.setString(4, v_title);
            pstmt.setString(5, v_contents);
            pstmt.setString(6,  s_userid);
            pstmt.setString(7,  v_isopen);
            pstmt.setString(8, s_userid);
            pstmt.setInt(9, v_cnt);
            pstmt.setString(10, s_grcode);
            pstmt.setString(11,v_categorycd);

isOk = pstmt.executeUpdate();

            sql2 = "select contents from TZ_HOMEQNA";
            sql2 += "  where tabseq    = " + v_tabseq ;
            sql2 += "    and seq    = " + v_seq;
            sql2 += "    and types = " + StringManager.makeSQL(v_types);

//            connMgr.setOracleCLOB(sql2, v_contents);       //      (ORACLE 9i ����)

//            if ( isOk > 0) connMgr.commit();
//           else connMgr.rollback();


		     isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq,v_types, box);       //      ����÷���ߴٸ� ����table��  insert

            // System.out.println(v_contents);
//System.out.println("isOk1::::" +isOk);
//System.out.println("isOk2::::" +isOk2);
//System.out.println("tabseq::::" +v_tabseq);
//System.out.println("seq::::" +v_seq);
//System.out.println("types::::" +v_types);
//System.out.println("title::::" +v_title);
//System.out.println("userid::::" +s_userid);
//System.out.println("isopen::::" +v_isopen);
//System.out.println("cnt::::" +v_cnt);
//System.out.println("grcode::::" +s_grcode);
//System.out.println("categorycd::::" +v_categorycd);
            if ( isOk > 0 && isOk2 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
				isOk = 1;
            } else { 
			    connMgr.rollback();
				isOk = 0;
			}


        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }


        return isOk;
    }

    /**
    * QNA ������ ����Ҷ�(�亯)
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertQnaCourseAns(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        int isOk  = 0;
        int isOk1 = 0;
		String s_grcode = box.getString("p_grcode");

        int    v_seq     = box.getInt("p_seq");
        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_type    = box.getString("p_type");
        String v_title   = box.getString("p_title");
        String v_contents   = box.getString("contents");
       // String v_contents =  StringManager.replace(box.getString("content"),"<br > ","\n");
        String v_isopen  = "Y";
        String s_userid = "";
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");
        String v_categorycd   = box.getString("p_categorycd");
        String v_kind         = "";
        String v_lesson       = box.getString("p_lesson");
//System.out.println("v_contents::::" +v_contents);
//        if ( s_gadmin.equals("A1") ) { 
//            s_userid = "���";
//        } else { 
            s_userid = box.getSession("userid");
//        }
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);
           sql  = " select max(to_number(kind)) from tz_qna  ";
           sql += "  where seq = " + v_seq + " and subj = '" +v_subj + "' and year = '" + v_year + "' and subjseq = '" + v_subjseq + "'" ;

           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_kind = String.valueOf(( ls.getInt(1) + 1));
           } else { 
                v_kind = "1";
           }

           String v_namoseq = v_subj +v_subjseq +v_year +String.valueOf(v_seq) + v_kind;
           /*********************************************************************************************/
		   // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ��ε�)
/*           
		   ConfigSet conf = new ConfigSet();
		   SmeNamoMime namo = new SmeNamoMime(v_contents); // ��ü��
		   boolean result = namo.parse(); // ��f �Ľ� ����

		   if ( !result ) { // �Ľ� ���н�
		   	System.out.println( namo.getDebugMsg() ); // ���� �޽��� ���
		   	System.out.println( "1111111" ); // ���� �޽��� ���
		   	return 0;
		   }

		   if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ�
		   	String v_server = conf.getProperty("autoever.url.value");
		   	String fPath    = conf.getProperty("dir.namo");   // ���� ���� ��� ��d
		   	String refUrl   = conf.getProperty("url.namo");; // %���� ����� ����; b���ϱ� '�� ���
		   	String prefix   =  "QnaCourseAdmin" + v_namoseq;         // ���ϸ� b�ξ�
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // ��f ���� ����
		   }

		   if ( !result ) { // �������� ���н�
		   	System.out.println( namo.getDebugMsg() ); // ���� �޽��� ���
		   	System.out.println( "222222" ); // ���� �޽��� ���
		   	return 0;
		   }
		   v_contents = namo.getContent(); // ��~ ����Ʈ ���
*/           
		   /*********************************************************************************************/


            sql2 =  "insert into TZ_QNA(subj,year,subjseq,lesson, seq, kind, title, contents, indate, inuserid, isopen, luserid, ldate, grcode,categorycd)                ";
            sql2 += "                values (?, ?, ?, ?,?,?,?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?) ";
//            sql2 += "                values (?, ?, ?, ?,?,?,?, empty_clob(), to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?) ";

            pstmt = connMgr.prepareStatement(sql2);
            pstmt.setString(1,  v_subj);
            pstmt.setString(2,  v_year);
            pstmt.setString(3,  v_subjseq);
            pstmt.setString(4, v_lesson);
            pstmt.setInt(5, v_seq);
            pstmt.setString(6, v_kind);
            pstmt.setString(7, v_title);
            pstmt.setString(8,  v_contents);
            pstmt.setString(9,  s_userid);
            pstmt.setString(10,  v_isopen);
            pstmt.setString(11, s_userid);
            pstmt.setString(12, s_grcode);
            pstmt.setString(13,v_categorycd);

            isOk = pstmt.executeUpdate();

            sql2 = "select contents from TZ_qna";
            sql2 += "  where seq = " + v_seq + " and subj = '" +v_subj + "' and year = '" + v_year + "' and subjseq = '" +v_subjseq + "' and kind= '" + v_kind + "' " ;

//            connMgr.setOracleCLOB(sql2, v_contents);       //      (ORACLE 9i ����)

            isOk1 = insertCourseUpFile(connMgr, v_subj, v_year, v_subjseq, v_seq, v_kind, box);

            if ( isOk > 0 && isOk1 > 0) connMgr.commit();
           else connMgr.rollback();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }


        return isOk;
    }

    /**
    * QNA �亯 ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   QNA ����Ʈ
    * @throws Exception
    */
    public ArrayList selectQnaListA(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ListSet ls1 = null;
        ArrayList           list    = null;
        String              sql     = "";
        String sql1 = "";
        DataBox             dbox    = null;

       String v_categorycd = box.getStringDefault("p_categorycd", "00");
        int v_pageno        = box.getInt("p_pageno");
        int v_seq = box.getInt("p_seq");
        // int v_tabseq = box.getInt("p_tabseq");
        String vv_type = box.getString("pp_type");
        String v_type = box.getString("p_type");

        String v_fileseq = box.getString("p_fileseq");
        String v_gadmin		= box.getSession("gadmin");
				int    v_upfilecnt	= (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

				Vector realfileVector = new Vector();
				Vector savefileVector = new Vector();
				Vector fileseqVector  = new Vector();


		int	[] fileseq = new int [v_upfilecnt];


        try { 
            connMgr = new DBConnectionManager();
            // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
if ( !"".equals(vv_type)) { 
                sql1 = "select tabseq from tz_bds where type = " + SQLString.Format(vv_type);
} else { 
                sql1 = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);

                //System.out.println("v_seq ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == = > " +v_seq);
}
                ls = connMgr.executeQuery(sql1);
                ls.next();
                int v_tabseq = ls.getInt(1);
                ls.close();
//                System.out.println("v_tabseq ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == > " +v_tabseq);
//                System.out.println("v_categorycd ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == > " +v_categorycd);
            // ------------------------------------------------------------------------------------
            list = new ArrayList();
            // seq, types, title, contents, indate, inuserid, upfile, isopen, luserid, ldate
            sql += " select a.tabseq, a.seq , a.types, a.title, a.contents, a.indate, a.inuserid, ";
            sql += "        a.upfile, a.isopen, a.luserid, a.ldate, b.name,a.cnt, a.categorycd, a.okyn1, a.okyn2 ";
            sql += "   from TZ_HOMEQNA a, tz_member b ";
            sql += "  where   ";
            sql += "  a.inuserid = b.userid( +)";
            sql += "  and   a.tabseq   = " + v_tabseq;
            sql += "  and   a.seq      = " + v_seq;
            sql += "  and   a.types != '0'" ;

            if ( !v_categorycd.equals("") ) { 
               sql +=" and a.categorycd = '" +v_categorycd + "'";
            }

            sql += " order by seq desc, types asc ";
//System.out.println("sql ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == = > " +sql);
           ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
				list.add(dbox);

				String types = ls.getString("types");
				//System.out.println("types:::" +types);
 // ���⼭ tz_homefile select �� ���Ͱ�8�� ��d


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
    * QNA ������ �亯 ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   QNA ����Ʈ
    * @throws Exception
    */
    public ArrayList selectQnaCourseListA(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        String sql1 = "";
        DataBox             dbox    = null;

        int    v_seq  = box.getInt("p_seq");
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_type    = box.getString("p_type");
        String v_categorycd = box.getString("p_categorycd");

        // String v_categorycd = box.getStringDefault("p_categorycd", "00");
        int v_pageno        = box.getInt("p_pageno");
        // int v_seq = box.getInt("p_seq");
        // int v_tabseq = box.getInt("p_tabseq");
        // String vv_type = box.getString("pp_type");
        // String v_type = box.getString("p_type");
        try { 
            connMgr = new DBConnectionManager();

            // ------------------------------------------------------------------------------------
            list = new ArrayList();
            // seq, types, title, contents, indate, inuserid, upfile, isopen, luserid, ldate

            sql  = "select  a.subj,a.year,a.subjseq,a.lesson,a.seq,a.kind,a.title, b.scsubjnm,b.subjseqgr,a.inuserid, a.title, a.contents, a.categorycd, a.indate ,c.name, ";
            sql += " okyn1, okuserid1, okyn2, okuserid2, okdate1, okdate2, ";
            sql += " (select grcodenm from tz_grcode where grcode = a.grcode) grcodenm,  ";
            sql += " (select codenm from tz_code  where gubun  = '0046' and levels = '1' and code = a.categorycd) categorynm  ";
            sql += " from tz_qna a, vz_scsubjseq b, tz_member c   ";
            sql += " where a.subj = b.subj and a.inuserid = c.userid( +) and a.year=b.year and a.subjseq = b.subjseq and a.kind > 0 ";
            sql += "       and a.seq = " + v_seq + " and a.subj= '" + v_subj + "' and a.year = '" +v_year + "' and a.subjseq = '" + v_subjseq + "'  ";



            if ( !v_categorycd.equals("") ) { 
               sql +=" and categorycd = '" +v_categorycd + "'";
            }

            sql += " order by seq desc ";

//System.out.println("��ȭ�鿡�� ��۳��� Ȯ���ϱ�:::::::::::::::::::::" +sql);
           ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);             //  ������� row ���� �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     �����������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ �� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row �� ��ȯ�Ѵ�

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


    /**
    * QNA ������ ��f�Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteCourseQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        String              sql     = "";
        String sql1 = "";
        int isOk1 = 1;
        int isOk2 = 1;

        int    v_seq  = box.getInt("p_seq");
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_type    = box.getString("p_type");
        String v_categorycd = box.getString("p_categorycd");

        try { 
            connMgr = new DBConnectionManager();

            // ���f�� �亯���û�f
                sql1  = " delete from TZ_qna    ";
                sql1 += "  where seq = " + v_seq + " and subj = '" +v_subj + "' and year= '" + v_year + "' and subjseq = '" + v_subjseq + "' ";
                pstmt1 = connMgr.prepareStatement(sql1);
                isOk1 = pstmt1.executeUpdate();
                isOk2 = this.deleteCourseUpFile(connMgr, box);


        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
    }
    
    
 
    /**
    * QNA ������ �亯 ��f�Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteRepCourseQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        
        int    v_repseq  = box.getInt("p_repseq");
        String v_repkind = box.getString("p_repkind");


        String v_categorycd = box.getString("p_categorycd");

        try { 
            connMgr = new DBConnectionManager();

            // ���f�� �亯���û�f
                sql1  = " delete from TZ_qna    ";
                sql1 += "  where ";
                sql1 += "  seq = " + v_repseq ;
                sql1 += "  and subj = '" +v_subj + "' ";
                sql1 += "  and year= '" + v_year + "'   ";
                sql1 += "  and subjseq = '" + v_subjseq + "' ";
                sql1 += "  and kind    = '" + v_repkind + "' ";
                
                pstmt1 = connMgr.prepareStatement(sql1);
                isOk1 = pstmt1.executeUpdate();
                
                // ���ϻ�f
                sql2 = "delete from tz_qnafile \n";
			    sql2 += " where                \n";
			    sql2 += " subj        = ?      \n";
			    sql2 += " and year    = ?      \n";
			    sql2 += " and subjseq = ?      \n";
			    sql2 += " and seq     = ?      \n";
			    sql2 += " and kind    = ?      \n";
                
			    pstmt2 = connMgr.prepareStatement(sql2);
                
			    pstmt2.setString(1, v_subj);
			    pstmt2.setString(2, v_year);
                pstmt2.setString(3, v_subjseq);
                pstmt2.setInt   (4, v_repseq);
                pstmt2.setString(5, v_repkind);
			    isOk2 =	pstmt2.executeUpdate();

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
    }


    /**
    * QNA ��f�Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;

        String v_type    = box.getString("p_type");

        int    v_seq     = box.getInt("p_seq");
        String v_types   = box.getString("p_types");

        int    v_repseq     = box.getInt("p_repseq");
        String v_reptypes   = box.getString("p_reptypes");

        Vector savefile  = box.getVector("p_savefile");
        String v_savemotion = box.getString("p_savemotion");


        try { 
            connMgr = new DBConnectionManager();
            // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
            sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);

            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_tabseq = ls.getInt(1);
            ls.close();
            // ------------------------------------------------------------------------------------
            if ( v_types.equals("0") ) {                 // ���f�� �亯���û�f
                sql1  = " delete from TZ_HOMEQNA    ";
                sql1 += "  where tabseq = ? and seq = ?";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
            } else { 
                sql1  = " delete from TZ_HOMEQNA";
                sql1 += "  where tabseq = ? and seq = ? and types = ?  ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_repseq);
                pstmt1.setString(3, v_reptypes);
           }

            isOk1 = pstmt1.executeUpdate();

            sql3  = " delete from TZ_COMMENTQNA    ";
            sql3 += "  where tabseq = ? and seq = ? and types = ?  ";
            pstmt2 = connMgr.prepareStatement(sql3);
            if ( v_types.equals("0") ) {                 // ���f�� �亯���û�f
              pstmt2.setInt(1, v_tabseq);
              pstmt2.setInt(2, v_seq);
              pstmt2.setString(3, v_types);
            } else { 
              pstmt2.setInt(1, v_tabseq);
              pstmt2.setInt(2, v_repseq);
              pstmt2.setString(3, v_reptypes);
            }

            isOk3 = pstmt2.executeUpdate();

            for ( int i = 0; i < savefile.size() ;i++ ) { 
                String str = (String)savefile.elementAt(i);
                if ( !str.equals("") ) { 
                    // isOk2 = this.deleteUpFile(connMgr, box);
                }
            }

            if ( isOk1 > 0 && isOk2 > 0 ) { 
                if ( savefile != null ) { 
                    FileManager.deleteFile(savefile);         //     ÷������ ��f
                }
                if ( v_savemotion != null ) { 
                    FileManager.deleteFile(v_savemotion);
                }

                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1 * isOk2;
    }


    /**
    * QNA ��f�Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteRepQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;

        String v_type    = box.getString("p_type");

        int    v_seq     = box.getInt("p_seq");
        String v_types   = box.getString("p_types");

        int    v_repseq     = box.getInt("p_repseq");
        String v_reptypes   = box.getString("p_reptypes");

        Vector savefile  = box.getVector("p_savefile");
        String v_savemotion = box.getString("p_savemotion");


        try { 
            connMgr = new DBConnectionManager();
            // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
            sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);

            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_tabseq = ls.getInt(1);
            ls.close();
            // ------------------------------------------------------------------------------------

            sql1  = " delete from TZ_HOMEQNA ";
            sql1 += " where tabseq = ? and seq = ? and types = ?  ";
            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);
            pstmt1.setInt(2, v_repseq);
            pstmt1.setString(3, v_reptypes);

            isOk1 = pstmt1.executeUpdate();

            sql3  = " delete from TZ_COMMENTQNA    ";
            sql3 += "  where tabseq = ? and seq = ? and types = ?  ";
            pstmt2 = connMgr.prepareStatement(sql3);

            pstmt2.setInt(1, v_tabseq);
            pstmt2.setInt(2, v_repseq);
            pstmt2.setString(3, v_reptypes);
            isOk3 = pstmt2.executeUpdate();

            if ( savefile.size() > 0 ) { 
              isOk2 = this.deleteRepUpFile(connMgr, box );
            }

            if ( isOk1 > 0 && isOk2 > 0 ) { 
                if ( savefile != null ) { 
                    FileManager.deleteFile(savefile);         //     ÷������ ��f
                }
                if ( v_savemotion != null ) { 
                    FileManager.deleteFile(v_savemotion);
                }
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1 * isOk2;
    }

    /**
    * QNA ��ο� �ڷ����� ���
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
	 public	int	insertUpFile(DBConnectionManager connMgr, int p_tabseq, int p_seq, String types, RequestBox	box) throws	Exception { 
		ListSet	ls = null;
		PreparedStatement pstmt2 = null;
		String              sql     = "";
		String sql2	= "";
		int	isOk2 =	0;
		int isOk = 1;

		// ----------------------   ��ε�Ǵ� ������ ���;	�˰� �ڵ��ؾ��Ѵ�  --------------------------------
//System.out.println("tabseq:::" +p_tabseq);
//System.out.println("p_seq:::" +p_seq);
//System.out.println("types:::" +types);
		String [] v_realFileName = new String [FILE_LIMIT];
		String [] v_newFileName	= new String [FILE_LIMIT];

		for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 

			v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
			v_newFileName [i] =	box.getNewFileName(FILE_TYPE + (i +1));
//System.out.println("v_realFileName [i]:::" +v_realFileName [i]);
		}
		// ----------------------------------------------------------------------------------------------------------------------------

		String s_userid	= box.getSession("userid");

		try	{ 
			 // ----------------------	�ڷ� ��ȣ ��n�´� ----------------------------
			sql	= "select nvl(max(fileseq),	0) from	tz_homefile	where tabseq = " +p_tabseq + " and seq = " +	p_seq + "and types = " + types ;

//System.out.println("fileinsert::::::::::::" +sql);

			ls = connMgr.executeQuery(sql);
			ls.next();
			int	v_fileseq =	ls.getInt(1) + 1;
//System.out.println("fileseq::::" +v_fileseq);
			ls.close();
			// ------------------------------------------------------------------------------------

			//// //// //// //// //// //// //// //// // 	 ���� table	�� �Է�	 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql2 =	"insert	into tz_homefile(tabseq, seq, fileseq, types, realfile, savefile, luserid,	ldate)";
			sql2 +=	" values (?, ?, ?, ?, ?, ?,?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

//System.out.println("sql2::::::" +sql2);

			pstmt2 = connMgr.prepareStatement(sql2);

			for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
//System.out.println("realfile:::::::::::::" + v_realFileName[i]);
				if ( 	!v_realFileName	[i].equals(""))	{ 		// 		��f ��ε�	�Ǵ� ���ϸ�	üũ�ؼ� db�� �Է��Ѵ�
					pstmt2.setInt(1, p_tabseq);
					pstmt2.setInt(2, p_seq);
					pstmt2.setInt(3, v_fileseq);
					pstmt2.setString(4, types);
					pstmt2.setString(5,	v_realFileName[i]);
					pstmt2.setString(6,	v_newFileName[i]);
					pstmt2.setString(7,	s_userid);
					isOk2 =	pstmt2.executeUpdate();

					isOk = isOk * isOk2;
					v_fileseq++;
//					System.out.println("p_tabseq:::" +p_tabseq);
//					System.out.println("p_seq:::" +p_seq);
//System.out.println("v_fileseq:::" +v_fileseq);
				}

			}
		}
		catch ( Exception ex ) { 
			FileManager.deleteFile(v_newFileName, FILE_LIMIT);		// 	�Ϲ�����, ÷������ ��8�� ��f..
			ErrorManager.getErrorStackTrace(ex,	box, sql2);
			throw new Exception("sql = " + sql2	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
			if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
		}
		return isOk;
	}

    /**
    * QNA Upload Filelist
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */

	public ArrayList fileList(int v_tabseq, int  v_seq, String types) throws	Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1 = null;
        ArrayList list = new ArrayList();
        String sql1 = "";
        DataBox             dbox    = null;

        try { 
        connMgr = new DBConnectionManager();
          sql1 = "select fileseq,realfile, savefile";
			sql1 += " from tz_homefile ";
			sql1 += " where tabseq = " +v_tabseq + "  and seq = " + v_seq + " and types =  '" +types + "'"  ;
			//System.out.println(sql1);
			ls1=connMgr.executeQuery(sql1);
		    while ( ls1.next() ) { 
		  dbox =ls1.getDataBox();
	          list.add(dbox);
		}
	}

	catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, null, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
}

    /**
    * QNA Upload Filelist
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
     */
    public ArrayList fileCourseList(String v_subj,  String v_year, String v_subjseq , int v_repseq, String v_repkind) throws	Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1 = null;
        ArrayList list = new ArrayList();
        String sql1 = "";
        DataBox             dbox    = null;

        try { 

          connMgr = new DBConnectionManager();
          sql1 = "select fileseq,realfile, savefile";
		  sql1 += " from tz_qnafile                        ";
		  sql1 += " where                                  ";
		  sql1 += "   subj = '" +v_subj + "'                ";
		  sql1 += "   and year = '" + v_year + "'           ";
		  sql1 += "   and subjseq  =  '" +v_subjseq + "'    ";
		  sql1 += "   and seq = " +v_repseq;
		  sql1 += "   and kind = '" +v_repkind + "' ";
		  //System.out.println(sql1);
		  ls1=connMgr.executeQuery(sql1);

		  while ( ls1.next() ) { 
		    dbox =ls1.getDataBox();
	        list.add(dbox);
		  }
	}

	catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, null, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
}



	/**
    * QNA �亯��d
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
     */
	 public int viewReplayUpdate(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        int isOk  = 0;
        int isOk2  = 0;
        int v_cnt = 0;
		String s_grcode     = box.getString("p_grcode");

        int    v_repseq     = box.getInt("p_repseq");
        String v_reptypes   = box.getString("p_reptypes");
        String v_type       = box.getStringDefault("p_type","PQ");// ��ü,Ȩ������,��ڿ���,������; ������ �� �ִ� Ű
        String v_title      = box.getString("p_title");
        String v_contents   = box.getString("p_contents");
        String v_isopen     = "Y";
        String s_userid     = "";
        String s_usernm     = box.getSession("name");
        String s_gadmin     = box.getSession("gadmin");
        String v_categorycd = box.getString("p_categorycd");

        //System.out.println("grcode::::" +s_grcode);

//        if ( s_gadmin.equals("A1") ) { 
//          s_userid = "���";
//        } else { 
            s_userid = box.getSession("userid");
//        }
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);
           // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
                sql1 = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
                ls = connMgr.executeQuery(sql1);
                ls.next();
                int v_tabseq = ls.getInt(1);
                ls.close();


           String v_namoseq = String.valueOf(v_repseq) + v_reptypes;

           /*********************************************************************************************/
		   // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ��ε�)
/*           
		   ConfigSet conf = new ConfigSet();
		   SmeNamoMime namo = new SmeNamoMime(v_contents); // ��ü��
		   boolean result = namo.parse(); // ��f �Ľ� ����

		   if ( !result ) { // �Ľ� ���н�
		   	System.out.println( namo.getDebugMsg() ); // ���� �޽��� ���
		   	return 0;
		   }

		   if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ�
		   	String v_server = conf.getProperty("autoever.url.value");
		   	String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ��d
		   	String refUrl = conf.getProperty("url.namo");; // %���� ����� ����; b���ϱ� '�� ���
		   	String prefix =  "QnaAdmin" + v_namoseq;         // ���ϸ� b�ξ�
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // ��f ���� ����
		   }

		   if ( !result ) { // �������� ���н�
		   	System.out.println( namo.getDebugMsg() ); // ���� �޽��� ���
		   	return 0;
		   }

		   v_contents = namo.getContent(); // ��~ ����Ʈ ���
*/           
		   /*********************************************************************************************/



            // sql2 =  "insert into TZ_HOMEQNA(tabseq, seq, types, title, contents, indate, inuserid, isopen, luserid, ldate, cnt, grcode,categorycd)                ";
            // sql2 += " values (?, ?, ?, ?, empty_clob(), to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?,?) ";

            sql2 = " update tz_homeqna set     ";
            sql2 += "   title = ?,              ";
            sql2 += "   contents = ?,";
//            sql2 += "   contents = empty_clob(),";
            sql2 += "   luserid = ?,            ";
            sql2 += "   ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')               ";
            sql2 += " where                     ";
            sql2 += "   tabseq = ?              ";
            sql2 += "   and seq = ?             ";
            sql2 += "   and types = ?           ";



            pstmt = connMgr.prepareStatement(sql2);
            pstmt.setString(1, v_title    );
            pstmt.setString(2, v_contents    );
            pstmt.setString(3, s_userid   );
            pstmt.setInt   (4, v_tabseq   );
            pstmt.setInt   (5, v_repseq   );
            pstmt.setString(6, v_reptypes );
            isOk = pstmt.executeUpdate();

            sql2 = "select contents from TZ_HOMEQNA";
            sql2 += "  where tabseq    = " + v_tabseq ;
            sql2 += "    and seq    = " + v_repseq;
            sql2 += "    and types = " + StringManager.makeSQL(v_reptypes);
//            connMgr.setOracleCLOB(sql2, v_contents);       //      (ORACLE 9i ����)

//            if ( isOk > 0) connMgr.commit();
//           else connMgr.rollback();

             isOk2 = this.deleteRepUpFile(connMgr, box);
		     isOk2 = this.insertUpFile(connMgr, v_tabseq, v_repseq, v_reptypes, box);       //      ����÷���ߴٸ� ����table��  insert

            // System.out.println(v_contents);
            // System.out.println("isOk1::::" +isOk);
            // System.out.println("isOk2::::" +isOk2);
            // System.out.println("tabseq::::" +v_tabseq);
            // System.out.println("seq::::" +v_seq);
            // System.out.println("types::::" +v_types);
            // System.out.println("title::::" +v_title);
            // System.out.println("userid::::" +s_userid);
            // System.out.println("isopen::::" +v_isopen);
            // System.out.println("cnt::::" +v_cnt);
            // System.out.println("grcode::::" +s_grcode);
            // System.out.println("categorycd::::" +v_categorycd);


            if ( isOk > 0 && isOk2 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
				isOk = 1;
            } else { 
			    connMgr.rollback();
				isOk = 0;
			}
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }


        return isOk;
    }



	    /**
    * QNA �亯��d
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
     */
	 public int viewReplayCourseUpdate(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt3 = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk  = 0;
        int isOk2  = 1;
        int isOk3  = 1;
        int v_cnt = 0;
		String s_grcode     = box.getString("p_grcode");

        String v_subj      = box.getString("p_subj");
        String v_subjseq   = box.getString("p_subjseq");
        String v_year      = box.getString("p_year");
        int    v_repseq     = box.getInt("p_repseq");
        String v_repkind    = box.getString("p_repkind");

        String v_title      = box.getString("p_title");
        String v_contents   = box.getString("p_contents");
        String v_isopen     = "Y";
        String s_userid     = "";
        String s_usernm     = box.getSession("name");
        String s_gadmin     = box.getSession("gadmin");
        String v_categorycd = box.getString("p_categorycd");
        
        int	v_upfilecnt       = box.getInt("p_upfilecnt");	// 	���� ������ִ� ���ϼ�
		Vector v_savefile     =	new	Vector();
		Vector v_filesequence =	new	Vector();

        //System.out.println("grcode::::" +s_grcode);

        s_userid = box.getSession("userid");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);

           String v_namoseq = String.valueOf(v_repseq) + v_repkind;

           /*********************************************************************************************/
		   // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ��ε�)
/*           
		   ConfigSet conf = new ConfigSet();
		   SmeNamoMime namo = new SmeNamoMime(v_contents); // ��ü��
		   boolean result = namo.parse(); // ��f �Ľ� ����

		   if ( !result ) { // �Ľ� ���н�
		   	System.out.println( namo.getDebugMsg() ); // ���� �޽��� ���
		   	return 0;
		   }

		   if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ�
		   	String v_server = conf.getProperty("autoever.url.value");
		   	String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ��d
		   	String refUrl = conf.getProperty("url.namo");; // %���� ����� ����; b���ϱ� '�� ���
		   	String prefix =  "QnaCourseAdmin" + v_namoseq;         // ���ϸ� b�ξ�
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // ��f ���� ����
		   }

		   if ( !result ) { // �������� ���н�
		   	System.out.println( namo.getDebugMsg() ); // ���� �޽��� ���
		   	return 0;
		   }

		   v_contents = namo.getContent(); // ��~ ����Ʈ ���
*/           
		   /*********************************************************************************************/
		   

		   for ( int	i =	0; i < v_upfilecnt;	i++ ) { 
			if ( 	!box.getString("p_fileseq" + i).equals(""))	{ 
				v_savefile.addElement(box.getString("p_savefile" + i));			// 		���� ������ִ� ���ϸ� �߿���	��f�� ���ϵ�
				v_filesequence.addElement(box.getString("p_fileseq"	 + i));		 // 		����	������ִ� ���Ϲ�ȣ	�߿��� ��f�� ���ϵ�
			}
		  }

            sql2 = " update tz_qna set     ";
            sql2 += "   title    = ?,              ";
            sql2 += "   contents = ?,";
//            sql2 += "   contents = empty_clob(),";
            sql2 += "   luserid = ?,            ";
            sql2 += "   ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')               ";
            sql2 += " where                     ";
            sql2 += "   subj = ?              ";
            sql2 += "   and subjseq = ?             ";
            sql2 += "   and year = ?           ";
            sql2 += "   and seq = ?           ";
            sql2 += "   and kind = ?           ";

            pstmt = connMgr.prepareStatement(sql2);
            pstmt.setString(1, v_title);
            pstmt.setString(2, v_contents);
            pstmt.setString(3, s_userid);
            pstmt.setString(4, v_subj);
            pstmt.setString(5, v_subjseq);
            pstmt.setString(6, v_year);
            pstmt.setInt   (7, v_repseq);
            pstmt.setString(8, v_repkind);
            isOk = pstmt.executeUpdate();

            sql2 = "select contents from TZ_QNA";
            sql2 += "  where subj    = '" +v_subj + "'" ;
            sql2 += "    and year    = '" +v_year + "'" ;
            sql2 += "    and subjseq = '" +v_subjseq + "'" ;
            sql2 += "    and seq     =  " +v_repseq;
            sql2 += "    and kind    = '" +v_repkind + "'";
//            connMgr.setOracleCLOB(sql2, v_contents);       // (ORACLE 9i ����)

            // ���ϻ�f
			sql3 = "delete from tz_qnafile where subj = " + v_subj + " and year ='" +v_year + "' and subjseq ='" +v_subjseq + "' and seq = " +v_repseq + " and kind = '" +v_repkind + "' and fileseq = ?";
			pstmt3 = connMgr.prepareStatement(sql3);
			for ( int	i =	0; i < v_filesequence.size(); i++ ) { 
			  int	v_fileseq =	Integer.parseInt((String)v_filesequence.elementAt(i));
			  pstmt3.setInt(1, v_fileseq);
			  isOk3 =	pstmt3.executeUpdate();
		    }
		    
		    isOk3 = this.insertCourseUpFile(connMgr, v_subj,v_year,v_subjseq,v_repseq,v_repkind, box);

            if ( isOk > 0 && isOk2 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
				isOk = 1;
            } else { 
			    connMgr.rollback();
				isOk = 0;
			}
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }


        return isOk;
    }



     /**
	 * ���õ� �ڷ����� DB���� ��f
	 * @param connMgr			DB Connection Manager
	 * @param box				receive from the form object and session
	 * @param p_filesequence    ���� ���� ����
	 * @return
	 * @throws Exception
	 */
	public int deleteUpFile(DBConnectionManager	connMgr, RequestBox box, Vector p_filesequence)	throws Exception { 
		PreparedStatement pstmt3 = null;
		String sql	= "";
		String sql3	= "";
        ListSet             ls      = null;
		int	isOk3 =	1;
        String v_types   = box.getString("p_types");
		int	v_seq =	box.getInt("p_seq");

		try	{ 

            // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
            sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
//System.out.println("file delete:::::::::::::" +sql);
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_tabseq = ls.getInt(1);
            ls.close();
            // ------------------------------------------------------------------------------------


			sql3 = "delete from tz_homefile where tabseq = " + v_tabseq + " and seq =? and fileseq = ?";
//System.out.println("1::::::::::::::;");
			pstmt3 = connMgr.prepareStatement(sql3);
			for ( int	i =	0; i < p_filesequence.size(); i++ ) { 
				int	v_fileseq =	Integer.parseInt((String)p_filesequence.elementAt(i));
//System.out.println("2::::::::::::::;");

				pstmt3.setInt(1, v_seq);
				pstmt3.setInt(2, v_fileseq);
				isOk3 =	pstmt3.executeUpdate();
			}
		}
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box,	sql3);
			throw new Exception("sql = " + sql3	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { }	}
		}
		return isOk3;
	}


	/**
	* ���õ� �ڷ����� DB���� ��f
	* @param connMgr			DB Connection Manager
	* @param box				receive from the form object and session
	* @param p_filesequence    ���� ���� ����
	* @return
	* @throws Exception
	*/
	public int deleteRepUpFile(DBConnectionManager	connMgr, RequestBox box)	throws Exception { 
		PreparedStatement pstmt3 = null;
		String sql	= "";
		String sql3	= "";
        ListSet             ls      = null;
		int	isOk3 =	1;
        String v_types   = box.getString("p_reptypes");
        String v_type    = box.getString("p_type");
		int	v_seq =	box.getInt("p_repseq");

		try	{ 

            // ----------------------   ��Խ�������d����  ��n�� tabseq�� �����Ѵ� ----------------------------
            sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
            //System.out.println("file delete:::::::::::::" +sql);
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_tabseq = ls.getInt(1);
            ls.close();
            // ------------------------------------------------------------------------------------

			sql3 = "delete from tz_homefile where tabseq = " + v_tabseq + " and seq =?";
			pstmt3 = connMgr.prepareStatement(sql3);
			pstmt3.setInt(1, v_seq);
			isOk3 =	pstmt3.executeUpdate();

		}
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box,	sql3);
			throw new Exception("sql = " + sql3	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { }	}
		}
		return isOk3;
	}



	/**
    * QNA ��ο� �ڷ����� ���
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
	 public	int	insertCourseUpFile(DBConnectionManager connMgr, String p_subj,String p_year,String p_subjseq,int p_seq,String p_kind, RequestBox box) throws	Exception { 
		ListSet	ls = null;
		PreparedStatement pstmt2 = null;
		String              sql     = "";
		String sql2	= "";
		int	isOk2 =	0;
		int isOk = 1;


		// ----------------------   ��ε�Ǵ� ������ ���;	�˰� �ڵ��ؾ��Ѵ�  --------------------------------
		String [] v_realFileName = new String [FILE_LIMIT];
		String [] v_newFileName	= new String [FILE_LIMIT];

		for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
			v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
			v_newFileName [i] =	box.getNewFileName(FILE_TYPE + (i +1));
            //System.out.println("v_realFileName [i]:::" +v_realFileName [i]);
		}
		// ----------------------------------------------------------------------------------------------------------------------------

		String s_userid	= box.getSession("userid");

		try	{ 
			 // ----------------------	�ڷ� ��ȣ ��n�´� ----------------------------
			sql	= "select nvl(max(fileseq),	0) from	tz_qnafile	where subj = '" +p_subj + "' and subjseq = '" +p_subjseq + "' and year = '" +p_year + "' and seq = " +p_seq + " and kind = '" +p_kind + "'" ;
            //System.out.println("fileinsert::::::::::::" +sql);

			ls = connMgr.executeQuery(sql);
			ls.next();
			int	v_fileseq =	ls.getInt(1) + 1;
            //System.out.println("fileseq::::" +v_fileseq);

			ls.close();
			// ------------------------------------------------------------------------------------

			//// //// //// //// //// //// //// //// // 	 ���� table	�� �Է�	 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql2 =	"insert	into tz_qnafile(";
			sql2 += " subj,     subjseq,    year,     lesson,   ";
			sql2 += " seq,      kind,       fileseq,  realfile, ";
			sql2 += " savefile, luserid,	ldate)              ";
			sql2 +=	" values (";
			sql2 +=	" ?,  ?,  ?,  '000', ";
			sql2 +=	" ?,  ?,  ?,  ?, ";
			sql2 +=	" ?,  '" +s_userid + "',  to_char(sysdate, 'YYYYMMDDHH24MISS'))";

//System.out.println("sql2::::::" +sql2);

			pstmt2 = connMgr.prepareStatement(sql2);

			for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
//System.out.println("realfile:::::::::::::" + v_realFileName[i]);
				if ( 	!v_realFileName	[i].equals(""))	{ 		// 		��f ��ε�	�Ǵ� ���ϸ�	üũ�ؼ� db�� �Է��Ѵ�
					pstmt2.setString(1, p_subj);
					pstmt2.setString(2, p_subjseq);
					pstmt2.setString(3, p_year);
					pstmt2.setInt(4, p_seq);
					pstmt2.setString(5, p_kind);
					pstmt2.setInt(6, v_fileseq);
					pstmt2.setString(7,	v_realFileName[i]);
					pstmt2.setString(8,	v_newFileName[i]);
					isOk2 =	pstmt2.executeUpdate();

					isOk = isOk * isOk2;
					v_fileseq++;
//					System.out.println("p_seq:::" +p_seq);
//System.out.println("v_fileseq:::" +v_fileseq);
				}

			}
		}
		catch ( Exception ex ) { 
			FileManager.deleteFile(v_newFileName, FILE_LIMIT);		// 	�Ϲ�����, ÷������ ��8�� ��f..
			ErrorManager.getErrorStackTrace(ex,	box, sql2);
			throw new Exception("sql = " + sql2	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
			if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
		}
		return isOk;
	}


	 /**
	 * ���õ� �ڷ����� DB���� ��f
	 * @param connMgr			DB Connection Manager
	 * @param box				receive from the form object and session
	 * @param p_filesequence    ���� ���� ����
	 * @return
	 * @throws Exception
	 */
	public int deleteCourseUpFile(DBConnectionManager connMgr, RequestBox box)	throws Exception { 
		PreparedStatement pstmt3 = null;
		String sql	= "";
		String sql3	= "";
        ListSet             ls      = null;
		int	isOk3 =	1;
        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        int    v_seq     = box.getInt("p_seq");

		try	{ 

			sql3 = "delete from tz_qnafile \n";
			sql3 += " where                \n";
			sql3 += " subj        = ?      \n";
			sql3 += " and year    = ?      \n";
			sql3 += " and subjseq = ?      \n";
			sql3 += " and seq     = ?      \n";

			pstmt3 = connMgr.prepareStatement(sql3);

			pstmt3.setString(1, v_subj);
			pstmt3.setString(2, v_year);
            pstmt3.setString(3, v_subjseq);
            pstmt3.setInt(4, v_seq);
			isOk3 =	pstmt3.executeUpdate();

		}
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box,	sql3);
			throw new Exception("sql = " + sql3	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { }	}
		}
		return isOk3;
	}

}