// **********************************************************
//  1. f      ��: ȸ����
//  2. �wα׷���? MemberBean.java
//  3. ��      ��: ȸ����
//  4. ȯ      ��: JDK 1.4
//  5. ��      o: 1.0
//  6. ��      ��: 
//  7. ��      d:
// **********************************************************
package com.ziaan.homepage;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.Encrypt;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeData;

public class MemberBean { 

    public MemberBean() { }

    /**
     * ȸ�� ���� ���� ����
     * @param  box       receive from the form object and session
     * @return int       �ߺ� ����
     * @throws Exception
     */
    public int getExistMember(RequestBox box) throws Exception { 
        DBConnectionManager connMgr  = null;
        ListSet             ls       = null;
        String              sql      = "";

        String              v_name   = box.getString("p_name");
        String              v_birth_date  = box.getString("p_birth_date1")+box.getString("p_birth_date2");
        
        int                 returnValue = 0;
        ConfigSet   ziaanconfig = new ConfigSet();
        String      v_retireday     = ziaanconfig.getProperty("retire.date");
        try { 
              connMgr = new DBConnectionManager();
              /*���?���� �� 2008.11.07
              sql = "SELECT                                \n" +
                    "    userid                            \n" +
                    ",   isretire                          \n" +
                    ",   retire_date                       \n" +
                    ",    ( select codenm                  \n" +
                    "      from tz_code                    \n" +
                    "      where code = a.retire_type      \n" +
                    "      and gubun = '0069'              \n" +
                    "    ) retire_type                     \n" +
                    ",   retire_reason                     \n" +
                    "FROM                                  \n" +
                    "    tz_member a                       \n" +
                    "WHERE                                 \n" +
                    "        name  = " + StringManager.makeSQL( v_name ) +"      \n" +
                    "    AND birth_date = " + StringManager.makeSQL( v_birth_date ) +"     \n"+
                    "AND '1'   = DECODE(NVL(isignoreretiredate, 'N'), 'Y', '2', DECODE(isretire, 'Y', CASE WHEN sysdate between TO_DATE(retire_date, 'YYYYMMDDHH24MISS') and add_months(to_date(retire_date, 'yyyyMMddhh24miss'), 3) THEN '1' \n"+    
                    "                                                                            ELSE '2'                                                                                                                           \n"+
                    "                                                                            END                                                                                                                                \n"+     
                    "                       , '1'                                                                                                                                                                                   \n"+
                    "               )                                                                                                                                                                                               \n"+
                    ")                                                                                                                                                                                                              \n";
              */
              sql = " SELECT 														\n" + 
              		" 		userid 													\n" + 
              		"  	,	isretire												\n" + 
              		"  	,	retire_date												\n" +
              		"	,    ( select codenm                  						\n" +
                    "      		from tz_code                    					\n" +
                    "      		where code = a.retire_type      					\n" +
                    "      		and gubun = '0069'              					\n" +
                    "    	) retire_type                     						\n" +
              		"  	,	retire_reason														\n" + 
              		"  	,	TO_NUMBER(TO_CHAR(SYSDATE, 'YYYYMMDD')) - TO_NUMBER(TO_DATE(retire_date, 'YYYYMMDD')) as retireday										\n" + 
              		" FROM                                  						\n" +
                    "     tz_member a                       						\n" +
                    " WHERE                                 						\n" +
                    "          birth_date = fn_crypt('1', " + StringManager.makeSQL( v_birth_date ) +" , 'knise')    	\n" +
                    //"    AND "+v_retireday +" <       	\n" +
              		"																  " ;
            	  
              
              //System.out.println("sql : " + sql);

              ls = connMgr.executeQuery(sql);
              if ( ls.next() ) { 
                  returnValue = 1;
                  box.put("p_isretire", ls.getString("isretire"));
                  box.put("p_retire_type", ls.getString("retire_type"));
                  box.put("p_retire_reason", ls.getString("retire_reason"));
                  box.put("p_retire_date", ls.getString("retire_date"));
                  box.put("p_retireday", ls.getString("retireday"));	// �?ÿ���?Ż����; �� ��
              }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls      != null ) { try { ls.close();               } catch ( Exception e )   { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return returnValue;
    }
    
    public ArrayList getZipcode(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        ArrayList           list    = null;
        String              sql     = "";
        //String              v_dong  = box.getString("p_dong");
        String 				v_dong  = box.getStringDefault("p_dong", "00000");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = "SELECT                                                        \n" +
                   "    zipcode,                                                  \n" +
                   "    sido,                                                     \n" +
                   "    gugun,                                                    \n" +
                   "    dong,                                                     \n" +
                   "    bunji                                                     \n" +
                   "FROM                                                          \n" +
                   "    tz_zipcode                                                \n" +
                   "WHERE                                                         \n" +
                   "    dong like '%" + v_dong + "%'                              \n" +
                   "ORDER BY                                                      \n" +
                   "    seq asc                                                   \n" ;

            System.out.println(sql);
            if ( !v_dong.equals("") ) {
                ls = connMgr.executeQuery(sql);
    
                while ( ls.next() ) {
                    dbox = ls.getDataBox();
                    
                    dbox.put("zipcode"       ,ls.getString("zipcode") );
                    dbox.put("sido"          ,ls.getString("sido") );
                    dbox.put("gugun"         ,ls.getString("gugun") );
                    dbox.put("dong"          ,ls.getString("dong") );
                    dbox.put("bunji"         ,ls.getString("bunji") );
    
                    list.add(dbox);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null      ) { try { ls.close();               } catch ( Exception e )   { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }    

    
    public ArrayList getCodeData(RequestBox box, String gubunCode) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        CodeData            data    = null;

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = "SELECT                                                   \n" +
                   "    code,                                                \n" +
                   "    codenm                                               \n" +
                   "FROM                                                     \n" +
                   "    tz_code                                              \n" +
                   "WHERE                                                    \n" +
                   "    gubun  = " + StringManager.makeSQL(gubunCode) + "    \n" +
                   "order by                                                 \n" +
                   "    code asc                                             \n" ;

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new CodeData();

                data.setCode( ls.getString("code") );
                data.setCodenm( ls.getString("codenm") );

                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null      ) { try { ls.close();               } catch ( Exception e )   { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }    

    public int addUser(RequestBox box) throws Exception {
        DBConnectionManager connMgr  = null;
        PreparedStatement   pstmt    = null;
        
        String              sql          = "";
        String              strTableName = "TZ_MEMBER";
        int                 isOk         = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
  			if(box.getSession("s_name").equals("") || box.getSession("s_birth_date").equals("") || box.getString("p_userid").equals("")){
  				return isOk = 0;
  			}
            
          // �ʼ� �Է»���
            String    name            = box.getSession("s_name");
            String    birth_date           = box.getSession("s_birth_date");
                   //   birth_date           = StringManager.BASE64Encode(birth_date);
            String    userid          = box.getString("p_userid");
            String    pwd             = box.getString("p_pwd");
            String    emp_gubun       = box.getString("p_emp_gubun");
//                      pwd             = StringManager.encode(pwd);
            //          pwd             = StringManager.BASE64Encode(pwd);

            String    post1           = box.getString("p_post1");
            String    post2           = box.getString("p_post2");
            String	  post			  = post1 +"-"+ post2;
            String    address1        = box.getString("p_address1");
            String    address2        = box.getString("p_address2");
            String    address         = address1 + address2;
            
            String    e_post1           = "";
            String    e_post2           = "";
            String	  e_post			= "";
            String    e_address1        = "";
            String    e_address2        = "";
            String    e_address         = "";
            

         
            if(emp_gubun.equals("P")){ //�кθ�
            	e_post1 	= box.getString("p_spost1");
            	e_post2		= box.getString("p_spost2");
        		e_post		= e_post1 +"-"+ e_post2;
        		e_address1	= box.getString("p_saddress1");
        		e_address2	= box.getString("p_saddress2");
        		e_address	= e_address1 + e_address2;
            }else if (emp_gubun.equals("T") || emp_gubun.equals("E")) { // T:����, E:Ư��0��v��
            	e_post1 	= box.getString("p_mpost1");
            	e_post2		= box.getString("p_mpost2");
        		e_post		= e_post1 +"-"+ e_post1;
        		e_address1	= box.getString("p_maddress1");
        		e_address2	= box.getString("p_maddress2");
        		e_address	= e_address1 + e_address2;
            	
            }
            System.out.println(emp_gubun+"==========emp_gubun==============");
            System.out.println(e_address+"==========e_address==============");
            System.out.println(e_post+"==========e_post==============");
                 
            String    hometel         = box.getString("p_hometel");
            String    handphone       = box.getString("p_handphone");

            //String    company		  = box.getString("p_position_nm");	//ȸ���?ȸ��d��
            String    lvl_nm		  = box.getString("p_lvl_nm");	//��å��
            String    job_nm		  = box.getString("p_job_nm");	//���?
            String    email           = box.getString("p_email1")+"@"+box.getString("p_email2");
            String    v_email_get 	  = box.getString("p_ismailling");
            String    v_company       = box.getString("s_company");  //ȸ���?ȸ��d��
            String    v_issms         = box.getString("p_issms");  //sms ��ſ���?
            String    v_position_nm   = box.getString("p_position_nm");
            String    v_isretire      = "N";
            
            String	  v_job_cd		  = box.getString("p_job_cd");
            String	  v_user_path	  = box.getString("p_user_path");
            String	  v_handphone_no  = box.getString("p_handphone_no");
            String	  v_dept_cd		  = box.getString("p_dept_cd");
            String	  v_agency_cd	  = box.getString("p_agency_cd");
            String	  v_hrdc		  = box.getString("p_hrdc"); //���� = MS :�б� , MH :���� /  �кθ� = SJ: ���� , SH : ����
            String	  v_virtualNo		  = box.getString("p_virtualNo"); 
            String	  v_authInfo		  = box.getString("p_authInfo"); 
            
            if(v_issms.length()==0) v_issms = "N";
  			if(v_email_get.length()==0) v_email_get = "N";
  			
  			/*
  			 * 2008.11.09 ȸ�� ���̺� ��d8�� ���� ���?�ۼ�
  			 */
  			sql  = "INSERT INTO TZ_MEMBER  							\n";
  			sql += " 	(	  USERID								\n";
  			sql += " 		, PWD									\n";
  			sql += " 		, birth_date									\n";
  			sql += " 		, NAME									\n";
  			sql += " 		, HOMETEL								\n";
  			sql += " 		, EMAIL									\n";
  			sql += " 		, HANDPHONE								\n";
  			sql += " 	  	, LGCNT   								\n";
  			sql += " 	  	, LGFAIL   								\n";
  			sql += " 	  	, LDATE   								\n";
  			sql += " 		, ZIP_CD 								\n";
  			sql += " 		, ADDRESS								\n";
  			sql += " 		, ISMAILLING							\n";
  			sql += " 		, POST_NM								\n";
  			sql += " 		, INDATE								\n";
  			sql += " 		, LVL_NM								\n";
  			sql += " 		, JOB_NM								\n";
  			sql += " 		, ISSMS							    	\n";
  			sql += " 		, COMP							    	\n";
  			sql += " 		, POSITION_NM							\n";
  			sql += " 		, ISRETIRE							    \n";
  			
  			sql += " 		, JOB_CD							    \n";
  			sql += " 		, USER_PATH							    \n";
  			sql += " 		, HANDPHONE_NO							\n";
  			sql += " 		, ZIP_CD1							    \n";
  			sql += " 		, ADDRESS1							    \n";
  			sql += " 		, DEPT_CD							    \n";
  			sql += " 		, AGENCY_CD							    \n";
  			sql += " 		, HRDC							    	\n";
  			sql += " 		, EMP_GUBUN					    	    \n";
  			sql += " 		, virtualNo					    	    \n";
  			sql += " 		, authInfo					    	    \n";
  			sql += " 	)											\n";
  			sql += " 	VALUES 										\n";
  			sql += " 	(	 ? 										\n";
  			sql += " 		,fn_crypt('1', ?, 'knise') 										\n";
  			sql += " 		,fn_crypt('1', ?, 'knise') 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,'0' 									\n";
  			sql += " 		,'0'									\n";
  			sql += " 		,to_char(sysdate, 'YYYYMMDDHH24MISS')	\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,to_char(sysdate, 'YYYYMMDDHH24MISS')	\n";
  			//sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 		,? 										\n";
  			sql += " 	)											\n";
  			
  			
  			pstmt = connMgr.prepareStatement(sql);
  			int index = 1;
  			pstmt.setString(index++, userid);
  			pstmt.setString(index++, pwd);
  			pstmt.setString(index++, birth_date);
  			pstmt.setString(index++, name);
  			pstmt.setString(index++, hometel);
  			pstmt.setString(index++, email);
  			pstmt.setString(index++, handphone);
  			pstmt.setString(index++, post);
  			pstmt.setString(index++, address);
  			pstmt.setString(index++, v_email_get);
   			pstmt.setString(index++, box.getString("p_post_nm"));
  			//pstmt.setString(index++, company);	   //ȸ���?ȸ��d��
  			pstmt.setString(index++, lvl_nm);	       //��å
  			pstmt.setString(index++, job_nm);	       //��
  			pstmt.setString(index++, v_issms);	       //sms��ſ���?
  			pstmt.setString(index++, "1001");       //ȸ���?ȸ��d��
  			pstmt.setString(index++, v_position_nm);   //�Ҽ�
  			pstmt.setString(index++, v_isretire);   
  			
  			pstmt.setString(index++, v_job_cd);
  			pstmt.setString(index++, v_user_path);
  			pstmt.setString(index++, v_handphone_no);
  			pstmt.setString(index++, e_post);
  			pstmt.setString(index++, e_address);
  			pstmt.setString(index++, v_dept_cd);
  			pstmt.setString(index++, v_agency_cd);
  			pstmt.setString(index++, v_hrdc);
  			pstmt.setString(index++, emp_gubun);
  			pstmt.setString(index++, v_virtualNo);
  			pstmt.setString(index++, v_authInfo);
  			
  			isOk  = pstmt.executeUpdate();
            
            if ( isOk > 0 ) {    connMgr.commit();      }
            else            {    connMgr.rollback();    }
            
        } catch ( Exception ex ) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return isOk;
    }
    
    public int modifyUser(RequestBox box) throws Exception {
        DBConnectionManager connMgr  = null;
        PreparedStatement   pstmt    = null;
        
        String              sql      = "";
        int                 isOk     = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
                
            String    type            = box.getString("p_type");            
            String    userid          = box.getSession("userid");
            
          // �ʼ� �Է»���
            String    pwd             = box.getString("p_pwd");
            //String    birth_date           = box.getString("p_birth_date");
            String birth_date = box.getString("p_birth_date1") + box.getString("p_birth_date2");
            		  //pwd = StringManager.encode(pwd);
            String    post1           = box.getString("p_post1").trim();
            String    post2           = box.getString("p_post2").trim();
            String	  post			  = post1 + "-" + post2;
            String    address1        = box.getString("p_address1");
            String    address2        = box.getString("p_address2");
            String    address		  = address1 + address2;
            String    hometel        = box.getString("p_hometel").trim();
            String    handphone      = box.getString("p_handphone").trim();
            String    email1           = box.getString("p_email1");
            String    email2           = box.getString("p_email2");
            String    email           = email1.trim() + "@" + email2.trim();
            String    ismailling      = box.getString("p_ismailling");
            String    v_empgubun      = box.getString("p_empgubun");
            if(ismailling.equals("")) ismailling = "N";
            
          // ���� �Է»���
            String 		position_nm		    = box.getString("p_position_nm");
            String 		lvl_nm				= box.getString("p_lvl_nm");
            //String 		job_nm				= box.getString("p_job_nm");
            String      v_company           = box.getString("s_company");  //ȸ���?ȸ��d��
            
            String v_issms = box.getString("p_issms");                     //sms ��ſ���?
            if(v_issms.equals("")){
            	v_issms = "N";
            }
            
            String    mpost1           = box.getString("p_mpost1").trim();
            String    mpost2           = box.getString("p_mpost2").trim();
            String	  mpost			  = mpost1 + "-" + mpost2;
            String    maddress1        = box.getString("p_maddress1");
            String    maddress2        = box.getString("p_maddress2");
            String    maddress		  = maddress1 + maddress2;
            
            String    hrdc		  = box.getString("p_hrdc2"); 
            String    v_dept_cd		  = box.getString("p_dept_cd"); 
            String    v_agency_cd		  = box.getString("p_agency_cd"); 

            sql  = "UPDATE                                  			\n" +
                   "    TZ_MEMBER                           			\n" +
                   "SET                                     			\n";
              sql+="    PWD            = fn_crypt('1', ?, 'knise')          ,       			\n";
              sql+="    ZIP_CD         = ?          ,       			\n";
              sql+="    ADDRESS        = ?          ,       			\n" +
                   "    HOMETEL        = ?          ,       			\n" + 
                   "    HANDPHONE      = ?          ,       			\n" +
                   "    EMAIL          = ?          ,       			\n" +
                   "    ISMAILLING     = ?          ,       			\n" +
                   "    POSITION_NM	   = ?			,					\n" +
                   "    LVL_NM 		   = ?      	,					\n" +
                   //"    JOB_NM 		   = ?  		,					\n" +
                   "    LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'),   \n";
                   //if(box.getSession("birth_date").equals("")){
              sql+="    birth_date 		   = fn_crypt('1', '" + birth_date + "', 'knise'),					\n"; //DECODE(CERT,'Y','"+Encrypt.com_Encode(birth_date)+"','"+birth_date+"')  		,					\n";
                   //}	   
              sql+="    ISSMS 		   = ?  		,					\n" +
                   "    COMP 		   = ?  		,					\n" +
                   "    emp_gubun	   = ?  		,					\n" +
                   "    zip_cd1	   = ?  		,					\n" +
                   "    address1	   = ?  		,					\n" +
                   "    hrdc	   = ?  		,					\n" +
                   "    user_path 	   = ?  	,						\n" +
                   "    dept_cd 	   = ?  	,						\n" +
                   "    agency_cd 	   = ?  							\n" +
                   " WHERE                                              \n" +
                   "    USERID = ?                                      \n" ; 
            
            pstmt = connMgr.prepareStatement(sql);
  
            int params = 1;
            pstmt.setString(params++, pwd);
            pstmt.setString(params++, post);
            pstmt.setString(params++, address);
            pstmt.setString(params++, hometel);
            pstmt.setString(params++, handphone);
            pstmt.setString(params++, email);
            pstmt.setString(params++, ismailling);
            pstmt.setString(params++, position_nm);
            pstmt.setString(params++, lvl_nm);
            //pstmt.setString(params++, job_nm);
            //if(box.getSession("birth_date").equals("")){
            //pstmt.setString(params++, birth_date);
            //}
            pstmt.setString(params++, v_issms);
            pstmt.setString(params++, v_company);
            pstmt.setString(params++, v_empgubun);
            
            pstmt.setString(params++, mpost);
            pstmt.setString(params++, maddress);
            pstmt.setString(params++, hrdc);
            pstmt.setString(params++, box.getString("p_user_path"));
            pstmt.setString(params++, v_dept_cd);
            pstmt.setString(params++, v_agency_cd);
            pstmt.setString(params++, userid);
            
            isOk  = pstmt.executeUpdate();
            
            if ( isOk > 0 ) {    connMgr.commit();      }
            else            {    connMgr.rollback();    }
            
        } catch ( Exception ex ) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return isOk;        
    }
    
    public int getNickName(RequestBox box) throws Exception { 
        DBConnectionManager connMgr  = null;
        PreparedStatement   pstmt    = null;
        ListSet             ls       = null;
        String              sql      = "";
        String              v_nickname  = box.getString("p_nickname");
        
        int                 returnValue = 0;
        String v_edit = box.getString("p_edit");
        String s_userid = box.getSession("userid");
        try { 
              connMgr = new DBConnectionManager();
              
              sql = "SELECT                                \n" +
                    "    nickname                          \n" +
                    "FROM                                  \n" +
                    "    tz_member                         \n" +
                    "WHERE                                 \n" +
                    "    nickname = ?                      \n";
                    if("Y".equals(v_edit) && s_userid !=null) {
                        sql+="    and nickname != ( select nickname from tz_member where userid= ? )";
                    }
                    sql+="union                                 \n" +
                    "SELECT                                \n" +
                    "    nickname                          \n" +
                    "FROM                                  \n" +
                    "    tz_member_type23                  \n" +
                    "WHERE                                 \n" +
                    "    nickname = ?                      \n" ;
                    
              pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

              int params = 1;
              pstmt.setString(params++, v_nickname);
              if("Y".equals(v_edit) && s_userid !=null) {
                  pstmt.setString(params++, s_userid);
              }
              pstmt.setString(params++, v_nickname);
           
              ls = new ListSet(pstmt);  
        
              if ( ls.next() ) { 
                  returnValue = 1;
              }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt   != null ) { try { pstmt.close();            } catch ( Exception e1 )  { } }
            if ( ls      != null ) { try { ls.close();               } catch ( Exception e )   { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
    
        return returnValue;
    }
    
    public int getExistID(RequestBox box) throws Exception { 
        DBConnectionManager connMgr  = null;
        PreparedStatement   pstmt    = null;
        ListSet             ls       = null;
        String              sql      = "";
        String              v_userid  = box.getString("p_userid");
        
        int                 returnValue = 0;
    
        try { 
              connMgr = new DBConnectionManager();
              
              sql = "SELECT                                \n" +
                    "    userid                            \n" +
                    "FROM                                  \n" +
                    "    tz_member                         \n" +
                    "WHERE                                 \n" +
                    //"    userid = ?                        \n" +
                    //"union                                 \n" +
                    //"SELECT                                \n" +
                    //"    userid                            \n" +
                    //"FROM                                  \n" +
                    //"    tz_member_type23                  \n" +
                    //"WHERE                                 \n" +
                    "    userid = ?                        \n" ;
              
              pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

              pstmt.setString(1, v_userid);
              //pstmt.setString(2, v_userid);
           
              ls = new ListSet(pstmt);  
        
              if ( ls.next() ) { 
                  returnValue = 1;
              }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt   != null ) { try { pstmt.close();            } catch ( Exception e1 )  { } }
            if ( ls      != null ) { try { ls.close();               } catch ( Exception e )   { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
    
        return returnValue;
    }
    
    public DataBox selectMember(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String s_userid = box.getSession("userid");
        try { 
            connMgr = new DBConnectionManager();

/*
            sql  = "select USERID, TYPE, USEUSERID, INDATE, LGCNT, PWD, NAME, NICKNAME, birth_date, BIRTHDAY,            \n"+
                   "    POST1, POST2, PWDHINTITEM, PWDHINTANSWER, ISMAILLING, ADDRESS1, ADDRESS2,                   \n"+
                   "    COMP, HOMETEL, HANDPHONE, COMPTEL, TEL_LINE, EMAIL, ENTRYTYPE, ENTRYPATH,                   \n"+
                   "    COMPNM, JIKUP, ACHIEVEMENT, ISWEDDING, NATIONALITY, GENDER, LICENSE_CHOICE,                 \n"+
                   "    LICENSE_LIST, PARENTNAME, PARENTbirth_date, PARENTHANDPHONE, PARENTEMAIL, ENTRYTYPE, ISSMS       \n"+
                   "from TZ_MEMBER                                                                                  \n"+
                   "where userid= " + SQLString.Format(s_userid) + "                                                \n";
*/            
            sql  = " select USERID, INDATE, LGCNT, fn_crypt('2', pwd, 'knise') PWD, NAME, fn_crypt('2', birth_date, 'knise') birth_date, zip_cd ,ISMAILLING, ADDRESS, emp_gubun, user_path, zip_cd1, ADDRESS1, HRDC,CERT,   	\n ";
            //sql += "		COMP, HOMETEL, HANDPHONE, EMAIL, position_nm, lvl_nm, job_nm, ISSMS         \n ";		
            sql += "		COMP, HOMETEL, HANDPHONE, EMAIL, position_nm, lvl_nm, job_nm, ISSMS, nvl(isivoline,'N') isivoline, \n ";		
            sql += "		dept_cd, agency_cd ";
            sql += " from TZ_MEMBER 																    \n ";		
            sql += " where userid= " + SQLString.Format(s_userid) + "                                   \n ";	
            ls = connMgr.executeQuery(sql);
            
            System.out.println("sql = " + sql);


            for ( int i = 0; ls.next(); i++ ) { 
                dbox = ls.getDataBox();
            }
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
    
    public int updateRetire(RequestBox box) throws Exception {
        DBConnectionManager connMgr  = null;
        PreparedStatement   pstmt    = null;
        
        String              sql      = "";
        int                 isOk     = 0;
        String v_retire_type = box.getString("p_retire_type");
        String v_retire_reason = "";
        if("F".equals(v_retire_type))
            v_retire_reason = box.getString("p_retire_reason");
        
//        String v_pwd = StringManager.BASE64Encode(box.getString("p_pwd"));
        String v_pwd = box.getString("p_pwd");
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
                
            String    userid          = box.getSession("userid");
            sql  = "UPDATE                                                                                              \n" +
                   "    TZ_MEMBER                                                                                       \n" +
                   "SET                                                                                                 \n" +
                   "    ISRETIRE = ?, RETIRE_TYPE = ?, RETIRE_REASON = ?, LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n" +
                   ",   RETIRE_DATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')                                              \n" +
                   "WHERE                                                                                               \n" +
                   "    USERID = ?                                                                                      \n" +
                   "AND PWD = fn_crypt('1', ?, 'knise')                                                                                        \n";
            
            pstmt = connMgr.prepareStatement(sql);
  
            int params = 1;
            pstmt.setString(params++, "Y");
            pstmt.setString(params++, v_retire_type);
            pstmt.setString(params++, "[��/ �Է¾��� Ż��] ");
            pstmt.setString(params++, userid);
            pstmt.setString(params++, v_pwd);
            
            isOk  = pstmt.executeUpdate();
            
            if ( isOk > 0 ) {    connMgr.commit();      }
            else            {    connMgr.rollback();    }
            
        } catch ( Exception ex ) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return isOk;        
    }
    
    
    public int agree14Member(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // System.out.println; Mehtod���� ����ϴ�?�� ǥ���ϴ� ����
        
        String              v_userid        = box.getString("p_userid"   );
        String              v_useuserid     = box.getString("p_useuserid");
        int                 v_agree_flag    = 0;              
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" SELECT   NVL(MAX( Agree_Flag ), 0) Agree_Flag                            \n")
                 .append(" FROM (                                                                   \n")
                 .append("          SELECT  1 Table_Name                                            \n")              
                 .append("              ,   DECODE(UseUserId, 'N' , 3                               \n")
                 .append("                                  ,       1                               \n")
                 .append("                        )         Agree_Flag                              \n")                      
                 .append("              ,   Type                                                    \n")
                 .append("          FROM    Tz_Member_Type23                                        \n")
                 .append("          WHERE   UserId         = " + SQLString.Format(v_userid) + "     \n")
                 .append("          AND     Type           = '2'                                    \n")
                 .append("          UNION ALL                                                       \n")
                 .append("          SELECT  2 Table_Name                                            \n")
                 .append("              ,   DECODE(UseUserId, 'Y', 4                                \n")
                 .append("                                  , 'N', 3                                \n")
                 .append("                                  , 'O', 2                                \n")
                 .append("                                  ,      2                                \n")
                 .append("                        )         Agree_Flag                              \n")  
                 .append("              ,  Type                                                     \n")
                 .append("          FROM   Tz_Member                                                \n")
                 .append("          WHERE  UserId          = " + SQLString.Format(v_userid) + "     \n")   
                 .append("      )                                                                   \n");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() )
                v_agree_flag    = ls.getInt("agree_flag");
            
            /*
            4 : �θ��� ���Ƿ� ó���� ���?
            3 : �θ� ���� ����8�� ó���� ���?
            2 : �Ϲ�ȸ���� ���?
            0 : �ƿ� x������ �ʴ� ���?Member ���̺�)
            */             
            
            // �̹� �θ𿡰� ���Ƿ� ó���� ���?
            if ( v_agree_flag == 4 )
                return -4;
            else if ( v_agree_flag == 3 ) // �̹� �θ𿡰� ���Ǿ���8�� ó���� ���?
                return -3;
            else if ( v_agree_flag == 2 )
                return -2;
            else if ( v_agree_flag == 0 )
                return -1;
            
            sbSQL.setLength(0);
            
            if ( v_useuserid.equals("Y") ) {    
                sbSQL.append(" INSERT INTO Tz_Member                                        \n")
                     .append(" (                                                            \n")
                     .append("         Userid                                               \n")
                     .append("     ,   Grcode                                               \n")
                     .append("     ,   Type                                                 \n")
                     .append("     ,   Useuserid                                            \n")
                     .append("     ,   Isgyeonggi                                           \n")
                     .append("     ,   Indate                                               \n")
                     .append("     ,   Lgcnt                                                \n")
                     .append("     ,   Lglast                                               \n")
                     .append("     ,   Lgip                                                 \n")
                     .append("     ,   Lgfail                                               \n")
                     .append("     ,   Ldate                                                \n")
                     .append("     ,   Pwd                                                  \n")
                     .append("     ,   Name                                                 \n")
                     .append("     ,   birth_date                                                \n")
                     .append("     ,   Birthday                                             \n")
                     .append("     ,   Post1                                                \n")
                     .append("     ,   Post2                                                \n")
                     .append("     ,   Address1                                             \n")
                     .append("     ,   Address2                                             \n")
                     .append("     ,   Comp                                                 \n")
                     .append("     ,   Hometel                                              \n")
                     .append("     ,   Handphone                                            \n")
                     .append("     ,   Comptel                                              \n")
                     .append("     ,   Tel_Line                                             \n")
                     .append("     ,   Email                                                \n")
                     .append("     ,   Entrytype                                            \n")
                     .append("     ,   Jikup                                                \n")
                     .append("     ,   Achievement                                          \n")
                     .append("     ,   Iswedding                                            \n")
                     .append("     ,   License_Choice                                       \n")
                     .append("     ,   License_List                                         \n")
                     .append("     ,   Parentname                                           \n")
                     .append("     ,   Parentbirth_date                                          \n")
                     .append("     ,   Parenthandphone                                      \n")
                     .append("     ,   Parentemail                                          \n")
                     .append("     ,   Gender                                               \n")
                     .append("     ,   Nationality                                          \n")
                     .append("     ,   Isretire                                             \n")
                     .append("     ,   Lgfirst                                              \n")
                     .append("     ,   Validation                                           \n")
                     .append("     ,   Ldatepayment                                         \n")
                     .append("     ,   Nickname                                             \n")
                     .append("     ,   Entrypath                                            \n")
                     .append("     ,   Compnm                                               \n")
                     .append("     ,   Pwdhintitem                                          \n")
                     .append("     ,   Pwdhintanswer                                        \n")
                     .append("     ,   Ismailling                                           \n")
                     .append("     ,   Issms                                                \n")
                     .append("     ,   Member_Info_Check                                    \n")
                     .append(" )                                                            \n")
                     .append(" SELECT  Userid                                               \n")
                     .append("     ,   Grcode                                               \n")
                     .append("     ,   Type                                                 \n")
                     .append("     ,   " + SQLString.Format(v_useuserid) + "                \n")
                     .append("     ,   Isgyeonggi                                           \n")
                     .append("     ,   Indate                                               \n")
                     .append("     ,   Lgcnt                                                \n")
                     .append("     ,   Lglast                                               \n")
                     .append("     ,   Lgip                                                 \n")
                     .append("     ,   Lgfail                                               \n")
                     .append("     ,   Ldate                                                \n")
                     .append("     ,   Pwd                                                  \n")
                     .append("     ,   Name                                                 \n")
                     .append("     ,   birth_date                                                \n")
                     .append("     ,   Birthday                                             \n")
                     .append("     ,   Post1                                                \n")
                     .append("     ,   Post2                                                \n")
                     .append("     ,   Address1                                             \n")
                     .append("     ,   Address2                                             \n")
                     .append("     ,   Comp                                                 \n")
                     .append("     ,   Hometel                                              \n")
                     .append("     ,   Handphone                                            \n")
                     .append("     ,   Comptel                                              \n")
                     .append("     ,   Tel_Line                                             \n")
                     .append("     ,   Email                                                \n")
                     .append("     ,   Entrytype                                            \n")
                     .append("     ,   Jikup                                                \n")
                     .append("     ,   Achievement                                          \n")
                     .append("     ,   Iswedding                                            \n")
                     .append("     ,   License_Choice                                       \n")
                     .append("     ,   License_List                                         \n")
                     .append("     ,   Parentname                                           \n")
                     .append("     ,   Parentbirth_date                                          \n")
                     .append("     ,   Parenthandphone                                      \n")
                     .append("     ,   Parentemail                                          \n")
                     .append("     ,   Gender                                               \n")
                     .append("     ,   Nationality                                          \n")
                     .append("     ,   Isretire                                             \n")
                     .append("     ,   Lgfirst                                              \n")
                     .append("     ,   Validation                                           \n")
                     .append("     ,   Ldatepayment                                         \n")
                     .append("     ,   Nickname                                             \n")
                     .append("     ,   Entrypath                                            \n")
                     .append("     ,   Compnm                                               \n")
                     .append("     ,   Pwdhintitem                                          \n")
                     .append("     ,   Pwdhintanswer                                        \n")
                     .append("     ,   Ismailling                                           \n")
                     .append("     ,   Issms                                                \n")
                     .append("     ,   ''                                                   \n")
                     .append(" FROM    Tz_Member_Type23                                     \n")
                     .append(" WHERE   UserId       = " + SQLString.Format(v_userid   ) + " \n");
            } else {                     
                sbSQL.append(" UPDATE  Tz_Member_Type23 SET                                 \n")
                     .append("         UseUserId    = " + SQLString.Format(v_useuserid) + " \n")
                     .append("     ,   LDate        = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n")
                     .append(" WHERE   UserId       = " + SQLString.Format(v_userid   ) + " \n");
            }
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());
  
            isOk  = pstmt.executeUpdate();
            
            pstmt.close();
            
            // �θ��� ���ǰ� ó���� ���?
            if ( v_useuserid.equals("Y") ) {
                sbSQL.setLength(0);
                
                sbSQL.append(" DELETE FROM Tz_Member_Type23                                 \n")
                     .append(" WHERE   UserId       = " + SQLString.Format(v_userid   ) + " \n");
                
                pstmt = connMgr.prepareStatement(sbSQL.toString());
                
                isOk  = pstmt.executeUpdate();
                
                isOk = 1;
            } else { //�θ��� ���� ����8�� ó���� ���?
                isOk = 0;
            }
            
            connMgr.commit();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            if ( v_useuserid.equals("Y") )
                isOk    = -5;
            else
                isOk    = -6;

            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            if ( v_useuserid.equals("Y") )
                isOk    = -5;
            else
                isOk    = -6;

            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
        }
        
        return isOk;
    }
    
    public int agreeForeigner(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // System.out.println; Mehtod���� ����ϴ�?�� ǥ���ϴ� ����
        
        String              v_userid        = box.getString("p_userid"   );
        String              v_useuserid     = box.getString("p_useuserid");
        int                 v_agree_flag    = 0;              
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" SELECT   NVL(MAX( Agree_Flag ), 0) Agree_Flag                            \n")
                 .append(" FROM (                                                                   \n")
                 .append("          SELECT  1 Table_Name                                            \n")              
                 .append("              ,   DECODE(UseUserId, 'N' , 3                               \n")
                 .append("                                  ,       1                               \n")
                 .append("                        )         Agree_Flag                              \n")                      
                 .append("              ,   Type                                                    \n")
                 .append("          FROM    Tz_Member_Type23                                        \n")
                 .append("          WHERE   UserId         = " + SQLString.Format(v_userid) + "     \n")
                 .append("          AND     Type           = '3'                                    \n")
                 .append("          UNION ALL                                                       \n")
                 .append("          SELECT  2 Table_Name                                            \n")
                 .append("              ,   DECODE(UseUserId, 'Y', 4                                \n")
                 .append("                                  , 'N', 3                                \n")
                 .append("                                  , 'O', 2                                \n")
                 .append("                                  ,      2                                \n")
                 .append("                        )         Agree_Flag                              \n")  
                 .append("              ,  Type                                                     \n")
                 .append("          FROM   Tz_Member                                                \n")
                 .append("          WHERE  UserId          = " + SQLString.Format(v_userid) + "     \n")   
                 .append("      )                                                                   \n");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() )
                v_agree_flag    = ls.getInt("agree_flag");
            
            /*
            4 : ���Ƿ� ó���� ���?
            3 : ���� ����8�� ó���� ���?
            2 : �Ϲ�ȸ���� ���?
            0 : �ƿ� x������ �ʴ� ���?Member ���̺�)
            */             
            
            // �̹� ���Ƿ� ó���� ���?
            if ( v_agree_flag == 4 )
                return -4;
            else if ( v_agree_flag == 3 ) // �̹� ���Ǿ���8�� ó���� ���?
                return -3;
            else if ( v_agree_flag == 2 )
                return -2;
            else if ( v_agree_flag == 0 )
                return -1;
            
            sbSQL.setLength(0);
            
            if ( v_useuserid.equals("Y") ) {    
                sbSQL.append(" INSERT INTO Tz_Member                                        \n")
                     .append(" (                                                            \n")
                     .append("         Userid                                               \n")
                     .append("     ,   Grcode                                               \n")
                     .append("     ,   Type                                                 \n")
                     .append("     ,   Useuserid                                            \n")
                     .append("     ,   Isgyeonggi                                           \n")
                     .append("     ,   Indate                                               \n")
                     .append("     ,   Lgcnt                                                \n")
                     .append("     ,   Lglast                                               \n")
                     .append("     ,   Lgip                                                 \n")
                     .append("     ,   Lgfail                                               \n")
                     .append("     ,   Ldate                                                \n")
                     .append("     ,   Pwd                                                  \n")
                     .append("     ,   Name                                                 \n")
                     .append("     ,   birth_date                                                \n")
                     .append("     ,   Birthday                                             \n")
                     .append("     ,   Post1                                                \n")
                     .append("     ,   Post2                                                \n")
                     .append("     ,   Address1                                             \n")
                     .append("     ,   Address2                                             \n")
                     .append("     ,   Comp                                                 \n")
                     .append("     ,   Hometel                                              \n")
                     .append("     ,   Handphone                                            \n")
                     .append("     ,   Comptel                                              \n")
                     .append("     ,   Tel_Line                                             \n")
                     .append("     ,   Email                                                \n")
                     .append("     ,   Entrytype                                            \n")
                     .append("     ,   Jikup                                                \n")
                     .append("     ,   Achievement                                          \n")
                     .append("     ,   Iswedding                                            \n")
                     .append("     ,   License_Choice                                       \n")
                     .append("     ,   License_List                                         \n")
                     .append("     ,   Parentname                                           \n")
                     .append("     ,   Parentbirth_date                                          \n")
                     .append("     ,   Parenthandphone                                      \n")
                     .append("     ,   Parentemail                                          \n")
                     .append("     ,   Gender                                               \n")
                     .append("     ,   Nationality                                          \n")
                     .append("     ,   Isretire                                             \n")
                     .append("     ,   Lgfirst                                              \n")
                     .append("     ,   Validation                                           \n")
                     .append("     ,   Ldatepayment                                         \n")
                     .append("     ,   Nickname                                             \n")
                     .append("     ,   Entrypath                                            \n")
                     .append("     ,   Compnm                                               \n")
                     .append("     ,   Pwdhintitem                                          \n")
                     .append("     ,   Pwdhintanswer                                        \n")
                     .append("     ,   Ismailling                                           \n")
                     .append("     ,   Issms                                                \n")
                     .append("     ,   Member_Info_Check                                    \n")
                     .append(" )                                                            \n")
                     .append(" SELECT  Userid                                               \n")
                     .append("     ,   Grcode                                               \n")
                     .append("     ,   Type                                                 \n")
                     .append("     ,   " + SQLString.Format(v_useuserid) + "                \n")
                     .append("     ,   Isgyeonggi                                           \n")
                     .append("     ,   Indate                                               \n")
                     .append("     ,   Lgcnt                                                \n")
                     .append("     ,   Lglast                                               \n")
                     .append("     ,   Lgip                                                 \n")
                     .append("     ,   Lgfail                                               \n")
                     .append("     ,   Ldate                                                \n")
                     .append("     ,   Pwd                                                  \n")
                     .append("     ,   Name                                                 \n")
                     .append("     ,   birth_date                                                \n")
                     .append("     ,   Birthday                                             \n")
                     .append("     ,   Post1                                                \n")
                     .append("     ,   Post2                                                \n")
                     .append("     ,   Address1                                             \n")
                     .append("     ,   Address2                                             \n")
                     .append("     ,   Comp                                                 \n")
                     .append("     ,   Hometel                                              \n")
                     .append("     ,   Handphone                                            \n")
                     .append("     ,   Comptel                                              \n")
                     .append("     ,   Tel_Line                                             \n")
                     .append("     ,   Email                                                \n")
                     .append("     ,   Entrytype                                            \n")
                     .append("     ,   Jikup                                                \n")
                     .append("     ,   Achievement                                          \n")
                     .append("     ,   Iswedding                                            \n")
                     .append("     ,   License_Choice                                       \n")
                     .append("     ,   License_List                                         \n")
                     .append("     ,   Parentname                                           \n")
                     .append("     ,   Parentbirth_date                                          \n")
                     .append("     ,   Parenthandphone                                      \n")
                     .append("     ,   Parentemail                                          \n")
                     .append("     ,   Gender                                               \n")
                     .append("     ,   Nationality                                          \n")
                     .append("     ,   Isretire                                             \n")
                     .append("     ,   Lgfirst                                              \n")
                     .append("     ,   Validation                                           \n")
                     .append("     ,   Ldatepayment                                         \n")
                     .append("     ,   Nickname                                             \n")
                     .append("     ,   Entrypath                                            \n")
                     .append("     ,   Compnm                                               \n")
                     .append("     ,   Pwdhintitem                                          \n")
                     .append("     ,   Pwdhintanswer                                        \n")
                     .append("     ,   Ismailling                                           \n")
                     .append("     ,   Issms                                                \n")
                     .append("     ,   ''                                                   \n")
                     .append(" FROM    Tz_Member_Type23                                     \n")
                     .append(" WHERE   UserId       = " + SQLString.Format(v_userid   ) + " \n");
            } else {                     
                sbSQL.append(" UPDATE  Tz_Member_Type23 SET                                 \n")
                     .append("         UseUserId    = " + SQLString.Format(v_useuserid) + " \n")
                     .append("     ,   LDate        = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n")
                     .append(" WHERE   UserId       = " + SQLString.Format(v_userid   ) + " \n");
            }
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());
  
            isOk  = pstmt.executeUpdate();
            
            pstmt.close();
            
            // ���ǰ� ó���� ���?
            if ( v_useuserid.equals("Y") ) {
                sbSQL.setLength(0);
                
                sbSQL.append(" DELETE FROM Tz_Member_Type23                                 \n")
                     .append(" WHERE   UserId       = " + SQLString.Format(v_userid   ) + " \n");
                
                pstmt = connMgr.prepareStatement(sbSQL.toString());
                
                isOk  = pstmt.executeUpdate();
                
                isOk = 1;
            } else { //���� ����8�� ó���� ���?
                isOk = 0;
            }
            
            connMgr.commit();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            if ( v_useuserid.equals("Y") )
                isOk    = -5;
            else
                isOk    = -6;

            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            if ( v_useuserid.equals("Y") )
                isOk    = -5;
            else
                isOk    = -6;

            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
        }
        
        return isOk;
    }
    
    
    public DataBox selectEtcMemberInfo(String v_userid) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();

             
            sbSQL.append(" SELECT  tm.Userid                                                                    \n")
                 .append("     ,   tm.name                                                                      \n")
                 .append("     ,   tm.Indate                                               \n")
                 .append("     ,   tm.Lgcnt                                                \n")
                 .append("     ,   tm.Lglast                                               \n")
                 //.append("  ,   tm.jikup                                                                        \n")
                 //.append("  ,   tm.specials                                                                     \n")
                 //.append("  ,   tm.achievement                                                                  \n")
                 .append("  ,   tm.lgfail                                                                       \n")
                 //.append("  ,   ta.codenm                 achievement_name                                      \n")
                 //.append("  ,   tj.codenm                 jikup_name                                            \n")
                 //.append("  ,   tt.codenm                 entrytype_name                                        \n")
                 //.append("  ,   tp.codenm                 entrypath_name                                        \n")
                 .append("  ,   NVL(isMailling, 'Y')      isMailling                                            \n")
                 .append("  ,   NVL(issms, 'Y')      issms                                            \n")
                 //.append("  ,   NVL(isSms, 'Y')           isSms                                                 \n")
                 //.append("  ,   entrytype                                                                       \n")
                 //.append("  ,   entrypath                                                                       \n")
                 //.append("  ,   tm.License_choice                                                               \n")
                 //.append("  ,   DECODE(INSTR(tm.License_choice, '01'), 0, '', 'IT'  ) License_choice1           \n")
                 //.append("  ,   DECODE(INSTR(tm.License_choice, '02'), 0, '', '�濵') License_choice2           \n")
                 //.append("  ,   DECODE(INSTR(tm.License_choice, '99'), 0, '', '��Ÿ') License_choice3           \n")
                 //.append("  ,   tm.License_list                                                                 \n")
                 //.append("  ,   tm.IsWedding                                                                    \n")
                 //.append("  ,   DECODE(tm.IsWedding, 'Y', '��ȥ', '��ȥ')   IsWedding_Name                       \n")
                 .append(" from    TZ_MEMBER   tm                                                               \n")                  
                 //.append("     ,    (                                                                           \n")
                 //.append("         SELECT  *                                                                    \n")
                 //.append("         FROM    Tz_Code                                                              \n")
                 //.append("         WHERE   gubun = '0063'                                                       \n")
                 //.append("         )           ta                                                               \n")
                 //.append("     ,    (                                                                           \n")
                 //.append("         SELECT  *                                                                    \n")
                 //.append("         FROM    Tz_Code                                                              \n")
                 //.append("         WHERE   gubun = '0064'                                                       \n")
                 //.append("         )           tj                                                               \n")
                 //.append("     ,    (                                                                           \n")
                 //.append("         SELECT  *                                                                    \n")
                 //.append("         FROM    Tz_Code                                                              \n")
                 //.append("         WHERE   gubun = '0065'                                                       \n")
                 //.append("         )           tt                                                               \n")
                 //.append("    ,     (                                                                           \n")
                 //.append("         SELECT  *                                                                    \n")
                 //.append("         FROM    Tz_Code                                                              \n")
                 //.append("         WHERE   gubun = '0066'                                                       \n")
                 //.append("         )           tp                                                               \n")
                 .append(" WHERE   tm.userid = " + SQLString.Format(v_userid) + "                               \n");
                 //.append(" AND     tm.Achievement  = ta.code(+)                                                 \n")
                 //.append(" AND     tm.Jikup        = tj.code(+)                                                 \n")
                 //.append(" AND     tm.entrytype    = tt.code(+)                                                 \n")
                 //.append(" AND     tm.entrypath    = tp.code(+)                                                 \n");
                 
