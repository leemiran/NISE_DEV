/**
* @(#) AbstractModel.java
* description : LCMS Connection 연결Class
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

public abstract class AbstractModel {
	
	private int totalCount = 0;
	
	/**
	 * description: connection 가져오기.
	 * @return connection
	 * @throws Exception
	 */	
	protected Connection getConnection() throws Exception{
		return ConnectionManager.getDefaultConnectionManager().getConnection();				
	}
	/**
	 * description: connection 가져오기.
	 * @return connection
	 * @throws Exception
	 */		
	protected Connection getConnection(String jndiName) throws Exception{
		return ConnectionManager.getConnectionManager(jndiName).getConnection();						
	}
	/**
	 * @param totalCnt The totalCnt to get.
	 */	
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCnt The totalCnt to set.
	 */
	protected void setTotalCount(int totalCnt) {
		this.totalCount = totalCnt;
	}
}
