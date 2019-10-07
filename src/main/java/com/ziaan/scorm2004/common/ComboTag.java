package com.ziaan.scorm2004.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;

public class ComboTag extends TagSupport
{
    private String cdName = "";
    private String table = "";
    private String cdCol = "";
    private String nmCol = "";
    private String condStmt = "";
    private String selected = "";

    public int doStartTag() throws JspException
    {
        try
        {
            JspWriter out = pageContext.getOut();
            List options = null;
            StringBuffer buff = new StringBuffer();

            options = this.getCombo(cdName, table, cdCol, nmCol, condStmt);

            if (options != null)
            {
                ComboSDO option = null;
                
                for (int i = 0; i < options.size(); i++)
                {
                    option = (ComboSDO) options.get(i);
                    
                    buff.append("<OPTION VALUE=\"" + option.getCode() + "\"");
                    if (option.getCode().equals(this.selected))
                    {
                        buff.append(" selected ");
                    }
                    buff.append(">" + option.getName() + "</OPTION>\n");
                }
            }
            // option ÅÂ±×ÀÎ¼â
            out.println(buff.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    private List getCombo(String cdName, String table, String cdCol, String nmCol, String condStmt) throws Exception
    {
        List options = null;

        if (options == null)
        {
            DBConnectionManager connMgr = null;
            ListSet ls = null;
            
            StringBuffer sb = new StringBuffer();
            
            try
            {
                connMgr = new DBConnectionManager();
                options = new ArrayList();
                
                sb.append("SELECT " + cdCol + ", " + nmCol);
                sb.append("  FROM " + table);
                sb.append(" WHERE " + condStmt);

                ls = connMgr.executeQuery(sb.toString());
    
                if (ls != null)
                {
                    int i = 0;
                    while ( ls.next() )
                    {
                        ComboSDO option = new ComboSDO();
                        option.setCode(ls.getString(1));
                        option.setName(ls.getString(2));
                        
                        options.add( option );
                        
                        i++;
                    }
                    // comboMap.put(cdName, options);
                    // pageContext.getServletContext().setAttribute("comboMap", comboMap);
                }
                
                ls.close();
            }
            catch (Exception ex) 
            {
                ErrorManager.getErrorStackTrace(ex);
                throw new Exception("sql = " + sb.toString() + "\r\n" + ex.getMessage());
            }
            finally 
            {
                if(ls != null) { try { ls.close(); }catch (Exception e) {} }
                if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
            }
        }
        return options;
    }

    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }

    public void release()
    {
        super.release();
    }

    public void setCdName(String cdName)
    {
        this.cdName = cdName;
    }

    public void setTable(String table)
    {
        this.table = table;
    }

    public void setCdCol(String cdCol)
    {
        this.cdCol = cdCol;
    }

    public void setNmCol(String nmCol)
    {
        this.nmCol = nmCol;
    }

    public void setCondStmt(String condStmt)
    {
        this.condStmt = condStmt;
    }

    public void setSelected(String selected)
    {
        this.selected = selected;
    }

}
