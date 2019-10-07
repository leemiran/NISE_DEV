// **********************************************************
// 1. 제      목:
// 2. 프로그램명: CommunityFrInvitationBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.community;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityFrInvitationBean { 
//    private ConfigSet config;
//    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      파일업로드되는 tag name
//    private static final int FILE_LIMIT = 1;                    //    페이지에 세팅된 파일첨부 갯수


    public CommunityFrInvitationBean() { 
        try { 
//            config = new ConfigSet();
//            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
//            row = 10; // 강제로 지정
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
    * 회원초대 메일전송
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int sendInvitation(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";
        int         isOk        = 1;
//        int         v_seq       = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_title         = box.getString("p_title");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");

        Vector  v_p_right_userid      = box.getVector("p_right_userid"  );
//        Vector  v_p_right_name        = box.getVector("p_right_name"    );
        Vector  v_p_right_email       = box.getVector("p_right_email"   );
//        Vector  v_p_right_jikwinm     = box.getVector("p_right_jikwinm" );
//        Vector  v_p_right_deptnam     = box.getVector("p_right_deptnam" );
//        Vector  v_p_right_jikupnm     = box.getVector("p_right_jikupnm" );

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           // 일련번호 구하기
           int v_mailno=0;
           sql1 = "select nvl(max(MAILNO), 0)   from TZ_CMUMAIL ";
           ls = connMgr.executeQuery(sql1);
           while ( ls.next() ) v_mailno = ls.getInt(1);


             sql  =" insert into TZ_CMUMAIL ( mailno, userid, kor_nm, recv_email"
                  + "                       ,cmuno, cmu_nm, SEND_USERID,send_email, title, content"
                  + "                       ,loc_fg,loc_nm,regster_dte, send_fg)"
                  + "               values  (?,?,?,?"
                  + "                       ,?,?,?,?,?,?"
                  + "                       ,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),'N')"
                  ;
             pstmt = connMgr.prepareStatement(sql);
           for ( int i = 0;i<v_p_right_userid.size();i++ ) { 
            
               // 수신자명구하기
               String v_tmp_nm= "";
               sql1 = "select name   from tz_member where userid = '" +(String)v_p_right_userid.elementAt(i) + "' ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) v_tmp_nm = ls.getString(1);

               // 커뮤니티명구하기
               String v_tmp_cmu_nm= "";
               sql1 = "select cmu_nm   from tz_cmubasemst where cmuno = '" +v_cmuno + "' ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) v_tmp_cmu_nm = ls.getString(1);


               // 발신자 이메일
               String v_tmp_send_email= "";
               sql1 = "select email   from tz_member where userid = '" +s_userid + "' ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) v_tmp_send_email = ls.getString(1);
               v_mailno =v_mailno +1;
               pstmt.setInt   (1, v_mailno                                );// 일련번호
               pstmt.setString(2, (String)v_p_right_userid.elementAt(i)   );// 수신자아이디
               pstmt.setString(3, v_tmp_nm                                );// 수신자명
               pstmt.setString(4, (String)v_p_right_email.elementAt(i)    );// 수신자이메일
               pstmt.setString(5, v_cmuno                                 );// 커뮤니티먼호
               pstmt.setString(6, v_tmp_cmu_nm                            );// 커뮤니티명
               pstmt.setString(7 ,s_userid                                );// 발신자아이디
               pstmt.setString(8 ,v_tmp_send_email                        );// 발신자이메일
               pstmt.setString(9 , v_title                                );// 제목
               pstmt.setString(10, v_intro          );
               pstmt.setString(11, "1"                                    );// 구분
               pstmt.setString(12, "초대메세지"                           );// 구분명
               isOk = pstmt.executeUpdate();
               if ( pstmt != null ) { pstmt.close(); }
//               sql1 = "select content  from TZ_CMUMAIL where mailno = '" +v_mailno + "'";
//               connMgr.setOracleCLOB(sql1, v_intro);
               if ( isOk > 0 ) { 
                   if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
               }


           }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }

    /**
    * 쪽지 보내기
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertMemo(RequestBox box) throws Exception {

        DBConnectionManager connMgr = null;
//        ListSet ls = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
//        String sql3 = "";
        int isOk1       = 0;
        int isOk2       = 0;
        int isOk1_check = 0;
        int isOk2_check = 0;
        int v_memocode  = 0;
        
        Vector  v_p_right_userid      = box.getVector("p_right_userid"  );

        String v_multireceiver  = box.getString("p_receiver");
        
           for ( int i = 0;i<v_p_right_userid.size();i++ ) {             
               // 수신자명구하기               
               if (i==v_p_right_userid.size()-1) {
               	v_multireceiver = v_multireceiver + (String)v_p_right_userid.elementAt(i);
               } else 
               {
               	v_multireceiver = v_multireceiver + (String)v_p_right_userid.elementAt(i) + ",";               	
               }
               
           }        
        
        
        String v_memosubj       = box.getString("p_memosubj");
        String v_contents       = StringManager.replace(box.getString("p_contents"),"\n","<br>");
        String s_userid         = box.getSession("userid");
        StringTokenizer st      = new StringTokenizer(v_multireceiver,",");
        String v_receiver  =    "";


        try {

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk1      = 1;
            isOk2      = 1;
            // 보낸쪽지 정보
            sql1 =  "insert into TZ_MEMO(memocode, sender, sdate, memosubj, contents, tablename, gubun ) ";
            sql1 += "              values (?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, ?, ?, ?)         ";
            pstmt1 = connMgr.prepareStatement(sql1);
            // 받은 쪽지 정보
            sql2 =  "insert into TZ_MEMORECV(memocode, receiver, gubun ) ";
            sql2 += "              values (?, ?, ?)                      ";
            pstmt2 = connMgr.prepareStatement(sql2);

            while (st.hasMoreTokens()) {
                v_receiver = st.nextToken();
                stmt1 = connMgr.createStatement();
                sql  = "select max(memocode) from TZ_MEMO  ";
                stmt1 = connMgr.createStatement();

                rs1 = stmt1.executeQuery(sql);
                if (rs1.next()) {
                    v_memocode = rs1.getInt(1) + 1;
                } else {
                    v_memocode = 1;
                }
                if(rs1 != null) { try { rs1.close(); } catch (Exception e) {} }
                if(stmt1 != null) { try { stmt1.close(); } catch (Exception e1) {} }


                // 보낸쪽지 정보 INSERT
                pstmt1.setInt(1,  v_memocode);
                pstmt1.setString(2,  s_userid);
                pstmt1.setString(3,  v_memosubj);
                pstmt1.setString(4, v_contents          );
//              connMgr.setCharacterStream(pstmt1, 4, v_contents); //      Oracle 9i or Weblogic 6.1 인 경우
//              pstmt1.setString(4,  v_contents);
//              pstmt1.setCharacterStream(4,  new StringReader(v_contents), v_contents.length());
                pstmt1.setString(5,  v_receiver);
                pstmt1.setInt(6,  0);
                isOk1_check = pstmt1.executeUpdate();
                if ( pstmt1 != null ) { pstmt1.close(); }
                if(isOk1_check == 0) isOk1 = 0;

                //sql3 = "select contents from TZ_MEMO where memocode= " + v_memocode + " for update";
//				sql3 = "select contents from TZ_MEMO where memocode= " + v_memocode ;
//                //connMgr.setWeblogicCLOB(sql3, v_contents);       //      clob 필드에 스트림을 이용,  data 를 넣는다(Weblogic 인 경우)
//				connMgr.setOracleCLOB(sql3, v_contents);	//오라클


                // 받은쪽지 정보 INSERT
                pstmt2.setInt(1,  v_memocode);
                pstmt2.setString(2,  v_receiver);
                pstmt2.setInt(3,  0);
                isOk2_check = pstmt2.executeUpdate();
                if(isOk2_check == 0) isOk2 = 0;

            }

            if ((isOk1 * isOk2) > 0)  connMgr.commit();
            else                 connMgr.rollback();
        }
        catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(rs1 != null) { try { rs1.close(); } catch (Exception e) {} }
            if(stmt1 != null) { try { stmt1.close(); } catch (Exception e1) {} }
            if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e1) {} }
            if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return (isOk1 * isOk2);
    }
}