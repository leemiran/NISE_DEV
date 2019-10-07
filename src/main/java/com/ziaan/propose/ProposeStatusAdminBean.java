// **********************************************************
//  1. ��      ��: PROPOSE STATUS ADMIN BEAN
//  2. ���α׷���: ProposeStatusAdminBean.java
//  3. ��      ��: ��û ��Ȳ ������ bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��:
//  7. ��      ��:
// **********************************************************
package com.ziaan.propose;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.system.ManagerAdminBean;

public class ProposeStatusAdminBean { 
    private ConfigSet config;
    private int row;

    public ProposeStatusAdminBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );  // �� ����� �������� row ���� �����Ѵ�
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    ��û��� ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectProposeMemberList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;        
        ArrayList list1     = null;
        ArrayList list2     = null;        

        String sql1         = "";
        String sql2         = "";
        
        ProposeStatusData data1= null;
        ProposeStatusData data2= null;  
        
        String  v_Bcourse   = ""; // �����ڽ�
        String  v_course    = ""; // �����ڽ�
        String  v_Bcourseseq= ""; // �����ڽ����
        String  v_courseseq = ""; // �����ڽ����
        String  v_Buserid   = "";
        String  v_userid    = "";
        
        
        int v_pageno = box.getInt("p_pageno");
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // �����׷�
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // �⵵
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // �������
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // ����з�
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// ����&�ڽ�
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // ���� ���
        String  ss_company  = box.getStringDefault("s_company","ALL");   // ȸ��
        String  ss_edustart = box.getStringDefault("s_edustart","");  // ����������
        String  ss_eduend   = box.getStringDefault("s_eduend","");    // ����������
        String  ss_action   = box.getString("s_action");
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name �˻�

        String  v_orderColumn= box.getString("p_orderColumn");           // ������ �÷���
        String  v_orderType     = box.getString("p_orderType");                 // ������ ����

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();                

                sql1 = "select                                                  \n" +
	                "    c.grseq,                                               \n" +
	                "    c.course,                                              \n" +
	                "    c.cyear,                                               \n" +
	                "    c.courseseq,                                           \n" +
	                "    c.coursenm,                                            \n" +
	                "    c.subj,                                                \n" +
	                "    c.year,                                                \n" +
	                "    c.subjnm,                                              \n" +
	                "    c.subjseq,                                             \n" +
	                "    c.subjseqgr,                                           \n" +
                    "    get_codenm('0004',c.isonoff) isonoff,                  \n" +
	                "    c.edustart,                                            \n" +
	                "    c.eduend,                                              \n" +
	                "    a.isproposeapproval,                                   \n" +
	                "    a.appdate,                                             \n" +
	                "    a.chkfirst,                                            \n" +
	                "    a.chkfinal,                                            \n" +
	                "    b.userid,                                              \n" +
	                "    b.name,                                                \n" +
	                "    b.email,                                   			\n" +
	                "    b.position_nm,                                   	    \n" +
	                "    b.lvl_nm,                                   			\n" +
	                "    c.biyong,                                   			\n" +
	                "    c.goyongpricemajor,                                   			\n" +
	                "    c.goyongpriceminor,                                   			\n" +
					"    get_compnm(b.comp) companynm, 							\n"	+                
	                "    (select                                                \n" +
	                "         grseqnm                                           \n" +
	                "     from                                                  \n" +
	                "         tz_grseq                                          \n" +
	                "     where                                                 \n" +
	                "             grcode = c.grcode                             \n" +
	                "         and gyear  = c.gyear                              \n" +
	                "         and grseq  = c.grseq                              \n" +
	                "     )                                  grseqnm            \n" +
	                "from                                                       \n" +
	                "    tz_propose                          a,                 \n" +
	                "    tz_member                           b,                 \n" +
	                "    vz_scsubjseq                        c                  \n" +
	                "where                                                      \n" +
	                "    1 = 1                                                  \n";

                if ( !ss_grcode.equals("ALL"))     sql1 += "and    c.grcode = " +SQLString.Format(ss_grcode) + " \n";
                if ( !ss_grseq.equals("ALL"))      sql1 += "and    c.grseq = " +SQLString.Format(ss_grseq) + " \n";
                if ( !ss_uclass.equals("ALL"))     sql1 += "and    c.scupperclass = " +SQLString.Format(ss_uclass) + " \n";
                if ( !ss_subjcourse.equals("ALL")) sql1 += "and    c.scsubj = " +SQLString.Format(ss_subjcourse) + " \n";
                if ( !ss_subjseq.equals("ALL"))    sql1 += "and    c.scsubjseq = " +SQLString.Format(ss_subjseq) + " \n";
                if ( !ss_company.equals("ALL"))    sql1 += "and    b.comp = " +SQLString.Format(ss_company) + " \n";
                if ( !ss_edustart.equals(""))      sql1 += "and    c.edustart >= " +SQLString.Format(ss_edustart + "00") + " \n";    // �ڸ��� ����
                if ( !ss_eduend.equals(""))        sql1 += "and    c.eduend <= " +SQLString.Format(ss_eduend + "00") + " \n";

                // ���������ϰ� �������� ���õ��� ���� ��쿡�� �����⵵���� �˻���
                if ( ss_edustart.equals("") && ss_eduend.equals("")) sql1 += "and    c.gyear = " +SQLString.Format(ss_gyear);

                //sql1 += "and    nvl(A.cancelkind,' ') not in ('P','F','D') 			\n";
                sql1 += "and    a.chkfinal = 'Y' 			                    \n";
                sql1 += "and    a.userid = b.userid 							\n";
                sql1 += "and    a.subj   = c.subj 								\n";
                sql1 += "and    a.year   = c.year 								\n";
                sql1 += "and    a.subjseq=c.subjseq 							\n";

                if ( v_orderColumn.equals("grseq"))   v_orderColumn = "c.grseq";
                if ( v_orderColumn.equals("subj"))    v_orderColumn = "c.subj";
                if ( v_orderColumn.equals("userid"))  v_orderColumn = "b.userid ";
                if ( v_orderColumn.equals("name"))    v_orderColumn = "b.name ";
                if ( v_orderColumn.equals("jiknm"))   v_orderColumn = "b.post_nm";

                if ( !v_searchtxt.equals("ZZZ") ) { 
	            	sql1 += "   and (b.userid like  '%" +v_searchtxt + "%' or b.name like '%" +v_searchtxt+"%') \n";
	            } 
                
                //if ( v_orderColumn.equals("") ) { 
                //    sql1 += " order  by c.subj, c.year, c.subjseq";
                //} else { 
                //    sql1 += " order  by " + v_orderColumn + v_orderType;
                //}
                
                if ( v_orderColumn.equals("") ) { 
                    sql1 += " order by C.course, C.cyear, C.courseseq, B.userid, C.subj,C.year,C.subjseq";
                } else { 
                    sql1 += " order by C.course, C.cyear, C.courseseq, B.userid, " + v_orderColumn + v_orderType;
                }
             System.out.println("===sql1====\n "+ sql1) ;
             
                ls1 = connMgr.executeQuery(sql1);

                ls1.setPageSize(row);                       // �������� row ������ �����Ѵ�
                ls1.setCurrentPage(v_pageno);               // ������������ȣ�� �����Ѵ�.
                int total_page_count = ls1.getTotalPage();  // ��ü ������ ���� ��ȯ�Ѵ�
                int total_row_count = ls1.getTotalCount();  // ��ü row ���� ��ȯ�Ѵ�

                while ( ls1.next() ) { 
                    data1 = new ProposeStatusData();
                    data1.setGrseq( ls1.getString("grseq") );
                    data1.setGrseqnm( ls1.getString("grseqnm") );
                    data1.setCourse( ls1.getString("course") );
                    data1.setCyear( ls1.getString("cyear") );
                    data1.setCourseseq( ls1.getString("courseseq") );
                    data1.setCoursenm( ls1.getString("coursenm") );
                    data1.setSubj( ls1.getString("subj") );
                    data1.setYear( ls1.getString("year") );
                    data1.setSubjseq( ls1.getString("subjseq") );
                    data1.setSubjseqgr( ls1.getString("subjseqgr") );
                    data1.setSubjnm( ls1.getString("subjnm") );
                    data1.setUserid( ls1.getString("userid") );
                    data1.setName( ls1.getString("name") );
                    data1.setPosition_nm( ls1.getString("position_nm") );
                    data1.setLvl_nm( ls1.getString("lvl_nm") );
                    data1.setCompanynm( ls1.getString("companynm") );                    
                    data1.setAppdate( ls1.getString("appdate") );
                    data1.setEdustart( ls1.getString("edustart") );
                    data1.setEduend( ls1.getString("eduend") );
                    data1.setIsproposeapproval( ls1.getString("isproposeapproval") );
                    data1.setChkfirst( ls1.getString("chkfirst") );
                    data1.setChkfinal( ls1.getString("chkfinal") );
                    data1.setEmail( ls1.getString("email") );
                    data1.setIsonoff( ls1.getString("isonoff") );
                    data1.setBiyong( ls1.getString("biyong") );
                    data1.setGoyongpricemajor( ls1.getString("goyongpricemajor") );
                    data1.setGoyongpriceminor( ls1.getString("goyongpriceminor") );
                    
                    
                    data1.setDispnum(total_row_count - ls1.getRowNum() + 1);
                    data1.setTotalPageCount(total_page_count);                    
                    data1.setRowCount(row);
                    list1.add(data1);

                }
                
                for(int i=0;i < list1.size(); i++){
                    data2       =   (ProposeStatusData)list1.get(i);
                    v_course    =   data2.getCourse();
                    v_courseseq =   data2.getCourseseq();
                    v_userid    =   data2.getUserid();
                    if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq) && v_Buserid.equals(v_userid))){
                        sql2 = "select count(*) cnt from VZ_SCSUBJSEQ A,TZ_PROPOSE B,TZ_MEMBER C ";                        
                        sql2+= "where A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq ";
                        sql2+= "and B.userid=C.userid ";
            			if (!ss_grcode.equals("ALL")) {
            				sql2+= " and A.grcode = "+SQLString.Format(ss_grcode);
            			}
            			if (!ss_grseq.equals("ALL")) {
            				sql2+= " and A.grseq = "+SQLString.Format(ss_grseq);
            			}
            			if (!ss_uclass.equals("ALL")) {
            			    sql2+= " and A.scupperclass = "+SQLString.Format(ss_uclass);
            			}
            			
            			if (!ss_subjcourse.equals("ALL")) {
            				sql2+= " and A.scsubj = "+SQLString.Format(ss_subjcourse);
            			}
            			if (!ss_subjseq.equals("ALL")) {
            				sql2+= " and A.scsubjseq = "+SQLString.Format(ss_subjseq);
            			}            
            			if ( !ss_company.equals("ALL"))    
                            sql2+= " and C.comp = "+SQLString.Format(ss_company);
            			
            			//���������ϰ� �������� ���õ��� ���� ��쿡�� �����⵵���� �˻���
            			if (ss_edustart.equals("ALL") && ss_eduend.equals("ALL")) { 
            				sql2+= " and A.gyear = "+SQLString.Format(ss_gyear);
            			}
                        sql2+= "and A.course = "+SQLString.Format(v_course)+" and A.courseseq = "+SQLString.Format(v_courseseq)
                            + "and B.userid = "+SQLString.Format(v_userid);
                        ls2 = connMgr.executeQuery(sql2);	
                        if(ls2.next()){
                            data2.setRowspan(ls2.getInt("cnt"));
                            data2.setIsnewcourse("Y");
                        }
                    }else{
                        data2.setRowspan(0);
                        data2.setIsnewcourse("N");
                    }
                    v_Bcourse   =   v_course;
                    v_Bcourseseq=   v_courseseq;
                    v_Buserid   =   v_userid;
                    list2.add(data2);
                    if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
                }    
                

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

    /**
    ��û��Ҹ�� ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectProposeCancelMemberList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        String sql1         = "";
        ProposeStatusData data1= null;
        int v_pageno = box.getInt("p_pageno");
        String  ss_grcode     = box.getStringDefault("s_grcode","ALL");    // �����׷�
        String  ss_gyear      = box.getStringDefault("s_gyear","ALL");     // �⵵
        String  ss_grseq      = box.getStringDefault("s_grseq","ALL");     // �������
        String  ss_uclass     = box.getStringDefault("s_upperclass","ALL");    // ����з�
        String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");// ����&�ڽ�
        String  ss_subjseq    = box.getStringDefault("s_subjseq","ALL");   // ���� ���
        String  ss_company    = box.getStringDefault("s_company","ALL");   // ȸ��
        String  ss_edustart   = box.getStringDefault("s_edustart","");  // ����������
        String  ss_eduend     = box.getStringDefault("s_eduend","");    // ����������
        String  ss_action     = box.getString("s_action");
        String  v_orderColumn = box.getString("p_orderColumn");           // ������ �÷���
        String  v_orderType     = box.getString("p_orderType");                 // ������ ����
        
        //�˻� �߰�
        String v_gubun = box.getString("p_gubun");
        String v_key1 = box.getString("p_key1");
        String v_search_payType = box.getString("p_search_payType");
        
        ManagerAdminBean bean = null;
        String  v_sql_add   = "";
        String  v_userid    = box.getSession("userid");
        String  s_gadmin    = box.getSession("gadmin");
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name �˻�

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

/* 2006.06.16 - ���� 
                // select grseq,course,cyear,courseseq,coursenm,subj,year,subjnm,subjseq,compnm,
                // jikwinm,userid,cono,name,canceldate,cancelkind,reason,email,ismailing,isonoff
                sql1 = "select C.grseq,C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq, C.subjseqgr, ";
                sql1 += "       get_compnm(B.comp,2,2) companynm, get_deptnm(B.deptnam,'') compnm,get_jikwinm(B.jikwi,B.comp) jikwinm,  ";
                sql1 += "       get_jikupnm(B.jikup,B.comp,B.jikupnm) jikupnm, B.userid,B.cono,B.name,A.canceldate,A.cancelkind,A.reason, ";
                sql1 += "       B.email,B.ismailing,C.isonoff,  ";
                sql1 += "       (select count(*) from tz_sangdam where subj=C.subj and year=C.year and subjseq=C.subjseq and userid = d.userid) csangcnt, ";
                sql1 += "       (select grseqnm from tz_grseq where grcode=c.grcode and gyear=c.gyear and grseq = c.grseq) grseqnm ";
                // sql1 += "from TZ_CANCEL A,TZ_MEMBER B,VZ_SCSUBJSEQ C, TZ_PROPOSE D ";
                sql1 += "from TZ_CANCEL A,TZ_MEMBER B,VZ_SCSUBJSEQ C ";
                // sql1 += " where a.subj=d.subj and a.year=d.year and a.subjseq=d.subjseq and a.userid=d.userid and d.cancelkind='P'  ";
                sql1 += " where A.userid=B.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq ";
*/

                sql1 = "select      c.grseq                                            	\n" +
		                "      ,	c.course                                           	\n" +
		                "      ,	c.cyear                                            	\n" +
		                "      ,	c.courseseq                                        	\n" +
		                "      ,	c.coursenm                                         	\n" +
		                "      ,	c.subj                                             	\n" +
		                "      ,	c.year                                             	\n" +
		                "      ,	c.subjnm                                           	\n" +
		                "      ,	c.subjseq                                          	\n" +
		                "      ,	c.subjseqgr                                        	\n" +
		                "      ,	c.isonoff                                          	\n" +
		                "      ,	a.canceldate                                       	\n" +
		                "      ,	a.cancelkind                                       	\n" +
		                "      ,	a.reason                                           	\n" +
		                "      ,	b.userid                                           	\n" +
		                "      ,	b.name                                             	\n" +
		                "      ,	b.email                                				\n" +
		                "      ,	get_compnm(b.comp) companynm           				\n" +                       
		                "      ,(select count(*)                                    	\n" +
		                "     	from   tz_sangdam                                  	\n" +
		                "     	where  subj    = c.subj                           	\n" +
		                "        and    year    = c.year                           	\n" +
		                "        and    subjseq = c.subjseq                        	\n" +
		                "        and    userid  = b.userid                         	\n" +
		                "       ) as csangcnt    									\n" +
		                "      ,(select grseqnm                                      \n" +
		                "        from   tz_grseq                                     \n" +
		                "        where  grcode = c.grcode                          	\n" +
		                "        and    gyear  = c.gyear                           	\n" +
		                "        and    grseq  = c.grseq                           	\n" +
		                "       ) as grseqnm      									\n" +
		                "       ,(select order_id from pa_payment where order_id=a.order_id)as order_id \n" +
		                "       ,(select type from pa_payment where order_id=a.order_id) as paycd \n" +
		                "       ,(select decode(trim(type), 'OB','����û�ϰ�����','PB','������','SC0010','�ſ�ī��','SC0030','������ü','SC0040','�������',type) from pa_payment where order_id=a.order_id) as paynm \n" +
		                "       ,(select amount from pa_payment where order_id=a.order_id) as amount \n" +
		                "       ,(select to_char(to_date(enter_dt,'yyyymmdd'),'yyyy-mm-dd') as enter_dt from pa_payment where order_id=a.order_id) as enter_dt \n" +
		                "       ,(select decode(enter_dt,null,'N','','N','Y') as enter_yn from pa_payment where order_id=a.order_id) as enter_yn \n" +
		                "       ,(select to_char(to_date(repay_dt,'yyyymmdd'),'yyyy-mm-dd') as repay_dt from pa_payment where order_id=a.order_id) as repay_dt \n" +
		                "       ,(select decode(repay_dt,null,'N','','N','Y') as repay_yn from pa_payment where order_id=a.order_id)as repay_yn \n" +
		                "from    tz_cancel    a                                      \n" +
		                "      , tz_member    b                                      \n" +
		                "      , vz_scsubjseq c                                      \n" +
		                "where   a.userid  = b.userid                            	\n" +
		                "and     a.subj    = c.subj                              	\n" +
		                "and     a.year    = c.year                              	\n" +
		                "and     a.subjseq = c.subjseq                           	\n";
		         
		         if ( !ss_grcode.equals("ALL"))     sql1 += "and    c.grcode = " +SQLString.Format(ss_grcode);
		         if ( !ss_grseq.equals("ALL"))      sql1 += "and    c.grseq = " +SQLString.Format(ss_grseq);
		         if ( !ss_gyear.equals("ALL"))      sql1 += "and    c.gyear = " +SQLString.Format(ss_gyear);
		         if ( !ss_uclass.equals("ALL"))     sql1 += "and    c.scupperclass = " +SQLString.Format(ss_uclass);
		         if ( !ss_subjcourse.equals("ALL")) sql1 += "and    c.scsubj = " +SQLString.Format(ss_subjcourse);
		         if ( !ss_subjseq.equals("ALL"))    sql1 += "and    c.scsubjseq = " +SQLString.Format(ss_subjseq);
		         if ( !ss_company.equals("ALL"))    sql1 += "and    b.comp = " +SQLString.Format(ss_company);
		         if ( !ss_edustart.equals(""))      sql1 += "and    c.edustart >= " +SQLString.Format(ss_edustart + "00");
		         if ( !ss_eduend.equals(""))        sql1 += "and    c.eduend <= " +SQLString.Format(ss_eduend + "00");
		
		         // �μ����ϰ��
		         if ( s_gadmin.equals("K7") ) { 
		             bean = new ManagerAdminBean();
		             v_sql_add   = bean.getManagerDept(v_userid, s_gadmin);
		             if ( !v_sql_add.equals("")) sql1 += " and B.comp in " + v_sql_add;       // �����μ��˻���������
		         }
		
		         if ( v_orderColumn.equals("grseq"))   v_orderColumn = "c.grseq";
		         if ( v_orderColumn.equals("subj"))    v_orderColumn = "c.subj";
		         //if ( v_orderColumn.equals("compnm1")) v_orderColumn = "get_compnm(b.comp,2,2)";
		         //if ( v_orderColumn.equals("compnm2")) v_orderColumn = "get_deptnm(b.deptnam,'')";
		         if ( v_orderColumn.equals("userid"))  v_orderColumn = "b.userid ";
		         if ( v_orderColumn.equals("name"))    v_orderColumn = "b.name ";
		         if ( v_orderColumn.equals("ldate"))    v_orderColumn = "a.canceldate ";
		         //if ( v_orderColumn.equals("jiknm"))   v_orderColumn = "b.lvl_nm";
		
		         if ( !v_searchtxt.equals("ZZZ") ) { 
		         	sql1 += "   and (b.userid like  '%" +v_searchtxt + "%' or b.name like '%" +v_searchtxt+"%') \n";
		         }
		         //String v_gubun = box.getString("p_gubun");
		         //String v_key1 = box.getString("p_key1");
		         //String v_search_payType = box.getString("p_search_payType");
		         if(!"".equals(v_search_payType)){
		        	 sql1 += " and (select trim(type) from pa_payment where order_id=a.order_id) = '"+v_search_payType+"' \n" ;		        	 
		         }
		         
		         if("name".equals(v_gubun)){
		        	 sql1 += "   and b.name like  '%" +v_key1 + "%' \n";
		         }else if("userid".equals(v_gubun)){
		        	 sql1 += "   and b.userid like  '%" +v_key1 + "%' \n";
		         }else if("birth_date".equals(v_gubun)){
		        	 sql1 += "   and substr(fn_crypt('1', b.birth_date, 'knise'),0,6) =  '" +v_key1 + "' \n";
		         }else if("handphone".equals(v_gubun)){
		        	 sql1 += "   and replace(b.handphone,'-','') like  '%" +v_key1.replaceAll("-","") + "%' \n";
		         }else if("user_path".equals(v_gubun)){	 
		        	 sql1 += "   and b.userid like  '%" +v_key1 + "%' \n";
		         }
		         
		         if ( v_orderColumn.equals("") ) { 
		             sql1 += " order by a.canceldate, c.subj, c.year, c.subjseq";
		         } else { 
		             sql1 += " order by " + v_orderColumn + v_orderType;
		         }
	System.out.println(sql1);	
		         ls1 = connMgr.executeQuery(sql1);
		         ls1.setPageSize(row);                       // �������� row ������ �����Ѵ�
		         ls1.setCurrentPage(v_pageno);               // ������������ȣ�� �����Ѵ�.
		         int total_page_count = ls1.getTotalPage();  // ��ü ������ ���� ��ȯ�Ѵ�
		         int total_row_count = ls1.getTotalCount();  // ��ü row ���� ��ȯ�Ѵ�
		         while ( ls1.next() ) { 
		             data1 = new ProposeStatusData();
		             data1.setGrseq( ls1.getString("grseq") );
		             data1.setGrseqnm( ls1.getString("grseqnm") );
		             data1.setCourse( ls1.getString("course") );
		             data1.setCyear( ls1.getString("cyear") );
		             data1.setCourseseq( ls1.getString("courseseq") );
		             data1.setCoursenm( ls1.getString("coursenm") );
		             data1.setSubj( ls1.getString("subj") );
		             data1.setYear( ls1.getString("year") );
		             data1.setSubjseq( ls1.getString("subjseq") );
		             data1.setSubjseqgr( ls1.getString("subjseqgr") );
		             data1.setSubjnm( ls1.getString("subjnm") );
		             data1.setUserid( ls1.getString("userid") );
		             data1.setName( ls1.getString("name") );
		             data1.setCanceldate( ls1.getString("canceldate") );
		             data1.setCancelkind( ls1.getString("cancelkind") );
		             data1.setReason( ls1.getString("reason") );
		             data1.setEmail( ls1.getString("email") );
		             data1.setIsonoff( ls1.getString("isonoff") );
		             data1.setCompanynm( ls1.getString("companynm") );
		             data1.setOrder_id( ls1.getString("order_id") );
		             data1.setPaycd( ls1.getString("paycd") );
		             data1.setPaynm( ls1.getString("paynm") );
		             data1.setAmount( ls1.getString("amount") );
		             data1.setEnter_dt( ls1.getString("enter_dt") );
		             data1.setEnter_yn( ls1.getString("enter_yn") );
		             data1.setRepay_dt( ls1.getString("repay_dt") );
		             data1.setRepay_yn( ls1.getString("repay_yn") );
		             
		             data1.setDispnum(total_row_count - ls1.getRowNum() + 1);
		             data1.setTotalPageCount(total_page_count);
		             data1.setRowCount(row);
		
		             list1.add(data1);
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
    ��û �ο� ��ȸ ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectProposeMemberCountList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        ProposeStatusData data1= null;
        ProposeStatusData data2= null;
        String  v_Bcourse   = ""; // �����ڽ�
        String  v_course    = ""; // �����ڽ�
        String  v_Bcourseseq= ""; // �����ڽ����
        String  v_courseseq = ""; // �����ڽ����
        int v_pageno = box.getInt("p_pageno");
        String  ss_grcode     = box.getStringDefault("s_grcode","ALL");    // �����׷�
        String  ss_gyear      = box.getStringDefault("s_gyear","ALL");     // �⵵
        String  ss_grseq      = box.getStringDefault("s_grseq","ALL");     // �������
        String  ss_uclass     = box.getStringDefault("s_upperclass","ALL");    // ����з�
        String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");// ����&�ڽ�
        String  ss_subjseq    = box.getStringDefault("s_subjseq","ALL");   // ���� ���
        String  ss_edustart   = box.getStringDefault("s_edustart","");  // ����������
        String  ss_eduend     = box.getStringDefault("s_eduend","");    // ����������
        String  ss_action     = box.getString("s_action");
        String  v_orderColumn = box.getString("p_orderColumn");           // ������ �÷���

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();

                sql1  = " select grseq, course, cyear, coursenm, courseseq, subj \n";
                sql1 += "      , year, subjnm, subjseq, subjseqgr \n";
                sql1 += "	   , propstart, propend, edustart, eduend, studentlimit \n";
                sql1 += "	   ,(select grseqnm \n";
                sql1 += "        from   tz_grseq \n";
                sql1 += "        where  grcode = b.grcode \n";
                sql1 += "        and    gyear = b.gyear \n";
                sql1 += "        and    grseq = b.grseq) as grseqnm \n";
                sql1 += "      ,(select count(subj) \n";
                sql1 += "        from   tz_propose \n";
                sql1 += "        where  subj = b.subj \n";
                sql1 += "        and    year = b.year \n";
                sql1 += "        and    subjseq=b.subjseq) as procnt \n";
                sql1 += "      ,(select count(subj) \n";
                sql1 += "        from   tz_propose \n";
                sql1 += "        where  subj = b.subj \n";
                sql1 += "        and 	year = b.year \n";
                sql1 += "		 and    subjseq=b.subjseq \n";
                sql1 += "        and    cancelkind in('P','F')) as cancnt \n";
                sql1 += "      , b.isonoff \n";
                sql1 += " from   vz_scsubjseq b \n";
                sql1 += " where  1 = 1  \n";

                if ( !ss_grcode.equals("ALL") ) { 
                    sql1 += " and    b.grcode = " +SQLString.Format(ss_grcode) + " \n";
                }
                    sql1 += " and    b.gyear = " +SQLString.Format(ss_gyear) + " \n";
                if ( !ss_grseq.equals("ALL") ) { 
                    sql1 += " and    b.grseq = " +SQLString.Format(ss_grseq) + " \n";
                }
                if ( !ss_uclass.equals("ALL") ) { 
                    sql1 += " and    b.scupperclass = " +SQLString.Format(ss_uclass) + " \n";
                }
                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1 += " and    b.scsubj = " +SQLString.Format(ss_subjcourse) + " \n";
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1 += " and    b.scsubjseq = " +SQLString.Format(ss_subjseq) + " \n";
                }
                if ( !ss_edustart.equals("") ) { 
                    sql1 += " and    b.edustart >= " +SQLString.Format(ss_edustart + "00") + " \n";
                }
                if ( !ss_eduend.equals("") ) { 
                    sql1 += " and    b.eduend <= " +SQLString.Format(ss_eduend + "00") + " \n";
                }
                // ���������ϰ� �������� ���õ��� ���� ��쿡�� �����⵵���� �˻���
                if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                    sql1 += " and    b.gyear = " +SQLString.Format(ss_gyear) + " \n";
                }
                if ( !v_orderColumn.equals("") ) { 
                    v_orderColumn = "a." +v_orderColumn;
                    sql1 += " order  by b.course,b.cyear,b.courseseq,b.subj,b.year,b.subjseq," +v_orderColumn;
                } else { 
                    sql1 += " order  by b.course,b.cyear,b.courseseq,b.subj,b.year,b.subjseq ";
                }

                ls1 = connMgr.executeQuery(sql1);

                ls1.setPageSize(row);                       // �������� row ������ �����Ѵ�
                ls1.setCurrentPage(v_pageno);               // ������������ȣ�� �����Ѵ�.
                int total_page_count = ls1.getTotalPage();  // ��ü ������ ���� ��ȯ�Ѵ�
                int total_row_count = ls1.getTotalCount();  // ��ü row ���� ��ȯ�Ѵ�

                while ( ls1.next() ) { 
                    data1 = new ProposeStatusData();
                    data1.setGrseq( ls1.getString("grseq") );
                    data1.setGrseqnm( ls1.getString("grseqnm") );
                    data1.setCourse( ls1.getString("course") );
                    data1.setCyear( ls1.getString("cyear") );
                    data1.setCourseseq( ls1.getString("courseseq") );
                    data1.setCoursenm( ls1.getString("coursenm") );
                    data1.setSubj( ls1.getString("subj") );
                    data1.setYear( ls1.getString("year") );
                    data1.setSubjseq( ls1.getString("subjseq") );
                    data1.setSubjseqgr( ls1.getString("subjseqgr") );
                    data1.setSubjnm( ls1.getString("subjnm") );
                    data1.setPropstart( ls1.getString("propstart") );
                    data1.setPropend( ls1.getString("propend") );
                    data1.setEdustart( ls1.getString("edustart") );
                    data1.setEduend( ls1.getString("eduend") );
                    data1.setStudentlimit( ls1.getInt("studentlimit") );
                    data1.setProcnt( ls1.getInt("procnt") );
                    data1.setCancnt( ls1.getInt("cancnt") );
                    data1.setIsonoff( ls1.getString("isonoff") );
                    data1.setDispnum(total_row_count - ls1.getRowNum() + 1);
                    data1.setTotalPageCount(total_page_count);
                    data1.setRowCount(row);
                    list1.add(data1);
                }
                for ( int i = 0;i < list1.size(); i++ ) { 
                    data2       =   (ProposeStatusData)list1.get(i);
                    v_course    =   data2.getCourse();
                    v_courseseq =   data2.getCourseseq();

                    if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                        sql2 =  " select count(a.subj) cnt \n" +
                        		" from   vz_scsubjseq a \n" +
                        		" where  ";
                        if ( !ss_grcode.equals("ALL") ) { 
                            sql2 += " a.grcode = " +SQLString.Format(ss_grcode) + " \n" +  
                            		" and ";
                        }
                        if ( !ss_grseq.equals("ALL") ) { 
                            sql2 += " a.grseq = " +SQLString.Format(ss_grseq) + " \n" + 
                            		" and    ";
                        }
                        if ( !ss_uclass.equals("ALL") ) { 
                            sql2 += " a.scupperclass = " +SQLString.Format(ss_uclass) + " \n" + 
                            		" and    ";
                        }
                        if ( !ss_subjcourse.equals("ALL") ) { 
                            sql2 += " a.scsubj = " +SQLString.Format(ss_subjcourse) + " \n" +  
                            		" and    ";
                        }
                        if ( !ss_subjseq.equals("ALL") ) { 
                            sql2 += " a.scsubjseq = " +SQLString.Format(ss_subjseq) + " \n" + 
                            		" and    ";
                        }
                        if ( !ss_edustart.equals("") ) { 
                            sql2 += " a.edustart >= " +SQLString.Format(ss_edustart + "00") + " \n" + 
                            		" and    ";
                        }
                        if ( !ss_eduend.equals("") ) { 
                            sql2 += " a.eduend <= " +SQLString.Format(ss_eduend + "00") + " \n" + 
                            		" and    ";
                        }
                        // ���������ϰ� �������� ���õ��� ���� ��쿡�� �����⵵���� �˻���
                        if ( ss_edustart.equals("") && ss_eduend.equals("") ) { 
                            sql2 += " a.gyear = " +SQLString.Format(ss_gyear) + " \n" + 
                            		" and    ";
                        }
                        sql2 += " a.course = " +SQLString.Format(v_course) + " \n" +
                        		" and    a.courseseq = " +SQLString.Format(v_courseseq) + " \n";

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
    ������ �߼�
    @param box      receive from the form object and session
    @return int
    */
    public int sendFormMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int cnt = 0;    //  ���Ϲ߼��� ������ �����
        // p_checks�� �Ѿ�� �ټ��� value�� ó���ϱ� ���� vector�� ����
        Vector v_check1     = new Vector();
        Vector v_check2     = new Vector();
        Vector v_check3     = new Vector();
        Vector v_check4     = new Vector();
        v_check1            = box.getVector("p_checks");
        v_check2            = box.getVector("p_subj");
        v_check3            = box.getVector("p_year");
        v_check4           = box.getVector("p_subjseq");
        Enumeration em1     = v_check1.elements();
        Enumeration em2     = v_check2.elements();
        Enumeration em3     = v_check3.elements();
        Enumeration em4     = v_check4.elements();
        String v_userid     = "";
        String v_subj       = "";
        String v_year       = "";
        String v_subjseq    = "";
        String v_isonoff    = "";
        String v_edustart   = "";

        try { 
            connMgr = new DBConnectionManager();

//// //// //// //// ////  ������ �߼� //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            String v_sendhtml = "mail8.html";
            FormMail fmail = new FormMail(v_sendhtml);     //      �����Ϲ߼��� ���
            MailSet mset = new MailSet(box);        //      ���� ���� �� �߼�
            String v_mailTitle = "�ȳ��ϼ���? ���̹������� ����Դϴ�.";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

            while ( em1.hasMoreElements() ) { 
                v_userid    = (String)em1.nextElement();
                v_subj      = (String)em2.nextElement();
                v_year      = (String)em3.nextElement();
                v_subjseq   = (String)em4.nextElement();

                // select subjnm,isonoff,edustart,name,ismailing,cono,email
                sql  = " select  b.subjnm, b.isonoff, d.name, d.email_get as ismailing 	\n";
                sql += "      ,  d.userid as cono, d.email 								\n";
                sql += "      , (select to_char(edustart,'yyyymmdd') - 2 				\n";
                sql += "         from   tz_subjseq 										\n";
                sql += "         where  subj = a.subj 									\n";
                sql += "         and    year = a.year 									\n";
                sql += "         and    subjseq = '0002') as edustart 					\n";
                sql += " from    tz_propose a, vz_scsubjseq b, tz_member d 				\n";
                sql += " where   a.userid = " +SQLString.Format(v_userid) + "			\n";
                sql += " and     a.subj = " +SQLString.Format(v_subj) + "				\n";
                sql += " and     a.year = " +SQLString.Format(v_year) + "				\n";
                sql += " and     a.subjseq = " +SQLString.Format(v_subjseq) + " 		\n";
                sql += " and     a.subj = b.subj 										\n";
                sql += " and     a.year = b.year 										\n";
                sql += " and     a.subjseq = b.subjseq 									\n";
                sql += " and     a.userid  = d.userid 									\n";
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    String v_toEmail =  ls.getString("email");
                    String v_toCono  =  ls.getString("cono");
                    String v_ismailing= ls.getString("ismailing");
                    // String v_toEmail =  "jj1004@dreamwiz.com";

                    mset.setSender(fmail);     // ���Ϻ����� ��� ����

                    if ( ls.getString("isonoff").equals("ON") ) { v_isonoff="���̹�";   }
                    else                                    {     v_isonoff="����";     }
                    v_edustart      = FormatDate.getFormatDate(v_edustart,"yyyy.MM.dd");

                    fmail.setVariable("subjnm", ls.getString("subjnm") );
                    fmail.setVariable("edustart", ls.getString("edustart") );
                    fmail.setVariable("isonoff", v_isonoff);
                    fmail.setVariable("toname", ls.getString("name") );

                    String v_mailContent = fmail.getNewMailContent();

                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
                    if ( isMailed) cnt++;     //      ���Ϲ߼ۿ� �����ϸ�
                }

            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }
    //ȯ������ ���� -�������߰�
	public int saveRepayData(RequestBox box) throws Exception{
		DBConnectionManager	connMgr	= null;
		PreparedStatement   pstmt               = null;
        String              sql     = "";
        int isOk = 0;   
        Vector v_param_check     = new Vector();//üũ�ڽ�
        Vector v_param_id     = new Vector();//�ֹ���ȣ
        Vector v_param_dt     = new Vector();//��¥
        
        v_param_check = box.getVector("p_checks_hidden");
        v_param_id    = box.getVector("order_id");
        v_param_dt    = box.getVector("repay_dt");
        System.out.println("p_checks_hidden::"+v_param_check.size());
        System.out.println("v_param_id::"+v_param_id.size());
        System.out.println("v_param_dt::"+v_param_dt.size());
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            sql = "update pa_payment set repay_dt = ? where order_id= ? ";
            pstmt   = connMgr.prepareStatement(sql);
            
            for(int i=0;i<v_param_check.size();i++){
            	if("1".equals(v_param_check.elementAt(i).toString())){
	            	pstmt.setString(1,v_param_dt.elementAt(i).toString().replaceAll("-",""));
	            	pstmt.setString(2,v_param_id.elementAt(i).toString());
	            	
	            	System.out.println("update pa_payment set repay_dt = '"+v_param_dt.elementAt(i).toString()+"' where order_id= '"+v_param_id.elementAt(i).toString()+"' ");
	            	
	            	isOk = pstmt.executeUpdate();
            	}
            }
           
            if(isOk > 0){
            	connMgr.commit();
            }
            	
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
        	if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        	if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e10 ) { } }
        }

        return isOk;
	}
}