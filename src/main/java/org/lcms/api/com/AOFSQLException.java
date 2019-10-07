/**
* @(#)AOFSQLException.java
* description :  LCMS AofSqlException 처리에 대한 Class
* note : 
* date : 2008. 12. 31.
* @author : 4CSOFT-오일환.
* @version : 1.0.
* modified : 
* comment : 
...
*/
package org.lcms.api.com;

import java.sql.SQLException;

public class AOFSQLException extends AOFException {

	String queryid=null;
	String sql=null;
	 
	public AOFSQLException(SQLException e,String queryid,String sql){		
		super(e);
		this.queryid=queryid;
		this.sql=sql;
	}
	
	public AOFSQLException(SQLException e,String sql){		
		super(e);
		this.sql=sql;			
	}

	public AOFSQLException(Exception ex) {
		// TODO Auto-generated constructor stub
		super(ex);
	}

	/**
	 * @return Returns the queryid.
	 */
	public String getQueryid() {
		return queryid;
	}

	/**
	 * @param queryid The queryid to set.
	 */
	public void setQueryid(String queryid) {
		this.queryid = queryid;
	}

	/**
	 * @return Returns the sql.
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql The sql to set.
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}
}
