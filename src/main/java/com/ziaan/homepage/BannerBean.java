// **********************************************************
//  1. ��      ��: ��ʰ���
//  2. ���α׷���: BannerBean.java
//  3. ��      ��: ��ʰ���
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ���̼�
//  7. ��      ��:
// **********************************************************
package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class BannerBean { 

    public BannerBean() { }


    /**
    ��� ����Ʈ ��ȸ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList SelectList(RequestBox box) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList           list    = null;
        String  sql          = "";
        DataBox dbox        = null;
        String ss_grcode= box.getString("s_grcode");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "select grcode, seq, sitenm, url, realfile, savefile, content, sort		\n"
            	+ "from tz_banner															\n"	
            	+ "where 1=1                                                                \n";
            
            if(!"----".equals(ss_grcode)) {
            	sql += "and grcode= " + StringManager.makeSQL(ss_grcode);
            }
            
            sql += "order by sort													\n";
            
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


   /**
    ��� ���� ����
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int updateSort(RequestBox box) throws Exception { 

		DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String  sql			= "";
        int isOk			= 0;

        String ss_grcode = box.getString("s_grcode");
        Vector v_seq	= box.getVector("seq");
        Vector v_sort	= box.getVector("p_sort");
        String  s_seq		= "";
        String  s_sort		= "";

        try { 
            connMgr = new DBConnectionManager();

			sql  = " update tz_banner set sort=? where seq = ? and grcode = ? ";
            
            pstmt = connMgr.prepareStatement(sql);

            if ( v_seq != null ) { 
                for ( int i = 0; i < v_seq.size() ; i++ ) {
                    s_seq = (String )v_seq.elementAt(i);
                    s_sort = (String )v_sort.elementAt(i);
                    pstmt.setString (1,  s_sort);
                    pstmt.setString (2,  s_seq);
                    pstmt.setString (3,  ss_grcode);
                    isOk = pstmt.executeUpdate();
                }
            }   
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }



    /**
    ��� ���� ��ȸ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public DataBox SelectView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String  sql          = "";
        DataBox dbox        = null;

        String ss_grcode = box.getString("s_grcode");
        String  v_seq		= box.getString ("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql = "select grcode, seq, sitenm, url, realfile, savefile, content, realfile, savefile \n ";
            sql+= "  from tz_banner \n ";
            sql+= "  where seq  = " + StringManager.makeSQL(v_seq);
            sql+= "and grcode = " + StringManager.makeSQL(ss_grcode);
            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) 
                dbox = ls.getDataBox();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }



   /**
    ��� ���
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int insert(RequestBox box) throws Exception { 
    	 DBConnectionManager connMgr = null;
         PreparedStatement pstmt = null;
         ListSet ls  = null;
         
         String  sql  = "";
         String  sql2 = "";
         
         int isOk   = 0;

         String v_grcode = box.getString("s_grcode");
         String v_sitenm   = box.getString("p_sitenm");
         String v_url     = box.getString("p_url");
         String v_content = box.getString("p_content");
         String s_userid  = box.getSession("userid");
         String v_realfile = box.getRealFileName("p_file");
         String v_savefile = box.getNewFileName("p_file");
         int v_seq = 0;
         try {
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
             sql2 ="select nvl(max(seq),0) + 1 seq from tz_banner where grcode= " + StringManager.makeSQL(v_grcode) + " \n";
             
             ls = connMgr.executeQuery(sql2);
  			 
             if(ls.next()) {
             	v_seq = ls.getInt("seq");
             }

 			sql  = " insert into tz_banner(grcode, seq, sitenm, content, url, ldate, luserid, realfile, savefile) "
 			     + " values (?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, ?, ?)"; 

 			pstmt = connMgr.prepareStatement(sql);
 			pstmt.setString (1,v_grcode);
 			pstmt.setInt (2,v_seq);
 			pstmt.setString (3,v_sitenm);
 			pstmt.setString (4,v_content);
 			pstmt.setString (5,v_url);
 			pstmt.setString (6,s_userid);
 			pstmt.setString (7,v_realfile);
 			pstmt.setString (8,v_savefile);
 			
 			isOk = pstmt.executeUpdate();

         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(pstmt != null)  { try { pstmt.close(); } catch (Exception e1) {} }
             if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e2) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return isOk;
    }


   /**
    ��� ����
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int update(RequestBox box) throws Exception { 

		DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String  sql			= "";
        int isOk			= 0;

        String v_grcode = box.getString("s_grcode");
        String v_sitenm   = box.getString("p_sitenm");
        String v_url     = box.getString("p_url");
        String v_content = box.getString("p_content");
        String s_userid  = box.getSession("userid");
        int v_seq = box.getInt("p_seq");
        String  v_delyn = box.getString("p_delyn");
        String v_realfile = box.getRealFileName("p_file");
        String v_savefile = box.getNewFileName("p_file");
        
        String v_originsavefile = box.getString("p_savefile");
        try { 
            connMgr = new DBConnectionManager();
         
            sql  = "update tz_banner set"
                 + " sitenm			 = ?, " 
			     + " content		 = ?, " 
			     + " url			 = ?, ";
            	if("Y".equals(v_delyn) || !"".equals(v_realfile)) {
			    	 sql += "realfile = ?, "
			    		 +  "savefile = ?, ";
			     }
			     sql+= " ldate			 = to_char(sysdate,'YYYYMMDDHH24MISS'), "
			     + " luserid         = ? \n"
			     + "where seq = ? "
				 + "and grcode = ? \n";

			pstmt = connMgr.prepareStatement(sql);

            int params = 1;
			pstmt.setString (params++,  v_sitenm);
			pstmt.setString (params++,  v_content);
            pstmt.setString (params++,  v_url);
            if("Y".equals(v_delyn) || !"".equals(v_realfile)) {
                pstmt.setString (params++,  v_realfile);
                pstmt.setString (params++,  v_savefile);
            }
            pstmt.setString (params++,  s_userid);
            pstmt.setInt(params++, v_seq);
			pstmt.setString(params++, v_grcode);

			isOk = pstmt.executeUpdate();
			
			
			if(("Y".equals(v_delyn) || !"".equals(v_realfile)) && isOk > 0) {
				if(!"".equals(v_originsavefile))
					FileManager.deleteFile(v_originsavefile);      //  �Ϲ�����, ÷������ ������ ����..
			}

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


   /**
    ��� ����
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int delete(RequestBox box) throws Exception { 

		DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String  sql			= "";
        String  sql1			= "";
        int isOk            = 0;

        String ss_grcode = box.getString("s_grcode");
        int v_seq			= box.getInt("p_seq");
        int v_sort			= box.getInt("p_sort");
        String v_savefile = box.getString("p_savefile");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

        	sql1  = " update tz_banner set sort=sort-1 						\n"
				  + " where sort > " +v_sort + "        					\n"
				  + " and grcode = " + StringManager.makeSQL(ss_grcode) + " \n";

			pstmt = connMgr.prepareStatement(sql1);
			pstmt.executeUpdate();

			if ( pstmt != null )  { pstmt.close(); }

			sql  = " delete from tz_banner 	\n"
			     + " where seq = ? 			\n"
			     + " and grcode = ?         \n";

			pstmt = connMgr.prepareStatement(sql);

			pstmt.setInt(1,  v_seq);
			pstmt.setString(2, ss_grcode);

			isOk = pstmt.executeUpdate();
			
			if ( pstmt != null )  { pstmt.close(); }
			
			if(isOk > 0) {
				if(!"".equals(v_savefile))
					FileManager.deleteFile(v_savefile);      //  �Ϲ�����, ÷������ ������ ����..
			}
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e2 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
     
     /**
     ����Ʈ ���� ��� ��ȸ
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList bannerList(RequestBox box) throws Exception { 

         DBConnectionManager	connMgr	= null;
         ListSet ls          = null;
         ArrayList           list    = null;
         String  sql          = "";
         DataBox dbox        = null;
         String s_grcode = box.getSession("tem_grcode");

         try { 
             connMgr = new DBConnectionManager();
             list = new ArrayList();

             sql = "select grcode, seq, sitenm, url, realfile, savefile, content, sort		\n"
             	+ "from tz_banner															\n"	
             	+ "where 1=1                                                                \n"
             	+ "and grcode= " + StringManager.makeSQL(s_grcode) + "                      \n"
                + "order by sort															\n";
             
             ls = connMgr.executeQuery(sql);

             while ( ls.next() ) { 
                 dbox = ls.getDataBox();
                 list.add(dbox);
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list;
     }
}
