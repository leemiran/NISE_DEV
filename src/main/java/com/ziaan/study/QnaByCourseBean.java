// **********************************************************
//  1. ��      ��: ����������
//  2. ���α׷���: QnaByCourseBean.java
//  3. ��      ��: ����������
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: 
//  7. ��      ��: 
// **********************************************************

package com.ziaan.study;

import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import oracle.sql.CLOB;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class QnaByCourseBean { 
    private static final String FILE_TYPE = "p_file";           //      ���Ͼ��ε�Ǵ� tag name
    private static final int FILE_LIMIT = 1;                    //    �������� ���õ� ����÷�� ����
    private ConfigSet config;
    private int row;

    public QnaByCourseBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    �����ڷ�� ������ ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList   
    */
     public ArrayList selectAdminList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls1         = null;       
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        ProjectData data1   = null;
        ProjectData data2   = null;
        String  v_Bcourse   = ""; // �����ڽ�
        String  v_course    = ""; // �����ڽ�
        String  v_Bcourseseq= ""; // �����ڽ����
        String  v_courseseq = ""; // �����ڽ����                
        int     l           = 0;
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // �����׷�
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // �⵵
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // �������
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // ����з�
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // ����з�
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // ����з�
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// ����&�ڽ�
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // ���� ���
        String  ss_action   = box.getString("s_action");
        
        String  v_orderColumn   = box.getString("p_orderColumn");               // ������ �÷���
        String  v_orderType     = box.getString("p_orderType");                 // ������ ����

		String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
        
        try { 
            if ( ss_action.equals("go") ) {  
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();                   
                                
                // select course,cyear,courseseq,coursenm,subj,isclosed,subjnm,year,subjseq,isonoff,cnt,tabseq
               //  sql1 = "select A.course,A.cyear,A.courseseq,A.coursenm,A.subj,A.isclosed,A.subjnm,A.year,A.subjseq,a.subjseqgr,A.isonoff,edustart,eduend, get_grcodenm(a.grcode) as grcodenm, ";
               //  sql1 += "(select count(seq) from TZ_BOARD where tabseq=B.tabseq) cnt,B.tabseq ";
               //  sql1 += "from VZ_SCSUBJSEQ A,TZ_BDS B where A.subj=B.subj and a.year=b.year and a.subjseq=b.subjseq and B.type='SQ' ";
                
                sql1= "\n select a.course,a.cyear,a.courseseq,a.coursenm,a.subj,a.subjnm,a.year,a.subjseq,a.subjseqgr,a.isonoff,a.isclosed,edustart,eduend  "
                	+ "\n      , (select count(distinct refseq) from tz_board "
                	+ "\n         where  tabseq = b.tabseq "
                	+ "\n         and    subj = a.subj "
	                + "\n         and    year = a.year "
	                + "\n         and    subjseq = a.subjseq ";

                sql1+="\n        ) cnt "
                	+ "\n      , b.tabseq, get_grcodenm(a.grcode) as grcodenm "
                	+ "\n from   vz_scsubjseq a "
                	+ "\n      , tz_bds b ";

                if (v_gadmin.equals("P")) {
                	sql1+="\n      , tz_classtutor d ";
                }
                
                sql1+="\n where  a.subj = b.subj "
               		+ "\n and    a.year = b.year "
            		+ "\n and    a.subjseq = b.subjseq "
                	+ "\n and    b.type='SQ' "
                	+ "\n and    a.gyear = '" + ss_gyear + "'";


                if (v_gadmin.equals("P")) {
                	sql1+="\n and    a.subj = d.subj(+) "
                		+ "\n and    a.year = d.year(+) "
                		+ "\n and    a.subjseq = d.subjseq(+) "
                		+ "\n and    d.tuserid(+)  = " + SQLString.Format(s_userid);
                }
                
            	sql1+="\n and  0 < (select count(distinct refseq) from tz_board   "
            		+ "\n            where  tabseq = b.tabseq "
            		+ "\n              and    subj = a.subj   "
            		+ "\n              and    year = a.year   "
            	    + "\n              and    subjseq = a.subjseq) ";
                

                 if ( !ss_grcode.equals("ALL") ) { 
                 	sql1 += "\n and    a.grcode = '" + ss_grcode + "'";
                 }

                if ( !ss_grseq.equals("ALL") ) { 
                	sql1 += "\n and    a.grseq = '" + ss_grseq + "'";
                }

                if ( !ss_uclass.equals("ALL") ) { 
                    sql1 += "\n and    a.scupperclass = '" + ss_uclass + "'";
                }
                if ( !ss_mclass.equals("ALL") ) { 
                    sql1 += "\n and    a.scmiddleclass = '" + ss_mclass + "'";
                }
                if ( !ss_lclass.equals("ALL") ) { 
                	sql1 += "\n and    a.sclowerclass = '" + ss_lclass + "'";
                }

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1 += "\n and    a.subj = '" + ss_subjcourse + "'";
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                	sql1 += "\n and    a.subjseq = '" + ss_subjseq + "'";
                }
                
				if ( v_orderColumn.equals("") ) { 
	                sql1 += "\n order by a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseqgr,a.subjseq ";					
				} else { 
				    sql1 += "\n order  by a.course, a.cyear, a.courseseq, " + v_orderColumn + v_orderType;
				}
            
				System.out.println(sql1);
				
                ls1 = connMgr.executeQuery(sql1);
                
                    while ( ls1.next() ) { 
                        data1 = new ProjectData();
                        data1.setCourse( ls1.getString("course") );
                        data1.setCyear( ls1.getString("cyear") );
                        data1.setCourseseq( ls1.getString("courseseq") );
                        data1.setCoursenm( ls1.getString("coursenm") );                    
                        data1.setSubj( ls1.getString("subj") );
                        data1.setYear( ls1.getString("year") );              
                        data1.setSubjseq( ls1.getString("subjseq") );
                        data1.setSubjseqgr( ls1.getString("subjseqgr") );
                        data1.setSubjnm( ls1.getString("subjnm") );
                        data1.setIsclosed( ls1.getString("isclosed") );                            
                        data1.setIsonoff( ls1.getString("isonoff") );    
                        data1.setEdustart( ls1.getString("edustart") );
                        data1.setEduend( ls1.getString("eduend") );                        
                        data1.setCnt( ls1.getInt("cnt") );        
                        data1.setTabseq( ls1.getInt("tabseq") );     
                        data1.setGrcodemn(ls1.getString("grcodenm"));
                        
                        list1.add(data1);
                    }
                    
                     for ( int i = 0;i < list1.size(); i++ ) { 
                        data2       =   (ProjectData)list1.get(i);
                        v_course    =   data2.getCourse();
                        v_courseseq =   data2.getCourseseq();
                        if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                            sql2 = "select count(A.subj) cnt from TZ_SUBJSEQ A, TZ_BDS B ";
                            sql2 += "where A.subj=B.subj and a.year=b.year and a.subjseq=b.subjseq and B.type='SQ' and A.course = '" + v_course + "' and A.courseseq = '" +v_courseseq + "' ";
                          if ( !ss_grcode.equals("ALL") ) { 
                              sql2 += " and A.grcode = '" + ss_grcode + "'";
                          } 
                          if ( !ss_gyear.equals("ALL") ) { 
                              sql2 += " and A.gyear = '" + ss_gyear + "'";
                          } 
                          if ( !ss_grseq.equals("ALL") ) { 
                              sql2 += " and A.grseq = '" + ss_grseq + "'";
                          }
                     
                          if ( !ss_uclass.equals("ALL") ) { 
                              sql2 += " and A.scupperclass = '" + ss_uclass + "'";
                          }
                     
                          if ( !ss_mclass.equals("ALL") ) { 
                              sql2 += " and A.scmiddleclass = '" + ss_mclass + "'";
                          }
                          if ( !ss_lclass.equals("ALL") ) { 
                              sql2 += " and A.sclowerclass = '" + ss_lclass + "'";
                          }
                          if ( !ss_subjcourse.equals("ALL") ) { 
                              sql2 += " and A.scsubj = '" + ss_subjcourse + "'";
                          }
                          if ( !ss_subjseq.equals("ALL") ) { 
                              sql2 += " and A.scsubjseq = '" + ss_subjseq + "'";
                          }
//                            System.out.println("sql2 ==  ==  ==  ==  ==  == > " +sql2);
                            ls2 = connMgr.executeQuery(sql2); 
                            if ( ls2.next() ) { 
                                data2.setRowspan( ls2.getInt("cnt") );
                                data2.setIsnewcourse("Y");
                            }
                        } else { 
                            data2.setRowspan(0);
                            data2.setIsnewcourse("N");
                        }
                        v_Bcourse   =   v_course;
                        v_Bcourseseq=   v_courseseq;
                        list2.add(data2);
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                     }
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;        
    }

    /**
    * �ڷ�� ����Ʈȭ�� select
    * @param    box          receive from the form object and session
    * @return ArrayList  �ڷ�� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectDocList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;


        int v_tabseq = box.getInt("p_tabseq");
        // int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");


        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, count(b.realfile) filecnt, decode(a.gadmin, 'ZZ', 'ZZ', 'P1', '����',  '���') gadmin, ";
            sql += "        a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, isopen ";
            sql += "   from TZ_BOARD a, TZ_BOARDFILE b                                                         ";
            sql += "  where a.tabseq = b.tabseq( +)                                                             ";
            sql += "    and a.seq    = b.seq( +)                                                                ";
            sql += "    and a.tabseq = " + v_tabseq;

            if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                if ( v_search.equals("name") ) {              //    �̸����� �˻��Ҷ�
                    sql += " and a.name like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                    sql += " and a.title like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                    sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                }                

            }
            sql += " group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position, decode(A.gadmin, 'ZZ', 'ZZ', 'P1', '����',  '���') ";
            sql += " order by a.refseq desc, position asc                                                       ";

            ls = connMgr.executeQuery(sql);

            // ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
            // ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            // int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            // int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                // dbox.put("d_dispnum",        new Integer(totalrowcount - ls.getRowNum() + 1));
                // dbox.put("d_totalpagecount", new Integer(totalpagecount));
                // dbox.put("d_rowcount",       new Integer(row));
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
   * ���õ� �ڷ�� �Խù� �󼼳��� select
   * @param box          receive from the form object and session
   * @return ArrayList   ��ȸ�� ������
   * @throws Exception
   */
   public DataBox selectBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");
        int v_upfilecnt = (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

       // String [] realfile = new String [v_upfilecnt];
       // String [] savefile= new String [v_upfilecnt];
       // int [] fileseq = new int [v_upfilecnt];
        Vector realfileVector = new Vector();
        Vector savefileVector = new Vector();
        Vector fileseqVector  = new Vector();        

        try { 
            connMgr = new DBConnectionManager();


            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, b.fileseq fileseq, b.realfile realfile, a.content content, decode(a.gadmin, 'ZZ', 'ZZ', 'P1', '����',  '���') gadmin, ";
            sql += "        b.savefile savefile, a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, isopen ";
            sql += " from TZ_BOARD a, TZ_BOARDFILE b                                                                                            ";
            sql += "  where a.tabseq = b.tabseq( +)                                                                                              ";
            sql += "    and a.seq    = b.seq( +)                                                                                                 ";
            sql += "    and a.tabseq = " + v_tabseq;            
            sql += "    and a.seq    = " + v_seq;

            ls = connMgr.executeQuery(sql);


            for ( int i = 0; ls.next(); i++ ) { 

                dbox = ls.getDataBox();

                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
                fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }
            if ( realfileVector      != null ) dbox.put("d_realfile", realfileVector);
            if ( savefileVector      != null ) dbox.put("d_savefile", savefileVector);
            if ( fileseqVector   != null ) dbox.put("d_fileseq", fileseqVector);

            connMgr.executeUpdate("update TZ_BOARD set cnt = cnt + 1 where  tabseq = " + v_tabseq + " and seq = " + v_seq);
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
    * ���ο� �ڷ�� ���� ���
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
       public int insertBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;

        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int v_seq = 0;
        ListSet ls1         = null;

        int    v_tabseq  = box.getInt("p_tabseq");
        String v_title   = box.getString("p_title");
        String v_content = box.getString("p_content"); // ���� clob

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name"); 
        String s_gadmin = box.getSession("gadmin");
        String v_isopen = box.getString("p_isopen");
        String v_sangdamgubun = box.getString("p_sangdamgubun");

        /*********************************************************************************************/
        // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_content); // ��ü���� 
        boolean result = namo.parse(); // ���� �Ľ� ���� 
        if ( !result ) { // �Ľ� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
            String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
            String prefix = "studyboard" + v_tabseq;         // ���ϸ� ���ξ�
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
        }
        if ( !result ) { // �������� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        v_content = namo.getContent(); // ���� ����Ʈ ���
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
            sql = " select nvl(max(seq), 0) from TZ_BOARD where tabseq = '" +v_tabseq + "' ";
            ls1 = connMgr.executeQuery(sql);
            if ( ls1.next() ) { 
                v_seq = ls1.getInt(1) + 1;
            }
            ls1.close();
            // --------------------------------------------------------------------------

            // ----------------------   �Խ��� table �� �Է�  --------------------------
            sql1 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, isopen, sangdam_gubun) ";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?, ?, ?)";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?, ?, ?)";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);
            pstmt1.setInt(2, v_seq);
            pstmt1.setString(3, s_userid);
            pstmt1.setString(4, s_usernm);
            pstmt1.setString(5, v_title);
            pstmt1.setString(6, v_content);
            pstmt1.setInt(7, 0);
            pstmt1.setInt(8, v_seq);
            pstmt1.setInt(9, 1);
            pstmt1.setInt(10, 1);
            pstmt1.setString(11, s_userid);
            pstmt1.setString(12, s_gadmin);
            pstmt1.setString(13, v_isopen);
            pstmt1.setString(14, v_sangdamgubun);

            isOk1 = pstmt1.executeUpdate();
            sql2 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       

            // ���Ͼ��ε�
            isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);
                    
            if ( isOk1 > 0 && isOk2 > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
        }

        return isOk1*isOk2;
    }





    /**
    * ���ο� �ڷ�� �亯 ���
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int replyBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;
        int v_seq = 0;

        int    v_tabseq   = box.getInt("p_tabseq");
        int    v_refseq   = box.getInt("p_refseq");
        int    v_levels   = box.getInt("p_levels");
        int    v_position = box.getInt("p_position");
        String v_title    = box.getString("p_title");
        String v_content  = box.getString("p_content");

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");
        String v_isopen = box.getString("p_isopen");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ���� �亯�� ��ġ ��ĭ������ ����
            sql1  = "update TZ_BOARD ";
            sql1 += "   set position = position + 1 ";
            sql1 += " where tabseq   = ? ";
            sql1 += "   and refseq   = ? ";
            sql1 += "   and position > ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);            
            pstmt1.setInt(2, v_refseq);
            pstmt1.setInt(3, v_position);
            isOk1 = pstmt1.executeUpdate();

            stmt1 = connMgr.createStatement();
            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
            sql2 = "select nvl(max(seq), 0) from TZ_BOARD where tabseq = " +  v_tabseq;
            rs1 = stmt1.executeQuery(sql2);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            rs1.close();
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// /  �Խ��� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql1 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, isopen) ";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?) ";

            pstmt2 = connMgr.prepareStatement(sql3);
            pstmt2.setInt(1, v_tabseq);            
            pstmt2.setInt(2, v_seq);
            pstmt2.setString(3, s_userid);
            pstmt2.setString(4, s_usernm);
            pstmt2.setString(5, v_title);
            connMgr.setCharacterStream(pstmt2, 6, v_content); //      Oracle 9i or Weblogic 6.1 �� ���
            pstmt2.setInt(7, 0);
            pstmt2.setInt(8, v_refseq);
            pstmt2.setInt(9, v_levels + 1);
            pstmt2.setInt(10, v_position + 1);
            pstmt2.setString(11, s_userid);
            pstmt2.setString(12, s_gadmin);
            pstmt2.setString(13, v_isopen);
            isOk2 = pstmt2.executeUpdate();


            isOk3 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);

            if ( isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( stmt2 != null ) { try { stmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
        }

        return isOk2*isOk3;
    }



    /**
    * ���õ� �ڷ� �󼼳��� ����
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        // DBConnectionManager_bulletin bulletinDB = null;
        Connection conn = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        CLOB clob                 = null;
        Writer clobWriter = null;
        Reader clobReader = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");

        int    v_tabseq    = box.getInt("p_tabseq");
        int    v_seq       = box.getInt("p_seq");
        int    v_upfilecnt = box.getInt("p_upfilecnt");    //  ������ ������ִ� ���ϼ�
        String v_title     = box.getString("p_title");
        String v_content   = box.getString("p_content");
        String v_isopen    = box.getString("p_isopen");
        String v_sangdamgubun   = box.getString("p_sangdamgubun");
        // String v_content =  StringManager.replace(box.getString("p_content"),"<br > ","\n"); 
        
        // Vector v_savefile     = box.getVector("p_savefile"); // ���û�������
        // Vector v_filesequence = box.getVector("p_fileseq");  // ���û������� sequence
        // Vector v_realfile     = box.getVector("p_file");     // ���� ��� ����

        Vector v_savefile     = new Vector();
        Vector v_filesequence = new Vector();
        
        for ( int   i = 0; i < v_upfilecnt; i++ ) { 
            if (    !box.getString("p_fileseq" + i).equals("")) { 

                v_savefile.addElement(box.getString("p_savefile" + i));         //      ������ ������ִ� ���ϸ� �߿���   ������ ���ϵ�
                v_filesequence.addElement(box.getString("p_fileseq"  + i));      //         ������ ������ִ� ���Ϲ�ȣ  �߿��� ������ ���ϵ�

            }
        }


        /*********************************************************************************************/
        // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_content); // ��ü���� 
        boolean result = namo.parse(); // ���� �Ľ� ���� 
        System.out.println(result);
        
        if ( !result ) { // �Ľ� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
            String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
            String prefix = "board" + v_seq;         // ���ϸ� ���ξ�
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
            System.out.println(result);
        }
        if ( !result ) { // �������� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        v_content = namo.getContent(); // ���� ����Ʈ ���
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql1 = "update TZ_BOARD set title = ?, content=?, isopen = ?, sangdam_gubun = ? ";//, userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')";
            sql1 += "  where tabseq = ? and seq = ?";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_content);
            pstmt1.setString(3, v_isopen);
            pstmt1.setString(4, v_sangdamgubun);
            
        //  connMgr.setCharacterStream(pstmt1, 2, v_content);           // Oracle 9i or Weblogic 6.1 �� ���
            /*pstmt1.setString(2, s_userid);
            pstmt1.setString(3, s_usernm);
            pstmt1.setString(4, s_userid);*/
            pstmt1.setInt(5, v_tabseq);
            pstmt1.setInt(6, v_seq);

            isOk1 = pstmt1.executeUpdate();

            // WebLogic 6.1�ΰ��
            sql2 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
            //connMgr.setWeblogicCLOB(sql2, v_content);       //      clob �ʵ忡 ��Ʈ���� �̿�,  data �� �ִ´�(Weblogic �� ���)

            isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);       //      ����÷���ߴٸ� ����table��  insert

            isOk3 = this.deleteUpFile(connMgr, box, v_filesequence);        //     ������ ������ �ִٸ� ����table���� ����

            if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
                if ( v_savefile != null ) { 
                    FileManager.deleteFile(v_savefile);         //   DB ���� ���ó���� �Ϸ�Ǹ� �ش� ÷������ ����
                }
            } else connMgr.rollback();
        } catch ( Exception ex ) { 
            // conn.rollback();
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( clob != null ) { try { clob.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            // if ( conn != null ) { try { conn.setAutoCommit(true); } catch ( Exception e10 ) { } }
            // if ( conn != null ) { try { bulletinDB.freeConnection("ziaan", conn); } catch ( Exception e10 ) { } }
        }

        return isOk1*isOk2*isOk3;
    }


    /**
    * ���õ� �Խù� ����
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");
        int v_upfilecnt = box.getInt("p_upfilecnt");    //  ������ ������ִ� ���ϼ�
        Vector v_savefile  = box.getVector("p_savefile");

        // �亯 ���� üũ(�亯 ������ �����Ұ�)
        if ( this.selectBoard(v_tabseq, v_seq) == 0 ) { 

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                isOk2 = 1;
                sql1 = "delete from TZ_BOARD where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);                
                pstmt1.setInt(2, v_seq);
                isOk1 = pstmt1.executeUpdate();

                if ( v_upfilecnt > 0 ) { 
                    sql2 = "delete from TZ_BOARDFILE where tabseq = ? and seq =  ?";
                    pstmt2 = connMgr.prepareStatement(sql2);
                    pstmt2.setInt(1, v_tabseq);
                    pstmt2.setInt(2, v_seq);                    
                    isOk2 = pstmt2.executeUpdate();
                }

                if ( isOk1 > 0 && isOk2 > 0 ) { 
                    connMgr.commit();
                    if ( v_savefile != null ) { 
                        FileManager.deleteFile(v_savefile);         //   ÷������ ����
                    }
                } else connMgr.rollback();
            }
            catch ( Exception ex ) { 
                connMgr.rollback();
                ErrorManager.getErrorStackTrace(ex, box,    sql1);
                throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }  
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk1*isOk2;
    }


   /**
   * ������ ���� �亯 ���� üũ
   * @param seq          �Խ��� ��ȣ
   * @return result      0 : �亯 ����,    1 : �亯 ����
   * @throws Exception
   */
   public int selectBoard(int tabseq, int seq) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                         ";
            sql += "  from                                        ";
            sql += "    (select tabseq, refseq, levels, position  ";
            sql += "       from TZ_BOARD                          ";
            sql += "      where tabseq = " + tabseq;
            sql += "        and seq = " + seq;
            sql += "     ) a, TZ_BOARD b                          ";
            sql += " where a.tabseq = b.tabseq                    ";
            sql += "   and a.refseq = b.refseq                    ";
            sql += "   and b.levels = (a.levels +1)                ";
            sql += "   and b.position = (a.position +1)            ";


            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
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


//// //// //// //// //// //// //// //// //// //// //// //// //// /// ���� ���̺�   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

    /**
    * ���ο� �ڷ����� ���
    * @param connMgr  DB Connection Manager
    * @param p_seq    �Խù� �Ϸù�ȣ
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertUpFile(DBConnectionManager connMgr, int p_tabseq, int p_seq, RequestBox   box) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql2 = "";
        int isOk2 = 1;

        // ----------------------   ���ε�Ǵ� ������ ������ �˰� �ڵ��ؾ��Ѵ�  --------------------------------

        String [] v_realFileName = new String [FILE_LIMIT];
        String [] v_newFileName = new String [FILE_LIMIT];

        for ( int i = 0; i < FILE_LIMIT; i++ ) { 
            v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
            v_newFileName [i] = box.getNewFileName(FILE_TYPE + (i +1));
        }
        // ----------------------------------------------------------------------------------------------------------------------------

        String s_userid = box.getSession("userid");

        try { 
             // ----------------------   �ڷ� ��ȣ �����´� ----------------------------
            sql = "select nvl(max(fileseq), 0) from TZ_BOARDFILE where tabseq = " + p_tabseq + " and seq =   " + p_seq;
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_fileseq = ls.getInt(1) + 1;
            ls.close();
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// //   ���� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql2 =  "insert into TZ_BOARDFILE(tabseq, seq, fileseq, realfile, savefile, luserid, ldate)";
            sql2 += " values (?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

            pstmt2 = connMgr.prepareStatement(sql2);

            for ( int i = 0; i < FILE_LIMIT; i++ ) { 
                if ( !v_realFileName [i].equals("") ) {       //      ���� ���ε� �Ǵ� ���ϸ� üũ�ؼ� db�� �Է��Ѵ�
                    pstmt2.setInt(1, p_tabseq);                 
                    pstmt2.setInt(2, p_seq);
                    pstmt2.setInt(3, v_fileseq);
                    pstmt2.setString(4, v_realFileName [i]);
                    pstmt2.setString(5, v_newFileName [i]);
                    pstmt2.setString(6, s_userid);

                    isOk2 = pstmt2.executeUpdate();
                    v_fileseq++;
                }
            }
        } catch ( Exception ex ) { 
            FileManager.deleteFile(v_newFileName, FILE_LIMIT);      //  �Ϲ�����, ÷������ ������ ����..
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
        }

        return isOk2;
    }


    /**
     * ���õ� �ڷ����� DB���� ����
     * @param connMgr           DB Connection Manager
     * @param box               receive from the form object and session
     * @param p_filesequence    ���� ���� ����
     * @return
     * @throws Exception
     */
    public int deleteUpFile(DBConnectionManager connMgr, RequestBox box, Vector p_filesequence) throws Exception { 
        PreparedStatement pstmt3 = null;
        String sql3 = "";
        int isOk3 = 1;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq    = box.getInt("p_seq");

        try { 
            sql3 = "delete from TZ_BOARDFILE where tabseq =? and seq =? and fileseq = ?";
Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >> " +sql3);
Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >> " +v_tabseq);
Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >> " +v_seq);
            pstmt3 = connMgr.prepareStatement(sql3);

Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >> " +p_filesequence.size() );

            for ( int i = 0; i < p_filesequence.size(); i++ ) { 
                int v_fileseq = Integer.parseInt((String)p_filesequence.elementAt(i));

Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > " +v_fileseq);

                pstmt3.setInt(1, v_tabseq);
                pstmt3.setInt(2, v_seq);
                pstmt3.setInt(3, v_fileseq);

                isOk3 = pstmt3.executeUpdate();
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql3);
            throw new Exception("sql = " + sql3 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
        }

        return isOk3;
    }

 
    public static String convertBody(String contents) throws Exception { 
        String result = "";
        
        result = StringManager.replace(contents, "<HTML > ","");
        result = StringManager.replace(result, "<HEAD > ","");
        result = StringManager.replace(result, "<META NAME=\"GENERATOR\" Content=\"Microsoft DHTML Editing Control\" > ","");
        result = StringManager.replace(result, "<TITLE > ","");
        result = StringManager.replace(result, "</TITLE > ","");
        result = StringManager.replace(result, "</HEAD > ","");
        result = StringManager.replace(result, "<BODY > ","");
        result = StringManager.replace(result, "</BODY > ","");       
        result = StringManager.replace(result, "</HTML > ","");
                
        return result;
    }
}
