// **********************************************************
//  1. ��      ��: �ڵ�� ���
//  2. ���α׷���: GetCodenm.java
//  3. ��      ��: DB�κ��� �ڵ尪 ��� Return
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: ����� 2003. 7. 23
//  7. ��      ��: 
// **********************************************************

package com.ziaan.common;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
/**
 * ���� �ڵ�� ���� ���̺귯��
 *
 * @date   : 2002. 11
 * @author : LeeSuMin
 * 
 */
public class GetCodenm { 

    /**
    �����׷�� Return
    @param String   �����׷��ڵ�
    @return String  �����׷��
    */
    public static String get_grcodenm(String p_grcode) throws Exception{ 
        return  get_grcodenm(null,p_grcode);
    }
    
    /**
    �����׷�� Return
    @param String   �����׷��ڵ�
    @param RequestBox   RequestBox
    @return String  �����׷��
    */  
    public static String get_grcodenm(RequestBox box, String p_grcode) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        String result = "";
        
        if ( box != null )  result = box.getString("grcodenm");
        
        if ( result.equals("") ) { 
            try { 
                connMgr = new DBConnectionManager();
                sql = " select  grcodenm  from tz_grcode where grcode=" +SQLString.Format(p_grcode);
                ls = connMgr.executeQuery(sql);
                if ( ls.next() )   result = ls.getString("grcodenm");
            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sql);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }
        return result;
    }
    /**
    ��������� Return
    @param String   �����׷��ڵ�
    @return String  �����׷��
    */
    public static String get_grseqnm(String p_grcode, String p_gyear, String p_grseq) throws Exception{ 
        return  get_grseqnm(null,p_grcode,p_gyear, p_grseq);
    }
    
    /**
    ��������� Return
    @param String   �����׷��ڵ�
    @param RequestBox   RequestBox
    @return String  �����׷��
    */  
    public static String get_grseqnm(RequestBox box, String p_grcode, String p_gyear, String p_grseq) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        String result = "";
        
        if ( box != null )  result = box.getString("grseqnm");
        
        if ( result.equals("") ) { 
            try { 
                connMgr = new DBConnectionManager();
                sql = " select  grseqnm  from tz_grseq where grcode=" +SQLString.Format(p_grcode)
                    + " and grseq=" +SQLString.Format(p_grseq)
                    + " and gyear=" +SQLString.Format(p_gyear);
                ls = connMgr.executeQuery(sql);
                if ( ls.next() )   result = ls.getString("grseqnm");
            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sql);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }
        return result;
    }

    /**
    �ڽ��� Return
    @param String   �ڽ��ڵ�
    @return String  �ڽ���
    */
    public static String get_coursenm(String p_course) throws Exception{ 
        return  get_coursenm(null,p_course);
    }
    
    /**
    �ڽ��� Return
    @param String   �ڽ��ڵ�
    @param RequestBox   RequestBox
    @return String  �ڽ���
    */  
    public static String get_coursenm(RequestBox box, String p_course) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        String result = "";
        
        if ( box != null )  result = box.getString("coursenm");
        
        if ( result.equals("") ) { 
            try { 
                connMgr = new DBConnectionManager();
                sql = " select  coursenm  from tz_course where course=" +SQLString.Format(p_course);
                ls = connMgr.executeQuery(sql);
                if ( ls.next() )   result = ls.getString("coursenm");
            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sql);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }
        return result;
    }   
    
    /**
     ����з��� Return
     @param subjatt �ڽ��ڵ�
     @return result  ����з���
     */
     public static String get_subjattnm(String subjatt) throws Exception{ 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String sql  = "";
         String result = "";

         try { 
             connMgr = new DBConnectionManager();
             sql = " select  classname  from tz_subjatt where upperclass=" +SQLString.Format(subjatt);
             ls = connMgr.executeQuery(sql);
             if ( ls.next() )  result = ls.getString("classname");
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return result;
     }

    /**
     ����� Return
     @param subjatt �ڽ��ڵ�
     @return result  ����з���
     */
     public static String get_subjnm(String subj) throws Exception{ 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String sql  = "";
         String result = "";

         try { 
             connMgr = new DBConnectionManager();
             sql = " select  subjnm  from tz_subj where subj=" +SQLString.Format(subj);
             ls = connMgr.executeQuery(sql);
             if ( ls.next() )  result = ls.getString("subjnm");
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return result;
     }  


    /**
     �����(�¶����׽�Ʈ) Return
     @param subjatt �ڽ��ڵ�
     @return result  ����з���
     */
     public static String get_subjnmot(String subj) throws Exception{ 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String sql  = "";
         String result = "";

         try { 
             connMgr = new DBConnectionManager();
             sql = " select  subjnm  from tz_otsubj where subj=" +SQLString.Format(subj);
             ls = connMgr.executeQuery(sql);
             if ( ls.next() )  result = ls.getString("subjnm");
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return result;
     }  

    /**
     ��������(�����׷캰) Return
     @param subjatt �ڽ��ڵ�
     @return result  ����з���
     */
     public static String get_subjseqgr(String subj, String grcode, String gyear, String subjseq) throws Exception{ 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String sql  = "";
         String result = "";

         try { 
             connMgr = new DBConnectionManager();
             sql = " select  subjseqgr  from tz_subjseq where subj=" +SQLString.Format(subj);
             sql += " and subjseq = " +SQLString.Format(subjseq);
             sql += " and grcode = " +SQLString.Format(grcode);
             sql += " and gyear = " +SQLString.Format(gyear);
             
             ls = connMgr.executeQuery(sql);
             if ( ls.next() )  result = ls.getString("subjseqgr");
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return result;
     }  
     
    /**
    Like�˻��� ���� ȸ���ڵ� ���� Return
    @param String   ȸ���ڵ�
    @return String  ����ȸ���ڵ�
    */
    public static String get_compval(String p_comp) throws Exception{ 
        String  v_compstr = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_comptype = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  comptype  from tz_comp where comp=" +SQLString.Format(p_comp);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                v_comptype = ls.getString("comptype");
                v_compstr = p_comp.substring(0,(Integer.parseInt(v_comptype)*2)) + "%"; 
            }else           v_compstr = p_comp;
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  v_compstr;
    }

    /**
    user's ȸ��code Return
    @param String   userid
    @return String  ȸ���ڵ�
    */
    public static String get_comp_userid(String p_userid) throws Exception{ 
        String  v_compstr = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_comptype = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  comp  from tz_member where userid=" +SQLString.Format(p_userid);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                v_comptype = ls.getString("comp");
            }else           v_compstr = "";
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  v_compstr;
    }
    /**
    tz_config�κ��� �� ���
    @param String   tz_config.name
    @return String tz_config.vals
    */
    public static String get_config(String p_name) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  rtrim(ltrim(vals)) VALS  from tz_config where name=" +SQLString.Format(p_name);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   v_vals = ls.getString("vals");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  v_vals;
    }

    /**
    tz_code�κ��� �ڵ�� ���
    @param String   tz_config.name
    @return String tz_config.vals
    */
    public static String get_codenm(String p_gubun, String p_code) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  rtrim(ltrim(codenm)) VALS  from tz_code "
                + "  where  gubun=" +SQLString.Format(p_gubun) + " and code=" +SQLString.Format(p_code);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   v_vals = ls.getString("vals");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  v_vals;
    }
    
    /**
    tz_code�κ��� �ڵ�� ���
    @param String   tz_config.name
    @return String tz_config.vals
    */
    public static String get_codenm(DBConnectionManager connMgr, String p_gubun, String p_code) throws Exception{ 
        String  v_vals = "";
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            sql = " select  rtrim(ltrim(codenm)) VALS  from tz_code "
                + "  where  gubun=" +SQLString.Format(p_gubun) + " and code=" +SQLString.Format(p_code);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   v_vals = ls.getString("vals");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        
        return  v_vals;
    }    

    /**
    ���ø�(lesson name) ���
    @param String   tz_config.name
    @return String tz_config.vals
    */
    public static String get_lessonnm(String p_subj, String p_lesson) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  rtrim(ltrim(sdesc)) VALS  from tz_subjlesson "
                + "  where  subj=" +SQLString.Format(p_subj) + " and lesson=" +SQLString.Format(p_lesson);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   v_vals = ls.getString("vals");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  v_vals;
    }

    /**
    Ŭ������ ���
    @param String   
    @return String 
    */
    public static String get_classnm(String p_subj, String p_year,String p_subjseq, String p_userid) throws Exception{ 
        String  v_vals = "not-exist";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  classnm from tz_student a, tz_class b "
                + "  where  a.subj=" +SQLString.Format(p_subj) + " and a.year=" +SQLString.Format(p_year)
                + "    and  a.subjseq=" +SQLString.Format(p_subjseq) + " and userid=" +SQLString.Format(p_userid)
                + "    and  a.subj=b.subj and a.year=b.year and a.subjseq=b.subjseq and a.class=b.class";
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   v_vals = ls.getString("classnm");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  v_vals;
    }


    /**
    �б��(branch name) ���
    @param String   tz_config.name
    @return String tz_config.vals
    select sdesc into v_lesson_brname from tz_subjlesson_branch where subj=p_subj and lesson=v_lesson and branch=v_curbranch;
    */
    public static String get_branchname(String p_subj, int p_branch) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  rtrim(ltrim(sdesc)) VALS  from tz_branch "
                + "  where  subj=" +SQLString.Format(p_subj) + " and branch=" +p_branch;
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   v_vals = ls.getString("vals");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  v_vals;
    }
    
    /**
    Lesson Branch Name ���
    @param String   tz_config.name
    @return String tz_config.vals
    select sdesc into v_lesson_brname from tz_subjlesson_branch where subj=p_subj and lesson=v_lesson and branch=v_curbranch;
    */
    public static String get_lessonBranchName(String p_subj, String p_lesson, int p_branch) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  rtrim(ltrim(sdesc)) VALS  from tz_subjlesson_branch "
                + "  where  subj=" +SQLString.Format(p_subj)
                + "     and  lesson=" +SQLString.Format(p_lesson)
                + "     and  branch=" +p_branch;
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   v_vals = ls.getString("vals");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  v_vals;
    }
    /**
    Lesson Branch Name ���
    @param String   tz_config.name
    @return String tz_config.vals
    select sdesc into v_lesson_brname from tz_subjlesson_branch where subj=p_subj and lesson=v_lesson and branch=v_curbranch;
    */
    public static String get_objectBranchName(
                        DBConnectionManager connMgr, 
                        String  p_subj, 
                        String  p_lesson,
                        int     p_ordering, 
                        int     p_branch) throws Exception{ 
        String  v_vals = "";
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            sql = " select  rtrim(ltrim(sdesc)) VALS  from tz_subjobj_branch "
                + "  where  subj=" +SQLString.Format(p_subj)
                + "     and  lesson=" +SQLString.Format(p_lesson)
                + "     and  branch=" +p_branch
                + "     and  ordering=" +p_ordering;
                
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   v_vals = ls.getString("vals");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        
        return  v_vals;
    }  

    /**
    ������� ���� ���� ���ɿ���
    @param String   �����ڵ�, ������ ��������, ������ ��������
    @return boolean ���ɿ���
    */
    public static boolean canChangeSubj(String p_subj, String p_gubun, String p_opt) throws Exception{ 
        boolean     result = true;
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            if ( p_gubun.equals("CA") ) {              // ��������-CA����
                sql = " select  count(*) CNTS from tz_progress where subj=" +SQLString.Format(p_subj);
                ls = connMgr.executeQuery(sql);
                ls.next();
                if ( ls.getInt("CNTS") > 0) result=false;       
            } else if ( p_gubun.equals("ProjOrd") ) {        // ���� ��������
                sql = " select  count(*) CNTS from tz_projrep where subj=" +SQLString.Format(p_subj)
                    + "   and  lesson=" +StringManager.substring(p_opt,0,2)
                    + "   and  ordseq=" +Integer.parseInt(StringManager.substring(p_opt,2));
                ls = connMgr.executeQuery(sql);
                ls.next();
                if ( ls.getInt("CNTS") > 0) result=false;
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  result;
    } 

     /**
    Conetnttype
    @param String   �����ڵ�
    @return String  ��뺸�迩��
    */
    public static String get_contenttype(String p_subj) throws Exception{ 
        String  result = "N";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  contenttype from tz_subj where subj=" +SQLString.Format(p_subj);
                
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )
            result = ls.getString("contenttype");

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  result;
    } 
     /**
    ��뺸�� ���񿩺�
    @param String   �����ڵ�
    @return String  ��뺸�迩��
    */
    public static String get_isgoyong(String p_subj) throws Exception{ 
        String  result = "N";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  nvl(isgoyong,'N') VALS from tz_subj where subj=" +SQLString.Format(p_subj);
                
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )
            result = ls.getString("VALS");

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  result;
    } 
     /**
    ��뺸�� ���񿩺�
    @param String    Connection, �����ڵ�
    @return String  ��뺸�迩��
    */
    public static String get_isgoyong(DBConnectionManager connMgr, String p_subj) throws Exception{ 
        String  result = "N";
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            sql = " select  nvl(isgoyong,'N') VALS from tz_subj where subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )
            result = ls.getString("VALS");

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        
        return  result;
    } 
     /**
    ��뺸�� ����������
    @param String   ����,�⵵,���
    @return String  ��뺸�迩��
    */
    public static String get_isgoyong_seq(String p_subj, String p_year, String p_subjseq) throws Exception{ 
        String  result = "N";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  nvl(isgoyong,'N') VALS from tz_subjseq "
                + "  where subj=" +SQLString.Format(p_subj)
                + "    and year=" +SQLString.Format(p_year)
                + "    and subjseq=" +SQLString.Format(p_subjseq);
                
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )
            result = ls.getString("VALS");

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  result;
    } 

    /**
    ��뺸�� ����������
    @param String Connection, ����,�⵵,���
    @return String  ��뺸�迩��
    */
    public static String get_isgoyong_seq(DBConnectionManager connMgr,String p_subj, String p_year, String p_subjseq) throws Exception{ 
        String result = "N";
        ListSet             ls      = null;
        String sql  = "";

        try {   
            sql = " select  nvl(isgoyong,'N') VALS from tz_subjseq "
                + "  where subj=" +SQLString.Format(p_subj)
                + "    and year=" +SQLString.Format(p_year)
                + "    and subjseq=" +SQLString.Format(p_subjseq);

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) result = ls.getString("VALS");

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );

        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return result;
    }



    /**
    ���Ʊ���� Return
    @param String       ���Ʊ���ڵ�
    @param RequestBox   RequestBox
    @return String      ���Ʊ����
    */  
    public static String get_eduseqnm(RequestBox box, String p_eduseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        String result = "";
        
        if ( box != null )  result = box.getString("eduseqnm");
        
        if ( result.equals("") ) { 
            try { 
                connMgr = new DBConnectionManager();
                sql = " select  eduseqnm  from TZ_EDUSEQ where eduseq=" +SQLString.Format(p_eduseq);
                ls = connMgr.executeQuery(sql);
                if ( ls.next() )   result = ls.getString("eduseqnm");
            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sql);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }
        return result;
    }

    /**
    ��������� Return
    @param String   �����ڵ�
    @return String  ���������
    */  
    public static String get_owner(String p_subj) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        String result = "";
        
        try { 
            connMgr = new DBConnectionManager();
            sql = " select  owner  from tz_subj where subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) result = ls.getString("owner");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    ����/�ڽ��з� Return
    @param String   ����/�ڽ� �ڵ�
    @param String   ����/�ڽ� �⵵
    @param String   ����/�ڽ� ���
    @return String  ����/�ڽ� �з�
    */  
    public static String get_upperclass(String p_subj, String p_year, String p_seq) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        String result = "";
        
        try { 
            connMgr = new DBConnectionManager();
            sql  = " select scupperclass from VZ_SCSUBJSEQ  ";
            sql += "  where scsubj=" +SQLString.Format(p_subj);
            sql += "    and scyear=" +SQLString.Format(p_year);
            sql += "    and scsubjseq=" +SQLString.Format(p_seq);

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) result = ls.getString("scupperclass");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }
    
    
    /**
     * @Description : tz_code�κ��� �ڵ�, �ڵ�� ���
     * @param p_gubun String
     * @return String select box String
     * @throws Exception
     */
    public static String getCodeInfo(String p_gubun, String selectname, String selected, boolean isALL) throws Exception{
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String  sql  = "";
        String result = "";

        try {
            connMgr = new DBConnectionManager();
            sql = " select  code, rtrim(ltrim(codenm)) codenm  from tz_code \n"
                + "  where  gubun = " + SQLString.Format(p_gubun) + "       \n";
            
            ls = connMgr.executeQuery(sql);
            
            result += getSelectTag(ls, isALL, selectname, selected);
            
        }catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return  result;
    }
    
    
    public static String getSelectTag(ListSet ls, boolean isALL, String selectname, String selected) throws Exception {
        StringBuffer sb = null;
        try {
            sb = new StringBuffer();

            sb.append("<select " + selectname + ">");
            if (isALL) {
                sb.append("<option value = \"ALL\">::��ü::</option>\r\n");
            }

            while (ls.next()) {
                ResultSetMetaData meta = ls.getMetaData();
                int columnCount = meta.getColumnCount();

                sb.append("<option value = \"" + ls.getString(1) + "\"");

                if (selected.equals(ls.getString(1))) sb.append(" selected");

                sb.append(">" + ls.getString(columnCount) + "</option>\r\n");
            }
            sb.append("</select>");
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage());
        }
        return sb.toString();
    }
    
    
    /**
     * @Description : TZ_COMPDIVI �����ڵ�, ���θ� 
     * @param p_gubun String
     * @return String select box String
     * @throws Exception
     */
    public static String getDiviInfo(String selectname, String selected, boolean isALL) throws Exception{
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String  sql  = "";
        String result = "";

        try {
            connMgr = new DBConnectionManager();
            
            sql += "select  a.legacydept, b.legacydeptnm   \n";
            sql += "from    TZ_COMPDIVI a,                 \n";
            sql += "        TZ_COMPORGA b                  \n";
            sql += "where   a.legacydept = b.legacydept    \n";
            
            ls = connMgr.executeQuery(sql);
            
            result += getSelectTag(ls, isALL, selectname, selected);
            
        }catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return  result;
    }
    
    /**
     * @Description : TZ_COMPDIVI �����ڵ�, ���θ� 
     * @param p_gubun String
     * @return String select box String
     * @throws Exception
     */
    public static String getJikwiInfo(String p_grpcomp, String selectname, String selected, boolean isALL) throws Exception{
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String  sql  = "";
        String result = "";

        try {
            connMgr = new DBConnectionManager();
            sql += "select jikwi, jikwinm                                \n";
            sql += "from tz_jikwi                                        \n";
            sql += "where grpcomp = " + SQLString.Format(p_grpcomp) + "  \n";
            
            ls = connMgr.executeQuery(sql);
            
            result += getSelectTag(ls, isALL, selectname, selected);
            
        }catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return  result;
    }
    
    
	/**
    �̸� Return
    @param String   ��� 
    */  
    public static String get_username(String p_userid) throws Exception{
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql  = "";
        String result ="";
        
        try {
            connMgr = new DBConnectionManager();
            sql  = " Select name From TZ_MEMBER ";
            sql += " Where userid = "+SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);
            if(ls.next()) result = ls.getString("name");
        }catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return result;
    }
    
    
	/**
    �Ҽ� Return
    @param String   ���
    */  
    public static String get_userdeptnm(String p_userid) throws Exception{
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql  = "";
        String result ="";
        
        try {
            connMgr = new DBConnectionManager();
            sql  = " Select deptnam From TZ_MEMBER ";
            sql += " Where userid = "+SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);
            if(ls.next()) result = ls.getString("deptnam");
        }catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return result;
    }

    /**
    �������� Ȯ��
    @param String	Connection, �����ڽ��ڵ�
    @return boolean 
    */
    public static String chkIsSubj(DBConnectionManager connMgr,String p_subj) throws Exception{
    	String result = "S";
		ListSet ls = null;
		String 	sql  = "";

		try {
			
			sql = " select count(*) cnt from tz_course where course = '"+p_subj+"'";
				
			ls = connMgr.executeQuery(sql);
			if(ls.next())
				if(ls.getInt("cnt")>0)	result = "C"; 
			
		}catch (Exception ex) {
		 	ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
		}
    	
    	return	result;
    }    	
    
    /**
    isonoff
    @param String   ��������
    @return String  
    */
    public static String get_isonoff(String p_subj) throws Exception{ 
        String  result = "N";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  isonoff from tz_subj where subj=" +SQLString.Format(p_subj);
                
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )
            result = ls.getString("isonoff");

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return  result;
    } 
    
    /**
     *  Ÿ��Ʋ ��Ī�� ���� 
     */
     public static String getTitle(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String              sql     = "";
         String v_grcodenm = "";
         String v_title = "";
         DataBox dbox = null;
         String s_grcode= box.getStringDefault("tem_grcode", box.getSession("tem_grcode"));
         String ss_site_gubun = box.getSession("s_site_gubun");

     	 try { 
             connMgr = new DBConnectionManager();

             sql = " select grcodenm 										\n"
            	 + "from tz_grcode											\n"
            	 + "where grcode=" + StringManager.makeSQL(s_grcode) + "    \n";
             
             ls = connMgr.executeQuery(sql);

             if ( ls.next() ) { 
            	 dbox = ls.getDataBox();
                 v_grcodenm = dbox.getString("d_grcodenm");
             }
             if("gate".equals(ss_site_gubun)) {
            	 v_title = v_grcodenm;
             } else {
            	 v_title = "KNISE";
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return v_title;
     }
    
}