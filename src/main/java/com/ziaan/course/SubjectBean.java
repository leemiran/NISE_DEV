// **********************************************************
//  1. 제      목: 과목OPERATION BEAN
//  2. 프로그램명: SubjectBean.java
//  3. 개      요: 과목OPERATION BEAN
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 노희성 2004. 11. 14
//  7. 수      정: 정기현 2006. 06. 01
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
     * @return ArrayList   과정데이터
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
     * 과정 리스트 조회
     * @param box          receive from the form object and session
     * @return ArrayList   과목리스트
     */
    public ArrayList SelectSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        SubjectData         data            = null;

        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // 교육주관
        String              ss_upperclass   = box.getStringDefault("s_upperclass"   , "ALL");   // 과목분류
        String              ss_middleclass  = box.getStringDefault("s_middleclass"  , "ALL");   // 과목분류
        String              ss_lowerclass   = box.getStringDefault("s_lowerclass"   , "ALL");   // 과목분류
        //String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // 과목&코스
        String              v_searchtext    = box.getString       ("p_searchtext"          );
                            
        String              v_orderColumn   = box.getString       ("p_orderColumn"         );   // 정렬할 컬럼명
        String              v_orderType     = box.getString       ("p_orderType"           );   // 정렬할 순서

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

            // 교육구분별 조회
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
     * 과정 리스트 조회
     * @param box          receive from the form object and session
     * @return ArrayList   과목리스트
     */
    public ArrayList SelectSubjectListForExcel(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        SubjectData         data            = null;

        String ss_grcode 		= box.getStringDefault("s_grcode", "ALL");   // 교육그룹
        String ss_upperclass   	= box.getStringDefault("s_upperclass"   , "ALL");   // 과목분류
        String ss_middleclass  	= box.getStringDefault("s_middleclass"  , "ALL");   // 과목분류
        String ss_lowerclass   	= box.getStringDefault("s_lowerclass"   , "ALL");   // 과목분류
        String v_searchtext    	= box.getString       ("p_searchtext");
        
        String v_searchGu1 = box.getString("p_searchGu1");
        String v_searchGu2 = box.getString("p_searchGu2");
        
        String sql = "";

        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            /**
             * 교육형태	교육부문	교육분야	과정코드	과정명	학습일차	학습시간	이수제	
             * 소유회사	제작사	수준	신고시간	차시	 등급 	 지원금 	 환급금 	과정심사일	 
             * 관련도서 	교육대상	교육목표	학습목차	컨텐츠Type	구분	컨텐츠형태	컨텐츠코드	
             * 도입	최종변경	신규/보완/지속
             */
            if ( ss_grcode.equals("ALL") ) { 
            	sql = "\n select get_codenm('"+ONOFF_GUBUN+"',a.isonoff) as isonoff  /* 교육구분 */ "
            		+ "\n      , get_subjclassnm(a.upperclass,'000','000') as upperclassnm   /* 과정분류 대 */ "
            		+ "\n      , get_subjclassnm(a.upperclass,middleclass,'000') as middleclassnm    /* 과정분류 중 */ "
            		+ "\n      , get_subjclassnm(a.upperclass,lowerclass,'000') as lowerclassnm    /* 과정분류 소 */ "            		
            		+ "\n      , a.subj 	 /* 과정코드 */ "
            		+ "\n      , a.subjnm    /* 과정명 */ "
            		+ "\n      , a.edudays   /* 학습일차 */ "
            		+ "\n      , a.edutimes  /* 학습시간 */ "
             		+ "\n      , a.owner    /* 소유회사 */ "
                    + "\n      , nvl(( "
                    + "\n             select cpnm "
                    + "\n             from   tz_cpinfo "
                    + "\n             where  cpseq = a.owner "
                    + "\n            ), ( "
                    + "\n                 select compnm "
                    + "\n                 from   tz_compclass "
                    + "\n                 where  comp = a.owner "
                    + "\n                 )) as ownernm /* 소유회사 */ "
            		+ "\n      , a.producer    /* 제작사 */ "
                    + "\n      , nvl(( "
                    + "\n             select cpnm "
                    + "\n             from   tz_cpinfo "
                    + "\n             where  cpseq = a.producer "
                    + "\n            ), ( "
                    + "\n                select  compnm "
                    + "\n                from    tz_compclass "
                    + "\n                where   comp = a.producer "
                    + "\n               )) as producernm /* 제작사 */ "
            		+ "\n      , get_codenm_cb('013',a.lev) as lev /* 교육수준 */ "            		
            		+ "\n      , a.edutimes /* 교육시간 */ "
            		+ "\n      , (select count(*) from tz_subjlesson where subj = a.subj) as lessoncnt  /* 차시 */ "
            		+ "\n      , a.contentgrade /* 컨텐츠등급 */ "
            		+ "\n      , a.isgoyong /* 고용보험여부 */ "
            		+ "\n      , a.goyongpriceminor   /* 우선지원대상금 */ "
            		+ "\n      , a.goyongpricemajor   /* 대기업 */ "
            		+ "\n      , a.judgedate    /* 과정심사일 */ "
            		+ "\n      , decode(a.usebook,'N','무','유') as usebook  /* 관련도서 */ "
            		+ "\n      , a.edumans  /* 교육대상 */ "
            		+ "\n      , a.intro   /* 교육목표 */ "
            		+ "\n      , a.explain   /* 학습목차 */ "
            		+ "\n      , 'WBT(' || a.ratewbt || '%) / VOD(' || a.ratevod || '%)' as conttentkind  /* 컨텐츠Type */ "
            		+ "\n      , get_codenm('0106',a.getmethod) as getmethod    /* 구분 */ "
            		+ "\n      , get_codenm('0007',a.contenttype) as contenttype  /* 컨텐츠형태 */ "
            		+ "\n      , a.content_cd   /* 컨텐츠코드 */ "
            		+ "\n      , a.firstdate    /* 도입 */ "
            		+ "\n      , a.getdate  /* 최종변경 */ "
            		+ "\n      , '' as contentstatus  /* 신규/보완/지속 */ "
            		+ "\n      , a.isuse  /* 사용여부 */ "
            		+ "\n      , a.biyong  "
            		+ "\n from   tz_subj a "
            		+ "\n where  1=1 ";
            } else { 
            	
            	sql = "\n select get_codenm('"+ONOFF_GUBUN+"',a.isonoff) as isonoff  /* 교육형태 */ "
            		+ "\n      , get_subjclassnm(a.upperclass,'000','000') as upperclassnm   /* 교육부문 */ "
            		+ "\n      , get_subjclassnm(a.upperclass,middleclass,'000') as middleclassnm    /* 교육분야 */ "
            		+ "\n      , get_subjclassnm(a.upperclass,lowerclass,'000') as lowerclassnm    /* 과정분류 소 */ "  
            		+ "\n      , a.subj /* 과정코드 */ "
            		+ "\n      , a.subjnm    /* 과정명 */ "
            		+ "\n      , a.edudays   /* 학습일차 */ "
            		+ "\n      , a.edutimes  /* 학습시간 */ "
            		+ "\n      , a.owner    /* 소유회사 */ "
                    + "\n      , nvl(( "
                    + "\n             select cpnm "
                    + "\n             from   tz_cpinfo "
                    + "\n             where  cpseq = a.owner "
                    + "\n            ), ( "
                    + "\n                 select compnm "
                    + "\n                 from   tz_compclass "
                    + "\n                 where  comp = a.owner "
                    + "\n                 )) as ownernm /* 소유회사 */ "
            		+ "\n      , a.producer    /* 제작사 */ "
                    + "\n      , nvl(( "
                    + "\n             select cpnm "
                    + "\n             from   tz_cpinfo "
                    + "\n             where  cpseq = a.producer "
                    + "\n            ), ( "
                    + "\n                select  compnm "
                    + "\n                from    tz_compclass "
                    + "\n                where   comp = a.producer "
                    + "\n               )) as producernm /* 제작사 */ "
            		+ "\n      , get_codenm_cb('013',a.lev) as lev /* 교육수준 */ "
            		+ "\n      , a.edutimes /* 교육시간 */ "
            		+ "\n      , (select count(*) from tz_subjlesson where subj = a.subj) as lessoncnt  /* 차시 */ "
            		+ "\n      , a.contentgrade /* 역량등급 */ "
            		+ "\n      , a.isgoyong /* 고용보험여부 */ "
            		+ "\n      , a.goyongpriceminor   /* 우선지원대상금 */ "
            		+ "\n      , a.goyongpricemajor   /* 환급금 */ "
            		+ "\n      , a.judgedate    /* 과정심사일 */ "
            		+ "\n      , decode(a.usebook,'N','무','유') as usebook  /* 관련도서 */ "
            		+ "\n      , a.edumans  /* 교육대상 */ "
            		+ "\n      , a.intro   /* 교육목표 */ "
            		+ "\n      , a.explain   /* 학습목차 */ "
            		+ "\n      , 'WBT(' || a.ratewbt || '%) / VOD(' || a.ratevod || '%)' as conttentkind  /* 컨텐츠Type */ "
            		+ "\n      , get_codenm('0106',a.getmethod) as getmethod    /* 구분 */ "
            		+ "\n      , get_codenm('0007',a.contenttype) as contenttype  /* 컨텐츠형태 */ "
            		+ "\n      , a.content_cd   /* 컨텐츠코드 */ "
            		+ "\n      , a.firstdate    /* 도입 */ "
            		+ "\n      , a.getdate  /* 최종변경 */ "
            		+ "\n      , '' as contentstatus  /* 신규/보완/지속 */ "
            		+ "\n      , a.isuse  /* 사용여부 */ "
            		+ "\n      , a.biyong  "
            		+ "\n from   tz_subj a "
            		+ "\n      , tz_grsubj b "
            		+ "\n where  a.subj = b.subjcourse "
            		+ "\n and    b.grcode = " + StringManager.makeSQL(ss_grcode);
            }

            // 교육구분별 조회
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
     * 과정 상세조회
     * @param box          receive from the form object and session
     * @return ArrayList   과목데이타
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
                data.setGradexam                (ls.getInt   ("gradexam"       ));// 수료기준-중간평가
                data.setGradftest               (ls.getInt   ("gradftest"      ));// 수료기준-최종평가
                data.setGradhtest               (ls.getInt   ("gradhtest"      ));// 수료기준-형성평가
                


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
     * 과정코드 등록 - 이러닝
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
        String v_grcode   	= box.getString ("s_grcode"     ); // 과목리스트에서의 SELECTBOX 교육주관 코드
        String v_dir 		= "";                              // v_dir은 p_contenttype이 Normal,OBC,SCORM방식일때 dp/content/과목코드를 넣어준다.
        String v_contenttype= box.getString ("p_contenttype");
        
        int pidx = 1;
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // KT디렉토리 링크생성시에 암호화
            KTUtil ktUtil = KTUtil.getInstance();
            
            String v_subj      	= getMaxSubjcode(connMgr, box.getString("p_upperclass"), box.getString("p_middleclass") );            
            String v_content_cd = v_subj;
            
            ConfigSet conf = new ConfigSet();
            String contentPath = conf.getProperty("dir.content.path");
            //String ktContentPath = conf.getProperty("dir.kt_content.path");
            String contentUploadPath = conf.getProperty("dir.content.upload");
            //String ktContentUploadPath = conf.getProperty("dir.kt_content.upload");
            //String ktContentLink = conf.getProperty("dir.kt_content.link");
            
            // 디렉토리경로는 컨텐츠타입에 따라 상이하다.
            // 컨테츠타입이 N(Normal), O(OBC), OA(OBC-Author), S(SCORM) 일때
            if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") || v_contenttype.equals("S") ) {
            	v_dir = contentPath + v_subj;
            	v_content_cd = v_subj; // 해당 컨텐츠타입의 컨텐츠코드는 과정코드와 동일하다. 
            } 
            // 그 이외의 컨텐츠타입 L(Link) 등등 일때
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

            pstmt.setString(pidx++, v_subj                           );                 // 과목코드
            pstmt.setString(pidx++, box.getString("p_subjnm"        ));                 // 과목명
            pstmt.setString(pidx++, box.getString("p_isonoff"       ));                 // 온라인/오프라인구분
            pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") +  box.getStringDefault("p_lowerclass","000") ); // 분류코드
            pstmt.setString(pidx++, box.getString("p_upperclass"    ));                 // 대분류
            pstmt.setString(pidx++, box.getString("p_middleclass"   ));                 // 중분류
            pstmt.setString(pidx++, box.getStringDefault("p_lowerclass" ,"000") );      // 소분류
            pstmt.setString(pidx++, box.getString("p_specials"      ));                 // 과목특성(YYY)
            pstmt.setString(pidx++, box.getString("p_muserid"       ));                 // 운영담당 ID
            pstmt.setString(pidx++, box.getString("p_cuserid"       ));                 // 컨텐츠담당 ID
            pstmt.setString(pidx++, box.getString("p_isuse"         ));                 // 사용여부(Y/N)
            pstmt.setString(pidx++, box.getStringDefault("p_ispropose"  ,"Y"    ));     // [X]수강신청여부(Y/N)
            
            pstmt.setInt   (pidx++, box.getInt   ("p_biyong"        ));                 // 수강료
            pstmt.setInt   (pidx++, box.getInt   ("p_edudays"       ));                 // 학습일차
            pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"  ));                 // 정원
            pstmt.setString(pidx++, box.getString("p_usebook"       ));                 // 교재사용여부
            pstmt.setString(pidx++, box.getStringDefault("p_bookprice"  , "0"   ));     // 교재비
            pstmt.setString(pidx++, box.getString("p_owner"         ));                 // 과목소유자
            pstmt.setString(pidx++, box.getSession("userid"      ));                 // 과목제공자
            //pstmt.setString(pidx++, box.getString("p_producer"      ));                 // 과목제공자
            pstmt.setString(pidx++, box.getString("p_eduurl"        ));                 // 교육URL
            pstmt.setString(pidx++, box.getString("p_crdate"        ));                 // 제작일자
            pstmt.setString(pidx++, box.getString("p_language"      ));                 // [X]언어선택
            pstmt.setString(pidx++, box.getString("p_server"        ));                 // [X]서버
            pstmt.setString(pidx++, v_dir                            );                 // 컨텐츠경로
            
            pstmt.setString(pidx++, box.getString("p_vodurl"        ));                 // VOD경로
            pstmt.setString(pidx++, box.getString("p_preurl"        ));                 // 맛보기URL
            pstmt.setString(pidx++, box.getString("p_conturl"       ));                 // 학습목차URL
            pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"       ));                 // 학습방법(WBT%)
            pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"       ));                 // 학습방법(VOD%)
            pstmt.setString(pidx++, box.getString("p_env"           ));                 // 학습환경
            pstmt.setString(pidx++, box.getString("p_tutor"         ));                 // 강사설명
            pstmt.setString(pidx++, box.getStringDefault("p_bookname"   , ""));         // 교재명
            pstmt.setString(pidx++, box.getString("p_sdesc"         ));                 // [X]비고
            pstmt.setInt   (pidx++, box.getInt   ("p_warndays"      ));                 // [X]학습경고적용일
            pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"      ));                 // [X]학습중지적용일
            pstmt.setInt   (pidx++, box.getInt   ("p_point"         ));                 // 수료점수
            
            pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"      ));                 // 1일최대학습량
            pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"     ));                 // 수료기준-점수
            pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"      ));                 // 수료기준-진도율
            pstmt.setDouble(pidx++, box.getDouble("p_wstep"         ));                 // 가중치-진도율
            pstmt.setDouble(pidx++, box.getDouble("p_wmtest"        ));                 // 가중치-중간평가
            pstmt.setDouble(pidx++, box.getDouble("p_wftest"        ));                 // 가중치-최종평가
            pstmt.setDouble(pidx++, box.getDouble("p_wreport"       ));                 // 가중치-과제
            pstmt.setDouble(pidx++, box.getDouble("p_wact"          ));                 // 가중치-액티비티
            pstmt.setDouble(pidx++, box.getDouble("p_wetc1"         ));                 // 가중치-참여도
            pstmt.setDouble(pidx++, box.getDouble("p_wetc2"         ));                 // 가중치-교수가점
            pstmt.setString(pidx++, v_luserid                        );                 // 생성자
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));              // 생성일
            
            pstmt.setString(pidx++, v_luserid);              							// 최종수정자
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));              // 최종수정일(ldate)
            pstmt.setString(pidx++, box.getString("p_proposetype"));                 	// 수강신청구분
            pstmt.setString(pidx++, box.getStringDefault("p_edumans", "" ));    		// 맛보기 교육대상
            pstmt.setString(pidx++, box.getStringDefault("p_intro", "" ));    			// 맛보기 교육목적
            pstmt.setString(pidx++, box.getStringDefault("p_explain", "" ));    		// 맛보기 교육내용
            pstmt.setString(pidx++, box.getStringDefault("p_isessential", "0"));    	// 과목구분
            pstmt.setInt   (pidx++, box.getInt   ("p_score"));                 			// 학점배점
            pstmt.setString(pidx++, v_contenttype);                 					// 컨텐츠타입
            pstmt.setString(pidx++, box.getString("p_gradexam"));        				// 수료기준(평가)
            pstmt.setInt   (pidx++, box.getInt	 ("p_gradreport"));        			 	// 수료기준(과제)
            pstmt.setString(pidx++, box.getString("p_whtest"));         				// 가중치(형성평가)
            pstmt.setString(pidx++, box.getString("p_isoutsourcing"));         			// 위탁교육여부
            pstmt.setString(pidx++, box.getString("p_isablereview"));					// 복습가능여부
            pstmt.setString(pidx++, box.getString("p_musertel"));						// 운영담당 전화번호
            pstmt.setInt   (pidx++, box.getInt	 ("p_gradftest"));						// 수료기준-최종평가
            pstmt.setInt   (pidx++, box.getInt	 ("p_gradhtest"));						// 수료기준-형성평가
            pstmt.setString(pidx++, box.getStringDefault("p_isvisible", "Y"));			// 학습자에게 보여주기
            pstmt.setString(pidx++, box.getString("p_isalledu"));						// 전사교육여부
            pstmt.setInt   (pidx++, box.getInt   ("p_edutimes"));						// 학습시간
            pstmt.setString(pidx++, "Y");         										// 과목승인여부 (여기선 항상 Y)
            pstmt.setString(pidx++, box.getString("p_isintroduction"));					// 과목소개 사용여부
            pstmt.setInt   (pidx++, box.getInt("p_eduperiod"));							// 학습기간
            pstmt.setString(pidx++, box.getRealFileName("p_introducefile"));			// 과목소개 이미지
            pstmt.setString(pidx++, box.getNewFileName ("p_introducefile"));			// 과목소개 이미지
            pstmt.setString(pidx++, box.getRealFileName("p_informationfile"));			// 파일(목차)
            pstmt.setString(pidx++, box.getNewFileName ("p_informationfile"));			// 파일(목차)
            pstmt.setString(pidx++, box.getString ("p_contentgrade"));					// 컨텐츠등급
            pstmt.setString(pidx++, box.getString ("p_memo"));							// 비고
            pstmt.setString(pidx++, box.getString ("p_ischarge"));						// 수강료 유/무료 여부
            pstmt.setString(pidx++, box.getString ("p_isopenedu"));						// 열린교육 여부
            pstmt.setString(pidx++, box.getString ("p_gradftest_flag"));				// 수료기준-최종평가(필수/선택여부)
            pstmt.setString(pidx++, box.getString ("p_gradexam_flag"));					// 수료기준-중간평가(필수/선택여부)
            pstmt.setString(pidx++, box.getString ("p_gradhtest_flag"));				// 수료기준-형성평가(필수/선택여부)
            pstmt.setString(pidx++, box.getString ("p_gradreport_flag"));				// 수료기준-과제      (필수/선택여부)
            pstmt.setString(pidx++, box.getString ("p_isgoyong"));               		// 고용보험여부(Y/N)
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_major"));      		// 고용보험 환급액-대기업
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_minor"));      		// 고용보험 환급액-우선대상

            pstmt.setString(pidx++, box.getString ("p_subj_gu"));      					// 과정종류(일반, JIT:J, 안전교육:M)
            pstmt.setString(pidx++, box.getString ("p_content_cd"));      				// 컨텐츠 코드(Normal과정일 경우 매핑)
            pstmt.setInt   (pidx++, box.getInt    ("p_study_count"));    		  		// 수료기준-접속횟수
            pstmt.setInt   (pidx++, box.getInt    ("p_reviewdays"));    		  		// 복습가능기간
            pstmt.setString(pidx++, box.getString ("p_lev"));    		  				// 교육수준
            pstmt.setInt   (pidx++, box.getInt    ("p_study_time"));    		  		// 수료기준-시간
            pstmt.setInt   (pidx++, box.getInt    ("p_cp_accrate"));    		  		// CP정산율

            pstmt.setString(pidx++, box.getString ("p_getmethod"));    		  			// 컨텐츠확보-방법
            pstmt.setString(pidx++, box.getString ("p_cp"));    		  				// 컨텐츠확보-CP
            pstmt.setString(pidx++, box.getString ("p_firstdate"));    		  			// 보유년도-최초확보
            pstmt.setString(pidx++, box.getString ("p_judgedate"));    		  			// 보유년도-심사연월
            pstmt.setString(pidx++, box.getString ("p_getdate"));    		  			// 보유년도-최종변경
            pstmt.setInt(pidx++, box.getInt ("p_cp_account"));    		  		// CP정산금액           
            pstmt.setInt(pidx++, box.getInt ("p_cp_vat"));    		  		// CP VAT                       
            pstmt.setString(pidx++, box.getString ("p_contentprogress"));    		  		// 진도체크(랜덤Y/순차N)                       
            pstmt.setString(pidx++, box.getString ("p_cpsubj"));    		  		        //CP과정코드                   
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            // 2008.11.30 김미향 수정
            // 기존에 과정개설 목록화면에서 했던 교육그룹맵핑을 과정개설 등록, 수정화면에서 같이 입력하는것으로 변경.
            //isOk2 = RelatedGrcodeInsert(connMgr, box);
            isOk2	=	1;

            // 과정기수별로 게시판 생성하므로 주석처리함.
            // 과정별자료실 Insert
            //isOk3 = this.InsertBds(connMgr,v_subj,"SD", "과정별자료실", "ALL", "ALL");

            // 과정별질문방 Insert
            //isOk4 = this.InsertBds(connMgr,v_subj,"SQ", "과정별질문방", "ALL", "ALL"); 

            // 과정별 게시판 Insert
            //isOk5 = this.InsertBds(connMgr,v_subj,"BD", "게시판", "ALL", "ALL"); 
            
            //이어보기 관련정보테이블 Insert
            isOk5 = this.InsertContInfo(connMgr,v_subj);
            
            // GRSUBJ 에 넣기
            SaveGrSubj(connMgr, v_grcode, v_subj, box.getSession("userid"));
            
            if ( isOk == 1) {
                if (isOk2 != -1) {
	
	                // 디렉토리경로는 컨텐츠타입에 따라 상이하다.
	                // 컨테츠타입이 N(Normal), O(OBC), OA(OBC-Author), S(SCORM) 일때
	                if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") || v_contenttype.equals("S") ) {
	                    ktUtil.createDirectory( contentUploadPath, v_content_cd );
	                } 
                } else {
                	// 교육그룹 생성시 에러가 났을 경우
                	isOk = -2;
                }
                
            } else {
            	// 과정개설이 안된경우 에러
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
     * 교육그룹 연결
     * @param DBConnectionManager DB Connection Manager
     * @param v_grcode            교육그룹
     * @param v_subj              과목코드
     * @param v_luserid           수정자
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
     * 신규 과정코드 등록
     * @param box      receive from the form object and session
     * @return String    1:insert success,0:insert fail
     */
    public String getMaxSubjcode(DBConnectionManager connMgr, String v_upperclass, String v_middleclass) throws Exception {
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        String 	            v_subjcode	     = "";

        // PRF + 년도 2자리 + 연번 4 자리
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
     * 과정등록 - 집합
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
            
            pstmt.setString(pidx++, v_subj								 );                     // 과목코드
            pstmt.setString(pidx++, box.getString("p_subjnm"            ));                     // 과목명
            pstmt.setString(pidx++, box.getString("p_isonoff"           ));                     // 온라인/오프라인구분
            pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000") ); // 분류코드
            pstmt.setString(pidx++, box.getString("p_upperclass"        ));                     // 대분류
            pstmt.setString(pidx++, box.getString("p_middleclass"       ));                     // 중분류
            pstmt.setString(pidx++, box.getStringDefault("p_lowerclass","000"));     			// 소분류
            pstmt.setString(pidx++, box.getString("p_specials"          ));                     // 과목특성
            pstmt.setString(pidx++, box.getString("p_muserid"           ));                     // 운영담당ID
            pstmt.setString(pidx++, box.getString("p_cuserid"           ));                     // [X]컨텐츠담당ID
            pstmt.setString(pidx++, box.getString("p_isuse"             ));                     // 사용여부
            pstmt.setString(pidx++, box.getString("p_ispropose"         ));                     // [X]수강신청여부(Y/N)
            pstmt.setInt   (pidx++, box.getInt   ("p_biyong"            ));                     // 수강료
            pstmt.setInt   (pidx++, box.getInt   ("p_edudays"           ));                     // 교육기간(일)
            pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"      ));                     // 기수당정원
            pstmt.setString(pidx++, box.getString("p_usebook"           ));                     // [X]교재사용여부
            pstmt.setInt   (pidx++, Integer.parseInt(box.getStringDefault("p_bookprice", "0")));// [X]교재비
            pstmt.setString(pidx++, box.getString("p_owner"             ));                     // [X]과목소유자
            pstmt.setString(pidx++, box.getString("p_producer"          ));                     // [X]과목제공자
            pstmt.setString(pidx++, box.getString("p_crdate"            ));                     // [X]제작일자
            pstmt.setString(pidx++, box.getString("p_language"          ));                     // [X]언어선택
            pstmt.setString(pidx++, box.getString("p_server"            ));                     // [X]서버
            pstmt.setString(pidx++, box.getString("p_dir"               ));                     // [X]컨텐츠경로
            pstmt.setString(pidx++, box.getString("p_eduurl"            ));                     // [X]교육URL
            pstmt.setString(pidx++, box.getString("p_vodurl"            ));                     // [X]VOD경로
            pstmt.setString(pidx++, box.getString("p_preurl"            ));                     // 맛보기URL
            pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"           ));                     // [X]학습방법(WBT%)
            pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"           ));                     // [X]학습방법(VOD%)
            pstmt.setString(pidx++, box.getString("p_env"               ));                     // [X]학습환경
            pstmt.setString(pidx++, box.getString("p_tutor"             ));                     // [X]강사설명
            pstmt.setString(pidx++, box.getStringDefault("p_bookname",""));     				// [X]교재명
            pstmt.setString(pidx++, box.getString("p_sdesc"             ));                     // [X]비고
            pstmt.setInt   (pidx++, box.getInt   ("p_warndays"          ));                     // [X]학습경고적용일
            pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"          ));                     // [X]학습중지적용일
            pstmt.setInt   (pidx++, box.getInt   ("p_point"             ));                     // 수료점수
            pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"          ));                     // [X]1일최대학습량
            pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"         ));                     // 수료기준-점수
            pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"          ));                     // 수료기준-출석률
            pstmt.setDouble(pidx++, box.getDouble("p_wstep"             ));                     // 가중치-출석률
            pstmt.setDouble(pidx++, box.getDouble("p_wmtest"            ));                     // [X]가중치-중간평가
            pstmt.setDouble(pidx++, box.getDouble("p_wftest"            ));                     // 가중치-최종평가
            pstmt.setDouble(pidx++, box.getDouble("p_wreport"           ));                     // 가중치-과제
            pstmt.setDouble(pidx++, box.getDouble("p_wact"              ));                     // [X]가중치-액티비티
            pstmt.setDouble(pidx++, box.getDouble("p_wetc1"             ));                     // 가중치-참여도
            pstmt.setDouble(pidx++, box.getDouble("p_wetc2"             ));                     // 가중치-교수가점
            pstmt.setString(pidx++, box.getString("p_place"             ));                     // 교육장소
            pstmt.setString(pidx++, box.getString("p_edumans"           ));                     // 맛보기 교육대상
            pstmt.setInt   (pidx++, box.getInt   ("p_score"             ));                     // 학점배점
            pstmt.setString(pidx++, box.getStringDefault("p_isessential", "0"));     			// 과목구분
            pstmt.setString(pidx++, v_luserid                            );                     // 생성자
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss") );                     // 생성일
            pstmt.setString(pidx++, v_luserid                            );                     // 최종수정자
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));                      // 최종수정일(ldate)
            pstmt.setString(pidx++, box.getString("p_proposetype"       ));                     // 수강신청구분(TZ_CODE GUBUN='0019')
            pstmt.setInt   (pidx++, box.getInt   ("p_edutimes"          ));                     // 교육시간
            pstmt.setString(pidx++, box.getString("p_edutype"           ));                     // 교육형태
            pstmt.setString(pidx++, box.getString("p_intro"             ));                     // 맛보기 교육목적
            pstmt.setString(pidx++, box.getString("p_explain"           ));                     // 맛보기 교육내용
            pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"          ));                     // 수료기준(평가)
            pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"        ));                     // 수료기준(과제)
            pstmt.setString(pidx++, box.getRealFileName("p_file"        ));                     // 교재파일명
            pstmt.setString(pidx++, box.getNewFileName("p_file"         ));                     // 교재파일DB명
            pstmt.setString(pidx++, box.getString("p_conturl"           ));                     // 학습목차URL
            pstmt.setString(pidx++, box.getString("p_musertel"          ));                     // 운영담당 전화번호
            pstmt.setInt   (pidx++, box.getInt   ("p_gradftest"         ));                     // 수료기준-최종평가
            pstmt.setInt   (pidx++, box.getInt   ("p_gradhtest"         ));                     // 수료기준-형성평가
            pstmt.setString(pidx++, box.getStringDefault("p_isvisible", "Y"));     			  	// 학습자에게 보여주기
            pstmt.setString(pidx++, box.getString("p_isalledu"          ));                     // 전사교육여부
            pstmt.setString(pidx++, "Y"                                  );                     // 과목승인여부 (여기선 항상 Y)
            pstmt.setString(pidx++, box.getString("p_isintroduction"    ));                     // 과목소개 사용여부
            pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"         ));                     // 학습기간
            pstmt.setString(pidx++, box.getRealFileName("p_introducefile"   ));                 // 과목소개 이미지
            pstmt.setString(pidx++, box.getNewFileName ("p_introducefile"   ));                 // 과목소개 이미지
            pstmt.setString(pidx++, box.getRealFileName("p_informationfile" ));                 // 파일(목차)
            pstmt.setString(pidx++, box.getNewFileName ("p_informationfile" ));                 // 파일(목차)
            pstmt.setString(pidx++, box.getString("p_ischarge"        ));						// 수강료 유/무료 구분
            pstmt.setString(pidx++, box.getString("p_isopenedu"       ));                 	  	// 열린 교육 여부
            pstmt.setString(pidx++, box.getString("p_gradftest_flag"   ));                	  	// 수료기준-평가(필수/선택여부)
            pstmt.setString(pidx++, box.getString("p_gradreport_flag" ));                 	  	// 수료기준-과제(필수/선택여부)
            pstmt.setString(pidx++, box.getString("p_isgoyong")         );               		// 고용보험여부(Y/N)
            pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_major"));      				// 고용보험 환급액-환급액(KT)      
            pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_minor"));      				// 고용보험 환급액-KT교육비
            
            isOk        = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            // 2008.11.30 김미향 수정
            // 기존에 과정개설 목록화면에서 했던 교육그룹맵핑을 과정개설 등록, 수정화면에서 같이 입력하는것으로 변경.
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
      * 과정수정 - 이러닝
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
         
         // v_dir은 p_contenttype이 Normal,OBC,SCORM방식일때 dp/content/과목코드를 넣어준다.
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

             // KT디렉토리 링크생성시에 암호화
             KTUtil ktUtil = KTUtil.getInstance();
             
             // 신규과정코드 생성 (KT인재개발원에서는 직접입력받으므로 주석처리)
             //v_subj      = getMaxSubjcode(connMgr, box.getString("p_upperclass"), box.getString("p_middleclass") );
             
             String v_subj = box.getString("p_subj");
             String v_old_content_cd = box.getString ("p_old_content_cd"); // 이전 컨텐츠코드 코드
             String v_content_cd = box.getString ("p_content_cd");
             
             ConfigSet conf = new ConfigSet();
             String contentPath = conf.getProperty("dir.content.path");
             String ktContentPath = conf.getProperty("dir.kt_content.path");
             String contentUploadPath = conf.getProperty("dir.content.upload");
             String ktContentUploadPath = conf.getProperty("dir.kt_content.upload");
             String ktContentLink = conf.getProperty("dir.kt_content.link");
             
             // 디렉토리경로는 컨텐츠타입에 따라 상이하다.
             // 컨테츠타입이 N(Normal), O(OBC), OA(OBC-Author), S(SCORM) 일때
             if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") || v_contenttype.equals("S") ) {
             	v_dir = contentPath + v_subj;
             	v_content_cd = v_subj; // 해당 컨텐츠타입의 컨텐츠코드는 과정코드와 동일하다. KT컨텐츠일경우는 직접 입력받는다.
             } 
             // KT컨텐츠일 일때
             else if ( v_contenttype.equals("K")) {
                 v_dir = ktContentPath + v_content_cd;
             }
             // 그 이외의 컨텐츠타입 L(Link) 등등 일때
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
             pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000") ); // 분류코드
             pstmt.setString(pidx++, box.getString("p_upperclass"    ));                 // 대분류
             pstmt.setString(pidx++, box.getString("p_middleclass"   ));                 // 중분류
             pstmt.setString(pidx++, box.getStringDefault("p_lowerclass"     ,"000"));   // 소분류
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
             pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"      ));                    // 수료기준(평가)
             pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"    ));                    // 수료기준(과제)
             pstmt.setDouble(pidx++, box.getDouble("p_whtest"   ));                   		// 가중치(형성평가)
             pstmt.setString(pidx++, box.getString("p_isablereview"  ));                    // 복습가능여부
             pstmt.setInt   (pidx++, box.getInt   ("p_score"         ));                    // 학점배점
             pstmt.setString(pidx++, box.getString("p_isoutsourcing" ));                    // 위탁교육여부
             pstmt.setString(pidx++, box.getString("p_conturl"       ));                    // 학습목차URL
             pstmt.setString(pidx++, box.getStringDefault("p_isessential"        , "0"));   // 과목구분
             pstmt.setString(pidx++, box.getString("p_musertel"      ));                    // 운영담당 전화번호
             pstmt.setInt(pidx++, box.getInt("p_gradftest"     ));                    		// 수료기준-최종평가
             pstmt.setInt(pidx++, box.getInt("p_gradhtest"     ));                    		// 수료기준-형성평가
             pstmt.setString(pidx++, box.getStringDefault("p_isvisible"          , "N"));   // 학습자에게 보여주기
             pstmt.setString(pidx++, box.getString("p_isalledu"      ));                    // 전사교육여부
             pstmt.setString(pidx++, box.getString("p_edutimes"      ));                    // 학습시간
             pstmt.setString(pidx++, box.getString("p_isintroduction"));                    // 학습소개 사용여부
             pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"     ));                    // 학습기간
             pstmt.setString(pidx++, v_introducefilenamereal          );                    // 과목소개 이미지
             pstmt.setString(pidx++, v_introducefilenamenew           );                    // 과목소개 이미지
             pstmt.setString(pidx++, v_informationfilenamereal        );                    // 파일(목차)
             pstmt.setString(pidx++, v_informationfilenamenew         );                    // 파일(목차)
             pstmt.setString(pidx++, box.getString("p_contentgrade"  ));                    // 컨텐츠등급
             pstmt.setString(pidx++, box.getString("p_memo"          ));                    // 비고
             pstmt.setString(pidx++, box.getString("p_isopenedu"     ));                    // 열린 교육 여부
             pstmt.setString(pidx++, box.getString("p_gradftest_flag"));                    // 수료기준-최종평가(필수/선택여부)
             pstmt.setString(pidx++, box.getString("p_gradexam_flag" ));                    // 수료기준-중간평가(필수/선택여부)
             pstmt.setString(pidx++, box.getString("p_gradhtest_flag"));                    // 수료기준-형성평가(필수/선택여부)
             pstmt.setString(pidx++, box.getString("p_gradreport_flag"));                   // 수료기준-과제       (필수/선택여부)
             pstmt.setString(pidx++, box.getString("p_isgoyong"));               			// 고용보험여부(Y/N)
             pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_major"));      			// 고용보험 환급액-환급액(KT)
             pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_minor"));      			// 고용보험 환급액-KT교육비
             pstmt.setString(pidx++, box.getString("p_subj_gu"));      						// 과정구분 (일반, JIT:J, 안전교육:M)
             pstmt.setString(pidx++, box.getString("p_content_cd"));      					// 컨텐츠코드 (Normal경우 매핑)
             pstmt.setInt   (pidx++, box.getInt   ("p_study_count"));      					// 접속횟수(수료기준)
             pstmt.setInt   (pidx++, box.getInt   ("p_reviewdays"));      					// 복습가능기간

             pstmt.setString(pidx++, box.getString("p_lev"));      							// 교육수준
             pstmt.setString(pidx++, box.getString("p_gubn"));      						// 이수구분
             pstmt.setString(pidx++, box.getString("p_grade"));      						// 역량등급
             pstmt.setString(pidx++, box.getString("p_test"));      						// 교육평가

             pstmt.setInt   (pidx++, box.getInt   ("p_study_time"));      					// 수료기준-시간
             pstmt.setInt   (pidx++, box.getInt   ("p_cp_accrate"));      					// CP정산율

             pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_stand"));      			// 고용보험 환급액-CP교육비
             pstmt.setString(pidx++, box.getString("p_getmethod"));      					// 컨텐츠확보-방법
             pstmt.setString(pidx++, box.getString("p_cp"));      							// 컨텐츠확보-CP
             pstmt.setString(pidx++, box.getString("p_firstdate"));      					// 보유년도-최초확보
             pstmt.setString(pidx++, box.getString("p_judgedate"));      					// 보유년도-심사연월
             pstmt.setString(pidx++, box.getString("p_getdate"));      						// 보유년도-최종변경
             pstmt.setString(pidx++, box.getStringDefault("p_iscustomedu","N"));			// 맞춤형과정여부(Y/N)

             pstmt.setInt(pidx++, box.getInt("p_cp_account"));								// CP정산금액
             pstmt.setInt(pidx++, box.getInt("p_cp_vat"));									// CP VAT 
             pstmt.setString(pidx++, box.getString("p_contentprogress"));					// 진도체크(랜덤(Y)/순차(N)) 
             pstmt.setString(pidx++, box.getString("p_cpsubj"));					        // CP과정코드 
             
             pstmt.setString(pidx++, v_subj);

             isOk   = pstmt.executeUpdate();
             
             pstmt.close();
             
             // 초기화
             sbSQL.setLength(0);
             
             // 기수에 대해 과목명 수정
             sbSQL.append(" update tz_subjseq  set subjnm = ? where subj = ? ");
             
             pstmt  = connMgr.prepareStatement(sbSQL.toString());

             pstmt.setString(1, box.getString("p_subjnm"));
             pstmt.setString(2, v_subj);

             isOk  += pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
             // 2008.11.30 김미향 수정
             // 기존에 과정개설 목록화면에서 했던 교육그룹맵핑을 과정개설 등록, 수정화면에서 같이 입력하는것으로 변경.
             //isOk2 = RelatedGrcodeInsert(connMgr, box);
             isOk2 = 1;
             if (isOk > 0) {
            	 if ( isOk2 != -1) {
	                 // 디렉토리경로는 컨텐츠타입에 따라 상이하다.
	                 // 컨테츠타입이 N(Normal), O(OBC), OA(OBC-Author), S(SCORM) 일때
	                 if ( v_contenttype.equals("N") || v_contenttype.equals("O") || v_contenttype.equals("OA") || v_contenttype.equals("S") ) {
	                	 ktUtil.createDirectory( contentUploadPath, v_content_cd );
	                 } else if (v_contenttype.equals("K")) {
	                	 String md5_content_cd = ktUtil.md5Encode(v_content_cd);
	                	 //String md5_old_content_cd = ktUtil.md5Encode(v_old_content_cd);
	                	 if (!v_content_cd.equals(v_old_content_cd)) {
			                 // 2008.11.29 김미향 KT인재개발원에 맞게 수정.
			                 // KT 컨텐츠이면서 컨텐츠코드가 수정이 되었을 경우, 컨텐츠 디렉토리를 새로 생성해주고,
			            	 // 이전 컨텐츠코드로 생성된 링크값을 삭제해주며, 새로운 컨텐츠코드로 링크를 걸어준다.
	                		 //if (!"".equals(md5_old_content_cd)) {
	                			 //ktUtil.deleteDirectory(ktContentLink, md5_old_content_cd);
	                		 //}
	                	 }
	                	 if (!"".equals(md5_content_cd)) {
		                	 ktUtil.createDirectory( ktContentUploadPath, v_content_cd );
			                 ktUtil.createLink( ktContentUploadPath + v_content_cd, ktContentLink + md5_content_cd );
	                	 } else {
	                		 // 암호화한 컨텐츠코드가 널일 경우 에러
	                		 isOk = -3;
	                	 }
	                 }
            	 } else {
            		 // 교육그룹 생성시 에러가 났을 경우
            		 isOk = -2; 
            	 }
             
             } else {
            	 // 과정 수정이 안되었을 경우
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
     * 과정수정 - 집합
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
        // 기존 파일
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
                pstmt.setString(pidx++, ""                           );                         // 교재파일명
                pstmt.setString(pidx++, ""                           );                         // 교재파일DB명
            } else {                                                                        
                if ( !box.getRealFileName("p_file").equals("") ) {                          
                    pstmt.setString(pidx++, box.getRealFileName("p_file"));                     // 교재파일명
                    pstmt.setString(pidx++, box.getNewFileName ("p_file"));                     // 교재파일DB명
                } else {                                                                    
                    pstmt.setString(pidx++, v_oldbookfilenamereal   );                          // 교재파일명
                    pstmt.setString(pidx++, v_oldbookfilenamenew    );                          // 교재파일DB명
                }
            }

            pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"          ));                     // 수료기준(평가)
            pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"        ));                     // 수료기준(과제)
            //pstmt.setString(pidx++, box.getString("p_usesubjseqapproval"));                     // 기수개설주관팀장(Y/N)
            //pstmt.setString(pidx++, box.getString("p_useproposeapproval"));                     // 수강신청현업팀장(Y/N)
            //pstmt.setString(pidx++, box.getString("p_usemanagerapproval"));                     // 주관팀장수강승인(Y/N)
            //pstmt.setString(pidx++, box.getStringDefault("p_rndcreditreq"       , "0"   ));     // 학점배점(연구개발)-필수
            //pstmt.setString(pidx++, box.getStringDefault("p_rndcreditchoice"    , "0"   ));     // 학점배점(연구개발)-선택
            //pstmt.setString(pidx++, box.getStringDefault("p_rndcreditadd"       , "0"   ));     // 학점배점(연구개발)-지정가점
            //pstmt.setString(pidx++, box.getStringDefault("p_rndcreditdeduct"    , "0"   ));     // 학점배점(연구개발)-지정감점
            pstmt.setString(pidx++, box.getStringDefault("p_isessential"        , "0"   ));     // 과목구분
            pstmt.setDouble(pidx++, box.getDouble("p_score"             ));                     // 
            pstmt.setString(pidx++, box.getString("p_musertel"          ));                     // 운영담당 전화번호
            pstmt.setInt(pidx++, box.getInt("p_gradftest"         ));                     // 수료기준-최종평가
            pstmt.setInt(pidx++, box.getInt("p_gradhtest"         ));                     // 수료기준-형성평가
            pstmt.setString(pidx++, box.getStringDefault("p_isvisible"          , "N"   ));     // 학습자에게 보여주기
            pstmt.setString(pidx++, box.getString("p_isalledu"          ));                     // 전사교육여부
            pstmt.setString(pidx++, box.getString("p_placejh"           ));                     // 집합장소
            pstmt.setString(pidx++, box.getString("p_isintroduction"    ));                     // 학습소개 사용여부
            pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"         ));                     // 학습기간
            pstmt.setString(pidx++, v_introducefilenamereal              );                     // 과목소개 이미지
            pstmt.setString(pidx++, v_introducefilenamenew               );                     // 과목소개 이미지
            pstmt.setString(pidx++, v_informationfilenamereal            );                     // 파일(목차)
            pstmt.setString(pidx++, v_informationfilenamenew             );                     // 파일(목차)
            pstmt.setString(pidx++, box.getStringDefault("p_ischarge"           , "N"   ));     // 수강료 유/무료 구분
            pstmt.setString(pidx++, box.getStringDefault("p_isopenedu"          , "N"   ));     // 열린 교육 여부 
            //pstmt.setInt   (pidx++, box.getInt   ("p_maleassignrate"    ));                     // 수료기준-평가(필수/선택여부)
            pstmt.setString(pidx++, box.getString("p_gradftest_flag"    ));                     // 수료기준-과제(필수/선택여부)
            pstmt.setString(pidx++, box.getString("p_gradreport_flag"   ));                     // 남성 할당 비율
            pstmt.setString(pidx++, box.getString ("p_isgoyong"));               //고용보험여부(Y/N)
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_major"));      //고용보험 환급액-환급액(KT)
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_minor"));      //고용보험 환급액-KT교육비

            pstmt.setString(pidx++, box.getString ("p_lev"));      				//교육수준
            pstmt.setString(pidx++, box.getString ("p_gubn"));      			//이수구분
            pstmt.setString(pidx++, box.getString ("p_grade"));      			//역량등급
            pstmt.setString(pidx++, box.getString ("p_test"));      			//교육평가
            pstmt.setString(pidx++, box.getString ("p_org"));      				//교육기관
            pstmt.setDouble(pidx++, box.getDouble ("p_goyongprice_stand"));     //고용보험 환급액-CP교육비
            
            pstmt.setString(pidx++, box.getString("p_subj"              ));
            
            isOk 	= pstmt.executeUpdate();
            
            pstmt.close();
            
            sbSQL.setLength(0);

            // 기수에 대해 과목명 수정
            sbSQL.append(" update tz_subjseq  set subjnm = ? where subj = ? \n");
            
            pstmt   = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setString(1, box.getString("p_subjnm" ));
            pstmt.setString(2, box.getString("p_subj"   ));

            pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            // 2008.11.30 김미향 수정
            // 기존에 과정개설 목록화면에서 했던 교육그룹맵핑을 과정개설 등록, 수정화면에서 같이 입력하는것으로 변경.
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
     * 과정삭제
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
     * 교육그룹 조회
     * @param box          receive from the form object and session
     * @return ArrayList   교육그룹 리스트
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

        	if (v_gadmin.equals("H")) {	// 교육그룹관리자
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
     * 교육그룹 상세조회
     * @param box          receive from the form object and session
     * @return ArrayList   교육그룹 리스트
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
     * 교육그룹 과정 맵핑
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
            // tz_preview 테이블은 tz_grsubj 테이블처럼 모든 데이타 삭제 후 Insert하지 않고
            // Insert하려는 데이타가 없는 경우에만 tz_subj 테이블의 맛보기 정보를 tz_preview에 Insert한다.
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

            // 교육주관연결시 연결되어 있는 교육주관을 삭제하는 경우
            // tz_grsubj 테이블에 없으므로 tz_preview 테이블에 있는 자료도 삭제한다.
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
     * 교육그룹 과정 맵핑
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
            // tz_preview 테이블은 tz_grsubj 테이블처럼 모든 데이타 삭제 후 Insert하지 않고
            // Insert하려는 데이타가 없는 경우에만 tz_subj 테이블의 맛보기 정보를 tz_preview에 Insert한다.
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

            // 교육주관연결시 연결되어 있는 교육주관을 삭제하는 경우
            // tz_grsubj 테이블에 없으므로 tz_preview 테이블에 있는 자료도 삭제한다.
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
     * 과정맛보기 등록된 교육그룹 조회
     * @param box          receive from the form object and session
     * @return ArrayList   교육그룹 리스트
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
     * 과정맛보기 등록
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
     * 과정맛보기 상세조회
     * @param box          receive from the form object and session
     * @return PreviewData   과목데이타
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
     * 과정맛보기 수정
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
     * 과정맛보기 삭제
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
     * 연결 과정 리스트 조회
     * @param box          receive from the form object and session
     * @return ArrayList   연결 과목리스트
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
     * 연결 과정리스트 조회
     * @param box          receive from the form object and session
     * @return ArrayList   연결 과목리스트
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
     * 연결과정 등록
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
     * 과정개설시 TZ_BDS에 등록
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
            
            // StringBuffer 초기화    
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
            pstmt.setString( 2, v_type      ); // 게시판 타입
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
      * 신규 과정 조회
      * @param box          receive from the form object and session
      * @return String      신규 이미지 리턴   
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
                 v_rtnvalue = "<img src=\"/images/user/c/icon_new03.gif\" alt=\"신규\" class=\"btn_space_l8\" />";
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
     추천 과정
     @param box          receive from the form object and session
     @return ArrayList   추천 이미지 리턴   
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
                     v_rtnvalue = "<img src=\"/images/user/c/icon_recommend03.gif\" alt=\"추천\" class=\"btn_space_l8\" />";
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
	 * 사외위탁과정 관리 LIst
	 * @param box          receive from the form object and session
	 * @return ArrayList   과정리스트
     */
    public ArrayList TrustSelectSubjectList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql  = "";
        DataBox dbox = null;
		
        String  ss_grcode       = box.getStringDefault("s_grcode","ALL");       //교육주관
        String  ss_gyear        = box.getStringDefault("s_gyear","ALL");       //교육주관        
        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");   //과정분류
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");  //과정분류
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");   //과정분류
        String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");   //과정&코스
        String  ss_subjseq      = box.getStringDefault("s_subjseq","ALL");   //과정&코스        
        String  ss_midstat		= box.getStringDefault("p_midstat","ALL");		//HRD 담당자 승인 상태
        String  ss_secstat		= box.getStringDefault("p_secstat","ALL"); 		//HRD 부장 승인 상태
        String  v_searchtext    = box.getString("p_searchtext");

        String  v_orderColumn   = box.getString("p_order");                     //정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 //정렬할 순서

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

            //교육그룹
            if (!ss_grcode.equals("ALL")) {
                sql+= "         and b.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            
            //교육년도
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
     * 사외위탁 과정 조회
     * @param box          receive from the form object and session
     * @return PreviewData   과정데이타
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
	 * 집단승인처리
	 * @param box          receive from the form object and session
	 * @return int     결과값(0,1)
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
		String v_rejectstat     = "";		//반려상태
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
                v_param     = vec_param.elementAt(i).toString();    //과정코드차수Userid...

				StringTokenizer arr_value = new StringTokenizer(v_param, ",");
 
				v_userid	= arr_value.nextToken();
				v_seq		= arr_value.nextToken();
//				v_midstat	= arr_value.nextToken();
				v_secstat	= arr_value.nextToken();
				v_chkfinal	= arr_value.nextToken();
				
				//반려일 경우 (v_chkfinal[반려:N | 승인:Y | 미처리:B])
				
				if(v_chkfinal.equals("N")) 
				{ 
                    v_secstat      = "";
					v_rejectstat   = "Y" ;
					v_rejectreson  = "운영자 반려";
                    v_rejectdate   = FormatDate.getDate("yyyyMMddHHmmss");
				}//반려상태
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
	 * 집단반려처리
	 * @param box          receive from the form object and session
	 * @return int     결과값(0,1)
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
			sql +=" Set midstat = ?, rejectstat = ?, rejectdate = ?, rejectreason = '운영자 반려' ";
			sql +=" Where seq = ? and userid = ?";
			
			pstmt = connMgr.prepareStatement(sql);
			for(int i = 0; i < vec_param.size(); i++) {
                v_param     = vec_param.elementAt(i).toString();    //과정코드차수Userid...

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
	 * 사외위탁 과정 개설 모듈 .
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

			// 기본 교육코드 설정.
			box.put("p_grcode", "N000001");

//            SubjectBean bean = new SubjectBean();

			if(v_subj.equals(""))
			{
				// 과정이 없을 시 과정 생성.
				v_subj = InsertTrustSubject(box, connMgr);
				box.put("p_subj", v_subj);

			}
		
			// 교육 차수 생성		
			v_grseq = InsertGrseq(box, connMgr);

			if(v_grseq.equals("")) isOk = 0;
			else	isOk = 1;

			v_subjseq = makeSubjseq("N000001", v_year, v_grseq, "000000", "0000","0000",  v_subj, box.getSession("userid"), 0,"","","","",-7,"","",connMgr);

			if(v_subjseq.equals("")) isOk2 = 0;
			else	isOk2	= 1;
			
			for(int j = 0; j < vec_param.size(); j++) {
                v_param     = vec_param.elementAt(j).toString();    //과정코드차수Userid...
				
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

			
			// 모든 과정 종료후 신청자 테이블 변경 & 입과.
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
	 * 과정등록 - 사외위탁 과정
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
        String v_grcode   = box.getStringDefault("s_grcode","ALL"); //과정리스트에서의 SELECTBOX 교육주관 코드
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

            pstmt.setString( 1, v_subj);							//과정코드
            pstmt.setString( 2, box.getString("p_subjnm"));			//과정명
            pstmt.setString( 3, box.getString("p_isonoff"));		//온라인/오프라인구분/사외위탁구분
            pstmt.setString( 4, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000")); //분류코드
            pstmt.setString( 5, box.getString("p_upperclass"));				//대분류
            pstmt.setString( 6, box.getString("p_middleclass"));			//중분류
            pstmt.setString( 7, box.getStringDefault("p_lowerclass","000"));//소분류
            pstmt.setString( 8, box.getString("p_specials"));               //과정특성
            pstmt.setString( 9, box.getStringDefault("p_edugubun","OFF"));  //교육구분             
            pstmt.setString(10, box.getString("p_isgoyong"));               //고용보험여부(Y/N)
            pstmt.setDouble(11, box.getDouble("p_goyongprice_major"));      //고용보험 환급액            
            pstmt.setInt   (12, box.getInt   ("p_biyong"));                 //수강료
            pstmt.setString(13, box.getString("p_isuse"));                  //사용여부
            pstmt.setString(14, "Y");                                       //과정승인여부 (여기선 항상 Y)   
            pstmt.setString(15, "Y");                                       //학습자에게 보여주기 (여기선 항상 Y)
            pstmt.setString(16, box.getString("p_owner"));                  //교육주관             
            pstmt.setInt   (17, box.getInt   ("p_edudays"));				//교육기간(일)
            pstmt.setInt   (18, box.getInt   ("p_edutimes"));               //교육시간            
            pstmt.setInt   (19, box.getInt   ("p_point"));                  //이수점수
            pstmt.setString(20, box.getString("p_place"));                  //교육장소
            pstmt.setInt   (21, box.getInt   ("p_score"));                  //학점배점
            pstmt.setString(22, v_luserid);                                 //생성자
            pstmt.setString(23, FormatDate.getDate("yyyyMMddHHmmss"));      //생성일
            pstmt.setString(24, v_luserid);                                 //최종수정자
            pstmt.setString(25, FormatDate.getDate("yyyyMMddHHmmss"));      //최종수정일(ldate)
            pstmt.setString(26, box.getString("p_proposetype"));            //수강신청구분(TZ_CODE GUBUN='0019')
            pstmt.setInt   (27, box.getInt   ("p_studentlimit"));           //차수당정원            
            pstmt.setString(28, box.getString("p_edutype"));				//교육형태
            pstmt.setString(29, box.getString("p_muserid"));                //운영담당ID        
            pstmt.setString(30, box.getString("p_musertel"));               //운영담당 전화번호            
            pstmt.setString(31, box.getString("p_museremail"));				//운영자 E-mail 
            pstmt.setString(32, box.getString("p_eduurl"));                 //교육형태
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            //box.put("s_upperclass",box.getString("p_upperclass"));
            //box.put("s_middleclass",box.getString("p_middleclass"));

            //교육주관 관리자는 해당 교육주관으로 INSERT하게 한다.
            if (StringManager.substring(box.getSession("gadmin"),0,1).equals("H")) {
                this.SaveGrSubj(connMgr, v_grcode, v_subj, v_luserid); //int isOk3 = 
            }
            //관리자 일경우 N000001 (오토에버) 교육그룹으로 자동 insert 한다
            else if (StringManager.substring(box.getSession("gadmin"),0,1).equals("A")) {
                this.SaveGrSubj(connMgr, "N000001", v_subj, v_luserid); //int isOk4 = 
            }

            //과정차수별 자료실 Insert
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
	 * 사외위탁 과정 개설 후 신청자 테이블 변경.
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
                v_param     = vec_param.elementAt(j).toString();    //과정코드차수Userid...
				
				StringTokenizer arr_value = new StringTokenizer(v_param, ",");
				
				v_userid	= arr_value.nextToken();
				v_seq		= arr_value.nextToken();
				
				pstmt.setString( 1, v_subj);
	            pstmt.setString( 2, v_year); //과정별 자료실
	            pstmt.setString( 3, v_subjseq);
	            pstmt.setString( 4, v_seq);

	            isOk = pstmt.executeUpdate();
				
				pstmt2.setString( 1, v_subj);
	            pstmt2.setString( 2, v_year); //과정별 자료실
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
				// Tz_studend 에 Insert
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
	  * 교육기수 등록
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
	            pstmt.setString(4,  "사외위탁연수");
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
     * 과정 기수 생성 - 과정정보를 상속 받는다.
     * @param      	String  p_grcode        교육그룹
     *             	String  p_gyear         교육연도
     *             	String  p_grseq         교육기수
     *          	String  p_course        코스코드
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
        String              v_subjseqgr         = "";    // 교육그룹에 속한 과목기수
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
        
            // 과목의 컨텐츠 타입를 구한다.
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

            // 년도별 과목기수를 가지고 온다.
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
                // 최근의 과목기수 정보를 가져오고
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
                // 지정된 과목기수 정보를 가져오고
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
                
                // 없으면 과목정보에서 상속받는다.
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
            	//과목기수별 나의각오방 Insert
            	//subBean.InsertBds(conn, p_subj,"MP", "나의각오방", p_gyear, v_subjseq);
    
                //과목기수별 성찰하기방 Insert
            	//subBean.InsertBds(conn, p_subj,"CJ", "성찰하기방", p_gyear, v_subjseq);
            //}
            
            // 학습종료일이 있을경우 과제마감일시를 종료일로 세팅
            if ( p_eduend.length() >= 8) { 
                v_expiredate    = StringManager.substring(p_eduend,0,8);
            } else { 
                v_expiredate    = "";
            }

            if ( isOk > 0) { 
                sbSQL.setLength(0);

                // Report 출제정보 Copy;

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
                    // 최근의 과목기수 정보를 가져오고
                    sbSQL.append(" and year+ subjseq = (                                                   \n")    
                         .append("                           select  max(year + subjseq)                   \n")
                         .append("                           from    tz_projord                             \n")
                         .append("                           where   subj = " + SQLString.Format(p_subj) + "\n")
                         .append("                       )                                                  \n");
                } else { 
                    // 지정된 과목기수 정보를 가져오고
                    sbSQL.append(" and year     = " + SQLString.Format(p_copy_gyear ) + "                   \n")
                         .append(" and subjseq  = " + SQLString.Format(p_copy_grseq ) + "                   \n");
                }

                isOk    = conn.executeUpdate(sbSQL.toString());
                
                isOk    = 1;

                // 평가문제지 Copy;
                ExamPaperBean   exambean    = new ExamPaperBean();
                isOk                        = exambean.insertExamPaper(p_subj, p_gyear, v_subjseq, p_userid);
                isOk                        = 1;
                
                sbSQL.setLength(0);
                

                // 집합강좌 Copy;
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
	  * 과정기수정보 수정
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

        String v_subj		= box.getString("p_subj");		//과정코드
        String v_year		= box.getString("p_year");		//년도
        String v_subjseq	= box.getString("p_subjseq");	//과정차수
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

        String v_luserid      = box.getSession("userid");       // 세션변수에서 사용자 id를 가져온다.

        //String v_targettablename = "tz_subjseq";
		// 수강제한 관련. 
		String v_limityn 		= box.getString("p_limitYn");
		String v_licensevalue 	= box.getString("p_licensevalue");
		String v_jikvalue 		= box.getString("p_jikvalue");
		
		int pidx = 1;
        try {
			
            // 과정의 컨텐츠 타입, YESLEARN 과정코드를 구한다.
            sql = " select contenttype from tz_subj where subj= " + StringManager.makeSQL(v_subj);
            ls  = connMgr.executeQuery(sql);
            if(ls.next()) {
                v_contenttype = ls.getString("contenttype");
                //v_aesserialno = ls.getString("aesserialno");
            }
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }

            // 과정차수에서 YESLEARN GroupList의 seq를 구한다.
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
            pstmt.setInt   (pidx++, box.getInt("p_gradexam"));                 	//이수기준(평가)
            pstmt.setInt   (pidx++, box.getInt("p_gradreport"));               	//이수기준(리포트)
            pstmt.setInt   (pidx++, box.getInt("p_whtest"));                   	//가중치(형성평가)
            pstmt.setDouble(pidx++, box.getDouble("p_rndcreditreq"));			//학점배점(연구개발)-필수
            pstmt.setDouble(pidx++, box.getDouble("p_rndcreditchoice"));		//학점배점(연구개발)-선택
            pstmt.setDouble(pidx++, box.getDouble("p_rndcreditadd"));			//학점배점(연구개발)-지정가점
            pstmt.setDouble(pidx++, box.getDouble("p_rndcreditdeduct"));		//학점배점(연구개발)-지정감점
            pstmt.setString(pidx++, box.getString("p_isablereview"));       	//복습가능여부
            pstmt.setInt   (pidx++, box.getInt("p_score"));                    	//학점배점
            pstmt.setInt   (pidx++, box.getInt("p_tsubjbudget"));              	//과정예산
            pstmt.setString(pidx++, box.getStringDefault("p_rndjijung","N"));	//지정과정여부
            pstmt.setString(pidx++, box.getStringDefault("p_isessential","0"));	//
            pstmt.setInt   (pidx++, box.getInt("p_gradftest"));                	//
            pstmt.setInt   (pidx++, box.getInt("p_gradhtest"));                	//
            pstmt.setString(pidx++, box.getStringDefault("p_isvisible","Y")); 	//학습자에게 보여주기
            pstmt.setString(pidx++, box.getString("p_place")); 					//교육장소
            pstmt.setString(pidx++, box.getString("p_placejh")); 				//집합장소
            pstmt.setInt   (pidx++, box.getInt("p_sulpaper")); 					// 설문지 번호
            pstmt.setInt   (pidx++, box.getInt("p_canceldays")); 				//취소날자
			
            pstmt.setString(pidx++, v_limityn); 								//수강제한여부
            pstmt.setString(pidx++, v_licensevalue); 							//자격증 등급
            pstmt.setString(pidx++, v_jikvalue); 								//직급 등급
            
            pstmt.setInt   (pidx++, box.getInt("p_reviewdyas")); 				//복습가능기간
            pstmt.setInt   (pidx++, box.getInt("p_study_count")); 				//접속횟수(수료기준)

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
	 * 과정코드 수정 - 사외위탁 과정
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

            pstmt.setString( 1, box.getString("p_subjnm"));			//과정명
            pstmt.setString( 2, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000")); //분류코드
            pstmt.setString( 3, box.getString("p_upperclass"));				//대분류
            pstmt.setString( 4, box.getString("p_middleclass"));			//중분류
            pstmt.setString( 5, box.getStringDefault("p_lowerclass","000"));//소분류
            pstmt.setString( 6, box.getStringDefault("p_edugubun","OFF"));   //교육구분             
            pstmt.setString( 7, box.getString("p_eduurl"));                 //과정특성
            pstmt.setString( 8, box.getString("p_isuse"));                  //사용여부
            pstmt.setInt   ( 9, box.getInt   ("p_biyong"));                 //수강료            
            pstmt.setString(10, box.getString("p_isgoyong"));               //고용보험여부(Y/N)
            pstmt.setString(11, box.getString("p_goyongprice_major"));      //고용보험 환급액
            pstmt.setString(12, box.getString("p_edutype"));                //교육형태            
            pstmt.setInt   (13, box.getInt   ("p_edudays"));				//교육기간(일)
            pstmt.setInt   (14, box.getInt   ("p_edutimes"));               //교육시간
            pstmt.setString(15, v_luserid);                                 //최종수정자
            pstmt.setString(16, FormatDate.getDate("yyyyMMddHHmmss"));      //최종수정일(ldate)
            pstmt.setString(17, box.getString("p_owner"));					//교육주관 
            pstmt.setString(18, box.getString("p_muserid"));                //운영담당ID            
            pstmt.setString(19, box.getString("p_musertel"));				//운영담당연락처
            pstmt.setString(20, box.getString("p_museremail"));					//운영담당이메일 			
            pstmt.setString(21, v_subj);							        //과정코드
            
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
	 * 사외위탁과정 금융자격증 LIst
	 * @param box          receive from the form object and session
	 * @return ArrayList   과정리스트
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
	 * 과정코드 수정 - 사외위탁 과정
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
            pstmt.setString( 1, box.getString("p_edugubun"));					//교육구분
            pstmt.setString( 2, box.getString("p_subjnm")); 					//과정명
            pstmt.setString( 3, box.getString("p_edustart"));				//교육시작일 
            pstmt.setString( 4, box.getString("p_eduend"));					//교육종료일
            pstmt.setInt   ( 5, box.getInt("p_edudays"));				//교육기간			
            pstmt.setInt   ( 6, box.getInt("p_edutimes"));               	//교육시간
            
            pstmt.setString( 7, box.getString("p_ownernm"));                	//교육주관
            pstmt.setString( 8, box.getString("p_eduplace"));                	//교육장소
            pstmt.setString( 9, box.getString("p_musernm"));				//담당자
            pstmt.setString(10, box.getString("p_mphone"));               //담당자 전화번호
            pstmt.setString(11, box.getString("p_mfax"));                //담당자 팩스
            pstmt.setString(12, box.getString("p_memail"));              //담당자 이메일
            
            pstmt.setString(13, box.getString("p_eduurl"));              //교육URL
            pstmt.setString(14, box.getString("p_edubill"));             //교육비
            pstmt.setString(15, box.getString("p_billgubun"));           //교육비 구분
            pstmt.setString(16, box.getString("p_accountnum"));          //계좌번호             
            pstmt.setString(17, box.getString("p_goyongyn"));            //고용보험여부
            pstmt.setString(18, box.getString("p_myduty"));              //본인직무
            pstmt.setString(19, box.getString("p_dutyterm"));			 //현직무 수행기간
            pstmt.setString(20, box.getString("p_result"));				 //개선효과			
            pstmt.setString(21, v_filereal);							 //첨부파일
            pstmt.setString(22, v_filenew); 							 //첨부파일			
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
	 * 사외위탁과정 관리 LIst
	 * @param box          receive from the form object and session
	 * @return ArrayList   과정리스트
     */
    public ArrayList TrustSelectSubjectExcel(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql  = "";
        DataBox dbox = null;
		
        String  ss_grcode       = box.getStringDefault("s_grcode","ALL");       //교육주관
        String  ss_gyear        = box.getStringDefault("s_gyear","ALL");       //교육주관        
        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");   //과정분류
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");  //과정분류
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");   //과정분류
        String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");   //과정&코스
        String  ss_subjseq      = box.getStringDefault("s_subjseq","ALL");   //과정&코스        
        String  ss_midstat      = box.getStringDefault("p_midstat","ALL");      //HRD 담당자 승인 상태
        String  ss_secstat      = box.getStringDefault("p_secstat","ALL");      //HRD 부장 승인 상태
        String  v_searchtext    = box.getString("p_searchtext");

        String  v_orderColumn   = box.getString("p_order");               //정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 //정렬할 순서

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

            //교육그룹
            if (!ss_grcode.equals("ALL")) {
                sql+= "         and b.grcode = " + StringManager.makeSQL(ss_grcode) + "	\n";
            }

            //교육년도
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
	 * 사외위탁 과정 직접입과
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
//        String  v_muserid   = ApprovalAdminBean.getMuserid(box);  // 부서장 정보
        
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
            
            //////////////////////////////////   사외위탁 신청 table 에 입력  ///////////////////////////////////////////////////////////////////
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
	 * 과정등록 - 독서교육
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
		String                 v_grcode   	= box.getStringDefault("s_grcode"   , "ALL");   // 과목리스트에서의 SELECTBOX 교육주관 코드

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

			pstmt.setString(pidx++, "K"+v_subj                               );                     // 과목코드
			pstmt.setString(pidx++, box.getString("p_subjnm"            ));                     // 과목명
			pstmt.setString(pidx++, box.getString("p_isonoff"           ));                     // 온라인/오프라인구분
			pstmt.setString(pidx++, box.getString("p_upperclass") + box.getString("p_middleclass") + box.getStringDefault("p_lowerclass","000") ); // 분류코드
			pstmt.setString(pidx++, box.getString("p_upperclass"        ));                     // 대분류
			pstmt.setString(pidx++, box.getString("p_middleclass"       ));                     // 중분류
			pstmt.setString(pidx++, box.getStringDefault("p_lowerclass"         , "000" ));     // 소분류
			pstmt.setString(pidx++, box.getString("p_specials"          ));                     // 과목특성
			pstmt.setString(pidx++, box.getString("p_muserid"           ));                     // 운영담당ID
			pstmt.setString(pidx++, box.getString("p_cuserid"           ));                     // [X]컨텐츠담당ID
			pstmt.setString(pidx++, box.getString("p_isuse"             ));                     // 사용여부
			pstmt.setString(pidx++, box.getString("p_ispropose"         ));                     // [X]수강신청여부(Y/N)
			pstmt.setInt   (pidx++, box.getInt   ("p_biyong"            ));                     // 수강료
			pstmt.setInt   (pidx++, box.getInt   ("p_edudays"           ));                     // 교육기간(일)
			pstmt.setInt   (pidx++, box.getInt   ("p_studentlimit"      ));                     // 기수당정원
			pstmt.setString(pidx++, box.getString("p_usebook"           ));                     // [X]교재사용여부
			pstmt.setInt   (pidx++, Integer.parseInt(box.getStringDefault("p_bookprice"          , "0"   )));     // [X]교재비
			pstmt.setString(pidx++, box.getString("p_owner"             ));                     // [X]과목소유자
			pstmt.setString(pidx++, box.getString("p_producer"          ));                     // [X]과목제공자
			pstmt.setString(pidx++, box.getString("p_crdate"            ));                     // [X]제작일자
			pstmt.setString(pidx++, box.getString("p_language"          ));                     // [X]언어선택
			pstmt.setString(pidx++, box.getString("p_server"            ));                     // [X]서버
			pstmt.setString(pidx++, box.getString("p_dir"               ));                     // [X]컨텐츠경로
			pstmt.setString(pidx++, box.getString("p_eduurl"            ));                     // [X]교육URL
			pstmt.setString(pidx++, box.getString("p_vodurl"            ));                     // [X]VOD경로
			pstmt.setString(pidx++, box.getString("p_preurl"            ));                     // 맛보기URL
			pstmt.setInt   (pidx++, box.getInt   ("p_ratewbt"           ));                     // [X]학습방법(WBT%)
			pstmt.setInt   (pidx++, box.getInt   ("p_ratevod"           ));                     // [X]학습방법(VOD%)
			pstmt.setString(pidx++, box.getString("p_env"               ));                     // [X]학습환경
			pstmt.setString(pidx++, box.getString("p_tutor"             ));                     // [X]강사설명
			pstmt.setString(pidx++, box.getStringDefault("p_bookname"           , ""    ));     // [X]교재명
			pstmt.setString(pidx++, box.getString("p_sdesc"             ));                     // [X]비고
			pstmt.setInt   (pidx++, box.getInt   ("p_warndays"          ));                     // [X]학습경고적용일
			pstmt.setInt   (pidx++, box.getInt   ("p_stopdays"          ));                     // [X]학습중지적용일
			pstmt.setInt   (pidx++, box.getInt   ("p_point"             ));                     // 수료점수
			pstmt.setInt   (pidx++, box.getInt   ("p_edulimit"          ));                     // [X]1일최대학습량
			pstmt.setInt   (pidx++, box.getInt   ("p_gradscore"         ));                     // 수료기준-점수
			pstmt.setInt   (pidx++, box.getInt   ("p_gradstep"          ));                     // 수료기준-출석률
			pstmt.setDouble(pidx++, box.getDouble("p_wstep"             ));                     // 가중치-출석률
			pstmt.setDouble(pidx++, box.getDouble("p_wmtest"            ));                     // [X]가중치-중간평가
			pstmt.setDouble(pidx++, box.getDouble("p_wftest"            ));                     // 가중치-최종평가
			pstmt.setDouble(pidx++, box.getDouble("p_wreport"           ));                     // 가중치-과제
			pstmt.setDouble(pidx++, box.getDouble("p_wact"              ));                     // [X]가중치-액티비티
			pstmt.setDouble(pidx++, box.getDouble("p_wetc1"             ));                     // 가중치-참여도
			pstmt.setDouble(pidx++, box.getDouble("p_wetc2"             ));                     // [X]가중치-토론참여도
			pstmt.setString(pidx++, box.getString("p_place"             ));                     // 교육장소
			pstmt.setString(pidx++, box.getString("p_edumans"           ));                     // 맛보기 교육대상
			pstmt.setString(pidx++, box.getStringDefault("p_isessential"        , "0"   ));     // 과목구분
			pstmt.setInt   (pidx++, box.getInt   ("p_score"             ));                     // 학점배점
			pstmt.setString(pidx++, v_luserid                            );                     // 생성자
			pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss") );                     // 생성일
			pstmt.setString(pidx++, v_luserid                            );                     // 최종수정자
			pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));                      // 최종수정일(ldate)
			pstmt.setString(pidx++, box.getString("p_proposetype"       ));                     // 수강신청구분(TZ_CODE GUBUN='0019')
			pstmt.setInt   (pidx++, box.getInt   ("p_edutimes"          ));                     // 교육시간
			pstmt.setString(pidx++, box.getString("p_edutype"           ));                     // 교육형태
			pstmt.setString(pidx++, box.getString("p_intro"             ));                     // 맛보기 교육목적
			pstmt.setString(pidx++, box.getString("p_explain"           ));                     // 맛보기 교육내용
			pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"          ));                     // 수료기준(평가)
			pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"        ));                     // 수료기준(과제)
			pstmt.setString(pidx++, box.getRealFileName("p_file"        ));                     // 교재파일명
			pstmt.setString(pidx++, box.getNewFileName("p_file"         ));                     // 교재파일DB명
			pstmt.setString(pidx++, box.getString("p_conturl"           ));                     // 학습목차URL
			pstmt.setString(pidx++, box.getString("p_musertel"          ));                     // 운영담당 전화번호
			pstmt.setInt   (pidx++, box.getInt   ("p_gradftest"         ));                     // 수료기준-최종평가
			pstmt.setInt   (pidx++, box.getInt   ("p_gradhtest"         ));                     // 수료기준-형성평가
			pstmt.setString(pidx++, box.getStringDefault("p_isvisible"          , "Y"   ));     // 학습자에게 보여주기
			pstmt.setString(pidx++, box.getString("p_isalledu"          ));                     // 전사교육여부
			pstmt.setString(pidx++, "Y"                                  );                     // 과목승인여부 (여기선 항상 Y)
			pstmt.setString(pidx++, box.getString("p_isintroduction"    ));                     // 과목소개 사용여부
			pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"         ));                     // 학습기간
			pstmt.setString(pidx++, box.getRealFileName("p_introducefile"   ));                 // 과목소개 이미지
			pstmt.setString(pidx++, box.getNewFileName ("p_introducefile"   ));                 // 과목소개 이미지
			pstmt.setString(pidx++, box.getRealFileName("p_informationfile" ));                 // 파일(목차)
			pstmt.setString(pidx++, box.getNewFileName ("p_informationfile" ));                 // 파일(목차)
			pstmt.setString(pidx++, box.getString      ("p_ischarge"        ));                 // 수강료 유/무료 구분
			pstmt.setString(pidx++, box.getString      ("p_isopenedu"       ));                 // 열린 교육 여부
			pstmt.setString(pidx++, box.getString      ("p_gradftest_flag"   ));                // 수료기준-평가(필수/선택여부)
			pstmt.setString(pidx++, box.getString      ("p_gradreport_flag" ));                 // 수료기준-과제(필수/선택여부)
			pstmt.setString(pidx++, box.getString("p_isgoyong"));               				// 고용보험여부(Y/N)
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_major"));      				// 고용보험 환급액-환급액(KT)   
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_minor"));      				// 고용보험 환급액-KT교육비
			pstmt.setString(pidx++, box.getString("p_isoutsourcing"));							// 위탁교육여부(Y/N)
			pstmt.setString(pidx++, box.getString("p_graduatednote"));							// 수료기준 비고
			pstmt.setString(pidx++, box.getString("p_isablereview"));							// 과정특성(신규,추천,HIT)

			pstmt.setString(pidx++, box.getString("p_subj_gu"));								// 과정구분(일반, JIT:J, 안전교육:M)
			pstmt.setString(pidx++, box.getString("p_content_cd")); 							// 컨텐츠코드(Normal일 경우 매핑)
			pstmt.setInt   (pidx++, box.getInt   ("p_study_count")); 							// 접속횟수(수료기준)
			pstmt.setInt   (pidx++, box.getInt   ("p_reviewdays")); 							// 복습가능기간

			pstmt.setString(pidx++, box.getString("p_sel_dept")); 								// 부서 신청제한
			pstmt.setString(pidx++, box.getString("p_sel_post")); 							 	// 직급 신청제한

			pstmt.setString(pidx++, box.getString("p_lev")); 							 		// 교육수준
			pstmt.setString(pidx++, box.getString("p_gubn")); 							 		// 이수구분
			pstmt.setString(pidx++, box.getString("p_grade")); 							 		// 역량등급
			pstmt.setString(pidx++, box.getString("p_test")); 							 		// 교육평가

			pstmt.setInt   (pidx++, box.getInt   ("p_cp_accrate")); 							// CP정산율
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_stand")); 						// 고용보험 환급액-CP교육비
			pstmt.setString(pidx++, box.getString("p_contenttype")); 							// 컨텐츠타입
			pstmt.setString(pidx++, box.getString("p_jikmu_limit")); 							// 직무신청제한
			pstmt.setString(pidx++, box.getString("p_cpsubj")); 							    // cp과정코드


			isOk        = pstmt.executeUpdate();

			/*
			// 교육주관을 선택하고 추가하는 경우는 해당 교육주관에 대해 TZ_PREVIEW에 INSERT한다.
			if ( !v_grcode.equals("ALL") ) { 
				box.put("p_grcode",v_grcode);
				box.put("p_subj",v_subj);
				isOk2   = InsertPreview(box);
			}

			// 교육주관 관리자는 해당 교육주관으로 INSERT하게 한다.
			if ( StringManager.substring(box.getSession("gadmin"),0,1).equals("H") ) { 
				this.SaveGrSubj(connMgr, v_grcode, v_subj, v_luserid); //isOk3   = 
			}

			// 관리자 일경우 N000001 (오토에버) 교육그룹으로 자동 insert 한다
			else if ( StringManager.substring(box.getSession("gadmin"),0,1).equals("A") ) { 
				this.SaveGrSubj(connMgr, "N000001", v_subj, v_luserid); //isOk4   = 
			}
			*/

			// 과목기수별  오류신고게시판 Insert : FI
			// 과목기수별 학습노트관리 Insert : SN
			// 과목기수별  FAQ Insert : FA

            // 2008.11.30 김미향 수정
            // 기존에 과정개설 목록화면에서 했던 교육그룹맵핑을 과정개설 등록, 수정화면에서 같이 입력하는것으로 변경.
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
	 * 과정코드 수정 - 독서교육
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
		// 기존 파일
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
				pstmt.setString(pidx++, ""                           );                         // 교재파일명
				pstmt.setString(pidx++, ""                           );                         // 교재파일DB명
			} else {                                                                        
				if ( !box.getRealFileName("p_file").equals("") ) {                          
					pstmt.setString(pidx++, box.getRealFileName("p_file"));                     // 교재파일명
					pstmt.setString(pidx++, box.getNewFileName ("p_file"));                     // 교재파일DB명
				} else {                                                                    
					pstmt.setString(pidx++, v_oldbookfilenamereal   );                          // 교재파일명
					pstmt.setString(pidx++, v_oldbookfilenamenew    );                          // 교재파일DB명
				}
			}

			pstmt.setInt   (pidx++, box.getInt   ("p_gradexam"          ));                     // 수료기준(평가)
			pstmt.setInt   (pidx++, box.getInt   ("p_gradreport"        ));                     // 수료기준(과제)
			//pstmt.setString(pidx++, box.getString("p_usesubjseqapproval"));                     // 기수개설주관팀장(Y/N)
			//pstmt.setString(pidx++, box.getString("p_useproposeapproval"));                     // 수강신청현업팀장(Y/N)
			//pstmt.setString(pidx++, box.getString("p_usemanagerapproval"));                     // 주관팀장수강승인(Y/N)
			//pstmt.setString(pidx++, box.getStringDefault("p_rndcreditreq"       , "0"   ));     // 학점배점(연구개발)-필수
			//pstmt.setString(pidx++, box.getStringDefault("p_rndcreditchoice"    , "0"   ));     // 학점배점(연구개발)-선택
			//pstmt.setString(pidx++, box.getStringDefault("p_rndcreditadd"       , "0"   ));     // 학점배점(연구개발)-지정가점
			//pstmt.setString(pidx++, box.getStringDefault("p_rndcreditdeduct"    , "0"   ));     // 학점배점(연구개발)-지정감점
			pstmt.setString(pidx++, box.getStringDefault("p_isessential"        , "0"   ));     // 과목구분
			pstmt.setDouble(pidx++, box.getDouble("p_score"             ));                     // 
			pstmt.setString(pidx++, box.getString("p_musertel"          ));                     // 운영담당 전화번호
			pstmt.setInt   (pidx++, box.getInt   ("p_gradftest"         ));                     // 수료기준-최종평가
			pstmt.setInt   (pidx++, box.getInt   ("p_gradhtest"         ));                     // 수료기준-형성평가
			pstmt.setString(pidx++, box.getStringDefault("p_isvisible"          , "N"   ));     // 학습자에게 보여주기
			pstmt.setString(pidx++, box.getString("p_isalledu"          ));                     // 전사교육여부
			pstmt.setString(pidx++, box.getString("p_placejh"           ));                     // 집합장소
			pstmt.setString(pidx++, box.getString("p_isintroduction"    ));                     // 학습소개 사용여부
			pstmt.setInt   (pidx++, box.getInt   ("p_eduperiod"         ));                     // 학습기간
			pstmt.setString(pidx++, v_introducefilenamereal              );                     // 과목소개 이미지
			pstmt.setString(pidx++, v_introducefilenamenew               );                     // 과목소개 이미지
			pstmt.setString(pidx++, v_informationfilenamereal            );                     // 파일(목차)
			pstmt.setString(pidx++, v_informationfilenamenew             );                     // 파일(목차)
			pstmt.setString(pidx++, box.getStringDefault("p_ischarge"           , "N"   ));     // 수강료 유/무료 구분
			pstmt.setString(pidx++, box.getStringDefault("p_isopenedu"          , "N"   ));     // 열린 교육 여부 
			//pstmt.setInt   (pidx++, box.getInt   ("p_maleassignrate"    ));                     // 수료기준-평가(필수/선택여부)
			pstmt.setString(pidx++, box.getString("p_gradftest_flag"    ));                     // 수료기준-과제(필수/선택여부)
			pstmt.setString(pidx++, box.getString("p_gradreport_flag"   ));                     // 남성 할당 비율
			pstmt.setString(pidx++, box.getString("p_isgoyong"));               				// 고용보험여부(Y/N)
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_major"));      				// 고용보험 환급액-환급액(KT)
			pstmt.setDouble(pidx++, box.getDouble("p_goyongprice_minor"));      				// 고용보험 환급액-KT교육비
			pstmt.setString(pidx++, box.getString("p_isoutsourcing"));							// 위탁과정여부(Y/N)
			pstmt.setString(pidx++, box.getString ("p_graduatednote"));							// 수료기준 비고
			pstmt.setString(pidx++, box.getString("p_isablereview"));							// 과정특성(신규,추천,HIT)
			pstmt.setString(pidx++, box.getString("p_eduurl"));									//

			pstmt.setString(pidx++, box.getString("p_subj_gu"));								// 과정구분(일반, JIT:J, 안전교육:M)
			pstmt.setString(pidx++, box.getString("p_content_cd"));								// 컨텐츠코드(Normal일경우 매핑)
			pstmt.setInt   (pidx++, box.getInt   ("p_study_count"));							// 접속횟수(수료기준)
			pstmt.setInt   (pidx++, box.getInt   ("p_reviewdays"));								// 복습가능기간

			pstmt.setString(pidx++, box.getString("p_sel_dept"));								// 부서 신청제한
			pstmt.setString(pidx++, box.getString("p_sel_post"));								// 직급 신청제한

			pstmt.setString(pidx++, box.getString("p_lev"));									// 교육수준
			pstmt.setString(pidx++, box.getString("p_gubn"));									// 이수구분
			pstmt.setString(pidx++, box.getString("p_grade"));									// 역량등급
			pstmt.setString(pidx++, box.getString("p_test"));									// 교육평가

			pstmt.setInt   (pidx++, box.getInt   ("p_cp_accrate"));								// CP정산율
			pstmt.setDouble(pidx++, box.getInt   ("p_goyongprice_stand"));						// 고용보험 환급액-CP교육비

			pstmt.setString(pidx++, box.getString("p_jikmu_limit"));							// 직무신청제한 여부
			pstmt.setString(pidx++, box.getString("p_cpsubj"));							        // CP과정코드

			pstmt.setString(pidx++, box.getString("p_subj"));									// 과정코드

			isOk 	= pstmt.executeUpdate();

			pstmt.close();

			sbSQL.setLength(0);

			// 기수에 대해 과목명 수정
			sbSQL.append(" update tz_subjseq  set subjnm = ? where subj = ? \n");

			pstmt   = connMgr.prepareStatement(sbSQL.toString());

			pstmt.setString(1, box.getString("p_subjnm" ));
			pstmt.setString(2, box.getString("p_subj"   ));

			pstmt.executeUpdate();

            // 2008.11.30 김미향 수정
            // 기존에 과정개설 목록화면에서 했던 교육그룹맵핑을 과정개설 등록, 수정화면에서 같이 입력하는것으로 변경.
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
	 * 월차별 도서 정보
	 * @param box          receive from the form object and session
	 * @return ArrayList   월차별 도서 정보
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
	 * 월차별 도서정보 저장 
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
	 * 독서교육 도서 등록회사 가져오기
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
	 * 월차별 도서 전체 보기
	 * @param box          receive from the form object and session
	 * @return ArrayList   월차별 도서 정보
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
     * 과정코드 중복확인
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
     * 과정별 직무 리스트
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
	 * 과정별 직무 저장
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
     * 과정개설시 TZ_BDS에 등록
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