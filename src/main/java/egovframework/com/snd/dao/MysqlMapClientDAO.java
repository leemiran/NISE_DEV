package egovframework.com.snd.dao;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

public class MysqlMapClientDAO extends EgovAbstractDAO {
    /**
     * DB별 sqlMapClient 지정	
     */
    @Resource(name = "mysqlSqlMapClient")
    public void setSuperSqlMapClient(SqlMapClient sqlMapClient) {
        super.setSuperSqlMapClient(sqlMapClient);
    }
}