// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 2005-12-05 오후 4:43:01
// Home Page : http:// members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CPEduStudentBean.java

package com.ziaan.cp;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeConfigBean;

public class CPEduStudentBean
{ 

    public CPEduStudentBean()
    { 
        try
        { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );
        } catch(Exception exception)
        { 
            exception.printStackTrace();
        }
    }

    public int getStucnt(RequestBox requestbox)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        Object obj = null;
        ListSet listset = null;
        Object obj1 = null;
        String s = "";
        Object obj2 = null;
        String v_searchtext = requestbox.getString("p_searchtext");
        String s2 = requestbox.getString("p_cp");
        String s3 = requestbox.getString("s_grcode");
        String s4 = requestbox.getString("s_gyear");
        String s5 = requestbox.getString("s_grseq");
        //String s6 = requestbox.getString("s_grcomp");
        String s6 = requestbox.getStringDefault("s_grcomp", "ALL");
        String s7 = "";
        if ( !s6.equals("ALL"))
        	s7 = s6;
            //s7 = s6.substring(0, 4);
        boolean flag = false;
        int j = 0;
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            ArrayList arraylist = new ArrayList();
            if ( s4.equals(""))
            { 
                s4 = FormatDate.getDate("yyyy");
                requestbox.put("s_gyear", s4);
            }
            s = "select \n";
            s = s + "   subj,     \n";
            s = s + "   subjnm, \n";
            s = s + "   year, \n";
            s = s + "   subjseq, \n";
            s = s + "   subjseqgr,\n";
            s = s + "   propstart, \n";
            s = s + "   propend,\n";
            s = s + "   edustart, \n";
            s = s + "   eduend, \n";
            s = s + "   cpnm, \n";
            s = s + "   cpsubjseq,\n";
            s = s + "   usercnt \n";
            s = s + " from ";
            s = s + " ( ";
            s = s + " select        \n";
            s = s + "   b.subj,     \n";
            s = s + "   b.subjnm, \n";
            s = s + "   b.year, \n";
            s = s + "   b.subjseq, \n";
            s = s + "   b.subjseqgr,\n";
            s = s + "   b.propstart, \n";
            s = s + "   b.propend,\n";
            s = s + "   b.edustart, \n";
            s = s + "   b.eduend, \n";
            s = s + "   c.cpnm, \n";
            s = s + "   b.cpsubjseq,\n";
            s = s + "   (  \n";
            s = s + "     select \n";
            s = s + "       count(x.userid) \n";
            s = s + "     from  \n";
            s = s + "       tz_student x, tz_member y \n";
            s = s + "     where                       \n";
            s = s + "       x.subj = b.subj             \n";
            s = s + "       and x.subjseq = b.subjseq   \n";
            s = s + "       and x.year = b.year         \n";
            s = s + "       and x.userid = y.userid   \n";
            if ( !s6.equals("ALL"))
            	s = s + "   and y.comp = '" + s7 + "' \n";
                //s = s + "   and substr(y.comp, 1, 4) = '" + s7 + "' \n";
            s = s + "   ) as usercnt  \n";
            s = s + " from \n";
            s = s + "   vz_scsubjseq b, \n";
            s = s + "   tz_cpinfo c\n";
            s = s + "   where \n";
            s = s + "   1=1     \n";
            s = s + "   and b.owner = c.cpseq  \n";
            if ( !s3.equals(""))
            { 
                s = s + " and b.grcode = " + SQLString.Format(s3);
                if ( !v_searchtext.equals(""))
                    s = s + " and lower(b.subjnm) like " + SQLString.Format("%" + v_searchtext.toLowerCase() + "%");
                s = s + " and b.gyear = " + SQLString.Format(s4);
                if ( !s5.equals("ALL"))
                    s = s + " and b.grseq = " + SQLString.Format(s5);
                if ( !s2.equals("ALL"))
                    s = s + " and b.owner = " + SQLString.Format(s2);
            }
            s = s + "   ) \n";
            s = s + " where \n";
            s = s + " usercnt != 0\n";
            for ( listset = dbconnectionmanager.executeQuery(s); listset.next();)
            { 
                int i = listset.getInt("usercnt");
                j += i;
            }

        } catch(Exception exception1)
        { 
            throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return j;
    }

    public ArrayList selectApprovalList(RequestBox requestbox)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        java.sql.PreparedStatement preparedstatement = null;
        ListSet listset = null;
        ArrayList arraylist = null;
        String s = "";
        Object obj = null;
        String v_searchtext = requestbox.getString("p_searchtext");
        String s2 = requestbox.getString("p_cp");
        String s3 = requestbox.getString("s_grcode");
        String s4 = requestbox.getString("s_gyear");
        String s5 = requestbox.getString("s_grseq");
        int i = requestbox.getInt("p_pageno");
        //String s6 = requestbox.getString("s_grcomp");
        String s6 = requestbox.getStringDefault("s_grcomp", "ALL");
        String s7 = "";
        String v_orderColumn   = requestbox.getString        ("p_orderColumn"        );   // 정렬할 컬럼명
        String v_orderType     = requestbox.getString        ("p_orderType"          );   // 정렬할 순서
        
        if ( !s6.equals("ALL"))
        	s7 = s6;
            //s7 = s6.substring(0, 4);
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            arraylist = new ArrayList();
            if ( s4.equals(""))
            { 
                s4 = FormatDate.getDate("yyyy");
                requestbox.put("s_gyear", s4);
            }
            s = "select \n";
            s = s + "   subj,     \n";
            s = s + "   subjnm, \n";
            s = s + "   year, \n";
            s = s + "   subjseq, \n";
            s = s + "   subjseqgr,\n";
            s = s + "   propstart, \n";
            s = s + "   propend,\n";
            s = s + "   edustart, \n";
            s = s + "   eduend, \n";
            s = s + "   cpnm, \n";
            s = s + "   cpsubjseq,\n";
            s = s + "   usercnt \n";
            s = s + " from ";
            s = s + " ( ";
            s = s + " select        \n";
            s = s + "   b.subj,     \n";
            s = s + "   b.subjnm, \n";
            s = s + "   b.year, \n";
            s = s + "   b.subjseq, \n";
            s = s + "   b.subjseqgr,\n";
            s = s + "   b.propstart, \n";
            s = s + "   b.propend,\n";
            s = s + "   b.edustart, \n";
            s = s + "   b.eduend, \n";
            s = s + "   c.cpnm, \n";
            s = s + "   b.cpsubjseq,\n";
            s = s + "   (  \n";
            s = s + "     select \n";
            s = s + "       count(x.userid) \n";
            s = s + "     from  \n";
            s = s + "       tz_student x, tz_member y \n";
            s = s + "     where                       \n";
            s = s + "       x.subj = b.subj             \n";
            s = s + "       and x.subjseq = b.subjseq   \n";
            s = s + "       and x.year = b.year         \n";
            s = s + "       and x.userid = y.userid   \n";
            if ( !s6.equals("ALL"))
            	s = s + "   and y.comp = '" + s7 + "' \n";
                //s = s + "   and substr(y.comp, 1, 4) = '" + s7 + "' \n";
            s = s + "   ) as usercnt  \n";
            s = s + " from \n";
            s = s + "   vz_scsubjseq b, \n";
            s = s + "   tz_cpinfo c\n";
            s = s + "   where \n";
            s = s + "   1=1     \n";
            s = s + "   and b.owner = c.cpseq  \n";
            if ( !s3.equals(""))
            { 
                s = s + " and b.grcode = " + SQLString.Format(s3);
                if ( !v_searchtext.equals(""))
                    s = s + " and lower(b.subjnm) like " + SQLString.Format("%" + v_searchtext.toLowerCase() + "%");
                s = s + " and b.gyear = " + SQLString.Format(s4);
                if ( !s5.equals("ALL"))
                    s = s + " and b.grseq = " + SQLString.Format(s5);
                if ( !s2.equals("ALL"))
                    s = s + " and b.owner = " + SQLString.Format(s2);
            }
            s = s + "   ) \n";
            s = s + " where \n";
            s = s + " usercnt != 0\n";
            
            if ( v_orderColumn.equals("grseq"    ) )    v_orderColumn = " C.grseq       ";
            if ( !v_orderColumn.equals("") ) { 
            	s = s + " order by " + v_orderColumn + v_orderType + " \n";
            } 
            
            preparedstatement = dbconnectionmanager.prepareStatement(s, 1004, 1008);
            listset = new ListSet(preparedstatement);
            listset.setPageSize(row);
            listset.setCurrentPage(i);
            int j = listset.getTotalPage();
            int k = listset.getTotalCount();
            com.ziaan.library.DataBox databox;
            for ( ; listset.next(); arraylist.add(databox))
            { 
                databox = listset.getDataBox();
                databox.put("d_dispnum", new Integer((k - listset.getRowNum() ) + 1));
                databox.put("d_totalpage", new Integer(j));
                databox.put("d_rowcount", new Integer(row));
            }

        } catch(Exception exception1)
        { 
            ErrorManager.getErrorStackTrace(exception1, requestbox, s);
            throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( preparedstatement != null )
                try
                { 
                    preparedstatement.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return arraylist;
    }

    public ArrayList selectApprovalUserList(RequestBox requestbox)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        Statement statement = null;
        ListSet listset = null;
        ArrayList arraylist = null;
        String s = "";
        Object obj = null;
        String s1 = requestbox.getString("p_gyear");
        String s2 = requestbox.getString("p_year");
        String s3 = requestbox.getString("p_subj");
        String s4 = requestbox.getString("p_subjseq");
        //String s5 = requestbox.getString("s_grcomp");
        String s5 = requestbox.getStringDefault("s_grcomp", "ALL");
        String s6 = "";
        if ( !s5.equals("ALL"))
        	s6 = s5;
            //s6 = s5.substring(0, 4);
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            arraylist = new ArrayList();
            s = "select       \n";
            s = s + " a.userid,   \n";
            s = s + " a.name,     \n";
            s = s + " a.email,    \n";
            s = s + " a.handphone,\n";
            //s = s + " a.comptel,  \n";
            s = s + " c.subjnm,   \n";
            s = s + " c.eduurl,   \n";
            s = s + " c.cpsubj,   \n";
            //s = s + " get_compnm(a.comp, 2, 2) compnm,\n";
            //s = s + " get_cpsubjeduurl(a.userid, b.subj, b.year, b.subjseq, 'ZZ') cpeduurl";
            s = s + " get_compnm(a.comp) compnm \n";
            //s = s + " get_cpsubjeduurl(a.userid, b.subj, b.year, b.subjseq, 'ZZ') cpeduurl";
            s = s + " from tz_member a, tz_student b, tz_subj c \n";
            s = s + " where a.userid = b.userid ";
            s = s + " and b.year = " + SQLString.Format(s2);
            s = s + " and b.subj = " + SQLString.Format(s3);
            s = s + " and b.subjseq = " + SQLString.Format(s4);
            s = s + " and b.subj = c.subj ";
            if ( !s5.equals("ALL"))
            	s = s + "   and a.comp = '" + s6 + "' \n";
            	//s = s + "   and substr(a.comp, 1, 4) = '" + s6 + "' \n";
            s = s + "order by a.name";
            System.out.println("cpstudent_sql ==  == >>  >> " + s);
            com.ziaan.library.DataBox databox;
            for ( listset = dbconnectionmanager.executeQuery(s); listset.next(); arraylist.add(databox))
            { 
                databox = listset.getDataBox();
                databox.put("d_dispnum", new Integer(listset.getRowNum() ));
            }

            String s7 = String.valueOf(listset.getRowNum() - 1);
            requestbox.put("d_totalrow", s7);
        } catch(Exception exception1)
        { 
            ErrorManager.getErrorStackTrace(exception1, requestbox, s);
            throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( statement != null )
                try
                { 
                    statement.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return arraylist;
    }

    public String selectCPseq(String s)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        ListSet listset = null;
        String s1 = "";
        Object obj = null;
        String s2 = "";
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            s1 = "select cpseq, cpnm ";
            s1 = s1 + " from tz_cpinfo ";
            s1 = s1 + " where userid = " + SQLString.Format(s);
            for ( listset = dbconnectionmanager.executeQuery(s1); listset.next();)
                s2 = listset.getString("cpseq");

        } catch(Exception exception1)
        { 
            throw new Exception("sql = " + s1 + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return s2;
    }

    public ArrayList selectCancelList(RequestBox requestbox)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        java.sql.PreparedStatement preparedstatement = null;
        ListSet listset = null;
        ArrayList arraylist = null;
        String s = "";
        Object obj = null;
        String s1 = requestbox.getString("p_searchtext");
        String s2 = requestbox.getString("p_cp");
        String s3 = requestbox.getString("s_grcode");
        String s4 = requestbox.getString("s_gyear");
        String s5 = requestbox.getString("s_grseq");
        int i = requestbox.getInt("p_pageno");
        //String s6 = requestbox.getString("s_grcomp");
        String s6 = requestbox.getStringDefault("s_grcomp", "ALL");
        String s7 = "";
        if ( !s6.equals("ALL"))
        	s7 = s6;
            //s7 = s6.substring(0, 4);
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            arraylist = new ArrayList();
            if ( s4.equals(""))
            { 
                s4 = FormatDate.getDate("yyyy");
                requestbox.put("s_gyear", s4);
            }
            s = s + " select        \n";
            s = s + "   subj,     \n";
            s = s + "   subjnm, \n";
            s = s + "   year, \n";
            s = s + "   subjseq, \n";
            s = s + "   subjseqgr,\n";
            s = s + "   propstart, \n";
            s = s + "   propend,\n";
            s = s + "   edustart, \n";
            s = s + "   eduend, \n";
            s = s + "   cpnm, \n";
            s = s + "   cpsubjseq,\n";
            s = s + "   usercnt \n";
            s = s + " from          \n";
            s = s + " (             \n";
            s = s + " select        \n";
            s = s + "   b.subj,     \n";
            s = s + "   b.subjnm, \n";
            s = s + "   b.year, \n";
            s = s + "   b.subjseq, \n";
            s = s + "   b.subjseqgr,\n";
            s = s + "   b.propstart, \n";
            s = s + "   b.propend,\n";
            s = s + "   b.edustart, \n";
            s = s + "   b.eduend, \n";
            s = s + "   c.cpnm, \n";
            s = s + "   b.cpsubjseq,\n";
            s = s + "   (  \n";
            s = s + "     select \n";
            s = s + "       count(x.userid) \n";
            s = s + "     from  \n";
            s = s + "       tz_cancel x, tz_member y \n";
            s = s + "     where                       \n";
            s = s + "       x.subj = b.subj             \n";
            s = s + "       and x.subjseq = b.subjseq   \n";
            s = s + "       and x.year = b.year         \n";
            s = s + "       and x.userid = y.userid   \n";
            if ( !s6.equals("ALL"))
                s = s + "   and y.comp = '" + s7 + "' \n";
            	//s = s + "   and substr(y.comp, 1, 4) = '" + s7 + "' \n";
            s = s + "   ) as usercnt  \n";
            s = s + " from \n";
            s = s + "   vz_scsubjseq b, \n";
            s = s + "   tz_cpinfo c\n";
            s = s + "   where \n";
            s = s + "   1=1     \n";
            s = s + "   and b.owner = c.cpseq  \n";
            if ( !s3.equals(""))
            { 
                s = s + " and b.grcode = " + SQLString.Format(s3);
                if ( !s1.equals(""))
                    s = s + " and b.subjnm like " + SQLString.Format("%" + s1 + "%");
                s = s + " and b.gyear = " + SQLString.Format(s4);
                if ( !s5.equals("ALL"))
                    s = s + " and b.grseq = " + SQLString.Format(s5);
                if ( !s2.equals("ALL"))
                    s = s + " and b.owner = " + SQLString.Format(s2);
            }
            s = s + " ) \n";
            s = s + " where \n";
            s = s + " usercnt != 0\n";
            preparedstatement = dbconnectionmanager.prepareStatement(s, 1004, 1008);
            listset = new ListSet(preparedstatement);
            listset.setPageSize(row);
            listset.setCurrentPage(i);
            int j = listset.getTotalPage();
            int k = listset.getTotalCount();
            com.ziaan.library.DataBox databox;
            for ( ; listset.next(); arraylist.add(databox))
            { 
                databox = listset.getDataBox();
                databox.put("d_dispnum", new Integer((k - listset.getRowNum() ) + 1));
                databox.put("d_totalpage", new Integer(j));
                databox.put("d_rowcount", new Integer(row));
            }

        } catch(Exception exception1)
        { 
            ErrorManager.getErrorStackTrace(exception1, requestbox, s);
            throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( preparedstatement != null )
                try
                { 
                    preparedstatement.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return arraylist;
    }
    /** 과목별 취소인원 엑셀 다운로드
     * selectCancelListExcel
     * @param requestbox
     * @return
     * @throws Exception
     */
    public ArrayList selectCancelListExcel(RequestBox requestbox)
    throws Exception
    { 
    	DBConnectionManager dbconnectionmanager = null;
    	Statement statement = null;
    	ListSet listset = null;
    	ArrayList arraylist = null;
    	String s = "";
    	Object obj = null;
    	String s1 = requestbox.getString("p_gyear");
    	String s2 = requestbox.getString("p_year");
    	String s3 = requestbox.getString("p_subj");
    	String s4 = requestbox.getString("p_subjseq");
    	//String s5 = requestbox.getString("s_grcomp");
    	String s5 = requestbox.getStringDefault("s_grcomp", "ALL");
    	String s6 = "";
    	if ( !s5.equals("ALL"))
    		s6 = s5;
    	//s6 = s5.substring(0, 4);
    	try
    	{ 
    		dbconnectionmanager = new DBConnectionManager();
    		arraylist = new ArrayList();
    		//s = "select a.userid, a.name, a.email, a.handphone, a.comptel, c.subjnm, c.eduurl, c.cpsubj, b.canceldate, b.reason ";
    		//2008.11.11 comptel 컬럼이 없음
    		s = "select a.userid, a.name, a.email, a.handphone, '' comptel, c.subjnm, c.eduurl, c.cpsubj, b.canceldate, b.reason ";
    		s = s + " from tz_member a, tz_cancel b, tz_subj c ";
    		s = s + " where a.userid = b.userid ";
    		s = s + " and b.year = " + SQLString.Format(s2);
    		s = s + " and b.subj = " + SQLString.Format(s3);
    		s = s + " and b.subjseq = " + SQLString.Format(s4);
    		s = s + " and b.subj = c.subj ";
    		if ( !s5.equals("ALL"))
    			s = s + "   and a.comp = '" + s6 + "' \n";
    		//s = s + "   and substr(a.comp, 1, 4) = '" + s6 + "' \n";
    		s = s + " order by a.name ";
    		com.ziaan.library.DataBox databox;
    		for ( listset = dbconnectionmanager.executeQuery(s); listset.next(); arraylist.add(databox))
    		{ 
    			databox = listset.getDataBox();
    			databox.put("d_dispnum", new Integer(listset.getRowNum() ));
    		}
    		
    		String s7 = String.valueOf(listset.getRowNum() - 1);
    		requestbox.put("d_totalrow", s7);
    	} catch(Exception exception1)
    	{ 
    		ErrorManager.getErrorStackTrace(exception1, requestbox, s);
    		throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
    	}
    	finally
    	{ 
    		if ( listset != null )
    			try
    		{ 
    				listset.close();
    		}
    		catch(Exception _ex) { }
    		if ( statement != null )
    			try
    		{ 
    				statement.close();
    		}
    		catch(Exception _ex) { }
    		if ( dbconnectionmanager != null )
    			try
    		{ 
    				dbconnectionmanager.freeConnection();
    		}
    		catch(Exception _ex) { }
    	}
    	
    	return arraylist;
    }
    public ArrayList selectCancelUserList(RequestBox requestbox)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        Statement statement = null;
        ListSet listset = null;
        ArrayList arraylist = null;
        String s = "";
        Object obj = null;
        String s1 = requestbox.getString("p_gyear");
        String s2 = requestbox.getString("p_year");
        String s3 = requestbox.getString("p_subj");
        String s4 = requestbox.getString("p_subjseq");
        //String s5 = requestbox.getString("s_grcomp");
        String s5 = requestbox.getStringDefault("s_grcomp", "ALL");
        String s6 = "";
        if ( !s5.equals("ALL"))
        	s6 = s5;
            //s6 = s5.substring(0, 4);
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            arraylist = new ArrayList();
            //s = "select a.userid, a.name, a.email, a.handphone, a.comptel, c.subjnm, c.eduurl, c.cpsubj, b.canceldate, b.reason ";
            //2008.11.11 comptel 컬럼이 없음
            s = "select a.userid, a.name, a.email, a.handphone, '' comptel, c.subjnm, c.eduurl, c.cpsubj, b.canceldate, b.reason ";
            s = s + " from tz_member a, tz_cancel b, tz_subj c ";
            s = s + " where a.userid = b.userid ";
            s = s + " and b.year = " + SQLString.Format(s2);
            s = s + " and b.subj = " + SQLString.Format(s3);
            s = s + " and b.subjseq = " + SQLString.Format(s4);
            s = s + " and b.subj = c.subj ";
            if ( !s5.equals("ALL"))
            	s = s + "   and a.comp = '" + s6 + "' \n";
                //s = s + "   and substr(a.comp, 1, 4) = '" + s6 + "' \n";
            s = s + " order by a.name ";
            com.ziaan.library.DataBox databox;
            for ( listset = dbconnectionmanager.executeQuery(s); listset.next(); arraylist.add(databox))
            { 
                databox = listset.getDataBox();
                databox.put("d_dispnum", new Integer(listset.getRowNum() ));
            }

            String s7 = String.valueOf(listset.getRowNum() - 1);
            requestbox.put("d_totalrow", s7);
        } catch(Exception exception1)
        { 
            ErrorManager.getErrorStackTrace(exception1, requestbox, s);
            throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( statement != null )
                try
                { 
                    statement.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return arraylist;
    }

    public ArrayList selectExcel(RequestBox requestbox)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        Statement statement = null;
        ListSet listset = null;
        ArrayList arraylist = null;
        String s = "";
        Object obj = null;
        String s1 = requestbox.getString("p_downgubun");
        String s2 = requestbox.getString("p_gyear");
        String s3 = requestbox.getString("p_year");
        String s4 = requestbox.getString("p_subj");
        String s5 = requestbox.getString("p_subjseq");
        //String s6 = requestbox.getString("s_grcomp");
        String s6 = requestbox.getStringDefault("s_grcomp", "ALL");
        String s7 = "";
        if ( !s6.equals("ALL"))
        	s7 = s6;
            //s7 = s6.substring(0, 4);
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            arraylist = new ArrayList();
            if ( s1.equals("1"))
            { 
                s = "select       \n";
                s = s + " a.userid,   \n";
                s = s + " a.name,     \n";
                s = s + " a.email,    \n";
                s = s + " a.handphone,\n";
                //s = s + " a.comptel,  \n";
                s = s + " c.subjnm,   \n";
                s = s + " c.eduurl,   \n";
                s = s + " c.cpsubj,   \n";
                s = s + " fn_crypt('2', a.birth_date, 'knise') birth_date,   \n";
                //s = s + " get_compnm(a.comp, 2, 2) compnm\n";
                s = s + " get_compnm(a.comp) compnm\n";
                s = s + " from tz_member a, tz_student b, tz_subj c \n";
                s = s + " where a.userid = b.userid ";
                s = s + " and b.year = " + SQLString.Format(s3);
                s = s + " and b.subj = " + SQLString.Format(s4);
                s = s + " and b.subjseq = " + SQLString.Format(s5);
                s = s + " and b.subj = c.subj ";
                if ( !s6.equals("ALL"))
                	s = s + "   and a.comp = '" + s7 + "' \n";
                    //s = s + "   and substr(a.comp, 1, 4) = '" + s7 + "' \n";
                s = s + "order by a.name";
            } else
            if ( s1.equals("2"))
            { 
                //s = "select a.userid, a.name, a.email, a.handphone, a.comptel, c.subjnm, c.eduurl, c.cpsubj, b.canceldate, b.reason, a.birth_date ";
                s = "select a.userid, a.name, a.email, a.handphone, c.subjnm, c.eduurl, c.cpsubj, b.canceldate, b.reason, fn_crypt('2', a.birth_date, 'knise') birth_date ";
                s = s + " from tz_member a, tz_cancel b, tz_subj c ";
                s = s + " where a.userid = b.userid ";
                s = s + " and b.year = " + SQLString.Format(s3);
                s = s + " and b.subj = " + SQLString.Format(s4);
                s = s + " and b.subjseq = " + SQLString.Format(s5);
                s = s + " and b.subj = c.subj ";
                if ( !s6.equals("ALL"))
                	s = s + "   and a.comp = '" + s7 + "' \n";
                    //s = s + "   and substr(a.comp, 1, 4) = '" + s7 + "' \n";
                s = s + " order by a.name ";
            } else
            if ( s1.equals("3"))
            { 
                s = "select a.userid, a.name, fn_crypt('2', a.birth_date, 'knise') birth_date, '' jikwinm, a.email, a.comptel, a.handphone, b.appdate as adate, c.subjnm, c.eduurl, c.cpsubj ";
                s = s + " from tz_member a, tz_propose b, tz_subj c ";
                s = s + " where a.userid = b.userid ";
                s = s + " and b.year = " + SQLString.Format(s3);
                s = s + " and b.subj = " + SQLString.Format(s4);
                s = s + " and b.subjseq = " + SQLString.Format(s5);
                s = s + " and b.subj = c.subj ";
            }
            System.out.println(s);
            com.ziaan.library.DataBox databox;
            for ( listset = dbconnectionmanager.executeQuery(s); listset.next(); arraylist.add(databox))
            { 
                databox = listset.getDataBox();
                databox.put("d_dispnum", new Integer(listset.getRowNum() ));
            }

            String s8 = String.valueOf(listset.getRowNum() - 1);
            requestbox.put("total_row_count", s8);
        } catch(Exception exception1)
        { 
            ErrorManager.getErrorStackTrace(exception1, requestbox, s);
            throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( statement != null )
                try
                { 
                    statement.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return arraylist;
    }

    public ArrayList selectProposeList(RequestBox requestbox)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        java.sql.PreparedStatement preparedstatement = null;
        ListSet listset = null;
        ArrayList arraylist = null;
        String s = "";
        Object obj = null;
        String s1 = requestbox.getString("p_searchtext");
        String s2 = requestbox.getString("p_cp");
        String s3 = requestbox.getString("s_grcode");
        String s4 = requestbox.getString("s_gyear");
        int i = requestbox.getInt("p_pageno");
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            arraylist = new ArrayList();
            if ( s4.equals(""))
            { 
                s4 = FormatDate.getDate("yyyy");
                requestbox.put("s_gyear", s4);
            }
            s = "select a.subj, a.subjnm, b.year, b.subjseq, b.propstart, b.propend, b.edustart, b.eduend, c.cpnm, b.cpsubjseq, count(d.userid) as usercnt ";
            s = s + " from tz_subj a, tz_subjseq b, tz_cpinfo c, tz_propose d ";
            s = s + " where a.subj = b.subj and ";
            s = s + " b.subj = d.subj( +) and ";
            s = s + " b.year = d.year( +) and ";
            s = s + " b.subjseq = d.subjseq( +) and ";
            if ( !s3.equals(""))
            { 
                s = s + " b.grcode = " + SQLString.Format(s3);
                s = s + " and ";
                if ( !s1.equals(""))
                { 
                    s = s + " a.subjnm like " + SQLString.Format("%" + s1 + "%");
                    if ( !s4.equals(""))
                        s = s + " and ";
                }
                if ( !s4.equals("") && !s4.equals("ALL"))
                { 
                    s = s + " b.gyear = " + SQLString.Format(s4);
                    s = s + " and ";
                } else
                if ( s4.equals(""))
                { 
                    s = s + " b.gyear = " + SQLString.Format(FormatDate.getDate("yyyy") );
                    s = s + " and ";
                }
                if ( !s2.equals("") && !s2.equals("ALL"))
                { 
                    s = s + " a.owner = " + SQLString.Format(s2);
                    s = s + " and a.owner = c.cpseq ";
                } else
                if ( !s2.equals("") || s2.equals("ALL"))
                    s = s + " a.owner = c.cpseq ";
                else
                    s = s + " a.owner = c.cpseq ";
            } else
            { 
                s = s + " b.grcode = 'zzzzzz'";
            }
            s = s + " group by a.subj, a.subjnm, b.year, b.subjseq, b.propstart, b.propend, b.edustart, b.eduend, b.cpsubjseq, c.cpnm ";
            preparedstatement = dbconnectionmanager.prepareStatement(s, 1004, 1008);
            listset = new ListSet(preparedstatement);
            listset.setPageSize(row);
            listset.setCurrentPage(i);
            int j = listset.getTotalPage();
            int k = listset.getTotalCount();
            com.ziaan.library.DataBox databox;
            for ( ; listset.next(); arraylist.add(databox))
            { 
                databox = listset.getDataBox();
                databox.put("d_dispnum", new Integer((k - listset.getRowNum() ) + 1));
                databox.put("d_totalpage", new Integer(j));
                databox.put("d_rowcount", new Integer(row));
            }

        } catch(Exception exception1)
        { 
            ErrorManager.getErrorStackTrace(exception1, requestbox, s);
            throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( preparedstatement != null )
                try
                { 
                    preparedstatement.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return arraylist;
    }

    public ArrayList selectProposeUserList(RequestBox requestbox)
        throws Exception
    { 
        DBConnectionManager dbconnectionmanager = null;
        Statement statement = null;
        ListSet listset = null;
        ArrayList arraylist = null;
        String s = "";
        Object obj = null;
        String s1 = requestbox.getString("p_gyear");
        String s2 = requestbox.getString("p_year");
        String s3 = requestbox.getString("p_subj");
        String s4 = requestbox.getString("p_subjseq");
        try
        { 
            dbconnectionmanager = new DBConnectionManager();
            arraylist = new ArrayList();
            s = "select a.userid, a.name, a.email, a.handphone, c.subjnm, c.eduurl, c.cpsubj ";
            s = s + " from tz_member a, tz_propose b, tz_subj c ";
            s = s + " where a.userid = b.userid ";
            s = s + " and b.year = " + SQLString.Format(s2);
            s = s + " and b.subj = " + SQLString.Format(s3);
            s = s + " and b.subjseq = " + SQLString.Format(s4);
            s = s + " and b.subj = c.subj ";
            com.ziaan.library.DataBox databox;
            for ( listset = dbconnectionmanager.executeQuery(s); listset.next(); arraylist.add(databox))
            { 
                databox = listset.getDataBox();
                databox.put("d_dispnum", new Integer(listset.getRowNum() ));
            }

            String s5 = String.valueOf(listset.getRowNum() - 1);
            requestbox.put("d_totalrow", s5);
        } catch(Exception exception1)
        { 
            ErrorManager.getErrorStackTrace(exception1, requestbox, s);
            throw new Exception("sql = " + s + "\r\n" + exception1.getMessage() );
        }
        finally
        { 
            if ( listset != null )
                try
                { 
                    listset.close();
                }
                catch(Exception _ex) { }
            if ( statement != null )
                try
                { 
                    statement.close();
                }
                catch(Exception _ex) { }
            if ( dbconnectionmanager != null )
                try
                { 
                    dbconnectionmanager.freeConnection();
                }
                catch(Exception _ex) { }
        }

        return arraylist;
    }

    /**
    수강확정/취소/신청 명단 엑셀 다운로드
    @param box          receive from the form object and session
    @return ArrayList	수강확정/취소/신청자 리스트
    */
    public ArrayList selectStudentExcel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String downgubun;	// 엑셀다운구분(1:확정자,2:취소자,3:신청자)
        
        downgubun = box.getString("p_downgubun");
        
        String v_syear = box.getString("p_syear");		// 수강시작년도
        String v_smon = CodeConfigBean.addZero(StringManager.toInt(box.getString("p_smon")), 2);		// 수강시작월
        String v_sday = CodeConfigBean.addZero(StringManager.toInt(box.getString("p_sday")), 2);		// 수강시작일
        
        String v_start = v_syear + v_smon + v_sday;
        
        String v_eyear = box.getString("p_eyear");		// 수강종료년도
        String v_emon = CodeConfigBean.addZero(StringManager.toInt(box.getString("p_emon")), 2);		// 수강종료월
        String v_eday = CodeConfigBean.addZero(StringManager.toInt(box.getString("p_eday")), 2);		// 수강종료일
        
        String v_end = v_eyear + v_emon + v_eday;
        
        String v_grcode = box.getString("s_grcode");
        String v_cp = box.getString("p_cp");

        if ( box.getSession("gadmin").equals("S1") ) { 
        	// 외주업체 담당자라면 해당 회사정보코드를 알아낸다.
        	v_cp = this.selectCPseq(box.getSession("userid") );
        	// v_cp = CodeConfigBean.addZero(StringManager.toInt(v_seq), 5);
    	}   
        
        try {
            System.out.println("v_start : " + v_start   );
            System.out.println("v_end   : " + v_end     );
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
            
            if ( downgubun.equals("1") ) { 
            	// 확정자 명단 다운로드
                //sql = " select a.userid, a.name, '' jikwinm, a.birth_date, a.email, a.comptel, a.handphone,                                        \n";
                sql = " select a.userid, a.name, '' jikwinm, fn_crypt('2', a.birth_date, 'knise') birth_date, a.email, a.handphone, get_compnm(a.comp) compnm, a.position_nm, a.lvl_nm,                         \n";
                sql += "   (select appdate from tz_propose x where x.subj = b.subj and x.subjseq = b.subjseq and x.year = b.year and x.userid = b.userid) as adate,     \n";
                sql += "   c.subjnm, c.subjseq, c.subjseqgr, c.cpsubjseq, c.edustart, c.eduend, d.cpnm, e.grcodenm,                                                     \n";
                sql += " '' work_plcnm                                      \n";
                sql += " from tz_member a, tz_student b, tz_subjseq c, tz_cpinfo d, tz_grcode e, tz_subj f                                                              \n";
                sql += " where a.userid = b.userid                                                                                                                      \n";
                sql += " and b.subj = c.subj                                                                                                                            \n";
                sql += " and b.subjseq = c.subjseq                                                                                                                      \n";
                sql += " and b.year = c.year                                                                                                                            \n";
                sql += " and c.subj = f.subj                                                                                                                            \n";
                sql += " and f.owner = d.cpseq                                                                                                                          \n";
                sql += " and c.grcode = e.grcode                                                                                                                        \n";
                sql += " and (                                                                                                                      \n";
                sql += "            c.edustart BETWEEN " + SQLString.Format(v_start + "00") + " AND " + SQLString.Format(v_end + "23") + "  \n";
                sql += "       and                                                                                                                  \n";
                sql += "            c.eduend BETWEEN   " + SQLString.Format(v_start + "00") + " AND " + SQLString.Format(v_end + "23") + "  \n";
                sql += "     )                                                                                                                      \n";

                /* 09.10.30 수정
                sql += " and (                                                                                                                      \n";
                sql += "            c.edustart BETWEEN " + SQLString.Format(v_start + "000000") + " AND " + SQLString.Format(v_end + "235959") + "  \n";
                sql += "       or                                                                                                                   \n";
                sql += "            c.eduend BETWEEN   " + SQLString.Format(v_start + "000000") + " AND " + SQLString.Format(v_end + "235959") + "  \n";
                sql += "     )                                                                                                                      \n";
                */

/*                
                sql += " and (                                                                                                                                          \n";
                sql += " (substr(c.edustart,1,8) <= " + SQLString.Format(v_start)   + "                                                                                 \n";
                sql += " and substr(c.eduend,1,8) >= " + SQLString.Format(v_start)  + "                                                                                 \n";
                sql += " ) or (                                                                                                                                         \n";
                sql += " substr(c.edustart,1,8) <= " + SQLString.Format(v_end)      + "                                                                                 \n";
                sql += " and substr(c.eduend,1,8) >= " + SQLString.Format(v_end)    + "                                                                                 \n";
                sql += " ) )                                                                                                                                            \n";
*/
                
	            // sql += " and b.isproposeapproval = ANY('Y','L') ";
	            // sql += " and b.chkfinal = 'Y' ";
	            // sql += " and (nvl(b.cancelkind,'zz') = 'zz' or length(ltrim(b.cancelkind)) = 0) ";
            	
            } else if ( downgubun.equals("2") ) { 
            	// 취소자 명단 다운로드

                //sql =  " select a.userid, a.name, '' orga_ename, '' jikwinm, a.birth_date, a.email, a.comptel, a.handphone,                               \n";
            	sql =  " select a.userid, a.name, '' orga_ename, '' jikwinm, fn_crypt('2', a.birth_date, 'knise') birth_date, a.email, a.handphone, get_compnm(a.comp) compnm, a.position_nm, a.lvl_nm, \n";
                sql += " b.canceldate as adate, c.subjnm, c.subjseq, c.subjseqgr, c.cpsubjseq, c.edustart, c.eduend, d.cpnm, e.grcodenm,            \n";
                sql += " '' work_plcnm                                                                                                              \n";
                sql += " from tz_member a, tz_cancel b, tz_subjseq c, tz_cpinfo d, tz_grcode e, tz_subj f                                           \n";
                sql += " where a.userid = b.userid                                                                                                  \n";
                sql += " and b.subj = c.subj                                                                                                        \n";
                sql += " and b.subjseq = c.subjseq                                                                                                  \n";
                sql += " and b.year = c.year                                                                                                        \n";
                sql += " and c.subj = f.subj                                                                                                        \n";
                sql += " and f.owner = d.cpseq                                                                                                      \n";
                sql += " and c.grcode = e.grcode                                                                                                    \n";
                sql += " and (                                                                                                                      \n";
                sql += "            c.edustart BETWEEN " + SQLString.Format(v_start + "00") + " AND " + SQLString.Format(v_end + "23") + "  \n";
                sql += "       and                                                                                                                   \n";
                sql += "            c.eduend BETWEEN   " + SQLString.Format(v_start + "00") + " AND " + SQLString.Format(v_end + "23") + "  \n";
                sql += "     )                                                                                                                      \n";
                
                /* 09.10.30 수정
                sql += " and (                                                                                                                      \n";
                sql += "            c.edustart BETWEEN " + SQLString.Format(v_start + "000000") + " AND " + SQLString.Format(v_end + "235959") + "  \n";
                sql += "       or                                                                                                                   \n";
                sql += "            c.eduend BETWEEN   " + SQLString.Format(v_start + "000000") + " AND " + SQLString.Format(v_end + "235959") + "  \n";
                sql += "     )                                                                                                                      \n";
                */

/*                
                sql += " and (                                                                                                                      \n";
                sql += " (substr(c.edustart,1,8) <= " + SQLString.Format(v_start)   + "                                                             \n";
                sql += " and substr(c.eduend,1,8) >= " + SQLString.Format(v_start)  + "                                                             \n";
                sql += " ) or (                                                                                                                     \n";
                sql += " substr(c.edustart,1,8) <= " + SQLString.Format(v_end)      + "                                                             \n";
                sql += " and substr(c.eduend,1,8) >= " + SQLString.Format(v_end)    + "                                                             \n";
                sql += " ) )";
*/                
            }            
            else if ( downgubun.equals("3") ) { 
            	// 신청자 명단 다운로드
                //sql = "  select a.userid, a.name, '' orga_ename, '' jikwinm, a.birth_date, a.email, a.comptel, a.handphone,     \n";
            	sql = "  select a.userid, a.name, '' orga_ename, '' jikwinm, fn_crypt('2', a.birth_date, 'knise') birth_date, a.email, a.handphone, get_compnm(a.comp) compnm,    \n";
                sql += " b.appdate as adate, c.subjnm, c.subjseq, c.subjseqgr, c.cpsubjseq, c.edustart, c.eduend, d.cpnm, e.grcodenm,               \n";
                sql += " '' work_plcnm                                                                                                              \n";
                sql += " from tz_member a, tz_propose b, tz_subjseq c, tz_cpinfo d, tz_grcode e, tz_subj f                                          \n";
                sql += " where a.userid = b.userid                                                                                                  \n";
                sql += " and b.subj = c.subj                                                                                                        \n";
                sql += " and b.subjseq = c.subjseq                                                                                                  \n";
                sql += " and b.year = c.year                                                                                                        \n";
                sql += " and c.subj = f.subj                                                                                                        \n";
                sql += " and f.owner = d.cpseq                                                                                                      \n";
                sql += " and c.grcode = e.grcode                                                                                                    \n";
                sql += " and (                                                                                                                      \n";
                sql += "            c.edustart BETWEEN " + SQLString.Format(v_start + "00") + " AND " + SQLString.Format(v_end + "23") + "  \n";
                sql += "       and                                                                                                                   \n";
                sql += "            c.eduend BETWEEN   " + SQLString.Format(v_start + "00") + " AND " + SQLString.Format(v_end + "23") + "  \n";
                sql += "     )                                                                                                                      \n";
                
                /* 09.10.30 수정 
                sql += " and (                                                                                                                      \n";
                sql += "            c.edustart BETWEEN " + SQLString.Format(v_start + "000000") + " AND " + SQLString.Format(v_end + "235959") + "  \n";
                sql += "       or                                                                                                                   \n";
                sql += "            c.eduend BETWEEN   " + SQLString.Format(v_start + "000000") + " AND " + SQLString.Format(v_end + "235959") + "  \n";
                sql += "     )                                                                                                                      \n";
                */

/*                
                sql += " and (                                                                                                                      \n";
                sql += " (substr(c.edustart,1,8) <= " + SQLString.Format(v_start)  + "                                                              \n";
                sql += " and substr(c.eduend,1,8) >= " + SQLString.Format(v_start) + "                                                              \n";
                sql += " ) or (                                                                                                                     \n";
                sql += " substr(c.edustart,1,8) <= " + SQLString.Format(v_end)     + "                                                              \n";
                sql += " and substr(c.eduend,1,8) >= " + SQLString.Format(v_end)   + "                                                              \n";
                sql += " ) )";
*/                
            }

            if ( !v_grcode.equals("ALL") ) { 
            	sql += " and e.grcode = " + SQLString.Format(v_grcode);
            }
            
            if ( !v_cp.equals("ALL") ) { 
            	sql += " and d.cpseq = " + SQLString.Format(v_cp);
            }
            sql += " order by e.grcode ";
//            System.out.println("cpsql ==  == =< > < > < > < > < > < > < > < > excel< > < > < > < > < > < > < > < > < > " +sql);
            
            ls = connMgr.executeQuery(sql);
			
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                dbox.put("d_dispnum", new Integer( ls.getRowNum() ));
                
                list.add(dbox);
            }
            
            String total_row_count = "" + ( ls.getRowNum() - 1);    //     전체 row 수를 반환한다
            
            box.put("total_row_count",total_row_count);
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }        //      꼭 닫아준다
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }  

    private ConfigSet config;
    private int row;
}