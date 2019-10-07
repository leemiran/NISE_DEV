
package com.ziaan.library;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

 /**
 * <p> 제목: DB Connection 관련 라이브러리</p> 
 * <p> 설명: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class DBConnectionManager { 
    private String default_pool_name = null;
    private Connection conn = null;
    private Context env= null;
    private DataSource source= null;
    private static int count = 0;
    private InitialContext initCtx = null;
    private String start = "";
    
	
    /** 
    * 생성자 함수 호출시 pool 에서 Connection 객체를 얻는다
    */
    public DBConnectionManager() throws Exception {    	
    	this.setDefaultPoolName();
    	this.initialize(default_pool_name);
    }
    
    /** 
    * 생성자 함수 호출시 pool 에서 Connection 객체를 얻는다
    */
    public DBConnectionManager(String poolName) throws Exception { 
	default_pool_name = poolName;
	this.initialize(default_pool_name);
    }
    
    
    public Connection getConnection() { 
    	return this.conn;
    }
    
    public void setDefaultPoolName() throws Exception{ 
        ConfigSet conf = new ConfigSet();
        try { 
            if ( default_pool_name == null ) {                
                default_pool_name = conf.getProperty("pool.name");
            }
        } catch ( Exception e ) { e.printStackTrace(); }
    }
    
    private void initialize(String poolName) throws Exception { 
        try {       
        	 	System.out.println("poolName " + poolName);
            // ------------- Interstage -----------------------------------------------------------------------
//            env=(Context) new InitialContext();
//            source=(DataSource)env.lookup("java:comp/env/jdbc/" +poolName);
            
            // ------------- resin -----------------------------------------------------------------------
			env=(Context) new InitialContext().lookup("java:comp/env");
			source=(DataSource)env.lookup("jdbc/" +poolName);
            
            // -------------- weblogic ---------------------------------------------------------------------
//            initCtx = new InitialContext();
//		    source = (DataSource)initCtx.lookup(poolName);
            // ----------------------------------------------------------------------------------------------------
            conn = source.getConnection();

            count++;
            start = FormatDate.getDate("yyyyMMddHHmmss");
        } 
        catch ( Exception ex ) { 
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.initialize(\"" + poolName + "\")");          
            ex.printStackTrace();
        }		
    }
    
    /** 
    * Connection 의 유효여부를 반환한다
    @return  conn  Connection 객체를 반환한다
    */
    public boolean valid() { 
        return ( conn != null );
    }
    
    /** 
    * 컨넥션풀로 Connection 객체를 돌려준다
    */
    public void freeConnection() throws Exception {        
        if ( conn != null ) try { conn.close(); } catch ( Exception ex ) { }        
        count--;
//        System.out.println("freeConn : " + count + " | " + FormatDate.getDate("yyyy.MM.dd HH:mm:ss") + " | " + FormatDate.getSecDifference(start, FormatDate.getDate("yyyyMMddHHmmss")));
        conn = null;
    }
    
    /** 
    * DatabaseMetaData 객체를 반환한다
    @return  md  DatabaseMetaData 객체를 반환한다
    */
    public DatabaseMetaData getMetaData() throws Exception { 
        DatabaseMetaData md = null ;        
        try { 
            md = conn.getMetaData();
        } catch (SQLException ex) { 
            throw new Exception("DBConnectionManager.getMetaData()\r\n" +ex.getMessage() );
        }      
        return md;
    }
    
    /** 
    * DB 트랜잭션의 AutoCommit 여부를 설정한다.
    @param autoCommit   AutoCommit 여부를 설정
    */
    public void setAutoCommit(boolean autoCommit) throws Exception {  
        try { 
            conn.setAutoCommit(autoCommit);
        } catch (SQLException ex) { 
            throw new Exception("DBConnectionManager.setAutoCommit()\r\n" +ex.getMessage() );
        }      
    }
    
    /** 
    * SQL 실행문을 commit 한다.
    */
    public void commit() throws Exception {   
        try { 
            conn.commit();
        } catch (SQLException ex) { 
            throw new Exception("DBConnectionManager.commit()\r\n" +ex.getMessage() );
        }      
    }
    
    /** 
    * SQL 실행문을 rollback 한다.
    */
    public void rollback() throws Exception {   
        try { 
            conn.rollback();
        } catch (SQLException ex) { 
            throw new Exception("DBConnectionManager.rollback()\r\n" +ex.getMessage() );
        }      
    }
    
     /** 
    * 데이터 조회시 Statement, ResultSet 객체를 생성하고 제어하는 ListSet 객체를 반환한다
    @param  sql  쿼리를 인자로 받는다
    @return  ls  ListSet 객체를 반환한다
    */
    public ListSet executeQuery(String sql) throws Exception { 
        ListSet             ls      = null;
        try { 
            ls = new ListSet(conn);     // Statement 객체를 생성
            ls.run(sql);    //  ResultSet 객체를 생성
        } catch ( Exception ex ) { 
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.executeQuery(String sql)" + sql);          
            throw new Exception("DBConnectionManager.executeQuery()\r\n\"" +sql + "\"\r\n" +ex.getMessage() );
        }

        return ls;
    }
        
    /** 
    * Statement 객체를 생성하고 주어진 쿼리에따라 입력, 수정, 삭제를 실행하고 row 수를 반환한다.  Statement 객체를 닫는다
    @param  sql  쿼리를 인자로 받는다
    @return  result  int형 DB의 실행된 row 수를 반환한다
    */
    public int executeUpdate(String sql) throws Exception { 
        int result = 0;     
        Statement stmt = null;
        
        try { 
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            result = stmt.executeUpdate(sql);
        } catch (SQLException ex) {         
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.executeUpdate()" + sql);             
            throw new Exception("DBConnectionManager.executeUpdate()\r\n\"" +sql + "\"\r\n" +ex.getMessage() );
        } finally { 
            if ( stmt != null ) try { stmt.close(); } catch (SQLException ex) { }
        }

        return result;
    }
    
     /** 
    * Statement 객체를 생성하고 반환한다
    */
    public Statement createStatement() throws Exception { 
        Statement stmt = null;
        try { 
            stmt = conn.createStatement();
        } catch ( Exception ex ) { 
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.createStatement()");        
            throw new Exception("DBConnectionManager.createStatement()\r\n" +ex.getMessage() );
        }
       return stmt;
    }    
    
    /** 
    * PreparedStatement 객체를 생성하고 반환한다
    @param  sql  쿼리를 인자로 받는다
    */
    public PreparedStatement prepareStatement(String sql) throws Exception { 
        PreparedStatement   pstmt   = null;
        try { 
            pstmt = conn.prepareStatement(sql);
        } catch ( Exception ex ) { 
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.prepareStatement(String sql)" + sql);      
            throw new Exception("DBConnectionManager.prepareStatement(\"" +sql + "\")\r\n" +ex.getMessage() );
        }
       return pstmt;
    }    
    
    /** 
    * PreparedStatement 객체를 생성하고 반환한다
    @param  sql  쿼리를 인자로 받는다
    */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws Exception { 
        PreparedStatement   pstmt   = null;
        try { 
            pstmt = conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
        } catch ( Exception ex ) { 
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.prepareStatement(String sql)" + sql);    
            throw new Exception("DBConnectionManager.prepareStatement(\"" +sql + "\")\r\n" +ex.getMessage() );
        }
       return pstmt;
    }   
    
     /** 
    * PreparedStatement 객체에 Oracle의 clob형 Field를 저장할때 사용한다.(Oracle 9i or Weblogic 6.1 인 경우)
    @param  p_pstmt  PreparedStatement 객체를 인자로 받는다
    @param  index  index 를 인자로 받는다
    @param  data  String형 data 를 인자로 받는다
    */
    public void setCharacterStream(PreparedStatement p_pstmt, int index, String data) throws Exception { 
        StringReader sreader = new StringReader(data);
        try { 
            p_pstmt.setCharacterStream(index, sreader, data.length() );
        } catch ( Exception ex ) { 
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.setCharacterStream(" +index + ",\"" +data + "\")");   
            throw new Exception("DBConnectionManager.setCharacterStream(" +index + ",\"" +data + "\")\r\n" +ex.getMessage() );
        }
        finally{ 
            if ( sreader != null ) { try { sreader.close(); } catch ( Exception e ) { } }
        }
    }      
    
     /** 
    * PreparedStatement 객체에 Oracle의 blob형 Field를 저장할때 사용한다.(Oracle 9i or Weblogic 6.1 인 경우)
    @param  p_pstmt  PreparedStatement 객체를 인자로 받는다
    @param  index  index 를 인자로 받는다
    @param  data  String형 data 를 인자로 받는다
    */
    public void setBinaryStream(PreparedStatement p_pstmt, int index, String data) throws Exception { 
        byte [] buf = data.getBytes();// System.out.println("buf : " + buf.length);   
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);// System.out.println("bais : " + bais);   
        try { 
            p_pstmt.setBinaryStream(index, bais, buf.length);
        } catch ( Exception ex ) { 
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.setBinaryStream(" +index + ",\"" +data + "\")");   
            throw new Exception("DBConnectionManager.setBinaryStream(" +index + ",\"" +data + "\")\r\n" +ex.getMessage() );
        }
        finally{ 
            if ( bais != null ) { try { bais.close(); } catch ( Exception e ) { } }
        }
    }
    
     /** 
    * PreparedStatement 객체에 Oracle의 blob형 Field를 저장할때 사용한다.(Oracle Jdbc driver 인 경우, Weblogic 제외)
    @param  sql  SQL문
    @param  p_content  String형 data 를 인자로 받는다
    */
   public void setOracleBLOB(String sql, String p_content) throws Exception {            //  (Oracle Jdbc driver 인 경우)
        Statement stmt = null;
        ResultSet rset = null;
        BLOB blob = null;
        ByteArrayInputStream instream = null;
        OutputStream outstream = null;
        
        try { 
            stmt = conn.createStatement();

            rset = stmt.executeQuery (sql);
            rset.next();
            
            // Get the BLOB locator.
            blob = ((OracleResultSet)rset).getBLOB(1);
                        
            byte [] buf = p_content.getBytes();
            instream = new ByteArrayInputStream(buf);      
            
            // Insert to the BLOB from an output stream.
            outstream = blob.getBinaryOutputStream();
            
            // Read the input stream and write the output stream by chunks.
            byte[] chunk = new byte[blob.getChunkSize()];
            int i=-1;

            while ( (i = instream.read(chunk)) != -1)
            { outstream.write(chunk, 0, i); }
        } catch ( Exception ex ) { 
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.setOracleBLOB()" + sql);   
            throw new Exception("DBConnectionManager.setOracleBLOB()\r\n" +ex.getMessage() );
        }
        finally{ 
            if ( instream != null ) { try { instream.close(); } catch ( Exception e ) { } }
            if ( outstream != null ) { try { outstream.close(); } catch ( Exception e ) { } }
            if ( stmt != null ) { try { stmt.close(); } catch ( Exception e ) { } }
            if ( rset != null ) { try { rset.close(); } catch ( Exception e ) { } }
        }
    }    
    
    /** 
    * PreparedStatement 객체에 Oracle의 clob형 Field를 저장할때 사용한다.(Oracle Jdbc driver 인 경우, Weblogic 제외)
    @param  sql  SQL문
    @param  p_content  String형 data 를 인자로 받는다
    */
    public void setOracleCLOB(String sql, String p_content) throws Exception {            // (Oracle Jdbc driver 인 경우)
        Statement stmt = null;
        ResultSet rset = null;
        StringReader sreader = null;
        Writer writer = null;
        OutputStream outstream = null;
        
        try { 
            stmt = conn.createStatement();

            rset = stmt.executeQuery (sql);
            rset.next();
            
            // Get the CLOB locator.
            CLOB clob = (oracle.sql.CLOB) rset.getClob(1);
            
            sreader = new StringReader(p_content);
            
            writer = clob.getCharacterOutputStream();
                        
            char [] chunk = new char[clob.getChunkSize()];
            int i=-1;
            while ( (i = sreader.read(chunk)) != -1 ) { 
                writer.write(chunk, 0, i);
            }
        } catch ( Exception ex ) { ex.printStackTrace();
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.setOracleCLOB()" + sql); 
            throw new Exception("DBConnectionManager.setOracleCLOB()\r\n" +ex.getMessage() );
        }
        finally{ 
            if ( sreader != null ) { try { sreader.close(); } catch ( Exception e ) { } }
            if ( writer != null ) { try { writer.close(); } catch ( Exception e ) { } }
            if ( stmt != null ) { try { stmt.close(); } catch ( Exception e ) { } }
            if ( rset != null ) { try { rset.close(); } catch ( Exception e ) { } }
        }
    }    
    
    /** 
    * PreparedStatement 객체에 Oracle의 clob형 Field를 저장할때 사용한다.(Weblogic Jdbc driver 인 경우)
    @param  sql  SQL문
    @param  p_content  String형 data 를 인자로 받는다
    */
    public void setWeblogicCLOB(String sql, String p_content) throws Exception {            // (Weblogic Jdbc driver 인 경우)
        Statement stmt = null;
        ResultSet rset = null;
        oracle.sql.CLOB clob = null;
        StringReader sreader = null;
        Writer writer = null;

        try { 
            stmt = conn.createStatement();
            
            rset = stmt.executeQuery (sql);
            rset.next();
            
            clob = (oracle.sql.CLOB)rset.getClob(1);
            
      /*      sreader = new StringReader(p_content);
            
            writer = clob.getCharacterOutputStream();

            char [] buffer = new char [1024];
            int read = 0;
            while ( (read = sreader.read(buffer)) != -1) { 
                writer.write(buffer, 0, read);
            }
            sreader.close();
            writer.close();*/
            
            writer = clob.getCharacterOutputStream();
            
            char[] b = p_content.toCharArray(); // converts 's' to a character array

            writer.write(b);
            writer.flush();           

            writer.close();
        } catch ( Exception ex ) { ex.printStackTrace();
            // Log.sys.println(this, ex, "Happen to DBConnectionManager.setWeblogicCLOB()" + sql); 
            throw new Exception("DBConnectionManager.setWeblogicCLOB()\r\n" +ex.getMessage() );
        }
        finally{ 
            if ( sreader != null ) { try { sreader.close(); } catch ( Exception e ) { } }
            if ( writer != null ) { try { writer.close(); } catch ( Exception e ) { } }
            if ( stmt != null ) { try { stmt.close(); } catch ( Exception e ) { } }
            if ( rset != null ) { try { rset.close(); } catch ( Exception e ) { } }
        }
    }  
}
		