            System.out.println("SQL========" + sbSQL.toString());
            
            ls = connMgr.executeQuery(sbSQL.toString());


            for ( int i = 0; ls.next(); i++ ) { 
                dbox = ls.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }   

    /**
     * ���̺��� ���� ��
     * @param box
     * @return
     * @throws Exception
     */
    public String setIvolineY(RequestBox box) throws Exception {
        DBConnectionManager connMgr  = null;
        PreparedStatement   pstmt    = null;
        StringBuffer        sbSQL    = null;
        int                 isOk     = 0;
        String result = "N";
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            String    userid          = box.getSession("userid");
            sbSQL = new StringBuffer();
            
            sbSQL.append(" update TZ_MEMBER \n ");
            sbSQL.append("    set isivoline = 'Y' \n ");
            sbSQL.append("  where userid = ? \n ");
  			
  			pstmt = connMgr.prepareStatement(sbSQL.toString());
  			
  			int index = 1;
  			
  			pstmt.setString(index++, userid);
  			isOk  = pstmt.executeUpdate();
            
            if ( isOk > 0 ) {    result = "Y"; connMgr.commit();      }
            else            {    connMgr.rollback();    }
            
        } catch ( Exception ex ) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return result;
    }    
    
    /**
     * ���̺��� ���ǿ��� vȸ 
     * @param box
     * @return
     * @throws Exception
     */
    public DataBox selectIvolineInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        DataBox             dbox    = null;
        String result = "";
        try { 
            connMgr = new DBConnectionManager();

             
            sbSQL.append(" select  nvl(isivoline,'N') isivoline, comp, userid, name, zip_cd, address, hometel, handphone, email, position_nm \n")
                 .append("   from  tz_member \n")
                 .append("   where userid = "+StringManager.makeSQL(box.getSession("userid"))+ " \n ");
                 
            ls = connMgr.executeQuery(sbSQL.toString());
            
            for ( int i = 0; ls.next(); i++ ) { 
                dbox = ls.getDataBox();
            }
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
}