// **********************************************************
//  1. 제      목: 교육그룹OPERATION BEAN
//  2. 파 일 명 : EduGroupBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.3
//  5. 버      젼: 0.1
//  6. 작      성: LeeSuMin 2003. 07. 16
//  7. 수      정:
// **********************************************************
package com.ziaan.course;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class EduGroupBean { 
    
    public EduGroupBean() { }

    /**
    교육그룹리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   교육그룹리스트
    */      
    public ArrayList SelectEduGroupList(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet ls = null,ls2= null;
        ArrayList list1 = null,grcomps= null;
        String sql  = "";
        DataBox      dbox       = null;
        GrcompBean   grcompBean = null;

        // String v_EduGroupnm = box.getString("p_grtype");            

        try { 
        	sql = "\n select a.grcode	"
        	    + "\n 	   , a.grcodenm	"
        	    + "\n      , a.idtype	"
        	    + "\n      , a.manager	"
        	    + "\n      , a.repdate	"
        	    + "\n      , a.domain	"
        	    + "\n      , a.chkFirst "
        	    + "\n      , a.chkFinal "
        	    + "\n      , a.islogin "
        	    + "\n      , a.isjik "
        	    + "\n      , a.isonlygate "
        	    + "\n      , a.isusebill "
        	    + "\n      , a.master "
        	    + "\n      , a.indate "
        	    + "\n      , a.luserid "
        	    + "\n      , a.ldate "
        	    + "\n      , a.propcnt "
        	    + "\n      , a.comp "
        	    + "\n      , b.name "
        	    + "\n      , b.email "
        	    + "\n      , c.telno as comptel "
        	    + "\n from   tz_grcode a "
        	    + "\n      , tz_member b "
        	    + "\n      , tz_compclass c "
        	    + "\n where  a.master = b.userid(+) "
        	    + "\n and    b.comp = c.comp(+) "
        	    + "\n order  by grcodenm ";
			
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            grcomps = new ArrayList();
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                // data = new EduGroupData();
                dbox = ls.getDataBox(); 
                
                dbox.put("grcode"	,ls.getString("grcode") );
                dbox.put("grcodenm"	,ls.getString("grcodenm") );
                dbox.put("comp"		,ls.getString("comp") );
                dbox.put("master"	,ls.getString("master") );
                dbox.put("manager"	,ls.getString("manager") );
            	dbox.put("mastername"	,ls.getString("name") );
            	dbox.put("masteremail"	,ls.getString("email") );
            	dbox.put("mastercomptel",ls.getString("comptel") );
            	                
                grcompBean = new GrcompBean();
                grcomps = (ArrayList)grcompBean.SelectGrcompList(box,ls.getString("grcode") );
                /* Config에 따라 compTxt 구성로직 추가--추후 */
                dbox.put("comptxt",grcompBean.getCompTxt(grcomps,ls.getString("grcode")));
                dbox.put("grcomps",grcomps);
                
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
  

    /**
    교육그룹데이타 조회
    @param box          receive from the form object and session
    @return EduGroupData   
    **/    
    public EduGroupData SelectEduGroupData(RequestBox box) throws Exception {               
        String v_grcode = box.getString("p_grcode");            
        EduGroupData data = null;
        
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
    
        try { 
            sql = "\n select a.grcode "
            	+ "\n       ,a.grcodenm "
            	+ "\n       ,a.idtype "
            	+ "\n       ,a.manager "
            	+ "\n       ,get_name(a.manager) as managernm "
            	+ "\n       ,a.repdate "
            	+ "\n       ,a.domain "
            	+ "\n       ,a.chkFirst "
            	+ "\n       ,a.chkFinal "
            	+ "\n       ,a.islogin "
            	+ "\n       ,a.isjik "
            	+ "\n       ,a.isonlygate "
            	+ "\n       ,a.isusebill "
            	+ "\n       ,a.master "
            	+ "\n       ,b.name as masternm "
            	+ "\n       ,b.email as masteremail "
            	+ "\n       ,c.telno as mastercomptel "
            	+ "\n       ,a.indate "
            	+ "\n       ,a.luserid "
            	+ "\n       ,a.ldate "
            	+ "\n       ,a.propcnt "
            	+ "\n       ,a.etcdata "
            	+ "\n       ,a.comp "
            	+ "\n from   tz_grcode a, tz_member b, tz_compclass c "
            	+ "\n where  a.master = b.userid(+) "
            	+ "\n and    b.comp = c.comp(+) "
            	+ "\n and    a.grcode = '" +v_grcode + "'";
        
            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql);
                
            while ( ls.next() ) { 
            	data = new EduGroupData();
                // dbox = ls.getDataBox(); 
              
                data.setGrcode( ls.getString("grcode") );             
                data.setGrcodenm( ls.getString("grcodenm") );             
                data.setComp( ls.getString("comp") );             
                data.setIdtype( ls.getString("idtype") );             
                data.setMaster( ls.getString("master") );
                data.setMasterName( ls.getString("masternm") );
                data.setMasterEmail( ls.getString("masteremail") );
                data.setMasterComptel( ls.getString("mastercomptel") );
                data.setManager( ls.getString("manager") );             
                data.setManagerName( ls.getString("managernm") );             
                data.setRepdate( ls.getString("repdate") );             
                data.setDomain( ls.getString("domain") );             
                data.setChkFirst( ls.getString("chkfirst") );             
                data.setChkFinal( ls.getString("chkfinal") );
                data.setIslogin( ls.getString("islogin") );
                data.setIsjik( ls.getString("isjik") );             
                data.setIsonlygate( ls.getString("isonlygate") );             
                data.setIsusebill( ls.getString("isusebill") );             
                data.setIndate( ls.getString("indate") );             
                data.setLuserid( ls.getString("luserid") );             
                data.setLdate( ls.getString("ldate") );
                data.setEtcdata( ls.getString("etcdata") );
                data.setPropcnt( ls.getInt("propcnt") );
                break;
            }
        
            // get 연결회사정보
            sql = "\n select a.comp comp, b.compnm compnm  "
            	+ "\n from   tz_grcomp a, tz_compclass b "
                + "\n where  a.comp = b.comp "
                + "\n and    a.grcode = " +StringManager.makeSQL(v_grcode);
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                data.makeSub( ls.getString("comp"),ls.getString("compnm") );
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
    새로운 교육그룹코드 등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int InsertEduGroup(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;   
        ListSet             ls      = null;     
        String              sql     = "";
        int isOk = 0;   
        
        String v_grcode     = "";    
        String v_luserid    = "session"; // 세션변수에서 사용자 id를 가져온다.
        
        try { 
            connMgr = new DBConnectionManager();
            sql = "\n select 'N'||nvl(ltrim(rtrim(to_char(to_number(max(substr(grcode,2,6))) +1,'000000'))),'000001') GRS "
            	+ "\n from   tz_grcode ";
            ls = connMgr.executeQuery(sql);
                
            if ( ls.next() ) { 
                v_grcode = ls.getString("GRS");
            } else { 
                v_grcode = "N000001";
            }
                                                     
            connMgr.setAutoCommit(false);  

            // insert TZ_EduGroup table
            sql =  "insert into TZ_GRCODE("
                +  "grcode, grcodenm, idtype, manager, repdate," 
                +  "domain, chkFirst, chkFinal, islogin, isjik,"
                +  "isonlygate, isusebill, master, indate, luserid,"
                +  "ldate, propcnt, etcdata "
                +  ") values (?,?,?,?,?, ?,?,?,?,?, ?,?,?,to_char(sysdate,'YYYYMMDD'),?,  to_char(sysdate,'YYYYMMDDHH24MISS'), ?, ? )";
            
            pstmt = connMgr.prepareStatement(sql);
            
            pstmt.setString(1,  v_grcode);   
            pstmt.setString(2,  box.getString("p_grcodenm") );
            pstmt.setString(3,  box.getStringDefault("p_idtype","ID") );
            pstmt.setString(4,  box.getString("p_manager") );   
            pstmt.setString(5,  box.getString("p_repdate") );   
            pstmt.setString(6,  box.getString("p_domain") );    
            pstmt.setString(7,  box.getString("p_chkfirst") );  
            pstmt.setString(8,  box.getString("p_chkfinal") );  
            pstmt.setString(9,  box.getStringDefault("p_islogin","N") );   
            pstmt.setString(10, box.getStringDefault("p_isjik","N") );     
            pstmt.setString(11, box.getStringDefault("p_isonlygate","N") );
            pstmt.setString(12, box.getStringDefault("p_isusebill","N") ); 
            pstmt.setString(13, box.getString("p_master") );
            pstmt.setString(14, v_luserid);    
            pstmt.setInt   (15, box.getInt("p_propcnt") );   
            pstmt.setString(16, box.getString("p_etcdata") );   
            //pstmt.setString(17, box.getString("p_compTxt") );   
   
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            if ( isOk == 1) 
                isOk = assignComp(connMgr,box,v_grcode);        // 회사지정정보저장
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }   
    /**
    회사연결
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int assignComp(DBConnectionManager connMgr, RequestBox box, String p_grcode) throws Exception { 

        PreparedStatement   pstmt   = null;   
        String              sql     = "";
        int isOk = 0;   
        
        String v_grcode     = p_grcode;    
        String v_luserid    = box.getSession("userid"); // 세션변수에서 사용자 id를 가져온다.
        
        try { 

            String v_codes    = box.getString("p_compTxt");
            StringTokenizer v_token = new StringTokenizer(v_codes, ";");
            String v_comp = "";
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
                        
            // TZ_GRcomp initialize 
            sql = " delete from tz_grcomp where grcode=?";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_grcode);
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            sql = " insert into tz_grcomp"
                + " (grcode, comp, indate, luserid, ldate) "
                + " values (?,?,to_char(sysdate,'YYYYMMDD'),?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
                             
            while ( v_token.hasMoreTokens() ) { 
                v_comp = v_token.nextToken();
                // insert TZ_GRCOMP table
                pstmt = connMgr.prepareStatement(sql); 
                pstmt.setString(1, v_grcode);
                pstmt.setString(2, v_comp);
                pstmt.setString(3, v_luserid);
                isOk = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }    

    /**
    선택된 교육그룹코드 수정
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    **/
     public int UpdateEduGroup(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;        
        String              sql     = "";
        int isOk = 0;   
        
        String v_grcode = box.getString("p_grcode");    
        String v_luserid      = "session"; // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr = new DBConnectionManager();                             
            connMgr.setAutoCommit(false);  
                        
            // insert TZ_EduGroup table
            sql = "update tz_grcode set "
                + " grcodenm   =?,  idtype     =?,  manager    =?, "
                + " master     =?,  repdate    =?,  propcnt    =?, "
                + " domain     =?,  chkfirst   =?,  chkfinal   =?, "
                + " islogin    =?,  isjik      =?,  isonlygate =?, "
                + " isusebill  =?,  luserid    =?,  ldate      =to_char(sysdate,'YYYYMMDDHH24MISS'), "
                + " etcdata    =?	" 
                + " where grcode = ? ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, box.getString("p_grcodenm") );
            pstmt.setString(2, box.getStringDefault("p_idtype","ID") );    
            pstmt.setString(3, box.getString("p_manager") );   
            pstmt.setString(4, box.getString("p_master") );    
            pstmt.setString(5, box.getString("p_repdate") );   
            pstmt.setInt   (6, box.getInt("p_propcnt") );   
            pstmt.setString(7, box.getString("p_domain") );    
            pstmt.setString(8, box.getStringDefault("p_chkfirst","N") );  
            pstmt.setString(9, box.getStringDefault("p_chkfinal","Y") );  
            pstmt.setString(10,box.getStringDefault("p_islogin","N") );   
            pstmt.setString(11,box.getStringDefault("p_isjik","N") );     
            pstmt.setString(12,box.getStringDefault("p_isonlygate","N") );
            pstmt.setString(13,box.getStringDefault("p_isusebill","N") ); 
            pstmt.setString(14,v_luserid);    
            pstmt.setString(15,box.getString("p_etcdata") ); 
            pstmt.setString(16,v_grcode);  

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            if ( isOk == 1) 
                isOk = assignComp(connMgr,box,v_grcode);        // 회사지정정보저장
            
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }   
    /**
    코스리스트 
    @param box          receive from the form object and session
    @return ArrayList   코스 리스트
    */      
    public ArrayList TargetCourseList(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list1 = null;
        String sql  = "";
        SubjectInfoData data = null;

        try { 
            sql = "select course, coursenm, inuserid, indate, gradscore, gradfailcnt, luserid, ldate "
                + "  from tz_course order by coursenm"; 

            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                data = new SubjectInfoData();    
                data.setSubj( ls.getString("course") );
                data.setSubjnm( ls.getString("coursenm") );
                data.setIsonoffnm( "패키지" );                
                list1.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    /**
    집합,사이버/대분류별 과목리스트 
    @param box          receive from the form object and session
    @return ArrayList   과목 리스트
    */      
    public ArrayList TargetSubjectList(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list1 = null;
        String sql  = "";
        SubjectInfoData data = null;

        String v_gubun      = box.getString("p_gubun");            
        String v_upperclass = box.getString("p_upperclass");      

        if ( v_gubun.equals("")) v_gubun = "ALL";
        if ( v_upperclass.equals("")) v_upperclass = "ALL";

        try { 
            sql = "select a.subj, a.subjnm, a.isonoff, get_codenm('0004',a.isonoff) isonoffnm, b.upperclass, b.classname "; 
            sql += "  from tz_subj a, tz_subjatt  b                               ";
//            sql += " where a.subjclass = b.subjclass ";
            sql += " where a.upperclass  = b.upperclass ";
            sql += "   and b.middleclass = '000' ";
            sql += "   and b.lowerclass  = '000' ";

            if ( !v_gubun.equals("ALL"))
                sql += " and a.isonoff = " + SQLString.Format(v_gubun);
            
            if ( !v_upperclass.equals("ALL"))
                sql += " and a.upperclass = " + SQLString.Format(v_upperclass);

            sql += " order by a.isonoff desc, b.upperclass, a.subjnm ";

            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                data = new SubjectInfoData();    
                               
                data.setSubj( ls.getString("subj") );
                data.setSubjnm( ls.getString("subjnm") );
                data.setIsonoff( ls.getString("isonoff") );
                data.setIsonoffnm(ls.getString("isonoffnm"));
                data.setUpperclass( ls.getString("upperclass") );
                data.setClassname( ls.getString("classname") );
            
                list1.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    선택한 과목/코스 리스트
    @param box          receive from the form object and session
    @return ArrayList   선택한 과목/코스 리스트
    */      
    public ArrayList SelectedList(RequestBox box) throws Exception {               
        ArrayList list1 = null;
        list1 = new ArrayList();
        SubjectInfoData data = null;
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
            
        String v_subjectcodes    = box.getString("p_selectedcodes");
        String v_subjecttexts    = box.getString("p_selectedtexts");
        String v_grcode   = box.getString("p_grcode");
    
        try { 
            if ( !v_subjectcodes.equals("") ) { 
                try { 
                    StringTokenizer v_tokencode = new StringTokenizer(v_subjectcodes, ";");
                    StringTokenizer v_tokentext = new StringTokenizer(v_subjecttexts, ";");
                                
                    String v_code = "";
                    String v_text = "";
                    
                    while ( v_tokencode.hasMoreTokens() && v_tokentext.hasMoreTokens() ) { 
                        v_code = v_tokencode.nextToken();
                        v_text = v_tokentext.nextToken();
                        
                        data = new SubjectInfoData();    
                                           
                        data.setSubj(v_code);
                        data.setDisplayname(v_text);
                        
                        list1.add(data);
                    }
                }
                catch ( Exception ex ) { 
                    ErrorManager.getErrorStackTrace(ex, box, "");
                    throw new Exception(ex.getMessage() );
                }
                finally { 
        
                }
            } else { 
                if ( !v_grcode.equals("") ) { 
                    sql = "select a.course, a.coursenm, a.inuserid, a.indate, a.gradscore, a.gradfailcnt, a.luserid, a.ldate "
                    	+ "       , (								\n"
 					    + "				select case when count(*) > 0 then 'N' else 'Y' end		\n"
 					    + "				from vz_scsubjseq										\n"
 					    + "				where grcode = b.grcode									\n"
 					    + "				and scsubj = a.course									\n"
 					    + "		  ) delyn"                    	
                        + "  from tz_course        a, "
                        + "       tz_grsubj      b"
                        + " where a.course=b.subjcourse and b.grcode=" +SQLString.Format(v_grcode)
                        + " order by course";
    
                    connMgr = new DBConnectionManager();
                    list1 = new ArrayList();
                    ls = connMgr.executeQuery(sql);
                
                    while ( ls.next() ) { 
                        data = new SubjectInfoData();    
                                   
                        data.setSubj( ls.getString("course") );
                        data.setSubjnm( ls.getString("coursenm") );
                        data.setIsonoffnm("패키지");
                        data.setDelyn( ls.getString("delyn"));                        
                        list1.add(data);
                    }
                    
                    sql = "select a.subj, a.subjnm, a.isonoff, get_codenm('0004',a.isonoff) isonoffnm, a.upperclass, "
					    + "       (select classname from tz_subjatt where upperclass = a.upperclass and middleclass = '000' and lowerclass  = '000') classname "
					    + "       , (								\n"
					    + "				select case when count(*) > 0 then 'N' else 'Y' end		\n"
					    + "				from vz_scsubjseq										\n"
					    + "				where grcode = b.grcode									\n"
					    + "				and subj = a.subj										\n"
					    + "		  ) delyn"					    
                        + "  from tz_subj        a, "
                        + "       tz_grsubj      b"
                        + " where a.subj=b.subjcourse and b.grcode=" +SQLString.Format(v_grcode)
                        + " order by a.isonoff desc, a.upperclass, a.subjnm, disseq";
                    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                    ls = connMgr.executeQuery(sql);
                
                    while ( ls.next() ) { 
                        data = new SubjectInfoData();    
                                   
                        data.setSubj( ls.getString("subj") );
                        data.setSubjnm( ls.getString("subjnm") );
                        data.setIsonoff( ls.getString("isonoff") );
                        data.setIsonoffnm(ls.getString("isonoffnm"));
                        data.setUpperclass( ls.getString("upperclass") );
                        data.setClassname( ls.getString("classname") );
                        data.setDelyn( ls.getString("delyn"));
                        list1.add(data);
                    }
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list1;
    }
    
    /**
    과목/코스 지정정보 저장
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    **/
     public int SaveAssign(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;
        PreparedStatement   pstmt1  = null;        
        ListSet             ls      = null; 
        String              sql     = "";
        int isOk = 0;   
        
        String v_grcode 	= box.getString("p_grcode");    
        String v_luserid    = box.getSession("userid"); // 세션변수에서 사용자 id를 가져온다.

        try { 

            String v_codes    = box.getString("p_selectedcodes");
            StringTokenizer v_token = new StringTokenizer(v_codes, ";");
            String v_code = "";
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
                        
            // TZ_GRSUBJ initialize (tz_subjseq에 존재하는 경우 삭제불가) 
            sql = " delete from tz_grsubj where grcode=?"
                + "    and (subjcourse not in (select distinct subj from tz_subjseq where grcode=?))"
                + "    and (subjcourse not in (select distinct course from tz_courseseq where grcode=?))";
            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_grcode);
            pstmt1.setString(2, v_grcode);
            pstmt1.setString(3, v_grcode);
            isOk = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            while ( v_token.hasMoreTokens() ) { 
                v_code = v_token.nextToken();
                // check exists in TZ_GRSUBJ
                sql = " select decode(count(*),0,'N','Y') isExist from tz_grsubj " 
                    + "where grcode='" +v_grcode + "' and subjcourse=rtrim('" +v_code + "')";
                
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                
                ls = connMgr.executeQuery(sql);
                
                if (ls.next()) {
                
	                if ( ls.getString("isExist").equals("N") ) { 
	                    // insert TZ_GRSUBJ table
	                    sql = " insert into tz_grsubj"
	                        + " (grcode, subjcourse, isnew, disseq, grpcode, grpname, luserid, ldate) "
	                        + " values (?,?,'N',0,'','',?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
	
	                    pstmt = connMgr.prepareStatement(sql);
	                    pstmt.setString(1, v_grcode);
	                    pstmt.setString(2, v_code);
	                    pstmt.setString(3, v_luserid);
	                    isOk = pstmt.executeUpdate();
	                    
	                    if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
	                }
                }
            }
        
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }       

    /**
    과목 지정정보 저장
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    **/
     public int SaveAssign2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;        
        ListSet             ls      = null; 
        String              sql     = "";
        int isOk = 0;   

        String v_grcode = box.getString("p_grcode");    
        String v_luserid			= box.getSession("userid");
        String v_code = "";
        Vector v_checks            = box.getVector("p_checks");
        Enumeration em             = v_checks.elements();

        try { 

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // TZ_GRSUBJ initialize (tz_subjseq에 존재하는 경우 삭제불가) 
            sql = " delete from tz_grsubj where grcode=?"
                + "    and (subjcourse not in (select distinct subj from tz_subjseq where grcode=?))"
                + "    and (subjcourse not in (select distinct course from tz_courseseq where grcode=?))";
            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_grcode);
            pstmt1.setString(2, v_grcode);
            pstmt1.setString(3, v_grcode);
            isOk = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            while ( em.hasMoreElements() ) { 
                // v_code = v_token.nextToken();
                v_code   = (String)em.nextElement();
                // check exists in TZ_GRSUBJ
                sql = " select decode(count(*),0,'N','Y') isExist from tz_grsubj " 
                    + "where grcode='" +v_grcode + "' and subjcourse=rtrim('" +v_code + "')";
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                ls = connMgr.executeQuery(sql);
                ls.next();

                if ( ls.getString("isExist").equals("N") ) { 
                    ls.close();
                    // insert TZ_GRSUBJ table
                    sql = " insert into tz_grsubj"
                        + " (grcode, subjcourse, isnew, disseq, grpcode, grpname, luserid, ldate) "
                        + " values (?,?,'N',0,'','',?,to_char(sysdate,'YYYYMMDDHH24MISS'))";

                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_grcode);
                    pstmt.setString(2, v_code);
                    pstmt.setString(3, v_luserid);
                    isOk = pstmt.executeUpdate();
                    pstmt.close();
                }

            }

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }     
     
     /**
      * 과목 지정정보 저장
      */
     public int SaveAssign3(RequestBox box) throws Exception { 
    	 DBConnectionManager	connMgr	= null;        
    	 
    	 PreparedStatement   pstmt   = null;
    	 PreparedStatement pstmt1 = null;        
    	 ListSet             ls      = null; 
    	 String              sql     = "";
    	 int isOk = 0;   
    	 
    	 String v_grcode = box.getString("p_grcode");    
    	 String v_luserid = box.getSession("userid");
    	 String v_code = "";
    	 
    	 
    	 Vector v_checks = box.getVector("p_checks");
    	 Enumeration em = v_checks.elements();
    	 
    	 Vector v_pageSubj = box.getVector("p_pageSubj");
    	 Enumeration em2 = v_pageSubj.elements();
    	 String v_em2 = "(";
    	 
    	 while (em2.hasMoreElements()) { 
    		 v_em2 += "'"+(String)em2.nextElement()+"',"; 
    	 }
    	 v_em2 = v_em2.substring(0,v_em2.length()-1);
    	 v_em2 += ")";
    	 
    	 try { 
    		 
    		 connMgr = new DBConnectionManager();
    		 connMgr.setAutoCommit(false);
    		 
    		 // TZ_GRSUBJ initialize (tz_subjseq에 존재하는 경우 삭제불가) 
    		 sql = " delete from tz_grsubj where grcode=? and subjcourse in "+v_em2+" "
    			 + "    and (subjcourse not in (select distinct subj from tz_subjseq where grcode=?))"
    			 + "    and (subjcourse not in (select distinct course from tz_courseseq where grcode=?))";
    		 
    		 pstmt1 = connMgr.prepareStatement(sql);
    		 pstmt1.setString(1, v_grcode);
    		 pstmt1.setString(2, v_grcode);
    		 pstmt1.setString(3, v_grcode);
    		 isOk = pstmt1.executeUpdate();
    		 if ( pstmt1 != null ) { pstmt1.close(); }
    		 
    		 while ( em.hasMoreElements() ) { 
    			 // v_code = v_token.nextToken();
    			 v_code   = (String)em.nextElement();
    			 // check exists in TZ_GRSUBJ
    			 sql = " select decode(count(*),0,'N','Y') isExist from tz_grsubj " 
    				 + "where grcode='" +v_grcode + "' and subjcourse=rtrim('" +v_code + "')";
    			 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    			 ls = connMgr.executeQuery(sql);
    			 ls.next();
    			 
    			 if ( ls.getString("isExist").equals("N") ) { 
    				 ls.close();
    				 // insert TZ_GRSUBJ table
    				 sql = " insert into tz_grsubj"
    					 + " (grcode, subjcourse, isnew, disseq, grpcode, grpname, luserid, ldate) "
    					 + " values (?,?,'N',0,'','',?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
    				 
    				 pstmt = connMgr.prepareStatement(sql);
    				 pstmt.setString(1, v_grcode);
    				 pstmt.setString(2, v_code);
    				 pstmt.setString(3, v_luserid);
    				 isOk = pstmt.executeUpdate();
    				 pstmt.close();
    			 }
    			 
    		 }
    		 
    	 } catch ( Exception ex ) { 
    		 connMgr.rollback();
    		 ErrorManager.getErrorStackTrace(ex, box, sql);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	 } finally { 
    		 if ( isOk > 0 ) { connMgr.commit(); }
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		 if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
    		 if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
    		 if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
    	 
    	 return isOk;
     } 
     
    /**
    과목 지정정보 저장
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    **/
     public int SaveRecom(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 	= null;        
        ListSet             ls      = null; 
        String              sql     = "";
        int isOk = 0;   

        String v_grcode 	= box.getString("p_grcode");    
        String v_luserid	= box.getSession("userid");
        String v_code 		= "";
        Vector v_checks     = box.getVector("p_checks");
        Enumeration em      = v_checks.elements();

        try { 

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // TZ_GRSUBJ initialize (tz_subjseq에 존재하는 경우 삭제불가) 
            sql = " delete from tz_grrecom where grcode=?";
            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_grcode);
            isOk = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            while ( em.hasMoreElements() ) { 
                // v_code = v_token.nextToken();
                v_code   = (String)em.nextElement();
                
                // check exists in TZ_GRSUBJ
                // sql = " select decode(count(*),0,'N','Y') isExist from tz_grrecom " 
                //    + "where grcode='" +v_grcode + "' and subjcourse=rtrim('" +v_code + "')";
                // if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                // ls = connMgr.executeQuery(sql);
                // ls.next();
                // 
                // if ( ls.getString("isExist").equals("N") ) { 
                    // insert TZ_GRSUBJ table
                    sql = " insert into tz_grrecom"
                        + " (grcode, subjcourse, isnew, disseq, grpcode, grpname, luserid, ldate) "
                        + " values (?,?,'N',0,'','',?,to_char(sysdate,'YYYYMMDDHH24MISS'))";

                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_grcode);
                    pstmt.setString(2, v_code);
                    pstmt.setString(3, v_luserid);
                    isOk = pstmt.executeUpdate();
                    if ( pstmt != null ) { pstmt.close(); }
                // }

            }

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }       

     
    /**
    과목리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   과목리스트
    */
    public ArrayList SelectSubjList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

		String  v_grcode		= box.getString("p_grcode");										 // 교육그룹
		String  ss_subjgubun     = box.getStringDefault("s_subjgubun", "ALL");  	     //과정구분
        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
        

        try { 

            sql  = "\n select 'S' subjtype, a.upperclass, b.classname, a.isonoff, get_codenm('0004',a.isonoff) as codenm ";
            sql += "\n      , a.subj, a.subjnm, a.isuse ";
            sql += "\n      , (select grcode from tz_grsubj where subjcourse=a.subj and grcode=" +SQLString.Format(v_grcode) + ") grcode ";
            sql += "\n      , a.eduperiod ";
            sql += "\n from   tz_subj a,  tz_subjatt b ";
            sql += "\n where  a.subjclass  = b.subjclass ";
            sql += "\n and    a.isuse     = 'Y' ";
            
            if ( !ss_subjgubun.equals("ALL") ) { 
            	sql += "\n and a.isonoff = " +SQLString.Format(ss_subjgubun);
            }
            
            if ( !ss_upperclass.equals("ALL") ) { 
  	            sql += "\n and a.upperclass = " +SQLString.Format(ss_upperclass);
            }
            if ( !ss_middleclass.equals("ALL") ) { 
            	sql += "\n and a.middleclass = " +SQLString.Format(ss_middleclass);
            }
            if ( !ss_lowerclass.equals("ALL") ) { 
            	sql += "\n and a.lowerclass = " +SQLString.Format(ss_lowerclass);
            }
            
            sql += "\n union all ";
            sql += "\n select 'C' subjtype , '패키지' upperclass, '패키지' classname, '' isonoff, '패키지' codenm, course, coursenm, 'Y' isuse " ;
            sql += "\n , (select grcode from tz_grsubj where subjcourse=c.course and grcode=" +SQLString.Format(v_grcode) + ") grcode  ";
            sql += "\n , null ";
            sql += "\n  from tz_course c "; 
            
            if ( v_orderColumn.equals("") ) { 
	            sql += "\n order by subjtype, upperclass, subjnm ";
            } else { 
            	sql += "\n order by subjtype, " + v_orderColumn + v_orderType;
	        }            
            

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
    과목리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   과목리스트
    */
    public ArrayList SelectSubjList2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

				String  v_grcode				= box.getString("p_grcode");										 // 교육그룹
        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

        try { 

            sql  = "\n select a.upperclass, b.classname, a.isonoff,  get_codenm('0004',a.isonoff) as codenm, ";
            sql += "\n        a.subj, a.subjnm, a.isuse,a.introducefilenamereal,a.introducefilenamenew,";
            sql += "\n        (select grcode from tz_grrecom where subjcourse=a.subj and grcode=" +SQLString.Format(v_grcode) + ") grcode ";
            sql += "\n from   tz_subj a,  tz_subjatt b ";
            sql += "\n where  a.subjclass = b.subjclass ";
            sql += "\n and    a.isuse     = 'Y' ";
            if ( !ss_upperclass.equals("ALL") ) { 
  	            sql += "\n and    a.upperclass = " +SQLString.Format(ss_upperclass);
            }
            if ( !ss_middleclass.equals("ALL") ) { 
            	sql += "\n and    a.middleclass = " +SQLString.Format(ss_middleclass);
            }
            if ( !ss_lowerclass.equals("ALL") ) { 
            	sql += "\n and    a.lowerclass = " +SQLString.Format(ss_lowerclass);
            }
            if ( v_orderColumn.equals("") ) { 
	            sql += "\n order  by a.upperclass, a.subjnm ";
        	} else { 
        		sql += "\n order  by " + v_orderColumn + v_orderType;
	        }
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
     * 추천과정 리스트
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectRecomSubjList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;

        String  s_userid = box.getSession("userid"); // 사용자ID
        String  s_comp = box.getSession("comp"); // 회사코드
        String  s_jobcd = box.getSession("jobcd"); // 직무코드

        try { 
        	if("1002".equals(s_comp)) {
        		sql = "\n select b.isonoff "
                	+ "\n      , b.subjclass_ktf subjclass "
                	+ "\n      , get_subjclassktfnm(b.upperclass_ktf, '000', '000') as uclassnm"
                	+ "\n      , get_subjclassktfnm(b.upperclass_ktf, b.middleclass_ktf, '000') as mclassnm"
                	+ "\n      , get_subjclassktfnm(b.upperclass_ktf, b.middleclass_ktf, b.lowerclass) as lclassnm"
                	+ "\n      , a.subjcourse as subj "
                	+ "\n      , b.subjnm "
                	+ "\n      , decode(c.subj, null, '-', substr(c.status,2,length(c.status))) as status "
                	+ "\n      , d.satisfaction "
                	+ "\n      , '교육그룹추천' as gu "
                	+ "\n from   tz_grrecom a "
                	+ "\n      , tz_subj b "
                	+ "\n      , (select a.subj "
                	+ "\n              , min(decode(d.isgraduated, 'Y', '0수료', 'N', '1미수료' "
                	+ "\n                    , decode(c.subj, null, '5수강신청' "
                	+ "\n                          , case when a.edustart > to_char(sysdate,'yyyymmddhh24miss') then '4수강신청' "
                	+ "\n                                 when a.eduend < to_char(sysdate,'yyyymmddhh24miss') then '2처리중' "
                	+ "\n                                 else '3수강' "
                	+ "\n                            end))) as status "
                	+ "\n         from   tz_subjseq a "
                	+ "\n              , tz_propose b "
                	+ "\n              , tz_student c "
                	+ "\n              , tz_stold d "
                	+ "\n         where  a.subj = b.subj(+) "
                	+ "\n         and    a.year = b.year(+) "
                	+ "\n         and    a.subjseq = b.subjseq(+) "
                	+ "\n         and    b.subj = c.subj(+) "
                	+ "\n         and    b.year = c.year(+) "
                	+ "\n         and    b.subjseq = c.subjseq(+) "
                	+ "\n         and    b.userid = c.userid(+) "
                	+ "\n         and    c.subj = d.subj(+) "
                	+ "\n         and    c.year = d.year(+) "
                	+ "\n         and    c.subjseq = d.subjseq(+) "
                	+ "\n         and    c.userid = d.userid(+) "
                	+ "\n         and    b.userid = '"+s_userid+"' "
                	+ "\n         group  by a.subj "
                	+ "\n        ) c "
                	+ "\n      , (select subj, avg(distcode1) satisfaction "
                	+ "\n         from   tz_suleach "
                	+ "\n         group  by subj) d "
                	+ "\n where  grcode in (select grcode "
                	+ "\n                   from   tz_grcomp "
                	+ "\n                   where  comp = '"+s_comp+"') "
                	+ "\n and    a.subjcourse = b.subj "
                	+ "\n and    b.subj = c.subj(+) "
                	+ "\n and    a.subjcourse = d.subj(+) "
                	+ "\n union "
                	+ "\n select a.isonoff "
                	+ "\n      , a.subjclass_ktf subjclass "
                	+ "\n      , get_subjclassktfnm(a.upperclass_ktf, '000', '000') as uclassnm"
                	+ "\n      , get_subjclassktfnm(a.upperclass_ktf, a.middleclass_ktf, '000') as mclassnm"
                	+ "\n      , get_subjclassktfnm(a.upperclass_ktf, a.middleclass_ktf, a.lowerclass) as lclassnm"
                	+ "\n      , a.subj "
                	+ "\n      , a.subjnm "
                	+ "\n      , decode(c.subj, null, '-',substr(c.status,2,length(c.status))) as status "
                	+ "\n      , d.satisfaction "
                	+ "\n      , '직무추천' as gu "
                	+ "\n from   tz_subj a "
                	+ "\n      , tz_subjjikmu b "
                	+ "\n      , (select a.subj "
                	+ "\n              , min(decode(d.isgraduated, 'Y', '0수료', 'N', '1미수료' "
                	+ "\n                    , decode(c.subj, null, '5수강신청' "
                	+ "\n                          , case when a.edustart > to_char(sysdate,'yyyymmddhh24miss') then '4수강신청' "
                	+ "\n                                 when a.eduend < to_char(sysdate,'yyyymmddhh24miss') then '2처리중' "
                	+ "\n                                 else '3수강' "
                	+ "\n                            end))) as status "
                	+ "\n         from   tz_subjseq a "
                	+ "\n              , tz_propose b "
                	+ "\n              , tz_student c "
                	+ "\n              , tz_stold d "
                	+ "\n         where  a.subj = b.subj(+) "
                	+ "\n         and    a.year = b.year(+) "
                	+ "\n         and    a.subjseq = b.subjseq(+) "
                	+ "\n         and    b.subj = c.subj(+) "
                	+ "\n         and    b.year = c.year(+) "
                	+ "\n         and    b.subjseq = c.subjseq(+) "
                	+ "\n         and    b.userid = c.userid(+) "
                	+ "\n         and    c.subj = d.subj(+) "
                	+ "\n         and    c.year = d.year(+) "
                	+ "\n         and    c.subjseq = d.subjseq(+) "
                	+ "\n         and    c.userid = d.userid(+) "
                	+ "\n         and    b.userid = '"+s_userid+"' "
                	+ "\n         group  by a.subj "
                	+ "\n        ) c "
                	+ "\n      , (select subj, avg(distcode1) satisfaction "
                	+ "\n         from   tz_suleach "
                	+ "\n         group  by subj) d "
                	+ "\n where  a.subj = b.subj "
                	+ "\n and    b.job_cd = '"+s_jobcd+"' "
                	+ "\n and    a.subj = c.subj(+) "
                	+ "\n and    a.subj = d.subj(+) "
                	+ "\n order by satisfaction ";
        	} else {
            sql = "\n select b.isonoff "
            	+ "\n      , b.subjclass "
            	+ "\n      , get_subjclassnm(b.upperclass, '000', '000') as uclassnm"
            	+ "\n      , get_subjclassnm(b.upperclass, b.middleclass, '000') as mclassnm"
            	+ "\n      , get_subjclassnm(b.upperclass, b.middleclass, b.lowerclass) as lclassnm"
            	+ "\n      , a.subjcourse as subj "
            	+ "\n      , b.subjnm "
            	+ "\n      , decode(c.subj, null, '-', substr(c.status,2,length(c.status))) as status "
            	+ "\n      , d.satisfaction "
            	+ "\n      , '교육그룹추천' as gu "
            	+ "\n from   tz_grrecom a "
            	+ "\n      , tz_subj b "
            	+ "\n      , (select a.subj "
            	+ "\n              , min(decode(d.isgraduated, 'Y', '0수료', 'N', '1미수료' "
            	+ "\n                    , decode(c.subj, null, '5수강신청' "
            	+ "\n                          , case when a.edustart > to_char(sysdate,'yyyymmddhh24miss') then '4수강신청' "
            	+ "\n                                 when a.eduend < to_char(sysdate,'yyyymmddhh24miss') then '2처리중' "
            	+ "\n                                 else '3수강' "
            	+ "\n                            end))) as status "
            	+ "\n         from   tz_subjseq a "
            	+ "\n              , tz_propose b "
            	+ "\n              , tz_student c "
            	+ "\n              , tz_stold d "
            	+ "\n         where  a.subj = b.subj(+) "
            	+ "\n         and    a.year = b.year(+) "
            	+ "\n         and    a.subjseq = b.subjseq(+) "
            	+ "\n         and    b.subj = c.subj(+) "
            	+ "\n         and    b.year = c.year(+) "
            	+ "\n         and    b.subjseq = c.subjseq(+) "
            	+ "\n         and    b.userid = c.userid(+) "
            	+ "\n         and    c.subj = d.subj(+) "
            	+ "\n         and    c.year = d.year(+) "
            	+ "\n         and    c.subjseq = d.subjseq(+) "
            	+ "\n         and    c.userid = d.userid(+) "
            	+ "\n         and    b.userid = '"+s_userid+"' "
            	+ "\n         group  by a.subj "
            	+ "\n        ) c "
            	+ "\n      , (select subj, avg(distcode1) satisfaction "
            	+ "\n         from   tz_suleach "
            	+ "\n         group  by subj) d "
            	+ "\n where  grcode in (select grcode "
            	+ "\n                   from   tz_grcomp "
            	+ "\n                   where  comp = '"+s_comp+"') "
            	+ "\n and    a.subjcourse = b.subj "
            	+ "\n and    b.subj = c.subj(+) "
            	+ "\n and    a.subjcourse = d.subj(+) "
            	+ "\n union "
            	+ "\n select a.isonoff "
            	+ "\n      , a.subjclass "
            	+ "\n      , get_subjclassnm(a.upperclass, '000', '000') as uclassnm"
            	+ "\n      , get_subjclassnm(a.upperclass, a.middleclass, '000') as mclassnm"
            	+ "\n      , get_subjclassnm(a.upperclass, a.middleclass, a.lowerclass) as lclassnm"
            	+ "\n      , a.subj "
            	+ "\n      , a.subjnm "
            	+ "\n      , decode(c.subj, null, '-',substr(c.status,2,length(c.status))) as status "
            	+ "\n      , d.satisfaction "
            	+ "\n      , '직무추천' as gu "
            	+ "\n from   tz_subj a "
            	+ "\n      , tz_subjjikmu b "
            	+ "\n      , (select a.subj "
            	+ "\n              , min(decode(d.isgraduated, 'Y', '0수료', 'N', '1미수료' "
            	+ "\n                    , decode(c.subj, null, '5수강신청' "
            	+ "\n                          , case when a.edustart > to_char(sysdate,'yyyymmddhh24miss') then '4수강신청' "
            	+ "\n                                 when a.eduend < to_char(sysdate,'yyyymmddhh24miss') then '2처리중' "
            	+ "\n                                 else '3수강' "
            	+ "\n                            end))) as status "
            	+ "\n         from   tz_subjseq a "
            	+ "\n              , tz_propose b "
            	+ "\n              , tz_student c "
            	+ "\n              , tz_stold d "
            	+ "\n         where  a.subj = b.subj(+) "
            	+ "\n         and    a.year = b.year(+) "
            	+ "\n         and    a.subjseq = b.subjseq(+) "
            	+ "\n         and    b.subj = c.subj(+) "
            	+ "\n         and    b.year = c.year(+) "
            	+ "\n         and    b.subjseq = c.subjseq(+) "
            	+ "\n         and    b.userid = c.userid(+) "
            	+ "\n         and    c.subj = d.subj(+) "
            	+ "\n         and    c.year = d.year(+) "
            	+ "\n         and    c.subjseq = d.subjseq(+) "
            	+ "\n         and    c.userid = d.userid(+) "
            	+ "\n         and    b.userid = '"+s_userid+"' "
            	+ "\n         group  by a.subj "
            	+ "\n        ) c "
            	+ "\n      , (select subj, avg(distcode1) satisfaction "
            	+ "\n         from   tz_suleach "
            	+ "\n         group  by subj) d "
            	+ "\n where  a.subj = b.subj "
            	+ "\n and    b.job_cd = '"+s_jobcd+"' "
            	+ "\n and    a.subj = c.subj(+) "
            	+ "\n and    a.subj = d.subj(+) "
            	+ "\n order by satisfaction ";
        	}
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
    
}
