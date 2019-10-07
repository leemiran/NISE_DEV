
package com.ziaan.common;

import java.util.*;
import com.ziaan.system.*;
import com.ziaan.library.*;

public class TableCountBean
{
  
  
    public int getExamAnswerCnt(int p_papernum, String p_userid, int p_examnum, int p_paramcnt) throws Exception {
        Hashtable cntdata = new Hashtable();
        cntdata.put("seqtable","tu_examanswer");
        if(p_paramcnt == 3){
        cntdata.put("paramcnt","3");
        cntdata.put("param0","papernum");
        cntdata.put("param1","userid");
        cntdata.put("param2","examnum");
        cntdata.put("papernum",  SQLString.Format(String.valueOf(p_papernum)));
        cntdata.put("userid",    SQLString.Format(String.valueOf(p_userid)));
        cntdata.put("examnum",   SQLString.Format(String.valueOf(p_examnum)));
        }else if(p_paramcnt == 2){
        cntdata.put("paramcnt","2");
        cntdata.put("param0","papernum");
        cntdata.put("param1","userid");
        cntdata.put("papernum",  SQLString.Format(String.valueOf(p_papernum)));
        cntdata.put("userid",    SQLString.Format(String.valueOf(p_userid)));
        }
        return SelectionUtil.getTableCount(cntdata);
    }
    
    public int getExamResultCnt(int p_papernum, String p_userid) throws Exception {
        Hashtable cntdata = new Hashtable();
        cntdata.put("seqtable","tu_examresult");
        cntdata.put("paramcnt","2");
        cntdata.put("param0","papernum");
        cntdata.put("param1","userid");
        cntdata.put("papernum",  SQLString.Format(String.valueOf(p_papernum)));
        cntdata.put("userid",    SQLString.Format(String.valueOf(p_userid)));
        return SelectionUtil.getTableCount(cntdata);
    }
    

    public int getExamByPaperCnt(int p_papernum) throws Exception {
        DataBox dbox = null;
        int cnt = 0;
        DatabaseExecute db = null;
        
        try {     
            db = new DatabaseExecute();
               
            String sql = "select examcnt from tu_exampaper where papernum ="+p_papernum;
            dbox = db.executeQuery(null, sql);      
            cnt = dbox.getInt("d_examcnt");
        }
        catch (Exception ex) {             
            ErrorManager.getErrorStackTrace(ex, null, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return cnt;
    }

}