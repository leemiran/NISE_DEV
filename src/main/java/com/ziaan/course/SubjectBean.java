// **********************************************************
//  1. ��      ��: ����OPERATION BEAN
//  2. ���α׷���: SubjectBean.java
//  3. ��      ��: ����OPERATION BEAN
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ���� 2004. 11. 14
//  7. ��      ��: ������ 2006. 06. 01
// **********************************************************
package com.ziaan.course;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.exam.ExamPaperBean;
import com.ziaan.lcms.KTUtil;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.ProposeBean;
import com.ziaan.system.SelectionData;

public class SubjectBean { 

    public final static String LANGUAGE_GUBUN   = "0017";
    public final static String ONOFF_GUBUN      = "0004";

    public SubjectBean() { }
    
    

    /**
     * @param box          receive from the form object and session
     * @return ArrayList   ����������
     */
    public static String getProducerName(String p_producernm ) throws Exception { 
        //ConfigSet           config              = new ConfigSet();
        
        String              v_propproducernm    = "";//(String)config.getProperty("producer.name");
        
        try { 
        
            String []   v_arrpropprocucernm     = v_propproducernm.split("-");
            
            if ( v_arrpropprocucernm == null || v_arrpropprocucernm.length < 2 )
                return p_producernm; 
            
            String []   v_arrpropprocucerdenm   = v_arrpropprocucernm == null || v_arrpropprocucernm.length < 2 ? null : v_arrpropprocucernm[1].split(",");  
            
            if ( v_arrpropprocucerdenm == null || v_arrpropprocucerdenm.length < 1 )
                return p_producernm; 
                
            for ( int i = 0 ; i < v_arrpropprocucerdenm.length ; i++ ) {
                if ( p_producernm.trim().equals(v_arrpropprocucerdenm[i].trim()) ) {
                    p_producernm    = v_arrpropprocucernm[0];
                    return p_producernm;
                }    
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
        }
        
        return p_producernm;
    }
    
    /**
     * ���� ����Ʈ ��ȸ
     * @param box          receive from the form object and session
     * @return ArrayList   ���񸮽�Ʈ
     */
    public ArrayList SelectSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        SubjectData         data            = null;

        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // �����ְ�
        String              ss_upperclass   = box.getStringDefault("s_upperclass"   , "ALL");   // ����з�
        String              ss_middleclass  = box.getStringDefault("s_middleclass"  , "ALL");   // ����з�
        String              ss_lowerclass   = box.getStringDefault("s_lowerclass"   , "ALL");   // ����з�
        //String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // ����&�ڽ�
        String              v_searchtext    = box.getString       ("p_searchtext"          );
                            
        String              v_orderColumn   = box.getString       ("p_orderColumn"         );   // ������ �÷���
        String              v_orderType     = box.getString       ("p_orderType"           );   // ������ ����

        String v_searchGu1 = box.getString("p_searchGu1");
        String v_searchGu2 = box.getString("p_searchGu2");

        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            if ( ss_grcode.equals("ALL") ) { 
                sbSQL.append(" select  a.upperclass, b.classname   , a.isonoff     , c.codenm               \n")
                     .append("     ,   a.subj      , a.subjnm      , a.muserid     , d.name                 \n")
                     .append("     ,   a.isuse     , '' grcode     , a.isapproval  , a.isintroduction       \n")
                     .append("     ,   a.introducefilenamereal     , a.introducefilenamenew                 \n")
                     .append("     ,   a.informationfilenamereal   , a.informationfilenamenew               \n")
                     .append("     ,   nvl(a.subj_gu,'') as subj_gu                                         \n")
                     .append("     ,   a.content_cd				                                            \n")
                     .append("     ,   nvl(a.study_count,0) as study_count                                  \n")
                     .append("     ,   a.ldate, a.contenttype                                            	\n")
                     .append(" from    tz_subj     a                                                        \n")
                     .append("     ,   tz_subjatt  b                                                        \n")
                     .append("     ,   tz_code     c                                                        \n")
                     .append("     ,   tz_member   d                                                        \n")
                     .append(" where   a.upperclass  = b.upperclass                                         \n")
                     .append(" and     a.isonoff     = c.code                                               \n")
                     .append(" and     a.muserid     = d.userid(+)    	                                    \n")
                     .append(" and     b.middleclass = '000'                                                \n")
                     .append(" and     b.lowerclass  = '000'                                                \n")
                     .append(" and     c.gubun       = " + SQLString.Format(ONOFF_GUBUN) + "                \n");
            } else { 
                sbSQL.append(" select  a.upperclass,   b.classname , a.isonoff     , c.codenm               \n")
                     .append("     ,   a.subj      ,   a.subjnm    , a.muserid     , d.name                 \n") 
                     .append("     ,   a.isuse     ,   e.grcode    , a.isapproval  , a.isintroduction       \n")
                     .append("     ,   a.introducefilenamereal     , a.introducefilenamenew                 \n")
                     .append("     ,   a.informationfilenamereal   , a.informationfilenamenew               \n")
                     .append("     ,   nvl(a.subj_gu,'') as subj_gu                                         \n")
                     .append("     ,   a.content_cd				                                            \n")
                     .append("     ,   nvl(a.study_count,0) as study_count                                  \n")
                     .append("     ,   a.ldate, a.contenttype				                                            	\n")
                     .append(" from    tz_subj     a                                                        \n")
                     .append("     ,   tz_subjatt  b                                                        \n")
                     .append("     ,   tz_code     c                                                        \n")
                     .append("     ,   tz_member   d                                                        \n")
                     .append("     ,   tz_grsubj   e                                                        \n")
                     .append("     ,   tz_grcode   f                                                        \n")
                     .append(" where   a.upperclass  = b.upperclass                                         \n")
                     .append(" and     a.isonoff     = c.code                                               \n")
                     .append(" and     a.muserid     = d.userid(+)	                                        \n")
                     .append(" and     b.middleclass = '000'                                                \n")
                     .append(" and     b.lowerclass  = '000'                                                \n")
                     .append(" and     a.subj        = e.subjcourse		                                    \n")
                     .append(" and     e.grcode      = f.grcode	 	                                        \n")
                     .append(" and     c.gubun       = " + SQLString.Format(ONOFF_GUBUN) + "                \n")
                     .append(" and     e.grcode = " + StringManager.makeSQL(ss_grcode)   + " 				\n");
            }

//            if ( !ss_subjcourse.equals("ALL") ) { 
//                sbSQL.append(" and a.subj = " + StringManager.makeSQL(ss_subjcourse) + " \n");
//            } else { 
                if ( !ss_upperclass.equals("ALL") ) { 
                    if ( !ss_upperclass.equals("ALL") ) { 
                        sbSQL.append(" and     a.upperclass  = " + StringManager.makeSQL(ss_upperclass ) + " \n");
                    }
                    if ( !ss_middleclass.equals("ALL") ) { 
                        sbSQL.append(" and     a.middleclass = " + StringManager.makeSQL(ss_middleclass) + " \n");
                    }
                    if ( !ss_lowerclass.equals("ALL") ) { 
                        sbSQL.append(" and     a.lowerclass  = " + StringManager.makeSQL(ss_lowerclass ) + " \n");
                    }
                }
//            }

            if ( !v_searchtext.equals("") ) { 
            	v_searchtext = v_searchtext.replaceAll("'", "");
            	v_searchtext = v_searchtext.replaceAll("/", "//");
            	v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
                sbSQL.append(" and     (upper(a.subjnm) like " + StringManager.makeSQL("%"+v_searchtext+"%") + " escape '/'  \n");
                sbSQL.append("          or upper(a.subj) like " + StringManager.makeSQL("%"+v_searchtext+"%") + " escape '/')  \n");
            }

            // �������к� ��ȸ
            if ( !v_searchGu1.equals("ALL")) {
            	sbSQL.append(" and    a.isonoff = " + StringManager.makeSQL(v_searchGu1) + " \n");
            }
            if ( !v_searchGu2.equals("ALL")) {
            	if ( v_searchGu2.equals("") ) {
            		sbSQL.append(" and    a.subj_gu is null \n");
            	} else {
            		sbSQL.append(" and    a.subj_gu = " + StringManager.makeSQL(v_searchGu2) + " \n");
            	}
            }
//System.out.println("~~~1111 : "+sbSQL.toString());
            if ( v_orderColumn.equals("") ) { 
                sbSQL.append(" order   by a.subj desc \n");
            } else { 
                sbSQL.append(" order   by " + v_orderColumn + v_orderType + " \n");
            }

            ls          = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                data    = new SubjectData();
                
                data.setUpperclass              ( ls.getString("upperclass"             ));
                data.setClassname               ( ls.getString("classname"              ));
                data.setIsonoff                 ( ls.getString("isonoff"                ));
                data.setCodenm                  ( ls.getString("codenm"                 ));
                data.setSubj                    ( ls.getString("subj"                   ));
                data.setSubjnm                  ( ls.getString("subjnm"                 ));
                data.setName                    ( ls.getString("name"                   ));
                data.setIsuse                   ( ls.getString("isuse"                  ));
                data.setIsapproval              ( ls.getString("isapproval"             ));
                data.setIsintroduction          ( ls.getString("isintroduction"         ));
                data.setIntroducefilenamereal   ( ls.getString("introducefilenamereal"  ));
                data.setIntroducefilenamenew    ( ls.getString("introducefilenamenew"   ));
                data.setInformationfilenamereal ( ls.getString("informationfilenamereal"));
                data.setInformationfilenamenew  ( ls.getString("informationfilenamenew" ));
                data.setSubj_gu				    ( ls.getString("subj_gu" ));
                data.setContent_cd			    ( ls.getString("content_cd" ));
                data.setStudy_count			    ( ls.getInt   ("study_count" ));
                data.setLdate				    ( ls.getString("ldate" ));
                data.setContenttype				( ls.getString("contenttype" ));
                
                list.add(data);
            }
            
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch (Exception e ) { } 
            }
        }
        
        return list;
    }

    /**
     * ���� ����Ʈ ��ȸ
     * @param box          receive from the form object and session
     * @return ArrayList   ���񸮽�Ʈ
     */
    public ArrayList SelectSubjectListForExcel(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        SubjectData         data            = null;

        String ss_grcode 		= box.getStringDefault("s_grcode", "ALL");   // �����׷�
        String ss_upperclass   	= box.getStringDefault("s_upperclass"   , "ALL");   // ����з�
        String ss_middleclass  	= box.getStringDefault("s_middleclass"  , "ALL");   // ����з�
        String ss_lowerclass   	= box.getStringDefault("s_lowerclass"   , "ALL");   // ����з�
        String v_searchtext    	= box.getString       ("p_searchtext");
        
        String v_searchGu1 = box.getString("p_searchGu1");
        String v_searchGu2 = box.getString("p_searchGu2");
        
        String sql = "";

        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            /**
             * ��������	�����ι�	�����о�	�����ڵ�	������	�н�����	�н��ð�	�̼���	
             * ����ȸ��	���ۻ�	����	�Ű�ð�	����	 ��� 	 ������ 	 ȯ�ޱ� 	�����ɻ���	 
             * ���õ��� 	�������	������ǥ	�н�����	������Type	����	����������	�������ڵ�	
             * ����	��������	�ű�/����/����
             */
            if ( ss_grcode.equals("ALL") ) { 
            	sql = "\n select get_codenm('"+ONOFF_GUBUN+"',a.isonoff) as isonoff  /* �������� */ "
            		+ "\n      , get_subjclassnm(a.upperclass,'000','000') as upperclassnm   /* �����з� �� */ "
            		+ "\n      , get_subjclassnm(a.upperclass,middleclass,'000') as middleclassnm    /* �����з� �� */ "
            		+ "\n      , get_subjclassnm(a.upperclass,lowerclass,'000') as lowerclassnm    /* �����з� �� */ "            		
            		+ "\n      , a.subj 	 /* �����ڵ� */ "
            		+ "\n      , a.subjnm    /* ������ */ "
            		+ "\n      , a.edudays   /* �н����� */ "
            		+ "\n      , a.edutimes  /* �н��ð� */ "
             		+ "\n      , a.owner    /* ����ȸ�� */ "
                    + "\n      , nvl(( "
                    + "\n             select cpnm "
                    + "\n             from   tz_cpinfo "
                    + "\n             where  cpseq = a.owner "
                    + "\n            ), ( "
                    + "\n                 select compnm "
                    + "\n                 from   tz_compclass "
                    + "\n                 where  comp = a.owner "
                    + "\n                 )) as ownernm /* ����ȸ�� */ "
            		+ "\n      , a.producer    /* ���ۻ� */ "
                    + "\n      , nvl(( "
                    + "\n             select cpnm "
                    + "\n             from   tz_cpinfo "
                    + "\n             where  cpseq = a.producer "
                    + "\n            ), ( "
                    + "\n                select  compnm "
                    + "\n                from    tz_compclass "
                    + "\n                where   comp = a.producer "
                    + "\n               )) as producernm /* ���ۻ� */ "
            		+ "\n      , get_codenm_cb('013',a.lev) as lev /* �������� */ "            		
            		+ "\n      , a.edutimes /* �����ð� */ "
            		+ "\n      , (select count(*) from tz_subjlesson where subj = a.subj) as lessoncnt  /* ���� */ "
            		+ "\n      , a.contentgrade /* ��������� */ "
            		+ "\n      , a.isgoyong /* ��뺸�迩�� */ "
            		+ "\n      , a.goyongpriceminor   /* �켱�������� */ "
            		+ "\n      , a.goyongpricemajor   /* ���� */ "
            		+ "\n      , a.judgedate    /* �����ɻ��� */ "
            		+ "\n      , decode(a.usebook,'N','��','��') as usebook  /* ���õ��� */ "
            		+ "\n      , a.edumans  /* ������� */ "
            		+ "\n      , a.intro   /* ������ǥ */ "
            		+ "\n      , a.explain   /* �н����� */ "
            		+ "\n      , 'WBT(' || a.ratewbt || '%) / VOD(' || a.ratevod || '%)' as conttentkind  /* ������Type */ "
            		+ "\n      , get_codenm('0106',a.getmethod) as getmethod    /* ���� */ "
            		+ "\n      , get_codenm('0007',a.contenttype) as contenttype  /* ���������� */ "
            		+ "\n      , a.content_cd   /* �������ڵ� */ "
            		+ "\n      , a.firstdate    /* ���� */ "
            		+ "\n      , a.getdate  /* �������� */ "
            		+ "\n      , '' as contentstatus  /* �ű�/����/���� */ "
            		+ "\n      , a.isuse  /* ��뿩�� */ "
            		+ "\n      , a.biyong  "
            		+ "\n from   tz_subj a "
            		+ "\n where  1=1 ";
            } else { 
            	
            	sql = "\n select get_codenm('"+ONOFF_GUBUN+"',a.isonoff) as isonoff  /* �������� */ "
            		+ "\n      , get_subjclassnm(a.upperclass,'000','000') as upperclassnm   /* �����ι� */ "
            		+ "\n      , get_subjclassnm(a.upperclass,middleclass,'000') as middleclassnm    /* �����о� */ "
            		+ "\n      , get_subjclassnm(a.upperclass,lowerclass,'000') as lowerclassnm    /* �����з� �� */ "  
            		+ "\n      , a.subj /* �����ڵ� */ "
            		+ "\n      , a.subjnm    /* ������ */ "
            		+ "\n      , a.edudays   /* �н����� */ "
            		+ "\n      , a.edutimes  /* �н��ð� */ "
            		+ "\n      , a.owner    /* ����ȸ�� */ "
                    + "\n      , nvl(( "
                    + "\n             select cpnm "
                    + "\n             from   tz_cpinfo "
                    + "\n             where  cpseq = a.owner "
                    + "\n            ), ( "
                    + "\n                 select compnm "
                    + "\n                 from   tz_compclass "
                    + "\n                 where  comp = a.owner "
                    + "\n                 )) as ownernm /* ����ȸ�� */ "
            		+ "\n      , a.producer    /* ���ۻ� */ "
                    + "\n      , nvl(( "
                    + "\n             select cpnm "
                    + "\n             from   tz_cpinfo "
                    + "\n             where  cpseq = a.producer "
                    + "\n            ), ( "
                    + "\n                select  compnm "
                    + "\n                from    tz_compclass "
                    + "\n                where   comp = a.producer "
                    + "\n               )) as producernm /* ���ۻ� */ "
            		+ "\n      , get_codenm_cb('013',a.lev) as lev /* �������� */ "
            		+ "\n      , a.edutimes /* �����ð� */ "
            		+ "\n      , (select count(*) from tz_subjlesson where subj = a.subj) as lessoncnt  /* ���� */ "
            		+ "\n      , a.contentgrade /* ������� */ "
            		+ "\n      , a.isgoyong /* ��뺸�迩�� */ "
            		+ "\n      , a.goyongpriceminor   /* �켱�������� */ "
            		+ "\n      , a.goyongpricemajor   /* ȯ�ޱ� */ "
            		+ "\n      , a.judgedate    /* �����ɻ��� */ "
            		+ "\n      , decode(a.usebook,'N','��','��') as usebook  /* ���õ��� */ "
            		+ "\n      , a.edumans  /* ������� */ "
            		+ "\n      , a.intro   /* ������ǥ */ "
            		+ "\n      , a.explain   /* �н����� */ "
            		+ "\n      , 'WBT(' || a.ratewbt || '%) / VOD(' || a.ratevod || '%)' as conttentkind  /* ������Type */ "
            		+ "\n      , get_codenm('0106',a.getmethod) as getmethod    /* ���� */ "
            		+ "\n      , get_codenm('0007',a.contenttype) as contenttype  /* ���������� */ "
            		+ "\n      , a.content_cd   /* �������ڵ� */ "
            		+ "\n      , a.firstdate    /* ���� */ "
            		+ "\n      , a.getdate  /* �������� */ "
            		+ "\n      , '' as contentstatus  /* �ű�/����/���� */ "
            		+ "\n      , a.isuse  /* ��뿩�� */ "
            		+ "\n      , a.biyong  "
            		+ "\n from   tz_subj a "
            		+ "\n      , tz_grsubj b "
            		+ "\n where  a.subj = b.subjcourse "
            		+ "\n and    b.grcode = " + StringManager.makeSQL(ss_grcode);
            }

            // �������к� ��ȸ
            if ( !v_searchGu1.equals("ALL")) {
            	sql +="\n and    a.isonoff = " + StringManager.makeSQL(v_searchGu1);
            }


            if ( !ss_upperclass.equals("ALL") ) { 
                if ( !ss_upperclass.equals("ALL") ) { 
                	sql +="\n and     a.upperclass  = " + StringManager.makeSQL(ss_upperclass );
                }
                if ( !ss_middleclass.equals("ALL") ) { 
                	sql +="\n and     a.middleclass = " + StringManager.makeSQL(ss_middleclass);
                }
                if ( !ss_lowerclass.equals("ALL") ) { 
                	sql +="\n and     a.lowerclass  = " + StringManager.makeSQL(ss_lowerclass );
                }
            }

	        if ( !v_searchtext.equals("") ) { 
	        	v_searchtext = v_searchtext.replaceAll("'", "");
	        	v_searchtext = v_searchtext.replaceAll("/", "//");
	        	v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
	        	sql +="\n and    (upper(a.subjnm) like " + StringManager.makeSQL("%"+v_searchtext+"%") + " escape '/' "
	        		+ "\n         or upper(a.subj) like " + StringManager.makeSQL("%"+v_searchtext+"%") + " escape '/') ";
	        }

            sql +="\n order   by a.subj desc ";

            ls          = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            	DataBox dbox = (DataBox)ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch (Exception e ) { } 
            }
        }
        
        return list;
    }

    /**
     * ���� ����ȸ
     * @param box          receive from the form object and session
     * @return ArrayList   ������Ÿ
     */
    public SubjectData SelectSubjectData(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        SubjectData         data        = null;
        String        		sql       	= "";
        
        String              v_subj      = box.getString("p_subj");
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "\n select get_subjclassnm(a.upperclass, '000', '000') as upperclassnm "
                + "\n      , get_subjclassnm(a.upperclass, a.middleclass, '000') as middleclassnm "
                + "\n      , get_subjclassnm(a.upperclass, a.middleclass, a.lowerclass) as lowerclassnm "
                + "\n      , a.subj          , a.subjnm          , a.isonoff     , a.subjclass "
                + "\n      , a.upperclass    , a.middleclass     , a.lowerclass  , a.specials "
                + "\n      , a.contenttype   , a.muserid         , a.musertel    , a.cuserid "
                + "\n      , a.isuse         , a.ispropose       , a.biyong      , a.edudays "
                + "\n      , a.studentlimit  , a.usebook         , a.bookprice   , a.owner "
                + "\n      , a.producer      , a.crdate          , a.language    , a.server "
                + "\n      , a.dir           , a.eduurl          , a.vodurl      , a.preurl "
                + "\n      , a.ratewbt       , a.ratevod         , a.env         , a.tutor "
                + "\n      , a.bookname      , a.sdesc           , a.warndays    , a.stopdays "
                + "\n      , a.point         , a.edulimit        , a.gradscore   , a.gradstep "
                + "\n      , a.wstep         , a.wmtest          , a.wftest      , a.wreport  "
                + "\n      , a.wact          , a.wetc1           , a.wetc2       , a.place "
                + "\n      , a.placejh       , a.ispromotion     , a.isessential , a.score "
                + "\n      , a.edumans subjtarget                , a.inuserid    , a.indate "
                + "\n      , a.luserid       , a.ldate "
                + "\n      , get_name(cuserid) as cuseridnm "  
                + "\n      , get_name(a.tutor) as tutornm "
                + "\n      , get_name(a.muserid) as museridnm "
                + "\n      , nvl(( "
                + "\n             select cpnm "
                + "\n             from   tz_cpinfo "
                + "\n             where  cpseq = a.producer "
                + "\n            ), ( "
                + "\n                select  compnm "
                + "\n                from    tz_compclass "
                + "\n                where   comp = a.producer "
                + "\n               )) as producernm "
                + "\n      , nvl(( "
                + "\n             select cpnm "
                + "\n             from   tz_cpinfo "
                + "\n             where  cpseq = a.owner "
                + "\n            ), ( "
                + "\n                 select compnm "
                + "\n                 from   tz_compclass "
                + "\n                 where  comp = a.owner "
                + "\n                 )) as ownernm "
                + "\n      , a.proposetype       , a.edumans             , a.edutimes        , a.edutype "
                + "\n      , a.intro             , a.explain             , a.whtest          , a.gradreport "
                + "\n      , a.gradexam          , a.gradftest           , a.gradhtest       , '' usesubjseqapproval "
                + "\n      , '' useproposeapproval, '' usemanagerapproval  , 0 rndcreditreq    , 0 rndcreditchoice "
                + "\n      , 0 rndcreditadd      , 0 rndcreditdeduct     , a.isablereview    , a.isoutsourcing "
                + "\n      , a.conturl           , a.isapproval          , '' rndjijung       , bookfilenamereal "
                + "\n      , bookfilenamenew "
                + "\n      , ( "
                + "\n         select   count(*) "
                + "\n         from    tz_subjseq "
                + "\n         where   subj=a.subj "
                + "\n        ) as subjseqcount "
                + "\n      , ( "
                + "\n         select  count(*) "
                + "\n         from    tz_subjobj "
                + "\n         where   subj = a.subj "
                + "\n        ) as subjobjcount "
                + "\n      , a.isvisible "
                + "\n      , a.isalledu "
                + "\n      , a.isintroduction "
                + "\n      , a.eduperiod "
                + "\n      , a.introducefilenamereal "
                + "\n      , a.introducefilenamenew "
                + "\n      , a.informationfilenamereal "
                + "\n      , a.informationfilenamenew "
                + "\n      , a.contentgrade "
                + "\n      , a.memo "
                + "\n      , a.ischarge "
                + "\n      , a.isopenedu "
                + "\n      , 0 maleassignrate "
                + "\n      , a.gradftest_flag "
                + "\n      , a.gradexam_flag "
                + "\n      , a.gradhtest_flag "
                + "\n      , a.gradreport_flag "
                + "\n      , a.isgoyong "
                + "\n      , a.goyongpricemajor "
                + "\n      , a.goyongpriceminor "
                + "\n      , a.graduatednote "
                + "\n      , nvl(a.subj_gu,'') as subj_gu "
                + "\n      , a.content_cd "
                + "\n      , nvl(a.study_count,0) as study_count "
                + "\n      , reviewdays "
                + "\n      , lev "
                + "\n      , gubn "
                + "\n      , grade "  
                + "\n      , test "
                + "\n      , sel_dept "
                + "\n      , sel_post "
                + "\n      , org "
                + "\n      , nvl(study_time,0) as study_time "
                + "\n      , nvl(a.cp_accrate,0) as cp_accrate "
                + "\n      , a.goyongpricestand "
                + "\n      , a.getmethod "
                + "\n      , a.cp "
                + "\n      , nvl(( "
                + "\n             select cpnm "
                + "\n             from   tz_cpinfo "
                + "\n             where  cpseq = a.cp "
                + "\n            ), ( "
                + "\n                select compnm "
                + "\n                from   tz_compclass "
                + "\n                where  comp = a.cp "
                + "\n               ))         cpnm "
                + "\n      , a.firstdate "
                + "\n      , a.judgedate "
                + "\n      , a.getdate "
                + "\n      , a.iscustomedu "
                + "\n      , a.cp_account "
                + "\n      , a.cp_vat "                
                + "\n      , a.contentprogress "                
                + "\n      , a.cpsubj "                
                + "\n from   tz_subj     a "
                + "\n where  a.subj = " + StringManager.makeSQL(v_subj);
            
            ls          = connMgr.executeQuery(sql);
          
            while ( ls.next() ) { 
            	
                data    = new SubjectData();
                
                data.setUpperclassnm            ( ls.getString("upperclassnm"   ));
                data.setMiddleclassnm           ( ls.getString("middleclassnm"  ));
                data.setLowerclassnm            ( ls.getString("lowerclassnm"   ));
                data.setSubj                    ( ls.getString("subj"           ));
                data.setSubjnm                  ( ls.getString("subjnm"         ));
                data.setIsonoff                 ( ls.getString("isonoff"        ));
                data.setSubjclass               ( ls.getString("subjclass"      ));
                data.setUpperclass              ( ls.getString("upperclass"     ));
                
                data.setMiddleclass             ( ls.getString("middleclass"    ));
                
                data.setLowerclass              ( ls.getString("lowerclass"     ));
                
                data.setSpecials                ( ls.getString("specials"       ));
                data.setContenttype             ( ls.getString("contenttype"    ));
                data.setMuserid                 ( ls.getString("muserid"        ));
                data.setMusertel                ( ls.getString("musertel"       ));
                data.setCuserid                 ( ls.getString("cuserid"        ));
                data.setIsuse                   ( ls.getString("isuse"          ));
                data.setIspropose               ( ls.getString("ispropose"      ));
                data.setBiyong                  ( ls.getInt   ("biyong"         ));
                data.setEdudays                 ( ls.getInt   ("edudays"        ));
                data.setStudentlimit            ( ls.getInt   ("studentlimit"   ));
                data.setUsebook                 ( ls.getString("usebook"        ));
                data.setBookprice               ( ls.getInt   ("bookprice"      ));
                data.setOwner                   ( ls.getString("owner"          ));
                data.setProducer                ( ls.getString("producer"       ));
                data.setCrdate                  ( ls.getString("crdate"         ));
                data.setLanguage                ( ls.getString("language"       ));
                data.setServer                  ( ls.getString("server"         ));
                data.setDir                     ( ls.getString("dir"            ));
                data.setEduurl                  ( ls.getString("eduurl"         ));
                data.setVodurl                  ( ls.getString("vodurl"         ));
                data.setPreurl                  ( ls.getString("preurl"         ));
                
                data.setRatewbt                 ( ls.getInt("ratewbt"           ));
                data.setRatevod                 ( ls.getInt("ratevod"           ));
                data.setEnv                     ( ls.getString("env"            ));
                
                data.setTutor                   ( ls.getString("tutor"          ));
                data.setTutornm     			(ls.getString("tutornm"));
				data.setBookname    			(ls.getString("bookname"));
				data.setSdesc       			(ls.getString("sdesc"));
				data.setWarndays    			(ls.getInt("warndays"));
				data.setStopdays    			(ls.getInt("stopdays"));
				data.setPoint       			(ls.getInt("point"));
				data.setEdulimit    			(ls.getInt("edulimit"));
				data.setGradscore   			(ls.getInt("gradscore"));
				data.setGradstep    			(ls.getInt("gradstep"));
				data.setWstep       			(ls.getDouble("wstep"));
				data.setWmtest      			(ls.getDouble("wmtest"));
				data.setWftest      			(ls.getDouble("wftest"));
				data.setWreport     			(ls.getDouble("wreport"));
				data.setWact        			(ls.getDouble("wact"));
				data.setWetc1       			(ls.getDouble("wetc1"));
				data.setWetc2       			(ls.getDouble("wetc2"));
				data.setPlace       			(ls.getString("place"));
				data.setPlacejh					(ls.getString("placejh"));
				data.setIsessential 			(ls.getString("isessential"));
				data.setSubjtarget  			(ls.getString("subjtarget"));
				data.setInuserid    			(ls.getString("inuserid"));
				data.setIndate      			(ls.getString("indate"));
				data.setLuserid     			(ls.getString("luserid"));
				data.setLdate      		 		(ls.getString("ldate"));

                data.setCuseridnm               (ls.getString("cuseridnm"      ));
                data.setMuseridnm               (ls.getString("museridnm"      ));
                data.setProducernm              (ls.getString("producernm"     ));
                data.setOwnernm                 (ls.getString("ownernm"        ));
                data.setProposetype             (ls.getString("proposetype"    ));
                data.setEdumans                 (ls.getString("edumans"        ));
                data.setEdutimes                (ls.getInt   ("edutimes"       ));
                data.setEdutype                 (ls.getString("edutype"        ));
                data.setIntro                   (ls.getString("intro"          ));
                data.setExplain                 (ls.getString("explain"        ));
                                                
                data.setWhtest                  (ls.getDouble("whtest"         ));
                data.setGradreport              (ls.getInt   ("gradreport"     ));
                data.setGradexam                (ls.getInt   ("gradexam"       ));// �������-�߰���
                data.setGradftest               (ls.getInt   ("gradftest"      ));// �������-������
                data.setGradhtest               (ls.getInt   ("gradhtest"      ));// �������-������
                


                data.setUsesubjseqapproval      (ls.getString("usesubjseqapproval" ));
                data.setUseproposeapproval      (ls.getString("useproposeapproval" ));
                
                data.setRndcreditreq            (ls.getDouble("rndcreditreq"       ));
                data.setRndcreditchoice         (ls.getDouble("rndcreditchoice"    ));
                data.setRndcreditadd            (ls.getDouble("rndcreditadd"       ));
                data.setRndcreditdeduct         (ls.getDouble("rndcreditdeduct"    ));
                data.setRndjijung               (ls.getString("rndjijung"          ));
                data.setIsablereview            (ls.getString("isablereview"       ));
                data.setIsoutsourcing           (ls.getString("isoutsourcing"      ));
                                                                                            
                data.setBookfilenamereal        (ls.getString("bookfilenamereal"   ));
                data.setBookfilenamenew         (ls.getString("bookfilenamenew"    ));
                                                                                            
                data.setSubjseqcount            (ls.getInt   ("subjseqcount"       ));
                                                                                            
                data.setIsvisible               (ls.getString("isvisible"          ));
                data.setSubjobjcount            (ls.getInt   ("subjobjcount"       ));
                data.setIsalledu                (ls.getString("isalledu"           ));
                                                                                           
                data.setIsintroduction          (ls.getString("isintroduction"         ));
                data.setEduperiod               (ls.getInt   ("eduperiod"              ));
                data.setIntroducefilenamereal   (ls.getString("introducefilenamereal"  ));
                data.setIntroducefilenamenew    (ls.getString("introducefilenamenew"   ));
                data.setInformationfilenamereal (ls.getString("informationfilenamereal"));
                data.setInformationfilenamenew  (ls.getString("informationfilenamenew" ));
                data.setContentgrade            (ls.getString("contentgrade"           ));
                data.setMemo                    (ls.getString("memo"                   ));
                data.setIscharge                (ls.getString("ischarge"               ));
                
                data.setIsopenedu               (ls.getString("isopenedu"              ));
                data.setMaleassignrate          (ls.getInt   ("maleassignrate"         ));
                
                data.setGradftest_flag          (ls.getString("gradftest_flag"     ));
                data.setGradhtest_flag          (ls.getString("gradhtest_flag"     ));
                data.setGradexam_flag           (ls.getString("gradexam_flag"      ));
                data.setGradreport_flag         (ls.getString("gradreport_flag"    ));
                data.setIsgoyong                (ls.getString("isgoyong"));
                data.setGoyongpricemajor        (ls.getInt   ("goyongpricemajor"));
                data.setGoyongpriceminor        (ls.getInt   ("goyongpriceminor"));       
                data.setGraduatednote           (ls.getString("graduatednote"));

                data.setSubj_gu                 (ls.getString("subj_gu"));
                data.setContent_cd              (ls.getString("content_cd"));
                data.setStudy_count             (ls.getInt   ("study_count"));
                data.setReviewdays          	(ls.getInt   ("reviewdays"));
                data.setLev         		    (ls.getString("lev"));
                data.setGubn		            (ls.getString("gubn"));
                data.setGrade             		(ls.getString("grade"));
                data.setTest             		(ls.getString("test"));

                data.setSel_dept           		(ls.getString("sel_dept"));
                data.setSel_post          		(ls.getString("sel_post"));

                data.setOrg         			(ls.getString("org"));
                data.setStudy_time         		(ls.getInt   ("study_time"));
                data.setCp_accrate         		(ls.getInt   ("cp_accrate"));

                data.setGoyongpricestand   		(ls.getInt   ("goyongpricestand"));
                data.setGetmethod         		(ls.getString("getmethod"));
                data.setCp         				(ls.getString("cp"));
                data.setCpnm       				(ls.getString("cpnm"));
                data.setFirstdate         		(ls.getString("firstdate"));
                data.setJudgedate         		(ls.getString("judgedate"));
                data.setGetdate         		(ls.getString("getdate"));
                data.setIscustomedu        		(ls.getString("iscustomedu"));

                data.setCp_account        		(ls.getInt("cp_account"));
                data.setCp_vat        			(ls.getInt("cp_vat"));                
                data.setContentprogress			(ls.getString("contentprogress"));                
                data.setCpsubj			        (ls.getString("cpsubj"));                
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n sql : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e1 ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return data;
    }

    
    /**
     * �����ڵ� ��� - �̷���
     * @param box      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int InsertSubject(RequestBox box) throws Exception { 
        DBConnectionManager 	connMgr 		= null;
        PreparedStatement 		pstmt 			= null;
        String		            sql           	= "";
        int						isOk            = 0;
        int						isOk2           = 0;
        int						isOk3           = 0;
        int						isOk4           = 0;
        int						isOk5           = 0;

        String v_luserid  	= box.getSession("userid"       );
        String v_grcode   	= box.getString ("s_grcode"     ); // ���񸮽�Ʈ������ SELECTBOX �����ְ� �ڵ�
        String v_dir 		= "";                              // v_dir�� p_contenttype�� Normal,OBC,SCORM����϶� dp/content/�����ڵ带 �־��ش�.
        String v_contenttype= box.getString ("p_contenttype");
        
        int pidx = 1;
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // KT���丮 ��ũ�����ÿ� ��ȣȭ
            KTUtil ktUtil = KTUtil.getInstance();
            
            String v_subj      	= getMaxSubjcode(connMgr, box.getString("p_upperclass"), box.getString("p_middleclass") );            
            String v_content_cd = v_subj;
            
            ConfigSet conf = new ConfigSet();
            String contentPath = conf.getProperty("dir.content.path");
            //String ktContentPath = conf.getProperty("dir.kt_content.path");
            String contentUploadPath = conf.getProperty("dir.content.upload");
            //String ktContentUploadPath = conf.getProperty("dir.kt_content.upload");
            //String ktContentLink = conf.getProperty("dir.kt_content.link");
            
            // ���丮��δ� ������Ÿ�Կ� ���� �����ϴ�.
            // ������Ÿ���� N(Normal), O(OBC), OA(OBC-Author), S(SCORM) �϶�
            if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") || v_contenttype.equals("S") ) {
            	v_dir = contentPath + v_subj;
            	v_content_cd = v_subj; // �ش� ������Ÿ���� �������ڵ�� �����ڵ�� �����ϴ�. 
            } 
            // �� �̿��� ������Ÿ�� L(Link) ��� �϶�
            else {
            	v_dir = "";
            	v_content_cd = "";
            }

            // insert tz_subj table
            sql = "\n insert into tz_subj "
                + "\n ( "
                + "\n     subj                    , subjnm                , isonoff                   , subjclass "
                + "\n   , upperclass              , middleclass           , lowerclass                , specials "
                + "\n   , muserid                 , cuserid               , isuse                     , ispropose "
                + "\n   , biyong                  , edudays               , studentlimit              , usebook "
                + "\n   , bookprice               , owner                 , producer                  , eduurl "
                + "\n   , crdate                  , language              , server                    , dir "
                + "\n   , vodurl                  , preurl                , conturl                   , ratewbt "
                + "\n   , ratevod                 , env                   , tutor                     , bookname "
                + "\n   , sdesc                   , warndays              , stopdays                  , point "
                + "\n   , edulimit                , gradscore             , gradstep                  , wstep "
                + "\n   , wmtest                  , wftest                , wreport                   , wact "
                + "\n   , wetc1                   , wetc2                 , inuserid                  , indate "
                + "\n   , luserid                 , ldate                 , proposetype               , edumans "
                + "\n   , intro                   , explain               , isessential               , score "
                + "\n   , contenttype             , gradexam              , gradreport                , whtest "
                + "\n   , isoutsourcing           , isablereview          , musertel                  , gradftest "
                + "\n   , gradhtest               , isvisible             , isalledu                  , edutimes "
                + "\n   , isapproval              , isintroduction        , eduperiod                 , introducefilenamereal "
                + "\n   , introducefilenamenew    , informationfilenamereal, informationfilenamenew   , contentgrade "
                + "\n   , memo                    , ischarge              , isopenedu "
                + "\n   , gradftest_flag          , gradexam_flag         , gradhtest_flag            , gradreport_flag "
                + "\n 	, isgoyong                , goyongpricemajor      , goyongpriceminor "
                + "\n 	, subj_gu                 , content_cd            , study_count			      , reviewdays "
                + "\n 	, lev                     , study_time            , cp_accrate 				  , getmethod "
                + "\n 	, cp 					  , firstdate     	   	  , judgedate 				  , getdate "	
                + "\n 	, cp_account			  , cp_vat 	              , contentprogress           , cpsubj  "             
                + "\n ) values (  "
                + "\n     ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n   , ?                       , ?                     , ?                             "
                + "\n   , ?                       , ?                     , ?                         , ? "
                + "\n	, ?					      , ?					  , ? "
                + "\n	, ?					      , ?					  , ?			              , ? "
                + "\n	, ?						  , ?					  , ? 						  , ? "
                + "\n	, ?						  , ?					  , ?		                  , ? "
                + "\n	, ?						  , ? 					  , ?                         , ? "                
                + "\n ) ";                                                    
                        
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(pidx++, v_subj                           );                 // �����ڵ�
            pstmt.setString(pidx++, box.getString("p_subjnm"        ));                 // �����
            pstmt.setString(pidx++, box.getString("p_isonoff"       ));                 // �¶���/�������α���
            pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") +  box.getStringDefault("p_lowerclass","000") ); // �з��ڵ�
            pstmt.setString(pidx++, box.getString("p_upperclass"    ));                 // ��з�
            pstmt.setString(pidx++, box.getString("p_middleclass"   ));                 // �ߺз�
            pstmt.setString(pidx++, box.getStringDefault("p_lowerclass" ,"000") );      // �Һз�
            pstmt.setString(pidx++, box.getString("p_specials"      ));                 // ����Ư��(YYY)
            pstmt.setString(pidx++, box.getString("p_muserid"       ));                 // ���� ID
            pstmt.setString(pidx++, box.getString("p_cuserid"       ));                 // ��������� ID
            pstmt.setString(pidx++, box.getString("p_isuse"         ));                 // ��뿩��(Y/N)
            pstmt.setString(pidx++, box.getStringDefault("p_ispropose"  ,"Y"    ));     // [X]������û����(Y/N)
            
            pstmt.setInt   (pidx++, box.getInt   ("p_biyong"        ));                 // ������
            pstmt.setInt   (pidx++, box.getInt   ("p_edudays"       ));                 // �н�����
            pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"  ));                 // ����
            pstmt.setString(pidx++, box.getString("p_usebook"       ));                 // �����뿩��
            pstmt.setString(pidx++, box.getStringDefault("p_bookprice"  , "0"   ));     // �����
            pstmt.setString(pidx++, box.getString("p_owner"         ));                 // ���������
            pstmt.setString(pidx++, box.getSession("userid"      ));                 // ����������
            //pstmt.setString(pidx++, box.getString("p_producer"      ));                 // ����������
            pstmt.setString(pidx++, box.getString("p_eduurl"        ));                 // ����URL
            pstmt.setString(pidx++, box.getString("p_crdate"        ));                 // ��������
            pstmt.setString(pidx++, box.getString("p_language"      ));                 // [X]����
            pstmt.setString(pidx++, box.getString("p_server"        ));                 // [X]����
            pstmt.setString(pidx++, v_dir                            );                 // ���������
            
            pstmt.setString(pidx++, box.getString("p_vodurl"        ));                 // VOD���
            pstmt.setString(pidx++, box.getString("p_preurl"        ));                 // ������URL
            pstmt.setString(pidx++, box.getString("p_conturl"       ));                 // �н�����URL
            pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"       ));                 // �н����(WBT%)
            pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"       ));                 // �н����(VOD%)
            pstmt.setString(pidx++, box.getString("p_env"           ));                 // �н�ȯ��
            pstmt.setString(pidx++, box.getString("p_tutor"         ));                 // ���缳��
            pstmt.setString(pidx++, box.getStringDefault("p_bookname"   , ""));         // �����
            pstmt.setString(pidx++, box.getString("p_sdesc"         ));                 // [X]���
            pstmt.setInt   (pidx++, box.getInt   ("p_warndays"      ));                 // [X]�н����������
            pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"      ));                 // [X]�н�����������
            pstmt.setInt   (pidx++, box.getInt   ("p_point"         ));                 // ��������
            
            pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"      ));                 // 1���ִ��н���
            pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"     ));                 // �������-����
            pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"      ));                 // �������-������
            pstmt.setDouble(pidx++, box.getDouble("p_wstep"         ));                 // ����ġ-������
            pstmt.setDouble(pidx++, box.getDouble("p_wmtest"        ));                 // ����ġ-�߰���
            pstmt.setDouble(pidx++, box.getDouble("p_wftest"        ));                 // ����ġ-������
            pstmt.setDouble(pidx++, box.getDouble("p_wreport"       ));                 // ����ġ-����
            pstmt.setDouble(pidx++, box.getDouble("p_wact"          ));                 // ����ġ-��Ƽ��Ƽ
            pstmt.setDouble(pidx++, box.getDouble("p_wetc1"         ));                 // ����ġ-������
            pstmt.setDouble(pidx++, box.getDouble("p_wetc2"         ));                 // ����ġ-��������
            pstmt.setString(pidx++, v_luserid                        );                 // ������
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));              // ������
            
            pstmt.setString(pidx++, v_luserid);              							// ����������
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));              // ����������(ldate)
            pstmt.setString(pidx++, box.getString("p_proposetype"));                 	// ������û����
            pstmt.setString(pidx++, box.getStringDefault("p_edumans", "" ));    		// ������ �������
            pstmt.setString(pidx++, box.getStringDefault("p_intro", "" ));    			// ������ ��������
            pstmt.setString(pidx++, box.getStringDefault("p_explain", "" ));    		// ������ ��������
            pstmt.setString(pidx++, box.getStringDefault("p_isessential", "0"));    	// ���񱸺�
            pstmt.setInt   (pidx++, box.getInt   ("p_score"));                 			// ��������
            pstmt.setString(pidx++, v_contenttype);                 					// ������Ÿ��
            pstmt.setString(pidx++, box.getString("p_gradexam"));        				// �������(��)
            pstmt.setInt   (pidx++, box.getInt	 ("p_gradreport"));        			 	// �������(����)
            pstmt.setString(pidx++, box.getString("p_whtest"));         				// ����ġ(������)
            pstmt.setString(pidx++, box.getString("p_isoutsourcing"));         			// ��Ź��������
            pstmt.setString(pidx++, box.getString("p_isablereview"));					// �������ɿ���
            pstmt.setString(pidx++, box.getString("p_musertel"));						// ���� ��ȭ��ȣ
            pstmt.setInt   (pidx++, box.getInt	 ("p_gradftest"));						// �������-������
            pstmt.setInt   (pidx++, box.getInt	 ("p_gradhtest"));						// �������-������
            pstmt.setString(pidx++, box.getStringDefault("p_isvisible", "Y"));			// �н��ڿ��� �����ֱ�
            pstmt.setString(pidx++, box.getString("p_isalledu"));						// ���米������
            pstmt.setInt   (pidx++, box.getInt   ("p_edutimes"));						// �н��ð�
            pstmt.setString(pidx++, "Y");         										// ������ο��� (���⼱ �׻� Y)
            pstmt.setString(pidx++, box.getString("p_isintroduction"));					// ����Ұ� ��뿩��
            pstmt.setInt   (pidx++, box.getInt("p_eduperiod"));							// �н��Ⱓ
            pstmt.setString(pidx++, box.getRealFileName("p_introducefile"));			// ����Ұ� �̹���
            pstmt.setString(pidx++, box.getNewFileName ("p_introducefile"));			// ����Ұ� �̹���
            pstmt.setString(pidx++, box.getRealFileName("p_informationfile"));			// ����(����)
            pstmt.setString(pidx++, box.getNewFileName ("p_informationfile"));			// ����(����)
            pstmt.setString(pidx++, box.getString ("p_contentgrade"));					// ���������
            pstmt.setString(pidx++, box.getString ("p_memo"));							// ���
            pstmt.setString(pidx++, box.getString ("p_ischarge"));						// ������ ��/���� ����
            pstmt.setString(pidx++, box.getString ("p_isopenedu"));						// �������� ����
            pstmt.setString(pidx++, box.getString ("p_gradftest_flag"));				// �������-������(�ʼ�/���ÿ���)
            pstmt.setString(pidx++, box.getString ("p_gradexam_flag"));					// �������-�߰���(�ʼ�/���ÿ���)
            pstmt.setString(pidx++, box.getString ("p_gradhtest_flag"));				// �������-������(�ʼ�/���ÿ���)
            pstmt.setString(pidx++, box.getString ("p_gradreport_flag"));				// �������-����      (�ʼ�/���ÿ���)
            pstmt.setString(pidx++, box.getString ("p_isgoyong"));               		// ��뺸�迩��(Y/N)
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_major"));      		// ��뺸�� ȯ�޾�-����
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_minor"));      		// ��뺸�� ȯ�޾�-�켱���

            pstmt.setString(pidx++, box.getString ("p_subj_gu"));      					// ��������(�Ϲ�, JIT:J, ��������:M)
            pstmt.setString(pidx++, box.getString ("p_content_cd"));      				// ������ �ڵ�(Normal������ ��� ����)
            pstmt.setInt   (pidx++, box.getInt    ("p_study_count"));    		  		// �������-����Ƚ��
            pstmt.setInt   (pidx++, box.getInt    ("p_reviewdays"));    		  		// �������ɱⰣ
            pstmt.setString(pidx++, box.getString ("p_lev"));    		  				// ��������
            pstmt.setInt   (pidx++, box.getInt    ("p_study_time"));    		  		// �������-�ð�
            pstmt.setInt   (pidx++, box.getInt    ("p_cp_accrate"));    		  		// CP������

            pstmt.setString(pidx++, box.getString ("p_getmethod"));    		  			// ������Ȯ��-���
            pstmt.setString(pidx++, box.getString ("p_cp"));    		  				// ������Ȯ��-CP
            pstmt.setString(pidx++, box.getString ("p_firstdate"));    		  			// �����⵵-����Ȯ��
            pstmt.setString(pidx++, box.getString ("p_judgedate"));    		  			// �����⵵-�ɻ翬��
            pstmt.setString(pidx++, box.getString ("p_getdate"));    		  			// �����⵵-��������
            pstmt.setInt(pidx++, box.getInt ("p_cp_account"));    		  		// CP����ݾ�           
            pstmt.setInt(pidx++, box.getInt ("p_cp_vat"));    		  		// CP VAT                       
            pstmt.setString(pidx++, box.getString ("p_contentprogress"));    		  		// ����üũ(����Y/����N)                       
            pstmt.setString(pidx++, box.getString ("p_cpsubj"));    		  		        //CP�����ڵ�                   
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            // 2008.11.30 ����� ����
            // ������ �������� ���ȭ�鿡�� �ߴ� �����׷������ �������� ���, ����ȭ�鿡�� ���� �Է��ϴ°����� ����.
            //isOk2 = RelatedGrcodeInsert(connMgr, box);
            isOk2	=	1;

            // ����������� �Խ��� �����ϹǷ� �ּ�ó����.
            // �������ڷ�� Insert
            //isOk3 = this.InsertBds(connMgr,v_subj,"SD", "�������ڷ��", "ALL", "ALL");

            // ������������ Insert
            //isOk4 = this.InsertBds(connMgr,v_subj,"SQ", "������������", "ALL", "ALL"); 

            // ������ �Խ��� Insert
            //isOk5 = this.InsertBds(connMgr,v_subj,"BD", "�Խ���", "ALL", "ALL"); 
            
            //�̾�� �����������̺� Insert
            isOk5 = this.InsertContInfo(connMgr,v_subj);
            
            // GRSUBJ �� �ֱ�
            SaveGrSubj(connMgr, v_grcode, v_subj, box.getSession("userid"));
            
            if ( isOk == 1) {
                if (isOk2 != -1) {
	
	                // ���丮��δ� ������Ÿ�Կ� ���� �����ϴ�.
	                // ������Ÿ���� N(Normal), O(OBC), OA(OBC-Author), S(SCORM) �϶�
	                if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") || v_contenttype.equals("S") ) {
	                    ktUtil.createDirectory( contentUploadPath, v_content_cd );
	                } 
                } else {
                	// �����׷� ������ ������ ���� ���
                	isOk = -2;
                }
                
            } else {
            	// ���������� �ȵȰ�� ����
            	isOk = -1;
            }
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
	               	if (isOk > 0) {
	               		connMgr.commit();
	            	}
					connMgr.setAutoCommit(true);					
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
            
        return isOk;
    }

     
    /**
     * �����׷� ����
     * @param DBConnectionManager DB Connection Manager
     * @param v_grcode            �����׷�
     * @param v_subj              �����ڵ�
     * @param v_luserid           ������
     * return isOk               1:insert success,0:insert fail
     */
    public int SaveGrSubj(DBConnectionManager connMgr, String v_grcode, String v_subj, String v_luserid) throws Exception { 
    	PreparedStatement  pstmt   = null;
    	StringBuffer       sbSQL   = new StringBuffer("");
    	int                isOk    = 0;
         
    	try { 
             
    		sbSQL.append(" insert into tz_grsubj                            \n")
				.append(" (                                                \n")
				.append("         grcode                                   \n")
				.append("     ,   subjcourse                               \n")
				.append("     ,   isnew                                    \n")
				.append("     ,   disseq                                   \n")
				.append("     ,   grpcode                                  \n")
				.append("     ,   grpname                                  \n")
				.append("     ,   luserid                                  \n")
				.append("     ,   ldate                                    \n")
				.append(" )                                                \n")
				.append(" values                                           \n")
				.append(" (       ?                                        \n")
				.append("     ,   ?                                        \n")
				.append("     ,   'N'                                      \n")
				.append("     ,   0                                        \n")
				.append("     ,   ''                                       \n")
				.append("     ,   ''                                       \n")
				.append("     ,   ?                                        \n")
				.append("     ,   to_char(sysdate,'YYYYMMDDHH24MISS')      \n")
				.append(" )                                                \n");

    		pstmt   = connMgr.prepareStatement(sbSQL.toString());
             
    		pstmt.setString(1, v_grcode        );
    		pstmt.setString(2, v_subj      );
    		pstmt.setString(3, v_luserid    );
             
    		isOk   = pstmt.executeUpdate();
    		if ( pstmt != null ) { pstmt.close(); }
    	} catch ( Exception e ) { 
    		connMgr.rollback();
    		ErrorManager.getErrorStackTrace(e);
    		throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
    	} finally { 
    		if ( pstmt != null ) { 
    			try { 
    				pstmt.close(); 
    			} catch ( Exception e ) { } 
    		}
    	}
         
    	return isOk;
	}

    
    /**
     * �ű� �����ڵ� ���
     * @param box      receive from the form object and session
     * @return String    1:insert success,0:insert fail
     */
    public String getMaxSubjcode(DBConnectionManager connMgr, String v_upperclass, String v_middleclass) throws Exception {
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        String 	            v_subjcode	     = "";

        // PRF + �⵵ 2�ڸ� + ���� 4 �ڸ�
        try { 
            sbSQL.append("SELECT SUBSTR('"+v_upperclass+"',0,3)||SUBSTR(TO_CHAR(SYSDATE,'YYYY'),3)||NVL(LPAD(MAX(SUBSTR(SUBJ,6))+1,4,'0'),'0001') AS SUBJ FROM TZ_SUBJ WHERE SUBJ LIKE SUBSTR('"+v_upperclass+"',0,3)||SUBSTR(TO_CHAR(SYSDATE,'YYYY'),3)||'%'");
            ls              = connMgr.executeQuery(sbSQL.toString());
       
            while ( ls.next() ) { 
            	v_subjcode   = ls.getString("subj");
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return v_subjcode;
    }

 
    public String getMaxSubjcodeOld(DBConnectionManager connMgr, String v_upperclass, String v_middleclass) throws Exception {
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        String 	            v_subjcode	     = "";
        String 	            v_maxsubj 	     = "";
        
        try { 
            sbSQL.append(" select   nvl((max(substr(subj,0,4))+1), 1000) maxsubj    \n")
                 .append("   from   tz_subj                                         \n")
            	 .append("  where   length(subj) = 4                                \n")
                 .append("    and   0 = regexp_instr(substr(subj,0,1), '[^[:digit:]]') \n");

            ls              = connMgr.executeQuery(sbSQL.toString());
       
            while ( ls.next() ) { 
                v_maxsubj   = ls.getString("maxsubj");
            }
            if ( v_maxsubj.equals("") ) { 
                v_subjcode  = "1000";
            } else { 
//                v_maxno     = Integer.parseInt(v_maxsubj.substring(1));
                v_subjcode  = v_maxsubj;

            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return v_subjcode;
    }
    
    
    /**
     * ������� - ����
     * @param box      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int InsertOffSubject(RequestBox box) throws Exception { 
        DBConnectionManager     connMgr     = null;
        PreparedStatement 		pstmt       = null;
        StringBuffer            sbSQL       = new StringBuffer("");
        int 					isOk 	    = 0;
        int 					isOk2 	    = 1;
        
        String                  v_subj      = "";
        String                  v_luserid 	= box.getSession      ("userid"            );

        int						pidx		= 1;
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // insert tz_subj table
            sbSQL.append(" insert into tz_subj (                                                                                            \n")
                 .append("         subj                , subjnm                    , isonoff               , subjclass                      \n")
                 .append("     ,   upperclass          , middleclass               , lowerclass            , specials                       \n")
                 .append("     ,   muserid             , cuserid                   , isuse                 , ispropose                      \n")
                 .append("     ,   biyong              , edudays                   , studentlimit          , usebook                        \n")
                 .append("     ,   bookprice           , owner                     , producer              , crdate                         \n")
                 .append("     ,   language            , server                    , dir                   , eduurl                         \n")
                 .append("     ,   vodurl              , preurl                    , ratewbt               , ratevod                        \n")
                 .append("     ,   env                 , tutor                     , bookname              , sdesc                          \n")
                 .append("     ,   warndays            , stopdays                  , point                 , edulimit                       \n")
                 .append("     ,   gradscore           , gradstep                  , wstep                 , wmtest                         \n")
                 .append("     ,   wftest              , wreport                   , wact                  , wetc1                          \n")
                 .append("     ,   wetc2               , place                     , edumans               , isessential                    \n")
                 .append("     ,   score               , inuserid                  , indate                , luserid  						\n")
                 .append("     ,   ldate               , proposetype               , edutimes              , edutype                        \n")
                 .append("     ,   intro               , explain                   , gradexam              , gradreport                     \n")
                 .append("     ,   bookfilenamereal    , bookfilenamenew           , conturl               , musertel                       \n")
                 .append("     ,   gradftest           , gradhtest                 , isvisible             , isalledu                       \n")
                 .append("     ,   isapproval          , isintroduction            , eduperiod             , introducefilenamereal          \n")
                 .append("     ,   introducefilenamenew, informationfilenamereal   , informationfilenamenew, ischarge                       \n")
                 .append("     ,   isopenedu           , gradftest_flag            , gradreport_flag                                        \n")
                 .append("     ,   isgoyong            , goyongpricemajor          , goyongpriceminor										\n")
                 .append(" ) values (                                                                                                       \n")
                 .append("         ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                     	   , ?                     , ?	     						\n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                     , ?                              \n")
                 .append("     ,   ?                   , ?                         , ?                                                      \n")
                 .append("	   ,   ?                   , ?                         , ?														\n")
                 .append(" )                                                                                                                \n");
            
            pstmt       = connMgr.prepareStatement(sbSQL.toString());
            v_subj      = getMaxSubjcode(connMgr, box.getString("p_upperclass"), box.getString("p_middleclass") );
            
            pstmt.setString(pidx++, v_subj								 );                     // �����ڵ�
            pstmt.setString(pidx++, box.getString("p_subjnm"            ));                     // �����
            pstmt.setString(pidx++, box.getString("p_isonoff"           ));                     // �¶���/�������α���
            pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000") ); // �з��ڵ�
            pstmt.setString(pidx++, box.getString("p_upperclass"        ));                     // ��з�
            pstmt.setString(pidx++, box.getString("p_middleclass"       ));                     // �ߺз�
            pstmt.setString(pidx++, box.getStringDefault("p_lowerclass","000"));     			// �Һз�
            pstmt.setString(pidx++, box.getString("p_specials"          ));                     // ����Ư��
            pstmt.setString(pidx++, box.getString("p_muserid"           ));                     // ����ID
            pstmt.setString(pidx++, box.getString("p_cuserid"           ));                     // [X]���������ID
            pstmt.setString(pidx++, box.getString("p_isuse"             ));                     // ��뿩��
            pstmt.setString(pidx++, box.getString("p_ispropose"         ));                     // [X]������û����(Y/N)
            pstmt.setInt   (pidx++, box.getInt   ("p_biyong"            ));                     // ������
            pstmt.setInt   (pidx++, box.getInt   ("p_edudays"           ));                     // �����Ⱓ(��)
            pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"      ));                     // ���������
            pstmt.setString(pidx++, box.getString("p_usebook"           ));                     // [X]�����뿩��
            pstmt.setInt   (pidx++, Integer.parseInt(box.getStringDefault("p_bookprice", "0")));// [X]�����
            pstmt.setString(pidx++, box.getString("p_owner"             ));                     // [X]���������
            pstmt.setString(pidx++, box.getString("p_producer"          ));                     // [X]����������
            pstmt.setString(pidx++, box.getString("p_crdate"            ));                     // [X]��������
            pstmt.setString(pidx++, box.getString("p_language"          ));                     // [X]����
            pstmt.setString(pidx++, box.getString("p_server"            ));                     // [X]����
            pstmt.setString(pidx++, box.getString("p_dir"               ));                     // [X]���������
            pstmt.setString(pidx++, box.getString("p_eduurl"            ));                     // [X]����URL
            pstmt.setString(pidx++, box.getString("p_vodurl"            ));                     // [X]VOD���
            pstmt.setString(pidx++, box.getString("p_preurl"            ));                     // ������URL
            pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"           ));                     // [X]�н����(WBT%)
            pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"           ));                     // [X]�н����(VOD%)
            pstmt.setString(pidx++, box.getString("p_env"               ));                     // [X]�н�ȯ��
            pstmt.setString(pidx++, box.getString("p_tutor"             ));                     // [X]���缳��
            pstmt.setString(pidx++, box.getStringDefault("p_bookname",""));     				// [X]�����
            pstmt.setString(pidx++, box.getString("p_sdesc"             ));                     // [X]���
            pstmt.setInt   (pidx++, box.getInt   ("p_warndays"          ));                     // [X]�н����������
            pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"          ));                     // [X]�н�����������
            pstmt.setInt   (pidx++, box.getInt   ("p_point"             ));                     // ��������
            pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"          ));                     // [X]1���ִ��н���
            pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"         ));                     // �������-����
            pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"          ));                     // �������-�⼮��
            pstmt.setDouble(pidx++, box.getDouble("p_wstep"             ));                     // ����ġ-�⼮��
            pstmt.setDouble(pidx++, box.getDouble("p_wmtest"            ));                     // [X]����ġ-�߰���
            pstmt.setDouble(pidx++, box.getDouble("p_wftest"            ));                     // ����ġ-������
            pstmt.setDouble(pidx++, box.getDouble("p_wreport"           ));                     // ����ġ-����
            pstmt.setDouble(pidx++, box.getDouble("p_wact"              ));                     // [X]����ġ-��Ƽ��Ƽ
            pstmt.setDouble(pidx++, box.getDouble("p_wetc1"             ));                     // ����ġ-������
            pstmt.setDouble(pidx++, box.getDouble("p_wetc2"             ));                     // ����ġ-��������
            pstmt.setString(pidx++, box.getString("p_place"             ));                     // �������
            pstmt.setString(pidx++, box.getString("p_edumans"           ));                     // ������ �������
            pstmt.setInt   (pidx++, box.getInt   ("p_score"             ));                     // ��������
            pstmt.setString(pidx++, box.getStringDefault("p_isessential", "0"));     			// ���񱸺�
            pstmt.setString(pidx++, v_luserid                            );                     // ������
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss") );                     // ������
            pstmt.setString(pidx++, v_luserid                            );                     // ����������
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));                      // ����������(ldate)
            pstmt.setString(pidx++, box.getString("p_proposetype"       ));                     // ������û����(TZ_CODE GUBUN='0019')
            pstmt.setInt   (pidx++, box.getInt   ("p_edutimes"          ));                     // �����ð�
            pstmt.setString(pidx++, box.getString("p_edutype"           ));                     // ��������
            pstmt.setString(pidx++, box.getString("p_intro"             ));                     // ������ ��������
            pstmt.setString(pidx++, box.getString("p_explain"           ));                     // ������ ��������
            pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"          ));                     // �������(��)
            pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"        ));                     // �������(����)
            pstmt.setString(pidx++, box.getRealFileName("p_file"        ));                     // �������ϸ�
            pstmt.setString(pidx++, box.getNewFileName("p_file"         ));                     // ��������DB��
            pstmt.setString(pidx++, box.getString("p_conturl"           ));                     // �н�����URL
            pstmt.setString(pidx++, box.getString("p_musertel"          ));                     // ���� ��ȭ��ȣ
            pstmt.setInt   (pidx++, box.getInt   ("p_gradftest"         ));                     // �������-������
            pstmt.setInt   (pidx++, box.getInt   ("p_gradhtest"         ));                     // �������-������
            pstmt.setString(pidx++, box.getStringDefault("p_isvisible", "Y"));     			  	// �н��ڿ��� �����ֱ�
            pstmt.setString(pidx++, box.getString("p_isalledu"          ));                     // ���米������
            pstmt.setString(pidx++, "Y"                                  );                     // ������ο��� (���⼱ �׻� Y)
            pstmt.setString(pidx++, box.getString("p_isintroduction"    ));                     // ����Ұ� ��뿩��
            pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"         ));                     // �н��Ⱓ
            pstmt.setString(pidx++, box.getRealFileName("p_introducefile"   ));                 // ����Ұ� �̹���
            pstmt.setString(pidx++, box.getNewFileName ("p_introducefile"   ));                 // ����Ұ� �̹���
            pstmt.setString(pidx++, box.getRealFileName("p_informationfile" ));                 // ����(����)
            pstmt.setString(pidx++, box.getNewFileName ("p_informationfile" ));                 // ����(����)
            pstmt.setString(pidx++, box.getString("p_ischarge"        ));						// ������ ��/���� ����
            pstmt.setString(pidx++, box.getString("p_isopenedu"       ));                 	  	// ���� ���� ����
            pstmt.setString(pidx++, box.getString("p_gradftest_flag"   ));                	  	// �������-��(�ʼ�/���ÿ���)
            pstmt.setString(pidx++, box.getString("p_gradreport_flag" ));                 	  	// �������-����(�ʼ�/���ÿ���)
            pstmt.setString(pidx++, box.getString("p_isgoyong")         );               		// ��뺸�迩��(Y/N)
            pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_major"));      				// ��뺸�� ȯ�޾�-ȯ�޾�(KT)      
            pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_minor"));      				// ��뺸�� ȯ�޾�-KT������
            
            isOk        = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            // 2008.11.30 ����� ����
            // ������ �������� ���ȭ�鿡�� �ߴ� �����׷������ �������� ���, ����ȭ�鿡�� ���� �Է��ϴ°����� ����.
            //isOk2 = RelatedGrcodeInsert(connMgr, box);
            isOk2	= 1;
            
            if ( isOk == 1 && isOk2 != -1) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
     }            

     
     /**
      * �������� - �̷���
      * @param box      receive from the form object and session
      * @return isOk    1:update success,0:update fail
      */
     public int UpdateSubject(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr                     = null;
         PreparedStatement      pstmt                       = null;
         StringBuffer           sbSQL                       = new StringBuffer("");
         int                    isOk                        = 0;
         int                    isOk2                       = 0;

         String                 v_luserid                   = box.getSession("userid");
         
         String                 v_introducefilenamereal     = box.getRealFileName("p_introducefile"     );
         String                 v_introducefilenamenew      = box.getNewFileName ("p_introducefile"     );
         String                 v_informationfilenamereal   = box.getRealFileName("p_informationfile"   );
         String                 v_informationfilenamenew    = box.getNewFileName ("p_informationfile"   );
                                 
         String                 v_introducefile0            = box.getStringDefault("p_introducefile0"  , "0");
         String                 v_informationfile0          = box.getStringDefault("p_informationfile0", "0");
         
         // v_dir�� p_contenttype�� Normal,OBC,SCORM����϶� dp/content/�����ڵ带 �־��ش�.
         String                     v_dir                       = "";
         String                     v_contenttype               = box.getString("p_contenttype");
         
         int pidx = 1;
         
         try { 
             connMgr    = new DBConnectionManager();
             connMgr.setAutoCommit(false);
        	 
             if ( v_introducefilenamereal.length() == 0 ) { 
                 if ( v_introducefile0.equals("1") ) { 
                     v_introducefilenamereal                    = "";
                     v_introducefilenamenew                     = "";
                 } else { 
                     v_introducefilenamereal                    = box.getString("p_introducefile1");
                     v_introducefilenamenew                     = box.getString("p_introducefile2");
                 }
             }
     
             if ( v_informationfilenamereal.length() == 0 ) { 
                 if ( v_informationfile0.equals("1") ) { 
                     v_informationfilenamereal              = "";
                     v_informationfilenamenew               = "";
                 } else { 
                     v_informationfilenamereal              = box.getString("p_informationfile1");
                     v_informationfilenamenew               = box.getString("p_informationfile2");
                 }
             }

             // KT���丮 ��ũ�����ÿ� ��ȣȭ
             KTUtil ktUtil = KTUtil.getInstance();
             
             // �ű԰����ڵ� ���� (KT���簳�߿������� �����Է¹����Ƿ� �ּ�ó��)
             //v_subj      = getMaxSubjcode(connMgr, box.getString("p_upperclass"), box.getString("p_middleclass") );
             
             String v_subj = box.getString("p_subj");
             String v_old_content_cd = box.getString ("p_old_content_cd"); // ���� �������ڵ� �ڵ�
             String v_content_cd = box.getString ("p_content_cd");
             
             ConfigSet conf = new ConfigSet();
             String contentPath = conf.getProperty("dir.content.path");
             String ktContentPath = conf.getProperty("dir.kt_content.path");
             String contentUploadPath = conf.getProperty("dir.content.upload");
             String ktContentUploadPath = conf.getProperty("dir.kt_content.upload");
             String ktContentLink = conf.getProperty("dir.kt_content.link");
             
             // ���丮��δ� ������Ÿ�Կ� ���� �����ϴ�.
             // ������Ÿ���� N(Normal), O(OBC), OA(OBC-Author), S(SCORM) �϶�
             if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") || v_contenttype.equals("S") ) {
             	v_dir = contentPath + v_subj;
             	v_content_cd = v_subj; // �ش� ������Ÿ���� �������ڵ�� �����ڵ�� �����ϴ�. KT�������ϰ��� ���� �Է¹޴´�.
             } 
             // KT�������� �϶�
             else if ( v_contenttype.equals("K")) {
                 v_dir = ktContentPath + v_content_cd;
             }
             // �� �̿��� ������Ÿ�� L(Link) ��� �϶�
             else {
             	v_dir = "";
             	v_content_cd = "";
             }
             

             // update tz_subj table
             sbSQL.append(" update tz_subj set                           \n")
                  .append("         subjnm                  = ?          \n")
                  .append("     ,   isonoff                 = ?          \n")
                  .append("     ,   subjclass               = ?          \n")
                  .append("     ,   upperclass              = ?          \n")
                  .append("     ,   middleclass             = ?          \n")
                  .append("     ,   lowerclass              = ?          \n")
                  .append("     ,   specials                = ?          \n")
                  .append("     ,   muserid                 = ?          \n")
                  .append("     ,   cuserid                 = ?          \n")
                  .append("     ,   isuse                   = ?          \n")
                  .append("     ,   ispropose               = ?          \n")
                  .append("     ,   biyong                  = ?          \n")
                  .append("     ,   ischarge                = ?          \n")
                  .append("     ,   edudays                 = ?          \n")
                  .append("     ,   studentlimit            = ?          \n")
                  .append("     ,   usebook                 = ?          \n")
                  .append("     ,   bookprice               = ?          \n")
                  .append("     ,   owner                   = ?          \n")
                  .append("     ,   producer                = ?          \n")
                  .append("     ,   crdate                  = ?          \n")
                  .append("     ,   language                = ?          \n")
                  .append("     ,   dir                     = ?          \n")
                  .append("     ,   eduurl                  = ?          \n")
                  .append("     ,   preurl                  = ?          \n")
                  .append("     ,   ratewbt                 = ?          \n")
                  .append("     ,   ratevod                 = ?          \n")
                  .append("     ,   env                     = ?          \n")
                  .append("     ,   tutor                   = ?          \n")
                  .append("     ,   bookname                = ?          \n")
                  .append("     ,   sdesc                   = ?          \n")
                  .append("     ,   warndays                = ?          \n")
                  .append("     ,   stopdays                = ?          \n")
                  .append("     ,   point                   = ?          \n")
                  .append("     ,   edulimit                = ?          \n")
                  .append("     ,   gradscore               = ?          \n")
                  .append("     ,   gradstep                = ?          \n")
                  .append("     ,   wstep                   = ?          \n")
                  .append("     ,   wmtest                  = ?          \n")
                  .append("     ,   wftest                  = ?          \n")
                  .append("     ,   wreport                 = ?          \n")
                  .append("     ,   wact                    = ?          \n")
                  .append("     ,   wetc1                   = ?          \n")
                  .append("     ,   wetc2                   = ?          \n")
                  .append("     ,   luserid                 = ?          \n")
                  .append("     ,   ldate                   = ?          \n")
                  .append("     ,   proposetype             = ?          \n")
                  .append("     ,   edumans                 = ?          \n")
                  .append("     ,   intro                   = ?          \n")
                  .append("     ,   explain                 = ?          \n")
                  .append("     ,   contenttype             = ?          \n")
                  .append("     ,   gradexam                = ?          \n")
                  .append("     ,   gradreport              = ?          \n")
                  .append("     ,   whtest                  = ?          \n")
                  .append("     ,   isablereview            = ?          \n")
                  .append("     ,   score                   = ?          \n")
                  .append("     ,   isoutsourcing           = ?          \n")
                  .append("     ,   conturl                 = ?          \n")
                  .append("     ,   isessential             = ?          \n")
                  .append("     ,   musertel                = ?          \n")
                  .append("     ,   gradftest               = ?          \n")
                  .append("     ,   gradhtest               = ?          \n")
                  .append("     ,   isvisible               = ?          \n")
                  .append("     ,   isalledu                = ?          \n")
                  .append("     ,   edutimes                = ?          \n")
                  .append("     ,   isintroduction          = ?          \n")
                  .append("     ,   eduperiod               = ?          \n")
                  .append("     ,   introducefilenamereal   = ?          \n")
                  .append("     ,   introducefilenamenew    = ?          \n")
                  .append("     ,   informationfilenamereal = ?          \n")
                  .append("     ,   informationfilenamenew  = ?          \n")
                  .append("     ,   contentgrade            = ?          \n")
                  .append("     ,   memo                    = ?          \n")
                  .append("     ,   isopenedu               = ?          \n")
                  .append("     ,   gradftest_flag          = ?          \n")
                  .append("     ,   gradexam_flag           = ?          \n")
                  .append("     ,   gradhtest_flag          = ?          \n")
                  .append("     ,   gradreport_flag         = ?          \n")
                  .append("     ,   isgoyong				= ?			 \n")
                  .append(" 	,   goyongpricemajor		= ? 		 \n")
                  .append(" 	,   goyongpriceminor		= ? 		 \n")
                  .append(" 	,   subj_gu					= ? 		 \n")
                  .append(" 	,   content_cd				= ? 		 \n")
                  .append(" 	,   study_count				= ? 		 \n")
                  .append(" 	,   reviewdays				= ? 		 \n")
                  .append(" 	,   lev						= ? 		 \n")
                  .append(" 	,   gubn					= ? 		 \n")
                  .append(" 	,   grade					= ? 		 \n")
                  .append(" 	,   test					= ? 		 \n")
                  .append(" 	,   study_time				= ? 		 \n")
                  .append(" 	,   cp_accrate				= ? 		 \n")
                  .append(" 	,   goyongpricestand		= ? 		 \n")
                  .append(" 	,   getmethod				= ? 		 \n")
                  .append(" 	,   cp						= ? 		 \n")
                  .append(" 	,   firstdate				= ? 		 \n")
                  .append(" 	,   judgedate				= ? 		 \n")
                  .append(" 	,   getdate					= ? 		 \n")
                  .append(" 	,   iscustomedu				= ? 		 \n")
                  .append(" 	,   cp_account				= ? 		 \n")
                  .append(" 	,   cp_vat					= ? 		 \n")                  
                  .append(" 	,   contentprogress			= ? 		 \n")                  
                  .append(" 	,   cpsubj			        = ? 		 \n")                  
                  .append(" where   subj                    = ?          \n");
             
             pstmt = connMgr.prepareStatement(sbSQL.toString());

             pstmt.setString(pidx++, box.getString("p_subjnm"        ));
             pstmt.setString(pidx++, box.getString("p_isonoff"       ));
             pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000") ); // �з��ڵ�
             pstmt.setString(pidx++, box.getString("p_upperclass"    ));                 // ��з�
             pstmt.setString(pidx++, box.getString("p_middleclass"   ));                 // �ߺз�
             pstmt.setString(pidx++, box.getStringDefault("p_lowerclass"     ,"000"));   // �Һз�
             pstmt.setString(pidx++, box.getString("p_specials"      ));
             pstmt.setString(pidx++, box.getString("p_muserid"       ));
             pstmt.setString(pidx++, box.getString("p_cuserid"       ));
             pstmt.setString(pidx++, box.getString("p_isuse"         ));
             pstmt.setString(pidx++, box.getString("p_ispropose"     ));
             pstmt.setInt   (pidx++, box.getInt   ("p_biyong"        ));
             pstmt.setString(pidx++, box.getStringDefault("p_ischarge"       , "N"));
             pstmt.setInt   (pidx++, box.getInt   ("p_edudays"       ));
             pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"  ));
             pstmt.setString(pidx++, box.getString("p_usebook"       ));
             pstmt.setString(pidx++, box.getStringDefault("p_bookprice"      , "0"));
             pstmt.setString(pidx++, box.getString("p_owner"         ));
             pstmt.setString(pidx++, box.getString("p_producer"      ));
             pstmt.setString(pidx++, box.getString("p_crdate"        ));
             pstmt.setString(pidx++, box.getString("p_language"      ));
             pstmt.setString(pidx++, v_dir                            );
             pstmt.setString(pidx++, box.getString("p_eduurl"        ));
             pstmt.setString(pidx++, box.getString("p_preurl"        ));
             pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"       ));
             pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"       ));
             pstmt.setString(pidx++, box.getString("p_env"           ));
             pstmt.setString(pidx++, box.getString("p_tutor"         ));
             pstmt.setString(pidx++, box.getStringDefault("p_bookname"   ,""));
             pstmt.setString(pidx++, box.getString("p_sdesc"         ));
             pstmt.setInt   (pidx++, box.getInt   ("p_warndays"      ));
             pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"      ));
             pstmt.setInt   (pidx++, box.getInt   ("p_point"         ));
             pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"      ));
             pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"     ));
             pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"      ));
             pstmt.setDouble(pidx++, box.getDouble("p_wstep"         ));
             pstmt.setDouble(pidx++, box.getDouble("p_wmtest"        ));
             pstmt.setDouble(pidx++, box.getDouble("p_wftest"        ));
             pstmt.setDouble(pidx++, box.getDouble("p_wreport"       ));
             pstmt.setDouble(pidx++, box.getDouble("p_wact"          ));
             pstmt.setDouble(pidx++, box.getDouble("p_wetc1"         ));
             pstmt.setDouble(pidx++, box.getDouble("p_wetc2"         ));
             pstmt.setString(pidx++, v_luserid                        );
             pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss") );
             pstmt.setString(pidx++, box.getString("p_proposetype"   ));
             pstmt.setString(pidx++, box.getString("p_edumans"       ));
             pstmt.setString(pidx++, box.getString("p_intro"         ));
             pstmt.setString(pidx++, box.getString("p_explain"       ));
             pstmt.setString(pidx++, box.getString("p_contenttype"   ));
             pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"      ));                    // �������(��)
             pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"    ));                    // �������(����)
             pstmt.setDouble(pidx++, box.getDouble("p_whtest"   ));                   		// ����ġ(������)
             pstmt.setString(pidx++, box.getString("p_isablereview"  ));                    // �������ɿ���
             pstmt.setInt   (pidx++, box.getInt   ("p_score"         ));                    // ��������
             pstmt.setString(pidx++, box.getString("p_isoutsourcing" ));                    // ��Ź��������
             pstmt.setString(pidx++, box.getString("p_conturl"       ));                    // �н�����URL
             pstmt.setString(pidx++, box.getStringDefault("p_isessential"        , "0"));   // ���񱸺�
             pstmt.setString(pidx++, box.getString("p_musertel"      ));                    // ���� ��ȭ��ȣ
             pstmt.setInt(pidx++, box.getInt("p_gradftest"     ));                    		// �������-������
             pstmt.setInt(pidx++, box.getInt("p_gradhtest"     ));                    		// �������-������
             pstmt.setString(pidx++, box.getStringDefault("p_isvisible"          , "N"));   // �н��ڿ��� �����ֱ�
             pstmt.setString(pidx++, box.getString("p_isalledu"      ));                    // ���米������
             pstmt.setString(pidx++, box.getString("p_edutimes"      ));                    // �н��ð�
             pstmt.setString(pidx++, box.getString("p_isintroduction"));                    // �н��Ұ� ��뿩��
             pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"     ));                    // �н��Ⱓ
             pstmt.setString(pidx++, v_introducefilenamereal          );                    // ����Ұ� �̹���
             pstmt.setString(pidx++, v_introducefilenamenew           );                    // ����Ұ� �̹���
             pstmt.setString(pidx++, v_informationfilenamereal        );                    // ����(����)
             pstmt.setString(pidx++, v_informationfilenamenew         );                    // ����(����)
             pstmt.setString(pidx++, box.getString("p_contentgrade"  ));                    // ���������
             pstmt.setString(pidx++, box.getString("p_memo"          ));                    // ���
             pstmt.setString(pidx++, box.getString("p_isopenedu"     ));                    // ���� ���� ����
             pstmt.setString(pidx++, box.getString("p_gradftest_flag"));                    // �������-������(�ʼ�/���ÿ���)
             pstmt.setString(pidx++, box.getString("p_gradexam_flag" ));                    // �������-�߰���(�ʼ�/���ÿ���)
             pstmt.setString(pidx++, box.getString("p_gradhtest_flag"));                    // �������-������(�ʼ�/���ÿ���)
             pstmt.setString(pidx++, box.getString("p_gradreport_flag"));                   // �������-����       (�ʼ�/���ÿ���)
             pstmt.setString(pidx++, box.getString("p_isgoyong"));               			// ��뺸�迩��(Y/N)
             pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_major"));      			// ��뺸�� ȯ�޾�-ȯ�޾�(KT)
             pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_minor"));      			// ��뺸�� ȯ�޾�-KT������
             pstmt.setString(pidx++, box.getString("p_subj_gu"));      						// �������� (�Ϲ�, JIT:J, ��������:M)
             pstmt.setString(pidx++, box.getString("p_content_cd"));      					// �������ڵ� (Normal��� ����)
             pstmt.setInt   (pidx++, box.getInt   ("p_study_count"));      					// ����Ƚ��(�������)
             pstmt.setInt   (pidx++, box.getInt   ("p_reviewdays"));      					// �������ɱⰣ

             pstmt.setString(pidx++, box.getString("p_lev"));      							// ��������
             pstmt.setString(pidx++, box.getString("p_gubn"));      						// �̼�����
             pstmt.setString(pidx++, box.getString("p_grade"));      						// �������
             pstmt.setString(pidx++, box.getString("p_test"));      						// ������

             pstmt.setInt   (pidx++, box.getInt   ("p_study_time"));      					// �������-�ð�
             pstmt.setInt   (pidx++, box.getInt   ("p_cp_accrate"));      					// CP������

             pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_stand"));      			// ��뺸�� ȯ�޾�-CP������
             pstmt.setString(pidx++, box.getString("p_getmethod"));      					// ������Ȯ��-���
             pstmt.setString(pidx++, box.getString("p_cp"));      							// ������Ȯ��-CP
             pstmt.setString(pidx++, box.getString("p_firstdate"));      					// �����⵵-����Ȯ��
             pstmt.setString(pidx++, box.getString("p_judgedate"));      					// �����⵵-�ɻ翬��
             pstmt.setString(pidx++, box.getString("p_getdate"));      						// �����⵵-��������
             pstmt.setString(pidx++, box.getStringDefault("p_iscustomedu","N"));			// ��������������(Y/N)

             pstmt.setInt(pidx++, box.getInt("p_cp_account"));								// CP����ݾ�
             pstmt.setInt(pidx++, box.getInt("p_cp_vat"));									// CP VAT 
             pstmt.setString(pidx++, box.getString("p_contentprogress"));					// ����üũ(����(Y)/����(N)) 
             pstmt.setString(pidx++, box.getString("p_cpsubj"));					        // CP�����ڵ� 
             
             pstmt.setString(pidx++, v_subj);

             isOk   = pstmt.executeUpdate();
             
             pstmt.close();
             
             // �ʱ�ȭ
             sbSQL.setLength(0);
             
             // ����� ���� ����� ����
             sbSQL.append(" update tz_subjseq  set subjnm = ? where subj = ? ");
             
             pstmt  = connMgr.prepareStatement(sbSQL.toString());

             pstmt.setString(1, box.getString("p_subjnm"));
             pstmt.setString(2, v_subj);

             isOk  += pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
             // 2008.11.30 ����� ����
             // ������ �������� ���ȭ�鿡�� �ߴ� �����׷������ �������� ���, ����ȭ�鿡�� ���� �Է��ϴ°����� ����.
             //isOk2 = RelatedGrcodeInsert(connMgr, box);
             isOk2 = 1;
             if (isOk > 0) {
            	 if ( isOk2 != -1) {
	                 // ���丮��δ� ������Ÿ�Կ� ���� �����ϴ�.
	                 // ������Ÿ���� N(Normal), O(OBC), OA(OBC-Author), S(SCORM) �϶�
	                 if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") || v_contenttype.equals("S") ) {
	                	 ktUtil.createDirectory( contentUploadPath, v_content_cd );
	                 } else if (v_contenttype.equals("K")) {
	                	 String md5_content_cd = ktUtil.md5Encode(v_content_cd);
	                	 //String md5_old_content_cd = ktUtil.md5Encode(v_old_content_cd);
	                	 if (!v_content_cd.equals(v_old_content_cd)) {
			                 // 2008.11.29 ����� KT���簳�߿��� �°� ����.
			                 // KT �������̸鼭 �������ڵ尡 ������ �Ǿ��� ���, ������ ���丮�� ���� �������ְ�,
			            	 // ���� �������ڵ�� ������ ��ũ���� �������ָ�, ���ο� �������ڵ�� ��ũ�� �ɾ��ش�.
	                		 //if (!"".equals(md5_old_content_cd)) {
	                			 //ktUtil.deleteDirectory(ktContentLink, md5_old_content_cd);
	                		 //}
	                	 }
	                	 if (!"".equals(md5_content_cd)) {
		                	 ktUtil.createDirectory( ktContentUploadPath, v_content_cd );
			                 ktUtil.createLink( ktContentUploadPath + v_content_cd, ktContentLink + md5_content_cd );
	                	 } else {
	                		 // ��ȣȭ�� �������ڵ尡 ���� ��� ����
	                		 isOk = -3;
	                	 }
	                 }
            	 } else {
            		 // �����׷� ������ ������ ���� ���
            		 isOk = -2; 
            	 }
             
             } else {
            	 // ���� ������ �ȵǾ��� ���
            	 isOk = -1;
             }
         } catch ( SQLException e ) {
             if ( connMgr != null ) { 
                 try { 
                     connMgr.rollback();
                 } catch ( Exception ex ) { } 
             }
             
             ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
             throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } catch ( Exception e ) {
             if ( connMgr != null ) { 
                 try { 
                     connMgr.rollback();
                 } catch ( Exception ex ) { } 
             }
             
             ErrorManager.getErrorStackTrace(e, box, "");
             throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } finally {
             if ( pstmt != null ) { 
                 try { 
                     pstmt.close();  
                 } catch ( Exception e ) { } 
             }
             
             if ( connMgr != null ) { 
                 try { 
                	 if (isOk > 0) {
                		 connMgr.commit();
                	 }
                	 connMgr.setAutoCommit(true);
                     connMgr.freeConnection(); 
                 } catch ( Exception e ) { } 
             }
         }

         return isOk;
     }

     
    /**
     * �������� - ����
     * @param box      receive from the form object and session
     * @return isOk    1:update success,0:update fail
     */
    public int UpdateOffSubject(RequestBox box) throws Exception { 
        DBConnectionManager connMgr                     = null;
        PreparedStatement 	pstmt                       = null;
        ListSet             ls                          = null;
        StringBuffer        sbSQL 		                = new StringBuffer("");
        int 				isOk                        = 0;
        int 				isOk2                       = 0;
        
        String 				v_luserid 	                = box.getSession("userid");
        // ���� ����
        String              v_oldbookfilenamereal       = "";
        String              v_oldbookfilenamenew        = "";
        String              v_introducefilenamereal     = box.getRealFileName ("p_introducefile"         );
        String              v_introducefilenamenew      = box.getNewFileName  ("p_introducefile"         );
        String              v_informationfilenamereal   = box.getRealFileName ("p_informationfile"       );
        String              v_informationfilenamenew    = box.getNewFileName  ("p_informationfile"       );
        String              v_introducefile0            = box.getStringDefault("p_introducefile0"   , "0");
        String              v_informationfile0          = box.getStringDefault("p_informationfile0" , "0");
        
        int pidx = 1;

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL.append(" select bookfilenamereal, bookfilenamenew from tz_subj where subj = " + StringManager.makeSQL(box.getString("p_subj")) + " \n");

            ls          = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                 v_oldbookfilenamereal 			= ls.getString("bookfilenamereal" );
                 v_oldbookfilenamenew 			= ls.getString("bookfilenamenew"  );
            }

	        if ( v_introducefilenamereal.length() == 0 ) { 
	            if ( v_introducefile0.equals("1") ) { 
	                v_introducefilenamereal    		= "";
	                v_introducefilenamenew     		= "";
	            } else { 
	                v_introducefilenamereal    		= box.getString("p_introducefile1");
	                v_introducefilenamenew     		= box.getString("p_introducefile2");
	            }
	        }
	
	        if ( v_informationfilenamereal.length() == 0 ) { 
	            if ( v_informationfile0.equals("1") ) { 
	                v_informationfilenamereal    	= "";
	                v_informationfilenamenew     	= "";
	            } else { 
	                v_informationfilenamereal    	= box.getString("p_informationfile1");
	                v_informationfilenamenew     	= box.getString("p_informationfile2");
	            }
	        }
            
            sbSQL.setLength(0);

            // update tz_subj table
            sbSQL.append(" update tz_subj set                               \n")
                 .append("         subjnm                  = ?              \n")
                 .append("     ,   isonoff                 = ?              \n")
                 .append("     ,   subjclass               = ?              \n")
                 .append("     ,   upperclass              = ?              \n")
                 .append("     ,   middleclass             = ?              \n")
                 .append("     ,   lowerclass              = ?              \n")
                 .append("     ,   specials                = ?              \n")
                 .append("     ,   muserid                 = ?              \n")
                 .append("     ,   cuserid                 = ?              \n")
                 .append("     ,   isuse                   = ?              \n")
                 .append("     ,   ispropose               = ?              \n")
                 .append("     ,   biyong                  = ?              \n")
                 .append("     ,   edudays                 = ?              \n")
                 .append("     ,   studentlimit            = ?              \n")
                 .append("     ,   usebook                 = ?              \n")
                 .append("     ,   bookprice               = ?              \n")
                 .append("     ,   owner                   = ?              \n")
                 .append("     ,   producer                = ?              \n")
                 .append("     ,   crdate                  = ?              \n")
                 .append("     ,   language                = ?              \n")
                 .append("     ,   ratewbt                 = ?              \n")
                 .append("     ,   ratevod                 = ?              \n")
                 .append("     ,   env                     = ?              \n")
                 .append("     ,   tutor                   = ?              \n")
                 .append("     ,   bookname                = ?              \n")
                 .append("     ,   sdesc                   = ?              \n")
                 .append("     ,   warndays                = ?              \n")
                 .append("     ,   stopdays                = ?              \n")
                 .append("     ,   point                   = ?              \n")
                 .append("     ,   edulimit                = ?              \n")
                 .append("     ,   gradscore               = ?              \n")
                 .append("     ,   gradstep                = ?              \n")
                 .append("     ,   wstep                   = ?              \n")
                 .append("     ,   wmtest                  = ?              \n")
                 .append("     ,   wftest                  = ?              \n")
                 .append("     ,   wreport                 = ?              \n")
                 .append("     ,   wact                    = ?              \n")
                 .append("     ,   wetc1                   = ?              \n")
                 .append("     ,   wetc2                   = ?              \n")
                 .append("     ,   place                   = ?              \n")
                 .append("     ,   edumans                 = ?              \n")
                 .append("     ,   luserid                 = ?              \n")
                 .append("     ,   ldate                   = ?              \n")
                 .append("     ,   proposetype             = ?              \n")
                 .append("     ,   edutimes                = ?              \n")
                 .append("     ,   edutype                 = ?              \n")
                 .append("     ,   intro                   = ?              \n")
                 .append("     ,   explain                 = ?              \n")
                 //.append("     ,   rndjijung               = ?              \n")
                 .append("     ,   bookfilenamereal        = ?              \n")
                 .append("     ,   bookfilenamenew         = ?              \n")
                 .append("     ,   gradexam                = ?              \n")
                 .append("     ,   gradreport              = ?              \n")
                 //.append("     ,   usesubjseqapproval      = ?              \n")
                 //.append("     ,   useproposeapproval      = ?              \n")
                 //.append("     ,   usemanagerapproval      = ?              \n")
                 //.append("     ,   rndcreditreq            = ?              \n")
                 //.append("     ,   rndcreditchoice         = ?              \n")
                 //.append("     ,   rndcreditadd            = ?              \n")
                 //.append("     ,   rndcreditdeduct         = ?              \n")
                 .append("     ,   isessential             = ?              \n")
                 .append("     ,   score                   = ?              \n")
                 .append("     ,   musertel                = ?              \n")
                 .append("     ,   gradftest               = ?              \n")
                 .append("     ,   gradhtest               = ?              \n")
                 .append("     ,   isvisible               = ?              \n")
                 .append("     ,   isalledu                = ?              \n")
                 .append("     ,   placejh                 = ?              \n")
                 .append("     ,   isintroduction          = ?              \n")
                 .append("     ,   eduperiod               = ?              \n")
                 .append("     ,   introducefilenamereal   = ?              \n")
                 .append("     ,   introducefilenamenew    = ?              \n")
                 .append("     ,   informationfilenamereal = ?              \n")
                 .append("     ,   informationfilenamenew  = ?              \n")
                 .append("     ,   ischarge                = ?              \n")
                 .append("     ,   isopenedu               = ?              \n")
                 //.append("     ,   maleassignrate          = ?              \n")
                 .append("     ,   gradftest_flag          = ?              \n")
                 .append("     ,   gradreport_flag         = ?              \n")
                 .append("     ,   isgoyong				   = ?			 	\n")
                 .append(" 	   ,   goyongpricemajor		   = ? 		 		\n")
                 .append(" 	   ,   goyongpriceminor		   = ? 		 		\n")
                 .append(" 	   ,   lev		   			   = ? 		 		\n")
                 .append(" 	   ,   gubn		   			   = ? 		 		\n")
                 .append(" 	   ,   grade		   		   = ? 		 		\n")
                 .append(" 	   ,   test		   			   = ? 		 		\n")
                 .append(" 	   ,   org		   			   = ? 		 		\n")
                 .append(" 	   ,   goyongpricestand		   = ? 		 		\n")
                 .append(" where   subj                    = ?              \n");
            
       
            pstmt   = connMgr.prepareStatement(sbSQL.toString());


            pstmt.setString(pidx++, box.getString("p_subjnm"        ));
            pstmt.setString(pidx++, box.getString("p_isonoff"       ));
            pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") +  box.getStringDefault("p_lowerclass" ,"000") );
            pstmt.setString(pidx++, box.getString("p_upperclass"    ));
            pstmt.setString(pidx++, box.getString("p_middleclass"   ));
            pstmt.setString(pidx++, box.getStringDefault("p_lowerclass"     , "000"));
            pstmt.setString(pidx++, box.getString("p_specials"      ));
            pstmt.setString(pidx++, box.getString("p_muserid"       ));
            pstmt.setString(pidx++, box.getString("p_cuserid"       ));
            pstmt.setString(pidx++, box.getString("p_isuse"         ));
            pstmt.setString(pidx++, box.getString("p_ispropose"     ));
            pstmt.setInt   (pidx++, box.getInt   ("p_biyong"        ));
            pstmt.setInt   (pidx++, box.getInt   ("p_edudays"       ));
            pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"  ));
            pstmt.setString(pidx++, box.getString("p_usebook"       ));
            pstmt.setInt(pidx++, Integer.parseInt(box.getStringDefault("p_bookprice"      , "0"   )));
            pstmt.setString(pidx++, box.getString("p_owner"         ));
            pstmt.setString(pidx++, box.getString("p_producer"      ));
            pstmt.setString(pidx++, box.getString("p_crdate"        ));
            pstmt.setString(pidx++, box.getString("p_language"      ));
            pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"       ));
            pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"       ));
            pstmt.setString(pidx++, box.getString("p_env"           ));
            pstmt.setString(pidx++, box.getString("p_tutor"         ));
            pstmt.setString(pidx++, box.getStringDefault("p_bookname"       , ""    ));
            pstmt.setString(pidx++, box.getString("p_sdesc"         ));
            pstmt.setInt   (pidx++, box.getInt   ("p_warndays"      ));
            pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"      ));
            pstmt.setInt   (pidx++, box.getInt   ("p_point"         ));
            pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"      ));
            pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"     ));
            pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"      ));
            pstmt.setDouble(pidx++, box.getDouble("p_wstep"         ));
            pstmt.setDouble(pidx++, box.getDouble("p_wmtest"        ));
            pstmt.setDouble(pidx++, box.getDouble("p_wftest"        ));
            pstmt.setDouble(pidx++, box.getDouble("p_wreport"       ));
            pstmt.setDouble(pidx++, box.getDouble("p_wact"          ));
            pstmt.setDouble(pidx++, box.getDouble("p_wetc1"         ));
            pstmt.setDouble(pidx++, box.getDouble("p_wetc2"         ));
            pstmt.setString(pidx++, box.getString("p_place"         ));
            pstmt.setString(pidx++, box.getString("p_edumans"       ));
            pstmt.setString(pidx++, v_luserid                        );
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(pidx++, box.getString("p_proposetype"   ));
            pstmt.setInt   (pidx++, box.getInt   ("p_edutimes"      ));
            pstmt.setString(pidx++, box.getString("p_edutype"       ));
            pstmt.setString(pidx++, box.getString("p_intro"         ));
            pstmt.setString(pidx++, box.getString("p_explain"       ));
            //pstmt.setString(pidx++, box.getString("p_rndjijung"     ));

            if ( box.getString("p_deletefile").equals("Y") ) { 
                pstmt.setString(pidx++, ""                           );                         // �������ϸ�
                pstmt.setString(pidx++, ""                           );                         // ��������DB��
            } else {                                                                        
                if ( !box.getRealFileName("p_file").equals("") ) {                          
                    pstmt.setString(pidx++, box.getRealFileName("p_file"));                     // �������ϸ�
                    pstmt.setString(pidx++, box.getNewFileName ("p_file"));                     // ��������DB��
                } else {                                                                    
                    pstmt.setString(pidx++, v_oldbookfilenamereal   );                          // �������ϸ�
                    pstmt.setString(pidx++, v_oldbookfilenamenew    );                          // ��������DB��
                }
            }

            pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"          ));                     // �������(��)
            pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"        ));                     // �������(����)
            //pstmt.setString(pidx++, box.getString("p_usesubjseqapproval"));                     // ��������ְ�����(Y/N)
            //pstmt.setString(pidx++, box.getString("p_useproposeapproval"));                     // ������û��������(Y/N)
            //pstmt.setString(pidx++, box.getString("p_usemanagerapproval"));                     // �ְ������������(Y/N)
            //pstmt.setString(pidx++, box.getStringDefault("p_rndcreditreq"       , "0"   ));     // ��������(��������)-�ʼ�
            //pstmt.setString(pidx++, box.getStringDefault("p_rndcreditchoice"    , "0"   ));     // ��������(��������)-����
            //pstmt.setString(pidx++, box.getStringDefault("p_rndcreditadd"       , "0"   ));     // ��������(��������)-��������
            //pstmt.setString(pidx++, box.getStringDefault("p_rndcreditdeduct"    , "0"   ));     // ��������(��������)-��������
            pstmt.setString(pidx++, box.getStringDefault("p_isessential"        , "0"   ));     // ���񱸺�
            pstmt.setDouble(pidx++, box.getDouble("p_score"             ));                     // 
            pstmt.setString(pidx++, box.getString("p_musertel"          ));                     // ���� ��ȭ��ȣ
            pstmt.setInt(pidx++, box.getInt("p_gradftest"         ));                     // �������-������
            pstmt.setInt(pidx++, box.getInt("p_gradhtest"         ));                     // �������-������
            pstmt.setString(pidx++, box.getStringDefault("p_isvisible"          , "N"   ));     // �н��ڿ��� �����ֱ�
            pstmt.setString(pidx++, box.getString("p_isalledu"          ));                     // ���米������
            pstmt.setString(pidx++, box.getString("p_placejh"           ));                     // �������
            pstmt.setString(pidx++, box.getString("p_isintroduction"    ));                     // �н��Ұ� ��뿩��
            pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"         ));                     // �н��Ⱓ
            pstmt.setString(pidx++, v_introducefilenamereal              );                     // ����Ұ� �̹���
            pstmt.setString(pidx++, v_introducefilenamenew               );                     // ����Ұ� �̹���
            pstmt.setString(pidx++, v_informationfilenamereal            );                     // ����(����)
            pstmt.setString(pidx++, v_informationfilenamenew             );                     // ����(����)
            pstmt.setString(pidx++, box.getStringDefault("p_ischarge"           , "N"   ));     // ������ ��/���� ����
            pstmt.setString(pidx++, box.getStringDefault("p_isopenedu"          , "N"   ));     // ���� ���� ���� 
            //pstmt.setInt   (pidx++, box.getInt   ("p_maleassignrate"    ));                     // �������-��(�ʼ�/���ÿ���)
            pstmt.setString(pidx++, box.getString("p_gradftest_flag"    ));                     // �������-����(�ʼ�/���ÿ���)
            pstmt.setString(pidx++, box.getString("p_gradreport_flag"   ));                     // ���� �Ҵ� ����
            pstmt.setString(pidx++, box.getString ("p_isgoyong"));               //��뺸�迩��(Y/N)
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_major"));      //��뺸�� ȯ�޾�-ȯ�޾�(KT)
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_minor"));      //��뺸�� ȯ�޾�-KT������

            pstmt.setString(pidx++, box.getString ("p_lev"));      				//��������
            pstmt.setString(pidx++, box.getString ("p_gubn"));      			//�̼�����
            pstmt.setString(pidx++, box.getString ("p_grade"));      			//�������
            pstmt.setString(pidx++, box.getString ("p_test"));      			//������
            pstmt.setString(pidx++, box.getString ("p_org"));      				//�������
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_stand"));     //��뺸�� ȯ�޾�-CP������
            
            pstmt.setString(pidx++, box.getString("p_subj"              ));
            
            isOk 	= pstmt.executeUpdate();
            
            pstmt.close();
            
            sbSQL.setLength(0);

            // ����� ���� ����� ����
            sbSQL.append(" update tz_subjseq  set subjnm = ? where subj = ? \n");
            
            pstmt   = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setString(1, box.getString("p_subjnm" ));
            pstmt.setString(2, box.getString("p_subj"   ));

            pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            // 2008.11.30 ����� ����
            // ������ �������� ���ȭ�鿡�� �ߴ� �����׷������ �������� ���, ����ȭ�鿡�� ���� �Է��ϴ°����� ����.
            isOk2 = RelatedGrcodeInsert(connMgr, box);

            if ( isOk > 0 && isOk2 != -1) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.setAutoCommit(true); 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }            

     
    /**
     * ��������
     * @param box      receive from the form object and session
     * @return isOk    1:delete success,0:delete fail
     */
    public int DeleteSubject(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement 	pstmt 	= null;
		PreparedStatement 	pstmt1 	= null;
        PreparedStatement 	pstmt2 	= null;
        PreparedStatement 	pstmt3 	= null;
        PreparedStatement 	pstmt4 	= null;
        String 				sql 	= "";
		String 				sql1 	= "";
		String 				sql4 	= "";
        int    				isOk 	= 0;

        String v_subj  				= box.getString("p_subj");

        try { 
            connMgr 				= new DBConnectionManager();

            // delete TZ_GRSUBJ table
            sql1 	= "delete from tz_grsubj where subjcourse = ?";
            pstmt1 	= connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_subj);
            pstmt1.executeUpdate(); //isOk1 	= 
            if ( pstmt1 != null ) { pstmt1.close(); }
            // delete TZ_BDS table
            sql 	= "delete from tz_bds where subj = ? and type ='SD' ";
            pstmt2 	= connMgr.prepareStatement(sql);
            pstmt2.setString(1, v_subj);
            pstmt2.executeUpdate(); //isOk2 	= 
            if ( pstmt2 != null ) { pstmt2.close(); }

            // delete tz_subj table
            sql4 	= "delete from tz_preview where subj = ? ";
            pstmt4 	= connMgr.prepareStatement(sql4);
            pstmt4.setString(1, v_subj);
            pstmt4.executeUpdate(); //isOk4 	= 
            if ( pstmt4 != null ) { pstmt4.close(); }
            // delete tz_subj table
            sql 	= "delete from tz_subj where subj = ? ";
            pstmt 	= connMgr.prepareStatement(sql);
            pstmt.setString(1, v_subj);
            isOk 	= pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt   != null ) { try { pstmt.close();  } catch ( Exception e1 ) { } }
            if ( pstmt2  != null ) { try { pstmt2.close(); } catch ( Exception e2 ) { } }
            if ( pstmt3  != null ) { try { pstmt3.close(); } catch ( Exception e  ) { } }
            if ( pstmt4  != null ) { try { pstmt4.close(); } catch ( Exception e  ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return isOk;
    }
    

    /**
     * �����׷� ��ȸ
     * @param box          receive from the form object and session
     * @return ArrayList   �����׷� ����Ʈ
     */
    public ArrayList TargetGrcodeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet 			ls 		= null;
        ArrayList 			list 	= null;
        String 				sql  	= "";
        SelectionData 		data 	= null;

        String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = s_gadmin.substring(0, 1);
        try { 

        	if (v_gadmin.equals("H")) {	// �����׷������
        		sql = "select a.grcode code, a.grcodenm name	\n"
	            	+ "from   tz_grcode a, tz_grcodeman b		\n"
	            	+ "where  a.grcode = b.grcode				\n"
	            	+ "and    b.gadmin = " + SQLString.Format(s_gadmin) +" \n"
	            	+ "and    b.userid = " + SQLString.Format(s_userid) +" \n"
	            	+ "order  by a.grcode 						\n";
        	} else {
        		sql = "select grcode code, grcodenm name	\n"
	            	+ "from   tz_grcode 					\n"
	            	+ "order  by grcode 					\n";
        	}
            connMgr = new DBConnectionManager();
            list 	= new ArrayList();
            ls 		= connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new SelectionData();

                data.setCode( ls.getString("code") );
                data.setName( ls.getString("name") );

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
     * �����׷� ����ȸ
     * @param box          receive from the form object and session
     * @return ArrayList   �����׷� ����Ʈ
     */
    public ArrayList SelectedGrcodeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet 			ls 		= null;
        ArrayList 			list 	= null;
        String 				sql  	= "";
        SelectionData 		data 	= null;
        String 				v_subj 	= box.getString("p_subj");

        try { 
            sql = "select a.grcode code											\n";
            sql +="     , a.grcodenm name 										\n";
            sql += "    ,(														\n"
            	+ "		  select case when count(*) > 0 then 'N' else 'Y' end	\n"
            	+ "		  from   vz_scsubjseq									\n"
            	+ "		  where  grcode = b.grcode								\n"
            	+ "		  and    subj   = b.subjcourse							\n"
            	+ "      ) as delyn												\n";											
            sql += "from  tz_grcode a 											\n";
            sql += "    , tz_grsubj b  											\n";
            sql += "where a.grcode = b.grcode 									\n";
            sql += "and   b.subjcourse = " + SQLString.Format(v_subj) + "       \n";
            sql += "order by a.grcode 											\n" ;
            
            
            connMgr = new DBConnectionManager();
            list 	= new ArrayList();
            ls 		= connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new SelectionData();

                data.setCode( ls.getString("code") );
                data.setName( ls.getString("name") );
                data.setDelyn(ls.getString("delyn"));

                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null 	 ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }

    
    /**
     * �����׷� ���� ����
     * @param box      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int RelatedGrcodeInsert(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement 	pstmt 	= null;
        PreparedStatement 	pstmt2 		= null;
        PreparedStatement 	pstmt3 		= null;
        String 				sql 		= "";
        String 				sql2 		= "";
        String 				sql3 		= "";
        int 				isOk 		= 0;
//        int 				isOk2 		= 0;
//        int 				isOk3 		= 0;

        String 				v_subj    	= box.getString("p_subj");
        String 				v_grcode  	= "";
        String 				v_luserid	= box.getSession("userid");

        try { 
            connMgr 					= new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // delete tz_grsubj table
            sql 	= "delete from tz_grsubj where subjcourse = ? ";
            pstmt 	= connMgr.prepareStatement(sql);
            pstmt.setString(1, v_subj);
            
            isOk 	= pstmt.executeUpdate();

            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }

            String 			v_selectedgrcodes   = box.getString("p_selectedgrcodes");
            StringTokenizer v_token 			= new StringTokenizer(v_selectedgrcodes, ";");

            // insert tz_grsubj table
            sql =  "insert into tz_grsubj(grcode, subjcourse, isnew, disseq, grpcode, grpname, luserid, ldate) ";
            sql +=  " values (?, ?, ?, ?, ?, ?, ?, ?)";

            // insert tz_preview table
            // tz_preview ���̺��� tz_grsubj ���̺�ó�� ��� ����Ÿ ���� �� Insert���� �ʰ�
            // Insert�Ϸ��� ����Ÿ�� ���� ��쿡�� tz_subj ���̺��� ������ ������ tz_preview�� Insert�Ѵ�.
            sql2 =  "insert into tz_preview(grcode, 								\n";
            sql2 +=  "                       subj, 									\n";
            sql2 +=  "                       edumans,								\n";
            sql2 +=  "                       intro,									\n";
            sql2 +=  "                       explain,								\n";
            sql2 +=  "                       luserid,								\n";
            sql2 +=  "                       ldate ) 								\n";
            sql2 +=  "select                 ?, 									\n";               // v_grcode
            sql2 +=  "                       subj, 									\n";
            sql2 +=  "                       edumans,								\n";
            sql2 +=  "                       intro,									\n";
            sql2 +=  "                       explain,								\n";
            sql2 +=  "                       ?,										\n";                // luserid
            sql2 +=  "                       ?										\n";                 // ldate
            sql2 +=  "from                   tz_subj a 								\n";
            sql2 +=  "where                  subj    = '" + v_subj + "' and 		\n";
            sql2 +=  "                       not exists( select  grcode, 			\n";
            sql2 +=  "                                           subj 				\n";
            sql2 +=  "                                   from    tz_preview b 		\n";
            sql2 +=  "                                   where   b.grcode = ? and 	\n"; // v_grcode
            sql2 +=  "                                           b.subj   = '" + v_subj + "')	\n";

            // �����ְ������ ����Ǿ� �ִ� �����ְ��� �����ϴ� ���
            // tz_grsubj ���̺� �����Ƿ� tz_preview ���̺� �ִ� �ڷᵵ �����Ѵ�.
            sql3 = "delete 															\n";
            sql3 += "from    tz_preview 											\n";
            sql3 += "where   subj = '" + v_subj + "' and 							\n";
            sql3 += "        not exists( select  grcode,							\n";
            sql3 += "                            subj 								\n";
            sql3 += "                    from    tz_grsubj 							\n";
            sql3 += "                    where   grcode  = tz_preview.grcode and 	\n";
            sql3 += "                            subj    = tz_preview.subj)			\n";

            pstmt 	= connMgr.prepareStatement(sql);
            pstmt2 	= connMgr.prepareStatement(sql2);
            pstmt3 	= connMgr.prepareStatement(sql3);


            while ( v_token.hasMoreTokens() ) { 
                v_grcode = v_token.nextToken();

                pstmt.setString(1, v_grcode		);
                pstmt.setString(2, v_subj		);
                pstmt.setString(3, "Y"			);
                pstmt.setInt   (4, 0			);
                pstmt.setString(5, ""			);
                pstmt.setString(6, ""			);
                pstmt.setString(7, v_luserid	);
                pstmt.setString(8, FormatDate.getDate("yyyyMMddHHmmss") );
                isOk += pstmt.executeUpdate();
                pstmt2.setString(1, v_grcode	);
                pstmt2.setString(2, v_luserid	);
                pstmt2.setString(3, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt2.setString(4, v_grcode	);
                pstmt2.executeUpdate(); //isOk2 = 
                // isOk2 = 1;
            }
            if ( pstmt != null ) { pstmt.close(); }
            if ( pstmt2 != null ) { pstmt2.close(); }
            
            pstmt3.executeUpdate();
            if ( pstmt3 != null ) { pstmt3.close(); }
            if (isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null   ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null   ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( pstmt3 != null   ) { try { pstmt3.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return isOk;
    }

    /**
     * �����׷� ���� ����
     * @param box      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int RelatedGrcodeInsert(DBConnectionManager connMgr, RequestBox box) throws Exception { 

        PreparedStatement 	pstmt 	= null;
        PreparedStatement 	pstmt2 		= null;
        PreparedStatement 	pstmt3 		= null;
        String 				sql 		= "";
        String 				sql2 		= "";
        String 				sql3 		= "";
        int 				isOk 		= 0;

        String 				v_subj    	= box.getString("p_subj");
        String 				v_grcode  	= "";
        String 				v_luserid	= box.getSession("userid");

        try { 

            // delete tz_grsubj table
            sql 	= "delete from tz_grsubj where subjcourse = ? ";
            pstmt 	= connMgr.prepareStatement(sql);
            pstmt.setString(1, v_subj);
            
            isOk 	= pstmt.executeUpdate();

            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }

            String 			v_selectedgrcodes   = box.getString("p_selectedgrcodes");
            StringTokenizer v_token 			= new StringTokenizer(v_selectedgrcodes, ";");

            // insert tz_grsubj table
            sql =  "insert into tz_grsubj(grcode, subjcourse, isnew, disseq, grpcode, grpname, luserid, ldate) ";
            sql +=  " values (?, ?, ?, ?, ?, ?, ?, ?)";

            // insert tz_preview table
            // tz_preview ���̺��� tz_grsubj ���̺�ó�� ��� ����Ÿ ���� �� Insert���� �ʰ�
            // Insert�Ϸ��� ����Ÿ�� ���� ��쿡�� tz_subj ���̺��� ������ ������ tz_preview�� Insert�Ѵ�.
            sql2 =  "insert into tz_preview(grcode, 								\n";
            sql2 +=  "                       subj, 									\n";
            sql2 +=  "                       edumans,								\n";
            sql2 +=  "                       intro,									\n";
            sql2 +=  "                       explain,								\n";
            sql2 +=  "                       luserid,								\n";
            sql2 +=  "                       ldate ) 								\n";
            sql2 +=  "select                 ?, 									\n";               // v_grcode
            sql2 +=  "                       subj, 									\n";
            sql2 +=  "                       edumans,								\n";
            sql2 +=  "                       intro,									\n";
            sql2 +=  "                       explain,								\n";
            sql2 +=  "                       ?,										\n";                // luserid
            sql2 +=  "                       ?										\n";                 // ldate
            sql2 +=  "from                   tz_subj a 								\n";
            sql2 +=  "where                  subj    = '" + v_subj + "' and 		\n";
            sql2 +=  "                       not exists( select  grcode, 			\n";
            sql2 +=  "                                           subj 				\n";
            sql2 +=  "                                   from    tz_preview b 		\n";
            sql2 +=  "                                   where   b.grcode = ? and 	\n"; // v_grcode
            sql2 +=  "                                           b.subj   = '" + v_subj + "')	\n";

            // �����ְ������ ����Ǿ� �ִ� �����ְ��� �����ϴ� ���
            // tz_grsubj ���̺� �����Ƿ� tz_preview ���̺� �ִ� �ڷᵵ �����Ѵ�.
            sql3 = "delete 															\n";
            sql3 += "from    tz_preview 											\n";
            sql3 += "where   subj = '" + v_subj + "' and 							\n";
            sql3 += "        not exists( select  grcode,							\n";
            sql3 += "                            subj 								\n";
            sql3 += "                    from    tz_grsubj 							\n";
            sql3 += "                    where   grcode  = tz_preview.grcode and 	\n";
            sql3 += "                            subj    = tz_preview.subj)			\n";

            pstmt 	= connMgr.prepareStatement(sql);
            pstmt2 	= connMgr.prepareStatement(sql2);
            pstmt3 	= connMgr.prepareStatement(sql3);


            while ( v_token.hasMoreTokens() ) { 
                v_grcode = v_token.nextToken();

                pstmt.setString(1, v_grcode		);
                pstmt.setString(2, v_subj		);
                pstmt.setString(3, "Y"			);
                pstmt.setInt   (4, 0			);
                pstmt.setString(5, ""			);
                pstmt.setString(6, ""			);
                pstmt.setString(7, v_luserid	);
                pstmt.setString(8, FormatDate.getDate("yyyyMMddHHmmss") );
                isOk += pstmt.executeUpdate();

                pstmt2.setString(1, v_grcode	);
                pstmt2.setString(2, v_luserid	);
                pstmt2.setString(3, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt2.setString(4, v_grcode	);
                pstmt2.executeUpdate();
            }
            
            pstmt3.executeUpdate();

        } catch ( Exception ex ) { 
            isOk = -1;
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
        	if ( pstmt != null   ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null   ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( pstmt3 != null   ) { try { pstmt3.close(); } catch ( Exception e ) { } }
        }
        
        return isOk;
    }
     
    /**
     * ���������� ��ϵ� �����׷� ��ȸ
     * @param box          receive from the form object and session
     * @return ArrayList   �����׷� ����Ʈ
     */
    public ArrayList PreviewGrcodeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet 			ls 		= null;
        ArrayList 			list 	= null;
        String 				sql  	= "";
        SelectionData 		data 	= null;
        String 				v_subj 	= box.getString("p_subj");
        
        try { 
            sql = "select a.grcode code, a.grcodenm name ";
            sql += "  from tz_grcode  a, ";
            sql += "       tz_preview b  ";
            sql += " where a.grcode = b.grcode ";
            sql += "   and b.subj = " + SQLString.Format(v_subj);
            sql += " order by a.grcode " ;

            connMgr = new DBConnectionManager();
            list 	= new ArrayList();
            ls 		= connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                data = new SelectionData();

                data.setCode( ls.getString("code") );
                data.setName( ls.getString("name") );

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
     * ���������� ���
     * @param box      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int InsertPreview(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;

        String              v_luserid   = box.getSession("userid");

        try { 
            connMgr     = new DBConnectionManager();

            // insert tz_subj table
            sbSQL.append(" insert into TZ_PREVIEW (                                         \n")
                 .append("         grcode      , subj          , subjtext  , edumans        \n")
                 .append("     ,   intro       , explain       , expect    , master         \n")
                 .append("     ,   masemail    , recommender   , recommend , luserid        \n")
                 .append("     ,   ldate                                                    \n")
                 .append(" ) values (                                                       \n")
                 .append("         ?           , ?             , ?         , ?              \n")
                 .append("     ,   ?           , ?             , ?         , ?              \n")
                 .append("     ,   ?           , ?             , ?         , ?              \n")
                 .append("     ,   ?                                                        \n")
                 .append(" )                                                                \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setString( 1, box.getString("p_grcode"        ));
            pstmt.setString( 2, box.getString("p_subj"          ));
            pstmt.setString( 3, box.getString("p_subjtext"      ));
            pstmt.setString( 4, box.getString("p_edumans"       ));
            pstmt.setString( 5, box.getString("p_intro"         ));
            pstmt.setString( 6, box.getString("p_explain"       ));
            pstmt.setString( 7, box.getString("p_expect"        ));
            pstmt.setString( 8, box.getString("p_master"        ));
            pstmt.setString( 9, box.getString("p_masemail"      ));
            pstmt.setString(10, box.getString("p_recommender"   ));
            pstmt.setString(11, box.getString("p_recommend"     ));
            pstmt.setString(12, v_luserid                        );
            pstmt.setString(13, FormatDate.getDate("yyyyMMddHHmmss") );

            isOk = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception e ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e10 ) { } 
            }
        }

        return isOk;
    }

     
    /**
     * ���������� ����ȸ
     * @param box          receive from the form object and session
     * @return PreviewData   ������Ÿ
     */
    public PreviewData SelectPreviewData(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 	= null;
        ListSet 			ls 			= null;
        PreviewData 		data 		= null;
        String 				sql  		= "";
        
        String 				v_grcode 	= box.getString("p_grcode");
        String 				v_subj 		= box.getString("p_subj");

        try { 
            sql =  " select grcode,   subj,        subjtext,  edumans, ";
            sql +=  "        intro,    explain,     expect,    master, ";
            sql +=  "        masemail, recommender, recommend, luserid, ";
            sql +=  "        ldate ";
            sql +=  "   from TZ_PREVIEW ";
            sql +=  "  where grcode = " + SQLString.Format(v_grcode);
            sql +=  "    and subj = " + SQLString.Format(v_subj);

            connMgr = new DBConnectionManager();
            ls 		= connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new PreviewData();
                
                data.setGrcode   	( ls.getString("grcode") 		);
                data.setSubj     	( ls.getString("subj") 			);
                data.setSubjtext 	( ls.getString("subjtext") 		);
                data.setEdumans  	( ls.getString("edumans") 		);
                data.setIntro    	( ls.getString("intro") 		);
                data.setExplain  	( ls.getString("explain") 		);
                data.setExpect   	( ls.getString("expect") 		);
                data.setMaster   	( ls.getString("master") 		);
                data.setMasemail 	( ls.getString("masemail") 		);
                data.setRecommender	( ls.getString("recommender") 	);
                data.setRecommend	( ls.getString("recommend") 	);
                data.setLuserid   	( ls.getString("luserid") 		);
                data.setLdate    	( ls.getString("ldate") 		);
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

    
    /**
     * ���������� ����
     * @param box      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int UpdatePreview(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 	= null;

        PreparedStatement 	pstmt 		= null;
        String 				sql 		= "";
        int 				isOk 		= 0;

        String 				v_luserid 	= box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            // insert tz_subj table
            sql =  "update TZ_PREVIEW ";
            sql +=  "   set subjtext   = ?, ";
            sql +=  "       edumans    = ?, ";
            sql +=  "       intro      = ?, ";
            sql +=  "       explain    = ?, ";
            sql +=  "       expect     = ?, ";
            sql +=  "       master     = ?, ";
            sql +=  "       masemail   = ?, ";
            sql +=  "       recommender= ?, ";
            sql +=  "       recommend  = ?, ";
            sql +=  "       luserid     = ?, ";
            sql +=  "       ldate      = ?  ";
            sql +=  " where grcode     = ?  ";
            sql +=  "   and subj       = ?  ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, box.getString("p_subjtext") );
            pstmt.setString( 2, box.getString("p_edumans") );
            pstmt.setString( 3, box.getString("p_intro") );
            pstmt.setString( 4, box.getString("p_explain") );
            pstmt.setString( 5, box.getString("p_expect") );
            pstmt.setString( 6, box.getString("p_master") );
            pstmt.setString( 7, box.getString("p_masemail") );
            pstmt.setString( 8, box.getString("p_recommender") );
            pstmt.setString( 9, box.getString("p_recommend") );
            pstmt.setString(10, v_luserid);
            pstmt.setString(11, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(12, box.getString("p_grcode") );
            pstmt.setString(13, box.getString("p_subj") );

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

     
    /**
     * ���������� ����
     * @param box      receive from the form object and session\
     * @return isOk    1:delete success,0:delete fail
     */
    public int DeletePreview(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 	= null;
        PreparedStatement 	pstmt 		= null;
        String 				sql 		= "";
        int    				isOk 		= 0;

        String 				v_grcode 	= box.getString("p_grcode");
        String 				v_subj  	= box.getString("p_subj");

        try { 
            connMgr = new DBConnectionManager();
            
            // delete tz_subj table
            sql 	= "delete from TZ_PREVIEW where grcode = ? and subj = ?";
            
            pstmt 	= connMgr.prepareStatement(sql);
            pstmt.setString(1, v_grcode	);
            pstmt.setString(2, v_subj	);
            
            isOk 	= pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return isOk;
    }

    
    /**
     * ���� ���� ����Ʈ ��ȸ
     * @param box          receive from the form object and session
     * @return ArrayList   ���� ���񸮽�Ʈ
     */
    public ArrayList RelatedSubjList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 	= null;
        ListSet 			ls 			= null;
        ArrayList 			list 		= null;
        String 				sql  		= "";
        SelectionData 		data 		= null;

        String 				v_grcode 	= box.getStringDefault("p_grcode","N000001");
        String 				v_subj   	= box.getString("p_subj");
        String 				v_subjgubun = box.getString("p_subjgubun");

        try { 
            sql = "select a.rsubj code, b.subjnm name";
            sql += "  from tz_subjrelate a, ";
            sql += "       tz_subj   b   ";
            sql += " where a.rsubj = b.subj  ";
            sql += "   and a.grcode = " + SQLString.Format(v_grcode);
            sql += "   and a.subj   = " + SQLString.Format(v_subj);
            sql += "   and a.gubun  = " + SQLString.Format(v_subjgubun);
            sql += " order by a.subj, b.subjnm";

            connMgr 					= new DBConnectionManager();
            list 						= new ArrayList();
            ls 							= connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new SelectionData();

                data.setCode( ls.getString("code") );
                data.setName( ls.getString("name") );

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
     * ���� ��������Ʈ ��ȸ
     * @param box          receive from the form object and session
     * @return ArrayList   ���� ���񸮽�Ʈ
     */
    public ArrayList SelectedRelatedSubjList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 		= null;
        ListSet 			ls 				= null;
        StringBuffer        sbSQL           = new StringBuffer("");
        ArrayList 			list 			= new ArrayList();
        SubjectInfoData 	data 			= null;
        
//        int                 iSysAdd         = 0;

        String 				v_subjectcodes  = box.getString("p_selectedsubjcodes" );
        String 				v_subjecttexts  = box.getString("p_selectedsubjtexts" );
        String 				v_grcode 		= box.getString("p_grcode"            );
        String 				v_subj   		= box.getString("p_subj"              );
        String 				v_subjgubun		= box.getString("p_subjgubun"         );
        
        try {
            StringTokenizer v_tokencode     = null;
            StringTokenizer v_tokentext     = null;
            String          v_code          = "";
            String          v_text          = "";
            
            if ( !v_subjectcodes.equals("") ) { 
                v_tokencode                 = new StringTokenizer(v_subjectcodes, ";");
                v_tokentext                 = new StringTokenizer(v_subjecttexts, ";");
                v_code                      = "";
                v_text                      = "";

                while ( v_tokencode.hasMoreTokens() && v_tokentext.hasMoreTokens() ) { 
                    v_code 	= v_tokencode.nextToken();
                    v_text 	= v_tokentext.nextToken();

                    data 	= new SubjectInfoData();

                    data.setSubj        (v_code);
                    data.setDisplayname (v_text);

                    list.add(data);
                }
            } else if ( !v_subj.equals("") ) {
                connMgr     = new DBConnectionManager();
                list        = new ArrayList();
                
                sbSQL.append(" select  c.rsubj                                                      \n")
                     .append("     ,   a.subjnm                                                     \n")
                     .append("     ,   a.isonoff                                                    \n")
                     .append("     ,   b.upperclass                                                 \n")
                     .append("     ,   b.classname                                                  \n")
                     .append("     ,   d.codenm                                                     \n")
                     .append(" from    tz_subj         a                                            \n")
                     .append("     ,   tz_subjatt      b                                            \n")
                     .append("     ,   tz_subjrelate   c                                            \n")
                     .append("     ,   tz_code         d                                            \n")
                     .append(" where   a.subjclass = b.subjclass                                    \n")
                     .append(" AND     a.subj      = c.rsubj                                        \n")
                     .append(" AND     a.isonoff   = d.code                                         \n")
                     .append(" AND     c.grcode    = " + StringManager.makeSQL(v_grcode     ) + "   \n")
                     .append(" AND     c.subj      = " + StringManager.makeSQL(v_subj       ) + "   \n")
                     .append(" AND     c.gubun     = " + StringManager.makeSQL(v_subjgubun  ) + "   \n")
                     .append(" AND     d.gubun     = " + StringManager.makeSQL(ONOFF_GUBUN  ) + "   \n")
                     .append(" ORDER BY c.subj                                                      \n");
                
                
                ls          = connMgr.executeQuery(sbSQL.toString());

                while ( ls.next() ) { 
                    data    = new SubjectInfoData();

                    data.setSubj		( ls.getString("rsubj"     ));
                    data.setSubjnm		( ls.getString("subjnm"    ));
                    data.setIsonoff		( ls.getString("isonoff"   ));
                    data.setUpperclass	( ls.getString("upperclass"));
                    data.setClassname	( ls.getString("classname" ));
                    data.setIsonoffnm	( ls.getString("codenm"    ));

                    list.add(data);
                }
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e10 ) { } 
            }
        }
        
        return list;
    }

    
    /**
     * ������� ���
     * @param box      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int RelatedSubjInsert(RequestBox box) throws Exception { 
       DBConnectionManager 	connMgr 	= null;

       PreparedStatement 	pstmt 		= null;
       String 				sql 		= "";
       int 					isOk 		= 0;

       String 				v_grcode    = box.getString("p_grcode");
       String 				v_subj      = box.getString("p_subj");
       String 				v_subjgubun	= box.getString("p_subjgubun");
       String 				v_luserid	= box.getSession("userid");
       String 				v_rsubj     = "";

       try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);

            // delete TZ_SUBJRELATE table
            sql 	= "delete from TZ_SUBJRELATE where grcode = ? and subj = ? and gubun = ?";
            pstmt 	= connMgr.prepareStatement(sql);
            pstmt.setString(1, v_grcode);
            pstmt.setString(2, v_subj);
            pstmt.setString(3, v_subjgubun);

            isOk 	= pstmt.executeUpdate();

            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }

            String 			v_subjectcodes  = box.getString("p_selectedsubjcodes");
            StringTokenizer v_token 		= new StringTokenizer(v_subjectcodes, ";");

            // insert TZ_SUBJRELATE table
            sql   =  "insert into TZ_SUBJRELATE(grcode, subj, gubun, rsubj, luserid, ldate) ";
            sql  +=  " values (?, ?, ?, ?, ?, ?)";
            pstmt = connMgr.prepareStatement(sql);
            
            while ( v_token.hasMoreTokens() ) { 
                v_rsubj = v_token.nextToken();

                pstmt.setString(1, v_grcode		);
                pstmt.setString(2, v_subj		);
                pstmt.setString(3, v_subjgubun	);
                pstmt.setString(4, v_rsubj		);
                pstmt.setString(5, v_luserid	);
                pstmt.setString(6, FormatDate.getDate("yyyyMMddHHmmss") );
                
                isOk = pstmt.executeUpdate();
            }
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception ex ) { 
           isOk = 0;
           connMgr.rollback();
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       } finally { 
           if ( isOk > 0 ) { connMgr.commit(); }
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }
       
       return isOk;
    }


    /**
     * ���������� TZ_BDS�� ���
     * @param connmgr, v_subj      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int InsertBds(DBConnectionManager connMgr,String v_subj,String v_type, String v_title, String v_year, String v_subjseq) throws Exception { 
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;
        
        ListSet             ls          = null;
        String              v_luserid   = "SYSTEM";
        int                 v_tabseq    = 0;
       
        if("ALL".equals(v_year)) {
            v_year = "0000";
        }
        if("ALL".equals(v_subjseq)) {
            v_subjseq = "0000";
        }

        try { 
            sbSQL.append(" select nvl(max(tabseq), 0) + 1 from tz_bds ");
            ls              = connMgr.executeQuery(sbSQL.toString());

            if ( ls.next() ) { 
                v_tabseq    = ls.getInt(1);
            } else { 
                v_tabseq    = 1;
            }
            
            // StringBuffer �ʱ�ȭ    
            sbSQL.setLength(0);

            // insert tz_bds table
            sbSQL.append(" insert into tz_bds                               \n")
                 .append(" (                                                \n")
                 .append("         tabseq  , type  , grcode    , comp       \n")
                 .append("     ,   subj    , year  , subjseq   , sdesc      \n")
                 .append("     ,   ldesc   , status, luserid   , ldate      \n")
                 .append(" ) values (                                       \n")
                 .append("         ?       , ?     , ?         , ?          \n")
                 .append("     ,   ?       , ?     , ?         , ?          \n")
                 .append("     ,   ?       , ?     , ?         , ?          \n")
                 .append(" )                                                \n");
            
            pstmt           = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setInt   ( 1, v_tabseq    );
            pstmt.setString( 2, v_type      ); // �Խ��� Ÿ��
            pstmt.setString( 3, "0000000"   );
            pstmt.setString( 4, "0000000000");
            pstmt.setString( 5, v_subj      );
            pstmt.setString( 6, v_year      );
            pstmt.setString( 7, v_subjseq   );
            pstmt.setString( 8, v_subj + v_title);
            pstmt.setString( 9, ""          );
            pstmt.setString(10, "A"         );
            pstmt.setString(11, v_luserid   );
            pstmt.setString(12, FormatDate.getDate("yyyyMMddHHmmss") );

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception e ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e1 ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return isOk;
    }
     
     
     /**
      * �ű� ���� ��ȸ
      * @param box          receive from the form object and session
      * @return String      �ű� �̹��� ����   
      */
     public static String getNewSubj(String v_subj) throws Exception { 
         DBConnectionManager    connMgr     = null;
         ListSet                ls          = null;
         DataBox                dbox        = null;
         String                 sql         = "";
         String                 v_rtnvalue  = "";
         
         
         try { 
             sql  = " select count(*) cnt                               	\n"
             	  + " from tz_subj                                      	\n"
             	  + " where subj        = " + SQLString.Format(v_subj) + " 	\n"
             	  + " and   specials    LIKE 'Y%'                       	\n";
             
             connMgr = new DBConnectionManager();
             ls         = connMgr.executeQuery(sql);

             while ( ls.next() ) { 
                 dbox   = ls.getDataBox();
             }
             
             if ( !dbox.getString("d_cnt").equals("0") )
                 v_rtnvalue = "<img src=\"/images/user/c/icon_new03.gif\" alt=\"�ű�\" class=\"btn_space_l8\" />";
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, null, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null     ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         
         return v_rtnvalue;
     }
     
     /**
     ��õ ����
     @param box          receive from the form object and session
     @return ArrayList   ��õ �̹��� ����   
     */
     public static String getRecomSubj(String v_subj) throws Exception { 
         DBConnectionManager    connMgr     = null;
         ListSet                ls          = null;
         DataBox                dbox        = null;
         String                 sql         = "";
         String                 v_rtnvalue  = "";
         
         try { 
             sql  = " select count(*) cnt                               	\n"
             	  + " from tz_subj                                      	\n"
             	  + " where subj        = " + SQLString.Format(v_subj)+ " 	\n"
             	  + " and   specials    LIKE '%Y'                       	\n";
             
             connMgr = new DBConnectionManager();
             ls         = connMgr.executeQuery(sql);

             while ( ls.next() ) { 
                 dbox   = ls.getDataBox();
                 if ( !dbox.getString("d_cnt").equals("0") )
                     v_rtnvalue = "<img src=\"/images/user/c/icon_recommend03.gif\" alt=\"��õ\" class=\"btn_space_l8\" />";
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, null, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null     ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         
         return v_rtnvalue;
     }
     
     
     
	/**
	 * �����Ź���� ���� LIst
	 * @param box          receive from the form object and session
	 * @return ArrayList   ��������Ʈ
     */
    public ArrayList TrustSelectSubjectList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql  = "";
        DataBox dbox = null;
		
        String  ss_grcode       = box.getStringDefault("s_grcode","ALL");       //�����ְ�
        String  ss_gyear        = box.getStringDefault("s_gyear","ALL");       //�����ְ�        
        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");   //�����з�
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");  //�����з�
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");   //�����з�
        String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");   //����&�ڽ�
        String  ss_subjseq      = box.getStringDefault("s_subjseq","ALL");   //����&�ڽ�        
        String  ss_midstat		= box.getStringDefault("p_midstat","ALL");		//HRD ����� ���� ����
        String  ss_secstat		= box.getStringDefault("p_secstat","ALL"); 		//HRD ���� ���� ����
        String  v_searchtext    = box.getString("p_searchtext");

        String  v_orderColumn   = box.getString("p_order");                     //������ �÷���
        String  v_orderType     = box.getString("p_orderType");                 //������ ����

        try {
			sql = " select seq, a.subj tmpsubj, b.subj, a.subjnm tmpsubjnm, b.subjnm, a.userid, ";
			sql +="   a.edustart, a.eduend, a.educomp, a.appdate, b.owner, firstat, midstat, secstat, ";
			sql +="	  m.name, m.deptnam, m.jikwinm  ";           
			sql +=" From " ;
			sql +=" 	tz_trustpropose a,";
			sql +=" 	vz_scsubjseq b,";
			sql +=" 	tz_member m ";
			sql +=" Where ";
			sql +=" 	a.subj = b.subj(+) and a.year = b.year(+) and a.subjseq = b.subjseq(+) and a.userid = m.userid ";
			sql +=" 	and a.firstat = 'Y' ";

            //�����׷�
            if (!ss_grcode.equals("ALL")) {
                sql+= "         and b.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            
            //�����⵵
            if (!ss_gyear.equals("ALL")) {
                sql+= "         and a.year = " + StringManager.makeSQL(ss_gyear);
            }            

            if (!ss_subjcourse.equals("ALL")) {
                sql+= "   and b.subj = " + StringManager.makeSQL(ss_subjcourse);
            } else {
                if (!ss_upperclass.equals("ALL")) {
                    if (!ss_upperclass.equals("ALL")) {
                        sql += " and b.upperclass = "+StringManager.makeSQL(ss_upperclass);
                    }
                    if (!ss_middleclass.equals("ALL")) {
                        sql += " and b.middleclass = "+StringManager.makeSQL(ss_middleclass);
                    }
                    if (!ss_lowerclass.equals("ALL")) {
                        sql += " and b.lowerclass = "+StringManager.makeSQL(ss_lowerclass);
                    }
                }
            }
            
            if (!ss_subjseq.equals("ALL")) {
                sql += " and b.subjseq = "+StringManager.makeSQL(ss_subjseq);
            }            
            
			if (!ss_midstat.equals("ALL")) {
                sql += " and a.midstat = "+StringManager.makeSQL(ss_midstat);
            }
			if (ss_secstat.equals("Y")||ss_secstat.equals("N")) {
                sql += " and a.secstat = "+StringManager.makeSQL(ss_secstat);
            }
            if (ss_secstat.equals("B")) {
                sql += " and a.secstat in('','-')";
            }            

            if(!v_searchtext.equals("")){
            	v_searchtext = v_searchtext.replaceAll("'", "");
            	v_searchtext = v_searchtext.replaceAll("/", "//");
            	v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
                sql += " and upper(a.subjnm) like " + StringManager.makeSQL("%"+v_searchtext+"%") + " escape '/'";
            }

            if(v_orderColumn.equals("")) {
                sql+= " order by a.appdate desc, a.subjnm ";
            } else {
                sql+= " order by " + v_orderColumn + v_orderType;
            }

            connMgr = new DBConnectionManager();
            list = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
	

    /**
     * �����Ź ���� ��ȸ
     * @param box          receive from the form object and session
     * @return PreviewData   ��������Ÿ
     */
    public DataBox TrustSelectSubjectView(RequestBox box,String gubun) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        DataBox dbox = null;
        String sql  = "";
        String v_seq = "";
//		String v_userid = "";
		
		String v_chk = box.getString("p_params");
		
		if(gubun.equals("Y"))
		{
			StringTokenizer arr_tmp = new StringTokenizer(v_chk, ",");
			
//			v_userid    = arr_tmp.nextToken();
			v_seq		= arr_tmp.nextToken();
		}
		else
		{
			v_seq = box.getString("p_seq");
		}

        try {
			
			sql = " select ";
			sql +="		seq, a.subj tmpsubj, b.subj, a.subjnm tmpsubjnm, b.subjnm, a.year, a.userid, a.edugubun, a.eduurl, a.educomp, a.eduplace, ";
			sql +=" 	a.edustart, a.eduend, a.eduday, a.edutime, a.musernm, a.mphone, a.mfax, a.memail, a.goyongyn, a.goyongbill, ";
			sql +="		a.billgubun, a.edubill, a.accountnum, a.educontent, a.myduty, a.dutyterm, a.result, a.realfile, a.newfile, ";
            sql +="     a.firstat, a.midstat, a.secstat, a.appdate, a.rejectstat, a.rejectreason, a.rejectdate, ";
            sql +="     m.name, m.deptnam, m.jikwinm ";     
			sql +=" From tz_trustpropose a, tz_subj b, tz_subjseq c, tz_member m ";
			sql +=" Where ";
			sql +="		a.subj = b.subj(+) and b.subj = c.subj(+) and a.userid = m.userid ";
			sql +=" 	and a.seq = " + SQLString.Format(v_seq);
            sql +=" Order by seq desc";
			
            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql);


            while (ls.next()) {
                dbox = ls.getDataBox(); 
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return dbox;
    }
	
    
	/**
	 * ���ܽ���ó��
	 * @param box          receive from the form object and session
	 * @return int     �����(0,1)
     */
    public int TrustMidApproval(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt     = null;
        ListSet ls      = null;
        String sql      = "";
        int isOk        = 0;        
        
        String v_param          = "";        //userid,seq
        String v_chkfinal       = "";        //userid,seq
		String v_userid         = "";
		String v_seq 	        = "";
//		String v_midstat	    = "";
		String v_secstat	    = "";
		String v_rejectstat     = "";		//�ݷ�����
		String v_rejectreson    = "";
        String v_rejectdate     = "";
		
		//Vector vec_chkfinal	= box.getVector("p_chkfinal");		
		Vector vec_param 	    = box.getVector("p_params");		
		
		try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
			sql = "Update TZ_TRUSTPROPOSE ";
			sql +=" Set midstat = ?, secstat = ?, rejectstat=?, rejectdate =?, rejectreason =?";
			sql +=" Where seq = ? and userid = ? ";
			
			pstmt = connMgr.prepareStatement(sql);
			for(int i = 0; i < vec_param.size(); i++) {
                v_param     = vec_param.elementAt(i).toString();    //�����ڵ�����Userid...

				StringTokenizer arr_value = new StringTokenizer(v_param, ",");
 
				v_userid	= arr_value.nextToken();
				v_seq		= arr_value.nextToken();
//				v_midstat	= arr_value.nextToken();
				v_secstat	= arr_value.nextToken();
				v_chkfinal	= arr_value.nextToken();
				
				//�ݷ��� ��� (v_chkfinal[�ݷ�:N | ����:Y | ��ó��:B])
				
				if(v_chkfinal.equals("N")) 
				{ 
                    v_secstat      = "";
					v_rejectstat   = "Y" ;
					v_rejectreson  = "��� �ݷ�";
                    v_rejectdate   = FormatDate.getDate("yyyyMMddHHmmss");
				}//�ݷ�����
				else if(v_chkfinal.equals("Y")) 
				{ 
                    v_secstat    = "B";
					v_rejectstat = "N";
                }
                else if(v_chkfinal.equals("B")) 
                { 
                    v_secstat    = "";
                    v_rejectstat = "N";
                }

				pstmt.setString(1, v_chkfinal);
                pstmt.setString(2, v_secstat);                
				pstmt.setString(3, v_rejectstat);
				pstmt.setString(4, v_rejectdate);
				pstmt.setString(5, v_rejectreson);
				pstmt.setString(6, v_seq);
				pstmt.setString(7, v_userid);
				
				isOk = pstmt.executeUpdate();
			}
			if ( pstmt != null ) { pstmt.close(); }
			
		}catch(Exception ex) {
            isOk = 0;
            connMgr.rollback();
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if (isOk > 0) {connMgr.commit();}
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
		
		return isOk;
    }
	
	/**
	 * ���ܹݷ�ó��
	 * @param box          receive from the form object and session
	 * @return int     �����(0,1)
     */
    public int TrustReject(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt     = null;
        ListSet ls = null;
        String sql  = "";
        int isOk =0;
        
        String v_param      = "";           //userid,seq
//        String v_chkfinal   = "";        //userid,seq
		String v_userid     = "";
		String v_seq 	    = "";
//		String v_midstat	= "";
//		String v_secstat	= "";
		
		//Vector vec_chkfinal	= box.getVector("p_chkfinal");		
		Vector vec_param 	= box.getVector("p_params");		
		
		try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
			
			sql = "Update TZ_TRUSTPROPOSE ";
			sql +=" Set midstat = ?, rejectstat = ?, rejectdate = ?, rejectreason = '��� �ݷ�' ";
			sql +=" Where seq = ? and userid = ?";
			
			pstmt = connMgr.prepareStatement(sql);
			for(int i = 0; i < vec_param.size(); i++) {
                v_param     = vec_param.elementAt(i).toString();    //�����ڵ�����Userid...

				StringTokenizer arr_value = new StringTokenizer(v_param, ",");
 
				v_userid	= arr_value.nextToken();
				v_seq		= arr_value.nextToken();
//				v_midstat	= arr_value.nextToken();
//				v_secstat	= arr_value.nextToken();
//				v_chkfinal	= arr_value.nextToken();
				
				pstmt.setString(1, "N");
				pstmt.setString(2, "Y");
				pstmt.setString(3, FormatDate.getDate("yyyyMMddHHmmss"));
				pstmt.setString(4, v_seq);
				pstmt.setString(5, v_userid);
				
				isOk = pstmt.executeUpdate();
				if ( pstmt != null ) { pstmt.close(); }
			}
			
		}catch(Exception ex) {
            isOk = 0;
            connMgr.rollback();
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if (isOk > 0) {connMgr.commit();}
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
		
		return isOk;
    }
	
	/**
	 * �����Ź ���� ���� ��� .
	 * @param connmgr, v_subj      receive from the form object and session
	 * @return isOk    1:insert success,0:insert fail
     */
    public int InsertTrustModule(RequestBox box) throws Exception {
        PreparedStatement pstmt = null;
		DBConnectionManager connMgr = null;
		ListSet ls = null;
        String sql = "";
        int isOk	= 0;
        int isOk2	= 0;
        int isOk3	= 0;
        int isOk4	= 0;
		
//        int    v_tabseq;
		Vector vec_param 	= box.getVector("p_params");
		
	    String v_param		= ""; 
//		String v_userid		= "";
		String v_seq		= "";
		String v_grseq		= "";
		String v_subjseq	= "";
		String v_edustart	= "";
        String v_edupend    = "";
		String v_propstart	= "";
        String v_propend    = "";
		
		String v_subj		= box.getString("p_subj");
//		String v_subjnm		= box.getString("p_subjnm");
		String v_year		= box.getString("p_gyear");

        try {
			connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

			// �⺻ �����ڵ� ����.
			box.put("p_grcode", "N000001");

//            SubjectBean bean = new SubjectBean();

			if(v_subj.equals(""))
			{
				// ������ ���� �� ���� ����.
				v_subj = InsertTrustSubject(box, connMgr);
				box.put("p_subj", v_subj);

			}
		
			// ���� ���� ����		
			v_grseq = InsertGrseq(box, connMgr);

			if(v_grseq.equals("")) isOk = 0;
			else	isOk = 1;

			v_subjseq = makeSubjseq("N000001", v_year, v_grseq, "000000", "0000","0000",  v_subj, box.getSession("userid"), 0,"","","","",-7,"","",connMgr);

			if(v_subjseq.equals("")) isOk2 = 0;
			else	isOk2	= 1;
			
			for(int j = 0; j < vec_param.size(); j++) {
                v_param     = vec_param.elementAt(j).toString();    //�����ڵ�����Userid...
				
				StringTokenizer arr_value = new StringTokenizer(v_param, ",");
				
//				v_userid	= arr_value.nextToken();
				v_seq		= arr_value.nextToken();
			}
			sql = " Select edustart, eduend, substring(appdate,1,8) propstart, (edustart - 1) propend From TZ_TRUSTPROPOSE ";
			sql +=" Where seq = " + SQLString.Format(v_seq);
			
			ls = connMgr.executeQuery(sql);
			while(ls.next())
			{
				v_edustart	= ls.getString("edustart");
				v_edupend	= ls.getString("eduend");
				v_propstart = ls.getString("propstart");
				v_propend 	= ls.getString("propend");
			}
									
			box.put("p_subj",		v_subj);
			box.put("p_year",		v_year);
			box.put("p_subjseq",	v_subjseq);
			box.put("p_edustart", 	v_edustart);
			box.put("p_eduend", 	v_edupend);
			box.put("p_propstart", 	v_propstart);
			box.put("p_propend", 	v_propend);
			
			//isOk3 = UpdateSubjseq(connMgr, box);			

			
			// ��� ���� ������ ��û�� ���̺� ���� & �԰�.
            isOk4 = UpdateTrustPropose(box, connMgr);
			
			if(isOk * isOk2 * isOk3 * isOk4 > 0)
			{
                connMgr.commit();
            } else {
                connMgr.rollback();
            }
        }
        catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
	
	/**
	 * ������� - �����Ź ����
	 * @param box      receive from the form object and session
	 * @return isOk    1:insert success,0:insert fail
     */
    public String InsertTrustSubject(RequestBox box,DBConnectionManager connMgr) throws Exception {

        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;
        int isOk2 = 1;
		String rtnvalue = "";

        String v_subj = "";
        String v_luserid = box.getSession("userid");
        String v_grcode   = box.getStringDefault("s_grcode","ALL"); //��������Ʈ������ SELECTBOX �����ְ� �ڵ�
//		String v_edugubun	= box.getStringDefault("p_edugubun","ON");

        try {
			
            connMgr.setAutoCommit(false);
			
			v_subj = getMaxSubjcode(connMgr, box.getString("p_upperclass"), box.getString("p_middleclass"));
            //insert tz_subj table
            sql = "insert into tz_subj ( ";
			sql+= " subj,      	subjnm,			isonoff,     	subjclass,";	//4
            sql+= " upperclass,	middleclass,	lowerclass,  	specials,";		//8
            sql+= " edugubun,   isgoyong,		goyongpricemajor,biyong,";		//12
            sql+= " isuse,      isapproval,     isvisible,      owner,";        //16            
			sql+= " edudays,    edutimes,	    point,     		place,";		//20
			sql+= " score,		inuserid,		indate,			luserid,";	    //24
			sql+= "	ldate,		proposetype,	studentlimit,	edutype,";		//28
			sql+= " muserid,    musertel,       museremail,     eduurl)";		//32
            sql+= " values (";
            sql+= " ?,  ?,	?,	?, ";		//4
            sql+= " ?,  ?,	?,  ?, ";		//8
			sql+= " ?, 	?,	?,  ?, ";		//12
			sql+= " ?, 	?,	?,	?, ";		//16
			sql+= " ?,	?,	?,	?, ";		//20
			sql+= " ?,	?,	?,	?, ";		//24
			sql+= " ?,	?,	?,	?, ";		//28
			sql+= " ?,	?,	?,	?) ";		//32

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, v_subj);							//�����ڵ�
            pstmt.setString( 2, box.getString("p_subjnm"));			//������
            pstmt.setString( 3, box.getString("p_isonoff"));		//�¶���/�������α���/�����Ź����
            pstmt.setString( 4, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000")); //�з��ڵ�
            pstmt.setString( 5, box.getString("p_upperclass"));				//��з�
            pstmt.setString( 6, box.getString("p_middleclass"));			//�ߺз�
            pstmt.setString( 7, box.getStringDefault("p_lowerclass","000"));//�Һз�
            pstmt.setString( 8, box.getString("p_specials"));               //����Ư��
            pstmt.setString( 9, box.getStringDefault("p_edugubun","OFF"));  //��������             
            pstmt.setString(10, box.getString("p_isgoyong"));               //��뺸�迩��(Y/N)
            pstmt.setDouble(11, box.getDouble("p_goyongprice_major"));      //��뺸�� ȯ�޾�            
            pstmt.setInt   (12, box.getInt   ("p_biyong"));                 //������
            pstmt.setString(13, box.getString("p_isuse"));                  //��뿩��
            pstmt.setString(14, "Y");                                       //�������ο��� (���⼱ �׻� Y)   
            pstmt.setString(15, "Y");                                       //�н��ڿ��� �����ֱ� (���⼱ �׻� Y)
            pstmt.setString(16, box.getString("p_owner"));                  //�����ְ�             
            pstmt.setInt   (17, box.getInt   ("p_edudays"));				//�����Ⱓ(��)
            pstmt.setInt   (18, box.getInt   ("p_edutimes"));               //�����ð�            
            pstmt.setInt   (19, box.getInt   ("p_point"));                  //�̼�����
            pstmt.setString(20, box.getString("p_place"));                  //�������
            pstmt.setInt   (21, box.getInt   ("p_score"));                  //��������
            pstmt.setString(22, v_luserid);                                 //������
            pstmt.setString(23, FormatDate.getDate("yyyyMMddHHmmss"));      //������
            pstmt.setString(24, v_luserid);                                 //����������
            pstmt.setString(25, FormatDate.getDate("yyyyMMddHHmmss"));      //����������(ldate)
            pstmt.setString(26, box.getString("p_proposetype"));            //������û����(TZ_CODE GUBUN='0019')
            pstmt.setInt   (27, box.getInt   ("p_studentlimit"));           //����������            
            pstmt.setString(28, box.getString("p_edutype"));				//��������
            pstmt.setString(29, box.getString("p_muserid"));                //����ID        
            pstmt.setString(30, box.getString("p_musertel"));               //���� ��ȭ��ȣ            
            pstmt.setString(31, box.getString("p_museremail"));				//��� E-mail 
            pstmt.setString(32, box.getString("p_eduurl"));                 //��������
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            //box.put("s_upperclass",box.getString("p_upperclass"));
            //box.put("s_middleclass",box.getString("p_middleclass"));

            //�����ְ� �����ڴ� �ش� �����ְ����� INSERT�ϰ� �Ѵ�.
            if (StringManager.substring(box.getSession("gadmin"),0,1).equals("H")) {
                this.SaveGrSubj(connMgr, v_grcode, v_subj, v_luserid); //int isOk3 = 
            }
            //������ �ϰ�� N000001 (���信��) �����׷����� �ڵ� insert �Ѵ�
            else if (StringManager.substring(box.getSession("gadmin"),0,1).equals("A")) {
                this.SaveGrSubj(connMgr, "N000001", v_subj, v_luserid); //int isOk4 = 
            }

            //���������� �ڷ�� Insert
            //int isOk5 = this.InsertBds(connMgr,v_subj);

			
            if (isOk == 1 && isOk2 == 1) {
				rtnvalue = v_subj;
                //connMgr.commit();
            } else {
                connMgr.rollback();
            }

        }
        catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            //if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return rtnvalue;
    }
	
	/**
	 * �����Ź ���� ���� �� ��û�� ���̺� ����.
	 * @param connmgr, v_subj      receive from the form object and session
	 * @return isOk    1:insert success,0:insert fail
     */
     public int UpdateTrustPropose(RequestBox box, DBConnectionManager connMgr) throws Exception {
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        Hashtable   insertData  = null;
        String sql = "";
        String sql2 = "";
        int isOk = 0;
        int isOk2 = 0;
		
//        int    v_tabseq;
		Vector vec_param 	= box.getVector("p_params");
	    String v_param		= ""; 
		String v_userid		= "";
		String v_seq		= "";
		String v_subj		= box.getString("p_subj");
		String v_year		= box.getString("p_year");
		String v_subjseq	= box.getString("p_subjseq");

        try {		
			
            //insert tz_subj table
            sql = " Update TZ_TRUSTPROPOSE";
			sql+= " Set subj = ?, year=?, subjseq=?, grcode='N000001' ";
			sql+= " Where seq=? ";
			
			sql2 = " Insert Into TZ_PROPOSE ";
			sql2 +=" 	(subj, year, subjseq, userid, comp, ";
			sql2 +=" 	appdate, isdinsert, isb2c, ischkfirst, isproposeapproval,";
			sql2 +=" 	chkfinal, luserid, ldate)";
			sql2 +=" Values ";
			sql2 +="     ( ?, ?, ?, ?, ?, ";
			sql2 +="       ?, ?, ?, ?, ?, ";
			sql2 +="       ?, ?, ?)";
			
            pstmt = connMgr.prepareStatement(sql);
            pstmt2 = connMgr.prepareStatement(sql2);
			
			for(int j = 0; j < vec_param.size(); j++) {
                v_param     = vec_param.elementAt(j).toString();    //�����ڵ�����Userid...
				
				StringTokenizer arr_value = new StringTokenizer(v_param, ",");
				
				v_userid	= arr_value.nextToken();
				v_seq		= arr_value.nextToken();
				
				pstmt.setString( 1, v_subj);
	            pstmt.setString( 2, v_year); //������ �ڷ��
	            pstmt.setString( 3, v_subjseq);
	            pstmt.setString( 4, v_seq);

	            isOk = pstmt.executeUpdate();
				
				pstmt2.setString( 1, v_subj);
	            pstmt2.setString( 2, v_year); //������ �ڷ��
	            pstmt2.setString( 3, v_subjseq);
	            pstmt2.setString( 4, v_userid);
	            pstmt2.setString( 5, "0101000000");
	            pstmt2.setString( 6, FormatDate.getDate("yyyyMMddHHmmss"));
	            pstmt2.setString( 7, "Y");
	            pstmt2.setString( 8, "B");
	            pstmt2.setString( 9, "Y");
	            pstmt2.setString(10, "Y");
	            pstmt2.setString(11, "Y");
	            pstmt2.setString(12, v_userid);
	            pstmt2.setString(13, FormatDate.getDate("yyyyMMddHHmmss"));
				
	            isOk = pstmt2.executeUpdate();
				// Tz_studend �� Insert
	            insertData = new Hashtable();
				
				insertData.clear();
	            insertData.put("connMgr",  connMgr);
	            insertData.put("subj",     v_subj);
	            insertData.put("year",     v_year);
	            insertData.put("subjseq",  v_subjseq);
	            insertData.put("userid",   v_userid);
	            insertData.put("luserid",  box.getSession("userid"));
	            insertData.put("isdinsert","N");
	            insertData.put("chkfirst", "");
	            insertData.put("chkfinal", "Y");
	            insertData.put("box",      box);			

		        ProposeBean propBean    = new ProposeBean();
	            
	            isOk2 = propBean.insertStudent(insertData);
			}   
        }
        catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
            //if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk * isOk2;
    }
	 
	 /**
	  * ������� ���
	  * @param box      receive from the form object and session
	  * @return isOk    1:insert success,0:insert fail
	  */
     public String InsertGrseq(RequestBox box, DBConnectionManager connMgr) throws Exception {

        PreparedStatement pstmt = null; //, pstmt2=null;
        ListSet ls	= null;
        ListSet ls2 = null;
        String sql	= "";
        String sql2 = "";
//		int isOk = 0;

        String v_grcode     = box.getString("p_grcode");
        String v_gyear      = box.getString("p_gyear");
//		String v_subjnm		= box.getString("p_subjnm");
        String v_grseq      = "";
//        String v_makeOption = box.getString("p_makeoption");
        String v_luserid	= box.getSession("userid");
//        String v_subjcourse = "";

//        int    v_sulpaper   = box.getInt("p_sulpaper");
//        String v_propstart  = box.getString("p_propstart");
//        String v_propend    = box.getString("p_propend");
//        String v_edustart   = box.getString("p_edustart");
//        String v_eduend     = box.getString("p_eduend");
//        int    v_canceldays = box.getInt("p_canceldays");

//        String v_copy_gyear	= box.getString("p_copy_gyear");
//        String v_copy_grseq	= box.getString("p_copy_grseq");
		
		String v_grseq_value = "";
//		String v_grseq_tru	 = "";
		String v_InsertYn = "";

        try {			
			sql = " Select nvl(ltrim(rtrim(to_char(to_number(max(g.grseq)),'0000'))),'0001') GRS ";
			sql +="	From tz_grseq g";
			sql +=" Where ";
			sql +="	 grcode=" + SQLString.Format(v_grcode);
			sql +="  and gyear=" + SQLString.Format(v_gyear);			
			
			ls = connMgr.executeQuery(sql);
			
			if(ls.next()){
				v_grseq = ls.getString("GRS");
			}
			
			
			if(v_grseq.equals("0001"))
			{
				v_grseq_value = v_grseq;
				v_InsertYn = "Y";
			}
			else
			{
				sql2 = " Select grseq GRS ";
				sql2 +="	From tz_grseq ";
				sql2 +=" Where grcode=" + SQLString.Format(v_grcode);
				sql2 +="   and gyear=" + SQLString.Format(v_gyear);
				sql2 +="   and TRUYN = 'Y' ";
				
				ls2 = connMgr.executeQuery(sql2);

				if(ls2.next()){
					v_grseq_value = ls2.getString("GRS");
				}
				else
				{
					v_grseq_value = v_grseq;
					v_InsertYn = "Y";
				}
			}
			
			
			if(v_InsertYn.equals("Y"))
			{	
	            //insert TZ_Grseq table
	            sql =  "Insert Into TZ_GRSEQ(grcode, gyear, grseq, grseqnm, truyn, luserid, ldate)"
	                +  " Values (?, ?, ?, ?, 'Y', ?, to_date(sysdate,'YYYYMMDDHH24MISS'))";

	            pstmt = connMgr.prepareStatement(sql);
	            
	            pstmt.setString(1,  v_grcode);
	            pstmt.setString(2,  v_gyear);
	            pstmt.setString(3,  "0002");
	            pstmt.setString(4,  "�����Ź����");
	            pstmt.setString(5,  v_luserid);
	            
	            pstmt.executeUpdate(); //isOk = 
	            if ( pstmt != null ) { pstmt.close(); }
			}
			
        }
        catch(Exception ex) {
//            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            //if (isOk > 0) {connMgr.commit();}
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            //if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
//	            if(mssql_connMgr != null) { try { mssql_connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return v_grseq_value;
    }
	
		 
    /**
     * ���� ��� ���� - ���������� ��� �޴´�.
     * @param      	String  p_grcode        �����׷�
     *             	String  p_gyear         ��������
     *             	String  p_grseq         �������
     *          	String  p_course        �ڽ��ڵ�
     *          	DBConnectionManager     conn    DB Connection Manager
     * @return isOk    1:make success,0:make fail
     */
    public String makeSubjseq(     String p_grcode, String p_gyear, String p_grseq
                            ,   String p_course, String p_cyear, String p_courseseq
                            ,   String p_subj,   String p_userid, int p_sulpaper
                            ,   String p_propstart, String p_propend, String p_edustart
                            ,   String p_eduend, int p_canceldays, String p_copy_gyear
                            ,   String p_copy_grseq, DBConnectionManager conn 
                           ) throws Exception { 

        PreparedStatement   pstmt               = null;
        ResultSet           rs                  = null;
        ListSet             ls                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 0;
//        int                 isOk2               = 0;
        
//        String              v_year              = "";
        String              v_subjseq           = "";
        String              v_isBelongCourse    = "Y";
        String              v_subjseqgr         = "";    // �����׷쿡 ���� ������
        String              v_expiredate        = "";
//        String              v_contenttype       = "";

        try { 
            if ( p_propstart.length() != 10 )  
                p_propstart                         = "";
                
            if ( p_propend.length() != 10)    
                p_propend                           = "";
                
            if ( p_edustart.length() != 10)   
                p_edustart                          = "";
                
            if ( p_eduend.length() != 10)     
                p_eduend                            = "";
   
            if ( p_course.equals("000000") )  
                v_isBelongCourse                    = "N";
        
            // ������ ������ Ÿ�Ը� ���Ѵ�.
            sbSQL.append(" select contenttype from tz_subj where subj = " + StringManager.makeSQL(p_subj) + " \n");
            
            ls      = conn.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
//                v_contenttype   = ls.getString("contenttype");
            }
            
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            sbSQL.setLength(0);

            // �⵵�� �������� ������ �´�.
            sbSQL.append(" select  ltrim(rtrim(to_char(to_number(nvl(max(subjseq),'0000')) +1, '0000'))) GRS       \n")
                 .append(" from    tz_subjseq                                                                      \n")
                 .append(" where   subj    = " + StringManager.makeSQL(p_subj  ) + "                               \n")
                 .append(" and     year    = " + StringManager.makeSQL(p_gyear ) + "                               \n");

            ls  = conn.executeQuery(sbSQL.toString());
            
            if ( ls.next() )       
                v_subjseq = ls.getString("GRS");
            else                
                v_subjseq = "0001";
                
            if ( ls != null ) { 
                try { ls.close(); } catch ( Exception e ) { } 
            }

            sbSQL.setLength(0);
            
            sbSQL.append(" select  ltrim(rtrim(to_char(to_number(nvl(max(subjseqgr),'0000')) +1, '0000'))) GRS      \n")
                 .append(" from    tz_subjseq                                                                       \n")
                 .append(" where   subj    = " + SQLString.Format(p_subj   ) + "                                    \n")
                 .append(" and     year    = " + SQLString.Format(p_gyear  ) + "                                    \n")
                 .append(" and     grcode  = " + SQLString.Format(p_grcode ) + "                                    \n");
            
            ls  = conn.executeQuery(sbSQL.toString());
            
            if ( ls.next() )       
                v_subjseqgr = ls.getString("GRS");
            else                
                v_subjseqgr = "0001";

            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            sbSQL.setLength(0);
            
            if ( p_copy_gyear.equals("") || p_copy_grseq.equals("") ) { 
                // �ֱ��� ������ ������ ��������
                sbSQL.append(" select  *                                                                                \n")
                     .append(" from    tz_subjseq                                                                       \n")
                     .append(" where   subj        = " + StringManager.makeSQL(p_subj   )   + "                         \n")
                     .append(" and     year        = " + StringManager.makeSQL(p_gyear  )   + "                         \n")
                     .append(" and     grcode      = " + StringManager.makeSQL(p_grcode )   + "                         \n")
                     .append(" and     subjseq     = (                                                                  \n")
                     .append("                         select  max(subjseq)                                             \n")
                     .append("                         from    tz_subjseq                                               \n")
                     .append("                         where   subj    = " + StringManager.makeSQL(p_subj ) + "         \n")
                     .append("                         and     year    = " + StringManager.makeSQL(p_gyear) + "         \n")
                     .append("                        )                                                                 \n");
            } else { 
                // ������ ������ ������ ��������
                sbSQL.append(" select   *                                                           \n")
                     .append(" from    tz_subjseq                                                   \n")
                     .append(" where   subj     = " + StringManager.makeSQL( p_subj       ) + "     \n")
                     .append(" and     year     = " + StringManager.makeSQL( p_copy_gyear ) + "     \n")
                     .append(" and     grcode   = " + StringManager.makeSQL( p_grcode     ) + "     \n")
                     .append(" and     subjseq  = " + StringManager.makeSQL( p_copy_grseq ) + "     \n");
            }
            
            ls  = conn.executeQuery(sbSQL.toString());
            
            if ( !ls.next() ) {
                if ( ls != null ) { 
                    try { 
                        ls.close(); 
                    } catch ( Exception e ) { } 
                }

                sbSQL.setLength(0);
                
                // ������ ������������ ��ӹ޴´�.
                sbSQL.append(" select * from tz_subj where subj = " + SQLString.Format(p_subj) + " \n");
                
                ls  = conn.executeQuery(sbSQL.toString());
                
                ls.next();
            }

            sbSQL.setLength(0);

            sbSQL.append(" insert into tz_subjseq                                                                                           \n")
                 .append(" (                                                                                                                \n")
                 .append("         subj                , year              , subjseq           , grcode                                     \n")
                 .append("     ,   gyear               , grseq             , isbelongcourse    , course                                     \n")
                 .append("     ,   cyear               , courseseq         , isclosed          , subjnm                                     \n")
                 .append("     ,   studentlimit        , point             , biyong            , edulimit                                   \n")
                 .append("     ,   ismultipaper        , warndays          , stopdays          , gradscore                                  \n")
                 .append("     ,   gradstep            , wstep             , wmtest            , wftest                                     \n")
                 .append("     ,   wreport             , wact              , wetc1             , wetc2                                      \n")
                 .append("     ,   luserid             , ldate             , proposetype       , subjseqgr                                  \n")
                 .append("     ,   score               , isablereview      , gradexam          , gradreport                                 \n")
                 .append("     ,   whtest              , isessential       , gradftest         , gradhtest                                  \n")
                 .append("     ,   place               , bookname          , bookprice         , sulpapernum                                \n")
                 .append("     ,   propstart           , propend           , edustart          , eduend                                     \n")
                 .append("     ,   canceldays          , ischarge          , isopenedu         				                                \n")
                 .append("     ,   reviewdays          , study_count					       				                                \n")
                 .append(" ) values (                                                                                                       \n")
                 .append("         ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , to_date(sysdate,'YYYYMMDDHH24MISS')             , ?                 , ?            \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 		                                        \n")
                 .append("     ,   ?                   , ?                 		               		                                        \n")
                 .append(" )                                                                                                                \n");
            
            pstmt = conn.prepareStatement(sbSQL.toString());
            int pidx = 1;
            pstmt.setString(pidx++, p_subj                       );
            pstmt.setString(pidx++, p_gyear                      );
            pstmt.setString(pidx++, v_subjseq                    );
            pstmt.setString(pidx++, p_grcode                     );
            pstmt.setString(pidx++, p_gyear                      );
            pstmt.setString(pidx++, p_grseq                      );
            pstmt.setString(pidx++, v_isBelongCourse             );
            pstmt.setString(pidx++, p_course                     );
            pstmt.setString(pidx++, p_cyear                      );
            pstmt.setString(pidx++, p_courseseq                  );
            pstmt.setString(pidx++, "N"                          );
            pstmt.setString(pidx++, ls.getString("subjnm"       ));
            pstmt.setInt   (pidx++, ls.getInt   ("studentlimit" ));
            pstmt.setInt   (pidx++, ls.getInt   ("point"        ));
            pstmt.setInt   (pidx++, ls.getInt   ("biyong"       ));
            pstmt.setInt   (pidx++, ls.getInt   ("edulimit"     ));
            pstmt.setString(pidx++, "N"                          );
            pstmt.setInt   (pidx++, ls.getInt   ("warndays"     ));
            pstmt.setInt   (pidx++, ls.getInt   ("stopdays"     ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradscore"    ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradstep"     ));
            pstmt.setInt   (pidx++, ls.getInt   ("wstep"        ));
            pstmt.setInt   (pidx++, ls.getInt   ("wmtest"       ));
            pstmt.setInt   (pidx++, ls.getInt   ("wftest"       ));
            pstmt.setInt   (pidx++, ls.getInt   ("wreport"      ));
            pstmt.setInt   (pidx++, ls.getInt   ("wact"         ));
            pstmt.setInt   (pidx++, ls.getInt   ("wetc1"        ));
            pstmt.setInt   (pidx++, ls.getInt   ("wetc2"        ));
            pstmt.setString(pidx++, p_userid                     );
            pstmt.setString(pidx++, ls.getString("proposetype"  ));
            pstmt.setString(pidx++, v_subjseqgr                  );
            //pstmt.setString(pidx++, ls.getString("usesubjseqapproval"));
            //pstmt.setString(pidx++, ls.getString("useproposeapproval"));
            //pstmt.setString(pidx++, ls.getString("usemanagerapproval"));
            pstmt.setInt   (pidx++, ls.getInt   ("score"             ));
            //pstmt.setDouble(pidx++, ls.getDouble("rndcreditreq"      ));
            //pstmt.setDouble(pidx++, ls.getDouble("rndcreditchoice"   ));
            //pstmt.setDouble(pidx++, ls.getDouble("rndcreditadd"      ));
            //pstmt.setDouble(pidx++, ls.getDouble("rndcreditdeduct"   ));
            //pstmt.setString(pidx++, ls.getString("rndjijung"         ));
            pstmt.setString(pidx++, ls.getString("isablereview"      ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradexam"          ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradreport"        ));
            pstmt.setInt   (pidx++, ls.getInt   ("whtest"            ));
            pstmt.setString(pidx++, ls.getString("isessential"       ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradftest"         ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradhtest"         ));
            pstmt.setString(pidx++, ls.getString("place"             ));
            //pstmt.setString(pidx++, ls.getString("placejh"           ));
            pstmt.setString(pidx++, ls.getString("bookname"          ));
            pstmt.setString(pidx++, ls.getString("bookprice"         ));
            pstmt.setInt   (pidx++, p_sulpaper                        );
            pstmt.setString(pidx++, p_propstart                       );
            pstmt.setString(pidx++, p_propend                         );
            pstmt.setString(pidx++, p_edustart                        );
            pstmt.setString(pidx++, p_eduend                          );
            pstmt.setInt   (pidx++, p_canceldays                      );
            pstmt.setString(pidx++, ls.getString("ischarge"          ));
            pstmt.setString(pidx++, ls.getString("isopenedu"         ));
            //pstmt.setInt   (pidx++, ls.getInt   ("maleassignrate"    ));
            
            pstmt.setInt   (pidx++, ls.getInt   ("reviewdays"));
            pstmt.setInt   (pidx++, ls.getInt   ("study_count"));

            isOk    = pstmt.executeUpdate();
            
            //if(isOk > 0) {
            	//SubjectBean subBean = new SubjectBean();
            	//�������� ���ǰ����� Insert
            	//subBean.InsertBds(conn, p_subj,"MP", "���ǰ�����", p_gyear, v_subjseq);
    
                //�������� �����ϱ�� Insert
            	//subBean.InsertBds(conn, p_subj,"CJ", "�����ϱ��", p_gyear, v_subjseq);
            //}
            
            // �н��������� ������� ���������Ͻø� �����Ϸ� ����
            if ( p_eduend.length() >= 8) { 
                v_expiredate    = StringManager.substring(p_eduend,0,8);
            } else { 
                v_expiredate    = "";
            }

            if ( isOk > 0) { 
                sbSQL.setLength(0);

                // Report �������� Copy;

                sbSQL.append(" insert into tz_projord                                                       \n")
                     .append(" (                                                                            \n")
                     .append("         subj            , year          , subjseq       , projseq            \n")
                     .append("     ,   ordseq          , lesson        , reptype       , isopen             \n")
                     .append("     ,   isopenscore     , title         , contents      , score              \n")
                     .append("     ,   expiredate      , upfile        , upfile2       , luserid            \n")
                     .append("     ,   ldate           , realfile      , realfile2     , upfilezise         \n")
                     .append("     ,   upfilesize2     , ansyn         , useyn                              \n")
                     .append(" )   select  SUBJ                                                             \n")
                     .append("         ,   " + SQLString.Format(p_gyear    ) + "                            \n")
                     .append("         ,   " + SQLString.Format(v_subjseq  ) + "                            \n")
                     .append("         ,   projseq                                                          \n")
                     .append("         ,   ordseq                                                           \n")
                     .append("         ,   lesson                                                           \n")
                     .append("         ,   REPTYPE                                                          \n")
                     .append("         ,   ISOPEN                                                           \n")
                     .append("         ,   ISOPENscore                                                      \n")
                     .append("         ,   TITLE                                                            \n")
                     .append("         ,   CONTENTS                                                         \n")
                     .append("         ,   score                                                            \n")
                     .append("         ,   " + SQLString.Format(v_expiredate)  + "                          \n")
                     .append("         ,   UPFILE                                                           \n")
                     .append("         ,   UPFILE2                                                          \n")
                     .append("         ,   " + SQLString.Format(p_userid)      + "                          \n")
                     .append("         ,   to_date(sysdate,'YYYYMMDDHH24MISS')                              \n")
                     .append("         ,   realfile                                                         \n")
                     .append("         ,   realfile2                                                        \n")
                     .append("         ,   upfilezise                                                       \n")
                     .append("         ,   upfilesize2                                                      \n")
                     .append("         ,   ansyn                                                            \n")
                     .append("         ,   useyn                                                            \n")
                     .append("     from    tz_projord                                                       \n")
                     .append("     where   subj = " + SQLString.Format(p_subj) + "                          \n");
                    
                if ( p_copy_gyear.equals("") || p_copy_grseq.equals("") ) { 
                    // �ֱ��� ������ ������ ��������
                    sbSQL.append(" and year+ subjseq = (                                                   \n")    
                         .append("                           select  max(year + subjseq)                   \n")
                         .append("                           from    tz_projord                             \n")
                         .append("                           where   subj = " + SQLString.Format(p_subj) + "\n")
                         .append("                       )                                                  \n");
                } else { 
                    // ������ ������ ������ ��������
                    sbSQL.append(" and year     = " + SQLString.Format(p_copy_gyear ) + "                   \n")
                         .append(" and subjseq  = " + SQLString.Format(p_copy_grseq ) + "                   \n");
                }

                isOk    = conn.executeUpdate(sbSQL.toString());
                
                isOk    = 1;

                // �򰡹����� Copy;
                ExamPaperBean   exambean    = new ExamPaperBean();
                isOk                        = exambean.insertExamPaper(p_subj, p_gyear, v_subjseq, p_userid);
                isOk                        = 1;
                
                sbSQL.setLength(0);
                

                // ���հ��� Copy;
                sbSQL.append(" insert into tz_offsubjlecture                                                            \n")
                     .append(" (                                                                                        \n")
                     .append("       SUBJ          , YEAR          , SUBJSEQ       , lecture                            \n")
                     .append("     , lectdate      , lectsttime    , lecttime      , sdesc                              \n")
                     .append("     , tutorid       , lectscore     , lectlevel     , luserid                            \n")
                     .append("     , ldate                                                                              \n")
                     .append(" ) select    SUBJ                                                                         \n")
                     .append("         ,   " + SQLString.Format(p_gyear    ) + "                                        \n")
                     .append("         ,   " + SQLString.Format(v_subjseq  ) + "                                        \n")
                     .append("         ,   lecture                                                                      \n")
                     .append("         ,   lectdate                                                                     \n")
                     .append("         ,   lectsttime                                                                   \n")
                     .append("         ,   lecttime                                                                     \n")
                     .append("         ,   sdesc                                                                        \n")
                     .append("         ,   tutorid                                                                      \n")
                     .append("         ,   lectscore                                                                    \n")
                     .append("         ,   lectlevel                                                                    \n")
                     .append("         ,   " + SQLString.Format(p_userid   ) + "                                        \n")
                     .append("         ,   to_date(sysdate,'YYYYMMDDHH24MISS')                                          \n")
                     .append("   from      tz_offsubjlecture                                                            \n")
                     .append("   where     subj    = " + SQLString.Format(p_subj   ) + "                                \n")
                     .append("   and       year    = " + SQLString.Format(p_gyear  ) + "                                \n")
                     .append("   and       subjseq =(  select  max(subjseq)                                             \n")
                     .append("                         from    tz_subjseq                                               \n")
                     .append("                         where   subj    =  " + SQLString.Format(p_subj      ) + "        \n")
                     .append("                         and     year    =  " + SQLString.Format(p_gyear     ) + "        \n")
                     .append("                         and     grcode  =  " + SQLString.Format(p_grcode    ) + "        \n")
                     .append("                         and     subjseq != " + SQLString.Format(v_subjseq   ) + "        \n")
                     .append("                       )                                                                  \n");
                     
                isOk = conn.executeUpdate(sbSQL.toString());
                
                isOk    = 1;
            }
            
            if ( isOk > 0 ) 
                conn.commit();
        } catch ( SQLException e ) {
            if ( conn != null ) { 
                try { 
                    conn.rollback();
                } catch ( Exception ex ) { } 
            }
            
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( conn != null ) { 
                try { 
                    conn.rollback();
                } catch ( Exception ex ) { } 
            }
            
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( rs != null ) { 
                try { 
                    rs.close(); 
                } catch ( Exception e ) { } 
            }
        
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
        }

        return v_subjseq;
    }
	 
	 /**
	  * ����������� ����
	  * @param box      receive from the form object and session
	  * @return isOk    1:update success,0:update fail
      */
     public int UpdateSubjseq(DBConnectionManager connMgr, RequestBox box) throws Exception {
	    ListSet ls = null;
        ListSet ls2 = null;
        ListSet ls3 = null;

        String sql = "";
        String sql5 = "";

        String  v_contenttype = "";

        PreparedStatement pstmt  = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt5 = null;

        int isOk = 0;

        String v_subj		= box.getString("p_subj");		//�����ڵ�
        String v_year		= box.getString("p_year");		//�⵵
        String v_subjseq	= box.getString("p_subjseq");	//��������
        String v_propstart  = box.getString("p_propstart"); //
        String v_propend    = box.getString("p_propend");       //
        String v_edustart   = box.getString("p_edustart");      //
        String v_eduend     = box.getString("p_eduend");        //
        String v_endfirst   = box.getString("p_endfirst");      //
        String v_endfinal   = box.getString("p_endfinal");      //
		
        if(v_propstart.length() == 8)  v_propstart += "00";
        else v_propstart	= "";
        if(v_propend.length() == 8)    v_propend += "23";
        else v_propend	= "";
        if(v_edustart.length() == 8)   v_edustart += "00";
        else v_edustart	= "";
        if(v_eduend.length() == 8)     v_eduend += "23";
        else v_eduend	= "";
        if(v_endfirst.length() != 10)   v_endfirst = "";
        if(v_endfinal.length() != 10)   v_endfinal = "";

        String v_luserid      = box.getSession("userid");       // ���Ǻ������� ����� id�� �����´�.

        //String v_targettablename = "tz_subjseq";
		// �������� ����. 
		String v_limityn 		= box.getString("p_limitYn");
		String v_licensevalue 	= box.getString("p_licensevalue");
		String v_jikvalue 		= box.getString("p_jikvalue");
		
		int pidx = 1;
        try {
			
            // ������ ������ Ÿ��, YESLEARN �����ڵ带 ���Ѵ�.
            sql = " select contenttype from tz_subj where subj= " + StringManager.makeSQL(v_subj);
            ls  = connMgr.executeQuery(sql);
            if(ls.next()) {
                v_contenttype = ls.getString("contenttype");
                //v_aesserialno = ls.getString("aesserialno");
            }
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }

            // ������������ YESLEARN GroupList�� seq�� ���Ѵ�.
            //sql = " select aesseq from tz_subjseq where subj='"+v_subj+"' and year = '"+v_year+"' and subjseq = '"+v_subjseq+ "'";
            //ls  = connMgr.executeQuery(sql);
            //if(ls.next()) {
            //    v_aesseq = ls.getInt("aesseq");
            //}
            //if(ls != null) { try { ls.close(); }catch (Exception e) {} }

            //update TZ_Grseq table
            sql = "update   tz_subjseq       \n"
                + "set      propstart   = ?, \n"
                + "         propend     = ?, \n"
                + "         edustart    = ?, \n"
                + "         eduend      = ?, \n"
                + "         endfirst    = ?, \n"
                + "         endfinal    = ?, \n"
                + "         isgoyong    = ?, \n"
                + "         ismultipaper= ?, \n"
                + "         subjnm      = ?, \n"
                + "         luserid     = ?, \n"
                + "         ldate       = to_date(sysdate,'YYYYMMDDHH24MISS'), \n"
                + "         studentlimit= ?, \n"
                + "         point       = ?, \n"
                + "         biyong      = ?, \n"
                + "         edulimit    = ?, \n"
                + "         warndays    = ?, \n"
                + "         stopdays    = ?, \n"
                + "         gradscore   = ?, \n"
                + "         gradstep    = ?, \n"
                + "         wstep       = ?, \n"
                + "         wmtest      = ?, \n"
                + "         wftest      = ?, \n"
                + "         wreport     = ?, \n"
                + "         wact        = ?, \n"
                + "         wetc1       = ?, \n"
                + "         wetc2       = ?, \n"
                + "         proposetype = ?, \n"
                + "         gradexam    = ?, \n"
                + "         gradreport  = ?, \n"
                + "         whtest      = ?, \n"
                + "         rndcreditreq        = ?, \n"
                + "         rndcreditchoice     = ?, \n"
                + "         rndcreditadd        = ?, \n"
                + "         rndcreditdeduct     = ?, \n"
                + "         isablereview        = ?, \n"
                + "         score               = ?, \n"
                + "         tsubjbudget         = ?, \n"
                + "         rndjijung           = ?, \n"
                + "         isessential         = ?, \n"
                + "         gradftest           = ?, \n"
                + "         gradhtest           = ?, \n"
                + "         isvisible           = ?, \n"
                + "         place       = ?, \n"
                + "         placejh     = ?, \n"
                + "         sulpapernum = ?, \n"
                + "         canceldays  = ?, \n"
                + "			limityn		= ?, \n"
                + "			licensegrade= ?, \n"
                + "			jikgrade 	= ?, \n"
                + "			reviewdays 	= ?  \n"
                + "			study_count	= ?  \n"
                + "where    subj        = ?  \n"
                + "and      year        = ?  \n"
                + "and      subjseq     = ?  \n";


            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(pidx++, v_propstart);
            pstmt.setString(pidx++, v_propend);
            pstmt.setString(pidx++, v_edustart);
            pstmt.setString(pidx++, v_eduend);
            pstmt.setString(pidx++, v_endfirst);
            pstmt.setString(pidx++, v_endfinal);
            pstmt.setString(pidx++, box.getString("p_isgoyong")) ;
            pstmt.setString(pidx++, box.getStringDefault("p_ismultipaper","N")) ;
            pstmt.setString(pidx++, box.getString("p_subjnm")) ;
            pstmt.setString(pidx++, v_luserid) ;
            pstmt.setInt   (pidx++, box.getInt("p_studentlimit"));
            pstmt.setInt   (pidx++, box.getInt("p_point"));
            pstmt.setInt   (pidx++, box.getInt("p_biyong"));
            pstmt.setInt   (pidx++, box.getInt("p_edulimit"));
            pstmt.setInt   (pidx++, box.getInt("p_warndays"));
            pstmt.setInt   (pidx++, box.getInt("p_stopdays"));
            pstmt.setInt   (pidx++, box.getInt("p_gradscore"));
            pstmt.setInt   (pidx++, box.getInt("p_gradstep"));
            pstmt.setInt   (pidx++, box.getInt("p_wstep"));
            pstmt.setInt   (pidx++, box.getInt("p_wmtest"));
            pstmt.setInt   (pidx++, box.getInt("p_wftest"));
            pstmt.setInt   (pidx++, box.getInt("p_wreport"));
            pstmt.setInt   (pidx++, box.getInt("p_wact"));
            pstmt.setInt   (pidx++, box.getInt("p_wetc1"));
            pstmt.setInt   (pidx++, box.getInt("p_wetc2"));
            pstmt.setInt   (pidx++, box.getInt("p_proposetype"));
            //------------------------------------------------------------------------------//
            pstmt.setInt   (pidx++, box.getInt("p_gradexam"));                 	//�̼�����(��)
            pstmt.setInt   (pidx++, box.getInt("p_gradreport"));               	//�̼�����(����Ʈ)
            pstmt.setInt   (pidx++, box.getInt("p_whtest"));                   	//����ġ(������)
            pstmt.setDouble(pidx++, box.getDouble("p_rndcreditreq"));			//��������(��������)-�ʼ�
            pstmt.setDouble(pidx++, box.getDouble("p_rndcreditchoice"));		//��������(��������)-����
            pstmt.setDouble(pidx++, box.getDouble("p_rndcreditadd"));			//��������(��������)-��������
            pstmt.setDouble(pidx++, box.getDouble("p_rndcreditdeduct"));		//��������(��������)-��������
            pstmt.setString(pidx++, box.getString("p_isablereview"));       	//�������ɿ���
            pstmt.setInt   (pidx++, box.getInt("p_score"));                    	//��������
            pstmt.setInt   (pidx++, box.getInt("p_tsubjbudget"));              	//��������
            pstmt.setString(pidx++, box.getStringDefault("p_rndjijung","N"));	//������������
            pstmt.setString(pidx++, box.getStringDefault("p_isessential","0"));	//
            pstmt.setInt   (pidx++, box.getInt("p_gradftest"));                	//
            pstmt.setInt   (pidx++, box.getInt("p_gradhtest"));                	//
            pstmt.setString(pidx++, box.getStringDefault("p_isvisible","Y")); 	//�н��ڿ��� �����ֱ�
            pstmt.setString(pidx++, box.getString("p_place")); 					//�������
            pstmt.setString(pidx++, box.getString("p_placejh")); 				//�������
            pstmt.setInt   (pidx++, box.getInt("p_sulpaper")); 					// ������ ��ȣ
            pstmt.setInt   (pidx++, box.getInt("p_canceldays")); 				//��ҳ���
			
            pstmt.setString(pidx++, v_limityn); 								//�������ѿ���
            pstmt.setString(pidx++, v_licensevalue); 							//�ڰ��� ���
            pstmt.setString(pidx++, v_jikvalue); 								//���� ���
            
            pstmt.setInt   (pidx++, box.getInt("p_reviewdyas")); 				//�������ɱⰣ
            pstmt.setInt   (pidx++, box.getInt("p_study_count")); 				//����Ƚ��(�������)

            pstmt.setString(pidx++, v_subj) ;
            pstmt.setString(pidx++, v_year) ;
            pstmt.setString(pidx++, v_subjseq) ;

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            if (v_contenttype.equals("Y")) {
                String v_edustart_value = "";
                String v_eduend_value   = "";
                if (v_edustart.equals("")) {
                    v_edustart_value = "sysdate";
                } else {
                    v_edustart_value = v_edustart.substring(0,8);
                    v_edustart_value = "to_date('" + v_edustart_value + "')";
                }
                if (v_eduend.equals("")) {
                    v_eduend_value = "sysdate";
                } else {
                    v_eduend_value = v_eduend.substring(0,8);
                    v_eduend_value = "to_date('" + v_eduend_value + "')";
                }
			}
			
			sql5 = " insert into tz_booksubj ";
			sql5 +="        (subj, year, subjseq, bookno, luserid, ldate)";
			sql5 +=" values (?,    ?,    ?,       ?,      ?,       ?)";
			
			pstmt5 = connMgr.prepareStatement(sql5);
			
			StringTokenizer arr_tmp = new StringTokenizer(box.getString("p_bookno_value"), "/");
			
			while( arr_tmp.hasMoreTokens())
			{
				pstmt5.setString(1, v_subj);
				pstmt5.setString(2, v_year);
				pstmt5.setString(3, v_subjseq);
				pstmt5.setString(4, arr_tmp.nextToken());
				pstmt5.setString(5, v_luserid);
				pstmt5.setString(6, FormatDate.getDate("yyyyMMddHHmmss")); 
				
	            pstmt5.executeUpdate(); //isOk5 = 
			}	
        }
        catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
            if(ls3 != null) { try { ls3.close(); }catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
        }
        return isOk;
    }
	 
	/**
	 * �����ڵ� ���� - �����Ź ����
	 * @param box      receive from the form object and session
	 * @return isOk    1:insert success,0:insert fail
     */
    public int UpdateTrustsubj(RequestBox box) throws Exception {
		 
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		String sql = "";
		int isOk = 0;
//		int isOk2 = 1;
//		String rtnvalue = "";
		
		String v_subj     = box.getString("p_subj");
		String v_luserid  = box.getSession("userid");

        try {
			connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
			
            //insert tz_subj table
            sql = "Update  tz_subj Set ";
			sql+= " subjnm=?,		subjclass=?, 	upperclass=?,	middleclass=?,";	//4
            sql+= " lowerclass=?,  	edugubun=?,     eduurl=?, 	  	isuse=?,";		    //8
            sql+= " biyong=?,   	isgoyong=?,     goyongpricemajor=?,    edutype=?,";	//12
			sql+= " edudays=?,      edutimes=?,     luserid=?,      ldate=?,";		    //16
			sql+= " owner=?,        muserid=?,      musertel=?,     museremail = ?";    //20
			sql+= " Where subj =?";		//21            

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, box.getString("p_subjnm"));			//������
            pstmt.setString( 2, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000")); //�з��ڵ�
            pstmt.setString( 3, box.getString("p_upperclass"));				//��з�
            pstmt.setString( 4, box.getString("p_middleclass"));			//�ߺз�
            pstmt.setString( 5, box.getStringDefault("p_lowerclass","000"));//�Һз�
            pstmt.setString( 6, box.getStringDefault("p_edugubun","OFF"));   //��������             
            pstmt.setString( 7, box.getString("p_eduurl"));                 //����Ư��
            pstmt.setString( 8, box.getString("p_isuse"));                  //��뿩��
            pstmt.setInt   ( 9, box.getInt   ("p_biyong"));                 //������            
            pstmt.setString(10, box.getString("p_isgoyong"));               //��뺸�迩��(Y/N)
            pstmt.setString(11, box.getString("p_goyongprice_major"));      //��뺸�� ȯ�޾�
            pstmt.setString(12, box.getString("p_edutype"));                //��������            
            pstmt.setInt   (13, box.getInt   ("p_edudays"));				//�����Ⱓ(��)
            pstmt.setInt   (14, box.getInt   ("p_edutimes"));               //�����ð�
            pstmt.setString(15, v_luserid);                                 //����������
            pstmt.setString(16, FormatDate.getDate("yyyyMMddHHmmss"));      //����������(ldate)
            pstmt.setString(17, box.getString("p_owner"));					//�����ְ� 
            pstmt.setString(18, box.getString("p_muserid"));                //����ID            
            pstmt.setString(19, box.getString("p_musertel"));				//���翬��ó
            pstmt.setString(20, box.getString("p_museremail"));					//�����̸��� 			
            pstmt.setString(21, v_subj);							        //�����ڵ�
            
            isOk = pstmt.executeUpdate();

            if (isOk == 1) {
                connMgr.commit();
            } else {
                connMgr.rollback();
            }

        }
        catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
	 
	public static String getTrustProposeBill(String v_seq) throws Exception{
		String sql = "";
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		 
		String rtnValue = "0|0";
		 
		try{
			connMgr = new DBConnectionManager();
			 
			sql = "Select edubill, goyongbill ";
			sql +=" From tz_trustpropose ";
			sql +=" Where seq = " + SQLString.Format(v_seq);
			 
			ls = connMgr.executeQuery(sql);
			if(ls.next())
			{
				rtnValue = ls.getString("edubill") + "|" + ls.getString("goyongbill");
			}
			 
		}catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		 	 
		return rtnValue;
	}
	 
	/**
	 * �����Ź���� �����ڰ��� LIst
	 * @param box          receive from the form object and session
	 * @return ArrayList   ��������Ʈ
     */
    public ArrayList TrustSelectCertList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql  = "";
        DataBox dbox = null;
		String v_userid = box.getString("p_userid");

        try {
			sql = " Select distinct h.licnm, h.licgbn, h.regdate, h.edudate, h.userid, licgubun ";
			sql +="\n From tz_certhst h, tz_certmst m ";
			sql +="\n Where h.licgbn = m.liccd ";
			sql +="\n	  and userid = "  + SQLString.Format(v_userid);
			sql +="\n and m.licgubun = 'E'";
			sql +="\n Order by h.regdate ASC";

            connMgr = new DBConnectionManager();
            list = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
	
	/**
	 * �����ڵ� ���� - �����Ź ����
	 * @param box      receive from the form object and session
	 * @return isOk    1:insert success,0:insert fail
     */
    public int UpdateTrustApproval(RequestBox box) throws Exception {
		 
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		String sql = "";
//		String sql1 = "";
		int isOk = 0;
		int isOk2 = 1;
//		String rtnvalue = "";
		
//		String v_seq	= box.getString("p_seq");
        String v_content= StringManager.replace(box.getString("p_educontent"),"<br>","\n");

        try {
			connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
			
			String v_filereal    = box.getRealFileName("p_file");
	        String v_filenew     = box.getNewFileName ("p_file");			

	        if(v_filereal.length() == 0) {
				if(!box.getString("p_file0").equals(""))
				{
					v_filereal    = "";
					v_filenew     = "";				
				}
				else
				{
					v_filereal    = box.getRealFileName("p_file0");
			        v_filenew     = box.getNewFileName ("p_file0");					
				}
	        }
			
            //insert tz_subj table
            sql = "Update  tz_trustpropose Set ";
			sql+= "  edugubun = ?,  subjnm = ?,   edustart = ?, eduend = ?,  eduday =?,  edutime = ?,";
			sql+= "  educomp = ?, 	eduplace = ?, musernm =?,   mphone = ?,  mfax =?,    memail = ?,";
            sql+= "  eduurl = ?,    edubill = ?,  billgubun =?, accountnum = ?, goyongyn = ?,";
			sql+= "  myduty = ?,  	dutyterm = ?, result = ?, 	secstat = 'M', realfile = ?, 	newfile = ?, educontent= ? ";
			sql+= " Where seq = ?";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, box.getString("p_edugubun"));					//��������
            pstmt.setString( 2, box.getString("p_subjnm")); 					//������
            pstmt.setString( 3, box.getString("p_edustart"));				//���������� 
            pstmt.setString( 4, box.getString("p_eduend"));					//����������
            pstmt.setInt   ( 5, box.getInt("p_edudays"));				//�����Ⱓ			
            pstmt.setInt   ( 6, box.getInt("p_edutimes"));               	//�����ð�
            
            pstmt.setString( 7, box.getString("p_ownernm"));                	//�����ְ�
            pstmt.setString( 8, box.getString("p_eduplace"));                	//�������
            pstmt.setString( 9, box.getString("p_musernm"));				//�����
            pstmt.setString(10, box.getString("p_mphone"));               //����� ��ȭ��ȣ
            pstmt.setString(11, box.getString("p_mfax"));                //����� �ѽ�
            pstmt.setString(12, box.getString("p_memail"));              //����� �̸���
            
            pstmt.setString(13, box.getString("p_eduurl"));              //����URL
            pstmt.setString(14, box.getString("p_edubill"));             //������
            pstmt.setString(15, box.getString("p_billgubun"));           //������ ����
            pstmt.setString(16, box.getString("p_accountnum"));          //���¹�ȣ             
            pstmt.setString(17, box.getString("p_goyongyn"));            //��뺸�迩��
            pstmt.setString(18, box.getString("p_myduty"));              //��������
            pstmt.setString(19, box.getString("p_dutyterm"));			 //������ ����Ⱓ
            pstmt.setString(20, box.getString("p_result"));				 //����ȿ��			
            pstmt.setString(21, v_filereal);							 //÷������
            pstmt.setString(22, v_filenew); 							 //÷������			
            pstmt.setString(23, v_content);			
            pstmt.setInt(24, box.getInt("p_seq"));						//seq
            
            isOk = pstmt.executeUpdate();
  
            if ((isOk * isOk2) == 1) {
                connMgr.commit();
            } else {
                connMgr.rollback();
            }

        }
        catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
	 
	/**
	 * �����Ź���� ���� LIst
	 * @param box          receive from the form object and session
	 * @return ArrayList   ��������Ʈ
     */
    public ArrayList TrustSelectSubjectExcel(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql  = "";
        DataBox dbox = null;
		
        String  ss_grcode       = box.getStringDefault("s_grcode","ALL");       //�����ְ�
        String  ss_gyear        = box.getStringDefault("s_gyear","ALL");       //�����ְ�        
        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");   //�����з�
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");  //�����з�
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");   //�����з�
        String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");   //����&�ڽ�
        String  ss_subjseq      = box.getStringDefault("s_subjseq","ALL");   //����&�ڽ�        
        String  ss_midstat      = box.getStringDefault("p_midstat","ALL");      //HRD ����� ���� ����
        String  ss_secstat      = box.getStringDefault("p_secstat","ALL");      //HRD ���� ���� ����
        String  v_searchtext    = box.getString("p_searchtext");

        String  v_orderColumn   = box.getString("p_order");               //������ �÷���
        String  v_orderType     = box.getString("p_orderType");                 //������ ����

        try {
            
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
			sql = " select distinct seq, a.subj tmpsubj, b.subj, a.subjnm tmpsubjnm, b.subjnm, a.userid, ";
			sql +="   a.eduurl, a.edustart, a.eduend, a.educomp, a.appdate, firstat, midstat, secstat, ";
			sql +="	  a.musernm, a.mphone, a.mfax, a.memail, a.edubill, a.billgubun, a.accountnum, a.goyongyn, a.goyongbill, ";
			sql +="	  b.owner, m.name, m.deptnam, m.jikwinm ";
			sql +=" From " ;
			sql +=" 	tz_trustpropose a,";
			sql +=" 	vz_scsubjseq b,";
			sql +=" 	tz_member m ";
			sql +=" Where ";
			sql +=" 	a.subj = b.subj(+) and a.year = b.year and a.subjseq = b.subjseq(+) and a.userid = m.userid ";
			sql +=" 	and a.firstat = 'Y' ";

            //�����׷�
            if (!ss_grcode.equals("ALL")) {
                sql+= "         and b.grcode = " + StringManager.makeSQL(ss_grcode) + "	\n";
            }

            //�����⵵
            if (!ss_gyear.equals("ALL")) {
                sql+= "         and b.gyear = " + StringManager.makeSQL(ss_gyear) + "	\n";
            }            

            if (!ss_subjcourse.equals("ALL")) {
                sql+= "   and b.subj = " + SQLString.Format(ss_subjcourse);
            } else {
                if (!ss_upperclass.equals("ALL")) {
                    if (!ss_upperclass.equals("ALL")) {
                        sql += " and b.upperclass = "+SQLString.Format(ss_upperclass);
                    }
                    if (!ss_middleclass.equals("ALL")) {
                        sql += " and b.middleclass = "+SQLString.Format(ss_middleclass);
                    }
                    if (!ss_lowerclass.equals("ALL")) {
                        sql += " and b.lowerclass = "+SQLString.Format(ss_lowerclass);
                    }
                }
            }
            
            if (!ss_subjseq.equals("ALL")) {
                sql += " and b.subjseq = "+SQLString.Format(ss_subjseq);
            }    
            
			if (!ss_midstat.equals("ALL")) {
                sql += " and a.midstat = "+SQLString.Format(ss_midstat);
            }
			if (!ss_secstat.equals("ALL")) {
                sql += " and a.secstat = "+SQLString.Format(ss_secstat);
            }

            if(!v_searchtext.equals("")){
            	v_searchtext = v_searchtext.replaceAll("'", "");
            	v_searchtext = v_searchtext.replaceAll("/", "//");
            	v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
                sql += " and upper(a.subjnm) like " + StringManager.makeSQL("%"+v_searchtext+"%") + " escape '/'";
            }

            if(v_orderColumn.equals("")) {
                sql+= " order by a.appdate, a.subjnm ";
            } else {
                sql+= " order by " + v_orderColumn + v_orderType;
            }
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
	
	/**
	 * �����Ź ���� �����԰�
	 * @param box      receive from the form object and session
	 * @return isOk    1:insert success,0:insert fail
     */
    public int TruMemInput(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql  = "";
        String sql2 = "";    
        int isOk    = 0; 
        int v_seq   = 0;
        
//        String  v_user_id   = box.getSession("userid");
//        String  v_muserid   = ApprovalAdminBean.getMuserid(box);  // �μ��� ����
        
        String v_edustart   = box.getString("p_edustart").replaceAll("-","");
        String v_eduend     = box.getString("p_eduend").replaceAll("-","");        

        try {            
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql = "select nvl(max(seq), 0) cnt from TZ_TRUSTPROPOSE";
   
            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                v_seq = ls.getInt("cnt"); 
            }                    
            
            //////////////////////////////////   �����Ź ��û table �� �Է�  ///////////////////////////////////////////////////////////////////
            sql2 =  "insert into TZ_TRUSTPROPOSE("
                  + "  	seq, 		edugubun, 	subjnm, 	userid, 	edustart,"
                  + "  	eduend,		eduday,		edutime,	educomp,	eduplace,"
                  + " 	musernm,	mphone,		mfax,		edubill,	billgubun,"
                  + "	accountnum,	goyongyn,	goyongbill,	educontent,	myduty,"
                  + " 	dutyterm,	result,		appdate,	realfile,	newfile,"
                  + "	firstat,	midstat,    rejectstat,	eduurl,		memail,     year)" 
                  + " values "
				  + "(?, ?, ?, ?, ?,"
				  + " ?, ?, ?, ?, ?,"
				  + " ?, ?, ?, ?, ?,"
				  + " ?, ?, ?, ?, ?,"
				  + " ?, ?, to_date(sysdate,'YYYYMMDDHH24MISS'), ?, ?,"
				  + " 'Y', 'B', 'N', ?, ?,  to_char(sysdate, 'YYYY'))";            

            pstmt = connMgr.prepareStatement(sql2);

            pstmt.setInt( 1, v_seq+1);            
            pstmt.setString( 2, box.getString("p_edugubun"));              
            pstmt.setString( 3, box.getString("p_subjnm"));
            pstmt.setString( 4, box.getString("p_userid"));
            pstmt.setString( 5, v_edustart);
            pstmt.setString( 6, v_eduend);
            pstmt.setString( 7, box.getString("p_eduday"));
            pstmt.setString( 8, box.getString("p_edutime"));
            pstmt.setString( 9, box.getString("p_educomp"));
            pstmt.setString( 10, box.getString("p_eduplace"));
            pstmt.setString( 11, box.getString("p_musernm"));
            pstmt.setString( 12, box.getString("p_mphone"));
            pstmt.setString( 13, box.getString("p_mfax"));
            pstmt.setString( 14, box.getString("p_edubill"));
            pstmt.setString( 15, box.getString("p_billgubun"));
            pstmt.setString( 16, box.getString("p_accountnum"));
            pstmt.setString( 17, box.getString("p_goyongyn"));
            pstmt.setString( 18, box.getString("p_goyongbill"));            
            pstmt.setString( 19, box.getString("p_educontent"));
            pstmt.setString( 20, box.getString("p_myduty"));
            pstmt.setString( 21, box.getString("p_dutyterm"));
            pstmt.setString( 22, box.getString("p_result"));
            pstmt.setString( 23, box.getRealFileName("p_file"));
            pstmt.setString( 24, box.getNewFileName("p_file"));  
            pstmt.setString( 25, box.getString("p_eduurl"));
            pstmt.setString( 26, box.getString("p_memail"));            
                            
            isOk = pstmt.executeUpdate();

            if(isOk > 0) {
                if(connMgr != null) { try { connMgr.commit(); }catch (Exception e10) {} }
            }    
        }
        catch(Exception ex) {
            if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }            
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
     
	/**
	 * ������� - ��������
	 * @param box      receive from the form object and session
	 * @return isOk    1:insert success,0:insert fail
	 */
	public int InsertBookSubject(RequestBox box) throws Exception { 
		DBConnectionManager    connMgr     = null;
		PreparedStatement 		pstmt       = null;
		StringBuffer           sbSQL       = new StringBuffer("");
		int 					isOk 	    = 0;
		int 					isOk2 	    = 1;

		String                 v_subj      = "";
		String                 v_luserid 	= box.getSession      ("userid"            );
		String                 v_grcode   	= box.getStringDefault("s_grcode"   , "ALL");   // ���񸮽�Ʈ������ SELECTBOX �����ְ� �ڵ�

		int					pidx		= 1;
		try { 
			connMgr     = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			// insert tz_subj table
			sbSQL.append(" insert into tz_subj (                                                                                            \n")
				.append("         subj                , subjnm                    , isonoff               , subjclass                      \n")
				.append("     ,   upperclass          , middleclass               , lowerclass            , specials                       \n")
				.append("     ,   muserid             , cuserid                   , isuse                 , ispropose                      \n")
				.append("     ,   biyong              , edudays                   , studentlimit          , usebook                        \n")
				.append("     ,   bookprice           , owner                     , producer              , crdate                         \n")
				.append("     ,   language            , server                    , dir                   , eduurl                         \n")
				.append("     ,   vodurl              , preurl                    , ratewbt               , ratevod                        \n")
				.append("     ,   env                 , tutor                     , bookname              , sdesc                          \n")
				.append("     ,   warndays            , stopdays                  , point                 , edulimit                       \n")
				.append("     ,   gradscore           , gradstep                  , wstep                 , wmtest                         \n")
				.append("     ,   wftest              , wreport                   , wact                  , wetc1                          \n")
				.append("     ,   wetc2               , place                     , edumans               , isessential                    \n")
				.append("     ,   score               , inuserid                  , indate                , luserid                        \n")
				.append("     ,   ldate               , proposetype               , edutimes              , edutype                        \n")
				.append("     ,   intro               , explain                   , gradexam              , gradreport                     \n")
				.append("     ,   bookfilenamereal    , bookfilenamenew           , conturl               , musertel                       \n")
				.append("     ,   gradftest           , gradhtest                 , isvisible             , isalledu                       \n")
				.append("     ,   isapproval          , isintroduction            , eduperiod             , introducefilenamereal          \n")
				.append("     ,   introducefilenamenew, informationfilenamereal   , informationfilenamenew, ischarge                       \n")
				.append("     ,   isopenedu           , gradftest_flag            , gradreport_flag                                        \n")
				.append("     ,   isgoyong			  , goyongpricemajor	      , goyongpriceminor						  			   \n")
				.append("     ,   isoutsourcing		  , graduatednote             , isablereview	                                       \n")
				.append("     ,   subj_gu		      , content_cd                , study_count		      , reviewdays                     \n")
				.append("     ,   sel_dept		      , sel_post																		   \n")
				.append("     ,   lev		      	  , gubn               		  , grade		      	  , test                     	   \n")
				.append("     ,   cp_accrate		  , goyongpricestand		  , contenttype			  , jikmu_limit					   \n")
				.append("     ,   cpsubj                                                                  					               \n")
				.append(" ) values (                                                                                                       \n")
				.append("         ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("     ,   ?                   , ?                         , ?                                                      \n")
				.append("	  ,   ?                   , ?                         , ?													   \n")
				.append("	  ,   ?                   , ?                      	  , ?													   \n")
				.append("     ,   ?                   , ? 			              , ?                     , ?                              \n")
				.append("	  ,   ?                   , ?                      															   \n")
				.append("     ,   ?                   , ?                         , ?                     , ?                              \n")
				.append("	  ,   ?                   , ? 	                   	  , ? 					  , ?							   \n")
				.append("	  ,   ?                                                                      						           \n")
				.append(" )                                                                                                                \n");

			pstmt       = connMgr.prepareStatement(sbSQL.toString());
			v_subj      = getMaxSubjcode(connMgr, box.getString("p_upperclass"), box.getString("p_middleclass") );
			//v_subj		= box.getString("p_subj");

			pstmt.setString(pidx++, "K"+v_subj                               );                     // �����ڵ�
			pstmt.setString(pidx++, box.getString("p_subjnm"            ));                     // �����
			pstmt.setString(pidx++, box.getString("p_isonoff"           ));                     // �¶���/�������α���
			pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000") ); // �з��ڵ�
			pstmt.setString(pidx++, box.getString("p_upperclass"        ));                     // ��з�
			pstmt.setString(pidx++, box.getString("p_middleclass"       ));                     // �ߺз�
			pstmt.setString(pidx++, box.getStringDefault("p_lowerclass"         , "000" ));     // �Һз�
			pstmt.setString(pidx++, box.getString("p_specials"          ));                     // ����Ư��
			pstmt.setString(pidx++, box.getString("p_muserid"           ));                     // ����ID
			pstmt.setString(pidx++, box.getString("p_cuserid"           ));                     // [X]���������ID
			pstmt.setString(pidx++, box.getString("p_isuse"             ));                     // ��뿩��
			pstmt.setString(pidx++, box.getString("p_ispropose"         ));                     // [X]������û����(Y/N)
			pstmt.setInt   (pidx++, box.getInt   ("p_biyong"            ));                     // ������
			pstmt.setInt   (pidx++, box.getInt   ("p_edudays"           ));                     // �����Ⱓ(��)
			pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"      ));                     // ���������
			pstmt.setString(pidx++, box.getString("p_usebook"           ));                     // [X]�����뿩��
			pstmt.setInt   (pidx++, Integer.parseInt(box.getStringDefault("p_bookprice"          , "0"   )));     // [X]�����
			pstmt.setString(pidx++, box.getString("p_owner"             ));                     // [X]���������
			pstmt.setString(pidx++, box.getString("p_producer"          ));                     // [X]����������
			pstmt.setString(pidx++, box.getString("p_crdate"            ));                     // [X]��������
			pstmt.setString(pidx++, box.getString("p_language"          ));                     // [X]����
			pstmt.setString(pidx++, box.getString("p_server"            ));                     // [X]����
			pstmt.setString(pidx++, box.getString("p_dir"               ));                     // [X]���������
			pstmt.setString(pidx++, box.getString("p_eduurl"            ));                     // [X]����URL
			pstmt.setString(pidx++, box.getString("p_vodurl"            ));                     // [X]VOD���
			pstmt.setString(pidx++, box.getString("p_preurl"            ));                     // ������URL
			pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"           ));                     // [X]�н����(WBT%)
			pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"           ));                     // [X]�н����(VOD%)
			pstmt.setString(pidx++, box.getString("p_env"               ));                     // [X]�н�ȯ��
			pstmt.setString(pidx++, box.getString("p_tutor"             ));                     // [X]���缳��
			pstmt.setString(pidx++, box.getStringDefault("p_bookname"           , ""    ));     // [X]�����
			pstmt.setString(pidx++, box.getString("p_sdesc"             ));                     // [X]���
			pstmt.setInt   (pidx++, box.getInt   ("p_warndays"          ));                     // [X]�н����������
			pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"          ));                     // [X]�н�����������
			pstmt.setInt   (pidx++, box.getInt   ("p_point"             ));                     // ��������
			pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"          ));                     // [X]1���ִ��н���
			pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"         ));                     // �������-����
			pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"          ));                     // �������-�⼮��
			pstmt.setDouble(pidx++, box.getDouble("p_wstep"             ));                     // ����ġ-�⼮��
			pstmt.setDouble(pidx++, box.getDouble("p_wmtest"            ));                     // [X]����ġ-�߰���
			pstmt.setDouble(pidx++, box.getDouble("p_wftest"            ));                     // ����ġ-������
			pstmt.setDouble(pidx++, box.getDouble("p_wreport"           ));                     // ����ġ-����
			pstmt.setDouble(pidx++, box.getDouble("p_wact"              ));                     // [X]����ġ-��Ƽ��Ƽ
			pstmt.setDouble(pidx++, box.getDouble("p_wetc1"             ));                     // ����ġ-������
			pstmt.setDouble(pidx++, box.getDouble("p_wetc2"             ));                     // [X]����ġ-���������
			pstmt.setString(pidx++, box.getString("p_place"             ));                     // �������
			pstmt.setString(pidx++, box.getString("p_edumans"           ));                     // ������ �������
			pstmt.setString(pidx++, box.getStringDefault("p_isessential"        , "0"   ));     // ���񱸺�
			pstmt.setInt   (pidx++, box.getInt   ("p_score"             ));                     // ��������
			pstmt.setString(pidx++, v_luserid                            );                     // ������
			pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss") );                     // ������
			pstmt.setString(pidx++, v_luserid                            );                     // ����������
			pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));                      // ����������(ldate)
			pstmt.setString(pidx++, box.getString("p_proposetype"       ));                     // ������û����(TZ_CODE GUBUN='0019')
			pstmt.setInt   (pidx++, box.getInt   ("p_edutimes"          ));                     // �����ð�
			pstmt.setString(pidx++, box.getString("p_edutype"           ));                     // ��������
			pstmt.setString(pidx++, box.getString("p_intro"             ));                     // ������ ��������
			pstmt.setString(pidx++, box.getString("p_explain"           ));                     // ������ ��������
			pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"          ));                     // �������(��)
			pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"        ));                     // �������(����)
			pstmt.setString(pidx++, box.getRealFileName("p_file"        ));                     // �������ϸ�
			pstmt.setString(pidx++, box.getNewFileName("p_file"         ));                     // ��������DB��
			pstmt.setString(pidx++, box.getString("p_conturl"           ));                     // �н�����URL
			pstmt.setString(pidx++, box.getString("p_musertel"          ));                     // ���� ��ȭ��ȣ
			pstmt.setInt   (pidx++, box.getInt   ("p_gradftest"         ));                     // �������-������
			pstmt.setInt   (pidx++, box.getInt   ("p_gradhtest"         ));                     // �������-������
			pstmt.setString(pidx++, box.getStringDefault("p_isvisible"          , "Y"   ));     // �н��ڿ��� �����ֱ�
			pstmt.setString(pidx++, box.getString("p_isalledu"          ));                     // ���米������
			pstmt.setString(pidx++, "Y"                                  );                     // ������ο��� (���⼱ �׻� Y)
			pstmt.setString(pidx++, box.getString("p_isintroduction"    ));                     // ����Ұ� ��뿩��
			pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"         ));                     // �н��Ⱓ
			pstmt.setString(pidx++, box.getRealFileName("p_introducefile"   ));                 // ����Ұ� �̹���
			pstmt.setString(pidx++, box.getNewFileName ("p_introducefile"   ));                 // ����Ұ� �̹���
			pstmt.setString(pidx++, box.getRealFileName("p_informationfile" ));                 // ����(����)
			pstmt.setString(pidx++, box.getNewFileName ("p_informationfile" ));                 // ����(����)
			pstmt.setString(pidx++, box.getString      ("p_ischarge"        ));                 // ������ ��/���� ����
			pstmt.setString(pidx++, box.getString      ("p_isopenedu"       ));                 // ���� ���� ����
			pstmt.setString(pidx++, box.getString      ("p_gradftest_flag"   ));                // �������-��(�ʼ�/���ÿ���)
			pstmt.setString(pidx++, box.getString      ("p_gradreport_flag" ));                 // �������-����(�ʼ�/���ÿ���)
			pstmt.setString(pidx++, box.getString("p_isgoyong"));               				// ��뺸�迩��(Y/N)
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_major"));      				// ��뺸�� ȯ�޾�-ȯ�޾�(KT)   
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_minor"));      				// ��뺸�� ȯ�޾�-KT������
			pstmt.setString(pidx++, box.getString("p_isoutsourcing"));							// ��Ź��������(Y/N)
			pstmt.setString(pidx++, box.getString("p_graduatednote"));							// ������� ���
			pstmt.setString(pidx++, box.getString("p_isablereview"));							// ����Ư��(�ű�,��õ,HIT)

			pstmt.setString(pidx++, box.getString("p_subj_gu"));								// ��������(�Ϲ�, JIT:J, ��������:M)
			pstmt.setString(pidx++, box.getString("p_content_cd")); 							// �������ڵ�(Normal�� ��� ����)
			pstmt.setInt   (pidx++, box.getInt   ("p_study_count")); 							// ����Ƚ��(�������)
			pstmt.setInt   (pidx++, box.getInt   ("p_reviewdays")); 							// �������ɱⰣ

			pstmt.setString(pidx++, box.getString("p_sel_dept")); 								// �μ� ��û����
			pstmt.setString(pidx++, box.getString("p_sel_post")); 							 	// ���� ��û����

			pstmt.setString(pidx++, box.getString("p_lev")); 							 		// ��������
			pstmt.setString(pidx++, box.getString("p_gubn")); 							 		// �̼�����
			pstmt.setString(pidx++, box.getString("p_grade")); 							 		// �������
			pstmt.setString(pidx++, box.getString("p_test")); 							 		// ������

			pstmt.setInt   (pidx++, box.getInt   ("p_cp_accrate")); 							// CP������
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_stand")); 						// ��뺸�� ȯ�޾�-CP������
			pstmt.setString(pidx++, box.getString("p_contenttype")); 							// ������Ÿ��
			pstmt.setString(pidx++, box.getString("p_jikmu_limit")); 							// ������û����
			pstmt.setString(pidx++, box.getString("p_cpsubj")); 							    // cp�����ڵ�


			isOk        = pstmt.executeUpdate();

			/*
			// �����ְ��� �����ϰ� �߰��ϴ� ���� �ش� �����ְ��� ���� TZ_PREVIEW�� INSERT�Ѵ�.
			if ( !v_grcode.equals("ALL") ) { 
				box.put("p_grcode",v_grcode);
				box.put("p_subj",v_subj);
				isOk2   = InsertPreview(box);
			}

			// �����ְ� �����ڴ� �ش� �����ְ����� INSERT�ϰ� �Ѵ�.
			if ( StringManager.substring(box.getSession("gadmin"),0,1).equals("H") ) { 
				this.SaveGrSubj(connMgr, v_grcode, v_subj, v_luserid); //isOk3   = 
			}

			// ������ �ϰ�� N000001 (���信��) �����׷����� �ڵ� insert �Ѵ�
			else if ( StringManager.substring(box.getSession("gadmin"),0,1).equals("A") ) { 
				this.SaveGrSubj(connMgr, "N000001", v_subj, v_luserid); //isOk4   = 
			}
			*/

			// ��������  �����Ű�Խ��� Insert : FI
			// �������� �н���Ʈ���� Insert : SN
			// ��������  FAQ Insert : FA

            // 2008.11.30 ����� ����
            // ������ �������� ���ȭ�鿡�� �ߴ� �����׷������ �������� ���, ����ȭ�鿡�� ���� �Է��ϴ°����� ����.
            //isOk2 = RelatedGrcodeInsert(connMgr, box);
			isOk2 = 1;
			
			if ( isOk == 1 && isOk2 != -1) { 
				connMgr.commit();
			} else { 
				connMgr.rollback();
			}
		} catch ( SQLException e ) {
			if ( connMgr != null ) { 
				try { 
					connMgr.rollback();
				} catch ( Exception ex ) { } 
			}

			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			if ( connMgr != null ) { 
				try { 
					connMgr.rollback();
				} catch ( Exception ex ) { } 
			}

			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.setAutoCommit(true);					
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return isOk;
	}     

	/**
	 * �����ڵ� ���� - ��������
	 * @param box      receive from the form object and session
	 * @return isOk    1:update success,0:update fail
	 */
	public int UpdateBookSubject(RequestBox box) throws Exception { 
		DBConnectionManager connMgr                     = null;
		PreparedStatement 	pstmt                       = null;
		ListSet             ls                          = null;
		StringBuffer        sbSQL 		                = new StringBuffer("");
		int 				isOk                        = 0;
		int 				isOk2                       = 0;

		String 				v_luserid 	                = box.getSession("userid");
		// ���� ����
		String              v_oldbookfilenamereal       = "";
		String              v_oldbookfilenamenew        = "";
		String              v_introducefilenamereal     = box.getRealFileName ("p_introducefile"         );
		String              v_introducefilenamenew      = box.getNewFileName  ("p_introducefile"         );
		String              v_informationfilenamereal   = box.getRealFileName ("p_informationfile"       );
		String              v_informationfilenamenew    = box.getNewFileName  ("p_informationfile"       );
		String              v_introducefile0            = box.getStringDefault("p_introducefile0"   , "0");
		String              v_informationfile0          = box.getStringDefault("p_informationfile0" , "0");

		int pidx = 1;

		try { 
			connMgr     = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			sbSQL.append(" select bookfilenamereal, bookfilenamenew from tz_subj where subj = " + StringManager.makeSQL(box.getString("p_subj")) + " \n");

			ls          = connMgr.executeQuery(sbSQL.toString());

			if ( ls.next() ) { 
				v_oldbookfilenamereal 			= ls.getString("bookfilenamereal" );
				v_oldbookfilenamenew 			= ls.getString("bookfilenamenew"  );
			}

			if ( v_introducefilenamereal.length() == 0 ) { 
				if ( v_introducefile0.equals("1") ) { 
					v_introducefilenamereal    		= "";
					v_introducefilenamenew     		= "";
				} else { 
					v_introducefilenamereal    		= box.getString("p_introducefile1");
					v_introducefilenamenew     		= box.getString("p_introducefile2");
				}
			}

			if ( v_informationfilenamereal.length() == 0 ) { 
				if ( v_informationfile0.equals("1") ) { 
					v_informationfilenamereal    	= "";
					v_informationfilenamenew     	= "";
				} else { 
					v_informationfilenamereal    	= box.getString("p_informationfile1");
					v_informationfilenamenew     	= box.getString("p_informationfile2");
				}
			}

			sbSQL.setLength(0);

			// update tz_subj table
			sbSQL.append(" update tz_subj set                              \n")
				.append("         subjnm                  = ?              \n")
				.append("     ,   isonoff                 = ?              \n")
				.append("     ,   subjclass               = ?              \n")
				.append("     ,   upperclass              = ?              \n")
				.append("     ,   middleclass             = ?              \n")
				.append("     ,   lowerclass              = ?              \n")
				.append("     ,   specials                = ?              \n")
				.append("     ,   muserid                 = ?              \n")
				.append("     ,   cuserid                 = ?              \n")
				.append("     ,   isuse                   = ?              \n")
				.append("     ,   ispropose               = ?              \n")
				.append("     ,   biyong                  = ?              \n")
				.append("     ,   edudays                 = ?              \n")
				.append("     ,   studentlimit            = ?              \n")
				.append("     ,   usebook                 = ?              \n")
				.append("     ,   bookprice               = ?              \n")
				.append("     ,   owner                   = ?              \n")
				.append("     ,   producer                = ?              \n")
				.append("     ,   crdate                  = ?              \n")
				.append("     ,   language                = ?              \n")
				.append("     ,   ratewbt                 = ?              \n")
				.append("     ,   ratevod                 = ?              \n")
				.append("     ,   env                     = ?              \n")
				.append("     ,   tutor                   = ?              \n")
				.append("     ,   bookname                = ?              \n")
				.append("     ,   sdesc                   = ?              \n")
				.append("     ,   warndays                = ?              \n")
				.append("     ,   stopdays                = ?              \n")
				.append("     ,   point                   = ?              \n")
				.append("     ,   edulimit                = ?              \n")
				.append("     ,   gradscore               = ?              \n")
				.append("     ,   gradstep                = ?              \n")
				.append("     ,   wstep                   = ?              \n")
				.append("     ,   wmtest                  = ?              \n")
				.append("     ,   wftest                  = ?              \n")
				.append("     ,   wreport                 = ?              \n")
				.append("     ,   wact                    = ?              \n")
				.append("     ,   wetc1                   = ?              \n")
				.append("     ,   wetc2                   = ?              \n")
				.append("     ,   place                   = ?              \n")
				.append("     ,   edumans                 = ?              \n")
				.append("     ,   luserid                 = ?              \n")
				.append("     ,   ldate                   = ?              \n")
				.append("     ,   proposetype             = ?              \n")
				.append("     ,   edutimes                = ?              \n")
				.append("     ,   edutype                 = ?              \n")
				.append("     ,   intro                   = ?              \n")
				.append("     ,   explain                 = ?              \n")
				//.append("     ,   rndjijung               = ?              \n")
				.append("     ,   bookfilenamereal        = ?              \n")
				.append("     ,   bookfilenamenew         = ?              \n")
				.append("     ,   gradexam                = ?              \n")
				.append("     ,   gradreport              = ?              \n")
				//.append("     ,   usesubjseqapproval      = ?              \n")
				//.append("     ,   useproposeapproval      = ?              \n")
				//.append("     ,   usemanagerapproval      = ?              \n")
				//.append("     ,   rndcreditreq            = ?              \n")
				//.append("     ,   rndcreditchoice         = ?              \n")
				//.append("     ,   rndcreditadd            = ?              \n")
				//.append("     ,   rndcreditdeduct         = ?              \n")
				.append("     ,   isessential             = ?              \n")
				.append("     ,   score                   = ?              \n")
				.append("     ,   musertel                = ?              \n")
				.append("     ,   gradftest               = ?              \n")
				.append("     ,   gradhtest               = ?              \n")
				.append("     ,   isvisible               = ?              \n")
				.append("     ,   isalledu                = ?              \n")
				.append("     ,   placejh                 = ?              \n")
				.append("     ,   isintroduction          = ?              \n")
				.append("     ,   eduperiod               = ?              \n")
				.append("     ,   introducefilenamereal   = ?              \n")
				.append("     ,   introducefilenamenew    = ?              \n")
				.append("     ,   informationfilenamereal = ?              \n")
				.append("     ,   informationfilenamenew  = ?              \n")
				.append("     ,   ischarge                = ?              \n")
				.append("     ,   isopenedu               = ?              \n")
				//.append("     ,   maleassignrate          = ?              \n")
				.append("     ,   gradftest_flag          = ?              \n")
				.append("     ,   gradreport_flag         = ?              \n")
				.append("     ,   isgoyong				  = ?			   \n")
				.append(" 	  ,   goyongpricemajor		  = ? 		 	   \n")
				.append(" 	  ,   goyongpriceminor		  = ? 		 	   \n")
				.append("     ,   isoutsourcing           = ?              \n")
				.append("     ,   graduatednote           = ?			   \n")
				.append("     ,   isablereview            = ?			   \n")
				.append("     ,   eduurl                  = ?			   \n")
				.append("     ,   subj_gu                 = ?			   \n")
				.append("     ,   content_cd              = ?			   \n")
				.append("     ,   study_count             = ?			   \n")
				.append("     ,   reviewdays              = ?			   \n")
				.append("     ,   sel_dept                = ?			   \n")
				.append("     ,   sel_post                = ?			   \n")
				.append("     ,   lev                	  = ?			   \n")
				.append("     ,   gubn                	  = ?			   \n")
				.append("     ,   grade                   = ?			   \n")
				.append("     ,   test                	  = ?			   \n")
				.append("     ,   cp_accrate           	  = ?			   \n")
				.append("     ,   goyongpricestand     	  = ?			   \n")
				.append("     ,   jikmu_limit	     	  = ?			   \n")
				.append("     ,   cpsubj	     	      = ?			   \n")
				.append(" where   subj                    = ?              \n");


			pstmt   = connMgr.prepareStatement(sbSQL.toString());


			pstmt.setString(pidx++, box.getString("p_subjnm"        ));
			pstmt.setString(pidx++, box.getString("p_isonoff"       ));
			pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") +  box.getStringDefault("p_lowerclass" ,"000") );
			pstmt.setString(pidx++, box.getString("p_upperclass"    ));
			pstmt.setString(pidx++, box.getString("p_middleclass"   ));
			pstmt.setString(pidx++, box.getStringDefault("p_lowerclass"     , "000"));
			pstmt.setString(pidx++, box.getString("p_specials"      ));
			pstmt.setString(pidx++, box.getString("p_muserid"       ));
			pstmt.setString(pidx++, box.getString("p_cuserid"       ));
			pstmt.setString(pidx++, box.getString("p_isuse"         ));
			pstmt.setString(pidx++, box.getString("p_ispropose"     ));
			pstmt.setInt   (pidx++, box.getInt   ("p_biyong"        ));
			pstmt.setInt   (pidx++, box.getInt   ("p_edudays"       ));
			pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"  ));
			pstmt.setString(pidx++, box.getString("p_usebook"       ));
			pstmt.setInt(pidx++, Integer.parseInt(box.getStringDefault("p_bookprice"      , "0"   )));
			pstmt.setString(pidx++, box.getString("p_owner"         ));
			pstmt.setString(pidx++, box.getString("p_producer"      ));
			pstmt.setString(pidx++, box.getString("p_crdate"        ));
			pstmt.setString(pidx++, box.getString("p_language"      ));
			pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"       ));
			pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"       ));
			pstmt.setString(pidx++, box.getString("p_env"           ));
			pstmt.setString(pidx++, box.getString("p_tutor"         ));
			pstmt.setString(pidx++, box.getStringDefault("p_bookname"       , ""    ));
			pstmt.setString(pidx++, box.getString("p_sdesc"         ));
			pstmt.setInt   (pidx++, box.getInt   ("p_warndays"      ));
			pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"      ));
			pstmt.setInt   (pidx++, box.getInt   ("p_point"         ));
			pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"      ));
			pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"     ));
			pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"      ));
			pstmt.setDouble(pidx++, box.getDouble("p_wstep"         ));
			pstmt.setDouble(pidx++, box.getDouble("p_wmtest"        ));
			pstmt.setDouble(pidx++, box.getDouble("p_wftest"        ));
			pstmt.setDouble(pidx++, box.getDouble("p_wreport"       ));
			pstmt.setDouble(pidx++, box.getDouble("p_wact"          ));
			pstmt.setDouble(pidx++, box.getDouble("p_wetc1"         ));
			pstmt.setDouble(pidx++, box.getDouble("p_wetc2"         ));
			pstmt.setString(pidx++, box.getString("p_place"         ));
			pstmt.setString(pidx++, box.getString("p_edumans"       ));
			pstmt.setString(pidx++, v_luserid                        );
			pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss") );
			pstmt.setString(pidx++, box.getString("p_proposetype"   ));
			pstmt.setInt   (pidx++, box.getInt   ("p_edutimes"      ));
			pstmt.setString(pidx++, box.getString("p_edutype"       ));
			pstmt.setString(pidx++, box.getString("p_intro"         ));
			pstmt.setString(pidx++, box.getString("p_explain"       ));
			//pstmt.setString(pidx++, box.getString("p_rndjijung"     ));

			if ( box.getString("p_deletefile").equals("Y") ) { 
				pstmt.setString(pidx++, ""                           );                         // �������ϸ�
				pstmt.setString(pidx++, ""                           );                         // ��������DB��
			} else {                                                                        
				if ( !box.getRealFileName("p_file").equals("") ) {                          
					pstmt.setString(pidx++, box.getRealFileName("p_file"));                     // �������ϸ�
					pstmt.setString(pidx++, box.getNewFileName ("p_file"));                     // ��������DB��
				} else {                                                                    
					pstmt.setString(pidx++, v_oldbookfilenamereal   );                          // �������ϸ�
					pstmt.setString(pidx++, v_oldbookfilenamenew    );                          // ��������DB��
				}
			}

			pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"          ));                     // �������(��)
			pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"        ));                     // �������(����)
			//pstmt.setString(pidx++, box.getString("p_usesubjseqapproval"));                     // ��������ְ�����(Y/N)
			//pstmt.setString(pidx++, box.getString("p_useproposeapproval"));                     // ������û��������(Y/N)
			//pstmt.setString(pidx++, box.getString("p_usemanagerapproval"));                     // �ְ������������(Y/N)
			//pstmt.setString(pidx++, box.getStringDefault("p_rndcreditreq"       , "0"   ));     // ��������(��������)-�ʼ�
			//pstmt.setString(pidx++, box.getStringDefault("p_rndcreditchoice"    , "0"   ));     // ��������(��������)-����
			//pstmt.setString(pidx++, box.getStringDefault("p_rndcreditadd"       , "0"   ));     // ��������(��������)-��������
			//pstmt.setString(pidx++, box.getStringDefault("p_rndcreditdeduct"    , "0"   ));     // ��������(��������)-��������
			pstmt.setString(pidx++, box.getStringDefault("p_isessential"        , "0"   ));     // ���񱸺�
			pstmt.setDouble(pidx++, box.getDouble("p_score"             ));                     // 
			pstmt.setString(pidx++, box.getString("p_musertel"          ));                     // ���� ��ȭ��ȣ
			pstmt.setInt   (pidx++, box.getInt   ("p_gradftest"         ));                     // �������-������
			pstmt.setInt   (pidx++, box.getInt   ("p_gradhtest"         ));                     // �������-������
			pstmt.setString(pidx++, box.getStringDefault("p_isvisible"          , "N"   ));     // �н��ڿ��� �����ֱ�
			pstmt.setString(pidx++, box.getString("p_isalledu"          ));                     // ���米������
			pstmt.setString(pidx++, box.getString("p_placejh"           ));                     // �������
			pstmt.setString(pidx++, box.getString("p_isintroduction"    ));                     // �н��Ұ� ��뿩��
			pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"         ));                     // �н��Ⱓ
			pstmt.setString(pidx++, v_introducefilenamereal              );                     // ����Ұ� �̹���
			pstmt.setString(pidx++, v_introducefilenamenew               );                     // ����Ұ� �̹���
			pstmt.setString(pidx++, v_informationfilenamereal            );                     // ����(����)
			pstmt.setString(pidx++, v_informationfilenamenew             );                     // ����(����)
			pstmt.setString(pidx++, box.getStringDefault("p_ischarge"           , "N"   ));     // ������ ��/���� ����
			pstmt.setString(pidx++, box.getStringDefault("p_isopenedu"          , "N"   ));     // ���� ���� ���� 
			//pstmt.setInt   (pidx++, box.getInt   ("p_maleassignrate"    ));                     // �������-��(�ʼ�/���ÿ���)
			pstmt.setString(pidx++, box.getString("p_gradftest_flag"    ));                     // �������-����(�ʼ�/���ÿ���)
			pstmt.setString(pidx++, box.getString("p_gradreport_flag"   ));                     // ���� �Ҵ� ����
			pstmt.setString(pidx++, box.getString("p_isgoyong"));               				// ��뺸�迩��(Y/N)
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_major"));      				// ��뺸�� ȯ�޾�-ȯ�޾�(KT)
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_minor"));      				// ��뺸�� ȯ�޾�-KT������
			pstmt.setString(pidx++, box.getString("p_isoutsourcing"));							// ��Ź��������(Y/N)
			pstmt.setString(pidx++, box.getString ("p_graduatednote"));							// ������� ���
			pstmt.setString(pidx++, box.getString("p_isablereview"));							// ����Ư��(�ű�,��õ,HIT)
			pstmt.setString(pidx++, box.getString("p_eduurl"));									//

			pstmt.setString(pidx++, box.getString("p_subj_gu"));								// ��������(�Ϲ�, JIT:J, ��������:M)
			pstmt.setString(pidx++, box.getString("p_content_cd"));								// �������ڵ�(Normal�ϰ�� ����)
			pstmt.setInt   (pidx++, box.getInt   ("p_study_count"));							// ����Ƚ��(�������)
			pstmt.setInt   (pidx++, box.getInt   ("p_reviewdays"));								// �������ɱⰣ

			pstmt.setString(pidx++, box.getString("p_sel_dept"));								// �μ� ��û����
			pstmt.setString(pidx++, box.getString("p_sel_post"));								// ���� ��û����

			pstmt.setString(pidx++, box.getString("p_lev"));									// ��������
			pstmt.setString(pidx++, box.getString("p_gubn"));									// �̼�����
			pstmt.setString(pidx++, box.getString("p_grade"));									// �������
			pstmt.setString(pidx++, box.getString("p_test"));									// ������

			pstmt.setInt   (pidx++, box.getInt   ("p_cp_accrate"));								// CP������
			pstmt.setDouble(pidx++, box.getInt   ("p_goyongprice_stand"));						// ��뺸�� ȯ�޾�-CP������

			pstmt.setString(pidx++, box.getString("p_jikmu_limit"));							// ������û���� ����
			pstmt.setString(pidx++, box.getString("p_cpsubj"));							        // CP�����ڵ�

			pstmt.setString(pidx++, box.getString("p_subj"));									// �����ڵ�

			isOk 	= pstmt.executeUpdate();

			pstmt.close();

			sbSQL.setLength(0);

			// ����� ���� ����� ����
			sbSQL.append(" update tz_subjseq  set subjnm = ? where subj = ? \n");

			pstmt   = connMgr.prepareStatement(sbSQL.toString());

			pstmt.setString(1, box.getString("p_subjnm" ));
			pstmt.setString(2, box.getString("p_subj"   ));

			pstmt.executeUpdate();

            // 2008.11.30 ����� ����
            // ������ �������� ���ȭ�鿡�� �ߴ� �����׷������ �������� ���, ����ȭ�鿡�� ���� �Է��ϴ°����� ����.
            isOk2 = RelatedGrcodeInsert(connMgr, box);
			
			if ( isOk > 0  && isOk2 != -1) { 
				connMgr.commit();
			} else { 
				connMgr.rollback();
			}
		} catch ( SQLException e ) {
			if ( connMgr != null ) { 
				try { 
					connMgr.rollback();
				} catch ( Exception ex ) { } 
			}

			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			if ( connMgr != null ) { 
				try { 
					connMgr.rollback();
				} catch ( Exception ex ) { } 
			}

			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { 
				try { 
					ls.close(); 
				} catch ( Exception e ) { } 
			}

			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.setAutoCommit(true); 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return isOk;
	}       

	/**
	 * ������ ���� ����
	 * @param box          receive from the form object and session
	 * @return ArrayList   ������ ���� ����
	 */
	public ArrayList SelectMonthlyBookinfoList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		ListSet             ls              = null;
		ArrayList           list            = null;
		String 			    sql 			= "";
		DataBox             dbox            = null;

		String 			    ss_bookname     = box.getString("s_bookname");
		String				v_subj			= box.getString("p_subj");
		int					v_month			= box.getInt("p_month");

		String				v_comp			= box.getString("p_comp");

		try { 
			connMgr     = new DBConnectionManager();
			list        = new ArrayList();

			sql = "select bookcode, bookname, author, publisher, price, ldate, indate, stock, relativebookcode		\n"
				+ " , (																			\n"
				+ "		select ( case when count(bookcode) > 0 then 'Y' else 'N' end )			\n"
				+ "		from tz_subjbook														\n"
				+ "		where bookcode = a.bookcode												\n"
				+ "		and subj = " + StringManager.makeSQL(v_subj) + "						\n"
				+ "		and month = " + SQLString.Format(v_month) + "							\n"
				+ ") checkgubun																	\n"		
				+ "from tz_bookinfo	a																\n"
				+ "where 1=1																		\n";  

			if(!v_comp.equals("ALL"))
			sql+= "  and comp = " + SQLString.Format(v_comp);

			if (!ss_bookname.equals(""))
			sql+= "  and (bookname like " + SQLString.Format("%" + ss_bookname + "%") + " or relativebookcode like " + SQLString.Format("%" + ss_bookname + "%") + ") \n";

			sql+= " order by bookname";
			//connMgr = new DBConnectionManager();
			list = new ArrayList();
			ls = connMgr.executeQuery(sql);

			while (ls.next()) {
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		} catch ( Exception e ) { 
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally { 
			if ( ls != null ) { 
				try { 
					ls.close(); 
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch (Exception e ) { } 
			}
		}
		return list;
	}

	/**
	 * ������ �������� ���� 
	 * @param box      receive from the form object and session
	 * @return isOk    1:insert success,0:insert fail
	 */
	public int InsertMonthlyBookInfo(RequestBox box) throws Exception { 
		DBConnectionManager 	connMgr 		= null;
		PreparedStatement 		pstmt 			= null;
		PreparedStatement 		pstmt2 			= null;
		StringBuffer            sbSQL           = new StringBuffer("");
		int                     isOk            = 0;
		int                     isOk2            = 0;
		String sql = "";

		Vector 				v_checks     = new Vector();
		v_checks            = box.getVector("p_checks");
		String 	v_bookcode 		= null;
		String ss_userid = box.getSession("userid");
		String v_subj = box.getString("p_subj");
		int v_month = box.getInt("p_month");

		try { 
			connMgr     = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			sql = "delete from tz_subjbook where subj = ? and month = ?		\n";
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString(1, v_subj);
			pstmt.setInt(2, v_month);
			isOk = pstmt.executeUpdate();

			Enumeration 		em     	= v_checks.elements();

			while ( em.hasMoreElements() ) {
				v_bookcode    = (String)em.nextElement();

				sql = "insert into tz_subjbook(subj, month, bookcode, ldate, luserid) 	\n"
				    + "values (?, ?, ?, to_char(sysdate,'yyyymmddhh24miss'), ?)			\n";

				pstmt = connMgr.prepareStatement(sql);
				pstmt.setString(1, v_subj);
				pstmt.setInt(2, v_month);
				pstmt.setInt(3, Integer.parseInt(v_bookcode));
				pstmt.setString(4, ss_userid);

				isOk = pstmt.executeUpdate();

				if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			}

			sql = "update tz_subj set edudays = ? where subj  = ?	\n";

			pstmt2 = connMgr.prepareStatement(sql);
			pstmt2.setInt(1, v_month);
			pstmt2.setString(2, v_subj);
			isOk2 = pstmt2.executeUpdate();
			if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }

			if(isOk > 0 && isOk2 > 0) {
				connMgr.commit();
			} else {
				connMgr.rollback();
			}
		} catch ( SQLException e ) {
			if ( connMgr != null ) { 
				try { 
					connMgr.rollback();
				} catch ( Exception ex ) { } 
			}

			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			if ( connMgr != null ) { 
				try { 
					connMgr.rollback();
				} catch ( Exception ex ) { } 
			}

			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
		}

		return isOk;
	}

	/**
	 * �������� ���� ���ȸ�� ��������
	 * @param box          receive from the form object and session
	 * @return ArrayList   
	 */      
	public ArrayList SelectBookComp(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		DataBox dbox = null;

		try {
			sql = "select a.comp, b.compnm			\n"
				+ "from   tz_bookinfo a				\n"
				+ "	   , (select comp, compnm		\n"
				+ "		  from   tz_compclass		\n"
				+ "		  union						\n"
				+ "		  select cpseq, cpnm		\n"
				+ "		  from   tz_cpinfo			\n"
				+ "      ) b						\n"
				+ "where a.comp = b.comp			\n"
				+ "group by a.comp, b.compnm		\n"
				+ "order by b.compnm                \n"; 

			connMgr = new DBConnectionManager();
			list = new ArrayList();
			ls = connMgr.executeQuery(sql);

			while (ls.next()) {
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		}            
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list;
	}

	/**
	 * ������ ���� ��ü ����
	 * @param box          receive from the form object and session
	 * @return ArrayList   ������ ���� ����
	 */
	public ArrayList SelectMonthlyBookinfoAllList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		ListSet             ls              = null;
		ArrayList           list            = null;
		String 			   sql 			   = "";
		DataBox             dbox            = null;

		String ss_subjnm = box.getString("s_subjnm");
		//String v_subj = box.getString("p_subj");
		//int v_month = box.getInt("p_month");

		String v_comp = box.getString("p_comp");
		String ss_monthly = box.getString("s_monthly");

		try { 
			connMgr     = new DBConnectionManager();
			list        = new ArrayList();

			sql = " select a.subj, c.subjnm, a.month, b.relativebookcode, b.bookname		\n"
				+ " from   tz_subjbook a, tz_bookinfo b, tz_subj c							\n"
				+ " where  a.bookcode = b.bookcode											\n"
				+ " and    a.subj = c.subj													\n"
				+ " and    1=1                                                              \n";

			if(!v_comp.equals("ALL"))
			sql+= " and    comp = " + SQLString.Format(v_comp);

			if (!ss_subjnm.equals(""))
			sql+= " and    (c.subjnm like " + SQLString.Format("%" + ss_subjnm + "%") + " or c.subj like " + SQLString.Format("%" + ss_subjnm + "%") + ") \n";

			if (!ss_subjnm.equals(""))
			sql+= " and    (a.month like " + SQLString.Format("%" + ss_monthly + "%") + ") \n";

			sql+= " order  by subjnm, a.month, b.bookname";
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			ls = connMgr.executeQuery(sql);

			while (ls.next()) {
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		} catch ( Exception e ) { 
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally { 
			if ( ls != null ) { 
				try { 
					ls.close(); 
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch (Exception e ) { } 
			}
		}
		return list;
	}
	


    
    /**
     * �����ڵ� �ߺ�Ȯ��
     * @param box      receive from the form object and session
     * @return String    1:insert success,0:insert fail
     */
    public boolean checkDupSubj(String v_subj) throws Exception {
    	DBConnectionManager connMgr			= null;
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        boolean				isOk			= false;
        
        try { 
        	connMgr     = new DBConnectionManager();
            sbSQL.append(" select subj     \n")
                 .append(" from   tz_subj  \n")
                 .append(" where  subj = " + StringManager.makeSQL(v_subj));

            ls = connMgr.executeQuery(sbSQL.toString());
       
            if (ls.next()) {
            	isOk = false;
            } else {
            	isOk = true;
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch (Exception e ) { } 
			}
        }
        
        return isOk;
    }


    /**
     * ������ ���� ����Ʈ
     * @param box
     * @return
     * @throws Exception 
     */
	public ArrayList SelectSubjectJikmuList(RequestBox box) throws Exception {
		DBConnectionManager connMgr         = null;
		ListSet             ls              = null;
		ArrayList           list            = null;
		String 			   sql 			   = "";
		DataBox             dbox            = null;

		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_jikmu_year = box.getStringDefault("p_jikmuyear",v_year);

		try { 
			connMgr     = new DBConnectionManager();
			list        = new ArrayList();

			sql = "\n select a.job_cd, b.jobnm "
				+ "\n from   tz_subjjikmu a, tz_jikmu b "
				+ "\n where  a.job_cd =  b.job_cd "
				+ "\n and    b.isdeleted = 'N' "
				+ "\n and    a.year = " + SQLString.Format(v_jikmu_year)
				+ "\n and    a.subj = " + SQLString.Format(v_subj)
				+ "\n order  by b.job_cd ";
			ls = connMgr.executeQuery(sql);

			while (ls.next()) {
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		} catch ( Exception e ) { 
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally { 
			if ( ls != null ) { 
				try { 
					ls.close(); 
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch (Exception e ) { } 
			}
		}
		return list;
	}


	/**
	 * ������ ���� ����
	 * @param box
	 * @return
	 * @throws Exception 
	 */
	public int InsertSubjJikmu(RequestBox box) throws Exception {
		
		DBConnectionManager 	connMgr 		= null;
		PreparedStatement 		pstmt 			= null;
		PreparedStatement 		pstmt2 			= null;
		int                     isOk            = 0;
		int                     isOk2           = 0;
		String sql = "";
		String sql2 = "";

		String ss_userid = box.getSession("userid");

		Vector v_jimus = new Vector();
		v_jimus = box.getVector("p_jikmu");
		
		String v_subj      = box.getString("p_subj");
		String v_jikmuyear = box.getString("p_jikmuyear");
		String v_jikmu     = "";

		try { 
			connMgr     = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			sql = "\n delete from tz_subjjikmu "
				+ "\n where  year = " + SQLString.Format(v_jikmuyear)
				+ "\n and    subj = " + SQLString.Format(v_subj);

			pstmt = connMgr.prepareStatement(sql);
			isOk = pstmt.executeUpdate();

			Enumeration em = v_jimus.elements();

			while ( em.hasMoreElements() ) {
				v_jikmu    = (String)em.nextElement();

				sql2 = "insert into tz_subjjikmu(year, subj, job_cd, ldate, luserid) \n"
				     + "values (?, ?, ?, to_char(sysdate,'yyyymmddhh24miss'), ?) \n";

				pstmt2 = connMgr.prepareStatement(sql2);
				pstmt2.setString(1, v_jikmuyear);
				pstmt2.setString(2, v_subj);
				pstmt2.setString(3, v_jikmu);
				pstmt2.setString(4, ss_userid);

				isOk = pstmt2.executeUpdate();

				if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
			}

			if(isOk > 0) {
				connMgr.commit();
			} else {
				connMgr.rollback();
			}
		} catch ( SQLException e ) {
			if ( connMgr != null ) { 
				try { 
					connMgr.rollback();
				} catch ( Exception ex ) { } 
			}

			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			if ( connMgr != null ) { 
				try { 
					connMgr.rollback();
				} catch ( Exception ex ) { } 
			}

			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
				} catch ( Exception e ) { } 
			}
			if ( pstmt2 != null ) { 
				try { 
					pstmt2.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
		}

		return isOk;
		
	}
	
    /**
     * ���������� TZ_BDS�� ���
     * @param connmgr, v_subj      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     */
    public int InsertContInfo(DBConnectionManager connMgr,String v_subj) throws Exception { 
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;
        
        ListSet             ls          = null;
        String              v_luserid   = "SYSTEM";
        int                 v_tabseq    = 0;
       
        try { 
            // insert tz_bds table
            sbSQL.append(" insert into tz_subjcontinfo                      \n")
                 .append(" ( subj    ,cont_yn   ,ldate                      \n")
                 .append(" ) values (                                       \n")
                 .append("   ?       ,'N'      ,to_char(sysdate,'yyyymmddhh24mmss')) \n");
            
            pstmt           = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setString( 1, v_subj    );

            isOk = pstmt.executeUpdate();
        } catch ( Exception e ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e1 ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return isOk;
    }
}