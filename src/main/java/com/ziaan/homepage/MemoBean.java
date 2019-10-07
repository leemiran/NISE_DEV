//**********************************************************
//  1. ��      ��: �����ּҷ� ����
//  2. ���α׷��� : MemoBean.java
//  3. ��      ��: �����ּҷ� ����
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��:  2003. 7. 13
//  7. ��      ��:
//**********************************************************

package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MemoBean {

    public MemoBean ()  {}

    /**
    * �������� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �������� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListRecvMemo(RequestBox  box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        MemoData data = null;

        int v_pageno    = box.getInt("p_pageno");
        String s_userid = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql += " select a.memocode memocode, b.sender sender, c.name name,     ";
            sql += "        b.sdate sdate, decode(a.viewdate, '','N','Y') viewyn,  ";
            sql += "        b.memosubj memosubj, b.contents contents               ";
            sql += " from TZ_MEMORECV a, TZ_MEMO b, TZ_MEMBER c                    ";
            sql += " where a.memocode = b.memocode                                 ";
            sql += "   and b.sender   = c.userid                                   ";
            sql += "   and a.gubun < 1                                             ";
            sql += "   and a.receiver = " + StringManager.makeSQL(s_userid);
            sql += " order by a.memocode desc                                      ";

            ls = connMgr.executeQuery(sql);

            ls.setPageSize(9);                         //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();   //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount  = ls.getTotalCount();  //     ��ü row ���� ��ȯ�Ѵ�

            while (ls.next()) {
                data = new MemoData();

                data.setMemocode(ls.getInt("memocode"));
                data.setSender(ls.getString("sender"));
                data.setSendernm(ls.getString("name"));
                data.setSdate(ls.getString("sdate"));
                data.setViewyn(ls.getString("viewyn"));
                data.setMemosubj(ls.getString("memosubj"));
//              data.setContents(ls.getString("contents"));
                data.setContents(ls.getCharacterStream("contents"));
                data.setDispnum(totalrowcount - ls.getRowNum() + 1);
                data.setTotalpagecount(totalpagecount);
                list.add(data);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
	/**
	* �ű� �������� ����Ʈ
	* @param box          receive from the form object and session
	* @return int   �������� ����Ʈ ����
	* @throws Exception
	*/
	public static int NewSelectListRecvMemoCnt(RequestBox  box) throws Exception {
        DBConnectionManager connMgr = null;
        int result = 0;
        ListSet ls = null;
        String sql = "";
        String s_userid = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();
           
            sql += "\n select count(a.memocode) as cnt    ";
            sql += "\n from TZ_MEMORECV a, TZ_MEMO b, TZ_MEMBER c                    ";
            sql += "\n where a.memocode = b.memocode                                 ";
            sql += "\n   and b.sender   = c.userid                                   ";
            sql += "\n   and a.viewdate is null                                      ";
            sql += "\n   and a.gubun = 0                                             ";
            sql += "\n   and a.receiver = " + StringManager.makeSQL(s_userid);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
            	result = ls.getInt("cnt");   
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }

    /**
    * �������� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �������� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListSendMemo(RequestBox  box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        MemoData data = null;

        int v_pageno    = box.getInt("p_pageno");
        String s_userid = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql += " select a.memocode memocode, b.receiver receiver, c.name name,   ";
            sql += "        a.sdate sdate, b.viewdate viewdate,                      ";
            sql += "        a.memosubj memosubj, a.contents contents                 ";
            sql += " from  TZ_MEMO a, TZ_MEMORECV b, TZ_MEMBER c                     ";
            sql += " where a.memocode  = b.memocode                                  ";
            sql += "   and a.tablename = c.userid                                    ";
            sql += "   and a.gubun < 1                                               ";
            sql += "   and a.sender = " + StringManager.makeSQL(s_userid);
            sql += " order by a.memocode desc                                        ";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(9);                         //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();   //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount  = ls.getTotalCount();  //     ��ü row ���� ��ȯ�Ѵ�

            while (ls.next()) {
                data = new MemoData();

                data.setMemocode(ls.getInt("memocode"));
                data.setReceiver(ls.getString("receiver"));
                data.setReceivernm(ls.getString("name"));
                data.setSdate(ls.getString("sdate"));
                data.setViewdate(ls.getString("viewdate"));
                data.setMemosubj(ls.getString("memosubj"));
//              data.setContents(ls.getString("contents"));
                data.setContents(ls.getCharacterStream("contents"));
                data.setDispnum(totalrowcount - ls.getRowNum() + 1);
                data.setTotalpagecount(totalpagecount);
                list.add(data);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
    * �������� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �������� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListDelMemo(RequestBox   box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        MemoData data = null;

        int v_pageno    = box.getInt("p_pageno");
        String s_userid = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql += " select a.memocode memocode, b.sender sender, c.name name,     ";
            sql += "        b.sdate sdate, decode(a.viewdate, '','N','Y') viewyn,  ";
            sql += "        b.memosubj memosubj, b.contents contents               ";
            sql += " from TZ_MEMORECV a, TZ_MEMO b, TZ_MEMBER c                    ";
            sql += " where a.memocode = b.memocode                                 ";
            sql += "   and b.sender   = c.userid                                   ";
            sql += "   and a.gubun = 1                                             ";
            sql += "   and a.receiver = " + StringManager.makeSQL(s_userid);
            sql += " order by a.memocode desc                                      ";
            
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(9);                         //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();   //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount  = ls.getTotalCount();  //     ��ü row ���� ��ȯ�Ѵ�

            while (ls.next()) {
                data = new MemoData();

                data.setMemocode(ls.getInt("memocode"));
                data.setSender(ls.getString("sender"));
                data.setSendernm(ls.getString("name"));
                data.setSdate(ls.getString("sdate"));
                data.setViewyn(ls.getString("viewyn"));
                data.setMemosubj(ls.getString("memosubj"));
//              data.setContents(ls.getString("contents"));
                data.setContents(ls.getCharacterStream("contents"));
                data.setDispnum(totalrowcount - ls.getRowNum() + 1);
                data.setTotalpagecount(totalpagecount);
                list.add(data);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
    * ��������ȭ�� �󼼺���
    * @param box          receive from the form object and session
    * @return ArrayList   ��ȸ�� ������
    * @throws Exception
    */
   public MemoData selectViewRecvMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        MemoData data = null;

        String v_memocode = box.getString("p_memocode");
        String s_userid   = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();

            sql += " select a.memocode memocode, b.sender sender, c.name name,     ";
            sql += "        b.sdate sdate, decode(a.viewdate, '','N','Y') viewyn,  ";
            sql += "        b.memosubj memosubj, b.contents contents               ";
            sql += " from TZ_MEMORECV a, TZ_MEMO b, TZ_MEMBER c                    ";
            sql += " where a.memocode = b.memocode                                 ";
            sql += "   and b.sender   = c.userid                                   ";
            sql += "   and a.gubun < 1                                             ";
            sql += "   and a.receiver = " + StringManager.makeSQL(s_userid);
            sql += "   and a.memocode = " + v_memocode;

            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                data=new MemoData();
                data.setMemocode(ls.getInt("memocode"));
                data.setSender(ls.getString("sender"));
                data.setSendernm(ls.getString("name"));
                data.setSdate(ls.getString("sdate"));
                data.setViewyn(ls.getString("viewyn"));
                data.setMemosubj(ls.getString("memosubj"));
//              data.setContents(ls.getString("contents"));
                data.setContents(ls.getCharacterStream("contents").replace("<br>","\r\n"));

            }

            // �޸�Ȯ�� �ð�
            if (data.getViewyn().equals("N")) {
                connMgr.executeUpdate("update TZ_MEMORECV set viewdate = to_char(sysdate, 'YYYYMMDDHH24MISS') where memocode = " + v_memocode);
            }

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return data;
    }

    /**
    * ��������ȭ�� �󼼺���
    * @param box          receive from the form object and session
    * @return ArrayList   ��ȸ�� ������
    * @throws Exception
    */
   public MemoData selectViewSendMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        MemoData data = null;

        String v_memocode = box.getString("p_memocode");
        String s_userid   = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();

            sql += " select a.memocode memocode, b.receiver receiver, c.name name,   ";
            sql += "        a.sdate sdate, b.viewdate viewdate,                      ";
            sql += "        a.memosubj memosubj, a.contents contents                 ";
            sql += " from  TZ_MEMO a, TZ_MEMORECV b, TZ_MEMBER c                     ";
            sql += " where a.memocode  = b.memocode                                  ";
            sql += "   and a.tablename = c.userid                                    ";
            sql += "   and a.gubun < 1                                               ";
            sql += "   and a.sender = " + StringManager.makeSQL(s_userid);
            sql += "   and a.memocode = " + v_memocode;

            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                data=new MemoData();
                data.setMemocode(ls.getInt("memocode"));
                data.setReceiver(ls.getString("receiver"));
                data.setReceivernm(ls.getString("name"));
                data.setSdate(ls.getString("sdate"));
                data.setViewdate(ls.getString("viewdate"));
                data.setMemosubj(ls.getString("memosubj"));
