// **********************************************************
//  1. 제      목: 코드 관리
//  2. 프로그램명 : CodeConfigBean.java
//  3. 개      요: 코드 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7.  14
//  7. 수      정:
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
 * 코드 관리(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class CodeConfigBean { 
	
	public static Logger logger = Logger.getLogger(CodeConfigBean.class);
	
    public CodeConfigBean() { }



    /**
    * 숫자앞에 0 으로 채우기,  (숫자,길이)
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
    *  컨피그 데이타값 (데이타명)
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
    *  코드 셀렉트박스 (config명,셀렉트박스명,선택값) 3
    */
    public static String getCodeSelect (String config, String name, String selected) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, "", 0, "");
    }

    /**
    *  코드 셀렉트박스 (config명,셀렉트박스명,선택값,전체유무) 4
    */
    public static String getCodeSelect (String config, String name, String selected, int allcheck) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, "", allcheck, "");
        /**(config명(0),코드,레벨,셀렉트박스명(0),선택값(0),이벤트명,전체유무(0)) **/
    }

    /**
    *  코드 셀렉트박스 (config명,코드,레벨,셀렉트박스명,이벤트명,선택값) 5
    */
    public static String getCodeSelect (String config, String code, int levels, String name, String selected) throws Exception { 
        return getCodeSelect (config, code, levels, name, selected, "",0, "");
    }

    /**
    *  코드 셀렉트박스 (config명,코드,레벨,셀렉트박스명,선택값,이벤트명,전체유무) 6
    */
    public static String getCodeSelect (String config, String code, int levels, String name, String selected, int allcheck) throws Exception { 
        return getCodeSelect (config, code, levels, name, selected, "", allcheck, "");
    }

    /**
    *  코드 셀렉트박스 (config명,코드,셀렉트박스명,선택값,이벤트명) 4
    */
    public static String getCodeSelect (String config, String name, String selected, String event) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, event, 0, "");
    }

    /**
    *  코드 셀렉트박스 (config명,코드,셀렉트박스명,선택값,이벤트명,전체유무) 5
    */
    public static String getCodeSelect (String config, String name, String selected, String event, int allcheck) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, event, allcheck, "");
    }

    /**
    *  코드 셀렉트박스 (config명, 셀렉트박스명, 선택값, 이벤트명, 전체유무, 부분출력)6
    */
    public static String getCodeSelect (String config, String name, String selected, String event, int allcheck, String aPart) throws Exception { 
        return getCodeSelect (config, "", 1, name, selected, event, allcheck, aPart);
    }

    /**
    *  코드 셀렉트박스 (config명,코드,레벨,셀렉트박스명,선택값,이벤트명,일부출력) 7
      *  TZ_CONFIG 값 이용
    *  allcheck값 전체여부 : 0 -전체없음, 1 - 전체있음, 2 - ALL
    *  aPart값(2자리) -- > 두자리수 사이에 있는 코드값을 return한다.
    */

    public static String getCodeSelect (String config, String code, int levels, String name, String selected, String event, int allcheck, String aPart) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";
        CodeData data = null;
        String gubun = getConfigValue(config);   // TZ_CONFIG 테이블에서 값을 얻어온다.
        String fChar = "";
        String sChar = "";

        result = "  <SELECT name=\"" + name + "\" name=\"" + name + "\" " + event + " > \n";

        if ( allcheck == 1) { 
          result += " <option value='' >== =전체 == =</option > \n";
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
    *  코드 셀렉트박스 (GUBUN,코드,레벨,셀렉트박스명,선택값,이벤트명,전체유무)
      *  TU_CODEGUBUN 값 이용
    *  전체여부 : 0 -전체없음, 1 - 전체있음, 2 - ALL, 3 - 선택
    */
    public static String getCodeGubunSelect (String gubun, String code, int levels, String name, String selected, String event, int allcheck) throws Exception {
       return getCodeGubunSelect (gubun, code, levels, name, selected, event, allcheck, null);
    }
    
    
    /**
    *  코드 셀렉트박스 (GUBUN,코드,레벨,셀렉트박스명,선택값,이벤트명,전체유무)
      *  TU_CODEGUBUN 값 이용
    *  전체여부 : 0 -전체없음, 1 - 전체있음, 2 - ALL, 3 - 선택
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
          result += " <option value=''>=== 전체 ===</option> \n";
        } else if (allcheck == 2) {
            result += " <option value='ALL'>ALL</option> \n";
        } else if (allcheck == 3) {
            result += " <option value=''>선택</option> \n";
        } else if (allcheck == 4) {
        	result += " <option value=''>직접입력</option> \n";
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
     *  코드 라디오버튼 (GUBUN,코드,레벨,라이오버튼명,선택값,이벤트명,전체유무)
       *  TZ_CODEGUBUN 값 이용
     *  전체여부 : 0 -전체없음, 1 - 전체있음, 2 - ALL
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
    *  코드구분명 (config명)
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
    *  코드명 (config명,코드)
    */
    public static String getCodeName (String config, String code) throws Exception {
        return getCodeName (config, code, 1);
    }

    /**
    *  코드명 (config명,코드,레벨)
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
     *  코드 갯수 (config명,상위코드,레벨)
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
     *  코드 리스트 (config명,상위코드,레벨)
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
     * 코드디폴트값(첫번째 코드값)
     * @param config     config명
     * @param code       상위코드
     * @param levels     레벨
     * @return result    코드값
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
     *  CB-HRD에서 사용하는 공통코드 셀렉트박스 (GUBUN, 셀렉트박스명,선택값,이벤트명,전체유무)
     *  전체여부 : 0 -전체없음, 1 - 전체있음, 2 - ALL, 3 - 선택
     *  김미향 2008.11.12
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
           result += " <option value=\"\">=== 전체 ===</option> \n";
         } else if (allcheck == 2) {
             result += " <option value=\"ALL\">ALL</option> \n";
         } else if (allcheck == 3) {
             result += " <option value=\"\">선택</option> \n";
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
      *  회원정보 교원에서 사용하는 공통코드 셀렉트박스 (GUBUN, 셀렉트박스명,선택값,이벤트명,전체유무)
      *  전체여부 : 0 -전체없음, 1 - 전체있음, 2 - ALL, 3 - 선택
  
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
            result += " <option value=\"\">=== 전체 ===</option> \n";
          } else if (allcheck == 2) {
              result += " <option value=\"ALL\">ALL</option> \n";
          } else if (allcheck == 3) {
              result += " <option value=\"\">선택</option> \n";
          }

          try {
     
              connMgr = new DBConnectionManager();
              	
              sql = " select orgid as code, org_nm as codenm "
             	 + " from   tz_eduorg "
             	 + " where  parord = " + StringManager.makeSQL(gubun)
              	 + " order  by orgid";
              
              
              logger.info("교육청정보 : " + sql);
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
