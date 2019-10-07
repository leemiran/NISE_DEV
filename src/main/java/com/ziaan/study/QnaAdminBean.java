// **********************************************************
//  1. ��      ��: QNA DATA
//  2. ���α׷���: QnaAdminBean.java
//  3. ��      ��: ���� admin bean
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ���� 2004. 11. 26
//  7. ��      ��: 
// **********************************************************

package com.ziaan.study;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.common.BoardBean;
import com.ziaan.common.GetCodenm;
import com.ziaan.homepage.LoginBean;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.MemberAdminBean;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class QnaAdminBean { 
    public QnaAdminBean() { }

    /**
    ���� q&a  ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectQnaSubjList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        String sql3         = "";
        QnaData data1  = null;
        QnaData data2  = null;
        String  v_Bcourse   = ""; // �����ڽ�
        String  v_course    = ""; // �����ڽ�
        String  v_Bcourseseq= ""; // �����ڽ����
        String  v_courseseq = ""; // �����ڽ����
        String  v_subj      = ""; // �����䰹�� ������� ����
        String  v_year      = "";
        String  v_subjseq   = "";
        String  v_dates     = "";
        int     v_noans     = 0;
        int     v_cnt       = 0;
        int     l           = 0;
        String  ss_grcode    = box.getStringDefault("s_grcode","ALL");        // �����׷�
        String  ss_gyear     = box.getStringDefault("s_gyear","ALL");         // �⵵
        String  ss_grseq     = box.getStringDefault("s_grseq","ALL");         // �������
        String  ss_uclass    = box.getStringDefault("s_uclass","ALL");        // ����з�
        String  ss_subjcourse= box.getStringDefault("s_subjcourse","ALL");    // ����&�ڽ�
        String  ss_subjseq   = box.getStringDefault("s_subjseq","ALL");       // ���� ���
        
        String  v_orderColumn   = box.getString("p_orderColumn");           	// ������ �÷���
        String  v_orderType     = box.getString("p_orderType");           		// ������ ����

        try { 
            System.out.println("s_action" + box.getString("s_action") );
            if ( box.getString("s_action").equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();

                sql1 = "select course,cyear,courseseq,coursenm,subj,year,subjseq,subjseqgr,subjnm,isclosed,isonoff, ";
                sql1 += "(select count(*) from TZ_QNA where subj = A.subj and subjseq=A.subjseq and kind = 0) as qcnt,";
                sql1 += "(select count(*) from TZ_QNA where subj = A.subj and subjseq=A.subjseq and kind > 0) as anscnt, ";
                
                sql1 += "((select count(*) from TZ_QNA where subj = A.subj and subjseq=A.subjseq and kind = 0) - ";
                sql1 += "(select count(*) from TZ_QNA where subj = A.subj and subjseq=A.subjseq and kind > 0)) as nanscnt ";
                
                sql1 += "from VZ_SCSUBJSEQ A where 1 = 1 ";
                if ( !ss_grcode.equals("ALL") ) { 
                    sql1 += " and grcode = '" + ss_grcode + "'";
                }
                if ( !ss_gyear.equals("ALL") ) { 
                    sql1 += " and gyear = '" + ss_gyear + "'";
                }
                if ( !ss_grseq.equals("ALL") ) { 
                    sql1 += " and grseq = '" + ss_grseq + "'";
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
                
				if ( v_orderColumn.equals("") ) { 
                	sql1 += " order by course, cyear, courseseq, subj, year, subjseq ";
				} else { 
				    sql1 += " order by " + v_orderColumn + v_orderType;
				}
				


                ls1 = connMgr.executeQuery(sql1);

                    while ( ls1.next() ) { 
                        data1 = new QnaData();
                        data1.setCourse( ls1.getString("course") );
                        data1.setCyear( ls1.getString("cyear") );
                        data1.setCourseseq( ls1.getString("courseseq") );
                        data1.setCoursenm( ls1.getString("coursenm") );
                        data1.setSubj( ls1.getString("subj") );
                        data1.setYear( ls1.getString("year") );
                        data1.setSubjseq( ls1.getString("subjseq") );
						data1.setSubjseqgr( ls1.getString("subjseqgr") );
                        data1.setSubjnm( ls1.getString("subjnm") );
                        data1.setQcnt( ls1.getInt("qcnt") );
                        data1.setAnscnt( ls1.getInt("anscnt") );
                        data1.setIsclosed( ls1.getString("isclosed") );
                        
                        v_subj = ls1.getString("subj");
                        v_year = ls1.getString("year");
                        v_subjseq = ls1.getString("subjseq");

                        sql3 = "select lesson, seq, count(*) cnt from tz_qna where subj='" +v_subj + "' and year='" +v_year + "'";
                        sql3 +="\r\n  and subjseq='" +v_subjseq + "'  group by lesson, seq";
                        ls2 = connMgr.executeQuery(sql3);
                        v_noans = 0;
                        while ( ls2.next() ) { 
                            v_cnt    = ls2.getInt("cnt");
                            if ( v_cnt == 1) { 
                                v_noans += 1;
                            }
                        }
                        ls2.close();

                        data1.setNoanscnt(v_noans);

                        list1.add(data1);
                    }
										ls1.close();
										
                    for ( int i = 0;i < list1.size(); i++ ) { 
                        data2       =   (QnaData)list1.get(i);
                        v_course    =   data2.getCourse();
                        v_courseseq =   data2.getCourseseq();
                        if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                            sql2 = "select count(subj) cnt from TZ_SUBJSEQ ";
                            sql2 += "where course = '" + v_course + "' and courseseq = '" +v_courseseq + "' ";
                            if ( !ss_grcode.equals("ALL") ) { 
                                sql2 += " and grcode = '" + ss_grcode + "'";
                            }
                            if ( !ss_gyear.equals("ALL") ) { 
                                sql2 += " and gyear = '" + ss_gyear + "'";
                            }
                            if ( !ss_grseq.equals("ALL") ) { 
                                sql2 += " and grseq = '" + ss_grseq + "'";
                            }
    //                        System.out.println("sql2 ==  ==  ==  ==  ==  == > " +sql2);
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

        return list2;
    }
    
    
    /**
    �������ú� q&a  ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectQnaSubjseqList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        String sql1         = "";
        String sql2         = "";
        QnaData data1  = null;
        String  v_lesson    = "";
        int     v_noans     = 0;
        int     v_cnt       = 0;
        int     l           = 0;
        String  v_subj    = box.getString("p_subj");        // ����
        String  v_year     = box.getString("p_year");         // �⵵
        String  v_subjseq   = box.getString("p_subjseq");       // ���� ���
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
// �ý��۰��ù� ��Ÿ���� ������ �������� start
            data1 = new QnaData();
            data1.setLesson("00");
            data1.setLessonnm("�ý��۰��� �� ��Ÿ����");
            sql1 = "select count(*) cnt from TZ_QNA where subj = '" +v_subj + "' and subjseq='" +v_subjseq + "' and lesson='00' and kind = 0";
            ls1 = connMgr.executeQuery(sql1);
            if ( ls1.next() ) { 
                data1.setQcnt( ls1.getInt("cnt") );
            }
            ls1.close();

            sql1 = "select seq, count(*) cnt from tz_qna where subj='" +v_subj + "' and year='" +v_year + "'";
            sql1 +="\r\n  and subjseq='" +v_subjseq + "' and  lesson = '00' group by seq";

            ls1 = connMgr.executeQuery(sql1);
            while ( ls1.next() ) { 
                v_cnt   = ls1.getInt("cnt");
                if ( v_cnt == 1) { 
                    v_noans += 1;
                }
            }
            ls1.close();
            data1.setNoanscnt(v_noans);
            list1.add(data1);
//          �ý��۰��ù� ��Ÿ���� ������ �������� end


            sql1 = "select lesson, sdesc, ";
            sql1 += "(select count(*) from TZ_QNA where subj = '" +v_subj + "' and subjseq='" +v_subjseq + "' and lesson=A.lesson and kind = 0) as qcnt ";
            sql1 += "from TZ_SUBJLESSON A where A.subj = '" +v_subj + "' ";
            System.out.println("sql1 ==  ==  ==  ==  ==  == > " +sql1);
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new QnaData();
                    data1.setLesson( ls1.getString("lesson") );
                    data1.setLessonnm( ls1.getString("sdesc") );
                    data1.setQcnt( ls1.getInt("qcnt") );
                    v_lesson = ls1.getString("lesson");

                    sql2 = "select seq, count(*) cnt from tz_qna where subj='" +v_subj + "' and year='" +v_year + "'";
                    sql2 +="\r\n  and subjseq='" +v_subjseq + "' and  lesson = '" +v_lesson + "' group by seq";
                    ls2 = connMgr.executeQuery(sql2);
                    v_noans = 0;
                    while ( ls2.next() ) { 
                        v_cnt    = ls2.getInt("cnt");
                        if ( v_cnt == 1) { 
                            v_noans += 1;
                        }
                    }
                    ls2.close();

                    data1.setNoanscnt(v_noans);

                    list1.add(data1);
                }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    
    /**
    ������ ����  ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectQnaList(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls1         = null;
       ArrayList list1     = null;
       String sql1         = "";
       String sql2         = "";
       QnaData data1  = null;

       String v_subj = box.getString("p_subj");
       String v_year = box.getString("p_year");
       String v_subjseq = box.getString("p_subjseq");
       String v_lesson = box.getString("p_lesson");

	  	// sql1 = "	select 	B.seq, B.title,  B.contents, B.inuserid, B.indate, b.jikwinm, b.compnm, b.cono,b.anscnt,b.replycono,b.replyindate,";
	  	// sql1 += "			(select name from tz_member where userid = b.replycono) replyusernm,";
	  	// sql1 += "			get_compnm((select comp from tz_member where userid = b.replycono),2,4) replyasgnnm, ";
	  	// sql1 += "			get_jikwinm((select jikwi from tz_member where userid = b.replycono), (select comp from tz_member where userid = b.replycono)) jikwi ";
	  	// sql1 += "  from 	( ";
	  	// sql1 += "			select 	B.seq, B.title,  B.contents, B.inuserid, B.indate, get_jikwinm(A.jikwi,A.comp) jikwinm, get_compnm(A.comp,2,4) compnm, A.cono, ";
	  	// sql1 += "					(select count(*) from TZ_QNA where subj=B.subj and year=B.year and subjseq=B.subjseq and lesson=B.lesson and seq=B.seq and kind > 0) anscnt, ";
	  	// sql1 += " 					(select inuserid from tz_qna where subj=b.subj and year=b.year and subjseq=b.subjseq and lesson=b.lesson and seq=b.seq and ";
   	  // sql1 += "		    				kind = (select min(kind) from tz_qna where subj=b.subj and year=b.year and subjseq=b.subjseq and lesson=b.lesson and seq=b.seq and kind > 0 )) replycono, ";
	  	// sql1 += "					(select indate from tz_qna where subj=b.subj and year=b.year and subjseq=b.subjseq and lesson=b.lesson and seq=b.seq and ";
   	  // sql1 += "							kind = (select min(kind) from tz_qna where subj=b.subj and year=b.year and subjseq=b.subjseq and lesson=b.lesson and seq=b.seq and kind > 0 )) replyindate ";
	  	// sql1 += "			from TZ_MEMBER A, TZ_QNA B where B.subj = '" +v_subj + "' and year = '" +v_year + "' and subjseq='" +v_subjseq + "' ";
			//// sql1 += "				and lesson = '" +v_lesson + "' ";
	  	// sql1 +=" and B.inuserid = A.userid and B.kind=0 order by B.indate desc ";
	  	// sql1 += " ) b ";
			
       try { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
System.out.println("sql1-------------------" +sql1);
            ls1 = connMgr.executeQuery(sql1);
            while ( ls1.next() ) { 
                data1 = new QnaData();
                data1.setSeq( ls1.getInt("seq") );
                data1.setTitle( ls1.getString("title") );
								data1.setContents( ls1.getCharacterStream("contents") );
                data1.setInuserid( ls1.getString("inuserid") );
                data1.setIndate( ls1.getString("indate") );
                data1.setJikwinm( ls1.getString("jikwinm") );
                data1.setAsgnnm( ls1.getString("compnm") );
                data1.setCono( ls1.getString("cono") );
                data1.setAnscnt( ls1.getInt("anscnt") );
                data1.setInusernm(MemberAdminBean.getUserName( ls1.getString("inuserid")));
								data1.setReplycono( ls1.getString("replycono") );	// ���� �亯�� ID
								data1.setReplyusernm( ls1.getString("replyusernm") ); // ���� �亯�� �̸�
								data1.setReplyasgnnm( ls1.getString("replyasgnnm") ); // ���� �亯�� �̸�
								data1.setReplyindate( ls1.getString("replyindate") );
								data1.setDelayday(FormatDate.datediff("date",ls1.getString("indate"),ls1.getString("replyindate")));
                list1.add(data1);
            }
            ls1.close();

      } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    /**
    ������ ����  ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectQnaList2(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls1         = null;
       ArrayList list1     = null;
       String sql1         = "";

       String v_subj        = box.getString("p_subj");
       String v_year        = box.getString("p_year");
       String v_subjseq     = box.getString("p_subjseq");
       String v_lesson      = box.getString("p_lesson");
       String v_userid      = box.getSession("userid");
       DataBox dbox1        = null;

       sql1 += "select                                              \n";
       sql1 += "       a.subj,                                      \n";
       sql1 += "       a.tabseq,                                      \n";
       sql1 += "       b.seq,                                       \n";
       sql1 += "       b.title       title,                         \n";
       sql1 += "       decode(sign((select count(indate) from tz_board where tabseq = b.tabseq and refseq = b.seq and levels = b.LEVELS+1 and userid = '"+v_userid+"')), 0, 'N', 'Y')        replygubun,  \n";
       sql1 += "       b.userid      userid,                                                                            \n";
       sql1 += "       b.name        name,                                                                              \n";
       sql1 += "       to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyy.mm.dd')      indate,                        \n";
       sql1 += "       (                                                                                                \n" +
               "        select max(to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyy.mm.dd'))                          \n" +
               "          from tz_board                                                                                 \n" +
               "         where tabseq = b.tabseq and refseq = b.seq and levels = b.LEVELS+1 and userid = '"+v_userid+"' \n" +
               "       ) replydate                                                                                      \n";
       sql1 += "  from                                                                                                  \n";
       sql1 += "       tz_bds a,                                                                                        \n";
       sql1 += "       tz_board b,                                                                                      \n";
       sql1 += "       vz_scsubjseq c                                                                                   \n";
       sql1 += " where                                                                                                  \n";
       sql1 += "       a.SUBJ = '"+v_subj+"'                                                                            \n";
       sql1 += "   and a.type = 'SQ'                                                                                    \n";
       sql1 += "   and b.levels = 1                                                                                     \n";
       sql1 += "   and a.tabseq = b.tabseq                                                                              \n";
       sql1 += "   and a.subj = c.subj and c.year = '"+v_year+"' and c.subjseq = '"+v_subjseq+"'               			\n";
       sql1 += "   and b.subj = c.subj and b.year = c.year and b.subjseq = c.subjseq									\n";
       sql1 += "   and substr(b.INDATE, 1, 10) between c.edustart and c.eduend                                          \n";       
       sql1 += " order by b.indate desc                                                                                 \n";
       
       try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            
            System.out.println(sql1);
            ls1 = connMgr.executeQuery(sql1);
            while ( ls1.next() ) {                 
                dbox1 = ls1.getDataBox();                 
                list1.add(dbox1);
            }
            ls1.close();

      } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    /**
    ���� ����ȸ  �亯�� ����
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectQnaDetail(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls1         = null;
       ArrayList list1     = null;
       String sql1         = "";
       QnaData data1  = null;
       String v_subj = box.getString("p_subj");
       String v_year = box.getString("p_year");
       String v_subjseq = box.getString("p_subjseq");
       String v_lesson = box.getString("p_lesson");
       String v_refseq = box.getString("p_refseq");

       sql1 += "select                                  \n";
       sql1 += "       b.*                              \n";
       sql1 += "  from                                  \n";
       sql1 += "       tz_bds a,                        \n";
       sql1 += "       tz_board b                       \n";
       sql1 += " where                                  \n";
       sql1 += "       a.SUBJ = '"+v_subj+"'            \n";
       sql1 += "   and a.type = 'SQ'                    \n";
       sql1 += "   and a.tabseq = b.tabseq              \n";
       sql1 += "   and b.refseq = '"+v_refseq+"'        \n";
       sql1 += " order by b.position			\n";

	   try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            ls1 = connMgr.executeQuery(sql1);
                         
            while ( ls1.next() ) { 
                DataBox dbox = ls1.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
            return list1;
    }

    public int insertQna(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls                  = null;
       PreparedStatement pstmt2    = null;
       PreparedStatement pstmt3    = null;
       String sql1                 = "";
       String sql2                 = "";
       String sql3                 = "";
       int isOk                    = 0;
       String v_user_id            = box.getSession("userid");
       String v_user_nm						 = box.getSession("name");
       String v_subj               = box.getString("p_subj");          // ����
       String v_year               = box.getString("p_year");          // �⵵
       String v_subjseq            = box.getString("p_subjseq");       // ������
       int v_kind                 = box.getInt("p_kind");
       String v_title              = box.getString("p_title");
       String v_contents           = box.getString("p_contents");
       String v_lesson             = box.getString("p_lesson");
       int  v_seq                  = box.getInt("p_seq");
       int  v_max                = 0;
       int  v_inseq             = 0;
       int  v_intype            = 0;
       int  i = 1;
       int  v_cnt               = 0;

       // String v_replygubun = getReplyGubun(box);
       String v_replygubun = "";

       try { 
           	connMgr = new DBConnectionManager();
		   			connMgr.setAutoCommit(false);
		   
            if ( v_kind == 0 ) {  // �����ϰ��
                sql1  = " select max(seq) +1 maxseq from TZ_QNA ";
                sql1 += "  where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "'";
                sql1 += "    and kind='0' ";
            } else {          // �亯�ϰ��
                sql1  = "select max(kind) +1 maxseq from TZ_QNA ";
                sql1 += " where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "'";
                sql1 += "  and seq=" +v_seq + " ";
            }

           ls = connMgr.executeQuery(sql1);
           System.out.println("sql1 ==  ==  ==  ==  == > " +sql1);

           if ( ls.next() ) { 
               v_max = ls.getInt(1);
           }

            if ( v_kind == 0 ) {  // �����ϰ��
                 v_inseq = v_max;
                 v_intype = 0;
            } else {          // �亯�ϰ��
                v_inseq = v_seq;
                v_intype = v_max;
            }

           sql2  = "insert into TZ_QNA(subj,year,subjseq,lesson,seq,kind,title,contents,";
           sql2 += "indate,inuserid,replygubun,luserid,ldate,grcode) ";
				   sql2 += "values(?,?,?,?,?,?,?,'" + v_contents + "',to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS'),'N00001')";
		       
           pstmt2 = connMgr.prepareStatement(sql2);

           pstmt2.setString(1,v_subj);
           pstmt2.setString(2,v_year);
           pstmt2.setString(3,v_subjseq);
           pstmt2.setString(4,"000");
           pstmt2.setInt(5,v_inseq);
           pstmt2.setInt(6,v_intype);
           pstmt2.setString(7,v_title);
           pstmt2.setString(8,v_user_id);
           pstmt2.setString(9,v_replygubun);
           pstmt2.setString(10,v_user_id);


           isOk = pstmt2.executeUpdate();
		  
   
		   // CLOB ����
			 sql3 = "select 	contents ";
		   sql3 += "from 	TZ_QNA ";
		   sql3 += "where 	subj = '" + v_subj + "' and year = '" + v_year + "' and subjseq = '" + v_subjseq  + "' and ";
		   sql3 += "  		lesson = '" + v_lesson + "' and  seq  = " + v_inseq + " and kind = '" + v_intype + "'	"; 
	   
		   if ( isOk > 0 ) { 
		   		sql1  = "select count(*) cnt ";
		   		sql1 += "  from TZ_QNA ";
		   		sql1 += " where subj='" +v_subj + "'";
		   		sql1 += "   and year='" +v_year + "'";
		   		sql1 += "   and subjseq='" +v_subjseq + "'";
		   		sql1 += "   and seq=" +v_seq;
		   		sql1 += "   and kind=0";
		   		sql1 += "   and replydate=''";
		   		
		   		ls = connMgr.executeQuery(sql1);
		   		if ( ls.next() ) { 
		   			v_cnt = ls.getInt(1);	
		   		}
		   		ls.close();
		   		
		   		if ( v_cnt == 0 ) { 
		   			sql1 = "update TZ_QNA set replyuserid=?,replyusernm=?,replydate=to_char(sysdate, 'YYYYMMDDHH24MISS') where subj=? and year=? and subjseq=? and seq=? and kind=0";
		   			pstmt3 = connMgr.prepareStatement(sql1);
          	
		   			pstmt3.setString(1,v_user_id);
          	pstmt3.setString(2,v_user_nm);
          	pstmt3.setString(3,v_subj);
          	pstmt3.setString(4,v_year);
          	pstmt3.setString(5,v_subjseq);
          	pstmt3.setInt(6,v_seq);
          	isOk = pstmt3.executeUpdate();
          }
          
		   		if ( v_kind != 0 ) { 
			   		// �亯�ڿ��� ����Ʈ ���� �ο�
			   		LoginBean bean = new LoginBean();
			   		bean.loginMileage(connMgr,v_user_id, "ELN_REG_REPLY");
			   	}
		   	
		   		connMgr.commit();
		   } else { 
		   		connMgr.rollback();
		   }
       }
       catch ( Exception ex ) { 
           throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
           if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
           if ( pstmt3 != null )  { try { pstmt3.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }
       return isOk;
   }
   
    /**
    ���� 
    @param box      receive from the form object and session
    @return int
    */     
   public int updateQna(RequestBox box) throws Exception { 
      DBConnectionManager connMgr     = null;
      ListSet ls                  = null;
      PreparedStatement pstmt2    = null;
      String sql1                 = "";
      String sql2                 = "";
      int isOk                    = 0;
      String v_user_id            = box.getSession("userid");
      String v_subj               = box.getString("p_subj");          // ����
      String v_year               = box.getString("p_year");          // �⵵
      String v_subjseq            = box.getString("p_subjseq");       // ������
      String v_lesson              = box.getString("p_lesson");
      int v_seq                   = box.getInt("p_seq");
      int v_kind                 = box.getInt("p_kind");
      String v_title              = box.getString("p_title");
      String v_contents           = box.getString("p_contents");
      int  i = 1;

      // String v_replygubun = getReplyGubun(box);
      String v_replygubun = "";

      try { 
          connMgr = new DBConnectionManager();
          sql2  = "update TZ_QNA set title=?,contents=?,inuserid=?,replygubun=?,luserid=?,ldate=to_char(sysdate, 'YYYYMMDDHH24MISS') ";
          sql2 += "where subj=? and year=? and subjseq=? and seq=? and kind=? ";
          pstmt2 = connMgr.prepareStatement(sql2);
//          System.out.println("sql2 ==  ==  ==  ==  == > " +sql2);

          pstmt2.setString(1,v_title);
//          pstmt2.setString(i++,v_contents);
          pstmt2.setCharacterStream(2,  new StringReader(v_contents), v_contents.length() );
          pstmt2.setString(3,v_user_id);
          pstmt2.setString(4,v_replygubun);
          pstmt2.setString(5,v_user_id);
          pstmt2.setString(6,v_subj);
          pstmt2.setString(7,v_year);
          pstmt2.setString(8,v_subjseq);
//          pstmt2.setString(i++,v_lesson);
          pstmt2.setInt(9,v_seq);
          pstmt2.setInt(10,v_kind);
          isOk = pstmt2.executeUpdate();

      }
      catch ( Exception ex ) { 
          throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
      }
      finally { 
          if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
          if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
          if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
      }
      return isOk;
  }


    /**
    ���� 
    @param box      receive from the form object and session
    @return int
    */  
  public int deleteQna(RequestBox box) throws Exception { 
     DBConnectionManager connMgr     = null;
     ListSet ls                  = null;
     PreparedStatement pstmt2    = null;
     String sql1                 = "", sql = "";
     int isOk                    = 0;
     String v_user_id            = box.getSession("userid");
     String v_subj               = box.getString("p_subj");          // ����
     String v_year               = box.getString("p_year");          // �⵵
     String v_subjseq            = box.getString("p_subjseq");       // ������
     String v_lesson               = box.getString("p_lesson");
     int v_seq                   = box.getInt("p_seq");
     int v_kind                 = box.getInt("p_kind");

     int  i = 1;
     int  v_cnt = 0;

     try { 
         connMgr = new DBConnectionManager();
         connMgr.setAutoCommit(false);
         sql1  = "delete TZ_QNA where subj=? and year=? and subjseq=? and seq=? and kind=? ";
         pstmt2 = connMgr.prepareStatement(sql1);

         pstmt2.setString(i++,v_subj);
         pstmt2.setString(i++,v_year);
         pstmt2.setString(i++,v_subjseq);
         pstmt2.setInt(i++,v_seq);
         pstmt2.setInt(i++,v_kind);
         isOk = pstmt2.executeUpdate();
         
         sql  = " select count(*) ";
         sql += "   from tz_qna ";
         sql += "   where subj='" +v_subj + "'";
         sql += "     and year='" +v_year + "'";
         sql += "     and subjseq='" +v_subjseq + "'";
         sql += "     and seq=" +v_seq;
         sql += "     and kind='1'";
         
         ls = connMgr.executeQuery(sql);
         
         if ( ls.next() ) { 
         		v_cnt = ls.getInt(1);
        	}
        	ls.close();
         
         	if ( v_cnt == 0) { 
         		sql  = "update TZ_QNA";
         		sql += "   set replyuserid='',";
         		sql += "       replyusernm='',";
         		sql += "       replydate=''";
         		sql += " where subj=" +SQLString.Format(v_subj);
         		sql += "   and year=" +SQLString.Format(v_year);
         		sql += "   and subjseq=" +SQLString.Format(v_subjseq);
         		sql += "   and seq=" +v_seq;
         		sql += "   and kind='0'";
         		isOk = connMgr.executeUpdate(sql);
         	} else { 
         		isOk=1;	
        	}
         
         if ( isOk > 0 ) { 
         		connMgr.commit();	
        	} else { 
        		connMgr.rollback();
        	}

     }
     catch ( Exception ex ) { 
         throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
     }
     finally { 
         if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
         if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
         if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
         if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
     }
     return isOk;
 }

    /**
    �亯�ڱ��� ���ϱ�(1:�н���, 2:����, 3:���)
    @param box      receive from the form object and session
    @return result  �亯�� ���а�
    */
    public String getReplyGubun(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls1         = null;
       String sql1         = "";
       String result       = null;
       int    cnt          = 0;
       String v_subj               = box.getString("p_subj");          // ����

       String s_user_id            = box.getSession("userid");
       String s_gadmin = box.getSession("gadmin");
       String v_gadmin_gubun = StringManager.substring(s_gadmin,0,1);

       // 1 : �н���, 2 : ����,  3 : ���
       if ( v_gadmin_gubun.equals("A")||v_gadmin_gubun.equals("H")||v_gadmin_gubun.equals("K") ) { 
           result = "3";
       } else if ( v_gadmin_gubun.equals("Z") ) { 
           result = "1";
       } else if ( v_gadmin_gubun.equals("F")||v_gadmin_gubun.equals("P") ) {          // ������������ üũ
           try { 
                connMgr = new DBConnectionManager();

                sql1  = " select count(*) cnt from TZ_GADMIN  ";
                sql1 += "  where userid = " + StringManager.makeSQL(s_user_id);
                sql1 += "    and gadmin = " + StringManager.makeSQL(s_gadmin);
                sql1 += "    and subj   = " + StringManager.makeSQL(v_subj);

                ls1 = connMgr.executeQuery(sql1);
                if ( ls1.next() ) cnt = ls1.getInt("cnt");

                if ( cnt > 0 ) {                                      // ���������ϰ��
                    if ( v_gadmin_gubun.equals("H")) result = "3";   // ����������ϰ�� = 3
                    else                            result = "2";   // �����ϰ��         = 2
                } else { 
                    result = "1";
                }
            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sql1);
                throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return result;
    }
    
    

    /**
    �������� ��ü ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectQnaAllList(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls1         = null;
       ArrayList list1     = null;
       String sql1         = "";
       String sql2         = "";       
       QnaData data1  = null;
       
       String  ss_grcode    = box.getStringDefault("s_grcode","ALL");        // �����׷�

	   try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            if ( !ss_grcode.equals("ALL")) sql2 = " and b.grcode='" +ss_grcode + "' ";
            sql1 = "	select a.seq, a.title, a.kind, a.inuserid, a.indate, b.scsubjnm, a.subj, a.year, a.subjseq, a.lesson ";
            sql1 += "	from TZ_QNA a, VZ_SCSUBJSEQ b ";
            sql1 += "	where a.subj=b.scsubj  " + sql2;
            sql1 += "    order by a.subj, a.year, a.subjseq, a.lesson, a.seq, a.kind ";

            ls1 = connMgr.executeQuery(sql1);
            while ( ls1.next() ) { 
                data1 = new QnaData();
                data1.setSeq( ls1.getInt("seq") );
                data1.setTitle( ls1.getString("title") );
				data1.setKind( ls1.getInt("kind") );
                data1.setInuserid( ls1.getString("inuserid") );
                data1.setIndate( ls1.getString("indate") );
                data1.setSubjnm( ls1.getString("scsubjnm") );
                data1.setSubj( ls1.getString("subj") );          
                data1.setYear( ls1.getString("year") );                             
                data1.setSubjseq( ls1.getString("subjseq") );
                data1.setLesson( ls1.getString("lesson") );                       
                list1.add(data1);
            }
            ls1.close();

      } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
	/**
	 * ���� �亯��Ͻ� �ش� ����CM��, �л����� �ڵ� SMS�߼��� ������ ���� ����
	 * @param box
	 * @return
	 * @throws Exception
	 */  
    public ArrayList getReceiver(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            int v_tabseq = box.getInt("p_tabseq");
            int v_refseq = box.getInt("p_refseq");
            String v_subjnm = GetCodenm.get_subjnm(v_subj);
            
            sql = "\n select a.userid, c.name, c.handphone "
            	+ "\n from   tz_manager a, tz_subjman b, tz_member c "
            	+ "\n where  a.gadmin like 'F%' "
            	+ "\n and    b.subj = " + StringManager.makeSQL(v_subj)
            	+ "\n and    a.gadmin = b.gadmin "
            	+ "\n and    a.userid = b.userid "
            	+ "\n and    b.userid = c.userid "
            	+ "\n union "
            	+ "\n select a.userid, b.name, b.handphone "
            	+ "\n from   tz_board a, tz_member b "
            	+ "\n where  a.tabseq = " + v_tabseq
            	+ "\n and    a.seq = " + v_refseq
            	+ "\n and    a.userid = b.userid ";
            
            ls = connMgr.executeQuery(sql);

            Vector vector1 = new Vector();
            Vector vector2 = new Vector();
            Vector vector3 = new Vector();
            while ( ls.next() ) { 
            	vector1.add(ls.getString("userid"));
            	vector2.add(ls.getString("name"));
            	vector3.add(ls.getString("handphone"));
             
            	dbox = ls.getDataBox();
                list.add(dbox);
            }
            box.put("p_checks", vector1);
            box.put("p_names", vector2);
            box.put("p_handphone", vector3);
            box.put("p_title", v_subjnm + "������ �亯�� ��ϵǾ����ϴ�.");
            
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
     * ���õ� �ڷ� �󼼳��� ����
     * @param box      receive from the form object and session
     * @return isOk    1:update success,0:update fail
     * @throws Exception
     */
 	public int updateBoardForTutorQna(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        String sql = "";

        int isOk = 0;
        int v_tabseq   = box.getInt("p_tabseq");
        int v_seq      = box.getInt("p_seq");
        String v_title     = box.getString("p_title");
        // String v_content =  StringManager.replace(box.getString("content"),"<br > ","\n"); 
        String v_content   = box.getString("p_content");
        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql = "update tz_board "
            	+ "set    title = ? "
            	+ "     , content=? "
//            	+ "     , content=empty_clob() "
            	+ "     , userid = ? "
            	+ "     , name = ? "
            	+ "     , luserid = ? "
            	+ "     , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') "
            	+ "where  tabseq = ? "
            	+ "and    seq = ?";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_title);
            pstmt.setString(2, v_content);
            pstmt.setString(3, s_userid);
            pstmt.setString(4, s_usernm);
            pstmt.setString(5, s_userid);
            pstmt.setInt(6, v_tabseq);
            pstmt.setInt(7, v_seq);

            isOk = pstmt.executeUpdate();

            sql = "select content from tz_board where tabseq = " + v_tabseq + " and seq = " + v_seq ;
//            connMgr.setOracleCLOB(sql, v_content);       //      clob �ʵ忡 ��Ʈ���� �̿�,  data �� �ִ´�(Weblogic �� ���)

 			if ( isOk > 0 )	{ 
 				connMgr.commit();
 			} else {
 				connMgr.rollback();
 			}
        }
        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
 	/**
 	 * ���õ� �Խù� ����
 	 * @param box      receive from the form object and session
 	 * @return isOk    1:delete success,0:delete fail
 	 * @throws Exception
 	 */
 	public int deleteBoardForTutorQna(RequestBox box) throws Exception { 
 		DBConnectionManager	connMgr	= null;
 		PreparedStatement pstmt = null;
 		String sql = "";
 		int isOk = 0;

 		int v_tabseq = box.getInt("p_tabseq");
 		int v_seq = box.getInt("p_seq");

 		BoardBean bbean = new BoardBean();
 		
       	// �亯 ���� üũ(�亯 ������ �����Ұ�)
 		if ( bbean.selectBoard(v_tabseq, v_seq) == 0 ) { 

 			try { 
 				connMgr = new DBConnectionManager();
 				connMgr.setAutoCommit(false);

 				sql = "delete from tz_board where tabseq = ? and seq = ? ";
 				pstmt = connMgr.prepareStatement(sql);
 				pstmt.setInt(1, v_tabseq);
 				pstmt.setInt(2, v_seq);
 				isOk = pstmt.executeUpdate();

               if ( isOk > 0 ) { 
                   connMgr.commit();
               } else {
            	   connMgr.rollback();
               }

 			}
 			catch ( Exception ex ) { 
 			    connMgr.rollback();
 			    ErrorManager.getErrorStackTrace(ex, box,    sql);
 			    throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
 			}
 			finally { 
 			    if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
 			    if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
 			    if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
 			}
 		} else {
 			isOk = -1;
 		}
 		return isOk;
 	}

}
