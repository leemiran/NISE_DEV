// **********************************************************
//  1. ��      ��: �ڵ� ����
//  2. ���α׷��� : CodeConfigBean.java
//  3. ��      ��: �ڵ� ����
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��:  2003. 7.  14
//  7. ��      ��:
// **********************************************************

package com.ziaan.system;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * �ڵ� ����(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class CodeConfigBean { 
	
	public static Logger logger = Logger.getLogger(CodeConfigBean.class);
	
    public CodeConfigBean() { }



    /**
    * ���ھտ� 0 ���� ä���,  (����,����)
    */
    public static String addZero (int chkNumber, int chkLen) { 
        String temp = null;
        temp = String.valueOf(chkNumber);
        int len = temp.length();

        if ( len < chkLen) { 
            for ( int i=1; i<=(chkLen-len); i++ ) { 
                temp = "0" + temp;
            }
        }

        return temp;
    }

    /**
    *  ���Ǳ� ����Ÿ�� (����Ÿ��)
    */
    public static String getConfigValue (String name) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select vals from TZ_CONFIG        ";
            sql += "  where name = " + StringManager.makeSQL(name);
            // System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getString("vals");
            }
            // System.out.println("result"=result);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }



    /**
    *  �ڵ� ����Ʈ�ڽ� (config��,����Ʈ�ڽ���,���ð�) 3
    */
    public static String getCodeSelect (String config, String name, String selected) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, "", 0, "");
    }

    /**
    *  �ڵ� ����Ʈ�ڽ� (config��,����Ʈ�ڽ���,���ð�,��ü����) 4
    */
    public static String getCodeSelect (String config, String name, String selected, int allcheck) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, "", allcheck, "");
        /**(config��(0),�ڵ�,����,����Ʈ�ڽ���(0),���ð�(0),�̺�Ʈ��,��ü����(0)) **/
    }

    /**
    *  �ڵ� ����Ʈ�ڽ� (config��,�ڵ�,����,����Ʈ�ڽ���,�̺�Ʈ��,���ð�) 5
    */
    public static String getCodeSelect (String config, String code, int levels, String name, String selected) throws Exception { 
        return getCodeSelect (config, code, levels, name, selected, "",0, "");
    }

    /**
    *  �ڵ� ����Ʈ�ڽ� (config��,�ڵ�,����,����Ʈ�ڽ���,���ð�,�̺�Ʈ��,��ü����) 6
    */
    public static String getCodeSelect (String config, String code, int levels, String name, String selected, int allcheck) throws Exception { 
        return getCodeSelect (config, code, levels, name, selected, "", allcheck, "");
    }

    /**
    *  �ڵ� ����Ʈ�ڽ� (config��,�ڵ�,����Ʈ�ڽ���,���ð�,�̺�Ʈ��) 4
    */
    public static String getCodeSelect (String config, String name, String selected, String event) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, event, 0, "");
    }

    /**
    *  �ڵ� ����Ʈ�ڽ� (config��,�ڵ�,����Ʈ�ڽ���,���ð�,�̺�Ʈ��,��ü����) 5
    */
    public static String getCodeSelect (String config, String name, String selected, String event, int allcheck) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, event, allcheck, "");
    }

    /**
    *  �ڵ� ����Ʈ�ڽ� (config��, ����Ʈ�ڽ���, ���ð�, �̺�Ʈ��, ��ü����, �κ����)6
    */
    public static String getCodeSelect (String config, String name, String selected, String event, int allcheck, String aPart) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, event, allcheck, aPart);
    }

    /**
    *  �ڵ� ����Ʈ�ڽ� (config��,�ڵ�,����,����Ʈ�ڽ���,���ð�,�̺�Ʈ��,�Ϻ����) 7
      *  TZ_CONFIG �� �̿�
    *  allcheck�� ��ü���� : 0 -��ü����, 1 - ��ü����, 2 - ALL
    *  aPart��(2�ڸ�) -- > ���ڸ��� ���̿� �ִ� �ڵ尪�� return�Ѵ�.
    */

    public static String getCodeSelect (String config, String code, int levels, String name, String selected, String event, int allcheck, String aPart) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";
        CodeData data = null;
        String gubun = getConfigValue(config);   // TZ_CONFIG ���̺��� ���� ���´�.
        String fChar = "";
        String sChar = "";

        result = "  <SELECT name=\"" + name + "\" name=\"" + name + "\" " + event + " > \n";

        if ( allcheck == 1) { 
          result += " <option value='' >== =��ü == =</option > \n";
        } else if ( allcheck == 2) { 
              result += " <option value='ALL' > ALL</option > \n";
        }

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select code, codenm from tz_code            ";
            sql += "  where gubun  = " + StringManager.makeSQL(gubun);
            sql += "    and levels = " + levels;
            if ( levels > 1) { 
              sql += "    and upper = " + StringManager.makeSQL(code);
            }

            if ( aPart.length() > 0 && aPart.length()<=2) { 
                  fChar = aPart.substring(0,1);
                  sChar = aPart.substring(1,2);
                  boolean check = fChar.charAt(0) <= sChar.charAt(0);

                  // System.out.println("check == > " + check);
                  if ( check) { 
                    // sql += "  and substr(code," +fChar + "," +sChar + ") = " +StringManager.makeSQL(tChar);
                    sql += "  and code >=" +StringManager.makeSQL(fChar) + " and code <= " +StringManager.makeSQL(sChar);
                }

            }

            sql += " order by code asc";
            ls = connMgr.executeQuery(sql);

              // System.out.println("code_sql == > " + sql);
            while ( ls.next() ) { 
                data = new CodeData();

                data.setCode( ls.getString("code") );
                data.setCodenm( ls.getString("codenm") );

                result += " <option value=" + data.getCode();
                if ( selected.equals(data.getCode() )) { 
                    result += " selected ";
                }

                result += " > " + data.getCodenm() + "</option > \n";
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



    /**
    *  �ڵ� ����Ʈ�ڽ� (GUBUN,�ڵ�,����,����Ʈ�ڽ���,���ð�,�̺�Ʈ��,��ü����)
      *  TU_CODEGUBUN �� �̿�
    *  ��ü���� : 0 -��ü����, 1 - ��ü����, 2 - ALL, 3 - ����
    */
    public static String getCodeGubunSelect (String gubun, String code, int levels, String name, String selected, String event, int allcheck) throws Exception {
       return getCodeGubunSelect (gubun, code, levels, name, selected, event, allcheck, null);
    }
    
    
    /**
    *  �ڵ� ����Ʈ�ڽ� (GUBUN,�ڵ�,����,����Ʈ�ڽ���,���ð�,�̺�Ʈ��,��ü����)
      *  TU_CODEGUBUN �� �̿�
    *  ��ü���� : 0 -��ü����, 1 - ��ü����, 2 - ALL, 3 - ����
    */
    public static String getCodeGubunSelect (String gubun, String code, int levels, String name, String selected, String event, int allcheck, RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String result = null;
        String sql = "";
        String language = "";
        String v_code_nm = "";
        CodeData data = null;
        

        language = "KOREAN";
        
        if(box == null){
          language = "KOREAN";
        }else{
          language = box.getSession("languageName");
        }
/*
System.out.println("gubun"+gubun);
System.out.println("code"+code);
System.out.println("levels"+levels);
System.out.println("name"+name);
System.out.println("selected"+selected);
System.out.println("event"+event);
System.out.println("allcheck"+allcheck);
*/

        result = "  <SELECT name=" + name + " " + event + " > \n";

        if (allcheck == 1) {
          result += " <option value=''>=== ��ü ===</option> \n";
        } else if (allcheck == 2) {
            result += " <option value='ALL'>ALL</option> \n";
        } else if (allcheck == 3) {
            result += " <option value=''>����</option> \n";
        } else if (allcheck == 4) {
        	result += " <option value=''>�����Է�</option> \n";
        }

        try {
            connMgr = new DBConnectionManager();

            sql  = " select code, codenm, codenm_eng, codenm_chn, codenm_jpn from tz_code            ";
            sql += "  where gubun  = " + StringManager.makeSQL(gubun);
            sql += "    and levels = " + levels;
            if (levels > 1) {
              sql += "    and upper = " + StringManager.makeSQL(code);
            }
            sql += " order by orders";
            //System.out.println("sql===>>>>>>>>"+sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                data = new CodeData();

                data.setCode(ls.getString("code"));
                data.setCodenm(ls.getString("codenm"));
                
             //   System.out.println(ls.getString("code"));
              //  System.out.println(ls.getString("codenm"));
                
                if(language.equals("KOREAN")){
                  v_code_nm = ls.getString("codenm");
                }else if(language.equals("ENGLISH")){
                  v_code_nm =ls.getString("codenm_eng");
                }else if(language.equals("JAPANESE")){
                  v_code_nm =ls.getString("codenm_jpn");
                }else if(language.equals("CHINESE")){
                  v_code_nm =ls.getString("codenm_chn");
                }

                result += " <option value='" + data.getCode()+"'";
                if (selected.equals(data.getCode())) {
                    result += " selected ";
                }

                result += ">" + v_code_nm + "</option> \n";

            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        result += "  </SELECT> \n";
        return result;
    }

    
    /**
     *  �ڵ� ������ư (GUBUN,�ڵ�,����,���̿���ư��,���ð�,�̺�Ʈ��,��ü����)
       *  TZ_CODEGUBUN �� �̿�
     *  ��ü���� : 0 -��ü����, 1 - ��ü����, 2 - ALL
     */
     public static String getCodeGubunRadio (String gubun, String code, int levels, String name, String checked, String event, int allcheck) throws Exception {
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         String result = null;
         String sql = "";
         CodeData data = null;

         //result = "  <SELECT name=" + name + " " + event + " > \n";         
         
         result = "";

         try {
             connMgr = new DBConnectionManager();

             sql  = " select code, codenm from tz_code            ";
             sql += "  where gubun  = " + StringManager.makeSQL(gubun);
             sql += "    and levels = " + levels;
             if (levels > 1) {
               sql += "    and upper = " + StringManager.makeSQL(code);
             }
             sql += " order by code asc";

             ls = connMgr.executeQuery(sql);

             while (ls.next()) {
                 data = new CodeData();

                 data.setCode(ls.getString("code"));
                 data.setCodenm(ls.getString("codenm"));

                 result += " <input type=radio name="+name+" value=" + data.getCode();
                 if (checked.equals(data.getCode())) {
                     result += " checked ";
                 }

                 result += ">" + data.getCodenm() + " \n";
             }
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }

         result += "  </SELECT> \n";
         return result;
     }



    /**
    *  �ڵ屸�и� (config��)
    */
    public static String getCodeName (String config) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        String result = "";
        CodeData data = null;
        String gubun = getConfigValue(config);

        try {
            connMgr = new DBConnectionManager();

            sql  = " select gubunnm, maxlevel from TZ_CODEGUBUN        ";
            sql += "  where gubun = " + StringManager.makeSQL(gubun);
            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                data=new CodeData();
                data.setGubunnm(ls.getString("gubunnm"));
                result = data.getGubunnm();
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return result;
    }


    /**
    *  �ڵ�� (config��,�ڵ�)
    */
    public static String getCodeName (String config, String code) throws Exception {
        return getCodeName (config, code, 1);
    }

    /**
    *  �ڵ�� (config��,�ڵ�,����)
    */
    public static String getCodeName (String config, String code, int levels) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        String result = "";
        CodeData data = null;
        String gubun = getConfigValue(config);
        if(gubun.equals("")) gubun = config;

        try {
            connMgr = new DBConnectionManager();

            sql  = " select codenm from TZ_CODE        ";
            sql += "  where gubun  = " + StringManager.makeSQL(gubun);
            sql += "    and levels = " + levels;
            sql += "    and code   = " + StringManager.makeSQL(code);
            ls = connMgr.executeQuery(sql);
            if (ls.next()) {
                data=new CodeData();
                data.setCodenm(ls.getString("codenm"));
                result = data.getCodenm();
            }

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return result;
    }


    /**
     *  �ڵ� ���� (config��,�����ڵ�,����)
     */
    public static int getCodeCnt(String config, String code, int levels) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        int result = 0;
        String gubun = getConfigValue(config);

        try {
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select count(*) cnt from tz_code            ";
            sql += "  where gubun  = " + StringManager.makeSQL(gubun);
            sql += "    and levels = " + levels;
            if (levels > 1) {
              sql += "    and upper = " + StringManager.makeSQL(code);
            }

            ls = connMgr.executeQuery(sql);

            if  (ls.next()) {
                result = ls.getInt("cnt");
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }


    /**
     *  �ڵ� ����Ʈ (config��,�����ڵ�,����)
     */
     public static ArrayList getCodeList (String config, String code, int levels) throws Exception {
         DBConnectionManager connMgr = null;
         ListSet ls     = null;
         ArrayList list = null;
         String sql = "";
         CodeData data = null;
         String gubun = getConfigValue(config);

         try {
             connMgr = new DBConnectionManager();
             list = new ArrayList();

             sql  = " select code, codenm from tz_code            ";
             sql += "  where gubun  = " + StringManager.makeSQL(gubun);
             sql += "    and levels = " + levels;
             if (levels > 1) {
               sql += "    and upper = " + StringManager.makeSQL(code);
             }
             sql += " order by code asc";

             ls = connMgr.executeQuery(sql);

             while (ls.next()) {
                 data = new CodeData();

                 data.setCode(ls.getString("code"));
                 data.setCodenm(ls.getString("codenm"));
                 list.add(data);
             }
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }

         return list;
     }


    /**
     * �ڵ����Ʈ��(ù��° �ڵ尪)
     * @param config     config��
     * @param code       �����ڵ�
     * @param levels     ����
     * @return result    �ڵ尪
     * @throws Exception
     */
    public static String getCodeDefault (String config, String code, int levels) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        String result = "";
        CodeData data = null;
        String gubun = getConfigValue(config);

        try {
            connMgr = new DBConnectionManager();

            sql  = " select code from TZ_CODE        ";
            sql += "  where gubun  = " + StringManager.makeSQL(gubun);
            sql += "    and levels = " + levels;
            if (levels > 1) {
              sql += "    and upper = " + StringManager.makeSQL(code);
            }

            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                data=new CodeData();
                data.setCode(ls.getString("code"));
                result = data.getCode();
            }

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return result;
    }
    
     
     /**
     *  CB-HRD���� ����ϴ� �����ڵ� ����Ʈ�ڽ� (GUBUN, ����Ʈ�ڽ���,���ð�,�̺�Ʈ��,��ü����)
     *  ��ü���� : 0 -��ü����, 1 - ��ü����, 2 - ALL, 3 - ����
     *  ����� 2008.11.12
     */
     public static String getCommonCodeSelectForCb (String gubun, String name, String selected, String event, int allcheck, RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         String result = null;
         String sql = "";
         String v_code_nm = "";
         CodeData data = null;
         
         result = "  <select name=\"" + name + "\" id=\"" + name + "\" " + event + " > \n";

         if (allcheck == 1) {
           result += " <option value=\"\">=== ��ü ===</option> \n";
         } else if (allcheck == 2) {
             result += " <option value=\"ALL\">ALL</option> \n";
         } else if (allcheck == 3) {
             result += " <option value=\"\">����</option> \n";
         }

         try {
             connMgr = new DBConnectionManager();

             sql = " select cd as code, cd_nm as codenm "
            	 + " from   tk_edu000t "
            	 + " where  co_cd  = " + StringManager.makeSQL(gubun)
             	 + " order  by cd";
             ls = connMgr.executeQuery(sql);

             while (ls.next()) {
                 data = new CodeData();

                 data.setCode(ls.getString("code"));
                 data.setCodenm(ls.getString("codenm"));
                 
                 result += " <option value=\"" + data.getCode() + "\"";
                 if (selected.equals(data.getCode())) {
                     result += " selected ";
                 }

                 result += ">" + data.getCodenm() + "</option> \n";

             }
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }

         result += "  </select> \n";
         return result;
     }

     /**
      *  ȸ������ �������� ����ϴ� �����ڵ� ����Ʈ�ڽ� (GUBUN, ����Ʈ�ڽ���,���ð�,�̺�Ʈ��,��ü����)
      *  ��ü���� : 0 -��ü����, 1 - ��ü����, 2 - ALL, 3 - ����
  
      */
      public static String getCommonCodeSelectDept(String gubun, String name, String selected, String event, int allcheck) throws Exception {
          DBConnectionManager connMgr = null;
          ListSet ls = null;
          String result = null;
          String sql = "";
          String v_code_nm = "";
          CodeData data = null;
          
          result = "  <select name=\"" + name + "\" id=\"" + name + "\" " + event + " > \n";

          if (allcheck == 1) {
            result += " <option value=\"\">=== ��ü ===</option> \n";
          } else if (allcheck == 2) {
              result += " <option value=\"ALL\">ALL</option> \n";
          } else if (allcheck == 3) {
              result += " <option value=\"\">����</option> \n";
          }

          try {
     
              connMgr = new DBConnectionManager();
              	
              sql = " select orgid as code, org_nm as codenm "
             	 + " from   tz_eduorg "
             	 + " where  parord = " + StringManager.makeSQL(gubun)
              	 + " order  by orgid";
              
              
              logger.info("����û���� : " + sql);
              ls = connMgr.executeQuery(sql);

              while (ls.next()) {
                  data = new CodeData();

                  data.setCode(ls.getString("code"));
                  data.setCodenm(ls.getString("codenm"));
                  
                  result += " <option value=\"" + data.getCode() + "\"";
                  
                  logger.info(" common select box  >> " + name + " : " + selected + " / " + data.getCode());
                  
                  
                  if (selected.equals(data.getCode())) {
                      result += " selected ";
                  }

                  result += ">" + data.getCodenm() + "</option> \n";

              }  
         }
          catch (Exception ex) {
              ErrorManager.getErrorStackTrace(ex);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
          }
          finally {
              if(ls != null) { try { ls.close(); }catch (Exception e) {} }
              if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
          }

          result += "  </select> \n";
          return result;
        
      }
    
}
