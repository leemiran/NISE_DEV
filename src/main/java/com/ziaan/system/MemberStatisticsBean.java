// **********************************************************
//  1. f      ��: �λ�DB �˻�
//  2. �wα׷���: MemberSearchBean.java
//  3. ��      ��: �λ�DB �˻�
//  4. ȯ      ��: JDK 1.4
//  5. ��      o: 1.0
//  6. ��      ��:  2004. 12. 20
//  7. ��      d:
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class MemberStatisticsBean {

    // private

    public MemberStatisticsBean() {
    }

    public static String getOrganization(RequestBox box, boolean isChange,
            boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        String result = "v�� ";

        try {
            String p_orgcode = box.getStringDefault("p_orgcode", "ALL"); // ��0���

            connMgr = new DBConnectionManager();

            sql = "select orgcode, title ";
            sql += " from tz_organization ";
            sql += " order by title";

            pstmt = connMgr.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);

            result += getSelectTag(ls, isChange, isALL, "p_orgcode", p_orgcode);
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }
        return result;
    }

    public static String getSelectTag(ListSet ls, boolean isChange,
            boolean isALL, String selname, String optionselected)
            throws Exception {
        StringBuffer sb = null;
        boolean isSelected = false;
        String v_tmpselname = "";

        try {
            sb = new StringBuffer();

            if (selname.equals("s_damungyyyy")) {
                v_tmpselname = "p_orgcode";
                sb.append("<select name = \"" + v_tmpselname + "\"");
            } else {
                sb.append("<select name = \"" + selname + "\"");
            }
            if (isChange)
                sb.append(" onChange = \"whenSelection('change')\"");
            sb.append(" > \r\n");
            if (isALL) {
                sb.append("<option value = \"ALL\" > ALL</option > \r\n");
            } else if (isChange) {
                if (selname.indexOf("year") == -1)
                    sb
                            .append("<option value = \"----\" >== ���� == </option > \r\n");
            }

            while (ls.next()) {
                ResultSetMetaData meta = ls.getMetaData();
                int columnCount = meta.getColumnCount();

                sb.append("<option value = \"" + ls.getString(1) + "\"");
                if (optionselected.equals(ls.getString(1)) && !isSelected) {
                    sb.append(" selected");
                    isSelected = true;
                } else if (selname.equals("s_gyear")
                        && ls.getString("gyear").equals(
                                FormatDate.getDate("yyyy"))
                        && optionselected.equals("") && !isSelected) { // ����ڵ������
                                                                        // ����
                    sb.append(" selected");
                    isSelected = true;
                }

                sb
                        .append(" > " + ls.getString(columnCount)
                                + "</option > \r\n");
            }
            sb.append("</select > \r\n");
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage());
        }
        return sb.toString();
    }

    /**
     * ȸ�纰 ȸ������Ȳ
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList performStatisticsMemberJoin(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;

        String ss_company = box.getStringDefault("s_company", "ALL");  // ȸ��
        String sql = "";
        String v_sido = box.getString("p_sido");
        String v_gugun = box.getString("p_gugun");
        String v_orderType = box.getString("p_orderType");
        String v_orderStr = box.getString("p_orderStr");

        String v_subQry1 = "";
        
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            if ( !ss_company.equals("ALL")) {
            	v_subQry1 = "\n and    a.comp = " + SQLString.Format(ss_company);
            }
            
            if (box.getString("p_action").equals("GO")) {
            	// ��f ȸ����; ���� ����Ʈ�� �̿��� ��� �Ʒ��� ��: ��谡 �ʿ�. 
            	/*
        	    sql = "\n select inmonth "
        	        + "\n      , count(inmonth) cnt "
        	        + "\n from   ( "                                                                                  
        	        + "\n        select decode(to_char(to_date(indate, 'yyyymmddhh24miss'),'mm') "
        	        + "\n                    , '01', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��01��' "
        	        + "\n                    , '02', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��02��' "
        	        + "\n                    , '03', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��03��' "
        	        + "\n                    , '04', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��04��' "
        	        + "\n                    , '05', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��05��' "
        	        + "\n                    , '06', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��06��' "
        	        + "\n                    , '07', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��07��' "
        	        + "\n                    , '08', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��08��' "
        	        + "\n                    , '09', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��09��' "
        	        + "\n                    , '10', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��10��' "
        	        + "\n                    , '11', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��11��' "
        	        + "\n                    , '12', to_char(to_date(indate, 'yyyymmddhh24miss'),'yyyy')||'��12��' "
        	        + "\n                    , '��Ÿ') inmonth "                      
        	        + "\n        from   tz_member "
        	        + v_subQry1
        	        + "\n       ) ";
            
        	    if (!"".equals(v_sdate) && !"".equals(v_ldate)) {
        	        if (v_sdate.equals(v_ldate)) {
        	            sql +="\n and    to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('" + v_sdate + "', 'yyyy-mm-dd'), 'yyyymmdd') ";
        	        } else {
        	            sql +="\n and    to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('" + v_sdate + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('" + v_ldate + "', 'yyyy-mm-dd'), 'yyyymmdd') ";
        	        }
        	    }
            
        	    sql +="\n group  by inMonth ";
        	    */
            	
            	// 2009.01.08 KT���簳�߿��� ��쿡�� ȸ������ ��� ������ ȸ�纰�� ȸ����Ȳ; �����ش�.
