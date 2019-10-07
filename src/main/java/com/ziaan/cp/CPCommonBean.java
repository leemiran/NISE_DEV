// **********************************************************
//  1. f      ��: ���뵥���Ͱ� ��
//  2. �wα׷���: CPCommonBean.java
//  3. ��      ��: ���뵥���Ͱ� �wα׷�
//  4. ȯ      ��: JDK 1.3
//  5. ��      o: 1.0
//  6. ��      ��: ��â�� 2004. 12. 22
//  7. ��      d:
// **********************************************************

package com.ziaan.cp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.beta.BPCourseBean;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
*���뵥���Ͱ�
*<p> f��:CPCommonBean.java</p> 
*<p> ����:���뵥���Ͱ� ��</p> 
*<p> Copyright: Copright(c)2004</p> 
*<p> Company: VLC</p> 
*@author ��â��
*@version 1.0
*/

public class CPCommonBean { 
    private ConfigSet config;
    private int row;
	    	
    public CPCommonBean() { }

    /**
    ���־�ü ����Ʈ ��d
    @param box          receive from the form object and session
    @param isChange		������;�� ������ ���ε忩��
    @param isALL		ALL ǥ�ÿ���
    @return String   	���־�ü����Ʈ ����Ʈ�±�
    */
	public static String getCP(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = " ";
        String v_cp = "";

        try {   
            v_cp = box.getString("p_cp");
            // System.out.println("v_cp ==  ==  ==  == = >>  >>  >> " +v_cp);
            
	    	connMgr = new DBConnectionManager();                 
            
            String s_gadmin = box.getSession("gadmin");
            String s_userid = box.getSession("userid");
            
            String v_gadmin = s_gadmin.substring(0,1);
            
            
            // ���־�ü ����Ʈ
            if ( s_gadmin.equals("S1") || s_gadmin.equals("T1") || v_gadmin.equals("M") ) { 
            	// ���־�ü ������ϰ��(�ش��ü�� d����������)
	            sql = "select cpseq, cpnm ";
	            sql += " from tz_cpinfo where userid = " + SQLString.Format(s_userid);	
	            sql += " order by cpnm";
	            isALL = false;
           	}
            else{ 
            	// ���־�ü ����ڰ��ƴҰ��(����ü d���� ������)
	            sql = "select cpseq, cpnm ";
	            sql += " from tz_cpinfo ";	
	            sql += " order by cpnm";
	        }
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);

            result += getSelectTag( ls, isChange, isALL, "p_cp", v_cp);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /**
    �⵵ ��n�1�
    @param box          receive from the form object and session
    @param isChange		������;�� ������ ���ε忩��
    @param isALL		ALL ǥ�ÿ���
    @param syear		���۳⵵
    @param selname		����Ʈ�ڽ� ����
    @return String   	�⵵ ����Ʈ�±�
    */
	public static String getYear(RequestBox box, boolean isChange, boolean isALL, String syear, String selname) throws Exception { 
		StringBuffer sb = null;
        String ss_year = "";
        int v_year = 0;
        String sb_year = "";
        
        try { 
            sb = new StringBuffer();  

			ss_year = box.getString("p_year");
			
			// if ( ss_year == "") ss_year = StringManager.toInt(FormatDate.getDate("yyyy") );
			if ( ss_year.equals("") ) { 
				ss_year = FormatDate.getDate("yyyy");				
			}
			v_year = StringManager.toInt(syear);
			sb_year = "" + v_year;
			
            sb.append(" <select name = \"" + selname + "\"");
            if ( isChange) sb.append(" onChange = \"whenSelection('change')\"");  
            sb.append(" > \r\n");
            if ( isALL) { 
                sb.append("<option value = \"ALL\" > ALL</option > \r\n");  
            }
            // else if ( isChange) { 
            //    if ( selname.indexOf("year") == -1) 
            //        sb.append("<option value = \"----\" > ----</option > \r\n");  
            // }
			
            while ( v_year<=StringManager.toInt(FormatDate.getDate("yyyy")) +1) {    

                sb.append("<option value = \"" + v_year + "\"");

                if ( sb_year.equals(ss_year)) sb.append(" selected");
               
                sb.append(" > " + v_year + " ��</option > \r\n");
                
                v_year = v_year + 1;
                sb_year = "" + v_year;
                
            }
            
            sb.append("</select > \r\n");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }

        return sb.toString();
	}

    /**
    ���� ��ü select box �±�
    @param ls          ȭ��d�� ����Ʈ��
    @param isChange		������;�� ������ ���ε忩��
    @param isALL		ALL ǥ�ÿ���
    @param selname		����Ʈ�ڽ� ����    
    @param optionselected		���ÿ���
    @return String   	�⵵ ����Ʈ�±�
    */
    public static String getSelectTag(ListSet ls, boolean isChange, boolean isALL, String selname, String optionselected) throws Exception { 
        StringBuffer sb = null;
        
        try { 
            sb = new StringBuffer();  

            sb.append("<select name = \"" + selname + "\"");
            if ( isChange) sb.append(" onChange = \"whenSelection('change')\"");  
            sb.append(" > \r\n");
            if ( isALL) { 
                sb.append("<option value = \"ALL\" > ALL</option > \r\n");  
            } else if ( isChange) { 
                    // if ( selname.indexOf("year") == -1) 
                    // sb.append("<option value = \"----\" >== ���� == </option > \r\n");  
            }

            while ( ls.next() ) {    
                ResultSetMetaData meta = ls.getMetaData();
                int columnCount = meta.getColumnCount();
                
                sb.append("<option value = \"" + ls.getString(1) + "\"");

                if ( optionselected.equals( ls.getString(1))) sb.append(" selected");
               
                sb.append(" > " + ls.getString(columnCount) + "</option > \r\n");
            }
            sb.append("</select > \r\n");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }

        return sb.toString();
    }


    /**
    ���� ��ü select box �±�
    @param ls          ȭ��d�� ����Ʈ��
    @param isChange		������;�� ������ ���ε忩��
    @param isALL		ALL ǥ�ÿ���
    @param selname		����Ʈ�ڽ� ����    
    @param optionselected		���ÿ���
    @return String   	�⵵ ����Ʈ�±�
    */
    public static String getSelectCourse(RequestBox box, boolean isChange, boolean isALL, String selname, String optionselected) throws Exception { 
        StringBuffer sb = null;
		BPCourseBean bean = new BPCourseBean();
		ArrayList list     = bean.selectCourseList(box);

        try { 
            sb = new StringBuffer();  

            sb.append("<select name = \"" + selname + "\"");
            if ( isChange) sb.append(" onChange = \"whenSelection('change')\"");  
            sb.append(" > \r\n");
            if ( isALL) { 
                sb.append("<option value = \"ALL\" > ALL</option > \r\n");  
            } else if ( isChange) { 
                if ( selname.indexOf("year") == -1) 
                    sb.append("<option value = \"----\" >== ���� == </option > \r\n");  
            }
			
			if ( list != null && list.size() > 0) { 

				for ( int i = 0; i < list.size(); i++ ) { 
					DataBox dbox = (DataBox)list.get(i);  

					sb.append("<option value = \"" + dbox.getString("d_subj") + "\"");

					if ( optionselected.equals(dbox.getString("d_subj"))) sb.append(" selected");
				   
					sb.append(" > " + dbox.getString("d_subjnm") + "</option > \r\n");
				}
				sb.append("</select > \r\n");
			}
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }

        return sb.toString();
    }

    
    /**
    *  ����� ������ ����Ʈ�ڽ� (RequestBox, ����Ʈ�ڽ���,���ð�,�̺�Ʈ��)
	*  TZ_SULPAPER �̿�
    */
    public static String getSulpaperSelect(String p_grcode, String p_gyear, String p_subj, String name, int selected, String event) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";
        DataBox     dbox = null;

        result = "  <SELECT name=" + name + " " + event + " > \n";
        result += " <option value='0' > ������ �����ϼ���.</option > \n";

        try { 
            connMgr = new DBConnectionManager();
			sql = "select grcode,       subj,         \n";
            sql += "       sulpapernum,  sulpapernm, year, \n";
            sql += "       totcnt,       sulnums, sulmailing, sulstart, sulend, \n";
            sql += "       'COMMON'      subjnm \n";
            sql += "  from tz_sulpaper  \n";
            sql += " where grcode = " + StringManager.makeSQL(p_grcode);
            sql += "   and subj   = " +StringManager.makeSQL(p_subj);
            sql += "   and year   = " + StringManager.makeSQL(p_gyear);
            sql += " order by subj, sulpapernum asc";
            
            System.out.println("sql == = >>  > " +sql);
            ls = connMgr.executeQuery(sql);
            
            String v_null_test = "";
            String v_subj_bef = "";
            
            while ( ls.next() ) { 

                    dbox = ls.getDataBox();

                result += " <option value=" + dbox.getInt("d_sulpapernum");
                if ( selected == dbox.getInt("d_sulpapernum") ) { 
                    result += " selected ";
                }
                
                result += " > " + dbox.getString("d_sulpapernm") + "</option > \n";
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

}