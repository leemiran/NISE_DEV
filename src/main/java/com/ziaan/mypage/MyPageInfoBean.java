// **********************************************************
// 1. 제      목:
// 2. 프로그램명: MyPageInfoBean.java
// 3. 개      요: 마이페이지 - 나의 이미지 변경 
// 4. 환      경: JDK 1.4
// 5. 버      젼: 0.1
// 6. 작      성: 조용준 2006.07.11
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.mypage;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author 김민수
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class MyPageInfoBean { 
    public static String SINGLE_CLASS = "1";
    public static String PLURAL_CLASS = "2";

    public static String CLASSNM = "클래스";
    public static String SINGLE_CLASS_CODE = "0001";

    private     ConfigSet   config;
    private     int         row;

            private	static final String	FILE_TYPE =	"p_photo";			// 		파일업로드되는 tag name
	    private	static final int FILE_LIMIT	= 5;	    
    
    public MyPageInfoBean() {
        try { 
            config          = new ConfigSet();
            row             = Integer.parseInt(config.getProperty("page.bulletin.row") ); // 이 모듈의 페이지당 row 수를 셋팅한다
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    이미지 리스트 가져오기
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectMemberImgList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        DataBox dbox        = null;

        
        String  v_user_id   = box.getSession("userid");        

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1  =
                "\n	select userid, seq, realfile, savefile, luserid, ldate, ismain 		" +
                "\n	from tz_memberfile							" +
                "\n	where userid		= ':userid'					" +                
                "\n	order by ismain desc, seq						";
                                

            sql1 = sql1.replaceAll( ":userid", v_user_id );

           ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                list1.add(dbox);
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
    상담 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectBoardList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        DataBox dbox        = null;

        
        String  v_user_id   = box.getSession("userid");        

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1  =
                "\n	select *								" +
                "\n	from (									" +
                "\n		select rownum rnum						" +
                "\n		from tz_member							" +
                "\n		where rownum<=3)A,						" +
                "\n		(								" +
                "\n		select tabseq, count(*) cnt, sum(decode(hasanswer,'Y',1,0)) ans	" +
                "\n		from tz_center_board						" +
                "\n		where userid="+StringManager.makeSQL(v_user_id)+
                "\n		group by tabseq							" +
                "\n		union all							" +
                "\n		select 3, count(*) , 0 						" +
                "\n		from tz_center_resboard						" +
                "\n		where userid="+StringManager.makeSQL(v_user_id)+
                "\n		) B								" +
                "\n	where A.rnum=b.tabseq(+)						" +
                "\n	order by a.rnum 							";
                                

            sql1 = sql1.replaceAll( ":userid", v_user_id );

           ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                list1.add(dbox);
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
    * 새로운 자료실 내용 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insert(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet rs1   = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2  = null;
        String sql    = "";
        String sql1   = "";
        String sql2   = "";
        
        int isOk1     = 1;
        int isOk2 = 1;
        int isOk3 = 1;
        
        int v_seq     = 0;  // 등록될 게시판 일련번호... max값.
        
        String s_userid     = box.getSession("userid");
        String s_userNm     = box.getSession("name");
        String v_ismain	    = box.getString("p_ismain");
		
		String [] v_realFileName = new String [FILE_LIMIT];
		String [] v_newFileName	= new String [FILE_LIMIT];

		for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
			v_realFileName	[i] =	box.getRealFileName(FILE_TYPE + (i +1));
			v_newFileName	[i] =	box.getNewFileName(FILE_TYPE + (i +1));
		}		

		
		            
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);		                
            
            //----------------------   게시판 번호 가져온다 ----------------------------
            sql = "select nvl(max(seq), 0) from TZ_MEMBERFILE where userid = " + StringManager.makeSQL(s_userid);
            rs1 = connMgr.executeQuery(sql);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            rs1.close();
            
            // ----------------------   게시판 update   --------------------------
            sql1 =  "	update TZ_MEMBERFILE 					 \n" +
            	    "	set ismain = 'N' 					\n" +
            	    "   where userid = ?                                        \n";

            pstmt1 = connMgr.prepareStatement(sql1);
            
            // 수정
            pstmt1.setString(1, s_userid);    
            if (v_ismain.equals("Y")){
            	isOk1 = pstmt1.executeUpdate();
            } else {
            	isOk1 = 1;
            }

            // ----------------------   게시판 table 에 입력  --------------------------
            sql2 =  "insert into TZ_MEMBERFILE (userid, seq, realfile, savefile, \n" +
            	    "		luserid, ldate, ismain )			\n" +
            	    "       values						\n" +
                    "      (                                                    \n" +               
                    "       ?,                                                  \n" +   //userid
                    "       ?,                                                  \n" +   //seq
                    "       ?,		                                        \n" +   //realfile 
                    "       ?,                                                  \n" +   //savefile
                    "       ?,                                                  \n" +   //luserid
                    "       to_char(sysdate, 'yyyymmddhh24miss'),               \n" +   //ldate
                    "       ?                                                   \n" +   //ismain                    
                    "      )                                                    \n";

            pstmt2 = connMgr.prepareStatement(sql2);
            
            // 수정
            pstmt2.setString(1, s_userid);    
            pstmt2.setInt(2, v_seq);    
            pstmt2.setString(3, v_realFileName[0]);
            pstmt2.setString(4, v_newFileName[0]);
            pstmt2.setString(5, s_userid);
            pstmt2.setString(6, v_ismain);                        
					
					            
            isOk2 = pstmt2.executeUpdate();            
            
            if ( isOk1 > 0 && isOk2 > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        }

        catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }    
    

/**
    * 나의 각오 내용 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertPrepared(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet rs1   = null;
        PreparedStatement pstmt1  = null;
        
        String sql    = "";
        
        int isOk1     = 1;
        
        String s_userid     	= box.getSession("userid");
        String v_my_prepared	= box.getString("p_my_prepared");
		            
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);		                            
            
            // ----------------------   나의 각오 update   --------------------------
            sql =  "	update TZ_MEMBER 					 \n" +
            	    "	set my_prepared = ? 					\n" +
            	    "   where userid = ?                                        \n";

            pstmt1 = connMgr.prepareStatement(sql);
            
            // 수정
            pstmt1.setString(1, v_my_prepared);    
            pstmt1.setString(2, s_userid);    
            
            isOk1 = pstmt1.executeUpdate();
            
            if ( isOk1 > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        }

        catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sq = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }          
    
/**
    * 사용자 메인 이미지 해제
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int release(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet rs1   = null;
        PreparedStatement pstmt1  = null;
                
        String sql1   = "";
        
        int isOk1     = 1;
        
        int v_seq     = box.getInt("p_seq");  // 등록될 게시판 일련번호... max값.
        
        String s_userid     = box.getSession("userid");
        String s_userNm     = box.getSession("name");        
		            
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);		                            
            
            // ----------------------   게시판 update   --------------------------
            sql1 =  "	update TZ_MEMBERFILE 					 \n" +
            	    "	set ismain = 'N' 					\n" +
            	    "   where userid = ?                                        \n";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, s_userid);
            
            isOk1 = pstmt1.executeUpdate();
            
            if ( isOk1 > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        }

        catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }   
    
/**
    * 사용자 메인 이미지 설정
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int reg(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet rs1   = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2  = null;
                
        String sql1   = "";
        String sql2   = "";
        
        int isOk1     = 1;
        int isOk2     = 1;
        
        int v_seq     = box.getInt("p_seq");  // 등록될 게시판 일련번호... max값.
        
        String s_userid     = box.getSession("userid");
        String s_userNm     = box.getSession("name");        
		            
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);		                            
            
            // ----------------------   게시판 update   --------------------------
            sql1 =  "	update TZ_MEMBERFILE 					 \n" +
            	    "	set ismain = 'N' 			  		 \n" +
            	    "   where userid = ? 		                         \n";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, s_userid);
            
            isOk1 = pstmt1.executeUpdate();
            
            // ----------------------   게시판 update   --------------------------
            sql2 =  "	update TZ_MEMBERFILE 					 \n" +
            	    "	set ismain = 'Y' 			  		 \n" +
            	    "   where userid = ? and seq = ?                             \n";

            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setString(1, s_userid);
            pstmt2.setInt(2, v_seq);
            
            isOk2 = pstmt2.executeUpdate();            
            
            if ( isOk1 > 0 && isOk2>0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        }

        catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    } 
    
/**
    * 사용자 메인 이미지 삭제
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int delete(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet rs1   = null;
        PreparedStatement pstmt1  = null;
                
        String sql    = "";
        String sql1   = "";
        
        int isOk1     = 1;
        
        int v_seq     = box.getInt("p_seq");  // 등록될 게시판 일련번호... max값.
        
        String s_userid     = box.getSession("userid");
        String s_userNm     = box.getSession("name");     

		String [] v_newFileName	= new String [FILE_LIMIT];        
        
           
		            
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);		
            
            //----------------------   게시판 번호 가져온다 ----------------------------
            sql  = "	select savefile, realfile from TZ_MEMBERFILE 			";
            sql += "	where userid = " + StringManager.makeSQL(s_userid) + " and seq = "+v_seq;
            rs1 = connMgr.executeQuery(sql);
            if ( rs1.next() ) { 
                v_newFileName[0] = rs1.getString(1);
            }
            rs1.close();
                                                    
            
            // ----------------------   게시판 update   --------------------------
            sql1 =  "	delete TZ_MEMBERFILE 					 \n" +            	    
            	    "   where userid = ? and seq = ?                             \n";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, s_userid);
            pstmt1.setInt(2, v_seq);
            
            isOk1 = pstmt1.executeUpdate();
            
FileManager.deleteFile(v_newFileName[0]);		// 	일반파일, 첨부파일 있으면 삭제..            
            
            if ( isOk1 > 0) { 
                if ( connMgr != null ) { 
                	try { 
                		connMgr.commit(); 
                		
                		} catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        }

        catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }          
    
    /**
    마이페이지에서 필요한 개인 정보 가져오기...
    @param box      receive from the form object and session
    @return ArrayList
    */
     public DataBox selectUserSuRoyInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        DataBox             dbox    = null;

        String  v_user_id   = box.getSession("userid");
        String  v_tem_grcode= box.getSession("tem_grcode");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1  = "";	            	  	
            	sql1 += "	select tm.name, 					";
            	sql1 += "		tm.zip_cd as post, 				";
            	sql1 += "		tm.address as addr, 			";
            	sql1 += "		nvl(tm.handphone,tm.hometel) phone, ";
            	sql1 += "		tm.email,				 		";
            	sql1 += "		tm.indate,				 		";
            	sql1 += "		tm.lgcnt,				 		";
				sql1 += "		tm.lglast,				 		";            	
				sql1 += "		tmf.savefile,				 	";            			
				sql1 += "		ts.isstrout,					";            	
				sql1 += "		'' as my_prepared				";            	
            	sql1 += "	from tz_member tm, tz_memberfile tmf, tz_strout ts 		";
            	sql1 += "	where tm.userid = " + StringManager.makeSQL(v_user_id)		;               	
				sql1 += "		and tm.userid	= tmf.userid(+)				";            	
				sql1 += "		and 'Y'		    = tmf.ismain(+)				";            	
            	sql1 += "		and tm.userid	= ts.userid(+) 				";
            	

            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
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
                      
}
