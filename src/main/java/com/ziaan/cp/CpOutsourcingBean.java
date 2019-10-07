package com.ziaan.cp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class CpOutsourcingBean { 

	
	private ConfigSet config;
    private int row; 
    private int adminrow; 
    private static final String JOBEXETYPE_SUBJ = "SUBJ";
    private static final String JOBEXETYPE_PRROPOSE = "PRROPOSE";
    private static final String JOBEXETYPE_SCORE = "SCOR";
	
    public CpOutsourcingBean() { 
	    try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );                  //이 모듈의 페이지당 row 수를 셋팅한다
            adminrow = Integer.parseInt(config.getProperty("page.bulletin.adminrow") );        //이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
     * JOB APPLICATION INFO
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectJobList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();            
            list = new ArrayList();

			sql =  " select                \n";
			sql += "   jobnm,              \n";
			sql += "   callprogram,        \n";
			sql += "   jobexetype,         \n";
			sql += "   jobtimetype,        \n";
			sql += "   starttime,          \n";
			sql += "   exectime,           \n";
			sql += "   jobexcuteyn,        \n";
			sql += "   use_yn,             \n";
			sql += "   param               \n";
			sql += " from  tz_cronjobs_t   \n";
            
            ls = connMgr.executeQuery(sql);

			ls.setPageSize(adminrow);                       // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();       // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();       // 전체 row 수를 반환한다
            
            while ( ls.next() ) { 
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(adminrow));
				list.add(dbox);
            }
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

    
    /**
     * 위탁과정의 과정/차수정보를 받는다.
     * @param box
     * @return
     * @throws Exception
     */
    public String selectListCpSubjSubjseq(RequestBox box) throws Exception {
    	HttpClient client = new HttpClient();
    	PostMethod httppost = null;
    	
    	DBConnectionManager	connMgr	= null;
    	PreparedStatement   pstmt   = null;        
    	ListSet             ls      = null;
    	
    	String ls_result;
    	String ls_param = box.getString("p_execMonth");
    	String v_year = "";
    	String v_month = "";
    	String lsRecvHttp = "";
    	String sql = "";
    	
    	String v_enc = box.getStringDefault("p_enc", "A");	
    	try{
    		connMgr = new DBConnectionManager();   
    		
    		sql =  " select                \n";
    		sql += "   jobnm,              \n";
    		sql += "   callprogram,        \n";
    		sql += "   jobexetype,         \n";
    		sql += "   jobtimetype,        \n";
    		sql += "   starttime,          \n";
    		sql += "   exectime,           \n";
    		sql += "   jobexcuteyn,        \n";
    		sql += "   use_yn,             \n";
    		sql += "   param               \n";
    		sql += " from  tz_cronjobs_t   \n";
    		sql += " where 1=1  \n";
    		sql += "   and enc = "+StringManager.makeSQL(v_enc)+"  \n";
    		sql += "   and jobexetype = '"+JOBEXETYPE_SUBJ+"'  \n";
    		
    		ls = connMgr.executeQuery(sql);
    		
    		if ( ls.next() ) { 
    			lsRecvHttp = ls.getString("callprogram");
    		}
    		
    		if(ls != null) ls.close();
    		
    		if (lsRecvHttp == null )
    		{
    			return "F";
    		}
			
    		if(ls_param.equals("")){
    			ls_param = FormatDate.getDate("yyyyMMddHHmmss").substring(0,6);
    		}
    		
    		//파라매터 값 지정
    		v_year = ls_param.substring(0,4);
    		v_month = ls_param.substring(4,6);
    		
    		//과정 차수 정보 받아 오는 주소 			
    		if(v_enc.equals("A")){ //크레듀는 파라메터 정보를 년도+월
    			lsRecvHttp = lsRecvHttp +"?p_yyyymm="+ls_param;			
    		}else if(v_enc.equals("Y")||v_enc.equals("W")){
    			lsRecvHttp = lsRecvHttp +"?p_year="+v_year+"&p_month="+v_month;				
    		}
    		
    		httppost = new PostMethod(lsRecvHttp);
    		
    		httppost.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");
    		
    		client.executeMethod(httppost);
    		
    		if (httppost.getStatusCode() == HttpStatus.SC_OK) {
    			ls_result = httppost.getResponseBodyAsString();
    			
    			//윈글리쉬은 언어가 asp 이므로 반드시 한글로 인코딩을 해야 한글이 깨지지 않는다. 안깨져서 주석처리함.
    			if(v_enc !=null && (v_enc.equals("W"))){
    				ls_result = StringManager.korEncode(ls_result);
    			}
    			
    			if(ls_result!=null && ls_result.indexOf("|") > -1){
    				//HanawProc proc = new HanawProc();
    				//proc.subjProcess(ls_result,enc, v_year);
    				
    				//ls_result = _CONFIG._SUCESS;
    			}
    			else
    			{
    				ls_result ="F"+"[LOG=NO-DATA"+ls_result+"]";
    			}
    		} else {
    			ls_result = "F"+"[LOG="+httppost.getStatusLine().toString()+"]";
    		}
    	}
    	catch(Exception e){
    		ErrorManager.getErrorStackTrace(e);
    		throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
    	}
    	finally {
    		httppost.releaseConnection();
    	}
    	return ls_result ;
    }
    
 
    /**
     * 위탁과정의 과정/차수정보 매칭 조회
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectViewSubj(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		StringBuffer strSQL = null;
		DataBox dbox = null;
		ArrayList list = null;

		String v_con_subj = box.getString("p_con_subj");
		String v_subj = box.getStringDefault("p_subj", "");
		String v_year = box.getString("p_con_year");
		String v_edustart = box.getString("p_con_edustart");
		
		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			strSQL = new StringBuffer();
			
			strSQL.append(" \n select subj from tz_subj WHERE cpsubj = " + StringManager.makeSQL(v_con_subj));
			ls = connMgr.executeQuery(strSQL.toString());
			while ( ls.next() ) { 
				v_subj = ls.getString("subj");
			}
			
			if(ls != null){ls.close();}
			
			strSQL.setLength(0);
			strSQL.append(" \n select subj, subjnm, year, edustart, cpsubjseq, subjseq") ;
			strSQL.append(" \n   from tz_subjseq ") ;
			strSQL.append(" \n  where subj = " + StringManager.makeSQL(v_subj)) ;
			strSQL.append(" \n    and year = " + StringManager.makeSQL(v_year)) ;
			strSQL.append(" \n    and edustart = " + StringManager.makeSQL(v_edustart)) ;
			strSQL.append(" \n  order by subjseq desc ") ;

			ls = connMgr.executeQuery(strSQL.toString());

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}
    
    /**
     * CP과정기수와의 동기화
     * @param box
     * @return
     * @throws Exception
     */
    public int UpdateCpsubjseqSync(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        String v_luserid = box.getSession("userid");
        String v_con_subjseq = box.getString("p_con_subjseq");
        String v_subj = box.getString("p_subj");
        String v_con_year = box.getString("p_con_year");
        String v_con_edustart = box.getString("p_con_edustart");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql = " UPDATE tz_subjseq SET ";
            sql += "    cpsubjseq = ?, luserid = ? , ldate = to_char(sysdate,'YYYYMMDDHH24MISS') ";
            sql += "  WHERE subj = ? AND year = ? AND edustart = ? ";

            pstmt = connMgr.prepareStatement(sql);			
			
            int index = 0;
            pstmt.setString(++index,	v_con_subjseq);	
            pstmt.setString(++index,	v_luserid);				            
            pstmt.setString(++index,	v_subj);				            
            pstmt.setString(++index,	v_con_year);
            pstmt.setString(++index,	v_con_edustart);	

            isOk = pstmt.executeUpdate();
            
            if ( isOk > 0) { 
            	connMgr.commit();
            } else { 
                connMgr.rollback();
            }

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
    
    /**
     * 학습정보(수강신청자 정보)
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectListCpSubjPropose(RequestBox box) throws Exception {
    	HttpClient client = new HttpClient();
    	PostMethod httppost = null;
    	
    	DBConnectionManager	connMgr	= null;
    	ListSet             ls      = null;
    	StringBuffer strSQL         = null;
    	DataBox dbox                = null;
    	ArrayList     list          = null;
    	
    	String v_process = box.getString("p_process");
    	String v_year = "";
    	String v_month = "";
    	String sql = "";
    	String v_owner = "";
    	String v_enc = box.getStringDefault("p_enc", "A");	
    	String v_con_subj = box.getString("p_con_subj");
    	String v_con_edustart = box.getString("p_con_edustart");
    	String v_subj = "";
    	
    	String v_execMonth = box.getString("p_execMonth");
    	
    	try{
    		connMgr = new DBConnectionManager(); 
    		strSQL  = new StringBuffer();
    		
			strSQL.append(" \n select subj from tz_subj WHERE cpsubj = " + StringManager.makeSQL(v_con_subj));
			
			ls = connMgr.executeQuery(strSQL.toString());
			
			while ( ls.next() ) { 
				v_subj = ls.getString("subj");
			}
			
			if(ls != null){ls.close();}
    		
    		if(v_enc.equals("A")){	        //크레듀
    			v_owner = "00005";
    		}else if(v_enc.equals("Y")){	//YBM
    			v_owner = "00011";
    		}else if(v_enc.equals("W")){	//윈글리쉬
    			v_owner = "00010";
    		}
    		
    		list    = new ArrayList();
    		
    		strSQL.setLength(0);
    		strSQL.append("\n select a.* ") ;
    		strSQL.append("\n   from ( ") ;
    		strSQL.append("\n    	  select d.cpsubj subj, a.year, c.cpsubjseq subjseq, ") ;
    		strSQL.append("\n    	         b.userid, b.name, b.email, b.handphone mobile, b.userid cono, ") ;
    		strSQL.append("\n    	 		 b.zip_cd post, b.address, get_compnm(b.comp) compnm, b.position_nm, b.lvl_nm, ") ;
    		strSQL.append("\n    	 		 decode(a.stustatus,'Y','1','2') protype, a.ldate, ") ;
    		strSQL.append("\n    	 		 a.subj subj2, a.year year2, a.subjseq subjseq2 ") ;
    		strSQL.append("\n            from tz_student a, tz_member b, tz_subjseq c , tz_subj d ") ;
    		strSQL.append("\n           where a.userid  = b.userid ") ;
    		strSQL.append("\n             and a.subj    = c.subj ") ;
    		strSQL.append("\n             and a.subjseq = c.subjseq ") ;
    		strSQL.append("\n             and a.year    = c.year ") ;
    		strSQL.append("\n             and a.subj    = d.subj ") ;
    		strSQL.append("\n             and isclosed  !='Y' ") ;
    		strSQL.append("\n             and d.owner   =  " + StringManager.makeSQL(v_owner)) ;
    		if(v_process.equals("selectViewSubj")){
    			strSQL.append("\n         and c.edustart   =  " + StringManager.makeSQL(v_con_edustart)) ;    //추가
    		    strSQL.append("\n         and c.subj   =  " + StringManager.makeSQL(v_subj)) ;                //추가
    		} else if(v_process.equals("selectCpProposeUser")){
    			strSQL.append("\n         and c.edustart   like  '" +v_execMonth + "%'") ;    //추가
    		}
    		//strSQL.append("\n             and substr(a.ldate,0,8) = TO_CHAR(SYSDATE,'YYYYMMDD') ") ;
    		//strSQL.append("\n             and a.stdgubun ='0' ") ;
    		strSQL.append("\n         UNION ALL ") ;
    		strSQL.append("\n           select d.cpsubj subj, a.year, c.cpsubjseq subjseq, ") ;
    		strSQL.append("\n         		  b.userid, b.name, b.email, b.handphone mobile, b.userid cono, ") ;
    		strSQL.append("\n         		  b.zip_cd post, b.address, get_compnm(b.comp) compnm, b.position_nm, b.lvl_nm, '2' protype, a.ldate, ") ;
    		strSQL.append("\n         		  a.subj subj2, a.year year2, a.subjseq subjseq2 ") ;
    		strSQL.append("\n            from tz_cancel a, tz_member b, tz_subjseq c , tz_subj d ") ;
    		strSQL.append("\n           where a.userid  = b.userid ") ;
    		strSQL.append("\n             and a.subj    = c.subj ") ;
    		strSQL.append("\n             and a.subjseq = c.subjseq ") ;
    		strSQL.append("\n             and a.year    = c.year ") ;
    		strSQL.append("\n             and a.subj    = d.subj ") ;
    		strSQL.append("\n             and isclosed !='Y' ") ;
    		//strSQL.append("\n             and substr(a.ldate,0,8) = TO_CHAR(SYSDATE,'YYYYMMDD') ") ;
    		strSQL.append("\n             and d.owner = " + StringManager.makeSQL(v_owner)) ;
    		if(v_process.equals("selectViewSubj")){
    			strSQL.append("\n         and c.edustart   =  " + StringManager.makeSQL(v_con_edustart)) ;    //추가
    		    strSQL.append("\n         and c.subj   =  " + StringManager.makeSQL(v_subj)) ;                //추가
    		}else if(v_process.equals("selectCpProposeUser")){
    			strSQL.append("\n         and c.edustart   like  '" +v_execMonth + "%'") ;    //추가
    		}
    		strSQL.append("\n          ) a ") ;
    		strSQL.append("\n where subjseq is not null ") ;
    		strSQL.append("\n order by protype desc, ldate desc ") ;

			ls = connMgr.executeQuery(strSQL.toString());

		     while(ls.next())
		     {
		    	 Hashtable data = new Hashtable();

		    	 data.put("p_subj",ls.getString("subj"));
		    	 data.put("p_year",ls.getString("year"));    	 
		    	 data.put("p_subjseq",ls.getString("subjseq"));
		    	 data.put("p_userid",ls.getString("userid"));   
		    	 data.put("p_name",ls.getString("name"));  
		    	 data.put("p_email",ls.getString("email")+" ");
		    	 data.put("p_mobile",ls.getString("mobile")+" ");   
		    	 data.put("p_cono",ls.getString("cono"));
		    	 data.put("p_post",ls.getString("post")+" ");   
		    	 data.put("p_address",ls.getString("address")+" ");
		    	 data.put("p_protype",ls.getString("protype"));  
		    	 data.put("p_compnm",ls.getString("compnm")+" ");     
		    	 data.put("p_position_nm",ls.getString("position_nm")+" ");  
		    	 data.put("p_lvl_nm",ls.getString("lvl_nm")+" ");      	 
		    	 data.put("p_subj2",ls.getString("subj2"));
		    	 data.put("p_year2",ls.getString("year2"));    	 
		    	 data.put("p_subjseq2",ls.getString("subjseq2"));    	 
		          
		    	 list.add(data);  	 
		     }
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}
    
    /**
     * 위탁과정 입과자 정보 동기화
     * @param box
     * @return
     * @throws Exception
     */
    public int UpdateCpProposeSync(RequestBox box) throws Exception { 
    	DBConnectionManager	connMgr	= null;
    	ListSet             ls      = null;
    	StringBuffer strSQL         = null;
    	DataBox dbox                = null;
    	
	    HttpClient client = new HttpClient();
	    PostMethod httppost = null;
	    String ls_result = "";
	    String sql = "";
	    
	    String v_enc = box.getString("p_enc");
	    String v_jobexetype = "PROPOSE";
	    String lsRecvHttp = "";
       
	    try{
	    	
            connMgr = new DBConnectionManager();            

			sql =  " select callprogram             \n";
			sql += "   from  tz_cronjobs_t   \n";
			sql += "  where  jobexetype = "+ StringManager.makeSQL(v_jobexetype) +"   \n";
			sql += "    and  enc = "+ StringManager.makeSQL(v_enc) +"   \n";
            
			
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
            	lsRecvHttp = ls.getString("callprogram");
            }
            
            if(ls != null){ ls.close(); }
            
    	    if (lsRecvHttp.equals(""))
    	    {
    	      return -99;
    	    }

	    	if(v_enc != null )
	    	{    		
	    	  ArrayList list = (ArrayList)  this.selectListCpSubjPropose(box);

		      int v_result 	= 0;
		      int v_success = 0;
		      int v_fail 	= 0;
		      if(list.size()>0){
		    	  for(int i = 0; i <= list.size(); i++){
		    		  
		       	 	Hashtable data = (Hashtable)list.get(i);
		     		System.out.println("HTTPCallResult_//:"+lsRecvHttp);
		     		
			        httppost = new PostMethod(lsRecvHttp);
			  	
			        httppost.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");	    	 

			        httppost.setParameter("p_subj", 	(String)data.get("p_subj"));//과정코드 세팅	
			        httppost.setParameter("p_year", 	(String)data.get("p_year"));//과정년도 세팅	
			        httppost.setParameter("p_subjseq",	(String)data.get("p_subjseq"));//과정차수 세팅	
			        httppost.setParameter("p_userid",	(String)data.get("p_userid"));//사용자 아이디 세팅
			        httppost.setParameter("p_name",		(String)data.get("p_name"));//수강생 이름 세팅
			        httppost.setParameter("p_email",	(String)data.get("p_email"));//수강생 이메일 세팅
			        httppost.setParameter("p_mobile",	(String)data.get("p_mobile"));//수강생 휴대폰번호 세팅
			        httppost.setParameter("p_cono",		(String)data.get("p_cono"));//수강생 사번 세팅
			        httppost.setParameter("p_post",		(String)data.get("p_post"));//수강생 우편번호 세팅
			        httppost.setParameter("p_addr",		(String)data.get("p_address"));//수강생 주소 세팅
			        httppost.setParameter("p_protype",	(String)data.get("p_protype"));//수강생 수강정보 세팅
			        httppost.setParameter("p_division",	(String)data.get("p_compnm"));//수강생 회사 세팅
			        httppost.setParameter("p_depart",   (String)data.get("p_position_nm"));//수강생 부서 세팅
			        httppost.setParameter("p_position", (String)data.get("p_lvl_nm"));//수강생 직급 세팅
			        
			        String v_url = "";
			        v_url = lsRecvHttp+"?p_subj="+ (String)data.get("p_subj")
			        		+"&p_year="+	(String)data.get("p_year")
			        		+"&p_subjseq="+	(String)data.get("p_subjseq")
			        		+"&p_userid="+	(String)data.get("p_userid")
			        		+"&p_name="+	(String)data.get("p_name")
			        		+"&p_email="+	(String)data.get("p_email")
			        		+"&p_mobile="+	(String)data.get("p_mobile")
			        		+"&p_cono="+	(String)data.get("p_cono")
			        		+"&p_post="+	(String)data.get("p_post")
			        		+"&p_addr="+	(String)data.get("p_address")
			        		+"&p_protype="+	(String)data.get("p_protype")
			        		+"&p_division="+(String)data.get("p_compnm")
			        		+"&p_depart="+	(String)data.get("p_position_nm")
	        				+"&p_position="+(String)data.get("p_lvl_nm");//값을셋팅			        
			        	        
			        client.executeMethod(httppost);		        
			        v_result = httppost.getStatusCode();
			        
			        if(v_result == 200){
			        	v_success = v_success + 1;
			        	String v_protype = (String)data.get("p_protype");
			        	if(v_protype.equals("1")){
			        		this.proposeSuccessProcess((String)data.get("p_subj2"),(String)data.get("p_year2"),(String)data.get("p_subjseq2"),(String)data.get("p_userid"));
			        	}
			        }else{
			        	v_fail = v_fail + 1;
			        }			       			        
				      
		    	}		// end for    	
		      }// end if
		        ls_result = "RESULT"+"[LOG=SUCCESS="+v_success+";FAIL = "+v_fail+"]";
	     	}
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    finally {
	    	if(httppost != null) httppost.releaseConnection();
	    }
	    return 0 ;
	  }	
    
    /**
     * 입과자 신청완료처리
     * @param p_datas
     * @param p_comp
     * @param p_uri
     */
    public void proposeSuccessProcess(String p_subj, String p_year, String p_subjseq, String p_userid){
    	
    	DBConnectionManager	connMgr	= null;
    	ListSet             ls      = null;
    	StringBuffer strSQL         = null;
    	DataBox dbox                = null;
    	
    	PreparedStatement pstmt = null;
    	String sql 		= ""; 
    	int isOk = 0;
    	try {
    		
    		connMgr = new DBConnectionManager();   
    		connMgr.setAutoCommit(false);
    		
    		sql = "update  tz_student set stdgubun ='Y' where subj=? and year=? and subjseq = ? and userid = ?";
    		
    		pstmt = connMgr.prepareStatement(sql);
    		
    		pstmt.setString(1, p_subj);
    		pstmt.setString(2, p_year);
    		pstmt.setString(3, p_subjseq);
    		pstmt.setString(4, p_userid);        
    		
    		isOk = pstmt.executeUpdate();
    		
    		if(pstmt != null) pstmt.close();
    		
    		if ( isOk > 0) { 
    			connMgr.commit();
    		} else { 
    			connMgr.rollback();
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	} finally { 
    		if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
    		if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
    		if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	}
    }  
    
    /**
     * 위탁과정 수료자정보 리스트
     * @param box
     * @return
     * @throws Exception
     */
    public String selectListCpSubjScore(RequestBox box) throws Exception {
    	HttpClient client = new HttpClient();
    	PostMethod httppost = null;
    	
    	DBConnectionManager	connMgr	= null;
    	PreparedStatement   pstmt   = null;        
    	ListSet             ls      = null;
    	
    	String ls_result = "";
    	String ls_param = box.getString("p_execMonth");
    	String v_year = "";
    	String v_month = "";
    	String lsRecvHttp = "";
    	String sql = "";
    	
    	String v_enc = box.getString("p_enc");	
    	try{
    		connMgr = new DBConnectionManager();   
    		
    		sql =  " select                \n";
    		sql += "   jobnm,              \n";
    		sql += "   callprogram,        \n";
    		sql += "   jobexetype,         \n";
    		sql += "   jobtimetype,        \n";
    		sql += "   starttime,          \n";
    		sql += "   exectime,           \n";
    		sql += "   jobexcuteyn,        \n";
    		sql += "   use_yn,             \n";
    		sql += "   param               \n";
    		sql += " from  tz_cronjobs_t   \n";
    		sql += " where 1=1  \n";
    		sql += "   and enc = "+StringManager.makeSQL(v_enc)+"  \n";
    		sql += "   and jobexetype = '"+JOBEXETYPE_SCORE+"'  \n";
    		
    		ls = connMgr.executeQuery(sql);
    		
    		if ( ls.next() ) { 
    			lsRecvHttp = ls.getString("callprogram");
    		}
    		
    		if(ls != null) ls.close();
    		
    		if (lsRecvHttp == null )
    		{
    			return "F";
    		}
    		
    		if(box.getString("p_execMonth").equals("")){
	    		ls_param = FormatDate.getDate("yyyyMMddHHmmss").substring(0,6);
    		}
	    		
	    		//파라매터 값 지정
	    	v_year = ls_param.substring(0,4);
	    	v_month = ls_param.substring(4,6);
    		
    		if(v_enc != null )
    		{
    			
    			if(v_enc.equals("A")){	//크레듀
    				lsRecvHttp = lsRecvHttp +"?p_yyyymm="+ls_param;  
    			}else  if(v_enc.equals("Y")){ //YBM
    				lsRecvHttp = lsRecvHttp +"?p_year="+v_year+"&p_month="+v_month;
    			}else  if(v_enc.equals("W")){ //윈글리쉬
    				lsRecvHttp = lsRecvHttp +"?p_ym="+ls_param;     		
    			}
    		}	
    		
    		//lsRecvHttp = lsRecvHttp +ls_param;
    		System.out.println("HTTPCallResult_//:"+lsRecvHttp);
    		httppost = new PostMethod(lsRecvHttp);
    		
    		httppost.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");
    		client.executeMethod(httppost);
    		System.out.println("httppost.getStatusCode()==="+httppost.getStatusCode());
    		
    		if (httppost.getStatusCode() == HttpStatus.SC_OK) {
    			
    			BufferedReader br = new BufferedReader(new InputStreamReader(httppost.getResponseBodyAsStream()));
    			StringBuffer buf = new StringBuffer();
    			
    			String s = null;
    			
    			while (null != (s = br.readLine())) {
    				
    				buf.append(s);
    				
    			}
    			
    			br.close();
    			
    			ls_result = buf.toString();
    		}
    	}
    	catch(Exception e){
    		ErrorManager.getErrorStackTrace(e);
    		throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
    	}
    	finally {
    		httppost.releaseConnection();
    	}
    	return ls_result ;
    }
    	    
    public static String getSubjnm(String cpsubj) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	
    	String sql = "";
    	String subjnm = "";
    	
    	try 
    	{
    		connMgr = new DBConnectionManager();
    		String s_subj = cpsubj;
    		
    		sql =
    			"\n  SELECT subjnm FROM tz_subj WHERE cpsubj = " + SQLString.Format( s_subj );
    		
    		ls = connMgr.executeQuery( sql );
    		
    		while ( ls.next() )
    		{
    			subjnm = ls.getString( "subjnm" ); 
    		}
    	}
    	catch (Exception ex) 
    	{
    		ErrorManager.getErrorStackTrace(ex);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	}
    	finally 
    	{
    		if(ls != null) { try { ls.close(); }catch (Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
    	}
    	
    	return subjnm;         
    }    	
    
    public int getExecuteCommand(String Command) throws Exception {
    	
    	System.out.println("init getExecuteCommand");
    	Process p = null;
    	System.out.println("ls");
    	try {
    		p = Runtime.getRuntime().exec("ls");
    		p.waitFor ();
    		
    		if (p.exitValue () != 0) {
    			BufferedReader err = new BufferedReader (new InputStreamReader (p.getErrorStream ()));
    			while (err.ready()) 
    				System.out.println ("ERR"+err.readLine ());
    			err.close ();
    			
    		} else { 
    			BufferedReader out = new BufferedReader (new InputStreamReader (p.getInputStream()));
    			while (out.ready()) 
    				System.out.println ("O K"+ out.readLine ());
    			out.close ();
    			
    		}
    		
    		p.destroy ();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return 0;
    }    
    	    
}
