
package com.ziaan.library;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

 /**
 * <p> 제목: System 에러 로그관련 라이브러리</p> 
 * <p> 설명: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class LogSysWriter { 
    private static PrintWriter writer = null;
    private final static Object lock = new Object();
    private static String today = null;
    private static boolean newLined = true;	
	
    public LogSysWriter() { 
	synchronized ( lock ) { 
	    checkDate();
	}
    }
    
    /** 
    * 현재날짜의 파일존재 여부를 확인하여 없으면 현재날짜의 로그 파일을 생성한다.
    */
    private static void checkDate() {   
        try { 
            String day = FormatDate.getDate("yyyyMMdd");
            if ( day.equals(today) ) return;
                       
            if ( writer != null ) { 
                try { writer.close(); } catch ( Exception e ) { }
                writer = null;
            }
            today = day;
            String logname = today + ".log" ;
            ConfigSet conf = new ConfigSet();
            String directory = conf.getProperty("log.dir.sys");
            java.io.File file = new java.io.File(directory, logname);
            String filename = file.getAbsolutePath();
            java.io.FileWriter fw =  new java.io.FileWriter(filename, true);// APPEND MODE
            writer = new PrintWriter(
            new java.io.BufferedWriter(fw), 
            new Boolean(conf.getProperty("log.autoflush")).booleanValue()  // AUTO Flush
            );
        } catch( Exception e ) { 
            e.printStackTrace();
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
            writer.println("Can't open log file : " + e.getMessage() );
            writer.println("Log will be printed into System.out");
        }
    }

    /** 
    * 환경설정파일에서 시스템로그를 생성할것인지 여부를 확인한다.
    @return  isPrintable   로그생성여부 
    */    
    private boolean isPrintMode() { 
        boolean isPrintable = true;
        try { 
            ConfigSet conf = new ConfigSet();
            isPrintable = new Boolean(conf.getProperty("log.sys.trace")).booleanValue();
        } catch( Exception e ) { 
            e.printStackTrace();
        }

        return isPrintable;
    }
    
    /** 
    * 로그생성시의 시간 
    */
    private void printTime() { 
        try { 
            checkDate();
            writer.write(FormatDate.getDate("HH:mm:ss") +' ') ;
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /** 로그를 해당 로그파일에 적는다. 
    @param  Object servletName  서블릿명을 담은 오브젝트 
    @param  Exception e
    @param  String msg  메시지 로그
    */
    public void println(Object servletName, Exception e, String msg) { 
        if ( ! isPrintMode() ) return;
        synchronized ( lock ) { 
            if ( newLined ) printTime();
                writer.println(getPrefixInfo(servletName));
                writer.print(ErrorManager.getErrorStackTrace(e, false));
                writer.println(msg);
                writer.println("");
                newLined = true;
        }
    }

    /** 로그를 해당 로그파일에 적는다. 
    @param  Object servletName  서블릿명을 담은 오브젝트 
    @param  Exception e
    */
    public void println(Object servletName, Exception e) { 
        if ( ! isPrintMode() ) return;
        synchronized ( lock ) { 
        if ( newLined ) printTime();
            writer.println(getPrefixInfo(servletName));
            writer.println(ErrorManager.getErrorStackTrace(e, false));
            newLined = true;
        }
    }
 
     /** 로그를 해당 로그파일에 적는다. 
    @param  Object userid  Userid를 담은 오브젝트
    @param  String msg  메시지 로그
    */   
    public void println(Object userid, String msg) { 
        if ( ! isPrintMode() ) return;
        synchronized ( lock ) { 
        if ( newLined ) printTime();
            writer.println(getValueInfo(userid));
            writer.println("  " + msg);
            newLined = true;
        }
    }
 
     /** 로그를 해당 로그파일에 적는다. 
    @param  Exception e  
    @param  String msg  메시지 로그
    */   
    public void println(Exception q, String msg) { 
        if ( ! isPrintMode() ) return;
        synchronized ( lock ) { 
        if ( newLined ) printTime();
            writer.println("");
            writer.print(ErrorManager.getErrorStackTrace(q, false));
            writer.println(msg);
            writer.println("");
            newLined = true;
        }
    }

     /** 로그를 해당 로그파일에 적는다. 
    @param  String msg  메시지 로그
    */ 
    public void println(String msg) { 
        if ( ! isPrintMode() ) return;
        synchronized (lock) { 
        if ( newLined ) printTime();
            writer.println(msg);
            newLined = true;
        }
    }
    
    protected String getPrefixInfo(Object o) { 
        StringBuffer info = new StringBuffer();
        info.append('[');
        
        if ( o == null ) { 
            info.append("null");
        }
        else { 
            Class c = o.getClass();
            String fullname = c.getName();
            info.append(fullname + "] " );
        }
        
        return info.toString();
    }
    
    protected String getValueInfo(Object o) { 
        int interval = 0;
        StringBuffer info = new StringBuffer();
        
        try { 
        
            if ( o == null ) { 
                info.append("null");
            } 
            else if ( o instanceof RequestBox ) { 
                RequestBox box = (RequestBox)o;                               
                
                String user = box.getSession("userid");
                if ( user != null ) info.append(user + "] ");
            } else { 
                Class c = o.getClass();
                String fullname = c.getName();
                String name = null;
                int index = fullname.lastIndexOf('.');
                if ( index == -1 ) name = fullname;
                else name = fullname.substring(index +1);
                info.append(name);
            }
        } catch ( Exception ex ) { 
            ex.printStackTrace();
        }

        return info.toString();
    }
}
