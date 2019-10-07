// **********************************************************
//  1. 제      목: 교육기수 OPERATION BEAN
//  2. 프로그램명: GrseqBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 노희성 2004. 11. 14
//  7. 수      정:
// **********************************************************
package com.ziaan.course;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.exam.ExamPaperBean;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
public class GrseqBean { 
    public GrseqBean() { }

    
    /**
    교육기수 구성리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   교육기수 구성데이터 (과목기수 및 코스기수정보) 리스트
    */
    public ArrayList SelectGrseqScreenList(RequestBox box) throws Exception { 
    	
    	DBConnectionManager connMgr = null;
		ListSet ls = null,ls2=null;
		ArrayList list1 = null, list2=null;
		String sql  = "";
//		GrseqScreenData data = null;
		DataBox dbox = null;
		String v_grcode = box.getString("s_grcode");
		String p_gyear  = box.getString("s_gyear");
		String p_grseq  = box.getString("s_grseq");
		String v_gyear="", v_grseq="",v_subj	= "", v_year="", v_subjseq="", v_course="", v_cyear="", v_courseseq="", v_courseStr="";

		//String v_Grseqnm = box.getString("p_grtype");      
		
		String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
		String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");   // 과목분류
		String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
		String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    // 과목&코스
		
		String v_order      = box.getString("p_orderColumn");
        String v_orderType     = box.getString("p_orderType");      
        
        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = s_gadmin.substring(0, 1);

		try {
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();
			
			//지정된 과정/코스가 없는 교육차수Record Add
			sql = "select gyear, grseq, grseqnm  from tz_grseq"
				+ " where grcode="+SQLString.Format(v_grcode)
   				+ "   and ( gyear||grseq not in (select distinct gyear||grseq from tz_subjseq where grcode="+SQLString.Format(v_grcode)+")"
         		+ "         and gyear||grseq not in (select distinct gyear||grseq from tz_courseseq where grcode="+SQLString.Format(v_grcode)+") )";
	    	if (!p_gyear.equals("ALL"))
	    		sql +=" and gyear='"+p_gyear+"' ";
	    	if (!p_grseq.equals("ALL"))
	    		sql +=" and grseq='"+p_grseq+"' ";
 			sql += " order by gyear, grseq desc";
 			
			ls = connMgr.executeQuery(sql);
			
			while (ls.next()) {
				dbox = ls.getDataBox();
				list1.add(dbox);    
			}
			
			sql = " select b.gyear,b.grseq, b.subj, b.subjnm, b.year, b.subjseq, b.propstart, b.propend, b.edustart, b.eduend,"
				+ "  	   c.course, c.coursenm, c.cyear, c.courseseq, d.isonoff "
				+ "		, get_name(b.muserid) musername				\n"
            	+ "		, d.isuse									\n"
            	+ "		, get_codenm('0004', d.isonoff) isonoffnm	\n"
            	+ "     , ( 										\n"
            	+ " 			select count(*)						\n"
            	+ "				from vz_scsubjseq					\n"
            	+ "				where grcode = b.grcode				\n"
            	+ "				and gyear = b.gyear					\n"
            	+ "				and grseq = b.grseq					\n"
            	+ "		) grseq_cnt									\n"	
                + "     ,(SELECT COUNT(*)  							\n"
                + "       FROM   TZ_PROPOSE  						\n"
                + "       WHERE  SUBJ = B.SUBJ  					\n"
                + "       AND    YEAR = B.YEAR  					\n"
                + "       AND    SUBJSEQ = B.SUBJSEQ) AS PROPOSECNT \n"
                + "     ,(SELECT COUNT(*)  							\n"
                + "       FROM   TZ_CANCEL  						\n"
                + "       WHERE  SUBJ = B.SUBJ  					\n"
                + "       AND    YEAR = B.YEAR  					\n"
                + "       AND    SUBJSEQ = B.SUBJSEQ) AS CANCELCNT  \n"
                + "     ,(SELECT COUNT(*)  							\n"
                + "       FROM   TZ_STUDENT  						\n"
                + "       WHERE  SUBJ = B.SUBJ  					\n"
                + "       AND    YEAR = B.YEAR  					\n"
                + "       AND    SUBJSEQ = B.SUBJSEQ) AS STUDENTCNT \n"
                + "     ,(SELECT COUNT(*)  							\n"
                + "       FROM   TZ_STOLD							\n"
                + "       WHERE  SUBJ = B.SUBJ						\n"
                + "       AND    YEAR = B.YEAR						\n"
                + "       AND    SUBJSEQ = B.SUBJSEQ				\n"
                + "       AND    ISGRADUATED = 'Y') AS STOLDCNT  	\n"            	
	    		+ "   from vz_scsubjseq b, tz_courseseq c, tz_subj d	"
	    		+ "  where ((b.grcode=c.grcode and b.gyear=c.gyear and b.grseq=c.grseq) "
	    		+ "          or (c.gyear='0000'))"
	    		+ "    and b.course=c.course and b.cyear=c.cyear and b.courseseq=c.courseseq "
	    		+ "    and b.subj=d.subj "
	    		+ "    and b.grcode="+StringManager.makeSQL(v_grcode)+"	\n";
			
    	        if (!p_gyear.equals("ALL"))
    		         sql +=" and b.gyear='"+p_gyear+"' ";
    	        if (!p_grseq.equals("ALL"))
    		         sql +=" and b.grseq='"+p_grseq+"' ";	    	        
    	        if ( !ss_subjcourse.equals("ALL") )  
    	        	sql += "and b.scsubj = " + SQLString.Format(ss_subjcourse) + " \n";    	        
	              
	            if ( !ss_upperclass.equals("ALL") ) { 
                  if ( !ss_upperclass.equals("ALL") ) { 
                      sql += "and d.upperclass = " + SQLString.Format(ss_upperclass) + "		\n";
                  }
                  if ( !ss_middleclass.equals("ALL") ) { 
                      sql += "and d.middleclass = " + SQLString.Format(ss_middleclass) + " 	\n";
                  }
                  if ( !ss_lowerclass.equals("ALL") ) { 
                      sql += "and d.lowerclass = " + SQLString.Format(ss_lowerclass) + "		\n";
                  }
                }
	            
	            if (v_gadmin.equals("M")) {
	            	sql += " and d.cp in (select cpseq from tz_cpinfo where userid = "+StringManager.makeSQL(box.getSession("userid"))+") ";	
	            }
	            
	    	if ( v_order.equals("subjnm")    )    v_order ="b.subjnm";	
	    	if ( v_order.equals("subjseq")    )    v_order ="b.subjseq";	
	    	        
		    if ( v_order.equals("") ) { 
    	        sql += "  order by b.gyear, b.grseq, c.course, c.cyear, c.courseseq desc, c.coursenm asc, b.subj, b.subjseq asc ,b.edustart desc";         	   
		    } else { 
		        sql += " order by b.gyear, b.grseq,  c.course, c.cyear, c.courseseq desc, c.coursenm asc, " + v_order + v_orderType;
		    }            
			
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			ls = connMgr.executeQuery(sql);
		
			System.out.println("List : " + sql);
			
			while (ls.next()) { 
				dbox = ls.getDataBox();
				dbox.put("d_grcode", v_grcode);
				dbox.put("d_gyear2", ls.getString("gyear"));
				dbox.put("d_grseq2", ls.getString("grseq"));
				//dbox.put("d_rowspan_grseq", new Integer(ls.getInt("grseq_cnt")));
				dbox.put("d_grseqnm", GetCodenm.get_grseqnm(v_grcode, ls.getString("gyear"), ls.getString("grseq")));				
				list1.add(dbox);
			}
			
			/* set Rowspan values */
			list2 = new ArrayList();

            for (int i=0; i<list1.size(); i++) {
            	dbox = (DataBox)list1.get(i);

				if (dbox.getString("d_subj") != null && !dbox.getString("d_subj").equals("")) {
										
            		v_subj		= dbox.getString("d_subj");
					v_year		= dbox.getString("d_year");
					v_subjseq	= dbox.getString("d_subjseq");
					int v_cnt =0;
					
											
					if (!v_grseq.equals(dbox.getString("d_grseq")) || !v_gyear.equals(dbox.getString("d_gyear")) ){
						v_grseq 	= dbox.getString("d_grseq");
	            		v_gyear 	= dbox.getString("d_gyear");
	            		v_course	= "";
	            		
//	            		sql  = "select count(*) CNTS from tz_subjseq where grcode="+StringManager.makeSQL(v_grcode)+" and gyear="+StringManager.makeSQL(v_gyear)+" and grseq="+StringManager.makeSQL(v_grseq)+" \n";
//	            		            		
	            		sql = "select count(*) CNTS 												\n"
	            			+ "from vz_scsubjseq b, tz_courseseq c, tz_subj d						\n"
	            			+ "where ((b.grcode=c.grcode and b.gyear=c.gyear and b.grseq=c.grseq)	\n"
	            			+ "or (c.gyear='0000'))													\n"
	            			+ "and b.course = c.course												\n"
	            			+ "and b.cyear = c.cyear												\n"
	            			+ "and b.courseseq = c.courseseq										\n"
	            			+ "and b.subj = d.subj													\n"
	            			+ "and b.grcode=" + StringManager.makeSQL(v_grcode) + "					\n"
	            			+ "and b.gyear=" + StringManager.makeSQL(v_gyear) + "					\n"
	            			+ " and b.grseq="+StringManager.makeSQL(v_grseq)+"	\n";
		    	        
	            		if ( !ss_subjcourse.equals("ALL") ) { 
	            			sql += "and b.scsubj = " + SQLString.Format(ss_subjcourse) + "         \n";
	            		}
	            		
	    	            if (v_gadmin.equals("M")) {
	    	            	sql += " and d.cp in (select cpseq from tz_cpinfo where userid = "+StringManager.makeSQL(box.getSession("userid"))+") ";	
	    	            }
		              
	            		if ( !ss_upperclass.equals("ALL") ) { 
	            			if ( !ss_upperclass.equals("ALL") ) { 
	            				sql += "and d.upperclass = " + SQLString.Format(ss_upperclass) + "		\n";
	            			}
	            			if ( !ss_middleclass.equals("ALL") ) { 
	            				sql += "and d.middleclass = " + SQLString.Format(ss_middleclass) + " 	\n";
	            			}
	            			if ( !ss_lowerclass.equals("ALL") ) { 
	            				sql += "and d.lowerclass = " + SQLString.Format(ss_lowerclass) + "		\n";
	            			}
	            		}System.out.println("sql==="+sql);
	            		
	            		if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
						ls2 = connMgr.executeQuery(sql);
						ls2.next();
						v_cnt = ls2.getInt("CNTS");
												 
						if ( v_cnt != 0 ) {
							dbox.put("d_rowspan_grseq", new Integer(ls2.getInt("CNTS")));
						}else{
							dbox.put("d_rowspan_grseq", new Integer(0));
						}
						//data.setRowspan_grseq(ls2.getInt("CNTS"));
						//data.setRowspan_grseq(v_cnt);
					}


					//Course != 000000 이면 코스내의 과정차수이므로 코스 rowspan 구한다.
					v_courseStr = dbox.getString("d_course")+dbox.getString("d_cyear")+dbox.getString("d_courseseq"); 

					if (!dbox.getString("d_course").equals("000000") && !v_courseStr.equals(v_course+v_cyear+v_courseseq)){
						v_course	= dbox.getString("d_course");
	            		v_cyear 	= dbox.getString("d_cyear");
	            		v_courseseq	= dbox.getString("d_courseseq");
	            		sql = "select count(*) CNTS from vz_scsubjseq a, tz_subj b where a.subj = b.subj \n"
	            			+ " and grcode="+StringManager.makeSQL(v_grcode)+" and gyear="+StringManager.makeSQL(v_gyear)+ " \n" //" and grseq="+StringManager.makeSQL(v_grseq)+"			\n"
	            			+ " and course="+StringManager.makeSQL(v_course)+" and cyear="+StringManager.makeSQL(v_cyear)+" and courseseq="+StringManager.makeSQL(v_courseseq)+"	\n";
	            		
	            		if (!p_grseq.equals("ALL"))
		    		         sql +=" and grseq="+StringManager.makeSQL(v_grseq)+"	\n";
	            		
	            		
	            		if ( !ss_subjcourse.equals("ALL") ) { 
	            			sql += "and a.scsubj = " + SQLString.Format(ss_subjcourse) + "                    \n";
	            		}	
		              
	            		if ( !ss_upperclass.equals("ALL") ) { 
	            			if ( !ss_upperclass.equals("ALL") ) { 
	            				sql += "and b.upperclass = " + SQLString.Format(ss_upperclass) + "		\n";
	            			}
	            			if ( !ss_middleclass.equals("ALL") ) { 
	            				sql += "and b.middleclass = " + SQLString.Format(ss_middleclass) + " 	\n";
	            			}
	            			if ( !ss_lowerclass.equals("ALL") ) { 
	            				sql += "and b.lowerclass = " + SQLString.Format(ss_lowerclass) + "		\n";
	            			}
	            		}
//	            		sql = "select count(*) CNTS 												\n"
//	            			+ "from tz_subjseq b, tz_courseseq c, tz_subj d							\n"
//	            			+ "where ((b.grcode=c.grcode and b.gyear=c.gyear and b.grseq=c.grseq)	\n"
//	            			+ "or (c.gyear='0000'))													\n"
//	            			+ "and b.course = c.course												\n"
//	            			+ "and b.cyear = c.cyear												\n"
//	            			+ "and b.courseseq = c.courseseq										\n"
//	            			+ "and b.subj = d.subj													\n"
//	            			+ "and b.grcode='N000001'												\n"
//	            			+ "and b.gyear=" + StringManager.makeSQL(v_gyear) + "					\n";
//
//	            		if (!p_grseq.equals("ALL"))
//		    		         sql +=" and b.grseq='"+p_grseq+"' ";
//		    	        
//	            		if ( !ss_subjcourse.equals("ALL") ) { 
//	            			sql += "and d.subj = " + SQLString.Format(ss_subjcourse) + "                    \n";
//	            		}	
//		              
//	            		if ( !ss_upperclass.equals("ALL") ) { 
//	            			if ( !ss_upperclass.equals("ALL") ) { 
//	            				sql += "and d.upperclass = " + SQLString.Format(ss_upperclass) + "		\n";
//	            			}
//	            			if ( !ss_middleclass.equals("ALL") ) { 
//	            				sql += "and d.middleclass = " + SQLString.Format(ss_middleclass) + " 	\n";
//	            			}
//	            			if ( !ss_lowerclass.equals("ALL") ) { 
//	            				sql += "and d.lowerclass = " + SQLString.Format(ss_lowerclass) + "		\n";
//	            			}
//	            		}

	            		if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
						ls2 = connMgr.executeQuery(sql);
						ls2.next();
						dbox.put("d_rowspan_course", new Integer(ls2.getInt("CNTS")));					
					}
/*					
				  sql  = " select count(userid) CNTS from tz_propose  ";
                  sql += "  where subj||year||subjseq in (select subj||year||subjseq from tz_subjseq  ";
                  sql += "                                 where subj=" + StringManager.makeSQL(v_subj) + " and year=" + StringManager.makeSQL(v_year) + " \n";
                  sql += "                                   and grcode = " + StringManager.makeSQL(v_grcode) + " and gyear=" +StringManager.makeSQL(v_gyear) + " and grseq=" + StringManager.makeSQL(v_grseq) + " and subjseq = " + StringManager.makeSQL(v_subjseq) + " ) \n";                                   
                  if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                  ls2 = connMgr.executeQuery(sql);
                  ls2.next();
                  dbox.put("d_proposecnt" , new Integer( ls2.getInt("CNTS")));
System.out.println("sql==="+sql); 
                  sql = "select count(userid) CNTS from tz_cancel  ";
                  sql += "  where subj||year||subjseq in (select subj||year||subjseq from tz_subjseq  ";
                  sql += "                                 where subj=" + StringManager.makeSQL(v_subj) + " and year=" +StringManager.makeSQL(v_year) + "	\n";
                  sql += "                                   and grcode = " +StringManager.makeSQL(v_grcode) + " and gyear=" +StringManager.makeSQL(v_gyear) + " and grseq=" +StringManager.makeSQL(v_grseq) + " and subjseq = " + StringManager.makeSQL(v_subjseq) + " ) \n";
                  if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                  ls2 = connMgr.executeQuery(sql);
                  ls2.next();
                  dbox.put("d_cancelcnt" , new Integer( ls2.getInt("CNTS")));
System.out.println("sql==="+sql);
                  sql = "select count(userid) CNTS from tz_student ";
                  sql += "  where subj||year||subjseq in (select subj||year||subjseq from tz_subjseq  ";
                  sql += "                                 where subj=" +StringManager.makeSQL(v_subj) + " and year=" +StringManager.makeSQL(v_year) + "	\n";
                  sql += "                                   and grcode = " +StringManager.makeSQL(v_grcode) + " and gyear=" +StringManager.makeSQL(v_gyear) + " and grseq=" +StringManager.makeSQL(v_grseq) + " and subjseq = " + StringManager.makeSQL(v_subjseq) + " ) \n";
                  if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                  ls2 = connMgr.executeQuery(sql);
                  ls2.next();
                  dbox.put("d_studentcnt" , new Integer( ls2.getInt("CNTS")));
System.out.println("sql==="+sql);
                  sql   = "select count(userid) CNTS from tz_stold ";
                  sql +=  "  where ISGRADUATED='Y'                 ";
                  sql += "     and subj||year||subjseq in (select subj||year||subjseq from tz_subjseq  ";
                  sql += "                                 where subj=" +StringManager.makeSQL(v_subj) + " and year=" +StringManager.makeSQL(v_year) + "	\n";
                  sql += "                                   and grcode = " +StringManager.makeSQL(v_grcode) + " and gyear=" +StringManager.makeSQL(v_gyear) + " and grseq=" +StringManager.makeSQL(v_grseq) + " and subjseq = " + StringManager.makeSQL(v_subjseq) + " )\n";
                  if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                  ls2 = connMgr.executeQuery(sql);
                  ls2.next();
                  dbox.put("d_stoldcnt" , new Integer( ls2.getInt("CNTS")));
System.out.println("sql==="+sql);    
*/              
				}				
				list2.add(dbox);
            }
			
		}            
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list2;
	}

    
    /**
     * 교육기수 구성리스트 조회
     * @param box          receive from the form object and session
     * @return ArrayList   교육기수 구성데이터 (과목기수 및 코스기수정보) 리스트
     */
    public ArrayList SelectGrseqScreenList_original(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls = null,ls2= null;
        ArrayList list = null;
        StringBuffer sbSQL = null;
/*        ArrayList list1 = null;
        ArrayList list2= null;
        String sql  = "";
        String sql2 = "";*/
        DataBox dbox    = null;
/*        String compTxt  = null;*/
        String v_grcode = box.getString("s_grcode");
        String p_gyear  = box.getString("s_gyear");
        String p_grseq  = box.getString("s_grseq");
        
        String p_orderType = box.getString("p_orderType");
        String p_orderColumn = box.getString("p_orderColumn");

        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
        String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    // 과목&코스

/*        String v_gyear     = "";
        String v_grseq     = "";
        String v_subj      = "";
        String v_year      = "";
        String v_subjseq   = "";
        String v_course    = "";
        String v_cyear     = "";
        String v_courseseq = "";
        String v_courseStr = "";*/

        // String v_Grseqnm = box.getString("p_grtype");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT A.GRCODE  \n");
            sbSQL.append("     , A.GYEAR  \n");
            sbSQL.append("     , A.GRSEQ  \n");
            sbSQL.append("     , A.GRSEQNM  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_SUBJSEQ T1  \n");
            sbSQL.append("            , TZ_SUBJ T2  \n");
            sbSQL.append("       WHERE  T1.SUBJ = T2.SUBJ  \n");
            sbSQL.append("       AND    T1.GRCODE = A.GRCODE  \n");
            sbSQL.append("       AND    T1.GYEAR = A.GYEAR  \n");
            sbSQL.append("       AND    T1.GRSEQ = A.GRSEQ  \n");
            if(!ss_upperclass.equals("ALL")) {
            	sbSQL.append("       AND    T2.UPPERCLASS = " + SQLString.Format(ss_upperclass) + "  \n");            	
            }
            if(!ss_middleclass.equals("ALL")) {
            	sbSQL.append("       AND    T2.MIDDLECLASS = " + SQLString.Format(ss_middleclass) + "  \n");            	
            }
            if(!ss_lowerclass.equals("ALL")) {
            	sbSQL.append("       AND    T2.LOWERCLASS = " + SQLString.Format(ss_lowerclass) + "  \n");            	
            }
            if(!ss_subjcourse.equals("ALL")) {
            	sbSQL.append("       AND    T2.SUBJ = " + SQLString.Format(ss_subjcourse) + "  \n");            	
            }
            sbSQL.append("      ) AS GRSEQ_CNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_SUBJSEQ T1  \n");
            sbSQL.append("            , TZ_SUBJ T2  \n");
            sbSQL.append("       WHERE  T1.SUBJ = T2.SUBJ  \n");
            sbSQL.append("       AND    T1.GRCODE = A.GRCODE  \n");
            sbSQL.append("       AND    T1.GYEAR = A.GYEAR  \n");
            sbSQL.append("       AND    T1.GRSEQ = A.GRSEQ  \n");
            if(!ss_upperclass.equals("ALL")) {
            	sbSQL.append("       AND    T2.UPPERCLASS = " + SQLString.Format(ss_upperclass) + "  \n");            	
            }
            if(!ss_middleclass.equals("ALL")) {
            	sbSQL.append("       AND    T2.MIDDLECLASS = " + SQLString.Format(ss_middleclass) + "  \n");            	
            }
            if(!ss_lowerclass.equals("ALL")) {
            	sbSQL.append("       AND    T2.LOWERCLASS = " + SQLString.Format(ss_lowerclass) + "  \n");            	
            }
            if(!ss_subjcourse.equals("ALL")) {
            	sbSQL.append("       AND    T2.SUBJ = " + SQLString.Format(ss_subjcourse) + "  \n");            	
            }
            sbSQL.append("      ) AS ROWSPAN_GRSEQ  \n");
            sbSQL.append("     , RANK() OVER(PARTITION BY A.GYEAR, A.GRSEQ ORDER BY B.SUBJNM) AS RN  \n");
            sbSQL.append("     , C.ISONOFF  \n");
            sbSQL.append("     , GET_CODENM('0004',C.ISONOFF) AS ISONOFFNM  \n");
            sbSQL.append("     , B.SUBJ  \n");
            sbSQL.append("     , B.SUBJNM  \n");
            sbSQL.append("     , C.SUBJ_GU  \n");
            sbSQL.append("     , B.SUBJSEQ  \n");
            sbSQL.append("     , TO_NUMBER(B.SUBJSEQ) AS SUBJSEQCNT  \n");
            sbSQL.append("     , B.SUBJSEQGR  \n");
            sbSQL.append("     , B.MUSERID  \n");
            sbSQL.append("     , GET_NAME(B.MUSERID) AS MUSERNAME  \n");
            sbSQL.append("     , C.ISAPPROVAL  \n");
            sbSQL.append("     , B.ISVISIBLE  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_PROPOSE  \n");
            sbSQL.append("       WHERE  SUBJ = B.SUBJ  \n");
            sbSQL.append("       AND    YEAR = B.YEAR  \n");
            sbSQL.append("       AND    SUBJSEQ = B.SUBJSEQ) AS PROPOSECNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_CANCEL  \n");
            sbSQL.append("       WHERE  SUBJ = B.SUBJ  \n");
            sbSQL.append("       AND    YEAR = B.YEAR  \n");
            sbSQL.append("       AND    SUBJSEQ = B.SUBJSEQ) AS CANCELCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_STUDENT  \n");
            sbSQL.append("       WHERE  SUBJ = B.SUBJ  \n");
            sbSQL.append("       AND    YEAR = B.YEAR  \n");
            sbSQL.append("       AND    SUBJSEQ = B.SUBJSEQ) AS STUDENTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_STOLD  \n");
            sbSQL.append("       WHERE  SUBJ = B.SUBJ  \n");
            sbSQL.append("       AND    YEAR = B.YEAR  \n");
            sbSQL.append("       AND    SUBJSEQ = B.SUBJSEQ  \n");
            sbSQL.append("       AND    ISGRADUATED = 'Y') AS STOLDCNT  \n");
            sbSQL.append("     , C.SUBJCLASS  \n");
            sbSQL.append("     , C.UPPERCLASS  \n");
            sbSQL.append("     , C.MIDDLECLASS  \n");
            sbSQL.append("     , C.LOWERCLASS  \n");
            sbSQL.append("     , C.ISUSE  \n");
            sbSQL.append("FROM   TZ_GRSEQ A  \n");
            sbSQL.append("     , TZ_SUBJSEQ B  \n");
            sbSQL.append("     , TZ_SUBJ C  \n");
            sbSQL.append("WHERE  A.GRCODE = B.GRCODE (+)  \n");
            sbSQL.append("AND    A.GYEAR = B.GYEAR (+)  \n");
            sbSQL.append("AND    A.GRSEQ = B.GRSEQ (+)  \n");
            sbSQL.append("AND    B.SUBJ = C.SUBJ (+)  \n");
            sbSQL.append("AND    A.GRCODE = " + SQLString.Format(v_grcode) + "  \n");
            sbSQL.append("AND    A.GYEAR = " + SQLString.Format(p_gyear) + "  \n");
            if(!p_grseq.equals("ALL")) {
            	sbSQL.append("AND    A.GRSEQ = " + SQLString.Format(p_grseq) + "  \n");
            }
            if(!ss_upperclass.equals("ALL")) {
            	sbSQL.append("AND    C.UPPERCLASS = " + SQLString.Format(ss_upperclass) + "  \n");            	
            }
            if(!ss_middleclass.equals("ALL")) {
            	sbSQL.append("AND    C.MIDDLECLASS = " + SQLString.Format(ss_middleclass) + "  \n");            	
            }
            if(!ss_lowerclass.equals("ALL")) {
            	sbSQL.append("AND    C.LOWERCLASS = " + SQLString.Format(ss_lowerclass) + "  \n");            	
            }
            if(!ss_subjcourse.equals("ALL")) {
            	sbSQL.append("AND    C.SUBJ = " + SQLString.Format(ss_subjcourse) + "  \n");            	
            }
            
            /*
             * 소팅
             */
            if(p_orderColumn.equals("")){
            	sbSQL.append("ORDER  BY A.GYEAR DESC, A.GRSEQ DESC, B.SUBJNM, B.SUBJSEQ  \n");
            }
            else{
            	sbSQL.append("ORDER  BY "+p_orderColumn+" "+p_orderType+", A.GRSEQ DESC, B.SUBJNM, B.SUBJSEQ  \n");
            }
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
            
            /*
            list1 = new ArrayList();

            // 지정된 과정이 없는 교육기수Record Add
            sql = "select gyear, grseq, grseqnm  \n"
            	+ "from   tz_grseq \n"
                + "where  grcode = " +SQLString.Format(v_grcode) + " \n"
                + "and   (gyear||grseq \n"
                + "           not in (select distinct gyear||grseq \n"
                + "                   from   tz_subjseq \n"
                + "                   where  grcode=" +SQLString.Format(v_grcode) + ") \n"
                + "      ) \n";
            
            if ( !p_gyear.equals("ALL")) {
                sql +="and    gyear = '" +p_gyear + "' \n";
            }
            if ( !p_grseq.equals("ALL")) {
                sql +="and    grseq = '" +p_grseq + "' \n";
            }
            sql += "order  by gyear, grseq desc \n";
            
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox=ls.getDataBox();
                dbox.put("d_grcode" , v_grcode);
                dbox.put("d_grseqnm" , GetCodenm.get_grseqnm(v_grcode, dbox.getString("d_gyear"), dbox.getString("d_grseq")));
                list1.add(dbox);
            }

            sql = "\n select a.subj "
            	+ "\n      , a.subjnm "
            	+ "\n      , a.isonoff "
            	+ "\n      , a.subj_gu "
            	+ "\n      , a.subjclass "
            	+ "\n      , a.upperclass "
            	+ "\n      , a.middleclass "
            	+ "\n      , a.lowerclass "
            	+ "\n      , a.muserid "
            	+ "\n      , get_name(b.muserid) as musername "
	            + "\n      , a.isuse "
	            + "\n      , a.isonoff "
	            + "\n      , decode(a.isonoff,'ON','이러닝','OFF','집합','RC','독서교육','') as isonoffnm "
	            + "\n      , a.isapproval "
	            + "\n      , b.year "
	            + "\n      , b.subjseqcnt "
	            + "\n      , b.gyear "
	            + "\n      , b.grseq "
	            + "\n      , (select grseqnm from tz_grseq where grcode = b.grcode and gyear = b.gyear and grseq = b.grseq) as grseqnm "
	            + "\n      , (select count(*)			"
	            + "\n		    from   vz_scsubjseq		"
	            + "\n		    where  grcode = b.grcode "
	            + "\n		    and    gyear = b.gyear "
	            + "\n		    and    grseq = b.grseq "
	            + "\n		   ) as grseq_cnt "			
	            + "\n from   tz_subj a "
	            + "\n      , (select grcode, subj, year, gyear, grseq, max(to_number(subjseq)) as subjseqcnt, max(muserid) as muserid "
	            + "\n         from   tz_subjseq "
	            + "\n         where  grcode = '" +v_grcode + "' "
	            + "\n         group  by grcode, subj, year, gyear, grseq ) b "
	            + "\n where  a.subj = b.subj ";

            if ( !p_gyear.equals("ALL"))
                sql +="\n and    b.gyear='" +p_gyear + "' ";
            if ( !p_grseq.equals("ALL"))
                sql +="\n and    b.grseq='" +p_grseq + "' ";

            if ( !ss_subjcourse.equals("ALL") ) { 
                sql += "\n and    a.subj = " + SQLString.Format(ss_subjcourse) + " ";
            } else { 
                if ( !ss_upperclass.equals("ALL") ) { 
                    if ( !ss_upperclass.equals("ALL") ) { 
                        sql += "\n and    a.upperclass = " +SQLString.Format(ss_upperclass)+ " ";
                    }
                    if ( !ss_middleclass.equals("ALL") ) { 
                        sql += "\n and    a.middleclass = " +SQLString.Format(ss_middleclass)+ " ";
                    }
                    if ( !ss_lowerclass.equals("ALL") ) { 
                        sql += "\n and    a.lowerclass = " +SQLString.Format(ss_lowerclass)+ " ";
                    }
                }
            }

            sql += "\n order  by b.gyear desc, b.grseq desc, a.subjnm ";

            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_grcode" , v_grcode);

                list1.add(dbox);
            }

            /* set Rowspan values *//*
            list2 = new ArrayList();

            for ( int i = 0; i<list1.size(); i++ ) { 
                dbox = (DataBox)list1.get(i);

                if ( dbox.getString("d_subj") != null && !dbox.getString("d_subj").equals("") ) { 
                    v_subj      = dbox.getString("d_subj");
                    v_year      = dbox.getString("d_year");

                    if ( !v_grseq.equals(dbox.getString("d_grseq")) || !v_gyear.equals(dbox.getString("d_gyear")) ) { 
                        v_gyear     = dbox.getString("d_gyear");
                        v_grseq     = dbox.getString("d_grseq");

                        sql = "\n select count( distinct subj) CNTS "
                        	+ "\n from   vz_scsubjseq "
                        	+ "\n where  grcode = '" +v_grcode + "' "
                        	+ "\n and    gyear = '" +v_gyear + "' "
                        	+ "\n and    grseq='" +v_grseq + "'";
                        if ( !ss_subjcourse.equals("ALL") ) { 
                            sql += "\n and    subj = " + SQLString.Format(ss_subjcourse) ;
                        } else { 
                            if ( !ss_upperclass.equals("ALL") ) { 
                                if ( !ss_upperclass.equals("ALL") ) { 
                                    sql += "\n and    scupperclass = " +SQLString.Format(ss_upperclass) ;
                                }
                                if ( !ss_middleclass.equals("ALL") ) { 
                                    sql += "\n and    scmiddleclass = " +SQLString.Format(ss_middleclass) ;
                                }
                                if ( !ss_lowerclass.equals("ALL") ) { 
                                    sql += "\n and    sclowerclass = " +SQLString.Format(ss_lowerclass) ;
                                }
                            }
                        }
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                        ls2 = connMgr.executeQuery(sql);
                        ls2.next();
                        dbox.put("d_rowspan_grseq" , new Integer( ls2.getInt("CNTS")));
                    }

                    /*
                    sql  = " select count(userid) CNTS from tz_propose  \n";
                    sql += " where  subj||year||subjseq in (select subj||year||subjseq from tz_subjseq  \n";
                    sql += "                                where  subj='" +v_subj + "' and year='" +v_year + "' \n";
                    sql += "                                and    grcode = '" +v_grcode + "' and gyear='" +v_gyear + "' and grseq='" +v_grseq + "') \n";
                    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    ls2 = connMgr.executeQuery(sql);
                    ls2.next();
                    dbox.put("d_proposecnt" , new Integer( ls2.getInt("CNTS")));

                    sql  = " select count(userid) CNTS from tz_cancel  ";
                    sql += " where  subj||year||subjseq in (select subj||year||subjseq from tz_subjseq  \n";
                    sql += "                                where subj='" +v_subj + "' and year='" +v_year + "' \n";
                    sql += "                                and grcode = '" +v_grcode + "' and gyear='" +v_gyear + "' and grseq='" +v_grseq + "') \n";
                    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    ls2 = connMgr.executeQuery(sql);
                    ls2.next();
                    dbox.put("d_cancelcnt" , new Integer( ls2.getInt("CNTS")));

                    sql  = " select count(userid) CNTS from tz_student \n";
                    sql += " where  subj||year||subjseq in (select subj||year||subjseq from tz_subjseq  \n";
                    sql += "                                where subj='" +v_subj + "' and year='" +v_year + "' \n";
                    sql += "                                and grcode = '" +v_grcode + "' and gyear='" +v_gyear + "' and grseq='" +v_grseq + "') \n";
                    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    ls2 = connMgr.executeQuery(sql);
                    ls2.next();
                    dbox.put("d_studentcnt" , new Integer( ls2.getInt("CNTS")));

                    sql  = " select count(userid) CNTS from tz_stold \n";
                    sql += " where  isgraduated = 'Y' \n";
                    sql += " and    subj||year||subjseq in (select subj||year||subjseq from tz_subjseq  \n";
                    sql += "                                where  subj='" +v_subj + "' and year='" +v_year + "' \n";
                    sql += "                                and    grcode = '" +v_grcode + "' and gyear='" +v_gyear + "' and grseq='" +v_grseq + "') \n";
                    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    ls2 = connMgr.executeQuery(sql);
                    ls2.next();
                    dbox.put("d_stoldcnt" , new Integer( ls2.getInt("CNTS")));
                    */
                    /*
                    sql = "select ( "
                    	+ "        select count(userid) CNTS from tz_propose "
                    	+ "        where  subj || year || subjseq in (select subj || year || subjseq from tz_subjseq "
                    	+ "                                           where  subj='" +v_subj + "' and year='" +v_year + "' "
                    	+ "                                           and    grcode = '" +v_grcode + "' and gyear='" +v_gyear + "' and grseq='" +v_grseq + "') "
                    	+ "       ) as propose_cnt "
                    	+ "     , ( "
                    	+ "        select count(userid) CNTS from tz_cancel "
                    	+ "        where  subj || year || subjseq in (select subj || year || subjseq from tz_subjseq "
                    	+ "                                           where  subj='" +v_subj + "' and year='" +v_year + "' "
                    	+ "                                           and    grcode = '" +v_grcode + "' and gyear='" +v_gyear + "' and grseq='" +v_grseq + "') "
                    	+ "       ) as cancel_cnt "
                    	+ "     , ( "
                    	+ "        select count(userid) CNTS from tz_student "
                    	+ "        where  subj || year || subjseq in (select subj || year || subjseq from tz_subjseq "
                    	+ "                                           where  subj='" +v_subj + "' and year='" +v_year + "' "
                    	+ "                                           and    grcode = '" +v_grcode + "' and gyear='" +v_gyear + "' and grseq='" +v_grseq + "') "
                    	+ "       ) as student_cnt "
                    	+ "     , ( "
                    	+ "        select count(userid) CNTS from tz_stold "
                    	+ "        where  isgraduated = 'Y' "
                    	+ "        and    subj || year || subjseq in (select subj || year || subjseq from tz_subjseq "
                    	+ "                                           where  subj='" +v_subj + "' and year='" +v_year + "' "
                    	+ "                                           and    grcode = '" +v_grcode + "' and gyear='" +v_gyear + "' and grseq='" +v_grseq + "') "
                    	+ "       ) as stold_cnt "
                    	+ "from   dual ";
                    
                    if ( ls2 != null ) { 
                    	try { 
                    		ls2.close(); 
                    	} catch ( Exception e ) { } 
                    }
                    ls2 = connMgr.executeQuery(sql);
                    ls2.next();
                    dbox.put("d_proposecnt" , new Integer(ls2.getInt("propose_cnt")));
                    dbox.put("d_cancelcnt"  , new Integer(ls2.getInt("cancel_cnt")));
                    dbox.put("d_studentcnt" , new Integer(ls2.getInt("student_cnt")));
                    dbox.put("d_stoldcnt"   , new Integer(ls2.getInt("stold_cnt")));
                    
                }
                list2.add(dbox);
            }
            */
            
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
     * 교육기수 세부 구성리스트 조회
     * @param box          receive from the form object and session
     * @return ArrayList   과목기수 구성데이터 리스트
     */
    public ArrayList SelectGrseqDetailList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        SubjectData data = null;
        DataBox             dbox    = null;

        String  ss_grcode       = box.getStringDefault("s_grcode","ALL");          // 교육주관
        String  ss_gyear        = box.getStringDefault("s_gyear","ALL");          // 교육주관
        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
        String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    // 과목&코스

        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

        String v_grseq  = box.getString("p_mgrseq");
        String v_subj   = box.getString("p_subj");


        try { 
                sql  = " select distinct ";
                sql += "        a.subj, a.subjnm, a.subjseq, a.subjseqgr, a.year, a.propstart, a.propend  ";
                sql += "      , a.edustart, a.eduend, a.studentlimit ";
                sql += "      , (select isonoff from tz_subj where subj=a.subj) isonoff ";
                sql += "      , (select count(*) from tz_propose where subj = a.subj and year = a.year and subjseq=a.subjseq) proposecnt ";
                sql += "      , (select count(*) from tz_cancel  where subj = a.subj and year = a.year and subjseq=a.subjseq) cancelcnt ";
                sql += "      , (select count(*) from tz_student where subj = a.subj and year = a.year and subjseq=a.subjseq) studentcnt ";
                sql += "      , (select count(*) from tz_stold   where subj = a.subj and year = a.year and subjseq=a.subjseq and isgraduated = 'Y' ) stoldcnt ";
                sql += "      , a.isdeleted ";	// 폐강여부
                sql += "      , a.muserid ";	// 담당자
                sql += "      , get_name(a.muserid) as musernm ";	// 담당자명
                sql += "      , case ";
                sql += "            when a.edustart <= to_char(sysdate,'yyyymmddhh') and a.eduend  > to_char(sysdate,'yyyymmddhh') then ";
                sql += "                 '4' ";
                sql += "            when a.eduend <= to_char(sysdate,'yyyymmddhh') then ";
                sql += "                 '5' ";
                sql += "            when to_char(sysdate,'yyyymmddhh') < a.propstart then ";
                sql += "                 '1' ";
                sql += "            when a.propstart <= to_char(sysdate,'yyyymmddhh') and a.propend  > to_char(sysdate,'yyyymmddhh') then ";
                sql += "                 '2' ";
                sql += "            when a.propend <= to_char(sysdate,'yyyymmddhh') and a.edustart  > to_char(sysdate,'yyyymmddhh') then ";
                sql += "                 '3' ";
                sql += "            when a.propstart is null and a.propend is null then ";
                sql += "                 '0' ";
                sql += "        end as eduterm ";
                sql += "   from tz_subjseq a  ";
                sql += "  where a.grcode = '" + ss_grcode + "' ";
                sql += "    and a.subj   = '" + v_subj + "' ";
                sql += "    and a.year   = '" + ss_gyear + "' ";
                sql += "    and a.grseq  = '" + v_grseq + "' ";

                if ( v_orderColumn.equals("") ) { 
                    sql += " order by 1,4";
                } else { 
                    sql += " order by " + v_orderColumn + v_orderType;
                }

            connMgr = new DBConnectionManager();
            list = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("subj"         , ls.getString("subj") );
                dbox.put("subjnm"       , ls.getString("subjnm") );
                dbox.put("subjseq"      , ls.getString("subjseq") );
                dbox.put("subjseqgr"    , ls.getString("subjseqgr") );
                dbox.put("year"         , ls.getString("year") );
                dbox.put("propstart"    , ls.getString("propstart") );
                dbox.put("propend"      , ls.getString("propend") );
                dbox.put("edustart"     , ls.getString("edustart") );
                dbox.put("eduend"       , ls.getString("eduend") );
                dbox.put("isonoff"      , ls.getString("isonoff") );
                dbox.put("studentlimit" , new Integer( ls.getInt("studentlimit")));
                dbox.put("d_proposecnt" , new Integer( ls.getInt("proposecnt")));
                dbox.put("d_cancelcnt"  , new Integer( ls.getInt("cancelcnt")));
                dbox.put("d_studentcnt" , new Integer( ls.getInt("studentcnt")));
                dbox.put("d_stoldcnt"   , new Integer( ls.getInt("stoldcnt")));

                dbox.put("d_muserid"    , ls.getString("muserid"));
                dbox.put("d_musernm"    , ls.getString("musernm"));
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
    교육기수데이타 조회
    @param box          receive from the form object and session
    @return GrseqData
    **/
    public GrseqData SelectGrseqData(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        GrseqData           data        = null;
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String              v_grcode    = box.getString("p_grcode");
        String              v_gyear     = box.getString("p_gyear");
        String              v_grseq     = box.getString("p_grseq");

        try { 
            connMgr     = new DBConnectionManager();
            
            sbSQL.append(" select  grseqnm                                                  \n")
                 .append("     ,   props                                                    \n")
                 .append("     ,   prope                                                    \n")
                 .append("     ,   luserid                                                  \n")
                 .append("     ,   ldate                                                    \n")
                 .append(" from    tz_grseq                                                 \n")
                 .append(" where   grcode  =" + SQLString.Format(v_grcode  ) + "            \n")
                 .append(" and     grseq   =" + SQLString.Format(v_grseq   ) + "            \n")
                 .append(" and     gyear   =" + SQLString.Format(v_gyear   ) + "            \n");
                 
            System.out.println(this.getClass().getName() + "." + "SelectGrseqData() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                data    = new GrseqData();
                
                data.setGrcode  ( v_grcode                );
                data.setGyear   ( v_gyear                 );
                data.setGrseq   ( v_grseq                 );
                data.setGrseqnm ( ls.getString("grseqnm") );
                data.setProps   ( ls.getString("props"  ) );
                data.setPrope   ( ls.getString("prope"  ) );
                data.setLuserid ( ls.getString("luserid") );
                data.setLdate   ( ls.getString("ldate"  ) );
                
                break;
            }                   
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
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
    새로운 교육기수 등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/
    public int InsertGrseq(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        int                 isOk            = 0;

        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_grcode        = box.getString ("p_grcode"     );
        String              v_gyear         = box.getString ("p_gyear"      );
        String              v_grseq         = "";
        String              v_makeOption    = box.getString ("p_makeoption" );
        String              v_luserid       = box.getSession("userid"       );
        String              v_subjcourse    = "";

        int                 v_sulpaper1     = box.getInt    ("p_sulpaper1"   );
        int                 v_sulpaper2     = box.getInt    ("p_sulpaper2"   );
        int                 v_sulpaper3     = box.getInt    ("p_sulpaper3"   );
        
        String              v_propstart     = box.getString ("p_propstart"  );
        String              v_propend       = box.getString ("p_propend"    );
        String              v_edustart      = box.getString ("p_edustart"   );
        String              v_eduend        = box.getString ("p_eduend"     );
        int                 v_canceldays    = box.getInt    ("p_canceldays" );

        String              v_copy_gyear    = box.getString ("p_copy_gyear" );
        String              v_copy_grseq    = box.getString ("p_copy_grseq" );
        
        String              v_isblended    = box.getString ("p_isblended"   );
        String              v_isexpert     = box.getString ("p_isexpert"    );

        String              v_presdate     = box.getString ("p_presdate"    );
        String              v_preedate     = box.getString ("p_preedate"    );
        String              v_aftersdate   = box.getString ("p_aftersdate"  );
        String              v_afteredate   = box.getString ("p_afteredate"  );

        // 형성평가 기간, 최종평가 기간
        String              v_mtest_start	= box.getString ("p_mtest_start");
        String              v_mtest_end     = box.getString ("p_mtest_end"  );
        String              v_ftest_start   = box.getString ("p_ftest_start");
        String              v_ftest_end		= box.getString ("p_ftest_end"  );
        
        // 중간리포트 기간, 기말리포트 기간
        String              v_mreport_start	= box.getString ("p_mreport_start");
        String              v_mreport_end   = box.getString ("p_mreport_end"  );
        String              v_freport_start = box.getString ("p_freport_start");
        String              v_freport_end	= box.getString ("p_freport_end"  );

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 교육그룹의 교육기수 생성
            sbSQL.append(" select  nvl(ltrim(rtrim(to_char(to_number(max(grseq)) +1,'0000'))),'0001') GRS       \n")
                 .append(" from    tz_grseq                                                                     \n")
                 .append(" where   grcode  = " + SQLString.Format(v_grcode ) + "                                \n")
                 .append(" and     gyear   = " + SQLString.Format(v_gyear  ) + "                                \n");
            
            System.out.println(this.getClass().getName() + "." + "InsertGrseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());

            if ( ls.next() ) { 
                v_grseq = ls.getString("GRS");
            } else { 
                v_grseq = "0001";
            }

            // StringBuffer 초기화    
            sbSQL.setLength(0);
            
            // 교육기수 테이블에 등록
            sbSQL.append(" insert into tz_grseq                               \n")      
                 .append(" (                                                  \n")
                 .append("         grcode                                     \n")
                 .append("     ,   gyear                                      \n")
                 .append("     ,   grseq                                      \n")
                 .append("     ,   grseqnm                                    \n")
                 .append("     ,   luserid                                    \n")
                 .append("     ,   ldate                                      \n")
                 .append("     ,   isblended                                  \n")
                 .append("     ,   isexpert                                   \n")
                 .append(" ) values (                                         \n")
                 .append("         ?                                          \n")
                 .append("     ,   ?                                          \n")
                 .append("     ,   ?                                          \n")
                 .append("     ,   ?                                          \n")
                 .append("     ,   ?                                          \n")
                 .append("     ,   to_char(sysdate,'yyyymmddhh24miss')        \n")
                 .append("     ,   ?                                          \n")
                 .append("     ,   ?    )                                     \n");

            System.out.println(this.getClass().getName() + "." + "InsertGrseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            pstmt   = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setString(1,  v_grcode                    );
            pstmt.setString(2,  v_gyear                     );
            pstmt.setString(3,  v_grseq                     );
            pstmt.setString(4,  box.getString("p_grseqnm")  );
            pstmt.setString(5,  v_luserid                   );
            pstmt.setString(6,  v_isblended                 );
            pstmt.setString(7,  v_isexpert                  );
            
            isOk    = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            // StringBuffer 초기화    
            sbSQL.setLength(0);

			// MAKE_ALL   : 교육그룹에 등록된 과정 모두 일괄생성
			// SELECT_ALL : 선택 교육기수에 등록된 과정 모두 일괄생성
			// MANUAL     : 직접선택
            
            // 직접선택이 아닌경우만 과정기수 생성 시작
            if ( !v_makeOption.equals("MANUAL") ) { 
                if ( ls != null ) { 
                    try { 
                        ls.close(); 
                    } catch ( Exception e ) { } 
                }

                // 교육그룹에 등록된 과정 모두 일괄생성
                if ( v_makeOption.equals("MAKE_ALL") ) { 

                    sbSQL.append(" select   subjcourse                                  \n")
                         .append(" from     tz_grsubj                                   \n")
                         .append(" where    grcode =" + SQLString.Format(v_grcode) + "  \n");

                    //System.out.println(this.getClass().getName() + "." + "InsertGrseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                    
                    ls  = connMgr.executeQuery(sbSQL.toString());
                } 
                // 교육그룹에 등록된 과정 모두 일괄생성
                else if ( v_makeOption.equals("SELECT_ALL") ) { 

                    sbSQL.append(" select  subj    subjcourse                                   \n")
                         .append(" from    tz_subjseq                                           \n")
                         .append(" where   grcode  = " + SQLString.Format(v_grcode     ) + "    \n")
                         .append(" and     gyear   = " + SQLString.Format(v_copy_gyear ) + "    \n")
                         .append(" and     grseq   = " + SQLString.Format(v_copy_grseq ) + "    \n");
                         
                    //System.out.println(this.getClass().getName() + "." + "InsertGrseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                    ls  = connMgr.executeQuery(sbSQL.toString());
                }

                while ( ls.next() ) { 
                    v_subjcourse    = ls.getString("subjcourse");
                    
                    // Subjseq Record 생성
                    isOk = makeSubjseq(v_grcode, v_gyear, v_grseq, "000000","0000","0000",v_subjcourse, v_luserid
                    						, v_sulpaper1, v_sulpaper2, v_sulpaper3
                    						, v_propstart, v_propend, v_edustart, v_eduend, v_canceldays
                    						, v_copy_gyear, v_copy_grseq, v_isblended, v_isexpert, connMgr
                    						, v_presdate, v_preedate, v_aftersdate, v_afteredate
                    						, v_mtest_start, v_mtest_end, v_ftest_start, v_ftest_end
                    						, v_mreport_start, v_mreport_end, v_freport_start, v_freport_end);

                    if ( isOk == 0) { 
                        throw new Exception();
                    }
                }
            }
            
            if ( isOk > 0 )
                connMgr.commit();
            else
                connMgr.rollback();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            isOk    = 0;
            
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
    선택된 교육기수코드 수정
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
    public int UpdateGrseq(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        int                 isOk            = 0;

        int                 iSysAdd         = 0;                            // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
         
        String              v_grcode        = box.getString("p_grcode"  );
        String              v_gyear         = box.getString("p_gyear"   );
        String              v_grseq         = box.getString("p_grseq"   );
        String              v_luserid       = "session";                    // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr = new DBConnectionManager();

            // update TZ_Grseq table

            sbSQL.append(" UPDATE  tz_grseq set                                      \n")           
                 .append("         GRSEQNM     = ?                                   \n")
                 .append("     ,   PROPS       = ?                                   \n")
                 .append("     ,   PROPE       = ?                                   \n")
                 .append("     ,   Luserid     = ?                                   \n")
                 .append("     ,   LDATE       = to_char(sysdate,'YYYYMMDDHH24MISS') \n")
                 .append(" where   grcode      = ?                                   \n")
                 .append(" and     gyear       = ?                                   \n")
                 .append(" and     grseq       = ?                                   \n");

            System.out.println(this.getClass().getName() + "." + "UpdateGrseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            pstmt   = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setString(1, box.getString("p_grseqnm"));
            pstmt.setString(2, box.getString("p_props"  ));
            pstmt.setString(3, box.getString("p_prope"  ));
            pstmt.setString(4, v_luserid                 );
            pstmt.setString(5, v_grcode                  );
            pstmt.setString(6, v_gyear                   );
            pstmt.setString(7, v_grseq                   );

            isOk    = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
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
     과정/코스 지정 화면
     @param box          receive from the form object and session
     @return ArrayList   선택한 과정/코스 리스트
     **/
    public ArrayList AssignedSubjCourseList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ListSet             ls1             = null;
        ListSet             ls2             = null;
        ArrayList           list1           = new ArrayList();;
        StringBuffer        sbSQL           = new StringBuffer("");
        GrseqAssignData     data            = null;
        
        String				sql				= "";
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String              v_grcode        = box.getString("p_grcode"  );
        String              v_gyear         = box.getString("p_gyear"   );
        String              v_grseq         = box.getString("p_grseq"   );

        String              ss_upperclass   = box.getStringDefault("s_upperclass"   , "ALL");   // 과목대분류
        String              ss_middleclass  = box.getStringDefault("s_middleclass"  , "ALL");   // 과목중분류
        String              ss_lowerclass   = box.getStringDefault("s_lowerclass"   , "ALL");   // 과목소분류
        String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // 과목&코스
                             
        String              v_orderColumn   = box.getString("p_orderColumn" );                  // 정렬할 컬럼명
        String              v_orderType     = box.getString("p_orderType"   );                  // 정렬할 순서
        
        String ss_subjgubun = box.getStringDefault("s_subjgubun"   , "ALL");   // 과정구분
        
        try { 
            connMgr     = new DBConnectionManager();

            /*
            // get CourseData
            sbSQL.append(" select  b.subjcourse                                         \n")
                 .append("     ,   MAX(a.isuse) isuse                                   \n")
                 .append("     ,   MAX(b.disseq) disseq                                 \n")
                 .append("     ,   MAX(a.subj)   subj                                   \n")
                 .append("     ,   MAX(a.subjnm) subjnm                                 \n")
                 .append("     ,   MAX(substr(a.ldate,1,4)) ldateyear                   \n")
                 .append("     ,   MAX( (                                                \n")
                 .append("             select  classname                                \n")
                 .append("             from    tz_subjatt                               \n")
                 .append("             where   upperclass  = a.upperclass               \n")
                 .append("             and     subjclass   = a.subjclass                \n")
                 .append("               )                                              \n")
                 .append("         )                       classname                    \n")
                 .append("     ,   count(c.subj)     cnt                                             \n")
                 .append(" from    tz_subj     a                                        \n")
                 .append("     ,   tz_grsubj   b                                        \n")
                 .append("     ,   tz_subjseq  c                                        \n")
                 .append(" where   a.subj   = b.subjcourse                              \n")
                 .append(" and     a.isuse  = 'Y'                                       \n")
                 .append(" and     b.grcode  = " + SQLString.Format(v_grcode) + "       \n")
                 .append(" and     c.grcode(+)  = b.grcode                              \n")
                 .append(" and     c.gyear(+)   = " + SQLString.Format(v_gyear  )               + "          \n")
                 .append(" and     c.grseq(+)   = " + SQLString.Format(v_grseq  )               + "          \n")
                 .append(" and     c.subj(+)    = b.subjcourse                                               \n");

            if ( !ss_subjcourse.equals("ALL") ) { 
                sbSQL.append(" and a.subj = " + SQLString.Format(ss_subjcourse) + "     \n");
            } else { 
                if ( !ss_upperclass.equals("ALL") ) { 
                    if ( !ss_upperclass.equals("ALL") ) { 
                        sbSQL.append(" and a.upperclass = " + SQLString.Format(ss_upperclass    ) + "   \n");
                    }
                    if ( !ss_middleclass.equals("ALL") ) { 
                        sbSQL.append(" and a.middleclass = " + SQLString.Format(ss_middleclass  ) + "   \n");
                    }
                    if ( !ss_lowerclass.equals("ALL") ) { 
                        sbSQL.append(" and a.lowerclass = " + SQLString.Format(ss_lowerclass    ) + "   \n");
                    }
                }
            }

            sbSQL.append(" group by b.subjcourse                                                        \n");

            if ( v_orderColumn.equals("") ) { 
                sbSQL.append(" order by cnt desc, max(a.subjnm)                            \n");
            } else { 
                sbSQL.append(" order by " + v_orderColumn + " " +  v_orderType + " \n");
//                sbSQL.append(" order by MAX(" + v_orderColumn + ") " +  v_orderType + " \n");
            }
            
            System.out.println("sbSQL.toString()=========="+sbSQL.toString()); 
            // " order by subjcourse";
            ls  = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                data    = new GrseqAssignData();

                data.setGrcode      ( v_grcode                   );
                data.setGyear       ( v_gyear                    );
                data.setGrseq       ( v_grseq                    );
                data.setSubjcourse  ( ls.getString("subjcourse" ));
                data.setDisseq      ( ls.getInt   ("disseq"     ));
                data.setIsuse       ( ls.getString("isuse"      ));
                //data.setGrseqnm     ( GetCodenm.get_grseqnm(v_grcode,v_gyear,v_grseq));
                
                sbSQL.setLength(0);
                
                sbSQL.append(" select  subjnm                                                                           \n")
                     .append("     ,   a.upperclass                                                                     \n")
                     .append("     ,   (                                                                                \n")
                     .append("             select  classname                                                            \n")
                     .append("             from    tz_subjatt                                                           \n")
                     .append("             where   upperclass = a.upperclass                                            \n")
                     .append("             and     subjclass  = a.subjclass                                             \n")
                     .append("         )                       classname                                                \n")
                     .append("     ,   get_codenm('0004',a.isonoff) as isonoff                                           \n")
                     .append("     ,   c.cnts                                                                           \n")
                     .append("     ,   substr(a.ldate,1,4)     ldateyear                                                \n")
                     .append("     ,   get_name(a.muserid) as musernm                       	                        \n")
                     .append(" from    tz_subj     a                                                                    \n")
                     .append("     ,   (                                                                                \n")
                     .append("             select  count(*) cnts                                                        \n")
                     .append("             from    tz_subjseq                                                           \n")
                     .append("             where   grcode  = " + SQLString.Format(v_grcode )               + "          \n")
                     .append("             and     gyear   = " + SQLString.Format(v_gyear  )               + "          \n")
                     .append("             and     grseq   = " + SQLString.Format(v_grseq  )               + "          \n")
                     .append("             and     subj    = " + SQLString.Format(data.getSubjcourse() )   + "          \n")
                     .append("         )           c                                                                    \n")
                     .append(" where   a.subj  = " + SQLString.Format(data.getSubjcourse()) + "                         \n");
                     
                ls1     = connMgr.executeQuery(sbSQL.toString());

                if ( ls1.next() ) { 
                    data.setSubjcoursenm( ls1.getString("subjnm"    ));
                    data.setIsonoff     ( ls1.getString("isonoff"   ));
                    data.setIscourse    ( "N"                        );
                    data.setSubjclass   ( ls1.getString("upperclass"));
                    data.setSubjclassnm ( ls1.getString("classname" ));
                    data.setLdateyear   ( ls1.getString("ldateyear" ));
                    data.setMusernm     ( ls1.getString("musernm"   ));
                }
                
                sbSQL.setLength(0);
                
                sbSQL.append(" select count(*) CNTS ");

                if ( StringManager.substring(data.getSubjcourse(),0,4).equals("COUR"))     
                    sbSQL.append(" from tz_courseseq ");
                else
                    sbSQL.append(" from tz_subjseq   ");

                sbSQL.append(" where    grcode  = " + SQLString.Format(v_grcode ) + "   \n")
                     .append(" and      grseq   = " + SQLString.Format(v_grseq  ) + "   \n")
                     .append(" and      gyear   = " + SQLString.Format(v_gyear  ) + "   \n");

                if ( StringManager.substring(data.getSubjcourse(),0,4).equals("COUR")) {     
                    sbSQL.append(" and course = " + SQLString.Format(data.getSubjcourse()) + "    \n");
                } else {
                    sbSQL.append(" and course   = '000000'                                        \n")    
                         .append(" and subj     = " + SQLString.Format(data.getSubjcourse()) + "  \n");
                }                         
                ls2     = connMgr.executeQuery(sbSQL.toString());
                
                ls2.next();
System.out.println("sbSQL.toString()=========="+sbSQL.toString());                
                data.setCnt_using( ls2.getInt("CNTS") );
                list1.add(data);
                
                
                if ( ls1 != null ) { try {  ls1.close();   } catch ( Exception e ) { }  }
                if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            }*/
            
            sql =  "select * from (";
			sql += " select subjcourse, coursenm, isnew, disseq, 'C' subjtype, substr(a.ldate,1,4) ldateyear 			\n";
			sql	+= "   from tz_grsubj a, tz_course b where a.subjcourse=b.course and grcode="+SQLString.Format(v_grcode)+ "	\n";
			sql += " union \n";
			sql += " select subjcourse, subjnm coursenm, isnew, disseq, 'S' subjtype, substr(a.ldate,1,4) ldateyear 			\n";
			sql	+= "   from tz_grsubj a, tz_subj b where a.subjcourse=b.subj and grcode="+SQLString.Format(v_grcode)+ "	and b.isuse  = 'Y' \n";
			
			if(!ss_subjgubun.equals("ALL")){
				sql += " and b.isonoff = " + SQLString.Format(ss_subjgubun) + " \n ";
			}
			
            if ( !ss_upperclass.equals("ALL") ) { 
                if ( !ss_upperclass.equals("ALL") ) { 
                	sql +=" and b.upperclass = " + SQLString.Format(ss_upperclass    ) + "   \n";
                }
                if ( !ss_middleclass.equals("ALL") ) { 
                	sql +=" and b.middleclass = " + SQLString.Format(ss_middleclass  ) + "   \n";
                }
                if ( !ss_lowerclass.equals("ALL") ) { 
                	sql +=" and b.lowerclass = " + SQLString.Format(ss_lowerclass    ) + "   \n";
                }
            }
            
			if(!"ALL".equals(ss_subjcourse))
				sql += "and subjcourse = " + SQLString.Format(ss_subjcourse) + "         								\n";
									
			sql += ") order by subjtype , subjcourse asc";
			
			ls = connMgr.executeQuery(sql);
   
			while (ls.next()) {
				data=new GrseqAssignData();  
				
				data.setGrcode(v_grcode);
				data.setGyear(v_gyear);
				data.setGrseq(v_grseq);
				data.setSubjcourse(ls.getString("subjcourse"));
				data.setDisseq(ls.getInt("disseq"));
				//data.setGrseqnm(GetCodenm.get_grseqnm(v_grcode,v_gyear,v_grseq));
				data.setLdateyear( ls.getString("ldateyear" ));
				data.setSubjcoursenm(ls.getString("coursenm"));				

				if(ls.getString("subjtype").equals("C")){			
					
					sql = "select count(*) CNTS ";
					if(ls.getString("subjtype").equals("C"))                sql += " from tz_courseseq ";
//					else													sql += " from tz_subjseq ";
					
					sql	+=" where grcode="+SQLString.Format(v_grcode)
						+ "   and grseq="+SQLString.Format(v_grseq)
						+ "   and gyear="+SQLString.Format(v_gyear);
					if(ls.getString("subjtype").equals("C"))                sql += " and course=";
//					else													sql += " and course = '000000' and subj="; 
						
					sql +=SQLString.Format(data.getSubjcourse());
					
					if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
					ls2	= connMgr.executeQuery(sql);
					ls2.next();
					
					//data.setSubjcoursenm(GetCodenm.get_coursenm(ls.getString("subjcourse")));
					data.setIsonoff("패키지");
					data.setIscourse("Y");
					data.setSubjclass("COUR");
					data.setSubjclassnm("패키지");
                    data.setMusernm    ("");	
                    data.setIsuse      ("Y");	 	
					data.setCnt_using(ls2.getInt("CNTS"));                    
				} else {
					sql = "select subjnm, a.upperclass, b.classname, a.isonoff, c.cnts, substr(a.ldate,1,4)     ldateyear,   get_name(a.muserid) as musernm, a.isuse "
						+ "  from tz_subj a, tz_subjatt b, " 
						+ "		  (select count(*) cnts from tz_subjseq"
						+ "			where grcode="+SQLString.Format(v_grcode)
						+ " 		  and gyear="+SQLString.Format(v_gyear)
						+ " 		  and grseq="+SQLString.Format(v_grseq)
						+ "   		  and subj="+SQLString.Format(data.getSubjcourse())+" )  c "
						+ " where a.upperclass=b.upperclass and b.middleclass = '000' and b.lowerclass = '000'"// and a.subjclass=b.subjclass"
						+ "   and a.subj="+SQLString.Format(data.getSubjcourse());
					
                    if ( !ss_upperclass.equals("ALL") ) { 
                    	sql+=" and a.upperclass = " + SQLString.Format(ss_upperclass    ) + "   \n";
                    }
                    if ( !ss_middleclass.equals("ALL") ) { 
                        sql+=" and a.middleclass = " + SQLString.Format(ss_middleclass  ) + "   \n";
                    }
                    if ( !ss_lowerclass.equals("ALL") ) { 
                        sql+=" and a.lowerclass = " + SQLString.Format(ss_lowerclass    ) + "   \n";
                    }												
					
					if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
					
					ls1 = connMgr.executeQuery(sql);
					if(ls1.next()){
						//data.setSubjcoursenm(ls1.getString("subjnm"));
						data.setIsonoff(ls1.getString("isonoff"));
						data.setIscourse("N");
						data.setSubjclass(ls1.getString("upperclass"));
						data.setSubjclassnm(ls1.getString("classname"));
						data.setLdateyear  (ls1.getString("ldateyear"));
	                    data.setMusernm    (ls1.getString("musernm"  ));	
	                    data.setIsuse      (ls1.getString("isuse"    ));	
	    				data.setCnt_using(ls1.getInt("CNTS"));	                    
					}
				}
																	
				list1.add(data);
			}
         } catch ( SQLException e ) {
             ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
             throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } catch ( Exception e ) {
             ErrorManager.getErrorStackTrace(e, box, "");
             throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } finally {
              if ( ls != null ) { 
                  try { ls.close(); } catch ( Exception e ) { } 
              }
              if ( ls1 != null ) { 
                  try { ls1.close(); } catch ( Exception e ) { } 
              }
              if ( ls2 != null ) { 
                  try { ls2.close(); } catch ( Exception e ) { } 
              }
              if ( connMgr != null ) { 
                  try { connMgr.freeConnection(); } catch ( Exception e ) { } 
              }
          }

        return list1;
    }            

    /**
    과목/코스 지정정보 저장(과목매핑시 기존 정보를 상속 받는다)
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
    public int SaveAssign(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;

        String              sql             = "";
        int                 isOk            = 1;
        String              v_subjcourse    = "";

        String              v_grcode        = box.getString ("p_grcode"     );
        String              v_gyear         = box.getString ("p_gyear"      );
        String              v_grseq         = box.getString ("p_grseq"      );
        Vector              vec_chk         = box.getVector ("p_chk"        );
                            
        int					v_sulpaper1     = box.getInt    ("p_sulpaper1"  );	//과정설문지
        int					v_sulpaper2     = box.getInt    ("p_sulpaper2"  );	//사전설문지
        int					v_sulpaper3     = box.getInt    ("p_sulpaper3"  );	//사후설문지
        String              v_propstart     = box.getString ("p_propstart"  );
        String              v_propend       = box.getString ("p_propend"    );
        String              v_edustart      = box.getString ("p_edustart"   );
        String              v_eduend        = box.getString ("p_eduend"     );
        int					v_canceldays    = box.getInt    ("p_canceldays" );
        String              v_isblended     = box.getString ("p_isblended"  );
        String              v_isexpert      = box.getString ("p_isexpert"   );
        
        // 사전설문, 사후설문 parameter (p_presdate , p_preedate , p_aftersdate , p_afteredate)
        String              v_presdate      = box.getString ("p_presdate"   );
        String              v_preedate      = box.getString ("p_preedate"   );
        String              v_aftersdate    = box.getString ("p_aftersdate" );
        String              v_afteredate    = box.getString ("p_afteredate" );

        // 중간평가기간, 최종평가기간
        String              v_mtest_start   = box.getString ("p_mtest_start");
        String              v_mtest_end     = box.getString ("p_mtest_end"  );
        String              v_ftest_start   = box.getString ("p_ftest_start");
        String              v_ftest_end     = box.getString ("p_ftest_end"  );

        // 중간리포트기간, 기말리포트기간
        String              v_mreport_start = box.getString ("p_mreport_start");
        String              v_mreport_end   = box.getString ("p_mreport_end"  );
        String              v_freport_start = box.getString ("p_freport_start");
        String              v_freport_end   = box.getString ("p_freport_end"  );

        String              v_luserid       = box.getSession("userid"       );  // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            for ( int i = 0;i<vec_chk.size();i++ ) { 
                v_subjcourse    = vec_chk.elementAt(i).toString();

                
                if(v_subjcourse.substring(0,1).equals("C")){	
					//CourseSeq Record 생성 (subjseq까지 생성)
					isOk = makeCourseseq(v_grcode, v_gyear, v_grseq, v_subjcourse, v_luserid, connMgr);
                }else{
					//Subjseq Record 생성
					//isOk = makeSubjseq(v_grcode, v_gyear, v_grseq, "000000","0000","0000",v_subjcourse, v_luserid, v_sulpaper, v_propstart, v_propend, v_edustart, v_eduend, v_canceldays, "","", connMgr);
	                isOk = makeSubjseq(v_grcode, v_gyear, v_grseq, "000000","0000","0000",v_subjcourse, v_luserid
	                									, v_sulpaper1, v_sulpaper2, v_sulpaper3
	                									, v_propstart, v_propend, v_edustart, v_eduend, v_canceldays
	                									, "","", v_isblended, v_isexpert, connMgr
	                									, v_presdate, v_preedate, v_aftersdate, v_afteredate
	                									, v_mtest_start, v_mtest_end, v_ftest_start, v_ftest_end
	                									, v_mreport_start, v_mreport_end, v_freport_start, v_freport_end);					
				}
                
                
                if ( isOk == 0) { 
                    throw new Exception();
                }
            }
            
            if ( isOk > 0 ) 
                connMgr.commit();
        } catch ( Exception e ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
        } finally { 
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
    과목 일괄 수정 화면
    @param box          receive from the form object and session
    @return ArrayList   선택한 과목/코스 리스트
    **/
    public ArrayList selectSubjCourseList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	        = null;
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        ArrayList           list1           = new ArrayList();
        DataBox             dbox            = null;
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_grcode        = box.getString("p_grcode"  );
        String              v_gyear         = box.getString("p_gyear"   );
        String              v_grseq         = box.getString("p_grseq"   );

        String              ss_upperclass   = box.getStringDefault("s_upperclass"   , "ALL");   // 과목대분류
        String              ss_middleclass  = box.getStringDefault("s_middleclass"  , "ALL");   // 과목중분류
        String              ss_lowerclass   = box.getStringDefault("s_lowerclass"   , "ALL");   // 과목소분류
        String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // 과목&코스

        String              v_orderColumn   = box.getString("p_orderColumn" );                  // 정렬할 컬럼명
        String              v_orderType     = box.getString("p_orderType"   );                  // 정렬할 순서

        try { 
            connMgr = new DBConnectionManager();

            // 
            sbSQL.append(" select  a.subj                                               \n")
                 .append("     ,   a.year                                               \n")
                 .append("     ,   a.subjseq                                            \n")
                 .append("     ,   a.subjnm                                             \n")
                 .append("     ,   get_codenm('0004',a.isonoff) isonoff                 \n")                 
                 .append("     ,   a.propstart                                          \n")
                 .append("     ,   a.propend                                            \n")
                 .append("     ,   a.edustart                                           \n")
                 .append("     ,   a.eduend                                             \n")
                 .append("     ,   a.edulimit                                           \n")
                 .append("     ,   a.isblended                                          \n")
                 .append("     ,   a.isexpert                                           \n")
                 .append("     ,   DECODE(a.isblended, 'Y', '블랜디드', DECODE(a.isexpert, 'Y', '전문가', '기본교육')) gubun_name \n")
                 .append("     ,   a.edulimit                                           \n")
                 .append("     ,   a.isuse                                              \n")
                 .append("     ,   a.isclosed                                           \n")
                 .append("     ,   (                                                    \n")
                 .append("             select  classname                                \n")
                 .append("             from    tz_subjatt                               \n")
                 .append("             where   upperclass  = a.scupperclass             \n")
                 .append("             and     subjclass   = a.scsubjclass              \n")
                 .append("         )               classname                            \n")
                 .append(" from    vz_scsubjseq    a                                    \n")
                 .append(" where   a.grcode =" +SQLString.Format(v_grcode  ) + "        \n")
                 .append(" and     a.gyear  =" +SQLString.Format(v_gyear   ) + "        \n")
                 .append(" and     a.grseq  =" +SQLString.Format(v_grseq   ) + "        \n");

            if ( !ss_subjcourse.equals("ALL") ) { 
                sbSQL.append(" and a.subj = " + SQLString.Format(ss_subjcourse) + " \n");
            } else { 
                if ( !ss_upperclass.equals("ALL") ) { 
                    if ( !ss_upperclass.equals("ALL") )  
                        sbSQL.append(" and a.scupperclass   = " + SQLString.Format(ss_upperclass    ) + " \n");
                    if ( !ss_middleclass.equals("ALL") ) 
                        sbSQL.append(" and a.scmiddleclass  = " + SQLString.Format(ss_middleclass   ) + " \n");
                    if ( !ss_lowerclass.equals("ALL") )  
                        sbSQL.append(" and a.sclowerclass   = " + SQLString.Format(ss_lowerclass    ) + " \n");
                }
            }

            if ( v_orderColumn.equals("") ) { 
                sbSQL.append(" order by a.subjnm                            \n");
            } else { 
                sbSQL.append(" order by " + v_orderColumn + v_orderType + " \n");
            }
            
            System.out.println(this.getClass().getName() + "." + "selectSubjCourseList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                list1.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list1;
    }            

    
    /**
    지정된 과목기수에 일괄적으로 1일최대 학습량을 변경한다.
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
    public int updateEdulimit(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt       = null;
        String              sql         = "";
                                        
        int                 isOk        = 1;
                                        
        Vector              vec_chk     = box.getVector("p_chk"         );
        String              chk         = "";
    
        String              v_subj      = "";
        String              v_year      = "";
        String              v_subjseq   = "";
        String              v_edulimit  = box.getString ("p_edulimit"   );
        String              v_luserid   = box.getSession("userid"       ); // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr     = new DBConnectionManager();

            sql  = " update tz_subjseq set edulimit = ?, luserid = ?, ldate = to_char(sysdate,'yyyymmddhh24miss') ";
            sql += "  where subj = ? and year = ? and subjseq = ?";
            

            for ( int i = 0; i < vec_chk.size(); i++ ) { 
            	pstmt       = connMgr.prepareStatement(sql);
                chk                 = vec_chk.elementAt(i).toString();
                StringTokenizer st  = new StringTokenizer(chk, ",");
                v_subj              = st.nextToken();
                v_year              = st.nextToken();
                v_subjseq           = st.nextToken();

                // TZ_SUBJSEQ 수정
                pstmt.setString(1, v_edulimit   );
                pstmt.setString(2, v_luserid    );
                pstmt.setString(3, v_subj       );
                pstmt.setString(4, v_year       );
                pstmt.setString(5, v_subjseq    );
                
                isOk                = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
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
    지정된 과목기수에 일괄적으로 1일최대 학습량을 변경한다.
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
    public int updateBlended(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt       = null;
        String              sql         = "";
                                        
        int                 isOk        = 1;
                                        
        Vector              vec_chk     = box.getVector("p_chk"         );
        String              chk         = "";
    
        String              v_subj      = "";
        String              v_year      = "";
        String              v_subjseq   = "";
        String              v_isblended = box.getString ("p_isblended"  );
        String              v_isexpert  = box.getString ("p_isexpert"   );
        String              v_luserid   = box.getSession("userid"       ); // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr     = new DBConnectionManager();

            sql  = " update tz_subjseq set isblended = ?, isexpert = ?, luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') ";
            sql += "  where subj = ? and year = ? and subjseq = ?";
            
            pstmt       = connMgr.prepareStatement(sql);

            for ( int i = 0; i < vec_chk.size(); i++ ) { 
                chk                 = vec_chk.elementAt(i).toString();
                StringTokenizer st  = new StringTokenizer(chk, ",");
                v_subj              = st.nextToken();
                v_year              = st.nextToken();
                v_subjseq           = st.nextToken();

                // tz_subjseq 수정
                pstmt.setString(1, v_isblended  );
                pstmt.setString(2, v_isexpert   );
                pstmt.setString(3, v_luserid    );
                pstmt.setString(4, v_subj       );
                pstmt.setString(5, v_year       );
                pstmt.setString(6, v_subjseq    );
                
                isOk                = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
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
    지정된 과목기수에 일괄적으로 1일최대 학습량을 변경한다.
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
    public int updateExpert(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt       = null;
        String              sql         = "";
                                        
        int                 isOk        = 1;
                                        
        Vector              vec_chk     = box.getVector("p_chk"         );
        String              chk         = "";
    
        String              v_subj      = "";
        String              v_year      = "";
        String              v_subjseq   = "";
        String              v_isexpert  = box.getString ("p_isexpert"   );
        String              v_luserid   = box.getSession("userid"       ); // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr     = new DBConnectionManager();

            sql  = " update tz_subjseq set isexpert = ?, luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') ";
            sql += "  where subj = ? and year = ? and subjseq = ?";
            
            pstmt       = connMgr.prepareStatement(sql);

            for ( int i = 0; i < vec_chk.size(); i++ ) { 
                chk                 = vec_chk.elementAt(i).toString();
                StringTokenizer st  = new StringTokenizer(chk, ",");
                v_subj              = st.nextToken();
                v_year              = st.nextToken();
                v_subjseq           = st.nextToken();

                // tz_subjseq 수정
                pstmt.setString(1, v_isexpert   );
                pstmt.setString(2, v_luserid    );
                pstmt.setString(3, v_subj       );
                pstmt.setString(4, v_year       );
                pstmt.setString(5, v_subjseq    );
                
                isOk                = pstmt.executeUpdate();
            }
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
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
    지정된 과목기수에 일괄적으로 수강신청기간을 변경한다.
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
    public int updatePropose(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt       = null;
        String              sql         = "";
        String              sql1        = "";
        int                 isOk        = 1;

        Vector              vec_chk     = box.getVector("p_chk");
        String              chk         = "";
    
        String              v_subj      = "";
        String              v_year      = "";
        String              v_subjseq   = "";
        String              v_propstart = box.getString ("p_propstart"  );
        String              v_propend   = box.getString ("p_propend"    );
        String              v_luserid   = box.getSession("userid"       ); // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr = new DBConnectionManager();

            sql = " update tz_subjseq set propstart = ?, propend= ?, luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') ";
            sql  += "  where subj = ? and year = ? and subjseq = ?";
            
            pstmt   = connMgr.prepareStatement(sql);

            for ( int i = 0; i < vec_chk.size(); i++ ) { 
                chk                 = vec_chk.elementAt(i).toString();
                StringTokenizer st  = new StringTokenizer(chk, ",");
                v_subj              = st.nextToken();
                v_year              = st.nextToken();
                v_subjseq           = st.nextToken();

                // tz_subjseq 수정
                pstmt.setString(1, v_propstart  );
                pstmt.setString(2, v_propend    );
                pstmt.setString(3, v_luserid    );
                pstmt.setString(4, v_subj       );
                pstmt.setString(5, v_year       );
                pstmt.setString(6, v_subjseq    );
                
                isOk = pstmt.executeUpdate();
            }
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
        } finally { 
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e1 ) { } 
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
    지정된 과목기수에 일괄적으로 학습기간을을 변경한다.
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
    public int updateEdu(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        String              sql                 = "";
        int                 isOk                = 1;

        Vector              vec_chk             = box.getVector("p_chk");
        String              chk                 = "";
    
        String              v_subj              = "";
        String              v_year              = "";
        String              v_subjseq           = "";
        String              v_edustart          = box.getString ("p_edustart"   );
        String              v_eduend            = box.getString ("p_eduend"     );
        String              v_edustart_value    = "";
        String              v_eduend_value      = "";
        String              v_luserid           = box.getSession("userid"       ); // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr = new DBConnectionManager();

            sql  = " update tz_subjseq set edustart = ?, eduend = ?, luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') ";
            sql += "  where subj = ? and year = ? and subjseq = ?";
            
            pstmt   = connMgr.prepareStatement(sql);

            if ( v_edustart.equals("") ) { 
                v_edustart_value = "getdate()";
            } else { 
                v_edustart_value = v_edustart.substring(0,8);
                v_edustart_value = "convert(datetime, '" + v_edustart_value + "')";
            }
            if ( v_eduend.equals("") ) { 
                v_eduend_value = "getdate()";
            } else { 
                v_eduend_value = v_eduend.substring(0,8);
                v_eduend_value = "convert(datetime, '" + v_eduend_value + "')";
            }

            for ( int i = 0; i < vec_chk.size(); i++ ) { 
                chk                 = vec_chk.elementAt(i).toString();
                StringTokenizer st  = new StringTokenizer(chk, ",");
                v_subj              = st.nextToken();
                v_year              = st.nextToken();
                v_subjseq           = st.nextToken();

                // tz_subjseq 수정
                pstmt.setString(1, v_edustart   );
                pstmt.setString(2, v_eduend     );
                pstmt.setString(3, v_luserid    );
                pstmt.setString(4, v_subj       );
                pstmt.setString(5, v_year       );
                pstmt.setString(6, v_subjseq    );
                
                isOk = pstmt.executeUpdate();
            }
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
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
     과목/코스 삭제 저장
     @param box      receive from the form object and session
     @return isOk    1:update success,0:update fail
     **/
     public int delSubjcourse(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr         = null;
         String              sql             = "";
         int                 isOk            = 1;

         String              v_process       = box.getString("p_process"     );
         String              v_grcode        = box.getString("p_grcode"      );
         String              s_grcode        = box.getString("s_grcode"      );
         String              v_gyear         = box.getString("p_mgyear"      );
         String              v_grseq         = box.getString("p_mgrseq"      );
         String              v_subjcourse    = box.getString("p_msubjcourse" );
         String              v_myear         = box.getString("p_myear"       );
         String              v_mseq          = box.getString("p_mseq"        );
         String              v_luserid       = "session"; // 세션변수에서 사용자 id를 가져온다.

         try { 
             connMgr     = new DBConnectionManager();
             
             connMgr.setAutoCommit(false);
             
             isOk        = delSubjseq(v_subjcourse, v_myear, v_mseq, v_luserid, connMgr);
             
             if ( isOk > 0 ) { 
                 connMgr.commit(); 
             } else { 
                 throw new Exception();
             }
         } catch ( Exception ex ) { 
             connMgr.rollback();
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( connMgr != null ) { 
                 try { 
                     connMgr.setAutoCommit(true); 
                     connMgr.freeConnection();                    
                 } catch ( Exception e ) { } 
             }
         }

         return 1;
     }

     
     /**
     과목/코스 기수 추가 저장
     @param box      receive from the form object and session
     @return isOk    1:update success,0:update fail
     **/
     public int addSubjcourse(RequestBox box) throws Exception { 
         DBConnectionManager connMgr         = null;
                                             
         String              sql             = "";
         int                 isOk            = 1;

         String              v_process       = box.getString("p_process"     );
         String              v_grcode        = box.getString("p_grcode"      );
         String              v_gyear         = box.getString("p_mgyear"      );
         String              v_grseq         = box.getString("p_mgrseq"      );
         String              v_subjcourse    = box.getString("p_msubjcourse" );
         String              v_luserid       = "session"; // 세션변수에서 사용자 id를 가져온다.

         try { 
             connMgr = new DBConnectionManager();
             
             connMgr.setAutoCommit(false);

             isOk    = makeSubjseq(v_grcode, v_gyear, v_grseq, "000000", "0000","0000", v_subjcourse, v_luserid, 0,0,0,"","","","",0,"","",connMgr,"","","","");
             
             if ( isOk > 0 )
                 connMgr.commit();
             else
                 throw new Exception();
         } catch ( Exception e ) { 
             connMgr.rollback();
             ErrorManager.getErrorStackTrace(e, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
         } finally { 
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
     과목/코스 기수 추가 저장 - n개만큼 저장
     @param box      receive from the form object and session
     @return isOk    1:update success,0:update fail
     **/
     public int AddSubjSeq(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr     = null;
                                         
         String              sql         = "";
         int                 isOk        = 0;

         String              v_process   = box.getString ("p_process");
         String              v_grcode    = box.getString ("s_grcode" );
         String              v_gyear     = box.getString ("s_gyear"  );
                             
         String              v_grseq     = box.getString ("p_mgrseq" );
         String              v_subj      = box.getString ("p_subj"   );
         int                 v_seqcnt    = box.getInt    ("p_seqcnt" ); // 등록될 기수 개수
                             
         String              v_luserid   = box.getSession("userid"   ); // 세션변수에서 사용자 id를 가져온다.

         try { 
             connMgr     = new DBConnectionManager();
             
             connMgr.setAutoCommit(false);

             // v_grcode, v_gyear, v_grseq
             for ( int i = 0; i < v_seqcnt; i++ ) { 
                 isOk   += makeSubjseq(v_grcode, v_gyear, v_grseq, "000000", "0000","0000", v_subj, v_luserid, 0,0,0,"","","","",0,"","",connMgr,"","","","");
             }
             
             if ( isOk > 0 ) 
                 connMgr.commit();
             else
                 throw new Exception();

         } catch ( Exception e ) { 
             connMgr.rollback();
             ErrorManager.getErrorStackTrace(e, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + e.getMessage());
         } finally { 
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
     교육기수 삭제 저장
     @param box      receive from the form object and session
     @return isOk    1:update success,0:update fail
     **/
     public int delGrseq(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr     = null;
         String              sql         = "";
         int                 isOk        = 1;
         ListSet             ls          = null;

         String              v_process   = box.getString ("p_process");
         String              v_grcode    = box.getString ("p_grcode" );
         String              v_gyear     = box.getString ("p_gyear"  );
         String              v_grseq     = box.getString ("p_grseq"  );
         String              v_luserid   = box.getSession("userid"   ); // 세션변수에서 사용자 id를 가져온다.

         try { 
             connMgr = new DBConnectionManager();
             
             connMgr.setAutoCommit(false);

             sql = "\n select course, cyear, courseseq "
            	 + "\n from   tz_courseseq "
                 + "\n where  grcode=" +SQLString.Format(v_grcode)
                 + "\n and    gyear=" +SQLString.Format(v_gyear)
                 + "\n and    grseq=" +SQLString.Format(v_grseq);
                 
             if ( ls != null ) { 
                 try { 
                     ls.close(); 
                 } catch ( Exception e ) { } 
             }
             
             ls = connMgr.executeQuery(sql);
             
             while ( ls.next() ) { 
                 isOk = delCourseseq( ls.getString("course")
                                  ,   ls.getString("cyear")
                                  ,   ls.getString("courseseq")
                                  ,   v_luserid
                                  ,   connMgr
                                    );
             }

             sql = "\n select subj, year, subjseq "
            	 + "\n from   tz_subjseq "
                 + "\n where  grcode=" +SQLString.Format(v_grcode)
                 + "\n and    gyear=" +SQLString.Format(v_gyear)
                 + "\n and    grseq=" +SQLString.Format(v_grseq);
                 
             if ( ls != null ) { 
                 try { 
                     ls.close(); 
                 } catch ( Exception e ) { } 
             }
             
             ls  = connMgr.executeQuery(sql);
             
             while ( ls.next() ) { 
                 isOk = delSubjseq(   ls.getString("subj")
                                  ,   ls.getString("year")
                                  ,   ls.getString("subjseq")
                                  ,   v_luserid
                                  ,   connMgr
                                  );
             }

             sql = " delete from tz_grseq "
                 + " where grcode=" +SQLString.Format(v_grcode)
                 + " and gyear=" +SQLString.Format(v_gyear)
                 + " and grseq=" +SQLString.Format(v_grseq);

             isOk    = connMgr.executeUpdate(sql);

             if ( isOk > 0)
                 connMgr.commit(); 
             else 
                 throw new Exception();
         } catch ( Exception e ) { 
             connMgr.rollback();
             ErrorManager.getErrorStackTrace(e, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
         } finally { 
             if ( ls != null ) { 
                 try { 
                     ls.close(); 
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
    과목기수데이터 조회
    @param box          receive from the form object and session
    @return SubjseqData
    **/
    public SubjseqData SelectSubjseqData(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        ListSet             ls2         = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        SubjseqData         data        = null;
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
     
        String              v_subj      = box.getString("p_subj");
        String              v_year      = box.getString("p_year");
        String              v_subjseq   = box.getString("p_subjseq");

        try { 
            connMgr     = new DBConnectionManager();

            sbSQL.append(" select a.subj            , a.year          , a.subjseq           , a.subjseqgr          \n")
	            .append("     ,   a.grcode          , a.gyear         , a.grseq             , a.isbelongcourse     \n")
	            .append("     ,   a.course          , a.cyear         , a.courseseq         , a.propstart          \n")
	            .append("     ,   a.propend         , a.edustart      , a.eduend            , a.isclosed           \n")
	            .append("     ,   a.ismultipaper    , a.subjnm        , a.luserid           , a.ldate              \n")
	            .append("     ,   a.studentlimit    , a.point         , a.biyong            , a.ischarge           \n")
	            .append("     ,   a.edulimit        , a.warndays      , a.stopdays          , a.gradscore          \n")
	            .append("     ,   a.gradstep        , a.wstep         , a.wmtest            , a.wftest             \n")
	            .append("     ,   a.wreport         , a.wact          , a.wetc1             , a.wetc2              \n")
	            .append("     ,   a.endfirst        , a.endfinal      , a.proposetype       , a.whtest             \n")
	            .append("     ,   a.score           , a.gradreport    , a.gradexam          , a.rndcreditreq       \n")
	            .append("     ,   a.rndcreditchoice , a.rndcreditadd  , a.rndcreditdeduct   , a.isablereview       \n")
	            .append("     ,   a.tsubjbudget     , a.rndjijung     , a.isusebudget       , a.isessential        \n")
	            .append("     ,   a.gradftest       , a.gradhtest     , a.isvisible         , a.place              \n")
	            .append("     ,   a.placejh         , a.bookname      , a.bookprice         , a.sulpapernum        \n")
	            .append("     ,   a.canceldays      , a.isopenedu     , a.maleassignrate                           \n")
	            .append("     ,   a.gradftest_flag  , a.gradexam_flag , a.gradhtest_flag    , a.gradreport_flag    \n")
	            .append("     ,   a.isattendchk     , a.attstime      , a.attetime          , a.isgoyong		   \n")
	            .append("     ,   a.goyongpricemajor, a.goyongpriceminor, a.usebook							       \n")
	            .append("     ,   a.presulpapernum  , a.presulsdate     , a.presuledate						       \n")
	            .append("     ,   a.aftersulpapernum, a.aftersulsdate   , a.aftersuledate					       \n")
	            .append("     ,   a.reviewdays      , a.study_count		, a.study_time							   \n")
	            .append("     ,   a.muserid         , get_name(a.muserid) as musernm        , a.musertel		   \n")
	            .append("     ,   a.mtest_start     , a.mtest_end     , a.ftest_start       , a.ftest_end          \n")
	            .append("     ,   a.mreport_start   , a.mreport_end   , a.freport_start     , a.freport_end        \n")
	            .append("     ,   a.goyongpricestand ,a.neweroom															   \n")
	            .append(" from    tz_subjseq a, tz_subj b                                                          \n")
	            .append(" where   a.subj = b.subj                                                                  \n")
	            .append(" and     a.subj    =" + SQLString.Format(v_subj    ) + "                                  \n")
	            .append(" and     a.year    =" + SQLString.Format(v_year    ) + "                                  \n")
	            .append(" and     a.subjseq =" + SQLString.Format(v_subjseq ) + "                                  \n");
            
	       ls      = connMgr.executeQuery(sbSQL.toString());
	       
	       if ( ls.next() ) { 
	           data = new SubjseqData();
	           
	           data.setSubj           ( ls.getString("subj"            ));
	           data.setYear           ( ls.getString("year"            ));
	           data.setSubjseq        ( ls.getString("subjseq"         ));
	           data.setSubjseqgr      ( ls.getString("subjseqgr"       ));
	           data.setGrcode         ( ls.getString("grcode"          ));
	           data.setGyear          ( ls.getString("gyear"           ));
	           data.setGrseq          ( ls.getString("grseq"           ));
	           data.setIsbelongcourse ( ls.getString("isbelongcourse"  ));
	           data.setCourse         ( ls.getString("course"          ));
	           data.setCyear          ( ls.getString("cyear"           ));
	           data.setCourseseq      ( ls.getString("courseseq"       ));
	           data.setPropstart      ( ls.getString("propstart"       ));
	           data.setPropend        ( ls.getString("propend"         ));
	           data.setEdustart       ( ls.getString("edustart"        ));
	           data.setEduend         ( ls.getString("eduend"          ));
	           data.setEndfirst       ( ls.getString("endfirst"        ));
	           data.setEndfinal       ( ls.getString("endfinal"        ));
	           data.setIsclosed       ( ls.getString("isclosed"        ));
	           data.setIsopenedu      ( ls.getString("isopenedu"       ));
	           data.setIsmultipaper   ( ls.getString("ismultipaper"    ));
	           data.setSubjnm         ( ls.getString("subjnm"          ));
	           data.setLuserid        ( ls.getString("luserid"         ));
	           data.setLdate          ( ls.getString("ldate"           ));
	           data.setStudentlimit   ( ls.getInt   ("studentlimit"    ));
	           data.setPoint          ( ls.getInt   ("point"           ));
	           data.setBiyong         ( ls.getInt   ("biyong"          ));
	           data.setIscharge       ( ls.getString("ischarge"        ));
	           data.setEdulimit       ( ls.getInt   ("edulimit"        ));
	           data.setWarndays       ( ls.getInt   ("warndays"        ));
	           data.setStopdays       ( ls.getInt   ("stopdays"        ));
	           data.setGradscore      ( ls.getInt   ("gradscore"       ));
	           data.setGradstep       ( ls.getInt   ("gradstep"        ));
	           data.setWstep          ( ls.getDouble("wstep"           ));
	           data.setWmtest         ( ls.getDouble("wmtest"          ));
	           data.setWftest         ( ls.getDouble("wftest"          ));
	           data.setWreport        ( ls.getDouble("wreport"         ));
	           data.setWact           ( ls.getDouble("wact"            ));
	           data.setWetc1          ( ls.getDouble("wetc1"           ));
	           data.setWetc2          ( ls.getDouble("wetc2"           ));
	           data.setProposetype    ( ls.getString("proposetype"     ));
	           data.setGrcodenm       (GetCodenm.get_grcodenm  (data.getGrcode()                                   ));
	           data.setGrseqnm        (GetCodenm.get_grseqnm   (data.getGrcode(), data.getGyear(), data.getGrseq() ));
	           data.setGradreport     ( ls.getInt   ("gradreport"      ));
	           data.setGradexam       ( ls.getInt   ("gradexam"        ));
	           data.setGradftest      ( ls.getInt   ("gradftest"       ));
	           data.setGradhtest      ( ls.getInt   ("gradhtest"       ));
	           data.setWhtest         ( ls.getDouble("whtest"          ));
	           data.setScore          ( ls.getInt   ("score"           ));
	           data.setRndcreditreq   ( ls.getDouble("rndcreditreq"    ));
	           data.setRndcreditchoice( ls.getDouble("rndcreditchoice" ));
	           data.setRndcreditadd   ( ls.getDouble("rndcreditadd"    ));
	           data.setRndcreditdeduct( ls.getDouble("rndcreditdeduct" ));
	           data.setRndjijung      ( ls.getString("rndjijung"       ));
	           data.setIsablereview   ( ls.getString("isablereview"    ));
	           data.setTsubjbudget    ( ls.getInt("tsubjbudget"        ));
	           data.setIsusebudget    ( ls.getString("isusebudget"     ));
	           data.setIsessential    ( ls.getString("isessential"     ));
	           data.setIsvisible      ( ls.getString("isvisible"       ));
	           data.setPlace          ( ls.getString("place"           ));
	           data.setPlacejh        ( ls.getString("placejh"         ));
	           data.setUsebook        ( ls.getString("usebook"         ));
	           data.setBookname       ( ls.getString("bookname"        ));
	           data.setBookprice      ( ls.getInt   ("bookprice"       ));
	           data.setSulpapernum    ( ls.getInt   ("sulpapernum"     ));
	           data.setCanceldays     ( ls.getInt   ("canceldays"      ));
	           data.setMaleassignrate ( ls.getInt   ("maleassignrate"  ));
	           data.setGradftest_flag ( ls.getString("gradftest_flag"  ));
	           data.setGradexam_flag  ( ls.getString("gradexam_flag"   ));
	           data.setGradhtest_flag ( ls.getString("gradhtest_flag"  ));
	           data.setGradreport_flag( ls.getString("gradreport_flag" ));
	           data.setIsattendchk    ( ls.getString("isattendchk" ));
	           data.setAttstime       ( ls.getString("attstime" ));
	           data.setAttetime       ( ls.getString("attetime" ));
	           data.setIsgoyong	      ( ls.getString("isgoyong"));
	           data.setGoyongpricemajor(ls.getInt("goyongpricemajor"));	// 고용보험-환급액(KT)
	           data.setGoyongpriceminor(ls.getInt("goyongpriceminor"));	// 고용보험-KT교육비
	           
	           //2008.11.14 오충현 사전,사후 설문 추가
	           data.setPresulpapernum	(ls.getInt("presulpapernum"));
	           data.setPresulsdate		(ls.getString("presulsdate"));
	           data.setPresuledate		(ls.getString("presuledate"));
	           data.setAftersulpapernum	(ls.getInt("aftersulpapernum"));
	           data.setAftersdate		(ls.getString("aftersulsdate"));
	           data.setAfteredate		(ls.getString("aftersuledate"));
	           
	           data.setReviewdays      (ls.getInt("reviewdays"));
	           data.setStudy_count     (ls.getInt("study_count"));
	           data.setStudy_time      (ls.getInt("study_time"));

	           data.setMuserid (ls.getString("muserid"));	// 담당자아이디
	           data.setMusernm (ls.getString("musernm"));	// 담당자명
	           data.setMusertel(ls.getString("musertel"));	// 담당자연락처

	           data.setMtest_start  (ls.getString("mtest_start"));		// 형성평가 시작일
	           data.setMtest_end    (ls.getString("mtest_end"));		// 형성평가 종료일
	           data.setFtest_start  (ls.getString("ftest_start"));		// 최종평가 시작일
	           data.setFtest_end    (ls.getString("ftest_end"));		// 최종평가 종료일	           
	           data.setMreport_start(ls.getString("mreport_start"));	// 중간리포트 시작일
	           data.setMreport_end  (ls.getString("mreport_end"));		// 중간리포트 종료일
	           data.setFreport_start(ls.getString("freport_start"));	// 기말리포트 시작일
	           data.setFreport_end  (ls.getString("freport_end"));		// 기말리포트 종료일
	           
	           data.setEroom         (ls.getString("neweroom"));		// 고사장정보
	           
	           data.setGoyongpricestand(ls.getInt("goyongpricestand"));	// 고용보험-CP교육비	           
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
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
    과목기수정보 수정
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
    public int UpdateSubjseq(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 0;
System.out.println("box----------------------------->" + box);  
        String              v_subj              = box.getString("p_subj"        );  // 과목코드
        String              v_subjnm            = box.getString("p_subjnm"      );  // 기수과목명
        String              v_year              = box.getString("p_year"        );  // 년도
        String              v_subjseq           = box.getString("p_subjseq"     );  // 과목기수
        String              v_newsubjseq        = box.getString("p_newsubjseq"  );  // 과목기수수정시 새로운기수
        String              v_propstart         = box.getString("p_propstart"   );  // 
        String              v_propend           = box.getString("p_propend"     );  // 
        String              v_edustart          = box.getString("p_edustart"    );  // 
        String              v_eduend            = box.getString("p_eduend"      );  // 
        String              v_endfirst          = box.getString("p_endfirst"    );  // 
        String              v_endfinal          = box.getString("p_endfinal"    );  //
        String              v_aftersdate       = StringManager.replace(box.getString("p_aftersdate"), "-", "");
        String              v_afteredate       = StringManager.replace(box.getString("p_afteredate"), "-", "");
        String              v_luserid           = box.getSession("userid");       // 세션변수에서 사용자 id를 가져온다.
System.out.println("v_aftersdate----------------------------->" + v_aftersdate);  
System.out.println("v_afteredate----------------------------->" + v_afteredate);  
        Vector  vec = box.getVector("p_eroomseq");
        String v_eroomseq ="";
        
        for(int p=0; p < vec.size(); p++){
        	
        	v_eroomseq = v_eroomseq+vec.get(p)+",";
        }
        
      //  System.out.println("v_eroomseq===========>"+v_eroomseq);
        int pidx = 1;
        
        try {
            if ( v_propstart.length() != 10 )
                v_propstart = "";
                
            if ( v_propend.length() != 10 )    
                v_propend   = "";
                
            if ( v_edustart.length() != 10 )
                v_edustart  = "";
                
            if ( v_eduend.length() != 10 )
                v_eduend    = "";
                
            if ( v_endfirst.length() != 10 )
                v_endfirst  = "";
                
            if ( v_endfinal.length() != 10 )
                v_endfinal  = "";
            
            if ( v_aftersdate.length() != 8 )
            	v_aftersdate  = "";
                
            if ( v_afteredate.length() != 8 )
            	v_afteredate  = "";
            
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);            
            
            sbSQL.setLength(0);

            // update TZ_Grseq table          
            sbSQL.append(" update  tz_subjseq set                                                   \n")
                 .append("         propstart           = ?                                          \n")
                 .append("     ,   propend             = ?                                          \n")
                 .append("     ,   edustart            = ?                                          \n")
                 .append("     ,   eduend              = ?                                          \n")
                 .append("     ,   endfirst            = ?                                          \n")
                 .append("     ,   endfinal            = ?                                          \n")
                 .append("     ,   ismultipaper        = ?                                          \n")
                 .append("     ,   subjnm              = ?                                          \n")
                 .append("     ,   luserid             = ?                                          \n")
                 .append("     ,   ldate               = to_char(sysdate,'yyyymmddhh24miss')        \n")
                 .append("     ,   studentlimit        = ?                                          \n")
                 .append("     ,   point               = ?                                          \n")
                 .append("     ,   biyong              = ?                                          \n")
                 .append("     ,   edulimit            = ?                                          \n")
                 .append("     ,   warndays            = ?                                          \n")
                 .append("     ,   stopdays            = ?                                          \n")
                 .append("     ,   gradscore           = ?                                          \n")
                 .append("     ,   gradstep            = ?                                          \n")
                 .append("     ,   wstep               = ?                                          \n")
                 .append("     ,   wmtest              = ?                                          \n")
                 .append("     ,   wftest              = ?                                          \n")
                 .append("     ,   wreport             = ?                                          \n")
                 .append("     ,   wact                = ?                                          \n")
                 .append("     ,   wetc1               = ?                                          \n")
                 .append("     ,   wetc2               = ?                                          \n")
                 .append("     ,   proposetype         = ?                                          \n")
                 .append("     ,   gradexam            = ?                                          \n")
                 .append("     ,   gradreport          = ?                                          \n")
                 .append("     ,   whtest              = ?                                          \n")
                 .append("     ,   rndcreditreq        = ?                                          \n")
                 .append("     ,   rndcreditchoice     = ?                                          \n")
                 .append("     ,   rndcreditadd        = ?                                          \n")
                 .append("     ,   rndcreditdeduct     = ?                                          \n")
                 .append("     ,   isablereview        = ?                                          \n")
                 .append("     ,   score               = ?                                          \n")
                 .append("     ,   tsubjbudget         = ?                                          \n")
                 .append("     ,   rndjijung           = ?                                          \n")
                 .append("     ,   isessential         = ?                                          \n")
                 .append("     ,   gradftest           = ?                                          \n")
                 .append("     ,   gradhtest           = ?                                          \n")
                 .append("     ,   isvisible           = ?                                          \n")
                 .append("     ,   place               = ?                                          \n")
                 .append("     ,   placejh             = ?                                          \n")
                 .append("     ,   sulpapernum         = ?                                          \n")
                 .append("     ,   canceldays          = ?                                          \n")
                 .append("     ,   ischarge            = ?                                          \n")
                 .append("     ,   isopenedu           = ?                                          \n")
                 .append("     ,   maleassignrate      = ?                                          \n")
                 .append("     ,   gradftest_flag      = ?                                          \n")
                 .append("     ,   gradexam_flag       = ?                                          \n")
                 .append("     ,   gradhtest_flag      = ?                                          \n")
                 .append("     ,   gradreport_flag     = ?                                          \n")
                 .append("     ,   isattendchk         = ?                                          \n")
                 .append("     ,   attstime            = ?                                          \n")
                 .append("     ,   attetime            = ?                                          \n")

                 .append("     ,   isgoyong			   = ?											\n")
	             .append("     ,   goyongpricemajor    = ?                                          \n")
	             .append("     ,   goyongpriceminor    = ?                                          \n")
	             .append("     ,   usebook             = ?                                          \n")
	             .append("     ,   bookname            = ?                                          \n")
	             .append("     ,   bookprice           = ?                                          \n")
                 
	             .append("     ,   reviewdays          = ?                                          \n")
                 .append("     ,   study_count         = ?                                          \n")
                 .append("     ,   study_time          = ?                                          \n")
                 .append("     ,   muserid      	   = ?                                          \n")
                 .append("     ,   musertel		       = ?                                          \n")
		         .append("     ,   AFTERSULSDATE      	   = ?                                      \n")
		         .append("     ,   AFTERSULEDATE		       = ?                                  \n");

            if (!"".equals(v_newsubjseq)) {     
            	sbSQL.append("     ,   subjseq		       = LPAD(?,4,'0')                                          \n");
            }
            
            //if (vec.size() > 0 ) {     
            	sbSQL.append("     ,   neweroom		       = ?                                          \n");
           // }
                 
            sbSQL.append(" where   subj                = ?                                          \n")
                 .append(" and     year                = ?                                          \n")
                 .append(" and     subjseq             = ?                                          \n");
            
            DecimalFormat df = new DecimalFormat("00");

            pstmt   = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setString (pidx++, v_propstart                        );
            pstmt.setString (pidx++, v_propend                          );
            pstmt.setString (pidx++, v_edustart                         );
            pstmt.setString (pidx++, v_eduend                           );
            pstmt.setString (pidx++, v_endfirst                         );
            pstmt.setString (pidx++, v_endfinal                         );
            pstmt.setString (pidx++, box.getStringDefault("p_ismultipaper","N"));
            pstmt.setString (pidx++, box.getString  ("p_subjnm"         ));
            pstmt.setString (pidx++, v_luserid                          ) ;
            pstmt.setInt    (pidx++, box.getInt     ("p_studentlimit"   ));
            pstmt.setInt    (pidx++, box.getInt     ("p_point"          ));
            pstmt.setInt    (pidx++, box.getInt     ("p_biyong"         ));
            pstmt.setInt    (pidx++, box.getInt     ("p_edulimit"       ));
            pstmt.setInt    (pidx++, box.getInt     ("p_warndays"       ));
            pstmt.setInt    (pidx++, box.getInt     ("p_stopdays"       ));
            pstmt.setInt    (pidx++, box.getInt     ("p_gradscore"      ));
            pstmt.setInt    (pidx++, box.getInt     ("p_gradstep"       ));
            pstmt.setInt    (pidx++, box.getInt     ("p_wstep"          ));
            pstmt.setInt    (pidx++, box.getInt     ("p_wmtest"         ));
            pstmt.setInt    (pidx++, box.getInt     ("p_wftest"         ));
            pstmt.setInt    (pidx++, box.getInt     ("p_wreport"        ));
            pstmt.setInt    (pidx++, box.getInt     ("p_wact"           ));
            pstmt.setInt    (pidx++, box.getInt     ("p_wetc1"          ));
            pstmt.setInt    (pidx++, box.getInt     ("p_wetc2"          ));
            pstmt.setString (pidx++, box.getString("p_proposetype"));        		// 운영자승인여부            
            pstmt.setInt    (pidx++, box.getInt     ("p_gradexam"       ));         // 수료기준(평가)
            pstmt.setInt    (pidx++, box.getInt     ("p_gradreport"     ));         // 수료기준(과제)
            pstmt.setInt    (pidx++, box.getInt     ("p_whtest"         ));         // 가중치(형성평가)
            pstmt.setDouble (pidx++, box.getDouble  ("p_rndcreditreq"   ));         // 학점배점(연구개발)-필수
            pstmt.setDouble (pidx++, box.getDouble  ("p_rndcreditchoice"));         // 학점배점(연구개발)-선택
            pstmt.setDouble (pidx++, box.getDouble  ("p_rndcreditadd"   ));         // 학점배점(연구개발)-지정가점
            pstmt.setDouble (pidx++, box.getDouble  ("p_rndcreditdeduct"));         // 학점배점(연구개발)-지정감점
            pstmt.setString (pidx++, box.getString  ("p_isablereview"   ));         // 복습가능여부
            pstmt.setInt    (pidx++, box.getInt     ("p_score"          ));         // 학점배점
            pstmt.setInt    (pidx++, box.getInt     ("p_tsubjbudget"    ));         // 과목예산
            pstmt.setString (pidx++, box.getStringDefault("p_rndjijung"     ,"N")); // 지정과목여부
            pstmt.setString (pidx++, box.getStringDefault("p_isessential"   ,"0")); // 
            pstmt.setInt    (pidx++, box.getInt     ("p_gradftest"      ));         // 
            pstmt.setInt    (pidx++, box.getInt     ("p_gradhtest"      ));         // 
            pstmt.setString (pidx++, box.getStringDefault("p_isvisible"     ,"Y")); // 학습자에게 보여주기
            pstmt.setString (pidx++, box.getString  ("p_place"          ));         // 교육장소
            pstmt.setString (pidx++, box.getString  ("p_placejh"        ));         // 집합장소
            pstmt.setInt    (pidx++, box.getInt     ("p_sulpaper1"       ));         // 설문지 번호
            pstmt.setInt    (pidx++, box.getInt     ("p_canceldays"     ));         // 취소날자
            pstmt.setString (pidx++, box.getStringDefault("p_ischarge"      , "N"));// 수강료 유/무료 구분
            pstmt.setString (pidx++, box.getString  ("p_isopenedu"      ));         // 열린교육 여부
            pstmt.setString (pidx++, box.getString  ("p_maleassignrate" ));         // 남성 할당 비율
            pstmt.setString (pidx++, box.getString  ("p_gradftest_flag" ));         // 수료기준-최종평가(필수/선택기준)
            pstmt.setString (pidx++, box.getString  ("p_gradexam_flag"  ));         // 수료기준-중간평가(필수/선택기준)
            pstmt.setString (pidx++, box.getString  ("p_gradhtest_flag" ));         // 수료기준-형성평가(필수/선택기준)
            pstmt.setString (pidx++, box.getString  ("p_gradreport_flag"));         // 수료기준-과제      (필수/선택기준)
            pstmt.setString (pidx++, box.getString  ("p_isattendchk"));             // 온라인출석체크 여부
            pstmt.setString (pidx++, df.format(box.getInt("p_att_stime")));         // 온라인출석체크 여부
            pstmt.setString (pidx++, df.format(box.getInt("p_att_etime")));         // 온라인출석체크 여부
            
            pstmt.setString (pidx++, box.getString("p_isgoyong"));					// 고용보험여부
            pstmt.setInt    (pidx++, box.getInt("p_goyongprice_major"));			// 고용보험-환급액(KT)
            pstmt.setInt    (pidx++, box.getInt("p_goyongprice_minor"));			// 고용보험-KT교육비
            pstmt.setString (pidx++, box.getString("p_usebook"));
            pstmt.setString (pidx++, box.getString("p_bookname"));
            pstmt.setInt    (pidx++, box.getInt("p_bookprice"));
            
            pstmt.setInt    (pidx++, box.getInt		("p_reviewdays"     ));			// 복습가능기간
            pstmt.setInt    (pidx++, box.getInt		("p_study_count"    ));			// 수료기준(접속횟수)
            pstmt.setInt    (pidx++, box.getInt		("p_study_time"     ));			// 수료기준(학습시간)
            
            pstmt.setString (pidx++, box.getString("p_muserid"));         			// 담당자아이디
            pstmt.setString (pidx++, box.getString("p_musertel"));        			// 담당자연락처
            
            pstmt.setString (pidx++, v_aftersdate);         			// 설문시작일자
            pstmt.setString (pidx++, v_afteredate);        			// 설문종료일자
            
            if (!"".equals(v_newsubjseq)) {     
            	pstmt.setString (pidx++, v_newsubjseq);        		// 새로운기수
            }
            
          //  if (vec.size() > 0) {     
            	pstmt.setString (pidx++, v_eroomseq);        		// 고사장번호
          //  }


            pstmt.setString (pidx++, v_subj                              );
            pstmt.setString (pidx++, v_year                              );
            pstmt.setString (pidx++, v_subjseq                           );
//System.out.println("!!!!!!!"+v_newsubjseq+"|"+v_subjseq);
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
           if ( isOk > 0 ) { 
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
    코스기수 생성
    @param      String  p_grcode        교육그룹
                String  p_gyear         교육연도
                String  p_grseq         교육기수
                String  p_course        코스코드
                DBConnectionManager     conn    DB Connection Manager
    @return isOk    1:make success,0:make fail
    **/
    public int makeCourseseq(String p_grcode, String p_gyear, String p_grseq, String p_course, String p_userid, DBConnectionManager conn) throws Exception { 
        PreparedStatement   pstmt       = null;
        ListSet             ls          = null;
        String              sql         = "";
        int                 isOk        = 0;
        String              v_cyear     = "";
        String              v_courseseq = "";
        String              v_subj      = "";

        try { 
            sql = "select ltrim(rtrim(to_char(to_number(nvl(max(courseseq),'0000')) +1, '0000'))) GRS "
                + "  from tz_courseseq "
                + " where course=" +SQLString.Format(p_course)
                + "   and cyear=" +SQLString.Format(p_gyear);

            ls  = conn.executeQuery(sql);
            System.out.println("sql===\n"+sql);
            if ( ls.next() )
                v_courseseq = ls.getString("GRS");
            else
                v_courseseq = "0001";

            if ( ls != null ) { 
                try { ls.close(); 
                } catch ( Exception e ) { } 
            }

            sql = "select coursenm, gradscore, gradfailcnt from tz_course where course=" +SQLString.Format(p_course);
            ls  = conn.executeQuery(sql);
            
            ls.next();

            sql = " insert into tz_courseseq ("
                + " course     , cyear      , courseseq  , grcode     , gyear,"
                + " grseq      , coursenm   , gradscore  , gradfailcnt, indate,"
                + " luserid    , ldate )  values (  "
                + " ?,?,?,?,?,  ?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),"
                + " ?, to_char(sysdate,'YYYYMMDDHH24MISS'))";
            
            pstmt   = conn.prepareStatement(sql);
            
            pstmt.setString (1 , p_course       );
            pstmt.setString (2 , p_gyear        );
            pstmt.setString (3 , v_courseseq    );
            pstmt.setString (4 , p_grcode       );
            pstmt.setString (5 , p_gyear        );
            pstmt.setString (6 , p_grseq        );
            pstmt.setString (7 , ls.getString("coursenm"    ));
            pstmt.setInt    (8 , ls.getInt   ("gradscore"   ));
            pstmt.setInt    (9 , ls.getInt   ("gradfailcnt" ));
            pstmt.setString (10, p_userid       );

            isOk = pstmt.executeUpdate();

            if ( isOk > 0) { 
                sql = "select subj from tz_coursesubj where course=" +SQLString.Format(p_course);
                
                if ( ls != null ) { 
                    try { 
                        ls.close(); 
                    } catch ( Exception e ) { } 
                }
                
                ls  = conn.executeQuery(sql);
                
                while ( ls.next() ) { 
                    v_subj  = ls.getString("subj");
                    
                    isOk    = makeSubjseq(p_grcode, p_gyear, p_grseq, p_course, p_gyear, v_courseseq, v_subj, p_userid, 0,0,0,"","","","",0,"","",conn, "","","","");
                    
                    if ( isOk == 0 )    
                        throw new Exception();
                }
            }
            
            if ( isOk > 0 ) { 
                conn.commit(); 
            }
        } catch ( Exception e ) { 
            isOk    = 0;
            conn.rollback();
            throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
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
        }

        return isOk;
     }

    
    /**
    과목기수 생성 - 과목정보를 상속 받는다.
    @param      String  p_grcode        교육그룹
                String  p_gyear         교육연도
                String  p_grseq         교육기수
                String  p_course        코스코드
                DBConnectionManager     conn    DB Connection Manager
    @return isOk    1:make success,0:make fail
    **/
    public int makeSubjseq(    String p_grcode, String p_gyear, String p_grseq
                            ,   String p_course, String p_cyear, String p_courseseq
                            ,   String p_subj,   String p_userid, int p_sulpaper1, int p_sulpaper2, int p_sulpaper3
                            ,   String p_propstart, String p_propend, String p_edustart
                            ,   String p_eduend, int p_canceldays, String p_copy_gyear
                            ,   String p_copy_grseq, DBConnectionManager conn
                            ,   String p_presdate, String p_preedate, String p_aftersdate, String p_afteredate
                           ) throws Exception { 

        PreparedStatement   pstmt               = null;
        ResultSet           rs                  = null;
        ListSet             ls                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 0;
        int                 isOk2               = 0;
        
        int                 iSysAdd             = 0;    // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_year              = "";
        String              v_subjseq           = "";
        String              v_isBelongCourse    = "Y";
        String              v_subjseqgr         = "";    // 교육그룹에 속한 과목기수
        String              v_expiredate        = "";
        String              v_contenttype       = "";

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
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls      = conn.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                v_contenttype   = ls.getString("contenttype");
            }
            
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            sbSQL.setLength(0);

            // 년도별 과정기수를 가지고 온다.
            sbSQL.append(" select  ltrim(rtrim(to_char(to_number(nvl(max(subjseq),'0000')) +1, '0000'))) GRS       \n")
                 .append(" from    tz_subjseq                                                                      \n")
                 .append(" where   subj    = " + StringManager.makeSQL(p_subj  ) + "                               \n")
                 .append(" and     year    = " + StringManager.makeSQL(p_gyear ) + "                               \n");

            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

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
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

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
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

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
                
                //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
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
                 .append("     ,   canceldays          , ischarge          , isopenedu        				                                \n")
                 .append("     ,   isgoyong            , goyongpricemajor  , goyongpriceminor  , usebook					   				\n")
                 .append("     ,   reviewdays          , study_count										                                \n")
                 .append("     ,   muserid             , musertel											                                \n")
                 //.append("     ,   presulpapernum      , presulsdate       , presuledate        				                            \n")
                 //.append("     ,   aftersulpapernum    , aftersulsdate     , aftersuledate        				                            \n")
                 //.append("     ,   goyongpricestand															                                \n")
                 .append(" ) values (                                                                                                       \n")
                 .append("         ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , to_char(sysdate,'YYYYMMDDHH24MISS')   , ?              , ?            				\n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                                                         		\n")
                 .append("     ,   ?                   , ?                 , ?                 , ?             		                    	\n")
                 .append("     ,   ?                   , ?                			                                                        \n")
                 .append("     ,   ?                   , ?                			                                                        \n")
                 //.append("     ,   ?                   , ?                 , ?                                                         		\n")
                 //.append("     ,   ?                   , ?                 , ?                                                         		\n")
                 //.append("     ,   ?                   		               			                                                        \n")
                 .append(" )                                                                                                                \n");
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                
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
            pstmt.setInt   (pidx++, ls.getInt   ("score"             ));
            pstmt.setString(pidx++, ls.getString("isablereview"      ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradexam"          ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradreport"        ));
            pstmt.setInt   (pidx++, ls.getInt   ("whtest"            ));
            pstmt.setString(pidx++, ls.getString("isessential"       ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradftest"         ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradhtest"         ));
            pstmt.setString(pidx++, ls.getString("place"             ));
            pstmt.setString(pidx++, ls.getString("bookname"          ));
            pstmt.setString(pidx++, ls.getString("bookprice"         ));
            pstmt.setInt   (pidx++, p_sulpaper1                        );
            pstmt.setString(pidx++, p_propstart                       );
            pstmt.setString(pidx++, p_propend                         );
            pstmt.setString(pidx++, p_edustart                        );
            pstmt.setString(pidx++, p_eduend                          );
            pstmt.setInt   (pidx++, p_canceldays                      );
            pstmt.setString(pidx++, ls.getString("ischarge"          ));
            pstmt.setString(pidx++, ls.getString("isopenedu"         ));
            
            pstmt.setString(pidx++, ls.getString("isgoyong"          ));
            pstmt.setInt   (pidx++, ls.getInt("goyongpricemajor"));		// 고용보험환급액(대기업)
            pstmt.setInt   (pidx++, ls.getInt("goyongpriceminor"));		// 고용보험환급액(우선지원대상)
            pstmt.setString(pidx++, ls.getString("usebook"          ));

            pstmt.setInt   (pidx++, ls.getInt("reviewdays"));
            pstmt.setInt   (pidx++, ls.getInt("study_count"));

            pstmt.setString(pidx++, ls.getString("muserid"));
            pstmt.setString(pidx++, ls.getString("musertel"));
            
            //pstmt.setInt   (pidx++, p_sulpaper2                   );
            //pstmt.setString(pidx++, p_presdate                          );
            //pstmt.setString(pidx++, p_preedate                          );
            //pstmt.setInt   (pidx++, p_sulpaper3                 );
            //pstmt.setString(pidx++, p_aftersdate                          );
            //pstmt.setString(pidx++, p_afteredate                          );
            //pstmt.setInt   (pidx++, ls.getInt("goyongpricestand"));	// 고용보험-CP교육비
            isOk    = pstmt.executeUpdate();
            
            if(isOk > 0) {
                SubjectBean subBean = new SubjectBean();
                // 과정기수별 자료실 Insert
                subBean.InsertBds(conn, p_subj,"SD", " 과정 "+v_subjseq+"차수 자료실", p_gyear, v_subjseq);
    
                // 과정기수별 질문방 Insert
                subBean.InsertBds(conn, p_subj,"SQ", " 과정 "+v_subjseq+"차수 질문방", p_gyear, v_subjseq);
                
                // 과정기수별 게시판 Insert
                subBean.InsertBds(conn, p_subj,"SB", " 과정 "+v_subjseq+"차수 게시판", p_gyear, v_subjseq);  
            }
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[기수등록여부 isOk] : " + " [\n" + isOk + "\n]");

            // 학습종료일이 있을경우 과제마감일시를 종료일로 세팅
            if ( p_eduend.length() >= 8) { 
                v_expiredate    = StringManager.substring(p_eduend,0,8);
            } else { 
                v_expiredate    = "";
            }

            if ( isOk > 0) { 
                //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[Report 복사 isOk] : " + " [\n" + isOk + "\n]");

                /*
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
                     .append(" )   select  subj                                                             \n")
                     .append("         ,   " + SQLString.Format(p_gyear    ) + "                            \n")
                     .append("         ,   " + SQLString.Format(v_subjseq  ) + "                            \n")
                     .append("         ,   projseq                                                          \n")
                     .append("         ,   ordseq                                                           \n")
                     .append("         ,   lesson                                                           \n")
                     .append("         ,   reptype                                                          \n")
                     .append("         ,   isopen                                                           \n")
                     .append("         ,   isopenscore                                                      \n")
                     .append("         ,   title                                                            \n")
                     .append("         ,   contents                                                         \n")
                     .append("         ,   score                                                            \n")
                     .append("         ,   " + SQLString.Format(v_expiredate)  + "                          \n")
                     .append("         ,   upfile                                                           \n")
                     .append("         ,   upfile2                                                          \n")
                     .append("         ,   " + SQLString.Format(p_userid)      + "                          \n")
                     .append("         ,   to_char(sysdate,'yyyymmddhh24miss')                              \n")
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
                    sbSQL.append(" and year|| subjseq = (                                                   \n")    
                         .append("                           select  max(year || subjseq)                   \n")
                         .append("                           from    tz_projord                             \n")
                         .append("                           where   subj = " + SQLString.Format(p_subj) + "\n")
                         .append("                       )                                                  \n");
                } else { 
                    // 지정된 과목기수 정보를 가져오고
                    sbSQL.append(" and year     = " + SQLString.Format(p_copy_gyear ) + "                   \n")
                         .append(" and subjseq  = " + SQLString.Format(p_copy_grseq ) + "                   \n");
                }

                System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                isOk    = conn.executeUpdate(sbSQL.toString());
                
                isOk    = 1;
                */
                // 2008.11.29 김미향 KT인재개발원에 맞게 수정
                // 리포트 셋트별로 관리한다.
                sbSQL.setLength(0);

                // Report 출제정보 Copy;
                sbSQL.append(" insert into tz_projgrp                           \n");
                sbSQL.append(" (                                                \n");
                sbSQL.append("        subj      , year     , subjseq  , grpseq  \n");
                sbSQL.append("      , projgubun , sdate    , edate    , luserid \n");
                sbSQL.append("      , ldate     , grpseqnm , reptype            \n");
                sbSQL.append(" )                                                \n");
                sbSQL.append(" select subj                                      \n");
                sbSQL.append("      , " + SQLString.Format(p_gyear    ) + "     \n");
                sbSQL.append("      , " + SQLString.Format(v_subjseq  ) + "     \n");
                sbSQL.append("      , grpseq                                    \n");
                sbSQL.append("      , projgubun                                 \n");
                sbSQL.append("      , sdate                                     \n");
                sbSQL.append("      , " + SQLString.Format(v_expiredate)  + "   \n");
                sbSQL.append("      , " + SQLString.Format(p_userid)      + "   \n");
                sbSQL.append("      , to_char(sysdate,'yyyymmddhh24miss')       \n");
                sbSQL.append("      , grpseqnm                                  \n");
                sbSQL.append("      , reptype                                   \n");
                sbSQL.append(" from   tz_projgrp                                \n");
                sbSQL.append(" where  subj = " + SQLString.Format(p_subj) + "   \n");
                sbSQL.append(" and    year || subjseq = (                                               \n");    
                sbSQL.append("                           select  max(year || subjseq)                   \n");
                sbSQL.append("                           from    tz_projgrp                             \n");
                sbSQL.append("                           where   subj = " + SQLString.Format(p_subj) + "\n");
                sbSQL.append("                          )                                               \n");

                isOk    = conn.executeUpdate(sbSQL.toString());       
                
                
                sbSQL.setLength(0);

                // Report 출제정보 Copy;
                sbSQL.append(" insert into tz_projmap                           \n");
                sbSQL.append(" (                                                \n");
                sbSQL.append("        subj      , year     , subjseq    		\n");
                sbSQL.append("      , grpseq    , ordseq  					\n");
                sbSQL.append("      , luserid   ,ldate                			\n");
                sbSQL.append(" )                                                \n");
                sbSQL.append(" select subj                                      \n");
                sbSQL.append("      , " + SQLString.Format(p_gyear    ) + "     \n");
                sbSQL.append("      , " + SQLString.Format(v_subjseq  ) + "     \n");
                sbSQL.append("      , grpseq                                    \n");
                sbSQL.append("      , ordseq                                    \n");
                sbSQL.append("      , " + SQLString.Format(p_userid)      + "   \n");
                sbSQL.append("      , to_char(sysdate,'yyyymmddhh24miss')       \n");
                sbSQL.append(" from   tz_projmap                                \n");
                sbSQL.append(" where  subj = " + SQLString.Format(p_subj) + "   \n");
                sbSQL.append(" and    year || subjseq = (                                               \n");    
                sbSQL.append("                           select  max(year || subjseq)                   \n");
                sbSQL.append("                           from    tz_projmap                             \n");
                sbSQL.append("                           where   subj = " + SQLString.Format(p_subj) + "\n");
                sbSQL.append("                          )                                               \n");

                isOk    = conn.executeUpdate(sbSQL.toString());        
                
                
                // 평가문제지 Copy;
                ExamPaperBean   exambean    = new ExamPaperBean();
                isOk                        = exambean.insertExamPaper(p_subj, p_gyear, v_subjseq, p_userid);
                isOk                        = 1;
                
                //sbSQL.setLength(0);                

                // 집합강좌 Copy;
                /*
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
                     .append("         ,   to_char(sysdate,'YYYYMMDDHH24MISS')                                          \n")
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
                */
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

        return isOk;
    }
    
    
    /**
    과목기수 생성 - 과목정보를 상속 받는다.
    @param      String  p_grcode        교육그룹
                String  p_gyear         교육연도
                String  p_grseq         교육기수
                String  p_course        코스코드
                DBConnectionManager     conn    DB Connection Manager
    @return isOk    1:make success,0:make fail
    **/
    public int makeSubjseq(     String p_grcode, String p_gyear, String p_grseq
                            ,   String p_course, String p_cyear, String p_courseseq
                            ,   String p_subj,   String p_userid, int p_sulpaper, int p_presulpaper, int p_aftersulpaper
                            ,   String p_propstart, String p_propend, String p_edustart
                            ,   String p_eduend, int p_canceldays, String p_copy_gyear
                            ,   String p_copy_grseq, String p_isblended, String p_isexpert, DBConnectionManager conn
                            ,	String p_presulsdate, String p_presuledate, String p_aftersulsdate, String  p_aftersuledate
                            ,	String p_mtest_start, String p_mtest_end, String p_ftest_start, String  p_ftest_end
                            ,	String p_mreport_start, String p_mreport_end, String p_freport_start, String  p_freport_end
                           ) throws Exception { 

        PreparedStatement   pstmt               = null;
        ResultSet           rs                  = null;
        ListSet             ls                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 0;
        int                 isOk2               = 0;
        
        int                 iSysAdd             = 0;    // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_year              = "";
        String              v_subjseq           = "";
        String              v_isBelongCourse    = "Y";
        String              v_subjseqgr         = "";    // 교육그룹에 속한 과목기수
        String              v_expiredate        = "";
        String              v_contenttype       = "";

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
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls      = conn.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                v_contenttype   = ls.getString("contenttype");
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
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

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
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

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
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

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
                
                //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
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
                 .append("     ,   usesubjseqapproval  , useproposeapproval, usemanagerapproval, score                                      \n")
                 .append("     ,   rndcreditreq        , rndcreditchoice   , rndcreditadd      , rndcreditdeduct                            \n")
                 .append("     ,   rndjijung           , isablereview      , gradexam          , gradreport                                 \n")
                 .append("     ,   whtest              , isessential       , gradftest         , gradhtest                                  \n")
                 .append("     ,   place               , placejh           , bookname          , bookprice                                  \n")
                 .append("     ,   sulpapernum         , propstart         , propend           , edustart                                   \n")
                 .append("     ,   eduend              , canceldays        , ischarge          , isopenedu                                  \n")
                 .append("     ,   maleassignrate      , isblended         , isexpert         			                             	    \n")
                 .append("     ,   isgoyong            , goyongpricemajor  , goyongpriceminor  , usebook				     				\n")
                 .append("     ,   reviewdays          , study_count       , study_time						                                \n")
                 .append("     ,   muserid             , musertel												                            \n")
                 //.append("     ,   presulpapernum      , presulsdate       , presuledate						                            \n")
                 //.append("     ,   aftersulpapernum    , aftersulsdate     , aftersuledate						                            \n")
                 //.append("     ,   mtest_start         , mtest_end		   , ftest_start	   , ftest_end		                            \n")
                 //.append("     ,   mreport_start       , mreport_end	   , freport_start	   , freport_end		                        \n")
                 //.append("     ,   goyongpricestand												                            				\n")
                 .append(" ) values (                                                                                                       \n")
                 .append("         ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , to_char(sysdate,'YYYYMMDDHH24MISS')   , ?              , ?				            \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?                                          \n")
                 .append("     ,   ?                   , ?                 , ?                		                                        \n")
                 .append("     ,   ?                   , ?                 , ?                 , ?              	                        \n")
                 .append("     ,   ?                   , ?                 , ?               		                                        \n")
                 .append("     ,   ?                   , ?                 		              		                                        \n")                 
                 //.append("     ,   ?                   , ?                 , ?               		                                        \n")
                 //.append("     ,   ?                   , ?                 , ?               		                                        \n")
                 //.append("     ,   ?                   , ?                 , ?                 , ?              	                        \n")
                 //.append("     ,   ?                   , ?                 , ?                 , ?              	                        \n")
                 //.append("     ,   ?                 	                		              		                                        \n")
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
            pstmt.setString(pidx++, "");
            pstmt.setString(pidx++, "");
            pstmt.setString(pidx++, "");
            pstmt.setInt   (pidx++, ls.getInt   ("score"             ));
            pstmt.setString(pidx++, "");
            pstmt.setString(pidx++, "");
            pstmt.setString(pidx++, "");
            pstmt.setString(pidx++, "");
            pstmt.setString(pidx++, "");
            pstmt.setString(pidx++, ls.getString("isablereview"      ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradexam"          ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradreport"        ));
            pstmt.setInt   (pidx++, ls.getInt   ("whtest"            ));
            pstmt.setString(pidx++, ls.getString("isessential"       ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradftest"         ));
            pstmt.setInt   (pidx++, ls.getInt   ("gradhtest"         ));
            pstmt.setString(pidx++, ls.getString("place"             ));
            pstmt.setString(pidx++, "");
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
            pstmt.setInt   (pidx++, 0);
            pstmt.setString(pidx++, p_isblended                       );
            pstmt.setString(pidx++, p_isexpert                        );

            pstmt.setString(pidx++, ls.getString("isgoyong"          ));
            pstmt.setInt   (pidx++, ls.getInt("goyongpricemajor"     ));	// 고용보험-대기업환급액)
            pstmt.setInt   (pidx++, ls.getInt("goyongpriceminor"     ));	// 고용보험-우선지원대상
            pstmt.setString(pidx++, ls.getString("usebook"           ));
            
            pstmt.setInt   (pidx++, ls.getInt("reviewdays"				));		// 복습기간
            pstmt.setInt   (pidx++, ls.getInt("study_count"				));		// 학습횟수
            pstmt.setInt   (pidx++, ls.getInt("study_time"				));		// 접속시간

            pstmt.setString(pidx++, ls.getString("muserid"				));		// 담당자 아이디
            pstmt.setString(pidx++, ls.getString("musertel"				));		// 담당자 연락처
            
            /*
            pstmt.setInt   (pidx++, p_presulpaper						);		//사전 설문 2008.11.14 오충현 수정
            pstmt.setString(pidx++, p_presulsdate                       );		//사전 설문 시작일
            pstmt.setString(pidx++, p_presuledate                       );		//사전 설문 종료일
            pstmt.setInt   (pidx++, p_aftersulpaper						);		//사후 설문 
            pstmt.setString(pidx++, p_aftersulsdate                     );		//사후 설문 시작일
            pstmt.setString(pidx++, p_aftersuledate                     );		//사후 설문 종료일

            pstmt.setString(pidx++, p_mtest_start	);		// 형성평가 시작일
            pstmt.setString(pidx++, p_mtest_end		);		// 형성평가 종료일
            pstmt.setString(pidx++, p_ftest_start	);		// 최종평가 시작일
            pstmt.setString(pidx++, p_ftest_end		);		// 최종평가 종료일
            pstmt.setString(pidx++, p_mreport_start	);		// 중간평가 시작일
            pstmt.setString(pidx++, p_mreport_end	);		// 중간평가 종료일
            pstmt.setString(pidx++, p_freport_start	);		// 기말평가 시작일
            pstmt.setString(pidx++, p_freport_end	);		// 기말평가 종료일

            pstmt.setInt   (pidx++, ls.getInt("goyongpricestand"));	// 고용보험-CP교육비
            
            */
            isOk    = pstmt.executeUpdate();
            
            if(isOk > 0) {
                SubjectBean subBean = new SubjectBean();
                // 과정기수별 자료실 Insert
                subBean.InsertBds(conn, p_subj,"SD", " 과정 "+v_subjseq+"차수 자료실", p_gyear, v_subjseq);
    
                // 과정기수별 질문방 Insert
                subBean.InsertBds(conn, p_subj,"SQ", " 과정 "+v_subjseq+"차수 질문방", p_gyear, v_subjseq);
                
                // 과정기수별 게시판 Insert
                subBean.InsertBds(conn, p_subj,"SB", " 과정 "+v_subjseq+"차수 게시판", p_gyear, v_subjseq);                
            }
            
            //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[기수등록여부 isOk] : " + " [\n" + isOk + "\n]");

            // 학습종료일이 있을경우 과제마감일시를 종료일로 세팅
            if ( p_eduend.length() >= 8) { 
                v_expiredate    = StringManager.substring(p_eduend,0,8);
            } else { 
                v_expiredate    = "";
            }

            if ( isOk > 0) { 
                //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[Report 복사 isOk] : " + " [\n" + isOk + "\n]");
                /*
                sbSQL.setLength(0);

                // Report 출제정보 Copy;

                sbSQL.append(" INSERT INTO tz_projord                                                       \n")
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
                     .append("         ,   to_char(sysdate,'YYYYMMDDHH24MISS')                              \n")
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
                    sbSQL.append(" and year|| subjseq = (                                                   \n")    
                         .append("                           select  max(year || subjseq)                   \n")
                         .append("                           from    tz_projord                             \n")
                         .append("                           where   subj = " + SQLString.Format(p_subj) + "\n")
                         .append("                       )                                                  \n");
                } else { 
                    // 지정된 과목기수 정보를 가져오고
                    sbSQL.append(" and year     = " + SQLString.Format(p_copy_gyear ) + "                   \n")
                         .append(" and subjseq  = " + SQLString.Format(p_copy_grseq ) + "                   \n");
                }

                System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                isOk    = conn.executeUpdate(sbSQL.toString());
                
                isOk    = 1;
*/ 
                // 2008.11.29 김미향 KT인재개발원에 맞게 수정
                // 리포트 셋트별로 관리한다.
                sbSQL.setLength(0);

                // Report 출제정보 Copy;
                sbSQL.append(" insert into tz_projgrp                           \n");
                sbSQL.append(" (                                                \n");
                sbSQL.append("        subj      , year     , subjseq  , grpseq  \n");
                sbSQL.append("      , projgubun , sdate    , edate    , luserid \n");
                sbSQL.append("      , ldate     , grpseqnm , reptype  , grptotalscore \n");
                sbSQL.append(" )                                                \n");
                sbSQL.append(" select subj                                      \n");
                sbSQL.append("      , " + SQLString.Format(p_gyear    ) + "     \n");
                sbSQL.append("      , " + SQLString.Format(v_subjseq  ) + "     \n");
                sbSQL.append("      , grpseq                                    \n");
                sbSQL.append("      , projgubun                                 \n");
                sbSQL.append("      , sdate                                     \n");
                sbSQL.append("      , " + SQLString.Format(v_expiredate)  + "   \n");
                sbSQL.append("      , " + SQLString.Format(p_userid)      + "   \n");
                sbSQL.append("      , to_char(sysdate,'yyyymmddhh24miss')       \n");
                sbSQL.append("      , grpseqnm                                  \n");
                sbSQL.append("      , reptype                                   \n");
                sbSQL.append("      , grptotalscore                             \n");
                sbSQL.append(" from   tz_projgrp                                \n");
                sbSQL.append(" where  subj = " + SQLString.Format(p_subj) + "   \n");
            	sbSQL.append(" and    year || subjseq = (                                               \n");    
                sbSQL.append("                           select  max(year || subjseq)                   \n");
                sbSQL.append("                           from    tz_projgrp                             \n");
                sbSQL.append("                           where   subj = " + SQLString.Format(p_subj) + "\n");
                sbSQL.append("                          )                                               \n");                

                //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                isOk    = conn.executeUpdate(sbSQL.toString());
                
                
                sbSQL.setLength(0);

                // Report 출제정보 Copy;
                sbSQL.append(" insert into tz_projmap                           \n");
                sbSQL.append(" (                                                \n");
                sbSQL.append("        subj      , year     , subjseq    		\n");
                sbSQL.append("      , grpseq    , ordseq  					\n");
                sbSQL.append("      , luserid   ,ldate                			\n");
                sbSQL.append(" )                                                \n");
                sbSQL.append(" select subj                                      \n");
                sbSQL.append("      , " + SQLString.Format(p_gyear    ) + "     \n");
                sbSQL.append("      , " + SQLString.Format(v_subjseq  ) + "     \n");
                sbSQL.append("      , grpseq                                    \n");
                sbSQL.append("      , ordseq                                    \n");
                sbSQL.append("      , " + SQLString.Format(p_userid)      + "   \n");
                sbSQL.append("      , to_char(sysdate,'yyyymmddhh24miss')       \n");
                sbSQL.append(" from   tz_projmap                                \n");
                sbSQL.append(" where  subj = " + SQLString.Format(p_subj) + "   \n");
            	sbSQL.append(" and    year || subjseq = (                                               \n");    
                sbSQL.append("                           select  max(year || subjseq)                   \n");
                sbSQL.append("                           from    tz_projmap                             \n");
                sbSQL.append("                           where   subj = " + SQLString.Format(p_subj) + "\n");
                sbSQL.append("                          )                                               \n");
                
                //System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                isOk    = conn.executeUpdate(sbSQL.toString());        
                
                
                // 평가문제지 Copy;
                ExamPaperBean   exambean    = new ExamPaperBean();
                isOk                        = exambean.insertExamPaper(p_subj, p_gyear, v_subjseq, p_userid);
                isOk                        = 1;
               
                sbSQL.setLength(0);
                

                // 집합강좌 Copy;
                /*
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
                     .append("         ,   to_char(sysdate,'YYYYMMDDHH24MISS')                                          \n")
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
                     
                System.out.println(this.getClass().getName() + "." + "makeSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                isOk = conn.executeUpdate(sbSQL.toString());
                */
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

        return isOk;
    }
     
     /**
     코스기수 삭제
     @param      String  p_course        코스코드
                 String  p_cyear         코스연도
                 String  p_courseseq         코스기수
                 String  p_userid        ID
                 DBConnectionManager     conn    DB Connection Manager
     @return isOk    1:make success,0:make fail
     **/
     public int delCourseseq(String p_course, String p_cyear, String p_courseseq, String p_userid, DBConnectionManager conn) throws Exception { 
         PreparedStatement   pstmt       = null;
         ListSet             ls          = null;
         String              sql         = "";
         int                 isOk        = 0;
         String              v_cyear     = "";
         String              v_courseseq = "";
         String              v_subj      = "";

         try { 
             sql = "select a.subj, b.year, b.subjseq, isrequired from tz_coursesubj a, tz_subjseq b "
                 + " where a.subj=b.subj and a.course=b.course and a.course=" +SQLString.Format(p_course)
                 + "   and b.cyear=" +SQLString.Format(p_cyear) + " and b.courseseq=" +SQLString.Format(p_courseseq);
                 
             ls  = conn.executeQuery(sql);
             
             while ( ls.next() ) { 
                 isOk = delSubjseq( ls.getString("subj"),ls.getString("year"),ls.getString("subjseq"), p_userid, conn);
                 
                 if ( isOk == 0 )
                     throw new Exception();
             }

             if ( isOk > 0) { 
                 sql     = "delete tz_courseseq where course=? and cyear=? and courseseq=?";
                 
                 pstmt   = conn.prepareStatement(sql);
                 
                 pstmt.setString(1, p_course     );
                 pstmt.setString(2, p_cyear      );
                 pstmt.setString(3, p_courseseq  );
                 
                 isOk    = pstmt.executeUpdate();
             }

             if ( isOk > 0 ) {
                 conn.commit(); 
             } else {
                 throw new Exception();
             }    
         } catch ( Exception e ) { 
             isOk    = 0;
             conn.rollback();
             throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
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
         }

         return isOk;
     }

     
     /**
     과목기수 삭제
     @param      String  p_subj          과목코드
                 String  p_year          연도
                 String  p_subjseq       과목기수
                 String  p_userid        ID
                 DBConnectionManager     conn    DB Connection Manager
     @return isOk    1:make success,0:make fail

     Process:    delete  tz_propose,     tz_student,     tz_stold ( == > must be Backup)
                 delete  tz_progress,    tz_progressobj, tz_progress_sco,tz_vodprogress,
                         tz_projord,     tz_projrep,     tz_examresult,  tz_exampaper,   tz_activity_ans,
                         tz_opin,        tz_expect,      tz_gong,        tz_toron,       tz_torontp,
                         tz_qna,         tz_objqna,      tz_class,       tz_classtutor,
                         tz_sulresult,   tz_sulresultp,  tz_suleach,     tz_bookmark,
     **/
     public int delSubjseq( String p_subj, String p_year, String p_subjseq, String p_userid, DBConnectionManager conn) throws Exception { 
         String      sql             = "";
         int         isOk            = 0;

         ArrayList   sqlArr          = new ArrayList();

         sqlArr.add("tz_progress"        ); // 진도
         // sqlArr.add("tz_progressobj"  );
         // sqlArr.add("tz_progress_sco" );
         // sqlArr.add("tz_vodprogress"  );
         sqlArr.add("tz_projgrp"         );//리포트 문제지
         sqlArr.add("tz_projmap"         );//리포트 문제매핑         
         sqlArr.add("tz_projrep"         );//리포트 제출
         sqlArr.add("tz_exampaper"       );//평가 문제지         
         sqlArr.add("tz_examresult"      );//평가 결과
         // sqlArr.add("tz_activity_ans" );
         // sqlArr.add("tz_opin"         );
         // sqlArr.add("tz_expect"       );
         sqlArr.add("tz_gong"            );//공지
         sqlArr.add("tz_toron"           );//토론방
         sqlArr.add("tz_torontp"         );//토론의견
         // sqlArr.add("tz_qna"          );
         // sqlArr.add("tz_objqna"       );
         sqlArr.add("tz_class"           );//분반
         sqlArr.add("tz_classtutor"      );//강사분반
         // sqlArr.add("tz_sulresult"    );
         // sqlArr.add("tz_sulresultp"   );
         sqlArr.add("tz_suleach"         );//설문결과
         // sqlArr.add("tz_bookmark"     );
         sqlArr.add("tz_propose"         );//수강신청정보
         sqlArr.add("tz_student"         );//수강생정보
         sqlArr.add("tz_stold"           );//수료정보
         // sqlArr.add("tz_subjseqtemp"  );
         sqlArr.add("tz_subjseq"         );//과정차수정보
         sqlArr.add("tz_offsubjlecture"  );//집합과정정보
         sqlArr.add("tz_bds"  			 );//게시판마스터  
         sqlArr.add("tz_board"  		 );//게시판            

//       BackUp 정책에 따라 Backup 및 Logging Process추가할 것.
         
         /* 삭제 교육기수에 해당하는 교육생 정보 backup */
         /*
         sql  = "insert into  tz_cancel     \n "
        	 + " (                          \n "
        	 + "   subj, year, subjseq, userid, seq, cancelkind, canceldate, reason, luserid, ldate, reasoncd, isdeleted, chkfinal   \n "
        	 + "  )                         \n "
        	 + " select subj, year, subjseq, userid, (select nvl(max(seq), 0)+1 from tz_cancel where subj = "+StringManager.makeSQL(p_subj) + " and year = "+StringManager.makeSQL(p_year)+" and subjseq = "+StringManager.makeSQL(p_subjseq)+" and userid = a.userid )      \n "
        	 + "       , 'D', to_char(sysdate,'YYYYMMDDHH24MISS'), '', '"+p_userid+"', to_char(sysdate,'YYYYMMDDHH24MISS'), '', 'N', chkfinal     \n "
        	 + "    from tz_propose a \n "
             + "  where subj    =" + StringManager.makeSQL(p_subj)
             + "    and year    =" + StringManager.makeSQL(p_year)
             + "    and subjseq =" + StringManager.makeSQL(p_subjseq);
             
         isOk = conn.executeUpdate(sql);
         */
         /* end */
         
         try { 

             // 마스터과목 매칭 삭제
             sql  = "delete  TZ_MASTERSUBJ "
                  + " where subjcourse=" +StringManager.makeSQL(p_subj)
                  + "   and year=" +StringManager.makeSQL(p_year)
                  + "   and subjseq=" +StringManager.makeSQL(p_subjseq);
                  
             isOk = conn.executeUpdate(sql);

             // 관련 테이블 삭제
             for ( int i = 0; i<sqlArr.size(); i++ ) { 
                 sql  = " delete  " + (String)sqlArr.get(i)
                      + " where subj=" +StringManager.makeSQL(p_subj)
                      + " and year=" +StringManager.makeSQL(p_year)
                      + " and subjseq=" +StringManager.makeSQL(p_subjseq);

                 isOk += conn.executeUpdate(sql);
             }
             
             if ( isOk > 0 ) { 
                 conn.commit(); 
             }
         } catch ( Exception e ) { 
             isOk    = 0;
             conn.rollback();
             throw new Exception("sql = " + sql + "\r\n" + e.getMessage());
         } finally { 
         }

         return isOk;
    }

     
    /**
    집합과목 강좌 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectLectureList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr     = null;
        ListSet             ls1         = null;
        ArrayList           list1       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        OffSubjLectureData  data1       = null;
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_year      = box.getString("p_year"    );  // 년도
        String              v_subj      = box.getString("p_subj"    );  // 과목
        String              v_subjseq   = box.getString("p_subjseq" );  // 과목 기수
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();

            // select lecture,sdesc,lectdate,lectsttime,tutorid,tutorname

            sbSQL.append(" select  lecture                                              \n")
                 .append("     ,   sdesc                                                \n")
                 .append("     ,   lectdate                                             \n")
                 .append("     ,   lectsttime                                           \n")
                 .append("     ,   tutorid                                              \n")
                 .append("     ,   (                                                    \n")
                 .append("             select name                                      \n")
                 .append("             from    TZ_TUTOR                                 \n")
                 .append("             where   userid = A.tutorid                       \n")
                 .append("         )                   tutorname                        \n")
                 .append(" from    TZ_OFFSUBJLECTURE A                                  \n")
                 .append(" where   subj    = " + SQLString.Format(v_subj   ) + "        \n")
                 .append(" and     year    = " + SQLString.Format(v_year   ) + "        \n")
                 .append(" and     subjseq = " + SQLString.Format(v_subjseq) + "        \n")
                 .append(" order by lecture                                             \n");
            
            System.out.println(this.getClass().getName() + "." + "selectLectureList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls1     = connMgr.executeQuery(sbSQL.toString());

            while ( ls1.next() ) { 
                data1   = new OffSubjLectureData();
                
                data1.setLecture    ( ls1.getString("lecture"   ));
                data1.setSdesc      ( ls1.getString("sdesc"     ));
                data1.setLectdate   ( ls1.getString("lectdate"  ));
                data1.setLectsttime ( ls1.getString("lectsttime"));
                data1.setTutorid    ( ls1.getString("tutorid"   ));
                data1.setTutorname  ( ls1.getString("tutorname" ));
                
                list1.add(data1);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls1 != null ) { 
                try { 
                    ls1.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list1;
    }
     

     /**
     강사 리스트
     @param box      receive from the form object and session
     @return ArrayList
     */
     public ArrayList selectTutorList(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet                ls1     = null;
         ArrayList              list1   = null;
         StringBuffer           sbSQL   = new StringBuffer("");
         OffSubjLectureData     data1   = null;
         
         int                    iSysAdd = 0;                        // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
         
         String                 v_subj  = box.getString("p_subj");  // 과목
         
         try { 
             connMgr     = new DBConnectionManager();
             list1       = new ArrayList();
         
             // select userid,name
             sbSQL.append(" select  A.userid                                         \n")
                  .append("     ,   A.name                                           \n")
                  .append(" from    TZ_TUTOR    A                                    \n")
                  .append("     ,   TZ_SUBJMAN  B                                    \n")
                  .append(" where   B.subj      = " + SQLString.Format(v_subj) + "   \n")
                  .append(" and     A.userid    = B.userid                           \n")
                  .append(" and     B.gadmin    = 'P1'                               \n")
                  .append(" order by A.name                                          \n");    
                  
             System.out.println(this.getClass().getName() + "." + "selectTutorList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
         
             ls1     = connMgr.executeQuery(sbSQL.toString());
         
             while ( ls1.next() ) { 
                 data1   = new OffSubjLectureData();
                 
                 data1.setTutorid    ( ls1.getString("userid"));
                 data1.setTutorname  ( ls1.getString("name"  ));
                 
                 list1.add(data1);
             }
         } catch ( SQLException e ) {
             ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
             throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } catch ( Exception e ) {
             ErrorManager.getErrorStackTrace(e, box, "");
             throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } finally {
             if ( ls1 != null ) { 
                 try { 
                     ls1.close();  
                 } catch ( Exception e ) { } 
             }
             
             if ( connMgr != null ) { 
                 try { 
                     connMgr.freeConnection(); 
                 } catch ( Exception e ) { } 
             }
         }

         return list1;
     }            

     
     /**
     강좌 등록
     @param box      receive from the form object and session
     @return int     1:insert success,0:insert fail
     */
     public int insertLecture(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr         = null;
         PreparedStatement   pstmt2          = null;
         String              sql1            = "";
         String              sql2            = "";
         ListSet             ls1             = null;
         int                 isOk            = 0;
         
         String              v_user_id       = box.getSession("userid"       );
         String              v_subj          = box.getString ("p_subj"       );          // 과목
         String              v_year          = box.getString ("p_year"       );          // 년도
         String              v_subjseq       = box.getString ("p_subjseq"    );       // 과목 기수
         String              v_lectdate      = box.getString ("p_lectdate"   );
         String              v_lectsttime1   = box.getString ("p_lectsttime1");
         String              v_lectsttime2   = box.getString ("p_lectsttime2");
         String              v_lectsttime    = v_lectsttime1 + v_lectsttime2;
         String              v_lecttime      = box.getString ("p_lecttime"   );
         String              v_sdesc         = box.getString ("p_sdesc"      );
         String              v_tutorid       = box.getString ("p_tutorid"    );
         int                 v_lecture       = 0;
         
         try { 
             connMgr = new DBConnectionManager();

             // select max(lecture)
             sql1 = "select nvl(max(lecture), 0) from TZ_OFFSUBJLECTURE ";
             
             ls1 = connMgr.executeQuery(sql1);
             
             if ( ls1.next() ) { 
                 v_lecture = ls1.getInt(1) + 1;    
             }

             // insert TZ_OFFSUBJLECTURE table
             sql2 =  "insert into TZ_OFFSUBJLECTURE(subj,year,subjseq,lecture,lectdate,lectsttime,lecttime,sdesc,tutorid,luserid,ldate) ";
             sql2 +=  "values (?,?,?,?,?,?,?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
             
             pstmt2  = connMgr.prepareStatement(sql2);

             pstmt2.setString(1 , v_subj         );
             pstmt2.setString(2 , v_year         );
             pstmt2.setString(3 , v_subjseq      );
             pstmt2.setInt   (4 , v_lecture      );
             pstmt2.setString(5 , v_lectdate     );
             pstmt2.setString(6 , v_lectsttime   );
             pstmt2.setString(7 , v_lecttime     );
             pstmt2.setString(8 , v_sdesc        );
             pstmt2.setString(9 , v_tutorid      );
             pstmt2.setString(10, v_user_id      );
             
             isOk    = pstmt2.executeUpdate();
             if ( pstmt2 != null ) { pstmt2.close(); }
             Log.info.println(this, box, "insert TZ_OFFSUBJLECTURE subj=" +v_subj + ",year=" +v_year + ",subjseq" +v_subjseq + ",lecture" +v_lecture);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) {
                try { 
                    ls1.close(); 
                } catch ( Exception e1 ) { } 
            }
             
            if ( pstmt2 != null ) { 
                try { 
                    pstmt2.close(); 
                } catch ( Exception e1 ) { } 
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
    집합과목 강좌 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public OffSubjLectureData selectLecture(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        String sql1         = "";
        OffSubjLectureData data1 = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_lecture   = box.getString("p_lecture");       // 과목 기수
        try { 
            connMgr = new DBConnectionManager();
            // select lecture,lectdate,lectsttime,lecttime,sdesc,tutorid,tutorname
            sql1 = "select lecture,lectdate,lectsttime,lecttime,sdesc,tutorid, ";
            sql1 += "(select name from TZ_TUTOR where userid = A.tutorid) as tutorname ";
            sql1 += " from TZ_OFFSUBJLECTURE A ";
            sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql1 += " and subjseq=" +SQLString.Format(v_subjseq) + " and lecture=" +SQLString.Format(v_lecture);
            sql1 += " order by lecture";
//            System.out.println("sql1 ==  ==  ==  ==  ==  == > " +sql1);
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new OffSubjLectureData();
                    data1.setLecture( ls1.getString("lecture") );
                    data1.setLectdate( ls1.getString("lectdate") );
                    data1.setLectsttime( ls1.getString("lectsttime") );
                    data1.setLecttime( ls1.getString("lecttime") );
                    data1.setSdesc( ls1.getString("sdesc") );
                    data1.setTutorid( ls1.getString("tutorid") );
                    data1.setTutorname( ls1.getString("tutorname") );
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data1;
    }

     
    /**
    집합과목 강좌 수정
    @param box      receive from the form object and session
    @return int
    */
    public int updateLecture(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt1          = null;
        String              sql1            = "";
        int                 isOk            = 0;
        
        String              v_user_id       = box.getSession("userid"       );
        String              v_subj          = box.getString ("p_subj"       );  // 과목
        String              v_year          = box.getString ("p_year"       );  // 년도
        String              v_subjseq       = box.getString ("p_subjseq"    );  // 과목 기수
        String              v_lectdate      = box.getString ("p_lectdate"   );
        String              v_lectsttime1   = box.getString ("p_lectsttime1");
        String              v_lectsttime2   = box.getString ("p_lectsttime2");
        String              v_lectsttime    = v_lectsttime1 + v_lectsttime2;
        String              v_lecttime      = box.getString ("p_lecttime"   );
        String              v_sdesc         = box.getString ("p_sdesc"      );
        String              v_tutorid       = box.getString ("p_tutorid"    );
        int                 v_lecture       = box.getInt    ("p_lecture"    );
        
        try { 
            connMgr = new DBConnectionManager();

            // update TZ_OFFSUBJLECTURE table
            sql1 =  "update TZ_OFFSUBJLECTURE set lectdate=?,lectsttime=?,lecttime=?,sdesc=?,tutorid=?,luserid=?,ldate=to_char(sysdate,'YYYYMMDDHH24MISS') ";
            sql1 +=  "where subj=? and year=? and subjseq=? and lecture=?";
            
            pstmt1  = connMgr.prepareStatement(sql1);

            pstmt1.setString(1 , v_lectdate     );
            pstmt1.setString(2 , v_lectsttime   );
            pstmt1.setString(3 , v_lecttime     );
            pstmt1.setString(4 , v_sdesc        );
            pstmt1.setString(5 , v_tutorid      );
            pstmt1.setString(6 , v_user_id      );
            pstmt1.setString(7 , v_subj         );
            pstmt1.setString(8 , v_year         );
            pstmt1.setString(9 , v_subjseq      );
            pstmt1.setInt   (10, v_lecture      );

            isOk    = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) {
                try { 
                    pstmt1.close(); 
                } catch ( Exception e1 ) { } 
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
    집합과목 강좌 삭제
    @param box      receive from the form object and session
    @return int
    */
    public int deleteLecture(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt1      = null;
        String              sql1        = "";
        int                 isOk        = 0;
        
        String              v_user_id   = box.getSession("userid"   );
        String              v_subj      = box.getString ("p_subj"   );  // 과목
        String              v_year      = box.getString ("p_year"   );  // 년도
        String              v_subjseq   = box.getString ("p_subjseq");  // 과목 기수
        int                 v_lecture   = box.getInt    ("p_lecture");
        try { 
            connMgr = new DBConnectionManager();

            // delete TZ_OFFSUBJLECTURE table
            sql1  = " delete from TZ_OFFSUBJLECTURE ";
            sql1 += " where subj=? and year=? and subjseq=? and lecture=? ";
            
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_subj      );
            pstmt1.setString(2, v_year      );
            pstmt1.setString(3, v_subjseq   );
            pstmt1.setInt   (4, v_lecture   );

            isOk    = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + e.getMessage() );
        } finally { 
            if ( pstmt1 != null ) {
                try { 
                    pstmt1.close(); 
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
    지정된 교육기수에 일괄적으로 1일최대 학습량을 변경한다.
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    **/
     public int SaveEdulimit(RequestBox box) throws Exception { 
        DBConnectionManager     connMgr     = null;
        PreparedStatement       pstmt       = null;
        StringBuffer            sbSQL       = new StringBuffer("");
        int                     isOk        = 1;

        int                     iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String                  v_grcode    = box.getString ("p_grcode"     );
        String                  v_gyear     = box.getString ("p_gyear"      );
        String                  v_grseq     = box.getString ("p_grseq"      );
                            
        String                  v_edulimit  = box.getString ("p_edulimit"   );
        String                  v_luserid   = box.getSession("userid"       ); // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr     = new DBConnectionManager();

            sbSQL.append(" update tz_subjseq set                                        \n")
                 .append("         edulimit    = ?                                      \n")
                 .append("     ,   luserid     = ?                                      \n")
                 .append("     ,   ldate       = to_char(sysdate,'yyyymmddhh24miss')    \n")
                 .append(" where   grcode      = ?                                      \n")
                 .append(" and     gyear       = ?                                      \n")
                 .append(" and     grseq       = ?                                      \n");
            
            System.out.println(this.getClass().getName() + "." + "SaveEdulimit() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            System.out.println(this.getClass().getName() + "." + "SaveEdulimit() Printing Order " + ++iSysAdd + ". ======[v_edulimit] : " + " [\n" + v_edulimit + "\n]");
            System.out.println(this.getClass().getName() + "." + "SaveEdulimit() Printing Order " + ++iSysAdd + ". ======[v_luserid] : " + " [\n" + v_luserid + "\n]");
            System.out.println(this.getClass().getName() + "." + "SaveEdulimit() Printing Order " + ++iSysAdd + ". ======[v_grcode] : " + " [\n" + v_grcode + "\n]");
            System.out.println(this.getClass().getName() + "." + "SaveEdulimit() Printing Order " + ++iSysAdd + ". ======[v_gyear] : " + " [\n" + v_gyear + "\n]");
            System.out.println(this.getClass().getName() + "." + "SaveEdulimit() Printing Order " + ++iSysAdd + ". ======[v_grseq] : " + " [\n" + v_grseq + "\n]");
            
            pstmt.setString(1, v_edulimit   );
            pstmt.setString(2, v_luserid    );
            pstmt.setString(3, v_grcode     );
            pstmt.setString(4, v_gyear      );
            pstmt.setString(5, v_grseq      );

            isOk    = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            System.out.println(this.getClass().getName() + "." + "SaveEdulimit() Printing Order " + ++iSysAdd + ". ======[isOk] : " + " [\n" + isOk + "\n]");
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
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
    과목기수정보(학습창에서 사용)
    @param box      receive from the form object and session
    @return ArrayList
    */
     public DataBox getSubjInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        String sql1         = "";
        DataBox             dbox    = null;
        String  v_year      = box.getString("FIELD2");          // 년도
        String  v_subj      = box.getString("FIELD3");          // 과목
        String  v_subjseq   = box.getString("FIELD4");       // 과목 기수

        try { 
            connMgr = new DBConnectionManager();

            sql1 = "select subj, year, subjseq, subjnm, eduurl, isoutsourcing,   ";
            sql1 += "       cpsubj, cpsubjseq, contenttype, aesseq, aescontentid  ";
            sql1 += " from VZ_SCSUBJSEQ                                           ";
            sql1 += " where subj    = " +SQLString.Format(v_subj);
            if ( !v_subjseq.equals("0000") ) { 
              sql1 += "   and year    = " +SQLString.Format(v_year);
              sql1 += "   and subjseq = " +SQLString.Format(v_subjseq);
            }
            System.out.println("sql1 ==  ==  ==  ==  ==  == > " +sql1);
            ls1 = connMgr.executeQuery(sql1);

                if ( ls1.next() ) { 
                    dbox = ls1.getDataBox();
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
    
    
    /**
    직무인지 어학인지여부
    @param box      receive from the form object and session
    @return ArrayList
    */
     public Hashtable getSubjCodeInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        String sql1         = "";
        DataBox             dbox    = null;
        String  v_year      = box.getString("FIELD2");          // 년도
        String  v_subj      = box.getString("FIELD3");          // 과목
        String  v_subjseq   = box.getString("FIELD4");       // 과목 기수
        
        Hashtable output = new Hashtable();

        try { 
            connMgr = new DBConnectionManager();

            sql1 += " select                                \n";
            sql1 += "   matchcode                           \n";
            sql1 += "   from tz_subj a, tz_classfymatch b   \n";
            sql1 += " where                                 \n";
            sql1 += "   a.subj = '" +v_subj + "'               \n";
            sql1 += "   and a.upperclass = b.upperclass     \n";

            System.out.println("sql1 ==  ==  ==  ==  ==  == > " +sql1);
            ls1 = connMgr.executeQuery(sql1);

                if ( ls1.next() ) { 
                    // dbox = ls1.getDataBox();
                    output.put("matchcode", ls1.getString("matchcode") );
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return output;
    }


    /**
    설문 리스트 SELECT BOX 구성
    @param  name      SELECT BOX NAME
    @param  selected  선택된 설문
    @param  allcheck  처음안내문구 여부 : 0 -선택없음, 1 - 선택있음
    @return String    SELECT BOX 구성 문자
    **/
    public String selectSulPaper(String name, String selected, int allcheck, String sultype) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = "";
        String              sql     = "";
        String code = "";
        String codenm = "";

        result = "  <SELECT name=" + name + " > \n";

        if ( allcheck == 1) { 
          result += " <option value='' >==  ==  == =설문지를 선택하세요 ==  ==  == </option > \n";
        }

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select sulpapernum, sulpapernm from tz_sulpaper ";
            sql += "  where subj = 'ALL' and grcode = 'ALL'          ";
            sql += "    and sultype = '" + sultype + "'			   \n";			
            sql += " order by sulpapernum asc                        ";
        // System.out.println("sql == > " + sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                code = ls.getString("sulpapernum");
                codenm = ls.getString("sulpapernm");

                result += " <option value='" + code + "'";
                if ( selected.equals(code)) { 
                    result += " selected ";
                }
                
                result += " > " + codenm + "</option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    } 


    /**
    복사대상 교육기수용 교육년도
    @param  grcode    교유그룹
    @param  name      SELECT BOX NAME
    @param  selected  선택된 년도
    @param  change    change시 자바함수
    @param  allcheck  처음안내문구 여부 : 0 -선택없음, 1 - 선택있음
    @return String    SELECT BOX 구성 문자
    **/
    public String selectGyear(String grcode, String name, String selected, String change, int allcheck) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = "";
        String              sql     = "";
        String code = "";
        String codenm = "";

        result = "  <SELECT name=" + name + " " + change + " > \n";

        if ( allcheck == 1) { 
          result += " <option value='' >== 선택 == </option > \n";
        }

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select distinct gyear ";
            sql += "   from tz_grseq       ";
            sql += "   where grcode = " + SQLString.Format(grcode);
            sql += "  order by gyear desc  ";
        // System.out.println("sql == > " + sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                code = ls.getString("gyear");
                codenm = ls.getString("gyear");

                result += " <option value='" + code + "'";
                if ( selected.equals(code)) { 
                    result += " selected ";
                }
                
                result += " > " + codenm + "</option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    } 

    /**
    복사대상 교육기수용 교육년도
    @param  grcode    교육그룹
    @param  gyear     교육년도
    @param  name      SELECT BOX NAME
    @param  selected  선택된 년도
    @param  change    change시 자바함수
    @param  allcheck  처음안내문구 여부 : 0 -선택없음, 1 - 선택있음
    @return String    SELECT BOX 구성 문자
    **/
    public String selectGrseq(String grcode, String gyear, String name, String selected, String change, int allcheck) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = "";
        String              sql     = "";
        String code = "";
        String codenm = "";

        result = "  <SELECT name=" + name + " " + change + " > \n";

        if ( allcheck == 1) { 
          result += " <option value='' >==  ==  ==  == =선택 ==  ==  ==  == =</option > \n";
        }

        try { 
            connMgr = new DBConnectionManager();

            sql = "select grseq, grseqnm ";
            sql += " from tz_grseq       ";
//            if ( !gyear.equals("") ) { 
              sql += " where gyear = " + SQLString.Format(gyear);
//            } else { 
//              sql += " where gyear = (select max(gyear) from tz_grseq where grcode = " + SQLString.Format(grcode) + ")";
//            }
            sql += " and grcode = " + SQLString.Format(grcode);
            sql += " order by grseq";

        // System.out.println("sql == > " + sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                code = ls.getString("grseq");
                codenm = ls.getString("grseqnm");

                result += " <option value='" + code + "'";
                if ( selected.equals(code)) { 
                    result += " selected ";
                }
                
                result += " > " + codenm + "</option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    } 

    /**
    교육기수 대상 회사
    @param  grcode    교육그룹
    @param  name      SELECT BOX NAME
    @param  selected  선택된 년도
    @param  change    change시 자바함수
    @param  allcheck  처음안내문구 여부 : 0 -선택없음, 1 - 선택있음
    @return String    SELECT BOX 구성 문자
    **/
    public String selectComp(String grcode, String name, String selected, String change, int allcheck) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = "";
        String              sql     = "";
        String code = "";
        String codenm = "";

        result = "  <SELECT name=" + name + " " + change + " > \n";

        if ( allcheck == 1) { 
          result += " <option value='' >==  ==  == 선택 ==  ==  == </option > \n";
        }

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select a.comp, b.compnm       ";
            sql += "  from tz_grcomp a, tz_comp b  ";
            sql += " where a.comp = b.comp         ";
            sql += "   and a.grcode = " + SQLString.Format(grcode);
            sql += "  order by a.comp              ";
        // System.out.println("sql == > " + sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                code = ls.getString("comp");
                codenm = ls.getString("compnm");

                result += " <option value='" + code + "'";
                if ( selected.equals(code)) { 
                    result += " selected ";
                }
                
                result += " > " + codenm + "</option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    } 


    /**
    과목/기수 코드 리스트 (입과일괄처리에서 다운로드)
    @param box          receive from the form object and session
    @return ArrayList   과목기수 코드 리스트
    */
    public ArrayList getSubjCodeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        try { 
            sql  = " select subj, subjnm, subjseq, subjseqgr, year ";
            sql += "   from vz_scsubjseq   ";
            sql += "  where 1=1          ";
            if ( !ss_grcode.equals("ALL"))     sql += " and grcode = " +SQLString.Format(ss_grcode);
            if ( !ss_grseq.equals("ALL"))      sql += " and grseq = " +SQLString.Format(ss_grseq);
            if ( !ss_uclass.equals("ALL"))     sql += " and scupperclass = " +SQLString.Format(ss_uclass);
            if ( !ss_mclass.equals("ALL"))     sql += " and scmiddleclass = " +SQLString.Format(ss_mclass);
            if ( !ss_lclass.equals("ALL"))     sql += " and sclowerclass = " +SQLString.Format(ss_lclass);
            if ( !ss_subjcourse.equals("ALL")) sql += " and scsubj = " +SQLString.Format(ss_subjcourse);
            if ( !ss_subjseq.equals("ALL"))    sql += " and scsubjseq = " +SQLString.Format(ss_subjseq);
            sql += " order by subj,subjseqgr";


            connMgr = new DBConnectionManager();
            list = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
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
    복사대상 교육기수용 교육년도 BL/전문가 양성 과정을 위한 화면
    @param  grcode    교육그룹
    @param  gyear     교육년도
    @param  name      SELECT BOX NAME
    @param  selected  선택된 년도
    @param  change    change시 자바함수
    @param  allcheck  처음안내문구 여부 : 0 -선택없음, 1 - 선택있음
    @return String    SELECT BOX 구성 문자
    **/
    public String selectGrseqForCourse(String grcode, String gyear, String name, String selected, String change, int allcheck, String gubun) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String result = "";
        String              sql     = "";
        String code = "";
        String codenm = "";

        result = "  <SELECT name=" + name + " " + change + " > \n";

        if ( allcheck == 1) { 
          result += " <option value='' >==  ==  ==  == =선택 ==  ==  ==  == =</option > \n";
        }

        try { 
            connMgr = new DBConnectionManager();

            sql = "select grseq, grseqnm ";
            sql += " from tz_grseq       ";
//            if ( !gyear.equals("") ) { 
              sql += " where gyear = " + SQLString.Format(gyear);
//            } else { 
//              sql += " where gyear = (select max(gyear) from tz_grseq where grcode = " + SQLString.Format(grcode) + ")";
//            }
            sql += " and grcode = " + SQLString.Format(grcode);
            if("BL".equals(gubun)) { // B/L과정이냐
                sql += " and isblended = 'Y'";
            } else if("PF".equals(gubun)) { // 전문가 양성 과정이냐
                sql += " and isexpert = 'Y'";
            }
            sql += " order by grseq";

        // System.out.println("sql == > " + sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                code = ls.getString("grseq");
                codenm = ls.getString("grseqnm");

                result += " <option value='" + code + "'";
                if ( selected.equals(code)) { 
                    result += " selected ";
                }
                
                result += " > " + codenm + "</option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    } 
    
    
    /**
    독서교육 과정별 책 정보
    @param box          receive from the form object and session
    @return ArrayList   독서교육 과정별 책 정보
    */
    public ArrayList SubjBookList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        
        try { 
        	sql = "select a.subj, c.subjnm, a.month, a.bookcode, b.bookname  \n"
	        	+ "from   tz_subjbook a, tz_bookinfo b, tz_subj c            \n"
	        	+ "where  a.bookcode = b.bookcode                            \n"
	        	+ "and    a.subj = c.subj                                    \n"
	        	+ "and    c.isonoff = 'RC'                                   \n";
            
            if ( !ss_uclass.equals("ALL"))     sql += "and    c.upperclass = " +SQLString.Format(ss_uclass) + "  \n";
            if ( !ss_mclass.equals("ALL"))     sql += "and    c.middleclass = " +SQLString.Format(ss_mclass)+ "  \n";
            if ( !ss_lclass.equals("ALL"))     sql += "and    c.lowerclass = " +SQLString.Format(ss_lclass) + "  \n";
            if ( !ss_subjcourse.equals("ALL")) sql += "and    c.subj = " +SQLString.Format(ss_subjcourse)   + "  \n";
            
            sql += "order  by a.subj, a.month, a.bookcode					\n";

            connMgr = new DBConnectionManager();
            list = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
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
    배송정보 입력 리스트 (입과일괄처리에서 다운로드)
    @param box          receive from the form object and session
    @return ArrayList   배송정보 입력 리스트
    */
    public ArrayList selectDeliveryList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        try { 
            sql = "select b.userid, b.name, a.subj, a.subjnm, a.subjseq  	\n"
            	+ "from   vz_scsubjseq a, tz_member b, tz_propose c  		\n"
            	+ "where  a.subj = c.subj 									\n"
            	+ "and    a.year = c.year                                   \n"
            	+ "and    a.subjseq = c.subjseq                             \n"
            	+ "and    b.userid = c.userid                               \n"
            	+ "and    a.isonoff = 'RC'									\n";
            	
            if ( !ss_grcode.equals("ALL"))     
            	sql += " and a.grcode = " +SQLString.Format(ss_grcode);
            
            if ( !ss_grseq.equals("ALL"))      sql += "and    a.grseq = " +SQLString.Format(ss_grseq)		  + "  \n";
            if ( !ss_uclass.equals("ALL"))     sql += "and    a.scupperclass = " +SQLString.Format(ss_uclass) + "  \n";
            if ( !ss_mclass.equals("ALL"))     sql += "and    a.scmiddleclass = " +SQLString.Format(ss_mclass)+ "  \n";
            if ( !ss_lclass.equals("ALL"))     sql += "and    a.sclowerclass = " +SQLString.Format(ss_lclass) + "  \n";
            if ( !ss_subjcourse.equals("ALL")) sql += "and    a.scsubj = " +SQLString.Format(ss_subjcourse)   + "  \n";
            if ( !ss_subjseq.equals("ALL"))    sql += "and    a.scsubjseq = " +SQLString.Format(ss_subjseq)   + "  \n";
            sql += " order  by a.subj,a.subjseqgr							\n";


            connMgr = new DBConnectionManager();
            list = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
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
     * 폐강취소
     * 1. 자동화처리시 폐강된 과정에 한해 isdelete='Y', isvisible='N'으로 변경해준다.
     *    사용자화면에서는 isvisible이 'Y'인것만 보여주기때문에 별다른 수정이 없어도 된다.
     * 2. 기수개설 상세 목록화면에서 수정컬럼에 폐강된 과정기수는 '수정'대신에 '폐강취소'버튼으로 바뀐다.
     *    기수개설 상세 목록화면에서 isdelete 여부를 가져와서 분기시킨다.
     * 3. '폐강취소'버튼을 클릭하면 isdeleted='N', isvisible='Y'로 변경해준다.
     * 4. 강제 폐강된 사용자들이 있으면 해당 사용자들을 다시 복귀시켜준다.
     * @param box
     * @return
     * @throws Exception 
     */
	public int UpdateCancelDeleted(RequestBox box) throws Exception {
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt1      = null;
        PreparedStatement   pstmt2      = null;
        PreparedStatement   pstmt3      = null;
        String              sql1        = "";
        String              sql2        = "";
        String              sql3        = "";
        int                 isOk1       = 0;
        int                 isOk2       = 0;
        int                 isOk3       = 0;
        
        String v_user_id   = box.getSession("userid"   );
		String v_subj      = box.getString ("p_subj"   );  // 과정
        String v_year      = box.getString ("p_year"   );  // 년도
        String v_subjseq   = box.getString ("p_subjseq");  // 과정 기수
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // tz_cancle에서 사용자중에 강제폐강으로 취소된 사용자들이 있는지 검색 후 있으면 tz_propse로 복귀시켜준다.
            sql1= "\n insert into tz_propose "
            	+ "\n ( "
            	+ "\n        subj "
            	+ "\n      , year "
            	+ "\n      , subjseq "
            	+ "\n      , userid "
            	+ "\n      , comp "
            	+ "\n      , jik "
            	+ "\n      , appdate "
            	+ "\n      , chkfirst "
            	+ "\n      , chkfinal "
            	+ "\n      , luserid "
            	+ "\n      , ldate "
            	+ "\n ) "
            	+ "\n select subj "
            	+ "\n      , year "
            	+ "\n      , subjseq "
            	+ "\n      , userid "
            	+ "\n      , (select comp from tz_member where userid = a.userid) as comp "
            	+ "\n      , (select post from tz_member where userid = a.userid) as jik "
            	+ "\n      , to_char(sysdate,'yyyymmddhh24miss') as appdate "
            	+ "\n      , 'Y' as chkfirst "
            	+ "\n      , 'W' as chkfinal"
            	+ "\n      , '" + v_user_id+ "' as luserid "
            	+ "\n      , to_char(sysdate,'yyyymmddhh24miss') as ldate "
            	+ "\n from   tz_cancel a "
            	+ "\n where  subj = ? "
            	+ "\n and    year = ? "
            	+ "\n and    subjseq = ? "
            	+ "\n and    isdeleted = 'Y' ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_subj      );
            pstmt1.setString(2, v_year      );
            pstmt1.setString(3, v_subjseq   );
            
            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            
            if (isOk1 > 0) {
	            // tz_cancel에 있는 사용자들 삭제해준다.
	            sql2= "\n delete from tz_cancel "
	            	+ "\n where  subj = ? "
	            	+ "\n and    year = ? "
	            	+ "\n and    subjseq = ? "
	            	+ "\n and    isdeleted = 'Y' ";
	            pstmt2 = connMgr.prepareStatement(sql2);
	
	            pstmt2.setString(1, v_subj      );
	            pstmt2.setString(2, v_year      );
	            pstmt2.setString(3, v_subjseq   );
	            
	            isOk2 = pstmt2.executeUpdate();
	            if ( pstmt2 != null ) { pstmt2.close(); }
            }
            
            // 폐강취소
            sql3= "\n update tz_subjseq "
            	+ "\n set    isdeleted = 'N' "
            	+ "\n      , isvisible = 'Y' "
            	+ "\n where  subj = ? "
            	+ "\n and    year = ? "
            	+ "\n and    subjseq = ? ";
            
            pstmt3 = connMgr.prepareStatement(sql3);

            pstmt3.setString(1, v_subj      );
            pstmt3.setString(2, v_year      );
            pstmt3.setString(3, v_subjseq   );

            isOk3 = pstmt3.executeUpdate();
            if ( pstmt3 != null ) { pstmt3.close(); }
            if ( isOk3 > 0 ) {
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
            isOk3 = 0;
            ErrorManager.getErrorStackTrace(e, box, sql3);
            throw new Exception("\n SQL : [\n" + sql3 + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            isOk3 = 0;
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt1 != null ) { 
                try { 
                    pstmt1.close();  
                } catch ( Exception e ) { } 
            }
            if ( pstmt2 != null ) { 
                try { 
                    pstmt2.close();  
                } catch ( Exception e ) { } 
            }
            if ( pstmt3 != null ) { 
                try { 
                    pstmt3.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk3;
	} 

    
    ////////////////////// 최성원 추가 CB-HRD 과정기수와 교육기수 연결 ////////////////////////////////
    
    /**
	 * 과정기수 리스트
	 * @param box          receive from the form object and session
	 * @return ArrayList   과정기수 리스트
     **/
    public ArrayList selectSubjseqList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	String v_from_dt = StringManager.replace(box.getString("p_startdt"), "-", "");
    	String v_to_dt = StringManager.replace(box.getString("p_enddt"), "-", "");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.SUBJ  \n");
            sbSQL.append("     , A.YEAR  \n");
            sbSQL.append("     , A.SUBJSEQ  \n");
            sbSQL.append("     , A.SUBJNM  \n");
            sbSQL.append("     , B.ISONOFF  \n");
            sbSQL.append("     , B.SUBJCLASS  \n");
            sbSQL.append("     , GET_SUBJCLASS_FULLNM(B.SUBJCLASS) AS SUBJCLASSNM  \n");
            sbSQL.append("     , A.EDUSTART  \n");
            sbSQL.append("     , A.EDUEND  \n");
            sbSQL.append("FROM   TZ_SUBJSEQ A  \n");
            sbSQL.append("     , TZ_SUBJ B  \n");
            sbSQL.append("WHERE  A.SUBJ = B.SUBJ  \n");
            sbSQL.append("AND    GYEAR IS NULL  \n");
            sbSQL.append("AND   (EDUSTART BETWEEN " + SQLString.Format(v_from_dt) + " || '00' AND " + SQLString.Format(v_to_dt) + " || '23'  \n");
            sbSQL.append("       OR EDUEND BETWEEN " + SQLString.Format(v_from_dt) + " || '00' AND " + SQLString.Format(v_to_dt) + " || '23')  \n");
            sbSQL.append("ORDER BY A.SUBJ, A.EDUSTART, A.SUBJSEQ  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		System.out.println("SQL : " + sbSQL.toString());
    		ex.printStackTrace();
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 과정기수 매핑
	 * @param box receive from the form object and session
	 * @return int 등록결과
     **/
    public int updateSubjseqGrseq(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        StringBuffer sbSQL = null;
        StringBuffer sbSQL2 = null;
        int isOk = 0;
        
        String v_subj = "";
        String v_year = "";
        String v_subjseq = "";
        Vector v_chks = box.getVector("p_chk");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_SUBJSEQ A  \n");
            sbSQL.append("SET    GRCODE = ?  \n");
            sbSQL.append("     , GYEAR = ?  \n");
            sbSQL.append("     , GRSEQ = ?  \n");
            sbSQL.append("     , SUBJSEQGR = (SELECT TRIM(TO_CHAR(NVL(TO_NUMBER(MAX(SUBJSEQGR)), 0) + 1, '0000'))  \n");
            sbSQL.append("                    FROM   TZ_SUBJSEQ  \n");
            sbSQL.append("                    WHERE  SUBJ = A.SUBJ  \n");
            sbSQL.append("                    AND    YEAR = A.YEAR  \n");
            sbSQL.append("                    AND    GRCODE = ?)  \n");
            sbSQL.append("WHERE  SUBJ = ?  \n");
            sbSQL.append("AND    YEAR = ?  \n");
            sbSQL.append("AND    SUBJSEQ = ?  \n");
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            sbSQL2 = new StringBuffer();
            sbSQL2.append("INSERT INTO TZ_GRSUBJ  \n");
            sbSQL2.append("      (GRCODE  \n");
            sbSQL2.append("     , SUBJCOURSE  \n");
            sbSQL2.append("     , ISNEW  \n");
            sbSQL2.append("     , DISSEQ  \n");
            sbSQL2.append("     , LUSERID  \n");
            sbSQL2.append("     , LDATE)  \n");
            sbSQL2.append("VALUES(?  \n");
            sbSQL2.append("     , ?  \n");
            sbSQL2.append("     , 'Y'  \n");
            sbSQL2.append("     , 0  \n");
            sbSQL2.append("     , ?  \n");
            sbSQL2.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))  \n");
            pstmt2 = connMgr.prepareStatement(sbSQL2.toString());
            
            for(int i = 0 ; i < v_chks.size(); i++) {
            	String[] v_chk = v_chks.elementAt(i).toString().split("&");
            	v_subj = v_chk[0];
            	v_year = v_chk[1];
            	v_subjseq = v_chk[2];
            	
	            int index = 1;
	            pstmt.setString(index++, box.getString("p_grcode"));
	            pstmt.setString(index++, box.getString("p_gyear"));
	            pstmt.setString(index++, box.getString("p_grseq"));
	            pstmt.setString(index++, box.getString("p_grcode"));
	            pstmt.setString(index++, v_subj);
	            pstmt.setString(index++, v_year);
	            pstmt.setString(index++, v_subjseq);
	            isOk += pstmt.executeUpdate();
	            index = 1;
	            pstmt2.setString(index++, box.getString("p_grcode"));
	            pstmt2.setString(index++, v_subj);
	            pstmt2.setString(index++, box.getSession("userid"));
	            try {
	            	pstmt2.executeUpdate();
	            } catch(Exception e) {
	            }
            }
            if ( pstmt2 != null ) { pstmt2.close(); }
            if ( pstmt != null ) { pstmt.close(); }

            if(isOk > 0) {
                connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(pstmt2 != null) { try { pstmt2.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
 
    /**
     * 교육기수 인원별 상세조회
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectDivStudent(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
		
		String v_grcode = box.getString("s_grcode");
		String v_year = box.getString("s_year");
		String v_subj = box.getString("s_subj");
		String v_subjseq = box.getString("s_subjseq");
		//String v_grseq = box.getString("s_grseq");
		//String v_company = box.getString("s_company");
		//String v_isonoff = box.getString("s_isonoff");
		
		String v_gubun = box.getString("s_gubun");
		
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
            
    		if(v_gubun.equals("propose")){ //신청
	    		sbSQL.append(" select get_codenm('0004', b.isonoff) isonoffnm, b.subj, b.subjnm, b.subjseq, userid, \n ") ;
	    		sbSQL.append("        get_name(userid) name, biyong, goyongpricemajor, goyongpriceminor, get_compnm(comp) company \n ") ;
	    		sbSQL.append("   from tz_propose a, vz_scsubjseq b  \n ") ;
	    		sbSQL.append("  where  a.subj = b.subj  \n ") ;
	    		sbSQL.append("    and  a.year = b.year  \n ") ;
	    		sbSQL.append("    and  a.subjseq = b.subjseq  \n ") ;
	    		sbSQL.append("    and  a.subjseq = " + SQLString.Format(v_subjseq) + " \n ");
	            sbSQL.append("    and  a.year = " + SQLString.Format(v_year) + " \n ");
	            sbSQL.append("    and  a.subj = " + SQLString.Format(v_subj) + " \n ");
	            sbSQL.append("    and  b.grcode = " + SQLString.Format(v_grcode) + " \n ");
	            
    		}else if(v_gubun.equals("cancel")){ //취소
	    		sbSQL.append(" select get_codenm('0004', b.isonoff) isonoffnm, b.subj, b.subjnm, b.subjseq, userid, reason, decode(a.cancelkind, 'P', '반려','취소') as cancelkind, \n ") ;
	    		sbSQL.append("        get_name(userid) name, biyong, goyongpricemajor, goyongpriceminor, get_compnm((select comp from tz_member where userid=a.userid)) company \n ") ;
	    		sbSQL.append("   from tz_cancel a, vz_scsubjseq b  \n ") ;
	    		sbSQL.append("  where  a.subj = b.subj  \n ") ;
	    		sbSQL.append("    and  a.year = b.year  \n ") ;
	    		sbSQL.append("    and  a.subjseq = b.subjseq  \n ") ;
	    		sbSQL.append("    and  a.subjseq = " + SQLString.Format(v_subjseq) + " \n ");
	            sbSQL.append("    and  a.year = " + SQLString.Format(v_year) + " \n ");
	            sbSQL.append("    and  a.subj = " + SQLString.Format(v_subj) + " \n ");
	            sbSQL.append("    and  b.grcode = " + SQLString.Format(v_grcode) + " \n ");
	            
    		}else if(v_gubun.equals("student")){ //승인
	    		sbSQL.append(" select get_codenm('0004', b.isonoff) isonoffnm, b.subj, b.subjnm, b.subjseq, userid, \n ") ;
	    		sbSQL.append("        get_name(userid) name, biyong, goyongpricemajor, goyongpriceminor, get_compnm(comp) company \n ") ;
	    		sbSQL.append("   from tz_student a, vz_scsubjseq b  \n ") ;
	    		sbSQL.append("  where  a.subj = b.subj  \n ") ;
	    		sbSQL.append("    and  a.year = b.year  \n ") ;
	    		sbSQL.append("    and  a.subjseq = b.subjseq  \n ") ;
	    		sbSQL.append("    and  a.subjseq = " + SQLString.Format(v_subjseq) + " \n ");
	            sbSQL.append("    and  a.year = " + SQLString.Format(v_year) + " \n ");
	            sbSQL.append("    and  a.subj = " + SQLString.Format(v_subj) + " \n ");
	            sbSQL.append("    and  b.grcode = " + SQLString.Format(v_grcode) + " \n ");
    			
    		}else if(v_gubun.equals("stold")){ //수료
	    		sbSQL.append(" select get_codenm('0004', b.isonoff) isonoffnm, b.subj, b.subjnm, b.subjseq, userid, \n ") ;
	    		sbSQL.append("        get_name(userid) name, biyong, goyongpricemajor, goyongpriceminor, get_compnm(comp) company \n ") ;
	    		sbSQL.append("   from tz_stold a, vz_scsubjseq b  \n ") ;
	    		sbSQL.append("  where  a.subj = b.subj  \n ") ;
	    		sbSQL.append("    and  a.year = b.year  \n ") ;
	    		sbSQL.append("    and  a.subjseq = b.subjseq  \n ") ;
	    		sbSQL.append("    and  a.subjseq = " + SQLString.Format(v_subjseq) + " \n ");
	            sbSQL.append("    and  a.year = " + SQLString.Format(v_year) + " \n ");
	            sbSQL.append("    and  a.subj = " + SQLString.Format(v_subj) + " \n ");
	            sbSQL.append("    and  b.grcode = " + SQLString.Format(v_grcode) + " \n ");
    			
    		}
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		System.out.println("SQL : " + sbSQL.toString());
    		ex.printStackTrace();
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }    
    
    /**
     * 해당교육기수에 신청자가 있는지 확인
     * @param box
     * @return
     * @throws Exception
     */
    public int selectCntProposeStu(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	String sql = "";
		
        String v_grcode = box.getString ("p_grcode" );
        String v_gyear  = box.getString ("p_gyear"  );
        String v_grseq  = box.getString ("p_grseq"  );
		
		int cnt = 0;
		
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
            
            sql = "\n select count(*) cnt "
           	    + "\n from   tz_subjseq a, tz_propose b "
           	    + "\n where  1=1 "
           	    + "\n and    a.subj    =b.subj "
           	    + "\n and    a.subjseq =b.subjseq "
           	    + "\n and    a.year    =b.year "
                + "\n and    grcode = " +SQLString.Format(v_grcode)
                + "\n and    gyear  = " +SQLString.Format(v_gyear)
                + "\n and    grseq  = " +SQLString.Format(v_grseq);
    		
    		ls = connMgr.executeQuery(sql);
    		
    		while(ls.next()) {
    			cnt = ls.getInt("cnt");
    		}
    	} catch(Exception ex) {
    		System.out.println("SQL : " + sbSQL.toString());
    		ex.printStackTrace();
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return cnt;
    }    
    
    
    
    
    /**
    신규기수가 기존기수와 동일한지 조회
    @param box          receive from the form object and session
    @return SubjseqData
    **/
    public int checkSubjseq(String year, String subj, String newsubjseq) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        String 				sql			= ""; 
        int                 ret     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            connMgr     = new DBConnectionManager();
            sql = "SELECT COUNT(1) AS CNT FROM TZ_SUBJSEQ WHERE YEAR = '"+year+"' AND SUBJ = '"+subj+"' AND SUBJSEQ = LPAD('"+newsubjseq+"',4,'0')";	
System.out.println("check = "+sql);
	        ls      = connMgr.executeQuery(sql);
	       
	       if ( ls.next() ) { 
	           ret = ls.getInt("cnt");
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, null, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
              
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return ret;
    }
    
}