//              data.setContents(ls.getString("contents"));
                data.setContents(ls.getCharacterStream("contents").replace("<br>","\r\n"));
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return data;
    }

    /**
    * �����޸� ���� �����Ҷ� (gubun flag 1�� ����)
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int deleteRMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk       = 0;

        String v_memocode     = box.getString("p_memocode");

        try {
            connMgr = new DBConnectionManager();

            sql  = " update TZ_MEMORECV  ";
            sql += "    set gubun = '1'  ";
            sql += "  where memocode = ? ";
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_memocode);
            isOk =  pstmt.executeUpdate();

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }

    /**
    * �����޸� ���� �����Ҷ� (gubun flag 1�� ����)
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int deleteSMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk       = 0;

        String v_memocode     = box.getString("p_memocode");

        try {
            connMgr = new DBConnectionManager();

            sql  = " update TZ_MEMO      ";
            sql += "    set gubun = '1'  ";
            sql += "  where memocode = ? ";
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_memocode);
            isOk =  pstmt.executeUpdate();

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }

    /**
    * ����޸� ���� �����Ҷ� (gubun flag 2�� ����)
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int deleteDMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk       = 0;

        String v_memocode     = box.getString("p_memocode");

        try {
            connMgr = new DBConnectionManager();

            sql  = " update TZ_MEMORECV  ";
            sql += "    set gubun = '2'  ";
            sql += "  where memocode = ? ";
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_memocode);
            isOk =  pstmt.executeUpdate();

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }

    /**
    * �����޸� �����Ҷ� (gubun flag 1�� ����)
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int deleteRecvMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk_check = 0;
        int isOk       = 0;
        Vector v_vecmemocode  = new Vector();
        String v_memocode     = "";

        v_vecmemocode = box.getVector("p_vmemocode");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk = 1;

            sql  = " update TZ_MEMORECV  ";
            sql += "    set gubun = '1'  ";
            sql += "  where memocode = ? ";
            pstmt = connMgr.prepareStatement(sql);

            for(int i = 0; i < v_vecmemocode.size() ; i++){
                v_memocode = (String)v_vecmemocode.elementAt(i);

                pstmt.setString(1, v_memocode);

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
    * �����޸� �����Ҷ� (gubun flag 1�� ����)
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int deleteSendMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk_check = 0;
        int isOk       = 0;
        Vector v_vecmemocode  = new Vector();
        String v_memocode     = "";

        v_vecmemocode = box.getVector("p_vmemocode");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk = 1;

            sql  = " update TZ_MEMO      ";
            sql += "    set gubun = '1'  ";
            sql += "  where memocode = ? ";
            pstmt = connMgr.prepareStatement(sql);

            for(int i = 0; i < v_vecmemocode.size() ; i++){
                v_memocode = (String)v_vecmemocode.elementAt(i);

                pstmt.setString(1, v_memocode);

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
    * ����޸� �����Ҷ� (gubun flag 2�� ����)
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int deleteDelMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk_check = 0;
        int isOk       = 0;
        Vector v_vecmemocode  = new Vector();
        String v_memocode     = "";

        v_vecmemocode = box.getVector("p_vmemocode");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk = 1;

            sql  = " update TZ_MEMORECV  ";
            sql += "    set gubun = '2'  ";
            sql += "  where memocode = ? ";
            pstmt = connMgr.prepareStatement(sql);

            for(int i = 0; i < v_vecmemocode.size() ; i++){
                v_memocode = (String)v_vecmemocode.elementAt(i);

                pstmt.setString(1, v_memocode);

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
    * ����޸� �����Ҷ� (gubun flag 0�� ����)
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public int recoverDelMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;

        PreparedStatement pstmt = null;
        String sql = "";
        int isOk_check = 0;
        int isOk       = 0;
        Vector v_vecmemocode  = new Vector();
        String v_memocode     = "";

        v_vecmemocode = box.getVector("p_vmemocode");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk = 1;

            sql  = " update TZ_MEMORECV  ";
            sql += "    set gubun = '0'  ";
            sql += "  where memocode = ? ";
            pstmt = connMgr.prepareStatement(sql);

            for(int i = 0; i < v_vecmemocode.size() ; i++){
                v_memocode = (String)v_vecmemocode.elementAt(i);

                pstmt.setString(1, v_memocode);

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
    * ���� ������
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertMemo(RequestBox box) throws Exception {

        DBConnectionManager connMgr = null;
        ListSet ls = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1       = 0;
        int isOk2       = 0;
        int isOk1_check = 0;
        int isOk2_check = 0;
        int v_memocode  = 0;

        String v_multireceiver  = box.getString("p_receiver");
        String v_memosubj       = box.getString("p_memosubj");
     // String v_contents       = StringManager.replace(box.getString("p_contents"),"&","&");
        String v_contents       = box.getString("p_contents").replace("\r\n","<br>");
        String s_userid         = box.getSession("userid");
        StringTokenizer st      = new StringTokenizer(v_multireceiver,",");
        String v_receiver  =    "";


        try {

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            isOk1      = 1;
            isOk2      = 1;
            // �������� ����
            sql1 =  "insert into TZ_MEMO(memocode, sender, sdate, memosubj, contents, tablename, gubun ) ";
            sql1 += "              values (?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?)         ";
//            sql1 += "              values (?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?)         ";
            pstmt1 = connMgr.prepareStatement(sql1);
            // ���� ���� ����
            sql2 =  "insert into TZ_MEMORECV(memocode, receiver, gubun ) ";
            sql2 += "              values (?, ?, ?)                      ";
            pstmt2 = connMgr.prepareStatement(sql2);

            while (st.hasMoreTokens()) {
                v_receiver = (String)st.nextToken();
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


                // �������� ���� INSERT
                pstmt1.setInt(1,  v_memocode);
                pstmt1.setString(2,  s_userid);
                pstmt1.setString(3,  v_memosubj);
//              connMgr.setCharacterStream(pstmt1, 4, v_contents); //      Oracle 9i or Weblogic 6.1 �� ���
                pstmt1.setString(4,  v_contents);
//              pstmt1.setCharacterStream(4,  new StringReader(v_contents), v_contents.length());
                pstmt1.setString(5,  v_receiver);
                pstmt1.setInt(6,  0);
                isOk1_check = pstmt1.executeUpdate();
                if(isOk1_check == 0) isOk1 = 0;

                //sql3 = "select contents from TZ_MEMO where memocode= " + v_memocode + " for update";
				sql3 = "select contents from TZ_MEMO where memocode= " + v_memocode ;
                //connMgr.setWeblogicCLOB(sql3, v_contents);       //      clob �ʵ忡 ��Ʈ���� �̿�,  data �� �ִ´�(Weblogic �� ���)
//				connMgr.setOracleCLOB(sql3, v_contents);	//����Ŭ


                // �������� ���� INSERT
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

    /**
    * ��Ȯ������ ����
    * @param box          receive from the form object and session
    * @return result      count
    * @throws Exception
    */
    public int getUnconfirmedMemo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        int result = 0;

        String s_userid = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();

            sql  = " select count(*) cnt   ";
            sql += " from TZ_MEMORECV      ";
            sql += " where gubun < 1       ";
            sql += "   and viewdate is null   ";
            sql += "   and receiver = " + StringManager.makeSQL(s_userid);
            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                result = ls.getInt("cnt");
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }

	/**
    * ��Ȯ������ ����
    * @param box          receive from the form object and session
    * @return result      count
    * @throws Exception
    */
    public int getUnconfirmedMemo(HttpSession session) throws Exception {
    	RequestBox box = null;
    	
    	box = new RequestBox("requestbox");

    	//System.out.println("String userid" + userid);
        //box.put("userid",userid);
        box.put("session", session);  
        //System.out.println("box userid" + box.getSession("userid"));
        return this.getUnconfirmedMemo(box);
    }

    /**
    * �����»�� �̸� ���ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
    public String receiveName(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        String result = "";
        int i = 0,j = 0;
        String v_multireceiver  = box.getString("p_receiver");
        String v_memoquery    = "";

        StringTokenizer st = new StringTokenizer(v_multireceiver,",");
        String v_receiver  ="";

        while (st.hasMoreTokens()) {
            v_receiver = (String)st.nextToken();
            if (i != 0) {
                v_memoquery += ",";
            } else {
                v_memoquery += "(";
            }

            v_memoquery = v_memoquery + "'" + v_receiver + "'";
            i++;
        }
        if (!v_memoquery.equals("")) {
            v_memoquery += ")";
        }


        if (!v_multireceiver.equals("")) {
            try {
                connMgr = new DBConnectionManager();

                sql  = " select name from TZ_MEMBER  ";
                sql += "  where userid in " + v_memoquery;

                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    if (j != 0) result += ",";
                    result += ls.getString("name");
                    j++;
                }
            }
            catch (Exception ex) {
                ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
            }
            finally {
                if(ls != null) { try { ls.close(); } catch (Exception e) {} }
                if(connMgr != null) try { connMgr.setAutoCommit(true); } catch (Exception e) {}
                if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
            }
        }

        return result;
    }

    /**
    * ���� ������(����)
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public boolean insertMemoByMail(String s_userid, String v_receiver, String v_memosubj, String v_contents ) throws Exception {
        DBConnectionManager connMgr = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2  = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1       = 0;
        int isOk2       = 0;
        boolean result  = false;
        int v_memocode  = 0;

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // �������� ����
            sql1 =  "insert into TZ_MEMO(memocode, sender, sdate, memosubj, contents, tablename, gubun ) ";
            sql1 += "              values (?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?)       ";
//            sql1 += "              values (?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?)       ";
            pstmt1 = connMgr.prepareStatement(sql1);
            // ���� ���� ����
            sql2 =  "insert into TZ_MEMORECV(memocode, receiver, gubun ) ";
            sql2 += "              values (?, ?, ?)                      ";
            pstmt2 = connMgr.prepareStatement(sql2);

            stmt1 = connMgr.createStatement();
            sql  = "select max(memocode) from TZ_MEMO  ";
            rs1 = stmt1.executeQuery(sql);
            if (rs1.next()) {
                v_memocode = rs1.getInt(1) + 1;
            } else {
                v_memocode = 1;
            }
            
            // �������� ���� INSERT
            pstmt1.setInt(1,  v_memocode);
            pstmt1.setString(2,  s_userid);
            pstmt1.setString(3,  v_memosubj);
            pstmt1.setString(4,  v_memosubj);
//          pstmt1.setCharacterStream(4,  new StringReader(v_contents), v_contents.length());
            pstmt1.setString(5,  v_receiver);
            pstmt1.setInt(6,  0);
            isOk1 = pstmt1.executeUpdate();

            sql3 = "select contents from TZ_MEMO where memocode= " + v_memocode ;
//	    connMgr.setOracleCLOB(sql3, v_contents);	//����Ŭ


            // �������� ���� INSERT
            pstmt2.setInt(1,  v_memocode);
            pstmt2.setString(2,  v_receiver);
            pstmt2.setInt(3,  0);
            isOk2 = pstmt2.executeUpdate();
            if ((isOk1 * isOk2) > 0) {
                connMgr.commit();
                result = true;
            } else connMgr.rollback();
        }
        catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
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

        return result;
    }
    
	/**
	* �ű� �������� ����Ʈ
	* @param box          receive from the form object and session
	* @return int   �������� ����Ʈ ����
	* @throws Exception
	*/
	public static int NewListRecvMemoCnt(String s_userid) throws Exception {
        DBConnectionManager connMgr = null;
        int result = 0;
        ListSet ls = null;
        String sql = "";

        try {
            connMgr = new DBConnectionManager();
           
            sql += "\n select count(a.memocode) as cnt    ";
            sql += "\n from TZ_MEMORECV a, TZ_MEMO b, TZ_MEMBER c                    ";
            sql += "\n where a.memocode = b.memocode                                 ";
            sql += "\n   and b.sender   = c.userid                                   ";
            sql += "\n   and a.viewdate is null                                      ";
            sql += "\n   and a.gubun = 0											 ";
            sql += "\n   and a.receiver = " + StringManager.makeSQL(s_userid);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
            	result = ls.getInt("cnt");   
            }
        }
        catch (Exception ex) {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }
}