package com.credu.learn;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.StringTokenizer;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DataBox;
import com.ziaan.library.DatabaseExecute;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.PreparedBox;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.library.FileManager;
import com.ziaan.common.UploadUtil;

public class ReportProfBean
{
    private ConfigSet config;
    private int row;
    private String dirUploadDefault;

    public ReportProfBean()
    {
        try
        {
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row"));                               
            dirUploadDefault = config.getProperty("dir.upload.reportprof");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList selectListOrder(RequestBox box) throws Exception
    {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";
    	
        try
        {
            db = new DatabaseExecute();
          
            String v_grcode  = box.getString("p_grcode");
            String v_subj    = box.getString("p_subj");
            String v_year    = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String v_class   = box.getString("p_class");
    
            sql  = "\r\n SELECT ordseq, weeklyseq, weeklysubseq, title, startdate, expiredate,                              ";
            sql += "\r\n        score, reptype, restartdate, reexpiredate, weight,                                          ";
            sql += "\r\n        CASE WHEN                                                                                   ";
            sql += "\r\n               TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += "\r\n              BETWEEN startdate AND expiredate                                                      ";
            sql += "\r\n             THEN 'Y' ELSE 'N'                                                                      ";
            sql += "\r\n        END indate,                                                                                 ";
            sql += "\r\n        CASE WHEN                                                                                   ";
            sql += "\r\n               TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += "\r\n              BETWEEN RESTARTDATE AND reexpiredate                                                  ";
            sql += "\r\n             THEN 'Y' ELSE 'N'                                                                      ";
            sql += "\r\n        END adddate, perfectscore                                                                                 ";
            sql += "\r\n   FROM tu_projord                                                                                  ";
            sql += "\r\n  WHERE 1=1                                                                                         ";
            sql += "\n\r    AND grcode = "      + StringManager.makeSQL(v_grcode);
            sql += "\n\r    AND subj = "          + StringManager.makeSQL(v_subj);
            sql += "\n\r    AND year = "        + StringManager.makeSQL(v_year);
            sql += "\n\r    AND subjseq = "   + StringManager.makeSQL(v_subjseq);
            sql += "\n\r    AND class = "         + StringManager.makeSQL(v_class);
            sql += "\r\n    AND reptype='P' AND (DELYN = 'N' OR DELYN IS NULL) ";
            sql += "\r\n    AND reptype='P' AND (DELYN = 'N' OR DELYN IS NULL) ";
            sql += "\r\n    AND grcode='"+v_grcode+"'  and subj = '"+v_subj+"' and year='"+v_year+"' and subjseq='"+v_subjseq+"' and class='"+v_class+"' ";
            sql += "\r\n  ORDER BY year DESC, subjseq, ordseq DESC";

//System.out.println("sql::"+sql);
            list = db.executeQueryList(box, sql);
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    public ArrayList selectListOrderPerson(RequestBox box) throws Exception
    {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        try
        {
            db = new DatabaseExecute();

            String s_userid = box.getSession("userid");

            String v_grcode  = box.getString("p_grcode");
            String v_subj    = box.getString("p_subj");
            String v_year      = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String v_class     = box.getString("p_class");
            String s_hakbun   = box.getSession("hakbun");

            sql  = "\r\n SELECT ordseq, weeklyseq, weeklysubseq, title, startdate, expiredate,                              ";
            sql += "\r\n        score, isopen, restartdate, reexpiredate, weight,                                           ";
            sql += "\r\n        CASE WHEN                                                                                   ";
            sql += "\r\n               TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += "\r\n              BETWEEN startdate AND expiredate                                                      ";
            sql += "\r\n        THEN 'Y' ELSE 'N'                                                                           ";
            sql += "\r\n        END indate,                                                                                 ";
            sql += "\r\n        CASE WHEN                                                                                   ";
            sql += "\r\n               TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')";
            sql += "\r\n              BETWEEN restartdate AND reexpiredate                                                  ";
            sql += "\r\n        THEN 'Y' ELSE 'N'                                                                           ";
            sql += "\r\n        END adddate,                                                                                ";
            sql += "\r\n        (CASE WHEN (SELECT COUNT(*) FROM TU_PROJREP                                                 ";
            sql += "\r\n                     WHERE ldate is not null                                                                      ";
            sql += "\n\r                       AND grcode = "         + StringManager.makeSQL(v_grcode);
            sql += "\n\r                       AND subj = "         + StringManager.makeSQL(v_subj);
            sql += "\n\r                       AND year = "       + StringManager.makeSQL(v_year);
            sql += "\n\r                       AND subjseq = "    + StringManager.makeSQL(v_subjseq);
            sql += "\n\r                       AND class = "      + StringManager.makeSQL(v_class);

            if(v_grcode.equals("N001001")){
                sql+="\r\n        AND substr(projid, 1, 9)  = " + StringManager.makeSQL(s_hakbun);
            }else{
                sql+="\r\n        AND projid  = " + StringManager.makeSQL(s_userid);
            }
            
            sql += "\r\n                       AND ordseq = a.ordseq ) > 0   ";
            sql += "\r\n        THEN 'Y' ELSE 'N' END                        ";
            sql += "\r\n        ) submityn, PERFECTSCORE, SUBMITSCORE, NOTSUBMITSCORE," +
            	   " (select score from TU_PROJREP where grcode = a.grcode and subj=a.subj and  year=a.year and subjseq=a.subjseq and class=a.class and projid='"+s_userid+"' and ordseq=a.ordseq) as user_score           ";
            sql += " ,(select realfile from tu_projrep  WHERE grcode = a.grcode ";
            sql += " AND subj = a.subj	";
            sql += " AND YEAR = a.YEAR ";
            sql += " AND subjseq = a.subjseq	";
            sql += " AND CLASS = a.CLASS 	";
            sql += " AND projid = '"+s_userid+"') realfile  ";
            sql += " ,(select newfile from tu_projrep  WHERE grcode = a.grcode ";
            sql += " AND subj = a.subj	";
            sql += " AND YEAR = a.YEAR ";
            sql += " AND subjseq = a.subjseq	";
            sql += " AND CLASS = a.CLASS 	";
            sql += " AND projid = '"+s_userid+"') newfile  ";
            sql += "\r\n   FROM tu_projord a                                 ";
            sql += "\r\n  WHERE 1=1                                          ";
            sql += "\n\r    AND grcode = "        + StringManager.makeSQL(v_grcode);
            sql += "\n\r    AND subj = "          + StringManager.makeSQL(v_subj);
            sql += "\n\r    AND year = "        + StringManager.makeSQL(v_year);
            sql += "\n\r    AND subjseq = "   + StringManager.makeSQL(v_subjseq);
            sql += "\n\r    AND class = "         + StringManager.makeSQL(v_class);
            sql += "\r\n    AND reptype = 'P' AND DELYN != 'Y' ";                                      
            sql += "\r\n  ORDER BY year DESC, subjseq, ordseq DESC ";
System.out.println("과제 ::::::::::::::::::::::" + sql);
            box.put("p_isPage", new Boolean(false));                    
            box.put("p_row", new Integer(row));                            
            //System.out.println("sql: "+sql);
            list = db.executeQueryList(box, sql);
            
        }catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    public DataBox selectViewOrder(RequestBox box) throws Exception
    {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

        try
        {
            db = new DatabaseExecute();

            String v_grcode  = box.getString("p_grcode");
            String v_subj      = box.getString("p_subj");
            String v_year      = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String v_class     = box.getString("p_class");

            String v_ordseq = box.getString("p_ordseq");

            sql  = "\r\n SELECT ordseq, weeklyseq, weeklysubseq, title, reptype,        ";
            sql += "\r\n        isopen,                                                 ";
            sql += "\r\n        score, basicscore, isopenscore,                         ";
            sql += "\r\n        startdate, expiredate, restartdate, reexpiredate,       ";
            sql += "\r\n        contents, submitfiletype, realfile, newfile, filesize, filelimit, ";
            sql += "\r\n        (SELECT count(*) FROM TU_PROJREP                        ";
            sql += "\r\n          WHERE 1=1                                             ";
            sql += "\n\r            AND grcode = "        + StringManager.makeSQL(v_grcode);
            sql += "\r\n            AND subj = "        + StringManager.makeSQL(v_subj);
            sql += "\r\n            AND year = "        + StringManager.makeSQL(v_year);
            sql += "\r\n            AND subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "\r\n            AND class = "   + StringManager.makeSQL(v_class);
            sql += "\r\n            AND ordseq = a.ordseq) reportcnt, PERFECTSCORE, SUBMITSCORE, NOTSUBMITSCORE  ";
            sql += "\r\n   FROM TU_PROJORD a                          ";
            sql += "\r\n  WHERE 1=1                                   ";
            sql += "\n\r    AND grcode = "        + StringManager.makeSQL(v_grcode);
            sql += "\r\n    AND subj = "        + StringManager.makeSQL(v_subj);
            sql += "\r\n    AND year = "        + StringManager.makeSQL(v_year);
            sql += "\r\n    AND subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "\r\n    AND class = "   + StringManager.makeSQL(v_class);
            sql += "\r\n    AND ordseq = "  + v_ordseq;
            sql += "\r\n    AND DELYN != 'Y' ";

                  dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;
    }


    public DataBox selectViewOrderStu(RequestBox box) throws Exception
    {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

        try
        {
            db = new DatabaseExecute();

            String v_grcode  = box.getString("p_grcode");
            String v_subj      = box.getString("p_subj");
            String v_year      = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String v_class     = box.getString("p_class");

            String v_ordseq     = box.getString("p_ordseq");
            String v_reptype    = box.getString("p_reptype");

            String s_userid   = box.getSession("userid");

            sql += "\r\n SELECT ordseq, weeklyseq, weeklysubseq, title, reptype,        ";
            sql += "\r\n        isopen, isopenscore, basicscore,                        ";
            sql += "\r\n        startdate, expiredate, restartdate, reexpiredate,       ";
            sql += "\r\n        contents, submitfiletype, realfile, newfile, filelimit, ";
            sql += "\r\n        (CASE WHEN (SELECT COUNT(*) FROM TU_PROJREP             ";
            sql += "\r\n                     WHERE ldate is not null                                  ";
            sql += "\n\r                       AND grcode = "         + StringManager.makeSQL(v_grcode);
            sql += "\r\n                       AND subj = "         + StringManager.makeSQL(v_subj);
            sql += "\r\n                       AND year = "         + StringManager.makeSQL(v_year);
            sql += "\r\n                       AND subjseq = "  + StringManager.makeSQL(v_subjseq);
            sql += "\r\n                       AND class = "        + StringManager.makeSQL(v_class);
            sql += "\r\n                       AND projid = "   + StringManager.makeSQL(s_userid);
            sql += "\r\n                       AND ordseq = a.ordseq ) > 0 ";
            sql += "\r\n        THEN 'Y' ELSE 'N' END   ";
            sql += "\r\n        ) submityn, PERFECTSCORE, SUBMITSCORE, NOTSUBMITSCORE              ";
            sql += "\r\n   FROM TU_PROJORD a            ";
            sql += "\r\n  WHERE 1=1                     ";
            sql += "\n\r    AND grcode = "        + StringManager.makeSQL(v_grcode);
            sql += "\r\n    AND subj = "        + StringManager.makeSQL(v_subj);
            sql += "\r\n    AND year = "        + StringManager.makeSQL(v_year);
            sql += "\r\n    AND subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "\r\n    AND class = "   + StringManager.makeSQL(v_class);
            sql += "\r\n    AND ordseq = "  + v_ordseq;
            sql += "\r\n    AND DELYN != 'Y' ";
//System.out.println(sql);
            dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;
    }

    public ArrayList selectProfFiles(RequestBox box) throws Exception
    {
      DatabaseExecute db = null;
      ArrayList list = null;
      StringBuffer sbSql = new StringBuffer();
      
      String v_grcode  = box.getString("p_grcode");
      String v_subj    = box.getString("p_subj");
      String v_year    = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class   = box.getString("p_class");
      String v_ordseq  = box.getString("p_ordseq");
      
      sbSql.append("SELECT GRCODE, SUBJ, YEAR, SUBJSEQ, CLASS, ORDSEQ, \r\n"
          ).append("  FILESEQ, REALFILE, NEWFILE, FILEPATH, FILESIZE \r\n"
          ).append(" FROM TU_PROJORD_FILE \r\n"
          ).append(" WHERE GRCODE = '").append(v_grcode).append("' \r\n"
          ).append("  AND SUBJ = '").append(v_subj).append("' \r\n"
          ).append("  AND YEAR = '").append(v_year).append("' \r\n"
          ).append("  AND SUBJSEQ = '").append(v_subjseq).append("' \r\n"
          ).append("  AND CLASS = '").append(v_class).append("' \r\n"
          ).append("  AND ORDSEQ = ").append(v_ordseq);
      //System.out.println(sbSql.toString());
      try {
        db = new DatabaseExecute();
        list = db.executeQueryList(box, sbSql.toString());
        
      } catch (Exception ex) {
        ErrorManager.getErrorStackTrace(ex, box, sbSql.toString());
        throw new Exception("sql = " + sbSql.toString() + "\r\n" + ex.getMessage());
      }
      return list;
      
    }
      
    public boolean insertFiles(RequestBox box, String p_class, int p_ordseq) throws Exception
    {
      DataBox dbox = null;
      boolean isCommit = false;
      DatabaseExecute db = null;

      char a = (char)2;
      char b = (char)3;
      
      StringBuffer sbSql = new StringBuffer();
      
      String s_userid    = box.getSession("userid");
      
      String v_grcode    = box.getString("p_grcode");
      String v_subj      = box.getString("p_subj");
      String v_year      = box.getString("p_year");
      String v_subjseq   = box.getString("p_subjseq");
      //String v_class     = box.getString("p_class");
      String v_class     = p_class;
      int v_ordseq       = p_ordseq;
      String v_files     = box.getString("p_files");
      
      String v_filepath = UploadUtil.performMakeFilepath("L", box);

      String v_filesArr [] = v_files.split("["+b+"]");

      Vector realFileNames = new Vector();
      Vector newFileNames  = new Vector();
      Vector fileSizes     = new Vector();
      Vector fileseqs      = new Vector();

      if (!v_files.equals("")){
          for(int i=0, j=v_filesArr.length; i < j; i++) {
              String v_filename []  = v_filesArr[i].split("["+a+"]");
              fileseqs.add(new Integer(i+1));
              realFileNames.add(v_filename[0]);
              newFileNames.add(v_filename[1]);
              fileSizes.add(v_filename[2]);
          }
      }
      
      sbSql.append("INSERT INTO TU_PROJORD_FILE \r\n"
          ).append("    (GRCODE, SUBJ, YEAR, SUBJSEQ, CLASS, ORDSEQ, \r\n"
          ).append("    FILESEQ, REALFILE, NEWFILE, FILEPATH, FILESIZE, \r\n"
          ).append("    LUSERID, LDATE) \r\n"
          ).append("  VALUES \r\n"
          ).append("    (?, ?, ?, ?, ?, ?, \r\n"
          ).append("    ?, ?, ?, ?, ?, \r\n"
          ).append("    ?,  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')) ");

      try {
        db = new DatabaseExecute(box);
        
        PreparedBox pbox = new PreparedBox("preparedbox");

        int lmt_Idx = 1;

        pbox.setString(lmt_Idx++, v_grcode);
        pbox.setString(lmt_Idx++, v_subj);
        pbox.setString(lmt_Idx++, v_year);
        pbox.setString(lmt_Idx++, v_subjseq);
        pbox.setString(lmt_Idx++, v_class);
        pbox.setInt(lmt_Idx++, v_ordseq);
        
        pbox.setVector(lmt_Idx++, fileseqs);
        pbox.setVector(lmt_Idx++, realFileNames);
        pbox.setVector(lmt_Idx++, newFileNames);
        pbox.setString(lmt_Idx++, v_filepath);
        pbox.setVector(lmt_Idx++, fileSizes);
        
        pbox.setString(lmt_Idx++, s_userid);
        
        if(realFileNames.size() > 0) {
          isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sbSql.toString()});
        } else {
          isCommit = true;
        }
            
      } catch (Exception ex) {
          ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
          throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
      }
      return isCommit;

    }
    
    public boolean insertOrder(RequestBox box) throws Exception
    {
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        boolean isCommit = false;
        DatabaseExecute db = null;

        try
        {
            db = new DatabaseExecute(box);

            String v_grcode    = box.getString("p_grcode");
            String v_subj      = box.getString("p_subj");
            String v_year      = box.getString("p_year");
            String v_subjseq   = box.getString("p_subjseq");
            String v_class     = box.getString("p_class");
            
            Vector v_classlist = box.getVector("p_classlist");
            v_classlist.add(v_class);

            
            
            for(int i=0; i<v_classlist.size(); i++){
            	v_class = "";
            	v_class = (String)v_classlist.get(i);
            	
            	
                sql  = "\r\n SELECT NVL(MAX(ordseq), 0)+1 ordseq FROM TU_PROJORD ";
                sql += "\r\n  WHERE 1=1                                             ";
                sql += "\n\r    AND grcode = "        + StringManager.makeSQL(v_grcode);
                sql += "\r\n    AND subj = "        + StringManager.makeSQL(v_subj);
                sql += "\r\n    AND year = "        + StringManager.makeSQL(v_year);
                sql += "\r\n    AND subjseq = " + StringManager.makeSQL(v_subjseq);
                sql += "\r\n    AND class = "   + StringManager.makeSQL(v_class);
                dbox = db.executeQuery(box, sql);
                int v_ordseq = dbox.getInt("d_ordseq");
                //-----------------------------------------------------------------------------------

                String s_userid = box.getSession("userid");

                String v_title          = box.getString("p_title");
                String v_weeklyseq      = box.getString("p_weeklyseq");
                String v_weeklysubseq   = box.getString("p_weeklysubseq");
                String v_reptype        = box.getString("p_reptype");
                float v_perfectscore    = box.getFloat("p_perfectscore");
                float v_submitscore     = box.getFloat("p_submitscore");
                float v_notsubmitscore  = box.getFloat("p_notsubmitscore");
                String v_isopen         = box.getString("p_isopen");
                String v_isopenscore    = box.getString("p_isopenscore");
                String v_startdate      = box.getString("p_startdate");
                String v_contents       = box.getString("p_contents");
                String v_filelimit      = box.getString("p_filelimit");
                String v_expiredate     = box.getString("p_expiredate");
                String v_restartdate    = box.getString("p_restartdate");
                String v_reexpiredate   = box.getString("p_reexpiredate");
                String v_submitfiletype = box.getString("p_submitfiletype");

                if(!v_startdate.equals(""))
                  v_startdate     = box.getString("p_startdate").replaceAll("-", "")    + box.getString("p_starthour")    + box.getString("p_startmin")     + box.getString("p_startsec") ;
                if(!v_expiredate.equals(""))
                  v_expiredate    = box.getString("p_expiredate").replaceAll("-", "")   + box.getString("p_expirehour")   + box.getString("p_expiremin")    + box.getString("p_expiresec");
                if(!v_restartdate.equals(""))
                  v_restartdate   = box.getString("p_restartdate").replaceAll("-", "")  + box.getString("p_restarthour")  + box.getString("p_restartmin")   + box.getString("p_restartsec");
                if(!v_reexpiredate.equals(""))
                  v_reexpiredate  = box.getString("p_reexpiredate").replaceAll("-", "") + box.getString("p_reexpirehour") + box.getString("p_reexpiremin")  + box.getString("p_reexpiresec");

                sql1  = "\r\n INSERT INTO tu_projord ( ";
                sql1 += "\r\n        grcode, subj, year, subjseq, class, ordseq, ";
                sql1 += "\r\n        title, weeklyseq, weeklysubseq, reptype,  ";
                sql1 += "\r\n        perfectscore, submitscore, notsubmitscore, isopen, isopenscore, ";
                sql1 += "\r\n        startdate, expiredate, restartdate, reexpiredate, ";
                sql1 += "\r\n        contents, submitfiletype, filelimit, luserid, ldate ";
                sql1 += "\r\n )VALUES(                                                                                            ";
                sql1 += "\r\n        ?, ?, ?, ?, ?, ?, ";
                sql1 += "\r\n        ?, ?, ?, ?, ";
                sql1 += "\r\n        ?, ?, ?, ?, ?, ";
                sql1 += "\r\n        ?, ?, ?, ?, ";
                sql1 += "\r\n        ?, ?, ?, ?, ";
                sql1 += "\r\n         TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'))     ";

                PreparedBox pbox1 = new PreparedBox("preparedbox");
                int lmt_Idx = 1;

                pbox1.setString(lmt_Idx++, v_grcode);
                pbox1.setString(lmt_Idx++, v_subj);
                pbox1.setString(lmt_Idx++, v_year);
                pbox1.setString(lmt_Idx++, v_subjseq);
                pbox1.setString(lmt_Idx++, v_class);
                pbox1.setInt(lmt_Idx++, v_ordseq);
                pbox1.setString(lmt_Idx++, v_title);
                pbox1.setString(lmt_Idx++, v_weeklyseq);
                pbox1.setString(lmt_Idx++, v_weeklysubseq);
                pbox1.setString(lmt_Idx++, v_reptype);

                pbox1.setFloat(lmt_Idx++, v_perfectscore);
                pbox1.setFloat(lmt_Idx++, v_submitscore);
                pbox1.setFloat(lmt_Idx++, v_notsubmitscore);

                pbox1.setString(lmt_Idx++, v_isopen);
                pbox1.setString(lmt_Idx++, v_isopenscore);
                pbox1.setString(lmt_Idx++, v_startdate);
                pbox1.setString(lmt_Idx++, v_expiredate);
                pbox1.setString(lmt_Idx++, v_restartdate);
                pbox1.setString(lmt_Idx++, v_reexpiredate);
                pbox1.setString(lmt_Idx++, v_contents);
                pbox1.setString(lmt_Idx++, v_submitfiletype);
                pbox1.setString(lmt_Idx++, v_filelimit);

                pbox1.setString(lmt_Idx++, s_userid);

                isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});
                
                if(isCommit) {
                  isCommit = insertFiles(box, v_class, v_ordseq);
                }
            }
            
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        
        
        return isCommit;
    }
    public boolean updateFiles(RequestBox box) throws Exception
    {
      DataBox dbox = null;
      boolean isCommit = false;
      DatabaseExecute db = null;

      char a = (char)2;
      char b = (char)3;
      
      StringBuffer sbSql = new StringBuffer();
      StringBuffer sbSql2 = new StringBuffer();
      StringBuffer sbSql3 = new StringBuffer();
      
      String s_userid    = box.getSession("userid");
      
      String v_grcode    = box.getString("p_grcode");
      String v_subj      = box.getString("p_subj");
      String v_year      = box.getString("p_year");
      String v_subjseq   = box.getString("p_subjseq");
      String v_class     = box.getString("p_class");
      int v_ordseq       = box.getInt("p_ordseq");
      String v_files     = box.getString("p_files");
      int v_maxfileseq   = 1;
      
      //----------------------   삭제되는 파일 --------------------------------
      int    v_upfilecnt  = box.getInt("p_upfilecnt");	//	서버에 저장되있는 파일수
      String v_deletefile = "";   
			
      Vector v_savefile        =	new	Vector();
      Vector v_filesArrequence =	new	Vector();
	 
      for(int	i =	0; i < v_upfilecnt;	i++) {
        if(	!box.getString("p_fileseq" + i).equals(""))	{
          v_savefile.addElement(box.getString("p_savefile" + i));    //서버에 저장되있는 파일명 중에서삭제할 파일들
          v_filesArrequence.addElement(box.getString("p_fileseq"	+ i)); //서버에	저장되있는 파일번호	중에서 삭제할 파일들
        }
      }

      //----------------------   업로드되는 경로 -------------------------------
      String v_filepath = UploadUtil.performMakeFilepath("L", box);

      //----------------------   업로드되는 파일 -------------------------------
      String v_filesArr [] = v_files.split("["+b+"]");

      Vector realFileNames = new Vector();
      Vector newFileNames  = new Vector();              
      Vector fileSizes     = new Vector(); 
      Vector fileseqs      = new Vector();

      sbSql.append("INSERT INTO TU_PROJORD_FILE \r\n"
          ).append("    (GRCODE, SUBJ, YEAR, SUBJSEQ, CLASS, ORDSEQ, \r\n"
          ).append("    FILESEQ, REALFILE, NEWFILE, FILEPATH, FILESIZE, \r\n"
          ).append("    LUSERID, LDATE) \r\n"
          ).append("  VALUES \r\n"
          ).append("    (?, ?, ?, ?, ?, ?, \r\n"
          ).append("    ?, ?, ?, ?, ?, \r\n"
          ).append("    ?,  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')) ");

      sbSql2.append("DELETE FROM TU_PROJORD_FILE \r\n"
           ).append("  WHERE GRCODE = ? \r\n"
           ).append("    AND SUBJ = ? \r\n"
           ).append("    AND YEAR = ? \r\n"
           ).append("    AND SUBJSEQ = ? \r\n"
           ).append("    AND CLASS = ? \r\n"
           ).append("    AND ORDSEQ = ? \r\n"
           ).append("    AND FILESEQ = ? ");

      sbSql3.append("SELECT NVL(MAX(FILESEQ), 0)+1 FILESEQ \r\n"
           ).append("  FROM TU_PROJORD_FILE \r\n"
           ).append("  WHERE GRCODE = '").append(v_grcode).append("' \r\n"
           ).append("    AND SUBJ = '").append(v_subj).append("' \r\n"
           ).append("    AND YEAR = '").append(v_year).append("' \r\n"
           ).append("    AND SUBJSEQ = '").append(v_subjseq).append("' \r\n"
           ).append("    AND CLASS = '").append(v_class).append("' \r\n"
           ).append("    AND ORDSEQ = ").append(""+ v_ordseq);
      
      try {
        db = new DatabaseExecute(box);
        
        // 현재 시작될 파일 시퀀스 번호 조회
        dbox = db.executeQuery(box, sbSql3.toString());
        v_maxfileseq = dbox.getInt("d_fileseq");
        
        // 업로드되는 파일 벡터에 넣기
        if (!v_files.equals("")){
          for(int i=0, j=v_filesArr.length; i < j; i++) {
            String v_filename [] = v_filesArr[i].split("["+a+"]");
            fileseqs.add(new Integer(v_maxfileseq+i));
            realFileNames.add(v_filename[0]);
            newFileNames.add(v_filename[1]);
            fileSizes.add(v_filename[2]);
          }
        }
        
        // 새로 업로드된 파일 저장
        PreparedBox pbox = new PreparedBox("preparedbox");

        int lmt_Idx = 1;
        pbox.setString(lmt_Idx++, v_grcode);
        pbox.setString(lmt_Idx++, v_subj);
        pbox.setString(lmt_Idx++, v_year);
        pbox.setString(lmt_Idx++, v_subjseq);
        pbox.setString(lmt_Idx++, v_class);
        pbox.setInt(lmt_Idx++, v_ordseq);
        
        pbox.setVector(lmt_Idx++, fileseqs);
        pbox.setVector(lmt_Idx++, realFileNames);
        pbox.setVector(lmt_Idx++, newFileNames);
        pbox.setString(lmt_Idx++, v_filepath);
        pbox.setVector(lmt_Idx++, fileSizes);
        
        pbox.setString(lmt_Idx++, s_userid);
        
        // 파일 삭제
        PreparedBox pbox2 = new PreparedBox("preparedbox");

        lmt_Idx = 1;
        pbox2.setString(lmt_Idx++, v_grcode);
        pbox2.setString(lmt_Idx++, v_subj);
        pbox2.setString(lmt_Idx++, v_year);
        pbox2.setString(lmt_Idx++, v_subjseq);
        pbox2.setString(lmt_Idx++, v_class);
        pbox2.setInt(lmt_Idx++, v_ordseq);
        
        pbox2.setVector(lmt_Idx++, v_filesArrequence);
        
        if(realFileNames.size() > 0 && v_filesArrequence.size() > 0){             //첨부파일이 있고 삭제할 파일이 있는 경우
         	isCommit = db.executeUpdate(new PreparedBox [] {pbox, pbox2},  new String [] {sbSql.toString(), sbSql2.toString()});
         	
         	//첨부파일을 삭제한다.
          if(isCommit){ 
            //FileManager.deleteFile("L", v_savefile, box);
          }
        }else if(realFileNames.size() == 0 && v_filesArrequence.size() > 0){		  //삭제할 파일이 있는 경우
         	isCommit = db.executeUpdate(new PreparedBox [] {pbox2},  new String [] {sbSql2.toString()});
         	
         	//첨부파일을 삭제한다.
          if(isCommit){ 
            //FileManager.deleteFile("L", v_savefile, box);
          }
        }else if(realFileNames.size() > 0 && v_filesArrequence.size() == 0){		  //첨부파일 추가한 경우
         	isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sbSql.toString()});
        }else{
         	// 작업이 없음.
         	isCommit = true;
        }

      } catch (Exception ex) {
          ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
          throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
      }
      return isCommit;
    }

    public boolean updateOrder(RequestBox box) throws Exception
    {
        boolean isCommit = false;
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

        String v_grcode  = box.getString("p_grcode");
        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_class   = box.getString("p_class");

        String v_ordseq       = box.getString("p_ordseq");
        String s_userid       = box.getSession("userid");
        String v_title        = box.getString("p_title");
        float v_perfectscore  = box.getFloat("p_perfectscore");
        float v_submitscore   = box.getFloat("p_submitscore");
        float v_notsubmitscore= box.getFloat("p_notsubmitscore");
        String v_isopen       = box.getString("p_isopen");
        String v_isopenscore  = box.getString("p_isopenscore");
        String v_startdate    = box.getString("p_startdate");
        String v_expiredate   = box.getString("p_expiredate");
        String v_restartdate  = box.getString("p_restartdate");
        String v_reexpiredate = box.getString("p_reexpiredate");

        if(!v_startdate.equals(""))
            v_startdate     = box.getString("p_startdate").replaceAll("-", "")    + box.getString("p_starthour")    + box.getString("p_startmin")     + box.getString("p_startsec");
        if(!v_expiredate.equals(""))
            v_expiredate    = box.getString("p_expiredate").replaceAll("-", "")   + box.getString("p_expirehour")   + box.getString("p_expiremin")    + box.getString("p_expiresec");
        if(!v_restartdate.equals(""))
            v_restartdate   = box.getString("p_restartdate").replaceAll("-", "")  + box.getString("p_restarthour")  + box.getString("p_restartmin")   + box.getString("p_restartsec");
        if(!v_reexpiredate.equals(""))
            v_reexpiredate  = box.getString("p_reexpiredate").replaceAll("-", "") + box.getString("p_reexpirehour") + box.getString("p_reexpiremin")  + box.getString("p_reexpiresec");

        String v_contents       = box.getString("p_contents");
        String v_submitfiletype = box.getString("p_submitfiletype");
        String v_filelimit      = box.getString("p_filelimit");

        //
        sql  = "\r\n UPDATE tu_projord SET ";
        sql += "\r\n           title           = ? ";
        sql += "\r\n         , perfectscore    = ? ";
        sql += "\r\n         , submitscore     = ? ";
        sql += "\r\n         , notsubmitscore  = ? ";
        sql += "\r\n         , isopen          = ? ";
        sql += "\r\n         , isopenscore     = ? ";
        sql += "\r\n         , startdate       = ? ";
        sql += "\r\n         , expiredate      = ? ";
        sql += "\r\n         , restartdate     = ? ";
        sql += "\r\n         , reexpiredate    = ? ";
        sql += "\r\n         , contents        = ? ";
        sql += "\r\n         , submitfiletype  = ? ";
        sql += "\r\n         , filelimit       = ? ";
        sql += "\r\n         , luserid         = ? ";
        sql += "\r\n         , ldate =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
        sql += "\r\n   WHERE 1=1                   ";
        sql += "\r\n      AND grcode  = ? ";
        sql += "\r\n      AND subj    = ?           ";
        sql += "\r\n      AND year    = ?           ";
        sql += "\r\n      AND subjseq = ?           ";
        sql += "\r\n      AND class   = ?           ";
        sql += "\r\n      AND ordseq  = ?           ";

        try
        {
            db = new DatabaseExecute(box);

            PreparedBox pbox = new PreparedBox("preparedbox");
            int lmt_Idx = 1;
            pbox.setString(lmt_Idx++, v_title);
            pbox.setFloat(lmt_Idx++, v_perfectscore);
            pbox.setFloat(lmt_Idx++, v_submitscore);
            pbox.setFloat(lmt_Idx++, v_notsubmitscore);
            pbox.setString(lmt_Idx++, v_isopen);
            pbox.setString(lmt_Idx++, v_isopenscore);
            pbox.setString(lmt_Idx++, v_startdate);
            pbox.setString(lmt_Idx++, v_expiredate);
            pbox.setString(lmt_Idx++, v_restartdate);
            pbox.setString(lmt_Idx++, v_reexpiredate);
            pbox.setString(lmt_Idx++, v_contents);
            pbox.setString(lmt_Idx++, v_submitfiletype);
            pbox.setString(lmt_Idx++, v_filelimit);
            pbox.setString(lmt_Idx++, s_userid);
            
            pbox.setString(lmt_Idx++, v_grcode);
            pbox.setString(lmt_Idx++, v_subj);
            pbox.setString(lmt_Idx++, v_year);
            pbox.setString(lmt_Idx++, v_subjseq);
            pbox.setString(lmt_Idx++, v_class);
            pbox.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));

            isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sql});

        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        
        if(isCommit) {
          isCommit = updateFiles(box);
        }
        return isCommit;
    }
    public boolean deleteOrder(RequestBox box) throws Exception
    {
        DatabaseExecute db = null;
        DataBox dbox = null;
        boolean isCommit = false;
        String sql = "";
        String sql1 = "";
        String v_savefile = "";

        try
        {
            db = new DatabaseExecute(box);

            String v_grcode  = box.getString("p_grcode");
      String v_subj    = box.getStringDefault("p_subj", box.getSession("psubj"));
      String v_year    = box.getStringDefault("p_year", box.getSession("pyear"));
      String v_subjseq = box.getStringDefault("p_subjseq", box.getSession("psubjseq"));
      String v_class   = box.getStringDefault("p_class" , box.getSession("pclass"));

            String v_ordseq = box.getString("p_ordseq");

            sql1  = "\r\n UPDATE TU_PROJORD SET ";
            sql1 += "\r\n    DELYN = 'Y' ";
            sql1 += "\r\n WHERE 1=1               ";
            sql1 += "\r\n   AND grcode = ?        ";
            sql1 += "\r\n   AND subj    = ?       ";
            sql1 += "\r\n   AND year    = ?       ";
            sql1 += "\r\n   AND subjseq = ?       ";
            sql1 += "\r\n   AND class   = ?       ";
            sql1 += "\r\n   AND ordseq  = ?       ";

            PreparedBox pbox1 = new PreparedBox("preparedbox");
            pbox1.setString(1, v_grcode);
            pbox1.setString(2, v_subj);
            pbox1.setString(3, v_year);
            pbox1.setString(4, v_subjseq);
            pbox1.setString(5, v_class);
            pbox1.setInt(6, Integer.parseInt(v_ordseq));

            isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});

        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }


    public boolean saveWeight(RequestBox box) throws Exception
    {
        DatabaseExecute db = null;
        boolean isCommit = false;
        String sql = "";

        try
        {
            db = new DatabaseExecute(box);

            String s_userid = box.getSession("userid");

		      String v_grcode  = box.getString("p_grcode");
		      String v_subj      = box.getString("p_subj");
		      String v_year      = box.getString("p_year");
		      String v_subjseq = box.getString("p_subjseq");
		      String v_class     = box.getString("p_class");

            Vector v_reportseq  = box.getVector("p_reportseq");
            Vector v_weight     = box.getVector("p_weight");

            Vector v_reportseq_tmp  = new Vector();
            Vector v_weight_tmp     = new Vector();

            for(int i=0; i<v_reportseq.size();i++)
            {
                v_reportseq_tmp.add(Integer.valueOf((String)v_reportseq.get(i)));
                v_weight_tmp.add(Integer.valueOf((String)v_weight.get(i)));
            }

            sql  = "\r\n UPDATE tu_projord SET  ";
            sql += "\r\n          weight   = ?  ";
            sql += "\r\n        , luserid  = ?  ";
            sql += "\r\n        , ldate    =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += "\r\n WHERE 1=1              ";
            sql += "\r\n   AND grcode  = ?      ";
            sql += "\r\n   AND subj    = ?      ";
            sql += "\r\n   AND year    = ?      ";
            sql += "\r\n   AND subjseq = ?      ";
            sql += "\r\n   AND class   = ?      ";
            sql += "\r\n   AND ordseq  = ?      ";

            PreparedBox pbox1 = new PreparedBox("preparedbox");

            pbox1.setVector(1, v_weight_tmp);
            pbox1.setString(2, s_userid);
            pbox1.setString(3, v_grcode);
            pbox1.setString(4, v_subj);
            pbox1.setString(5, v_year);
            pbox1.setString(6, v_subjseq);
            pbox1.setString(7, v_class);
            pbox1.setVector(8, v_reportseq_tmp);

            isCommit = db.executeUpdate(new PreparedBox [] {pbox1}, new String [] {sql});
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }
    
    public ArrayList selectClassList(RequestBox box) throws Exception {
      DatabaseExecute db    = null;
      ArrayList       list  = new ArrayList();
      StringBuffer    sbSql = new StringBuffer();
      
      String v_grcode  = box.getString("p_grcode"  );
      String v_subj    = box.getString("p_subj"    );
      String v_year    = box.getString("p_year"    );
      String v_subjseq = box.getString("p_subjseq" );
      String v_class   = box.getString("p_class"   );
      


      sbSql.append(" SELECT GRCODE, SUBJ, YEAR, SUBJSEQ \n") 
	       .append("   FROM Tz_SUBJSEQ                         \n") 
	       .append("  WHERE GRCODE  = '" + v_grcode  + "'      \n") 
	       .append("    AND SUBJ    = '" + v_subj    + "'      \n") 
	       .append("    AND YEAR    = '" + v_year    + "'      \n") 
	       .append("    AND SUBJSEQ = '" + v_subjseq + "'      \n");

      
      try {
        db   = new DatabaseExecute();
        list = db.executeQueryList(box, sbSql.toString());
        
      } 
      catch (Exception ex) {
        ErrorManager.getErrorStackTrace(ex, box, sbSql.toString());
        throw new Exception("sql = " + sbSql.toString() + "\r\n" + ex.getMessage());
      }
      
      
      return list;
      
    }
}