/*            	sql = "\n select nvl(comp,'-') as comp "
            	    + "\n      , nvl(get_compnm(comp),'-') as compnm "
            	    + "\n      , count(userid) cnt "
            	    + "\n from   tz_member "
            	    + "\n where  1=1 " 
            	    + v_subQry1
            	    + "\n group  by comp ";
*/
            	
            	sql = "select nvl(a.comp,'-') as comp " +
            		  "     , nvl(get_compnm(a.comp),'-') as compnm                " +
            		  "     , count(a.userid) cnt           " +
            		  "  from tz_member a , tz_grcomp b          " +
            		  " where  1=1             " +
            		  "   and  a.comp = b.comp          " 
            		  + v_subQry1 +
            		  " group  by a.comp";            	
                if ("cnt".equals(v_orderStr)) {
                    sql +="\n order by cnt " + v_orderType;
                }
                
                ls = connMgr.executeQuery(sql);
            }

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("compnm"));
                dbox.put("cnt", ls.getInt("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }
    
    
    /**
     * �õ��� ȸ�� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberDate(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String  v_orgcode   = box.getStringDefault("p_orgcode","ALL");
        String  v_inmonth   = box.getString("p_inmonth");
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                          \n";
        sql += "       indate,                                                                                                 \n";
        sql += "       count(indate) cnt                                                                                       \n";
        sql += "  from                                                                                                          \n";
        sql += "       (                                                                                                        \n";
        sql += "        select                                                                                                  \n";
        sql += "               NVL(to_char(to_date(a.indate, 'yyyymmddhh24miss'),'YYYY.MM.DD'), '��Ÿ')   inDate                 \n";
        sql += "          from                                                                                                  \n";
        sql += "               tz_member a                                                                                      \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            //sql += "    ,   vz_orgmember    b                  \n";
        } else {
            //sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            //sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            //sql += "   and a.userid  = b.userid                \n";
        } else {
            //sql += "   where a.userid  = b.userid(+)             \n";
        }
        sql += "   where 1 =1  \n";
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                               \n";
            }
        }
        
        if ( v_inmonth.equals("��Ÿ") ) {
            sql += "   and (a.indate IS NULL OR TRIM(a.indate) = '' )                  \n";
        } else if ( !v_inmonth.equals("��Ÿ")  ) {
            sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between (SUBSTR(" + SQLString.Format(v_inmonth) + ", 1, 4) || SUBSTR(" + SQLString.Format(v_inmonth) + ", 6, 2) ) || '01' ";  
            sql += "                                                                  and     (SUBSTR(" + SQLString.Format(v_inmonth) + ", 1, 4) || SUBSTR(" + SQLString.Format(v_inmonth) + ", 6, 2) ) || '31' ";  
        }
        
        sql += "       )      \n";
        sql += " group by     \n";
        sql += "       inDate \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            if (box.getString("p_action").equals("GO"))
                ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("inDate"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }
    

    /**
     * �õ��� ȸ�� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentMemberJoin(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;

        String sql = "";
/*
        sql = "";
        sql += "select                                                                                                          \n";
        sql += "       inMonth,                                                                                                 \n";
        sql += "       count(inMonth) cnt                                                                                       \n";
        sql += "  from                                                                                                          \n";
        sql += "       (                                                                                                        \n";
        sql += "        select                                                                                                  \n";
        sql += "               decode(                                                                                          \n";
        sql += "                      to_char(to_date(a.indate, 'yyyymmddhh24miss'),'MM'),                                        \n";
        sql += "                      '01', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��01��',                       \n";
        sql += "                      '02', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��02��',                       \n";
        sql += "                      '03', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��03��',                       \n";
        sql += "                      '04', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��04��',                       \n";
        sql += "                      '05', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��05��',                       \n";
        sql += "                      '06', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��06��',                       \n";
        sql += "                      '07', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��07��',                       \n";
        sql += "                      '08', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��08��',                       \n";
        sql += "                      '09', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��09��',                       \n";
        sql += "                      '10', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��10��',                       \n";
        sql += "                      '11', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��11��',                       \n";
        sql += "                      '12', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��12��',                       \n";
        sql += "                      '��Ÿ'                                                                                     \n";
        sql += "                     ) inMonth                                                                                  \n";
        sql += "          from                                                                                                  \n";
        sql += "               tz_member a                                                                                  \n";
        sql += "        , (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               e                               \n";
        
        if (!box.getString("p_orgcode").equals("ALL")) {
            sql += "               , vz_zipcode b, tz_organization c, tz_organization_mat d       \n";
        }
        
        sql += "         where                                                                      \n";
        sql += "               1=1                                                                  \n";
        sql += "         AND   a.userid = e.userid                                                  \n";
        
        if (!box.getString("p_orgcode").equals("ALL")) {
            sql += "          and a.post1||'-'||a.post2 = b.zipcode(+)           \n";
            sql += "          and b.gugun = d.gugun           \n";
            sql += "          and b.sido = c.sido           \n";
            sql += "          and c.orgcode = d.orgcode           \n";
            sql += "          and c.orgcode = " + box.getString("p_orgcode")
                    + "         \n";
        }

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        sql += "       )\n";
        sql += " group by \n";
        sql += "       inMonth\n";
*/
        
        


        sql += " SELECT v.inMonth, v.cnt, nvl((                                                                                                         \n";
        sql += "                            SELECT sum(cnt)                                                                                             \n";
        sql += "                            FROM (                                                                                                      \n";
        sql += "                            select                                                                                                      \n";
        sql += "                                   inMonth,                                                                                             \n";
        sql += "                                   count(inMonth) cnt                                                                                   \n";
        sql += "                              from                                                                                                      \n";
        sql += "                                   (                                                                                                    \n";
        sql += "                                    select                                                                                              \n";
        sql += "                                           decode(                                                                                      \n";
        sql += "                                                  to_char(to_date(a.indate, 'yyyymmddhh24miss'),'MM'),                                  \n";
        sql += "                                                  '01', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��01��',                \n";
        sql += "                                                  '02', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��02��',                \n";
        sql += "                                                  '03', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��03��',                \n";
        sql += "                                                  '04', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��04��',                \n";
        sql += "                                                  '05', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��05��',                \n";
        sql += "                                                  '06', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��06��',                \n";
        sql += "                                                  '07', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��07��',                \n";
        sql += "                                                  '08', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��08��',                \n";
        sql += "                                                  '09', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��09��',                \n";
        sql += "                                                  '10', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��10��',                \n";
        sql += "                                                  '11', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��11��',                \n";
        sql += "                                                  '12', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��12��',                \n";
        sql += "                                                  '��Ÿ'                                                                                \n";
        sql += "                                                 ) inMonth                                                                              \n";
        sql += "                                      from                                                                                              \n";
        sql += "                                              Tz_Member                      a                                                          \n";

    if (!box.getStringDefault("p_orgcode","ALL").equals("ALL")) {
        sql += "               , vz_zipcode b, tz_organization c, tz_organization_mat d       \n";
    }
        
        sql += "                                            , (                                                                                         \n";
        sql += "                                                SELECT b.UserId                                                                         \n";
        sql += "                                                FROM   VZ_SCSUBJSEQ          a                                                          \n";
        sql += "                                                    ,  Tz_Student            b                                                          \n";
        sql += "                                                WHERE  a.grcode  = 'N000001'                                                            \n";
        sql += "                                                AND    a.subj    = b.subj                                                               \n";
        sql += "                                                AND    a.subjseq = b.subjseq                                                            \n";
        sql += "                                                AND    a.year    = b.Year                                                               \n";
        sql += "                                            )               e                                                                           \n";
        sql += "                                             where                                                                                      \n";
        sql += "                                                   1=1                                                                                  \n";
        sql += "                                             AND   a.userid = e.userid                                                                  \n";
        
    if (!box.getStringDefault("p_orgcode","ALL").equals("ALL")) {
        sql += "          and a.zip_cd = b.zipcode(+)           \n";
        sql += "          and b.gugun = d.gugun           \n";
        sql += "          and b.sido = c.sido           \n";
        sql += "          and c.orgcode = d.orgcode           \n";
        sql += "          and c.orgcode = " + box.getString("p_orgcode")
                + "         \n";
    }

    if (!box.getString("p_sdate").equals("")
            && !box.getString("p_ldate").equals("")) {
        if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
            sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                    + box.getString("p_sdate")
                    + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
        } else {
            sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                    + box.getString("p_sdate")
                    + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                    + box.getString("p_ldate")
                    + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
        }
    }
        
        sql += "                                   )                                                                                                    \n";
        sql += "                             group by                                                                                                   \n";
        sql += "                                   inMonth                                                                                              \n";
        sql += "                                   ) dd where dd.inmonth < v.inmonth                                                                    \n";
        sql += "                         ), 0) acccnt                                                                                                   \n";
        sql += " FROM   (                                                                                                                               \n";
        sql += " select                                                                                                                                 \n";
        sql += "        inMonth,                                                                                                                        \n";
        sql += "        count(inMonth) cnt                                                                                                              \n";
        sql += "   from                                                                                                                                 \n";
        sql += "        (                                                                                                                               \n";
        sql += "         select                                                                                                                         \n";
        sql += "                decode(                                                                                                                 \n";
        sql += "                       to_char(to_date(a.indate, 'yyyymmddhh24miss'),'MM'),                                                             \n";
        sql += "                       '01', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��01��',                                           \n";
        sql += "                       '02', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��02��',                                           \n";
        sql += "                       '03', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��03��',                                           \n";
        sql += "                       '04', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��04��',                                           \n";
        sql += "                       '05', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��05��',                                           \n";
        sql += "                       '06', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��06��',                                           \n";
        sql += "                       '07', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��07��',                                           \n";
        sql += "                       '08', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��08��',                                           \n";
        sql += "                       '09', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��09��',                                           \n";
        sql += "                       '10', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��10��',                                           \n";
        sql += "                       '11', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��11��',                                           \n";
        sql += "                       '12', to_char(to_date(a.indate, 'yyyymmddhh24miss'),'yyyy')||'��12��',                                           \n";
        sql += "                       '��Ÿ'                                                                                                           \n";
        sql += "                      ) inMonth                                                                                                         \n";
        sql += "           from                                                                                                                         \n";
        sql += "                tz_member a                                                                                                             \n";
        
    if (!box.getStringDefault("p_orgcode","ALL").equals("ALL")) {
        sql += "               , vz_zipcode b, tz_organization c, tz_organization_mat d       \n";
    }
        
        sql += "         , (                                                                                                                            \n";
        sql += "             SELECT b.UserId                                                                                                            \n";
        sql += "             FROM   VZ_SCSUBJSEQ          a                                                                                             \n";
        sql += "                 ,  Tz_Student            b                                                                                             \n";
        sql += "             WHERE  a.grcode  = 'N000001'                                                                                               \n";
        sql += "             AND    a.subj    = b.subj                                                                                                  \n";
        sql += "             AND    a.subjseq = b.subjseq                                                                                               \n";
        sql += "             AND    a.year    = b.Year                                                                                                  \n";
        sql += "         )               e                                                                                                              \n";
        sql += "          where                                                                                                                         \n";
        sql += "                1=1                                                                                                                     \n";
        sql += "          AND   a.userid = e.userid                                                                                                     \n";
        
    if (!box.getStringDefault("p_orgcode","ALL").equals("ALL")) {
        sql += "          and a.zip_cd = b.zipcode(+)           \n";
        sql += "          and b.gugun = d.gugun           \n";
        sql += "          and b.sido = c.sido           \n";
        sql += "          and c.orgcode = d.orgcode           \n";
        sql += "          and c.orgcode = " + box.getString("p_orgcode")
                + "         \n";
    }

    if (!box.getString("p_sdate").equals("")
            && !box.getString("p_ldate").equals("")) {
        if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
            sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                    + box.getString("p_sdate")
                    + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
        } else {
            sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                    + box.getString("p_sdate")
                    + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                    + box.getString("p_ldate")
                    + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
        }
    }
        
        sql += "        )                                                                                                                               \n";
        sql += "  group by                                                                                                                              \n";
        sql += "        inMonth                                                                                                                         \n";
        sql += " )     v                                                                                                                                \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            if (box.getString("p_action").equals("GO"))
                ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("inMonth"));
                dbox.put("cnt", ls.getString("cnt"));
                dbox.put("acccnt", ls.getString("acccnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }
    
    
    
    
    /**
     * �õ��� ȸ�� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentDate(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String  v_orgcode   = box.getStringDefault("p_orgcode", "ALL");
        String  v_inmonth   = box.getString("p_inmonth");
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                          \n";
        sql += "       indate,                                                                                                 \n";
        sql += "       count(indate) cnt                                                                                       \n";
        sql += "  from                                                                                                          \n";
        sql += "       (                                                                                                        \n";
        sql += "        select                                                                                                  \n";
        sql += "               NVL(to_char(to_date(a.indate, 'yyyymmddhh24miss'),'YYYY.MM.DD'), '��Ÿ')   inDate                 \n";
        sql += "          from                                                                                                  \n";
        sql += "               tz_member a                                                                                      \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }
        
        sql += "        , (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               e                               \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                               \n";
            }
        }
        
        if ( v_inmonth.equals("��Ÿ") ) {
            sql += "   and (a.indate IS NULL OR TRIM(a.indate) = '' )                  \n";
        } else if ( !v_inmonth.equals("��Ÿ")  ) {
            sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between (SUBSTR(" + SQLString.Format(v_inmonth) + ", 1, 4) || SUBSTR(" + SQLString.Format(v_inmonth) + ", 6, 2) ) || '01' ";  
            sql += "                                                                  and     (SUBSTR(" + SQLString.Format(v_inmonth) + ", 1, 4) || SUBSTR(" + SQLString.Format(v_inmonth) + ", 6, 2) ) || '31' ";  
        }
        
        sql += "   where a.userid  = e.userid             \n";
        
        sql += "       )      \n";
        sql += " group by     \n";
        sql += "       inDate \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            if (box.getString("p_action").equals("GO"))
                ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("inDate"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }
    
    /**
     * ��� ���
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList performStatisticsMember(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String ss_company = box.getStringDefault("s_company", "ALL");  // ȸ��
        String sql = "";
        String v_orderType = box.getString("p_orderType");
        String v_orderStr = box.getString("p_orderStr");
        
        String v_subQry1= "";

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            if ( !ss_company.equals("ALL")) {
            	v_subQry1 = "\n and    b.comp = " + SQLString.Format(ss_company);
            }

            sql = "\n select nvl(a.sido,'-') as sido "
            	+ "\n      , count(b.userid) cnt "
            	+ "\n from   (select distinct zipcode, sido from tz_zipcode) a "
            	+ "\n      , tz_member b "
            	+ "\n where  a.zipcode(+) = b.zip_cd "
            	+ v_subQry1
            	+ "\n group  by a.sido ";

            if ("cnt".equals(v_orderStr)) {
                sql +="\n order by cnt " + v_orderType;
            }
            
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("sido"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ������ ȸ�����
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList performStatisticsMemberGugun(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;

        String sql = "";
        String v_sido = box.getString("p_sido");
        String ss_company = box.getStringDefault("s_company", "ALL");  // v���ڵ�

        String v_orderType = box.getString("p_orderType");
        String v_orderStr = box.getString("p_orderStr");
        String v_subQry1 = "";
        
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            if ( !ss_company.equals("ALL")) {
            	v_subQry1 = "\n and    b.comp = " + StringManager.makeSQL(ss_company);
            }

            sql = "\n select nvl(a.gugun,'-') as gugun "
                + "\n      , count(*) cnt "
                + "\n from   (select distinct zipcode, sido, gugun from tz_zipcode) a "
                + "\n      , tz_member b "
                + "\n where  a.zipcode(+) = b.zip_cd "
                + "\n and    a.sido like " + StringManager.makeSQL(v_sido + "%")
                + v_subQry1
                + "\n group  by a.sido, a.gugun ";

            if ("cnt".equals(v_orderStr)) {
                sql += "\n order  by cnt " + v_orderType;
            }

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("gugun"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���� ȸ�� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberDong(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;

        String sql = "";
        String v_sido = box.getString("p_sido");
        String v_gugun = box.getString("p_gugun");
        String ss_company = box.getStringDefault("s_company", "ALL");  // v���ڵ�

        String v_orderType = box.getString("p_orderType");
        String v_orderStr = box.getString("p_orderStr");
        String v_subQry1 = "";
        
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            if ( !ss_company.equals("ALL")) {
            	v_subQry1 = "\n and    b.comp = " + StringManager.makeSQL(ss_company);
            }

            sql = "\n select nvl(a.dong,'-') as dong "
                + "\n      , count(*) cnt "
                + "\n from   (select distinct zipcode, sido, gugun, dong from tz_zipcode) a "
                + "\n      , tz_member b "
                + "\n where  a.zipcode(+) = b.zip_cd "
                + "\n and    a.sido like " + StringManager.makeSQL(v_sido + "%")
                + "\n and    a.gugun like " + StringManager.makeSQL(v_gugun + "%")
                + v_subQry1
                + "\n group  by a.sido, a.gugun, a.dong ";

            if ("cnt".equals(v_orderStr)) {
                sql += "\n order  by cnt " + v_orderType;
            }

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("dong"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���� ȸ�� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberSex(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

//        sql = "";
//        sql += "select                                \n";
//        sql += "       decode(substr(nvl(a.birth_date,'0000000000'),7,1), '2', '����', '1', '����', '��Ÿ') gender                         \n";
//        sql += "     , count( nvl(substr(nvl(a.birth_date,'0000000000'),7,1), '1') ) cnt              \n";
//        sql += "  from                                \n";
//        sql += "         tz_member a                    \n";
//        
//        if ( !v_orgcode.equals("ALL") ) { 
//            //sql += "    ,   vz_orgmember    b                  \n";
//        } else {
//            //sql += "    ,   vz_orgmember1   b                  \n";
//        }
//        
//        
//        if ( !v_orgcode.equals("ALL") ) {   // v����
//            //sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
//            //sql += "   and a.userid  = b.userid                \n";
//        } else {
//            //sql += "   where a.userid  = b.userid(+)             \n";
//        }
//        sql  = " select gender, cnt 													\n";
//        sql += " from 																	\n";
//        sql += " 	(select '����' as gender, count(*) cnt from tz_member  				\n";
//        sql += " 	 where substr(nvl(birth_date,'0000000000'),7,1) ='1' 					\n";
//        sql += "     union all 															\n";
//        sql += " 	 select '����' as gender, count(*) cnt from tz_member 				\n";
//        sql += " 	 where substr(nvl(birth_date,'0000000000'),7,1) ='2'						\n";
//        sql += " 	 union all 															\n";
//        sql += " 	 select '��Ÿ' as gender, count(*) cnt from tz_member				\n";
//        sql += " 	 where substr(nvl(birth_date, '0000000000'),7,1) not in ('1','2')		\n";
//        sql += " 	)																	\n";
//        sql += "   where 1 = 1  														\n";
        
        sql = "select gender, count(gender) cnt							\n"
        	+ "from (													\n"
        	+ "         select case when length(birth_date) >=7 then			\n" 
        	+ "                    case when substr(birth_date, 7,1) = '1' then '����' else '����' end		\n"
        	+ "                else '��Ÿ' end gender, indate										\n"
        	+ "         from tz_member a															\n"
        	+ "         where 1=1																	\n";
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
                                      
         sql+= "    )																				\n"         
        	+ "group by gender																		\n";
        	
        //sql += "group by a.birth_date                       \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("gender"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���� ȸ�� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberAge(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                           \n";
        sql += "      decode(nvl(a.age, ' '), ' ', '��Ÿ', a.age) age                                                                             \n";
        sql += "    , count(nvl(a.age, ' ')) cnt                                                                                                 \n";
        sql += "  from                                                                                                                           \n";
        sql += "       (                                                                                                                         \n";
        sql += "     select                                                                                                                      \n";
        sql += "              case when to_char(sysdate, 'yyyy') - b.byyyy <= 5 then '01~05��'                                                   \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy >  5 and to_char(sysdate, 'yyyy') - b.byyyy <= 10 then '06~10��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 10 and to_char(sysdate, 'yyyy') - b.byyyy <= 15 then '11~15��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 15 and to_char(sysdate, 'yyyy') - b.byyyy <= 20 then '16~20��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 20 and to_char(sysdate, 'yyyy') - b.byyyy <= 25 then '21~25��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 25 and to_char(sysdate, 'yyyy') - b.byyyy <= 30 then '26~30��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 30 and to_char(sysdate, 'yyyy') - b.byyyy <= 35 then '31~35��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 35 and to_char(sysdate, 'yyyy') - b.byyyy <= 40 then '36~40��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 40 and to_char(sysdate, 'yyyy') - b.byyyy <= 45 then '41~45��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 45 and to_char(sysdate, 'yyyy') - b.byyyy <= 50 then '46~50��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 50 and to_char(sysdate, 'yyyy') - b.byyyy <= 55 then '51~55��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 55 and to_char(sysdate, 'yyyy') - b.byyyy <= 60 then '56~60��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 60 and to_char(sysdate, 'yyyy') - b.byyyy <= 65 then '61~65��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 65 and to_char(sysdate, 'yyyy') - b.byyyy <= 70 then '66~70��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 70 and to_char(sysdate, 'yyyy') - b.byyyy <= 75 then '71~75��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 75 and to_char(sysdate, 'yyyy') - b.byyyy <= 80 then '76~80��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 80 and to_char(sysdate, 'yyyy') - b.byyyy <= 85 then '81~85��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 85 and to_char(sysdate, 'yyyy') - b.byyyy <= 90 then '86~90��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 90 and to_char(sysdate, 'yyyy') - b.byyyy <= 95 then '91~95��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 95 and to_char(sysdate, 'yyyy') - b.byyyy <= 100 then '96~100��'    \n";
        sql += "                   else ''                                                                                                       \n";
        sql += "               end age                                                                                                           \n";
        sql += "       from                                                                                                                      \n";
        sql += "            (                                                                                                                    \n";
        sql += "             select                                                                                                              \n";
        sql += "                     decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '' ) byyyy                                                                      \n";
        sql += "               from                                                                                                              \n";
        sql += "                     tz_member a                                                                                                 \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            //sql += "    ,   vz_orgmember    b                  \n";
        } else {
            //sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            //sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            //sql += "   and a.userid  = b.userid                \n";
        } else {
            //sql += "   where a.userid  = b.userid(+)             \n";
        }
        sql += "   where 1= 1  \n";
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        sql += "            ) b                                                                                                                  \n";
        sql += "    ) a                                                                                                                          \n";
        sql += " group by age                                                                                                                    \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("age"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ������ ȸ�� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberNationality(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql += "select                              \n";
        sql += "        a.codenm,                   \n";
        sql += "        count(b.userid) cnt         \n";
        sql += "  from                              \n";
        sql += "        tz_code a,                  \n";
        sql += "        tz_member b                 \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and b.userid  = c.userid                \n";
        } else {
            sql += "   where b.userid  = c.userid(+)             \n";
        }
        
        sql += "   and a.gubun = '0068'            \n";

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        sql += "    and a.code = b.NATIONALITY   \n";
        sql += " group by a.codenm                  \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("codenm"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * �ڰ���з� ȸ�� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberLicense(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                      \n";
        sql += "       a.codenm,                                                                                            \n";
        sql += "       (                                                                                                    \n";
        sql += "        select                                                                                              \n";
        sql += "               count(b.license_choice)                                                                      \n";
        sql += "          from                                                                                              \n";
        sql += "               tz_member b                                                                                  \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and b.userid  = c.userid                \n";
        } else {
            sql += "   where b.userid  = c.userid(+)             \n";
        }

        sql += "           and instr(b.license_choice, a.code) > 0        \n";
        
        if ( !box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        sql += "       ) cnt                                                                                                \n";
        sql += "  from                                                                                                      \n";
        sql += "       tz_code a                                                                                            \n";
        sql += " where                                                                                                      \n";
        sql += "       a.gubun = '0062'                                                                                     \n";
        sql += "union all                                                                                                   \n";
        sql += "select                                                                                                      \n";
        sql += "       '���4�' codenm,                                                                                      \n";
        sql += "       count(a.userid) cnt                                                                                    \n";
        sql += "  from                                                                                                      \n";
        sql += "       tz_member a                                                                                            \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        sql += "   and a.license_choice is null                                                                               \n";

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("codenm"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * �зº� ȸ�� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberAchievement(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                                  \n";
        sql += "       decode(a.achievement, '', '���4�', (select codenm from tz_code where gubun='0063' and code = a.achievement)) achievement,     \n";
        sql += "       count(nvl(a.achievement, '0')) cnt                                                                                               \n";
        sql += "  from                                                                                                                                  \n";
        sql += "       tz_member a                                                                                                                      \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += " group by                                                                                                                               \n";
        sql += "       a.achievement                                                                                                                    \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("achievement"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ��� ȸ�� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberJikup(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                                  \n";
        sql += "       decode(a.jikup, '', '���4�', (select codenm from tz_code where gubun='0064' and code = a.jikup)) jikup,                       \n";
        sql += "       count(nvl(a.jikup, '0')) cnt                                                                                                     \n";
        sql += "  from                                                                                                                                  \n";
        sql += "       tz_member a                                                                                                                      \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }

        sql += " group by                                                                                                                               \n";
        sql += "       a.jikup                                                                                                                          \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("jikup"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���Ե��⺰ ȸ�� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberEntrytype(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                                  \n";
        sql += "       decode(a.entrytype, '', '���4�', (select codenm from tz_code where gubun='0065' and code = a.entrytype)) entrytype,           \n";
        sql += "       count(nvl(a.entrytype, '0')) cnt                                                                                                 \n";
        sql += "  from                                                                                                                                  \n";
        sql += "       tz_member a                                                                                                                      \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        sql += " group by                                                                                                                               \n";
        sql += "       a.entrytype                                                                                                                      \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("entrytype"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���԰�κ� ȸ�� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberEntrypath(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                                              \n";
        sql += "       decode(a.entrypath, '', '���4�', 'OM', '��xȸ��', (select codenm from tz_code where gubun='0066' and code = a.entrypath)) entrypath,     \n";
        sql += "       count(nvl(a.entrypath, '0')) cnt                                                                                                             \n";
        sql += "  from                                                                                                                                              \n";
        sql += "       tz_member a                                                                                                                                  \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        sql += " group by                                                                                                                                           \n";
        sql += "       a.entrypath                                                                                                                                  \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("entrypath"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ��ȥ���κ� ȸ�� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberIswedding(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                          \n";
        sql += "       decode(a.iswedding, '', '���4�', 'N', '��ȥ', 'Y', '��ȥ', a.iswedding) iswedding,        \n";
        sql += "       count(nvl(a.iswedding, 0)) cnt                                                             \n";
        sql += "  from                                                                                          \n";
        sql += "       tz_member a                                                                                \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += " group by                                                                                       \n";
        sql += "       a.iswedding                                                                                \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("iswedding"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * �õ��� ��� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudent(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += " select                                                    \n";
        sql += "        nvl(b.sido, '��Ÿ') sido,                                            \n";
        sql += "        count(nvl(b.sido, ' ')) cnt                                  \n";
        sql += "   from                                                    \n";
        sql += "        tz_member a                                       \n";
        sql += "      , (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               c                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += "    and a.userid = c.userid                                \n";
        sql += "  group by                                                 \n";
        sql += "        b.sido                                             \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("sido"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ������ ��� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentGugun(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";
        String v_sido = box.getString("p_sido");

        sql = "";
        sql += "select                                         \n";
        sql += "      nvl(b.gugun, ' ') gugun,                                 \n";
        sql += "      count(nvl(b.gugun, ' ')) cnt                       \n";
        sql += "  from                                         \n";
        sql += "      tz_member a                             \n";
        sql += "     , (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               c                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        sql += "   and b.sido like '%" + v_sido
                + "%'                        \n";
        sql += "   and a.userid = c.userid                     \n";
        sql += " group by                                      \n";
        sql += "      b.sido, b.gugun                          \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("gugun"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���� ��� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentDong(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";
        String v_sido = box.getString("p_sido");
        String v_gugun = box.getString("p_gugun");

        sql = "";
        sql += "select                                         \n";
        sql += "      nvl(b.dong, ' ') dong,                                 \n";
        sql += "      count(nvl(b.dong, ' ')) cnt                       \n";
        sql += "  from                                         \n";
        sql += "      tz_member a                             \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }

        sql += "   ,   (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               c                               \n";
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        sql += "   and b.sido like '%" + v_sido
                + "%'                        \n";
        sql += "   and b.gugun like '%" + v_gugun
                + "%'                        \n";
        sql += "   and a.userid = c.userid                     \n";
        sql += " group by                                      \n";
        sql += "      b.dong                          \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("dong"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���� ��� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentSex(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                   \n";
        sql += "       decode(nvl(a.gender, ' '), 'F', '����', 'M', '����', '��Ÿ') gender        \n";
        sql += "     , count(nvl(a.gender, ' ')) cnt                                               \n";
        sql += "  from                                                                   \n";
        sql += "       tz_member a                                                       \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }

        sql += "     ,  (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               c                               \n";
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        sql += " and a.userid = c.userid                                      \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }

        sql += "group by a.gender                                                          \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("gender"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ��� ��� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentAge(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                               \n";
        sql += "      decode(nvl(a.age, ' '), ' ', '��Ÿ', a.age) age                                                                                                                       \n";
        sql += "    , count(nvl(a.age, ' ')) cnt                                                                                                               \n";
        sql += "  from                                                                                                                               \n";
        sql += "       (                                                                                                                         \n";
        sql += "     select                                                                                                                      \n";
        sql += "              case when to_char(sysdate, 'yyyy') - b.byyyy <= 5 then '01~05��'                                                   \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy >  5 and to_char(sysdate, 'yyyy') - b.byyyy <= 10 then '06~10��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 10 and to_char(sysdate, 'yyyy') - b.byyyy <= 15 then '11~15��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 15 and to_char(sysdate, 'yyyy') - b.byyyy <= 20 then '16~20��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 20 and to_char(sysdate, 'yyyy') - b.byyyy <= 25 then '21~25��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 25 and to_char(sysdate, 'yyyy') - b.byyyy <= 30 then '26~30��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 30 and to_char(sysdate, 'yyyy') - b.byyyy <= 35 then '31~35��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 35 and to_char(sysdate, 'yyyy') - b.byyyy <= 40 then '36~40��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 40 and to_char(sysdate, 'yyyy') - b.byyyy <= 45 then '41~45��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 45 and to_char(sysdate, 'yyyy') - b.byyyy <= 50 then '46~50��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 50 and to_char(sysdate, 'yyyy') - b.byyyy <= 55 then '51~55��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 55 and to_char(sysdate, 'yyyy') - b.byyyy <= 60 then '56~60��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 60 and to_char(sysdate, 'yyyy') - b.byyyy <= 65 then '61~65��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 65 and to_char(sysdate, 'yyyy') - b.byyyy <= 70 then '66~70��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 70 and to_char(sysdate, 'yyyy') - b.byyyy <= 75 then '71~75��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 75 and to_char(sysdate, 'yyyy') - b.byyyy <= 80 then '76~80��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 80 and to_char(sysdate, 'yyyy') - b.byyyy <= 85 then '81~85��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 85 and to_char(sysdate, 'yyyy') - b.byyyy <= 90 then '86~90��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 90 and to_char(sysdate, 'yyyy') - b.byyyy <= 95 then '91~95��'      \n";
        sql += "                   when to_char(sysdate, 'yyyy') - b.byyyy > 95 and to_char(sysdate, 'yyyy') - b.byyyy <= 100 then '96~100��'    \n";
        sql += "                   else ''                                                                                                       \n";
        sql += "               end age                                                                                                           \n";
        sql += "               , userid                                                                                                           \n";
        sql += "       from                                                                                                                      \n";
        sql += "            (                                                                                                                    \n";
        sql += "             select                                                                                                              \n";
        sql += "                     decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)                                                  \n";
        sql += "                                               , '' ) byyyy                                                \n";
        sql += "                     , a.userid                                                \n";
        sql += "               from                                                                                                              \n";
        sql += "                     tz_member a                                                                                                   \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += "            ) b                                                                                                                  \n";
        sql += "    ) a                                                                                                                          \n";
        sql += "    ,  (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               c                               \n";
        sql += " where                         \n";
        sql += "       a.userid = c.userid                      \n";
        sql += " group by age                                                                                                                        \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("age"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ������ ��� ����
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentNationality(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql += "select                                                             \n";
        sql += "      c.codenm                                                     \n";
        sql += "    , nvl(d.cnt, 0) cnt                                            \n";
        sql += "  from                                                             \n";
        sql += "     tz_code c,                                                    \n";
        sql += "     (                                                             \n";
        sql += "       select                                                      \n";
        sql += "             count(a.userid) cnt,                                  \n";
        sql += "             a.nationality                                         \n";
        sql += "         from                                                      \n";
        sql += "             tz_member a,                                          \n";
        sql += "        (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               b                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = c.userid                \n";
        } else {
            sql += "   where a.userid  = c.userid(+)             \n";
        }
        
        sql += "       and      a.userid = b.userid                                   \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }

        sql += "        group by                                                   \n";
        sql += "             a.nationality                                         \n";
        sql += "     ) d                                                           \n";
        sql += " where                                                             \n";
        sql += "      c.gubun = '0068'                                             \n";
        sql += "   and c.code = d.nationality                                   \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        } else {
            sql += " order by c.codenm                                                 \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("codenm"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * �ڰ���з� ��� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentLicense(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                      \n";
        sql += "       a.codenm,                                                            \n";
        sql += "       (                                                                    \n";
        sql += "        select                                                              \n";
        sql += "               count(b.license_choice)                                      \n";
        sql += "          from                                                              \n";
        sql += "               tz_member b,                                                 \n";
        sql += "        (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               c                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    d                  \n";
        } else {
            sql += "    ,   vz_orgmember1   d                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where d.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and b.userid  = d.userid                \n";
        } else {
            sql += "   where b.userid  = d.userid(+)             \n";
        }
        
        sql += "       and instr(b.license_choice, a.code) > 0                          \n";        

        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += "           and b.userid = c.userid                                          \n";
        sql += "       ) cnt                                                                \n";
        sql += "  from                                                                      \n";
        sql += "       tz_code a                                                            \n";
        sql += " where                                                                      \n";
        sql += "       a.gubun = '0062'                                                     \n";
        sql += "union all                                                                   \n";
        sql += "select                                                                      \n";
        sql += "       '�4����' codenm,                                                   \n";
        sql += "       count(b.userid) cnt                                                  \n";
        sql += "  from                                                                      \n";
        sql += "       tz_member a,                                                         \n";
        sql += "        (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               b                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = c.userid                \n";
        } else {
            sql += "   where a.userid  = c.userid(+)             \n";
        }
        
        sql += "    and license_choice is null                                               \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += "   and a.userid = b.userid                                                  \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("codenm"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * �зº� ��� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentAchievement(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                                      \n";
        sql += "       decode(a.achievement, '', '���4�', (select codenm from tz_code where gubun='0063' and code = a.achievement)) achievement,         \n";
        sql += "       count(nvl(a.achievement, '0')) cnt                                                                                                   \n";
        sql += "  from                                                                                                                                      \n";
        sql += "       tz_member a,                                                                                                                         \n";
        sql += "        (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               b                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = c.userid                \n";
        } else {
            sql += "   where a.userid  = c.userid(+)             \n";
        }

        sql += "   and a.userid = b.userid                                                                                                                  \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += " group by                                                                                                                                   \n";
        sql += "       a.achievement                                                                                                                        \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("achievement"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ��� ��� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentJikup(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                                      \n";
        sql += "       decode(a.jikup, '', '���4�', (select codenm from tz_code where gubun='0064' and code = a.jikup)) jikup,                           \n";
        sql += "       count(nvl(a.jikup, '0')) cnt                                                                                                         \n";
        sql += "  from                                                                                                                                      \n";
        sql += "       tz_member a,                                                                                                                         \n";
        sql += "        (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               b                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = c.userid                \n";
        } else {
            sql += "   where a.userid  = c.userid(+)             \n";
        }

        sql += "    and a.userid = b.userid                                                                                                                  \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += " group by                                                                                                                                   \n";
        sql += "       a.jikup                                                                                                                              \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("jikup"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���Ե��⺰ ��� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentEntrytype(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                                      \n";
        sql += "       decode(a.entrytype, '', '���4�', (select codenm from tz_code where gubun='0065' and code = a.entrytype)) entrytype,               \n";
        sql += "       count(nvl(a.entrytype, '0')) cnt                                                                                                     \n";
        sql += "  from                                                                                                                                      \n";
        sql += "       tz_member a,                                                                                                                         \n";
        sql += "        (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               b                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = c.userid                \n";
        } else {
            sql += "   where a.userid  = c.userid(+)             \n";
        }

        sql += "      and a.userid = b.userid                                                                                                                  \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += " group by                                                                                                                                   \n";
        sql += "       a.entrytype                                                                                                                          \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("entrytype"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ���԰�κ� ��� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentEntrypath(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                                                                              \n";
        sql += "       decode(a.entrypath, '', '���4�', 'OM', '��xȸ��', (select codenm from tz_code where gubun='0066' and code = a.entrypath)) entrypath,     \n";
        sql += "       count(nvl(a.entrypath, '0')) cnt                                                                                                             \n";
        sql += "  from                                                                                                                                              \n";
        sql += "       tz_member a,                                                                                                                                 \n";
        sql += "        (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               b                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = c.userid                \n";
        } else {
            sql += "   where a.userid  = c.userid(+)             \n";
        }
        
        sql += "      and a.userid = b.userid                                                                                                                          \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += " group by                                                                                                                                           \n";
        sql += "       a.entrypath                                                                                                                                  \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("entrypath"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ��ȥ���κ� ��� ���
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsStudentIswedding(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                                              \n";
        sql += "       decode(a.iswedding, '', '���4�', 'N', '��ȥ', 'Y', '��ȥ', a.iswedding) iswedding,        \n";
        sql += "       count(nvl(a.iswedding, 0)) cnt                                                               \n";
        sql += "  from                                                                                              \n";
        sql += "       tz_member a,                                                                                 \n";
        sql += "        (                                               \n";
        sql += "            SELECT b.UserId                             \n";
        sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
        sql += "                ,  Tz_Student            b              \n";
        sql += "            WHERE  a.grcode  = 'N000001'                \n";
        sql += "            AND    a.subj    = b.subj                   \n";
        sql += "            AND    a.subjseq = b.subjseq                \n";
        sql += "            AND    a.year    = b.Year                   \n";
        sql += "        )               b                               \n";
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    c                  \n";
        } else {
            sql += "    ,   vz_orgmember1   c                  \n";
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where c.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = c.userid                \n";
        } else {
            sql += "   where a.userid  = c.userid(+)             \n";
        }
        
        sql += "      and a.userid = b.userid                                                                          \n";
        
        if (!box.getString("p_sdate").equals("")
                && !box.getString("p_ldate").equals("")) {
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        sql += " group by                                                                                           \n";
        sql += "       a.iswedding                                                                                    \n";

        if (box.getString("p_orderStr").equals("cnt")) {
            sql += " order by cnt " + box.getString("p_orderType")
                    + "        \n";
        }

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("sido", ls.getString("iswedding"));
                dbox.put("cnt", ls.getString("cnt"));
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

    /**
     * ȸ����Ȳ ����˻�
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList performStatisticsMemberCross(RequestBox box)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;

        String c_sido = box.getString("c_sido");
        String c_gugun = box.getString("c_gugun");
        String c_gender = box.getString("c_gender");
        String c_achievement = box.getString("c_achievement");
        String c_age = box.getString("c_age");
        String v_typeU = box.getString("p_typeU");
        
        String v_orgcode = box.getStringDefault("p_orgcode", "ALL");  // v���ڵ�
        
        String sql = "";

        sql = "";
        sql += "select                                                                         \n";
        sql += "       '1'                                                                         \n";
        
        if (c_sido.equals("Y")) {  // �õ�����
            sql += "       ,nvl(b.sido, '��Ÿ') sido                                                 \n";
//            sql += "       ,count(nvl(b.sido, ' ')) cnt                                            \n";
        }
        
        if (c_gugun.equals("Y")) {  // ��������
            sql += "      ,nvl(b.gugun, '��Ÿ') gugun\n";
//            sql += "       ,count(nvl(b.gugun, ' ')) cnt                                            \n";
        }
        
        if (c_gender.equals("Y")) {  // ��������
            sql += "      ,decode(nvl(a.gender, ' '), 'F', '����', 'M', '����', '��Ÿ(�ܱ���)') gender \n";
//            sql += "       ,count(nvl(a.gender, ' ')) cnt                                            \n";
        }
        
        if (c_achievement.equals("Y")) {  // �з¼���
            sql += "      ,decode(a.achievement, '', '���4�', (select codenm from tz_code where gubun='0063' and code = a.achievement)) achievement \n";     
//            sql += "       ,count(nvl(a.achievement, ' ')) cnt                                            \n";
        }
        
        if (c_age.equals("Y")) {  // ���ɼ���
            sql += "      ,decode(nvl(f.age, ' '), ' ', '��Ÿ', f.age) age\n";
//            sql += "       ,count(nvl(f.age, ' ')) cnt                                            \n";
        }
      sql += "       ,count(*) cnt                                                             \n";
        
        sql += "  from                                                                         \n";
        sql += "       tz_member a                                                             \n";
        
        
        if ( !v_orgcode.equals("ALL") ) { 
            sql += "    ,   vz_orgmember    b                  \n";
        } else {
            sql += "    ,   vz_orgmember1   b                  \n";
        }
        
        if (c_age.equals("Y")) {  // ���ɼ���
            sql += "       ,(\n";
            sql += "         select\n";
            sql += "                userid,\n";
            sql += "                case when to_char(sysdate, 'yyyy') - b.byyyy <= 5 then '01~05��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy >  5 and to_char(sysdate, 'yyyy') - b.byyyy <= 10 then '06~10��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 10 and to_char(sysdate, 'yyyy') - b.byyyy <= 15 then '11~15��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 15 and to_char(sysdate, 'yyyy') - b.byyyy <= 20 then '16~20��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 20 and to_char(sysdate, 'yyyy') - b.byyyy <= 25 then '21~25��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 25 and to_char(sysdate, 'yyyy') - b.byyyy <= 30 then '26~30��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 30 and to_char(sysdate, 'yyyy') - b.byyyy <= 35 then '31~35��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 35 and to_char(sysdate, 'yyyy') - b.byyyy <= 40 then '36~40��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 40 and to_char(sysdate, 'yyyy') - b.byyyy <= 45 then '41~45��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 45 and to_char(sysdate, 'yyyy') - b.byyyy <= 50 then '46~50��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 50 and to_char(sysdate, 'yyyy') - b.byyyy <= 55 then '51~55��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 55 and to_char(sysdate, 'yyyy') - b.byyyy <= 60 then '56~60��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 60 and to_char(sysdate, 'yyyy') - b.byyyy <= 65 then '61~65��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 65 and to_char(sysdate, 'yyyy') - b.byyyy <= 70 then '66~70��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 70 and to_char(sysdate, 'yyyy') - b.byyyy <= 75 then '71~75��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 75 and to_char(sysdate, 'yyyy') - b.byyyy <= 80 then '76~80��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 80 and to_char(sysdate, 'yyyy') - b.byyyy <= 85 then '81~85��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 85 and to_char(sysdate, 'yyyy') - b.byyyy <= 90 then '86~90��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 90 and to_char(sysdate, 'yyyy') - b.byyyy <= 95 then '91~95��'\n";
            sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 95 and to_char(sysdate, 'yyyy') - b.byyyy <= 100 then '96~100��'\n";
            sql += "                     else ''\n";
            sql += "                 end age\n";
            sql += "           from\n";
            sql += "                (\n";
            sql += "                 select\n";
            sql += "                         userid, \n";
            sql += "                         decode(substr(birth_date, 7, 1), '9', '18'||substr(birth_date, 0, 2)\n";
            sql += "                                                   , '0', '18'||substr(birth_date, 0, 2)\n";
            sql += "                                                   , '1', '19'||substr(birth_date, 0, 2)\n";
            sql += "                                                   , '2', '19'||substr(birth_date, 0, 2)\n";
            sql += "                                                   , '3', '20'||substr(birth_date, 0, 2)\n";
            sql += "                                                   , '4', '20'||substr(birth_date, 0, 2)\n";
            sql += "                                                   , '' ) byyyy\n";
            sql += "                   from\n";
            sql += "                         tz_member\n";
            sql += "                ) b\n";
            sql += "       ) f           \n";
        }
        if(v_typeU.equals("2")) {
            sql += "       ,(                                               \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               g                               \n";
            
        }
        
        if ( !v_orgcode.equals("ALL") ) {   // v����
            sql += "   where b.orgcode = " + SQLString.Format(v_orgcode) + "  \n";
            sql += "   and a.userid  = b.userid                \n";
        } else {
            sql += "   where a.userid  = b.userid(+)             \n";
        }
        
        if (c_age.equals("Y")) {  // ���ɼ���
            sql += "   and a.userid = f.userid                               \n";
        }
        
        if (!box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("")) { // �Ⱓ����
            if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                        + box.getString("p_sdate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                        + box.getString("p_ldate")
                        + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
            }
        }
        
        if(v_typeU.equals("2")) {
            sql += "   and a.userid = g.userid                                    \n";
        }
        
        sql += " group by                                                                      \n";
        sql += "       '1'                                                                      \n";        
        if (c_sido.equals("Y")) {  // �õ�����
            sql += "      ,b.sido                                                                  \n";
        }
        if (c_gugun.equals("Y")) {  // ��������
            sql += "      ,b.gugun\n";
        }
        if (c_gender.equals("Y")) {  // ��������
            sql += "      ,a.gender\n";
        }
        if (c_achievement.equals("Y")) {  // �з¼���
            sql += "      ,a.achievement \n";
        }
        if (c_age.equals("Y")) {  // ���ɼ���
            sql += "      ,f.age                               \n";
        }
        if (!box.getString("p_orderStr").equals("")) {
            sql += " order by " + box.getString("p_orderStr") + " "
                    + box.getString("p_orderType") + "        \n";
        } else {
            sql += " order by                                                                      \n";
            sql += "       '1'                                                                      \n";
            if (c_sido.equals("Y")) {  // �õ�����
                sql += "      ,b.sido                                                                  \n";
            }
            if (c_gugun.equals("Y")) {  // ��������
                sql += "      ,b.gugun\n";
            }
            if (c_gender.equals("Y")) {  // ��������
                sql += "      ,a.gender\n";
            }
            if (c_achievement.equals("Y")) {  // �з¼���
                sql += "      ,a.achievement \n";
            }
            if (c_age.equals("Y")) {  // ���ɼ���
                sql += "      ,f.age                               \n";
            }
        }

        try {
            if(box.getString("p_action").equals("GO")){
                connMgr = new DBConnectionManager();
                list = new ArrayList();
    
                System.out.println(sql);
                ls = connMgr.executeQuery(sql);
    
                while (ls.next()) {
                    dbox = ls.getDataBox();
                    list.add(dbox);
                }
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return list;
    }

}