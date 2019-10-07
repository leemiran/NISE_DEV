
package com.ziaan.library;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;

  /**
 * <p> 제목: Error 관련 라이브러리</p> 
 * <p> 설명: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class ErrorManager { 
    private static String trace_delim = "\r\n\tat ";
    private static String msg_delim = ": ";
        
    /** 
    * stackTrace 를 Html 형식으로 정렬한다
    @param  stackTrace  stackTrace String을 인자로 받는다
    @return  result  Html 형식으로 정렬된 stackTrace 를 반환한다
    */
    public static String getHtmlLineup(String stackTrace) { 
        String result = "";

        if ( stackTrace != null ) { 
            //result = StringManager.replace(stackTrace, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            //result = StringManager.replace(stackTrace, "\r\n", "<br > ");	
            
            result = StringManager.replace(stackTrace, "<br>", "\r\n");
            result = StringManager.replace(stackTrace, "<br >", "\r\n");
        }

        return result;
    }
    
    /** 
    * Error 메시지(sql 포함)와 stackTrace 를 웹화면에서 보여준다
    @param  ex  Throwable 를 인자로 받는다
    @param  out  PrintWriter 를 인자로 받는다
    @param  str  sql 구문을 인자로 받는다
    */
    public static void getErrorStackTrace(Throwable ex, RequestBox box, String str) {      
        ByteArrayOutputStream baos = null;
        PrintStream ps = null;   
        String sql = str;
        PrintWriter out = null;

         try { 
            baos = new ByteArrayOutputStream();
            ps = new PrintStream(baos);
            ex.printStackTrace(ps);
            String error_msg = baos.toString(); 
            ConfigSet conf = new ConfigSet();
            if(conf.getProperty("error.massage.show").equals("N")) {
            	error_msg = "";
            }
            String html = "";
            
            if ( isErrorMessageView() ) { 
                out = (PrintWriter)box.getObject("errorout");
                if ( out != null ) { 
                    /*out.println("<HTML > ");
                    out.println("<HEAD > <TITLE > Compound JSPs</TITLE > </HEAD > ");
                    out.println("<BODY BGCOLOR=#C0C0C0 > ");
                    out.println("<H2 > Exception Occurred</H2 > ");
                    out.println("<FONT SIZE=2 > ");
                    out.println(sql + "<br > <br > ");
                    out.println(getHtmlLineup(error_msg));
                    out.println("</FONT > ");
                    out.println("</BODY > </HTML > ");*/
                    html="<html>\n"
                    +"<head>\n"
                    +"<title>알려드립니다.</title>\n"
                    +"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"
                    +"<link rel='stylesheet' href='/css/common.css' type='text/css'>\n"
                    +"</head>\n"
                    +"<body bgcolor='#FFFFFF' text='#000000' topmargin='0' leftmargin='0'>\n"
                    +"<table width='100%' border='0' cellspacing='0' cellpadding='0' height='100%'>\n"
                      +"<tr>\n"
                        +"<td align='center' valign='middle'>\n"
                          +"<table width='833' border='0' cellspacing='0' cellpadding='0' height='455'>\n"
                            +"<tr>\n"
                              +"<td background='/images/error/bg.gif' valign='top'>\n" 
                                +"<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"
                                  +"<tr>\n"
                                    +"<td width='368'>&nbsp;</td>\n"
                                    +"<td width='375'>\n"
                                      +"<table width='100%' border='0' cellspacing='0' cellpadding='0' height='455'>\n"
                                        +"<tr>\n"
                                          +"<td height='109'>&nbsp;</td>\n"
                                        +"</tr>\n"
                                        +"<tr>\n"
                                          +"<td valign='top' align='right'>\n" 
                                            +"<table width='97%' border='0' cellspacing='0' cellpadding='0'>\n"
                                              +"<tr>\n"
                                                +"<td height='13' class='title'>죄송합니다.<br>\n"
                                                  +"작업처리중 오류가 발생했습니다.</td>\n"
                                              +"</tr>\n"
                                              +"<tr>\n"
                                                +"<td>&nbsp;</td>\n"
                                              +"</tr>\n"
                                              +"<tr>\n" 
                                                +"<td class='title_s'>" + "[ " + FormatDate.getDate("yyyy년  MM월 dd일  HH시 mm분 ss초")+ "]" + "</td>\n"
                                              +"</tr>\n"
                                              +"<tr>\n" 
                                                +"<td>&nbsp;</td>\n"
                                              +"</tr>\n"
                                              +"<tr>\n" 
                                                +"<td><font size=2>동일한 문제가 지속적으로 발생할 경우, <br>운영자에게 문의하십시오.<p>"
                                                +"운영자 연락처 ) <br>"
                                                +"전화번호 : "+conf.getProperty("admin.tel")+"</font></td></td>\n"
                                              +"</tr>\n"
                                              +"<tr>\n"
                                                +"<td height='15'></td>\n"
                                              +"</tr>\n"
                                              +"<tr>\n" 
                                                +"<td><textarea style='display:none;'>\r\n" + getHtmlLineup(error_msg) +"\r\n</textarea></td>\n"
                                              +"</tr>\n"
                                              +"<tr>\n"
                                                +"<td  class='title_s'><a href='javascript:history.back()'>돌아가기</a>&gt;&gt;</td>\n"
                                              +"</tr>\n"
                                            +"</table>\n"
                                          +"</td>\n"
                                        +"</tr>\n"
                                      +"</table>\n"
                                    +"</td>\n"
                                    +"<td>&nbsp;</td>\n"
                                  +"</tr>\n"
                                +"</table>\n"
                              +"</td>\n"
                            +"</tr>\n"
                          +"</table>\n"
                        +"</td>\n"
                      +"</tr>\n"
                    +"</table>\n"
                    +"</body>\n"
                    +"</html>\n";
                    out.println(html);
                }
            } else { 
		Log.err.println(box, "Sql : " + sql, "StackTrace : " + error_msg);
           //     ex.printStackTrace();
            }
        } catch ( Exception e ) { 
	    Log.sys.println(box, "ErrorManager.getErrorStackTrace(Throwable ex, RequestBox box, String str) is critical error\r\n" + e.getMessage() );
         //   e.printStackTrace();
        } finally { 
            if ( ps != null ) { try { ps.close(); } catch ( Exception e1 ) { } }
            if ( baos != null ) { try { baos.close(); } catch ( Exception e1 ) { } }
            if ( out != null ) { try { out.close(); } catch ( Exception e1 ) { } }
        }
    }
    
    /** 
    * Error 메시지와 stackTrace 를 웹화면에서 보여준다
    @param  ex  Throwable 를 인자로 받는다
    @param  out  PrintWriter 를 인자로 받는다
    */
    public static void getErrorStackTrace(Throwable ex, PrintWriter out) {      
        ByteArrayOutputStream baos = null;
        PrintStream ps = null;   

         try { 
            baos = new ByteArrayOutputStream();
            ps = new PrintStream(baos);
            ex.printStackTrace(ps);
            String error_msg = baos.toString();
            ConfigSet conf = new ConfigSet();
            if(conf.getProperty("error.massage.show").equals("N")) {
            	error_msg = "";
            }
            String html="";
            
            if ( out != null ) { 
                if ( isErrorMessageView() ) { 
                    /*out.println("<HTML > ");
                    out.println("<HEAD > <TITLE > Compound JSPs</TITLE > </HEAD > ");
                    out.println("<BODY BGCOLOR=#C0C0C0 > ");
                    out.println("<H2 > Exception Occurred</H2 > ");
                    out.println("<FONT SIZE=2 > ");
                    out.println(getHtmlLineup(error_msg));
                    out.println("</FONT > ");
                    out.println("</BODY > </HTML > ");*/
                    html="<html>\n"
                        +"<head>\n"
                        +"<title>알려드립니다.</title>\n"
                        +"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"
                        +"<link rel='stylesheet' href='/css/common.css' type='text/css'>\n"
                        +"</head>\n"
                        +"<body bgcolor='#FFFFFF' text='#000000' topmargin='0' leftmargin='0'>\n"
                        +"<table width='100%' border='0' cellspacing='0' cellpadding='0' height='100%'>\n"
                          +"<tr>\n"
                            +"<td align='center' valign='middle'>\n"
                              +"<table width='833' border='0' cellspacing='0' cellpadding='0' height='455'>\n"
                                +"<tr>\n"
                                  +"<td background='/images/error/bg.gif' valign='top'>\n" 
                                    +"<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"
                                      +"<tr>\n"
                                        +"<td width='368'>&nbsp;</td>\n"
                                        +"<td width='375'>\n"
                                          +"<table width='100%' border='0' cellspacing='0' cellpadding='0' height='455'>\n"
                                            +"<tr>\n"
                                              +"<td height='109'>&nbsp;</td>\n"
                                            +"</tr>\n"
                                            +"<tr>\n"
                                              +"<td valign='top' align='right'>\n" 
                                                +"<table width='97%' border='0' cellspacing='0' cellpadding='0'>\n"
                                                  +"<tr>\n"
                                                    +"<td height='13' class='title'>죄송합니다.<br>\n"
                                                      +"작업처리중 오류가 발생했습니다.</td>\n"
                                                  +"</tr>\n"
                                                  +"<tr>\n"
                                                    +"<td>&nbsp;</td>\n"
                                                  +"</tr>\n"
                                                  +"<tr>\n" 
                                                    +"<td class='title_s'>" + "[ " + FormatDate.getDate("yyyy년  MM월 dd일  HH시 mm분 ss초")+ "]" + "</td>\n"
                                                  +"</tr>\n"
                                                  +"<tr>\n" 
                                                    +"<td>&nbsp;</td>\n"
                                                  +"</tr>\n"
                                                  +"<tr>\n" 
                                                  +"<td><font size=2>동일한 문제가 지속적으로 발생할 경우, <br>운영자에게 문의하십시오.<p>"
                                                  +"운영자 연락처 ) <br>"
                                                  +"전화번호 : "+conf.getProperty("admin.tel")+"</td></td>\n"
                                                  +"</tr>\n"
                                                  +"<tr>\n"
                                                    +"<td height='15'></td>\n"
                                                  +"</tr>\n"
                                                  +"<tr>\n" 
                                                    +"<td><textarea style='display:none;'>\r\n" + getHtmlLineup(error_msg) +"\r\n</textarea></td>\n"
                                                  +"</tr>\n"
                                                  +"<tr>\n"
                                                    +"<td  class='title_s'><a href='javascript:history.back()'>돌아가기</a>&gt;&gt;</td>\n"
                                                  +"</tr>\n"
                                                +"</table>\n"
                                              +"</td>\n"
                                            +"</tr>\n"
                                          +"</table>\n"
                                        +"</td>\n"
                                        +"<td>&nbsp;</td>\n"
                                      +"</tr>\n"
                                    +"</table>\n"
                                  +"</td>\n"
                                +"</tr>\n"
                              +"</table>\n"
                            +"</td>\n"
                          +"</tr>\n"
                        +"</table>\n"
                        +"</body>\n"
                        +"</html>\n";
                        out.println(html);
                }
                else { 
                    out.println("<html > <head > ");
              //      out.println("<head > <script language = 'javascript' > ");
              //      out.println("alert('시스템 점검중입니다. 관리자에게 문의하시기 바랍니다. tel) 2704')");
              //     out.println("history.back(-1);");
              //      out.println("</script > ");
                    out.println("</head > ");
                    
                    out.println("<body onload='javascript:document.errform.submit()' > ");
                    out.println("<form name=errform action='/portal/include/printSystemErrorMessage.jsp' method=post > ");
                    out.println("</body > ");
                    
                    out.println("</html > ");
                    Log.err.println("StackTrace : " + error_msg);
              //      ex.printStackTrace();
                }
            }          
        } catch ( Exception e ) { 
            Log.sys.println("ErrorManager.getErrorStackTrace(Throwable ex, PrintWriter out) is critical error\r\n" + e.getMessage() );
         //   e.printStackTrace();
        } finally { 
            if ( ps != null ) { try { ps.close(); } catch ( Exception e1 ) { } }
            if ( baos != null ) { try { baos.close(); } catch ( Exception e1 ) { } }
            if ( out != null ) { try { out.close(); } catch ( Exception e1 ) { } }
        }
    }   

    /** 
    * Error 메시지와 stackTrace 를 웹화면 or 콘솔에서 보여준다
    @param  ex  Throwable 를 인자로 받는다
    @param  isHtml  웹화면에서 보여줄지 여부를 설정한다.
    @return  error_msg  에러메시지를 리턴한다.
    */       
    public static String getErrorStackTrace(Throwable ex, boolean isHtml) {      
         ByteArrayOutputStream baos = null;
         PrintStream ps = null;   
         String error_msg = "";

         try { 
            baos = new ByteArrayOutputStream();
            ps = new PrintStream(baos);
            ex.printStackTrace(ps);
            error_msg = baos.toString(); 
 
            Log.err.println("StackTrace : " + error_msg); 
            
            if ( isHtml) error_msg = getHtmlLineup(error_msg);                      
        } catch ( Exception e ) { 
            Log.sys.println("ErrorManager.getErrorStackTrace(Throwable ex, boolean isHtml) is critical error\r\n" + e.getMessage() );
         //   e.printStackTrace();
        }

        return error_msg;
    }

    /** 
    * Error 메시지와 stackTrace 를  보여준다
    @param  ex  Throwable 를 인자로 받는다
    */         
    public static void getErrorStackTrace(Throwable ex) {      
         ByteArrayOutputStream baos = null;
         PrintStream ps = null;   
         String error_msg = "";

         try { 
            baos = new ByteArrayOutputStream();
            ps = new PrintStream(baos);
            ex.printStackTrace(ps);
            error_msg = baos.toString();     
            Log.err.println("StackTrace : " + error_msg);           
        } catch ( Exception e ) { 
            Log.sys.println("ErrorManager.getErrorStackTrace(Throwable ex) is critical error\r\n" + e.getMessage() );
         //   e.printStackTrace();
        }
    }

    /** 
    * RequestBox 를 인자로 받아 box 안에 담긴 key, value 를 console 에 찍는다.
    @param  box  RequestBox
    */     
    public static void systemOutPrintln(RequestBox box) throws Exception {   
        try { 
            if ( isErrorMessageView() ) { 
                Enumeration e = box.keys();
        
                while ( e.hasMoreElements() ) { 
                    String key = (String)e.nextElement();
                    
                    String value = box.get(key);
                    
                    System.out.println(key + " : " + value);
                }   
            }
        } catch ( Exception e ) { 
            Log.sys.println("ErrorManager.systemOutPrintln(RequestBox box) is critical error\r\n" + e.getMessage() );
         //   e.printStackTrace();
        }
    }
    
    public static boolean isErrorMessageView() throws Exception {       
        boolean result = false;
        try { 
            ConfigSet conf = new ConfigSet();
            String isMessage = conf.getProperty("error.message.view");
            if ( isMessage.equals("true")) result = true;
            else result = false;
        } catch ( Exception ex ) { 
            throw new Exception("ErrorManager.isErrorMessageView()\r\n" +ex.getMessage() );
        }

        return result;
    }
}