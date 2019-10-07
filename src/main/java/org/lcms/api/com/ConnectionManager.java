/**
* @(#)ConnectionManager.java
* description :  LCMS Connection 관리 Class
* note : 
* date : 2008. 12. 31.
* @author : 4CSOFT-오일환.
* @version : 1.0.
* modified : 
* comment : 
...
*/

package org.lcms.api.com;

import java.sql.Connection;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public abstract class ConnectionManager {

	InitialContext initContext = null;
	DataSource ds = null;
	
	
	public static Hashtable connectionManagerHashtable=new Hashtable();
	
	public String dataSource=null; 
	/**
	 * description: Connection get.
	 * @return Connection
	 * @throws Exception
	 */	
	protected Connection getConnection() throws Exception{
		return getConnection(dataSource);
	}	
	/**
	 * description: Connection get.
	 * @return Connection
	 * @throws Exception
	 */	
	protected abstract Connection getConnection(String jndiName) throws Exception;
	/**
	 * description: dataSource get.
	 * @return String dataSource
	 * @throws Exception
	 */	
	public String getDataSource() {
		return dataSource;
	}
	/**
	 * description: dataSource set.
	 * @return 
	 * @throws Exception
	 */	
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * description: default ConnectionManager get.
	 * @return ConnectionManager
	 * @throws Exception
	 */	
	public static ConnectionManager getDefaultConnectionManager(){
		return (ConnectionManager)connectionManagerHashtable.get("default");
	}
	/**
	 * description: datasource ConnectionManager get.
	 * @return ConnectionManager
	 * @throws Exception
	 */	
	public static ConnectionManager getConnectionManager(String datasource){
		return (ConnectionManager)connectionManagerHashtable.get(datasource);
	}		
}