// **********************************************************
//  1. ��      ��: ���������� > ���ǰ�������
//  2. ���α׷�: ConcernInfoBean.java
//  3. ��      ��: �����ȳ�/��û
//  4. ȯ      ��: JDK 1.5
//  5. ��      ��: 1.0
//  6. ��      ��: 2009.09.20
//  7. ��      ��:
// **********************************************************
package com.ziaan.study;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class ConcernInfoBean { 
    private ConfigSet config;
    private int row;
    private int row_movie;
    
    public ConcernInfoBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // �� ����� �������� row ���� �����Ѵ�
            row_movie = 8;
            
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
     * ���ɰ��� ���
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList searchSubjConcern(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        String  s_userid = box.getSession("userid");
        int v_pageno = box.getInt("p_pageno");
        String s_comp = box.getSession("comp");
                
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            if("1002".equals(s_comp)) {
            sql = "select isonoff																\n"
            	+ "    , isonoffvalue															\n"            	
            	+ "    , upperclassnm															\n"
            	+ "    , middleclassnm															\n"
            	+ "    , lowerclassnm															\n"
            	+ "    , subj                                                                   \n"
            	+ "    , subjnm																	\n"
            	+ "    , status                                                                 \n"
            	+ "from (																		\n"
            	+ "    select b.isonoff															\n"
            	+ "    	   , get_codenm('0004',b.isonoff) isonoffvalue							\n"
            	+ "        , GET_SUBJCLASSKTFNM(b.upperclass_ktf, '000', '000') upperclassnm	\n"
            	+ "        , GET_SUBJCLASSKTFNM(b.upperclass_ktf, b.middleclass_ktf, '000') middleclassnm	\n"
            	+ "        , GET_SUBJCLASSKTFNM(b.upperclass_ktf, b.middleclass_ktf, b.lowerclass_ktf) lowerclassnm	\n"
            	+ "        , b.subj                                                             \n"
            	+ "        , b.subjnm															\n"
//            	+ "        , (																	\n"
//            	+ "            select case when count(subj) > 0 then max(isgraduated) else 'X' end	\n"
//            	+ "            from tz_stold														\n" 
//            	+ "            where subj = b.subj													\n"
//            	+ "            and userid = a.userid												\n"
//            	+ "            and rownum = 1														\n"
//            	+ "        ) stold_gu																\n"
//            	+ "        , (																		\n"
//            	+ "            select case when count(subj) > 0 then 'Y' else 'X' end				\n"
//            	+ "            from tz_student														\n" 
//            	+ "            where subj = b.subj													\n"
//            	+ "            and userid = a.userid												\n"
//            	+ "            and rownum = 1														\n"
//            	+ "        ) student_gu																\n"
//            	+ "        , (																		\n"
//            	+ "            select case when count(subj) > 0 then 'Y' else 'X' end				\n"
//            	+ "            from tz_propose														\n" 
//            	+ "            where subj = b.subj													\n"
//            	+ "            and userid = a.userid												\n"
//            	+ "            and rownum = 1														\n"
//            	+ "        ) propose_gu																\n"
//            	+ "        , (																		\n"
//            	+ "            select case when count(subj) > 0 and max(plan_gu)='���' then '�н����'	\n" 
//            	+ "                when count(subj) > 0 and max(plan_gu)='��ȹ' then '�н���ȹ'			\n"
//            	+ "                else 'X' end															\n"
//            	+ "            from tz_subj_plan														\n"
//            	+ "            where subj = b.subj														\n"
//            	+ "            and userid = a.userid													\n"
//            	+ "        ) plan_gu																	\n"
            	//+ "        , decode(subj_gu, 'J', 'JIT') subj_gu                       					\n"
                + "        , c.status                                                                   \n"
            	+ "    from  tz_subj_concern a,  tz_subj b												\n"
            	+ "      , ( select subj, max(status) status											\n"
            	+ "    		 from (																		\n"	
            	+ "        		select a.subj, case when d.isgraduated = 'Y' then '����'					\n"					 
            	+ "                     when d.isgraduated = 'N' then '�̼���'							\n"
            	+ "                     else ( case when d.userid is null and c.userid is not null and eduend > to_char(sysdate,'yyyymmddhh24mi') then 'ó����'	\n"
            	+ "                           when c.userid is not null and edustart <= to_char(sysdate, 'yyyymmddhh24mi') and eduend >= to_char(sysdate, 'yyyymmddhh24mi') then '����'	\n"
            	+ "                           when b.userid is not null then '������û'					\n"
            	+ "                           else ( case when plan_gu='�н����' then '�н����'			\n"
            	+ "                                       when plan_gu='�н���ȹ' then '�н���ȹ'			\n"
            	+ "                                       else '-' end) end) end status					\n"
            	+ "        		from tz_subjseq a left outer join tz_propose b							\n"
            	+ "		        on a.subj = b.subj														\n"
            	+ "  		    and a.year = b.year														\n"		
            	+ "		        and a.subjseq = b.subjseq												\n"
            	+ "     		left outer join tz_student c											\n"
            	+ "        		on b.subj = c.subj														\n"
            	+ "        		and b.year = c.year														\n"
            	+ "		        and b.subjseq = c.subjseq												\n"
            	+ "        		and b.userid = c.userid													\n"	
            	+ "		        left outer join tz_stold d												\n"
            	+ "	        	on b.subj = d.subj														\n"
            	+ "		        and b.year = d.year														\n"
            	+ "		        and b.subjseq = d.subjseq												\n"
            	+ "		        and b.userid = d.userid													\n"
            	+ "	 	        left outer join tz_subj_plan e											\n"
            	+ "		        on b.subj = e.subj														\n"
            	+ "		        and b.userid = e.userid													\n"
            	+ "        		and b.userid = " + StringManager.makeSQL(s_userid) + "                  \n"
            	+ "        ) x																			\n"
            	+ "        group by subj																\n"
            	+ "    ) c																				\n"
            	+ "    where a.subj = b.subj															\n"
            	+ "    and a.subj = c.subj                                                              \n"
    			+ "    and   b.isopenedu = 'N' 															\n"
    			+ "    and   b.isuse = 'Y' 																\n"
    			+ "    and   b.isvisible = 'Y' 															\n"
            	+ "    and   a.userid = " + StringManager.makeSQL(s_userid) + "                         \n"
            	+ ") xx																					\n";
            } else {
            	sql = "select isonoff																\n"
                	+ "    , isonoffvalue															\n"             		
                	+ "    , upperclassnm															\n"
                	+ "    , middleclassnm															\n"
                	+ "    , lowerclassnm															\n"
                	+ "    , subj                                                                   \n"
                	+ "    , subjnm																	\n"
                	+ "    , stold_gu																	\n"
                	+ "    , student_gu																	\n"
                	+ "    , propose_gu																	\n"
                	+ "    , plan_gu, year, subjseq, edustart, eduend																	\n"
                	
                	//+ "    , status                                                                 \n"
                	+ "from (																		\n"
                	+ "    select b.isonoff															\n"
                	+ "    	   , get_codenm('0004',b.isonoff) isonoffvalue						 	\n"                   	
                	+ "        , GET_SUBJCLASSNM(b.upperclass, '000', '000') upperclassnm			\n"
                	+ "        , GET_SUBJCLASSNM(b.upperclass, b.middleclass, '000') middleclassnm	\n"
                	+ "        , GET_SUBJCLASSNM(b.upperclass, b.middleclass, b.lowerclass) lowerclassnm	\n"
                	+ "        , b.subj                                                             \n"
                	+ "        , b.subjnm															\n"
                	+ "        , (																	\n"
                	+ "            select case when count(subj) > 0 then max(isgraduated) else 'X' end	\n"
                	+ "            from tz_stold														\n" 
                	+ "            where subj = b.subj													\n"
                	+ "            and userid = a.userid												\n"
                	+ "            and rownum = 1														\n"
                	+ "        ) stold_gu																\n"
                	+ "        , (																		\n"
                	+ "            select case when count(subj) > 0 then 'Y' else 'X' end				\n"
                	+ "            from tz_student														\n" 
                	+ "            where subj = b.subj													\n"
                	+ "            and userid = a.userid												\n"
                	+ "            and rownum = 1														\n"
                	+ "        ) student_gu																\n"
                	+ "        , (																		\n"
                	+ "            select case when count(subj) > 0 then 'Y' else 'X' end				\n"
                	+ "            from tz_propose														\n" 
                	+ "            where subj = b.subj													\n"
                	+ "            and userid = a.userid												\n"
                	+ "            and rownum = 1														\n"
                	+ "        ) propose_gu																\n"
                	+ "        , (																		\n"
                	+ "            select case when count(subj) > 0 and max(plan_gu)='���' then '�н����'	\n" 
                	+ "                when count(subj) > 0 and max(plan_gu)='��ȹ' then '�н���ȹ'			\n"
                	+ "                else 'X' end															\n"
                	+ "            from tz_subj_plan														\n"
                	+ "            where subj = b.subj														\n"
                	+ "            and userid = a.userid													\n"
                	+ "        ) plan_gu ,d.year, d.subjseq, d.edustart, d.eduend																	\n"
                	+ "    from  tz_subj_concern a" +
                			"    left outer join tz_subjseq d inner join tz_propose p on d.subj = p.subj and d.year=p.year and d.subjseq = p.subjseq and p.userid = " + StringManager.makeSQL(s_userid) + " on a.subj = d.subj ,  tz_subj b												\n"
/*                    + "        , c.status                                                                   \n"
                	+ "      , ( select subj, userid, min(status) status											\n"
                	+ "    		 from (																		\n"	
                	+ "        		select a.subj,b.userid, 
                					   case when d.isgraduated = 'Y' then '����'					\n"					 
                	+ "                     when d.isgraduated = 'N' then '�̼���'							\n"
                	+ "                     else ( case when d.userid is null and c.userid is not null and eduend > to_char(sysdate,'yyyymmddhh24mi') then 'ó����'	\n"
                	+ "                           when c.userid is not null and a.edustart <= to_char(sysdate, 'yyyymmddhh24mi') and a.eduend >= to_char(sysdate, 'yyyymmddhh24mi') then '����'	\n"
                	+ "                           when b.userid is not null then '������û'					\n"
                	+ "                           else ( case when plan_gu='�н����' then '�н����'			\n"
                	+ "                                       when plan_gu='�н���ȹ' then '�н���ȹ'			\n"
                	+ "                                       else '-' end) end) end status					\n"
                	+ "        		from tz_subjseq a left outer join tz_propose b							\n"
                	+ "		        on a.subj = b.subj														\n"
                	+ "  		    and a.year = b.year														\n"		
                	+ "		        and a.subjseq = b.subjseq												\n"
                	+ "     		left outer join tz_student c											\n"
                	+ "        		on b.subj = c.subj														\n"
                	+ "        		and b.year = c.year														\n"
                	+ "		        and b.subjseq = c.subjseq												\n"
                	+ "        		and b.userid = c.userid													\n"	
                	+ "		        left outer join tz_stold d												\n"
                	+ "	        	on b.subj = d.subj														\n"
                	+ "		        and b.year = d.year														\n"
                	+ "		        and b.subjseq = d.subjseq												\n"
                	+ "		        and b.userid = d.userid													\n"
                	+ "	 	        left outer join tz_subj_plan e											\n"
                	+ "		        on b.subj = e.subj														\n"
                	+ "		        and b.userid = e.userid		" 
                	+ "				and b.userid = " + StringManager.makeSQL(s_userid) + "    					\n"
                	+ "        ) x																			\n"
                	+ "        group by subj																\n"
                	+ "    ) c																				\n"*/
                	+ "    where a.subj = b.subj															\n"
//                	+ "    and a.subj = c.subj                                                              \n"
        			+ "    and   b.isopenedu = 'N' 															\n"
        			+ "    and   b.isuse = 'Y' 																\n"
        			+ "    and   b.isvisible = 'Y' 															\n"
                	+ "    and   a.userid = " + StringManager.makeSQL(s_userid) + "                         \n"
                	+ ") xx																					\n";
            }
            
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�


            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum()+1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	/**
     * ���ɰ��� ����
     * @param box      receive from the form object and session
     * @return isOk    
     * @throws Exception
     */
    public int delSubjConcern(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk_check = 0;
        int isOk       = 0;
        Vector v_checks  = new Vector();
        String v_check     = "";

        v_checks = box.getVector("p_checks");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk = 1;

            sql  = "delete from tz_subj_concern 	\n"
            	 + "where subj = ? 					\n";
            pstmt = connMgr.prepareStatement(sql);

            for(int i = 0; i < v_checks.size() ; i++){
                v_check = (String)v_checks.elementAt(i);

                pstmt.setString(1, v_check);

                isOk_check =  pstmt.executeUpdate();
                if (isOk_check == 0) isOk = 0;
            }
            if ( isOk > 0) connMgr.commit();
            else connMgr.rollback();
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch (Exception e) {}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
	    
    /**
     * �����н��ڿ� ����
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList searchCategory(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
                
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select code								\n" 
            	+ "		, codenm							\n"
            	+ "from tz_code								\n" 
            	+ "where gubun = '0093'						\n";
            
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	 /**
     * �����н��ڿ� ����
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList searchBunru(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
                
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select bunru_seq							\n"
            	+ "    , bunru								\n"
            	+ "from tz_resources_type					\n"
            	+ "where isdeleted ='N'						\n";
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	
    
    /**
     * �����н��ڿ� ���
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList searchResourcesConcern(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        String  s_userid = box.getSession("userid");
        int v_pageno = box.getInt("p_pageno");
        String v_gubun = box.getStringDefault("p_gubun", "ALL");
        String v_category = box.getStringDefault("p_category", "ALL");
                
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select b.seq											\n"
            	+ "    , b.bunru_seq									\n"
            	+ "    , b.ldate										\n" 
            	+ "    , b.visit										\n"
            	+ "    , b.title										\n"
            	+ "    , b.content										\n"
            	+ "    , a.ldate settledate								\n"
            	+ "    , bunru											\n"
            	+ "    , main_img										\n"
            	+ "    , file_directory									\n"
            	+ "    , get_codenm('0093',b.category) as categorynm	\n"
            	+ "    , width	\n"
            	+ "    , height	\n"
            	+ "from tz_resources_interest a, tz_resources_info b, tz_resources_type c	\n"
            	+ "where a.seq = b.seq														\n"
            	+ "and a.bunru_seq = b.bunru_seq											\n"
            	+ "and b.bunru_seq = c.bunru_seq											\n"
            	+ "and del_flag = 'N'														\n"
            	+ "and a.userid = " + StringManager.makeSQL(s_userid) + "                   \n";
            
            if(!"ALL".equals(v_category)) {
            	sql += "and category = " + SQLString.Format(v_category) + "                 \n";
            }
            
            if(!"ALL".equals(v_gubun)) {
            	sql += "and b.bunru_seq = " + SQLString.Format(v_gubun) + "                \n";
            }
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�


            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum()+1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	/**
    * �����н��ڿ� ����
    * @param box      receive from the form object and session
    * @return isOk    
    * @throws Exception
    */
    public int delResourcesConcern(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk_check = 0;
        int isOk       = 0;
        Vector v_checks  = new Vector();
        String v_check     = "";

        v_checks = box.getVector("p_checks");
        
        Enumeration em  = v_checks.elements();
        StringTokenizer v_token = null;
        String v_seq = "";
        String v_bunru_seq = "";
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk = 1;

            sql  = "delete from tz_resources_interest 	\n"
            	 + "where seq = ? 						\n"
            	 + "and bunru_seq = ?					\n";
            pstmt = connMgr.prepareStatement(sql);

            while ( em.hasMoreElements() ) { 
                v_token = new StringTokenizer((String)em.nextElement(), ",");
                while ( v_token.hasMoreTokens() ) { 
                    v_seq = v_token.nextToken();
                    v_bunru_seq = v_token.nextToken();

	                pstmt.setString(1, v_seq);
	                pstmt.setString(2, v_bunru_seq);
	                isOk_check =  pstmt.executeUpdate();
	                if (isOk_check == 0) isOk = 0;
	                //if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
                }
            }
            if ( isOk > 0) connMgr.commit();
            else connMgr.rollback();
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch (Exception e) {}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
    
    /**
     * ���� UCC ī�װ�
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public DataBox searchUccCategory(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        DataBox dbox        = null;
        String  sql        = "";
        String  s_userid = box.getSession("userid");
                
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "select sum(case when ismovie = 'Y' then cnt end) ismoviey			\n"
            	+ "    	, sum(case when ismovie = 'N' then cnt end) ismovien			\n"
            	+ "from (																\n"
            	+ "		SELECT ISMOVIE, COUNT(*) CNT									\n"
            	+ "		FROM   TZ_KNOWLEDGEUCC A										\n"  
            	+ "			     , TZ_KNOWLEDGEUCC_INTEREST B							\n"  
            	+ "		WHERE  A.CONTENTKEY = B.CONTENTKEY								\n"  
            	+ "		AND    A.ISDELETED != 'Y'										\n"
            	+ "		AND    B.USERID = " + StringManager.makeSQL(s_userid) + "    	\n"
               	+ "		GROUP BY A.ISMOVIE												\n"
               	+ ")																	\n";
            
            ls = connMgr.executeQuery(sql);
           
            if ( ls.next() )  
                dbox = ls.getDataBox();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
	    
    /**
	 * ���� UCC ���
	 * @param box          receive from the form object and session
	 * @return ArrayList   ���� UCC ���
     **/
    public ArrayList selectKnowledgeUCCList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	int v_pageno = box.getInt("p_pageno");
    	String s_userid = box.getSession("userid");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.CONTENTKEY  \n");
            sbSQL.append("     , A.CATEGORY  \n");
            sbSQL.append("     , GET_CODENM('0092', A.CATEGORY) AS CATEGORYNM  \n");
            sbSQL.append("     , A.TITLE  \n");
            sbSQL.append("     , A.CONTENTSUM  \n");
            sbSQL.append("     , A.TAG  \n");
            sbSQL.append("     , A.READCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_RECOMMEND  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY) AS RECOMCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_COMMENT  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY  \n");
            sbSQL.append("       AND    ISDELETED != 'Y') AS COMMENTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_INTEREST  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY) AS INTERESTCNT  \n");
            sbSQL.append("     , A.CONTENTURL  \n");
            sbSQL.append("     , A.GROUPCODE  \n");
            sbSQL.append("     , A.IDATE  \n");
            sbSQL.append("     , A.IUSERID  \n");
            sbSQL.append("     , GET_NAME(A.IUSERID) NAME  \n");
            sbSQL.append("     , B.LDATE  \n"); // ����UCC�� ������ ����
            sbSQL.append("     , A.ISMOVIE  \n"); // ����������, �Ϲ����� ����
            sbSQL.append("     , a.contentdesc \n");
    		sbSQL.append("FROM   TZ_KNOWLEDGEUCC A  \n");
    		sbSQL.append("     , TZ_KNOWLEDGEUCC_INTEREST B  \n");
    		sbSQL.append("WHERE  A.CONTENTKEY = B.CONTENTKEY  \n");
    		sbSQL.append("AND B.USERID = " + StringManager.makeSQL(s_userid) + "  \n");
    		sbSQL.append("AND    A.ISDELETED != 'Y'  \n");
    		if(box.getStringDefault("p_ismovie", "Y").equals("Y")) {
	    		sbSQL.append("AND    A.ISMOVIE = 'Y'  \n");
    		} else {
	    		sbSQL.append("AND    A.ISMOVIE = 'N'  \n");
    		}
    		sbSQL.append("ORDER BY A.IDATE DESC  \n");
    		ls = connMgr.executeQuery(sbSQL.toString());
            ls.setPageSize(box.getStringDefault("p_ismovie", "Y").equals("Y") ? row_movie : row);	// �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               // ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    // ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    // ��ü row ���� ��ȯ�Ѵ�
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(box.getStringDefault("p_ismovie", "Y").equals("Y") ? row_movie : row));
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
	    
    /**
    * ����UCC ����
    * @param box      receive from the form object and session
    * @return isOk    
    * @throws Exception
    */
    public int delUccConcern(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk_check = 0;
        int isOk       = 0;
        Vector v_checks  = new Vector();
        String v_check     = "";

        v_checks = box.getVector("p_checks");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk = 1;

            sql  = "delete from tz_knowledgeucc_interest 	\n"
            	 + "where contentkey = ? 					\n";
            pstmt = connMgr.prepareStatement(sql);

            for(int i = 0; i < v_checks.size() ; i++){
                v_check = (String)v_checks.elementAt(i);

                pstmt.setString(1, v_check);

                isOk_check =  pstmt.executeUpdate();
                if (isOk_check == 0) isOk = 0;
            }
            if ( isOk > 0) connMgr.commit();
            else connMgr.rollback();
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch (Exception e) {}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
	    
    /**
	 * ���� �������� ����Ʈ
	 * @param box          receive from the form object and session
	 * @return ArrayList   �������� ����Ʈ
     **/
    public ArrayList selectOpenSubjConcernList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	String sql1 = null;
    	String sql2 = null;
    	String v_grcode = box.getStringDefault("s_grcode", "ALL");
    	int v_pageno = box.getInt("p_pageno");
    	String v_userid = box.getSession("userid");
    	String v_jobcd = box.getSession("jobcd");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		
    		sql1= "\n select c.subj "
    			+ "\n      , c.isonoff "
    			+ "\n      , c.subjnm "
    			+ "\n      , get_subjclassnm(c.upperclass,'000','000') as upperclassnm "
    			+ "\n      , get_subjclassnm(c.upperclass,c.middleclass,'000') as middleclassnm "
    			+ "\n      , get_subjclassnm(c.upperclass,c.middleclass,c.lowerclass) as lowerclassnm "
    			+ "\n      , (select job_cd "
    			+ "\n         from   tz_subjjikmu "
    			+ "\n         where  year = '" + FormatDate.getDate("yyyy") + "' "
    			+ "\n         and    subj = c.subj "
    			+ "\n         and    job_cd = '" + v_jobcd + "') as job_cd "
    			+ "\n      , (select cnt "
    			+ "\n         from   tz_subj_cnt "
    			+ "\n         where  subj = c.subj) as cnt "
    			+ "\n      , c.intro "
    			+ "\n      , c.explain "
    			+ "\n      , c.indate "
    			+ "\n      , c.contenttype    "
    			+ "\n from   tz_subj_concern a, tz_subj c "
    			+ "\n where  a.subj = c.subj "
    			+ "\n and    a.userid = '" + v_userid+ "' "
    			+ "\n and    c.isopenedu = 'Y' "
    			+ "\n and    c.isuse = 'Y' "
    			+ "\n and    c.isvisible = 'Y' "
    			+ "\n order  by a.ldate desc ";

    		ls = connMgr.executeQuery(sql1);
            ls.setPageSize(row);					// �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);			// ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();	// ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();	// ��ü row ���� ��ȯ�Ѵ�

            while(ls.next()) {
    			dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
                
    			list.add(dbox);
    		}
    		
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sql1);
    		throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
     * ���� �������� ����
     * @param box      receive from the form object and session
     * @return isOk    
     * @throws Exception
     */
    public int delOpenSubjConcern(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk_check = 0;
        int isOk = 0;
        int pidx = 1;
        Vector v_checks  = new Vector();
        String v_check = "";
        String v_userid = box.getSession("userid");
        
        v_checks = box.getVector("p_checks");
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk = 1;

            sql = "\n delete from tz_subj_concern "
            	+ "\n where  subj = ? "
            	+ "\n and    userid = ? ";
            pstmt = connMgr.prepareStatement(sql);

            for(int i = 0; i < v_checks.size() ; i++){
                v_check = (String)v_checks.elementAt(i);

                pstmt.setString(pidx++, v_check);
                pstmt.setString(pidx++, v_userid);

                isOk_check =  pstmt.executeUpdate();
                if (isOk_check == 0) isOk = 0;
            }
            if ( isOk > 0) connMgr.commit();
            else connMgr.rollback();
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch (Exception e) {}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
    
    /**
     * ����ó������ ��������
     * @param subj
     * @param year
     * @param userid
     * @return
     * @throws Exception
     */
    public static String getStatus(String subj, String year , String subjseq, String userid) throws Exception { 
		DBConnectionManager		connMgr	= null;
        ListSet 				ls = null;
        
        String returnStr = "";
        
        StringBuffer strSQL = new StringBuffer();
        
        try { 
            connMgr = new DBConnectionManager();

            strSQL.append("select case when CANCELKIND is null then '������û' else '�ݷ�' end status") ;
            strSQL.append("      from tz_propose ") ;
            strSQL.append("     where subj = '" + subj+ "' ") ;
            strSQL.append("       and year = '" + year+ "' ") ;
            strSQL.append("       and subjseq = '" + subjseq+ "' ") ;
            strSQL.append("       and userid = '" + userid+ "' ") ;

            ls = connMgr.executeQuery(strSQL.toString());
            
            if ( ls.next() )
            {
            	returnStr = ls.getString("status");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, strSQL.toString());
            throw new Exception("sql1 = " + strSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return returnStr;
	}
	
	/**
	 * ���� �̼��� ���� ����
	 * @param subj
	 * @param year
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public static String getPassStatus(String subj, String year , String subjseq, String userid) throws Exception { 
		DBConnectionManager		connMgr	= null;
		ListSet 				ls = null;
		
		String returnStr = "";
		
		StringBuffer strSQL = new StringBuffer();
		
		try { 
			connMgr = new DBConnectionManager();
			
			strSQL.append("select case when isgraduated = 'Y' then '����' else '�̼���' end status") ;
			strSQL.append("      from tz_stold ") ;
			strSQL.append("     where subj = '" + subj+ "' ") ;
			strSQL.append("       and year = '" + year+ "' ") ;
			strSQL.append("       and subjseq = '" + subjseq+ "' ") ;
			strSQL.append("       and userid = '" + userid+ "' ") ;
			
			ls = connMgr.executeQuery(strSQL.toString());
			
			if ( ls.next() )
			{
				returnStr = ls.getString("status");
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, null, strSQL.toString());
			throw new Exception("sql1 = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return returnStr;
	}
}